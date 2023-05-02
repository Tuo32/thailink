package com.liu.thailink.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.config.interceptor.JwtInterceptor;
import com.liu.thailink.controller.dto.StudyServiceDTO;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.entities.Study;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.service.RoleMenuService;
import com.liu.thailink.service.StudyService;
import com.liu.thailink.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.httpervletRequest;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/study")
public class StudyController {
    @Autowired
    private StudyService ss;
    @Autowired
    private RoleMenuService rms;

    @Autowired
    private UserService us;

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @PostMapping("/save")
    public Result save(@RequestBody Study study, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",24);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST study/save");
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return Result.success(ss.saveStudy(study));
    }

    @GetMapping
    public List<Study> findAll(){
        return ss.list();
    }

    @GetMapping("/total")
    public long total(){
        return ss.count();
    }

    @DeleteMapping("/del")
    public Boolean delete(@RequestParam Integer id, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",25);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("DELETE study/del");
        logger.info("Request Parameter = " + id);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return ss.removeById(id);
    }

    @GetMapping("/page")
    public IPage<Study> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String studyID, @RequestParam String userID, @RequestParam String applySchool, @RequestParam String updatedOrder, httpervletRequest request) {
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",16);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        IPage<Study> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Study> queryWrapper = new QueryWrapper<>();

        queryWrapper.like("studyID", studyID);
        queryWrapper.like("applySchool", applySchool);

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
        return ss.page(page, queryWrapper);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",25);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }


        StringBuffer sb = new StringBuffer();
        for (Integer id : ids) {
            sb.append(id);
            sb.append(",");
        }
        logger.info("=============================");
        logger.info("DELETE study/del/batch");
        logger.info("Request Parameter = " + sb);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return ss.removeBatchByIds(ids);
    }

    @GetMapping("/study-service")
    public StudyServiceDTO joinView(@RequestParam String studyID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",16);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        return ss.join(studyID);
    }

    @PostMapping("/statusUpdate")
    public Result studyStatusUpdate(@RequestParam String studyID, @RequestParam String status, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",26);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST study/statusUpdate");
        logger.info("Request Parameter = " + studyID);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return Result.success(ss.statusUpdate(studyID, status));
    }

    @PostMapping("/statusRevert")
    public Result studyStatusRevert(@RequestParam String studyID, @RequestParam String status, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = us.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",26);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST study/statusRevert");
        logger.info("Request Parameter = " + studyID);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return Result.success(ss.statusRevert(studyID, status));
    }


}
