package tn.esprit.insurance.controllers;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.insurance.entities.*;
import tn.esprit.insurance.services.DevisService;
import tn.esprit.insurance.services.SinisterService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/sin")
public class SinisterController {

    @Autowired
    private SinisterService sinisterService;
    @Autowired
    private DevisService devisService;

    @DeleteMapping("/delete/{id_sin}")
    public String deleteSinister(@PathVariable int id_sin)
    {
        sinisterService.deleteSinister(id_sin);
        return "Sinister removed ! " +id_sin;
    }


    @GetMapping("/getall")
    public List<Sinister> getAllS() {

        return sinisterService.getAll();
    }

    @PutMapping("/update")
    public Sinister updateSinister(@RequestBody Sinister sinister) {

        return sinisterService.updateSinister(sinister);
    }


    @PostMapping("/affecter/{idDev}")
    public ResponseEntity<String> affecterSinistre(@PathVariable int idDev, @RequestBody Sinister sin) {
        boolean success = sinisterService.affecterSinistre(sin, idDev);
        if (success) {
            return ResponseEntity.ok("Le sinistre a été affecté au devis avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Impossible d'affecter le sinistre au devis.");
        }
    }

    @PostMapping("upload2")
    public boolean uploadFiles(@RequestParam(value = "paper",required = false ) MultipartFile paper,
                            @RequestParam(value = "amount",required = false) float amount,
                            @RequestParam(value = "cmnt",required = false) String cmnt,
                            @RequestParam(value = "date_sin",required = false) String date_sin,
                            @RequestParam(value = "type_sin",required = false) TypeSin typeSin,
                            @RequestParam(value = "iddv",required = false) int devis) throws IOException {
        Sinister sin = new Sinister();
        sin.setAmount(amount);
        sin.setStatut_sin(Statut_Sin.In_Progress);
        sin.setDate_sin(LocalDate.parse(date_sin));
        sin.setCmnt(cmnt);
        sin.setTypeSin(typeSin);
        System.out.println();
        // Process the files and loan data

        // Save files to the server
        if (paper != null) {
            try {
                sin.setPaper(this.saveFile(paper, "paper"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            return sinisterService.affecterSinistre(sin, (int)devis);

        }
        return false;
    }

    public String saveFile(MultipartFile file, String prefix) throws IOException {
        String destinationDirectory="C:/Users/Ryhab/Desktop/data";
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

    @GetMapping("/stats/by-type")
    public Map<TypeSin, Long> getSinByType() {
        return sinisterService.countSinistersByType();
    }

    @PutMapping("/modify/{id_sin}")
    public Sinister modifyStat(@PathVariable int id_sin)
    {
        return sinisterService.modifyStat(id_sin);
    }

    @GetMapping("/getsin/{idDev}")
    public List<Sinister> getSinByDev(@PathVariable int idDev) {

        return sinisterService.getSinByDev(idDev);
    }

}
