package com.maskvote.maskvoter.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.maskvote.maskvoter.Connection.Connection;
import com.maskvote.maskvoter.Connection.QueryResult;
import com.maskvote.maskvoter.Encrypto.Commit;
import com.maskvote.maskvoter.Encrypto.ReadBase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 14:46 2021/5/26
 * @ Description：
 * @ Version: 1.0
 */

@Controller
public class VoteController {


    /**
     * 设置全局变量
     */
    String electAffairStr = null;
    String electStartTime = null;
    String electEndTime = null;
    String electSearchNum = null;
    String electNum = null;

    String voteAffairStr = null;
    String voteStartTime = null;
    String voteEndTime = null;
    String voteSearchNum = null;
    String voteNum = null;

    /**
     * 保存选举事务的结果集
     */
    HashMap<String,String> electMap = new HashMap<>();
    /**
     * 保存每一个选举事务
     */
    HashMap<String, HashMap<String, String>> allElectMap = new HashMap<>();

    /**
     * 保存表决事务的结果集
     */
    HashMap<String,String> voteMap = new HashMap<>();
    /**
     * 保存每一个选举事务
     */
    HashMap<String, HashMap<String, String>> allVoteMap = new HashMap<>();

    /**
     * 跳转到投票主页面
     * @return
     */
    @RequestMapping("electMain")
    public String electMain(@RequestParam("electSearchNum") String searchNum, Model model){
        /**
         * 从链上获取选举信息
         */
        String string = QueryResult.queryResult(searchNum, Connection.getContract(Connection.getNetwork()));
        electSearchNum  = searchNum;
//        String string = "{\"104088118271759868796343142729660204418203271561297695421065426846521342038688\":" +
//                "{\"candidateNum\":3,\"electAffairStr\":\"校长选举\",\"electNum\":4,\"endTime\":\"2021-06-24 11:40:00\"," +
//                "\"id\":\"002\",\"startTime\":\"2021-06-23 09:00:00\"}}";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(string);
        string = string.replaceAll("\\\\\"", "\"");
        String str1 = JSON.parseObject(string).getString(searchNum);
        electAffairStr = JSON.parseObject(str1).getString("electAffairStr");

        //投票开始时间
        electStartTime = JSON.parseObject(str1).getString("electStartTime");
        //结束时间
        electEndTime = JSON.parseObject(str1).getString("electEndTime");
        //选举人数
        electNum = JSON.parseObject(str1).getString("electNum");


        /**
         * 吧- 替换成 /
         */
        String newEndTime1 = electEndTime.replaceAll("-", "/");
//        System.out.println(newEndTime1);

        electMap.put("electAffairStr", electAffairStr);
        electMap.put("electStartTime", electStartTime);
        electMap.put("electEndTime", electEndTime);
        electMap.put("electNum", electNum);
        allElectMap.put(electSearchNum, electMap);

//        //当前时间
//        Date currentTime = null;
//        //结束时间
//        Date newEndTime = null;
//        try {
//            currentTime = df.parse(df.format(new Date()));
//            newEndTime = df.parse(JSON.parseObject(str1).getString("endTime"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        long diff = newEndTime.getTime() - currentTime.getTime();//这样得到的差值是微秒级别
//
//        long days = diff / (1000 * 60 * 60 * 24);
//        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60); //获取时
//        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);  //获取分钟
//        long s=(diff/1000-days*24*60*60-hours*60*60-minutes*60);//获取秒
//
//        System.out.println(days+"天"+hours+"小时"+minutes+"分"+s+"秒");

        model.addAttribute("electAffairStr", electAffairStr);
        model.addAttribute("electStartTime", electStartTime);
        model.addAttribute("electEndTime", newEndTime1);



//        String remainTime = JSON.parseObject(str1).getString("electAffairStr");
//        System.out.println(str1_1);
        return "electMain";
    }

    /**
     * 选举检索页面
     */
    @RequestMapping("electSearch")
    public String electSearch(){


        return "electSearch";
    }


    /**
     * 跳转到表决的主页面
     * @return
     */
    @RequestMapping("voteMain")
    public String voteMain(@RequestParam("voteSearchNum") String searchNum, Model model){

        /**
         * 从链上获取选举信息
         */
        String string = QueryResult.queryResult(searchNum, Connection.getContract(Connection.getNetwork()));

        voteSearchNum = searchNum;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(string);
        string = string.replaceAll("\\\\\"", "\"");
        String str1 = JSON.parseObject(string).getString(searchNum);
        voteAffairStr = JSON.parseObject(str1).getString("voteAffairStr");
        //投票开始时间
        voteStartTime = JSON.parseObject(str1).getString("voteStartTime");
        //结束时间
        voteEndTime = JSON.parseObject(str1).getString("voteEndTime");
        //选举人数
        voteNum = JSON.parseObject(str1).getString("voteNum");

        /**
         * 吧- 替换成 /
         */
        String newEndTime1 =  voteEndTime.replaceAll("-", "/");
//        System.out.println(newEndTime1);

        voteMap.put("voteAffairStr", voteAffairStr);
        voteMap.put("voteStartTime", voteStartTime);
        voteMap.put("voteEndTime", voteEndTime);
        voteMap.put("voteNum", voteNum);

        allVoteMap.put(voteSearchNum, voteMap);

        model.addAttribute("voteAffairStr", voteAffairStr);
        model.addAttribute("voteStartTime", voteStartTime);
        model.addAttribute("voteEndTime", newEndTime1);

        return "voteMain";
    }


    /**
     * 表决检索页面
     */
    @RequestMapping("voteSearch")
    public String voteSearch(){


        return "voteSearch";
    }



