package com.smgeek.gkrpc;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 芳芳
 * @create 2022-03-22 20:23
 * @address
 * @desc 表示网络传输的一个端点
 **/
@Data
@AllArgsConstructor
public class Peer {
    private String host;
    private int port;
}
