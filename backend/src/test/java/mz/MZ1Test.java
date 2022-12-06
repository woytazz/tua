package mz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

public class MZ1Test {

    private String URL = "http://localhost:8080/api/mz/offer/add";

    private String JWTToken;

    @Test
    public void positiveTests() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMOK5");
        offerDTO.setPrice(123);
        offerDTO.setTitle("test");

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));

        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    public void NoJWTNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        OfferDTO offerDTO = new OfferDTO();

        Response response = target
                .request()
                .post(Entity.json(offerDTO));

        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void NoRoleNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("test")));

        OfferDTO offerDTO = new OfferDTO();

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));

        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    public void InvalidDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("");
        offerDTO.setPrice(1000000000);

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));

        Assertions.assertEquals(400, response.getStatus());
    }

}
