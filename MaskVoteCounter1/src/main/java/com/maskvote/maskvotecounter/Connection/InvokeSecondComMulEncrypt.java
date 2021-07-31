package com.maskvote.maskvotecounter.Connection;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.Peer;

import java.util.EnumSet;

public interface InvokeSecondComMulEncrypt {
    /**
     *@描述 计票员获取链上所有的选票，并计算第二部分的乘积，并用自己的私钥加密，然后上传至链上
     *@参数
     *@返回值
     *@创建人 lxgxgxgxg
     *@创建时间
     */
    static void invokeSecondComMulEncrypt(Network network, String str, String newTrans){
        byte[] invokeResult = new byte[0];
        Contract contract = network.getContract("mycc");
        try {
            invokeResult = contract.createTransaction("set")
                    .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                    .submit(str, newTrans);
//            System.out.println(new String(invokeResult, StandardCharsets.UTF_8));
//            System.out.println(str+"信息已成功上链！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
