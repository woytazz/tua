package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class ServiceNameTakenException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.CONFLICT;

    private static final String DEFAULT_MESSAGE = "service.name.taken.exception";

    public ServiceNameTakenException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public ServiceNameTakenException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
