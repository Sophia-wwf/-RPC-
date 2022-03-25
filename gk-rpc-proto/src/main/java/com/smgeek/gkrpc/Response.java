package com.smgeek.gkrpc;

import lombok.Data;

/**
 * @author 芳芳
 * @create 2022-03-22 20:35
 * @address
 * @desc 表示RPC的返回
 **/
@Data
public class Response {
    /**
     * 服务返回编码，0-成功，非0失败
     */
    private int code = 0;
    /**
     * 具体的错误信息
     */
    private String message = "ok";
    /**
     * 返回的数据
     */
    private Object data;
}
