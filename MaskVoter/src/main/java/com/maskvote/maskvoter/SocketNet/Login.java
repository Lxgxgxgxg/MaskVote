package com.maskvote.maskvoter.SocketNet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 17:23 2021/5/26
 * @ Description：
 * @ Version: 1.0
 */

public class Login {
    /**
     * 验证登录的投票员身份
     * @return
     */
    public static String login(String str){
        String result = null;
        Socket socket = null;
        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream bas = null;

        try {
            InetAddress inet = InetAddress.getByName("127.0.0.1");
            socket = new Socket(inet, 8989);
            os = socket.getOutputStream();
            os.write(str.getBytes());

            socket.shutdownOutput();

            //接受来自协调者的验证登陆
            is = socket.getInputStream();
            bas = new ByteArrayOutputStream();
            byte[] buffer = new byte[200];
            int len = 0;
            while ((len = is.read(buffer)) != -1){
                bas.write(buffer, 0, len);
            }

            result = bas.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bas.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
