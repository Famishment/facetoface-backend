package com.lzj.facetoface.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 自定义 Swagger 接口文档的配置
 */
@Configuration
@EnableSwagger2WebMvc
@Profile({"dev","test"})  // 让这个Bean，在某些特定的环境中生效
public class SwaggerConfig {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //这里指定Controller扫描包路径(项目路径也行)
                .apis(RequestHandlerSelectors.basePackage("com.lzj.facetoface.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * api 信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户中心")
                .description("用户中心接口文档")
                .contact(new Contact("famishment","http://github/famishment","xxx@163.com"))
                .termsOfServiceUrl("http://github/famishment")
                .version("1.0.0")
                .build();
    }
}