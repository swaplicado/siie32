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
import javax.swing.JOptionPane;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SMaterialRequestEntryRow implements SGridRow {
    
    protected int mnPkMatRequestId;
    protected int mnPkEntryId;
    protected double mdQuantity;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkConsumeEntityId_n;
    protected int mnFkSubConsumeEntityId_n;
    
    protected String msAuxItemCode;
    protected String msAuxItemName;
    protected String msAuxUnitCode;
    protected String msAuxConsumeEntity;
    protected double mdAuxSegregated;
    protected double mdAuxSupplied;
    protected double mdAuxStock;
    protected double mdAuxToSegregate;
    protected double mdAuxToSupply;
    protected boolean mbAuxBulk;
    
    SClientInterface miClient;
    
    protected int mnFormType;
    
    public static int FORM_SEGREGATION = 1;
    public static int FORM_SUPPLY = 2;
    
    public SMaterialRequestEntryRow(SClientInterface client, final int formType, final int fkItem, final int fkUnit) {
        miClient = client;
        mnFormType = formType;
        mnFkItemId = fkItem;
        mnFkUnitId = fkUnit;
        mnFkConsumeEntityId_n = 0;
        mnFkSubConsumeEntityId_n = 0;
        
        try {
            this.readAuxs();
        }
        catch (SQLException ex) {
            Logger.getLogger(SMaterialRequestEntryRow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void readAuxs() throws SQLException {
        SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { mnFkItemId }, SLibConstants.EXEC_MODE_VERBOSE);
        SDataUnit unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { mnFkUnitId }, SLibConstants.EXEC_MODE_VERBOSE);
        
        msAuxItemCode = item.getKey();
        msAuxItemName = item.getName();
        msAuxUnitCode = unit.getSymbol();
        mbAuxBulk = item.getIsBulk();
        
        msAuxConsumeEntity = "";
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
                + "WHERE id_mat_cons_ent = " + mnFkSubConsumeEntityId_n + " AND id_mat_cons_subent = " + mnFkSubConsumeEntityId_n + ";";

            ResultSet resultSetSubEntity = miClient.getSession().getStatement().getConnection().createStatement().executeQuery(sqlSubEntity);
            if (resultSetSubEntity.next()) {
                msAuxConsumeEntity += "-" + resultSetSubEntity.getString("name");
            }
        }
    }
    
    public void setPkMatRequestId(int n) { mnPkMatRequestId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }

    public void setAuxSegregated(double mdAuxSegregated) { this.mdAuxSegregated = mdAuxSegregated; }
    public void setAuxSupplied(double mdAuxSupplied) { this.mdAuxSupplied = mdAuxSupplied; }
    public void setAuxStock(double mdAuxStock) { this.mdAuxStock = mdAuxStock; }
    public void setAuxToSegregate(double mdAuxToSegregate) { this.mdAuxToSegregate = mdAuxToSegregate; }
    public void setAuxToSupply(double mdAuxToSupply) { this.mdAuxToSupply = mdAuxToSupply; }

    public int getPkMatRequestId() { return mnPkMatRequestId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantity() { return mdQuantity; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkConsumeEntityId_n() { return mnFkConsumeEntityId_n; }
    public int getFkSubConsumeEntityId_n() { return mnFkSubConsumeEntityId_n; }
    
    public String getAuxItemCode() { return msAuxItemCode; }
    public String getAuxItemName() { return msAuxItemName; }
    public String getAuxUnitCode() { return msAuxUnitCode; }
    public double getAuxSegregated() { return mdAuxSegregated; }
    public double getAuxSupplied() { return mdAuxSupplied; }
    public double getAuxStock() { return mdAuxStock; }
    public double getAuxToSegregate() { return mdAuxToSegregate; }
    public double getAuxToSupply() { return mdAuxToSupply; }

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
        
        if (mnFormType == FORM_SEGREGATION) {
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
                    value = mdQuantity;
                    break;
                case 4:
                    value = mdAuxSegregated;
                    break;
                case 5:
                    value = mdAuxStock;
                    break;
                case 6:
                    value = mdQuantity - mdAuxSegregated;
                    break;
                case 7:
                    value = mdAuxToSegregate;
                    break;
                default:
            }
        }
        else {
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
                    value = mdQuantity;
                    break;
                case 4:
                    value = mdAuxSupplied;
                    break;
                case 5:
                    value = mdAuxSegregated;
                    break;
                case 6:
                    value = mdAuxStock;
                    break;
                case 7:
                    value = mdQuantity - mdAuxSupplied;
                    break;
                case 8:
                    value = mdAuxToSupply;
                    break;
                case 9:
                    value = msAuxConsumeEntity;
                    break;
                default:
            }
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        if (mnFormType == FORM_SEGREGATION) {
            switch(col) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    break;
                case 7:
                    if (SMaterialRequestEntryRow.hasDecimals((double) value) && !mbAuxBulk) {
                        JOptionPane.showMessageDialog(null, "El ítem no es a granel.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        mdAuxToSegregate = (double) value;
                    }
                    break;
                default:
            }
        }
        else {
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
                case 8:
                    if (SMaterialRequestEntryRow.hasDecimals((double) value) && !mbAuxBulk) {
                        JOptionPane.showMessageDialog(null, "El ítem no es a granel.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        mdAuxToSupply = (double) value;
                    }
                    break;
                case 9:
                    break;
                default:
            }
        }
    }
    
    // Método para validar si un double tiene decimales o es un entero
    public static boolean hasDecimals(double number) {
        // Comparamos el número con su versión truncada y redondeada
        return number != Math.floor(number) || number != Math.ceil(number);
    }
}
