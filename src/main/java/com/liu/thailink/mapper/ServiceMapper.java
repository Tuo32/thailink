package com.liu.thailink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.thailink.entities.Service;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2022-11-01
 */
public interface ServiceMapper extends BaseMapper<Service> {

    @Insert("INSERT INTO sys_service_history (serviceID, customerID, serviceType, paid) SELECT serviceID, customerID, serviceType, paid FROM sys_service WHERE serviceID = #{serviceID}")
    Integer moveToHistory(@Param("serviceID") Integer serviceID);

    @Insert("INSERT INTO sys_finance_history (financeID, serviceID, description, amount, createTime) SELECT financeID, serviceID, description, amount, createTime FROM sys_finance WHERE serviceID = #{serviceID}")
    Integer moveFinanceToHistory(@Param("serviceID") Integer serviceID);

    @Select("SELECT serviceType FROM sys_service WHERE serviceID = #{serviceID}")
    String getServiceType (@Param("serviceID") Integer serviceID);

    @Select("SELECT infoID FROM sys_service WHERE serviceID = #{serviceID}")
    String getInfoID (@Param("serviceID") Integer serviceID);
}
