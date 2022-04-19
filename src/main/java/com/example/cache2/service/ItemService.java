package com.example.cache2.service;

import com.example.cache2.dao.ItemDao;
import com.example.cache2.domain.ItemDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemService extends MyListableCacheService<ItemDO,String>{

    @Autowired
    private ItemDao itemDao;

    @Override
    protected List<ItemDO> listAll() {
        return itemDao.listAll();
    }

    @Override
    protected void doSave(ItemDO obj) {
        itemDao.save(obj);
    }

    @Override
    protected void doDelete(String s) {
        itemDao.delete(s);
    }

    @Override
    protected String getKeyHolderByObj(ItemDO obj) {
        return obj.getId();
    }
}
