package pl.lodz.p.it.ssbd2022.ssbd01.mapper;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Offer;

public class OfferMapper {

    public static Offer convertToOffer(OfferDTO dto) {
        return new Offer(
                dto.getTitle(),
                dto.getDescription(),
                dto.getPrice());
    }

}
