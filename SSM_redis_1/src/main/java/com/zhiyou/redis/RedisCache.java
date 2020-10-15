package com.zhiyou.redis;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisConnection;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisCache implements Cache{

	//定义一个ID
	private final String id;
	//创建读写锁对象
	private final ReadWriteLock rwl = new ReentrantReadWriteLock();
	//创建redis连接
	private static JedisConnectionFactory jcf;
	
	//为类中注入JedisConnectionFactory
	public static void setJedisConnection(JedisConnectionFactory jcf){
		RedisCache.jcf = jcf;
	}
	//通过构造方法给ID赋值
	public RedisCache(String id){
		this.id=id;
	}
	
	//这个id实际上就是缓存的key
	public String getId() {
		return id;
	}

	//将数据写入到缓存中（redis）
	public void putObject(Object key, Object value) {
		rwl.writeLock().lock();  //写数据时加入锁，防止自己写时别人也写
		RedisConnection redis = jcf.getConnection();	//获取redis连接
		//用于序列化数据（key,values）
		RedisSerializer<Object> ser = new JdkSerializationRedisSerializer(); 
		//将key与value序列化后放入redis中
		redis.set(ser.serialize(key), ser.serialize(value));
		System.out.println("--------------------添加二级缓存成功key："+key+"value:"+value);
		redis.close();
		rwl.writeLock().unlock();    //写入完成后，放开锁
	}

	//从缓存中取出数据
	public Object getObject(Object key) {
		rwl.readLock().lock();  //读锁
		//获取redis连接
		RedisConnection redis = jcf.getConnection();
		//用于序列化数据（key,values）
		RedisSerializer<Object> ser = new JdkSerializationRedisSerializer();
		//根据序列化后的key从redis中取值，然后再将redis中取出的数据进行反序列化
		Object object = ser.deserialize(redis.get(ser.serialize(key)));
		System.out.println("--------------------命中二级缓存成功value:"+object);
		redis.close();
		rwl.readLock().unlock();
		//将redis中取出的数据返回
		return object;
	}

	//从缓存中删除数据
	public Object removeObject(Object key) {
		rwl.writeLock().lock();  //写锁
		//获取redis连接
		RedisConnection redis = jcf.getConnection();
		//用于序列化数据（key,values）
		RedisSerializer<Object> ser = new JdkSerializationRedisSerializer();
		//根据序列化后的key,将redis中对应的生命周期设置为0
		Boolean expire = redis.expire(ser.serialize(key), 0);
		redis.close();
		rwl.writeLock().unlock();
		//将redis中取出的数据返回
		return expire;
	}

	//清空所有缓存
	public void clear() {
		rwl.readLock().lock();
		RedisConnection redis = jcf.getConnection();
		redis.flushDb();
		redis.flushAll();
		redis.close();
		rwl.readLock().unlock();
	}

	//获得缓存中数据量
	public int getSize() {
		RedisConnection redis = jcf.getConnection();
		Integer size = Integer.valueOf(redis.dbSize().toString());
		redis.close();
		return size;
	}

	//
	public ReadWriteLock getReadWriteLock() {
		
		return rwl;
	}

}
