package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_study")
@Data
public class Study {
    @TableId(type = IdType.AUTO, value="studyID")
    private Integer studyID;
    @TableField(value = "bloodType")
    private String bloodType;
    private String religion;
    @TableField(value = "formerSchool")
    private String formerSchool;
    @TableField(value = "fatherName")
    private String fatherName;
    @TableField(value = "motherName")
    private String motherName;

    @TableField(value="userID")
    private String userID;
    @TableField(value = "applySchool")
    private String applySchool;
    private String status;
    private String comment;
    private String updated;
    @TableField(value = "createTime")
    private String createTime;

    @TableField(exist = false)
    private Integer customerID;
    @TableField(exist = false)
    private Double price;

    @Override
    public String toString() {
        return "Study{" +
                "studyID=" + studyID +
                ", bloodType='" + bloodType + '\'' +
                ", religion='" + religion + '\'' +
                ", formerSchool='" + formerSchool + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", applySchool='" + applySchool + '\'' +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                ", updated='" + updated + '\'' +
                ", createTime='" + createTime + '\'' +
                ", customerID=" + customerID +
                ", price=" + price +
                '}';
    }
}
