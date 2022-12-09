package com.framework.base.utils;

public class NumberUtils {
    public static void getRandomNumber(){
        //生成6位随机数字
        System.out.println((int)((Math.random()*9+1)*100000));
        //生成5位随机数字
        System.out.println((int)((Math.random()*9+1)*10000));
        //生成4位随机数字
        System.out.println((int)((Math.random()*9+1)*1000));
        //生成3位随机数字
        System.out.println((int)((Math.random()*9+1)*100));
        //生成2位随机数字
        System.out.println((int)((Math.random()*9+1)*10));
        //生成1位随机数字
        System.out.println((int)((Math.random()*9+1)));
    }
}
