/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialRequestNote extends SDbRegistryUser {
    
    protected int mnPkMatRequestId;
    protected int mnPkNotesId;
    protected String msNotes;

    public SDbMaterialRequestNote() {
        super(SModConsts.TRN_MAT_REQ_NTS);
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkNotesId(int n) { mnPkNotesId = n; }
    public void setNotes(String s) { msNotes = s; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkNotesId() { return mnPkNotesId; }
    public String getNotes() { return msNotes; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatRequestId = pk[0];
        mnPkNotesId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkNotesId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatRequestId = 0;
        mnPkNotesId = 0;
        msNotes = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_req = " + mnPkMatRequestId + " AND id_nts = " + mnPkNotesId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_req = " + pk[0] + " AND id_nts = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkNotesId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_nts), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkNotesId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMatRequestId = resultSet.getInt("id_mat_req");
            mnPkNotesId = resultSet.getInt("id_nts");
            msNotes = resultSet.getString("nts");

            mbRegistryNew = false;
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
                    mnPkMatRequestId + ", " + 
                    mnPkNotesId + ", " + 
                    "'" + msNotes + "' " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_mat_req = " + mnPkMatRequestId + ", " +
                    //"id_nts = " + mnPkNotesId + ", " +
                    "nts = '" + msNotes + "' " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialRequestNote clone() throws CloneNotSupportedException {
        SDbMaterialRequestNote registry = new SDbMaterialRequestNote();
        
        registry.setPkMatRequestId(this.getPkMatRequestId());
        registry.setPkNotesId(this.getPkNotesId());
        registry.setNotes(this.getNotes());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
    
}
