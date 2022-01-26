package com.example.designpatterns.adapter;

/**
 * 适配器模式
 * @author skyline
 */
public class AdapterMain {
    public static void main(String[] args) {
        IUserService userService = new OAUserServiceAdapter();
        UserDTO zhangsan = userService.getByName("zhangsan");
        System.out.println(zhangsan);
    }
}
