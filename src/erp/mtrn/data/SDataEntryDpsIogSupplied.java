/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataEntryDpsIogSupplied extends erp.lib.table.STableRow {

    protected int mnPkDpsYearId;
    protected int mnPkDpsDocId;
    protected int mnPkDpsEntryId;
    protected java.lang.String msItemKey;
    protected java.lang.String msConcept;
    protected double mdQuantity;
    protected double mdQuantityOriginal;
    protected double mdQuantityReturned;
    protected double mdQuantityNet;
    protected java.lang.String msUnitSymbol;
    protected double mdSurplusPercentage;
    protected double mdQuantitySupplied;
    protected double mdQuantityToSupply;
    protected double mdQuantityActualSupplied;
    protected double mdQuantityToActualSupply;
    protected double mdQuantityToSupplyNet;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkUnitOriginalId;
    protected double mdPriceUnitaryReal;
    protected String msLot;

    protected SDataDiogEntry moParamDiogEntry;

    protected boolean mbAuxIsEdited;

    protected java.lang.String msParamBizPartner;
    protected java.util.Date mtParamDpsDate;
    protected java.lang.String msParamTypeDocument;
    protected java.lang.String msParamNumberSerie;
    protected java.lang.String msParamCompanyBranch;
    protected int mnParamShipmentsDps;
    protected int mnParamShipments;
    protected double mdParamTotalCurrency;
    protected java.lang.String msParamCurrencyKey;

    public SDataEntryDpsIogSupplied() {
        reset();
        prepareTableRow();
    }

    public void setPkDpsYearId(int n) { mnPkDpsYearId = n; }
    public void setPkDpsDocId(int n) { mnPkDpsDocId = n; }
    public void setPkDpsEntryId(int n) { mnPkDpsEntryId = n; }
    public void setItemKey(java.lang.String s) { msItemKey = s; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setQuantityReturned(double d) { mdQuantityReturned = d; }
    public void setQuantityNet(double d) { mdQuantityNet = d; }
    public void setUnitSymbol(java.lang.String s) { msUnitSymbol = s; }
    public void setSurplusPercentage(double d) { mdSurplusPercentage = d; }
    public void setQuantitySupplied(double d) { mdQuantitySupplied = d; }
    public void setQuantityToSupply(double d) { mdQuantityToSupply = d; }
    public void setQuantityActualSupplied(double d) { mdQuantityActualSupplied = d; }
    public void setQuantityToActualSupply(double d) { mdQuantityToActualSupply = d; }
    public void setQuantityToSupplyNet(double d) { mdQuantityToSupplyNet = d; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setPriceUnitaryReal(double d) { mdPriceUnitaryReal = d; }
    public void setLot(java.lang.String s) { msLot = s; }

    public int getPkDpsYearId() { return mnPkDpsYearId; }
    public int getPkDpsDocId() { return mnPkDpsDocId; }
    public int getPkDpsEntryId() { return mnPkDpsEntryId; }
    public java.lang.String getItemKey() { return msItemKey; }
    public java.lang.String getConcept() { return msConcept; }
    public double getQuantity() { return mdQuantity; }
    public double getQuantityReturned() { return mdQuantityReturned; }
    public double getQuantityNet() { return mdQuantityNet; }
    public java.lang.String getUnitSymbol() { return msUnitSymbol; }
    public double getSurplusPercentage() { return mdSurplusPercentage; }
    public double getQuantitySupplied() { return mdQuantitySupplied; }
    public double getQuantityToSupply() { return mdQuantityToSupply; }
    public double getQuantityActualSupplied() { return mdQuantityActualSupplied; }
    public double getQuantityToActualSupply() { return mdQuantityToActualSupply; }
    public double getQuantityToSupplyNet() { return mdQuantityToSupplyNet; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public double getPriceUnitaryReal() { return mdPriceUnitaryReal; }
    public java.lang.String getLot() { return msLot; }

    public void setParamDiogEntry(erp.mtrn.data.SDataDiogEntry e) { moParamDiogEntry = e; }
    public erp.mtrn.data.SDataDiogEntry getParamDiogEntry() { return moParamDiogEntry; }

    public void setAuxIsEdited(boolean b) { mbAuxIsEdited = b; }
    public boolean getAuxIsEdited() { return mbAuxIsEdited; }

    public void setParamBizPartner(java.lang.String s) { msParamBizPartner = s; }
    public void setParamDpsDate(java.util.Date dt) { mtParamDpsDate = dt; }
    public void setParamTypeDocument(java.lang.String s) { msParamTypeDocument = s; }
    public void setParamNumberSerie(java.lang.String s) { msParamNumberSerie = s; }
    public void setParamCompanyBranch(java.lang.String s) { msParamCompanyBranch = s; }
    public void setParamShipmentsDps(int n) { mnParamShipmentsDps = n; }
    public void setParamShipments(int n) { mnParamShipments = n; }
    public void setParamTotalCurrency(double d) { mdParamTotalCurrency = d; }
    public void setParamCurrencyKey(java.lang.String s) { msParamCurrencyKey = s; }

    public java.lang.String getParamBizPartern() { return msParamBizPartner; }
    public java.util.Date getParamDpsDate() { return mtParamDpsDate; }
    public java.lang.String getParamTypeDocument() { return msParamTypeDocument; }
    public java.lang.String getParamNumberSerie() { return msParamNumberSerie; }
    public java.lang.String getParamCompanyBranch() { return msParamCompanyBranch; }
    public int getParamShipmentsDps() { return mnParamShipmentsDps; }
    public int getParamShipments() { return mnParamShipments; }
    public double getParamTotalCurrency() { return mdParamTotalCurrency; }
    public java.lang.String getParamCurrencyKey() { return msParamCurrencyKey; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkDpsYearId = ((int[]) pk)[0];
        mnPkDpsDocId = ((int[]) pk)[1];
        mnPkDpsEntryId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkDpsYearId, mnPkDpsDocId, mnPkDpsEntryId };
    }

   public void reset() {
        mnPkDpsYearId = 0;
        mnPkDpsDocId = 0;
        mnPkDpsEntryId = 0;
        msItemKey = "";
        msConcept = "";
        mdQuantity = 0;
        mdQuantityReturned = 0;
        mdQuantityNet = 0;
        msUnitSymbol = "";
        mdSurplusPercentage = 0;
        mdQuantitySupplied = 0;
        mdQuantityToSupply = 0;
        mdQuantityActualSupplied = 0;
        mdQuantityToActualSupply = 0;
        mdQuantityToSupplyNet = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mdPriceUnitaryReal = 0;
        msLot = "";

        mbAuxIsEdited = false;
        moParamDiogEntry = null;

        msParamBizPartner = "";
        mtParamDpsDate = null;
        msParamTypeDocument = "";
        msParamNumberSerie = "";
        msParamCompanyBranch = "";
        mnParamShipmentsDps = 0;
        mnParamShipments = 0;
        mdParamTotalCurrency = 0;
        msParamCurrencyKey = "";
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msItemKey);
        mvValues.add(msConcept);
        mvValues.add(mdQuantity);
        mvValues.add(mdQuantityReturned);
        mvValues.add(mdQuantityNet);
        mvValues.add(msUnitSymbol);
        mvValues.add(mdSurplusPercentage);
        mvValues.add(mdQuantitySupplied);
        mvValues.add(mdQuantityToSupply);
        mvValues.add(mdQuantityActualSupplied);
        mvValues.add(mdQuantityToActualSupply);
        mvValues.add(mdQuantityToSupplyNet);
    }
}
