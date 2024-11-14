/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.data;

/**
 *
 * @author Isabel Servín
 */
public final class SDataScaleBizPartnerRow extends erp.lib.table.STableRow {

    public int[] moId;
    public String msName;
    public int mnCant;
    public String msDefault;
    public boolean mbSup;
    public boolean mbCus;
    
    public SDataScaleBizPartnerRow(int[] id, String name, int cant, String def, boolean sup, boolean cus) {
        moId = id;
        msName = name;
        mnCant = cant;
        msDefault = def;
        mbSup = sup;
        mbCus = cus;
        
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
