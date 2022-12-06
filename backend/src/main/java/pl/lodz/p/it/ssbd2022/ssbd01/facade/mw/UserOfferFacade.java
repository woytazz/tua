package pl.lodz.p.it.ssbd2022.ssbd01.facade.mw;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.UserOffer;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Fasada odpowiedzialna za obsługe operacji na bazie danych dotyczących encji UserOffer
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class,
        LoggerInterceptor.class})
public class UserOfferFacade extends AbstractFacade<UserOffer> {

    @PersistenceContext(unitName = "ssbd01mwPU")
    private EntityManager entityManager;

    public UserOfferFacade() {
        super(UserOffer.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @RolesAllowed("Renter")
    @Override
    public void create(UserOffer userOffer) {
        super.create(userOffer);
    }
}
