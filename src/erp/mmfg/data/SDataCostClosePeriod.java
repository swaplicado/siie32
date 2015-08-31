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
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */

public class SDataCostClosePeriod extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkTypeCostObjectId;
    protected int mnPkRefCompanyBranchId;
    protected int mnPkRefReferenceId;
    protected int mnPkRefEntityId;
    protected int mnPkBizPartnerId;
    protected java.util.Date mtPkDateId;
    protected int mnPkTypeHourId;
    protected double mdQuantity;
    protected boolean mbIsAccounting;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnDbmsPkBookkepingCenterId;
    protected int[] mvDbmsPeriod;
    protected java.lang.String msDbmsFkAccountPayRoll;
    protected java.lang.String msDbmsFkAccountMfgWp;
    protected java.util.Date mtDbmsDateStart;
    protected java.util.Date mtDbmsDateEnd;

    public SDataCostClosePeriod() {
        super(SDataConstants.MFGX_COST_CLS_PER);

        reset();
    }

    /* Private functions */

    private void saveAccountingRecords(java.sql.Connection connection, SDataCostClosePeriod oCost) {
        int i=0;
        int j=0;

        SDataRecord oRecord = null;
        SDataRecordEntry oRecordEntry = null;
        SimpleDateFormat oFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // 01. Create record accounting (header) by date:

            oRecord = new SDataRecord();
            oRecord.setPrimaryKey(new Object[] { mvDbmsPeriod[0], mvDbmsPeriod[1], mnDbmsPkBookkepingCenterId, "CP", 0 });
            oRecord = createRecordHeader(oRecord, oCost, "DOC. LABORAL, FECHA: " + oFormat.format(oCost.getPkDateId()));

            // 02. Create entry record accounting with account 'fid_acc_payroll':

            oRecordEntry = new SDataRecordEntry();
            oRecordEntry.setPrimaryKey(new Object[] { oRecord.getPkYearId(), oRecord.getPkPeriodId(), oRecord.getPkBookkeepingCenterId(), oRecord.getPkRecordTypeId(), oRecord.getPkNumberId(), 0 });
            oRecordEntry = createRecordEntry(oRecordEntry, oCost, msDbmsFkAccountPayRoll,
                    "DOC. LABORAL, FECHA: " + oFormat.format(oCost.getPkDateId()) +
                    "TIPO HORA: ", 1, 0, calculateTotalHours(connection, oCost, oCost.getQuantity()));
            oRecord.getDbmsRecordEntries().add(oRecordEntry);

            // 03. Create entry record accounting with account 'fid_acc_expen_mfg' or 'fid_acc_wp':

            oRecordEntry = new SDataRecordEntry();
            oRecordEntry.setPrimaryKey(new Object[] { oRecord.getPkYearId(), oRecord.getPkPeriodId(), oRecord.getPkBookkeepingCenterId(), oRecord.getPkRecordTypeId(), oRecord.getPkNumberId(), 0 });
            oRecordEntry = createRecordEntry(oRecordEntry, oCost, msDbmsFkAccountMfgWp,
                    "DOC. LABORAL, FECHA: " + oFormat.format(oCost.getPkDateId()) +
                    "TIPO HORA: ", 1, calculateTotalHours(connection, oCost, oCost.getQuantity()), 0);
            oRecord.getDbmsRecordEntries().add(oRecordEntry);

            // 04. Save record accounting:

            if (oRecord.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
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

    private erp.mfin.data.SDataRecord createRecordHeader(erp.mfin.data.SDataRecord oRecord, SDataCostClosePeriod oCost, java.lang.String sConcept) {

        oRecord.setDate(oCost.getPkDateId());
        oRecord.setConcept(sConcept.length() > 100 ? sConcept.substring(0, 96).trim() + "..." : sConcept);
        oRecord.setIsAudited(false);
        oRecord.setIsAuthorized(false);
        oRecord.setIsSystem(true);
        oRecord.setIsDeleted(mbIsDeleted);
        oRecord.setFkCompanyBranchId(mnPkRefCompanyBranchId);
        oRecord.setFkCompanyBranchId_n(0);
        oRecord.setFkAccountCashId_n(0);
        oRecord.setFkUserNewId(oCost.getFkUserNewId());
        oRecord.setFkUserEditId(oCost.getFkUserEditId());

        return oRecord;
    }

    private erp.mfin.data.SDataRecordEntry createRecordEntry(erp.mfin.data.SDataRecordEntry oRecordEntry, SDataCostClosePeriod oCost, java.lang.String sAccount, java.lang.String sConcept, int nSortPos, double dDebit, double dCredit) {

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
        oRecordEntry.setFkCompanyBranchId_n(mnPkRefCompanyBranchId);
        oRecordEntry.setFkBizPartnerId_nr(oCost.getPkBizPartnerId());

        if (oCost.getPkTypeCostObjectId() == SDataConstantsSys.MFGS_TP_COST_ORD) {
            oRecordEntry.setFkMfgYearId_n(oCost.getPkRefReferenceId());
            oRecordEntry.setFkMfgOrdId_n(oCost.getPkRefEntityId());
        }
        else {
            oRecordEntry.setFkPlantCompanyBranchId_n(oCost.getPkRefReferenceId());
            oRecordEntry.setFkPlantEntityId_n(oCost.getPkRefEntityId());
        }
        oRecordEntry.setFkCostGicId_n(1); // XXX Check value for this field

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
        oRecordEntry.setFkUserNewId(oCost.getFkUserNewId());
        oRecordEntry.setFkUserEditId(oCost.getFkUserEditId());
        oRecordEntry.setFkUserDeleteId(oCost.getFkUserDeleteId());
        oRecordEntry.setUserNewTs(oCost.getUserNewTs());
        oRecordEntry.setUserEditTs(oCost.getUserEditTs());
        oRecordEntry.setUserDeleteTs(oCost.getUserDeleteTs());

        return oRecordEntry;
    }

    private double calculateTotalHours(java.sql.Connection connection, SDataCostClosePeriod oCost, double dQuantity) {
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
                "WHERE ct.id_bp = " + oCost.getPkBizPartnerId() + " AND bp.b_att_emp = 1 ";

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

    /* Public functions */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkTypeCostObjectId(int n) { mnPkTypeCostObjectId = n; }
    public void setPkRefCompanyBranchId(int n) { mnPkRefCompanyBranchId = n; }
    public void setPkRefReferenceId(int n) { mnPkRefReferenceId = n; }
    public void setPkRefEntityId(int n) { mnPkRefEntityId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkDateId(java.util.Date t) { mtPkDateId = t; }
    public void setPkTypeHourId(int n) { mnPkTypeHourId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setIsAccounting(boolean b) { mbIsAccounting = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkTypeCostObjectId() { return mnPkTypeCostObjectId; }
    public int getPkRefCompanyBranchId() { return mnPkRefCompanyBranchId; }
    public int getPkRefReferenceId() { return mnPkRefReferenceId; }
    public int getPkRefEntityId() { return mnPkRefEntityId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public java.util.Date getPkDateId() { return mtPkDateId; }
    public int getPkTypeHourId() { return mnPkTypeHourId; }
    public double getQuantity() { return mdQuantity; }
    public boolean getIsAccounting() { return mbIsAccounting; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsDateStart(java.util.Date t) { mtDbmsDateStart = t; }
    public void setDbmsDateEnd(java.util.Date t) { mtDbmsDateEnd = t; }
    public void setDbmsPeriod(int[] n) { mvDbmsPeriod = n; }
    public void setDbmsPkBookkepingCenterId(int n) { mnDbmsPkBookkepingCenterId = n; }
    public void setDbmsFkAccountPayRoll(java.lang.String s) { msDbmsFkAccountPayRoll = s; }
    public void setDbmsFkAccountMfgWp(java.lang.String s) { msDbmsFkAccountMfgWp = s; };

    public java.util.Date getDbmsDateStart() { return mtDbmsDateStart; }
    public java.util.Date getDbmsDateEnd() { return mtDbmsDateEnd; }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkTypeCostObjectId = 0;
        mnPkRefCompanyBranchId = 0;
        mnPkRefReferenceId = 0;
        mnPkRefEntityId = 0;
        mnPkBizPartnerId = 0;
        mtPkDateId = null;
        mnPkTypeHourId = 0;
        mdQuantity = 0;
        mbIsAccounting = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mtDbmsDateStart = null;
        mtDbmsDateEnd = null;
        mvDbmsPeriod = null;
    }

    @Override
    public void setPrimaryKey(java.lang.Object key) {

        /****
         * This application has a different form for save and read registry,
         * the registry is read for range of dates (start date - end date)
         ****/

        System.out.println("setPrimaryKey");
        System.out.println(((int[]) key)[1]);
        mnPkYearId = ((int[]) key)[1];
        mnPkTypeCostObjectId = ((int[]) key)[2];
        mnPkRefCompanyBranchId = ((int[]) key)[3];
        mnPkRefReferenceId = ((int[]) key)[4];
        mnPkRefEntityId = ((int[]) key)[5];
        mtDbmsDateStart = ((java.util.Date[]) key)[6];
        mtDbmsDateEnd = ((java.util.Date[]) key)[7];
        System.out.println("END setPrimaryKey");
    }

    @Override
    public java.lang.Object[] getPrimaryKey() {
        return new java.lang.Object[] { mnPkYearId, mnPkTypeCostObjectId, mnPkRefCompanyBranchId, mnPkRefReferenceId, mnPkRefEntityId, mnPkBizPartnerId, mtPkDateId, mnPkTypeHourId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        Object[] key = (Object[]) pk;
        String sql = "";

        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            System.out.println("ANTES DE SQL");
            sql = "SELECT bp.bp, b.bpb, t.tp_cost_obj, " +
                ((Integer)((Object[]) key)[2] == SDataConstantsSys.MFGS_TP_COST_ORD ? "o.ref " :
                 (Integer)((Object[]) key)[2] == SDataConstantsSys.MFGS_TP_COST_LINE ? "l.line " :
                 (Integer)((Object[]) key)[2] == SDataConstantsSys.MFGS_TP_COST_PLT ? "e.ent " : "b.bpb ") +
                "AS f_cost_obj, c.* " +
                "FROM mfg_cost AS c " +
                "INNER JOIN erp.mfgs_tp_cost_obj AS t ON c.id_tp_cost_obj = t.id_tp_cost_obj " +
                "INNER JOIN erp.bpsu_bp AS bp ON c.id_bp = bp.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS b ON c.id_ref_cob = b.id_bpb " +
                ((Integer)((Object[]) key)[2] == SDataConstantsSys.MFGS_TP_COST_ORD ? "INNER JOIN mfg_ord AS o ON c.id_ref_ref = o.id_year AND c.id_ref_ent = o.id_ord " :
                 (Integer)((Object[]) key)[2] == SDataConstantsSys.MFGS_TP_COST_LINE ? "INNER JOIN mfg_line AS l ON c.id_ref_cob = l.id_cob AND c.id_ref_ref = l.id_ent AND c.id_ref_ent = l.id_line " :
                 (Integer)((Object[]) key)[2] == SDataConstantsSys.MFGS_TP_COST_PLT ? "INNER JOIN erp.cfgu_cob_ent AS e ON c.id_ref_ref = e.id_cob AND c.id_ref_ent = e.id_ent " : "") +
                "WHERE " +
                "c.id_year = " + (Integer)((Object[]) key)[1] +
                " AND c.id_tp_cost_obj = " + (Integer)((Object[]) key)[2] +
                " AND c.id_ref_cob = " + (Integer)((Object[]) key)[3] +
                " AND c.id_ref_ref = " + (Integer)((Object[]) key)[4] +
                " AND c.id_ref_ent = " + (Integer)((Object[]) key)[5] +
                " AND c.id_dt BETWEEN '" + (java.util.Date)((Object[]) key)[7] + "' AND '" + (java.util.Date)((Object[]) key)[8] + "' " +
                " AND c.b_del = 0 " +
                // "GROUP BY c.id_year, c.id_tp_cost_obj, c.fid_cob, c.fid_cob_n, c.fid_ent_n, c.fid_ord_year_n, c.fid_ord_ord_n, c.fid_line_cob_n, c.fid_ent_n, c.fid_line_line_n " +
                "ORDER BY bp.bp, c.id_year, c.id_tp_cost_obj, c.id_ref_cob, c.id_ref_ref, c.id_ref_ent, c.id_bp, c.id_dt, c.id_tp_hr ";

            mtDbmsDateStart = (java.util.Date)((Object[]) key)[7];
            mtDbmsDateEnd = (java.util.Date)((Object[]) key)[8];

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("c.id_year");
                mnPkTypeCostObjectId = resultSet.getInt("c.id_tp_cost_obj");
                mnPkRefCompanyBranchId = resultSet.getInt("c.id_ref_cob");
                mnPkRefReferenceId = resultSet.getInt("c.id_ref_ref");
                mnPkRefEntityId = resultSet.getInt("c.id_ref_ent");
                mnPkBizPartnerId = resultSet.getInt("c.id_bp");
                mtPkDateId = resultSet.getDate("c.id_dt");
                mnPkTypeHourId = resultSet.getInt("c.id_tp_hr");
                mdQuantity = resultSet.getDouble("c.qty");
                mbIsAccounting = resultSet.getBoolean("c.b_acc");
                mbIsDeleted = resultSet.getBoolean("c.b_del");
                mnFkUserNewId = resultSet.getInt("c.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("c.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("c.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("c.ts_new");
                mtUserEditTs = resultSet.getTimestamp("c.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("c.ts_del");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        String sSql="";
        SimpleDateFormat oFormat = new SimpleDateFormat("yyyy/MM/dd");

        ResultSet resultSet = null;
        Statement statement = null;
        SDataCostClosePeriod oCost = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            // Accounting registry:

            // 01. Executed query for read affected registries:

            sSql = "SELECT * " +
                "FROM mfg_cost " +
                "WHERE id_dt BETWEEN '" + oFormat.format(mtDbmsDateStart) + "' AND '" + oFormat.format(mtDbmsDateEnd) + "' AND b_del = 0 AND " +
                "id_ref_cob = " + mnPkRefCompanyBranchId + " AND id_ref_ref = " + mnPkRefReferenceId + " AND id_ref_ent = " + mnPkRefEntityId + " " +
                "ORDER BY id_year, id_tp_cost_obj, id_ref_cob, id_ref_ref, id_ref_ent, id_bp, id_dt, id_tp_hr ";
            System.out.println("sSql: " + sSql);

            // 02. Accounting registries:

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sSql);
            while (resultSet.next()) {
                oCost = new SDataCostClosePeriod();
                oCost.setPkYearId(resultSet.getInt("id_year"));
                oCost.setPkTypeCostObjectId(resultSet.getInt("id_tp_cost_obj"));
                oCost.setPkRefCompanyBranchId(resultSet.getInt("id_ref_cob"));
                oCost.setPkRefReferenceId(resultSet.getInt("id_ref_ref"));
                oCost.setPkRefEntityId(resultSet.getInt("id_ref_ent"));
                oCost.setPkBizPartnerId(resultSet.getInt("id_bp"));
                oCost.setPkDateId(resultSet.getDate("id_dt"));
                oCost.setPkTypeHourId(resultSet.getInt("id_tp_hr"));
                oCost.setQuantity(resultSet.getDouble("qty"));
                oCost.setIsAccounting(resultSet.getBoolean("b_acc"));
                oCost.setIsDeleted(resultSet.getBoolean("b_del"));
                oCost.setFkUserNewId(resultSet.getInt("fid_usr_new"));
                oCost.setFkUserEditId(resultSet.getInt("fid_usr_edit"));
                oCost.setFkUserDeleteId(resultSet.getInt("fid_usr_del"));
                oCost.setUserNewTs(resultSet.getTimestamp("ts_new"));
                oCost.setUserEditTs(resultSet.getTimestamp("ts_edit"));
                oCost.setUserDeleteTs(resultSet.getTimestamp("ts_del"));

                // 03. Accounting each registry:

                saveAccountingRecords(connection, oCost);
            }

            // 04. Update cost registry with 'b_acc = true':

            sSql = "UPDATE mfg_cost SET b_acc = 1 " +
                "WHERE id_dt BETWEEN '" + oFormat.format(mtDbmsDateStart) + "' AND '" + oFormat.format(mtDbmsDateEnd) + "' AND b_del = 0 AND " +
                "id_ref_cob = " + mnPkRefCompanyBranchId + " AND id_ref_ref = " + mnPkRefReferenceId + " AND id_ref_ent = " + mnPkRefEntityId + " " +
                "ORDER BY id_year, id_tp_cost_obj, id_bp, id_dt, id_tp_hr ";
            System.out.println("sSql: " + sSql);
            statement.executeUpdate(sSql);

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
    public int delete(java.sql.Connection connection) {
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
