package com.smgeek.gkrpc;

import lombok.Data;

/**
 * @author 芳芳
 * @create 2022-03-22 20:31
 * @address
 * @desc 表示RPC的一个请求
 **/
@Data
public class Request {
    private ServiceDescriptor service;
    private Object[] parameters;
}
