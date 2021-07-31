package com.maskvote.Register;

import java.math.BigInteger;
import java.security.SecureRandom;

public interface GetPrivateKey {
    /**
     * 得到私钥
     */
    static BigInteger getPrivateKey(BigInteger[] arr){
        SecureRandom seRandom = new SecureRandom();
        BigInteger x = new BigInteger(100, seRandom);

        while(x.compareTo(arr[1].subtract(BigInteger.valueOf(2))) == 1 || x.compareTo(BigInteger.valueOf(2)) == -1){
            x = new BigInteger(100, seRandom);
        }
        return x;
    }
}
