/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbMaterialRequestEntry extends SDbRegistryUser implements SGridRow, Serializable {
    
    protected int mnPkMatRequestId;
    protected int mnPkEntryId;
    protected Date mtDateRequest_n;
    protected Date mtDateDelivery_n;
    protected double mdQuantity;
    protected double mdPriceUnitarySystem;
    protected double mdPriceUnitary;
    protected double mdUserQuantity;
    protected double mdUserPriceUnitary;
    protected String msUserPriceUnitaryReference;
    protected double mdTotal_r;
    protected int mnCosnsumptionEstimated;
    protected boolean mbNewItem;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkUserUnitId;
    protected int mnFkMatRequestPriorityId_n;
    protected int mnFkEntMatConsumptionEntityId_n;
    protected int mnFkSubentMatConsumptionEntityId_n;
    protected int mnFkSubentMatConsumptionSubentityId_n;
    protected int mnFkCostCenterId_n;
    protected int mnFkItemReferenceId_n;
    
    protected ArrayList<SDbMaterialRequestEntryNote> maChildNotes;
    protected ArrayList<SDbMaterialRequestEntryItemChange> maChildItemChange;
    
    protected int mnAuxRowId;
    
    protected SDataItem moDataItem;
    protected SDataItem moDataItemRef;
    protected SDataUnit moDataUnitUsr;
    protected SDbMaterialConsumptionEntity moDbmsMatConsEntity;
    protected SDbMaterialConsumptionSubentity moDbmsMatConsSubentity;
    protected SDbMaterialRequestPriority moDbmsMatReqPty;

    public SDbMaterialRequestEntry() {
        super(SModConsts.TRN_MAT_REQ_ETY);
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setDateRequest_n(Date t) { mtDateRequest_n = t; }
    public void setDateDelivery_n(Date t) { mtDateDelivery_n = t; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setPriceUnitarySystem(double d) { mdPriceUnitarySystem = d; }
    public void setPriceUnitary(double d) { mdPriceUnitary = d; }
    public void setUserQuantity(double d) { mdUserQuantity = d; }
    public void setUserPriceUnitary(double d) { mdUserPriceUnitary = d; }
    public void setUserPriceUnitaryReference(String s) { msUserPriceUnitaryReference = s; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setCosnsumptionEstimated(int n) { mnCosnsumptionEstimated = n; }
    public void setNewItem(boolean b) { mbNewItem = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkUserUnitId(int n) { mnFkUserUnitId = n; }
    public void setFkMatRequestPriorityId_n(int n) { mnFkMatRequestPriorityId_n = n; }
    public void setFkEntMatConsumptionEntityId_n(int n) { mnFkEntMatConsumptionEntityId_n = n; }
    public void setFkSubentMatConsumptionEntityId_n(int n) { mnFkSubentMatConsumptionEntityId_n = n; }
    public void setFkSubentMatConsumptionSubentityId_n(int n) { mnFkSubentMatConsumptionSubentityId_n = n; }
    public void setFkCostCenterId_n(int n) { mnFkCostCenterId_n = n; }
    public void setFkItemReferenceId_n(int n) { mnFkItemReferenceId_n = n; }
    
    public void setDataItem(SDataItem o) { moDataItem = o; }
    public void setDataItemRef(SDataItem o) { moDataItemRef = o; }
    public void setDataUnitUsr(SDataUnit o) { moDataUnitUsr = o; }
    
    public void setAuxRowId(int n) { mnAuxRowId = n; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public Date getDateRequest_n() { return mtDateRequest_n; }
    public Date getDateDelivery_n() { return mtDateDelivery_n; }
    public double getQuantity() { return mdQuantity; }
    public double getPriceUnitarySystem() { return mdPriceUnitarySystem; }
    public double getPriceUnitary() { return mdPriceUnitary; }
    public double getUserQuantity() { return mdUserQuantity; }
    public double getUserPriceUnitary() { return mdUserPriceUnitary; }
    public String getUserPriceUnitaryReference() { return msUserPriceUnitaryReference; }
    public double getTotal_r() { return mdTotal_r; }
    public int getCosnsumptionEstimated() { return mnCosnsumptionEstimated; }
    public boolean isNewItem() { return mbNewItem; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkUserUnitId() { return mnFkUserUnitId; }
    public int getFkMatRequestPriorityId_n() { return mnFkMatRequestPriorityId_n; }
    public int getFkEntMatConsumptionEntityId_n() { return mnFkEntMatConsumptionEntityId_n; }
    public int getFkSubentMatConsumptionEntityId_n() { return mnFkSubentMatConsumptionEntityId_n; }
    public int getFkSubentMatConsumptionSubentityId_n() { return mnFkSubentMatConsumptionSubentityId_n; }
    public int getFkCostCenterId_n() { return mnFkCostCenterId_n; }
    public int getFkItemReferenceId_n() { return mnFkItemReferenceId_n; }
    
    public ArrayList<SDbMaterialRequestEntryNote> getChildNotes() { return maChildNotes; }
    public ArrayList<SDbMaterialRequestEntryItemChange> getChildItemChange() { return maChildItemChange; }
    
    public int getAuxRowId() { return mnAuxRowId; }
    public SDataItem getDataItem() { return moDataItem; }
    public SDataItem getDataItemRef() { return moDataItemRef; }
    public SDataUnit getDataUnitUsr() { return moDataUnitUsr; }
    
    public String getItemNewDescription() {
        for (SDbMaterialRequestEntryNote note : maChildNotes) {
            if (note.getIsDescription()) {
                return note.getNotes();
            }
        }
        return "";
    }
    
    /**
     * Obtiene las configuraciones de sub entidad de consumo vs centros de costo
     * 
     * @return lista de configuraciones
     */
    public ArrayList<SMatConsumeSubEntCcConfig> getCcConfigs() {
        ArrayList<SMatConsumeSubEntCcConfig> lConfigs = new ArrayList<>();
        
        if (mnFkSubentMatConsumptionEntityId_n > 0 && mnFkSubentMatConsumptionSubentityId_n > 0) {
            SMatConsumeSubEntCcConfig oConfig = new SMatConsumeSubEntCcConfig();
            
            oConfig.setFkMaterialRequestId(mnPkMatRequestId);
            oConfig.setFkMaterialRequestEntryId(mnPkEntryId);
            oConfig.setFkSubentMatConsumptionEntityId(mnFkSubentMatConsumptionEntityId_n);
            oConfig.setFkSubentMatConsumptionSubentityId(mnFkSubentMatConsumptionSubentityId_n);
            oConfig.setFkCostCenterId(mnFkCostCenterId_n);

            lConfigs.add(oConfig);
        }
        
        return lConfigs;
    }
    
    public String getConsumptionInfo() {
        String description = "";
        if (moDbmsMatConsEntity != null && moDbmsMatConsSubentity != null) {
            description = moDbmsMatConsEntity.getName() + " - " + moDbmsMatConsSubentity.getName();
        }
        
        return description;
    }
    
    public String getConsumptionEntityInfo() {
        String description = "";
        if (moDbmsMatConsEntity != null) {
            description = moDbmsMatConsEntity.getName();
        }
        
        return description;
    }
    
    public String getConsumptionSubentityInfo() {
        String description = "";
        if (moDbmsMatConsSubentity != null) {
            description = moDbmsMatConsSubentity.getName();
        }
        
        return description;
    }

    public void readOptionalInfo(SGuiSession session) throws Exception {
        moDbmsMatConsEntity = null;
        moDbmsMatConsSubentity = null;
        moDbmsMatReqPty = null;
        
        if (mnFkEntMatConsumptionEntityId_n != 0) {
            moDbmsMatConsEntity = new SDbMaterialConsumptionEntity();
            moDbmsMatConsEntity.read(session, new int[] { mnFkEntMatConsumptionEntityId_n });
        }
        if (mnFkSubentMatConsumptionEntityId_n != 0) {
            moDbmsMatConsSubentity = new SDbMaterialConsumptionSubentity();
            moDbmsMatConsSubentity.read(session, new int[] { mnFkSubentMatConsumptionEntityId_n, mnFkSubentMatConsumptionSubentityId_n });
        }
        if (mnFkMatRequestPriorityId_n != 0) {
            moDbmsMatReqPty = new SDbMaterialRequestPriority();
            moDbmsMatReqPty.read(session, new int[] { mnFkMatRequestPriorityId_n });
        }
    }
            
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
        mtDateDelivery_n = null;
        mdQuantity = 0;
        mdPriceUnitarySystem = 0;
        mdPriceUnitary = 0;
        mdUserQuantity = 0;
        mdUserPriceUnitary = 0;
        msUserPriceUnitaryReference = "";
        mdTotal_r = 0;
        mnCosnsumptionEstimated = 0;
        mbNewItem = false;
        mbDeleted = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkUserUnitId = 0;
        mnFkMatRequestPriorityId_n = 0;
        mnFkEntMatConsumptionEntityId_n = 0;
        mnFkSubentMatConsumptionEntityId_n = 0;
        mnFkSubentMatConsumptionSubentityId_n = 0;
        mnFkCostCenterId_n = 0;
        mnFkItemReferenceId_n = 0;
        
        maChildNotes = new ArrayList<>();
        maChildItemChange = new ArrayList<>();
        
        mnAuxRowId = 0;
        moDataItem = null;
        moDataItemRef = null;
        moDataUnitUsr = null;
        moDbmsMatConsEntity = null;
        moDbmsMatConsSubentity = null;
        moDbmsMatReqPty = null;
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
        
        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " WHERE id_mat_req = " + mnPkMatRequestId + " ";
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
        SDbMaterialRequestEntryItemChange change;
                
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
            mtDateDelivery_n = resultSet.getDate("dt_delivery_n");
            mdQuantity = resultSet.getDouble("qty");
            mdPriceUnitarySystem = resultSet.getDouble("price_u_sys");
            mdPriceUnitary = resultSet.getDouble("price_u");
            mdUserQuantity = resultSet.getDouble("user_qty");
            mdUserPriceUnitary = resultSet.getDouble("user_price_u");
            msUserPriceUnitaryReference = resultSet.getString("user_price_u_ref");
            mdTotal_r = resultSet.getDouble("tot_r");
            mnCosnsumptionEstimated = resultSet.getInt("cons_est");
            mbNewItem = resultSet.getBoolean("b_new_item");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkUserUnitId = resultSet.getInt("fk_user_unit");
            mnFkMatRequestPriorityId_n = resultSet.getInt("fk_mat_req_pty_n");
            mnFkEntMatConsumptionEntityId_n = resultSet.getInt("fk_ent_mat_cons_ent_n");
            mnFkSubentMatConsumptionEntityId_n = resultSet.getInt("fk_subent_mat_cons_ent_n");
            mnFkSubentMatConsumptionSubentityId_n = resultSet.getInt("fk_subent_mat_cons_subent_n");
            mnFkCostCenterId_n = resultSet.getInt("fk_cc_n");
            mnFkItemReferenceId_n = resultSet.getInt("fk_item_ref_n");
            
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
            
            msSql = "SELECT id_chg " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY_ITEM_CHG) + " " +
                    "WHERE id_mat_req = " + mnPkMatRequestId + " AND id_ety = " + mnPkEntryId + " " +
                    "ORDER BY id_chg ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                change = new SDbMaterialRequestEntryItemChange();
                change.read(session, new int[] { mnPkMatRequestId, mnPkEntryId, resultSet.getInt(1) });
                maChildItemChange.add(change);
            }
            
            // Read ítem:
            
            if (mnFkItemId != 0) {
                moDataItem = new SDataItem();
                moDataItem.read(new int[] { mnFkItemId }, statement);
            }
            
            if (mnFkItemReferenceId_n != 0) {
                moDataItemRef = new SDataItem();
                moDataItemRef.read(new int[] { mnFkItemReferenceId_n }, statement);
            }
            
            if (mnFkUserUnitId != 0) {
                moDataUnitUsr = new SDataUnit();
                moDataUnitUsr.read(new int[] { mnFkUserUnitId }, statement);
            }
            
            readOptionalInfo(session);

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
                    (mtDateDelivery_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateDelivery_n) + "', ") + 
                    mdQuantity + ", " + 
                    mdPriceUnitarySystem + ", " + 
                    mdPriceUnitary + ", " + 
                    mdUserQuantity + ", " + 
                    mdUserPriceUnitary + ", " + 
                    "'" + msUserPriceUnitaryReference + "', " + 
                    mdTotal_r + ", " + 
                    mnCosnsumptionEstimated + ", " + 
                    (mbNewItem ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    mnFkUserUnitId + ", " + 
                    (mnFkMatRequestPriorityId_n == 0 ? "NULL, " : mnFkMatRequestPriorityId_n + ", ") + 
                    (mnFkEntMatConsumptionEntityId_n == 0 ? "NULL, " : mnFkEntMatConsumptionEntityId_n + ", ") + 
                    (mnFkSubentMatConsumptionEntityId_n == 0 ? "NULL, " : mnFkSubentMatConsumptionEntityId_n + ", ") + 
                    (mnFkSubentMatConsumptionSubentityId_n == 0 ? "NULL, " : mnFkSubentMatConsumptionSubentityId_n + ", ") + 
                    (mnFkCostCenterId_n == 0 ? "NULL, " : mnFkCostCenterId_n + ", ") + 
                    (mnFkItemReferenceId_n == 0 ? "NULL " : mnFkItemReferenceId_n + " ") + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_mat_req = " + mnPkMatRequestId + ", " +
                    //"id_ety = " + mnPkEntryId + ", " +
                    "dt_req_n = " + (mtDateRequest_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateRequest_n) + "', ") +
                    "dt_delivery_n = " + (mtDateDelivery_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateDelivery_n) + "', ") +
                    "qty = " + mdQuantity + ", " +
                    "price_u_sys = " + mdPriceUnitarySystem + ", " +
                    "price_u = " + mdPriceUnitary + ", " +
                    "user_qty = " + mdUserQuantity + ", " +
                    "user_price_u = " + mdUserPriceUnitary + ", " +
                    "user_price_u_ref = '" + msUserPriceUnitaryReference + "', " +
                    "tot_r = " + mdTotal_r + ", " +
                    "cons_est = " + mnCosnsumptionEstimated + ", " +
                    "b_new_item = " + (mbNewItem ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_user_unit = " + mnFkUserUnitId + ", " +
                    "fk_mat_req_pty_n = " + (mnFkMatRequestPriorityId_n == 0 ? "NULL, " : mnFkMatRequestPriorityId_n + ", ") +
                    "fk_ent_mat_cons_ent_n = " + (mnFkEntMatConsumptionEntityId_n == 0 ? "NULL, " : mnFkEntMatConsumptionEntityId_n + ", ") +
                    "fk_subent_mat_cons_ent_n = " + (mnFkSubentMatConsumptionEntityId_n == 0 ? "NULL, " : mnFkSubentMatConsumptionEntityId_n + ", ") +
                    "fk_subent_mat_cons_subent_n = " + (mnFkSubentMatConsumptionSubentityId_n == 0 ? "NULL, " : mnFkSubentMatConsumptionSubentityId_n + ", ") +
                    "fk_cc_n = " + (mnFkCostCenterId_n == 0 ? "NULL, " : mnFkCostCenterId_n + ", ") +
                    "fk_item_ref_n = " + (mnFkItemReferenceId_n == 0 ? "NULL " : mnFkItemReferenceId_n + " ") +
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
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY_ITEM_CHG) + " " + 
                "WHERE id_mat_req = " + mnPkMatRequestId + " AND id_ety = " + mnPkEntryId + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialRequestEntryItemChange change : maChildItemChange) {
            change.setPkMatRequestId(mnPkMatRequestId);
            change.setPkEntryId(mnPkEntryId);
            change.setRegistryNew(true);
            change.save(session);
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
        registry.setDateDelivery_n(this.getDateDelivery_n());
        registry.setQuantity(this.getQuantity());
        registry.setPriceUnitarySystem(this.getPriceUnitarySystem());
        registry.setPriceUnitary(this.getPriceUnitary());
        registry.setUserQuantity(this.getUserQuantity());
        registry.setUserPriceUnitary(this.getUserPriceUnitary());
        registry.setUserPriceUnitaryReference(this.getUserPriceUnitaryReference());
        registry.setTotal_r(this.getTotal_r());
        registry.setCosnsumptionEstimated(this.getCosnsumptionEstimated());
        registry.setNewItem(this.isNewItem());
        registry.setDeleted(this.isDeleted());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkUserUnitId(this.getFkUserUnitId());
        registry.setFkMatRequestPriorityId_n(this.getFkMatRequestPriorityId_n());
        registry.setFkEntMatConsumptionEntityId_n(this.getFkEntMatConsumptionEntityId_n());
        registry.setFkSubentMatConsumptionEntityId_n(this.getFkSubentMatConsumptionEntityId_n());
        registry.setFkSubentMatConsumptionSubentityId_n(this.getFkSubentMatConsumptionSubentityId_n());
        registry.setFkCostCenterId_n(this.getFkCostCenterId_n());
        registry.setFkItemReferenceId_n(this.getFkItemReferenceId_n());
        
        for (SDbMaterialRequestEntryNote note : this.getChildNotes()) {
            registry.getChildNotes().add(note);
        }
        
        for (SDbMaterialRequestEntryItemChange change : this.getChildItemChange()) {
            registry.getChildItemChange().add(change);
        }
        
        registry.setDataItem(this.getDataItem());
        registry.setDataItemRef(this.getDataItemRef());
        registry.setAuxRowId(this.getAuxRowId());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkEntryId };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch (row) {
            case 0: value = mbNewItem ? "" : moDataItem.getKey(); break;
            case 1: value = mbNewItem ? getItemNewDescription() : moDataItem.getName(); break;
            case 2: value = mbNewItem ? "" : moDataItem.getPartNumber(); break;
            case 3: value = moDataItemRef == null ? "" : moDataItemRef.getKey(); break;
            case 4: value = mdUserQuantity; break;
            case 5: value = moDataUnitUsr.getSymbol(); break;
            case 6: value = mdQuantity; break;
            case 7: value = moDataItem.getDbmsDataUnit().getSymbol(); break;
            case 8: value = mdTotal_r; break;
            case 9: value = mbNewItem; break;
            case 10: value = mnCosnsumptionEstimated; break;
            case 11: value = moDbmsMatConsEntity != null ? moDbmsMatConsEntity.getCode() : ""; break;
            case 12: value = moDbmsMatConsSubentity != null ? moDbmsMatConsSubentity.getCode() : ""; break;
            case 13: value = mtDateRequest_n != null ? mtDateRequest_n : null; break;
            case 14: value = moDbmsMatReqPty != null ? moDbmsMatReqPty.getName(): ""; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
