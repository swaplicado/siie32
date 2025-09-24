/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class SDbInitiative extends SDbRegistryUser {
    
    public static final int ID_TYPE_E = 1; // per Event
    public static final int ID_TYPE_R = 2; // Recurrent
    
    public static final String TYPE_E = "E"; // per Event
    public static final String TYPE_R = "R"; // Recurrent
    
    public static final String TYPE_PER_EVENT = "Por evento"; // per Event
    public static final String TYPE_RECURRENT = "Recurrente"; // Recurrent
    
    public static HashMap<String, Integer> TypeIdsMap = new HashMap<>();
    
    static {
        TypeIdsMap.put(TYPE_E, ID_TYPE_E);
        TypeIdsMap.put(TYPE_R, ID_TYPE_R);
    }
    
    protected int mnPkInitiativeId;
    protected String msCode;
    protected String msName;
    protected String msPurpose;
    protected String msGoals;
    protected String msDescription;
    protected String msType;
    protected double mdBudget;
    protected Date mtDateStart_n;
    protected Date mtDateEnd_n;
    //protected boolean mbDeleted;
    protected int mnFkPeriodicityTypeId;
    protected int mnFkFunctionalAreaId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msDbmsType;
    protected String msDbmsPeriodicityType;
    protected String msDbmsFuncArea;

    public SDbInitiative() {
        super(SModConsts.TRN_INIT);
    }
    
    public void setPkInitiativeId(int n) { mnPkInitiativeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setPurpose(String s) { msPurpose = s; }
    public void setGoals(String s) { msGoals = s; }
    public void setDescription(String s) { msDescription = s; }
    public void setType(String s) { msType = s; }
    public void setBudget(double d) { mdBudget = d; }
    public void setDateStart_n(Date t) { mtDateStart_n = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkPeriodicityTypeId(int n) { mnFkPeriodicityTypeId = n; }
    public void setFkFunctionalAreaId(int n) { mnFkFunctionalAreaId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setDbmsType(String s) { msDbmsType = s; }
    public void setDbmsPeriodicityType(String s) { msDbmsPeriodicityType = s; }
    public void setDbmsFuncArea(String s) { msDbmsFuncArea = s; }

    public int getPkInitiativeId() { return mnPkInitiativeId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getPurpose() { return msPurpose; }
    public String getGoals() { return msGoals; }
    public String getDescription() { return msDescription; }
    public String getType() { return msType; }
    public double getBudget() { return mdBudget; }
    public Date getDateStart_n() { return mtDateStart_n; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkPeriodicityTypeId() { return mnFkPeriodicityTypeId; }
    public int getFkFunctionalAreaId() { return mnFkFunctionalAreaId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public String getDbmsType() { return msDbmsType; }
    public String getDbmsPeriodicityType() { return msDbmsPeriodicityType; }
    public String getDbmsFuncArea() { return msDbmsFuncArea; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkInitiativeId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkInitiativeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkInitiativeId = 0;
        msCode = "";
        msName = "";
        msPurpose = null;
        msGoals = null;
        msDescription = null;
        msType = "";
        mdBudget = 0;
        mtDateStart_n = null;
        mtDateEnd_n = null;
        mbDeleted = false;
        mnFkPeriodicityTypeId = 0;
        mnFkFunctionalAreaId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msDbmsType = "";
        msDbmsPeriodicityType = "";
        msDbmsFuncArea = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_init = " + mnPkInitiativeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_init = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkInitiativeId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_init), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkInitiativeId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT i.*, "
                + "CASE WHEN i.type = '" + TYPE_E + "' THEN '" + TYPE_PER_EVENT + "' WHEN i.type = '" + TYPE_R + "' THEN '" + TYPE_RECURRENT + "' ELSE '?' END AS _type, "
                + "tp.name AS _periodicity_type, f.name AS _func "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INIT) + " AS i "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_PERIOD) + " AS tp ON i.fk_tp_period = tp.id_tp_period "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_FUNC) + " AS f ON i.fk_func = f.id_func " +
                getSqlWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkInitiativeId = resultSet.getInt("id_init");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msPurpose = resultSet.getString("purpose");
            msGoals = resultSet.getString("goals");
            msDescription = resultSet.getString("description");
            msType = resultSet.getString("type");
            mdBudget = resultSet.getDouble("budget");
            mtDateStart_n = resultSet.getDate("dt_sta_n");
            mtDateEnd_n = resultSet.getDate("dt_end_n");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkPeriodicityTypeId = resultSet.getInt("fk_tp_period");
            mnFkFunctionalAreaId = resultSet.getInt("fk_func");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            msDbmsType = resultSet.getString("_type");
            msDbmsPeriodicityType = resultSet.getString("_periodicity_type");
            msDbmsFuncArea = resultSet.getString("_func");

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
                    mnPkInitiativeId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msPurpose + "', " + 
                    "'" + msGoals + "', " + 
                    "'" + msDescription + "', " + 
                    "'" + msType + "', " + 
                    mdBudget + ", " + 
                    (mtDateStart_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart_n) + "', ") + 
                    (mtDateEnd_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "', ") + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkPeriodicityTypeId + ", " + 
                    mnFkFunctionalAreaId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_init = " + mnPkInitiativeId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "purpose = '" + msPurpose + "', " +
                    "goals = '" + msGoals + "', " +
                    "description = '" + msDescription + "', " +
                    "type = '" + msType + "', " +
                    "budget = " + mdBudget + ", " +
                    "dt_sta_n = " + (mtDateStart_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart_n) + "', ") +
                    "dt_end_n = " + (mtDateEnd_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "', ") +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_period = " + mnFkPeriodicityTypeId + ", " +
                    "fk_func = " + mnFkFunctionalAreaId + ", " +
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
    public SDbInitiative clone() throws CloneNotSupportedException {
        SDbInitiative registry = new SDbInitiative();
        
        registry.setPkInitiativeId(this.getPkInitiativeId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setPurpose(this.getPurpose());
        registry.setGoals(this.getGoals());
        registry.setDescription(this.getDescription());
        registry.setType(this.getType());
        registry.setBudget(this.getBudget());
        registry.setDateStart_n(this.getDateStart_n());
        registry.setDateEnd_n(this.getDateEnd_n());
        registry.setDeleted(this.isDeleted());
        registry.setFkPeriodicityTypeId(this.getFkPeriodicityTypeId());
        registry.setFkFunctionalAreaId(this.getFkFunctionalAreaId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setDbmsType(this.getDbmsType());
        registry.setDbmsPeriodicityType(this.getDbmsPeriodicityType());
        registry.setDbmsFuncArea(this.getDbmsFuncArea());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
    
    public static ArrayList<SGuiItem> createTypesGuiItem() {
        ArrayList<SGuiItem> guiItems = new ArrayList<>();
        
        guiItems.add(new SGuiItem(new int[] { ID_TYPE_E }, TYPE_PER_EVENT, TYPE_E ));
        guiItems.add(new SGuiItem(new int[] { ID_TYPE_R }, TYPE_RECURRENT, TYPE_R ));
        
        return guiItems;
    }
}
