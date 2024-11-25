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
public class SWebDps {

    int idYear;
    int idDoc;
    int idUser;
    SWebDpsRow oDpsHeader;
    ArrayList<SWebDpsEty> lEtys;
    ArrayList<SWebDpsEtyFolder> lFolders;
    ArrayList<SWebDpsNote> lNotes;
    SWebAuthorization oWebAuthorization;

    public SWebDps(int idYear, int idDoc) {
        this.idYear = idYear;
        this.idDoc = idDoc;

        this.lEtys = new ArrayList<>();
        this.lFolders = new ArrayList<>();
        this.lNotes = new ArrayList<>();
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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public SWebDpsRow getoDpsHeader() {
        return oDpsHeader;
    }

    public void setoDpsHeader(SWebDpsRow oDpsHeader) {
        this.oDpsHeader = oDpsHeader;
    }

    public ArrayList<SWebDpsEty> getlEtys() {
        return lEtys;
    }

    public void setlEtys(ArrayList<SWebDpsEty> lEtys) {
        this.lEtys = lEtys;
    }

    public ArrayList<SWebDpsEtyFolder> getlFolders() {
        return lFolders;
    }

    public void setlFolders(ArrayList<SWebDpsEtyFolder> lFolders) {
        this.lFolders = lFolders;
    }

    public SWebAuthorization getoWebAuthorization() {
        return oWebAuthorization;
    }

    public void setoWebAuthorization(SWebAuthorization oWebAuthorization) {
        this.oWebAuthorization = oWebAuthorization;
    }

    public ArrayList<SWebDpsNote> getlNotes() {
        return lNotes;
    }

    public void setlNotes(ArrayList<SWebDpsNote> lNotes) {
        this.lNotes = lNotes;
    }
}
