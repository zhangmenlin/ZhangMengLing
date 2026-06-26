package com.xzcit.zhangmengling.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page; // Add this import
import com.baomidou.mybatisplus.extension.service.IService;
import com.xzcit.zhangmengling.dto.DishDto;
import com.xzcit.zhangmengling.entity.Dish;
import java.util.List;

/**
 * 菜品服务接口
 */
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);
    public void delByIdWithFlavor(List<Long> ids);
}
