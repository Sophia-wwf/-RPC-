package com.smgeek.gkrpc.client;

import com.smgeek.gkrpc.Request;
import com.smgeek.gkrpc.Response;
import com.smgeek.gkrpc.ServiceDescriptor;
import com.smgeek.gkrpc.codec.Decoder;
import com.smgeek.gkrpc.codec.Encoder;
import com.smgeek.gkrpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Queue;

/**
 * @author 芳芳
 * @create 2022-03-25 9:04
 * @address
 * @desc 调用远程服务的代理类
 **/
@Slf4j
public class RemoteInvoker implements InvocationHandler {
    private Class clazz;
    private Encoder encoder;
    private Decoder decoder;
    private TransportSelector selector;

    public RemoteInvoker(Class clazz,
                  Encoder encoder,
                  Decoder decoder,
                  TransportSelector selector){
        this.clazz = clazz;
        this.decoder = decoder;
        this.encoder = encoder;
        this.selector = selector;
    }

    @Override
    public Object invoke(Object proxy,
                         Method method,
                         Object[] args) throws Throwable {
        Request request = new Request();
        request.setService(ServiceDescriptor.from(clazz, method));
        request.setParameters(args);

        Response resp = invokeRemote(request);
        if(resp == null || resp.getCode()!=0){
            throw new IllegalStateException("fail to invoke remote: "+ resp);
        }
        return resp.getData();
    }

    private Response invokeRemote(Request request) {
        TransportClient client = null;
        Response resp = null;
        try{
            client = selector.select();

            byte[] outBytes = encoder.encode(request);
            InputStream revice = client.write(new ByteArrayInputStream(outBytes));

            byte[] intBytes = IOUtils.readFully(revice, revice.available());
            // response赋值
            resp = decoder.decode(intBytes, Response.class);

        } catch (IOException e){
            log.warn(e.getMessage(), e);
            resp = new Response();
            resp.setCode(1);
            resp.setMessage("RpcClient got error: "
                    + e.getClass()
                    +" : "+ e.getMessage());
        } finally {
            if(client != null){
                selector.release(client);
            }
        }

        return resp;
    }
}
