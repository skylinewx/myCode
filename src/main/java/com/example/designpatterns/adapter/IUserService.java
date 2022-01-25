package com.example.designpatterns.adapter;

import java.util.List;

/**
 * 用户查询Service
 * @author skyline
 */
public interface IUserService {

    /**
     * 通过用户名查询用户
     * @param name
     * @return
     */
    UserDTO getByName(String name);

    /**
     * 展示所有用户
     * @return
     */
    List<UserDTO> listAll();
}
