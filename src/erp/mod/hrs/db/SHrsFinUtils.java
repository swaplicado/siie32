/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataRecord;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinUtils;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Irving Sánchez, Sergio Flores
 */
public abstract class SHrsFinUtils {
    
    public static final DecimalFormat RecordNumberFormat = new DecimalFormat(SLibUtils.textRepeat("0", SDataConstantsSys.NUM_LEN_FIN_REC));

    /**
     * Check if payroll can be opened.
     * @param session GUI session.
     * @param payrollId Payroll ID.
     * @return <code>true</code> if payroll can be opened, otherwise <code>false</code>.
     * @throws Exception 
     */
    public static boolean canOpenPayroll(final SGuiSession session, final int payrollId) throws Exception {
        String sql = "SELECT DISTINCT r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num "
                + "FROM fin_rec r "
                + "INNER JOIN fin_rec_ety re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "WHERE fid_pay_n =  " + payrollId + "  AND NOT r.b_del AND NOT re.b_del;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                Object [] pk = new Object[] {
                    resultSet.getInt("r.id_year"),
                    resultSet.getInt("r.id_per"),
                    resultSet.getInt("r.id_bkc"),
                    resultSet.getString("r.id_tp_rec"),
                    resultSet.getInt("r.id_num") };

                SDataRecord record = new SDataRecord();
                record.read(pk, session.getStatement().getConnection().createStatement());
                if (!SDataUtilities.isPeriodOpen((SClientInterface) session.getClient(), record.getDate())) {
                    throw new Exception("El periodo contable de la póliza ' " + record.getRecordNumber() + "' se encuentra cerrado.");
                }
            }
        }
        
        return true;
    }
    
    /**
     * Delete payroll record entries.
     * @param session
     * @param payrollId
     * @throws Exception 
     */
    public static void deletePayrollRecordEntries(final SGuiSession session, final int payrollId) throws Exception {
        String sql;
        
        sql = "UPDATE fin_rec_ety SET "
                + "b_del = 1, "
                + "fid_usr_del = " + session.getUser().getPkUserId() + ", ts_del = now() "
                + "WHERE fid_pay_n = " + payrollId + " AND NOT b_del;";
        
        session.getStatement().execute(sql);
        
        sql = "UPDATE hrs_acc_pay SET "
                + "b_del = 1, "
                + "fk_usr_upd = " + session.getUser().getPkUserId() + ", ts_usr_upd = now() "
                + "WHERE id_pay = " + payrollId + " AND NOT b_del;";
        
        session.getStatement().execute(sql);
    }
    
    /**
     * Get last alive accounting payroll for requested payroll.
     * @param session GUI session.
     * @param payrollId ID of requested payroll.
     * @return Last alive accounting payroll, if any, otherwise <code>null</code>.
     * @throws Exception 
     */
    public static SDbAccountingPayroll getLastAccountingPayroll(final SGuiSession session, final int payrollId) throws Exception {
        SDbAccountingPayroll accountingPayroll = null;
        
        String sql = "SELECT id_acc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_PAY) + " "
                + "WHERE id_pay = " + payrollId + " AND NOT b_del "
                + "ORDER BY id_acc DESC;";
        
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            if (resultSet.next()) {
                accountingPayroll = (SDbAccountingPayroll) session.readRegistry(SModConsts.HRS_ACC_PAY, new int[] { payrollId, resultSet.getInt("id_acc") });
            }
        }
        
        return accountingPayroll;
    }
    
    /**
     * Get accounting records of requested payroll.
     * @param session User session.
     * @param payrollId ID of payroll.
     * @param receipts Number of receipts in payroll.
     * @return If existing, accounting records of requested payroll, otherwise an empty string is returned.
     * @throws java.lang.Exception
     */
    public static String getAccountingRecords(final SGuiSession session, final int payrollId, final int receipts) throws Exception {
        int bookkept = 0;
        String records = "";
        
        String sql = "SELECT r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, "
                + "r.dt, r.concept, bkc.code, cob.code, COUNT(*) AS _count "
                + "FROM hrs_pay AS p "
                + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                + "INNER JOIN hrs_acc_pay AS ap ON ap.id_pay = p.id_pay "
                + "INNER JOIN hrs_acc_pay_rcp AS apr ON apr.id_pay = ap.id_pay AND apr.id_acc = ap.id_acc AND apr.id_emp = pr.id_emp "
                + "INNER JOIN fin_rec AS r ON r.id_year = apr.fid_rec_year AND r.id_per = apr.fid_rec_per AND r.id_bkc = apr.fid_rec_bkc AND r.id_tp_rec = apr.fid_rec_tp_rec AND r.id_num = apr.fid_rec_num "
                + "INNER JOIN fin_bkc AS bkc ON bkc.id_bkc = r.id_bkc "
                + "INNER JOIN erp.bpsu_bpb AS cob ON cob.id_bpb = r.fid_cob "
                + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT ap.b_del AND "
                + "p.id_pay = " + payrollId + " "
                + "GROUP BY r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, r.concept "
                + "ORDER BY r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, r.concept;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                records += (records.isEmpty() ? "" : "\n")
                        + "- En la póliza contable '"
                        + SLibUtils.DecimalFormatCalendarYear.format(resultSet.getInt("r.id_year")) + "-"
                        + SLibUtils.DecimalFormatCalendarMonth.format(resultSet.getInt("r.id_per")) + " "
                        + resultSet.getString("bkc.code") + " "
                        + resultSet.getString("cob.code") + " "
                        + resultSet.getString("r.id_tp_rec") + "-"
                        + RecordNumberFormat.format(resultSet.getInt("r.id_num")) + "', "
                        + "del " + SLibUtils.DateFormatDate.format(resultSet.getDate("r.dt")) + ", "
                        + "\"" + resultSet.getString("r.concept") + "\", hay "
                        + (resultSet.getInt("_count") == 1 ? "un recibo" : SLibUtils.DecimalFormatInteger.format(resultSet.getInt("_count")) + " recibos") + ".";
                bookkept += resultSet.getInt("_count");
            }
        }
        
        records += (records.isEmpty() ? "" : "\n")
                + "Número de recibos contabilizados: " + SLibUtils.DecimalFormatInteger.format(bookkept) + " de " + SLibUtils.DecimalFormatInteger.format(receipts) + " "
                + "(" + SLibUtils.DecimalFormatPercentage2D.format(receipts != 0 ? bookkept / (double) receipts : 0) + ").\n"
                + (bookkept < receipts ? "Número de recibos por contabilizar: " + SLibUtils.DecimalFormatInteger.format(receipts - bookkept) + "." : "");
        
        return records;
    }
    
    /**
     * Actualizar las configuraciones de contabilización faltantes de percepciones y deducciones (procesamiento original), así como borrar las configuraciones obsoletas.
     * @param session User session.
     * @throws java.lang.Exception
     */
    public static void restoreAccountingSettings(final SGuiSession session) throws Exception {
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            // Recuperar configuraciones de contabilización cuyas percepciones o deducciones estén activas:
            
            int[] accountingTypes = new int[] { SModSysConsts.HRSS_TP_ACC_GBL, SModSysConsts.HRSS_TP_ACC_DEP, SModSysConsts.HRSS_TP_ACC_EMP };
            
            String[] updatesUndel = new String[accountingTypes.length];
            for (int i = 0; i < updatesUndel.length; i++) {
                updatesUndel[i] = "UPDATE hrs_acc_ear SET b_del = 0, fk_usr_upd = " + session.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                        + "WHERE id_tp_acc = " + accountingTypes[i] + " AND b_del AND id_ear IN (SELECT id_ear FROM hrs_ear WHERE fk_tp_acc_cfg = " + accountingTypes[i] + " AND NOT b_del);";
            }
            
            // Borrar configuraciones de contabilización cuyos departamentos estén eliminados o cuyos empleados estén eliminados o inactivos:
            
            String[] updatesDel = new String[] {
                "UPDATE hrs_acc_ear SET b_del = 1, fk_usr_upd = " + session.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                    + "WHERE NOT b_del AND id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_DEP + " AND id_ref IN (SELECT id_dep FROM erp.hrsu_dep WHERE b_del);",
                "UPDATE hrs_acc_ear SET b_del = 1, fk_usr_upd = " + session.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                    + "WHERE NOT b_del AND id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_EMP + " AND id_ref IN (SELECT id_emp FROM erp.hrsu_emp WHERE b_del OR NOT b_act);"
            };
            
            // Crear configuraciones de contabilización faltantes:
            
            String[] inserts = new String[] {
                "INSERT INTO hrs_acc_ear "
                    + "SELECT c.id_ear, " + SModSysConsts.HRSS_TP_ACC_GBL + ", 0, 0, 1, NULL, NULL, NULL, NULL, NULL, " + session.getUser().getPkUserId() + ", 1, NOW(), NOW() "
                    + "FROM hrs_ear AS c "
                    + "WHERE NOT c.b_del AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_GBL + " AND "
                    + "NOT EXISTS (SELECT * FROM hrs_acc_ear AS ac WHERE ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_GBL + " AND ac.id_ear = c.id_ear AND ac.id_ref = 0) "
                    + "ORDER BY c.id_ear;",
                "INSERT INTO hrs_acc_ear "
                    + "SELECT c.id_ear, " + SModSysConsts.HRSS_TP_ACC_DEP + ", d.id_dep, 0, 1, NULL, NULL, NULL, NULL, NULL, " + session.getUser().getPkUserId() + ", 1, NOW(), NOW() "
                    + "FROM hrs_ear AS c "
                    + "INNER JOIN erp.hrsu_dep AS d "
                    + "WHERE NOT c.b_del AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_DEP + " AND NOT d.b_del AND "
                    + "NOT EXISTS (SELECT * FROM hrs_acc_ear AS ac WHERE ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_DEP + " AND ac.id_ear = c.id_ear AND ac.id_ref = d.id_dep) "
                    + "ORDER BY c.id_ear, d.id_dep;",
                "INSERT INTO hrs_acc_ear "
                    + "SELECT c.id_ear, " + SModSysConsts.HRSS_TP_ACC_EMP + ", e.id_emp, 0, 1, NULL, NULL, NULL, NULL, NULL, " + session.getUser().getPkUserId() + ", 1, NOW(), NOW() "
                    + "FROM hrs_ear AS c "
                    + "INNER JOIN erp.hrsu_emp AS e "
                    + "INNER JOIN hrs_emp_member AS em ON em.id_emp = e.id_emp "
                    + "WHERE NOT c.b_del AND c.fk_tp_acc_cfg = " + SModSysConsts.HRSS_TP_ACC_EMP + " AND NOT e.b_del AND e.b_act AND "
                    + "NOT EXISTS (SELECT * FROM hrs_acc_ear AS ac WHERE ac.id_tp_acc = " + SModSysConsts.HRSS_TP_ACC_EMP + " AND ac.id_ear = c.id_ear AND ac.id_ref = e.id_emp) "
                    + "ORDER BY c.id_ear, e.id_emp;"
            };
            
            int i = 0;
            String[] sqls = new String[updatesUndel.length + updatesDel.length + inserts.length];
            
            for (String sql : updatesUndel) {
                sqls[i++] = sql;
            }
            for (String sql : updatesDel) {
                sqls[i++] = sql;
            }
            for (String sql : inserts) {
                sqls[i++] = sql;
            }
            
            for (String sql : sqls) {
                statement.execute(sql); // para percepciones
                statement.execute(sql.replaceAll("_ear", "_ded")); // para deducciones
            }
        }
    }
    
    /**
     * Validate if exists any accounting settings (original processing) for all earning and deduction of the payroll.
     * @param session User GUI session.
     * @param payrollId payroll ID to validate.
     * @return
     * @throws Exception 
     */
    public static boolean validateAccountingSettingsForPayroll(final SGuiSession session, final int payrollId) throws Exception {
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            try (Statement statementAcc = session.getStatement().getConnection().createStatement()) {
                String sql = "SELECT DISTINCT " + SModConsts.HRS_ACC_EAR + " AS _move_type, "
                        + "e.id_ear AS _concept_id, e.code AS _code, e.name AS _name, e.name_abbr AS _name_abbr "
                        + "FROM hrs_pay AS p "
                        + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                        + "INNER JOIN hrs_pay_rcp_ear AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                        + "INNER JOIN hrs_ear AS e ON e.id_ear = pre.fk_ear "
                        + "WHERE p.id_pay = " + payrollId + " AND NOT pr.b_del AND "
                            + "pr.id_emp NOT IN (" // exclude payroll receipts already bookkept
                            + "SELECT apr.id_emp "
                            + "FROM hrs_acc_pay AS ap "
                            + "INNER JOIN hrs_acc_pay_rcp AS apr ON apr.id_pay = ap.id_pay "
                            + "WHERE ap.id_pay = " + payrollId + " AND NOT ap.b_del ORDER BY apr.id_emp) "
                        + "UNION "
                        + "SELECT DISTINCT " + SModConsts.HRS_ACC_DED + " AS _move_type, "
                        + "d.id_ded AS _concept_id, d.code AS _code, d.name AS _name, d.name_abbr AS _name_abbr "
                        + "FROM hrs_pay AS p "
                        + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                        + "INNER JOIN hrs_pay_rcp_ded AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                        + "INNER JOIN hrs_ded AS d ON d.id_ded = prd.fk_ded "
                        + "WHERE p.id_pay = " + payrollId + " AND NOT pr.b_del AND "
                        + "pr.id_emp NOT IN (" // exclude payroll receipts already bookkept
                            + "SELECT apr.id_emp "
                            + "FROM hrs_acc_pay AS ap "
                            + "INNER JOIN hrs_acc_pay_rcp AS apr ON apr.id_pay = ap.id_pay "
                            + "WHERE ap.id_pay = " + payrollId + " AND NOT ap.b_del ORDER BY apr.id_emp)"
                        + ";";

                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    int moveType = resultSet.getInt("_move_type");
                    int conceptId = resultSet.getInt("_concept_id");
                    String conceptType = "";
                    String code = resultSet.getString("_code");
                    String name = resultSet.getString("_name");
                    String nameAbbr = resultSet.getString("_name_abbr");

                    switch (moveType) {
                        case SModConsts.HRS_ACC_EAR:
                            conceptType = "percepción";

                            sql = "SELECT COUNT(*) AS _count "
                                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EAR) + " AS e "
                                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_EAR) + " AS ae ON "
                                    + "ae.id_ear = e.id_ear AND ae.id_tp_acc = e.fk_tp_acc_cfg "
                                    + "WHERE NOT ae.b_del AND ae.id_ear = " + conceptId + ";";
                            break;

                        case SModConsts.HRS_ACC_DED:
                            conceptType = "deducción";

                            sql = "SELECT COUNT(*) AS _count "
                                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DED) + " AS d "
                                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_ACC_DED) + " AS ad ON "
                                    + "ad.id_ded = d.id_ded AND ad.id_tp_acc = d.fk_tp_acc_cfg "
                                    + "WHERE NOT ad.b_del AND ad.id_ded = " + conceptId + ";";
                            break;

                        default:
                            // do nothing
                    }

                    try (ResultSet resultSetAcc = statementAcc.executeQuery(sql)) {
                        if (!resultSetAcc.next() || resultSetAcc.getInt("_count") == 0) {
                            throw new Exception("Falta la configuración de contabilización para la " + conceptType + " código '" + code + "', '" + name + "' '(" + nameAbbr + ")'.");
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    /** Validate that all required settings of accounting configurations (dynamic proccessing) are ready and available.
     * @param session User session.
     * @param payrollId ID of payroll.
     * @param employeeIds Array of ID of employees.
     * @return <code>true</code> if all settings are ready and available.
     * @throws java.lang.Exception
     */
    public static boolean validateCfgAccountingSettings(final SGuiSession session, final int payrollId, final int[] employeeIds) throws Exception {
        try (Statement statement = session.getStatement().getConnection().createStatement()) {
            String sql;
            ResultSet resultSet;
            
            // validate payroll's earnings:
            
            sql = "SELECT DISTINCT e.code, e.name, e.id_ear, e.fk_tp_acc_rec "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY) + " AS p "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP) + " AS pr ON pr.id_pay = p.id_pay "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_EAR) + " AS pre ON pre.id_pay = pr.id_pay AND pre.id_emp = pr.id_emp "
                    + "WHERE NOT p.b_del AND NOT pr.b_del AND NOT pre.b_del "
                    + "AND p.id_pay = " + payrollId + " AND pr.id_emp IN (" + StringUtils.join(ArrayUtils.toObject(employeeIds), ",") + ") "
                    + "ORDER BY e.code, e.name, e.id_ear;";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                switch (resultSet.getInt("e.fi_tp_acc_rec")) {
                    case SModSysConsts.HRSS_TP_ACC_GBL:
                        SDbCfgAccountingEarning cfgAccountingEarning = (SDbCfgAccountingEarning) session.readRegistry(SModConsts.HRS_CFG_ACC_EAR, new int[] { resultSet.getInt("e.id_ear") });
                        break;
                        
                    case SModSysConsts.HRSS_TP_ACC_DEP:
                    case SModSysConsts.HRSS_TP_ACC_EMP:
                        break;
                        
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                                + "(Tipo de registro contable en la configuración de contabilización de la percepción '" + resultSet.getString("e.name") + "'.)");
                }
            }
        }
        
        return true;
    }
    
    /**
     * Get account by its numeric ID.
     * @param session
     * @param accountId
     * @return 
     */
    public static SDataAccount getAccount(final SGuiSession session, final int accountId) {
        String accountPk = SFinUtils.getAccountFormerIdXXX(session, accountId);
        return (SDataAccount) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountPk }, SLibConstants.EXEC_MODE_VERBOSE);
    }
    
    /**
     * Get ledger account from given account.
     * @param session
     * @param account
     * @return 
     */
    public static SDataAccount getAccountLedger(final SGuiSession session, SDataAccount account) {
        return (SDataAccount) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { account.getDbmsPkAccountMajorIdXXX() }, SLibConstants.EXEC_MODE_VERBOSE);
    }
    
    /**
     * Get cost center by its numeric ID.
     * @param session
     * @param costCenterId
     * @return 
     */
    public static SDataCostCenter getCostCenter(final SGuiSession session, final int costCenterId) {
        String costCenterPk = SFinUtils.getCostCenterFormerIdXXX(session, costCenterId);
        return (SDataCostCenter) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_CC, new Object[] { costCenterPk }, SLibConstants.EXEC_MODE_VERBOSE);
    }
    
    /**
     * Validate account.
     * @param session
     * @param account
     * @throws Exception 
     */
    private static void validateAccount(final SGuiSession session, SDataAccount account, final boolean validatingLedger) throws Exception {
        String message = SDataUtilities.validateAccount((SClientInterface) session.getClient(), account, null, validatingLedger);
        if (!message.isEmpty()) {
            String accountPk = (account != null ? "'" + SFinUtils.getAccountFormerIdXXX(session, account.getPkAccountId()) + "' ": "");
            throw new Exception("La cuenta contable " + accountPk + "tiene este inconveniente:\n"
                    + message);
        }
    }
    
    /**
     * Validate cost center.
     * @param session
     * @param costCenter
     * @throws Exception 
     */
    private static void validateCostCenter(final SGuiSession session, SDataCostCenter costCenter) throws Exception {
        String message = SDataUtilities.validateCostCenter((SClientInterface) session.getClient(), costCenter, null);
        if (message.length() != 0) {
            String costCenterPk = (costCenter != null ? "'" + SFinUtils.getCostCenterFormerIdXXX(session, costCenter.getPkCostCenterId()) + "' ": "");
            throw new Exception("La centro de costos " + costCenterPk + "tiene este inconveniente:\n"
                    + message);
        }
    }
    
    public static boolean isAccountTypeForResults(final SDataAccount account) {
        return account.getFkAccountTypeId_r() == SDataConstantsSys.FINS_TP_ACC_RES;
    }
    
    public static boolean isAccountSystemTypeForBizPartner(final SDataAccount account) {
        int[] types = new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS, SDataConstantsSys.FINS_TP_ACC_SYS_CDR, SDataConstantsSys.FINS_TP_ACC_SYS_DBR };
        
        return SLibUtils.belongsTo(account.getFkAccountSystemTypeId(), types);
    }
    
    public static boolean isAccountSystemTypeForTax(final SDataAccount account) {
        int[] types = new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT };
        
        return SLibUtils.belongsTo(account.getFkAccountSystemTypeId(), types);
    }
    
    /**
     * Validate cost center for given account and ledger account.
     * @param session
     * @param account
     * @param accountLedger
     * @param costCenterId
     * @throws Exception 
     */
    public static void validateAccountCostCenter(final SGuiSession session, final SDataAccount account, final SDataAccount accountLedger, final int costCenterId) throws Exception {
        if (account.getIsRequiredCostCenter() || accountLedger.getIsRequiredCostCenter() || isAccountTypeForResults(accountLedger)) {
            if (costCenterId == 0) {
                throw new Exception("La cuenta contable '" + account.getPkAccountIdXXX() + "' tiene este inconveniente:\n"
                        + "Requiere centro de costos, pero no ha sido definido.");
            }
            else {
                SDataCostCenter costCenter = getCostCenter(session, costCenterId);
                validateCostCenter(session, costCenter);
            }
        }
    }
    
    /**
     * Validate item for given account and ledger account.
     * @param account
     * @param accountLedger
     * @param itemId
     * @throws Exception 
     */
    public static void validateAccountItem(final SDataAccount account, final SDataAccount accountLedger, final int itemId) throws Exception {
        if ((account.getIsRequiredItem() || accountLedger.getIsRequiredItem() || isAccountTypeForResults(accountLedger)) && itemId == 0) {
            throw new Exception("La cuenta contable '" + account.getPkAccountIdXXX() + "' tiene este inconveniente:\n"
                    + "Requiere ítem, pero no ha sido definido.");
        }
    }
    
    /**
     * Validate business partner for given account and ledger account.
     * @param account
     * @param accountLedger
     * @param bizPartnerId
     * @throws Exception 
     */
    public static void validateAccountBizPartner(final SDataAccount account, final SDataAccount accountLedger, final int bizPartnerId) throws Exception {
        if ((account.getIsRequiredBizPartner() || accountLedger.getIsRequiredBizPartner() || isAccountSystemTypeForBizPartner(accountLedger)) && bizPartnerId == 0) {
            throw new Exception("La cuenta contable '" + account.getPkAccountIdXXX() + "' tiene este inconveniente:\n"
                    + "Requiere asociado de negocios, pero no ha sido definido.");
        }
    }
    
    /**
     * Validate tax for given account and ledger account.
     * @param account
     * @param accountLedger
     * @param taxBasicId
     * @param taxTaxId
     * @throws Exception 
     */
    public static void validateAccountTax(final SDataAccount account, final SDataAccount accountLedger, final int taxBasicId, final int taxTaxId) throws Exception {
        if (isAccountSystemTypeForTax(accountLedger) && (taxBasicId == 0 || taxTaxId == 0)) {
            throw new Exception("La cuenta contable '" + account.getPkAccountIdXXX() + "' tiene este inconveniente:\n"
                    + "Requiere impuesto, pero no ha sido definido.");
        }
    }
    
    /**
     * Validate payroll accounting configuration.
     * @param session User GUI session.
     * @param accountId ID of account.
     * @param costCenterId ID of cost center. Set to -1 to skip validation of cost center.
     * @param bizPartnerId ID of business partner. Set to -1 to skip validation of business partner.
     * @param itemId ID of item. Set to -1 to skip validation of item.
     * @param taxBasicId ID of basic tax. Tax validation cannot be skipped.
     * @param taxTaxId ID of tax. Tax validation cannot be skipped.
     * @return <code>true</code> if configurations is valid.
     * @throws Exception 
     */
    public static boolean validateAccount(final SGuiSession session, final int accountId, final int costCenterId, final int bizPartnerId, final int itemId, final int taxBasicId, final int taxTaxId) throws Exception {
        SDataAccount account = getAccount(session, accountId);
        validateAccount(session, account, false);
        
        SDataAccount accountLedger = getAccountLedger(session, account);
        validateAccount(session, accountLedger, true);
        
        if (costCenterId != -1) {
            validateAccountCostCenter(session, account, accountLedger, costCenterId);
        }
        
        if (bizPartnerId != -1) {
            validateAccountBizPartner(account, accountLedger, bizPartnerId);
        }
        
        if (itemId != -1) {
            validateAccountItem(account, accountLedger, itemId);
        }
        
        validateAccountTax(account, accountLedger, taxBasicId, taxTaxId);
        
        return true;
    }
}
