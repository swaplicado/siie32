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
public class SDataScaleItemMapRow extends erp.lib.table.STableRow {

    public int[] moScaId;
    public int mnItemId;
    public String msName;
    public boolean mbDefault;
    public boolean mbIsNew;
    public boolean mbIsEdit;
    
    public SDataScaleItemMapRow(int[] scaId, int itemId, String name, boolean def, boolean isNew) {
        moScaId = scaId;
        mnItemId = itemId;
        msName = name;
        mbDefault = def;
        mbIsNew = isNew;
        mbIsEdit = false;
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        mvValues.add(msName);
        mvValues.add(mbDefault);
    }
}
