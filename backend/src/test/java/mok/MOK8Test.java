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

public class MOK8Test {

    private String URL = "http://localhost:8080/api/mok/update/changeUserOwnPassword";

    private String createAdminURL = "http://localhost:8080/api/mok/create/admin";

    private String JWTToken;

    @Test
    public void positiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        //TWORZENIE NOWEGO UZYTKOWNKA
        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO adminDTO = new PostAdminDTO();
        adminDTO.setLogin("MOK8Test1");
        adminDTO.setPassword("ValidPassword1");
        adminDTO.setName("MokTest");
        adminDTO.setSurname("MokTest");
        adminDTO.setEmail("MOK81@test.test");
        adminDTO.setPhoneNumber("123456321");

        WebTarget createNewUser = client.target(createAdminURL);

        createNewUser
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(adminDTO));

        //ZMIANA HASŁA
        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK8Test1", new HashSet<>(List.of("Admin")));

        String ETag = HMAC.calculateHMAC("MOK8Test1", 1L);

        Response response = target
                .queryParam("oldPassword", "ValidPassword1")
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

        String ETag = HMAC.calculateHMAC("MOK8Test1", 1L);

        Response response = target
                .queryParam("oldPassword", "ValidPassword1")
                .queryParam("newPassword", "NewValidPassword1")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
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
                .generateAccessJWTString("MOK8Test1", new HashSet<>(List.of("test")));

        String ETag = HMAC.calculateHMAC("MOK8Test1", 1L);

        Response response = target
                .queryParam("oldPassword", "ValidPassword1")
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

        //TWORZENIE NOWEGO UZYTKOWNKA
        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO adminDTO = new PostAdminDTO();
        adminDTO.setLogin("MOK8Test1");
        adminDTO.setPassword("ValidPassword1");
        adminDTO.setName("MokTest");
        adminDTO.setSurname("MokTest");
        adminDTO.setEmail("MOK8@test.test");
        adminDTO.setPhoneNumber("123456321");

        WebTarget createNewUser = client.target(createAdminURL);

        createNewUser
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(adminDTO));

        //ZMIANA HASŁA
        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK8Test1", new HashSet<>(List.of("Admin")));

        String ETag = HMAC.calculateHMAC("MOK8Test1", 1L);

        Response response = target
                .queryParam("oldPassword", "ValidPassword1")
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

        //TWORZENIE NOWEGO UZYTKOWNKA
        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO adminDTO = new PostAdminDTO();
        adminDTO.setLogin("MOK8Test3");
        adminDTO.setPassword("ValidPassword1");
        adminDTO.setName("MokTest");
        adminDTO.setSurname("MokTest");
        adminDTO.setEmail("MOK8@test.test");
        adminDTO.setPhoneNumber("123456321");

        WebTarget createNewUser = client.target(createAdminURL);

        createNewUser
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(adminDTO));

        //ZMIANA HASŁA
        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK8Test3", new HashSet<>(List.of("Admin")));

        String ETag = HMAC.calculateHMAC("MOK8Test3", 2L);

        Response response = target
                .queryParam("oldPassword", "ValidPassword1")
                .queryParam("newPassword", "NewValidPassword1")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        Assertions.assertEquals(409, response.getStatus());
    }
}
