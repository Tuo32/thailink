package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName(value="sys_receipt")
public class Receipt {
    @TableId(type = IdType.AUTO, value="receiptID")
    private Integer receiptID;

    @TableField(value="userID")
    private Integer useID;

    private String createTime;
}
