package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class UserNotFoundException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = "user.not.found.exception";

    public UserNotFoundException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public UserNotFoundException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
