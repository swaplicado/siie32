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
public class SWebDpsFile {
    private int idSupFile;
    private int idYear;
    private int idDoc;
    private int sort;
    private String fileType;
    private double totalLocal;
    private double exchangeRateLocal;
    private double totalDps;
    private double exchangeRateDps;
    private double totalCurQuot;
    private String notes;
    private boolean isExtTemp;
    private int fkDpsCurrency;
    private int fkQuotCurrency;
    private SWebFile oWebFile;

    public SWebDpsFile() {
        this.idSupFile = 0;
        this.idYear = 0;
        this.idDoc = 0;
        this.sort = 0;
        this.fileType = "";
        this.totalLocal = 0d;
        this.exchangeRateLocal = 0d;
        this.totalDps = 0d;
        this.exchangeRateDps = 0d;
        this.totalCurQuot = 0d;
        this.notes = "";
        this.isExtTemp = false;
        this.fkDpsCurrency = 0;
        this.fkQuotCurrency = 0;
        this.oWebFile = new SWebFile();
    }

    public int getIdSupFile() {
        return idSupFile;
    }

    public void setIdSupFile(int idSupFile) {
        this.idSupFile = idSupFile;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public double getTotalLocal() {
        return totalLocal;
    }

    public void setTotalLocal(double totalLocal) {
        this.totalLocal = totalLocal;
    }

    public double getExchangeRateLocal() {
        return exchangeRateLocal;
    }

    public void setExchangeRateLocal(double exchangeRateLocal) {
        this.exchangeRateLocal = exchangeRateLocal;
    }

    public double getTotalDps() {
        return totalDps;
    }

    public void setTotalDps(double totalDps) {
        this.totalDps = totalDps;
    }

    public double getExchangeRateDps() {
        return exchangeRateDps;
    }

    public void setExchangeRateDps(double exchangeRateDps) {
        this.exchangeRateDps = exchangeRateDps;
    }

    public double getTotalCurQuot() {
        return totalCurQuot;
    }

    public void setTotalCurQuot(double totalCurQuot) {
        this.totalCurQuot = totalCurQuot;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isExtTemp() {
        return isExtTemp;
    }

    public void setExtTemp(boolean isExtTemp) {
        this.isExtTemp = isExtTemp;
    }

    public int getFkDpsCurrency() {
        return fkDpsCurrency;
    }

    public void setFkDpsCurrency(int fkDpsCurrency) {
        this.fkDpsCurrency = fkDpsCurrency;
    }

    public int getFkQuotCurrency() {
        return fkQuotCurrency;
    }

    public void setFkQuotCurrency(int fkQuotCurrency) {
        this.fkQuotCurrency = fkQuotCurrency;
    }

    public SWebFile getoWebFile() {
        return oWebFile;
    }

    public void setoWebFile(SWebFile oWebFile) {
        this.oWebFile = oWebFile;
    }
}
