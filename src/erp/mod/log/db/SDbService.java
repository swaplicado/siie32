/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbService extends SDbRegistryUser {

    protected int mnPkServiceId;
    protected String msCode;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkServiceTypeId;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkCostCenterId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbService() {
        super(SModConsts.LOG_SERVICE);
    }

    public void setPkServiceId(int n) { mnPkServiceId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkServiceTypeId(int n) { mnFkServiceTypeId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkCostCenterId_n(int n) { mnFkCostCenterId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkServiceId() { return mnPkServiceId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkServiceTypeId() { return mnFkServiceTypeId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkCostCenterId_n() { return mnFkCostCenterId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public boolean isFreightOutgoing() {
        return mnFkServiceTypeId == SModSysConsts.LOGS_TP_SERVICE_FREI ||
                mnFkServiceTypeId == SModSysConsts.LOGS_TP_SERVICE_DLVY ||
                mnFkServiceTypeId == SModSysConsts.LOGS_TP_SERVICE_PICK;
    }

    public boolean isFreightIncoming() {
        return mnFkServiceTypeId == SModSysConsts.LOGS_TP_SERVICE_RETN;
    }

    public boolean canBeGenericSourceSpot() {
        return mnFkServiceTypeId == SModSysConsts.LOGS_TP_SERVICE_RETN;
    }

    public boolean canBeGenericDestinySpot() {
        return mnFkServiceTypeId == SModSysConsts.LOGS_TP_SERVICE_DLVY || mnFkServiceTypeId == SModSysConsts.LOGS_TP_SERVICE_PICK;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkServiceId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkServiceId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkServiceId = 0;
        msCode = "";
        msName = "";
        mbDeleted = false;
        mnFkServiceTypeId = 0;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkCostCenterId_n = 0;
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
        return "WHERE id_service = " + mnPkServiceId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_service = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkServiceId = 0;

        msSql = "SELECT COALESCE(MAX(id_service), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkServiceId = resultSet.getInt(1);
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
            mnPkServiceId = resultSet.getInt("id_service");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkServiceTypeId = resultSet.getInt("fk_tp_service");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkCostCenterId_n = resultSet.getInt("fk_cc_n");
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
                    mnPkServiceId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkServiceTypeId + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    mnFkCostCenterId_n + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_service = " + mnPkServiceId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_service = " + mnFkServiceTypeId + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_cc_n = " + mnFkCostCenterId_n + ", " +
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
    public SDbService clone() throws CloneNotSupportedException {
        SDbService registry = new SDbService();

        registry.setPkServiceId(this.getPkServiceId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkServiceTypeId(this.getFkServiceTypeId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkCostCenterId_n(this.getFkCostCenterId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
