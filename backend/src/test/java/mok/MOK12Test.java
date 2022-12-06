package mok;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutProviderDTO;
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

public class MOK12Test {

    private String createServiceURL = "http://localhost:8080/api/mok/create/register-provider";

    private String editServiceURL = "http://localhost:8080/api/mok/update/editServiceProvider";

    @Test
    public void positiveRenterTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //TWORZENIE SERVICEPROVIDERA

        WebTarget newRenter = client.target(createServiceURL);

        PostServiceProviderDTO serviceProviderDTO = new PostServiceProviderDTO();
        serviceProviderDTO.setLogin("MOK12Test1");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Test");
        serviceProviderDTO.setSurname("Test");
        serviceProviderDTO.setEmail("MOK12@test.test");
        serviceProviderDTO.setPhoneNumber("121212121");
        serviceProviderDTO.setServiceName("testMOK12");
        serviceProviderDTO.setNip("1212121212");
        serviceProviderDTO.setAddress("TEST12");
        serviceProviderDTO.setDescription("Halo halo halo");
        serviceProviderDTO.setLogoUrl("test");

        newRenter
                .request()
                .post(Entity.json(serviceProviderDTO));

        //EDYCJA SERVIEPROVIDERA

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK12Test1", new HashSet<>(List.of("ServiceProvider")));

        PutProviderDTO putServiceDTO = new PutProviderDTO();
        putServiceDTO.setName("Testedit");
        putServiceDTO.setSurname("Testedit");
        putServiceDTO.setPhoneNumber("121212121");
        putServiceDTO.setAddress("testMOK12");
        putServiceDTO.setDescription("test");
        putServiceDTO.setLogoUrl("test");

        String ETag = HMAC.calculateHMAC("MOK12Test1", 1L) + "," + HMAC.calculateHMAC("MOK12Test1", 1L);

        WebTarget editService = client.target(editServiceURL);

        Response response = editService
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putServiceDTO));

        assertEquals(200, response.getStatus());

    }

    @Test
    public void NoJWTServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //EDYCJA SERVIEPROVIDERA

        String ETag = HMAC.calculateHMAC("MOK12Test1", 1L) + "," + HMAC.calculateHMAC("MOK12Test1", 1L);

        WebTarget editService = client.target(editServiceURL);

        Response response = editService
                .request()
                .header("If-Match", ETag)
                .put(Entity.json(""));

        assertEquals(401, response.getStatus());

    }

    @Test
    public void NoRoleServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //EDYCJA SERVIEPROVIDERA

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK12Test1", new HashSet<>(List.of("test")));

        String ETag = HMAC.calculateHMAC("MOK12Test1", 1L) + "," + HMAC.calculateHMAC("MOK12Test1", 1L);

        WebTarget editService = client.target(editServiceURL);

        Response response = editService
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(""));

        assertEquals(403, response.getStatus());

    }

    @Test
    public void InvalidServiceDataNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //EDYCJA SERVIEPROVIDERA

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK12Test1", new HashSet<>(List.of("ServiceProvider")));

        PutProviderDTO putServiceDTO = new PutProviderDTO();
        putServiceDTO.setName("9estedit");
        putServiceDTO.setSurname("Testedit");
        putServiceDTO.setPhoneNumber("1212121211");
        putServiceDTO.setAddress("testMOK12");
        putServiceDTO.setDescription("test");
        putServiceDTO.setLogoUrl("test");

        String ETag = HMAC.calculateHMAC("MOK12Test1", 1L) + "," + HMAC.calculateHMAC("MOK12Test1", 1L);

        WebTarget editService = client.target(editServiceURL);

        Response response = editService
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putServiceDTO));

        assertEquals(400, response.getStatus());

    }

    @Test
    public void OptimisticLockServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //TWORZENIE SERVICEPROVIDERA

        WebTarget newRenter = client.target(createServiceURL);

        PostServiceProviderDTO serviceProviderDTO = new PostServiceProviderDTO();
        serviceProviderDTO.setLogin("MOK12Test2");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Test");
        serviceProviderDTO.setSurname("Test");
        serviceProviderDTO.setEmail("MOK122@test.test");
        serviceProviderDTO.setPhoneNumber("121212122");
        serviceProviderDTO.setServiceName("testMOK122");
        serviceProviderDTO.setNip("1212121211");
        serviceProviderDTO.setAddress("TEST122");
        serviceProviderDTO.setDescription("Halo halo halo");
        serviceProviderDTO.setLogoUrl("test");

        newRenter
                .request()
                .post(Entity.json(serviceProviderDTO));

        //EDYCJA SERVIEPROVIDERA

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("MOK12Test2", new HashSet<>(List.of("ServiceProvider")));

        PutProviderDTO putServiceDTO = new PutProviderDTO();
        putServiceDTO.setName("Testedit");
        putServiceDTO.setSurname("Testedit");
        putServiceDTO.setPhoneNumber("121212121");
        putServiceDTO.setAddress("testMOK12");
        putServiceDTO.setDescription("test");
        putServiceDTO.setLogoUrl("test");

        String ETag = HMAC.calculateHMAC("MOK12Test2", 2L) + "," + HMAC.calculateHMAC("MOK12Test2", 2L);

        WebTarget editService = client.target(editServiceURL);

        Response response = editService
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putServiceDTO));

        assertEquals(409, response.getStatus());

    }


}
