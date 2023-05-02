package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value="sys_file")
public class File {
    @TableId(type = IdType.AUTO, value="fileID")
    private Integer fileID;
    @TableField(value = "serviceID")
    private Integer serviceID;
    private String name;
    private String type;
    private Long size;
    private String url;
}
