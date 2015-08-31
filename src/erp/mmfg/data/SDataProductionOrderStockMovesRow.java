/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

/**
 *
 * @author Juan Barajas
 */
public class SDataProductionOrderStockMovesRow extends erp.lib.table.STableRow {

    protected erp.client.SClientInterface miClient;

    protected java.util.Date mtDate;
    protected java.lang.String msNumberSeries;
    protected java.lang.String msDiogTypeCode;
    protected java.lang.String msDiogType;
    protected java.lang.String msCompanyBranchOrg;
    protected java.lang.String msWarehouseOrg;
    protected java.lang.String msLot;
    protected java.util.Date mtDateExpiration_n;
    protected double mdMovIn;
    protected double mdMovOut;
    protected java.lang.String msItemKey;
    protected java.lang.String msUnit;
    protected double mdPercentage;
    
    public SDataProductionOrderStockMovesRow(erp.client.SClientInterface client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mtDate = null;
        msNumberSeries = "";
        msDiogTypeCode = "";
        msDiogType = "";
        msCompanyBranchOrg = "";
        msWarehouseOrg = "";
        msLot = "";
        mtDateExpiration_n = null;
        mdMovIn = 0;
        mdMovOut = 0;
        msItemKey = "";
        msUnit = "";
        mdPercentage = 0;
    }

    public void setDate(java.util.Date t) { mtDate = t; }
    public void setNumberSeries(java.lang.String s) { msNumberSeries = s; }
    public void setDiogTypeCode(java.lang.String s) { msDiogTypeCode = s; }
    public void setDiogType(java.lang.String s) { msDiogType = s; }
    public void setCompanyBranchOrg(java.lang.String s) { msCompanyBranchOrg = s; }
    public void setWarehouseOrg(java.lang.String s) { msWarehouseOrg = s; }
    public void setLot(java.lang.String s) { msLot = s; }
    public void setDateExpiration_n(java.util.Date t) { mtDateExpiration_n = t; }
    public void setMovIn(double d) { mdMovIn = d; }
    public void setMovOut(double d) { mdMovOut = d; }
    public void setItemKey(java.lang.String s) { msItemKey = s; }
    public void setUnit(java.lang.String s) { msUnit = s; }
    public void setPercentage(double d) { mdPercentage = d; }
    
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getNumberSeries() { return msNumberSeries; }
    public java.lang.String getDiogTypeCode() { return msDiogTypeCode; }
    public java.lang.String getDiogType() { return msDiogType; }
    public java.lang.String getCompanyBranchOrg() { return msCompanyBranchOrg; }
    public java.lang.String getWarehouseOrg() { return msWarehouseOrg; }
    public java.lang.String getLot() { return msLot; }
    public java.util.Date getDateExpiration_n() { return mtDateExpiration_n; }
    public double getMovIn() { return mdMovIn; }
    public double getMovOut() { return mdMovOut; }
    public java.lang.String getItemKey() { return msItemKey; }
    public java.lang.String getUnit() { return msUnit; }
    public double getPercentage() { return mdPercentage; }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(mtDate);
        mvValues.add(msNumberSeries);
        mvValues.add(msDiogTypeCode);
        mvValues.add(msDiogType);
        mvValues.add(msCompanyBranchOrg);
        mvValues.add(msWarehouseOrg);
        mvValues.add(msItemKey);
        mvValues.add(msLot);
        mvValues.add(mtDateExpiration_n);
        mvValues.add(mdMovIn);
        mvValues.add(mdMovOut);        
        mvValues.add(msUnit);
        mvValues.add(mdPercentage);        
    }
}
