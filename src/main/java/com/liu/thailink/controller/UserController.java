package com.liu.thailink.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.config.interceptor.JwtInterceptor;
import com.liu.thailink.controller.dto.UserDTO;
import com.liu.thailink.entities.Menu;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.entities.User;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.RoleMenuMapper;
import com.liu.thailink.service.MenuService;
import com.liu.thailink.service.RoleMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.liu.thailink.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.httpervletRequest;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "http://localhost:80" )
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleMenuService rms;

    @PostMapping("/email-auth")
    public Result emailAuth(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error("Code_400","username or password is wrong");
        }
        userService.sendAuthenticationCode(userDTO);
        return Result.success();
    }


    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error("Code_400","username or password is wrong");
        }
        UserDTO dto = userService.login(userDTO);
        List<Integer> menuIDs = roleMenuMapper.selectByRoleID(dto.getRoleID());
        //find all menus in the system
        List<Menu> menus = menuService.list();
//        //the list after filter
        List<Menu> roleMenus = new ArrayList<>();
//        //filter the user's menus
        for(Menu menu : menus){
            if(menuIDs.contains(menu.getMenuID())){
                roleMenus.add(menu);
            }
        }
        dto.setMenus(roleMenus);
        return Result.success(dto);
        }


    @PostMapping("/save")
    public Result save(@RequestBody User user, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",1);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("POST user/save");
        logger.info("Requester = " + requesterID);
        logger.info("=============================");



        return Result.success(userService.saveUser(user));
    }

    @GetMapping
    public List<User> findAll(){
        return userService.list();
    }

    @DeleteMapping("/del")
    public Result delete(@RequestParam Integer id, @RequestParam Integer requesterID,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",1);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }
        authority.eq("menuID",1);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        logger.info("=============================");
        logger.info("DELETE user/del");
        logger.info("Request Parameter = " + id);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");




        return Result.success(userService.delete(id));
    }

    //find user by page
    @GetMapping("/page")
    public  IPage<User> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String userID, @RequestParam String username, @RequestParam String name, @RequestParam String roleID,  httpervletRequest request ){

        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",1);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        IPage<User> page = new Page<>(pageNum,pageSize);
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.like("userID",userID);
    queryWrapper.like("username",username);
    queryWrapper.like("name",name);

    if(roleID != ""){
        queryWrapper.eq("roleID",roleID);
    }


    return userService.page(page, queryWrapper );
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids, @RequestParam Integer requesterID ,  httpervletRequest request){
        String token = request.getHeader("token");
        Integer userId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        Integer roleId = userService.getById(userId).getRoleID();
        QueryWrapper<RoleMenu> authority = new QueryWrapper<>();
        authority.eq("roleID", roleId);
        authority.eq("menuID",1);
        if(rms.getOne(authority) == null){
            throw new ServiceException("CODE_403", "permission denied");
        }

        StringBuffer sb = new StringBuffer();
        for (Integer id : ids) {
           sb.append(id);
           sb.append(",");
        }
        logger.info("=============================");
        logger.info("DELETE user/del/batch");
        logger.info("Request Parameter = " + sb);
        logger.info("Requester = " + requesterID);
        logger.info("=============================");
        return Result.success(userService.batchDelete(ids));
    }
}
