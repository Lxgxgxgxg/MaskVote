package com.maskvote.maskvotecounter.Connection;

import org.hyperledger.fabric.gateway.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lxq
 * \* Date: 2021/3/19
 * \* Time: 19:12
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class Connection {
    /**
     * 获取网络
     * @param
     */
    public static Network getNetwork() {
        Network network = null;
        try {
            //获取相应参数
            Properties properties = new Properties();
            InputStream inputStream = com.maskvote.maskvotecounter.Connection.Connection.class.getResourceAsStream("/fabric.config.properties");
            properties.load(inputStream);

            String networkConfigPath = properties.getProperty("networkConfigPath");
            String channelName = properties.getProperty("channelName");
            String contractName = properties.getProperty("contractName");
            //使用org1中的user1初始化一个网关wallet账户用于连接网络
            String certificatePath = properties.getProperty("certificatePath");
            X509Certificate certificate = readX509Certificate(Paths.get(certificatePath));

            String privateKeyPath = properties.getProperty("privateKeyPath");
            PrivateKey privateKey = getPrivateKey(Paths.get(privateKeyPath));

            Wallet wallet = Wallets.newInMemoryWallet();
            wallet.put("user1", Identities.newX509Identity("Org1MSP", certificate, privateKey));

            //根据connection.json 获取Fabric网络连接对象
            Gateway.Builder builder = Gateway.createBuilder()
                    .identity(wallet, "user1")
                    .networkConfig(Paths.get(networkConfigPath));
            //连接网关
            Gateway gateway = builder.connect();
//            FileWriter file4 = new FileWriter("netWork.txt");
//            file4.write(network+"\n");
//            file4.close();
            network = gateway.getNetwork(channelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return network;
    }

    /**
     * 获取合约
     * @param
     */
    public static Contract getContract(Network network) {
        Contract contract = network.getContract("mycc");
        return  contract;
    }

    private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }


    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }

//    public static void main(String[] args) {
//        Query.query(getContract(getNetwork()));
//        invoke.invoke(getNetwork());
//        query.query(getContract(getNetwork()));
//    }
}
