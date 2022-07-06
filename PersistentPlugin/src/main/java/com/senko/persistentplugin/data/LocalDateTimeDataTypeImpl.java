package com.senko.persistentplugin.data;

import com.alibaba.fastjson.JSON;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * LocalDateTime类型
 * @author senko
 * @date 2022/7/6 13:10
 */
public class LocalDateTimeDataTypeImpl implements PersistentDataType<byte[], LocalDateTime> {

    /**
     * 获取原始类型
     */
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    /**
     * 获取复杂类型（就算我们要存储的对象class
     */
    @Override
    public Class<LocalDateTime> getComplexType() {
        return LocalDateTime.class;
    }

    /**
     * localDateTime对象序列化为byte[]
     *
     * 有一个更偷懒的方式，借用JSON工具类，
     * 把对象转换成JSON，再getBytes
     */
    @Override
    public byte[] toPrimitive(LocalDateTime complex, PersistentDataAdapterContext context) {
        if (Objects.isNull(complex)) {
            //如果对象为空，则返回 初始化的byte数组
            return new byte[0];
        }
        //如果不为空，则利用fastjson将LocalDateTime先转换成JSON，再getByte变成byte数组
        return JSON.toJSONBytes(complex);
    }

    /**
     * byte[]数组反序列化回LocalDateTime
     */
    @Override
    public LocalDateTime fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        if (Objects.isNull(primitive) || primitive.length <= 0) {
            //如果byte数组为空，则返回null值
            return null;
        }
        //不为空，则使用fastjson反序列化
        Object datetime = JSON.parseObject(primitive, LocalDateTime.class);
        return (LocalDateTime) datetime;
    }
}
