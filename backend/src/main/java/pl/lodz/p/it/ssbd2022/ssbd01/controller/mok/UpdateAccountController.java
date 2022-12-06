package pl.lodz.p.it.ssbd2022.ssbd01.controller.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.add.AddRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.add.AddServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutAccountDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.TokenExpiredException;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.AddMokMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.PutMokMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.MOKEmailSenderInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.UpdateAccountServiceInterface;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.util.logging.Logger;

/**
 * Kontroler odpowiedzialny za aktualizację kont użytkowników
 */

@Path("/mok/update")
@RequestScoped
public class UpdateAccountController {

    private static final Logger LOGGER = Logger.getLogger(UpdateAccountController.class.getName());

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private MOKEmailSenderInterface emailSender;

    @Inject
    private UpdateAccountServiceInterface updateAccountService;

    /**
     * Punkt końcowy odpowiezdialny za zmianę statusu konta użytkownika, zależnie od poprzedniego statusu
     *
     * @param login  - login użytkownika
     * @param active - status aktualizowanego użytkownika
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/changeActive")
    public Response changeActive(@QueryParam("login") String login, @QueryParam("active") boolean active, @QueryParam("language") String language) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                if (active) {
                    updateAccountService.activateUserAccount(login, httpServletRequest.getHeader("If-Match"));
                } else {
                    updateAccountService.deactivateUserAccount(login, httpServletRequest.getHeader("If-Match"));
                }

                rollbackTX = updateAccountService.isLastTransactionRollback();
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

        if (active) {
            emailSender.sendActivateEmail(login, language);
        } else {
            emailSender.sendDeactivateEmail(login, language);
        }


        return Response
                .ok()
                .build();
    }

    /**
     * Punkt końcowy odpowiedzialny za dodanie poziomu dostępu renter dla konta administratora
     *
     * @param renterDTO       - DTO ze szczegółowymi danymi wynajmującego
     * @param securityContext - Kontekst bezpieczeństwa
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/addRenterAccess")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRenterAccess(@Context SecurityContext securityContext, @Valid AddRenterDTO renterDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                updateAccountService.addRenterAccessLevel(
                        securityContext.getUserPrincipal().getName(),
                        AddMokMapper.convertToRenterDetails(renterDTO)
                );

                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialny za dodanie poziomu dostępu service provider dla konta administratora
     *
     * @param addServiceProviderDTO - DTO ze szczegółowymi danymi dostarczyciela usług
     * @param securityContext       - kontekst bezpieczeństwa
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/addServiceProviderAccess")
    public Response addServiceProviderAccess(@Context SecurityContext securityContext, @Valid AddServiceProviderDTO addServiceProviderDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                updateAccountService.addServiceProviderAccessLevel(
                        securityContext.getUserPrincipal().getName(),
                        PutMokMapper.convertToServiceProviderDetails(addServiceProviderDTO)
                );

                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialny za usuwanie poziomu dostępu z konta administratora
     *
     * @param securityContext - kontekst bezpieczeństwa
     * @param accessLevel     - nazwa poziomu dostępu do usunięcia
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/deleteAccessLevel")
    public Response deleteAccessLevel(@Context SecurityContext securityContext, @QueryParam("accessLevel") String accessLevel) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                if (accessLevel.equals("Admin")) {
                    return Response
                            .status(403, "access.level.cannot.be.deleted")
                            .build();
                }

                updateAccountService.deleteAccessLevel(
                        securityContext.getUserPrincipal().getName(),
                        accessLevel
                );

                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialny za potwierdzenie konta dostarczyciela usług
     *
     * @param login - login użytkownika do potwierdzienia
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/activateServiceProvider")
    public Response confirmServiceProvider(@QueryParam("login") String login, @QueryParam("language") String language) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                updateAccountService.confirmServiceProvider(login, language);

                rollbackTX = updateAccountService.isLastTransactionRollback();
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

        emailSender.sendConfirmServiceProviderEmail(login, language);

        return Response
                .ok()
                .build();
    }

    /**
     * Punkt końcowy odpowiedzialny za zmianę hasła wybranego użytkownika przez administratora
     *
     * @param login       - login użytkownika, którego hasło ma ulec zmianie
     * @param newPassword - nowe hasło dla użytkownika
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/changeUserPassword")
    public Response changeUserPassword(@QueryParam("login") String login, @QueryParam("newPassword") @Valid @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,64}$") String newPassword) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                updateAccountService.changePassword(login, newPassword, httpServletRequest.getHeader("If-Match"));

                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialny za zmianę własnego hasła przez użytkownika
     *
     * @param oldPassword - stare(aktualne) hasło
     * @param newPassword - nowe hasło dla użytkownika
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("ALL")
    @Path("/changeUserOwnPassword")
    public Response changeUserOwnPassword(@Context SecurityContext securityContext, @QueryParam("oldPassword") String oldPassword, @QueryParam("newPassword") @Valid @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,64}$") String newPassword) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                updateAccountService.changeOwnPassword(securityContext.getUserPrincipal().getName(), oldPassword, newPassword, httpServletRequest.getHeader("If-Match"));

                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialny za zmianę własnych danych przez dostawcę usługi
     *
     * @param securityContext - kontekst bezpieczeństwa
     * @param putProviderDTO  - DTO z nowymi danymi szczegółowymi dostawcy usług
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("ServiceProvider")
    @Path("/editServiceProvider")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editServiceProvider(@Context SecurityContext securityContext, @Valid PutProviderDTO putProviderDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;

        do {
            try {
                updateAccountService.editServiceProviderDetails(
                        securityContext.getUserPrincipal().getName(),
                        putProviderDTO,
                        httpServletRequest.getHeader("If-Match")
                );

                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialny za zmianę danych dostawcy usług przez administratora
     *
     * @param login          - login zmienianego dostawcy usług
     * @param putProviderDTO - dto ze szczegółowymi danymi dostawcy usług
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/editServiceProvider/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editServiceProviderAdmin(@PathParam("login") String login, @Valid PutProviderDTO putProviderDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;

        do {
            try {
                updateAccountService.editServiceProviderDetails(
                        login,
                        putProviderDTO,
                        httpServletRequest.getHeader("If-Match")
                );

                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialany za zmianę własnych danych przez administratora
     *
     * @param securityContext - kontekst bezpieczeństwa
     * @param putAccountDTO   - dto z danymi administratora
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/editAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAdminAccount(@Context SecurityContext securityContext, @Valid PutAccountDTO putAccountDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                updateAccountService.updateAdmin(
                        securityContext.getUserPrincipal().getName(),
                        putAccountDTO,
                        httpServletRequest.getHeader("If-Match")
                );

                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialany za zmianę własnych danych przez wypożyczającego
     *
     * @param securityContext - kontekst bezpieczeństwa
     * @param putRenterDTO    - dto z danymi wypożyczającego
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Renter")
    @Path("/editRenter")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRenterAccount(@Context SecurityContext securityContext, @Valid PutRenterDTO putRenterDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;
        do {
            try {
                updateAccountService.updateRenter(
                        securityContext.getUserPrincipal().getName(),
                        putRenterDTO,
                        httpServletRequest.getHeader("If-Match")
                );
                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialany za zmianę danych wypożyczającego przez administratora
     *
     * @param login        - login zmienianego wypożyczającego
     * @param putRenterDTO - dto z danymi wypożyczającego
     * @return - odpowiedź JSON
     */

