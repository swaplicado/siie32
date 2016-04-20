/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
     * @param piClient ERP Client interface.
     * @param panTaxKey Tax key (basic tax ID, tax ID).
     * @param pnDpsCategoryId DPS category ID.
     * @param ptDateStart Starting date.
     * @param pnTaxAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @return Account ID, if any, for parameters supplied, otherwise empty <code>String</code>.
     */
    public static java.lang.String obtainTaxAccountId(erp.client.SClientInterface piClient, 
            int[] panTaxKey, int pnDpsCategoryId, java.util.Date ptDateStart, int pnTaxAccountTypeId) {
        SServerRequest request = null;
        SServerResponse response = null;
        String sAccountId = "";

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_TAX_ID, new Object[] { panTaxKey, pnDpsCategoryId, ptDateStart, pnTaxAccountTypeId });
        response = piClient.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            SLibUtilities.renderException(SFinAccountUtilities.class.getName(), new Exception(response.getMessage()));
        }
        else {
            sAccountId = (String) response.getPacket();
        }

        return sAccountId;
    }

    /**
     * Obtain account for tax, DPS category and date supplied.
     *
     * @param panTaxKey Tax key (basic tax ID, tax ID).
     * @param pnDpsCategoryId DPS category ID.
     * @param ptDateStart Starting date.
     * @param pnTaxAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @param poStatement Database connection statement.
     * @param poDateFormatDbms SimpleDateFormat object for DBMS specific as text date format.
     * @return Account ID, if any, for parameters supplied, otherwise empty <code>String</code>.
     */
    public static java.lang.String obtainTaxAccountId(
            int[] panTaxKey, int pnDpsCategoryId, java.util.Date ptDateStart, int pnTaxAccountTypeId, java.sql.Statement poStatement) throws java.lang.Exception {

        String sSql = "";
        String sField = "";
        String sAccount = "";
        String sEmptyAccount = "";
        ResultSet oResultSet = null;

        // Define empty account:

        sSql = "SELECT fmt_id_acc FROM erp.cfg_param_erp WHERE id_erp = 1 ";
        oResultSet = poStatement.executeQuery(sSql);
        if (oResultSet.next()) {
            sEmptyAccount = oResultSet.getString("fmt_id_acc").replace('9', '0');
        }

        // Obtain tax account ID:

        switch (pnTaxAccountTypeId) {
            case SDataConstantsSys.FINX_ACC_PAY:
                sField = "fid_acc_pay";
                break;
            case SDataConstantsSys.FINX_ACC_PAY_PEND:
                sField = "fid_acc_pay_pend";
                break;
            case SDataConstantsSys.FINX_ACC_PAY_PEND_ADV:
                sField = "fid_acc_pay_pend_adv";
                break;
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        sSql = "SELECT id_dt_start, " + sField + " AS f_acc FROM fin_acc_tax " +
                "WHERE id_tax_bas = " + panTaxKey[0] + " AND id_tax = " + panTaxKey[1] + " AND id_ct_dps = " + pnDpsCategoryId + " AND " +
                "id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(ptDateStart) + "' AND b_del = 0 " +
                "ORDER BY id_dt_start DESC ";
        oResultSet = poStatement.executeQuery(sSql);
        if (!oResultSet.next()) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Tax account)");
        }
        else {
            if (oResultSet.getString("f_acc").compareTo(sEmptyAccount) == 0) {
                throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Tax account)");
            }

            sAccount = oResultSet.getString("f_acc");
        }

        return sAccount;
    }

    /**
     * Obtain account and cost center configurations for business partner, bookkeeping center and date supplied.
     *
     * @param piClient ERP Client interface.
     * @param pnBpId Business partner ID.
     * @param pnBpCategoryId Business partner category ID.
     * @param pnBkcId Bookkeeping center ID.
     * @param ptDateStart Starting date.
     * @param pnBpAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @param pbIsDebit Is debit movement.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainBizPartnerAccountConfigs(erp.client.SClientInterface piClient,
            int pnBpId, int pnBpCategoryId, int pnBkcId, java.util.Date ptDateStart, int pnBpAccountTypeId, boolean pbIsDebit) throws java.lang.Exception {

        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFinAccountConfigEntry> accountConfigs = null;

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_BP, new Object[] { pnBpId, pnBpCategoryId, pnBkcId, ptDateStart, pnBpAccountTypeId, pbIsDebit });
        response = piClient.getSessionXXX().request(request);

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
     * @param pnBpId Business partner ID.
     * @param pnBpCategoryId Business partner category ID.
     * @param pnBkcId Bookkeeping center ID.
     * @param ptDateStart Starting date.
     * @param pnBpAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @param pbIsDebit Is debit movement.
     * @param poStatement Database connection statement.
     * @param poDateFormatDbms SimpleDateFormat object for DBMS specific as text date format.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainBizPartnerAccountConfigs(
            int pnBpId, int pnBpCategoryId, int pnBkcId, java.util.Date ptDateStart, int pnBpAccountTypeId, boolean pbIsDebit, java.sql.Statement poStatement) throws java.lang.Exception {

        int nAccountId = 0;
        String sSql = "";
        String sEmptyAccount = "";
        ResultSet oResultSet = null;
        Vector<SFinAccountConfigEntry> vConfigs = new Vector<SFinAccountConfigEntry>();

        // Define empty account:

        sSql = "SELECT fmt_id_acc FROM erp.cfg_param_erp WHERE id_erp = 1 ";
        oResultSet = poStatement.executeQuery(sSql);
        if (oResultSet.next()) {
            sEmptyAccount = oResultSet.getString("fmt_id_acc").replace('9', '0');
        }

        // Check if there is an account for business partner:

        sSql = "SELECT abb.id_dt_start, abb.id_acc_bp " +
                "FROM fin_acc_bp_bp AS abb INNER JOIN fin_acc_bp AS ab ON " +
                "abb.id_acc_bp = ab.id_acc_bp AND ab.b_del = 0 " +
                "WHERE abb.id_bp = " + pnBpId + " AND abb.id_ct_bp = " + pnBpCategoryId + " AND abb.id_bkc = " + pnBkcId + " AND " +
                "abb.id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(ptDateStart) + "' AND abb.b_del = 0 " +
                "ORDER BY abb.id_dt_start DESC ";
        oResultSet = poStatement.executeQuery(sSql);
        if (oResultSet.next()) {
            nAccountId = oResultSet.getInt("abb.id_acc_bp");
        }
        else {
            // Check if there is an account for business partner type, but first, obtain business partner type key:

            sSql = "SELECT fid_ct_bp, fid_tp_bp FROM erp.bpsu_bp_ct WHERE id_bp = " + pnBpId + " AND id_ct_bp = " + pnBpCategoryId + " ";
            oResultSet = poStatement.executeQuery(sSql);
            if (!oResultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Business partner category for BP: " + pnBpId + ")");
            }
            else {
                sSql = "SELECT atb.id_dt_start, atb.id_acc_bp " +
                        "FROM fin_acc_bp_tp_bp AS atb INNER JOIN fin_acc_bp AS ab ON " +
                        "atb.id_acc_bp = ab.id_acc_bp AND ab.b_del = 0 " +
                        "WHERE atb.id_ct_bp = " + oResultSet.getInt("fid_ct_bp") + " AND atb.id_tp_bp = " + oResultSet.getInt("fid_tp_bp") + " AND atb.id_bkc = " + pnBkcId + " AND " +
                        "atb.id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(ptDateStart) + "' AND atb.b_del = 0 " +
                        "ORDER BY atb.id_dt_start DESC ";
                oResultSet = poStatement.executeQuery(sSql);
                if (oResultSet.next()) {
                    nAccountId = oResultSet.getInt("atb.id_acc_bp");
                }
            }
        }

        if (nAccountId == 0) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Business partner accounts for BP: " + pnBpId + ")");
        }
        else {
            sSql = "SELECT abe.fid_acc, abe.fid_cc_n, abe.per " +
                    "FROM fin_acc_bp ab INNER JOIN fin_acc_bp_ety AS abe ON " +
                    "ab.id_acc_bp = abe.id_acc_bp AND ab.id_acc_bp = " + nAccountId + " AND ab.b_del = 0 AND abe.id_tp_acc_bp = " + pnBpAccountTypeId + " AND " +
                    "abe.fid_tp_bkr IN (" + SDataConstantsSys.FINS_TP_BKR_ALL + ", " + (pbIsDebit ? SDataConstantsSys.FINS_TP_BKR_DBT : SDataConstantsSys.FINS_TP_BKR_CDT) + ") " +
                    "ORDER BY abe.fid_acc, abe.fid_cc_n, abe.per ";
            oResultSet = poStatement.executeQuery(sSql);
            while (oResultSet.next()) {
                if (oResultSet.getString("abe.fid_acc").compareTo(sEmptyAccount) == 0) {
                    throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Account for business partner for BP: " + pnBpId + ")");
                }
                else {
                    vConfigs.add(new SFinAccountConfigEntry(oResultSet.getString("abe.fid_acc"), oResultSet.getString("abe.fid_cc_n"), oResultSet.getDouble("abe.per")));
                }
            }

            if (vConfigs.isEmpty()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Account for business partner for BP: " + pnBpId + ")");
            }
        }

        return vConfigs;
    }

    /**
     * Obtain account and cost center configurations for item, bookkeeping center and date supplied.
     *
     * @param piClient ERP Client interface.
     * @param pnItemId Item ID.
     * @param pnBkcId Bookkeeping center ID.
     * @param ptDateStart Starting date.
     * @param pnItemAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @param pbIsDebit Is debit movement.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainItemAccountConfigs(erp.client.SClientInterface piClient,
            int pnItemId, int pnBkcId, java.util.Date ptDateStart, int pnItemAccountTypeId, boolean pbIsDebit) throws java.lang.Exception {

        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFinAccountConfigEntry> accountConfigs = null;

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_ITEM, new Object[] { pnItemId, pnBkcId, ptDateStart, pnItemAccountTypeId, pbIsDebit });
        response = piClient.getSessionXXX().request(request);

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
     * @param pnItemId Item ID.
     * @param pnBkcId Bookkeeping center ID.
     * @param ptDateStart Starting date.
     * @param pnItemAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @param pbIsDebit Is debit movement.
     * @param poStatement Database connection statement.
     * @param poDateFormatDbms SimpleDateFormat object for DBMS specific as text date format.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainItemAccountConfigs(
            int pnItemId, int pnBkcId, java.util.Date ptDateStart, int pnItemAccountTypeId, boolean pbIsDebit, java.sql.Statement poStatement) throws java.lang.Exception {

        int nAccountId = 0;
        int nReferenceId = 0;
        int[] anLinkTypes = null;
        String sSql = "";
        String sEmptyAccount = "";
        ResultSet oResultSet = null;
        SDataItem oItem = new SDataItem();
        Vector<SFinAccountConfigEntry> vConfigs = new Vector<SFinAccountConfigEntry>();

        // Define empty account:

        sSql = "SELECT fmt_id_acc FROM erp.cfg_param_erp WHERE id_erp = 1 ";
        oResultSet = poStatement.executeQuery(sSql);
        if (oResultSet.next()) {
            sEmptyAccount = oResultSet.getString("fmt_id_acc").replace('9', '0');
        }

        // Read item registry:

        if (oItem.read(new int[] { pnItemId }, poStatement) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Item registry for item: " + pnItemId + ")");
        }

        anLinkTypes = new int[] {
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

        for (int type : anLinkTypes) {
            // Check if there is an account for link type:

            switch (type) {
                case SDataConstantsSys.TRNS_TP_LINK_ITEM:
                    nReferenceId = pnItemId;
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_MFR:
                    nReferenceId = oItem.getFkManufacturerId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_BRD:
                    nReferenceId = oItem.getFkBrandId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_LINE:
                    nReferenceId = oItem.getFkItemLineId_n();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IGEN:
                    nReferenceId = oItem.getFkItemGenericId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IGRP:
                    nReferenceId = oItem.getDbmsDataItemGeneric().getFkItemGroupId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IFAM:
                    nReferenceId = oItem.getDbmsDataItemGeneric().getDbmsDataItemGroup().getFkItemFamilyId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                    nReferenceId = oItem.getDbmsDataItemGeneric().getFkItemTypeId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
                    nReferenceId = oItem.getDbmsDataItemGeneric().getFkItemClassId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
                    nReferenceId = oItem.getDbmsDataItemGeneric().getFkItemCategoryId();
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_ALL:
                    nReferenceId = SLibConstants.UNDEFINED;
                    break;
                default:
            }

            sSql = "SELECT aii.id_dt_start, aii.id_acc_item " +
                    "FROM fin_acc_item_item AS aii INNER JOIN fin_acc_item AS ai ON " +
                    "aii.id_acc_item = ai.id_acc_item AND ai.b_del = 0 " +
                    "WHERE aii.id_tp_link = " + type + " AND aii.id_ref = " + nReferenceId + " AND aii.id_bkc = " + pnBkcId + " AND " +
                    "aii.id_dt_start <= '" + SLibUtils.DbmsDateFormatDate.format(ptDateStart) + "' AND aii.b_del = 0 " +
                    "ORDER BY aii.id_dt_start DESC, aii.id_acc_item ";
            oResultSet = poStatement.executeQuery(sSql);
            if (oResultSet.next()) {
                nAccountId = oResultSet.getInt("aii.id_acc_item");
                break;
            }
        }

        if (nAccountId == 0) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Item accounts for item: " + pnItemId + ")");
        }
        else {
            sSql = "SELECT aie.per, aie.fid_acc_n, aie.fid_cc_n " +
                    "FROM fin_acc_item ai INNER JOIN fin_acc_item_ety AS aie ON " +
                    "ai.id_acc_item = aie.id_acc_item AND ai.id_acc_item = " + nAccountId + " AND ai.b_del = 0 AND aie.id_tp_acc_item = " + pnItemAccountTypeId + " AND " +
                    "aie.fid_tp_bkr IN (" + SDataConstantsSys.FINS_TP_BKR_ALL + ", " + (pbIsDebit ? SDataConstantsSys.FINS_TP_BKR_DBT : SDataConstantsSys.FINS_TP_BKR_CDT) + ") " +
                    "ORDER BY aie.fid_acc_n, aie.fid_cc_n, aie.per ";
            oResultSet = poStatement.executeQuery(sSql);
            while (oResultSet.next()) {
                if (oResultSet.getString("aie.fid_acc_n") == null || oResultSet.getString("aie.fid_acc_n").compareTo(sEmptyAccount) == 0) {
                    throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Account for item: " + pnItemId + ")");
                }
                else {
                    vConfigs.add(new SFinAccountConfigEntry(oResultSet.getString("aie.fid_acc_n"), oResultSet.getString("aie.fid_cc_n"), oResultSet.getDouble("aie.per")));
                }
            }

            if (vConfigs.isEmpty()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Account for item: " + pnItemId + ")");
            }
        }

        return vConfigs;
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
