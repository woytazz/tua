package pl.lodz.p.it.ssbd2022.ssbd01.facade.mw;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.RateFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Rate;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Fasada odpowiedzialna za obsługe operacji na bazie danych dotyczących encji Rate
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class, RateFacadeInterceptor.class,
        LoggerInterceptor.class})
public class RateFacade extends AbstractFacade<Rate> {

    @PersistenceContext(unitName = "ssbd01mwPU")
    private EntityManager entityManager;

    public RateFacade() {
        super(Rate.class);
    }

    @RolesAllowed("Renter")
    @Override
    public void edit(Rate entity) {
        super.edit(entity);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda odpowiedzialna za znajdowanie oceny dla danego usługodawcy
     *
     * @param serviceName - nazwa usługodawcy
     * @return encja Rate
     */

    @RolesAllowed("Renter")
    public Rate findByServiceName(String serviceName) {
        TypedQuery<Rate> typedQuery = entityManager.createNamedQuery("Rate.findByServiceName", Rate.class);
        typedQuery.setParameter("service", serviceName);
        return typedQuery.getSingleResult();
    }
}
