/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mbps.data.SDataBizPartner;
import erp.mod.SModSysConsts;
import erp.musr.data.SDataUser;
import java.io.Serializable;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SRowCCGroupVsUser implements SGridRow, Serializable {
    
    protected String msLinkName;
    protected String msReferenceName;
    protected int mnLinkId;
    protected int mnReferenceId;
    
    public void setLinkName(String s) { msLinkName = s; }
    public void setReferenceName(String s) { msReferenceName = s; }
    public void setLinkId(int n) { mnLinkId = n; }
    public void setReferenceId(int n) { mnReferenceId = n; }
    
    public String getTpLinkName() { return msLinkName; }
    public String getName() { return msReferenceName; }
    public int getLinkId() { return mnLinkId; }
    public int getReferenceId() { return mnReferenceId; }
    
    public void readReference (SGuiSession session) throws Exception {
        switch (mnLinkId) {
            case SModSysConsts.USRS_LINK_EMP:
                SDataBizPartner bp = new SDataBizPartner();
                bp.read(new int[] { mnReferenceId }, session.getStatement());
                msLinkName = "EMPLEADO";
                msReferenceName = bp.getBizPartner();
                break;
            case SModSysConsts.USRS_LINK_USR:
                SDataUser user = new SDataUser();
                user.read(new int[] { mnReferenceId }, session.getStatement());
                msLinkName = "USUARIO";
                msReferenceName = user.getUser();
                break;
        }
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnLinkId, mnReferenceId };
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
            case 0: value = msLinkName; break;
            case 1: value = msReferenceName; break;
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
