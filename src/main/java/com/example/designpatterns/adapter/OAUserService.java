package com.example.designpatterns.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 假设这是来自OA sdk的一个类
 * @author skyline
 */
public class OAUserService {

    private final Map<String,OAUserDTO> userDTOMap;

    public OAUserService() {
        userDTOMap = new HashMap<>();
        init();
    }

    private void init() {
        OAUserDTO zhangsan = new OAUserDTO();
        zhangsan.setAge(18);
        zhangsan.setDeptName("部门A");
        zhangsan.setLoginName("zhangsan");
        userDTOMap.put("zhangsan",zhangsan);
        OAUserDTO lisi = new OAUserDTO();
        lisi.setAge(19);
        lisi.setDeptName("部门B");
        lisi.setLoginName("lisi");
        userDTOMap.put("lisi",lisi);
        OAUserDTO wangwu = new OAUserDTO();
        wangwu.setAge(20);
        wangwu.setDeptName("部门A");
        wangwu.setLoginName("wangwu");
        userDTOMap.put("wangwu",wangwu);
    }

    public OAUserDTO queryUser(String userName){
        return userDTOMap.get(userName);
    }

    public List<OAUserDTO> getAllUser(){
        return new ArrayList<>(userDTOMap.values());
    }
}
