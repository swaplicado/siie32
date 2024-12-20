/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.data;

/**
 *
 * @author Edwin Carmona
 */
public class SWebMatReqEtyNote extends SWebMatReqNote {
    
    private int idMaterialRequestEty;
    
    public SWebMatReqEtyNote(int idMaterialRequest, int idMaterialRequestEty, int idNote, String Note) {
        super(idMaterialRequest, idNote, Note);
        this.idMaterialRequestEty = idMaterialRequestEty;
    }

    public int getIdMaterialRequestEty() {
        return idMaterialRequestEty;
    }

    public void setIdMaterialRequestEty(int idMaterialRequestEty) {
        this.idMaterialRequestEty = idMaterialRequestEty;
    }
    
}
