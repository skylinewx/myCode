package com.example.cache2.service;

import com.example.cache2.domain.GroupDO;

public interface MyService {

    /**
     * 根据id获取数据
     * @param id
     * @return
     */
    GroupDO getById(String id);

    /**
     * 根据id获取数据后放入回调器
     * @param id
     * @param afterGet
     */
    void getById(String id, IAfterGet afterGet);

    /**
     * 保存数据
     * @param groupDO
     */
    void save(GroupDO groupDO);

    /**
     * 删除数据
     * @param id
     */
    void delete(String id);
}
