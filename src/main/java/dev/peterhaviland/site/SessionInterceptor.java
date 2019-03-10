package dev.peterhaviland.site;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dev.peterhaviland.site.beans.User;

public class SessionInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired
    private User user;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, java.lang.Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            modelAndView.addObject("id", user.getId());
            modelAndView.addObject("username", user.getUsername());
        }
    }
    
}
