package tn.esprit.insurance.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.insurance.entities.Loan;
import tn.esprit.insurance.entities.Recoveries;
import tn.esprit.insurance.services.LoanService;
import tn.esprit.insurance.services.RecoveriesService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping({"/recoveries"})
public class RecoveriesController {
    private RecoveriesService ps;
    private LoanService ls;

    @PostMapping({"/save"})
    public Recoveries add(@RequestBody Recoveries p, Model model, HttpSession session) {
        return this.ps.addRecoveries(p);
    }

    @GetMapping({"/all"})
    public List<Recoveries> all() {
        return this.ps.getAll();
    }

    @PutMapping({"/up"})
    public Recoveries update(@RequestBody Recoveries p) {
        return this.ps.updateRecoveries(p);
    }

    @DeleteMapping({"/delete/{id}"})
    public String delete(@PathVariable int id) {
        this.ps.deleteRecoveries(id);
        return "removed successfully";
    }
    @PostMapping("val/{id}")
    public List<Recoveries> validate(@PathVariable("id") int id){
        Loan l =ls.getLoanById(id);
        return ps.validateloan(l);

    }
    @GetMapping("/byl/{id}")
    List<Recoveries> byloan(@PathVariable("id") int id){
        Loan l =ls.getLoanById(id);
        return ps.getbyloan(l);}
    @GetMapping("/stat")
    List<Object[]> stat(){
        return ps.stat();
    }
    @GetMapping("/late/{id}")
    Integer latedays(@PathVariable("id") int id){
        return ps.latedays(id);
    }

    public RecoveriesController(RecoveriesService ps,LoanService ls) {
        this.ps = ps;
        this.ls=ls;
    }

}
