/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.print;

import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver32.DElementComprobante;
import cfd.ver32.DElementTimbreFiscalDigital;
import cfd.ver3.nom11.DElementNomina;
import erp.cfd.SCfdConsts;
import erp.cfd.SDialogResult;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mcfg.data.SDataCurrency;
import erp.mhrs.data.SDataFormerPayroll;
import erp.mhrs.data.SDataFormerPayrollEmp;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mtrn.data.SDataCfd;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;

/**
 *
 * @author JBarajas
 */
public class PrintCfdiThread extends Thread {

    protected SClientInterface miClient;
    protected int mnCfdId;
    protected int mnPrintMode;
    protected int mnNumCopies;
    protected int mnSubtypeCfd;
    
    protected SDialogResult moDialogResult;
    
    public PrintCfdiThread(final SClientInterface client, final int cfdId, final int pnPrintMode, final int pnNumCopies, final int pnSubtypeCfd, final SDialogResult dialogResult) {
        miClient = client;
        mnCfdId = cfdId;
        mnPrintMode = pnPrintMode;
        mnNumCopies = pnNumCopies;
        mnSubtypeCfd = pnSubtypeCfd;
        moDialogResult = dialogResult;
    }

    public void startThread() {
        setDaemon(true);
        super.start();
    }

