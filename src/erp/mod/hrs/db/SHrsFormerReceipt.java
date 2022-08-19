/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import cfd.DAttributeOptionCondicionesPago;
import cfd.DCfdConsts;
import cfd.DElement;
import cfd.ver2.DAttributeOptionFormaDePago;
import cfd.ver2.DAttributeOptionTipoDeComprobante;
import cfd.ver3.DCfdVer3Utils;
import cfd.ver33.DCfdi33Catalogs;
import erp.cfd.SCfdConsts;
import erp.cfd.SCfdDataConcepto;
import erp.cfd.SCfdDataImpuesto;
import erp.cfd.SCfdXmlCfdi32;
import erp.cfd.SCfdXmlCfdi33;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Juan Barajas, Sergio Flores
 * 
 * Maintenance Log:
 * 2016-07-11, Sergio Flores:
 *  Deletion of obsolete import sentences.
 * 2018-01-02, Sergio Flores:
 *  Implementation of payroll into CFDI 3.3.
 */
public class SHrsFormerReceipt implements SCfdXmlCfdi32, SCfdXmlCfdi33 {

    protected SClientInterface miClient;

    protected int mnPkEmpleadoId;
    protected int mnPkSucursalEmpleadoId;
    protected String msRegistroPatronal;
    protected String msEmpleadoNum;
    protected String msEmpleadoCurp;
    protected int mnTipoRegimen;
    protected String msTipoRegimenCode;
    protected String msNumSeguridadSocial;
    protected Date mtFechaPago;
    protected Date mtFechaInicialPago;
    protected Date mtFechaFinalPago;
    protected double mdNumDiasPagados;
    protected String msDepartamento;
    protected int mnBanco;
    protected String msCuentaBancaria;
    protected Date mtFechaInicioRelLaboral;
    /** Antigüedad en semanas. */
    protected int mnAntiguedad;
    protected String msPuesto;
    protected String msTipoContrato;
    protected String msSindicalizado;
    protected String msTipoJornada;
    protected String msPeriodicidadPago;
    protected double mdSalarioBaseCotApor;
    protected int mnRiesgoPuesto;
    protected String msRiesgoPuesto;
    protected double mdSalarioDiarioIntegrado;
    protected String msClaveEstado;

    protected double mdTotalPercepciones;
    protected double mdTotalDeducciones;
    protected double mdTotalRetenciones;
    protected double mdTotalNeto;
    protected int mnMetodoPago;
    protected String msMetodoPagoAux;
    protected String msSerie;
    protected int mnFolio;
    protected String msMoneda;
    protected String msLugarExpedicion;
    protected String msConfirmacion;
    protected String msRegimenFiscal;
    protected String msCfdiRelacionadosTipoRelacion;
    protected ArrayList<String> maCfdiRelacionados;

    protected int mnAuxEmpleadoId;
    protected double mdAuxSueldoMensual;

    protected SHrsFormerPayroll moParentPayroll;
    protected ArrayList<SHrsFormerReceiptConcept> moChildConcepts;

    public SHrsFormerReceipt(SClientInterface client, SHrsFormerPayroll parentPayroll) {
        miClient = client;

        mnPkEmpleadoId = 0;
        mnPkSucursalEmpleadoId = 0;
        msRegistroPatronal = "";
        msEmpleadoNum = "";
        msEmpleadoCurp = "";
        mnTipoRegimen = 0;
        msTipoRegimenCode = "";
        msNumSeguridadSocial = "";
        mtFechaPago = null;
        mtFechaInicialPago = null;
        mtFechaFinalPago = null;
        mdNumDiasPagados = 0;
        msDepartamento = "";
        mnBanco = 0;
        msCuentaBancaria = "";
        mtFechaInicioRelLaboral = null;
        mnAntiguedad = 0;
        msPuesto = "";
        msTipoContrato = "";
        msSindicalizado = "";
        msTipoJornada = "";
        msPeriodicidadPago = "";
        mdSalarioBaseCotApor = 0;
        mnRiesgoPuesto = 0;
        msRiesgoPuesto = "";
        mdSalarioDiarioIntegrado = 0;
        msClaveEstado = "";

        mdTotalPercepciones = 0;
        mdTotalDeducciones = 0;
        mdTotalRetenciones = 0;
        mdTotalNeto = 0;
        mnMetodoPago = 0;
        msMetodoPagoAux = "";
        msSerie = "";
        mnFolio = 0;
        msMoneda = "";
        msLugarExpedicion = "";
        msConfirmacion = "";
        msRegimenFiscal = "";
        msCfdiRelacionadosTipoRelacion = "";
        maCfdiRelacionados = new ArrayList<>();

        mnAuxEmpleadoId = 0;
        mdAuxSueldoMensual = 0;

        moParentPayroll = parentPayroll;
        moChildConcepts = new ArrayList<>();
    }

    public void setPkEmpleadoId(int n) { mnPkEmpleadoId = n; }
    public void setPkSucursalEmpleadoId(int n) { mnPkSucursalEmpleadoId = n; }
    public void setRegistroPatronal(String s) { msRegistroPatronal = s; }
    public void setEmpleadoNum(String s) { msEmpleadoNum = s; }
    public void setEmpleadoCurp(String s) { msEmpleadoCurp = s; }
    public void setTipoRegimen(int n) { mnTipoRegimen = n; setTipoRegimenCode((String) miClient.getSession().readField(SModConsts.HRSS_TP_REC_SCHE, new int[] { mnTipoRegimen }, SDbRegistry.FIELD_CODE)); }
    public void setTipoRegimenCode(String s) { msTipoRegimenCode = s; }
    public void setNumSeguridadSocial(String s) { msNumSeguridadSocial = s; }
    public void setFechaPago(Date t) { mtFechaPago = t; }
    public void setFechaInicialPago(Date t) { mtFechaInicialPago = t; }
    public void setFechaFinalPago(Date t) { mtFechaFinalPago = t; }
    public void setNumDiasPagados(double d) { mdNumDiasPagados = d; }
    public void setDepartamento(String s) { msDepartamento = s; }
    public void setBanco(int n) { mnBanco = n; }
    public void setCuentaBancaria(String s) { msCuentaBancaria = s; }
    public void setFechaInicioRelLaboral(Date t) { mtFechaInicioRelLaboral = t; }
    /** Antigüedad en semanas. */
    public void setAntiguedad(int n) { mnAntiguedad = n; }
    public void setPuesto(String s) { msPuesto = s; }
    public void setTipoContrato(String s) { msTipoContrato = s; }
    public void setSindicalizado(String s) { msSindicalizado = s; }
    public void setTipoJornada(String s) { msTipoJornada = s; }
    public void setPeriodicidadPago(String s) { msPeriodicidadPago = s; }
    public void setSalarioBaseCotApor(double d) { mdSalarioBaseCotApor = d; }
    public void setRiesgoPuesto(int n) { mnRiesgoPuesto = n; }
    public void setRiesgoPuesto(String s) { msRiesgoPuesto = s; }
    public void setSalarioDiarioIntegrado(double d) { mdSalarioDiarioIntegrado = d; }
    public void setClaveEstado(String s) { msClaveEstado = s; }

