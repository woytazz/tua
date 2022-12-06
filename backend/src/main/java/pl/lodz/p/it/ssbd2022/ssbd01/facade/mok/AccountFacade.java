package pl.lodz.p.it.ssbd2022.ssbd01.facade.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.AccountFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;

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
 * Fasada odpowiedzialna za obsługe operacji na bazie danych dotyczących encji Account
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class,
        AccountFacadeInterceptor.class,
        LoggerInterceptor.class})
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd01mokPU")
    private EntityManager entityManager;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @PermitAll
    @Override
    public void create(Account account) {
        super.create(account);
    }

    @PermitAll
    @Override
    public void edit(Account entity) {
        super.edit(entity);
    }

    @PermitAll
    @Override
    public void remove(Account entity) {
        super.remove(entity);
    }

    /**
     * Method odpowiedzialna za znajdowanie konta po zadanym loginie w bazie danych
     *
     * @param login - login użytkownika
     * @return encja Account
     */

    @PermitAll
    public Account findByLogin(String login) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByLogin", Account.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda odpowiedzialna za znajowanie wszystkich kont w bazie danych
     *
     * @return lista kont
     */

    @RolesAllowed("Admin")
    @Override
    public List<Account> findAll() {
        return super.findAll();
    }

    /**
     * Metoda odpowiedzialna za znajowanie konta według danego emial'a w bazie danych
     *
     * @param email - email użytkownika
     * @return encja Account
     */

    @PermitAll
    public Account findByEmail(String email) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByEmail", Account.class);
        typedQuery.setParameter("email", email);
        return typedQuery.getSingleResult();
    }

    /**
     * Method odpowiedzialna za znajdowanie konta po zadanym loginie oraz poziomie dostepu w bazie danych
     *
     * @param login       - login użytkownika
     * @param accessLevel - poziom dostepu
     * @return encja Account
     */

    @RolesAllowed("Admin")
    public Account findByLoginAccessLevel(String login, String accessLevel) {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByLoginAndAccessLevel", Account.class);
        typedQuery.setParameter("login", login);
        typedQuery.setParameter("accessLevel", accessLevel);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda odpowiedzialna za odczytywanie z bazy danych niepotwierdzonych usługodawców
     *
     * @return list kont
     */

    @RolesAllowed("Admin")
    public List<Account> findNotConfirmedServiceProviders() {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findNotConfirmedServiceProviders", Account.class);
        typedQuery.setParameter("accessLevel", "ServiceProvider");
        return typedQuery.getResultList();
    }

    /**
     * Metoda odpowiedzialna za odczytywanie z bazy danych wszystkich usługodawców
     *
     * @return list kont
     */

    @PermitAll
    public List<Account> getAllServiceProviders() {
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByServiceProvidersActiveAndConfirmed", Account.class);
        typedQuery.setParameter("accessLevel", "ServiceProvider");
        return typedQuery.getResultList();
    }
}
