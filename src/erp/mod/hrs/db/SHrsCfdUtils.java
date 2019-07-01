/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import cfd.DCfdUtils;
import cfd.ver33.DCfdi33Catalogs;
import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.mhrs.data.SHrsPayrollEmployeeReceipt;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SCfdPacket;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Irving Sánchez, Juan Barajas, Sergio Flores, Claudio Peña
 * 
 * Maintenance Log:
 * 2018-01-02, Sergio Flores:
 *  Implementation of payroll into CFDI 3.3.
 */
public abstract class SHrsCfdUtils {
    
    private static boolean validateReceiptsPendingCfdi( final SGuiSession session, final int payrollId) throws Exception {
        ArrayList<SDataCfd> cfds = SCfdUtils.getPayrollCfds((SClientInterface) session.getClient(), SCfdConsts.CFDI_PAYROLL_VER_CUR, new int[] { payrollId });
        SCfdUtils.existsCfdiPending((SClientInterface) session.getClient(), cfds);
        return true;
    }
    
    public static boolean canGenetareCfdReceipts(final SGuiSession session, final int payrollId) throws Exception {
        String sql = "SELECT b_clo, b_del FROM hrs_pay WHERE id_pay = " + payrollId + "; ";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if(!resultSet.getBoolean("b_clo")) {
                throw new Exception("No se pueden generar CFDI, la nómina no está cerrada.");
            }
            else if (resultSet.getBoolean("b_del")) {
                throw new Exception("No se pueden generar CFDI, la nómina está eliminada.");
            }
        }
        
