package tn.esprit.insurance.entities;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

public class ExcelReader {

    public List<ExcelData> readExcelFile(MultipartFile multipartFile) throws IOException, ParseException {
        List<ExcelData> dataList = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

        for (Row row : sheet) {


            Float ceiling = null;
            Cell ceilingCell = row.getCell(0);
            if (ceilingCell != null) {
                ceiling = (float) ceilingCell.getNumericCellValue();
            }

            String conditions = null;
            Cell conditionsCell = row.getCell(1);
            if (conditionsCell != null) {
                conditions = conditionsCell.getStringCellValue();
            }

            String description = null;
            Cell descriptionCell = row.getCell(2);
            if (descriptionCell != null) {
                description = descriptionCell.getStringCellValue();
            }

            String eventAss = null;
            Cell eventAssCell = row.getCell(3);
            if (eventAssCell != null) {
                eventAss = eventAssCell.getStringCellValue();
            }

            String exclusions = null;
            Cell exclusionsCell = row.getCell(4);
            if (exclusionsCell != null) {
                exclusions = exclusionsCell.getStringCellValue();
            }

            String name = null;
            Cell nameCell = row.getCell(5);
            if (nameCell != null) {
                name = nameCell.getStringCellValue();
            }

            TypeAss typeAss = null;
            Cell AssypeCell = row.getCell(6);
            if (AssypeCell != null) {
                String assTypeStr = AssypeCell.getStringCellValue();
                try {
                    typeAss = TypeAss.valueOf(assTypeStr);
                } catch (IllegalArgumentException e) {
                    // Handle invalid enum value
                    System.err.println("Invalid Insurance type: " + assTypeStr);
                }
            }

            ExcelData data = new ExcelData();
            data.setCeiling(ceiling);
            data.setConditions(conditions);
            data.setDescription(description);
            data.setEvent_ass(eventAss);
            data.setExclusions(exclusions);
            data.setName(name);
            data.setTypeAss(typeAss);

            dataList.add(data);
        }


        workbook.close();
        return dataList;
    }
}