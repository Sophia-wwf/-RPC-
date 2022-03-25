package com.smgeek.gkrpc.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author 芳芳
 * @create 2022-03-24 10:20
 * @address
 * @desc 基于JSON的反序列化实现
 **/
public class JSONDecoder implements Decoder{

    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
