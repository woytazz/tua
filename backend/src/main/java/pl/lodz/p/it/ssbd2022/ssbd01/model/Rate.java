package pl.lodz.p.it.ssbd2022.ssbd01.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Cacheable(value = false)
@Table(name = "Rate")
@NamedQueries({
        @NamedQuery(name = "Rate.findAll", query = "select r from Rate r"),
        @NamedQuery(name = "Rate.findByServiceName", query = "select r from Rate r where r.serviceProvider.serviceName = :service")
})
public class Rate extends AbstractEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "average_rate")
    @Getter
    @Setter
    private float averageRate = 0;

    @Column(name = "rates_number")
    @Getter
    @Setter
    private int ratesNumber = 0;

    @ManyToMany
    @JoinColumn(name = "renter_id", referencedColumnName = "id")
    @Getter
    @Setter
    private Collection<RenterDetails> renters;

    @OneToOne
    @JoinColumn(name = "service_provider_id", referencedColumnName = "id")
    @Getter
    @Setter
    private ServiceProviderDetails serviceProvider;

    public Rate() {
    }
}
