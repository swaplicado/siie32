/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import java.util.Date;

/**
 *
 * @author AdrianAviles
 */
public class SAuthorizationsData {
    int[] idData;
    Date date;
    String folio;
    int dataType;
    int fkUserCreator;
    int fkUserUpdator;
    Date dateInsert;
    Date dateUpdate;
    int authorizationStatus;

    public void setIdData(int[] idData) {
        this.idData = idData;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setFkUserCreator(int fkUserCreator) {
        this.fkUserCreator = fkUserCreator;
    }

    public void setFkUserUpdator(int fkUserUpdator) {
        this.fkUserUpdator = fkUserUpdator;
    }

    public void setDateInsert(Date dateInsert) {
        this.dateInsert = dateInsert;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public void setAuthorizationStatus(int authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    public int[] getIdData() {
        return idData;
    }

    public Date getDate() {
        return date;
    }

    public String getFolio() {
        return folio;
    }

    public int getDataType() {
        return dataType;
    }

    public int getFkUserCreator() {
        return fkUserCreator;
    }

    public int getFkUserUpdator() {
        return fkUserUpdator;
    }

    public Date getDateInsert() {
        return dateInsert;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public int getAuthorizationStatus() {
        return authorizationStatus;
    }
}
