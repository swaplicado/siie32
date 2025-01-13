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
public class SWebDpsNote {
    private int idYear;
    private int idDoc;
    private int idNote;
    private String note;

    public SWebDpsNote(int idYear, int idDoc, int idNote, String note) {
        this.idYear = idYear;
        this.idDoc = idDoc;
        this.idNote = idNote;
        this.note = note;
    }
    public int getIdYear() {
        return idYear;
    }
    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }
    public int getIdDoc() {
        return idDoc;
    }
    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }
    public int getIdNote() {
        return idNote;
    }
    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}