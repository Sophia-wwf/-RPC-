package com.smgeek.gkrpc.server;

import com.smgeek.gkrpc.codec.Decoder;
import com.smgeek.gkrpc.codec.Encoder;
import com.smgeek.gkrpc.codec.JSONDecoder;
import com.smgeek.gkrpc.codec.JSONEncoder;
import com.smgeek.gkrpc.transport.HTTPTransportServer;
import com.smgeek.gkrpc.transport.TransportServer;
import lombok.Data;

/**
 * @author 芳芳
 * @create 2022-03-24 11:09
 * @address
 * @desc server配置
 **/
@Data
public class RpcServerConfig {
    private Class<? extends TransportServer> transportClass = HTTPTransportServer.class;
    private Class<? extends Encoder> encoderClass = JSONEncoder.class;
    private Class<? extends Decoder> decoderClass = JSONDecoder.class;
    private int port = 3000;
}
