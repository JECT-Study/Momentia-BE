package org.ject.momentia.api.config;

import lombok.RequiredArgsConstructor;
import org.ject.momentia.api.mvc.handler.MomentiaUserArgumentHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MomentiaUserArgumentHandler momentiaUserArgumentHandler;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(momentiaUserArgumentHandler);
    }
}