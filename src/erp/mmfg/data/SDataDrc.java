/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mod.SModSysConsts;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDrc extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.util.Date mtDate;
    protected java.lang.String msNumber;
    protected java.util.Date mtWeekStart;
    protected java.util.Date mtWeekEnd;
    protected boolean mbIsCostDirect;
    protected boolean mbIsDeleted;
    protected int mnFkCompanyBranchId;
    protected int mnFkBizPartnerId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<erp.mmfg.data.SDataDrcEntry> mvDbmsDrcEntries;
    protected java.util.Vector<erp.mfin.data.SDataRecord> mvDbmsRecords;

    private erp.mmfg.data.SDataDrcEntry moDbmsDrcEntry;
    private erp.mmfg.data.SDataDrcEntryHour moDbmsDrcEntryHour;

    public SDataDrc() {
        super(SDataConstants.MFG_DRC);

        mvDbmsDrcEntries = new Vector<SDataDrcEntry>();
        mvDbmsRecords = new Vector<SDataRecord>();

        reset();
    }

    /* Private functions */

    private void saveAccountingRecords(java.sql.Connection connection) {
        int i=0;
        int j=0;

        SDataRecord oRecord = null;
        SDataRecordEntry oRecordEntry = null;
        SimpleDateFormat oFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            for (i=0; i<mvDbmsDrcEntries.size(); i++) {

                // 01. Create record accounting (header) by date:

                moDbmsDrcEntry = (SDataDrcEntry) mvDbmsDrcEntries.get(i);
                oRecord = (SDataRecord) mvDbmsRecords.get(i);
                oRecord = createRecordHeader(oRecord, "DOC. LABORAL, FOLIO: " + mnPkDocId + ", FECHA: " + oFormat.format(moDbmsDrcEntry.getPkDayId()));
                for (j=0; j<moDbmsDrcEntry.getDbmsDrcEntriesHours().size(); j++) {

                    moDbmsDrcEntryHour = moDbmsDrcEntry.getDbmsDrcEntriesHours().get(j);

                    // 02. Create entry record accounting with account 'fid_acc_payroll':

                    oRecordEntry = new SDataRecordEntry();
                    oRecordEntry.setPrimaryKey(new Object[] { oRecord.getPkYearId(), oRecord.getPkPeriodId(), oRecord.getPkBookkeepingCenterId(), oRecord.getPkRecordTypeId(), oRecord.getPkNumberId(), 0 });
                    oRecordEntry = createRecordEntry(oRecordEntry, moDbmsDrcEntry.getDbmsAccounts()[0],
                            "DOC. LABORAL, FOLIO: " + mnPkDocId + ", FECHA: " + oFormat.format(moDbmsDrcEntry.getPkDayId()) +
                            "TIPO HORA: " + moDbmsDrcEntryHour.getPkHourTypeId(), j+1,
                            0, calculateTotalHours(connection, moDbmsDrcEntryHour.mdQuantity));
                    oRecord.getDbmsRecordEntries().add(oRecordEntry);

                    // 03. Create entry record accounting with account 'fid_acc_expen_mfg' or 'fid_acc_wp':

                    oRecordEntry = new SDataRecordEntry();
                    oRecordEntry.setPrimaryKey(new Object[] { oRecord.getPkYearId(), oRecord.getPkPeriodId(), oRecord.getPkBookkeepingCenterId(), oRecord.getPkRecordTypeId(), oRecord.getPkNumberId(), 0 });
                    oRecordEntry = createRecordEntry(oRecordEntry, moDbmsDrcEntry.getDbmsAccounts()[1],
                            "DOC. LABORAL, FOLIO: " + mnPkDocId + ", FECHA: " + oFormat.format(moDbmsDrcEntry.getPkDayId()) +
                            "TIPO HORA: " + moDbmsDrcEntryHour.getPkHourTypeId(), j+2,
                            calculateTotalHours(connection, moDbmsDrcEntryHour.mdQuantity), 0);
                    oRecord.getDbmsRecordEntries().add(oRecordEntry);

                }

                // 04. Save record accounting:

                if (oRecord.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }

                // 05. Save relation between drc vs. record:

                if (saveDrcVsRecord(connection, oRecord) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
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

        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
    }

    private erp.mfin.data.SDataRecord createRecordHeader(erp.mfin.data.SDataRecord oRecord, java.lang.String sConcept) {

        oRecord.setDate(moDbmsDrcEntry.getPkDayId());
        oRecord.setConcept(sConcept.length() > 100 ? sConcept.substring(0, 96).trim() + "..." : sConcept);
        oRecord.setIsAudited(false);
        oRecord.setIsAuthorized(false);
        oRecord.setIsSystem(true);
        oRecord.setIsDeleted(mbIsDeleted);
        oRecord.setFkCompanyBranchId(mnFkCompanyBranchId);
        oRecord.setFkCompanyBranchId_n(0);
        oRecord.setFkAccountCashId_n(0);

        if (mbIsRegistryNew) {
            oRecord.setFkUserNewId(mnFkUserNewId);
            oRecord.setFkUserEditId(mnFkUserNewId);
        }
        else {
            oRecord.setFkUserNewId(mnFkUserEditId);
            oRecord.setFkUserEditId(mnFkUserEditId);
        }

        return oRecord;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntry(erp.mfin.data.SDataRecordEntry oRecordEntry, java.lang.String sAccount, java.lang.String sConcept, int nSortPos, double dDebit, double dCredit) {

        // Create record entry complement:

        oRecordEntry.setConcept(sConcept.length() > 100 ? sConcept.substring(0, 96).trim() + "..." : sConcept);
        oRecordEntry.setFkAccountIdXXX(sAccount);
        oRecordEntry.setSortingPosition(nSortPos);
        oRecordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_COGM[0]);
        oRecordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_COGM[1]);
        oRecordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_COGM[2]);
        oRecordEntry.setFkSystemMoveClassId(SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT[0]);
        oRecordEntry.setFkSystemMoveTypeId(SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT[1]);
        oRecordEntry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[0]);
        oRecordEntry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_NA_NA[1]);
        oRecordEntry.setFkSystemMoveCategoryIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[0]);
        oRecordEntry.setFkSystemMoveTypeIdXXX(SDataConstantsSys.FINS_TP_SYS_MOV_NA[1]);
        oRecordEntry.setFkCompanyBranchId_n(mnFkCompanyBranchId);
        oRecordEntry.setFkBizPartnerId_nr(mnFkBizPartnerId);
        oRecordEntry.setFkMfgYearId_n(moDbmsDrcEntry.getFkOrdYearId_n());
        oRecordEntry.setFkMfgOrdId_n(moDbmsDrcEntry.getFkOrdDocId_n());
        oRecordEntry.setFkPlantCompanyBranchId_n(moDbmsDrcEntry.getFkCobId_n());
        oRecordEntry.setFkPlantEntityId_n(moDbmsDrcEntry.getFkEntId_n());
        oRecordEntry.setFkCostGicId_n(moDbmsDrcEntry.getFkCostGicId_n());

        oRecordEntry.setDebit(dDebit);
        oRecordEntry.setCredit(dCredit);
        oRecordEntry.setDebitCy(dDebit);
        oRecordEntry.setCreditCy(dCredit);
        oRecordEntry.setFkCurrencyId(1);
        oRecordEntry.setExchangeRate(1);
        oRecordEntry.setExchangeRateSystem(1);

        oRecordEntry.setReference("");
        oRecordEntry.setFkDpsYearId_n(0);
        oRecordEntry.setFkDpsDocId_n(0);
        oRecordEntry.setUnits(0);
        oRecordEntry.setIsDeleted(mbIsDeleted);
        oRecordEntry.setIsSystem(true);
        oRecordEntry.setFkCostCenterIdXXX_n("");
        oRecordEntry.setFkEntityId_n(0);
        oRecordEntry.setFkTaxBasicId_n(0);
        oRecordEntry.setFkTaxId_n(0);
        oRecordEntry.setFkBizPartnerBranchId_n(0);
        oRecordEntry.setFkYearId_n(0);
        oRecordEntry.setFkReferenceCategoryId_n(0);
        oRecordEntry.setFkDpsAdjustmentYearId_n(0);
        oRecordEntry.setFkDpsAdjustmentDocId_n(0);
        oRecordEntry.setFkDiogYearId_n(0);
        oRecordEntry.setFkDiogDocId_n(0);
        oRecordEntry.setFkItemId_n(0);
        oRecordEntry.setFkCheckWalletId_n(0);
        oRecordEntry.setFkCheckId_n(0);
        oRecordEntry.setFkUserNewId(mnFkUserNewId);
        oRecordEntry.setFkUserEditId(1);
        oRecordEntry.setFkUserDeleteId(1);
        oRecordEntry.setUserNewTs(mtDate);
        oRecordEntry.setUserEditTs(mtDate);
        oRecordEntry.setUserDeleteTs(mtDate);

        return oRecordEntry;
    }

    private int saveDrcVsRecord(java.sql.Connection connection, erp.mfin.data.SDataRecord oRecord) {
        int nResult=SLibConstants.UNDEFINED;

        SDataDrcEntryRecord oDrcEntryRecord = new SDataDrcEntryRecord();

        try {
            oDrcEntryRecord.setPrimaryKey(new Object[] {
                moDbmsDrcEntry.getPkYearId(), moDbmsDrcEntry.getPkDocId(), moDbmsDrcEntry.getPkEntryId(), moDbmsDrcEntry.getPkDayId(),
                oRecord.getPkYearId(), oRecord.getPkPeriodId(), oRecord.getPkBookkeepingCenterId(), oRecord.getPkRecordTypeId(), oRecord.getPkNumberId()  });

            if (oDrcEntryRecord.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
            }

            nResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return nResult;
    }

    private int deleteAccountingRecords(java.sql.Connection connection) {
        java.lang.String sSql = "";

        Statement statement = null;
        ResultSet resultSet = null;
        SDataRecord oRecord = null;
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();
            for (int i=0; i<mvDbmsDrcEntries.size(); i++) {

                moDbmsDrcEntry = mvDbmsDrcEntries.get(i);

                // Read record data:

                sSql = "SELECT * FROM mfg_drc_ety_rec " +
                    "WHERE id_drc_year = " + moDbmsDrcEntry.getPkYearId() +
                    " AND id_drc_doc = " + moDbmsDrcEntry.getPkDocId() +
                    " AND id_drc_ety = " + moDbmsDrcEntry.getPkEntryId() +
                    " AND id_drc_day = '" + sdfFormat.format(moDbmsDrcEntry.getPkDayId()) + "' ";
                resultSet = statement.executeQuery(sSql);
                if (!resultSet.next()) {
                    mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
                }
                else {
                    oRecord = new SDataRecord();

                    oRecord.setPrimaryKey(new Object [] {
                        resultSet.getInt("id_rec_year"),
                        resultSet.getInt("id_rec_per"),
                        resultSet.getInt("id_rec_bkc"),
                        resultSet.getString("id_rec_tp_rec"),
                        resultSet.getInt("id_rec_num") });

                    // Delete mfg_drc_ety_rec row:

                    sSql = "DELETE FROM mfg_drc_ety_rec " +
                        "WHERE id_drc_year = " + moDbmsDrcEntry.getPkYearId() +
                        " AND id_drc_doc = " + moDbmsDrcEntry.getPkDocId();
                    statement.execute(sSql);

                    // Delete record entries data:

                    sSql = "DELETE FROM fin_rec_ety " +
                        "WHERE id_year = " + oRecord.getPkYearId() +
                        " AND id_per = " + oRecord.getPkPeriodId() +
                        " AND id_bkc = " + oRecord.getPkBookkeepingCenterId() +
                        " AND id_tp_rec = '" + oRecord.getPkRecordTypeId() + "' " +
                        " AND id_num = " + oRecord.getPkNumberId() + "; ";
                    statement.execute(sSql);

                    // Delete record entries data:

                    sSql = "DELETE FROM fin_rec " +
                        "WHERE id_year = " + oRecord.getPkYearId() +
                        " AND id_per = " + oRecord.getPkPeriodId() +
                        " AND id_bkc = " + oRecord.getPkBookkeepingCenterId() +
                        " AND id_tp_rec = '" + oRecord.getPkRecordTypeId() + "' " +
                        " AND id_num = " + oRecord.getPkNumberId() + "; ";
                    statement.execute(sSql);

                    mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
                }
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    private double calculateTotalHours(java.sql.Connection connection, double dQuantity) {
        double dTotalHours = 0;
        java.lang.String sSql = "";

        Statement statement = null;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            sSql = "SELECT c.cost " +
                "FROM erp.bpsu_bp AS bp " +
                "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp " +
                "INNER JOIN bps_cost AS c ON ct.fid_ct_bp = c.id_ct_bp AND ct.fid_tp_bp = c.id_tp_bp " +
                "WHERE ct.id_bp = " + mnFkBizPartnerId + " AND bp.b_att_emp = 1 ";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sSql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                dTotalHours = dQuantity * resultSet.getDouble("c.cost");
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

        return dTotalHours;
    }

    /* Public funcitions */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setNumber(java.lang.String s) { msNumber = s; }
    public void setWeekStart(java.util.Date t) { mtWeekStart = t; }
    public void setWeekEnd(java.util.Date t) { mtWeekEnd = t; }
    public void setIsCostDirect(boolean b) { mbIsCostDirect = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getNumber() { return msNumber; }
    public java.util.Date getWeekStart() { return mtWeekStart; }
    public java.util.Date getWeekEnd() { return mtWeekEnd; }
    public boolean getIsCostDirect() { return mbIsCostDirect; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public java.util.Vector<SDataDrcEntry> getDbmsDrcEntry() { return mvDbmsDrcEntries; }
    public java.util.Vector<SDataRecord> getDbmsRecords() { return mvDbmsRecords; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        msNumber = "";
        mtWeekStart = null;
        mtWeekEnd = null;
        mbIsCostDirect = false;
        mbIsDeleted = false;
        mnFkCompanyBranchId = 0;
        mnFkBizPartnerId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mvDbmsDrcEntries.clear();
        mvDbmsRecords.clear();
        moDbmsDrcEntry = null;
        moDbmsDrcEntryHour = null;

    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;

        Statement statementAux = null;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT *, erp.lib_fix_int(num, 4) AS f_num FROM mfg_drc WHERE id_year = " + key[0] + " AND id_doc =  " + key[1];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mtDate = resultSet.getDate("dt");
                msNumber = resultSet.getString("f_num");
                mtWeekStart = resultSet.getDate("week_start");
                mtWeekEnd = resultSet.getDate("week_end");
                mbIsCostDirect = resultSet.getBoolean("b_cost_dir");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkCompanyBranchId = resultSet.getInt("fid_cob");
                mnFkBizPartnerId = resultSet.getInt("fid_bp");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read entries:

                statementAux = statement.getConnection().createStatement();
                mvDbmsDrcEntries.removeAllElements();
                sql = "SELECT * FROM mfg_drc_ety " +
                    "WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " " +
                    "GROUP BY id_year, id_doc, id_ety " + // Group by day (date)
                    "ORDER BY id_ety, id_day ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    moDbmsDrcEntry = new SDataDrcEntry();
                    if (moDbmsDrcEntry.read(new Object[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDrcEntries.add(moDbmsDrcEntry);
                    }
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

    @Override
    public int save(java.sql.Connection connection) {
        int i=0;
        int nParam=1;
        java.lang.String sSql ="";
        java.util.Date tDay=null;

        CallableStatement callableStatement = null;
        Statement statementAux = null;

        SDataDrcEntry oDrcEntry = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_drc_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setString(nParam++, msNumber);
            callableStatement.setDate(nParam++, new java.sql.Date(mtWeekStart.getTime()));
            callableStatement.setDate(nParam++, new java.sql.Date(mtWeekEnd.getTime()));
            callableStatement.setBoolean(nParam++, mbIsCostDirect);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkCompanyBranchId);
            callableStatement.setInt(nParam++, mnFkBizPartnerId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkDocId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
                statementAux = connection.createStatement();

                // Delete accounting records:

                if(!mbIsRegistryNew) {

                    deleteAccountingRecords(connection);

                    // Delete hours of entries:

                    sSql = "DELETE FROM mfg_drc_ety_hr WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                    statementAux.execute(sSql);

                    // Delete entries:

                    sSql = "DELETE FROM mfg_drc_ety WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
                    statementAux.execute(sSql);
                }

                // Save entries:

                for (i = 0; i < mvDbmsDrcEntries.size(); i++) {
                    oDrcEntry = (SDataDrcEntry) mvDbmsDrcEntries.get(i);
                    if (oDrcEntry != null) {
                        oDrcEntry.setPkYearId(mnPkYearId);
                        oDrcEntry.setPkDocId(mnPkDocId);
                        oDrcEntry.setFkUserNewId(mnFkUserNewId);
                        if (oDrcEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }
            }

            // Record accounting:

            if (mvDbmsDrcEntries.size() > 0) {

                // Save accounting records:

                saveAccountingRecords(connection);
            }

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
