package pl.lodz.p.it.ssbd2022.ssbd01.controller.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.DeleteAccountServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.MOKEmailSenderInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.ReadAccountServiceInterface;

import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Kontroler odpowiedzialny za usuwanie kont
 */

@Path("/mok/delete")
@RequestScoped
public class DeleteAccountController {

    private static final Logger LOGGER = Logger.getLogger(DeleteAccountController.class.getName());

    @Inject
    private DeleteAccountServiceInterface deleteAccountService;

    @Inject
    private MOKEmailSenderInterface emailSender;

    @Inject
    private ReadAccountServiceInterface readAccountService;

    /**
     * Punkt końcowy odpowiedzialny za usuwanie dostawców usług, które nie zostały potwierdzone przez administratora
     *
     * @param login - login użytkownika do usunięcia
     * @return - odpowiedź JSON
     */

    @DELETE
    @RolesAllowed("Admin")
    @Path("/deleteServiceProvider")
    public Response deleteServiceProvider(@QueryParam("login") String login, @QueryParam("language") String language) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        String email = readAccountService.readAccountByLogin(login).getEmail();
        do {
            try {
                deleteAccountService.deleteServiceProvider(login);

                rollbackTX = deleteAccountService.isLastTransactionRollback();
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

        emailSender.sendRemoveEmail(email, language);

        return Response
                .status(204)
                .build();
    }
}
