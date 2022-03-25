package com.smgeek.gkrpc.example;

import com.smgeek.gkrpc.client.RpcClient;

/**
 * @author 芳芳
 * @create 2022-03-25 9:39
 * @address
 * @desc
 **/
public class Client {
    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        CalcService service = client.getProxy(CalcService.class);

        int r1  = service.add(1, 2);
        int r2 = service.minus(10, 8);

        System.out.println(r1);
        System.out.println(r2);
    }
}
