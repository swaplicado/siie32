/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDsmEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected java.lang.String msSourceReference;
    protected double mdSourceValue;
    protected double mdSourceExchangeRate;
    protected double mdSourceExchangeRateSystem;
    protected double mdSourceValueCy;
    protected java.lang.String msDestinyReference;
    protected double mdDestinyValue;
    protected double mdDestinyExchangeRate;
    protected double mdDestinyExchangeRateSystem;
    protected double mdDestinyValueCy;
    protected boolean mbIsDeleted;
    protected int mnFkAccountingMoveTypeId;
    protected int mnFkAccountingMoveClassId;
    protected int mnFkAccountingMoveSubclassId;
    protected int mnFkBizPartnerId;
    protected int mnFkSourceCurrencyId;
    protected java.lang.String msFkSourceAccountId_n;
    protected int mnFkSourceDpsYearId_n;
    protected int mnFkSourceDpsDocId_n;
    protected int mnFkDestinyCurrencyId;
    protected java.lang.String msFkDestinyAccountId_n;
    protected int mnFkDestinyDpsYearId_n;
    protected int mnFkDestinyDpsDocId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected boolean mbDbmsIsReferenceTax;
    protected double mdDbmsBalanceDps;
    protected int mnDbmsFkDpsCategoryId;
    protected int mnDbmsCtSysMovId;
    protected int mnDbmsTpSysMovId;
    protected int mnDbmsFkBizPartnerBranchId_n;

    protected java.lang.String msDbmsAccountPay;
    protected java.lang.String msDbmsAccountOp;
    protected java.lang.String msDbmsSubclassMove;
    protected java.lang.String msDbmsBizPartner;
    protected java.lang.String msDbmsSourceCurrencyKey;
    protected java.lang.String msDbmsSourceAccount;
    protected java.lang.String msDbmsSourceDps;
    protected java.lang.String msDbmsSourceTpDps;
    protected java.lang.String msDbmsDestinyCurrencyKey;
    protected java.lang.String msDbmsDestinyAccount;
    protected java.lang.String msDbmsDestinyDps;
    protected java.lang.String msDbmsDestinyTpDps;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    protected java.util.Vector<erp.mtrn.data.SDataDsmEntryNotes> mvDbmsDsmEntryNotes;

    public SDataDsmEntry () {
        super(SDataConstants.TRN_DSM_ETY);

        mvDbmsDsmEntryNotes = new Vector<SDataDsmEntryNotes>();
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setSourceReference(java.lang.String s) { msSourceReference = s; }
    public void setSourceValue(double d) { mdSourceValue = d; }
    public void setSourceExchangeRate(double d) { mdSourceExchangeRate = d; }
    public void setSourceExchangeRateSystem(double d) { mdSourceExchangeRateSystem = d; }
    public void setSourceValueCy(double d) { mdSourceValueCy = d; }
    public void setDestinyReference(java.lang.String s) { msDestinyReference = s; }
    public void setDestinyValue(double d) { mdDestinyValue = d; }
    public void setDestinyExchangeRate(double d) { mdDestinyExchangeRate = d; }
    public void setDestinyExchangeRateSystem(double d) { mdDestinyExchangeRateSystem = d; }
    public void setDestinyValueCy(double d) { mdDestinyValueCy = d; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAccountingMoveTypeId(int n) { mnFkAccountingMoveTypeId = n; }
    public void setFkAccountingMoveClassId(int n) { mnFkAccountingMoveClassId = n; }
    public void setFkAccountingMoveSubclassId(int n) { mnFkAccountingMoveSubclassId = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkSourceCurrencyId(int n) { mnFkSourceCurrencyId = n; }
    public void setFkSourceAccountId_n(java.lang.String s) { msFkSourceAccountId_n = s; }
    public void setFkSourceDpsYearId_n(int n) { mnFkSourceDpsYearId_n = n; }
    public void setFkSourceDpsDocId_n(int n) { mnFkSourceDpsDocId_n = n; }
    public void setFkDestinyCurrencyId(int n) { mnFkDestinyCurrencyId = n; }
    public void setFkDestinyAccountId_n(java.lang.String s) { msFkDestinyAccountId_n = s; }
    public void setFkDestinyDpsYearId_n(int n) { mnFkDestinyDpsYearId_n = n; }
    public void setFkDestinyDpsDocId_n(int n) { mnFkDestinyDpsDocId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public java.lang.String getSourceReference() { return msSourceReference; }
    public double getSourceValue() { return mdSourceValue; }
    public double getSourceExchangeRate() { return mdSourceExchangeRate; }
    public double getSourceExchangeRateSystem() { return mdSourceExchangeRateSystem; }
    public double getSourceValueCy() { return mdSourceValueCy; }
    public java.lang.String getDestinyReference() { return msDestinyReference; }
    public double getDestinyValue() { return mdDestinyValue; }
    public double getDestinyExchangeRate() { return mdDestinyExchangeRate; }
    public double getDestinyExchangeRateSystem() { return mdDestinyExchangeRateSystem; }
    public double getDestinyValueCy() { return mdDestinyValueCy; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkAccountingMoveTypeId() { return mnFkAccountingMoveTypeId; }
    public int getFkAccountingMoveClassId() { return mnFkAccountingMoveClassId; }
    public int getFkAccountingMoveSubclassId() { return mnFkAccountingMoveSubclassId; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkSourceCurrencyId() { return mnFkSourceCurrencyId; }
    public java.lang.String getFkSourceAccountId_n() { return msFkSourceAccountId_n; }
    public int getFkSourceDpsYearId_n() { return mnFkSourceDpsYearId_n; }
    public int getFkSourceDpsDocId_n() { return mnFkSourceDpsDocId_n; }
    public int getFkDestinyCurrencyId() { return mnFkDestinyCurrencyId; }
    public java.lang.String getFkDestinyAccountId_n() { return msFkDestinyAccountId_n; }
    public int getFkDestinyDpsYearId_n() { return mnFkDestinyDpsYearId_n; }
    public int getFkDestinyDpsDocId_n() { return mnFkDestinyDpsDocId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsFkDpsCategoryId(int n) { mnDbmsFkDpsCategoryId = n; }
    public void setDbmsIsReferenceTax(boolean b) { mbDbmsIsReferenceTax = b; }
    public void setDbmsBalanceDps(double d) { mdDbmsBalanceDps = d; }
    public void setDbmsTpSysMovId(int n) { mnDbmsTpSysMovId = n; }
    public void setDbmsFkBizPartnerBranchId_n(int n) { mnDbmsFkBizPartnerBranchId_n = n; }
    public void setDbmsCtSysMovId(int n) { mnDbmsCtSysMovId = n; }
    public void setDbmsAccountPay (java.lang.String s) { msDbmsAccountPay = s; }
    public void setDbmsAccountOp (java.lang.String s) { msDbmsAccountOp = s; }
    public void setDbmsSubclassMove(java.lang.String s) { msDbmsSubclassMove = s; }
    public void setDbmsBizPartner(java.lang.String s) { msDbmsBizPartner = s; }
    public void setDbmsSourceCurrencyKey(java.lang.String s) { msDbmsSourceCurrencyKey = s; }
    public void setDbmsSourceAccount(java.lang.String s) { msDbmsSourceAccount = s; }
    public void setDbmsSourceDps(java.lang.String s) { msDbmsSourceDps = s; }
    public void setDbmsSourceTpDps(java.lang.String s) { msDbmsSourceTpDps = s; }
    public void setDbmsDestinyCurrencyKey(java.lang.String s) { msDbmsDestinyCurrencyKey = s; }
    public void setDbmsDestinyAccount(java.lang.String s) { msDbmsDestinyAccount = s; }
    public void setDbmsDestinyDps(java.lang.String s) { msDbmsDestinyDps = s; }
    public void setDbmsDestinyTpDps(java.lang.String s) { msDbmsDestinyTpDps = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public int getDbmsFkDpsCategoryId() { return mnDbmsFkDpsCategoryId; }
    public int getDbmsCtSysMovId() { return mnDbmsCtSysMovId; }
    public int getDbmsTpSysMovId() { return mnDbmsTpSysMovId; }
    public int getDbmsFkBizPartnerBranchId_n() { return mnDbmsFkBizPartnerBranchId_n; }
    public boolean getDbmsIsReferenceTax() { return mbDbmsIsReferenceTax; }
    public double getDbmsBalanceDps() { return mdDbmsBalanceDps; }
    public java.lang.String getDbmsAccountPay() { return msDbmsAccountPay; }
    public java.lang.String getDbmsAccountOp() { return msDbmsAccountOp; }
    public java.lang.String getDbmsSubclassMove() { return msDbmsSubclassMove; }
    public java.lang.String getDbmsBizPartner() { return msDbmsBizPartner; }
    public java.lang.String getDbmsSourceCurrencyKey() { return msDbmsSourceCurrencyKey; }
    public java.lang.String getDbmsSourceAccount() { return msDbmsSourceAccount; }
    public java.lang.String getDbmsSourceDps() { return msDbmsSourceDps; }
    public java.lang.String getDbmsSourceTpDps() { return msDbmsSourceTpDps; }
    public java.lang.String getDbmsDestinyCurrencyKey() { return msDbmsDestinyCurrencyKey; }
    public java.lang.String getDbmsDestinyAccount() { return msDbmsDestinyAccount; }
    public java.lang.String getDbmsDestinyDps() { return msDbmsDestinyDps; }
    public java.lang.String getDbmsDestinyTpDps() { return msDbmsDestinyTpDps; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    public java.util.Vector<SDataDsmEntryNotes> getDbmsNotes() { return mvDbmsDsmEntryNotes; }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        msSourceReference = "";
        mdSourceValue = 0;
        mdSourceExchangeRate = 0;
        mdSourceExchangeRateSystem = 0;
        mdSourceValueCy = 0;
        msDestinyReference = "";
        mdDestinyValue = 0;
        mdDestinyExchangeRate = 0;
        mdDestinyExchangeRateSystem = 0;
        mdDestinyValueCy = 0;
        mbIsDeleted = false;
        mnFkAccountingMoveTypeId = 0;
        mnFkAccountingMoveClassId = 0;
        mnFkAccountingMoveSubclassId = 0;
        mnFkBizPartnerId = 0;
        mnFkSourceCurrencyId = 0;
        msFkSourceAccountId_n = "";
        mnFkSourceDpsYearId_n = 0;
        mnFkSourceDpsDocId_n = 0;
        mnFkDestinyCurrencyId = 0;
        msFkDestinyAccountId_n = "";
        mnFkDestinyDpsYearId_n = 0;
        mnFkDestinyDpsDocId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mnDbmsFkDpsCategoryId = 0;
        mnDbmsCtSysMovId = 0;
        mnDbmsTpSysMovId = 0;
        mnDbmsFkBizPartnerBranchId_n = 0;
        mbDbmsIsReferenceTax = false;
        mdDbmsBalanceDps = 0;
        msDbmsAccountPay = "";
        msDbmsAccountOp = "";
        msDbmsSubclassMove = "";
        msDbmsBizPartner = "";
        msDbmsSourceCurrencyKey = "";
        msDbmsSourceAccount = "";
        msDbmsSourceDps = "";
        msDbmsDestinyCurrencyKey = "";
        msDbmsDestinyAccount = "";
        msDbmsDestinyDps = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }

    @Override
    public void setPrimaryKey(Object key) {
        mnPkYearId = ((int[]) key)[0];
        mnPkDocId = ((int[]) key)[1];
        mnPkEntryId = ((int[]) key)[2];
    }

    @Override
    public Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId };
    }

    @Override
    public int read(Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        java.sql.ResultSet resultSet = null;
        java.sql.Statement statementAux = null;
        SDataDsmEntryNotes oDsmEntryNotes = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT dsm.*, cls.cls_acc_mov, bp.bp, cur1.cur_key, acc1.acc, CONCAT(dps1.num_ser, '-', dps1.num) AS folio, " +
                "cur2.cur_key, acc2.acc, CONCAT(dps2.num_ser, '-', dps2.num) AS folio1, tp.code AS src_cod, tp1.code AS des_cod, un.usr, ue.usr, ud.usr " +
                "FROM trn_dsm_ety AS dsm " +
                "INNER JOIN erp.fins_cls_acc_mov AS cls ON dsm.fid_tp_acc_mov = cls.id_tp_acc_mov AND dsm.fid_cl_acc_mov = cls.id_cl_acc_mov AND " +
                "dsm.fid_cls_acc_mov = cls.id_cls_acc_mov " +
                "INNER JOIN erp.bpsu_bp AS bp ON dsm.fid_bp = bp.id_bp " +
                "INNER JOIN erp.cfgu_cur AS cur1 ON dsm.fid_src_cur = cur1.id_cur " +
                "INNER JOIN erp.cfgu_cur AS cur2 ON dsm.fid_des_cur = cur2.id_cur " +
                "INNER JOIN erp.usru_usr AS un ON dsm.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON dsm.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON dsm.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_dps AS dps1 ON dsm.fid_src_dps_year_n = dps1.id_year AND dsm.fid_src_dps_doc_n = dps1.id_doc " +
                "LEFT OUTER JOIN erp.trnu_tp_dps AS tp ON dps1.fid_ct_dps = tp.id_ct_dps AND dps1.fid_cl_dps = tp.id_cl_dps AND dps1.fid_tp_dps = tp.id_tp_dps " +
                "LEFT OUTER JOIN trn_dps AS dps2 ON dsm.fid_des_dps_year_n = dps2.id_year AND dsm.fid_des_dps_doc_n = dps2.id_doc " +
                "LEFT OUTER JOIN erp.trnu_tp_dps AS tp1 ON dps2.fid_ct_dps = tp1.id_ct_dps AND dps2.fid_cl_dps = tp1.id_cl_dps AND dps2.fid_tp_dps = tp1.id_tp_dps " +
                "LEFT OUTER JOIN fin_acc AS acc1 ON dsm.fid_src_acc_n = acc1.id_acc " +
                "LEFT OUTER JOIN fin_acc AS acc2 ON dsm.fid_des_acc_n = acc2.id_acc " +
                "WHERE dsm.id_year = " + key[0] + " AND dsm.id_doc = " + key[1] + " AND dsm.id_ety = " + key[2] + " " +
                "ORDER BY id_ety ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("dsm.id_year");
                mnPkDocId = resultSet.getInt("dsm.id_doc");
                mnPkEntryId = resultSet.getInt("dsm.id_ety");
                msSourceReference = resultSet.getString("dsm.src_ref");
                mdSourceValue = resultSet.getDouble("dsm.src_val");
                mdSourceExchangeRate = resultSet.getDouble("dsm.src_exc_rate");
                mdSourceExchangeRateSystem = resultSet.getDouble("dsm.src_exc_rate_sys");
                mdSourceValueCy = resultSet.getDouble("dsm.src_val_cur");
                msDestinyReference = resultSet.getString("dsm.des_ref");
                mdDestinyValue = resultSet.getDouble("dsm.des_val");
                mdDestinyExchangeRate = resultSet.getDouble("dsm.des_exc_rate");
                mdDestinyExchangeRateSystem = resultSet.getDouble("dsm.des_exc_rate_sys");
                mdDestinyValueCy = resultSet.getDouble("dsm.des_val_cur");
                mbIsDeleted = resultSet.getBoolean("dsm.b_del");
                mnFkAccountingMoveTypeId = resultSet.getInt("dsm.fid_tp_acc_mov");
                mnFkAccountingMoveClassId = resultSet.getInt("dsm.fid_cl_acc_mov");
                mnFkAccountingMoveSubclassId = resultSet.getInt("dsm.fid_cls_acc_mov");
                mnFkBizPartnerId = resultSet.getInt("dsm.fid_bp");
                mnFkSourceCurrencyId = resultSet.getInt("dsm.fid_src_cur");
                msFkSourceAccountId_n = resultSet.getString("dsm.fid_src_acc_n");
                if (resultSet.wasNull()) msFkSourceAccountId_n = "";
                mnFkSourceDpsYearId_n = resultSet.getInt("dsm.fid_src_dps_year_n");
                if (resultSet.wasNull()) mnFkSourceDpsYearId_n = 0;
                mnFkSourceDpsDocId_n = resultSet.getInt("dsm.fid_src_dps_doc_n");
                if (resultSet.wasNull()) mnFkSourceDpsDocId_n = 0;
                mnFkDestinyCurrencyId = resultSet.getInt("dsm.fid_des_cur");
                msFkDestinyAccountId_n = resultSet.getString("dsm.fid_des_acc_n");
                if (resultSet.wasNull()) msFkDestinyAccountId_n = "";
                mnFkDestinyDpsYearId_n = resultSet.getInt("dsm.fid_des_dps_year_n");
                if (resultSet.wasNull()) mnFkDestinyDpsYearId_n = 0;
                mnFkDestinyDpsDocId_n = resultSet.getInt("dsm.fid_des_dps_doc_n");
                if (resultSet.wasNull()) mnFkDestinyDpsDocId_n = 0;
                mnFkUserNewId = resultSet.getInt("dsm.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("dsm.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("dsm.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("dsm.ts_new");
                mtUserEditTs = resultSet.getTimestamp("dsm.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("dsm.ts_del");

                msDbmsSubclassMove = resultSet.getString("cls.cls_acc_mov");
                if (resultSet.wasNull()) msDbmsSubclassMove = "";
                msDbmsBizPartner = resultSet.getString("bp.bp");
                if (resultSet.wasNull()) msDbmsBizPartner = "";
                msDbmsSourceCurrencyKey = resultSet.getString("cur1.cur_key");
                if (resultSet.wasNull()) msDbmsSourceCurrencyKey = "";
                msDbmsSourceAccount = resultSet.getString("acc1.acc");
                if (resultSet.wasNull()) msDbmsSourceAccount = "(n/a)";
                msDbmsSourceDps = resultSet.getString("folio");
                if (resultSet.wasNull()) msDbmsSourceDps = "";
                msDbmsDestinyCurrencyKey = resultSet.getString("cur2.cur_key");
                if (resultSet.wasNull()) msDbmsDestinyCurrencyKey = "";
                msDbmsDestinyAccount = resultSet.getString("acc2.acc");
                if (resultSet.wasNull()) msDbmsDestinyAccount = "(n/a)";
                msDbmsDestinyDps = resultSet.getString("folio1");
                if (resultSet.wasNull()) msDbmsDestinyDps = "";
                msDbmsUserNew = resultSet.getString("un.usr");
                if (resultSet.wasNull()) msDbmsUserNew = "";
                msDbmsUserEdit = resultSet.getString("ue.usr");
                if (resultSet.wasNull()) msDbmsUserEdit = "";
                msDbmsUserDelete = resultSet.getString("ud.usr");
                if (resultSet.wasNull()) msDbmsUserDelete = "";
                msDbmsSourceTpDps = resultSet.getString("src_cod");
                if (resultSet.wasNull()) msDbmsSourceTpDps = "";
                msDbmsDestinyTpDps = resultSet.getString("des_cod");
                if (resultSet.wasNull()) msDbmsDestinyTpDps = "";

                // Read notes:

                statementAux = statement.getConnection().createStatement();
                mvDbmsDsmEntryNotes.removeAllElements();
                sql = "SELECT * FROM trn_dsm_ety_nts " +
                    "WHERE id_year = " + key[0] + " AND id_ety = " + key[1] + " AND id_doc = " + key[2] + " " +
                    "ORDER BY nts ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oDsmEntryNotes = new SDataDsmEntryNotes();
                    if (oDsmEntryNotes.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc"), resultSet.getInt("id_ety"), resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDsmEntryNotes.add(oDsmEntryNotes);
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
        int nParam = 1;
        int i = 0;
        String sql = "";
        java.sql.CallableStatement callableStatement = null;
        java.sql.Statement statementAux = null;
        SDataDsmEntryNotes dataDsmEntryNotes = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ " +
                    "CALL trn_dsm_ety_save(" +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setString(nParam++, msSourceReference);
            callableStatement.setDouble(nParam++, mdSourceValue);
            callableStatement.setDouble(nParam++, mdSourceExchangeRate);
            callableStatement.setDouble(nParam++, mdSourceExchangeRateSystem);
            callableStatement.setDouble(nParam++, mdSourceValueCy);
            callableStatement.setString(nParam++, msDestinyReference);
            callableStatement.setDouble(nParam++, mdDestinyValue);
            callableStatement.setDouble(nParam++, mdDestinyExchangeRate);
            callableStatement.setDouble(nParam++, mdDestinyExchangeRateSystem);
            callableStatement.setDouble(nParam++, mdDestinyValueCy);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkAccountingMoveTypeId);
            callableStatement.setInt(nParam++, mnFkAccountingMoveClassId);
            callableStatement.setInt(nParam++, mnFkAccountingMoveSubclassId);
            callableStatement.setInt(nParam++, mnFkBizPartnerId);
            callableStatement.setInt(nParam++, mnFkSourceCurrencyId);
            if (msFkSourceAccountId_n.length() > 0) callableStatement.setString(nParam++, msFkSourceAccountId_n); else callableStatement.setNull(nParam++, java.sql.Types.VARCHAR);
            if (mnFkSourceDpsYearId_n > 0) callableStatement.setInt(nParam++, mnFkSourceDpsYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkSourceDpsDocId_n > 0) callableStatement.setInt(nParam++, mnFkSourceDpsDocId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkDestinyCurrencyId);
            if (msFkDestinyAccountId_n.length() > 0) callableStatement.setString(nParam++, msFkDestinyAccountId_n); else callableStatement.setNull(nParam++, java.sql.Types.VARCHAR);
            if (mnFkDestinyDpsYearId_n > 0) callableStatement.setInt(nParam++, mnFkDestinyDpsYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkDestinyDpsDocId_n > 0) callableStatement.setInt(nParam++, mnFkDestinyDpsDocId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {

                // Save notes:

                statementAux = connection.createStatement();
                sql = "DELETE FROM trn_dsm_ety_nts WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId;
                statementAux.execute(sql);
                for (i = 0; i < mvDbmsDsmEntryNotes.size(); i++) {
                    dataDsmEntryNotes = (SDataDsmEntryNotes) mvDbmsDsmEntryNotes.get(i);
                    if (dataDsmEntryNotes != null) {
                        dataDsmEntryNotes.setPkYearId(mnPkYearId);
                        dataDsmEntryNotes.setPkDocId(mnPkDocId);
                        dataDsmEntryNotes.setPkEntryId(mnPkEntryId);
                        dataDsmEntryNotes.setPkNotesId(0);
                        if (dataDsmEntryNotes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
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
