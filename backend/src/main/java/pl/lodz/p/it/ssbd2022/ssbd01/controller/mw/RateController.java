package pl.lodz.p.it.ssbd2022.ssbd01.controller.mw;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mw.interfaces.RateServiceInterface;

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

/**
 * Kontroler odpowiedzialny za ocenianie usług
 */

@Path("/mw")
@RequestScoped
public class RateController {

    private static final Logger LOGGER = Logger.getLogger(HiringController.class.getName());

    @Inject
    private RateServiceInterface rateService;


    /**
     * Endpoint odpowiedzialny za ocenianie usług
     *
     * @param serviceName - nazwa usługi
     * @param rateNumber  - ocena
     * @return - JSON response
     */
    @PUT
    @RolesAllowed("Renter")
    @Path("/rate/{serviceName}/{rateNumber}")
    public Response rate(@Context SecurityContext securityContext, @PathParam("serviceName") String serviceName, @PathParam("rateNumber") int rateNumber) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                rateService.rate(
                        securityContext.getUserPrincipal().getName(),
                        rateNumber,
                        serviceName
                );
                rollbackTX = rateService.isLastTransactionRollback();
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
