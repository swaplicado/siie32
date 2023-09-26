/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
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
public class SDbMaterialRequest extends SDbRegistryUser {
    
    protected int mnPkMatRequestId;
    protected int mnNumber;
    protected Date mtDate;
    protected Date mtDateRequest_n;
    protected String msReference;
    protected double mdTotal_r;
    protected boolean mbExternalSystem;
    protected String msExternalSystemId;
    protected boolean mbCloseProvision;
    protected boolean mbClosePurchase;
    //protected boolean mbDeleted;
    protected int mnFkMatProvisionEntityId;
    protected int mnFkMatRequestPriorityId;
    protected int mnFkMatRequestStatusId;
    protected int mnFkMatProvisionStatusId;
    protected int mnFkMatPurchaseStatusId;
    protected int mnFkUserRequesterId;
    protected int mnFkContractorId_n;
    protected int mnFkUserCloseProvisionId;
    protected int mnFkUserClosePurchaseId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserCloseProvision;
    protected Date mtTsUserClosePurchase;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbMaterialRequestNote> maChildNotes;
    protected ArrayList<SDbMaterialRequestCostCenter> maChildCostCenters;
    protected ArrayList<SDbMaterialRequestEntry> maChildEntries;
   
    protected String msAuxReqStatus;
    protected String msAuxAuthStatus;
    protected String msAuxProvStatus;
    protected String msAuxPurStatus;
    
    protected int mnAuxReqStatusIdOld;
    protected int mnAuxReqAuthStatusId;
    protected int mnAuxReqAuthStatusIdOld;
    protected int mnAuxReqProvStatusIdOld;
    protected int mnAuxReqPurStatusIdOld;
    protected String msAuxProvEntName;
    
    protected String msAuxAuthUser;
    
    protected boolean mbAuxLastProvClosedSta;
    protected boolean mbAuxLastPurClosedSta;

