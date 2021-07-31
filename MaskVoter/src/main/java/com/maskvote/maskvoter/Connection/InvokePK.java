package com.maskvote.maskvoter.Connection;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.Peer;

import java.util.EnumSet;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lxq
 * \* Date: 2021/3/19
 * \* Time: 19:14
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public interface InvokePK {
    static void invoke(Network network, String str, String newTrans){
        // 增加新的资产
        byte[] invokeResult = new byte[0];
        Contract contract = network.getContract("mycc");
        try {
            invokeResult = contract.createTransaction("set")
                    .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                    .submit(str, newTrans);
//            System.out.println(new String(invokeResult, StandardCharsets.UTF_8));
            System.out.println("投票信息已成功上链！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
