package com.liu.thailink.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.thailink.controller.dto.FinanceLineChart;
import com.liu.thailink.entities.Finance;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface FinanceMapper extends BaseMapper<Finance> {

    @Select("SELECT amounts FROM ( SELECT LEFT(createTime, 10) AS dates, SUM(amount) AS amounts FROM ( SELECT createTime, amount FROM sys_finance where createTime between #{startDate} and #{endDate} UNION ALL SELECT createTime, amount FROM sys_finance_history where createTime between #{startDate} and #{endDate}) AS finance_combined GROUP BY LEFT(createTime, 10) ) AS dates;")
    List<Double> selectAmountForLineChart(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT dates FROM ( SELECT LEFT(createTime, 10) AS dates, SUM(amount) AS amounts FROM ( SELECT createTime, amount FROM sys_finance where createTime between #{startDate} and #{endDate} UNION ALL SELECT createTime, amount FROM sys_finance_history where createTime between #{startDate} and #{endDate}) AS finance_combined GROUP BY LEFT(createTime, 10) ) AS amounts;")
    List<String> selectDateForLineChart(@Param("startDate") String startDate, @Param("endDate") String endDate);


    @Select("select sum(amount) from sys_finance where createTime between #{startDate} and #{endDate}")
    Double selectSum(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("select sum(amount) from sys_finance_history where createTime between #{startDate} and #{endDate}")
    Double selectSumHistory(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("select sum(amount) from sys_finance where serviceID = #{serviceID} and status = 'confirmed'")
    Double sumPaid( @Param("serviceID") Integer serviceID);
}
