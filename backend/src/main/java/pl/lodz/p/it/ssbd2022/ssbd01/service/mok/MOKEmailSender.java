package pl.lodz.p.it.ssbd2022.ssbd01.service.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.VerificationTokenFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.MailingServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd01.model.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.MOKEmailSenderInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.util.email.EmailSender;
import pl.lodz.p.it.ssbd2022.ssbd01.util.email.MailTypeEnum;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({
        GenericServiceInterceptor.class,
        MailingServiceInterceptor.class,
        LoggerInterceptor.class})
public class MOKEmailSender implements MOKEmailSenderInterface {

    @Inject
    private EmailSender emailService;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private VerificationTokenFacade verificationTokenFacade;

    @Override
    @PermitAll
    public void sendVerificationEmail(String email, String language) {
        VerificationToken token = verificationTokenFacade.findByAccountEmail(email);
        emailService.sendEmail(email, "https://studapp.it.p.lodz.pl:8401/api/mok/create/confirmation/" + token.getGeneratedToken(), MailTypeEnum.VERIFICATION_MAIL, language);
    }

    @Override
    @PermitAll
    public void sendRemoveEmail(String email, String language) {
        emailService.sendEmail(email, "", MailTypeEnum.ACCOUNT_DELETED_MAIL, language);
    }

    @Override
    @PermitAll
    public void sendActivateEmail(String login, String language) {
        Account account = accountFacade.findByLogin(login);
        emailService.sendEmail(account.getEmail(), "", MailTypeEnum.ACCOUNT_ACTIVATE_MAIL, language);
    }

    @Override
    @PermitAll
    public void sendDeactivateEmail(String login, String language) {
        Account account = accountFacade.findByLogin(login);
        emailService.sendEmail(account.getEmail(), "", MailTypeEnum.ACCOUNT_DEACTIVATE_MAIL, language);
    }

    @Override
    @PermitAll
    public void sendConfirmServiceProviderEmail(String login, String language) {
        Account account = accountFacade.findByLogin(login);
        emailService.sendEmail(account.getEmail(), "", MailTypeEnum.ACCOUNT_CONFIRMED_MAIL, language);
    }

    @Override
    @PermitAll
    public void sendPasswordResetMail(String email, String language) {
        VerificationToken token = verificationTokenFacade.findByAccountEmail(email);
        emailService.sendEmail(email, "https://studapp.it.p.lodz.pl:8401/api/mok/update/password-reset-email-confirm/" + token.getGeneratedToken(), MailTypeEnum.RESET_PASSWORD_MAIL, language);
    }
}
