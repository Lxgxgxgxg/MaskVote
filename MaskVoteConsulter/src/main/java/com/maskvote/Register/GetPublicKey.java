package com.maskvote.Register;

import java.math.BigInteger;
import java.security.SecureRandom;

public interface GetPublicKey {
    /**
     * 得到公钥
     */
    static BigInteger getPublicKey(BigInteger[] arr, BigInteger privateKey){
        SecureRandom seRandom = new SecureRandom();
        BigInteger y = arr[3].modPow(privateKey, arr[0]).mod(arr[0]);
        return y;
    }
}
