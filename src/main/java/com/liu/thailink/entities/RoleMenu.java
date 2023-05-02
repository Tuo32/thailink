package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_role_menu")
@Data
public class RoleMenu {
    @TableField(value="roleID")
    private Integer roleID;
    @TableField(value="menuID")
    private Integer menuID;
}
