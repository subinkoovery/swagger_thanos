package com.swager.prethanos;

import com.swager.prethanos.entity.SwaggerSpec;
import com.swager.prethanos.repository.SwaggerSpecRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class SwaggerWsEndpointsConfig {

    @Autowired
    SwaggerSpecRepository swaggerSpecRepository;

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider) {
        return () -> {

            List<SwaggerResource> resources = new ArrayList<>();
            log.info("Available swagger definition : "+swaggerSpecRepository.findAllByOrderByPriority().size());
            for (SwaggerSpec swaggerSpec:swaggerSpecRepository.findAll()){
                SwaggerResource wsResource = new SwaggerResource();
                wsResource.setName(swaggerSpec.getName());
                wsResource.setSwaggerVersion(swaggerSpec.getVersion());
                wsResource.setUrl(swaggerSpec.getUrl());


                resources.add(wsResource);

            }

            return resources;
        };
    }
}