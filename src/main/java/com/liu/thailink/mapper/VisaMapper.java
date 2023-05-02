package com.liu.thailink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.thailink.entities.Visa;
import org.apache.ibatis.annotations.Select;

public interface VisaMapper extends BaseMapper<Visa> {
    @Select("select max(visaID) from sys_visa")
    int getMaxID();



}
