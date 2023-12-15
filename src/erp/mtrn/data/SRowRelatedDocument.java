/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import cfd.ver40.DCfdi40Catalogs;

/**
 * 
 * @author Isabel Servín
 */
public final class SRowRelatedDocument extends erp.lib.table.STableRow implements java.io.Serializable {
    
    private final String msRelationTypeId;
    private String msDocUuids;
    private String msFirstDocUuid;
    private int[] moFirstDocKey;
    
    /**
     * 
     * @param relationTypeId Clave SAT para el tipo de relación.
     * @param docUuids UUIDs separados por coma y sin espacios.
     */
    public SRowRelatedDocument(String relationTypeId, String docUuids) {
        msRelationTypeId = relationTypeId;
        msDocUuids = docUuids;
        msFirstDocUuid = "";
        moFirstDocKey = null;
        prepareTableRow();
    }
    
    public void setFirstDocUuid(String s) { msFirstDocUuid = s; }
    public void setFirstDocKey(int[] o) { moFirstDocKey = o; }
    public void setDocUuids(String s) { msDocUuids = s; }
    
    public String getRelationTypeId() { return msRelationTypeId; }
    public String getDocUuids() { return msDocUuids; }
    public String getFirstDocUuid() { return msFirstDocUuid; }
    public int[] getFirstDocKey() { return moFirstDocKey; }
    
    public String getAsString() {
        String uuids[] = msDocUuids.trim().split(",");
        String s = msRelationTypeId + " - " + DCfdi40Catalogs.TipoRelación.get(msRelationTypeId) + ":\n";
        for (String uuid : uuids) {
            s += uuid + "\n";
        }
        return s;
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        
        mvValues.add(msRelationTypeId);
        mvValues.add(DCfdi40Catalogs.TipoRelación.get(msRelationTypeId));
        mvValues.add(msDocUuids);
    }
}
