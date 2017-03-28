/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod;

/**
 *
 * @author Sergio Flores
 */
public abstract class SModSysConsts {

    /*
     * Configuration
     */

    public static final int CFGS_CT_ENT_CASH = 1;
    public static final int CFGS_CT_ENT_WH = 2;
    public static final int CFGS_CT_ENT_POS = 3;
    public static final int CFGS_CT_ENT_PLANT = 4;

    public static final int[] CFGS_TP_ENT_CASH_CASH = new int[] { 1, 1 };
    public static final int[] CFGS_TP_ENT_CASH_BANK = new int[] { 1, 2 };
    public static final int[] CFGS_TP_ENT_WH_SP = new int[] { 2, 1 };
    public static final int[] CFGS_TP_ENT_WH_MFG_RM = new int[] { 2, 2 };
    public static final int[] CFGS_TP_ENT_WH_MFG_WP = new int[] { 2, 3 };
    public static final int[] CFGS_TP_ENT_WH_MFG_FG = new int[] { 2, 4 };
    public static final int[] CFGS_TP_ENT_WH_GDS = new int[] { 2, 5 };
    public static final int[] CFGS_TP_ENT_WH_WAR_PUR = new int[] { 2, 6 };
    public static final int[] CFGS_TP_ENT_WH_WAR_SAL = new int[] { 2, 7 };
    public static final int[] CFGS_TP_ENT_WH_CSG_PUR = new int[] { 2, 8 };
    public static final int[] CFGS_TP_ENT_WH_CSG_SAL = new int[] { 2, 9 };
    public static final int[] CFGS_TP_ENT_POS_POS = new int[] { 3, 1 };
    public static final int[] CFGS_TP_ENT_PLT_PLT = new int[] { 4, 1 };

    public static final int CFGS_TP_SORT_KEY_NAME = 1;
    public static final int CFGS_TP_SORT_NAME_KEY = 2;
    
    public static final int CFGS_TP_MMS_CFD = 1;
    public static final int CFGS_TP_MMS_CON = 2;
    public static final int CFGS_TP_MMS_ORD_SAL = 3;
    public static final int CFGS_TP_MMS_ORD_PUR = 4;
    public static final int CFGS_TP_MMS_FIN_PAY_AUTH_REQ = 11;

    public static final int CFGU_CUR_MXN = 1;
    public static final int CFGU_CUR_USD = 2;
    public static final int CFGU_CUR_EUR = 3;

    public static final int CFGU_FUNC_NON = 1;
    
    public static final int CFGX_IVM_FIFO = 1;
    public static final int CFGX_IVM_LIFO = 2;
    public static final int CFGX_IVM_AVG = 3;

    /*
     * Users
     */

    public static final int USRS_TP_USR_USR = 1;
    public static final int USRS_TP_USR_ADM = 2;
    public static final int USRS_TP_USR_SUP = 3;

    /*
     * Business partners
     */

    public static final int BPSS_CT_BP_CO = 1;
    public static final int BPSS_CT_BP_SUP = 2;
    public static final int BPSS_CT_BP_CUS = 3;
    public static final int BPSS_CT_BP_CDR = 4;
    public static final int BPSS_CT_BP_DBR = 5;

    public static final int BPSS_TP_BP_IDY_PER = 1;
    public static final int BPSS_TP_BP_IDY_ORG = 2;

    public static final int BPSS_TP_BPB_HQ = 1;
    public static final int BPSS_TP_BPB_B = 2;

    public static final int BPSS_TP_ADD_OFF = 1;
    public static final int BPSS_TP_ADD_OFF_NO = 2;

    public static final int BPSS_TP_ADD_FMT_STD = 1;
    public static final int BPSS_TP_ADD_FMT_US = 2;

    public static final int BPSS_TP_CON_ADM = 1;
    public static final int BPSS_TP_CON_PUR = 2;
    public static final int BPSS_TP_CON_PAY = 3;
    public static final int BPSS_TP_CON_SAL = 4;
    public static final int BPSS_TP_CON_COL = 5;
    public static final int BPSS_TP_CON_OTH = 9;

    public static final int BPSS_TP_TEL_NA = 1;
    public static final int BPSS_TP_TEL_TEL = 2;
    public static final int BPSS_TP_TEL_TEL_FAX = 3;
    public static final int BPSS_TP_TEL_FAX = 4;
    public static final int BPSS_TP_TEL_MOV = 5;
    public static final int BPSS_TP_TEL_OTH = 6;

    public static final int BPSS_TP_CRED_CRED_NO = 1;
    public static final int BPSS_TP_CRED_CRED_LIM_NO = 2;
    public static final int BPSS_TP_CRED_CRED_LIM = 3;

    public static final int BPSS_TP_CFD_ADD_CFD_ADD_NA = 1;
    public static final int BPSS_TP_CFD_ADD_CFD_ADD_SORIANA = 2;
    public static final int BPSS_TP_CFD_ADD_CFD_ADD_LOREAL = 3;
    public static final int BPSS_TP_CFD_ADD_CFD_ADD_BACHOCO = 4;

    public static final int BPSS_RISK_A_RSK_L = 1;
    public static final int BPSS_RISK_B_RSK_M = 2;
    public static final int BPSS_RISK_C_RSK_H = 3;
    public static final int BPSS_RISK_D_BLK = 4;
    public static final int BPSS_RISK_E_TRL = 5;

    public static final int BPSS_LINK_ALL = 1;
    public static final int BPSS_LINK_CUS_MKT_TP = 2;
    public static final int BPSS_LINK_BP_TP = 3;
    public static final int BPSS_LINK_BP = 4;
    public static final int BPSS_LINK_BPB = 5;

    public static final int BPSU_TP_BP_DEF = 1;
    
    public static final int BPSU_BA_DEF = 1;

    public static final String TXT_HQ = "MATRIZ";
    public static final String TXT_OFFICIAL = "OFICIAL";

    /*
     * Items
     */

    public static final int ITMS_LINK_ALL = 1;
    public static final int ITMS_LINK_CT_ITEM = 2;
    public static final int ITMS_LINK_CL_ITEM = 3;
    public static final int ITMS_LINK_TP_ITEM = 4;
    public static final int ITMS_LINK_IFAM = 5;
    public static final int ITMS_LINK_IGRP = 6;
    public static final int ITMS_LINK_IGEN = 7;
    public static final int ITMS_LINK_LINE = 8;
    public static final int ITMS_LINK_BRD = 9;
    public static final int ITMS_LINK_MFR = 10;
    public static final int ITMS_LINK_ITEM = 11;

    public static final int ITMS_ST_ITEM_ACT = 1;
    public static final int ITMS_ST_ITEM_RES = 2;
    public static final int ITMS_ST_ITEM_INA = 3;

    public static final int ITMU_UNIT_NA = 1;
    public static final int ITMU_UNIT_UNIT = 2;
    public static final int ITMU_UNIT_PCE = 3;
    public static final int ITMU_UNIT_K = 6;
    public static final int ITMU_UNIT_MT = 12;
    public static final int ITMU_UNIT_MT2 = 24;
    public static final int ITMU_UNIT_FT2 = 31;
    public static final int ITMU_UNIT_MT3 = 40;
    public static final int ITMU_UNIT_FT3 = 47;
    public static final int ITMU_UNIT_MT_TON = 58;
    public static final int ITMU_UNIT_KG = 59;
    public static final int ITMU_UNIT_LB = 66;
    public static final int ITMU_UNIT_OZ = 67;
    public static final int ITMU_UNIT_HOUR = 70;

    public static final int ITMU_TP_UNIT_NA = 1;
    public static final int ITMU_TP_UNIT_QTY = 2;
    public static final int ITMU_TP_UNIT_LEN = 3;
    public static final int ITMU_TP_UNIT_SURF = 4;
    public static final int ITMU_TP_UNIT_VOL = 5;
    public static final int ITMU_TP_UNIT_MASS = 6;
    public static final int ITMU_TP_UNIT_TIME = 7;

