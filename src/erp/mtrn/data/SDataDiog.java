/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mod.SModSysConsts;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;
import sa.lib.SLibUtils;

/* IMPORTANT:
 * Every single change made to the definition of this class' table must be updated also in the following classes:
 * - erp.mod.trn.db.SDbInventoryValuationXXX
 * - erp.mtrn.data.SDataRawMaterialsConsume
 * All of them also make raw SQL insertions.
 */

/**
 *
 * @author Sergio Flores, Edwin Carmona, Gil De Jesús
 */
public class SDataDiog extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.util.Date mtDate;
    protected java.lang.String msNumberSeries;
    protected java.lang.String msNumber;
    protected java.lang.String msReference;
    protected double mdValue_r;
    protected int mnCostAsigned;
    protected int mnCostTransferred;
    protected boolean mbIsShipmentRequired;
    protected boolean mbIsShipped;
    protected boolean mbIsAudited;
    protected boolean mbIsAuthorized;
    protected boolean mbIsRecordAutomatic;
    protected boolean mbIsSystem;
    protected boolean mbIsDeleted;
    protected int mnFkDiogCategoryId;
    protected int mnFkDiogClassId;
    protected int mnFkDiogTypeId;
    protected int mnFkDiogAdjustmentTypeId;
    protected int mnFkCompanyBranchId;
    protected int mnFkWarehouseId;
    protected int mnFkDpsYearId_n;
    protected int mnFkDpsDocId_n;
    protected int mnFkDiogYearId_n;
    protected int mnFkDiogDocId_n;
    protected int mnFkMfgYearId_n;
    protected int mnFkMfgOrderId_n;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnFkMaintMovementTypeId;
    protected int mnFkMaintUserId_n;
    protected int mnFkMaintUserSupervisorId;
    protected int mnFkMaintReturnUserId_n;
    protected int mnFkMaintReturnUserSupervisorId;
    protected int mnFkUserShippedId;
    protected int mnFkUserAuditedId;
    protected int mnFkUserAuthorizedId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserShippedTs;
    protected java.util.Date mtUserAuditedTs;
    protected java.util.Date mtUserAuthorizedTs;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnDbmsLastDiogSignatureId;
    protected java.lang.String msDbmsDiogCategory;
    protected java.lang.String msDbmsDiogClass;
    protected java.lang.String msDbmsDiogType;
    protected java.lang.String msDbmsCompanyBranch;
    protected java.lang.String msDbmsCompanyBranchCode;
    protected java.lang.String msDbmsWarehouse;
    protected java.lang.String msDbmsWarehouseCode;
    protected java.lang.Object moDbmsRecordKey;
    protected java.util.Date mtDbmsRecordDate;

    protected erp.mtrn.data.SDataDiog moDbmsDataCounterpartDiog;

    protected java.util.Vector<erp.mtrn.data.SDataDiogEntry> mvDbmsDiogEntries;
    protected java.util.Vector<erp.mtrn.data.SDataDiogNotes> mvDbmsDiogNotes;

    protected boolean mbAuxIsDbmsDataCounterpartDiog;
    protected boolean mbAuxSignDiog;
    protected int moAuxSegregationStockId;

    public SDataDiog() {
        super(SDataConstants.TRN_DIOG);
        mvDbmsDiogEntries = new Vector<>();
        mvDbmsDiogNotes = new Vector<>();
        reset();
    }

    /*
     * Private methods
     */

    private boolean isBookkeepingRequired() {
        return !mbIsDeleted || mbIsRegistryEdited;
    }

    private boolean isStockRequired() {
        return !mbIsDeleted || mbIsRegistryEdited;
    }

    private void computeBookkeeping(final java.sql.Connection connection) throws Exception {

    }

    private SDataStockMove createStockMove(final java.sql.Statement statement, final SDataDiogEntry iogEntry, final STrnStockMove move) throws SQLException, Exception {
        int lotId = SLibConstants.UNDEFINED;
        String sql = "";
        ResultSet resultSet = null;
        SDataStockLot stockLot = null;
        SDataStockMove stockMove = new SDataStockMove();

        stockMove.setPkYearId(SLibTimeUtilities.digestYear(mtDate)[0]);
        stockMove.setPkItemId(move.getPkItemId());
        stockMove.setPkUnitId(move.getPkUnitId());
        stockMove.setPkLotId(move.getPkLotId());
        stockMove.setPkCompanyBranchId(mnFkCompanyBranchId);
        stockMove.setPkWarehouseId(mnFkWarehouseId);
        stockMove.setPkMoveId(0);
        stockMove.setDate(mtDate);

        if (mnFkDiogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN) {
            stockMove.setMoveIn(move.getQuantity());
            stockMove.setMoveOut(0);
            stockMove.setDebit(move.getValue());    // can be set by SDataStockMove.computeValue()
            stockMove.setCredit(0);                 // can be set by SDataStockMove.computeValue()
        }
        else {
            stockMove.setMoveIn(0);
            stockMove.setMoveOut(move.getQuantity());
            stockMove.setDebit(0);                  // can be set by SDataStockMove.computeValue()
            stockMove.setCredit(move.getValue());   // can be set by SDataStockMove.computeValue()
        }

        stockMove.setCostUnitary(iogEntry.getValueUnitary());
        stockMove.setCost(0);
        stockMove.setIsDeleted(false);
        stockMove.setFkDiogCategoryId(mnFkDiogCategoryId);
        stockMove.setFkDiogClassId(mnFkDiogClassId);
        stockMove.setFkDiogTypeId(mnFkDiogTypeId);
        stockMove.setFkDiogAdjustmentTypeId(mnFkDiogAdjustmentTypeId);
        stockMove.setFkDiogYearId(iogEntry.getPkYearId());
        stockMove.setFkDiogDocId(iogEntry.getPkDocId());
        stockMove.setFkDiogEntryId(iogEntry.getPkEntryId());
        stockMove.setFkDpsYearId_n(iogEntry.getFkDpsYearId_n());
        stockMove.setFkDpsDocId_n(iogEntry.getFkDpsDocId_n());
        stockMove.setFkDpsEntryId_n(iogEntry.getFkDpsEntryId_n());
        stockMove.setFkDpsAdjustmentYearId_n(iogEntry.getFkDpsAdjustmentYearId_n());
        stockMove.setFkDpsAdjustmentDocId_n(iogEntry.getFkDpsAdjustmentDocId_n());
        stockMove.setFkDpsAdjustmentEntryId_n(iogEntry.getFkDpsAdjustmentEntryId_n());
        stockMove.setFkMfgYearId_n(iogEntry.getFkMfgYearId_n());
        stockMove.setFkMfgOrderId_n(iogEntry.getFkMfgOrderId_n());
        stockMove.setFkMfgChargeId_n(iogEntry.getFkMfgChargeId_n());
        stockMove.setFkBookkeepingYearId_n(mnFkBookkeepingYearId_n);
        stockMove.setFkBookkeepingNumberId_n(mnFkBookkeepingNumberId_n);
        stockMove.setAuxValue(iogEntry.getValue());

        stockMove.computeValue();

       if (move.getPkLotId() == SLibConstants.UNDEFINED) {
            sql = "SELECT id_lot, dt_exp_n FROM trn_lot WHERE " +
                    "id_item = " + move.getPkItemId() + " AND id_unit = " + move.getPkUnitId() + " AND " +
                    "lot = '" + move.getAuxLot() + "' AND b_del = 0 ";
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                lotId = resultSet.getInt("id_lot");
                
                if (move.getAuxLotDateExpiration() != null) {
                    if (resultSet.getDate("dt_exp_n") != move.getAuxLotDateExpiration()) {
                       sql = "UPDATE trn_lot SET dt_exp_n = '" + new java.sql.Date(move.getAuxLotDateExpiration().getTime())  + "' WHERE " +
                        "id_item = " + move.getPkItemId() + " AND id_unit = " + move.getPkUnitId() + " AND " +       
                        "id_lot = " + lotId + " ";
                         statement.execute(sql);
                    }
                }    
            }
            else {
                stockLot = new SDataStockLot();
                stockLot.setPkItemId(move.getPkItemId());
                stockLot.setPkUnitId(move.getPkUnitId());
                stockLot.setPkLotId(move.getPkLotId());
                stockLot.setLot(move.getAuxLot());
                stockLot.setDateExpiration_n(move.getAuxLotDateExpiration());
                stockLot.setIsBlocked(false);
                stockLot.setIsDeleted(false);
                stockLot.setFkUserNewId(mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
                stockLot.setFkUserEditId(SLibConstants.UNDEFINED);
                stockLot.setFkUserDeleteId(SLibConstants.UNDEFINED);

                if (stockLot.save(statement.getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }

                lotId = stockLot.getPkLotId();
            }

            stockMove.setPkLotId(lotId);
        }
       else {
            if (move.getAuxLotDateExpiration() != null) {
                sql = "SELECT dt_exp_n FROM trn_lot WHERE " +
                      "id_item = " + move.getPkItemId() + " AND id_unit = " + move.getPkUnitId() + " AND " + 
                      "id_lot = " + move.getPkLotId() + " ";
                resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    if (resultSet.getDate("dt_exp_n") != new java.sql.Date(move.getAuxLotDateExpiration().getTime())) {
                        sql = "UPDATE trn_lot SET dt_exp_n = '" + new java.sql.Date(move.getAuxLotDateExpiration().getTime())  + "' WHERE " +
                        "id_item = " + move.getPkItemId() + " AND id_unit = " + move.getPkUnitId() + " AND " +        
                        "id_lot = " + move.getPkLotId() + " ";
                        statement.execute(sql);
                    }
                }
            }
       }

        return stockMove;
    }

    private void computeStock(final java.sql.Connection connection) throws Exception {
        String sql = "";
        Statement statement = connection.createStatement();
        SDataStockMove stockMove = null;
        Vector<SDataStockMove> stockMoves = new Vector<>();

        // Delete previous stock moves, if any:

        sql = "UPDATE trn_stk SET b_del = 1 " +
                "WHERE fid_diog_year = " + mnPkYearId + " AND fid_diog_doc = " + mnPkDocId + " AND b_del = 0 ";
        statement.execute(sql);

        if (isStockRequired()) {
            // Save stock moves:

            for (SDataDiogEntry entry : mvDbmsDiogEntries) {
                if (!entry.getIsDeleted() && entry.getIsInventoriable()) {
                    for (STrnStockMove move : entry.getAuxStockMoves()) {
                        stockMove = createStockMove(statement, entry, move);
                        stockMoves.add(stockMove);
                    }
                }
            }

            for (SDataStockMove move : stockMoves) {
                if (move.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
            }
        }
    }
    
    /**
     * Releasing segregated stocks when material is assigned to PO.
     * Only what is segregated is released
     * 
     * @param connection
     * @param segregationStkId id of segregation to release.
     * @param entries items to be moved.
     * @param companyBranchId id of the company branch.
     * @param warehouseId id of warehouse origin of movement.
     * @throws Exception 
     */
    private void releaseSegregations(final java.sql.Connection connection, final int segregationStkId, final Vector<SDataDiogEntry> entries, final int companyBranchId, final int warehouseId) throws Exception {
        SDataStockSegregationWarehouseEntry ety = null;
        double quantityToRelease = 0;
        double quantitySegregated = 0;
        
        if (!STrnStockSegregationUtils.isValidYear(connection, segregationStkId, mnPkYearId)) {
            throw new Exception("La segregación es de un año diferente al actual.");
        }
        
        for (SDataDiogEntry diogEty: entries) {
            ResultSet resSeg = null;

            String sqlStockSegregation = "SELECT COALESCE(SUM(wety.qty_inc - wety.qty_dec), 0) AS f_seg_qty " +
                    "FROM trn_stk_seg_whs AS swhs " +
                    "INNER JOIN trn_stk_seg_whs_ety AS wety ON swhs.id_stk_seg = wety.id_stk_seg AND swhs.id_whs = wety.id_whs " +
                    "WHERE fid_year = " + diogEty.getPkYearId() + " AND fid_item = " + diogEty.getFkItemId() + " AND fid_unit = " + diogEty.getFkOriginalUnitId() + " ";

            if (companyBranchId != 0) {
                sqlStockSegregation += "AND swhs.fid_cob = " + companyBranchId + " ";
            }

            if (warehouseId != 0) {
                sqlStockSegregation += "AND swhs.fid_whs = " + warehouseId + " ";
            }

            if (segregationStkId != 0) {
                sqlStockSegregation += " AND wety.id_stk_seg = " + segregationStkId;
            }

            resSeg = connection.createStatement().executeQuery(sqlStockSegregation);

            if (resSeg.next()) {
                quantitySegregated = resSeg.getDouble("f_seg_qty");
            
                if (quantitySegregated > 0) {
                    if (quantitySegregated >= diogEty.getQuantity()) {
                        quantityToRelease = diogEty.getQuantity();
                    }
                    else {
                        quantityToRelease = quantitySegregated;
                    }

                    ety = new SDataStockSegregationWarehouseEntry();
                    ety.setPkStockSegregationId(segregationStkId);
                    ety.setPkWarehouseId(warehouseId);
                    ety.setQuantityDecrement(quantityToRelease);
                    ety.setFkStockSegregationMovementTypeId(SDataConstantsSys.TRNS_TP_STK_SEG_DEC);
                    ety.setFkYearId(mnPkYearId);
                    ety.setFkItemId(diogEty.getFkItemId());
                    ety.setFkUnitId(diogEty.getFkOriginalUnitId());

                    ety.save(connection);
                }
            }
        }
    }

    /**
     * Verifies that the document (Dps) exists in the detailed inventory(iog) 
     * if the document does not appear in the detail, the value would be null in case 
     * there is no other document reference, other wise it will take the first document
     * reference.
     * 
     */
    private void verifyDpsRelation(){
        boolean bDpsExist = false;
        int nDpsNumberId_n = SLibConstants.UNDEFINED; 
        int nDpsYearId_n = SLibConstants.UNDEFINED;
        
        
        for (SDataDiogEntry entry : mvDbmsDiogEntries) {
            if (entry.getFkDpsDocId_n() == mnFkDpsDocId_n && entry.getFkDpsYearId_n() == mnFkDpsYearId_n) {
                bDpsExist = true;
                break;
            }
            else if ((entry.getFkDpsDocId_n() != SLibConstants.UNDEFINED && entry.getFkDpsYearId_n() != SLibConstants.UNDEFINED) &&
                    (nDpsNumberId_n == SLibConstants.UNDEFINED && nDpsYearId_n == SLibConstants.UNDEFINED)) {
                nDpsNumberId_n = entry.getFkDpsDocId_n();
                nDpsYearId_n = entry.getFkDpsYearId_n();
            }
        }
         
        if (!bDpsExist) {
            mnFkDpsDocId_n = nDpsNumberId_n;
            mnFkDpsYearId_n = nDpsYearId_n;
        }
        
    }
    
    /*
     * Public methods
     */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setNumberSeries(java.lang.String s) { msNumberSeries = s; }
    public void setNumber(java.lang.String s) { msNumber = s; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setValue_r(double d) { mdValue_r = d; }
    public void setCostAsigned(int n) { mnCostAsigned = n; }
    public void setCostTransferred(int n) { mnCostTransferred = n; }
    public void setIsShipmentRequired(boolean b) { mbIsShipmentRequired = b; }
    public void setIsShipped(boolean b) { mbIsShipped = b; }
    public void setIsAudited(boolean b) { mbIsAudited = b; }
    public void setIsAuthorized(boolean b) { mbIsAuthorized = b; }
    public void setIsRecordAutomatic(boolean b) { mbIsRecordAutomatic = b; }
    public void setIsSystem(boolean b) { mbIsSystem = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkDiogCategoryId(int n) { mnFkDiogCategoryId = n; }
    public void setFkDiogClassId(int n) { mnFkDiogClassId = n; }
    public void setFkDiogTypeId(int n) { mnFkDiogTypeId = n; }
    public void setFkDiogAdjustmentTypeId(int n) { mnFkDiogAdjustmentTypeId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkWarehouseId(int n) { mnFkWarehouseId = n; }
    public void setFkDpsYearId_n(int n) { mnFkDpsYearId_n = n; }
    public void setFkDpsDocId_n(int n) { mnFkDpsDocId_n = n; }
    public void setFkDiogYearId_n(int n) { mnFkDiogYearId_n = n; }
    public void setFkDiogDocId_n(int n) { mnFkDiogDocId_n = n; }
    public void setFkMfgYearId_n(int n) { mnFkMfgYearId_n = n; }
    public void setFkMfgOrderId_n(int n) { mnFkMfgOrderId_n = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setFkMaintMovementTypeId(int n) { mnFkMaintMovementTypeId = n; }
    public void setFkMaintUserId_n(int n) { mnFkMaintUserId_n = n; }
    public void setFkMaintUserSupervisorId(int n) { mnFkMaintUserSupervisorId = n; }
    public void setFkMaintReturnUserId_n(int n) { mnFkMaintReturnUserId_n = n; }
    public void setFkMaintReturnUserSupervisorId(int n) { mnFkMaintReturnUserSupervisorId = n; }
    public void setFkUserShippedId(int n) { mnFkUserShippedId = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserAuthorizedId(int n) { mnFkUserAuthorizedId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserShippedTs(java.util.Date t) { mtUserShippedTs = t; }
    public void setUserAuditedTs(java.util.Date t) { mtUserAuditedTs = t; }
    public void setUserAuthorizedTs(java.util.Date t) { mtUserAuthorizedTs = t; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.util.Date getDate() { return mtDate; }
    public java.lang.String getNumberSeries() { return msNumberSeries; }
    public java.lang.String getNumber() { return msNumber; }
    public java.lang.String getReference() { return msReference; }
    public double getValue_r() { return mdValue_r; }
    public int getCostAsigned() { return mnCostAsigned; }
    public int getCostTransferred() { return mnCostTransferred; }
    public boolean getIsShipmentRequired() { return mbIsShipmentRequired; }
    public boolean getIsShipped() { return mbIsShipped; }
    public boolean getIsAudited() { return mbIsAudited; }
    public boolean getIsAuthorized() { return mbIsAuthorized; }
    public boolean getIsRecordAutomatic() { return mbIsRecordAutomatic; }
    public boolean getIsSystem() { return mbIsSystem; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkDiogCategoryId() { return mnFkDiogCategoryId; }
    public int getFkDiogClassId() { return mnFkDiogClassId; }
    public int getFkDiogTypeId() { return mnFkDiogTypeId; }
    public int getFkDiogAdjustmentTypeId() { return mnFkDiogAdjustmentTypeId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkWarehouseId() { return mnFkWarehouseId; }
    public int getFkDpsYearId_n() { return mnFkDpsYearId_n; }
    public int getFkDpsDocId_n() { return mnFkDpsDocId_n; }
    public int getFkDiogYearId_n() { return mnFkDiogYearId_n; }
    public int getFkDiogDocId_n() { return mnFkDiogDocId_n; }
    public int getFkMfgYearId_n() { return mnFkMfgYearId_n; }
    public int getFkMfgOrderId_n() { return mnFkMfgOrderId_n; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getFkMaintMovementTypeId() { return mnFkMaintMovementTypeId; }
    public int getFkMaintUserId_n() { return mnFkMaintUserId_n; }
    public int getFkMaintUserSupervisorId() { return mnFkMaintUserSupervisorId; }
    public int getFkMaintReturnUserId_n() { return mnFkMaintReturnUserId_n; }
    public int getFkMaintReturnUserSupervisorId() { return mnFkMaintReturnUserSupervisorId; }
    public int getFkUserShippedId() { return mnFkUserShippedId; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserAuthorizedId() { return mnFkUserAuthorizedId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserShippedTs() { return mtUserShippedTs; }
    public java.util.Date getUserAuditedTs() { return mtUserAuditedTs; }
    public java.util.Date getUserAuthorizedTs() { return mtUserAuthorizedTs; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsLastDiogSignatureId(int n) { mnDbmsLastDiogSignatureId = n; }
    public void setDbmsDiogCategory(java.lang.String s) { msDbmsDiogCategory = s; }
    public void setDbmsDiogClass(java.lang.String s) { msDbmsDiogClass = s; }
    public void setDbmsDiogType(java.lang.String s) { msDbmsDiogType = s; }
    public void setDbmsCompanyBranch(java.lang.String s) { msDbmsCompanyBranch = s; }
    public void setDbmsCompanyBranchCode(java.lang.String s) { msDbmsCompanyBranchCode = s; }
    public void setDbmsWarehouse(java.lang.String s) { msDbmsWarehouse = s; }
    public void setDbmsWarehouseCode(java.lang.String s) { msDbmsWarehouseCode = s; }
    public void setDbmsRecordKey(java.lang.Object o) { moDbmsRecordKey = o; }
    public void setDbmsRecordDate(java.util.Date t) { mtDbmsRecordDate = t; }

    public void setDbmsDataCounterpartDiog(erp.mtrn.data.SDataDiog o) { moDbmsDataCounterpartDiog = o; }

    public int getDbmsLastDiogSignatureId() { return mnDbmsLastDiogSignatureId; }
    public java.lang.String getDbmsDiogCategory() { return msDbmsDiogCategory; }
    public java.lang.String getDbmsDiogClass() { return msDbmsDiogClass; }
    public java.lang.String getDbmsDiogType() { return msDbmsDiogType; }
    public java.lang.String getDbmsCompanyBranch() { return msDbmsCompanyBranch; }
    public java.lang.String getDbmsCompanyBranchCode() { return msDbmsCompanyBranchCode; }
    public java.lang.String getDbmsWarehouse() { return msDbmsWarehouse; }
    public java.lang.String getDbmsWarehouseCode() { return msDbmsWarehouseCode; }
    public java.lang.Object getDbmsRecordKey() { return moDbmsRecordKey; }
    public java.util.Date getDbmsRecordDate() { return mtDbmsRecordDate; }

    public erp.mtrn.data.SDataDiog getDbmsDataCounterpartDiog() { return moDbmsDataCounterpartDiog; }

    public java.util.Vector<erp.mtrn.data.SDataDiogEntry> getDbmsEntries() { return mvDbmsDiogEntries; }
    public java.util.Vector<erp.mtrn.data.SDataDiogNotes> getDbmsNotes() { return mvDbmsDiogNotes; }

    public void setAuxIsDbmsDataCounterpartDiog(boolean b) { mbAuxIsDbmsDataCounterpartDiog = b; }
    public void setAuxSignDiog(boolean b) { mbAuxSignDiog = b; }
    public void setAuxSegregationStockId(int n) { moAuxSegregationStockId = n; }

    public boolean getAuxIsDbmsDataCounterpartDiog() { return mbAuxIsDbmsDataCounterpartDiog; }
    public boolean getAuxSignDiog() { return mbAuxSignDiog; }
    public int getAuxSegregationStockId() { return moAuxSegregationStockId; }

    public int[] getDiogCategoryKey() { return new int[] { mnFkDiogCategoryId }; }
    public int[] getDiogClassKey() { return new int[] { mnFkDiogCategoryId, mnFkDiogClassId }; }
    public int[] getDiogTypeKey() { return new int[] { mnFkDiogCategoryId, mnFkDiogClassId, mnFkDiogTypeId }; }
    public int[] getDiogTypeAdjKey() { return new int[] { mnFkDiogAdjustmentTypeId }; }
    public int[] getWarehouseKey() { return new int[] { mnFkCompanyBranchId, mnFkWarehouseId }; }
    public int[] getLinkedDpsKey_n() { return mnFkDpsYearId_n == SLibConstants.UNDEFINED || mnFkDpsDocId_n == SLibConstants.UNDEFINED ? null : new int[] { mnFkDpsYearId_n, mnFkDpsDocId_n }; }
    public int[] getLinkedDiogKey_n() { return mnFkDiogYearId_n == SLibConstants.UNDEFINED || mnFkDiogDocId_n == SLibConstants.UNDEFINED ? null : new int[] { mnFkDiogYearId_n, mnFkDiogDocId_n }; }
    public int[] getProdOrderKey_n() { return mnFkMfgYearId_n == SLibConstants.UNDEFINED || mnFkMfgOrderId_n == SLibConstants.UNDEFINED ? null : new int[] { mnFkMfgYearId_n, mnFkMfgOrderId_n }; }

    public boolean isRegistryEditable() {
        boolean editable = true;

        if (this.getIsShipped()) {
            editable = false;
        }
        else if (this.getIsAudited()) {
            editable = false;
        }
        else if (this.getIsAuthorized()) {
            editable = false;
        }
        else if (this.getIsSystem()) {
            editable = false;
        }
        else if (this.getIsDeleted()) {
            editable = false;
        }

        return editable;
    }

    public String getNonEditableHelp() {
        String help = "";

        if (this.getIsShipped()) {
            help += "\n- El documento está embarcado.";
        }
        else if (this.getIsAudited()) {
            help += "\n- El documento está auditado.";
        }
        else if (this.getIsAuthorized()) {
            help += "\n- El documento está autorizado.";
        }
        else if (this.getIsSystem()) {
            help += "\n- El documento es de sistema.";
        }
        else if (this.getIsDeleted()) {
            help += "\n- El documento está eliminado.";
        }

        if (help.length() > 0) {
            help = "No se puede modificar el documento porque:" + help;
        }

        return help;
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mtDate = null;
        msNumberSeries = "";
        msNumber = "";
        msReference = "";
        mdValue_r = 0;
        mnCostAsigned = 0;
        mnCostTransferred = 0;
        mbIsShipmentRequired = false;
        mbIsShipped = false;
        mbIsAudited = false;
        mbIsAuthorized = false;
        mbIsRecordAutomatic = false;
        mbIsSystem = false;
        mbIsDeleted = false;
        mnFkDiogCategoryId = 0;
        mnFkDiogClassId = 0;
        mnFkDiogTypeId = 0;
        mnFkDiogAdjustmentTypeId = 0;
        mnFkCompanyBranchId = 0;
        mnFkWarehouseId = 0;
        mnFkDpsYearId_n = 0;
        mnFkDpsDocId_n = 0;
        mnFkDiogYearId_n = 0;
        mnFkDiogDocId_n = 0;
        mnFkMfgYearId_n = 0;
        mnFkMfgOrderId_n = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnFkMaintMovementTypeId = SModSysConsts.TRNS_TP_MAINT_MOV_NA;   // default value set only for preventing bugs
        mnFkMaintUserId_n = 0;
        mnFkMaintUserSupervisorId = SModSysConsts.TRN_MAINT_USER_SUPV_NA;   // default value set only for preventing bugs
        mnFkMaintReturnUserId_n = 0;
        mnFkMaintReturnUserSupervisorId = SModSysConsts.TRN_MAINT_USER_SUPV_NA;   // default value set only for preventing bugs
        mnFkUserShippedId = 0;
        mnFkUserAuditedId = 0;
        mnFkUserAuthorizedId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserShippedTs = null;
        mtUserAuditedTs = null;
        mtUserAuthorizedTs = null;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mnDbmsLastDiogSignatureId = 0;
        msDbmsDiogCategory = "";
        msDbmsDiogClass = "";
        msDbmsDiogType = "";
        msDbmsCompanyBranch = "";
        msDbmsCompanyBranchCode = "";
        msDbmsWarehouse = "";
        msDbmsWarehouseCode = "";
        moDbmsRecordKey = null;
        mtDbmsRecordDate = null;

        moDbmsDataCounterpartDiog = null;

        mvDbmsDiogEntries.clear();
        mvDbmsDiogNotes.clear();

        mbAuxIsDbmsDataCounterpartDiog = false;
        mbAuxSignDiog = false;
        moAuxSegregationStockId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT iog.*, iog_ct.ct_iog, iog_cl.cl_iog, iog_tp.tp_iog, cob.bpb, cob.code, ent.ent, ent.code " +
                    "FROM trn_diog AS iog " +
                    "INNER JOIN erp.trns_ct_iog AS iog_ct ON iog.fid_ct_iog = iog_ct.id_ct_iog " +
                    "INNER JOIN erp.trns_cl_iog AS iog_cl ON iog.fid_cl_iog = iog_cl.id_cl_iog AND iog_cl.id_ct_iog = iog.fid_ct_iog " +
                    "INNER JOIN erp.trns_tp_iog AS iog_tp ON iog.fid_tp_iog = iog_tp.id_tp_iog AND iog_tp.id_ct_iog = iog.fid_ct_iog AND iog_tp.id_cl_iog = iog.fid_cl_iog " +
                    "INNER JOIN erp.bpsu_bpb AS cob ON iog.fid_cob = cob.id_bpb " +
                    "INNER JOIN erp.cfgu_cob_ent AS ent ON iog.fid_cob = ent.id_cob AND iog.fid_wh = ent.id_ent " +
                    "WHERE iog.id_year = " + key[0] + " AND iog.id_doc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("iog.id_year");
                mnPkDocId = resultSet.getInt("iog.id_doc");
                mtDate = resultSet.getDate("iog.dt");
                msNumberSeries = resultSet.getString("iog.num_ser");
                msNumber = resultSet.getString("iog.num");
                msReference = resultSet.getString("iog.ref");
                mdValue_r = resultSet.getDouble("iog.val_r");
                mnCostAsigned = resultSet.getInt("iog.cst_asig");
                mnCostTransferred = resultSet.getInt("iog.cst_tran");
                mbIsShipmentRequired = resultSet.getBoolean("iog.b_ship_req");
                mbIsShipped = resultSet.getBoolean("iog.b_ship");
                mbIsAudited = resultSet.getBoolean("iog.b_audit");
                mbIsAuthorized = resultSet.getBoolean("iog.b_authorn");
                mbIsRecordAutomatic = resultSet.getBoolean("iog.b_rec_aut");
                mbIsSystem = resultSet.getBoolean("iog.b_sys");
                mbIsDeleted = resultSet.getBoolean("iog.b_del");
                mnFkDiogCategoryId = resultSet.getInt("iog.fid_ct_iog");
                mnFkDiogClassId = resultSet.getInt("iog.fid_cl_iog");
                mnFkDiogTypeId = resultSet.getInt("iog.fid_tp_iog");
                mnFkDiogAdjustmentTypeId = resultSet.getInt("iog.fid_tp_iog_adj");
                mnFkCompanyBranchId = resultSet.getInt("iog.fid_cob");
                mnFkWarehouseId = resultSet.getInt("iog.fid_wh");
                mnFkDpsYearId_n = resultSet.getInt("iog.fid_dps_year_n");
                mnFkDpsDocId_n = resultSet.getInt("iog.fid_dps_doc_n");
                mnFkDiogYearId_n = resultSet.getInt("iog.fid_diog_year_n");
                mnFkDiogDocId_n = resultSet.getInt("iog.fid_diog_doc_n");
                mnFkMfgYearId_n = resultSet.getInt("iog.fid_mfg_year_n");
                mnFkMfgOrderId_n = resultSet.getInt("iog.fid_mfg_ord_n");
                mnFkBookkeepingYearId_n = resultSet.getInt("iog.fid_bkk_year_n");
                mnFkBookkeepingNumberId_n = resultSet.getInt("iog.fid_bkk_num_n");
                mnFkMaintMovementTypeId = resultSet.getInt("iog.fid_maint_mov_tp");
                mnFkMaintUserId_n = resultSet.getInt("iog.fid_maint_user_n");
                mnFkMaintUserSupervisorId = resultSet.getInt("iog.fid_maint_user_supv");
                mnFkMaintReturnUserId_n = resultSet.getInt("iog.fid_maint_ret_user_n");
                mnFkMaintReturnUserSupervisorId = resultSet.getInt("iog.fid_maint_ret_user_supv");
                mnFkUserShippedId = resultSet.getInt("iog.fid_usr_ship");
                mnFkUserAuditedId = resultSet.getInt("iog.fid_usr_audit");
                mnFkUserAuthorizedId = resultSet.getInt("iog.fid_usr_authorn");
                mnFkUserNewId = resultSet.getInt("iog.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("iog.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("iog.fid_usr_del");
                mtUserShippedTs = resultSet.getTimestamp("iog.ts_ship");
                mtUserAuditedTs = resultSet.getTimestamp("iog.ts_audit");
                mtUserAuthorizedTs = resultSet.getTimestamp("iog.ts_authorn");
                mtUserNewTs = resultSet.getTimestamp("iog.ts_new");
                mtUserEditTs = resultSet.getTimestamp("iog.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("iog.ts_del");

                msDbmsDiogCategory = resultSet.getString("iog_ct.ct_iog");
                msDbmsDiogClass = resultSet.getString("iog_cl.cl_iog");
                msDbmsDiogType = resultSet.getString("iog_tp.tp_iog");
                msDbmsCompanyBranch = resultSet.getString("cob.bpb");
                msDbmsCompanyBranchCode = resultSet.getString("cob.code");
                msDbmsWarehouse = resultSet.getString("ent.ent");
                msDbmsWarehouseCode = resultSet.getString("ent.code");

                statementAux = statement.getConnection().createStatement();

                // Read aswell counterpart document if any:

                if (mnFkDiogYearId_n != SLibConstants.UNDEFINED && mnFkDiogDocId_n != SLibConstants.UNDEFINED) {
                    SDataDiog diog = new SDataDiog();
                    if (diog.read(new int[] { mnFkDiogYearId_n, mnFkDiogDocId_n }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        diog.setAuxIsDbmsDataCounterpartDiog(true);
                        moDbmsDataCounterpartDiog = diog;
                    }
                }

                // Read aswell document entries:

                sql = "SELECT id_year, id_doc, id_ety FROM trn_diog_ety WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " ORDER BY sort_pos ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataDiogEntry entry = new SDataDiogEntry();
                    if (entry.read(new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDiogEntries.add(entry);
                    }
                }

                // Read aswell document notes:

                sql = "SELECT id_year, id_doc, id_nts FROM trn_diog_nts WHERE id_year = " + key[0] + " AND id_doc = " + key[1] +" ORDER BY id_nts ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    SDataDiogNotes note = new SDataDiogNotes();
                    if (note.read(new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsDiogNotes.add(note);
                    }
                }

                // Read aswell the information of the accounting record:

                sql = "SELECT iogr.fid_rec_year, iogr.fid_rec_per, iogr.fid_rec_bkc, iogr.fid_rec_tp_rec, iogr.fid_rec_num, r.dt " +
                        "FROM trn_diog_rec AS iogr INNER JOIN fin_rec AS r ON " +
                        "iogr.fid_rec_year = r.id_year AND iogr.fid_rec_per = r.id_per AND iogr.fid_rec_bkc = r.id_bkc AND iogr.fid_rec_tp_rec = r.id_tp_rec AND iogr.fid_rec_num = r.id_num " +
                        "WHERE iogr.id_iog_year = " + key[0] + " AND iogr.id_iog_doc = " + key[1] + " ";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    moDbmsRecordKey = new Object[] {resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getInt(5) };
                    mtDbmsRecordDate = resultSet.getDate("r.dt");
                }
                
                // Read aswell, if any, last maintenance stock-movement's ID:
                
                mnDbmsLastDiogSignatureId = STrnMaintUtilities.getLastMaintDiogSignature(statement, (int[]) getPrimaryKey());

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        int nSortingPosition = 0;
        CallableStatement callableStatement = null;
        SDataBookkeepingNumber bookkeepingNumber = null;

        //System.out.println("SDataDiog: 0");
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            verifyDpsRelation();
            
            // Create bookkeeping number:

            //System.out.println("SDataDiog: 1a");
            if (!mbIsDeleted && !mbAuxIsDbmsDataCounterpartDiog) {
                //System.out.println("SDataDiog: 1a.1");
                bookkeepingNumber = new SDataBookkeepingNumber();
                bookkeepingNumber.setPkYearId(mnPkYearId);
                bookkeepingNumber.setFkUserNewId(mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
                if (bookkeepingNumber.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
                else {
                    mnFkBookkeepingYearId_n = bookkeepingNumber.getPkYearId();
                    mnFkBookkeepingNumberId_n = bookkeepingNumber.getPkNumberId();
                }
            }

            if (moDbmsDataCounterpartDiog != null) {
                moDbmsDataCounterpartDiog.setIsDeleted(mbIsDeleted);
                moDbmsDataCounterpartDiog.setReference(msNumber);
                moDbmsDataCounterpartDiog.setIsShipmentRequired(mbIsShipmentRequired);
                moDbmsDataCounterpartDiog.setIsShipped(mbIsShipped);
                moDbmsDataCounterpartDiog.setIsAudited(mbIsAudited);
                moDbmsDataCounterpartDiog.setIsAuthorized(mbIsAuthorized);
                moDbmsDataCounterpartDiog.setFkUserShippedId(mnFkUserShippedId);
                moDbmsDataCounterpartDiog.setFkUserAuditedId(mnFkUserAuditedId);
                moDbmsDataCounterpartDiog.setFkUserAuthorizedId(mnFkUserAuthorizedId);
                moDbmsDataCounterpartDiog.setFkUserNewId(mnFkUserNewId);
                moDbmsDataCounterpartDiog.setFkUserEditId(mnFkUserEditId);
                moDbmsDataCounterpartDiog.setFkUserDeleteId(mnFkUserDeleteId);
                moDbmsDataCounterpartDiog.setFkBookkeepingYearId_n(mnFkBookkeepingYearId_n);
                moDbmsDataCounterpartDiog.setFkBookkeepingNumberId_n(mnFkBookkeepingNumberId_n);
                moDbmsDataCounterpartDiog.setFkMaintMovementTypeId(mnFkMaintMovementTypeId);
                moDbmsDataCounterpartDiog.setFkMaintUserId_n(mnFkMaintUserId_n);
                moDbmsDataCounterpartDiog.setFkMaintUserSupervisorId(mnFkMaintUserSupervisorId);
                moDbmsDataCounterpartDiog.setFkMaintReturnUserId_n(mnFkMaintReturnUserId_n);
                moDbmsDataCounterpartDiog.setFkMaintReturnUserSupervisorId(mnFkMaintReturnUserSupervisorId);

                if (moDbmsDataCounterpartDiog.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
                else {
                    mnFkDiogYearId_n = moDbmsDataCounterpartDiog.getPkYearId();
                    mnFkDiogDocId_n = moDbmsDataCounterpartDiog.getPkDocId();
                }
            }

            calculateTotal();
            
            //System.out.println("SDataDiog: 1b");
            callableStatement = connection.prepareCall(
                    "{ CALL trn_diog_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkDocId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setString(nParam++, msNumberSeries);
            callableStatement.setString(nParam++, msNumber);
            callableStatement.setString(nParam++, msReference);
            callableStatement.setDouble(nParam++, mdValue_r);
            callableStatement.setInt(nParam++, mnCostAsigned);
            callableStatement.setInt(nParam++, mnCostTransferred);
            callableStatement.setBoolean(nParam++, mbIsShipmentRequired);
            callableStatement.setBoolean(nParam++, mbIsShipped);
            callableStatement.setBoolean(nParam++, mbIsAudited);
            callableStatement.setBoolean(nParam++, mbIsAuthorized);
            callableStatement.setBoolean(nParam++, mbIsRecordAutomatic);
            callableStatement.setBoolean(nParam++, mbIsSystem);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkDiogCategoryId);
            callableStatement.setInt(nParam++, mnFkDiogClassId);
            callableStatement.setInt(nParam++, mnFkDiogTypeId);
            callableStatement.setInt(nParam++, mnFkDiogAdjustmentTypeId);
            callableStatement.setInt(nParam++, mnFkCompanyBranchId);
            callableStatement.setInt(nParam++, mnFkWarehouseId);
            if (mnFkDpsYearId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDpsYearId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            if (mnFkDpsDocId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDpsDocId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkDiogYearId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDiogYearId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            if (mnFkDiogDocId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkDiogDocId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkMfgYearId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMfgYearId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            if (mnFkMfgOrderId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMfgOrderId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            if (mnFkBookkeepingYearId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkBookkeepingYearId_n); else callableStatement.setNull(nParam++, Types.SMALLINT);
            if (mnFkBookkeepingNumberId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkBookkeepingNumberId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkMaintMovementTypeId);
            if (mnFkMaintUserId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMaintUserId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkMaintUserSupervisorId);
            if (mnFkMaintReturnUserId_n != SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkMaintReturnUserId_n); else callableStatement.setNull(nParam++, Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkMaintReturnUserSupervisorId);
            callableStatement.setInt(nParam++, mnFkUserShippedId);
            callableStatement.setInt(nParam++, mnFkUserAuditedId);
            callableStatement.setInt(nParam++, mnFkUserAuthorizedId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.setBoolean(nParam++, false);  // XXX is being shipped
            callableStatement.setBoolean(nParam++, false);  // XXX is being audited
            callableStatement.setBoolean(nParam++, false);  // XXX is being authorized
            callableStatement.registerOutParameter(nParam++, Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, Types.VARCHAR);
            callableStatement.execute();

            //System.out.println("SDataDiog: 2");
            mnPkDocId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            //System.out.println("SDataDiog: 3");
            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
                // Save aswell document entries:

                //System.out.println("SDataDiog: 3.1");
                for (SDataDiogEntry entry : mvDbmsDiogEntries) {
                    if (entry.getIsRegistryNew() || entry.getIsRegistryEdited()) {
                        entry.setPkYearId(mnPkYearId);
                        entry.setPkDocId(mnPkDocId);

                        if (entry.getIsDeleted()) {
                            entry.setSortingPosition(0);
                        }
                        else {
                            entry.setSortingPosition(++nSortingPosition);
                        }

                        //System.out.println("SDataDiog: 3.1.1");
                        if (entry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save aswell document notes:

                //System.out.println("SDataDiog: 3.2");
                for (SDataDiogNotes notes : mvDbmsDiogNotes) {
                    if (notes.getIsRegistryNew() || notes.getIsRegistryEdited()) {
                        notes.setPkYearId(mnPkYearId);
                        notes.setPkDocId(mnPkDocId);

                        //System.out.println("SDataDiog: 3.2.1");
                        if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Aditional processing:

                //System.out.println("SDataDiog: 3.3");
                computeBookkeeping(connection);
                //System.out.println("SDataDiog: 3.4");
                computeStock(connection);
                //System.out.println("SDataDiog: 3.5");
                
                // sign DIOG:
                if (mbAuxSignDiog) {
                    int user = SLibConstants.UNDEFINED;
                    int userSupv = SLibConstants.UNDEFINED;
                    
                    switch (mnFkMaintMovementTypeId) {
                        case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART:
                        case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_TOOL:
                        case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT:
                        case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT:
                        case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST:
                            user = mnFkMaintReturnUserId_n;
                            userSupv = mnFkMaintReturnUserSupervisorId;
                            break;
                        case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART:
                        case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_TOOL:
                        case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT:
                        case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT:
                        case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST:
                            user = mnFkMaintUserId_n;
                            userSupv = mnFkMaintUserSupervisorId;
                            break;
                        default:
                    }
                    
                    String sql;
                    
                    sql = "SET @id = (SELECT COALESCE(MAX(id_maint_diog_sig), 0) + 1 FROM trn_maint_diog_sig);";
                    connection.createStatement().execute(sql);
                    
                    sql = "INSERT INTO trn_maint_diog_sig "
                            + "VALUES (@id, " + mnPkYearId + ", " + mnPkDocId + ", " + user + ", " + userSupv + ", " + (mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId) + ", NOW());";
                    connection.createStatement().execute(sql);
                }
                
                // release the stock in segregations:
                if (moAuxSegregationStockId != SLibConstants.UNDEFINED && mnFkDiogCategoryId == SDataConstantsSys.TRNS_CT_IOG_OUT) {
                    releaseSegregations(connection, moAuxSegregationStockId, mvDbmsDiogEntries, mnFkCompanyBranchId, mnFkWarehouseId);
                }
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
    
    public void calculateTotal() {
        mdValue_r = 0;
        
        for (SDataDiogEntry entry : mvDbmsDiogEntries) {
            if (!entry.getIsDeleted()) {
                entry.calculateTotal();
                
                mdValue_r = SLibUtilities.round(mdValue_r + entry.getValue(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            }
        }
    }

    @Override
    public int canDelete(java.sql.Connection connection) {
        String msg = "No se puede eliminar el documento: ";

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsSystem) {
                mnDbmsErrorId = 2;
                msDbmsError = msg + "¡El documento es de sistema!";
                throw new Exception(msDbmsError);
            }
            /*
            else if (mbIsDeleted) {
                mnDbmsErrorId = 3;
                msDbmsError = msg + "¡El documento está eliminado!";
                throw new Exception(msDbmsError);
            }
            */
            else if (mbIsAudited) {
                mnDbmsErrorId = 31;
                msDbmsError = msg + "¡El documento está auditado!";
                throw new Exception(msDbmsError);
            }
            else if (mbIsAuthorized) {
                mnDbmsErrorId = 32;
                msDbmsError = msg + "¡El documento está autorizado!";
                throw new Exception(msDbmsError);
            }
            else if (mbIsShipped) {
                mnDbmsErrorId = 33;
                msDbmsError = msg + "¡El documento está embarcado!";
                throw new Exception(msDbmsError);
            }
            else {
                for (SDataDiogEntry entry : mvDbmsDiogEntries) {
                    if (entry.hasDiogLinksShipment()) {
                        mnDbmsErrorId = 41;
                        msDbmsError = msg + "¡El documento está asociado a un documento de embarques!";
                        throw new Exception(msDbmsError);
                    }
                    else {
                        mnLastDbActionResult = SLibConstants.DB_CAN_DELETE_YES;
                    }
                }
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_DELETE_NO;
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_CAN_DELETE_NO;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int delete(java.sql.Connection connection) {
        String sMsg = "No se puede eliminar el documento: ";

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryRequestDelete) {
                // Set document as deleted:

                if (mbIsDeleted) {
                    mnDbmsErrorId = 1;
                    msDbmsError = sMsg + "El documento ya está marcado como eliminado.";
                    throw new Exception(msDbmsError);
                }
                else {
                    mbIsDeleted = true;

                    if (save(connection) == SLibConstants.DB_ACTION_SAVE_OK) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
                    }
                }
            }
            else {
                // Revert DPS deletion:

                if (!mbIsDeleted) {
                    mnDbmsErrorId = 2;
                    msDbmsError = sMsg + "El documento ya está desmarcado como eliminado.";
                    throw new Exception(msDbmsError);
                }
                else {
                    mbIsDeleted = false;

                    if (save(connection) == SLibConstants.DB_ACTION_SAVE_OK) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
                    }
                    else {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
                    }
                }
            }
        }
        catch (SQLException exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, exception);
        }
        catch (Exception exception) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, exception);
        }

        return mnLastDbActionResult;
    }

    public java.lang.String validateStockLots(final erp.client.SClientInterface client, final boolean isDocBeingDeleted) throws Exception {
        return STrnStockValidator.validateStockLots(client, mvDbmsDiogEntries, mnFkDiogCategoryId, isDocBeingDeleted);
    }

    public java.lang.String validateStockMoves(final erp.client.SClientInterface client, final boolean isDocBeingDeleted) throws Exception {
        return STrnStockValidator.validateStockMoves(client, mvDbmsDiogEntries, mnFkDiogCategoryId, (int[]) getPrimaryKey(), getWarehouseKey(), isDocBeingDeleted, mtDate, SLibConstants.UNDEFINED, null, mnFkMaintUserId_n);
    }

    @Override
    public SDataDiog clone() throws CloneNotSupportedException {
        SDataDiog registry = new SDataDiog();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkDocId(this.getPkDocId());
        registry.setDate(this.getDate());
        registry.setNumberSeries(this.getNumberSeries());
        registry.setNumber(this.getNumber());
        registry.setReference(this.getReference());
        registry.setValue_r(this.getValue_r());
        registry.setCostAsigned(this.getCostAsigned());
        registry.setCostTransferred(this.getCostTransferred());
        registry.setIsShipmentRequired(this.getIsShipmentRequired());
        registry.setIsShipped(this.getIsShipped());
        registry.setIsAudited(this.getIsAudited());
        registry.setIsAuthorized(this.getIsAuthorized());
        registry.setIsRecordAutomatic(this.getIsRecordAutomatic());
        registry.setIsSystem(this.getIsSystem());
        registry.setIsDeleted(this.getIsDeleted());
        registry.setFkDiogCategoryId(this.getFkDiogCategoryId());
        registry.setFkDiogClassId(this.getFkDiogClassId());
        registry.setFkDiogTypeId(this.getFkDiogTypeId());
        registry.setFkDiogAdjustmentTypeId(this.getFkDiogAdjustmentTypeId());
        registry.setFkCompanyBranchId(this.getFkCompanyBranchId());
        registry.setFkWarehouseId(this.getFkWarehouseId());
        registry.setFkDpsYearId_n(this.getFkDpsYearId_n());
        registry.setFkDpsDocId_n(this.getFkDpsDocId_n());
        registry.setFkDiogYearId_n(this.getFkDiogYearId_n());
        registry.setFkDiogDocId_n(this.getFkDiogDocId_n());
        registry.setFkMfgYearId_n(this.getFkMfgYearId_n());
        registry.setFkMfgOrderId_n(this.getFkMfgOrderId_n());
        registry.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        registry.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());
        registry.setFkMaintMovementTypeId(this.getFkMaintMovementTypeId());
        registry.setFkMaintUserId_n(this.getFkMaintUserId_n());
        registry.setFkMaintUserSupervisorId(this.getFkMaintUserSupervisorId());
        registry.setFkMaintReturnUserId_n(this.getFkMaintReturnUserId_n());
        registry.setFkMaintReturnUserSupervisorId(this.getFkMaintReturnUserSupervisorId());
        registry.setFkUserShippedId(this.getFkUserShippedId());
        registry.setFkUserAuditedId(this.getFkUserAuditedId());
        registry.setFkUserAuthorizedId(this.getFkUserAuthorizedId());
        registry.setFkUserNewId(this.getFkUserNewId());
        registry.setFkUserEditId(this.getFkUserEditId());
        registry.setFkUserDeleteId(this.getFkUserDeleteId());
        registry.setUserShippedTs(this.getUserShippedTs());
        registry.setUserAuditedTs(this.getUserAuditedTs());
        registry.setUserAuthorizedTs(this.getUserAuthorizedTs());
        registry.setUserNewTs(this.getUserNewTs());
        registry.setUserEditTs(this.getUserEditTs());
        registry.setUserDeleteTs(this.getUserDeleteTs());

        registry.setDbmsLastDiogSignatureId(this.getDbmsLastDiogSignatureId());
        registry.setDbmsDiogCategory(this.getDbmsDiogCategory());
        registry.setDbmsDiogClass(this.getDbmsDiogClass());
        registry.setDbmsDiogType(this.getDbmsDiogType());
        registry.setDbmsCompanyBranch(this.getDbmsCompanyBranch());
        registry.setDbmsWarehouse(this.getDbmsWarehouse());
        registry.setDbmsRecordKey(this.getDbmsRecordKey());
        registry.setDbmsRecordDate(this.getDbmsRecordDate());

        for (SDataDiogEntry entry : mvDbmsDiogEntries) {
            registry.getDbmsEntries().add(entry.clone());
        }

        for (SDataDiogNotes notes : mvDbmsDiogNotes) {
            registry.getDbmsNotes().add(notes.clone());
        }

        registry.setAuxIsDbmsDataCounterpartDiog(this.getAuxIsDbmsDataCounterpartDiog());
        registry.setAuxSignDiog(this.getAuxSignDiog());
        registry.setAuxSegregationStockId(this.getAuxSegregationStockId());
    
        return registry;
    }
}
