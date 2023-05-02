package com.liu.thailink.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value="sys_service")
public class Service {
        @TableId(type = IdType.AUTO, value="serviceID")
        private Integer serviceID;
        @TableField(value="customerID")
        private Integer customerID;
        @TableField(value="infoID")
        private Integer infoID;
        @TableField(value="serviceType")
        private String serviceType;
        private Double price;
        private Double paid;

        public String toString() {
                return "Service{" +
                        "serviceID=" + serviceID +
                        ", customerID=" + customerID +
                        ", infoID=" + infoID +
                        ", serviceType='" + serviceType + '\'' +
                        ", price=" + price +
                        ", paid=" + paid +
                        '}';
        }
}
