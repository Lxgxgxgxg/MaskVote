package com.maskvote.maskvotecounter.Controller;

import com.alibaba.fastjson.JSON;
import com.maskvote.maskvotecounter.Connection.*;
import com.maskvote.maskvotecounter.Count.*;
import com.maskvote.maskvotecounter.Zkp.ZKPProof;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 16:22 2021/5/27
 * @ Description：
 * @ Version: 1.0
 */

@Controller
@CrossOrigin
public class CountController {

    /**
     * 设置全局变量
     */
    String electAffairStr = null;
    String electStartTime = null;
    String electEndTime = null;
    String electSearchNum1 = null;
    String electNum = null;

    /**
     * 保存选举事务的map
     */
    HashMap<String,String> electMap = new HashMap<>();
    /**
     * 保存每一个选举事务
     */
    HashMap<String, HashMap<String, String>> allElectMap = new HashMap<>();

    /**
     * 保存每一个选举事务结果map
     */
    HashMap<String, TreeMap<String, BigInteger>> allElectResultMap = new HashMap<>();

    //保存从链上获取到的投票信息
    HashMap<String, String> map1 = new HashMap<>();

    //返回界面文本框的值
    HashMap<Integer, String> tempMap1 = new HashMap<>();
    HashMap<Integer, String> tempMap2 = new HashMap<>();

    //联合公钥
    BigInteger upk = new BigInteger("468862796825576484238173312980");

    //椭圆曲线的基本参数
    BigInteger[] arr = ReadBase.readGroupBase();

    //保存选票累乘6部分的map
    HashMap<String, BigInteger> mulResMap = new HashMap<>();

    //计票员获取加密的第二部分，然后计算
    HashMap<String, String> secondHXRMap1 = new HashMap<>();

    //结果的Map
    TreeMap<String, BigInteger> result1 = new TreeMap<>();


    /**
     * 根据搜索码搜索具体的选举事务
     * @param electSearchNum
     * @param model
     * @return
     */
    @RequestMapping("electCount")
    public String electCountPage(@RequestParam("electSearchNum") String electSearchNum, Model model){
        /**
         * 根据electSearchNum进行信息的获取
         */
        String electInfor = Query.query(electSearchNum, Connection.getContract(Connection.getNetwork()));
//        System.out.println(electSearchNum);
//        System.out.println(electInfor);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(string);
        electInfor = electInfor.replaceAll("\\\\\"", "\"");
        String str1 = JSON.parseObject(electInfor).getString(electSearchNum);
//        System.out.println(str1);
        //事务描述
        electAffairStr = JSON.parseObject(str1).getString("electAffairStr");
        //开始时间
        electStartTime = JSON.parseObject(str1).getString("electStartTime");
        //结束时间
        electEndTime = JSON.parseObject(str1).getString("electEndTime");
        //投票人数
        electNum = JSON.parseObject(str1).getString("electNum");
        //搜索码
        electSearchNum1 = JSON.parseObject(str1).getString("electSearchNum");
//        System.out.println(electSearchNum1);

        electMap.put("electAffairStr", electAffairStr);
        electMap.put("electStartTime", electStartTime);
        electMap.put("electEndTime", electEndTime);
        electMap.put("electNum", electNum);
        allElectMap.put(electSearchNum1, electMap);


        model.addAttribute("electAffairStr", electAffairStr);
        model.addAttribute("electStartTime", electStartTime);
        model.addAttribute("electEndTime", electEndTime);
        model.addAttribute("electNum", electNum);

        return "electCount";
    }


