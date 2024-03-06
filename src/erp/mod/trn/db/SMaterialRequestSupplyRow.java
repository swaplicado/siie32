/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SMaterialRequestSupplyRow implements SGridRow {
    
    protected int mnPkDiogYearId;
    protected int mnPkDiogDocId;
    protected int mnPkDiogEtyId;
    protected int mnFkMatRequestId;
    protected int mnFkMatRequestEntryId;
    protected int mnFkConsumeEntityId_n;
    protected int mnFkSubConsumeEntityId_n;
    protected int mnFkSubConsumeSubEntityId_n;
    protected double mdQuantity;
    protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkOrigUnitId;
    protected int mnFkCompanyBranchId;
    protected int mnFkWarehouseId;
    
    protected String msAuxItemCode;
    protected String msAuxItemName;
    protected String msAuxUnitCode;
    protected String msAuxWarehouseName;
    protected String msAuxConsumeEntity;
    protected String msAuxSubConsumeEntity;
    protected double mdAuxSegregated;
    protected double mdAuxSupplied;
    protected double mdAuxStock;
    protected double mdAuxToSegregate;
    protected double mdAuxToSupply;
    protected boolean mbAuxBulk;
    protected boolean mbAuxIsInv;
    
    SClientInterface miClient;
    protected SDbMaterialRequestEntry moAuxMatReqEty;
    
    public SMaterialRequestSupplyRow(SClientInterface client, final int fkItem, final int fkUnit, final int fkOrigUnit, final int fkCoB, final int fkWhs) {
        miClient = client;
        mnFkItemId = fkItem;
        mnFkUnitId = fkUnit;
        mnFkOrigUnitId = fkOrigUnit;
        mnFkCompanyBranchId = fkCoB;
        mnFkWarehouseId = fkWhs;
        mnFkConsumeEntityId_n = 0;
        mnFkSubConsumeEntityId_n = 0;
        mnFkSubConsumeSubEntityId_n = 0;
        
        mbDeleted = false;
        moAuxMatReqEty = null;
        
        msAuxItemCode = "";
        msAuxItemName = "";
        msAuxUnitCode = "";
        msAuxWarehouseName = "";
        msAuxConsumeEntity = "";
        msAuxSubConsumeEntity = "";
        
        this.readAuxs();
    }
    
    private void readAuxs() {
        try {
            SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { mnFkItemId }, SLibConstants.EXEC_MODE_VERBOSE);
            SDataUnit unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { mnFkUnitId }, SLibConstants.EXEC_MODE_VERBOSE);
            
            String sqlCob = "SELECT code, ent FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " "
                    + "WHERE id_cob = " + mnFkCompanyBranchId + " AND id_ent = " + mnFkWarehouseId + ";";
            
            ResultSet resultSetCob = miClient.getSession().getStatement().getConnection().createStatement().executeQuery(sqlCob);
            if (resultSetCob.next()) {
                msAuxWarehouseName = resultSetCob.getString("code") + " - " + resultSetCob.getString("ent");
            }
            
            String sqlItem = "SELECT b_bulk FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " "
                    + "WHERE id_item = " + mnFkItemId + ";";
            
            ResultSet resultSetItem = miClient.getSession().getStatement().getConnection().createStatement().executeQuery(sqlItem);
            if (resultSetItem.next()) {
                mbAuxBulk = resultSetItem.getBoolean("b_bulk");
            }
            
            if (mnFkConsumeEntityId_n > 0) {
                String sqlEntity = "SELECT name FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_ENT) + " "
                    + "WHERE id_mat_cons_ent = " + mnFkConsumeEntityId_n + ";";
            
                ResultSet resultSetEntity = miClient.getSession().getStatement().getConnection().createStatement().executeQuery(sqlEntity);
                if (resultSetEntity.next()) {
                    msAuxConsumeEntity = resultSetEntity.getString("name");
                }
            }
            
            if (mnFkSubConsumeEntityId_n > 0) {
                String sqlSubEntity = "SELECT name FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CONS_SUBENT) + " "
                    + "WHERE id_mat_cons_ent = " + mnFkSubConsumeEntityId_n + " AND id_mat_cons_subent = " + mnFkSubConsumeSubEntityId_n + ";";
            
                ResultSet resultSetSubEntity = miClient.getSession().getStatement().getConnection().createStatement().executeQuery(sqlSubEntity);
                if (resultSetSubEntity.next()) {
                    msAuxSubConsumeEntity = resultSetSubEntity.getString("name");
                }
            }
            
            msAuxItemCode = item.getKey();
            msAuxItemName = item.getName();
            msAuxUnitCode = unit.getSymbol();
            mbAuxBulk = item.getIsBulk();
            mbAuxIsInv = item.getIsInventoriable();
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestSupplyRow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setPkDiogYearId(int n) { mnPkDiogYearId = n; }
    public void setPkDiogDocId(int n) { mnPkDiogDocId = n; }
    public void setPkDiogEtyId(int n) { mnPkDiogEtyId = n; }
    public void setFkMatRequestId(int n) { mnFkMatRequestId = n; }
    public void setFkMatRequestEntryId(int n) { mnFkMatRequestEntryId = n; }
    public void setFkConsumeEntityId_n(int n) { mnFkConsumeEntityId_n = n; }
    public void setFkSubConsumeEntityId_n(int n) { mnFkSubConsumeEntityId_n = n; }
    public void setFkSubConsumeSubEntityId_n(int n) { mnFkSubConsumeSubEntityId_n = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setIsDeleted(boolean b) { mbDeleted = b; }

    public void setAuxSegregated(double mdAuxSegregated) { this.mdAuxSegregated = mdAuxSegregated; }
    public void setAuxSupplied(double mdAuxSupplied) { this.mdAuxSupplied = mdAuxSupplied; }
    public void setAuxStock(double mdAuxStock) { this.mdAuxStock = mdAuxStock; }
    public void setAuxToSegregate(double mdAuxToSegregate) { this.mdAuxToSegregate = mdAuxToSegregate; }
    public void setAuxToSupply(double mdAuxToSupply) { this.mdAuxToSupply = mdAuxToSupply; }

    public int getPkYearId() { return mnPkDiogYearId; }
    public int getPkDocId() { return mnPkDiogDocId; }
    public int getPkEtyId() { return mnPkDiogEtyId; }
    public int getFkMatRequestId() { return mnFkMatRequestId; }
    public int getFkMatRequestEntryId() { return mnFkMatRequestEntryId; }
    public int getFkConsumeEntityId_n() { return mnFkConsumeEntityId_n; }
    public int getFkSubConsumeEntityId_n() { return mnFkSubConsumeEntityId_n; }
    public int getFkSubConsumeSubEntityId_n() { return mnFkSubConsumeSubEntityId_n; }
    public double getQuantity() { return mdQuantity; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkOrigUnitId() { return mnFkOrigUnitId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkWarehouseId() { return mnFkWarehouseId; }
    
    public String getAuxItemCode() { return msAuxItemCode; }
    public String getAuxItemName() { return msAuxItemName; }
    public String getAuxUnitCode() { return msAuxUnitCode; }
    public double getAuxSegregated() { return mdAuxSegregated; }
    public double getAuxSupplied() { return mdAuxSupplied; }
    public double getAuxStock() { return mdAuxStock; }
    public double getAuxToSegregate() { return mdAuxToSegregate; }
    public double getAuxToSupply() { return mdAuxToSupply; }
    public boolean getIsInventorable() { return mbAuxIsInv; }
    
    public boolean isTheSameRow(SMaterialRequestSupplyRow oRow) {
        return oRow.getFkCompanyBranchId() == this.getFkCompanyBranchId()
                && oRow.getFkWarehouseId() == this.getFkWarehouseId()
                && oRow.getFkMatRequestId() == this.getFkMatRequestId()
                && oRow.getFkMatRequestEntryId() == this.getFkMatRequestEntryId()
                && oRow.getFkItemId() == this.getFkItemId()
                && oRow.getFkUnitId() == this.getFkUnitId()
                && oRow.getQuantity() == this.getQuantity()
                && oRow.getPkYearId() == this.getPkYearId()
                && oRow.getPkDocId() == this.getPkDocId()
                && oRow.getPkEtyId() == this.getPkEtyId();
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnFkMatRequestId, mnFkMatRequestEntryId };
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
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        switch(col) {
            case 0:
                value = msAuxItemCode;
                break;
            case 1:
                value = msAuxItemName;
                break;
            case 2:
                value = msAuxUnitCode;
                break;
            case 3:
                value = msAuxWarehouseName;
                break;
            case 4:
                value = msAuxConsumeEntity;
                break;
            case 5:
                value = msAuxSubConsumeEntity;
                break;
            case 6:
                value = mdQuantity;
                break;
            case 7:
                value = mnPkDiogDocId > 0;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch(col) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                break;
            default:
        }
    }
    
    // Método para validar si un double tiene decimales o es un entero
    public static boolean hasDecimals(double number) {
        // Comparamos el número con su versión truncada y redondeada
        return number != Math.floor(number) || number != Math.ceil(number);
    }
}
