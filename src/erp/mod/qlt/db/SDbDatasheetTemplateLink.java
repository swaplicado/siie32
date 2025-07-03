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
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbDatasheetTemplateLink extends SDbRegistryUser implements java.io.Serializable {

    protected int mnPkDatasheetTemplateLinkId;
    protected String msConfigurationName;
    protected boolean mbValid;

    /*
    protected boolean mbDeleted;
    */
    
    protected int mnFkLinkTypeId;
    protected int mnFkReferenceId;
    protected int mnFkDatasheetTemplateId;

    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbDatasheetTemplateLink() {
        super(SModConsts.QLT_DATASHEET_TEMPLATE_LINK);
    }

    public void setPkDatasheetTemplateLinkId(int n) { mnPkDatasheetTemplateLinkId = n; }
    public void setConfigurationName(String s) { msConfigurationName = s; }
    public void setValid(boolean b) { mbValid = b; }
    public void setFkLinkTypeId(int n) { mnFkLinkTypeId = n; }
    public void setFkReferenceId(int n) { mnFkReferenceId = n; }
    public void setFkDatasheetTemplateId(int n) { mnFkDatasheetTemplateId = n; }

    public int getPkDatasheetTemplateLinkId() { return mnPkDatasheetTemplateLinkId; }
    public String getConfigurationName() { return msConfigurationName; }
    public boolean isValid() { return mbValid; }
    public int getFkLinkTypeId() { return mnFkLinkTypeId; }
    public int getFkReferenceId() { return mnFkReferenceId; }
    public int getFkDatasheetTemplateId() { return mnFkDatasheetTemplateId; }
    
    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }
    
    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDatasheetTemplateLinkId };
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDatasheetTemplateLinkId = pk[0];
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDatasheetTemplateLinkId = 0;
        msConfigurationName = "";
        mbValid = false;
        mbDeleted = false;
        mnFkLinkTypeId = 0;
        mnFkReferenceId = 0;
        mnFkDatasheetTemplateId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_datasheet_template_link = " + mnPkDatasheetTemplateLinkId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_datasheet_template_link = " + pk[0] + " ";
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
            mnPkDatasheetTemplateLinkId = resultSet.getInt("id_datasheet_template_link");
            msConfigurationName = resultSet.getString("cfg_name");
            mbValid = resultSet.getBoolean("b_valid");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkLinkTypeId = resultSet.getInt("fk_tp_link");
            mnFkReferenceId = resultSet.getInt("fk_ref");
            mnFkDatasheetTemplateId = resultSet.getInt("fk_datasheet_template");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
            
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
                        mnPkDatasheetTemplateLinkId + ", " + 
                        "'" + msConfigurationName + "', " + 
                        (mbValid ? 1 : 0) + ", " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        mnFkLinkTypeId + ", " + 
                        mnFkReferenceId + ", " + 
                        mnFkDatasheetTemplateId + ", " + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " +
                        ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_datasheet_template_link = " + mnPkDatasheetTemplateLinkId + ", " +
                    "cfg_name = '" + msConfigurationName + "', " +
                    "b_valid = " + (mbValid ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_link = " + mnFkLinkTypeId + ", " +
                    "fk_ref = " + mnFkReferenceId + ", " +
                    "fk_datasheet_template = " + mnFkDatasheetTemplateId + ", " +
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
        SDbDatasheetTemplateLink registry = new SDbDatasheetTemplateLink();

        registry.setPkDatasheetTemplateLinkId(this.getPkDatasheetTemplateLinkId());
        registry.setConfigurationName(this.getConfigurationName());
        registry.setValid(this.isValid());
        registry.setDeleted(this.isDeleted());
        registry.setFkLinkTypeId(this.getFkLinkTypeId());
        registry.setFkReferenceId(this.getFkReferenceId());
        registry.setFkDatasheetTemplateId(this.getFkDatasheetTemplateId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;
        mnPkDatasheetTemplateLinkId = 0;

        msSql = "SELECT COALESCE(MAX(id_datasheet_template_link), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDatasheetTemplateLinkId = resultSet.getInt(1);
        }
    }
}
