package pl.lodz.p.it.ssbd2022.ssbd01.mapper;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferViewDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Offer;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper odpowiedzialny za mapowanie encji na odpowiednie DTO dla żądań GET
 */

public class GetMoMapper {

    public static OfferViewDTO getOfferDTO(Offer offer) {
        return new OfferViewDTO(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getPrice(),
                offer.isActive(),
                offer.getServiceProvider().getServiceName(),
                HMAC.calculateHMAC(offer.getId(), offer.getVersion())
        );
    }

    public static List<OfferViewDTO> getOfferDTOList(Collection<Offer> offers) {
        return offers.stream().filter(Objects::nonNull)
                .map(GetMoMapper::getOfferDTO)
                .collect(Collectors.toUnmodifiableList());
    }

}
