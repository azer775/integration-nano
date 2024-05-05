package tn.esprit.insurance.controllers;

//import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.DocumentException;
import jakarta.annotation.Resource;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.insurance.entities.*;
import tn.esprit.insurance.services.DevisService;
import tn.esprit.insurance.services.InsuranceService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@AllArgsConstructor
@MultipartConfig
@RequestMapping("/dev")
public class DevisController {


    @Autowired
    private DevisService devisService;


    @DeleteMapping("/delete/{idDev}")
    public String deleteDevis(@PathVariable int idDev)
    {
        devisService.deleteDevis(idDev);
        return "Devis removed ! "+idDev;
    }

    @GetMapping("/getall")
    public List<Devis> getAll()
    {
        return devisService.getAll();
    }

    @PutMapping("/update")
    public Devis updateDevis(@RequestBody Devis devis)
    {
        return devisService.updateDevis(devis);
    }

    @GetMapping("/archive")
    public List<Devis> displayArchivedDevis() {
        return devisService.retrieveArchivedinsurance();
    }


    @PostMapping("/toins/{id_ass}")
    public Devis addDevtoIns(@RequestBody Devis i, @PathVariable int id_ass) throws IOException {
        return devisService.addDevtoIns(i,id_ass);
    }
    //list devis selon type d'assurance
    @GetMapping("/ins/{id_ass}/devis")
    public ResponseEntity<List<Devis>> getDevisForIns(@PathVariable int id_ass) {
        List<Devis> devisList = devisService.getDevisForAssurance(id_ass);
        return new ResponseEntity<>(devisList, HttpStatus.OK);
    }

    @PostMapping("/insurance/renewal")
    public void renouvellementAutomatique(@RequestParam int idDev) {
        devisService.renouvellementAutomatique(idDev);
    }


    @GetMapping("/FilterDevisByType")
    public List<Devis> FilterDevis(TypeSin type){
        return devisService.FilterDevis(type);
    }

    @GetMapping("/pdf/{idDev}")
    @ResponseBody
    public ResponseEntity<byte[]> genererDevistPDF(@PathVariable int idDev) {
        try {
            Devis devis = devisService.getDevisById(idDev);
            if (devis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Créez un ByteArrayOutputStream pour stocker le contenu du PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Utilisez outputStream pour générer le PDF (à la place de FileOutputStream)
            devisService.genererContratPDF(devis);

            // Renvoyez le contenu du PDF dans la réponse HTTP
            byte[] pdfBytes = outputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.setContentDispositionFormData("filename", "devis.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/report/{idDev}")
    public String generateAssuranceReport(@PathVariable int idDev) {
        return devisService.generateAssuranceReport(idDev);
    }

    @GetMapping("/risk-score/{idDev}")
    public double calculateRiskScore(@PathVariable int idDev) {
        return devisService.calculateRiskScore(idDev);
    }


    @GetMapping("/sinatt/{id}")
    public int calculateExpectedClaims(@PathVariable int id) {
        return devisService.calculateExpectedClaims(id);
    }

    @GetMapping("/rachat/{idDev}")
    public double calculerValeurRachat(@PathVariable int idDev) {
        return devisService.calculerValeurRachat(idDev);
    }

    @GetMapping("/get/{idDev}")
    public Devis getDevById(@PathVariable int idDev)
    {
        return devisService.getDevById(idDev);
    }

    @GetMapping("/devis/{idDev}/details")
    public Devis getDevisDetails(@PathVariable int idDev) {
        // Récupérer les détails du devis avec son assurance associée et les détails de ses sinistres
        Devis devis = devisService.getDevisById(idDev);
        Insurance insurance = devis.getInsurance();
        Set<Sinister> sinistres = devis.getSinisters();
        return new Devis(devis, insurance, sinistres);
    }

}
