package pl.lodz.p.it.ssbd2022.ssbd01.dto.mok.get;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;

@Getter
@Setter
@NoArgsConstructor
public abstract class AccessLevelDTO {

    private String accessLevel;

    private boolean active;

    @JsonbProperty("etag")
    private String ETag;


    public AccessLevelDTO(String accessLevel, boolean active, String ETag) {
        this.accessLevel = accessLevel;
        this.active = active;
        this.ETag = ETag;
    }
}
