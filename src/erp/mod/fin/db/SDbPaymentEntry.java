/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mcfg.data.SDataCurrency;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataDps;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SDbPaymentEntry extends SDbRegistryUser implements SGridRow, Serializable {
    
    public static final String ENTRY_TYPE_ADVANCE = "A"; // anticipo
    public static final String ENTRY_TYPE_PAYMENT = "P"; // pago a documento

    public static final String DESC_ENTRY_TYPE_ADVANCE = "Anticipo"; // anticipo
    public static final String DESC_ENTRY_TYPE_PAYMENT = "Pago a documento"; // pago a documento

    protected int mnPkPaymentId;
    protected int mnPkEntryId;
    protected String msEntryType;
    protected double mdEntryPaymentCy;
    protected double mdEntryPaymentApplication;
    protected double mdConversionRateApplication;
    protected double mdDestinyPaymentApplicationEntryCy;
    protected double mdEntryPayment;
    protected double mdConversionRate;
    protected double mdDestinyPaymentEntryCy;
    protected int mnInstallment;
    protected double mdDocBalancePreviousApplicationCy;
    protected double mdDocBalanceUnpaidApplicationCy_r;
    protected double mdDocBalancePreviousCy;
    protected double mdDocBalanceUnpaidCy_r;
    protected int mnFkDocYearId_n;
    protected int mnFkDocDocId_n;
    protected int mnFkEntryCurrencyId;
    protected int mnFkPaymentRequestId_n;
    
    protected SDataDps moDpsRelated;
    protected SDataCurrency moPayCurrency;
    protected SDataCurrency moEntryCurrency;
    
    public SDbPaymentEntry() {
        super(SModConsts.FIN_PAY_ETY);
    }
    
    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setEntryType(String s) { msEntryType = s; }
    public void setEntryPaymentCy(double d) { mdEntryPaymentCy = d; }
    public void setEntryPaymentApplication(double d) { mdEntryPaymentApplication = d; }
    public void setConversionRateApplication(double d) { mdConversionRateApplication = d; }
    public void setDestinyPaymentApplicationEntryCy(double d) { mdDestinyPaymentApplicationEntryCy = d; }
    public void setEntryPayment(double d) { mdEntryPayment = d; }
    public void setConversionRate(double d) { mdConversionRate = d; }
    public void setDestinyPaymentEntryCy(double d) { mdDestinyPaymentEntryCy = d; }
    public void setInstallment(int n) { mnInstallment = n; }
    public void setDocBalancePreviousApplicationCy(double d) { mdDocBalancePreviousApplicationCy = d; }
    public void setDocBalanceUnpaidApplicationCy_r(double d) { mdDocBalanceUnpaidApplicationCy_r = d; }
    public void setDocBalancePreviousCy(double d) { mdDocBalancePreviousCy = d; }
    public void setDocBalanceUnpaidCy_r(double d) { mdDocBalanceUnpaidCy_r = d; }
    public void setFkDocYearId_n(int n) { mnFkDocYearId_n = n; }
    public void setFkDocDocId_n(int n) { mnFkDocDocId_n = n; }
    public void setFkEntryCurrencyId(int n) { mnFkEntryCurrencyId = n; }
    public void setFkPaymentRequestId_n(int n) { mnFkPaymentRequestId_n = n; }

    public int getPkPaymentId() { return mnPkPaymentId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public String getEntryType() { return msEntryType; }
    public double getEntryPaymentCy() { return mdEntryPaymentCy; }
    public double getEntryPaymentApplication() { return mdEntryPaymentApplication; }
    public double getConversionRateApplication() { return mdConversionRateApplication; }
    public double getDestinyPaymentApplicationEntryCy() { return mdDestinyPaymentApplicationEntryCy; }
    public double getEntryPayment() { return mdEntryPayment; }
    public double getConversionRate() { return mdConversionRate; }
    public double getDestinyPaymentEntryCy() { return mdDestinyPaymentEntryCy; }
    public int getInstallment() { return mnInstallment; }
    public double getDocBalancePreviousApplicationCy() { return mdDocBalancePreviousApplicationCy; }
    public double getDocBalanceUnpaidApplicationCy_r() { return mdDocBalanceUnpaidApplicationCy_r; }
    public double getDocBalancePreviousCy() { return mdDocBalancePreviousCy; }
    public double getDocBalanceUnpaidCy_r() { return mdDocBalanceUnpaidCy_r; }
    public int getFkDocYearId_n() { return mnFkDocYearId_n; }
    public int getFkDocDocId_n() { return mnFkDocDocId_n; }
    public int getFkEntryCurrencyId() { return mnFkEntryCurrencyId; }
    public int getFkPaymentRequestId_n() { return mnFkPaymentRequestId_n; }
    
    public void setDpsRelated(SDataDps o) { moDpsRelated = o; }
    public void setPayCurrency(SDataCurrency o) { moPayCurrency = o; }
    public void setEntryCurrency(SDataCurrency o) { moEntryCurrency = o; }
    
    public SDataDps getDpsRelated() { return moDpsRelated; }
    public SDataCurrency getPayCurrency() { return moPayCurrency; }
    public SDataCurrency getEntryCurrency() { return moEntryCurrency; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPaymentId = pk[0];
        mnPkEntryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPaymentId, mnPkEntryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkPaymentId = 0;
        mnPkEntryId = 0;
        msEntryType = "";
        mdEntryPaymentCy = 0;
        mdEntryPaymentApplication = 0;
        mdConversionRateApplication = 0;
        mdDestinyPaymentApplicationEntryCy = 0;
        mdEntryPayment = 0;
        mdConversionRate = 0;
        mdDestinyPaymentEntryCy = 0;
        mnInstallment = 0;
        mdDocBalancePreviousApplicationCy = 0;
        mdDocBalanceUnpaidApplicationCy_r = 0;
        mdDocBalancePreviousCy = 0;
        mdDocBalanceUnpaidCy_r = 0;
        mnFkDocYearId_n = 0;
        mnFkDocDocId_n = 0;
        mnFkEntryCurrencyId = 0;
        mnFkPaymentRequestId_n = 0;
        
        moDpsRelated = null;
        moPayCurrency = null;
        moEntryCurrency = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);    
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPaymentId + " " +
                "AND id_ety = " + mnPkEntryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " " +
                "AND id_ety = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkEntryId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " WHERE id_pay = " + mnPkPaymentId + "";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEntryId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkPaymentId = resultSet.getInt("id_pay");
            mnPkEntryId = resultSet.getInt("id_ety");
            msEntryType = resultSet.getString("ety_tp");
            mdEntryPaymentCy = resultSet.getDouble("ety_pay_cur");
            mdEntryPaymentApplication = resultSet.getDouble("ety_pay_app");
            mdConversionRateApplication = resultSet.getDouble("conv_rate_app");
            mdDestinyPaymentApplicationEntryCy = resultSet.getDouble("des_pay_app_ety_cur");
            mdEntryPayment = resultSet.getDouble("ety_pay");
            mdConversionRate = resultSet.getDouble("conv_rate");
            mdDestinyPaymentEntryCy = resultSet.getDouble("des_pay_ety_cur");
            mnInstallment = resultSet.getInt("install");
            mdDocBalancePreviousApplicationCy = resultSet.getDouble("doc_bal_prev_app_cur");
            mdDocBalanceUnpaidApplicationCy_r = resultSet.getDouble("doc_bal_unpd_app_cur_r");
            mdDocBalancePreviousCy = resultSet.getDouble("doc_bal_prev_cur");
            mdDocBalanceUnpaidCy_r = resultSet.getDouble("doc_bal_unpd_cur_r");
            mnFkDocYearId_n = resultSet.getInt("fk_doc_year_n");
            mnFkDocDocId_n = resultSet.getInt("fk_doc_doc_n");
            mnFkEntryCurrencyId = resultSet.getInt("fk_ety_cur");
            mnFkPaymentRequestId_n = resultSet.getInt("fk_pay_req_n");
            
            statement = session.getStatement().getConnection().createStatement();
            
            moEntryCurrency = new SDataCurrency();
            moEntryCurrency.read(new int[] { mnFkEntryCurrencyId }, statement);
            
            if (mnFkDocYearId_n != 0 && mnFkDocDocId_n != 0) {
                moDpsRelated = new SDataDps();
                moDpsRelated.read(new int[] { mnFkDocYearId_n, mnFkDocDocId_n }, statement);
            }

            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPaymentId + ", " + 
                    mnPkEntryId + ", " + 
                    "'" + msEntryType + "', " + 
                    mdEntryPaymentCy + ", " + 
                    mdEntryPaymentApplication + ", " + 
                    mdConversionRateApplication + ", " + 
                    mdDestinyPaymentApplicationEntryCy + ", " + 
                    mdEntryPayment + ", " + 
                    mdConversionRate + ", " + 
                    mdDestinyPaymentEntryCy + ", " + 
                    mnInstallment + ", " + 
                    mdDocBalancePreviousApplicationCy + ", " + 
                    mdDocBalanceUnpaidApplicationCy_r + ", " + 
                    mdDocBalancePreviousCy + ", " + 
                    mdDocBalanceUnpaidCy_r + ", " + 
                    (mnFkDocYearId_n == 0 ? "NULL, " : mnFkDocYearId_n + ", ") + 
                    (mnFkDocDocId_n == 0 ? "NULL, " : mnFkDocDocId_n + ", ") + 
                    mnFkEntryCurrencyId + ", " +
                    (mnFkPaymentRequestId_n == 0 ? "NULL " : mnFkPaymentRequestId_n + " ") + 
                    ")";

        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_pay = " + mnPkPaymentId + ", " +
                    //"id_ety = " + mnPkEntryId + ", " +
                    "ety_tp = '" + msEntryType + "', " +
                    "ety_pay_cur = " + mdEntryPaymentCy + ", " +
                    "ety_pay_app = " + mdEntryPaymentApplication + ", " +
                    "conv_rate_app = " + mdConversionRateApplication + ", " +
                    "des_pay_app_ety_cur = " + mdDestinyPaymentApplicationEntryCy + ", " +
                    "ety_pay = " + mdEntryPayment + ", " +
                    "conv_rate = " + mdConversionRate + ", " +
                    "des_pay_ety_cur = " + mdDestinyPaymentEntryCy + ", " +
                    "install = " + mnInstallment + ", " +
                    "doc_bal_prev_app_cur = " + mdDocBalancePreviousApplicationCy + ", " +
                    "doc_bal_unpd_app_cur_r = " + mdDocBalanceUnpaidApplicationCy_r + ", " +
                    "doc_bal_prev_cur = " + mdDocBalancePreviousCy + ", " +
                    "doc_bal_unpd_cur_r = " + mdDocBalanceUnpaidCy_r + ", " +
                    "fk_doc_year_n = " + (mnFkDocYearId_n == 0 ? "NULL, " : mnFkDocYearId_n + ", ") +
                    "fk_doc_doc_n = " + (mnFkDocDocId_n == 0 ? "NULL, " : mnFkDocDocId_n + ", ") +
                    "fk_ety_cur = " + mnFkEntryCurrencyId + ", " +
                    "fk_pay_req_n = " + (mnFkPaymentRequestId_n == 0 ? "NULL " : mnFkPaymentRequestId_n + " ") +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPaymentEntry clone() throws CloneNotSupportedException {
        SDbPaymentEntry registry = new SDbPaymentEntry();
        
        registry.setPkPaymentId(this.getPkPaymentId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setEntryType(this.getEntryType());
        registry.setEntryPaymentCy(this.getEntryPaymentCy());
        registry.setEntryPaymentApplication(this.getEntryPaymentApplication());
        registry.setConversionRateApplication(this.getConversionRateApplication());
        registry.setDestinyPaymentApplicationEntryCy(this.getDestinyPaymentApplicationEntryCy());
        registry.setEntryPayment(this.getEntryPayment());
        registry.setConversionRate(this.getConversionRate());
        registry.setDestinyPaymentEntryCy(this.getDestinyPaymentEntryCy());
        registry.setInstallment(this.getInstallment());
        registry.setDocBalancePreviousApplicationCy(this.getDocBalancePreviousApplicationCy());
        registry.setDocBalanceUnpaidApplicationCy_r(this.getDocBalanceUnpaidApplicationCy_r());
        registry.setDocBalancePreviousCy(this.getDocBalancePreviousCy());
        registry.setDocBalanceUnpaidCy_r(this.getDocBalanceUnpaidCy_r());
        registry.setFkDocYearId_n(this.getFkDocYearId_n());
        registry.setFkDocDocId_n(this.getFkDocDocId_n());
        registry.setFkEntryCurrencyId(this.getFkEntryCurrencyId());
        registry.setFkPaymentRequestId_n(this.getFkPaymentRequestId_n());
        
        registry.setRegistryNew(true);
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return mbRegistryEdited;
    }

    @Override
    public void setRowEdited(boolean edited) {
        mbRegistryEdited = edited;
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0: value = msEntryType.equals(ENTRY_TYPE_PAYMENT) ? "Pago a documento" : "Pago simple"; break;
            case 1: value = moDpsRelated == null ? "" : moDpsRelated.getDpsNumber(); break;
            case 2: value = mdEntryPaymentCy; break;
            case 3: value = moPayCurrency.getKey(); break;
            case 4: value = mdDestinyPaymentApplicationEntryCy; break;
            case 5: value = moEntryCurrency.getKey(); break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) { }
}
