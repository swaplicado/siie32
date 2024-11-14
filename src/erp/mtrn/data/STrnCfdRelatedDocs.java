/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.lib.table.STableRow;
import java.util.ArrayList;

/**
 *
 * @author Isabel Servín, Sergio Flores
 */
public class STrnCfdRelatedDocs implements java.io.Serializable {
    
    private final ArrayList<SRowCfdRelatedDocs> maRowCfdRelatedDocs;
    
    public STrnCfdRelatedDocs() {
        maRowCfdRelatedDocs = new ArrayList<>();
    }
    
    public ArrayList<SRowCfdRelatedDocs> getRowCfdRelatedDocs() { return maRowCfdRelatedDocs; }
    
    public void addCfdRelatedDoc(String typeRelation, String uuid) {
        boolean documentFound = false;
        
        for (SRowCfdRelatedDocs row : maRowCfdRelatedDocs) {
            if (row.getRelationType().equals(typeRelation) && !documentFound) {
                for (String u : row.getDocUuids().trim().split(",")) {
                    if (u.equals(uuid)) {
                        documentFound = true; // UUID already added!
                        break;
                    }
                }
                
                if (!documentFound) {
                   row.setDocUuids(row.getDocUuids() + "," + uuid); // add UUID to matching relation type!
                   documentFound = true; // UUID just appended!
                   break;
                }
            }
        }
        
        if (!documentFound) {
            maRowCfdRelatedDocs.add(new SRowCfdRelatedDocs(typeRelation, uuid)); // add a new row
        }
    }
    
    @Override
    public String toString() {
        String string = "";
        
        for (STableRow row : maRowCfdRelatedDocs) {
            string += string.isEmpty() ? "" : "\n";
            string += ((SRowCfdRelatedDocs) row).toString();
        }
        
        return string;
    }
}
