<div align = center><font color=#A52A2A size=6>MaskVote</font></div>

## 参数初始化：

<table><tr><td bgcolor=#D1EEEE>下面的系统介绍是基于单目标投票</td></tr></table>

##### 系统成员：

> - 3个计票员
> - 4个投票者
> - 1个协调者

##### 系统介绍

- 首先投票者进行注册，和他交互的是协调者。投票者输入ID号。则返回自己的私钥，公钥保存在协调者的本地。

- 计票员在本地进行注册，私钥$x_i$保存本地，公钥$y_i=h^{x_i}$上传至区块链上。其中一名计票员从链上获取其他计票员的公钥，进行联合公钥$Y = \prod_{i=1}^t y_i$的计算，并上传至区块链上。其他的计票员可以验证联合公钥的正确性。
- 投票者首先从链上获取联合公钥$Y$，进行加密自己的选票信息$(g^dY^r, h^r)$，并生成自己的零知识证明(包括范围证明、指数相等证明、可验证加密：确实使用联合公钥加密)，然后将选票信息上传至区块链上。
- 所有的计票员都从链上获取每一条选票信息，拉取到本地之后，进行零知识证明；之后计算选票信息的累积乘$(g^{\sum d}Y^R, h^R)$，其中我们要求$\sum d$需要知道$Y^R$，相除之后查表就会知道g的指数。现在如何计算$Y^R = (h^{\sum x_i})^R = (h^R)^{\sum x_i} = \prod_{i=1}^{t}h^{Rx_i}$。所以计票员把加密的第一部分累积乘$g^{\sum d}Y^R$保存本地，加密的第二部分累积乘$h^R$在用计票员自己的私钥加密$h^{Rx_i}$，然后上传至上链。
- 其中的一名计票员从链上获取其他计票员的加密第二部分，然后算出$Y^R$，然后算出$g^{sum}$查表得到投票的数量。

> 在系统的测试中，我们先把三个计票员的公私钥提前初始化好，并且计算出联合公钥。把者四个全局变量上传至链上，供其他的参与者验证。

| 计票员   | 私钥                           | 公钥                            |
| -------- | ------------------------------ | ------------------------------- |
| counter1 | 476489973340520229188253354830 | 2060076060217661799675441861149 |
| counter2 | 867179510563592458426934415402 | 1352895011969955031421909340991 |
| counter3 | 495894706847913815761708248047 | 1082766309319151721808263240285 |
| 联合公钥 |                                | 468862796825576484238173312980  |

##### g的指数表

| index | $g^{index}$                     |
| ----- | ------------------------------- |
| 0     | 1                               |
| 1     | 1413725201043655687806426909828 |
| 2     | 1750742576262600654506941253804 |
| 3     | 310339300967179069297448514413  |
| 4     | 1700665966400988042079266337689 |
| 5     | 1421245184312183264223578839220 |
| 6     | 1732533456276251810196998069242 |
| 7     | 1060610491901052232220807333510 |
| 8     | 1487352758104605009351999760849 |
| 9     | 758763866668915015114369703483  |
| 10    | 2359819723432380752679491018907 |

##### 投票员的公私钥对

| 投票员 | 私钥                            | 公钥                            |
| ------ | ------------------------------- | ------------------------------- |
| voter1 | 225003900294155863896676656086  | 952814407666192733160724562268  |
| voter2 | 1229070854056078761921421743689 | 487479675105132876708504087942  |
| voter3 | 988212233582660575973843358279  | 1718110071901838528441944713778 |
| voter4 | 1141436285012644213905173843623 | 837601335864698280432082510423  |

## 操作流程

#### 初始化操作

##### 1、联合公钥上链

- 上传：


```shell
peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:9051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["set","upk","468862796825576484238173312980"]}'
```

- 查看

```shell
peer chaincode query -C mychannel -n mycc -c '{"Args":["query", "upk"]}'
```

##### 2、counter1公钥上链

- 上传

```shell
peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:9051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["set","counter1pk","2060076060217661799675441861149"]}'
```

##### 3、counter2公钥上链

- 上传

```shell
peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:9051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["set","counter2pk","1352895011969955031421909340991"]}'
```

##### 4、counter3公钥上链

- 上传

```shell
peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:9051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["set","counter3pk","1082766309319151721808263240285"]}'
```

- 查看

