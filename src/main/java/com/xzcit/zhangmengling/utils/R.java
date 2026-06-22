package com.xzcit.zhangmengling.utils;

import lombok.Data;

@Data
public class R<T> {
    private Integer code;
    private T data;
    private String msg;

    // 成功响应 code=1
    public static <T> R<T> success(T data) {
        R<T> r = new R<>();
        r.setCode(1);
        r.setData(data);
        return r;
    }

    // 失败响应 code=0
    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.setCode(0);
        r.setMsg(msg);
        return r;
    }
}
