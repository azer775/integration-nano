package tn.esprit.insurance.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.esprit.insurance.entities.Interview;
import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.services.InterviewService;

@Controller
@RequestMapping("/r")
public class VideocallController {

    InterviewService interviewService;
    @GetMapping("/videocall")
    public String process(Model model, HttpSession session, @RequestParam("roomID")  int roomID) {
        Interview i=interviewService.getInterviewById(roomID);
        @SuppressWarnings("unchecked")
        User user= (User) session.getAttribute("User");
        model.addAttribute("u",user.getFirstname());
        model.addAttribute("providedDate",i.getDate());
        model.addAttribute("agentid",i.getAgent().getId());
        model.addAttribute("clientid",i.getLoan().getUser().getId());
        model.addAttribute("currentuser",user.getId());
        return "videocall";
    }

    public VideocallController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }
}
