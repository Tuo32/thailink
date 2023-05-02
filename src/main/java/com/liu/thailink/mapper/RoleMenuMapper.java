package com.liu.thailink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.thailink.entities.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    @Delete("delete from sys_role_menu where roleID = #{roleID}")
    int deleteByRoleID(@Param("roleID") Integer roleID);

    @Select("select menuID from sys_role_menu where roleID = #{roleID}")
    List<Integer> selectByRoleID(@Param("roleID") Integer roleID);
}
