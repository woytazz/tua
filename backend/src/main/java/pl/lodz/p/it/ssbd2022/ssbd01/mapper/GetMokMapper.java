package pl.lodz.p.it.ssbd2022.ssbd01.mapper;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.get.*;
import pl.lodz.p.it.ssbd2022.ssbd01.model.*;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mappers responsible for mapping entities into dto for GET calls
 */

public class GetMokMapper {

    /**
     * Method responsible for mapping accounts list
     *
     * @param accounts - list of account entities
     * @return list of mapped account dto
     */

    public static List<AccountDTO> getAccountDTOList(List<Account> accounts) {
        return accounts.stream()
                .filter(Objects::nonNull)
                .map(GetMokMapper::getAccountDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Method responsible for mapping account entity
     *
     * @param account - account entity
     * @return account dto
     */
    public static AccountDTO getAccountDTO(Account account) {
        return new AccountDTO(
                account.getLogin(),
                account.isActive(),
                account.isConfirmed(),
                account.getName(),
                account.getSurname(),
                account.getEmail(),
                account.getPhoneNumber(),
                getAccessLevelDTOs(account.getAccessLevelCollection()),
                HMAC.calculateHMAC(account.getLogin(), account.getVersion())
        );
    }

    /**
     * Method responsible for mapping access levels list
     *
     * @param collection - collection of access levels entities
     * @return collection of access level dtos
     */

    private static Collection<AccessLevelDTO> getAccessLevelDTOs(Collection<AccessLevel> collection) {
        return collection
                .stream()
                .filter(Objects::nonNull)
                .map(GetMokMapper::getAccessLevelDTO)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Method responsible for mapping single access level
     *
     * @param accessLevel - entity of access level
     * @return access level dto
     */

    private static AccessLevelDTO getAccessLevelDTO(AccessLevel accessLevel) {
        if (accessLevel instanceof AdminDetails) {
            return getAdminDTO((AdminDetails) accessLevel);
        } else if (accessLevel instanceof RenterDetails) {
            return getRenterDTO((RenterDetails) accessLevel);
        } else if (accessLevel instanceof ServiceProviderDetails) {
            return getServiceProviderDTO((ServiceProviderDetails) accessLevel);
        } else {
            return null;
        }
    }

    /**
     * Method responsible for mapping admin details
     *
     * @param r - admin details entity
     * @return admin details dto
     */

    private static AdminDTO getAdminDTO(AdminDetails r) {
        return new AdminDTO(
                r.getAccessLevel(),
                r.isActive(),
                HMAC.calculateHMAC(r.getAccount().getLogin(), r.getVersion())
        );
    }

    /**
     * Method responsible for mapping renter details
     *
     * @param r - renter details entity
     * @return renter details dto
     */

    private static RenterDTO getRenterDTO(RenterDetails r) {
        return new RenterDTO(
                r.getAccessLevel(),
                r.isActive(),
                r.getUserName(),
                HMAC.calculateHMAC(r.getAccount().getLogin(), r.getVersion())
        );
    }

    /**
     * Method responsible for mapping service provider details
     *
     * @param r - service providers entity
     * @return service provider dto
     */

    public static ServiceProviderDTO getServiceProviderDTO(ServiceProviderDetails r) {
        return new ServiceProviderDTO(
                r.getAccessLevel(),
                r.isActive(),
                r.getServiceName(),
                r.getNip(),
                r.getAddress(),
                r.getDescription(),
                r.getLogoUrl(),
                r.getRate().getAverageRate(),
                HMAC.calculateHMAC(r.getAccount().getLogin(), r.getVersion())
        );
    }


}