    public static final int ITMS_CT_ITEM_SAL = 1;
    public static final int ITMS_CT_ITEM_ASS = 2;
    public static final int ITMS_CT_ITEM_PUR = 3;
    public static final int ITMS_CT_ITEM_EXP = 4;

    public static final int[] ITMS_CL_ITEM_SAL_PRO = new int[] { 1, 1 };
    public static final int[] ITMS_CL_ITEM_SAL_SRV = new int[] { 1, 2 };
    public static final int[] ITMS_CL_ITEM_ASS_ASS = new int[] { 2, 1 };
    public static final int[] ITMS_CL_ITEM_PUR_CON = new int[] { 3, 1 };
    public static final int[] ITMS_CL_ITEM_PUR_EXP = new int[] { 3, 2 };
    public static final int[] ITMS_CL_ITEM_EXP_MFG = new int[] { 4, 1 };
    public static final int[] ITMS_CL_ITEM_EXP_OPE = new int[] { 4, 2 };

    public static final int[] ITMS_TP_ITEM_SAL_PRO_P = new int[] { 1, 1, 1 };
    public static final int[] ITMS_TP_ITEM_SAL_PRO_FG = new int[] { 1, 1, 2 };
    public static final int[] ITMS_TP_ITEM_SAL_PRO_WP = new int[] { 1, 1, 3 };
    public static final int[] ITMS_TP_ITEM_SAL_PRO_SP = new int[] { 1, 1, 4 };
    public static final int[] ITMS_TP_ITEM_SAL_PRO_WA = new int[] { 1, 1, 5 };
    public static final int[] ITMS_TP_ITEM_SAL_SRV_SRV = new int[] { 1, 2, 1 };
    public static final int[] ITMS_TP_ITEM_ASS_ASS_ASS = new int[] { 2, 1, 1 };
    public static final int[] ITMS_TP_ITEM_PUR_CON_RMD = new int[] { 3, 1, 1 };
    public static final int[] ITMS_TP_ITEM_PUR_CON_RMI = new int[] { 3, 1, 2 };
    public static final int[] ITMS_TP_ITEM_PUR_EXP_EXP = new int[] { 3, 2, 1 };
    public static final int[] ITMS_TP_ITEM_EXP_MFG_RM = new int[] { 4, 1, 1 };
    public static final int[] ITMS_TP_ITEM_EXP_MFG_LF = new int[] { 4, 1, 2 };
    public static final int[] ITMS_TP_ITEM_EXP_MFG_IE = new int[] { 4, 1, 3 };
    public static final int[] ITMS_TP_ITEM_EXP_OPE_RM = new int[] { 4, 2, 1 };
    public static final int[] ITMS_TP_ITEM_EXP_OPE_LF = new int[] { 4, 2, 2 };
    public static final int[] ITMS_TP_ITEM_EXP_OPE_IE = new int[] { 4, 2, 3 };

    /*
     * Finance
     */

    public static final int FINS_TP_ACC_NA = 1;
    public static final int FINS_TP_ACC_BAL = 2;
    public static final int FINS_TP_ACC_RES = 3;

    public static final int[] FINS_CL_ACC_NA = { 1, 1 };
    public static final int[] FINS_CL_ACC_ASSET = { 2, 1 };
    public static final int[] FINS_CL_ACC_LIABTY = { 2, 2 };
    public static final int[] FINS_CL_ACC_EQUITY = { 2, 3 };
    public static final int[] FINS_CL_ACC_ORD_DBT = { 2, 4 };
    public static final int[] FINS_CL_ACC_ORD_CDT = { 2, 5 };
    public static final int[] FINS_CL_ACC_RES_DBT = { 3, 1 };
    public static final int[] FINS_CL_ACC_RES_CDT = { 3, 2 };

    public static final int FINS_TP_ACC_SPE_NA = 1;
    public static final int FINS_TP_ACC_SPE_ENT_CSH = 11;
    public static final int FINS_TP_ACC_SPE_ENT_WAH = 12;
    public static final int FINS_TP_ACC_SPE_BPR_SUP = 21;
    public static final int FINS_TP_ACC_SPE_BPR_CUS = 22;
    public static final int FINS_TP_ACC_SPE_BPR_CDR = 23;
    public static final int FINS_TP_ACC_SPE_BPR_DBR = 24;
    public static final int FINS_TP_ACC_SPE_BPR_SUP_ADV = 26;
    public static final int FINS_TP_ACC_SPE_BPR_CUS_ADV = 27;
    public static final int FINS_TP_ACC_SPE_TAX_DBT = 31;
    public static final int FINS_TP_ACC_SPE_TAX_CDT = 32;
    public static final int FINS_TP_ACC_SPE_AST_DEP = 41;
    public static final int FINS_TP_ACC_SPE_AST_AMO = 42;
    public static final int FINS_TP_ACC_SPE_LBT_DEP = 46;
    public static final int FINS_TP_ACC_SPE_LBT_AMO = 47;
    public static final int FINS_TP_ACC_SPE_DP_DBT = 51;
    public static final int FINS_TP_ACC_SPE_DP_CDT = 52;
    public static final int FINS_TP_ACC_SPE_WP = 81;
    public static final int FINS_TP_ACC_SPE_PRP = 82;
    public static final int FINS_TP_ACC_SPE_AIC = 83;
    public static final int FINS_TP_ACC_SPE_YER_CLO = 91;
    public static final int FINS_TP_ACC_SPE_YER_RES = 92;
    public static final int FINS_TP_ACC_SPE_JOU = 99;

    public static final int FINS_CL_SYS_ACC_NA = 1;
    public static final int FINS_CL_SYS_ACC_ENT_CSH = 11;
    public static final int FINS_CL_SYS_ACC_ENT_WAH = 12;
    public static final int FINS_CL_SYS_ACC_BPR_SUP = 21;
    public static final int FINS_CL_SYS_ACC_BPR_CUS = 22;
    public static final int FINS_CL_SYS_ACC_BPR_CDR = 23;
    public static final int FINS_CL_SYS_ACC_BPR_DBR = 24;

    public static final int[] FINS_TP_SYS_ACC_NA_NA = new int[] { 1, 1 };
    public static final int[] FINS_TP_SYS_ACC_ENT_CSH_CSH = new int[] { 11, 1 };
    public static final int[] FINS_TP_SYS_ACC_ENT_CSH_BNK = new int[] { 11, 2 };
    public static final int[] FINS_TP_SYS_ACC_ENT_WAH_WAH = new int[] { 12, 1 };
    public static final int[] FINS_TP_SYS_ACC_BPR_SUP_BAL = new int[] { 21, 1 };
    public static final int[] FINS_TP_SYS_ACC_BPR_SUP_ADV = new int[] { 21, 2 };
    public static final int[] FINS_TP_SYS_ACC_BPR_CUS_BAL = new int[] { 22, 1 };
    public static final int[] FINS_TP_SYS_ACC_BPR_CUS_ADV = new int[] { 22, 2 };
    public static final int[] FINS_TP_SYS_ACC_BPR_CDR_BAL = new int[] { 23, 1 };
    public static final int[] FINS_TP_SYS_ACC_BPR_DBR_BAL = new int[] { 24, 1 };

    public static final int FINS_CL_SYS_MOV_MI = 11;
    public static final int FINS_CL_SYS_MOV_MO = 12;
    public static final int FINS_CL_SYS_MOV_GI = 21;
    public static final int FINS_CL_SYS_MOV_GO = 22;
    public static final int FINS_CL_SYS_MOV_PUR = 31;
    public static final int FINS_CL_SYS_MOV_SAL = 32;
    public static final int FINS_CL_SYS_MOV_SUP = 41;
    public static final int FINS_CL_SYS_MOV_CUS = 42;
    public static final int FINS_CL_SYS_MOV_CDR = 43;
    public static final int FINS_CL_SYS_MOV_DBR = 44;
    public static final int FINS_CL_SYS_MOV_YC = 91;
    public static final int FINS_CL_SYS_MOV_YO = 92;
    public static final int FINS_CL_SYS_MOV_JOU = 99;

