package com.maskvote.maskvotecounter.Controller;

import com.maskvote.maskvotecounter.Connection.Connection;
import com.maskvote.maskvotecounter.Connection.QueryUPK;
import com.maskvote.maskvotecounter.SocketNet.Login;
import com.maskvote.maskvotecounter.SocketNet.Register;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 16:15 2021/5/27
 * @ Description：
 * @ Version: 1.0
 */

@Controller
public class IndexController {

    @GetMapping(value = {"/", "login"})
    public String loginPage(){

        return "login";
    }

    @PostMapping("login")
    public String main(@RequestParam("token") String token, HttpSession session, Model model){
//        System.out.println(sk);
        String str1 = Login.login("count"+token);
        System.out.println(str1);
        String temp = str1.substring(0,3);
        if (temp.equals("yes")){
            //身份标识
            String temp2 = str1.substring(3, 11);
            //计票员私钥
            String temp3 = str1.substring(11, str1.length());
            session.setAttribute("count", "count");
            session.setAttribute("counter", temp2);
            session.setAttribute("counterSk", temp3);
            /**
             *  获取联合公钥
             */
            QueryUPK.query("upk", Connection.getContract(Connection.getNetwork()));
            return "redirect:/Main";
        }else {
            model.addAttribute("msg","输入的密钥不正确！");
            return "login";
        }
    }


    @GetMapping("Main")
    public String mainPage(){

        return "index";
    }


    @RequestMapping("/counterRegister")
    @ResponseBody
    public String counterRegister(Model model){
        /**
         * 去管理员注册
         */

        String str = Register.register("counterRegister");
        System.out.println(str);
//        model.addAttribute("zhucema", str);
//        return "lxq";
        return str;
    }


}
