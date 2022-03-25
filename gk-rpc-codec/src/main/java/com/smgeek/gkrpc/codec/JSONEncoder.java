package com.smgeek.gkrpc.codec;

import com.alibaba.fastjson.JSON;

/**
 * @author 芳芳
 * @create 2022-03-24 10:16
 * @address
 * @desc 基于json的序列化实现
 **/
public class JSONEncoder implements Encoder {
    @Override
    public byte[] encode(Object obj) {
        return JSON.toJSONBytes(obj);
    }
}
