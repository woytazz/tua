package mw;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mo.OfferDateDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferViewDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

public class MW1Test {

    private String URL_HIRE = "http://localhost:8080/api/mw/hire";
    private String URL_CREATE = "http://localhost:8080/api/mz/offer/add";
    private String URL_DELETE = "http://localhost:8080/api/mz/offer/deleteByAdmin";
    private String URL_READ = "http://localhost:8080/api/mo/view";

    private String JWTToken;

    private String JWTToken2;

    private String JWTToken3;

    @Test
    public void hireOfferPositiveTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetCreateOffer = client.target(URL_CREATE);
        WebTarget targetHire = client.target(URL_HIRE);
        WebTarget targetRead = client.target(URL_READ);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setPrice(300);
        offerDTO.setDescription("testowanko");
        offerDTO.setTitle("test");

        targetCreateOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));

        Response responseOffers = targetRead
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = responseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();

        OfferDateDTO offerDateDTO = new OfferDateDTO();
        LocalDate date = LocalDate.of(2080, Month.APRIL, 19);
        offerDateDTO.setDate(date);
        offerDateDTO.setId(id);

        JWTToken2 = JWTGeneratorVerifier.generateAccessJWTString("renter", new HashSet<>(List.of("Renter")));

        String ETag = HMAC.calculateHMAC(id, 1L);

        String jsonObject = new JSONObject().put("id", id).put("date", date).toString();

        Response response = targetHire
                .request()
                .header("Authorization", "Bearer " + JWTToken2)
                .header("If-Match", ETag)
                .post(Entity.json(jsonObject));

        Assertions.assertEquals(201, response.getStatus());
    }


    @Test
    public void hireOfferUnauthorizedNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetHire = client.target(URL_HIRE);


        OfferDateDTO offerDateDTO = new OfferDateDTO();
        LocalDate date = LocalDate.of(2025, Month.APRIL, 19);
        offerDateDTO.setDate(date);
        offerDateDTO.setId(2L);

        String ETag = HMAC.calculateHMAC(2L, 1L);

        String jsonObject = new JSONObject().put("id", 2L).put("date", date).toString();

        Response response = targetHire
                .request()
                .header("If-Match", ETag)
                .post(Entity.json(jsonObject));

        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void hireOfferInvalidDateNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetCreateOffer = client.target(URL_CREATE);
        WebTarget targetHire = client.target(URL_HIRE);
        WebTarget targetRead = client.target(URL_READ);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setPrice(300);
        offerDTO.setDescription("testoaawanko");

        targetCreateOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));

        Response responseOffers = targetRead
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = responseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();

        OfferDateDTO offerDateDTO = new OfferDateDTO();
        LocalDate date = LocalDate.of(2019, Month.APRIL, 19);
        offerDateDTO.setDate(date);
        offerDateDTO.setId(id);

        JWTToken2 = JWTGeneratorVerifier.generateAccessJWTString("renter", new HashSet<>(List.of("Renter")));

        String ETag = HMAC.calculateHMAC(id, 1L);

        String jsonObject = new JSONObject().put("id", id).put("date", date).toString();

        Response response = targetHire
                .request()
                .header("Authorization", "Bearer " + JWTToken2)
                .header("If-Match", ETag)
                .post(Entity.json(jsonObject));

        Assertions.assertEquals(409, response.getStatus());
    }

    @Test
    public void hireOfferOfferAlredyTakenNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetCreateOffer = client.target(URL_CREATE);
        WebTarget targetHire1 = client.target(URL_HIRE);
        WebTarget targetHire2 = client.target(URL_HIRE);
        WebTarget targetRead = client.target(URL_READ);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setPrice(300);
        offerDTO.setDescription("testoaawanko");

        targetCreateOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));

        Response responseOffers = targetRead
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = responseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();

        OfferDateDTO offerDateDTO = new OfferDateDTO();
        LocalDate date = LocalDate.of(2030, Month.APRIL, 19);
        offerDateDTO.setDate(date);
        offerDateDTO.setId(id);

        JWTToken2 = JWTGeneratorVerifier.generateAccessJWTString("renter", new HashSet<>(List.of("Renter")));

        String ETag = HMAC.calculateHMAC(id, 1L);

        String jsonObject = new JSONObject().put("id", id).put("date", date).toString();

        targetHire1
                .request()
                .header("Authorization", "Bearer " + JWTToken2)
                .header("If-Match", ETag)
                .post(Entity.json(jsonObject));

        Response response = targetHire2
                .request()
                .header("Authorization", "Bearer " + JWTToken2)
                .header("If-Match", ETag)
                .post(Entity.json(jsonObject));

        Assertions.assertEquals(409, response.getStatus());
    }

    @Test
    public void hireOfferOfferInactiveNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetCreateOffer = client.target(URL_CREATE);
        WebTarget targetHire = client.target(URL_HIRE);
        WebTarget targetRead = client.target(URL_READ);
        WebTarget targetDeactivate = client.target(URL_DELETE);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setPrice(300);
        offerDTO.setDescription("testoaawankaao");

        targetCreateOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));

        Response responseOffers = targetRead
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = responseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();

        JWTToken2 = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response res = targetDeactivate
                .path("/{id}")
                .resolveTemplate("id", id)
                .request()
                .header("Authorization", "Bearer " + JWTToken2)
                .delete();

        OfferDateDTO offerDateDTO = new OfferDateDTO();
        LocalDate date = LocalDate.of(2030, Month.APRIL, 19);
        offerDateDTO.setDate(date);
        offerDateDTO.setId(id);

        JWTToken3 = JWTGeneratorVerifier.generateAccessJWTString("renter", new HashSet<>(List.of("Renter")));

        String ETag = HMAC.calculateHMAC(id, 1L);

        String jsonObject = new JSONObject().put("id", id).put("date", date).toString();

        Response response = targetHire
                .request()
                .header("Authorization", "Bearer " + JWTToken3)
                .header("If-Match", ETag)
                .post(Entity.json(jsonObject));

        Assertions.assertEquals(409, response.getStatus());
    }

}
