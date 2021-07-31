package com.maskvote.maskvotecounter.Zkp;

import java.math.BigInteger;

public interface IndexEqualsProof {
    //指数相等证明
    static boolean indexEqualsProof(BigInteger firstCom, BigInteger upk, BigInteger secondCom, BigInteger indexEqualT, BigInteger indexEqualS1, BigInteger indexEqualS2, BigInteger indexEqualS3, BigInteger[] arr){
        boolean result;
        String str2 = arr[2].toString() + upk.toString() + arr[3].toString()+ indexEqualT.toString();
        BigInteger c = new BigInteger(HashFunction.getSHA256StrJava(str2)).mod(arr[1]);
//        System.out.println(c);
        BigInteger t1 = arr[2].modPow(indexEqualS1, arr[0]).multiply(upk.modPow(indexEqualS2, arr[0])).multiply(arr[3].modPow(indexEqualS3, arr[0])).multiply((firstCom.multiply(secondCom)).modPow(c,arr[0])).mod(arr[0]);
        BigInteger t2 = indexEqualS1.multiply(BigInteger.valueOf(0)).add(indexEqualS2.multiply(BigInteger.valueOf(1))).add(indexEqualS3.multiply(BigInteger.valueOf(-1)));
//        System.out.println("t:"+t);
//        System.out.println("t1:"+t1);
//        System.out.println("t2:"+t2);
        if (indexEqualT.equals(t1) && t2.equals(BigInteger.valueOf(0))){
            result = true;
        }else{
            result = false;
        }
        return result;
    }
}
