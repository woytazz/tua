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

public class MOK7Test {

    private String URL = "http://localhost:8080/api/mok/update/changeUserPassword";

    private String createAdminURL = "http://localhost:8080/api/mok/create/admin";

    private String JWTToken;

    @Test
    public void positiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO adminDTO = new PostAdminDTO();
        adminDTO.setLogin("MOK7Test1");
        adminDTO.setPassword("ValidPassword1");
        adminDTO.setName("MokTest");
        adminDTO.setSurname("MokTest");
        adminDTO.setEmail("MOK71@test.test");
        adminDTO.setPhoneNumber("123456321");

        WebTarget createNewUser = client.target(createAdminURL);

        createNewUser
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(adminDTO));

        String ETag = HMAC.calculateHMAC("MOK7Test1", 1L);

        Response response = target
                .queryParam("login", "MOK7Test1")
                .queryParam("newPassword", "NewValidPassword1")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void NoJWTNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String ETag = HMAC.calculateHMAC("MOK7Test", 1L);

        Response response = target
                .queryParam("login", "MOK7Test")
                .queryParam("newPassword", "NewValidPassword1")
                .request()
                .header("If-Match", ETag)
                .put(Entity.json(""));

        Assertions.assertEquals(401, response.getStatus());
    }

    @Test
    public void NoRoleNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("test")));

        String ETag = HMAC.calculateHMAC("MOK7Test", 1L);

        Response response = target
                .queryParam("login", "MOK7Test")
                .queryParam("newPassword", "NewValidPassword1")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    public void InvalidDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO adminDTO = new PostAdminDTO();
        adminDTO.setLogin("MOK7Test2");
        adminDTO.setPassword("ValidPassword1");
        adminDTO.setName("MokTest");
        adminDTO.setSurname("MokTest");
        adminDTO.setEmail("MOK7@test.test");
        adminDTO.setPhoneNumber("123456321");

        WebTarget createNewUser = client.target(createAdminURL);

        createNewUser
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(adminDTO));

        String ETag = HMAC.calculateHMAC("MOK7Test2", 1L);

        Response response = target
                .queryParam("login", "MOK7Test2")
                .queryParam("newPassword", "asd")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    public void OptimisticLockNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO adminDTO = new PostAdminDTO();
        adminDTO.setLogin("MOK7Test3");
        adminDTO.setPassword("ValidPassword1");
        adminDTO.setName("MokTest");
        adminDTO.setSurname("MokTest");
        adminDTO.setEmail("MOK7@test.test");
        adminDTO.setPhoneNumber("123456321");

        WebTarget createNewUser = client.target(createAdminURL);

        createNewUser
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(adminDTO));

        String ETag = HMAC.calculateHMAC("MOK7Test3", 2L);

        Response response = target
                .queryParam("login", "MOK7Test3")
                .queryParam("newPassword", "NewValidPassword1")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        Assertions.assertEquals(409, response.getStatus());
    }

}
