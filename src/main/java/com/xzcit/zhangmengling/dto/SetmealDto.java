package com.xzcit.zhangmengling.dto;

import com.xzcit.zhangmengling.entity.Setmeal;
import com.xzcit.zhangmengling.entity.SetmealDish;
import lombok.Data;

import java.util.List;
@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
