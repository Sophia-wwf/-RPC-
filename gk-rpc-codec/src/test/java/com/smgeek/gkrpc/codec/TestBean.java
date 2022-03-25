package com.smgeek.gkrpc.codec;

import lombok.Data;

/**
 * @author 芳芳
 * @create 2022-03-24 10:22
 * @address
 * @desc
 **/
@Data
public class TestBean {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
