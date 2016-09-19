/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
 */
package erp.mfin.data;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mitm.data.SDataItem;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.sql.ResultSet;
import java.util.Vector;
import sa.lib.SLibUtils;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SFinAccountUtilities {

    /**
     * Obtain account for tax, DPS category and date supplied.
     *
     * @param client ERP Client interface.
     * @param keyTax Tax key (basic tax ID, tax ID).
     * @param idDpsCategory DPS category ID.
     * @param dateStart Starting date.
     * @param idTaxAccountType Account type. Constants defined in SDataConstantsSys class.
     * @return Account ID, if any, for parameters supplied, otherwise empty <code>String</code>.
     */
    public static java.lang.String obtainTaxAccountId(erp.client.SClientInterface client, 
            int[] keyTax, int idDpsCategory, java.util.Date dateStart, int idTaxAccountType) {
        SServerRequest request = null;
        SServerResponse response = null;
        String idAccount = "";

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_TAX_ID, new Object[] { keyTax, idDpsCategory, dateStart, idTaxAccountType });
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            SLibUtilities.renderException(SFinAccountUtilities.class.getName(), new Exception(response.getMessage()));
        }
        else {
            idAccount = (String) response.getPacket();
        }

        return idAccount;
    }

    /**
     * Obtain account for tax, DPS category and date supplied.
     *
     * @param keyTax Tax key (basic tax ID, tax ID).
     * @param idDpsCategory DPS category ID.
     * @param dateStart Starting date.
     * @param idTaxAccountType Account type. Constants defined in SDataConstantsSys class.
     * @param statement Database connection statement.
     * @return Account ID, if any, for parameters supplied, otherwise empty <code>String</code>.
     */
    public static java.lang.String obtainTaxAccountId(
            int[] keyTax, int idDpsCategory, java.util.Date dateStart, int idTaxAccountType, java.sql.Statement statement) throws java.lang.Exception {

        String sql = "";
        String field = "";
        String idAccount = "";
        String idEmptyAccount = "";
        ResultSet resultSet = null;

        // Define empty account:

        sql = "SELECT fmt_id_acc FROM erp.cfg_param_erp WHERE id_erp = 1 ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            idEmptyAccount = resultSet.getString("fmt_id_acc").replace('9', '0');
        }

        // Obtain tax account ID:

        switch (idTaxAccountType) {
            case SDataConstantsSys.FINX_ACC_PAY:
                field = "fid_acc_pay";
                break;
            case SDataConstantsSys.FINX_ACC_PAY_PEND:
                field = "fid_acc_pay_pend";
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        sql = "SELECT id_dt_start, " + field + " AS f_acc FROM fin_acc_tax " +
                "WHERE id_tax_bas = " + keyTax[0] + " AND id_tax = " + keyTax[1] + " AND id_ct_dps = " + idDpsCategory + " AND " +
                "id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND b_del = 0 " +
                "ORDER BY id_dt_start DESC ";
        resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Tax account)");
        }
        else {
            if (resultSet.getString("f_acc").compareTo(idEmptyAccount) == 0) {
                throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Tax account)");
            }

            idAccount = resultSet.getString("f_acc");
        }

        return idAccount;
    }

    /**
     * Obtain account and cost center configurations for business partner, bookkeeping center and date supplied.
     *
     * @param client client Client interface.
     * @param idBizPartner Business partner ID.
     * @param idBizPartnenrCategory Business partner category ID.
     * @param idBkc Bookkeeping center ID.
     * @param dateStart Starting date.
     * @param idBizPartnerAccountType Account type. Constants defined in SDataConstantsSys class.
     * @param isDebit Is debit movement.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainBizPartnerAccountConfigs(erp.client.SClientInterface client,
            int idBizPartner, int idBizPartnenrCategory, int idBkc, java.util.Date dateStart, int idBizPartnerAccountType, boolean isDebit) throws java.lang.Exception {

        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFinAccountConfigEntry> accountConfigs = null;

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_BP, new Object[] { idBizPartner, idBizPartnenrCategory, idBkc, dateStart, idBizPartnerAccountType, isDebit });
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            SLibUtilities.renderException(SFinAccountUtilities.class.getName(), new Exception(response.getMessage()));
        }
        else {
            accountConfigs = (Vector<SFinAccountConfigEntry>) response.getPacket();
        }

        return accountConfigs;
    }

    /**
     * Obtain account and cost center configurations for business partner, bookkeeping center and date supplied.
     *
     * @param idBizPartner Business partner ID.
     * @param idBizPartnerCategory Business partner category ID.
     * @param idBkc Bookkeeping center ID.
     * @param dateStart Starting date.
     * @param idBizPartnerAccountType Account type. Constants defined in SDataConstantsSys class.
     * @param isDebit Is debit movement.
     * @param statement Database connection statement.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainBizPartnerAccountConfigs(
            int idBizPartner, int idBizPartnerCategory, int idBkc, java.util.Date dateStart, int idBizPartnerAccountType, boolean isDebit, java.sql.Statement statement) throws java.lang.Exception {

        int idAccount = 0;
        String sql = "";
        String idEmptyAccount = "";
        ResultSet resultSet = null;
        Vector<SFinAccountConfigEntry> accountConfigs = new Vector<SFinAccountConfigEntry>();

        // Define empty account:

        sql = "SELECT fmt_id_acc FROM erp.cfg_param_erp WHERE id_erp = 1 ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            idEmptyAccount = resultSet.getString("fmt_id_acc").replace('9', '0');
        }

        // Check if there is an account for business partner:

        sql = "SELECT abb.id_dt_start, abb.id_acc_bp " +
                "FROM fin_acc_bp_bp AS abb INNER JOIN fin_acc_bp AS ab ON " +
                "abb.id_acc_bp = ab.id_acc_bp AND ab.b_del = 0 " +
                "WHERE abb.id_bp = " + idBizPartner + " AND abb.id_ct_bp = " + idBizPartnerCategory + " AND abb.id_bkc = " + idBkc + " AND " +
                "abb.id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND abb.b_del = 0 " +
                "ORDER BY abb.id_dt_start DESC ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            idAccount = resultSet.getInt("abb.id_acc_bp");
        }
        else {
            // Check if there is an account for business partner type, but first, obtain business partner type key:

            sql = "SELECT fid_ct_bp, fid_tp_bp FROM erp.bpsu_bp_ct WHERE id_bp = " + idBizPartner + " AND id_ct_bp = " + idBizPartnerCategory + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Business partner category for BP: " + idBizPartner + ")");
            }
            else {
                sql = "SELECT atb.id_dt_start, atb.id_acc_bp " +
                        "FROM fin_acc_bp_tp_bp AS atb INNER JOIN fin_acc_bp AS ab ON " +
                        "atb.id_acc_bp = ab.id_acc_bp AND ab.b_del = 0 " +
                        "WHERE atb.id_ct_bp = " + resultSet.getInt("fid_ct_bp") + " AND atb.id_tp_bp = " + resultSet.getInt("fid_tp_bp") + " AND atb.id_bkc = " + idBkc + " AND " +
                        "atb.id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND atb.b_del = 0 " +
                        "ORDER BY atb.id_dt_start DESC ";
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    idAccount = resultSet.getInt("atb.id_acc_bp");
                }
            }
        }

        if (idAccount == 0) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Business partner accounts for BP: " + idBizPartner + ")");
        }
        else {
            sql = "SELECT abe.fid_acc, abe.fid_cc_n, abe.per " +
                    "FROM fin_acc_bp ab INNER JOIN fin_acc_bp_ety AS abe ON " +
                    "ab.id_acc_bp = abe.id_acc_bp AND ab.id_acc_bp = " + idAccount + " AND ab.b_del = 0 AND abe.id_tp_acc_bp = " + idBizPartnerAccountType + " AND " +
                    "abe.fid_tp_bkr IN (" + SDataConstantsSys.FINS_TP_BKR_ALL + ", " + (isDebit ? SDataConstantsSys.FINS_TP_BKR_DBT : SDataConstantsSys.FINS_TP_BKR_CDT) + ") " +
                    "ORDER BY abe.fid_acc, abe.fid_cc_n, abe.per ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                if (resultSet.getString("abe.fid_acc").compareTo(idEmptyAccount) == 0) {
                    throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Account for business partner for BP: " + idBizPartner + ")");
                }
                else {
                    accountConfigs.add(new SFinAccountConfigEntry(resultSet.getString("abe.fid_acc"), resultSet.getString("abe.fid_cc_n"), resultSet.getDouble("abe.per")));
                }
            }

            if (accountConfigs.isEmpty()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Account for business partner for BP: " + idBizPartner + ")");
            }
        }

        return accountConfigs;
    }

    /**
     * Obtain account and cost center configurations for item, bookkeeping center and date supplied.
     *
     * @param client client Client interface.
     * @param idItem Item ID.
     * @param idBkc Bookkeeping center ID.
     * @param dateStart Starting date.
     * @param idItemAccountType Account type. Constants defined in SDataConstantsSys class.
     * @param isDebit Is debit movement.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainItemAccountConfigs(erp.client.SClientInterface client,
            int idItem, int idBkc, java.util.Date dateStart, int idItemAccountType, boolean isDebit) throws java.lang.Exception {

        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFinAccountConfigEntry> accountConfigs = null;

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_ITEM, new Object[] { idItem, idBkc, dateStart, idItemAccountType, isDebit });
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            SLibUtilities.renderException(SFinAccountUtilities.class.getName(), new Exception(response.getMessage()));
        }
        else {
            accountConfigs = (Vector<SFinAccountConfigEntry>) response.getPacket();
        }

        return accountConfigs;
    }

    /**
     * Obtain account and cost center configurations for item, bookkeeping center and date supplied.
     *
     * @param idItem Item ID.
     * @param idBkc Bookkeeping center ID.
     * @param dateStart Starting date.
     * @param idItemAccountType Account type. Constants defined in SDataConstantsSys class.
     * @param isDebit Is debit movement.
     * @param statement Database connection statement.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainItemAccountConfigs(
            int idItem, int idBkc, java.util.Date dateStart, int idItemAccountType, boolean isDebit, java.sql.Statement statement) throws java.lang.Exception {

        int idAccount = 0;
        int idReference = 0;
        int[] linkTypes = null;
        String sql = "";
        String idEmptyAccount = "";
        ResultSet resultSet = null;
        SDataItem item = new SDataItem();
        Vector<SFinAccountConfigEntry> accountConfigs = new Vector<SFinAccountConfigEntry>();

        // Define empty account:

        sql = "SELECT fmt_id_acc FROM erp.cfg_param_erp WHERE id_erp = 1 ";
        resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            idEmptyAccount = resultSet.getString("fmt_id_acc").replace('9', '0');
        }

        // Read item registry:

        if (item.read(new int[] { idItem }, statement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Item registry for item: " + idItem + ")");
        }

        linkTypes = new int[] {
            SDataConstantsSys.TRNS_TP_LINK_ITEM,
            SDataConstantsSys.TRNS_TP_LINK_MFR,
            SDataConstantsSys.TRNS_TP_LINK_BRD,
            SDataConstantsSys.TRNS_TP_LINK_LINE,
            SDataConstantsSys.TRNS_TP_LINK_IGEN,
            SDataConstantsSys.TRNS_TP_LINK_IGRP,
            SDataConstantsSys.TRNS_TP_LINK_IFAM,
            SDataConstantsSys.TRNS_TP_LINK_TP_ITEM,
            SDataConstantsSys.TRNS_TP_LINK_CL_ITEM,
            SDataConstantsSys.TRNS_TP_LINK_CT_ITEM,
            SDataConstantsSys.TRNS_TP_LINK_ALL
        };

        for (int type : linkTypes) {
            // Check if there is an account for link type:

            switch (type) {
                case SDataConstantsSys.TRNS_TP_LINK_ITEM:
                    idReference = idItem;
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_MFR:
                    idReference = item.getFkManufacturerId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_BRD:
                    idReference = item.getFkBrandId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_LINE:
                    idReference = item.getFkItemLineId_n();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IGEN:
                    idReference = item.getFkItemGenericId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IGRP:
                    idReference = item.getDbmsDataItemGeneric().getFkItemGroupId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IFAM:
                    idReference = item.getDbmsDataItemGeneric().getDbmsDataItemGroup().getFkItemFamilyId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                    idReference = item.getDbmsDataItemGeneric().getFkItemTypeId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
                    idReference = item.getDbmsDataItemGeneric().getFkItemClassId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
                    idReference = item.getDbmsDataItemGeneric().getFkItemCategoryId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_ALL:
                    idReference = SLibConstants.UNDEFINED;
                    break;
                default:
            }

            sql = "SELECT aii.id_dt_start, aii.id_acc_item " +
                    "FROM fin_acc_item_item AS aii INNER JOIN fin_acc_item AS ai ON " +
                    "aii.id_acc_item = ai.id_acc_item AND ai.b_del = 0 " +
                    "WHERE aii.id_tp_link = " + type + " AND aii.id_ref = " + idReference + " AND aii.id_bkc = " + idBkc + " AND " +
                    "aii.id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(dateStart) + "' AND aii.b_del = 0 " +
                    "ORDER BY aii.id_dt_start DESC, aii.id_acc_item ";
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                idAccount = resultSet.getInt("aii.id_acc_item");
                break;
            }
        }

        if (idAccount == 0) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Item accounts for item: " + idItem + ")");
        }
        else {
            sql = "SELECT aie.per, aie.fid_acc_n, aie.fid_cc_n " +
                    "FROM fin_acc_item ai INNER JOIN fin_acc_item_ety AS aie ON " +
                    "ai.id_acc_item = aie.id_acc_item AND ai.id_acc_item = " + idAccount + " AND ai.b_del = 0 AND aie.id_tp_acc_item = " + idItemAccountType + " AND " +
                    "aie.fid_tp_bkr IN (" + SDataConstantsSys.FINS_TP_BKR_ALL + ", " + (isDebit ? SDataConstantsSys.FINS_TP_BKR_DBT : SDataConstantsSys.FINS_TP_BKR_CDT) + ") " +
                    "ORDER BY aie.fid_acc_n, aie.fid_cc_n, aie.per ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                if (resultSet.getString("aie.fid_acc_n") == null || resultSet.getString("aie.fid_acc_n").compareTo(idEmptyAccount) == 0) {
                    throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Account for item: " + idItem + ")");
                }
                else {
                    accountConfigs.add(new SFinAccountConfigEntry(resultSet.getString("aie.fid_acc_n"), resultSet.getString("aie.fid_cc_n"), resultSet.getDouble("aie.per")));
                }
            }

            if (accountConfigs.isEmpty()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Account for item: " + idItem + ")");
            }
        }

        return accountConfigs;
    }

    public static double[] prorateAmount(final double amount, final double[] percentages) {
        int index = -1;
        double total = 0;
        double amountMax = Double.MIN_VALUE;
        double amountStd = SLibUtilities.round(amount, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        double[] amounts = new double[percentages.length];

        // Prorate value:

        for (int i = 0; i < percentages.length; i++) {
            amounts[i] = SLibUtilities.round(amount * percentages[i], SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            total += amounts[i];
        }

        // Round prorated amounts if needed:

        if (total != amountStd) {
            for (int i = 0; i < amounts.length; i++) {
                if (amounts[i] > amountMax) {
                    amountMax = amounts[i];
                    index = i;
                }
            }

            if (index != -1) {
                amounts[index] = SLibUtils.round(amounts[index] + (amountStd - total), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
            }
        }

        return amounts;
    }
}
