package com.peter_haviland.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.peter_haviland.site.beans.Session;
import com.peter_haviland.site.dao.UsersDAO;

@Controller
public class SiteController {

    @Autowired
    UsersDAO usersDAO;
    @Autowired
    private Session session;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("username", session.getId());
        return "index";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginAttempt(@RequestParam String username, @RequestParam String password, Model model) {
        if(usersDAO.login(username, password) == false)
            return "login";
        return "redirect:/";
    }

}
