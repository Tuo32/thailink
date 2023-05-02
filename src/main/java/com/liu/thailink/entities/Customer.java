package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName(value="sys_customer")
public class Customer {
    @TableId(type = IdType.AUTO, value = "customerID")
    private Integer customerID;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String name;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String gender;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String passport;
    @TableField(value = "birthDate", updateStrategy = FieldStrategy.IGNORED)
    private String birthDate;
    @TableField(value = "birthPlace", updateStrategy = FieldStrategy.IGNORED)
    private String birthPlace;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String address;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String postcode;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String phone;
    private String email;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String comment;
    @TableField(value = "modifyTime")
    private String modifyTime;
    @TableField(value = "createTime")
    private String createTime;


}
