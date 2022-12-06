package pl.lodz.p.it.ssbd2022.ssbd01.service.mok;

import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutAccountDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutProviderDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.put.PutRenterDTO;
import pl.lodz.p.it.ssbd2022.ssbd01.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccessLevelFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.facade.mok.VerificationTokenFacade;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.GenericServiceInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.model.*;
import pl.lodz.p.it.ssbd2022.ssbd01.service.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd01.service.mok.interfaces.UpdateAccountServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd01.util.HMAC;
import pl.lodz.p.it.ssbd2022.ssbd01.util.hash.HashGenerator;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.UUID;


@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({
        GenericServiceInterceptor.class,
        LoggerInterceptor.class})
public class UpdateAccountService extends AbstractService implements UpdateAccountServiceInterface, SessionSynchronization {

    @Inject
    private HashGenerator hashGenerator;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private AccessLevelFacade accessLevelFacade;

    @Inject
    private VerificationTokenFacade verificationTokenFacade;

    @RolesAllowed("Admin")
    @Override
    public void activateUserAccount(String login, String Etag) {
        Account account = accountFacade.findByLogin(login);
        if (Etag.equals(HMAC.calculateHMAC(account.getLogin(), account.getVersion()))) {
            account.setActive(true);
            accountFacade.edit(account);
        } else {
            throw new OptimisticLockException();
        }
    }

    @RolesAllowed("Admin")
    @Override
    public void deactivateUserAccount(String login, String ETag) {
        Account account = accountFacade.findByLogin(login);
        if (ETag.equals(HMAC.calculateHMAC(account.getLogin(), account.getVersion()))) {
            account.setActive(false);
            accountFacade.edit(account);
        } else {
            throw new OptimisticLockException();
        }
    }

    @RolesAllowed("Admin")
    @Override
    public void addServiceProviderAccessLevel(String login, ServiceProviderDetails accessLevel) {
        Account account = accountFacade.findByLogin(login);
        AccessLevel accessLevelObj = account.getAccessLevelCollection()
                .stream()
                .filter(e -> e.getClass().getName().equals(accessLevel.getClass().getName()))
                .findAny()
                .orElse(null);

        if (accessLevelObj == null) {
            accessLevel.setAccount(account);
            Rate rate = new Rate();
            rate.setServiceProvider(accessLevel);
            accessLevel.setRate(rate);
            account.getAccessLevelCollection().add(accessLevel);
            accountFacade.edit(account);
        } else {
            throw new AccessLevelAlreadyExistsException();
        }
    }

    @RolesAllowed("Admin")
    @Override
    public void addRenterAccessLevel(String login, RenterDetails accessLevel) {
        Account account = accountFacade.findByLogin(login);
        AccessLevel accessLevelObj = account.getAccessLevelCollection()
                .stream()
                .filter(e -> e.getClass().getName().equals(accessLevel.getClass().getName()))
                .findAny()
                .orElse(null);

        if (accessLevelObj == null) {
            accessLevel.setAccount(account);
            account.getAccessLevelCollection().add(accessLevel);
            accountFacade.edit(account);
        } else {
            throw new AccessLevelAlreadyExistsException();
        }
    }

    @RolesAllowed("Admin")
    @Override
    public void deleteAccessLevel(String login, String accessString) {
        if (accessString.equals("Admin")) {
            throw new AccessLevelCannotBeDeletedException();
        }
        Account account = accountFacade.findByLoginAccessLevel(login, accessString);
        AccessLevel accessLevelObj = account.getAccessLevelCollection()
                .stream()
                .filter(e -> e.getAccessLevel().equals(accessString))
                .findFirst()
                .orElse(null);
        if (accessLevelObj != null) {
            accessLevelFacade.remove(accessLevelObj);
        } else {
            throw new AccessLevelNotFoundException();
        }
    }

    @RolesAllowed("Admin")
    @Override
    public void confirmServiceProvider(String login, String language) {
        Account account = accountFacade.findByLogin(login);
        account.setConfirmed(true);
        accountFacade.edit(account);
    }

    @RolesAllowed("Admin")
    @Override
    public void changePassword(String login, String newPassword, String ETag) {
        Account account = accountFacade.findByLogin(login);
        if (ETag.equals(HMAC.calculateHMAC(account.getLogin(), account.getVersion()))) {
            if (account.getPassword().equals(hashGenerator.generateHash(newPassword))) {
                throw new PasswordAlreadyUsedException();
            }
            account.setPassword(hashGenerator.generateHash(newPassword));
            accountFacade.edit(account);
        } else {
            throw new OptimisticLockException();
        }
    }

