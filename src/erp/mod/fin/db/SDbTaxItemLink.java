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
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbTaxItemLink extends SDbRegistryUser {

    protected int mnPkLinkId;
    protected int mnPkReferenceId;
    protected int mnPkConfigId;
    protected Date mtDateStart;
    //protected boolean mbDeleted;
    protected int mnFkTaxRegionId;
    protected int mnFkTaxGroupId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbTaxItemLink() {
        super(SModConsts.FIN_TAX_ITEM_LINK);
    }

    public void setPkLinkId(int n) { mnPkLinkId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setPkConfigId(int n) { mnPkConfigId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkTaxRegionId(int n) { mnFkTaxRegionId = n; }
    public void setFkTaxGroupId(int n) { mnFkTaxGroupId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkLinkId() { return mnPkLinkId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public int getPkConfigId() { return mnPkConfigId; }
    public Date getDateStart() { return mtDateStart; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkTaxRegionId() { return mnFkTaxRegionId; }
    public int getFkTaxGroupId() { return mnFkTaxGroupId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLinkId = pk[0];
        mnPkReferenceId = pk[1];
        mnPkConfigId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLinkId, mnPkReferenceId, mnPkConfigId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLinkId = 0;
        mnPkReferenceId = 0;
        mnPkConfigId = 0;
        mtDateStart = null;
        mbDeleted = false;
        mnFkTaxRegionId = 0;
        mnFkTaxGroupId = 0;
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
        return "WHERE id_link = " + mnPkLinkId + " AND " +
                "id_ref = " + mnPkReferenceId + " AND " +
                "id_cfg = " + mnPkConfigId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_link = " + pk[0] + " AND " +
                "id_ref = " + pk[1] + " AND " +
                "id_cfg = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConfigId = 0;

        msSql = "SELECT COALESCE(MAX(id_cfg), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_link = " + mnPkLinkId + " AND id_ref = " + mnPkReferenceId + " " ;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkConfigId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkLinkId = resultSet.getInt("id_link");
            mnPkReferenceId = resultSet.getInt("id_ref");
            mnPkConfigId = resultSet.getInt("id_cfg");
            mtDateStart = resultSet.getDate("dt_start");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkTaxRegionId = resultSet.getInt("fk_tax_reg");
            mnFkTaxGroupId = resultSet.getInt("fk_tax_grp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLinkId + ", " +
                    mnPkReferenceId + ", " +
                    mnPkConfigId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkTaxRegionId + ", " +
                    mnFkTaxGroupId + ", " +
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
                    "id_link = " + mnPkLinkId + ", " +
                    "id_ref = " + mnPkReferenceId + ", " +
                    "id_cfg = " + mnPkConfigId + ", " +
                    */
                    "dt_start = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tax_reg = " + mnFkTaxRegionId + ", " +
                    "fk_tax_grp = " + mnFkTaxGroupId + ", " +
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
    public SDbTaxItemLink clone() throws CloneNotSupportedException {
        SDbTaxItemLink registry = new SDbTaxItemLink();

        registry.setPkLinkId(this.getPkLinkId());
        registry.setPkReferenceId(this.getPkReferenceId());
        registry.setPkConfigId(this.getPkConfigId());
        registry.setDateStart(this.getDateStart());
        registry.setDeleted(this.isDeleted());
        registry.setFkTaxRegionId(this.getFkTaxRegionId());
        registry.setFkTaxGroupId(this.getFkTaxGroupId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
