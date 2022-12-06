package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class TokenExpiredException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.GONE;

    private static final String DEFAULT_MESSAGE = "token.expired.exception";

    public TokenExpiredException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public TokenExpiredException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
