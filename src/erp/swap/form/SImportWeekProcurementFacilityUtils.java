/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.diot.SDiotUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Adrian Aviles
 */
public class SImportWeekProcurementFacilityUtils {
    
    public static boolean validateMovementWeekProcurementFacility(SGuiClient miClient, SDataAccount oAccount, SDataAccount oAccountMajor ) {
        boolean validate = true;
        boolean mbIsCurrentAccountDiogAccount = false;
        boolean mbIsBizPartnerRequired = false;
        int mnOptionsBizPartnerType = 0;
        boolean mbIsItemRequired = false;
        
        if (oAccountMajor == null) {
            validate = false;
        }
        else {
            int mnAccountSystemTypeId = oAccountMajor.getFkAccountSystemTypeId();
            int[] anAccountSubclass = new int[] { oAccountMajor.getFkAccountTypeId_r(), oAccountMajor.getFkAccountClassId_r(), oAccountMajor.getFkAccountSubclassId_r() };
            boolean isAccSysBizPartnerAll = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS, SDataConstantsSys.FINS_TP_ACC_SYS_CDR, SDataConstantsSys.FINS_TP_ACC_SYS_DBR });
            boolean isAccSysBizPartnerSupCus = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS });
            boolean isAccSysPurchases = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PUR, SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ });
            boolean isAccClsPurchases = SLibUtilities.belongsTo(anAccountSubclass, new int[][] {SDataConstantsSys.FINS_CLS_ACC_PUR, SDataConstantsSys.FINS_CLS_ACC_PUR_ADJ });
            boolean isAccSysSales = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SAL, SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ });
            boolean isAccClsSales = SLibUtilities.belongsTo(anAccountSubclass, new int[][] {SDataConstantsSys.FINS_CLS_ACC_SAL, SDataConstantsSys.FINS_CLS_ACC_SAL_ADJ });
            boolean isAccSysTax = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT });
            
            try {
                mbIsCurrentAccountDiogAccount = SDiotUtils.isDiotAccount(miClient.getSession().getStatement(), oAccountMajor) || 
                        SDiotUtils.isDiotAccount(miClient.getSession().getStatement(), oAccount);
            }
            catch (Exception e) { }

            // Check if it is necesary to enable business partner fields:

            if (oAccountMajor.getIsRequiredBizPartner() || isAccSysBizPartnerAll || isAccSysPurchases || isAccClsPurchases || isAccSysSales || isAccClsSales || isAccSysTax || mbIsCurrentAccountDiogAccount) {
                mbIsBizPartnerRequired = true;
                
                if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_SUP || isAccSysPurchases || mbIsCurrentAccountDiogAccount) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_SUP;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_CUS || isAccSysSales) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_CUS;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_CDR) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_CDR;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_DBR) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_DBR;
                }
                else if (isAccSysTax) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_X_SUP_CUS; // suppliers and customers!
                }
                else {
                    switch (oAccountMajor.getFkAccountLedgerTypeId()) {
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_CUS:
                            mnOptionsBizPartnerType = SDataConstants.BPSX_BP_CUS;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_DBR:
                            mnOptionsBizPartnerType = SDataConstants.BPSX_BP_X_CUS_DBR;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_SUP:
                            mnOptionsBizPartnerType = SDataConstants.BPSX_BP_SUP;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_CDR:
                            mnOptionsBizPartnerType = SDataConstants.BPSX_BP_X_SUP_CDR;
                            break;
                        default:
                            mnOptionsBizPartnerType = SDataConstants.BPSU_BP; // all business partners!
                    }
                }
            }
            
            if (oAccountMajor.getIsRequiredItem() || isAccSysBizPartnerSupCus || isAccSysPurchases || isAccSysSales || isAccSysTax) {
                mbIsItemRequired = true;
            }
        }
        
        return validate;
    }
}