```shell
peer chaincode query -C mychannel -n mycc -c '{"Args":["query", "counter3pk"]}'
```

#### 投票员投票操作

##### 1、投票信息上链操作

- 程序提示完成

##### 2、查看投票信息

- 区块高度增加1

```shell
peer chaincode query -C mychannel -n mycc -c '{"Args":["query", "voter1"]}'
```

> 其他的投票员的操作一样

#### 计票员操作

![image-20210331213720299](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331213720299.png)

##### 1、链上获取其他计票员的公钥
![image-20210331213808511](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331213808511.png)

##### 2、计算联合公钥并上传至链上

> 其中的一个计票员计算联合公钥即可，其他的计票员从链上获取即可。

联合公钥已经计算完成，并且上链。上链的key ------> upk
##### 3、链上获取所有投票者的信息
![image-20210331213938615](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331213938615.png)

##### 4、对选票信息进行零知识证明 

> 在此的过程中计算firstComMul和secondComMul。


![image-20210331214009767](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331214009767.png)
##### 5、计票员用自己的私钥对secondComMul加密上链

<table><tr><td bgcolor = "red">注意切换计票员身份</td></tr></table>

> 计票员就是用自己的私钥对h_indexR_x进行加密，然后上传至链上。

- counter1进行h_indexR_x加密上链

![image-20210331214544879](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331214544879.png)

- counter2进行h_indexR_x加密上链

![image-20210331214511310](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331214511310.png)

- counter3进行h_indexR_x加密上链

![image-20210331214435645](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331214435645.png)


##### 6、链上获取其他计票员上传的加密部分，然后计算Y_R
![image-20210331214944818](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331214944818.png)


##### 7、计算最后的投票结果

> 用firstComMul除以Y_R，就可以得到g的指数参数，然后通过查表就可以得到最终的投票结果。

![image-20210331215041420](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331215041420.png)

查表发现是4票，符合投票员的投票情况。

#### 投票员查看投票结果

##### 1、投票员从链上获取最后的结果

![image-20210331222725966](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210331222725966.png)



<table><tr><td bgcolor=#D1EEEE>下面的系统介绍是基于多目标的投票</td></tr></table>

> 本测试例中的投票是三选一。

#### 参数初始化

##### 和单目标的参数初始化一模一样，首先上传联合公钥、计票员1、计票员2和计票员3的公钥。

#### 投票员投票操作

##### 1、投票

![image-20210412155923905](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412155923905.png)

#####  2、查看

```shell
peer chaincode query -C mychannel -n mycc -c '{"Args":["query", "voter1"]}'
```

#### 计票员操作

##### 1、链上获取其他计票员的公钥

> 同上单目标

##### 2、计算联合公钥并上传至链上

> 同上单目标

##### 3、链上获取所有投票者的信息

> 同上单目标

##### 4、对选票信息进行零知识证明

> 在此的过程中计算firstComMul和secondComMul。

![image-20210412160703110](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412160703110.png)

##### 5、计票员用自己的私钥对secondComMul加密上链

<table><tr><td bgcolor = "red">注意切换计票员身份</td></tr></table>

>  计票员就是用自己的私钥对vote1h_indexR_x，vote2h_indexR_x，vote3h_indexR_x进行加密，然后上传至链上。

- counter1进行h_indexR_x加密上链

![image-20210412161040997](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412161040997.png)

- counter2进行h_indexR_x加密上链

![image-20210412161234142](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412161234142.png)

- counter3进行h_indexR_x加密上链

![image-20210412161255151](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412161255151.png)

```shell
peer chaincode query -C mychannel -n mycc -c '{"Args":["query", "counter3SecondComMulEncrypt"]}'
```

![image-20210412161806857](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412161806857.png)

##### 6、链上获取其他计票员上传的加密部分，然后计算Y_R

> 同上单目标

1

```shell
peer chaincode query -C mychannel -n mycc -c '{"Args":["query", "counter3Y_indexR"]}'
```

![image-20210412162146980](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412162146980.png)

![image-20210412162232038](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412162232038.png)

![image-20210412162250992](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412162250992.png)

![image-20210412161941007](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412161941007.png)

##### 7、计算最后的投票结果

> 用firstComMul除以Y_R，就可以得到g的指数参数，然后通过查表就可以得到最终的投票结果。

![image-20210412161529804](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412161529804.png)

查表之后就可以看到每个候选项的得票数！

#### 投票员查看投票结果

