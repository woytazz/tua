package pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class PostRenterDTO extends PostAccountDTO {

    @Size(min = 2, max = 64)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9.,$;]+$")
    @NotBlank
    private String userName;
}
