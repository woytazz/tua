package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class AccessLevelAlreadyExistsException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.CONFLICT;

    private static final String DEFAULT_MESSAGE = "access.level.already.exists.exception";

    public AccessLevelAlreadyExistsException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public AccessLevelAlreadyExistsException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
