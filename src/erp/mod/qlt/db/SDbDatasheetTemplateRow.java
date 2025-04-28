/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.qlt.db;

import erp.data.SDataConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbDatasheetTemplateRow extends SDbRegistryUser implements java.io.Serializable, SGridRow {

    protected int mnPkDatasheetTemplateId;
    protected int mnPkAnalysisId;
    protected int mnSortPosition;
    protected String msSpecification;
    protected String msMinValue;
    protected String msMaxValue;
    protected boolean mbMin;
    protected boolean mbMax;
    protected boolean mbIsForDps;
    protected boolean mbIsForCoA;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msAnalysisName;
    protected String msAnalysisUnit;
    protected String msAnalysisType;

    public SDbDatasheetTemplateRow() {
        super(SModConsts.QLT_DATASHEET_TEMPLATE_ROW);
    }

    public void setPkDatasheetTemplateId(int n) { mnPkDatasheetTemplateId = n; }
    public void setPkAnalysisId(int n) { mnPkAnalysisId = n; }
    public void setSortPosition(int n) { mnSortPosition = n; }
    public void setSpecification(String s) { msSpecification = s; }
    public void setMinValue(String s) { msMinValue = s; }
    public void setMaxValue(String s) { msMaxValue = s; }
    public void setMin(boolean b) { mbMin = b; }
    public void setMax(boolean b) { mbMax = b; }
    public void setIsForDps(boolean b) { mbIsForDps = b; }
    public void setIsForCoA(boolean b) { mbIsForCoA = b; }
    
    public void setAuxAnalysisName(String s) { msAnalysisName = s; }
    public void setAuxAnalysisUnit(String s) { msAnalysisUnit = s; }
    public void setAuxAnalysisType(String s) { msAnalysisType = s; }

    public int getPkDatasheetTemplateId() { return mnPkDatasheetTemplateId; }
    public int getPkAnalysisId() { return mnPkAnalysisId; }
    public int getSortPosition() { return mnSortPosition; }
    public String getSpecification() { return msSpecification; }
    public String getMinValue() { return msMinValue; }
    public String getMaxValue() { return msMaxValue; }
    public boolean isMin() { return mbMin; }
    public boolean isMax() { return mbMax; }
    public boolean isForDps() { return mbIsForDps; }
    public boolean isForCoA() { return mbIsForCoA; }
    
    public String getAuxAnalysisName() { return msAnalysisName; }
    public String getAuxAnalysisUnit() { return msAnalysisUnit; }
    public String getAuxAnalysisType() { return msAnalysisType; }

    public void readAnalysisAuxData(java.sql.Statement statement) {
        String sql = "SELECT qa.unit_symbol, qa.analysis_name, qtp.name "
                + "FROM " + SDataConstants.TablesMap.get(SDataConstants.QLT_ANALYSIS) + " AS qa "
                + "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.QLT_TP_ANALYSIS) + " AS qtp "
                + "ON qa.fk_tp_analysis_id = qtp.id_tp_analysis "
                + "WHERE qa.id_analysis = " + mnPkAnalysisId;
        
        try {
            ResultSet resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (resultSet.next()) {
                msAnalysisName = resultSet.getString("analysis_name");
                msAnalysisUnit = resultSet.getString("unit_symbol");
                msAnalysisType = resultSet.getString("name");
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }
    }
    
    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }
    
    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDatasheetTemplateId };
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDatasheetTemplateId = pk[0];
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDatasheetTemplateId = 0;
        mnPkAnalysisId = 0;
        mnSortPosition = 0;
        msSpecification = "";
        msMinValue = "";
        msMaxValue = "";
        mbMin = false;
        mbMax = false;
        mbIsForDps = false;
        mbIsForCoA = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_datasheet_template = " + mnPkDatasheetTemplateId + " AND "
                + "id_analysis = " + mnPkAnalysisId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_datasheet_template = " + pk[0] + " AND "
                + "id_analysis = " + pk[1] + " ";
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
            mnPkDatasheetTemplateId = resultSet.getInt("id_datasheet_template");
            mnPkAnalysisId = resultSet.getInt("id_analysis");
            mnSortPosition = resultSet.getInt("sort_pos");
            msSpecification = resultSet.getString("specification");
            msMinValue = resultSet.getString("min_value");
            msMaxValue = resultSet.getString("max_value");
            mbMin = resultSet.getBoolean("b_min");
            mbMax = resultSet.getBoolean("b_max");
            mbIsForDps = resultSet.getBoolean("b_dps");
            mbIsForCoA = resultSet.getBoolean("b_coa");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
            
