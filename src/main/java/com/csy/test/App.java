package com.csy.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan("com.csy.test.webui.**.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class App {
	
    public static void main( String[] args ){
        SpringApplication.run(App.class , args);
    }
}
