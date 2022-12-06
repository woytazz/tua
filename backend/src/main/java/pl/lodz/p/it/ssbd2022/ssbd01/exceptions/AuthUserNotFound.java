package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class AuthUserNotFound extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = "auth.user.not.found.exception";

    public AuthUserNotFound() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public AuthUserNotFound(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
