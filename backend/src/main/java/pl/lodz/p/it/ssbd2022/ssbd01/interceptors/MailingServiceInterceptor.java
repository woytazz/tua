package pl.lodz.p.it.ssbd2022.ssbd01.interceptors;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.mail.MessagingException;

public class MailingServiceInterceptor {
    @AroundInvoke
    public Object interceptor(InvocationContext ictx) throws Exception {
        try {
            return ictx.proceed();
        } catch (MessagingException exc) {
            throw ApplicationBaseException.getEmailSenderException(exc);
        } catch (Exception exc) {
            throw ApplicationBaseException.getGeneralErrorException(exc);
        }
    }
}
