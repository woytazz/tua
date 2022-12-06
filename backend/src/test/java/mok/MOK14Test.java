package mok;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.auth.AuthDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

public class MOK14Test {

    private String URL = "http://localhost:8080/api/auth";

    private String JWTToken;

    @Test
    public void positiveTestLoginUser() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        AuthDTO authDTO = new AuthDTO();
        authDTO.setLogin("renter");
        authDTO.setPassword("ssbd01");

        Response response = target
                .request()
                .post(Entity.json(authDTO));

        Assertions.assertEquals(202, response.getStatus());
    }

    @Test
    public void positiveTestUserSelf() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target("http://localhost:8080/api/mok/read");


        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response response = target
                .path("/_self")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + JWTToken)
                .get();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void userSelfUnauthorizedNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target("http://localhost:8080/api/mok/read");


        Response response = target
                .path("/_self")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void loginUserWrongDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        AuthDTO authDTO = new AuthDTO();
        authDTO.setLogin("re");
        authDTO.setPassword("ssbd01");

        Response response = target
                .request()
                .post(Entity.json(authDTO));

        Assertions.assertEquals(401, response.getStatus());
    }


}
