package pl.lodz.p.it.ssbd2022.ssbd01.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable(value = false)
@Table(name = "User_offer")
@NamedQueries({
        @NamedQuery(name = "UserOffer.findAll", query = "select u from UserOffer u")
})
public class UserOffer extends AbstractEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @OneToOne
    @JoinColumn(name = "offer_date_id", referencedColumnName = "id")
    @Getter
    @Setter
    private OfferDate offerDate;

    @ManyToOne
    @JoinColumn(name = "renter_id", referencedColumnName = "id")
    @Getter
    @Setter
    private RenterDetails renter;

    public UserOffer() {
    }
}
