package com.maskvote.maskvoter.SocketNet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 17:27 2021/5/26
 * @ Description：
 * @ Version: 1.0
 */

public class Register {

    public static String register(String str1){

        String privateKey = null;
        try{
            InetAddress inet = InetAddress.getByName("127.0.0.1");
            Socket socket = new Socket(inet, 8989);
            OutputStream os = socket.getOutputStream();
            os.write(str1.getBytes());
//            os.write(str2.getBytes());

            socket.shutdownOutput();

            //接受来自协助者的私钥
            InputStream is = socket.getInputStream();
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            byte[] buffer = new byte[800];
            int len = 0;
            while ((len = is.read(buffer)) != -1){
                bas.write(buffer, 0, len);
            }
            privateKey = bas.toString();
//            System.out.println(privateKey);

            //将私钥写入文件
//            FileWriter file = new FileWriter(str1 + "privateKey.txt");
//            file.write(privateKey+"\n");
//            file.close();
            bas.close();
            is.close();
            os.close();
//            socket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return privateKey;
    }
}
