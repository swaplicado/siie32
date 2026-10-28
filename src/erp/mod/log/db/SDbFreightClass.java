/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbFreightClass extends SDbRegistryUser {
    
    public static final String TYPE_HIGH = "H";
    public static final String TYPE_MIDDLE = "M";
    public static final String TYPE_LOW = "L";
    
    public static final HashMap<String, String> Types = new HashMap<>();
    
    static {
        Types.put(TYPE_HIGH, "Alta");
        Types.put(TYPE_MIDDLE, "Media");
        Types.put(TYPE_LOW, "Baja");
    }

    protected int mnPkFreightClassId;
    protected String msCode;
    protected String msName;
    protected String msClassType;
    protected boolean mbDefault;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbFreightClass() {
        super(SModConsts.LOG_FREIGHT_CLASS);
    }

    public void setPkFreightClassId(int n) { mnPkFreightClassId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setClassType(String s) { msClassType = s; }
    public void setDefault(boolean b) { mbDefault = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkFreightClassId() { return mnPkFreightClassId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getClassType() { return msClassType; }
    public boolean isDefault() { return mbDefault; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkFreightClassId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkFreightClassId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkFreightClassId = 0;
        msCode = "";
        msName = "";
        msClassType = "";
        mbDefault = false;
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
        return "WHERE id_freight_class = " + mnPkFreightClassId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_freight_class = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkFreightClassId = 0;

        msSql = "SELECT COALESCE(MAX(id_freight_class), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkFreightClassId = resultSet.getInt(1);
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
            mnPkFreightClassId = resultSet.getInt("id_freight_class");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msClassType = resultSet.getString("class_tp");
            mbDefault = resultSet.getBoolean("b_def");
            mbDeleted = resultSet.getBoolean("b_del");
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
                    mnPkFreightClassId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msClassType + "', " + 
                    (mbDefault ? 1 : 0) + ", " + 
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
                    //"id_freight_class = " + mnPkFreightClassId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "class_tp = '" + msClassType + "', " +
                    "b_def = " + (mbDefault ? 1 : 0) + ", " +
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
    public SDbFreightClass clone() throws CloneNotSupportedException {
        SDbFreightClass registry = new SDbFreightClass();

        registry.setPkFreightClassId(this.getPkFreightClassId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setClassType(this.getClassType());
        registry.setDefault(this.isDefault());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
