package pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces;

import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;

import javax.ejb.Local;
import java.util.List;

/**
 * Serwis odpowiedzialny za czytanie informacji o kontach użytkowników
 */

@Local
public interface ReadAccountServiceInterface {

    /**
     * Metoda odpowiedzialna za czytanie informacji o koncie użytkownika o podanym loginie
     *
     * @param login - login szukanego użytkownika
     * @return - odpowiedź JSON
     */

    Account readAccountByLogin(String login);

    /**
     * Metoda odpowiedzialna za czytanie wszystkich użytkowników
     *
     * @return Lista kont
     */

    List<Account> readAllAccounts();

    /**
     * Metoda odpowiedzialna za czytanie kont dostawców usług, które nie zostały potwierdzone
     *
     * @return Lista kont
     */

    List<Account> readProvidersToConfirm();

    /**
     * Metoda odpowiedzialna za czytanie konta dostawcy usług o podanej nazwie.
     *
     * @param serviceName - nazwa dostawcy usług
     * @return ServiceProviderDetails
     */

    Account readServiceProviderByName(String serviceName);
}
