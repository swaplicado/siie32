/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import erp.mod.SModSysConsts;

/**
 *
 * @author Juan Barajas
 */
public class SCfdDataImpuesto {
    
    protected int mnImpuestoBasico;
    protected int mnImpuesto;
    protected String msImpuestoClave;
    protected double mdBase;
    protected double mdTasa;
    protected double mdImporte;
    protected String msTipoFactor;
    
    public SCfdDataImpuesto() {
        mnImpuestoBasico = 0;
        mnImpuesto = 0;
        msImpuestoClave = "";
        mdBase = 0;
        mdTasa = 0;
        mdImporte = 0;
        msTipoFactor = "";
    }
    
    public void setImpuestoBasico(int n) { mnImpuestoBasico = n; }
    public void setImpuesto(int n) { mnImpuesto = n; }
    public void setImpuestoClave(String s) { msImpuestoClave = s; }
    public void setBase(double d) { mdBase = d; }
    public void setTasa(double d) { mdTasa = d; }
    public void setImporte(double d) { mdImporte = d; }
    public void setTipoFactor(String s) { msTipoFactor = s; }
    
    public int getImpuestoBasico() { return mnImpuestoBasico; }
    public int getImpuesto() { return mnImpuesto; }
    public String getImpuestoClave() { return msImpuestoClave; }
    public double getBase() { return mdBase; }
    public double getTasa() { return mdTasa; }
    public double getImporte() { return mdImporte; }
    public String getTipopFactor() { return msTipoFactor; }
    
    /**
     * Create node for taxes in concepts.
     * @return Node
     * @throws Exception 
     */
    public cfd.DElement createRootElementConceptoImpuesto33() throws Exception {
        cfd.DElement impuesto = null;
        
        switch (mnImpuestoBasico) {
            case SModSysConsts.FINS_TP_TAX_RETAINED:
                cfd.ver33.DElementConceptoImpuestoRetencion conceptoImpuestoRetencion = new cfd.ver33.DElementConceptoImpuestoRetencion();
                
                if (msTipoFactor.compareTo(msTipoFactor) == 0) { // XXX jbarajas falta las constantes para comparar contra tipo exento
                    throw new Exception("Error al generar el nodo impuesto 'retenido' el tipo de factor debe ser distinto de exento.");
                }
                
                conceptoImpuestoRetencion.getAttBase().setDouble(mdBase);
                conceptoImpuestoRetencion.getAttImpuesto().setString(msImpuestoClave);
                conceptoImpuestoRetencion.getAttTipoFactor().setString(msTipoFactor);
                conceptoImpuestoRetencion.getAttTasaOCuota().setDouble(mdTasa);
                conceptoImpuestoRetencion.getAttImporte().setDouble(mdImporte);
                
                impuesto = conceptoImpuestoRetencion;
                break;
            case SModSysConsts.FINS_TP_TAX_CHARGED:
                cfd.ver33.DElementConceptoImpuestoTraslado conceptoImpuestoTraslado = new cfd.ver33.DElementConceptoImpuestoTraslado();
                
                conceptoImpuestoTraslado.getAttBase().setDouble(mdBase);
                conceptoImpuestoTraslado.getAttImpuesto().setString(msImpuestoClave);
                conceptoImpuestoTraslado.getAttTipoFactor().setString(msTipoFactor);
                if (msTipoFactor.compareTo(msTipoFactor) == 0) { // XXX jbarajas falta las constantes para comparar contra tipo exento
                    conceptoImpuestoTraslado.getAttTasaOCuota().setDouble(mdTasa);
                    conceptoImpuestoTraslado.getAttImporte().setDouble(mdImporte);
                }
                
                impuesto = conceptoImpuestoTraslado;
                break;
            default:
                throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + mnImpuestoBasico + ").");
        }
        
        return impuesto;
    }
    
    /**
     * Create node for taxes in CFDI.
     * @return Node
     * @throws Exception 
     */
    public cfd.DElement createRootElementImpuesto33() throws Exception {
        cfd.DElement impuesto = null;
        
        switch (mnImpuestoBasico) {
            case SModSysConsts.FINS_TP_TAX_RETAINED:
                cfd.ver33.DElementImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementImpuestoRetencion();
                
                impuestoRetencion.getAttImpuesto().setString(msImpuestoClave);
                impuestoRetencion.getAttImporte().setDouble(mdImporte);
                
                impuesto = impuestoRetencion;
                break;
            case SModSysConsts.FINS_TP_TAX_CHARGED:
                cfd.ver33.DElementImpuestoTraslado impuestoTraslado = new cfd.ver33.DElementImpuestoTraslado();
                
                impuestoTraslado.getAttImpuesto().setString(msImpuestoClave);
                impuestoTraslado.getAttTipoFactor().setString(msTipoFactor);
                impuestoTraslado.getAttTasaOCuota().setDouble(mdTasa);
                impuestoTraslado.getAttImporte().setDouble(mdImporte);
                
                impuesto = impuestoTraslado;
                break;
            default:
                throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + mnImpuestoBasico + ").");
        }
        
        return impuesto;
    }
}
