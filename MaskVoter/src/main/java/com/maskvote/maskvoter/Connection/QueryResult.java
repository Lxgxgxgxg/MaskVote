package com.maskvote.maskvoter.Connection;

import org.hyperledger.fabric.gateway.Contract;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public interface QueryResult {
    /**
     *@描述 从链上获取最后的投票结果
     *@参数 query str, contract
     *@返回值 string
     *@创建人 lxgxgxgxg
     *@创建时间
     */
    static String queryResult(String str, Contract contract){
        String result = null;
        try {
            byte[] voteResult = contract.evaluateTransaction("get",str);
            result = new String(voteResult, StandardCharsets.UTF_8);
//            result = result.replaceAll("\\\\","");
            System.out.println("此次投票的最后结果："+result+", 请查表！");
//            FileWriter file = new FileWriter("voteResult.txt");
//            file.write(result+"\n");
//            file.close();
//            System.out.println("联合公钥已成功获取！");
//            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }
}
