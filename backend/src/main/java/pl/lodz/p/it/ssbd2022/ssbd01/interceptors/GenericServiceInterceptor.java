package pl.lodz.p.it.ssbd2022.ssbd01.interceptors;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;

import javax.ejb.AccessLocalException;
import javax.ejb.EJBAccessException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.OptimisticLockException;
import javax.validation.ValidationException;

public class GenericServiceInterceptor {

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
        } catch (EJBAccessException | AccessLocalException exc) {
            throw ApplicationBaseException.getAccessDeniedException(exc);
        } catch (Exception exc) {
            throw ApplicationBaseException.getGeneralErrorException(exc);
        }
    }
}
