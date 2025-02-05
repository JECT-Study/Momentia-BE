package org.ject.momentia.api.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
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

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		// 1. 기존 코드
		// RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
		// config.setPassword(password);
		// return new LettuceConnectionFactory(config);

		// 2.
		List<String> clusterNodes = Arrays.asList(clusterNodesString.split(","));
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
		redisClusterConfiguration.setPassword(password);
		return new LettuceConnectionFactory(redisClusterConfiguration);

		// 3.
		// //----------------- (1) Socket Option
		// SocketOptions socketOptions = SocketOptions.builder()
		// 	.connectTimeout(Duration.ofMillis(100L))
		// 	.keepAlive(true)
		// 	.build();
		//
		// //----------------- (2) Cluster topology refresh 옵션
		// ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
		// 	.builder()
		// 	.dynamicRefreshSources(true)
		// 	.enableAllAdaptiveRefreshTriggers()
		// 	.enablePeriodicRefresh(Duration.ofSeconds(30))
		// 	.build();
		//
		// //----------------- (3) Cluster client 옵션
		// ClusterClientOptions clusterClientOptions = ClusterClientOptions
		// 	.builder()
		// 	.pingBeforeActivateConnection(true)
		// 	.autoReconnect(true)
		// 	.socketOptions(socketOptions)
		// 	.topologyRefreshOptions(clusterTopologyRefreshOptions)
		// 	.maxRedirects(3).build();
		//
		// //----------------- (4) Lettuce Client 옵션
		// final LettuceClientConfiguration clientConfig = LettuceClientConfiguration
		// 	.builder()
		// 	.commandTimeout(Duration.ofMillis(150L))
		// 	.clientOptions(clusterClientOptions)
		// 	.build();
		//
		// List<String> clusterNodes = Arrays.asList(clusterNodesString.split(","));
		// RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(clusterNodes);
		// clusterConfig.setMaxRedirects(3);
		// clusterConfig.setPassword("password");
		//
		// LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig, clientConfig);
		//
		// //----------------- (5) LettuceConnectionFactory 옵션
		// factory.setValidateConnection(false);
		//
		// return factory;
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		return redisTemplate;
	}

}