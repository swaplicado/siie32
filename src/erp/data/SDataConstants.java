/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.data;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDataConstants {

    public static final int UNDEFINED = 0;

    public static final int MOD_CFG = 101000;
    public static final int MOD_FIN = 102000;
    public static final int MOD_PUR = 103000;
    public static final int MOD_SAL = 104000;
    public static final int MOD_INV = 105000;
    public static final int MOD_MKT = 106000;
    public static final int MOD_LOG = 107000;
    public static final int MOD_MFG = 108000;
    public static final int MOD_HRS = 109000;
    public static final int MOD_XXX = 109999;

    public static final int MOD_CFG_CFG = 101100;
    public static final int MOD_CFG_CFG_CFG = 101110;   // configuration
    public static final int MOD_CFG_CFG_DNS = 101120;   // Document Number Series
    public static final int MOD_CFG_CFG_DNC = 101130;   // Document Numbering Center
    public static final int MOD_CFG_CAT = 101200;
    public static final int MOD_CFG_REP = 101900;
    
    public static final int MOD_FIN_CFG = 102100;
    public static final int MOD_FIN_CAT = 102200;
    public static final int MOD_FIN_CAT_CPT = 102240;   // concepts
    public static final int MOD_FIN_ACC = 102300;
    public static final int MOD_FIN_JOU = 102400;
    public static final int MOD_FIN_FIN = 102500;
    public static final int MOD_FIN_FIN_CSH = 102520;   // cash: checks & counter-receipts
    public static final int MOD_FIN_REP = 102900;
    public static final int MOD_FIN_REP_AUX = 102940;
    
    public static final int MOD_PUR_CAT = 103100;
    public static final int MOD_PUR_EST = 103200;
    public static final int MOD_PUR_CON = 103300;
    public static final int MOD_PUR_ORD = 103400;
    public static final int MOD_PUR_INV = 103500;       // invoices
    public static final int MOD_PUR_CNT = 103600;       // credit notes
    public static final int MOD_PUR_DVY = 103700;       // deliveries
    public static final int MOD_PUR_RET = 103800;       // returns
    public static final int MOD_PUR_REP = 103900;
    public static final int MOD_PUR_REP_CON = 103909;   // contracts
    public static final int MOD_PUR_REP_CST = 103910;   // costs
    
    public static final int MOD_SAL_CAT = 104100;
    public static final int MOD_SAL_EST = 104200;
    public static final int MOD_SAL_CON = 104300;
    public static final int MOD_SAL_ORD = 104400;
    public static final int MOD_SAL_INV = 104500;       // invoices
    public static final int MOD_SAL_CNT = 104600;       // credit notes
    public static final int MOD_SAL_DVY = 104700;       // deliveries
    public static final int MOD_SAL_RET = 104800;       // returns
    public static final int MOD_SAL_REP = 104900;
    public static final int MOD_SAL_REP_CON = 104909;   // contracts
    public static final int MOD_SAL_REP_SHI = 104910;   // shipments
    public static final int MOD_SAL_REP_MIN = 104911;   // money in
    
    public static final int MOD_MFG_HRS = 108500;       // labor hours
    
    public static final int MOD_HRS_IMP = 109500;       // import
    
    public static final int GLOBAL_CAT_CFG = 201000;
    public static final int GLOBAL_CAT_USR = 202000;
    public static final int GLOBAL_CAT_LOC = 203000;
    public static final int GLOBAL_CAT_BPS = 204000;
    public static final int GLOBAL_CAT_ITM = 205000;
    public static final int GLOBAL_CAT_FIN = 206000;
    public static final int GLOBAL_CAT_TRN = 207000;
    public static final int GLOBAL_CAT_MKT = 208000;
    public static final int GLOBAL_CAT_LOG = 209000;
    public static final int GLOBAL_CAT_MFG = 210000;
    public static final int GLOBAL_CAT_HRS = 211000;
    public static final int GLOBAL_CAT_XXX = 211999;

    public static final int CFGS_CT_ENT = 201001;
    public static final int CFGS_TP_ENT = 201002;
    public static final int CFGS_TP_SORT = 201003;
    public static final int CFGS_TP_BAL = 201004;
    public static final int CFGS_TP_REL = 201005;
    public static final int CFGS_TP_FMT_D = 201006;
    public static final int CFGS_TP_FMT_DT = 201007;
    public static final int CFGS_TP_FMT_T = 201008;
    public static final int CFGS_TP_DBMS = 201009;
    public static final int CFGS_TP_MOD = 201010;
    public static final int CFGU_CUR = 201011;
    public static final int CFGU_CO = 201012;
    public static final int CFGU_COB_ENT = 201013;
    public static final int CFGU_LAN = 201014;
    public static final int CFGU_PARAM_ERP = 201015;
    public static final int CFGU_PARAM_CO = 201016;
    public static final int CFGU_CERT = 201017;

    public static final int CFGX_COB_ENT_CASH = 201501;
    public static final int CFGX_COB_ENT_WH = 201502;
    public static final int CFGX_COB_ENT_POS = 201503;
    public static final int CFGX_COB_ENT_PLANT = 201504;
    public static final int CFGX_CT_ENT_CFG = 201505;

    public static final int USRS_TP_LEV = 202001;
    public static final int USRS_TP_PRV = 202002;
    public static final int USRS_TP_ROL = 202003;
    public static final int USRS_PRV = 202004;
    public static final int USRS_ROL = 202005;
    public static final int USRS_ROL_PRV = 202006;
    public static final int USRU_ACCESS_CO = 202007;
    public static final int USRU_ACCESS_COB = 202008;
    public static final int USRU_ACCESS_COB_ENT = 202009;
    public static final int USRU_ACCESS_COB_ENT_UNIV = 202010;
    public static final int USRU_PRV_USR = 202011;
    public static final int USRU_PRV_CO = 202012;
    public static final int USRU_PRV_COB = 202013;
    public static final int USRU_ROL_USR = 202014;
    public static final int USRU_ROL_CO = 202015;
    public static final int USRU_ROL_COB = 202016;
    public static final int USRU_PREF = 202017;
    public static final int USRU_USR = 202018;

    public static final int USRX_RIGHT = 202501;
    public static final int USRX_RIGHT_PRV = 202502;
    public static final int USRX_RIGHT_ROL = 202503;
    public static final int USRX_TP_ROL_ALL = 202504;

    public static final int LOCU_CTY = 203001;

    public static final int BPSS_CT_BP = 204001;
    public static final int BPSS_TP_BP_IDY = 204002;
    public static final int BPSS_TP_BP_ATT = 204003;
    public static final int BPSS_TP_BPB = 204004;
    public static final int BPSS_TP_ADD = 204005;
    public static final int BPSS_TP_ADD_FMT = 204006;
    public static final int BPSS_TP_CON = 204007;
    public static final int BPSS_TP_TEL = 204008;
    public static final int BPSS_TP_CRED = 204009;
    public static final int BPSS_TP_CFD_ADD = 204010;
    public static final int BPSS_TP_RISK = 204011;
    public static final int BPSU_BP = 204012;
    public static final int BPSU_BP_CT = 204013;
    public static final int BPSU_BP_ATT = 204014;
    public static final int BPSU_BP_BA = 204015;
    public static final int BPSU_BP_NTS = 204016;
    public static final int BPSU_BPB = 204017;
    public static final int BPSU_BPB_NTS = 204018;
    public static final int BPSU_BPB_ADD = 204019;
    public static final int BPSU_BPB_ADD_NTS = 204020;
    public static final int BPSU_BPB_CON = 204021;
    public static final int BPSU_BANK_ACC = 204022;
    public static final int BPSU_BANK_ACC_CARD = 204023;
    public static final int BPSU_BANK_ACC_NTS = 204024;
    public static final int BPSU_BANK_ACC_LAY_BANK = 204025;
    public static final int BPSU_TP_BP = 204026;
    public static final int BPSU_BA = 204027;
    public static final int BPSS_LINK = 204028;

    public static final int BPSX_TP_BP_CO = 204501;
    public static final int BPSX_TP_BP_SUP = 204502;
    public static final int BPSX_TP_BP_CUS = 204503;
    public static final int BPSX_TP_BP_CDR = 204504;
    public static final int BPSX_TP_BP_DBR = 204505;
    public static final int BPSX_TP_BP_EMP = 204506;

    public static final int BPSX_BP_CO = 204507;
    public static final int BPSX_BP_SUP = 204508;
    public static final int BPSX_BP_CUS = 204509;
    public static final int BPSX_BP_CDR = 204510;
    public static final int BPSX_BP_DBR = 204511;
    public static final int BPSX_BP_EMP = 204512;

    public static final int BPSX_BP_X_SUP_CUS = 204514;
    public static final int BPSX_BP_X_CDR_DBR = 204515;
    public static final int BPSX_BP_CT = 204518;

    public static final int BPSX_BPB_CON_SUP = 204521;
    public static final int BPSX_BPB_CON_CUS = 204522;
    public static final int BPSX_BPB_CON_CDR = 204523;
    public static final int BPSX_BPB_CON_DBR = 204524;
    public static final int BPSX_BPB_CON_EMP = 204525;

    public static final int BPSX_BPB_EMP = 204526;
    public static final int BPSX_BP_ITEM_DESC = 204527;

    public static final int BPSX_BPB_SUP = 204528;
    public static final int BPSX_BPB_CUS = 204529;
    public static final int BPSX_BPB_CDR = 204530;
    public static final int BPSX_BPB_DBR = 204531;

    public static final int BPSX_BANK_ACC_SUP = 204532;
    public static final int BPSX_BANK_ACC_CUS = 204533;
    public static final int BPSX_BANK_ACC_CDR = 204534;
    public static final int BPSX_BANK_ACC_DBR = 204535;
    public static final int BPSX_BANK_ACC_EMP = 204536;

    public static final int BPSX_BPB_ADD_SUP = 204537;
    public static final int BPSX_BPB_ADD_CUS = 204538;
    public static final int BPSX_BPB_ADD_CDR = 204539;
    public static final int BPSX_BPB_ADD_DBR = 204540;
    public static final int BPSX_BPB_ADD_EMP = 204541;

    public static final int BPSX_BANK_ACC_CHECK = 204544;
    public static final int BPSX_BANK_ACC = 204545;

    public static final int BPSX_BP_ATT_BANK = 204551;
    public static final int BPSX_BP_ATT_CARR = 204552;
    public static final int BPSX_BP_ATT_EMP = 204553;
    public static final int BPSX_BP_ATT_EMP_MFG = 204517;
    public static final int BPSX_BP_ATT_SAL_AGT = 204555;

    public static final int ITMS_CT_ITEM = 205001;
    public static final int ITMS_CL_ITEM = 205002;
    public static final int ITMS_TP_ITEM = 205003;
    public static final int ITMS_TP_SNR = 205004;
    public static final int ITMU_IFAM = 205005;
    public static final int ITMU_IGRP = 205006;
    public static final int ITMU_IGEN = 205007;
    public static final int ITMU_IGEN_BA = 205008;
    public static final int ITMU_LINE = 205009;
    public static final int ITMU_ITEM = 205010;
    public static final int ITMU_ITEM_BARC = 205011;
    public static final int ITMU_CFG_ITEM_LAN = 205012;
    public static final int ITMU_CFG_ITEM_BP = 205013;
    public static final int ITMU_TP_UNIT = 205014;
    public static final int ITMU_TP_LEV = 205015;
    public static final int ITMU_UNIT = 205016;
    public static final int ITMU_TP_VAR = 205017;
    public static final int ITMU_VAR = 205018;
    public static final int ITMU_BRD = 205019;
    public static final int ITMU_MFR = 205020;
    public static final int ITMU_EMT = 205021;
    public static final int ITMU_TP_BRD = 205022;
    public static final int ITMU_TP_EMT = 205023;
    public static final int ITMU_TP_MFR = 205024;
    public static final int ITMS_TP_LOT_REQ = 205025;

    public static final int ITMX_ITEM_BY_KEY = 205501;
    public static final int ITMX_ITEM_BY_NAME = 205502;
    public static final int ITMX_ITEM_BY_BRAND = 205503;
    public static final int ITMX_ITEM_BY_MANUFACTURER = 205504;
    public static final int ITMX_ITEM_PACK = 205507;
    public static final int ITMX_ITEM_IOG = 205508;
    public static final int ITMX_IGEN_LINE = 205509;
    public static final int ITMX_ITEM_BOM_ITEM = 205510;
    public static final int ITMX_ITEM_BOM_LEVEL = 205511;
    public static final int ITMX_ITEM_SIMPLE = 205512;
    public static final int ITMX_ITEM_IDX_SAL_PRO = 205521;
    public static final int ITMX_ITEM_IDX_SAL_SRV = 205522;
    public static final int ITMX_ITEM_IDX_ASS_ASS = 205523;
    public static final int ITMX_ITEM_IDX_PUR_CON = 205524;
    public static final int ITMX_ITEM_IDX_PUR_EXP = 205525;
    public static final int ITMX_ITEM_IDX_EXP_MFG = 205526;
    public static final int ITMX_ITEM_IDX_EXP_OPE = 205527;

    public static final int FINS_TP_TAX = 206001;
    public static final int FINS_TP_TAX_CAL = 206002;
    public static final int FINS_TP_TAX_APP = 206003;
    public static final int FINS_TP_BKR = 206004;
    public static final int FINS_TP_ACC_MOV = 206005;
    public static final int FINS_CL_ACC_MOV = 206006;
    public static final int FINS_CLS_ACC_MOV = 206007;
    public static final int FINS_TP_ACC = 206008;
    public static final int FINS_CL_ACC = 206009;
    public static final int FINS_CLS_ACC = 206010;
    public static final int FINS_TP_ACC_SPE= 206011;
    public static final int FINS_TP_ACC_SYS = 206012;
    public static final int FINS_CT_SYS_MOV = 206013;
    public static final int FINS_TP_SYS_MOV = 206014;
    public static final int FINS_CT_ACC_CASH = 206015;
    public static final int FINS_TP_ACC_CASH = 206016;
    public static final int FINS_TP_ACC_BP = 206017;
    public static final int FINS_TP_ACC_ITEM = 206018;
    public static final int FINS_TP_CARD = 206019;
    public static final int FINS_TP_PAY_BANK = 206020;
    public static final int FINS_ST_FIN_MOV = 206021;
    public static final int FINU_TAX_REG = 206022;
    public static final int FINU_TAX_IDY = 206023;
    public static final int FINU_TAX_BAS = 206024;
    public static final int FINU_TAX = 206025;
    public static final int FINU_CARD_ISS = 206026;
    public static final int FINU_CHECK_FMT = 206027;
    public static final int FINU_CHECK_FMT_GP = 206028;
    public static final int FINU_TP_REC = 206029;
    public static final int FINU_TP_ACC_USR = 206030;
    public static final int FINU_CL_ACC_USR = 206031;
    public static final int FINU_CLS_ACC_USR = 206032;
    public static final int FINU_TP_ACC_LEDGER = 206033;
    public static final int FINU_TP_ACC_EBITDA = 206034;
    public static final int FINU_TP_ASSET_FIX = 206035;
    public static final int FINU_TP_ASSET_DIF = 206036;
    public static final int FINU_TP_LIABTY_DIF = 206037;
    public static final int FINU_TP_EXPEN_OP = 206038;
    public static final int FINU_TP_ADM_CPT = 206039;
    public static final int FINU_TP_TAX_CPT = 206040;
    public static final int FINU_TP_LAY_BANK = 206041;
    public static final int FINU_COST_GIC = 206042;
    public static final int FIN_YEAR = 206043;
    public static final int FIN_YEAR_PER = 206044;
    public static final int FIN_EXC_RATE = 206045;
    public static final int FIN_ACC = 206046;
    public static final int FIN_CC = 206047;
    public static final int FIN_ACC_CASH = 206048;
    public static final int FIN_CHECK_WAL = 206049;
    public static final int FIN_CHECK = 206050;
    public static final int FIN_TAX_GRP = 206051;
    public static final int FIN_TAX_GRP_ETY = 206052;
    public static final int FIN_TAX_GRP_IGEN = 206053;
    public static final int FIN_TAX_GRP_ITEM = 206054;
    public static final int FIN_BKK_NUM = 206055;
    public static final int FIN_REC = 206056;
    public static final int FIN_REC_ETY = 206057;
    public static final int FIN_ACC_BP = 206058;
    public static final int FIN_ACC_BP_ETY = 206059;
    public static final int FIN_ACC_BP_TP_BP = 206060;
    public static final int FIN_ACC_BP_BP = 206061;
    public static final int FIN_ACC_ITEM = 206062;
    public static final int FIN_ACC_ITEM_ETY = 206063;
    public static final int FIN_ACC_ITEM_ITEM = 206064;
    public static final int FIN_ACC_TAX = 206065;
    public static final int FIN_ACC_COB_ENT = 206066;
    public static final int FIN_CC_ITEM = 206067;
    public static final int FIN_COB_BKC = 206068;
    public static final int FIN_BKC = 206069;
    public static final int FINS_FISCAL_ACC = 206071;
    public static final int FINS_FISCAL_CUR = 206072;
    public static final int FINS_FISCAL_BANK = 206073;
    public static final int FINS_FISCAL_PAY_MET = 206074;
    public static final int FIN_LAY_BANK = 206075;

    public static final int FINX_REC_CASH = 206101;
    public static final int FINX_MOVES_ACC = 206501;
    public static final int FINX_ACC_MAJOR = 206502;
    public static final int FINX_CC_MAJOR = 206503;
    public static final int FINX_REC_HEADER = 206504;
    public static final int FINX_TP_REC_ALL = 206505;
    public static final int FINX_TP_REC_USER = 206506;
    public static final int FINX_TP_ACC_CASH = 206507;
    public static final int FINX_TP_ACC_CASH_BANK = 206508;
    public static final int FINX_ACC_CASH_CASH = 206509;
    public static final int FINX_ACC_CASH_BANK = 206510;
    public static final int FINX_ACC_CASH_BANK_CHECK = 206511;
    public static final int FINX_TAX_GRP_ALL_ITEM = 206512;
    public static final int FINX_TAX_GRP_ALL_IGEN = 206513;
    public static final int FINX_TAX_BAS_TAX = 206514;
    public static final int FINX_ACCOUNTING = 206515;
    public static final int FINX_ACC_BP_QRY = 206516;
    public static final int FINX_ACC_ITEM_QRY = 206517;
    public static final int FINX_REC_USER = 206518;
    public static final int FINX_REC_RO = 206519;
    public static final int FINX_REC_DPS = 206520;

    public static final int TRNS_CT_DPS = 207001;
    public static final int TRNS_CL_DPS = 207002;
    public static final int TRNS_TP_DPS_ADJ = 207003;
    public static final int TRNS_STP_DPS_ADJ = 207004;
    public static final int TRNS_CT_IOG = 207005;
    public static final int TRNS_CL_IOG = 207006;
    public static final int TRNS_TP_IOG = 207007;
    public static final int TRNS_TP_PAY = 207011;
    public static final int TRNS_TP_PAY_WAY = 207012;
    public static final int TRNS_TP_LINK = 207013;
    public static final int TRNS_TP_DPS_EVT = 207014;
    public static final int TRNS_TP_DPS_PRT = 207015;
    public static final int TRNS_TP_DPS_ETY = 207016;
    public static final int TRNS_ST_DPS = 207017;
    public static final int TRNS_ST_DPS_VAL = 207018;
    public static final int TRNS_ST_DPS_AUTHORN = 207019;
    public static final int TRNS_CT_SIGN = 207020;
    public static final int TRNS_TP_SIGN = 207021;
    public static final int TRNS_TP_XML = 207022;
    public static final int TRNU_DPS_NAT = 207024;
    public static final int TRNU_TP_DPS = 207025;
    public static final int TRNU_TP_DPS_SRC_ITEM = 207023;  // out of place, because of number! (sflores, 2015-10-01)
    public static final int TRNU_TP_PAY_SYS = 207026;
    public static final int TRNU_TP_IOG_ADJ = 207027;
    public static final int TRNU_PAC = 207030;              // out of place, because of number! (sflores, 2015-10-01)
    public static final int TRN_DNS_DPS = 207028;
    public static final int TRN_DNS_DIOG = 207029;
    public static final int TRN_DNC_DPS_COB = 207031;
    public static final int TRN_DNC_DPS_COB_ENT = 207032;
    public static final int TRN_DNC_DIOG_COB = 207033;
    public static final int TRN_DNC_DIOG_COB_ENT = 207034;
    public static final int TRN_BP_BLOCK = 207037;
    public static final int TRN_SUP_LT_CO = 207038;
    public static final int TRN_SUP_LT_COB = 207039;
    public static final int TRN_SYS_NTS = 207040;
    public static final int TRN_DPS = 207041;
    public static final int TRN_DPS_SND_LOG = 207042;
    public static final int TRN_DPS_ADD = 207043;
    public static final int TRN_DPS_ADD_ETY = 207044;
    public static final int TRN_DPS_EVT = 207046;
    public static final int TRN_DPS_NTS = 207047;
    public static final int TRN_DPS_ETY = 207048;
    public static final int TRN_DPS_ETY_NTS = 207049;
    public static final int TRN_DPS_ETY_PRC = 207063;       // out of place, because of number! (sflores, 2015-03-18)
    public static final int TRN_DPS_ETY_TAX = 207050;
    public static final int TRN_DPS_ETY_COMMS = 207051;
    public static final int TRN_DPS_RISS = 207052;
    public static final int TRN_DPS_REPL = 207053;
    public static final int TRN_DPS_DPS_SUPPLY = 207054;
    public static final int TRN_DPS_DPS_ADJ = 207055;
    public static final int TRN_DPS_IOG_CHG = 207056;
    public static final int TRN_DPS_IOG_WAR = 207057;
    public static final int TRN_DPS_REC = 207058;
    public static final int TRN_DIOG = 207059;
    public static final int TRN_DIOG_NTS = 207060;
    public static final int TRN_DIOG_ETY = 207061;
    public static final int TRN_DIOG_REC = 207062;
    public static final int TRN_CTR = 207070;
    public static final int TRN_CTR_ETY = 207071;
    public static final int TRN_DSM = 207072;
    public static final int TRN_DSM_NTS = 207073;
    public static final int TRN_DSM_ETY = 207074;
    public static final int TRN_DSM_ETY_NTS = 207075;
    public static final int TRN_DSM_REC = 207076;
    public static final int TRN_USR_CFG = 207077;           // XXX Review if is still needed
    public static final int TRN_USR_CFG_IFAM = 207078;      // XXX Review if is still needed
    public static final int TRN_USR_CFG_BA = 207079;        // XXX Review if is still needed
    public static final int TRN_LOT = 207080;
    public static final int TRN_STK_CFG = 207081;
    public static final int TRN_STK_CFG_ITEM = 207082;
    public static final int TRN_STK_CFG_DNS = 207083;
    public static final int TRN_STK = 207084;
    public static final int TRN_SIGN = 207085;
    public static final int TRN_DNC_DPS = 207086;
    public static final int TRN_DNC_DPS_DNS = 207087;
    public static final int TRN_DNC_DIOG = 207088;
    public static final int TRN_DNC_DIOG_DNS = 207089;
    public static final int TRNS_TP_XML_DVY = 207090;   // out of place! (sflores, 2014-01-06)
    public static final int TRNS_ST_XML_DVY = 207091;   // out of place! (sflores, 2014-01-06)
    public static final int TRNS_TP_CFD = 207092;       // out of place! (sflores, 2014-01-28)
    public static final int TRN_DPS_XML_DVY = 207094;   // XXX eliminate! (sflores, 2014-01-28)
    public static final int TRN_CFD = 207095;           // out of place! (sflores, 2014-01-28)
    public static final int TRN_CFD_SND_LOG = 207096;   // out of place! (sflores, 2014-05-12)
    public static final int TRN_PAC = 207097;           // out of place! (sflores, 2014-02-17)
    public static final int TRN_TP_CFD_PAC = 207098;    // out of place! (sflores, 2014-02-17)
    public static final int TRN_MMS_LOG = 207099;       // out of place! (sflores, 2014-01-28)
    public static final int TRN_CFD_SIGN_LOG = 207100;  // out of place! (sflores, 2014-09-01)

    public static final int TRNX_DPS_RO = 207505;
    public static final int TRNX_DPS_QRY = 207506;

    public static final int TRNX_DPS_BAL = 207511;
    public static final int TRNX_DPS_FILL = 207512;
    public static final int TRNX_DPS_PEND_LINK = 207513;
    public static final int TRNX_DPS_PEND_ADJ = 207514;
    public static final int TRNX_DPS_BACKORDER = 207516;
    public static final int TRNX_DPS_BAL_AGING = 207519;
    public static final int TRNX_PRICE_HIST = 207520;
    public static final int TRNX_DPS_COMMS_PEND = 207521;
    public static final int TRNX_STAMP_AVL = 207522;
    public static final int TRNX_STAMP_MOV = 207523;
    public static final int TRNX_STAMP_SIGN = 207524;
    public static final int TRNX_STAMP_SIGN_PEND = 207525;

    public static final int TRNX_DPS_PAY_PEND = 207525;
    public static final int TRNX_DPS_PAYED = 207526;
    public static final int TRNX_DPS_PAYS = 207527;
    public static final int TRNX_DPS_SHIP_PEND_LINK = 207528;

    public static final int TRNX_DPS_LINK_PEND = 207531;
    public static final int TRNX_DPS_LINK_PEND_ETY = 207532;
    public static final int TRNX_DPS_LINKED = 207533;
    public static final int TRNX_DPS_LINKED_ETY = 207534;

    public static final int TRNX_DPS_LINKS = 207536;
    public static final int TRNX_DPS_LINKS_TRACE = 207537;

    public static final int TRNX_DPS_SUPPLY_PEND = 207541;
    public static final int TRNX_DPS_SUPPLY_PEND_ETY = 207542;
    public static final int TRNX_DPS_SUPPLIED = 207543;
    public static final int TRNX_DPS_SUPPLIED_ETY = 207544;

    public static final int TRNX_DPS_RETURN_PEND = 207551;
    public static final int TRNX_DPS_RETURN_PEND_ETY = 207552;
    public static final int TRNX_DPS_RETURNED = 207553;
    public static final int TRNX_DPS_RETURNED_ETY = 207554;

    public static final int TRNX_DPS_AUTHORIZE_PEND = 207561;
    public static final int TRNX_DPS_AUTHORIZED = 207562;

    public static final int TRNX_DPS_AUDIT_PEND = 207571;
    public static final int TRNX_DPS_AUDITED = 207572;
    public static final int TRNX_DPS_SEND_PEND = 207573;
    public static final int TRNX_DPS_SENT = 207574;

    public static final int TRNX_STK_STK = 207581;
    public static final int TRNX_STK_STK_WH = 207582;
    public static final int TRNX_STK_LOT = 207583;
    public static final int TRNX_STK_LOT_WH = 207584;
    public static final int TRNX_STK_MOVES = 207585;
    public static final int TRNX_STK_MOVES_ETY = 207586;
    public static final int TRNX_STK_ROTATION = 207587;
    public static final int TRNX_STK_COMSUME = 207588;
    public static final int TRNX_DIOG_MFG = 207589;
    public static final int TRNX_DIOG_MFG_RM = 207590;
    public static final int TRNX_DIOG_MFG_WP = 207591;
    public static final int TRNX_DIOG_MFG_FG = 207592;
    public static final int TRNX_DIOG_MFG_CON = 207593;
    public static final int TRNX_DIOG_MFG_MOVE_IN = 207594;
    public static final int TRNX_DIOG_MFG_MOVE_OUT = 207595;
    public static final int TRNX_DIOG_MFG_MOVE_ASG = 207596;
    public static final int TRNX_DIOG_MFG_MOVE_RET = 207597;

    public static final int TRNX_DIOG_AUDIT_PEND = 207599;
    public static final int TRNX_DIOG_AUDITED = 207600;

    // XXX
    public static final int TRNX_DSM_ETY_SOURCE = 207601;
    public static final int TRNX_DSM_ETY_DESTINY = 207602;
    public static final int TRNX_DPS_SRC = 207603;
    public static final int TRNX_DPS_DES = 207604;
    public static final int TRNX_DPS_ADJ = 207605;
    public static final int TRNX_TP_DPS = 207606;
    public static final int TRNX_SAL_PUR_TOT = 207607;
    public static final int TRNX_SAL_PUR_GLB = 207608;

    public static final int TRNX_DPS_ACT_ANNUL = 207611;
    public static final int TRNX_DPS_ACT_RISS = 207612;
    public static final int TRNX_DPS_ACT_REPL = 207613;
    public static final int TRNX_DPS_ACT_VIEW_LINKS = 207614;
    // XXX

    public static final int TRNX_MFG_ORD = 207620;
    public static final int TRNX_MFG_ORD_ASSIGN_PEND = 207621;
    public static final int TRNX_MFG_ORD_ASSIGN_PEND_ETY = 207622;
    public static final int TRNX_MFG_ORD_ASSIGNED = 207623;
    public static final int TRNX_MFG_ORD_ASSIGNED_ETY = 207624;
    public static final int TRNX_MFG_ORD_CONSUME_PEND = 207626;
    public static final int TRNX_MFG_ORD_CONSUME_PEND_ETY = 207627;
    public static final int TRNX_MFG_ORD_CONSUMED = 207628;
    public static final int TRNX_MFG_ORD_CONSUMED_ETY = 207629;
    public static final int TRNX_MFG_ORD_CONSUME_PEND_MASS = 207630;
    public static final int TRNX_MFG_ORD_CONSUME_PEND_ETY_MASS = 207631;
    public static final int TRNX_MFG_ORD_CONSUMED_MASS = 207632;
    public static final int TRNX_MFG_ORD_CONSUMED_ETY_MASS = 207633;
    public static final int TRNX_MFG_ORD_FINISH_PEND = 207634;
    public static final int TRNX_MFG_ORD_FINISH_PEND_ETY = 207635;
    public static final int TRNX_MFG_ORD_FINISHED = 207636;
    public static final int TRNX_MFG_ORD_FINISHED_ETY = 207637;

    public static final int TRNR_ACCOUNT_CASH_PDAY = 207640;
    public static final int TRNR_ACCOUNT_BANK_PDAY = 207641;
    public static final int TRNR_ACCOUNT_CASH_CON = 207642;
    public static final int TRNR_ACCOUNT_BANK_CON = 207643;

    public static final int MKTS_TP_DISC_APP = 208001;
    public static final int MKTU_TP_CUS = 208002;
    public static final int MKTU_TP_SAL_AGT = 208003;
    public static final int MKTU_MKT_SEGM = 208004;
    public static final int MKTU_MKT_SEGM_SUB = 208005;
    public static final int MKTU_DIST_CHAN = 208006;
    public static final int MKTU_SAL_ROUTE = 208007;
    public static final int MKTU_SAL_AGT = 208008;
    public static final int MKT_CFG_CUS = 208009;
    public static final int MKT_CFG_CUSB = 208010;
    public static final int MKT_CFG_SAL_AGT = 208011;
    public static final int MKT_PLIST_GRP = 208012;
    public static final int MKT_PLIST = 208013;
    public static final int MKT_PLIST_ITEM = 208014;
    public static final int MKT_PLIST_PRICE = 208015;
    public static final int MKT_PLIST_BP_LINK = 208016;
    public static final int MKT_PLIST_CUS = 208017;     // XXX eliminate! (sflores, 2014-02-26)
    public static final int MKT_PLIST_CUS_TP = 208018;  // XXX eliminate! (sflores, 2014-02-26)
    public static final int MKT_COMMS_SAL_AGT = 208019;
    public static final int MKT_COMMS_SAL_AGT_TP = 208020;
    public static final int MKT_COMMS_DPS = 208021;     // XXX eliminate, no loger useful! (sflores, 2014-09-01)
    public static final int MKT_COMMS_LOG = 208021;
    public static final int MKT_COMMS = 208022;
    public static final int MKT_COMMS_PAY = 208023;
    public static final int MKT_COMMS_PAY_ETY = 208024;

    public static final int MKTX_COMMS_ITEM_SAL_AGT = 208030;
    public static final int MKTX_COMMS_SAL_AGT_CONS = 208031;
    public static final int MKTX_COMMS_SAL_AGT_TP_CONS = 208032;
    public static final int MKTX_COMMS_SAL_AGT_TPS = 208033;
    public static final int MKTX_COMMS_SAL_AGTS = 208034;
    public static final int MKTX_COMMS_RES = 208035;
    public static final int MKTX_COMMS_DET = 208036;
    public static final int MKTX_COMMS_NOT_PAY = 208037;
    public static final int MKTX_COMMS_WITH_PAY = 208038;
    public static final int MKTX_COMMS_ALL = 208039;
    public static final int MKTX_COMMS_DPS_SAL_AGT = 208040;

    public static final int MFGS_ST_ORD = 210001;
    public static final int MFGS_PTY_ORD = 210002;
    public static final int MFGS_TP_REQ = 210003;
    public static final int MFGS_TP_COST_OBJ = 210004;
    public static final int MFGU_TP_ORD = 210005;
    public static final int MFGU_TURN = 210006;
    public static final int MFGU_GANG = 210007;
    public static final int MFGU_GANG_ETY = 210008;
    public static final int MFG_BOM = 210009;
    public static final int MFG_BOM_NTS = 210010;
    public static final int MFG_BOM_SUB = 210011;
    public static final int MFG_SGDS = 210012;
    public static final int MFG_LINE = 210013;
    public static final int MFG_ORD = 210014;
    public static final int MFG_ORD_PER = 210015;
    public static final int MFG_ORD_NTS = 210016;
    public static final int MFG_ORD_CHG = 210017;
    public static final int MFG_ORD_CHG_ETY = 210018;
    public static final int MFG_ORD_CHG_ETY_LOT = 210019;
    public static final int MFG_ORD_SGDS = 210020;
    public static final int MFG_EXP = 210021;
    public static final int MFG_EXP_ORD = 210022;
    public static final int MFG_EXP_ETY_ITEM = 210023;
    public static final int MFG_EXP_ETY = 210024;
    public static final int MFG_REQ = 210025;
    public static final int MFG_EXP_REQ = 210026;
    public static final int MFG_REQ_ETY = 210027;
    public static final int MFG_REQ_PUR = 210028;
    public static final int MFG_DRC = 210029;
    public static final int MFG_DRC_ETY = 210030;
    public static final int MFG_DRC_ETY_HR = 210031;
    public static final int MFG_COST = 210032;
    public static final int MFG_LT_CO = 210033;
    public static final int MFG_LT_COB = 210034;
    public static final int MFGU_LINE = 210035;
    public static final int MFGU_LINE_CFG_ITEM = 210036;

    public static final int MFG_REP_MON = 210100;

    public static final int MFGX_BOM_ITEMS = 210200;
    public static final int MFGX_BOM_LEV = 210201;
    public static final int MFGX_COST_CLS_PER = 210202;
    public static final int MFGX_COST_DIR = 210203;
    public static final int MFGX_COST_EMP = 210204;
    public static final int MFGX_COST_IND = 210205;
    public static final int MFGX_EXP_ETY_LOT_VIEW = 210206;
    public static final int MFGX_EXP_FOR = 210207;
    public static final int MFGX_GANG_EMP = 210208;
    public static final int MFGX_LT = 210209;
    public static final int MFGX_ORD = 210210;
    public static final int MFGX_ORD_ALL = 210211;
    public static final int MFGX_ORD_FAT_SON = 210212;
    public static final int MFGX_ORD_FOR = 210213;
    public static final int MFGX_ORD_LOT_FG = 210214;
    public static final int MFGX_ORD_LOT_RM = 210215;
    public static final int MFGX_ORD_MAIN_NA = 210216;
    public static final int MFGX_ORD_MAIN_FA = 210217;
    public static final int MFGX_ORD_MAIN_CH = 210218;
    public static final int MFGX_ORD_PERF = 210219;
    public static final int MFGX_PROD = 210220;
    public static final int MFGX_PROD_BY_ITM = 210221;
    public static final int MFGX_PROD_BY_IGEN = 210222;
    public static final int MFGX_PROD_BY_ITM_BIZ = 210223;
    public static final int MFGX_PROD_BY_BIZ_ITM = 210224;

    public static final int HRS_FORMER_PAYR = 220001;
    public static final int HRS_FORMER_PAYR_EMP = 220002;
    public static final int HRS_FORMER_PAYR_MOV = 220003;

    public static final java.lang.String MSG_ERR_DATA_NOT_FOUND = "El tipo de registro no existe.";
}
