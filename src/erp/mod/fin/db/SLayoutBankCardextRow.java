/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModSysConsts;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Uriel Castañeda
 */
public class SLayoutBankCardextRow implements SGridRow {

    protected SGuiClient miClient;
    
    protected int mnLayoutRowType;
    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchBankAccountId;
    protected java.lang.String msBizPartner;
    protected java.lang.String msBizPartnerKey;
    protected java.lang.String msBizPartnerBranch;
    protected java.lang.String msSantanderBankCode;
    protected java.lang.String msTypeDps;
    protected java.lang.String msNumberSer;
    protected java.util.Date mtDate;
    protected java.lang.String msBizPartnerBranchCob;
    protected SMoney moBalanceTot;
    protected double mdBalanceTotByBizPartner;
    protected double mdBalancePayed;
    protected java.lang.String msCurrencyKey;
    protected java.lang.String msCurrencyKeyCy;
    protected java.lang.String msAccountCredit;
    protected String msObservation;
    protected String msRecordPeriod;
    protected String msRecordBkc;
    protected String msRecordCob;
    protected String msRecordNumber;
    protected Date mtRecordDate;
    
    
    public SLayoutBankCardextRow(SGuiClient client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mnLayoutRowType = 0;
        mnPkYearId = 0;
        mnPkDocId = 0;
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerBranchBankAccountId = 0;
        msBizPartner = "";
        msBizPartnerKey = "";
        msBizPartnerBranch = "";
        msSantanderBankCode = "";
        msTypeDps = "";
        msNumberSer = "";
        mtDate = null;
        msBizPartnerBranchCob = "";
        moBalanceTot = null;
        mdBalanceTotByBizPartner = 0;
        mdBalancePayed = 0;
        msCurrencyKey = "";
        msCurrencyKeyCy = "";
        msAccountCredit = "";
        msObservation = "";
        msRecordPeriod = "";
        msRecordBkc = "";
        msRecordCob = "";
        msRecordNumber = "";
        mtRecordDate = null;
        
    }

    public void setLayoutRowType(int n) { mnLayoutRowType = n; }
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchAccountId(int n) { mnBizPartnerBranchBankAccountId = n; }
    public void setBizPartner(java.lang.String s) { msBizPartner = s; }
    public void setBizPartnerKey(java.lang.String s) { msBizPartnerKey = s; }
    public void setBizPartnerBranch(String s) { msBizPartnerBranch = s; }
    public void setSantanderBankCode(String s) { msSantanderBankCode = s; }
    public void setTypeDps(String s) { msTypeDps = s; }
    public void setNumberSer(String s) { msNumberSer = s; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setBizPartnerBranchCob(String s) { msBizPartnerBranchCob = s; }
    public void setBalanceTot(SMoney o) { moBalanceTot = o; }
    public void setBalanceTotByBizPartner(double d) { mdBalanceTotByBizPartner = d; }
    public void setBalancePayed(double d) { mdBalancePayed = d; }
    public void setCurrencyKey(java.lang.String s) { msCurrencyKey = s; }
    public void setCurrencyKeyCy(java.lang.String s) { msCurrencyKeyCy = s; }
    public void setAccountCredit(java.lang.String s) { msAccountCredit = s; }
    public void setExchangeRate(double d) { moBalanceTot.setExchangeRate(d); }
    public void setObservation(String s) { msObservation = s; }
    public void setRecordPeriod(String s) { msRecordPeriod = s; }
    public void setRecordBkc(String s) { msRecordBkc = s; }
    public void setRecordCob(String s) { msRecordCob = s; }
    public void setRecordNumber(String s) { msRecordNumber = s; }
    public void setRecordDate(Date t) { mtRecordDate = t; }
    
    public void setPrimaryKey(int[] pk) {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        
        if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_DPS) {
            mnPkYearId = pk[0];
            mnPkDocId = pk[1];
        }
        else if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_ADV) {
            mnBizPartnerId = pk[0];
        }
        
    }
    
    public int getLayoutRowType() { return mnLayoutRowType; }
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchAccountId() { return mnBizPartnerBranchBankAccountId; }
    public java.lang.String getBizPartner() { return msBizPartner; }
    public java.lang.String getBizPartnerKey() { return msBizPartnerKey; }
    public java.lang.String getBizPartnerBranch() { return msBizPartnerBranch; }
    public java.lang.String getSantanderBankCode() { return msSantanderBankCode; }
    public java.lang.String getTypeDps() { return msTypeDps; }
    public java.lang.String getNumberSer() { return msNumberSer; }
    public java.util.Date getDate() { return mtDate; }
    public SMoney getBalanceTot() { return moBalanceTot; }
    public double getBalanceTotByBizPartner() { return mdBalanceTotByBizPartner; }
    public double getBalancePayed() { return mdBalancePayed; }
    public java.lang.String getCurrencyKey() { return msCurrencyKey; }
    public java.lang.String getCurrencyKeyCy() { return msCurrencyKeyCy; }
    public java.lang.String getAccountCredit() { return msAccountCredit; }
    public String getObservation() { return msObservation; }
    public String getRecordPeriod() { return msRecordPeriod; }
    public String getRecordBkc() { return msRecordBkc; }
    public String getRecordCob() { return msRecordCob; }
    public String getRecordNumber() { return msRecordNumber; }
    public Date getRecordDate() { return mtRecordDate; }
    
    @Override
    public int[] getRowPrimaryKey() {
        int[] key = new int[] { SLibConsts.UNDEFINED };
        
        if (mnLayoutRowType == SModSysConsts.FIN_LAY_BANK_DPS) {
            key = new int[] { mnPkYearId, mnPkDocId };
        }

        return key;
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msBizPartner;
                break;
            case 1:
                value = msBizPartnerKey;
                break;
            case 2:
                value = msTypeDps;
                break;
            case 3:
                value = msNumberSer;
                break;
            case 4:
                value = mtDate;
                break;
            case 5:
                value = msBizPartnerBranchCob;
                break;
            case 6:
                value = moBalanceTot.getAmountOriginal(); 
                break;
            case 7:
                value =  msCurrencyKey;
                break;    
            case 8:
                value = moBalanceTot.getExchangeRate(); 
                break;     
            case 9:
                value = msAccountCredit; 
                break;
            case 10:
                value = msRecordPeriod;
                break;
            case 11:
                value = msRecordBkc;
                break;
            case 12:
                value = msRecordCob;
                break;
            case 13:
                value = msRecordNumber;
                break;
            case 14:
                value = mtRecordDate;
                break;
            case 15:
                value = msObservation;
                break;    
           default:
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
       
    
}
