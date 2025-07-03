/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.qlt.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbCoAResult extends SDbRegistryUser implements java.io.Serializable {

    protected int mnPkCoAResultId;
    protected Date mtDate;
    protected String msCaptureStatus;
    protected String msExternalDocumentId;
    protected String msExternalFileId;
    protected boolean mbClosed;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    
    protected int mnFkLogTypeDeliveryId;
    protected int mnFkDatasheetTemplateId_n;
    protected int mnFkSourceDpsYearId_n;
    protected int mnFkSourceDpsDocId_n;
    protected int mnFkSourceDpsEtyId_n;
    protected int mnFkDpsYearId;
    protected int mnFkDpsDocId;
    protected int mnFkDpsEtyId;
    protected int mnFkUserCoAGenId;

    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsCoAGenerated;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected int mnAuxFkNewUserCoAGenId;
    protected SDbDatasheetTemplate moAuxDatasheetTemplate;
    protected List<SDbCoAResultRow> lCoAResultRows;

    public SDbCoAResult() {
        super(SModConsts.QLT_COA_RESULT);
    }

    public void setPkCoAResultId(int n) { mnPkCoAResultId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setCaptureStatus(String s) { msCaptureStatus = s; }
    public void setExternalDocumentId(String s) { msExternalDocumentId = s; }
    public void setExternalFileId(String s) { msExternalFileId = s; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setFkLogTypeDeliveryId(int n) { mnFkLogTypeDeliveryId = n; }
    public void setFkDatasheetTemplateId_n(int n) { mnFkDatasheetTemplateId_n = n; }
    public void setFkSourceDpsYearId_n(int n) { mnFkSourceDpsYearId_n = n; }
    public void setFkSourceDpsDocId_n(int n) { mnFkSourceDpsDocId_n = n; }
    public void setFkSourceDpsEtyId_n(int n) { mnFkSourceDpsEtyId_n = n; }
    public void setFkDpsYearId(int n) { mnFkDpsYearId = n; }
    public void setFkDpsDocId(int n) { mnFkDpsDocId = n; }
    public void setFkDpsEtyId(int n) { mnFkDpsEtyId = n; }
    public void setFkUserCoAGenId(int n) { mnFkUserCoAGenId = n; }
    public void setTsCoAGenerated(Date t) { mtTsCoAGenerated = t; }

    public void setAuxFkNewUserCoAGenId(int n) { mnAuxFkNewUserCoAGenId = n; }
    public void setAuxDatasheetTemplate(SDbDatasheetTemplate o) { moAuxDatasheetTemplate = o; }

    public int getPkCoAResultId() { return mnPkCoAResultId; }
    public Date getDate() { return mtDate; }
    public String getCaptureStatus() { return msCaptureStatus; }
    public String getExternalDocumentId() { return msExternalDocumentId; }
    public String getExternalFileId() { return msExternalFileId; }
    public boolean isClosed() { return mbClosed; }
    public int getFkLogTypeDeliveryId() { return mnFkLogTypeDeliveryId; }
    public int getFkDatasheetTemplateId_n() { return mnFkDatasheetTemplateId_n; }
    public int getFkSourceDpsYearId_n() { return mnFkSourceDpsYearId_n; }
    public int getFkSourceDpsDocId_n() { return mnFkSourceDpsDocId_n; }
    public int getFkSourceDpsEtyId_n() { return mnFkSourceDpsEtyId_n; }
    public int getFkDpsYearId() { return mnFkDpsYearId; }
    public int getFkDpsDocId() { return mnFkDpsDocId; }
    public int getFkDpsEtyId() { return mnFkDpsEtyId; }
    public int getFkUserCoAGenId() { return mnFkUserCoAGenId; }
    public Date getTsCoAGenerated() { return mtTsCoAGenerated; }

    public int getAuxFkNewUserCoAGenId() { return mnAuxFkNewUserCoAGenId; }
    public SDbDatasheetTemplate getAuxDatasheetTemplate() { return moAuxDatasheetTemplate; }
    public List<SDbCoAResultRow> getCoAResultRows() { return lCoAResultRows; }
    
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
        mtDate = null;
        msCaptureStatus = null;
        msExternalDocumentId = null;
        msExternalFileId = null;
        mbClosed = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkLogTypeDeliveryId = 0;
        mnFkDatasheetTemplateId_n = 0;
        mnFkSourceDpsYearId_n = 0;
        mnFkSourceDpsDocId_n = 0;
        mnFkSourceDpsEtyId_n = 0;
        mnFkDpsYearId = 0;
        mnFkDpsDocId = 0;
        mnFkDpsEtyId = 0;
        mnFkUserCoAGenId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsCoAGenerated = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        mnAuxFkNewUserCoAGenId = 0;
        moAuxDatasheetTemplate = null;
        lCoAResultRows = new ArrayList<>();
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_coa_result = " + mnPkCoAResultId ;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_coa_result = " + pk[0] ;
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCoAResultId = 0;

        msSql = "SELECT COALESCE(MAX(id_coa_result), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCoAResultId = resultSet.getInt(1);
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
            mtDate = resultSet.getDate("dt_date");
            msCaptureStatus = resultSet.getString("capt_status");
            msExternalDocumentId = resultSet.getString("ext_doc_id");
            msExternalFileId = resultSet.getString("ext_file_id");
            mbClosed = resultSet.getBoolean("b_closed");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkLogTypeDeliveryId = resultSet.getInt("fk_tp_log_delivery");
            mnFkDatasheetTemplateId_n = resultSet.getInt("fk_datasheet_template_n");
            mnFkSourceDpsYearId_n = resultSet.getInt("fk_dps_src_year_n");
            mnFkSourceDpsDocId_n = resultSet.getInt("fk_dps_src_doc_n");
            mnFkSourceDpsEtyId_n = resultSet.getInt("fk_dps_src_ety_n");
            mnFkDpsYearId = resultSet.getInt("fk_dps_year");
            mnFkDpsDocId = resultSet.getInt("fk_dps_doc");
            mnFkDpsEtyId = resultSet.getInt("fk_dps_ety");
            mnFkUserCoAGenId = resultSet.getInt("fk_usr_coa_gen");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsCoAGenerated = resultSet.getTimestamp("ts_coa_gen");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mnAuxFkNewUserCoAGenId = mnFkUserCoAGenId;

            mbRegistryNew = false;
//            moAuxAnalysisType.read(session, new int[] { mnFkAnalysisTypeId });

            // leer renglones:
            msSql = "SELECT * " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_COA_RESULT_ROW) + " " +
                    "WHERE id_coa_result = " + mnPkCoAResultId + " " +
                    "ORDER BY sort_pos ASC ";

            resultSet = session.getStatement().getConnection().createStatement().executeQuery(msSql);
            while (resultSet.next()) {
                SDbCoAResultRow row = new SDbCoAResultRow();
                row.setAuxFkLogTypeId_n(mnFkLogTypeDeliveryId);
                row.setAuxFkTemplateId_n(mnFkDatasheetTemplateId_n);
                row.read(session, new int[] { mnPkCoAResultId, resultSet.getInt("id_coa_result_row") });
                lCoAResultRows.add(row);
            }

            // leer plantilla:
            if (mnFkDatasheetTemplateId_n > 0) {
                moAuxDatasheetTemplate = new SDbDatasheetTemplate();
                moAuxDatasheetTemplate.read(session, new int[] { mnFkDatasheetTemplateId_n });
            }
            else {
                moAuxDatasheetTemplate = null;
            }
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
            mnFkUserCoAGenId = SUtilConsts.USR_NA_ID;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                        mnPkCoAResultId + ", " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                        "'" + msCaptureStatus + "', " +
                        "'" + ((msExternalDocumentId == null || msExternalDocumentId.isEmpty()) ? "" : msExternalDocumentId) + "', " +
                        "'" + ((msExternalFileId == null || msExternalFileId.isEmpty()) ? "" : msExternalFileId) + "', " +
                        (mbClosed ? 1 : 0) + ", " +
                        (mbDeleted ? 1 : 0) + ", " + 
                        (mbSystem ? 1 : 0) + ", " + 
                        mnFkLogTypeDeliveryId + ", " +
                        (mnFkDatasheetTemplateId_n == 0 ? "NULL" : mnFkDatasheetTemplateId_n) + ", " +
                        (mnFkSourceDpsYearId_n == 0 ? "NULL" : mnFkSourceDpsYearId_n) + ", " +
                        (mnFkSourceDpsDocId_n == 0 ? "NULL" : mnFkSourceDpsDocId_n) + ", " +
                        (mnFkSourceDpsEtyId_n == 0 ? "NULL" : mnFkSourceDpsEtyId_n) + ", " +
                        mnFkDpsYearId + ", " + 
                        mnFkDpsDocId + ", " + 
                        mnFkDpsEtyId + ", " + 
                        mnFkUserCoAGenId + ", " +
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " +                         
                        "NOW()" + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " +
                        ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_coa_result = " + mnPkCoAResultId + ", " +
                    "dt_date = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "capt_status = '" + msCaptureStatus + "', " +
                    "ext_doc_id = '" + ((msExternalDocumentId == null || msExternalDocumentId.isEmpty()) ? "" : msExternalDocumentId) + "', " +
                    "ext_file_id = '" + ((msExternalFileId == null || msExternalFileId.isEmpty()) ? "" : msExternalFileId) + "', " +
                    "b_closed = " + (mbClosed ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_tp_log_delivery = " + mnFkLogTypeDeliveryId + ", " +
                    "fk_datasheet_template_n = " + (mnFkDatasheetTemplateId_n == 0 ? "NULL" : mnFkDatasheetTemplateId_n) + ", " +
                    "fk_dps_src_year_n = " + (mnFkSourceDpsYearId_n == 0 ? "NULL" : mnFkSourceDpsYearId_n) + ", " +
                    "fk_dps_src_doc_n = " + (mnFkSourceDpsDocId_n == 0 ? "NULL" : mnFkSourceDpsDocId_n) + ", " +
                    "fk_dps_src_ety_n = " + (mnFkSourceDpsEtyId_n == 0 ? "NULL" : mnFkSourceDpsEtyId_n) + ", " +
                    "fk_dps_year = " + mnFkDpsYearId + ", " +
                    "fk_dps_doc = " + mnFkDpsDocId + ", " +
                    "fk_dps_ety = " + mnFkDpsEtyId + ", " +
                    ((mnFkUserCoAGenId != mnAuxFkNewUserCoAGenId) ? ("fk_usr_coa_gen = " + mnAuxFkNewUserCoAGenId + ", ") : ("")) +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    ((mnFkUserCoAGenId != mnAuxFkNewUserCoAGenId) ? ("ts_coa_gen = " + "NOW()" + ", ") : ("")) +
//                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().getConnection().createStatement().execute(msSql);

        // guardar renglones:
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.QLT_COA_RESULT_ROW) + " " +
                "WHERE id_coa_result = " + mnPkCoAResultId + " ";
        session.getStatement().getConnection().createStatement().execute(msSql);
        for (SDbCoAResultRow row : lCoAResultRows) {
            row.setPkCoAResultId(mnPkCoAResultId);
            row.setRegistryNew(true);
            row.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistryUser clone() throws CloneNotSupportedException {
        SDbCoAResult registry = new SDbCoAResult();

        registry.setPkCoAResultId(this.getPkCoAResultId());
        registry.setDate(this.getDate());
        registry.setCaptureStatus(this.getCaptureStatus());
        registry.setExternalDocumentId(this.getExternalDocumentId());
        registry.setExternalFileId(this.getExternalFileId());
        registry.setClosed(this.isClosed());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkLogTypeDeliveryId(this.getFkLogTypeDeliveryId());
        registry.setFkDatasheetTemplateId_n(this.getFkDatasheetTemplateId_n());
        registry.setFkSourceDpsYearId_n(this.getFkSourceDpsYearId_n());
        registry.setFkSourceDpsDocId_n(this.getFkSourceDpsDocId_n());
        registry.setFkSourceDpsEtyId_n(this.getFkSourceDpsEtyId_n());
        registry.setFkDpsYearId(this.getFkDpsYearId());
        registry.setFkDpsDocId(this.getFkDpsDocId());
        registry.setFkDpsEtyId(this.getFkDpsEtyId());
        registry.setFkUserCoAGenId(this.getFkUserCoAGenId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsCoAGenerated(this.getTsCoAGenerated());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        // clonar renglones:
        for (SDbCoAResultRow row : lCoAResultRows) {
            registry.getCoAResultRows().add((SDbCoAResultRow) row.clone());
        }
        if (moAuxDatasheetTemplate != null) {
            registry.setAuxDatasheetTemplate((SDbDatasheetTemplate) moAuxDatasheetTemplate.clone());
        }
        else {
            registry.setAuxDatasheetTemplate(null);
        }

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
