package erp.mod.cfg.swap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author Edwin Carmona
 */
public class ActionHistory {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("sequence")
    private Integer sequence;
    
    @JsonProperty("flow_status")
    private FlowStatus flowStatus;
    
    @JsonProperty("notes")
    private String notes;
    
    @JsonProperty("actioned_at")
    private String actionedAt;
    
    @JsonProperty("actioned_by")
    private Actor actionedBy;
    
    @JsonProperty("all_actors")
    private List<Actor> allActors;
    
    @JsonProperty("is_current")
    private Boolean isCurrent;
    
    @JsonProperty("created_at")
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public FlowStatus getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(FlowStatus flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getActionedAt() {
        return actionedAt;
    }

    public void setActionedAt(String actionedAt) {
        this.actionedAt = actionedAt;
    }

    public Actor getActionedBy() {
        return actionedBy;
    }

    public void setActionedBy(Actor actionedBy) {
        this.actionedBy = actionedBy;
    }

    public List<Actor> getAllActors() {
        return allActors;
    }

    public void setAllActors(List<Actor> allActors) {
        this.allActors = allActors;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Métodos auxiliares para obtención de datos, no contenidos en el JSON, pero necesarios para la lógica de negocio
     */
    public String getActorName() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allActors.size(); i++) {
            sb.append(allActors.get(i).getFullName());
            if (i < allActors.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
