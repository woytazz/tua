package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class OfferOwnerException extends ApplicationBaseException {
    private static final Response.Status DEFAULT_STATUS = Response.Status.CONFLICT;

    private static final String DEFAULT_MESSAGE = "offer.owner.exception";

    public OfferOwnerException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public OfferOwnerException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
