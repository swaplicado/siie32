/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.io.Serializable;
import java.sql.ResultSet;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Isabel Servín
 */
public class SRowSupplierFileDpsEntry implements  SGridRow, Serializable {

    protected final SDbDpsEntry moDpsEty;
    
    protected String msCur;
    protected int mnPos;
    protected String msUnitCode;
    protected boolean mbVinculed;

    public SRowSupplierFileDpsEntry(SGuiClient client, SDbDpsEntry dpsEty, String cur) {
        moDpsEty = dpsEty;
        msCur = cur;
        readUnit(client);
    }
    
    public void setPos(int n) { mnPos = n; }
    public void setVinculed(boolean b) { mbVinculed = b; }
    
    public int getPos() { return mnPos; }
    public boolean getIsVinculed() { return mbVinculed; }
    public double getTotalRow() { return moDpsEty.getTotalCy_r(); }
    
    private void readUnit(SGuiClient client) {
        try {
            String sql = "SELECT symbol FROM erp.itmu_unit WHERE id_unit = " + moDpsEty.getFkUnitId();
            ResultSet resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                msUnitCode = resultSet.getString(1);
            }
        }
        catch (Exception e) {
            client.showMsgBoxError(e.getMessage());
        }
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return moDpsEty.getPrimaryKey();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0: value = mnPos; break;
            case 1: value = moDpsEty.getConceptKey(); break;
            case 2: value = moDpsEty.getConcept(); break;
            case 3: value = moDpsEty.getQuantity(); break;
            case 4: value = msUnitCode; break;
            case 5: value = moDpsEty.getPriceUnitaryCy(); break;
            case 6: value = moDpsEty.getTotalCy_r(); break;
            case 7: value = msCur; break;
            case 8: value = mbVinculed; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 8: mbVinculed = (boolean) value; break;
        }
    }
}
