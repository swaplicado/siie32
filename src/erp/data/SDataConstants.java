/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.data;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores, Uriel Castañeda, Claudio Peña, Daniel López, Sergio Flores, Isabel Servín, Claudio Peña
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
    public static final int MOD_QLT = 110000;
    public static final int MOD_XXX = 199999;

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
    public static final int GLOBAL_CAT_QLT = 212000;
    public static final int GLOBAL_CAT_XXX = 212999;

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
    public static final int CFG_PARAM_ERP = 201015;
    public static final int CFG_PARAM_CO = 201016;
    public static final int CFGU_CERT = 201017;
    public static final int CFGU_SHIFT = 201018;

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
    public static final int USRU_ROL_USR = 202014;
    public static final int USRU_ROL_CO = 202015;
    public static final int USRU_USR = 202018;
    public static final int USRU_USR_REDIS_LOCKS = 20219;
    public static final int USRU_USR_UPDATED = 202020;
    public static final int USRU_USR_REDIS = 202021;

    public static final int USRX_RIGHT = 202501;
    public static final int USRX_RIGHT_PRV = 202502;
    public static final int USRX_RIGHT_ROL = 202503;
    public static final int USRX_TP_ROL_ALL = 202504;
    public static final int USRX_FIN_REC = 202511;
    public static final int USRX_USR_FUNC = 202521;

    public static final int LOCU_CTY = 203001;
    public static final int LOCU_STA = 203002;
    public static final int LOCS_BOL_COUNTY = 203011;
    public static final int LOCS_BOL_LOCALITY = 203012;
    public static final int LOCS_BOL_ZIP_CODE = 203013;
    public static final int LOCS_BOL_NEI_ZIP_CODE = 203014;

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
    public static final int BPSS_RISK = 204011;
    public static final int BPSS_LINK = 204028;
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
    public static final int BPSU_BP_ADDEE = 204031;

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
    public static final int BPSX_BP_SUP_FI = 204519;
    public static final int BPSX_BP_CUS_FI = 204520;
    public static final int BPSX_BP_EMP_CON_EXP = 204561; // employee contract expiration
    public static final int BPSX_BP_EMP_REL = 204571; // employee relatives
    public static final int BPSX_BP_INT_SUP = 204608; //international suppliers
    public static final int BPSX_BP_INT_CUS = 204609; //international customers

    public static final int BPSX_BP_X_SUP_CUS = 204514;
    public static final int BPSX_BP_X_CDR_DBR = 204515;
    public static final int BPSX_BP_X_SUP_CDR = 204516;
    public static final int BPSX_BP_X_CUS_DBR = 204517;
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

    public static final int BPSX_BP_ATT_BANK = 204551;
    public static final int BPSX_BP_ATT_CARR = 204552;
    public static final int BPSX_BP_ATT_EMP = 204553;
    public static final int BPSX_BP_ATT_EMP_MFG = 204554;
    public static final int BPSX_BP_ATT_SAL_AGT = 204555;

    public static final int ITMS_CT_ITEM = 205001;
    public static final int ITMS_CL_ITEM = 205002;
    public static final int ITMS_TP_ITEM = 205003;
    public static final int ITMS_TP_SNR = 205004;
    public static final int ITMS_LINK = 205025;
    public static final int ITMS_ST_ITEM = 205026;
    public static final int ITMU_IFAM = 205005;
    public static final int ITMU_IGRP = 205006;
    public static final int ITMU_IGEN = 205007;
    public static final int ITMU_IGEN_BA = 205008;
    public static final int ITMU_LINE = 205009;
    public static final int ITMU_ITEM = 205010;
    public static final int ITMU_ITEM_BARC = 205011;
    public static final int ITMU_CFG_ITEM_LAN = 205012;
    public static final int ITMU_CFG_ITEM_BP = 205013;
    public static final int ITMU_MATCH_ITEM_CPT_BP = 205051;
    public static final int ITMU_TP_UNIT = 205014;
    public static final int ITMU_TP_LEV = 205015;
    public static final int ITMU_UNIT = 205016;
    public static final int ITMU_TP_VAR = 205017;
    public static final int ITMU_VAR = 205018;
    public static final int ITMU_TP_BRD = 205022;
    public static final int ITMU_BRD = 205019;
    public static final int ITMU_TP_MFR = 205024;
    public static final int ITMU_MFR = 205020;
    public static final int ITMU_TP_EMT = 205023;
    public static final int ITMU_EMT = 205021;
    public static final int ITMU_MATCH_ITEM_CPT_BP_COMP = 205151;

    public static final int ITMX_ITEM_BY_KEY = 205501;
    public static final int ITMX_ITEM_BY_NAME = 205502;
    public static final int ITMX_ITEM_BY_BRAND = 205503;
    public static final int ITMX_ITEM_BY_MANUFACTURER = 205504;
    public static final int ITMX_ITEM_BY_PART_NUM = 205505;
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
    public static final int FINS_TP_ACC_SPE = 206011;
    public static final int FINS_TP_ACC_SYS = 206012;
    public static final int FINS_CT_SYS_MOV = 206013;   // fins_ct_sys_mov_xxx
    public static final int FINS_TP_SYS_MOV = 206014;   // fins_tp_sys_mov_xxx
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
    public static final int FINU_BANK_NB_DAY = 206076;
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
    public static final int FINX_ACCOUNTING_ALL = 206521;
    public static final int FINX_ACC_BP_QRY = 206516;
    public static final int FINX_ACC_ITEM_QRY = 206517;
    public static final int FINX_REC_USER = 206518;
    public static final int FINX_REC_RO = 206519;
    public static final int FINX_REC_DPS = 206520;
    public static final int FINX_REC_W_XML = 206521;
    public static final int FINX_REC_CFD_DIRECT = 206522;
    public static final int FINX_REC_CFD_INDIRECT = 206523;
    
    public static final int TRNS_CT_DPS = 207001;
    public static final int TRNS_CL_DPS = 207002;
    public static final int TRNS_TP_DPS_ADJ = 207003;
    public static final int TRNS_STP_DPS_ADJ = 207004;
    public static final int TRNS_CT_IOG = 207005;
    public static final int TRNS_CL_IOG = 207006;
    public static final int TRNS_TP_IOG = 207007;
    public static final int TRNS_TP_PAY = 207011;
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
    public static final int TRNS_TP_XML_DVY = 207090;
    public static final int TRNS_ST_XML_DVY = 207091;
    public static final int TRNS_TP_CFD = 207092;
    public static final int TRNS_TP_STK_SEG = 207131;
    public static final int TRNS_TP_STK_SEG_MOV = 207132;
    public static final int TRNU_DPS_NAT = 207024;
    public static final int TRNU_TP_DPS = 207025;
    public static final int TRNU_TP_DPS_SRC_ITEM = 207023;  // out of place, because of number! (sflores, 2015-10-01)
    public static final int TRNU_TP_PAY_SYS = 207026;
    public static final int TRNU_TP_IOG_ADJ = 207027;
    public static final int TRNU_TP_DPS_ANN = 207030;       // out of place, because of number! (sflores, 2016-09-21)
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
    public static final int TRN_DPS_CFD = 207201;
    public static final int TRN_DPS_CFD_ETY = 207202;
    public static final int TRN_DPS_ADD = 207043;
    public static final int TRN_DPS_ADD_ETY = 207044;
    public static final int TRN_DPS_EVT = 207046;
    public static final int TRN_DPS_NTS = 207047;
    public static final int TRN_DPS_ACK = 207161;
    public static final int TRN_DPS_ETY = 207048;
    public static final int TRN_DPS_ETY_NTS = 207049;
    public static final int TRN_DPS_ETY_PRC = 207063;
    public static final int TRN_DPS_ETY_TAX = 207050;
    public static final int TRN_DPS_ETY_COMMS = 207051;
    public static final int TRN_DPS_ETY_HIST = 207106;
    public static final int TRN_DPS_ETY_ANALYSIS = 207105;
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
    public static final int TRN_USR_DPS_DNS = 207121;
    public static final int TRN_LOT = 207080;
    public static final int TRN_STK_CFG = 207081;
    public static final int TRN_STK_CFG_ITEM = 207082;
    public static final int TRN_STK_CFG_DNS = 207083;
    public static final int TRN_STK = 207084;
    public static final int TRN_STK_SEG = 207101;
    public static final int TRN_STK_SEG_WHS = 207102;
    public static final int TRN_STK_SEG_WHS_ETY = 207103;
    public static final int TRN_PDF = 207094;
    public static final int TRN_CFD = 207095;
    public static final int TRN_CFD_SIGN_LOG = 207100;
    public static final int TRN_CFD_SND_LOG = 207096;
    public static final int TRN_CFD_FIN_REC = 207104;
    public static final int TRN_PAY = 207401;
    public static final int TRN_PAY_PAY = 207402;
    public static final int TRN_PAY_PAY_TAX = 2023403;
    public static final int TRN_PAY_PAY_DOC = 207404;
    public static final int TRN_PAY_PAY_DOC_TAX = 207405;
    public static final int TRN_PAC = 207097;
    public static final int TRN_TP_CFD_PAC = 207098;
    public static final int TRN_SIGN = 207085;
    public static final int TRN_MMS_LOG = 207099;
    public static final int TRN_DVY = 207111;
    public static final int TRN_DVY_ETY = 207112;
    public static final int TRN_DNC_DPS = 207086;
    public static final int TRN_DNC_DPS_DNS = 207087;
    public static final int TRN_DNC_DIOG = 207088;
    public static final int TRN_DNC_DIOG_DNS = 207089;

    public static final int TRNX_DPS_RO_STK = 207504;
    public static final int TRNX_DPS_RO = 207505;
    public static final int TRNX_DPS_QRY = 207506;
    public static final int TRNX_DPS_EDIT = 207507;

    public static final int TRNX_DPS_BAL = 207511;
    public static final int TRNX_DPS_FILL = 207512;
    public static final int TRNX_DPS_PEND_LINK = 207513;
    public static final int TRNX_DPS_PEND_ADJ = 207514;
    public static final int TRNX_DPS_BACKORDER = 207516;
    public static final int TRNX_DPS_BAL_AGING = 207519;
    public static final int TRNX_DPS_LAST_MOV = 207517;
    public static final int TRNX_DPS_ETY_REF = 207529;
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
    public static final int TRNX_DPS_DETAIL = 207535;

    public static final int TRNX_DPS_LINKS = 207536;
    public static final int TRNX_DPS_LINKS_TRACE = 207537;
    public static final int TRNX_DPS_SUPPLY = 207538;

    public static final int TRNX_DPS_SUPPLY_PEND = 207541;
    public static final int TRNX_DPS_SUPPLY_PEND_ETY = 207542;
    public static final int TRNX_DPS_SUPPLIED = 207543;
    public static final int TRNX_DPS_SUPPLIED_ETY = 207544;
    public static final int TRNX_DPS_SUPPLIED_ORDER = 207545; // order whit supplied movements
    public static final int TRNX_DPS_SUPPLIED_ORDER_INVOICE = 207546; // order whit supplied movements invoice

    public static final int TRNX_DPS_RETURN_PEND = 207551;
    public static final int TRNX_DPS_RETURN_PEND_ETY = 207552;
    public static final int TRNX_DPS_RETURNED = 207553;
    public static final int TRNX_DPS_RETURNED_ETY = 207554;

    public static final int TRNX_DPS_AUTHORIZE_PEND = 207561;
    public static final int TRNX_DPS_AUTHORIZED = 207562;
    
    public static final int TRNX_CON_AUTHORIZE_PEND = 207563;
    public static final int TRNX_CON_AUTHORIZED = 207564;

    public static final int TRNX_DPS_AUDIT_PEND = 207571;
    public static final int TRNX_DPS_AUDITED = 207572;
    public static final int TRNX_DPS_SEND_PEND = 207573;
    public static final int TRNX_DPS_SENT = 207574;
    public static final int TRNX_DOC_REMISSION = 207575;

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
    public static final int TRNX_STK_PERIOD = 207598;
    public static final int TRNX_STK_ITEM = 207601;
    public static final int TRNX_STK_ITEM_HIS = 207602;

    public static final int TRNX_DIOG_AUDIT_PEND = 207599;
    public static final int TRNX_DIOG_AUDITED = 207600;
    public static final int TRNX_CFD_PAY_REC = 207611;
    public static final int TRNX_CFD_PAY_REC_EXT = 207612;
    public static final int TRNX_DPS_ACT_VIEW_LINKS = 207614;
    public static final int TRNX_MAINT_DIOG = 207651;
    public static final int TRNX_MAINT_STK = 207652;
    public static final int TRNX_MAINT_MOV = 207653;

    // XXX
    public static final int TRNX_DSM_ETY_SOURCE = 207601;
    public static final int TRNX_DSM_ETY_DESTINY = 207602;
    public static final int TRNX_DPS_SRC = 207603;
    public static final int TRNX_DPS_DES = 207604;
    public static final int TRNX_DPS_ADJ = 207605;
    public static final int TRNX_TP_DPS = 207606;
    public static final int TRNX_SAL_PUR_TOT = 207607;
    public static final int TRNX_SAL_PUR_GLB = 207608;
    public static final int TRNX_OPE_TYPE = 207609;
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
    public static final int MKT_CFG_CUS = 208009;
    public static final int MKT_CFG_CUSB = 208010;
    public static final int MKT_CFG_SAL_AGT = 208011;
    public static final int MKT_PLIST_GRP = 208012;
    public static final int MKT_PLIST = 208013;
    public static final int MKT_PLIST_ITEM = 208014;
    public static final int MKT_PLIST_PRICE = 208015;
    public static final int MKT_PLIST_BP_LINK = 208016;
    public static final int MKT_PLIST_CUS = 208017;
    public static final int MKT_COMMS_SAL_AGT = 208019;
    public static final int MKT_COMMS_SAL_AGT_TP = 208020;
    public static final int MKT_COMMS_LOG = 208021;
    public static final int MKT_COMMS = 208022;
    public static final int MKT_COMMS_PAY = 208023;
    public static final int MKT_COMMS_PAY_ETY = 208024;

    public static final int MKTX_SAL_AGT = 208008;
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
    public static final int MFG_COST = 210032;
    public static final int MFGU_LINE = 210035;
    public static final int MFGU_LINE_CFG_ITEM = 210036;

    public static final int MFG_REP_MON = 210100;

    public static final int MFGX_BOM_ITEMS = 210200;
    public static final int MFGX_BOM_LEV = 210201;
    public static final int MFGX_BOM_COST = 210231;
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

    public static final int HRS_SIE_PAY = 220001;
    public static final int HRS_SIE_PAY_EMP = 220002;
    public static final int HRS_SIE_PAY_MOV = 220003;

    public static final int MAX_ID = 1000000;

    public static final java.lang.String MSG_ERR_DATA_NOT_FOUND = "El tipo de registro no existe.";

    public static final HashMap<Integer, String> TablesMap = new HashMap<>();

    static {
        TablesMap.put(CFGS_CT_ENT, "erp.cfgs_ct_ent");
        TablesMap.put(CFGS_TP_ENT, "erp.cfgs_tp_ent");
        TablesMap.put(CFGS_TP_SORT, "erp.cfgs_tp_sort");
        TablesMap.put(CFGS_TP_BAL, "erp.cfgs_tp_bal");
        TablesMap.put(CFGS_TP_REL, "erp.cfgs_tp_rel");
        TablesMap.put(CFGS_TP_FMT_D, "erp.cfgs_tp_fmt_d");
        TablesMap.put(CFGS_TP_FMT_DT, "erp.cfgs_tp_fmt_dt");
        TablesMap.put(CFGS_TP_FMT_T, "erp.cfgs_tp_fmt_t");
        TablesMap.put(CFGS_TP_DBMS, "erp.cfgs_tp_dbms");
        TablesMap.put(CFGS_TP_MOD, "erp.cfgs_tp_mod");

        TablesMap.put(CFGU_CUR, "erp.cfgu_cur");
        TablesMap.put(CFGU_CO, "erp.cfgu_co");
        TablesMap.put(CFGU_COB_ENT, "erp.cfgu_cob_ent");
        TablesMap.put(CFGU_LAN, "erp.cfgu_lan");
        TablesMap.put(CFG_PARAM_ERP, "erp.cfg_param_erp");
        TablesMap.put(CFGU_CERT, "erp.cfgu_cert");

        TablesMap.put(CFG_PARAM_CO, "erp.cfg_param_co");

        TablesMap.put(USRS_TP_LEV, "erp.usrs_tp_lev");
        TablesMap.put(USRS_TP_PRV, "erp.usrs_tp_prv");
        TablesMap.put(USRS_TP_ROL, "erp.usrs_tp_rol");
        TablesMap.put(USRS_PRV, "erp.usrs_prv");
        TablesMap.put(USRS_ROL, "erp.usrs_rol");
        TablesMap.put(USRS_ROL_PRV, "erp.usrs_rol_prv");

        TablesMap.put(USRU_ACCESS_CO, "erp.usru_access_co");
        TablesMap.put(USRU_ACCESS_COB, "erp.usru_access_cob");
        TablesMap.put(USRU_ACCESS_COB_ENT, "erp.usru_access_cob_ent");
        TablesMap.put(USRU_ACCESS_COB_ENT_UNIV, "erp.usru_access_cob_ent_univ");
        TablesMap.put(USRU_PRV_USR, "erp.usru_prv_usr");
        TablesMap.put(USRU_PRV_CO, "erp.usru_prv_co");
        TablesMap.put(USRU_ROL_USR, "erp.usru_rol_usr");
        TablesMap.put(USRU_ROL_CO, "erp.usru_rol_co");
        TablesMap.put(USRU_USR, "erp.usru_usr");

        TablesMap.put(LOCU_CTY, "erp.locu_cty");
        TablesMap.put(LOCU_STA, "erp.locu_sta");
        TablesMap.put(LOCS_BOL_COUNTY, "erp.locs_bol_county");
        TablesMap.put(LOCS_BOL_LOCALITY, "erp.locs_bol_locality");
        TablesMap.put(LOCS_BOL_ZIP_CODE, "erp.locs_bol_zip_code");
        TablesMap.put(LOCS_BOL_NEI_ZIP_CODE, "erp.locs_bol_nei_zip_code");

        TablesMap.put(BPSS_CT_BP, "erp.bpss_ct_bp");
        TablesMap.put(BPSS_TP_BP_IDY, "erp.bpss_tp_bp_idy");
        TablesMap.put(BPSS_TP_BP_ATT, "erp.bpss_tp_bp_att");
        TablesMap.put(BPSS_TP_BPB, "erp.bpss_tp_bpb");
        TablesMap.put(BPSS_TP_ADD, "erp.bpss_tp_add");
        TablesMap.put(BPSS_TP_ADD_FMT, "erp.bpss_tp_add_fmt");
        TablesMap.put(BPSS_TP_CON, "erp.bpss_tp_con");
        TablesMap.put(BPSS_TP_TEL, "erp.bpss_tp_tel");
        TablesMap.put(BPSS_TP_CRED, "erp.bpss_tp_cred");
        TablesMap.put(BPSS_TP_CFD_ADD, "erp.bpss_tp_cfd_add");
        TablesMap.put(BPSS_RISK, "erp.bpss_risk");
        TablesMap.put(BPSS_LINK, "erp.bpss_link");

        TablesMap.put(BPSU_BP, "erp.bpsu_bp");
        TablesMap.put(BPSU_BP_CT, "erp.bpsu_bp_ct");
        TablesMap.put(BPSU_BP_ATT, "erp.bpsu_bp_att");
        TablesMap.put(BPSU_BP_BA, "erp.bpsu_bp_ba");
        TablesMap.put(BPSU_BP_NTS, "erp.bpsu_bp_nts");
        TablesMap.put(BPSU_BPB, "erp.bpsu_bpb");
        TablesMap.put(BPSU_BPB_NTS, "erp.bpsu_bpb_nts");
        TablesMap.put(BPSU_BPB_ADD, "erp.bpsu_bpb_add");
        TablesMap.put(BPSU_BPB_ADD_NTS, "erp.bpsu_bpb_add_nts");
        TablesMap.put(BPSU_BPB_CON, "erp.bpsu_bpb_con");
        TablesMap.put(BPSU_BANK_ACC, "erp.bpsu_bank_acc");
        TablesMap.put(BPSU_BANK_ACC_LAY_BANK, "erp.bpsu_bank_acc_lay_bank");
        TablesMap.put(BPSU_BANK_ACC_CARD, "erp.bpsu_bank_acc_card");
        TablesMap.put(BPSU_BANK_ACC_NTS, "erp.bpsu_bank_acc_nts");
        TablesMap.put(BPSU_TP_BP, "erp.bpsu_tp_bp");
        TablesMap.put(BPSU_BA, "erp.bpsu_ba");
        TablesMap.put(BPSU_BP_ADDEE, "erp.bpsu_bp_addee");

        TablesMap.put(ITMS_CT_ITEM, "erp.itms_ct_item");
        TablesMap.put(ITMS_CL_ITEM, "erp.itms_cl_item");
        TablesMap.put(ITMS_TP_ITEM, "erp.itms_tp_item");
        TablesMap.put(ITMS_TP_SNR, "erp.itms_tp_snr");

        TablesMap.put(ITMU_IFAM, "erp.itmu_ifam");
        TablesMap.put(ITMU_IGRP, "erp.itmu_igrp");
        TablesMap.put(ITMU_IGEN, "erp.itmu_igen");
        TablesMap.put(ITMU_IGEN_BA, "erp.itmu_igen_ba");
        TablesMap.put(ITMU_LINE, "erp.itmu_line");
        TablesMap.put(ITMU_ITEM, "erp.itmu_item");
        TablesMap.put(ITMU_ITEM_BARC, "erp.itmu_item_barc");
        TablesMap.put(ITMU_CFG_ITEM_LAN, "erp.itmu_cfg_item_lan");
        TablesMap.put(ITMU_CFG_ITEM_BP, "erp.itmu_cfg_item_bp");
        TablesMap.put(ITMU_TP_LEV, "erp.itmu_tp_lev");
        TablesMap.put(ITMU_TP_UNIT, "erp.itmu_tp_unit");
        TablesMap.put(ITMU_UNIT, "erp.itmu_unit");
        TablesMap.put(ITMU_TP_VAR, "erp.itmu_tp_var");
        TablesMap.put(ITMU_VAR, "erp.itmu_var");
        TablesMap.put(ITMU_TP_BRD, "erp.itmu_tp_brd");
        TablesMap.put(ITMU_BRD, "erp.itmu_brd");
        TablesMap.put(ITMU_TP_MFR, "erp.itmu_tp_mfr");
        TablesMap.put(ITMU_MFR, "erp.itmu_mfr");
        TablesMap.put(ITMU_TP_EMT, "erp.itmu_tp_emt");
        TablesMap.put(ITMU_EMT, "erp.itmu_emt");

        TablesMap.put(FINS_TP_BKR, "erp.fins_tp_bkr");
        TablesMap.put(FINS_TP_ACC_MOV, "erp.fins_tp_acc_mov");
        TablesMap.put(FINS_CL_ACC_MOV, "erp.fins_cl_acc_mov");
        TablesMap.put(FINS_CLS_ACC_MOV, "erp.fins_cls_acc_mov");
        TablesMap.put(FINS_TP_ACC, "erp.fins_tp_acc");
        TablesMap.put(FINS_CL_ACC, "erp.fins_cl_acc");
        TablesMap.put(FINS_CLS_ACC, "erp.fins_cls_acc");
        TablesMap.put(FINS_TP_ACC_SPE, "erp.fins_tp_acc_spe");
        TablesMap.put(FINS_TP_ACC_SYS, "erp.fins_tp_acc_sys");
        TablesMap.put(FINS_CT_SYS_MOV, "erp.fins_ct_sys_mov_xxx");
        TablesMap.put(FINS_TP_SYS_MOV, "erp.fins_tp_sys_mov_xxx");
        TablesMap.put(FINS_CT_ACC_CASH, "erp.fins_ct_acc_cash");
        TablesMap.put(FINS_TP_ACC_CASH, "erp.fins_tp_acc_cash");
        TablesMap.put(FINS_TP_TAX, "erp.fins_tp_tax");
        TablesMap.put(FINS_TP_TAX_CAL, "erp.fins_tp_tax_cal");
        TablesMap.put(FINS_TP_TAX_APP, "erp.fins_tp_tax_app");
        TablesMap.put(FINS_TP_ACC_BP, "erp.fins_tp_acc_bp");
        TablesMap.put(FINS_TP_ACC_ITEM, "erp.fins_tp_acc_item");
        TablesMap.put(FINS_TP_CARD, "erp.fins_tp_card");
        TablesMap.put(FINS_TP_PAY_BANK, "erp.fins_tp_pay_bank");
        TablesMap.put(FINS_ST_FIN_MOV, "erp.fins_st_fin_mov");
        TablesMap.put(FINS_FISCAL_ACC, "erp.fins_fiscal_acc");
        TablesMap.put(FINS_FISCAL_CUR, "erp.fins_fiscal_cur");
        TablesMap.put(FINS_FISCAL_BANK, "erp.fins_fiscal_bank");
        TablesMap.put(FINS_FISCAL_PAY_MET, "erp.fins_fiscal_pay_met");

        TablesMap.put(FINU_TAX_REG, "erp.finu_tax_reg");
        TablesMap.put(FINU_TAX_IDY, "erp.finu_tax_idy");
        TablesMap.put(FINU_TAX_BAS, "erp.finu_tax_bas");
        TablesMap.put(FINU_TAX, "erp.finu_tax");
        TablesMap.put(FINU_CARD_ISS, "erp.finu_card_iss");
        TablesMap.put(FINU_CHECK_FMT, "erp.finu_check_fmt");
        TablesMap.put(FINU_CHECK_FMT_GP, "erp.finu_check_fmt_gp");
        TablesMap.put(FINU_TP_REC, "erp.finu_tp_rec");
        TablesMap.put(FINU_TP_ACC_USR, "erp.finu_tp_acc_usr");
        TablesMap.put(FINU_CL_ACC_USR, "erp.finu_cl_acc_usr");
        TablesMap.put(FINU_CLS_ACC_USR, "erp.finu_cls_acc_usr");
        TablesMap.put(FINU_TP_ACC_LEDGER, "erp.finu_tp_acc_ledger");
        TablesMap.put(FINU_TP_ACC_EBITDA, "erp.finu_tp_acc_ebitda");
        TablesMap.put(FINU_TP_ASSET_FIX, "erp.finu_tp_asset_fix");
        TablesMap.put(FINU_TP_ASSET_DIF, "erp.finu_tp_asset_dif");
        TablesMap.put(FINU_TP_LIABTY_DIF, "erp.finu_tp_liabty_dif");
        TablesMap.put(FINU_TP_EXPEN_OP, "erp.finu_tp_expen_op");
        TablesMap.put(FINU_TP_ADM_CPT, "erp.finu_tp_adm_cpt");
        TablesMap.put(FINU_TP_TAX_CPT, "erp.finu_tp_tax_cpt");
        TablesMap.put(FINU_TP_LAY_BANK, "erp.finu_tp_lay_bank");
        TablesMap.put(FINU_COST_GIC, "erp.finu_cost_gic");

        TablesMap.put(TRNS_CT_DPS, "erp.trns_ct_dps");
        TablesMap.put(TRNS_CL_DPS, "erp.trns_cl_dps");
        TablesMap.put(TRNS_TP_DPS_ADJ, "erp.trns_tp_dps_adj");
        TablesMap.put(TRNS_STP_DPS_ADJ, "erp.trns_stp_dps_adj");
        TablesMap.put(TRNS_CT_IOG, "erp.trns_ct_iog");
        TablesMap.put(TRNS_CL_IOG, "erp.trns_cl_iog");
        TablesMap.put(TRNS_TP_IOG, "erp.trns_tp_iog");
        TablesMap.put(TRNS_TP_PAY, "erp.trns_tp_pay");
        TablesMap.put(TRNS_TP_LINK, "erp.trns_tp_link");
        TablesMap.put(TRNS_TP_DPS_EVT, "erp.trns_tp_dps_evt");
        TablesMap.put(TRNS_TP_DPS_PRT, "erp.trns_tp_dps_prt");
        TablesMap.put(TRNS_TP_DPS_ETY, "erp.trns_tp_dps_ety");
        TablesMap.put(TRNS_ST_DPS, "erp.trns_st_dps");
        TablesMap.put(TRNS_ST_DPS_VAL, "erp.trns_st_dps_val");
        TablesMap.put(TRNS_ST_DPS_AUTHORN, "erp.trns_st_dps_authorn");
        TablesMap.put(TRNS_CT_SIGN, "erp.trns_ct_sign");
        TablesMap.put(TRNS_TP_SIGN, "erp.trns_tp_sign");
        TablesMap.put(TRNS_TP_XML, "erp.trns_tp_xml");
        TablesMap.put(TRNS_TP_XML_DVY, "erp.trns_tp_xml_dvy");
        TablesMap.put(TRNS_ST_XML_DVY, "erp.trns_st_xml_dvy");
        TablesMap.put(TRNS_TP_CFD, "erp.trns_tp_cfd");

        TablesMap.put(TRNU_DPS_NAT, "erp.trnu_dps_nat");
        TablesMap.put(TRNU_TP_DPS, "erp.trnu_tp_dps");
        TablesMap.put(TRNU_TP_DPS_SRC_ITEM, "erp.trnu_tp_dps_src_item");
        TablesMap.put(TRNU_TP_PAY_SYS, "erp.trnu_tp_pay_sys");
        TablesMap.put(TRNU_TP_IOG_ADJ, "erp.trnu_tp_iog_adj");
        TablesMap.put(TRNU_TP_DPS_ANN, "erp.trnu_tp_dps_ann");

        TablesMap.put(MKTS_TP_DISC_APP, "erp.mkts_tp_disc_app");

        TablesMap.put(MFGS_ST_ORD, "erp.mfgs_st_ord");
        TablesMap.put(MFGS_PTY_ORD, "erp.mfgs_pty_ord");
        TablesMap.put(MFGS_TP_REQ, "erp.mfgs_tp_req");
        TablesMap.put(MFGS_TP_COST_OBJ, "erp.mfgs_tp_cost_obj");

        TablesMap.put(MFGU_TP_ORD, "erp.mfgu_tp_ord");
        TablesMap.put(MFGU_TURN, "erp.mfgu_turn");

        TablesMap.put(FIN_YEAR, "fin_year");
        TablesMap.put(FIN_YEAR_PER, "fin_year_per");
        TablesMap.put(FIN_EXC_RATE, "fin_exc_rate");
        TablesMap.put(FINU_BANK_NB_DAY, "erp.finu_bank_nb_day");
        TablesMap.put(FIN_ACC, "fin_acc");
        TablesMap.put(FIN_CC, "fin_cc");
        TablesMap.put(FIN_ACC_CASH, "fin_acc_cash");
        TablesMap.put(FIN_CHECK_WAL, "fin_check_wal");
        TablesMap.put(FIN_CHECK, "fin_check");
        TablesMap.put(FIN_TAX_GRP, "fin_tax_grp");
        TablesMap.put(FIN_TAX_GRP_ETY, "fin_tax_grp_ety");
        TablesMap.put(FIN_TAX_GRP_IGEN, "fin_tax_grp_igen");
        TablesMap.put(FIN_TAX_GRP_ITEM, "fin_tax_grp_item");

        TablesMap.put(FIN_BKK_NUM, "fin_bkk_num");
        TablesMap.put(FIN_REC, "fin_rec");
        TablesMap.put(FIN_REC_ETY, "fin_rec_ety");
        TablesMap.put(FIN_ACC_BP, "fin_acc_bp");
        TablesMap.put(FIN_ACC_BP_ETY, "fin_acc_bp_ety");
        TablesMap.put(FIN_ACC_BP_TP_BP, "fin_acc_bp_tp_bp");
        TablesMap.put(FIN_ACC_BP_BP, "fin_acc_bp_bp");
        TablesMap.put(FIN_ACC_ITEM, "fin_acc_item");
        TablesMap.put(FIN_ACC_ITEM_ETY, "fin_acc_item_ety");
        TablesMap.put(FIN_ACC_ITEM_ITEM, "fin_acc_item_item");
        TablesMap.put(FIN_ACC_TAX, "fin_acc_tax");
        TablesMap.put(FIN_ACC_COB_ENT, "fin_acc_cob_ent");
        TablesMap.put(FIN_CC_ITEM, "fin_cc_item");
        TablesMap.put(FIN_COB_BKC, "fin_cob_bkc");
        TablesMap.put(FIN_LAY_BANK, "fin_lay_bank");
        TablesMap.put(FINU_BANK_NB_DAY, "erp.finu_bank_nb_day");
        TablesMap.put(FIN_BKC, "fin_bkc");

        TablesMap.put(TRN_DNS_DPS, "trn_dns_dps");
        TablesMap.put(TRN_DNS_DIOG, "trn_dns_diog");
        TablesMap.put(TRN_DNC_DPS_COB, "trn_dnc_dps_cob");
        TablesMap.put(TRN_DNC_DPS_COB_ENT, "trn_dnc_dps_cob_ent");
        TablesMap.put(TRN_DNC_DIOG_COB, "trn_dnc_diog_cob");
        TablesMap.put(TRN_DNC_DIOG_COB_ENT, "trn_dnc_diog_cob_ent");
        TablesMap.put(TRN_BP_BLOCK, "trn_bp_block");
        TablesMap.put(TRN_SUP_LT_CO, "trn_sup_lt_co");
        TablesMap.put(TRN_SUP_LT_COB, "trn_sup_lt_cob");
        TablesMap.put(TRN_SYS_NTS, "trn_sys_nts");
        TablesMap.put(TRN_DPS, "trn_dps");
        TablesMap.put(TRN_DPS_SND_LOG, "trn_dps_snd_log");
        TablesMap.put(TRN_DPS_ADD, "trn_dps_add");
        TablesMap.put(TRN_DPS_ADD_ETY, "trn_dps_add_ety");
        TablesMap.put(TRN_DPS_EVT, "trn_dps_evt");
        TablesMap.put(TRN_DPS_NTS, "trn_dps_nts");
        TablesMap.put(TRN_DPS_ETY, "trn_dps_ety");
        TablesMap.put(TRN_DPS_ETY_NTS, "trn_dps_ety_nts");
        TablesMap.put(TRN_DPS_ETY_PRC, "trn_dps_ety_prc");
        TablesMap.put(TRN_DPS_ETY_TAX, "trn_dps_ety_tax");
        TablesMap.put(TRN_DPS_ETY_COMMS, "trn_dps_ety_comms");
        TablesMap.put(TRN_DPS_ETY_HIST, "trn_dps_ety_hist");
        TablesMap.put(TRN_DPS_ETY_ANALYSIS, "trn_dps_ety_analysis");
        TablesMap.put(TRN_DPS_RISS, "trn_dps_riss");
        TablesMap.put(TRN_DPS_REPL, "trn_dps_repl");
        TablesMap.put(TRN_DPS_DPS_SUPPLY, "trn_dps_dps_supply");
        TablesMap.put(TRN_DPS_DPS_ADJ, "trn_dps_dps_adj");
        TablesMap.put(TRN_DPS_IOG_CHG, "trn_dps_iog_chg");
        TablesMap.put(TRN_DPS_IOG_WAR, "trn_dps_iog_war");
        TablesMap.put(TRN_DPS_REC, "trn_dps_rec");
        TablesMap.put(TRN_DIOG, "trn_diog");
        TablesMap.put(TRN_DIOG_NTS, "trn_diog_nts");
        TablesMap.put(TRN_DIOG_ETY, "trn_diog_ety");
        TablesMap.put(TRN_DIOG_REC, "trn_diog_rec");
        TablesMap.put(TRN_CTR, "trn_ctr");
        TablesMap.put(TRN_CTR_ETY, "trn_ctr_ety");
        TablesMap.put(TRN_DSM, "trn_dsm");
        TablesMap.put(TRN_DSM_NTS, "trn_dsm_nts");
        TablesMap.put(TRN_DSM_ETY, "trn_dsm_ety");
        TablesMap.put(TRN_DSM_ETY_NTS, "trn_dsm_ety_nts");
        TablesMap.put(TRN_DSM_REC, "trn_dsm_rec");
        TablesMap.put(TRN_USR_CFG, "trn_usr_cfg");
        TablesMap.put(TRN_USR_CFG_IFAM, "trn_usr_cfg_ifam");
        TablesMap.put(TRN_USR_CFG_BA, "trn_usr_cfg_ba");
        TablesMap.put(TRN_USR_DPS_DNS, "trn_usr_dns_dps");
        TablesMap.put(TRN_LOT, "trn_lot");
        TablesMap.put(TRN_STK_CFG, "trn_stk_cfg");
        TablesMap.put(TRN_STK_CFG_ITEM, "trn_stk_cfg_item");
        TablesMap.put(TRN_STK_CFG_DNS, "trn_stk_cfg_dns");
        TablesMap.put(TRN_STK, "trn_stk");
        TablesMap.put(TRN_STK_SEG, "trn_stk_seg");
        TablesMap.put(TRN_STK_SEG_WHS, "trn_stk_seg_whs");
        TablesMap.put(TRN_STK_SEG_WHS_ETY, "trn_stk_seg_whs_ety");
        TablesMap.put(TRN_PDF, "trn_pdf");
        TablesMap.put(TRN_CFD, "trn_cfd");
        TablesMap.put(TRN_CFD_SIGN_LOG, "trn_cfd_sign_log");
        TablesMap.put(TRN_CFD_SND_LOG, "trn_cfd_snd_log");
        TablesMap.put(TRN_CFD_FIN_REC, "trn_cfd_fin_rec");
        TablesMap.put(TRN_PAY, "trn_pay");
        TablesMap.put(TRN_PAY_PAY, "trn_pay_pay");
        TablesMap.put(TRN_PAY_PAY_TAX, "trn_pay_pay_tax");
        TablesMap.put(TRN_PAY_PAY_DOC, "trn_pay_pay_doc");
        TablesMap.put(TRN_PAY_PAY_DOC_TAX, "trn_pay_pay_doc_tax");
        TablesMap.put(TRN_PAC, "trn_pac");
        TablesMap.put(TRN_TP_CFD_PAC, "trn_tp_cfd_pac");
        TablesMap.put(TRN_SIGN, "trn_sign");
        TablesMap.put(TRN_MMS_LOG, "trn_mms_log");
        TablesMap.put(TRN_DVY, "trn_dvy");
        TablesMap.put(TRN_DVY_ETY, "trn_dvy_ety");
        TablesMap.put(TRN_DNC_DPS, "trn_dnc_dps");
        TablesMap.put(TRN_DNC_DPS_DNS, "trn_dnc_dps_dns");
        TablesMap.put(TRN_DNC_DIOG, "trn_dnc_diog");
        TablesMap.put(TRN_DNC_DIOG_DNS, "trn_dnc_diog_dns");

        TablesMap.put(MKT_CFG_CUS, "mkt_cfg_cus");
        TablesMap.put(MKT_CFG_CUSB, "mkt_cfg_cusb");
        TablesMap.put(MKT_CFG_SAL_AGT, "mkt_cfg_sal_agt");
        TablesMap.put(MKT_PLIST_GRP, "mkt_plist_grp");
        TablesMap.put(MKT_PLIST, "mkt_plist");
        TablesMap.put(MKT_PLIST_ITEM, "mkt_plist_item");
        TablesMap.put(MKT_PLIST_PRICE, "mkt_plist_price");
        TablesMap.put(MKT_PLIST_CUS, "mkt_plist_cus");
        TablesMap.put(MKT_COMMS_SAL_AGT, "mkt_comms_sal_agt");
        TablesMap.put(MKT_COMMS_SAL_AGT_TP, "mkt_comms_sal_agt_tp");
        TablesMap.put(MKT_COMMS_LOG, "mkt_comms_log");
        TablesMap.put(MKT_COMMS, "mkt_comms");
        TablesMap.put(MKT_COMMS_PAY, "mkt_comms_pay");
        TablesMap.put(MKT_COMMS_PAY_ETY, "mkt_comms_pay_ety");

        TablesMap.put(MKTU_TP_CUS, "mktu_tp_cus");
        TablesMap.put(MKTU_TP_SAL_AGT, "mktu_tp_sal_agt");
        TablesMap.put(MKTU_MKT_SEGM, "mktu_mkt_segm");
        TablesMap.put(MKTU_MKT_SEGM_SUB, "mktu_mkt_segm_sub");
        TablesMap.put(MKTU_DIST_CHAN, "mktu_dist_chan");
        TablesMap.put(MKTU_SAL_ROUTE, "mktu_sal_route");

        TablesMap.put(MFG_BOM, "mfg_bom");
        TablesMap.put(MFG_BOM_NTS, "mfg_bom_nts");
        TablesMap.put(MFG_SGDS, "mfg_sgds");
        TablesMap.put(MFG_BOM_SUB, "mfg_bom_sub");
        TablesMap.put(MFG_LINE, "mfg_line");

        TablesMap.put(MFGU_GANG, "mfgu_gang");
        TablesMap.put(MFGU_GANG_ETY, "mfgu_gang_ety");
        TablesMap.put(MFGU_LINE, "mfgu_line");
        TablesMap.put(MFGU_LINE_CFG_ITEM, "mfgu_line_cfg_item");

        TablesMap.put(MFG_ORD, "mfg_ord");
        TablesMap.put(MFG_ORD_PER, "mfg_ord_per");
        TablesMap.put(MFG_ORD_NTS, "mfg_ord_nts");
        TablesMap.put(MFG_ORD_CHG, "mfg_ord_chg");
        TablesMap.put(MFG_ORD_CHG_ETY, "mfg_ord_chg_ety");
        TablesMap.put(MFG_ORD_CHG_ETY_LOT, "mfg_ord_chg_ety_lot");
        TablesMap.put(MFG_ORD_SGDS, "mfg_ord_sgds");
        TablesMap.put(MFG_EXP, "mfg_exp");
        TablesMap.put(MFG_EXP_ORD, "mfg_exp_ord");
        TablesMap.put(MFG_EXP_ETY_ITEM, "mfg_exp_ety_item");
        TablesMap.put(MFG_EXP_ETY, "mfg_exp_ety");
        TablesMap.put(MFG_REQ, "mfg_req");
        TablesMap.put(MFG_EXP_REQ, "mfg_exp_req");
        TablesMap.put(MFG_REQ_ETY, "mfg_req_ety");
        TablesMap.put(MFG_REQ_PUR, "mfg_req_pur");

        TablesMap.put(MFG_COST, "mfg_cost");

        TablesMap.put(HRS_SIE_PAY, "hrs_sie_pay");
        TablesMap.put(HRS_SIE_PAY_EMP, "hrs_sie_pay_emp");
        TablesMap.put(HRS_SIE_PAY_MOV, "hrs_sie_pay_mov");
    }
}
