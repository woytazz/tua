package pl.lodz.p.it.ssbd2022.ssbd01.service.mw;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.InvalidServiceException;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.RateOutOfRangeException;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ServiceAlreadyRatedException;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.RenterDetailsFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mw.RateFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericFacadeInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Rate;
import pl.lodz.p.it.ssbd2022.ssbd01.model.RenterDetails;
import pl.lodz.p.it.ssbd2022.ssbd01.model.UserOffer;
import pl.lodz.p.it.ssbd2022.ssbd01.service.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mw.interfaces.RateServiceInterface;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Collection;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericFacadeInterceptor.class,
        LoggerInterceptor.class})
public class RateService extends AbstractService implements RateServiceInterface, SessionSynchronization {

    @Inject
    private RateFacade rateFacade;

    @Inject
    private RenterDetailsFacade renterDetailsFacade;

    @Inject
    private AccountFacade accountFacade;

    @RolesAllowed("Renter")
    @Override
    public void rate(String login, int rateNumber, String serviceName) {

        if (rateNumber < 1 || rateNumber > 5) {
            throw new RateOutOfRangeException();
        }

        Rate rate = rateFacade.findByServiceName(serviceName);
        Account account = accountFacade.findByLogin(login);

        Collection<RenterDetails> renters = rate.getRenters();

        RenterDetails renter = renterDetailsFacade.findByAccountId(account.getId());

        Collection<UserOffer> offers = renter.getUserOfferCollection();

        boolean tmp = false;

        for (UserOffer offer : offers) {
            if (offer.getOfferDate().getOffer().getServiceProvider().getServiceName().equals(serviceName)
                    && !offer.getOfferDate().getDate().isAfter(ChronoLocalDate.from(LocalDateTime.now()))) {
                tmp = true;
                break;
            }
        }

        if (!tmp) {
            throw new InvalidServiceException();
        }

        for (RenterDetails it : renters) {
            if (it.getId().equals(renter.getId())) {
                throw new ServiceAlreadyRatedException();
            }
        }

        renters.add(renter);
        float newAverage = ((rate.getAverageRate() * rate.getRatesNumber()) + rateNumber) / (rate.getRatesNumber() + 1);

        rate.setAverageRate(newAverage);
        rate.setRatesNumber(rate.getRatesNumber() + 1);
        rate.setRenters(renters);
        rateFacade.edit(rate);
    }
}
