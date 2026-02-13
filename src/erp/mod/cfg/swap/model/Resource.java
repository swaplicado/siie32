package erp.mod.cfg.swap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;

/**
 *
 * @author Edwin Carmona
 */
public class Resource {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("resource_type")
    private ResourceType resourceType;
    
    @JsonProperty("created_at")
    private Date createdAt;
    
    @JsonProperty("updated_at")
    private Date updatedAt;
    
    @JsonProperty("deleted_at")
    private Date deletedAt;
    
    @JsonProperty("code")
    private String code;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("content")
    private JsonNode content;
    
    @JsonProperty("external_id")
    private String externalId;
    
    @JsonProperty("external_siie_id")
    private String externalSiieId;
    
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
    
    @JsonProperty("created_by")
    private Integer createdBy;
    
    @JsonProperty("updated_by")
    private Integer updatedBy;
    
    @JsonProperty("deleted_by")
    private Integer deletedBy;
    
    @JsonProperty("external_sys")
    private Integer externalSys;
    
    @JsonProperty("company")
    private Integer company;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getContent() {
        return content;
    }

    public void setContent(JsonNode content) {
        this.content = content;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalSiieId() {
        return externalSiieId;
    }

    public void setExternalSiieId(String externalSiieId) {
        this.externalSiieId = externalSiieId;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Integer deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Integer getExternalSys() {
        return externalSys;
    }

    public void setExternalSys(Integer externalSys) {
        this.externalSys = externalSys;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }
}
