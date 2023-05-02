package com.liu.thailink.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.config.interceptor.JwtInterceptor;
import com.liu.thailink.controller.dto.VisaServiceDTO;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.entities.Visa;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.service.RoleMenuService;
import com.liu.thailink.service.UserService;
import com.liu.thailink.service.VisaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.httpervletRequest;


//@CrossOrigin
@RestController
@RequestMapping("/visa")
public class VisaController {
    @Autowired
    private VisaService vs;

    @Autowired
    private RoleMenuService rms;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @PostMapping("/save")
    public Result save(@RequestBody Visa visa, @RequestParam Integer requesterID,  httpervletRequest request){
        //verify permission
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",23);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST visa/save");
        logger.info("Requester = " + requesterID);
        logger.info("=============================");

        return Result.success(vs.saveVisa(visa));
    }

    @GetMapping("/total")
    public long total(){
        return vs.count();
    }


    //load page
    @GetMapping("/page")
    public IPage<Visa> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String visaID, @RequestParam String visaType, @RequestParam String userID, @RequestParam String updatedOrder, httpervletRequest request) {
        //verify permission
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",17);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }


        IPage<Visa> page = new Page<>(pageNum, pageSize);
        //configure the parameters of query
        QueryWrapper<Visa> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("visaID", visaID);
        queryWrapper.like("visaType", visaType);
        if (!userID.isEmpty()) {
            queryWrapper.eq("userID", userID);
        }

        switch (updatedOrder){
            case "normal":
                break;
            case "asc":
                queryWrapper.orderByAsc("updated");
                break;
            case "desc":
                queryWrapper.orderByDesc("updated");
        }

        return vs.page(page, queryWrapper);
    }


    @PostMapping("/statusUpdate")
    public Result visaStatusUpdate(@RequestParam String visaID, @RequestParam String status, @RequestParam Integer requesterID,  httpervletRequest request){
        //verify permission
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",21);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST visa/statusUpdate");
        logger.info("Request Parameter = " + visaID);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return Result.success(vs.statusUpdate(visaID, status));
    }

    @PostMapping("/statusRevert")
    public Result visaStatusRevert(@RequestParam String visaID, @RequestParam String status, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",21);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST visa/statusRevert");
        logger.info("Request Parameter = " + visaID);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");



        return Result.success(vs.statusRevert(visaID, status));
    }


    @GetMapping("/visa-service")
    public VisaServiceDTO joinView(@RequestParam String visaID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",17);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        return vs.join(visaID);
    }

}
