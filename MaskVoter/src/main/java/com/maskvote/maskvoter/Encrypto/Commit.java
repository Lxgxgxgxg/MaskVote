package com.maskvote.maskvoter.Encrypto;

import com.alibaba.fastjson.JSON;
import com.maskvote.maskvoter.Connection.Connection;
import com.maskvote.maskvoter.Bean.Vote;
import com.maskvote.maskvoter.Connection.InvokePK;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.jar.JarOutputStream;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 17:05 2021/3/26
 * @ Description：
 * @ Version: 1.0
 */
public class Commit {

    //保存r的map
    static HashMap<String, BigInteger> map = new HashMap<>();
    static int i = 1;

    /**
     * 此文件主要是对选票的信息进行承诺，加密
     */
    public static BigInteger[] commitVote(String voterStr, BigInteger value, BigInteger[] arr){
        //读取联合公钥
        BigInteger upk = ReadUnionPublicKey.getUnionPublicKey();

        BigInteger[] allCom = new BigInteger[16];
        //承诺的随机数
        BigInteger r = GetRandom.getRandom(arr);

        System.out.println("投票值："+value);

        allCom[0] = arr[2].modPow(value, arr[0]).multiply(upk.modPow(r, arr[0])).mod(arr[0]);
        allCom[1] = arr[3].modPow(r, arr[0]).mod(arr[0]);
//        //把使用到的r保存到文件中
//        try {
//            FileWriter file = new FileWriter(voterStr+"r.txt",true);
//            file.write(r+"\n");
//            file.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(r);
        if (i == 4){
            i=1;
        }
//        System.out.println(i);
        map.put(voterStr+i++, r);



        /**
         * 下面的字段用于证明非0即1
         */
        //随机数
        BigInteger a = GetRandom.getRandom(arr);
        BigInteger s = GetRandom.getRandom(arr);
        BigInteger t = GetRandom.getRandom(arr);

        //中间变量1
        allCom[2] = (arr[2].modPow(a, arr[0]).multiply(upk.modPow(s, arr[0]))).mod(arr[0]);
        //中间变量2
        allCom[3] = (arr[2].modPow(a.multiply(value), arr[0]).multiply(upk.modPow(t, arr[0]))).mod(arr[0]);
        //挑战
        allCom[4] = new BigInteger(HashFunction.getSHA256StrJava(allCom[2].toString()+allCom[3].toString())).mod(arr[1]);
        //响应1
        allCom[5] = (allCom[4].multiply(value).add(a)).mod(arr[1]);
        //响应2
        allCom[6] = (r.multiply(allCom[4]).add(s)).mod(arr[1]);
        //响应3
        allCom[7] = (r.multiply(allCom[4].subtract(allCom[5])).add(t)).mod(arr[1]);

        /**
         * 证明firstCom的Y的指数和secondCom的指数相等
         */
        //首先来三个随机数
        BigInteger random1 = GetRandom.getRandom(arr);
        BigInteger random2 = GetRandom.getRandom(arr);
        BigInteger random3 = random2;

        allCom[8] = arr[2].modPow(random1, arr[0]).multiply(upk.modPow(random2, arr[0])).multiply(arr[3].modPow(random3, arr[0])).mod(arr[0]);
        String str = arr[2].toString()+upk.toString()+arr[3].toString()+allCom[8].toString();
        BigInteger hashc = new BigInteger(HashFunction.getSHA256StrJava(str)).mod(arr[1]);
        allCom[9] = (random1.subtract(hashc.multiply(value))).mod(arr[1]);
        allCom[10] = (random2.subtract(hashc.multiply(r))).mod(arr[1]);
        allCom[11] = (random3.subtract(hashc.multiply(r))).mod(arr[1]);

        /**
         * 证明确实是用联合公钥加密的
         */
        BigInteger random4 = GetRandom.getRandom(arr);
        BigInteger random5 = GetRandom.getRandom(arr);
        allCom[12] = (arr[2].modPow(random4, arr[0]).multiply(upk.modPow(random5, arr[0]))).mod(arr[0]);
        allCom[13] = new BigInteger(HashFunction.getSHA256StrJava(arr[2].toString()+upk.toString()+allCom[12].toString())).mod(arr[1]);
        allCom[14] = (random4.subtract(allCom[13].multiply(value))).mod(arr[1]);
        allCom[15] = (random5.subtract(allCom[13].multiply(r))).mod(arr[1]);


        return allCom;
    }

