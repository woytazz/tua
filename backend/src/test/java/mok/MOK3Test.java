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

public class MOK3Test {

    private String URL = "http://localhost:8080/api/mok/update/changeActive";

    private String JWTToken;

    @Test
    public void positiveTestActivateAccount() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRegister = client.target("http://localhost:8080/api/mok/create");
        WebTarget targetDeactivate = client.target(URL);
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));


        PostAdminDTO postAdminDTO = new PostAdminDTO();
        postAdminDTO.setLogin("tescikkkkkAdmin");
        postAdminDTO.setPassword("Test1234");
        postAdminDTO.setName("Adminekkkkk");
        postAdminDTO.setSurname("Adminekkkkkkk");
        postAdminDTO.setEmail("adminosskks@admin.pl");
        postAdminDTO.setPhoneNumber("113223140");

        String ETag1v = HMAC.calculateHMAC("tescikkkkkAdmin", 1L);

        targetRegister
                .path("/admin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(postAdminDTO));

        targetDeactivate
                .queryParam("login", "tescikkkkkAdmin")
                .queryParam("active", false)
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag1v)
                .put(Entity.json(""));

        String ETag2v = HMAC.calculateHMAC("tescikkkkkAdmin", 2L);

        Response response = target
                .queryParam("login", "tescikkkkkAdmin")
                .queryParam("active", true)
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag2v)
                .put(Entity.json(""));
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void activateUserUnauthorizedNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String ETag = HMAC.calculateHMAC("renter", 1L);

        Response response = target
                .queryParam("login", "renter")
                .queryParam("active", true)
                .queryParam("language", "pl")
                .request()
                .header("If-Match", ETag)
                .put(Entity.json(""));

        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void activateUserConflictWrongETagNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String ETag = HMAC.calculateHMAC("lol", 1L);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response response = target
                .queryParam("login", "renter")
                .queryParam("active", true)
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));
        Assertions.assertEquals(409, response.getStatus());
    }

}
