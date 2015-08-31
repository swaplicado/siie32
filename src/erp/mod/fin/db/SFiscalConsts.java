/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.db;

/**
 *
 * @author Sergio Flores
 */
public abstract class SFiscalConsts {

    public static final int YEAR_MIN = 2015;
    public static final int YEAR_MAX = 2099;

    public static final String COA_DBT = "D";               // chart of accounts, debit
    public static final String COA_CDT = "A";               // chart of accounts, credit
    public static final String TRS_NOR = "N";               // trial sheet, normal
    public static final String TRS_CMP = "C";               // trial sheet, complementary
    public static final String JOV_REQ_TP_AF = "AF";        // trial sheet, complementary
    public static final String JOV_REQ_TP_FC = "FC";        // trial sheet, complementary
    public static final String JOV_REQ_TP_DE = "DE";        // trial sheet, complementary
    public static final String JOV_REQ_TP_CO = "CO";        // trial sheet, complementary

    public static final String XML_COA = "CT";              // chart of accounts
    public static final String XML_TRS = "B";               // trial sheet
    public static final String XML_TRS_NOR = "N";           // trial sheet "normal"
    public static final String XML_TRS_SUB = "C";           // trial sheet "complementaria"
    public static final String XML_JOV = "PL";              // journal voucher
    public static final String XML_DEC = "XF";              // detailed CFDI
    public static final String XML_DEL = "XC";              // detailed ledger

    public static final String ACC_CSH_CSH = "101";                 // Caja
    public static final String ACC_CSH_CSH_CSH = "101.01";

    public static final String ACC_CSH_BNK = "102";                 // Bancos
    public static final String ACC_CSH_BNK_DOM = "102.01";
    public static final String ACC_CSH_BNK_INT = "102.02";

    public static final String ACC_BPR_CUS = "105";                 // Clientes
    public static final String ACC_BPR_CUS_DOM = "105.01";
    public static final String ACC_BPR_CUS_INT = "105.02";
    public static final String ACC_BPR_CUS_REL_DOM = "105.03";
    public static final String ACC_BPR_CUS_REL_INT = "105.04";

    public static final String ACC_BPR_DBR = "107";                 // Deudores diversos
    public static final String ACC_BPR_DBR_EMP = "107.01";
    public static final String ACC_BPR_DBR_SHH = "107.02";
    public static final String ACC_BPR_DBR_REL_DOM = "107.03";
    public static final String ACC_BPR_DBR_REL_INT = "107.04";
    public static final String ACC_BPR_DBR_OTH = "107.05";

    public static final String ACC_BPR_SUP_ADV = "120";             // Anticipo a proveedores
    public static final String ACC_BPR_SUP_ADV_DOM = "120.01";
    public static final String ACC_BPR_SUP_ADV_INT = "120.02";
    public static final String ACC_BPR_SUP_ADV_REL_DOM = "120.03";
    public static final String ACC_BPR_SUP_ADV_REL_INT = "120.04";

    public static final String ACC_BPR_SUP = "201";                 // Proveedores
    public static final String ACC_BPR_SUP_DOM = "201.01";
    public static final String ACC_BPR_SUP_INT = "201.02";
    public static final String ACC_BPR_SUP_REL_DOM = "201.03";
    public static final String ACC_BPR_SUP_REL_INT = "201.04";

    public static final String ACC_BPR_CDR_ST = "205";              // Acreedores diversos cto. plazo
    public static final String ACC_BPR_CDR_ST_SHH = "205.01";
    public static final String ACC_BPR_CDR_ST_DOM = "205.02";
    public static final String ACC_BPR_CDR_ST_INT = "205.03";
    public static final String ACC_BPR_CDR_ST_REL_DOM = "205.04";
    public static final String ACC_BPR_CDR_ST_REL_INT = "205.05";
    //public static final String ACC_BPR_CDR_ST_OTH = "205.06";     // option discarted

    public static final String ACC_BPR_CUS_ADV = "206";             // Anticipo de clientes
    public static final String ACC_BPR_CUS_ADV_DOM = "206.01";
    public static final String ACC_BPR_CUS_ADV_INT = "206.02";
    public static final String ACC_BPR_CUS_ADV_REL_DOM = "206.03";
    public static final String ACC_BPR_CUS_ADV_REL_INT = "206.04";
    //public static final String ACC_BPR_CUS_ADV_OTH = "206.05";    // option discarted

    public static final String ACC_BPR_CDR_LT = "251";              // Acreedores diversos lgo. plazo
    public static final String ACC_BPR_CDR_LT_SHH = "251.01";
    public static final String ACC_BPR_CDR_LT_DOM = "251.02";
    public static final String ACC_BPR_CDR_LT_INT = "251.03";
    public static final String ACC_BPR_CDR_LT_REL_DOM = "251.04";
    public static final String ACC_BPR_CDR_LT_REL_INT = "251.05";
    //public static final String ACC_BPR_CDR_LT_OTH = "251.06";     // option discarted
}
