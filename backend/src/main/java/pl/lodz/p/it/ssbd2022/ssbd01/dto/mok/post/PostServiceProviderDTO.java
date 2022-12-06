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
public class PostServiceProviderDTO extends PostAccountDTO {

    @Size(min = 2, max = 32)
    @NotBlank
    private String serviceName;

    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{10}$")
    @NotBlank
    private String nip;

    @Size(min = 4, max = 100)
    @NotBlank
    private String address;

    private String description;

    private String logoUrl;
}
