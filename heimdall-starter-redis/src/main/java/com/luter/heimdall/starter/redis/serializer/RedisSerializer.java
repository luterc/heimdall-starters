package com.luter.heimdall.starter.redis.serializer;


import org.springframework.data.redis.serializer.SerializationException;

public interface RedisSerializer<T> {

    byte[] serialize(T t) throws SerializationException;

    T deserialize(byte[] bytes) throws SerializationException;
}
