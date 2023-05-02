package com.liu.thailink.service;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.thailink.entities.Menu;
import com.liu.thailink.mapper.MenuMapper;
import org.springframework.stereotype.Service;


@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    public boolean saveMenu(Menu menu)
    {
        return saveOrUpdate(menu);
    }


}
