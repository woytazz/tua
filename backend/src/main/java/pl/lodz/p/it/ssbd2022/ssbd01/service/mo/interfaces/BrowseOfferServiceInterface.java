package pl.lodz.p.it.ssbd2022.ssbd01.service.mo.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.model.Offer;

import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd01.model.OfferDate;

import javax.ejb.Local;
import java.util.List;

/**
 * Serwis odpowiedzialny za przeglądanie ofert i dostawców usług
 */

@Local
public interface BrowseOfferServiceInterface {

    /**
     * Metoda odpowiedzialna za pobieranie wszystkich ofert
     *
     * @return lista encji Offer
     */

    List<Offer> getAllOffers();

    /**
     * Metoda odpowiedzialna za pobieranie aktywnych ofert
     *
     * @return lista encji Offer
     */

    List<Offer> getActiveOffers();

    /**
     * Metoda odpowiedzialna za pobieranie ofert danego usługowacy
     *
     * @param login - login usługowacy
     * @return lista encji Offer
     */

    List<Offer> getAllServiceProviderOffers(String login);

    /**
     * Metoda odpowiedzialna za pobieranie ofert o danym id
     *
     * @param id - id oferty
     * @return encja Offer
     */

    Offer getOfferById(Long id);

    /**
     * Metoda odpowiedzialna za pobieranie wszystkich odstawców usług
     *
     * @return lista encji Account
     */

    List<Account> getAllServiceProviders();

    /**
     * Metoda odpowiedzialna za filtrowanie ofert po cenie
     *
     * @param minPrice minimalna cena oferty
     * @param maxPrice maksymalna cena oferty
     */

    List<Offer> getOffersFilteredByPrice(int minPrice, int maxPrice);

    /**
     * Metoda odpowiedzialna za czytanie listy zarezerwowanych dat dla danej oferty
     *
     * @param id - id oferty
     * @return - lista offerDate
     */

    List<OfferDate> getOfferDatesByOfferId(Long id);
}
