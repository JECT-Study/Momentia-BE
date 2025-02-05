package org.ject.momentia.api.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(
	enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
	shadowCopy = RedisKeyValueAdapter.ShadowCopy.OFF,
	keyspaceNotificationsConfigParameter = ""
)
public class redisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Value("${spring.data.redis.password}")
	private String password;

	@Value("${spring.data.redis.cluster.nodes}")
	private String clusterNodesString;

	@Value("${spring.data.redis.type}")
	private String type;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		if (type.equals("CLUSTER")) {
			List<String> clusterNodes = Arrays.asList(clusterNodesString.split(","));
			RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
			redisClusterConfiguration.setPassword(password);
			return new LettuceConnectionFactory(redisClusterConfiguration);
		} else {
			RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
			config.setPassword(password);
			return new LettuceConnectionFactory(config);
		}

	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}

}