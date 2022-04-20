package com.example.cache2.service;

import com.example.cache2.dao.GroupDao;
import com.example.cache2.domain.GroupDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分布式本地缓存
 */
@Component
public class GroupService extends MyCacheService<GroupDO, String> {

    @Autowired
    private GroupDao groupDao;

    @Override
    protected GroupDO doGet(String s) {
        return groupDao.getById(s);
    }

    @Override
    protected void doSave(GroupDO obj) {
        groupDao.save(obj);
    }

    @Override
    protected void doDelete(String s) {
        groupDao.delete(s);
    }

    @Override
    protected String getKeyHolderByObj(GroupDO obj) {
        return obj.getId();
    }
}
