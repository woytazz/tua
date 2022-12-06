package pl.lodz.p.it.ssbd2022.ssbd01.service.mw;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mo.OfferDateDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.InvalidOfferDateException;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.OfferAlreadyTakenException;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.OfferInactiveException;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.RenterDetailsFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mw.OfferDateFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mw.UserOfferFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mz.OfferFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.*;
import pl.lodz.p.it.ssbd2022.ssbd01.service.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mw.interfaces.HiringServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;

import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(GenericServiceInterceptor.class)
public class HiringService extends AbstractService implements HiringServiceInterface, SessionSynchronization {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private RenterDetailsFacade renterDetailsFacade;

    @Inject
    private OfferFacade offerFacade;

    @Inject
    private OfferDateFacade offerDateFacade;

    @Inject
    private UserOfferFacade userOfferFacade;

    @Override
    @RolesAllowed("Renter")
    public void hireOffer(String login, OfferDateDTO dto, String ETag) {

        if (dto.getDate().isBefore(LocalDate.now())) {
            throw new InvalidOfferDateException();
        }
        if (!offerDateFacade.findByDateAndOfferId(dto.getDate(), dto.getId()).isEmpty()) {
            throw new OfferAlreadyTakenException();
        }
        if (!offerFacade.find(dto.getId()).isActive()) {
            throw new OfferInactiveException();
        }

        Account account = accountFacade.findByLogin(login);
        RenterDetails renter = renterDetailsFacade.findByAccountId(account.getId());

        Offer offer = offerFacade.find(dto.getId());
        if (!ETag.equals(HMAC.calculateHMAC(offer.getId(), offer.getVersion()))) {
            throw new OptimisticLockException();
        }

        OfferDate offerDate = new OfferDate();
        offerDate.setDate(dto.getDate());
        offerDate.setOffer(offer);

        offerDateFacade.create(offerDate);

        UserOffer userOffer = new UserOffer();
        userOffer.setRenter(renter);
        userOffer.setOfferDate(offerDate);

        userOfferFacade.create(userOffer);
    }

    @Override
    @RolesAllowed("Renter")
    public List<OfferDate> getUsersOffers(String username) {
        Account account = accountFacade.findByLogin(username);
        RenterDetails renterDetails = renterDetailsFacade.findByAccountId(account.getId());
        return offerDateFacade.findOfferDatesByUsername(renterDetails.getUserName());
    }

}
