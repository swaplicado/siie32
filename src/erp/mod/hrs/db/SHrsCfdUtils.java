/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import cfd.DCfdUtils;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver40.DCfdi40Catalogs;
import erp.cfd.SCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
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
 * @author Irving Sánchez, Juan Barajas, Claudio Peña, Isabel Servín, Sergio Flores
 * 
 * Maintenance Log:
 * 2018-01-02, Sergio Flores:
 *  Implementation of payroll into CFDI 3.3.
 */
public abstract class SHrsCfdUtils {
    
    /**
     * Check if type of contract is for employment.
     * @param tipoContrato SAT code of type of contract.
     * @return <code>true</code> if type of contract is for employment.
     */
    public static boolean isTypeContractForEmployment(final String tipoContrato) {
        return tipoContrato.compareTo(DCfdi40Catalogs.ClaveTipoContratoModalidadTrabajoComisión) <= 0;
    }
    
    /**
     * Check if type of recruitment schema is for employment.
     * @param tipoRegimenContratación SAT code of type of recruitment schema.
     * @return <code>true</code> if type of recruitment schema is for employment.
     */
    public static boolean isTypeRecruitmentSchemaForEmployment(final String tipoRegimenContratación) {
        return tipoRegimenContratación.equals(DCfdi40Catalogs.ClaveTipoRégimenSueldos) || 
                tipoRegimenContratación.equals(DCfdi40Catalogs.ClaveTipoRégimenJubilados) || 
                tipoRegimenContratación.equals(DCfdi40Catalogs.ClaveTipoRégimenPensionados) || 
                tipoRegimenContratación.equals(DCfdi40Catalogs.ClaveTipoRégimenJubiladosOPensionados);
    }
    
    /**
     * Check if type of recruitment schema is for employment.
     * @param tipoRegimenContratación Type of recruitment schema.
     * @return <code>true</code> if type of recruitment schema is for employment.
     */
    public static boolean isTypeRecruitmentSchemaForEmployment(final int tipoRegimenContratación) {
        return SLibUtils.belongsTo(tipoRegimenContratación, new int[] { 
            SModSysConsts.HRSS_TP_REC_SCHE_WAG, 
            SModSysConsts.HRSS_TP_REC_SCHE_RET, 
            SModSysConsts.HRSS_TP_REC_SCHE_RET_PEN, 
            SModSysConsts.HRSS_TP_REC_SCHE_PEN });
    }
    
    private static boolean validateReceiptsPendingCfdi( final SGuiSession session, final int payrollId) throws Exception {
        ArrayList<SDataCfd> cfds = SCfdUtils.getPayrollCfds((SClientInterface) session.getClient(), SCfdConsts.CFDI_PAYROLL_VER_CUR, new int[] { payrollId });
        SCfdUtils.existsCfdiPending((SClientInterface) session.getClient(), cfds);
        return true;
    }
    
