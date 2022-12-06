package pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.get;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceProviderDTO extends AccessLevelDTO {

    private String serviceName;

    private String nip;

    private String address;

    private String description;

    private String logoUrl;

    private float rate;

    public ServiceProviderDTO(String accessLevel, boolean active, String serviceName, String nip, String address, String description, String logoUrl, float rate, String versionHash) {
        super(accessLevel, active, versionHash);
        this.serviceName = serviceName;
        this.nip = nip;
        this.address = address;
        this.description = description;
        this.logoUrl = logoUrl;
        this.rate = rate;
    }
}
