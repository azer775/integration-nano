package tn.esprit.insurance.services;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class PdfParsingService {
    public String extractSalaireBrut(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Recherche de la table contenant les informations sur le salaire brut
            String[] lines = text.split("\\r?\\n");
            boolean tableStart = false;
            StringBuilder salaireBrut = new StringBuilder();
            for (String line : lines) {
                if (line.contains("Code")) { // Adapter en fonction de la balise ou du texte entourant le tableau
                    tableStart = true;
                }
                if (tableStart && line.contains("Salaire brut")) {
                    salaireBrut.append(line).append("\n");
                    // Peut-être besoin de récupérer d'autres lignes du tableau pour obtenir les détails du salaire brut
                    break; // Vous pouvez ajuster ceci en fonction de la structure réelle de votre fiche de paie
                }

            }

            return salaireBrut.toString();
        }
    }
    public String extractSalaireBrut1(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Recherche de la ligne contenant le terme "Salaire brut"
            String[] lines = text.split("\\r?\\n");
            StringBuilder salaireBrut = new StringBuilder();
            for (String line : lines) {
                if (line.contains("Salaire brut")) {
                    salaireBrut.append(line).append("\n");
                }
            }

            return salaireBrut.toString();
        }
    }
    public String extractSalaireBrutFromPdf(String pdfFilePath) {
        try {
            File pdfFile = new File(pdfFilePath);
            PDDocument document = PDDocument.load(pdfFile);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String pdfText = pdfTextStripper.getText(document);

            // Recherche du libellé "Salaire Brut"
            if (pdfText.contains("Salaire brut")) {
                // Extraction de la valeur du salaire brut
                String brutValue = pdfText.split("Salaire brut")[1].trim();
                System.out.println(brutValue);
                document.close();
                return brutValue;
            } else {
                document.close();
                return "Libellé 'Salaire Brut' non trouvé dans le PDF.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la lecture du fichier PDF.";
        }
    }
    public String extractSalaireBrutLine(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Recherche de la ligne contenant le terme "salaire brut"
            String[] lines = text.split("\\r?\\n");
            for (String line : lines) {
                if (line.contains("salaire brut") || line.contains("Salaire brut")) {
                    return line;
                }
            }

            return "Non trouvé"; // Ou null, selon le cas d'utilisation
        }
    }

}
