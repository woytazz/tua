package mok;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.add.AddRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

public class MOK5Test {

    private String URL = "http://localhost:8080/api/mok/update/addRenterAccess";

    private String JWTToken;

    @Test
    public void positiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        AddRenterDTO renterDTO = new AddRenterDTO();
        renterDTO.setUserName("testMOK5");

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(renterDTO));

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void NoJWTNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        AddRenterDTO renterDTO = new AddRenterDTO();

        Response response = target
                .request()
                .put(Entity.json(renterDTO));

        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void NoRoleNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("test")));

        AddRenterDTO renterDTO = new AddRenterDTO();

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(renterDTO));

        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    public void InvalidDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        AddRenterDTO renterDTO = new AddRenterDTO();
        renterDTO.setUserName("5testMOK5");

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(renterDTO));

        Assertions.assertEquals(400, response.getStatus());
    }
}
