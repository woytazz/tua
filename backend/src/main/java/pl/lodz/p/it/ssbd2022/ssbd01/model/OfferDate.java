package pl.lodz.p.it.ssbd2022.ssbd01.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Cacheable(value = false)
@Table(name = "Offer_date")
@NamedQueries({
        @NamedQuery(name = "OfferDate.findAll", query = "select o from OfferDate o"),
        @NamedQuery(name = "OfferDate.findOfferDatesByOfferId", query = "select o from OfferDate o where o.offer.id = :id"),
        @NamedQuery(name = "OfferDate.findByDateAndOfferId", query = "select o from OfferDate o where o.date = :date and o.offer.id = :offer_id"),
        @NamedQuery(name = "OfferDate.findByUserOffer_Renter_UserName", query = "select o from OfferDate o where o.userOffer.renter.userName = :userName")
})
public class OfferDate extends AbstractEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Basic(optional = false)
    @Column(name = "date")
    @Getter
    @Setter
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "offer_id", referencedColumnName = "id")
    @Getter
    @Setter
    private Offer offer;

    @OneToOne(mappedBy = "offerDate")
    @Getter
    @Setter
    private UserOffer userOffer;

    public OfferDate() {
    }

    public OfferDate(LocalDate date) {
        this.date = date;
    }
}
