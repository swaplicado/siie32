/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mcfg.data.SDataCurrency;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SDbPayment extends SDbRegistryUser {
    
    public static final int PRIORITY_NORMAL = 0;
    public static final int PRIORITY_URGENT = 1;

    public static final int FIELD_STATUS_PAYMENT = FIELD_BASE + 1;
    
    protected int mnPkPaymentId;
    protected String msSeries;
    protected int mnNumber;
    protected Date mtDateApplication;
    protected Date mtDateRequired;
    protected Date mtDateSchedule_n;
    protected Date mtDateExecution_n;
    protected double mdPaymentCy;
    protected double mdPaymentExchangeRateApplication;
    protected double mdPaymentApplication;
    protected double mdPaymentExchangeRate;
    protected double mdPayment;
    protected String msPaymentWay;
    protected int mnPriority;
    protected String msNotes;
    protected String msNotesAuthorization;
    protected boolean mbReceiptPaymentRequired;
    //protected boolean mbDeleted;
    //protected boolean mbSystem;
    protected int mnFkStatusPaymentId;
    protected int mnFkCurrencyId;
    protected int mnFkBeneficiaryId;
    protected int mnFkFunctionalAreaId;
    protected int mnFkFunctionalSubareaId;
    protected int mnFkPayerCashBizPartnerBranchId_n;
    protected int mnFkPayerCashAccountingCashId_n;
    protected int mnFkBeneficiaryBankBizParterBranchId_n;
    protected int mnFkBeneficiaryBankAccountCashId_n;
    protected int mnFkUserScheduleId;
    protected int mnFkUserExecutiondId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserSchedule;
    protected Date mtTsUserExecution;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbPaymentEntry> maChildEntries;
    protected ArrayList<SDbPaymentFile> maFiles;
    protected ArrayList<SDbPaymentFile> maFilesDeleted;
    
    protected String msDbmsStatus;
    protected SDataCurrency moDbmsCurrency;
    
    protected Date mtOldDateSchedule_n;
    protected Date mtOldDateExecution_n;
    protected int mnOldFkUserScheduleId;
    protected int mnOldFkUserExecutiondId;
    
    protected boolean mbAuxReloadEntries;
    
    protected double mnAuxOriginalAmount;
    
    public SDbPayment() {
        super(SModConsts.FIN_PAY);
    }
    
    public void computeNextNumber(final SGuiSession session) throws Exception {
        String sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE ser = '" + msSeries + "' AND NOT b_del;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                mnNumber = resultSet.getInt(1);
            }
        }
    }
    
    private boolean hasChangedSchedule() {
        return mnFkUserScheduleId != mnOldFkUserScheduleId ||
                (mtDateSchedule_n == null && mtOldDateSchedule_n != null) ||
                (mtDateSchedule_n != null && mtOldDateSchedule_n == null) ||
                (mtDateSchedule_n != null && mtOldDateSchedule_n != null && !SLibTimeUtils.isSameDate(mtDateSchedule_n, mtOldDateSchedule_n));
    }
    
    private boolean hasChangedExecution() {
        return mnFkUserExecutiondId != mnOldFkUserExecutiondId ||
                (mtDateExecution_n == null && mtOldDateExecution_n != null) ||
                (mtDateExecution_n != null && mtOldDateExecution_n == null) ||
                (mtDateExecution_n != null && mtOldDateExecution_n != null && !SLibTimeUtils.isSameDate(mtDateExecution_n, mtOldDateExecution_n));
    }
    
    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDateApplication(Date t) { mtDateApplication = t; }
    public void setDateRequired(Date t) { mtDateRequired = t; }
    public void setDateSchedule_n(Date t) { mtDateSchedule_n = t; }
    public void setDateExecution_n(Date t) { mtDateExecution_n = t; }
    public void setPaymentCy(double d) { mdPaymentCy = d; }
    public void setPaymentExchangeRateApplication(double d) { mdPaymentExchangeRateApplication = d; }
    public void setPaymentApplication(double d) { mdPaymentApplication = d; }
    public void setPaymentExchangeRate(double d) { mdPaymentExchangeRate = d; }
    public void setPayment(double d) { mdPayment = d; }
    public void setPaymentWay(String s) { msPaymentWay = s; }
    public void setPriority(int n) { mnPriority = n; }
    public void setNotes(String s) { msNotes = s; }
    public void setNotesAuthorization(String s) { msNotesAuthorization = s; }
    public void setReceiptPaymentRequired(boolean b) { mbReceiptPaymentRequired = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkStatusPaymentId(int n) { mnFkStatusPaymentId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkBeneficiaryId(int n) { mnFkBeneficiaryId = n; }
    public void setFkFunctionalAreaId(int n) { mnFkFunctionalAreaId = n; }
    public void setFkFunctionalSubareaId(int n) { mnFkFunctionalSubareaId = n; }
    public void setFkPayerCashBizPartnerBranchId_n(int n) { mnFkPayerCashBizPartnerBranchId_n = n; }
    public void setFkPayerCashAccountingCashId_n(int n) { mnFkPayerCashAccountingCashId_n = n; }
    public void setFkBeneficiaryBankBizParterBranchId_n(int n) { mnFkBeneficiaryBankBizParterBranchId_n = n; }
    public void setFkBeneficiaryBankAccountCashId_n(int n) { mnFkBeneficiaryBankAccountCashId_n = n; }
    public void setFkUserScheduleId(int n) { mnFkUserScheduleId = n; }
    public void setFkUserExecutiondId(int n) { mnFkUserExecutiondId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserSchedule(Date t) { mtTsUserSchedule = t; }
    public void setTsUserExecution(Date t) { mtTsUserExecution = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkPaymentId() { return mnPkPaymentId; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public Date getDateApplication() { return mtDateApplication; }
    public Date getDateRequired() { return mtDateRequired; }
    public Date getDateSchedule_n() { return mtDateSchedule_n; }
    public Date getDateExecution_n() { return mtDateExecution_n; }
    public double getPaymentCy() { return mdPaymentCy; }
    public double getPaymentExchangeRateApplication() { return mdPaymentExchangeRateApplication; }
    public double getPaymentApplication() { return mdPaymentApplication; }
    public double getPaymentExchangeRate() { return mdPaymentExchangeRate; }
    public double getPayment() { return mdPayment; }
    public String getPaymentWay() { return msPaymentWay; }
    public int getPriority() { return mnPriority; }
    public String getNotes() { return msNotes; }
    public String getNotesAuthorization() { return msNotesAuthorization; }
    public boolean isReceiptPaymentRequired() { return mbReceiptPaymentRequired; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkStatusPaymentId() { return mnFkStatusPaymentId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkBeneficiaryId() { return mnFkBeneficiaryId; }
    public int getFkFunctionalAreaId() { return mnFkFunctionalAreaId; }
    public int getFkFunctionalSubareaId() { return mnFkFunctionalSubareaId; }
    public int getFkPayerCashBizPartnerBranchId_n() { return mnFkPayerCashBizPartnerBranchId_n; }
    public int getFkPayerCashAccountingCashId_n() { return mnFkPayerCashAccountingCashId_n; }
    public int getFkBeneficiaryBankBizParterBranchId_n() { return mnFkBeneficiaryBankBizParterBranchId_n; }
    public int getFkBeneficiaryBankAccountCashId_n() { return mnFkBeneficiaryBankAccountCashId_n; }
    public int getFkUserScheduleId() { return mnFkUserScheduleId; }
    public int getFkUserExecutiondId() { return mnFkUserExecutiondId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserSchedule() { return mtTsUserSchedule; }
    public Date getTsUserExecution() { return mtTsUserExecution; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbPaymentEntry> getChildEntries() { return maChildEntries; }
    public ArrayList<SDbPaymentFile> getFiles() { return maFiles; }
    public ArrayList<SDbPaymentFile> getFilesDeleted() { return maFilesDeleted; }
    
    public void setDbmsStatus(String s) { msDbmsStatus = s; }
    public void setDbmsCurrency(SDataCurrency o) { moDbmsCurrency = o; }
    
    public String getDbmsStatus() { return msDbmsStatus; }
    public SDataCurrency getDbmsCurrency() { return moDbmsCurrency; }
    
    public void setOldDateSchedule_n(Date t) { mtOldDateSchedule_n = t; }
    public void setOldDateExecution_n(Date t) { mtOldDateExecution_n = t; }
    public void setOldFkUserScheduleId(int n) { mnOldFkUserScheduleId = n; }
    public void setOldFkUserExecutiondId(int n) { mnOldFkUserExecutiondId = n; }
    
    public Date getOldDateSchedule_n() { return mtOldDateSchedule_n; }
    public Date getOldDateExecution_n() { return mtOldDateExecution_n; }
    public int getOldFkUserScheduleId() { return mnOldFkUserScheduleId; }
    public int getOldFkUserExecutiondId() { return mnOldFkUserExecutiondId; }
    
    public void setAuxReloadEntries(boolean b) { mbAuxReloadEntries = b; }
    public void setAuxOriginalAmount(double d) { mnAuxOriginalAmount = d; }
    
    public boolean getAuxReloadEntries() { return mbAuxReloadEntries; }
    public double getAuxOriginalAmount() { return mnAuxOriginalAmount; }
    
    public String getFolio() {
        return msSeries + (msSeries.isEmpty() ? "" : "-") + mnNumber;
    }
    
    public void deleteFiles(SGuiSession session) throws Exception {
        for (SDbPaymentFile file : maFiles) {
            file.delete(session);
        }
    }
    
    public void updatePaymentStatus(SGuiSession session, int status) throws Exception {
        msSql = "UPDATE " + getSqlTable() + " SET " +
                "fk_st_pay = " + status + ", " + 
                "fk_usr_upd = " + session.getUser().getPkUserId() + ", " +
                "ts_usr_upd = NOW() ";
                getSqlWhere();
        session.getStatement().execute(msSql);
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
        mtDateSchedule_n = null;
        mtDateExecution_n = null;
        mdPaymentCy = 0;
        mdPaymentExchangeRateApplication = 0;
        mdPaymentApplication = 0;
        mdPaymentExchangeRate = 0;
        mdPayment = 0;
        msPaymentWay = "";
        mnPriority = PRIORITY_NORMAL;
        msNotes = "";
        msNotesAuthorization = "";
        mbReceiptPaymentRequired = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkStatusPaymentId = 0;
        mnFkCurrencyId = 0;
        mnFkBeneficiaryId = 0;
        mnFkFunctionalAreaId = 0;
        mnFkFunctionalSubareaId = 0;
        mnFkPayerCashBizPartnerBranchId_n = 0;
        mnFkPayerCashAccountingCashId_n = 0;
        mnFkBeneficiaryBankBizParterBranchId_n = 0;
        mnFkBeneficiaryBankAccountCashId_n = 0;
        mnFkUserScheduleId = 0;
        mnFkUserExecutiondId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserSchedule = null;
        mtTsUserExecution = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildEntries = new ArrayList<>();
        maFiles = new ArrayList<>();
        maFilesDeleted = new ArrayList<>();
        
        msDbmsStatus = "";
        moDbmsCurrency = null;
        
        mtOldDateSchedule_n = null;
        mtOldDateExecution_n = null;
        mnOldFkUserScheduleId = 0;
        mnOldFkUserExecutiondId = 0;
        
        mbAuxReloadEntries = false;
        mnAuxOriginalAmount = 0;        
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
        
        msSql = "SELECT COALESCE(MAX(id_pay), 0) + 1 "
                + "FROM " + getSqlTable() + " ";
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
            mtDateSchedule_n = resultSet.getDate("dt_sched_n");
            mtDateExecution_n = resultSet.getDate("dt_exec_n");
            mdPaymentCy = resultSet.getDouble("pay_cur");
            mdPaymentExchangeRateApplication = resultSet.getDouble("pay_exc_rate_app");
            mdPaymentApplication = resultSet.getDouble("pay_app");
            mdPaymentExchangeRate = resultSet.getDouble("pay_exc_rate");
            mdPayment = resultSet.getDouble("pay");
            msPaymentWay = resultSet.getString("pay_way");
            mnPriority = resultSet.getInt("priority");
            msNotes = resultSet.getString("nts");
            msNotesAuthorization = resultSet.getString("nts_auth");
            mbReceiptPaymentRequired = resultSet.getBoolean("b_rcpt_pay_req");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkStatusPaymentId = resultSet.getInt("fk_st_pay");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkBeneficiaryId = resultSet.getInt("fk_ben");
            mnFkFunctionalAreaId = resultSet.getInt("fk_func");
            mnFkFunctionalSubareaId = resultSet.getInt("fk_func_sub");
            mnFkPayerCashBizPartnerBranchId_n = resultSet.getInt("fk_pay_cash_cob_n");
            mnFkPayerCashAccountingCashId_n = resultSet.getInt("fk_pay_cash_acc_cash_n");
            mnFkBeneficiaryBankBizParterBranchId_n = resultSet.getInt("fk_ben_bank_cob_n");
            mnFkBeneficiaryBankAccountCashId_n = resultSet.getInt("fk_ben_bank_acc_cash_n");
            mnFkUserScheduleId = resultSet.getInt("fk_usr_sched");
            mnFkUserExecutiondId = resultSet.getInt("fk_usr_exec");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserSchedule = resultSet.getTimestamp("ts_usr_sched");
            mtTsUserExecution = resultSet.getTimestamp("ts_usr_exec");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mtOldDateSchedule_n = mtDateSchedule_n;
            mtOldDateExecution_n = mtDateExecution_n;
            mnOldFkUserScheduleId = mnFkUserScheduleId;
            mnOldFkUserExecutiondId = mnFkUserExecutiondId;
            
            // Read aswell document entries:
            
            msDbmsStatus = (String) session.readField(SModConsts.FINS_ST_PAY, new int[] { mnFkStatusPaymentId }, SDbRegistry.FIELD_NAME);
            
            statement = session.getStatement().getConnection().createStatement();
            
            moDbmsCurrency = new SDataCurrency();
            moDbmsCurrency.read(new int[] { mnFkCurrencyId }, statement);
            
            msSql = "SELECT id_ety " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " " + 
                    "WHERE id_pay = " + mnPkPaymentId + " " + 
                    "ORDER BY id_ety ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                entry = new SDbPaymentEntry();
                entry.read(session, new int[] { mnPkPaymentId, resultSet.getInt(1) });
                entry.setPayCurrency(moDbmsCurrency);
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
            computeNextNumber(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            mnFkUserScheduleId = SUtilConsts.USR_NA_ID;
            mnFkUserExecutiondId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPaymentId + ", " + 
                    "'" + msSeries + "', " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateApplication) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateRequired) + "', " + 
                    (mtDateSchedule_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSchedule_n) + "', ") + 
                    (mtDateExecution_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateExecution_n) + "', ") + 
                    mdPaymentCy + ", " + 
                    mdPaymentExchangeRateApplication + ", " + 
                    mdPaymentApplication + ", " + 
                    mdPaymentExchangeRate + ", " + 
                    mdPayment + ", " + 
                    "'" + msPaymentWay + "', " + 
                    mnPriority + ", " + 
                    "'" + msNotes + "', " + 
                    "'" + msNotesAuthorization + "', " + 
                    (mbReceiptPaymentRequired ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkStatusPaymentId + ", " + 
                    mnFkCurrencyId + ", " + 
                    mnFkBeneficiaryId + ", " + 
                    mnFkFunctionalAreaId + ", " + 
                    mnFkFunctionalSubareaId + ", " + 
                    (mnFkPayerCashBizPartnerBranchId_n == 0 ? "NULL, " : mnFkPayerCashBizPartnerBranchId_n + ", ") + 
                    (mnFkPayerCashAccountingCashId_n == 0 ? "NULL, " : mnFkPayerCashAccountingCashId_n + ", ") + 
                    (mnFkBeneficiaryBankBizParterBranchId_n == 0 ? "NULL, " : mnFkBeneficiaryBankBizParterBranchId_n + ", ") + 
                    (mnFkBeneficiaryBankAccountCashId_n == 0 ? "NULL, " : mnFkBeneficiaryBankAccountCashId_n + ", ") + 
                    mnFkUserScheduleId + ", " + 
                    mnFkUserExecutiondId + ", " + 
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
                    "dt_sched_n = " + (mtDateSchedule_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSchedule_n) + "', ") +
                    "dt_exec_n = " + (mtDateExecution_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateExecution_n) + "', ") +
                    "pay_cur = " + mdPaymentCy + ", " +
                    "pay_exc_rate_app = " + mdPaymentExchangeRateApplication + ", " +
                    "pay_app = " + mdPaymentApplication + ", " +
                    "pay_exc_rate = " + mdPaymentExchangeRate + ", " +
                    "pay = " + mdPayment + ", " +
                    "pay_way = '" + msPaymentWay + "', " +
                    "priority = " + mnPriority + ", " +
                    "nts = '" + msNotes + "', " +
                    "nts_auth = '" + msNotesAuthorization + "', " +
                    "b_rcpt_pay_req = " + (mbReceiptPaymentRequired ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_st_pay = " + mnFkStatusPaymentId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_ben = " + mnFkBeneficiaryId + ", " +
                    "fk_func = " + mnFkFunctionalAreaId + ", " +
                    "fk_func_sub = " + mnFkFunctionalSubareaId + ", " +
                    "fk_pay_cash_cob_n = " + (mnFkPayerCashBizPartnerBranchId_n == 0 ? "NULL, " : mnFkPayerCashBizPartnerBranchId_n + ", ") +
                    "fk_pay_cash_acc_cash_n = " + (mnFkPayerCashAccountingCashId_n == 0 ? "NULL, " : mnFkPayerCashAccountingCashId_n + ", ") +
                    "fk_ben_bank_cob_n = " + (mnFkBeneficiaryBankBizParterBranchId_n == 0 ? "NULL, " : mnFkBeneficiaryBankBizParterBranchId_n + ", ") +
                    "fk_ben_bank_acc_cash_n = " + (mnFkBeneficiaryBankAccountCashId_n == 0 ? "NULL, " : mnFkBeneficiaryBankAccountCashId_n + ", ") +
                    "fk_usr_sched = " + mnFkUserScheduleId + ", " +
                    "fk_usr_exec = " + mnFkUserExecutiondId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    (hasChangedSchedule() ? "ts_usr_sched = NOW(), " : "") +
                    (hasChangedExecution()? "ts_usr_exec = NOW(), " : "") +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        if (mbRegistryNew || mbAuxReloadEntries) {
            msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " " + 
                    getSqlWhere();
            
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
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        int newStatusPaymentId = 0;
        int userId = 0;
        String database = "";
        
        if (value instanceof Object[]) {
            newStatusPaymentId = (Integer) ((Object[]) value)[0];
            userId = (Integer) ((Object[]) value)[1];
            database = (String) ((Object[]) value)[2];
            
            switch (newStatusPaymentId) {
                case SModSysConsts.FINS_ST_PAY_PRC_AUTH:
                case SModSysConsts.FINS_ST_PAY_REJ:
                //case SModSysConsts.FINS_ST_PAY_SCHED: // change to scheduled with updateStatusToScheduled()
                //case SModSysConsts.FINS_ST_PAY_EXEC: // change to executed with updateStatusToExecuted()
                case SModSysConsts.FINS_ST_PAY_SUBR:
                case SModSysConsts.FINS_ST_PAY_RCPT:
                case SModSysConsts.FINS_ST_PAY_CAN:
                    break;
                    
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Nuevo estatus '" + newStatusPaymentId + "'.)");
            }
        }
        else {
            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Argumento 'value'.)");
        }

        msSql = "UPDATE " + (!database.isEmpty() ? database + "." : "") + getSqlTable() + " SET ";
        
        switch (field) {
            case FIELD_STATUS_PAYMENT:
                msSql += "fk_st_pay = " + newStatusPaymentId + ", ";
                msSql += "fk_usr_upd = " + userId + ", ";
                msSql += "ts_usr_upd = NOW() ";
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Dato a actualizar '" + field + "'.)");
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        
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
        registry.setDateSchedule_n(this.getDateSchedule_n());
        registry.setDateExecution_n(this.getDateExecution_n());
        registry.setPaymentCy(this.getPaymentCy());
        registry.setPaymentExchangeRateApplication(this.getPaymentExchangeRateApplication());
        registry.setPaymentApplication(this.getPaymentApplication());
        registry.setPaymentExchangeRate(this.getPaymentExchangeRate());
        registry.setPayment(this.getPayment());
        registry.setPaymentWay(this.getPaymentWay());
        registry.setPriority(this.getPriority());
        registry.setNotes(this.getNotes());
        registry.setNotesAuthorization(this.getNotesAuthorization());
        registry.setReceiptPaymentRequired(this.isReceiptPaymentRequired());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkStatusPaymentId(this.getFkStatusPaymentId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkBeneficiaryId(this.getFkBeneficiaryId());
        registry.setFkFunctionalAreaId(this.getFkFunctionalAreaId());
        registry.setFkFunctionalSubareaId(this.getFkFunctionalSubareaId());
        registry.setFkPayerCashBizPartnerBranchId_n(this.getFkPayerCashBizPartnerBranchId_n());
        registry.setFkPayerCashAccountingCashId_n(this.getFkPayerCashAccountingCashId_n());
        registry.setFkBeneficiaryBankBizParterBranchId_n(this.getFkBeneficiaryBankBizParterBranchId_n());
        registry.setFkBeneficiaryBankAccountCashId_n(this.getFkBeneficiaryBankAccountCashId_n());
        registry.setFkUserScheduleId(this.getFkUserScheduleId());
        registry.setFkUserExecutiondId(this.getFkUserExecutiondId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserSchedule(this.getTsUserSchedule());
        registry.setTsUserExecution(this.getTsUserExecution());
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
        
        registry.setDbmsStatus(this.getDbmsStatus());
        registry.setDbmsCurrency(this.getDbmsCurrency());
        
        registry.setOldDateSchedule_n(this.getOldDateSchedule_n());
        registry.setOldDateExecution_n(this.getOldDateExecution_n());
        registry.setOldFkUserScheduleId(this.getOldFkUserScheduleId());
        registry.setOldFkUserExecutiondId(this.getOldFkUserExecutiondId());
        
        registry.setAuxReloadEntries(this.getAuxReloadEntries());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
    
    /**
     * Check if payment exists.
     * @param statement DB statement.
     * @param database DB name. Can be blank for current DB in statement.
     * @param paymentId Payment ID.
     * @return
     * @throws Exception 
     */
    public static boolean checkPaymentExists(final Statement statement, final String database, final int paymentId) throws Exception {
        boolean exists = false;
        
        String sql = "SELECT COUNT(*) "
                + "FROM " + (!database.isEmpty() ? database + "." : "") + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " "
                + "WHERE id_pay = " + paymentId + ";";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                exists = resultSet.getBoolean(1);
            }
        }
        
        return exists;
    }
    
    /**
     * Update status of payment to schedule.
     * @param statement DB statement.
     * @param database DB name. Can be blank for current DB in statement.
     * @param paymentId Payment ID.
     * @param dateScheduled Date scheduled. Can be <code>null</code> for original required date.
     * @param userId User ID.
     * @throws Exception 
     */
    public static void updateStatusSchedule(final Statement statement, final String database, final int paymentId, final Date dateScheduled, final int userId) throws Exception {
        if (!checkPaymentExists(statement, null, paymentId)) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n(Pago ID " + paymentId + (!database.isEmpty() ? "; en BD '" + database + "'" : "") +".)");
        }
        else {
            String sql = "UPDATE " + (!database.isEmpty() ? database + "." : "") + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " SET "
                    + "fk_st_pay = " + SModSysConsts.FINS_ST_PAY_SCHED + ", "
                    + "dt_sched_n = " + (dateScheduled != null ? "'" + SLibUtils.DbmsDateFormatDate.format(dateScheduled) + "'": "dt_req") + ", "
                    + "fk_usr_sched = " + userId + ", "
                    + "ts_usr_sched = NOW(), "
                    + "fk_usr_upd = " + userId + ", "
                    + "ts_usr_upd = NOW() "
                    + "WHERE id_pay = " + paymentId + ";";

            statement.execute(sql);
        }
    }
}