    BigInteger[] arr = ReadBase.readGroupBase();
    /**
     * 跳转到投票等待页面
     * @return
     */
    @RequestMapping("electWaitPage")
    public String electWait(@RequestParam(value = "election",required = true) String election, HttpServletRequest request, Model model){
//        System.out.println(election);
        HttpSession session = request.getSession();
        Object voter = session.getAttribute("vote");
//        System.out.println(voter);
        if (election.equals("一")){
            //把票投给了1号
            BigInteger value1 = BigInteger.valueOf(1);
            BigInteger value2 = BigInteger.valueOf(0);
            BigInteger value3 = BigInteger.valueOf(0);
            Commit.CommitAll(electSearchNum, voter.toString(), value1, value2, value3, arr);

        } else if (election.equals("二")) {
            //把票投给了2号
            BigInteger value1 = BigInteger.valueOf(0);
            BigInteger value2 = BigInteger.valueOf(1);
            BigInteger value3 = BigInteger.valueOf(0);
            Commit.CommitAll(electSearchNum, voter.toString(), value1, value2, value3, arr);

        } else {
            //把票投给了3号
            BigInteger value1 = BigInteger.valueOf(0);
            BigInteger value2 = BigInteger.valueOf(0);
            BigInteger value3 = BigInteger.valueOf(1);
            Commit.CommitAll(electSearchNum, voter.toString(), value1, value2, value3, arr);

        }

        model.addAttribute("election", election);
        model.addAttribute("electAffairStr", electAffairStr);
        model.addAttribute("electStartTime", electStartTime);
        model.addAttribute("electEndTime", electEndTime);
        return "electWaitPage";
    }


    /**
     * 跳转到投票结果页面
     * @return
     */
    @RequestMapping(value = {"electResultPage.html", "electResultPage"})
    public String electResult(@RequestParam("electSearchNum") String electSearchNum1, Model model){
        System.out.println(electSearchNum1);
        String result = QueryResult.queryResult(electSearchNum1+"counter1VoteResult", Connection.getContract(Connection.getNetwork()));
//        String result = "{\"vote1Result\":1700665966400988042079266337689,\"vote2Result\":1,\"vote3Result\":1}";
        String str1 = JSON.parseObject(result).getString("vote1Result");
        String str2 = JSON.parseObject(result).getString("vote2Result");
        String str3 = JSON.parseObject(result).getString("vote3Result");
        HashMap<String, Integer> map = Commit.querySet();
        int a = map.get(str1);
        int b = map.get(str2);
        int c = map.get(str3);

        /**
         * 根据搜索码获取对应的选举事务
         */
        HashMap<String, String> affairMap = allElectMap.get(electSearchNum1);

        model.addAttribute("electAffairStr", affairMap.get("electAffairStr"));
        model.addAttribute("electStartTime", affairMap.get("electStartTime"));
        model.addAttribute("electEndTime", affairMap.get("electEndTime"));
        model.addAttribute("electNum", affairMap.get("electNum"));

        model.addAttribute("vote1Result",a);
        model.addAttribute("vote2Result",b);
        model.addAttribute("vote3Result",c);

        return "electResultPage";
    }


    /**
     * 跳转到表决等待页面
     * @return
     */
    @PostMapping("voteWaitPage")
    public String voteWait(@RequestParam("vote") int vote, HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        Object voter = session.getAttribute("vote");

        if (vote == 1){
            BigInteger value = BigInteger.valueOf(1);
            Commit.CommitOneTransaction(voteSearchNum, voter.toString(), value, arr);
        }else {
            BigInteger value = BigInteger.valueOf(0);
            Commit.CommitOneTransaction(voteSearchNum, voter.toString(), value, arr);
        }

        model.addAttribute("vote", vote);
//        System.out.println(vote);
        /**
         * 吧- 替换成 /
         */
        String newEndTime1 =  voteEndTime.replaceAll("-", "/");
        model.addAttribute("voteAffairStr", voteAffairStr);
        model.addAttribute("voteStartTime", voteStartTime);
        model.addAttribute("voteEndTime", newEndTime1);
        return "voteWaitPage";
    }


    /**
     * 跳转到表决结果页面
     * @return
     */
    @RequestMapping(value = {"voteResultPage.html", "voteResultPage"})
    public String voteResult(@RequestParam("voteSearchNum") String voteSearchNum1, Model model){

        //获取表决的结果
        String voteResult = QueryResult.queryResult(voteSearchNum1+"counter2BJResult", Connection.getContract(Connection.getNetwork()));
//        String voteResult = "{\"vote1Result\":1700665966400988042079266337689}";
        //string转JSON
        String res = JSON.parseObject(voteResult).getString("vote1Result");
        HashMap<String, Integer> map = Commit.querySet();

        int a = map.get(res);
//        int a = 2;
        int b = 4 - a;

        HashMap<String, String> affairMap = allVoteMap.get(voteSearchNum1);

        model.addAttribute("voteAffairStr", affairMap.get("voteAffairStr"));
        model.addAttribute("voteStartTime", affairMap.get("voteStartTime"));
        model.addAttribute("voteEndTime",affairMap.get("voteEndTime"));
        model.addAttribute("voteNum", affairMap.get("voteNum"));

        model.addAttribute("vote1Result",a);
        model.addAttribute("vote2Result",b);
        return "voteResultPage";
    }


    @GetMapping("electResultSearch")
    public String electResultSearch(){

        return "electResultSearch";
    }

    @GetMapping("voteResultSearch")
    public String voteResultSearch(){

        return "voteResultSearch";
    }
}
