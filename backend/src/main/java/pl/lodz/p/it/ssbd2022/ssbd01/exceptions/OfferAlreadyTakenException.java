package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import javax.ws.rs.core.Response;

public class OfferAlreadyTakenException extends ApplicationBaseException {

    private static final Response.Status DEFAULT_STATUS = Response.Status.CONFLICT;

    private static final String DEFAULT_MESSAGE = "offer.already.taken.exception";

    public OfferAlreadyTakenException() {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE);
    }

    public OfferAlreadyTakenException(Throwable cause) {
        super(DEFAULT_STATUS, DEFAULT_MESSAGE, cause);
    }
}