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
public final class SRowTicketsPicker extends STableRow {

    public int mnPk;
    public int mnNum;
    public int mnItem;
    public int mnBp;
    public String msLink;
    public String msBp;
    public String msItem;
    
    public SRowTicketsPicker(int pk, int num, int item, int bp, String link, String bpName, String itemName) {
        mnPk = pk;
        mnNum = num;
        mnItem = item;
        mnBp = bp;
        msLink = link;
        msBp = bpName;
        msItem = itemName;
        
        prepareTableRow();
    }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        mvValues.add(mnNum);
        mvValues.add(msBp);
        mvValues.add(msItem);
        mvValues.add(msLink);
    }
}
