package com.smgeek.gkrpc.server;

import com.smgeek.gkrpc.Request;
import com.smgeek.gkrpc.common.utils.ReflectionUtils;

/**
 * @author 芳芳
 * @create 2022-03-24 20:38
 * @address
 * @desc 调用具体服务
 **/
public class ServiceInvoker {
    public Object invoke(ServiceInstance service,
                         Request request){
        return ReflectionUtils.invoke(
                service.getTarget(),
                service.getMethod(),
                request.getParameters()
        );
    }
}
