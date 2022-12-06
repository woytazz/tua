package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class OfferNotFoundException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = "offer.not.found.exception";

    public OfferNotFoundException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public OfferNotFoundException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}
