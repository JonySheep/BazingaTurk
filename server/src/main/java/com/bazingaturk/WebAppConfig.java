package com.bazingaturk;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

    public static String BASE = null;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String base = WebAppConfig.class.getClassLoader().getResource("").getPath();

        //判断是开发环境还是部署环境
        if(base.contains("BOOT-INF")){
            System.out.println("部署环境：");
            System.out.println(base);
            if(base.startsWith("file:/")){
                base = base.substring(5);
                System.out.println(base);
            }
            base = new File(base).getParentFile().getParentFile().getParentFile().getAbsolutePath();
            System.out.println(base);
            System.out.println("file:"+base+"/assets/");
            BASE = base;
        }else{
            System.out.println("开发环境：");
            base = new File(base).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getAbsolutePath();
            System.out.println(base);
            System.out.println("file:"+base+"/assets/");
            BASE = base;
        }

        registry.addResourceHandler("/server/**").addResourceLocations("file:"+base+"/assets/");
        super.addResourceHandlers(registry);
    }
}