package com.maskvote.maskvoter.Encrypto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public interface ReadBase {
    /**
     * 读取大素数群的基本参数
     */
    static BigInteger[] readGroupBase(){
        BigInteger p = null;
        BigInteger q = null;
        BigInteger g = null;
        BigInteger h = null;
        try {
            FileReader file = new FileReader("group.txt");
            BufferedReader br = new BufferedReader(file);
            String pInput = br.readLine();
            String qInput = br.readLine();
            String gInput = br.readLine();
            String hInput = br.readLine();
            p = new BigInteger(pInput);
            q = new BigInteger(qInput);
            g = new BigInteger(gInput);
            h = new BigInteger(hInput);
            br.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigInteger[] {p,q,g,h};
    }
}
