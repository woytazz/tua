package pl.lodz.p.it.ssbd2022.ssbd01.controller.mz;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.OfferMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mz.interfaces.OfferServiceInterface;

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

@Path("/mz/offer")
@RequestScoped
public class OfferController {

    private static final Logger LOGGER = Logger.getLogger(OfferController.class.getName());

    @Inject
    private OfferServiceInterface offerService;

    @Inject
    private HttpServletRequest httpServletRequest;

    /**
     * Endpoint odpowiedzialny za dodawanie nowej oferty
     *
     * @param securityContext - security context
     * @param offerDTO        - DTO for adding new offer
     * @return - JSON response
     */

    @POST
    @Path("/add")
    @RolesAllowed("ServiceProvider")
    public Response addOffer(@Context SecurityContext securityContext, @Valid OfferDTO offerDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                offerService.addOffer(
                        securityContext.getUserPrincipal().getName(),
                        OfferMapper.convertToOffer(offerDTO)
                );

                rollbackTX = offerService.isLastTransactionRollback();
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

    /**
     * Endpoint odpoiwedzialny za usuwanie oferty przez admina
     *
     * @param id offer's id
     * @return - JSON response
     */

    @DELETE
    @Path("/deleteByAdmin/{id}")
    @RolesAllowed("Admin")
    public Response deleteOfferAdmin(@PathParam("id") Long id) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                offerService.deleteOffer(id);

                rollbackTX = offerService.isLastTransactionRollback();
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
                .ok()
                .build();
    }

    /**
     * Endpoint odpoiwedzialny za usuwanie swojej oferty
     *
     * @param securityContext security context
     * @param id              offer's id
     * @return - JSON response
     */

    @DELETE
    @Path("/deleteByService/{id}")
    @RolesAllowed("ServiceProvider")
    public Response deleteOfferServiceProvider(@Context SecurityContext securityContext, @PathParam("id") Long id) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {

            try {
                offerService.deleteOwnOffer(securityContext.getUserPrincipal().getName(), id);

                rollbackTX = offerService.isLastTransactionRollback();
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
                .ok()
                .build();
    }

    /**
     * Endpoint odpoiwedzialny za edytowanie swojej oferty
     *
     * @param securityContext security context
     * @param offerDTO        DTO for editing service_provider's offer
     * @param id              id oferty
     * @return - JSON response
     */

    @PUT
    @Path("/editByService/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ServiceProvider")
    public Response editOfferServiceProvider(@Context SecurityContext securityContext, @Valid OfferDTO offerDTO, @PathParam("id") Long id) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {

            try {
                offerService.editOffer(securityContext.getUserPrincipal().getName(), offerDTO, id, httpServletRequest.getHeader("If-Match"));
                rollbackTX = offerService.isLastTransactionRollback();
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
                .ok()
                .build();
    }

    /**
     * Endpoint odpoiwedzialny za edytowanie ofert przez administratora
     *
     * @param id       offer id
     * @param offerDTO DTO for editing service_provider's offer
     * @return - JSON response
     */

    @PUT
    @Path("/editByAdmin/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Admin")
    public Response editOfferAdmin(@Valid OfferDTO offerDTO, @PathParam("id") Long id) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                offerService.editOfferAdmin(offerDTO, id, httpServletRequest.getHeader("If-Match"));
                rollbackTX = offerService.isLastTransactionRollback();
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
                .ok()
                .build();
    }
}
