/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.qlt.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbCoAResultRow extends SDbRegistryUser implements java.io.Serializable, SGridRow {

    protected int mnPkCoAResultId;
    protected int mnPkCoAResultRowId;
    protected int mnSortPosition;
    protected String msSpecification;
    protected String msAnalysisResult;
    protected String msMinValue;
    protected String msMaxValue;
    protected boolean mbMin;
    protected boolean mbMax;
    protected boolean mbForDps;
    protected boolean mbCoA;
    
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    
    protected int mnFkAnalysisId;
    
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected int mnAuxFkTemplateId_n;
    protected int mnAuxFkLogTypeId_n;
    protected SDbQltyAnalysis moAuxAnalysis;

    public SDbCoAResultRow() {
        super(SModConsts.QLT_COA_RESULT_ROW);
    }

    public void setPkCoAResultId(int n) { mnPkCoAResultId = n; }
    public void setPkCoAResultRowId(int n) { mnPkCoAResultRowId = n; }
    public void setSortPosition(int n) { mnSortPosition = n; }
    public void setSpecification(String s) { msSpecification = s; }
    public void setAnalysisResult(String s) { msAnalysisResult = s; }
    public void setMinValue(String s) { msMinValue = s; }
    public void setMaxValue(String s) { msMaxValue = s; }
    public void setMin(boolean b) { mbMin = b; }
    public void setMax(boolean b) { mbMax = b; }
    public void setForDps(boolean b) { mbForDps = b; }
    public void setCoA(boolean b) { mbCoA = b; }
    public void setFkAnalysisId(int n) { mnFkAnalysisId = n; }

    public void setAuxFkTemplateId_n(int n) { mnAuxFkTemplateId_n = n; }
    public void setAuxFkLogTypeId_n(int n) { mnAuxFkLogTypeId_n = n; }
    public void setAuxAnalysis(SDbQltyAnalysis o) { moAuxAnalysis = o; }

    public int getPkCoAResultId() { return mnPkCoAResultId; }
    public int getPkCoAResultRowId() { return mnPkCoAResultRowId; }
    public int getSortPosition() { return mnSortPosition; }
    public String getSpecification() { return msSpecification; }
    public String getAnalysisResult() { return msAnalysisResult; }
    public String getMinValue() { return msMinValue; }
    public String getMaxValue() { return msMaxValue; }
    public boolean isMin() { return mbMin; }
    public boolean isMax() { return mbMax; }
    public boolean isForDps() { return mbForDps; }
    public boolean isCoA() { return mbCoA; }
    public int getFkAnalysisId() { return mnFkAnalysisId; }

    public int getAuxFkTemplateId_n() { return mnAuxFkTemplateId_n; }
    public int getAuxFkLogTypeId_n() { return mnAuxFkLogTypeId_n; }
    public SDbQltyAnalysis getAuxAnalysis() { return moAuxAnalysis; }

    public void readAuxData(SGuiSession session) throws SQLException, Exception {
        if (moAuxAnalysis == null) {
            moAuxAnalysis = new SDbQltyAnalysis();
        }

        moAuxAnalysis.read(session, new int[] { mnFkAnalysisId });

        if (mnAuxFkTemplateId_n > 0 && mnAuxFkLogTypeId_n == 0) {
            String sql = "SELECT " +
                        "    fk_log_tp_dly_n " +
                        "FROM " +
                        "    " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE) + " " +
                        "WHERE " +
                        "    id_datasheet_template = " + mnAuxFkTemplateId_n + " ";

            ResultSet resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                mnAuxFkLogTypeId_n = resultSet.getInt("fk_log_tp_dly_n");
            }
        }
        else {
            mnAuxFkLogTypeId_n = 0;
        }
    }
    
    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }
    
    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCoAResultId };
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCoAResultId = pk[0];
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCoAResultId = 0;
        mnPkCoAResultRowId = 0;
        mnSortPosition = 0;
        msSpecification = "";
        msAnalysisResult = "";
        msMinValue = "";
        msMaxValue = "";
        mbMin = false;
        mbMax = false;
        mbForDps = false;
        mbCoA = false;
        mbDeleted = false;
        mnFkAnalysisId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        moAuxAnalysis = null;
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_coa_result = " + mnPkCoAResultId + " "
                + "AND id_coa_result_row = " + mnPkCoAResultRowId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_coa_result = " + pk[0] + " AND id_coa_result_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCoAResultRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_coa_result_row), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_coa_result = " + mnPkCoAResultId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCoAResultRowId = resultSet.getInt(1);
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
            mnPkCoAResultId = resultSet.getInt("id_coa_result");
            mnPkCoAResultRowId = resultSet.getInt("id_coa_result_row");
            mnSortPosition = resultSet.getInt("sort_pos");
            msSpecification = resultSet.getString("specification");
            msAnalysisResult = resultSet.getString("analysis_result");
            msMinValue = resultSet.getString("min_value");
            msMaxValue = resultSet.getString("max_value");
            mbMin = resultSet.getBoolean("b_min");
            mbMax = resultSet.getBoolean("b_max");
            mbForDps = resultSet.getBoolean("b_dps");
            mbCoA = resultSet.getBoolean("b_coa");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAnalysisId = resultSet.getInt("fk_analysis");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        this.readAuxData(session);

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
                        mnPkCoAResultId + ", " + 
                        mnPkCoAResultRowId + ", " + 
                        mnSortPosition + ", " + 
                        "'" + msSpecification + "', " + 
                        "'" + msAnalysisResult + "', " + 
                        "'" + msMinValue + "', " + 
                        "'" + msMaxValue + "', " + 
                        (mbMin ? 1 : 0) + ", " + 
                        (mbMax ? 1 : 0) + ", " + 
                        (mbForDps ? 1 : 0) + ", " + 
                        (mbCoA ? 1 : 0) + ", " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        mnFkAnalysisId + ", " + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_coa_result = " + mnPkCoAResultId + ", " +
