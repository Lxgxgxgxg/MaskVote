package com.maskvote.maskvotecounter.Zkp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maskvote.maskvotecounter.Bean.Vote;

import java.math.BigInteger;
import java.util.Map;

public interface ZKPFunction {
    /**
     * 由于我们读取到的每一个投票人员的选票都是一样的，所以我准备写一个函数，把json转成的对象穿进去，这样简化代码。
     */
    static boolean oneVoteInformationZKP(BigInteger upk, BigInteger[] arr, Vote[] voter, JSONObject voterAllZKPInformation){
        boolean[] voterBitProof = new boolean[3];
        boolean[] voterIndexProofResult = new boolean[3];
        boolean[] vote1PKEncryptProofResult = new boolean[3];

        for (int i = 0; i < voter.length;i++){
            /**
             * firstCom非0即1的证明
             */
            BigInteger voterFirstCom = voter[i].getFirstCom();
            BigInteger voterFirstComTemp1 = voter[i].getFirstComTemp1();
            BigInteger voterFirstComTemp2 = voter[i].getFirstComTemp2();
            BigInteger voterHash_challenge = voter[i].getHash_challenge();
            BigInteger voterFirstComResponse1 = voter[i].getFirstComResponse1();
            BigInteger voterFirstComResponse2 = voter[i].getFirstComResponse2();
            BigInteger voterFirstComResponse3 = voter[i].getFirstComResponse3();

            voterBitProof[i] = If0Else1Proof.bit01Proof(voterFirstCom, upk, voterFirstComTemp1, voterFirstComTemp2, voterHash_challenge,voterFirstComResponse1,
                    voterFirstComResponse2, voterFirstComResponse3, arr);
//        System.out.println(voterBitProof);

            /**
             * 下面的字段用于证明firstCom Y的指数=secondCom h的指数
             */
            BigInteger voterSecondCom = voter[i].getSecondCom();
            BigInteger voterIndexEqualT = voter[i].getIndexEqualT();
            BigInteger voterIndexEqialS1 = voter[i].getIndexEqualS1();
            BigInteger voterIndexEqualS2 = voter[i].getIndexEqualS2();
            BigInteger voterIndexEqualS3 = voter[i].getIndexEqualS3();

            voterIndexProofResult[i] = IndexEqualsProof.indexEqualsProof(voterFirstCom, upk, voterSecondCom, voterIndexEqualT, voterIndexEqialS1, voterIndexEqualS2, voterIndexEqualS3, arr);
//        System.out.println(voterIndexProofResult);

            /**
             * 下面的字段用于可验证加密，确实是用联合公钥加密的
             */
            BigInteger voterPKEncryptTemp = voter[i].getPKEncryptTemp();
            BigInteger voterPKEncryptHash = voter[i].getPKEncryptHash();
            BigInteger voterPKEncryptS1 = voter[i].getPKEncryptS1();
            BigInteger voterPKEncryptS2 = voter[i].getPKEncryptS2();

            vote1PKEncryptProofResult[i] = PKEncrypt.pKEcryptProof(voterFirstCom, upk, voterPKEncryptTemp, voterPKEncryptHash, voterPKEncryptS1, voterPKEncryptS2, arr);
//        System.out.println(vote1PKEncryptProofResult);
        }

        //下面对三选0或1进行零知识证明
        Map list = (Map)JSON.parse(voterAllZKPInformation.toString());
        BigInteger vote1FirstCom = voter[0].getFirstCom();
        BigInteger vote2FirstCom = voter[1].getFirstCom();
        BigInteger vote3FirstCom = voter[2].getFirstCom();
        BigInteger voteAllFirstCom = vote1FirstCom.multiply(vote2FirstCom).multiply(vote3FirstCom).mod(arr[0]);
        BigInteger allZKPTemp1 = new BigInteger(list.get("allZKPTemp1").toString());
        BigInteger allZKPTemp2 = new BigInteger(list.get("allZKPTemp2").toString());
        BigInteger allZKPChaelge = new BigInteger(list.get("allZKPChallenge").toString());
        BigInteger allZKPResponse1 = new BigInteger(list.get("allZKPResponse1").toString());
        BigInteger allZKPResponse2 = new BigInteger(list.get("allZKPResponse2").toString());
        BigInteger allZKPResponse3 = new BigInteger(list.get("allZKPResponse3").toString());
        boolean voteAllZKP = If0Else1Proof.bit01Proof(voteAllFirstCom,upk,allZKPTemp1,allZKPTemp2,allZKPChaelge,allZKPResponse1,allZKPResponse2,allZKPResponse3,arr);
//        System.out.println(voteAllZKP);


        if (voterBitProof[0] && voterIndexProofResult[0] && vote1PKEncryptProofResult[0]
            && voterBitProof[1] && voterIndexProofResult[1] && vote1PKEncryptProofResult[1]
            && voterBitProof[2] && voterIndexProofResult[2] && vote1PKEncryptProofResult[2]
            && voteAllZKP){
            return true;
        }else {
            return false;
        }
    }



