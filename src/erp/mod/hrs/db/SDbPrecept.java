/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbPrecept extends SDbRegistryUser {
    
    protected int mnPkPreceptId;
    protected String msCode;
    protected String msName;
    protected Date mtDate;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkPreceptTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbPreceptSection> maChildSections;
    
    public SDbPrecept() {
        super(SModConsts.HRS_PREC);
    }
    
    public void setPkPreceptId(int n) { mnPkPreceptId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkPreceptTypeId(int n) { mnFkPreceptTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPreceptId() { return mnPkPreceptId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getDate() { return mtDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkPreceptTypeId() { return mnFkPreceptTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbPreceptSection> getChildSections() { return maChildSections; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPreceptId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPreceptId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPreceptId = 0;
        msCode = "";
        msName = "";
        mtDate = null;
        mbDeleted = false;
        mnFkPreceptTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildSections = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_prec = " + mnPkPreceptId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_prec = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPreceptId = 0;

        msSql = "SELECT COALESCE(MAX(id_prec), 0) + 1 "
                + "FROM " + getSqlTable() + " ";
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
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mtDate = resultSet.getDate("dt_prec");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkPreceptTypeId = resultSet.getInt("fk_tp_prec");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read children:
            
            Statement statement = session.getStatement().getConnection().createStatement();
                    
            msSql = "SELECT id_sec "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PREC_SEC) + " "
                    + "WHERE id_prec = " + mnPkPreceptId + " "
                    + "ORDER BY id_sec;";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbPreceptSection child = new SDbPreceptSection();
                child.read(session, new int[] { mnPkPreceptId, resultSet.getInt(1) });
                maChildSections.add(child);
            }
            
            // finish reading:

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
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkPreceptTypeId + ", " + 
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
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "dt_prec = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_prec = " + mnFkPreceptTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // save children:
        
        for (SDbPreceptSection child : maChildSections) {
            if (child.isRegistryNew() || child.isRegistryEdited()) {
                child.setPkPreceptId(mnPkPreceptId);
                child.save(session);
            }
        }
        
        // finish reading:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPrecept clone() throws CloneNotSupportedException {
        SDbPrecept registry = new SDbPrecept();

        registry.setPkPreceptId(this.getPkPreceptId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDate(this.getDate());
        registry.setDeleted(this.isDeleted());
        registry.setFkPreceptTypeId(this.getFkPreceptTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbPreceptSection child : maChildSections) {
            registry.getChildSections().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
