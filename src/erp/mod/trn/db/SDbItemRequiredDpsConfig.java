/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

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
public class SDbItemRequiredDpsConfig extends SDbRegistryUser {
    
    protected int mnPkDpsCategoryId;
    protected int mnPkDpsClassId;
    protected int mnPkDpsTypeId;
    protected int mnPkConfigId;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkLinkTypeId;
    protected int mnFkReferenceId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbItemRequiredDpsConfig() {
        super(SModConsts.TRNU_TP_DPS_SRC_ITEM);
    }
    
    public void setPkDpsCategoryId(int n) { mnPkDpsCategoryId = n; }
    public void setPkDpsClassId(int n) { mnPkDpsClassId = n; }
    public void setPkDpsTypeId(int n) { mnPkDpsTypeId = n; }
    public void setPkConfigId(int n) { mnPkConfigId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkLinkTypeId(int n) { mnFkLinkTypeId = n; }
    public void setFkReferenceId(int n) { mnFkReferenceId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDpsCategoryId() { return mnPkDpsCategoryId; }
    public int getPkDpsClassId() { return mnPkDpsClassId; }
    public int getPkDpsTypeId() { return mnPkDpsTypeId; }
    public int getPkConfigId() { return mnPkConfigId; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkLinkTypeId() { return mnFkLinkTypeId; }
    public int getFkReferenceId() { return mnFkReferenceId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsCategoryId = pk[0];
        mnPkDpsClassId = pk[1];
        mnPkDpsTypeId = pk[2];
        mnPkConfigId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsCategoryId, mnPkDpsClassId, mnPkDpsTypeId, mnPkConfigId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkDpsCategoryId = 0;
        mnPkDpsClassId = 0;
        mnPkDpsTypeId = 0;
        mnPkConfigId = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkLinkTypeId = 0;
        mnFkReferenceId = 0;
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
        return "WHERE id_ct_dps = " + mnPkDpsCategoryId + " AND "
                + "id_cl_dps = " + mnPkDpsClassId  + " AND "
                + "id_tp_dps = " + mnPkDpsTypeId  + " AND "
                + "id_cfg = " + mnPkConfigId  + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ct_dps = " + pk[0] + " AND "
                + "id_cl_dps = " + pk[1]  + " AND "
                + "id_tp_dps = " + pk[2]  + " AND "
                + "id_cfg = " + pk[3]  + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkConfigId = 0;

        msSql = "SELECT COALESCE(MAX(id_cfg), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_ct_dps = " + mnPkDpsCategoryId + " AND " +
                "id_cl_dps = " + mnPkDpsClassId  + " AND " +
                "id_tp_dps = " + mnPkDpsTypeId  + " ";
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
            mnPkDpsCategoryId = resultSet.getInt("id_ct_dps");
            mnPkDpsClassId = resultSet.getInt("id_cl_dps");
            mnPkDpsTypeId = resultSet.getInt("id_tp_dps");
            mnPkConfigId = resultSet.getInt("id_cfg");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkLinkTypeId = resultSet.getInt("fk_tp_link");
            mnFkReferenceId = resultSet.getInt("fk_ref");
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
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDpsCategoryId + ", " + 
                    mnPkDpsClassId + ", " + 
                    mnPkDpsTypeId + ", " + 
                    mnPkConfigId + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkLinkTypeId + ", " + 
                    mnFkReferenceId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " +
                    "NOW()" + "); " ; 
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_ct_dps = " + mnPkDpsCategoryId + ", " +
                    "id_cl_dps = " + mnPkDpsClassId + ", " +
                    "id_tp_dps = " + mnPkDpsTypeId + ", " +
                    "id_cfg = " + mnPkConfigId + ", " +
                    */
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_tp_link = " + mnFkLinkTypeId + ", " +
                    "fk_ref = " + mnFkReferenceId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " 
                    + getSqlWhere() ;
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbItemRequiredDpsConfig clone() throws CloneNotSupportedException {
        SDbItemRequiredDpsConfig registry = new SDbItemRequiredDpsConfig();
        
        registry.setPkDpsCategoryId(this.getPkDpsCategoryId());
        registry.setPkDpsClassId(this.getPkDpsClassId());
        registry.setPkDpsTypeId(this.getPkDpsTypeId());
        registry.setPkConfigId(this.getPkConfigId());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkLinkTypeId(this.getFkLinkTypeId());
        registry.setFkReferenceId(this.getFkReferenceId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }
    
}
