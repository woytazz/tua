package pl.lodz.p.it.ssbd2022.ssbd01.service.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.ServiceProviderDetailsFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd01.service.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.ReadAccountServiceInterface;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({
        GenericServiceInterceptor.class,
        LoggerInterceptor.class})
public class ReadAccountService extends AbstractService implements ReadAccountServiceInterface, SessionSynchronization {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private ServiceProviderDetailsFacade serviceProviderDetailsFacade;

    @RolesAllowed("ALL")
    @Override
    public Account readAccountByLogin(String login) {
        return accountFacade.findByLogin(login);
    }

    @RolesAllowed("Admin")
    @Override
    public List<Account> readAllAccounts() {
        return accountFacade.findAll();
    }

    @RolesAllowed("Admin")
    @Override
    public List<Account> readProvidersToConfirm() {
        return accountFacade.findNotConfirmedServiceProviders();
    }

    @Override
    @PermitAll
    public Account readServiceProviderByName(String serviceName) {
        return serviceProviderDetailsFacade.findByServiceName(serviceName).getAccount();
    }

}
