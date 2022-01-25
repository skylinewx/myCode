package com.example.designpatterns.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的用户服务
 */
public class MyUserService implements IUserService{

    private final Map<String,UserDTO> userDTOMap;

    public MyUserService(){
        userDTOMap = new HashMap<>();
        init();
    }

    private void init() {
        UserDTO zhangsan = new UserDTO();
        zhangsan.setAge(18);
        zhangsan.setDept("部门A");
        zhangsan.setName("zhangsan");
        userDTOMap.put("zhangsan", zhangsan);
        UserDTO lisi = new UserDTO();
        lisi.setAge(19);
        lisi.setDept("部门B");
        lisi.setName("lisi");
        userDTOMap.put("lisi", lisi);
        UserDTO wangwu = new UserDTO();
        wangwu.setAge(20);
        wangwu.setDept("部门A");
        wangwu.setName("wangwu");
        userDTOMap.put("wangwu", wangwu);
    }

    @Override
    public UserDTO getByName(String name) {
        return userDTOMap.get(name);
    }

    @Override
    public List<UserDTO> listAll() {
        return new ArrayList<>(userDTOMap.values());
    }
}