        return validateReceiptsPendingCfdi(session, payrollId);
    }
    
    public static ArrayList<SHrsPayrollEmployeeReceipt> getReceiptsPendig(final SGuiSession session, final int payrollId) throws Exception {
        ArrayList<SHrsPayrollEmployeeReceipt> receipts = new ArrayList<>();
        
        String sql = "SELECT p.id_pay, p.per_year, p.per, p.num, p.dt_sta, p.dt_end, p.nts, p.fk_tp_pay, pr.id_emp, bp.bp, emp.num AS f_emp_num, "
                + "@ear := (SELECT COALESCE(SUM(rcp_ear.amt_r), 0) "
                + "FROM hrs_pay_rcp AS r "
                + "INNER JOIN hrs_pay_rcp_ear AS rcp_ear ON rcp_ear.id_pay = r.id_pay AND rcp_ear.id_emp = r.id_emp "
                + "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ear.b_del = 0 AND rcp_ear.id_emp = pr.id_emp) AS f_ear, "
                + "@ded := (SELECT COALESCE(SUM(rcp_ded.amt_r), 0) "
                + "FROM hrs_pay_rcp AS r "
                + "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp "
                + "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.id_emp = pr.id_emp) AS f_ded, "
                + "@ear - @ded AS f_net, "
                + "pri.id_iss, pri.num_ser, pri.uuid_rel, "
                + "pri.fk_tp_pay_sys, pri.dt_iss, pri.dt_pay " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON "
                + "pr.id_pay = pri.id_pay AND pr.id_emp = pri.id_emp AND pri.b_del = 0 AND pri.fk_st_rcp = " + SModSysConsts.TRNS_ST_DPS_EMITED + " AND pri.id_iss = "
                + "(SELECT pri1.id_iss FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri1 WHERE pri1.id_pay = pri.id_pay AND pri1.id_emp = pri.id_emp AND pri1.b_del = 0 ORDER BY pri1.id_iss DESC LIMIT 1) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = pr.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON bp.id_bp = emp.id_emp "
                + "WHERE p.id_pay = " + payrollId + " AND p.b_del = 0 AND pr.b_del = 0 AND pr.b_cfd_req = 1 AND "
                + "NOT EXISTS (SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " WHERE fid_pay_rcp_pay_n = pri.id_pay AND fid_pay_rcp_emp_n = pri.id_emp AND fid_pay_rcp_iss_n = pri.id_iss) "
                + "UNION "
                + "SELECT p.id_pay, p.per_year, p.per, p.num, p.dt_sta, p.dt_end, p.nts, p.fk_tp_pay, pr.id_emp, bp.bp, emp.num AS f_emp_num, "
                + "@ear := (SELECT COALESCE(SUM(rcp_ear.amt_r), 0) "
                + "FROM hrs_pay_rcp AS r "
                + "INNER JOIN hrs_pay_rcp_ear AS rcp_ear ON rcp_ear.id_pay = r.id_pay AND rcp_ear.id_emp = r.id_emp "
                + "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ear.b_del = 0 AND rcp_ear.id_emp = pr.id_emp) AS f_ear, "
                + "@ded := (SELECT COALESCE(SUM(rcp_ded.amt_r), 0) "
                + "FROM hrs_pay_rcp AS r "
                + "INNER JOIN hrs_pay_rcp_ded AS rcp_ded ON rcp_ded.id_pay = r.id_pay AND rcp_ded.id_emp = r.id_emp "
                + "WHERE r.id_pay = p.id_pay AND r.b_del = 0 AND rcp_ded.b_del = 0 AND rcp_ded.id_emp = pr.id_emp) AS f_ded, "
                + "@ear - @ded AS f_net, "
                + "pri.id_iss, pri.num_ser, pri.uuid_rel, "
                + "pri.fk_tp_pay_sys, pri.dt_iss, pri.dt_pay "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON "
                + "pr.id_pay = pri.id_pay AND pr.id_emp = pri.id_emp AND pri.b_del = 0 " + /*AND pri.fk_st_rcp = " + SModSysConsts.TRNS_ST_DPS_ANNULED + "*/ " AND pri.id_iss = "
                + "(SELECT pri1.id_iss FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri1 WHERE pri1.id_pay = pri.id_pay AND pri1.id_emp = pri.id_emp AND pri1.b_del = 0 ORDER BY pri1.id_iss DESC LIMIT 1) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = pr.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON bp.id_bp = emp.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c ON pri.id_pay = c.fid_pay_rcp_pay_n AND pri.id_emp = c.fid_pay_rcp_emp_n AND pri.id_iss = c.fid_pay_rcp_iss_n AND (c.fid_st_xml IN (" + SDataConstantsSys.TRNS_ST_DPS_NEW + " , " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ") OR LENGTH(c.uuid) = 0 ) "
                + "WHERE p.id_pay = " + payrollId + " AND p.b_del = 0 AND pr.b_del = 0 "
                + "ORDER BY id_pay, per_year, per, num, dt_sta, dt_end, nts, fk_tp_pay, bp, id_emp, f_emp_num, f_ear, f_ded, id_iss, num_ser, f_net, fk_tp_pay_sys, dt_iss, dt_pay ";
        
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            SHrsPayrollEmployeeReceipt receipt = new SHrsPayrollEmployeeReceipt();
            
            receipt.setPkPayrollId(resultSet.getInt("id_pay"));
            receipt.setPkEmployeeId(resultSet.getInt("id_emp"));
            receipt.setPkIssueId(resultSet.getInt("id_iss"));
            receipt.setPeriodYear(resultSet.getInt("per_year"));
            receipt.setPeriod(resultSet.getInt("per"));
            receipt.setPayrollNumber(resultSet.getInt("num"));
            receipt.setDateStart(resultSet.getDate("dt_sta"));
            receipt.setDateEnd(resultSet.getDate("dt_end"));
            receipt.setNotes(resultSet.getString("nts"));
            receipt.setFkPaymentTypeId(resultSet.getInt("fk_tp_pay"));
            receipt.setEmployeeNumber(resultSet.getString("f_emp_num"));
            receipt.setEmployeeName(resultSet.getString("bp"));
            receipt.setTotalEarnings(resultSet.getDouble("f_ear"));
            receipt.setTotalDeductions(resultSet.getDouble("f_ded"));
            receipt.setTotalNet(resultSet.getDouble("f_net"));
            receipt.setNumberSeries(resultSet.getString("num_ser"));
            receipt.setDateIssue(resultSet.getDate("dt_iss"));
            receipt.setDatePayment(resultSet.getDate("dt_pay"));
            receipt.setPaymentTypeSysId(resultSet.getInt("fk_tp_pay_sys"));
            receipt.setPaymentTypeSys("");
            receipt.setUuidToSubstitute(resultSet.getString("uuid_rel"));
            
            receipts.add(receipt);
        }
        
        return receipts;
    }
    
    private static SHrsFormerPayroll readHrsFormerPayrollAndReceipt(final SClientInterface client, final int[] receiptKey) throws SQLException, Exception {
        int nPaymentType = 0;
        int nBankDefaultId = 0;
        int nEarningTaxSubsidyCompId = 0;
        String sEarTaxSubsidyCode = "";
        boolean bBankAccountUse = false;
        String sql = "";
        String deductionsTaxRetained = "";
        double dAmountEarTax = 0;
        double dAmountEarExe = 0;
        double dTotalEar = 0;
        double dAmountMonth = 0;

        SHrsFormerPayroll hrsFormerPayroll = null;
        SHrsFormerReceipt hrsFormerReceipt = null;
        SHrsFormerReceiptConcept hrsFormerReceiptConcept = null;
        SHrsFormerConceptExtraTime hrsFormerConceptExtraTime = null;

        Statement statement = client.getSession().getStatement().getConnection().createStatement();
        Statement statementAux = client.getSession().getStatement().getConnection().createStatement();
        Statement statementAuxInc = client.getSession().getStatement().getConnection().createStatement();
        Statement statementClient = client.getSession().getStatement().getConnection().createStatement();

        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        ResultSet resultSetAuxInc = null;
        ResultSet resultSetClient = null;

        // Settings module human resource:

        sql = "SELECT cfg.b_bank_acc_use, cfg.fk_bank, cfg.fk_ear_tax_sub_comp_n, e.code AS _tax_sub_code "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_CFG) + " AS cfg "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e ON cfg.fk_ear_tax_sub_n = e.id_ear "
                + "WHERE cfg.id_cfg = " + SUtilConsts.BPR_CO_ID + ";";
        
        resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception("No se encontró la configuración del Módulo Recursos Humanos.");
        }
        else {
            bBankAccountUse = resultSet.getBoolean("b_bank_acc_use");
            nBankDefaultId = resultSet.getInt("fk_bank");
            nEarningTaxSubsidyCompId = resultSet.getInt("fk_ear_tax_sub_comp_n");
            sEarTaxSubsidyCode = resultSet.getString("_tax_sub_code");
        }
        
        // Obtain deductions for tax retained:
        
        sql = "SELECT id_ded FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " WHERE fk_tp_ded = " + SModSysConsts.HRSS_TP_DED_TAX + "; ";
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            deductionsTaxRetained += (deductionsTaxRetained.isEmpty() ? "" : ", ") + resultSet.getInt("id_ded");
        }

        // Obtain payroll header (this is always an one-row query):

        sql = "SELECT pri.dt_iss, p.dt_sta, p.dt_end, p.per_year, p.num, p.fk_tp_pay, p.fk_tp_pay_sht, " +
                "(SELECT COALESCE(SUM(pre.amt_r), 0.0) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre " +
                "WHERE pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp AND NOT pre.b_del) AS _ear, " +
                "(SELECT COALESCE(SUM(prd.amt_r), 0.0) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd " +
                "WHERE prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp AND NOT prd.b_del) AS _ded, " +
                "(SELECT COALESCE(SUM(prd.amt_r), 0.0) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd " +
                "WHERE prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp AND NOT prd.b_del AND prd.fk_ded IN (" + deductionsTaxRetained + ")) AS _ded_tax " +
                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay " +
                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp " +
                "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pri.b_del AND " +
                "pri.id_pay = " + receiptKey[0] + " AND pri.id_emp = " + receiptKey[1] + " AND pri.id_iss = " + receiptKey[2] + ";";
        
        resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception("No se encontró la nómina.");
        }
        else {
            hrsFormerPayroll = new SHrsFormerPayroll(client);
            hrsFormerPayroll.setPkNominaId(receiptKey[0]);
            hrsFormerPayroll.setFecha(resultSet.getDate("pri.dt_iss"));
            hrsFormerPayroll.setFechaInicial(resultSet.getDate("p.dt_sta"));
            hrsFormerPayroll.setFechaFinal(resultSet.getDate("p.dt_end"));
            hrsFormerPayroll.setTotalPercepciones(resultSet.getDouble("_ear"));
            hrsFormerPayroll.setTotalDeducciones(resultSet.getDouble("_ded"));
            hrsFormerPayroll.setTotalRetenciones(resultSet.getDouble("_ded_tax"));
            hrsFormerPayroll.setEmpresaId(client.getSession().getConfigCompany().getCompanyId());
            hrsFormerPayroll.setSucursalEmpresaId(client.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getPkBizPartnerBranchId());
            hrsFormerPayroll.setRegimenFiscal(new String[] { client.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal() });
            hrsFormerPayroll.setFkNominaTipoId(resultSet.getInt("p.fk_tp_pay_sht"));
            
            nPaymentType = resultSet.getInt("p.fk_tp_pay");

            // Obtain employee payroll receipt (this is always an one-row query):

            sql = "SELECT emp.num AS f_emp_num, bp.alt_id AS f_emp_curp, emp.ssn AS f_emp_nss, " +
                    "tsch.code AS f_emp_reg_tp, pr.day_pad AS f_emp_dias_pag, dep.name AS f_emp_dep, " +
                    "pr.pay_tax_sub_assd, pr.pay_tax_sub_comp, pr.pay_tax_sub_pend_r, pr.pay_tax_sub_payd, " +
                    (bBankAccountUse ? "emp.bank_acc" : "''") + " AS f_emp_bank_clabe, " +
                    "CASE WHEN emp.fk_bank_n IS NOT NULL THEN emp.fk_bank_n ELSE " + nBankDefaultId + " END AS f_emp_bank, " +
                    "pr.sal, pr.wage, pr.dt_hire AS f_emp_alta, p.dt_sta AS f_nom_ini, p.dt_end AS f_nom_fin, " +
                    "pri.dt_pay, pri.num_ser, pri.num, pri.uuid_rel, pri.fk_tp_pay_sys, " +
                    "TIMESTAMPDIFF(DAY, emp.dt_hire, p.dt_end) / " + SHrsConsts.WEEK_DAYS + " AS f_emp_sen, pos.name AS f_emp_pos, " +
                    "tcon.code AS f_emp_cont_tp, twkd.code AS f_emp_jorn_tp, tpay.code AS f_emp_pay, pr.sal_ssc AS f_emp_sal_bc, trsk.code AS f_emp_risk, " +
                    "IF(emp.b_uni, '" + DCfdi33Catalogs.TxtSí + "', '" + DCfdi33Catalogs.TxtNo + "') AS f_emp_union, " +
                    "NOW() AS f_emp_date_edit " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp " +
                    "INNER JOIN erp.bpsu_bp AS bp ON bp.id_bp = pr.id_emp " +
                    "INNER JOIN erp.hrsu_emp AS emp ON emp.id_emp = bp.id_bp " +
                    "INNER JOIN erp.hrsu_dep AS dep ON dep.id_dep = pr.fk_dep " +
                    "INNER JOIN erp.hrsu_pos AS pos ON pos.id_pos = pr.fk_pos " +
                    "INNER JOIN erp.hrss_tp_con AS tcon ON tcon.id_tp_con = pr.fk_tp_con " +
                    "INNER JOIN erp.hrss_tp_rec_sche AS tsch ON tsch.id_tp_rec_sche = pr.fk_tp_rec_sche " +
                    "INNER JOIN erp.hrss_tp_pos_risk AS trsk ON trsk.id_tp_pos_risk = pr.fk_tp_pos_risk " +
                    "INNER JOIN erp.hrss_tp_work_day AS twkd ON pr.fk_tp_work_day = twkd.id_tp_work_day " +
                    "INNER JOIN erp.hrss_tp_pay AS tpay ON p.fk_tp_pay = tpay.id_tp_pay " +
                    "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pri.b_del AND " +
                    "pri.id_pay = " + receiptKey[0] + " AND pri.id_emp = " + receiptKey[1] + " AND pri.id_iss = " + receiptKey[2] + ";";
            resultSet = statement.executeQuery(sql);

            if (!resultSet.next()) {
                throw new Exception("No se encontró el recibo de nómina.");
            }
            else {
                dAmountEarTax = 0;
                dAmountEarExe = 0;
                dTotalEar = 0;
                dAmountMonth = 0;

                // Obtain employee company branch:

                sql = "SELECT bpb.id_bpb, sta.sta_code AS _sta " +
                    "FROM erp.bpsu_bpb AS bpb " +
                    "INNER JOIN erp.bpsu_bpb_add AS bpb_add ON bpb.id_bpb = bpb_add.id_bpb AND bpb_add.fid_tp_add = " + SDataConstantsSys.BPSS_TP_ADD_OFF + " " +
                    "LEFT OUTER JOIN erp.locu_sta AS sta ON bpb_add.fid_sta_n = sta.id_sta " +
                    "WHERE bpb.b_del = 0 AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " AND bpb.fid_bp = " + receiptKey[1] + ";";
                resultSetClient = statementClient.executeQuery(sql);

                if (!resultSetClient.next()) {
                    throw new Exception("No se encontró la sucursal del empleado con ID = " + receiptKey[1] + ".");
                }
                else {
                    hrsFormerReceipt = new SHrsFormerReceipt(client, hrsFormerPayroll);
                    hrsFormerReceipt.setPkEmpleadoId(receiptKey[1]);
                    hrsFormerReceipt.setAuxEmpleadoId(receiptKey[1]);
                    hrsFormerReceipt.setPkSucursalEmpleadoId(resultSetClient.getInt("bpb.id_bpb"));
                    hrsFormerReceipt.setRegistroPatronal(client.getSessionXXX().getParamsCompany().getRegistrySs());
                    hrsFormerReceipt.setNumEmpleado(SLibUtilities.textTrim(resultSet.getString("f_emp_num")));
                    hrsFormerReceipt.setCurp(SLibUtilities.textTrim(resultSet.getString("f_emp_curp")));
                    hrsFormerReceipt.setTipoRegimen(resultSet.getInt("f_emp_reg_tp"));
                    hrsFormerReceipt.setNumSeguridadSocial(SLibUtilities.textTrim(resultSet.getString("f_emp_nss")));
                    hrsFormerReceipt.setFechaPago(resultSet.getDate("pri.dt_pay"));
                    hrsFormerReceipt.setFechaInicialPago(resultSet.getDate("f_nom_ini"));
                    hrsFormerReceipt.setFechaFinalPago(resultSet.getDate("f_nom_fin"));
                    hrsFormerReceipt.setNumDiasPagados(resultSet.getDouble("f_emp_dias_pag"));
                    hrsFormerReceipt.setDepartamento(SLibUtilities.textTrim(resultSet.getString("f_emp_dep")));
                    hrsFormerReceipt.setBanco(resultSet.getInt("f_emp_bank"));
                    hrsFormerReceipt.setCuentaBancaria(SLibUtilities.textTrim(resultSet.getString("f_emp_bank_clabe")));
                    hrsFormerReceipt.setFechaInicioRelLaboral(resultSet.getDate("f_emp_alta"));
                    hrsFormerReceipt.setAntiguedad(resultSet.getInt("f_emp_sen"));
                    hrsFormerReceipt.setPuesto(SLibUtilities.textTrim(resultSet.getString("f_emp_pos")));
                    hrsFormerReceipt.setTipoContrato(SLibUtilities.textTrim(resultSet.getString("f_emp_cont_tp")));
                    hrsFormerReceipt.setSindicalizado(SLibUtilities.textTrim(resultSet.getString("f_emp_union")));
                    hrsFormerReceipt.setTipoJornada(SLibUtilities.textTrim(resultSet.getString("f_emp_jorn_tp")));
                    hrsFormerReceipt.setPeriodicidadPago(SLibUtilities.textTrim(resultSet.getString("f_emp_pay")));
                    hrsFormerReceipt.setSalarioBaseCotApor(resultSet.getDouble("f_emp_sal_bc"));
                    hrsFormerReceipt.setRiesgoPuesto(resultSet.getInt("f_emp_risk"));
                    hrsFormerReceipt.setSalarioDiarioIntegrado(hrsFormerReceipt.getSalarioBaseCotApor());
                    hrsFormerReceipt.setClaveEstado(SLibUtilities.textTrim(resultSetClient.getString("_sta")));
                    
                    hrsFormerReceipt.setMetodoPago(resultSet.getInt("pri.fk_tp_pay_sys"));
                    hrsFormerReceipt.setSerie(SLibUtilities.textTrim(resultSet.getString("pri.num_ser")));
                    hrsFormerReceipt.setFolio(resultSet.getInt("pri.num"));
                    hrsFormerReceipt.setFechaEdicion(resultSet.getDate("f_emp_date_edit"));

                    dAmountMonth = SLibUtils.roundAmount(nPaymentType == SModSysConsts.HRSS_TP_PAY_WEE ? (resultSet.getDouble("pr.sal") * SHrsConsts.MONTH_DAYS_FIXED) : resultSet.getDouble("pr.wage"));
                    hrsFormerReceipt.setAuxSueldoMensual(dAmountMonth);
                    
                    if (!resultSet.getString("pri.uuid_rel").isEmpty()) {
                        hrsFormerReceipt.setCfdiRelacionadosTipoRelacion(DCfdi33Catalogs.REL_TP_SUSTITUCION);
                        hrsFormerReceipt.getCfdiRelacionados().add(resultSet.getString("pri.uuid_rel"));
                    }

                    boolean bTaxSubFound = false;
                    double dTaxSubPayrollComp = SLibUtils.roundAmount(resultSet.getDouble("pr.pay_tax_sub_comp") + resultSet.getDouble("pr.pay_tax_sub_payd"));

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
                        boolean hasEarningTaxSubComp = false;
                        
                        hrsFormerReceipt.setMoneda(SLibUtilities.textTrim(resultSetClient.getString("c.cur_key")));
                        hrsFormerReceipt.setLugarExpedicion(client.getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
                        hrsFormerReceipt.setRegimenFiscal(client.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());

                        // Obtain perceptions:

                        sql = "SELECT e.id_ear AS f_conc_id, e.code AS f_conc_cve, e.name_abbr AS f_conc, e.fk_tp_ear AS f_conc_cfdi, e.unt_fac, pre.fk_ear, " +
                                "CASE WHEN e.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVR_TME + " THEN CASE WHEN pre.unt >= 0 AND pre.unt < " + SHrsConsts.OVER_TIME_2X_MAX_DAY + " THEN 1 ELSE pre.unt / " + SHrsConsts.OVER_TIME_2X_MAX_DAY + " END ELSE " +
                                "CASE WHEN pre.unt >= 0 AND pre.unt <= 1 THEN 1 ELSE pre.unt END END AS f_conc_qty, " +
                                "CASE WHEN e.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVR_TME + " THEN " +
                                "CASE WHEN pre.unt >= 0 AND pre.unt <= 1 THEN 1 ELSE pre.unt END ELSE 0 END AS f_conc_hrs, ec.code AS f_conc_unid, " +
                                "pre.b_aut AS f_aut, pre.b_usr AS f_usr, pre.amt_taxa AS f_conc_mont_grav, pre.amt_exem AS f_conc_mont_ext, " +
                                "" + SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[0] + " AS f_conc_tp, " +
                                "CASE WHEN e.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVR_TME + " AND e.unt_fac = " + SHrsConsts.OVER_TIME_2X + " THEN " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] + " ELSE " +
                                "CASE WHEN e.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVR_TME + " AND e.unt_fac = " + SHrsConsts.OVER_TIME_3X + " THEN " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1] + " ELSE " + 
                                "" + SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[1] + " END END AS f_conc_stp " +
                                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay AND pr.b_del = 0 " +
                                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp AND pre.b_del = 0 " +
                                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e ON e.id_ear = pre.fk_ear " +
                                "INNER JOIN erp.hrss_tp_ear_comp AS ec ON ec.id_tp_ear_comp = e.fk_tp_ear_comp " +
                                "WHERE p.id_pay = " + receiptKey[0] + " AND pr.id_emp = " + receiptKey[1] + ";";

                        resultSetAux = statementAux.executeQuery(sql);
                        while (resultSetAux.next()) {
                            dAmountEarTax = resultSetAux.getDouble("f_conc_mont_grav");
                            dAmountEarExe = resultSetAux.getDouble("f_conc_mont_ext");
                            
                            if (dAmountEarTax > 0 || dAmountEarExe > 0) {
                                // check if current earning is a dummy earning created only for acomplishing CFDI requirements:
                                if (resultSetAux.getInt("f_conc_id") == nEarningTaxSubsidyCompId && resultSetAux.getBoolean("f_aut") && !resultSetAux.getBoolean("f_usr") && SLibUtils.roundAmount(dAmountEarTax) == 0 && SLibUtils.compareAmount(SLibUtils.roundAmount(dAmountEarExe), 0.01)) {
                                    hasEarningTaxSubComp = true;
                                    continue; // omit current earning because subsidy was totally offset against tax; anyway it will be created later in this current method!
                                }

                                dTotalEar = SLibUtils.roundAmount(dTotalEar + dAmountEarTax + dAmountEarExe);

                                hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();
                                hrsFormerReceiptConcept.setClaveEmpresa(SLibUtilities.textTrim(resultSetAux.getString("f_conc_cve")));
                                hrsFormerReceiptConcept.setClaveOficial(resultSetAux.getInt("f_conc_cfdi"));
                                hrsFormerReceiptConcept.setConcepto(SLibUtilities.textTrim(resultSetAux.getString("f_conc")));
                                hrsFormerReceiptConcept.setCantidad(hrsFormerReceiptConcept.getClaveOficial() == SModSysConsts.HRSS_TP_EAR_OVR_TME ? Math.ceil(resultSetAux.getDouble("f_conc_qty")) : resultSetAux.getDouble("f_conc_qty"));
                                hrsFormerReceiptConcept.setHoras_r(resultSetAux.getInt("f_conc_hrs"));
                                hrsFormerReceiptConcept.setTotalGravado(dAmountEarTax);
                                hrsFormerReceiptConcept.setTotalExento(dAmountEarExe);
                                hrsFormerReceiptConcept.setPkTipoConcepto(resultSetAux.getInt("f_conc_tp"));
                                hrsFormerReceiptConcept.setPkSubtipoConcepto(resultSetAux.getInt("f_conc_stp"));

                                switch (hrsFormerReceiptConcept.getClaveOficial()) {
                                    case SModSysConsts.HRSS_TP_EAR_OVR_TME:
                                        hrsFormerConceptExtraTime = new SHrsFormerConceptExtraTime();
                                        hrsFormerConceptExtraTime.setTipoHoras(resultSetAux.getInt("unt_fac") == SHrsConsts.OVER_TIME_2X ?
                                            SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                                        hrsFormerConceptExtraTime.setDias(hrsFormerReceiptConcept.getCantidad());
                                        hrsFormerConceptExtraTime.setHorasExtra(hrsFormerReceiptConcept.getHoras_r());
                                        hrsFormerConceptExtraTime.setImportePagado(hrsFormerReceiptConcept.getTotalImporte());

                                        hrsFormerReceiptConcept.setChildExtraTime(hrsFormerConceptExtraTime);
                                        break;

                                    case SModSysConsts.HRSS_TP_EAR_DIS:
                                        sql = "SELECT tpd.code " +
                                                "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e " +
                                                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_ABS) + " AS tpa ON e.fk_cl_abs_n = tpa.id_cl_abs AND e.fk_tp_abs_n = tpa.id_tp_abs " +
                                                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_DIS) + " AS tpd ON tpa.fk_tp_dis_n = tpd.id_tp_dis " +
                                                "WHERE e.id_ear = " + resultSetAux.getInt("pre.fk_ear") + " ";

                                        resultSetAuxInc = statementAuxInc.executeQuery(sql);
                                        if (resultSetAuxInc.next()) {
                                            hrsFormerReceiptConcept.setXtaClaveIncapacidad(resultSetAuxInc.getString("tpd.code"));
                                        }
                                        break;

                                    case SModSysConsts.HRSS_TP_EAR_TAX_SUB:
                                        bTaxSubFound = true;
                                        if (dTaxSubPayrollComp > 0) {
                                            hrsFormerReceiptConcept.setXtaSubsidioEmpleo(dTaxSubPayrollComp);
                                        }
                                        break;

                                    default:
                                }

                                hrsFormerReceipt.getChildConcepts().add(hrsFormerReceiptConcept);
                            }
                        }

                        // Add a dummy earning only for acomplishing CFDI requirements, if it is needed:
                        
                        if (!bTaxSubFound && dTaxSubPayrollComp > 0) {
                            if (!hasEarningTaxSubComp) {
                                throw new Exception("El recibo no tiene el nodo otro pago para informar del Subsidio para el empleo totalmente compensado.");
                            }
                            
                            hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();
                            hrsFormerReceiptConcept.setClaveEmpresa(sEarTaxSubsidyCode);
                            hrsFormerReceiptConcept.setClaveOficial(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
                            hrsFormerReceiptConcept.setConcepto(SCfdConsts.CFDI_OTHER_PAY_TAX_SUBSIDY.toUpperCase());
                            hrsFormerReceiptConcept.setCantidad(0);
                            hrsFormerReceiptConcept.setHoras_r(0);
                            hrsFormerReceiptConcept.setTotalGravado(0);
                            hrsFormerReceiptConcept.setTotalExento(0.01); // fixed value when tax subsidy is not actually paid
                            hrsFormerReceiptConcept.setPkTipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_TAX_SUBSIDY[0]);
                            hrsFormerReceiptConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_TAX_SUBSIDY[1]);
                            hrsFormerReceiptConcept.setXtaSubsidioEmpleo(dTaxSubPayrollComp);

                            hrsFormerReceipt.getChildConcepts().add(hrsFormerReceiptConcept);
                            
                            // XXX 2019-02-22, Sergio Flores: Temporal block of code, remove this!
                            dTotalEar = SLibUtils.roundAmount(dTotalEar + 0.01);
                            hrsFormerPayroll.setTotalPercepciones(SLibUtils.roundAmount(hrsFormerPayroll.getTotalPercepciones() + 0.01));
                            // XXX 2019-02-22, Sergio Flores: Temporal block of code, remove this!
                        }

                        // Obtain deductions:

                        double dTotalDed = 0;
                        double dTotalDedRet = 0;
                        
                        sql = "SELECT d.id_ded AS f_conc_id, d.code AS f_conc_cve, d.fk_tp_ded AS f_conc_cfdi, d.fk_tp_ded, " +
                                "d.name AS f_conc, " +
                                "prd.unt AS f_conc_qty, 0 AS f_conc_hrs, '' AS f_conc_unid, prd.amt_r AS f_conc_mont_grav, 0 AS f_conc_mont_ext, " +
                                "" + SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[0] + " AS f_conc_tp, " +
                                "" + SCfdConsts.CFDI_PAYROLL_DEDUCTION_DEDUCTION[1] + " AS f_conc_stp " +
                                "FROM hrs_pay AS p " +
                                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay " +
                                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp " +
                                "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS d ON d.id_ded = prd.fk_ded " +
                                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_LOAN) + " AS l ON l.id_emp = prd.fk_loan_emp_n AND l.id_loan = prd.fk_loan_loan_n " +
                                "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_LOAN) + " AS tl ON tl.id_tp_loan = prd.fk_tp_loan_n " +
                                "WHERE p.id_pay = " + receiptKey[0] + " AND pr.b_del = 0 AND prd.b_del = 0 AND pr.id_emp = " + receiptKey[1] + ";";

                        resultSetAux = statementAux.executeQuery(sql);
                        while (resultSetAux.next()) {
                            double dAmountDedTax = resultSetAux.getDouble("f_conc_mont_grav");
                            double dAmountDedExe = resultSetAux.getDouble("f_conc_mont_ext");
                            
                            if (dAmountDedTax > 0 || dAmountDedExe > 0) {
                                dTotalDed = SLibUtils.roundAmount(dTotalDed + dAmountDedTax + dAmountDedExe);

                                if (resultSetAux.getInt("d.fk_tp_ded") == SModSysConsts.HRSS_TP_DED_TAX) {
                                    dTotalDedRet = SLibUtils.roundAmount(dTotalDedRet + dAmountDedTax + dAmountDedExe);
                                }

                                hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();
                                hrsFormerReceiptConcept.setClaveEmpresa(SLibUtilities.textTrim(resultSetAux.getString("f_conc_cve")));
                                hrsFormerReceiptConcept.setClaveOficial(resultSetAux.getInt("f_conc_cfdi"));
                                hrsFormerReceiptConcept.setConcepto(SLibUtilities.textTrim(resultSetAux.getString("f_conc")));
                                hrsFormerReceiptConcept.setCantidad(SLibUtils.roundAmount(resultSetAux.getDouble("f_conc_qty")));
                                hrsFormerReceiptConcept.setHoras_r(resultSetAux.getInt("f_conc_hrs"));
                                hrsFormerReceiptConcept.setTotalGravado(dAmountDedTax);
                                hrsFormerReceiptConcept.setTotalExento(dAmountDedExe);
                                hrsFormerReceiptConcept.setPkTipoConcepto(resultSetAux.getInt("f_conc_tp"));
                                hrsFormerReceiptConcept.setPkSubtipoConcepto(resultSetAux.getInt("f_conc_stp"));

                                hrsFormerReceipt.getChildConcepts().add(hrsFormerReceiptConcept);
                            }
                        }

                        hrsFormerReceipt.setTotalPercepciones(dTotalEar);
                        hrsFormerReceipt.setTotalDeducciones(dTotalDed);
                        hrsFormerReceipt.setTotalRetenciones(dTotalDedRet);
                        hrsFormerReceipt.setTotalNeto(SLibUtils.roundAmount(dTotalEar - dTotalDed));

                        hrsFormerPayroll.getChildReceipts().add(hrsFormerReceipt);
                    }
                }
            }
        }

        return hrsFormerPayroll;
    }
    
    private static SDataCfd computeCfdi(final SGuiSession session, final SHrsFormerReceipt hrsFormerReceipt, final int receiptIssueId, final boolean cfdiPendingSigned) throws Exception {
        boolean add = true;
        int cfdId = 0;
        String docXmlUuid = "";
        
        String sql = "SELECT id_cfd, doc_xml_uuid, fid_st_xml " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " "
                + "WHERE fid_pay_rcp_pay_n = " + hrsFormerReceipt.getParentPayroll().getPkNominaId() + " AND fid_pay_rcp_emp_n = " + hrsFormerReceipt.getPkEmpleadoId() + " AND fid_pay_rcp_iss_n = " + receiptIssueId + " "
                + "ORDER BY id_cfd ";

        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            if (resultSet.getInt("fid_st_xml") != SDataConstantsSys.TRNS_ST_DPS_ANNULED) {
                if (resultSet.getInt("fid_st_xml") == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                    add = !cfdiPendingSigned;
                }
                else {
                    cfdId = resultSet.getInt("id_cfd");
                    docXmlUuid = resultSet.getString("doc_xml_uuid");
                }
            }
        }
                            
        if (add) {
            // generate CFDI:

            cfd.ver32.DElementComprobante comprobanteCfdi32 = null;
            cfd.ver33.DElementComprobante comprobanteCfdi33 = null;
            
            SCfdPacket packet = new SCfdPacket();
            packet.setCfdId(cfdId);
            packet.setIsCfdConsistent(cfdId == 0);
        
            int xmlType = ((SSessionCustom) session.getSessionCustom()).getCfdTypeXmlTypes().get(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
            float cfdVersion = 0f;
            
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                        comprobanteCfdi32 = (cfd.ver32.DElementComprobante) SCfdUtils.createCfdi32RootElement((SClientInterface) session.getClient(), hrsFormerReceipt);
                        cfdVersion = comprobanteCfdi32.getVersion();
                        
                        packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi32));
                        packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                        comprobanteCfdi33 = (cfd.ver33.DElementComprobante) SCfdUtils.createCfdi33RootElement((SClientInterface) session.getClient(), hrsFormerReceipt);
                        cfdVersion = comprobanteCfdi33.getVersion();
                        
                        packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi33));
                        packet.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW); // after stamping changes to emitted
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
            
            packet.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_PAYROLL);
            packet.setFkXmlTypeId(xmlType);
            packet.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
            packet.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
            packet.setFkUserDeliveryId(session.getUser().getPkUserId());
            packet.setPayrollReceiptPayrollId(hrsFormerReceipt.getParentPayroll().getPkNominaId());
            packet.setPayrollReceiptEmployeeId(hrsFormerReceipt.getAuxEmpleadoId());
            packet.setPayrollReceiptIssueId(receiptIssueId);
            
            packet.setCfdCertNumber(((SClientInterface) session.getClient()).getCfdSignature(cfdVersion).getCertNumber());
            packet.setCfdSignature(((SClientInterface) session.getClient()).getCfdSignature(cfdVersion).sign(packet.getCfdStringSigned(), SLibTimeUtilities.digestYear(hrsFormerReceipt.getParentPayroll().getFecha())[0]));
            packet.setBaseXUuid(docXmlUuid);
            
            switch (xmlType) {
                case SDataConstantsSys.TRNS_TP_XML_CFDI_32:
                    comprobanteCfdi32.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfdi32);
                    break;
                case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                    comprobanteCfdi33.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfdi33);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            ArrayList<SCfdPacket> cfdPackets = new ArrayList<>();
            cfdPackets.add(packet);

            // end of Generate CFDI
            
            SDbFormerPayrollImport payrollImport = new SDbFormerPayrollImport();
            payrollImport.setPayrollId(hrsFormerReceipt.getParentPayroll().getPkNominaId());
            payrollImport.setRegenerateOnlyNonStampedCfdi(cfdiPendingSigned);
            payrollImport.getCfdPackets().addAll(cfdPackets);
            
            SServerRequest request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
            request.setPacket(payrollImport);
            SServerResponse response = ((SClientInterface)session.getClient()).getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                if (response.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception("Código de error al emitir el CFD: " + SLibConstants.MSG_ERR_DB_REG_SAVE + ".");
                }
            }
        }
        
        return SCfdUtils.getPayrollReceiptLastCfd((SClientInterface)session.getClient(), SCfdConsts.CFDI_PAYROLL_VER_CUR, new int[] { hrsFormerReceipt.getParentPayroll().getPkNominaId(), hrsFormerReceipt.getPkEmpleadoId(), receiptIssueId });
    }
        
    public static void computeSignCfdi(final SGuiSession session, int[] keyReceipt) throws Exception {
        SHrsFormerPayroll hrsFormerPayroll = readHrsFormerPayrollAndReceipt((SClientInterface) session.getClient(), keyReceipt);
        
        SHrsFormerReceipt hrsFormerReceipt = hrsFormerPayroll.getChildReceipts().get(0); // there is allways only one receipt
        
        hrsFormerReceipt.setParentPayroll(hrsFormerPayroll);
        hrsFormerReceipt.setFechaEdicion(session.getCurrentDate());
        hrsFormerReceipt.setMoneda(session.getSessionCustom().getLocalCurrencyCode()); 
        hrsFormerReceipt.setLugarExpedicion(((SClientInterface) session.getClient()).getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
        hrsFormerReceipt.setRegimenFiscal(((SClientInterface) session.getClient()).getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());
        
        SDataCfd cfd = computeCfdi(session, hrsFormerReceipt, keyReceipt[2], true);
        if (cfd == null) {
            throw new Exception("Error al leer el CFD, no se encontró el registro.");
        }

        if (((SClientInterface) session.getClient()).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
            SCfdUtils.signAndSendCfdi((SClientInterface) session.getClient(), cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, false, false);
        }
        else {
            SCfdUtils.signCfdi((SClientInterface) session.getClient(), cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, false, false);
        }
    }
    
    public static int getCfdCountByDepartment(final SGuiSession session, final int payrollId, final int departmentId) throws Exception {
        int count = 0;

        String sql = "SELECT COUNT(c.id_cfd) "
                + "FROM trn_cfd AS c "
                + "INNER JOIN hrs_pay_rcp_iss AS pri ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + "INNER JOIN hrs_pay_rcp AS pr ON c.fid_pay_rcp_pay_n = pr.id_pay AND c.fid_pay_rcp_emp_n = pr.id_emp "
                + "WHERE c.fid_pay_rcp_pay_n = " + payrollId + " AND NOT (c.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " AND NOT c.b_con) AND "
                + "pri.fk_st_rcp = " + SModSysConsts.TRNS_ST_DPS_EMITED + " AND NOT pri.b_del AND "
                + "pr.fk_dep = " + departmentId + " AND NOT pr.b_del;";

        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
                        
        return count;
    }
}
