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
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbDatasheetTemplate extends SDbRegistryUser implements java.io.Serializable {

    protected int mnPkDatasheetTemplateId;
    protected Date mtDate;
    protected String msTemplateName;
    protected String msTemplateStandard;
    protected String msTemplateVersion;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkLogTypeDeliveryId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected List<SDbDatasheetTemplateRow> lDatasheetTemplateRows;

    public SDbDatasheetTemplate() {
        super(SModConsts.QLT_DATASHEET_TEMPLATE);
    }

    public void setPkDatasheetTemplateId(int n) { mnPkDatasheetTemplateId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setTemplateName(String s) { msTemplateName = s; }
    public void setTemplateStandard(String s) { msTemplateStandard = s; }
    public void setTemplateVersion(String s) { msTemplateVersion = s; }
    public void setFkLogTypeDeliveryId_n(int n) { mnFkLogTypeDeliveryId_n = n; }

    public int getPkDatasheetTemplateId() { return mnPkDatasheetTemplateId; }
    public Date getDate() { return mtDate; }
    public String getTemplateName() { return msTemplateName; }
    public String getTemplateStandard() { return msTemplateStandard; }
    public String getTemplateVersion() { return msTemplateVersion; }
    public int getFkLogTypeDeliveryId_n() { return mnFkLogTypeDeliveryId_n; }

    public List<SDbDatasheetTemplateRow> getDatasheetTemplateRows() { return lDatasheetTemplateRows; }
    
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
        mtDate = null;
        msTemplateName = "";
        msTemplateStandard = "";
        msTemplateVersion = "";
        mbDeleted = false;
        mbSystem = false;
        mnFkLogTypeDeliveryId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        lDatasheetTemplateRows = new ArrayList<>();
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_datasheet_template = " + mnPkDatasheetTemplateId ;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_datasheet_template = " + pk[0] ;
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDatasheetTemplateId = 0;

        msSql = "SELECT COALESCE(MAX(id_datasheet_template), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDatasheetTemplateId = resultSet.getInt(1);
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
            mnPkDatasheetTemplateId = resultSet.getInt("id_datasheet_template");
            mtDate = resultSet.getDate("dt_date");
            msTemplateName = resultSet.getString("template_name");
            msTemplateStandard = resultSet.getString("template_standard");
            msTemplateVersion = resultSet.getString("template_version");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkLogTypeDeliveryId_n = resultSet.getInt("fk_log_tp_dly_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
//            moAuxAnalysisType.read(session, new int[] { mnFkAnalysisTypeId });

            // leer renglones:
            msSql = "SELECT * " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE_ROW) + " " +
                    "WHERE id_datasheet_template = " + mnPkDatasheetTemplateId + " " +
                    "ORDER BY sort_pos ASC ";

            resultSet = session.getStatement().getConnection().createStatement().executeQuery(msSql);
            while (resultSet.next()) {
                SDbDatasheetTemplateRow row = new SDbDatasheetTemplateRow();
                row.read(session, new int[] { mnPkDatasheetTemplateId, resultSet.getInt("id_analysis") });
                lDatasheetTemplateRows.add(row);
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
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                        mnPkDatasheetTemplateId + ", " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                        "'" + msTemplateName + "', " + 
                        "'" + msTemplateStandard + "', " + 
                        "'" + msTemplateVersion + "', " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        (mbSystem ? 1 : 0) + ", " + 
                        (mnFkLogTypeDeliveryId_n == 0 ? "NULL" : mnFkLogTypeDeliveryId_n) + ", " +
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " +  
                        ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_datasheet_template = " + mnPkDatasheetTemplateId + ", " +
                    "dt_date = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "template_name = '" + msTemplateName + "', " +
                    "template_standard = '" + msTemplateStandard + "', " +
                    "template_version = '" + msTemplateVersion + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
//                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_log_tp_dly_n = " + (mnFkLogTypeDeliveryId_n == 0 ? "NULL" : mnFkLogTypeDeliveryId_n) + ", " +
//                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
//                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().getConnection().createStatement().execute(msSql);

        // guardar renglones:
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.QLT_DATASHEET_TEMPLATE_ROW) + " " +
                "WHERE id_datasheet_template = " + mnPkDatasheetTemplateId + " ";
        session.getStatement().getConnection().createStatement().execute(msSql);
        for (SDbDatasheetTemplateRow row : lDatasheetTemplateRows) {
            row.setPkDatasheetTemplateId(mnPkDatasheetTemplateId);
            row.setRegistryNew(true);
            row.save(session);
        }

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbDatasheetTemplate registry = new SDbDatasheetTemplate();

        registry.setPkDatasheetTemplateId(this.getPkDatasheetTemplateId());
        registry.setDate(this.getDate());
        registry.setTemplateName(this.getTemplateName());
        registry.setTemplateStandard(this.getTemplateStandard());
        registry.setTemplateVersion(this.getTemplateVersion());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkLogTypeDeliveryId_n(this.getFkLogTypeDeliveryId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        // clonar renglones:
        for (SDbDatasheetTemplateRow row : lDatasheetTemplateRows) {
            registry.getDatasheetTemplateRows().add((SDbDatasheetTemplateRow) row.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
