package com.maskvote.maskvotecounter.Interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ Author：lxgxgxgxg
 * @ Date：Created in 22:40 2021/5/27
 * @ Description：
 * @ Version: 1.0
 */


public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Object counter = session.getAttribute("count");
        if (counter != null){
            return true;
        }
        request.setAttribute("msg", "请先登录！");
        request.getRequestDispatcher("/").forward(request, response);
        return false;
    }
}
