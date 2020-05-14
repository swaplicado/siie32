/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import cfd.DCfdUtils;
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

    public void renderPayroll(ArrayList<SDataCfd> cfds) throws java.lang.Exception {
        cfd.ver32.DElementComprobante comprobante;

        // Comprobante:

        comprobante = cfd.DCfdUtils.getCfdi32(cfds.get(0).getDocXml());

        mtFecha = comprobante.getAttFecha().getDatetime();
        mnPkNominaId = cfds.get(0).getFkPayrollPayrollId_n();   // only old payroll version supported!

        // Emisor:

        SCfdDataBizPartner bizPartnerEmisor = new SCfdDataBizPartner();
        
        cfd.ver32.DElementEmisor emisor = comprobante.getEltEmisor(); // convenience variable

        bizPartnerEmisor.setBizPartnerRfc(emisor.getAttRfc().getString());
        bizPartnerEmisor.setBizPartnerName(emisor.getAttNombre().getString());
        
        cfd.ver32.DElementTipoUbicacionFiscal domicilioFiscal = emisor.getEltDomicilioFiscal();

        bizPartnerEmisor.setBizPartnerStreet(domicilioFiscal.getAttCalle().getString());
        bizPartnerEmisor.setBizPartnerStreetNumberExt(domicilioFiscal.getAttNoExterior().getString());
        bizPartnerEmisor.setBizPartnerStreetNumberInt(domicilioFiscal.getAttNoInterior().getString());
        bizPartnerEmisor.setBizPartnerNeighborhood(domicilioFiscal.getAttColonia().getString());
        bizPartnerEmisor.setBizPartnerLocality(domicilioFiscal.getAttLocalidad().getString());
        bizPartnerEmisor.setBizPartnerReference(domicilioFiscal.getAttReferencia().getString());
        bizPartnerEmisor.setBizPartnerCounty(domicilioFiscal.getAttMunicipio().getString());
        bizPartnerEmisor.setBizPartnerStateName(domicilioFiscal.getAttEstado().getString());
        bizPartnerEmisor.setBizPartnerCountryName(domicilioFiscal.getAttPais().getString());
        bizPartnerEmisor.setBizPartnerZipCode(domicilioFiscal.getAttCodigoPostal().getString());

        if (!emisor.getEltHijosRegimenFiscal().isEmpty()) {
            masRegimenFiscal = new String[1];

            masRegimenFiscal[0] = emisor.getEltHijosRegimenFiscal().get(0).getAttRegimen().getString();

            bizPartnerEmisor.setBizPartnerFiscalRegime(masRegimenFiscal[0]);
        }

        if (emisor.getEltOpcExpedidoEn() != null) {
            SCfdDataBizPartner bizPartnerEmisorExpedición = new SCfdDataBizPartner();
            cfd.ver32.DElementTipoUbicacion expedidoEn = emisor.getEltOpcExpedidoEn();

            bizPartnerEmisorExpedición.setBizPartnerStreet(expedidoEn.getAttCalle().getString());
            bizPartnerEmisorExpedición.setBizPartnerStreetNumberExt(expedidoEn.getAttNoExterior().getString());
            bizPartnerEmisorExpedición.setBizPartnerStreetNumberInt(expedidoEn.getAttNoInterior().getString());
            bizPartnerEmisorExpedición.setBizPartnerNeighborhood(expedidoEn.getAttColonia().getString());
            bizPartnerEmisorExpedición.setBizPartnerLocality(expedidoEn.getAttLocalidad().getString());
            bizPartnerEmisorExpedición.setBizPartnerReference(expedidoEn.getAttReferencia().getString());
            bizPartnerEmisorExpedición.setBizPartnerCounty(expedidoEn.getAttMunicipio().getString());
            bizPartnerEmisorExpedición.setBizPartnerStateName(expedidoEn.getAttEstado().getString());
            bizPartnerEmisorExpedición.setBizPartnerCountryName(expedidoEn.getAttPais().getString());
            bizPartnerEmisorExpedición.setBizPartnerZipCode(expedidoEn.getAttCodigoPostal().getString());
        }

        // load payroll receipts:

        for (SDataCfd cfd : cfds) {
            comprobante = DCfdUtils.getCfdi32(cfd.getDocXml());

            SHrsFormerReceipt hrsFormerReceipt = new SHrsFormerReceipt(miClient, this);

            // Receptor:
            
            SCfdDataBizPartner bizPartnerReceptor = new SCfdDataBizPartner();
            
            cfd.ver32.DElementReceptor receptor = comprobante.getEltReceptor();
            
            bizPartnerReceptor.setBizPartnerRfc(receptor.getAttRfc().getString());
            bizPartnerReceptor.setBizPartnerName(receptor.getAttNombre().getString());

            cfd.ver32.DElementTipoUbicacion domicilio = receptor.getEltDomicilio();
            
            bizPartnerReceptor.setBizPartnerStreet(domicilio.getAttCalle().getString());
            bizPartnerReceptor.setBizPartnerStreetNumberExt(domicilio.getAttNoExterior().getString());
            bizPartnerReceptor.setBizPartnerStreetNumberInt(domicilio.getAttNoInterior().getString());
            bizPartnerReceptor.setBizPartnerNeighborhood(domicilio.getAttColonia().getString());
            bizPartnerReceptor.setBizPartnerLocality(domicilio.getAttLocalidad().getString());
            bizPartnerReceptor.setBizPartnerReference(domicilio.getAttReferencia().getString());
            bizPartnerReceptor.setBizPartnerCounty(domicilio.getAttMunicipio().getString());
            bizPartnerReceptor.setBizPartnerStateName(domicilio.getAttEstado().getString());
            bizPartnerReceptor.setBizPartnerCountryName(domicilio.getAttPais().getString());
            bizPartnerReceptor.setBizPartnerZipCode(domicilio.getAttCodigoPostal().getString());

            // Payroll:

            double totalPayments = 0;
            double totalDiscounts = 0;
            double totalTaxWithheld = comprobante.getEltImpuestos().getAttTotalImpuestosRetenidos().getDouble();

            hrsFormerReceipt.setPkEmpleadoId(cfd.getFkPayrollBizPartnerId_n());
            hrsFormerReceipt.setAuxEmpleadoId(cfd.getFkPayrollEmployeeId_n());

            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element.getName().equals("nomina:Nomina")) {
                    cfd.ver3.nom11.DElementNomina nomina = ((cfd.ver3.nom11.DElementNomina) element);
                    
                    hrsFormerReceipt.setRegistroPatronal(nomina.getAttRegistroPatronal().getString());
                    hrsFormerReceipt.setNumEmpleado(nomina.getAttNumEmpleado().getString());
                    hrsFormerReceipt.setCurp(nomina.getAttCurp().getString());
                    hrsFormerReceipt.setTipoRegimen(nomina.getAttTipoRegimen().getInteger());
                    hrsFormerReceipt.setNumSeguridadSocial(nomina.getAttNumSeguridadSocial().getString());
                    hrsFormerReceipt.setFechaPago(nomina.getAttFechaPago().getDate());
                    hrsFormerReceipt.setFechaInicialPago(nomina.getAttFechaInicialPago().getDate());
                    hrsFormerReceipt.setFechaFinalPago(nomina.getAttFechaFinalPago().getDate());
                    hrsFormerReceipt.setNumDiasPagados(nomina.getAttNumDiasPagados().getDouble());
                    hrsFormerReceipt.setDepartamento(nomina.getAttDepartamento().getString());
                    hrsFormerReceipt.setCuentaBancaria(nomina.getAttClabe().getString());
                    hrsFormerReceipt.setBanco(nomina.getAttBanco().getInteger());
                    hrsFormerReceipt.setFechaInicioRelLaboral(nomina.getAttFechaInicioRelLaboral().getDate());
                    hrsFormerReceipt.setAntiguedad(nomina.getAttAntiguedad().getInteger());
                    hrsFormerReceipt.setPuesto(nomina.getAttPuesto().getString());
                    hrsFormerReceipt.setTipoContrato(nomina.getAttTipoContrato().getString());
                    hrsFormerReceipt.setTipoJornada(nomina.getAttTipoJornada().getString());
                    hrsFormerReceipt.setPeriodicidadPago(nomina.getAttPeriodicidadPago().getString());
                    hrsFormerReceipt.setSalarioBaseCotApor(nomina.getAttSalarioBaseCotApor().getDouble());
                    hrsFormerReceipt.setRiesgoPuesto(nomina.getAttRiesgoPuesto().getInteger());
                    hrsFormerReceipt.setSalarioDiarioIntegrado(nomina.getAttSalarioDiarioIntegrado().getDouble());
                    hrsFormerReceipt.setMetodoPagoAux(comprobante.getAttMetodoDePago().getString());
                    hrsFormerReceipt.setSerie(comprobante.getAttSerie().getString());
                    hrsFormerReceipt.setMoneda(comprobante.getAttMoneda().getString());

                    // Perceptions:

                    if (nomina.getEltPercepciones() != null) {
                        for (int i = 0; i < nomina.getEltPercepciones().getEltHijosPercepcion().size(); i++) {
                            SHrsFormerReceiptConcept conceptPayment = new SHrsFormerReceiptConcept();

                            conceptPayment.setPkTipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[0]);
                            conceptPayment.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[1]);
                            conceptPayment.setClaveOficial(nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getInteger());
                            conceptPayment.setClaveEmpresa("" + SLibUtils.parseInt(nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString()));
                            conceptPayment.setConcepto(nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttConcepto().getString());
                            conceptPayment.setTotalGravado(nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble());
                            conceptPayment.setTotalExento(nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble());

                            totalPayments += nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                            nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble();

                            hrsFormerReceipt.getChildConcepts().add(conceptPayment);

                            if (nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getInteger() == SModSysConsts.HRSS_TP_EAR_OVER_TIME) {
                                SHrsFormerConceptExtraTime conceptExtraTime = new SHrsFormerConceptExtraTime();

                                for (int j = 0; j < nomina.getEltHorasExtras().getEltHijosHorasExtra().size(); j++) {
                                    if (nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME) == 0) {
                                        conceptPayment.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1]);
                                        conceptPayment.setCantidad(0);
                                        conceptPayment.setHoras_r(0);
                                        conceptExtraTime.setImportePagado(0);
                                    }
                                    else if (nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME_TYPE_DOUBLE) == 0) {
                                            if (nomina.getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE) == 0) {
                                                conceptPayment.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1]);
                                                conceptPayment.setCantidad(nomina.getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttDias().getInteger());
                                                conceptPayment.setHoras_r(nomina.getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttHorasExtra().getInteger());
                                                conceptExtraTime.setImportePagado(nomina.getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttImportePagado().getDouble());
                                            }
                                    }
                                    else if (nomina.getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME_TYPE_TRIPLE) == 0) {
                                            if (nomina.getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE) == 0) {
                                                conceptPayment.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1]);
                                                conceptPayment.setCantidad(nomina.getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttDias().getInteger());
                                                conceptPayment.setHoras_r(nomina.getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttHorasExtra().getInteger());
                                                conceptExtraTime.setImportePagado(nomina.getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttImportePagado().getDouble());
                                            }
                                    }
                                }

                                // Node extraTime:

                                conceptExtraTime.setDias(conceptPayment.getCantidad());
                                conceptExtraTime.setTipoHoras(conceptPayment.getPkSubtipoConcepto() == SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] ? SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                                conceptExtraTime.setHorasExtra(conceptPayment.getHoras_r());

                                conceptPayment.setChildExtraTime(conceptExtraTime);
                            }
                        }
                    }

                    // Deductions:

                    if (nomina.getEltDeducciones() != null) {
                        for (int i = 0; i < nomina.getEltDeducciones().getEltHijosDeduccion().size(); i++) {
                            SHrsFormerReceiptConcept conceptDiscount = new SHrsFormerReceiptConcept();

                            conceptDiscount.setPkTipoConcepto(SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[0]);
                            conceptDiscount.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[1]);
                            conceptDiscount.setClaveOficial(nomina.getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger());
                            conceptDiscount.setClaveEmpresa("" + SLibUtils.parseInt(nomina.getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString()));
                            conceptDiscount.setConcepto(nomina.getEltDeducciones().getEltHijosDeduccion().get(i).getAttConcepto().getString());
                            conceptDiscount.setTotalGravado(nomina.getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble());
                            conceptDiscount.setTotalExento(nomina.getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble());

                            totalDiscounts += nomina.getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble() +
                                            nomina.getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble();

                            hrsFormerReceipt.getChildConcepts().add(conceptDiscount);

                            if (SLibUtils.belongsTo(nomina.getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger(), new int[] { SModSysConsts.HRSS_TP_DED_DIS, SModSysConsts.HRSS_TP_DED_ABS })) {
                                for (int j = 0; j < nomina.getEltIncapacidades().getEltHijosIncapacidad().size(); j++) {
                                }
                            }
                        }
                    }

                    hrsFormerReceipt.setTotalPercepciones(SLibUtils.round(totalPayments, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    hrsFormerReceipt.setTotalDeducciones(SLibUtils.round(totalDiscounts, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    hrsFormerReceipt.setTotalRetenciones(SLibUtils.round(totalTaxWithheld, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    hrsFormerReceipt.setTotalNeto(SLibUtils.round((totalPayments - totalDiscounts), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));

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
