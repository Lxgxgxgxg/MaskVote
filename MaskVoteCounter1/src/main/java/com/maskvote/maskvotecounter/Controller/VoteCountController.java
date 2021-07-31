package com.maskvote.maskvotecounter.Controller;

import com.alibaba.fastjson.JSON;
import com.maskvote.maskvotecounter.Connection.*;
import com.maskvote.maskvotecounter.Count.ComVoteHIndexR;
import com.maskvote.maskvotecounter.Count.CountResult;
import com.maskvote.maskvotecounter.Count.ReadBase;
import com.maskvote.maskvotecounter.Count.SecondComMulEncrypt;
import com.maskvote.maskvotecounter.Zkp.ZKPProof1;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 10:47 2021/5/31
 * @ Description：
 * @ Version: 1.0
 */

@Controller
@CrossOrigin
public class VoteCountController {

    /**
     * 设置全局变量
     */
    String voteAffairStr = null;
    String voteStartTime = null;
    String voteEndTime = null;
    String voteSearchNum1 = null;
    String voteNum = null;

    /**
     * 保存选举事务的map
     */
    HashMap<String,String> voteMap = new HashMap<>();
    /**
     * 保存每一个选举事务
     */
    HashMap<String, HashMap<String, String>> allVoteMap = new HashMap<>();

    /**
     * 保存每一个选举事务结果map
     */
    HashMap<String, TreeMap<String, BigInteger>> allVoteResultMap = new HashMap<>();


    //保存从链上取到的表决的信息
    HashMap<String, String> map1 = new HashMap<>();

    //返回界面获取到的文本框
    HashMap<Integer, String> temp1 = new HashMap<>();
    HashMap<Integer, String> temp2 = new HashMap<>();

    //联合公钥
    BigInteger upk = new BigInteger("468862796825576484238173312980");

    //椭圆曲线的基本参数
    BigInteger[] arr = ReadBase.readGroupBase();

    //保存选票累乘的map
    HashMap<String, BigInteger> mulResMap = new HashMap<>();

    //计票员获取加密的第二部分，然后计算
    HashMap<String, BigInteger> secondHXRMap1 = new HashMap<>();

    //结果的Map
    TreeMap<String, BigInteger> result1 = new TreeMap<>();

    @RequestMapping("voteCount")
    public String voteCountPage(@RequestParam("voteSearchNum") String voteSearchNum, Model model){

        /**
         * 根据electSearchNum进行信息的获取
         */
        String electInfor = Query.query(voteSearchNum, Connection.getContract(Connection.getNetwork()));
//        System.out.println(electSearchNum);
//        System.out.println(electInfor);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(string);
        electInfor = electInfor.replaceAll("\\\\\"", "\"");
        String str1 = JSON.parseObject(electInfor).getString(voteSearchNum);
//        System.out.println(str1);
        //事务描述
        voteAffairStr = JSON.parseObject(str1).getString("voteAffairStr");
        //开始时间
        voteStartTime = JSON.parseObject(str1).getString("voteStartTime");
        //结束时间
        voteEndTime = JSON.parseObject(str1).getString("voteEndTime");
        //投票人数
        voteNum = JSON.parseObject(str1).getString("voteNum");
        //搜索码
        voteSearchNum1 = JSON.parseObject(str1).getString("voteSearchNum");
//        System.out.println(electSearchNum1);

        voteMap.put("voteAffairStr", voteAffairStr);
        voteMap.put("voteStartTime", voteStartTime);
        voteMap.put("voteEndTime", voteEndTime);
        voteMap.put("voteNum", voteNum);
        allVoteMap.put(voteSearchNum1, voteMap);


        model.addAttribute("voteAffairStr", voteAffairStr);
        model.addAttribute("voteStartTime", voteStartTime);
        model.addAttribute("voteEndTime", voteEndTime);
        model.addAttribute("voteNum", voteNum);


        return "voteCount";
    }


    @RequestMapping("/startVote")
    @ResponseBody
    public Map StartCountBJ(HttpServletRequest request, String model){

        HttpSession session = request.getSession();
        Object counter = session.getAttribute("counter");
        BigInteger sk = new BigInteger(session.getAttribute("counterSk").toString());
        System.out.println("计票员私钥："+sk);

//        //计票员私钥
//        BigInteger counterSk1 = new BigInteger("476489973340520229188253354830");
//        BigInteger counterSk2 = new BigInteger("867179510563592458426934415402");
//        BigInteger counterSk3 = new BigInteger("495894706847913815761708248047");
//        BigInteger sk = null;
//        if (counter.equals("counter1")){
//            sk = counterSk1;
//        }else if (counter.equals("counter2")){
//            sk = counterSk2;
//        }else {
//            sk = counterSk3;
//        }


        /**
         * 链上获取所有投票者的信息
         */
        for (int i = 1; i < 5; i++) {
            String str = "voter"+i+"Vote";
            String result = QueryVote.query(voteSearchNum1+str, Connection.getContract(Connection.getNetwork()));
//            System.out.println(result);
            map1.put(str, result);
        }
        /**
         * 把所有的承诺加入map1,在前端显示
         */
        for (int i = 1; i <= map1.size(); i++) {
            temp1.put(i, map1.get("voter"+i+"Vote"));
        }
        temp1.put(map1.size()+1, "所有的选票信息已经成功的获取！");


        /**
         * 对选票的信息进行零知识证明，并把多张选票的两部分累乘
         */
        mulResMap = ZKPProof1.ReadFiledAndZKP(arr, map1);
        temp1.put(map1.size()+2,"零知识证明通过，并且计票累乘！");

        /**
         * 下面的计票员用自己的私钥对SecondComMul加密上链
         */
        //获取第二部分
        BigInteger voteYTemp = mulResMap.get("vote1SecondComMul");
//        BigInteger sk = counterSk;

        BigInteger h_indexR_x = SecondComMulEncrypt.secondComMulEncrypt(voteYTemp, sk, arr);
        TreeMap<String, BigInteger> map = new TreeMap<>();
        map.put("vote1H_indexR_x", h_indexR_x);
        String h_indexR_xStr = JSON.toJSONString(h_indexR_x);

        InvokeSecondComMulEncrypt.invokeSecondComMulEncrypt(Connection.getNetwork(), voteSearchNum1+counter+"VoteSecondComMulEncrypt", h_indexR_xStr);

        temp1.put(map1.size()+3, "计票员累乘加密成功！");
//        System.out.println("~~~~~~~~~~~~~");

        return temp1;

    }


