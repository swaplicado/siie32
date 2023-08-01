/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.data;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores, Isabel Servín, Adrián Avilés, Edwin Carmona, Claudio Peña, Sergio Flores
 * 
 */
public abstract class SDataConstantsSys {

    public static final int UNDEFINED = 0;
    public static final int NA = 1;
    
    public static final int CFGS_CT_ENT_CASH = 1;
    public static final int CFGS_CT_ENT_WH = 2;
    public static final int CFGS_CT_ENT_POS = 3;
    public static final int CFGS_CT_ENT_PLANT = 4;

    public static final int[] CFGS_TP_ENT_CASH_CASH = new int[] { 1, 1 };
    public static final int[] CFGS_TP_ENT_CASH_BANK = new int[] { 1, 2 };
    public static final int[] CFGS_TP_ENT_WH_SP = new int[] { 2, 11};
    public static final int[] CFGS_TP_ENT_WH_MFG_RM = new int[] { 2, 21 };
    public static final int[] CFGS_TP_ENT_WH_MFG_WP = new int[] { 2, 22 };
    public static final int[] CFGS_TP_ENT_WH_MFG_FG = new int[] { 2, 23 };
    public static final int[] CFGS_TP_ENT_WH_MFG_MS = new int[] { 2, 24 };
    public static final int[] CFGS_TP_ENT_WH_GDS = new int[] { 2, 31 };
    public static final int[] CFGS_TP_ENT_WH_WAR_PUR = new int[] { 2, 41 };
    public static final int[] CFGS_TP_ENT_WH_WAR_SAL = new int[] { 2, 42 };
    public static final int[] CFGS_TP_ENT_WH_CSG_PUR = new int[] { 2, 51 };
    public static final int[] CFGS_TP_ENT_WH_CSG_SAL = new int[] { 2, 52 };
    public static final int[] CFGS_TP_ENT_POS_POS = new int[] { 3, 1 };
    public static final int[] CFGS_TP_ENT_PLT_PLT = new int[] { 4, 1 };

    public static final int CFGS_TP_SORT_KEY_NAME = 1;
    public static final int CFGS_TP_SORT_NAME_KEY = 2;
    public static final int CFGS_TP_SORT_KEY_NAME_COMM = 3;
    public static final int CFGS_TP_SORT_NAME_COMM_KEY = 4;
    public static final int CFGS_TP_SORT_DOC_BIZ_P = 5;
    public static final int CFGS_TP_SORT_BIZ_P_DOC = 6;

    public static final int CFGS_TP_BAL_DEBIT_CREDIT = 1;
    public static final int CFGS_TP_BAL_CREDIT_DEBIT = 2;

    public static final int CFGS_TP_REL_0_TO_0 = 1;
    public static final int CFGS_TP_REL_1_TO_1 = 2;
    public static final int CFGS_TP_REL_1_TO_N = 3;
    public static final int CFGS_TP_REL_N_TO_1 = 4;
    public static final int CFGS_TP_REL_N_TO_M = 5;

    public static final int CFGS_TP_FMT_D_YYYY_MM_DD = 1;
    public static final int CFGS_TP_FMT_D_DD_MM_YYYY = 2;
    public static final int CFGS_TP_FMT_D_MM_DD_YYYY = 3;

    public static final int CFGS_TP_FMT_DT_YYYY_MM_DD_HH_MM = 1;
    public static final int CFGS_TP_FMT_DT_YYYY_MM_DD_HH_MM_AP = 2;
    public static final int CFGS_TP_FMT_DT_YYYY_MM_DD_HH_MM_SS = 3;
    public static final int CFGS_TP_FMT_DT_YYYY_MM_DD_HH_MM_SS_AP = 4;
    public static final int CFGS_TP_FMT_DT_DD_MM_YYYY_HH_MM = 5;
    public static final int CFGS_TP_FMT_DT_DD_MM_YYYY_HH_MM_AP = 6;
    public static final int CFGS_TP_FMT_DT_DD_MM_YYYY_HH_MM_SS = 7;
    public static final int CFGS_TP_FMT_DT_DD_MM_YYYY_HH_MM_SS_AP = 8;
    public static final int CFGS_TP_FMT_DT_MM_DD_YYYY_HH_MM = 9;
    public static final int CFGS_TP_FMT_DT_MM_DD_YYYY_HH_MM_AP = 10;
    public static final int CFGS_TP_FMT_DT_MM_DD_YYYY_HH_MM_SS = 11;
    public static final int CFGS_TP_FMT_DT_MM_DD_YYYY_HH_MM_SS_AP = 12;

    public static final int CFGS_TP_FMT_T_HH_MM = 1;
    public static final int CFGS_TP_FMT_T_HH_MM_AP = 2;
    public static final int CFGS_TP_FMT_T_HH_MM_SS = 3;
    public static final int CFGS_TP_FMT_T_HH_MM_SS_AP = 4;

    public static final int CFGS_TP_DBMS_MY_SQL = 1;
    public static final int CFGS_TP_DBMS_SQL_SERVER_2000 = 2;
    public static final int CFGS_TP_DBMS_SQL_SERVER_2005 = 3;

    public static final int CFGS_TAX_MODEL_DPS = 1;
    public static final int CFGS_TAX_MODEL_DPS_EXC = 2;

    public static final int CFGS_LOT_MODEL_CONS = 1;
    public static final int CFGS_LOT_MODEL_CONS_DATE = 2;

    public static final String CFG_PARAM_DIOT_ACCOUNTS = "DIOT_ACCOUNTS";
    public static final String CFG_PARAM_DIOT_VAT_DEFAULT = "DIOT_VAT_DEFAULT";
    public static final String CFG_PARAM_HRS_EMPLOYEES_CRUD = "HRS_EMPLOYEES_CRUD";
    public static final String CFG_PARAM_HRS_SIBLING_COMPANIES = "HRS_SIBLING_COMPANIES";
    public static final String CFG_PARAM_HRS_CAP = "HRS_CAP";
    public static final String CFG_PARAM_HRS_AF02 = "HRS_AF02";
    public static final String CFG_PARAM_HRS_VARIABLE_EARNINGS = "HRS_VARIABLE_EARNINGS";
    public static final String CFG_PARAM_CFD_TYPES = "CFD_TYPES";
    public static final String CFG_PARAM_CFD_ORG_NAMES = "CFD_ORG_NAMES";
    public static final String CFG_PARAM_FIN_USD_XRT_POLICY = "FIN_USD_XRT_POLICY";
    public static final String CFG_PARAM_FIN_BANK_LAYOUT_CFD_REQ = "FIN_BANK_LAYOUT_CFD_REQ";
    public static final String CFG_PARAM_FIN_AP_PAY_CFD_REQ = "FIN_AP_PAY_CFD_REQ";
    public static final String CFG_PARAM_TRN_MAT_REQ_PTY_DEFAULT = "TRN_MAT_REQ_PTY_DEFAULT";
    public static final String CFG_PARAM_TRN_MAT_REQ_ETY_ITEM_NEW = "TRN_MAT_REQ_ETY_ITEM_NEW";
    public static final String CFG_PARAM_SIIE_WEB_DB = "SIIE_WEB_DB";
    
    public static final int CFG_PARAM_CFD_ORG_NAMES_ALL_FULL_NAME = 11;
    public static final int CFG_PARAM_CFD_ORG_NAMES_ALL_FISCAL_NAME = 12;
    public static final int CFG_PARAM_CFD_ORG_NAMES_RECEPTOR_CHOICE = 13;
    public static final String CFG_PARAM_QLT_DPS_ANALYSIS_TO = "QLT_DPS_ANALYSIS_TO";
    public static final String CFG_PARAM_QLT_DPS_ANALYSIS_CC = "QLT_DPS_ANALYSIS_CC";
    
    public static final int USD_XRT_POLICY_BANXICO = 1;
    public static final int USD_XRT_POLICY_INFORMAL = 2;
    
    public static final int CFGX_IVM_FIFO = 1;
    
    
    /* XXX 2019-08-16 Sergio Flores: Not used yet.
    public static final int USRS_TP_LEV_NO_PERMISSION = 0;
    public static final int USRS_TP_LEV_READ = 1;
    public static final int USRS_TP_LEV_CAPTURE = 2;
    public static final int USRS_TP_LEV_AUTHOR = 3;
    public static final int USRS_TP_LEV_EDITOR = 4;
    public static final int USRS_TP_LEV_MANAGER = 5;
    */
    
    public static final int USRX_USER_NA = 1;
    public static final int USRX_USER_SUPER = 2;

    public static final int USRX_PREF_SESSION = 1;
    public static final int USRX_PREF_VIEW = 2;
    public static final int USRX_PREF_FORM = 3;
    public static final int USRX_PREF_REPORT = 4;

    public static final int PRV_SPE = 300000;

    public static final int PRV_CAT_CFG_LAN = 201001;
    public static final int PRV_CAT_CFG_CUR = 201002;
    public static final int PRV_CAT_CFG_CO = 201003;
    public static final int PRV_CAT_USR = 202001;
    public static final int PRV_CAT_LOC = 203001;
    public static final int PRV_CAT_BPS_BA = 204001;
    public static final int PRV_CAT_BPS_TP_BP = 204002;
    public static final int PRV_CAT_BPS_BP = 204003;
    public static final int PRV_CAT_BPS_BP_SUP = 204004;
    public static final int PRV_CAT_BPS_BP_CUS = 204005;
    public static final int PRV_CAT_BPS_BP_CDR = 204006;
    public static final int PRV_CAT_BPS_BP_DBR = 204007;
    public static final int PRV_CAT_BPS_BP_EMP = 204008;
    public static final int PRV_CAT_BPS_BPB = 204009;
    public static final int PRV_CAT_BPS_BPB_SUP = 204010;
    public static final int PRV_CAT_BPS_BPB_CUS = 204011;
    public static final int PRV_CAT_BPS_BPB_ADD = 204012;
    public static final int PRV_CAT_BPS_BPB_ADD_SUP = 204013;
    public static final int PRV_CAT_BPS_BPB_ADD_CUS = 204014;
    public static final int PRV_CAT_BPS_BPB_CON = 204015;
    public static final int PRV_CAT_BPS_BPB_CON_SUP = 204016;
    public static final int PRV_CAT_BPS_BPB_CON_CUS = 204017;
    public static final int PRV_CAT_BPS_BK_ACC = 204018;
    public static final int PRV_CAT_BPS_BK_ACC_SUP = 204019;
    public static final int PRV_CAT_BPS_BK_ACC_CUS = 204020;
    public static final int PRV_CAT_BPS_BK_ACC_CDR = 204021;
    public static final int PRV_CAT_BPS_BK_ACC_DBR = 204022;
    public static final int PRV_CAT_BPS_BK_ACC_EMP = 204023;
    public static final int PRV_CAT_ITM_UNIT = 205001;
    public static final int PRV_CAT_ITM_ITEM_VAR = 205002;
    public static final int PRV_CAT_ITM_ITEM_CFG = 205003;
    public static final int PRV_CAT_ITM_ITEM = 205004;
    public static final int PRV_CAT_ITM_ITEM_CT_ASSET = 205005;
    public static final int PRV_CAT_ITM_ITEM_CT_EXPEN = 205006;
    public static final int PRV_CAT_ITM_ITEM_CT_SPARE = 205007;
    public static final int PRV_CAT_ITM_ITEM_CT_CONSUM = 205008;
    public static final int PRV_CAT_ITM_ITEM_CT_GDS_PROC = 205009;
    public static final int PRV_CAT_ITM_ITEM_CT_GDS_FIN = 205010;
    public static final int PRV_CAT_ITM_ITEM_CT_GDS = 205011;
    public static final int PRV_CAT_ITM_ITEM_BP = 205012;
    public static final int PRV_CAT_ITM_ITEM_BP_SUP = 205013;
    public static final int PRV_CAT_ITM_ITEM_BP_CUS = 205014;
    public static final int PRV_CAT_FIN_ACC_CFG = 206001;
    public static final int PRV_CAT_FIN_ACC_TAX = 206002;
    public static final int PRV_CAT_FIN_ACC_MISC = 206003;
    public static final int PRV_CAT_TRN_DOC_PRT = 207001;
    public static final int PRV_CAT_TRN_DOC_TP = 207002;
    public static final int PRV_CAT_MKT_PRICE_SRP = 208001;
    public static final int PRV_CAT_MKT_PRICE_PUB = 208002;
    public static final int PRV_CAT_LOG = 209001;
    public static final int PRV_CAT_MFG = 210001;
    public static final int PRV_CAT_HRS = 211001;
    public static final int PRV_CAT_QLT = 212001;

    public static final int PRV_CFG_ERP = 101001;
    public static final int PRV_CFG_CO = 101002;

    public static final int PRV_FIN_YEAR = 102001;
    public static final int PRV_FIN_PER = 102002;
    public static final int PRV_FIN_EXC_RATE = 102003;
    public static final int PRV_FIN_ACC = 102004;
    public static final int PRV_FIN_ACC_CASH = 102005;
    public static final int PRV_FIN_CC = 102006;
    public static final int PRV_FIN_BKC = 102007;
    public static final int PRV_FIN_CHECK = 102008;
    public static final int PRV_FIN_TAX_GRP = 102009;
    public static final int PRV_FIN_REC = 102010;
    public static final int PRV_FIN_ACC_BP = 102011;
    public static final int PRV_FIN_ACC_ITEM = 102012;
    public static final int PRV_FIN_DPS_DNS = 102013;
    public static final int PRV_FIN_DPS_DNC = 102014;
    //public static final int PRV_FIN_DIOM_DNS = 102015;
    //public static final int PRV_FIN_DIOM_DNC = 102016;
    //public static final int PRV_FIN_DIOM_OUT = 102017;
    //public static final int PRV_FIN_DIOM_IN = 102018;
    public static final int PRV_FIN_MOV_ACC_CASH = 102019;
    public static final int PRV_FIN_MOV_CDR = 102020;
    public static final int PRV_FIN_MOV_DBR = 102021;
    public static final int PRV_FIN_COUNTER_RCPT = 102022;
    public static final int PRV_FIN_CFD_PAYMENT = 102051;
    public static final int PRV_FIN_REP = 102901;
    public static final int PRV_FIN_REP_STATS = 102902;
    public static final int PRV_FIN_REP_INDEX = 102903;

