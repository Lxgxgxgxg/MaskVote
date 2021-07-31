package com.maskvote.maskvoter.Encrypto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public interface ReadUnionPublicKey {
    /**
     * 读取从链上获取到的联合公钥
     */
    static BigInteger getUnionPublicKey(){
        FileReader file = null;
        BigInteger upk = null;
        try {
            file = new FileReader("UnionPublicKey.txt");
            BufferedReader br = new BufferedReader(file);
            String str = br.readLine();
            upk = new BigInteger(str);
            br.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upk;
    }
}