    @PUT
    @RolesAllowed("Admin")
    @Path("/editRenter/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRenterAccountAdmin(@PathParam("login") String login, @Valid PutRenterDTO putRenterDTO) {
        int retryTXCounter = 3;
        boolean rollbackTX;

        do {
            try {
                updateAccountService.updateRenter(
                        login,
                        putRenterDTO,
                        httpServletRequest.getHeader("If-Match")
                );
                rollbackTX = updateAccountService.isLastTransactionRollback();
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
     * Punkt końcowy odpowiedzialany za wysłanie wiadomości email ze zmianą hasła
     *
     * @param email    - email na który ma zostać wysłana wiadomość
     * @param language - język wiadomości
     * @return - odpowiedź JSON
     */

    @PUT
    @PermitAll
    @Path("/password-reset-email-send/")
    public Response sendResetPasswordEmail(@QueryParam("email") String email, @QueryParam("language") String language) {

        int retryTXCounter = 3;
        boolean rollbackTX;

        do {
            try {
                updateAccountService.sendPasswordResetMail(email);

                rollbackTX = updateAccountService.isLastTransactionRollback();
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

        emailSender.sendPasswordResetMail(email, language);

        return Response.ok().build();
    }

    /**
     * Punkt końcowy odpowiedzialany za potwierdzenie zmiany hasła
     *
     * @param token - token weryfikacyjny
     * @return - odpowiedź JSON
     */

    @GET
    @PermitAll
    @Path("/password-reset-email-confirm/{token}")
    public Response confirmResetPasswordEmail(@PathParam("token") String token) {

        final URI resetPasswordUri = URI.create("https://studapp.it.p.lodz.pl:8401/passwordReset/" + token);
        final URI tokenExpiredUri = URI.create("https://studapp.it.p.lodz.pl:8401/tokenExpired");

        int retryTXCounter = 3;
        boolean rollbackTX;

        do {
            try {

                updateAccountService.findByVerificationToken(token);
                rollbackTX = updateAccountService.isLastTransactionRollback();
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
                .seeOther(resetPasswordUri)
                .build();
    }

    /**
     * Punkt końcowy odpowiedzialany za zmianę hasła
     *
     * @param token       - token weryfikacyjny
     * @param newPassword - nowe hasło dla użytkownika
     * @return - odpowiedź JSON
     */

    @PUT
    @PermitAll
    @Path("/password-reset")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePassword(@QueryParam("token") String token, @QueryParam("newPassword") String newPassword) {

        int retryTXCounter = 3;
        boolean rollbackTX;

        do {
            try {
                updateAccountService.resetPassword(token, newPassword);

                rollbackTX = updateAccountService.isLastTransactionRollback();
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

        return Response.ok().build();
    }

}
