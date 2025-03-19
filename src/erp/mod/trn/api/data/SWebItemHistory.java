/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.data;

import java.util.Date;

/**
 *
 * @author Edwin Carmona
 */
public class SWebItemHistory {
    private int idItem;
    private Date lastPurchaseDate;
    private String numFact;
    private String lastProvider;
    private String conceptKey;
    private String concept;
    private double quantity;
    private double priceUnitary;
    private double priceUnitaryCur;
    private double currentPriceUnitary;
    private double currentPriceUnitaryCur;
    private String unitName;
    private String unitSymbol;
    private String currencyName;
    private String currencySymbol;
    private double percentage;

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getNumFact() {
        return numFact;
    }

    public void setNumFact(String numFact) {
        this.numFact = numFact;
    }

    public Date getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(Date lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }

    public String getLastProvider() {
        return lastProvider;
    }

    public void setLastProvider(String lastProvider) {
        this.lastProvider = lastProvider;
    }

    public String getConceptKey() {
        return conceptKey;
    }

    public void setConceptKey(String conceptKey) {
        this.conceptKey = conceptKey;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPriceUnitary() {
        return priceUnitary;
    }

    public void setPriceUnitary(double priceUnitary) {
        this.priceUnitary = priceUnitary;
    }

    public double getPriceUnitaryCur() {
        return priceUnitaryCur;
    }

    public void setPriceUnitaryCur(double priceUnitaryCur) {
        this.priceUnitaryCur = priceUnitaryCur;
    }

    public double getCurrentPriceUnitary() {
        return currentPriceUnitary;
    }

    public void setCurrentPriceUnitary(double currentPriceUnitary) {
        this.currentPriceUnitary = currentPriceUnitary;
    }

    public double getCurrentPriceUnitaryCur() {
        return currentPriceUnitaryCur;
    }

    public void setCurrentPriceUnitaryCur(double currentPriceUnitaryCur) {
        this.currentPriceUnitaryCur = currentPriceUnitaryCur;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
    
}
