/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import cfd.DElement;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdDataAsociadoNegocios;
import erp.client.SClientInterface;
import erp.mtrn.data.SDataCfd;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas
 */
public class SHrsFormerPayroll {

    protected SClientInterface miClient;

    protected int mnPkNominaId;
    protected Date mtFecha;
    protected Date mtFechaIncial;
    protected Date mtFechaFinal;
    protected double mdTotalPercepciones;
    protected double mdTotalDeducciones;
    protected double mdTotalRetenciones;
    protected String msDescripcion;
    protected int mnEmpresaId;
    protected int mnSucursalEmpresaId;
    private String[] masRegimenFiscal;

    protected ArrayList<SHrsFormerPayrollReceipt> moChildReceipts;

    private void reset() {
        mnPkNominaId = 0;
        mtFecha = null;
        mtFechaIncial = null;
        mtFechaFinal = null;
        mdTotalPercepciones = 0;
        mdTotalDeducciones = 0;
        mdTotalRetenciones = 0;
        msDescripcion = "";
        mnEmpresaId= 0;
        mnSucursalEmpresaId= 0;
        masRegimenFiscal = null;

        moChildReceipts = new ArrayList<SHrsFormerPayrollReceipt>();
    }

    public SHrsFormerPayroll(SClientInterface client) {
        miClient = client;

        reset();
    }

    public void setPkNominaId(int n) { mnPkNominaId = n; }
    public void setFecha(Date t) { mtFecha = t; }
    public void setFechaInicial(Date t) { mtFechaIncial = t; }
    public void setFechaFinal(Date t) { mtFechaFinal = t; }
    public void setTotalPercepciones(double d) { mdTotalPercepciones = d; }
    public void setTotalDeducciones(double d) { mdTotalDeducciones = d; }
    public void setTotalRetenciones(double d) { mdTotalRetenciones = d; }
    public void setDescripcion(String s) { msDescripcion = s; }
    public void setEmpresaId(int n) { mnEmpresaId = n; }
    public void setSucursalEmpresaId(int n) { mnSucursalEmpresaId = n; }
    public void setRegimenFiscal(String[] s) { masRegimenFiscal = s; }

    public int getPkNominaId() { return mnPkNominaId; }
    public Date getFecha() { return mtFecha; }
    public Date getFechaInicial() { return mtFechaIncial; }
    public Date getFechaFinal() { return mtFechaFinal; }
    public double getTotalPercepciones() { return mdTotalPercepciones; }
    public double getTotalDeducciones() { return mdTotalDeducciones; }
    public double getTotalRetenciones() { return mdTotalRetenciones; }
    public String getDescripcion() { return msDescripcion; }
    public int getEmpresaId() { return mnEmpresaId; }
    public int getSucursalEmpresaId() { return mnSucursalEmpresaId; }
    public String[] getRegimenFiscal() { return masRegimenFiscal; }

    public ArrayList<SHrsFormerPayrollReceipt> getChildPayrollReceipts() { return moChildReceipts; }

