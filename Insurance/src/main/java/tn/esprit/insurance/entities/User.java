package tn.esprit.insurance.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails, Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   int Id;
   String firstname;
   String lastname;
   @JsonFormat(pattern = "yyyy-MM-dd")
   LocalDate DateOfBirth;
   @Column(unique = true)
   String email;
   String password;
   String adress;
   String region;
   @Enumerated(EnumType.STRING)
   Role role;
   @Enumerated(EnumType.STRING)
   StatusU status;
   String work;
   String verificationCode;

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "profil_id", referencedColumnName = "IdP")
   private Profil profil;

   public User(int id) {
      Id = id;
   }

   @OneToMany(mappedBy = "user")
   private List<Token> tokens;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return role.getAuthorities();
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
}
