package pl.lodz.p.it.ssbd2022.ssbd01.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Cacheable(value = false)
@Table(name = "Admin_details")
@DiscriminatorValue("Admin")
@NamedQueries({
        @NamedQuery(name = "AdminDetails.findAll", query = "select a from AdminDetails a")
})
public class AdminDetails extends AccessLevel implements Serializable {

    public AdminDetails() {
    }
}
