package com.maskvote.maskvotecounter.Count;

import com.maskvote.maskvotecounter.Connection.*;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 20:23 2021/3/30
 * @ Description：
 * @ Version: 1.0
 */
public class CountVote {
    /**
     * 计票的操作主函数
     */
    public static void count(String str, BigInteger[] arr){
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("1、链上获取其他计票员的公钥：");
            System.out.println("2、计算联合公钥并上传至链上：");
            System.out.println("3、链上获取投票者的投票信息：");
            System.out.println("4、选票信息零知识证明：");
            System.out.println("5、计票者私钥加密第二部分并上传至链上：");
            System.out.println("6、链上获取其他计票员的加密部分并计算Y^R:");
            System.out.println("7、计算最后的投票结果:");
            System.out.println("8、退出");
            String key1 = sc.nextLine();
            switch (key1){
                case "1":
                    for (int i = 1; i < 4; i++){
                        String str1 = "counter"+i+"pk";
                        Query.query(str1, Connection.getContract(Connection.getNetwork()));
                    }
                    break;
                case "2":
//                    BigInteger unionPK = GetUnionPublicKey.getUnionPublicKey(arr, ReadVotier.readFile("counterPublicKey.txt"));
//                    Invoke.invoke(Connection.getNetwork(),"upk", unionPK.toString());
//                    jisuangongyao.caclUPK(arr);
                    break;
                case "3":
                    System.out.println("开始获取所有投票者的投票信息：");
                    for (int i = 1; i < 5;i++){
                        String str2 = "voter" + i;
                        QueryVote.query(str2,Connection.getContract(Connection.getNetwork()));
                    }
                    System.out.println("所有的选票已经成功获取");
                    break;
                case "4":
//                    ZKPProof.ReadFiledAndZKP(arr);
                    break;
                case "5":
                    //读取y^R
                    BigInteger vote1yTempR = new BigInteger(ReadString.readFile("vote1SecondComMul.txt"));
                    BigInteger vote2yTempR = new BigInteger(ReadString.readFile("vote2SecondComMul.txt"));
                    BigInteger vote3yTempR = new BigInteger(ReadString.readFile("vote3SecondComMul.txt"));
                    //读取计票员的私钥
                    BigInteger sk = new BigInteger(ReadString.readFile(str+"PrivateKey.txt"));
//                    BigInteger sk2 = new BigInteger(ReadString.readFile("counter2PrivateKey.txt"));
//                    BigInteger sk3 = new BigInteger(ReadString.readFile("counter3PrivateKey.txt"));
                    BigInteger[] voteyTemp = {vote1yTempR, vote2yTempR, vote3yTempR};
                    //计算用自己私钥加密的结果并上链
//                    TreeMap h_indexR_x = SecondComMulEncrypt.secondComMulEncrypt(str, voteyTemp, sk, arr);
//                    String h_indexR_xStr = JSON.toJSONString(h_indexR_x);
//                    try {
//                        FileWriter file = new FileWriter(str+"H_indexR_x.txt");
//                        file.write(h_indexR_xStr+"\n");
//                        file.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(h_indexR_xStr);
//                    InvokeSecondComMulEncrypt.invokeSecondComMulEncrypt(Connection.getNetwork(), str+"SecondComMulEncrypt", h_indexR_xStr);
//                    secondComEncrypt.secondComEncrypt("counter2", yTempR, sk2, arr);
//                    secondComEncrypt.secondComEncrypt("counter3", yTempR, sk3, arr);
                    break;
                case "6":
                    //链上获取其他计票员上传的h_indexRx
                    for (int i = 1;i < 4; i++){
                        QueryH_indexR_x.query("counter"+i+"SecondComMulEncrypt", Connection.getContract(Connection.getNetwork()));
                    }
                    //计算每个候选项最后的Y_indexR
//                    TreeMap<String, BigInteger> map = ComVoteHIndexR.comAllHR(arr);
                    //文件读取Y_indexR
//                    String allHRStr = JSON.toJSONString(map);
//                    InvokeY_indexR.invokeY_indexR(Connection.getNetwork(), str+"Y_indexR", allHRStr);
                    System.out.println("Y_indexR计算成功并上传至链上！");
                    break;
                case "7":
                    BigInteger vote1firstComMul = new BigInteger(ReadString.readFile("vote1FirstComMul.txt"));
                    BigInteger vote2firstComMul = new BigInteger(ReadString.readFile("vote2FirstComMul.txt"));
                    BigInteger vote3firstComMul = new BigInteger(ReadString.readFile("vote3FirstComMul.txt"));
                    BigInteger[] firstComMul = {vote1firstComMul, vote2firstComMul, vote3firstComMul};
                    BigInteger vote1HR = new BigInteger(ReadString.readFile("vote1Y_indexR.txt"));
                    BigInteger vote2HR = new BigInteger(ReadString.readFile("vote2Y_indexR.txt"));
                    BigInteger vote3HR = new BigInteger(ReadString.readFile("vote3Y_indexR.txt"));
//                    BigInteger[] YR = ReadVotier.readFile("Y_indexR.txt");
                    BigInteger[] YR = {vote1HR, vote2HR, vote3HR};
                    TreeMap<String, BigInteger> result = CountResult.countResult(firstComMul, YR, arr);
                    System.out.println("最后的投票结果为：" + result+"，请查表！");
                    InvokeResult.invokeVoteResult(Connection.getNetwork(), str+"VoteResult",result.toString());
                    break;
                case "8":
                    return;
            }
        }
    }
}
