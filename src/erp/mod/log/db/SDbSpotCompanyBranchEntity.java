/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbSpotCompanyBranchEntity extends SDbRegistryUser {

    public static final int FIELD_FK_SPOT = FIELD_BASE + 1;
    
    protected int mnPkCompanyBranchId;
    protected int mnPkEntityId;
    //protected boolean mbDeleted;
    protected int mnFkSpotId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbSpotCompanyBranchEntity() {
        super(SModConsts.LOGU_SPOT_COB_ENT);
    }

    public void setPkCompanyBranchId(int n) { mnPkCompanyBranchId = n; }
    public void setPkEntityId(int n) { mnPkEntityId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkSpotId(int n) { mnFkSpotId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCompanyBranchId() { return mnPkCompanyBranchId; }
    public int getPkEntityId() { return mnPkEntityId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkSpotId() { return mnFkSpotId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCompanyBranchId = pk[0];
        mnPkEntityId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCompanyBranchId, mnPkEntityId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCompanyBranchId = 0;
        mnPkEntityId = 0;
        mbDeleted = false;
        mnFkSpotId = 0;
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
        return "WHERE id_cob = " + mnPkCompanyBranchId + " AND id_ent = " + mnPkEntityId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cob = " + pk[0] + " AND id_ent = " + pk[1] + " ";
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

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkCompanyBranchId = resultSet.getInt("id_cob");
            mnPkEntityId = resultSet.getInt("id_ent");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkSpotId = resultSet.getInt("fk_spot");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkCompanyBranchId + ", " + 
                mnPkEntityId + ", " + 
                (mbDeleted ? 1 : 0) + ", " + 
                mnFkSpotId + ", " + 
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
                    "id_cob = " + mnPkCompanyBranchId + ", " +
                    "id_ent = " + mnPkEntityId + ", " +
                    */
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_spot = " + mnFkSpotId + ", " +
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
    public Object readField(Statement statement, int[] pk, int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;
                
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case FIELD_FK_SPOT:
                msSql += "fk_spot FROM " + getSqlTable() + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        resultSet = statement.executeQuery(msSql);
        
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
                case FIELD_FK_SPOT:
                    value = resultSet.getInt(1);
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
        
        return value;
    }
    
    @Override
    public SDbSpotCompanyBranchEntity clone() throws CloneNotSupportedException {
        SDbSpotCompanyBranchEntity registry = new SDbSpotCompanyBranchEntity();

        registry.setPkCompanyBranchId(this.getPkCompanyBranchId());
        registry.setPkEntityId(this.getPkEntityId());
        registry.setDeleted(this.isDeleted());
        registry.setFkSpotId(this.getFkSpotId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }
}
