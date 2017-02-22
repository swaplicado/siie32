/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import erp.client.SClientInterface;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mtrn.data.SDataStockLot;
import erp.mtrn.data.STrnStockSegregationUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import sa.lib.SLibUtils;

/**
 *
 * @author Néstor Ávalos, Edwin Carmona
 */
public class SDataProductionOrder extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkOrderId;
    protected java.lang.String msNumber;
    protected java.lang.String msReference;
    protected double mdQuantityOriginal;
    protected double mdQuantityRework;
    protected double mdQuantity;
    protected int mnCharges;
    protected java.lang.String msNumberReference_n;
    protected java.util.Date mtDate;
    protected java.util.Date mtDateDelivery;
    protected java.util.Date mtDateLot_n;
    protected java.util.Date mtDateLotAssigned_n;
    protected java.util.Date mtDateStart_n;
    protected java.util.Date mtDateEnd_n;
    protected java.util.Date mtDateClose_n;
    protected java.util.Date mtTsLotAssigned_n;
    protected java.util.Date mtTsStart_n;
    protected java.util.Date mtTsEnd_n;
    protected java.util.Date mtTsClose_n;
    protected int mnIsCostDone;
    protected boolean mbIsConsume;
    protected boolean mbIsForecast;
    protected boolean mbIsProgrammed;
    protected boolean mbIsDeleted;
    protected int mnFkOrdTypeId;
    protected int mnFkBomId;
    protected int mnFkItemId_r;
    protected int mnFkUnitId_r;
    protected int mnFkCobId;
    protected int mnFkEntityId;
    protected int mnFkOrdPriorityId;
    protected int mnFkOrdStatusId;
    protected int mnFkOrdYearId_n;
    protected int mnFkOrdId_n;
    protected int mnFkBizPartnerId_n;
    protected int mnFkBizPartnerOperatorId_n;
    protected int mnFkLotItemId_nr;
    protected int mnFkLotUnitId_nr;
    protected int mnFkLotId_n;
    protected int mnFkInventoryValuationId_n;
    protected int mnFkTurnDeliveryId;
    protected int mnFkTurnLotAssignedId;
    protected int mnFkUserLotAssignedId;
    protected int mnFkTurnStartId;
    protected int mnFkUserStartId;
    protected int mnFkTurnEndId;
    protected int mnFkUserEndId;
    protected int mnFkTurnCloseId;
    protected int mnFkUserCloseId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected boolean mbDbmsIsLot;
    protected boolean mbDbmsIsExploded;
    protected int mnDbmsFkCompanyBranchId;
    protected int mnDbmsFkWarehouseId;
    protected java.lang.String msDbmsNumber;
    protected java.lang.String msDbmsProductionOrder_n;
    protected java.lang.String msDbmsCompanyBranch;
    protected java.lang.String msDbmsCompanyBranchCode;
    protected java.lang.String msDbmsEntity;
    protected java.lang.String msDbmsEntityCode;
    protected java.lang.String msDbmsExplotionMaterialsReference;
    protected java.lang.String msDbmsExplotionCompanyBranch;
    protected java.lang.String msDbmsExplotionCompanyBranchCode;
    protected java.lang.String msDbmsExplotionWarehouse;
    protected java.lang.String msDbmsExplotionWarehouseCode;
    protected java.lang.String msDbmsFinishedGood;
    protected java.lang.String msDbmsBom;
    protected java.lang.String msDbmsBomUnitSymbol;
    protected java.lang.String msDbmsProductionOrderStatus;
    protected java.lang.String msDbmsProductionOrderType;
    protected java.lang.String msDbmsNumberFather;
    protected java.lang.String msDbmsLotItem;
    protected java.lang.String msDbmsLotUnit;
    protected java.lang.String msDbmsLot;
    protected java.lang.String msDbmsUserLotAssigned;
    protected java.lang.String msDbmsUserStart;
    protected java.lang.String msDbmsUserEnd;
    protected java.lang.String msDbmsUserClose;
    protected java.util.Date mtDbmsLotDateExpired;

    protected erp.mmfg.data.SDataExplotionMaterials moDbmsExplotionMaterials;

    protected java.util.Vector<erp.mmfg.data.SDataProductionOrderCharge> mvDbmsProductionOrderCharges;
    protected java.util.Vector<erp.mmfg.data.SDataProductionOrderPeriod> mvDbmsProductionOrderPeriods;
    protected java.util.Vector<erp.mmfg.data.SDataProductionOrderNotes> mvDbmsProductionOrderNotes;

    protected ArrayList<SDataProductionOrder> maAuxProductionOrderRelations;

    public SDataProductionOrder() {
        super(SDataConstants.MFG_ORD);

        mvDbmsProductionOrderCharges = new Vector<SDataProductionOrderCharge>();
        mvDbmsProductionOrderPeriods = new Vector<SDataProductionOrderPeriod>();
        mvDbmsProductionOrderNotes = new Vector<SDataProductionOrderNotes>();

        maAuxProductionOrderRelations = new ArrayList<SDataProductionOrder>();

        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkOrdId(int n) { mnPkOrderId = n; }
    public void setNumber(java.lang.String s) { msNumber = s; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setQuantityOriginal(double d) { mdQuantityOriginal = d; }
    public void setQuantityRework(double d) { mdQuantityRework = d; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setCharges(int n) { mnCharges = n; }
    public void setNumberReference_n(java.lang.String s) { msNumberReference_n = s; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setDateDelivery(java.util.Date t) { mtDateDelivery = t; }
    public void setDateLot_n(java.util.Date t) { mtDateLot_n = t; }
    public void setDateLotAssigned_n(java.util.Date t) { mtDateLotAssigned_n = t; }
    public void setDateStart_n(java.util.Date t) { mtDateStart_n = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setDateClose_n(java.util.Date t) { mtDateClose_n = t; }
    public void setTsLotAssigned_n(java.util.Date t) { mtTsLotAssigned_n = t; }
    public void setTsStart_n(java.util.Date t) { mtTsStart_n = t; }
    public void setTsEnd_n(java.util.Date t) { mtTsEnd_n = t; }
    public void setTsClose_n(java.util.Date t) { mtTsClose_n = t; }
    public void setIsCostDone(int n) { mnIsCostDone = n; }
    public void setIsConsume(boolean b) { mbIsConsume = b; }
    public void setIsForecast(boolean b) { mbIsForecast = b; }
    public void setIsProgrammed(boolean b) { mbIsProgrammed = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkOrdTypeId(int n) { mnFkOrdTypeId = n; }
    public void setFkBomId(int n) { mnFkBomId = n; }
    public void setFkItemId_r(int n) { mnFkItemId_r = n; }
    public void setFkUnitId_r(int n) { mnFkUnitId_r = n; }
    public void setFkCobId(int n) { mnFkCobId = n; }
    public void setFkEntityId(int n) { mnFkEntityId = n; }
    public void setFkOrdPriorityId(int n) { mnFkOrdPriorityId = n; }
    public void setFkOrdStatusId(int n) { mnFkOrdStatusId = n; }
    public void setFkOrdYearId_n(int n) { mnFkOrdYearId_n = n; }
    public void setFkOrdId_n(int n) { mnFkOrdId_n = n; }
    public void setFkBizPartnerId_n(int n) { mnFkBizPartnerId_n = n; }
    public void setFkBizPartnerOperatorId_n(int n) { mnFkBizPartnerOperatorId_n = n; }
    public void setFkLotItemId_nr(int n) { mnFkLotItemId_nr = n; }
    public void setFkLotUnitId_nr(int n) { mnFkLotUnitId_nr = n; }
    public void setFkLotId_n(int n) { mnFkLotId_n = n; }
    public void setFkInventoryValuationId_n(int n) { mnFkInventoryValuationId_n = n; }
    public void setFkTurnDeliveryId(int n) { mnFkTurnDeliveryId = n; }
    public void setFkTurnLotAssignedId(int n) { mnFkTurnLotAssignedId = n; }
    public void setFkUserLotAssignedId(int n) { mnFkUserLotAssignedId = n; }
    public void setFkTurnStartId(int n) { mnFkTurnStartId = n; }
    public void setFkUserStartId(int n) { mnFkUserStartId = n; }
    public void setFkTurnEndId(int n) { mnFkTurnEndId = n; }
    public void setFkUserEndId(int n) { mnFkUserEndId = n; }
    public void setFkTurnCloseId(int n) { mnFkTurnCloseId = n; }
    public void setFkUserCloseId(int n) { mnFkUserCloseId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkOrdId() { return mnPkOrderId; }
    public java.lang.String getNumber() { return msNumber; }
    public java.lang.String getReference() { return msReference; }
    public double getQuantityOriginal() { return mdQuantityOriginal; }
    public double getQuantityRework() { return mdQuantityRework; }
    public double getQuantity() { return mdQuantity; }
    public int getCharges() { return mnCharges; }
    public java.lang.String getNumberReference_n() { return msNumberReference_n; }
    public java.util.Date getDate() { return mtDate; }
    public java.util.Date getDateDelivery() { return mtDateDelivery; }
    public java.util.Date getDateLot_n() { return mtDateLot_n; }
    public java.util.Date getDateLotAssigned_n() { return mtDateLotAssigned_n; }
    public java.util.Date getDateStart_n() { return mtDateStart_n; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public java.util.Date getDateClose_n() { return mtDateClose_n; }
    public java.util.Date getTsLotAssigned_n() { return mtTsLotAssigned_n; }
    public java.util.Date getTsStart_n() { return mtTsStart_n; }
    public java.util.Date getTsEnd_n() { return mtTsEnd_n; }
    public java.util.Date getTsClose_n() { return mtTsClose_n; }
    public int getIsCostDone() { return mnIsCostDone; }
    public boolean getIsConsume() { return mbIsConsume; }
    public boolean getIsForecast() { return mbIsForecast; }
    public boolean getIsProgrammed() { return mbIsProgrammed; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkOrdTypeId() { return mnFkOrdTypeId; }
    public int getFkBomId() { return mnFkBomId; }
    public int getFkItemId_r() { return mnFkItemId_r; }
    public int getFkUnitId_r() { return mnFkUnitId_r; }
    public int getFkCobId() { return mnFkCobId; }
    public int getFkEntityId() { return mnFkEntityId; }
    public int getFkOrdPriorityId() { return mnFkOrdPriorityId; }
    public int getFkOrdStatusId() { return mnFkOrdStatusId; }
    public int getFkOrdYearId_n() { return mnFkOrdYearId_n; }
    public int getFkOrdId_n() { return mnFkOrdId_n; }
    public int getFkBizPartnerId_n() { return mnFkBizPartnerId_n; }
    public int getFkBizPartnerOperatorId_n() { return mnFkBizPartnerOperatorId_n; }
    public int getFkLotItemId_nr() { return mnFkLotItemId_nr; }
    public int getFkLotUnitId_nr() { return mnFkLotUnitId_nr; }
    public int getFkLotId_n() { return mnFkLotId_n; }
    public int getFkInventoryValuationId_n() { return mnFkInventoryValuationId_n; }
    public int getFkTurnDeliveryId() { return mnFkTurnDeliveryId; }
    public int getFkTurnLotAssignedId() { return mnFkTurnLotAssignedId; }
    public int getFkUserLotAssignedId() { return mnFkUserLotAssignedId; }
    public int getFkTurnStartId() { return mnFkTurnStartId; }
    public int getFkUserStartId() { return mnFkUserStartId; }
    public int getFkTurnEndId() { return mnFkTurnEndId; }
    public int getFkUserEndId() { return mnFkUserEndId; }
    public int getFkTurnCloseId() { return mnFkTurnCloseId; }
    public int getFkUserCloseId() { return mnFkUserCloseId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsIsLot(boolean b) { mbDbmsIsLot = b; }
    public void setDbmsBomUnitSymbol(java.lang.String s) { msDbmsBomUnitSymbol = s; }
    public void setDbmsStatus(java.lang.String s) { msDbmsProductionOrderStatus = s; }
    public void setDbmsLot(java.lang.String s) { msDbmsLot = s; }
    public void setDbmsLotDateExpired(java.util.Date t) { mtDbmsLotDateExpired = t; }

    public boolean getDbmsIsExploded() { return mbDbmsIsExploded; }
    public int getDbmsFkCompanyBranchId() { return mnDbmsFkCompanyBranchId; }
    public int getDbmsFkWarehouseId() { return mnDbmsFkWarehouseId; }
    public java.lang.String getDbmsNumber() { return msDbmsNumber; }
    public java.lang.String getDbmsProductionOrder_n() { return msDbmsProductionOrder_n; }
    public java.lang.String getDbmsCompanyBranch() { return msDbmsCompanyBranch; }
    public java.lang.String getDbmsCompanyBranchCode() { return msDbmsCompanyBranchCode; }
    public java.lang.String getDbmsEntity() { return msDbmsEntity; }
    public java.lang.String getDbmsEntityCode() { return msDbmsEntityCode; }
    public java.lang.String getDbmsExplotionMaterialsReference() { return msDbmsExplotionMaterialsReference; }
    public java.lang.String getDbmsExplotionCompanyBranch() { return msDbmsExplotionCompanyBranch; }
    public java.lang.String getDbmsExplotionCompanyBranchCode() { return msDbmsExplotionCompanyBranchCode; }
    public java.lang.String getDbmsExplotionWarehouse() { return msDbmsExplotionWarehouse; }
    public java.lang.String getDbmsExplotionWarehouseCode() { return msDbmsExplotionWarehouseCode; }
    public java.lang.String getDbmsFinishedGood() { return msDbmsFinishedGood; }
    public java.lang.String getDbmsBom() { return msDbmsBom; }
    public java.lang.String getDbmsBomUnitSymbol() { return msDbmsBomUnitSymbol; }
    public java.lang.String getDbmsProductionOrderStatus() { return msDbmsProductionOrderStatus; }
    public java.lang.String getDbmsProductionOrderType() { return msDbmsProductionOrderType; }
    public java.lang.String getDbmsNumberFather() { return msDbmsNumberFather; }
    public java.lang.String getDbmsLotItem() { return msDbmsLotItem; }
    public java.lang.String getDbmsLotUnit() { return msDbmsLotUnit; }
    public java.lang.String getDbmsLot() { return msDbmsLot; }
    public java.lang.String getDbmsUserLotAssigned() { return  msDbmsUserLotAssigned; }
    public java.lang.String getDbmsUserStart() { return  msDbmsUserStart; }
    public java.lang.String getDbmsUserEnd() { return  msDbmsUserEnd; }
    public java.lang.String getDbmsUserClose() { return  msDbmsUserClose; }
    public java.util.Date getDbmsLotDateExpired() { return mtDbmsLotDateExpired; }

    public erp.mmfg.data.SDataExplotionMaterials getDbmsExplotionMaterials() { return moDbmsExplotionMaterials; }

    public java.util.Vector<SDataProductionOrderCharge> getDbmsProductionOrderCharges() { return mvDbmsProductionOrderCharges; }
    public java.util.Vector<SDataProductionOrderPeriod> getDbmsProductionOrderPeriods() { return mvDbmsProductionOrderPeriods; }
    public java.util.Vector<SDataProductionOrderNotes> getDbmsProductionOrderNotes() { return mvDbmsProductionOrderNotes; }

    public ArrayList<SDataProductionOrder> getAuxProductionOrdersRelations() { return maAuxProductionOrderRelations; }

    public int[] getParentProductionOrderKey() { return new int[] { mnFkOrdYearId_n, mnFkOrdId_n }; }
    
    /**
     * This function marks the production order as programmed and done this, segregates what it contains
     * 
     * @param client
     * @param isMassProgramming This attribute indicates whether the function call comes from a mass programming
     */
    public void programProductionOrder(final SClientInterface client, final boolean isMassProgramming) {
        try {
            if (!mbIsProgrammed) {
                if (isMassProgramming || mbDbmsIsExploded) {
                    mbIsProgrammed = true;
                    save(client.getSession().getStatement().getConnection());
                    
                    // segregate items from production order
                    STrnStockSegregationUtils.segregate(client, new int[] { mnPkYearId, mnPkOrderId }, SDataConstantsSys.TRNS_TP_STK_SEG_MFG_ORD);
                }
                else {
                    client.showMsgBoxWarning("La orden de producción '" + msDbmsNumber + " - " + msReference + "', aún no es explosionada.");
                }
            }
            else {
                if (!isMassProgramming && client.showMsgBoxConfirm("La orden producción '" + msDbmsNumber + " - " + msReference + "' ya está programada,\n¿desea desprogramarla?\n") == JOptionPane.YES_OPTION) {
                    mbIsProgrammed = false;
                    save(client.getSession().getStatement().getConnection());
                    
                    // releasea segregation by production order
                    STrnStockSegregationUtils.releaseSegregation(client, new int[] { mnPkYearId, mnPkOrderId }, SDataConstantsSys.TRNS_TP_STK_SEG_MFG_ORD);
                }
            }
        }
        catch (SQLException ex) {
            SLibUtils.showException(client, ex);
        }
     }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkOrderId = 0;
        msNumber = "";
        msReference = "";
        mdQuantityOriginal = 0;
        mdQuantityRework = 0;
        mdQuantity = 0;
        mnCharges = 0;
        msNumberReference_n = "";
        mtDate = null;
        mtDateDelivery = null;
        mtDateLot_n = null;
        mtDateLotAssigned_n = null;
        mtDateStart_n = null;
        mtDateEnd_n = null;
        mtDateClose_n = null;
        mtTsLotAssigned_n = null;
        mtTsStart_n = null;
        mtTsEnd_n = null;
        mtTsClose_n = null;
        mnIsCostDone = 0;
        mbIsConsume = false;
        mbIsForecast = false;
        mbIsProgrammed = false;
        mbIsDeleted = false;
        mnFkOrdTypeId = 0;
        mnFkBomId = 0;
        mnFkItemId_r = 0;
        mnFkUnitId_r = 0;
        mnFkCobId = 0;
        mnFkEntityId = 0;
        mnFkOrdPriorityId = 0;
        mnFkOrdStatusId = 0;
        mnFkOrdYearId_n = 0;
        mnFkOrdId_n = 0;
        mnFkBizPartnerId_n = 0;
        mnFkBizPartnerOperatorId_n = 0;
        mnFkLotItemId_nr = 0;
        mnFkLotUnitId_nr = 0;
        mnFkLotId_n = 0;
        mnFkInventoryValuationId_n = 0;
        mnFkTurnDeliveryId = 0;
        mnFkTurnLotAssignedId = 0;
        mnFkUserLotAssignedId = 0;
        mnFkTurnStartId = 0;
        mnFkUserStartId = 0;
        mnFkTurnEndId = 0;
        mnFkUserEndId = 0;
        mnFkTurnCloseId = 0;
        mnFkUserCloseId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mbDbmsIsExploded = false;
        msDbmsProductionOrder_n = "";
        msDbmsCompanyBranch = "";
        msDbmsCompanyBranchCode = "";
        msDbmsEntity = "";
        msDbmsEntityCode = "";
        msDbmsExplotionMaterialsReference = "";
        msDbmsExplotionCompanyBranch = "";
        msDbmsExplotionCompanyBranchCode = "";
        msDbmsExplotionWarehouse = "";
        msDbmsExplotionWarehouseCode = "";
        msDbmsFinishedGood = "";
        msDbmsBom = "";
        msDbmsBomUnitSymbol = "";
        msDbmsProductionOrderStatus = "";
        msDbmsProductionOrderType = "";
        msDbmsNumberFather = "";
        msDbmsUserLotAssigned = "";
        msDbmsUserStart = "";
        msDbmsUserEnd = "";
        msDbmsUserClose = "";
        msDbmsLotItem = "";
        msDbmsLotUnit = "";
        msDbmsLot = "";
        mtDbmsLotDateExpired = null;

        moDbmsExplotionMaterials = null;

        mvDbmsProductionOrderCharges.clear();
        mvDbmsProductionOrderPeriods.clear();
        mvDbmsProductionOrderNotes.clear();

        maAuxProductionOrderRelations.clear();
    }

    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkYearId = ((int[]) key)[0];
        mnPkOrderId = ((int[]) key)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkOrderId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";

        ResultSet resultSet = null;
        Statement statementAux = null;

        SDataProductionOrderCharge oProductionOrderCharge = null;
        SDataProductionOrderPeriod oProductionOrderPeriod = null;
        SDataProductionOrderNotes oProductionOrderNotes = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT o.*, CONCAT(o.id_year, '-',o.num) AS f_num, COALESCE(CONCAT(of.id_year, '-',of.num), '(N/A)') AS f_num_father, " +
                    "u.symbol, s.st, b.qty, ul.usr, CONCAT(i.item_key, ' - ', i.item) AS f_item, b.bom, t.tp, bpb.bpb, bpb.code, ent.ent, ent.code, " +
                    "CONCAT(il.item_key, ' - ', il.item) AS f_lot_item, unl.symbol AS f_lot_unit, of.ref, us.usr, uf.usr, uc.usr, l.* " +
                "FROM mfg_ord AS o " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON o.fid_cob = bpb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON o.fid_cob = ent.id_cob AND o.fid_ent = ent.id_ent " +
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
                "INNER JOIN erp.mfgs_st_ord AS s ON o.fid_st_ord = s.id_st " +
                "INNER JOIN mfg_bom AS b ON o.fid_bom = b.id_bom " +
                "INNER JOIN erp.mfgu_tp_ord AS t ON o.fid_tp_ord = t.id_tp " +
                "INNER JOIN erp.usru_usr AS ul ON o.fid_usr_lot_asg = ul.id_usr " +
                "INNER JOIN erp.usru_usr AS us ON o.fid_usr_start = us.id_usr " +
                "INNER JOIN erp.usru_usr AS uf ON o.fid_usr_end = uf.id_usr " +
                "INNER JOIN erp.usru_usr AS uc ON o.fid_usr_close = uc.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON o.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON o.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON o.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_lot AS l ON o.fid_lot_item_nr = l.id_item AND o.fid_lot_unit_nr = l.id_unit AND o.fid_lot_n = l.id_lot " +
                "LEFT OUTER JOIN erp.itmu_item AS il ON l.id_item = il.id_item " +
                "LEFT OUTER JOIN erp.itmu_unit AS unl ON l.id_unit = unl.id_unit " +
                "LEFT OUTER JOIN mfg_ord AS of ON o.fid_ord_year_n = of.id_year AND o.fid_ord_n = of.id_ord " +
                "WHERE o.id_year = " + key[0] + " AND o.id_ord = " + key[1] + " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("o.id_year");
                mnPkOrderId = resultSet.getInt("o.id_ord");
                msNumber = resultSet.getString("o.num");
                msReference = resultSet.getString("o.ref");
                mdQuantityOriginal = resultSet.getDouble("o.qty_ori");
                mdQuantityRework = resultSet.getDouble("o.qty_rew");
                mdQuantity = resultSet.getDouble("o.qty");
                mnCharges = resultSet.getInt("o.chgs");
                msNumberReference_n = resultSet.getString("o.num_ref_n");
                if (resultSet.wasNull()) msNumberReference_n = "";
                mtDate = resultSet.getDate("o.dt");
                mtDateDelivery = resultSet.getDate("o.dt_dly");
                mtDateLot_n = resultSet.getDate("o.dt_lot_n");
                if (resultSet.wasNull()) mtDateLot_n = null;
                mtDateLotAssigned_n = resultSet.getDate("o.dt_lot_asg_n");
                if (resultSet.wasNull()) mtDateLotAssigned_n = null;
                mtDateStart_n = resultSet.getDate("o.dt_start_n");
                if (resultSet.wasNull()) mtDateStart_n = null;
                mtDateEnd_n = resultSet.getDate("o.dt_end_n");
                if (resultSet.wasNull()) mtDateEnd_n = null;
                mtDateClose_n = resultSet.getDate("o.dt_close_n");
                if (resultSet.wasNull()) mtDateClose_n = null;
                mtTsLotAssigned_n = resultSet.getTimestamp("o.ts_lot_asg_n");
                if (resultSet.wasNull()) mtTsLotAssigned_n = null;
                mtTsStart_n = resultSet.getTimestamp("o.ts_start_n");
                if (resultSet.wasNull()) mtTsStart_n = null;
                mtTsEnd_n = resultSet.getTimestamp("o.ts_end_n");
                if (resultSet.wasNull()) mtTsEnd_n = null;
                mtTsClose_n = resultSet.getTimestamp("o.ts_close_n");
                if (resultSet.wasNull()) mtTsClose_n = null;
                mnIsCostDone = resultSet.getInt("o.cst_done");
                mbIsConsume = resultSet.getBoolean("o.b_con");
                mbIsForecast = resultSet.getBoolean("o.b_for");
                mbIsProgrammed = resultSet.getBoolean("o.b_prog");
                mbIsDeleted = resultSet.getBoolean("o.b_del");
                mnFkOrdTypeId = resultSet.getInt("o.fid_tp_ord");
                mnFkBomId = resultSet.getInt("o.fid_bom");
                mnFkItemId_r = resultSet.getInt("o.fid_item_r");
                mnFkUnitId_r = resultSet.getInt("o.fid_unit_r");
                mnFkCobId = resultSet.getInt("o.fid_cob");
                mnFkEntityId = resultSet.getInt("o.fid_ent");
                mnFkOrdPriorityId = resultSet.getInt("o.fid_pty_ord");
                mnFkOrdStatusId = resultSet.getInt("o.fid_st_ord");
                mnFkOrdYearId_n = resultSet.getInt("o.fid_ord_year_n");
                if (resultSet.wasNull()) mnFkOrdYearId_n = 0;
                mnFkOrdId_n = resultSet.getInt("o.fid_ord_n");
                if (resultSet.wasNull()) mnFkOrdId_n = 0;
                mnFkBizPartnerId_n = resultSet.getInt("o.fid_bp_n");
                if (resultSet.wasNull()) mnFkBizPartnerId_n = 0;
                mnFkBizPartnerOperatorId_n = resultSet.getInt("o.fid_bp_ope_n");
                if (resultSet.wasNull()) mnFkBizPartnerOperatorId_n = 0;
                mnFkLotItemId_nr = resultSet.getInt("o.fid_lot_item_nr");
                if (resultSet.wasNull()) mnFkLotItemId_nr = 0;
                mnFkLotUnitId_nr = resultSet.getInt("o.fid_lot_unit_nr");
                if (resultSet.wasNull()) mnFkLotUnitId_nr = 0;
                mnFkLotId_n = resultSet.getInt("o.fid_lot_n");
                if (resultSet.wasNull()) mnFkLotId_n = 0;
                mnFkInventoryValuationId_n = resultSet.getInt("o.fid_inv_val_n");
                if (resultSet.wasNull()) mnFkInventoryValuationId_n = 0;
                mnFkTurnDeliveryId = resultSet.getInt("o.fid_turn_dly");
                mnFkTurnLotAssignedId = resultSet.getInt("o.fid_turn_lot_asg");
                mnFkUserLotAssignedId = resultSet.getInt("o.fid_usr_lot_asg");
                mnFkTurnStartId = resultSet.getInt("o.fid_turn_start");
                mnFkUserStartId = resultSet.getInt("o.fid_usr_start");
                mnFkTurnEndId = resultSet.getInt("o.fid_turn_end");
                mnFkUserEndId = resultSet.getInt("o.fid_usr_end");
                mnFkTurnCloseId = resultSet.getInt("o.fid_turn_close");
                mnFkUserCloseId = resultSet.getInt("o.fid_usr_close");
                mnFkUserNewId = resultSet.getInt("o.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("o.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("o.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("o.ts_new");
                mtUserEditTs = resultSet.getTimestamp("o.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("o.ts_del");

                msDbmsNumber = resultSet.getString("f_num");
                msDbmsProductionOrder_n = resultSet.getString("of.ref");
                if (resultSet.wasNull()) msDbmsProductionOrder_n = "";
                msDbmsCompanyBranch = resultSet.getString("bpb.bpb");
                msDbmsCompanyBranchCode = resultSet.getString("bpb.code");
                msDbmsEntity = resultSet.getString("ent.ent");
                msDbmsEntityCode = resultSet.getString("ent.code");
                msDbmsFinishedGood = resultSet.getString("f_item");
                msDbmsBom = resultSet.getString("b.bom");
                msDbmsBomUnitSymbol = resultSet.getString("u.symbol");
                msDbmsProductionOrderStatus = resultSet.getString("s.st");
                msDbmsProductionOrderType = resultSet.getString("t.tp");
                msDbmsNumberFather = resultSet.getString("f_num_father");
                msDbmsUserLotAssigned = resultSet.getString("ul.usr");
                msDbmsUserStart = resultSet.getString("us.usr");
                msDbmsUserEnd = resultSet.getString("uf.usr");
                msDbmsUserClose = resultSet.getString("uc.usr");
                msDbmsLotItem = resultSet.getString("f_lot_item");
                if (resultSet.wasNull()) msDbmsLotItem = "";
                msDbmsLotUnit = resultSet.getString("f_lot_unit");
                if (resultSet.wasNull()) msDbmsLotUnit = "";
                msDbmsLot = resultSet.getString("l.lot");
                if (resultSet.wasNull()) msDbmsLot= "";
                mtDbmsLotDateExpired = resultSet.getDate("l.dt_exp_n");
                if (resultSet.wasNull()) mtDbmsLotDateExpired = null;

                // Read periods:

                statementAux = statement.getConnection().createStatement();
                mvDbmsProductionOrderPeriods.removeAllElements();
                sql = "SELECT * " +
                       "FROM mfg_ord_per " +
                       "WHERE id_ord_year = " + key[0] +  " AND id_ord = " + key[1] + " ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oProductionOrderPeriod = new SDataProductionOrderPeriod();
                    if (oProductionOrderPeriod.read(new int[] { resultSet.getInt("id_ord_year"), resultSet.getInt("id_ord"), resultSet.getInt("id_per") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsProductionOrderPeriods.add(oProductionOrderPeriod);
                    }
                }

                // Read notes:

                mvDbmsProductionOrderNotes.removeAllElements();
                sql = "SELECT * " +
                      "FROM mfg_ord_nts " +
                      "WHERE id_ord_year = " + key[0] + " AND id_ord = " + key[1] + " ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oProductionOrderNotes = new SDataProductionOrderNotes();
                    if (oProductionOrderNotes.read(new int[] { resultSet.getInt("id_ord_year"), resultSet.getInt("id_ord"), resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsProductionOrderNotes.add(oProductionOrderNotes);
                    }
                }

                // Read the explotion materials of this order:

                sql = "SELECT eo.id_exp_year, eo.id_exp, ee.fid_cob, ee.fid_cob_n, ee.fid_wh_n, bb.bpb, bb.code, cbe.ent, cbe.code " +
                        "FROM mfg_exp_ord AS eo " +
                        "INNER JOIN mfg_exp AS e ON eo.id_exp_year = e.id_year AND eo.id_exp = e.id_exp " +
                        "INNER JOIN mfg_exp_ety AS ee ON e.id_year = ee.id_year AND e.id_exp = ee.id_exp " +
                        "INNER JOIN erp.bpsu_bpb AS bb ON ee.fid_cob = bb.id_bpb " +
                        "LEFT OUTER JOIN erp.cfgu_cob_ent AS cbe ON ee.fid_cob_n = cbe.id_cob AND ee.fid_wh_n = cbe.id_ent " +
                        "WHERE eo.id_ord_year = " + key[0] + " AND eo.id_ord = " + key[1] + " AND NOT e.b_del; ";
                resultSet = statement.executeQuery(sql);
                mbDbmsIsExploded = false;
                if (resultSet.next()) {
                    moDbmsExplotionMaterials = new SDataExplotionMaterials();
                    if (moDbmsExplotionMaterials.read(new int[] { resultSet.getInt("eo.id_exp_year"), resultSet.getInt("eo.id_exp") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }

                    msDbmsExplotionMaterialsReference = moDbmsExplotionMaterials.getReference();
                    mnDbmsFkCompanyBranchId = resultSet.getInt("ee.fid_cob_n");
                    mnDbmsFkWarehouseId = resultSet.getInt("ee.fid_wh_n");
                    msDbmsExplotionCompanyBranch = resultSet.getString("bb.bpb");
                    msDbmsExplotionCompanyBranchCode = resultSet.getString("bb.code");
                    msDbmsExplotionWarehouse = resultSet.getString("cbe.ent");
                    if (resultSet.wasNull()) msDbmsExplotionWarehouse = "(n/a)";
                    msDbmsExplotionWarehouseCode = resultSet.getString("cbe.code");
                    if (resultSet.wasNull()) msDbmsExplotionWarehouseCode = "(n/a)";
                    mbDbmsIsExploded = true;
                }

                // Read production order charges:

                sql = "SELECT c.* " +
                    "FROM mfg_ord AS o " +
                    "INNER JOIN mfg_ord_chg AS c ON o.id_year = c.id_year AND o.id_ord = c.id_ord " +
                    "WHERE c.id_year = " + key[0] + " AND c.id_ord = " + key[1] + " AND c.b_del = 0 " +
                    "ORDER BY c.b_del, c.num, c.id_year, c.id_ord, c.id_chg";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oProductionOrderCharge = new SDataProductionOrderCharge();
                    oProductionOrderCharge.setDbmsFkCobId(mnFkCobId);
                    if (oProductionOrderCharge.read(new int[] { resultSet.getInt("c.id_year"), resultSet.getInt("c.id_ord"), resultSet.getInt("c.id_chg") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsProductionOrderCharges.add(oProductionOrderCharge);
                    }
                }

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
        int i = 0;
        int nParam = 1;
        String sSql = "";

        CallableStatement callableStatement = null;
        Statement statement = null;

        SDataProductionOrderCharge charge = null;
        SDataProductionOrderPeriod period = null;
        SDataProductionOrderNotes notes = null;
        SDataStockLot oStockLot = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            // Validate if is necessary save lot:

            if (mbDbmsIsLot) {
                oStockLot = new SDataStockLot();
                oStockLot.setPkItemId(mnFkLotItemId_nr);
                oStockLot.setPkUnitId(mnFkLotUnitId_nr);
                oStockLot.setPkLotId(mnFkLotId_n);
                oStockLot.setLot(msDbmsLot);
                oStockLot.setDateExpiration_n(mtDbmsLotDateExpired);
                oStockLot.setFkUserNewId(mnFkUserNewId);
                oStockLot.setFkUserEditId(mnFkUserEditId);

                if (oStockLot.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
                else {
                    mnFkLotId_n = oStockLot.getPkLotId();
                }
            }

            callableStatement = connection.prepareCall(
                    "{ CALL mfg_ord_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkOrderId);
            callableStatement.setString(nParam++, msNumber);
            callableStatement.setString(nParam++, msReference);
            callableStatement.setDouble(nParam++, mdQuantityOriginal);
            callableStatement.setDouble(nParam++, mdQuantityRework);
            callableStatement.setDouble(nParam++, mdQuantity);
            callableStatement.setInt(nParam++, mnCharges);
            callableStatement.setString(nParam++, msNumberReference_n);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateDelivery.getTime()));
            if (mtDateLot_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateLot_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            if (mtDateLotAssigned_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateLotAssigned_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            if (mtDateStart_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateStart_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            if (mtDateEnd_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            if (mtDateClose_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateClose_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            if (mtTsLotAssigned_n != null) callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtTsLotAssigned_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.TIMESTAMP);
            if (mtTsStart_n != null) callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtTsStart_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.TIMESTAMP);
            if (mtTsEnd_n != null) callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtTsEnd_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.TIMESTAMP);
            if (mtTsClose_n != null) callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtTsClose_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.TIMESTAMP);
            callableStatement.setInt(nParam++, mnIsCostDone);
            callableStatement.setBoolean(nParam++, mbIsConsume);
            callableStatement.setBoolean(nParam++, mbIsForecast);
            callableStatement.setBoolean(nParam++, mbIsProgrammed);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkOrdTypeId);
            callableStatement.setInt(nParam++, mnFkBomId);
            callableStatement.setInt(nParam++, mnFkItemId_r);
            callableStatement.setInt(nParam++, mnFkUnitId_r);
            callableStatement.setInt(nParam++, mnFkCobId);
            callableStatement.setInt(nParam++, mnFkEntityId);
            callableStatement.setInt(nParam++, mnFkOrdPriorityId);
            callableStatement.setInt(nParam++, mnFkOrdStatusId);
            if (mnFkOrdYearId_n != 0) callableStatement.setInt(nParam++, mnFkOrdYearId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkOrdId_n != 0) callableStatement.setInt(nParam++, mnFkOrdId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkBizPartnerId_n != 0) callableStatement.setInt(nParam++, mnFkBizPartnerId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkBizPartnerOperatorId_n != 0) callableStatement.setInt(nParam++, mnFkBizPartnerOperatorId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkLotItemId_nr != 0) callableStatement.setInt(nParam++, mnFkLotItemId_nr); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkLotUnitId_nr != 0) callableStatement.setInt(nParam++, mnFkLotUnitId_nr); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkLotId_n != 0) callableStatement.setInt(nParam++, mnFkLotId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnFkInventoryValuationId_n != 0) callableStatement.setInt(nParam++, mnFkInventoryValuationId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkTurnDeliveryId);
            callableStatement.setInt(nParam++, mnFkTurnLotAssignedId);
            callableStatement.setInt(nParam++, mnFkUserLotAssignedId);
            callableStatement.setInt(nParam++, mnFkTurnStartId);
            callableStatement.setInt(nParam++, mnFkUserStartId);
            callableStatement.setInt(nParam++, mnFkTurnEndId);
            callableStatement.setInt(nParam++, mnFkUserEndId);
            callableStatement.setInt(nParam++, mnFkTurnCloseId);
            callableStatement.setInt(nParam++, mnFkUserCloseId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkOrderId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
                /*
                // Delete production order charges lots:

                statement = connection.createStatement();
                sSql = "DELETE FROM mfg_ord_chg_ety_lot WHERE id_year = " + mnPkYearId + " AND id_ord = " + mnPkOrderId;
                statement.executeUpdate(sSql);

                // Delete production order charges entries:

                sSql = "DELETE FROM mfg_ord_chg_ety WHERE id_year = " + mnPkYearId + " AND id_ord = " + mnPkOrderId;
                statement.executeUpdate(sSql);

                // Delete production order charges:

                sSql = "DELETE FROM mfg_ord_chg WHERE id_year = " + mnPkYearId + " AND id_ord = " + mnPkOrderId;
                statement.executeUpdate(sSql);
                */
                
                // Save charges:

                for (i = 0; i < mvDbmsProductionOrderCharges.size(); i++) {
                    charge = (SDataProductionOrderCharge) mvDbmsProductionOrderCharges.get(i);
                    if (charge != null) {
                        charge.setPkYearId(mnPkYearId);
                        charge.setPkOrderId(mnPkOrderId);
                        //charge.setPkChargeId(0);
                        if (charge.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save periods:

                for (i = 0; i < mvDbmsProductionOrderPeriods.size(); i++) {
                    period = (SDataProductionOrderPeriod) mvDbmsProductionOrderPeriods.get(i);
                    if (period != null) {
                        period.setPkOrdYearId(mnPkYearId);
                        period.setPkOrdId(mnPkOrderId);
                        if (period.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                // Save notes:

                for (i = 0; i < mvDbmsProductionOrderNotes.size(); i++) {
                    notes = (SDataProductionOrderNotes) mvDbmsProductionOrderNotes.get(i);
                    if (notes != null) {
                        notes.setPkOrdYearId(mnPkYearId);
                        notes.setPkOrdId(mnPkOrderId);
                        if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }

    @Override
    public int process(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        if (save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_PROCESS_ERROR;
        }
        else {
            mnLastDbActionResult = SLibConstants.DB_ACTION_PROCESS_OK;
        }

        return mnLastDbActionResult;
    }

}
