package pl.lodz.p.it.ssbd2022.ssbd01.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Cacheable(value = false)
@Table(name = "Verification_token")
@NamedQueries({
        @NamedQuery(name = "VerificationToken.findByToken", query = "select t from VerificationToken t where t.generatedToken = :token"),
        @NamedQuery(name = "VerificationToken.getExpiredTokens", query = "select t from VerificationToken t where t.expiryDate < :dateNow"),
        @NamedQuery(name = "VerificationToken.findByAccount_Email", query = "select v from VerificationToken v where v.account.email = :email")
})
public class VerificationToken extends AbstractEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Basic(optional = false)
    @Column(name = "generated_token", unique = true)
    @Getter
    @Setter
    private String generatedToken;

    @Basic(optional = false)
    @Column(name = "creation_date")
    @Getter
    @Setter
    private LocalDateTime creationDate;

    @Basic(optional = false)
    @Column(name = "expiry_date")
    @Getter
    @Setter
    private LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @Getter
    @Setter
    private Account account;

    public VerificationToken() {
    }

    public VerificationToken(String generatedToken, LocalDateTime creationDate, LocalDateTime expiryDate, Account account) {
        this.generatedToken = generatedToken;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
        this.account = account;
    }
}
