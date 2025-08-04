/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores, Uriel Castañeda, Isabel Servín, Edwin Carmona, Sergio Flores, Claudio Peña
 */
public abstract class SModConsts {

    /*
     * GUI
     */

    public static final int VIEW_ST_REG = 1;        // registry status
    public static final int VIEW_ST_DONE = 11;      // action done
    public static final int VIEW_ST_PEND = 12;      // action pending
    public static final int VIEW_SC_SUM = 21;       // summary
    public static final int VIEW_SC_DET = 22;       // detail
    public static final int OPC_BAL_ALL = 1;        // balance: all registries
    public static final int OPC_BAL_BAL = 2;        // balance: registries with balance

    public static final int MOD_CFG = 101000;
    public static final int MOD_FIN = 102000;
    public static final int MOD_PUR = 103000;
    public static final int MOD_SAL = 104000;
    public static final int MOD_INV = 105000;
    public static final int MOD_MKT = 106000;
    public static final int MOD_LOG = 107000;
    public static final int MOD_MFG = 108000;
    public static final int MOD_HRS = 109000;
    public static final int MOD_QLT = 109999;   // it should be 110000, but now is not possible due to constants for new-framework modules

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

    public static final int MOD_CFG_N = 110000;
    public static final int MOD_USR_N = 120000;
    public static final int MOD_LOC_N = 130000;
    public static final int MOD_BPS_N = 140000;
    public static final int MOD_ITM_N = 150000;
    public static final int MOD_FIN_N = 210000;
    public static final int MOD_TRN_N = 220000;
    public static final int MOD_TRN_PUR_N = 220001;
    public static final int MOD_TRN_SAL_N = 220002;
    public static final int MOD_TRN_INV_N = 220003;
    public static final int MOD_MKT_N = 230000;
    public static final int MOD_LOG_N = 240000;
    public static final int MOD_MFG_N = 250000;
    public static final int MOD_HRS_N = 260000;
    public static final int MOD_QLT_N = 270000;

    /*
     * Configuration
     */

    public static final int CFGS_CT_ENT = 1011001;
    public static final int CFGS_TP_ENT = 1011002;
    public static final int CFGS_TP_SORT = 1011003;
    public static final int CFGS_TP_BAL = 1011004;
    public static final int CFGS_TP_REL = 1011005;
    public static final int CFGS_TP_FMT_D = 1011006;
    public static final int CFGS_TP_FMT_DT = 1011007;
    public static final int CFGS_TP_FMT_T = 1011008;
    public static final int CFGS_TP_DBMS = 1011009;
    public static final int CFGS_TP_MMS = 1011010;
    public static final int CFGS_TP_MOD = 1011011;
    public static final int CFGS_TP_AUTHORN = 1011021;
    public static final int CFGS_ST_AUTHORN = 1011022;
    public static final int CFGS_TP_DOC = 1011501;

    public static final int CFGU_CUR = 1012001;
    public static final int CFGU_CO = 1012002;
    public static final int CFGU_COB_ENT = 1012003;
    public static final int CFGU_LAN = 1012004;
    public static final int CFGU_SCA = 1012091;
    public static final int CFG_PARAM_ERP = 1012005;
    public static final int CFG_SYNC_LOG = 1012051;
    public static final int CFG_SYNC_LOG_ETY = 1012052;
    
    public static final int CFGU_CERT = 1013001;
    public static final int CFGU_FUNC = 1013011;
    public static final int CFGU_FUNC_SUB = 1013012;
    public static final int CFGU_SHIFT = 1013021;
    public static final int CFGU_AUTHORN_NODE = 1013033;
    public static final int CFGU_AUTHORN_NODE_USR = 1013034;
    public static final int CFGU_AUTHORN_NODE_POS = 1013035;
    public static final int CFGU_AUTHORN_PATH = 1013031;
    public static final int CFGU_AUTHORN_STEP = 1013032;
    public static final int CFGU_DOC = 1013500;

    public static final int CFG_MMS = 1013501;
    public static final int CFG_PARAM_CO = 1013502;
    public static final int CFG_PARAM = 1013521;
    public static final int CFG_CFD = 1013511;
    public static final int CFG_CUSTOM_REP = 1013531;
    public static final int CFG_COM_SYNC_LOG = 1013551;
    public static final int CFG_COM_SYNC_LOG_ETY = 1013552;
    
    public static final int CFGX_AUTHORN_COMMENTS = 1014001;
    
    /*
     * Users
     */

    public static final int USRS_TP_USR = 1021001;
    public static final int USRS_TP_LEV = 1021002;
    public static final int USRS_TP_PRV = 1021003;
    public static final int USRS_TP_ROL = 1021004;
    public static final int USRS_PRV = 1021005;
    public static final int USRS_ROL = 1021006;
    public static final int USRS_ROL_PRV = 1021007;
    public static final int USRS_LINK = 1021008;

    public static final int USRU_ACCESS_CO = 1022001;
    public static final int USRU_ACCESS_COB = 1022002;
    public static final int USRU_ACCESS_COB_ENT = 1022003;
    public static final int USRU_ACCESS_COB_ENT_UNIV = 1022004;
    public static final int USRU_PRV_USR = 1022005;
    public static final int USRU_PRV_CO = 1022006;
    public static final int USRU_ROL_USR = 1022007;
    public static final int USRU_ROL_CO = 1022008;
    public static final int USRU_USR = 1022009;
    public static final int USRU_USR_GRP = 1022010;
    public static final int USRU_USR_GRP_USR = 1022011;

    public static final int USR_USR_GUI = 1023001;
    public static final int USR_USR_FUNC = 1023011;
    public static final int USR_USR_FUNC_SUB = 1023012;

    /*
     * Localities
     */

    public static final int LOCU_CTY = 1032001;
    public static final int LOCU_STA = 1032002;
    public static final int LOCS_BOL_COUNTY = 1031011;
    public static final int LOCS_BOL_LOCALITY = 1031012;
    public static final int LOCS_BOL_ZIP_CODE = 1031013;
    public static final int LOCS_BOL_NEI_ZIP_CODE = 1031014;

    /*
     * Business partners
     */

    public static final int BPSS_CT_BP = 1041001;
    public static final int BPSS_TP_BP_IDY = 1041002;
    public static final int BPSS_TP_BP_ATT = 1041003;
    public static final int BPSS_TP_BPB = 1041004;
    public static final int BPSS_TP_ADD = 1041005;
    public static final int BPSS_TP_ADD_FMT = 1041006;
    public static final int BPSS_TP_CON = 1041007;
    public static final int BPSS_TP_TEL = 1041008;
    public static final int BPSS_TP_CRED = 1041009;
    public static final int BPSS_TP_CFD_ADD = 1041010;
    public static final int BPSS_RISK = 1041011;
    public static final int BPSS_LINK = 1041012;

    public static final int BPSU_BP = 1042001;
    public static final int BPSU_BP_CT = 1042002;
    public static final int BPSU_BP_ATT = 1042003;
    public static final int BPSU_BP_BA = 1042004;
    public static final int BPSU_BP_NTS = 1042005;
    public static final int BPSU_BP_UPD_LOG = 1042100;
    public static final int BPSU_BPB = 1042006;
    public static final int BPSU_BPB_NTS = 1042007;
    public static final int BPSU_BPB_ADD = 1042008;
    public static final int BPSU_BPB_ADD_NTS = 1042009;
    public static final int BPSU_BPB_CON = 1042010;
    public static final int BPSU_BANK_ACC = 1042011;
    public static final int BPSU_BANK_ACC_LAY_BANK = 1042012;
    public static final int BPSU_BANK_ACC_CARD = 1042013;
    public static final int BPSU_BANK_ACC_NTS = 1042014;
    public static final int BPSU_TP_BP = 1042015;
    public static final int BPSU_BA = 1042016;
    public static final int BPSU_BP_ADDEE = 1042031;
    public static final int BPSU_SCA_BP = 1042091;
    public static final int BPSU_SCA_BP_MAP = 1042092;

    public static final int BPSX_BP_ATT_BANK = 1044001;
    public static final int BPSX_BP_ATT_CARR = 1044002;
    public static final int BPSX_BP_X_SAL_AGT = 1044003;

    /*
     * Items
     */

    public static final int ITMS_CT_ITEM = 1051001;
    public static final int ITMS_CL_ITEM = 1051002;
    public static final int ITMS_TP_ITEM = 1051003;
    public static final int ITMS_TP_SNR = 1051004;
    public static final int ITMS_LINK = 1051005;
    public static final int ITMS_ST_ITEM = 1051011;
    public static final int ITMS_CFD_PROD_SERV = 1051201;
    public static final int ITMS_CFD_UNIT = 1051202;

    public static final int ITMU_IFAM = 1052001;
    public static final int ITMU_IGRP = 1052002;
    public static final int ITMU_IGEN = 1052003;
    public static final int ITMU_IGEN_BA = 1052004;
    public static final int ITMU_LINE = 1052005;
    public static final int ITMU_ITEM = 1052006;
    public static final int ITMU_ITEM_BARC = 1052007;
    public static final int ITMU_CFG_ITEM_LAN = 1052008;
    public static final int ITMU_CFG_ITEM_BP = 1052009;
    public static final int ITMU_TP_MAT_MAT_ATT = 1052010;
    public static final int ITMU_ITEM_MAT_ATT = 1052011;
    public static final int ITMU_MATCH_ITEM_CPT_BP = 1052021;
    public static final int ITMU_SCA_ITEM = 1052091;
    public static final int ITMU_SCA_ITEM_MAP = 1052092;

    public static final int ITMU_TP_LEV = 1052501;
    public static final int ITMU_TP_UNIT = 1052502;
    public static final int ITMU_UNIT = 1052503;
    public static final int ITMU_UNIT_EQUIV = 1052514;
    public static final int ITMU_TP_VAR = 1052504;
    public static final int ITMU_VAR = 1052505;
    public static final int ITMU_TP_BRD = 1052506;
    public static final int ITMU_BRD = 1052507;
    public static final int ITMU_TP_MFR = 1052508;
    public static final int ITMU_MFR = 1052509;
    public static final int ITMU_TP_EMT = 1052510;
    public static final int ITMU_EMT = 1052511;
    public static final int ITMU_TP_MAT = 1052512;
    public static final int ITMU_MAT_ATT = 1052513;

    public static final int ITMU_MATCH_ITEM_CPT_BP_COMP = 1052121;
    public static final int ITMU_PRICE_COMM_LOG = 1052122;
    public static final int ITMU_ITEM_COMP = 1052141;
    
    public static final int ITMX_IGEN_INV = 1053001;

    /*
     * Finance
     */

    public static final int FINS_TP_BKR = 2011001;
    public static final int FINS_TP_ACC_MOV = 2011002;
    public static final int FINS_CL_ACC_MOV = 2011003;
    public static final int FINS_CLS_ACC_MOV = 2011004;
    public static final int FINS_TP_ACC = 2011005;
    public static final int FINS_CL_ACC = 2011006;
    public static final int FINS_CLS_ACC = 2011007;
    public static final int FINS_TP_ACC_SPE = 2011008;
    public static final int FINS_CL_SYS_ACC = 2011009;
    public static final int FINS_TP_SYS_ACC = 2011010;
    public static final int FINS_CL_SYS_MOV = 2011011;
    public static final int FINS_TP_SYS_MOV = 2011012;
    public static final int FINS_TP_ACC_SYS = 2011013;
    public static final int FINS_CT_SYS_MOV_XXX = 2011014;
    public static final int FINS_TP_SYS_MOV_XXX = 2011015;
    public static final int FINS_CT_ACC_CASH = 2011016;
    public static final int FINS_TP_ACC_CASH = 2011017;
    public static final int FINS_TP_TAX = 2011018;
    public static final int FINS_TP_TAX_CAL = 2011019;
    public static final int FINS_TP_TAX_APP = 2011020;
    public static final int FINS_TP_ACC_BP = 2011021;
    public static final int FINS_TP_ACC_ITEM = 2011022;
    public static final int FINS_TP_CARD = 2011023;
    public static final int FINS_TP_PAY_BANK = 2011024;
    public static final int FINS_ST_FIN_MOV = 2011025;
    public static final int FINS_FISCAL_ACC = 2011101;
    public static final int FINS_FISCAL_CUR = 2011106;
    public static final int FINS_FISCAL_BANK = 2011107;
    public static final int FINS_FISCAL_PAY_MET = 2011111;
    public static final int FINS_TP_FISCAL_ACC_LINK = 2011121;
    public static final int FINS_CFD_TAX = 2011201;