    /**
     * 由于我们读取到的每一个投票人员的选票都是一样的，所以我准备写一个函数，把json转成的对象穿进去，这样简化代码。
     */
    static boolean oneVoteInformationZKP(BigInteger upk, BigInteger[] arr, Vote voter){
        /**
         * firstCom非0即1的证明
         */
//        System.out.println(voter.toString());
        BigInteger voterFirstCom = voter.getFirstCom();
        BigInteger voterFirstComTemp1 = voter.getFirstComTemp1();
        BigInteger voterFirstComTemp2 = voter.getFirstComTemp2();
        BigInteger voterHash_challenge = voter.getHash_challenge();
        BigInteger voterFirstComResponse1 = voter.getFirstComResponse1();
        BigInteger voterFirstComResponse2 = voter.getFirstComResponse2();
        BigInteger voterFirstComResponse3 = voter.getFirstComResponse3();

        boolean voterBitProof = If0Else1Proof.bit01Proof(voterFirstCom, upk, voterFirstComTemp1, voterFirstComTemp2, voterHash_challenge,voterFirstComResponse1,
                voterFirstComResponse2, voterFirstComResponse3, arr);
//        System.out.println(voterBitProof);

        /**
         * 下面的字段用于证明firstCom Y的指数=secondCom h的指数
         */
        BigInteger voterSecondCom = voter.getSecondCom();
        BigInteger voterIndexEqualT = voter.getIndexEqualT();
        BigInteger voterIndexEqialS1 = voter.getIndexEqualS1();
        BigInteger voterIndexEqualS2 = voter.getIndexEqualS2();
        BigInteger voterIndexEqualS3 = voter.getIndexEqualS3();

        boolean voterIndexProofResult = IndexEqualsProof.indexEqualsProof(voterFirstCom, upk, voterSecondCom, voterIndexEqualT, voterIndexEqialS1, voterIndexEqualS2, voterIndexEqualS3, arr);
//        System.out.println(voterIndexProofResult);

        /**
         * 下面的字段用于可验证加密，确实是用联合公钥加密的
         */
        BigInteger voterPKEncryptTemp = voter.getPKEncryptTemp();
        BigInteger voterPKEncryptHash = voter.getPKEncryptHash();
        BigInteger voterPKEncryptS1 = voter.getPKEncryptS1();
        BigInteger voterPKEncryptS2 = voter.getPKEncryptS2();

        boolean vote1PKEncryptProofResult = PKEncrypt.pKEcryptProof(voterFirstCom, upk, voterPKEncryptTemp, voterPKEncryptHash, voterPKEncryptS1, voterPKEncryptS2, arr);
//        System.out.println(vote1PKEncryptProofResult);
        if (voterBitProof && voterIndexProofResult && vote1PKEncryptProofResult){
            return true;
        }else {
            return false;
        }
    }
}