##### 1、投票员从链上获取最后的结果

![image-20210412162441895](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412162441895.png)









##### 假设票全投给vote1。

-  **voter1**

![image-20210412165343087](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412165343087.png)

- **voter2**

![image-20210412165421029](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412165421029.png)

- **voter3**

![image-20210412165515573](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412165515573.png)

- **voter4**

![image-20210412165620526](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412165620526.png)

![image-20210412165150592](https://cdn.jsdelivr.net/gh/Lxgxgxgxg/TyporaImages/imagesimage-20210412165150592.png)

``` java
{vote1Result=310339300967179069297448514413,vote2Result=1,vote3Result=1}
```

```java
peer chaincode invoke -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n mycc --peerAddresses peer0.org1.example.com:7051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses peer0.org2.example.com:9051 --tlsRootCertFiles /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["set","result","{\"voter1\":{\"vote1\":{\"firstCom\":1335547451719135993342606864365,\"firstComResponse1\":1181862779818441245517682534914,\"firstComResponse2\":1182673613428921439011207162570,\"firstComResponse3\":1029034318406770277186409845131,\"firstComTemp1\":916868529077998420657923425638,\"firstComTemp2\":1087076063892365886053800618031,\"hash_challenge\":777620003060671524637902657233,\"indexEqualS1\":325363916379805764584517964816,\"indexEqualS2\":752827950608281986549521344793,\"indexEqualS3\":752827950608281986549521344793,\"indexEqualT\":1193525174776719613636968749847,\"pKEncryptHash\":1022726472087084490158928352820,\"pKEncryptS1\":792590225684910815919475994502,\"pKEncryptS2\":1057129378372042614704641168930,\"pKEncryptTemp\":297480725726081715366007999930,\"secondCom\":1625132207134182666490686214403},\"vote2\":{\"firstCom\":866858265702435697339611873612,\"firstComResponse1\":1202254536228538002343467257085,\"firstComResponse2\":889366531190259480963896227218,\"firstComResponse3\":532875395754603527974558151900,\"firstComTemp1\":53234907751354268913633802676,\"firstComTemp2\":2115942597061899596331690459926,\"hash_challenge\":921181641906421520679671817762,\"indexEqualS1\":539988609210259631392698173562,\"indexEqualS2\":210802943029600505088345015633,\"indexEqualS3\":210802943029600505088345015633,\"indexEqualT\":860641377880678543912632224293,\"pKEncryptHash\":621164496053698812532581733441,\"pKEncryptS1\":366773615432686828203504289017,\"pKEncryptS2\":729201578002185285037083368049,\"pKEncryptTemp\":1331554099185173318718647315661,\"secondCom\":72208608828795892679146269647},\"vote3\":{\"firstCom\":1695575640816657060728814290345,\"firstComResponse1\":95819818699677308889382892139,\"firstComResponse2\":490963934310620090499691103681,\"firstComResponse3\":832966202795279750118714659478,\"firstComTemp1\":1888741836418729653209957525536,\"firstComTemp2\":2245671949930204002926304881376,\"hash_challenge\":607714712471434479961342927467,\"indexEqualS1\":943016091512034539593087638928,\"indexEqualS2\":736744488917826406780526211225,\"indexEqualS3\":736744488917826406780526211225,\"indexEqualT\":1057281994031054933979860444857,\"pKEncryptHash\":827187457938722769248171193803,\"pKEncryptS1\":336270801432644045124840536636,\"pKEncryptS2\":438766174020734635904586039690,\"pKEncryptTemp\":1841660167557218342929060889358,\"secondCom\":836277671264131037976479496653},\"allZKP\":{\"allZKPChallenge\":1096003913869967583943834731574,\"allZKPResponse1\":1020599419803612210157770066409,\"allZKPResponse2\":726376078013538256602155572465,\"allZKPResponse3\":393526485558112650911864543862,\"allZKPTemp1\":635912240736530323998650139965,\"allZKPTemp2\":878165853550997111473303536139}}}"]}'
```

```java
peer chaincode query -C mychannel -n mycc -c '{"Args":["query","voter1"]}'
peer chaincode query -C mychannel -n mycc -c '{"Args":["query","counter1SecondComMulEncrypt"]}'
    
peer chaincode query -C mychannel -n mycc -c '{"Args":["query","counter1Y_indexR"]}'
```



查表得vote1得4票，vote2、vote3得0票。