    public static final int FINU_TAX_REG = 2012001;
    public static final int FINU_TAX_IDY = 2012002;
    public static final int FINU_TAX_BAS = 2012003;
    public static final int FINU_TAX = 2012004;
    public static final int FINU_CARD_ISS = 2012005;
    public static final int FINU_CHECK_FMT = 2012006;
    public static final int FINU_CHECK_FMT_GP = 2012007;

    public static final int FINU_TP_REC = 2012501;
    public static final int FINU_TP_ACC_USR = 2012502;
    public static final int FINU_CL_ACC_USR = 2012503;
    public static final int FINU_CLS_ACC_USR = 2012504;
    public static final int FINU_TP_ACC_LEDGER = 2012505;
    public static final int FINU_TP_ACC_EBITDA = 2012506;
    public static final int FINU_TP_ASSET_FIX = 2012507;
    public static final int FINU_TP_ASSET_DIF = 2012508;
    public static final int FINU_TP_LIABTY_DIF = 2012509;
    public static final int FINU_TP_EXPEN_OP = 2012510;
    public static final int FINU_TP_ADM_CPT = 2012511;
    public static final int FINU_TP_TAX_CPT = 2012512;
    public static final int FINU_TP_LAY_BANK = 2012513;
    public static final int FINU_COST_GIC = 2012514;

    public static final int FIN_YEAR = 2013001;
    public static final int FIN_YEAR_PER = 2013002;
    public static final int FIN_EXC_RATE = 2013003;
    public static final int FIN_ACC = 2013004;
    public static final int FIN_ACC_ITEM_LINK = 2013005;
    public static final int FIN_CC = 2013006;
    public static final int FIN_ACC_CASH = 2013007;
    public static final int FIN_CHECK_WAL = 2013008;
    public static final int FIN_CHECK = 2013009;
    public static final int FIN_TAX_GRP = 2013010;
    public static final int FIN_TAX_GRP_ETY = 2013011;
    public static final int FIN_TAX_GRP_IGEN = 2013012;
    public static final int FIN_TAX_GRP_ITEM = 2013013;
    public static final int FIN_TAX_ITEM_LINK = 2013014;
    public static final int FIN_BKK_NUM = 2013015;
    public static final int FIN_REC = 2013016;
    public static final int FIN_REC_ETY = 2013017;
    public static final int FIN_ACC_BP = 2013018;
    public static final int FIN_ACC_BP_ETY = 2013019;
    public static final int FIN_ACC_BP_TP_BP = 2013020;
    public static final int FIN_ACC_BP_BP = 2013021;
    public static final int FIN_ACC_ITEM = 2013022;
    public static final int FIN_ACC_ITEM_ETY = 2013023;
    public static final int FIN_ACC_ITEM_ITEM = 2013024;
    public static final int FIN_ACC_TAX = 2013025;
    public static final int FIN_ACC_COB_ENT = 2013026;
    public static final int FIN_CC_ITEM = 2013027;
    public static final int FIN_COB_BKC = 2013028;
    public static final int FIN_ABP_ENT = 2013029;
    public static final int FIN_ABP_ENT_LINK = 2013030;
    public static final int FIN_ABP_BP = 2013031;
    public static final int FIN_ABP_BP_LINK = 2013032;
    public static final int FIN_ABP_ITEM = 2013033;
    public static final int FIN_ABP_ITEM_LINK = 2013034;
    public static final int FIN_ABP_TAX = 2013035;
    public static final int FIN_FISCAL_ACC_LINK = 2013451;
    public static final int FIN_FISCAL_ACC_LINK_DET = 2013452;
    public static final int FIN_LAY_BANK = 2013461;
    public static final int FIN_LAY_BANK_DEP = 2013462;
    public static final int FIN_LAY_BANK_DEP_ANA = 2013463;
    public static final int FIN_REP_CUS_ACC = 2013491;
    public static final int FIN_REP_CUS_ACC_ACC = 2013492;
    public static final int FIN_REP_CUS_ACC_CC = 2013493;
    public static final int FIN_REP_CUS_ACC_USR = 2013494;


    public static final int FIN_BKC = 2013501;

    public static final int FINX_REC_CASH = 2014001;
    public static final int FINX_ACC_CASH_BANK = 2014002;
    public static final int FINX_REP_CUS_ACC = 2014003;
    public static final int FINX_CUST_REPS_EXPS = 2014100; // custom reports expenses

    public static final int FINR_CSH_FLW_EXP = 2015001;
    public static final int FINR_MAS_PRI = 2015002;
    public static final int FINR_DPS_TAX_PEND = 2015011;
    public static final int FINR_LAY_BANK = 2015021;
    
    public static final int TRNX_DPS_PAYS = 2015022;

    /*
     * Transactions
     */

    public static final int TRNS_CT_DPS = 2021001;
    public static final int TRNS_CL_DPS = 2021002;
    public static final int TRNS_TP_DPS_ADJ = 2021003;
    public static final int TRNS_STP_DPS_ADJ = 2021004;
    public static final int TRNS_CT_IOG = 2021005;
    public static final int TRNS_CL_IOG = 2021006;
    public static final int TRNS_TP_IOG = 2021007;
    public static final int TRNS_TP_PAY = 2021008;
    public static final int TRNS_TP_PAY_WAY = 2021009;
    public static final int TRNS_TP_LINK = 2021010;
    public static final int TRNS_TP_DPS_EVT = 2021011;
    public static final int TRNS_TP_DPS_PRT = 2021012;
    public static final int TRNS_TP_DPS_ETY = 2021013;
    public static final int TRNS_ST_DPS = 2021014;
    public static final int TRNS_ST_DPS_VAL = 2021015;
    public static final int TRNS_ST_DPS_AUTHORN = 2021016;
    public static final int TRNS_CT_SIGN = 2021017;
    public static final int TRNS_TP_SIGN = 2021018;
    public static final int TRNS_TP_XML = 2021019;
    public static final int TRNS_TP_XML_DVY = 2021020;
    public static final int TRNS_ST_XML_DVY = 2021021;
    public static final int TRNS_TP_CFD = 2021022;
    public static final int TRNS_TP_STK_SEG = 2021031;
    public static final int TRNS_TP_STK_SEG_MOV = 2021032;
    public static final int TRNS_CFD_CAT = 2021201;
    public static final int TRNS_TP_MAINT_MOV = 2021301;
    public static final int TRNS_ST_MAT_REQ = 2021302;
    public static final int TRNS_ST_MAT_PROV = 2021303;
    public static final int TRNS_ST_MAT_PUR = 2021304;
    public static final int TRNS_TP_PERIOD = 2021361;

    public static final int TRNU_DPS_NAT = 2022001;
    public static final int TRNU_TP_DPS = 2022002;
    public static final int TRNU_TP_DPS_SRC_ITEM = 2022006;
    public static final int TRNU_TP_PAY_SYS = 2022003;
    public static final int TRNU_TP_IOG_ADJ = 2022004;
    public static final int TRNU_TP_DPS_ANN = 2022005;
    public static final int TRNU_MAT_REQ_PTY = 2022007;
    public static final int TRNU_SCA_TIC = 2022091;
    
    public static final int TRN_DNS_DPS = 2023001;
    public static final int TRN_DNS_DIOG = 2023002;
    public static final int TRN_DNC_DPS_COB = 2023003;
    public static final int TRN_DNC_DPS_COB_ENT = 2023004;
    public static final int TRN_DNC_DIOG_COB = 2023005;
    public static final int TRN_DNC_DIOG_COB_ENT = 2023006;
    public static final int TRN_BP_BLOCK = 2023007;
    public static final int TRN_SUP_LT_CO = 2023008;
    public static final int TRN_SUP_LT_COB = 2023009;
    public static final int TRN_SYS_NTS = 2023010;
    public static final int TRN_DPS = 2023011;
    public static final int TRN_DPS_SND_LOG = 2023012;
    public static final int TRN_DPS_CFD = 2023201;
    public static final int TRN_DPS_CFD_ETY = 2023202;
    public static final int TRN_DPS_ADD = 2023013;
    public static final int TRN_DPS_ADD_ETY = 2023014;
    public static final int TRN_DPS_EVT = 2023016;
    public static final int TRN_DPS_NTS = 2023017;
    public static final int TRN_DPS_ACK = 2023061;
    public static final int TRN_DPS_ETY = 2023018;
    public static final int TRN_DPS_ETY_NTS = 2023019;
    public static final int TRN_DPS_ETY_PRC = 2023015;
    public static final int TRN_DPS_ETY_TAX = 2023020;
    public static final int TRN_DPS_ETY_COMMS = 2023021;
    public static final int TRN_DPS_ETY_HIST = 2023066;
    public static final int TRN_DPS_ETY_ITEM_COMP = 2023072;
    public static final int TRN_DPS_ETY_QTY_CHG = 2023073;
    public static final int TRN_DPS_ETY_IOG_ETY_XFR = 2023074;
    public static final int TRN_DPS_ETY_ANALYSIS = 2023065;
    public static final int TRN_DPS_RISS = 2023022;
    public static final int TRN_DPS_REPL = 2023023;
    public static final int TRN_DPS_DPS_SUPPLY = 2023024;
    public static final int TRN_DPS_DPS_ADJ = 2023025;
    public static final int TRN_DPS_MAT_REQ = 2023067;
    public static final int TRN_DPS_IOG_CHG = 2023026;
    public static final int TRN_DPS_IOG_WAR = 2023027;
    public static final int TRN_DPS_REC = 2023028;
    public static final int TRN_SUP_FILE = 2023506;
    public static final int TRN_SUP_FILE_DPS = 2023507;
    public static final int TRN_SUP_FILE_DPS_ETY = 2023508;
    public static final int TRN_SCA_TIC_DPS = 2023390;
    public static final int TRN_SCA_TIC_DPS_ETY = 2023391;
    public static final int TRN_INIT = 2023361;
    public static final int TRN_INIT_DPS = 2023366;
    public static final int TRN_DIOG = 2023029;
    public static final int TRN_DIOG_NTS = 2023030;
    public static final int TRN_DIOG_ETY = 2023031;
    public static final int TRN_DIOG_REC = 2023032;
    public static final int TRN_CTR = 2023033;
    public static final int TRN_CTR_ETY = 2023034;
    public static final int TRN_DSM = 2023035;
    public static final int TRN_DSM_NTS = 2023036;
    public static final int TRN_DSM_ETY = 2023037;
    public static final int TRN_DSM_ETY_NTS = 2023038;
    public static final int TRN_DSM_REC = 2023039;
    public static final int TRN_USR_CFG = 2023040;
    public static final int TRN_USR_CFG_IFAM = 2023041;
    public static final int TRN_USR_CFG_BA = 2023042;
    public static final int TRN_USR_DPS_DNS = 2023071;
    public static final int TRN_LOT = 2023043;
    public static final int TRN_STK_CFG = 2023044;
    public static final int TRN_STK_CFG_ITEM = 2023045;
    public static final int TRN_STK_CFG_DNS = 2023046;
    public static final int TRN_STK = 2023047;
    public static final int TRN_STK_SEG = 2023061;
    public static final int TRN_STK_SEG_WHS = 2023062;
    public static final int TRN_STK_SEG_WHS_ETY = 2023063;
    public static final int TRN_STK_SEG_X = 2023065;
    public static final int TRN_STK_VAL = 2023086;
    public static final int TRN_STK_VAL_MVT = 2023087;
    public static final int TRN_STK_VAL_ACC = 2023088;
    public static final int TRN_STK_VAL_DIOG_ADJ = 2023089;
    public static final int TRN_INV_VAL = 2023048;
    public static final int TRN_INV_MFG_CST = 2023049;
    public static final int TRN_ITEM_COST = 2023050;
    public static final int TRN_COST_IDENT_CALC = 2023081;
    public static final int TRN_COST_IDENT_LOT = 2023082;
    public static final int TRN_PDF = 2023059;
    public static final int TRN_CFD = 2023051;
    public static final int TRN_CFD_SIGN_LOG = 2023052;
    public static final int TRN_CFD_SIGN_LOG_MSG = 2023053;
    public static final int TRN_CFD_SND_LOG = 2023054;
    public static final int TRN_CFD_FIN_REC = 2023058;
    public static final int TRN_PAY = 2023401;
    public static final int TRN_PAY_PAY = 2023402;
    public static final int TRN_PAY_PAY_TAX = 2023403;
    public static final int TRN_PAY_PAY_DOC = 2023404;
    public static final int TRN_PAY_PAY_DOC_TAX = 2023405;
    public static final int TRN_PAC = 2023055;
    public static final int TRN_TP_CFD_PAC = 2023056;
    public static final int TRN_SIGN = 2023057;
    public static final int TRN_MMS_LOG = 2023101;
    public static final int TRN_MMS_CFG = 2023102;
    public static final int TRN_DVY = 2023121;
    public static final int TRN_DVY_ETY = 2023122;
    public static final int TRN_MAINT_CFG = 2023300;
    public static final int TRN_MAINT_AREA = 2023301;
    public static final int TRN_MAINT_USER = 2023311;
    public static final int TRN_MAINT_USER_SUPV = 2023316;
    public static final int TRN_MAINT_DIOG_SIG = 2023321;
    public static final int TRN_FUNC_BUDGET = 2023331;
    public static final int TRN_MAT_CC_GRP = 2023332;
    public static final int TRN_MAT_CC_GRP_ITEM = 2023333;
    public static final int TRN_MAT_CC_GRP_USR = 2023334;
    public static final int TRN_MAT_CONS_ENT = 2023335;
    public static final int TRN_MAT_CONS_ENT_BUDGET = 2023350;
    public static final int TRN_MAT_CONS_ENT_USR = 2023336;
    public static final int TRN_MAT_CONS_ENT_WHS = 2023445;
    public static final int TRN_MAT_CONS_SUBENT = 2023337;
    public static final int TRN_MAT_CONS_SUBENT_USR = 2023338;
    public static final int TRN_MAT_CONS_SUBENT_CC = 2023339;
    public static final int TRN_MAT_CONS_SUBENT_CC_CC_GRP = 2023340;
    public static final int TRN_MAT_PROV_ENT = 2023341;
    public static final int TRN_MAT_PROV_ENT_USR = 2023342;
    public static final int TRN_MAT_PROV_ENT_WHS = 2023343;
    public static final int TRN_MAT_REQ = 2023344;
    public static final int TRN_MAT_REQ_CC = 2023351;
    public static final int TRN_MAT_REQ_NTS = 2023345;
    public static final int TRN_MAT_REQ_ETY = 2023346;
    public static final int TRN_MAT_REQ_ETY_NTS = 2023347;
    public static final int TRN_MAT_REQ_ETY_ITEM_CHG = 2023348;
    public static final int TRN_MAT_REQ_ST_LOG = 2023349;
    public static final int TRN_MAT_REQ_EXT_STO_LOG = 2023360;
    public static final int TRN_EST_REQ = 2023352;
    public static final int TRN_EST_REQ_ETY = 2023353;
    public static final int TRN_EST_REQ_REC = 2023354;
    public static final int TRN_DPS_CFD_PAY = 2023096;
    public static final int TRN_DPS_CFD_PAY_DONE = 2023097;
    public static final int TRN_CFD_PAY = 2023091;
    public static final int TRN_DPS_AUTHORN = 2023505;
    
