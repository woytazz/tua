package mok;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.post.PostServiceProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutProviderDTO;
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

public class MOK10Test {

    private String createRenterURL = "http://localhost:8080/api/mok/create/register-renter";

    private String createServiceURL = "http://localhost:8080/api/mok/create/register-provider";

    private String editRenterURL = "http://localhost:8080/api/mok/update/editRenter";

    private String editServiceURL = "http://localhost:8080/api/mok/update/editServiceProvider";

    @Test
    public void positiveRenterTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //TWORZENIE RENTERA

        WebTarget newRenter = client.target(createRenterURL);

        PostRenterDTO postRenterDTO = new PostRenterDTO();
        postRenterDTO.setLogin("MOK10Test1");
        postRenterDTO.setPassword("Test1234");
        postRenterDTO.setName("Test");
        postRenterDTO.setSurname("Test");
        postRenterDTO.setEmail("MOK10@test.test");
        postRenterDTO.setPhoneNumber("101010101");
        postRenterDTO.setUserName("testMOK10Renter");

        newRenter
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(postRenterDTO));

        //EDYCJA RENTERA
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("ADMIN", new HashSet<>(List.of("Admin")));

        PutRenterDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("Test");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("101010101");
        putRenterDTO.setUserName("testMOK10RenterEdit");

        String ETag = HMAC.calculateHMAC("MOK10Test1", 1L) + "," + HMAC.calculateHMAC("MOK10Test1", 1L);

        WebTarget editRenter = client.target(editRenterURL);

        Response response = editRenter
                .path("/MOK10Test1")
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

        //EDYCJA RENTERA

        String ETag = HMAC.calculateHMAC("MOK10Test1", 1L) + "," + HMAC.calculateHMAC("MOK10Test1", 1L);

        WebTarget editRenter = client.target(editRenterURL);

        Response response = editRenter
                .path("/MOK10Test1")
                .request()
                .header("If-Match", ETag)
                .put(Entity.json(""));

        assertEquals(401, response.getStatus());
    }

    @Test
    public void NoRoleRenterNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //EDYCJA RENTERA
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("ADMIN", new HashSet<>(List.of("test")));

        String ETag = HMAC.calculateHMAC("MOK10Test1", 1L) + "," + HMAC.calculateHMAC("MOK10Test1", 1L);

        WebTarget editRenter = client.target(editRenterURL);

        Response response = editRenter
                .path("/MOK10Test1")
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

        //EDYCJA RENTERA
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("ADMIN", new HashSet<>(List.of("Admin")));

        PutRenterDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("3Test");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("101010101");
        putRenterDTO.setUserName("testMOK10RenterEdit");

        String ETag = HMAC.calculateHMAC("MOK10Test1", 1L) + "," + HMAC.calculateHMAC("MOK10Test1", 1L);

        WebTarget editRenter = client.target(editRenterURL);

        Response response = editRenter
                .path("/MOK10Test1")
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

        //TWORZENIE RENTERA

        WebTarget newRenter = client.target(createRenterURL);

        PostRenterDTO postRenterDTO = new PostRenterDTO();
        postRenterDTO.setLogin("MOK10Test2");
        postRenterDTO.setPassword("Test1234");
        postRenterDTO.setName("Test");
        postRenterDTO.setSurname("Test");
        postRenterDTO.setEmail("MOK102@test.test");
        postRenterDTO.setPhoneNumber("101010102");
        postRenterDTO.setUserName("testMOK10Renter");

        newRenter
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(postRenterDTO));

        //EDYCJA RENTERA
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("ADMIN", new HashSet<>(List.of("Admin")));

        PutRenterDTO putRenterDTO = new PutRenterDTO();
        putRenterDTO.setName("Test");
        putRenterDTO.setSurname("Test");
        putRenterDTO.setPhoneNumber("101010102");
        putRenterDTO.setUserName("testMOK10RenterEdit");

        String ETag = HMAC.calculateHMAC("MOK10Test2", 2L) + "," + HMAC.calculateHMAC("MOK10Test2", 2L);

        WebTarget editRenter = client.target(editRenterURL);

        Response response = editRenter
                .path("/MOK10Test2")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putRenterDTO));

        assertEquals(409, response.getStatus());
    }

    /////////////////////////////////////////////////////////////////////SERVICPROVIDER

    @Test
    public void positiveServiceProviderTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //TWORZENIE SERVICEPROVIDERA

        WebTarget newRenter = client.target(createServiceURL);

        PostServiceProviderDTO serviceProviderDTO = new PostServiceProviderDTO();
        serviceProviderDTO.setLogin("MOK103Test1");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Test");
        serviceProviderDTO.setSurname("Test");
        serviceProviderDTO.setEmail("MOK103@test.test");
        serviceProviderDTO.setPhoneNumber("101010103");
        serviceProviderDTO.setServiceName("testMOK101");
        serviceProviderDTO.setNip("1010101010");
        serviceProviderDTO.setAddress("TEST1");
        serviceProviderDTO.setDescription("Halo halo halo");
        serviceProviderDTO.setLogoUrl("test");

        newRenter
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(serviceProviderDTO));

        //EDYCJA SERVICEPROVIDERA
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("ADMIN", new HashSet<>(List.of("Admin")));

        PutProviderDTO putServiceDTO = new PutProviderDTO();
        putServiceDTO.setName("Test");
        putServiceDTO.setSurname("Test");
        putServiceDTO.setPhoneNumber("101010101");
        putServiceDTO.setAddress("testMOK103");
        putServiceDTO.setDescription("test");
        putServiceDTO.setLogoUrl("test");

        String ETag = HMAC.calculateHMAC("MOK103Test1", 1L) + "," + HMAC.calculateHMAC("MOK103Test1", 1L);

        WebTarget editRenter = client.target(editServiceURL);

        Response response = editRenter
                .path("/MOK103Test1")
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

        //EDYCJA SERVICEPROVIDERA

        String ETag = HMAC.calculateHMAC("MOK103Test1", 1L) + "," + HMAC.calculateHMAC("MOK103Test1", 1L);

        WebTarget editRenter = client.target(editServiceURL);

        Response response = editRenter
                .path("/MOK103Test1")
                .request()
                .header("If-Match", ETag)
                .put(Entity.json(""));

        assertEquals(401, response.getStatus());
    }

    @Test
    public void NoRoleServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //EDYCJA SERVICEPROVIDERA
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("ADMIN", new HashSet<>(List.of("test")));

        String ETag = HMAC.calculateHMAC("MOK103Test1", 1L) + "," + HMAC.calculateHMAC("MOK103Test1", 1L);

        WebTarget editRenter = client.target(editServiceURL);

        Response response = editRenter
                .path("/MOK103Test1")
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

        //EDYCJA SERVICEPROVIDERA
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("ADMIN", new HashSet<>(List.of("Admin")));

        PutProviderDTO putServiceDTO = new PutProviderDTO();
        putServiceDTO.setName("3est");
        putServiceDTO.setSurname("Test");
        putServiceDTO.setPhoneNumber("101010101");
        putServiceDTO.setAddress("tes");
        putServiceDTO.setDescription("test");
        putServiceDTO.setLogoUrl("test");

        String ETag = HMAC.calculateHMAC("MOK103Test1", 1L) + "," + HMAC.calculateHMAC("MOK103Test1", 1L);

        WebTarget editRenter = client.target(editServiceURL);

        Response response = editRenter
                .path("/MOK103Test1")
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
        serviceProviderDTO.setLogin("MOK104Test1");
        serviceProviderDTO.setPassword("Test1234");
        serviceProviderDTO.setName("Test");
        serviceProviderDTO.setSurname("Test");
        serviceProviderDTO.setEmail("MOK104@test.test");
        serviceProviderDTO.setPhoneNumber("101010104");
        serviceProviderDTO.setServiceName("testMOK104");
        serviceProviderDTO.setNip("1010101014");
        serviceProviderDTO.setAddress("TEST4");
        serviceProviderDTO.setDescription("Halo halo halo");
        serviceProviderDTO.setLogoUrl("test");

        newRenter
                .queryParam("language", "pl")
                .request()
                .post(Entity.json(serviceProviderDTO));

        //EDYCJA SERVICEPROVIDERA
        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("ADMIN", new HashSet<>(List.of("Admin")));

        PutProviderDTO putServiceDTO = new PutProviderDTO();
        putServiceDTO.setName("Test");
        putServiceDTO.setSurname("Test");
        putServiceDTO.setPhoneNumber("101010104");
        putServiceDTO.setAddress("testMOK104");
        putServiceDTO.setDescription("test");
        putServiceDTO.setLogoUrl("test");

        String ETag = HMAC.calculateHMAC("MOK104Test1", 2L) + "," + HMAC.calculateHMAC("MOK104Test1", 2L);

        WebTarget editRenter = client.target(editServiceURL);

        Response response = editRenter
                .path("/MOK104Test1")
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .header("If-Match", ETag)
                .put(Entity.json(putServiceDTO));

        assertEquals(409, response.getStatus());
    }

}
