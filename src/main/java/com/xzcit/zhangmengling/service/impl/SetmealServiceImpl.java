package com.xzcit.zhangmengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzcit.zhangmengling.dto.SetmealDto;
import com.xzcit.zhangmengling.entity.Setmeal;
import com.xzcit.zhangmengling.entity.SetmealDish;
import com.xzcit.zhangmengling.mapper.SetmealMapper;
import com.xzcit.zhangmengling.service.SetmealDishService;
import com.xzcit.zhangmengling.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto){
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    public SetmealDto getByIdWithDish(Long id){
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        queryWrapper.orderByAsc(SetmealDish::getSort);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto){
        this.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes = setmealDishes.stream().map((item)->{
                item.setSetmealId(setmealDto.getId());
                return item;
            }).collect(Collectors.toList());
            setmealDishService.saveBatch(setmealDishes);
        }
    }

    @Transactional
    public void delByIdWithDish(List<Long> ids){
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<>();
        qw.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(qw);
    }

    public SetmealDto getSetmealDtoById(Long id){
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> dishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(dishes);
        return setmealDto;
    }
}