    public void setTotalPercepciones(double d) { mdTotalPercepciones = d; }
    public void setTotalDeducciones(double d) { mdTotalDeducciones = d; }
    public void setTotalRetenciones(double d) { mdTotalRetenciones = d; }
    public void setTotalNeto(double d) { mdTotalNeto = d; }
    public void setMetodoPago(int n) { mnMetodoPago = n; }
    public void setMetodoPagoAux(String s) { msMetodoPagoAux = s; }
    public void setSerie(String s) { msSerie = s; }
    public void setFolio(int n) { mnFolio = n; }
    public void setMoneda(String s) { msMoneda = s; }
    public void setLugarExpedicion(String s) { msLugarExpedicion = s; }
    public void setConfirmacion(String s) { msConfirmacion = s; }
    public void setRegimenFiscal(String s) { msRegimenFiscal = s; }
    public void setCfdiRelacionadosTipoRelacion(String s) { msCfdiRelacionadosTipoRelacion = s; }

    public void setAuxEmpleadoId(int n) { mnAuxEmpleadoId = n; }
    public void setAuxSueldoMensual(double d) { mdAuxSueldoMensual = d; }

    public void setParentPayroll(SHrsFormerPayroll o) { moParentPayroll = o; }

    public int getPkEmpleadoId() { return mnPkEmpleadoId; }
    public int getPkSucursalEmpleadoId() { return mnPkSucursalEmpleadoId; }
    public String getRegistroPatronal() { return msRegistroPatronal; }
    public String getEmpleadoNum() { return msEmpleadoNum; }
    public String getEmpleadoCurp() { return msEmpleadoCurp; }
    public int getTipoRegimen() { return mnTipoRegimen; }
    public String getTipoRegimenCode() { return msTipoRegimenCode; }
    public String getNumSeguridadSocial() { return msNumSeguridadSocial; }
    public Date getFechaPago() { return mtFechaPago; }
    public Date getFechaInicialPago() { return mtFechaInicialPago; }
    public Date getFechaFinalPago() { return mtFechaFinalPago; }
    public double getNumDiasPagados() { return mdNumDiasPagados; }
    public String getDepartamento() { return msDepartamento; }
    public int getBanco() { return mnBanco; }
    public String getCuentaBancaria() { return msCuentaBancaria; }
    public Date getFechaInicioRelLaboral() { return mtFechaInicioRelLaboral; }
    /** Antigüedad en semanas. */
    public int getAntiguedad() { return mnAntiguedad; }
    public String getPuesto() { return msPuesto; }
    public int getRiesgoPuesto() { return mnRiesgoPuesto; }
    public String getTipoContrato() { return msTipoContrato; }
    public String getSindicalizado() { return msSindicalizado; }
    public String getTipoJornada() { return msTipoJornada; }
    public String getPeriodicidadPago() { return msPeriodicidadPago; }
    public double getSalarioBaseCotApor() { return mdSalarioBaseCotApor; }
    public double getSalarioDiarioIntegrado() { return mdSalarioDiarioIntegrado; }
    public String getClaveEstado() { return msClaveEstado; }

    public double getTotalPercepciones() { return mdTotalPercepciones; }
    public double getTotalDeducciones() { return mdTotalDeducciones; }
    public double getTotalRetenciones() { return mdTotalRetenciones; }
    public double getTotalNeto() { return mdTotalNeto; }
    public int getMetodoPago() { return mnMetodoPago; }
    public String getMetodoPagoAux() { return msMetodoPagoAux; }
    public String getSerie() { return msSerie; }
    public int getFolio() { return mnFolio; }
    public String getMoneda() { return msMoneda; }
    public String getLugarExpedicion() { return msLugarExpedicion; }
    public String getConfirmacion() { return msConfirmacion; }
    public String getRegimenFiscal() { return msRegimenFiscal; }
    //public String getCfdiRelacionadosTipoRelacion() { return msCfdiRelacionadosTipoRelacion; } // implemented within interface SCfdXmlCfdi33
    //public ArrayList<String> getCfdiRelacionados() { return maCfdiRelacionados; } // implemented within interface SCfdXmlCfdi33
    
    public int getAuxEmpleadoId() { return mnAuxEmpleadoId; }
    public double getAuxSueldoMensual() { return mdAuxSueldoMensual; }
    
    public SHrsFormerPayroll getParentPayroll() { return moParentPayroll; }
    public ArrayList<SHrsFormerReceiptConcept> getChildConcepts() { return moChildConcepts; }

    /*
     * CFDI methods:
     */

    private boolean isTypeContractForEmployment() {
        return SHrsCfdUtils.isTypeContractForEmployment(msTipoContrato);
    }
    
    private boolean isTypeRecruitmentSchemaForEmployment() {
        return SHrsCfdUtils.isTypeRecruitmentSchemaForEmployment(msTipoRegimenCode);
    }
    
