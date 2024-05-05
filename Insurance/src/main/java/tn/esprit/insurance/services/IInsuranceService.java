package tn.esprit.insurance.services;

import tn.esprit.insurance.entities.ExcelData;
import tn.esprit.insurance.entities.Insurance;

import java.util.List;

public interface IInsuranceService {

    Insurance addInsurance(Insurance ins);

    Insurance updateInsurance(Insurance insurance);
    void deleteInsurance(int id_ass);
    List<Insurance> getAll();
    Insurance getInsuranceById(int id_ass);


    public void saveInsFromExcel(List<ExcelData> excelDataList) ;

}
