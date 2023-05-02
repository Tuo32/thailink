package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Data
@TableName(value="sys_finance")
public class Finance {
        @TableId(type = IdType.AUTO, value="financeID")
        private String financeID;
        @TableField(value="serviceID")
        private Integer serviceID;
        private String description;
        private Double amount;

        @TableField(value="createTime")
        private String createTime;

        public String toString() {
                return "Finance{" +
                        "financeID='" + financeID + '\'' +
                        ", serviceID=" + serviceID +
                        ", description='" + description + '\'' +
                        ", amount=" + amount +
                        ", createTime='" + createTime + '\'' +
                        '}';
        }
}
