package com.waihai.usercenter.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                // 接口文档标题
                .info(new Info().title("伙伴匹配系统接口文档")
                        // 接口文档简介
                        .description("这是接口文档的介绍")
                        // 接口文档版本
                        .version("0.0.1-SNAPSHOT")
                        // 开发者联系方式
                        .contact(new Contact().name("外害")
                                .email("20000000@qq.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("这是一份介绍")
                        .url("http://127.0.0.1:8888"));
    }
}
