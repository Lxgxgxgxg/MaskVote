package com.maskvote.maskvoter.Encrypto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

public interface ReadVotier {
    /**
     * 读文件的函数
     */
    static BigInteger[] readFile(String str){
        FileReader file = null;
        BigInteger[] g = new BigInteger[3];
        String str1 = null;
        try {
            file = new FileReader(str);
            BufferedReader br = new BufferedReader(file);
            int i = 0;
            while((str1 = br.readLine()) != null){
                g[i] = new BigInteger(str1);
                i++;
            }
            br.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return g;
    }
}
