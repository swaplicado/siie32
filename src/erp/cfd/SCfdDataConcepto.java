/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import cfd.DAttributeOptionImpuestoRetencion;
import cfd.DAttributeOptionImpuestoTraslado;
import cfd.ver33.DElementConceptoImpuestos;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDpsEntryTax;
import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas
 */
public class SCfdDataConcepto {
    
    protected String msClaveProdServ;
    protected String msNoIdentificacion;
    protected double mdCantidad;
    protected String msClaveUnidad;
    protected String msUnidad;
    protected String msDescripcion;
    protected double mdValorUnitario;
    protected double mdImporte;
    
    protected int mnCfdiType;
    
    protected ArrayList<SCfdDataImpuesto> maImpuestosXml;
    
    public SCfdDataConcepto() {
        msClaveProdServ = "";
        msNoIdentificacion = "";
        mdCantidad = 0;
        msClaveUnidad = "";
        msUnidad = "";
        msDescripcion = "";
        mdValorUnitario = 0;
        mdImporte = 0;
        mnCfdiType = 0;
        maImpuestosXml = new ArrayList<SCfdDataImpuesto>();
    }
    
    public void setClaveProdServ(String s) { msClaveProdServ = s; }
    public void setNoIdentificacion(String s) { msNoIdentificacion = s; }
    public void setCantidad(double d) { mdCantidad = d; }
    public void setClaveUnidad(String s) { msClaveUnidad = s; }
    public void setUnidad(String s) { msUnidad = s; }
    public void setDescripcion(String s) { msDescripcion = s; }
    public void setValorUnitario(double d) { mdValorUnitario = d; }
    public void setImporte(double d) { mdImporte = d; }
    public void setCfdiType(int n) { mnCfdiType = n; }
    
    public String getClaveProdServ() { return msClaveProdServ; }
    public String getNoIdentificacion() { return msNoIdentificacion; }
    public double getCantidad() { return mdCantidad; }
    public String getClaveUnidad() { return msClaveUnidad; }
    public String getUnidad() { return msUnidad; }
    public String getDescripcion() { return msDescripcion; }
    public double getValorUnitario() { return mdValorUnitario; }
    public double getImporte() { return mdImporte; }
    public int getCfdiType() { return mnCfdiType; }
    
    public ArrayList<SCfdDataImpuesto> getCfdDataImpuestos() { return maImpuestosXml; }
    
    /**
     * Computed values for taxes.
     * @param entry 
     */
    public void computeCfdImpuestosConceptos(final erp.mtrn.data.SDataDpsEntry entry) {
        SCfdDataImpuesto impuestoXml = null;

        try {
            for (SDataDpsEntryTax tax : entry.getDbmsEntryTaxes()) {
                if (tax.getFkTaxCalculationTypeId() != SModSysConsts.FINS_TP_TAX_CAL_RATE) {
                    throw new Exception("Todos los impuestos deben ser en base a una tasa (" + tax.getFkTaxCalculationTypeId() + ").");
                }
                else {
                    impuestoXml = new SCfdDataImpuesto();
                    
                    switch (tax.getFkTaxTypeId()) {
                        case SModSysConsts.FINS_TP_TAX_RETAINED:
                            switch (tax.getPkTaxBasicId()) {
                                case 1: // IVA
                                    impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_IVA);
                                    impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_RETAINED);
                                    break;
                                case 2: // ISR
                                    impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_ISR);
                                    impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_RETAINED);
                                    break;
                                default:
                                    throw new Exception("Todos los impuestos retenidos deben ser conocidos (" + tax.getPkTaxBasicId() + ").");
                            }
                            break;

                        case SModSysConsts.FINS_TP_TAX_CHARGED:
                            switch (tax.getPkTaxBasicId()) {
                                case 1: // IVA
                                    impuestoXml.setImpuesto(DAttributeOptionImpuestoTraslado.CFD_IVA);
                                    impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_CHARGED);
                                    break;
                                case 3: // IEPS
                                    impuestoXml.setImpuesto(DAttributeOptionImpuestoTraslado.CFD_IEPS);
                                    impuestoXml.setImpuestoBasico(SModSysConsts.FINS_TP_TAX_CHARGED);
                                    break;
                                default:
                                    throw new Exception("Todos los impuestos trasladados deben ser conocidos (" + tax.getPkTaxBasicId() + ").");
                            }
                            break;

