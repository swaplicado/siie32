/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.data;

import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SDocumentsResponse {
    ArrayList<SWebDpsRow> lDocuments;

    public ArrayList<SWebDpsRow> getlDocuments() {
        return lDocuments;
    }

    public void setlDocuments(ArrayList<SWebDpsRow> lDocuments) {
        this.lDocuments = lDocuments;
    }
}
