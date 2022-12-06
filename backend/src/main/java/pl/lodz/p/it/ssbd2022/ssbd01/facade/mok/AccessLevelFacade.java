package pl.lodz.p.it.ssbd2022.ssbd01.facade.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.AccessLevel;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Fasada odpoiwedzialna za obsługe operacji na bazie danych dotyczących encji AccessLevel
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class,
        LoggerInterceptor.class})
public class AccessLevelFacade extends AbstractFacade<AccessLevel> {

    @PersistenceContext(unitName = "ssbd01mokPU")
    private EntityManager entityManager;

    public AccessLevelFacade() {
        super(AccessLevel.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @RolesAllowed("ALL")
    @Override
    public void edit(AccessLevel entity) {
        super.edit(entity);
    }

    @RolesAllowed("Admin")
    @Override
    public void remove(AccessLevel entity) {
        super.remove(entity);
    }

}
