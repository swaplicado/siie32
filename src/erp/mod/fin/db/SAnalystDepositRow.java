/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataRecord;
import java.util.Date;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;

/**
 * Structure of import lines
 * 
 * @author Edwin Carmona
 */
public class SAnalystDepositRow implements SGridRow {

    protected SGuiClient miClient;
    
    protected int mnPkDepositId;
    protected int mnPkAnalystId;
    protected int mnBizPartnerId;
    protected int mnCurrencyId;
    protected java.util.Date mtDateDeposit;
    protected java.lang.String msBizPartner;
    protected java.lang.String msBizPartnerRFC;
    protected java.lang.String msUserAnalyst;
    protected java.lang.String msReference;
    protected java.lang.String msConcept;
    protected java.lang.String msNumberTx;
    protected java.lang.String msPaymentType;
    protected double mdAmountOriCurrency;
    protected java.lang.String msCurrency;
    protected boolean mbImported;
    protected java.lang.String msReferenceAdv;
    protected double mdExhangeRate;
    protected double mdAmountLocal;
    protected java.lang.String msRecordPeriod;
    protected java.lang.String msRecordBkc;
    protected java.lang.String msRecordBranch;
    protected java.lang.String msRecordNumber;
    protected java.lang.String msRecordConcept;
    protected java.lang.String msBizPartnerAccountId;
    protected int mnBkcYear;
    protected int mnBkcNum;
    
    protected SDataBizPartner moBizPartner;
    protected SDataRecord moRecord;

    public SAnalystDepositRow(SGuiClient client) {
        miClient = client;
        reset();
    }

    public void reset() {
        mnPkDepositId = 0;
        mnPkAnalystId = 0;
        mnBizPartnerId = 0;
        mnCurrencyId = 0;
        mtDateDeposit = null;
        msBizPartner = "";
        msBizPartnerRFC = "";
        msUserAnalyst = "";
        msReference = "";
        msConcept = "";
        msNumberTx = "";
        msPaymentType = "";
        mdAmountOriCurrency = 0;
        msCurrency = "";
        mbImported = false;
        msReferenceAdv = "";
        mdExhangeRate = 0;
        mdAmountLocal = 0;
        msRecordPeriod = "";
        msRecordBkc = "";
        msRecordBranch = "";
        msRecordNumber = "";
        msRecordConcept = "";
        msBizPartnerAccountId = "";
        mnBkcYear = 0;
        mnBkcNum = 0;
    }

    public void setPkDepositId(int n) { mnPkDepositId = n; }
    public void setPkAnalystId(int n) { mnPkAnalystId = n; }
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setDateDeposit(Date t) { mtDateDeposit = t; }
    public void setUserAnalyst(String s) { msUserAnalyst = s; }
    public void setReference(String s) { msReference = s; }
    public void setConcept(String s) { msConcept = s; }
    public void setNumberTx(String s) { msNumberTx = s; }
    public void setPaymentType(String s) { msPaymentType = s; }
    public void setAmountOrigCurrency(double d) { mdAmountOriCurrency = d; }
    public void setCurrency(String s) { msCurrency = s; }
    public void setImported(boolean b) { mbImported = b; }
    public void setReferenceAdv(String s) { msReferenceAdv = s; }
    public void setExchangeRate(double d) { mdExhangeRate = d; }
    public void setAmountLocal(double d) { mdAmountLocal = d; }
    public void setBizPartnerAccountId(String s) { msBizPartnerAccountId = s; }
    public void setBkcYear(int n) { mnBkcYear = n; }
    public void setBkcNum(int n) { mnBkcNum = n; }
    
    public void setRecord(SDataRecord o) { moRecord = o; }
    public void setBizPartner(SDataBizPartner o) { moBizPartner = o; }
    
