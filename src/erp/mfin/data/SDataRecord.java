/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.SErpConsts;
import erp.data.SDataConstants;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mtrn.data.SDataCfd;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mfin.data.SDataFiscalYearClosing
 * - erp.mfin.data.SDataFiscalYearOpening
 * - erp.util.imp.ImportAccountingRecords
 * - erp.util.imp.ImportAccountingRecordsMicroSip
 * - erp.mod.hrs.db.SHrsFinUtils
 * Also update any change to class members in methods encodeJson() and decodeJson()!
 * All of them also make raw SQL queries and insertions.
 */

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SDataRecord extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    public static final int FIELD_AUDIT = 1;
    public static final int FIELD_USER_EDIT_ID = 2;
    
    protected int mnPkYearId;
    protected int mnPkPeriodId;
    protected int mnPkBookkeepingCenterId;
    protected java.lang.String msPkRecordTypeId;
    protected int mnPkNumberId;
    protected java.util.Date mtDate;
    protected java.lang.String msConcept;
    protected boolean mbIsAdjustmentsYearEnd;
    protected boolean mbIsAdjustmentsAudit;
    protected boolean mbIsAudited;
    protected boolean mbIsAuthorized;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected int mnFkCompanyBranchId;
    protected int mnFkCompanyBranchId_n;
    protected int mnFkAccountCashId_n;
    protected int mnFkUserAuditedId;
    protected int mnFkUserAuthorizedId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserAuditedTs;
    protected java.util.Date mtUserAuthorizedTs;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected java.lang.String msDbmsBookkeepingCenterCode;
    protected java.lang.String msDbmsCompanyBranchCode;
    protected erp.mfin.data.SDataAccountCash moDbmsDataAccountCash;
    protected int mnDbmsXmlFilesNumber;
    protected HashSet<erp.mtrn.data.SDataCfd> maDbmsDataCfd;
    protected java.util.Vector<erp.mfin.data.SDataRecordEntry> mvDbmsRecordEntries;
    
    protected HashSet<erp.mtrn.data.SDataCfd> maAuxDataCfdToDel;
    protected boolean mbAuxReadHeaderOnly; // it reduces dramatically reading time when entries and extra stuff are useless

    protected boolean mbTempDataJustRecovered;
    protected java.util.HashMap<java.lang.Integer, java.util.HashMap> moTempMapOfMaps; // for JSON decodign of record entries
    
    public SDataRecord() {
        super(SDataConstants.FIN_REC);
        mlRegistryTimeout = 1000 * 60 * 60 * 2; // 2 hr
        mvDbmsRecordEntries = new Vector<>();
        reset();
    }

    /*
     * Private methods
     */
    
    private void sanitizeData() {
        if (msConcept.length() > SDataRecordEntry.LEN_CONCEPT) {
            msConcept = msConcept.substring(0, SDataRecordEntry.LEN_CONCEPT - SErpConsts.ELLIPSIS.length()).trim() + SErpConsts.ELLIPSIS;
        }
    }

    private void updateChecksLinks(java.sql.Connection connection) throws java.sql.SQLException, java.lang.Exception{
        String sql = "";
        Statement statement = connection.createStatement();
        SDataCheck check = null;

        for (SDataRecordEntry entryCheck : mvDbmsRecordEntries) {
            // Lookup for entries with checks:

            if (!entryCheck.getIsDeleted() && entryCheck.getDbmsCheck() != null) {
                check = entryCheck.getDbmsCheck();

                for (SDataRecordEntry entry : mvDbmsRecordEntries) {
                    // Lookup for entries that have a reference to current check:

                    if (!SLibUtilities.compareKeys(entryCheck.getPrimaryKey(), entry.getPrimaryKey())) {
                        if (check.getNumber() != 0 && check.getNumber() == entry.getAuxCheckNumber()) {
                            sql = "UPDATE fin_rec_ety SET fid_check_wal_n = " + check.getPkCheckWalletId() + ", fid_check_n = " + check.getPkCheckId() + " " +
                                    "WHERE id_year = " + entry.getPkYearId() + " AND id_per = " + entry.getPkPeriodId() + " AND id_bkc = " + entry.getPkBookkeepingCenterId() + " AND " +
                                    "id_tp_rec = '" + entry.getPkRecordTypeId() + "' AND id_num = " + entry.getPkNumberId() + " AND id_ety = " + entry.getPkEntryId() + " ";
                            statement.execute(sql);
                        }
                    }
                }
            }
        }
    }

    private void readAccountCash(java.sql.Statement statement) throws Exception {
        moDbmsDataAccountCash = null;
        
        if (mnFkCompanyBranchId_n != 0 && mnFkAccountCashId_n != 0) {
            moDbmsDataAccountCash = new SDataAccountCash();
            if (moDbmsDataAccountCash.read(new int[] { mnFkCompanyBranchId_n, mnFkAccountCashId_n }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
        }
    }

    /*
     * Public methods
     */
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPeriodId(int n) { mnPkPeriodId = n; }
    public void setPkBookkeepingCenterId(int n) { mnPkBookkeepingCenterId = n; }
    public void setPkRecordTypeId(java.lang.String s) { msPkRecordTypeId = s; }
    public void setPkNumberId(int n) { mnPkNumberId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setConcept(java.lang.String s) { msConcept = s; }
    public void setIsAdjustmentsYearEnd(boolean b) { mbIsAdjustmentsYearEnd = b; }
    public void setIsAdjustmentsAudit(boolean b) { mbIsAdjustmentsAudit = b; }
    public void setIsAudited(boolean b) { mbIsAudited = b; }
    public void setIsAuthorized(boolean b) { mbIsAuthorized = b; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkCompanyBranchId_n(int n) { mnFkCompanyBranchId_n = n; }
    public void setFkAccountCashId_n(int n) { mnFkAccountCashId_n = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserAuthorizedId(int n) { mnFkUserAuthorizedId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserAuditedTs(java.util.Date t) { mtUserAuditedTs = t; }
    public void setUserAuthorizedTs(java.util.Date t) { mtUserAuthorizedTs = t; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkPeriodId() { return mnPkPeriodId; }
    public int getPkBookkeepingCenterId() { return mnPkBookkeepingCenterId; }
    public java.lang.String getPkRecordTypeId() { return msPkRecordTypeId; }
    public int getPkNumberId() { return mnPkNumberId; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getConcept() { return msConcept; }
    public boolean getIsAdjustmentsYearEnd() { return mbIsAdjustmentsYearEnd; }
    public boolean getIsAdjustmentsAudit() { return mbIsAdjustmentsAudit; }
    public boolean getIsAudited() { return mbIsAudited; }
    public boolean getIsAuthorized() { return mbIsAuthorized; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkCompanyBranchId_n() { return mnFkCompanyBranchId_n; }
    public int getFkAccountCashId_n() { return mnFkAccountCashId_n; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserAuthorizedId() { return mnFkUserAuthorizedId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserAuditedTs() { return mtUserAuditedTs; }
    public java.util.Date getUserAuthorizedTs() { return mtUserAuthorizedTs; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsBookkeepingCenterCode(java.lang.String s) { msDbmsBookkeepingCenterCode = s; }
    public void setDbmsCompanyBranchCode(java.lang.String s) { msDbmsCompanyBranchCode = s; }
    public void setDbmsDataAccountCash(erp.mfin.data.SDataAccountCash o) { moDbmsDataAccountCash = o; }
    public void setDbmsXmlFilesNumber(int i) { mnDbmsXmlFilesNumber = i; }
    public void setDbmsDataCfd(HashSet<erp.mtrn.data.SDataCfd> a) { maDbmsDataCfd = a; }
    
    public java.lang.String getDbmsBookkeepingCenterCode() { return msDbmsBookkeepingCenterCode; }
    public java.lang.String getDbmsCompanyBranchCode() { return msDbmsCompanyBranchCode; }
    public erp.mfin.data.SDataAccountCash getDbmsDataAccountCash() { return moDbmsDataAccountCash; }
    public int getDbmsXmlFilesNumber() { return mnDbmsXmlFilesNumber; }
    public HashSet<erp.mtrn.data.SDataCfd> getDbmsDataCfds() { return maDbmsDataCfd; }
    public java.util.Vector<SDataRecordEntry> getDbmsRecordEntries() { return mvDbmsRecordEntries; }
    
    public void setAuxDataCfdToDel(HashSet<erp.mtrn.data.SDataCfd> a ) { maAuxDataCfdToDel = a; }
    public boolean getAuxReadHeaderOnly() { return mbAuxReadHeaderOnly; }
    
    public HashSet<erp.mtrn.data.SDataCfd> getAuxDataCfdToDel() { return maAuxDataCfdToDel; }
    public void setAuxReadHeaderOnly(boolean b) { mbAuxReadHeaderOnly = b; }
    
    public void setTempDataJustRecovered(boolean b) { mbTempDataJustRecovered = b; }
    
    public boolean getTempDataJustRecovered() { return mbTempDataJustRecovered; }
    /**
     * Get temporal map of maps for JSON decodign of record entries.
     * Prevent from reading the description of the same depending registry more than once.
     * @return 
     */
    public java.util.HashMap<java.lang.Integer, java.util.HashMap> getTempMapOfMaps() { if (moTempMapOfMaps == null) moTempMapOfMaps = new HashMap<>(); return moTempMapOfMaps; }
    
    /**
     * Get temporal map for JSON decodign of record entries.
     * Prevent from reading the description of the same depending registry more than once.
     * @param dbms Required map.
     * @return 
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getTempMap(final int dbms) {
        HashMap<String, Object> tempMap = getTempMapOfMaps().get(dbms);
        
        if (tempMap == null) {
            tempMap = new HashMap<>();
        }
        
        return tempMap;
    }
    
    /**
     * Composes record period in format yyyy-mm (i.e., year-month).
     * @return 
     */
    public java.lang.String getRecordPeriod() {
        return SLibUtils.DecimalFormatCalendarYear.format(mnPkYearId) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(mnPkPeriodId);
    }

    /**
     * Composes record number in format tp-000000 (i.e., type-number).
     * @return 
     */
    public java.lang.String getRecordNumber() {
        return msPkRecordTypeId + "-" + SLibUtils.DecimalNumberFormat.format(mnPkNumberId);
    }

    /**
     * Composes record primary key in format yyyy-mm-0-tp-000000 (i.e., year-month-BKC-type-number).
     * @return 
     */
    public String getRecordPrimaryKey() {
        return getRecordPeriod() + "-" + mnPkBookkeepingCenterId + "-" + getRecordNumber();
    }

    /**
     * Composes record primary key in format yyyy-mm-0-tp-000000 (i.e., year-month-BKC-type-number).
     * @param yearId
     * @param periodId
     * @param bookkeepingCenterId
     * @param recordTypeId
     * @param numberId
     * @return 
     */
    public static String getRecordPrimaryKey(final int yearId, final int periodId, final int bookkeepingCenterId, final String recordTypeId, final int numberId) {
        return SLibUtils.DecimalFormatCalendarYear.format(yearId) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(periodId) + "-" + bookkeepingCenterId + "-" + recordTypeId + "-" + SLibUtils.DecimalNumberFormat.format(numberId);
    }

    public int getLastSortingPosition() {
        int lastSortingPosition = 0;
                
        for (SDataRecordEntry entry : mvDbmsRecordEntries) {
            if (!entry.getIsDeleted() && entry.getSortingPosition() > lastSortingPosition) {
                lastSortingPosition = entry.getSortingPosition();
            }
        }
        
        return lastSortingPosition;
    }

    public static int getLastSortingPosition(Connection connection, Object primaryKey) throws Exception {
        int lastSortingPosition = 0;
        
        String sql = "SELECT MAX(sort_pos) "
                + "FROM fin_rec_ety "
                + "WHERE id_year = " + ((Object[]) primaryKey)[0] + " AND "
                + "id_per = " + ((Object[]) primaryKey)[1] + " AND "
                + "id_bkc = " + ((Object[]) primaryKey)[2] + " AND "
                + "id_tp_rec = '" + ((Object[]) primaryKey)[3] + "' AND "
                + "id_num = " + ((Object[]) primaryKey)[4] + " AND "
                + "NOT b_del;";
        
        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                lastSortingPosition = resultSet.getInt(1);
            }
        }
        
        return lastSortingPosition;
    }

    public void checkIsEditable(final Connection connection) throws Exception {
        if (mbIsSystem) {
            throw new Exception("¡La póliza contable '" + getRecordNumber() + "' es de sistema!");
        }
        
        if (mbIsDeleted) {
            throw new Exception("¡La póliza contable '" + getRecordNumber() + "' está eliminada!");
        }
        
        int index = 1;
        CallableStatement oCallableStatement = connection.prepareCall("{ CALL fin_year_per_st(?, ?, ?) }");
        oCallableStatement.setInt(index++, mnPkYearId);
        oCallableStatement.setInt(index++, mnPkPeriodId);
        oCallableStatement.registerOutParameter(index++, java.sql.Types.INTEGER);
        oCallableStatement.execute();

        if (oCallableStatement.getBoolean(index - 1)) {
            throw new Exception("¡El período contable '" + getRecordPeriod() + "' de la póliza contable '" + getRecordNumber() + "' está cerrado!");
        }
    }
    
    /*
     * Public overriden methods
     */
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = (Integer) ((Object[]) pk)[0];
        mnPkPeriodId = (Integer) ((Object[]) pk)[1];
        mnPkBookkeepingCenterId = (Integer) ((Object[]) pk)[2];
        msPkRecordTypeId = (String) ((Object[]) pk)[3];
        mnPkNumberId = (Integer) ((Object[]) pk)[4];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new Object[] { mnPkYearId, mnPkPeriodId, mnPkBookkeepingCenterId, msPkRecordTypeId, mnPkNumberId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkPeriodId = 0;
        mnPkBookkeepingCenterId = 0;
        msPkRecordTypeId = "";
        mnPkNumberId = 0;
        mtDate = null;
        msConcept = "";
        mbIsAdjustmentsYearEnd = false;
        mbIsAdjustmentsAudit = false;
        mbIsAudited = false;
        mbIsAuthorized = false;
        mbIsSystem = false;
        mbIsDeleted = false;
        mnFkCompanyBranchId = 0;
        mnFkCompanyBranchId_n = 0;
        mnFkAccountCashId_n = 0;
        mnFkUserAuditedId = 0;
        mnFkUserAuthorizedId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserAuditedTs = null;
        mtUserAuthorizedTs = null;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsBookkeepingCenterCode = "";
        msDbmsCompanyBranchCode = "";
        moDbmsDataAccountCash = null;
        mnDbmsXmlFilesNumber = 0;
        maDbmsDataCfd = new HashSet<>();
        mvDbmsRecordEntries.clear();
        
        maAuxDataCfdToDel = new HashSet<>();
        //mbAuxReadHeaderOnly = false; // prevent from reseting this flag
        
        mbTempDataJustRecovered = false;
        moTempMapOfMaps = null; // instantianted only when needed
    }
    
    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;
        java.sql.Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT r.*, bkc.code, cob.code " +
                    "FROM fin_rec AS r " +
                    "INNER JOIN fin_bkc AS bkc ON r.id_bkc = bkc.id_bkc " +
                    "INNER JOIN erp.bpsu_bpb AS cob ON r.fid_cob = cob.id_bpb " +
                    "WHERE r.id_year = " + key[0] + " AND r.id_per = " + key[1] + " AND " +
                    "r.id_bkc = " + key[2] + " AND r.id_tp_rec = '" + key[3] + "' AND r.id_num = " + key[4] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("r.id_year");
                mnPkPeriodId = resultSet.getInt("r.id_per");
                mnPkBookkeepingCenterId = resultSet.getInt("r.id_bkc");
                msPkRecordTypeId = resultSet.getString("r.id_tp_rec");
                mnPkNumberId = resultSet.getInt("r.id_num");
                mtDate = resultSet.getDate("r.dt");
                msConcept = resultSet.getString("r.concept");
                mbIsAdjustmentsYearEnd = resultSet.getBoolean("r.b_adj_year");
                mbIsAdjustmentsAudit = resultSet.getBoolean("r.b_adj_audit");
                mbIsAudited = resultSet.getBoolean("r.b_audit");
                mbIsAuthorized = resultSet.getBoolean("r.b_authorn");
                mbIsSystem = resultSet.getBoolean("r.b_sys");
                mbIsDeleted = resultSet.getBoolean("r.b_del");
                mnFkCompanyBranchId = resultSet.getInt("r.fid_cob");
                mnFkCompanyBranchId_n = resultSet.getInt("r.fid_cob_n");
                mnFkAccountCashId_n = resultSet.getInt("r.fid_acc_cash_n");
                mnFkUserAuditedId = resultSet.getInt("r.fid_usr_audit");
                mnFkUserAuthorizedId = resultSet.getInt("r.fid_usr_authorn");
                mnFkUserNewId = resultSet.getInt("r.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("r.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("r.fid_usr_del");
                mtUserAuditedTs = resultSet.getTimestamp("r.ts_audit");
                mtUserAuthorizedTs = resultSet.getTimestamp("r.ts_authorn");
                mtUserNewTs = resultSet.getTimestamp("r.ts_new");
                mtUserEditTs = resultSet.getTimestamp("r.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("r.ts_del");
                
                // Read aswell complementary data:

                msDbmsBookkeepingCenterCode = resultSet.getString("bkc.code");
                msDbmsCompanyBranchCode = resultSet.getString("cob.code");
                readAccountCash(statement);
                
                if (!mbAuxReadHeaderOnly) {
                    statementAux = statement.getConnection().createStatement();

                    // Read aswell record entries:

                    sql = "SELECT id_ety " +
                            "FROM fin_rec_ety " +
                            "WHERE id_year = " + (Integer) key[0] + " AND id_per = " + (Integer) key[1] + " AND " +
                            "id_bkc = " + (Integer) key[2] + " AND id_tp_rec = '" + (String) key[3] + "' AND id_num = " + (Integer) key[4] + " " +
                            "ORDER BY sort_pos, id_ety ";
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        SDataRecordEntry entry = new SDataRecordEntry();
                        entry.setParentRecord(this);
                        if (entry.read(new Object[] { mnPkYearId, mnPkPeriodId, mnPkBookkeepingCenterId, msPkRecordTypeId, mnPkNumberId, resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            mvDbmsRecordEntries.add(entry);
                        }
                    }

                    // CFD

                    sql = "SELECT id_cfd " +
                            "FROM trn_cfd " +
                            "WHERE fid_fin_rec_year_n = " + key[0] + " AND fid_fin_rec_per_n = " + key[1] + " AND " +
                            "fid_fin_rec_bkc_n = " + key[2] + " AND fid_fin_rec_tp_rec_n = '" + key[3] + "' AND fid_fin_rec_num_n = " + key[4] + ";";
                    readCfds(statement, sql);

                    mnDbmsXmlFilesNumber = maDbmsDataCfd.size();
                }
 
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    private void readCfds(Statement statement, String sql) throws Exception {
        try (Statement statementAux = statement.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDataCfd cfd = new SDataCfd();
                if (cfd.read(new int[] { resultSet.getInt("id_cfd") }, statementAux)!= SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                
                if (!cfd.getDocXmlName().isEmpty()) {
                    maDbmsDataCfd.add(cfd);
                }
            }
        }
    }

    /**
     * Saves accounting record.
     * Sorting position of all active entries must be already set.
     * @param connection
     */
    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbAuxReadHeaderOnly) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
            }
            
            sanitizeData();
            
            callableStatement = connection.prepareCall(
                    "{ CALL fin_rec_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkPeriodId);
            callableStatement.setInt(nParam++, mnPkBookkeepingCenterId);
            callableStatement.setString(nParam++, msPkRecordTypeId);
            callableStatement.setInt(nParam++, mnPkNumberId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setString(nParam++, msConcept);
            callableStatement.setBoolean(nParam++, mbIsAdjustmentsYearEnd);
            callableStatement.setBoolean(nParam++, mbIsAdjustmentsAudit);
            callableStatement.setBoolean(nParam++, mbIsAudited);
            callableStatement.setBoolean(nParam++, mbIsAuthorized);
            callableStatement.setBoolean(nParam++, mbIsSystem);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkCompanyBranchId);
            if (mnFkCompanyBranchId_n > 0) callableStatement.setInt(nParam++, mnFkCompanyBranchId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkAccountCashId_n > 0) callableStatement.setInt(nParam++, mnFkAccountCashId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkNumberId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save aswell record entries:

                for (SDataRecordEntry entry : mvDbmsRecordEntries) {
                    if (entry.getIsRegistryNew() || entry.getIsRegistryEdited()) {
                        entry.setParentRecord(this);
                        entry.setPkYearId(mnPkYearId);
                        entry.setPkPeriodId(mnPkPeriodId);
                        entry.setPkBookkeepingCenterId(mnPkBookkeepingCenterId);
                        entry.setPkRecordTypeId(msPkRecordTypeId);
                        entry.setPkNumberId(mnPkNumberId);
                        entry.setAuxDateCfd(mtDate);

                        if (entry.getIsDeleted()) {
                            entry.setSortingPosition(0);
                        }
                        else {
                            entry.setSortingPosition(entry.getSortingPosition());
                        }

                        if (entry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save XML associated:

                for (SDataCfd cfd : maDbmsDataCfd) {
                    cfd.setFkFinRecordYearId_n(mnPkYearId);
                    cfd.setFkFinRecordPeriodId_n(mnPkPeriodId);
                    cfd.setFkFinRecordBookkeepingCenterId_n(mnPkBookkeepingCenterId);
                    cfd.setFkFinRecordRecordTypeId_n(msPkRecordTypeId);
                    cfd.setFkFinRecordNumberId_n(mnPkNumberId);
                    cfd.setFkRecordYearId_n(0);
                    cfd.setFkRecordPeriodId_n(0);
                    cfd.setFkRecordBookkeepingCenterId_n(0);
                    cfd.setFkRecordRecordTypeId_n("");
                    cfd.setFkRecordNumberId_n(0);
                    cfd.setFkRecordEntryId_n(0);
                    cfd.setTimestamp(mtDate);

                    if (cfd.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Delete XML:

                for (SDataCfd cfd : maAuxDataCfdToDel) {
                    cfd.setFkFinRecordYearId_n(0);
                    cfd.setFkFinRecordPeriodId_n(0);
                    cfd.setFkFinRecordBookkeepingCenterId_n(0);
                    cfd.setFkFinRecordRecordTypeId_n("");
                    cfd.setFkFinRecordNumberId_n(0);

                    if (cfd.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                updateChecksLinks(connection);  // link check registries with corresponding record entries

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    public void saveField(java.sql.Connection connection, final int field, final Object value) throws Exception {
        String sql = "UPDATE fin_rec SET ";
        
        switch (field) {
            case FIELD_AUDIT:
                mbIsAudited = (boolean) value;
                sql += "b_audit = " + mbIsAudited + ", ts_audit = NOW(), fid_usr_audit = " + mnFkUserEditId + " ";
                break;
            case FIELD_USER_EDIT_ID:
                mnFkUserEditId = (int) value;
                sql += "fid_usr_edit = " + mnFkUserEditId + ", ts_edit = NOW() ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        try (Statement statement = connection.createStatement()) {
            sql += "WHERE id_year = " + mnPkYearId + " AND id_per = " + mnPkPeriodId + " AND " +
                    "id_bkc = " + mnPkBookkeepingCenterId + " AND id_tp_rec = '" + msPkRecordTypeId + "' AND id_num = " + mnPkNumberId + " ";
            
            statement.execute(sql);
        }
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    @Override
    public int canDelete(java.sql.Connection connection) {
        int can = SLibConstants.DB_CAN_DELETE_YES;
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            if (mbAuxReadHeaderOnly) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
            }
            
            if (mbIsSystem) {
                can = SLibConstants.DB_CAN_DELETE_NO;
                msDbmsError = SLibConstants.MSG_ERR_REG_SYSTEM;
            }
            else {
                statement = connection.createStatement();

                sql = "SELECT COUNT(*) AS f_count FROM fin_rec_ety " +
                        "WHERE id_year = " + mnPkYearId + " AND id_per = " + mnPkPeriodId + " AND " +
                        "id_bkc = " + mnPkBookkeepingCenterId + " AND id_tp_rec = '" + msPkRecordTypeId + "' AND id_num = " + mnPkNumberId + " ";
                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ);
                }
                else {

                }
            }
        }
        catch (Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_ANNUL_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return can;
    }

    @Override
    public int delete(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        return mnLastDbActionResult;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public String encodeJson(erp.client.SClientInterface client) throws ParseException, Exception {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonHeader = new JSONObject();
        
        mtTempFileTimestamp = new Date();
        jsonHeader.put("timestamp", SLibUtils.DbmsDateFormatDatetime.format(mtTempFileTimestamp));
        
        jsonHeader.put("id_year", mnPkYearId);
        jsonHeader.put("id_per", mnPkPeriodId);
        jsonHeader.put("id_bkc", mnPkBookkeepingCenterId);
        jsonHeader.put("id_tp_rec", msPkRecordTypeId);
        jsonHeader.put("id_num", mnPkNumberId);
        jsonHeader.put("dt", SLibUtils.DbmsDateFormatDate.format(mtDate != null ? mtDate : new Date()));
        jsonHeader.put("concept", msConcept);
        jsonHeader.put("b_adj_year", mbIsAdjustmentsYearEnd);
        jsonHeader.put("b_adj_audit", mbIsAdjustmentsAudit);
        jsonHeader.put("b_audit", mbIsAudited);
        jsonHeader.put("b_authorn", mbIsAuthorized);
        jsonHeader.put("b_sys", mbIsSystem);
        jsonHeader.put("b_del", mbIsDeleted);
        jsonHeader.put("fid_cob", mnFkCompanyBranchId);
        jsonHeader.put("fid_cob_n", mnFkCompanyBranchId_n);
        jsonHeader.put("fid_acc_cash_n", mnFkAccountCashId_n);
        jsonHeader.put("fid_usr_audit", mnFkUserAuditedId);
        jsonHeader.put("fid_usr_authorn", mnFkUserAuthorizedId);
        jsonHeader.put("fid_usr_new", mnFkUserNewId);
        jsonHeader.put("fid_usr_edit", mnFkUserEditId);
        jsonHeader.put("fid_usr_del", mnFkUserDeleteId);
        jsonHeader.put("ts_audit", SLibUtils.DbmsDateFormatDatetime.format(mtUserAuditedTs != null ? mtUserAuditedTs : new Date()));
        jsonHeader.put("ts_authorn", SLibUtils.DbmsDateFormatDatetime.format(mtUserAuthorizedTs != null ? mtUserAuthorizedTs : new Date()));
        jsonHeader.put("ts_new", SLibUtils.DbmsDateFormatDatetime.format(mtUserNewTs != null ? mtUserNewTs : new Date()));
        jsonHeader.put("ts_edit", SLibUtils.DbmsDateFormatDatetime.format(mtUserEditTs != null ? mtUserEditTs : new Date()));
        jsonHeader.put("ts_del", SLibUtils.DbmsDateFormatDatetime.format(mtUserDeleteTs != null ? mtUserDeleteTs : new Date()));
        
        JSONObject jsonEntries = new JSONObject();
        
        int n = 0;
        for (SDataRecordEntry entry : mvDbmsRecordEntries) {
            JSONObject jsonEntry = (JSONObject) jsonParser.parse(entry.encodeJson(client));
            
            jsonEntries.put("entry-" + ++n, jsonEntry);
        }
        
        jsonHeader.put("entries", jsonEntries);
        
        return jsonHeader.toJSONString();
    }
    
    @Override
    public void decodeJson(erp.client.SClientInterface client, java.lang.String json) throws ParseException, Exception {
        reset();
        
        // recover data:
        
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonHeader = (JSONObject) jsonParser.parse(json);
        
        mtTempFileTimestamp = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonHeader.get("timestamp"));
        
        mnPkYearId = new Long((long) jsonHeader.get("id_year")).intValue();
        mnPkPeriodId = new Long((long) jsonHeader.get("id_per")).intValue();
        mnPkBookkeepingCenterId = new Long((long) jsonHeader.get("id_bkc")).intValue();
        msPkRecordTypeId = (java.lang.String) jsonHeader.get("id_tp_rec");
        mnPkNumberId = new Long((long) jsonHeader.get("id_num")).intValue();
        mtDate = SLibUtils.DbmsDateFormatDate.parse((java.lang.String) jsonHeader.get("dt"));
        msConcept = (java.lang.String) jsonHeader.get("concept");
        mbIsAdjustmentsYearEnd = (boolean) jsonHeader.get("b_adj_year");
        mbIsAdjustmentsAudit = (boolean) jsonHeader.get("b_adj_audit");
        mbIsAudited = (boolean) jsonHeader.get("b_audit");
        mbIsAuthorized = (boolean) jsonHeader.get("b_authorn");
        mbIsSystem = (boolean) jsonHeader.get("b_sys");
        mbIsDeleted = (boolean) jsonHeader.get("b_del");
        mnFkCompanyBranchId = new Long((long) jsonHeader.get("fid_cob")).intValue();
        mnFkCompanyBranchId_n = new Long((long) jsonHeader.get("fid_cob_n")).intValue();
        mnFkAccountCashId_n = new Long((long) jsonHeader.get("fid_acc_cash_n")).intValue();
        mnFkUserAuditedId = new Long((long) jsonHeader.get("fid_usr_audit")).intValue();
        mnFkUserAuthorizedId = new Long((long) jsonHeader.get("fid_usr_authorn")).intValue();
        mnFkUserNewId = new Long((long) jsonHeader.get("fid_usr_new")).intValue();
        mnFkUserEditId = new Long((long) jsonHeader.get("fid_usr_edit")).intValue();
        mnFkUserDeleteId = new Long((long) jsonHeader.get("fid_usr_del")).intValue();
        mtUserAuditedTs = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonHeader.get("ts_audit"));
        mtUserAuthorizedTs = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonHeader.get("ts_authorn"));
        mtUserNewTs = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonHeader.get("ts_new"));
        mtUserEditTs = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonHeader.get("ts_edit"));
        mtUserDeleteTs = SLibUtils.DbmsDateFormatDatetime.parse((java.lang.String) jsonHeader.get("ts_del"));
        
        msDbmsBookkeepingCenterCode = (String) SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FIN_BKC, new int[] { mnPkBookkeepingCenterId }, SLibConstants.DESCRIPTION_CODE);
        msDbmsCompanyBranchCode = (String) SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.BPSU_BPB, new int[] { mnFkCompanyBranchId }, SLibConstants.DESCRIPTION_CODE);
        readAccountCash(client.getSession().getStatement());
                
        int n = 0;
        JSONObject jsonEntry = null;
        JSONObject jsonEntries = (JSONObject) jsonHeader.get("entries");
        
        while ((jsonEntry = (JSONObject) jsonEntries.get("entry-" + ++n)) != null) {
            SDataRecordEntry entry = new SDataRecordEntry();
            entry.setParentRecord(this);
            entry.decodeJson(client, jsonEntry.toJSONString());
            mvDbmsRecordEntries.add(entry);
        }
        
        // check if registry is new:
        
        mbIsRegistryNew = mnPkNumberId == 0;
        
        // mark this record as recovered!
        
        mbTempDataJustRecovered = true;
    }
}
