package pl.lodz.p.it.ssbd2022.ssbd01.controller.mw;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mo.OfferDateDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.GetOfferDateMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mw.interfaces.HiringServiceInterface;

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

/**
 * Kontroler odpowiedzialny za wynajmowanie ofert
 */

@Path("/mw")
@RequestScoped
public class HiringController {

    private static final Logger LOGGER = Logger.getLogger(HiringController.class.getName());

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private HiringServiceInterface hiringService;

    /**
     * Punkt końcowy odpowiedzialny za rezerwowanie danej oferty
     *
     * @param offerDateDto - DTO z danymi o wynajmowanej ofercie
     * @return - odpowiedź JSON
     */

    @POST
    @Path("/hire")
    @RolesAllowed("Renter")
    public Response hireOffer(@Context SecurityContext securityContext, OfferDateDTO offerDateDto) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                hiringService.hireOffer(
                        securityContext.getUserPrincipal().getName(),
                        offerDateDto,
                        httpServletRequest.getHeader("If-Match")
                );

                rollbackTX = hiringService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** Transaction rollback");

            } catch (ApplicationBaseException exc) {
                throw exc;
            } catch (EJBAccessException | AccessLocalException exc) {
                throw ApplicationBaseException.getAccessDeniedException(exc);
            } catch (Exception exc) {
                throw ApplicationBaseException.getGeneralErrorException(exc);
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX) {
            throw ApplicationBaseException.getGeneralErrorException();
        }

        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @GET
    @Path("/getMyOfferDates")
    @RolesAllowed("Renter")
    public Response getMyOfferDates(@Context SecurityContext securityContext) {
        try {
            return Response
                    .ok()
                    .entity(GetOfferDateMapper.getOfferDateViewDTOList(hiringService.getUsersOffers(securityContext.getUserPrincipal().getName())))
                    .build();
        } catch (ApplicationBaseException exc) {
            throw exc;
        } catch (EJBAccessException | AccessLocalException exc) {
            throw ApplicationBaseException.getAccessDeniedException(exc);
        } catch (Exception exc) {
            throw ApplicationBaseException.getGeneralErrorException(exc);
        }
    }

}
