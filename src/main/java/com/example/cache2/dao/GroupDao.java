package com.example.cache2.dao;

import com.example.cache2.domain.GroupDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(GroupDao.class);

    private final Map<String, GroupDO> store = new HashMap<>();
    public GroupDO getById(String id){
        GroupDO groupDO = store.get(id);
        logger.info("getById:{}", groupDO);
        return groupDO;
    }

    public List<GroupDO> listAll(){
        return new ArrayList<>(store.values());
    }

    public void save(GroupDO obj){
        logger.info("save:{}",obj);
        store.put(obj.getId(), obj);
    }

    public void delete(String id){
        logger.info("delete:{}",id);
        store.remove(id);
    }
}
