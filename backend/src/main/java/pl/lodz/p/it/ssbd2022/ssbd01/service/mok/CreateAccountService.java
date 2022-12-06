package pl.lodz.p.it.ssbd2022.ssbd01.service.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.VerificationTokenFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.*;
import pl.lodz.p.it.ssbd2022.ssbd01.service.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.CreateAccountServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.util.hash.HashGenerator;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.LocalDateTime;
import java.util.UUID;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({
        GenericServiceInterceptor.class,
        LoggerInterceptor.class})
public class CreateAccountService extends AbstractService implements CreateAccountServiceInterface, SessionSynchronization {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private HashGenerator hashGenerator;

    @Inject
    private VerificationTokenFacade verificationTokenFacade;


    @RolesAllowed("Admin")
    @Override
    public void createAdminAccount(Account account, AdminDetails accessLevel) {
        account.setPassword(hashGenerator.generateHash(account.getPassword()));
        accessLevel.setAccount(account);
        account.getAccessLevelCollection().add(accessLevel);
        account.setConfirmed(true);
        accountFacade.create(account);
    }

    @PermitAll
    @Override
    public void createServiceProviderAccount(Account account, ServiceProviderDetails accessLevel) {
        account.setPassword(hashGenerator.generateHash(account.getPassword()));
        accessLevel.setAccount(account);
        Rate rate = new Rate();
        rate.setServiceProvider(accessLevel);
        accessLevel.setRate(rate);
        account.getAccessLevelCollection().add(accessLevel);
        accountFacade.create(account);
    }

    @PermitAll
    @Override
    public void createRenterAccountAndSendVerificationEmail(Account account, RenterDetails accessLevel) {
        // create account
        account.setPassword(hashGenerator.generateHash(account.getPassword()));
        accessLevel.setAccount(account);
        account.getAccessLevelCollection().add(accessLevel);
        accountFacade.create(account);

        //create verification token
        VerificationToken tokenObject = new VerificationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                account
        );
        verificationTokenFacade.create(tokenObject);
    }


    @PermitAll
    @Override
    public void confirmToken(String token) {

        VerificationToken verificationToken;
        verificationToken = verificationTokenFacade.findByToken(token);

        Account account = verificationToken.getAccount();
        account.setConfirmed(true);
        accountFacade.edit(account);
        verificationTokenFacade.remove(verificationToken);
    }
}
