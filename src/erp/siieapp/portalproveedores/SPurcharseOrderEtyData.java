/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 * Representa una partida de una orden de compra expuesta al portal de
 * proveedores.
 * <p>
 * Contiene los importes en moneda local y en moneda extranjera, así como la
 * información del artículo y las unidades de medida original y de compra. La
 * cabecera de la orden se maneja en {@link SPurcharseOrdersData}.
 * </p>
 *
 * @author swaplicado
 */
public class SPurcharseOrderEtyData {

    /**
     * Año fiscal del documento al que pertenece la partida.
     */
    int idYear;
    /**
     * ID del documento (orden de compra) al que pertenece la partida.
     */
    int idDoc;
    /**
     * Número de partida dentro del documento.
     */
    int idEty;
    /**
     * Clave del concepto de la partida.
     */
    String conceptKey;
    /**
     * Descripción del concepto de la partida.
     */
    String concept;
    /**
     * Referencia adicional de la partida.
     */
    String ref;
    /**
     * Cantidad de la partida en la unidad de compra.
     */
    float qty;
    /**
     * Precio unitario en moneda local.
     */
    float priceUnit;
    /**
     * Subtotal en moneda local.
     */
    double sTot;
    /**
     * Impuestos trasladados en moneda local.
     */
    double taxCharged;
    /**
     * Impuestos retenidos en moneda local.
     */
    double taxRetained;
    /**
     * Total en moneda local.
     */
    double tot;
    /**
     * Precio unitario real en moneda local (considerando conversiones).
     */
    double priceUReal;
    /**
     * Precio unitario en moneda extranjera.
     */
    double priceUCur;
    /**
     * Subtotal en moneda extranjera.
     */
    double sTotCur;
    /**
     * Impuestos trasladados en moneda extranjera.
     */
    double taxChargedCur;
    /**
     * Impuestos retenidos en moneda extranjera.
     */
    double taxRetainedCur;
    /**
     * Total en moneda extranjera.
     */
    double totCur;
    /**
     * Precio unitario real en moneda extranjera.
     */
    double priceURealCur;
    /**
     * ID del artículo.
     */
    int idItem;
    /**
     * Nombre del artículo.
     */
    String name;
    /**
     * Clave del artículo.
     */
    String item;
    /**
     * ID de la unidad de medida de compra.
     */
    int idUnit;
    /**
     * Nombre de la unidad de medida de compra.
     */
    String unit;
    /**
     * ID de la unidad de medida original del artículo.
     */
    int idOriginalUnit;
    /**
     * Nombre de la unidad de medida original del artículo.
     */
    String originalUnit;

    public int getIdYear() {
        return idYear;
    }

    public int getIdDoc() {
        return idDoc;
    }

    public int getIdEty() {
        return idEty;
    }

    public String getConceptKey() {
        return conceptKey;
    }

    public String getConcept() {
        return concept;
    }

    public String getRef() {
        return ref;
    }

    public float getQty() {
        return qty;
    }

    public float getPriceUnit() {
        return priceUnit;
    }

    public double getsTot() {
        return sTot;
    }

    public double getTaxCharged() {
        return taxCharged;
    }

    public double getTaxRetained() {
        return taxRetained;
    }

    public double getTot() {
        return tot;
    }

    public double getPriceUReal() {
        return priceUReal;
    }

    public double getPriceUCur() {
        return priceUCur;
    }

    public double getsTotCur() {
        return sTotCur;
    }

    public double getTaxChargedCur() {
        return taxChargedCur;
    }

    public double getTaxRetainedCur() {
        return taxRetainedCur;
    }

    public double getTotCur() {
        return totCur;
    }

    public double getPriceURealCur() {
        return priceURealCur;
    }

    public int getIdItem() {
        return idItem;
    }

    public String getName() {
        return name;
    }

    public String getItem() {
        return item;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public String getUnit() {
        return unit;
    }

    public int getIdOriginalUnit() {
        return idOriginalUnit;
    }

    public String getOriginalUnit() {
        return originalUnit;
    }

    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }

    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }

    public void setIdEty(int idEty) {
        this.idEty = idEty;
    }

    public void setConceptKey(String conceptKey) {
        this.conceptKey = conceptKey;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public void setPriceUnit(float priceUnit) {
        this.priceUnit = priceUnit;
    }

    public void setsTot(double sTot) {
        this.sTot = sTot;
    }

    public void setTaxCharged(double taxCharged) {
        this.taxCharged = taxCharged;
    }

    public void setTaxRetained(double taxRetained) {
        this.taxRetained = taxRetained;
    }

    public void setTot(double tot) {
        this.tot = tot;
    }

    public void setPriceUReal(double priceUReal) {
        this.priceUReal = priceUReal;
    }

    public void setPriceUCur(double priceUCur) {
        this.priceUCur = priceUCur;
    }

    public void setsTotCur(double sTotCur) {
        this.sTotCur = sTotCur;
    }

    public void setTaxChargedCur(double taxChargedCur) {
        this.taxChargedCur = taxChargedCur;
    }

    public void setTaxRetainedCur(double taxRetainedCur) {
        this.taxRetainedCur = taxRetainedCur;
    }

    public void setTotCur(double totCur) {
        this.totCur = totCur;
    }

    public void setPriceURealCur(double priceURealCur) {
        this.priceURealCur = priceURealCur;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setIdUnit(int idUnit) {
        this.idUnit = idUnit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setIdOriginalUnit(int idOriginalUnit) {
        this.idOriginalUnit = idOriginalUnit;
    }

    public void setOriginalUnit(String originalUnit) {
        this.originalUnit = originalUnit;
    }

}
