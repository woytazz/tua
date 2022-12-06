package mok;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostServiceProviderDTO;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class MOK1Test {

    private String URL = "http://localhost:8080/api/mok/create";

    @Test
    public void positiveTestRegisterServiceProvider() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        PostServiceProviderDTO serviceProviderDTO = new PostServiceProviderDTO();
        serviceProviderDTO.setLogin("testerr1234");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Testrer");
        serviceProviderDTO.setSurname("Testorwski");
        serviceProviderDTO.setEmail("teser@terster.pl");
        serviceProviderDTO.setPhoneNumber("123223133");
        serviceProviderDTO.setServiceName("testMOf5");
        serviceProviderDTO.setNip("1231231431");
        serviceProviderDTO.setAddress("Łódź, 95-100, aWółczańska 100");
        serviceProviderDTO.setDescription("Halo halo ahalo");
        serviceProviderDTO.setLogoUrl("http://zdjecie.laol.lol.lol");

        Response response = target
                .path("/register-provider")
                .request()
                .post(Entity.json(serviceProviderDTO));

        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    public void positiveTestRegisterRenter() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        PostRenterDTO postRenterDTO = new PostRenterDTO();
        postRenterDTO.setLogin("testerRenter1234");
        postRenterDTO.setPassword("Test1234");
        postRenterDTO.setName("Testerr");
        postRenterDTO.setSurname("Testowskii");
        postRenterDTO.setEmail("teser2@tester.pl");
        postRenterDTO.setPhoneNumber("123222123");
        postRenterDTO.setUserName("testMOK5Renter");

        Response response = target
                .path("/register-renter")
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(postRenterDTO));

        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    public void RegisterServiceProviderTakenEmailNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        PostServiceProviderDTO serviceProviderDTO = new PostServiceProviderDTO();
        serviceProviderDTO.setLogin("tester1234");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Tester");
        serviceProviderDTO.setSurname("Testowski");
        serviceProviderDTO.setEmail("ssbd01serviceprovider@ias.pl");
        serviceProviderDTO.setPhoneNumber("123123123");
        serviceProviderDTO.setServiceName("testMOK5");
        serviceProviderDTO.setNip("1231231232");
        serviceProviderDTO.setAddress("Łódź, 95-100, Wółczańska 100");
        serviceProviderDTO.setDescription("Halo halo halo");
        serviceProviderDTO.setLogoUrl("http://zdjecie.lol.lol.lol");

        Response response = target
                .path("/register-provider")
                .request()
                .post(Entity.json(serviceProviderDTO));

        Assertions.assertEquals(409, response.getStatus());
    }


    @Test
    public void RegisterServiceProviderInvalidEmailNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        PostServiceProviderDTO serviceProviderDTO = new PostServiceProviderDTO();
        serviceProviderDTO.setLogin("testerr1234");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Testerr");
        serviceProviderDTO.setSurname("Testowskir");
        serviceProviderDTO.setEmail("tesertester.pl");
        serviceProviderDTO.setPhoneNumber("123123125");
        serviceProviderDTO.setServiceName("testMOK11111");
        serviceProviderDTO.setNip("1231231235");
        serviceProviderDTO.setAddress("Łódź, 95-100, Wółczańsk 100");
        serviceProviderDTO.setDescription("Halo halo hal");
        serviceProviderDTO.setLogoUrl("http://zdjecie.lol.lol.lo");

        Response response = target
                .path("/register-provider")
                .request()
                .post(Entity.json(serviceProviderDTO));

        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    public void RegisterRenterInvalidEmailNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        PostRenterDTO postRenterDTO = new PostRenterDTO();
        postRenterDTO.setLogin("testerRenter1234");
        postRenterDTO.setPassword("Test1234");
        postRenterDTO.setName("Testerr");
        postRenterDTO.setSurname("Testowskii");
        postRenterDTO.setEmail("teser2tester.pl");
        postRenterDTO.setPhoneNumber("123123123");
        postRenterDTO.setUserName("testMOK5Renter");

        Response response = target
                .path("/register-renter")
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(postRenterDTO));

        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    public void RegisterRenterTakenEmailNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL);

        PostRenterDTO postRenterDTO = new PostRenterDTO();
        postRenterDTO.setLogin("testerRenter1234");
        postRenterDTO.setPassword("Test1234");
        postRenterDTO.setName("Testerr");
        postRenterDTO.setSurname("Testowskii");
        postRenterDTO.setEmail("ssbd01renter@ias.pl");
        postRenterDTO.setPhoneNumber("123123123");
        postRenterDTO.setUserName("testMOK5Renter");

        Response response = target
                .path("/register-renter")
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(postRenterDTO));

        Assertions.assertEquals(409, response.getStatus());
    }

}
