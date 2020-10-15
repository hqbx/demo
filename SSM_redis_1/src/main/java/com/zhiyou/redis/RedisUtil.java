package com.zhiyou.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;

//静态注入中间类
@Component
public class RedisUtil {

	/*
	 * 由于RedisCache不能被spring直接管理，所以无法直接在里面获得JedisConnectionFactory对象，
	 * 所以需要通过这个类，让这个类被IOC容器管理，然后从容器中取出JedisConnectionFactory对象，
	 * 调用RedisCache类中的方法
	 */
	@Autowired
	public void setJedisConnection(JedisConnectionFactory jcf){
		RedisCache.setJedisConnection(jcf);
	}
}
