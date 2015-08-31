/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mmfg.data;

import erp.data.*;
import erp.lib.SLibTimeUtilities;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.util.Vector;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Néstor Ávalos
 */
public abstract class SProcUtilities {

    /**
     * Obtains item price based in price list by customer or customer type.
     *
     * @param client SClientInterface.
     * @param pnBizPartnerId Biz ID.
     * @param pnItemId Item ID.
     * @param ptDateDoc Document date.
     * @return double array with item price and discount percentage.
     */
    @SuppressWarnings("unchecked")
    public static double[] obtainItemPrice(erp.client.SClientInterface client, int pnBizPartnerId, int pnItemId, java.util.Date ptDateDoc) throws java.lang.Exception {

        double dItemPrice = 0;
        double dPercentage = 0;
        String sql = "";
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<Vector<Object>> rows = null;

        sql = "SELECT IF(c.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_PRICE_U + ", i.price - (i.price*c.per), i.price) AS f_price, " +
            "IF(c.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_DISC_U + ", (i.price*c.per), 0) AS f_per, 1 AS f_type, c.id_dt_start AS f_date " +
            "FROM mkt_plist_cus AS c " +
            "INNER JOIN mkt_plist AS p ON c.fid_plist = p.id_plist AND p.b_del = FALSE AND " +
            "'" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(ptDateDoc) + "' >= c.id_dt_start AND c.id_cus = " + pnBizPartnerId + " AND c.b_del = FALSE " +
            "INNER JOIN mkt_plist_price AS i ON p.id_plist = i.id_plist AND i.id_item = " + pnItemId + " " +
            "UNION " +
            "SELECT IF(cf.b_free_disc_doc = 1, i.price, IF(c.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_PRICE_U + ", i.price - (i.price*c.per), i.price)) AS f_price, " +
            "IF(cf.b_free_disc_doc = 1, 0, IF(c.fid_tp_disc_app = " + SDataConstantsSys.MKTS_TP_DISC_APP_DISC_U + ", (i.price*c.per), 0)) AS f_per, 2 AS f_type, c.id_dt_start AS f_date " +
            "FROM mkt_cfg_cus AS cf " +
            "INNER JOIN mkt_plist_cus_tp AS c ON cf.fid_tp_cus = c.id_tp_cus AND cf.id_cus = " + pnBizPartnerId + " AND cf.b_del = FALSE AND " +
            "'" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(ptDateDoc) + "' >= c.id_dt_start AND c.b_del = FALSE " +
            "INNER JOIN mkt_plist AS p ON c.fid_plist = p.id_plist AND p.b_del = FALSE " +
            "INNER JOIN mkt_plist_price AS i ON p.id_plist = i.id_plist AND i.id_item = " + pnItemId + " " +
            "ORDER BY f_type, f_date DESC";

        request = new SServerRequest(SServerConstants.REQ_DB_QUERY_SIMPLE, sql);
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
        else {
            rows = (Vector<Vector<Object>>) response.getPacket();
            for (Vector<Object> row : rows) {
                dItemPrice = row.get(0) == null ? 0d : ((Number) row.get(0)).doubleValue();
                dPercentage = row.get(1) == null ? 0d : ((Number) row.get(1)).doubleValue();
                break;
            }
        }

        return new double[] { dItemPrice, dPercentage };
    }

    /**
     * Obtains backorder for a item
     *
     * @param client SClientInterface.
     * @param pnCtDpsId Category Dps ID.
     * @param pnCobId Company branch ID.
     * @param pnItemId Item ID.
     * @param ptDateCut Cut date.
     * @return backorder quantity.
     */
    @SuppressWarnings("unchecked")
    public static double obtainBackorderItem(erp.client.SClientInterface client, int pnCtDpsId, int pnCobId, int pnItemId, java.util.Date ptDateCut) throws java.lang.Exception {

        double dBackorder = 0;
        String sql = "";
        String sqlCompanyBranch = (pnCobId > 0 ? "AND d.fid_cob = " + pnCobId : "");
        String sqlCompanyBranchSubquery = (pnCobId > 0 ? "AND t.fid_cob = " + pnCobId : "");
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<Vector<Object>> rows = null;

        sql = "SELECT SUM(de.qty - " +
                "(SELECT COALESCE(SUM(s.qty), 0) " +
                "FROM trn_dps_dps_supply AS s " +
                "INNER JOIN trn_dps AS t ON s.id_des_year = t.id_year AND s.id_des_doc = t.id_doc " +
                "INNER JOIN trn_dps_ety AS te ON s.id_des_year = te.id_year AND s.id_des_doc = te.id_doc AND s.id_des_ety = te.id_ety " +
                "WHERE s.id_src_year = de.id_year AND s.id_src_doc = de.id_doc AND s.id_src_ety = de.id_ety AND " +
                "t.b_del = FALSE AND te.b_del = FALSE AND d.dt <= '" + ptDateCut + "' " + sqlCompanyBranchSubquery + ")) AS f_qty_pend " +
            "FROM trn_dps AS d " +
            "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
            "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
            "d.fid_ct_dps = " + (pnCtDpsId == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0]) +
            " AND d.fid_cl_dps = " + (pnCtDpsId == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1]) +
            " AND d.fid_tp_dps = " + (pnCtDpsId == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2]) + " " +
            "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
            "WHERE d.b_del = FALSE AND de.b_del = FALSE AND de.fid_item = " + pnItemId + " AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " +
            SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " AND d.b_link = FALSE AND d.dt <= '" + ptDateCut + "' AND YEAR(d.dt) = " +
            SLibTimeUtilities.digestYear(ptDateCut)[0] + " " + sqlCompanyBranch + " ";
            // "HAVING f_qty_pend > 0 " +

        request = new SServerRequest(SServerConstants.REQ_DB_QUERY_SIMPLE, sql);
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
        else {
            rows = (Vector<Vector<Object>>) response.getPacket();
            for (Vector<Object> row : rows) {
                dBackorder = row.get(0) == null ? 0d : ((Number) row.get(0)).doubleValue();
                break;
            }
        }

        return dBackorder;
    }


}