//            moAuxAnalysisType.read(session, new int[] { mnFkAnalysisTypeId });
            this.readAnalysisAuxData(session.getStatement());
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                        mnPkDatasheetTemplateId + ", " + 
                        mnPkAnalysisId + ", " + 
                        mnSortPosition + ", " + 
                        "'" + msSpecification + "', " + 
                        "'" + msMinValue + "', " + 
                        "'" + msMaxValue + "', " + 
                        (mbMin ? 1 : 0) + ", " + 
                        (mbMax ? 1 : 0) + ", " + 
                        (mbIsForDps ? 1 : 0) + ", " + 
                        (mbIsForCoA ? 1 : 0) + ", " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        (mbSystem ? 1 : 0) + ", " + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                        "id_datasheet_template = " + mnPkDatasheetTemplateId + ", " +
//                        "id_analysis = " + mnPkAnalysisId + ", " +
                        "sort_pos = " + mnSortPosition + ", " +
                        "specification = '" + msSpecification + "', " +
                        "min_value = '" + msMinValue + "', " +
                        "max_value = '" + msMaxValue + "', " +
                        "b_min = " + (mbMin ? 1 : 0) + ", " +
                        "b_max = " + (mbMax ? 1 : 0) + ", " +
                        "b_dps = " + (mbIsForDps ? 1 : 0) + ", " +
                        "b_coa = " + (mbIsForCoA ? 1 : 0) + ", " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
//                        "b_sys = " + (mbSystem ? 1 : 0) + ", " +
//                        "fk_usr_ins = " + mnFkUserInsertId + ", " +
                        "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                        "ts_usr_ins = " + "NOW()" + ", " +
                        "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().getConnection().createStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbDatasheetTemplateRow registry = new SDbDatasheetTemplateRow();

        registry.setPkDatasheetTemplateId(this.getPkDatasheetTemplateId());
        registry.setPkAnalysisId(this.getPkAnalysisId());
        registry.setSortPosition(this.getSortPosition());
        registry.setSpecification(this.getSpecification());
        registry.setMinValue(this.getMinValue());
        registry.setMaxValue(this.getMaxValue());
        registry.setMin(this.isMin());
        registry.setMax(this.isMax());
        registry.setIsForDps(this.isForDps());
        registry.setIsForCoA(this.isForCoA());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxAnalysisName(this.getAuxAnalysisName());
        registry.setAuxAnalysisUnit(this.getAuxAnalysisUnit());
        registry.setAuxAnalysisType(this.getAuxAnalysisType());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkDatasheetTemplateId, mnPkAnalysisId };
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                return mnSortPosition;
            case 1:
                return msAnalysisName;
            case 2:
                return msSpecification;
            case 3:
                return msMinValue;
            case 4:
                return msMaxValue;
            case 5:
                return mbMin;
            case 6:
                return mbMax;
            case 7:
                return mbIsForDps;
            case 8:
                return mbIsForCoA;
            default:
                return null;
        }
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 0:
                mnSortPosition = (Integer) value;
            case 1:
                msAnalysisName = (String) value;
                break;
            case 2:
                msSpecification = (String) value;
                break;
            case 3:
                msMinValue = (String) value;
                break;
            case 4:
                msMaxValue = (String) value;
                break;
            case 5:
                mbMin = (boolean) value;
                break;
            case 6:
                mbMax = (boolean) value;
                break;
            case 7:
                mbIsForDps = (boolean) value;
                break;
            case 8:
                mbIsForCoA = (boolean) value;
                break;
        }
    }
}
