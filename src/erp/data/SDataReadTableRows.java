/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.data;

import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableRow;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import erp.server.SQueryRequest;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibUtils;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDataReadTableRows {

    private static SQueryRequest getSettingsCatCfg(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";

        switch (pnDataType) {
            case SDataConstants.CFGU_CUR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cur");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cur");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cur_key");

                sSql = "SELECT id_cur, cur, cur_key " +
                        "FROM erp.cfgu_cur WHERE b_del = 0 " +
                        "ORDER BY cur, id_cur ";
                break;

            case SDataConstants.CFGU_CO:
                String sqlBetween = "";

                if (filterKey != null) {
                    sqlBetween = "(";
                    for (int j = 0; j < ((Vector)filterKey).size(); j++) {
                        sqlBetween = sqlBetween + ((Vector)filterKey).get(j);
                    }
                    sqlBetween = sqlBetween + ")";
                }

                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_co");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "co");

                sSql = "SELECT id_co, co " +
                        "FROM erp.cfgu_co WHERE b_del = 0 " +
                        (filterKey == null ? "" : " AND id_co IN" + sqlBetween) + " " +
                        "ORDER BY co, id_co ";
                break;

            case SDataConstants.CFGU_COB_ENT:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cob");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ent");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ent");

                sSql = "SELECT id_cob, id_ent, ent " +
                        "FROM erp.cfgu_cob_ent WHERE b_del = 0 " +
                        (filterKey == null ? "" : "AND id_cob = " + ((int[]) filterKey)[0] + " AND fid_ct_ent = " + ((int[]) filterKey)[1] + " " +
                        (((int[]) filterKey).length > 2 ? " AND fid_tp_ent = " + ((int[]) filterKey)[2] : "" ) ) + " " +
                        "ORDER BY id_cob, ent, id_ent ";
                break;

            case SDataConstants.CFGU_LAN:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_lan");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "lan");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "lan_key");

                sSql = "SELECT id_lan, lan, lan_key " +
                        "FROM erp.cfgu_lan WHERE b_del = 0 " +
                        "ORDER BY lan, id_lan ";
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatUsr(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";

        switch (pnDataType) {
            case SDataConstants.USRU_USR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_usr");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "usr");

                sSql = "SELECT id_usr, usr " +
                        "FROM erp.usru_usr WHERE b_del = 0 " +
                        "ORDER BY usr, id_usr ";
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatLoc(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";

        switch (pnDataType) {
            case SDataConstants.LOCU_CTY:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cty");

                i = 0;
                aoQueryFields = new STableField[3];
                if (piClient.getSessionXXX().getParamsErp().getFkSortingLocalityTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cty_key");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cty");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cty_abbr");
                }
                else {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cty");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cty_abbr");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cty_key");
                }

                sSql = "SELECT id_cty, cty_key, cty, cty_abbr " +
                        "FROM erp.locu_cty WHERE b_del = 0 " +
                        "ORDER BY " + (piClient.getSessionXXX().getParamsErp().getFkSortingLocalityTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "cty_key, cty, " : "cty, cty_key, ") + "id_cty ";
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatBps(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        int category = 0;
        int sortingType = 0;
        String field = "";
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";

        switch (pnDataType) {
            case SDataConstants.BPSS_CT_BP:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ct_bp");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct_bp");

                sSql = "SELECT id_ct_bp, ct_bp " +
                        "FROM erp.bpss_ct_bp WHERE b_del = 0 AND id_ct_bp <> " + SDataConstantsSys.BPSS_CT_BP_CO + " " +
                        "ORDER BY id_ct_bp ";
                break;

            case SDataConstants.BPSS_TP_BP_IDY:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_bp_idy");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_bp_idy");

                sSql = "SELECT id_tp_bp_idy, tp_bp_idy " +
                        "FROM erp.bpss_tp_bp_idy WHERE b_del = 0 ORDER BY id_tp_bp_idy ";
                break;

             case SDataConstants.BPSS_TP_CON:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_con");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_con");

                sSql = "SELECT id_tp_con, tp_con " +
                        "FROM erp.bpss_tp_con WHERE b_del = 0 ORDER BY id_tp_con ";
                break;

            case SDataConstants.BPSU_BP:
            case SDataConstants.BPSX_BP_ATT_BANK:
            case SDataConstants.BPSX_BP_ATT_CARR:
                int attribute = SDataConstantsSys.UNDEFINED;

                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bp.id_bp");

                i = 0;
                aoQueryFields = new STableField[4];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_bp_idy.tp_bp_idy");

                if (pnDataType == SDataConstants.BPSX_BP_ATT_BANK) {
                    attribute = SDataConstantsSys.BPSS_TP_BP_ATT_BANK;
                }
                else if (pnDataType == SDataConstants.BPSX_BP_ATT_CARR) {
                    attribute = SDataConstantsSys.BPSS_TP_BP_ATT_CARR;
                }

                sSql = "SELECT bp.id_bp, bp.bp, bp.bp_comm, bp.fiscal_id, tp_bp_idy.tp_bp_idy " +
                        "FROM erp.bpsu_bp AS bp " +
                        "INNER JOIN erp.bpss_tp_bp_idy AS tp_bp_idy ON " +
                        "bp.fid_tp_bp_idy = tp_bp_idy.id_tp_bp_idy " +
                        (filterKey == null ? "" : "" +
                        "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + ((int[]) filterKey)[0] + " ") +
                        "WHERE bp.b_del = 0 " + (attribute == SDataConstantsSys.UNDEFINED ? "" :
                        (attribute == SDataConstantsSys.BPSS_TP_BP_ATT_BANK ? " AND bp.b_att_bank = 1 " : attribute == SDataConstantsSys.BPSS_TP_BP_ATT_CARR ? " AND bp.b_att_car = 1  "
                        + " " : "")) +
                        "ORDER BY bp.bp, bp.id_bp ";
                break;

            case SDataConstants.BPSU_BPB:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bpb.id_bpb");

                i = 0;
                aoQueryFields = new STableField[3];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bpb.bpb");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_bpb.tp_bpb");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tax_reg.tax_reg");

                sSql = "SELECT bpb.id_bpb, bpb.bpb, tp_bpb.tp_bpb, tax_reg.tax_reg " +
                        "FROM erp.bpsu_bpb AS bpb " +
                        "INNER JOIN erp.bpss_tp_bpb AS tp_bpb ON " +
                        "bpb.fid_tp_bpb = tp_bpb.id_tp_bpb " +
                        "LEFT OUTER JOIN erp.finu_tax_reg AS tax_reg ON " +
                        "bpb.fid_tax_reg_n = tax_reg.id_tax_reg " +
                        "WHERE bpb.b_del = 0 " +
                        (filterKey == null ? "" : "AND bpb.fid_bp = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY bpb.bpb, bpb.id_bpb ";
                break;

            case SDataConstants.BPSU_BPB_ADD:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_bpb");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_add");

                i = 0;
                aoQueryFields = new STableField[11];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ta.tp_add");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.bpb_add");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.street");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_street_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.neighborhood");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.zip_code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.po_box");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.locality");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.county");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.state");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_cty_abbr");

                sSql = "SELECT a.id_bpb, a.id_add, a.bpb_add, a.street, RTRIM(CONCAT(a.street_num_ext, ' ', a.street_num_int)) AS f_street_num, a.neighborhood, " +
                        "a.zip_code, a.po_box, a.fid_tp_add, ta.tp_add, a.locality, a.county, a.state, COALESCE(cty.cty_abbr, '" + piClient.getSession().getSessionCustom().getLocalCountryCode() + "') AS f_cty_abbr " +
                        "FROM erp.bpsu_bpb_add AS a " +
                        "INNER JOIN erp.bpss_tp_add AS ta ON " +
                        "a.fid_tp_add = ta.id_tp_add " +
                        (filterKey == null ? "" : "AND a.id_bpb = " + ((int[]) filterKey)[0] + " ") +
                        "LEFT OUTER JOIN erp.locu_cty AS cty ON " +
                        "a.fid_cty_n = cty.id_cty " +
                        "WHERE a.b_del = 0 " +
                        "ORDER BY a.id_bpb, a.fid_tp_add, a.bpb_add, a.street, a.street_num_ext, a.street_num_int ";
                break;

            case SDataConstants.BPSU_BANK_ACC:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bpb");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bank_acc");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bank_acc");

                sSql = "SELECT id_bpb, id_bank_acc, bank_acc " +
                        "FROM erp.bpsu_bank_acc WHERE b_del = 0 " +
                        (filterKey == null ? "" : "AND id_bpb = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY id_bpb, bank_acc, id_bank_acc ";
                break;

            case SDataConstants.BPSU_TP_BP:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ct_bp");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_bp");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_bp");

                sSql = "SELECT id_ct_bp, id_tp_bp, tp_bp FROM erp.bpsu_tp_bp " +
                        "WHERE b_del = 0 " +
                        (filterKey == null ? "" : "AND id_ct_bp = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY id_ct_bp, tp_bp, id_tp_bp ";
                break;

            case SDataConstants.BPSX_BP_SUP:
            case SDataConstants.BPSX_BP_CUS:
            case SDataConstants.BPSX_BP_CDR:
            case SDataConstants.BPSX_BP_DBR:
                switch (pnDataType) {
                    case SDataConstants.BPSX_BP_SUP:
                        category = SDataConstantsSys.BPSS_CT_BP_SUP;
                        sortingType = piClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId();
                        break;
                    case SDataConstants.BPSX_BP_CUS:
                        category = SDataConstantsSys.BPSS_CT_BP_CUS;
                        sortingType = piClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId();
                        break;
                    case SDataConstants.BPSX_BP_CDR:
                        category = SDataConstantsSys.BPSS_CT_BP_CDR;
                        sortingType = piClient.getSessionXXX().getParamsErp().getFkSortingCreditorTypeId();
                        break;
                    case SDataConstants.BPSX_BP_DBR:
                        category = SDataConstantsSys.BPSS_CT_BP_DBR;
                        sortingType = piClient.getSessionXXX().getParamsErp().getFkSortingDebtorTypeId();
                        break;
                    default:
                }

                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bp.id_bp");

                i = 0;
                aoQueryFields = new STableField[4];
                if (sortingType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm");
                    sSql = "ct.bp_key, bp.bp, bp.bp_comm, ";
                }
                else if (sortingType == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME_COMM) {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                    sSql = "ct.bp_key, bp.bp_comm, bp.bp, ";
                }
                else {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp_comm");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                    sSql = "bp.bp, bp.bp_comm, ct.bp_key, ";
                }
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.fiscal_id");

                sSql = "SELECT bp.id_bp, bp.bp, bp.bp_comm, bp.fiscal_id, ct.bp_key " +
                        "FROM erp.bpsu_bp AS bp " +
                        "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
                        "bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + category + " AND bp.b_del = 0 AND ct.b_del = 0 " +
                        "ORDER BY " + sSql + "bp.id_bp ";
                break;

            case SDataConstants.BPSX_BP_X_SUP_CUS:
            case SDataConstants.BPSX_BP_X_CDR_DBR:
                switch (pnDataType) {
                    case SDataConstants.BPSX_BP_X_SUP_CUS:
                        field = "AND (b_sup = 1 OR b_cus = 1) ";
                        break;
                    case SDataConstants.BPSX_BP_X_CDR_DBR:
                        field = "AND (b_cdr = 1 OR b_dbr = 1) ";
                        break;
                    default:
                }

                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bp");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp");

                sSql = "SELECT bp.id_bp, bp.bp " +
                        "FROM erp.bpsu_bp AS bp " +
                        "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
                        "bp.id_bp = ct.id_bp AND ct.id_ct_bp IN(" + (pnDataType == SDataConstants.BPSX_BP_X_SUP_CUS ? SDataConstantsSys.BPSS_CT_BP_SUP + ", " + SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_CDR + ", " + SDataConstantsSys.BPSS_CT_BP_DBR) + ") " +
                        "WHERE bp.b_del = 0 AND ct.b_del = 0 " + field +
                        "ORDER BY bp.bp, bp.id_bp ";
                break;

            case SDataConstants.BPSX_BP_EMP:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bp");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp");

                sSql = "SELECT DISTINCT id_bp, bp " +
                        "FROM erp.bpsu_bp " +
                        "WHERE b_del = 0 AND b_att_emp = 1 " +
                        "ORDER BY bp, id_bp ";
                break;

            case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bp");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp");

                sSql = "SELECT DISTINCT id_bp, bp " +
                        "FROM erp.bpsu_bp " +
                        "WHERE b_del = 0 AND b_att_sal_agt = 1 " +
                        "ORDER BY bp, id_bp ";
                break;

            case SDataConstants.BPSX_BP_ATT_EMP_MFG:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bp");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp");

                sSql = "SELECT DISTINCT bp.id_bp, bp.bp " +
                        "FROM erp.bpsu_bp AS bp " +
                        "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND bp.b_del = 0 " +
                        "INNER JOIN erp.hrsu_emp AS e ON bp.id_bp = e.id_emp AND e.b_mfg_ope = 1 " +
                        "ORDER BY bp.bp, bp.id_bp ";
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatItm(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";
        java.sql.Date oDate = null;
        boolean bInventoriableNot = false;
        boolean bInventoriableOnly = false;

        if (params != null) {
            bInventoriableNot = params.get(SLibConstants.VALUE_INV_NOT) == null ? false : (Boolean) params.get(SLibConstants.VALUE_INV_NOT);
            bInventoriableOnly = params.get(SLibConstants.VALUE_INV_ONLY) == null ? false : (Boolean) params.get(SLibConstants.VALUE_INV_ONLY);
        }

        switch (pnDataType) {
            case SDataConstants.ITMS_CT_ITEM:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ct_item");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct_item");

                sSql = "SELECT id_ct_item, ct_item " +
                        "FROM erp.itms_ct_item WHERE b_del = 0 ORDER BY id_ct_item ";
                break;

            case SDataConstants.ITMS_CL_ITEM:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cl.id_ct_item");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cl.id_cl_item");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.ct_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cl.cl_item");

                sSql = "SELECT cl.id_ct_item, cl.id_cl_item, cl.cl_item, ct.id_ct_item, ct.ct_item " +
                        "FROM erp.itms_cl_item AS cl " +
                        "INNER JOIN erp.itms_ct_item AS ct ON cl.id_ct_item = ct.id_ct_item " +
                        (filterKey == null ? "" : "AND cl.id_ct_item = " + ((int[]) filterKey)[0] + " ") +
                        "WHERE cl.b_del = 0 ORDER BY cl.id_ct_item, cl.id_cl_item ";
                break;

            case SDataConstants.ITMS_TP_ITEM:
                aoPkFields = new STableField[3];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "tp.id_ct_item");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "tp.id_cl_item");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "tp.id_tp_item");

                i = 0;
                aoQueryFields = new STableField[3];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.ct_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cl.cl_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp.tp_item");

                sSql = "SELECT tp.id_ct_item, tp.id_cl_item, tp.id_tp_item, tp.tp_item, cl.cl_item, ct.ct_item " +
                        "FROM erp.itms_tp_item AS tp " +
                        "INNER JOIN erp.itms_ct_item AS ct ON tp.id_ct_item = ct.id_ct_item " +
                        "INNER JOIN erp.itms_cl_item AS cl ON tp.id_ct_item = cl.id_ct_item AND tp.id_cl_item = cl.id_cl_item " +
                        (filterKey == null ? "" : "AND tp.id_ct_item = " + ((int[]) filterKey)[0] + " AND tp.id_cl_item = " + ((int[]) filterKey)[1] + " ") +
                        "WHERE tp.b_del = 0 ORDER BY tp.id_ct_item, tp.id_cl_item, tp.id_tp_item ";
                break;

            case SDataConstants.ITMU_IFAM:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ifam");

                i = 0;
                aoQueryFields = new STableField[7];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ifam");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "b_free_price");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "b_free_disc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "b_free_disc_u");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "b_free_disc_ety");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "b_free_disc_doc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "b_free_comms");

                sSql = "SELECT id_ifam, ifam, b_free_price, b_free_disc, b_free_disc_u, b_free_disc_ety, b_free_disc_doc, b_free_comms FROM erp.itmu_ifam " +
                        "WHERE b_del = 0 " +
                        "ORDER BY ifam, id_ifam ";
                break;

            case SDataConstants.ITMU_IGRP:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "igrp.id_igrp");

                i = 0;
                aoQueryFields = new STableField[8];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ifam.ifam");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "igrp.igrp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_price");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_disc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_disc_u");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_disc_ety");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_disc_doc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igrp.b_free_comms");

                sSql = "SELECT igrp.id_igrp, igrp.igrp, igrp.b_free_price, igrp.b_free_disc, igrp.b_free_disc_u, igrp.b_free_disc_ety, igrp.b_free_disc_doc, " +
                        "igrp.b_free_comms, ifam.ifam FROM erp.itmu_igrp AS igrp " +
                        "INNER JOIN erp.itmu_ifam AS ifam ON " +
                        "igrp.fid_ifam = ifam.id_ifam " +
                        "WHERE igrp.b_del = 0 " +
                        (filterKey == null ? "" : "AND igrp.fid_ifam = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY ifam.ifam, igrp.igrp, igrp.id_igrp ";
                break;

            case SDataConstants.ITMU_IGEN:
            case SDataConstants.ITMX_IGEN_LINE:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "igen.id_igen");

                i = 0;
                aoQueryFields = new STableField[8];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_igen");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "igrp.igrp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igen.b_free_price");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igen.b_free_disc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igen.b_free_disc_u");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igen.b_free_disc_ety");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igen.b_free_disc_doc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "igen.b_free_comms");

                sSql = "SELECT igen.id_igen, CONCAT(igen.igen, ' (', igen.code, ')') AS f_igen, igen.b_free_price, igen.b_free_disc, igen.b_free_disc_u, igen.b_free_disc_ety, " +
                        "igen.b_free_disc_doc, igen.b_free_comms, igrp.igrp " +
                        "FROM erp.itmu_igen AS igen " +
                        "INNER JOIN erp.itmu_igrp AS igrp ON " +
                        "igen.fid_igrp = igrp.id_igrp " +
                        "WHERE igen.b_del = 0 " + (pnDataType != SDataConstants.ITMX_IGEN_LINE ? "" : "AND b_line = 1 ") + " " +
                        (filterKey == null ? "" :
                        ((int[]) filterKey).length == 1 ? "AND fid_ct_item = " + ((int[]) filterKey)[0] :
                        "AND fid_ct_item = " + ((int[]) filterKey)[0] + " AND (fid_cl_item = " + ((int[]) filterKey)[1]  +
                        (SLibUtilities.compareKeys(filterKey, SDataConstantsSys.ITMS_CL_ITEM_EXP_MFG) ? " OR fid_cl_item = " +
                        SDataConstantsSys.ITMS_CL_ITEM_EXP_OPE[1] : "") + ")") + " " +
                        "ORDER BY igen.igen, igen.id_igen ";
                break;

            case SDataConstants.ITMU_LINE:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_line");

                i = 0;
                aoQueryFields = new STableField[8];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "line");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "igen");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "brd");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "mfr");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "emt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "v1");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "v2");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "v3");

                sSql = "SELECT l.id_line AS id_line, CONCAT(l.line, ' (', l.code, ')') AS line, ig.igen AS igen, brd.brd AS brd, mfr.mfr AS mfr, " +
                        "emt.emt AS emt, v1.tp_var AS v1, v2.tp_var AS v2, v3.tp_var AS v3 " +
                        "FROM erp.itmu_line AS l " +
                        "INNER JOIN erp.itmu_igen ig ON " +
                        "l.fid_igen = ig.id_igen " +
                        "INNER JOIN erp.itmu_brd brd ON " +
                        "l.fid_brd = brd.id_brd " +
                        "INNER JOIN erp.itmu_mfr mfr ON " +
                        "l.fid_mfr = mfr.id_mfr " +
                        "INNER JOIN erp.itmu_emt emt ON " +
                        "l.fid_emt = emt.id_emt " +
                        "INNER JOIN erp.itmu_tp_var v1 ON " +
                        "l.fid_tp_var_01 = v1.id_tp_var " +
                        "INNER JOIN erp.itmu_tp_var v2 ON " +
                        "l.fid_tp_var_02 = v2.id_tp_var " +
                        "INNER JOIN erp.itmu_tp_var v3 ON " +
                        "l.fid_tp_var_03 = v3.id_tp_var " +
                        "WHERE l.b_del = 0 " + (filterKey == null ? "" : "AND l.fid_igen = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY l.line, l.id_line, ig.igen ";
                break;

            case SDataConstants.ITMU_ITEM:
            case SDataConstants.ITMX_ITEM_BOM_ITEM:
            case SDataConstants.ITMX_ITEM_BOM_LEVEL:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "i.id_item");

                i = 0;
                aoQueryFields = new STableField[3];
                if (piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                }
                else {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                }
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "si.name");

                sSql = "SELECT i.id_item, i.item, i.item_key, si.name " +
                        "FROM erp.itmu_item AS i " +
                        "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " + (filterKey == null ? "" : "AND ig.fid_ct_item = " + ((int[]) filterKey)[0]) + " " +
                        (!bInventoriableNot ? "" : "AND i.b_inv = 0 ") +
                        (!bInventoriableOnly ? "" : "AND i.b_inv = 1 ") +
                        "INNER JOIN erp.itms_st_item AS si ON i.fid_st_item = si.id_st_item " +
                        "WHERE i.b_del = 0 ";

                switch (pnDataType) {
                    case SDataConstants.ITMX_ITEM_BOM_ITEM:
                        sSql += "AND ((" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG[2] + ") OR (" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[2] + ")) ";
                        break;
                    case SDataConstants.ITMX_ITEM_BOM_LEVEL:
                        sSql += "AND ((" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMD[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMD[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMD[2] + ") OR (" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMP[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMP[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_PUR_CON_RMP[2] + ") OR (" +
                                "ig.fid_ct_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[0] + " AND " +
                                "ig.fid_cl_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[1] + " AND " +
                                "ig.fid_tp_item = " + SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP[2] + ")) ";
                        break;
                    default:
                }

                if (filterKey != null) {
                    switch (((int[]) filterKey).length) {
                        case 1:
                            sSql += "AND ig.fid_ct_item = " + ((int[]) filterKey)[0] + " ";
                            break;
                        case 2:
                            sSql += "AND ig.fid_ct_item = " + ((int[]) filterKey)[0] + " ";
                            sSql += "AND ig.fid_cl_item = " + ((int[]) filterKey)[1] + " ";
                            break;
                        case 3:
                            sSql += "AND ig.fid_ct_item = " + ((int[]) filterKey)[0] + " ";
                            sSql += "AND ig.fid_cl_item = " + ((int[]) filterKey)[1] + " ";
                            sSql += "AND ig.fid_tp_item = " + ((int[]) filterKey)[2] + " ";
                            break;
                        default:
                    }
                }

                sSql += "ORDER BY " + (!piClient.getSessionXXX().getParamsErp().getIsItemKeyApplying() ? "i.item, " :
                    piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") + "i.id_item ";
                break;

            case SDataConstants.ITMU_CFG_ITEM_BP:
                aoPkFields = new STableField[3];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_item");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bp");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cfg");

                i = 0;
                aoQueryFields = new STableField[3];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "item_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "symbol");

                sSql = "SELECT id_item, id_bp, id_cfg, item_key, item, symbol FROM erp.itmu_cfg_item_bp AS i " +
                        "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
                        "WHERE i.b_del = 0 " +
                        (filterKey == null ? "" : "AND id_item = " + ((int[]) filterKey)[0] + " AND id_bp = " + ((int[]) filterKey)[1] + " ") +
                        "ORDER BY item_key, item ";

                break;

            case SDataConstants.ITMU_TP_UNIT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_unit");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_unit");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "unit_base");

                sSql = "SELECT id_tp_unit, tp_unit, unit_base FROM erp.itmu_tp_unit " +
                        "WHERE b_del = 0 " +
                        "ORDER BY tp_unit, id_tp_unit ";
                break;

            case SDataConstants.ITMU_UNIT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_unit");

                i = 0;
                aoQueryFields = new STableField[3];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "unit");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "symbol");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "unit_base_equiv");

                sSql = "SELECT id_unit, unit, symbol, unit_base_equiv " +
                        "FROM erp.itmu_unit " +
                        "WHERE b_del = 0 " +
                        (filterKey == null ? "" : "AND fid_tp_unit = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY fid_tp_unit, sort_pos, unit, id_unit ";

                break;

            case SDataConstants.ITMU_TP_VAR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_var");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_var");

                sSql = "SELECT id_tp_var, tp_var FROM erp.itmu_tp_var " +
                        "WHERE b_del = 0 " +
                        "ORDER BY tp_var, id_tp_var ";
                break;

            case SDataConstants.ITMU_TP_BRD:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_brd");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_brd");

                sSql = "SELECT id_tp_brd, tp_brd FROM erp.itmu_tp_brd " +
                        "WHERE b_del = 0 " +
                        "ORDER BY tp_brd, id_tp_brd ";
                break;

            case SDataConstants.ITMU_TP_EMT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_emt");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_emt");

                sSql = "SELECT id_tp_emt, tp_emt FROM erp.itmu_tp_emt " +
                        "WHERE b_del = 0 " +
                        "ORDER BY tp_emt, id_tp_emt ";
                break;

            case SDataConstants.ITMU_TP_MFR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_mfr");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_mfr");

                sSql = "SELECT id_tp_mfr, tp_mfr FROM erp.itmu_tp_mfr " +
                        "WHERE b_del = 0 " +
                        "ORDER BY tp_mfr, id_tp_mfr ";
                break;

            case SDataConstants.ITMU_VAR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_var");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "var");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "code");

                sSql = "SELECT id_var, var, code " +
                        "FROM erp.itmu_var " +
                        "WHERE b_del = 0 AND fid_tp_var = " + ((int[]) filterKey)[0] + " " +
                        "ORDER BY fid_tp_var, v.var, id_var ";
                break;

            case SDataConstants.ITMU_BRD:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_brd");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_brd");

                sSql = "SELECT id_brd, CONCAT(brd, ' (', code, ')') AS f_brd " +
                        "FROM erp.itmu_brd " +
                        "WHERE b_del = 0 " +
                        "ORDER BY fid_tp_brd, brd, id_brd  ";
                break;

            case SDataConstants.ITMU_MFR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_mfr");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_mfr");

                sSql = "SELECT id_mfr, CONCAT(mfr, ' (', code, ')') AS f_mfr " +
                        "FROM erp.itmu_mfr " +
                        "WHERE b_del = 0 " +
                        "ORDER BY fid_tp_mfr, mfr, id_mfr ";
                break;

            case SDataConstants.ITMU_EMT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_emt");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_emt");

                sSql = "SELECT id_emt, CONCAT(emt, ' (', code, ')') AS f_emt " +
                        "FROM erp.itmu_emt " +
                        "WHERE b_del = 0 " +
                        "ORDER BY fid_tp_emt, emt, id_emt ";
                break;

            case SDataConstants.ITMX_ITEM_IOG:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "i.id_item");

                i = 0;
                if (filterKey == null || filterKey instanceof int[]) {
                    aoQueryFields = new STableField[8];
                }
                else {
                    aoQueryFields = new STableField[9];
                }

                if (piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                }
                else {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                }
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "u.symbol");
                if (filterKey != null && filterKey instanceof Object[]) {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "stock");
                }
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.brd");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "m.mfr");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "icl.id_ct_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "icl.id_cl_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "icl.cl_item");

                if (filterKey != null && filterKey instanceof Object[] && ((Object[]) filterKey)[1] != null) {
                    oDate = new java.sql.Date(((java.util.Date)((Object[]) filterKey)[1]).getTime());
                }
                sSql = "SELECT i.id_item, i.item, i.item_key, u.symbol, b.brd, m.mfr, icl.id_ct_item, icl.id_cl_item, icl.cl_item " +
                        ((filterKey == null || filterKey instanceof int[]) ? "" : (", trn_stk_get(" +
                        ((filterKey == null || filterKey instanceof int[]) ? piClient.getSessionXXX().getWorkingYear() : (Integer)((Object[]) filterKey)[0]) + ", " +
                        "i.id_item, " +
                        ((filterKey == null || filterKey instanceof int[]) ? (Integer)((Object[]) filterKey)[2] == 0 ? " i.fid_unit" : (Integer)((Object[]) filterKey)[2] : " i.fid_unit") + ", " +
                        "NULL, " +  // lot
                        ((filterKey == null || filterKey instanceof int[]) ? " null" : (Integer)((Object[]) filterKey)[3]) + ", " +
                        ((filterKey == null || filterKey instanceof int[]) ? " null" : (Integer)((Object[]) filterKey)[4]) + ", " +
                        ((filterKey == null || filterKey instanceof int[]) ? " null" : "'" + oDate + "'") +  " " +
                        ") AS stock ")) +
                        "FROM erp.itmu_item AS i " +
                        "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
                        (!bInventoriableNot ? "" : "AND i.b_inv = 0 ") +
                        (!bInventoriableOnly ? "" : "AND i.b_inv = 1 ") +
                        "INNER JOIN erp.itmu_brd AS b ON i.fid_brd = b.id_brd " +
                        "INNER JOIN erp.itmu_mfr AS m ON i.fid_mfr = m.id_mfr " +
                        "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                        "INNER JOIN erp.itms_cl_item AS icl ON ig.fid_ct_item = icl.id_ct_item AND ig.fid_cl_item = icl.id_cl_item " +
                        "WHERE i.b_del = 0 " +
                        "ORDER BY " + (piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") + "i.id_item ";
                break;

            case SDataConstants.ITMX_ITEM_BY_KEY:
            case SDataConstants.ITMX_ITEM_BY_NAME:
            case SDataConstants.ITMX_ITEM_BY_BRAND:
            case SDataConstants.ITMX_ITEM_BY_MANUFACTURER:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "i.id_item");
                
                /* Filter Key, if provided:
                 * Index 0: year
                 * Index 1: cut off date
                 * Index 2: unit of measure
                 * Index 3: id warehouse (company branch)
                 * Index 4: id warehouse (branch entity)
                 */

                i = 0;
                if (filterKey == null || filterKey instanceof int[]) {
                    aoQueryFields = new STableField[9];
                }
                else {
                    aoQueryFields = new STableField[10];
                }
                
                switch (pnDataType) {
                    case SDataConstants.ITMX_ITEM_BY_KEY:
                    case SDataConstants.ITMX_ITEM_BY_NAME:
                        break;
                    case SDataConstants.ITMX_ITEM_BY_BRAND:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.brd");
                        break;
                    case SDataConstants.ITMX_ITEM_BY_MANUFACTURER:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "m.mfr");
                        break;
                }
                
                switch (pnDataType) {
                    case SDataConstants.ITMX_ITEM_BY_KEY:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                        break;
                    case SDataConstants.ITMX_ITEM_BY_NAME:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                        break;
                    case SDataConstants.ITMX_ITEM_BY_BRAND:
                    case SDataConstants.ITMX_ITEM_BY_MANUFACTURER:
                        if (piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                            aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                            aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                        }
                        else {
                            aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                            aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                        }
                        break;
                }
                
                if (filterKey != null && filterKey instanceof Object[]) {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_stock");
                }
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "u.symbol");
                
                switch (pnDataType) {
                    case SDataConstants.ITMX_ITEM_BY_KEY:
                    case SDataConstants.ITMX_ITEM_BY_NAME:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.brd");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "m.mfr");
                        break;
                    case SDataConstants.ITMX_ITEM_BY_BRAND:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "m.mfr");
                        break;
                    case SDataConstants.ITMX_ITEM_BY_MANUFACTURER:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.brd");
                        break;
                }
                
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "icl.id_ct_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "icl.id_cl_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "icl.cl_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "si.name");

                if (filterKey != null && filterKey instanceof Object[] && ((Object[]) filterKey)[1] != null) {
                    oDate = new java.sql.Date(((java.util.Date)((Object[]) filterKey)[1]).getTime());
                }
                
                sSql = "SELECT i.id_item, i.item, i.item_key, u.id_unit, u.symbol, b.id_brd, b.brd, m.id_mfr, m.mfr, icl.id_ct_item, icl.id_cl_item, icl.cl_item, si.id_st_item, si.name " +
                        (filterKey == null || filterKey instanceof int[] ? "" : ", trn_stk_get(" +
                        (Integer) ((Object[]) filterKey)[0] + ", i.id_item, i.fid_unit, NULL, " + (Integer) ((Object[]) filterKey)[3] + ", " + (Integer) ((Object[]) filterKey)[4] + ", '" + SLibUtils.DbmsDateFormatDate.format(oDate) + "') AS f_stock ") +
                        "FROM erp.itmu_item AS i " +
                        "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
                        (!bInventoriableNot ? "" : "AND i.b_inv = 0 ") +
                        (!bInventoriableOnly ? "" : "AND i.b_inv = 1 ") +
                        "INNER JOIN erp.itmu_brd AS b ON i.fid_brd = b.id_brd " +
                        "INNER JOIN erp.itmu_mfr AS m ON i.fid_mfr = m.id_mfr " +
                        "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                        "INNER JOIN erp.itms_cl_item AS icl ON ig.fid_ct_item = icl.id_ct_item AND ig.fid_cl_item = icl.id_cl_item " +
                        "INNER JOIN erp.itms_st_item AS si ON i.fid_st_item = si.id_st_item " +
                        "WHERE i.b_del = 0 ";
                
                switch (pnDataType) {
                    case SDataConstants.ITMX_ITEM_BY_KEY:
                        sSql += "ORDER BY i.item_key, i.item, i.id_item ";
                        break;
                    case SDataConstants.ITMX_ITEM_BY_NAME:
                        sSql += "ORDER BY i.item, i.item_key, i.id_item ";
                        break;
                    case SDataConstants.ITMX_ITEM_BY_BRAND:
                        sSql += "ORDER BY b.brd, b.id_brd, " + (piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") + "i.id_item ";
                        break;
                    case SDataConstants.ITMX_ITEM_BY_MANUFACTURER:
                        sSql += "ORDER BY m.mfr, m.id_mfr, " + (piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") + "i.id_item ";
                        break;
                }
                
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatFin(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";
        String sWorkingDate = piClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(piClient.getSessionXXX().getWorkingDate());

        switch (pnDataType) {
            case SDataConstants.FINS_TP_ACC_SPE:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_tp_acc_spe");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.name");

                sSql = "SELECT t.id_tp_acc_spe, t.name " +
                        "FROM erp.fins_tp_acc_spe AS t " +
                        "WHERE t.b_del = 0 " +
                        "ORDER BY t.sort ";
                break;

            case SDataConstants.FINS_FISCAL_ACC:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_fiscal_acc");

                i = 0;
                aoQueryFields = new STableField[3];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.name");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "t.b_eli");

                sSql = "SELECT t.id_fiscal_acc, t.code, t.name, t.b_eli " +
                        "FROM erp.fins_fiscal_acc AS t " +
                        "WHERE t.b_del = 0 " +
                        "ORDER BY t.code, t.name, t.id_fiscal_acc ";
                break;

            case SDataConstants.FINS_FISCAL_BANK:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_fiscal_bank");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.name");

                sSql = "SELECT t.id_fiscal_bank, t.code, t.name " +
                        "FROM erp.fins_fiscal_bank AS t " +
                        "WHERE t.b_del = 0 " +
                        "ORDER BY t.code, t.name, t.id_fiscal_bank ";
                break;

            case SDataConstants.FINU_TP_ACC_USR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_tp_acc_usr");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.tp_acc_usr");

                sSql = "SELECT t.id_tp_acc_usr, t.tp_acc_usr " +
                        "FROM erp.finu_tp_acc_usr AS t " +
                        "WHERE t.b_del = 0 AND t.id_tp_acc_usr > 1 " +
                        "ORDER BY t.fid_tp_acc ";
                break;

            case SDataConstants.FINU_CL_ACC_USR:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_tp_acc_usr");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.id_cl_acc_usr");

                i = 0;
                aoQueryFields = new STableField[4];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.tp_acc_usr");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cl_acc_usr");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.num_start");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.num_end");

                sSql = "SELECT c.id_tp_acc_usr, c.id_cl_acc_usr, c.cl_acc_usr, " +
                        "c.num_start, c.num_end, t.tp_acc_usr " +
                        "FROM erp.finu_cl_acc_usr AS c " +
                        "INNER JOIN erp.finu_tp_acc_usr AS t ON " +
                        "c.id_tp_acc_usr = t.id_tp_acc_usr " +
                        "WHERE c.b_del = 0 AND c.id_tp_acc_usr > 1 " +
                        (filterKey == null ? "" : "AND c.id_tp_acc_usr = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY c.fid_tp_acc, c.fid_cl_acc ";
                break;

            case SDataConstants.FINU_CLS_ACC_USR:
                aoPkFields = new STableField[3];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cs.id_tp_acc_usr");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cs.id_cl_acc_usr");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cs.id_cls_acc_usr");

                i = 0;
                aoQueryFields = new STableField[5];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.tp_acc_usr");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cl_acc_usr");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cs.cls_acc_usr");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cs.num_start");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "cs.num_end");

                sSql = "SELECT cs.id_tp_acc_usr, cs.id_cl_acc_usr, cs.id_cls_acc_usr, cs.cls_acc_usr, " +
                        "cs.num_start, cs.num_end, c.cl_acc_usr, t.tp_acc_usr " +
                        "FROM erp.finu_cls_acc_usr AS cs " +
                        "INNER JOIN erp.finu_cl_acc_usr AS c ON " +
                        "cs.id_tp_acc_usr = c.id_tp_acc_usr AND cs.id_cl_acc_usr = c.id_cl_acc_usr " +
                        "INNER JOIN erp.finu_tp_acc_usr AS t ON " +
                        "cs.id_tp_acc_usr = t.id_tp_acc_usr " +
                        "WHERE cs.b_del = 0 AND cs.id_tp_acc_usr > 1 " +
                        (filterKey == null ? "" : "AND cs.id_tp_acc_usr = " + ((int[]) filterKey)[0] + " AND " +
                        "cs.id_cl_acc_usr = " + ((int[]) filterKey)[1] + " ") +
                        "ORDER BY cs.fid_tp_acc, cs.fid_cl_acc, cs.fid_cls_acc ";
                break;

            case SDataConstants.FINU_TP_ACC_LEDGER:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "t.id_tp_acc_ledger");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.tp_acc_ledger");

                sSql = "SELECT t.id_tp_acc_ledger, t.tp_acc_ledger " +
                        "FROM erp.finu_tp_acc_ledger AS t " +
                        "WHERE t.b_del = 0 AND t.id_tp_acc_ledger > 1 " +
                        (filterKey == null ? "" : "AND t.fid_tp_acc_usr = " + ((int[]) filterKey)[0] + " AND " +
                        "t.fid_cl_acc_usr = " + ((int[]) filterKey)[1] + " AND t.fid_cls_acc_usr = " + ((int[]) filterKey)[2] + " ") +
                        "ORDER BY t.id_tp_acc_ledger ";
                break;

            case SDataConstants.FINU_TP_ACC_EBITDA:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_acc_ebitda");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_acc_ebitda");

                sSql = "SELECT id_tp_acc_ebitda, tp_acc_ebitda FROM erp.finu_tp_acc_ebitda WHERE NOT b_del ORDER BY sort_pos ";
                break;

            case SDataConstants.FINU_TP_REC:
            case SDataConstants.FINX_TP_REC_ALL:
            case SDataConstants.FINX_TP_REC_USER:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "id_tp_rec");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "id_tp_rec");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_rec");

                sSql = "SELECT id_tp_rec, tp_rec FROM erp.finu_tp_rec " +
                        "WHERE " + (pnDataType != SDataConstants.FINX_TP_REC_USER ? "" : "b_sys = 0 AND ") + "b_del = 0 ORDER BY id_tp_rec ";
                break;

            case SDataConstants.FINU_TP_ADM_CPT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_adm_cpt");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_adm_cpt");

                sSql = "SELECT id_tp_adm_cpt, tp_adm_cpt FROM erp.finu_tp_adm_cpt " +
                        "WHERE b_del = 0 ORDER BY tp_adm_cpt, id_tp_adm_cpt ";
                break;

            case SDataConstants.FINU_TP_TAX_CPT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_tax_cpt");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_tax_cpt");

                sSql = "SELECT id_tp_tax_cpt, tp_tax_cpt FROM erp.finu_tp_tax_cpt " +
                        "WHERE b_del = 0 ORDER BY tp_tax_cpt, id_tp_tax_cpt ";
                break;

            case SDataConstants.FIN_ACC_CASH:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_cob");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_acc_cash");

                i = 0;
                aoQueryFields = new STableField[7];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "e.ent");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "e.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ac.ct_acc_cash");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "at.tp_acc_cash");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.bp_comm");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_check_wal");

                sSql = "SELECT a.id_cob, a.id_acc_cash, a.b_check_wal, e.ent, e.code, ac.ct_acc_cash, at.tp_acc_cash, c.cur_key, b.bp_comm " +
                        "FROM fin_acc_cash AS a " +
                        "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                        "a.id_cob = e.id_cob AND a.id_acc_cash = e.id_ent AND " +
                        "a.b_del = 0 " + (filterKey == null ? "" : "AND a.id_cob = " + ((int[]) filterKey)[0] + " ") +
                        "INNER JOIN erp.fins_ct_acc_cash AS ac ON " +
                        "a.fid_ct_acc_cash = ac.id_ct_acc_cash " +
                        "INNER JOIN erp.fins_tp_acc_cash AS at ON " +
                        "a.fid_ct_acc_cash = at.id_ct_acc_cash AND a.fid_tp_acc_cash = at.id_tp_acc_cash " +
                        "INNER JOIN erp.cfgu_cur AS c ON " +
                        "a.fid_cur = c.id_cur " +
                        "LEFT JOIN erp.bpsu_bank_acc AS ba ON " +
                        "a.fid_bpb_n = ba.id_bpb AND a.fid_bank_acc_n = ba.id_bank_acc " +
                        "LEFT OUTER JOIN erp.bpsu_bp AS b ON " +
                        "ba.fid_bank = b.id_bp " +
                        "ORDER BY a.id_cob, e.ent, e.code, a.id_acc_cash ";
                break;

            case SDataConstants.FINX_ACC_CASH_CASH:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_cob");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_acc_cash");

                i = 0;
                aoQueryFields = new STableField[4];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "e.ent");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "e.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "at.tp_acc_cash");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");

                sSql = "SELECT a.id_cob, a.id_acc_cash, e.ent, e.code, at.tp_acc_cash, c.cur_key " +
                        "FROM fin_acc_cash AS a " +
                        "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                        "a.id_cob = e.id_cob AND a.id_acc_cash = e.id_ent AND a.b_del = 0 AND " +
                        "e.fid_ct_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_CASH[0] + " AND " +
                        "e.fid_tp_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_CASH[1] + " " +
                        (filterKey == null ? "" : "AND a.id_cob = " + ((int[]) filterKey)[0] + " ") +
                        "INNER JOIN erp.fins_tp_acc_cash AS at ON " +
                        "a.fid_ct_acc_cash = at.id_ct_acc_cash AND a.fid_tp_acc_cash = at.id_tp_acc_cash " +
                        "INNER JOIN erp.cfgu_cur AS c ON " +
                        "a.fid_cur = c.id_cur " +
                        "ORDER BY a.id_cob, e.ent, e.code, a.id_acc_cash ";
                break;

            case SDataConstants.FINX_ACC_CASH_BANK:
            case SDataConstants.FINX_ACC_CASH_BANK_CHECK:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_cob");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.id_acc_cash");

                i = 0;
                aoQueryFields = new STableField[6];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "e.ent");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "e.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "at.tp_acc_cash");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.bp_comm");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_check_wal");

                sSql = "SELECT a.id_cob, a.id_acc_cash, a.b_check_wal, e.ent, e.code, at.tp_acc_cash, c.cur_key, b.bp_comm " +
                        "FROM fin_acc_cash AS a " +
                        "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                        "a.id_cob = e.id_cob AND a.id_acc_cash = e.id_ent AND a.b_del = 0 AND " +
                        "e.fid_ct_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[0] + " AND " +
                        "e.fid_tp_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[1] + " " +
                        (filterKey == null ? "" : "AND a.id_cob = " + ((int[]) filterKey)[0] + " ") +
                        (pnDataType != SDataConstants.FINX_ACC_CASH_BANK_CHECK ? "" : "AND a.b_check_wal = 1 ") +
                        "INNER JOIN erp.fins_tp_acc_cash AS at ON " +
                        "a.fid_ct_acc_cash = at.id_ct_acc_cash AND a.fid_tp_acc_cash = at.id_tp_acc_cash " +
                        "INNER JOIN erp.cfgu_cur AS c ON " +
                        "a.fid_cur = c.id_cur " +
                        "INNER JOIN erp.bpsu_bank_acc AS ba ON " +
                        "a.fid_bpb_n = ba.id_bpb AND a.fid_bank_acc_n = ba.id_bank_acc " +
                        "INNER JOIN erp.bpsu_bp AS b ON " +
                        "ba.fid_bank = b.id_bp " +
                        "ORDER BY a.id_cob, e.ent, e.code, a.id_acc_cash ";
                break;

            case SDataConstants.FIN_BKC:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bkc");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bkc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "code");

                sSql = "SELECT id_bkc, bkc, code " +
                        "FROM fin_bkc WHERE b_del = 0 " +
                        "ORDER BY bkc, id_bkc ";
                break;

            case SDataConstants.FIN_ACC:
                String account = piClient.getSessionXXX().getParamsErp().getFormatAccountId().replace('9', '0');
                Vector<Integer> levelsAccount = SDataUtilities.getAccountLevels(account);

                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.id_acc");

                i = 0;
                aoQueryFields = new STableField[11];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_acc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.acc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.deep");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "a.lev");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "a.dt_start");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "a.dt_end_n");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "s.tp_acc_sys");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_req_cc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_req_ent");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_req_bp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_req_item");

                sSql = "SELECT a.id_acc, f_acc_usr(" + ((SDataParamsCompany) piClient.getSession().getConfigCompany()).getMaskAccount() + ", a.code) AS f_acc, a.acc, a.dt_start, a.dt_end_n, a.deep, a.lev, a.b_req_cc, a.b_req_ent, a.b_req_bp, a.b_req_item, s.tp_acc_sys, " +
                        "IF(a.lev = 1, " + STableConstants.STYLE_BOLD + ", IF(a.lev < am.deep, " + STableConstants.STYLE_ITALIC + ", " + STableConstants.UNDEFINED + ")) AS f_style " +
                        "FROM fin_acc AS a " +
                        "INNER JOIN fin_acc AS am ON " +
                        "CONCAT(LEFT(a.id_acc, " + (levelsAccount.get(1) - 1) + "), '" + account.substring(levelsAccount.get(1) - 1) + "') = am.id_acc " +
                        "INNER JOIN erp.fins_tp_acc_sys AS s ON " +
                        "a.fid_tp_acc_sys = s.id_tp_acc_sys " +
                        "WHERE a.b_act = 1 AND a.b_del = 0 AND " +
                        "'" + sWorkingDate + "' >= a.dt_start AND '" + sWorkingDate + "' <= COALESCE(a.dt_end_n, '" + sWorkingDate + "') " +
                        "ORDER BY a.id_acc ";
                break;

            case SDataConstants.FIN_CC:
                String costCenter = piClient.getSessionXXX().getParamsErp().getFormatCostCenterId().replace('9', '0');
                Vector<Integer> levels = SDataUtilities.getAccountLevels(costCenter);

                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.id_cc");

                i = 0;
                aoQueryFields = new STableField[6];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_cc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.deep");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "c.lev");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "c.dt_start");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "c.dt_end_n");

                sSql = "SELECT c.id_cc, f_acc_usr(" + ((SDataParamsCompany) piClient.getSession().getConfigCompany()).getMaskCostCenter() + ", c.code) AS f_cc, c.cc, c.dt_start, c.dt_end_n, c.deep, c.lev, " +
                        "IF(c.lev = 1, " + STableConstants.STYLE_BOLD + ", IF(c.lev < cm.deep, " + STableConstants.STYLE_ITALIC + ", " + STableConstants.UNDEFINED + ")) AS f_style " +
                        "FROM fin_cc AS c " +
                        "INNER JOIN fin_cc AS cm ON " +
                        "CONCAT(LEFT(c.id_cc, " + (levels.get(1) - 1) + "), '" + costCenter.substring(levels.get(1) - 1) + "') = cm.id_cc " +
                        "WHERE c.b_act = 1 AND c.b_del = 0 AND " +
                        "'" + sWorkingDate + "' >= c.dt_start AND '" + sWorkingDate + "' <= COALESCE(c.dt_end_n, '" + sWorkingDate + "') " +
                        "ORDER BY c.id_cc ";
                break;

            case SDataConstants.FIN_EXC_RATE:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "e.id_cur");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "e.id_dt");

                i = 0;
                aoQueryFields = new STableField[3];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "e.id_dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "e.exc_rate");

                sSql = "SELECT e.id_cur, e.id_dt, e.exc_rate, c.cur_key " +
                        "FROM fin_exc_rate AS e INNER JOIN erp.cfgu_cur AS c ON e.id_cur = c.id_cur " +
                        "WHERE e.b_del = 0 AND e.id_dt BETWEEN ADDDATE('" + sWorkingDate + "', -31) AND ADDDATE('" + sWorkingDate + "', 31) " +
                        (filterKey == null ? "" : "AND e.id_cur = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY c.cur_key, e.id_dt DESC ";
                break;

            case SDataConstants.FINU_TAX_REG:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tax_reg");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tax_reg");

                sSql = "SELECT id_tax_reg, tax_reg " +
                        "FROM erp.finu_tax_reg WHERE b_del = 0 " +
                        "ORDER BY tax_reg, id_tax_reg ";
                break;

            case SDataConstants.FINU_TAX_IDY:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tax_idy");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tax_idy");

                sSql = "SELECT id_tax_idy, tax_idy " +
                        "FROM erp.finu_tax_idy WHERE b_del = 0 " +
                        (filterKey == null ? "" : "AND fid_tp_bp_idy = " + ((int[]) filterKey)[0]) + " " +
                        "ORDER BY tax_idy, id_tax_idy ";
                break;

            case SDataConstants.FINU_TAX_BAS:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tax_bas");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tax_bas");

                sSql = "SELECT id_tax_bas, tax_bas " +
                        "FROM erp.finu_tax_bas WHERE b_del = 0 " +
                        "ORDER BY tax_bas, id_tax_bas ";
                break;

            case SDataConstants.FINU_TAX:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tax_bas");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tax");

                i = 0;
                aoQueryFields = new STableField[4];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tax");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "per");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "val_u");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "val");

                sSql = "SELECT id_tax_bas, id_tax, tax, per*100 AS per, val_u, val " +
                        "FROM erp.finu_tax WHERE b_del = 0 " +
                        (filterKey == null ? "" : "AND id_tax_bas = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY tax, id_tax_bas, id_tax ";
                break;

            case SDataConstants.FINU_CARD_ISS:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_card_iss");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "card_iss");

                sSql = "SELECT id_card_iss, card_iss " +
                        "FROM erp.finu_card_iss WHERE b_del = 0 " +
                        "ORDER BY card_iss, id_card_iss ";
                break;

            case SDataConstants.FINU_CHECK_FMT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_check_fmt");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "check_fmt");

                sSql = "SELECT id_check_fmt, check_fmt " +
                        "FROM erp.finu_check_fmt WHERE b_del = 0 " +
                        "ORDER BY check_fmt, id_check_fmt ";
                break;

            case SDataConstants.FINU_CHECK_FMT_GP:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_check_fmt_gp");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "check_fmt_gp");

                sSql = "SELECT id_check_fmt_gp, check_fmt_gp " +
                        "FROM erp.finu_check_fmt_gp WHERE b_del = 0 " +
                        "ORDER BY check_fmt_gp, id_check_fmt_gp ";
                break;

            case SDataConstants.FIN_REC:
            case SDataConstants.FINX_REC_USER:
                aoPkFields = new STableField[5];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "r.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "r.id_per");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "r.id_bkc");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "r.id_tp_rec");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "r.id_num");

                i = 0;
                aoQueryFields = new STableField[13];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_per");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bkc.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "r.dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "r.concept");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_debit");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_credit");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_balance");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "r.b_sys");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "e.ent");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "r.b_audit");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "r.b_authorn");

                sSql = "SELECT r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, r.concept, " +
                        "r.b_audit, r.b_authorn, r.b_sys, bkc.code, cob.code, e.ent, " +
                        "CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_per, " +
                        "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_num, " +
                        "SUM(re.debit) AS f_debit, SUM(re.credit) AS f_credit, SUM(re.debit) - SUM(re.credit) AS f_balance " +
                        "FROM fin_rec AS r " +
                        "INNER JOIN fin_bkc AS bkc ON " +
                        "r.id_bkc = bkc.id_bkc " +
                        "INNER JOIN erp.bpsu_bpb AS cob ON " +
                        "r.fid_cob = cob.id_bpb " +
                        "LEFT OUTER JOIN erp.cfgu_cob_ent AS e ON " +
                        "r.fid_cob_n = e.id_cob AND r.fid_acc_cash_n = e.id_ent " +
                        "LEFT OUTER JOIN fin_rec_ety AS re ON " +
                        "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND re.b_del = 0 " +
                        "WHERE r.b_del = 0 AND " +
                        "r.id_year = " + SLibTimeUtilities.digestYearMonth((java.util.Date) filterKey)[0] + " AND " +
                        "r.id_per = " + SLibTimeUtilities.digestYearMonth((java.util.Date) filterKey)[1] + " " +
                        (pnDataType != SDataConstants.FINX_REC_USER ? "" : "AND r.b_sys = 0 ") +
                        (params == null ? "" : ("AND r.fid_cob = " + params.get(SDataConstants.FIN_ACC_COB_ENT) + " AND r.fid_acc_cash_n = " + params.get(SDataConstants.FIN_ACC_CASH) + " ")) +
                        "GROUP BY r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, r.id_num, r.dt, r.concept, " +
                        "r.b_audit, r.b_authorn, r.b_sys, bkc.code, cob.code " +
                        "ORDER BY r.id_year DESC, r.id_per DESC, r.dt DESC, bkc.code, r.id_bkc, r.id_tp_rec, r.id_num ";
                break;

            case SDataConstants.FIN_YEAR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "year");

                sSql = "SELECT id_year, id_year AS year  FROM fin_year WHERE b_del = 0 ORDER BY id_year ";
                break;

            case SDataConstants.FIN_TAX_GRP:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tax_grp");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tax_grp");

                sSql = "SELECT id_tax_grp, tax_grp " +
                        "FROM fin_tax_grp WHERE b_del = 0 " +
                        "ORDER BY tax_grp, id_tax_grp ";
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatTrn(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";

        switch (pnDataType) {
            case SDataConstants.TRNS_CT_DPS:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ct_dps");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct_dps");

                sSql = "SELECT id_ct_dps, ct_dps " +
                        "FROM erp.trns_ct_dps WHERE b_del = 0 " +
                        "ORDER BY id_ct_dps, ct_dps ";
                break;

            case SDataConstants.TRNS_CL_DPS:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ct_dps");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cl_dps");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cl_dps");

                sSql = "SELECT id_ct_dps, id_cl_dps, cl_dps " +
                        "FROM erp.trns_cl_dps WHERE b_del = 0 ";
                if (filterKey != null) {
                    sSql += " AND id_ct_dps = " + ((int[]) filterKey)[0] + " ";
                }

                sSql += "ORDER BY id_ct_dps, id_cl_dps, cl_dps ";
                break;

            case SDataConstants.TRNU_TP_DPS:
                aoPkFields = new STableField[3];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ct_dps");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cl_dps");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_dps");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_dps");

                sSql = "SELECT id_ct_dps, id_cl_dps, id_tp_dps, tp_dps " +
                        "FROM erp.trnu_tp_dps WHERE b_del = 0 ";
                if (filterKey != null) {
                    if (((int[]) filterKey).length >= 1) {
                        sSql += " AND id_ct_dps = " + ((int[]) filterKey)[0] + " ";
                    }
                    if (((int[]) filterKey).length >= 2) {
                        sSql += " AND id_cl_dps = " + ((int[]) filterKey)[1] + " ";
                    }
                }

                sSql += "ORDER BY id_ct_dps, id_cl_dps, id_tp_dps, tp_dps ";
                break;

            case SDataConstants.TRNS_TP_IOG:
                aoPkFields = new STableField[3];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ct_iog");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cl_iog");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_iog");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_iog");

                sSql = "SELECT id_ct_iog, id_cl_iog, id_tp_iog, tp_iog " +
                        "FROM erp.trns_tp_iog WHERE b_del = 0 ";
                if (filterKey != null) {
                    if (((int[]) filterKey).length >= 1) {
                        sSql += " AND id_ct_iog = " + ((int[]) filterKey)[0] + " ";
                    }
                    if (((int[]) filterKey).length >= 2) {
                        sSql += " AND id_cl_iog = " + ((int[]) filterKey)[1] + " ";
                    }
                }

                sSql += "ORDER BY id_ct_iog, id_cl_iog, id_tp_iog, tp_iog ";
                break;

            case SDataConstants.TRNS_TP_LINK:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_link");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_link");

                sSql = "SELECT id_tp_link, tp_link " +
                        "FROM erp.trns_tp_link WHERE b_del = 0 " +
                        "ORDER BY id_tp_link ";
                break;

            case SDataConstants.TRN_DNS_DPS:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_dns");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_item");

                sSql = "SELECT s.id_dns, CONCAT(s.dns, ' (', t.tp_dps, ')') AS f_item " +
                        "FROM trn_dns_dps AS s " +
                        "INNER JOIN erp.trnu_tp_dps AS t ON " +
                        "s.fid_ct_dps = t.id_ct_dps AND s.fid_cl_dps = t.id_cl_dps AND s.fid_tp_dps = t.id_tp_dps " +
                        (filterKey == null ? "" : "AND t.id_ct_dps = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY s.dns, s.id_dns, t.id_ct_dps, t.id_cl_dps, t.tp_dps ";
                break;

            case SDataConstants.TRNU_DPS_NAT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_dps_nat");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_item");

                sSql = "SELECT id_dps_nat, CONCAT(dps_nat, ' (', code, ')') AS f_item " +
                        "FROM erp.trnu_dps_nat WHERE b_del = 0 " +
                        "ORDER BY dps_nat, id_dps_nat ";
                break;

            case SDataConstants.TRN_SYS_NTS:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_nts");

                i = 0;
                aoQueryFields = new STableField[5];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.tp_dps");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "s.b_aut");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "s.b_prt");

                sSql = "SELECT s.id_nts, s.nts AS f_item, t.tp_dps, c.cur_key, s.b_aut, s.b_prt " +
                        "FROM trn_sys_nts AS s " +
                        "INNER JOIN erp.trnu_tp_dps AS t ON " +
                        "s.fid_ct_dps = t.id_ct_dps AND s.fid_cl_dps = t.id_cl_dps AND s.fid_tp_dps = t.id_tp_dps " +
                        (filterKey == null ? "" : "AND t.id_ct_dps = " + ((int[]) filterKey)[0] + " AND s.fid_cl_dps = " + ((int[]) filterKey)[1] + " AND s.fid_tp_dps = " + ((int[]) filterKey)[2] + " ") +
                        "INNER JOIN erp.cfgu_cur AS c ON s.fid_cur = c.id_cur " +
                        (filterKey == null ? "" : "AND c.id_cur = " + ((int[]) filterKey)[3] + " ") +
                        "WHERE s.b_del = 0 " +
                        "ORDER BY s.nts, s.id_nts ";
                break;

            case SDataConstants.TRN_DPS:
                /* Parameter filterKey is an Object array of 1 or 2 dimensions, wich can contain 2 int arrays:
                 * 1. int array of document class primary key.
                 * 2. int array of business partner primary key (optional).
                 */

                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");

                i = 0;
                aoQueryFields = new STableField[9];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dt.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.bp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");

                sSql = "SELECT d.id_year, d.id_doc, d.dt, d.tot_r, d.exc_rate, d.tot_cur_r, " +
                        "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, dt.code, b.bp, b.id_bp, c.cur_key, cob.code " +
                        "FROM trn_dps AS d " +
                        "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                        (filterKey == null ? "" : "d.fid_ct_dps = " + ((int[])((Object[]) filterKey)[0])[0] + " AND d.fid_cl_dps = " + ((int[])((Object[]) filterKey)[0])[1] + " AND ") +
                        (((Object[]) filterKey).length == 1 ? "" : "d.fid_bp_r = " + ((int[]) ((Object[]) filterKey)[1])[0] + " AND ") +
                        "d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                        "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                        "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                        "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                        "ORDER BY d.dt, dt.code, d.num_ser, d.num, b.bp, b.id_bp ";
                break;

            case SDataConstants.TRN_DPS_IOG_CHG:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dps.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dps.id_doc");

                i = 0;
                aoQueryFields = new STableField[7];
                if (((int[]) filterKey)[1] == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                    if (piClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "dps.dt");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp.tp_dps");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "num_ser");
                    }
                    else {
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "dps.dt");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp.tp_dps");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "num_ser");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                    }
                }
                else {
                    if (piClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "dps.dt");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp.tp_dps");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "num_ser");
                    }
                    else {
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "dps.dt");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp.tp_dps");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "num_ser");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                    }
                }
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "dps.tot_cur_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cur.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bpb.code");

                sSql = "SELECT dps.id_year, dps.id_doc, dps.dt, CONCAT(dps.num_ser, IF(LENGTH(dps.num_ser) = 0, '', '-'), dps.num) AS num_ser, dps.tot_cur_r, " +
                        "tp.tp_dps, bp.bp, bpb.code, cur.cur_key " +
                        "FROM trn_dps AS dps " +
                        "INNER JOIN erp.trnu_tp_dps AS tp ON " +
                        "dps.fid_ct_dps = tp.id_ct_dps AND dps.fid_cl_dps = tp.id_cl_dps AND dps.fid_tp_dps = tp.id_tp_dps " + (filterKey == null ? "" :"AND dps.fid_ct_dps = " +
                        ((int[]) filterKey)[2] + " AND dps.fid_cl_dps = " + ((int[]) filterKey)[3] + " AND dps.fid_tp_dps = " + ((int[]) filterKey)[4]) + " " +
                        "INNER JOIN erp.bpsu_bp AS bp ON " +
                        "dps.fid_bp_r = bp.id_bp " +
                        "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                        "dps.fid_cob = bpb.id_bpb " +
                        "INNER JOIN erp.cfgu_cur AS cur ON " +
                        "dps.fid_cur = cur.id_cur " +
                        "WHERE dps.fid_bp_r = " + ((int[]) filterKey)[0] + " AND dps.b_del = 0 " +
                        "ORDER BY " + (filterKey == null ? "dps.fid_bp_r, dps.dt, dps.fid_ct_dps, dps.fid_cl_dps, dps.fid_tp_dps, num_ser" : (((int[]) filterKey)[1] == SDataConstantsSys.TRNS_CT_DPS_PUR ?
                            (piClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC ? "dps.fid_bp_r, dps.dt, dps.fid_ct_dps, dps.fid_cl_dps, dps.fid_tp_dps, num_ser" : "dps.dt, dps.fid_ct_dps, dps.fid_cl_dps, dps.fid_tp_dps, num_ser, dps.fid_bp_r"):
                                (piClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC ? "dps.fid_bp_r, dps.dt, dps.fid_ct_dps, dps.fid_cl_dps, dps.fid_tp_dps, num_ser" : "dps.dt, dps.fid_ct_dps, dps.fid_cl_dps, dps.fid_tp_dps, num_ser, dps.fid_bp_r"))) + " ";
                break;

            case SDataConstants.TRNX_PRICE_HIST:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");

                i = 0;
                aoQueryFields = new STableField[9];
                switch (((int[]) filterKey)[0] == SDataConstantsSys.TRNS_CT_DPS_PUR ?
                    piClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId() :
                    piClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId()) {

                    case SDataConstantsSys.CFGS_TP_SORT_DOC_BIZ_P:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dt.code");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "d.num_ref");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");

                        if (((int[]) filterKey)[0] == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                            if (piClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                            }
                            else {
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                            }
                        }
                        else {
                            if (piClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                            }
                            else {
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                            }
                        }
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bpb.bpb");

                        break;
                    case SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC:
                        if (((int[]) filterKey)[0] == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                            if (piClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                            }
                            else {
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                            }
                        }
                        else {
                            if (piClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                            }
                            else {
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                            }
                        }
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bpb.bpb");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dt.code");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "d.num_ref");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");
                        break;
                    default:
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dt.code");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "d.num_ref");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "ct.bp_key");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bp.bp");
                        aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bpb.bpb");
                }
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_price_u");

                sSql = "SELECT d.id_year, d.id_doc, i.id_item, i.item, i.item_key, bp.bp, ct.bp_key, bpb.bpb, d.dt, dt.code, d.num_ref, cob.code, " +
                    "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                    "(SELECT ets.price_u FROM trn_dps AS ds " +
                    "INNER JOIN trn_dps_ety AS ets ON ds.id_year = ets.id_year AND ds.id_doc = ets.id_doc AND ds.b_del = 0 AND ets.b_del = 0 AND " +
                    "ds.fid_ct_dps = " + (filterKey == null ? "0" : ((int []) filterKey)[0]) + " " + " AND " +
                    "ds.fid_cl_dps = " + (filterKey == null ? "0" : ((int []) filterKey)[0] == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] : SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] ) + " " +
                    "WHERE ds.id_year = d.id_year AND ds.id_doc = d.id_doc AND ets.id_year = et.id_year AND ets.id_doc = et.id_doc AND ets.id_ety = et.id_ety) AS f_price_u " +
                    "FROM trn_dps AS d " +
                    "INNER JOIN trn_dps_ety AS et ON d.id_year = et.id_year AND d.id_doc = et.id_doc AND d.b_del = 0 AND et.b_del = 0 AND " +
                    "d.fid_ct_dps = " + (filterKey == null ? "0" : ((int []) filterKey)[0]) + " " + " AND " +
                    "d.fid_cl_dps = " + (filterKey == null ? "0" : ((int []) filterKey)[0] == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] : SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] ) + " " +
                    "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                    "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.fid_ct_bp = " + (((int[]) filterKey)[0] == SDataConstantsSys.TRNS_CT_DPS_PUR ?
                        SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                    "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                    "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                    "INNER JOIN erp.itmu_item AS i ON et.fid_item = i.id_item " +
                    "WHERE i.id_item = " + (filterKey == null ? "0" : ((int []) filterKey)[1]) + " " +
                    "ORDER BY d.dt DESC";

                break;

            case SDataConstants.TRNX_DPS_PAY_PEND:
                /* Parameter filterKey is an Object array of 2 or 3 dimensions, wich contain the following:
                 * 1. int representing fiscal year.
                 * 2. int array of document class primary key.
                 * 3. int array of business partner primary key (optional).
                 */

                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");

                i = 0;
                aoQueryFields = new STableField[12];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dt.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.bp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_bal");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_bal_cur");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");

                sSql = "SELECT b.id_bp, b.bp, d.id_year, d.id_doc, d.dt, dt.code, " +
                        "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                        "d.tot_r, d.exc_rate, d.tot_cur_r, c.cur_key, cob.code, " +
                        "SUM(re.debit - re.credit) AS f_bal, SUM(IF(re.fid_cur <> d.fid_cur, 0, re.debit_cur - re.credit_cur)) AS f_bal_cur " +
                        "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                        "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                        "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + ((Object[]) filterKey)[0] + " AND " +
                        "re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_CT_SYS_MOV_BPS + " AND " +
                        "re.fid_tp_sys_mov_xxx = " + (((int[]) ((Object[]) filterKey)[1])[0] == SDataConstantsSys.TRNS_CT_DPS_PUR ?
                            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]) + " " +
                        "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                        "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " + (((Object[]) filterKey).length == 2 ? "" : "AND d.fid_bp_r = " + ((int[]) ((Object[]) filterKey)[2])[0] + " ") +
                        "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                        "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                        "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                        "GROUP BY b.id_bp, b.bp, d.id_year, d.id_doc, d.dt, dt.code, d.num_ser, d.num, " +
                        "d.tot_r, d.exc_rate, d.tot_cur_r, c.cur_key, cob.code " +
                        "HAVING " + (((int[]) ((Object[]) filterKey)[1])[0] == SDataConstantsSys.TRNS_CT_DPS_SAL ?
                            "SUM(debit - credit) > 0 OR SUM(debit_cur - credit_cur) > 0 " :
                            "SUM(credit - debit) > 0 OR SUM(credit_cur - debit_cur) > 0 ") +
                        "ORDER BY d.dt DESC, dt.code, f_num, cob.code, d.id_year, d.id_doc ";
                break;

            case SDataConstants.TRNX_DPS_PEND_LINK:
                /* Parameter filterKey is an Object array of 1 or 2 dimensions, wich can contain 2 int arrays:
                 * 1. int array of document class primary key.
                 * 2. int array of business partner primary key (optional).
                 */

                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");

                i = 0;
                aoQueryFields = new STableField[12];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dt.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "d.num_ref");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.bp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_disc_doc");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_disc_doc_per");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.disc_doc_per");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "f_aut");

                sSql = "SELECT DISTINCT d.id_year, d.id_doc, d.dt, dt.code, " +
                        "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                        "d.num_ref, d.b_disc_doc, d.b_disc_doc_per, d.disc_doc_per, d.tot_cur_r, b.id_bp, b.bp, c.cur_key, cob.code, " +
                        "d.fid_st_dps_authorn = " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN + " AS f_aut " +
                        "FROM trn_dps AS d INNER JOIN trn_dps_ety AS de ON " +
                        "d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                        "d.fid_ct_dps = " + ((int[]) ((Object[]) filterKey)[0])[0] + " AND d.fid_cl_dps = " + ((int[]) ((Object[]) filterKey)[0])[1] + " AND " +
                        (((Object[]) filterKey).length == 1 ? "" : "d.fid_bp_r = " + ((int[]) ((Object[]) filterKey)[1])[0] + " AND ") +
                        "d.b_del = 0 AND d.b_link = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND " +
                        "de.b_del = 0 AND de.orig_qty > (SELECT COALESCE(SUM(dds.orig_qty), 0) " +
                        "FROM trn_dps_dps_supply AS dds INNER JOIN trn_dps AS dd ON dds.id_des_year = dd.id_year AND dds.id_des_doc = dd.id_doc AND " +
                        "dd.b_del = false AND dd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                        "INNER JOIN trn_dps_ety AS dde ON dds.id_des_year = dde.id_year AND dds.id_des_doc = dde.id_doc AND dds.id_des_ety = dde.id_ety AND dde.b_del = false " +
                        "WHERE de.id_year = dds.id_src_year AND de.id_doc = dds.id_src_doc AND de.id_ety = dds.id_src_ety) " +
                        "INNER JOIN erp.trnu_tp_dps AS dt ON " +
                        "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                        "INNER JOIN erp.bpsu_bp AS b ON " +
                        "d.fid_bp_r = b.id_bp " +
                        "INNER JOIN erp.cfgu_cur AS c ON " +
                        "d.fid_cur = c.id_cur " +
                        "INNER JOIN erp.bpsu_bpb AS cob ON " +
                        "d.fid_cob = cob.id_bpb " +
                        "ORDER BY d.dt DESC, dt.code, f_num, cob.code, d.id_year, d.id_doc ";
                break;

            case SDataConstants.TRNX_DPS_PEND_ADJ:
                /* Parameter filterKey is an Object array of 1 or 2 dimensions, wich can contain 2 int arrays:
                 * 1. int array of document class primary key.
                 * 2. int array of business partner primary key (optional).
                 */

                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");

                i = 0;
                aoQueryFields = new STableField[9];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dt.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.bp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_adj");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_bal");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");

                sSql = "SELECT DISTINCT d.id_year, d.id_doc, d.dt, dt.code, " +
                        "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                        "d.tot_cur_r, b.bp, c.cur_key, cob.code, " +
                        "(SELECT COALESCE(SUM(dda.val_cur), 0) " +
                        "FROM trn_dps_dps_adj AS dda INNER JOIN trn_dps AS da ON dda.id_adj_year = da.id_year AND dda.id_adj_doc = da.id_doc AND " +
                        "da.b_del = 0 AND da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                        "INNER JOIN trn_dps_ety AS dae ON dda.id_adj_year = dae.id_year AND dda.id_adj_doc = dae.id_doc AND dda.id_adj_ety = dae.id_ety AND dae.b_del = 0 " +
                        "WHERE de.id_year = dda.id_dps_year AND de.id_doc = dda.id_dps_doc) AS f_adj, " +
                        "d.tot_cur_r - " +
                        "(SELECT COALESCE(SUM(dda.val_cur), 0) " +
                        "FROM trn_dps_dps_adj AS dda INNER JOIN trn_dps AS da ON dda.id_adj_year = da.id_year AND dda.id_adj_doc = da.id_doc AND " +
                        "da.b_del = 0 AND da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                        "INNER JOIN trn_dps_ety AS dae ON dda.id_adj_year = dae.id_year AND dda.id_adj_doc = dae.id_doc AND dda.id_adj_ety = dae.id_ety AND dae.b_del = 0 " +
                        "WHERE de.id_year = dda.id_dps_year AND de.id_doc = dda.id_dps_doc) AS f_bal " +
                        "FROM trn_dps AS d INNER JOIN trn_dps_ety AS de ON " +
                        "d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                        "d.fid_ct_dps = " + ((int[]) ((Object[]) filterKey)[0])[0] + " AND d.fid_cl_dps = " + ((int[]) ((Object[]) filterKey)[0])[1] + " AND " +
                        (((Object[]) filterKey).length == 1 ? "" : "d.fid_bp_r = " + ((int[]) ((Object[]) filterKey)[1])[0] + " AND ") +
                        "d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND " +
                        "de.b_del = 0 AND (de.orig_qty > (SELECT COALESCE(SUM(dda.orig_qty), 0) " +
                        "FROM trn_dps_dps_adj AS dda INNER JOIN trn_dps AS da ON dda.id_adj_year = da.id_year AND dda.id_adj_doc = da.id_doc AND " +
                        "da.b_del = 0 AND da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                        "INNER JOIN trn_dps_ety AS dae ON dda.id_adj_year = dae.id_year AND dda.id_adj_doc = dae.id_doc AND dda.id_adj_ety = dae.id_ety AND dae.b_del = 0 " +
                        "WHERE de.id_year = dda.id_dps_year AND de.id_doc = dda.id_dps_doc AND de.id_ety = dda.id_dps_ety) OR " +
                        "d.tot_cur_r > (SELECT COALESCE(SUM(dda.val_cur), 0) " +
                        "FROM trn_dps_dps_adj AS dda INNER JOIN trn_dps AS da ON dda.id_adj_year = da.id_year AND dda.id_adj_doc = da.id_doc AND " +
                        "da.b_del = 0 AND da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                        "INNER JOIN trn_dps_ety AS dae ON dda.id_adj_year = dae.id_year AND dda.id_adj_doc = dae.id_doc AND dda.id_adj_ety = dae.id_ety AND dae.b_del = 0 " +
                        "WHERE de.id_year = dda.id_dps_year AND de.id_doc = dda.id_dps_doc AND de.id_ety = dda.id_dps_ety)) " +
                        "INNER JOIN erp.trnu_tp_dps AS dt ON " +
                        "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                        "INNER JOIN erp.bpsu_bp AS b ON " +
                        "d.fid_bp_r = b.id_bp " +
                        "INNER JOIN erp.cfgu_cur AS c ON " +
                        "d.fid_cur = c.id_cur " +
                        "INNER JOIN erp.bpsu_bpb AS cob ON " +
                        "d.fid_cob = cob.id_bpb " +
                        "ORDER BY d.dt DESC, dt.code, f_num, cob.code, d.id_year, d.id_doc ";
                break;

            case SDataConstants.TRNX_DPS_SHIP_PEND_LINK:

                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");

                i = 0;
                aoQueryFields = new STableField[12];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dt.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "b.bp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_bal");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_bal_cur");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");

                sSql = "SELECT b.id_bp, b.bp, d.id_year, d.id_doc, d.dt, dt.code, " +
                    "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                    "d.tot_r, d.exc_rate, d.tot_cur_r, c.cur_key, cob.code, " +
                    "SUM(re.debit - re.credit) AS f_bal, SUM(IF(re.fid_cur <> d.fid_cur, 0, re.debit_cur - re.credit_cur)) AS f_bal_cur " +
                    "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                    "r.b_del = 0 AND re.b_del = 0 AND r.id_year = " + ((Object[]) filterKey)[0] + " AND " +
                    "re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_CT_SYS_MOV_BPS + " AND " +
                    "re.fid_tp_sys_mov_xxx = " + (((int[]) ((Object[]) filterKey)[1])[0] == SDataConstantsSys.TRNS_CT_DPS_PUR ?
                        SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]) + " " +
                    "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " +
                    "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " + (((Object[]) filterKey).length == 2 ? "" : "AND d.fid_bp_r = " + ((int[]) ((Object[]) filterKey)[2])[0] + " ") +
                    "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                    "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                    "GROUP BY b.id_bp, b.bp, d.id_year, d.id_doc, d.dt, dt.code, d.num_ser, d.num, " +
                    "d.tot_r, d.exc_rate, d.tot_cur_r, c.cur_key, cob.code " +
                    "ORDER BY d.dt DESC, dt.code, f_num, cob.code, d.id_year, d.id_doc ";
                break;

            case SDataConstants.TRNX_DPS_BAL:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");

                i = 0;
                aoQueryFields = new STableField[7];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "d.dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "td.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_tot_net");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_balance");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "c.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cb.code");

                sSql = "SELECT d.id_year, d.id_doc, d.dt, td.code, c.cur_key, cb.code, " +
                    "d.tot_cur_r - COALESCE((SELECT SUM(dda.val_cur) FROM trn_dps_dps_adj AS dda " +
                    "WHERE dda.id_dps_year = d.id_year AND dda.id_dps_doc = d.id_doc), 0) AS f_tot_net, " +
                    "CONCAT(d.num_ser, IF(LENGTH(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                    "SUM(IF(d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_PUR + ", -1, 1) * (re.debit - re.credit)) AS f_balance " +
                    "SUM(IF(d.fid_cur = re.fid_cur, IF(d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_PUR + ", -1, 1), 0) * (re.debit_cur - re.credit_cur)) AS f_balance_cur " +
                    "FROM fin_rec AS r INNER JOIN fin_rec_ety AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                    "r.b_del = 0 AND re.b_del = 0 " + (filterKey == null ? "" :
                    " AND re.fid_ct_sys_mov_xxx = " + ((int[])((Object[]) filterKey)[0])[0] + " " +
                    " AND re.fid_tp_sys_mov_xxx = " + ((int[])((Object[]) filterKey)[0])[1]) + " " +
                    "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp " + (filterKey == null ? "" : "AND b.id_bp = " + (Integer)((Object[]) filterKey)[1]) + " " +
                    "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp " + (filterKey == null ? "" : "AND bc.id_ct_bp =  " +
                    (SLibUtilities.compareKeys(((Object[]) filterKey)[0], SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP) ?
                    SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS)) + " " +
                    "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.b_del = 0 " +
                    "INNER JOIN erp.trnu_tp_dps AS td ON d.fid_ct_dps = td.id_ct_dps AND d.fid_cl_dps = td.id_cl_dps AND d.fid_tp_dps = td.id_tp_dps " +
                    "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                    "GROUP BY d.id_year, d.id_doc, d.dt, d.num_ser, d.num, f_tot_net, td.code, c.cur_key, cb.code " +
                    (filterKey == null ? "" : ((Integer)((Object[]) filterKey)[2] == SDataConstantsSys.TRNX_DPS_BAL_PAY_PEND ? "HAVING f_balance_cur <> 0 AND f_balance <> 0 " : "HAVING f_balance = 0 ")) + " " +
                    "ORDER BY d.id_year, d.id_doc, d.dt, d.num_ser, d.num, f_tot_net, td.code, c.cur_key, cb.code ";
                break;

            case SDataConstants.TRNX_DPS_COMMS_PEND:
                String sDateStart = piClient.getSessionXXX().getFormatters().getDbmsDateFormat().format((Date)((Object[]) filterKey)[0]);
                String sDateEnd = piClient.getSessionXXX().getFormatters().getDbmsDateFormat().format((Date)((Object[]) filterKey)[1]);

                String sqlQueryComputeBalance =  "FROM fin_rec AS sr " +
                    "INNER JOIN fin_rec_ety AS sre ON " +
                    "sr.id_year = sre.id_year AND sr.id_per = sre.id_per AND sr.id_bkc = sre.id_bkc AND sr.id_tp_rec = sre.id_tp_rec AND " +
                    "sr.id_num = sre.id_num AND sr.b_del = 0 AND sre.b_del = 0 AND sre.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0] + " AND " +
                    "sre.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1] + " " +
                    "INNER JOIN trn_dps AS sd ON " +
                    "sre.fid_dps_year_n = sd.id_year AND sre.fid_dps_doc_n = sd.id_doc AND sd.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_SAL + " AND sd.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " AND " +
                    "sd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND sd.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " AND sd.b_del = 0 " +
                    "INNER JOIN trn_dps_ety AS sde ON " +
                    "sd.id_year = sde.id_year AND sd.id_doc = sde.id_doc " +
                    "WHERE sr.id_tp_rec <> '" + SDataConstantsSys.FINU_TP_REC_FY_OPEN + "' AND sd.id_year = d.id_year AND sd.id_doc = d.id_doc AND sde.id_ety = de.id_ety " + " AND " +
                        "sr.dt <= '" + sDateEnd + "' " +
                    "GROUP BY sde.id_year, sde.id_doc) ";

                String sqlBalance = "(SELECT SUM(sre.debit - sre.credit) " + sqlQueryComputeBalance + " AS f_balance ";

                String sqlBalanceCur = "(SELECT SUM(sre.debit_cur - sre.credit_cur) " + sqlQueryComputeBalance + " AS f_balance_cur ";

                String sqlBalanceDate = "(SELECT MAX(sr.dt) " + sqlQueryComputeBalance + " AS f_dt_pay ";

                String sqlFields = "SELECT " +
                    "de.id_year AS f_id_year, " +
                    "de.id_doc AS f_id_doc, " +
                    "r.dt, " +
                    "re.fid_dps_year_n, " +
                    "re.fid_dps_doc_n, " +
                    sqlBalance + ", " +
                    sqlBalanceCur + ", " +
                    sqlBalanceDate + ", " +
                    "o.orig_comms AS f_orig_comms, " +
                    "a.bp AS f_agt, " +
                    "su.bp AS f_sup, " +
                    "d.dt AS f_dt, " +
                    "dt.code AS f_dt_code, " +
                    "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                    "b.bp AS f_bp, " +
                    "d.stot_r AS f_stot_r, " +
                    "d.exc_rate, " +
                    "d.tot_r, " +
                    "cu.cur_key AS f_cur_key, " +
                    "cob.code AS f_code, ";

                String sqlAmountAdjustment = "COALESCE((SELECT COALESCE(SUM(da.stot_r), 0) " +
                    "FROM trn_dps_dps_adj AS dda " +
                    "INNER JOIN trn_dps AS da ON " +
                    "dda.id_adj_year = da.id_year AND dda.id_adj_doc = da.id_doc AND da.b_del = 0 AND da.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                    "INNER JOIN trn_dps_ety AS dae ON " +
                    "dda.id_adj_year = dae.id_year AND dda.id_adj_doc = dae.id_doc AND dda.id_adj_ety = dae.id_ety AND dae.b_del = 0 " +
                    "WHERE de.id_year = dda.id_dps_year AND de.id_doc = dda.id_dps_doc AND de.id_ety = dda.id_dps_ety " +
                    "GROUP BY dda.id_dps_year, dda.id_dps_doc ";

                String sqlAmountSubtotalNet = "(d.stot_r - "  + sqlAmountAdjustment + "), 0)) AS f_stot_net, ";

                String sqlAmountCommission =  "COALESCE((SELECT SUM(cac.comms) " +
                    "FROM mkt_comms AS cac " +
                    "WHERE cac.b_del = 0 AND cac.b_clo = 0 AND de.id_year = cac.id_year AND de.id_doc = cac.id_doc AND de.id_ety = cac.id_ety ";
                String sqlAmountCommissionGroupBy = "GROUP BY cac.id_year, cac.id_doc), 0) ";
                String sqlAmountCommissionAsCommsByAgent =  "AS f_comms_agt, ";
                String sqlAmountCommissionAsCommsBySupervisor =  "AS f_comms_sup, ";

                String sqlAmountCommissionByAgent = sqlAmountCommission + "AND d.fid_sal_agt_n = cac.id_sal_agt " + sqlAmountCommissionGroupBy;
                String sqlAmountCommissionBySupervisor = sqlAmountCommission + "AND d.fid_sal_sup_n = cac.id_sal_agt " + sqlAmountCommissionGroupBy;

                String sqlAmountCommissionAdjustment = "COALESCE((SELECT SUM(cac.comms) " +
                    "FROM mkt_comms AS cac " +
                    "INNER JOIN trn_dps_dps_adj AS ddac ON " +
                    "cac.id_year = ddac.id_adj_year AND cac.id_doc = ddac.id_adj_doc AND cac.id_ety = ddac.id_adj_ety " +
                    "WHERE cac.b_del = 0 AND cac.b_clo = 0 AND de.id_year = ddac.id_dps_year AND de.id_doc = ddac.id_dps_doc AND de.id_ety = ddac.id_dps_ety ";
                String sqlAmountCommissionAdjustmentGroupBy = "GROUP BY cac.id_year, cac.id_doc), 0) ";
                String sqlAmountCommissionAdjustmentAsAdjCommsByAgent = "AS f_adj_comms_agt, ";
                String sqlAmountCommissionAdjustmentAsAdjCommsBySupervisor = "AS f_adj_comms_sup, ";

                String sqlAmountCommissionAdjustmentByAgent = sqlAmountCommissionAdjustment + "AND d.fid_sal_agt_n = cac.id_sal_agt " + sqlAmountCommissionAdjustmentGroupBy;
                String sqlAmountCommissionAdjustmentBySupervisor = sqlAmountCommissionAdjustment + "AND d.fid_sal_sup_n = cac.id_sal_agt " + sqlAmountCommissionAdjustmentGroupBy;

                String sqlAmountCommissionTotalByAgent = "((" + sqlAmountCommissionByAgent + ") + (" + sqlAmountCommissionAdjustmentByAgent + ")) ";
                String sqlAmountCommissionTotalAsTotalCommsByAgent = "AS f_tot_comms_agt, ";

                String sqlAmountCommissionTotalBySupervisor = "((" + sqlAmountCommissionBySupervisor + ") + (" + sqlAmountCommissionAdjustmentBySupervisor + ")) ";
                String sqlAmountCommissionTotalAsTotalCommsBySupervisor = "AS f_tot_comms_sup, ";

                String sqlAmountCommissionPaidByAgent = "COALESCE((SELECT SUM(pe.pay - pe.rfd) " +
                    "FROM mkt_comms_pay AS p " +
                    "INNER JOIN mkt_comms_pay_ety AS pe ON " +
                    "p.id_pay = pe.id_pay " +
                    "WHERE p.b_del = 0 AND pe.fk_year = de.id_year AND pe.fk_doc = de.id_doc AND pe.fk_ety = de.id_ety AND pe.fk_sal_agt = d.fid_sal_agt_n " +
                    "GROUP BY  pe.fk_year, pe.fk_doc), 0) AS f_comms_pay_agt, ";

                String sqlAmountCommissionPaidBySupervisor = "COALESCE((SELECT SUM(pe.pay - pe.rfd) " +
                    "FROM mkt_comms_pay AS p " +
                    "INNER JOIN mkt_comms_pay_ety AS pe ON " +
                    "p.id_pay = pe.id_pay " +
                    "WHERE p.b_del = 0 AND pe.fk_year = de.id_year AND pe.fk_doc = de.id_doc AND pe.fk_ety = de.id_ety AND pe.fk_sal_agt = d.fid_sal_sup_n " +
                    "GROUP BY  pe.fk_year, pe.fk_doc), 0) AS f_comms_pay_sup, ";

                String sqlAmountCommissionPaidDateByAgent = "COALESCE((SELECT MAX(p.dt) " +
                    "FROM mkt_comms_pay AS p " +
                    "INNER JOIN mkt_comms_pay_ety AS pe ON " +
                    "p.id_pay = pe.id_pay " +
                    "WHERE p.b_del = 0 AND pe.fk_year = de.id_year AND pe.fk_doc = de.id_doc AND pe.fk_ety = de.id_ety AND pe.fk_sal_agt = d.fid_sal_agt_n " +
                    "GROUP BY  pe.fk_year, pe.fk_doc), null) AS f_comms_date_pay_agt, ";

                String sqlAmountCommissionPaidDateBySupervisor = "COALESCE((SELECT MAX(p.dt) " +
                    "FROM mkt_comms_pay AS p " +
                    "INNER JOIN mkt_comms_pay_ety AS pe ON " +
                    "p.id_pay = pe.id_pay " +
                    "WHERE p.b_del = 0 AND pe.fk_year = de.id_year AND pe.fk_doc = de.id_doc AND pe.fk_ety = de.id_ety AND pe.fk_sal_agt = d.fid_sal_sup_n " +
                    "GROUP BY  pe.fk_year, pe.fk_doc), null) AS f_comms_date_pay_sup ";

                String sqlFrom = "FROM fin_rec AS r " +
                    "INNER JOIN fin_rec_ety AS re ON " +
                    "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
                    "r.b_del = 0 AND re.b_del = 0 AND re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0] + " AND " +
                    "re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1] + " " +
                    "INNER JOIN trn_dps AS d ON " +
                    "re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_SAL + " AND " +
                    "d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND " +
                    "d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " AND d.b_del = 0 " +
                    "INNER JOIN trn_dps_ety AS de ON " +
                    "d.id_year = de.id_year AND d.id_doc = de.id_doc AND d.b_del = 0 " +
                    "INNER JOIN erp.trnu_tp_dps AS dt ON " +
                    "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                    "INNER JOIN erp.cfgu_cur AS cu ON " +
                    "cu.id_cur = " + piClient.getSessionXXX().getParamsErp().getFkCurrencyId()  +  " " +
                    "INNER JOIN erp.bpsu_bpb AS cob ON " +
                    "d.fid_cob = cob.id_bpb " +
                    "INNER JOIN erp.bpsu_bp AS b ON " +
                    "d.fid_bp_r = b.id_bp " +
                    "LEFT OUTER JOIN mkt_comms AS c ON " +
                    "de.id_year = c.id_year AND de.id_doc = c.id_doc AND de.id_ety = c.id_ety AND c.b_del = 0 AND c.b_clo = 0 ";

                String sqlFromCommissionOrigin = "LEFT OUTER JOIN erp.mkts_orig_comms AS o ON " +
                    "c.fk_orig_comms = o.id_orig_comms AND o.id_orig_comms = " + SDataConstantsSys.MKTS_ORIG_COMMS_DOC + " ";

                String sqlFromByAgent = "LEFT OUTER JOIN erp.bpsu_bp AS a ON " +
                    "d.fid_sal_agt_n = a.id_bp ";

                String sqlFromBySupervisor = "LEFT OUTER JOIN erp.bpsu_bp AS su ON " +
                    "d.fid_sal_sup_n = su.id_bp ";

                String sqlFromSalesAgentByAgent = "LEFT OUTER JOIN mkt_cfg_sal_agt AS m ON " +
                    "d.fid_sal_agt_n = m.id_sal_agt ";

                String sqlFromSalesAgentBySupervisor = "LEFT OUTER JOIN mkt_cfg_sal_agt AS msu ON " +
                    "d.fid_sal_sup_n = msu.id_sal_agt ";

                String sqlWherePeriod = "WHERE r.id_tp_rec <> '" + SDataConstantsSys.FINU_TP_REC_FY_OPEN + "' AND r.dt BETWEEN '" + sDateStart + "' AND '" + sDateEnd + "' AND d.b_del = 0 AND de.b_del = 0 AND ";

                String sqlWhereCommisionsOpen = "d.b_close_comms = 0 ";

                String sqlGroupBy = "GROUP BY de.id_year, de.id_doc ";

                String sqlHaving = "HAVING f_balance = 0 ";

                sSql = sqlFields +
                    sqlAmountAdjustment + "), 0) AS f_adj, " +
                    sqlAmountSubtotalNet + " " +
                    sqlAmountCommissionByAgent  + sqlAmountCommissionAsCommsByAgent +
                    sqlAmountCommissionBySupervisor  + sqlAmountCommissionAsCommsBySupervisor +
                    sqlAmountCommissionAdjustmentByAgent + sqlAmountCommissionAdjustmentAsAdjCommsByAgent +
                    sqlAmountCommissionAdjustmentBySupervisor + sqlAmountCommissionAdjustmentAsAdjCommsBySupervisor +
                    sqlAmountCommissionTotalByAgent + sqlAmountCommissionTotalAsTotalCommsByAgent +
                    sqlAmountCommissionTotalBySupervisor + sqlAmountCommissionTotalAsTotalCommsBySupervisor +
                    sqlAmountCommissionPaidByAgent +
                    sqlAmountCommissionPaidBySupervisor +
                    sqlAmountCommissionPaidDateByAgent +
                    sqlAmountCommissionPaidDateBySupervisor +
                    sqlFrom +
                    sqlFromCommissionOrigin +
                    sqlFromByAgent +
                    sqlFromBySupervisor +
                    sqlFromSalesAgentByAgent +
                    sqlFromSalesAgentBySupervisor +
                    sqlWherePeriod +
                    sqlWhereCommisionsOpen +
                    sqlGroupBy +
                    sqlHaving +
                    "ORDER BY f_orig_comms, f_agt, f_dt, f_dt_code, f_num; ";

                i = 0;
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "f_id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "f_id_doc");

                i = 0;
                aoQueryFields = new STableField[18];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "f_dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_dt_code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_bp");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_adj");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_net");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "f_dt_pay");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_agt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_tot_comms_agt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_comms_pay_agt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "f_comms_date_pay_agt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_sup");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_tot_comms_sup");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "f_comms_pay_sup");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "f_comms_date_pay_sup");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_cur_key");

                break;

            case SDataConstants.TRNX_TP_DPS:
                aoPkFields = new STableField[3];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ct_dps");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cl_dps");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_dps");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_dps");

                sSql = "SELECT * FROM erp.trnu_tp_dps WHERE " + (((int[])filterKey)[0] == SDataConstantsSys.TRNS_CT_DPS_PUR ?
                    " (id_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " AND id_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " AND id_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " OR " +
                    " id_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " AND id_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " AND id_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + ") " :
                    " (id_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0] + " AND id_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1] + " AND id_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2] + " OR " +
                    " id_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND id_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND id_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + ") ");
                break;
                
            case SDataConstants.TRN_DPS_ACK:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dps.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dps.id_doc");
                
                i = 0;
                aoQueryFields = new STableField[9];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "_num");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dps.num_ref");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DATE, "dps.dt");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cob.code");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bpb.bpb");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_FLOAT, "dps.tot_cur_r");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "cur.cur_key");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "_count_ack");
                
                sSql = "SELECT dps.id_year, dps.id_doc, "
                        + "tp.code, "
                        + "CONCAT(dps.num_ser, IF(length(dps.num_ser) = 0, '', '-'), dps.num) AS _num, "
                        + "dps.num_ref, "
                        + "dps.dt, "
                        + "cob.code, "
                        + "bpb.bpb, "
                        + "dps.tot_cur_r, "
                        + "cur.cur_key, "
                        + "(SELECT COUNT(*) FROM trn_dps_ack AS ack WHERE ack.fid_year = dps.id_year AND ack.fid_doc = dps.id_doc AND NOT ack.b_del) AS _count_ack "
                        + "FROM trn_dps AS dps "
                        + "INNER JOIN erp.trnu_tp_dps AS tp ON dps.fid_ct_dps = tp.id_ct_dps AND dps.fid_cl_dps = tp.id_cl_dps AND dps.fid_tp_dps = tp.id_tp_dps "
                        + "INNER JOIN erp.bpsu_bp AS bp ON dps.fid_bp_r = bp.id_bp "
                        + "INNER JOIN erp.bpsu_bpb AS cob ON dps.fid_cob = cob.id_bpb "
                        + "INNER JOIN erp.bpsu_bpb AS bpb ON dps.fid_bpb = bpb.id_bpb "
                        + "INNER JOIN erp.cfgu_cur AS cur ON dps.fid_cur = cur.id_cur "
                        + "WHERE dps.fid_ct_dps = " + ((int[]) ((Object[]) filterKey)[0])[0] + " AND dps.fid_cl_dps = " + ((int[]) ((Object[]) filterKey)[0])[1] + " "
                        + "AND dps.b_del = 0 AND fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND dps.b_dps_ack = 0 "
                        + "AND bp.id_bp = " + ((int[]) ((Object[]) filterKey)[1])[0] + " "
                        + "GROUP BY dps.id_year, dps.id_doc "
                        + "ORDER BY tp.code, _num, dps.dt, dps.id_year, dps.id_doc ";
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatMkt(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";

        switch (pnDataType) {
            case SDataConstants.MKTS_TP_DISC_APP:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_disc_app");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_disc_app");

                sSql = "SELECT id_tp_disc_app, tp_disc_app " +
                        "FROM erp.mkts_tp_disc_app " +
                        "WHERE b_del = false " +
                        "ORDER BY tp_disc_app ";
                break;

            case SDataConstants.MKTU_TP_CUS:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_cus");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_cus");

                sSql = "SELECT id_tp_cus, tp_cus " +
                        "FROM mktu_tp_cus " +
                        "WHERE b_del = 0 " +
                        "ORDER BY tp_cus ";
                break;

            case SDataConstants.MKT_PLIST:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_plist");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "plist");

                sSql = "SELECT id_plist, plist " +
                        "FROM mkt_plist " +
                        "WHERE b_del = false " + (filterKey == null ? "" : "AND fid_ct_dps = " + ((Integer) filterKey == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstantsSys.TRNS_CT_DPS_SAL : SDataConstantsSys.TRNS_CT_DPS_PUR)) + " " +
                        "ORDER BY plist; ";
                break;

            case SDataConstants.MKTU_SAL_ROUTE:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_sal_route");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "sal_route");

                sSql = "SELECT id_sal_route, sal_route " +
                        "FROM mktu_sal_route " +
                        "WHERE b_del = false " +
                        "ORDER BY sal_route ";
                break;

            case SDataConstants.MKTU_MKT_SEGM:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_mkt_segm");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "mkt_segm");

                sSql = "SELECT id_mkt_segm, mkt_segm " +
                        "FROM mktu_mkt_segm " +
                        "WHERE b_del = 0 " +
                        "ORDER BY mkt_segm  ";
                break;

            case SDataConstants.MKTU_MKT_SEGM_SUB:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_mkt_segm");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_mkt_sub");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "mkt_segm_sub");

                sSql = "SELECT id_mkt_segm, id_mkt_sub, mkt_segm_sub " +
                        "FROM mktu_mkt_segm_sub " +
                        "WHERE b_del = 0 AND id_mkt_segm = " + (filterKey == null ? 0 : ((int[]) filterKey)[0]) + " " +
                        "ORDER BY mkt_segm_sub";
                break;

            case SDataConstants.MKTU_DIST_CHAN:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_dist_chan");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "dist_chan");

                sSql = "SELECT id_dist_chan, dist_chan " +
                        "FROM mktu_dist_chan " +
                        "WHERE b_del = 0 " +
                        "ORDER BY dist_chan ";
                break;

            case SDataConstants.MKTU_TP_SAL_AGT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_sal_agt");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "tp_sal_agt");

                sSql = "SELECT id_tp_sal_agt, tp_sal_agt " +
                        "FROM mktu_tp_sal_agt " +
                        "WHERE b_del = 0 " +
                        "ORDER BY tp_sal_agt ";
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatLog(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";

        switch (pnDataType) {
            case SModConsts.LOGS_TP_MOT:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_mot");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "name");

                sSql = "SELECT id_tp_mot, name FROM " + SModConsts.TablesMap.get(pnDataType) + " WHERE b_del = 0 ORDER BY sort ";
                break;

            case SModConsts.LOGS_TP_CAR:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_car");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "name");

                sSql = "SELECT id_tp_car, name FROM " + SModConsts.TablesMap.get(pnDataType) + " WHERE b_del = 0 ORDER BY sort ";
                break;

            case SModConsts.LOGS_INC:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_inc");

                i = 0;
                aoQueryFields = new STableField[2];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "name");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "code");

                sSql = "SELECT id_inc, name, code FROM erp.logs_inc WHERE b_del = 0 ORDER BY sort ";
                break;

            case SModConsts.LOGU_TP_VEH:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_tp_veh");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "name");

                sSql = "SELECT id_tp_veh, name FROM " + SModConsts.TablesMap.get(pnDataType) + " WHERE b_del = 0 ORDER BY name, id_tp_veh ";
                break;

            case SModConsts.LOG_VEH:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_veh");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "name");

                sSql = "SELECT id_veh, name FROM log_veh " +
                        "WHERE b_del = 0 " + (filterKey == null ? "" : " AND fk_tp_veh = " + ((int[]) filterKey)[0] + " ") +
                        "ORDER BY name, id_veh ";
                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    private static SQueryRequest getSettingsCatMfg(erp.client.SClientInterface piClient, int pnDataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> params) {
        int i = 0;
        STableField[] aoPkFields = null;
        STableField[] aoQueryFields = null;
        ArrayList<STableField> aPkFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryFields = new ArrayList<STableField>();
        ArrayList<STableField> aQueryAdditionalFields = new ArrayList<STableField>();
        ArrayList<SLibRpnArgument>[] aaRpnArguments = null;
        String sSql = "";

        switch (pnDataType) {
            case SDataConstants.MFGU_LINE:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_line");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "line");

                sSql = "SELECT id_line, line " +
                        "FROM mfgu_line " +
                        "WHERE b_del = 0 " +
                        "ORDER BY line; ";
                break;

            case SDataConstants.MFG_BOM:
                aoPkFields = new STableField[1];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_bom");

                i = 0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "bom");

                sSql = "SELECT id_bom, bom " +
                        "FROM mfg_bom " +
                        "WHERE b_del = 0 AND fid_item_n IS NULL " +
                        (filterKey == null ? "" : "AND fid_item = " + ((int[]) filterKey)[0] + " AND (ts_end_n IS NULL OR ts_end_n >= NOW()) ") +
                        "ORDER BY bom; ";
                break;

            case SDataConstants.MFG_BOM_SUB:
                aoPkFields = new STableField[4];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bs.id_bom");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bs.id_item");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bs.id_unit");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "bs.id_bom_sub");

                i = 0;
                aoQueryFields = new STableField[4];
                if (piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                }
                else {
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item");
                    aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "i.item_key");
                }
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "u.symbol");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_DOUBLE, "bs.per");

                sSql = "SELECT bs.*, i.item_key, i.item, u.symbol " +
                        "FROM mfg_bom AS b " +
                        "INNER JOIN mfg_bom_sub AS bs ON b.id_bom = bs.id_bom " +
                        (filterKey == null ? "" : "AND b.fid_item_n = " + ((int[]) filterKey)[2] + " AND b.fid_unit_n = " + ((int[]) filterKey)[3]) + " " +
                        "INNER JOIN erp.itmu_item AS i ON bs.fid_item_sub = i.id_item " +
                        "INNER JOIN erp.itmu_unit AS u ON bs.fid_unit_sub = u.id_unit " +
                        "WHERE bs.b_del = 0 " +
                        (filterKey == null ? "" : "AND bs.id_item = " + ((int[]) filterKey)[0] + " AND bs.id_unit = " + ((int[]) filterKey)[1]) + " " +
                        "ORDER BY " + (!piClient.getSessionXXX().getParamsErp().getIsItemKeyApplying() ? "i.item " :
                            (piClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        "i.item_key, i.item" : "i.item, i.item_key "));
                break;

            case SDataConstants.MFG_ORD:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_ord");

                i = 0;
                aoQueryFields = new STableField[3];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "o.ref");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.tp");

                sSql = "SELECT o.id_year, o.id_ord, CONCAT(o.id_year, '-', o.num) AS f_item, o.ref, t.tp " +
                        "FROM mfg_ord AS o " +
                        "INNER JOIN erp.mfgu_tp_ord AS t ON o.fid_tp_ord = t.id_tp " +
                        "WHERE o.b_del = 0 " +
                        (filterKey == null ? "" : "AND o.fid_st_ord IN (" + (String) ((Object[]) filterKey)[0] + ") " +
                            ((Integer) ((Object[]) filterKey)[1] == SDataConstants.MFGX_ORD_MAIN_NA ? "" :
                             (Integer) ((Object[]) filterKey)[1] == SDataConstants.MFGX_ORD_MAIN_FA ? "AND t.b_req_fat = 0 AND t.id_tp > 1 " :
                             (Integer) ((Object[]) filterKey)[1] == SDataConstants.MFGX_ORD_MAIN_CH ? "AND t.b_req_fat = 1 AND t.id_tp > 1 " : "") +
                        " AND o.b_for = " + ((Boolean)((Object[]) filterKey)[2] == false ? "0 " : "1 ")) +
                        "ORDER BY o.id_year, o.id_ord, o.ref ";
                break;

            case SDataConstants.MFGX_ORD:
                aoPkFields = new STableField[2];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_year");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "o.id_ord");

                i = 0;
                aoQueryFields = new STableField[3];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "f_item");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "o.ref");
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "t.tp");

                sSql = "SELECT o.id_year, o.id_ord, CONCAT(o.id_year, '-', o.num) AS f_item, o.ref, t.tp " +
                        "FROM mfg_ord AS o " +
                        "INNER JOIN erp.mfgu_tp_ord AS t ON o.fid_tp_ord = t.id_tp " +
                        "WHERE o.b_del = 0 " +
                        (filterKey == null ? "" : "AND o.fid_st_ord IN (" + (String) ((Object[]) filterKey)[0] + ") " +
                        (((Object[]) filterKey).length == 1 ? "" : "AND o.fid_ord_year_n = " + ((int[]) ((Object[]) filterKey)[1])[0] + " AND o.fid_ord_n = " + ((int[]) ((Object[]) filterKey)[1])[1] + " ")) +
                        "ORDER BY o.id_year, o.id_ord, o.ref ";
                break;

            case SDataConstants.MFG_LINE:
                aoPkFields = new STableField[3];
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cob");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ent");
                aoPkFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_line");

                i=0;
                aoQueryFields = new STableField[1];
                aoQueryFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "line");

                sSql = "SELECT id_cob, id_ent, id_line, line " +
                        "FROM mfg_line " +
                        "WHERE b_del = 0 " + (filterKey == null ? "" : "AND id_cob = " + ((int[]) filterKey)[0] + " AND id_ent = " + ((int[]) filterKey)[1]) + " " +
                        "ORDER BY line ";

                break;

            default:
                piClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (aoPkFields != null) {
            for (STableField tableField : aoPkFields) {
                aPkFields.add(tableField);
            }
        }

        if (aoQueryFields != null) {
            for (STableField tableField : aoQueryFields) {
                aQueryFields.add(tableField);
            }

            aaRpnArguments = new ArrayList[aoQueryFields.length];
        }

        return new SQueryRequest(aPkFields, aQueryFields, aQueryAdditionalFields, aaRpnArguments, new String[] { sSql });
    }

    /**
     * @param dataType Constants defined in erp.data.SDataConstants.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.lib.table.STableRow> getTableRows(erp.client.SClientInterface client, int dataType, java.lang.Object filterKey) {
        return getTableRows(client, dataType, filterKey, null);
    }

    /**
     * @param dataType Constants defined in erp.data.SDataConstants.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.lib.table.STableRow> getTableRows(erp.client.SClientInterface client, int dataType, java.lang.Object filterKey, java.util.HashMap<Integer, Object> paramsMap) {
        Vector<STableRow> vector = null;
        SQueryRequest queryRequest = null;
        SServerRequest request = null;
        SServerResponse response = null;

        if (SDataUtilities.isCatalogueCfg(dataType)) {
            queryRequest = getSettingsCatCfg(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueUsr(dataType)) {
            queryRequest = getSettingsCatUsr(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueLoc(dataType)) {
            queryRequest = getSettingsCatLoc(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueBps(dataType)) {
            queryRequest = getSettingsCatBps(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueItm(dataType)) {
            queryRequest = getSettingsCatItm(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueFin(dataType)) {
            queryRequest = getSettingsCatFin(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueTrn(dataType)) {
            queryRequest = getSettingsCatTrn(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueTrnPur(dataType)) {

        }
        else if (SDataUtilities.isCatalogueTrnSal(dataType)) {

        }
        else if (SDataUtilities.isCatalogueTrnInv(dataType)) {

        }
        else if (SDataUtilities.isCatalogueMkt(dataType)) {
            queryRequest = getSettingsCatMkt(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueLog(dataType)) {
            queryRequest = getSettingsCatLog(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueMfg(dataType)) {
            queryRequest = getSettingsCatMfg(client, dataType, filterKey, paramsMap);
        }
        else if (SDataUtilities.isCatalogueHrs(dataType)) {

        }
        else {
            client.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        try {
            request = new SServerRequest(SServerConstants.REQ_DB_QUERY, queryRequest);
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                vector = (Vector<STableRow>) response.getPacket();
            }
        }
        catch(java.lang.Exception e) {
            SLibUtilities.renderException(SDataReadTableRows.class.getName(), e);
        }

        return vector;
    }
}
