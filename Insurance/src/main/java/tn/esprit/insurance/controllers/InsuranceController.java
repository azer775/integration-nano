package tn.esprit.insurance.controllers;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.insurance.entities.ExcelData;
import tn.esprit.insurance.entities.ExcelReader;
import tn.esprit.insurance.entities.Insurance;
import tn.esprit.insurance.entities.TypeAss;
import tn.esprit.insurance.repositories.IInsuranceRepository;
import tn.esprit.insurance.services.InsuranceService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@AllArgsConstructor
@MultipartConfig
@CrossOrigin
@RequestMapping("/ins")
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private IInsuranceRepository insuranceRepository;

    @PostMapping("/save")
    public Insurance addInsurance(@RequestBody Insurance insurance)
    {
        return insuranceService.addInsurance(insurance);

    }

    @DeleteMapping("/delete/{id_ass}")
    public void deleteInsurance(@PathVariable int id_ass)
    {
        insuranceService.deleteInsurance(id_ass);

    }



    @GetMapping("/getall")
    public List<Insurance> getAll()
    {
        return insuranceService.getAll();
    }

    @PutMapping("/update")
    public Insurance updateInsurance(@RequestBody Insurance insurance)
    {
        return insuranceService.updateInsurance(insurance);
    }

    @GetMapping("/get/{id_ass}")
    public Insurance getInsuranceById(@PathVariable int id_ass)
    {
        return insuranceService.getInsuranceById(id_ass);
    }


    @PostMapping("/upload")
    public void uploadInsFromExcel(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        ExcelReader excelReader = new ExcelReader();
        List<ExcelData> excelDataList = excelReader.readExcelFile(file);
        insuranceService.saveInsFromExcel(excelDataList);
    }

    @GetMapping("/report/{id_ass}")
    public String generateAssuranceReport(@PathVariable int id_ass) {
        return insuranceService.generateAssuranceReport(id_ass);
    }

    @GetMapping("/typeAssById/{id_ass}")
    public TypeAss getTypeAssById(@PathVariable int id_ass) {
        return insuranceService.getTypeAssById(id_ass);
    }


}
