package com.maskvote.Test;

import com.maskvote.Connection.Connection;
import com.maskvote.Connection.Invoke;
import com.maskvote.Login.Login;
import com.maskvote.Register.GetPrivateKey;
import com.maskvote.Register.GetPublicKey;
import com.maskvote.Register.RSAUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import static com.maskvote.Register.RSAUtils.*;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 19:02 2021/3/27
 * @ Description：
 * @ Version: 1.0
 */
public class Test {
    /**
     * 1、接受来自投票者的信息，返回给投票者私钥，公钥保存当地。
     * 2、在投票者登陆的时候，与投票者输入的私钥进行验证。通过之后返回结果，不通过也返回结果。
     */

    /**
     * 服务器端监听网络
     */


    //保存投票员私钥的哈希，以及身份编号
    static HashMap<String, String> voterMap = new HashMap<>();

    //保存计票员的私钥哈希，身份编号
    static HashMap<String, String> counterMap = new HashMap<>();

    //保存计票员的身份编号，私钥
    static HashMap<String, String> counterMap1 = new HashMap<>();

    //保存计票员的身份编号，公钥
    static HashMap<String, String> counterMap2 = new HashMap<>();
    static int i = 1;
    static int j = 1;

    public void Server(BigInteger[] arr){
        ServerSocket ss = null;
        Socket socket = null;
        InputStream is = null;
        ByteArrayOutputStream bas = null;
        OutputStream os = null;
        while (true){
            try {
                ss = new ServerSocket(8989);
                socket = ss.accept();
                is = socket.getInputStream();

                bas = new ByteArrayOutputStream();
                byte[] buffer = new byte[100];
                int len = 0;
                while ((len = is.read(buffer)) != -1){
                    bas.write(buffer, 0, len);
                }

                String str = bas.toString();
                String firstStr = str.substring(0,5);
//                    System.out.println(firstStr);
                if (str.equals("voterRegister")){
                    /**
                     * 投票者注册
                     */
                    BigInteger sk = GetPrivateKey.getPrivateKey(arr);
                    BigInteger pk = GetPublicKey.getPublicKey(arr, sk);

                    /**
                     * 得到注册的投票员的公私钥
                     */
                    String firstStr1 = str.substring(0,5);

                    //私钥同RSA公钥加密之后，得到注册码，给投票员
                    String zhucema = null;
                    try {
//                        zhucema = encryptByPubKey(sk.toString(), publicKeyString);
                        String zhucemaTemp = HashFunction.getSHA256StrJava(sk.toString());
                        zhucema = zhucemaTemp.substring(0, 6);
                        voterMap.put(zhucema, firstStr1+i);
                        i++;
                        System.out.println("i="+i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


//                    System.out.println("++++:"+zhucema);

                    //私钥返回给投票者
                    os = socket.getOutputStream();
                    os.write(zhucema.toString().getBytes());
                }else if (str.equals("counterRegister")){
                    /**
                     * 计票员注册
                     */
                    BigInteger sk = GetPrivateKey.getPrivateKey(arr);
                    BigInteger pk = GetPublicKey.getPublicKey(arr, sk);

                    String firstStr2 = str.substring(0,5);

                    System.out.println("~~~~~~~~~~~~~~~~~~");

                    //私钥同RSA公钥加密之后，得到注册码，给投票员
                    String zhucema = null;
                    try {
                        /**
                         * 得到注册的计票员的公私钥
                         */
                        //公钥保存map中
                        String zhucemaTemp = HashFunction.getSHA256StrJava(sk.toString());
                        System.out.println("zhucemaTemp:"+zhucemaTemp);
                        zhucema = zhucemaTemp.substring(0, 6);
                        counterMap.put(zhucema, firstStr2+j);
                        counterMap1.put(firstStr2+j, sk.toString());
                        counterMap2.put(firstStr2+j, pk.toString());
                        j++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /**
                     * 三个计票员，一旦j==4，则计算公钥并上链
                     */
                    if (j == 4){
                        BigInteger p = new BigInteger("2486327737341467787801453450323");
                        BigInteger upk = new BigInteger("1");
                        for (String value : counterMap2.values()) {
                            upk = upk.multiply(new BigInteger(value));
                            upk = upk.mod(p);
                            System.out.println("upk"+upk);
                        }
                        /**
                         * 上链
                         */
                        Invoke.invoke(Connection.getNetwork(), "upk", upk.toString());
                    }

                    os = socket.getOutputStream();
                    System.out.println("注册码："+zhucema);
                    os.write(zhucema.toString().getBytes());
                } else if (firstStr.equals("voter")){

                    System.out.println("str:"+str);
                    String jiemizhucema = str.substring(5, str.length());

                    System.out.println("jiemie:"+jiemizhucema);

                    /**
                     * 投票员计票员身份者验证,str就是注册码
                     * 遍历两个map
                     */
                    String result = null;
                    result = voterMap.get(jiemizhucema);
                    for (Map.Entry<String, String> entry1 : voterMap.entrySet()) {
                        System.out.println("key:"+entry1.getKey()+",value="+entry1.getValue());
//                        if (jiemizhucema.equals(entry1.getKey())){
//                            result = "yes"+entry1.getValue();
//                        } else {
//                            result = "novoter";
//                        }
                    }
                    if (result != null){
                        result = "yes"+result;
                    }else {
                        result = "novoter";
                    }
                    System.out.println(result);

                    os = socket.getOutputStream();
                    os.write(result.getBytes());

                } else {
                    /**
                     * 计票员身份验证
                     */
                    String jiemizhucema = str.substring(5, str.length());

                    System.out.println("countjiemi:"+jiemizhucema);

                    String result = null;
                    result = counterMap.get(jiemizhucema);

                    //遍历counterMap
                    for (Map.Entry<String, String> entry : counterMap.entrySet()){
                        System.out.println("counter注册码:"+entry.getKey()+",身份标识="+entry.getValue());
                    }
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                    //遍历counterMap1
                    for (Map.Entry<String, String> entry1 : counterMap1.entrySet()){
                        System.out.println("counter身份标识:"+entry1.getKey()+",私钥="+entry1.getValue());
                    }

                    //遍历counterMap2
                    for (Map.Entry<String, String> entry2 : counterMap2.entrySet()){
                        System.out.println("counter身份标识:"+entry2.getKey()+",公钥="+entry2.getValue());
                    }

                    if (result != null){
                        result = "yes"+result.substring(0,5)+"er"+result.substring(5, result.length())+counterMap1.get(result);
                    }else {
                        result = "nocounter";
                    }

                    System.out.println(result);

                    os = socket.getOutputStream();
                    os.write(result.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (os != null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bas != null){
                    try {
                        bas.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ss != null){
                    try {
                        ss.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

//    public static void main(String[] args) {
//        BigInteger[] arr = ReadBase.readGroupBase();
////        RegisterServer.server(arr);
//        Server(arr);
//    }
}
