package pl.lodz.p.it.ssbd2022.ssbd01.mapper;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.add.AddServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.RenterDetails;
import pl.lodz.p.it.ssbd2022.ssbd01.model.ServiceProviderDetails;

/**
 * Mappers responsible for mapping dto into entities for PUT calls
 */

public class PutMokMapper {

    /**
     * Method responsible for mapping renter details put dto into entity
     *
     * @param putRenterDTO - put renter details dto
     * @return renter details entity
     */

    public static RenterDetails convertToRenterDetails(PutRenterDTO putRenterDTO) {
        return new RenterDetails(
                putRenterDTO.getUserName()
        );
    }

    /**
     * Method responsible for mapping service providers details put dto into entity
     *
     * @param addServiceProviderDTO - put service provider details dto
     * @return service provider details entity
     */

    public static ServiceProviderDetails convertToServiceProviderDetails(AddServiceProviderDTO addServiceProviderDTO) {
        return new ServiceProviderDetails(
                addServiceProviderDTO.getServiceName(),
                addServiceProviderDTO.getNip(),
                addServiceProviderDTO.getAddress(),
                addServiceProviderDTO.getDescription(),
                addServiceProviderDTO.getLogoUrl()
        );
    }
}
