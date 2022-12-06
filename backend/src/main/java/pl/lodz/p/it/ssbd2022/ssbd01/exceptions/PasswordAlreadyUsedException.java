package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class PasswordAlreadyUsedException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.CONFLICT;

    private static final String DEFAULT_MESSAGE = "password.already.used.exception";

    public PasswordAlreadyUsedException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public PasswordAlreadyUsedException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
