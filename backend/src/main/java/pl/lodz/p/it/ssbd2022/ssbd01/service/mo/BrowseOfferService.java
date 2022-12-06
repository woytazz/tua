package pl.lodz.p.it.ssbd2022.ssbd01.service.mo;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mw.OfferDateFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mz.OfferFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Offer;
import pl.lodz.p.it.ssbd2022.ssbd01.model.OfferDate;
import pl.lodz.p.it.ssbd2022.ssbd01.service.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mo.interfaces.BrowseOfferServiceInterface;

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
@Interceptors(GenericServiceInterceptor.class)
public class BrowseOfferService extends AbstractService implements BrowseOfferServiceInterface, SessionSynchronization {

    @Inject
    private OfferFacade offerFacade;

    @Inject
    private OfferDateFacade offerDateFacade;

    @Inject
    private AccountFacade accountFacade;

    @PermitAll
    @Override
    public List<Offer> getAllOffers() {
        return offerFacade.findAll();
    }

    @Override
    @PermitAll
    public List<Offer> getActiveOffers() {
        return offerFacade.getActiveOffers();
    }

    @Override
    @RolesAllowed("ServiceProvider")
    public List<Offer> getAllServiceProviderOffers(String login) {
        return offerFacade.getOfferByServiceProvider(login);
    }

    @Override
    @PermitAll
    public Offer getOfferById(Long id) {
        return offerFacade.getOfferById(id);
    }

    @PermitAll
    public List<Account> getAllServiceProviders() {
        return accountFacade.getAllServiceProviders();
    }

    @PermitAll
    @Override
    public List<Offer> getOffersFilteredByPrice(int minPrice, int maxPrice) {

        if (maxPrice == -1) {
            maxPrice = offerFacade.findOfferWithHighestPrice().get(0).getPrice();
        }

        return offerFacade.findOffersFilteredByPrice(minPrice, maxPrice);
    }

    @Override
    @PermitAll
    public List<OfferDate> getOfferDatesByOfferId(Long id) {
        return offerDateFacade.findOfferDatesByOfferId(id);
    }

}
