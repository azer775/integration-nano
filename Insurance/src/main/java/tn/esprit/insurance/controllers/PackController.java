package tn.esprit.insurance.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.insurance.entities.Loan;
import tn.esprit.insurance.entities.Pack;
import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.services.PackService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping({"/pack"})
@PreAuthorize("hasRole('ADMIN')")
public class PackController {
    private PackService ps;
    @PostMapping({"/sa"})
    public void all1(){
        this.ps.all();
    }

    @PostMapping({"/save"})
    public Pack add(@RequestBody Pack p) {
        return this.ps.addPack(p);
    }

    @GetMapping({"/all"})
    public List<Pack> all() {
        return this.ps.getAll();
    }

    @PutMapping({"/up"})
    public Pack update(@RequestBody Pack p) {
        return this.ps.updatePack(p);
    }

    @DeleteMapping({"/delete/{id}"})
    public String delete(@PathVariable int id) {
        this.ps.deletePack(id);
        return "removed successfully";
    }
    @GetMapping("/{id}")
    public Pack Byid(@PathVariable int id){
        return this.ps.getPackById(id);
    }

    public PackController(PackService ps) {
        this.ps = ps;
    }
    @PostMapping("/upload")
    public Pack uploadFiles(@RequestParam(value = "cin",required = false ) boolean cin,
                            @RequestParam(value = "possession",required = false) boolean possession,
                            @RequestParam(value = "diploma",required = false) boolean diploma,
                            @RequestParam(value = "work",required = false) boolean work,
                            @RequestParam(value = "bankstat",required = false) boolean bankstat,
                            @RequestParam(value = "img",required = false) MultipartFile img,
                            @RequestParam(value = "max",required = false) float max,
                            @RequestParam(value = "min",required = false) float min,
                            @RequestParam(value = "name",required = false) String name,
                            @RequestParam(value = "interest",required = false) int interest,
                            @RequestParam(value = "target",required = false) String target,
                            @RequestParam(value = "description",required = false) String description) {
        Pack pack=new Pack();
        pack.setCin(cin);
        pack.setPossession(possession);
        pack.setDiploma(diploma);
        pack.setWork(work);
        pack.setBankstat(bankstat);
        pack.setMax(max);
        pack.setMin(min);
        pack.setName(name);
        pack.setInterest(interest);
        pack.setTarget(target);
        pack.setDescription(description);





        // Process the files and loan data
        try {
            // Save files to the server
            if(img!=null){
                pack.setImg(this.saveFile(img,"img"));
                System.out.println("cv");
            }


            return ps.addPack(pack);
        } catch (IOException e) {
            return null;
        }
    }
    @PutMapping("/updatedwithfiles")
    public Pack updated(@RequestParam(value = "cin",required = false ) boolean cin,
                            @RequestParam(value = "possession",required = false) boolean possession,
                            @RequestParam(value = "diploma",required = false) boolean diploma,
                            @RequestParam(value = "work",required = false) boolean work,
                            @RequestParam(value = "bankstat",required = false) boolean bankstat,
                            @RequestParam(value = "img",required = false) MultipartFile img,
                            @RequestParam(value = "max",required = false) float max,
                            @RequestParam(value = "min",required = false) float min,
                            @RequestParam(value = "name",required = false) String name,
                            @RequestParam(value = "interest",required = false) int interest,
                            @RequestParam(value = "target",required = false) String target,
                            @RequestParam(value = "description",required = false) String description,
                            @RequestParam(value = "id") int id) {
        Pack pack=new Pack();
        pack.setCin(cin);
        pack.setPossession(possession);
        pack.setDiploma(diploma);
        pack.setWork(work);
        pack.setBankstat(bankstat);
        pack.setMax(max);
        pack.setMin(min);
        pack.setName(name);
        pack.setInterest(interest);
        pack.setTarget(target);
        pack.setDescription(description);
        pack.setId(id);




        // Process the files and loan data
        try {
            // Save files to the server
            if(img!=null){
                pack.setImg(this.saveFile(img,"img"));
                System.out.println("cv");
            }
            return ps.updatePack(pack);
        } catch (IOException e) {
            return null;
        }
    }
    public String saveFile(MultipartFile file, String prefix) throws IOException {
        String destinationDirectory="C:/xampp/htdocs/data/";
        String savedpath="http://localhost/data/";
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = generateUniqueFileName(prefix, extension);
        File destFile = new File(destinationDirectory + File.separator + newFileName);
        file.transferTo(destFile);
        return savedpath+newFileName;
    }

    private String generateUniqueFileName(String prefix, String extension) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedDateTime = now.format(formatter);
        return prefix + "_" + formattedDateTime + extension;
    }
}
