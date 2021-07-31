package com.maskvote.maskvoter.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 16:24 2021/5/26
 * @ Description：
 * @ Version: 1.0
 */

public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null){
            return true;
        }
        request.setAttribute("msg", "请先登录！");
        request.getRequestDispatcher("/").forward(request, response);
        return false;
    }
}
