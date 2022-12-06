package pl.lodz.p.it.ssbd2022.ssbd01.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Cacheable(value = false)
@Table(name = "Offer")
@NamedQueries({
        @NamedQuery(name = "Offer.findAll", query = "select o from Offer o"),
        @NamedQuery(name = "Offer.findByActiveTrue", query = "select o from Offer o where o.active = true"),
        @NamedQuery(name = "Offer.findById", query = "select o from Offer o where o.id = :id"),
        @NamedQuery(name = "Offer.findHighestPriceOffer", query = "SELECT o1 FROM Offer o1 WHERE o1.price=(SELECT MAX(o.price) FROM Offer o)"),
        @NamedQuery(name = "Offer.findByPriceGreaterThanEqualAndPriceLessThanEqual", query = "select o from Offer o where o.price >= :price1 and o.price <= :price2"),
        @NamedQuery(name = "Offer.findByServiceProvider_Account_Login", query = "select o from Offer o where o.serviceProvider.account.login = :login")

})
public class Offer extends AbstractEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Basic(optional = false)
    @Column(name = "title")
    @Getter
    @Setter
    private String title;

    @Basic(optional = false)
    @Column(name = "description")
    @Getter
    @Setter
    private String description;

    @Basic(optional = false)
    @Column(name = "price")
    @Getter
    @Setter
    private int price;

    @Basic(optional = false)
    @Column(name = "active")
    @Getter
    @Setter
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "service_provider_id", referencedColumnName = "id")
    @Getter
    @Setter
    private ServiceProviderDetails serviceProvider;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "offer")
    @Getter
    @Setter
    private Collection<OfferDate> offerDateCollection = new ArrayList<>();

    public Offer() {
    }

    public Offer(String title, String description, int price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }
}