    public static final int[] FINS_TP_SYS_MOV_MI_CUS_PAY = new int[] { 11, 11 };
    public static final int[] FINS_TP_SYS_MOV_MI_CUS_ADV = new int[] { 11, 12 };
    public static final int[] FINS_TP_SYS_MOV_MI_DBR_PAY = new int[] { 11, 13 };

    public static final int[] FINS_TP_SYS_MOV_MI_SUP_PAY = new int[] { 11, 21 };
    public static final int[] FINS_TP_SYS_MOV_MI_SUP_ADV = new int[] { 11, 22 };
    public static final int[] FINS_TP_SYS_MOV_MI_CDR_PAY = new int[] { 11, 23 };

    public static final int[] FINS_TP_SYS_MOV_MI_EQY = new int[] { 11, 31 };
    public static final int[] FINS_TP_SYS_MOV_MI_EXR = new int[] { 11, 32 };
    public static final int[] FINS_TP_SYS_MOV_MI_ADJ = new int[] { 11, 33 };
    public static final int[] FINS_TP_SYS_MOV_MI_TRA = new int[] { 11, 34 };

    public static final int[] FINS_TP_SYS_MOV_MI_INV = new int[] { 11, 51 };
    public static final int[] FINS_TP_SYS_MOV_MI_FIN = new int[] { 11, 52 };

    public static final int[] FINS_TP_SYS_MOV_MO_CUS_PAY = new int[] { 12, 11 };
    public static final int[] FINS_TP_SYS_MOV_MO_CUS_ADV = new int[] { 12, 12 };
    public static final int[] FINS_TP_SYS_MOV_MO_DBR_PAY = new int[] { 12, 13 };

    public static final int[] FINS_TP_SYS_MOV_MO_SUP_PAY = new int[] { 12, 21 };
    public static final int[] FINS_TP_SYS_MOV_MO_SUP_ADV = new int[] { 12, 22 };
    public static final int[] FINS_TP_SYS_MOV_MO_CDR_PAY = new int[] { 12, 23 };

    public static final int[] FINS_TP_SYS_MOV_MO_EQY = new int[] { 12, 31 };
    public static final int[] FINS_TP_SYS_MOV_MO_EXR = new int[] { 12, 32 };
    public static final int[] FINS_TP_SYS_MOV_MO_ADJ = new int[] { 12, 33 };
    public static final int[] FINS_TP_SYS_MOV_MO_TRA = new int[] { 12, 34 };

    public static final int[] FINS_TP_SYS_MOV_MO_INV = new int[] { 12, 51 };
    public static final int[] FINS_TP_SYS_MOV_MO_FIN = new int[] { 12, 52 };

    public static final int[] FINS_TP_SYS_MOV_GI_PUR_PUR = new int[] { 21, 11 };
    public static final int[] FINS_TP_SYS_MOV_GI_PUR_CHG = new int[] { 21, 12 };
    public static final int[] FINS_TP_SYS_MOV_GI_PUR_WAR = new int[] { 21, 13 };
    public static final int[] FINS_TP_SYS_MOV_GI_PUR_CSG = new int[] { 21, 14 };

    public static final int[] FINS_TP_SYS_MOV_GI_SAL_SAL = new int[] { 21, 21 };
    public static final int[] FINS_TP_SYS_MOV_GI_SAL_CHG = new int[] { 21, 22 };
    public static final int[] FINS_TP_SYS_MOV_GI_SAL_WAR = new int[] { 21, 23 };
    public static final int[] FINS_TP_SYS_MOV_GI_SAL_CSG = new int[] { 21, 24 };

    public static final int[] FINS_TP_SYS_MOV_GI_EXT_ADJ = new int[] { 21, 31 };
    public static final int[] FINS_TP_SYS_MOV_GI_EXT_INV = new int[] { 21, 32 };
    public static final int[] FINS_TP_SYS_MOV_GI_INT_TRA = new int[] { 21, 33 };
    public static final int[] FINS_TP_SYS_MOV_GI_INT_CNV = new int[] { 21, 34 };

    public static final int[] FINS_TP_SYS_MOV_GI_MFG_RM_ASD = new int[] { 21, 41 };
    public static final int[] FINS_TP_SYS_MOV_GI_MFG_RM_RET = new int[] { 21, 42 };
    public static final int[] FINS_TP_SYS_MOV_GI_MFG_WP_ASD = new int[] { 21, 43 };
    public static final int[] FINS_TP_SYS_MOV_GI_MFG_WP_RET = new int[] { 21, 44 };
    public static final int[] FINS_TP_SYS_MOV_GI_MFG_FG_ASD = new int[] { 21, 45 };
    public static final int[] FINS_TP_SYS_MOV_GI_MFG_FG_RET = new int[] { 21, 46 };

    public static final int[] FINS_TP_SYS_MOV_GO_PUR_PUR = new int[] { 22, 11 };
    public static final int[] FINS_TP_SYS_MOV_GO_PUR_CHG = new int[] { 22, 12 };
    public static final int[] FINS_TP_SYS_MOV_GO_PUR_WAR = new int[] { 22, 13 };
    public static final int[] FINS_TP_SYS_MOV_GO_PUR_CSG = new int[] { 22, 14 };

    public static final int[] FINS_TP_SYS_MOV_GO_SAL_SAL = new int[] { 22, 21 };
    public static final int[] FINS_TP_SYS_MOV_GO_SAL_CHG = new int[] { 22, 22 };
    public static final int[] FINS_TP_SYS_MOV_GO_SAL_WAR = new int[] { 22, 23 };
    public static final int[] FINS_TP_SYS_MOV_GO_SAL_CSG = new int[] { 22, 29 };

    public static final int[] FINS_TP_SYS_MOV_GO_EXT_ADJ = new int[] { 22, 31 };
    public static final int[] FINS_TP_SYS_MOV_GO_EXT_INV = new int[] { 22, 32 };
    public static final int[] FINS_TP_SYS_MOV_GO_INT_TRA = new int[] { 22, 33 };
    public static final int[] FINS_TP_SYS_MOV_GO_INT_CNV = new int[] { 22, 34 };

    public static final int[] FINS_TP_SYS_MOV_GO_MFG_RM_ASD = new int[] { 22, 41 };
    public static final int[] FINS_TP_SYS_MOV_GO_MFG_RM_RET = new int[] { 22, 42 };
    public static final int[] FINS_TP_SYS_MOV_GO_MFG_WP_ASD = new int[] { 22, 43 };
    public static final int[] FINS_TP_SYS_MOV_GO_MFG_WP_RET = new int[] { 22, 44 };
    public static final int[] FINS_TP_SYS_MOV_GO_MFG_FG_ASD = new int[] { 22, 45 };
    public static final int[] FINS_TP_SYS_MOV_GO_MFG_FG_RET = new int[] { 22, 46 };

    public static final int[] FINS_TP_SYS_MOV_PUR = new int[] { 31, 1 };

    public static final int[] FINS_TP_SYS_MOV_PUR_INC = new int[] { 31, 10 };
    public static final int[] FINS_TP_SYS_MOV_PUR_INC_INC = new int[] { 31, 11 };
    public static final int[] FINS_TP_SYS_MOV_PUR_INC_ADD = new int[] { 31, 12 };
    public static final int[] FINS_TP_SYS_MOV_PUR_DEC = new int[] { 31, 20 };
    public static final int[] FINS_TP_SYS_MOV_PUR_DEC_DIS = new int[] { 31, 21 };
    public static final int[] FINS_TP_SYS_MOV_PUR_DEC_RET = new int[] { 31, 22 };

    public static final int[] FINS_TP_SYS_MOV_PUR_TAX_DBT_PEN = new int[] { 31, 31 };
    public static final int[] FINS_TP_SYS_MOV_PUR_TAX_DBT_PAI = new int[] { 31, 32 };
    public static final int[] FINS_TP_SYS_MOV_PUR_TAX_CDT_PEN = new int[] { 31, 41 };
    public static final int[] FINS_TP_SYS_MOV_PUR_TAX_CDT_PAI = new int[] { 31, 42 };

