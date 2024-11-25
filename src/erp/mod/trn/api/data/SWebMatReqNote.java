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
public class SWebMatReqNote {
    private int idMaterialRequest;
    private int idNote;
    private String Note;

    public SWebMatReqNote(int idMaterialRequest, int idNote, String Note) {
        this.idMaterialRequest = idMaterialRequest;
        this.idNote = idNote;
        this.Note = Note;
    }

    public int getIdMaterialRequest() {
        return idMaterialRequest;
    }

    public void setIdMaterialRequest(int idMaterialRequest) {
        this.idMaterialRequest = idMaterialRequest;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }
}
