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
public class SDataScaleBizPartnerMapRow extends erp.lib.table.STableRow {

    public int[] moScaId;
    public int mnBizPartnerId;
    public String msName;
    public boolean mbDefault;
    public boolean mbIsNew;
    public boolean mbIsEdit;
    
    public SDataScaleBizPartnerMapRow(int[] scaId, int bizPartnerId, String name, boolean def, boolean isNew) {
        moScaId = scaId;
        mnBizPartnerId = bizPartnerId;
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
