package pl.lodz.p.it.ssbd2022.ssbd01.facade.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.DetailsFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.ServiceProviderDetails;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Fasada odpowiedzialna za obsługe operacji na bazie danych dotyczących encji ServiceProviderDetails
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class,
        DetailsFacadeInterceptor.class,
        LoggerInterceptor.class})
public class ServiceProviderDetailsFacade extends AbstractFacade<ServiceProviderDetails> {

    @PersistenceContext(unitName = "ssbd01mokPU")
    private EntityManager entityManager;

    public ServiceProviderDetailsFacade() {
        super(ServiceProviderDetails.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Metoda odpowiedzialna za znalezieie dostwacy usług w bazie danych o podanej nazwie
     *
     * @param serviceName - nazwa usługodawcy
     * @return encja ServiceProviderDetails
     */

    @PermitAll
    public ServiceProviderDetails findByServiceName(String serviceName) {
        TypedQuery<ServiceProviderDetails> typedQuery = entityManager.createNamedQuery("ServiceProviderDetails.findByServiceName", ServiceProviderDetails.class);
        typedQuery.setParameter("serviceName", serviceName);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda odpowiedzialna za znalezieie dostwacy usług w bazie danych o podanym loginie konta
     *
     * @param login - login konta
     * @return encja ServiceProviderDetails
     */

    @PermitAll
    public ServiceProviderDetails findByAccountLogin(String login) {
        TypedQuery<ServiceProviderDetails> typedQuery = entityManager.createNamedQuery("ServiceProviderDetails.findByAccountLogin", ServiceProviderDetails.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }
}
