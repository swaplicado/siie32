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
public class SDbQualityConfigurationRequired extends SDbRegistryUser implements java.io.Serializable {

    protected int mnPkQualityConfigurationRequiredId;

    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    
    protected int mnFkLinkTypeId;
    protected int mnFkReferenceId;

    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbQualityConfigurationRequired() {
        super(SModConsts.QLT_QLTY_CONFIG_REQUIRED);
    }

    public void setPkQualityConfigurationRequiredId(int n) { mnPkQualityConfigurationRequiredId = n; }
    public void setFkLinkTypeId(int n) { mnFkLinkTypeId = n; }
    public void setFkReferenceId(int n) { mnFkReferenceId = n; }

    public int getPkQualityConfigurationRequiredId() { return mnPkQualityConfigurationRequiredId; }
    public int getFkLinkTypeId() { return mnFkLinkTypeId; }
    public int getFkReferenceId() { return mnFkReferenceId; }
    
    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }
    
    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkQualityConfigurationRequiredId };
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkQualityConfigurationRequiredId = pk[0];
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkQualityConfigurationRequiredId = 0;
        mbDeleted = false;
        mnFkLinkTypeId = 0;
        mnFkReferenceId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_qlty_config_required = " + mnPkQualityConfigurationRequiredId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_qlty_config_required = " + pk[0] + " ";
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
            mnPkQualityConfigurationRequiredId = resultSet.getInt("id_qlty_config_required");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkLinkTypeId = resultSet.getInt("fk_tp_link");
            mnFkReferenceId = resultSet.getInt("fk_ref");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                        mnPkQualityConfigurationRequiredId + ", " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        mnFkLinkTypeId + ", " + 
                        mnFkReferenceId + ", " + 
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_qlty_config_required = " + mnPkQualityConfigurationRequiredId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_link = " + mnFkLinkTypeId + ", " +
                    "fk_ref = " + mnFkReferenceId + ", " +
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
        SDbQualityConfigurationRequired registry = new SDbQualityConfigurationRequired();

        registry.setPkQualityConfigurationRequiredId(this.getPkQualityConfigurationRequiredId());
        registry.setDeleted(this.isDeleted());
        registry.setFkLinkTypeId(this.getFkLinkTypeId());
        registry.setFkReferenceId(this.getFkReferenceId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
