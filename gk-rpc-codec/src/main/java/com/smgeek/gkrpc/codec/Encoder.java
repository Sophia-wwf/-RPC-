package com.smgeek.gkrpc.codec;

/**
 * @author 芳芳
 * @create 2022-03-24 10:13
 * @address
 * @desc 序列化
 **/
public interface Encoder {
    byte[] encode(Object obj);
}
