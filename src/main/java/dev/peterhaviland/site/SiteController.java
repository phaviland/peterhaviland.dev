package dev.peterhaviland.site;

import java.util.Locale;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dev.peterhaviland.site.beans.Session;
import dev.peterhaviland.site.dao.UsersDAO;

@Controller
public class SiteController {

    @Autowired
    UsersDAO usersDAO;
    @Autowired
    private Session session;
    @Autowired
    private MessageSource messageSource;
    
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
        if (!validateLogin(username, password, model))
            return "login";
        
        Document account;
        String hash;
        if ((account = usersDAO.loginAttempt(username)) == null)
            model.addAttribute("error", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
        else if ((hash = account.getString("hash")) == null)
            model.addAttribute("error", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
        else if (!BCrypt.checkpw(password, hash))
            model.addAttribute("error", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
        else {
            session.setId(account.getString("_id"));
            return "redirect:/";
        }
        
        return "login";        
    }
    
    public boolean validateLogin(String username, String password, Model model) {
        if (username == null || username.isEmpty())
            model.addAttribute("error", messageSource.getMessage("missingUsername", null, Locale.US));
        else if (password == null || password.isEmpty())
            model.addAttribute("error", messageSource.getMessage("missingPassword", null, Locale.US));
        else
            return true;
        return false;
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String registerAttempt(@RequestParam String username, @RequestParam String password, Model model) {
        if (!validateRegistration(username, password, model))
            return "register";
        
        String hash = BCrypt.hashpw(password, BCrypt.gensalt()); 
        usersDAO.registerAttempt(username, hash);
        session.setId(username);
        
        return "redirect:/";
    }
    
    public boolean validateRegistration(String username, String password, Model model) {
        if (username == null || username.isEmpty())            
            model.addAttribute("error", messageSource.getMessage("missingUsername", null, Locale.US));
        else if (password == null || password.isEmpty())
            model.addAttribute("error", messageSource.getMessage("missingPassword", null, Locale.US));
        else if (usersDAO.loginAttempt(username) != null)
            model.addAttribute("error", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
        else
            return true;
        return false;
    }

}
