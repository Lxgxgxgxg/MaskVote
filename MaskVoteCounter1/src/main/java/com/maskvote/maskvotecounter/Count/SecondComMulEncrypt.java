package com.maskvote.maskvotecounter.Count;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.TreeMap;

public interface SecondComMulEncrypt {
    static TreeMap<String, BigInteger> secondComMulEncrypt(BigInteger[] hIndexR, BigInteger sk, BigInteger[] arr){
        /**
         * 计票员用自己的私钥对选票累乘的第二部分进行加密，然后上传至链上
         * 这里是对三张选票的第二部分进行加密
         */

        /**
         * 这里直接就是对生成的三个选票的第二部分的加密加入到map中
         */
        TreeMap<String, BigInteger> map = new TreeMap<>();
        for (int i = 0;i<hIndexR.length;i++){
            BigInteger temp = hIndexR[i].modPow(sk, arr[0]).mod(arr[0]);
            map.put("vote"+(i+1)+"H_indexR_x", temp);
//            try {
//                FileWriter file = new FileWriter(str+"vote"+(i+1)+"H_indexR_x.txt");
//                file.write(temp+"\n");
//                file.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

//        try {
//            FileWriter file = new FileWriter(str+"H_indexR_x.txt");
//            file.write(temp+"\n");
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return map;
    }



    static BigInteger secondComMulEncrypt(BigInteger hIndexR, BigInteger sk, BigInteger[] arr){
        /**
         * 计票员用自己的私钥对选票累乘的第二部分进行加密，然后上传至链上
         */
        BigInteger temp = hIndexR.modPow(sk, arr[0]).mod(arr[0]);
//        try {
//            FileWriter file = new FileWriter(str+"H_indexR_x.txt");
//            file.write(temp+"\n");
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return temp;
    }
}
