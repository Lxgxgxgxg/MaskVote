package com.maskvote.maskvotecounter.Zkp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maskvote.maskvotecounter.Count.ReadString;
import com.maskvote.maskvotecounter.Count.ReadVotier;
import com.maskvote.maskvotecounter.Bean.*;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 21:31 2021/3/29
 * @ Description：对每一个投票者的选票信息进行零知识验证
 * @ Version: 1.0
 */
public class ZKPProof {
    //零知识证明的主函数，并从链上拉取的json中获取每一个字段
    public static HashMap<String, BigInteger>  ReadFiledAndZKP(BigInteger[] arr, HashMap<String, String> map){
        /**
         * 首先，计票首先从链上拉取一条投票信息，保存到voteInformation.txt中，然后计票员对选票信息进行零知识证明。
         * 一边后续的计算。
         */
        HashMap<String, BigInteger> tempMap = new HashMap<>();
//        String[] str = ReadVotier.readVoteInformation("voteInformation.txt");
//        /**
//         * 投票员1的三张选票信息和ZKP
//         */
//        JSONObject voter1Information = JSON.parseObject(str[0]).getJSONObject("voter1");
//        JSONObject voter1Vote1Information = JSON.parseObject(voter1Information.toString()).getJSONObject("vote1");
//        JSONObject voter1Vote2Information = JSON.parseObject(voter1Information.toString()).getJSONObject("vote2");
//        JSONObject voter1Vote3Information = JSON.parseObject(voter1Information.toString()).getJSONObject("vote3");
//        JSONObject voter1AllZKPInformation = JSON.parseObject(voter1Information.toString()).getJSONObject("allZKP");
//        //将投票员1的信息json转换成数组
//        Vote voter1Vote1 = JSON.parseObject(String.valueOf(voter1Vote1Information), Vote.class);
//        Vote voter1Vote2 = JSON.parseObject(String.valueOf(voter1Vote2Information), Vote.class);
//        Vote voter1Vote3 = JSON.parseObject(String.valueOf(voter1Vote3Information), Vote.class);
//////        System.out.println(voter1Vote1Information);
//////        System.out.println(voter1Vote2Information);
//////        System.out.println(voter1Vote3Information);
//////        System.out.println(voter1AllZKPInformation);
//        JSONObject voter2Information = JSON.parseObject(str[1]).getJSONObject("voter2");
//        JSONObject voter2Vote1Information = JSON.parseObject(voter2Information.toString()).getJSONObject("vote1");
//        JSONObject voter2Vote2Information = JSON.parseObject(voter2Information.toString()).getJSONObject("vote2");
//        JSONObject voter2Vote3Information = JSON.parseObject(voter2Information.toString()).getJSONObject("vote3");
//        JSONObject voter2AllZKPInformation = JSON.parseObject(voter2Information.toString()).getJSONObject("allZKP");
//        //将投票员2的信息json转换成数组
//        Vote voter2Vote1 = JSON.parseObject(String.valueOf(voter2Vote1Information), Vote.class);
//        Vote voter2Vote2 = JSON.parseObject(String.valueOf(voter2Vote2Information), Vote.class);
//        Vote voter2Vote3 = JSON.parseObject(String.valueOf(voter2Vote3Information), Vote.class);
//
//        JSONObject voter3Information = JSON.parseObject(str[2]).getJSONObject("voter3");
//        JSONObject voter3Vote1Information = JSON.parseObject(voter3Information.toString()).getJSONObject("vote1");
//        JSONObject voter3Vote2Information = JSON.parseObject(voter3Information.toString()).getJSONObject("vote2");
//        JSONObject voter3Vote3Information = JSON.parseObject(voter3Information.toString()).getJSONObject("vote3");
//        JSONObject voter3AllZKPInformation = JSON.parseObject(voter3Information.toString()).getJSONObject("allZKP");
//        //将投票员1的信息json转换成数组
//        Vote voter1Vote1 = JSON.parseObject(String.valueOf(voter3Vote1Information), Vote.class);
//        Vote voter1Vote2 = JSON.parseObject(String.valueOf(voter3Vote2Information), Vote.class);
//        Vote voter1Vote3 = JSON.parseObject(String.valueOf(voter3Vote3Information), Vote.class);
//        JSONObject voter4Information = JSON.parseObject(str[3]).getJSONObject("voter4");
//        JSONObject voter4Vote1Information = JSON.parseObject(voter1Information.toString()).getJSONObject("vote1");
//        JSONObject voter4Vote2Information = JSON.parseObject(voter1Information.toString()).getJSONObject("vote2");
//        JSONObject voter4Vote3Information = JSON.parseObject(voter1Information.toString()).getJSONObject("vote3");
//        JSONObject voter4AllZKPInformation = JSON.parseObject(voter1Information.toString()).getJSONObject("allZKP");
//        //将投票员1的信息json转换成数组
//        Vote voter1Vote1 = JSON.parseObject(String.valueOf(voter1Vote1Information), Vote.class);
//        Vote voter1Vote2 = JSON.parseObject(String.valueOf(voter1Vote2Information), Vote.class);
//        Vote voter1Vote3 = JSON.parseObject(String.valueOf(voter1Vote3Information), Vote.class);

//        Map voter1AllZKP = JSON.parseObject(String.valueOf(voter1Vote1Information));
//        System.out.println(voter1AllZKP.get("allZKPResponse1"));
//        Vote voter2 = JSON.parseObject(String.valueOf(voter2Information), Vote.class);
//        Vote voter3 = JSON.parseObject(String.valueOf(voter3Information), Vote.class);
//        Vote voter4 = JSON.parseObject(String.valueOf(voter4Information), Vote.class);
//        System.out.println(vote1);
        /**
        Vote[] voter = new Vote[str.length];
        voter[0] = voter1Vote1;
        voter[1] = voter1Vote2;
        voter[2] = voter1Vote3;
//        voter[3] = voter4;

        /**
         * 读取联合公钥
         */
        BigInteger upk = ReadUnionPublicKey.getUnionPublicKey();;
//        System.out.println(upk);
        BigInteger vote1SecondComMul = new BigInteger("1");
        BigInteger vote1FirstComMul = new BigInteger("1");
        BigInteger vote2SecondComMul = new BigInteger("1");
        BigInteger vote2FirstComMul = new BigInteger("1");
        BigInteger vote3SecondComMul = new BigInteger("1");
        BigInteger vote3FirstComMul = new BigInteger("1");
        for (int i = 0; i<map.size();i++){
//            JSONObject voterInformation = JSON.parseObject(str[i]).getJSONObject("voter"+(i+1));
            JSONObject voterInformation = JSON.parseObject(map.get("voter"+(i+1))).getJSONObject("voter"+(i+1));
            JSONObject voterVote1Information = JSON.parseObject(voterInformation.toString()).getJSONObject("vote1");
            JSONObject voterVote2Information = JSON.parseObject(voterInformation.toString()).getJSONObject("vote2");
            JSONObject voterVote3Information = JSON.parseObject(voterInformation.toString()).getJSONObject("vote3");
            JSONObject voterAllZKPInformation = JSON.parseObject(voterInformation.toString()).getJSONObject("allZKP");
            //将投票员1的信息json转换成数组
            Vote voterVote1 = JSON.parseObject(String.valueOf(voterVote1Information), Vote.class);
            Vote voterVote2 = JSON.parseObject(String.valueOf(voterVote2Information), Vote.class);
            Vote voterVote3 = JSON.parseObject(String.valueOf(voterVote3Information), Vote.class);
            Vote[] voter = new Vote[3];
            voter[0] = voterVote1;
            voter[1] = voterVote2;
            voter[2] = voterVote3;
            boolean result = ZKPFunction.oneVoteInformationZKP(upk, arr, voter, voterAllZKPInformation);
            /**
             * 记得改成result
             */
            if (result){
//                System.out.println("投票员"+(i+1)+"零知识证明通过！");
                /**
                 * 零知识验证通过之后，计算 h^R,
                 */
                vote1SecondComMul = vote1SecondComMul.multiply(voter[0].getSecondCom()).mod(arr[0]);
                vote1FirstComMul = vote1FirstComMul.multiply(voter[0].getFirstCom()).mod(arr[0]);
                vote2SecondComMul = vote2SecondComMul.multiply(voter[1].getSecondCom()).mod(arr[0]);
                vote2FirstComMul = vote2FirstComMul.multiply(voter[1].getFirstCom()).mod(arr[0]);
                vote3SecondComMul = vote3SecondComMul.multiply(voter[2].getSecondCom()).mod(arr[0]);
                vote3FirstComMul = vote3FirstComMul.multiply(voter[2].getFirstCom()).mod(arr[0]);
//                try {
//                    FileWriter file = new FileWriter("h_indexR.txt");
//                    FileWriter file1 = new FileWriter("firstComMul.txt");
//                    file.write(secondComMul+"\n");
//                    file1.write(firstComMul+"\n");
//                    file.close();
//                    file1.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }else {
                System.out.println("投票员"+(i+1)+"零知识证明不通过！");
            }
        }
//        try {
////            FileWriter file1 = new FileWriter("vote1FirstComMul.txt");
////            FileWriter file2 = new FileWriter("vote1SecondComMul.txt");
////            FileWriter file3 = new FileWriter("vote2FirstComMul.txt");
////            FileWriter file4 = new FileWriter("vote2SecondComMul.txt");
////            FileWriter file5 = new FileWriter("vote3FirstComMul.txt");
////            FileWriter file6 = new FileWriter("vote3SecondComMul.txt");
////            file1.write(vote1FirstComMul+"\n");
////            file2.write(vote1SecondComMul+"\n");
////            file3.write(vote2FirstComMul+"\n");
////            file4.write(vote2SecondComMul+"\n");
////            file5.write(vote3FirstComMul+"\n");
////            file6.write(vote3SecondComMul+"\n");
////            file1.close();
////            file2.close();
////            file3.close();
////            file4.close();
////            file5.close();
////            file6.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        tempMap.put("vote1FirstComMul", vote1FirstComMul);
        tempMap.put("vote1SecondComMul", vote1SecondComMul);
        tempMap.put("vote2FirstComMul", vote2FirstComMul);
        tempMap.put("vote2SecondComMul", vote2SecondComMul);
        tempMap.put("vote3FirstComMul", vote3FirstComMul);
        tempMap.put("vote3SecondComMul", vote3SecondComMul);
//        System.out.println("1f:"+vote1FirstComMul);
//        System.out.println("1s:"+vote1SecondComMul);
//        System.out.println("2f:"+vote2FirstComMul);
//        System.out.println("2s:"+vote2SecondComMul);
//        System.out.println("3f:"+vote3FirstComMul);
//        System.out.println("3s:"+vote3SecondComMul);
//        System.out.println("选票累乘计算成功！");

        return tempMap;
    }

}
