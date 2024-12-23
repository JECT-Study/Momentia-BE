package org.ject.momentia.api.config;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {
	public static final List<String> ALLOW_URLS = List.of(
		"/v1/user/validation/**",
		"/v1/user/register",
		"/v1/user/login/**",
		"/v1/user/refresh"
	);
}

