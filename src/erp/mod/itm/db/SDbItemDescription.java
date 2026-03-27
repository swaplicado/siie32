/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.itm.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Rodrigo Ayala
 */
public class SDbItemDescription extends SDbRegistryUser {

    protected int mnPkItemId;
    protected java.lang.String msItemDescription;
    protected boolean mbIsDeleted;
    
    public SDbItemDescription() {
        super(SModConsts.ITMU_ITEM_DESC);
    }
    
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setItemDescription(java.lang.String s) { msItemDescription = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }

    public int getPkItemId() { return mnPkItemId; }
    public String getItemDescription() { return msItemDescription; }
    public boolean getIsDeleted() { return mbIsDeleted; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
                
        mnPkItemId = 0;
        msItemDescription = "";
        mbIsDeleted = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_item = " + mnPkItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_item = " + pk[0] + " ";
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkItemId = resultSet.getInt("id_item");
            msItemDescription = resultSet.getString("item_desc");
            mbIsDeleted = resultSet.getBoolean("b_del");
            
            mbRegistryNew = false;
        }
        
        mnQueryResultId = SDbConsts.READ_OK;
    }
    
    @Override
    public void computePrimaryKey(SGuiSession sgs) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(SGuiSession sgs) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry clone() throws CloneNotSupportedException {
        SDbItemDescription registry = new SDbItemDescription();

        registry.setPkItemId(this.getPkItemId());
        registry.setItemDescription(this.getItemDescription());
        registry.setIsDeleted(this.getIsDeleted());

        registry.setRegistryNew(this.isRegistryNew());

        return registry;
    }
}
