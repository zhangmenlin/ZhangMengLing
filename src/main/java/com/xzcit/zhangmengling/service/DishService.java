package com.xzcit.zhangmengling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzcit.zhangmengling.dto.DishDto;
import com.xzcit.zhangmengling.entity.Dish;
import java.util.List;

/**
 * 菜品服务接口
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品及其对应口味
     *
     * @param dishDto 菜品DTO对象，包含菜品信息和口味列表
     */
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品及其对应口味
     *
     * @param id 菜品id
     * @return 菜品DTO对象
     */
    DishDto getByIdWithFlavor(Long id);

    /**
     * 修改菜品及其对应口味
     *
     * @param dishDto 菜品DTO对象
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * 批量删除菜品（删除前校验是否被套餐绑定）
     *
     * @param ids 菜品id列表
     */
    void removeBatchWithCheck(List<Long> ids);
}
