/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public final class SCfdParams implements java.io.Serializable {

    private erp.mbps.data.SDataBizPartner moReceptor;
    private erp.mbps.data.SDataBizPartnerBranch moReceptorBranch;
    private erp.mbps.data.SDataBizPartner moEmisor;
    private erp.mbps.data.SDataBizPartnerBranchAddress moLugarExpedicion;
    private java.lang.String msUnidadPesoBruto;
    private java.lang.String msUnidadPesoNeto;
    private java.lang.String msContrato;
    private java.lang.String msPedido;
    private java.lang.String msFactura;
    private java.lang.String msRuta;
    private double mdInterestDelayRate;

    private java.lang.String msLorealFolioNotaRecepción;
    private java.lang.String msBachocoSociedad;
    private java.lang.String msBachocoOrganizaciónCompra;
    private java.lang.String msBachocoDivisión;
    private int mnSorianaTienda;
    private int mnSorianaEntregaMercancía;
    private Date mtSorianaRemisiónFecha;
    private java.lang.String msSorianaRemisiónFolio;
    private java.lang.String msSorianaPedidoFolio;
    private int mnSorianaBultoTipo;
    private double mdSorianaBultoCantidad;
    protected java.lang.String msSorianaNotaEntradaFolio;
    protected java.lang.String msSorianaCita;
    private java.lang.String msModeloDpsDescripción;
    protected int mnCfdAddendaSubtype;
    private java.lang.String msJsonData;
    private int mnFkCfdAddendaTypeId;
    
    private java.lang.String msAcuseCancelacion;
    private java.lang.String msUuid;
    private java.lang.String mtFechaTimbrado;
    private java.lang.String msSelloCfd;
    private java.lang.String msNoCertificadoSat;
    private java.lang.String msSelloSat;
    private java.lang.String msRfcEmisor;
    private java.lang.String msRfcReceptor;
    private double mdTotalCy;
    private java.lang.String[] masRegimenFiscal;
    private boolean mbComsumirTimbre;
    private java.lang.String msElektraOrder;
    private java.lang.String msElektraBarcode;
    private int mnElektraCages;
    private double mdElektraCagePriceUnitary;
    private int mnElektraParts;
    private double mdElektraPartPriceUnitary;

    protected boolean mbGenerarCodigoQr;

    public SCfdParams() {
        moReceptor = null;
        moReceptorBranch = null;
        moEmisor = null;
        moLugarExpedicion = null;
        msUnidadPesoBruto = "";
        msUnidadPesoNeto = "";
        msContrato = "";
        msPedido = "";
        msFactura = "";
        msRuta = "";
        mdInterestDelayRate = 0;

        msLorealFolioNotaRecepción = "";
        msBachocoSociedad = "";
        msBachocoOrganizaciónCompra = "";
        msBachocoDivisión = "";
        mnSorianaTienda = 0;
        mnSorianaEntregaMercancía = 0;
        mtSorianaRemisiónFecha = null;
        msSorianaRemisiónFolio = "";
        msSorianaPedidoFolio = "";
        mnSorianaBultoTipo = 0;
        mdSorianaBultoCantidad = 0;
        msSorianaNotaEntradaFolio = "";
        msSorianaCita = "";
        msModeloDpsDescripción = "";
        mnCfdAddendaSubtype = 0;
        msJsonData = "";
        mnFkCfdAddendaTypeId = 0;
        
        msAcuseCancelacion = "";
        msUuid = "";
        mtFechaTimbrado = null;
        msSelloCfd = "";
        msNoCertificadoSat = "";
        msSelloSat = "";
        msRfcEmisor = "";
        msRfcReceptor = "";
        mdTotalCy = 0;
        masRegimenFiscal = null;
        mbComsumirTimbre = false;
        msElektraOrder = "";
        msElektraBarcode = "";
        mnElektraCages = 0;
        mdElektraCagePriceUnitary = 0;
        mnElektraParts = 0;
        mdElektraPartPriceUnitary = 0;

        mbGenerarCodigoQr = false;
    }

    public void setReceptor(erp.mbps.data.SDataBizPartner o) { moReceptor = o; }
    public void setReceptorBranch(erp.mbps.data.SDataBizPartnerBranch o) { moReceptorBranch = o; }
    public void setEmisor(erp.mbps.data.SDataBizPartner o) { moEmisor = o; }
    public void setLugarExpedicion(erp.mbps.data.SDataBizPartnerBranchAddress o) { moLugarExpedicion = o; }
    public void setUnidadPesoBruto(java.lang.String s) { msUnidadPesoBruto = s; }
    public void setUnidadPesoNeto(java.lang.String s) { msUnidadPesoNeto = s; }
    public void setContrato(java.lang.String s) { msContrato = s; }
    public void setPedido(java.lang.String s) { msPedido = s; }
    public void setFactura(java.lang.String s) { msFactura = s; }
    public void setRuta(java.lang.String s) { msRuta = s; }
    public void setInterestDelayRate(double d) { mdInterestDelayRate = d; }
    
    public void setLorealFolioNotaRecepción(java.lang.String s) { msLorealFolioNotaRecepción = s; }
    public void setBachocoSociedad(java.lang.String s) { msBachocoSociedad = s; }
    public void setBachocoOrganizaciónCompra(java.lang.String s) { msBachocoOrganizaciónCompra = s; }
    public void setBachocoDivisión(java.lang.String s) { msBachocoDivisión = s; }
    public void setSorianaTienda(int n) { mnSorianaTienda = n; }
    public void setSorianaEntregaMercancía(int n) { mnSorianaEntregaMercancía = n; }
    public void setSorianaRemisiónFecha(Date t) { mtSorianaRemisiónFecha = t; }
    public void setSorianaRemisiónFolio(java.lang.String s) { msSorianaRemisiónFolio = s; }
    public void setSorianaPedidoFolio(java.lang.String s) { msSorianaPedidoFolio = s; }
    public void setSorianaBultoTipo(int n) { mnSorianaBultoTipo = n; }
    public void setSorianaBultoCantidad(double d) { mdSorianaBultoCantidad = d; }
    public void setSorianaNotaEntradaFolio(java.lang.String s) { msSorianaNotaEntradaFolio = s; }
    public void setSorianaCita(java.lang.String s) { msSorianaCita = s; }
    public void setModeloDpsDescripción(java.lang.String s) { msModeloDpsDescripción = s; }
    public void setCfdAddendaSubtype(int n) { mnCfdAddendaSubtype = n; }
    public void setJsonData(java.lang.String s) { msJsonData = s; }
    public void setFkCfdAddendaTypeId(int n) { mnFkCfdAddendaTypeId = n; }
    
    public void setAcuseCancelacion(java.lang.String s) { msAcuseCancelacion = s; }
    public void setUuid(java.lang.String s) { msUuid = s; }
    public void setFechaTimbrado(java.lang.String s) { mtFechaTimbrado = s; }
    public void setSelloCfd(java.lang.String s) { msSelloCfd = s; }
    public void setNoCertificadoSat(java.lang.String s) { msNoCertificadoSat = s; }
    public void setSelloSat(java.lang.String s) { msSelloSat = s; }
    public void setRfcEmisor(java.lang.String s) { msRfcEmisor = s; }
    public void setRfcReceptor(java.lang.String s) { msRfcReceptor = s; }
    public void setTotalCy(double d) { mdTotalCy = d; }
    public void setRegimenFiscal(java.lang.String[] as) { masRegimenFiscal = as; }
    public void setComsumirTimbre(java.lang.Boolean b) { mbComsumirTimbre = b; }
    public void setElektraOrder(java.lang.String s) { msElektraOrder = s; }
    public void setElektraBarcode(java.lang.String s) { msElektraBarcode = s; }
    public void setElektraCages(int n) { mnElektraCages = n; }
    public void setElectraCagePriceUnitary(double d) { mdElektraCagePriceUnitary = d; }
    public void setElektraParts(int n) { mnElektraParts = n; }
    public void setElektraPartPriceUnitary(double d) { mdElektraPartPriceUnitary = d; }
    public void setGenerarCodigoQr(boolean b) { mbGenerarCodigoQr = b; }

    public erp.mbps.data.SDataBizPartner getReceptor() { return moReceptor; }
    public erp.mbps.data.SDataBizPartnerBranch getReceptorBranch() { return moReceptorBranch; }
    public erp.mbps.data.SDataBizPartner getEmisor() { return moEmisor; }
    public erp.mbps.data.SDataBizPartnerBranchAddress getLugarExpedicion() { return moLugarExpedicion; }
    public java.lang.String getUnidadPesoBruto() { return msUnidadPesoBruto; }
    public java.lang.String getUnidadPesoNeto() { return msUnidadPesoNeto; }
    public java.lang.String getContrato() { return msContrato; }
    public java.lang.String getPedido() { return msPedido; }
    public java.lang.String getFactura() { return msFactura; }
    public java.lang.String getRuta() { return msRuta; }
    public double getInterestDelayRate() { return mdInterestDelayRate; }
    
    public java.lang.String getLorealFolioNotaRecepción() { return msLorealFolioNotaRecepción; }
    public java.lang.String getBachocoSociedad() { return msBachocoSociedad; }
    public java.lang.String getBachocoOrganizaciónCompra() { return msBachocoOrganizaciónCompra; }
    public java.lang.String getBachocoDivisión() { return msBachocoDivisión; }
    public int getSorianaTienda() { return mnSorianaTienda; }
    public int getSorianaEntregaMercancía() { return mnSorianaEntregaMercancía; }
    public Date getSorianaRemisiónFecha() { return mtSorianaRemisiónFecha; }
    public java.lang.String getSorianaRemisiónFolio() { return msSorianaRemisiónFolio; }
    public java.lang.String getSorianaPedidoFolio() { return msSorianaPedidoFolio; }
    public int getSorianaBultoTipo() { return mnSorianaBultoTipo; }
    public double getSorianaBultoCantidad() { return mdSorianaBultoCantidad; }
    public java.lang.String getSorianaNotaEntradaFolio() { return msSorianaNotaEntradaFolio; }
    public java.lang.String getSorianaCita() { return msSorianaCita; }
    public java.lang.String getModeloDpsDescripción() { return msModeloDpsDescripción; }
    public int getCfdAddendaSubtype() { return mnCfdAddendaSubtype; }
    public java.lang.String getJsonData() { return msJsonData; }
    public int getFkCfdAddendaTypeId() { return mnFkCfdAddendaTypeId; }
    
    public java.lang.String getAcuseCancelacion() { return msAcuseCancelacion; }
    public java.lang.String getUuid() { return msUuid; }
    public java.lang.String getFechaTimbrado() { return mtFechaTimbrado; }
    public java.lang.String getSelloCfd() { return msSelloCfd; }
    public java.lang.String getNoCertificadoSat() { return msNoCertificadoSat; }
    public java.lang.String getSelloSat() { return msSelloSat; }
    public java.lang.String getRfcEmisor() { return msRfcEmisor; }
    public java.lang.String getRfcReceptor() { return msRfcReceptor; }
    public double getTotalCy() { return mdTotalCy; }
    public java.lang.String[] getRegimenFiscal() { return masRegimenFiscal; }
    public java.lang.Boolean getComsumirTimbre() { return mbComsumirTimbre; }
    public java.lang.String getElektraOrder() { return msElektraOrder; }
    public java.lang.String getElektraBarcode() { return msElektraBarcode; }
    public int getElektraCages() { return mnElektraCages; }
    public double getElectraCagePriceUnitary() { return mdElektraCagePriceUnitary; }
    public int getElektraParts() { return mnElektraParts; }
    public double getElektraPartPriceUnitary() { return mdElektraPartPriceUnitary; }
    public boolean getGenerarCodigoQr() { return mbGenerarCodigoQr; }
}