    public static final int[] FINS_TP_SYS_MOV_SAL = new int[] { 32, 1 };

    public static final int[] FINS_TP_SYS_MOV_SAL_INC = new int[] { 32, 10 };
    public static final int[] FINS_TP_SYS_MOV_SAL_INC_INC = new int[] { 32, 11 };
    public static final int[] FINS_TP_SYS_MOV_SAL_INC_ADD = new int[] { 32, 12 };
    public static final int[] FINS_TP_SYS_MOV_SAL_DEC = new int[] { 32, 20 };
    public static final int[] FINS_TP_SYS_MOV_SAL_DEC_DIS = new int[] { 32, 21 };
    public static final int[] FINS_TP_SYS_MOV_SAL_DEC_RET = new int[] { 32, 22 };

    public static final int[] FINS_TP_SYS_MOV_SAL_TAX_DBT_PEN = new int[] { 32, 31 };
    public static final int[] FINS_TP_SYS_MOV_SAL_TAX_DBT_PAI = new int[] { 32, 32 };
    public static final int[] FINS_TP_SYS_MOV_SAL_TAX_CDT_PEN = new int[] { 32, 41 };
    public static final int[] FINS_TP_SYS_MOV_SAL_TAX_CDT_PAI = new int[] { 32, 42 };

    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_DEC_ADV = new int[] { 41, 1 };
    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_INC_ADV = new int[] { 41, 2 };

    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_DEC_EXR = new int[] { 41, 11 };
    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_INC_EXR = new int[] { 41, 12 };
    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_DEC_ADJ = new int[] { 41, 13 };
    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_INC_ADJ = new int[] { 41, 14 };
    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_DEC_TRA = new int[] { 41, 15 };
    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_INC_TRA = new int[] { 41, 16 };
    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_DEC_ACC = new int[] { 41, 17 };
    public static final int[] FINS_TP_SYS_MOV_SUP_BAL_INC_ACC = new int[] { 41, 18 };

    public static final int[] FINS_TP_SYS_MOV_SUP_ADV_DEC_EXR = new int[] { 41, 21 };
    public static final int[] FINS_TP_SYS_MOV_SUP_ADV_INC_EXR = new int[] { 41, 22 };
    public static final int[] FINS_TP_SYS_MOV_SUP_ADV_DEC_ADJ = new int[] { 41, 23 };
    public static final int[] FINS_TP_SYS_MOV_SUP_ADV_INC_ADJ = new int[] { 41, 24 };
    public static final int[] FINS_TP_SYS_MOV_SUP_ADV_DEC_TRA = new int[] { 41, 25 };
    public static final int[] FINS_TP_SYS_MOV_SUP_ADV_INC_TRA = new int[] { 41, 26 };
    public static final int[] FINS_TP_SYS_MOV_SUP_ADV_DEC_ACC = new int[] { 41, 27 };
    public static final int[] FINS_TP_SYS_MOV_SUP_ADV_INC_ACC = new int[] { 41, 28 };

    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_DEC_ADV = new int[] { 42, 1 };
    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_INC_ADV = new int[] { 42, 2 };

    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_DEC_EXR = new int[] { 42, 11 };
    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_INC_EXR = new int[] { 42, 12 };
    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_DEC_ADJ = new int[] { 42, 13 };
    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_INC_ADJ = new int[] { 42, 14 };
    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_DEC_TRA = new int[] { 42, 15 };
    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_INC_TRA = new int[] { 42, 16 };
    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_DEC_ACC = new int[] { 42, 17 };
    public static final int[] FINS_TP_SYS_MOV_CUS_BAL_INC_ACC = new int[] { 42, 18 };

    public static final int[] FINS_TP_SYS_MOV_CUS_ADV_DEC_EXR = new int[] { 42, 21 };
    public static final int[] FINS_TP_SYS_MOV_CUS_ADV_INC_EXR = new int[] { 42, 22 };
    public static final int[] FINS_TP_SYS_MOV_CUS_ADV_DEC_ADJ = new int[] { 42, 23 };
    public static final int[] FINS_TP_SYS_MOV_CUS_ADV_INC_ADJ = new int[] { 42, 24 };
    public static final int[] FINS_TP_SYS_MOV_CUS_ADV_DEC_TRA = new int[] { 42, 25 };
    public static final int[] FINS_TP_SYS_MOV_CUS_ADV_INC_TRA = new int[] { 42, 26 };
    public static final int[] FINS_TP_SYS_MOV_CUS_ADV_DEC_ACC = new int[] { 42, 27 };
    public static final int[] FINS_TP_SYS_MOV_CUS_ADV_INC_ACC = new int[] { 42, 28 };

    public static final int[] FINS_TP_SYS_MOV_CDR_BAL_DEC_EXR = new int[] { 43, 21 };
    public static final int[] FINS_TP_SYS_MOV_CDR_BAL_INC_EXR = new int[] { 43, 22 };
    public static final int[] FINS_TP_SYS_MOV_CDR_BAL_DEC_ADJ = new int[] { 43, 23 };
    public static final int[] FINS_TP_SYS_MOV_CDR_BAL_INC_ADJ = new int[] { 43, 24 };
    public static final int[] FINS_TP_SYS_MOV_CDR_BAL_DEC_TRA = new int[] { 43, 25 };
    public static final int[] FINS_TP_SYS_MOV_CDR_BAL_INC_TRA = new int[] { 43, 26 };
    public static final int[] FINS_TP_SYS_MOV_CDR_BAL_DEC_ACC = new int[] { 43, 27 };
    public static final int[] FINS_TP_SYS_MOV_CDR_BAL_INC_ACC = new int[] { 43, 28 };

    public static final int[] FINS_TP_SYS_MOV_DBR_BAL_DEC_EXR = new int[] { 44, 21 };
    public static final int[] FINS_TP_SYS_MOV_DBR_BAL_INC_EXR = new int[] { 44, 22 };
    public static final int[] FINS_TP_SYS_MOV_DBR_BAL_DEC_ADJ = new int[] { 44, 23 };
    public static final int[] FINS_TP_SYS_MOV_DBR_BAL_INC_ADJ = new int[] { 44, 24 };
    public static final int[] FINS_TP_SYS_MOV_DBR_BAL_DEC_TRA = new int[] { 44, 25 };
    public static final int[] FINS_TP_SYS_MOV_DBR_BAL_INC_TRA = new int[] { 44, 26 };
    public static final int[] FINS_TP_SYS_MOV_DBR_BAL_DEC_ACC = new int[] { 44, 27 };
    public static final int[] FINS_TP_SYS_MOV_DBR_BAL_INC_ACC = new int[] { 44, 28 };

    public static final int[] FINS_TP_SYS_MOV_YC_DBT = new int[] { 91, 1 };
    public static final int[] FINS_TP_SYS_MOV_YC_CDT = new int[] { 91, 2 };

    public static final int[] FINS_TP_SYS_MOV_YO_DBT = new int[] { 92, 1 };
    public static final int[] FINS_TP_SYS_MOV_YO_CDT = new int[] { 92, 2 };

    public static final int[] FINS_TP_SYS_MOV_JOU_DBT = new int[] { 99, 1 };
    public static final int[] FINS_TP_SYS_MOV_JOU_CDT = new int[] { 99, 2 };

    public static final int FINS_CT_ACC_CASH_CASH = 1;  // XXX eliminate same constant in SDataConstantsSys (sflores, 2013-07-04)
    public static final int FINS_CT_ACC_CASH_BANK = 2;  // XXX eliminate same constant in SDataConstantsSys (sflores, 2013-07-04)

    public static final int FINS_TP_TAX_CHARGED = 1;
    public static final int FINS_TP_TAX_RETAINED = 2;
    public static final int FINS_TP_TAX_OTHER = 9;

