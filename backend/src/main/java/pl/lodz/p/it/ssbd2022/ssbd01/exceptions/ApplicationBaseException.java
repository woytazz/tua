package pl.lodz.p.it.ssbd2022.ssbd01.exceptions;

import lombok.Getter;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class ApplicationBaseException extends WebApplicationException {

    private static final String UNKNOWN_ERROR_EXCEPTION = "unknown.error.exception";

    private static final String PERSISTENCE_ERROR_EXCEPTION = "persistence.error.exception";

    private static final String OPTIMISTIC_LOCK_EXCEPTION = "optimistic.lock.exception";

    private static final String ACCESS_DENIED_EXCEPTION = "access.denied.exception";

    private static final String DATA_NOT_VALID_EXCEPTION = "data.not.valid.exception";

    private static final String EMAIL_SENDER_EXCEPTION = "email.sender.exception";

    @Getter
    private Throwable cause;

    protected ApplicationBaseException(Response.Status status, String key) {
        super(Response.status(status).entity(key).build());
    }

    protected ApplicationBaseException(Response.Status status, String key, Throwable cause) {
        super(Response.status(status).entity(key).build());
        this.cause = cause;
    }

    public static ApplicationBaseException getGeneralErrorException() {
        return new ApplicationBaseException(Response.Status.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_EXCEPTION);
    }

    public static ApplicationBaseException getGeneralErrorException(Exception cause) {
        return new ApplicationBaseException(Response.Status.INTERNAL_SERVER_ERROR, UNKNOWN_ERROR_EXCEPTION, cause);
    }

    public static ApplicationBaseException getPersistenceException(Exception cause) {
        return new ApplicationBaseException(Response.Status.INTERNAL_SERVER_ERROR, PERSISTENCE_ERROR_EXCEPTION, cause);
    }

    public static ApplicationBaseException getOptimisticLockException(Exception cause) {
        return new ApplicationBaseException(Response.Status.CONFLICT, OPTIMISTIC_LOCK_EXCEPTION, cause);
    }

    public static ApplicationBaseException getAccessDeniedException(Exception cause) {
        return new ApplicationBaseException(Response.Status.FORBIDDEN, ACCESS_DENIED_EXCEPTION, cause);
    }

    public static ApplicationBaseException getValidationException(Exception cause) {
        return new ApplicationBaseException(Response.Status.BAD_REQUEST, DATA_NOT_VALID_EXCEPTION, cause);
    }

    public static ApplicationBaseException getEmailSenderException(Exception cause) {
        return new ApplicationBaseException(Response.Status.INTERNAL_SERVER_ERROR, EMAIL_SENDER_EXCEPTION, cause);
    }
}