    public static final int TRN_DNC_DPS = 2023501;
    public static final int TRN_DNC_DPS_DNS = 2023502;
    public static final int TRN_DNC_DIOG = 2023503;
    public static final int TRN_DNC_DIOG_DNS = 2023504;

    public static final int TRNX_DPS_BAL = 2024001;         // document balance
    public static final int TRNX_DPS_CON_SEND = 2024002;    // document send
    public static final int TRNX_STK_WAH = 2024003;         // stock by warehouse
    public static final int TRNX_STK_COST = 2024049;        // stock cost
    public static final int TRNX_STK_VAL_DET = 2024091;
    public static final int TRNX_STK_VAL_DET_CONS = 2024092;
    public static final int TRNX_INV_VAL_COST_UPD = 2024061;    // inventory valuation vs. cost
    public static final int TRNX_INV_VAL_COST_QRY = 2024066;    // inventory valuation vs. cost
    public static final int TRNX_ORD_LIM_MAX = 2024051;     // operations control
    public static final int TRNX_BP_BAL_CUR = 2024052;      // operations control
    public static final int TRNX_INT_CUS_QRY = 2024053;     // integral query customers
    public static final int TRNX_INT_SUP_QRY = 2024054;     // integral query provider
    public static final int TRNX_ACC_PEND = 2024071;        // accounts pending: receivable accounts & payable accounts
    public static final int TRNX_MAT_REQ_PEND = 2024075;        // material requisitions pending
    public static final int TRNX_MAT_REQ_PEND_SUP = 2024072;    // material requisitions pending supply
    public static final int TRNX_MAT_REQ_PEND_PUR = 2024074;    // material requisitions pending purchase
    public static final int TRNX_MAT_REQ_CLO_PUR = 2024080;     // material requisitions closed purchase
    public static final int TRNX_MAT_REQ_EST = 2024078;         // material requisitions pending purchase
    public static final int TRNX_MAT_REQ_ACC = 2024079;     // material requisitions pending purchase
    public static final int TRNX_MAT_REQ_FOLL_PUR = 2024081;    // material requisitions following
    public static final int TRNX_MAT_REQ_ALL = 2024082;         // material requisitions all
    public static final int TRNX_MAT_REQ_ETY_ROW = 2024073;     // renglones de requisiciones en diálogo de segregaciones
    public static final int TRNX_MAT_REQ_ESTIMATE = 2024076;    // diálogo de cotizaciones
    public static final int TRNX_MAT_REQ_EST_PROVID_ROW = 2024077;   // renglones de cotizaciones
    public static final int TRNX_FUNC_BUDGETS = 2024331;    // massive CRUD of functional area monthly-budgets
    public static final int TRNX_FUNC_EXPENSES = 2024336;   // massive CRUD of functional area monthly-budgets
    public static final int TRNX_CONF_USR_VS_ENT = 2024337;
    public static final int TRNX_DET_USR_VS_ENT = 2024338;
    public static final int TRNX_CONF_EMP_VS_ENT = 2024339;
    public static final int TRNX_DET_EMP_VS_ENT = 2024340;
    public static final int TRNX_CONF_WHS_VS_PRV_ENT = 2024341;
    public static final int TRNX_DET_WHS_VS_PRV_ENT = 2024342;
    public static final int TRNX_CONF_SUBENT_VS_CC = 2024343;
    public static final int TRNX_DET_SUBENT_VS_CC = 2024344;
    public static final int TRNX_CONF_SUBENT_VS_CC_GRP = 2024345;
    public static final int TRNX_DET_SUBENT_VS_CC_GRP = 2024346;
    public static final int TRNX_CONF_CC_GRP_VS_ITM = 2024347;
    public static final int TRNX_DET_CC_GRP_VS_ITM = 2024348;
    public static final int TRNX_CONF_CC_GRP_VS_USR = 2024349;
    public static final int TRNX_DET_CC_GRP_VS_USR = 2024350;
    public static final int TRNX_MAT_REQ_STK_SUP = 2024351;
    public static final int TRNX_CONF_WHS_VS_CON_ENT = 2024352;
    public static final int TRNX_DET_WHS_VS_CON_ENT = 2024353;
    public static final int TRNX_MAT_REQ_ITM_SUP = 2024354;
    public static final int TRNX_MAT_REQ_ITM_SUP_SEL = 2024355;
    public static final int TRNX_MAT_REQ_DOCS_KAR = 2024358;
    public static final int TRNX_MAT_CONS = 2024356;
    public static final int TRNX_MAT_CONS_CC = 2024357;
    public static final int TRNX_MAT_BUDGET_SUM = 2024359;
    public static final int TRNX_WAH_CONS_DET = 2024360;
    public static final int TRNX_MAT_CONS_CC_R = 1;
    public static final int TRNX_INV_VAL_PRC_CALC = 1;  // inventory valuation: process calculation
    public static final int TRNX_INV_VAL_UPD_COST = 2;  // inventory valuation: update costs (from file)
    public static final int TRNX_TP_VAL_MVT = 2024092;
    public static final int TRNX_SUP_FILE_DPS_PROC = 2024093;   // DPS support file process
    public static final int TRNX_DPS_TANK_CAR = 2024094;
    public static final int TRNX_DPS_TANK_CAR_CARDEX = 2024095;
    public static final int TRNX_DPS_ETY_ACI_PER = 2024096;
    public static final int TRNX_DPS_ACC_TAG = 2024097;
    public static final int TRNX_STK_VAL_UPD = 2024098;
    public static final int TRNX_STK_VAL_IN_CARDEX = 2024099;
    
    public static final int TRNR_DPS_CON_BP = 2025001;
    public static final int TRNR_CON_STA = 2025002;
    public static final int TRNR_CON_STA_BP = 2025003;
    public static final int TRNR_CON_MON_DVY_PROG = 2025004;
    public static final int TRNR_DPS_CON = 2025005;
    public static final int TRNR_VTAS_FSC = 2025006;

    /*
     * Marketing
     */

    public static final int MKTS_TP_PLIST_GRP = 2031001;
    public static final int MKTS_TP_LIST = 2031002;
    public static final int MKTS_TP_PRICE_UPD = 2031003;
    public static final int MKTS_TP_DISC_APP = 2031004;
    public static final int MKTS_TP_COMMS = 2031005;
    public static final int MKTS_ORIG_COMMS = 2031006;

    public static final int MKTU_PLIST_SRP = 2032001;
    public static final int MKTU_PLIST_PUB = 2032002;

    public static final int MKT_PLIST_PUB_CO = 2033001;
    public static final int MKT_PLIST_PUB_COB = 2033002;
    public static final int MKT_CFG_CUS = 2033003;
    public static final int MKT_CFG_CUSB = 2033004;
    public static final int MKT_CFG_SAL_AGT = 2033005;
    public static final int MKT_PLIST_GRP = 2033006;
    public static final int MKT_PLIST_GRP_ITEM = 2033007;
    public static final int MKT_PLIST_GRP_CUS = 2033008;
    public static final int MKT_PLIST_GRP_CUS_TP = 2033009;
    public static final int MKT_PLIST_GRP_BP_TP = 2033010;
    public static final int MKT_PLIST = 2033011;
    public static final int MKT_PLIST_ITEM = 2033012;
    public static final int MKT_PLIST_PRICE = 2033013;
    public static final int MKT_PLIST_BP_LINK = 2033014;
    public static final int MKT_PRICE_FIX_CUS = 2033017;
    public static final int MKT_PRICE_FIX_CUS_TP = 2033018;
    public static final int MKT_DISC_ADD_CUS = 2033019;
    public static final int MKT_DISC_ADD_CUS_TP = 2033020;
    public static final int MKT_COMMS_SAL_AGT = 2033021;
    public static final int MKT_COMMS_SAL_AGT_TP = 2033022;
    public static final int MKT_COMMS_LOG = 2033031;
    public static final int MKT_COMMS = 2033032;
    public static final int MKT_COMMS_PAY = 2033033;
    public static final int MKT_COMMS_PAY_ETY = 2033034;

