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

    private int mnTipoAddenda;
    private java.lang.String msLorealFolioNotaRecepcion;
    private java.lang.String msBachocoSociedad;
    private java.lang.String msBachocoOrganizacionCompra;
    private java.lang.String msBachocoDivision;
    private java.lang.String msModeloDpsDescripcion;
    private java.lang.String msCentro;
    private int mnSorianaTienda;
    private int mnSorianaEntregaMercancia;
    private Date mtSorianaFechaRemision;
    private java.lang.String msSorianaFolioRemision;
    private java.lang.String msSorianaFolioPedido;
    private int mnSorianaTipoBulto;
    private double mdSorianaCantidadBulto;
    protected java.lang.String msSorianaNotaEntradaFolio;
    protected java.lang.String msSorianaCita;
    protected int mnCfdAddendaSubtype;
    private java.lang.Boolean mbAgregarAddenda;
    private java.lang.String msAcuseCancelacion;
    private java.lang.String msUuid;
    private java.lang.String mtFechaTimbrado;
    private java.lang.String msSelloCFD;
    private java.lang.String msNoCertificadoSAT;
    private java.lang.String msSelloSAT;
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

        mnTipoAddenda = 0;
        msLorealFolioNotaRecepcion = "";
        msBachocoSociedad = "";
        msBachocoOrganizacionCompra = "";
        msBachocoDivision = "";
        msModeloDpsDescripcion = "";
        msCentro = "";
        mnSorianaTienda = 0;
        mnSorianaEntregaMercancia = 0;
        mtSorianaFechaRemision = null;
        msSorianaFolioRemision = "";
        msSorianaFolioPedido = "";
        mnSorianaTipoBulto = 0;
        mdSorianaCantidadBulto = 0;
        msSorianaNotaEntradaFolio = "";
        msSorianaCita = "";
        mnCfdAddendaSubtype = 0;
        mbAgregarAddenda = false;
        msAcuseCancelacion = "";
        msUuid = "";
        mtFechaTimbrado = null;
        msSelloCFD = "";
        msNoCertificadoSAT = "";
        msSelloSAT = "";
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
    public void setTipoAddenda(int n) { mnTipoAddenda = n; }
    public void setLorealFolioNotaRecepcion(java.lang.String s) { msLorealFolioNotaRecepcion = s; }
    public void setBachocoSociedad(java.lang.String s) { msBachocoSociedad = s; }
    public void setBachocoOrganizacionCompra(java.lang.String s) { msBachocoOrganizacionCompra = s; }
    public void setBachocoDivision(java.lang.String s) { msBachocoDivision = s; }
    public void setModeloDpsDescripcion(java.lang.String s) { msModeloDpsDescripcion = s; }
    public void setCentro(java.lang.String s) { msCentro = s; }
    public void setSorianaTienda(int n) { mnSorianaTienda = n; }
    public void setSorianaEntregaMercancia(int n) { mnSorianaEntregaMercancia = n; }
    public void setSorianaFechaRemision(Date t) { mtSorianaFechaRemision = t; }
    public void setSorianaFolioRemision(java.lang.String s) { msSorianaFolioRemision = s; }
    public void setSorianaFolioPedido(java.lang.String s) { msSorianaFolioPedido = s; }
    public void setSorianaTipoBulto(int n) { mnSorianaTipoBulto = n; }
    public void setSorianaCantidadBulto(double d) { mdSorianaCantidadBulto = d; }
    public void setSorianaNotaEntradaFolio(java.lang.String s) { msSorianaNotaEntradaFolio = s; }
    public void setSorianaCita(java.lang.String s) { msSorianaCita = s; }
    public void setCfdAddendaSubtype(int n) { mnCfdAddendaSubtype = n; }
    public void setAgregarAddenda(java.lang.Boolean b) { mbAgregarAddenda = b; }
    public void setAcuseCancelacion(java.lang.String s) { msAcuseCancelacion = s; }
    public void setUuid(java.lang.String s) { msUuid = s; }
    public void setFechaTimbrado(java.lang.String s) { mtFechaTimbrado = s; }
    public void setSelloCFD(java.lang.String s) { msSelloCFD = s; }
    public void setNoCertificadoSAT(java.lang.String s) { msNoCertificadoSAT = s; }
    public void setSelloSAT(java.lang.String s) { msSelloSAT = s; }
    public void setRfcEmisor(java.lang.String s) { msRfcEmisor = s; }
    public void setRfcReceptor(java.lang.String s) { msRfcReceptor = s; }
    public void setTotalCy(double d) { mdTotalCy = d; }
    public void setRegimenFiscal(java.lang.String[] s) { masRegimenFiscal = s; }
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
    public int getTipoAddenda() { return mnTipoAddenda; }
    public java.lang.String getLorealFolioNotaRecepcion() { return msLorealFolioNotaRecepcion; }
    public java.lang.String getBachocoSociedad() { return msBachocoSociedad; }
    public java.lang.String getBachocoOrganizacionCompra() { return msBachocoOrganizacionCompra; }
    public java.lang.String getBachocoDivision() { return msBachocoDivision; }
    public java.lang.String getModeloDpsDescripcion() { return msModeloDpsDescripcion; }
    public java.lang.String getCentro() { return msCentro; }
    public int getSorianaTienda() { return mnSorianaTienda; }
    public int getSorianaEntregaMercancia() { return mnSorianaEntregaMercancia; }
    public Date getSorianaFechaRemision() { return mtSorianaFechaRemision; }
    public java.lang.String getSorianaFolioRemision() { return msSorianaFolioRemision; }
    public java.lang.String getSorianaFolioPedido() { return msSorianaFolioPedido; }
    public int getSorianaTipoBulto() { return mnSorianaTipoBulto; }
    public double getSorianaCantidadBulto() { return mdSorianaCantidadBulto; }
    public java.lang.String getSorianaNotaEntradaFolio() { return msSorianaNotaEntradaFolio; }
    public java.lang.String getSorianaCita() { return msSorianaCita; }
    public int getCfdAddendaSubtype() { return mnCfdAddendaSubtype; }
    public java.lang.Boolean getAgregarAddenda() { return mbAgregarAddenda; }
    public java.lang.String getAcuseCancelacion() { return msAcuseCancelacion; }
    public java.lang.String getUuid() { return msUuid; }
    public java.lang.String getFechaTimbrado() { return mtFechaTimbrado; }
    public java.lang.String getSelloCFD() { return msSelloCFD; }
    public java.lang.String getNoCertificadoSAT() { return msNoCertificadoSAT; }
    public java.lang.String getSelloSAT() { return msSelloSAT; }
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
