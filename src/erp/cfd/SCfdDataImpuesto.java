/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import cfd.DCfdTax;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver40.DCfdi40Catalogs;
import erp.mod.SModSysConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores, Isabel Servín
 */
public class SCfdDataImpuesto implements DCfdTax {
    
    /*
     * NOTE:
     * The trick identifing taxes is that its text is equal (ignoring case) to the corresponding tax description in SIIE taxes catalog!, WTF!
     */
    
    protected int mnImpuestoTipo;
    protected int mnImpuesto;
    protected String msImpuestoClave;
    protected double mdBase;
    protected double mdTasa;
    protected double mdImporte;
    protected String msTipoFactor;
    
    private String getFactorTypeSat(String factor) throws Exception {
        String factorType = "";
        
        if (factor.compareToIgnoreCase(DCfdi33Catalogs.FAC_TP_TASA) == 0) {
            factorType = DCfdi33Catalogs.FAC_TP_TASA;
        }
        else if (factor.compareToIgnoreCase(DCfdi33Catalogs.FAC_TP_CUOTA) == 0) {
            factorType = DCfdi33Catalogs.FAC_TP_CUOTA;
        }
        else if (factor.compareToIgnoreCase(DCfdi33Catalogs.FAC_TP_EXENTO) == 0) {
            factorType = DCfdi33Catalogs.FAC_TP_EXENTO;
        }
        else {
            throw new Exception("El tipo de factor (" + factor + ") no es válido.");
        }
        
        return factorType;
    }
    
    public SCfdDataImpuesto() {
        mnImpuestoTipo = 0;
        mnImpuesto = 0;
        msImpuestoClave = "";
        mdBase = 0;
        mdTasa = 0;
        mdImporte = 0;
        msTipoFactor = "";
    }
    
    public void setImpuestoTipo(int n) { mnImpuestoTipo = n; }
    public void setImpuesto(int n) { mnImpuesto = n; }
    @Override
    public void setImpuestoClave(String s) { msImpuestoClave = s; }
    public void setBase(double d) { mdBase = d; }
    @Override
    public void setTasa(double d) { mdTasa = d; }
    public void setImporte(double d) { mdImporte = d; }
    @Override
    public void setTipoFactor(String s) { msTipoFactor = s; }
    
    public int getImpuestoTipo() { return mnImpuestoTipo; }
    public int getImpuesto() { return mnImpuesto; }
    @Override
    public String getImpuestoClave() { return msImpuestoClave; }
    @Override
    public double getBase() { return mdBase; }
    @Override
    public double getTasa() { return mdTasa; }
    @Override
    public double getImporte() { return mdImporte; }
    @Override
    public String getTipoFactor() { return msTipoFactor; }

    @Override
    public void clearBaseImporte() {
        mdBase = 0;
        mdImporte = 0;
    }

