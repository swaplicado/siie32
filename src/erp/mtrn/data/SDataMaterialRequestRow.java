/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.musr.data.SDataUser;

/**
 *
 * @author Edwin Carmona
 */
public class SDataMaterialRequestRow extends erp.lib.table.STableRow {
    
    private String msUserRequester;
    private final SDbMaterialRequest moMatRequest;
    private erp.client.SClientInterface miClient;
    
    public SDataMaterialRequestRow(erp.client.SClientInterface client, java.lang.Object data) {
        miClient = client;
        moMatRequest = (SDbMaterialRequest) data;
        this.readData();
        prepareTableRow();
    }
    
    private void readData() {
        msUserRequester = "";
        SDataUser oUserRequester = (SDataUser) SDataUtilities.readRegistry(miClient, SDataConstants.USRU_USR, new int[] { moMatRequest.getFkUserRequesterId() }, SLibConstants.EXEC_MODE_SILENT);
        msUserRequester = oUserRequester.getName();
    }

    @Override
    public void prepareTableRow() {
        this.setPrimaryKey(moMatRequest.getPrimaryKey());
        this.setRowPrimaryKey(moMatRequest.getPrimaryKey());
        
        mvValues.clear();
        mvValues.add(moMatRequest.getAuxProvEntName());
        mvValues.add(String.format("%05d", moMatRequest.getNumber()));
        mvValues.add(moMatRequest.getDate());
        mvValues.add(moMatRequest.getReference());
        mvValues.add(msUserRequester);
    }
    
    
}