    public static final int MKTU_TP_CUS = 2033501;
    public static final int MKTU_TP_SAL_AGT = 2033502;
    public static final int MKTU_MKT_SEGM = 2033503;
    public static final int MKTU_MKT_SEGM_SUB = 2033504;
    public static final int MKTU_DIST_CHAN = 2033505;
    public static final int MKTU_SAL_ROUTE = 2033506;

    public static final int MKTX_COMMS_PAY = 2034001;
    public static final int MKTX_COMMS_PAID = 2034002;
    public static final int MKTX_COMMS_PAY_ETY = 2034003;
    public static final int MKTX_COMMS_PAID_ETY = 2034004;
    public static final int MKTX_COMMS_CONS = 2034005;
    public static final int MKTX_COMMS_CALC = 2034006;
    public static final int MKTX_COMMS_PAY_REC = 2033035;

    public static final int MKTR_COMMS_PAY = 2035001;

    /*
     * Logistics
     */

    public static final int LOGS_TP_SHIP = 2041001;
    public static final int LOGS_TP_DLY = 2041002;
    public static final int LOGS_TP_SPOT = 2041003;
    public static final int LOGS_TP_MOT = 2041004;
    public static final int LOGS_TP_CAR = 2041005;
    public static final int LOGS_TP_DOC_SHIP = 2041006;
    public static final int LOGS_TP_BOL_PERSON = 2041008;
    public static final int LOGS_INC = 2041007;

    public static final int LOGU_TP_VEH = 2042001;
    public static final int LOGU_BPB_ADD_NEI = 2042005;
    public static final int LOGU_SPOT = 2042002;
    public static final int LOGU_SPOT_COB = 2042003;
    public static final int LOGU_SPOT_COB_ENT = 2042004;

    public static final int LOG_INSURER = 2043011;
    public static final int LOG_VEH = 2043001;
    public static final int LOG_TRAILER = 2043012;
    public static final int LOG_TANK_CAR = 2043091;
    public static final int LOG_BOL_PERSON = 2043013;
    public static final int LOG_DRIVER_VEH = 2043014;
    public static final int LOG_DIST_LOCATION = 2043015;
    public static final int LOG_RATE = 2043002;
    public static final int LOG_SHIP = 2043003;
    public static final int LOG_SHIP_NTS = 2043004;
    public static final int LOG_SHIP_DEST = 2043005;
    public static final int LOG_SHIP_DEST_ETY = 2043006;
    public static final int LOG_BOL = 2043016;
    public static final int LOG_BOL_TRANSP_MODE = 2043017;
    public static final int LOG_BOL_TRANSP_MODE_EXTRA = 2043018;
    public static final int LOG_BOL_LOCATION = 2043019;
    public static final int LOG_BOL_MERCH = 2043020;
    public static final int LOG_BOL_MERCH_QTY = 2043021;

    public static final int LOGX_TP_VEH = 2044001;
    public static final int LOGX_RATE = 2044002;
    public static final int LOGX_SHIP_DPS = 2044003;
    public static final int LOGX_SHIP_DPS_ADJ = 2044004;
    public static final int LOGX_SHIP_DPS_SAL = 2044005;
    public static final int LOGX_SHIP_DIOG = 2044006;
    public static final int LOGX_SHIP_DLY = 2044007;
    public static final int LOGX_SHIP_BOL = 2044008;
    public static final int LOGX_SHIP_AUTH = 2044009;
    public static final int LOGX_SHIP_DEST_ETY_DPS = 2044010;
    public static final int LOGX_SHIP_DEST_ETY_DIOG = 2044011;
    public static final int LOGX_TP_MOT_SHIP = 2044012;
    public static final int LOGX_LOCATION_TP = 2044013;
    public static final int LOGX_BOL = 2044014;

    public static final int LOGR_SHIP = 2045001;

    /*
     * Manufacturing
     */

    public static final int MFGS_ST_ORD = 2051001;
    public static final int MFGS_PTY_ORD = 2051002;
    public static final int MFGS_TP_REQ = 2051003;
    public static final int MFGS_TP_COST_OBJ = 2051004;
    public static final int MFGS_TP_HR = 2051005;

    public static final int MFGU_TP_ORD = 2052001;
    public static final int MFGU_TURN = 2052002;

    public static final int MFG_BOM = 2053001;
    public static final int MFG_BOM_NTS = 2053002;
    public static final int MFG_SGDS = 2053003;
    public static final int MFG_BOM_SUB = 2053004;
    public static final int MFG_LINE = 2053005;
    public static final int MFGU_GANG = 2053006;
    public static final int MFGU_GANG_ETY = 2053007;
    public static final int MFGU_LINE = 2053008;
    public static final int MFGU_LINE_CFG_ITEM = 2053009;

    public static final int MFG = 2053501;
    public static final int MFG_ORD = 2053502;
    public static final int MFG_ORD_PER = 2053503;
    public static final int MFG_ORD_NTS = 2053504;
    public static final int MFG_ORD_CHG = 2053505;
    public static final int MFG_ORD_CHG_ETY = 2053506;
    public static final int MFG_ORD_CHG_ETY_LOT = 2053507;
    public static final int MFG_ORD_SGDS = 2053508;
    public static final int MFG_EXP = 2053509;
    public static final int MFG_EXP_ORD = 2053510;
    public static final int MFG_EXP_ETY_ITEM = 2053511;
    public static final int MFG_EXP_ETY = 2053512;
    public static final int MFG_REQ = 2053513;
    public static final int MFG_EXP_REQ = 2053514;
    public static final int MFG_REQ_ETY = 2053515;
    public static final int MFG_REQ_PUR = 2053516;
    public static final int MFG_DOC_COST = 2053517;
    public static final int MFG_DOC_COST_ETY = 2053518;
    public static final int MFG_COST = 2053519;

    /*
     * Human resources
     */

    public static final int HRSS_CL_HRS_CAT = 2061001;
    public static final int HRSS_TP_HRS_CAT = 2061002;
    public static final int HRSS_TP_PAY = 2061011;
    public static final int HRSS_TP_PAY_SHT = 2061012;
    public static final int HRSS_TP_SAL = 2061013;
    public static final int HRSS_TP_ENT = 2061014;
    public static final int HRSS_TP_ACC = 2061016;
    public static final int HRSS_TP_TAX_COMP = 2061021;
    public static final int HRSS_TP_EAR_COMP = 2061031;
    public static final int HRSS_TP_EAR_EXEM = 2061032;
    public static final int HRSS_TP_EAR = 2061036;
    public static final int HRSS_TP_OTH_PAY = 2061037;
    public static final int HRSS_TP_DED_COMP = 2061041;
    public static final int HRSS_TP_DED = 2061046;
    public static final int HRSS_TP_BEN = 2061051;
    public static final int HRSS_TP_LOAN = 2061061;
    public static final int HRSS_TP_LOAN_PAY = 2061062;
    public static final int HRSS_TP_CON = 2061071;
    public static final int HRSS_TP_REC_SCHE = 2061072;
    public static final int HRSS_TP_POS_RISK = 2061073;
    public static final int HRSS_TP_WORK_DAY = 2061074;
    public static final int HRSS_TP_DIS = 2061075;
    public static final int HRSS_TP_DAY = 2061081;
    public static final int HRSS_BANK = 2061091;
    public static final int HRSS_GROCERY_SRV = 2061096;
    public static final int HRSS_BONUS = 2061101;
    public static final int HRSS_TP_PREC = 2061501;

    public static final int HRSU_CL_ABS = 2062001;
    public static final int HRSU_TP_ABS = 2062002;
    public static final int HRSU_TP_EMP_DIS = 2062003;
    public static final int HRSU_TP_EMP = 2062011;
    public static final int HRSU_TP_WRK = 2062012;
    public static final int HRSU_TP_MWZ = 2062021;
    public static final int HRSU_TP_PAY_SHT_CUS = 2062025;
    public static final int HRSU_DEP = 2062031;
    public static final int HRSU_POS = 2062032;
    public static final int HRSU_SHT = 2062033;
    public static final int HRS_DEP_CC = 2062034;
    public static final int HRSU_EMP = 2062051;
    public static final int HRSU_EMP_REL = 2062052;
    public static final int HRSU_TP_EXP = 2062301;
    public static final int HRSU_PACK_EXP = 2062311;
    public static final int HRSU_PACK_EXP_ITEM = 2062312;
    
    public static final int HRSU_EMP_SUA = 2062053; // XXX 2023-09-07 Sergio Flores: Remove or change, it does not correspond to a DB table!
    public static final int HRSU_EMP_IDSE = 2062054; // XXX 2023-09-07 Sergio Flores: Remove or change, it does not correspond to a DB table!

    public static final int HRS_SIE_PAY = 2063001;
    public static final int HRS_SIE_PAY_EMP = 2063002;
    public static final int HRS_SIE_PAY_MOV = 2063003;
    public static final int HRS_CFG = 2063011;
    public static final int HRS_FDY = 2063012;
    public static final int HRS_HOL = 2063013;
    public static final int HRS_WDS = 2063015;
    public static final int HRS_PRE_PAY_CUT_CAL = 2063016;
    public static final int HRS_TAX = 2063021;
    public static final int HRS_TAX_ROW = 2063022;
    public static final int HRS_TAX_SUB = 2063031;
    public static final int HRS_TAX_SUB_ROW = 2063032;
    public static final int HRS_EMPL_SUB = 2063036;
    public static final int HRS_SSC = 2063041;
    public static final int HRS_SSC_ROW = 2063042;
    public static final int HRS_EMPL_QUO = 2063043;
    public static final int HRS_EMPL_QUO_ROW = 2063044;
    public static final int HRS_BEN = 2063051;
    public static final int HRS_BEN_ROW = 2063052;
    public static final int HRS_BEN_ROW_AUX = 2063056;
    public static final int HRS_WRK_SAL = 2063071;
    public static final int HRS_MWZ_WAGE = 2063072;
    public static final int HRS_UMA = 2063073;
    public static final int HRS_UMI = 2063074;
    public static final int HRS_TP_LOAN_ADJ = 2063076;
    public static final int HRS_EMP_MEMBER = 2063080;
    public static final int HRS_EMP_LOG_HIRE = 2063081;
    public static final int HRS_EMP_LOG_WAGE = 2063082;
    public static final int HRS_EMP_LOG_SAL_SSC = 2063083;
    public static final int HRS_EMP_LOG_DEP_POS = 2063084;
    public static final int HRS_EMP_BEN = 2063151;
    public static final int HRS_EMP_BEN_ANN = 2063152;
    public static final int HRS_EMP_WAGE_FAC_ANN = 2063156;
    public static final int HRS_LOAN = 2063091;
    public static final int HRS_ABS = 2063101;
    public static final int HRS_ABS_CNS = 2063102;
    public static final int HRS_EAR = 2063111;
    public static final int HRS_DED = 2063112;
    public static final int HRS_COND_EAR = 2063120;
    public static final int HRS_AUT_EAR = 2063121;
    public static final int HRS_AUT_DED = 2063122;
    public static final int HRS_ACC_EAR = 2063131;
    public static final int HRS_ACC_DED = 2063132;
    public static final int HRS_TP_EXP_ACC = 2063301;
    public static final int HRS_PACK_CC = 2063311;
    public static final int HRS_PACK_CC_CC = 2063312;
    public static final int HRS_CFG_ACC_DEP = 2063321;
    public static final int HRS_CFG_ACC_DEP_PACK_CC = 2063322;
    public static final int HRS_CFG_ACC_EMP_PACK_CC = 2063326;
    public static final int HRS_CFG_ACC_EMP_EAR = 2063327;
    public static final int HRS_CFG_ACC_EMP_DED = 2063328;
    public static final int HRS_CFG_ACC_EAR = 2063331;
    public static final int HRS_CFG_ACC_DED = 2063332;
    public static final int HRS_PAY = 2063201;
    public static final int HRS_PAY_RCP = 2063211;
    public static final int HRS_PAY_RCP_IMPORT = 2063215;
    public static final int HRS_PAY_RCP_ISS = 2063216;
    public static final int HRS_PAY_RCP_DAY = 2063221;
    public static final int HRS_PAY_RCP_EAR = 2063231;
    public static final int HRS_PAY_RCP_EAR_CMP = 2063236;
    public static final int HRS_PAY_RCP_DED = 2063241;
    public static final int HRS_PAY_RCP_DED_CMP = 2063246;
    public static final int HRS_ACC_PAY = 2063251;
    public static final int HRS_ACC_PAY_RCP = 2063252;
    public static final int HRS_ADV_SET = 2063261;
    public static final int HRS_PREC = 2063501;
    public static final int HRS_PREC_SEC = 2063502;
    public static final int HRS_PREC_SUBSEC = 2063503;
    public static final int HRS_DOC_BREACH = 2063511;
    public static final int HRS_DOC_BREACH_PREC_SUBSEC = 2063512;
    public static final int HRS_DOC_ADM_REC = 2063521;
    public static final int HRS_DOC_ADM_REC_PREC_SUBSEC = 2063522;

