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
public class SWebDpsEty {
    int idYear;
    int idDoc;
    int idEty;
    int idItem;
    int idUnit;
    String conceptKey;
    String concept;
    String reference;
    String partNumber;
    String unitSymbol;
    String costCenter;
    double quantity;
    double price;
    double priceCur;
    double subtotal;
    double taxCharged;
    double taxRetained;
    double total;
    ArrayList<SWebItemHistory> lItemHistory;
    SWebMaterialRequest oMaterialRequest;

    public SWebDpsEty() {
        this.lItemHistory = new ArrayList<>();
    }

    /**
     * getters y setters
     */
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
    public int getIdEty() {
        return idEty;
    }
    public void setIdEty(int idEty) {
        this.idEty = idEty;
    }
    public int getIdItem() {
        return idItem;
    }
    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }
    public int getIdUnit() {
        return idUnit;
    }
    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
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
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getPartNum() {
        return partNumber;
    }
    public void setPartNum(String partNumber) {
        this.partNumber = partNumber;
    }
    public String getUnitSymbol() {
        return unitSymbol;
    }
    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }
    public String getCostCenter() {
        return costCenter;
    }
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getPriceCur() {
        return priceCur;
    }
    public void setPriceCur(double priceCur) {
        this.priceCur = priceCur;
    }
    public double getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    public double getTaxCharged() {
        return taxCharged;
    }
    public void setTaxCharged(double taxCharged) {
        this.taxCharged = taxCharged;
    }
    public double getTaxRetained() {
        return taxRetained;
    }
    public void setTaxRetained(double taxRetained) {
        this.taxRetained = taxRetained;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public String getPartNumber() {
        return partNumber;
    }
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }
    public ArrayList<SWebItemHistory> getlItemHistory() {
        return lItemHistory;
    }
    public void setlItemHistory(ArrayList<SWebItemHistory> lItemHistory) {
        this.lItemHistory = lItemHistory;
    }
    public SWebMaterialRequest getoMaterialRequest() {
        return oMaterialRequest;
    }
    public void setoMaterialRequest(SWebMaterialRequest oMaterialRequest) {
        this.oMaterialRequest = oMaterialRequest;
    }

}
