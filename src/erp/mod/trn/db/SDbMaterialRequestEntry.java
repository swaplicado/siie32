/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialRequestEntry extends SDbRegistryUser {
    
    protected int mnPkMatRequestId;
    protected int mnPkEntryId;
    protected Date mtDateRequest_n;
    protected double mdQuantity;
    protected double mdFactorConvertion;
    protected int mnCosnsumptionEstimated;
    protected boolean mbNewItem;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkMatPresentationId_n;
    protected int mnFkMatRequestPriorityId_n;
    protected int mnFkEntMatConsumptionEntityId_n;
    protected int mnFkSubentMatConsumptionEntityId_n;
    protected int mnFkSubentMatConsumptionSubentityId_n;
    
    protected ArrayList<SDbMaterialRequestEntryNote> maChildNotes;

    public SDbMaterialRequestEntry() {
        super(SModConsts.TRN_MAT_REQ_ETY);
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setDateRequest_n(Date t) { mtDateRequest_n = t; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setFactorConvertion(double d) { mdFactorConvertion = d; }
    public void setCosnsumptionEstimated(int n) { mnCosnsumptionEstimated = n; }
    public void setNewItem(boolean b) { mbNewItem = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkMatPresentationId_n(int n) { mnFkMatPresentationId_n = n; }
    public void setFkMatRequestPriorityId_n(int n) { mnFkMatRequestPriorityId_n = n; }
    public void setFkEntMatConsumptionEntityId_n(int n) { mnFkEntMatConsumptionEntityId_n = n; }
    public void setFkSubentMatConsumptionEntityId_n(int n) { mnFkSubentMatConsumptionEntityId_n = n; }
    public void setFkSubentMatConsumptionSubentityId_n(int n) { mnFkSubentMatConsumptionSubentityId_n = n; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public Date getDateRequest_n() { return mtDateRequest_n; }
    public double getQuantity() { return mdQuantity; }
    public double getFactorConvertion() { return mdFactorConvertion; }
    public int getCosnsumptionEstimated() { return mnCosnsumptionEstimated; }
    public boolean isNewItem() { return mbNewItem; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkMatPresentationId_n() { return mnFkMatPresentationId_n; }
    public int getFkMatRequestPriorityId_n() { return mnFkMatRequestPriorityId_n; }
    public int getFkEntMatConsumptionEntityId_n() { return mnFkEntMatConsumptionEntityId_n; }
    public int getFkSubentMatConsumptionEntityId_n() { return mnFkSubentMatConsumptionEntityId_n; }
    public int getFkSubentMatConsumptionSubentityId_n() { return mnFkSubentMatConsumptionSubentityId_n; }
    
    public ArrayList<SDbMaterialRequestEntryNote> getChildNotes() { return maChildNotes; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatRequestId = pk[0];
        mnPkEntryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkEntryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatRequestId = 0;
        mnPkEntryId = 0;
        mtDateRequest_n = null;
        mdQuantity = 0;
        mdFactorConvertion = 0;
        mnCosnsumptionEstimated = 0;
        mbNewItem = false;
        mbDeleted = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkMatPresentationId_n = 0;
        mnFkMatRequestPriorityId_n = 0;
        mnFkEntMatConsumptionEntityId_n = 0;
        mnFkSubentMatConsumptionEntityId_n = 0;
        mnFkSubentMatConsumptionSubentityId_n = 0;
        
        maChildNotes = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_req = " + mnPkMatRequestId + " AND id_ety = " + mnPkEntryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_req = " + pk[0] + " AND id_ety = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkEntryId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEntryId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        SDbMaterialRequestEntryNote note;
        
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
            mnPkEntryId = resultSet.getInt("id_ety");
            mtDateRequest_n = resultSet.getDate("dt_req_n");
            mdQuantity = resultSet.getDouble("qty");
            mdFactorConvertion = resultSet.getDouble("fact_conv");
            mnCosnsumptionEstimated = resultSet.getInt("cons_est");
            mbNewItem = resultSet.getBoolean("b_new_item");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkMatPresentationId_n = resultSet.getInt("fk_mat_pres");
            mnFkMatRequestPriorityId_n = resultSet.getInt("fk_mat_req_pty_n");
            mnFkEntMatConsumptionEntityId_n = resultSet.getInt("fk_ent_mat_cons_ent_n");
            mnFkSubentMatConsumptionEntityId_n = resultSet.getInt("fk_subent_mat_cons_ent_n");
            mnFkSubentMatConsumptionSubentityId_n = resultSet.getInt("fk_subent_mat_cons_subent_n");
            
            // Read aswell document notes:
            
            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_nts " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY_NTS) + " " +
                    "WHERE id_mat_req = " + mnPkMatRequestId + " AND id_ety = " + mnPkEntryId + " " +
                    "ORDER BY id_nts ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                note = new SDbMaterialRequestEntryNote();
                note.read(session, new int[] { mnPkMatRequestId, mnPkEntryId, resultSet.getInt(1) });
                maChildNotes.add(note);
            }

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
                    mnPkEntryId + ", " + 
                    (mtDateRequest_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateRequest_n) + "', ") + 
                    mdQuantity + ", " + 
                    mdFactorConvertion + ", " + 
                    mnCosnsumptionEstimated + ", " + 
                    (mbNewItem ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    (mnFkMatPresentationId_n == 0 ? "NULL, " : mnFkMatPresentationId_n + ", ") + 
                    (mnFkMatRequestPriorityId_n == 0 ? "NULL, " : mnFkMatRequestPriorityId_n + ", ") + 
                    (mnFkEntMatConsumptionEntityId_n == 0 ? "NULL, " : mnFkEntMatConsumptionEntityId_n + ", ") + 
                    (mnFkSubentMatConsumptionEntityId_n == 0 ? "NULL, " : mnFkSubentMatConsumptionEntityId_n + ", ") + 
                    (mnFkSubentMatConsumptionSubentityId_n == 0 ? "NULL " : mnFkSubentMatConsumptionSubentityId_n + " ") + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_mat_req = " + mnPkMatRequestId + ", " +
                    //"id_ety = " + mnPkEntryId + ", " +
                    "dt_req_n = " + (mtDateRequest_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateRequest_n) + "', ") +
                    "qty = " + mdQuantity + ", " +
                    "fact_conv = " + mdFactorConvertion + ", " +
                    "cons_est = " + mnCosnsumptionEstimated + ", " +
                    "b_new_item = " + (mbNewItem ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_mat_pres_n = " + (mnFkMatPresentationId_n == 0 ? "NULL, " : mnFkMatPresentationId_n + ", ") +
                    "fk_mat_req_pty_n = " + (mnFkMatRequestPriorityId_n == 0 ? "NULL, " : mnFkMatRequestPriorityId_n + ", ") +
                    "fk_ent_mat_cons_ent_n = " + (mnFkEntMatConsumptionEntityId_n == 0 ? "NULL, " : mnFkEntMatConsumptionEntityId_n + ", ") +
                    "fk_subent_mat_cons_ent_n = " + (mnFkSubentMatConsumptionEntityId_n == 0 ? "NULL, " : mnFkSubentMatConsumptionEntityId_n + ", ") +
                    "fk_subent_mat_cons_subent_n = " + (mnFkSubentMatConsumptionSubentityId_n == 0 ? "NULL " : mnFkSubentMatConsumptionSubentityId_n + " ") +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Save notes:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY_NTS) + " " + 
                "WHERE id_mat_req = " + mnPkMatRequestId + " AND id_ety = " + mnPkEntryId + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialRequestEntryNote note : maChildNotes) {
            note.setPkMatRequestId(mnPkMatRequestId);
            note.setPkEntryId(mnPkEntryId);
            note.setRegistryNew(true);
            note.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialRequestEntry clone() throws CloneNotSupportedException {
        SDbMaterialRequestEntry registry = new SDbMaterialRequestEntry();
        
        registry.setPkMatRequestId(this.getPkMatRequestId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setDateRequest_n(this.getDateRequest_n());
        registry.setQuantity(this.getQuantity());
        registry.setFactorConvertion(this.getFactorConvertion());
        registry.setCosnsumptionEstimated(this.getCosnsumptionEstimated());
        registry.setNewItem(this.isNewItem());
        registry.setDeleted(this.isDeleted());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkMatPresentationId_n(this.getFkMatPresentationId_n());
        registry.setFkMatRequestPriorityId_n(this.getFkMatRequestPriorityId_n());
        registry.setFkEntMatConsumptionEntityId_n(this.getFkEntMatConsumptionEntityId_n());
        registry.setFkSubentMatConsumptionEntityId_n(this.getFkSubentMatConsumptionEntityId_n());
        registry.setFkSubentMatConsumptionSubentityId_n(this.getFkSubentMatConsumptionSubentityId_n());
        
        for (SDbMaterialRequestEntryNote note : this.getChildNotes()) {
            registry.getChildNotes().add(note);
        }

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
    
}
