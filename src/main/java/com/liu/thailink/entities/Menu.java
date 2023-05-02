package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value="sys_menu")
public class Menu {
    @TableId(type = IdType.AUTO, value="menuID")
    private Integer menuID;
    @TableField(value = "menuName")
    private String menuName;
    private String path;
    @TableField(value = "docName")
    private String docName;
    private String description;
    private Integer pid;
    private String icon;
    private String position;

    @TableField(exist = false)
    private List<Menu> children;
}
