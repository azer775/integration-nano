package tn.esprit.insurance.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tn.esprit.insurance.services.InfoExtractor;
import tn.esprit.insurance.services.PdfParsingService;

import dev.langchain4j.data.document.Document;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/pdf")
public class PdfParsingController {
    private PdfParsingService pdfParsingService;
    private InfoExtractor infoExtractor;
    @GetMapping("/extract-salaire-brut")
    public ResponseEntity<String> extractSalaireBrut1() {
        String filePath="C:\\Users\\azerb\\Desktop\\Fiche-de-paie-type-1.pdf";
        try {
            String salaireBrut = pdfParsingService.extractSalaireBrutLine(filePath);//.extractSalaireBrut1(filePath);
            if (!"Non trouvé".equals(salaireBrut)) {
                System.out.println(salaireBrut);
                return ResponseEntity.ok(salaireBrut);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salaire brut non trouvé dans la fiche de paie");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du parsing du PDF");
        }
    }
    @GetMapping("par")
    public String test(){
        String filePath="C:\\Users\\azerb\\Desktop\\Fiche-de-paie-type-1.pdf";
        return pdfParsingService.extractSalaireBrutFromPdf(filePath);
    }
    @GetMapping("info")
    public Object testlang(){
        //return infoExtractor.Simple_Prompt();
        return infoExtractor.IfYouNeedSimplicity();
    }
    @GetMapping("info22")
    public Document testp(){
        return infoExtractor.testpdf();
    }
    @PostMapping("info2")
    public Document testp1(@RequestBody String path){
        return infoExtractor.testpdf();
    }
    @PostMapping("chat")
    public Object chat(@RequestBody String q){
        return infoExtractor.chat(q);
    }

}
