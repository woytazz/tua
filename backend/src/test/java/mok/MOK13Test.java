package mok;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

public class MOK13Test {

    private String URL_READ = "http://localhost:8080/api/mok/read";
    private String URL_UPDATE = "http://localhost:8080/api/mok/update";
    private String URL_DELETE = "http://localhost:8080/api/mok/delete";
    private String URL_CREATE = "http://localhost:8080/api/mok/create";

    private String JWTToken;

    @Test
    public void positiveTestDeleteServiceProvider() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetDelete = client.target(URL_DELETE);
        WebTarget targetCreate = client.target(URL_CREATE);

        PostServiceProviderDTO serviceProviderDTO = new PostServiceProviderDTO();
        serviceProviderDTO.setLogin("testerrrrr1234");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Testr");
        serviceProviderDTO.setSurname("Testorwskrrrrrrrrri");
        serviceProviderDTO.setEmail("teser@tersterrrrrrrr.pl");
        serviceProviderDTO.setPhoneNumber("123223133");
        serviceProviderDTO.setServiceName("testMOrrrrrrf5");
        serviceProviderDTO.setNip("1231231341");
        serviceProviderDTO.setAddress("Łódź, 95-100, aWółcazańska 100");
        serviceProviderDTO.setDescription("Halo halo ahaalo");
        serviceProviderDTO.setLogoUrl("http://zdjecie.laaol.lol.lol");

        targetCreate
                .path("/register-provider")
                .request()
                .post(Entity.json(serviceProviderDTO));

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response response = targetDelete
                .path("/deleteServiceProvider")
                .queryParam("login", "testerrrrr1234")
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .delete();

        Assertions.assertEquals(204, response.getStatus());
    }


    @Test
    public void deleteServiceProviderUserNotFoundNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetDelete = client.target(URL_DELETE);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response response = targetDelete
                .path("/deleteServiceProvider")
                .queryParam("login", "t1234")
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .delete();

        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    public void positiveTestReadServiceProviderDetails() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRead = client.target(URL_READ);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response response = targetRead
                .path("/providersToConfirm")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + JWTToken)
                .get();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void readServiceProvidersToConfirmUnauthorizedNegativeTest() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetRead = client.target(URL_READ);

        Response response = targetRead
                .path("/providersToConfirm")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(401, response.getStatus());
    }


    @Test
    public void positiveTestConfirmServiceProvider() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetUpdate = client.target(URL_UPDATE);
        WebTarget targetCreate = client.target(URL_CREATE);

        PostServiceProviderDTO serviceProviderDTO = new PostServiceProviderDTO();
        serviceProviderDTO.setLogin("rrrr1234");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Tetr");
        serviceProviderDTO.setSurname("Rrrrrrrrri");
        serviceProviderDTO.setEmail("teser@rrrrrrrr.pl");
        serviceProviderDTO.setPhoneNumber("123223133");
        serviceProviderDTO.setServiceName("tesrrrrrf5");
        serviceProviderDTO.setNip("1261231341");
        serviceProviderDTO.setAddress("Łódź, 95-100, aaaWółcazańska 100");
        serviceProviderDTO.setDescription("Halo halo ahaaaalo");
        serviceProviderDTO.setLogoUrl("http://zdjecie.aalaaol.lol.lol");

        targetCreate
                .path("/register-provider")
                .request()
                .post(Entity.json(serviceProviderDTO));

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));


        Response response = targetUpdate
                .path("/activateServiceProvider")
                .queryParam("login", "rrrr1234")
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(""));

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void confirmServiceProviderUnauthorizedNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetUpdate1 = client.target(URL_UPDATE);

        Response response = targetUpdate1
                .path("/activateServiceProvider")
                .queryParam("login", "rrrr1234")
                .queryParam("language", "pl")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(""));

        Assertions.assertEquals(401, response.getStatus());
    }

}
