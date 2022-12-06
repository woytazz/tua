package pl.lodz.p.it.ssbd2022.ssbd01.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable(value = false)
@Table(name = "Access_level")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level")
@NamedQueries({
        @NamedQuery(name = "AccessLevel.findAll", query = "select a from AccessLevel a"),
        @NamedQuery(name = "AccessLevel.findByAccountId", query = "select a from AccessLevel a where a.account.id = :account_id")
})
public abstract class AccessLevel extends AbstractEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Basic(optional = false)
    @Column(name = "access_level")
    @Getter
    @Setter
    private String accessLevel;

    @Basic(optional = false)
    @Column(name = "active")
    @Getter
    @Setter
    private boolean active = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @Getter
    @Setter
    private Account account;

    public AccessLevel() {
    }
}
