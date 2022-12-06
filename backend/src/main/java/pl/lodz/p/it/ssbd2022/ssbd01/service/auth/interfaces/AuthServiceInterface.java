package pl.lodz.p.it.ssbd2022.ssbd01.service.auth.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.auth.TokenDTO;

import javax.ejb.Local;
import java.util.Set;

/**
 * Serwis odpowiedzialny za autoryzację użytkowników i zarządzanie tokenami dostępu użytkowników
 */

@Local
public interface AuthServiceInterface {

    /**
     * Metoda odpowiedzialna za generowanie tokenów dostępu i odświeżającego
     *
     * @param login     - login użytkownika
     * @param password- hasło użytkownika
     * @return JWT token dostępu i JWT token odświeżający
     */

    TokenDTO generateTokens(String login, String password);

    /**
     * Metoda odpowiedzialna za odświeżanie tokenu dostępu i generowanie nowego tokenu odświeżającego
     *
     * @param refreshToken - token odświeżający do zmiany tokenu dostępu
     * @return JWT token dostępu i JWT token odświeżający
     */

    TokenDTO refreshTokens(String refreshToken);

    /**
     * Metoda odpowiedzialna za sprawdzanie czy użytkownik może zostać zautoryzowany
     *
     * @param username  - login użytkownika
     * @param password- hasło użytkownika
     * @return prawda bądź fałsz, zależnie od rezultatu autoryzacji
     */

    boolean authenticateUsernamePassword(String username, String password);

    /**
     * Metoda odpowiedzialna za zwracanie poziomów dostępu użytkownika
     *
     * @param username - login użytkownika
     * @return Zestaw poziomów dostępu użytkownika
     */

    Set<String> getCallerGroups(String username);
}
