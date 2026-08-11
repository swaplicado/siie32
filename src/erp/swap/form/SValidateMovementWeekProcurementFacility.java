/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import erp.client.SClientInterface;
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
public class SValidateMovementWeekProcurementFacility {
    private boolean Valid;
    private boolean BizPartnerRequired;
    private int BizPartnerType;
    private boolean ItemRequired;
    private final SGuiClient miClient;
    private final SDataAccount oAccount;
    private final SDataAccount oAccountMajor;
    private final SImportWeekMovProcurementFacility oWeekProcurementFacility;
    private String Erros;

    public SValidateMovementWeekProcurementFacility() {
        this.Valid = true;
        this.BizPartnerRequired = false;
        this.BizPartnerType = 0;
        this.ItemRequired = false;
        this.miClient = null;
        this.oAccount = null;
        this.oAccountMajor = null;
        this.oWeekProcurementFacility = null;
        this.Erros = "";
    }
    
    public SValidateMovementWeekProcurementFacility(SGuiClient miClient, SDataAccount oAccount, SDataAccount oAccountMajor) {
        this.miClient = miClient;
        this.oAccount = oAccount;
        this.oAccountMajor = oAccountMajor;
        this.oWeekProcurementFacility = null;
        this.Erros = "";
        this.validateMovementWeekProcurementFacility();
    }
    
    public SValidateMovementWeekProcurementFacility(SGuiClient miClient, SImportWeekMovProcurementFacility oWeekProcurementFacility) {
        this.miClient = miClient;
        this.oAccount = oWeekProcurementFacility.getDataAccount();
        this.oAccountMajor = oWeekProcurementFacility.getDataAccountMajor();
        this.oWeekProcurementFacility = oWeekProcurementFacility;
        this.validateMovementWeekProcurementFacility();
    }

    public int getBizPartnerType() {
        return BizPartnerType;
    }

    public boolean isBizPartnerRequired() {
        return BizPartnerRequired;
    }

    public boolean isItemRequired() {
        return ItemRequired;
    }

    public boolean isValid() {
        return Valid;
    }

    public String getErros() {
        return Erros;
    }
    
    private void validateMovementWeekProcurementFacility() {
        boolean mbIsCurrentAccountDiogAccount = false;
        this.Valid = true;
        
        if (oAccountMajor == null) {
            this.Valid = false;
        }
        else {
            int mnAccountSystemTypeId = oAccountMajor.getFkAccountSystemTypeId();
            int[] anAccountSubclass = new int[] { oAccountMajor.getFkAccountTypeId_r(), oAccountMajor.getFkAccountClassId_r(), oAccountMajor.getFkAccountSubclassId_r() };
            boolean isAccShortTermDoc = oAccountMajor.getFkAccountSpecializedTypeId() == SDataConstantsSys.FINS_TP_ACC_SPE_DOC_PAY || oAccountMajor.getFkAccountSpecializedTypeId() == SDataConstantsSys.FINS_TP_ACC_SPE_DOC_REC;
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
                this.BizPartnerRequired = true;
                
                if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_SUP || isAccSysPurchases || mbIsCurrentAccountDiogAccount) {
                    this.BizPartnerType = SDataConstants.BPSX_BP_SUP;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_CUS || isAccSysSales) {
                    this.BizPartnerType = SDataConstants.BPSX_BP_CUS;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_CDR) {
                    this.BizPartnerType = SDataConstants.BPSX_BP_CDR;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_DBR) {
                    this.BizPartnerType = SDataConstants.BPSX_BP_DBR;
                }
                else if (isAccSysTax) {
                    this.BizPartnerType = SDataConstants.BPSX_BP_X_SUP_CUS; // suppliers and customers!
                }
                else {
                    switch (oAccountMajor.getFkAccountLedgerTypeId()) {
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_CUS:
                            this.BizPartnerType = SDataConstants.BPSX_BP_CUS;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_DBR:
                            this.BizPartnerType = SDataConstants.BPSX_BP_X_CUS_DBR;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_SUP:
                            this.BizPartnerType = SDataConstants.BPSX_BP_SUP;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_CDR:
                            this.BizPartnerType = SDataConstants.BPSX_BP_X_SUP_CDR;
                            break;
                        default:
                            this.BizPartnerType = SDataConstants.BPSU_BP; // all business partners!
                    }
                }
            }
            
            if (oAccountMajor.getIsRequiredItem() || isAccSysBizPartnerSupCus || isAccSysPurchases || isAccSysSales || isAccSysTax) {
                this.ItemRequired = true;
            }
            
            if (oWeekProcurementFacility != null) {
                SClientInterface client = (SClientInterface) miClient;
                String DateFormated = client.getSessionXXX().getFormatters().getDbmsDateFormat().format(oWeekProcurementFacility.getMovement_date());
                this.Erros = "Movimiento: Concepto - " + oWeekProcurementFacility.getConcept() + ", Fecha - " + DateFormated + ".\n";
                if (this.BizPartnerRequired && this.oWeekProcurementFacility.getDataBizPartner() == null) {
                    this.Valid = false;
                    this.Erros += "* El asociado de negocios es requerido.\n";
                }
                
                if (this.ItemRequired && this.oWeekProcurementFacility.getItem() == null) {
                    this.Valid = false;
                    this.Erros += "* El item es requerido.\n";
                }
                
                if (this.Valid) {
                    this.Erros = "";
                }
            }
        }
    }
}
