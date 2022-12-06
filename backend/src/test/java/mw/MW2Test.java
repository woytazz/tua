package mw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

public class MW2Test {

    private String URL_RATE = "http://localhost:8080/api/mw/rate";

    private String JWTToken;

    @Test
    public void rateServicePositiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRate = client.target(URL_RATE);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("renter", new HashSet<>(List.of("Renter")));

        Response response = targetRate
                .path("/{serviceName}/{rateNumber}")
                .resolveTemplate("serviceName", "service")
                .resolveTemplate("rateNumber", 4)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(""));

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void rateServiceUnauthorizedNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRate = client.target(URL_RATE);


        Response response = targetRate
                .path("/{serviceName}/{rateNumber}")
                .resolveTemplate("serviceName", "service")
                .resolveTemplate("rateNumber", 4)
                .request()
                .put(Entity.json(""));

        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void rateServiceInvalidRateNumberNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRate = client.target(URL_RATE);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("renter", new HashSet<>(List.of("Renter")));

        Response response = targetRate
                .path("/{serviceName}/{rateNumber}")
                .resolveTemplate("serviceName", "service")
                .resolveTemplate("rateNumber", 10)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(""));

        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    public void rateServiceServiceNotFoundNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRate = client.target(URL_RATE);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("renter", new HashSet<>(List.of("Renter")));

        Response response = targetRate
                .path("/{serviceName}/{rateNumber}")
                .resolveTemplate("serviceName", "ser")
                .resolveTemplate("rateNumber", 4)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(""));

        Assertions.assertEquals(404, response.getStatus());
    }
}
