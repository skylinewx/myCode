package com.example.juc.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wangxing
 */
@Service
public class AccountChangeService {

    @Autowired
    AccountChangeService self;
    @Autowired
    private AccountDAO accountDAO;

    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferDTO transferDTO) {
        AccountChangeDTO changeDTO = new AccountChangeDTO();
        changeDTO.setUserId(transferDTO.getFromUserId());
        changeDTO.setChangeMoney(transferDTO.getMoney().multiply(new BigDecimal("-1")));
        accountDAO.updateAccount(changeDTO);
        changeDTO.setUserId(transferDTO.getToUserId());
        changeDTO.setChangeMoney(transferDTO.getMoney());
        if (transferDTO.getMoney().equals(new BigDecimal(1))) {
            throw new RuntimeException("等于1了，抛个异常");
        }
        accountDAO.updateAccount(changeDTO);
    }

    public void test() {
        Random random = new Random();
        int sleep = random.nextInt(5);
        try {
            TimeUnit.SECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setMoney(new BigDecimal(String.valueOf(sleep)));
        //小于3就从1->2否则就2->1，伪随机
        if (sleep < 3) {
            transferDTO.setFromUserId("001");
            transferDTO.setToUserId("002");
        } else {
            transferDTO.setFromUserId("002");
            transferDTO.setToUserId("001");
        }
        self.transfer(transferDTO);
    }
}
