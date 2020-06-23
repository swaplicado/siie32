/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import cfd.DAttributeOptionImpuestoRetencion;
import cfd.DAttributeOptionImpuestoTraslado;
import cfd.ver33.DElementConceptoImpuestos;
import erp.data.SDataConstantsSys;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDpsEntryTax;
import java.util.ArrayList;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SCfdDataConcepto {
    
    public static final int LEN_DESCRIPCION = 1000;
    private static final int INT_COMMERCE_DECS = 2;
    
    protected int mnCfdType;
    protected boolean mbHasIntCommerceComplement;
    protected String msClaveProdServ;
    protected String msNoIdentificacion;
    protected double mdCantidad;
    protected String msClaveUnidad;
    protected String msUnidad;
    protected String msDescripcion;
    protected double mdValorUnitario;
    protected double mdImporte;
    protected double mdDescuento;
    protected ArrayList<SCfdDataImpuesto> maImpuestosXml;
    
    /**
     * Creates a CFD Concepto XML node.
     * @param cfdType CFD type. Supported options any constant in SDataConstantsSys.TRNS_TP_CFD_...
     * Helps formatting some of the attributes of the Concepto XML node, according of the type of CFD provided.
     */
    public SCfdDataConcepto(final int cfdType) {
        mnCfdType = cfdType;
        mbHasIntCommerceComplement = false;
        msClaveProdServ = "";
        msNoIdentificacion = "";
        mdCantidad = 0;
        msClaveUnidad = "";
        msUnidad = "";
        msDescripcion = "";
        mdValorUnitario = 0;
        mdImporte = 0;
        mdDescuento = 0;
        maImpuestosXml = new ArrayList<>();
    }
    
    /*
     * Private methods
     */
    
    /**
     * Remove trailing zeros from decimal part.
     * @param decValue Decimal value as text.
     * @param requiredDecs Minimum required decimals.
     * @return 
     */
    private String removeTrailingZeros(final String decValue, final int requiredDecs) {
        int pointIndex = decValue.lastIndexOf('.');
        String newDecValue = decValue;
        
        /*
        Decimal value as text: 15042.150000
        Minimum required decimals: 2
        
        value: 15042.1500
        index: 0123456789
        */
        
        if (pointIndex != -1) {
            int trailingZeros = 0;
            
            for (int index = decValue.length() - 1; index > (pointIndex + requiredDecs); index--) {
                if (decValue.charAt(index) == '0') {
                    trailingZeros++;
                }
                else {
                    break;
                }
            }
            
            if (trailingZeros > 0) {
                newDecValue = newDecValue.substring(0, decValue.length() - trailingZeros);
            }
        }
        
        return newDecValue;
    }
    
    /*
     * Public methods
     */
    
    public void setHasIntCommerceComplement(boolean b) { mbHasIntCommerceComplement = b; }
    public void setClaveProdServ(String s) { msClaveProdServ = s; }
    public void setNoIdentificacion(String s) { msNoIdentificacion = s; }
    public void setCantidad(double d) { mdCantidad = d; }
    public void setClaveUnidad(String s) { msClaveUnidad = s; }
    public void setUnidad(String s) { msUnidad = s; }
    public void setDescripcion(String s) { String descripcion = s.replaceAll("\n", " "); if (descripcion.length() > LEN_DESCRIPCION) { descripcion = descripcion.substring(0, LEN_DESCRIPCION); } msDescripcion = descripcion; }
    public void setValorUnitario(double d) { mdValorUnitario = d; }
    public void setImporte(double d) { mdImporte = d; }
    public void setDescuento(double d) { mdDescuento = d; }
    
    public boolean hasIntCommerceComplement() { return mbHasIntCommerceComplement; }
    public int getCfdType() { return mnCfdType; }
    public String getClaveProdServ() { return msClaveProdServ; }
    public String getNoIdentificacion() { return msNoIdentificacion; }
    public double getCantidad() { return mdCantidad; }
    public String getClaveUnidad() { return msClaveUnidad; }
    public String getUnidad() { return msUnidad; }
    public String getDescripcion() { return msDescripcion; }
    public double getValorUnitario() { return mdValorUnitario; }
    public double getImporte() { return mdImporte; }
    public double getDescuento() { return mdDescuento; }
    
    public ArrayList<SCfdDataImpuesto> getCfdDataImpuestos() { return maImpuestosXml; }
    
    /**
     * Computed values for taxes.
     * @param entry 
     */
    public void computeCfdImpuestosConceptos(final erp.mtrn.data.SDataDpsEntry entry) {
        SCfdDataImpuesto impuestoXml = null;

        try {
            for (SDataDpsEntryTax tax : entry.getDbmsEntryTaxes()) {
                if (!SLibUtils.belongsTo(tax.getFkTaxCalculationTypeId(), new int[] { SModSysConsts.FINS_TP_TAX_CAL_RATE, SModSysConsts.FINS_TP_TAX_CAL_EXEMPT })) {
                    throw new Exception("El tipo de cálculo '" + tax.getDbmsTaxCalculationType() + "' del impuesto '" + tax.getDbmsTax() + "' no está soportado.");
                }
                else if (entry.getSubtotalCy_r() > 0) {
                    impuestoXml = new SCfdDataImpuesto();
                    
                    switch (tax.getFkTaxTypeId()) {
                        case SModSysConsts.FINS_TP_TAX_RETAINED:
                            switch (tax.getDbmsCfdTaxId()) {
                                case SModSysConsts.FINS_CFD_TAX_IVA:
                                    impuestoXml.setImpuestoTipo(SModSysConsts.FINS_TP_TAX_RETAINED);
                                    impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_IVA);
                                    break;
                                case SModSysConsts.FINS_CFD_TAX_ISR:
                                    impuestoXml.setImpuestoTipo(SModSysConsts.FINS_TP_TAX_RETAINED);
                                    impuestoXml.setImpuesto(DAttributeOptionImpuestoRetencion.CFD_ISR);
                                    break;
                                default:
                                    throw new Exception("Todos los impuestos retenidos deben ser conocidos (" + tax.getDbmsCfdTaxId() + ").");
                            }
                            break;

                        case SModSysConsts.FINS_TP_TAX_CHARGED:
                            switch (tax.getDbmsCfdTaxId()) {
                                case SModSysConsts.FINS_CFD_TAX_IVA:
                                    impuestoXml.setImpuestoTipo(SModSysConsts.FINS_TP_TAX_CHARGED);
                                    impuestoXml.setImpuesto(DAttributeOptionImpuestoTraslado.CFD_IVA);
                                    break;
                                case SModSysConsts.FINS_CFD_TAX_IEPS:
                                    impuestoXml.setImpuestoTipo(SModSysConsts.FINS_TP_TAX_CHARGED);
                                    impuestoXml.setImpuesto(DAttributeOptionImpuestoTraslado.CFD_IEPS);
                                    break;
                                default:
                                    throw new Exception("Todos los impuestos trasladados deben ser conocidos (" + tax.getDbmsCfdTaxId() + ").");
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
     * @return XML node of class cfd.ver33.DElementConcepto.
     * @throws Exception 
     */
    public cfd.ver33.DElementConcepto createRootElementConcept33() throws Exception {
        cfd.ver33.DElementConcepto concepto = new cfd.ver33.DElementConcepto();
        
        concepto.getAttClaveProdServ().setString(msClaveProdServ);
        concepto.getAttNoIdentificacion().setString(msNoIdentificacion);
        concepto.getAttCantidad().setDouble(mdCantidad);
        concepto.getAttClaveUnidad().setString(msClaveUnidad);
        concepto.getAttUnidad().setString(msUnidad);
        concepto.getAttDescripcion().setString(msDescripcion);
        concepto.getAttValorUnitario().setDouble(mdValorUnitario);
        concepto.getAttImporte().setDouble(mdImporte);
        concepto.getAttDescuento().setDouble(mdDescuento);
        
        if (mbHasIntCommerceComplement) {
            String valorOriginal;
            String valorIntCommerce;
            
            valorOriginal = concepto.getAttValorUnitario().getAttributeForOriginalString();
            valorOriginal = valorOriginal.substring(0, valorOriginal.length() - 1);
            valorIntCommerce = removeTrailingZeros(valorOriginal, INT_COMMERCE_DECS);
            
            if (valorIntCommerce.length() < valorOriginal.length()) {
                concepto.getAttValorUnitario().setDecimals(concepto.getAttValorUnitario().getDecimals() - (valorOriginal.length() - valorIntCommerce.length()));
            }
        }
        else {
            switch (mnCfdType) {
                case SDataConstantsSys.TRNS_TP_CFD_INV:
                    break;
                case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                    concepto.getAttCantidad().setDecimals(0);
                    concepto.getAttValorUnitario().setDecimals(0);
                    concepto.getAttImporte().setDecimals(0);
                    break;
                case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                    concepto.getAttCantidad().setDecimals(0);
                    break;
                default:
            }
        }
        
        // Taxes:
            
        cfd.ver33.DElementConceptoImpuestosRetenciones impuestosRetenciones = new cfd.ver33.DElementConceptoImpuestosRetenciones();
        cfd.ver33.DElementConceptoImpuestosTraslados impuestosTrasladados = new cfd.ver33.DElementConceptoImpuestosTraslados();
        
        for (SCfdDataImpuesto impuesto : maImpuestosXml) {
            switch (impuesto.getImpuestoTipo()) {
                case SModSysConsts.FINS_TP_TAX_RETAINED:
                    impuestosRetenciones.getEltImpuestoRetenciones().add((cfd.ver33.DElementConceptoImpuestoRetencion) impuesto.createRootElementConceptoImpuesto33());
                    break;
                case SModSysConsts.FINS_TP_TAX_CHARGED:
                    impuestosTrasladados.getEltImpuestoTrasladados().add((cfd.ver33.DElementConceptoImpuestoTraslado) impuesto.createRootElementConceptoImpuesto33());
                    break;
                default:
            }
        }
        
        if (!impuestosTrasladados.getEltImpuestoTrasladados().isEmpty() || !impuestosRetenciones.getEltImpuestoRetenciones().isEmpty()) {
            concepto.setEltOpcConceptoImpuestos(new DElementConceptoImpuestos());
        }
        
        if (!impuestosTrasladados.getEltImpuestoTrasladados().isEmpty()) {
            concepto.getEltOpcConceptoImpuestos().setEltOpcImpuestosTrasladados(impuestosTrasladados);
        }
        
        if (!impuestosRetenciones.getEltImpuestoRetenciones().isEmpty()) {
            concepto.getEltOpcConceptoImpuestos().setEltOpcImpuestosRetenciones(impuestosRetenciones);
        }
        
        return concepto;
    }
}
