package pl.lodz.p.it.ssbd2022.ssbd01.service.mz;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.OfferOwnerException;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.ServiceProviderDetailsFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mz.OfferFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Offer;
import pl.lodz.p.it.ssbd2022.ssbd01.model.ServiceProviderDetails;
import pl.lodz.p.it.ssbd2022.ssbd01.service.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mz.interfaces.OfferServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.OptimisticLockException;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(GenericServiceInterceptor.class)
public class OfferService extends AbstractService implements OfferServiceInterface, SessionSynchronization {

    @Inject
    private OfferFacade offerFacade;

    @Inject
    private ServiceProviderDetailsFacade serviceProviderFacade;

    @RolesAllowed("ServiceProvider")
    @Override
    public void addOffer(String login, Offer offer) {
        ServiceProviderDetails serviceProviderDetails = serviceProviderFacade.findByAccountLogin(login);
        offer.setServiceProvider(serviceProviderDetails);
        offerFacade.create(offer);
    }

    @RolesAllowed("Admin")
    @Override
    public void deleteOffer(Long id) {
        Offer offer = offerFacade.find(id);

        offer.setActive(false);
        offerFacade.edit(offer);
    }

    @RolesAllowed("ServiceProvider")
    @Override
    public void deleteOwnOffer(String login, Long id) {
        Offer offer = offerFacade.getById(id);
        if (!offer.getServiceProvider().getAccount().getLogin().equals(login)) {
            throw new OfferOwnerException();
        }

        offer.setActive(false);
        offerFacade.edit(offer);
    }

    @RolesAllowed("ServiceProvider")
    public void editOffer(String login, OfferDTO offerDTO, Long id, String ETag) {
        Offer offer = offerFacade.getById(id);

        if (!ETag.equals(HMAC.calculateHMAC(offer.getId(), offer.getVersion()))) {
            throw new OptimisticLockException();
        }
        if (!offer.getServiceProvider().getAccount().getLogin().equals(login)) {
            throw new OfferOwnerException();
        }

        offer.setTitle(offerDTO.getTitle());
        offer.setPrice(offerDTO.getPrice());
        offer.setDescription(offerDTO.getDescription());
        offerFacade.edit(offer);
    }

    @RolesAllowed("Admin")
    public void editOfferAdmin(OfferDTO offerDTO, Long id, String ETag) {
        Offer offer = offerFacade.getById(id);

        if (!ETag.equals(HMAC.calculateHMAC(offer.getId(), offer.getVersion()))) {
            throw new OptimisticLockException();
        }
        offer.setTitle(offerDTO.getTitle());
        offer.setPrice(offerDTO.getPrice());
        offer.setDescription(offerDTO.getDescription());
        offerFacade.edit(offer);
    }
}
