package com.maskvote.maskvotecounter.Count;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public interface CalcuYR {
    static BigInteger calcuHIndexR1(BigInteger[] arr, BigInteger...  hRX){
        BigInteger YR = new BigInteger("1");
        for (int i = 0; i < hRX.length;i++){
            YR = YR.multiply(hRX[i]).mod(arr[0]);
        }
//        try {
//            FileWriter file = new FileWriter("vote1"+"Y_indexR.txt");
//            file.write(YR+"\n");
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return YR;
    }


    static BigInteger calcuHIndexR2(BigInteger[] arr, BigInteger...  hRX){
        BigInteger YR = new BigInteger("1");
        for (int i = 0; i < hRX.length;i++){
            YR = YR.multiply(hRX[i]).mod(arr[0]);
        }
//        try {
//            FileWriter file = new FileWriter("vote2"+"Y_indexR.txt");
//            file.write(YR+"\n");
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return YR;
    }


    static BigInteger calcuHIndexR3(BigInteger[] arr, BigInteger...  hRX){
        BigInteger YR = new BigInteger("1");
        for (int i = 0; i < hRX.length;i++){
            YR = YR.multiply(hRX[i]).mod(arr[0]);
        }
//        try {
//            FileWriter file = new FileWriter("vote3"+"Y_indexR.txt");
//            file.write(YR+"\n");
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return YR;
    }
}
