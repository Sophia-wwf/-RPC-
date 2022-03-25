package com.smgeek.gkrpc.codec;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class JSONEncoderTest {
    @Test
    public void encode(){
        Encoder encoder = new JSONEncoder();

        TestBean bean = new TestBean();
        bean.setName("smgeek");
        bean.setAge(18);

        byte[] bytes = encoder.encode(bean);

        assertNotNull(bytes);
    }
}
