package com.example.cache.service.impl;

import com.example.cache.CacheSpringBootMain;
import com.example.cache.domain.MyObj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CacheSpringBootMain.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
class MyDistributedLocalCacheServiceImplTest {

    @Autowired
    private MyDistributedLocalCacheServiceImpl cacheService;

    @BeforeEach
    public void preparedata(){
        saveMyObj("001", "001");
    }

    @Test
    void test() {
        cacheService.getById("001");
        cacheService.getById("001");
        saveMyObj("001", "001");
        cacheService.getById("001");
    }

    private void saveMyObj(String id,String content){
        MyObj myObj = new MyObj();
        myObj.setId(id);
        myObj.setContent(content);
        cacheService.save(myObj);
    }
}