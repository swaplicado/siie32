/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import cfd.DElement;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdDataBizPartner;
import erp.client.SClientInterface;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataCfd;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Sergio Flores
 * 
 * Maintenance Log:
 * 2018-01-02, Sergio Flores:
 *  Implementation of payroll into CFDI 3.3.
 */
public class SHrsFormerPayroll {

    protected SClientInterface miClient;

    protected int mnPkNominaId;
    protected int mnFkNominaTipoId;
    protected Date mtFecha;
    protected Date mtFechaIncial;
    protected Date mtFechaFinal;
    protected double mdTotalPercepciones;
    protected double mdTotalDeducciones;
    protected double mdTotalRetenciones;
    protected int mnEmpresaId;
    protected int mnSucursalEmpresaId;
    private String[] masRegimenFiscal;

    protected ArrayList<SHrsFormerReceipt> moChildReceipts;

    private void reset() {
        mnPkNominaId = 0;
        mnFkNominaTipoId = 0;
        mtFecha = null;
        mtFechaIncial = null;
        mtFechaFinal = null;
        mdTotalPercepciones = 0;
        mdTotalDeducciones = 0;
        mdTotalRetenciones = 0;
        mnEmpresaId = 0;
        mnSucursalEmpresaId = 0;
        masRegimenFiscal = null;

        moChildReceipts = new ArrayList<>();
    }

    public SHrsFormerPayroll(SClientInterface client) {
        miClient = client;

        reset();
    }

    public void setPkNominaId(int n) { mnPkNominaId = n; }
    public void setFkNominaTipoId(int n) { mnFkNominaTipoId = n; }
    public void setFecha(Date t) { mtFecha = t; }
    public void setFechaInicial(Date t) { mtFechaIncial = t; }
    public void setFechaFinal(Date t) { mtFechaFinal = t; }
    public void setTotalPercepciones(double d) { mdTotalPercepciones = d; }
    public void setTotalDeducciones(double d) { mdTotalDeducciones = d; }
    public void setTotalRetenciones(double d) { mdTotalRetenciones = d; }
    public void setEmpresaId(int n) { mnEmpresaId = n; }
    public void setSucursalEmpresaId(int n) { mnSucursalEmpresaId = n; }
    public void setRegimenFiscal(String[] s) { masRegimenFiscal = s; }

    public int getPkNominaId() { return mnPkNominaId; }
    public int getFkNominaTipoId() { return mnFkNominaTipoId; }
    public Date getFecha() { return mtFecha; }
    public Date getFechaInicial() { return mtFechaIncial; }
    public Date getFechaFinal() { return mtFechaFinal; }
    public double getTotalPercepciones() { return mdTotalPercepciones; }
    public double getTotalDeducciones() { return mdTotalDeducciones; }
    public double getTotalRetenciones() { return mdTotalRetenciones; }
    public int getEmpresaId() { return mnEmpresaId; }
    public int getSucursalEmpresaId() { return mnSucursalEmpresaId; }
    public String[] getRegimenFiscal() { return masRegimenFiscal; }

    public ArrayList<SHrsFormerReceipt> getChildReceipts() { return moChildReceipts; }

