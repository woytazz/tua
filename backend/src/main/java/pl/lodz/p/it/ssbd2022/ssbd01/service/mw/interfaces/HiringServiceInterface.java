package pl.lodz.p.it.ssbd2022.ssbd01.service.mw.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mo.OfferDateDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.OfferDate;
import pl.lodz.p.it.ssbd2022.ssbd01.service.TransactionInterface;

import javax.ejb.Local;
import java.util.List;

/**
 * Serwis odpowiedzialny za wynajmowanie usług
 */

@Local
public interface HiringServiceInterface extends TransactionInterface {

    /**
     * Metoda odpowiedzialna za wynajęcie usługi
     *
     * @param login - login osobyh która wynajmuje
     * @param dto - DTO z danymi o wynajmowanej ofercie
     */

    void hireOffer(String login, OfferDateDTO dto, String ETag);
    List<OfferDate> getUsersOffers(String username);

}
