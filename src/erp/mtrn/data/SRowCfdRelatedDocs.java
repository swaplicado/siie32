/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.ver40.DCfdi40Catalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 * 
 * @author Isabel Servín, Sergio Flores
 */
public final class SRowCfdRelatedDocs extends erp.lib.table.STableRow implements java.io.Serializable {
    
    private final String msRelationType; // read-only member
    private String msDocUuids;
    
    private String msFirstDocUuid;
    private int[] manFirstDocKey;
    private SDataDps moFirstDps;
    
    /**
     * 
     * @param relationType Clave SAT para el tipo de relación.
     * @param docUuids UUIDs separados por coma y sin espacios.
     */
    public SRowCfdRelatedDocs(String relationType, String docUuids) {
        msRelationType = relationType;
        msDocUuids = docUuids;
        
        msFirstDocUuid = "";
        manFirstDocKey = null;
        moFirstDps = null;
        
        prepareTableRow();
    }
    
    //public void setRelationType(String s) { msRelationType = s; }
    public void setDocUuids(String s) { msDocUuids = s; }
    
    public void setFirstDocUuid(String s) { msFirstDocUuid = s; }
    public void setFirstDocKey(int[] key) { manFirstDocKey = key; }
    
    public String getRelationType() { return msRelationType; }
    public String getDocUuids() { return msDocUuids; }
    
    public String getFirstDocUuid() { return msFirstDocUuid; }
    public int[] getFirstDocKey() { return manFirstDocKey; }
    
    public boolean isFirstDocSet() {
        return !msFirstDocUuid.isEmpty() && manFirstDocKey != null;
    }
    
    public SDataDps getFirstDps(SClientInterface client) {
        if (!isFirstDocSet()) {
            moFirstDps = null;
        }
        else if (moFirstDps == null || !SLibUtilities.compareKeys(moFirstDps.getPrimaryKey(), manFirstDocKey)) {
            moFirstDps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, manFirstDocKey, SLibConstants.EXEC_MODE_SILENT);
        }
        
        return moFirstDps;
    }
    
    public String composeFirstDps(SClientInterface client) {
        String text = "";
        
        if (isFirstDocSet()) {
            getFirstDps(client);
            
            int[] curKey = new int[] { moFirstDps.getFkCurrencyId() };
            String curCode = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGU_CUR, curKey, SLibConstants.DESCRIPTION_CODE);
            
            text = moFirstDps.getDpsNumber() + "; "
                    + SLibUtils.DateFormatDate.format(moFirstDps.getDate()) + "; "
                    + "$" + SLibUtils.getDecimalFormatAmount().format(moFirstDps.getTotalCy_r()) + " " + curCode
                    + (((SGuiClient) client).getSession().getSessionCustom().isLocalCurrency(curKey) ? "" : " (TC " + SLibUtils.getDecimalFormatExchangeRate().format(moFirstDps.getExchangeRate()) + ")");
        }
        
        return text;
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        mvValues.add(msRelationType);
        mvValues.add(DCfdi40Catalogs.TipoRelación.get(msRelationType));
        mvValues.add(msDocUuids);
    }
    
    @Override
    public String toString() {
        String string = msRelationType + " - " + DCfdi40Catalogs.TipoRelación.get(msRelationType) + ":\n";
        String uuids[] = msDocUuids.trim().split(",");
        
        for (String uuid : uuids) {
            string += uuid + "\n";
        }
        
        return string;
    }
}
