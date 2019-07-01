/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SHrsFormerReceiptConcept {

    protected int mnPkTipoConcepto;
    protected int mnPkSubtipoConcepto;
    protected String msClaveEmpresa;
    protected int mnClaveOficial;
    protected String msConcepto;
    protected double mdCantidad;
    protected int mnHoras_r;
    protected double mdTotalGravado;
    protected double mdTotalExento;
    
    protected double mdXtaSubsidioEmpleo;
    protected String msXtaClaveIncapacidad;
    protected boolean mbAuxFound;

    protected SHrsFormerConceptExtraTime moChildExtraTime;
    protected ArrayList<SHrsFormerConceptIncident> moChildIncidents;

    public SHrsFormerReceiptConcept() {
        mnPkTipoConcepto = 0;
        mnPkSubtipoConcepto = 0;
        msClaveEmpresa = "";
        mnClaveOficial = 0;
        msConcepto = "";
        mdCantidad = 0;
        mnHoras_r = 0;
        mdTotalGravado = 0;
        mdTotalExento = 0;
        
        mdXtaSubsidioEmpleo = 0;
        msXtaClaveIncapacidad = "";
        mbAuxFound = false;

        moChildExtraTime = new SHrsFormerConceptExtraTime();
        moChildIncidents = new ArrayList<>();
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
    
    public void setXtaSubsidioEmpleo(double d) { mdXtaSubsidioEmpleo = d; }
    public void setXtaClaveIncapacidad(String s) { msXtaClaveIncapacidad = s; }
    public void setAuxFound(boolean b) { mbAuxFound = b; }

    public void setChildExtraTime(SHrsFormerConceptExtraTime o) { moChildExtraTime = o; }
    
    public int getPkTipoConcepto() { return mnPkTipoConcepto; }
    public int getPkSubtipoConcepto() { return mnPkSubtipoConcepto; }
    public String getClaveEmpresa() { return msClaveEmpresa; }
    public int getClaveOficial() { return mnClaveOficial; }
    public String getConcepto() { return msConcepto; }
    public double getCantidad() { return mdCantidad; }
    public int getHoras_r() { return mnHoras_r; }
    public double getTotalGravado() { return mdTotalGravado; }
    public double getTotalExento() { return mdTotalExento; }
    
    public double getXtaSubsidioEmpleo() { return mdXtaSubsidioEmpleo; }
    public String getXtaClaveIncapacidad() { return msXtaClaveIncapacidad; }
    public boolean isAuxFound() { return mbAuxFound; }
    
    public SHrsFormerConceptExtraTime getChildExtraTime() { return moChildExtraTime; }
    public ArrayList<SHrsFormerConceptIncident> getChildIncidents() { return moChildIncidents; }
    
    public double getTotalImporte() { return SLibUtils.roundAmount(mdTotalGravado + mdTotalExento); }
}
