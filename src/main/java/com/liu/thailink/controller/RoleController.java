package com.liu.thailink.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.entities.Role;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.service.RoleMenuService;
import com.liu.thailink.service.RoleService;
import com.liu.thailink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.httpervletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService rs;

    @Autowired
    private RoleMenuService rms;

    @Autowired
    private UserService us;

    @PostMapping
    public Result save(@RequestBody Role role,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",2);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        return Result.success(rs.saveRole(role));
    }

    @GetMapping("/all")
    public List<Role> findAll(httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",2);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }
        return rs.list();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",2);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        return Result.success(rs.delete(id));
    }

    @GetMapping("/page")
    public IPage<Role> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String roleID, @RequestParam String roleName,  httpervletRequest request) {
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",2);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        IPage<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("roleID", roleID);
        queryWrapper.like("roleName", roleName);
        //hide the root role and demonstration role
        List<String> collection = new ArrayList<>();
        collection.add("1");
        collection.add("21");

        queryWrapper.notIn("roleID", collection);
        return rs.page(page, queryWrapper);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",2);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        return Result.success( rs.batchDelete(ids));
    }

    //match role and menu authority
    @PostMapping("/roleMenu/{roleID}")
    public Result roleMenu(@PathVariable Integer roleID, @RequestBody List<Integer> menuIDs,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",2);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        rs.setRoleMenu(roleID,menuIDs);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleID}")
    public Result getRoleMenu(@PathVariable Integer roleID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",2);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        return Result.success(rs.getRoleMenu(roleID));
    }
}

