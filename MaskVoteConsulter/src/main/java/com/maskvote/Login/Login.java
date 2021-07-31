package com.maskvote.Login;

import java.math.BigInteger;

public interface Login {
    /**
     * 投票者登陆身份验证
     * @param str1
     */
    static String login1(BigInteger[] arr, String str1){
        String result = null;
        BigInteger pk = new BigInteger(str1);
//        BigInteger pk = ReadPublicKey.getPublicKey(str);

        //投票者公钥
        BigInteger[] pk1 = new BigInteger[4];
//        pk1[0] = new BigInteger("952814407666192733160724562268");
//        pk1[1] = new BigInteger("487479675105132876708504087942");
//        pk1[2] = new BigInteger("1718110071901838528441944713778");
//        pk1[3] = new BigInteger("837601335864698280432082510423");
        pk1[0] = new BigInteger("225003900294155863896676656086");
        pk1[1] = new BigInteger("1229070854056078761921421743689");
        pk1[2] = new BigInteger("988212233582660575973843358279");
        pk1[3] = new BigInteger("1141436285012644213905173843623");
        for (int i = 0; i < pk1.length; i++) {
            if ( (arr[3].modPow(pk1[i], arr[0]).mod(arr[0])).equals(pk) ){
                result = "yesvoter"+(i+1);
                System.out.println(result);
                break;
            }else {
                result = "novoter";
            }
        }
        return result;
    }

    static String login2(BigInteger[] arr, String str1){
        String result = null;
        BigInteger pk = new BigInteger(str1);
        //计票员公钥
        BigInteger[] pk2 = new BigInteger[3];
//        pk2[0] = new BigInteger("2060076060217661799675441861149");
//        pk2[1] = new BigInteger("1352895011969955031421909340991");
//        pk2[2] = new BigInteger("1082766309319151721808263240285");
        pk2[0] = new BigInteger("476489973340520229188253354830");
        pk2[1] = new BigInteger("867179510563592458426934415402");
        pk2[2] = new BigInteger("495894706847913815761708248047");
        for (int i = 0; i < pk2.length; i++) {
            if ( (arr[3].modPow(pk2[i], arr[0]).mod(arr[0])).equals(pk) ){
                result = "yescounter"+(i+1);
                System.out.println(result);
                break;
            }else {
                result = "nocounter";
            }
        }
        return result;
    }
}
