package br.com.acaboumony.account.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReqDTO {
    @NotBlank String name;
    @NotBlank @Size(min = 11, max = 11) String cpf;
    @NotBlank String password;
    @NotBlank String contact;
    @NotBlank @Email String email;
}
