package pl.lodz.p.it.ssbd2022.ssbd01.security;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.AuthUserNotFound;
import pl.lodz.p.it.ssbd2022.ssbd01.service.auth.interfaces.AuthServiceInterface;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Set;

@ApplicationScoped
public class DatabaseIdentityStore implements IdentityStore {

    @Inject
    private AuthServiceInterface authService;

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return IdentityStore.super.getCallerGroups(validationResult);
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;

            try {
                if (authService.authenticateUsernamePassword(usernamePasswordCredential.getCaller(), usernamePasswordCredential.getPasswordAsString())) {
                    return new CredentialValidationResult(usernamePasswordCredential.getCaller(), authService.getCallerGroups(usernamePasswordCredential.getCaller()));
                } else {
                    return CredentialValidationResult.INVALID_RESULT;
                }
            } catch (AuthUserNotFound exception) {
                return CredentialValidationResult.NOT_VALIDATED_RESULT;
            }
        }
        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }
}