    public static final int HRSU_TP_PAY_SYS = 2064050;

    public static final int HRSX_DATE = 2064001;
    public static final int HRSX_AUT_EAR = 2064021;
    public static final int HRSX_AUT_DED = 2064022;
    public static final int HRSX_PAY_REC = 2064031;
    public static final int HRSX_PAY_REC_EAR = 2064036;
    public static final int HRSX_PAY_REC_DED = 2064037;
    public static final int HRSX_PAY_REC_EMP = 2064038;
    public static final int HRSX_PAY_REC_REC = 2064039;
    public static final int HRSX_PAY_LAY = 2064041;
    public static final int HRSX_BEN_MOV = 2064051;
    public static final int HRSX_BEN_SUM = 2064052;
    public static final int HRSX_BEN_DET = 2064053;
    public static final int HRSX_BEN_VAC_PEND = 2064056;
    public static final int HRSX_BEN_VAC_STAT = 2064057;
    public static final int HRSX_EMP_INT = 2064061; // employees integral query
    public static final int HRSX_EMP_CON_EXP = 2064066; // employees contract expiration
    public static final int HRSX_EMP_LOG_HIRE_BY_PER = 2064069; // hire and dismisss query by period
    public static final int HRSX_EMP_MASSIVE_UPD_SSC = 2063071;
    public static final int HRSX_LOAN_PAY = 2064091;
    public static final int HRSX_ABS_MOV = 2064101;
    public static final int HRSX_SSC_UPD = 2064102;
    public static final int HRSX_EAR_SSC = 2064103;
    public static final int HRSX_PTU = 2064106;
    public static final int HRSX_BEN_ANN_BON = 2064107;
    public static final int HRSX_BANK_PAY_DISP = 2064108;
    public static final int HRSX_EMP_CC = 2064109;
    public static final int HRSX_IMPORT_CAP = 2064110;
    public static final int HRSX_EMP_QUO = 2064111;
    public static final int HRSX_EMP_QUO_REP = 2064112;
    public static final int HRSX_EMP_LOG_SUA = 2064161;
    public static final int HRSX_EMP_LOG_IDSE = 2064162;
    public static final int HRSX_DOC_BREACH_SUM = 2064511;
    public static final int HRSX_DOC_ADM_REC_SUM = 2064521;
    
    public static final int HRSX_LAYOUT_SUA_HIRE = 1; // High worker
    public static final int HRSX_LAYOUT_SUA_DISMISS = 2; // Low worker
    public static final int HRSX_LAYOUT_IDSE_HIRE = 3; // High worker IDSE
    public static final int HRSX_LAYOUT_IDSE_DISMISS = 4; //Low worker IDSE
    public static final int HRSX_LAYOUT_IDSE_SSC = 5; // Modification of worker's salary IDSE
    public static final int HRSX_LAYOUT_SUA_SSC = 7; // Modification of worker's salary
    public static final int HRSX_LAYOUT_SUA_ENTRY = 8; // Reentry worker
    public static final int HRSX_LAYOUT_SUA_VOLUNT = 9; // Voluntary contribution
    public static final int HRSX_LAYOUT_SUA_TRUANCY = 11; // Absense of the worker
    public static final int HRSX_LAYOUT_SUA_INABILITY = 12; // Worker disability docs mov 
    public static final int HRSX_LAYOUT_SUA_INABILITY_IMP = 13; // Worker disability
    public static final int HRSX_LAYOUT_SUA_ABS_TRUANCY = 1; // Absence of the worker variable
    public static final int HRSX_LAYOUT_SUA_ABS_INABILITY = 2; // Disability the worker variable
    public static final int HRSX_LAYOUT_SUA_AFI = 14; // Affiliate data
    public static final int HRSX_HIRE_DISMISSED = 0; // Employee hire log dismissed
    public static final int HRSX_HIRE_ACTIVE = 1;    // Employee hire log active

    public static final int HRSR_PAY = 2065001;
    public static final int HRSR_PRE_PAY = 2065002;
    public static final int HRSR_PAY_SUM = 2065003;
    public static final int HRSR_PAY_RCP = 2065004;
    public static final int HRSR_PAY_RCP_PAY = 2065005;
    public static final int HRSR_PAY_TAX = 2065031;
    public static final int HRSR_PAY_AUX = 2065032;
    public static final int HRSR_PAY_AUX_EAR_DED = 2065033;
    public static final int HRSR_PAY_EAR_DED = 2065034;
    public static final int HRSR_LIST_EAR = 2065041;
    public static final int HRSR_LIST_DED = 2065042;
    public static final int HRSR_ACT_EMP = 2065043;
    public static final int HRSR_POS = 2065044;
    public static final int HRSR_WAGE_SAL_CSV = 2065051;
    public static final int HRSR_EAR_DED_CSV = 2065052;
    public static final int HRSR_VAC_CSV = 2065053;
    public static final int HRSR_DOC_BREACH = 2065511;
    public static final int HRSR_DOC_ADM_REC = 2065521;

    /*
     * Quality
     */

    public static final int QLT_LOT_APR = 2073001;
    public static final int QLT_TP_ANALYSIS = 2073010;
    public static final int QLT_ANALYSIS = 2073011;
    public static final int QLT_ANALYSIS_ITEM = 2073012;
    public static final int QLT_DATASHEET_TEMPLATE = 2073012;
    public static final int QLT_DATASHEET_TEMPLATE_ROW = 2073013;
    public static final int QLT_DATASHEET_TEMPLATE_LINK = 2073014;
    public static final int QLT_QLTY_CONFIG_REQUIRED = 2073015;
    public static final int QLT_COA_RESULT = 2073008;
    public static final int QLT_COA_RESULT_ROW = 2073009;
    
    public static final int QLTR_COA_RESULT = 2074008;
    
    public static final int QLTX_QLT_DPS_ETY = 2075001;
    
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
        TablesMap.put(CFGS_TP_MMS, "erp.cfgs_tp_mms");
        TablesMap.put(CFGS_TP_MOD, "erp.cfgs_tp_mod");
        TablesMap.put(CFGS_TP_AUTHORN, "erp.cfgs_tp_authorn");
        TablesMap.put(CFGS_ST_AUTHORN, "erp.cfgs_st_authorn");
        TablesMap.put(CFGS_TP_DOC, "erp.cfgs_tp_doc");

        TablesMap.put(CFGU_CUR, "erp.cfgu_cur");
        TablesMap.put(CFGU_CO, "erp.cfgu_co");
        TablesMap.put(CFGU_COB_ENT, "erp.cfgu_cob_ent");
        TablesMap.put(CFGU_LAN, "erp.cfgu_lan");
        TablesMap.put(CFGU_SCA, "erp.cfgu_sca");
        TablesMap.put(CFG_PARAM_ERP, "erp.cfg_param_erp");
        TablesMap.put(CFG_SYNC_LOG, "erp.cfg_sync_log");
        TablesMap.put(CFG_SYNC_LOG_ETY, "erp.cfg_sync_log_ety");
        
        TablesMap.put(CFGU_CERT, "cfgu_cert");
        TablesMap.put(CFGU_FUNC, "cfgu_func");
        TablesMap.put(CFGU_FUNC_SUB, "cfgu_func_sub");
        TablesMap.put(CFGU_SHIFT, "cfgu_shift");
        TablesMap.put(CFGU_AUTHORN_NODE, "cfgu_authorn_node");
        TablesMap.put(CFGU_AUTHORN_NODE_USR, "cfgu_authorn_node_usr");
        TablesMap.put(CFGU_AUTHORN_NODE_POS, "cfgu_authorn_node_pos");
        TablesMap.put(CFGU_AUTHORN_PATH, "cfgu_authorn_path");
        TablesMap.put(CFGU_AUTHORN_STEP, "cfgu_authorn_step");
        TablesMap.put(CFGU_DOC, "cfgu_doc");

        TablesMap.put(CFG_MMS, "cfg_mms");
        TablesMap.put(CFG_PARAM_CO, "cfg_param_co");
        TablesMap.put(CFG_PARAM, "cfg_param");
        TablesMap.put(CFG_CFD, "cfg_cfd");
        TablesMap.put(CFG_CUSTOM_REP, "cfg_custom_rep");
        TablesMap.put(CFG_COM_SYNC_LOG, "cfg_com_sync_log");
        TablesMap.put(CFG_COM_SYNC_LOG_ETY, "cfg_com_sync_log_ety");

        TablesMap.put(USRS_TP_USR, "erp.usrs_tp_usr");
        TablesMap.put(USRS_TP_LEV, "erp.usrs_tp_lev");
        TablesMap.put(USRS_TP_PRV, "erp.usrs_tp_prv");
        TablesMap.put(USRS_TP_ROL, "erp.usrs_tp_rol");
        TablesMap.put(USRS_PRV, "erp.usrs_prv");
        TablesMap.put(USRS_ROL, "erp.usrs_rol");
        TablesMap.put(USRS_ROL_PRV, "erp.usrs_rol_prv");
        TablesMap.put(USRS_LINK, "erp.usrs_link");

        TablesMap.put(USRU_ACCESS_CO, "erp.usru_access_co");
        TablesMap.put(USRU_ACCESS_COB, "erp.usru_access_cob");
        TablesMap.put(USRU_ACCESS_COB_ENT, "erp.usru_access_cob_ent");
        TablesMap.put(USRU_ACCESS_COB_ENT_UNIV, "erp.usru_access_cob_ent_univ");
        TablesMap.put(USRU_PRV_USR, "erp.usru_prv_usr");
        TablesMap.put(USRU_PRV_CO, "erp.usru_prv_co");
        TablesMap.put(USRU_ROL_USR, "erp.usru_rol_usr");
        TablesMap.put(USRU_ROL_CO, "erp.usru_rol_co");
        TablesMap.put(USRU_USR, "erp.usru_usr");
        TablesMap.put(USRU_USR_GRP, "erp.usru_usr_grp");
        TablesMap.put(USRU_USR_GRP_USR, "erp.usru_usr_grp_usr");

        TablesMap.put(USR_USR_GUI, "usr_usr_gui");
        TablesMap.put(USR_USR_FUNC, "usr_usr_func");
        TablesMap.put(USR_USR_FUNC_SUB, "usr_usr_func_sub");

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
        TablesMap.put(BPSU_BP_UPD_LOG, "erp.bpsu_bp_upd_log");
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
        TablesMap.put(BPSU_SCA_BP, "erp.bpsu_sca_bp");
        TablesMap.put(BPSU_SCA_BP_MAP, "erp.bpsu_sca_bp_map");

