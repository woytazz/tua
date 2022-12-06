package pl.lodz.p.it.ssbd2022.ssbd01.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
