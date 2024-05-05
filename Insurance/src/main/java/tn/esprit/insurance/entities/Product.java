package tn.esprit.insurance.entities;


import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.util.Timer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int id_prod;
     String name_prod;
     String description;
     float price;
     String image;
    @Enumerated (EnumType.STRING)
     Category category;
     String promotion;
     int quantity;


}
