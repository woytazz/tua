package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class LoginTakenException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.CONFLICT;

    private static final String DEFAULT_MESSAGE = "login.taken.exception";

    public LoginTakenException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public LoginTakenException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
