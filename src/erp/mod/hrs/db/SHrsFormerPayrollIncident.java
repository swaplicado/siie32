/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.Date;

/**
 *
 * @author Juan Barajas
 */
public class SHrsFormerPayrollIncident {

    protected int mnPkTipo;
    protected int mnPkSubtipo;
    protected Date mtFecha;
    protected Date mtFechaInicial;
    protected Date mtFechaFinal;

    public SHrsFormerPayrollIncident() {
        mnPkTipo = 0;
        mnPkSubtipo = 0;
        mtFecha = null;
        mtFechaInicial = null;
        mtFechaFinal = null;
    }

    public void setPkTipo(int n) { mnPkTipo = n; }
    public void setPkSubtipo(int n) { mnPkSubtipo = n; }
    public void setFecha(Date t) { mtFecha = t; }
    public void setFechaInicial(Date t) { mtFechaInicial = t; }
    public void setFechaFinal(Date t) { mtFechaFinal = t; }

    public int getPkTipo() { return mnPkTipo; }
    public int getPkSubtipo() { return mnPkSubtipo; }
    public Date getFecha() { return mtFecha; }
    public Date getFechaInicial() { return mtFechaInicial; }
    public Date getFechaFinal() { return mtFechaFinal; }
}
