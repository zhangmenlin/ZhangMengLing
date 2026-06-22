package com.xzcit.zhangmengling.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xzcit.zhangmengling.common.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            userId = 1L;
        }

        this.setFieldValByName("createUser", userId, metaObject);
        this.setFieldValByName("updateUser", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);

        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            userId = 1L;
        }

        this.setFieldValByName("updateUser", userId, metaObject);
    }
}