    public static final int FINS_TP_TAX_CAL_RATE = 1;
    public static final int FINS_TP_TAX_CAL_AMT_FIX_U = 2;
    public static final int FINS_TP_TAX_CAL_AMT_FIX = 3;
    public static final int FINS_TP_TAX_CAL_AMT = 4;

    public static final int FINS_TP_TAX_APP_ACCR = 1;
    public static final int FINS_TP_TAX_APP_CASH = 2;

    public static final String FINS_TP_TAX_APP_ACCR_NAME = "DEVENGADO";
    public static final String FINS_TP_TAX_APP_CASH_NAME = "PAGADO";

    public static final int FINS_FISCAL_ACC_NA = 1;
    public static final String FINS_FISCAL_ACC_NA_CODE = "0";
    public static final String FINS_FISCAL_ACC_NA_NAME = "(N/A)";
    public static final int FINS_FISCAL_CUR_JPY_ID = 72;
    public static final int FINS_FISCAL_CUR_EUR_ID = 48;
    public static final int FINS_FISCAL_CUR_MXN_ID = 101;
    public static final int FINS_FISCAL_CUR_USD_ID = 147;
    public static final int FINS_FISCAL_CUR_XXX_ID = 172;
    public static final String FINS_FISCAL_CUR_JPY_NAME = "JPY";
    public static final String FINS_FISCAL_CUR_EUR_NAME = "EUR";
    public static final String FINS_FISCAL_CUR_MXN_NAME = "MXN";
    public static final String FINS_FISCAL_CUR_USD_NAME = "USD";
    public static final String FINS_FISCAL_CUR_XXX_NAME = "XXX";
    public static final int FINS_FISCAL_BANK_NA = 999;
    public static final int FINS_FISCAL_PAY_MET_NA = 98;

    public static final int FINS_TP_FISCAL_ACC_LINK_ACC = 1;
    public static final int FINS_TP_FISCAL_ACC_LINK_CSH_CSH = 11;
    public static final int FINS_TP_FISCAL_ACC_LINK_CSH_BNK = 12;
    public static final int FINS_TP_FISCAL_ACC_LINK_BPS_SUP = 21;
    public static final int FINS_TP_FISCAL_ACC_LINK_BPS_CUS = 22;
    public static final int FINS_TP_FISCAL_ACC_LINK_BPS_CDR = 23;
    public static final int FINS_TP_FISCAL_ACC_LINK_BPS_DBR = 24;
    public static final int FINS_TP_FISCAL_ACC_LINK_BPS_SUP_ADV = 26;
    public static final int FINS_TP_FISCAL_ACC_LINK_BPS_CUS_ADV = 27;
    public static final int FINS_TP_FISCAL_ACC_LINK_RES_INC = 31;
    public static final int FINS_TP_FISCAL_ACC_LINK_RES_EXP = 32;
    
    public static final int FINS_CFD_TAX_NA = 0;
    public static final int FINS_CFD_TAX_ISR = 1;
    public static final int FINS_CFD_TAX_IVA = 2;
    public static final int FINS_CFD_TAX_IEPS = 3;

    public static final int FIN_ACC_NA = 1;
    public static final int FIN_CC_NA = 1;

    public static final String FINU_TP_REC_FY_OPEN = "EA";
    public static final String FINU_TP_REC_FY_END = "EC";
    public static final String FINU_TP_REC_PUR = "C";
    public static final String FINU_TP_REC_SAL = "V";
    public static final String FINU_TP_REC_GDS_IN = "ME";
    public static final String FINU_TP_REC_GDS_OUT = "MS";
    public static final String FINU_TP_REC_CASH_IN = "DE";
    public static final String FINU_TP_REC_CASH_OUT = "DS";
    public static final String FINU_TP_REC_EXC_FLUC_PROF = "FG";
    public static final String FINU_TP_REC_EXC_FLUC_LOSS = "FP";
    public static final String FINU_TP_REC_DEPREC = "Dp";
    public static final String FINU_TP_REC_AMORT = "Am";
    public static final String FINU_TP_REC_COGM = "CP";
    public static final String FINU_TP_REC_COGS = "CV";
    public static final String FINU_TP_REC_SUBSYS_SUP = "SP";
    public static final String FINU_TP_REC_SUBSYS_CUS = "SC";
    public static final String FINU_TP_REC_SUBSYS_CDR = "SA";
    public static final String FINU_TP_REC_SUBSYS_DBR = "SD";
    public static final String FINU_TP_REC_JOURNAL = "Dr";
    public static final String FINU_TP_REC_CASH_BANK = "CB";
    
    public static final int FIN_LAY_BANK_REC = 1;
    public static final int FIN_LAY_BANK_DPS = 2;
    public static final int FIN_LAY_BANK_ADV = 3;
    public static final int FIN_LAY_BANK_QRY = 4;

    /*
     * Transactions
     */

    public static final int TRNS_ST_DPS_NEW = 1;
    public static final int TRNS_ST_DPS_EMITED = 2;
    public static final int TRNS_ST_DPS_ANNULED = 3;

    public static final int TRNS_TP_XML_DVY_NA = 1;
    public static final int TRNS_TP_XML_DVY_WS_SOR = 2;

    public static final int TRNS_ST_XML_DVY_NA = 1;
    public static final int TRNS_ST_XML_DVY_PENDING = 2;
    public static final int TRNS_ST_XML_DVY_REJECT = 3;
    public static final int TRNS_ST_XML_DVY_APPROVED = 4;

    public static final String TXT_ST_XML_DVY_NA = "NA";
    public static final String TXT_ST_XML_DVY_PENDING = "PENDING";
    public static final String TXT_ST_XML_DVY_REJECT = "REJECT";
    public static final String TXT_ST_XML_DVY_APPROVED = "ACCEPTED";

    public static final int TRNS_ST_DPS_VAL_EFF = 1;
    public static final int TRNS_ST_DPS_VAL_RISS = 2;
    public static final int TRNS_ST_DPS_VAL_REPL = 3;

    public static final int TRNS_ST_DPS_AUTHORN_NA = 1;
    public static final int TRNS_ST_DPS_AUTHORN_PENDING = 2;
    public static final int TRNS_ST_DPS_AUTHORN_REJECT = 3;
    public static final int TRNS_ST_DPS_AUTHORN_AUTHORN = 4;

    public static final int TRNS_CT_DPS_PUR = 1;
    public static final int TRNS_CT_DPS_SAL = 2;

    public static final int[] TRNS_CL_DPS_PUR_EST = { 1, 1 };
    public static final int[] TRNS_CL_DPS_PUR_ORD = { 1, 2 };
    public static final int[] TRNS_CL_DPS_PUR_DOC = { 1, 3 };
    //public static final int[] TRNS_CL_DPS_PUR_BOL = { 1, 4 };
    public static final int[] TRNS_CL_DPS_PUR_ADJ = { 1, 5 };
    public static final int[] TRNS_CL_DPS_SAL_EST = { 2, 1 };
    public static final int[] TRNS_CL_DPS_SAL_ORD = { 2, 2 };
    public static final int[] TRNS_CL_DPS_SAL_DOC = { 2, 3 };
    //public static final int[] TRNS_CL_DPS_SAL_BOL = { 2, 4 };
    public static final int[] TRNS_CL_DPS_SAL_ADJ = { 2, 5 };

