/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

/**
 *
 * @author AdrianAviles
 */
public class SAuthorizationEty {
    int idEty;
    float qty;
    float factConv;
    float priceUnitSys;
    float priceUnit;
    float total;
    int idItem;
    String itemKey;
    String item;
    int idUnit;
    String unit;
    String symbol;
    String consumeEntity;
    String subConsumeEntity;
    String fcc;

    public int getIdEty() {
        return idEty;
    }

    public void setIdEty(int idEty) {
        this.idEty = idEty;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getFactConv() {
        return factConv;
    }

    public void setFactConv(float factConv) {
        this.factConv = factConv;
    }

    public float getPriceUnitSys() {
        return priceUnitSys;
    }

    public void setPriceUnitSys(float priceUnitSys) {
        this.priceUnitSys = priceUnitSys;
    }

    public float getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(float priceUnit) {
        this.priceUnit = priceUnit;
    }

    public float getTotal() {
        return total;
    }
    
    public String getConsumeEntity() {
        return consumeEntity;
    }

    public String getSubConsumeEntity() {
        return subConsumeEntity;
    }

    public String getFcc() {
        return fcc;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setConsumeEntity(String consumeEntity) {
        this.consumeEntity = consumeEntity;
    }

    public void setSubConsumeEntity(String subConsumeEntity) {
        this.subConsumeEntity = subConsumeEntity;
    }

    public void setFcc(String fcc) {
        this.fcc = fcc;
    }
}