    @RolesAllowed("ALL")
    @Override
    public void changeOwnPassword(String login, String oldPassword, String newPassword, String ETag) {
        Account account = accountFacade.findByLogin(login);

        if (ETag.equals(HMAC.calculateHMAC(account.getLogin(), account.getVersion()))) {
            if (account.getPassword().equals(hashGenerator.generateHash(oldPassword))) {
                if (account.getPassword().equals(hashGenerator.generateHash(newPassword))) {
                    throw new PasswordAlreadyUsedException();
                }
                account.setPassword(hashGenerator.generateHash(newPassword));
                accountFacade.edit(account);
            } else {
                throw new AuthorizationException();
            }

        } else {
            throw new OptimisticLockException();
        }
    }

    @RolesAllowed("AdminService")
    @Override
    public void editServiceProviderDetails(String login, PutProviderDTO putProviderDTO, String ETag) {

        String[] ETagTable = ETag.split(",");

        Account dbAccount = accountFacade.findByLogin(login);

        for (AccessLevel ac : dbAccount.getAccessLevelCollection()) {
            if (ac instanceof ServiceProviderDetails) {
                ServiceProviderDetails accessLevel = (ServiceProviderDetails) ac;

                if (ETagTable[0].equals(HMAC.calculateHMAC(dbAccount.getLogin(), dbAccount.getVersion()))
                        && ETagTable[1].equals(HMAC.calculateHMAC(dbAccount.getLogin(), accessLevel.getVersion()))) {

                    dbAccount.setName(putProviderDTO.getName());
                    dbAccount.setSurname(putProviderDTO.getSurname());
                    dbAccount.setPhoneNumber(putProviderDTO.getPhoneNumber());

                    accessLevel.setAddress(putProviderDTO.getAddress());
                    accessLevel.setDescription(putProviderDTO.getDescription());
                    accessLevel.setLogoUrl(putProviderDTO.getLogoUrl());
                    accountFacade.edit(dbAccount);
                    accessLevelFacade.edit(accessLevel);

                } else {
                    throw new OptimisticLockException();
                }
            }
        }
    }

    @RolesAllowed("Admin")
    @Override
    public void updateAdmin(String login, PutAccountDTO dto, String ETag) {
        Account dbAccount = accountFacade.findByLogin(login);

        if (ETag.equals(HMAC.calculateHMAC(dbAccount.getLogin(), dbAccount.getVersion()))) {
            dbAccount.setName(dto.getName());
            dbAccount.setSurname(dto.getSurname());
            dbAccount.setPhoneNumber(dto.getPhoneNumber());

            accountFacade.edit(dbAccount);
        } else {
            throw new OptimisticLockException();
        }
    }

    @RolesAllowed("AdminRenter")
    @Override
    public void updateRenter(String login, PutRenterDTO dto, String ETag) {
        Account dbAccount = accountFacade.findByLogin(login);

        String[] ETagTable = ETag.split(",");

        for (AccessLevel ac : dbAccount.getAccessLevelCollection()) {
            if (ac instanceof RenterDetails) {
                RenterDetails accessLevel = (RenterDetails) ac;

                if (ETagTable[0].equals(HMAC.calculateHMAC(dbAccount.getLogin(), dbAccount.getVersion()))
                        && ETagTable[1].equals(HMAC.calculateHMAC(dbAccount.getLogin(), accessLevel.getVersion()))) {

                    dbAccount.setName(dto.getName());
                    dbAccount.setSurname(dto.getSurname());
                    dbAccount.setPhoneNumber(dto.getPhoneNumber());

                    accessLevel.setUserName(dto.getUserName());
                    accountFacade.edit(dbAccount);
                    accessLevelFacade.edit(accessLevel);

                } else {
                    throw new OptimisticLockException();
                }
            }
        }
    }


    @PermitAll
    @Override
    public void sendPasswordResetMail(String email) {
        //check if email exists in database
        Account account = accountFacade.findByEmail(email);
        //create verification token
        String token = UUID.randomUUID().toString();
        VerificationToken tokenObject = new VerificationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                account
        );
        verificationTokenFacade.create(tokenObject);
    }

    @PermitAll
    @Override
    public void resetPassword(String token, String newPassword) {

        VerificationToken verificationToken;
        verificationToken = verificationTokenFacade.findByToken(token);

        Account account = verificationToken.getAccount();

        account.setPassword(hashGenerator.generateHash(newPassword));

        accountFacade.edit(account);
        verificationTokenFacade.remove(verificationToken);
    }

    @PermitAll
    @Override
    public void findByVerificationToken(String token) {
        verificationTokenFacade.findByToken(token);
    }
}
