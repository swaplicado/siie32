/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

/**
 * Entidad que representa una línea de detalle de una solicitud de autorización.
 *
 * Encapsula información sobre un artículo o entidad dentro de un documento que
 * requiere autorización. Incluye datos de cantidad, precios, unidades de
 * medida, conversiones de factores, y datos de consumo/ubicación del artículo.
 *
 * @author Adrián Avilés
 * @version 1.0
 */
public class SAuthorizationEty {

    /**
     * Identificador único de la entidad/línea
     */
    int idEty;
    /**
     * Cantidad del artículo
     */
    float qty;
    /**
     * Factor de conversión entre unidades
     */
    float factConv;
    /**
     * Precio unitario en la moneda del sistema
     */
    float priceUnitSys;
    /**
     * Precio unitario en la moneda del documento
     */
    float priceUnit;
    /**
     * Monto total de la línea
     */
    float total;
    /**
     * Identificador del artículo/item
     */
    int idItem;
    /**
     * Clave o código único del artículo
     */
    String itemKey;
    /**
     * Descripción del artículo
     */
    String item;
    /**
     * Identificador de la unidad de medida
     */
    int idUnit;
    /**
     * Nombre de la unidad de medida
     */
    String unit;
    /**
     * Símbolo o abreviatura de la unidad
     */
    String symbol;
    /**
     * Entidad de consumo (ubicación principal)
     */
    String consumeEntity;
    /**
     * Sub-entidad de consumo (ubicación secundaria)
     */
    String subConsumeEntity;
    /**
     * Código funcional o centro de costos
     */
    String fcc;

    /**
     * Obtiene el identificador de la entidad.
     *
     * @return identificador único de la línea
     */
    public int getIdEty() {
        return idEty;
    }

    /**
     * Establece el identificador de la entidad.
     *
     * @param idEty identificador único de la línea
     */
    public void setIdEty(int idEty) {
        this.idEty = idEty;
    }

    /**
     * Obtiene la cantidad del artículo.
     *
     * @return cantidad
     */
    public float getQty() {
        return qty;
    }

    /**
     * Establece la cantidad del artículo.
     *
     * @param qty cantidad
     */
    public void setQty(float qty) {
        this.qty = qty;
    }

    /**
     * Obtiene el factor de conversión entre unidades.
     *
     * @return factor de conversión
     */
    public float getFactConv() {
        return factConv;
    }

    /**
     * Establece el factor de conversión entre unidades.
     *
     * @param factConv factor de conversión
     */
    public void setFactConv(float factConv) {
        this.factConv = factConv;
    }

    /**
     * Obtiene el precio unitario en la moneda del sistema.
     *
     * @return precio unitario en moneda del sistema
     */
    public float getPriceUnitSys() {
        return priceUnitSys;
    }

    /**
     * Establece el precio unitario en la moneda del sistema.
     *
     * @param priceUnitSys precio unitario en moneda del sistema
     */
    public void setPriceUnitSys(float priceUnitSys) {
        this.priceUnitSys = priceUnitSys;
    }

    /**
     * Obtiene el precio unitario en la moneda del documento.
     *
     * @return precio unitario en moneda del documento
     */
    public float getPriceUnit() {
        return priceUnit;
    }

    /**
     * Establece el precio unitario en la moneda del documento.
     *
     * @param priceUnit precio unitario en moneda del documento
     */
    public void setPriceUnit(float priceUnit) {
        this.priceUnit = priceUnit;
    }

    /**
     * Obtiene el monto total de la línea.
     *
     * @return monto total
     */
    public float getTotal() {
        return total;
    }

    /**
     * Obtiene la entidad de consumo (ubicación principal).
     *
     * @return entidad de consumo
     */
    public String getConsumeEntity() {
        return consumeEntity;
    }

    /**
     * Obtiene la sub-entidad de consumo (ubicación secundaria).
     *
     * @return sub-entidad de consumo
     */
    public String getSubConsumeEntity() {
        return subConsumeEntity;
    }

    /**
     * Obtiene el código funcional o centro de costos.
     *
     * @return código funcional (FCC)
     */
    public String getFcc() {
        return fcc;
    }

    /**
     * Establece el monto total de la línea.
     *
     * @param total monto total
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * Obtiene el identificador del artículo.
     *
     * @return identificador del artículo/item
     */
    public int getIdItem() {
        return idItem;
    }

    /**
     * Establece el identificador del artículo.
     *
     * @param idItem identificador del artículo/item
     */
    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    /**
     * Obtiene la clave o código único del artículo.
     *
     * @return clave del artículo
     */
    public String getItemKey() {
        return itemKey;
    }

    /**
     * Establece la clave o código único del artículo.
     *
     * @param itemKey clave del artículo
     */
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    /**
     * Obtiene la descripción del artículo.
     *
     * @return descripción del artículo
     */
    public String getItem() {
        return item;
    }

    /**
     * Establece la descripción del artículo.
     *
     * @param item descripción del artículo
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * Obtiene el identificador de la unidad de medida.
     *
     * @return identificador de la unidad
     */
    public int getIdUnit() {
        return idUnit;
    }

    /**
     * Establece el identificador de la unidad de medida.
     *
     * @param idUnit identificador de la unidad
     */
    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    /**
     * Obtiene el nombre de la unidad de medida.
     *
     * @return nombre de la unidad
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Establece el nombre de la unidad de medida.
     *
     * @param unit nombre de la unidad
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Obtiene el símbolo o abreviatura de la unidad.
     *
     * @return símbolo de la unidad
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Establece el símbolo o abreviatura de la unidad.
     *
     * @param symbol símbolo de la unidad
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Establece la entidad de consumo (ubicación principal).
     *
     * @param consumeEntity entidad de consumo
     */
    public void setConsumeEntity(String consumeEntity) {
        this.consumeEntity = consumeEntity;
    }

    /**
     * Establece la sub-entidad de consumo (ubicación secundaria).
     *
     * @param subConsumeEntity sub-entidad de consumo
     */
    public void setSubConsumeEntity(String subConsumeEntity) {
        this.subConsumeEntity = subConsumeEntity;
    }

    /**
     * Establece el código funcional o centro de costos.
     *
     * @param fcc código funcional (FCC)
     */
    public void setFcc(String fcc) {
        this.fcc = fcc;
    }
}
