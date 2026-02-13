package erp.mod.cfg.swap.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Edwin Carmona
 */
public class Actor {
    @JsonProperty("full_name")
    private String fullName;
    
    @JsonProperty("external_id")
    private Integer externalId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getExternalId() {
        return externalId;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }
}
