package pl.lodz.p.it.ssbd2022.ssbd01.util.email;

/**
 * Serwis odpowiedzialny za wysyłanie maili
 */

public interface EmailSender {

    /**
     * Metoda odpowiedzialna za wysyłanie danego rodzaju maila
     *
     * @param to       - mail użytkownika do którego ma zostać wysłany mail
     * @param token    - token wryfikacyjny
     * @param mailType - rodzaj maila
     * @param language - język maila
     */

    void sendEmail(String to, String token, MailTypeEnum mailType, String language);

}
