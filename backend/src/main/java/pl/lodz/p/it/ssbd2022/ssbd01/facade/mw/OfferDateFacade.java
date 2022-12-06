package pl.lodz.p.it.ssbd2022.ssbd01.facade.mw;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.OfferDate;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * Fasada odpowiedzialna za obsługe operacji na bazie danych dotyczących encji OfferDate
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class,
        LoggerInterceptor.class})
public class OfferDateFacade extends AbstractFacade<OfferDate> {

    @PersistenceContext(unitName = "ssbd01mwPU")
    private EntityManager entityManager;

    public OfferDateFacade() {
        super(OfferDate.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @RolesAllowed("Renter")
    @Override
    public void create(OfferDate offerDate) {
        super.create(offerDate);
    }

    /**
     * Metoda odpowiedzialna za znajdownie listy OfferDate z danym dniem oraz id
     *
     * @param date    - data
     * @param offerId - id oferty
     * @return - list encji OfferDate
     */

    @RolesAllowed("Renter")
    public List<OfferDate> findByDateAndOfferId(LocalDate date, Long offerId) {
        TypedQuery<OfferDate> typedQuery = entityManager.createNamedQuery("OfferDate.findByDateAndOfferId", OfferDate.class);
        typedQuery.setParameter("date", date);
        typedQuery.setParameter("offer_id", offerId);
        return typedQuery.getResultList();
    }

    /**
     * Metoda odpowieedzialna za znajdownie listy OfferDate o podanym id oferty
     *
     * @param id - id oferty
     * @return - list encji OfferDate
     */

    @PermitAll
    public List<OfferDate> findOfferDatesByOfferId(Long id) {
        TypedQuery<OfferDate> typedQuery = entityManager.createNamedQuery("OfferDate.findOfferDatesByOfferId", OfferDate.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getResultList();
    }

    @RolesAllowed("Renter")
    public List<OfferDate> findOfferDatesByUsername(String username) {
        TypedQuery<OfferDate> typedQuery = entityManager.createNamedQuery("OfferDate.findByUserOffer_Renter_UserName", OfferDate.class);
        typedQuery.setParameter("userName", username);
        return typedQuery.getResultList();
    }

}
