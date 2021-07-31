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
public interface Query {
    static String query(String str, Contract contract){
        //链上获取所有计票员的公钥，准备计算联合公钥的计算
        String result = null;
        try {
            byte[] queryAllAssets = contract.evaluateTransaction("get",str);
            result = new String(queryAllAssets, StandardCharsets.UTF_8);
//            result = result.replaceAll("\\\\","");
//            FileWriter file = new FileWriter(str+".txt");
//            file.write(result+"\n");
//            file.close();
//            System.out.println(str+"公钥已成功获取！");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