        TablesMap.put(ITMS_CT_ITEM, "erp.itms_ct_item");
        TablesMap.put(ITMS_CL_ITEM, "erp.itms_cl_item");
        TablesMap.put(ITMS_TP_ITEM, "erp.itms_tp_item");
        TablesMap.put(ITMS_TP_SNR, "erp.itms_tp_snr");
        TablesMap.put(ITMS_LINK, "erp.itms_link");
        TablesMap.put(ITMS_ST_ITEM, "erp.itms_st_item");
        TablesMap.put(ITMS_CFD_PROD_SERV, "erp.itms_cfd_prod_serv");
        TablesMap.put(ITMS_CFD_UNIT, "erp.itms_cfd_unit");

        TablesMap.put(ITMU_IFAM, "erp.itmu_ifam");
        TablesMap.put(ITMU_IGRP, "erp.itmu_igrp");
        TablesMap.put(ITMU_IGEN, "erp.itmu_igen");
        TablesMap.put(ITMU_IGEN_BA, "erp.itmu_igen_ba");
        TablesMap.put(ITMU_LINE, "erp.itmu_line");
        TablesMap.put(ITMU_ITEM, "erp.itmu_item");
        TablesMap.put(ITMU_ITEM_BARC, "erp.itmu_item_barc");
        TablesMap.put(ITMU_CFG_ITEM_LAN, "erp.itmu_cfg_item_lan");
        TablesMap.put(ITMU_CFG_ITEM_BP, "erp.itmu_cfg_item_bp");
        TablesMap.put(ITMU_TP_MAT_MAT_ATT, "erp.itmu_tp_mat_mat_att");
        TablesMap.put(ITMU_ITEM_MAT_ATT, "erp.itmu_item_mat_att");
        TablesMap.put(ITMU_MATCH_ITEM_CPT_BP, "erp.itmu_match_item_cpt_bp");
        TablesMap.put(ITMU_SCA_ITEM, "erp.itmu_sca_item");
        TablesMap.put(ITMU_SCA_ITEM_MAP, "erp.itmu_sca_item_map");

        TablesMap.put(ITMU_TP_LEV, "erp.itmu_tp_lev");
        TablesMap.put(ITMU_TP_UNIT, "erp.itmu_tp_unit");
        TablesMap.put(ITMU_UNIT, "erp.itmu_unit");
        TablesMap.put(ITMU_UNIT_EQUIV, "erp.itmu_unit_equiv");
        TablesMap.put(ITMU_TP_VAR, "erp.itmu_tp_var");
        TablesMap.put(ITMU_VAR, "erp.itmu_var");
        TablesMap.put(ITMU_TP_BRD, "erp.itmu_tp_brd");
        TablesMap.put(ITMU_BRD, "erp.itmu_brd");
        TablesMap.put(ITMU_TP_MFR, "erp.itmu_tp_mfr");
        TablesMap.put(ITMU_MFR, "erp.itmu_mfr");
        TablesMap.put(ITMU_TP_EMT, "erp.itmu_tp_emt");
        TablesMap.put(ITMU_EMT, "erp.itmu_emt");
        TablesMap.put(ITMU_TP_MAT, "erp.itmu_tp_mat");
        TablesMap.put(ITMU_MAT_ATT, "erp.itmu_mat_att");

        TablesMap.put(ITMU_MATCH_ITEM_CPT_BP_COMP, "itmu_match_item_cpt_bp_comp");
        TablesMap.put(ITMU_PRICE_COMM_LOG, "itmu_price_comm_log");
        TablesMap.put(ITMU_ITEM_COMP, "itmu_item_comp");
        
        TablesMap.put(FINS_TP_BKR, "erp.fins_tp_bkr");
        TablesMap.put(FINS_TP_ACC_MOV, "erp.fins_tp_acc_mov");
        TablesMap.put(FINS_CL_ACC_MOV, "erp.fins_cl_acc_mov");
        TablesMap.put(FINS_CLS_ACC_MOV, "erp.fins_cls_acc_mov");
        TablesMap.put(FINS_TP_ACC, "erp.fins_tp_acc");
        TablesMap.put(FINS_CL_ACC, "erp.fins_cl_acc");
        TablesMap.put(FINS_CLS_ACC, "erp.fins_cls_acc");
        TablesMap.put(FINS_TP_ACC_SPE, "erp.fins_tp_acc_spe");
        TablesMap.put(FINS_CL_SYS_ACC, "erp.fins_cl_sys_acc");
        TablesMap.put(FINS_TP_SYS_ACC, "erp.fins_tp_sys_acc");
        TablesMap.put(FINS_CL_SYS_MOV, "erp.fins_cl_sys_mov");
        TablesMap.put(FINS_TP_SYS_MOV, "erp.fins_tp_sys_mov");
        TablesMap.put(FINS_TP_ACC_SYS, "erp.fins_tp_acc_sys");
        TablesMap.put(FINS_CT_SYS_MOV_XXX, "erp.fins_ct_sys_mov_xxx");
        TablesMap.put(FINS_TP_SYS_MOV_XXX, "erp.fins_tp_sys_mov_xxx");
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
        TablesMap.put(FINS_TP_FISCAL_ACC_LINK, "erp.fins_tp_fiscal_acc_link");
        TablesMap.put(FINS_CFD_TAX, "erp.fins_cfd_tax");

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

        TablesMap.put(FIN_YEAR, "fin_year");
        TablesMap.put(FIN_YEAR_PER, "fin_year_per");
        TablesMap.put(FIN_EXC_RATE, "fin_exc_rate");
        TablesMap.put(FIN_ACC, "fin_acc");
        TablesMap.put(FIN_ACC_ITEM_LINK, "fin_acc_item_link");
        TablesMap.put(FIN_CC, "fin_cc");
        TablesMap.put(FIN_ACC_CASH, "fin_acc_cash");
        TablesMap.put(FIN_CHECK_WAL, "fin_check_wal");
        TablesMap.put(FIN_CHECK, "fin_check");
        TablesMap.put(FIN_TAX_GRP, "fin_tax_grp");
        TablesMap.put(FIN_TAX_GRP_ETY, "fin_tax_grp_ety");
        TablesMap.put(FIN_TAX_GRP_IGEN, "fin_tax_grp_igen");
        TablesMap.put(FIN_TAX_GRP_ITEM, "fin_tax_grp_item");
        TablesMap.put(FIN_TAX_ITEM_LINK, "fin_tax_item_link");
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
        TablesMap.put(FIN_ABP_ENT, "fin_abp_ent");
        TablesMap.put(FIN_ABP_ENT_LINK, "fin_abp_ent_link");
        TablesMap.put(FIN_ABP_BP, "fin_abp_bp");
        TablesMap.put(FIN_ABP_BP_LINK, "fin_abp_bp_link");
        TablesMap.put(FIN_ABP_ITEM, "fin_abp_item");
        TablesMap.put(FIN_ABP_ITEM_LINK, "fin_abp_item_link");
        TablesMap.put(FIN_ABP_TAX, "fin_abp_tax");
        TablesMap.put(FIN_FISCAL_ACC_LINK, "fin_fiscal_acc_link");
        TablesMap.put(FIN_FISCAL_ACC_LINK_DET, "fin_fiscal_acc_link_det");
        TablesMap.put(FIN_LAY_BANK, "fin_lay_bank");
        TablesMap.put(FIN_LAY_BANK_DEP, "fin_lay_bank_dep");
        TablesMap.put(FIN_LAY_BANK_DEP_ANA, "fin_lay_bank_dep_ana");
        TablesMap.put(FIN_REP_CUS_ACC, "fin_rep_cus_acc");
        TablesMap.put(FIN_REP_CUS_ACC_ACC, "fin_rep_cus_acc_acc");
        TablesMap.put(FIN_REP_CUS_ACC_CC, "fin_rep_cus_acc_cc");
        TablesMap.put(FIN_REP_CUS_ACC_USR, "fin_rep_cus_acc_usr");

        TablesMap.put(FIN_BKC, "fin_bkc");

        TablesMap.put(TRNS_CT_DPS, "erp.trns_ct_dps");
        TablesMap.put(TRNS_CL_DPS, "erp.trns_cl_dps");
        TablesMap.put(TRNS_TP_DPS_ADJ, "erp.trns_tp_dps_adj");
        TablesMap.put(TRNS_STP_DPS_ADJ, "erp.trns_stp_dps_adj");
        TablesMap.put(TRNS_CT_IOG, "erp.trns_ct_iog");
        TablesMap.put(TRNS_CL_IOG, "erp.trns_cl_iog");
        TablesMap.put(TRNS_TP_IOG, "erp.trns_tp_iog");
        TablesMap.put(TRNS_TP_PAY, "erp.trns_tp_pay");
        TablesMap.put(TRNS_TP_PAY_WAY, "erp.trns_tp_pay_way");
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
        TablesMap.put(TRNS_TP_STK_SEG, "erp.trns_tp_stk_seg");
        TablesMap.put(TRNS_TP_STK_SEG_MOV, "erp.trns_tp_stk_seg_mov");
        TablesMap.put(TRNS_CFD_CAT, "erp.trns_cfd_cat");
        TablesMap.put(TRNS_TP_MAINT_MOV, "erp.trns_tp_maint_mov");
        TablesMap.put(TRNS_ST_MAT_REQ, "erp.trns_st_mat_req");
        TablesMap.put(TRNS_ST_MAT_PROV, "erp.trns_st_mat_prov");
        TablesMap.put(TRNS_ST_MAT_PUR, "erp.trns_st_mat_pur");
        TablesMap.put(TRNS_TP_PERIOD, "erp.trns_tp_period");

