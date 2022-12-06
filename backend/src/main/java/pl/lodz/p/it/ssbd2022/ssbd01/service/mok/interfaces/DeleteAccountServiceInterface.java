package pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.service.TransactionInterface;

import javax.ejb.Local;

/**
 * Serwis odpowiedzialny za usuwanie kont
 */

@Local
public interface DeleteAccountServiceInterface extends TransactionInterface {

    /**
     * Metoda odpowiedzialna za usuwanie konta dostawcy usług, które nie zostało potwierdzone przez administratora
     *
     * @param login - login użytkownika do usunięcia
     */

    void deleteServiceProvider(String login);
}
