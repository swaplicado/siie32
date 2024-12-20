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
public class SWebFile {

    private int idSupFile;
    private double totalCurrencyQuot;
    private String fileNumber;
    private String userFileName;
    private String mongoId;
    private String fileExtension;
    private String cloudStorageName;
    private String cloudFileUrl;
    private String externalBpName;
    private int fkCurQuot;

    public SWebFile() {
        this.idSupFile = 0;
        this.totalCurrencyQuot = 0d;
        this.fileNumber = "";
        this.userFileName = "";
        this.mongoId = "";
        this.fileExtension = "";
        this.cloudStorageName = "";
        this.cloudFileUrl = "";
        this.externalBpName = "";
        this.fkCurQuot = 0;
    }

    public int getIdSupFile() {
        return idSupFile;
    }

    public void setIdSupFile(int idSupFile) {
        this.idSupFile = idSupFile;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getUserFileName() {
        return userFileName;
    }

    public void setUserFileName(String userFileName) {
        this.userFileName = userFileName;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getCloudStorageName() {
        return cloudStorageName;
    }

    public void setCloudStorageName(String cloudStorageName) {
        this.cloudStorageName = cloudStorageName;
    }

    public double getTotalCurrencyQuot() {
        return totalCurrencyQuot;
    }

    public void setTotalCurrencyQuot(double totalCurrencyQuot) {
        this.totalCurrencyQuot = totalCurrencyQuot;
    }

    public String getCloudFileUrl() {
        return cloudFileUrl;
    }

    public void setCloudFileUrl(String cloudFileUrl) {
        this.cloudFileUrl = cloudFileUrl;
    }

    public String getExternalBpName() {
        return externalBpName;
    }

    public void setExternalBpName(String externalBpName) {
        this.externalBpName = externalBpName;
    }

    public int getFkCurQuot() {
        return fkCurQuot;
    }

    public void setFkCurQuot(int fkCurQuot) {
        this.fkCurQuot = fkCurQuot;
    }
}
