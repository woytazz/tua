package pl.lodz.p.it.ssbd2022.ssbd01.dto.mz;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbProperty;

@Data
@NoArgsConstructor
public class OfferViewDTO {

    private Long id;

    private String title;

    private String description;

    private int price;

    private boolean active;

    private String serviceProviderName;


    @JsonbProperty("etag")
    private String ETag;

    public OfferViewDTO(Long id, String title, String description, int price, boolean active, String serviceProviderName, String ETag) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.active = active;
        this.serviceProviderName = serviceProviderName;
        this.ETag = ETag;
    }
}

