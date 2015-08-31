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
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SFinanceUtilities {

    /**
     * Obtain account for tax, DPS category and date supplied.
     *
     * @param piClient ERP Client interface.
     * @param taxKey Tax key (basic tax ID, tax ID).
     * @param dpsCategoryId DPS category ID.
     * @param dateStart Starting date.
     * @param accountType Account type. Constants defined in SDataConstantsSys class.
     * @return Account ID, if any, for parameters supplied.
     */
    public static java.lang.String obtainTaxAccountId(erp.client.SClientInterface piClient, int[] panTaxKey, int pnDpsCategoryId, java.util.Date ptDateStart, int pnTaxAccountTypeId) {
        SServerRequest request = null;
        SServerResponse response = null;
        String sAccountId = "";

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_TAX_ID, new Object[] { panTaxKey, pnDpsCategoryId, ptDateStart, pnTaxAccountTypeId, piClient.getSessionXXX().getFormatters().getDbmsDateFormat() });
        response = piClient.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            SLibUtilities.renderException(SFinanceUtilities.class.getName(), new Exception(response.getMessage()));
        }
        else {
            sAccountId = (String) response.getPacket();
        }

        return sAccountId;
    }

    /**
     * Obtain account for tax, DPS category and date supplied.
     *
     * @param taxKey Tax key (basic tax ID, tax ID).
     * @param dpsCategoryId DPS category ID.
     * @param dateStart Starting date.
     * @param accountType Account type. Constants defined in SDataConstantsSys class.
     * @param statement Database connection statement.
     * @param dateFormat SimpleDateFormat object for DBMS specific as text date format.
     * @return Account ID, if any, for parameters supplied.
     */
    public static java.lang.String obtainTaxAccountId(int[] panTaxKey, int pnDpsCategoryId, java.util.Date ptDateStart, int pnTaxAccountTypeId,
            java.sql.Statement poStatement, java.text.SimpleDateFormat poDateFormatDbms) throws java.lang.Exception {

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
                "id_dt_start <= '" + poDateFormatDbms.format(ptDateStart) + "' AND b_del = FALSE " +
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
     * @param pnAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mfin.data.SFinanceAccountConfig> obtainBizPartnerAccountConfigs(erp.client.SClientInterface piClient,
            int pnBpId, int pnBpCategoryId, int pnBkcId, java.util.Date ptDateStart, int pnBpAccountTypeId, boolean pbIsDebit) throws java.lang.Exception {

        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFinanceAccountConfig> accountConfigs = null;

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_BP, new Object[] { pnBpId, pnBpCategoryId, pnBkcId, ptDateStart, pnBpAccountTypeId, pbIsDebit, piClient.getSessionXXX().getFormatters().getDbmsDateFormat() });
        response = piClient.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            SLibUtilities.renderException(SFinanceUtilities.class.getName(), new Exception(response.getMessage()));
        }
        else {
            accountConfigs = (Vector<SFinanceAccountConfig>) response.getPacket();
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
     * @param pnAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @param poStatement Database connection statement.
     * @param poDateFormat SimpleDateFormat object for DBMS specific as text date format.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    public static java.util.Vector<erp.mfin.data.SFinanceAccountConfig> obtainBizPartnerAccountConfigs(
            int pnBpId, int pnBpCategoryId, int pnBkcId, java.util.Date ptDateStart, int pnBpAccountTypeId, boolean pbIsDebit,
            java.sql.Statement poStatement, java.text.SimpleDateFormat poDateFormatDbms) throws java.lang.Exception {

        int nAccountId = 0;
        String sSql = "";
        String sEmptyAccount = "";
        ResultSet oResultSet = null;
        Vector<SFinanceAccountConfig> vConfigs = new Vector<SFinanceAccountConfig>();

        // Define empty account:

        sSql = "SELECT fmt_id_acc FROM erp.cfg_param_erp WHERE id_erp = 1 ";
        oResultSet = poStatement.executeQuery(sSql);
        if (oResultSet.next()) {
            sEmptyAccount = oResultSet.getString("fmt_id_acc").replace('9', '0');
        }

        // Check if there is an account for business partner:

        sSql = "SELECT abb.id_dt_start, abb.id_acc_bp " +
                "FROM fin_acc_bp_bp AS abb INNER JOIN fin_acc_bp AS ab ON " +
                "abb.id_acc_bp = ab.id_acc_bp AND ab.b_del = FALSE " +
                "WHERE abb.id_bp = " + pnBpId + " AND abb.id_ct_bp = " + pnBpCategoryId + " AND abb.id_bkc = " + pnBkcId + " AND " +
                "abb.id_dt_start <= '" + poDateFormatDbms.format(ptDateStart) + "' AND abb.b_del = FALSE " +
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
                        "atb.id_acc_bp = ab.id_acc_bp AND ab.b_del = FALSE " +
                        "WHERE atb.id_ct_bp = " + oResultSet.getInt("fid_ct_bp") + " AND atb.id_tp_bp = " + oResultSet.getInt("fid_tp_bp") + " AND atb.id_bkc = " + pnBkcId + " AND " +
                        "atb.id_dt_start <= '" + poDateFormatDbms.format(ptDateStart) + "' AND atb.b_del = FALSE " +
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
            sSql = "SELECT abe.per, abe.fid_acc, abe.fid_cc_n " +
                    "FROM fin_acc_bp ab INNER JOIN fin_acc_bp_ety AS abe ON " +
                    "ab.id_acc_bp = abe.id_acc_bp AND ab.id_acc_bp = " + nAccountId + " AND ab.b_del = FALSE AND abe.id_tp_acc_bp = " + pnBpAccountTypeId + " AND " +
                    "abe.fid_tp_bkr IN (" + SDataConstantsSys.FINS_TP_BKR_ALL + ", " + (pbIsDebit ? SDataConstantsSys.FINS_TP_BKR_DBT : SDataConstantsSys.FINS_TP_BKR_CDT) + ") " +
                    "ORDER BY abe.fid_acc, abe.fid_cc_n, abe.per ";
            oResultSet = poStatement.executeQuery(sSql);
            while (oResultSet.next()) {
                SFinanceAccountConfig config = null;

                if (oResultSet.getString("abe.fid_acc").compareTo(sEmptyAccount) == 0) {
                    throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Account for business partner for BP: " + pnBpId + ")");
                }

                config = new SFinanceAccountConfig();
                config.setPercentage(oResultSet.getDouble("abe.per"));
                config.setAccountId(oResultSet.getString("abe.fid_acc"));       // this function validates non-null asignations
                config.setCostCenterId(oResultSet.getString("abe.fid_cc_n"));   // this function validates non-null asignations

                vConfigs.add(config);
            }

            if (vConfigs.size() == 0) {
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
     * @param pnAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mfin.data.SFinanceAccountConfig> obtainItemAccountConfigs(erp.client.SClientInterface piClient,
            int pnItemId, int pnBkcId, java.util.Date ptDateStart, int pnItemAccountTypeId, boolean pbIsDebit) throws java.lang.Exception {

        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFinanceAccountConfig> accountConfigs = null;

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_ITEM, new Object[] { pnItemId, pnBkcId, ptDateStart, pnItemAccountTypeId, pbIsDebit, piClient.getSessionXXX().getFormatters().getDbmsDateFormat() });
        response = piClient.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            SLibUtilities.renderException(SFinanceUtilities.class.getName(), new Exception(response.getMessage()));
        }
        else {
            accountConfigs = (Vector<SFinanceAccountConfig>) response.getPacket();
        }

        return accountConfigs;
    }

    /**
     * Obtain account and cost center configurations for item, bookkeeping center and date supplied.
     *
     * @param pnItemId Item ID.
     * @param pnBkcId Bookkeeping center ID.
     * @param ptDateStart Starting date.
     * @param pnAccountTypeId Account type. Constants defined in SDataConstantsSys class.
     * @param poStatement Database connection statement.
     * @param poDateFormat SimpleDateFormat object for DBMS specific as text date format.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    public static java.util.Vector<erp.mfin.data.SFinanceAccountConfig> obtainItemAccountConfigs(
            int pnItemId, int pnBkcId, java.util.Date ptDateStart, int pnItemAccountTypeId, boolean pbIsDebit,
            java.sql.Statement poStatement, java.text.SimpleDateFormat poDateFormatDbms) throws java.lang.Exception {

        int nAccountId = 0;
        int nReferenceId = 0;
        int[] anLinkTypes = null;
        String sSql = "";
        String sEmptyAccount = "";
        ResultSet oResultSet = null;
        SDataItem oItem = new SDataItem();
        Vector<SFinanceAccountConfig> vConfigs = new Vector<SFinanceAccountConfig>();

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
                    "aii.id_acc_item = ai.id_acc_item AND ai.b_del = FALSE " +
                    "WHERE aii.id_tp_link = " + type + " AND aii.id_ref = " + nReferenceId + " AND aii.id_bkc = " + pnBkcId + " AND " +
                    "aii.id_dt_start <= '" + poDateFormatDbms.format(ptDateStart) + "' AND aii.b_del = FALSE " +
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
                    "ai.id_acc_item = aie.id_acc_item AND ai.id_acc_item = " + nAccountId + " AND ai.b_del = FALSE AND aie.id_tp_acc_item = " + pnItemAccountTypeId + " AND " +
                    "aie.fid_tp_bkr IN (" + SDataConstantsSys.FINS_TP_BKR_ALL + ", " + (pbIsDebit ? SDataConstantsSys.FINS_TP_BKR_DBT : SDataConstantsSys.FINS_TP_BKR_CDT) + ") " +
                    "ORDER BY aie.fid_acc_n, aie.fid_cc_n, aie.per ";
            oResultSet = poStatement.executeQuery(sSql);
            while (oResultSet.next()) {
                SFinanceAccountConfig config = null;

                if (oResultSet.getString("aie.fid_acc_n") == null || oResultSet.getString("aie.fid_acc_n").compareTo(sEmptyAccount) == 0) {
                    throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Account for item: " + pnItemId + ")");
                }

                config = new SFinanceAccountConfig();
                config.setPercentage(oResultSet.getDouble("aie.per"));
                config.setAccountId(oResultSet.getString("aie.fid_acc_n"));     // this function validates non-null asignations
                config.setCostCenterId(oResultSet.getString("aie.fid_cc_n"));   // this function validates non-null asignations

                vConfigs.add(config);
            }

            if (vConfigs.size() == 0) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Account for item: " + pnItemId + ")");
            }
        }

        return vConfigs;
    }

    public static double[] createPercentages(java.util.Vector<erp.mfin.data.SFinanceAccountConfig> configs) {
        double[] percentages = new double[configs.size()];

        for (int i = 0; i < configs.size(); i++) {
            percentages[i] = configs.get(i).getPercentage();
        }

        return percentages;
    }

    public static double[] applyPercentages(double value, double[] percentages, int decimals) {
        int i = 0;
        int maxIndex = -1;
        double max = Double.MIN_VALUE;
        double total = 0;
        double originalValue = SLibUtilities.round(value, decimals);
        double[] values = new double[percentages.length];

        // Apply percentages to original value:

        for (i = 0; i < percentages.length; i++) {
            values[i] = SLibUtilities.round(value * percentages[i], decimals);
            total += values[i];
        }

        // Check if total equals original value:

        if (originalValue != total) {
            for (i = 0; i < values.length; i++) {
                if (values[i] > max) {
                    max = values[i];
                    maxIndex = i;
                }
            }

            if (maxIndex != -1) {
                values[i] += originalValue - total;
            }
        }

        return values;
    }
}
