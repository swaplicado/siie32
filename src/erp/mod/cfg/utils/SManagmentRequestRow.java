package erp.mod.cfg.utils;


import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Edwin Carmona
 */
public class SManagmentRequestRow {
    
    @JsonProperty("id_pc")
    private String idPc;
    
    @JsonProperty("id_siie")
    private String idSiie;
    
    @JsonProperty("id_siie_company")
    private int idSiieCompany;
    
    @JsonProperty("id_authz_resource_type")
    private int idAuthzResourceType;
    
    // Constructores
    public SManagmentRequestRow() {}
    
    public SManagmentRequestRow(String idPc, String idSiie, int idSiieCompany, int idAuthzResourceType) {
        this.idPc = idPc;
        this.idSiie = idSiie;
        this.idSiieCompany = idSiieCompany;
        this.idAuthzResourceType = idAuthzResourceType;
    }
    
    // Getters y Setters
    public String getIdPc() {
        return idPc;
    }
    
    public void setIdPc(String idPc) {
        this.idPc = idPc;
    }
    
    public String getIdSiie() {
        return idSiie;
    }
    
    public void setIdSiie(String idSiie) {
        this.idSiie = idSiie;
    }
    
    public int getIdSiieCompany() {
        return idSiieCompany;
    }
    
    public void setIdSiieCompany(int idSiieCompany) {
        this.idSiieCompany = idSiieCompany;
    }
    
    public int getIdAuthzResourceType() {
        return idAuthzResourceType;
    }
    
    public void setIdAuthzResourceType(int idAuthzResourceType) {
        this.idAuthzResourceType = idAuthzResourceType;
    }
}