    public void renderPayroll(ArrayList<SDataCfd> pCfd) throws java.lang.Exception {
        double dTotalIncome = 0;
        double dTotalDeductions = 0;
        double dTotalRentRetained = 0;
        SHrsFormerReceipt hrsFormerReceipt = null;
        SHrsFormerReceiptConcept hrsFormerReceiptConcept = null;
        SHrsFormerConceptExtraTime hrsFormerConceptExtraTime = null;
        SCfdDataBizPartner xmlEmisor = null;
        SCfdDataBizPartner xmlExpeditionSpot = null;
        SCfdDataBizPartner xmlReceptor = null;
        cfd.ver32.DElementComprobante comprobante = null;

        // Comprobante:

        comprobante = cfd.DCfdUtils.getCfdi32(pCfd.get(0).getDocXml());

        mtFecha = comprobante.getAttFecha().getDatetime();
        mnPkNominaId = pCfd.get(0).getFkPayrollPayrollId_n();   // only old payroll version supported!

        // Emisor:

        xmlEmisor = new SCfdDataBizPartner();

        xmlEmisor.setBizPartnerRfc(comprobante.getEltEmisor().getAttRfc().getString());
        xmlEmisor.setBizPartnerName(comprobante.getEltEmisor().getAttNombre().getString());

        xmlEmisor.setBizPartnerStreet(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttCalle().getString());
        xmlEmisor.setBizPartnerStreetNumberExt(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttNoExterior().getString());
        xmlEmisor.setBizPartnerStreetNumberInt(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttNoInterior().getString());
        xmlEmisor.setBizPartnerNeighborhood(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttColonia().getString());
        xmlEmisor.setBizPartnerLocality(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttLocalidad().getString());
        xmlEmisor.setBizPartnerReference(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttReferencia().getString());
        xmlEmisor.setBizPartnerCounty(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttMunicipio().getString());
        xmlEmisor.setBizPartnerStateName(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttEstado().getString());
        xmlEmisor.setBizPartnerCountryName(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttPais().getString());
        xmlEmisor.setBizPartnerZipCode(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttCodigoPostal().getString());

        if (!comprobante.getEltEmisor().getEltHijosRegimenFiscal().isEmpty()) {
            masRegimenFiscal = new String[1];

            masRegimenFiscal[0] = comprobante.getEltEmisor().getEltHijosRegimenFiscal().get(0).getAttRegimen().getString();

            xmlEmisor.setBizPartnerFiscalRegime(masRegimenFiscal[0]);
        }

        if (comprobante.getEltEmisor().getEltOpcExpedidoEn() != null) {
            xmlExpeditionSpot = new SCfdDataBizPartner();

            xmlExpeditionSpot.setBizPartnerStreet(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttCalle().getString());
            xmlExpeditionSpot.setBizPartnerStreetNumberExt(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttNoExterior().getString());
            xmlExpeditionSpot.setBizPartnerStreetNumberInt(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttNoInterior().getString());
            xmlExpeditionSpot.setBizPartnerNeighborhood(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttColonia().getString());
            xmlExpeditionSpot.setBizPartnerLocality(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttLocalidad().getString());
            xmlExpeditionSpot.setBizPartnerReference(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttReferencia().getString());
            xmlExpeditionSpot.setBizPartnerCounty(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttMunicipio().getString());
            xmlExpeditionSpot.setBizPartnerStateName(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttEstado().getString());
            xmlExpeditionSpot.setBizPartnerCountryName(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttPais().getString());
            xmlExpeditionSpot.setBizPartnerZipCode(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttCodigoPostal().getString());
        }

        // load payroll receipts:

        for (SDataCfd cfdReceipt : pCfd) {
            comprobante = cfd.DCfdUtils.getCfdi32(cfdReceipt.getDocXml());
            dTotalIncome = 0;
            dTotalDeductions = 0;
            dTotalRentRetained = 0;

            hrsFormerReceipt = new SHrsFormerReceipt(miClient, this);

            xmlReceptor = new SCfdDataBizPartner();

            // Receptor:

            xmlReceptor.setBizPartnerRfc(comprobante.getEltReceptor().getAttRfc().getString());
            xmlReceptor.setBizPartnerName(comprobante.getEltReceptor().getAttNombre().getString());

            xmlReceptor.setBizPartnerStreet(comprobante.getEltReceptor().getEltDomicilio().getAttCalle().getString());
            xmlReceptor.setBizPartnerStreetNumberExt(comprobante.getEltReceptor().getEltDomicilio().getAttNoExterior().getString());
            xmlReceptor.setBizPartnerStreetNumberInt(comprobante.getEltReceptor().getEltDomicilio().getAttNoInterior().getString());
            xmlReceptor.setBizPartnerNeighborhood(comprobante.getEltReceptor().getEltDomicilio().getAttColonia().getString());
            xmlReceptor.setBizPartnerLocality(comprobante.getEltReceptor().getEltDomicilio().getAttLocalidad().getString());
            xmlReceptor.setBizPartnerReference(comprobante.getEltReceptor().getEltDomicilio().getAttReferencia().getString());
            xmlReceptor.setBizPartnerCounty(comprobante.getEltReceptor().getEltDomicilio().getAttMunicipio().getString());
            xmlReceptor.setBizPartnerStateName(comprobante.getEltReceptor().getEltDomicilio().getAttEstado().getString());
            xmlReceptor.setBizPartnerCountryName(comprobante.getEltReceptor().getEltDomicilio().getAttPais().getString());
            xmlReceptor.setBizPartnerZipCode(comprobante.getEltReceptor().getEltDomicilio().getAttCodigoPostal().getString());

            // Payroll:

            dTotalRentRetained = comprobante.getEltImpuestos().getAttTotalImpuestosRetenidos().getDouble();

            hrsFormerReceipt.setPkEmpleadoId(cfdReceipt.getFkPayrollBizPartnerId_n());
            hrsFormerReceipt.setAuxEmpleadoId(cfdReceipt.getFkPayrollEmployeeId_n());

            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element.getName().compareTo("nomina:Nomina") == 0) {
                    hrsFormerReceipt.setRegistroPatronal(((cfd.ver3.nom11.DElementNomina) element).getAttRegistroPatronal().getString());
                    hrsFormerReceipt.setNumEmpleado(((cfd.ver3.nom11.DElementNomina) element).getAttNumEmpleado().getString());
                    hrsFormerReceipt.setCurp(((cfd.ver3.nom11.DElementNomina) element).getAttCurp().getString());
                    hrsFormerReceipt.setTipoRegimen(((cfd.ver3.nom11.DElementNomina) element).getAttTipoRegimen().getInteger());
                    hrsFormerReceipt.setNumSeguridadSocial(((cfd.ver3.nom11.DElementNomina) element).getAttNumSeguridadSocial().getString());
                    hrsFormerReceipt.setFechaPago(((cfd.ver3.nom11.DElementNomina) element).getAttFechaPago().getDate());
                    hrsFormerReceipt.setFechaInicialPago(((cfd.ver3.nom11.DElementNomina) element).getAttFechaInicialPago().getDate());
                    hrsFormerReceipt.setFechaFinalPago(((cfd.ver3.nom11.DElementNomina) element).getAttFechaFinalPago().getDate());
                    hrsFormerReceipt.setNumDiasPagados(((cfd.ver3.nom11.DElementNomina) element).getAttNumDiasPagados().getDouble());
                    hrsFormerReceipt.setDepartamento(((cfd.ver3.nom11.DElementNomina) element).getAttDepartamento().getString());
                    hrsFormerReceipt.setCuentaBancaria(((cfd.ver3.nom11.DElementNomina) element).getAttClabe().getString());
                    hrsFormerReceipt.setBanco(((cfd.ver3.nom11.DElementNomina) element).getAttBanco().getInteger());
                    hrsFormerReceipt.setFechaInicioRelLaboral(((cfd.ver3.nom11.DElementNomina) element).getAttFechaInicioRelLaboral().getDate());
                    hrsFormerReceipt.setAntiguedad(((cfd.ver3.nom11.DElementNomina) element).getAttAntiguedad().getInteger());
                    hrsFormerReceipt.setPuesto(((cfd.ver3.nom11.DElementNomina) element).getAttPuesto().getString());
                    hrsFormerReceipt.setTipoContrato(((cfd.ver3.nom11.DElementNomina) element).getAttTipoContrato().getString());
                    hrsFormerReceipt.setTipoJornada(((cfd.ver3.nom11.DElementNomina) element).getAttTipoJornada().getString());
                    hrsFormerReceipt.setPeriodicidadPago(((cfd.ver3.nom11.DElementNomina) element).getAttPeriodicidadPago().getString());
                    hrsFormerReceipt.setSalarioBaseCotApor(((cfd.ver3.nom11.DElementNomina) element).getAttSalarioBaseCotApor().getDouble());
                    hrsFormerReceipt.setRiesgoPuesto(((cfd.ver3.nom11.DElementNomina) element).getAttRiesgoPuesto().getInteger());
                    hrsFormerReceipt.setSalarioDiarioIntegrado(((cfd.ver3.nom11.DElementNomina) element).getAttSalarioDiarioIntegrado().getDouble());
                    hrsFormerReceipt.setMetodoPagoAux(comprobante.getAttMetodoDePago().getString());
                    hrsFormerReceipt.setSerie(comprobante.getAttSerie().getString());
                    hrsFormerReceipt.setMoneda(comprobante.getAttMoneda().getString());

                    // Perceptions:

                    if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones() != null) {
                        for (int i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().size(); i++) {
                            hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();

                            hrsFormerReceiptConcept.setPkTipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[0]);
                            hrsFormerReceiptConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[1]);
                            hrsFormerReceiptConcept.setClaveOficial(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getInteger());
                            hrsFormerReceiptConcept.setClaveEmpresa("" + SLibUtils.parseInt(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString()));
                            hrsFormerReceiptConcept.setConcepto(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttConcepto().getString());
                            hrsFormerReceiptConcept.setTotalGravado(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble());
                            hrsFormerReceiptConcept.setTotalExento(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble());

                            dTotalIncome += ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                            ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble();

                            hrsFormerReceipt.getChildConcepts().add(hrsFormerReceiptConcept);

                            if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getInteger() == SModSysConsts.HRSS_TP_EAR_OVR_TME) {
                                hrsFormerConceptExtraTime = new SHrsFormerConceptExtraTime();

                                for (int j = 0; j < ((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().size(); j++) {
                                    if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME) == 0) {
                                        hrsFormerReceiptConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1]);
                                        hrsFormerReceiptConcept.setCantidad(0);
                                        hrsFormerReceiptConcept.setHoras_r(0);
                                        hrsFormerConceptExtraTime.setImportePagado(0);
                                    }
                                    else if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME_TYPE_DOUBLE) == 0) {
                                            if (((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE) == 0) {
                                                hrsFormerReceiptConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1]);
                                                hrsFormerReceiptConcept.setCantidad(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttDias().getInteger());
                                                hrsFormerReceiptConcept.setHoras_r(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttHorasExtra().getInteger());
                                                hrsFormerConceptExtraTime.setImportePagado(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttImportePagado().getDouble());
                                            }
                                    }
                                    else if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME_TYPE_TRIPLE) == 0) {
                                            if (((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE) == 0) {
                                                hrsFormerReceiptConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1]);
                                                hrsFormerReceiptConcept.setCantidad(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttDias().getInteger());
                                                hrsFormerReceiptConcept.setHoras_r(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttHorasExtra().getInteger());
                                                hrsFormerConceptExtraTime.setImportePagado(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttImportePagado().getDouble());
                                            }
                                    }
                                }

                                // Node extraTime:

                                hrsFormerConceptExtraTime.setDias(hrsFormerReceiptConcept.getCantidad());
                                hrsFormerConceptExtraTime.setTipoHoras(hrsFormerReceiptConcept.getPkSubtipoConcepto() == SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] ? SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                                hrsFormerConceptExtraTime.setHorasExtra(hrsFormerReceiptConcept.getHoras_r());

                                hrsFormerReceiptConcept.setChildExtraTime(hrsFormerConceptExtraTime);
                            }
                        }
                    }

                    // Deductions:

                    if (((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones() != null) {
                        for (int i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().size(); i++) {
                            hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();

                            hrsFormerReceiptConcept.setPkTipoConcepto(SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[0]);
                            hrsFormerReceiptConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[1]);
                            hrsFormerReceiptConcept.setClaveOficial(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger());
                            hrsFormerReceiptConcept.setClaveEmpresa("" + SLibUtils.parseInt(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString()));
                            hrsFormerReceiptConcept.setConcepto(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttConcepto().getString());
                            hrsFormerReceiptConcept.setTotalGravado(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble());
                            hrsFormerReceiptConcept.setTotalExento(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble());

                            dTotalDeductions += ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble() +
                                            ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble();

                            hrsFormerReceipt.getChildConcepts().add(hrsFormerReceiptConcept);

                            if (SLibUtils.belongsTo(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger(), new int[] { SModSysConsts.HRSS_TP_DED_DIS, SModSysConsts.HRSS_TP_DED_ABS })) {
                                for (int j = 0; j < ((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().size(); j++) {
                                }
                            }
                        }
                    }

                    hrsFormerReceipt.setTotalPercepciones(SLibUtils.round(dTotalIncome, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    hrsFormerReceipt.setTotalDeducciones(SLibUtils.round(dTotalDeductions, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    hrsFormerReceipt.setTotalRetenciones(SLibUtils.round(dTotalRentRetained, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    hrsFormerReceipt.setTotalNeto(SLibUtils.round((dTotalIncome - dTotalDeductions), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));

                    moChildReceipts.add(hrsFormerReceipt);
                }
            }
        }

        for (SHrsFormerReceipt receipt : moChildReceipts) {
            mdTotalDeducciones += receipt.getTotalDeducciones();
            mdTotalPercepciones += receipt.getTotalPercepciones();
            mdTotalRetenciones += receipt.getTotalRetenciones();
        }
    }

    public boolean isValidPayroll() {
        double perceptions = 0;
        double deductions = 0;
        double retentions = 0;

        for (SHrsFormerReceipt receipt : moChildReceipts) {
            perceptions += receipt.getTotalPercepciones();
            deductions += receipt.getTotalDeducciones();
            retentions += receipt.getTotalRetenciones();
        }

        return SLibUtils.compareAmount(mdTotalPercepciones, SLibUtils.roundAmount(perceptions)) && 
                SLibUtils.compareAmount(mdTotalDeducciones, SLibUtils.roundAmount(deductions)) && 
                SLibUtils.compareAmount(mdTotalRetenciones, SLibUtils.roundAmount(retentions));
    }
}
