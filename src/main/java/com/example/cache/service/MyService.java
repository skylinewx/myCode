package com.example.cache.service;

import com.example.cache.domain.MyObj;

public interface MyService {

    /**
     * 根据id获取数据
     * @param id
     * @return
     */
    MyObj getById(String id);

    /**
     * 根据id获取数据后放入回调器
     * @param id
     * @param afterGet
     */
    void getById(String id, IAfterGet afterGet);

    /**
     * 保存数据
     * @param myObj
     */
    void save(MyObj myObj);

    /**
     * 删除数据
     * @param id
     */
    void delete(String id);
}
