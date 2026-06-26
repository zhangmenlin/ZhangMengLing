package com.xzcit.zhangmengling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzcit.zhangmengling.dto.SetmealDto;
import com.xzcit.zhangmengling.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public SetmealDto getByIdWithDish(Long id);

    public void updateWithDish(SetmealDto setmealDto);

    public void delByIdWithDish(List<Long> ids);
    public SetmealDto getSetmealDtoById(Long id);
}
