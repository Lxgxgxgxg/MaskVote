package com.maskvote.maskvotecounter.Zkp;

import java.math.BigInteger;

public interface If0Else1Proof {
    //证明非0即1
    static boolean bit01Proof(BigInteger firstCom,BigInteger upk, BigInteger firstComTemp1, BigInteger firstComTemp2, BigInteger hash_challenge, BigInteger firstComResponse1, BigInteger firstComResponse2, BigInteger firstComResponse3, BigInteger[] arr){
        boolean result;
        BigInteger temp1 = (firstCom.modPow(hash_challenge, arr[0]).multiply(firstComTemp1)).mod(arr[0]);
//        System.out.println("temp1="+temp1);
        BigInteger temp2 = (arr[2].modPow(firstComResponse1, arr[0]).multiply(upk.modPow(firstComResponse2, arr[0]))).mod(arr[0]);
//        System.out.println("temp2="+temp2);

        BigInteger temp3 = (firstCom.modPow(hash_challenge.subtract(firstComResponse1), arr[0]).multiply(firstComTemp2)).mod(arr[0]);
//        System.out.println("temp3="+temp3);
        BigInteger temp4 = (arr[2].modPow(BigInteger.valueOf(0), arr[0]).multiply(upk.modPow(firstComResponse3, arr[0]))).mod(arr[0]);
//        System.out.println("temp4="+temp4);

        BigInteger new_hash_challenge = new BigInteger(HashFunction.getSHA256StrJava(firstComTemp1.toString()+firstComTemp2.toString())).mod(arr[1]);
//        System.out.println("hash      =" + hash_x);
//        System.out.println("new_hash_x="+new_hash_x);

        if(temp1.equals(temp2) && temp3.equals(temp4) && hash_challenge.equals(new_hash_challenge)){
            result = true;
        }else{
            result = false;
        }

        return result;
    }
}
