package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
@Data
@TableName(value="sys_user")
public class User {
    @TableId(type = IdType.AUTO, value = "userID")
    private Integer userID;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String address;
    private String email;
    @TableField(value = "roleID")
    private Integer roleID;
    @TableField(value = "createTime")
    private String createTime;

    @TableField(value = "modifyTime")
    private String modifyTime;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String authentication;

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", roleID=" + roleID +
                ", createTime='" + createTime + '\'' +
                ", authentication='" + authentication + '\'' +
                '}';
    }


}
