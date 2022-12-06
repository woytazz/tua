package pl.lodz.p.it.ssbd2022.ssbd01.interceptors;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerInterceptor {

    @Resource
    private SessionContext sctx;
    private static final Logger loger = Logger.getLogger(LoggerInterceptor.class.getName());

    @AroundInvoke
    public Object traceInvoke(InvocationContext ictx) throws Exception {
        StringBuilder message = new StringBuilder("Method invoked: ");
        Object result;
        try {
            try {
                message.append(ictx.getMethod().toString());
                message.append(", Caller: ").append(sctx.getCallerPrincipal().getName());
                message.append(", Method parameters: ");
                if (null != ictx.getParameters()) {
                    for (Object param : ictx.getParameters()) {
                        message.append(String.valueOf(param)).append(" ");
                    }
                }

            } catch (Exception e) {
                loger.log(Level.SEVERE, "Unexpected logging exception: ", e.getMessage());
                throw e;
            }

            result = ictx.proceed();

        } catch (Exception e) {
            message.append(" Method invocation ended with exception: ").append(e.getMessage());
            loger.log(Level.SEVERE, message.toString(), e);
            throw e;
        }

        message.append("Returned value: ").append(String.valueOf(result)).append(" ");

        loger.info(message.toString());

        return result;
    }
}
