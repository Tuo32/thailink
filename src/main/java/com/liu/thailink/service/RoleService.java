package com.liu.thailink.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.thailink.entities.Role;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.RoleMapper;
import com.liu.thailink.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
    @Resource
    private RoleMenuMapper roleMenuMapper;
    public boolean saveRole(Role role)
    {
        try{
            return  saveOrUpdate(role);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, please check your input");
        }
    }

    @Transactional
    public void setRoleMenu(Integer roleID, List<Integer> menuIDs) {
   //remove current menu authority and role relation by roleID
     roleMenuMapper.deleteByRoleID(roleID);
     // match new relationship
        for(Integer menuID : menuIDs){
            RoleMenu rm = new RoleMenu();
            rm.setRoleID(roleID);
            rm.setMenuID(menuID);
            roleMenuMapper.insert(rm);
        }
    }

    public List<Integer> getRoleMenu(Integer roleID){
        return roleMenuMapper.selectByRoleID(roleID);
    }

    public boolean delete(Integer id) {
            try{
                return removeById(id);
            }catch (Exception e){
                throw new ServiceException("CODE_500", "system error");
            }
        }

    public boolean batchDelete(List<Integer> ids) {
        try{
            return removeBatchByIds(ids);
        }catch (Exception e){
            throw new ServiceException("CODE_500", "system error");
        }
    }
}
