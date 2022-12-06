package mz;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferViewDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MZ2Test {

    private String deleteOfferURL = "http://localhost:8080/api/mz/offer/deleteByAdmin";

    private String deleteOfferByOwnerURL = "http://localhost:8080/api/mz/offer/deleteByService";

    private String addOfferURL = "http://localhost:8080/api/mz/offer/add";

    private String readOffersURL = "http://localhost:8080/api/mo/view";

    ///////////ADMIN

    @Test
    public void positiveTests() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //DODAJ NOWA OFERTE

        WebTarget addOffer = client.target(addOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMZ2");
        offerDTO.setPrice(123);

        addOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));


        //POBIERZ UTWORZONA OFERTE

        WebTarget readOffers = client.target(readOffersURL);

        Response readResponseOffers = readOffers
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = readResponseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();


        //USUN OFERTE

        WebTarget deleteOffer = client.target(deleteOfferURL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response deleteResponseOffer = deleteOffer
                .path("/" + id)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .delete();

        assertEquals(200, deleteResponseOffer.getStatus());
    }

    @Test
    public void NoJWTNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //USUN OFERTE

        WebTarget deleteOffer = client.target(deleteOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response deleteResponseOffer = deleteOffer
                .path("/" + 1L)
                .request()
                .delete();

        assertEquals(401, deleteResponseOffer.getStatus());
    }

    @Test
    public void NoRoleNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //USUN OFERTE

        WebTarget deleteOffer = client.target(deleteOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("test")));

        Response deleteResponseOffer = deleteOffer
                .path("/" + 1L)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .delete();

        assertEquals(403, deleteResponseOffer.getStatus());
    }

    @Test
    public void InvalidDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //USUN OFERTE

        WebTarget deleteOffer = client.target(deleteOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response deleteResponseOffer = deleteOffer
                .path("/1.2")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .delete();

        assertEquals(403, deleteResponseOffer.getStatus());
    }


    ////////////////////SERVICEPROVIDER


    @Test
    public void positiveServiceTests() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //DODAJ NOWA OFERTE

        WebTarget addOffer = client.target(addOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMZ2");
        offerDTO.setPrice(123);

        addOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));


        //POBIERZ UTWORZONA OFERTE

        WebTarget readOffers = client.target(readOffersURL);

        Response readResponseOffers = readOffers
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = readResponseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();


        //USUN OFERTE

        WebTarget deleteOffer = client.target(deleteOfferByOwnerURL);

        Response deleteResponseOffer = deleteOffer
                .path("/" + id)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .delete();

        assertEquals(200, deleteResponseOffer.getStatus());
    }

    @Test
    public void NoJWTServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //USUN OFERTE

        WebTarget deleteOffer = client.target(deleteOfferByOwnerURL);

        Response deleteResponseOffer = deleteOffer
                .path("/" + 1L)
                .request()
                .delete();

        assertEquals(401, deleteResponseOffer.getStatus());
    }

    @Test
    public void NoRoleServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //USUN OFERTE

        WebTarget deleteOffer = client.target(deleteOfferByOwnerURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("test")));

        Response deleteResponseOffer = deleteOffer
                .path("/" + 1L)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .delete();

        assertEquals(403, deleteResponseOffer.getStatus());
    }

    @Test
    public void InvalidDataServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //USUN OFERTE

        WebTarget deleteOffer = client.target(deleteOfferByOwnerURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        Response deleteResponseOffer = deleteOffer
                .path("/1.2")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .delete();

        assertEquals(403, deleteResponseOffer.getStatus());
    }
}
