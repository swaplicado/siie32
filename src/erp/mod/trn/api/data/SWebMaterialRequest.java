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
    private String mrClass;
    private String mrType;
    private double mrTotal;
    private String mrProvEntity;
    private String mrStatus;
    private String mrNature;
    private String mrItemReference;
    private boolean authorized;
    private boolean returned;
    private String authText;
    private int authStatusId;
    private int matReqUserId;
    private String matReqUser;
    private String notesAuth;
    private String userInTurn;
    private int authorizationPriority;
    private String mrStorageCloudUrl;
    private ArrayList<SWebMaterialRequestEty> lEtys;
    private ArrayList<SWebMatReqNote> lNotes;
    private ArrayList<SWebMatReqEtyNote> lEtyNotes;
    
    SWebAuthorization oWebAuthorization;

    public SWebMaterialRequest() {
        this.mrStorageCloudUrl = "";
        this.lEtys = new ArrayList<>();
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

    public String getMrClass() {
        return mrClass;
    }

    public void setMrClass(String mrClass) {
        this.mrClass = mrClass;
    }

    public String getMrType() {
        return mrType;
    }

    public void setMrType(String mrType) {
        this.mrType = mrType;
    }

    public double getMrTotal() {
        return mrTotal;
    }

    public void setMrTotal(double mrTotal) {
        this.mrTotal = mrTotal;
    }

    public String getMrProvEntity() {
        return mrProvEntity;
    }

    public void setMrProvEntity(String mrProvEntity) {
        this.mrProvEntity = mrProvEntity;
    }

    public String getMrStatus() {
        return mrStatus;
    }

    public void setMrStatus(String mrStatus) {
        this.mrStatus = mrStatus;
    }

    public String getMrNature() {
        return mrNature;
    }

    public void setMrNature(String mrNature) {
        this.mrNature = mrNature;
    }

    public String getMrItemReference() {
        return mrItemReference;
    }
    
    public void setMrItemReference(String mrItemReference) {
        this.mrItemReference = mrItemReference;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public String getAuthText() {
        return authText;
    }

    public void setAuthText(String authText) {
        this.authText = authText;
    }

    public int getAuthStatusId() {
        return authStatusId;
    }

    public void setAuthStatusId(int authStatusId) {
        this.authStatusId = authStatusId;
    }
    
    public int getMatReqUserId() {
        return matReqUserId;
    }

    public void setMatReqUserId(int matReqUserId) {
        this.matReqUserId = matReqUserId;
    }

    public String getMatReqUser() {
        return matReqUser;
    }

    public void setMatReqUser(String matReqUser) {
        this.matReqUser = matReqUser;
    }

    public String getNotesAuth() {
        return notesAuth;
    }

    public void setNotesAuth(String notesAuth) {
        this.notesAuth = notesAuth;
    }

    public String getUserInTurn() {
        return userInTurn;
    }

    public void setUserInTurn(String userInTurn) {
        this.userInTurn = userInTurn;
    }

    public int getAuthorizationPriority() {
        return authorizationPriority;
    }

    public void setAuthorizationPriority(int authorizationPriority) {
        this.authorizationPriority = authorizationPriority;
    }

    public String getMrStorageCloudUrl() {
        return mrStorageCloudUrl;
    }

    public void setMrStorageCloudUrl(String mrStorageCloudUrl) {
        this.mrStorageCloudUrl = mrStorageCloudUrl;
    }

    public ArrayList<SWebMaterialRequestEty> getlEtys() {
        return lEtys;
    }

    public void setlEtys(ArrayList<SWebMaterialRequestEty> lEtys) {
        this.lEtys = lEtys;
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
    
    public SWebAuthorization getoWebAuthorization() {
        return oWebAuthorization;
    }

    public void setoWebAuthorization(SWebAuthorization oWebAuthorization) {
        this.oWebAuthorization = oWebAuthorization;
    }
}
