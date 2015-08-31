/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import java.util.Vector;
import java.sql.ResultSet;
import java.sql.Statement;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public class SDataAccountBizPartner extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkAccountBizPartnerId;
    protected java.lang.String msAccountBizPartner;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<erp.mfin.data.SDataAccountBizPartnerEntry> mvDbmsEntries;
    protected java.util.Vector<erp.mfin.data.SDataAccountBizPartnerBpType> mvDbmsBizPartnerTypes;
    protected java.util.Vector<erp.mfin.data.SDataAccountBizPartnerBp> mvDbmsBizPartners;

    public SDataAccountBizPartner() {
        super(SDataConstants.FIN_ACC_BP);
        mvDbmsEntries = new Vector<SDataAccountBizPartnerEntry>();
        mvDbmsBizPartnerTypes = new Vector<SDataAccountBizPartnerBpType>();
        mvDbmsBizPartners = new Vector<SDataAccountBizPartnerBp>();
        reset();
    }

    public void setPkAccountBizPartnerId(int n) { mnPkAccountBizPartnerId = n; }
    public void setAccountBizPartner(java.lang.String s) { msAccountBizPartner = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkAccountBizPartnerId() { return mnPkAccountBizPartnerId; }
    public java.lang.String getAccountBizPartner() { return msAccountBizPartner; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public java.util.Vector<erp.mfin.data.SDataAccountBizPartnerEntry> getDbmsEntries() { return mvDbmsEntries; }
    public java.util.Vector<erp.mfin.data.SDataAccountBizPartnerBpType> getDbmsBizPartnerTypes() { return mvDbmsBizPartnerTypes; }
    public java.util.Vector<erp.mfin.data.SDataAccountBizPartnerBp> getDbmsBizPartners() { return mvDbmsBizPartners; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkAccountBizPartnerId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkAccountBizPartnerId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkAccountBizPartnerId = 0;
        msAccountBizPartner = "";
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mvDbmsEntries.clear();
        mvDbmsBizPartnerTypes.clear();
        mvDbmsBizPartners.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM fin_acc_bp WHERE id_acc_bp = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkAccountBizPartnerId = resultSet.getInt("id_acc_bp");
                msAccountBizPartner = resultSet.getString("acc_bp");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                statementAux = statement.getConnection().createStatement();

                // Read aswell account entries:

                sql = "SELECT id_tp_acc_bp, id_ety FROM fin_acc_bp_ety WHERE id_acc_bp = " + key[0] + " " +
                        "ORDER BY id_tp_acc_bp, fid_acc, fid_cc_n, fid_tp_bkr, id_ety ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccountBizPartnerEntry entry = new SDataAccountBizPartnerEntry();
                    if (entry.read(new int[] { mnPkAccountBizPartnerId, resultSet.getInt("id_tp_acc_bp"), resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsEntries.add(entry);
                    }
                }

                // Read aswell asignation on business partner types:

                sql = "SELECT id_ct_bp, id_tp_bp, id_bkc, id_dt_start FROM fin_acc_bp_tp_bp WHERE id_acc_bp = " + key[0] + " " +
                        "ORDER BY b_del DESC, id_ct_bp, id_tp_bp, id_bkc, id_acc_bp, id_dt_start ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccountBizPartnerBpType entry = new SDataAccountBizPartnerBpType();
                    if (entry.read(new Object[] { resultSet.getInt("id_ct_bp"), resultSet.getInt("id_tp_bp"), resultSet.getInt("id_bkc"), mnPkAccountBizPartnerId, resultSet.getTimestamp("id_dt_start") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartnerTypes.add(entry);
                    }
                }

                // Read aswell asignation on business partners:

                sql = "SELECT id_bp, id_ct_bp, id_bkc, id_dt_start FROM fin_acc_bp_bp WHERE id_acc_bp = " + key[0] + " " +
                        "ORDER BY b_del DESC, id_bp, id_ct_bp, id_bkc, id_acc_bp, id_dt_start ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataAccountBizPartnerBp entry = new SDataAccountBizPartnerBp();
                    if (entry.read(new Object[] { resultSet.getInt("id_bp"), resultSet.getInt("id_ct_bp"), resultSet.getInt("id_bkc"), mnPkAccountBizPartnerId, resultSet.getTimestamp("id_dt_start") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBizPartners.add(entry);
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
        int i = 0;
        int nParam = 1;
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL fin_acc_bp_save(" +
                    "?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkAccountBizPartnerId);
            callableStatement.setString(nParam++, msAccountBizPartner);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkAccountBizPartnerId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Delete previous account entries:

                connection.createStatement().execute("DELETE FROM fin_acc_bp_ety WHERE id_acc_bp = " + mnPkAccountBizPartnerId + " ");

                // Save aswell account entries:

                for (i = 0; i < mvDbmsEntries.size(); i++) {
                    mvDbmsEntries.get(i).setIsRegistryNew(true);
                    mvDbmsEntries.get(i).setPkAccountBizPartnerId(mnPkAccountBizPartnerId);
                    mvDbmsEntries.get(i).setPkEntryId(0);   // force save action as if registry were new

                    if (mvDbmsEntries.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell asignation on business partner types:

                for (i = 0; i < mvDbmsBizPartnerTypes.size(); i++) {
                    mvDbmsBizPartnerTypes.get(i).setPkAccountBizPartnerId(mnPkAccountBizPartnerId);

                    if (mvDbmsBizPartnerTypes.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

                // Save aswell asignation on business partners:

                for (i = 0; i < mvDbmsBizPartners.size(); i++) {
                    mvDbmsBizPartners.get(i).setPkAccountBizPartnerId(mnPkAccountBizPartnerId);

                    if (mvDbmsBizPartners.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                }

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

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
