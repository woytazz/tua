package pl.lodz.p.it.ssbd2022.ssbd01.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Cacheable(value = false)
@Table(name = "Renter_details")
@DiscriminatorValue("Renter")
@NamedQueries({
        @NamedQuery(name = "RenterDetails.findAll", query = "select r from RenterDetails r"),
        @NamedQuery(name = "RenterDetails.findByUserName", query = "select r from RenterDetails r where r.userName = :userName"),
        @NamedQuery(name = "RenterDetails.findByAccountId", query = "select r from RenterDetails r where r.account.id = :account_id")
})
public class RenterDetails extends AccessLevel implements Serializable {

    @Basic(optional = false)
    @Size(min = 2, max = 64)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9.,$;]+$")
    @Column(name = "user_name", unique = true)
    @Getter
    @Setter
    private String userName;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "renters")
    @Getter
    @Setter
    private Collection<Rate> rateCollection = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "renter")
    @Getter
    @Setter
    private Collection<UserOffer> userOfferCollection = new ArrayList<>();

    public RenterDetails() {
    }

    public RenterDetails(String userName) {
        this.userName = userName;
    }
}
