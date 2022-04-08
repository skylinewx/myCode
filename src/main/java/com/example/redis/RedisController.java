package com.example.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private static final Logger logger = LoggerFactory.getLogger("reimbursement");

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private ConcurrentHashMap<String,Object> lockMap = new ConcurrentHashMap<>();

    @GetMapping("/getFromRedis/{key}")
    public Object getFromRedis(@PathVariable String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 报销请求，没有锁的保护，在单机多线程的情况下会出现超额报销的问题，<br/>
     * 比如一共5000块，实际报销了1000块，但是依旧剩余4000多的问题
     *
     * @param vo 请求参数
     * @return
     */
    @PostMapping("/reimbursement")
    public String reimbursement(@RequestBody ReimbursementVO vo) {
        String jineStr = stringRedisTemplate.opsForValue().get(vo.getKmcode());
        if (jineStr == null) {
            logger.info("没钱了");
            return "没钱了";
        }
        int jine = Integer.parseInt(jineStr);
        if (jine < vo.getJine()) {
            logger.info(vo.getKmcode() + "的剩余费用不足" + vo.getJine());
            return vo.getKmcode() + "的剩余费用不足" + vo.getJine();
        }
        stringRedisTemplate.opsForValue().set(vo.getKmcode(), String.valueOf(jine- vo.getJine()));
        logger.info("报销成功");
        return "报销成功";
    }

    /**
     * 报销请求，使用分段锁机制，在保证效率的情况下保证单个服务内部线程安全，单体服务下不会出现超额报销的问题。
     * 但是在集群环境下还是会出现超额报销的问题
     * @param vo
     * @return
     */
    @PostMapping("/reimbursement2")
    public String reimbursement2(@RequestBody ReimbursementVO vo) {
        Object o = lockMap.computeIfAbsent(vo.getKmcode(), k -> new Object());
        synchronized (o){
            return reimbursement(vo);
        }
    }

    /**
     * 报销请求，使用redis中间件同步锁的状态<br/>
     * 但是存在一定的问题：<br/>
     * 1、redis的get和set分成两行写在并发场景下会存在线程安全问题。
     * 比如线程1get出来的lockValue为null，此时线程1被挂起，线程2也过来get相同的lockKey，此时获取到的lockValue也是null。
     * 所以线程1和线程2都可以加锁成功<br/>
     * 2、在redis中设置lockKey之后，并没有设置超时时间，假如一个服务加锁成功，但是因为某种原因异常宕机了，
     * 此时任何其他服务都无法再针对相同的kmcode加锁成功了，也就形成了死锁。<br/>
     * @param vo
     * @return
     */
    @PostMapping("/reimbursement3")
    public String reimbursement3(@RequestBody ReimbursementVO vo) {
        String kmcode = vo.getKmcode();
        String lockKey = "lock:"+kmcode;
        String lockValue = stringRedisTemplate.opsForValue().get(lockKey);
        if (lockValue == null) {
            stringRedisTemplate.opsForValue().set(lockKey, "1");
            try {
                return reimbursement(vo);
            }finally {
                stringRedisTemplate.delete(lockKey);
            }
        }else {
            return "当前报销人数较多，请稍后再试";
        }
    }

    /**
     * 方案3的优化版，使用redis的setnx命令保证当key不存在时才能设置成功，设置了lockKey的超时时间，
     * 不用担心获取锁的服务异常宕机导致死锁发生，增加了适当的自旋机制，尽可能保证加锁的成功率。
     * 但是这个方案依旧存在问题。假如服务A加锁成功，但是没能在10秒内完成业务逻辑处理，导致锁自动失效。
     * 锁失效后，服务B成功加锁，此时服务A的业务逻辑处理完毕，执行锁删除操作，又会在服务B没有执行完毕时释放锁，
     * 最坏的情况会导致锁完全失效。
     * @param vo
     * @return
     */
    @PostMapping("/reimbursement4")
    public String reimbursement4(@RequestBody ReimbursementVO vo) {
        String kmcode = vo.getKmcode();
        String lockKey = "lock:"+kmcode;
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        int tryCount = 1;
        while (!lock && tryCount<3){
            int randomTime = ThreadLocalRandom.current().nextInt(15);
            try {
                TimeUnit.MILLISECONDS.sleep(randomTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1",10,TimeUnit.SECONDS);
            tryCount++;
        }
        if (lock) {
            try {
                return reimbursement(vo);
            }finally {
                stringRedisTemplate.delete(lockKey);
            }
        }else {
            return "当前报销人数较多，请稍后再试";
        }
    }

    /**
     * 使用uuid作为lockValue，在锁释放时判断一下当前redis中的lockValue是不是本次加锁时存放的lockValue，如果是，那再删除lockKey。
     * 但是本方案依旧存在一定的漏洞，假如服务A获取到锁后，在finally中判断当前的lockValue确实是本次加锁时的lockValue。
     * 判断成功后，发生FGC导致redis中的lockKey过期，此时服务B加锁成功，服务B开始执行业务逻辑。然后A执行锁释放操作，依旧会导致锁失效问题。
     * 只是现在出现锁失效的概率低了很多。
     * @param vo
     * @return
     */
    @PostMapping("/reimbursement5")
    public String reimbursement5(@RequestBody ReimbursementVO vo) {
        String kmcode = vo.getKmcode();
        String lockKey = "lock:"+kmcode;
        String lockValue = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 10, TimeUnit.SECONDS);
        int tryCount = 1;
        while (!lock && tryCount<3){
            int randomTime = ThreadLocalRandom.current().nextInt(15);
            try {
                TimeUnit.MILLISECONDS.sleep(randomTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue,10,TimeUnit.SECONDS);
            tryCount++;
        }
        if (lock) {
            try {
                return reimbursement(vo);
            }finally {
                if (lockValue.equals(stringRedisTemplate.opsForValue().get(lockKey))) {
                    stringRedisTemplate.delete(lockKey);
                }
            }
        }else {
            return "当前报销人数较多，请稍后再试";
        }
    }

    /**
     * 当然了，基于redis实现分布式锁的终极解决方案还是使用redisson。
     * 在Redisson中使用lua脚本保证命令的原子性，同时使用watchDog实现锁续命。
     * @param vo
     * @return
     */
    @PostMapping("/reimbursement6")
    public String reimbursement6(@RequestBody ReimbursementVO vo) {
        String kmcode = vo.getKmcode();
        String lockKey = "lock:"+kmcode;
        RLock rLock = redissonClient.getLock(lockKey);
        rLock.lock();
        try {
            return reimbursement(vo);
        }finally {
            rLock.unlock();
        }
    }
}
