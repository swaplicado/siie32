/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

import erp.mcfg.data.SDataCurrency;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.swap.form.SDocumentUtils;
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
    
    public static final String TYPE_REQUEST = "R";
    public static final String TYPE_PAYMENT = "P";
    
    public static final int PRIORITY_NORMAL = 0;
    public static final int PRIORITY_URGENT = 1;

    public static final String DESC_PRIORITY_NORMAL = "Normal";
    public static final String DESC_PRIORITY_URGENT = "Urgente";
    
    public static final int FIELD_STATUS_PAYMENT = FIELD_BASE + 1;
    
    public static final String ST_NEW = "NUEVO";
    public static final String ST_IN_AUTH = "EN AUTORIZACIÓN";
    public static final String ST_REJC = "RECHAZADO";
    public static final String ST_SCHED = "AUTORIZADO";
    public static final String ST_BLOCK = "BLOQUEADO";
    
    protected int mnPkPaymentId;
    protected String msPaymentType;
    protected String msSeries;
    protected int mnNumber;
    /** Fecha de creación de la solicitud de pago o del pago. */
    protected Date mtDateApplication;
    /** Fecha requerida del pago. */
    protected Date mtDateRequired;
    /** Fecha programada del pago. */
    protected Date mtDateSchedule_n;
    /** Fecha de operación del pago. */
    protected Date mtDateExecution_n;
    /** Monto del pago en la moneda de pago del día de creación de la solicitud de pago o del pago. */
    protected double mdPaymentApplicationCy;
    /** Tipo de cambio de la moneda de pago del día de creación de la solicitud de pago o del pago. */
    protected double mdPaymentExchangeRateApplication;
    /** Monto del pago en la moneda local del día de creación de la solicitud de pago o del pago. */
    protected double mdPaymentApplication;
    /** Monto del pago en la moneda del pago del día de operación del pago. */
    protected double mdPaymentCy;
    /** Tipo de cambio de la moneda del pago del día de operación del pago. */
    protected double mdPaymentExchangeRate;
    /** Monto del pago en la moneda local del día de operación del pago. */
    protected double mdPayment;
    protected String msPaymentWay;
    protected int mnPriority;
    protected String msNotes;
    protected String msNotesAuthorization;
    protected String msNotesAuthorizationFlow;
    protected boolean mbReceiptPaymentRequired;
    protected boolean mbRescheduled;
    protected boolean mbExecutedManually;
    //protected boolean mbDeleted;
    //protected boolean mbSystem;
    protected int mnFkStatusPaymentId;
    /** Moneda de pago (de la solicitud de pago o del pago). */
    protected int mnFkCurrencyId;
    protected int mnFkBeneficiaryId;
    protected int mnFkFunctionalAreaId;
    protected int mnFkFunctionalSubareaId;
    protected int mnFkPayerCashBizPartnerBranchId_n;
    protected int mnFkPayerCashAccountingCashId_n;
    protected int mnFkBeneficiaryBankBizParterBranchId_n;
    protected int mnFkBeneficiaryBankAccountCashId_n;
    protected int mnFkUserScheduleId;
    protected int mnFkUserRescheduleId;
    protected int mnFkUserExecutiondId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserSchedule;
    protected Date mtTsUserReschedule;
    protected Date mtTsUserExecution;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbPaymentEntry> maChildEntries;
    protected ArrayList<SDbPaymentFile> maFiles;
    protected ArrayList<SDbPaymentFile> maFilesDeleted;
    
    protected String msDbmsStatus;
    protected String msDbmsBeneficiary;
    protected SDataCurrency moDbmsDataCurrency;
    
    protected Date mtOldDateSchedule_n;
    protected Date mtOldDateExecution_n;
    protected int mnOldFkUserScheduleId;
    protected int mnOldFkUserRescheduleId;
    protected int mnOldFkUserExecutiondId;
    protected double mdOldPaymentCy;
    protected int mnOldFkCurrencyId;
    
    protected boolean mbAuxReloadEntries;
    protected double mnAuxOriginalAmount;
    
    public SDbPayment() {
        super(SModConsts.FIN_PAY);
    }
    
    private boolean hasChangedSchedule() {
        return mnFkUserRescheduleId != mnOldFkUserRescheduleId ||
                (mtDateSchedule_n == null && mtOldDateSchedule_n != null) ||
                (mtDateSchedule_n != null && mtOldDateSchedule_n == null) ||
                (mtDateSchedule_n != null && mtOldDateSchedule_n != null && !SLibTimeUtils.isSameDate(mtDateSchedule_n, mtOldDateSchedule_n));
    }
    
    private boolean hasChangedReschedule() {
        return mnFkUserRescheduleId != mnOldFkUserRescheduleId ||
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
    public void setPaymentType(String s) { msPaymentType = s; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDateApplication(Date t) { mtDateApplication = t; }
    public void setDateRequired(Date t) { mtDateRequired = t; }
    public void setDateSchedule_n(Date t) { mtDateSchedule_n = t; }
    public void setDateExecution_n(Date t) { mtDateExecution_n = t; }
    public void setPaymentApplicationCy(double d) { mdPaymentApplicationCy = d; }
    public void setPaymentExchangeRateApplication(double d) { mdPaymentExchangeRateApplication = d; }
    public void setPaymentApplication(double d) { mdPaymentApplication = d; }
    public void setPaymentCy(double d) { mdPaymentCy = d; }
    public void setPaymentExchangeRate(double d) { mdPaymentExchangeRate = d; }
    public void setPayment(double d) { mdPayment = d; }
    public void setPaymentWay(String s) { msPaymentWay = s; }
    public void setPriority(int n) { mnPriority = n; }
    public void setNotes(String s) { msNotes = s; }
    public void setNotesAuthorization(String s) { msNotesAuthorization = s; }
    public void setNotesAuthorizationFlow(String s) { msNotesAuthorizationFlow = s; }
    public void setReceiptPaymentRequired(boolean b) { mbReceiptPaymentRequired = b; }
    public void setRescheduled(boolean b) { mbRescheduled = b; }
    public void setExecutedManually(boolean b) { mbExecutedManually = b; }
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
    public void setFkUserRescheduleId(int n) { mnFkUserRescheduleId = n; }
    public void setFkUserExecutiondId(int n) { mnFkUserExecutiondId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserSchedule(Date t) { mtTsUserSchedule = t; }
    public void setTsUserReschedule(Date t) { mtTsUserReschedule = t; }
    public void setTsUserExecution(Date t) { mtTsUserExecution = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkPaymentId() { return mnPkPaymentId; }
    public String getPaymentType() { return msPaymentType; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public Date getDateApplication() { return mtDateApplication; }
    public Date getDateRequired() { return mtDateRequired; }
    public Date getDateSchedule_n() { return mtDateSchedule_n; }
    public Date getDateExecution_n() { return mtDateExecution_n; }
    public double getPaymentApplicationCy() { return mdPaymentApplicationCy; }
    public double getPaymentExchangeRateApplication() { return mdPaymentExchangeRateApplication; }
    public double getPaymentApplication() { return mdPaymentApplication; }
    public double getPaymentCy() { return mdPaymentCy; }
    public double getPaymentExchangeRate() { return mdPaymentExchangeRate; }
    public double getPayment() { return mdPayment; }
    public String getPaymentWay() { return msPaymentWay; }
    public int getPriority() { return mnPriority; }
    public String getNotes() { return msNotes; }
    public String getNotesAuthorization() { return msNotesAuthorization; }
    public String getNotesAuthorizationFlow() { return msNotesAuthorizationFlow; }
    public boolean isReceiptPaymentRequired() { return mbReceiptPaymentRequired; }
    public boolean isRescheduled() { return mbRescheduled; }
    public boolean isExecutedManually() { return mbExecutedManually; }
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
    public int getFkUserRescheduleId() { return mnFkUserRescheduleId; }
    public int getFkUserExecutiondId() { return mnFkUserExecutiondId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserSchedule() { return mtTsUserSchedule; }
    public Date getTsUserReschedule() { return mtTsUserReschedule; }
    public Date getTsUserExecution() { return mtTsUserExecution; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public ArrayList<SDbPaymentEntry> getChildEntries() { return maChildEntries; }
    public ArrayList<SDbPaymentFile> getFiles() { return maFiles; }
    public ArrayList<SDbPaymentFile> getFilesDeleted() { return maFilesDeleted; }
    
    public void setDbmsStatus(String s) { msDbmsStatus = s; }
    public void setDbmsBeneficiary(String s) { msDbmsBeneficiary = s; }
    public void setDbmsDataCurrency(SDataCurrency o) { moDbmsDataCurrency = o; }
    
    public String getDbmsStatus() { return msDbmsStatus; }
    public String getDbmsBeneficiary() { return msDbmsBeneficiary; }
    public SDataCurrency getDbmsDataCurrency() { return moDbmsDataCurrency; }
    
    public void setOldDateSchedule_n(Date t) { mtOldDateSchedule_n = t; }
    public void setOldDateExecution_n(Date t) { mtOldDateExecution_n = t; }
    public void setOldFkUserScheduleId(int n) { mnOldFkUserScheduleId = n; }
    public void setOldFkUserRescheduleId(int n) { mnOldFkUserRescheduleId = n; }
    public void setOldFkUserExecutiondId(int n) { mnOldFkUserExecutiondId = n; }
    public void setOldPaymentCy(double d) { mdOldPaymentCy = d; }
    public void setOldFkCurrencyId(int n) { mnOldFkCurrencyId = n; }
    
    public Date getOldDateSchedule_n() { return mtOldDateSchedule_n; }
    public Date getOldDateExecution_n() { return mtOldDateExecution_n; }
    public int getOldFkUserScheduleId() { return mnOldFkUserScheduleId; }
    public int getOldFkUserRescheduleId() { return mnOldFkUserRescheduleId; }
    public int getOldFkUserExecutiondId() { return mnOldFkUserExecutiondId; }
    public double getOldPaymentCy() { return mdOldPaymentCy; }
    public int getOldFkCurrencyId() { return mnOldFkCurrencyId; }
    
    public void setAuxReloadEntries(boolean b) { mbAuxReloadEntries = b; }
    public void setAuxOriginalAmount(double d) { mnAuxOriginalAmount = d; }
    
    public boolean getAuxReloadEntries() { return mbAuxReloadEntries; }
    public double getAuxOriginalAmount() { return mnAuxOriginalAmount; }
    
    public String getFolio() {
        return msSeries + (msSeries.isEmpty() ? "" : "-") + mnNumber;
    }
    
    public SDbPaymentEntry getSingleEntry() throws Exception {
        SDbPaymentEntry singleEntry = null;
        
        if (maChildEntries.isEmpty()) {
            throw new Exception("El pago no tiene partidas.");
        }
        else if (maChildEntries.size() != 1) {
            throw new Exception("El pago tiene " + maChildEntries.size() + " partidas.");
        }
        else {
            singleEntry = maChildEntries.get(0);
        }
        
        return singleEntry;
    }
    
    public boolean isExportable() {
        return SLibUtils.belongsTo(mnFkStatusPaymentId, new int[] { SModSysConsts.FINS_ST_PAY_NEW, SModSysConsts.FINS_ST_PAY_SCHED });
    }
    
    public void deleteFiles(final SGuiSession session) throws Exception {
        for (SDbPaymentFile file : maFiles) {
            file.delete(session);
        }
    }
    
    public void updatePaymentStatus(final SGuiSession session, final int newStatus) throws Exception {
        msSql = "UPDATE " + getSqlTable() + " SET " +
                "fk_st_pay = " + newStatus + ", " + 
                "fk_usr_upd = " + session.getUser().getPkUserId() + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();
        session.getStatement().execute(msSql);
    }
    
    public void updateAuthorizationData(final SGuiSession session) throws Exception {
        msSql = "UPDATE " + getSqlTable() + " SET " + 
                "priority = " + mnPriority + ", " + 
                "nts_auth = '" + msNotesAuthorization + "' " + 
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
        msPaymentType = "";
        msSeries = "";
        mnNumber = 0;
        mtDateApplication = null;
        mtDateRequired = null;
        mtDateSchedule_n = null;
        mtDateExecution_n = null;
        mdPaymentApplicationCy = 0;
        mdPaymentExchangeRateApplication = 0;
        mdPaymentApplication = 0;
        mdPaymentCy = 0;
        mdPaymentExchangeRate = 0;
        mdPayment = 0;
        msPaymentWay = "";
        mnPriority = PRIORITY_NORMAL;
        msNotes = "";
        msNotesAuthorization = "";
        msNotesAuthorizationFlow = "";
        mbReceiptPaymentRequired = false;
        mbRescheduled = false;
        mbExecutedManually = false;
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
        mnFkUserRescheduleId = 0;
        mnFkUserExecutiondId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserSchedule = null;
        mtTsUserReschedule = null;
        mtTsUserExecution = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildEntries = new ArrayList<>();
        maFiles = new ArrayList<>();
        maFilesDeleted = new ArrayList<>();
        
        msDbmsStatus = "";
        msDbmsBeneficiary = "";
        moDbmsDataCurrency = null;
        
        mtOldDateSchedule_n = null;
        mtOldDateExecution_n = null;
        mnOldFkUserScheduleId = 0;
        mnOldFkUserRescheduleId = 0;
        mnOldFkUserExecutiondId = 0;
        mdOldPaymentCy = 0;
        mnOldFkCurrencyId = 0;
        
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
            msPaymentType = resultSet.getString("pay_tp");
            mnNumber = resultSet.getInt("num");
            mtDateApplication = resultSet.getDate("dt_app");
            mtDateRequired = resultSet.getDate("dt_req");
            mtDateSchedule_n = resultSet.getDate("dt_sched_n");
            mtDateExecution_n = resultSet.getDate("dt_exec_n");
            mdPaymentApplicationCy = resultSet.getDouble("pay_app_cur");
            mdPaymentExchangeRateApplication = resultSet.getDouble("pay_exc_rate_app");
            mdPaymentApplication = resultSet.getDouble("pay_app");
            mdPaymentCy = resultSet.getDouble("pay_cur");
            mdPaymentExchangeRate = resultSet.getDouble("pay_exc_rate");
            mdPayment = resultSet.getDouble("pay");
            msPaymentWay = resultSet.getString("pay_way");
            mnPriority = resultSet.getInt("priority");
            msNotes = resultSet.getString("nts");
            msNotesAuthorization = resultSet.getString("nts_auth");
            msNotesAuthorizationFlow = resultSet.getString("nts_auth_flow");
            mbReceiptPaymentRequired = resultSet.getBoolean("b_rcpt_pay_req");
            mbRescheduled = resultSet.getBoolean("b_resched");
            mbExecutedManually = resultSet.getBoolean("b_exec_man");
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
            mnFkUserRescheduleId = resultSet.getInt("fk_usr_resched");
            mnFkUserExecutiondId = resultSet.getInt("fk_usr_exec");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserSchedule = resultSet.getTimestamp("ts_usr_sched");
            mtTsUserReschedule = resultSet.getTimestamp("ts_usr_resched");
            mtTsUserExecution = resultSet.getTimestamp("ts_usr_exec");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mtOldDateSchedule_n = mtDateSchedule_n;
            mtOldDateExecution_n = mtDateExecution_n;
            mnOldFkUserScheduleId = mnFkUserScheduleId;
            mnOldFkUserRescheduleId = mnFkUserRescheduleId;
            mnOldFkUserExecutiondId = mnFkUserExecutiondId;
            mdOldPaymentCy = mdPaymentCy;
            mnOldFkCurrencyId = mnFkCurrencyId;
            
            // Read aswell document entries:
            
            msDbmsStatus = (String) session.readField(SModConsts.FINS_ST_PAY, new int[] { mnFkStatusPaymentId }, SDbRegistry.FIELD_NAME);
            msDbmsBeneficiary = (String) session.readField(SModConsts.BPSU_BP, new int[] { mnFkBeneficiaryId }, SDbRegistry.FIELD_NAME);
            
            statement = session.getStatement().getConnection().createStatement();
            
            moDbmsDataCurrency = new SDataCurrency();
            moDbmsDataCurrency.read(new int[] { mnFkCurrencyId }, statement);
            
            msSql = "SELECT id_ety " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY_ETY) + " " + 
                    "WHERE id_pay = " + mnPkPaymentId + " " + 
                    "ORDER BY id_ety ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                entry = new SDbPaymentEntry();
                entry.read(session, new int[] { mnPkPaymentId, resultSet.getInt(1) });
                entry.setPayCurrency(moDbmsDataCurrency);
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
            mnNumber = computeNextNumber(session, msSeries);
            
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            mnFkUserScheduleId = SUtilConsts.USR_NA_ID;
            mnFkUserRescheduleId = SUtilConsts.USR_NA_ID;
            mnFkUserExecutiondId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPaymentId + ", " + 
                    "'" + msPaymentType + "', " + 
                    "'" + msSeries + "', " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateApplication) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateRequired) + "', " + 
                    (mtDateSchedule_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSchedule_n) + "', ") + 
                    (mtDateExecution_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateExecution_n) + "', ") + 
                    mdPaymentApplicationCy + ", " + 
                    mdPaymentExchangeRateApplication + ", " + 
                    mdPaymentApplication + ", " + 
                    mdPaymentCy + ", " + 
                    mdPaymentExchangeRate + ", " + 
                    mdPayment + ", " + 
                    "'" + msPaymentWay + "', " + 
                    mnPriority + ", " + 
                    "'" + msNotes + "', " + 
                    "'" + msNotesAuthorization + "', " + 
                    "'" + msNotesAuthorizationFlow + "', " + 
                    (mbReceiptPaymentRequired ? 1 : 0) + ", " + 
                    (mbRescheduled ? 1 : 0) + ", " + 
                    (mbExecutedManually ? 1 : 0) + ", " + 
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
                    mnFkUserRescheduleId + ", " + 
                    mnFkUserExecutiondId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            mnFkStatusPaymentId = mnFkStatusPaymentId == SModSysConsts.FINS_ST_PAY_REJC ? SModSysConsts.FINS_ST_PAY_NEW : mnFkStatusPaymentId;
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_pay = " + mnPkPaymentId + ", " +
                    "pay_tp = '" + msPaymentType + "', " +
                    "ser = '" + msSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "dt_app = '" + SLibUtils.DbmsDateFormatDate.format(mtDateApplication) + "', " +
                    "dt_req = '" + SLibUtils.DbmsDateFormatDate.format(mtDateRequired) + "', " +
                    "dt_sched_n = " + (mtDateSchedule_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateSchedule_n) + "', ") +
                    "dt_exec_n = " + (mtDateExecution_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateExecution_n) + "', ") +
                    "pay_app_cur = " + mdPaymentApplicationCy + ", " +
                    "pay_exc_rate_app = " + mdPaymentExchangeRateApplication + ", " +
                    "pay_app = " + mdPaymentApplication + ", " +
                    "pay_cur = " + mdPaymentCy + ", " +
                    "pay_exc_rate = " + mdPaymentExchangeRate + ", " +
                    "pay = " + mdPayment + ", " +
                    "pay_way = '" + msPaymentWay + "', " +
                    "priority = " + mnPriority + ", " +
                    "nts = '" + msNotes + "', " +
                    "nts_auth = '" + msNotesAuthorization + "', " +
                    "nts_auth_flow = '" + msNotesAuthorizationFlow + "', " +
                    "b_rcpt_pay_req = " + (mbReceiptPaymentRequired ? 1 : 0) + ", " +
                    "b_resched = " + (mbRescheduled ? 1 : 0) + ", " +
                    "b_exec_man = " + (mbExecutedManually ? 1 : 0) + ", " +
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
                    "fk_usr_resched = " + mnFkUserRescheduleId + ", " +
                    "fk_usr_exec = " + mnFkUserExecutiondId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    (hasChangedSchedule() ? "ts_usr_sched = NOW(), " : "") +
                    (hasChangedReschedule() ? "ts_usr_resched = NOW(), " : "") +
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
            
            if (((Object[]) value).length > 2) {
                database = (String) ((Object[]) value)[2];
            }
            
            switch (newStatusPaymentId) {
                case SModSysConsts.FINS_ST_PAY_IN_AUTH:
                case SModSysConsts.FINS_ST_PAY_REJC:
                case SModSysConsts.FINS_ST_PAY_SCHED:
                case SModSysConsts.FINS_ST_PAY_EXEC:
                case SModSysConsts.FINS_ST_PAY_SUBR:
                case SModSysConsts.FINS_ST_PAY_RCPT:
                case SModSysConsts.FINS_ST_PAY_BLOC:
                case SModSysConsts.FINS_ST_PAY_CANC:
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
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        boolean can = true;
        initQueryMembers();
        
        if (mbSystem) {
            can = false;
            msQueryResult = "Este pago es de sistema.";
        }
        else if (!mbDeletable) {
            can = false;
            msQueryResult = "Este pago no es eliminable.";
        }
        else if (mbDeleted) {
            can = false;
            msQueryResult = "Este pago ya está eliminado.";
        }
        else if (mnFkStatusPaymentId != SModSysConsts.FINS_ST_PAY_NEW && mnFkStatusPaymentId != SModSysConsts.FINS_ST_PAY_CANC) {
            can = false;
            msQueryResult = "Este pago debe tener estatus 'nuevo' o 'cancelado' para poder ser eliminado.";
        }
        
        return can;
    }
    
    @Override
    public SDbPayment clone() throws CloneNotSupportedException {
        SDbPayment registry = new SDbPayment();
        
        registry.setPkPaymentId(this.getPkPaymentId());
        registry.setPaymentType(this.getPaymentType());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setDateApplication(this.getDateApplication());
        registry.setDateRequired(this.getDateRequired());
        registry.setDateSchedule_n(this.getDateSchedule_n());
        registry.setDateExecution_n(this.getDateExecution_n());
        registry.setPaymentApplicationCy(this.getPaymentApplicationCy());
        registry.setPaymentExchangeRateApplication(this.getPaymentExchangeRateApplication());
        registry.setPaymentApplication(this.getPaymentApplication());
        registry.setPaymentCy(this.getPaymentCy());
        registry.setPaymentExchangeRate(this.getPaymentExchangeRate());
        registry.setPayment(this.getPayment());
        registry.setPaymentWay(this.getPaymentWay());
        registry.setPriority(this.getPriority());
        registry.setNotes(this.getNotes());
        registry.setNotesAuthorization(this.getNotesAuthorization());
        registry.setNotesAuthorizationFlow(this.getNotesAuthorizationFlow());
        registry.setReceiptPaymentRequired(this.isReceiptPaymentRequired());
        registry.setRescheduled(this.isRescheduled());
        registry.setExecutedManually(this.isExecutedManually());
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
        registry.setFkUserRescheduleId(this.getFkUserRescheduleId());
        registry.setFkUserExecutiondId(this.getFkUserExecutiondId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserSchedule(this.getTsUserSchedule());
        registry.setTsUserReschedule(this.getTsUserReschedule());
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
        registry.setDbmsBeneficiary(this.getDbmsBeneficiary());
        registry.setDbmsDataCurrency(this.getDbmsDataCurrency());
        
        registry.setOldDateSchedule_n(this.getOldDateSchedule_n());
        registry.setOldDateExecution_n(this.getOldDateExecution_n());
        registry.setOldFkUserScheduleId(this.getOldFkUserScheduleId());
        registry.setOldFkUserRescheduleId(this.getOldFkUserRescheduleId());
        registry.setOldFkUserExecutiondId(this.getOldFkUserExecutiondId());
        registry.setOldPaymentCy(this.getOldPaymentCy());
        registry.setOldFkCurrencyId(this.getOldFkCurrencyId());
        
        registry.setAuxReloadEntries(this.getAuxReloadEntries());
        registry.setAuxOriginalAmount(this.getAuxOriginalAmount());
        
        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
    
    /**
     * Process payment amount at application.
     * XXX 2025-12-08, Sergio Flores: TBD
     * Extend this algorithm to SFormPayment.getRegistry()
     * @param session GUI session.
     * @param requiredPaymentAmount Required payment amount.
     * @param requiredCurrencyId Required currency ID.
     * @param exchangeRate Exchange rate.
     * @param isRequiredPaymentLoc Is required payment in local currency?
     * @param docInstallment Document's number of installment. Can be zero when payment is not for a document.
     * @param docBalance Document's balance.
     * @throws Exception 
     */
    public void processPaymentAtApplication(final SGuiSession session, final double requiredPaymentAmount, final int requiredCurrencyId, final double exchangeRate, final boolean isRequiredPaymentLoc, final int docInstallment, final double docBalance) throws Exception {
        double paymentAtApplication;
        double exchangeRateAtApplication;
        int paymentCurrencyId;
        double conversionRateAtApplication;
        
        /*
        Payment cases:

        1. currency == local + is_payment_local == true | false
            payment data:
                currency: local
                computation: required payment
                exchange rate: 1

        2. currency <> local + is_payment_local == true
            payment data:
                currency: local
                computation: required payment * applicable exchange rate
                exchange rate: 1

        3. currency <> local + is_payment_local == false
            payment data:
                currency: local
                computation: required payment
                exchange rate: applicable exchange rate
        */

        // process payment:
        
        boolean isLocalRequiredCurrency = session.getSessionCustom().isLocalCurrency(new int[] { requiredCurrencyId }); // convenience variable

        if (isLocalRequiredCurrency) {
            // payment case 1:
            paymentAtApplication = requiredPaymentAmount;
            exchangeRateAtApplication = 1;
            paymentCurrencyId = requiredCurrencyId;

            conversionRateAtApplication = 1; // currencies conversion: from local to local
        }
        else if (isRequiredPaymentLoc) {
            // payment case 2:
            paymentAtApplication = SLibUtils.roundAmount(requiredPaymentAmount * exchangeRate);
            exchangeRateAtApplication = 1;
            paymentCurrencyId = session.getSessionCustom().getLocalCurrencyKey()[0];

            conversionRateAtApplication = 1 / exchangeRate; // currencies conversion: from local to foreign
        }
        else {
            // payment case 3:
            paymentAtApplication = requiredPaymentAmount;
            exchangeRateAtApplication = exchangeRate;
            paymentCurrencyId = requiredCurrencyId;

            conversionRateAtApplication = 1; // currencies conversion: from foreign to the same foreign
        }

        mdPaymentApplicationCy = paymentAtApplication;
        mdPaymentExchangeRateApplication = exchangeRateAtApplication;
        mdPaymentApplication = SLibUtils.roundAmount(paymentAtApplication * exchangeRateAtApplication);

        mdPaymentCy = mdPaymentApplicationCy; // same value "at application"!
        mdPaymentExchangeRate = mdPaymentExchangeRateApplication; // same value "at application"!
        mdPayment = mdPaymentApplication; // same value "at application"!

        mnFkCurrencyId = paymentCurrencyId;
        
        // process the only single one entry of requests of payment:
        
        SDbPaymentEntry paymentEntry = maChildEntries.get(0);
        
        paymentEntry.setEntryPaymentApplicationCy(mdPaymentApplicationCy);
        paymentEntry.setEntryPaymentApplication(mdPaymentApplication);
        paymentEntry.setConversionRateApplication(conversionRateAtApplication);
        paymentEntry.setDestinyPaymentApplicationEntryCy(requiredPaymentAmount);

        paymentEntry.setEntryPaymentCy(paymentEntry.getEntryPaymentApplicationCy()); // same value "at application"!
        paymentEntry.setEntryPayment(paymentEntry.getEntryPaymentApplication()); // same value "at application"!
        paymentEntry.setConversionRate(paymentEntry.getConversionRateApplication()); // same value "at application"!
        paymentEntry.setDestinyPaymentEntryCy(paymentEntry.getDestinyPaymentApplicationEntryCy()); // same value "at application"!

        if (docInstallment > 0) {
            paymentEntry.setDocInstallment(docInstallment);
            paymentEntry.setDocBalancePreviousApplicationCy(docBalance);
            paymentEntry.setDocBalanceUnpaidApplicationCy_r(SLibUtils.roundAmount(paymentEntry.getDocBalancePreviousApplicationCy() - paymentEntry.getDestinyPaymentApplicationEntryCy()));
        }
        else {
            paymentEntry.setDocInstallment(0);
            paymentEntry.setDocBalancePreviousApplicationCy(0);
            paymentEntry.setDocBalanceUnpaidApplicationCy_r(0);
        }
        
        paymentEntry.setDocBalancePreviousCy(paymentEntry.getDocBalancePreviousApplicationCy()); // same value "at application"!
        paymentEntry.setDocBalanceUnpaidCy_r(paymentEntry.getDocBalanceUnpaidApplicationCy_r()); // same value "at application"!

        paymentEntry.setFkEntryCurrencyId(requiredCurrencyId);
    }
    
    /**
     * Process payment amount at execution. Currency of payment remains the same.
     * XXX 2025-12-08, Sergio Flores: TBD
     * Extend this algorithm to SFormPayment.getRegistry()
     * @param session GUI session.
     * @param executedPaymentAmount Required payment amount.
     * @param exchangeRate Exchange rate.
     * @param docInstallment Document's number of installment. Can be zero when payment is not for a document.
     * @param docBalance Document's balance.
     * @throws Exception 
     */
    public void processPaymentAtExecution(final SGuiSession session, final double executedPaymentAmount, final double exchangeRate, final int docInstallment, final double docBalance) throws Exception {
        double paymentAtExecution;
        double exchangeRateAtExecution;
        double conversionRateAtExecution;
        SDbPaymentEntry paymentEntry = maChildEntries.get(0);
        
        /*
        Payment cases:

        1. currency == local + is_payment_local == true | false
            payment data:
                currency: local
                computation: required payment
                exchange rate: 1

        2. currency <> local + is_payment_local == true
            payment data:
                currency: local
                computation: required payment * applicable exchange rate
                exchange rate: 1

        3. currency <> local + is_payment_local == false
            payment data:
                currency: local
                computation: required payment
                exchange rate: applicable exchange rate
        */

        // process payment:
        
        boolean isLocalPaymentCurrency = session.getSessionCustom().isLocalCurrency(new int[] { mnFkCurrencyId }); // convenience variable
        
        if (isLocalPaymentCurrency && mnFkCurrencyId == paymentEntry.getFkEntryCurrencyId()) {
            // payment case 1:
            paymentAtExecution = executedPaymentAmount;
            exchangeRateAtExecution = 1;

            conversionRateAtExecution = 1; // currencies conversion: from local to local
        }
        else if (isLocalPaymentCurrency && mnFkCurrencyId != paymentEntry.getFkEntryCurrencyId()) {
            // payment case 2:
            paymentAtExecution = SLibUtils.roundAmount(executedPaymentAmount * exchangeRate);
            exchangeRateAtExecution = 1;

            conversionRateAtExecution = 1 / exchangeRate; // currencies conversion: from local to foreign
        }
        else {
            // payment case 3:
            paymentAtExecution = executedPaymentAmount;
            exchangeRateAtExecution = exchangeRate;

            conversionRateAtExecution = 1; // currencies conversion: from foreign to the same foreign
        }

        mdPaymentCy = paymentAtExecution;
        mdPaymentExchangeRate = exchangeRateAtExecution;
        mdPayment = SLibUtils.roundAmount(paymentAtExecution * exchangeRateAtExecution);

        // process the only single one entry of requests of payment:
        
        paymentEntry.setEntryPaymentCy(mdPaymentCy);
        paymentEntry.setEntryPayment(mdPayment);
        paymentEntry.setConversionRate(conversionRateAtExecution);
        paymentEntry.setDestinyPaymentEntryCy(executedPaymentAmount);

        if (docInstallment > 0) {
            paymentEntry.setDocInstallment(docInstallment);
            paymentEntry.setDocBalancePreviousCy(docBalance);
            paymentEntry.setDocBalanceUnpaidCy_r(paymentEntry.getDocBalancePreviousCy() - paymentEntry.getDestinyPaymentEntryCy());
        }
        else {
            paymentEntry.setDocBalancePreviousCy(0);
            paymentEntry.setDocBalanceUnpaidCy_r(0);
        }
    }
    
    /**
     * Change currency of payment. Payment amount remains the same.
     * If needed, exchange rate corresponds to payment application date.
     * @param session GUI session.
     * @param newPaymentCurrencyId ID of new currency.
     * @throws Exception 
     */
    public void changePaymentCurrency(final SGuiSession session, final int newPaymentCurrencyId) throws Exception {
        String prefix = "Se puede cambiar la moneda de pago, solo si ";
        
        if (mnFkStatusPaymentId != SModSysConsts.FINS_ST_PAY_REJC && mnFkStatusPaymentId != SModSysConsts.FINS_ST_PAY_SCHED) {
            throw new Exception(prefix + "el estatus de la solicitud de pago es '" + ST_REJC + "' o '" + ST_SCHED + "' .");
        }
        else {
            int[] newPaymentCurrencyKey = new int[] { newPaymentCurrencyId };
            
            int paymentCurrencyId = mnFkCurrencyId; // variable declared to improve understanding of alghrithm
            int[] paymentCurrencyKey = new int[] { paymentCurrencyId };
            
            prefix += "la nueva moneda, " + session.getSessionCustom().getCurrencyCode(newPaymentCurrencyKey) + ", ";
            
            if (newPaymentCurrencyId == paymentCurrencyId)  {
                // the new payment currency cannot be the current one:
                throw new Exception(prefix + "es distintia a la actual moneda de pago, " + session.getSessionCustom().getCurrencyCode(paymentCurrencyKey) + ".");
            }
            else {
                SDbPaymentEntry paymentEntry = maChildEntries.get(0);
                
                int entryCurrencyId = paymentEntry.getFkEntryCurrencyId(); // variable declared to improve understanding of alghrithm
                int[] entryCurrencyKey = new int[] { entryCurrencyId };
                
                boolean isLocalNewPaymentCurrency = session.getSessionCustom().isLocalCurrency(newPaymentCurrencyKey);
                boolean isLocalEntryCurrency = session.getSessionCustom().isLocalCurrency(entryCurrencyKey);
                
                if (!(isLocalNewPaymentCurrency || newPaymentCurrencyId == entryCurrencyId)) {
                    // the new payment currency only can be either the local one or the entry's currency:
                    throw new Exception(prefix + "es igual a la actual moneda a pagar, " + session.getSessionCustom().getCurrencyCode(entryCurrencyKey) + (isLocalEntryCurrency ? "" : ", o a la moneda local, " + session.getSessionCustom().getLocalCurrencyCode()) + ".");
                }
                else {
                    double exchangeRate;
                    double entryConversionRate;
                    
                    if (isLocalNewPaymentCurrency) {
                        // change from foreign to local currency:
                        exchangeRate = SDocumentUtils.getExchangeRate(session, paymentCurrencyId, mtDateApplication);
                        
                        mdPaymentApplicationCy = SLibUtils.roundAmount(mdPaymentApplicationCy * exchangeRate); // start from the original required payment amount
                        mdPaymentExchangeRateApplication = 1;
                    }
                    else {
                        // change from local to foreign currency:
                        exchangeRate = SDocumentUtils.getExchangeRate(session, newPaymentCurrencyId, mtDateApplication);
                        
                        mdPaymentApplicationCy = SLibUtils.roundAmount(mdPaymentApplicationCy / exchangeRate); // start from the original required payment amount
                        mdPaymentExchangeRateApplication = exchangeRate;
                    }
                    
                    entryConversionRate = newPaymentCurrencyId == entryCurrencyId ? 1 : (1 / exchangeRate);
                    
                    mdPaymentApplication = SLibUtils.roundAmount(mdPaymentApplicationCy * mdPaymentExchangeRateApplication);
                    mdPaymentExchangeRate = mdPaymentExchangeRateApplication; // same value "at application"!
                    mdPayment = mdPaymentApplication; // same value "at application"!
                    mnFkCurrencyId = newPaymentCurrencyId;
                    
                    paymentEntry.setEntryPaymentApplicationCy(mdPaymentApplicationCy);

                    paymentEntry.setEntryPaymentApplication(mdPaymentApplication);
                    paymentEntry.setConversionRateApplication(entryConversionRate);

                    paymentEntry.setEntryPayment(paymentEntry.getEntryPaymentApplication()); // same value "at application"!
                    paymentEntry.setConversionRate(paymentEntry.getConversionRateApplication()); // same value "at application"!
                }
            }
        }
    }
    
    /**
     * Get settled status for the given one.
     * @param processStatusPaymentId Status of payment.
     * @return 
     */
    public static int getSettledStatusPaymentId(final int processStatusPaymentId) {
        int settledStatusPayment = 0;
        
        switch (processStatusPaymentId) {
            case SModSysConsts.FINS_ST_PAY_NEW:
                settledStatusPayment = SModSysConsts.FINS_ST_PAY_IN_AUTH;
                break;
            case SModSysConsts.FINS_ST_PAY_REJC_P:
                settledStatusPayment = SModSysConsts.FINS_ST_PAY_REJC;
                break;
            case SModSysConsts.FINS_ST_PAY_SCHED_P:
                settledStatusPayment = SModSysConsts.FINS_ST_PAY_SCHED;
                break;
            case SModSysConsts.FINS_ST_PAY_EXEC_P:
                settledStatusPayment = SModSysConsts.FINS_ST_PAY_EXEC;
                break;
            case SModSysConsts.FINS_ST_PAY_SUBR_P:
                settledStatusPayment = SModSysConsts.FINS_ST_PAY_SUBR;
                break;
            case SModSysConsts.FINS_ST_PAY_RCPT_P:
                settledStatusPayment = SModSysConsts.FINS_ST_PAY_RCPT;
                break;
            case SModSysConsts.FINS_ST_PAY_BLOC_P:
                settledStatusPayment = SModSysConsts.FINS_ST_PAY_BLOC;
                break;
            case SModSysConsts.FINS_ST_PAY_CANC_P:
                settledStatusPayment = SModSysConsts.FINS_ST_PAY_CANC;
                break;
            default:
                // nothing
        }
        
        return settledStatusPayment;
    }
    
    /**
     * Check if payment of given status should be exported as deleted.
     * @param statusPaymentInProcessId Status of payment.
     * @return 
     */
    public static boolean exportStatusPaymentAsDeleted(final int statusPaymentInProcessId) {
        boolean exportAsDeleted = false;
        
        switch (statusPaymentInProcessId) {
            case SModSysConsts.FINS_ST_PAY_SUBR_P:
            case SModSysConsts.FINS_ST_PAY_CANC_P:
                exportAsDeleted = true;
                break;
            default:
                // nothing
        }
        
        return exportAsDeleted;
    }
    
    /**
     * Get next number.
     * @param session GUI session.
     * @param series Required folio series.
     * @return Next number.
     * @throws Exception 
     */
    public static int computeNextNumber(final SGuiSession session, final String series) throws Exception {
        int nextNumber = 0;
        
        String sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_PAY) + " "
                + "WHERE ser = '" + series + "';";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        
        return nextNumber;
    }
}
