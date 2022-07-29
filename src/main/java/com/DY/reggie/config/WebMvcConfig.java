package com.DY.reggie.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.DY.reggie.common.JacksonObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 大勇
 */
@Configuration
@Slf4j
@EnableSwagger2
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        registry.addResourceHandler("/","/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/","/front/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

    }

    /**
     * 扩展mvc的转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters){
        log.info("扩展消息转换器");
        //创建消息转换对象
        MappingJackson2HttpMessageConverter messageConverter =new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层将Jackson转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }

    /**
     * 配置docket以配置Swagger具体参数
     * @return
     */
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.DY.reggie"))
                .build();

    }

    private ApiInfo apiInfo(){
        Contact contact = new Contact("大勇","https://blog.csdn.net/zly03?spm=1000.2115.3001.5343","lyzhang@163.com");
        return new ApiInfo(
                "Swagger学习",//标题
                "测试",
                "v1.0",//版本
                "https://blog.csdn.net/zly03?spm=1000.2115.3001.5343",
                contact,
                "Apache 2.0许可",//许可
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()//扩展
        );
    }



}


