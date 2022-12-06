package pl.lodz.p.it.ssbd2022.ssbd01.dto.mo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class OfferDateDTO {
    private Long id;
    private LocalDate date;

    public OfferDateDTO(Long id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

}
