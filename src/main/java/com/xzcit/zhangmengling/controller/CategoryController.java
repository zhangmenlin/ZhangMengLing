package com.xzcit.zhangmengling.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzcit.zhangmengling.common.CustomException;
import com.xzcit.zhangmengling.common.R;
import com.xzcit.zhangmengling.entity.Category;
import com.xzcit.zhangmengling.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增分类：{}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 根据类型查询分类列表
     *
     * @param type 1菜品分类 2套餐分类
     */
    @GetMapping("/list")
    public R<List<Category>> list(Integer type) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null, Category::getType, type);
        queryWrapper.orderByAsc(Category::getSort);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据ID查询分类信息
     */
    @GetMapping("/{id}")
    public R<Category> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return R.success(category);
    }

    /**
     * 修改分类
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类：{}", category);
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 根据ID删除分类
     */
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable String id) {
        log.info("========== Controller接收删除请求，分类ID(字符串)：{} ==========", id);

        try {
            Long categoryId = Long.parseLong(id);
            log.info("========== 转换后的Long类型ID：{} ==========", categoryId);

            categoryService.removeWithCheck(categoryId);
            log.info("========== Controller：删除分类成功，ID：{} ==========", categoryId);
            return R.success("删除分类成功");
        } catch (NumberFormatException e) {
            log.error("========== ID格式错误：{} ==========", id);
            return R.error("分类ID格式错误");
        } catch (CustomException e) {
            log.error("========== Controller：业务异常 - {} ==========", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("========== Controller：系统异常 - {}", e.getMessage(), e);
            return R.error("删除分类失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询
     *
     * @param page     页码
     * @param pageSize 每页条数
     */
    // ... existing code ...
    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize, Integer type) {
        log.info("分页查询分类，page={}, pageSize={}, type={}", page, pageSize, type);

        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        if (type != null) {
            queryWrapper.eq(Category::getType, type);
        }

        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo, queryWrapper);

        log.info("查询结果：总记录数={}, 当前页={}, 总页数={}",
                pageInfo.getTotal(), pageInfo.getCurrent(), pageInfo.getPages());

        return R.success(pageInfo);
    }
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable Long id) {
        log.info("删除分类，id：{}", id);
        categoryService.removeWithCheck(id);
        return R.success("删除分类成功");
    }
}