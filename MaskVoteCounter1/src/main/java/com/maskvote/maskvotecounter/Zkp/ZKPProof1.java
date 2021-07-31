package com.maskvote.maskvotecounter.Zkp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maskvote.maskvotecounter.Bean.Vote;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 11:13 2021/5/31
 * @ Description：
 * @ Version: 1.0
 */
public class ZKPProof1 {

    //零知识证明的主函数，并从链上拉取的json中获取每一个字段
    public static HashMap<String, BigInteger> ReadFiledAndZKP(BigInteger[] arr, HashMap<String, String> map){
        /**
         * 首先，计票首先从链上拉取一条投票信息，保存到voteInformation.txt中，然后计票员对选票信息进行零知识证明。
         * 一边后续的计算。
         */
        HashMap<String, BigInteger> tempMap = new HashMap<>();


        /**
         Vote[] voter = new Vote[str.length];
         voter[0] = voter1Vote1;
         voter[1] = voter1Vote2;
         voter[2] = voter1Vote3;
         //        voter[3] = voter4;

         /**
         * 读取联合公钥
         */
//        BigInteger upk = new BigInteger("468862796825576484238173312980");
        BigInteger upk = ReadUnionPublicKey.getUnionPublicKey();
        System.out.println(upk);
        BigInteger vote1SecondComMul = new BigInteger("1");
        BigInteger vote1FirstComMul = new BigInteger("1");
        for (int i = 0; i<map.size();i++){
            JSONObject voterInformation = JSON.parseObject(map.get("voter"+(i+1)+"Vote")).getJSONObject("voter"+(i+1));
//            JSONObject voterVote1Information = JSON.parseObject(voterInformation.toString()).getJSONObject("vote1");
            //将投票员1的信息json转换成数组
            Vote voterVote1 = JSON.parseObject(String.valueOf(voterInformation), Vote.class);
            Vote voter = voterVote1;
//            System.out.println(voter.toString());
            boolean result = ZKPFunction.oneVoteInformationZKP(upk, arr, voter);
            /**
             * 记得改成result
             */
            if (result){
                /**
                 * 零知识验证通过之后，计算 h^R,
                 */
                vote1SecondComMul = vote1SecondComMul.multiply(voter.getSecondCom()).mod(arr[0]);
                vote1FirstComMul = vote1FirstComMul.multiply(voter.getFirstCom()).mod(arr[0]);
            }else {
                System.out.println("投票员"+(i+1)+"零知识证明不通过！");
            }
        }
        tempMap.put("vote1FirstComMul", vote1FirstComMul);
        tempMap.put("vote1SecondComMul", vote1SecondComMul);

        return tempMap;
    }
}
