/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.qlt.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbQltyAnalysis extends SDbRegistryUser implements java.io.Serializable {

    protected int mnPkAnalysisId;
    protected String msUnitSymbol;
    protected String msUnitName;
    protected String msAnalysysShortName;
    protected String msAnalysisName;

    /*
    protected boolean mbDeleted;
    */
    
    protected int mnFkAnalysisTypeId;
    
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SDbQltyAnalysisType moAuxAnalysisType;

    public SDbQltyAnalysis() {
        super(SModConsts.QLT_ANALYSIS);
    }

    public void setPkAnalysisId(int n) { mnPkAnalysisId = n; }
    public void setUnitSymbol(String s) { msUnitSymbol = s; }
    public void setUnitName(String s) { msUnitName = s; }
    public void setAnalysysShortName(String s) { msAnalysysShortName = s; }
    public void setAnalysisName(String s) { msAnalysisName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAnalysisTypeId(int n) { mnFkAnalysisTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAnalysisId() { return mnPkAnalysisId; }
    public String getUnitSymbol() { return msUnitSymbol; }
    public String getUnitName() { return msUnitName; }
    public String getAnalysysShortName() { return msAnalysysShortName; }
    public String getAnalysisName() { return msAnalysisName; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkAnalysisTypeId() { return mnFkAnalysisTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public SDbQltyAnalysisType getAuxAnalysisType() { return moAuxAnalysisType; }
    
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }
    
    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAnalysisId };
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAnalysisId = pk[0];
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAnalysisId = 0;
        msUnitSymbol = "";
        msUnitName = "";
        msAnalysysShortName = "";
        msAnalysisName = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkAnalysisTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moAuxAnalysisType = null;
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_analysis = " + mnPkAnalysisId ;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_analysis = " + pk[0] ;
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAnalysisId = 0;

        msSql = "SELECT COALESCE(MAX(id_analysis), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAnalysisId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().getConnection().createStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkAnalysisId = resultSet.getInt("id_analysis");
            msUnitSymbol = resultSet.getString("unit_symbol");
            msUnitName = resultSet.getString("unit_name");
            msAnalysysShortName = resultSet.getString("analysis_short_name");
            msAnalysisName = resultSet.getString("analysis_name");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAnalysisTypeId = resultSet.getInt("fk_tp_analysis_id");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
            
            moAuxAnalysisType = new SDbQltyAnalysisType();
//            moAuxAnalysisType.read(session, new int[] { mnFkAnalysisTypeId });
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
                        mnPkAnalysisId + ", " + 
                        "'" + msUnitSymbol + "', " + 
                        "'" + msUnitName + "', " + 
                        "'" + msAnalysysShortName + "', " + 
                        "'" + msAnalysisName + "', " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        (mbSystem ? 1 : 0) + ", " + 
                        mnFkAnalysisTypeId + ", " + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_analysis = " + mnPkAnalysisId + ", " +
                    "unit_symbol = '" + msUnitSymbol + "', " +
                    "unit_name = '" + msUnitName + "', " +
                    "analysis_short_name = '" + msAnalysysShortName + "', " +
                    "analysis_name = '" + msAnalysisName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
//                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_tp_analysis_id = " + mnFkAnalysisTypeId + ", " +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().getConnection().createStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbQltyAnalysis registry = new SDbQltyAnalysis();

        registry.setPkAnalysisId(this.getPkAnalysisId());
        registry.setUnitSymbol(this.getUnitSymbol());
        registry.setUnitName(this.getUnitName());
        registry.setAnalysysShortName(this.getAnalysysShortName());
        registry.setAnalysisName(this.getAnalysisName());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAnalysisTypeId(this.getFkAnalysisTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
