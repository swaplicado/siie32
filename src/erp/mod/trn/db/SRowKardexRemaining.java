package erp.mod.trn.db;

import java.util.Date;

/**
 * Saldo neto de una entrada de kardex (entradas - salidas ya guardadas),
 * agrupado por diog in. Usado internamente en el proceso de consumo.
 *
 * @author Edwin Carmona
 */
public class SRowKardexRemaining {

    private int mnPkKardexId;
    private int mnFkDiogYearInId;
    private int mnFkDiogDocInId;
    private int mnFkDiogEntryInId;
    private int mnFkDpsYearInMainId;
    private int mnFkDpsDocInMainId;
    private int mnFkDpsEntryInMainId;
    private int mnFkDpsCurrencyInMainId;
    private int mnFkItemId;
    private int mnFkUnitId;
    private int mnFkLotId;
    private int mnFkCompanyBranchId;
    private int mnFkWarehouseId;
    private Date mtMovDate;
    private double mdExchangeRate;
    private double mdQtyAvailable;
    private double mdQtyConsumed;
    private double mdRemaining;
    private double mdRemainingCurrency;
    private String msAuxDpsCostCenterCode;

    public SRowKardexRemaining() {
        msAuxDpsCostCenterCode = "";
    }

    public void setPkKardexId(int n) { mnPkKardexId = n; }
    public void setFkDiogYearInId(int n) { mnFkDiogYearInId = n; }
    public void setFkDiogDocInId(int n) { mnFkDiogDocInId = n; }
    public void setFkDiogEntryInId(int n) { mnFkDiogEntryInId = n; }
    public void setFkDpsYearInMainId(int n) { mnFkDpsYearInMainId = n; }
    public void setFkDpsDocInMainId(int n) { mnFkDpsDocInMainId = n; }
    public void setFkDpsEntryInMainId(int n) { mnFkDpsEntryInMainId = n; }
    public void setFkDpsCurrencyInMainId(int n) { mnFkDpsCurrencyInMainId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkLotId(int n) { mnFkLotId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkWarehouseId(int n) { mnFkWarehouseId = n; }
    public void setMovDate(Date t) { mtMovDate = t; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setQtyAvailable(double d) { mdQtyAvailable = d; }
    public void setQtyConsumed(double d) { mdQtyConsumed = d; }
    public void setRemaining(double d) { mdRemaining = d; }
    public void setRemainingCurrency(double d) { mdRemainingCurrency = d; }
    public void setAuxDpsCostCenterCode(String s) { msAuxDpsCostCenterCode = s; }

    public int getPkKardexId() { return mnPkKardexId; }
    public int getFkDiogYearInId() { return mnFkDiogYearInId; }
    public int getFkDiogDocInId() { return mnFkDiogDocInId; }
    public int getFkDiogEntryInId() { return mnFkDiogEntryInId; }
    public int getFkDpsYearInMainId() { return mnFkDpsYearInMainId; }
    public int getFkDpsDocInMainId() { return mnFkDpsDocInMainId; }
    public int getFkDpsEntryInMainId() { return mnFkDpsEntryInMainId; }
    public int getFkDpsCurrencyInMainId() { return mnFkDpsCurrencyInMainId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkLotId() { return mnFkLotId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkWarehouseId() { return mnFkWarehouseId; }
    public Date getMovDate() { return mtMovDate; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getQtyAvailable() { return mdQtyAvailable; }
    public double getQtyConsumed() { return mdQtyConsumed; }
    public double getRemaining() { return mdRemaining; }
    public double getRemainingCurrency() { return mdRemainingCurrency; }
    public String getAuxDpsCostCenterCode() { return msAuxDpsCostCenterCode; }

    public String getDiogInKey() {
        return mnFkDiogYearInId + "-" + mnFkDiogDocInId + "-" + mnFkDiogEntryInId;
    }

    public String getItemKey() {
        return mnFkItemId + "-" + mnFkUnitId + "-" + mnFkLotId;
    }
}
