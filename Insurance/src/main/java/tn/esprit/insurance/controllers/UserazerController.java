package tn.esprit.insurance.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.services.UserazerService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping({"/session"})
public class UserazerController {

    private UserazerService userService;

    @GetMapping("/index")
    public String process(Model model, HttpSession session) {
       // @SuppressWarnings("unchecked")
        User user= (User) session.getAttribute("User");
        model.addAttribute("u",user);
        return "videocall";
    }
    @GetMapping("/getuser")
    public User getusersession(HttpSession session){

        User u = (User) session.getAttribute("User");
        System.out.println(u);

        return u;

    }
    @CrossOrigin("*")
    @PostMapping("/login")
    public User login(@RequestParam("msg") int id, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        User user=userService.getUserById(id);
        request.getSession().setAttribute("User",user);
        return user;
    }
    @PostMapping( "/login1/{id}")
    public User login1(@PathVariable int id, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        User user=userService.getUserById(id);
        request.getSession().setAttribute("User",user);
        return user;
    }
    @PostMapping("/logout")
    public void logout(HttpSession session){
        session.removeAttribute("User");
    }

   /* @PostMapping("/destroy")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }*/

    public UserazerController(UserazerService userService) {
        this.userService = userService;
    }
}