    public static final int PRV_PUR_DPS_DNS = 103001;
    public static final int PRV_PUR_DPS_DNC = 103002;
    public static final int PRV_PUR_DPS_PRT_FMT = 103003;
    //public static final int PRV_PUR_DIOM_DNS = 103004;
    //public static final int PRV_PUR_DIOM_DNC = 103005;
    public static final int PRV_PUR_DOC_EST = 103006;
    public static final int PRV_PUR_DOC_EST_AUTHORN = 103071;
    public static final int PRV_PUR_DOC_ORD = 103007;
    public static final int PRV_PUR_DOC_ORD_AUTHORN = 103008;
    public static final int PRV_PUR_DOC_TRN = 103009;
    public static final int PRV_PUR_DOC_TRN_ADJ = 103010;
    public static final int PRV_PUR_DIOG_OUT = 103011;
    public static final int PRV_PUR_DIOG_IN = 103012;
    public static final int PRV_PUR_CRED = 103013;
    public static final int PRV_PUR_ACC_PEND = 103014;
    public static final int PRV_PUR_BP_BLOCK = 103015;
    public static final int PRV_PUR_BP_STATS = 103016;
    public static final int PRV_PUR_BP_CUR = 103017;
    public static final int PRV_PUR_EXC_RATE = 103018;
    public static final int PRV_PUR_ANNUL_DAY = 103019;
    public static final int PRV_PUR_ANNUL = 103020;
    public static final int PRV_PUR_SUST_DOC = 103021;
    public static final int PRV_PUR_DOC_ORD_REJECT_OWN = 103022;
    public static final int PRV_PUR_DOC_ORD_DELAY = 103023;
    public static final int PRV_PUR_DOC_OMT_DOC_SRC = 103024;
    public static final int PRV_PUR_DOC_ORD_ALL_DNS = 103025;
    public static final int PRV_PUR_PRICE_CHG = 103031;
    public static final int PRV_PUR_CRED_CONFIG = 103041;
    public static final int PRV_PUR_ITEM_SUP = 103051;
    public static final int PRV_PUR_DOC_EST_CLO = 103056;
    public static final int PRV_PUR_DOC_ORD_CLO = 103057;
    public static final int PRV_PUR_REP = 103901;
    
    public static final int PRV_SAL_DPS_DNS = 104001;
    public static final int PRV_SAL_DPS_DNC = 104002;
    public static final int PRV_SAL_DPS_PRT_FMT = 104003;
    //public static final int PRV_SAL_DIOM_DNS = 104004;
    //public static final int PRV_SAL_DIOM_DNC = 104005;
    public static final int PRV_SAL_DOC_EST = 104006;
    public static final int PRV_SAL_DOC_EST_AUTHORN = 104071;
    public static final int PRV_SAL_DOC_ORD = 104007;
    public static final int PRV_SAL_DOC_ORD_AUTHORN = 104008;
    public static final int PRV_SAL_DOC_TRN = 104009;
    public static final int PRV_SAL_DOC_TRN_ADJ = 104010;
    public static final int PRV_SAL_DIOG_OUT = 104011;
    public static final int PRV_SAL_DIOG_IN = 104012;
    public static final int PRV_SAL_CRED = 104013;
    public static final int PRV_SAL_ACC_PEND = 104014;
    public static final int PRV_SAL_BP_BLOCK = 104015;
    public static final int PRV_SAL_BP_STATS = 104016;
    public static final int PRV_SAL_BP_CUR = 104017;
    public static final int PRV_SAL_EXC_RATE = 104018;
    public static final int PRV_SAL_ANNUL_DAY = 104019;
    public static final int PRV_SAL_ANNUL = 104020;
    public static final int PRV_SAL_SUST_DOC = 104021;
    public static final int PRV_SAL_DOC_ORD_REJECT_OWN = 104022;
    public static final int PRV_SAL_DOC_ORD_DELAY = 104023;
    public static final int PRV_SAL_DOC_OMT_DOC_SRC = 104024;
    public static final int PRV_SAL_DOC_ORD_ALL_DNS = 104025;
    public static final int PRV_SAL_PRICE_CHG = 104031;
    public static final int PRV_SAL_CRED_CONFIG = 104041;
    public static final int PRV_SAL_DOC_EST_CLO = 104056;
    public static final int PRV_SAL_DOC_ORD_CLO = 104057;
    public static final int PRV_SAL_COMP_MON_EXT = 104061;
    public static final int PRV_SAL_COMP_SIGN_RESTRICT = 104062;
    public static final int PRV_SAL_COMP_SIGN_IMMEX = 104063;
    public static final int PRV_SAL_REP = 104901;

    public static final int PRV_INV_DIOG_CFG = 105001;
    public static final int PRV_INV_DNC_DIOG = 105002;
    public static final int PRV_INV_ITEM_WH = 105003;
    public static final int PRV_INV_IN_PUR = 105004;
    public static final int PRV_INV_IN_SAL = 105005;
    public static final int PRV_INV_IN_ADJ = 105006;
    public static final int PRV_INV_IN_MFG = 105007;
    public static final int PRV_INV_IN_INT = 105008;
    public static final int PRV_INV_IN_EXT = 105009;
    public static final int PRV_INV_OUT_PUR = 105010;
    public static final int PRV_INV_OUT_SAL = 105011;
    public static final int PRV_INV_OUT_ADJ = 105012;
    public static final int PRV_INV_OUT_MFG = 105013;
    public static final int PRV_INV_OUT_INT = 105014;
    public static final int PRV_INV_OUT_EXT = 105015;
    public static final int PRV_INV_WH_SEC = 105016;
    public static final int PRV_INV_STOCK = 105017;
    public static final int PRV_INV_AUDIT = 105018;
    public static final int PRV_INV_MFG_RM_ASG = 105051;
    public static final int PRV_INV_MFG_RM_DEV = 105052;
    public static final int PRV_INV_MFG_WP_ASG = 105053;
    public static final int PRV_INV_MFG_WP_DEV = 105054;
    public static final int PRV_INV_MFG_FG_ASG = 105055;
    public static final int PRV_INV_MFG_FG_DEV = 105056;
    public static final int PRV_INV_MFG_CON = 105057;
    public static final int PRV_INV_MAINT = 105058;
    public static final int PRV_INV_REQ_MAT_REQ = 105061;
    public static final int PRV_INV_REQ_MAT_REV = 105062;
    public static final int PRV_INV_REQ_MAT_PROV = 105063;
    public static final int PRV_INV_REQ_MAT_PUR = 105064;
    public static final int PRV_INV_REQ_MAT_ADMOR = 105065;
    public static final int PRV_INV_REP = 105901;

    public static final int PRV_MKT_CUS_CFG = 106001;
    public static final int PRV_MKT_PRICE_PUB = 106002;
    public static final int PRV_MKT_PLIST_GRP = 106003;
    public static final int PRV_MKT_COMMS = 106004;
    public static final int PRV_MKT_COMMS_PAY = 106005;
    public static final int PRV_MKT_MISC = 106006;
    public static final int PRV_MKT_SAL_SAL_AGT = 106007;
    public static final int PRV_MKT_SAL_PRICE_PLIST = 106008;
    public static final int PRV_MKT_SAL_PRICE = 106009;
    public static final int PRV_MKT_SAL_COMMS = 106010;
    public static final int PRV_MKT_SAL_DISC_DOC = 106011;
    public static final int PRV_MKT_PLIST_SAL = 106012;
    public static final int PRV_MKT_PLIST_PUR = 106013;
    public static final int PRV_MKT_REP = 106901;

    public static final int PRV_LOG_MISC = 107001;
    public static final int PRV_LOG_RATE = 107002;
    public static final int PRV_LOG_REP = 107901;

    public static final int PRV_MFG_MISC = 108001;
    public static final int PRV_MFG_BOM = 108002;
    public static final int PRV_MFG_ORD_NEW = 108003;
    public static final int PRV_MFG_ORD_ST_NEW = 108004;
    public static final int PRV_MFG_ORD_ST_LOT = 108005;
    public static final int PRV_MFG_ORD_ST_LOT_ASG = 108006;
    public static final int PRV_MFG_ORD_ST_PROC = 108007;
    public static final int PRV_MFG_ORD_ST_END = 108008;
    public static final int PRV_MFG_ORD_ST_CLS = 108009;
    public static final int PRV_MFG_ORD_REOPEN = 108021;
    public static final int PRV_MFG_ASG_LOT = 108010;
    public static final int PRV_MFG_EXP = 108011;
    public static final int PRV_MFG_LT = 108012;
    public static final int PRV_MFG_REQ = 108013;
    public static final int PRV_MFG_ASG_REW = 108014;
    public static final int PRV_MFG_ASG_DATE = 108015;
    public static final int PRV_MFG_REP = 108901;

    public static final int PRV_HRS_IMP = 109001;
    public static final int PRV_HRS_CFG = 109002;
    public static final int PRV_HRS_CAT = 109003;
    public static final int PRV_HRS_CAT_EMP = 109004;
    public static final int PRV_HRS_CAT_EMP_WAGE = 109005;
    public static final int PRV_HRS_PAY = 109006;
    public static final int PRV_HRS_PAY_OC = 109007;
    public static final int PRV_HRS_PAY_WEE = 109011;
    public static final int PRV_HRS_PAY_FOR = 109012;
    public static final int PRV_HRS_ABS = 109016;
    public static final int PRV_HRS_AUX_HRS = 109021;
    public static final int PRV_HRS_EMP_PERS_DATA = 109091;
    public static final int PRV_HRS_EMP_VARIABLE_EARNINGS = 109092;
    public static final int PRV_HRS_REP = 109901;

    public static final int PRV_QLT_LOT_APR = 110001;
    public static final int PRV_QLT_REP = 110901;

    public static final int ROL_CAT_CFG = 201000;
    public static final int ROL_CAT_USR = 202000;
    public static final int ROL_CAT_LOC = 203000;
    public static final int ROL_CAT_BPS = 204000;
    public static final int ROL_CAT_ITM = 205000;
    public static final int ROL_CAT_FIN = 206000;
    public static final int ROL_CAT_TRN = 207000;
    public static final int ROL_CAT_MKT = 208000;
    public static final int ROL_CAT_LOG = 209000;
    public static final int ROL_CAT_MFG = 210000;
    public static final int ROL_CAT_HRS = 211000;
    public static final int ROL_CAT_QLT = 212000;

    public static final int ROL_CFG_ADMOR = 101001;

    public static final int ROL_FIN_ADMOR = 102001;
    public static final int ROL_FIN_ACCT = 102002;
    public static final int ROL_FIN_ACCT_AUX = 102003;
    public static final int ROL_FIN_ACCT_CASH = 102004;

    public static final int ROL_PUR_ADMOR = 103001;
    public static final int ROL_PUR_EST = 103002;
    public static final int ROL_PUR_EST_SUPR = 103011;
    public static final int ROL_PUR_ORD = 103003;
    public static final int ROL_PUR_ORD_SUPR = 103004;
    public static final int ROL_PUR_TRN = 103005;
    public static final int ROL_PUR_TRN_ADJ = 103006;
    public static final int ROL_PUR_TRN_SUPR = 103007;
    public static final int ROL_PUR_CRED = 103008;
    public static final int ROL_PUR_PAY = 103009;

    public static final int ROL_SAL_ADMOR = 104001;
    public static final int ROL_SAL_EST = 104002;
    public static final int ROL_SAL_EST_SUPR = 104011;
    public static final int ROL_SAL_ORD = 104003;
    public static final int ROL_SAL_ORD_SUPR = 104004;
    public static final int ROL_SAL_TRN = 104005;
    public static final int ROL_SAL_TRN_ADJ = 104006;
    public static final int ROL_SAL_TRN_SUPR = 104007;
    public static final int ROL_SAL_CRED = 104008;
    public static final int ROL_SAL_PAY = 104009;

    public static final int ROL_INV_ADMOR = 105001;
    public static final int ROL_INV_CLK = 105002;
    public static final int ROL_INV_CLK_PUR = 105003;
    public static final int ROL_INV_CLK_SAL = 105004;
    public static final int ROL_INV_CLK_MFG = 105005;
    public static final int ROL_INV_CLK_OMI = 105006;
    public static final int ROL_INV_CLK_OME = 105007;

    public static final int ROL_MKT_ADMOR = 106001;
    public static final int ROL_MKT_CUS_CFG = 106002;
    public static final int ROL_MKT_PRICE_PUB = 106003;
    public static final int ROL_MKT_PLIST = 106004;
    public static final int ROL_MKT_COMMS = 106005;
    public static final int ROL_MKT_COMMS_PAY = 106006;

    public static final int ROL_LOG_ADMOR = 107001;

    public static final int ROL_MFG_ADMOR = 108001;
    public static final int ROL_MFG_ADMOR_BOM = 108002;
    public static final int ROL_MFG_AUX = 108006;

    public static final int ROL_HRS_ADMOR = 109001;

    public static final int ROL_QLT_ADMOR = 110001;
    
    public static final int ROL_SPE_SUPER = 300001;
    public static final int ROL_SPE_CONFIG = 300002;
    public static final int ROL_SPE_ADMOR = 300003;

    public static final int BPSS_CT_BP_CO = 1;
    public static final int BPSS_CT_BP_SUP = 2;
    public static final int BPSS_CT_BP_CUS = 3;
    public static final int BPSS_CT_BP_CDR = 4;
    public static final int BPSS_CT_BP_DBR = 5;

    public static final int BPSU_TP_BP_DEFAULT = 1;

    public static final int BPSS_TP_BP_IDY_PER = 1;
    public static final int BPSS_TP_BP_IDY_ORG = 2;

    public static final int BPSS_TP_BP_ATT_BANK = 1;
    public static final int BPSS_TP_BP_ATT_CARR = 2;

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
    public static final int BPSS_TP_CON_OTH = 6;

