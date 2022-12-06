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
@Table(name = "Service_provider_details")
@DiscriminatorValue("ServiceProvider")
@NamedQueries({
        @NamedQuery(name = "ServiceProviderDetails.findAll", query = "select s from ServiceProviderDetails s"),
        @NamedQuery(name = "ServiceProviderDetails.findByServiceName", query = "select s from ServiceProviderDetails s where s.serviceName = :serviceName"),
        @NamedQuery(name = "ServiceProviderDetails.findByNip", query = "select s from ServiceProviderDetails s where s.nip = :nip"),
        @NamedQuery(name = "ServiceProviderDetails.findByAddress", query = "select s from ServiceProviderDetails s where s.address = :address"),
        @NamedQuery(name = "ServiceProviderDetails.findByAccountId", query = "select s from ServiceProviderDetails s where s.account.id = :account_id"),
        @NamedQuery(name = "ServiceProviderDetails.findByAccountLogin", query = "select s from ServiceProviderDetails s where s.account.login = :login")
})
public class ServiceProviderDetails extends AccessLevel implements Serializable {

    @Basic(optional = false)
    @Size(min = 2, max = 32)
    @Column(name = "service_name", unique = true)
    @Getter
    @Setter
    private String serviceName;

    @Basic(optional = false)
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d{10}$")
    @Column(name = "NIP", updatable = false, unique = true)
    @Getter
    @Setter
    private String nip;

    @Basic(optional = false)
    @Column(name = "address", unique = true)
    @Size(min = 4, max = 100)
    @Getter
    @Setter
    private String address;

    @Basic(optional = false)
    @Column(name = "description")
    @Getter
    @Setter
    private String description;

    @Basic(optional = false)
    @Column(name = "logo_url")
    @Getter
    @Setter
    private String logoUrl;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "serviceProvider")
    @Getter
    @Setter
    private Rate rate;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "serviceProvider")
    @Getter
    @Setter
    private Collection<Offer> offerCollection = new ArrayList<>();

    public ServiceProviderDetails() {
    }

    public ServiceProviderDetails(String serviceName, String nip, String address, String description, String logoUrl) {
        this.serviceName = serviceName;
        this.nip = nip;
        this.address = address;
        this.description = description;
        this.logoUrl = logoUrl;
    }
}
