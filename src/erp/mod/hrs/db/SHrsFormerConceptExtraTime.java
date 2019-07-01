/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

/**
 *
 * @author Juan Barajas
 */
public class SHrsFormerConceptExtraTime {

    protected double mdDias;
    protected String msTipoHoras;
    protected int mnHorasExtra;
    protected double mdImportePagado;

    public SHrsFormerConceptExtraTime() {
        mdDias = 0;
        msTipoHoras = "";
        mnHorasExtra = 0;
        mdImportePagado = 0;
    }

    public void setDias(double d) { mdDias = d; }
    public void setTipoHoras(String s) { msTipoHoras = s; }
    public void setHorasExtra(int n) { mnHorasExtra = n; }
    public void setImportePagado(double d) { mdImportePagado = d; }

    public double getDias() { return mdDias; }
    public String getTipoHoras() { return msTipoHoras; }
    public int getHorasExtra() { return mnHorasExtra; }
    public double getImportePagado() { return mdImportePagado; }
}
