package pl.lodz.p.it.ssbd2022.ssbd01.facade.auth;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.AccountAuthFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class,
        AccountAuthFacadeInterceptor.class,
        LoggerInterceptor.class})
public class AccountAuthFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd01authPU")
    private EntityManager entityManager;

    public AccountAuthFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @PermitAll
    public Account findByLoginActiveConfirmed(String login) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByLoginActiveConfirmed", Account.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }
}
