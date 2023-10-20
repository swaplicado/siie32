/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SProviderMailRow implements SGridRow {
    
    int mnRowPk;
    String msProvider;
    String msTo;
    String msCc;
    String msCco;
    String msSubject;
    String msBody;
    int mnFkProviderId_n;

    public SProviderMailRow() {
        msProvider = "Proveedor";
        msTo = "destinario@mail.com";
        msCc = "copiaa@mail.com;otro@mail.com";
        msCco = "copiaoculta@mail.com";
        msSubject = "Asunto";
        msBody = "Este sería el cuerpo del correo";
        mnFkProviderId_n = 1;
    }

    public int getRowPk() {
        return mnRowPk;
    }
    public String getProvider() {
        return msProvider;
    }
    public String getTo() {
        return msTo;
    }
    public String getCc() {
        return msCc;
    }
    public String getCco() {
        return msCco;
    }
    public String getSubject() {
        return msSubject;
    }
    public String getBody() {
        return msBody;
    }
    public int getFkProviderId_n() {
        return mnFkProviderId_n;
    }

    public void setRowPk(int n) {
        this.mnRowPk = n;
    }
    public void setProvider(String s) {
        this.msProvider = s;
    }
    public void setTo(String s) {
        this.msTo = s;
    }
    public void setCc(String s) {
        this.msCc = s;
    }
    public void setCco(String s) {
        this.msCco = s;
    }
    public void setSubject(String s) {
        this.msSubject = s;
    }
    public void setBody(String s) {
        this.msBody = s;
    }
    public void setFkProviderId_n(int n) {
        this.mnFkProviderId_n = n;
    }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        
        switch(col) {
            case 0:
                value = msProvider;
                break;
            case 1:
                value = msTo;
                break;
            case 2:
                value = msCc;
                break;
            case 3:
                value = msCco;
                break;
            case 4:
                value = msSubject;
                break;
            case 5:
                value = msBody;
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
                break;
            default:
        }
    }
    
}
