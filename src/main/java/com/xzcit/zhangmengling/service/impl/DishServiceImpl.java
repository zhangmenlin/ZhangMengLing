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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private SetmealDishService setmealDishService;

    /**
     * 新增菜品及其对应口味
     * @param dishDto 菜品DTO对象
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 1. 将DTO转换为Dish实体并保存菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        this.save(dish);

        // 2. 获取保存后的菜品ID
        Long dishId = dish.getId();

        // 3. 保存菜品口味列表
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishFlavorService.saveBatch(flavors);
        }
    }

    /**
     * 根据id查询菜品及其对应口味
     * @param id 菜品ID
     * @return 菜品DTO对象
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 1. 根据ID查询菜品基本信息
        Dish dish = this.getById(id);
        if (dish == null) {
            return null;
        }

        // 2. 将Dish转换为DishDto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 3. 根据菜品ID查询对应的口味列表
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 修改菜品及其对应口味
     * @param dishDto 菜品DTO对象
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 1. 更新菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        this.updateById(dish);

        // 2. 删除原有的口味列表
        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(queryWrapper);

        // 3. 保存新的口味列表
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishFlavorService.saveBatch(flavors);
        }
    }

    /**
     * 批量删除菜品（删除前校验是否被套餐绑定）
     * @param ids 菜品ID列表
     */
    @Override
    @Transactional
    public void removeBatchWithCheck(List<Long> ids) {
        // 1. 校验菜品是否绑定套餐
        LambdaQueryWrapper<SetmealDish> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.in(SetmealDish::getDishId, ids);
        long bindCount = setmealDishService.count(setmealWrapper);
        if (bindCount > 0) {
            throw new CustomException("选中菜品已关联套餐，无法删除，请先解除套餐绑定");
        }

        // 2. 删除对应的口味列表
        LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
        flavorWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(flavorWrapper);

        // 3. 批量删除菜品
        this.removeBatchIds(ids);
    }
}
