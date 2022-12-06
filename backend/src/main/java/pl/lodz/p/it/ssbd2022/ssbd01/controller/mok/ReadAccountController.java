package pl.lodz.p.it.ssbd2022.ssbd01.controller.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.GetMokMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.ReadAccountServiceInterface;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Kontroler odpowiedzialny za czytanie danych o koncie użytkownika
 */

@Path("/mok/read")
@RequestScoped
public class ReadAccountController {

    @Inject
    private ReadAccountServiceInterface readAccountService;

    /**
     * Endpoint odpowiedzialny za odczytywanie danych konta po zalogowaniu
     *
     * @param securityContext - security context
     * @return - JSON response
     */

    @GET
    @RolesAllowed("ALL")
    @Path("/_self")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findSelf(@Context SecurityContext securityContext) {
        try {
            return Response
                    .ok()
                    .entity(GetMokMapper.getAccountDTO(readAccountService.readAccountByLogin(securityContext.getUserPrincipal().getName())))
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
     * Punkt końcowy odpowiedzialny za znalezienie użytkownika przy pomocy loginu
     *
     * @param login - login użytkownika
     * @return - odpowiedź JSON
     */

    @GET
    @RolesAllowed("ALL")
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAccountByLogin(@PathParam("login") String login) {
        try {
            return Response
                    .ok()
                    .entity(GetMokMapper.getAccountDTO(readAccountService.readAccountByLogin(login)))
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
     * Endpoint odpowiedzialny za pobieranie danych usługodawcy po przekazanej nazwie
     *
     * @param serviceName - nazwa usługodawcy
     * @return JSON response
     */

    @GET
    @PermitAll
    @Path("/serviceProvider/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readServiceProviderByName(@PathParam("name") String serviceName) {
        try {
            return Response
                    .ok()
                    .entity(GetMokMapper.getAccountDTO(readAccountService.readServiceProviderByName(serviceName)))
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
     * Punkt końcowy odpowiedzialny za czytanie listy dostawców usług, których konta nie są jeszcze potwierdzone
     *
     * @return - odpowiedź JSON
     */

    @GET
    @RolesAllowed("Admin")
    @Path("/providersToConfirm")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readProvidersToConfirm() {
        try {
            return Response
                    .ok()
                    .entity(GetMokMapper.getAccountDTOList(readAccountService.readProvidersToConfirm()))
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
     * Punkt końcowy odpowiedzialny czytanie wszystkich użytkowników
     *
     * @return - odpowiedź JSON
     */

    @GET
    @RolesAllowed("Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAllAccounts() {
        try {
            return Response
                    .ok()
                    .entity(GetMokMapper.getAccountDTOList(readAccountService.readAllAccounts()))
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
