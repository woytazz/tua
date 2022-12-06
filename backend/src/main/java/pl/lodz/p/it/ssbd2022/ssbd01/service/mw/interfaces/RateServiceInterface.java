package pl.lodz.p.it.ssbd2022.ssbd01.service.mw.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.service.TransactionInterface;

import javax.ejb.Local;

/**
 * Serwis odpowiedzialny za ocenianie usług
 */
@Local
public interface RateServiceInterface extends TransactionInterface {

    /**
     * Metoda odpowiedzialna za ocenianie dostawcy usługi
     *
     * @param login - login wynajmujacegeo
     * @param rate - ocena
     * @param serviceName - nazwa usługodawcy
     */


    void rate(String login, int rate, String serviceName);
}
