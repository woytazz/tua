package pl.lodz.p.it.ssbd2022.ssbd01.interceptors;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.*;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

public class AccountFacadeInterceptor {

    @AroundInvoke
    public Object interceptor(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (OptimisticLockException exc) {
            throw exc;
        } catch (NoResultException exc) {
            throw new UserNotFoundException(exc);
        } catch (PersistenceException exc) {
            if (exc.getMessage().contains("account_login_key")) {
                throw new LoginTakenException(exc);
            }
            if (exc.getMessage().contains("account_email_key")) {
                throw new EmailTakenException(exc);
            }
            if (exc.getMessage().contains("service_provider_details_service_name_key")) {
                throw new ServiceNameTakenException(exc);
            }
            if (exc.getMessage().contains("service_provider_details_nip_key")) {
                throw new NIPTakenException(exc);
            }
            if (exc.getMessage().contains("service_provider_details_address_key")) {
                throw new ServiceAddressTakenException(exc);
            }
            throw exc;
        }
    }
}
