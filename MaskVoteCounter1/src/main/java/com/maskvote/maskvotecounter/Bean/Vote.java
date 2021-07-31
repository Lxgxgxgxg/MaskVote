package com.maskvote.maskvotecounter.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 23:07 2021/5/27
 * @ Description：
 * @ Version: 1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Vote {

    private BigInteger firstCom;
    private BigInteger secondCom;

    /**
     * 下面的字段用于证明证明firstCom中的隐藏值非0即1
     */
    private BigInteger firstComTemp1;
    private BigInteger firstComTemp2;
    private BigInteger hash_challenge;
    private BigInteger firstComResponse1;
    private BigInteger firstComResponse2;
    private BigInteger firstComResponse3;

    /**
     * 下面的字段用于证明公钥的r和secondCom的r相等
     */
    private BigInteger indexEqualT;
    private BigInteger indexEqualS1;
    private BigInteger indexEqualS2;
    private BigInteger indexEqualS3;

    /**
     * 下面的字段用于证明确实是用联合公钥加密的
     */
    private BigInteger PKEncryptTemp;
    private BigInteger PKEncryptHash;
    private BigInteger PKEncryptS1;
    private BigInteger PKEncryptS2;


    public BigInteger getFirstCom() {
        return firstCom;
    }

    public void setFirstCom(BigInteger firstCom) {
        this.firstCom = firstCom;
    }

    public BigInteger getSecondCom() {
        return secondCom;
    }

    public void setSecondCom(BigInteger secondCom) {
        this.secondCom = secondCom;
    }

    public BigInteger getFirstComTemp1() {
        return firstComTemp1;
    }

    public void setFirstComTemp1(BigInteger firstComTemp1) {
        this.firstComTemp1 = firstComTemp1;
    }

    public BigInteger getFirstComTemp2() {
        return firstComTemp2;
    }

    public void setFirstComTemp2(BigInteger firstComTemp2) {
        this.firstComTemp2 = firstComTemp2;
    }

    public BigInteger getHash_challenge() {
        return hash_challenge;
    }

    public void setHash_challenge(BigInteger hash_challenge) {
        this.hash_challenge = hash_challenge;
    }

    public BigInteger getFirstComResponse1() {
        return firstComResponse1;
    }

    public void setFirstComResponse1(BigInteger firstComResponse1) {
        this.firstComResponse1 = firstComResponse1;
    }

    public BigInteger getFirstComResponse2() {
        return firstComResponse2;
    }

    public void setFirstComResponse2(BigInteger firstComResponse2) {
        this.firstComResponse2 = firstComResponse2;
    }

    public BigInteger getFirstComResponse3() {
        return firstComResponse3;
    }

    public void setFirstComResponse3(BigInteger firstComResponse3) {
        this.firstComResponse3 = firstComResponse3;
    }

    public BigInteger getIndexEqualT() {
        return indexEqualT;
    }

    public void setIndexEqualT(BigInteger indexEqualT) {
        this.indexEqualT = indexEqualT;
    }

    public BigInteger getIndexEqualS1() {
        return indexEqualS1;
    }

    public void setIndexEqualS1(BigInteger indexEqualS1) {
        this.indexEqualS1 = indexEqualS1;
    }

    public BigInteger getIndexEqualS2() {
        return indexEqualS2;
    }

    public void setIndexEqualS2(BigInteger indexEqualS2) {
        this.indexEqualS2 = indexEqualS2;
    }

    public BigInteger getIndexEqualS3() {
        return indexEqualS3;
    }

    public void setIndexEqualS3(BigInteger indexEqualS3) {
        this.indexEqualS3 = indexEqualS3;
    }

    public BigInteger getPKEncryptTemp() {
        return PKEncryptTemp;
    }

    public void setPKEncryptTemp(BigInteger PKEncryptTemp) {
        this.PKEncryptTemp = PKEncryptTemp;
    }

    public BigInteger getPKEncryptHash() {
        return PKEncryptHash;
    }

    public void setPKEncryptHash(BigInteger PKEncryptHash) {
        this.PKEncryptHash = PKEncryptHash;
    }

    public BigInteger getPKEncryptS1() {
        return PKEncryptS1;
    }

    public void setPKEncryptS1(BigInteger PKEncryptS1) {
        this.PKEncryptS1 = PKEncryptS1;
    }

    public BigInteger getPKEncryptS2() {
        return PKEncryptS2;
    }

    public void setPKEncryptS2(BigInteger PKEncryptS2) {
        this.PKEncryptS2 = PKEncryptS2;
    }
}
