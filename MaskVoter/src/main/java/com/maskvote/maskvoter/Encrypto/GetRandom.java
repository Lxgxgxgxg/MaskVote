package com.maskvote.maskvoter.Encrypto;

import java.math.BigInteger;
import java.security.SecureRandom;

public interface GetRandom {
    /**
     * 得到承诺的随机数
     */
    static BigInteger getRandom(BigInteger[] arr){
        SecureRandom seRandom = new SecureRandom();
        BigInteger r = new BigInteger(100, seRandom);

        while(r.compareTo(arr[1].subtract(BigInteger.valueOf(2))) == 1 || r.compareTo(BigInteger.valueOf(2)) == -1){
            r = new BigInteger(100, seRandom);
        }
        return r;
    }

}