    @ResponseBody
    @RequestMapping("/countYRVote")
    public Map countYRAndInvoke(HttpServletRequest request, String model){

        HttpSession session = request.getSession();
        Object counter = session.getAttribute("counter");

        /**
         * 链上获取其他计票员上传的加密部分，然后计算Y_R
         */
        for (int i = 1; i < 4; i++) {
            String res = QueryH_indexR_x.query(voteSearchNum1+"counter"+i+"VoteSecondComMulEncrypt", Connection.getContract(Connection.getNetwork()));

            secondHXRMap1.put("counter"+i+"VoteSecondComMulEncrypt", new BigInteger(res));
        }
        //计算最后的Y_R
        TreeMap<String, BigInteger> YRMap = ComVoteHIndexR.comAllHR1(secondHXRMap1, arr);
        String allHRStr = JSON.toJSONString(YRMap);
        InvokeY_indexR.invokeY_indexR(Connection.getNetwork(), voteSearchNum1+counter+"VoteY_indexR", allHRStr);

        temp2.put(1, "Y_R已经计算成功并上链！");


        /**
         * 计算最后的结果
         */
        BigInteger vote1FirstComMul = mulResMap.get("vote1FirstComMul");
        BigInteger vote1YR = YRMap.get("vote1HR");

        //计算结果
        result1 = CountResult.countResult1(vote1FirstComMul, vote1YR, arr);

        /**
         * 将选举结果加入map
         */
        allVoteResultMap.put(voteSearchNum1, result1);


        /**
         * 将结果返回
         */
        temp2.put(2, result1.toString());

        temp2.put(3, "表决结果已经计算完成！");

        return temp2;

    }


    @RequestMapping("/startUploadVote")
    @ResponseBody
    public String startVoteCount(){
        return "表决结果已经上链";
    }


    @RequestMapping("voteResult")
    public String voteResultPage(HttpServletRequest request, Model model){

        /**
         * 这里是表决的结果界面
         */
        HttpSession session = request.getSession();
        Object counter = session.getAttribute("counter");
        HashMap<String, Integer> map = CountController.querySet();

        BigInteger res1 = result1.get("vote1Result");
        int size = map1.size();

        int a = map.get(res1.toString());
        int b = size - a;

        /**
         * 根据搜索码获取对应的选举事务
         */
        HashMap<String, String> affairMap = allVoteMap.get(voteSearchNum1);

        model.addAttribute("voteAffairStr", affairMap.get("voteAffairStr"));
        model.addAttribute("voteStartTime", affairMap.get("voteStartTime"));
        model.addAttribute("voteEndTime", affairMap.get("voteEndTime"));
        model.addAttribute("voteNum", affairMap.get("voteNum"));

        model.addAttribute("vote1AgreeResult", a);
        model.addAttribute("vote1DisagreeResult", b);

//        //上链
        InvokeResult.invokeVoteResult(Connection.getNetwork(), voteSearchNum1+counter+"BJResult", JSON.toJSONString(result1));

        return "voteResult";
    }


    @RequestMapping("voteResult1")
    public String voteResultPage1(@RequestParam("voteSearchNum") String voteSearchNum, HttpServletRequest request, Model model){

        /**
         * 这里是表决的结果界面
         */
        HttpSession session = request.getSession();
        Object counter = session.getAttribute("counter");
        HashMap<String, Integer> map = CountController.querySet();

        /**
         * 从allVoteResultMap获取对应的结果Map
         */
        TreeMap<String, BigInteger> result = new TreeMap<>();
//        System.out.println(allVoteResultMap);
        result = allVoteResultMap.get(voteSearchNum);

        BigInteger res1 = result.get("vote1Result");
        int size = map1.size();

        int a = map.get(res1.toString());
        int b = size - a;

        /**
         * 根据搜索码获取对应的选举事务
         */
        HashMap<String, String> affairMap = allVoteMap.get(voteSearchNum1);

        model.addAttribute("voteAffairStr", affairMap.get("voteAffairStr"));
        model.addAttribute("voteStartTime", affairMap.get("voteStartTime"));
        model.addAttribute("voteEndTime", affairMap.get("voteEndTime"));
        model.addAttribute("voteNum", affairMap.get("voteNum"));

        model.addAttribute("vote1AgreeResult", a);
        model.addAttribute("vote1DisagreeResult", b);

//        //上链
        InvokeResult.invokeVoteResult(Connection.getNetwork(), voteSearchNum1+counter+"BJResult", JSON.toJSONString(result1));

        return "voteResult";
    }


    @GetMapping("voteSearch")
    public String voteSearch(){

        return "voteSearch";
    }

    @GetMapping("voteResultSearch")
    public String voteResultSearch(){

        return "voteResultSearch";
    }
}
