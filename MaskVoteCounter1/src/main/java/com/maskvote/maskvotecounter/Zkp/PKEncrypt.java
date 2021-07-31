package com.maskvote.maskvotecounter.Zkp;

import java.math.BigInteger;

public interface PKEncrypt {
    static boolean pKEcryptProof(BigInteger firstCom, BigInteger upk,  BigInteger PKEncryptTemp, BigInteger PKEncryptHash, BigInteger PKEncryptS1,
                                 BigInteger PKEncryptS2, BigInteger[] arr){
        BigInteger newHashc = new BigInteger(com.maskvote.maskvotecounter.Zkp.HashFunction.getSHA256StrJava(arr[2].toString()+upk.toString()+PKEncryptTemp.toString())).mod(arr[1]);
        BigInteger temp1 = arr[2].modPow(PKEncryptS1, arr[0]).multiply(upk.modPow(PKEncryptS2, arr[0])).multiply(firstCom.modPow(newHashc, arr[0])).mod(arr[0]);

        if (PKEncryptHash.equals(newHashc) && temp1.equals(PKEncryptTemp)){
            return true;
        }else {
            return false;
        }
    }
}
