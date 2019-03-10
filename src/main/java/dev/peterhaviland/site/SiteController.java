package dev.peterhaviland.site;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dev.peterhaviland.site.beans.Post;
import dev.peterhaviland.site.beans.User;
import dev.peterhaviland.site.dao.PostsDAO;
import dev.peterhaviland.site.dao.SequenceDAO;
import dev.peterhaviland.site.dao.UsersDAO;

@Controller
public class SiteController {

    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private PostsDAO postsDAO;
    @Autowired
    private SequenceDAO sequenceDAO;
    @Autowired
    private User user;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Properties properties;
    
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
        
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginAttempt(@RequestParam String username, @RequestParam String password, Model model) {
        if (username == null || (username = username.trim()).isEmpty())
            model.addAttribute("message", messageSource.getMessage("missingUsername", null, Locale.US));
        else if (password == null || password.isEmpty())
            model.addAttribute("message", messageSource.getMessage("missingPassword", null, Locale.US));
        else {
            User userInst;
            if ((userInst = usersDAO.loginAttempt(username)) == null)
                model.addAttribute("message", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
            else if (userInst.getHash() == null)
                model.addAttribute("message", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
            else if (!BCrypt.checkpw(password, userInst.getHash()))
                model.addAttribute("message", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
            else {
                BeanUtils.copyProperties(userInst, user);
                return "redirect:/";
            }
        }
        
        return "login";        
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String registerAttempt(@RequestParam String username, @RequestParam String password, Model model) {
        if (username == null || (username = username.trim()).isEmpty())            
            model.addAttribute("message", messageSource.getMessage("missingUsername", null, Locale.US));
        else if (password == null || password.isEmpty())
            model.addAttribute("message", messageSource.getMessage("missingPassword", null, Locale.US));
        else if (usersDAO.loginAttempt(username) != null)
            model.addAttribute("message", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
        else {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt()); 
            usersDAO.registerAttempt(username, hash);
            model.addAttribute("message", messageSource.getMessage("registrationSuccessful", null, Locale.US));
        }
        return "register";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "index";
    }
    
    @GetMapping("/compose")
    public String compose() {
        if (user.getId() == null)
            return "redirect:/";
        else
            return "compose";
    }
    
    @RequestMapping(value="/compose", method=RequestMethod.POST)
    public String composeAttempt(@RequestParam String subject, @RequestParam String body, Model model) {
        if (subject == null || (subject = subject.trim()).isEmpty())            
            model.addAttribute("message", messageSource.getMessage("missingSubject", null, Locale.US));
        else if (body == null || (body = body.trim()).isEmpty())
            model.addAttribute("message", messageSource.getMessage("missingBody", null, Locale.US));
        else {
            Post post = new Post();
            post.setId(sequenceDAO.findAndUpdateSequence());
            post.setUserId(user.getId());
            post.setUsername(user.getUsername());
            post.setSubject(subject);
            post.setBody(body);
            post.setDate(new Date());
            
            postsDAO.composeAttempt(post);
            
            model.addAttribute("message", messageSource.getMessage("writeSuccessful", null, Locale.US));
        }
        return "compose";
    }
    
    @GetMapping(value="/blog/posts/{id}")
    public String retrievePostById(@PathVariable("id") int id, Model model) {
        Post post = postsDAO.getPost(id);
        if (post == null)
            return "redirect:/";
        model.addAttribute("post", post);
        return "post";
    }
    
    @GetMapping(value="/blog/posts/{id}/edit")
    public String retrievePostByIdForEdit(@PathVariable("id") int id, Model model) {
        Post post = postsDAO.getPost(id);
        if (post == null)
            return "redirect:/";
        model.addAttribute("post", post);
        return "compose";
    }
    
    @RequestMapping(value="/blog/posts/{id}/edit", method=RequestMethod.POST)
    public String editPostById(@PathVariable("id") int id, @RequestParam String subject, @RequestParam String body, Model model) {
        if (subject == null || (subject = subject.trim()).isEmpty())            
            model.addAttribute("message", messageSource.getMessage("missingSubject", null, Locale.US));
        else if (body == null || (body = body.trim()).isEmpty())
            model.addAttribute("message", messageSource.getMessage("missingBody", null, Locale.US));
        else {
            Post post = new Post();
            post.setId(id);
            post.setSubject(subject);
            post.setBody(body);
            
            int updatedCount = postsDAO.updatePost(post);
            if (updatedCount == 0)
                model.addAttribute("message", messageSource.getMessage("editUnsuccessful", null, Locale.US));
        }
        return "redirect:/blog/posts/" + id;
    }
    
    @GetMapping(value="/blog/posts/{id}/delete")
    public String deletePostById(@PathVariable("id") int id, Model model) {
        postsDAO.deletePost(id);
        return "redirect:/blog";
    }
    
    @GetMapping("/blog")
    public String blog(Model model) {
        List<Post> posts = postsDAO.getMostRecentPosts(properties.getPageSize());
        model.addAttribute("posts", posts);
        return "blog";
    }
    
    @RequestMapping(value="/blog/loadMorePosts", method=RequestMethod.POST)
    public String blogLoadMorePosts(@RequestParam int offset, Model model) {
        List<Post> posts = postsDAO.getPosts(offset, properties.getPageSize());
        model.addAttribute("posts", posts);
        return "blog :: articles";
    }
}
