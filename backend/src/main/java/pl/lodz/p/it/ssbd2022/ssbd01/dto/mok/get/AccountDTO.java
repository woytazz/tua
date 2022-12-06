package pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.get;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {

    private String login;

    private boolean active;

    private boolean confirmed;

    private String name;

    private String surname;

    private String email;

    private String phoneNumber;

    private Collection<AccessLevelDTO> accessLevelDTOs;

    @JsonbProperty("etag")
    private String ETag;

    public AccountDTO(String login, boolean active, boolean confirmed, String name, String surname, String email, String phoneNumber, Collection<AccessLevelDTO> accessLevelDTOs, String ETag) {
        this.login = login;
        this.active = active;
        this.confirmed = confirmed;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accessLevelDTOs = accessLevelDTOs;
        this.ETag = ETag;
    }
}
