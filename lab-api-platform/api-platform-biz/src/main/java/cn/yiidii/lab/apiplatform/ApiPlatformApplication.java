package cn.yiidii.lab.apiplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * ApiPlatformApplication
 *
 * @author ed w
 * @since 1.0
 */
@ComponentScan("cn.yiidii")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cn.yiidii.lab")
public class ApiPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiPlatformApplication.class, args);
    }
}