    public void renderPayroll(ArrayList<SDataCfd> pCfd, int subtypeCfd) throws java.lang.Exception {
        double dTotalIncome = 0;
        double dTotalDeductions = 0;
        double dTotalRentRetained = 0;
        SHrsFormerPayrollReceipt payrollReceipt = null;
        SHrsFormerPayrollConcept payrollConcept = null;
        SHrsFormerPayrollExtraTime payrollExtraTime = null;
        SCfdDataAsociadoNegocios xmlEmisor = null;
        SCfdDataAsociadoNegocios xmlExpeditionSpot = null;
        SCfdDataAsociadoNegocios xmlReceptor = null;
        cfd.ver3.DElementComprobante comprobante = null;

        // Comprobante:

        comprobante = cfd.DCfdUtils.getCfdi(pCfd.get(0).getDocXml());

        mtFecha = comprobante.getAttFecha().getDatetime();
        mnPkNominaId = subtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? pCfd.get(0).getFkPayrollPayrollId_n() : pCfd.get(0).getFkPayrollReceiptPayrollId_n();

        // Emisor:

        xmlEmisor = new SCfdDataAsociadoNegocios();

        xmlEmisor.setBizPartnerRfc(comprobante.getEltEmisor().getAttRfc().getString());
        xmlEmisor.setBizPartnerName(comprobante.getEltEmisor().getAttNombre().getString());

        xmlEmisor.setBizPartnerStreet(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttCalle().getString());
        xmlEmisor.setBizPartnerStreetNumberExt(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttNoExterior().getString());
        xmlEmisor.setBizPartnerStreetNumberInt(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttNoInterior().getString());
        xmlEmisor.setBizPartnerNeighborhood(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttColonia().getString());
        xmlEmisor.setBizPartnerLocality(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttLocalidad().getString());
        xmlEmisor.setBizPartnerReference(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttReferencia().getString());
        xmlEmisor.setBizPartnerCounty(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttMunicipio().getString());
        xmlEmisor.setBizPartnerState(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttEstado().getString());
        xmlEmisor.setBizPartnerCountry(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttPais().getString());
        xmlEmisor.setBizPartnerZipCode(comprobante.getEltEmisor().getEltDomicilioFiscal().getAttCodigoPostal().getString());

        if (!comprobante.getEltEmisor().getEltHijosRegimenFiscal().isEmpty()) {
            masRegimenFiscal = new String[1];

            masRegimenFiscal[0] = comprobante.getEltEmisor().getEltHijosRegimenFiscal().get(0).getAttRegimen().getString();

            xmlEmisor.setBizPartnerFiscalRegime(masRegimenFiscal[0]);
        }

        if (comprobante.getEltEmisor().getEltOpcExpedidoEn() != null) {
            xmlExpeditionSpot = new SCfdDataAsociadoNegocios();

            xmlExpeditionSpot.setBizPartnerStreet(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttCalle().getString());
            xmlExpeditionSpot.setBizPartnerStreetNumberExt(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttNoExterior().getString());
            xmlExpeditionSpot.setBizPartnerStreetNumberInt(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttNoInterior().getString());
            xmlExpeditionSpot.setBizPartnerNeighborhood(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttColonia().getString());
            xmlExpeditionSpot.setBizPartnerLocality(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttLocalidad().getString());
            xmlExpeditionSpot.setBizPartnerReference(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttReferencia().getString());
            xmlExpeditionSpot.setBizPartnerCounty(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttMunicipio().getString());
            xmlExpeditionSpot.setBizPartnerState(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttEstado().getString());
            xmlExpeditionSpot.setBizPartnerCountry(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttPais().getString());
            xmlExpeditionSpot.setBizPartnerZipCode(comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttCodigoPostal().getString());
        }

        // load payroll receipts:

        for (SDataCfd cfdReceipt : pCfd) {
            comprobante = cfd.DCfdUtils.getCfdi(cfdReceipt.getDocXml());
            dTotalIncome = 0;
            dTotalDeductions = 0;
            dTotalRentRetained = 0;

            payrollReceipt = new SHrsFormerPayrollReceipt(this, miClient);

            xmlReceptor = new SCfdDataAsociadoNegocios();

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
            xmlReceptor.setBizPartnerState(comprobante.getEltReceptor().getEltDomicilio().getAttEstado().getString());
            xmlReceptor.setBizPartnerCountry(comprobante.getEltReceptor().getEltDomicilio().getAttPais().getString());
            xmlReceptor.setBizPartnerZipCode(comprobante.getEltReceptor().getEltDomicilio().getAttCodigoPostal().getString());

            // Payroll:

            dTotalRentRetained = comprobante.getEltImpuestos().getAttTotalImpuestosRetenidos().getDouble();

            payrollReceipt.setPkEmpleadoId(cfdReceipt.getFkPayrollBizPartnerId_n());
            payrollReceipt.setAuxEmpleadoId(cfdReceipt.getFkPayrollEmployeeId_n());

            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element.getName().compareTo("nomina:Nomina") == 0) {
                    payrollReceipt.setRegistroPatronal(((cfd.ver3.nom11.DElementNomina) element).getAttRegistroPatronal().getString());
                    payrollReceipt.setNumEmpleado(((cfd.ver3.nom11.DElementNomina) element).getAttNumEmpleado().getString());
                    payrollReceipt.setCurp(((cfd.ver3.nom11.DElementNomina) element).getAttCurp().getString());
                    payrollReceipt.setTipoRegimen(((cfd.ver3.nom11.DElementNomina) element).getAttTipoRegimen().getInteger());
                    payrollReceipt.setNumSeguridadSocial(((cfd.ver3.nom11.DElementNomina) element).getAttNumSeguridadSocial().getString());
                    payrollReceipt.setFechaPago(((cfd.ver3.nom11.DElementNomina) element).getAttFechaPago().getDate());
                    payrollReceipt.setFechaInicialPago(((cfd.ver3.nom11.DElementNomina) element).getAttFechaInicialPago().getDate());
                    payrollReceipt.setFechaFinalPago(((cfd.ver3.nom11.DElementNomina) element).getAttFechaFinalPago().getDate());
                    payrollReceipt.setNumDiasPagados(((cfd.ver3.nom11.DElementNomina) element).getAttNumDiasPagados().getDouble());
                    payrollReceipt.setDepartamento(((cfd.ver3.nom11.DElementNomina) element).getAttDepartamento().getString());
                    payrollReceipt.setClabe(((cfd.ver3.nom11.DElementNomina) element).getAttClabe().getString());
                    payrollReceipt.setBanco(((cfd.ver3.nom11.DElementNomina) element).getAttBanco().getInteger());
                    payrollReceipt.setFechaInicioRelLaboral(((cfd.ver3.nom11.DElementNomina) element).getAttFechaInicioRelLaboral().getDate());
                    payrollReceipt.setAntiguedad(((cfd.ver3.nom11.DElementNomina) element).getAttAntiguedad().getInteger());
                    payrollReceipt.setPuesto(((cfd.ver3.nom11.DElementNomina) element).getAttPuesto().getString());
                    payrollReceipt.setTipoContrato(((cfd.ver3.nom11.DElementNomina) element).getAttTipoContrato().getString());
                    payrollReceipt.setTipoJornada(((cfd.ver3.nom11.DElementNomina) element).getAttTipoJornada().getString());
                    payrollReceipt.setPeriodicidadPago(((cfd.ver3.nom11.DElementNomina) element).getAttPeriodicidadPago().getString());
                    payrollReceipt.setSalarioBaseCotApor(((cfd.ver3.nom11.DElementNomina) element).getAttSalarioBaseCotApor().getDouble());
                    payrollReceipt.setRiesgoPuesto(((cfd.ver3.nom11.DElementNomina) element).getAttRiesgoPuesto().getInteger());
                    payrollReceipt.setSalarioDiarioIntegrado(((cfd.ver3.nom11.DElementNomina) element).getAttSalarioDiarioIntegrado().getDouble());
                    payrollReceipt.setMetodoPagoAux(comprobante.getAttMetodoDePago().getString());
                    payrollReceipt.setSerie(comprobante.getAttSerie().getString());
                    payrollReceipt.setMoneda(comprobante.getAttMoneda().getString());

                    // Perceptions:

                    if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones() != null) {
                        for (int i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().size(); i++) {
                            payrollConcept = new SHrsFormerPayrollConcept();

                            payrollConcept.setPkTipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[0]);
                            payrollConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[1]);
                            payrollConcept.setClaveOficial(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getInteger());
                            payrollConcept.setClaveEmpresa("" + SLibUtils.parseInt(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString()));
                            payrollConcept.setConcepto(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttConcepto().getString());
                            payrollConcept.setTotalGravado(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble());
                            payrollConcept.setTotalExento(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble());

                            dTotalIncome += ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                            ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble();

                            payrollReceipt.getChildPayrollConcept().add(payrollConcept);

                            if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getInteger() == SCfdConsts.PER_HRS_EXT) {
                                payrollExtraTime = new SHrsFormerPayrollExtraTime();

                                for (int j = 0; j < ((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().size(); j++) {
                                    if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME) == 0) {
                                        payrollConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1]);
                                        payrollConcept.setCantidad(0);
                                        payrollConcept.setHoras_r(0);
                                        payrollExtraTime.setImportePagado(0);
                                    }
                                    else if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME_TYPE_DOUBLE) == 0) {
                                            if (((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE) == 0) {
                                                payrollConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1]);
                                                payrollConcept.setCantidad(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttDias().getInteger());
                                                payrollConcept.setHoras_r(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttHorasExtra().getInteger());
                                                payrollExtraTime.setImportePagado(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttImportePagado().getDouble());
                                            }
                                    }
                                    else if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_EXTRA_TIME_TYPE_TRIPLE) == 0) {
                                            if (((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE) == 0) {
                                                payrollConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1]);
                                                payrollConcept.setCantidad(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttDias().getInteger());
                                                payrollConcept.setHoras_r(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttHorasExtra().getInteger());
                                                payrollExtraTime.setImportePagado(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(j).getAttImportePagado().getDouble());
                                            }
                                    }
                                }

                                // Node extraTime:

                                payrollExtraTime.setDias(payrollConcept.getCantidad());
                                payrollExtraTime.setTipoHoras(payrollConcept.getPkSubtipoConcepto() == SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] ? SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                                payrollExtraTime.setHorasExtra(payrollConcept.getHoras_r());

                                payrollConcept.setChildPayrollExtraTimes(payrollExtraTime);
                            }
                        }
                    }

                    // Deductions:

                    if (((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones() != null) {
                        for (int i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().size(); i++) {
                            payrollConcept = new SHrsFormerPayrollConcept();

                            payrollConcept.setPkTipoConcepto(SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[0]);
                            payrollConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[1]);
                            payrollConcept.setClaveOficial(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger());
                            payrollConcept.setClaveEmpresa("" + SLibUtils.parseInt(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString()));
                            payrollConcept.setConcepto(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttConcepto().getString());
                            payrollConcept.setTotalGravado(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble());
                            payrollConcept.setTotalExento(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble());

                            dTotalDeductions += ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble() +
                                            ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble();

                            payrollReceipt.getChildPayrollConcept().add(payrollConcept);

                            if (SLibUtils.belongsTo(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger(), new int[] { SCfdConsts.DED_INC, SCfdConsts.DED_AUS })) {
                                for (int j = 0; j < ((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().size(); j++) {
                                }
                            }
                        }
                    }

                    payrollReceipt.setTotalPercepciones(SLibUtils.round(dTotalIncome, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    payrollReceipt.setTotalDeducciones(SLibUtils.round(dTotalDeductions, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    payrollReceipt.setTotalRetenciones(SLibUtils.round(dTotalRentRetained, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    payrollReceipt.setTotalNeto(SLibUtils.round((dTotalIncome - dTotalDeductions), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));

                    moChildReceipts.add(payrollReceipt);
                }
            }
        }

        for (SHrsFormerPayrollReceipt receipt : moChildReceipts) {
            mdTotalDeducciones += receipt.getTotalDeducciones();
            mdTotalPercepciones += receipt.getTotalPercepciones();
            mdTotalRetenciones += receipt.getTotalRetenciones();
        }
    }

    public boolean isValidPayroll() {
        double perceptions = 0;
        double deductions = 0;
        double retentions = 0;

        for (SHrsFormerPayrollReceipt receipt : moChildReceipts) {
            perceptions += receipt.getTotalPercepciones();
            deductions += receipt.getTotalDeducciones();
            retentions += receipt.getTotalRetenciones();
        }

        return (mdTotalPercepciones == SLibUtils.round(perceptions, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()) && mdTotalDeducciones == SLibUtils.round(deductions, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()) && mdTotalRetenciones == SLibUtils.round(retentions, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
    }
}
