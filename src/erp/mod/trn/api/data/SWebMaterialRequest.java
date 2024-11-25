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
public class SWebMaterialRequest {

    private int idMaterialRequest;
    private String mrFolio;
    private String mrDate;
    private String mrUser;
    private String mrPriority;
    private String mrRequiredDate;
    private String mrType;
    private ArrayList<SWebMatReqNote> lNotes;
    private ArrayList<SWebMatReqEtyNote> lEtyNotes;

    public SWebMaterialRequest() {
        this.lNotes = new ArrayList<>();
        this.lEtyNotes = new ArrayList<>();
    }

    public int getIdMaterialRequest() {
        return idMaterialRequest;
    }

    public void setIdMaterialRequest(int idMaterialRequest) {
        this.idMaterialRequest = idMaterialRequest;
    }

    public String getMrFolio() {
        return mrFolio;
    }

    public void setMrFolio(String mrFolio) {
        this.mrFolio = mrFolio;
    }

    public String getMrDate() {
        return mrDate;
    }

    public void setMrDate(String mrDate) {
        this.mrDate = mrDate;
    }

    public String getMrUser() {
        return mrUser;
    }

    public void setMrUser(String mrUser) {
        this.mrUser = mrUser;
    }

    public String getMrPriority() {
        return mrPriority;
    }

    public void setMrPriority(String mrPriority) {
        this.mrPriority = mrPriority;
    }

    public String getMrRequiredDate() {
        return mrRequiredDate;
    }

    public void setMrRequiredDate(String mrRequiredDate) {
        this.mrRequiredDate = mrRequiredDate;
    }

    public String getMrType() {
        return mrType;
    }

    public void setMrType(String mrType) {
        this.mrType = mrType;
    }

    public ArrayList<SWebMatReqNote> getlNotes() {
        return lNotes;
    }

    public void setlNotes(ArrayList<SWebMatReqNote> lNotes) {
        this.lNotes = lNotes;
    }

    public ArrayList<SWebMatReqEtyNote> getlEtyNotes() {
        return lEtyNotes;
    }

    public void setlEtyNotes(ArrayList<SWebMatReqEtyNote> lEtyNotes) {
        this.lEtyNotes = lEtyNotes;
    }

}
