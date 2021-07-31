package com.maskvote.maskvoter.Encrypto;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 14:57 2021/3/26
 * @ Description：
 * @ Version: 1.0
 */
public class Curve {
    BigInteger p;
    BigInteger q;
    BigInteger g;
    BigInteger h;

    public Curve() throws IOException {
        // 产生和打印 pqg	q是阶数，p是2q+1 大素数
        SecureRandom random = new SecureRandom();

        while (true) {
            q = BigInteger.probablePrime(100, random);//长度100位
            p = q.multiply(BigInteger.valueOf(2));//乘2
            p = p.add(BigInteger.valueOf(1));//加1
            if (p.isProbablePrime(1)) {	//是不是素数
                break;
            }
        }

        //g=x^2modp
        BigInteger x = new BigInteger(100, random);
        //大于 返回1，小于返回-1 确定x是2到 p-2
        while(x.compareTo(p.subtract(BigInteger.valueOf(2))) == 1 || x.compareTo(BigInteger.valueOf(2)) == -1){
            x = new BigInteger(100, random);
        }

        g = x.modPow(BigInteger.valueOf(2), p);

        BigInteger y = new BigInteger(100, random);

        while(y.compareTo(q.subtract(BigInteger.valueOf(2))) == 1 || y.compareTo(BigInteger.valueOf(2)) == -1){
            y = new BigInteger(100, random);
        }

        // generate h: h = g^y where y is any random number between 2 and q-2.
        h = g.modPow(y, p);
        //g 和y是该椭圆曲线的两个生成元


        //写入文件
        //写入文件
        FileWriter file = new FileWriter("ecc.txt");
        file.write(p +"\n" + q + "\n" + g + "\n" + h);
        file.close();
        // display
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("g: " + g);
        System.out.println("h: " + h);
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getH() {
        return h;
    }
}
