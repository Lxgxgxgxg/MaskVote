package com.maskvote.maskvoter.Encrypto;

import com.alibaba.fastjson.JSON;

import java.math.BigInteger;
import java.util.TreeMap;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 16:14 2021/4/9
 * @ Description：对三个选票信息进行零知识证明，即三张票的承诺信息之和是否非0即1
 *                也就是真实环境中的三选一投票，确保你的投票只能投给三人中的一人，不能给三选二或者三选三
 * @ Version: 1.0
 */
public interface AllVoteInformationZKP {

    static String generateAllVoteZKP(BigInteger valueSum, BigInteger upk, BigInteger r, BigInteger[] arr){
        //下面的字段用于证明三个承诺相乘之后的指数非0即1
        BigInteger[] comNums = new BigInteger[6];
        //三个随机数
        BigInteger a = GetRandom.getRandom(arr);
        BigInteger s = GetRandom.getRandom(arr);
        BigInteger t = GetRandom.getRandom(arr);

        //中间变量1
        comNums[0] = (arr[2].modPow(a, arr[0]).multiply(upk.modPow(s, arr[0]))).mod(arr[0]);
        //中间变量2
        comNums[1] = (arr[2].modPow(a.multiply(valueSum), arr[0]).multiply(upk.modPow(t, arr[0]))).mod(arr[0]);
        //挑战
        comNums[2] = new BigInteger(HashFunction.getSHA256StrJava(comNums[0].toString()+comNums[1].toString())).mod(arr[1]);
        //响应1
        comNums[3] = (comNums[2].multiply(valueSum).add(a)).mod(arr[1]);
        //响应2
        comNums[4] = (r.multiply(comNums[2]).add(s)).mod(arr[1]);
        //响应3
        comNums[5] = (r.multiply(comNums[2].subtract(comNums[3])).add(t)).mod(arr[1]);

//        String str = "allVoteZKP:"+"{"+comNums[0]+
        TreeMap<String, BigInteger> map = new TreeMap<>();
        map.put("allZKPTemp1", comNums[0]);
        map.put("allZKPTemp2", comNums[1]);
        map.put("allZKPChallenge", comNums[2]);
        map.put("allZKPResponse1", comNums[3]);
        map.put("allZKPResponse2", comNums[4]);
        map.put("allZKPResponse3", comNums[5]);

        String mapStr = JSON.toJSONString(map);

        return mapStr;
    }
}
