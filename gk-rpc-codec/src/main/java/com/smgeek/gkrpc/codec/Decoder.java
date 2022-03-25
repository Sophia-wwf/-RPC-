package com.smgeek.gkrpc.codec;

/**
 * @author 芳芳
 * @create 2022-03-24 10:14
 * @address
 * @desc 反序列化
 **/
public interface Decoder {
    <T> T decode(byte[] bytes, Class<T> clazz);
}
