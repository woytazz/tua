package pl.lodz.p.it.ssbd2022.ssbd01.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Cacheable(value = false)
@Table(name = "Account")
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "select a from Account a"),
        @NamedQuery(name = "Account.findById", query = "select a from Account a where a.id = :id"),
        @NamedQuery(name = "Account.findByEmail", query = "select a from Account a where a.email = :email"),
        @NamedQuery(name = "Account.findByLogin", query = "select a from Account a where a.login = :login"),
        @NamedQuery(name = "Account.findByLoginActiveConfirmed", query = "select a from Account a where a.login = :login and a.confirmed = true and a.active = true"),
        @NamedQuery(name = "Account.findByLoginAndAccessLevel", query = "select a from Account a inner join a.accessLevelCollection accessLevelCollection where a.login = :login and accessLevelCollection.accessLevel = :accessLevel"),
        @NamedQuery(name = "Account.findNotConfirmedServiceProviders", query = "select a from Account a inner join a.accessLevelCollection accessLevelCollection where a.confirmed = false and accessLevelCollection.accessLevel = :accessLevel"),
        @NamedQuery(name = "Account.findByServiceProvidersActiveAndConfirmed", query = "select a from Account a inner join a.accessLevelCollection accessLevelCollection where accessLevelCollection.accessLevel = :accessLevel and a.confirmed = true and a.active = true")
})
public class Account extends AbstractEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Basic(optional = false)
    @Column(name = "login", unique = true)
    @Size(min = 4, max = 16)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9.,$;]+$")
    @Getter
    @Setter
    private String login;

    @Basic(optional = false)
    @Column(name = "password")
    @Size(min = 8, max = 64)
    @Getter
    @Setter
    private String password;

    @Basic(optional = false)
    @Column(name = "active")
    @Getter
    @Setter
    private boolean active = true;

    @Basic(optional = false)
    @Column(name = "confirmed")
    @Getter
    @Setter
    private boolean confirmed = false;

    @Basic(optional = false)
    @Size(min = 2, max = 64)
    @Pattern(regexp = "^([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+$")
    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Basic(optional = false)
    @Size(min = 2, max = 64)
    @Pattern(regexp = "^([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+$")
    @Column(name = "surname")
    @Getter
    @Setter
    private String surname;

    @Basic(optional = false)
    @Size(min = 1, max = 256)
    @Email
    @Column(name = "email", unique = true)
    @Getter
    @Setter
    private String email;

    @Basic(optional = false)
    @Size(min = 9, max = 9)
    @Pattern(regexp = "\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{3})")
    @Column(name = "phone_number")
    @Getter
    @Setter
    private String phoneNumber;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "account")
    @Getter
    @Setter
    private Collection<AccessLevel> accessLevelCollection = new ArrayList<>();

    public Account() {
    }

    public Account(String login, String password, String name, String surname, String email, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
