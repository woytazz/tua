package pl.lodz.p.it.ssbd2022.ssbd01.controller.mo;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.GetMoMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.GetMokMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.mapper.GetOfferDateMapper;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mo.interfaces.BrowseOfferServiceInterface;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Kontroler odpowiedzialny za przeglądanie ofert
 */

@Path("/mo/view")
@RequestScoped
public class BrowseOfferController {

    @Inject
    private BrowseOfferServiceInterface browseOfferService;

    /**
     * Endpoint odpowiedzialny za odczytanie wszysktich ofert
     *
     * @return JSON response
     */

    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOffers() {
        try {
            return Response
                    .ok()
                    .entity(GetMoMapper.getOfferDTOList(browseOfferService.getAllOffers()))
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
     * Endpoint odpowiedzialny za odczytanie wszysktich aktywnych ofert
     *
     * @return JSON response
     */

    @GET
    @PermitAll
    @Path("/allActive")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllActiveOffers() {
        try {
            return Response.ok().entity(GetMoMapper.getOfferDTOList(browseOfferService.getActiveOffers())).build();
        } catch (ApplicationBaseException exc) {
            throw exc;
        } catch (EJBAccessException | AccessLocalException exc) {
            throw ApplicationBaseException.getAccessDeniedException(exc);
        } catch (Exception exc) {
            throw ApplicationBaseException.getGeneralErrorException(exc);
        }
    }

    /**
     * Endpoint odpowiedzialny za odczytanie oferty o danym ID
     *
     * @param id - id oferty
     * @return JSON response
     */

    @GET
    @PermitAll
    @Path("/byId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfferById(@QueryParam("id") Long id) {
        try {
            return Response.ok().entity(GetMoMapper.getOfferDTO(browseOfferService.getOfferById(id))).build();
        } catch (ApplicationBaseException exc) {
            throw exc;
        } catch (EJBAccessException | AccessLocalException exc) {
            throw ApplicationBaseException.getAccessDeniedException(exc);
        } catch (Exception exc) {
            throw ApplicationBaseException.getGeneralErrorException(exc);
        }
    }

    /**
     * Endpoint odpowiedzialny za odczytanie ofert danego usługodawcy
     *
     * @param securityContext - security context
     * @return JSON response
     */

    @GET
    @RolesAllowed("ServiceProvider")
    @Path("/byServiceName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOffersByServiceName(@Context SecurityContext securityContext) {
        try {
            return Response.ok().entity(GetMoMapper.getOfferDTOList(browseOfferService.getAllServiceProviderOffers(securityContext.getUserPrincipal().getName()))).build();
        } catch (ApplicationBaseException exc) {
            throw exc;
        } catch (EJBAccessException | AccessLocalException exc) {
            throw ApplicationBaseException.getAccessDeniedException(exc);
        } catch (Exception exc) {
            throw ApplicationBaseException.getGeneralErrorException(exc);
        }
    }

    /**
     * Endpoint odpoiwedzialny za odczytywanie wszystkich usługodawców
     *
     * @return JSON response
     */

    @GET
    @PermitAll
    @Path("/service_providers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllServiceProviders() {
        try {
            return Response
                    .ok()
                    .entity(GetMokMapper.getAccountDTOList(browseOfferService.getAllServiceProviders()))
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
     * Endpoint służący do filtrowania ofert po cenie
     *
     * @param minPrice minimalna cena oferty
     * @param maxPrice maksymalna cena oferty
     * @return JSON response
     */

    @GET
    @PermitAll
    @Path("/byPrice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOffersFilteredByPrice(@QueryParam("minPrice") int minPrice, @QueryParam("maxPrice") int maxPrice) {
        try {
            return Response
                    .ok()
                    .entity(GetMoMapper.getOfferDTOList(browseOfferService.getOffersFilteredByPrice(minPrice, maxPrice)))
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
     * Endpoint służący do otrzymania zarezerwoanych dat dla danej oferty
     *
     * @param offerId - id oferty
     * @return JSON response
     */
    @GET
    @PermitAll
    @Path("/offerDates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfferDatesByOfferId(@QueryParam("id") Long offerId) {
        try {
            return Response
                    .ok()
                    .entity(GetOfferDateMapper.getOfferDateDTOList(browseOfferService.getOfferDatesByOfferId(offerId)))
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
