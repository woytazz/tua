package pl.lodz.p.it.ssbd2022.ssbd01.dto.mo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OfferDateViewDTO {
    private String offerName;
    private int price;
    private LocalDate date;
    private String serviceProviderName;

    public OfferDateViewDTO(String offerName, int price, LocalDate date, String serviceProviderName) {
        this.offerName = offerName;
        this.price = price;
        this.date = date;
        this.serviceProviderName = serviceProviderName;
    }
}
