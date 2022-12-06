package mok;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MOK15Test {

    private String URL_UPDATE = "http://localhost:8080/api/mok/update";

    @Test
    public void sendResetPasswordMailPositiveTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetUpdate1 = client.target(URL_UPDATE);

        Response response = targetUpdate1
                .path("/password-reset-email-send/")
                .queryParam("email", "ssbd01admin@ias.pl")
                .queryParam("language", "pl")
                .request()
                .put(Entity.json(""));

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    public void sendResetPasswordMailEmailNotFoundNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetUpdate1 = client.target(URL_UPDATE);

        Response response = targetUpdate1
                .path("/password-reset-email-send/")
                .queryParam("email", "sadmin@i.l")
                .queryParam("language", "pl")
                .request()
                .put(Entity.json(""));

        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    public void confirmResetPasswordInvalidTokenSeeOtherTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetUpdate1 = client.target(URL_UPDATE);

        Response response = targetUpdate1
                .path("/password-reset-email-confirm/{token}")
                .resolveTemplate("token", "123123")
                .request(MediaType.APPLICATION_JSON)
                .get();

        Assertions.assertEquals(303, response.getStatus());
    }

    @Test
    public void updatePasswordInvalidTokenNegativeTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget targetUpdate1 = client.target(URL_UPDATE);

        Response response = targetUpdate1
                .path("/password-reset")
                .queryParam("token", "213123")
                .queryParam("newPassword", "1")
                .request()
                .put(Entity.json(""));

        Assertions.assertEquals(410, response.getStatus());
    }
}
