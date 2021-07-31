package com.maskvote.maskvotecounter.Count;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.TreeMap;

public interface CountResult {
    /**
     * 计算最后的g的sum结果，我们已经知道g^sum * Y^R, 和Y^R，只需相除就可以得到最后的结果。
     */
    static TreeMap<String, BigInteger> countResult(BigInteger[] firstComMul, BigInteger[] YR, BigInteger[] arr){
        TreeMap<String, BigInteger> map = new TreeMap<>();
        for (int i = 0; i < firstComMul.length;i++){
            BigInteger newYR = YR[i].modPow(BigInteger.valueOf(-1), arr[0]).mod(arr[0]);
            BigInteger result = firstComMul[i].multiply(newYR).mod(arr[0]);
            map.put("vote"+(i+1)+"Result", result);
//            try {
//                FileWriter file = new FileWriter("vote"+(i+1)+"Result.txt");
//                file.write(result+"\n");
//                file.close();
////                System.out.println(result);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return map;
    }



    /**
     * 计算最后的g的sum结果，我们已经知道g^sum * Y^R, 和Y^R，只需相除就可以得到最后的结果。
     */
    static TreeMap<String, BigInteger> countResult1(BigInteger firstComMul, BigInteger YR, BigInteger[] arr){
        TreeMap<String, BigInteger> map = new TreeMap<>();
        BigInteger newYR = YR.modPow(BigInteger.valueOf(-1), arr[0]).mod(arr[0]);
        BigInteger result = firstComMul.multiply(newYR).mod(arr[0]);
        map.put("vote1Result", result);
        return map;
    }
}
