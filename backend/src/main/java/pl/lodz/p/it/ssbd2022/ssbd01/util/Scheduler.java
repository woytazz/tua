package pl.lodz.p.it.ssbd2022.ssbd01.util;

import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.ApplicationBaseException;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.VerificationTokenFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.model.VerificationToken;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;

/**
 * Klasa odpowiedzialna za wywyoływnaie metod w zadanym przedziale czasowym
 */

@Singleton
@Startup
public class Scheduler {

    @Inject
    private VerificationTokenFacade verificationTokenFacade;

    @Inject
    private AccountFacade accountFacade;

//    /**
//     * Metoda wywoływana co minute, usuwająca konta które nie zostały powtwierdzone w zadanym przedziale czasowym
//     */
//    @Schedule(hour = "*", minute = "*/1")
//    public void removeInactiveTokensAndUsers() {
//        List<VerificationToken> tokens = verificationTokenFacade.getExpiredTokens();
//        tokens.forEach(token -> {
//            try {
//                verificationTokenFacade.remove(token);
//                accountFacade.remove(token.getAccount());
//            } catch (ApplicationBaseException e) {
//                e.printStackTrace();
//            }
//        });
//    }
}