    @RequestMapping("electResult")
    public String electResultPage(HttpServletRequest request, Model model){

        /**
         * 这里是投票的结果页面
         */
        HttpSession session = request.getSession();
        Object counter = session.getAttribute("counter");
        HashMap<String, Integer> map = querySet();

//        /**
//         * 根据搜索码从allElectResultMap中获取结果集
//         */
//        TreeMap<String, BigInteger> result = new TreeMap<>();
//        result = allElectResultMap.get("electSearchNum");
        BigInteger res1 = result1.get("vote1Result");
        BigInteger res2 = result1.get("vote2Result");
        BigInteger res3 = result1.get("vote3Result");
//        System.out.println(map);
//        System.out.println(res1);
//        System.out.println(res2);
//        System.out.println(res3);
        int a = map.get(res1.toString());
        int b = map.get(res2.toString());
        int c = map.get(res3.toString());

        /**
         * 根据搜索码获取对应的选举事务
         */
        HashMap<String, String> affairMap = allElectMap.get(electSearchNum1);

        model.addAttribute("electAffairStr", affairMap.get("electAffairStr"));
        model.addAttribute("electStartTime", affairMap.get("electStartTime"));
        model.addAttribute("electEndTime", affairMap.get("electEndTime"));
        model.addAttribute("electNum", affairMap.get("electNum"));

        model.addAttribute("vote1Result", a);
        model.addAttribute("vote2Result", b);
        model.addAttribute("vote3Result", c);

        //上传结果上链
        InvokeResult.invokeVoteResult(Connection.getNetwork(), (electSearchNum1+counter+"VoteResult").toString(), JSON.toJSONString(result1));

        return "electResult";
    }


    @RequestMapping("electResult1")
    public String electResultPage1(@RequestParam("electSearchNum") String electSearchNum, HttpServletRequest request, Model model){

        /**
         * 这里是投票的结果页面
         */
        HttpSession session = request.getSession();
        Object counter = session.getAttribute("counter");
        HashMap<String, Integer> map = querySet();

        /**
         * 根据搜索码从allElectResultMap中获取结果集
         */
        TreeMap<String, BigInteger> result = new TreeMap<>();
        System.out.println(allElectResultMap);
        result = allElectResultMap.get(electSearchNum);
        BigInteger res1 = result.get("vote1Result");
        BigInteger res2 = result.get("vote2Result");
        BigInteger res3 = result.get("vote3Result");
//        System.out.println(map);
//        System.out.println(res1);
//        System.out.println(res2);
//        System.out.println(res3);
        int a = map.get(res1.toString());
        int b = map.get(res2.toString());
        int c = map.get(res3.toString());

        /**
         * 根据搜索码获取对应的选举事务
         */
        HashMap<String, String> affairMap = allElectMap.get(electSearchNum1);

        model.addAttribute("electAffairStr", affairMap.get("electAffairStr"));
        model.addAttribute("electStartTime", affairMap.get("electStartTime"));
        model.addAttribute("electEndTime", affairMap.get("electEndTime"));
        model.addAttribute("electNum", affairMap.get("electNum"));

        model.addAttribute("vote1Result", a);
        model.addAttribute("vote2Result", b);
        model.addAttribute("vote3Result", c);

        //上传结果上链
        InvokeResult.invokeVoteResult(Connection.getNetwork(), (electSearchNum1+counter+"VoteResult").toString(), JSON.toJSONString(result1));

        return "electResult";
    }




    @RequestMapping("/start")
    @ResponseBody
    public Map startCount(HttpServletRequest request, String model){
//        System.out.println(model);
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
//        for (int i = 1; i < Integer.parseInt(electMap.get(electNum)); i++) {
            String str = "voter"+i;
//            System.out.println(electSearchNum1);
//            System.out.println(str);
            String result = QueryVote.query(electSearchNum1+str, Connection.getContract(Connection.getNetwork()));
//            System.out.println("1");
            map1.put(str, result);
        }
//        tempMap1.putAll(map1);
        /**
         * 把所有的承诺加入map1,在前端显示
         */
        for (int i = 1; i <= map1.size(); i++) {
            tempMap1.put(i, map1.get("voter"+i));
        }
//        System.out.println(map1);
//        System.out.println(tempMap1);
        tempMap1.put(map1.size()+1,"所有的选票已成功获取！");





        /**
         * 对选票信息进行零知识证明,并把多张选票的两部分累乘
         */
        mulResMap = ZKPProof.ReadFiledAndZKP(arr, map1);
        tempMap1.put(map1.size()+2, "零知识证明通过，并计票累乘！");

        /**
         * 下面计票员开始用自己的私钥对SecondComMul加密上链
         */
        //获取第二部分
        BigInteger[] voteYTemp = {mulResMap.get("vote1SecondComMul"), mulResMap.get("vote2SecondComMul"), mulResMap.get("vote3SecondComMul")};


