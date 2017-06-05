package com.icarat.amazons3.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /**
   * Docket Configuration
   * 
   * @return
   */

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        .paths(PathSelectors.any()).build().pathMapping("/").apiInfo(apiInfo()).useDefaultResponseMessages(false);
  }

  /**
   * swagger apiInfo conFiguration
   * 
   * @return ApiInfoBuilder
   */

  @Bean
  public ApiInfo apiInfo() {
    final ApiInfoBuilder builder = new ApiInfoBuilder();
    builder.title("amazon S3 sample REST Api").version("1.0").license(" Copyright ")
        .description("The API provides a platform to work with amazon s3 buckets.");
    return builder.build();
  }

  /**
   * CorsConfiguration
   * 
   * @return FilterRegistrationBean
   */

  @Bean
  public FilterRegistrationBean corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
    bean.setOrder(0);
    return bean;
  }
}
