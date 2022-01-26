package com.example.designpatterns.adapter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OA用户服务适配器
 * @author skyline
 */
public class OAUserServiceAdapter implements IUserService{

    private final OAUserService oaUserService;

    public OAUserServiceAdapter() {
        oaUserService = new OAUserService();
    }

    @Override
    public UserDTO getByName(String name) {
        OAUserDTO oaUserDTO = oaUserService.queryUser(name);
        return convert(oaUserDTO);
    }

    private UserDTO convert(OAUserDTO oaUserDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(oaUserDTO.getLoginName());
        userDTO.setDept(oaUserDTO.getDeptName());
        userDTO.setAge(oaUserDTO.getAge());
        return userDTO;
    }

    @Override
    public List<UserDTO> listAll() {
        List<OAUserDTO> allUser = oaUserService.getAllUser();
        return allUser.stream().map(this::convert).collect(Collectors.toList());
    }
}
