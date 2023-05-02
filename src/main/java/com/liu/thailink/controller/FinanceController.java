package com.liu.thailink.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.config.interceptor.JwtInterceptor;
import com.liu.thailink.controller.dto.FinanceLineChart;
import com.liu.thailink.entities.Finance;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.service.FinanceService;
import com.liu.thailink.service.RoleMenuService;
import com.liu.thailink.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.httpervletRequest;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/finance")
public class FinanceController {
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
    @Autowired
    private FinanceService fs;

    @Autowired
    private UserService us;

    @Autowired
    private RoleMenuService rms;

    @PostMapping("/save")
    public Result save(@RequestBody Finance finance, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",5);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST finance/save");
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return Result.success(fs.saveFinance(finance));
    }

    @GetMapping
    public List<Finance> findAll(){
        return fs.list();
    }


    @GetMapping("/page")
    public IPage<Finance> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String serviceID, @RequestParam String createTime,  httpervletRequest request) {
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",5);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        IPage<Finance> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Finance> queryWrapper = new QueryWrapper<>();
        if(serviceID != ""){
            queryWrapper.eq("serviceID", serviceID);
        }
        if(!createTime.equals("null")){
            queryWrapper.like("createTime", createTime);
        }
        return fs.page(page, queryWrapper);
    }

    @GetMapping("/chart/line")
    public FinanceLineChart getLineChartData(@RequestParam String startDate, @RequestParam String endDate,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",5);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        endDate +=  " 23:59:59";

        return fs.getLineChartData(startDate,endDate);
    }

    @GetMapping("/sum")
    public Double selectSum(@RequestParam String startDate, @RequestParam String endDate,  httpervletRequest request){
        endDate +=  " 23:59:59";
        return fs.selectSum(startDate,endDate);
    }



}
