package tn.esprit.insurance.Models;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailVerificationRequest {

    private String email;
    private String verificationCode;

}
