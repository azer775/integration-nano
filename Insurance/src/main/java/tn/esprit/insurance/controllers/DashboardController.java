package tn.esprit.insurance.controllers;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.insurance.services.DevisService;
import tn.esprit.insurance.services.InsuranceService;
import tn.esprit.insurance.services.SinisterService;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@MultipartConfig
@RequestMapping("/dash")
public class DashboardController {
    @Autowired
    private final DevisService devisService;
    @Autowired
    private final SinisterService sinisterService;
    @Autowired
    private final InsuranceService insuranceService;

    @GetMapping("/countins")
    public long countInsurances() {
        return sinisterService.countInsurances();
    }
    @GetMapping("/countdev")
    public long countDevis() {
        return sinisterService.countDevis();
    }
    @GetMapping("/countByType")
    public List<Object[]> countDevisByInsuranceType() {
        return sinisterService.countDevisByInsuranceType();
    }
    @GetMapping("/countsin")
    public long countSinisters() {
        return sinisterService.countSinisters();
    }
}
