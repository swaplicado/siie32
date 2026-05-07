/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 * Representa una partida de una solicitud de cotización.
 * <p>
 * Contiene el artículo solicitado, la cantidad y la unidad de medida
 * correspondientes a una línea de la solicitud ({@link SEstimateRequestData}).
 * </p>
 *
 * @author César Orozco
 */
public class SEstimateRequestEtyData {

    /**
     * ID de la solicitud de cotización a la que pertenece esta partida.
     */
    int idEstimateRequest;
    /**
     * Número de partida dentro de la solicitud.
     */
    int idEty;
    /**
     * Cantidad solicitada.
     */
    double qty;
    /**
     * ID del artículo solicitado.
     */
    int idItem;
    /**
     * Nombre del artículo solicitado.
     */
    String nameItem;
    /**
     * ID de la unidad de medida.
     */
    int idUnit;
    /**
     * Nombre de la unidad de medida.
     */
    String nameUnit;
    /**
     * Símbolo de la unidad de medida (ej. {@code kg}, {@code pza}).
     */
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
