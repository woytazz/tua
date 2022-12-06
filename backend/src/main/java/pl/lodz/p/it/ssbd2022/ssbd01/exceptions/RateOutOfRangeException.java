package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class RateOutOfRangeException extends ApplicationBaseException {
    private static final Response.Status DEFAULT_STATUS = Response.Status.BAD_REQUEST;
    private static final String DEFAULT_MESSAGE = "rate.out.of.range.exception";

    public RateOutOfRangeException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public RateOutOfRangeException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
