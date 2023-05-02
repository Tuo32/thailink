package com.liu.thailink;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liu.thailink.controller.dto.UserDTO;
import com.liu.thailink.entities.User;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.UserMapper;
import com.liu.thailink.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;
    private UserDTO userDTO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("pass1");
        user1.setEmail("user1@test.com");

        user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("pass2");
        user2.setEmail("user2@test.com");

        userDTO = new UserDTO();
        userDTO.setUsername("user1");
        userDTO.setPassword("pass1");
        userDTO.setAuthentication("123456");
    }

    @Test
    public void testSaveUser() {
        when(userMapper.insert(user1)).thenReturn(1);
        boolean result = userService.saveUser(user1);
        assertTrue(result);
    }

    @Test
    public void testSaveUserThrowsException() {
        when(userMapper.insert(user1)).thenThrow(new RuntimeException());
        try {
            userService.saveUser(user1);
            fail("Expected ServiceException not thrown");
        } catch (ServiceException ex) {
            assertEquals("CODE_500", ex.getCode());
        }
    }

    @Test
    public void testSendAuthenticationCode() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        when(userMapper.selectOne(queryWrapper)).thenReturn(user1);


    }

    @Test
    public void testSendAuthenticationCodeThrowsException() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        when(userMapper.selectOne(queryWrapper)).thenThrow(new RuntimeException());

        try {
            userService.sendAuthenticationCode(userDTO);
            fail("Expected ServiceException not thrown");
        } catch (ServiceException ex) {
            assertEquals("CODE_600", ex.getCode());
        }
    }

    @Test
    public void testLogin() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        when(userMapper.selectOne(queryWrapper)).thenReturn(user1);

        UserDTO result = userService.login(userDTO);

        assertEquals(user1.getUsername(), result.getUsername());
        assertEquals(user1.getUserID(), result.getId());
        assertNotNull(result.getToken());
        assertNull(user1.getAuthentication());
        verify(userMapper).updateById(user1);
    }

}
