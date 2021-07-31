package com.maskvote.Login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public interface ReadPublicKey {
    /**
     * 读取投票者公钥
     */
    static BigInteger getPublicKey(String vote){
        FileReader file = null;
        BigInteger pk = null;
        try {
            file = new FileReader(vote+"PublicKey.txt");
            BufferedReader br = new BufferedReader(file);
            String str = br.readLine();
            pk = new BigInteger(str);
            br.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pk;
    }
}
