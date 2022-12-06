package pl.lodz.p.it.ssbd2022.ssbd01.controller.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostAdminDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.TokenExpiredException;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.PostMokMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.CreateAccountServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.MOKEmailSenderInterface;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.logging.Logger;

/**
 * Kontroler odpowiedzialny za stworzenie konta i rejestracje użytkownika
 */

@Path("/mok/create")
@RequestScoped
public class CreateAccountController {

    private static final Logger LOGGER = Logger.getLogger(CreateAccountController.class.getName());

    @Inject
    private CreateAccountServiceInterface createAccountService;

    @Inject
    private MOKEmailSenderInterface emailSender;

    /**
     * Punkt końcowy odpowiedzialny za stworzenie konta administratora
     *
     * @param postAdminDTO - DTO z danymi administratora
     * @return - odpowiedź JSON
     */

    @POST
    @RolesAllowed("Admin")
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAdmin(@Valid PostAdminDTO postAdminDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                createAccountService.createAdminAccount(
                        PostMokMapper.convertToAccount(postAdminDTO),
                        PostMokMapper.convertToAdminDetails(postAdminDTO)
                );

                rollbackTX = createAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialny za rejestrację użytkownika i stworzenie konta wypożyczającego
     *
     * @param postRenterDTO - DTO z danymi wypożyczającego
     * @return - odpowiedź JSON
     */

    @POST
    @PermitAll
    @Path("/register-renter")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerRenterUser(@Valid PostRenterDTO postRenterDTO, @QueryParam("language") String language) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                createAccountService.createRenterAccountAndSendVerificationEmail(
                        PostMokMapper.convertToAccount(postRenterDTO),
                        PostMokMapper.convertToRenterDetails(postRenterDTO)
                );

                rollbackTX = createAccountService.isLastTransactionRollback();
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

        emailSender.sendVerificationEmail(postRenterDTO.getEmail(), language);

        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    /**
     * Punkt końcowy odpowiedzialny za rejestrację użytkownika i stworzenie konta dostawcy usług
     *
     * @param postServiceProviderDTO - DTO z danymi dostawcy usług
     * @return - odpowiedź JSON
     */

    @POST
    @PermitAll
    @Path("/register-provider")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerServiceProviderUser(@Valid PostServiceProviderDTO postServiceProviderDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                createAccountService.createServiceProviderAccount(
                        PostMokMapper.convertToAccount(postServiceProviderDTO),
                        PostMokMapper.convertToServiceProviderDetails(postServiceProviderDTO)
                );

                rollbackTX = createAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialny za potwierdzenie konta użytkownika
     *
     * @param token - UUID token wysłany na email
     * @return - odpowiedź JSON
     */

    @GET
    @PermitAll
    @Path("/confirmation/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirm(@PathParam("token") String token) {
        final URI tokenConfirmedUri = URI.create("https://studapp.it.p.lodz.pl:8401/tokenConfirmed");
        final URI tokenExpiredUri = URI.create("https://studapp.it.p.lodz.pl:8401/tokenExpired");
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                createAccountService.confirmToken(token);

                rollbackTX = createAccountService.isLastTransactionRollback();
                if (rollbackTX) LOGGER.info("*** Transaction rollback");

            } catch (TokenExpiredException e) {
                return Response
                        .seeOther(tokenExpiredUri)
                        .build();

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
                .seeOther(tokenConfirmedUri)
                .build();
    }
}
