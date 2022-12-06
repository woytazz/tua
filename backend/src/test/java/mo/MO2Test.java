package mo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MO2Test {

    private String URL_READ = "http://localhost:8080/api/mo/view";

    @Test
    public void getAllOffersPositiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL_READ);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void getAllActiveOffersPositiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL_READ);

        Response response = target
                .path("/allActive")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void getOfferByIdPositiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL_READ);

        Response response = target
                .path("/byId")
                .queryParam("id", 1L)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void getOfferByIdNorFoundNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL_READ);

        Response response = target
                .path("/byId")
                .queryParam("id", 10000000000L)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(404, response.getStatus());
    }
}