    public static final int[] TRNU_TP_DPS_PUR_EST = { 1, 1, 1 };
    public static final int[] TRNU_TP_DPS_PUR_CON = { 1, 1, 2 };
    public static final int[] TRNU_TP_DPS_PUR_ORD = { 1, 2, 1 };
    public static final int[] TRNU_TP_DPS_PUR_INV = { 1, 3, 1 };
    public static final int[] TRNU_TP_DPS_PUR_REM = { 1, 3, 2 };
    public static final int[] TRNU_TP_DPS_PUR_REC = { 1, 3, 3 };
    public static final int[] TRNU_TP_DPS_PUR_TIC = { 1, 3, 4 };
    //public static final int[] TRNU_TP_DPS_PUR_BOL = { 1, 4, 1 };
    public static final int[] TRNU_TP_DPS_PUR_CN = { 1, 5, 1 };
    public static final int[] TRNU_TP_DPS_SAL_EST = { 2, 1, 1 };
    public static final int[] TRNU_TP_DPS_SAL_CON = { 2, 1, 2 };
    public static final int[] TRNU_TP_DPS_SAL_ORD = { 2, 2, 1 };
    public static final int[] TRNU_TP_DPS_SAL_INV = { 2, 3, 1 };
    public static final int[] TRNU_TP_DPS_SAL_REM = { 2, 3, 2 };
    public static final int[] TRNU_TP_DPS_SAL_REC = { 2, 3, 3 };
    public static final int[] TRNU_TP_DPS_SAL_TIC = { 2, 3, 4 };
    //public static final int[] TRNU_TP_DPS_SAL_BOL = { 2, 4, 1 };
    public static final int[] TRNU_TP_DPS_SAL_CN = { 2, 5, 1 };

    public static final int TRNS_CT_IOG_IN = 1;
    public static final int TRNS_CT_IOG_OUT = 2;

    public static final int[] TRNS_CL_IOG_IN_PUR = { 1, 1 };    // purchases
    public static final int[] TRNS_CL_IOG_IN_SAL = { 1, 2 };    // sales
    public static final int[] TRNS_CL_IOG_IN_ADJ = { 1, 3 };    // adjustments
    public static final int[] TRNS_CL_IOG_IN_EXT = { 1, 4 };    // other external
    public static final int[] TRNS_CL_IOG_IN_INT = { 1, 5 };    // other internal
    public static final int[] TRNS_CL_IOG_IN_MFG = { 1, 6 };    // manufacturing
    public static final int[] TRNS_CL_IOG_IN_EXP = { 1, 7 };    // expenses
    public static final int[] TRNS_CL_IOG_OUT_PUR = { 2, 1 };   // purchases
    public static final int[] TRNS_CL_IOG_OUT_SAL = { 2, 2 };   // sales
    public static final int[] TRNS_CL_IOG_OUT_ADJ = { 2, 3 };   // adjustments
    public static final int[] TRNS_CL_IOG_OUT_EXT = { 2, 4 };   // other external
    public static final int[] TRNS_CL_IOG_OUT_INT = { 2, 5 };   // other internal
    public static final int[] TRNS_CL_IOG_OUT_MFG = { 2, 6 };   // manufacturing
    public static final int[] TRNS_CL_IOG_OUT_EXP = { 2, 7 };   // expenses

    public static final int[] TRNS_TP_IOG_IN_PUR_PUR = { 1, 1, 1 };
    public static final int[] TRNS_TP_IOG_IN_SAL_SAL = { 1, 2, 1 };
    public static final int[] TRNS_TP_IOG_IN_ADJ_INV = { 1, 3, 1 };
    public static final int[] TRNS_TP_IOG_IN_ADJ_ADJ = { 1, 3, 2 };
    public static final int[] TRNS_TP_IOG_IN_EXT_CHG_PUR = { 1, 4, 1 };
    public static final int[] TRNS_TP_IOG_IN_EXT_CHG_SAL = { 1, 4, 2 };
    public static final int[] TRNS_TP_IOG_IN_EXT_WAR_PUR = { 1, 4, 3 };
    public static final int[] TRNS_TP_IOG_IN_EXT_WAR_SAL = { 1, 4, 4 };
    public static final int[] TRNS_TP_IOG_IN_EXT_CSG_PUR = { 1, 4, 5 };
    public static final int[] TRNS_TP_IOG_IN_EXT_CSG_SAL = { 1, 4, 6 };
    public static final int[] TRNS_TP_IOG_IN_INT_TRA = { 1, 5, 1 };
    public static final int[] TRNS_TP_IOG_IN_INT_CNV = { 1, 5, 2 };
    public static final int[] TRNS_TP_IOG_IN_MFG_RM_ASD = { 1, 6, 1 };
    public static final int[] TRNS_TP_IOG_IN_MFG_RM_RET = { 1, 6, 2 };
    public static final int[] TRNS_TP_IOG_IN_MFG_WP_ASD = { 1, 6, 3 };
    public static final int[] TRNS_TP_IOG_IN_MFG_WP_RET = { 1, 6, 4 };
    public static final int[] TRNS_TP_IOG_IN_MFG_FG_ASD = { 1, 6, 5 };
    public static final int[] TRNS_TP_IOG_IN_MFG_FG_RET = { 1, 6, 6 };
    public static final int[] TRNS_TP_IOG_IN_MFG_CON = { 1, 6, 7 };
    public static final int[] TRNS_TP_IOG_IN_EXP_PUR = {1, 9, 1 };
    public static final int[] TRNS_TP_IOG_IN_EXP_MFG = {1, 9, 2 };
    public static final int[] TRNS_TP_IOG_IN_CST_RM = {1, 9, 3 };
    public static final int[] TRNS_TP_IOG_OUT_PUR_PUR = { 2, 1, 1 };
    public static final int[] TRNS_TP_IOG_OUT_SAL_SAL = { 2, 2, 1 };
    public static final int[] TRNS_TP_IOG_OUT_ADJ_INV = { 2, 3, 1 };
    public static final int[] TRNS_TP_IOG_OUT_ADJ_ADJ = { 2, 3, 2 };
    public static final int[] TRNS_TP_IOG_OUT_EXT_CHG_PUR = { 2, 4, 1 };
    public static final int[] TRNS_TP_IOG_OUT_EXT_CHG_SAL = { 2, 4, 2 };
    public static final int[] TRNS_TP_IOG_OUT_EXT_WAR_PUR = { 2, 4, 3 };
    public static final int[] TRNS_TP_IOG_OUT_EXT_WAR_SAL = { 2, 4, 4 };
    public static final int[] TRNS_TP_IOG_OUT_EXT_CSG_PUR = { 2, 4, 5 };
    public static final int[] TRNS_TP_IOG_OUT_EXT_CSG_SAL = { 2, 4, 6 };
    public static final int[] TRNS_TP_IOG_OUT_INT_TRA = { 2, 5, 1 };
    public static final int[] TRNS_TP_IOG_OUT_INT_CNV = { 2, 5, 2 };
    public static final int[] TRNS_TP_IOG_OUT_MFG_RM_ASD = { 2, 6, 1 };
    public static final int[] TRNS_TP_IOG_OUT_MFG_RM_RET = { 2, 6, 2 };
    public static final int[] TRNS_TP_IOG_OUT_MFG_WP_ASD = { 2, 6, 3 };
    public static final int[] TRNS_TP_IOG_OUT_MFG_WP_RET = { 2, 6, 4 };
    public static final int[] TRNS_TP_IOG_OUT_MFG_FG_ASD = { 2, 6, 5 };
    public static final int[] TRNS_TP_IOG_OUT_MFG_FG_RET = { 2, 6, 6 };
    public static final int[] TRNS_TP_IOG_OUT_MFG_CON = { 2, 6, 7 };
    public static final int[] TRNS_TP_IOG_OUT_EXP_PUR = {2, 9, 1 };
    public static final int[] TRNS_TP_IOG_OUT_EXP_MFG = {2, 9, 2 };
    public static final int[] TRNS_TP_IOG_OUT_CST_RM = {2, 9, 3 };

    public static final int TRNS_TP_LINK_ALL = 1;
    public static final int TRNS_TP_LINK_CT_ITEM = 2;
    public static final int TRNS_TP_LINK_CL_ITEM = 3;
    public static final int TRNS_TP_LINK_TP_ITEM = 4;
    public static final int TRNS_TP_LINK_IFAM = 5;
    public static final int TRNS_TP_LINK_IGRP = 6;
    public static final int TRNS_TP_LINK_IGEN = 7;
    public static final int TRNS_TP_LINK_LINE = 8;
    public static final int TRNS_TP_LINK_BRD = 9;
    public static final int TRNS_TP_LINK_MFR = 10;
    public static final int TRNS_TP_LINK_ITEM = 11;
    