    public static final int BPSS_TP_TEL_NA = 1;
    public static final int BPSS_TP_TEL_TEL = 2;
    public static final int BPSS_TP_TEL_TEL_FAX = 3;
    public static final int BPSS_TP_TEL_FAX = 4;
    public static final int BPSS_TP_TEL_MOV = 5;
    public static final int BPSS_TP_TEL_OTH = 6;

    public static final int BPSS_TP_CRED_CRED_NO = 1;
    public static final int BPSS_TP_CRED_CRED_LIM = 2;
    public static final int BPSS_TP_CRED_CRED_UNLIM = 3;

    public static final int BPSS_TP_CFD_ADD_NA = 1;
    public static final int BPSS_TP_CFD_ADD_SORIANA = 2;
    public static final int BPSS_TP_CFD_ADD_LOREAL = 3;
    public static final int BPSS_TP_CFD_ADD_BACHOCO = 4;
    public static final int BPSS_TP_CFD_ADD_MODELO = 5;
    public static final int BPSS_TP_CFD_ADD_ELEKTRA = 6;
    public static final int BPSS_TP_CFD_ADD_WALDOS = 7;
    public static final int BPSS_TP_CFD_ADD_AMECE71 = 11;
    
    public static final int BPSS_BPB_BANK_ACC_TEL = 10;
    public static final int BPSS_BPB_BANK_ACC_NUM = 11;
    public static final int BPSS_BPB_BANK_ACC_TRJ = 16;
    public static final int BPSS_BPB_BANK_ACC_CBE = 18;

    public static final int BPSU_BA_DEFAULT = 1;

    public static final int BPSX_CT_BP_QTY = 5;

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
    public static final int[] ITMS_TP_ITEM_PUR_CON_RMD = new int[] { 3, 1, 1 }; //raw materials (direct)
    public static final int[] ITMS_TP_ITEM_PUR_CON_RMP = new int[] { 3, 1, 2 }; //raw materials packagin
    public static final int[] ITMS_TP_ITEM_PUR_CON_RMI = new int[] { 3, 1, 3 }; //raw materials indirect
    public static final int[] ITMS_TP_ITEM_PUR_EXP_EXP = new int[] { 3, 2, 1 };
    public static final int[] ITMS_TP_ITEM_EXP_MFG_RM = new int[] { 4, 1, 1 };
    public static final int[] ITMS_TP_ITEM_EXP_MFG_LF = new int[] { 4, 1, 2 };
    public static final int[] ITMS_TP_ITEM_EXP_MFG_IE = new int[] { 4, 1, 3 };
    public static final int[] ITMS_TP_ITEM_EXP_OPE_RM = new int[] { 4, 2, 1 };
    public static final int[] ITMS_TP_ITEM_EXP_OPE_LF = new int[] { 4, 2, 2 };
    public static final int[] ITMS_TP_ITEM_EXP_OPE_IE = new int[] { 4, 2, 3 };

    public static final int ITMS_TP_SNR_NA = 1;
    public static final int ITMS_TP_SNR_SIM = 2;
    public static final int ITMS_TP_SNR_COM = 3;

    public static final int ITMU_TP_UNIT_NA = 1;
    public static final int ITMU_TP_UNIT_QTY = 2;
    public static final int ITMU_TP_UNIT_LEN = 3;
    public static final int ITMU_TP_UNIT_SURF = 4;
    public static final int ITMU_TP_UNIT_VOL = 5;
    public static final int ITMU_TP_UNIT_MASS = 6;
    public static final int ITMU_TP_UNIT_TIME = 7;

    public static final int ITMU_TP_LEV_NA = 1;

    public static final int ITMU_UNIT_NA = 1;
    public static final int ITMU_KG = 59;

    public static final int ITMU_TP_VAR_NA = 1;

    public static final int ITMU_VAR_NA = 1;
    public static final int ITMU_BRD_NA = 1;
    public static final int ITMU_MFR_NA = 1;
    public static final int ITMU_EMT_NA = 1;

    public static final int ITMS_NAM_ORD_POS_QTY = 5;
    public static final int ITMS_NAM_LINE_POS_QTY = 4;
    public static final int ITMS_KEY_ORD_POS_QTY = 4;
    public static final int ITMS_KEY_LINE_POS_QTY = 4;

    public static final int ITMS_DEF_ITEM_KEY = 1;
    public static final int ITMS_DEF_ITEM = 2;
    public static final int ITMS_DEF_ITEM_SHORT = 3;

    public static final int FINS_TP_BKR_ALL = 1;
    public static final int FINS_TP_BKR_DBT = 2;
    public static final int FINS_TP_BKR_CDT = 3;

    public static final int FINS_TP_ACC_MOV_NA = 1;
    public static final int FINS_TP_ACC_MOV_FY_OPEN = 2;
    public static final int FINS_TP_ACC_MOV_FY_CLOSE = 3;
    public static final int FINS_TP_ACC_MOV_PUR = 4;
    public static final int FINS_TP_ACC_MOV_SAL = 5;
    public static final int FINS_TP_ACC_MOV_GDS_IN = 6;
    public static final int FINS_TP_ACC_MOV_GDS_OUT = 7;
    public static final int FINS_TP_ACC_MOV_CASH_IN = 8;
    public static final int FINS_TP_ACC_MOV_CASH_OUT = 9;
    public static final int FINS_TP_ACC_MOV_EXC_FLUC_PROF = 10;
    public static final int FINS_TP_ACC_MOV_EXC_FLUC_LOSS = 11;
    public static final int FINS_TP_ACC_MOV_DEPREC = 12;
    public static final int FINS_TP_ACC_MOV_AMORT = 13;
    public static final int FINS_TP_ACC_MOV_COGM = 14;
    public static final int FINS_TP_ACC_MOV_COGS = 15;
    public static final int FINS_TP_ACC_MOV_SUBSYS = 16;
    public static final int FINS_TP_ACC_MOV_TAX = 17;
    public static final int FINS_TP_ACC_MOV_JOURNAL = 99;

    public static final int[] FINS_CL_ACC_MOV_NA = { 1, 1 };
    public static final int[] FINS_CL_ACC_MOV_FY_OPEN = { 2, 1 };
    public static final int[] FINS_CL_ACC_MOV_FY_CLOSE = { 3, 1 };
    public static final int[] FINS_CL_ACC_MOV_PUR = { 4, 1 };
    public static final int[] FINS_CL_ACC_MOV_PUR_ADJ = { 4, 2 };
    public static final int[] FINS_CL_ACC_MOV_SAL = { 5, 1 };
    public static final int[] FINS_CL_ACC_MOV_SAL_ADJ = { 5, 2 };
    public static final int[] FINS_CL_ACC_MOV_GDS_IN_PUR = { 6, 1 };
    public static final int[] FINS_CL_ACC_MOV_GDS_IN_SAL = { 6, 2 };
    public static final int[] FINS_CL_ACC_MOV_GDS_IN_ADJ = { 6, 3 };
    public static final int[] FINS_CL_ACC_MOV_GDS_IN_EXT = { 6, 4 };
    public static final int[] FINS_CL_ACC_MOV_GDS_IN_INT = { 6, 5 };
    public static final int[] FINS_CL_ACC_MOV_GDS_IN_MFG = { 6, 6 };
    public static final int[] FINS_CL_ACC_MOV_GDS_OUT_PUR = { 7, 1 };
    public static final int[] FINS_CL_ACC_MOV_GDS_OUT_SAL = { 7, 2 };
    public static final int[] FINS_CL_ACC_MOV_GDS_OUT_ADJ = { 7, 3 };
    public static final int[] FINS_CL_ACC_MOV_GDS_OUT_EXT = { 7, 4 };
    public static final int[] FINS_CL_ACC_MOV_GDS_OUT_INT = { 7, 5 };
    public static final int[] FINS_CL_ACC_MOV_GDS_OUT_MFG = { 7, 6 };
    public static final int[] FINS_CL_ACC_MOV_CASH_IN_PUR = { 8, 1 };
    public static final int[] FINS_CL_ACC_MOV_CASH_IN_SAL = { 8, 2 };
    public static final int[] FINS_CL_ACC_MOV_CASH_IN_ADJ = { 8, 3 };
    public static final int[] FINS_CL_ACC_MOV_CASH_IN_EXT = { 8, 4 };
    public static final int[] FINS_CL_ACC_MOV_CASH_IN_INT = { 8, 5 };
    public static final int[] FINS_CL_ACC_MOV_CASH_OUT_PUR = { 9, 1 };
    public static final int[] FINS_CL_ACC_MOV_CASH_OUT_SAL = { 9, 2 };
    public static final int[] FINS_CL_ACC_MOV_CASH_OUT_ADJ = { 9, 3 };
    public static final int[] FINS_CL_ACC_MOV_CASH_OUT_EXT = { 9, 4 };
    public static final int[] FINS_CL_ACC_MOV_CASH_OUT_INT = { 9, 5 };
    public static final int[] FINS_CL_ACC_MOV_PROF_LIQ_PUR = { 10, 1 };
    public static final int[] FINS_CL_ACC_MOV_PROF_LIQ_SAL = { 10, 2 };
    public static final int[] FINS_CL_ACC_MOV_PROF_FY_CLOSE = { 10, 3 };
    public static final int[] FINS_CL_ACC_MOV_LOSS_LIQ_PUR = { 11, 1 };
    public static final int[] FINS_CL_ACC_MOV_LOSS_LIQ_SAL = { 11, 2 };
    public static final int[] FINS_CL_ACC_MOV_LOSS_FY_CLOSE = { 11, 3 };
    public static final int[] FINS_CL_ACC_MOV_DEPREC = { 12, 1 };
    public static final int[] FINS_CL_ACC_MOV_AMORT = { 13, 1 };
    public static final int[] FINS_CL_ACC_MOV_COGM = { 14, 1 };
    public static final int[] FINS_CL_ACC_MOV_COGS = { 15, 1 };
    public static final int[] FINS_CL_ACC_MOV_SUBSYS_PAY = { 16, 1 };
    public static final int[] FINS_CL_ACC_MOV_SUBSYS_BAL = { 16, 2 };
    public static final int[] FINS_CL_ACC_MOV_TAX_DEBIT = { 17, 1 };
    public static final int[] FINS_CL_ACC_MOV_TAX_CREDIT = { 17, 2 };
    public static final int[] FINS_CL_ACC_MOV_JOURNAL = { 99, 1 };

