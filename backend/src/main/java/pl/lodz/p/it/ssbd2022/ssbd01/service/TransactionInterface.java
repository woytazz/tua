package pl.lodz.p.it.ssbd2022.ssbd01.service;

import javax.ejb.Local;

/**
 * Serwis odpowiedzialny za powtarzanie transakcji
 */

@Local
public interface TransactionInterface {

    /**
     * Metoda odpowiedzialna za sprawdzanie, czy transakcja została odwołana
     *
     * @return flaga wskazująca czy poprzednia transakcja została odwołana
     */

    boolean isLastTransactionRollback();
}
