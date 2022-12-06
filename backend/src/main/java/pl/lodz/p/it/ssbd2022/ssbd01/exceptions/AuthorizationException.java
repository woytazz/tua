package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class AuthorizationException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.UNAUTHORIZED;

    private static final String DEFAULT_MESSAGE = "authorization.exception";

    public AuthorizationException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public AuthorizationException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
