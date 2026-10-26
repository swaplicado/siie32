/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

/**
 * Representa un renglón del estado de cuenta de un proveedor.
 * <p>
 * Cada instancia corresponde a un movimiento contable (o al saldo inicial del
 * periodo) que se expone al portal de proveedores vía
 * {@link SAccountStatusApi}.
 * </p>
 *
 * @author swaplicado
 */
public class SAccountStatusData {

    /**
     * Año fiscal del movimiento.
     */
    int idYear;
    /**
     * Fecha del movimiento (formato {@code YYYY-MM-DD}).
     */
    String date;
    /**
     * Concepto o descripción del movimiento.
     */
    String concept;
    /**
     * Importe en moneda extranjera (suma de cargo y abono en moneda de la
     * partida).
     */
    double importForeignCurrency;
    /**
     * Tipo de cambio aplicado al movimiento.
     */
    float excRate;
    /**
     * Clave de la moneda del movimiento (ej. {@code USD}, {@code MXN}).
     */
    String currencyCode;
    /**
     * Cargo del movimiento en moneda local.
     */
    double debit;
    /**
     * Abono del movimiento en moneda local.
     */
    double credit;

    public int getIdYear() {
        return idYear;
    }

    public String getDate() {
        return date;
    }

    public String getConcept() {
        return concept;
    }

    public double getImportForeignCurrency() {
        return importForeignCurrency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getDebit() {
        return debit;
    }

    public double getCredit() {
        return credit;
    }

    public float getExcRate() {
        return excRate;
    }

    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public void setImportForeignCurrency(double importForeignCurrency) {
        this.importForeignCurrency = importForeignCurrency;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public void setExcRate(float excRate) {
        this.excRate = excRate;
    }

}
