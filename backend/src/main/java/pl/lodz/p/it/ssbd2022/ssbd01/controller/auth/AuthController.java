package pl.lodz.p.it.ssbd2022.ssbd01.controller.auth;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.auth.AuthDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.service.auth.interfaces.AuthServiceInterface;

import javax.annotation.security.PermitAll;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controller responsible for authentication process
 */

@Path("/auth")
@RequestScoped
public class AuthController {

    @Inject
    private AuthServiceInterface authService;

    /**
     * Endpoint odpowiedzialny za uwierzytelnianie użytkowników w aplikacji
     *
     * @param authDTO - DTO for authenticating users
     * @return - JSON response
     */

    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(@Valid AuthDTO authDTO) {
        try {
            return Response
                    .status(Response.Status.ACCEPTED)
                    .entity(authService.generateTokens(authDTO.getLogin(), authDTO.getPassword()))
                    .build();

        } catch (ApplicationBaseException exc) {
            throw exc;
        } catch (EJBAccessException | AccessLocalException exc) {
            throw ApplicationBaseException.getAccessDeniedException(exc);
        } catch (Exception exc) {
            throw ApplicationBaseException.getGeneralErrorException(exc);
        }
    }

    /**
     * Endpoint odpoiwedzialny za odświeżanie accessTokenu
     *
     * @param refreshToken - refresh token
     * @return - JSON response
     */

    @GET
    @PermitAll
    @Path("/refresh/{refreshToken}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshToken(@PathParam("refreshToken") String refreshToken) {
        try {
            return Response
                    .ok()
                    .entity(authService.refreshTokens(refreshToken))
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