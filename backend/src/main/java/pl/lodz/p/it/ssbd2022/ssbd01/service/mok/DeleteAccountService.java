package pl.lodz.p.it.ssbd2022.ssbd01.service.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd01.service.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.DeleteAccountServiceInterface;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({
        GenericServiceInterceptor.class,
        LoggerInterceptor.class})
public class DeleteAccountService extends AbstractService implements DeleteAccountServiceInterface, SessionSynchronization {

    @Inject
    private AccountFacade accountFacade;

    @RolesAllowed("Admin")
    @Override
    public void deleteServiceProvider(String login) {
        Account account = accountFacade.findByLogin(login);
        accountFacade.remove(account);
    }
}
