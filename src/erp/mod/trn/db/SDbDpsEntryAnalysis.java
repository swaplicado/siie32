package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;

import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

public class SDbDpsEntryAnalysis extends SDbRegistryUser {

    protected int mnPkEntryAnalysisId;
    protected int mnSortPos;
    protected String msOriginalSpecification;
    protected String msSpecification;
    protected String msOriginalMinValue;
    protected String msOriginalMaxValue;
    protected String msMinValue;
    protected String msMaxValue;
    protected boolean mbMin;
    protected boolean mbMax;
    protected boolean mbRequired;
    protected boolean mbIsForDps;
    protected boolean msIsForCoA;
    protected boolean mbWasAdded;
    protected boolean mbIsDeleted;
    protected int mnFkDpsYearId;
    protected int mnFkDpsDocId;
    protected int mnFkDpsEntryId;
    protected int mnFkAnalysisId;
    protected int mnFkItemId;
    protected int mnFkDatasheetTemplateId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected String msAuxAnalysisName;
    protected String msAuxAnalysisUnit;
    protected String msAuxAnalysisType;

    public SDbDpsEntryAnalysis() {
        super(SModConsts.TRN_DPS_ETY_ANALYSIS);
    }

    /**
     * Public methods
     */
    public void setPkEntryAnalysisId(int n) { mnPkEntryAnalysisId = n; }
    public void setSortPos(int n) { mnSortPos = n; }
    public void setOriginalSpecification(String s) { msOriginalSpecification = s; }
    public void setSpecification(String s) { msSpecification = s; }
    public void setOriginalMinValue(String s) { msOriginalMinValue = s; }
    public void setOriginalMaxValue(String s) { msOriginalMaxValue = s; }
    public void setMinValue(String s) { msMinValue = s; }
    public void setMaxValue(String s) { msMaxValue = s; }
    public void setMin(boolean b) { mbMin = b; }
    public void setMax(boolean b) { mbMax = b; }
    public void setRequired(boolean b) { mbRequired = b; }
    public void setIsForDps(boolean b) { mbIsForDps = b; }
    public void setIsForCoA(boolean b) { msIsForCoA = b; }
    public void setWasAdded(boolean b) { mbWasAdded = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDpsYearId(int n) { mnFkDpsYearId = n; }
    public void setFkDpsDocId(int n) { mnFkDpsDocId = n; }
    public void setFkDpsEntryId(int n) { mnFkDpsEntryId = n; }
    public void setFkAnalysisId(int n) { mnFkAnalysisId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkDatasheetTemplateId_n(int n) { mnFkDatasheetTemplateId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public void setAuxAnalysisName(String s) { msAuxAnalysisName = s; }
    public void setAuxAnalysisUnit(String s) { msAuxAnalysisUnit = s; }
    public void setAuxAnalysisType(String s) { msAuxAnalysisType = s; }

    public int getPkEntryAnalysisId() { return mnPkEntryAnalysisId; }
    public int getSortPos() { return mnSortPos; }
    public String getOriginalSpecification() { return msOriginalSpecification; }
    public String getSpecification() { return msSpecification; }
    public String getOriginalMinValue() { return msOriginalMinValue; }
    public String getOriginalMaxValue() { return msOriginalMaxValue; }
    public String getMinValue() { return msMinValue; }
    public String getMaxValue() { return msMaxValue; }
    public boolean isMin() { return mbMin; }
    public boolean isMax() { return mbMax; }
    public boolean isRequired() { return mbRequired; }
    public boolean isForDps() { return mbIsForDps; }
    public boolean isForCoA() { return msIsForCoA; }
    public boolean wasAdded() { return mbWasAdded; }
    public boolean isDeleted() { return mbIsDeleted; }
    public int getFkDpsYearId() { return mnFkDpsYearId; }
    public int getFkDpsDocId() { return mnFkDpsDocId; }
    public int getFkDpsEntryId() { return mnFkDpsEntryId; }
    public int getFkAnalysisId() { return mnFkAnalysisId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkDatasheetTemplateId_n() { return mnFkDatasheetTemplateId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public String getAuxAnalysisName() { return msAuxAnalysisName; }
    public String getAuxAnalysisUnit() { return msAuxAnalysisUnit; }
    public String getAuxAnalysisType() { return msAuxAnalysisType; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEntryAnalysisId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEntryAnalysisId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEntryAnalysisId = 0;
        mnSortPos = 0;
        msOriginalSpecification = "";
        msSpecification = "";
        msOriginalMinValue = "";
        msOriginalMaxValue = "";
        msMinValue = "";
        msMaxValue = "";
        mbMin = false;
        mbMax = false;
        mbRequired = false;
        mbIsForDps = false;
        msIsForCoA = false;
        mbWasAdded = false;
        mbIsDeleted = false;
        mnFkDpsYearId = 0;
        mnFkDpsDocId = 0;
        mnFkDpsEntryId = 0;
        mnFkAnalysisId = 0;
        mnFkItemId = 0;
        mnFkDatasheetTemplateId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msAuxAnalysisName = "";
        msAuxAnalysisUnit = "";
        msAuxAnalysisType = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY_ANALYSIS);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ety_analysis = " + mnPkEntryAnalysisId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ety_analysis = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEntryAnalysisId = 0;

        msSql = "SELECT COALESCE(MAX(id_ety_analysis), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEntryAnalysisId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();

        msSql = "SELECT * FROM " + getSqlTable() + " WHERE id_ety_analysis = " + pk[0];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkEntryAnalysisId = resultSet.getInt("id_ety_analysis");
            mnSortPos = resultSet.getInt("sort_pos");
            msOriginalSpecification = resultSet.getString("orig_specification");
            msSpecification = resultSet.getString("specification");
            msOriginalMinValue = resultSet.getString("orig_min_value");
            msOriginalMaxValue = resultSet.getString("orig_max_value");
            msMinValue = resultSet.getString("min_value");
            msMaxValue = resultSet.getString("max_value");
            mbMin = resultSet.getBoolean("b_min");
            mbMax = resultSet.getBoolean("b_max");
            mbRequired = resultSet.getBoolean("b_required");
            mbIsForDps = resultSet.getBoolean("b_dps");
            msIsForCoA = resultSet.getBoolean("b_coa");
            mbWasAdded = resultSet.getBoolean("b_added");
            mbIsDeleted = resultSet.getBoolean("b_del");
            mnFkDpsYearId = resultSet.getInt("fid_dps_year");
            mnFkDpsDocId = resultSet.getInt("fid_dps_doc");
            mnFkDpsEntryId = resultSet.getInt("fid_dps_ety");
            mnFkAnalysisId = resultSet.getInt("fid_analysis");
            mnFkItemId = resultSet.getInt("fid_item");
            mnFkDatasheetTemplateId_n = resultSet.getInt("fid_datasheet_template_n");
            mnFkUserNewId = resultSet.getInt("fid_usr_new");
            mnFkUserEditId = resultSet.getInt("fid_usr_edit");
            mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
            mtUserNewTs = resultSet.getTimestamp("ts_new");
            mtUserEditTs = resultSet.getTimestamp("ts_edit");
            mtUserDeleteTs = resultSet.getTimestamp("ts_del");

            mbRegistryNew = false;
        }
        
        msSql = "SELECT qa.unit_symbol, qa.analysis_name, qtp.name "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_ANALYSIS) + " AS qa "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.QLT_TP_ANALYSIS) + " AS qtp "
                    + "ON qa.fk_tp_analysis_id = qtp.id_tp_analysis "
                    + "WHERE qa.id_analysis = " + mnFkAnalysisId;
            
        ResultSet resultSetAux = session.getStatement().getConnection().createStatement().executeQuery(msSql);
        if (resultSetAux.next()) {
            msAuxAnalysisName = resultSetAux.getString("analysis_name");
            msAuxAnalysisUnit = resultSetAux.getString("unit_symbol");
            msAuxAnalysisType = resultSetAux.getString("name");
        }

        resultSet.close();
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
            mnFkUserDeleteId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEntryAnalysisId + ", " +
                    mnSortPos + ", " +
                    "'" + msOriginalSpecification + "', " +
                    "'" + msSpecification + "', " +
                    "'" + msOriginalMinValue + "', " +
                    "'" + msOriginalMaxValue + "', " +
                    "'" + msMinValue + "', " +
                    "'" + msMaxValue + "', " +
                    (mbMin ? 1 : 0) + ", " +
                    (mbMax ? 1 : 0) + ", " +
                    (mbRequired ? 1 : 0) + ", " +
                    (mbIsForDps ? 1 : 0) + ", " +
                    (msIsForCoA ? 1 : 0) + ", " +
                    (mbWasAdded ? 1 : 0) + ", " +
                    (mbIsDeleted ? 1 : 0) + ", " +
                    mnFkDpsYearId + ", " +
                    mnFkDpsDocId + ", " +
                    mnFkDpsEntryId + ", " +
                    mnFkAnalysisId + ", " +
                    mnFkItemId + ", " +
                    (mnFkDatasheetTemplateId_n == 0 ? "NULL" : mnFkDatasheetTemplateId_n) + ", " +
                    mnFkUserNewId + ", " +
                    mnFkUserEditId + ", " +
                    mnFkUserDeleteId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "sort_pos = " + mnSortPos + ", " +
                    "orig_specification = '" + msOriginalSpecification + "', " +
                    "specification = '" + msSpecification + "', " +
                    "orig_min_value = '" + msOriginalMinValue + "', " +
                    "orig_max_value = '" + msOriginalMaxValue + "', " +
                    "min_value = '" + msMinValue + "', " +
                    "max_value = '" + msMaxValue + "', " +
                    "b_min = " + (mbMin ? 1 : 0) + ", " +
                    "b_max = " + (mbMax ? 1 : 0) + ", " +
                    "b_required = " + (mbRequired ? 1 : 0) + ", " +
                    "b_dps = " + (mbIsForDps ? 1 : 0) + ", " +
                    "b_coa = " + (msIsForCoA ? 1 : 0) + ", " +
                    "b_added = " + (mbWasAdded ? 1 : 0) + ", " +
                    "b_del = " + (mbIsDeleted ? 1 : 0) + ", " +
                    // "fid_dps_year_n = " + mnFkDpsYearId_n + ", " +
                    // "fid_dps_doc_n = " + mnFkDpsDocId_n + ", " +
                    // "fid_dps_ety_n = " + mnFkDpsEntryId_n + ", " +
                    "fid_analysis_id = " + mnFkAnalysisId + ", " +
                    "fid_item_id = " + mnFkItemId + ", " +
                    "fid_datasheet_template_n = " + (mnFkDatasheetTemplateId_n == 0 ? "NULL" : mnFkDatasheetTemplateId_n) + ", " +
                    "fid_usr_edit = " + mnFkUserEditId + ", " +
                    "fid_usr_del = " + mnFkUserDeleteId + ", " +
                    "ts_edit = " + "NOW()" + ", " +
                    "ts_del = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbDpsEntryAnalysis registry = new SDbDpsEntryAnalysis();
        
        registry.setPkEntryAnalysisId(this.getPkEntryAnalysisId());
        registry.setSortPos(this.getSortPos());
        registry.setOriginalSpecification(this.getOriginalSpecification());
        registry.setSpecification(this.getSpecification());
        registry.setOriginalMinValue(this.getOriginalMinValue());
        registry.setOriginalMaxValue(this.getOriginalMaxValue());
        registry.setMinValue(this.getMinValue());
        registry.setMaxValue(this.getMaxValue());
        registry.setMin(this.isMin());
        registry.setMax(this.isMax());
        registry.setRequired(this.isRequired());
        registry.setIsForDps(this.isForDps());
        registry.setIsForCoA(this.isForCoA());
        registry.setWasAdded(this.wasAdded());
        registry.setIsDeleted(this.isDeleted());
        registry.setFkDpsYearId(this.getFkDpsYearId());
        registry.setFkDpsDocId(this.getFkDpsDocId());
        registry.setFkDpsEntryId(this.getFkDpsEntryId());
        registry.setFkAnalysisId(this.getFkAnalysisId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkDatasheetTemplateId_n(this.getFkDatasheetTemplateId_n());
        registry.setFkUserNewId(this.getFkUserNewId());
        registry.setFkUserEditId(this.getFkUserEditId());
        registry.setFkUserDeleteId(this.getFkUserDeleteId());
        registry.setUserNewTs(this.getUserNewTs());
        registry.setUserEditTs(this.getUserEditTs());
        registry.setUserDeleteTs(this.getUserDeleteTs());
        registry.setRegistryNew(this.isRegistryNew());
        registry.setUserEditTs(this.getUserEditTs());
        registry.setUserDeleteTs(this.getUserDeleteTs());
        registry.setUserNewTs(this.getUserNewTs());
        registry.setUserEditTs(this.getUserEditTs());

        return registry;
    }
}
