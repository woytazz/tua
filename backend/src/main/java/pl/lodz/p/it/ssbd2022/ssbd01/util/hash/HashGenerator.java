package pl.lodz.p.it.ssbd2022.ssbd01.util.hash;

/**
 * Interfejs odpowiedzialny za szyfrowanie danych
 */
public interface HashGenerator {

    /**
     * Metoda odpowiedzialna za generowanie kodu hash z zadanego stringa
     *
     * @param input tekst do zaszyfrowania
     * @return zaszyfrowany tekst
     */

    String generateHash(String input);
}
