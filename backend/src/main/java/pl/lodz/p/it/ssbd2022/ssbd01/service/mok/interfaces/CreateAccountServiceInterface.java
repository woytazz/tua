package pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.model.*;
import pl.lodz.p.it.ssbd2022.ssbd01.service.TransactionInterface;

import javax.ejb.Local;

/**
 * Serwis odpowiedzialny za tworzenie kont
 */

@Local
public interface CreateAccountServiceInterface extends TransactionInterface {

    /**
     * Metoda odpowiedzialna za tworzenie konta administratora
     *
     * @param account     - encja konta
     * @param accessLevel - encja poziomu dostępu
     */

    void createAdminAccount(Account account, AdminDetails accessLevel);

    /**
     * Metoda odpowiedzialna za tworzenie konta dostawcy usług
     *
     * @param account     - encja konta
     * @param accessLevel - encja poziomu dostępu
     */

    void createServiceProviderAccount(Account account, ServiceProviderDetails accessLevel);

    /**
     * Method responsible for creating renter account and sending verification email to registered user
     * Metoda odpowiedzialna za tworzenie konta wypożyczającego i wysyłanie wiadomości email z weryfikacją zarejestrowanego użytkownika
     *
     * @param account     - encja konta
     * @param accessLevel - encja poziomu dostępu
     */


    void createRenterAccountAndSendVerificationEmail(Account account, RenterDetails accessLevel);

    /**
     * Metoda odpowiedzialna za potwierdzanie konta
     *
     * @param token - token weryfikacyjny (UUID)
     */

    void confirmToken(String token);
}