    public static void CommitAll(String electSearchNum, String str, BigInteger value1, BigInteger value2, BigInteger value3,  BigInteger[] arr){
        /**
         * 第一个候选人的投票信息
         */
        Vote vote1 = new Vote();
        BigInteger[] voteArray1 = commitVote(str,value1, arr);
        vote1.setFirstCom(voteArray1[0]);
        vote1.setSecondCom(voteArray1[1]);
        vote1.setFirstComTemp1(voteArray1[2]);
        vote1.setFirstComTemp2(voteArray1[3]);
        vote1.setHash_challenge(voteArray1[4]);
        vote1.setFirstComResponse1(voteArray1[5]);
        vote1.setFirstComResponse2(voteArray1[6]);
        vote1.setFirstComResponse3(voteArray1[7]);
        vote1.setIndexEqualT(voteArray1[8]);
        vote1.setIndexEqualS1(voteArray1[9]);
        vote1.setIndexEqualS2(voteArray1[10]);
        vote1.setIndexEqualS3(voteArray1[11]);
        vote1.setPKEncryptTemp(voteArray1[12]);
        vote1.setPKEncryptHash(voteArray1[13]);
        vote1.setPKEncryptS1(voteArray1[14]);
        vote1.setPKEncryptS2(voteArray1[15]);

        /**
         * 第二个候选人的投票信息
         */
        Vote vote2 = new Vote();
        BigInteger[] voteArray2 = commitVote(str, value2, arr);
        vote2.setFirstCom(voteArray2[0]);
        vote2.setSecondCom(voteArray2[1]);
        vote2.setFirstComTemp1(voteArray2[2]);
        vote2.setFirstComTemp2(voteArray2[3]);
        vote2.setHash_challenge(voteArray2[4]);
        vote2.setFirstComResponse1(voteArray2[5]);
        vote2.setFirstComResponse2(voteArray2[6]);
        vote2.setFirstComResponse3(voteArray2[7]);
        vote2.setIndexEqualT(voteArray2[8]);
        vote2.setIndexEqualS1(voteArray2[9]);
        vote2.setIndexEqualS2(voteArray2[10]);
        vote2.setIndexEqualS3(voteArray2[11]);
        vote2.setPKEncryptTemp(voteArray2[12]);
        vote2.setPKEncryptHash(voteArray2[13]);
        vote2.setPKEncryptS1(voteArray2[14]);
        vote2.setPKEncryptS2(voteArray2[15]);

        /**
         * 第三个候选人的投票信息
         */
        Vote vote3 = new Vote();
        BigInteger[] voteArray3 = commitVote(str, value3, arr);
        vote3.setFirstCom(voteArray3[0]);
        vote3.setSecondCom(voteArray3[1]);
        vote3.setFirstComTemp1(voteArray3[2]);
        vote3.setFirstComTemp2(voteArray3[3]);
        vote3.setHash_challenge(voteArray3[4]);
        vote3.setFirstComResponse1(voteArray3[5]);
        vote3.setFirstComResponse2(voteArray3[6]);
        vote3.setFirstComResponse3(voteArray3[7]);
        vote3.setIndexEqualT(voteArray3[8]);
        vote3.setIndexEqualS1(voteArray3[9]);
        vote3.setIndexEqualS2(voteArray3[10]);
        vote3.setIndexEqualS3(voteArray3[11]);
        vote3.setPKEncryptTemp(voteArray3[12]);
        vote3.setPKEncryptHash(voteArray3[13]);
        vote3.setPKEncryptS1(voteArray3[14]);
        vote3.setPKEncryptS2(voteArray3[15]);


        /**
         * 下面的函数主要是对所有的选票承诺的相乘之后零知识证明，三选一或三选0，证明非0即1, 需要的是三个选票信息的FirstCom的随机数r
         */
//        //读取三个致盲因子
//        BigInteger[] threeR = ReadVotier.readFile(str+"r.txt");
        //计算三个r的和
//        System.out.println(map.isEmpty());
//        System.out.println("~~~");
//        System.out.println(map.get(str+"1"));
//        System.out.println("~~~");
//        System.out.println(map.get(str+"2"));
//        System.out.println("~~~");
//        System.out.println(map.get(str+"3"));
//        System.out.println("~~~");
         BigInteger rSum = (map.get(str+"1").add(map.get(str+"2")).add(map.get(str+"3"))).mod(arr[1]);

        BigInteger valueSum = value1.add(value2).add(value3);
        BigInteger upk = ReadUnionPublicKey.getUnionPublicKey();
        String allZKPStr = "},\"allZKP\":"+AllVoteInformationZKP.generateAllVoteZKP(valueSum, upk, rSum, arr) + "}";
//        System.out.println(allZKPStr );

        mapping(electSearchNum, str, vote1, vote2, vote3, allZKPStr);
    }