    public int getPkDepositId() { return mnPkDepositId; }
    public int getPkAnalystId() { return mnPkAnalystId; }
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getCurrencyId() { return mnCurrencyId; }
    public Date getDateDeposit() { return mtDateDeposit; }
    public String getBizPartnerName() { return moBizPartner.getBizPartner(); }
    public String getBizPartnerRFC() { return moBizPartner.getFiscalId(); }
    public String getUserAnalyst() { return msUserAnalyst; }
    public String getReference() { return msReference; }
    public String getConcept() { return msConcept; }
    public String getNumberTx() { return msNumberTx; }
    public String getPaymentType() { return msPaymentType; }
    public double getAmountOrigCurrency() { return mdAmountOriCurrency; }
    public String getCurrency() { return msCurrency; }
    public boolean getImported() { return mbImported; }
    public String getReferenceAdv() { return msReferenceAdv; }
    public double getExchangeRate() { return mdExhangeRate; }
    public double getAmountLocal() { return mdAmountLocal; }
    public String getRecordPeriod() { return moRecord.getRecordPeriod(); }
    public String getRecordBkc() { return moRecord.getPkBookkeepingCenterId() + ""; }
    public String getRecordBranch() { return moRecord.getFkCompanyBranchId() + ""; }
    public String getRecordNumber() { return moRecord.getRecordNumber(); }
    public String getRecordConcept() { return moRecord.getConcept(); }
    public String getBizPartnerAccountId() { return msBizPartnerAccountId; }
    public int getBkcYear() { return mnBkcYear; }
    public int getBkcNum() { return mnBkcNum; }
    
    public SDataRecord getRecord() { return moRecord; }
    public SDataBizPartner getBizPartner() { return moBizPartner; }
    
    public void setPrimaryKey(int[] pk) {
        mnPkDepositId = pk[0];
        mnPkAnalystId= pk[1];
    }

    @Override
    public int[] getRowPrimaryKey() {
        int[] key = new int[] { mnPkDepositId, mnPkAnalystId };

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
                        value = mnPkDepositId;
                        break;
                    case 1:
                        value = mtDateDeposit;
                        break;
                    case 2:
                        value = moBizPartner != null ? moBizPartner.getBizPartner() : "N/I";
                        break;
                    case 3:
                        value = moBizPartner != null ? moBizPartner.getFiscalId() : "N/I";
                        break;
                    case 4:
                        value = msUserAnalyst;
                        break;
                    case 5:
                        value = msReference;
                        break;
                    case 6:
                        value = msConcept;
                        break;
                    case 7:
                        value = msNumberTx;
                        break;
                    case 8:
                        value = msPaymentType;
                        break;
                    case 9:
                        value = mdAmountOriCurrency;
                        break;
                    case 10:
                        value = msCurrency;
                        break;    
                    case 11:
                        value = mbImported;
                        break;
                    case 12:
                        value = msReferenceAdv;
                        break;
                    case 13:
                        value = mdExhangeRate;
                        break;
                    case 14:
                        value = mdAmountLocal;
                        break;
                    case 15:
                        value = moRecord != null && moRecord.getPkYearId() != 0 ? moRecord.getRecordPeriod() : "";
                        break;
                    case 16:
                        value =  moRecord != null && moRecord.getPkYearId() != 0 ? moRecord.getPkBookkeepingCenterId() + "" : "";
                        break;
                    case 17:
                        value =  moRecord != null && moRecord.getPkYearId() != 0 ? moRecord.getFkCompanyBranchId() + "" : "";
                        break;
                    case 18:
                        value =  moRecord != null && moRecord.getPkYearId() != 0 ? moRecord.getRecordNumber() : "";
                        break;
                    case 19:
                        value =  moRecord != null && moRecord.getPkYearId() != 0 ? moRecord.getConcept() : "";
                        break;
                   default:
            }
        
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch (row) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9: 
                break;
            case 10:
                break;
            case 11:
                mbImported = (boolean) value;
                break;    
            case 12:
                msReferenceAdv = (String) value;
                break;
            case 13:
                mdExhangeRate = (double) value;
                break;
            case 14:
                break;
            case 15:
                break;
            case 16:
                break;
            case 17:
                break;
            case 18:
                break;
            case 19:
                break;
            default:
                break;
        }
    }
}
