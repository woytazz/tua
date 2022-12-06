package pl.lodz.p.it.ssbd2022.ssbd01.mapper;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostAccountDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostAdminDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd01.model.AdminDetails;
import pl.lodz.p.it.ssbd2022.ssbd01.model.RenterDetails;
import pl.lodz.p.it.ssbd2022.ssbd01.model.ServiceProviderDetails;

/**
 * Mappers responsible for mapping dto into entities for POST calls
 */

public class PostMokMapper {

    /**
     * Method responsible for mapping incoming account dto into entity
     *
     * @param postAccountDTO - account post dto
     * @return account entity
     */

    public static Account convertToAccount(PostAccountDTO postAccountDTO) {
        return new Account(
                postAccountDTO.getLogin(),
                postAccountDTO.getPassword(),
                postAccountDTO.getName(),
                postAccountDTO.getSurname(),
                postAccountDTO.getEmail(),
                postAccountDTO.getPhoneNumber()
        );
    }

    /**
     * Method responsible for mapping admin details dto into entity
     *
     * @param postAdminDTO - admin details post dto
     * @return admin details entity
     */

    public static AdminDetails convertToAdminDetails(PostAdminDTO postAdminDTO) {
        return new AdminDetails();
    }

    /**
     * Method responsible for mapping renter details post dto into entity
     *
     * @param postRenterDTO - renter details post dto
     * @return - renter details entity
     */

    public static RenterDetails convertToRenterDetails(PostRenterDTO postRenterDTO) {
        return new RenterDetails(
                postRenterDTO.getUserName()
        );
    }

    /**
     * Method responsible for mapping service provider details post dto into entity
     *
     * @param postServiceProviderDTO - service provider post dto
     * @return - service provider details entity
     */

    public static ServiceProviderDetails convertToServiceProviderDetails(PostServiceProviderDTO postServiceProviderDTO) {
        return new ServiceProviderDetails(
                postServiceProviderDTO.getServiceName(),
                postServiceProviderDTO.getNip(),
                postServiceProviderDTO.getAddress(),
                postServiceProviderDTO.getDescription(),
                postServiceProviderDTO.getLogoUrl()
        );
    }
}
