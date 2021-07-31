package com.maskvote.maskvotecounter.Connection;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.Peer;

import java.util.EnumSet;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 20:37 2021/3/31
 * @ Description：
 * @ Version: 1.0
 */
public interface InvokeResult {
    /**
     * 把最后的结果上传到链上去，供其他的人查看
     */
    static void invokeVoteResult(Network network, String str, String newTrans){
        byte[] invokeResult = new byte[0];
        Contract contract = network.getContract("mycc");
        try {
            invokeResult = contract.createTransaction("set")
                    .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                    .submit(str, newTrans);
//            System.out.println(new String(invokeResult, StandardCharsets.UTF_8));
            System.out.println(str+"已成功上链！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