    @Override
    public void addBaseImporte(double base, double importe) {
        mdBase = SLibUtils.round(mdBase + base, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        mdImporte = SLibUtils.round(mdImporte + importe, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
    }
    
    /**
     * Create node for taxes in concepts.
     * @return Node
     * @throws Exception 
     */
    public cfd.DElement createRootElementConceptoImpuesto33() throws Exception {
        cfd.DElement impuesto = null;
        
        switch (mnImpuestoTipo) {
            case SModSysConsts.FINS_TP_TAX_RETAINED:
                cfd.ver33.DElementConceptoImpuestoRetencion conceptoImpuestoRetencion = new cfd.ver33.DElementConceptoImpuestoRetencion();
                
                if (msTipoFactor.compareToIgnoreCase(DCfdi33Catalogs.FAC_TP_EXENTO) == 0) { // XXX jbarajas falta las constantes para comparar contra tipo exento
                    throw new Exception("Error al generar el nodo impuesto 'retenido' el tipo de factor debe ser distinto de exento.");
                }
                
                conceptoImpuestoRetencion.getAttBase().setDouble(mdBase);
                conceptoImpuestoRetencion.getAttImpuesto().setString(msImpuestoClave);
                conceptoImpuestoRetencion.getAttTipoFactor().setString(getFactorTypeSat(msTipoFactor));
                
                conceptoImpuestoRetencion.getAttTasaOCuota().setDouble(mdTasa);
                conceptoImpuestoRetencion.getAttImporte().setDouble(mdImporte);
                
                impuesto = conceptoImpuestoRetencion;
                break;
                
            case SModSysConsts.FINS_TP_TAX_CHARGED:
                cfd.ver33.DElementConceptoImpuestoTraslado conceptoImpuestoTraslado = new cfd.ver33.DElementConceptoImpuestoTraslado();
                
                conceptoImpuestoTraslado.getAttBase().setDouble(mdBase);
                conceptoImpuestoTraslado.getAttImpuesto().setString(msImpuestoClave);
                conceptoImpuestoTraslado.getAttTipoFactor().setString(getFactorTypeSat(msTipoFactor));
                
                if (msTipoFactor.compareToIgnoreCase(DCfdi33Catalogs.FAC_TP_EXENTO) != 0) {
                    conceptoImpuestoTraslado.getAttTasaOCuota().setDouble(mdTasa);
                    conceptoImpuestoTraslado.getAttImporte().setDouble(mdImporte);
                }
                
                impuesto = conceptoImpuestoTraslado;
                break;
                
            default:
                throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + mnImpuestoTipo + ").");
        }
        
        return impuesto;
    }
    
    /**
     * Create node for taxes in concepts.
     * CFDI 4.0
     * @return Node
     * @throws Exception 
     */
    public cfd.DElement createRootElementConceptoImpuesto40() throws Exception {
        cfd.DElement impuesto = null;
        
        switch (mnImpuestoTipo) {
            case SModSysConsts.FINS_TP_TAX_RETAINED:
                cfd.ver40.DElementConceptoImpuestoRetencion conceptoImpuestoRetencion = new cfd.ver40.DElementConceptoImpuestoRetencion();
                
                if (msTipoFactor.compareToIgnoreCase(DCfdi40Catalogs.FAC_TP_EXENTO) == 0) { // XXX jbarajas falta las constantes para comparar contra tipo exento
                    throw new Exception("Error al generar el nodo impuesto 'retenido' el tipo de factor debe ser distinto de exento.");
                }
                
                conceptoImpuestoRetencion.getAttBase().setDouble(mdBase);
                conceptoImpuestoRetencion.getAttImpuesto().setString(msImpuestoClave);
                conceptoImpuestoRetencion.getAttTipoFactor().setString(getFactorTypeSat(msTipoFactor));
                
                conceptoImpuestoRetencion.getAttTasaOCuota().setDouble(mdTasa);
                conceptoImpuestoRetencion.getAttImporte().setDouble(mdImporte);
                
                impuesto = conceptoImpuestoRetencion;
                break;
                
            case SModSysConsts.FINS_TP_TAX_CHARGED:
                cfd.ver40.DElementConceptoImpuestoTraslado conceptoImpuestoTraslado = new cfd.ver40.DElementConceptoImpuestoTraslado();
                
                conceptoImpuestoTraslado.getAttBase().setDouble(mdBase);
                conceptoImpuestoTraslado.getAttImpuesto().setString(msImpuestoClave);
                conceptoImpuestoTraslado.getAttTipoFactor().setString(getFactorTypeSat(msTipoFactor));
                
                if (msTipoFactor.compareToIgnoreCase(DCfdi40Catalogs.FAC_TP_EXENTO) != 0) {
                    conceptoImpuestoTraslado.getAttTasaOCuota().setDouble(mdTasa);
                    conceptoImpuestoTraslado.getAttImporte().setDouble(mdImporte);
                }
                
                impuesto = conceptoImpuestoTraslado;
                break;
                
            default:
                throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + mnImpuestoTipo + ").");
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
        
        switch (mnImpuestoTipo) {
            case SModSysConsts.FINS_TP_TAX_RETAINED:
                cfd.ver33.DElementImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementImpuestoRetencion();
                
                impuestoRetencion.getAttImpuesto().setString(msImpuestoClave);
                impuestoRetencion.getAttImporte().setDouble(mdImporte);
                
                impuesto = impuestoRetencion;
                break;
            case SModSysConsts.FINS_TP_TAX_CHARGED:
                cfd.ver33.DElementImpuestoTraslado impuestoTraslado = new cfd.ver33.DElementImpuestoTraslado();
                
                impuestoTraslado.getAttImpuesto().setString(msImpuestoClave);
                impuestoTraslado.getAttTipoFactor().setString(getFactorTypeSat(msTipoFactor));
                impuestoTraslado.getAttTasaOCuota().setDouble(mdTasa);
                impuestoTraslado.getAttImporte().setDouble(mdImporte);
                
                impuesto = impuestoTraslado;
                break;
            default:
                throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + mnImpuestoTipo + ").");
        }
        
        return impuesto;
    }
    
    /**
     * Create node for taxes in CFDI.
     * CFDI 4.0
     * @return Node
     * @throws Exception 
     */
    public cfd.DElement createRootElementImpuesto40() throws Exception {
        cfd.DElement impuesto = null;
        
        switch (mnImpuestoTipo) {
            case SModSysConsts.FINS_TP_TAX_RETAINED:
                cfd.ver40.DElementImpuestoRetencion impuestoRetencion = new cfd.ver40.DElementImpuestoRetencion();
                
                impuestoRetencion.getAttImpuesto().setString(msImpuestoClave);
                impuestoRetencion.getAttImporte().setDouble(mdImporte);
                
                impuesto = impuestoRetencion;
                break;
            case SModSysConsts.FINS_TP_TAX_CHARGED:
                cfd.ver40.DElementImpuestoTraslado impuestoTraslado = new cfd.ver40.DElementImpuestoTraslado();
                
                impuestoTraslado.getAttBase().setDouble(mdBase);
                impuestoTraslado.getAttImpuesto().setString(msImpuestoClave);
                impuestoTraslado.getAttTipoFactor().setString(getFactorTypeSat(msTipoFactor));
                if (!impuestoTraslado.getAttTipoFactor().getString().equals(DCfdi40Catalogs.FAC_TP_EXENTO)) {
                    impuestoTraslado.getAttTasaOCuota().setDouble(mdTasa);
                    impuestoTraslado.getAttImporte().setDouble(mdImporte);
                }
                
                impuesto = impuestoTraslado;
                break;
            default:
                throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + mnImpuestoTipo + ").");
        }
        
        return impuesto;
    }
}
