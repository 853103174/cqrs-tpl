package com.sdnc.common.redis;

import java.time.Duration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.sdnc.common.json.Snack2JsonRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * Redis配置信息
 *
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfigurer implements CachingConfigurer {

	/**
	 * 设置redis数据默认过期时间 设置@cacheable序列化方式
	 */
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
				.fromSerializer(new Snack2JsonRedisSerializer());
		return RedisCacheConfiguration.defaultCacheConfig()
				.serializeValuesWith(pair)
				.entryTtl(Duration.ofHours(6));
	}

	@Bean
	public RedisConnectionFactory connectionFactory(RedisProperties properties) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		JedisClientConfiguration clientConfig = JedisClientConfiguration
				.builder()
				.usePooling()
				.poolConfig(poolConfig)
				.and()
				.readTimeout(Duration.ofSeconds(2))
				.build();
		RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
		standaloneConfig.setHostName(properties.getHost());
		standaloneConfig.setPort(properties.getPort());
		standaloneConfig.setDatabase(properties.getDatabase());
		standaloneConfig.setPassword(properties.getPassword());

		return new JedisConnectionFactory(standaloneConfig, clientConfig);
	}

	@Bean
	@Primary
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new Snack2JsonRedisSerializer());
		// Hash的key也采用StringRedisSerializer的序列化方式
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new Snack2JsonRedisSerializer());
		template.afterPropertiesSet();

		return template;
	}

}
