package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value="sys_visa")
public class Visa {
    @TableId(type = IdType.AUTO, value="visaID")
    private Integer visaID;
    @TableField(value="visaType")
    private String visaType;
    private String comment;
    private String status;
    private String updated;

    @TableField(value="userID")
    private String userID;
    @TableField(value="createTime")
    private String createTime;
    @TableField(exist = false)
    private Integer customerID;
    @TableField(exist = false)
    private Double price;

    @Override
    public String toString() {
        return "Visa{" +
                "visaID=" + visaID +
                ", visaType='" + visaType + '\'' +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                ", updated='" + updated + '\'' +
                ", createTime='" + createTime + '\'' +
                ", customerID=" + customerID +
                ", price=" + price +
                '}';
    }
}
