/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mcfg.data.SDataCurrency;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbPayment extends SDbRegistryUser {

    protected int mnPkPaymentId;
    protected String msSeries;
    protected int mnNumber;
    protected Date mtDateApplication;
    protected Date mtDateRequired;
    protected Date mtDateScheduler_n;
    protected Date mtDateExecute_n;
    protected double mdPaymentCurrency;
    protected double mdPaymentExchangeRateApplication;
    protected double mdPaymentApplication;
    protected double mdPaymentExchangeRate;
    protected double mdPayment;
    protected String msPaymentWay;
    protected int mnPriority;
    protected String msNotes;
    //protected boolean mbDeleted;
    protected int mnFkStatusPaymentId;
    protected int mnFkCurrencyId;
    protected int mnFkBeneficiaryId;
    protected int mnFkFunctionalAreaId;
    protected int mnFkFunctionalSubareaId;
    protected int mnFkPayerCashBizPartnerBranchId_n;
    protected int mnFkPayerCashAccountingCashId_n;
    protected int mnFkBeneficiaryBankBizParterBranchId_n;
    protected int mnFkBeneficiaryBankAccountCashId_n;
    protected int mnFkUserScheduledId;
    protected int mnFkUserExecutedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserScheduledId;
    protected Date mtTsUserExecuted;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbPaymentEntry> maChildEntries;
    protected ArrayList<SDbPaymentFile> maFiles;
    protected ArrayList<SDbPaymentFile> maFilesDeleted;
    protected SDataCurrency moCurrency;
    
    protected boolean mbAuxReloadEntries;
    
    public SDbPayment() {
        super(SModConsts.FIN_PAY);
    }
    
    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDateApplication(Date t) { mtDateApplication = t; }
    public void setDateRequired(Date t) { mtDateRequired = t; }
    public void setDateScheduler_n(Date t) { mtDateScheduler_n = t; }
    public void setDateExecute_n(Date t) { mtDateExecute_n = t; }
    public void setPaymentCurrency(double d) { mdPaymentCurrency = d; }
    public void setPaymentExchangeRateApplication(double d) { mdPaymentExchangeRateApplication = d; }
    public void setPaymentApplication(double d) { mdPaymentApplication = d; }
    public void setPaymentExchangeRate(double d) { mdPaymentExchangeRate = d; }
    public void setPayment(double d) { mdPayment = d; }
    public void setPaymentWay(String s) { msPaymentWay = s; }
    public void setPriority(int n) { mnPriority = n; }
    public void setNotes(String s) { msNotes = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkStatusPaymentId(int n) { mnFkStatusPaymentId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkBeneficiaryId(int n) { mnFkBeneficiaryId = n; }
    public void setFkFunctionalAreaId(int n) { mnFkFunctionalAreaId = n; }
    public void setFkFunctionalSubareaId(int n) { mnFkFunctionalSubareaId = n; }
    public void setFkPayerCashBizPartnerBranchId_n(int n) { mnFkPayerCashBizPartnerBranchId_n = n; }
    public void setFkPayerCashAccountingCashId_n(int n) { mnFkPayerCashAccountingCashId_n = n; }
    public void setFkBeneficiaryBankBizParterBranchId_n(int n) { mnFkBeneficiaryBankBizParterBranchId_n = n; }
    public void setFkBeneficiaryBankAccountCashId_n(int n) { mnFkBeneficiaryBankAccountCashId_n = n; }
    public void setFkUserScheduledId(int n) { mnFkUserScheduledId = n; }
    public void setFkUserExecutedId(int n) { mnFkUserExecutedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserScheduledId(Date t) { mtTsUserScheduledId = t; }
    public void setTsUserExecuted(Date t) { mtTsUserExecuted = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxReloadEntries(boolean b) { mbAuxReloadEntries = b; }
    
    public int getPkPaymentId() { return mnPkPaymentId; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public Date getDateApplication() { return mtDateApplication; }
    public Date getDateRequired() { return mtDateRequired; }
    public Date getDateScheduler_n() { return mtDateScheduler_n; }
    public Date getDateExecute_n() { return mtDateExecute_n; }
    public double getPaymentCurrency() { return mdPaymentCurrency; }
    public double getPaymentExchangeRateApplication() { return mdPaymentExchangeRateApplication; }
    public double getPaymentApplication() { return mdPaymentApplication; }
    public double getPaymentExchangeRate() { return mdPaymentExchangeRate; }
    public double getPayment() { return mdPayment; }
    public String getPaymentWay() { return msPaymentWay; }
    public int getPriority() { return mnPriority; }
    public String getNotes() { return msNotes; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkStatusPaymentId() { return mnFkStatusPaymentId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkBeneficiaryId() { return mnFkBeneficiaryId; }
    public int getFkFunctionalAreaId() { return mnFkFunctionalAreaId; }
    public int getFkFunctionalSubareaId() { return mnFkFunctionalSubareaId; }
    public int getFkPayerCashBizPartnerBranchId_n() { return mnFkPayerCashBizPartnerBranchId_n; }
    public int getFkPayerCashAccountingCashId_n() { return mnFkPayerCashAccountingCashId_n; }
    public int getFkBeneficiaryBankBizParterBranchId_n() { return mnFkBeneficiaryBankBizParterBranchId_n; }
    public int getFkBeneficiaryBankAccountCashId_n() { return mnFkBeneficiaryBankAccountCashId_n; }
    public int getFkUserScheduledId() { return mnFkUserScheduledId; }
    public int getFkUserExecutedId() { return mnFkUserExecutedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserScheduledId() { return mtTsUserScheduledId; }
    public Date getTsUserExecuted() { return mtTsUserExecuted; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbPaymentEntry> getChildEntries() { return maChildEntries; }
    public ArrayList<SDbPaymentFile> getFiles() { return maFiles; }
    public ArrayList<SDbPaymentFile> getFilesDeleted() { return maFilesDeleted; }
    public SDataCurrency getCurrency() { return moCurrency; }
    public boolean getAuxReloadEntries() { return mbAuxReloadEntries; }
    
    public void deleteFiles(SGuiSession session) throws Exception {
        for (SDbPaymentFile file : maFiles) {
            file.delete(session);
        }
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPaymentId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPaymentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkPaymentId = 0;
        msSeries = "";
        mnNumber = 0;
        mtDateApplication = null;
        mtDateRequired = null;
        mtDateScheduler_n = null;
        mtDateExecute_n = null;
        mdPaymentCurrency = 0;
        mdPaymentExchangeRateApplication = 0;
        mdPaymentApplication = 0;
        mdPaymentExchangeRate = 0;
        mdPayment = 0;
        msPaymentWay = "";
        mnPriority = 0;
        msNotes = "";
        mbDeleted = false;
        mnFkStatusPaymentId = 0;
        mnFkCurrencyId = 0;
        mnFkBeneficiaryId = 0;
        mnFkFunctionalAreaId = 0;
        mnFkFunctionalSubareaId = 0;
        mnFkPayerCashBizPartnerBranchId_n = 0;
        mnFkPayerCashAccountingCashId_n = 0;
        mnFkBeneficiaryBankBizParterBranchId_n = 0;
        mnFkBeneficiaryBankAccountCashId_n = 0;
        mnFkUserScheduledId = 0;
        mnFkUserExecutedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserScheduledId = null;
        mtTsUserExecuted = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildEntries = new ArrayList<>();
        maFiles = new ArrayList<>();
        maFilesDeleted = new ArrayList<>();
        moCurrency = null;
        
        mbAuxReloadEntries = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPaymentId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkPaymentId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_pay), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkPaymentId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        SDbPaymentEntry entry;
        SDbPaymentFile file;
        
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
            msSeries = resultSet.getString("ser");
            mnNumber = resultSet.getInt("num");
            mtDateApplication = resultSet.getDate("dt_app");
            mtDateRequired = resultSet.getDate("dt_req");
            mtDateScheduler_n = resultSet.getDate("dt_sched_n");
            mtDateExecute_n = resultSet.getDate("dt_exec_n");
            mdPaymentCurrency = resultSet.getDouble("pay_cur");
            mdPaymentExchangeRateApplication = resultSet.getDouble("pay_exc_rate_app");
            mdPaymentApplication = resultSet.getDouble("pay_app");
            mdPaymentExchangeRate = resultSet.getDouble("pay_exc_rate");
            mdPayment = resultSet.getDouble("pay");
            msPaymentWay = resultSet.getString("pay_way");
            mnPriority = resultSet.getInt("priority");
            msNotes = resultSet.getString("nts");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkStatusPaymentId = resultSet.getInt("fk_st_pay");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkBeneficiaryId = resultSet.getInt("fk_ben");
            mnFkFunctionalAreaId = resultSet.getInt("fk_func");
            mnFkFunctionalSubareaId = resultSet.getInt("fk_func_sub");
            mnFkPayerCashBizPartnerBranchId_n = resultSet.getInt("fk_pay_cash_cob_n");
            mnFkPayerCashAccountingCashId_n = resultSet.getInt("fk_pay_cash_acc_cash_n");
            mnFkBeneficiaryBankBizParterBranchId_n = resultSet.getInt("fk_ben_bank_cob_n");
            mnFkBeneficiaryBankAccountCashId_n = resultSet.getInt("fk_ben_bank_acc_cash_n");
            mnFkUserScheduledId = resultSet.getInt("fk_usr_sched");
            mnFkUserExecutedId = resultSet.getInt("fk_usr_exec");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserScheduledId = resultSet.getTimestamp("ts_usr_sched");
            mtTsUserExecuted = resultSet.getTimestamp("ts_usr_exec");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read aswell document entries:
            
            statement = session.getStatement().getConnection().createStatement();
            
            moCurrency = new SDataCurrency();
            moCurrency.read(new int[] { mnFkCurrencyId }, statement);
            
            msSql = "SELECT id_ety " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " " + 
                    "WHERE id_pay = " + mnPkPaymentId + " " + 
                    "ORDER BY id_ety ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                entry = new SDbPaymentEntry();
                entry.read(session, new int[] { mnPkPaymentId, resultSet.getInt(1) });
                entry.setPayCurrency(moCurrency);
                maChildEntries.add(entry);
            }
            
            msSql = "SELECT id_file " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_FILE) + " " + 
                    "WHERE id_pay = " + mnPkPaymentId + " " + 
                    "ORDER BY id_file ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                file = new SDbPaymentFile();
                file.read(session, new int[] { mnPkPaymentId, resultSet.getInt(1) });
                maFiles.add(file);
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
            mnFkUserScheduledId = SUtilConsts.USR_NA_ID;
            mnFkUserExecutedId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPaymentId + ", " + 
                    "'" + msSeries + "', " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateApplication) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateRequired) + "', " + 
                    (mtDateScheduler_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateScheduler_n) + "', ") + 
                    (mtDateExecute_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateExecute_n) + "', ") + 
                    mdPaymentCurrency + ", " + 
                    mdPaymentExchangeRateApplication + ", " + 
                    mdPaymentApplication + ", " + 
                    mdPaymentExchangeRate + ", " + 
                    mdPayment + ", " + 
                    "'" + msPaymentWay + "', " + 
                    mnPriority + ", " + 
                    "'" + msNotes + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkStatusPaymentId + ", " + 
                    mnFkCurrencyId + ", " + 
                    mnFkBeneficiaryId + ", " + 
                    mnFkFunctionalAreaId + ", " + 
                    mnFkFunctionalSubareaId + ", " + 
                    (mnFkPayerCashBizPartnerBranchId_n == 0 ? "NULL, " : mnFkPayerCashBizPartnerBranchId_n + ", ") + 
                    (mnFkPayerCashAccountingCashId_n == 0 ? "NULL, " : mnFkPayerCashAccountingCashId_n + ", ") + 
                    (mnFkBeneficiaryBankBizParterBranchId_n == 0 ? "NULL, " : mnFkBeneficiaryBankBizParterBranchId_n + ", ") + 
                    (mnFkBeneficiaryBankAccountCashId_n == 0 ? "NULL, " : mnFkBeneficiaryBankAccountCashId_n + ", ") + 
                    mnFkUserScheduledId + ", " + 
                    mnFkUserExecutedId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_pay = " + mnPkPaymentId + ", " +
                    "ser = '" + msSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "dt_app = '" + SLibUtils.DbmsDateFormatDate.format(mtDateApplication) + "', " +
                    "dt_req = '" + SLibUtils.DbmsDateFormatDate.format(mtDateRequired) + "', " +
                    "dt_sched_n = " + (mtDateScheduler_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateScheduler_n) + "', ") +
                    "dt_exec_n = " + (mtDateExecute_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateExecute_n) + "', ") +
                    "pay_cur = " + mdPaymentCurrency + ", " +
                    "pay_exc_rate_app = " + mdPaymentExchangeRateApplication + ", " +
                    "pay_app = " + mdPaymentApplication + ", " +
                    "pay_exc_rate = " + mdPaymentExchangeRate + ", " +
                    "pay = " + mdPayment + ", " +
                    "pay_way = '" + msPaymentWay + "', " +
                    "priority = " + mnPriority + ", " +
                    "nts = '" + msNotes + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_st_pay = " + mnFkStatusPaymentId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_ben = " + mnFkBeneficiaryId + ", " +
                    "fk_func = " + mnFkFunctionalAreaId + ", " +
                    "fk_func_sub = " + mnFkFunctionalSubareaId + ", " +
                    "fk_pay_cash_cob_n = " + (mnFkPayerCashBizPartnerBranchId_n == 0 ? "NULL, " : mnFkPayerCashBizPartnerBranchId_n + ", ") +
                    "fk_pay_cash_acc_cash_n = " + (mnFkPayerCashAccountingCashId_n == 0 ? "NULL, " : mnFkPayerCashAccountingCashId_n + ", ") +
                    "fk_ben_bank_cob_n = " + (mnFkBeneficiaryBankBizParterBranchId_n == 0 ? "NULL, " : mnFkBeneficiaryBankBizParterBranchId_n + ", ") +
                    "fk_ben_bank_acc_cash_n = " + (mnFkBeneficiaryBankAccountCashId_n == 0 ? "NULL, " : mnFkBeneficiaryBankAccountCashId_n + ", ") +
                    "fk_usr_sched = " + mnFkUserScheduledId + ", " +
                    "fk_usr_exec = " + mnFkUserExecutedId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_sched = " + "NOW()" + ", " +
                    "ts_usr_exec = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        if (mbAuxReloadEntries) {
            msSql = "DELETE " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " " + 
                    "WHERE id_pay = " + mnPkPaymentId + " ";
            session.getStatement().execute(msSql);
            for (SDbPaymentEntry entry : maChildEntries) {
                entry.setPkPaymentId(mnPkPaymentId);
                entry.setRegistryNew(true);
                entry.save(session);
            }
        }
        
        for (SDbPaymentFile file : maFilesDeleted) {
            file.delete(session);
        }
        
        for (SDbPaymentFile file : maFiles) {
            file.setPkPaymentId(mnPkPaymentId);
            file.save(session);
        } 
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPayment clone() throws CloneNotSupportedException {
        SDbPayment registry = new SDbPayment();
        
        registry.setPkPaymentId(this.getPkPaymentId());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setDateApplication(this.getDateApplication());
        registry.setDateRequired(this.getDateRequired());
        registry.setDateScheduler_n(this.getDateScheduler_n());
        registry.setDateExecute_n(this.getDateExecute_n());
        registry.setPaymentCurrency(this.getPaymentCurrency());
        registry.setPaymentExchangeRateApplication(this.getPaymentExchangeRateApplication());
        registry.setPaymentApplication(this.getPaymentApplication());
        registry.setPaymentExchangeRate(this.getPaymentExchangeRate());
        registry.setPayment(this.getPayment());
        registry.setPaymentWay(this.getPaymentWay());
        registry.setPriority(this.getPriority());
        registry.setNotes(this.getNotes());
        registry.setDeleted(this.isDeleted());
        registry.setFkStatusPaymentId(this.getFkStatusPaymentId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkBeneficiaryId(this.getFkBeneficiaryId());
        registry.setFkFunctionalAreaId(this.getFkFunctionalAreaId());
        registry.setFkFunctionalSubareaId(this.getFkFunctionalSubareaId());
        registry.setFkPayerCashBizPartnerBranchId_n(this.getFkPayerCashBizPartnerBranchId_n());
        registry.setFkPayerCashAccountingCashId_n(this.getFkPayerCashAccountingCashId_n());
        registry.setFkBeneficiaryBankBizParterBranchId_n(this.getFkBeneficiaryBankBizParterBranchId_n());
        registry.setFkBeneficiaryBankAccountCashId_n(this.getFkBeneficiaryBankAccountCashId_n());
        registry.setFkUserScheduledId(this.getFkUserScheduledId());
        registry.setFkUserExecutedId(this.getFkUserExecutedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserScheduledId(this.getTsUserScheduledId());
        registry.setTsUserExecuted(this.getTsUserExecuted());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbPaymentEntry entry : this.getChildEntries()) {
            registry.getChildEntries().add(entry);
        }
        
        for (SDbPaymentFile file : this.getFiles()) {
            registry.getFiles().add(file);
        }
        
        for (SDbPaymentFile file : this.getFilesDeleted()) {
            registry.getFilesDeleted().add(file);
        }
        
        registry.setAuxReloadEntries(this.getAuxReloadEntries());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
}
