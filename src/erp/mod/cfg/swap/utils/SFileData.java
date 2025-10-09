/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.utils;

import java.util.Date;

/**
 *
 * @author Edwin Carmona
 */
public class SFileData {
    
    private int idYear;
    private int idDoc;
    private String dbName;
    private String fileName;
    Date lastUpdate;
    private String bucketName;
    private String projectId;

    public SFileData() { }
    
    public SFileData(int idYear, int idDoc, String companyName, Date lastUpdate) {
        this.idYear = idYear;
        this.idDoc = idDoc;
        this.lastUpdate = lastUpdate;
        this.dbName = companyName;
        this.fileName = "OC_" + companyName + "_" + idYear + "_" + idDoc + ".pdf";
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

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
