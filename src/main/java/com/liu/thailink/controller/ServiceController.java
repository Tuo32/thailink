package com.liu.thailink.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.config.interceptor.JwtInterceptor;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.entities.Service;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.service.RoleMenuService;
import com.liu.thailink.service.ServiceService;
import com.liu.thailink.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.httpervletRequest;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private ServiceService ss;

    @Autowired
    private RoleMenuService rms;

    @Autowired
    private UserService us;

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @PostMapping("/save")
    public Result save(@RequestBody Service service, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",31);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST service/save");
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return Result.success(ss.saveService(service));
    }

    @GetMapping
    public List<Service> findAll(){
        return ss.list();
    }

    @GetMapping("/total")
    public long total(){
        return ss.count();
    }


    @GetMapping("/page")
    public IPage<Service> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String serviceID, @RequestParam String customerID, @RequestParam String serviceType, @RequestParam String infoID ,  httpervletRequest request) {
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",30);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }


        IPage<Service> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Service> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("serviceID", serviceID);

        if(customerID != ""){
            queryWrapper.eq("customerID", customerID);
        }

        if(serviceType != ""){
            queryWrapper.eq("serviceType", serviceType);
        }

        if(infoID != ""){
            queryWrapper.eq("infoID", infoID);
        }


        return ss.page(page, queryWrapper);
    }

    @PostMapping("/history")
    public Result history(@RequestBody Integer id, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",31);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }
        logger.info("=============================");
        logger.info("POST user/history");
        logger.info("Request Parameter = " + id);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return Result.success(ss.history(id));
    }




}
