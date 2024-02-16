/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
 */
package erp.mfin.data;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mitm.data.SDataItem;
import erp.mod.fin.db.SFinUtils;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestCostCenter;
import erp.mod.trn.db.SDbMaterialRequestEntry;
import erp.mtrn.data.SDataDpsMaterialRequest;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class SFinAccountUtilities {
    
    /**
     * Gets the current account format from ERP configuration.
     * @param statement Database statement.
     * @return Current account format (e.g., "0000-0000-0000").
     * @throws SQLException
     * @throws Exception 
     */
    public static String getConfigAccountFormat(final Statement statement) throws SQLException, Exception {
        String format = "";
        ResultSet resultSet = statement.executeQuery("SELECT fmt_id_acc FROM erp.cfg_param_erp WHERE id_erp = 1; ");
        
        if (resultSet.next()) {
            format = resultSet.getString(1).replace('9', '0');
        }
        
        return format;
    }
    
    /**
     * Obtains the ledger account of provided account.
     * @param account Provided account from which its ledger account will be obtained.
     * @param accountFormat Account format from ERP configuration.
     * @return Ledger account.
     */
    public static String obtainAccountLedger(final String account, final String accountFormat) {
        int ledgerPos = account.indexOf("-");
        
        return ledgerPos == -1 ? "" : account.substring(0, ledgerPos) + accountFormat.substring(ledgerPos);
    }
    
    /**
     * Gets the system-account type of provided account.
     * @param connection Database connection.
     * @param account Provided account from which its system-account type will be get.
     * @return System-account type.
     * @throws SQLException
     * @throws Exception 
     */
    public static int getSystemAccountType(final Connection connection, final String account) throws SQLException, Exception {
        int type = SLibConsts.UNDEFINED;
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT fid_tp_acc_sys FROM fin_acc WHERE id_acc = '" + account + "';");
        
        if (resultSet.next()) {
            type = resultSet.getInt(1);
        }
        
        return type;
    }

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
     * @throws java.lang.Exception
     */
    public static java.lang.String obtainTaxAccountId(
            int[] keyTax, int idDpsCategory, java.util.Date dateStart, int idTaxAccountType, java.sql.Statement statement) throws java.lang.Exception {
        String sql = "";
        String field = "";
        String idAccount = "";
        String emptyAccount = getConfigAccountFormat(statement);   // current account format is an empty account
        ResultSet resultSet = null;

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
            if (resultSet.getString("f_acc").compareTo(emptyAccount) == 0) {
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
     * @param taxPk
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    @SuppressWarnings("unchecked")
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainBizPartnerAccountConfigs(erp.client.SClientInterface client,
            int idBizPartner, int idBizPartnenrCategory, int idBkc, java.util.Date dateStart, int idBizPartnerAccountType, boolean isDebit, int[] taxPk) throws java.lang.Exception {
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<SFinAccountConfigEntry> accountConfigs = null;

        request = new SServerRequest(SServerConstants.REQ_OBJ_FIN_ACC_BP, new Object[] { idBizPartner, idBizPartnenrCategory, idBkc, dateStart, idBizPartnerAccountType, isDebit, taxPk });
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
     * @param pkTax pk of tax configured, can be null
     * @param statement Database connection statement.
     * @return Account and cost center configurations, if any, for parameters supplied.
     */
    public static java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> obtainBizPartnerAccountConfigs(
            int idBizPartner, int idBizPartnerCategory, int idBkc, java.util.Date dateStart, int idBizPartnerAccountType, 
            boolean isDebit, int[] pkTax, java.sql.Statement statement) throws java.lang.Exception {
        int idAccount = 0;
        String sql = "";
        String emptyAccount = getConfigAccountFormat(statement);   // current account format is an empty account
        ResultSet resultSet = null;
        Vector<SFinAccountConfigEntry> accountConfigs = new Vector<SFinAccountConfigEntry>();

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
                    "abe.fid_tp_bkr IN (" + SDataConstantsSys.FINS_TP_BKR_ALL + ", " + (isDebit ? SDataConstantsSys.FINS_TP_BKR_DBT : SDataConstantsSys.FINS_TP_BKR_CDT) + ") WHERE " +
                    (pkTax != null && pkTax[0] > 0 ? ("abe.fid_tax_bas_n = " + pkTax[0] + " AND abe.fid_tax_n = " + pkTax[1] + " " ) : ("abe.fid_tax_bas_n IS NULL AND abe.fid_tax_n IS NULL " )) +
                    "ORDER BY abe.fid_acc, abe.fid_cc_n, abe.per ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                if (resultSet.getString("abe.fid_acc").compareTo(emptyAccount) == 0) {
                    throw new Exception(SLibConstants.MSG_ERR_GUI_EMPTY_ACC + " (Account for business partner for BP: " + idBizPartner + ")");
                }
                else {
                    accountConfigs.add(new SFinAccountConfigEntry(resultSet.getString("abe.fid_acc"), resultSet.getString("abe.fid_cc_n"), resultSet.getDouble("abe.per")));
                }
            }

            if (accountConfigs.isEmpty() && pkTax == null) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (Account for business partner for BP: " + idBizPartner + ")");
            }
            
            if (accountConfigs.isEmpty() && pkTax != null) {
                return null;
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
        String emptyAccount = getConfigAccountFormat(statement);   // current account format is an empty account
        ResultSet resultSet = null;
        SDataItem item = new SDataItem();
        Vector<SFinAccountConfigEntry> accountConfigs = new Vector<SFinAccountConfigEntry>();

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
                    idReference = item.getDbmsDataItemGeneric().getDbmsFkItemFamilyId();
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
                if (resultSet.getString("aie.fid_acc_n") == null || resultSet.getString("aie.fid_acc_n").compareTo(emptyAccount) == 0) {
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
    
    public static ArrayList<SFinAccountConfigEntry> getMaterialRequestEntryAccountConfigs(Connection connection, SDataDpsMaterialRequest oMatRequestDpsEtyLink) throws Exception {
        if (oMatRequestDpsEtyLink == null) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + " (No se encontró vínculo con la Requisición de materiales)");
        }
        
        // Lee la partida de la requisición de materiales y obtiene centro de costo y cuenta contable
        ArrayList<SFinAccountConfigEntry> lConfigs = getMaterialRequestEntryAccountConfigsByMatReqEty(connection, oMatRequestDpsEtyLink.getFkMaterialRequestId(), oMatRequestDpsEtyLink.getFkMaterialRequestEntryId());
        if (lConfigs.size() > 0) {
            return lConfigs;
        }

        // Lee el encabezado de la requisición de materiales y obtiene centro de costo y cuenta contable
        return getMaterialRequestEntryAccountConfigsByMatReq(connection, oMatRequestDpsEtyLink.getFkMaterialRequestId());
    }
    
    public static ArrayList<SFinAccountConfigEntry> getMaterialRequestEntryAccountConfigsByMatReqEty(Connection connection, int idMatReq, int idMatReqEty) throws Exception {
        // Proceso para obtener la información de la partida de la requisición de materiales
        String sql = "SELECT " +
                "re.fk_cc_n, cc.id_cc, cc.cc, se.code, se.name, se.fk_acc_fa, acc.id_acc, '1' AS per " +
            "FROM " +
                "trn_mat_req_ety re " +
                    "LEFT JOIN " +
                "fin_cc cc ON re.fk_cc_n = cc.pk_cc " +
                    "LEFT JOIN " +
                "trn_mat_cons_subent se ON re.fk_subent_mat_cons_ent_n = se.id_mat_cons_ent " +
                    "AND re.fk_subent_mat_cons_subent_n = se.id_mat_cons_subent " +
                    "LEFT JOIN " +
                "fin_acc acc ON se.fk_acc_fa = acc.pk_acc " +
            "WHERE re.id_mat_req = " + idMatReq + " " +
                "AND re.id_ety = " + idMatReqEty + ";";

        return executeQueryAndGetConfigs(connection, sql);
    }

    public static ArrayList<SFinAccountConfigEntry> getMaterialRequestEntryAccountConfigsByMatReq(Connection connection, int idMatReq) throws Exception {
        // Proceso para obtener la información del encabezado de la requisición de materiales
        String sql = "SELECT " +
                "rcc.id_cc AS pk_cc, cc.id_cc, cc.cc, se.fk_acc_fa, rcc.per, acc.id_acc " +
            "FROM " +
                "trn_mat_req r " +
                    "LEFT JOIN " +
                "trn_mat_req_cc rcc ON r.id_mat_req = rcc.id_mat_req " +
                    "LEFT JOIN " +
                "trn_mat_cons_subent se ON rcc.id_mat_subent_cons_ent = se.id_mat_cons_ent " +
                    "AND rcc.id_mat_subent_cons_subent = se.id_mat_cons_subent " +
                    "LEFT JOIN " +
                "fin_acc acc ON se.fk_acc_fa = acc.pk_acc " +
                    "LEFT JOIN " +
                "fin_cc cc ON rcc.id_cc = cc.pk_cc " +
            "WHERE r.id_mat_req = " + idMatReq + ";";

        return executeQueryAndGetConfigs(connection, sql);
    }

    private static ArrayList<SFinAccountConfigEntry> executeQueryAndGetConfigs(Connection connection, String sql) throws SQLException {
        ArrayList<SFinAccountConfigEntry> lConfigs = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            if (resultSet.getString("id_acc") != null && resultSet.getString("id_acc").length() > 0) {
                SFinAccountConfigEntry oConfigEntry = new SFinAccountConfigEntry(resultSet.getString("id_acc"), resultSet.getString("id_cc"), resultSet.getDouble("per"));
                lConfigs.add(oConfigEntry);
            }
        }
        return lConfigs;
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
