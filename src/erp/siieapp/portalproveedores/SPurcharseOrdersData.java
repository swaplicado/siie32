/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 * Representa la cabecera de una orden de compra expuesta al portal de
 * proveedores.
 * <p>
 * Contiene los datos generales del documento: proveedor, fechas, importes en
 * moneda local y extranjera, y datos de identificación fiscal. Las partidas de
 * la orden se manejan en {@link SPurcharseOrderEtyData}.
 * </p>
 *
 * @author César Orozco
 */
public class SPurcharseOrdersData {

    /**
     * Año fiscal del documento.
     */
    int idYear;
    /**
     * ID del documento (orden de compra).
     */
    int idDoc;
    /**
     * Fecha del documento.
     */
    String date;
    /**
     * Fecha de inicio del crédito.
     */
    String dateStartCred;
    /**
     * Fecha de entrega pactada del documento.
     */
    String dateDocDelivery;
    /**
     * Serie del folio del documento.
     */
    String serie;
    /**
     * Número de folio del documento.
     */
    String folio;
    /**
     * Referencia completa del documento (serie + folio).
     */
    String numRef;
    /**
     * Días de crédito pactados.
     */
    int daysCred;
    /**
     * Tipo de cambio aplicado al documento.
     */
    float excRate;
    /**
     * Subtotal en moneda local.
     */
    double stot;
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
     * Subtotal en moneda extranjera.
     */
    double stotCur;
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
     * ID del socio de negocio (proveedor).
     */
    int idBP;
    /**
     * Nombre del proveedor.
     */
    String bp;
    /**
     * Nombre de la sucursal del proveedor.
     */
    String bpb;
    /**
     * RFC o identificador fiscal del proveedor.
     */
    String fiscalId;
    /**
     * Clave de la moneda del documento (ej. {@code USD}).
     */
    String fCurKey;
    /**
     * Clave de la moneda local (ej. {@code MXN}).
     */
    String fCurKeyLocal;

    public int getIdYear() {
        return idYear;
    }

    public int getIdDoc() {
        return idDoc;
    }

    public String getDate() {
        return date;
    }

    public String getDateStartCred() {
        return dateStartCred;
    }

    public String getDateDocDelivery() {
        return dateDocDelivery;
    }

    public String getNumRef() {
        return numRef;
    }

    public int getDaysCred() {
        return daysCred;
    }

    public float getExcRate() {
        return excRate;
    }

    public double getStot() {
        return stot;
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

    public double getStotCur() {
        return stotCur;
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

    public int getIdBP() {
        return idBP;
    }

    public String getBp() {
        return bp;
    }

    public String getBpb() {
        return bpb;
    }

    public String getFiscalId() {
        return fiscalId;
    }

    public String getfCurKey() {
        return fCurKey;
    }

    public String getfCurKeyLocal() {
        return fCurKeyLocal;
    }

    public String getSerie() {
        return serie;
    }

    public String getFolio() {
        return folio;
    }

    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }

    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateStartCred(String dateStartCred) {
        this.dateStartCred = dateStartCred;
    }

    public void setDateDocDelivery(String dateDocDelivery) {
        this.dateDocDelivery = dateDocDelivery;
    }

    public void setNumRef(String numRef) {
        this.numRef = numRef;
    }

    public void setDaysCred(int daysCred) {
        this.daysCred = daysCred;
    }

    public void setExcRate(float excRate) {
        this.excRate = excRate;
    }

    public void setStot(double stot) {
        this.stot = stot;
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

    public void setStotCur(double stotCur) {
        this.stotCur = stotCur;
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

    public void setIdBP(int idBP) {
        this.idBP = idBP;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public void setBpb(String bpb) {
        this.bpb = bpb;
    }

    public void setFiscalId(String fiscalId) {
        this.fiscalId = fiscalId;
    }

    public void setfCurKey(String fCurKey) {
        this.fCurKey = fCurKey;
    }

    public void setfCurKeyLocal(String fCurKeyLocal) {
        this.fCurKeyLocal = fCurKeyLocal;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }
}
