/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

/**
 *
 * @author Edwin Carmona
 */
public class SMatConsumeSubEntCcConfig {
    
    protected int mnPkId;
    protected int mnFkMaterialRequestId;
    protected int mnFkMaterialRequestEntryId;
    protected int mnFkSubentMatConsumptionEntityId;
    protected int mnFkSubentMatConsumptionSubentityId;
    protected int mnFkCostCenterId;
    protected double mdPercentage;

    public int getPkId() {
        return mnPkId;
    }

    public void setPkId(int pkId) {
        this.mnPkId = pkId;
    }

    public int getFkMaterialRequestId() {
        return mnFkMaterialRequestId;
    }

    public void setFkMaterialRequestId(int fkMaterialRequestId) {
        this.mnFkMaterialRequestId = fkMaterialRequestId;
    }

    public int getFkMaterialRequestEntryId() {
        return mnFkMaterialRequestEntryId;
    }

    public void setFkMaterialRequestEntryId(int fkMaterialRequestEntryId) {
        this.mnFkMaterialRequestEntryId = fkMaterialRequestEntryId;
    }

    public int getFkSubentMatConsumptionEntityId() {
        return mnFkSubentMatConsumptionEntityId;
    }

    public void setFkSubentMatConsumptionEntityId(int fkSubentMatConsumptionEntityId) {
        this.mnFkSubentMatConsumptionEntityId = fkSubentMatConsumptionEntityId;
    }

    public int getFkSubentMatConsumptionSubentityId() {
        return mnFkSubentMatConsumptionSubentityId;
    }

    public void setFkSubentMatConsumptionSubentityId(int fkSubentMatConsumptionSubentityId) {
        this.mnFkSubentMatConsumptionSubentityId = fkSubentMatConsumptionSubentityId;
    }

    public int getFkCostCenterId() {
        return mnFkCostCenterId;
    }

    public void setFkCostCenterId(int fkCostCenterId) {
        this.mnFkCostCenterId = fkCostCenterId;
    }

    public double getPercentage() {
        return mdPercentage;
    }

    public void setPercentage(double percentage) {
        this.mdPercentage = percentage;
    }
    
    
    
}
