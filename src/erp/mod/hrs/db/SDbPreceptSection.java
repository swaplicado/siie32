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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbPreceptSection extends SDbRegistryUser {
    
    protected int mnPkPreceptId;
    protected int mnPkSectionId;
    protected String msCode;
    protected String msName;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbPreceptSubsection> maChildSubsections;
    
    public SDbPreceptSection() {
        super(SModConsts.HRS_PREC_SEC);
    }
    
    public void setPkPreceptId(int n) { mnPkPreceptId = n; }
    public void setPkSectionId(int n) { mnPkSectionId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkPreceptId() { return mnPkPreceptId; }
    public int getPkSectionId() { return mnPkSectionId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbPreceptSubsection> getChildSubsections() { return maChildSubsections; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPreceptId = pk[0];
        mnPkSectionId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPreceptId, mnPkSectionId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPreceptId = 0;
        mnPkSectionId = 0;
        msCode = "";
        msName = null;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildSubsections = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_prec = " + mnPkPreceptId + " "
                + "AND id_sec = " + mnPkSectionId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_prec = " + pk[0] + " "
                + "AND id_sec = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPreceptId = 0;

        msSql = "SELECT COALESCE(MAX(id_sec), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_prec = " + mnPkPreceptId + " ";
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
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read children:
            
            Statement statement = session.getStatement().getConnection().createStatement();
                    
            msSql = "SELECT id_subsec "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PREC_SUBSEC) + " "
                    + "WHERE id_prec = " + mnPkPreceptId + " "
                    + "AND id_sec = " + mnPkSectionId + " "
                    + "ORDER BY id_subsec;";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbPreceptSubsection child = new SDbPreceptSubsection();
                child.read(session, new int[] { mnPkPreceptId, mnPkSectionId, resultSet.getInt(1) });
                maChildSubsections.add(child);
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
                    mnPkSectionId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
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
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // save children:
        
        for (SDbPreceptSubsection child : maChildSubsections) {
            if (child.isRegistryNew() || child.isRegistryEdited()) {
                child.setPkPreceptId(mnPkPreceptId);
                child.setPkSectionId(mnPkSectionId);
                child.save(session);
            }
        }
        
        // finish reading:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPreceptSection clone() throws CloneNotSupportedException {
        SDbPreceptSection registry = new SDbPreceptSection();

        registry.setPkPreceptId(this.getPkPreceptId());
        registry.setPkSectionId(this.getPkSectionId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbPreceptSubsection child : maChildSubsections) {
            registry.getChildSubsections().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
