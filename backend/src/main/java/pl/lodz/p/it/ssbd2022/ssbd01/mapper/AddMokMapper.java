package pl.lodz.p.it.ssbd2022.ssbd01.mapper;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.add.AddRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.add.AddServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.RenterDetails;
import pl.lodz.p.it.ssbd2022.ssbd01.model.ServiceProviderDetails;

public class AddMokMapper {

    /**
     * Method responsible for mapping accounts list
     *
     * @param addRenterDTO - DTO used to add access level
     * @return access level entity
     */

    public static RenterDetails convertToRenterDetails(AddRenterDTO addRenterDTO) {
        return new RenterDetails(
                addRenterDTO.getUserName()
        );
    }

    /**
     * Method responsible for mapping accounts list
     *
     * @param addServiceProviderDTO - DTO used to add access level
     * @return access level entity
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
