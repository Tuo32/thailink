package com.liu.thailink.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liu.thailink.common.Result;
import com.liu.thailink.entities.Menu;
import com.liu.thailink.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService ms;
    @PostMapping
    public boolean save(@RequestBody Menu menu){
        return ms.saveMenu(menu);
    }

    @GetMapping
    public Result findAll(){
//        find all menus
        List<Menu> list = ms.list();
        //find the menus which pid is null, set as parent menu
        List<Menu> parentNode = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //find the sub menus of parent menu
        for(Menu menu : parentNode){
            menu.setChildren(list.stream().filter(m -> menu.getMenuID().equals(m.getPid())).collect(Collectors.toList()));
        }
        return Result.success(parentNode);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return ms.removeById(id);
    }

    @GetMapping("/page")
    public IPage<Menu> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String menuName) {
        IPage<Menu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("menuName", menuName);
        return ms.page(page, queryWrapper);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return ms.removeBatchByIds(ids);
    }
}
