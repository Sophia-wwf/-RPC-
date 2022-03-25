package com.smgeek.gkrpc.transport;

import com.smgeek.gkrpc.Request;

/**
 * @author 芳芳
 * @create 2022-03-24 10:43
 * @address
 * @desc
 * 1.启动、监听
 * 2.接受请求
 * 3.关闭监听
 **/
public interface TransportServer {
    void init(int port, RequestHandler handler);

    void start();

    void stop();
}
