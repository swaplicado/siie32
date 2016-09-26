/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDataRecord extends erp.lib.data.SDataRegistry implements java.io.Serializable {

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

    protected erp.mfin.data.SDataAccountCash moDbmsDataAccountCash;
    protected java.util.Vector<erp.mfin.data.SDataRecordEntry> mvDbmsRecordEntries;

    public SDataRecord() {
        super(SDataConstants.FIN_REC);
        mlRegistryTimeout = 1000 * 60 * 60 * 2; // 2 hr
        mvDbmsRecordEntries = new Vector<SDataRecordEntry>();
        reset();
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

    public void setDbmsDataAccountCash(erp.mfin.data.SDataAccountCash o) { moDbmsDataAccountCash = o; }

    public erp.mfin.data.SDataAccountCash getDbmsDataAccountCash() { return moDbmsDataAccountCash; }
    public java.util.Vector<SDataRecordEntry> getDbmsRecordEntries() { return mvDbmsRecordEntries; }

    public java.lang.String getRecordPeriod() {
        return SLibUtils.DecimalFormatCalendarYear.format(mnPkYearId) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(mnPkPeriodId);
    }

    public java.lang.String getRecordNumber() {
        return msPkRecordTypeId + "-" + SLibUtils.DecimalNumberFormat.format(mnPkNumberId);
    }

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

        moDbmsDataAccountCash = null;
        mvDbmsRecordEntries.clear();
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
            sql = "SELECT * FROM fin_rec " +
                    "WHERE id_year = " + key[0] + " AND id_per = " + key[1] + " AND " +
                    "id_bkc = " + key[2] + " AND id_tp_rec = '" + key[3] + "' AND " +
                    "id_num = " + key[4] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkPeriodId = resultSet.getInt("id_per");
                mnPkBookkeepingCenterId = resultSet.getInt("id_bkc");
                msPkRecordTypeId = resultSet.getString("id_tp_rec");
                mnPkNumberId = resultSet.getInt("id_num");
                mtDate = resultSet.getDate("dt");
                msConcept = resultSet.getString("concept");
                mbIsAdjustmentsYearEnd = resultSet.getBoolean("b_adj_year");
                mbIsAdjustmentsAudit = resultSet.getBoolean("b_adj_audit");
                mbIsAudited = resultSet.getBoolean("b_audit");
                mbIsAuthorized = resultSet.getBoolean("b_authorn");
                mbIsSystem = resultSet.getBoolean("b_sys");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkCompanyBranchId = resultSet.getInt("fid_cob");
                mnFkCompanyBranchId_n = resultSet.getInt("fid_cob_n");
                mnFkAccountCashId_n = resultSet.getInt("fid_acc_cash_n");
                mnFkUserAuditedId = resultSet.getInt("fid_usr_audit");
                mnFkUserAuthorizedId = resultSet.getInt("fid_usr_authorn");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserAuditedTs = resultSet.getTimestamp("ts_audit");
                mtUserAuthorizedTs = resultSet.getTimestamp("ts_authorn");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read aswell cash account:

                if (mnFkCompanyBranchId_n != 0 && mnFkAccountCashId_n != 0) {
                    moDbmsDataAccountCash = new SDataAccountCash();
                    if (moDbmsDataAccountCash.read(new int[] { mnFkCompanyBranchId_n, mnFkAccountCashId_n }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                }

                statementAux = statement.getConnection().createStatement();

                // Read aswell record entries:

                sql = "SELECT id_ety FROM fin_rec_ety " +
                        "WHERE id_year = " + (Integer) key[0] + " AND id_per = " + (Integer) key[1] + " AND " +
                        "id_bkc = " + (Integer) key[2] + " AND id_tp_rec = '" + (String) key[3] + "' AND id_num = " + (Integer) key[4] + " " +
                        "ORDER BY sort_pos, id_ety ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataRecordEntry entry = new SDataRecordEntry();
                    if (entry.read(new Object[] { mnPkYearId, mnPkPeriodId, mnPkBookkeepingCenterId, msPkRecordTypeId, mnPkNumberId, resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsRecordEntries.add(entry);
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

    /**
     * Saves accounting record.
     * Sorting position of all active entries must be already set.
     */
    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
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
                if (mnDbmsErrorId != 0) {
                    throw new Exception(msDbmsError);
                }
                else {
                    // Save aswell record entries:

                    for (SDataRecordEntry entry : mvDbmsRecordEntries) {
                        if (entry.getIsRegistryNew() || entry.getIsRegistryEdited()) {
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

                    updateChecksLinks(connection);  // link check registries with corresponding record entries

                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
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

        return mnLastDbActionResult;
    }
    
    public void saveField(java.sql.Connection connection, final int field, final Object value) throws Exception {

        Statement statement = null;
        
        int queryResultId = SLibConstants.DB_ACTION_SAVE_ERROR;
        String msSql = "UPDATE " + "fin_rec" + " SET ";
        
        try {
            switch (field) {
                case SUtilConsts.AUD:
                    msSql += "b_audit = " + (boolean) value + ", ts_audit = NOW(), fid_usr_audit = " + mnFkUserEditId + " ";
                    break;
                
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            statement = connection.createStatement();

            msSql += "WHERE id_year = " + mnPkYearId + " AND id_per = " + mnPkPeriodId + " AND " +
                        "id_bkc = " + mnPkBookkeepingCenterId + " AND id_tp_rec = '" + msPkRecordTypeId + "' AND id_num = " + mnPkNumberId + " ";
            
            statement.execute(msSql);

            queryResultId = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
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
}
