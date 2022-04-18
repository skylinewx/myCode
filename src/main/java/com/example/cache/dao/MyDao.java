package com.example.cache.dao;

import com.example.cache.domain.MyObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MyDao {

    private static final Logger logger = LoggerFactory.getLogger(MyDao.class);

    private final Map<String,MyObj> store = new HashMap<>();
    public MyObj getById(String id){
        MyObj myObj = store.get(id);
        logger.info("getById:{}",myObj);
        return myObj;
    }

    public void save(MyObj obj){
        logger.info("save:{}",obj);
        store.put(obj.getId(), obj);
    }

    public void delete(String id){
        logger.info("delete:{}",id);
        store.remove(id);
    }
}
