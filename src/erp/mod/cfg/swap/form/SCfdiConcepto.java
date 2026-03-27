/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import cfd.ver40.DCfdi40Catalogs;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SCfdiConcepto implements SGridRow {
    
    public cfd.ver40.DElementConcepto Concepto;
    
    public SCfdiConcepto(final cfd.ver40.DElementConcepto concepto) {
        Concepto = concepto;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            case 0:
                value = Concepto.getAttClaveProdServ().getString();
                break;
            case 1:
                value = Concepto.getAttDescripcion().getString();
                break;
            case 2:
                value = Concepto.getAttCantidad().getDouble();
                break;
            case 3:
                value = Concepto.getAttClaveUnidad().getString();
                break;
            case 4:
                value = Concepto.getAttUnidad().getString();
                break;
            case 5:
                value = Concepto.getAttValorUnitario().getDouble();
                break;
            case 6:
                value = Concepto.getAttImporte().getDouble();
                break;
            case 7:
                value = Concepto.getAttObjetoImp().getString();
                break;
            case 8:
                value = Concepto.getSumOfImporteDeImpuestosTrasladados(DCfdi40Catalogs.IMP_IVA);
                break;
            case 9:
                value = Concepto.getSumOfImporteDeImpuestosRetenidos(DCfdi40Catalogs.IMP_IVA);
                break;
            case 10:
                value = Concepto.getSumOfImporteDeImpuestosRetenidos(DCfdi40Catalogs.IMP_ISR);
                break;
            case 11:
                value = Concepto.getAttDescuento().getDouble();
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
