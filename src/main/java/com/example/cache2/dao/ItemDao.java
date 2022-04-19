package com.example.cache2.dao;

import com.example.cache2.domain.ItemDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemDao {
    private static final Logger logger = LoggerFactory.getLogger(ItemDao.class);

    private final Map<String, ItemDO> store = new HashMap<>();
    public ItemDO getById(String id){
        ItemDO ItemDO = store.get(id);
        logger.info("getById:{}", ItemDO);
        return ItemDO;
    }

    public List<ItemDO> listAll(){
        return new ArrayList<>(store.values());
    }

    public void save(ItemDO obj){
        logger.info("save:{}",obj);
        store.put(obj.getId(), obj);
    }

    public void delete(String id){
        logger.info("delete:{}",id);
        store.remove(id);
    }
}