    private String composeKey(final String key, final int minLen) {
        return key.length() < minLen ? SLibUtils.textRepeat("0", minLen - key.length()) + key : key;
    }
    
    private cfd.ver3.nom12.DElementDeduccion createElementDeduction(final SHrsFormerReceiptConcept concept) {
        cfd.ver3.nom12.DElementDeduccion deduccion = new cfd.ver3.nom12.DElementDeduccion();

        deduccion.getAttTipoDeduccion().setString((String) miClient.getSession().readField(SModConsts.HRSS_TP_DED, new int[] { concept.getClaveOficial() }, SDbRegistry.FIELD_CODE));
        deduccion.getAttClave().setString(DCfdVer3Utils.formatAttributeValueAsKey(composeKey(concept.getClaveEmpresa(), 3)));
        deduccion.getAttConcepto().setString(SLibUtils.textToXml(concept.getConcepto()));
        deduccion.getAttImporte().setDouble(concept.getTotalImporte());
        
        return deduccion;
    }
    
    private cfd.ver3.nom12.DElementPercepcion createElementEarning(final SHrsFormerReceiptConcept concept) {
        cfd.ver3.nom12.DElementPercepcion percepcion = new cfd.ver3.nom12.DElementPercepcion();

        percepcion.getAttTipoPercepcion().setString((String) miClient.getSession().readField(SModConsts.HRSS_TP_EAR, new int[] { concept.getClaveOficial() }, SDbRegistry.FIELD_CODE));
        percepcion.getAttClave().setString(DCfdVer3Utils.formatAttributeValueAsKey(composeKey(concept.getClaveEmpresa(), 3)));
        percepcion.getAttConcepto().setString(SLibUtils.textToXml(concept.getConcepto()));
        percepcion.getAttImporteGravado().setDouble(concept.getTotalGravado());
        percepcion.getAttImporteExento().setDouble(concept.getTotalExento());
        
        return percepcion;
    }
    
    private cfd.ver3.nom12.DElementHorasExtra createElementEarningOverTime(final SHrsFormerReceiptConcept concept) {
        cfd.ver3.nom12.DElementHorasExtra horasExtra = new cfd.ver3.nom12.DElementHorasExtra();

        horasExtra.getAttDias().setInteger((int) concept.getCantidad());
        horasExtra.getAttTipoHoras().setString(concept.getPkSubtipoConcepto() == SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] ? SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE_CODE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE_CODE);
        horasExtra.getAttHorasExtra().setInteger(concept.getHoras_r());
        horasExtra.getAttImportePagado().setDouble(concept.getTotalImporte());
        
