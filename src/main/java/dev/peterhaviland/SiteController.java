package dev.peterhaviland;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import dev.peterhaviland.beans.Post;
import dev.peterhaviland.beans.User;
import dev.peterhaviland.dao.PostsDAO;
import dev.peterhaviland.dao.SequenceDAO;
import dev.peterhaviland.dao.UsersDAO;
import dev.peterhaviland.forms.ComposeForm;
import dev.peterhaviland.forms.LoginForm;
import dev.peterhaviland.forms.RegisterForm;

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
    
    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.addHeader("Content-Security-Policy", "default-src 'self'; font-src 'none'; img-src 'self' data:; object-src 'none'; script-src 'self' https://code.jquery.com https://cdnjs.cloudflare.com https://stackpath.bootstrapcdn.com; style-src 'self' https://stackpath.bootstrapcdn.com; frame-ancestors 'none'");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("X-XSS-Protection", "1; mode=block");
    }
    
    @ModelAttribute
    public void setModel(Model model) {
        model.addAttribute("id", user.getId());
    }    
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login(LoginForm loginForm) {
        return "login";
    }
        
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ModelAndView loginAttempt(@Valid LoginForm loginForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            User userInst;
            if ((userInst = usersDAO.loginAttempt(loginForm.getUsername())) == null)
                model.addAttribute("error", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
            else if (userInst.getHash() == null)
                model.addAttribute("error", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
            else if (!BCrypt.checkpw(loginForm.getPassword(), userInst.getHash()))
                model.addAttribute("error", messageSource.getMessage("invalidUsernamePassword", null, Locale.US));
            else {
                BeanUtils.copyProperties(userInst, user);
                RedirectView view = new RedirectView("/", true);
                view.setExposeModelAttributes(false);
                return new ModelAndView(view);
            }
        }
        return new ModelAndView("login");        
    }
    
    @GetMapping("/register")
    public String register(RegisterForm registerForm) {
        return "register";
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String registerAttempt(@Valid RegisterForm registerForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {        
            if (!"true".equalsIgnoreCase(properties.getAllowRegistrations()))
                model.addAttribute("error", messageSource.getMessage("registrationsDisabled", null, Locale.US));
            else {
                String hash = BCrypt.hashpw(registerForm.getPassword(), BCrypt.gensalt()); 
                usersDAO.registerAttempt(registerForm.getUsername(), hash);
                model.addAttribute("message", messageSource.getMessage("registrationSuccessful", null, Locale.US));
            }
        }
        model.addAttribute(registerForm.getUsername());
        return "register";
    }
    
    @GetMapping("/logout")
    public ModelAndView logout(HttpSession httpSession) {
        httpSession.invalidate();
        RedirectView view = new RedirectView("/", true);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }
    
    @GetMapping("/compose")
    public String compose(ComposeForm composeForm) {
        return "compose";
    }
    
    @RequestMapping(value="/compose", method=RequestMethod.POST)
    public ModelAndView composePost(@Valid ComposeForm composeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ModelAndView("compose");

        Post post = new Post();
        post.setId(sequenceDAO.findAndUpdateSequence());
        post.setUserId(user.getId());
        post.setUsername(user.getUsername());
        post.setSubject(composeForm.getSubject());
        post.setBody(composeForm.getBody());
        post.setDate(new Date());
        postsDAO.composePost(post);
        
        RedirectView view = new RedirectView("/blog/posts/" + post.getId(), true);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }
    
    @GetMapping(value="/blog/posts/{id}")
    public ModelAndView retrievePostById(@PathVariable("id") int id, Model model) {
        Post post = postsDAO.getPost(id);
        if (post == null) {
            RedirectView view = new RedirectView("/", true);
            view.setExposeModelAttributes(false);
            return new ModelAndView(view);
        }
        model.addAttribute("post", post);
        return new ModelAndView("post");
    }
    
    @GetMapping(value="/blog/posts/{id}/edit")
    public ModelAndView retrievePostByIdForEdit(@PathVariable("id") int id, ComposeForm composeForm) {
        Post post = postsDAO.getPost(id);
        if (post == null) {
            RedirectView view = new RedirectView("/", true);
            view.setExposeModelAttributes(false);
            return new ModelAndView(view);
        }
        composeForm.setSubject(post.getSubject());
        composeForm.setBody(post.getBody());
        return new ModelAndView("compose");
    }
    
    @RequestMapping(value="/blog/posts/{id}/edit", method=RequestMethod.POST)
    public ModelAndView editPostById(@PathVariable("id") int id, @Valid ComposeForm composeForm, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (postsDAO.updatePost(id, composeForm.getSubject(), composeForm.getBody()) == 0)
                model.addAttribute("error", messageSource.getMessage("editUnsuccessful", null, Locale.US));
            else {
                RedirectView view = new RedirectView("/blog/posts/" + id, true);
                view.setExposeModelAttributes(false);
                return new ModelAndView(view);
            }
        }
        
        Post post = postsDAO.getPost(id);
        if (post == null) {
            RedirectView view = new RedirectView("/", true);
            view.setExposeModelAttributes(false);
            return new ModelAndView(view);
        }
        composeForm.setSubject(post.getSubject());
        composeForm.setBody(post.getBody());    
        return new ModelAndView("compose");
    }
    
    @GetMapping(value="/blog/posts/{id}/delete")
    public ModelAndView deletePostById(@PathVariable("id") int id) {
        postsDAO.deletePost(id);
        
        RedirectView view = new RedirectView("/blog", true);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }
    
    @GetMapping("/blog")
    public String blog(Model model) {
        List<Post> posts = postsDAO.getMostRecentPosts(properties.getPageSize());
        model.addAttribute("posts", posts);
        return "blog";
    }
    
    @RequestMapping(value="/blog/loadMorePosts", method=RequestMethod.POST)
    public String blogLoadMorePosts(@RequestParam int minArticleId, Model model) {
        List<Post> posts = postsDAO.getPosts(minArticleId, properties.getPageSize());
        model.addAttribute("posts", posts);
        return "blog :: articles";
    }
    
    /*
    @RequestMapping(value="/blog/posts/{id}", method=RequestMethod.POST)
    public ModelAndView composeComment(@PathVariable("id") int id, @RequestParam String username, @RequestParam String body, RedirectAttributes redirectAttributes) {
        if (username == null || (username = username.trim()).isEmpty())            
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("missingUsername", null, Locale.US));
        else if (body == null || (body = body.trim()).isEmpty())
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("missingBody", null, Locale.US));
        else {
            Comment comment = new Comment();
            comment.setUsername(username);
            comment.setDate(new Date());
            comment.setBody(body);

            if (postsDAO.composeComment(id, comment) == 0)
                redirectAttributes.addFlashAttribute("message", messageSource.getMessage("commentUnsuccessful", null, Locale.US));
        }
        
        RedirectView view = new RedirectView("/blog/posts/" + id, true);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }
    */
    
}