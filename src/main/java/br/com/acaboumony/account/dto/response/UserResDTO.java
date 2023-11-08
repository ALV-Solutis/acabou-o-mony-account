package br.com.acaboumony.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResDTO {
    String name;
    String cpf;
    String contact;
    String email;
    }
