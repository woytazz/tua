package pl.lodz.p.it.ssbd2022.ssbd01.service.mz.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Offer;
import pl.lodz.p.it.ssbd2022.ssbd01.service.TransactionInterface;

import javax.ejb.Local;

/**
 * Serwis odpowiedzialny za zarządzanie ofertami
 */

@Local
public interface OfferServiceInterface extends TransactionInterface {

    /**
     * Metoda odpowiedzialna za dodanie nowej oferty do dostawcy usług
     *
     * @param login - login dostawcy usług
     * @param offer - encja oferty
     */

    void addOffer(String login, Offer offer);

    /**
     * Metoda odpowiedzialna za usuwanie oferty dostawcy usług
     *
     * @param id - id oferty
     */
    void deleteOffer(Long id);

    /**
     * Metoda odpowiedzialna za usuwanie własnej oferty
     *
     * @param login login usługodawcy
     * @param id    id oferty
     */
    void deleteOwnOffer(String login, Long id);

    /**
     * Metoda odpowiedzialna za edycję swojej oferty
     *
     * @param login    - login usługowacy
     * @param offerDTO - dane o ofercie
     * @param id       - id oferty
     * @param ETag     - etag
     */
    void editOffer(String login, OfferDTO offerDTO, Long id, String ETag);

    /**
     * Metoda odpowiedzialna za edycję oferty przez administratora
     *
     * @param offerDTO - dane o ofercie
     * @param id       - id oferty
     * @param ETag     - etag
     */
    void editOfferAdmin(OfferDTO offerDTO, Long id, String ETag);
}
