package erp.mfin.data.diot;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mcfg.data.SCfgUtils;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataTax;
import java.sql.Statement;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDiotUtils {
    
    private static String[] DiotAccounts;
    
    public static String[] getDiotAccounts(final Statement statement) throws Exception {
        return SLibUtils.textsTrim(SLibUtils.textExplode(SCfgUtils.getParamValue(statement, SDataConstantsSys.CFG_PARAM_DIOT_ACCOUNTS), ";"));
    }
    
    public static boolean isDiotAccount(final Statement statement, final SDataAccount account) throws Exception {
        if (DiotAccounts == null) {
            DiotAccounts = getDiotAccounts(statement);
        }
        
        return account.getFkAccountLedgerTypeId() == SDataConstantsSys.FINU_TP_ACC_LEDGER_VAT_CREDITABLE || SLibUtils.belongsTo(account.getCode(), DiotAccounts);
    }
    
    public static int[] getDiotVatDefaultKey(final Statement statement) throws Exception {
        int[] vatDefaultPk = null;
        String param = SCfgUtils.getParamValue(statement, SDataConstantsSys.CFG_PARAM_DIOT_VAT_DEFAULT);
        
        if (!param.isEmpty()) {
            String[] pk = SLibUtils.textsTrim(SLibUtils.textExplode(param, ";"));
            vatDefaultPk = new int[] { SLibUtils.parseInt(pk[0]), SLibUtils.parseInt(pk[1]) };
        }
        
        return vatDefaultPk;
    }
    
    public static SDataTax getDiotVatDefault(final SClientInterface client) throws Exception {
        int[] vatDefaultPk = getDiotVatDefaultKey(client.getSession().getStatement());
        
        return vatDefaultPk == null ? null : (SDataTax) SDataUtilities.readRegistry(client, SDataConstants.FINU_TAX, vatDefaultPk, SLibConstants.EXEC_MODE_STEALTH);
    }
    
    public static int[] getDiotTaxRegionsIgnored(final Statement statement) throws Exception {
        int[] keys = null;
        String[] values = SLibUtils.textsTrim(SLibUtils.textExplode(SCfgUtils.getParamValue(statement, SDataConstantsSys.CFG_PARAM_DIOT_TAX_REGS_IGNORED), ";"));
        
        if (values != null && values.length > 0 && !values[0].isEmpty()) {
            keys = new int[values.length];
            
            for (int i = 0; i < values.length; i++) {
                keys[i] = SLibUtils.parseInt(values[i]);
            }
        }
        
        return keys;
    }
}