    public static final int[] FINS_CLS_ACC_MOV_NA = { 1, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_FY_OPEN = { 2, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_FY_CLOSE = { 3, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_PUR = { 4, 1, 1 };
    //public static final int[] FINS_CLS_ACC_MOV_PUR_ANNUL = { 4, 1, 2 };
    //public static final int[] FINS_CLS_ACC_MOV_PUR_ADJ_RET = { 4, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_PUR_ADJ_DISC = { 4, 2, 2 };
    //public static final int[] FINS_CLS_ACC_MOV_PUR_CANCEL_ADJ_RET = { 4, 2, 3 };
    //public static final int[] FINS_CLS_ACC_MOV_PUR_CANCEL_ADJ_DISC = { 4, 2, 4 };
    public static final int[] FINS_CLS_ACC_MOV_SAL = { 5, 1, 1 };
    //public static final int[] FINS_CLS_ACC_MOV_SAL_ANNUL = { 5, 1, 2 };
    //public static final int[] FINS_CLS_ACC_MOV_SAL_ADJ_RET = { 5, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_SAL_ADJ_DISC = { 5, 2, 2 };
    //public static final int[] FINS_CLS_ACC_MOV_SAL_CANCEL_ADJ_RET = { 5, 2, 3 };
    //public static final int[] FINS_CLS_ACC_MOV_SAL_CANCEL_ADJ_DISC = { 5, 2, 4 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_PUR_FILL = { 6, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_SAL_DEV = { 6, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_INV_OPEN = { 6, 3, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_ADJ = { 6, 3, 2 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_CHG_PUR = { 6, 4, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_CHG_SAL = { 6, 4, 2 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_WAR_PUR = { 6, 4, 3 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_WAR_SAL = { 6, 4, 4 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_CONSIG_PUR = { 6, 4, 5 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_CONSIG_SAL = { 6, 4, 6 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_TRANSF = { 6, 5, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_CONV = { 6, 5, 2 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_MFG_CO = { 6, 6, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_IN_MFG_BP = { 6, 6, 2 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_PUR_DEV = { 7, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_SAL_FILL = { 7, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_INV_END = { 7, 3, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_ADJ = { 7, 3, 2 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_CHG_PUR = { 7, 4, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_CHG_SAL = { 7, 4, 2 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_WAR_PUR = { 7, 4, 3 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_WAR_SAL = { 7, 4, 4 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_CONSIG_PUR = { 7, 4, 5 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_CONSIG_SAL = { 7, 4, 6 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_TRANSF = { 7, 5, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_CONV = { 7, 5, 2 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_MFG_CO = { 7, 6, 1 };
    public static final int[] FINS_CLS_ACC_MOV_GDS_OUT_MFG_BP = { 7, 6, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_SUP_ADV_REF = { 8, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_SUP_PAY_REF = { 8, 1, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_CUS_ADV = { 8, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_CUS_PAY = { 8, 2, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_ADJ_BAL = { 8, 3, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_ADJ = { 8, 3, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_EXT_CDR = { 8, 4, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_EXT_DBR = { 8, 4, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_EXT_PAR = { 8, 4, 3 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_EXT_OTH = { 8, 4, 4 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_IN_INT_TRA = { 8, 5, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_SUP_ADV = { 9, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_SUP_PAY = { 9, 1, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_CUS_ADV_REF = { 9, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_CUS_PAY_REF = { 9, 2, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_ADJ_BAL = { 9, 3, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_ADJ = { 9, 3, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_EXT_CDR = { 9, 4, 1 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_EXT_DBR = { 9, 4, 2 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_EXT_PAR = { 9, 4, 3 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_EXT_OTH = { 9, 4, 4 };
    public static final int[] FINS_CLS_ACC_MOV_CASH_OUT_INT_TRA = { 9, 5, 1 };
    public static final int[] FINS_CLS_ACC_MOV_PROF_LIQ_PUR = { 10, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_PROF_LIQ_SAL = { 10, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_PROF_FY_CLOSE = { 10, 3, 1 };
    public static final int[] FINS_CLS_ACC_MOV_LOSS_LIQ_PUR = { 11, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_LOSS_LIQ_SAL = { 11, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_LOSS_FY_CLOSE = { 11, 3, 1 };
    public static final int[] FINS_CLS_ACC_MOV_DEPREC = { 12, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_AMORT = { 13, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_COGM = { 14, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_COGS = { 15, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_SUBSYS_PAY_APP = { 16, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA = { 16, 1, 2 };
    public static final int[] FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO = { 16, 1, 3 };
    public static final int[] FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE = { 16, 1, 4 };
    public static final int[] FINS_CLS_ACC_MOV_SUBSYS_BAL_APP = { 16, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_SUBSYS_BAL_TRA = { 16, 2, 2 };
    public static final int[] FINS_CLS_ACC_MOV_SUBSYS_BAL_CLO = { 16, 2, 3 };
    public static final int[] FINS_CLS_ACC_MOV_SUBSYS_BAL_OPE = { 16, 2, 4 };
    public static final int[] FINS_CLS_ACC_MOV_TAX_DEBIT_CASH = { 17, 1, 1 };
    public static final int[] FINS_CLS_ACC_MOV_TAX_DEBIT_PEND = { 17, 1, 2 };
    public static final int[] FINS_CLS_ACC_MOV_TAX_DEBIT_PEND_ADV = { 17, 1, 3 };
    public static final int[] FINS_CLS_ACC_MOV_TAX_CREDIT_CASH = { 17, 2, 1 };
    public static final int[] FINS_CLS_ACC_MOV_TAX_CREDIT_PEND = { 17, 2, 2 };
    public static final int[] FINS_CLS_ACC_MOV_TAX_CREDIT_PEND_ADV = { 17, 2, 3 };
    public static final int[] FINS_CLS_ACC_MOV_JOURNAL = { 99, 1, 1 };

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
    public static final int FINS_TP_ACC_SPE_BPR_VEN = 21;
    public static final int FINS_TP_ACC_SPE_BPR_CUS = 22;
    public static final int FINS_TP_ACC_SPE_BPR_CDR = 23;
    public static final int FINS_TP_ACC_SPE_BPR_DBR = 24;
    public static final int FINS_TP_ACC_SPE_BPR_VEN_ADV = 26;
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

    public static final int[] FINS_CLS_ACC_NA = { 1, 1, 1 };
    public static final int[] FINS_CLS_ACC_ASSET_ST = { 2, 1, 1 };
    public static final int[] FINS_CLS_ACC_ASSET_LT = { 2, 1, 1 };
    public static final int[] FINS_CLS_ACC_ASSET_DIF = { 2, 1, 3 };
    public static final int[] FINS_CLS_ACC_ASSET_OTH = { 2, 1, 4 };
    public static final int[] FINS_CLS_ACC_LIABTY_ST = { 2, 2, 1 };
    public static final int[] FINS_CLS_ACC_LIABTY_LT = { 2, 2, 1 };
    public static final int[] FINS_CLS_ACC_LIABTY_DIF = { 2, 2, 3 };
    public static final int[] FINS_CLS_ACC_LIABTY_OTH = { 2, 2, 4 };
    public static final int[] FINS_CLS_ACC_EQUITY_CONT = { 2, 3, 1 };
    public static final int[] FINS_CLS_ACC_EQUITY_GAIN = { 2, 3, 2 };
    public static final int[] FINS_CLS_ACC_ORD_DBT = { 2, 4, 1 };
    public static final int[] FINS_CLS_ACC_ORD_CDT = { 2, 5, 1 };
    public static final int[] FINS_CLS_ACC_PUR = { 3, 1, 1 };
    public static final int[] FINS_CLS_ACC_SAL_ADJ = { 3, 1, 2 };
    public static final int[] FINS_CLS_ACC_COGS = { 3, 1, 3 };
    public static final int[] FINS_CLS_ACC_EXPEN_PUR = { 3, 1, 4 };
    public static final int[] FINS_CLS_ACC_EXPEN_MFG = { 3, 1, 5 };
    public static final int[] FINS_CLS_ACC_EXPEN_OP = { 3, 1, 6 };
    public static final int[] FINS_CLS_ACC_FIN_EXPEN = { 3, 1, 7 };
    public static final int[] FINS_CLS_ACC_OTH_EXPEN = { 3, 1, 8 };
    public static final int[] FINS_CLS_ACC_RES_OTH_DBT = { 3, 1, 9 };
    public static final int[] FINS_CLS_ACC_SAL = { 3, 2, 1 };
    public static final int[] FINS_CLS_ACC_PUR_ADJ = { 3, 2, 2 };
    public static final int[] FINS_CLS_ACC_FIN_INCOME = { 3, 2, 3 };
    public static final int[] FINS_CLS_ACC_OTH_INCOME = { 3, 2, 4 };
    public static final int[] FINS_CLS_ACC_RES_OTH_CDT = { 3, 2, 5 };

    public static final int FINS_TP_ACC_SYS_NA = 1;
    public static final int FINS_TP_ACC_SYS_CASH_CASH = 2;
    public static final int FINS_TP_ACC_SYS_CASH_BANK = 3;
    public static final int FINS_TP_ACC_SYS_SUP = 4;
    //public static final int FINS_TP_ACC_SYS_SUP_ADV = 5;
    public static final int FINS_TP_ACC_SYS_CUS = 6;
    //public static final int FINS_TP_ACC_SYS_CUS_ADV = 7;
    public static final int FINS_TP_ACC_SYS_CDR = 8;
    public static final int FINS_TP_ACC_SYS_DBR = 9;
    public static final int FINS_TP_ACC_SYS_INV = 10;
    //public static final int FINS_TP_ACC_SYS_INV_MFG = 11;
    public static final int FINS_TP_ACC_SYS_PUR = 12;
    public static final int FINS_TP_ACC_SYS_PUR_ADJ = 13;
    public static final int FINS_TP_ACC_SYS_SAL = 14;
    public static final int FINS_TP_ACC_SYS_SAL_ADJ = 15;
    public static final int FINS_TP_ACC_SYS_TAX_DBT = 16;
    public static final int FINS_TP_ACC_SYS_TAX_CDT = 17;
    public static final int FINS_TP_ACC_SYS_PROF_LOSS = 18;
    public static final int FINS_TP_ACC_SYS_ASSET_FIX = 19;
    //public static final int FINS_TP_ACC_SYS_ASSET_DIF = 20;
    //public static final int FINS_TP_ACC_SYS_LIABTY_DIF = 21;
    //public static final int FINS_TP_ACC_SYS_GTY_DEP_DEBIT = 22;
    //public static final int FINS_TP_ACC_SYS_GTY_DEP_CREDIT = 23;
    //public static final int FINS_TP_ACC_SYS_PAY_NOTE = 24;
    //public static final int FINS_TP_ACC_SYS_PAY_MORT = 25;
    //public static final int FINS_TP_ACC_SYS_REC_NOTE = 26;
    //public static final int FINS_TP_ACC_SYS_REC_MORT = 27;
    
    public static final int[] FINS_TP_SYS_MOV_TAX_SAL_EFVO = new int[] { 32, 32 }; // table fid_tp_sys_mov
    
    public static final int FINS_CT_SYS_MOV_NA = 1; // table fid_ct_sys_mov_xxx
    public static final int FINS_CT_SYS_MOV_ASSET = 2; // table fid_ct_sys_mov_xxx
    public static final int FINS_CT_SYS_MOV_CASH = 3; // table fid_ct_sys_mov_xxx
    public static final int FINS_CT_SYS_MOV_BPS = 4; // table fid_ct_sys_mov_xxx
    public static final int FINS_CT_SYS_MOV_TAX = 5; // table fid_ct_sys_mov_xxx
    public static final int FINS_CT_SYS_MOV_PUR = 6; // table fid_ct_sys_mov_xxx
    public static final int FINS_CT_SYS_MOV_SAL = 7; // table fid_ct_sys_mov_xxx
    public static final int FINS_CT_SYS_MOV_COGS = 8; // table fid_ct_sys_mov_xxx
    public static final int FINS_CT_SYS_MOV_PROF = 9; // table fid_ct_sys_mov_xxx

    public static final int[] FINS_TP_SYS_MOV_NA = new int[] { 1, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_ASSET_ASSET = new int[] { 2, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_ASSET_STOCK = new int[] { 2, 2 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_CASH_CASH = new int[] { 3, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_CASH_BANK = new int[] { 3, 2 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_BPS_CO = new int[] { 4, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_BPS_SUP = new int[] { 4, 2 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_BPS_CUS = new int[] { 4, 3 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_BPS_CDR = new int[] { 4, 4 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_BPS_DBR = new int[] { 4, 5 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_TAX_DBT = new int[] { 5, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_TAX_DBT_PEND = new int[] { 5, 2 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_TAX_DBT_PEND_ADV = new int[] { 5, 3 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_TAX_CDT = new int[] { 5, 11 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_TAX_CDT_PEND = new int[] { 5, 12 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_TAX_CDT_PEND_ADV = new int[] { 5, 13 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_PUR_ASSET = new int[] { 6, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_PUR_GOOD = new int[] { 6, 2 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_PUR_SERV = new int[] { 6, 3 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_SAL_ASSET = new int[] { 7, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_SAL_GOOD = new int[] { 7, 2 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_SAL_SERV = new int[] { 7, 3 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_COGS_ASSET = new int[] { 8, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_COGS_GOOD = new int[] { 8, 2 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_PROF_LOSS = new int[] { 9, 1 }; // table fid_tp_sys_mov_xxx
    public static final int[] FINS_TP_SYS_MOV_PROF_PROF = new int[] { 9, 2 }; // table fid_tp_sys_mov_xxx

    public static final int FINS_CT_ACC_CASH_CASH = 1;
    public static final int FINS_CT_ACC_CASH_BANK = 2;

    public static final int[] FINS_TP_ACC_CASH_CASH_CASH = { 1, 1 };
    public static final int[] FINS_TP_ACC_CASH_CASH_CASH_PETTY = { 1, 2 };
    public static final int[] FINS_TP_ACC_CASH_BANK_SAV = { 2, 1 };
    public static final int[] FINS_TP_ACC_CASH_BANK_DBT = { 2, 2 };
    public static final int[] FINS_TP_ACC_CASH_BANK_CDT = { 2, 3 };
    public static final int[] FINS_TP_ACC_CASH_BANK_AGR = { 2, 4 };

    public static final int FINS_TP_ACC_BP_OP = 1;
    public static final int FINS_TP_ACC_BP_PAY = 2;
    public static final int FINS_TP_ACC_BP_ADV_BILL = 3;

    public static final int FINS_TP_ACC_ITEM_ASSET = 1;
    public static final int FINS_TP_ACC_ITEM_INV_MFG_CO = 2;
    public static final int FINS_TP_ACC_ITEM_INV_MFG_BP = 3;
    public static final int FINS_TP_ACC_ITEM_INV_WAR_PUR = 4;
    public static final int FINS_TP_ACC_ITEM_INV_WAR_SAL = 5;
    public static final int FINS_TP_ACC_ITEM_INV_CONSIG_PUR = 6;
    public static final int FINS_TP_ACC_ITEM_INV_CONSIG_SAL = 7;
    public static final int FINS_TP_ACC_ITEM_COGS = 8;
    public static final int FINS_TP_ACC_ITEM_PUR = 9;
    public static final int FINS_TP_ACC_ITEM_PUR_ADJ_DEV = 10;
    public static final int FINS_TP_ACC_ITEM_PUR_ADJ_DISC = 11;
    public static final int FINS_TP_ACC_ITEM_SAL = 12;
    public static final int FINS_TP_ACC_ITEM_SAL_ADJ_DEV = 13;
    public static final int FINS_TP_ACC_ITEM_SAL_ADJ_DISC = 14;
    public static final int FINS_TP_ACC_ITEM_INV_OPEN = 15;
    public static final int FINS_TP_ACC_ITEM_INV_END = 16;
    public static final int FINS_TP_ACC_ITEM_ADJ_INC = 17;
    public static final int FINS_TP_ACC_ITEM_ADJ_DEC = 18;
    public static final int FINS_TP_ACC_ITEM_CHG_PUR = 19;
    public static final int FINS_TP_ACC_ITEM_CHG_SAL = 20;
    public static final int FINS_TP_ACC_ITEM_MFG_CO = 21;
    public static final int FINS_TP_ACC_ITEM_MFG_BP = 22;
    public static final int FINS_TP_ACC_ITEM_WAR_PUR = 23;
    public static final int FINS_TP_ACC_ITEM_WAR_SAL = 24;
    public static final int FINS_TP_ACC_ITEM_CONSIG_PUR = 25;
    public static final int FINS_TP_ACC_ITEM_CONSIG_SAL = 26;
    public static final int FINS_TP_ACC_ITEM_DEPREC = 27;
    public static final int FINS_TP_ACC_ITEM_DEPREC_REC = 28;
    public static final int FINS_TP_ACC_ITEM_DEPREC_EXP = 29;

    public static final int FINS_TP_CARD_DEB = 1;
    public static final int FINS_TP_CARD_CRE = 2;

    public static final int FINS_TP_PAY_BANK_THIRD = 1;
    public static final int FINS_TP_PAY_BANK_TEF = 11;
    public static final int FINS_TP_PAY_BANK_SPEI_FD_N = 21;
    public static final int FINS_TP_PAY_BANK_SPEI_FD_Y = 22;
    public static final int FINS_TP_PAY_BANK_AGREE = 31;

    public static final int FINS_ST_FIN_MOV_AVLB = 1;
    public static final int FINS_ST_FIN_MOV_TRANSIT = 2;

    public static final int FINU_TAX_BAS_VAT = 1;
    
    public static final java.lang.String FINU_TP_REC_FY_OPEN = "EA";
    public static final java.lang.String FINU_TP_REC_FY_END = "EC";
    public static final java.lang.String FINU_TP_REC_PUR = "C";
    public static final java.lang.String FINU_TP_REC_SAL = "V";
    public static final java.lang.String FINU_TP_REC_GDS_IN = "ME";
    public static final java.lang.String FINU_TP_REC_GDS_OUT = "MS";
    public static final java.lang.String FINU_TP_REC_CASH_IN = "DE";
    public static final java.lang.String FINU_TP_REC_CASH_OUT = "DS";
    public static final java.lang.String FINU_TP_REC_EXC_FLUC_PROF = "FG";
    public static final java.lang.String FINU_TP_REC_EXC_FLUC_LOSS = "FP";
    public static final java.lang.String FINU_TP_REC_DEPREC = "Dp";
    public static final java.lang.String FINU_TP_REC_AMORT = "Am";
    public static final java.lang.String FINU_TP_REC_COGM = "CP";
    public static final java.lang.String FINU_TP_REC_COGS = "CV";
    public static final java.lang.String FINU_TP_REC_SUBSYS_SUP = "SP";
    public static final java.lang.String FINU_TP_REC_SUBSYS_CUS = "SC";
    public static final java.lang.String FINU_TP_REC_SUBSYS_CDR = "SA";
    public static final java.lang.String FINU_TP_REC_SUBSYS_DBR = "SD";
    public static final java.lang.String FINU_TP_REC_JOURNAL = "Dr";
    public static final java.lang.String FINU_TP_REC_CASH_BANK = "CB";

    public static final int FINU_TP_ACC_USR_NA = 1;
    public static final int FINU_TP_ACC_USR_BALANCE = 2;
    public static final int FINU_TP_ACC_USR_RESULTS = 3;
    public static final int FINU_TP_ACC_USR_ORDER = 4;

    public static final int [] FINU_CL_ACC_USR_NA = { 1, 1 };
    public static final int [] FINU_CL_ACC_USR_ASSET = { 2, 1 };
    public static final int [] FINU_CL_ACC_USR_LIABTY = { 2, 2 };
    public static final int [] FINU_CL_ACC_USR_EQUITY = { 2, 3 };
    public static final int [] FINU_CL_ACC_USR_RES_DEB = { 3, 1 };
    public static final int [] FINU_CL_ACC_USR_RES_CRE = { 3, 2 };
    public static final int [] FINU_CL_ACC_USR_ORD_DEB = { 4, 1 };
    public static final int [] FINU_CL_ACC_USR_ORD_CRE = { 4, 2 };

    public static final int [] FINU_CLS_ACC_USR_NA = { 1, 1, 1 };
    public static final int [] FINU_CLS_ACC_USR_ASSET_ST = { 2, 1, 1 };
    public static final int [] FINU_CLS_ACC_USR_ASSET_LT = { 2, 1, 2 };
    public static final int [] FINU_CLS_ACC_USR_ASSET_DIF = { 2, 1, 3 };
    public static final int [] FINU_CLS_ACC_USR_ASSET_OTH = { 2, 1, 4 };
    public static final int [] FINU_CLS_ACC_USR_LIABTY_ST = { 2, 2, 1 };
    public static final int [] FINU_CLS_ACC_USR_LIABTY_LT = { 2, 2, 2 };
    public static final int [] FINU_CLS_ACC_USR_LIABTY_DIF = { 2, 2, 3 };
    public static final int [] FINU_CLS_ACC_USR_LIABTY_OTH = { 2, 2, 4 };
    public static final int [] FINU_CLS_ACC_USR_EQUITY_CONT = { 2, 3, 1 };
    public static final int [] FINU_CLS_ACC_USR_EQUITY_GAIN = { 2, 3, 2 };
    public static final int [] FINU_CLS_ACC_USR_PUR = { 3, 1, 1 };
    public static final int [] FINU_CLS_ACC_USR_SAL_ADJ = { 3, 1, 2 };
    public static final int [] FINU_CLS_ACC_USR_COGS = { 3, 1, 3 };
    public static final int [] FINU_CLS_ACC_USR_EXPEN_PUR = { 3, 1, 4 };
    public static final int [] FINU_CLS_ACC_USR_EXPEN_MFG = { 3, 1, 5 };
    public static final int [] FINU_CLS_ACC_USR_EXPEN_OP = { 3, 1, 6 };
    public static final int [] FINU_CLS_ACC_USR_FIN_EXPEN = { 3, 1, 7 };
    public static final int [] FINU_CLS_ACC_USR_OTH_EXPEN = { 3, 1, 8 };
    public static final int [] FINU_CLS_ACC_USR_RES_DEB_OTH = { 3, 1, 9 };
    public static final int [] FINU_CLS_ACC_USR_SAL = { 3, 2, 1 };
    public static final int [] FINU_CLS_ACC_USR_PUR_ADJ = { 3, 2, 2 };
    public static final int [] FINU_CLS_ACC_USR_FIN_INCOME = { 3, 2, 3 };
    public static final int [] FINU_CLS_ACC_USR_OTH_INCOME = { 3, 2, 4 };
    public static final int [] FINU_CLS_ACC_USR_RES_CRE_OTH = { 3, 2, 5 };
    public static final int [] FINU_CLS_ACC_USR_ORD_DEB = { 4, 1, 1 };
    public static final int [] FINU_CLS_ACC_USR_ORD_CRE = { 4, 2, 1 };

    public static final int FINU_TP_ACC_LEDGER_CASH = 2;
    public static final int FINU_TP_ACC_LEDGER_BANK = 3;
    public static final int FINU_TP_ACC_LEDGER_CUS = 6;
    public static final int FINU_TP_ACC_LEDGER_DBR = 7;
    public static final int FINU_TP_ACC_LEDGER_INV = 8;
    public static final int FINU_TP_ACC_LEDGER_VAT_CREDITABLE = 9;
    public static final int FINU_TP_ACC_LEDGER_VAT_CREDITABLE_PEND = 10;
    public static final int FINU_TP_ACC_LEDGER_SUP = 33;
    public static final int FINU_TP_ACC_LEDGER_CDR = 34;
    public static final int FINU_TP_ACC_LEDGER_VAT_PAYABLE = 36;
    public static final int FINU_TP_ACC_LEDGER_VAT_PAYABLE_PEND = 37;
    
    public static final int FINX_ACC_PAY = 1;
    public static final int FINX_ACC_PAY_PEND = 2;

    public static final int FINX_TP_ACC_MOV_DPS_PUR = 1;
    public static final int FINX_TP_ACC_MOV_DPS_SAL = 2;
    public static final int FINX_TP_ACC_MOV_BIZ_PARTNER_SUP = 3;
    public static final int FINX_TP_ACC_MOV_BIZ_PARTNER_CUS = 4;
    
    public static final int FINX_ACC = 1;
    public static final int FINX_ACC_DEEP = 2;
    
    public static final String FINX_TAX_FACT_EX = "E";
    public static final String FINX_TAX_FACT_TA = "T";
    
    public static final int TRNS_CT_DPS_PUR = 1;
    public static final int TRNS_CT_DPS_SAL = 2;
    
    public static final int TRNS_CL_DPS_EST = 1;
    public static final int TRNS_CL_DPS_ORD = 2;
    public static final int TRNS_CL_DPS_DOC = 3;
    public static final int TRNS_CL_DPS_ADJ = 5;

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

    public static final int TRNS_TP_DPS_ADJ_NA = 1;
    public static final int TRNS_TP_DPS_ADJ_RET = 2;
    public static final int TRNS_TP_DPS_ADJ_DISC = 3;
    //public static final int TRNS_TP_DPS_ADJ_RET_NULL = 4;
    //public static final int TRNS_TP_DPS_ADJ_DISC_NULL = 5;

    public static final int[] TRNS_STP_DPS_ADJ_NA_NA = { 1, 1 };
    public static final int[] TRNS_STP_DPS_ADJ_RET_RET = { 2, 1 };
    public static final int[] TRNS_STP_DPS_ADJ_RET_MISS = { 2, 2 };
    public static final int[] TRNS_STP_DPS_ADJ_RET_ANNUL = { 2, 3 };
    public static final int[] TRNS_STP_DPS_ADJ_DISC_DISC = { 3, 1 };
    public static final int[] TRNS_STP_DPS_ADJ_DISC_PAY = { 3, 2 };
    public static final int[] TRNS_STP_DPS_ADJ_DISC_PRICE = { 3, 3 };
    public static final int[] TRNS_STP_DPS_ADJ_DISC_BONUS = { 3, 4};
    public static final int[] TRNS_STP_DPS_ADJ_RET_NULL = { 4, 1 };
    public static final int[] TRNS_STP_DPS_ADJ_DISC_NULL = { 5, 1 };

    public static final int TRNS_CT_IOG_IN = 1;
    public static final int TRNS_CT_IOG_OUT = 2;

    public static final int[] TRNS_CL_IOG_IN_PUR = { 1, 1 };
    public static final int[] TRNS_CL_IOG_IN_SAL = { 1, 2 };
    public static final int[] TRNS_CL_IOG_IN_ADJ = { 1, 3 };
    public static final int[] TRNS_CL_IOG_IN_EXT = { 1, 4 };
    public static final int[] TRNS_CL_IOG_IN_INT = { 1, 5 };
    public static final int[] TRNS_CL_IOG_IN_MFG = { 1, 6 };
    public static final int[] TRNS_CL_IOG_IN_EXP = { 1, 9 };
    public static final int[] TRNS_CL_IOG_OUT_PUR = { 2, 1 };
    public static final int[] TRNS_CL_IOG_OUT_SAL = { 2, 2 };
    public static final int[] TRNS_CL_IOG_OUT_ADJ = { 2, 3 };
    public static final int[] TRNS_CL_IOG_OUT_EXT = { 2, 4 };
    public static final int[] TRNS_CL_IOG_OUT_INT = { 2, 5 };
    public static final int[] TRNS_CL_IOG_OUT_MFG = { 2, 6 };
    public static final int[] TRNS_CL_IOG_OUT_EXP = { 2, 9 };

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

    public static final int TRNS_TP_PAY_CASH = 1;
    public static final int TRNS_TP_PAY_CREDIT = 2;

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

    public static final int TRNS_TP_DPS_EVT_EMIT = 1;
    public static final int TRNS_TP_DPS_EVT_ANNUL = 2;
    public static final int TRNS_TP_DPS_EVT_RISS = 3;
    public static final int TRNS_TP_DPS_EVT_REPL = 4;
    public static final int TRNS_TP_DPS_EVT_EDIT = 5;
    public static final int TRNS_TP_DPS_EVT_PRINT = 6;
    public static final int TRNS_TP_DPS_EVT_CLOSE = 7;
    public static final int TRNS_TP_DPS_EVT_FFL = 8;
    public static final int TRNS_TP_DPS_EVT_PAY = 9;

    public static final int TRNS_TP_DPS_PRT_NA = 1;
    public static final int TRNS_TP_DPS_PRT_BP = 2;
    public static final int TRNS_TP_DPS_PRT_GP = 3;

    public static final int TRNS_TP_DPS_ETY_SYS = 1;
    public static final int TRNS_TP_DPS_ETY_ORDY = 2;
    public static final int TRNS_TP_DPS_ETY_VIRT = 3;

    public static final int TRNS_ST_DPS_NEW = 1;
    public static final int TRNS_ST_DPS_EMITED = 2;
    public static final int TRNS_ST_DPS_ANNULED = 3;

    public static final int TRNS_ST_DPS_VAL_EFF = 1;
    public static final int TRNS_ST_DPS_VAL_RISS = 2;
    public static final int TRNS_ST_DPS_VAL_REPL = 3;

    public static final int TRNS_ST_DPS_AUTHORN_NA = 1;
    public static final int TRNS_ST_DPS_AUTHORN_PENDING = 2;
    public static final int TRNS_ST_DPS_AUTHORN_REJECT = 3;
    public static final int TRNS_ST_DPS_AUTHORN_AUTHORN = 4;

    public static final int TRNS_CT_SIGN_IN = 1;
    public static final int TRNS_CT_SIGN_OUT = 2;

    public static final int[] TRNS_TP_SIGN_IN_INV_OPEN = { 1, 1 };
    public static final int[] TRNS_TP_SIGN_IN_ACQUIRED = { 1, 2 };
    public static final int[] TRNS_TP_SIGN_OUT_INV_END = { 2, 1 };
    public static final int[] TRNS_TP_SIGN_OUT_EMITED = { 2, 2 };
    public static final int[] TRNS_TP_SIGN_OUT_ANNULED = { 2, 3 };

    public static final int TRNS_TP_XML_NA = 1;
    public static final int TRNS_TP_XML_CFD = 2;
    public static final int TRNS_TP_XML_CFDI_32 = 3;
    public static final int TRNS_TP_XML_CFDI_33 = 4;
    public static final int TRNS_TP_XML_CFDI_40 = 5;

    public static final int TRNS_TP_CFD_INV = 1;        // invoice
    public static final int TRNS_TP_CFD_BOL = 2;        // bill of lading
    public static final int TRNS_TP_CFD_PAY_REC = 6;    // payment receipt
    public static final int TRNS_TP_CFD_PAYROLL = 11;   // payroll

    public static final int TRNS_TP_STK_SEG_SHIP = 1;       // shipment
    public static final int TRNS_TP_STK_SEG_MFG_ORD = 2;    // manufacturing order
    public static final int TRNS_TP_STK_SEG_REQ_MAT = 3;    // Material request

    public static final int TRNS_TP_STK_SEG_INC = 1;    // increment
    public static final int TRNS_TP_STK_SEG_DEC = 2;    // decrement

    public static final int TRNS_CFD_CAT_PAY_WAY = 11;  // payment way
    public static final int TRNS_CFD_CAT_PAY_MET = 12;  // payment method
    public static final int TRNS_CFD_CAT_CUR = 13;      // currency
    public static final int TRNS_CFD_CAT_CFD_TP = 14;   // CFDI type
    public static final int TRNS_CFD_CAT_ZIP = 15;      // zip code
    public static final int TRNS_CFD_CAT_REL_TP = 16;   // relation type
    public static final int TRNS_CFD_CAT_TAX_REG = 17;  // tax regime
    public static final int TRNS_CFD_CAT_CTY = 18;      // country
    public static final int TRNS_CFD_CAT_CFD_USE = 19;  // CFDI use
    public static final int TRNS_CFD_CAT_INT_MOV_REA = 21;  // International Commerce: move reason (Int. Commerce)
    public static final int TRNS_CFD_CAT_INT_OPN_TP = 22;   // International Commerce: operation type (Int. Commerce)
    public static final int TRNS_CFD_CAT_INT_REQ_KEY = 23;  // International Commerce: request keys (Int. Commerce)
    public static final int TRNS_CFD_CAT_EXP = 24; // exportation
    public static final int TRNS_CFD_CAT_PER = 25; // periodicity
    public static final int TRNS_CFD_CAT_MON = 26; // month
    public static final int TRNS_CFD_CAT_TAX_OBJ = 27; // tax object
    public static final int TRNS_CFD_CAT_BOL_TRA = 31;          // BOL: transportation code
    public static final int TRNS_CFD_CAT_BOL_PER_TP = 32;       // BOL: SCT permisson type
    public static final int TRNS_CFD_CAT_BOL_MOTOR_CFG = 33;    // BOL: motortransport configuration
    public static final int TRNS_CFD_CAT_BOL_TRAILER_STP = 34;  // BOL: trailer subtype
    public static final int TRNS_CFD_CAT_BOL_TRANSP_PART = 35;  // BOL: transport part
    
    public static final int TRNU_DPS_NAT_DEF = 1;
    
    public static final int TRNU_CT_DPS_PUR = 1;
    public static final int TRNU_CT_DPS_SAL = 2;

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

    public static final int TRNU_TP_PAY_SYS_NA = 1;
    public static final int TRNU_TP_PAY_SYS_CASH = 11;
    public static final int TRNU_TP_PAY_SYS_CHECK = 12;
    public static final int TRNU_TP_PAY_SYS_TRANSF = 13;
    public static final int TRNU_TP_PAY_SYS_CARD_DBT = 21;
    public static final int TRNU_TP_PAY_SYS_CARD_CDT = 22;
    public static final int TRNU_TP_PAY_SYS_CARD_SRV = 23;
    public static final int TRNU_TP_PAY_SYS_E_PURSE = 31;
    public static final int TRNU_TP_PAY_SYS_E_MONEY = 32;
    public static final int TRNU_TP_PAY_SYS_FOOD_STP = 41;
    public static final int TRNU_TP_PAY_SYS_UNDEF = 98;
    public static final int TRNU_TP_PAY_SYS_OTHER = 99;
    
    public static final int TRNU_TP_IOG_ADJ_NA = 1;

    public static final int TRNU_TP_DPS_ANN_NA = 1;
    
    public static final int TRNX_TP_DPS_EST = 100;
    public static final int TRNX_TP_DPS_EST_EST = 101;
    public static final int TRNX_TP_DPS_EST_CON = 102;
    public static final int TRNX_TP_DPS_ORD = 200;
    public static final int TRNX_TP_DPS_ORD_STK = 201;
    public static final int TRNX_TP_DPS_DOC = 300;
    public static final int TRNX_TP_DPS_BOL = 400;
    public static final int TRNX_TP_DPS_ADJ = 500;

    public static final int TRNX_DPS_BAL_ALL = 1;
    public static final int TRNX_DPS_BAL_PAY = 2;
    public static final int TRNX_DPS_BAL_PAY_PEND = 3;

    public static final int TRNX_LINK_EST_EST_SRC = 1;
    public static final int TRNX_LINK_EST_CON_SRC = 2;
    public static final int TRNX_LINK_ORD_SRC = 3;
    public static final int TRNX_LINK_ORD_DES = 4;
    public static final int TRNX_LINK_DOC_DES = 5;
    
    public static final int TRNX_DPS_PUR_CON_AUT_PEND = 61;
    public static final int TRNX_DPS_PUR_CON_AUT_AUT = 62;
    public static final int TRNX_DPS_PUR_CON_AUT_REJ = 63;
    public static final int TRNX_DPS_SAL_CON_AUT_PEND = 64;
    public static final int TRNX_DPS_SAL_CON_AUT_AUT = 65;
    public static final int TRNX_DPS_SAL_CON_AUT_REJ = 66;

    public static final int TRNX_DPS_PUR_ORD_AUT_AUT_STK = 48;    
    public static final int TRNX_DPS_PUR_ORD_AUT_AUT = 49;
    public static final int TRNX_DPS_PUR_ORD_AUT_PEND = 50;
    public static final int TRNX_DPS_PUR_ORD_AUT_REJ = 51;
    public static final int TRNX_DPS_SAL_ORD_AUT_AUT = 52;
    public static final int TRNX_DPS_SAL_ORD_AUT_PEND = 53;
    public static final int TRNX_DPS_SAL_ORD_AUT_REJ = 54;

    public static final int TRNX_DPS_PUR_DOC_AUT_AUT = 55;
    public static final int TRNX_DPS_PUR_DOC_AUT_PEND = 56;
    public static final int TRNX_DPS_PUR_DOC_AUT_REJ = 57;
    public static final int TRNX_DPS_SAL_DOC_AUT_AUT = 58;
    public static final int TRNX_DPS_SAL_DOC_AUT_PEND = 59;
    public static final int TRNX_DPS_SAL_DOC_AUT_REJ = 60;
    

    public static final int TRNX_TP_IOG_IN_PUR_PUR = 101;
    public static final int TRNX_TP_IOG_IN_SAL_SAL = 102;
    public static final int TRNX_TP_IOG_IN_ADJ_INV = 103;
    public static final int TRNX_TP_IOG_IN_ADJ_ADJ = 104;
    public static final int TRNX_TP_IOG_IN_EXT_CHG_PUR = 105;
    public static final int TRNX_TP_IOG_IN_EXT_CHG_SAL = 106;
    public static final int TRNX_TP_IOG_IN_EXT_WAR_PUR = 107;
    public static final int TRNX_TP_IOG_IN_EXT_WAR_SAL = 108;
    public static final int TRNX_TP_IOG_IN_EXT_CSG_PUR = 109;
    public static final int TRNX_TP_IOG_IN_EXT_CSG_SAL = 110;
    public static final int TRNX_TP_IOG_IN_INT_TRA = 111;
    public static final int TRNX_TP_IOG_IN_INT_CNV = 112;
    public static final int TRNX_TP_IOG_IN_MFG_RM_ASD = 113;
    public static final int TRNX_TP_IOG_IN_MFG_RM_RET = 114;
    public static final int TRNX_TP_IOG_IN_MFG_WP_ASD = 115;
    public static final int TRNX_TP_IOG_IN_MFG_WP_RET = 116;
    public static final int TRNX_TP_IOG_IN_MFG_FG_ASD = 117;
    public static final int TRNX_TP_IOG_IN_MFG_FG_RET = 118;

    public static final int TRNX_TP_IOG_OUT_PUR_PUR = 201;
    public static final int TRNX_TP_IOG_OUT_SAL_SAL = 202;
    public static final int TRNX_TP_IOG_OUT_ADJ_INV = 203;
    public static final int TRNX_TP_IOG_OUT_ADJ_ADJ = 204;
    public static final int TRNX_TP_IOG_OUT_EXT_CHG_PUR = 205;
    public static final int TRNX_TP_IOG_OUT_EXT_CHG_SAL = 206;
    public static final int TRNX_TP_IOG_OUT_EXT_WAR_PUR = 207;
    public static final int TRNX_TP_IOG_OUT_EXT_WAR_SAL = 208;
    public static final int TRNX_TP_IOG_OUT_EXT_CSG_PUR = 209;
    public static final int TRNX_TP_IOG_OUT_EXT_CSG_SAL = 210;
    public static final int TRNX_TP_IOG_OUT_INT_TRA = 211;
    public static final int TRNX_TP_IOG_OUT_INT_CNV = 212;
    public static final int TRNX_TP_IOG_OUT_MFG_RM_ASD = 213;
    public static final int TRNX_TP_IOG_OUT_MFG_RM_RET = 214;
    public static final int TRNX_TP_IOG_OUT_MFG_WP_ASD = 215;
    public static final int TRNX_TP_IOG_OUT_MFG_WP_RET = 216;
    public static final int TRNX_TP_IOG_OUT_MFG_FG_ASD = 217;
    public static final int TRNX_TP_IOG_OUT_MFG_FG_RET = 218;

    public static final int TRNX_PUR_TOT = 201;
    public static final int TRNX_PUR_TOT_MONTH = 202;
    public static final int TRNX_PUR_TOT_BY_BP = 203;
    public static final int TRNX_PUR_TOT_BY_BP_ITEM = 204;
    public static final int TRNX_PUR_TOT_BY_IGEN = 205;
    public static final int TRNX_PUR_TOT_BY_IGEN_BP = 206;
    public static final int TRNX_PUR_TOT_BY_ITEM = 207;
    public static final int TRNX_PUR_TOT_BY_ITEM_BP = 208;
    public static final int TRNX_PUR_TOT_BY_AGS = 209;
    public static final int TRNX_PUR_TOT_BY_AGS_BP = 210;
    public static final int TRNX_PUR_TOT_BY_AGS_ITEM = 211;
    public static final int TRNX_PUR_TOT_BY_TP_BP = 212;
    public static final int TRNX_PUR_TOT_BY_TP_BP_BP = 213;
    /** All document entries purchase at once. What type of document is needed must be provided: invoices or credit notes. */
    public static final int TRNX_PUR_DPS_BY_ITEM_N_BP_ALL = 214;
    /** Purchase document entries from one business partner and/or item at a time. Entries from invoices and credit notes are shown together. */
    public static final int TRNX_PUR_DPS_BY_ITEM_N_BP_ONE = 215;
    public static final int TRNX_PUR_DPS_BY_CHANGE_ITEM_CONCEPT = 216;

    public static final int TRNX_SAL_TOT = 301;
    public static final int TRNX_SAL_TOT_MONTH = 302;
    public static final int TRNX_SAL_TOT_BY_BP = 303;
    public static final int TRNX_SAL_TOT_BY_BP_ITEM = 304;
    public static final int TRNX_SAL_TOT_BY_IGEN = 305;
    public static final int TRNX_SAL_TOT_BY_IGEN_BP = 306;
    public static final int TRNX_SAL_TOT_BY_ITEM = 307;
    public static final int TRNX_SAL_TOT_BY_ITEM_BP = 308;
    public static final int TRNX_SAL_TOT_BY_AGS = 309;
    public static final int TRNX_SAL_TOT_BY_AGS_BP = 310;
    public static final int TRNX_SAL_TOT_BY_AGS_ITEM = 311;
    public static final int TRNX_SAL_TOT_BY_TP_BP = 312;
    public static final int TRNX_SAL_TOT_BY_TP_BP_BP = 313;
    /** All sales document entries at once. What type of document is needed must be provided: invoices or credit notes. */
    public static final int TRNX_SAL_DPS_BY_ITEM_N_BP_ALL = 314;
    /** Sales document entries from one business partner and/or item at a time. Entries from invoices and credit notes are shown together. */
    public static final int TRNX_SAL_DPS_BY_ITEM_N_BP_ONE = 315;

    public static final int TRNX_PUR_BACKORDER_CON = 11;
    public static final int TRNX_PUR_BACKORDER_CON_ITEM = 12;
    public static final int TRNX_PUR_BACKORDER_CON_ITEM_BP = 13;
    public static final int TRNX_PUR_BACKORDER_CON_ITEM_BP_BRA = 14;
    public static final int TRNX_PUR_BACKORDER_ORD = 16;
    public static final int TRNX_PUR_BACKORDER_ORD_ITEM = 17;
    public static final int TRNX_PUR_BACKORDER_ORD_ITEM_BP = 18;
    public static final int TRNX_PUR_BACKORDER_ORD_ITEM_BP_BRA = 19;

    public static final int TRNX_SAL_BACKORDER_CON = 21;
    public static final int TRNX_SAL_BACKORDER_CON_ITEM = 22;
    public static final int TRNX_SAL_BACKORDER_CON_ITEM_BP = 23;
    public static final int TRNX_SAL_BACKORDER_CON_ITEM_BP_BRA = 24;
    public static final int TRNX_SAL_BACKORDER_ORD = 26;
    public static final int TRNX_SAL_BACKORDER_ORD_ITEM = 27;
    public static final int TRNX_SAL_BACKORDER_ORD_ITEM_BP = 28;
    public static final int TRNX_SAL_BACKORDER_ORD_ITEM_BP_BRA = 29;

    public static final int TRNX_STK_LOT_DEF_ID = 1;

    public static final int TRNX_TP_UNIT_TOT_QTY = 1;
    public static final int TRNX_TP_UNIT_TOT_LEN = 2;
    public static final int TRNX_TP_UNIT_TOT_SURF = 3;
    public static final int TRNX_TP_UNIT_TOT_VOL = 4;
    public static final int TRNX_TP_UNIT_TOT_MASS = 5;

    public static final int TRNX_DIOG_CST_ASIG_NA = 0;
    public static final int TRNX_DIOG_CST_ASIG_NO = 1;
    public static final int TRNX_DIOG_CST_ASIG_YES = 2;

    public static final int TRNX_DIOG_CST_TRAN_NA = 0;
    public static final int TRNX_DIOG_CST_TRAN_NO = 1;
    public static final int TRNX_DIOG_CST_TRAN_YES = 2;
    
    public static final int TRNX_OPS_TYPE_OPS_OPS = 11;             // operations
    public static final int TRNX_OPS_TYPE_OPS_OPS_APP_PREPAY = 12;  // operations - application of prepayment invoiced as discount
    public static final int TRNX_OPS_TYPE_OPS_PREPAY = 13;          // prepayment invoiced

    public static final int TRNX_OPS_TYPE_ADJ_OPS = 21;             // adjustment of operations
    public static final int TRNX_OPS_TYPE_ADJ_OPS_APP_PREPAY = 22;  // adjustment of operations - application of prepayment invoiced as discount
    public static final int TRNX_OPS_TYPE_ADJ_PREPAY = 23;          // adjustment of prepayment invoiced
    public static final int TRNX_OPS_TYPE_ADJ_APP_PREPAY = 26;      // application of prepayment invoiced
    
    public static final int MKTU_SAL_ROUTE_DEFAULT = 1;
    
    public static final HashMap<Integer, String> OperationsTypesOpsMap = new HashMap<>();
    public static final HashMap<Integer, String> OperationsTypesAdjMap = new HashMap<>();
    
    static {
        OperationsTypesOpsMap.put(TRNX_OPS_TYPE_OPS_OPS, "Operación");
        OperationsTypesOpsMap.put(TRNX_OPS_TYPE_OPS_OPS_APP_PREPAY, "Operación con aplicación anticipo facturado");
        OperationsTypesOpsMap.put(TRNX_OPS_TYPE_OPS_PREPAY, "Facturación de anticipo");
        
        OperationsTypesAdjMap.put(TRNX_OPS_TYPE_ADJ_OPS, "Ajuste operación");
        OperationsTypesAdjMap.put(TRNX_OPS_TYPE_ADJ_OPS_APP_PREPAY, "Ajuste operación con aplicación anticipo facturado");
        OperationsTypesAdjMap.put(TRNX_OPS_TYPE_ADJ_PREPAY, "Ajuste facturación de anticipo");
        OperationsTypesAdjMap.put(TRNX_OPS_TYPE_ADJ_APP_PREPAY, "Aplicación anticipo facturado");
    }

    /*
    public static final int TRN_DPS_PUR_LINK_EST_PEND = 27;
    public static final int TRN_DPS_PUR_LINK_EST_LINKED = 28;
    public static final int TRN_DPS_PUR_LINK_CON_PEND = 29;
    public static final int TRN_DPS_PUR_LINK_CON_LINKED = 30;
    public static final int TRN_DPS_PUR_LINK_ORD_PEND = 31;
    public static final int TRN_DPS_PUR_LINK_ORD_LINKED = 32;

    public static final int TRN_DPS_SAL_LINK_EST_PEND = 33;
    public static final int TRN_DPS_SAL_LINK_EST_LINKED = 34;
    public static final int TRN_DPS_SAL_LINK_CON_PEND = 35;
    public static final int TRN_DPS_SAL_LINK_CON_LINKED = 36;
    public static final int TRN_DPS_SAL_LINK_ORD_PEND = 37;
    public static final int TRN_DPS_SAL_LINK_ORD_LINKED = 38;
    */

    public static final java.lang.String getDpsTypeNameSng(final int type) {
        java.lang.String name = "";

        switch (type) {
            case TRNX_TP_DPS_EST:
                name = "Estimación";
                break;
            case TRNX_TP_DPS_EST_EST:
                name = "Cotización";
                break;
            case TRNX_TP_DPS_EST_CON:
                name = "Contrato";
                break;
            case TRNX_TP_DPS_ORD:
                name = "Pedido";
                break;
            case TRNX_TP_DPS_DOC:
                name = "Factura";
                break;
            case TRNX_TP_DPS_BOL:
                name = "Carta porte";
                break;
            case TRNX_TP_DPS_ADJ:
                name = "Nota crédito";
                break;
            default:
        }

        return name;
    }

    public static final java.lang.String getDpsTypeNamePlr(final int type) {
        java.lang.String name = "";

        switch (type) {
            case TRNX_TP_DPS_EST:
                name = "Estimaciones";
                break;
            case TRNX_TP_DPS_EST_EST:
                name = "Cotizaciones";
                break;
            case TRNX_TP_DPS_EST_CON:
                name = "Contratos";
                break;
            case TRNX_TP_DPS_ORD:
                name = "Pedidos";
                break;
            case TRNX_TP_DPS_DOC:
                name = "Facturas";
                break;
            case TRNX_TP_DPS_BOL:
                name = "Cartas porte";
                break;
            case TRNX_TP_DPS_ADJ:
                name = "Notas crédito";
                break;
            default:
        }

        return name;
    }

    public static final java.lang.String getLinkNameSng(final int type) {
        java.lang.String name = "";

        switch (type) {
            case TRNX_LINK_EST_EST_SRC:
                name = getDpsTypeNameSng(TRNX_TP_DPS_EST_EST) + " (origen)";
                break;
            case TRNX_LINK_EST_CON_SRC:
                name = getDpsTypeNameSng(TRNX_TP_DPS_EST_CON) + " (origen)";
                break;
            case TRNX_LINK_ORD_SRC:
                name = getDpsTypeNameSng(TRNX_TP_DPS_ORD) + " (origen)";
                break;
            case TRNX_LINK_ORD_DES:
                name = getDpsTypeNameSng(TRNX_TP_DPS_ORD) + " (destino)";
                break;
            case TRNX_LINK_DOC_DES:
                name = getDpsTypeNameSng(TRNX_TP_DPS_DOC) + " (destino)";
                break;
            default:
        }

        return name;
    }

    public static final java.lang.String getLinkNamePlr(final int type) {
        java.lang.String name = "";

        switch (type) {
            case TRNX_LINK_EST_EST_SRC:
                name = getDpsTypeNamePlr(TRNX_TP_DPS_EST_EST) + " (origen)";
                break;
            case TRNX_LINK_EST_CON_SRC:
                name = getDpsTypeNamePlr(TRNX_TP_DPS_EST_CON) + " (origen)";
                break;
            case TRNX_LINK_ORD_SRC:
                name = getDpsTypeNamePlr(TRNX_TP_DPS_ORD) + " (origen)";
                break;
            case TRNX_LINK_ORD_DES:
                name = getDpsTypeNamePlr(TRNX_TP_DPS_ORD) + " (destino)";
                break;
            case TRNX_LINK_DOC_DES:
                name = getDpsTypeNamePlr(TRNX_TP_DPS_DOC) + " (destino)";
                break;
            default:
        }

        return name;
    }

    public static final int MKTS_TP_PLIST_GRP_LIST_INDIV = 1;
    public static final int MKTS_TP_PLIST_GRP_LIST_LINKED = 2;

    public static final int MKTS_TP_PLIST_ARBIT = 1;
    public static final int MKTS_TP_PLIST_SRP_INC_DEC = 2;
    public static final int MKTS_TP_PLIST_PLIST_INC_DEC = 3;
    public static final int MKTS_TP_PLIST_PPUR_BENEFIT = 4;
    public static final int MKTS_TP_PLIST_PPUR_UTILITY = 5;
    public static final int MKTS_TP_PLIST_LBASE_INC_DEC = 6;

    public static final int MKTS_TP_PRICE_UDP_MAN = 1;
    public static final int MKTS_TP_PRICE_UDP_AUT = 2;

    public static final int MKTS_TP_DISC_APP_NA = 1;
    public static final int MKTS_TP_DISC_APP_PRICE_U = 2;
    public static final int MKTS_TP_DISC_APP_DISC_U = 3;

    public static final int MKTS_TP_COMMS_NA = 1;
    public static final int MKTS_TP_COMMS_PER = 2;
    public static final int MKTS_TP_COMMS_AMT_FIX_U = 3;
    public static final int MKTS_TP_COMMS_AMT_FIX = 4;
    public static final int MKTS_TP_COMMS_AMT = 5;

    public static final int MKTS_ORIG_COMMS_NA = 1;
    public static final int MKTS_ORIG_COMMS_DOC = 2;
    public static final int MKTS_ORIG_COMMS_ITEM = 3;

    public static final int MFGS_ST_ORD_NEW = 1; // new constants group for job order status (sflores, 2016-03-07)
    public static final int MFGS_ST_ORD_WEI = 2; // new constants group for job order status (sflores, 2016-03-07)
    public static final int MFGS_ST_ORD_FLR = 3; // new constants group for job order status (sflores, 2016-03-07)
    public static final int MFGS_ST_ORD_PRO = 4; // new constants group for job order status (sflores, 2016-03-07)
    public static final int MFGS_ST_ORD_FIN = 5; // new constants group for job order status (sflores, 2016-03-07)
    public static final int MFGS_ST_ORD_CLO = 6; // new constants group for job order status (sflores, 2016-03-07)

    //public static final int MFGS_ST_ORD_NEW = 1;
    public static final int MFGS_ST_ORD_LOT = 2;
    public static final int MFGS_ST_ORD_LOT_ASIG = 3;
    public static final int MFGS_ST_ORD_PROC = 4;
    public static final int MFGS_ST_ORD_END = 5;
    public static final int MFGS_ST_ORD_CLS = 6;
    public static final int MFGS_ST_ORD_DLY = 10;     // XXX 07/09/2011 Temporal comment, MFG changes pending
    public static final int MFGS_ST_ORD_PENDING = 11;   // XXX 07/09/2011 Temporal comment, MFG changes pending

    public static final int MFGU_TURN_NA = 1;
    public static final int MFGU_TURN_MOR = 2;
    public static final int MFGU_TURN_EVE = 3;
    public static final int MFGU_TURN_NIG = 4;

    public static final int MFGS_TP_REQ_REQ = 1;
    public static final int MFGS_TP_REQ_SAL = 2;

    public static final int MFGS_TP_COST_ORD = 1;
    public static final int MFGS_TP_COST_LINE = 2;
    public static final int MFGS_TP_COST_PLT = 3;
    public static final int MFGS_TP_COST_COB = 4;

    public static final int MFGS_TP_HOUR_NML = 1;
    public static final int MFGS_TP_HOUR_DBL = 2;
    public static final int MFGS_TP_HOUR_TPL = 3;

    public static final int MFGU_TP_ORD_CONTINUE = 1;
    
    public static final int MFGX_ORD_CST_DONE_NA = 0;
    public static final int MFGX_ORD_CST_DONE_NO = 1;
    public static final int MFGX_ORD_CST_DONE_YES = 2;

    public static final int VALUE_BIZ_PARTNER_TYPE = 1;
    public static final int VALUE_COMPANY_BRANCH = 1;
    public static final int VALUE_ACCOUNT_CASH = 2;
    public static final int VALUE_TYPE_DOC = 3;
    public static final int VALUE_BIZ_PARTNER = 4;
    public static final int VALUE_RECORD_ENTRY = 5;
    public static final int VALUE_CHECK = 6;
    public static final int VALUE_IS_PUR = 7;
    public static final int VALUE_TP_UNIT = 8;

    public static final int REP_FIN_ACC = 206001;
    public static final int REP_FIN_REC = 206002;
    public static final int REP_FIN_REC_CY = 206003;
    public static final int REP_FIN_RECS = 206004;
    public static final int REP_FIN_RECS_CY = 206005;
    public static final int REP_FIN_REC_ADV = 206053;
    public static final int REP_FIN_DPS_LAST_MOV = 206054;
    public static final int REP_FIN_JOURNAL_VOUCHERS = 206071;
    public static final int REP_FIN_JOURNAL_VOUCHERS_CY = 206072;
    public static final int REP_FIN_BPS_BAL = 206006;           // business partner balance (cutoff date)
    public static final int REP_FIN_BPS_BAL_PER = 206049;       // business partner balance (cutoff period)
    public static final int REP_FIN_BPS_BAL_CRED = 206050;      // business partner balance with credit (cutoff date)
    public static final int REP_FIN_BPS_BAL_DPS = 206007;       // business partner balance per document (cutoff date)
    public static final int REP_FIN_BPS_BAL_DPS_EXR = 206063;   // business partner balance per document (cutoff date) with exchange rate for revaluation
    public static final int REP_FIN_BPS_BAL_COLL = 206008;
    public static final int REP_FIN_BPS_BAL_COLL_DPS = 206009;
    public static final int REP_FIN_BPS_MOV = 206010;
    public static final int REP_FIN_ACC_CASH_BAL = 206011;
    public static final int REP_FIN_BPS_STT = 206012;
    public static final int REP_FIN_BPS_STT_DPS = 206013;
    public static final int REP_FIN_BPS_ACC_MOV = 206014;
    public static final int REP_FIN_BPS_ACC_MOV_DAY = 206015;
    public static final int REP_FIN_CASH_BAL = 206016;
    public static final int REP_FIN_CASH_MOV = 206017;
    public static final int REP_FIN_BANK_BAL = 206018;
    public static final int REP_FIN_BANK_MOV = 206019;
    public static final int REP_FIN_CASH_MOV_DET = 206020;
    public static final int REP_FIN_BANK_MOV_DET = 206021;
    public static final int REP_FIN_CASH_MOV_CON = 206022;
    public static final int REP_FIN_BANK_MOV_CON = 206023;
    public static final int REP_FIN_TAX_DBT_MOV = 206024;
    public static final int REP_FIN_TAX_CDT_MOV = 206025;
    public static final int REP_FIN_PROF_LOSS_MOV = 206026;
    public static final int REP_FIN_TRIAL_BAL = 206027;
    public static final int REP_FIN_TRIAL_BAL_DUAL = 206028;
    public static final int REP_FIN_TRIAL_BAL_MAJOR = 206029;
    public static final int REP_FIN_TRIAL_BAL_CC = 206030;
    public static final int REP_FIN_TRIAL_BAL_ITEM = 206031;
    public static final int REP_FIN_TRIAL_BAL_CC_ITEM = 206032;
    public static final int REP_FIN_STAT_BAL_SHEET = 206033;
    public static final int REP_FIN_STAT_PROF_LOSS = 206034;
    public static final int REP_FIN_ACC_CPT = 206035;
    public static final int REP_FIN_CC = 206036;
    public static final int REP_FIN_TAX_PAY_CPT = 206037;
    public static final int REP_FIN_AUX_ACC = 206038;
    public static final int REP_FIN_TAX_MOV = 206039;
    public static final int REP_FIN_CHECK_REC = 206040;
    public static final int REP_FIN_DPS_PAY = 206041;
    public static final int REP_FIN_AUX_MOV_BPS = 206042;
    public static final int REP_FIN_STA_BPS = 206046; // statement of account of business partner
    public static final int REP_FIN_BPS_ACC = 206047; // not used yet, for future use
    public static final int REP_FIN_PS_CL_ITEM = 206051;
    public static final int REP_FIN_PS_CL_ITEM_TOT = 206052;
    public static final int REP_FIN_BPS_ACC_AGI = 206061;       // business partner accounts aging
    public static final int REP_FIN_BPS_ACC_AGI_CRED = 206062;  // business partner accounts aging with credit information (credit days, credit limit, guarantee & insurance)
    public static final int REP_FIN_BPS_ACC_MOV_ORD = 206064;
    public static final int REP_FIN_EXC_DIFF = 206065;

    public static final int REP_TRN_DPS_BPS = 207001;
    public static final int REP_TRN_DPS_UNP = 207002;
    public static final int REP_TRN_DPS_UNP_CY = 207003;
    public static final int REP_TRN_STK = 207004;
    public static final int REP_TRN_STK_PERIOD = 207005;
    public static final int REP_TRN_PS = 207006;
    public static final int REP_TRN_PS_UNIT = 207007;
    public static final int REP_TRN_PS_CON = 207008;
    public static final int REP_TRN_PS_LOC = 207009;
    public static final int REP_TRN_PS_DIARY = 207010;
    public static final int REP_TRN_PS_CL_ITEM = 207011;
    public static final int REP_TRN_PS_CL_ITEM_TOT = 207012;
    public static final int REP_TRN_PS_COMP_PY = 207013;
    public static final int REP_TRN_PS_COMP_PPY = 207014;
    public static final int REP_TRN_PUR_UNIT_COST = 207015;
    public static final int REP_TRN_DPS = 207016;
    public static final int REP_TRN_DPS_US = 207036;
    public static final int REP_TRN_DPS_ADJ = 207017;
    public static final int REP_TRN_DPS_LIST = 207018;
    public static final int REP_TRN_DPS_ORDER = 207019;
    public static final int REP_TRN_CON = 207020;
    public static final int REP_TRN_CON_MOV = 207021;
    public static final int REP_TRN_BAL_AGI = 207022;
    public static final int REP_TRN_BPS_MOV_DPS = 207023;
    public static final int REP_TRN_ORD_GDS = 207024;
    public static final int REP_TRN_EST = 207025;
    public static final int REP_TRN_PS_ITEM_UNIT_PRICE = 207026;
    public static final int REP_TRN_DPS_BPS_DETAIL = 207027;
    public static final int REP_TRN_DPS_MOV = 207028;
    public static final int REP_TRN_STK_MOV = 207031;       // stock movements detailed per item
    public static final int REP_TRN_STK_MOV_MOV = 207032;   // stock movements detailed per movement
    public static final int REP_TRN_STK_MOV_SUM = 207033;   // stock movements summary
    public static final int REP_TRN_STK_MOV_SUM_SUM = 207034;   // sumary of stock movements summary
    public static final int REP_TRN_PS_CL_ITEM_TOT_COMM = 207035;
    public static final int REP_TRN_ADV = 207037;
    
    public static final int REP_TRN_CFD = 207101;
    public static final int REP_TRN_CFDI = 207102;
    public static final int REP_TRN_CFDI_33 = 207112;
    public static final int REP_TRN_CFDI_33_CRP_10 = 207113;
    public static final int REP_TRN_CFDI_ACK_CAN = 207103;
    public static final int REP_TRN_CFDI_40 = 207114;
    public static final int REP_TRN_CFDI_40_ENG = 207117;
    public static final int REP_TRN_CFDI_40_CRP_20 = 207116;
    public static final int REP_TRN_SHIP = 207104;
    public static final int REP_TRN_COMMS_ITEM = 207105;
    public static final int REP_TRN_COMMS_DPS = 207106;
    public static final int REP_TRN_CTR = 207107;
    public static final int REP_TRN_DIOG = 207108;
    public static final int REP_TRN_DPS_SHIP_ITEM = 207109;
    public static final int REP_TRN_CON_STK = 207110;
    public static final int REP_TRN_CFDI_PAYROLL_33 = 207111;
    public static final int REP_TRN_CFDI_PAYROLL_40 = 207115;

    public static final int REP_MFG_PROG_MON = 208001;
    public static final int REP_MFG_ORD = 208002;
    public static final int REP_MFG_ORD_PERFORMANCE = 208003;
    public static final int REP_MFG_BOM = 208004;
    public static final int REP_MFG_PROD = 208005;
    public static final int REP_MFG_FINISHED_GOODS_EFFICIENCY = 208006;
    public static final int REP_MFG_RAW_MATERIALS_EFFICIENCY = 208007;

    public static final int REPX_BAL_SHEET_ASSET = 1;
    public static final int REPX_BAL_SHEET_LIABTY_EQUITY = 2;
    public static final int REPX_BAL_SHEET_ORDER_DBT = 3;
    public static final int REPX_BAL_SHEET_ORDER_CDT = 4;

    public static final java.lang.String TXT_TRNS_TP_LINK_ALL = "DEFAULT";
    public static final java.lang.String TXT_DOUBLE_CLICK = "(Doble click para elegir opción)";
    public static final java.lang.String TXT_UNSIGNED = "*"; // to be used in DPS reports
    public static final java.lang.String REP_TXT_PROF_LOSS_SPA = "RESULTADO DEL EJERCICIO";
    public static final java.lang.String REP_TXT_PROF_LOSS_ENG = "PROFIT AND LOSS";

    public static final java.lang.String REF_BLANK = "<vacío>";

    public static final int NUM_LEN_FIN_REC = 6;
    public static final int NUM_LEN_DPS = 6;
    public static final int NUM_LEN_IOG = 6;
    public static final int NUM_LEN_MFG_ORD = 6;
    public static final int NUM_LEN_MFG_CHG = 3;

    public static final int UPD_DPS_FL_LINK = 1;        // set field
    public static final int UPD_DPS_FL_CLOSE = 2;       // set field
    public static final int UPD_DPS_FL_CLOSE_COMMS = 3; // set field
    public static final int UPD_DPS_FL_SHIP = 4;        // set field
    public static final int UPD_DPS_FL_DPS_ACK = 5;     // set field
    public static final int UPD_DPS_FL_AUDIT = 6;       // set field
    public static final int UPD_DPS_FL_AUTHORN = 7;     // set field

    public static final int UPD_DIOG_FL_SHIP = 1;       // set field
    public static final int UPD_DIOG_FL_AUDIT = 2;      // set field
    public static final int UPD_DIOG_FL_AUTHORN = 3;    // set field

    public static final java.lang.String[] DB_SETTINGS = new java.lang.String[] {
        "SET @CFGS_TP_SORT_KEY_NAME = " + CFGS_TP_SORT_KEY_NAME,
        "SET @CFGS_TP_SORT_NAME_KEY = " + CFGS_TP_SORT_NAME_KEY,
        "SET @FINS_TP_ACC_SYS_SUP = " + FINS_TP_ACC_SYS_SUP,
        "SET @FINS_TP_ACC_SYS_CUS = " + FINS_TP_ACC_SYS_CUS,
        "SET @FINS_TP_ACC_SYS_CDR = " + FINS_TP_ACC_SYS_CDR,
        "SET @FINS_TP_ACC_SYS_DBR = " + FINS_TP_ACC_SYS_DBR,
        "SET @FINS_TP_ACC_MOV_OPEN = " + FINS_TP_ACC_MOV_FY_OPEN,
        "SET @FINS_TP_ACC_MOV_CLOSE = " + FINS_TP_ACC_MOV_FY_CLOSE,
        "SET @FINS_CT_SYS_MOV_BPS = " + FINS_CT_SYS_MOV_BPS,
        "SET @FINS_CT_SYS_MOV_BPS_SUP = " + FINS_TP_SYS_MOV_BPS_SUP[0],
        "SET @FINS_TP_SYS_MOV_BPS_SUP = " + FINS_TP_SYS_MOV_BPS_SUP[1],
        "SET @FINS_CT_SYS_MOV_BPS_CUS = " + FINS_TP_SYS_MOV_BPS_CUS[0],
        "SET @FINS_TP_SYS_MOV_BPS_CUS = " + FINS_TP_SYS_MOV_BPS_CUS[1],
        "SET @FINS_ST_CHECK_NULL = " + TRNS_ST_DPS_ANNULED,
        "SET @BPSS_CT_BP_CO = " + BPSS_CT_BP_CO,
        "SET @BPSS_CT_BP_SUP = " + BPSS_CT_BP_SUP,
        "SET @BPSS_CT_BP_CUS = " + BPSS_CT_BP_CUS,
        "SET @BPSS_CT_BP_CDR = " + BPSS_CT_BP_CDR,
        "SET @BPSS_CT_BP_DBR = " + BPSS_CT_BP_DBR,
        "SET @TRN_DPS = " + SDataConstants.TRN_DPS,
        "SET @TRN_DIOG = " + SDataConstants.TRN_DIOG,
        "SET @TRNS_CT_DPS_PUR = " + TRNS_CT_DPS_PUR,
        "SET @TRNS_CT_DPS_SAL = " + TRNS_CT_DPS_SAL,
        "SET @ITMS_NAM_ORD_POS_QTY = " + ITMS_NAM_ORD_POS_QTY,
        "SET @ITMS_NAM_LINE_POS_QTY = " + ITMS_NAM_LINE_POS_QTY,
        "SET @ITMS_KEY_ORD_POS_QTY = " + ITMS_KEY_ORD_POS_QTY,
        "SET @ITMS_KEY_LINE_POS_QTY = " + ITMS_KEY_LINE_POS_QTY,
        "SET @ITMS_DEF_ITEM_KEY = " + ITMS_DEF_ITEM_KEY,
        "SET @ITMS_DEF_ITEM = " + ITMS_DEF_ITEM,
        "SET @ITMS_DEF_ITEM_SHORT = " + ITMS_DEF_ITEM_SHORT,
        "SET @MFGS_TP_ORD_CONTINUE = " + MFGU_TP_ORD_CONTINUE,
        "SET @MFGS_ST_ORD_NEW = " + MFGS_ST_ORD_NEW,
        "SET @MFGS_ST_ORD_LOT = " + MFGS_ST_ORD_LOT,
        "SET @MFGS_ST_ORD_LOT_ASIG = " + MFGS_ST_ORD_LOT_ASIG,
        "SET @MFGS_ST_ORD_PROCESS = " + MFGS_ST_ORD_PROC,
        "SET @MFGS_ST_ORD_END = " + MFGS_ST_ORD_END,
        "SET @MFGS_ST_ORD_CLOSE = " + MFGS_ST_ORD_CLS,
        "SET @MFGU_TURN_NA = " + MFGU_TURN_NA,
        "SET @MFGU_TURN_MOR = " + MFGU_TURN_MOR,
        "SET @MFGU_TURN_EVE = " + MFGU_TURN_EVE,
        "SET @MFGU_TURN_EVE = " + MFGU_TURN_EVE,
        "SET @TRNS_TP_LINK_ALL = " + TRNS_TP_LINK_ALL,
        "SET @TRNS_TP_LINK_CT_ITEM = " + TRNS_TP_LINK_CT_ITEM,
        "SET @TRNS_TP_LINK_CL_ITEM = " + TRNS_TP_LINK_CL_ITEM,
        "SET @TRNS_TP_LINK_TP_ITEM = " + TRNS_TP_LINK_TP_ITEM,
        "SET @TRNS_TP_LINK_IFAM = " + TRNS_TP_LINK_IFAM,
        "SET @TRNS_TP_LINK_IGRP = " + TRNS_TP_LINK_IGRP,
        "SET @TRNS_TP_LINK_IGEN = " + TRNS_TP_LINK_IGEN,
        "SET @TRNS_TP_LINK_LINE = " + TRNS_TP_LINK_LINE,
        "SET @TRNS_TP_LINK_BRD = " + TRNS_TP_LINK_BRD,
        "SET @TRNS_TP_LINK_MFR = " + TRNS_TP_LINK_MFR,
        "SET @TRNS_TP_LINK_ITEM = " + TRNS_TP_LINK_ITEM,
        "SET @TRNS_CL_DPS_SAL_ADJ = " + TRNS_CL_DPS_SAL_ADJ[1],
        "SET @MKTS_TP_COMMS_PER = " + MKTS_TP_COMMS_PER,
        "SET @UPD_DPS_FL_LINK = " + UPD_DPS_FL_LINK,
        "SET @UPD_DPS_FL_CLOSE = " + UPD_DPS_FL_CLOSE,
        "SET @UPD_DPS_FL_CLOSE_COMMS = " + UPD_DPS_FL_CLOSE_COMMS,
        "SET @UPD_DPS_FL_SHIP = " + UPD_DPS_FL_SHIP,
        "SET @UPD_DPS_FL_DPS_ACK = " + UPD_DPS_FL_DPS_ACK,
        "SET @UPD_DPS_FL_AUDIT = " + UPD_DPS_FL_AUDIT,
        "SET @UPD_DPS_FL_AUTHORN = " + UPD_DPS_FL_AUTHORN,
        "SET @UPD_DIOG_FL_SHIP = " + UPD_DIOG_FL_SHIP,
        "SET @UPD_DIOG_FL_AUDIT = " + UPD_DIOG_FL_AUDIT,
        "SET @UPD_DIOG_FL_AUTHORN = " + UPD_DIOG_FL_AUTHORN,
        "SET @TRNS_ST_DPS_NEW = " + TRNS_ST_DPS_NEW,
        "SET @TRNS_ST_DPS_EMITED = " + TRNS_ST_DPS_EMITED,
        "SET @TRNS_ST_DPS_ANNULED = " + TRNS_ST_DPS_ANNULED,
        "SET @PROC_UPDATE_DELETE = 1",
        "SET @PROC_UPDATE_SORT_POS = 2",
        "SET @PROC_UPDATE_CLEAR_FK = 3",
        "SET @PROC_UNKOWN_OPTION = 1"
    };
}
