package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class ServiceRateNotFoundException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = "service.rate.not.found.exception";

    public ServiceRateNotFoundException() { super(DEFAULT_STATUS, DEFAULT_MESSAGE); }

    public ServiceRateNotFoundException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
