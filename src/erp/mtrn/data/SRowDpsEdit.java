/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataCostCenter;
import erp.mitm.data.SDataItem;

/**
 * Modificar el ítem y el centro de costo de un documento y de todos los documentos asociados a este, sin necesidad de editar cada documento de forma manual.
 * @author Isabel Servín
 */
public final class SRowDpsEdit extends erp.lib.table.STableRow {
    
    private final SClientInterface miClient;
    private final SDataDpsEntry moDpsEntry;
    private final String msConceptKey;
    private SDataItem moItemOld;
    private SDataItem moItemNew;
    private SDataCostCenter moCostCenterOld;
    private SDataCostCenter moCostCenterNew;
    
    /**
     * 
     * @param client Cliente GUI.
     * @param entry
     */
    public SRowDpsEdit(SClientInterface client, SDataDpsEntry entry) {
        miClient = client;
        moDpsEntry = entry;
        msConceptKey = moDpsEntry.getConceptKey();
        setItemOld();
        setCostCenterOld();
        prepareTableRow();
    }

    private void setItemOld() { 
        moItemOld = (SDataItem) SDataUtilities.readRegistry(miClient, 
                SDataConstants.ITMU_ITEM, new int[] { moDpsEntry.getFkItemId() }, SLibConstants.EXEC_MODE_SILENT);
    }
    
    private void setCostCenterOld() {
        moCostCenterOld = (SDataCostCenter) SDataUtilities.readRegistry(miClient, 
                SDataConstants.FIN_CC, new String[] { moDpsEntry.getFkCostCenterId_n() }, SLibConstants.EXEC_MODE_SILENT);
    }
    public void setItemNew(final SDataItem o) { moItemNew = o; }
    public void setCostCenterNew(final SDataCostCenter o) { moCostCenterNew = o; }
    
    public SDataItem getItemOld() { return moItemOld; }
    public SDataItem getItemNew() { return moItemNew; }
    public SDataCostCenter getCostCenterOld() { return moCostCenterOld; }
    public SDataCostCenter getCostCenterNew() { return moCostCenterNew; }
    public Object getDpsEntryPK() { return moDpsEntry.getPrimaryKey(); }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();        
        
        mvValues.add(moDpsEntry.getPkEntryId()); //#
        mvValues.add(msConceptKey); //No. identificación
        mvValues.add(moItemOld.getKey()); //Código ítem anterior
        mvValues.add(moItemOld.getItem()); //Ítem anterior
        mvValues.add(moCostCenterOld.getPkCostCenterIdXXX());
        mvValues.add(moCostCenterOld.getCostCenter());
        mvValues.add(moItemNew == null ? "" : moItemNew.getKey()); //Código ítem nuevo
        mvValues.add(moItemNew == null ? "" : moItemNew.getItem()); //Ítem nuevo    
        mvValues.add(moCostCenterNew == null ? "" : moCostCenterNew.getPkCostCenterIdXXX());
        mvValues.add(moCostCenterNew == null ? "" : moCostCenterNew.getCostCenter());
    }
}