        TablesMap.put(TRNU_DPS_NAT, "erp.trnu_dps_nat");
        TablesMap.put(TRNU_TP_DPS, "erp.trnu_tp_dps");
        TablesMap.put(TRNU_TP_DPS_SRC_ITEM, "erp.trnu_tp_dps_src_item");
        TablesMap.put(TRNU_TP_PAY_SYS, "erp.trnu_tp_pay_sys");
        TablesMap.put(TRNU_TP_IOG_ADJ, "erp.trnu_tp_iog_adj");
        TablesMap.put(TRNU_TP_DPS_ANN, "erp.trnu_tp_dps_ann");
        TablesMap.put(TRNU_MAT_REQ_PTY, "erp.trnu_mat_req_pty");
        TablesMap.put(TRNU_SCA_TIC, "erp.trnu_sca_tic");

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
        TablesMap.put(TRN_DPS_CFD, "trn_dps_cfd");
        TablesMap.put(TRN_DPS_CFD_ETY, "trn_dps_cfd_ety");
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
        TablesMap.put(TRN_DPS_ETY_ITEM_COMP, "trn_dps_ety_item_comp");
        TablesMap.put(TRN_DPS_ETY_QTY_CHG, "trn_dps_ety_qty_chg");
        TablesMap.put(TRN_DPS_ETY_IOG_ETY_XFR, "trn_dps_ety_iog_ety_xfr");
        TablesMap.put(TRN_DPS_ETY_ANALYSIS, "trn_dps_ety_analysis");
        TablesMap.put(TRN_DPS_RISS, "trn_dps_riss");
        TablesMap.put(TRN_DPS_REPL, "trn_dps_repl");
        TablesMap.put(TRN_DPS_DPS_SUPPLY, "trn_dps_dps_supply");
        TablesMap.put(TRN_DPS_DPS_ADJ, "trn_dps_dps_adj");
        TablesMap.put(TRN_DPS_MAT_REQ, "trn_dps_mat_req");
        TablesMap.put(TRN_DPS_IOG_CHG, "trn_dps_iog_chg");
        TablesMap.put(TRN_DPS_IOG_WAR, "trn_dps_iog_war");
        TablesMap.put(TRN_DPS_REC, "trn_dps_rec");
        TablesMap.put(TRN_SUP_FILE, "trn_sup_file");
        TablesMap.put(TRN_SUP_FILE_DPS, "trn_sup_file_dps");
        TablesMap.put(TRN_SUP_FILE_DPS_ETY, "trn_sup_file_dps_ety");
        TablesMap.put(TRN_SCA_TIC_DPS, "trn_sca_tic_dps");
        TablesMap.put(TRN_SCA_TIC_DPS_ETY, "trn_sca_tic_dps_ety");
        TablesMap.put(TRN_INIT, "trn_init");
        TablesMap.put(TRN_INIT_DPS, "trn_init_dps");
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
        TablesMap.put(TRN_STK_VAL, "trn_stk_val");
        TablesMap.put(TRN_STK_VAL_MVT, "trn_stk_val_mvt");
        TablesMap.put(TRN_STK_VAL_ACC, "trn_stk_val_acc");
        TablesMap.put(TRN_STK_VAL_DIOG_ADJ, "trn_stk_val_diog_adj");
        TablesMap.put(TRN_INV_VAL, "trn_inv_val");
        TablesMap.put(TRN_INV_MFG_CST, "trn_inv_mfg_cst");
        TablesMap.put(TRN_ITEM_COST, "trn_item_cost");
        TablesMap.put(TRN_COST_IDENT_CALC, "trn_cost_ident_calc");
        TablesMap.put(TRN_COST_IDENT_LOT, "trn_cost_ident_lot");
        TablesMap.put(TRN_PDF, "trn_pdf");
        TablesMap.put(TRN_CFD, "trn_cfd");
        TablesMap.put(TRN_CFD_SIGN_LOG, "trn_cfd_sign_log");
        TablesMap.put(TRN_CFD_SIGN_LOG_MSG, "trn_cfd_sign_log_msg");
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
        TablesMap.put(TRN_MMS_CFG, "trn_mms_cfg");
        TablesMap.put(TRN_DVY, "trn_dvy");
        TablesMap.put(TRN_DVY_ETY, "trn_dvy_ety");
        TablesMap.put(TRN_MAINT_CFG, "trn_maint_cfg");
        TablesMap.put(TRN_MAINT_AREA, "trn_maint_area");
        TablesMap.put(TRN_MAINT_USER, "trn_maint_user");
        TablesMap.put(TRN_MAINT_USER_SUPV, "trn_maint_user_supv");
        TablesMap.put(TRN_MAINT_DIOG_SIG, "trn_maint_diog_sig");
        TablesMap.put(TRN_FUNC_BUDGET, "trn_func_budget");
        TablesMap.put(TRN_MAT_CC_GRP, "trn_mat_cc_grp");
        TablesMap.put(TRN_MAT_CC_GRP_ITEM, "trn_mat_cc_grp_item");
        TablesMap.put(TRN_MAT_CC_GRP_USR, "trn_mat_cc_grp_usr");
        TablesMap.put(TRN_MAT_CONS_ENT, "trn_mat_cons_ent");
        TablesMap.put(TRN_MAT_CONS_ENT_BUDGET, "trn_mat_cons_ent_budget");
        TablesMap.put(TRN_MAT_CONS_ENT_USR, "trn_mat_cons_ent_usr");
        TablesMap.put(TRN_MAT_CONS_ENT_WHS, "trn_mat_cons_ent_whs");
        TablesMap.put(TRN_MAT_CONS_SUBENT, "trn_mat_cons_subent");
        TablesMap.put(TRN_MAT_CONS_SUBENT_USR, "trn_mat_cons_subent_usr");
        TablesMap.put(TRN_MAT_CONS_SUBENT_CC, "trn_mat_cons_subent_cc");
        TablesMap.put(TRN_MAT_CONS_SUBENT_CC_CC_GRP, "trn_mat_cons_subent_cc_cc_grp");
        TablesMap.put(TRN_MAT_PROV_ENT, "trn_mat_prov_ent");
        TablesMap.put(TRN_MAT_PROV_ENT_USR, "trn_mat_prov_ent_usr");
        TablesMap.put(TRN_MAT_PROV_ENT_WHS, "trn_mat_prov_ent_whs");
        TablesMap.put(TRN_MAT_REQ, "trn_mat_req");
        TablesMap.put(TRN_MAT_REQ_CC, "trn_mat_req_cc");
        TablesMap.put(TRN_MAT_REQ_NTS, "trn_mat_req_nts");
        TablesMap.put(TRN_MAT_REQ_ETY, "trn_mat_req_ety");
        TablesMap.put(TRN_MAT_REQ_ETY_NTS, "trn_mat_req_ety_nts");
        TablesMap.put(TRN_MAT_REQ_ETY_ITEM_CHG, "trn_mat_req_ety_item_chg");
        TablesMap.put(TRN_MAT_REQ_ST_LOG, "trn_mat_req_st_log");
        TablesMap.put(TRN_MAT_REQ_EXT_STO_LOG, "trn_mat_req_ext_sto_log");
        TablesMap.put(TRN_EST_REQ, "trn_est_req");
        TablesMap.put(TRN_EST_REQ_ETY, "trn_est_req_ety");
        TablesMap.put(TRN_EST_REQ_REC, "trn_est_req_rec");
        TablesMap.put(TRN_DPS_CFD_PAY_DONE, "trn_dps_cfd_pay_done");
        TablesMap.put(TRN_CFD_PAY, "trn_cfd_pay");
        TablesMap.put(TRN_DPS_AUTHORN, "trn_dps_authorn");

        TablesMap.put(TRN_DNC_DPS, "trn_dnc_dps");
        TablesMap.put(TRN_DNC_DPS_DNS, "trn_dnc_dps_dns");
        TablesMap.put(TRN_DNC_DIOG, "trn_dnc_diog");
        TablesMap.put(TRN_DNC_DIOG_DNS, "trn_dnc_diog_dns");

        TablesMap.put(MKTS_TP_PLIST_GRP, "erp.mkts_tp_plist_grp");
        TablesMap.put(MKTS_TP_LIST, "erp.mkts_tp_list");
        TablesMap.put(MKTS_TP_PRICE_UPD, "erp.mkts_tp_price_upd");
        TablesMap.put(MKTS_TP_DISC_APP, "erp.mkts_tp_disc_app");
        TablesMap.put(MKTS_TP_COMMS, "erp.mkts_tp_comms");
        TablesMap.put(MKTS_ORIG_COMMS, "erp.mkts_orig_comms");

        TablesMap.put(MKTU_PLIST_SRP, "erp.mktu_plist_srp");
        TablesMap.put(MKTU_PLIST_PUB, "erp.mktu_plist_pub");

        TablesMap.put(MKT_PLIST_PUB_CO, "mkt_plist_pub_co");
        TablesMap.put(MKT_PLIST_PUB_COB, "mkt_plist_pub_cob");
        TablesMap.put(MKT_CFG_CUS, "mkt_cfg_cus");
        TablesMap.put(MKT_CFG_CUSB, "mkt_cfg_cusb");
        TablesMap.put(MKT_CFG_SAL_AGT, "mkt_cfg_sal_agt");
        TablesMap.put(MKT_PLIST_GRP, "mkt_plist_grp");
        TablesMap.put(MKT_PLIST_GRP_ITEM, "mkt_plist_grp_item");
        TablesMap.put(MKT_PLIST_GRP_CUS, "mkt_plist_grp_cus");
        TablesMap.put(MKT_PLIST_GRP_CUS_TP, "mkt_plist_grp_cus_tp");
        TablesMap.put(MKT_PLIST_GRP_BP_TP, "mkt_plist_grp_bp_tp");
        TablesMap.put(MKT_PLIST, "mkt_plist");
        TablesMap.put(MKT_PLIST_ITEM, "mkt_plist_item");
        TablesMap.put(MKT_PLIST_PRICE, "mkt_plist_price");
        TablesMap.put(MKT_PLIST_BP_LINK, "mkt_plist_bp_link");
        TablesMap.put(MKT_PRICE_FIX_CUS, "mkt_price_fix_cus");
        TablesMap.put(MKT_PRICE_FIX_CUS_TP, "mkt_price_fix_cus_tp");
        TablesMap.put(MKT_DISC_ADD_CUS, "mkt_disc_add_cus");
        TablesMap.put(MKT_DISC_ADD_CUS_TP, "mkt_disc_add_cus_tp");
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

        TablesMap.put(LOGS_TP_SHIP, "erp.logs_tp_ship");
        TablesMap.put(LOGS_TP_DLY, "erp.logs_tp_dly");
        TablesMap.put(LOGS_TP_SPOT, "erp.logs_tp_spot");
        TablesMap.put(LOGS_TP_MOT, "erp.logs_tp_mot");
        TablesMap.put(LOGS_TP_CAR, "erp.logs_tp_car");
        TablesMap.put(LOGS_TP_DOC_SHIP, "erp.logs_tp_doc_ship");
        TablesMap.put(LOGS_TP_BOL_PERSON, "erp.logs_tp_bol_person");
        TablesMap.put(LOGS_INC, "erp.logs_inc");

        TablesMap.put(LOGU_TP_VEH, "erp.logu_tp_veh");
        TablesMap.put(LOGU_BPB_ADD_NEI, "erp.logu_bpb_add_nei");
        TablesMap.put(LOGU_SPOT, "erp.logu_spot");
        TablesMap.put(LOGU_SPOT_COB, "erp.logu_spot_cob");
        TablesMap.put(LOGU_SPOT_COB_ENT, "erp.logu_spot_cob_ent");

        TablesMap.put(LOG_INSURER, "log_insurer");
        TablesMap.put(LOG_VEH, "log_veh");
        TablesMap.put(LOG_TRAILER, "log_trailer");
        TablesMap.put(LOG_TANK_CAR, "log_tank_car");
        TablesMap.put(LOG_BOL_PERSON, "log_bol_person");
        TablesMap.put(LOG_DRIVER_VEH, "log_driver_veh");
        TablesMap.put(LOG_DIST_LOCATION, "log_dist_location");
        TablesMap.put(LOG_RATE, "log_rate");
        TablesMap.put(LOG_SHIP, "log_ship");
        TablesMap.put(LOG_SHIP_NTS, "log_ship_nts");
        TablesMap.put(LOG_SHIP_DEST, "log_ship_dest");
        TablesMap.put(LOG_SHIP_DEST_ETY, "log_ship_dest_ety");
        TablesMap.put(LOG_BOL, "log_bol");
        TablesMap.put(LOG_BOL_TRANSP_MODE, "log_bol_transp_mode");
        TablesMap.put(LOG_BOL_TRANSP_MODE_EXTRA, "log_bol_transp_mode_extra");
        TablesMap.put(LOG_BOL_LOCATION, "log_bol_location");
        TablesMap.put(LOG_BOL_MERCH, "log_bol_merch");
        TablesMap.put(LOG_BOL_MERCH_QTY, "log_bol_merch_qty");

        TablesMap.put(LOGX_TP_MOT_SHIP, "erp.logs_tp_mot");

        TablesMap.put(MFGS_ST_ORD, "erp.mfgs_st_ord");
        TablesMap.put(MFGS_PTY_ORD, "erp.mfgs_pty_ord");
        TablesMap.put(MFGS_TP_REQ, "erp.mfgs_tp_req");
        TablesMap.put(MFGS_TP_COST_OBJ, "erp.mfgs_tp_cost_obj");
        TablesMap.put(MFGS_TP_HR, "erp.mfgs_tp_hr");

        TablesMap.put(MFGU_TP_ORD, "erp.mfgu_tp_ord");
        TablesMap.put(MFGU_TURN, "erp.mfgu_turn");

        TablesMap.put(MFG_BOM, "mfg_bom");
        TablesMap.put(MFG_BOM_NTS, "mfg_bom_nts");
        TablesMap.put(MFG_SGDS, "mfg_sgds");
        TablesMap.put(MFG_BOM_SUB, "mfg_bom_sub");
        TablesMap.put(MFG_LINE, "mfg_line");

