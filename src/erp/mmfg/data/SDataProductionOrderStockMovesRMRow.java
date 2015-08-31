/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SDataConstantsSys;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderStockMovesRMRow extends erp.lib.table.STableRow {

    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnFkSortingItemTypeId;
    protected java.lang.String msItemKey;
    protected java.lang.String msItem;
    protected double mdGrossRequirement;
    protected java.lang.String msUnit;
    protected double mdRawMaterialAssigned;
    protected boolean mbIsFormula;
    protected double mdPercentage;
    
    public SDataProductionOrderStockMovesRMRow(int nFkSortingItemTypeId) {
        reset();

        mnFkSortingItemTypeId = nFkSortingItemTypeId;
    }

    public void reset() {
        mnPkItemId = 0;
        mnPkUnitId = 0;
        msItemKey = "";
        msItem = "";
        mdGrossRequirement = 0;
        msUnit = "";
        mdRawMaterialAssigned = 0;
        mbIsFormula = false;
        mdPercentage = 0;
        mnFkSortingItemTypeId = 0;
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setItemKey(java.lang.String s) { msItemKey = s; }
    public void setItem(java.lang.String s) { msItem = s; }
    public void setGrossRequirement(double d) { mdGrossRequirement = d; }
    public void setUnit(java.lang.String s) { msUnit = s; }
    public void setRawMaterialAssigned(double d) { mdRawMaterialAssigned = d; }
    public void setIsFormula(boolean b) { mbIsFormula = b; }
    public void setPercentage(double d) { mdPercentage = d; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }

    @Override
    public void prepareTableRow() {
        mvValues.clear();

        if (mnFkSortingItemTypeId == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            mvValues.add(msItemKey);
            mvValues.add(msItem);
        }
        else {
            mvValues.add(msItem);
            mvValues.add(msItemKey);
        }
        
        mvValues.add(mdGrossRequirement);
        mvValues.add(msUnit);
        mvValues.add(mdRawMaterialAssigned);
        mvValues.add(msUnit);
        mvValues.add(mbIsFormula);
        mvValues.add(mdPercentage);
    }
}