    public SDbMaterialRequest() {
        super(SModConsts.TRN_MAT_REQ);
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDateRequest_n(Date t) { mtDateRequest_n = t; }
    public void setReference(String s) { msReference = s; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setExternalSystem(boolean b) { mbExternalSystem = b; }
    public void setExternalSystemId(String s) { msExternalSystemId = s; }
    public void setCloseProvision(boolean b) { mbCloseProvision = b; }
    public void setClosePurchase(boolean b) { mbClosePurchase = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkMatProvisionEntityId(int n) { mnFkMatProvisionEntityId = n; }
    public void setFkMatRequestPriorityId(int n) { mnFkMatRequestPriorityId = n; }
    public void setFkMatRequestStatusId(int n) { mnFkMatRequestStatusId = n; }
    public void setFkMatProvisionStatusId(int n) { mnFkMatProvisionStatusId = n; }
    public void setFkMatPurchaseStatusId(int n) { mnFkMatPurchaseStatusId = n; }
    public void setFkUserRequesterId(int n) { mnFkUserRequesterId = n; }
    public void setFkContractorId_n(int n) { mnFkContractorId_n = n; }
    public void setFkUserCloseProvisionId(int n) { mnFkUserCloseProvisionId = n; }
    public void setFkUserClosePurchaseId(int n) { mnFkUserClosePurchaseId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserCloseProvision(Date t) { mtTsUserCloseProvision = t; }
    public void setTsUserClosePurchase(Date t) { mtTsUserClosePurchase = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setAuxReqStatus(String s) { msAuxReqStatus = s; }
    public void setAuxAuthStatus(String s) { msAuxAuthStatus = s; }
    public void setAuxProvStatus(String s) { msAuxProvStatus = s; }
    public void setAuxPurStatus(String s) { msAuxPurStatus = s; }
    public void setAuxProvEntName(String s) { msAuxProvEntName = s; }
    
    public void setAuxReqStatusIdOld(int n) { mnAuxReqStatusIdOld = n; }
    public void setAuxReqAuthStatusId(int n) { mnAuxReqAuthStatusId = n; }
    public void setAuxReqAuthStatusIdOld(int n) { mnAuxReqAuthStatusIdOld = n; }
    public void setAuxReqProvStatusIdOld(int n) { mnAuxReqProvStatusIdOld = n; }
    public void setAuxReqPurStatusIdOld(int n) { mnAuxReqPurStatusIdOld = n; }
    
    public void setAuxLastProvClosedSta(boolean b) { mbAuxLastProvClosedSta = b; }
    public void setAuxLastPurClosedSta(boolean b) { mbAuxLastPurClosedSta = b; }
    
    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public Date getDateRequest_n() { return mtDateRequest_n; }
    public String getReference() { return msReference; }
    public double getTotal_r() { return mdTotal_r; }
    public boolean isExternalSystem() { return mbExternalSystem; }
    public String getExternalSystemId() { return msExternalSystemId; }
    public boolean isCloseProvision() { return mbCloseProvision; }
    public boolean isClosePurchase() { return mbClosePurchase; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkMatProvisionEntityId() { return mnFkMatProvisionEntityId; }
    public int getFkMatRequestPriorityId() { return mnFkMatRequestPriorityId; }
    public int getFkMatRequestStatusId() { return mnFkMatRequestStatusId; }
    public int getFkMatProvisionStatusId() { return mnFkMatProvisionStatusId; }
    public int getFkMatPurchaseStatusId() { return mnFkMatPurchaseStatusId; }
    public int getFkUserRequesterId() { return mnFkUserRequesterId; }
    public int getFkContractorId_n() { return mnFkContractorId_n; }
    public int getFkUserCloseProvisionId() { return mnFkUserCloseProvisionId; }
    public int getFkUserClosePurchaseId() { return mnFkUserClosePurchaseId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserCloseProvision() { return mtTsUserCloseProvision; }
    public Date getTsUserClosePurchase() { return mtTsUserClosePurchase; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbMaterialRequestNote> getChildNotes() { return maChildNotes; }
    public ArrayList<SDbMaterialRequestCostCenter> getChildCostCenters() { return maChildCostCenters; }
    public ArrayList<SDbMaterialRequestEntry> getChildEntries() { return maChildEntries; }
    
    public String getAuxReqStatus() { return msAuxReqStatus; }
    public String getAuxAuthStatus() { return msAuxAuthStatus; }
    public String getAuxProvStatus() { return msAuxProvStatus; }
    public String getAuxPurStatus() { return msAuxPurStatus; }

    public int getAuxReqStatusIdOld() { return mnAuxReqStatusIdOld; }
    public int getAuxReqAuthStatusId() { return mnAuxReqAuthStatusId; }
    public int getAuxReqAuthStatusIdOld() { return mnAuxReqAuthStatusIdOld; }
    public int getAuxReqProvStatusIdOld() { return mnAuxReqProvStatusIdOld; }
    public int getAuxReqPurStatusIdOld() { return mnAuxReqPurStatusIdOld; } 
    
    public String getAuxProvEntName() { return msAuxProvEntName; }
    
    public boolean getAuxLastProvClosedSta() { return mbAuxLastProvClosedSta; }
    public boolean getAuxLastPurClosedSta() { return mbAuxLastPurClosedSta; }
    
    public String getConsumptionInfo() {
        String description = "";
        for (SDbMaterialRequestCostCenter oCc : maChildCostCenters) {
            description += oCc.getConsumptionInfo() + " ";
        }
        
        return description;
    }
    
    private void saveLog(SGuiSession session) throws Exception {
        SDbMaterialRequestStatusLog log = new SDbMaterialRequestStatusLog();
        log.setPkMatRequestId(mnPkMatRequestId);
        log.setFkMatRequestStatusId(mnFkMatRequestStatusId);
        log.setFkMatRequestAuthotizationStatusId(mnAuxReqAuthStatusId);
        log.setFkMatProvisionStatusId(mnFkMatProvisionStatusId);
        log.setFkMatPurchaseStatusId(mnFkMatPurchaseStatusId);
        log.setRegistryNew(true);
        log.save(session);
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMatRequestId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMatRequestId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMatRequestId = 0;
        mnNumber = 0;
        mtDate = null;
        mtDateRequest_n = null;
        msReference = "";
        mdTotal_r = 0;
        mbExternalSystem = false;
        msExternalSystemId = "";
        mbCloseProvision = false;
        mbClosePurchase = false;
        mbDeleted = false;
        mnFkMatProvisionEntityId = 0;
        mnFkMatRequestPriorityId = 0;
        mnFkMatRequestStatusId = 0;
        mnFkMatProvisionStatusId = 0;
        mnFkMatPurchaseStatusId = 0;
        mnFkUserRequesterId = 0;
        mnFkContractorId_n = 0;
        mnFkUserCloseProvisionId = 0;
        mnFkUserClosePurchaseId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserCloseProvision = null;
        mtTsUserClosePurchase = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildNotes = new ArrayList<>();
        maChildCostCenters = new ArrayList<>();
        maChildEntries = new ArrayList<>();
        
        msAuxReqStatus = "";
        msAuxAuthStatus = "";
        msAuxProvStatus = "";
        msAuxPurStatus = "";
        
        mnAuxReqStatusIdOld = 0;
        mnAuxReqAuthStatusId = SAuthorizationUtils.AUTH_STATUS_NA; // Las autorizaciones trabajan de manera independiente, por lo cual es necesario iniciar en NA
        mnAuxReqAuthStatusIdOld = 0;
        mnAuxReqProvStatusIdOld = 0;
        mnAuxReqPurStatusIdOld = 0;
        
        msAuxProvEntName = "";
        mbAuxLastProvClosedSta = false;
        mbAuxLastPurClosedSta = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mat_req = " + mnPkMatRequestId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mat_req = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkMatRequestId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_mat_req), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMatRequestId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement;
        SDbMaterialRequestNote note;
        SDbMaterialRequestCostCenter costc;
        SDbMaterialRequestEntry entry;
        
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
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            mtDateRequest_n = resultSet.getDate("dt_req_n");
            msReference = resultSet.getString("ref");
            mdTotal_r = resultSet.getDouble("tot_r");
            mbExternalSystem = resultSet.getBoolean("b_ext_sys");
            msExternalSystemId = resultSet.getString("ext_sys_id");
            mbCloseProvision = resultSet.getBoolean("b_clo_prov");
            mbClosePurchase = resultSet.getBoolean("b_clo_pur");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkMatProvisionEntityId = resultSet.getInt("fk_mat_prov_ent");
            mnFkMatRequestPriorityId = resultSet.getInt("fk_mat_req_pty");
            mnFkMatRequestStatusId = resultSet.getInt("fk_st_mat_req");
            mnFkMatProvisionStatusId = resultSet.getInt("fk_st_mat_prov");
            mnFkMatPurchaseStatusId = resultSet.getInt("fk_st_mat_pur");
            mnFkUserRequesterId = resultSet.getInt("fk_usr_req");
            mnFkContractorId_n = resultSet.getInt("fk_contractor_n");
            mnFkUserCloseProvisionId = resultSet.getInt("fk_usr_clo_prov");
            mnFkUserClosePurchaseId = resultSet.getInt("fk_usr_clo_pur");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserCloseProvision = resultSet.getTimestamp("ts_usr_clo_prov");
            mtTsUserClosePurchase = resultSet.getTimestamp("ts_usr_clo_pur");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mbAuxLastProvClosedSta = mbCloseProvision;
            mbAuxLastPurClosedSta = mbClosePurchase;
            
            // Read aswell document notes:
            
            statement = session.getStatement().getConnection().createStatement();
            
            msSql = "SELECT id_nts " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_NTS) + " " +
                    "WHERE id_mat_req = " + mnPkMatRequestId + " " +
                    "ORDER BY id_nts ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                note = new SDbMaterialRequestNote();
                note.read(session, new int[] { mnPkMatRequestId, resultSet.getInt(1) });
                maChildNotes.add(note);
            }
            
            // Read aswell cost centers:
            
            msSql = "SELECT id_mat_ent_cons_ent, id_mat_subent_cons_ent, id_mat_subent_cons_subent, id_cc " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_CC) + " " +
                    "WHERE id_mat_req = " + mnPkMatRequestId + " ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                costc = new SDbMaterialRequestCostCenter();
                costc.read(session, new int[] { mnPkMatRequestId, resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4) });
                maChildCostCenters.add(costc);
            }
            
            // Read aswell document entries:
            
            msSql = "SELECT id_ety " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " " +
                    "WHERE id_mat_req = " + mnPkMatRequestId + " " + 
                    "ORDER BY id_ety ";
            
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                entry = new SDbMaterialRequestEntry();
                entry.read(session, new int[] { mnPkMatRequestId, resultSet.getInt(1) });
                maChildEntries.add(entry);
            }
            
            // Read Req Status
            
            msSql = "SELECT name " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_REQ) + " " +
                    "WHERE id_st_mat_req = " + mnFkMatRequestStatusId + " ";
            
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                msAuxReqStatus = resultSet.getString(1);
            }
            
            // Read Prov Status
            
            msSql = "SELECT name " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_PROV) + " " +
                    "WHERE id_st_mat_prov = " + mnFkMatProvisionStatusId + " ";
            
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                msAuxProvStatus = resultSet.getString(1);
            }
            // Read Pur Status
            
            msSql = "SELECT name " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_ST_MAT_PUR) + " " +
                    "WHERE id_st_mat_pur = " + mnFkMatPurchaseStatusId + " ";
            
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                msAuxPurStatus = resultSet.getString(1);
            }
            
            mnAuxReqAuthStatusId = SAuthorizationUtils.getAuthStatus(session, 
                    SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, new int[] { mnPkMatRequestId } );
            msAuxAuthStatus = SAuthorizationUtils.AUTH_STATUS_DESC.get(mnAuxReqAuthStatusId).toUpperCase();
            
            mnAuxReqStatusIdOld = mnFkMatRequestStatusId;
            mnAuxReqAuthStatusIdOld = mnAuxReqAuthStatusId;
            mnAuxReqProvStatusIdOld = mnFkMatProvisionStatusId;
            mnAuxReqPurStatusIdOld = mnFkMatPurchaseStatusId;
            
            // Read Requisition area
            msSql = "SELECT name " + 
                    "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_PROV_ENT) + " " +
                    "WHERE id_mat_prov_ent = " + mnFkMatProvisionEntityId + " ";
            
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                msAuxProvEntName = resultSet.getString(1);
            }
                       
            msAuxAuthStatus = SAuthorizationUtils.AUTH_STATUS_DESC.get(SAuthorizationUtils.getAuthStatus(session, 
                    SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, new int[] { mnPkMatRequestId } )).toUpperCase();
            
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
            mnFkUserCloseProvisionId = mbCloseProvision ? session.getUser().getPkUserId() : SUtilConsts.USR_NA_ID;
            mnFkUserClosePurchaseId = mbClosePurchase ? session.getUser().getPkUserId() : SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMatRequestId + ", " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    (mtDateRequest_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateRequest_n) + "', ") + 
                    "'" + msReference + "', " + 
                    mdTotal_r + ", " + 
                    (mbExternalSystem ? 1 : 0) + ", " + 
                    "'" + msExternalSystemId + "', " + 
                    (mbCloseProvision ? 1 : 0) + ", " + 
                    (mbClosePurchase ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkMatProvisionEntityId + ", " + 
                    mnFkMatRequestPriorityId + ", " + 
                    mnFkMatRequestStatusId + ", " + 
                    mnFkMatProvisionStatusId + ", " + 
                    mnFkMatPurchaseStatusId + ", " + 
                    mnFkUserRequesterId + ", " + 
                    (mnFkContractorId_n == 0 ? "NULL, " : mnFkContractorId_n + ", ") + 
                    mnFkUserCloseProvisionId + ", " + 
                    mnFkUserClosePurchaseId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            boolean updateProvisionStatus = false;
            boolean updatePurchaseStatus = false;
            
            mnFkUserUpdateId = session.getUser().getPkUserId();
            if (mbAuxLastProvClosedSta != mbCloseProvision && mbCloseProvision) {
                mnFkUserCloseProvisionId = session.getUser().getPkUserId();
                updateProvisionStatus = true;
            }
            else if (!mbCloseProvision) {
                mnFkUserCloseProvisionId = SUtilConsts.USR_NA_ID;
            }
            
            if (mbAuxLastPurClosedSta != mbClosePurchase && mbClosePurchase) {
                mnFkUserClosePurchaseId = session.getUser().getPkUserId();
                updatePurchaseStatus = true;
            }
            else if (!mbCloseProvision) {
                mnFkUserClosePurchaseId = SUtilConsts.USR_NA_ID;
            }
            mnFkMatRequestStatusId = mnFkMatRequestStatusId == 0 ? mnAuxReqStatusIdOld : mnFkMatRequestStatusId;
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_mat_req = " + mnPkMatRequestId + ", " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "dt_req_n = " + (mtDateRequest_n == null ? "NULL, " : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateRequest_n) + "', ") +
                    "ref = '" + msReference + "', " +
                    "tot_r = " + mdTotal_r + ", " +
                    "b_ext_sys = " + (mbExternalSystem ? 1 : 0) + ", " +
                    "ext_sys_id = '" + msExternalSystemId + "', " +
                    "b_clo_prov = " + (mbCloseProvision ? 1 : 0) + ", " +
                    "b_clo_pur = " + (mbClosePurchase ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_mat_prov_ent = " + mnFkMatProvisionEntityId + ", " +
                    "fk_mat_req_pty = " + mnFkMatRequestPriorityId + ", " +
                    "fk_st_mat_req = " + mnFkMatRequestStatusId + ", " +
                    "fk_st_mat_prov = " + mnFkMatProvisionStatusId + ", " +
                    "fk_st_mat_pur = " + mnFkMatPurchaseStatusId + ", " +
                    "fk_usr_req = " + mnFkUserRequesterId + ", " +
                    "fk_contractor_n = " + (mnFkContractorId_n == 0 ? "NULL, " : mnFkContractorId_n + ", ") +
                    "fk_usr_clo_prov = " + mnFkUserCloseProvisionId + ", " +
                    "fk_usr_clo_pur = " + mnFkUserClosePurchaseId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    (updateProvisionStatus ? "ts_usr_clo_prov = " + "NOW()" + ", " : "") +
                    (updatePurchaseStatus ? "ts_usr_clo_pur = " + "NOW()" + ", " : "") +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Save notes:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_NTS) + " " + 
                "WHERE id_mat_req = " + mnPkMatRequestId + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialRequestNote note : maChildNotes) {
            note.setPkMatRequestId(mnPkMatRequestId);
            note.setRegistryNew(true);
            note.save(session);
        }
        
        // Save cost centers:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_CC) + " " + 
                "WHERE id_mat_req = " + mnPkMatRequestId + " ";
        session.getStatement().execute(msSql);
        for (SDbMaterialRequestCostCenter cc : maChildCostCenters) {
            cc.setPkMatRequestId(mnPkMatRequestId);
            cc.setRegistryNew(true);
            cc.save(session);
        }
        
        // Save entries:
        
        for (SDbMaterialRequestEntry entry : maChildEntries) {
            entry.setPkMatRequestId(mnPkMatRequestId);
            entry.save(session);
        }
        
        // Guardar bitacora de cambios de estado
        
        mnAuxReqAuthStatusIdOld = mnAuxReqAuthStatusId;
        mnAuxReqAuthStatusId = SAuthorizationUtils.getAuthStatus(session, SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, new int[]{ mnPkMatRequestId });
        if (mnAuxReqStatusIdOld != mnFkMatRequestStatusId || mnAuxReqProvStatusIdOld != mnFkMatProvisionStatusId
                || mnAuxReqPurStatusIdOld != mnFkMatPurchaseStatusId || mnAuxReqAuthStatusIdOld != mnAuxReqAuthStatusId) {
            saveLog(session);
        }
        
        // Si el estatus de requisición esta "En autorización"
        if (mnFkMatRequestStatusId == SModSysConsts.TRNS_ST_MAT_REQ_AUTH) {
            mnAuxReqAuthStatusIdOld = mnAuxReqAuthStatusId;
            SAuthorizationUtils.processAuthorizations(session, SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, new int[]{ mnPkMatRequestId });
            mnAuxReqAuthStatusId = SAuthorizationUtils.getAuthStatus(session, SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, new int[]{ mnPkMatRequestId });
            
            mnAuxReqStatusIdOld = mnFkMatRequestStatusId;
            // Si el estatus de autorización esta "Autorizado" o "NA", y el estatus de requisición esta "En autorización", este pasa a "En suministro"
            if (mnAuxReqAuthStatusId == SAuthorizationUtils.AUTH_STATUS_AUTHORIZED || mnAuxReqAuthStatusId == SAuthorizationUtils.AUTH_STATUS_NA){
                mnFkMatRequestStatusId = SModSysConsts.TRNS_ST_MAT_REQ_PROV; 
            }
            // Si el estatus de autorización esta "Rechazado", y el estatus de requisición esta "En autorización", este pasa a "Cancelado"
            else if (mnAuxReqAuthStatusId == SAuthorizationUtils.AUTH_STATUS_REJECTED) {
                mnFkMatRequestStatusId = SModSysConsts.TRNS_ST_MAT_REQ_CAN;
            }
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    "fk_st_mat_req = " + mnFkMatRequestStatusId + " " + 
                    getSqlWhere();
            session.getStatement().execute(msSql);
            
            if (mnAuxReqStatusIdOld != mnFkMatRequestStatusId || mnAuxReqAuthStatusIdOld != mnAuxReqAuthStatusId) {
                saveLog(session);
            }
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaterialRequest clone() throws CloneNotSupportedException {
        SDbMaterialRequest registry = new SDbMaterialRequest();
        
        registry.setPkMatRequestId(this.getPkMatRequestId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setDateRequest_n(this.getDateRequest_n());
        registry.setReference(this.getReference());
        registry.setTotal_r(this.getTotal_r());
        registry.setExternalSystem(this.isExternalSystem());
        registry.setExternalSystemId(this.getExternalSystemId());
        registry.setCloseProvision(this.isCloseProvision());
        registry.setClosePurchase(this.isClosePurchase());
        registry.setDeleted(this.isDeleted());
        registry.setFkMatProvisionEntityId(this.getFkMatProvisionEntityId());
        registry.setFkMatRequestPriorityId(this.getFkMatRequestPriorityId());
        registry.setFkMatRequestStatusId(this.getFkMatRequestStatusId());
        registry.setFkMatProvisionStatusId(this.getFkMatProvisionStatusId());
        registry.setFkMatPurchaseStatusId(this.getFkMatPurchaseStatusId());
        registry.setFkUserRequesterId(this.getFkUserRequesterId());
        registry.setFkContractorId_n(this.getFkContractorId_n());
        registry.setFkUserCloseProvisionId(this.getFkUserCloseProvisionId());
        registry.setFkUserClosePurchaseId(this.getFkUserClosePurchaseId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserCloseProvision(this.getTsUserCloseProvision());
        registry.setTsUserClosePurchase(this.getTsUserClosePurchase());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbMaterialRequestNote note : this.getChildNotes()) {
            registry.getChildNotes().add(note);
        }
        
        for (SDbMaterialRequestCostCenter cc : this.getChildCostCenters()) {
            registry.getChildCostCenters().add(cc);
        }
        
        for (SDbMaterialRequestEntry entry : this.getChildEntries()) {
            registry.getChildEntries().add(entry);
        }
        
        registry.setAuxReqStatus(this.getAuxReqStatus());
        registry.setAuxProvStatus(this.getAuxProvStatus());
        registry.setAuxPurStatus(this.getAuxPurStatus());
        
        registry.setAuxReqStatusIdOld(this.getAuxReqStatusIdOld());
        registry.setAuxReqAuthStatusId(this.getAuxReqAuthStatusId());
        registry.setAuxReqAuthStatusIdOld(this.getAuxReqAuthStatusIdOld());
        registry.setAuxReqProvStatusIdOld(this.getAuxReqProvStatusIdOld());
        registry.setAuxReqPurStatusIdOld(this.getAuxReqPurStatusIdOld());
        
        registry.setAuxLastProvClosedSta(this.getAuxLastProvClosedSta());
        registry.setAuxLastPurClosedSta(this.getAuxLastPurClosedSta());
        
        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
    
}
