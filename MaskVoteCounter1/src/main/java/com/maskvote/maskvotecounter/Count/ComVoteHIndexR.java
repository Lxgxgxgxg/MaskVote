package com.maskvote.maskvotecounter.Count;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public interface ComVoteHIndexR {
    /**
     * 读取每一个计票员对每一个候选项生成的第二部分的加密
     * @param str
     * @return
     */
    static String[] readAllVotehindexR(String str){
        FileReader file = null;
        String[] hRInfor = new String[3];
        String str1 = null;
        try {
            file = new FileReader(str);
            BufferedReader br = new BufferedReader(file);
            int i = 0;
            while((str1 = br.readLine()) != null){
                hRInfor[i] =str1;
                i++;
            }
            br.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hRInfor;
    }


    static TreeMap<String, BigInteger> comAllHR(HashMap<String, String> HXRMap, BigInteger[] arr){
        TreeMap<String, BigInteger> map = new TreeMap<>();
//        String[] str = readAllVotehindexR("allH_indexR_x.txt");
        //计票员1的hR
        BigInteger[] counter1HR = jsonToBigInteger(HXRMap.get("counter1SecondComMulEncrypt").toString());
        //计票员2的hR
        BigInteger[] counter2HR = jsonToBigInteger(HXRMap.get("counter2SecondComMulEncrypt").toString());
        //计票员3的hR
        BigInteger[] counter3HR = jsonToBigInteger(HXRMap.get("counter3SecondComMulEncrypt").toString());

        /**
         * 计算每一个候选项的最终的YR
         */
        //候选项1
        BigInteger vote1HR = CalcuYR.calcuHIndexR1(arr, counter1HR[0], counter2HR[0], counter3HR[0]);
        //候选项2
        BigInteger vote2HR = CalcuYR.calcuHIndexR2(arr, counter1HR[1], counter2HR[1], counter3HR[1]);
        //候选项3
        BigInteger vote3HR = CalcuYR.calcuHIndexR3(arr, counter1HR[2], counter2HR[2], counter3HR[2]);
        map.put("vote1HR", vote1HR);
        map.put("vote2HR", vote2HR);
        map.put("vote3HR", vote3HR);
        return map;
    }

    /**
     * 将获取到的json字符串转换为BigInteger[]数组
     * @param str
     * @return
     */
    static BigInteger[] jsonToBigInteger(String str){
        BigInteger[] hR = new BigInteger[3];
        //json转map
        Map list = (Map)JSON.parse(str.toString());
        hR[0] = new BigInteger(list.get("vote1H_indexR_x").toString());
        hR[1] = new BigInteger(list.get("vote2H_indexR_x").toString());
        hR[2] = new BigInteger(list.get("vote3H_indexR_x").toString());
        return hR;
    }



    static TreeMap<String, BigInteger> comAllHR1(HashMap<String, BigInteger> HXRMap, BigInteger[] arr){
        TreeMap<String, BigInteger> map = new TreeMap<>();
//        String[] str = readAllVotehindexR("allH_indexR_x.txt");
        //计票员1的hR
        BigInteger counter1HR = HXRMap.get("counter1VoteSecondComMulEncrypt");
        //计票员2的hR
        BigInteger counter2HR = HXRMap.get("counter2VoteSecondComMulEncrypt");
        //计票员3的hR
        BigInteger counter3HR = HXRMap.get("counter3VoteSecondComMulEncrypt");

        /**
         * 计算每一个候选项的最终的YR
         */
        //候选项1
        BigInteger vote1HR = CalcuYR.calcuHIndexR1(arr, counter1HR, counter2HR, counter3HR);

        map.put("vote1HR", vote1HR);

        return map;
    }
}
