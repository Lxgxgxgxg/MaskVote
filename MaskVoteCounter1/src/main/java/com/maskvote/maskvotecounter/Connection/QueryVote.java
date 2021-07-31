package com.maskvote.maskvotecounter.Connection;

import org.hyperledger.fabric.gateway.Contract;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 16:22 2021/3/29
 * @ Description：
 * @ Version: 1.0
 */
public interface QueryVote {
    static String query(String str, Contract contract){
        //链上获取所有投票员的投票信息
        String result = null;
        try {
            byte[] voteInformation = contract.evaluateTransaction("get",str);
            result = new String(voteInformation, StandardCharsets.UTF_8);
            result = result.replaceAll("\\\\","");
//            FileWriter file = new FileWriter("voteInformation.txt",true);
//            file.write(result+"\n");
//            file.close();
//            System.out.println(str + "选票信息已成功获取！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
