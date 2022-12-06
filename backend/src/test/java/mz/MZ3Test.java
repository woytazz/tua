package mz;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mz.OfferViewDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.security.JWTGeneratorVerifier;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MZ3Test {

    private String editByServiceOfferURL = "http://localhost:8080/api/mz/offer/editByService";

    private String editByAdminOfferURL = "http://localhost:8080/api/mz/offer/editByAdmin";

    private String addOfferURL = "http://localhost:8080/api/mz/offer/add";

    private String readOffersURL = "http://localhost:8080/api/mo/view";

    ///////////////////////EDIT SERVICEPROVIDER

    @Test
    public void positiveTests() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //DODAJ NOWA OFERTE

        WebTarget addOffer = client.target(addOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMZ3");
        offerDTO.setPrice(123);
        offerDTO.setTitle("test");

        addOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));


        //POBIERZ UTWORZONA OFERTE

        WebTarget readOffers = client.target(readOffersURL);

        Response readResponseOffers = readOffers
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = readResponseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();


        //USUN EDYTUJ OFERTE

        WebTarget editByServiceTarget = client.target(editByServiceOfferURL);

        OfferDTO editOfferDTO = new OfferDTO();
        editOfferDTO.setDescription("testMZ3test");
        editOfferDTO.setPrice(123);
        editOfferDTO.setTitle("test");

        String ETag = HMAC.calculateHMAC(id, 1L);

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", id)
                .request()
                .header("If-Match", ETag)
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(editOfferDTO));

        assertEquals(200, deleteResponseOffer.getStatus());
    }

    @Test
    public void NoJWTServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //USUN EDYTUJ OFERTE

        WebTarget editByServiceTarget = client.target(editByServiceOfferURL);

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", 3)
                .request()
                .put(Entity.json(""));

        assertEquals(401, deleteResponseOffer.getStatus());
    }

    @Test
    public void NoRoleServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("test")));

        WebTarget editByServiceTarget = client.target(editByServiceOfferURL);

        OfferDTO editOfferDTO = new OfferDTO();
        editOfferDTO.setDescription("testMZ3test");
        editOfferDTO.setPrice(123);

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", 3)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(editOfferDTO));

        assertEquals(403, deleteResponseOffer.getStatus());
    }

    @Test
    public void InvalidDataServiceNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //DODAJ NOWA OFERTE

        WebTarget addOffer = client.target(addOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMZ3");
        offerDTO.setPrice(123);
        offerDTO.setTitle("test");

        addOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));


        //POBIERZ UTWORZONA OFERTE

        WebTarget readOffers = client.target(readOffersURL);

        Response readResponseOffers = readOffers
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = readResponseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();


        //USUN EDYTUJ OFERTE

        WebTarget editByServiceTarget = client.target(editByServiceOfferURL);

        OfferDTO editOfferDTO = new OfferDTO();
        editOfferDTO.setDescription("testMZ3test");
        editOfferDTO.setPrice(-123);

        String ETag = HMAC.calculateHMAC(id, 1L);

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", id)
                .request()
                .header("If-Match", ETag)
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(editOfferDTO));

        assertEquals(400, deleteResponseOffer.getStatus());
    }

    @Test
    public void OptimisticLockServiceNegativeTests() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //DODAJ NOWA OFERTE

        WebTarget addOffer = client.target(addOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMZ3");
        offerDTO.setPrice(123);
        offerDTO.setTitle("test");

        addOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));


        //POBIERZ UTWORZONA OFERTE

        WebTarget readOffers = client.target(readOffersURL);

        Response readResponseOffers = readOffers
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = readResponseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();


        //USUN EDYTUJ OFERTE

        WebTarget editByServiceTarget = client.target(editByServiceOfferURL);

        OfferDTO editOfferDTO = new OfferDTO();
        editOfferDTO.setDescription("testMZ3test");
        editOfferDTO.setPrice(123);
        editOfferDTO.setTitle("test");

        String ETag = HMAC.calculateHMAC(id, 2L);

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", id)
                .request()
                .header("If-Match", ETag)
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(editOfferDTO));

        assertEquals(409, deleteResponseOffer.getStatus());
    }

    /////////////////////////////////////////ADMIN

    @Test
    public void positiveAdminTests() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //DODAJ NOWA OFERTE

        WebTarget addOffer = client.target(addOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMZ3");
        offerDTO.setPrice(123);
        offerDTO.setTitle("test");

        addOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));


        //POBIERZ UTWORZONA OFERTE

        WebTarget readOffers = client.target(readOffersURL);

        Response readResponseOffers = readOffers
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = readResponseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();


        //USUN EDYTUJ OFERTE

        WebTarget editByServiceTarget = client.target(editByAdminOfferURL);

        OfferDTO editOfferDTO = new OfferDTO();
        editOfferDTO.setDescription("testMZ3test");
        editOfferDTO.setPrice(123);
        editOfferDTO.setTitle("test");

        String ETag = HMAC.calculateHMAC(id, 1L);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", id)
                .request()
                .header("If-Match", ETag)
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(editOfferDTO));

        assertEquals(200, deleteResponseOffer.getStatus());
    }

    @Test
    public void NoJWTAdminNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        //USUN EDYTUJ OFERTE

        WebTarget editByServiceTarget = client.target(editByAdminOfferURL);

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", 3)
                .request()
                .put(Entity.json(""));

        assertEquals(401, deleteResponseOffer.getStatus());
    }

    @Test
    public void NoRoleAdminNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("test")));

        WebTarget editByServiceTarget = client.target(editByAdminOfferURL);

        OfferDTO editOfferDTO = new OfferDTO();
        editOfferDTO.setDescription("testMZ3test");
        editOfferDTO.setPrice(123);

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", 3)
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(editOfferDTO));

        assertEquals(403, deleteResponseOffer.getStatus());
    }

    @Test
    public void InvalidDataAdminNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //DODAJ NOWA OFERTE

        WebTarget addOffer = client.target(addOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMZ3");
        offerDTO.setPrice(123);
        offerDTO.setTitle("test");

        addOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));


        //POBIERZ UTWORZONA OFERTE

        WebTarget readOffers = client.target(readOffersURL);

        Response readResponseOffers = readOffers
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = readResponseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();


        //USUN EDYTUJ OFERTE

        WebTarget editByServiceTarget = client.target(editByAdminOfferURL);

        OfferDTO editOfferDTO = new OfferDTO();
        editOfferDTO.setDescription("testMZ3test");
        editOfferDTO.setPrice(-123);

        String ETag = HMAC.calculateHMAC(id, 1L);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", id)
                .request()
                .header("If-Match", ETag)
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(editOfferDTO));

        assertEquals(400, deleteResponseOffer.getStatus());
    }

    @Test
    public void OptimisticLockAdminNegativeTests() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();


        //DODAJ NOWA OFERTE

        WebTarget addOffer = client.target(addOfferURL);

        String JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("serviceProvider", new HashSet<>(List.of("ServiceProvider")));

        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("testMZ3");
        offerDTO.setPrice(123);
        offerDTO.setTitle("test");

        addOffer
                .request()
                .header("Authorization", "Bearer " + JWTToken)
                .post(Entity.json(offerDTO));


        //POBIERZ UTWORZONA OFERTE

        WebTarget readOffers = client.target(readOffersURL);

        Response readResponseOffers = readOffers
                .request(MediaType.APPLICATION_JSON)
                .get();

        List<OfferViewDTO> offers = readResponseOffers.readEntity(new GenericType<>() {
        });

        Long id = offers.get(offers.size() - 1).getId();


        //USUN EDYTUJ OFERTE

        WebTarget editByServiceTarget = client.target(editByAdminOfferURL);

        OfferDTO editOfferDTO = new OfferDTO();
        editOfferDTO.setDescription("testMZ3test");
        editOfferDTO.setPrice(123);
        editOfferDTO.setTitle("test");

        String ETag = HMAC.calculateHMAC(id, 2L);

        JWTToken = JWTGeneratorVerifier
                .generateAccessJWTString("admin", new HashSet<>(List.of("Admin")));

        Response deleteResponseOffer = editByServiceTarget
                .path("/{id}")
                .resolveTemplate("id", id)
                .request()
                .header("If-Match", ETag)
                .header("Authorization", "Bearer " + JWTToken)
                .put(Entity.json(editOfferDTO));

        assertEquals(409, deleteResponseOffer.getStatus());
    }

}