        TablesMap.put(MFGU_GANG, "mfgu_gang");
        TablesMap.put(MFGU_GANG_ETY, "mfgu_gang_ety");
        TablesMap.put(MFGU_LINE, "mfgu_line");
        TablesMap.put(MFGU_LINE_CFG_ITEM, "mfgu_line_cfg_item");

        TablesMap.put(MFG, "mfg");
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
        TablesMap.put(MFG_DOC_COST, "mfg_doc_cost");
        TablesMap.put(MFG_DOC_COST_ETY, "mfg_doc_cost_ety");
        TablesMap.put(MFG_COST, "mfg_cost");

        TablesMap.put(HRSS_CL_HRS_CAT, "erp.hrss_cl_hrs_cat");
        TablesMap.put(HRSS_TP_HRS_CAT, "erp.hrss_tp_hrs_cat");
        TablesMap.put(HRSS_TP_PAY, "erp.hrss_tp_pay");
        TablesMap.put(HRSS_TP_PAY_SHT, "erp.hrss_tp_pay_sht");
        TablesMap.put(HRSS_TP_SAL, "erp.hrss_tp_sal");
        TablesMap.put(HRSS_TP_ACC, "erp.hrss_tp_acc");
        TablesMap.put(HRSS_TP_TAX_COMP, "erp.hrss_tp_tax_comp");
        TablesMap.put(HRSS_TP_EAR_COMP, "erp.hrss_tp_ear_comp");
        TablesMap.put(HRSS_TP_EAR_EXEM, "erp.hrss_tp_ear_exem");
        TablesMap.put(HRSS_TP_EAR, "erp.hrss_tp_ear");
        TablesMap.put(HRSS_TP_OTH_PAY, "erp.hrss_tp_oth_pay");
        TablesMap.put(HRSS_TP_DED_COMP, "erp.hrss_tp_ded_comp");
        TablesMap.put(HRSS_TP_DED, "erp.hrss_tp_ded");
        TablesMap.put(HRSS_TP_BEN, "erp.hrss_tp_ben");
        TablesMap.put(HRSS_TP_LOAN, "erp.hrss_tp_loan");
        TablesMap.put(HRSS_TP_LOAN_PAY, "erp.hrss_tp_loan_pay");
        TablesMap.put(HRSS_TP_CON, "erp.hrss_tp_con");
        TablesMap.put(HRSS_TP_REC_SCHE, "erp.hrss_tp_rec_sche");
        TablesMap.put(HRSS_TP_POS_RISK, "erp.hrss_tp_pos_risk");
        TablesMap.put(HRSS_TP_WORK_DAY, "erp.hrss_tp_work_day");
        TablesMap.put(HRSS_TP_DIS, "erp.hrss_tp_dis");
        TablesMap.put(HRSS_TP_DAY, "erp.hrss_tp_day");
        TablesMap.put(HRSS_BANK, "erp.hrss_bank");
        TablesMap.put(HRSS_GROCERY_SRV, "erp.hrss_grocery_srv");
        TablesMap.put(HRSS_BONUS, "erp.hrss_bonus");
        TablesMap.put(HRSS_TP_PREC, "erp.hrss_tp_prec");

        TablesMap.put(HRSU_CL_ABS, "erp.hrsu_cl_abs");
        TablesMap.put(HRSU_TP_ABS, "erp.hrsu_tp_abs");
        TablesMap.put(HRSU_TP_EMP_DIS, "erp.hrsu_tp_emp_dis");
        TablesMap.put(HRSU_TP_EMP, "erp.hrsu_tp_emp");
        TablesMap.put(HRSU_TP_WRK, "erp.hrsu_tp_wrk");
        TablesMap.put(HRSU_TP_MWZ, "erp.hrsu_tp_mwz");
        TablesMap.put(HRSU_TP_PAY_SHT_CUS, "erp.hrsu_tp_pay_sht_cus");
        TablesMap.put(HRSU_DEP, "erp.hrsu_dep");
        TablesMap.put(HRSU_POS, "erp.hrsu_pos");
        TablesMap.put(HRSU_SHT, "erp.hrsu_sht");
        TablesMap.put(HRSU_EMP, "erp.hrsu_emp");
        TablesMap.put(HRSU_EMP_REL, "erp.hrsu_emp_rel");
        TablesMap.put(HRSU_TP_EXP, "erp.hrsu_tp_exp");
        TablesMap.put(HRSU_PACK_EXP, "erp.hrsu_pack_exp");
        TablesMap.put(HRSU_PACK_EXP_ITEM, "erp.hrsu_pack_exp_item");

        TablesMap.put(HRS_SIE_PAY, "hrs_sie_pay");
        TablesMap.put(HRS_SIE_PAY_EMP, "hrs_sie_pay_emp");
        TablesMap.put(HRS_SIE_PAY_MOV, "hrs_sie_pay_mov");
        TablesMap.put(HRS_CFG, "hrs_cfg");
        TablesMap.put(HRS_FDY, "hrs_fdy");
        TablesMap.put(HRS_HOL, "hrs_hol");
        TablesMap.put(HRS_WDS, "hrs_wds");
        TablesMap.put(HRS_PRE_PAY_CUT_CAL, "hrs_pre_pay_cut_cal");
        TablesMap.put(HRS_TAX, "hrs_tax");
        TablesMap.put(HRS_TAX_ROW, "hrs_tax_row");
        TablesMap.put(HRS_TAX_SUB, "hrs_tax_sub");
        TablesMap.put(HRS_TAX_SUB_ROW, "hrs_tax_sub_row");
        TablesMap.put(HRS_EMPL_SUB, "hrs_empl_sub");
        TablesMap.put(HRS_SSC, "hrs_ssc");
        TablesMap.put(HRS_SSC_ROW, "hrs_ssc_row");
        TablesMap.put(HRS_EMPL_QUO, "hrs_empr_ssc");
        TablesMap.put(HRS_EMPL_QUO_ROW, "hrs_empr_ssc_row");
        TablesMap.put(HRS_BEN, "hrs_ben");
        TablesMap.put(HRS_BEN_ROW, "hrs_ben_row");
        TablesMap.put(HRS_BEN_ROW_AUX, "hrs_ben_row_aux");
        TablesMap.put(HRS_WRK_SAL, "hrs_wrk_sal");
        TablesMap.put(HRS_MWZ_WAGE, "hrs_mwz_wage");
        TablesMap.put(HRS_UMA, "hrs_uma");
        TablesMap.put(HRS_UMI, "hrs_umi");
        TablesMap.put(HRS_TP_LOAN_ADJ, "hrs_tp_loan_adj");
        TablesMap.put(HRS_DEP_CC, "hrs_dep_cc");
        TablesMap.put(HRS_EMP_MEMBER, "hrs_emp_member");
        TablesMap.put(HRS_EMP_LOG_HIRE, "hrs_emp_log_hire");
        TablesMap.put(HRS_EMP_LOG_WAGE, "hrs_emp_log_wage");
        TablesMap.put(HRS_EMP_LOG_SAL_SSC, "hrs_emp_log_sal_ssc");
        TablesMap.put(HRS_EMP_LOG_DEP_POS, "hrs_emp_log_pos");
        TablesMap.put(HRS_EMP_BEN, "hrs_emp_ben");
        TablesMap.put(HRS_EMP_BEN_ANN, "hrs_emp_ben_ann");
        TablesMap.put(HRS_EMP_WAGE_FAC_ANN, "hrs_emp_wage_fac_ann");
        TablesMap.put(HRS_LOAN, "hrs_loan");
        TablesMap.put(HRS_ABS, "hrs_abs");
        TablesMap.put(HRS_ABS_CNS, "hrs_abs_cns");
        TablesMap.put(HRS_EAR, "hrs_ear");
        TablesMap.put(HRS_DED, "hrs_ded");
        TablesMap.put(HRS_COND_EAR, "hrs_cond_ear");
        TablesMap.put(HRS_AUT_EAR, "hrs_aut_ear");
        TablesMap.put(HRS_AUT_DED, "hrs_aut_ded");
        TablesMap.put(HRS_ACC_EAR, "hrs_acc_ear");
        TablesMap.put(HRS_ACC_DED, "hrs_acc_ded");
        TablesMap.put(HRS_TP_EXP_ACC, "hrs_tp_exp_acc");
        TablesMap.put(HRS_PACK_CC, "hrs_pack_cc");
        TablesMap.put(HRS_PACK_CC_CC, "hrs_pack_cc_cc");
        TablesMap.put(HRS_CFG_ACC_DEP, "hrs_cfg_acc_dep");
        TablesMap.put(HRS_CFG_ACC_DEP_PACK_CC, "hrs_cfg_acc_dep_pack_cc");
        TablesMap.put(HRS_CFG_ACC_EMP_PACK_CC, "hrs_cfg_acc_emp_pack_cc");
        TablesMap.put(HRS_CFG_ACC_EMP_EAR, "hrs_cfg_acc_emp_ear");
        TablesMap.put(HRS_CFG_ACC_EMP_DED, "hrs_cfg_acc_emp_ded");
        TablesMap.put(HRS_CFG_ACC_EAR, "hrs_cfg_acc_ear");
        TablesMap.put(HRS_CFG_ACC_DED, "hrs_cfg_acc_ded");
        TablesMap.put(HRS_PAY, "hrs_pay");
        TablesMap.put(HRS_PAY_RCP, "hrs_pay_rcp");
        TablesMap.put(HRS_PAY_RCP_ISS, "hrs_pay_rcp_iss");
        TablesMap.put(HRS_PAY_RCP_DAY, "hrs_pay_rcp_day");
        TablesMap.put(HRS_PAY_RCP_EAR, "hrs_pay_rcp_ear");
        TablesMap.put(HRS_PAY_RCP_EAR_CMP, "hrs_pay_rcp_ear_cmp");
        TablesMap.put(HRS_PAY_RCP_DED, "hrs_pay_rcp_ded");
        TablesMap.put(HRS_PAY_RCP_DED_CMP, "hrs_pay_rcp_ded_cmp");
        TablesMap.put(HRS_ACC_PAY, "hrs_acc_pay");
        TablesMap.put(HRS_ACC_PAY_RCP, "hrs_acc_pay_rcp");
        TablesMap.put(HRS_ADV_SET, "hrs_adv_set");
        TablesMap.put(HRS_PREC, "hrs_prec");
        TablesMap.put(HRS_PREC_SEC, "hrs_prec_sec");
        TablesMap.put(HRS_PREC_SUBSEC, "hrs_prec_subsec");
        TablesMap.put(HRS_DOC_BREACH, "hrs_doc_breach");
        TablesMap.put(HRS_DOC_BREACH_PREC_SUBSEC, "hrs_doc_breach_prec_subsec");
        TablesMap.put(HRS_DOC_ADM_REC, "hrs_doc_adm_rec");
        TablesMap.put(HRS_DOC_ADM_REC_PREC_SUBSEC, "hrs_doc_adm_rec_prec_subsec");
        
        TablesMap.put(QLT_LOT_APR, "qlt_lot_apr");
        TablesMap.put(QLT_TP_ANALYSIS, "qlt_tp_analysis");
        TablesMap.put(QLT_ANALYSIS, "qlt_analysis");
        TablesMap.put(QLT_ANALYSIS_ITEM, "qlt_analysis_item");
        TablesMap.put(QLT_DATASHEET_TEMPLATE, "qlt_datasheet_template");
        TablesMap.put(QLT_DATASHEET_TEMPLATE_ROW, "qlt_datasheet_template_row");
        TablesMap.put(QLT_DATASHEET_TEMPLATE_LINK, "qlt_datasheet_template_link");
        TablesMap.put(QLT_QLTY_CONFIG_REQUIRED, "qlt_qlty_config_required");
        TablesMap.put(QLT_COA_RESULT, "qlt_coa_result");
        TablesMap.put(QLT_COA_RESULT_ROW, "qlt_coa_result_row");

    }
}
