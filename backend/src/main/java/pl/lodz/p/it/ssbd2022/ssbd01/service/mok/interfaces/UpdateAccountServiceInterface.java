package pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutAccountDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.model.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd01.model.RenterDetails;
import pl.lodz.p.it.ssbd2022.ssbd01.model.ServiceProviderDetails;
import pl.lodz.p.it.ssbd2022.ssbd01.service.TransactionInterface;

import javax.ejb.Local;

/**
 * Serwis odpowiedzialny za aktualizowanie kont użytkowników
 */

@Local
public interface UpdateAccountServiceInterface extends TransactionInterface {

    /**
     * Metoda odpowiedzialna za aktywowanie wybranego konta użytkownika
     *
     * @param login - login konta użytkownika
     * @param ETag  - podpisane pole wersji
     */

    void activateUserAccount(String login, String ETag);

    /**
     * Metoda odpowiedzialna za dezaktywowanie wybranego konta użytkownika
     *
     * @param login - login konta użytkownika
     * @param ETag  - podpisane pole wersji
     */

    void deactivateUserAccount(String login, String ETag);

    /**
     * Metoda odpowiedzialna za dodanie poziomu dostępu dla podanego konta administratora
     *
     * @param login       - login konta użytkownika
     * @param accessLevel - encja poziomu dostępu
     */

    void addServiceProviderAccessLevel(String login, ServiceProviderDetails accessLevel);

    /**
     * Metoda odpowiedzialna za dodanie poziomu dostępu dla podanego konta administratora
     *
     * @param login       - login konta użytkownika
     * @param accessLevel - encja poziomu dostępu
     */

    void addRenterAccessLevel(String login, RenterDetails accessLevel);

    /**
     * Metoda odpowiedzialna za usunięcie poziomu dostępu dla podanego konta administratora
     *
     * @param login        - login konta użytkownika
     * @param accessString - nazwa poziomu dostępu
     */

    void deleteAccessLevel(String login, String accessString);

    /**
     * Metoda odpowiedzialna za aktywowanie konta dostawcy usług
     *
     * @param login - login konta użytkownika
     */

    void confirmServiceProvider(String login, String language);

    /**
     * Metoda odpowiedzialna za zmianę hasła wybranego użytkownika
     *
     * @param login       - login konta użytkownika
     * @param newPassword - nowe hasło dla konta użytkownika
     */

    void changePassword(String login, String newPassword, String ETag);

    /**
     * Metoda odpowiedzialna za zmianę własnego hasła przez użytkownika
     *
     * @param login       - login konta użytkownika
     * @param oldPassword - stare hasło użytkownika old user's password
     * @param newPassword - new password for user's account
     */

    void changeOwnPassword(String login, String oldPassword, String newPassword, String ETag);

    /**
     * Metoda odpowiedzialna za zmianę danych dostawcy usług
     *
     * @param login          - login konta użytkownika
     * @param putProviderDTO - DTO z nowymi danymi
     */

    void editServiceProviderDetails(String login, PutProviderDTO putProviderDTO, String ETag);

    /**
     * Metoda odpowiedzialna za zmianę danych administratora
     *
     * @param login - login konta użytkownika
     * @param dto   - DTO z nowymi danymi
     */

    void updateAdmin(String login, PutAccountDTO dto, String ETag);

    /**
     * Metoda odpowiedzialna za zmianę danych wypożyczającego
     *
     * @param login - login konta użytkownika
     * @param dto   - DTO z nowymi danymi
     */

    void updateRenter(String login, PutRenterDTO dto, String ETag);

    /**
     * Metoda odpowiedzialna za wysłyłanie maila ze zmianą hasła
     *
     * @param email - email na który ma zostać wysłana wiadomość
     */

    void sendPasswordResetMail(String email);

    /**
     * Metoda odpowiedzialna za zmianę hasła
     *
     * @param token       - token weryfikacyjny
     * @param newPassword - nowe hasło
     */

    void resetPassword(String token, String newPassword);

    /**
     * Metoda odpowiedzialna za sprawdzenie, czy token istnieje
     *
     * @param token - token weryfikacyjny
     */

    void findByVerificationToken(String token);
}