    public static final int TRNU_TP_IOG_ADJ_NA = 1;

    public static final int TRNU_TP_DPS_ANN_NA = 1;
    
    public static final int TRN_PAC_FCG = 1;    // Formas Contínuas de Guadalajara SA de CV
    public static final int TRN_PAC_FNK = 2;    // Finkok SA de CV

    public static final int TRNX_DIOG_CST_ASIG_NA = 0;
    public static final int TRNX_DIOG_CST_ASIG_NO = 1;
    public static final int TRNX_DIOG_CST_ASIG_YES = 2;

    public static final int TRNX_DIOG_CST_TRAN_NA = 0;
    public static final int TRNX_DIOG_CST_TRAN_NO = 1;
    public static final int TRNX_DIOG_CST_TRAN_YES = 2;
    
    /*
     * Marketing
     */

    public static final int MKTS_TP_COMMS_NA = 1;
    public static final int MKTS_TP_COMMS_PER = 2;
    public static final int MKTS_TP_COMMS_AMT_FIX_U = 3;
    public static final int MKTS_TP_COMMS_AMT_FIX = 4;
    public static final int MKTS_TP_COMMS_AMT = 5;

    public static final int MKTS_ORIG_COMMS_NA = 1;
    public static final int MKTS_ORIG_COMMS_DOC = 2;
    public static final int MKTS_ORIG_COMMS_ITEM = 3;

    public static final int MKTS_COMMS_MOD_AGT = 1;     // modality: as agent
    public static final int MKTS_COMMS_MOD_SUP = 2;     // modality: as supervisor

    public static final int MKTS_COMMS_SRC_DIR = 1;     // source: direct
    public static final int MKTS_COMMS_SRC_TYPE = 2;    // source: by type

    /*
     * Logistics
     */

    public static final int LOGS_TP_SHIP_SHI = 1;
    public static final int LOGS_TP_SHIP_REC = 2;

    public static final int LOGS_TP_DLY_DOM = 1;
    public static final int LOGS_TP_DLY_INT = 2;

    public static final int LOGS_TP_SPOT_PLA = 1;
    public static final int LOGS_TP_SPOT_POR = 2;
    public static final int LOGS_TP_SPOT_TER = 3;

    public static final int LOGS_TP_MOT_NA = 1;
    public static final int LOGS_TP_MOT_LAN = 2;
    public static final int LOGS_TP_MOT_AIR = 3;
    public static final int LOGS_TP_MOT_SEA = 4;
    public static final int LOGS_TP_MOT_MUL = 5;

    public static final int LOGS_TP_CAR_NA = 1;
    public static final int LOGS_TP_CAR_OWN = 2;
    public static final int LOGS_TP_CAR_BPR = 3;
    public static final int LOGS_TP_CAR_CAR = 4;
    public static final int LOGS_TP_CAR_OTH = 9;

    public static final int LOGS_TP_DOC_SHIP_DPS = 1;
    public static final int LOGS_TP_DOC_SHIP_IOG = 2;

    public static final int LOGS_INC_NA = 1;
    public static final int LOGS_INC_EXW = 2;
    public static final int LOGS_INC_FCA = 3;
    public static final int LOGS_INC_CPT = 4;
    public static final int LOGS_INC_CIP = 5;
    public static final int LOGS_INC_DAT = 6;
    public static final int LOGS_INC_DAP = 7;
    public static final int LOGS_INC_DDP = 8;
    public static final int LOGS_INC_FAS = 9;
    public static final int LOGS_INC_FOB = 10;
    public static final int LOGS_INC_CFR = 11;
    public static final int LOGS_INC_CIF = 12;
    public static final int LOGS_INC_LAB = 101;

    public static final int FIELD_MONTH_JANUARY = 1;
    public static final int FIELD_MONTH_FEBRUARY = 2;
    public static final int FIELD_MONTH_MARCH = 3;
    public static final int FIELD_MONTH_APRIL = 4;
    public static final int FIELD_MONTH_MAY = 5;
    public static final int FIELD_MONTH_JUNE = 6;
    public static final int FIELD_MONTH_JULY = 7;
    public static final int FIELD_MONTH_AUGUST = 8;
    public static final int FIELD_MONTH_SEPTEMBER = 9;
    public static final int FIELD_MONTH_OCTOBER = 10;
    public static final int FIELD_MONTH_NOVEMBER = 11;
    public static final int FIELD_MONTH_DECEMBER = 12;

    /*
     * Manufacturing
     */

    public static final int MFGS_ST_ORD_NEW = 1;
    public static final int MFGS_ST_ORD_WEI = 2;
    public static final int MFGS_ST_ORD_FLR = 3;
    public static final int MFGS_ST_ORD_PRO = 4;
    public static final int MFGS_ST_ORD_FIN = 5;
    public static final int MFGS_ST_ORD_CLO = 6;

    public static final int MFGX_ORD_CST_DONE_NA = 0;
    public static final int MFGX_ORD_CST_DONE_NO = 1;
    public static final int MFGX_ORD_CST_DONE_YES = 2;

    /*
     * Human Resources
     */
    
    public static final int HRSS_CL_HRS_CAT_SEX = 1; // Sex
    public static final int HRSS_CL_HRS_CAT_BLO = 2; // Blod Type
    public static final int HRSS_CL_HRS_CAT_MAR = 3; // Marital Status
    public static final int HRSS_CL_HRS_CAT_EDU = 4; // Level Of Education

    public static final int[] HRSS_TP_HRS_CAT_MAL = new int[] { 1, 1 }; // Male
    public static final int[] HRSS_TP_HRS_CAT_FEM = new int[] { 1, 2 }; // Female

    public static final int HRSS_TP_PAY_WEE = 1;    // Weekly 
    public static final int HRSS_TP_PAY_FOR = 2;    // Fortnightly 

    public static final int HRSS_TP_PAY_SHT_NOR = 1;    // Normal
    public static final int HRSS_TP_PAY_SHT_SPE = 2;    // Special
    public static final int HRSS_TP_PAY_SHT_EXT = 3;    // Extraordinary

    public static final int HRSS_TP_SAL_FIX = 1;    // Fixed
    public static final int HRSS_TP_SAL_VAR = 2;    // Variable
    public static final int HRSS_TP_SAL_MIX = 3;    // Mixed

    public static final int HRSS_TP_ACC_GBL = 1;    // Global
    public static final int HRSS_TP_ACC_DEP = 2;    // Department
    public static final int HRSS_TP_ACC_EMP = 3;    // Employee

    public static final int HRSS_TP_TAX_COMP_WOT = 1;   // With out tax
    public static final int HRSS_TP_TAX_COMP_PAY = 2;   // Payroll
    public static final int HRSS_TP_TAX_COMP_ANN = 3;   // Annual

    public static final int HRSS_TP_EAR_COMP_AMT = 1;       // Amount
    public static final int HRSS_TP_EAR_COMP_DAY = 2;       // Days
    public static final int HRSS_TP_EAR_COMP_HRS = 3;       // Hours
    public static final int HRSS_TP_EAR_COMP_PER_DAY = 12;  // % Day
    public static final int HRSS_TP_EAR_COMP_PER_HRS = 13;  // % Hour
    public static final int HRSS_TP_EAR_COMP_PER_EAR = 21;  // % Earnings

    public static final int HRSS_TP_EAR_EXEM_NON = 1;       // Non applicable
    public static final int HRSS_TP_EAR_EXEM_PER = 11;      // Percentage
    public static final int HRSS_TP_EAR_EXEM_MWZ_GBL = 21;  // Minimum Wage Global
    public static final int HRSS_TP_EAR_EXEM_MWZ_EVT = 22;  // Minimum Wage Event
    public static final int HRSS_TP_EAR_EXEM_MWZ_SEN = 23;  // Minimum Wage Seniority
    public static final int HRSS_TP_EAR_EXEM_SAL_GBL = 31;  // Salary Global
    public static final int HRSS_TP_EAR_EXEM_SAL_EVT = 32;  // Salary Event
    public static final int HRSS_TP_EAR_EXEM_SAL_SEN = 33;  // Salary Seniority
    public static final int HRSS_TP_EAR_EXEM_WCB_GBL = 41;  // Wage Contribution Base Global
    public static final int HRSS_TP_EAR_EXEM_WCB_EVT = 42;  // Wage Contribution Base Event
    public static final int HRSS_TP_EAR_EXEM_WCB_SEN = 43;  // Wage Contribution Base Seniority