    public static void mapping(String electSearchNum, String str, Vote vote1, Vote vote2, Vote vote3, String allZKPStr){
        /**
         * 产生json字符串
         */
        TreeMap<String, Vote> map = new TreeMap<>();
        TreeMap<String, TreeMap<String, Vote>> treeMap = new TreeMap<>();
        map.put("vote1", vote1);
        map.put("vote2", vote2);
        map.put("vote3", vote3);
        treeMap.put(str, map);

        String strTemp = JSON.toJSONString(treeMap);
//        System.out.println(strTemp);
        //拼接到strTemp后面
        strTemp = strTemp.replaceFirst("}}",allZKPStr);
//        System.out.println(strTemp);
//        try {
//            FileWriter file1 = new FileWriter("voteInformation.txt",true);
//            file1.write(strTemp+"\n");
////            System.out.println("写入文件成功");
//            file1.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(strTemp);

        strTemp = strTemp.replaceAll("\"", "\\\\\"");
        InvokePK.invoke(Connection.getNetwork(), electSearchNum+str, strTemp);
        System.out.println("electSearchNum+str:"+electSearchNum+str);
        System.out.println(strTemp);

    }


    /**
     * 这里的函数就是生成表决加密信息的函数
     *  1、也就是只需要产生加密一条交易信息
     */
    public static void CommitOneTransaction(String voteSearchNum, String str, BigInteger value, BigInteger[] arr){
        /**
         * 表决的信息
         */
        Vote vote1 = new Vote();
        BigInteger[] voteArray1 = commitVote1(str,value, arr);
        vote1.setFirstCom(voteArray1[0]);
        vote1.setSecondCom(voteArray1[1]);
        vote1.setFirstComTemp1(voteArray1[2]);
        vote1.setFirstComTemp2(voteArray1[3]);
        vote1.setHash_challenge(voteArray1[4]);
        vote1.setFirstComResponse1(voteArray1[5]);
        vote1.setFirstComResponse2(voteArray1[6]);
        vote1.setFirstComResponse3(voteArray1[7]);
        vote1.setIndexEqualT(voteArray1[8]);
        vote1.setIndexEqualS1(voteArray1[9]);
        vote1.setIndexEqualS2(voteArray1[10]);
        vote1.setIndexEqualS3(voteArray1[11]);
        vote1.setPKEncryptTemp(voteArray1[12]);
        vote1.setPKEncryptHash(voteArray1[13]);
        vote1.setPKEncryptS1(voteArray1[14]);
        vote1.setPKEncryptS2(voteArray1[15]);

        mapping1(voteSearchNum, str, vote1);
    }

