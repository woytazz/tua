package pl.lodz.p.it.ssbd2022.ssbd01.service.auth;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.auth.TokenDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.AuthorizationException;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.auth.AccountAuthFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;
import pl.lodz.p.it.ssbd2022.ssbd01.service.auth.interfaces.AuthServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.util.hash.HashGenerator;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({
        GenericServiceInterceptor.class,
        LoggerInterceptor.class})
public class AuthService implements AuthServiceInterface {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());

    @Inject
    private HashGenerator hashGenerator;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private AccountAuthFacade accountAuthFacade;

    @Inject
    private HttpServletRequest request;


    @Override
    @PermitAll
    public TokenDTO generateTokens(String login, String password) {
        Credential credential = new UsernamePasswordCredential(login, new Password(password));
        CredentialValidationResult result = identityStoreHandler.validate(credential);

        if (result.getStatus() == CredentialValidationResult.Status.VALID) {

            if (request != null) {
                String remoteAddr = request.getHeader("X-FORWARDED-FOR");
                if (remoteAddr == null || "".equals(remoteAddr)) {
                    LOGGER.info("User logged in from ip: " + request.getRemoteAddr());
                }
            }

            return new TokenDTO(
                    JWTGeneratorVerifier.generateAccessJWTString(result.getCallerPrincipal().getName(), result.getCallerGroups()),
                    JWTGeneratorVerifier.generateRefreshJWTString(result.getCallerPrincipal().getName())
            );
        } else {
            throw new AuthorizationException();
        }
    }


    @Override
    @PermitAll
    public TokenDTO refreshTokens(String refreshToken) {
        if (JWTGeneratorVerifier.validateJWTSignature(refreshToken)) {
            String username = JWTGeneratorVerifier.decodeJWT(refreshToken).getSubject();
            return new TokenDTO(
                    JWTGeneratorVerifier.generateAccessJWTString(username, this.getCallerGroups(username)),
                    JWTGeneratorVerifier.generateRefreshJWTString(username)
            );
        } else {
            throw new AuthorizationException();
        }
    }

    @Override
    @PermitAll
    public boolean authenticateUsernamePassword(String username, String password) {
        Account account = accountAuthFacade.findByLoginActiveConfirmed(username);
        String passHash = hashGenerator.generateHash(password);

        return passHash.equals(account.getPassword());
    }


    @Override
    @PermitAll
    public Set<String> getCallerGroups(String username) {
        Set<String> groups = new HashSet<>();
        Account account = accountAuthFacade.findByLoginActiveConfirmed(username);

        for (AccessLevel accessLevel : account.getAccessLevelCollection()) {
            groups.add(accessLevel.getAccessLevel());
        }

        return groups;
    }
}
