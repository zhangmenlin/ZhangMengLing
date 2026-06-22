package com.xzcit.zhangmengling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.xzcit.zhangmengling.mapper")
public class ZhangMengLingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhangMengLingApplication.class, args);
    }

}
