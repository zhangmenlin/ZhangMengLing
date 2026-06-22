package com.xzcit.zhangmengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzcit.zhangmengling.common.CustomException;
import com.xzcit.zhangmengling.entity.Category;
import com.xzcit.zhangmengling.entity.Dish;
import com.xzcit.zhangmengling.entity.Setmeal;
import com.xzcit.zhangmengling.mapper.CategoryMapper;
import com.xzcit.zhangmengling.service.CategoryService;
import com.xzcit.zhangmengling.service.DishService;
import com.xzcit.zhangmengling.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据ID删除分类，删除前需检查是否关联菜品或套餐
     * @param id 分类ID
     */
    @Override
    public void removeWithCheck(Long id) {
        // 查询当前分类是否关联了菜品
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        long dishCount = dishService.count(dishQueryWrapper);

        if (dishCount > 0) {
            // 如果关联了菜品，则抛出业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        // 查询当前分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        long setmealCount = setmealService.count(setmealQueryWrapper);

        if (setmealCount > 0) {
            // 如果关联了套餐，则抛出业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        // 如果没有关联菜品和套餐，则可以删除
        super.removeById(id);
    }
}
