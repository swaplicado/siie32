/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class SDataDpsDestinyChangesLog extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkLogId;
    protected int mnFkBizPartnerAddresseeId_n;
    protected int mnFkAddresseeBizPartnerId_nr;
    protected int mnFkAddresseeBizPartnerBranchId_n;
    protected int mnFkAddresseeBizPartnerBranchAddressId_n;
    protected int mnFkUserChangedId;
    protected java.util.Date mtUserChangedTs;

    public SDataDpsDestinyChangesLog() {
        super(SDataConstants.TRN_DPS_DESTINY_CHANGES_LOG);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setFkBizPartnerAddresseeId_n(int n) { mnFkBizPartnerAddresseeId_n = n; }
    public void setFkAddresseeBizPartnerId_nr(int n) { mnFkAddresseeBizPartnerId_nr = n; }
    public void setFkAddresseeBizPartnerBranchId_n(int n) { mnFkAddresseeBizPartnerBranchId_n = n; }
    public void setFkAddresseeBizPartnerBranchAddressId_n(int n) { mnFkAddresseeBizPartnerBranchAddressId_n = n; }
    public void setFkUserChangedId(int n) { mnFkUserChangedId = n; }
    public void setUserChangedTs(java.util.Date t) { mtUserChangedTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkLogId() { return mnPkLogId; }
    public int getFkBizPartnerAddresseeId_n() { return mnFkBizPartnerAddresseeId_n; }
    public int getFkAddresseeBizPartnerId_nr() { return mnFkAddresseeBizPartnerId_nr; }
    public int getFkAddresseeBizPartnerBranchId_n() { return mnFkAddresseeBizPartnerBranchId_n; }
    public int getFkAddresseeBizPartnerBranchAddressId_n() { return mnFkAddresseeBizPartnerBranchAddressId_n; }
    public int getFkUserChangedId() { return mnFkUserChangedId; }
    public java.util.Date getUserChangedTs() { return mtUserChangedTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
        mnPkLogId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[]{mnPkYearId, mnPkDocId, mnPkLogId};
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkLogId = 0;
        mnFkBizPartnerAddresseeId_n = 0;
        mnFkUserChangedId = 0;
        mtUserChangedTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_dest_chg_log AS l "
                    + "WHERE l.id_year = " + key[0]
                    + " AND l.id_doc = " + key[1]
                    + " AND l.id_log = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("l.id_year");
                mnPkDocId = resultSet.getInt("l.id_doc");
                mnPkLogId = resultSet.getInt("l.id_log");
                mnFkBizPartnerAddresseeId_n = resultSet.getInt("l.fid_bp_addee_n");
                mnFkAddresseeBizPartnerId_nr = resultSet.getInt("l.fid_add_bp_nr");
                mnFkAddresseeBizPartnerBranchId_n = resultSet.getInt("l.fid_add_bpb_n");
                mnFkAddresseeBizPartnerBranchAddressId_n = resultSet.getInt("l.fid_add_add_n");
                mnFkUserChangedId = resultSet.getInt("l.fid_usr_chg");
                mtUserChangedTs = resultSet.getTimestamp("l.ts_chg");

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
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            try (Statement statement = connection.createStatement()) {
                String sql;

                if (mnPkLogId == 0) {
                    // get next entry ID:

                    sql = "SELECT COALESCE(MAX(id_log), 0) + 1 "
                            + "FROM trn_dps_dest_chg_log "
                            + "WHERE id_year = " + mnPkYearId + " "
                            + "AND id_doc = " + mnPkDocId + ";";
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        mnPkLogId = resultSet.getInt(1);
                    }

                    sql = "INSERT INTO trn_dps_dest_chg_log VALUES ("
                            + mnPkYearId + ", "
                            + mnPkDocId + ", "
                            + mnPkLogId + ", "
                            + (mnFkBizPartnerAddresseeId_n == 0 ? "NULL" : mnFkBizPartnerAddresseeId_n) + ", "
                            + (mnFkAddresseeBizPartnerId_nr == 0 ? "NULL" : mnFkAddresseeBizPartnerId_nr) + ", "
                            + (mnFkAddresseeBizPartnerBranchId_n == 0 ? "NULL" : mnFkAddresseeBizPartnerBranchId_n) + ", "
                            + (mnFkAddresseeBizPartnerBranchAddressId_n == 0 ? "NULL" : mnFkAddresseeBizPartnerBranchAddressId_n) + ", "
                            + mnFkUserChangedId + ", "
                            + "NOW() "
                            + ");";
                }
                else {
                    sql = "UPDATE trn_dps_dest_chg_log SET "
                            //+ "id_year = " + mnPkYearId + ", "
                            //+ "id_doc = " + mnPkDocId + ", "
                            //+ "id_log = " + mnPkLogId + ", "
                            + "fid_bp_addee_n = " + (mnFkBizPartnerAddresseeId_n == 0 ? "NULL" : mnFkBizPartnerAddresseeId_n) + ", "
                            + "fid_add_bp_nr = " + (mnFkAddresseeBizPartnerId_nr == 0 ? "NULL" : mnFkAddresseeBizPartnerId_nr) + ", "
                            + "fid_add_bpb_n = " + (mnFkAddresseeBizPartnerBranchId_n == 0 ? "NULL" : mnFkAddresseeBizPartnerBranchId_n) + ", "
                            + "fid_add_add_n = " + (mnFkAddresseeBizPartnerBranchAddressId_n == 0 ? "NULL" : mnFkAddresseeBizPartnerBranchAddressId_n) + ", "
                            + "fid_usr_chg = " + mnFkUserChangedId + ", "
                            + "ts_chg = " + "NOW() "
                            + "WHERE id_year = " + mnPkYearId + " "
                            + "AND id_doc = " + mnPkDocId + " "
                            + "AND id_log = " + mnPkLogId + ";";
                }

                statement.execute(sql);
            }

            mnDbmsErrorId = 0;
            msDbmsError = "";

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            Logger.getLogger(SDataDpsDestinyChangesLog.class.getName()).log(Level.SEVERE, null, e);
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserChangedTs;
    }
}
