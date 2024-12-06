package org.ject.momentia.api;

import org.springframework.boot.SpringApplication;

public class TestMomentiaApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(MomentiaApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
