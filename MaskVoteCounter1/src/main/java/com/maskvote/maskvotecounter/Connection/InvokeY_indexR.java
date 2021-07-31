package com.maskvote.maskvotecounter.Connection;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.Peer;

import java.util.EnumSet;

public interface InvokeY_indexR {
    /**
     * 计票员计算完最后的Y_indexR之后，上传至链上
     */
    static void invokeY_indexR(Network network, String str, String newTrans){
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
