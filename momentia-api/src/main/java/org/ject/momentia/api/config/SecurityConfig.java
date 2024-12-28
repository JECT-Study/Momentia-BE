package org.ject.momentia.api.config;

import org.ject.momentia.api.mvc.filter.JwtFilter;
import org.ject.momentia.api.mvc.handler.JwtAccessDeniedHandler;
import org.ject.momentia.api.user.infra.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationConfiguration authenticationConfiguration;
	private final CorsConfigurationSource apiConfigurationSource;
	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CORS
		http.cors(cors -> cors.configurationSource(apiConfigurationSource));

		// CSRF (Disable)
		http.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable);

		// Stateless
		http.sessionManagement(session -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);

		// 경로별 인가 설정
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
			.requestMatchers(SecurityConstants.ALLOW_URLS.toArray(new String[0])).permitAll()
				.requestMatchers("/v1/**").permitAll()
				.anyRequest()
			.authenticated()
		);

		// 예외 처리 핸들러 설정
		http.exceptionHandling(configurer ->
			configurer.authenticationEntryPoint(new JwtAccessDeniedHandler())
		);

		// JwtFilter를 UsernamePasswordAuthenticationFilter 이전에 추가
		http.addFilterBefore(new JwtFilter(jwtTokenProvider),
			UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