                        default:
                            throw new Exception("Todos los tipos de impuestos deben ser conocidos (" + tax.getFkTaxTypeId() + ").");
                    }
                    impuestoXml.setBase(entry.getSubtotalCy_r());
                    impuestoXml.setImpuestoClave(tax.getDbmsCfdTax());
                    impuestoXml.setTasa(tax.getPercentage());
                    impuestoXml.setImporte(tax.getTaxCy());
                    impuestoXml.setTipoFactor(tax.getDbmsTaxCalculationType());

                    maImpuestosXml.add(impuestoXml);
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    /**
     * Create node for concept version 3.3.
     * @return Node
     * @throws Exception 
     */
    public cfd.ver33.DElementConcepto createRootElementConcept33() throws Exception {
        cfd.ver33.DElementConcepto concepto = new cfd.ver33.DElementConcepto();
        
        concepto.getAttClaveProdServ().setString(msClaveProdServ);
        concepto.getAttNoIdentificacion().setString(msNoIdentificacion);
        concepto.getAttUnidad().setString(msUnidad);
        concepto.getAttClaveUnidad().setString(msClaveUnidad);
        
        if (mnCfdiType == SCfdConsts.CFD_TYPE_PAYROLL) {
            concepto.getAttCantidad().setDecimals(0);
        }
        concepto.getAttCantidad().setDouble(mdCantidad);
        concepto.getAttDescripcion().setString(msDescripcion);
        concepto.getAttValorUnitario().setDouble(mdValorUnitario);
        concepto.getAttImporte().setDouble(mdImporte);
        
        // Taxes:
            
        cfd.ver33.DElementConceptoImpuestosRetenidos impuestosRetenidos = new cfd.ver33.DElementConceptoImpuestosRetenidos();
        cfd.ver33.DElementConceptoImpuestosTraslados impuestosTrasladados = new cfd.ver33.DElementConceptoImpuestosTraslados();
        
        for (SCfdDataImpuesto impuesto : maImpuestosXml) {
            switch (impuesto.getImpuestoBasico()) {
                case SModSysConsts.FINS_TP_TAX_RETAINED:
                    impuestosRetenidos.getEltHijosImpuestoRetenido().add((cfd.ver33.DElementConceptoImpuestoRetencion) impuesto.createRootElementConceptoImpuesto());
                    break;
                case SModSysConsts.FINS_TP_TAX_CHARGED:
                    impuestosTrasladados.getEltHijosImpuestoTrasladado().add((cfd.ver33.DElementConceptoImpuestoTraslado) impuesto.createRootElementConceptoImpuesto());
                    break;
                default:
            }
        }
        
        if (!impuestosTrasladados.getEltHijosImpuestoTrasladado().isEmpty() || !impuestosRetenidos.getEltHijosImpuestoRetenido().isEmpty()) {
            concepto.setEltOpcConceptoImpuestos(new DElementConceptoImpuestos());
        }
        
        if (!impuestosTrasladados.getEltHijosImpuestoTrasladado().isEmpty()) {
            concepto.getEltImpuestos().setEltOpcImpuestosTrasladados(impuestosTrasladados);
        }
        if (!impuestosRetenidos.getEltHijosImpuestoRetenido().isEmpty()) {
            concepto.getEltImpuestos().setEltOpcImpuestosRetenidos(impuestosRetenidos);
        }
        
        return concepto;
    }
}
