package pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces;

import javax.ejb.Local;

/**
 * Serwis odpowiedzialny za wysyłanie mail
 */


@Local
public interface MOKEmailSenderInterface {

    /**
     * Metoda odpowiedzialna za wysyłanie emaila weryfikacyjnego
     *
     * @param email    - email na który ma zostać wysłana wiadomość
     * @param language - język wiadomości
     */

    void sendVerificationEmail(String email, String language);

    /**
     * Metoda odpowiedzialna za wysyłanie emaila z informacją o niepotwierdzeniu i usunięciu konta
     *
     * @param email    - email na który ma zostać wysłana wiadomość
     * @param language - język wiadomości
     */

    void sendRemoveEmail(String email, String language);

    /**
     * Metoda odpowiedzialna za wysyłanie emaila z wiadomością o aktywowaniu konta
     *
     * @param login    - login użytkownika do którego ma zostać wysłany email
     * @param language - język wiadomości
     */

    void sendActivateEmail(String login, String language);

    /**
     * Metoda odpowiedzialna za wysyłanie emaila z wiadomością o dezaktywowaniu konta
     *
     * @param login    - login użytkownika do którego ma zostać wysłany email
     * @param language - język wiadomości
     */

    void sendDeactivateEmail(String login, String language);

    /**
     * Metoda odpowiedzialna za wysłyłanie emaila z wiadomością o potwierdzeniu konta dostawcy usług
     *
     * @param login    - login użytkownika do którego ma zostać wysłany email
     * @param language - język wiadomości
     */

    void sendConfirmServiceProviderEmail(String login, String language);

    /**
     * Metoda odpowiedzialna za wysyłanie emaila z resetem hasła użytkownika
     *
     * @param email    - email na który ma zostać wysłana wiadomość
     * @param language - język wiadomości
     */

    void sendPasswordResetMail(String email, String language);
}
