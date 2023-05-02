package com.liu.thailink.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.entities.Customer;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.service.CustomerService;
import com.liu.thailink.service.RoleMenuService;
import com.liu.thailink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.httpervletRequest;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/customer")
public class CustomerController {
    //this is for testing the authority of users
    @Autowired
    private RoleMenuService rms;
    @Autowired
    private CustomerService csService;

    @Autowired
    private UserService us;

    @PostMapping
    public Result save(@RequestBody Customer cs,  httpervletRequest request ){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",6);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }
        return Result.success(csService.saveCustomer(cs));
    }

    @GetMapping
    public List<Customer> findAll(){
        return csService.list();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id,  httpervletRequest request ){
        //verify permission
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",8);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }
        return Result.success(csService.delete(id));
    }

    //load the page of table
    @GetMapping("/page")
    public IPage<Customer> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String customerID, @RequestParam String name,  httpervletRequest request ) {
       //verify permission
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",4);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        IPage<Customer> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        //search the records which have similar value
        queryWrapper.like("customerID", customerID);
        queryWrapper.like("name", name);
        return csService.page(page, queryWrapper);
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids,  httpervletRequest request ){
        //verify permission
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",8);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }
        return Result.success(csService.batchDelete(ids));
    }

    //remove the sensitive information of customer
    @PostMapping("/clear")
    public Result privacy(@RequestBody List<Integer> ids,  httpervletRequest request ){
        //verify permission
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",6);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }
        return Result.success(csService.clearCustomer(ids));

    }

}
