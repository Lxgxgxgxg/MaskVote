package com.maskvote.maskvoter.Controller;

import com.maskvote.maskvoter.Connection.Connection;
import com.maskvote.maskvoter.Connection.QueryUPK;
import com.maskvote.maskvoter.SocketNet.Login;
import com.maskvote.maskvoter.SocketNet.Register;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 22:02 2021/5/25
 * @ Description：
 * @ Version: 1.0
 */

@Controller
@CrossOrigin
public class IndexController {

    @GetMapping(value = {"/", "login"})
    public String loginPage(){

        return "login";
    }

    @PostMapping("login")
    public String main(@RequestParam("sk") String sk, HttpSession session, Model model){
//            System.out.println(voter.getSk());
//        String str1 = sk.getSk().toString();
        System.out.println(sk);
        String str2 = Login.login("voter"+sk);
        System.out.println(str2);
        String temp = str2.substring(0, 3);
        System.out.println(temp);
        String temp2 = str2.substring(3, str2.length());
        System.out.println(temp2);
        if (temp.equals("yes")){
            session.setAttribute("loginUser", temp2);
            session.setAttribute("vote", temp2);
            /**
             *  获取联合公钥
             */
            QueryUPK.query("upk", Connection.getContract(Connection.getNetwork()));
            return "redirect:/Main.html";
        }else{
            model.addAttribute("msg", "输入的密钥不正确！");
            return "login";
        }
    }

    @GetMapping("/Main.html")
    public String mainPage(){
        return "index";
    }




    @RequestMapping("/voteRegister")
    @ResponseBody
    public String voteRegister(Model model){
        /**
         * 去管理员注册
         */

        String str = Register.register("voterRegister");
        System.out.println(str);
//        model.addAttribute("zhucema", str);
        return str;
    }

    @RequestMapping("/startUpload")
    @ResponseBody
    public String startVoteCount(){

        return "选举结果已上链";
    }


//    @RequestMapping("zhuCeMa")
//    public String

}
