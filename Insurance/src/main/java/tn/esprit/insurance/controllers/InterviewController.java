package tn.esprit.insurance.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.insurance.entities.Interview;
import tn.esprit.insurance.services.InterviewService;
import tn.esprit.insurance.services.LoanService;
import tn.esprit.insurance.services.MailingService;
import tn.esprit.insurance.services.UserazerService;

import java.util.List;

@RestController
@RequestMapping({"/Interview"})
public class InterviewController {
    @Autowired
    MailingService mailingService;
    @Autowired
    LoanService ls;
    @Autowired
    UserazerService us;
    InterviewService ins;
    @PostMapping({"/save/{id}"})
    public Interview add(@RequestBody Interview l, @PathVariable int id, HttpSession session) throws MessagingException {
        l.setLoan(ls.getLoanById(id));
        l.setAgent(us.getusersession(session));
        l=(Interview) this.ins.addInterview(l);
        mailingService.sendEmail(l.getAgent(),"meeting invitation",l);
        mailingService.sendEmail(l.getLoan().getUser(),"meeting invitation",l);
        return l;
    }

    @GetMapping({"/all"})
    public List<Interview> all() {
        return this.ins.getAll();
    }

    @PutMapping({"/up"})
    public Interview update(@RequestBody Interview p) {
        return this.ins.updateInterview(p);
    }

    @DeleteMapping({"/delete/{id}"})
    public String delete(@PathVariable int id) {
        this.ins.deleteInterview(id);
        return "removed successfully";
    }
    @GetMapping("/byid/{id}")
    public Interview getbyid(@PathVariable int id){
        return ins.getInterviewById(id);
    }
    /*@GetMapping("send")
    public void send() throws MessagingException {
        mailingService.sendEmail("azerbennasr@gmail.com","test","test");
    }*/
    public InterviewController(InterviewService ins,MailingService mailingService){
        this.ins=ins;
        this.mailingService=mailingService;
    }
}
