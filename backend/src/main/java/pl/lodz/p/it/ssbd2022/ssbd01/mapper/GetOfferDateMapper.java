package pl.lodz.p.it.ssbd2022.ssbd01.mapper;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mo.OfferDateDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mo.OfferDateViewDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.OfferDate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetOfferDateMapper {

    public static OfferDateDTO getOfferDateDTO(OfferDate offerDate) {
        return new OfferDateDTO(
                offerDate.getId(),
                offerDate.getDate()
        );
    }

    public static List<OfferDateDTO> getOfferDateDTOList(List<OfferDate> offerDates) {
        return offerDates.stream().filter(Objects::nonNull)
                .map(GetOfferDateMapper::getOfferDateDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    public static OfferDateViewDTO getOfferDateViewDTO(OfferDate offerDate) {
        return new OfferDateViewDTO(
                offerDate.getOffer().getTitle(),
                offerDate.getOffer().getPrice(),
                offerDate.getDate(),
                offerDate.getOffer().getServiceProvider().getServiceName()
        );
    }

    public static List<OfferDateViewDTO> getOfferDateViewDTOList(List<OfferDate> offerDates) {
        return offerDates.stream().filter(Objects::nonNull)
                .map(GetOfferDateMapper::getOfferDateViewDTO)
                .collect(Collectors.toUnmodifiableList());
    }

}
