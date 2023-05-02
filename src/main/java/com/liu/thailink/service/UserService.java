package com.liu.thailink.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.thailink.controller.dto.UserDTO;
import com.liu.thailink.entities.User;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.UserMapper;
import com.liu.thailink.utils.EmailUtils;
import com.liu.thailink.utils.TokenUtils;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserService extends ServiceImpl<UserMapper,User>{
    public boolean saveUser(User user){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = dateFormat.format(new Date());
            user.setModifyTime(currentTime);
          return  saveOrUpdate(user);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, please check your input.");
        }
    }

    public void sendAuthenticationCode(UserDTO userDTO){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username",userDTO.getUsername());
        qw.eq("password",userDTO.getPassword());
        User one = new User();
        try{
            one = getOne(qw);
        } catch (Exception e){
            e.printStackTrace();
            one.setUsername("error");
        }
        if(one != null){
            //random authentication code
            String str = "abcedfghijklmnopqrstuvwxyz1234567890";
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < 6; i++){
                int number = random.nextInt(36);
                sb.append(str.charAt(number));
            }
            one.setAuthentication(sb.toString());
            updateById(one);
            EmailUtils.sendEmail(sb.toString(), one.getEmail());
        }else{
            throw new ServiceException("CODE_600", "username or password is wrong");
        }

    }

    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username",userDTO.getUsername());
        qw.eq("password",userDTO.getPassword());
//        qw.eq("authentication",userDTO.getAuthentication());
        User one = new User();
        try{
             one = getOne(qw);

        } catch (Exception e){
            e.printStackTrace();
            one.setUsername("error");
            throw new ServiceException("CODE_600", "username, password, or authentication code is wrong");
        }

        if(one != null){
            one.setAuthentication(null);
//            remove the authentication code after login
            updateById(one);
            UserDTO returnDTO = new UserDTO();
            returnDTO.setUsername(one.getUsername());
            returnDTO.setId(one.getUserID());
            String Token = TokenUtils.genToken(one.getUserID().toString(), one.getPassword());
            returnDTO.setToken(Token);
            returnDTO.setName(one.getName());
            returnDTO.setEmail(one.getEmail());
            returnDTO.setAddress(one.getAddress());
            returnDTO.setPhone(one.getPhone());
            returnDTO.setRoleID( one.getRoleID());
            returnDTO.setMenus(userDTO.getMenus());
            return returnDTO;
        }else{
            throw new ServiceException("CODE_600", "username, password, or authentication code is wrong");
        }

    }

    public boolean delete(Integer id) {
        try{
            return  removeById(id);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, the record may have relative records.");
        }
    }

    public boolean batchDelete(List<Integer> ids) {
        try{
            return removeBatchByIds(ids);
        } catch (Exception e){
            throw new ServiceException("CODE_500", "system error, the record may have relative records.");
        }
    }
}
