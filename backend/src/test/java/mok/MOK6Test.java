package mok;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.add.AddServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

public class MOK6Test {

    private String URL = "http://localhost:8080/api/mok/update/addServiceProviderAccess";

    private String JWTToken;

    @Test
    public void positiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        AddServiceProviderDTO serviceProviderDTO = new AddServiceProviderDTO();
        serviceProviderDTO.setServiceName("testMOK6");
        serviceProviderDTO.setAddress("testMOK6");
        serviceProviderDTO.setNip("1231231231");
        serviceProviderDTO.setDescription("testMOK6");
        serviceProviderDTO.setLogoUrl("testMOK6");

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(serviceProviderDTO));

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void NoJWTNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        AddServiceProviderDTO serviceProviderDTO = new AddServiceProviderDTO();

        Response response = target
                .request()
                .put(Entity.json(serviceProviderDTO));

        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void NoRoleNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("test")));

        AddServiceProviderDTO serviceProviderDTO = new AddServiceProviderDTO();

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(serviceProviderDTO));

        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    public void InvalidDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        AddServiceProviderDTO serviceProviderDTO = new AddServiceProviderDTO();
        serviceProviderDTO.setServiceName("testMOK6");
        serviceProviderDTO.setAddress("testMOK6");
        serviceProviderDTO.setNip("123123123");
        serviceProviderDTO.setDescription("testMOK6");
        serviceProviderDTO.setLogoUrl("testMOK6");

        Response response = target
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(serviceProviderDTO));

        Assertions.assertEquals(400, response.getStatus());
    }
}
