package com.smgeek.gkrpc.example;

/**
 * @author 芳芳
 * @create 2022-03-25 9:40
 * @address
 * @desc
 **/
public class CalcServiceImpl implements CalcService {
    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public int minus(int a, int b) {
        return a-b;
    }
}
