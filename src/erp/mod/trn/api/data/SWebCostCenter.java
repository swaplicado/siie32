/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.data;

/**
 *
 * @author Edwin Carmona
 */
public class SWebCostCenter {
    
    int idMatReq;
    int idConsumeEntity;
    int idConsumeSubEntity;
    int idConsumeSubSubEntity;
    int idCostCenter;
    double percentage;
    String costCenter;

    public int getIdMatReq() {
        return idMatReq;
    }
    public void setIdMatReq(int idMatReq) {
        this.idMatReq = idMatReq;
    }
    public int getIdConsumeEntity() {
        return idConsumeEntity;
    }
    public void setIdConsumeEntity(int idConsumeEntity) {
        this.idConsumeEntity = idConsumeEntity;
    }
    public int getIdConsumeSubEntity() {
        return idConsumeSubEntity;
    }
    public void setIdConsumeSubEntity(int idConsumeSubEntity) {
        this.idConsumeSubEntity = idConsumeSubEntity;
    }
    public int getIdConsumeSubSubEntity() {
        return idConsumeSubSubEntity;
    }
    public void setIdConsumeSubSubEntity(int idConsumeSubSubEntity) {
        this.idConsumeSubSubEntity = idConsumeSubSubEntity;
    }
    public int getIdCostCenter() {
        return idCostCenter;
    }
    public void setIdCostCenter(int idCostCenter) {
        this.idCostCenter = idCostCenter;
    }
    public double getPercentage() {
        return percentage;
    }
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    public String getCostCenter() {
        return costCenter;
    }
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }
    
}
