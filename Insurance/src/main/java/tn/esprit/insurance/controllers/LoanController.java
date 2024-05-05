package tn.esprit.insurance.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.insurance.entities.Loan;
import tn.esprit.insurance.entities.Pack;
import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.repositories.UserRepository;
import tn.esprit.insurance.services.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
@RestController
@CrossOrigin("*")
@RequestMapping({"/loan"})
@PreAuthorize("hasRole('USER')")
public class LoanController {
    private LoanService ls;
    @Autowired
    private PackService ps;
    @Autowired
    private UserazerService us;
    @Autowired
    private JwtService js;
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserService service;

    @PostMapping({"/save"})
    public Loan add( @RequestBody Loan l) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        // Find the user based on the username (assuming username is the email)
//        User user = repository.findByEmail(userDetails.getUsername());
       /* System.out.println(user);
        l.setUser(user);*/
        User currentUser;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        currentUser = service.loadUserByEmail(currentEmail);
        l.setUser(currentUser);
        System.out.println(l);
        return this.ls.addLoan(l);
    }


    @PostMapping("upload2")
    public Loan uploadFiles(@RequestParam(value = "cin",required = false ) MultipartFile cin,
                              @RequestParam(value = "possession",required = false) MultipartFile possession,
                              @RequestParam(value = "diploma",required = false) MultipartFile diploma,
                              @RequestParam(value = "work",required = false) MultipartFile work,
                              @RequestParam(value = "bankstat",required = false) MultipartFile bankstat,
                              @RequestParam(value = "state",required = false) String state,
                              @RequestParam(value = "amount",required = false) float amount,
                              @RequestParam(value = "duration",required = false) int duration,
                              @RequestParam(value = "start",required = false) String start,
                              @RequestParam(value = "idp",required = false) int idp,
                            HttpSession session) {
        Loan loan=new Loan();
        loan.setAmount(amount);
        loan.setState("en Attente");
        loan.setStart(LocalDate.parse(start));
        loan.setDuration(duration);
        loan.setPack(this.ps.getPackById(idp));

        User user=new User();
        user =(User) session.getAttribute("User");
        System.out.println(user);
        loan.setUser(user);




        // Process the files and loan data
        try {
            // Save files to the server
            if(cin!=null){
            loan.setCin(this.saveFile(cin,"cin"));
            }
            if(work!=null){
                loan.setWork(this.saveFile(work,"work"));
            }
            if(possession!=null){
                loan.setPossession(this.saveFile(possession,"possession"));
            }
            if(diploma!=null){
                loan.setDiploma(this.saveFile(diploma,"diploma"));
            }
            if(bankstat!=null){
                loan.setBankstat(this.saveFile(bankstat,"bankstat"));
            }

            return ls.addLoan(loan);
        } catch (IOException e) {
            return null;
        }
    }
    @PutMapping("updatewithfiles")
    public Loan updatewithFiles(@RequestParam(value = "cin",required = false ) MultipartFile cin,
                            @RequestParam(value = "possession",required = false) MultipartFile possession,
                            @RequestParam(value = "diploma",required = false) MultipartFile diploma,
                            @RequestParam(value = "work",required = false) MultipartFile work,
                            @RequestParam(value = "bankstat",required = false) MultipartFile bankstat,
                            @RequestParam(value = "state",required = false) String state,
                            @RequestParam(value = "amount",required = false) float amount,
                            @RequestParam(value = "duration",required = false) int duration,
                            @RequestParam(value = "start",required = false) String start,
                            @RequestParam(value = "idp",required = false) int idp,
                            @RequestParam(value = "id") int id) {
        Loan loan=new Loan();
        loan.setAmount(amount);
        loan.setState("en Attente");
        loan.setStart(LocalDate.parse(start));
        loan.setDuration(duration);
        loan.setPack(this.ps.getPackById(idp));
        loan.setId(id);


        // Process the files and loan data
        try {
            // Save files to the server
            if(cin!=null){
                loan.setCin(this.saveFile(cin,"cin"));
            }
            if(work!=null){
                loan.setWork(this.saveFile(work,"work"));
            }
            if(possession!=null){
                loan.setPossession(this.saveFile(possession,"possession"));
            }
            if(diploma!=null){
                loan.setDiploma(this.saveFile(diploma,"diploma"));
            }
            if(bankstat!=null){
                loan.setBankstat(this.saveFile(bankstat,"bankstat"));
            }

            return ls.updateLoan(loan);
        } catch (IOException e) {
            return null;
        }
    }
    public String saveFile(MultipartFile file, String prefix) throws IOException {
        String destinationDirectory="C:/Users/azerb/Desktop/data";
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = generateUniqueFileName(prefix, extension);
        File destFile = new File(destinationDirectory + File.separator + newFileName);
        file.transferTo(destFile);
        return destinationDirectory+"/"+newFileName;
    }

    private String generateUniqueFileName(String prefix, String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = now.format(formatter);
        return prefix + "_" + formattedDateTime + extension;
    }


    @GetMapping({"/all"})
    public List<Loan> all() {
        return this.ls.getAll();
    }

    @PutMapping({"/up"})
    public Loan update(@RequestBody Loan p) {
        return this.ls.updateLoan(p);
    }

    @DeleteMapping({"/delete/{id}"})
    public String delete(@PathVariable int id) {
        this.ls.deleteLoan(id);
        return "removed successfully";
    }
    @GetMapping("/byid/{id}")
    public Loan getbyid(@PathVariable int id){
        return ls.getLoanById(id);
    }
    @GetMapping("/generer-tableau-amortissement/{montantEmprunte}/{tauxInteretAnnuel}/{dureeMois}\"")
    public String genererTableauAmortissement(@PathVariable double montantEmprunte,@PathVariable double tauxInteretAnnuel,@PathVariable int dureeMois) {
        try {
            ls.genererTableauAmortissement(montantEmprunte, tauxInteretAnnuel,dureeMois , "tableau_amortissement1.xlsx");
            return "Fichier Excel généré avec succès";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la génération du fichier Excel";
        }
    }
    @GetMapping("/generer-tableau-amortissement1/{montantEmprunte}/{tauxInteretAnnuel}/{dureeMois}")
    public String genererTableauAmortissement1(@PathVariable double montantEmprunte,@PathVariable double tauxInteretAnnuel,@PathVariable int dureeMois) {
        try {
            ls.genererTableauAmortissement1(montantEmprunte, tauxInteretAnnuel,dureeMois , "tableau_amortissement1.xlsx");
            return "Fichier Excel généré avec succès";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la génération du fichier Excel";
        }
    }
    @GetMapping("/down/{montantEmprunte}/{tauxInteretAnnuel}/{dureeMois}")
    public String down(@PathVariable double montantEmprunte,@PathVariable double tauxInteretAnnuel,@PathVariable int dureeMois, HttpServletResponse response) {
        try {
            ls.genererTableauAmortissement12(montantEmprunte, tauxInteretAnnuel,dureeMois , response);
            return "Fichier Excel généré avec succès";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la génération du fichier Excel";
        }
    }
    @GetMapping(value = "/down1/{montantEmprunte}/{tauxInteretAnnuel}/{dureeMois}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> down2(@PathVariable double montantEmprunte, @PathVariable double tauxInteretAnnuel, @PathVariable int dureeMois, HttpServletResponse response) throws IOException {
        byte[] fileContent = ls.genererTableauAmortissement12(montantEmprunte, tauxInteretAnnuel, dureeMois);

        // Configurez les en-têtes pour le téléchargement du fichier
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("filename", "votre_fichier.xlsx");
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
    @GetMapping("getuser")
    public User getuser(HttpSession session){
        User user=us.getusersession(session);
        System.out.println(user);
        return user;

    }
    @PostMapping("getuser2")
    public User getuser2(HttpSession session){
        User user=us.getusersession(session);
        System.out.println(user);
        return user;

    }

    public LoanController(LoanService loanService, UserazerService us) {
        this.ls = loanService;
        this.us=us;
    }
}