    public void stopThread() {
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void run() {
        try {
            printPayrollReceipt();
        }
        catch (Exception e) {
            SLibUtils.printException(this, e);
        }
    }
    
    public void printPayrollReceipt() throws java.lang.Exception {
        int i = 0;
        int overTime = 0;
        int nTotalTiempoExtra = 0;
        int nPayrollPeriodYear = 0;
        int nPayrollPeriod = 0;
        int nPayrollNumber = 0;
        String sAuxPaymentType = "";

        double dTotalPercepciones = 0;
        double dTotalDeducciones = 0;
        double dTotalTiempoExtraPagado = 0;
        double dTotalIncapacidades = 0;
        double dTotalIsr = 0;

        String sPdfFileName = "";
        String sSql = "";
        ResultSet resultSet = null;
        
        SDataFormerPayroll oFormerPayroll = null;
        SDataFormerPayrollEmp oFormerPayrollEmployee = null;
        SDbPayrollReceipt oPayrollReceipt = null;
        SDataCfd cfd = null;
        SDataCurrency cur = new SDataCurrency();

        cur = miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency();
        
        DElementComprobante comprobante = null;
        HashMap<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        ArrayList aPercepciones = null;
        ArrayList aDeducciones = null;
        ArrayList aTiempoExtra = null;
        ArrayList aIncapacidades = null;

        DecimalFormat oFixedFormat = new DecimalFormat(SLibUtils.textRepeat("0", 3));
        DecimalFormat oFixedFormatAux = new DecimalFormat(SLibUtils.textRepeat("0", 2));
        
        cfd = new SDataCfd();
        
        if (cfd.read(new int[] { mnCfdId }, moDialogResult.getSession().getStatement().getConnection().createStatement())!= SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }

        System.out.println("Inciando hilo de CFD: " + cfd.getDocXmlName());
        
        map = miClient.createReportParams();

        SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        switch (mnSubtypeCfd) {
            case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                oFormerPayroll = new SDataFormerPayroll();
                oFormerPayroll.read(new int[] { cfd.getFkPayrollPayrollId_n() }, miClient.getSession().getStatement().getConnection().createStatement());
                
                oFormerPayrollEmployee = new SDataFormerPayrollEmp();
                oFormerPayrollEmployee.read(new int[] { cfd.getFkPayrollPayrollId_n(), cfd.getFkPayrollEmployeeId_n() }, miClient.getSession().getStatement().getConnection().createStatement());
                
                sSql = "SELECT id_pay, pay_note AS f_pay_nts FROM hrs_sie_pay WHERE id_pay = " + oFormerPayroll.getPkPayrollId();
                break;
            case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                sSql = "SELECT p.*, t.name "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS t ON "
                + "p.fk_tp_pay = t.id_tp_pay "
                + "WHERE id_pay = " + cfd.getFkPayrollReceiptPayrollId_n() + " ";
                
                resultSet = moDialogResult.getSession().getDatabase().getConnection().createStatement().executeQuery(sSql);
                if (!resultSet.next()) {
                    throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else {
                    nPayrollPeriodYear = resultSet.getInt("p.per_year");
                    nPayrollPeriod = resultSet.getInt("p.per");
                    nPayrollNumber = resultSet.getInt("p.num");
                    sAuxPaymentType = resultSet.getString("t.name");
                }
                
                oPayrollReceipt = new SDbPayrollReceipt();
                oPayrollReceipt.read(moDialogResult.getSession(), new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n() });
                
                sSql = "SELECT id_pay, nts AS f_pay_nts FROM hrs_pay WHERE id_pay = " + cfd.getFkPayrollReceiptPayrollId_n();
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        comprobante = DCfdUtils.getCfdi32(cfd.getDocXml());
        
        map.put("sSql", sSql);
        map.put("sCfdFecha", SLibUtils.DbmsDateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        map.put("sCfdFormaDePago", comprobante.getAttFormaDePago().getOption());
        map.put("sCfdNoCuentaPago", comprobante.getAttNumCtaPago().getString());
        map.put("sCfdCondicionesDePagoOpc", comprobante.getAttCondicionesDePago().getOption());
        map.put("dCfdSubtotal", comprobante.getAttSubTotal().getDouble());
        map.put("dCfdDescuento", comprobante.getAttDescuento().getDouble());
        map.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        map.put("sCfdMetodoDePagoOpc", comprobante.getAttMetodoDePago().getString());
        map.put("sExpedidoEn", comprobante.getAttLugarExpedicion().getString());
        map.put("sCfdTipoComprobante", comprobante.getAttTipoDeComprobante().getOption());
        map.put("sCfdNoCuentaPago", comprobante.getAttNumCtaPago().getString());
        map.put("sCfdNoCertificado", comprobante.getAttNoCertificado().getString());
        map.put("sEmiRegimenFiscal", comprobante.getEltEmisor().getEltHijosRegimenFiscal().get(0).getAttRegimen().getString());
        map.put("sEmiRfc", comprobante.getEltEmisor().getAttRfc().getString());
        map.put("sRecRfc", comprobante.getEltReceptor().getAttRfc().getString());
        map.put("dCfdTotal", comprobante.getAttTotal().getDouble());
        map.put("sDocTotalConLetra", SLibUtilities.translateValueToText(comprobante.getAttTotal().getDouble(), miClient.getSessionXXX().getParamsErp().getDecimalsValue(), miClient.getSessionXXX().getParamsErp().getFkCurrencyId(),
                cur.getTextSingular(), cur.getTextPlural(), cur.getTextPrefix(), cur.getTextSuffix()));
        map.put("sCfdMoneda", comprobante.getAttMoneda().getString());
        map.put("dCfdTipoCambio", comprobante.getAttTipoCambio().getDouble());
        map.put("ReceptorNombre", comprobante.getEltReceptor().getAttNombre().getString());
        map.put("bIsAnnulled", cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED);
        map.put("nPkPayrollId", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayroll.getPkPayrollId() : cfd.getFkPayrollReceiptPayrollId_n());
        map.put("NominaNumTipo", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (oFormerPayroll.getNumber() + " " + oFormerPayroll.getType()) : (nPayrollNumber + " " + sAuxPaymentType));
        map.put("NominaFolio", comprobante.getAttSerie().getString() + "-" + comprobante.getAttFolio().getString());
        map.put("sXmlBaseDir", miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory());

        map.put("dCfdConceptoCantidad", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttCantidad().getDouble());
        map.put("sCfdConceptoUnidad", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttUnidad().getString());
        map.put("sCfdConceptoNoIdentifiacion", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttNoIdentificacion().getString());
        map.put("sCfdConceptoDescripcion", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttDescripcion().getString());
        map.put("dCfdConceptoValorUnitario", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttValorUnitario().getDouble());
        map.put("dCfdConceptoImporte", comprobante.getEltConceptos().getEltHijosConcepto().get(0).getAttImporte().getDouble());

        for (DElement element : comprobante.getEltOpcComplemento().getElements()) {

            if (element.getName().compareTo("nomina:Nomina") == 0) {

                map.put("RegistroPatronal", ((DElementNomina) element).getAttRegistroPatronal().getString());
                map.put("NumEmpleado", ((DElementNomina) element).getAttNumEmpleado().getString());
                map.put("CURP", ((DElementNomina) element).getAttCurp().getString());
                map.put("TipoRegimen", SCfdConsts.RegimenMap.get(((DElementNomina) element).getAttTipoRegimen().getInteger()));
                map.put("NumSeguridadSocial", ((DElementNomina) element).getAttNumSeguridadSocial().getString());
                map.put("FechaPago", oSimpleDateFormat.format(((DElementNomina) element).getAttFechaPago().getDate()));
                map.put("FechaInicialPago", oSimpleDateFormat.format(((DElementNomina) element).getAttFechaInicialPago().getDate()));
                map.put("FechaFinalPago", oSimpleDateFormat.format(((DElementNomina) element).getAttFechaFinalPago().getDate()));
                map.put("NumDiasNoLaborados", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getDaysNotWorked() : oPayrollReceipt.getDaysNotWorked_r());
                map.put("NumDiasLaborados", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getDaysWorked() : oPayrollReceipt.getDaysWorked()); // XXX Optional
                map.put("NumDiasPagar", 0d); // Calculate?, navalos (2014-03-13)
                map.put("NumDiasPagados", ((DElementNomina) element).getAttNumDiasPagados().getDouble());
                map.put("Departamento", ((DElementNomina) element).getAttDepartamento().getString());
                map.put("CLABE", ((DElementNomina) element).getAttClabe().getString());
                map.put("Banco", SCfdConsts.BancoMap.get(((DElementNomina) element).getAttBanco().getInteger()));
                map.put("FechaInicioRelLaboral", oSimpleDateFormat.format(((DElementNomina) element).getAttFechaInicioRelLaboral().getDate()));
                map.put("Antiguedad", ((DElementNomina) element).getAttAntiguedad().getInteger());
                map.put("Puesto", ((DElementNomina) element).getAttPuesto().getString());
                map.put("TipoContrato", ((DElementNomina) element).getAttTipoContrato().getString());
                map.put("TipoJornada", ((DElementNomina) element).getAttTipoJornada().getString());
                map.put("PeriodicidadPago", ((DElementNomina) element).getAttPeriodicidadPago().getString());
                map.put("RiesgoPuesto", SCfdConsts.RiesgoMap.get(((DElementNomina) element).getAttRiesgoPuesto().getInteger()));
                map.put("TipoPago", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? SModSysConsts.HRSS_TP_PAY_FOR : oPayrollReceipt.getFkPaymentTypeId());
                map.put("Sueldo", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalary() : oPayrollReceipt.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ? oPayrollReceipt.getSalary() : oPayrollReceipt.getWage());
                map.put("SalarioBaseCotApor", ((DElementNomina) element).getAttSalarioBaseCotApor().getDouble());
                map.put("SalarioDiarioIntegrado", ((DElementNomina) element).getAttSalarioDiarioIntegrado().getDouble());

                map.put("TipoEmpleado", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getEmployeeType() : miClient.getSession().readField(SModConsts.HRSU_TP_EMP, new int[] { oPayrollReceipt.getFkEmployeeTypeId() }, SDbRegistry.FIELD_CODE));
                map.put("Categoria", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getEmployeeCategory() : miClient.getSession().readField(SModConsts.HRSU_TP_WRK, new int[] { oPayrollReceipt.getFkWorkerTypeId() }, SDbRegistry.FIELD_CODE));
                map.put("TipoSalario", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalaryType() : miClient.getSession().readField(SModConsts.HRSS_TP_SAL, new int[] { oPayrollReceipt.getFkSalaryTypeId() }, SDbRegistry.FIELD_NAME));
                map.put("Ejercicio", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (oFormerPayroll.getYear() + "-" + oFixedFormatAux.format(oFormerPayroll.getPeriod())) : (nPayrollPeriodYear + "-" + oFixedFormatAux.format(nPayrollPeriod)));

                aPercepciones = new ArrayList();
                aDeducciones = new ArrayList();
                aTiempoExtra = new ArrayList();
                aIncapacidades = new ArrayList();

                dTotalPercepciones = 0;
                dTotalDeducciones = 0;
                nTotalTiempoExtra = 0;
                dTotalTiempoExtraPagado = 0;
                dTotalIncapacidades = 0;
                dTotalIsr = 0;

                // Perceptions:

                i = 0;
                if (((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones() != null) {
                    for (i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().size(); i++) {
                        aPercepciones.add(oFixedFormat.format(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getInteger()));
                        aPercepciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString());
                        aPercepciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttConcepto().getString());
                        aPercepciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble());
                        aPercepciones.add(null); // pending to be used, navalos (2014-03-13)

                        dTotalPercepciones += ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom11.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble();
                    }
                }

                for (int j = i; j < 10; j++) {
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                }

                // ExtraTimes:

                i = 0;
                if (((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras() != null) {
                    for (i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().size(); i++) {

                        aTiempoExtra.add(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE) == 0 ?
                            SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                        aTiempoExtra.add(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttDias().getInteger());
                        aTiempoExtra.add(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttHorasExtra().getInteger());
                        aTiempoExtra.add(((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttImportePagado().getDouble());

                        nTotalTiempoExtra += ((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttHorasExtra().getInteger();
                        dTotalTiempoExtraPagado += ((cfd.ver3.nom11.DElementNomina) element).getEltHorasExtras().getEltHijosHorasExtra().get(i).getAttImportePagado().getDouble();
                    }
                }

                for (int j = i; j < 5; j++) {
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                }

                // Deductions:

                i = 0;
                if (((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones() != null) {
                    for (i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().size(); i++) {

                        aDeducciones.add(oFixedFormat.format(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger()));
                        aDeducciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString());
                        aDeducciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttConcepto().getString());
                        aDeducciones.add(((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble());
                        aDeducciones.add(null); // pending to be used, navalos (2014-03-13)

                        // Obtain isr tax

                        if (((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_PER_ISR) == 0 &&
                            ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getInteger() == SCfdConsts.DED_ISR) {
                            dTotalIsr += ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble();
                        }

                        dTotalDeducciones += ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom11.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporteExento().getDouble();
                    }
                }

                for (int j = i; j < 10; j++) {
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                }

                // Incapacities:

                i = 0;
                if (((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades() != null) {
                    for (i = 0; i < ((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().size(); i++) {

                        aIncapacidades.add(oFixedFormat.format(((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttTipoIncapacidad().getInteger()));
                        aIncapacidades.add(((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttDiasIncapacidad().getDouble());
                        aIncapacidades.add(((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttDescuento().getDouble());

                        dTotalIncapacidades += ((cfd.ver3.nom11.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttDescuento().getDouble();
                    }
                }

                for (int j = i; j < 5; j++) {
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                }

                map.put("oPerceptions", aPercepciones);
                map.put("oDeductions", aDeducciones);
                map.put("oExtratimes", aTiempoExtra);
                map.put("oIncapacities", aIncapacidades);
                map.put("TotalPercepcionesGravado", dTotalPercepciones);
                map.put("TotalPercepcionesExento", null);
                map.put("TotalDeduccionesGravado", dTotalDeducciones);
                map.put("TotalDeduccionesExento", null);
                map.put("TotalTiempoExtra", nTotalTiempoExtra);
                map.put("TotalTiempoExtraPagado", dTotalTiempoExtraPagado);
                map.put("TotalIncapacidades", dTotalIncapacidades);
                map.put("dCfdTotalIsr", dTotalIsr);
            }
            else if (element.getName().compareTo("nomina12:Nomina") == 0) {

                map.put("TipoNomina", ((cfd.ver3.nom12.DElementNomina) element).getAttTipoNomina().getString());
                map.put("FechaPago", oSimpleDateFormat.format(((cfd.ver3.nom12.DElementNomina) element).getAttFechaPago().getDate()));
                map.put("FechaInicialPago", oSimpleDateFormat.format(((cfd.ver3.nom12.DElementNomina) element).getAttFechaInicialPago().getDate()));
                map.put("FechaFinalPago", oSimpleDateFormat.format(((cfd.ver3.nom12.DElementNomina) element).getAttFechaFinalPago().getDate()));
                map.put("NumDiasPagados", ((cfd.ver3.nom12.DElementNomina) element).getAttNumDiasPagados().getDouble());
                map.put("NumDiasNoLaborados", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getDaysNotWorked() : oPayrollReceipt.getDaysNotWorked_r());
                map.put("NumDiasLaborados", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getDaysWorked() : oPayrollReceipt.getDaysWorked()); // XXX Optional
                map.put("NumDiasPagar", 0d); // Calculate?, navalos (2014-03-13)
                
                // Emisor:

                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltEmisor()!= null) {
                    map.put("RegistroPatronal", ((cfd.ver3.nom12.DElementNomina) element).getEltEmisor().getAttRegistroPatronal().getString());
                }

                // Receptor:

                String antigüedad = "";
                
                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltReceptor() != null) {
                    map.put("CURP", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttCurp().getString());
                    map.put("NumSeguridadSocial", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttNumSeguridadSocial().getString());
                    
                    if (((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttFechaInicioRelLaboral().getDate() != null) {
                        map.put("FechaInicioRelLaboral", oSimpleDateFormat.format(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttFechaInicioRelLaboral().getDate()));
                    }
                    
                    if (!((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttAntiguedad().getString().isEmpty()) {
                        antigüedad = ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttAntiguedad().getString();
                        antigüedad = antigüedad.substring(1, antigüedad.length() - 1);
                    }
                    
                    map.put("Antiguedad", SLibUtils.parseInt(antigüedad));
                    map.put("TipoContrato", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttTipoContrato().getString());
                    // Sindicalizado 
                    map.put("TipoJornada", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttTipoJornada().getString());
                    map.put("TipoRegimen", SCfdConsts.RegimenMap.get(SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttTipoRegimen().getString())));
                    map.put("NumEmpleado", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttNumEmpleado().getString());
                    map.put("Departamento", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttDepartamento().getString());
                    map.put("Puesto", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttPuesto().getString());
                    map.put("RiesgoPuesto", SCfdConsts.RiesgoMap.get(SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttRiesgoPuesto().getString())));
                    map.put("PeriodicidadPago", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttPeriodicidadPago().getString());
                    map.put("Banco", SCfdConsts.BancoMap.get(SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttBanco().getString())));
                    map.put("CLABE", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttCuentaBancaria().getString());
                    map.put("SalarioBaseCotApor", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttSalarioBaseCotApor().getDouble());
                    map.put("SalarioDiarioIntegrado", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttSalarioDiarioIntegrado().getDouble());
                    map.put("ClaveEstado", ((cfd.ver3.nom12.DElementNomina) element).getEltReceptor().getAttClaveEntFed().getString());
                    map.put("TipoPago", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? SModSysConsts.HRSS_TP_PAY_FOR : oPayrollReceipt.getFkPaymentTypeId());
                    map.put("Sueldo", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalary() : oPayrollReceipt.getFkPaymentTypeId() == SModSysConsts.HRSS_TP_PAY_WEE ? oPayrollReceipt.getSalary() : oPayrollReceipt.getWage());
                    map.put("TipoEmpleado", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getEmployeeType() : miClient.getSession().readField(SModConsts.HRSU_TP_EMP, new int[] { oPayrollReceipt.getFkEmployeeTypeId() }, SDbRegistry.FIELD_CODE));
                    map.put("Categoria", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getEmployeeCategory() : miClient.getSession().readField(SModConsts.HRSU_TP_WRK, new int[] { oPayrollReceipt.getFkWorkerTypeId() }, SDbRegistry.FIELD_CODE));
                    map.put("TipoSalario", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? oFormerPayrollEmployee.getSalaryType() : miClient.getSession().readField(SModConsts.HRSS_TP_SAL, new int[] { oPayrollReceipt.getFkSalaryTypeId() }, SDbRegistry.FIELD_NAME));
                    map.put("Ejercicio", mnSubtypeCfd == SCfdConsts.CFDI_PAYROLL_VER_OLD ? (oFormerPayroll.getYear() + "-" + oFixedFormatAux.format(oFormerPayroll.getPeriod())) : (nPayrollPeriodYear + "-" + oFixedFormatAux.format(nPayrollPeriod)));
                }
                
                aPercepciones = new ArrayList();
                aDeducciones = new ArrayList();
                aTiempoExtra = new ArrayList();
                aIncapacidades = new ArrayList();

                dTotalPercepciones = 0;
                dTotalDeducciones = 0;
                nTotalTiempoExtra = 0;
                dTotalTiempoExtraPagado = 0;
                dTotalIncapacidades = 0;
                dTotalIsr = 0;

                // Perceptions:

                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones() != null) {
                    for (i = 0; i < ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().size(); i++) {
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttTipoPercepcion().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttClave().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttConcepto().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble());
                        aPercepciones.add(null); // pending to be used, navalos (2014-03-13)

                        dTotalPercepciones += ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteGravado().getDouble() +
                                ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getAttImporteExento().getDouble();
                    
                        // ExtraTimes:

                        overTime = 0;
                        for (; overTime < ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().size(); overTime++) {
                            aTiempoExtra.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttTipoHoras().getString().compareTo(SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE_COD) == 0 ?
                            SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                            aTiempoExtra.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttDias().getInteger());
                            aTiempoExtra.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttHorasExtra().getInteger());
                            aTiempoExtra.add(((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttImportePagado().getDouble());

                            nTotalTiempoExtra += ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttHorasExtra().getInteger();
                            dTotalTiempoExtraPagado += ((cfd.ver3.nom12.DElementNomina) element).getEltPercepciones().getEltHijosPercepcion().get(i).getEltHijosHorasExtra().get(overTime).getAttImportePagado().getDouble();
                        }
                    }
                }

                // Other payment:
                
                if (((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos() != null) {
                    for (i = 0; i < ((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().size(); i++) {
                        aPercepciones.add(""); // is blank because it is not earning
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().get(i).getAttClave().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().get(i).getAttConcepto().getString());
                        aPercepciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltOtrosPagos().getEltHijosOtroPago().get(i).getAttImporte().getDouble());
                        aPercepciones.add(null); // pending to be used, navalos (2014-03-13)
                    }
                }
                
                for (int j = i; j < 10; j++) {
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                    aPercepciones.add(null);
                }

                for (int j = overTime; j < 5; j++) {
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                    aTiempoExtra.add(null);
                }

                // Deductions:

                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones() != null) {
                    for (i = 0; i < ((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().size(); i++) {

                        aDeducciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getString());
                        aDeducciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString());
                        aDeducciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttConcepto().getString());
                        aDeducciones.add(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporte().getDouble());
                        aDeducciones.add(null); // pending to be used, navalos (2014-03-13)

                        // Obtain isr tax

                        if (((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttClave().getString().compareTo(SCfdConsts.PAYROLL_PER_ISR) == 0 &&
                            SLibUtils.parseInt(((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttTipoDeduccion().getString()) == SCfdConsts.DED_ISR) {
                            dTotalIsr += ((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporte().getDouble();
                        }

                        dTotalDeducciones += ((cfd.ver3.nom12.DElementNomina) element).getEltDeducciones().getEltHijosDeduccion().get(i).getAttImporte().getDouble();
                    }
                }

                for (int j = i; j < 10; j++) {
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                    aDeducciones.add(null);
                }

                // Incapacities:

                i = 0;
                if (((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades() != null) {
                    for (i = 0; i < ((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().size(); i++) {

                        aIncapacidades.add(oFixedFormat.format(((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttTipoIncapacidad().getString()));
                        aIncapacidades.add(((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttDiasIncapacidad().getInteger());
                        aIncapacidades.add(((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttImporteMonetario().getDouble());

                        dTotalIncapacidades += ((cfd.ver3.nom12.DElementNomina) element).getEltIncapacidades().getEltHijosIncapacidad().get(i).getAttImporteMonetario().getDouble();
                    }
                }

                for (int j = i; j < 5; j++) {
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                    aIncapacidades.add(null);
                }

                map.put("oPerceptions", aPercepciones);
                map.put("oDeductions", aDeducciones);
                map.put("oExtratimes", aTiempoExtra);
                map.put("oIncapacities", aIncapacidades);
                map.put("TotalPercepcionesGravado", dTotalPercepciones);
                map.put("TotalPercepcionesExento", null);
                map.put("TotalDeduccionesGravado", dTotalDeducciones);
                map.put("TotalDeduccionesExento", null);
                map.put("TotalTiempoExtra", nTotalTiempoExtra);
                map.put("TotalTiempoExtraPagado", dTotalTiempoExtraPagado);
                map.put("TotalIncapacidades", dTotalIncapacidades);
                map.put("dCfdTotalIsr", dTotalIsr);
            }
            else if (element.getName().compareTo("tfd:TimbreFiscalDigital") == 0) {

                map.put("sCfdiVersion", ((DElementTimbreFiscalDigital) element).getAttVersion().getString());
                map.put("sCfdiUuid", ((DElementTimbreFiscalDigital) element).getAttUuid().getString());
                map.put("sCfdiSelloCFD", ((DElementTimbreFiscalDigital) element).getAttSelloCfd().getString());
                map.put("sCfdiSelloSAT", ((DElementTimbreFiscalDigital) element).getAttSelloSAT().getString());
                map.put("sCfdiNoCertificadoSAT", ((DElementTimbreFiscalDigital) element).getAttNoCertificadoSAT().getString());
                map.put("sCfdiFechaTimbre", ((DElementTimbreFiscalDigital) element).getAttFechaTimbrado().getString());
            }
        }

        jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_CFDI_PAYROLL, map);
        sPdfFileName = cfd.getDocXmlName().substring(0, cfd.getDocXmlName().lastIndexOf(".xml"));
        sPdfFileName = miClient.getSessionXXX().getParamsCompany().getXmlBaseDirectory() + sPdfFileName + ".pdf";
        
        switch (mnPrintMode) {
            case SDataConstantsPrint.PRINT_MODE_VIEWER:
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Comprobante Fiscal Digital por Internet");
                jasperViewer.setVisible(true);
                break;
            case SDataConstantsPrint.PRINT_MODE_PDF:
                JasperExportManager.exportReportToPdfFile(jasperPrint, sPdfFileName);
                break;
            case SDataConstantsPrint.PRINT_MODE_STREAM:
                for (int j = 0; j < mnNumCopies; j++) {
                    JasperPrintManager.printReport(jasperPrint, false);                    
                }
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
        System.out.println("Terminando hilo de CFD: " + cfd.getDocXmlName());
        System.gc();
    }
}
