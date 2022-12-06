package mok;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostAdminDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

public class MOK4Test {

    private String URL = "http://localhost:8080/api/mok/update/changeActive";

    private String JWTToken;

    @Test
    public void positiveTestDeactivateAccount() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRegister = client.target("http://localhost:8080/api/mok/create");
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        String ETag = HMAC.calculateHMAC("tescikkAdmin", 1L);

        PostAdminDTO postAdminDTO = new PostAdminDTO();
        postAdminDTO.setLogin("tescikkAdmin");
        postAdminDTO.setPassword("Test1234");
        postAdminDTO.setName("Adminekk");
        postAdminDTO.setSurname("Adminekkkk");
        postAdminDTO.setEmail("adminosss@admin.pl");
        postAdminDTO.setPhoneNumber("123123112");

        targetRegister
                .path("/admin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(postAdminDTO));

        Response response = target
                .queryParam("login", "tescikkAdmin")
                .queryParam("active", false)
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void deactivateUserUnauthorizedNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String ETag = HMAC.calculateHMAC("renter", 1L);

        Response response = target
                .queryParam("login", "renter")
                .queryParam("active", false)
                .queryParam("language", "pl")
                .request()
                .header("If-Match", ETag)
                .put(Entity.json(""));
        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void deactivateUserConflictWrongETagNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String ETag = HMAC.calculateHMAC("lol", 1L);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response response = target
                .queryParam("login", "renter")
                .queryParam("active", false)
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));
        Assertions.assertEquals(409, response.getStatus());

    }

    @Test
    public void deactivateUserConflictUserAlreadyInactiveNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRegister = client.target("http://localhost:8080/api/mok/create");
        WebTarget target = client.target(URL);
        WebTarget targetDeactivate = client.target(URL);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));


        PostAdminDTO postAdminDTO = new PostAdminDTO();
        postAdminDTO.setLogin("tescikkkAdmin");
        postAdminDTO.setPassword("Test1234");
        postAdminDTO.setName("Adminekkk");
        postAdminDTO.setSurname("Adminekkkkk");
        postAdminDTO.setEmail("adminossks@admin.pl");
        postAdminDTO.setPhoneNumber("123123140");

        String ETag = HMAC.calculateHMAC("tescikkkAdmin", 1L);

        targetRegister
                .path("/admin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(postAdminDTO));

        targetDeactivate.
                queryParam("login", "tescikkkAdmin")
                .queryParam("active", false)
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        Response response = target
                .queryParam("login", "tescikkkAdmin")
                .queryParam("active", false)
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));
        Assertions.assertEquals(409, response.getStatus());
    }
}