        return horasExtra;
    }
    
    private cfd.ver3.nom12.DElementSeparacionIndemnizacion createElementEarningSeparation(final double totalGravado, final double totalExento) {
        cfd.ver3.nom12.DElementSeparacionIndemnizacion separacionIndemnizacion = new cfd.ver3.nom12.DElementSeparacionIndemnizacion();
        
        separacionIndemnizacion.getAttTotalPagado().setDouble(totalGravado + totalExento);
        separacionIndemnizacion.getAttNumAñosServicio().setInteger((int) SLibUtils.round(((double) mnAntiguedad / SHrsConsts.YEAR_WEEKS), 0));
        separacionIndemnizacion.getAttUltimoSueldoMensOrd().setDouble(mdAuxSueldoMensual);
        separacionIndemnizacion.getAttIngresoAcumulable().setDouble(totalGravado);
        separacionIndemnizacion.getAttIngresoNoAcumulable().setDouble(totalExento);
        
        return separacionIndemnizacion;
    }
    
    private cfd.ver3.nom12.DElementIncapacidad createElementEarningDisability(final SHrsFormerReceiptConcept concept) {
        cfd.ver3.nom12.DElementIncapacidad incapacidad =  new cfd.ver3.nom12.DElementIncapacidad();
        
        incapacidad.getAttDiasIncapacidad().setInteger((int) concept.getCantidad());
        incapacidad.getAttTipoIncapacidad().setString(concept.getXtaIncapacidadClave());
        incapacidad.getAttImporteMonetario().setDouble(concept.getTotalImporte());
        
        return incapacidad;
    }
    
    private cfd.ver3.nom12.DElementOtroPago createElementOtroPago12(final String tipoOtroPago, final String clave, final String concepto, final double importe) {
        cfd.ver3.nom12.DElementOtroPago otroPago = new cfd.ver3.nom12.DElementOtroPago();
        otroPago.getAttTipoOtroPago().setString(tipoOtroPago);
        otroPago.getAttClave().setString(SLibUtils.textToXml(clave));
        otroPago.getAttConcepto().setString(SLibUtils.textToXml(concepto));
        otroPago.getAttImporte().setDouble(importe);
        return otroPago;
    }
    
    private cfd.ver3.nom12.DElementSubsidioEmpleo createElementSubsidioEmpleo12(final double subsidioCausado) {
        cfd.ver3.nom12.DElementSubsidioEmpleo subsidioEmpleo = new cfd.ver3.nom12.DElementSubsidioEmpleo();
        subsidioEmpleo.getAttSubsidioCausado().setDouble(subsidioCausado);
        return subsidioEmpleo;
    }
    
    private cfd.ver3.nom12.DElementCompensacionSaldosFavor createElementCompensacionSaldosFavor12(final double saldoAFavor, final double saldoAFavorRemanente, final int año) {
        cfd.ver3.nom12.DElementCompensacionSaldosFavor compensacionSaldosFavor = new cfd.ver3.nom12.DElementCompensacionSaldosFavor();
        compensacionSaldosFavor.getAttSaldoFavor().setDouble(saldoAFavor);
        compensacionSaldosFavor.getAttAño().setInteger(año);
        compensacionSaldosFavor.getAttRemanenteSalFav().setDouble(saldoAFavorRemanente);
        return compensacionSaldosFavor;
    }
    
    private cfd.ver3.nom12.DElementOtroPago createElementEarningOtherPayment(final SHrsFormerReceiptConcept concept) {
        cfd.ver3.nom12.DElementOtroPago otroPago = null;

        if (concept.getTotalImporte() != 0 || concept.getXtaSubsidioEmpleo() != 0) {
            String conceptoOtroPago = "";
            
            switch (concept.getClaveOficial()) {
                case SModSysConsts.HRSS_TP_EAR_TAX_SUB:
                    conceptoOtroPago = SCfdConsts.CFDI_OTHER_PAY_TAX_SUBSIDY_EFF; // to preserve text case according to oficially suggested text
                    break;
                    
                case SModSysConsts.HRSS_TP_EAR_OTH:
                    conceptoOtroPago = concept.getConcepto();
                    break;
                    
                default:
            }
            
            if (!conceptoOtroPago.isEmpty()) { // guarantee than only real other payments are processed
                otroPago = createElementOtroPago12(
                        concept.getXtaTipoOtroPagoClave(), 
                        DCfdVer3Utils.formatAttributeValueAsKey(composeKey(concept.getClaveEmpresa(), 3)), 
                        conceptoOtroPago, 
                        concept.getTotalImporte());
                
                switch (concept.getClaveOficial()) {
                    case SModSysConsts.HRSS_TP_EAR_TAX_SUB:
                        otroPago.setEltSubsidioEmpleo(createElementSubsidioEmpleo12(concept.getXtaSubsidioEmpleo()));
                        break;
                        
                    case SModSysConsts.HRSS_TP_EAR_OTH:
                        switch (concept.getXtaTipoOtroPagoId()) {
                            case SModSysConsts.HRSS_TP_OTH_PAY_TAX_BAL:
                                otroPago.setEltCompensacionSaldosFavor(createElementCompensacionSaldosFavor12(concept.getXtaCompSaldoAFavor(), concept.getXtaCompSaldoAFavorRemanente(), concept.getXtaCompAño()));
                                break;
                                
                            default:
                        }
                        break;
                        
                    default:
                }
            }
        }
        
        return otroPago;
    }
    
    @Deprecated
    private cfd.DElement createCfdiElementNomina11() throws java.lang.Exception {
        double dTotalTaxedPerceptions = 0;
        double dTotalExemptPerceptions = 0;
        double dTotalTaxedDeductions = 0;
        double dTotalExemptDeductions = 0;
        double dTotalExtraTime = 0;
        double dTotalExtraTimeAux = 0;
        cfd.ver3.nom11.DElementNomina nomina = new cfd.ver3.nom11.DElementNomina();
        cfd.ver3.nom11.DElementPercepciones percepciones = new cfd.ver3.nom11.DElementPercepciones();
        cfd.ver3.nom11.DElementDeducciones deducciones = new cfd.ver3.nom11.DElementDeducciones();
        cfd.ver3.nom11.DElementIncapacidades incapacidades = new cfd.ver3.nom11.DElementIncapacidades();
        cfd.ver3.nom11.DElementHorasExtras horasExtras = new cfd.ver3.nom11.DElementHorasExtras();

        nomina.getAttRegistroPatronal().setString(msRegistroPatronal);
        nomina.getAttNumEmpleado().setString(msEmpleadoNum);
        nomina.getAttCurp().setString(msEmpleadoCurp);
        nomina.getAttTipoRegimen().setInteger(mnTipoRegimen);
        nomina.getAttNumSeguridadSocial().setString(msNumSeguridadSocial);
        nomina.getAttFechaPago().setDate(mtFechaPago);
        nomina.getAttFechaInicialPago().setDate(mtFechaInicialPago);
        nomina.getAttFechaFinalPago().setDate(mtFechaFinalPago);
        nomina.getAttNumDiasPagados().setDouble(mdNumDiasPagados);
        nomina.getAttDepartamento().setString(msDepartamento);
        nomina.getAttClabe().setString(msCuentaBancaria);
        nomina.getAttBanco().setInteger(mnBanco);
        nomina.getAttFechaInicioRelLaboral().setDate(mtFechaInicioRelLaboral);
        nomina.getAttAntiguedad().setInteger(mnAntiguedad);
        nomina.getAttPuesto().setString(msPuesto);
        nomina.getAttTipoContrato().setString(msTipoContrato);
        nomina.getAttTipoJornada().setString(msTipoJornada);
        nomina.getAttPeriodicidadPago().setString(msPeriodicidadPago);
        nomina.getAttSalarioBaseCotApor().setDouble(mdSalarioBaseCotApor);
        nomina.getAttRiesgoPuesto().setInteger(mnRiesgoPuesto);
        nomina.getAttSalarioDiarioIntegrado().setDouble(mdSalarioDiarioIntegrado);

        for (SHrsFormerReceiptConcept concept : moChildConcepts) {
            switch (concept.getPkTipoConcepto()) {
                case SCfdConsts.CFDI_PAYROLL_PERCEPTION:
                    cfd.ver3.nom11.DElementPercepcion percepcion = new cfd.ver3.nom11.DElementPercepcion();

                    dTotalTaxedPerceptions = SLibUtils.roundAmount(dTotalTaxedPerceptions + concept.getTotalGravado());
                    dTotalExemptPerceptions = SLibUtils.roundAmount(dTotalExemptPerceptions + concept.getTotalExento());

                    percepcion.getAttTipoPercepcion().setInteger(concept.getClaveOficial());
                    percepcion.getAttClave().setString(composeKey(concept.getClaveEmpresa(), 3));
                    percepcion.getAttConcepto().setString(concept.getConcepto());
                    percepcion.getAttImporteGravado().setDouble(concept.getTotalGravado());
                    percepcion.getAttImporteExento().setDouble(concept.getTotalExento());

                    percepciones.getEltHijosPercepcion().add(percepcion);

                    if (SLibUtils.belongsTo(concept.getPkSubtipoConcepto(), new int[] { SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1], SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1] })) {
                        cfd.ver3.nom11.DElementHorasExtra horasExtra = new cfd.ver3.nom11.DElementHorasExtra();

                        horasExtra.getAttDias().setInteger((int) concept.getCantidad());
                        horasExtra.getAttTipoHoras().setString(concept.getPkSubtipoConcepto() == SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] ? SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                        horasExtra.getAttHorasExtra().setInteger(concept.getHoras_r());
                        horasExtra.getAttImportePagado().setDouble(concept.getTotalImporte());

                        dTotalExtraTime = SLibUtils.roundAmount(dTotalExtraTime + concept.getTotalImporte());

                        horasExtras.getEltHijosHorasExtra().add(horasExtra);
                    }
                    break;
                    
                case SCfdConsts.CFDI_PAYROLL_DEDUCTION:
                    cfd.ver3.nom11.DElementDeduccion deduccion = new cfd.ver3.nom11.DElementDeduccion();

                    dTotalTaxedDeductions = SLibUtils.roundAmount(dTotalTaxedDeductions + concept.getTotalGravado());
                    dTotalExemptDeductions = SLibUtils.roundAmount(dTotalExemptDeductions + concept.getTotalExento());

                    deduccion.getAttTipoDeduccion().setInteger(concept.getClaveOficial());
                    deduccion.getAttClave().setString(composeKey(concept.getClaveEmpresa(), 3));
                    deduccion.getAttConcepto().setString(concept.getConcepto());
                    deduccion.getAttImporteGravado().setDouble(concept.getTotalGravado());
                    deduccion.getAttImporteExento().setDouble(concept.getTotalExento());

                    deducciones.getEltHijosDeduccion().add(deduccion);
                    break;
                    
                default:
            }
        }

        if (!percepciones.getEltHijosPercepcion().isEmpty()) {
            percepciones.getAttTotalGravado().setDouble(dTotalTaxedPerceptions);
            percepciones.getAttTotalExento().setDouble(dTotalExemptPerceptions);
            nomina.setEltPercepciones(percepciones);
        }

        if (!deducciones.getEltHijosDeduccion().isEmpty()) {
            deducciones.getAttTotalGravado().setDouble(dTotalTaxedDeductions);
            deducciones.getAttTotalExento().setDouble(dTotalExemptDeductions);
            nomina.setEltDeducciones(deducciones);
        }

        if (!horasExtras.getEltHijosHorasExtra().isEmpty()) {
            nomina.setEltHorasExtras(horasExtras);
        }

        if (!incapacidades.getEltHijosIncapacidad().isEmpty()) {
            nomina.setEltIncapacidades(incapacidades);
        }

        for (int i = 0; i < horasExtras.getEltHijosHorasExtra().size(); i++) {
            dTotalExtraTimeAux += horasExtras.getEltHijosHorasExtra().get(i).getAttImportePagado().getDouble();
        }

        if (SLibUtils.round(dTotalExtraTime, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()) != SLibUtils.round(dTotalExtraTimeAux, SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits())) {
            nomina = null;
            throw new Exception("El importe pagado de horas extra (" + SLibUtils.getDecimalFormatAmount().format(dTotalExtraTimeAux) + ") no coincide con el total de las percepciones de horas extra (" + SLibUtils.getDecimalFormatAmount().format(dTotalExtraTime) + ").");
        }

        return nomina;
    }
    
    private cfd.DElement createCfdiElementNomina12() throws java.lang.Exception {
        String sPayrollType = "";
        double dTotalGravado = 0;
        double dTotalExento = 0;
        double dTotalSueldos = 0;
        double dTotalSeparacionIndemnizacion = 0;
        double dTotalSeparacionIndemnizacionGravado = 0;
        double dTotalSeparacionIndemnizacionExento = 0;
        double dTotalImpuestosRetenidos = 0;
        double dTotalOtrasDeducciones = 0;
        cfd.ver3.nom12.DElementNomina nomina = new cfd.ver3.nom12.DElementNomina();
        cfd.ver3.nom12.DElementEmisor emisor = new cfd.ver3.nom12.DElementEmisor();
        cfd.ver3.nom12.DElementReceptor receptor = new cfd.ver3.nom12.DElementReceptor();
        cfd.ver3.nom12.DElementPercepciones percepciones = new cfd.ver3.nom12.DElementPercepciones();
        cfd.ver3.nom12.DElementDeducciones deducciones = new cfd.ver3.nom12.DElementDeducciones();
        cfd.ver3.nom12.DElementIncapacidades incapacidades = new cfd.ver3.nom12.DElementIncapacidades();
        cfd.ver3.nom12.DElementOtrosPagos otrosPagos = new cfd.ver3.nom12.DElementOtrosPagos();
        
        if (SLibUtils.belongsTo(moParentPayroll.getFkNominaTipoId(), new int[] { SModSysConsts.HRSS_TP_PAY_SHT_NOR, SModSysConsts.HRSS_TP_PAY_SHT_SPE } )) {
            sPayrollType = DCfdi33Catalogs.ClaveNominaOrd;
        }
        else if (SLibUtils.belongsTo(moParentPayroll.getFkNominaTipoId(), new int[] { SModSysConsts.HRSS_TP_PAY_SHT_EXT } )) {
            sPayrollType = DCfdi33Catalogs.ClaveNominaExt;
            msPeriodicidadPago = DCfdi33Catalogs.ClavePeriodicidadPagoOtra;
        }
        
        nomina.getAttTipoNomina().setString(sPayrollType);
        nomina.getAttFechaPago().setDate(mtFechaPago);
        nomina.getAttFechaInicialPago().setDate(mtFechaInicialPago);
        nomina.getAttFechaFinalPago().setDate(mtFechaFinalPago);
        nomina.getAttNumDiasPagados().setDouble(mdNumDiasPagados == 0d ? DCfdi33Catalogs.DIAS_PAG_MIN : mdNumDiasPagados);
        
        // Emisor & Receptor:
        
        if (isTypeContractForEmployment()) {
            //emisor.getAttCurp().setString(...); // individuals as employeer not yet supported!
            //emisor.getAttRfcPatronOrigen().setString(...); // labor outsourcing not yet supported!
            
            if (!msNumSeguridadSocial.isEmpty()) {
                emisor.getAttRegistroPatronal().setString(DCfdVer3Utils.formatAttributeValueAsKey(msRegistroPatronal));

                // Receptor:

                receptor.getAttNumSeguridadSocial().setString(msNumSeguridadSocial);
                receptor.getAttFechaInicioRelLaboral().setDate(mtFechaInicioRelLaboral);
                receptor.getAttAntiguedad().setString("P" + (mnAntiguedad < 0 ? 0 : mnAntiguedad) + "W");
                receptor.getAttRiesgoPuesto().setString("" + mnRiesgoPuesto);
                receptor.getAttSalarioBaseCotApor().setDouble(mdSalarioBaseCotApor);
                receptor.getAttSalarioDiarioIntegrado().setDouble(mdSalarioDiarioIntegrado);
            }
        }
        
        // Complement Receptor:
        
        receptor.getAttCurp().setString(msEmpleadoCurp);
        receptor.getAttTipoContrato().setString(msTipoContrato);
        receptor.getAttSindicalizado().setString(msSindicalizado);
        
        if (Integer.parseInt(msTipoJornada) != SModSysConsts.HRSS_TP_WORK_DAY_NA) {
            receptor.getAttTipoJornada().setString(msTipoJornada);
        }
        
        receptor.getAttNumEmpleado().setString(DCfdVer3Utils.formatAttributeValueAsKey(msEmpleadoNum));
        receptor.getAttDepartamento().setString(SLibUtils.textToXml(msDepartamento));
        receptor.getAttPuesto().setString(SLibUtils.textToXml(msPuesto));
        receptor.getAttPeriodicidadPago().setString(msPeriodicidadPago);
        
        // Validate recruitment schema:
        
        if (isTypeContractForEmployment() && !isTypeRecruitmentSchemaForEmployment()) {
            throw new Exception("El régimen de contratación del empleado en el CFDI (clave: '" + msTipoRegimenCode + "') no corresponde a una relación laboral subordinada (clave tipo contrato: '" + msTipoContrato + "').");
        }
        
        if (!isTypeContractForEmployment() && isTypeRecruitmentSchemaForEmployment()) {
            throw new Exception("El régimen de contratación del empleado en el CFDI (clave: '" + msTipoRegimenCode + "') no corresponde a un esquema insubordinado (clave tipo contrato: '" + msTipoContrato + "').");
        }
        
        receptor.getAttTipoRegimen().setString(msTipoRegimenCode);
        
        // Validate length the account bank:
        
        if (!msCuentaBancaria.isEmpty()) {
            if (msCuentaBancaria.length() != SDataConstantsSys.BPSS_BPB_BANK_ACC_TEL && msCuentaBancaria.length() != SDataConstantsSys.BPSS_BPB_BANK_ACC_NUM &&
                    msCuentaBancaria.length() != SDataConstantsSys.BPSS_BPB_BANK_ACC_TRJ && msCuentaBancaria.length() != SDataConstantsSys.BPSS_BPB_BANK_ACC_CBE) {
                throw new Exception("La cuenta bancaria del empleado '" + msCuentaBancaria + "' tiene una longitud incorrecta: " + msCuentaBancaria.length() + ".");
            }

            if (msCuentaBancaria.length() != SDataConstantsSys.BPSS_BPB_BANK_ACC_CBE) {
                receptor.getAttBanco().setString((String) miClient.getSession().readField(SModConsts.HRSS_BANK, new int[] { mnBanco }, SDbRegistry.FIELD_CODE));
            }
            receptor.getAttCuentaBancaria().setString(msCuentaBancaria);
        }
        
        receptor.getAttClaveEntFed().setString(msClaveEstado);
        
        // Payments and discounts:
        
        boolean requireSubsidioEmpleo = mnTipoRegimen == SModSysConsts.HRSS_TP_REC_SCHE_WAG;
        boolean containsSubsidioEmpleo = false;
        
        for (SHrsFormerReceiptConcept concept : moChildConcepts) {
            if (concept.getTotalImporte() != 0) {
                switch (concept.getPkTipoConcepto()) {
                    case SCfdConsts.CFDI_PAYROLL_PERCEPTION:
                        switch (concept.getClaveOficial()) {
                            case SModSysConsts.HRSS_TP_EAR_TAX_SUB:
                                containsSubsidioEmpleo = true;
                                // falls through...
                            case SModSysConsts.HRSS_TP_EAR_OTH:
                                cfd.ver3.nom12.DElementOtroPago otroPago = createElementEarningOtherPayment(concept);
                                if (otroPago != null) {
                                    otrosPagos.getEltHijosOtroPago().add(otroPago);
                                }
                                break;

                            default:
                                cfd.ver3.nom12.DElementPercepcion percepcion = createElementEarning(concept);

                                switch (concept.getClaveOficial()) {
                                    case SModSysConsts.HRSS_TP_EAR_OVER_TIME:
                                        percepcion.getEltHijosHorasExtra().add(createElementEarningOverTime(concept));
                                        break;

                                    case SModSysConsts.HRSS_TP_EAR_DISAB:
                                        incapacidades.getEltHijosIncapacidad().add(createElementEarningDisability(concept));
                                        break;

                                    case SModSysConsts.HRSS_TP_EAR_SEN_BONUS:
                                    case SModSysConsts.HRSS_TP_EAR_SETT:
                                    case SModSysConsts.HRSS_TP_EAR_COMP:
                                        dTotalSeparacionIndemnizacionGravado = SLibUtils.roundAmount(dTotalSeparacionIndemnizacionGravado + percepcion.getAttImporteGravado().getDouble());
                                        dTotalSeparacionIndemnizacionExento = SLibUtils.roundAmount(dTotalSeparacionIndemnizacionExento + percepcion.getAttImporteExento().getDouble());
                                        break;

                                    default:
                                }

                                if (!SLibUtils.belongsTo(concept.getClaveOficial(), new int[] { SModSysConsts.HRSS_TP_EAR_SEN_BONUS, SModSysConsts.HRSS_TP_EAR_SETT, SModSysConsts.HRSS_TP_EAR_COMP })) { 
                                    dTotalSueldos = SLibUtils.roundAmount(dTotalSueldos + (percepcion.getAttImporteGravado().getDouble() + percepcion.getAttImporteExento().getDouble()));
                                }

                                dTotalGravado = SLibUtils.roundAmount(dTotalGravado + percepcion.getAttImporteGravado().getDouble());
                                dTotalExento = SLibUtils.roundAmount(dTotalExento + percepcion.getAttImporteExento().getDouble());

                                percepciones.getEltHijosPercepcion().add(percepcion);
                        }
                        break;

                    case SCfdConsts.CFDI_PAYROLL_DEDUCTION:
                        if (concept.getClaveOficial() == SModSysConsts.HRSS_TP_DED_TAX) {
                            dTotalImpuestosRetenidos = SLibUtils.roundAmount(dTotalImpuestosRetenidos + concept.getTotalImporte());
                        }
                        else {
                            dTotalOtrasDeducciones = SLibUtils.roundAmount(dTotalOtrasDeducciones + concept.getTotalImporte());

                            if (concept.getClaveOficial() == SModSysConsts.HRSS_TP_DED_DIS) {
                                incapacidades.getEltHijosIncapacidad().add(createElementEarningDisability(concept));
                            }
                        }
                        deducciones.getEltHijosDeduccion().add(createElementDeduction(concept));
                        break;

                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
            }
        }
        
        if (requireSubsidioEmpleo && !containsSubsidioEmpleo) {
            // Subsidio para el empleo is required, eventhough it is zero:
            
            cfd.ver3.nom12.DElementOtroPago otroPago = createElementOtroPago12(
                    (String) miClient.getSession().readField(SModConsts.HRSS_TP_OTH_PAY, new int[] { SModSysConsts.HRSS_TP_OTH_PAY_TAX_SUB }, SDbRegistry.FIELD_CODE), 
                    SHrsUtils.getEarning((SGuiClient) miClient, SModSysConsts.HRSS_TP_EAR_TAX_SUB).getCode(), 
                    SCfdConsts.CFDI_OTHER_PAY_TAX_SUBSIDY, 
                    0);
            
            otroPago.setEltSubsidioEmpleo(createElementSubsidioEmpleo12(0));
            
            otrosPagos.getEltHijosOtroPago().add(otroPago);
            
            nomina.getAttTotalOtrosPagos().setCanBeZero(true);
        }

        nomina.setEltEmisor(emisor);
        nomina.setEltReceptor(receptor);
        
        if (dTotalSeparacionIndemnizacionGravado > 0 || dTotalSeparacionIndemnizacionExento > 0) {
            percepciones.setEltSeparacionIndemnizacion(createElementEarningSeparation(dTotalSeparacionIndemnizacionGravado, dTotalSeparacionIndemnizacionExento));
            dTotalSeparacionIndemnizacion = dTotalSeparacionIndemnizacionGravado + dTotalSeparacionIndemnizacionExento;
        }
        
        if (!otrosPagos.getEltHijosOtroPago().isEmpty()) {
            nomina.getAttTotalOtrosPagos().setDouble(otrosPagos.getTotal());
            nomina.setEltOtrosPagos(otrosPagos);
        }
        
        if (!percepciones.getEltHijosPercepcion().isEmpty()) {
            percepciones.getAttTotalSueldos().setDouble(dTotalSueldos);
            percepciones.getAttTotalSeparacionIndemnizacion().setDouble(dTotalSeparacionIndemnizacion);
            percepciones.getAttTotalGravado().setDouble(dTotalGravado);
            percepciones.getAttTotalExento().setDouble(dTotalExento);
            
            nomina.getAttTotalPercepciones().setDouble(dTotalSueldos + dTotalSeparacionIndemnizacion);
            nomina.setEltPercepciones(percepciones);
        }

        if (!deducciones.getEltHijosDeduccion().isEmpty()) {
            deducciones.getAttTotalOtrasDeducciones().setDouble(dTotalOtrasDeducciones);
            deducciones.getAttTotalImpuestosRetenidos().setDouble(dTotalImpuestosRetenidos);
            
            nomina.getAttTotalDeducciones().setDouble(dTotalOtrasDeducciones + dTotalImpuestosRetenidos);
            nomina.setEltDeducciones(deducciones);
        }
        
        if (!incapacidades.getEltHijosIncapacidad().isEmpty()) {
            nomina.setEltIncapacidades(incapacidades);
        }

        return nomina;
    }

    /*
     * Implementation of SCfdXmlCfdi32 and SCfdXmlCfdi33:
     */

    @Override
    public int getCfdType() { // CFDI 3.2 & 3.3
        return SDataConstantsSys.TRNS_TP_CFD_PAYROLL;
    }

    @Override
    public String getComprobanteVersion() { // CFDI 3.3
        return "" + DCfdConsts.CFDI_VER_33;
    }

    @Override
    public String getComprobanteSerie() { // CFDI 3.2 & 3.3
        return msSerie;
    }

    @Override
    public String getComprobanteFolio() { // CFDI 3.2 & 3.3
        return "" + mnFolio;
    }

    @Override
    public Date getComprobanteFecha() { // CFDI 3.2 & 3.3
        Date datetime = new Date();
        long ellapsedMilliseconds = datetime.getTime() - SLibTimeUtils.convertToDateOnly(datetime).getTime();

        Date date = new Date();
        date.setTime(moParentPayroll.getFecha().getTime() + ellapsedMilliseconds - (1000 * 60 * 60 * 2)); // -2 hr to prevent error code 401 when stamping fiscal voucher

        return date;
    }

    @Override
    public int getComprobanteFormaDePagoPagos() { // CFDI 3.2
        return DAttributeOptionFormaDePago.CFD_UNA_EXHIBICION;
    }

    @Override
    public String getComprobanteFormaPago() { // CFDI 3.3
        return SDataConstantsSys.TRNS_CFD_CAT_PAY_WAY_99;
    }

    @Override
    public int getComprobanteCondicionesDePago() { // CFDI 3.2
        return DAttributeOptionCondicionesPago.CFD_CONTADO;
    }

    @Override
    public String getComprobanteCondicionesPago() { // CFDI 3.3
        return "";  // not required in payroll
    }

    @Override
    public double getComprobanteSubtotal() { // CFDI 3.2 & 3.3
        return mdTotalPercepciones;
    }

    @Override
    public double getComprobanteDescuento() { // CFDI 3.2 & 3.3
        return mdTotalDeducciones;
    }

    @Override
    public String getComprobanteMotivoDescuento() { // CFDI 3.2
        return "";
    }

    @Override
    public String getComprobanteMoneda() { // CFDI 3.2 & 3.3
        return msMoneda;
    }

    @Override
    public double getComprobanteTipoCambio() { // CFDI 3.2 & 3.3
        return 0;   // not required in payroll
    }

    @Override
    public double getComprobanteTotal() { // CFDI 3.2 & 3.3
        return mdTotalNeto;
    }

    @Override
    public int getComprobanteTipoDeComprobante() {  // CFDI 3.2
        return DAttributeOptionTipoDeComprobante.CFD_NOMINA;
    }

    @Override
    public String getComprobanteTipoComprobante() { // CFDI 3.3
        return DCfdi33Catalogs.CFD_TP_N;
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getComprobanteMetodoDePago() { // CFDI 3.2
        String formaPago = "";

        try {
            formaPago = SHrsFormerUtils.getPaymentMethodName(miClient, mnMetodoPago);
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }

        return formaPago;
    }

    @Override
    public String getComprobanteMetodoPago() { // CFDI 3.3
        return SDataConstantsSys.TRNS_CFD_CAT_PAY_MET_PUE;
    }

    @Override
    public String getComprobanteNumCtaPago() { // CFDI 3.2
        return "";
    }

    @Override
    public String getComprobanteLugarExpedicion() { // CFDI 3.3
        return msLugarExpedicion;
    }

    @Override
    public String getComprobanteConfirmacion() { // CFDI 3.3
        return msConfirmacion;
    }

    @Override
    public String getCfdiRelacionadosTipoRelacion() { // CFDI 3.3
        return msCfdiRelacionadosTipoRelacion;
    }
    
    @Override
    public ArrayList<String> getCfdiRelacionados() { // CFDI 3.3
        return maCfdiRelacionados;
    }

    @Override
    public int getEmisorId() { // CFDI 3.2 & 3.3
        return moParentPayroll.getEmpresaId();
    }

    @Override
    public int getEmisorSucursalId() { // CFDI 3.2 & 3.3
        return moParentPayroll.getSucursalEmpresaId();
    }

    @Override
    public ArrayList<DElement> getElementsEmisorRegimenFiscal() { // CFDI 3.2
        ArrayList<DElement> regimes = new ArrayList<>();

        for (int i = 0; i < moParentPayroll.getRegimenFiscal().length; i++) {
            DElement regimen = new cfd.ver32.DElementRegimenFiscal();

            ((cfd.ver32.DElementRegimenFiscal) regimen).getAttRegimen().setString(moParentPayroll.getRegimenFiscal()[i]);
            regimes.add(regimen);
        }

        return regimes;
    }

    @Override
    public String getEmisorRegimenFiscal() { // CFDI 3.3
        return msRegimenFiscal;
    }

    @Override
    public int getReceptorId() { // CFDI 3.2 & 3.3
        return mnPkEmpleadoId;
    }

    @Override
    public int getReceptorSucursalId() { // CFDI 3.2 & 3.3
        return mnPkSucursalEmpleadoId;
    }

    @Override
    public String getReceptorResidenciaFiscal() { // CFDI 3.3
        return "";  // not required in payroll
    }

    @Override
    public String getReceptorNumRegIdTrib() { // CFDI 3.3
        return "";  // not required in payroll
    }

    @Override
    public String getReceptorUsoCFDI() { // CFDI 3.3
        return SDataConstantsSys.TRNS_CFD_CAT_CFD_USE_P01;
    }

    @Override
    public int getDestinatarioId() { // CFDI 3.2 & 3.3
        return SLibConstants.UNDEFINED;
    }

    @Override
    public int getDestinatarioSucursalId() { // CFDI 3.2 & 3.3
        return SLibConstants.UNDEFINED;
    }

    @Override
    public int getDestinatarioDomicilioId() { // CFDI 3.2 & 3.3
        return SLibConstants.UNDEFINED;
    }
    
    @Override
    public ArrayList<SCfdDataConcepto> getElementsConcepto() { // CFDI 3.2 & 3.3
        SCfdDataConcepto concepto = new SCfdDataConcepto(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
        concepto.setClaveProdServ(DCfdi33Catalogs.ClaveProdServServsSueldosSalarios);
        concepto.setNoIdentificacion("");
        concepto.setCantidad(1);
        concepto.setClaveUnidad(DCfdi33Catalogs.ClaveUnidadAct);
        concepto.setUnidad("");
        concepto.setDescripcion(DCfdi33Catalogs.ConceptoSueldosSalarios);
        concepto.setValorUnitario(mdTotalPercepciones);
        concepto.setImporte(mdTotalPercepciones);
        concepto.setDescuento(mdTotalDeducciones);

        ArrayList<SCfdDataConcepto> conceptos = new ArrayList<>();
        conceptos.add(concepto);

        return conceptos;
    }

    @Override
    public ArrayList<SCfdDataImpuesto> getElementsImpuestos(float xmlVersion) { // CFDI 3.2 & 3.3
        return null;
    }
    
    @Override
    public DElement getElementComplemento() throws Exception { // CFDI 3.2 & 3.3
        DElement complemento = new cfd.ver33.DElementComplemento();

        //((cfd.ver33.DElementComplemento) complemento).getElements().add(createCfdiElementNomina11()); // vigente hasta 31/03/2017
        ((cfd.ver33.DElementComplemento) complemento).getElements().add(createCfdiElementNomina12());   // vigente a partir de 01/04/2017

        return complemento;
    }

    @Override
    public DElement getElementAddenda() { // CFDI 3.2 & 3.3
        return null;
    }
}
