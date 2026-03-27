package erp.mod.cfg.swap.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author Edwin Carmona
 */
public class FlowResponse {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("resource")
    private Resource resource;
    
    @JsonProperty("priority")
    private Integer priority;
    
    @JsonProperty("deadline")
    private String deadline;
    
    @JsonProperty("flow_status")
    private Integer flowStatus;
    
    @JsonProperty("notes")
    private String notes;
    
    @JsonProperty("actions_history")
    private List<ActionHistory> actionsHistory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Integer getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(Integer flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ActionHistory> getActionsHistory() {
        return actionsHistory;
    }

    public void setActionsHistory(List<ActionHistory> actionsHistory) {
        this.actionsHistory = actionsHistory;
    }
}
