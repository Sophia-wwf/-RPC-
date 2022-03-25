package com.smgeek.gkrpc.example;

import com.smgeek.gkrpc.server.RpcServer;
import com.smgeek.gkrpc.server.RpcServerConfig;

import java.rmi.Naming;

/**
 * @author 芳芳
 * @create 2022-03-25 9:39
 * @address
 * @desc
 **/
public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer(new RpcServerConfig());
        server.register(CalcService.class, new CalcServiceImpl());
        server.start();
    }
}
