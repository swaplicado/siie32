/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 *
 * @author César Orozco
 */
public class SEstimateRequestEtyData {
    int idEstimateRequest;
    int idEty;
    double qty;
    int idItem;
    String nameItem;
    int idUnit;
    String nameUnit;
    String symbol;

    public int getIdEstimateRequest() {
        return idEstimateRequest;
    }

    public int getIdEty() {
        return idEty;
    }

    public double getQty() {
        return qty;
    }

    public int getIdItem() {
        return idItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public String getNameUnit() {
        return nameUnit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setIdEstimateRequest(int idEstimateRequest) {
        this.idEstimateRequest = idEstimateRequest;
    }

    public void setIdEty(int idEty) {
        this.idEty = idEty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    public void setNameUnit(String nameUnit) {
        this.nameUnit = nameUnit;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
