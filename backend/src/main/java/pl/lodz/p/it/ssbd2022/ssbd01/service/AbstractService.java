package pl.lodz.p.it.ssbd2022.ssbd01.service;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

public abstract class AbstractService {

    protected static final Logger LOGGER = Logger.getGlobal();

    @Resource
    private SessionContext sctx;

    private String transactionId;

    private boolean lastTransactionRollback;

    @TransactionAttribute(NOT_SUPPORTED)
    public boolean isLastTransactionRollback() {
        return lastTransactionRollback;
    }

    public void afterBegin() {
        transactionId = Long.toString(System.currentTimeMillis())
                + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

        LOGGER.log(Level.INFO, "Transaction TX_id={0} begin in {1}, identity: {2}",
                new Object[]{transactionId, this.getClass().getName(), sctx.getCallerPrincipal().getName()});
    }

    public void beforeCompletion() {
        LOGGER.log(Level.INFO, "Transaction TX_id={0} before approval in {1}, identity: {2}",
                new Object[]{transactionId, this.getClass().getName(), sctx.getCallerPrincipal().getName()});
    }

    public void afterCompletion(boolean committed) {
        lastTransactionRollback = !committed;

        LOGGER.log(Level.INFO, "Transaction TX_id={0} end in {1}, by {3}, identity {2}",
                new Object[]{transactionId, this.getClass().getName(), sctx.getCallerPrincipal().getName(),
                        committed ? "APPROVAL" : "CANCELLATION"});
    }
}
