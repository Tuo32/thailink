package com.liu.thailink.controller.dto;

import com.liu.thailink.entities.Menu;
import lombok.Data;

import java.util.List;

//this is used to transfer user data.
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String name;
    private Integer roleID;
    private String authentication;
    private String token;
    private List<Menu> menus;
}
