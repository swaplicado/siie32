package erp.mod.cfg.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author Edwin Carmona
 */
public class SManagmentRequest {
    
    @JsonProperty("id_external_system")
    private int idExternalSystem;
    
    @JsonProperty("b_history")
    private int bHistory;
    
    private List<SManagmentRequestRow> rows;
    
    // Constructores
    public SManagmentRequest() {}
    
    public SManagmentRequest(int idExternalSystem, int bHistory, List<SManagmentRequestRow> rows) {
        this.idExternalSystem = idExternalSystem;
        this.bHistory = bHistory;
        this.rows = rows;
    }
    
    // Getters y Setters
    public int getIdExternalSystem() {
        return idExternalSystem;
    }
    
    public void setIdExternalSystem(int idExternalSystem) {
        this.idExternalSystem = idExternalSystem;
    }
    
    public int getBHistory() {
        return bHistory;
    }
    
    public void setBHistory(int bHistory) {
        this.bHistory = bHistory;
    }
    
    public List<SManagmentRequestRow> getRows() {
        return rows;
    }
    
    public void setRows(List<SManagmentRequestRow> rows) {
        this.rows = rows;
    }
}
