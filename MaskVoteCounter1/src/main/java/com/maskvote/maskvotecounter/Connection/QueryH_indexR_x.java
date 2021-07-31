package com.maskvote.maskvotecounter.Connection;

import org.hyperledger.fabric.gateway.Contract;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public interface QueryH_indexR_x {
    /**
     * 从链上获取每一个计票员上传的h_indexR_x,计算最后的Y_indexR
     */
    static String query(String str, Contract contract){
        //链上获取所有计票员h_indexR_x
        String result = null;
        try {
            byte[] queryAllAssets = contract.evaluateTransaction("get",str);
            result = new String(queryAllAssets, StandardCharsets.UTF_8);
//            System.out.println(result);
//            result = result.replaceAll("\\\\","");
//            FileWriter file = new FileWriter("allH_indexR_x.txt",true);
//            file.write(result+"\n");
//            file.close();
//            System.out.println(str+"已成功获取！");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
