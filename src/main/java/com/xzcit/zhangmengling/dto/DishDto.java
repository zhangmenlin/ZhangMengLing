package com.xzcit.zhangmengling.dto;

import com.xzcit.zhangmengling.entity.Dish;
import com.xzcit.zhangmengling.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
