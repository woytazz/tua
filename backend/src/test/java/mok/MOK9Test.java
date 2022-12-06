package mok;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostAdminDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutAccountDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MOK9Test {

    private String URL = "http://localhost:8080/api/mok/update";

    private String createRenterURL = "http://localhost:8080/api/mok/create/register-renter";

    private String createAdminURL = "http://localhost:8080/api/mok/create/admin";

    @Test
    public void positiveRenterTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        //TWORZENIE RENTERA

        WebTarget newRenter = client.target(createRenterURL);

        PostRenterDTO postRenterDTO = new PostRenterDTO();
        postRenterDTO.setLogin("MOK9Test1");
        postRenterDTO.setPassword("Test1234");
        postRenterDTO.setName("Test");
        postRenterDTO.setSurname("Test");
        postRenterDTO.setEmail("MOK9@tester.pl");
        postRenterDTO.setPhoneNumber("123123123");
        postRenterDTO.setUserName("testMOK9Renter");

        newRenter
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(postRenterDTO));

        //EDYCJA SAMEGO SIEBIE
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9Test1", new HashSet<>(List.of("Renter")));

        PutRenterDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("Test");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("123123123");
        putRenterDTO.setUserName("testMOK9Renter1");

        String ETag = HMAC.calculateHMAC("MOK9Test1", 1L) + "," + HMAC.calculateHMAC("MOK9Test1", 1L);

        Response response = target
                .path("/editRenter")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putRenterDTO));

        assertEquals(200, response.getStatus());
    }

    @Test
    public void NoJWTRenterNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String ETag = HMAC.calculateHMAC("MOK9Test1", 1L) + "," + HMAC.calculateHMAC("MOK9Test1", 1L);

        Response response = target
                .path("/editRenter")
                .request()
                .header("If-Match", ETag)
                .put(Entity.json(""));

        assertEquals(401, response.getStatus());
    }

    @Test
    public void NoRoleRenterNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9Test1", new HashSet<>(List.of("Test")));

        String ETag = HMAC.calculateHMAC("MOK9Test1", 1L) + "," + HMAC.calculateHMAC("MOK9Test1", 1L);

        Response response = target
                .path("/editRenter")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        assertEquals(403, response.getStatus());
    }

    @Test
    public void InvalidRenterDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9Test1", new HashSet<>(List.of("Renter")));

        PutRenterDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("9est");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("123123123");
        putRenterDTO.setUserName("testMOK9Renter1");

        String ETag = HMAC.calculateHMAC("MOK9Test1", 1L) + "," + HMAC.calculateHMAC("MOK9Test1", 1L);

        Response response = target
                .path("/editRenter")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putRenterDTO));

        assertEquals(400, response.getStatus());
    }

    @Test
    public void OptimisticLockRenterNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        //TWORZENIE RENTERA

        WebTarget newRenter = client.target(createRenterURL);

        PostRenterDTO postRenterDTO = new PostRenterDTO();
        postRenterDTO.setLogin("MOK9Test2");
        postRenterDTO.setPassword("Testasd1234");
        postRenterDTO.setName("Testt");
        postRenterDTO.setSurname("Testt");
        postRenterDTO.setEmail("MOK91@tester.pl");
        postRenterDTO.setPhoneNumber("223123123");
        postRenterDTO.setUserName("testsadMOK9Renter");

        newRenter
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(postRenterDTO));

        //EDYCJA SAMEGO SIEBIE
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9Test2", new HashSet<>(List.of("Renter")));

        PutRenterDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("Test");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("123153123");
        putRenterDTO.setUserName("testMOK9Renter1");

        String ETag = HMAC.calculateHMAC("MOK9Test2", 2L) + "," + HMAC.calculateHMAC("MOK9Test2", 2L);

        Response response = target
                .path("/editRenter")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putRenterDTO));

        assertEquals(409, response.getStatus());
    }

    //////////////////////////////////////////////////////////////////////////////////ADMIN

    @Test
    public void positiveAdminTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        //TWORZENIE ADMINA

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9Test", new HashSet<>(List.of("Admin")));

        WebTarget newRenter = client.target(createAdminURL);

        PostAdminDTO postRenterDTO = new PostAdminDTO();
        postRenterDTO.setLogin("MOK9TestAdmin1");
        postRenterDTO.setPassword("Tesasdt1234");
        postRenterDTO.setName("Tesasdt");
        postRenterDTO.setSurname("Tesasdt");
        postRenterDTO.setEmail("MOK9admin@tester.pl");
        postRenterDTO.setPhoneNumber("121212121");

        newRenter
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(postRenterDTO));

        //EDYCJA SAMEGO SIEBIE
        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9TestAdmin1", new HashSet<>(List.of("Admin")));

        PutAccountDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("Test");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("121212221");

        String ETag = HMAC.calculateHMAC("MOK9TestAdmin1", 1L);

        Response response = target
                .path("/editAdmin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putRenterDTO));

        assertEquals(200, response.getStatus());
    }

    @Test
    public void NoJWTAdminNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String ETag = HMAC.calculateHMAC("MOK9AdminTest1", 1L) + "," + HMAC.calculateHMAC("MOK9AdminTest1", 1L);

        Response response = target
                .path("/editAdmin")
                .request()
                .header("If-Match", ETag)
                .put(Entity.json(""));

        assertEquals(401, response.getStatus());
    }

    @Test
    public void NoRoleAdminNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9AdminTest1", new HashSet<>(List.of("Test")));

        String ETag = HMAC.calculateHMAC("MOK9AdminTest1", 1L) + "," + HMAC.calculateHMAC("MOK9AdminTest1", 1L);

        Response response = target
                .path("/editAdmin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        assertEquals(403, response.getStatus());
    }

    @Test
    public void InvalidAdminDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9AdminTest1", new HashSet<>(List.of("Admin")));

        PutAccountDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("9est");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("123123123");

        String ETag = HMAC.calculateHMAC("MOK9AdminTest1", 1L) + "," + HMAC.calculateHMAC("MOK9AdminTest1", 1L);

        Response response = target
                .path("/editAdmin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putRenterDTO));

        assertEquals(400, response.getStatus());
    }

    @Test
    public void OptimisticLockAdminNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        //TWORZENIE ADMINA

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9Test", new HashSet<>(List.of("Admin")));

        WebTarget newRenter = client.target(createAdminURL);

        PostAdminDTO postRenterDTO = new PostAdminDTO();
        postRenterDTO.setLogin("MOK9TestAdmin2");
        postRenterDTO.setPassword("Tesasdt1234");
        postRenterDTO.setName("Tesasdt");
        postRenterDTO.setSurname("Tesasdt");
        postRenterDTO.setEmail("MOK9admin2@tester.pl");
        postRenterDTO.setPhoneNumber("111112121");

        newRenter
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(postRenterDTO));

        //EDYCJA SAMEGO SIEBIE
        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK9TestAdmin2", new HashSet<>(List.of("Admin")));

        PutAccountDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("Test");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("111112121");

        String ETag = HMAC.calculateHMAC("MOK9TestAdmin2", 2L);

        Response response = target
                .path("/editAdmin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putRenterDTO));

        assertEquals(409, response.getStatus());
    }
}