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
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataRecord;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinUtils;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Irving Sánchez, Sergio Flores
 */
public abstract class SHrsFinUtils {
    
    public static boolean canOpenPayroll(final SGuiSession session, final int payrollId) throws Exception {
        String sql = "";
        Object [] pk = null;
        SDataRecord moRecord = null;     
        ResultSet resultSet = null;
        
        sql = "SELECT DISTINCT r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num "
                + "FROM fin_rec r "
                + "INNER JOIN fin_rec_ety re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "WHERE fid_pay_n =  " + payrollId + "  AND r.b_del = 0 AND re.b_del = 0 ; ";
        
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            moRecord = new SDataRecord();
            pk = new Object[5];
            pk [0] = resultSet.getInt("r.id_year");
            pk [1] = resultSet.getInt("r.id_per");
            pk [2] = resultSet.getInt("r.id_bkc");
            pk [3] = resultSet.getString("r.id_tp_rec");
            pk [4] = resultSet.getInt("r.id_num");
            
            moRecord.read(pk, session.getStatement().getConnection().createStatement());
            if (!SDataUtilities.isPeriodOpen((SClientInterface) session.getClient(), moRecord.getDate())) {
                //Period is close
                throw new Exception("El periodo contable de la póliza ' " + moRecord.getRecordNumber() + "' se encuentra cerrado.");
            }
        }
        
        return true;
    }
    
    public static void deletePayrollRecordEntries(final SGuiSession session, final int payrollId) throws Exception {
        String sql = "";
        
        sql = "UPDATE fin_rec_ety SET "
                + "b_del = 1, "
                + "fid_usr_del = " + session.getUser().getPkUserId() + ", ts_del = now() "
                + "WHERE fid_pay_n = " + payrollId + ";";
        session.getStatement().execute(sql);
    }
    
    /**
     * Validate if exists any accounting settings for all earning and deduction of the payroll.
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
                        + "WHERE p.id_pay = " + payrollId + " AND NOT pr.b_del "
                        + "UNION "
                        + "SELECT DISTINCT " + SModConsts.HRS_ACC_DED + " AS _move_type, "
                        + "d.id_ded AS _concept_id, d.code AS _code, d.name AS _name, d.name_abbr AS _name_abbr "
                        + "FROM hrs_pay AS p "
                        + "INNER JOIN hrs_pay_rcp AS pr ON pr.id_pay = p.id_pay "
                        + "INNER JOIN hrs_pay_rcp_ded AS prd ON prd.id_pay = pr.id_pay AND prd.id_emp = pr.id_emp "
                        + "INNER JOIN hrs_ded AS d ON d.id_ded = prd.fk_ded "
                        + "WHERE p.id_pay = " + payrollId + " AND NOT pr.b_del;";

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
    
    public static boolean validateAccount(final SGuiSession session, final int accountId, final int costCenterId, final int bizPartnerId, final int itemId, final int taxBasicId, final int taxTaxId) throws Exception {
        String validationMsg;
        
        String accountPk = SFinUtils.getAccountFormerIdXXX(session, accountId);
        
        SDataAccount account = (SDataAccount) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountPk }, SLibConstants.EXEC_MODE_VERBOSE);
        validationMsg = SDataUtilities.validateAccount((SClientInterface) session.getClient(), account, null, false);
        if (!validationMsg.isEmpty()) {
            throw new Exception("La cuenta contable ('" + accountPk + "') tiene un inconveniente:\n" + validationMsg);
        }
        
        SDataAccount accountLedger = (SDataAccount) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { account.getDbmsPkAccountMajorId() }, SLibConstants.EXEC_MODE_VERBOSE);
        validationMsg = SDataUtilities.validateAccount((SClientInterface) session.getClient(), accountLedger, null, true);
        if (!validationMsg.isEmpty()) {
            throw new Exception("La cuenta contable de mayor ('" + account.getDbmsPkAccountMajorId() + "') tiene un inconveniente:\n" + validationMsg);
        }
        
        if (account.getIsRequiredCostCenter() || accountLedger.getIsRequiredCostCenter() || accountLedger.getFkAccountTypeId_r() == SDataConstantsSys.FINS_TP_ACC_RES) {
            if (costCenterId == SLibConsts.UNDEFINED) {
                throw new Exception("La cuenta contable ('" + accountPk + "') tiene un inconveniente:\nRequiere centro de costos y no está definido.");
            }
            else {
                String costCenterPk = SFinUtils.getCostCenterFormerIdXXX(session, costCenterId);
                
                SDataCostCenter costCenter = (SDataCostCenter) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_CC, new Object[] { costCenterPk }, SLibConstants.EXEC_MODE_VERBOSE);
                validationMsg = SDataUtilities.validateCostCenter((SClientInterface) session.getClient(), costCenter, null);
                if (validationMsg.length() != 0) {
                    throw new Exception("'El centro de costo ('" + costCenterPk + "') tiene un inconveniente:\n" + validationMsg);
                }
            }
        }
        
        int nSystemType = accountLedger.getFkAccountSystemTypeId();
        
        if ((accountLedger.getIsRequiredBizPartner() || SLibUtilities.belongsTo(nSystemType, new int[] {
            SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS, SDataConstantsSys.FINS_TP_ACC_SYS_CDR, SDataConstantsSys.FINS_TP_ACC_SYS_DBR })) &&
                bizPartnerId == SLibConsts.UNDEFINED) {
            throw new Exception("La cuenta contable ('" + accountPk + "') tiene un inconveniente:\nRequiere asociado de negocios y no está definido.");
        }
        if ((accountLedger.getIsRequiredItem() || accountLedger.getFkAccountTypeId_r() == SDataConstantsSys.FINS_TP_ACC_RES) &&
                itemId == SLibConsts.UNDEFINED) {
            throw new Exception("La cuenta contable ('" + accountPk + "') tiene un inconveniente:\nRequiere ítem y no está definido.");
        }
        if (SLibUtilities.belongsTo(nSystemType, new int[] {
            SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT }) &&
                taxBasicId == SLibConsts.UNDEFINED && taxTaxId == SLibConsts.UNDEFINED) {
            throw new Exception("La cuenta contable ('" + accountPk + "') tiene un inconveniente:\nRequiere impuesto y no está definido.");
        }
        
        return true;
    }
    
    /**
     * Actualizar las configuraciones de contabilización faltantes de percepciones y deducciones, así como borrar las configuraciones obsoletas.
     * @param session User session.
     * @throws Exception 
     */
    public static void updateAccountingConfigs(final SGuiSession session) throws Exception {
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
}
