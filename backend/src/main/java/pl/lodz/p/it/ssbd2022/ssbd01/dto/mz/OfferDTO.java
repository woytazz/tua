package pl.lodz.p.it.ssbd2022.ssbd01.dto.mz;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
public class OfferDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @PositiveOrZero
    private int price;
}