    public static BigInteger[] commitVote1(String voterStr, BigInteger value, BigInteger[] arr){
        //读取联合公钥
        BigInteger upk = ReadUnionPublicKey.getUnionPublicKey();

        BigInteger[] allCom = new BigInteger[16];
        //承诺的随机数
        BigInteger r = GetRandom.getRandom(arr);

        allCom[0] = arr[2].modPow(value, arr[0]).multiply(upk.modPow(r, arr[0])).mod(arr[0]);
        allCom[1] = arr[3].modPow(r, arr[0]).mod(arr[0]);

        /**
         * 下面的字段用于证明非0即1
         */
        //随机数
        BigInteger a = GetRandom.getRandom(arr);
        BigInteger s = GetRandom.getRandom(arr);
        BigInteger t = GetRandom.getRandom(arr);

        //中间变量1
        allCom[2] = (arr[2].modPow(a, arr[0]).multiply(upk.modPow(s, arr[0]))).mod(arr[0]);
        //中间变量2
        allCom[3] = (arr[2].modPow(a.multiply(value), arr[0]).multiply(upk.modPow(t, arr[0]))).mod(arr[0]);
        //挑战
        allCom[4] = new BigInteger(HashFunction.getSHA256StrJava(allCom[2].toString()+allCom[3].toString())).mod(arr[1]);
        //响应1
        allCom[5] = (allCom[4].multiply(value).add(a)).mod(arr[1]);
        //响应2
        allCom[6] = (r.multiply(allCom[4]).add(s)).mod(arr[1]);
        //响应3
        allCom[7] = (r.multiply(allCom[4].subtract(allCom[5])).add(t)).mod(arr[1]);

        /**
         * 证明firstCom的Y的指数和secondCom的指数相等
         */
        //首先来三个随机数
        BigInteger random1 = GetRandom.getRandom(arr);
        BigInteger random2 = GetRandom.getRandom(arr);
        BigInteger random3 = random2;

        allCom[8] = arr[2].modPow(random1, arr[0]).multiply(upk.modPow(random2, arr[0])).multiply(arr[3].modPow(random3, arr[0])).mod(arr[0]);
        String str = arr[2].toString()+upk.toString()+arr[3].toString()+allCom[8].toString();
        BigInteger hashc = new BigInteger(HashFunction.getSHA256StrJava(str)).mod(arr[1]);
        allCom[9] = (random1.subtract(hashc.multiply(value))).mod(arr[1]);
        allCom[10] = (random2.subtract(hashc.multiply(r))).mod(arr[1]);
        allCom[11] = (random3.subtract(hashc.multiply(r))).mod(arr[1]);

        /**
         * 证明确实是用联合公钥加密的
         */
        BigInteger random4 = GetRandom.getRandom(arr);
        BigInteger random5 = GetRandom.getRandom(arr);
        allCom[12] = (arr[2].modPow(random4, arr[0]).multiply(upk.modPow(random5, arr[0]))).mod(arr[0]);
        allCom[13] = new BigInteger(HashFunction.getSHA256StrJava(arr[2].toString()+upk.toString()+allCom[12].toString())).mod(arr[1]);
        allCom[14] = (random4.subtract(allCom[13].multiply(value))).mod(arr[1]);
        allCom[15] = (random5.subtract(allCom[13].multiply(r))).mod(arr[1]);

        return allCom;

    }


    public static void mapping1(String voteSearchNum, String str, Vote vote1){
        /**
         * 产生json字符串
         */
        TreeMap<String, Vote> map = new TreeMap<>();
        map.put(str, vote1);

        String mapStr = JSON.toJSONString(map);

        mapStr = mapStr.replaceAll("\"","\\\\\"");
        //上链
        InvokePK.invoke(Connection.getNetwork(), voteSearchNum+str+"Vote", mapStr);

        System.out.println(mapStr);
        System.out.println(str);
    }


    /**
     * 结果集查询
     */
    public static HashMap<String, Integer> querySet(){
        HashMap<String, Integer> map = new HashMap<>();
        map.put("1",0);
        map.put("1413725201043655687806426909828",1);
        map.put("1750742576262600654506941253804",2);
        map.put("310339300967179069297448514413",3);
        map.put("1700665966400988042079266337689",4);
        map.put("1421245184312183264223578839220",5);
        map.put("1732533456276251810196998069242",6);
        map.put("1060610491901052232220807333510",7);
        map.put("1487352758104605009351999760849",8);
        map.put("758763866668915015114369703483",9);
        map.put("2359819723432380752679491018907",10);

        return map;
    }
}
