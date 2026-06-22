package com.xzcit.zhangmengling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzcit.zhangmengling.entity.Category;

public interface CategoryService extends IService<Category> {

    /**
     * 根据ID删除分类，删除前需检查是否关联菜品或套餐
     * @param id 分类ID
     */
    void removeWithCheck(Long id);
}
