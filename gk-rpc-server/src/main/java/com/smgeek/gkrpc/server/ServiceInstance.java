package com.smgeek.gkrpc.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author 芳芳
 * @create 2022-03-24 20:06
 * @address
 * @desc 表示一个具体服务
 **/
@Data
@AllArgsConstructor
public class ServiceInstance {
    private Object target;
    private Method method;
}
