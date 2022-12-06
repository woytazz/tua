package pl.lodz.p.it.ssbd2022.ssbd01.facade.mz;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.OfferFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Offer;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Fasada odpowiedzialna za obsługe operacji na bazie danych dotyczących encji Offer
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class, OfferFacadeInterceptor.class,
        LoggerInterceptor.class})
public class OfferFacade extends AbstractFacade<Offer> {

    @PersistenceContext(unitName = "ssbd01mzPU")
    private EntityManager entityManager;

    public OfferFacade() {
        super(Offer.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @PermitAll
    public Offer find(Object id) {
        return super.find(id);
    }

    @RolesAllowed("ServiceProvider")
    @Override
    public void create(Offer entity) {
        super.create(entity);
    }

    @RolesAllowed("AdminService")
    @Override
    public void edit(Offer entity) {
        super.edit(entity);
    }

    /**
     * Metoda odpowiedzialna za odnalezienie wszystkich ofert w bazie danych
     *
     * @return offer entity
     */
    @PermitAll
    @Override
    public List<Offer> findAll() {
        return super.findAll();
    }


    @PermitAll
    public Offer getById(Long id) {
        TypedQuery<Offer> typedQuery = entityManager.createNamedQuery("Offer.findById", Offer.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda odpowiedzialna za odnalezienie oferty o najwyższej cenie
     *
     * @return lista encji Offer
     */

    @PermitAll
    public List<Offer> findOfferWithHighestPrice() {
        TypedQuery<Offer> typedQuery = entityManager.createNamedQuery("Offer.findHighestPriceOffer", Offer.class);
        return typedQuery.getResultList();
    }

    /**
     * Metoda odpowiedzialna za znajdowanie oferty o zadanej cenie minimalnej i maksymalnej
     *
     * @param minPrice minimalna cena
     * @param maxPrice maksymalna cena
     * @return lista ofert
     */

    @PermitAll
    public List<Offer> findOffersFilteredByPrice(int minPrice, int maxPrice) {
        TypedQuery<Offer> typedQuery = entityManager.createNamedQuery("Offer.findByPriceGreaterThanEqualAndPriceLessThanEqual", Offer.class);
        typedQuery.setParameter("price1", minPrice);
        typedQuery.setParameter("price2", maxPrice);
        return typedQuery.getResultList();
    }

    /**
     * Metoda odpowiedzialna za odczytywanie wszystkich aktywnych ofert
     *
     * @return lista ofert
     */

    @PermitAll
    public List<Offer> getActiveOffers() {
        TypedQuery<Offer> typedQuery = entityManager.createNamedQuery("Offer.findByActiveTrue", Offer.class);
        return typedQuery.getResultList();
    }

    /**
     * Metoda odpowiedzialna za odczytywanie oferty o podanym id
     *
     * @param id - id oferty
     * @return Encja Offer
     */

    @PermitAll
    public Offer getOfferById(Long id) {
        TypedQuery<Offer> typedQuery = entityManager.createNamedQuery("Offer.findById", Offer.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda odpowiedzialna za odczytywanie wszystkich ofert danego usługodawcy
     *
     * @param login - login usługodawcy
     * @return lista encji Offer
     */

    @RolesAllowed("ServiceProvider")
    public List<Offer> getOfferByServiceProvider(String login) {
        TypedQuery<Offer> typedQuery = entityManager.createNamedQuery("Offer.findByServiceProvider_Account_Login", Offer.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getResultList();
    }
}
