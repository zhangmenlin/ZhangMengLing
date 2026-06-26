package com.xzcit.zhangmengling.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xzcit.zhangmengling.entity.Dish;
import com.xzcit.zhangmengling.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    @TableField(exist = false)
    private List<DishFlavor> flavors = new ArrayList<>();

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private Integer copies;
}
