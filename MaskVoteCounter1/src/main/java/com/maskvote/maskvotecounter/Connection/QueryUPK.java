package com.maskvote.maskvotecounter.Connection;

import org.hyperledger.fabric.gateway.Contract;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lxq
 * \* Date: 2021/3/19
 * \* Time: 19:14
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public interface QueryUPK {
    static void query(String str, Contract contract){
        //先从链上获取联合公钥
        try {
            byte[] upk = contract.evaluateTransaction("get",str);
            String result = new String(upk, StandardCharsets.UTF_8);
//            result = result.replaceAll("\\\\","");
//            System.out.println(result);
            FileWriter file = new FileWriter("UnionPublicKey.txt");
            file.write(result+"\n");
            file.close();
            System.out.println("联合公钥已成功获取！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
