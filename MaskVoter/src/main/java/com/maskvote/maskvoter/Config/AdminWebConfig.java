package com.maskvote.maskvoter.Config;

import com.maskvote.maskvoter.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 16:22 2021/5/26
 * @ Description：
 * @ Version: 1.0
 */

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(("/**"))
                .excludePathPatterns("/", "/login", "/css/**", "/images/**", "/js/**", "/voteRegister");
    }
}