        //计票员用自己的私钥加密第二部分并上链
        TreeMap h_indexR_x = SecondComMulEncrypt.secondComMulEncrypt(voteYTemp, sk, arr);
        String h_indexR_xStr = JSON.toJSONString(h_indexR_x);

        InvokeSecondComMulEncrypt.invokeSecondComMulEncrypt(Connection.getNetwork(), electSearchNum1+counter+"SecondComMulEncrypt", h_indexR_xStr);
        tempMap1.put(map1.size()+3, "计票员累乘加密成功！");

        return tempMap1;
    }


    @RequestMapping("/countYR")
    @ResponseBody
    public Map countYRAndInvoke(HttpServletRequest request, String model){

        HttpSession session = request.getSession();
        Object counter = session.getAttribute("counter");

        /**
         * 链上获取其他计票员上传的加密部分，然后计算Y_R
         */
        for (int i = 1; i < 4; i++) {
            String res = QueryH_indexR_x.query(electSearchNum1+"counter"+i+"SecondComMulEncrypt", Connection.getContract(Connection.getNetwork()));
            secondHXRMap1.put("counter"+i+"SecondComMulEncrypt", res);
        }
        //计算最后的Y_R，并上链
        TreeMap<String, BigInteger> YRMap = ComVoteHIndexR.comAllHR(secondHXRMap1, arr);
        String allHRStr = JSON.toJSONString(YRMap);
        InvokeY_indexR.invokeY_indexR(Connection.getNetwork(), electSearchNum1+counter+"Y_indexR", allHRStr);

        /**
         * 将allHRStr加入到返回的map中去
         */
        tempMap2.put(1,allHRStr);

        tempMap2.put(2, "Y^R已经计算成功并上链！");

        /**
         * 计算最后的结果
         */
        BigInteger vote1firstComMul = mulResMap.get("vote1FirstComMul");
        BigInteger vote2firstComMul = mulResMap.get("vote2FirstComMul");
        BigInteger vote3firstComMul = mulResMap.get("vote3FirstComMul");
        BigInteger[] firstComMul = {vote1firstComMul,vote2firstComMul,vote3firstComMul};
//        System.out.println(Arrays.toString(firstComMul));
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");


        BigInteger vote1HR = YRMap.get("vote1HR");
        BigInteger vote2HR = YRMap.get("vote2HR");
        BigInteger vote3HR = YRMap.get("vote3HR");
        BigInteger[] YR = {vote1HR, vote2HR, vote3HR};
//        System.out.println(Arrays.toString(YR));
//        System.out.println("!~!~!~!~!!~!~!~!~!~!~!~!~!~!");


        //计算结果函数
        result1 = CountResult.countResult(firstComMul, YR, arr);
//        System.out.println(result1);

//        System.out.println(tempMap2);
//        System.out.println("~~~~~~~~~~~~");

        /**
         * 将选举结果加入map
         */
        allElectResultMap.put(electSearchNum1, result1);

        /**
         * 选举结果上链
         */


        /**
         * 将结果返回
         */
        for (int i = 3; i < 3+result1.size(); i++) {
            tempMap2.put(i, "vote"+(i-2)+"Result:"+result1.get("vote"+(i-2)+"Result").toString());
        }


        tempMap2.put(6,"选举结果已经计算完成！");
//        System.out.println(tempMap2);

        return tempMap2;
    }


    /**
     * 结果集查询
     */
    public static HashMap<String, Integer> querySet(){
        HashMap<String, Integer> map = new HashMap<>();
        map.put("1",0);
        map.put("1413725201043655687806426909828",1);
        map.put("1750742576262600654506941253804",2);
        map.put("310339300967179069297448514413",3);
        map.put("1700665966400988042079266337689",4);
        map.put("1421245184312183264223578839220",5);
        map.put("1732533456276251810196998069242",6);
        map.put("1060610491901052232220807333510",7);
        map.put("1487352758104605009351999760849",8);
        map.put("758763866668915015114369703483",9);
        map.put("2359819723432380752679491018907",10);

        return map;
    }


    @RequestMapping("/startUpload")
    @ResponseBody
    public String startVoteCount(){

        return "选举结果已上链";
    }


    @GetMapping("electSearch")
    public String electSearch(){

        return "electSearch";
    }


    @GetMapping("electResultSearch")
    public String electResultSearch(){

        return "electResultSearch";
    }


}
