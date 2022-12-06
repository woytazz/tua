package pl.lodz.p.it.ssbd2022.ssbd01.interceptors;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.OfferNotFoundException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;

public class OfferFacadeInterceptor {
    @AroundInvoke
    public Object interceptor(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (OptimisticLockException exc) {
            throw exc;
        } catch (NoResultException exc) {
            throw new OfferNotFoundException(exc);
        }
    }
}
