package pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.get;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RenterDTO extends AccessLevelDTO {

    private String userName;

    public RenterDTO(String accessLevel, boolean active, String userName, String versionHash) {
        super(accessLevel, active, versionHash);
        this.userName = userName;
    }
}
