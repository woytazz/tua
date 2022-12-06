package pl.lodz.p.it.ssbd2022.ssbd01.interceptors;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.validation.ValidationException;
import java.sql.SQLException;

public class GenericFacadeInterceptor {

    @AroundInvoke
    public Object interceptor(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (ApplicationBaseException exc) {
            throw exc;
        } catch (ValidationException exc) {
            throw ApplicationBaseException.getValidationException(exc);
        } catch (OptimisticLockException exc) {
            throw ApplicationBaseException.getOptimisticLockException(exc);
        } catch (PersistenceException | SQLException exc) {
            throw ApplicationBaseException.getPersistenceException(exc);
        } catch (Exception exc) {
            throw ApplicationBaseException.getGeneralErrorException(exc);
        }
    }
}
