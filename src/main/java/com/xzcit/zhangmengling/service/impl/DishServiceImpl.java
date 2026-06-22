package com.xzcit.zhangmengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzcit.zhangmengling.common.CustomException;
import com.xzcit.zhangmengling.dto.DishDto;
import com.xzcit.zhangmengling.entity.Dish;
import com.xzcit.zhangmengling.entity.DishFlavor;
import com.xzcit.zhangmengling.entity.SetmealDish;
import com.xzcit.zhangmengling.mapper.DishMapper;
import com.xzcit.zhangmengling.service.DishFlavorService;
import com.xzcit.zhangmengling.service.DishService;
import com.xzcit.zhangmengling.service.SetmealDishService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

@Service
// 补上 implements DishService
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private SetmealDishService setmealDishService;

    // 1.原有新增菜品实现
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 你原有业务代码
    }

    // 2.原有根据id查询菜品+口味实现
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        return null;
        // 你原有业务代码
    }

    // 3.原有修改菜品实现
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 你原有业务代码
    }

    // 4.新增批量删除方法
    @Override
    @Transactional
    public void removeBatchWithCheck(List<Long> ids) {
        // 校验菜品是否绑定套餐
        LambdaQueryWrapper<SetmealDish> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.in(SetmealDish::getDishId, ids);
        long bindCount = setmealDishService.count(setmealWrapper);
        if (bindCount > 0) {
            throw new CustomException("选中菜品已关联套餐，无法删除，请先解除套餐绑定");
        }

        // 删除对应口味
        LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
        flavorWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(flavorWrapper);

        // 批量删菜品
        baseMapper.deleteBatchIds(ids);
    }
}