//                    "id_coa_result_row = " + mnPkCoAResultRowId + ", " +
                    "sort_pos = " + mnSortPosition + ", " +
                    "specification = '" + msSpecification + "', " +
                    "analysis_result = '" + msAnalysisResult + "', " +
                    "min_value = '" + msMinValue + "', " +
                    "max_value = '" + msMaxValue + "', " +
                    "b_min = " + (mbMin ? 1 : 0) + ", " +
                    "b_max = " + (mbMax ? 1 : 0) + ", " +
                    "b_dps = " + (mbForDps ? 1 : 0) + ", " +
                    "b_coa = " + (mbCoA ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_analysis = " + mnFkAnalysisId + ", " +
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
    public SDbRegistryUser clone() throws CloneNotSupportedException {
        SDbCoAResultRow registry = new SDbCoAResultRow();

        registry.setPkCoAResultId(this.getPkCoAResultId());
        registry.setPkCoAResultRowId(this.getPkCoAResultRowId());
        registry.setSortPosition(this.getSortPosition());
        registry.setSpecification(this.getSpecification());
        registry.setAnalysisResult(this.getAnalysisResult());
        registry.setMinValue(this.getMinValue());
        registry.setMaxValue(this.getMaxValue());
        registry.setMin(this.isMin());
        registry.setMax(this.isMax());
        registry.setForDps(this.isForDps());
        registry.setCoA(this.isCoA());
        registry.setDeleted(this.isDeleted());
        registry.setFkAnalysisId(this.getFkAnalysisId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setAuxFkLogTypeId_n(this.getAuxFkLogTypeId_n());
        registry.setAuxAnalysis(this.getAuxAnalysis());
        registry.setAuxFkTemplateId_n(this.getAuxFkTemplateId_n());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkCoAResultId, mnPkCoAResultRowId };
    }

    @Override
    public String getRowCode() {
        return this.getSpecification();
    }

    @Override
    public String getRowName() {
        return this.getSpecification();
    }

    @Override
    public boolean isRowSystem() {
        return this.isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        switch (col) {
            case 0:
                return this.getSortPosition();

            case 1:
                if (moAuxAnalysis != null) {
                    return mnAuxFkLogTypeId_n == 2 ? moAuxAnalysis.getAnalysisNameEnglish() : moAuxAnalysis.getAnalysisName();
                }
                return "-";

            case 2:
                if (moAuxAnalysis != null) {
                    return mnAuxFkLogTypeId_n == 2 ? moAuxAnalysis.getMethodNameEnglish() : moAuxAnalysis.getMethodName();
                }
                return "-";

            case 3:
                return msSpecification;

            case 4:
                return msAnalysisResult;

            case 5:
                if (moAuxAnalysis != null) {
                    return mnAuxFkLogTypeId_n == 2 ? moAuxAnalysis.getUnitSymbolEnglish() : moAuxAnalysis.getUnitSymbol();
                }
                return "-";

            case 6:
                return mbCoA;

            case 7:
                return mbForDps;
                
            default:
                return null;
        }
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 4:
                msAnalysisResult = (String) value;
                break;
        }
    }
}
