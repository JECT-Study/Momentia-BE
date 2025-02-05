package org.ject.momentia.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"org.ject.momentia.api"})
@EntityScan(basePackages = {"org.ject.momentia.common.domain"})
@EnableJpaAuditing
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = {"org.ject.momentia.api.config.property"})
@EnableScheduling
public class MomentiaApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(MomentiaApiApplication.class, args);
	}
}
