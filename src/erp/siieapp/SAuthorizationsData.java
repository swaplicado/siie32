/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

/**
 *
 * @author Adrián Avilés
 */
public class SAuthorizationsData {
    int[] idData;
    String folio;
    String authorizationStatusName;
    String authorizationTypeName;
    String dataTypeName;
    String userCreator;
    String userUpdator;
    String authorizationUser;
    String consumeEntity;
    String subConsumeEntity;
    String supplierEntity;
    String requisitionStatus;
    String date;
    int fkDataType;
    int fkUserCreator;
    int fkUserUpdator;
    int authorizationStatus;
    int fkPriority;
    String priority;
    String dateInsert;
    String dateUpdate;

    public void setFkPriority(int fkPriority) {
        this.fkPriority = fkPriority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public void setIdData(int[] idData) {
        this.idData = idData;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public void setDataType(int dataType) {
        this.fkDataType = dataType;
    }

    public void setFkUserCreator(int fkUserCreator) {
        this.fkUserCreator = fkUserCreator;
    }

    public void setFkUserUpdator(int fkUserUpdator) {
        this.fkUserUpdator = fkUserUpdator;
    }

    public void setDateInsert(String dateInsert) {
        this.dateInsert = dateInsert;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public void setAuthorizationStatus(int authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    public int[] getIdData() {
        return idData;
    }

    public String getDate() {
        return date;
    }

    public String getFolio() {
        return folio;
    }

    public int getDataType() {
        return fkDataType;
    }

    public int getFkUserCreator() {
        return fkUserCreator;
    }

    public int getFkUserUpdator() {
        return fkUserUpdator;
    }

    public String getDateInsert() {
        return dateInsert;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public int getAuthorizationStatus() {
        return authorizationStatus;
    }

    public String getAuthorizationStatusName() {
        return authorizationStatusName;
    }

    public void setAuthorizationStatusName(String authorizationStatusName) {
        this.authorizationStatusName = authorizationStatusName;
    }

    public String getAuthorizationTypeName() {
        return authorizationTypeName;
    }

    public void setAuthorizationTypeName(String authorizationTypeName) {
        this.authorizationTypeName = authorizationTypeName;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public String getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    public String getUserUpdator() {
        return userUpdator;
    }

    public void setUserUpdator(String userUpdator) {
        this.userUpdator = userUpdator;
    }

    public String getAuthorizationUser() {
        return authorizationUser;
    }

    public void setAuthorizationUser(String authorizationUser) {
        this.authorizationUser = authorizationUser;
    }

    public String getConsumeEntity() {
        return consumeEntity;
    }
    
    public String getSubConsumeEntity() {
        return subConsumeEntity;
    }

    public void setConsumeEntity(String consumeEntity) {
        this.consumeEntity = consumeEntity;
    }
    
    public void setSubConsumeEntity(String subConsumeEntity) {
        this.subConsumeEntity = subConsumeEntity;
    }

    public String getSupplierEntity() {
        return supplierEntity;
    }

    public void setSupplierEntity(String supplierEntity) {
        this.supplierEntity = supplierEntity;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public int getFkDataType() {
        return fkDataType;
    }

    public void setFkDataType(int fkDataType) {
        this.fkDataType = fkDataType;
    }

    public int getFkPriority() {
        return fkPriority;
    }

    public String getPriority() {
        return priority;
    }
}
