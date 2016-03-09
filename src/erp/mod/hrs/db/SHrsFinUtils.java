/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataRecord;
import erp.mod.fin.db.SFinUtils;
import java.sql.ResultSet;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Irving Sánchez
 */
public abstract class SHrsFinUtils {
    
    public static boolean canOpenPayRoll(final SGuiSession session, final int payrollId) throws Exception {
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
    
    public static boolean canClosePayRoll(final SGuiSession session, final int payrollId) throws Exception {
        throw new Exception("Not supported yet.");
    }
    
    public static void deletePayRollRecords(final SGuiSession session, final int payrollId) throws Exception {
        String sql = "";
        
        sql = "UPDATE fin_rec_ety SET b_del = 1, fid_usr_del = " + session.getUser().getPkUserId() + ", ts_del = now() "
                + "WHERE fid_pay_n = " + payrollId + ";";
        session.getStatement().execute(sql);
    }
    
    public static boolean isRecordPayroll(final SGuiSession session, final int payrollId) throws Exception {
        boolean isRecord = false;
        String sql = "";
        Object [] pk = null;
        ResultSet resultSet = null;
        
        sql = "SELECT DISTINCT r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num "
                + "FROM fin_rec r "
                + "INNER JOIN fin_rec_ety re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "WHERE fid_pay_n = " + payrollId + " AND r.b_del = 0 AND re.b_del = 0; ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            isRecord = true;
        }
        return isRecord;
    }
    
    public static boolean validateAccount(final SGuiSession session, final int accountId, final int costCenterId, final int bizPartnerId, final int itemId, final int taxBasicId, final int taxTaxId) throws Exception {
        SDataAccount account = null;
        SDataAccount accountMajor = null;
        SDataCostCenter costCenter = null;
        String fk_acc_s = "";
        String fk_cc_s = "";
        String sVal = "";
        
        fk_acc_s = SFinUtils.getAccountFormerIdXXX(session, accountId);
        account = (SDataAccount) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { fk_acc_s }, SLibConstants.EXEC_MODE_VERBOSE);
        
        sVal = SDataUtilities.validateAccount((SClientInterface) session.getClient(), account, null);
        if (sVal.length() != 0) {
            throw new Exception("La cuenta contable ('" + fk_acc_s + "') tiene un inconveniente:\n" + sVal);
        }
        accountMajor = (SDataAccount) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { account.getDbmsPkAccountMajorId() }, SLibConstants.EXEC_MODE_VERBOSE);
        
        if (accountMajor.getIsRequiredCostCenter()) {
            if (costCenterId == SLibConsts.UNDEFINED) {
                throw new Exception("La cuenta contable ('" + fk_acc_s + "') tiene un inconveniente:\nRequiere centro de costos y no está definido.");
            }
            else {
                fk_cc_s = SFinUtils.getCostCenterFormerIdXXX(session, costCenterId);
                costCenter = (SDataCostCenter) SDataUtilities.readRegistry((SClientInterface) session.getClient(), SDataConstants.FIN_CC, new Object[] { fk_cc_s }, SLibConstants.EXEC_MODE_VERBOSE);
                sVal = SDataUtilities.validateCostCenter((SClientInterface) session.getClient(), costCenter, null);
                if (sVal.length() != 0) {
                    throw new Exception("'El centro de costo ('" + fk_cc_s + "') tiene un inconveniente:\n" + sVal);
                }
            }
        }
        if (accountMajor.getIsRequiredBizPartner() && bizPartnerId == SLibConsts.UNDEFINED) {
            throw new Exception("La cuenta contable ('" + fk_acc_s + "') tiene un inconveniente:\nRequiere asociado de negocios y no está definido.");
        }
        if (accountMajor.getIsRequiredBizPartner() && itemId == SLibConsts.UNDEFINED) {
            throw new Exception("La cuenta contable ('" + fk_acc_s + "') tiene un inconveniente:\nRequiere ítem y no está definido.");
        }
        if (accountMajor.getIsRequiredBizPartner() && taxBasicId == SLibConsts.UNDEFINED && taxTaxId == SLibConsts.UNDEFINED) {
            throw new Exception("La cuenta contable ('" + fk_acc_s + "') tiene un inconveniente:\nRequiere impuesto y no está definido.");
        }
        
        return true;
    }
}