    public static boolean canGenetareCfdReceipts(final SGuiSession session, final int payrollId) throws Exception {
        if (((SClientInterface) session.getClient()).getSessionXXX().getCurrentCompanyBranchId() == 0) {
            throw new Exception(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH);
        }
            
        String sql = "SELECT b_clo, b_del FROM hrs_pay WHERE id_pay = " + payrollId + "; ";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                if (!resultSet.getBoolean("b_clo")) {
                    throw new Exception("No se pueden generar los CFDI de nómina, la nómina no está cerrada.");
                }
                else if (resultSet.getBoolean("b_del")) {
                    throw new Exception("No se pueden generar los CFDI de nómina, la nómina está eliminada.");
                }
            }
        }
        
        return validateReceiptsPendingCfdi(session, payrollId);
    }

    /**
     * Obtener la lista de recibos de nómina activos pendientes de timbrar, de la siguiente forma:<br>
     * 1) Por un lado, recibos de nómina cuyas emisiones estén 'emitidas' o 'canceladas', pero sin CFD.<br>
     * 2) Por otro lado, recibos de nómina cuyas emisiones estén 'canceladas', pero con CFD 'nuevo' o 'cancelado' o sin UUID (i.e., sin timbrar, o sea, redundantemente, 'nuevo').<br>
     * <b>NOTA:</b> Al cerrarse la nómina, se crean la emisión de cada recibo de nómina como 'emitida'.
     * @param session
     * @param payrollId
     * @return
     * @throws Exception 
     */
    public static ArrayList<SHrsPayrollEmployeeReceipt> getReceiptsPendig(final SGuiSession session, final int payrollId) throws Exception {
        ArrayList<SHrsPayrollEmployeeReceipt> receipts = new ArrayList<>();
        
        String sql = "SELECT pri.id_pay, pri.id_emp, pri.id_iss, "
                + "p.per_year, p.per, p.num, p.dt_sta, p.dt_end, p.nts, p.fk_tp_pay, "
                + "bp.bp AS _emp_name, emp.num AS _emp_num, dep.name AS _dep_name, pr.fk_dep, pr.ear_r, pr.ded_r, pr.pay_r, "
                + "pri.num_ser, pri.num, pri.uuid_rel, pri.fk_tp_pay_sys, pri.dt_iss, pri.dt_pay "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON p.id_pay = pr.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pr.id_pay = pri.id_pay AND pr.id_emp = pri.id_emp "
                + "AND pri.id_iss = COALESCE((SELECT MAX(prix.id_iss) FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS prix "
                + " WHERE prix.id_pay = pr.id_pay AND prix.id_emp = pr.id_emp AND NOT prix.b_del), 0) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " bp ON bp.id_bp = pr.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = pr.id_emp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = pr.fk_dep "
                + "WHERE p.id_pay = " + payrollId + " "
                + "AND NOT p.b_del AND NOT pr.b_del AND NOT pri.b_del AND pr.b_cfd_req "
                + "AND ("
                + "(pri.fk_st_rcp IN (" + SModSysConsts.TRNS_ST_DPS_EMITED + ", " + SModSysConsts.TRNS_ST_DPS_ANNULED + ") "
                + "AND NOT EXISTS (SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c "
                + " WHERE c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss)) "
                + "OR "
                + "(pri.fk_st_rcp = " + SModSysConsts.TRNS_ST_DPS_ANNULED + " "
                + "AND EXISTS (SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " AS c "
                + " WHERE c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                + " AND (c.fid_st_xml IN (" + SModSysConsts.TRNS_ST_DPS_NEW + " , " + SModSysConsts.TRNS_ST_DPS_ANNULED + ") OR c.uuid = '')))) "
                + "ORDER BY _emp_name, id_emp, id_iss;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
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
                receipt.setEmployeeNumber(resultSet.getString("_emp_num"));
                receipt.setEmployeeName(resultSet.getString("_emp_name"));
                receipt.setDepartmentId(resultSet.getInt("fk_dep"));
                receipt.setDepartment(resultSet.getString("_dep_name"));
                receipt.setTotalEarnings(resultSet.getDouble("ear_r"));
                receipt.setTotalDeductions(resultSet.getDouble("ded_r"));
                receipt.setTotalNet(resultSet.getDouble("pay_r"));
                receipt.setNumberSeries(resultSet.getString("num_ser"));
                receipt.setDateOfIssue(resultSet.getDate("dt_iss"));
                receipt.setDateOfPayment(resultSet.getDate("dt_pay"));
                receipt.setPaymentTypeSysId(resultSet.getInt("fk_tp_pay_sys"));
                receipt.setPaymentTypeSys("");
                receipt.setUuidToSubstitute(resultSet.getString("uuid_rel"));
                
                receipts.add(receipt);
            }
        }
        
        return receipts;
    }
    
    /**
     * Create CFDI payroll.
     * @param session
     * @param hrsFormerReceipt
     * @param receiptIssueId
     * @param cfdiPendingSigned
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("deprecation")
    private static SDataCfd createCfdi(final SGuiSession session, final SHrsFormerReceipt hrsFormerReceipt, final int receiptIssueId, final boolean cfdiPendingSigned) throws Exception {
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
            cfd.ver40.DElementComprobante comprobanteCfdi40 = null;
            
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
                case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                        comprobanteCfdi40 = (cfd.ver40.DElementComprobante) SCfdUtils.createCfdi40RootElement((SClientInterface) session.getClient(), hrsFormerReceipt);
                        cfdVersion = comprobanteCfdi40.getVersion();
                        
                        packet.setCfdStringSigned(DCfdUtils.generateOriginalString(comprobanteCfdi40));
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
                case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                    comprobanteCfdi40.getAttSello().setString(packet.getCfdSignature());
                    packet.setAuxCfdRootElement(comprobanteCfdi40);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            ArrayList<SCfdPacket> cfdPackets = new ArrayList<>();
            cfdPackets.add(packet);

            // end of generating CFDI:
            
            SDbFormerPayrollImport payrollImport = new SDbFormerPayrollImport();
            payrollImport.setPayrollId(hrsFormerReceipt.getParentPayroll().getPkNominaId());
            payrollImport.setRegenerateNonStampedCfdi(cfdiPendingSigned);
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
        
        return SCfdUtils.getPayrollReceiptLastCfd((SClientInterface) session.getClient(), SCfdConsts.CFDI_PAYROLL_VER_CUR, new int[] { hrsFormerReceipt.getParentPayroll().getPkNominaId(), hrsFormerReceipt.getPkEmpleadoId(), receiptIssueId });
    }
    
    /**
     * Read payroll and receipt in former format.
     * @param client
     * @param keyPayrollReceiptIssue
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public static SHrsFormerPayroll readHrsFormerPayrollAndReceipt(final SClientInterface client, final int[] keyPayrollReceiptIssue) throws SQLException, Exception {
        int nPaymentType = 0;
        int nBankDefaultId = 0;
        int nEarningTaxSubsidyCompId = 0;
        String sEarTaxSubsidyCode = "";
        boolean bBankAccountUse = false;
        String sql = "";
        String sqlDeductionsTaxWithheld = "";
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
        
        // Obtain deductions for withheld tax:
        
        sql = "SELECT id_ded "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " "
                + "WHERE fk_tp_ded = " + SModSysConsts.HRSS_TP_DED_TAX + " AND NOT b_del;";
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            sqlDeductionsTaxWithheld += (sqlDeductionsTaxWithheld.isEmpty() ? "" : ", ") + resultSet.getInt("id_ded");
        }

        // Obtain payroll header and receipt main info (this is always a one-row query):

        sql = "SELECT pri.dt_iss, p.dt_sta, p.dt_end, p.per_year, p.num, p.fk_tp_pay, p.fk_tp_pay_sht, "
                + "pr.ear_r, pr.ded_r, "
                + "(SELECT COALESCE(SUM(prd.amt_r), 0.0) "
                + " FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_DED) + " AS prd "
                + " WHERE prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp AND NOT prd.b_del AND prd.fk_ded IN (" + sqlDeductionsTaxWithheld + ")) AS _ded_tax "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp "
                + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pri.b_del AND "
                + "pri.id_pay = " + keyPayrollReceiptIssue[0] + " AND pri.id_emp = " + keyPayrollReceiptIssue[1] + " AND pri.id_iss = " + keyPayrollReceiptIssue[2] + ";";
        
        resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception("No se encontró la nómina.");
        }
        else {
            hrsFormerPayroll = new SHrsFormerPayroll(client);
            hrsFormerPayroll.setPkNominaId(keyPayrollReceiptIssue[0]);
            hrsFormerPayroll.setFecha(resultSet.getDate("pri.dt_iss"));
            hrsFormerPayroll.setFechaInicial(resultSet.getDate("p.dt_sta"));
            hrsFormerPayroll.setFechaFinal(resultSet.getDate("p.dt_end"));
            hrsFormerPayroll.setTotalPercepciones(resultSet.getDouble("pr.ear_r"));
            hrsFormerPayroll.setTotalDeducciones(resultSet.getDouble("pr.ded_r"));
            hrsFormerPayroll.setTotalRetenciones(resultSet.getDouble("_ded_tax"));
            hrsFormerPayroll.setEmpresaId(client.getSession().getConfigCompany().getCompanyId());
            hrsFormerPayroll.setSucursalEmpresaId(client.getSessionXXX().getCurrentCompanyBranchId());
            hrsFormerPayroll.setRegimenFiscal(new String[] { client.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal() });
            hrsFormerPayroll.setFkNominaTipoId(resultSet.getInt("p.fk_tp_pay_sht"));
            
            nPaymentType = resultSet.getInt("p.fk_tp_pay");

            // Obtain employee payroll receipt (this is always a one-row query):

            sql = "SELECT emp.num AS f_emp_num, bp.alt_id AS f_emp_curp, emp.ssn AS f_emp_nss, " +
                    "tsch.code AS f_emp_reg_tp, pr.day_pad AS f_emp_days_pay, dep.name AS f_emp_dep, " +
                    "pr.pay_tax_sub_assd, pr.pay_tax_sub_comp, pr.pay_tax_sub_pend_r, pr.pay_tax_sub_payd, " +
                    (bBankAccountUse ? "emp.bank_acc" : "''") + " AS f_emp_bank_clabe, " +
                    "CASE WHEN emp.fk_bank_n IS NOT NULL THEN emp.fk_bank_n ELSE " + nBankDefaultId + " END AS f_emp_bank, " +
                    "pr.sal, pr.wage, pr.dt_ben AS f_emp_hire, p.dt_sta AS f_pay_start, p.dt_end AS f_pay_end, " +
                    "pri.dt_pay, pri.num_ser, pri.num, pri.uuid_rel, pri.fk_tp_pay_sys, " +
                    "TIMESTAMPDIFF(DAY, pr.dt_ben, p.dt_end) / " + SHrsConsts.WEEK_DAYS + " AS f_emp_sen, pos.name AS f_emp_pos, " +
                    "tcon.code AS f_emp_cont_tp, twkd.code AS f_emp_jorn_tp, tpay.code AS f_emp_pay, pr.sal_ssc AS f_emp_sal_bc, trsk.code AS f_emp_risk, " +
                    "IF(emp.b_uni, '" + DCfdi40Catalogs.TextoSí + "', '" + DCfdi40Catalogs.TextoNo + "') AS f_emp_union, " +
                    "NOW() AS f_emp_date_edit " +
                    "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " AS pri ON pri.id_pay = pr.id_pay AND pri.id_emp = pr.id_emp " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS bp ON bp.id_bp = pr.id_emp " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_PAY) + " AS tpay ON tpay.id_tp_pay = p.fk_tp_pay " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_EMP) + " AS emp ON emp.id_emp = bp.id_bp " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_DEP) + " AS dep ON dep.id_dep = pr.fk_dep " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_POS) + " AS pos ON pos.id_pos = pr.fk_pos " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_CON) + " AS tcon ON tcon.id_tp_con = pr.fk_tp_con " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_REC_SCHE) + " AS tsch ON tsch.id_tp_rec_sche = pr.fk_tp_rec_sche " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_POS_RISK) + " AS trsk ON trsk.id_tp_pos_risk = pr.fk_tp_pos_risk " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_WORK_DAY) + " AS twkd ON pr.fk_tp_work_day = twkd.id_tp_work_day " +
                    "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pri.b_del AND " +
                    "pri.id_pay = " + keyPayrollReceiptIssue[0] + " AND pri.id_emp = " + keyPayrollReceiptIssue[1] + " AND pri.id_iss = " + keyPayrollReceiptIssue[2] + ";";
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
                    "WHERE bpb.b_del = 0 AND bpb.fid_tp_bpb = " + SDataConstantsSys.BPSS_TP_BPB_HQ + " AND bpb.fid_bp = " + keyPayrollReceiptIssue[1] + ";";
                resultSetClient = statementClient.executeQuery(sql);

                if (!resultSetClient.next()) {
                    throw new Exception("No se encontró la sucursal del empleado con ID = " + keyPayrollReceiptIssue[1] + ".");
                }
                else {
                    hrsFormerReceipt = new SHrsFormerReceipt(client, hrsFormerPayroll);
                    hrsFormerReceipt.setPkEmpleadoId(keyPayrollReceiptIssue[1]);
                    hrsFormerReceipt.setAuxEmpleadoId(keyPayrollReceiptIssue[1]);
                    hrsFormerReceipt.setPkSucursalEmpleadoId(resultSetClient.getInt("bpb.id_bpb"));
                    hrsFormerReceipt.setRegistroPatronal(client.getSessionXXX().getParamsCompany().getRegistrySs());
                    hrsFormerReceipt.setEmpleadoNum(SLibUtilities.textTrim(resultSet.getString("f_emp_num")));
                    hrsFormerReceipt.setEmpleadoCurp(SLibUtilities.textTrim(resultSet.getString("f_emp_curp")));
                    hrsFormerReceipt.setTipoRegimen(resultSet.getInt("f_emp_reg_tp"));
                    hrsFormerReceipt.setNumSeguridadSocial(SLibUtilities.textTrim(resultSet.getString("f_emp_nss")));
                    hrsFormerReceipt.setFechaPago(resultSet.getDate("pri.dt_pay"));
                    hrsFormerReceipt.setFechaInicialPago(resultSet.getDate("f_pay_start"));
                    hrsFormerReceipt.setFechaFinalPago(resultSet.getDate("f_pay_end"));
                    hrsFormerReceipt.setNumDiasPagados(resultSet.getDouble("f_emp_days_pay"));
                    hrsFormerReceipt.setDepartamento(SLibUtilities.textTrim(resultSet.getString("f_emp_dep")));
                    hrsFormerReceipt.setBanco(resultSet.getInt("f_emp_bank"));
                    hrsFormerReceipt.setCuentaBancaria(SLibUtilities.textTrim(resultSet.getString("f_emp_bank_clabe")));
                    hrsFormerReceipt.setFechaInicioRelLaboral(resultSet.getDate("f_emp_hire"));
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
                    hrsFormerReceipt.setMoneda(client.getSession().getSessionCustom().getLocalCurrencyCode());
                    hrsFormerReceipt.setLugarExpedicion(client.getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
                    //hrsFormerReceipt.setConfirmacion(...);
                    hrsFormerReceipt.setRegimenFiscal(client.getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());

                    dAmountMonth = SLibUtils.roundAmount(nPaymentType == SModSysConsts.HRSS_TP_PAY_WEE ? (resultSet.getDouble("pr.sal") * SHrsConsts.MONTH_DAYS_FIXED) : resultSet.getDouble("pr.wage"));
                    hrsFormerReceipt.setAuxSueldoMensual(dAmountMonth);
                    
                    if (!resultSet.getString("pri.uuid_rel").isEmpty()) {
                        switch (hrsFormerReceipt.getCfdiVersion()) {
                            case SDataConstantsSys.TRNS_TP_XML_CFDI_33:
                                hrsFormerReceipt.setCfdiRelacionadosTipoRelacion(DCfdi33Catalogs.ClaveTipoRelaciónSustitución);
                                hrsFormerReceipt.getCfdiRelacionados33().add(resultSet.getString("pri.uuid_rel"));
                                break;
                            case SDataConstantsSys.TRNS_TP_XML_CFDI_40:
                                hrsFormerReceipt.getCfdiRelacionados().addRelatedDocument(DCfdi40Catalogs.ClaveTipoRelaciónSustitución, resultSet.getString("pri.uuid_rel"));
                                break;
                            default:
                        }
                    }

                    boolean bTaxSubFound = false;
                    boolean bHasEarningTaxSubComp = false;
                    double dTaxSubEffective = SLibUtils.roundAmount(resultSet.getDouble("pr.pay_tax_sub_comp") + resultSet.getDouble("pr.pay_tax_sub_payd"));

                    // Obtain perceptions:

                    sql = "SELECT e.id_ear AS f_conc_id, e.code AS f_conc_cve, e.name_abbr AS f_conc, e.fk_tp_ear AS f_conc_cfdi, e.unt_fac, " +
                            "CASE WHEN e.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVER_TIME + " THEN CASE WHEN pre.unt >= 0 AND pre.unt < " + SHrsConsts.OVERTIME_2X_MAX_DAYS + " THEN 1 ELSE pre.unt / " + SHrsConsts.OVERTIME_2X_MAX_DAYS + " END ELSE " +
                            "CASE WHEN pre.unt >= 0 AND pre.unt <= 1 THEN 1 ELSE pre.unt END END AS f_conc_qty, " +
                            "CASE WHEN e.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVER_TIME + " THEN " +
                            "CASE WHEN pre.unt >= 0 AND pre.unt <= 1 THEN 1 ELSE pre.unt END ELSE 0 END AS f_conc_hrs, ec.code AS f_conc_unid, " +
                            "pre.b_aut AS f_aut, pre.b_usr AS f_usr, pre.amt_taxa AS f_conc_mont_grav, pre.amt_exem AS f_conc_mont_ext, " +
                            "pre.aux_amt1, pre.aux_amt2, pre.aux_val, pre.fk_ear, pre.fk_tp_oth_pay, top.code AS f_top_code, " +
                            "" + SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[0] + " AS f_conc_tp, " +
                            "CASE WHEN e.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVER_TIME + " AND e.unt_fac = " + SHrsConsts.OVERTIME_2X + " THEN " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_DOUBLE[1] + " ELSE " +
                            "CASE WHEN e.fk_tp_ear = " + SModSysConsts.HRSS_TP_EAR_OVER_TIME + " AND e.unt_fac = " + SHrsConsts.OVERTIME_3X + " THEN " + SCfdConsts.CFDI_PAYROLL_PERCEPTION_EXTRA_TIME_TRIPLE[1] + " ELSE " + 
                            "" + SCfdConsts.CFDI_PAYROLL_PERCEPTION_PERCEPTION[1] + " END END AS f_conc_stp " +
                            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p " +
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay AND pr.b_del = 0 " +
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp AND pre.b_del = 0 " +
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e ON e.id_ear = pre.fk_ear " +
                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_OTH_PAY) + " AS top ON pre.fk_tp_oth_pay = top.id_tp_oth_pay " +
                            "INNER JOIN erp.hrss_tp_ear_comp AS ec ON ec.id_tp_ear_comp = e.fk_tp_ear_comp " +
                            "WHERE p.id_pay = " + keyPayrollReceiptIssue[0] + " AND pr.id_emp = " + keyPayrollReceiptIssue[1] + ";";

                    resultSetAux = statementAux.executeQuery(sql);
                    while (resultSetAux.next()) {
                        dAmountEarTax = resultSetAux.getDouble("f_conc_mont_grav");
                        dAmountEarExe = resultSetAux.getDouble("f_conc_mont_ext");

                        if (dAmountEarTax > 0 || dAmountEarExe > 0) {
                            // check if current earning is a dummy earning created only for acomplishing CFDI requirements:
                            if (resultSetAux.getInt("f_conc_id") == nEarningTaxSubsidyCompId && resultSetAux.getBoolean("f_aut") && !resultSetAux.getBoolean("f_usr") && SLibUtils.roundAmount(dAmountEarTax) == 0 && SLibUtils.compareAmount(SLibUtils.roundAmount(dAmountEarExe), 0.01)) {
                                bHasEarningTaxSubComp = true;
                                continue; // omit current earning because subsidy was totally offset against tax; anyway it will be created later in this current method!
                            }

                            dTotalEar = SLibUtils.roundAmount(dTotalEar + dAmountEarTax + dAmountEarExe);

                            hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();
                            hrsFormerReceiptConcept.setClaveEmpresa(SLibUtilities.textTrim(resultSetAux.getString("f_conc_cve")));
                            hrsFormerReceiptConcept.setClaveOficial(resultSetAux.getInt("f_conc_cfdi"));
                            hrsFormerReceiptConcept.setConcepto(SLibUtilities.textTrim(resultSetAux.getString("f_conc")));
                            hrsFormerReceiptConcept.setCantidad(hrsFormerReceiptConcept.getClaveOficial() == SModSysConsts.HRSS_TP_EAR_OVER_TIME ? Math.ceil(resultSetAux.getDouble("f_conc_qty")) : resultSetAux.getDouble("f_conc_qty"));
                            hrsFormerReceiptConcept.setHoras_r(resultSetAux.getInt("f_conc_hrs"));
                            hrsFormerReceiptConcept.setTotalGravado(dAmountEarTax);
                            hrsFormerReceiptConcept.setTotalExento(dAmountEarExe);
                            hrsFormerReceiptConcept.setPkTipoConcepto(resultSetAux.getInt("f_conc_tp"));
                            hrsFormerReceiptConcept.setPkSubtipoConcepto(resultSetAux.getInt("f_conc_stp"));
                            hrsFormerReceiptConcept.setXtaTipoOtroPagoClave(resultSetAux.getString("f_top_code")); // code of type of other payment when earning is precisely 'other payment', note that when earning is 'tax subsidy' there is not any type of other payment in database registry!
                            hrsFormerReceiptConcept.setXtaTipoOtroPagoId(resultSetAux.getInt("pre.fk_tp_oth_pay"));

                            switch (hrsFormerReceiptConcept.getClaveOficial()) {
                                case SModSysConsts.HRSS_TP_EAR_OVER_TIME:
                                    hrsFormerConceptExtraTime = new SHrsFormerConceptExtraTime();
                                    hrsFormerConceptExtraTime.setTipoHoras(resultSetAux.getInt("unt_fac") == SHrsConsts.OVERTIME_2X ? SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_DOUBLE : SCfdConsts.CFDI_PAYROLL_EXTRA_TIME_TYPE_TRIPLE);
                                    hrsFormerConceptExtraTime.setDias(hrsFormerReceiptConcept.getCantidad());
                                    hrsFormerConceptExtraTime.setHorasExtra(hrsFormerReceiptConcept.getHoras_r());
                                    hrsFormerConceptExtraTime.setImportePagado(hrsFormerReceiptConcept.getTotalImporte());

                                    hrsFormerReceiptConcept.setChildExtraTime(hrsFormerConceptExtraTime);
                                    break;

                                case SModSysConsts.HRSS_TP_EAR_DISAB:
                                    sql = "SELECT tpd.code " +
                                            "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e " +
                                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSU_TP_ABS) + " AS tpa ON e.fk_cl_abs_n = tpa.id_cl_abs AND e.fk_tp_abs_n = tpa.id_tp_abs " +
                                            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRSS_TP_DIS) + " AS tpd ON tpa.fk_tp_dis_n = tpd.id_tp_dis " +
                                            "WHERE e.id_ear = " + resultSetAux.getInt("pre.fk_ear") + " ";

                                    resultSetAuxInc = statementAuxInc.executeQuery(sql);
                                    if (resultSetAuxInc.next()) {
                                        hrsFormerReceiptConcept.setXtaIncapacidadClave(resultSetAuxInc.getString("tpd.code"));
                                    }
                                    break;

                                case SModSysConsts.HRSS_TP_EAR_TAX_SUB:
                                    bTaxSubFound = true;
                                    if (resultSetAux.getDouble("pre.aux_amt1") > 0) {
                                        hrsFormerReceiptConcept.setXtaSubsidioEmpleo(resultSetAux.getDouble("pre.aux_amt1"));
                                    }
                                    else if (dTaxSubEffective > 0) {
                                        hrsFormerReceiptConcept.setXtaSubsidioEmpleo(dTaxSubEffective);
                                    }
                                    hrsFormerReceiptConcept.setXtaTipoOtroPagoClave(DCfdi40Catalogs.ClaveTipoOtroPagoSubsidioEmpleo); // code of type of other payment when earning is tax subsidy!
                                    break;

                                case SModSysConsts.HRSS_TP_EAR_OTH:
                                    switch (hrsFormerReceiptConcept.getXtaTipoOtroPagoId()) {
                                        case SModSysConsts.HRSS_TP_OTH_PAY_TAX_BAL:
                                            hrsFormerReceiptConcept.setXtaCompSaldoAFavor(resultSetAux.getDouble("pre.aux_amt1"));
                                            hrsFormerReceiptConcept.setXtaCompSaldoAFavorRemanente(resultSetAux.getDouble("pre.aux_amt2"));
                                            hrsFormerReceiptConcept.setXtaCompAño(resultSetAux.getInt("pre.aux_val"));
                                            break;
                                            
                                        default:
                                    }
                                    break;
                                    
                                default:
                            }

                            hrsFormerReceipt.getChildConcepts().add(hrsFormerReceiptConcept);
                        }
                    }

                    // Add a dummy earning only for acomplishing CFDI requirements, if it is needed:

                    if (!bTaxSubFound && dTaxSubEffective > 0) {
                        if (!bHasEarningTaxSubComp) {
                            throw new Exception("El recibo no tiene el nodo otro pago para informar del Subsidio para el empleo totalmente compensado.");
                        }

                        hrsFormerReceiptConcept = new SHrsFormerReceiptConcept();
                        hrsFormerReceiptConcept.setClaveEmpresa(sEarTaxSubsidyCode);
                        hrsFormerReceiptConcept.setClaveOficial(SModSysConsts.HRSS_TP_EAR_TAX_SUB);
                        hrsFormerReceiptConcept.setConcepto(SCfdConsts.CFDI_OTHER_PAY_TAX_SUBSIDY_EFF.toUpperCase());
                        hrsFormerReceiptConcept.setCantidad(0);
                        hrsFormerReceiptConcept.setHoras_r(0);
                        hrsFormerReceiptConcept.setTotalGravado(0);
                        hrsFormerReceiptConcept.setTotalExento(0.01); // fixed value when tax subsidy is not actually paid
                        hrsFormerReceiptConcept.setPkTipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_TAX_SUBSIDY[0]);
                        hrsFormerReceiptConcept.setPkSubtipoConcepto(SCfdConsts.CFDI_PAYROLL_PERCEPTION_TAX_SUBSIDY[1]);
                        hrsFormerReceiptConcept.setXtaSubsidioEmpleo(dTaxSubEffective);
                        hrsFormerReceiptConcept.setXtaTipoOtroPagoClave(DCfdi40Catalogs.ClaveTipoOtroPagoSubsidioEmpleo); // code of type of other payment when earning is tax subsidy!

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
                            "WHERE p.id_pay = " + keyPayrollReceiptIssue[0] + " AND pr.b_del = 0 AND prd.b_del = 0 AND pr.id_emp = " + keyPayrollReceiptIssue[1] + ";";

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
        
        resultSet.close();

        return hrsFormerPayroll;
    }
    
    /**
     * Compute CFDI payroll.
     * @param session
     * @param keyPayrollReceiptIssue
     * @return
     * @throws Exception 
     */
    private static SDataCfd computeCfdi(final SGuiSession session, final int[] keyPayrollReceiptIssue, final boolean isSingle) throws Exception {
        SHrsFormerPayroll hrsFormerPayroll = readHrsFormerPayrollAndReceipt((SClientInterface) session.getClient(), keyPayrollReceiptIssue);
        SHrsFormerReceipt hrsFormerReceipt = hrsFormerPayroll.getChildReceipts().get(0); // there is allways only one receipt
        
        hrsFormerReceipt.setParentPayroll(hrsFormerPayroll);
        hrsFormerReceipt.setMoneda(session.getSessionCustom().getLocalCurrencyCode()); 
        hrsFormerReceipt.setLugarExpedicion(((SClientInterface) session.getClient()).getSessionXXX().getCurrentCompanyBranch().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
        hrsFormerReceipt.setRegimenFiscal(((SClientInterface) session.getClient()).getSessionXXX().getParamsCompany().getDbmsDataCfgCfd().getCfdRegimenFiscal());
        
        SDataCfd cfd = createCfdi(session, hrsFormerReceipt, keyPayrollReceiptIssue[2], true);
        
        if (cfd == null) {
            throw new Exception("Error al leer el CFD, no se encontró el registro.");
        }

        if (((SClientInterface) session.getClient()).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
            SCfdUtils.signAndSendCfdi((SClientInterface) session.getClient(), cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, isSingle, false);
        }
        else {
            SCfdUtils.signCfdi((SClientInterface) session.getClient(), cfd, SCfdConsts.CFDI_PAYROLL_VER_CUR, isSingle, false);
        }
        
        return (SDataCfd) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.TRN_CFD, cfd.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT); // read again just signed CFDI
    }
        
    /**
     * Compute CFDI for payroll receipt issue.
     * @param session GUI user session.
     * @param keyPayrollReceiptIssue Primary key of receipt issue.
     * @param isSingle Is processing only one CFDI, otherwise all CFDI in payroll.
     * @return 
     * @throws java.lang.Exception 
     */
    public static SDataCfd computeCfdiPayrollReceiptIssue(final SGuiSession session, final int[] keyPayrollReceiptIssue, final boolean isSingle) throws Exception {
        // discard other non-emitted receipt issues:
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql = "SELECT pri.id_pay, pri.id_emp, pri.id_iss, pri.fk_st_rcp, c.id_cfd, c.fid_st_xml "
                    + "FROM hrs_pay_rcp_iss AS pri "
                    + "LEFT OUTER JOIN trn_cfd AS c ON c.fid_pay_rcp_pay_n = pri.id_pay AND c.fid_pay_rcp_emp_n = pri.id_emp AND c.fid_pay_rcp_iss_n = pri.id_iss "
                    + "WHERE pri.id_pay = " + keyPayrollReceiptIssue[0] + " AND pri.id_emp = " + keyPayrollReceiptIssue[1] + " AND pri.id_iss <> " + keyPayrollReceiptIssue[2] + " AND "
                    + "NOT pri.b_del AND c.id_cfd IS NULL;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SDbPayrollReceiptIssue receiptIssue = (SDbPayrollReceiptIssue) session.readRegistry(SModConsts.HRS_PAY_RCP_ISS, new int[] { resultSet.getInt("pri.id_pay"), resultSet.getInt("pri.id_emp"), resultSet.getInt("pri.id_iss") });
                receiptIssue.setDeleted(true);
                receiptIssue.save(session);
            }
        }
        
        // compute requested CFDI:
        
        SDbPayrollReceiptIssue receiptIssue = (SDbPayrollReceiptIssue) session.readRegistry(SModConsts.HRS_PAY_RCP_ISS, keyPayrollReceiptIssue);
        String series = receiptIssue.getNumberSeries();
        int number = 0;

        if (receiptIssue.getNumber() != 0) {
            // preserve already defined number:
            number = receiptIssue.getNumber();
        }
        else {
            // generate a new number:
            number = SHrsUtils.getPayrollReceiptNextNumber(session, receiptIssue.getNumberSeries());
            receiptIssue.setNumber(number); // update memory
            receiptIssue.saveField(session.getStatement(), receiptIssue.getPrimaryKey(), SDbPayrollReceiptIssue.FIELD_NUMBER, number); // update persistent storage as well
        }
        
        SDataCfd cfd = computeCfdi(session, keyPayrollReceiptIssue, isSingle);
        cfd.setExtraSeries(series);
        cfd.setExtraNumber(number);
        
        return cfd;
    }
    
    /**
     * Get the number of CFD of receipts in payroll by department.
     * @param session
     * @param payrollId
     * @param departmentId
     * @return
     * @throws Exception 
     */
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
    
    /**
     * Get the number of receipts in payroll that do not require a CFD.
     * @param session
     * @param payrollId
     * @return Number of receipts in payroll that do not require a CFD.
     * @throws Exception 
     */
    public static int getReceiptCountCfdNotRequired(final SGuiSession session, final int payrollId) throws Exception {
        int count = 0;
        
        if (payrollId != 0) {
            String sql = "SELECT COUNT(*) "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                    + "WHERE p.id_pay = " + payrollId + " AND p.b_del = 0 AND pr.b_del = 0 AND NOT pr.b_cfd_req;";

            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }
        
        return count;
    }
}
