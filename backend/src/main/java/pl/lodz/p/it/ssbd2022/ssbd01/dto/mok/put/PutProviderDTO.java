package pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class PutProviderDTO extends PutAccountDTO {

    @Size(min = 4, max = 100)
    @NotBlank
    private String address;

    @NotEmpty
    private String description;

    @NotEmpty
    private String logoUrl;
}
