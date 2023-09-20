/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialCostCenterGroupUser extends SDbRegistryUser {

    protected int mnPkMatCostCenterGroup;
    protected int mnPkLinkId;
    protected int mnPkReferenceId;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    public SDbMaterialCostCenterGroupUser() {
        super(SModConsts.TRN_MAT_CC_GRP_USR);
    }
    
    public void setPkMatCostCenterGroup(int n) { mnPkMatCostCenterGroup = n; }
    public void setPkLinkId(int n) { mnPkLinkId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkMatCostCenterGroup() { return mnPkMatCostCenterGroup; }
    public int getPkLinkId() { return mnPkLinkId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatCostCenterGroup = pk[0];
        mnPkLinkId = pk[1];
        mnPkReferenceId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatCostCenterGroup, mnPkLinkId, mnPkReferenceId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatCostCenterGroup = 0;
        mnPkLinkId = 0;
        mnPkReferenceId = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_cc_grp = " + mnPkMatCostCenterGroup + " " +
                "AND id_link = " + mnPkLinkId + " " +
                "AND id_ref = " + mnPkReferenceId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_cc_grp = " + pk[0] + " " +
                "AND id_link = " + pk[1] + " " +
                "AND id_ref = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if(!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMatCostCenterGroup = resultSet.getInt("id_mat_cc_grp");
            mnPkLinkId = resultSet.getInt("id_link");
            mnPkReferenceId = resultSet.getInt("id_ref");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        mnFkUserId = session.getUser().getPkUserId();
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                    mnPkMatCostCenterGroup + ", " + 
                    mnPkLinkId + ", " + 
                    mnPkReferenceId + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_mat_cc_grp = " + mnPkMatCostCenterGroup + ", " +
                    //"id_link = " + mnPkLinkId + ", " +
                    //"id_ref = " + mnPkReferenceId + ", " +
                    //"fk_usr = " + mnFkUserId + ", " +
                    //"ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialCostCenterGroupUser clone() throws CloneNotSupportedException {
        SDbMaterialCostCenterGroupUser registry = new SDbMaterialCostCenterGroupUser();
        
        registry.setPkMatCostCenterGroup(this.getPkMatCostCenterGroup());
        registry.setPkLinkId(this.getPkLinkId());
        registry.setPkReferenceId(this.getPkReferenceId());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
}
