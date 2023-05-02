package com.liu.thailink;

import com.liu.thailink.entities.Role;
import com.liu.thailink.entities.RoleMenu;
import com.liu.thailink.exception.ServiceException;
import com.liu.thailink.mapper.RoleMapper;
import com.liu.thailink.mapper.RoleMenuMapper;
import com.liu.thailink.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Resource
    private RoleService roleService;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RoleMenuMapper roleMenuMapper;

    private Role role;
    private RoleMenu roleMenu;

    @BeforeEach
    public void setUp() {
        roleService = new RoleService();

        role = new Role();
        role.setRoleID(1);
        role.setRoleName("Admin");
        role.setDescription("Admin Role");

        roleMenu = new RoleMenu();
        roleMenu.setRoleID(1);
        roleMenu.setMenuID(1);
    }

    @Test
    public void saveRoleTest() throws Exception {
        when(roleMapper.insert(role)).thenReturn(1);
        boolean result = roleService.saveRole(role);
        Assertions.assertTrue(result);
        verify(roleMapper, times(1)).insert(role);
    }

    @Test
    public void saveRoleTest_Exception() {
        when(roleMapper.insert(role)).thenThrow(ServiceException.class);
        Assertions.assertThrows(ServiceException.class, () -> {
            roleService.saveRole(role);
        });
        verify(roleMapper, times(1)).insert(role);
    }

    @Test
    public void setRoleMenuTest() {
        List<Integer> menuIDs = Arrays.asList(1, 2, 3);
        when(roleMenuMapper.deleteByRoleID(1)).thenReturn(1);
        when(roleMenuMapper.insert(any(RoleMenu.class))).thenReturn(1);
        roleService.setRoleMenu(1, menuIDs);
        verify(roleMenuMapper, times(1)).deleteByRoleID(1);
        verify(roleMenuMapper, times(3)).insert(any(RoleMenu.class));
    }

    @Test
    public void getRoleMenuTest() {
        List<Integer> menuIDs = Arrays.asList(1, 2, 3);
        when(roleMenuMapper.selectByRoleID(1)).thenReturn(menuIDs);
        List<Integer> result = roleService.getRoleMenu(1);
        Assertions.assertEquals(result, menuIDs);
        verify(roleMenuMapper, times(1)).selectByRoleID(1);
    }

    @Test
    public void deleteTest() {
        when(roleMapper.deleteById(1)).thenReturn(1);
        boolean result = roleService.delete(1);
        Assertions.assertTrue(result);
        verify(roleMapper, times(1)).deleteById(1);
    }

    @Test
    public void deleteTest_Exception() {
        when(roleMapper.deleteById(1)).thenThrow(ServiceException.class);
        Assertions.assertThrows(ServiceException.class, () -> {
            roleService.delete(1);
        });
        verify(roleMapper, times(1)).deleteById(1);
    }
}