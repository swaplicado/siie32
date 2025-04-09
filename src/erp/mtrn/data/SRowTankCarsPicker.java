/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.lib.table.STableRow;

/**
 *
 * @author Isabel Servín
 */
public final class SRowTankCarsPicker extends STableRow {

    public int mnPk;
    public String msPlate;
    
    public SRowTankCarsPicker(int pk, String plate) {
        mnPk = pk;
        msPlate = plate;
        
        prepareTableRow();
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        mvValues.add(msPlate);
    }
}
