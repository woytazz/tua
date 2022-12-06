package pl.lodz.p.it.ssbd2022.ssbd01.facade.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.VerificationTokenFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.VerificationToken;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Fasada odpowiedzialna za obsługe operacji na bazie danych dotyczących encji VerificationToken
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericFacadeInterceptor.class,
        VerificationTokenFacadeInterceptor.class,
        LoggerInterceptor.class})
public class VerificationTokenFacade extends AbstractFacade<VerificationToken> {

    @PersistenceContext(unitName = "ssbd01mokPU")
    private EntityManager entityManager;

    public VerificationTokenFacade() {
        super(VerificationToken.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @PermitAll
    @Override
    public void create(VerificationToken entity) {
        super.create(entity);
    }

    @PermitAll
    @Override
    public void remove(VerificationToken entity) {
        super.remove(entity);
    }

    /**
     * Metoda odpowiedzialna za znajodowanie encji VerificationToken po zadanym tokenie
     *
     * @param token - token
     * @return Encja VerificationToken
     */

    @PermitAll
    public VerificationToken findByToken(String token) {
        TypedQuery<VerificationToken> typedQuery = entityManager.createNamedQuery("VerificationToken.findByToken", VerificationToken.class);
        typedQuery.setParameter("token", token);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda odpowiedzialna za znajodowanie encji VerificationToken po zadanym emailu
     *
     * @param email - email
     * @return Encja VerificationToken
     */

    @PermitAll
    public VerificationToken findByAccountEmail(String email) {
        TypedQuery<VerificationToken> typedQuery = entityManager.createNamedQuery("VerificationToken.findByAccount_Email", VerificationToken.class);
        typedQuery.setParameter("email", email);
        return typedQuery.getSingleResult();
    }

    /**
     * Metoda odpowiedzialna za znajdowanie listy przedawnionych tokenów
     *
     * @return lista encji VerificationToken
     */

    @PermitAll
    public List<VerificationToken> getExpiredTokens() {
        TypedQuery<VerificationToken> typedQuery = entityManager.createNamedQuery("VerificationToken.getExpiredTokens", VerificationToken.class);
        typedQuery.setParameter("dateNow", LocalDateTime.now());
        return typedQuery.getResultList();
    }
}
