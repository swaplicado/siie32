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
    private SDataItem moItemRefOld;
    private SDataItem moItemRefNew;
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
        if (moDpsEntry.getFkItemRefId_n() > 0) {
            moItemRefOld = (SDataItem) SDataUtilities.readRegistry(miClient, 
                    SDataConstants.ITMU_ITEM, new int[] { moDpsEntry.getFkItemRefId_n() }, SLibConstants.EXEC_MODE_SILENT);
        }
        else {
            moItemRefOld = null;
        }
    }
    
    private void setCostCenterOld() {
        moCostCenterOld = (SDataCostCenter) SDataUtilities.readRegistry(miClient, 
                SDataConstants.FIN_CC, new String[] { moDpsEntry.getFkCostCenterId_n() }, SLibConstants.EXEC_MODE_SILENT);
    }
    public void setItemRefNew(final SDataItem o) { moItemRefNew = o; }
    public void setCostCenterNew(final SDataCostCenter o) { moCostCenterNew = o; }
    
    public SDataItem getItemRefOld() { return moItemRefOld; }
    public SDataItem getItemRefNew() { return moItemRefNew; }
    public SDataCostCenter getCostCenterOld() { return moCostCenterOld; }
    public SDataCostCenter getCostCenterNew() { return moCostCenterNew; }
    public Object getDpsEntryPK() { return moDpsEntry.getPrimaryKey(); }
    
    @Override
    public void prepareTableRow() {
        mvValues.clear();        
        
        mvValues.add(moDpsEntry.getPkEntryId()); //#
        mvValues.add(msConceptKey); //No. identificación
        mvValues.add(moDpsEntry.getConcept()); //No. identificación
        mvValues.add(moItemRefOld != null ? moItemRefOld.getKey() : ""); //Código ítem anterior
        mvValues.add(moItemRefOld != null ? moItemRefOld.getItem() : ""); //Ítem anterior
        mvValues.add(moCostCenterOld.getPkCostCenterIdXXX());
        mvValues.add(moCostCenterOld.getCostCenter());
        mvValues.add(moItemRefNew == null ? "" : moItemRefNew.getKey()); //Código ítem nuevo
        mvValues.add(moItemRefNew == null ? "" : moItemRefNew.getItem()); //Ítem nuevo    
        mvValues.add(moCostCenterNew == null ? "" : moCostCenterNew.getPkCostCenterIdXXX());
        mvValues.add(moCostCenterNew == null ? "" : moCostCenterNew.getCostCenter());
    }
}
