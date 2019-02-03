package com.peter_haviland.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.peter_haviland.site.dao.UsersDAO;

@Controller
public class SiteController {

    @Autowired
    UsersDAO usersDAO;
    
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

}