    public static final int HRSS_TP_EAR_EAR = 1;        // Earnings
    public static final int HRSS_TP_EAR_DIS = 14;       // Disability
    public static final int HRSS_TP_EAR_TAX_SUB = 17;   // Tax
    public static final int HRSS_TP_EAR_OVR_TME = 19;   // Overtime
    public static final int HRSS_TP_EAR_SEN_BON = 22;   // Seniority bonus 
    public static final int HRSS_TP_EAR_SET = 23;       // Payment by settlement
    public static final int HRSS_TP_EAR_CMP = 25;       // Compensation
    public static final int HRSS_TP_EAR_OTH = 999;  // Other payments diferent the catalogue earnings type the SAT

    public static final int HRSS_TP_OTH_PAY_TAX_REF = 1;    // Tax Refund
    public static final int HRSS_TP_OTH_PAY_TAX_SUB = 2;    // Tax Subsidy
    public static final int HRSS_TP_OTH_PAY_TRV_EXP = 3;    // Travel Expenses
    public static final int HRSS_TP_OTH_PAY_TAX_BAL = 4;    // Tax Positive Balance
    public static final int HRSS_TP_OTH_PAY_OTH = 999;  // Other payments

    public static final int HRSS_TP_DED_COMP_AMT = 1;       // Amount
    public static final int HRSS_TP_DED_COMP_DAY = 2;       // Days
    public static final int HRSS_TP_DED_COMP_HRS = 3;       // Hours
    public static final int HRSS_TP_DED_COMP_PER_DAY = 12;  // % Day
    public static final int HRSS_TP_DED_COMP_PER_HRS = 13;  // % Hour
    public static final int HRSS_TP_DED_COMP_PBT = 21;      // Pay Before Tax
    public static final int HRSS_TP_DED_COMP_PAT = 22;      // Pay After Tax
    public static final int HRSS_TP_DED_COMP_IBT = 26;      // Income Before Tax
    public static final int HRSS_TP_DED_COMP_IAT = 27;      // Income After Tax

    public static final int HRSS_TP_DED_SSC = 1;    // Social Security
    public static final int HRSS_TP_DED_TAX = 2;    // Tax
    public static final int HRSS_TP_DED_DIS = 6;    // Disability

    public static final int HRSS_TP_BEN_NON = 1;        // Non applicable
    public static final int HRSS_TP_BEN_ANN_BON = 11;   // Annual bonus
    public static final int HRSS_TP_BEN_VAC = 21;       // Loan
    public static final int HRSS_TP_BEN_VAC_BON = 22;   // Home credit

    public static final int HRSS_TP_LOAN_NON = 1;       // Non applicable
    public static final int HRSS_TP_LOAN_LOA_COM = 2;   // Company loans
    public static final int HRSS_TP_LOAN_LOA_UNI = 3;   // Union loans
    public static final int HRSS_TP_LOAN_LOA_TPS = 4;   // Third parties loans
    public static final int HRSS_TP_LOAN_HOM = 11;      // Home credit
    public static final int HRSS_TP_LOAN_CON = 12;      // Consumer credit

    public static final int HRSS_TP_LOAN_PAY_AMT = 1;   // Amount
    public static final int HRSS_TP_LOAN_PAY_FIX = 2;   // Fixed
    public static final int HRSS_TP_LOAN_PAY_PER = 3;   // Percentage
    public static final int HRSS_TP_LOAN_PAY_UMA = 4;   // UMA

    public static final int HRSS_TP_CON_TME_DET = 1;    // Contract undetermined time
    public static final int HRSS_TP_CON_LAB_CMM = 8;    // Contract labor commission
    public static final int HRSS_TP_CON_OTH = 99;       // Other contract
    
    public static final int HRSS_TP_REC_SCHE_WAG = 2;       // Wages & salaries
    public static final int HRSS_TP_REC_SCHE_RET = 3;       // Retirees
    public static final int HRSS_TP_REC_SCHE_PEN = 4;       // Pensioners
    public static final int HRSS_TP_REC_SCHE_ASS_COO = 5;   // Cooperative society members
    public static final int HRSS_TP_REC_SCHE_ASS_CIV = 6;   // Civil society & association members
    public static final int HRSS_TP_REC_SCHE_ASS_BRD = 7;   // Board members
    public static final int HRSS_TP_REC_SCHE_ASS_SAL = 8;   // Sales representatives
    public static final int HRSS_TP_REC_SCHE_ASS_PRO = 9;   // Professionals
    public static final int HRSS_TP_REC_SCHE_ASS_SHA = 10;  // Shareholders
    public static final int HRSS_TP_REC_SCHE_ASS_OTH = 11;  // Other
    public static final int HRSS_TP_REC_SCHE_OTH = 99;      // Other

    public static final int HRSS_TP_POS_RISK_CL1 = 1;   // Class I
    public static final int HRSS_TP_POS_RISK_CL2 = 2;   // Class II
    public static final int HRSS_TP_POS_RISK_CL3 = 3;   // Class III
    public static final int HRSS_TP_POS_RISK_CL4 = 4;   // Class IV
    public static final int HRSS_TP_POS_RISK_CL5 = 5;   // Class V

    public static final int HRSS_TP_WORK_DAY_NON = 0; // Non applicable
    public static final int HRSS_TP_WORK_DAY_DIU = 1;   // Diurnal
    
    public static final int HRSS_TP_DIS_RSK = 1;    // Risk
    public static final int HRSS_TP_DIS_DIS = 2;    // Disease
    public static final int HRSS_TP_DIS_MAT = 3;    // Maternity

    public static final int HRSS_TP_DAY_NON = 1;    // Non applicable
    public static final int HRSS_TP_DAY_WRK = 2;    // Workday
    public static final int HRSS_TP_DAY_OFF = 3;    // Day off
    public static final int HRSS_TP_DAY_HOL = 4;    // Holiday
    public static final int HRSS_TP_DAY_ABS = 5;    // Absence

    public static final int HRSS_BANK_NON = 999;    // Non applicable

    public static final int HRSU_CL_ABS_ABS = 1;    // Absence
    public static final int HRSU_CL_ABS_DIS = 2;    // Disease
    public static final int HRSU_CL_ABS_VAC = 3;    // Vacation

    public static final int[] HRSU_TP_ABS_ABS = new int[] { 1, 1 };     // Absence
    public static final int[] HRSU_TP_ABS_DIS_RSK = new int[] { 2, 1 }; // Disease, risk
    public static final int[] HRSU_TP_ABS_DIS_DIS = new int[] { 2, 2 }; // Disease, disease
    public static final int[] HRSU_TP_ABS_DIS_MAT = new int[] { 2, 3 }; // Disease, maternity
    public static final int[] HRSU_TP_ABS_VAC = new int[] { 3, 1 };     // Vacation

    public static final int HRSU_TP_EMP_DIS_NON = 1;    // Non applicable

    public static final int HRSU_TP_EMP_NON = 1;    // Non applicable

    public static final int HRSU_TP_WRK_NON = 1;    // Non applicable

    public static final int HRSU_TP_MWZ_DEF = 1;    // Default

    public static final int HRSU_DEP_NON = 1;   // Non applicable

    public static final int HRSU_POS_NON = 1;   // Non applicable

    public static final int HRSU_SHT_NON = 1;   // Non applicable

    public static final int HRS_AUT_GBL = 1;
    public static final int HRS_AUT_EMP = 2;
}
