/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mitm.data;

/**
 *
 * @author Isabel Servín
 */
public final class SDataScaleItemRow extends erp.lib.table.STableRow {

    public int[] moId;
    public String msName;
    public int mnCant;
    public String msDefault;
    public boolean mbPur;
    public boolean mbSal;
    
    public SDataScaleItemRow(int[] id, String name, int cant, String def, boolean pur, boolean sal) {
        moId = id;
        msName = name;
        mnCant = cant;
        msDefault = def;
        mbPur = pur;
        mbSal = sal;
        
        prepareTableRow();
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        mvValues.add(msName);
        mvValues.add(mnCant);
        mvValues.add(msDefault);
    }
}
