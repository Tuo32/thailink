package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value="sys_role")
public class Role {
        @TableId(type = IdType.AUTO, value="roleID")
        private Integer roleID;
        @TableField(value="roleName")
        private String roleName;
        private String description;



}
