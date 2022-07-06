/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibUtilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 * 
 * Maintenance Log:
 * 2018-01-02, Sergio Flores:
 *  Implementation of payroll into CFDI 3.3.
 */
@Deprecated
public abstract class SHrsFormerUtils {

    /**
     * Read payroll from database of former system (SIIE 1.0, a.k.a., Magic)
     * @param client
     * @param statementClient
     * @param payrollId
     * @param nPkCompanyId
     * @param tPayrollDate
     * @param tPayrollDatePayment
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    @Deprecated
    public static SHrsFormerPayroll readHrsFormerPayroll(final SClientInterface client, final Statement statementClient, final int payrollId, final int nPkCompanyId, final java.util.Date tPayrollDate, final java.util.Date tPayrollDatePayment) throws SQLException, Exception {
        int f_emp_map_bp = 0;
        int f_emp_id = 0;
        int claveEmpresa = 0;
        String sql = "";
        String cia_reg_imss = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Connection connectionOdbc = getConnectionOdbc(client);
        Connection connectionOdbcAux = getConnectionOdbc(client);

        SHrsFormerPayroll hrsFormerPayroll = null;
        SHrsFormerReceipt hrsFormerReceipt = null;
        SHrsFormerReceiptConcept hrsFormerReceiptConcept = null;
        SHrsFormerConceptExtraTime hrsFormerConceptExtraTime = null;

        Statement statement = connectionOdbc.createStatement();
        Statement statementAux = connectionOdbcAux.createStatement();

        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        ResultSet resultSetClient = null;

        // Obtain payroll header:

        sql = "SELECT " +
            "n.id_nomina,  " +
            "GETDATE() AS f_date, " +
            "n.fecha_ini,  " +
            "n.fecha_fin,  " +
            "(SELECT COALESCE(sum(m.monto), 0)  " +
            "FROM nom_nominas_emp_movs_p AS m  " +
            "WHERE m.id_nomina = n.id_nomina) AS f_per,  " +
            "(SELECT COALESCE(sum(m.monto), 0)  " +
            "FROM nom_nominas_emp_movs_d AS m  " +
            "WHERE m.id_nomina = n.id_nomina) AS f_ded, " +
            "(SELECT COALESCE(sum(m.monto), 0)  " +
            "FROM nom_nominas_emp_movs_d AS m  " +
            "WHERE m.id_nomina = n.id_nomina AND m.fid_deduccion = " + SHrsFormerConsts.FPS_DED_INC_TAX + ") AS f_rent_ret, " +
            "'SUELDOS Y SALARIOS ' AS f_descrip,  " +
            "pt.id_tipo  " +
            "FROM nom_nominas AS n  " +
            "INNER JOIN nomt_periodo_pago AS pt ON n.fid_periodo_pago_tp = pt.id_tipo  " +
            "WHERE id_nomina = " + payrollId + " " +
            "ORDER BY n.ejercicio DESC, n.periodo DESC, pt.id_tipo, n.nomina_num, n.id_nomina; ";
        resultSet = statement.executeQuery(sql);

        if (!resultSet.next()) {
            throw new Exception("No se encontró la nómina.");
        }
        else {

            // Obtain company parameters (id_co, id_bpb, fiscal_settings):

            sql = "SELECT p.id_co, bpb.id_bpb, p.fiscal_settings " +
                "FROM cfg_param_co AS p " +
                "INNER JOIN erp.bpsu_bp AS b ON " +
                "p.id_co = b.id_bp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                "b.id_bp = bpb.fid_bp " +
                "WHERE p.b_del = 0 AND p.id_co = " + nPkCompanyId + " AND b.b_co = 1 ";
            resultSetClient = statementClient.executeQuery(sql);

            if (!resultSetClient.next()) {
                throw new Exception("No se encontró la configuración de la empresa.");
            }

            hrsFormerPayroll = new SHrsFormerPayroll(client);

            hrsFormerPayroll.setPkNominaId(resultSet.getInt("id_nomina"));
            hrsFormerPayroll.setFecha(tPayrollDate);
            hrsFormerPayroll.setFechaInicial(dateFormat.parse(SLibUtilities.textTrim(resultSet.getString("fecha_ini"))));
            hrsFormerPayroll.setFechaFinal(dateFormat.parse(SLibUtilities.textTrim(resultSet.getString("fecha_fin"))));
            hrsFormerPayroll.setTotalPercepciones(resultSet.getDouble("f_per"));
            hrsFormerPayroll.setTotalDeducciones(resultSet.getDouble("f_ded"));
            hrsFormerPayroll.setTotalRetenciones(resultSet.getDouble("f_rent_ret"));
            //hrsPayroll.setDescripcion(SLibUtilities.textLeft(SLibUtilities.textTrim(resultSet.getString("f_descrip")), 100)); // 100 = pay_note column width
            hrsFormerPayroll.setEmpresaId(resultSetClient.getInt("p.id_co"));
            hrsFormerPayroll.setSucursalEmpresaId(resultSetClient.getInt("bpb.id_bpb"));
            hrsFormerPayroll.setRegimenFiscal(new String[] { SLibUtilities.textTrim(resultSetClient.getString("p.fiscal_settings")) });
        }

        resultSet.close();

        // Obtain employer registry:

        sql = "SELECT cia_reg_imss FROM empresas WHERE cia_clave = " + SHrsFormerConsts.FPS_COM_ID + ";";

        resultSet = statement.executeQuery(sql);

        if (!resultSet.next()) {
            throw new Exception("No se encontró el registro patronal de la empresa.");
        }
        else {
            cia_reg_imss = SLibUtilities.textTrim(resultSet.getString("cia_reg_imss"));
        }

        resultSet.close();

        // Obtain employee payroll:

        sql = "SELECT " +
            "e.map_bp AS f_emp_map_bp, " +
            "ne.id_empleado AS f_emp_id, " +
            "e.id_empleado AS f_emp_num, " +
            "e.clave_2 AS f_emp_curp, " +
            "e.clave_3 AS f_emp_nss, " +
            "e.cfdi_regimen_tp AS f_emp_reg_tp, " +
            "ne.dias_pagados AS f_emp_dias_pag, " +
            "d.departamento_cve AS f_emp_dep_cve, " +
            "d.departamento AS f_emp_dep, " +
            "'' AS f_emp_bank_clabe, " +
            "CASE WHEN e.cfdi_banco > 0 THEN e.cfdi_banco ELSE " +
            "(SELECT cfdi_banco FROM param_nomina WHERE id_param = " + SHrsFormerConsts.FPS_COM_ID + ") END AS f_emp_bank, " +
            "ne.fecha_alta AS f_emp_alta, " +
            "n.fecha_ini AS f_nom_date_start, " +
            "n.fecha_fin AS f_nom_date_end, " +
            "DATEDIFF(day, e.fecha_prestaciones, n.fecha_fin) / " + SHrsConsts.WEEK_DAYS + " AS f_emp_sen, " + // days per week
            "p.puesto AS f_emp_emp, " +
            "'' AS f_emp_cont_tp, " +
            "'' AS f_emp_jorn_tp, " +
            "pp.tipo AS f_emp_pay, " +
            "ne.salario_bc AS f_emp_sal_bc, " +
            "e.cfdi_riesgo_puesto_tp AS f_emp_risk, " +
            "(SELECT COALESCE(sum(m.monto), 0) " +
            "FROM nom_nominas_emp_movs_p AS m " +
            "WHERE m.id_nomina = ne.id_nomina AND m.id_empleado = ne.id_empleado) AS f_emp_tot_per, " +
            "(SELECT COALESCE(sum(m.monto), 0) " +
            "FROM nom_nominas_emp_movs_d AS m " +
            "WHERE m.id_nomina = ne.id_nomina AND m.id_empleado = ne.id_empleado) AS f_emp_tot_ded, " +
            "(SELECT COALESCE(sum(m.monto), 0) " +
            "FROM nom_nominas_emp_movs_d AS m " +
            "WHERE m.id_nomina = ne.id_nomina AND m.id_empleado = ne.id_empleado AND m.fid_deduccion = " + SHrsFormerConsts.FPS_DED_INC_TAX + ") AS f_emp_tot_rent_ret, " +
            "((SELECT COALESCE(sum(m.monto), 0) " +
            "FROM nom_nominas_emp_movs_p AS m " +
            "WHERE m.id_nomina = ne.id_nomina AND m.id_empleado = ne.id_empleado) - " +
            "(SELECT COALESCE(sum(m.monto), 0) " +
            "FROM nom_nominas_emp_movs_d AS m " +
            "WHERE m.id_nomina = ne.id_nomina AND m.id_empleado = ne.id_empleado)) AS f_emp_tot_net " +
            "FROM nom_nominas AS n " +
            "INNER JOIN nom_nominas_emp AS ne ON " +
            "n.id_nomina = ne.id_nomina " +
            "INNER JOIN nom_empleados AS e ON " +
            "ne.id_empleado = e.id_empleado " +
            "INNER JOIN nom_departamentos AS d ON " +
            "e.fid_departamento = d.id_departamento " +
            "INNER JOIN nom_puestos AS p ON " +
            "ne.fid_puesto = p.id_puesto " +
            "INNER JOIN nomt_periodo_pago AS pp ON " +
            "ne.fid_periodo_pago_tp = pp.id_tipo " +
            "INNER JOIN nom_empleados_tipo AS tp ON " +
            "e.fid_empleado_tp = tp.id_empleado_tipo " +
            "WHERE ne.id_nomina = " + payrollId + " ";
        resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            hrsFormerReceipt = new SHrsFormerReceipt(client, hrsFormerPayroll);

            // Obtain employee company branch:

            f_emp_map_bp = resultSet.getInt("f_emp_map_bp");

            sql = "SELECT bpb.id_bpb  " +
                "FROM erp.bpsu_bpb AS bpb " +
                "WHERE bpb.b_del = 0 AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " AND bpb.fid_bp = " + f_emp_map_bp + " ";
            resultSetClient = statementClient.executeQuery(sql);

            if (!resultSetClient.next()) {
                throw new Exception("No se encontró la sucursal del empleado '" + f_emp_map_bp + "'.");
            }

            hrsFormerReceipt.setPkEmpleadoId(f_emp_map_bp);
            f_emp_id = resultSet.getInt("f_emp_id");
            hrsFormerReceipt.setAuxEmpleadoId(f_emp_id);
            hrsFormerReceipt.setPkSucursalEmpleadoId(resultSetClient.getInt("bpb.id_bpb"));
            hrsFormerReceipt.setRegistroPatronal(cia_reg_imss);
            hrsFormerReceipt.setEmpleadoNum(SLibUtilities.textTrim(resultSet.getString("f_emp_num")));
            hrsFormerReceipt.setEmpleadoCurp(SLibUtilities.textTrim(resultSet.getString("f_emp_curp")));
            hrsFormerReceipt.setNumSeguridadSocial(SLibUtilities.textTrim(resultSet.getString("f_emp_nss")));
            hrsFormerReceipt.setTipoRegimen(resultSet.getInt("f_emp_reg_tp"));
            hrsFormerReceipt.setNumDiasPagados(resultSet.getDouble("f_emp_dias_pag"));
            hrsFormerReceipt.setDepartamento(SLibUtilities.textTrim(resultSet.getString("f_emp_dep")));
            hrsFormerReceipt.setCuentaBancaria(SLibUtilities.textTrim(resultSet.getString("f_emp_bank_clabe")));
            hrsFormerReceipt.setBanco(resultSet.getInt("f_emp_bank"));
            hrsFormerReceipt.setFechaPago(tPayrollDatePayment);
            hrsFormerReceipt.setFechaInicioRelLaboral(dateFormat.parse(SLibUtilities.textTrim(resultSet.getString("f_emp_alta"))));
            hrsFormerReceipt.setFechaInicialPago(dateFormat.parse(SLibUtilities.textTrim(resultSet.getString("f_nom_date_start"))));
            hrsFormerReceipt.setFechaFinalPago(dateFormat.parse(SLibUtilities.textTrim(resultSet.getString("f_nom_date_end"))));
            hrsFormerReceipt.setAntiguedad(resultSet.getInt("f_emp_sen"));
            hrsFormerReceipt.setPuesto(SLibUtilities.textTrim(resultSet.getString("f_emp_emp")));
            hrsFormerReceipt.setTipoContrato(SLibUtilities.textTrim(resultSet.getString("f_emp_cont_tp")));
            hrsFormerReceipt.setTipoJornada(SLibUtilities.textTrim(resultSet.getString("f_emp_jorn_tp")));
            hrsFormerReceipt.setPeriodicidadPago(SLibUtilities.textTrim(resultSet.getString("f_emp_pay")));
            hrsFormerReceipt.setSalarioBaseCotApor(resultSet.getDouble("f_emp_sal_bc"));
            hrsFormerReceipt.setRiesgoPuesto(resultSet.getInt("f_emp_risk"));
            hrsFormerReceipt.setSalarioDiarioIntegrado(hrsFormerReceipt.getSalarioBaseCotApor());
            hrsFormerReceipt.setTotalPercepciones(resultSet.getDouble("f_emp_tot_per"));
            hrsFormerReceipt.setTotalDeducciones(resultSet.getDouble("f_emp_tot_ded"));
            hrsFormerReceipt.setTotalRetenciones(resultSet.getDouble("f_emp_tot_rent_ret"));
            hrsFormerReceipt.setTotalNeto(resultSet.getDouble("f_emp_tot_net"));

            // Obtain 'num_ser', 'num', 'fid_tp_pay_sys' from 'hrs_sie_pay_emp' table:

            sql = "SELECT pe.num_ser, pe.num, pe.fid_tp_pay_sys " +
                "FROM hrs_sie_pay AS p " +
                "INNER JOIN hrs_sie_pay_emp AS pe ON " +
                "p.id_pay = pe.id_pay AND pe.b_del = FALSE " +
                "WHERE pe.id_pay = " + payrollId + " AND pe.id_emp = " + f_emp_id + "";
            resultSetClient = statementClient.executeQuery(sql);

            if (!resultSetClient.next()) {
                throw new Exception("No se encontró el folio del empleado '" + SLibUtilities.textTrim(resultSet.getString("f_emp_num")) + "'.");
            }
            else {
                hrsFormerReceipt.setSerie(SLibUtilities.textTrim(resultSetClient.getString("pe.num_ser")));
                hrsFormerReceipt.setFolio(resultSetClient.getInt("pe.num"));
                hrsFormerReceipt.setMetodoPago(resultSetClient.getInt("pe.fid_tp_pay_sys"));
            }

            // Obtain currency key from ERP parameters:

            sql = "SELECT c.cur_key " +
                "FROM erp.cfg_param_erp AS p " +
                "INNER JOIN erp.cfgu_cur AS c ON " +
                "p.fid_cur = c.id_cur " +
                "WHERE p.b_del = 0 ";
            resultSetClient = statementClient.executeQuery(sql);

            if (!resultSetClient.next()) {
                throw new Exception("No se encontró la configuración del ERP.");
            }
            else {
                hrsFormerReceipt.setMoneda(SLibUtilities.textTrim(resultSetClient.getString("c.cur_key")));
            }

            hrsFormerPayroll.getChildReceipts().add(hrsFormerReceipt);

            if (hrsFormerReceipt != null) {

                // Obtain perceptions:

                sql = "SELECT " +
                    "p.id_percepcion AS f_conc_cve, " +
                    "p.percepcion AS f_conc, " +
                    "p.cfdi_percepcion_tp AS f_conc_cfdi, " +
                    "CASE WHEN m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT + " OR m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT_DBL + " OR m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT_TRP + " " +
                    "THEN CASE WHEN m.unidades >= 0 AND m.unidades < " + SHrsFormerConsts.OVT_MAX_HRS_DAY + " THEN 1 ELSE m.unidades/" + SHrsFormerConsts.OVT_MAX_HRS_DAY + " END " +
                    " ELSE CASE WHEN m.unidades >= 0 AND m.unidades <= 1 THEN 1 ELSE m.unidades END END AS f_conc_qty, " +
                    "CASE WHEN m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT + " OR m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT_DBL + " OR m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT_TRP + " " +
                    "THEN CASE WHEN m.unidades >= 0 AND m.unidades <= 1 THEN 1 ELSE m.unidades END ELSE 0 END AS f_conc_hrs, " +
                    "p.unidad_medida AS f_conc_unid, " +
                    "m.monto_gravado AS f_conc_mont_grav, " +
                    "m.monto - m.monto_gravado AS f_conc_mont_ext, " +
                    "" + SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[0] + " AS f_conc_tp, " +
                    "CASE WHEN m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT + " OR m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT_DBL + " " +
                    "THEN " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] + " ELSE CASE WHEN m.fid_percepcion = " + SHrsFormerConsts.FPS_PER_OVT_TRP + " " +
                    "THEN " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1] + " " +
                    "ELSE " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[1] + " END END AS f_conc_stp " +
                    "FROM nom_nominas_emp_movs_p AS m  " +
                    "INNER JOIN nom_percepciones AS p ON " +
                    "m.fid_percepcion = p.id_percepcion " +
                    "WHERE m.id_nomina = " + payrollId + " AND m.id_empleado = " + f_emp_id + ";";

                resultSetAux = statementAux.executeQuery(sql);
                while (resultSetAux.next()) {

                    hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();

                    hrsFormerReceiptConcept.setClaveEmpresa(SLibUtilities.textTrim(resultSetAux.getString("f_conc_cve")));
                    hrsFormerReceiptConcept.setConcepto(SLibUtilities.textTrim(resultSetAux.getString("f_conc")));
                    hrsFormerReceiptConcept.setClaveOficial(resultSetAux.getInt("f_conc_cfdi"));
                    claveEmpresa = SLibUtils.parseInt(hrsFormerReceiptConcept.getClaveEmpresa());
                    hrsFormerReceiptConcept.setCantidad(claveEmpresa == SHrsFormerConsts.FPS_PER_OVT ||
                        claveEmpresa == SHrsFormerConsts.FPS_PER_OVT_DBL ||
                        claveEmpresa == SHrsFormerConsts.FPS_PER_OVT_TRP ? Math.ceil(resultSetAux.getDouble("f_conc_qty")) : resultSetAux.getDouble("f_conc_qty"));
                    hrsFormerReceiptConcept.setHoras_r(resultSetAux.getInt("f_conc_hrs"));
                    hrsFormerReceiptConcept.setTotalGravado(resultSetAux.getDouble("f_conc_mont_grav"));
                    hrsFormerReceiptConcept.setTotalExento(resultSetAux.getDouble("f_conc_mont_ext"));
                    hrsFormerReceiptConcept.setPkTipoConcepto(resultSetAux.getInt("f_conc_tp"));
                    hrsFormerReceiptConcept.setPkSubtipoConcepto(resultSetAux.getInt("f_conc_stp"));

                    if (claveEmpresa == SHrsFormerConsts.FPS_PER_OVT ||
                        claveEmpresa == SHrsFormerConsts.FPS_PER_OVT_DBL ||
                        claveEmpresa == SHrsFormerConsts.FPS_PER_OVT_TRP) {

                        hrsFormerConceptExtraTime = new SHrsFormerConceptExtraTime();
                        hrsFormerConceptExtraTime.setTipoHoras(claveEmpresa == SHrsFormerConsts.FPS_PER_OVT || claveEmpresa == SHrsFormerConsts.FPS_PER_OVT_DBL ?
                            SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                        hrsFormerConceptExtraTime.setDias(hrsFormerReceiptConcept.getCantidad());
                        hrsFormerConceptExtraTime.setHorasExtra(hrsFormerReceiptConcept.getHoras_r());
                        hrsFormerConceptExtraTime.setImportePagado(hrsFormerReceiptConcept.getTotalImporte());

                        hrsFormerReceiptConcept.setChildExtraTime(hrsFormerConceptExtraTime);
                    }

                    hrsFormerReceipt.getChildConcepts().add(hrsFormerReceiptConcept);
                }

                resultSetAux.close();

                // Obtain deductions:

                sql = "SELECT " +
                    "d.id_deduccion AS f_conc_cve, " +
                    "d.cfdi_deduccion_tp AS f_conc_cfdi, " +
                    "RTRIM(d.deduccion) + " +
                    "(CASE WHEN m.fid_credito = 0 AND m.fid_prestamo = 0 THEN '' ELSE '; ' + " +
                    "(CASE WHEN m.fid_credito = 0 THEN '' ELSE " +
                    "(CASE WHEN LEN(c.credito_num) = 0 THEN '' ELSE 'no. ' + RTRIM(c.credito_num) + '; ' END) END) + " +
                    "(CASE WHEN m.fid_credito = 0 THEN '' ELSE 'ini.: ' + CONVERT(VARCHAR, CONVERT(DATETIME, c.fecha_inicio, 112), 103) END) + " + // MS SQL Server: 103 = British/French date standard, i.e. "dd/mm/yyyy"; 112 = ISO date standard, i.e. "yyyymmdd" (also Magic date format)
                    "(CASE WHEN m.fid_prestamo = 0 THEN '' ELSE " +
                    "(CASE WHEN LEN(p.prestamo_num) = 0 THEN '' ELSE 'no. ' + RTRIM(p.prestamo_num) + '; ' END) END) + " +
                    "(CASE WHEN m.fid_prestamo = 0 THEN '' ELSE 'ini.: ' + CONVERT(VARCHAR, CONVERT(DATETIME, p.fecha_inicio, 112), 103) END) END) " + // MS SQL Server: 103 = British/French date standard, i.e. "dd/mm/yyyy"; 112 = ISO date standard, i.e. "yyyymmdd" (also Magic date format)
                    " AS f_conc, " +
                    "m.unidades AS f_conc_qty, " +
                    "0 AS f_conc_hrs, " +
                    "'' AS f_conc_unid, " +
                    "m.monto AS f_conc_mont_grav, " +
                    "0 AS f_conc_mont_ext, " +
                    "" + SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[0] + " AS f_conc_tp, " +
                    "" + SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[1] + " AS f_conc_stp " +
                    "FROM nom_nominas_emp_movs_d AS m " +
                    "INNER JOIN nom_deducciones AS d ON " +
                    "m.fid_deduccion = d.id_deduccion " +
                    "LEFT OUTER JOIN nom_creditos_emp  AS c ON " +
                    "m.id_empleado = c.id_empleado AND m.fid_credito = c.id_credito " +
                    "LEFT OUTER JOIN nom_prestamos_emp  AS p ON " +
                    "m.id_empleado = p.id_empleado AND m.fid_prestamo = p.id_prestamo " +
                    "WHERE m.id_nomina = " + payrollId + " AND m.id_empleado = " + f_emp_id + ";";

                resultSetAux = statementAux.executeQuery(sql);
                while (resultSetAux.next()) {

                    hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();

                    hrsFormerReceiptConcept.setClaveEmpresa(SLibUtilities.textTrim(resultSetAux.getString("f_conc_cve")));
                    hrsFormerReceiptConcept.setClaveOficial(resultSetAux.getInt("f_conc_cfdi"));
                    hrsFormerReceiptConcept.setConcepto(SLibUtilities.textTrim(resultSetAux.getString("f_conc")));
                    hrsFormerReceiptConcept.setCantidad(SLibUtils.round(resultSetAux.getDouble("f_conc_qty"), SLibUtils.DecimalFormatPercentage2D.getMaximumFractionDigits()));
                    hrsFormerReceiptConcept.setHoras_r(resultSetAux.getInt("f_conc_hrs"));
                    hrsFormerReceiptConcept.setTotalGravado(resultSetAux.getDouble("f_conc_mont_grav"));
                    hrsFormerReceiptConcept.setTotalExento(resultSetAux.getDouble("f_conc_mont_ext"));
                    hrsFormerReceiptConcept.setPkTipoConcepto(resultSetAux.getInt("f_conc_tp"));
                    hrsFormerReceiptConcept.setPkSubtipoConcepto(resultSetAux.getInt("f_conc_stp"));

                    hrsFormerReceipt.getChildConcepts().add(hrsFormerReceiptConcept);
                }

                resultSetAux.close();
            }
        }

        connectionOdbc.close();
        connectionOdbcAux.close();

        return hrsFormerPayroll;
    }

    // XXX improve this!, this method should not exist, use instead other technique, such as erp.data.SDataReadDescriptions (Sergio Flores, 2017-08-10).
    @Deprecated
    public static String getPaymentMethodName(final SClientInterface client, final int paymentMethodId) throws Exception {
        String paymentMethod = "";
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT tp_pay_sys " +
                "FROM erp.trnu_tp_pay_sys " +
                "WHERE b_del = 0 AND id_tp_pay_sys = " + paymentMethodId + " ";
        resultSet = client.getSession().getStatement().executeQuery(sql);

        if (resultSet.next()) {
            paymentMethod = resultSet.getString("tp_pay_sys");
        }

        return paymentMethod;
    }
    
    @Deprecated
    public static String getPaymentMethodCode(final SClientInterface client, final int paymentMethodId) throws Exception {
        String paymentMethod = "";
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT cfd_pay_met " +
                "FROM erp.trnu_tp_pay_sys " +
                "WHERE b_del = 0 AND id_tp_pay_sys = " + paymentMethodId + " ";
        resultSet = client.getSession().getStatement().executeQuery(sql);

        if (resultSet.next()) {
            paymentMethod = resultSet.getString("cfd_pay_met");
        }

        return paymentMethod;
    }

    @Deprecated
    public static Connection getConnectionOdbc(final SClientInterface client) throws Exception {
        Properties properties = null;
        Connection connectionOdbc;

        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

        if (client.getSessionXXX().getParamsCompany().getFormerSystemOdbcUser().length() == 0) {
            connectionOdbc = DriverManager.getConnection("jdbc:odbc:" + client.getSessionXXX().getParamsCompany().getFormerSystemOdbc());
        }
        else {
            properties = new Properties();
            properties.put("user", client.getSessionXXX().getParamsCompany().getFormerSystemOdbcUser());
            properties.put("password", client.getSessionXXX().getParamsCompany().getFormerSystemOdbcUserPassword());
            connectionOdbc = DriverManager.getConnection("jdbc:odbc:" + client.getSessionXXX().getParamsCompany().getFormerSystemOdbc(), properties);
        }

        if (connectionOdbc == null || connectionOdbc.isClosed()) {
            throw new Exception("No se pudo establecer la conexión con el origen de datos ODBC '" + client.getSessionXXX().getParamsCompany().getFormerSystemOdbc() + "'.");
        }


        return connectionOdbc;
    }
}
