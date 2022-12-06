package pl.lodz.p.it.ssbd2022.ssbd01.facade.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.DetailsFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.RenterDetails;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Fasada odpowiedzialna za obsługe operacji na bazie danych dotyczących encji RenterDetails
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class,
        DetailsFacadeInterceptor.class,
        LoggerInterceptor.class})
public class RenterDetailsFacade extends AbstractFacade<RenterDetails> {

    @PersistenceContext(unitName = "ssbd01mokPU")
    private EntityManager entityManager;

    public RenterDetailsFacade() {
        super(RenterDetails.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda odpowiedzialna za znajdowanie informacji o wynajmującym po zadanym id konta
     *
     * @param accountId - id konta
     * @return Encja RenterDetails
     */

    @RolesAllowed("Renter")
    public RenterDetails findByAccountId(Long accountId) {
        TypedQuery<RenterDetails> typedQuery = entityManager.createNamedQuery("RenterDetails.findByAccountId", RenterDetails.class);
        typedQuery.setParameter("account_id", accountId);
        return typedQuery.getSingleResult();
    }
}
