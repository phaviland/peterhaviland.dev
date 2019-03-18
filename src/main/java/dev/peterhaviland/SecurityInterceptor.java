package dev.peterhaviland;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import dev.peterhaviland.beans.User;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired
    private User user;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (user.getId() == null) {
            response.sendError(401);
            return false;
        }
        else
            return true;
    }

}
