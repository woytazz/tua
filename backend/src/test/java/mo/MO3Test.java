package mo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MO3Test {

    private String URL_READ = "http://localhost:8080/api/mo/view";

    @Test
    public void getOffersFilteredByPricePositiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL_READ);

        Response response = target
                .path("/byPrice")
                .queryParam("minPrice", 100)
                .queryParam("maxPrice", 200)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void getOffersFilteredWhenPriceEquals0PositiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget target = client.target(URL_READ);

        Response response = target
                .path("/byPrice")
                .queryParam("minPrice", 0)
                .queryParam("maxPrice", 0)
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(200, response.getStatus());
    }
}
