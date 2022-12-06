package mok;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostAdminDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

public class MOK2Test {

    private String URL = "http://localhost:8080/api/mok/create";

    private String JWTToken;

    @Test
    public void positiveTestCreateAdminAccount() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO postAdminDTO = new PostAdminDTO();
        postAdminDTO.setLogin("tescikAdmin");
        postAdminDTO.setPassword("Test1234");
        postAdminDTO.setName("Adminek");
        postAdminDTO.setSurname("Adminekkk");
        postAdminDTO.setEmail("admin@admin.pl");
        postAdminDTO.setPhoneNumber("123123129");

        Response response = target
                .path("/admin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(postAdminDTO));

        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    public void createAdminAccountUnauthorizedNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);


        PostAdminDTO postAdminDTO = new PostAdminDTO();
        postAdminDTO.setLogin("tescikAdmin");
        postAdminDTO.setPassword("Test1234");
        postAdminDTO.setName("Adminek");
        postAdminDTO.setSurname("Adminekkk");
        postAdminDTO.setEmail("admin@admin.pl");
        postAdminDTO.setPhoneNumber("123123129");

        Response response = target
                .path("/admin")
                .request()
                .post(Entity.json(postAdminDTO));

        Assertions.assertEquals(401, response.getStatus());
    }


    @Test
    public void createAdminAccountTakenEmailNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO postAdminDTO = new PostAdminDTO();
        postAdminDTO.setLogin("tescikAdmin");
        postAdminDTO.setPassword("Test1234");
        postAdminDTO.setName("Adminek");
        postAdminDTO.setSurname("Adminekkk");
        postAdminDTO.setEmail("ssbd01admin@ias.pl");
        postAdminDTO.setPhoneNumber("123123129");

        Response response = target
                .path("/admin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(postAdminDTO));

        Assertions.assertEquals(409, response.getStatus());
    }

    @Test
    public void createAdminAccountInvalidEmailNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        JWTToken = JWTGeneratorVerifier.generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        PostAdminDTO postAdminDTO = new PostAdminDTO();
        postAdminDTO.setLogin("tescikAdmin");
        postAdminDTO.setPassword("Test1234");
        postAdminDTO.setName("Adminek");
        postAdminDTO.setSurname("Adminekkk");
        postAdminDTO.setEmail("ssbd01adminias.pl");
        postAdminDTO.setPhoneNumber("123123129");

        Response response = target
                .path("/admin")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(postAdminDTO));

        Assertions.assertEquals(400, response.getStatus());
    }
}
