/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

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
 * @author Sergio Flores
 */
public class SDbPreceptSubsection extends SDbRegistryUser {
    
    protected int mnPkPreceptId;
    protected int mnPkSectionId;
    protected int mnPkSubsectionId;
    protected String msCode;
    protected String msName;
    protected int mnSortingPos;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbPreceptSubsection() {
        super(SModConsts.HRS_PREC_SUBSEC);
    }
    
    public void setPkPreceptId(int n) { mnPkPreceptId = n; }
    public void setPkSectionId(int n) { mnPkSectionId = n; }
    public void setPkSubsectionId(int n) { mnPkSubsectionId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPreceptId() { return mnPkPreceptId; }
    public int getPkSectionId() { return mnPkSectionId; }
    public int getPkSubsectionId() { return mnPkSubsectionId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public int getSortingPos() { return mnSortingPos; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPreceptId = pk[0];
        mnPkSectionId = pk[1];
        mnPkSubsectionId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPreceptId, mnPkSectionId, mnPkSubsectionId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPreceptId = 0;
        mnPkSectionId = 0;
        mnPkSubsectionId = 0;
        msCode = "";
        msName = "";
        mnSortingPos = 0;
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
        return "WHERE id_prec = " + mnPkPreceptId + " "
                + "AND id_sec = " + mnPkSectionId + " "
                + "AND id_subsec = " + mnPkSubsectionId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_prec = " + pk[0] + " "
                + "AND id_sec = " + pk[1] + " "
                + "AND id_subsec = " + pk[2]+ " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPreceptId = 0;

        msSql = "SELECT COALESCE(MAX(id_subsec), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_prec = " + mnPkPreceptId + " "
                + "AND id_sec = " + mnPkSectionId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkPreceptId = resultSet.getInt(1);
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
            mnPkPreceptId = resultSet.getInt("id_prec");
            mnPkSectionId = resultSet.getInt("id_sec");
            mnPkSubsectionId = resultSet.getInt("id_subsec");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mnSortingPos = resultSet.getInt("sort");
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
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPreceptId + ", " + 
                    mnPkSectionId + ", " + 
                    mnPkSubsectionId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    mnSortingPos + ", " + 
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
                    //"id_prec = " + mnPkPreceptId + ", " +
                    //"id_sec = " + mnPkSectionId + ", " +
                    //"id_subsec = " + mnPkSubsectionId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "sort = " + mnSortingPos + ", " +
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
    public SDbPreceptSubsection clone() throws CloneNotSupportedException {
        SDbPreceptSubsection registry = new SDbPreceptSubsection();

        registry.setPkPreceptId(this.getPkPreceptId());
        registry.setPkSectionId(this.getPkSectionId());
        registry.setPkSubsectionId(this.getPkSubsectionId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setSortingPos(this.getSortingPos());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
