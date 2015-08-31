/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbAbpBizPartnerLink extends SDbRegistryUser {

    protected int mnPkAbpBizPartnerId;
    protected int mnPkLinkId;
    protected int mnPkReferenceId;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected int mnAuxBizPartnerCatId;

    public SDbAbpBizPartnerLink() {
        super(SModConsts.FIN_ABP_BP_LINK);
    }

    public void setPkAbpBizPartnerId(int n) { mnPkAbpBizPartnerId = n; }
    public void setPkLinkId(int n) { mnPkLinkId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setAuxBizPartnerCatId(int n) { mnAuxBizPartnerCatId = n; }

    public int getPkAbpBizPartnerId() { return mnPkAbpBizPartnerId; }
    public int getPkLinkId() { return mnPkLinkId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public int getAuxBizPartnerCatId() { return mnAuxBizPartnerCatId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpBizPartnerId = pk[0];
        mnPkLinkId = pk[1];
        mnPkReferenceId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpBizPartnerId, mnPkLinkId, mnPkReferenceId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpBizPartnerId = 0;
        mnPkLinkId = 0;
        mnPkReferenceId = 0;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_abp_bp = " + mnPkAbpBizPartnerId + " AND " +
                "id_link = " + mnPkLinkId + " AND " +
                "id_ref = " + mnPkReferenceId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE bp.id_abp_bp = " + pk[0] + " AND " +
                "bp.id_link = " + pk[1] + " AND " +
                "bp.id_ref = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT bp.*, abp.fk_ct_bp FROM " + getSqlTable() + " AS bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_BP) + " AS abp ON bp.id_abp_bp = abp.id_abp_bp "
                + getSqlWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkAbpBizPartnerId = resultSet.getInt("bp.id_abp_bp");
            mnPkLinkId = resultSet.getInt("bp.id_link");
            mnPkReferenceId = resultSet.getInt("bp.id_ref");
            mbDeleted = resultSet.getBoolean("bp.b_del");
            mnFkUserInsertId = resultSet.getInt("bp.fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("bp.fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("bp.ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("bp.ts_usr_upd");

            mnAuxBizPartnerCatId = resultSet.getInt("abp.fk_ct_bp");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAbpBizPartnerId + ", " +
                    mnPkLinkId + ", " +
                    mnPkReferenceId + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_abp_bp = " + mnPkAbpBizPartnerId + ", " +
                    "id_link = " + mnPkLinkId + ", " +
                    "id_ref = " + mnPkReferenceId + ", " +
                    */
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbAbpBizPartnerLink clone() throws CloneNotSupportedException {
        SDbAbpBizPartnerLink registry = new SDbAbpBizPartnerLink();

        registry.setPkAbpBizPartnerId(this.getPkAbpBizPartnerId());
        registry.setPkLinkId(this.getPkLinkId());
        registry.setPkReferenceId(this.getPkReferenceId());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
