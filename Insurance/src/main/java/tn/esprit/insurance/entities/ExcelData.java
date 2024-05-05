package tn.esprit.insurance.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExcelData {

    private float ceiling;
    private String conditions;
    private String description;
    private String event_ass;
    private String exclusions;
    private String name;
    @Enumerated(EnumType.STRING)
    private TypeAss typeAss;

}
