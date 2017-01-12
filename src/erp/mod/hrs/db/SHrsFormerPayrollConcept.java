/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;

/**
 *
 * @author Juan Barajas
 */
public class SHrsFormerPayrollConcept {

    protected int mnPkTipoConcepto;
    protected int mnPkSubtipoConcepto;
    protected String msClaveEmpresa;
    protected int mnClaveOficial;
    protected String msConcepto;
    protected double mdCantidad;
    protected int mnHoras_r;
    protected double mdTotalGravado;
    protected double mdTotalExento;

    protected String msClaveIncapacidad;
    protected boolean mbExtIsFound;

    protected SHrsFormerPayrollExtraTime moChildExtraTimes;
    protected ArrayList<SHrsFormerPayrollIncident> moChildIncidents;

    public SHrsFormerPayrollConcept() {
        mnPkTipoConcepto = 0;
        mnPkSubtipoConcepto = 0;
        msClaveEmpresa = "";
        mnClaveOficial = 0;
        msConcepto = "";
        mdCantidad = 0;
        mnHoras_r = 0;
        mdTotalGravado = 0;
        mdTotalExento = 0;

        msClaveIncapacidad = "";
        mbExtIsFound = false;

        moChildExtraTimes = new SHrsFormerPayrollExtraTime();
        moChildIncidents = new ArrayList<SHrsFormerPayrollIncident>();
    }

    public void setPkTipoConcepto(int n) { mnPkTipoConcepto = n; }
    public void setPkSubtipoConcepto(int n) { mnPkSubtipoConcepto = n; }
    public void setClaveEmpresa(String s) { msClaveEmpresa = s; }
    public void setClaveOficial(int n) { mnClaveOficial = n; }
    public void setConcepto(String s) { msConcepto = s; }
    public void setCantidad(double d) { mdCantidad = d; }
    public void setHoras_r(int n) { mnHoras_r = n; }
    public void setTotalGravado(double d) { mdTotalGravado = d; }
    public void setTotalExento(double d) { mdTotalExento = d; }

    public void setClaveIncapacidad(String s) { msClaveIncapacidad = s; }
    public void setExtIsFound(boolean b) { mbExtIsFound = b; }

    public int getPkTipoConcepto() { return mnPkTipoConcepto; }
    public int getPkSubtipoConcepto() { return mnPkSubtipoConcepto; }
    public String getClaveEmpresa() { return msClaveEmpresa; }
    public int getClaveOficial() { return mnClaveOficial; }
    public String getConcepto() { return msConcepto; }
    public double getCantidad() { return mdCantidad; }
    public int getHoras_r() { return mnHoras_r; }
    public double getTotalGravado() { return mdTotalGravado; }
    public double getTotalExento() { return mdTotalExento; }

    public String getClaveIncapacidad() { return msClaveIncapacidad; }
    public boolean getExtIsFound() { return mbExtIsFound; }

    public void setChildPayrollExtraTimes(SHrsFormerPayrollExtraTime o) { moChildExtraTimes = o; }
    public SHrsFormerPayrollExtraTime getChildPayrollExtraTimes() { return moChildExtraTimes; }

    public ArrayList<SHrsFormerPayrollIncident> getChildPayrollIncident() { return moChildIncidents; }
}
