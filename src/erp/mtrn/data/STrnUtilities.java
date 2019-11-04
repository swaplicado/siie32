/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.SClient;
import erp.cfd.SCfdConsts;
import erp.cfd.SDialogCfdSend;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormComponentItem;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataEmployee;
import erp.mcfg.data.SDataCompanyBranchEntity;
import erp.mhrs.data.SDataFormerPayroll;
import erp.mhrs.data.SDataFormerPayrollEmp;
import erp.mitm.data.SDataItem;
import erp.mmfg.data.SDataProductionOrder;
import erp.mmfg.data.SDataProductionOrderCharge;
import erp.mmfg.data.SDataProductionOrderChargeEntry;
import erp.mmfg.data.SDataProductionOrderChargeEntryLot;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.cfg.db.SDbMms;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SHrsFormerConsts;
import erp.musr.data.SDataUser;
import erp.print.SDataConstantsPrint;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.mail.MessagingException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.mail.SMail;
import sa.lib.mail.SMailConsts;
import sa.lib.mail.SMailSender;
import sa.lib.srv.SSrvConsts;
import sa.lib.srv.SSrvLock;
import sa.lib.srv.SSrvUtils;

/**
 *
 * @author Sergio Flores, Daniel López, Claudio Peña, Sergio Flores
 */
public abstract class STrnUtilities {

    /**
     * @return Production order status set as comma separated values where stock moves are allowed.
     */
    public static String getProdOrderActiveStatus() {
        return "" + SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG + ", " + SDataConstantsSys.MFGS_ST_ORD_PROC + ", " + SDataConstantsSys.MFGS_ST_ORD_END;
    }

    /**
     * Validates if a new or updated lot is not yet existing. Used in lots catalogue maintenance.
     * @param client ERP Client interface.
     * @param lotKey Lot primary key.
     * @param lot Lot to validate.
     * @returns Validation error message, otherwise empty string:
     */
    @SuppressWarnings("unchecked")
    public static String validateLot(final SClientInterface client, final int[] lotKey, final String lot) throws Exception {
        int count = 0;
        String sql = "";
        ResultSet resulSet = null;
        String msg = "";

        sql = "SELECT COUNT(*) AS f_count FROM trn_lot " +
                "WHERE id_item = " + lotKey[0] + " AND id_unit = " + lotKey[1] + " AND id_lot <> " + lotKey[2] + " AND " +
                "lot = '" + lot + "' ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            count = resulSet.getInt("f_count");

            if (count > 0) {
                msg = "El lote '" + lot + "' ya existe.";
            }
        }

        return msg;
    }

    /**
     * Reads lot registry.
     * @param client ERP Client interface.
     * @param itemId Lot item ID (primary key).
     * @param unitId Lot unit ID (primary key).
     * @param lot Lot to read.
     * @returns Validation error message, otherwise empty string:
     */
    @SuppressWarnings("unchecked")
    public static SDataStockLot readLot(final SClientInterface client, final int itemId, final int unitId, final String lot) throws Exception {
        String sql = "";
        ResultSet resulSet = null;
        SDataStockLot stockLot = null;

        sql = "SELECT id_lot FROM trn_lot " +
                "WHERE b_del = 0 AND id_item = " + itemId + " AND id_unit = " + unitId + " AND " +
                "lot = '" + lot + "' ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            stockLot = (SDataStockLot) SDataUtilities.readRegistry(client, SDataConstants.TRN_LOT, new int[] { itemId, unitId, resulSet.getInt("id_lot") }, SLibConstants.EXEC_MODE_STEALTH);
        }

        return stockLot;
    }

    /**
     * Obtains available stock on a certain year up to cut off date.
     * @param client ERP Client interface.
     * @param itemId Lot item ID (primary key).
     * @param unitId Lot unit ID (primary key).
     * @param lotId_n Lot ID (primary key), can be 0 meaning undefined.
     * @param companyBranchId_n Company branch ID (primary key), can be 0 meaning undefined.
     * @param warehouseId_n Warehouse ID (primary key), can be 0 meaning undefined.
     * @param dateCutOff_n Cut off date.
     * @param iogKey_n Primary key of IOG being edited.
     * @returns Available stock for provided params.
     */
    @SuppressWarnings("unchecked")
    public static double obtainStock(final SClientInterface client, final int year, final int itemId, final int unitId, final int lotId_n, final int companyBranchId_n, final int warehouseId_n, final int maintUserId_n, final Date dateCutOff_n, final int[] iogKey_n) throws Exception {
        double stock = 0;
        String sql = "";
        ResultSet resulSet = null;

        sql = "SELECT trn_stk_get(" + year + ", " + itemId + ", " + unitId + ", " +
                (lotId_n == SLibConstants.UNDEFINED ? "NULL" : "" + lotId_n) + ", " +
                (companyBranchId_n == SLibConstants.UNDEFINED ? "NULL" : "" + companyBranchId_n) + ", " +
                (warehouseId_n == SLibConstants.UNDEFINED ? "NULL" : "" + warehouseId_n) + ", " +
                (maintUserId_n == SLibConstants.UNDEFINED ? "NULL" : "" + maintUserId_n) + ", " +
                (dateCutOff_n == null ? "NULL" : "'" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(dateCutOff_n) + "'") + ", " +
                (iogKey_n == null || (iogKey_n[0] == SLibConstants.UNDEFINED || iogKey_n[1] == SLibConstants.UNDEFINED) ? "NULL, NULL" : "" + iogKey_n[0] + ", " + iogKey_n[1]) + ") ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            stock = resulSet.getDouble(1);
        }

        return stock;
    }

    /**
     * Obtains available stock in warehouse on a certain year up to cut off date.
     * @param client ERP Client interface.
     * @param year Stock year.
     * @param dateCutOff_n Cut off date.
     * @param warehouseKey Warehouse ID (primary key).
     * @return Available stock for provided params.
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static Vector<STrnStockMove> obtainStockWarehouse(final SClientInterface client, final int year, final Date dateCutOff_n, final int[] warehouseKey) throws Exception {
        String sql = "";
        ResultSet resulSet = null;
        STrnStockMove stockMove = null;
        Vector<STrnStockMove> stockMoves = new Vector<STrnStockMove>();

        sql = "SELECT s.id_year, s.id_item, s.id_unit, s.id_lot, s.id_wh, i.item_key, i.item, u.symbol, l.lot, l.dt_exp_n, " +
                "l.b_block, SUM(s.mov_in - s.mov_out) AS f_stk, " +
                "SUM(s.debit - s.credit) AS f_bal " +
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot " +
                "WHERE s.b_del = 0 AND s.id_year = " + year + " AND s.dt <= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(dateCutOff_n) + "' AND s.id_cob = " + warehouseKey[0] + " AND s.id_wh = " + warehouseKey[1] + " " +
                "GROUP BY s.id_year, s.id_item, s.id_unit, s.id_lot, s.id_wh, l.lot, l.dt_exp_n, l.b_block, i.item_key, i.item, u.symbol " +
                "HAVING f_stk <> 0 OR f_bal <> 0 " +
                "ORDER BY " + (client.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") +
                "s.id_item, u.symbol, s.id_unit, l.lot, l.dt_exp_n, l.b_block, s.id_lot ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        while (resulSet.next()) {
            stockMove = new STrnStockMove(new int[] { year, resulSet.getInt("s.id_item"), resulSet.getInt("s.id_unit"), resulSet.getInt("s.id_lot"),
                warehouseKey[0], warehouseKey[1] }, resulSet.getDouble("f_stk"));

            stockMove.setAuxLot(resulSet.getString("l.lot"));
            stockMove.setAuxLotDateExpiration(resulSet.getDate("l.dt_exp_n"));
            stockMove.setAuxIsLotBlocked(resulSet.getBoolean("l.b_block"));
            stockMove.setValue(resulSet.getDouble("f_bal"));

            stockMoves.add(stockMove);
        }

        return stockMoves;
    }

    /**
     * Obtains all available lots as stock moves for a given item and unit.
     *
     * @param client GUI client.
     * @param year Stock year.
     * @param warehouseKey Company branch warehouse primary key.
     * @param itemId_n Stock item ID.
     * @param unitId_n Stock unit ID.
     * @return Vector of STrnStockMove objects.
     * @throws java.lang.Exception
     */
    public static Vector<STrnStockMove> obtainAvailableLots(final SClientInterface client, final int year, final int[] warehouseKey, final boolean onlyUnblocked, final int itemId_n, final int unitId_n) throws Exception {
        return obtainAvailableLots(client, year, warehouseKey, onlyUnblocked, itemId_n, unitId_n, null);
    }

    /**
     * Obtains all available lots as stock moves for a given item and unit, excluding IOG being edited.
     *
     * @param client GUI client.
     * @param year Stock year.
     * @param warehouseKey Company branch warehouse primary key.
     * @param itemId_n Stock item ID.
     * @param unitId_n Stock unit ID.
     * @param iogKey_n Primary key of IOG being edited.
     * @return Vector of STrnStockMove objects.
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static Vector<STrnStockMove> obtainAvailableLots(final SClientInterface client, final int year, final int[] warehouseKey, final boolean onlyUnblocked, final int itemId_n, final int unitId_n, final int[] iogKey_n) throws Exception {
        String sql = "";
        ResultSet resulSet = null;
        STrnStockMove stockMove = null;
        Vector<STrnStockMove> stockMoves = new Vector<STrnStockMove>();

        sql = "SELECT s.id_item, s.id_unit, s.id_lot, l.lot, l.dt_exp_n, l.b_block, " +
                "IF(l.dt_exp_n IS NOT NULL, 0, 1) AS f_ord, " +
                "SUM(s.debit - s.credit) AS f_bal, " +
                "SUM(s.mov_in - s.mov_out) AS f_stk " +
                "FROM trn_stk AS s " +
                "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot " +
                (!onlyUnblocked ? "" : "AND l.b_block = 0 ") +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                "WHERE s.id_year = " + year + " AND s.id_cob = " + warehouseKey[0] + " AND s.id_wh = " + warehouseKey[1] + " AND s.b_del = 0 " +
                (itemId_n == SLibConstants.UNDEFINED || unitId_n == SLibConstants.UNDEFINED ? "" : "AND s.id_item = " + itemId_n + " AND s.id_unit = " + unitId_n + " ") +
                (iogKey_n == null ? "" : "AND NOT (s.fid_diog_year = " + iogKey_n[0] + " AND s.fid_diog_doc = " + iogKey_n[1] + ") ") +
                "GROUP BY s.id_item, s.id_unit, s.id_lot, l.lot, l.dt_exp_n, l.b_block " +
                "HAVING f_stk <> 0 " +
                "ORDER BY " + (client.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") +
                "s.id_item, u.symbol, s.id_unit, f_ord, l.dt_exp_n, l.lot, s.id_lot ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        while (resulSet.next()) {
            stockMove = new STrnStockMove(new int[] { year, resulSet.getInt("s.id_item"), resulSet.getInt("s.id_unit"), resulSet.getInt("s.id_lot"),
                warehouseKey[0], warehouseKey[1] }, resulSet.getDouble("f_stk"));

            stockMove.setAuxLot(resulSet.getString("l.lot"));
            stockMove.setAuxLotDateExpiration(resulSet.getDate("l.dt_exp_n"));
            stockMove.setAuxIsLotBlocked(resulSet.getBoolean("l.b_block"));
            stockMove.setValue(resulSet.getDouble("f_bal"));

            stockMoves.add(stockMove);
        }

        return stockMoves;
    }

    /**
     * Obtain older lot:
     * @param client
     * @param warehouseKey
     * @param itemId
     * @param unitId
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public static int[] obtainOlderLot(final SClientInterface client, final int[] warehouseKey, final int itemId, final int unitId) throws Exception {
        int[] lot = new int[3];
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COALESCE(l.dt_exp_n, NOW()) AS dt_exp_n, SUM(s.mov_in - s.mov_out) AS f_stk, s.id_lot, l.lot " +
                "FROM trn_stk AS s " +
                "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot AND l.b_block = 0  " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                "WHERE s.id_cob = " + warehouseKey[0] + " AND s.id_wh = " + warehouseKey[1] + " AND s.b_del = 0 " +
                "AND s.id_item = " + itemId + " AND s.id_unit = " + unitId + " " +
                "GROUP BY s.id_item, s.id_unit, s.id_lot, l.lot, l.dt_exp_n " +
                "HAVING f_stk <> 0 " +
                "ORDER BY l.dt_exp_n, l.lot, s.id_lot ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            lot[0] = itemId;
            lot[1] = unitId;
            lot[2] = resultSet.getInt("s.id_lot");
        }

        return lot;
    }
    
    /**
     * Validate if has stock for a given item.
     * @param client GUI client.
     * @param year Stock year.
     * @param itemId Stock item ID.
     * @param lotId_n Stock lot ID, can be 0 meaning undefined.
     * @param dateCutOff_n Cut off date.
     * @return true if has.
     * @throws Exception
     */
    public static boolean hasStock(final SClientInterface client, final int year, final int itemId, final int lotId_n, final Date dateCutOff_n) throws Exception {
        String sql = "";
        ResultSet resulSet = null;
        boolean has = false;

        sql = "SELECT SUM(s.mov_in - s.mov_out) AS f_stk, s.id_item, s.id_unit " +
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot " +
                "WHERE s.id_year = " + year + " AND s.dt <= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(dateCutOff_n) + "' AND s.id_item = " + itemId +
                (lotId_n == SLibConstants.UNDEFINED ? "" : " AND s.id_lot = " + lotId_n) + " AND s.b_del = 0 " +
                "GROUP BY s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh " +
                "HAVING f_stk <> 0 " +
                "ORDER BY s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            has = true;
        }

        return has;
    }

    /**
     * Obtains lots as stock moves for a given quantity.
     *
     * @param stockMoves Vector of STrnStockMove objects, with available lots.
     * @param quantity Quantity required.
     * @return Vector of STrnStockMove objects, with required lots.
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static Vector<STrnStockMove> obtainRequiredLots(final Vector<STrnStockMove> availableMoves, final double quantityReq) throws Exception {
        double quantitySum = 0;
        STrnStockMove requiredMove = null;
        Vector<STrnStockMove> requiredMoves = new Vector<STrnStockMove>();

        if (quantityReq > 0) {
            for (STrnStockMove availableMove : availableMoves) {
                requiredMove = availableMove.clone();

                if (requiredMove.getQuantity() > (quantityReq - quantitySum)) {
                    requiredMove.setQuantity(quantityReq - quantitySum);
                }

                requiredMoves.add(requiredMove);

                quantitySum += requiredMove.getQuantity();
                if (quantityReq >= quantitySum) {
                    break;
                }
            }
        }

        return requiredMoves;
    }

    /**
     * Obtains materials assignated to production order.
     *
     * @param client GUI client.
     * @param date DIOG date (stock move date).
     * @param prodOrderKey Production order primary key (one document per available warehouse).
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     * @param warehouseKey Warehouse primary key.
     * @param justThisWarehouse Flag that indicates that only provided warehouse must be considered for materials assignated in production order to consume them.
     * @return Vector of SDataDiog objects.
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public static Vector<SDataDiog> obtainProdOrderMaterialsAssigned(final SClientInterface client, final Date date, final int[] prodOrderKey, final int[] iogTypeKey) throws Exception {
        int pos = 0;
        int year = 0;
        int[] auxLotKey = null;
        int[] curLotKey = null;
        int[] auxWarehouseKey = null;
        int[] curWarehouseKey = null;
        double qty = 0;
        String sql = "";
        String series = "";
        SServerRequest request = null;
        SServerResponse response = null;
        Vector<Vector<Object>> rows = null;
        Vector<SFormComponentItem> items = null;
        SDataCompanyBranchEntity warehouse = null;
        SDataDiog iog = null;
        SDataDiogEntry iogEntry = null;
        Vector<SDataDiog> iogs = new Vector<SDataDiog>();

        sql = "SELECT s.id_cob, s.id_wh, s.id_item, s.id_unit, s.id_lot, " +    // 04
                "i.item_key, i.item, u.symbol, l.lot, l.dt_exp_n, " +           // 09
                "SUM(s.mov_in - s.mov_out) AS f_net " +                         // 10
                "FROM trn_diog AS g " +
                "INNER JOIN trn_diog_ety AS ge ON g.fid_mfg_year_n = " + prodOrderKey[0] + " AND g.fid_mfg_ord_n = " + prodOrderKey[1] + " AND " +
                "g.id_year = ge.id_year AND g.id_doc = ge.id_doc AND g.b_del = 0 AND ge.b_del = 0 " +
                "INNER JOIN trn_stk AS s ON ge.id_year = s.fid_diog_year AND ge.id_doc = s.fid_diog_doc AND ge.id_ety = s.fid_diog_ety AND s.b_del = 0 " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot " +
                "WHERE (" +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ") OR " +
                "(g.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[0] + " AND g.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[1] + " AND g.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET[2] + ")) " +
                "GROUP BY s.id_cob, s.id_wh, s.id_item, s.id_unit, s.id_lot, " +
                "i.item_key, i.item, u.symbol, l.lot, l.dt_exp_n " +
                "HAVING f_net <> 0 " +
                "ORDER BY s.id_cob, s.id_wh, i.item_key, i.item, u.symbol, l.lot, l.dt_exp_n, " +
                "s.id_item, s.id_unit, s.id_lot ";

        request = new SServerRequest(SServerConstants.REQ_DB_QUERY_SIMPLE, sql);
        response = client.getSessionXXX().request(request);

        if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
            throw new Exception(response.getMessage());
        }
        else {
            year = SLibTimeUtilities.digestYear(date)[0];

            rows = (Vector<Vector<Object>>) response.getPacket();
            for (Vector<Object> row : rows) {
                auxWarehouseKey = new int[] { ((Number) row.get(0)).intValue(), ((Number) row.get(1)).intValue() };

                if (!SLibUtilities.compareKeys(auxWarehouseKey, curWarehouseKey)) {
                    curWarehouseKey = auxWarehouseKey;
                    curLotKey = null;
                    pos = 0;

                    warehouse = (SDataCompanyBranchEntity) SDataUtilities.readRegistry(client, SDataConstants.CFGU_COB_ENT, curWarehouseKey, SLibConstants.EXEC_MODE_SILENT);
                    items = warehouse.getDnsForDiog(new int[] { iogTypeKey[0], iogTypeKey[1] });
                    series = items.size() == 0 ? "" : items.get(0).toString();

                    iog = new SDataDiog();
                    iog.setPkYearId(year);
                    iog.setPkDocId(0);
                    iog.setDate(date);
                    iog.setNumberSeries(series);
                    iog.setNumber("");
                    iog.setReference("");
                    iog.setIsAudited(false);
                    iog.setIsAuthorized(false);
                    iog.setIsRecordAutomatic(false);
                    iog.setIsSystem(true);
                    iog.setIsDeleted(false);
                    iog.setFkDiogCategoryId(iogTypeKey[0]);
                    iog.setFkDiogClassId(iogTypeKey[1]);
                    iog.setFkDiogTypeId(iogTypeKey[2]);
                    iog.setFkDiogAdjustmentTypeId(1);
                    iog.setFkCompanyBranchId(curWarehouseKey[0]);
                    iog.setFkWarehouseId(curWarehouseKey[1]);
                    iog.setFkMfgYearId_n(prodOrderKey[0]);
                    iog.setFkMfgOrderId_n(prodOrderKey[1]);
                    iog.setFkMaintMovementTypeId(SModSysConsts.TRNS_TP_MAINT_MOV_NA);
                    iog.setFkMaintUserId_n(SLibConsts.UNDEFINED);
                    iog.setFkMaintUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
                    iog.setFkMaintReturnUserId_n(SLibConsts.UNDEFINED);
                    iog.setFkMaintReturnUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
                    iog.setFkUserShippedId(SUtilConsts.USR_NA_ID);
                    iog.setFkUserAuditedId(SUtilConsts.USR_NA_ID);
                    iog.setFkUserAuthorizedId(SUtilConsts.USR_NA_ID);
                    iog.setFkUserNewId(client.getSession().getUser().getPkUserId());

                    iogs.add(iog);
                }

                auxLotKey = new int[] { ((Number) row.get(2)).intValue(), ((Number) row.get(3)).intValue(), ((Number) row.get(4)).intValue() };

                if (!SLibUtilities.compareKeys(auxLotKey, curLotKey)) {
                    curLotKey = auxLotKey;
                    qty = 0;

                    iogEntry = new SDataDiogEntry();
                    iogEntry.setPkYearId(year);
                    iogEntry.setPkDocId(0);
                    iogEntry.setPkEntryId(0);
                    iogEntry.setSortingPosition(++pos);
                    iogEntry.setIsInventoriable(true);
                    iogEntry.setIsDeleted(false);
                    iogEntry.setFkItemId(curLotKey[0]);
                    iogEntry.setFkUnitId(curLotKey[1]);
                    iogEntry.setFkOriginalUnitId(curLotKey[1]);
                    iogEntry.setFkUserNewId(client.getSession().getUser().getPkUserId());

                    iog.getDbmsEntries().add(iogEntry);
                }

                qty += ((Number) row.get(10)).doubleValue();
                iogEntry.setQuantity(qty);
                iogEntry.setOriginalQuantity(qty);
                iogEntry.getAuxStockMoves().add(new STrnStockMove(new int[] { year, curLotKey[0], curLotKey[1], curLotKey[2], curWarehouseKey[0], curWarehouseKey[1] }, ((Number) row.get(10)).doubleValue()));
            }
        }

        return iogs;
    }

    public static double obtainQuantityLimit(final SClientInterface client, final int nTypeLink, final int[] dpsSourceKey, final int[] dpsDestinyKey) throws Exception {
        double qty = 0;
        String sql = "";
        ResultSet resulSet = null;

        if (dpsDestinyKey != null) {
            sql = "SELECT orig_qty, surplus_per " +
                    "FROM trn_dps_ety " +
                    "WHERE id_year = " + dpsSourceKey[0] + " AND id_doc = " + dpsSourceKey[1] + " AND id_ety = " + dpsSourceKey[2] + " ";

            resulSet = client.getSession().getStatement().executeQuery(sql);
            if (resulSet.next()) {
                qty = resulSet.getDouble("orig_qty") * (1d + resulSet.getDouble("surplus_per"));
            }
        }

        switch (nTypeLink) {
            case SDataConstants.TRN_DPS_DPS_SUPPLY:
                sql = "SELECT COALESCE(SUM(s.orig_qty), 0) AS f_orig_qty " +
                        "FROM trn_dps_dps_supply AS s " +
                        "WHERE s.id_src_year = " + dpsSourceKey[0] + " AND s.id_src_doc = " + dpsSourceKey[1] + " AND s.id_src_ety = " + dpsSourceKey[2] +
                        (dpsDestinyKey == null ? "" : " AND NOT(s.id_des_year = " + dpsDestinyKey[0] + " AND s.id_des_doc = " + dpsDestinyKey[1] + " )");
                break;
            case SDataConstants.TRN_DPS_DPS_ADJ:
                sql = "SELECT COALESCE(SUM(orig_qty), 0) AS f_orig_qty " +
                        "FROM trn_dps_dps_adj " +
                        "WHERE id_dps_year = " + dpsSourceKey[0] + " AND id_dps_doc = " + dpsSourceKey[1] + " AND id_dps_ety = " + dpsSourceKey[2] +
                        (dpsDestinyKey == null ? "" : " AND NOT(id_adj_year = " + dpsDestinyKey[0] + " AND id_adj_doc = " + dpsDestinyKey[1] + " )");
                break;
            default:
                break;
        }

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            if (dpsDestinyKey != null) {
                qty -= resulSet.getDouble("f_orig_qty");
            }
            else {
                qty = resulSet.getDouble("f_orig_qty");
            }
        }

        return qty;
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean isIogTypeForTransfer(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_OUT_INT_TRA });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean isIogTypeForDpsSupply(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_IN_PUR_PUR, SDataConstantsSys.TRNS_TP_IOG_OUT_SAL_SAL });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean isIogTypeForDpsReturn(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_OUT_PUR_PUR, SDataConstantsSys.TRNS_TP_IOG_IN_SAL_SAL });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean isIogTypeForProdOrder(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean isIogTypeForProdOrderRm(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean isIogTypeForProdOrderWp(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean isIogTypeForProdOrderFg(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean isIogTypeAdjustment(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_IN_ADJ_ADJ, SDataConstantsSys.TRNS_TP_IOG_OUT_ADJ_ADJ});
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean canIogTypeWarehousesBeTheSame(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            /*SDataConstantsSys.TRNS_TP_IOG_IN_INT_CNV,*/SDataConstantsSys.TRNS_TP_IOG_OUT_INT_CNV,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean needsIogTypeWarehouseDestiny(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            /*SDataConstantsSys.TRNS_TP_IOG_IN_INT_TRA,*/SDataConstantsSys.TRNS_TP_IOG_OUT_INT_TRA,
            /*SDataConstantsSys.TRNS_TP_IOG_IN_INT_CNV,*/SDataConstantsSys.TRNS_TP_IOG_OUT_INT_CNV,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static boolean needsIogTypeProdOrderDestiny(final int[] iogTypeKey) {
        return SLibUtilities.belongsTo(iogTypeKey, new int[][] {
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD,
            SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET });
    }

    /**
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     */
    public static int[] getIogTypeCounterpart(final int[] iogTypeKey) {
        int[] key = null;

        if /**/ (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_INT_TRA)) {
            key = SDataConstantsSys.TRNS_TP_IOG_IN_INT_TRA;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_INT_CNV)) {
            key = SDataConstantsSys.TRNS_TP_IOG_IN_INT_CNV;
        }
        /* Not necessary cases:
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_INT_TRA)) {
            key = SDataConstantsSys.TRNS_TP_IOG_OUT_INT_TRA;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_INT_CNV)) {
            key = SDataConstantsSys.TRNS_TP_IOG_OUT_INT_CNV;
        }
        */
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD)) {
            key = SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET)) {
            key = SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_RET;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD)) {
            key = SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET)) {
            key = SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD)) {
            key = SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET)) {
            key = SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD)) {
            key = SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_RET)) {
            key = SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD)) {
            key = SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET)) {
            key = SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD)) {
            key = SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD;
        }
        else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET)) {
            key = SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET;
        }

        return key;
    }

    /**
     * Gets appropriate warehouse types array for provided parameters.
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     * @param warehouseMode Warehouse mode (source or destiny). Constants defined in SLibConstants (i.e. MODE_AS_SRC, MODE_AS_DES).
     */
    public static int[][] getAppropriateWarehouseTypes(final int[] iogTypeKey, final int warehouseMode) {
        int[] manIogClassKey = new int[] { iogTypeKey[0], iogTypeKey[1] };
        int[][] warehouseTypeKeys = null;

        if (SLibUtilities.belongsTo(manIogClassKey, new int[][] { SDataConstantsSys.TRNS_CL_IOG_IN_PUR, SDataConstantsSys.TRNS_CL_IOG_OUT_PUR })) {
            warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_GDS, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM, SDataConstantsSys.CFGS_TP_ENT_WH_SP };
        }
        else if (SLibUtilities.belongsTo(manIogClassKey, new int[][] { SDataConstantsSys.TRNS_CL_IOG_IN_SAL, SDataConstantsSys.TRNS_CL_IOG_OUT_SAL })) {
            warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_GDS, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG };
        }
        else if (SLibUtilities.belongsTo(manIogClassKey, new int[][] { SDataConstantsSys.TRNS_CL_IOG_IN_INT, SDataConstantsSys.TRNS_CL_IOG_OUT_INT })) {
            if (SLibUtilities.belongsTo(iogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_IN_INT_CNV, SDataConstantsSys.TRNS_TP_IOG_OUT_INT_CNV })) {
                warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_GDS };
            }
        }
        else if (SLibUtilities.belongsTo(manIogClassKey, new int[][] { SDataConstantsSys.TRNS_CL_IOG_IN_EXT, SDataConstantsSys.TRNS_CL_IOG_OUT_EXT })) {
            if /**/ (SLibUtilities.belongsTo(iogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_IN_EXT_CHG_PUR, SDataConstantsSys.TRNS_TP_IOG_OUT_EXT_CHG_PUR })) {
                warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_GDS };
            }
            else if (SLibUtilities.belongsTo(iogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_IN_EXT_CHG_SAL, SDataConstantsSys.TRNS_TP_IOG_OUT_EXT_CHG_SAL })) {
                warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_GDS };
            }
            else if (SLibUtilities.belongsTo(iogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_IN_EXT_WAR_PUR, SDataConstantsSys.TRNS_TP_IOG_OUT_EXT_WAR_PUR })) {
                warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_WAR_PUR };
            }
            else if (SLibUtilities.belongsTo(iogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_IN_EXT_WAR_SAL, SDataConstantsSys.TRNS_TP_IOG_OUT_EXT_WAR_SAL })) {
                warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_WAR_SAL };
            }
            else if (SLibUtilities.belongsTo(iogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_IN_EXT_CSG_PUR, SDataConstantsSys.TRNS_TP_IOG_OUT_EXT_CSG_PUR })) {
                warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_CSG_PUR };
            }
            else if (SLibUtilities.belongsTo(iogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_IN_EXT_CSG_SAL, SDataConstantsSys.TRNS_TP_IOG_OUT_EXT_CSG_SAL })) {
                warehouseTypeKeys = new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_CSG_SAL };
            }
        }
        else if (SLibUtilities.belongsTo(manIogClassKey, new int[][] { SDataConstantsSys.TRNS_CL_IOG_IN_MFG, SDataConstantsSys.TRNS_CL_IOG_OUT_MFG })) {
            if /**/ (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
            }
            else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_RET)) {
                warehouseTypeKeys = warehouseMode == SLibConstants.MODE_AS_SRC ? new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM } : new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP };
            }
        }

        return warehouseTypeKeys;
    }

    /**
     * Validates appropriate warehouse types array for provided parameters.
     * @param iogTypeKey IOG type key. Constants defined in SDataConstantsSys.
     * @param warehouseMode Warehouse mode (source or destiny). Constants defined in SLibConstants (i.e. MODE_AS_SRC, MODE_AS_DES).
     * @return Empty String if validation is correct, otherwise validation error message.
     */
    public static String validateAppropiateWarehouseType(final SClientInterface client, final int[] iogTypeKey, final int[] warehouseTypeKey, final int warehouseMode) {
        String msg = "";

        switch (warehouseMode) {
            case SLibConstants.MODE_AS_SRC:
                if /**/ (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG);
                        msg += " o " + SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_GDS);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG);
                        msg += " o " + SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_GDS);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM);
                    }
                }
                break;

            case SLibConstants.MODE_AS_DES:
                if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG);
                        msg += " o " + SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_GDS);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_RM);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG, SDataConstantsSys.CFGS_TP_ENT_WH_GDS })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_FG);
                        msg += " o " + SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_GDS);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_WP_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                else if (SLibUtilities.compareKeys(iogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_MFG_RM_RET)) {
                    if (!SLibUtilities.belongsTo(warehouseTypeKey, new int[][] { SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP })) {
                        msg = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.CFGS_TP_ENT, SDataConstantsSys.CFGS_TP_ENT_WH_MFG_WP);
                    }
                }
                break;

            default:
                client.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return msg;
    }
    
    /**
     * Gets proper IOG category ID for for provided DPS class key.
     * @param dpsClassKey DPS class key. Constants defined in SDataConstantsSys.
     * @return Corresponding IOG category ID, if any, otherwise <code>SLibConsts.UNDEFINED</code>.
     */
    public static int getIogCatForDpsClass(final int[] dpsClassKey) {
        int cat = SLibConsts.UNDEFINED;
        
        if (SLibUtils.belongsTo(dpsClassKey, new int[][] { SDataConstantsSys.TRNS_CL_DPS_PUR_EST, SDataConstantsSys.TRNS_CL_DPS_PUR_ORD, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ })) {
            cat = SDataConstantsSys.TRNS_CT_IOG_IN;
        }
        else if (SLibUtils.belongsTo(dpsClassKey, new int[][] { SDataConstantsSys.TRNS_CL_DPS_SAL_EST, SDataConstantsSys.TRNS_CL_DPS_SAL_ORD, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ })) {
            cat = SDataConstantsSys.TRNS_CT_IOG_OUT;
        }
        
        return cat;
    }

    /**
     * Validates appropriate warehouse item for provided parameter.
     * @param client
     * @param itemId
     * @param warehouseKey
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static boolean getIsAppropiateWarehouseItem(final SClientInterface client, final int itemId, final int[] warehouseKey) throws Exception {
        boolean bIsAppropiate = false;
        String sql = "";
        ResultSet resulSet = null;

        sql = "SELECT * " +
              "FROM trn_stk_cfg_item " +
              "WHERE b_del = 0 AND id_cob = " + warehouseKey[0] + " AND id_wh = " + warehouseKey[1] + " " +
              "ORDER BY id_tp_link ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            for (int tp_link = SDataConstantsSys.TRNS_TP_LINK_ITEM; tp_link >= SDataConstantsSys.TRNS_TP_LINK_ALL; tp_link--) {
                switch(tp_link) {
                    case SDataConstantsSys.TRNS_TP_LINK_ITEM:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM trn_stk_cfg_item AS stk_it " +
                        "INNER JOIN erp.itmu_item AS i ON i.id_item = stk_it.id_ref " +
                        "WHERE stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_ITEM +  " AND stk_it.id_ref = " + itemId + " AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_MFR:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itmu_mfr AS mfr " +
                        "INNER JOIN erp.itmu_item AS i ON mfr.id_mfr = i.fid_mfr AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_MFR +  " AND stk_it.id_ref = i.fid_mfr AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_BRD:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itmu_brd AS brd " +
                        "INNER JOIN erp.itmu_item AS i ON brd.id_brd = i.fid_brd AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_BRD +  " AND stk_it.id_ref = i.fid_brd AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_LINE:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itmu_line AS line " +
                        "INNER JOIN erp.itmu_item AS i ON line.id_line = i.fid_line_n AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_LINE +  " AND stk_it.id_ref = i.fid_line_n AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_IGEN:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itmu_igen AS gen " +
                        "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IGEN +  " AND stk_it.id_ref = i.fid_igen AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_IGRP:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itmu_igrp AS grp " +
                        "INNER JOIN erp.itmu_igen AS gen ON grp.id_igrp = gen.fid_igrp " +
                        "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IGRP +  " AND stk_it.id_ref = grp.id_igrp AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_IFAM:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itmu_ifam AS fam " +
                        "INNER JOIN erp.itmu_igrp AS grp ON fam.id_ifam = grp.fid_ifam " +
                        "INNER JOIN erp.itmu_igen AS gen ON gen.fid_igrp = grp.id_igrp " +
                        "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_IFAM +  " AND stk_it.id_ref = fam.id_ifam AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itms_ct_item AS ct " +
                        "INNER JOIN erp.itms_cl_item AS cl ON ct.id_ct_item = cl.id_ct_item " +
                        "INNER JOIN erp.itms_tp_item AS tp ON tp.id_ct_item = ct.id_ct_item AND tp.id_cl_item = cl.id_cl_item " +
                        "INNER JOIN erp.itmu_igen AS gen ON gen.fid_ct_item = ct.id_ct_item AND gen.fid_cl_item = cl.id_cl_item AND gen.fid_tp_item = tp.id_tp_item " +
                        "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_TP_ITEM +  " AND stk_it.id_ref = tp.id_tp_item AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itms_ct_item AS ct " +
                        "INNER JOIN erp.itms_cl_item AS cl ON ct.id_ct_item = cl.id_ct_item " +
                        "INNER JOIN erp.itmu_igen AS gen ON gen.fid_ct_item = ct.id_ct_item AND gen.fid_cl_item = cl.id_cl_item " +
                        "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_CL_ITEM +  " AND stk_it.id_ref = cl.id_cl_item AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM erp.itms_ct_item AS ct " +
                        "INNER JOIN erp.itmu_igen AS gen ON gen.fid_ct_item = ct.id_ct_item " +
                        "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen AND i.id_item = " + itemId + " " +
                        "INNER JOIN trn_stk_cfg_item AS stk_it ON stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_CT_ITEM +  " AND stk_it.id_ref = ct.id_ct_item AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    case SDataConstantsSys.TRNS_TP_LINK_ALL:

                        sql = "SELECT stk_it.id_tp_link, stk_it.id_ref, stk_it.id_cob, stk_it.id_wh " +
                        "FROM trn_stk_cfg_item AS stk_it " +
                        "WHERE stk_it.b_del = 0 AND stk_it.id_tp_link = " + SDataConstantsSys.TRNS_TP_LINK_ALL +  " AND stk_it.id_ref = 0 AND stk_it.id_cob = " + warehouseKey[0] + " AND stk_it.id_wh = " + warehouseKey[1] + " ";
                        break;
                    default:
                }

                resulSet = client.getSession().getStatement().executeQuery(sql);
                if (resulSet.next()) {
                    bIsAppropiate = true;
                    break;
                }
            }
        }
        else {
            bIsAppropiate = true;
        }

        return bIsAppropiate;
    }

    /**
     * Validates appropriate warehouse dns for provided parameter.
     * @param client
     * @param itemId
     * @param warehouseKey
     * @param dnsId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static boolean getIsAppropiateWarehouseDns(final SClientInterface client, final int[] warehouseKey, final java.lang.String numberSeries, final int[] dpsType) throws Exception {
        boolean bIsAppropiate = false;
        int dnsId = 0;
        int totalNumberSeries = 0;
        String sql = "";
        ResultSet resulSet = null;

        sql = "SELECT COUNT(*) AS f_count " +
              "FROM trn_stk_cfg_dns " +
              "WHERE b_del = 0 AND id_cob = " + warehouseKey[0] + " AND id_wh = " + warehouseKey[1];

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            totalNumberSeries = resulSet.getObject("f_count") != null ? resulSet.getInt("f_count") : 0;
        }

        if (totalNumberSeries > 0) {
            sql = "SELECT id_dns " +
                  "FROM trn_dns_dps " +
                  "WHERE b_del = 0 AND dns = '" + numberSeries + "' AND fid_ct_dps = " + dpsType[0] + " AND fid_cl_dps = " + dpsType[1] + " " + " AND fid_tp_dps = " + dpsType[2];

            resulSet = client.getSession().getStatement().executeQuery(sql);
            if (resulSet.next()) {
                dnsId = resulSet.getObject("id_dns") != null ? resulSet.getInt("id_dns") : 0;
            }

            if (dnsId > 0) {
                sql = "SELECT * " +
                      "FROM trn_stk_cfg_dns " +
                      "WHERE b_del = 0 AND id_cob = " + warehouseKey[0] + " AND id_wh = " + warehouseKey[1] + " " + " AND id_dns = " + dnsId;

                resulSet = client.getSession().getStatement().executeQuery(sql);
                if (resulSet.next()) {
                    bIsAppropiate = true;
                }
                else {
                    bIsAppropiate = false;
                }
            }
        }
        else {
            bIsAppropiate = true;
        }

        return bIsAppropiate;
    }


    /**
     * Validate if can cancel stock.
     * @param client ERP Client interface.
     * @param dateCutOff_n Cut off date.
     * @param warehouseKey Warehouse primary key.
     * @return true if can.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static boolean canStockWarehouseBeCancel(final SClientInterface client,  final Date dateCutOff_n, final int[] warehouseKey) throws Exception {
        boolean bIsCan = true;
        String sql = "";
        ResultSet resulSet = null;

        sql = "SELECT COUNT(*) AS f_count FROM trn_stk WHERE b_del = 0 AND " +
                "dt >= '" + client.getSessionXXX().getFormatters().getDbmsDateFormat().format(dateCutOff_n) + "' AND " +
                "id_cob = " + warehouseKey[0] + " AND id_wh = " + warehouseKey[1] + " AND fid_ct_iog = " + SDataConstantsSys.TRNS_CT_IOG_OUT + " ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            if (resulSet.getInt("f_count") > 0) {
                bIsCan = false;
            }
        }

        return bIsCan;
    }

    /**
     * Parses user input to find items on inventory documents.
     * @param client GUI client interface.
     * @param textToFind General form is [999...*]XXX...
     * Where:
     * 999... is desired quantity.
     * XXX... is desired item code.
     */
    public static STrnItemFound computeItemFound(final SClientInterface client, final String textToFind) {
        int index = textToFind.indexOf("*");
        int[] itemFoundIds = null;
        double quantity = 0;
        String itemKey = "";
        SDataRegistry[] registries = null;
        STrnItemFound itemFound = null;

        if (index == -1) {
            quantity = 1;
            itemKey = textToFind;
        }
        else {
            quantity = SLibUtilities.parseDouble(textToFind.substring(0, index));
            itemKey = textToFind.substring(index + 1);
        }

        try {
            itemFound = new STrnItemFound(quantity, itemKey);
            registries = (SDataRegistry[]) SDataUtilities.readRegistriesByKey(client, SDataConstants.ITMU_ITEM, itemKey, SLibConstants.EXEC_MODE_SILENT);

            if (registries != null) {
                itemFoundIds = new int[registries.length];

                for (SDataRegistry registry : registries) {
                    itemFoundIds[0] = ((int[]) registry.getPrimaryKey())[0];
                }

                itemFound.setItemFoundIds(itemFoundIds);
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }

        return itemFound;
    }

    /**
     * Gets all production order charge entry lots that fit provided parameters.
     */
    private static Vector<SDataProductionOrderChargeEntryLot> getProdOrderChargeEntryLots(final SDataProductionOrder prodOrder, final int chargeId, final int itemId, final int unitId) {
        Vector<SDataProductionOrderChargeEntryLot> chargeEntryLots = new Vector<SDataProductionOrderChargeEntryLot>();

        for (SDataProductionOrderCharge charge : prodOrder.getDbmsProductionOrderCharges()) {
            if (charge.getPkChargeId() == chargeId) {
                for (SDataProductionOrderChargeEntry chargeEntry : charge.getDbmsProductionOrderChargeEntries()) {
                    if (chargeEntry.getFkItemId_r() == itemId && chargeEntry.getFkUnitId_r() == unitId) {
                        chargeEntryLots.addAll(chargeEntry.getDbmsProductionOrderChargeEntryLots());
                    }
                }
            }
        }

        return chargeEntryLots;
    }

    /**
     * Prepares stock lot information for provided stock moves.
     */
    private static void prepareStockMovesLotsInfo(final SClientInterface client, Vector<STrnStockMove> stockMoves) {
        SDataStockLot lot = null;

        for (STrnStockMove stockMove : stockMoves) {
            lot = (SDataStockLot) SDataUtilities.readRegistry(client, SDataConstants.TRN_LOT, stockMove.getLotKey(), SLibConstants.EXEC_MODE_STEALTH);
            if (lot != null) {
                stockMove.setAuxLot(lot.getLot());
                stockMove.setAuxLotDateExpiration(lot.getDateExpiration_n());
                stockMove.setAuxIsLotBlocked(lot.getIsBlocked());
            }
        }
    }

    /**
     * Gets stock moves for raw materials assignment on a production order as defined in lots assignation.
     */
    public static Vector<STrnStockMove> getProdOrderChargeLotsToAssign(final SClientInterface client, final SDataProductionOrder prodOrder, final int year, final int[] warehouseKey, final int chargeId, final int itemId, final int unitId, final double qtyAssigned, final double qtyToAssign) {
        double qty = 0;
        double qtyLotted = 0;
        double qtyProcessed = 0;
        Vector<SDataProductionOrderChargeEntryLot> chargeEntryLots = null;
        Vector<STrnStockMove> stockMoves = new Vector<STrnStockMove>();

        if (qtyToAssign > 0) {
            chargeEntryLots = getProdOrderChargeEntryLots(prodOrder, chargeId, itemId, unitId);

            for (SDataProductionOrderChargeEntryLot chargeEntryLot : chargeEntryLots) {
                qtyLotted += chargeEntryLot.getQuantity();

                if (qtyLotted >= qtyAssigned) {
                    if (qtyProcessed == 0) {
                        qty = qtyLotted - qtyAssigned;
                    }
                    else {
                        qty = chargeEntryLot.getQuantity();
                    }

                    if (qty > 0) {
                        if (qtyProcessed + qty > qtyToAssign) {
                            qty = qtyToAssign - qtyProcessed;
                        }

                        stockMoves.add(new STrnStockMove(new int[] { year, chargeEntryLot.getPkItemId(), chargeEntryLot.getPkUnitId(), chargeEntryLot.getPkLotId(), warehouseKey[0], warehouseKey[1] }, qty));

                        qtyProcessed += qty;
                        if (qtyProcessed >= qtyToAssign) {
                            break;
                        }
                    }
                }
            }
        }

        prepareStockMovesLotsInfo(client, stockMoves);  // add lots information (i.e. name, expiration date, etc.)

        return stockMoves;
    }

    /**
     * Gets stock moves for raw materials return on a production order as defined in lots assignation.
     */
    public static Vector<STrnStockMove> getProdOrderChargeLotsToReturn(final SClientInterface client, final SDataProductionOrder prodOrder, final int year, final int[] warehouseKey, final int chargeId, final int itemId, final int unitId, final double qtyAssigned, final double qtyToReturn) {
        double qty = 0;
        double qtyLotted = 0;
        double qtyProcessed = 0;
        double qtyToReturnApprobed = qtyToReturn <= qtyAssigned ? qtyToReturn : qtyAssigned;
        SDataProductionOrderChargeEntryLot chargeEntryLot = null;
        Vector<SDataProductionOrderChargeEntryLot> chargeEntryLots = null;
        Vector<STrnStockMove> stockMoves = new Vector<STrnStockMove>();

        if (qtyAssigned > 0 && qtyToReturnApprobed > 0) {
            chargeEntryLots = getProdOrderChargeEntryLots(prodOrder, chargeId, itemId, unitId);

            for (int i = 0; i < chargeEntryLots.size(); i++) {
                chargeEntryLot = chargeEntryLots.get(i);
                qtyLotted += chargeEntryLot.getQuantity();

                if (qtyLotted >= qtyAssigned) {
                    for (; i >= 0; i--) {
                        chargeEntryLot = chargeEntryLots.get(i);

                        if (qtyProcessed == 0) {
                            qty = qtyAssigned - (qtyLotted - chargeEntryLot.getQuantity());
                        }
                        else {
                            qty = chargeEntryLot.getQuantity();
                        }

                        if (qty > 0) {
                            if (qtyProcessed + qty > qtyToReturnApprobed) {
                                qty = qtyToReturnApprobed - qtyProcessed;
                            }

                            stockMoves.add(new STrnStockMove(new int[] { year, chargeEntryLot.getPkItemId(), chargeEntryLot.getPkUnitId(), chargeEntryLot.getPkLotId(), warehouseKey[0], warehouseKey[1] }, qty));

                            qtyProcessed += qty;
                            if (qtyProcessed >= qtyToReturnApprobed) {
                                break;
                            }
                        }
                    }

                    break;
                }
            }
        }

        prepareStockMovesLotsInfo(client, stockMoves);  // add lots information (i.e. name, expiration date, etc.)

        return stockMoves;
    }

    public static void printDiog(final SClientInterface client, final int[] key) {
        Cursor cursor = null;
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        SDataDiog diog = null;
        SDataBizPartnerBranchAddress oAddress = null;

        diog = (SDataDiog) SDataUtilities.readRegistry(client, SDataConstants.TRN_DIOG, key, SLibConstants.EXEC_MODE_VERBOSE);
        oAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BPB_ADD, new int [] { diog.getFkCompanyBranchId(), 1}, SLibConstants.EXEC_MODE_SILENT);

        try {
            cursor = client.getFrame().getCursor();
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));

            map = client.createReportParams();
            map.put("nPkYearId", diog.getPkYearId());
            map.put("nPkDocId", diog.getPkDocId());
            map.put("sCalle", oAddress.getStreet());
            map.put("sNoExterior", oAddress.getStreetNumberExt());
            map.put("sNoInterior", oAddress.getStreetNumberInt());
            map.put("sColonia", oAddress.getNeighborhood());
            map.put("sLocalidad", oAddress.getLocality());
            map.put("sReferencia", oAddress.getReference());
            map.put("sMunicipio", oAddress.getCounty());
            map.put("sEstado", oAddress.getState());
            map.put("sPais", oAddress.getDbmsDataCountry().getCountry());
            map.put("sCodigoPostal", oAddress.getZipCode());

            jasperPrint = SDataUtilities.fillReport(client, SDataConstantsSys.REP_TRN_DIOG, map);
            jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle("Impresión de documento de inventarios");
            jasperViewer.setVisible(true);
        }
        catch (Exception e) {
            SLibUtilities.renderException(STrnUtilities.class.getName(), e);
        }
        finally {
            client.getFrame().setCursor(cursor);
        }
    }

    public static String getMailToSendForOrder(final SClientInterface client, final int[] keyDoc) throws Exception {
        String mailToSend = "";
        SDataDps oDps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, keyDoc, SLibConstants.EXEC_MODE_SILENT);
        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[] { oDps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_SILENT);
        
        mailToSend = bizPartner.getBizPartnerBranchContactMail(new int[] { oDps.getFkBizPartnerBranchId() });
        
        if (mailToSend.isEmpty()) {
            throw new Exception("El receptor del documento no tiene buzones de correo-e.");
        }
        
        return mailToSend;
    }

    public static String getMailToSendForCfd(final SClientInterface client, final int idBizPartner, final int idBizPartnerBranch, final int contactType) throws Exception {
        String mailToSend = "";
        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[] { idBizPartner }, SLibConstants.EXEC_MODE_SILENT);
        
        if (idBizPartnerBranch == SLibConstants.UNDEFINED) {
            mailToSend = bizPartner.getBizPartnerContactMail(contactType);
        }
        else {
            mailToSend = bizPartner.getBizPartnerBranchContactMail(new int[] { idBizPartnerBranch }, contactType);
        }
        
        if (mailToSend.isEmpty()) {
            throw new Exception("El receptor del documento no tiene buzones de correo-e.");
        }
        
        return mailToSend;
    }
    
     /**
     * Confirms CFDI mail sending.
     * @param client ERP Client interface.
     * @param title Dialog's title.
     * @param cfd CFI to be send.
     * @param dps DPS to be send.
     * @param idBizPartner id of Bussines Partner.
     * @param idBizPartnerBranch id of Bussines Partner Branch.
     * @return CFDI mail sending confirmation.
     * @throws RemoteException, Exception
     */
    public static boolean confirmSend(final SClientInterface client, final String title, final SDataCfd cfd, final SDataDps dps, final int idBizPartner, final int idBizPartnerBranch) throws RemoteException, Exception {
        boolean send = false;
        SSrvLock lock = null;
        SServerRequest request = null;
        SServerResponse response = null;
        SDialogCfdSend dlgCfdSend = null;
        SDataBizPartner bizPartner  = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[] { idBizPartner }, SLibConstants.EXEC_MODE_SILENT);
        
        dlgCfdSend = new SDialogCfdSend((SGuiClient) client, title, cfd, dps, bizPartner, idBizPartnerBranch);
        dlgCfdSend.setVisible(true);

        if (dlgCfdSend.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            if ((boolean) dlgCfdSend.getValue(SDialogCfdSend.VAL_IS_EMAIL_EDITED)) {
                lock = SSrvUtils.gainLock(client.getSession(), client.getSessionXXX().getCompany().getPkCompanyId(), SDataConstants.BPSU_BP, new int[] { idBizPartner }, bizPartner.getRegistryTimeout());
                
                if (idBizPartnerBranch == SLibConsts.UNDEFINED) {
                    bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0).setEmail01(((String) dlgCfdSend.getValue(SDialogCfdSend.VAL_EMAIL)));
                }
                else {
                    bizPartner.getDbmsBizPartnerBranch(new int[] { idBizPartnerBranch }).getDbmsBizPartnerBranchContacts().get(0).setEmail01(((String) dlgCfdSend.getValue(SDialogCfdSend.VAL_EMAIL)));
                }
                
                request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
                request.setPacket(bizPartner);
                response = client.getSessionXXX().request(request);

                SSrvUtils.releaseLock(client.getSession(), lock);
                
                if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage()));
                }
            }
            
            send = true;
        }
        
        return send;
    }
   
       /**
     * Sends a DPS
     * @param client ERP Client interface.
     * @param typeDps Type DPS .
     * @param dpsKey DPS key.
     * @param confirmSending It is true when the confirmation will be done.
     * @param catchExceptions When true all exceptions are handled internally, otherwise are shown into dialog messages.
     * @throws javax.mail.MessagingException, java.sql.SQLException
     */
    public static void sendDps(final SClientInterface client, final int typeDps, final int[] dpsKey,boolean confirmSending, boolean catchExceptions) throws MessagingException, SQLException, Exception {
        boolean send = true;
        int idBizPartner = SLibConsts.UNDEFINED;
        int idBizPartnerBranch = SLibConsts.UNDEFINED;
        SDataDps dps = null;
        
        dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, dpsKey, SLibConstants.EXEC_MODE_SILENT);
        idBizPartner = dps.getFkBizPartnerId_r();
        idBizPartnerBranch = dps.getFkBizPartnerBranchId();
            
        if (confirmSending) {
            send = confirmSend(client, SCfdUtils.TXT_SEND_DPS, null, dps, idBizPartner, idBizPartnerBranch);
        }

        if (send) {
            sendMailOrder(client, dpsKey, typeDps);
        }
    }
    
    /**
     * Send mail with information of contracts specificated.
     * @param client ERP Client interface.
     * @param keyDoc DPS primary Key.
     * @param typeDoc type the DPS SDataConstantsSys.TRNS_CT_DPS_PUR or SDataConstantsSys.TRNS_CT_DPS_SAL
     */
    public static void sendMailOrder(final SClientInterface client, final int[] keyDoc, final int typeDoc) {
        String addressee = "";
        String msg = "";
        String userMail = "";
        String bizPartnerMail = "";
        SDataDps oDps = null;
        boolean canSend = true;
        SMailSender sender = null;
        SMail mail = null;
        ArrayList<String> toRecipients = null;
        File pdf = null;
        SDbMms mms = null;     
        SDataBizPartner bizPartnerUserSend = null;

        try {
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            mms = getMms(client, typeDoc == SDataConstantsSys.TRNS_CT_DPS_PUR ? SModSysConsts.CFGS_TP_MMS_ORD_PUR : SModSysConsts.CFGS_TP_MMS_ORD_SAL);
            oDps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, keyDoc, SLibConstants.EXEC_MODE_SILENT);
            
            bizPartnerMail = getMailToSendForOrder(client,keyDoc);
            if (mms.getQueryResultId() != SDbConsts.READ_OK) {
                client.showMsgBoxWarning("No existe ningún correo-e configurado para envío de pedidos.");
            }
            else {
                if (((SDataUser) client.getSession().getUser()).getFkBizPartnerId_n() != SLibConstants.UNDEFINED) {
                    bizPartnerUserSend = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[] { ((SDataUser) client.getSession().getUser()).getFkBizPartnerId_n() }, SLibConstants.EXEC_MODE_SILENT); 
                    userMail = bizPartnerUserSend.getBizPartnerContactMail(SDataConstantsSys.BPSS_TP_CON_ADM);
                }
                
                sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), (userMail.isEmpty() ? mms.getUser() : userMail));
                toRecipients = new ArrayList<String>(Arrays.asList(SLibUtils.textExplode(bizPartnerMail, ";")));

                if (toRecipients.isEmpty()) {
                    client.showMsgBoxWarning("No existe ningún correo-e destinatario configurado.");
                    canSend = false;
                }
                else {
                    mail = new SMail(sender, mms.getTextSubject(), mms.getTextBody(), toRecipients);
                }

                if (canSend) {
                    pdf = new File("temp", oDps.getNumberSeries() + (oDps.getNumberSeries().isEmpty() ? "" : "_") + oDps.getNumber() + ".pdf");

                    createReportOrder(client, pdf, oDps, SDataConstantsPrint.PRINT_MODE_PDF_FILE);

                    mail.getAttachments().add(pdf);
                    mail.send();

                    for (String recipient : toRecipients) {
                        addressee += (addressee.isEmpty() ? "" : ";") + recipient;
                    }

                    if (!STrnUtilities.insertDpsSendLog(client, oDps, addressee, false)) {
                    }

                    pdf.delete();
                    client.showMsgBoxInformation("El correo-e ha sido enviado.\n" + msg);
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(STrnUtilities.class.getName(), e);
        }
        finally {
            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Send mail with information of contracts specificated.
     * @param client ERP Client interface.
     * @param numberStart Number contract initial.
     * @param numberEnd Number contract final.
     * @param recipientsTo Recipients the information.
     * @param recipientsCc Recipients carbon copy, the information.
     * @param recipientsBcc Recipients blind carbon copy, the information
     */
    public static void sendMailContractInfo(final SClientInterface client, int numberStart, int numberEnd, ArrayList<String> recipientsTo, ArrayList<String> recipientsCc, ArrayList<String> recipientsBcc) {
        String sql = "";
        String msg = "";
        boolean canSend = true;
        SMailSender sender = null;
        SMail mail = null;
        ArrayList<String> toRecipients = null;
        ArrayList<String> ccRecipients = null;
        ArrayList<String> bccRecipients = null;
        ArrayList<STrnDoc> aTrnDocs = null;
        File pdf = null;
        SDbMms mms = null;

        try {
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            mms = getMms(client, SModSysConsts.CFGS_TP_MMS_CON_SAL);

            if (mms.getQueryResultId() != SDbConsts.READ_OK) {
                client.showMsgBoxWarning("No existe ningún correo-e configurado para envío de movimientos de contratos.");
            }
            else {
                aTrnDocs = obtainContractsToSend(client, numberStart, numberEnd);
                msg = "Del rango: " + numberStart + " al " + numberEnd + " se encontraron " + aTrnDocs.size() + " registros.";

                if (aTrnDocs.isEmpty() && numberStart != SLibConstants.UNDEFINED && numberEnd != SLibConstants.UNDEFINED) {
                    client.showMsgBoxWarning(msg);
                }
                else {
                    toRecipients = recipientsTo;
                    ccRecipients = recipientsCc;
                    bccRecipients = recipientsBcc;

                    sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());

                    if (!mms.getRecipientTo().isEmpty() && toRecipients == null) {
                        toRecipients.addAll(Arrays.asList(SLibUtilities.textExplode(mms.getRecipientTo(), ";")));
                    }
                    if (!mms.getRecipientCarbonCopy().isEmpty() && ccRecipients == null) {
                        ccRecipients.addAll(Arrays.asList(SLibUtilities.textExplode(mms.getRecipientCarbonCopy(), ";")));
                    }
                    if (!mms.getRecipientBlindCarbonCopy().isEmpty() && bccRecipients == null) {
                        bccRecipients.addAll(Arrays.asList(SLibUtilities.textExplode(mms.getRecipientBlindCarbonCopy(), ";")));
                    }

                    if (toRecipients.isEmpty()) {
                        client.showMsgBoxWarning("No existe ningún destinatario configurado.");
                        canSend = false;
                    }
                    else if (ccRecipients.isEmpty() && bccRecipients.isEmpty()) {
                        mail = new SMail(sender, mms.getTextSubject(), mms.getTextBody(), toRecipients);
                    }
                    else if (!ccRecipients.isEmpty() && bccRecipients.isEmpty()) {
                        mail = new SMail(sender, mms.getTextSubject(), mms.getTextBody(), toRecipients, ccRecipients);
                    }
                    else {
                        mail = new SMail(sender, mms.getTextSubject(), mms.getTextBody(), toRecipients, ccRecipients, bccRecipients);
                    }

                    if (canSend) {
                        for (int i = 0; i < aTrnDocs.size(); i++) {
                            pdf = new File("CON_" + ((STrnDoc) aTrnDocs.get(i)).getNumberSeries() +
                                    (((STrnDoc) aTrnDocs.get(i)).getNumberSeries().isEmpty() ? "" : "_") + ((STrnDoc) aTrnDocs.get(i)).getNumber() + ".pdf");

                            createReportContractAnalysis(client, ((STrnDoc) aTrnDocs.get(i)), null);

                            mail.getAttachments().add(pdf);
                        }
                        mail.send();

                        sql = "INSERT INTO trn_mms_log SELECT " + SModSysConsts.CFGS_TP_MMS_CON_SAL + ", COALESCE(MAX(id_log + 1), 1), "
                               + client.getSession().getUser().getPkUserId() + ", NOW() FROM trn_mms_log WHERE id_mms = " + SModSysConsts.CFGS_TP_MMS_CON_SAL + " ";

                        if (!client.getSession().getStatement().execute(sql)) {
                        }

                        for (int i = 0; i < aTrnDocs.size(); i++) {
                            pdf = new File("CON_" + ((STrnDoc) aTrnDocs.get(i)).getNumberSeries() +
                                    (((STrnDoc) aTrnDocs.get(i)).getNumberSeries().isEmpty() ? "" : "_") + ((STrnDoc) aTrnDocs.get(i)).getNumber() + ".pdf");
                            pdf.delete();
                        }
                        client.showMsgBoxInformation("El correo-e ha sido enviado.\n" + msg);
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(STrnUtilities.class.getName(), e);
        }
        finally {
            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Send mail with information of contracts edited after the last send.
     * @param client ERP Client interface.
     */
    public static void sendMailContractInfo(final SClientInterface client) {
        sendMailContractInfo(client, SLibConstants.UNDEFINED, SLibConstants.UNDEFINED, null, null, null);
    }
    
    public static void sendMailDpsSummaryByPeriod(final SClientInterface client, final int[] dpsTypeKey, final Date start, final Date end,  ArrayList<String> recipientsTo, ArrayList<String> recipientsCc, ArrayList<String> recipientsBcc) {
        try {
            String body = "";
            String sql = "SELECT "
                + "CONCAT(sd.num_ser, IF(LENGTH(sd.num_ser) = 0, '', '-'), sd.num) AS f_num, "
                + "bp.bp as bp, "
                + "sde.concept AS f_cpt, "
                + "@contract_qty := sde.orig_qty as orig_qty, "
                + "@order_qty := (SELECT SUM(s.orig_qty) "
                + "FROM trn_dps_dps_supply AS s "
                + "INNER JOIN trn_dps AS td ON s.id_des_year = td.id_year AND s.id_des_doc = td.id_doc AND td.b_del = 0 "
                + "INNER JOIN trn_dps_ety AS tde ON s.id_des_year = tde.id_year AND s.id_des_doc = tde.id_doc AND s.id_des_ety = tde.id_ety AND tde.b_del = 0 "
                + "WHERE s.id_src_year = src.id_src_year AND s.id_src_doc = src.id_src_doc AND s.id_src_ety = src.id_src_ety AND td.dt <= '" + SLibUtils.DbmsDateFormatDate.format(end) + "') AS src_orig_qty, "
                + "(SELECT SUM(d.orig_qty) "
                + "FROM trn_dps_dps_supply AS s "
                + "INNER JOIN trn_dps_dps_supply d ON s.id_des_year = d.id_src_year AND s.id_des_doc = d.id_src_doc AND s.id_des_ety = d.id_src_ety "
                + "INNER JOIN trn_dps AS td ON d.id_des_year = td.id_year AND d.id_des_doc = td.id_doc AND td.b_del = 0 "
                + "INNER JOIN trn_dps_ety AS tde ON d.id_des_year = tde.id_year AND d.id_des_doc = tde.id_doc AND d.id_des_ety = tde.id_ety AND tde.b_del = 0 "
                + "WHERE s.id_src_year = src.id_src_year AND s.id_src_doc = src.id_src_doc AND s.id_src_ety = src.id_src_ety AND td.dt <= '" + SLibUtils.DbmsDateFormatDate.format(end) + "') AS des_orig_qty, "
                + "(@contract_qty - @order_qty) AS balance, "
                + "src.id_src_year, src.id_src_doc, src.id_src_ety "
                + "FROM trn_dps_dps_supply AS src "
                + "INNER JOIN trn_dps AS dd ON src.id_des_year = dd.id_year AND src.id_des_doc = dd.id_doc AND dd.b_del = 0 "
                + "INNER JOIN trn_dps_ety AS dde ON src.id_des_year = dde.id_year AND src.id_des_doc = dde.id_doc AND src.id_des_ety = dde.id_ety AND dde.b_del = 0 "
                + "INNER JOIN trn_dps AS sd ON src.id_src_year = sd.id_year AND src.id_src_doc = sd.id_doc AND sd.b_del = 0 "
                + "INNER JOIN trn_dps_ety AS sde ON src.id_src_year = sde.id_year AND src.id_src_doc = sde.id_doc AND src.id_src_ety = sde.id_ety AND sde.b_del = 0 "
                + "INNER JOIN erp.bpsu_bp bp ON bp.id_bp = sd.fid_bp_r "
                + "WHERE dd.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(start) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(end) + "' AND dd.fid_ct_dps = " + dpsTypeKey[0] + " AND dd.fid_cl_dps = " + dpsTypeKey[1] + " AND dd.fid_tp_dps = " + dpsTypeKey[2] + " "
                + "GROUP BY src.id_src_year, src.id_src_doc, src.id_src_ety;";

            try (ResultSet resultSet = client.getSession().getDatabase().getConnection().createStatement().executeQuery(sql)) {
                while (resultSet.next()) {
                    body += "<tr>";
                    body += "<td style=\"min-width: 8em; max-width: 10em;\"> " + SLibUtils.textToHtml(resultSet.getString("f_num")) + "</td> ";
                    body += "<td style=\"min-width: 16em; max-width: 18em;\"> " + SLibUtils.textToHtml(resultSet.getString("bp")) + "</td> ";
                    body += "<td style=\"min-width: 16em; max-width: 18em;\"> " + SLibUtils.textToHtml(resultSet.getString("f_cpt")) + "</td> ";
                    body += "<td id=\"number\" style=\"min-width: 10em; max-width: 10em;\"> " + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("orig_qty")) + "</td> ";
                    body += "<td id=\"number\" style=\"min-width: 10em; max-width: 10em;\"> " + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("src_orig_qty")) + "</td> ";
                    body += "<td id=\"number\" style=\"min-width: 10em; max-width: 10em;\"> " + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("des_orig_qty")) + "</td> ";
                    body += "<td id=\"number\" style=\"min-width: 10em; max-width: 10em;\"> " + SLibUtils.DecimalFormatValue2D.format(resultSet.getDouble("balance")) + "</td> ";
                    body += "</tr>";
                }
            }
            
            if (!body.isEmpty()) {
                body = computeMailHeader(client.getSessionXXX().getCompany().getCompany())
                        + "<p style = \"font-weight: bold; font-size: 1em;\">Resumen de status de contratos por " + (end.compareTo(start) == 0 ?  SLibUtils.textToHtml("día") + " (" + SLibUtils.DateFormatDate.format(start) + ")</p> " : "rango de fechas (del " + SLibUtils.DateFormatDate.format(start) + " al " + SLibUtils.DateFormatDate.format(end) + ")</p> ")
                        + "<table style = \"width = 980px\">"
                        + "<tr>"
                        + "<th style=\"min-width: 8em; max-width: 10em;\">Id doc.</th> "
                        + "<th style=\"min-width: 16em; max-width: 18em;\">Cliente</th> "
                        + "<th style=\"min-width: 16em; max-width: 18em;\">Producto</th> "
                        + "<th id=\"number\" style=\"min-width: 10em; max-width: 10em;\">Cantidad original</th> "
                        + "<th id=\"number\" style=\"min-width: 10em; max-width: 10em;\">Cantidad procesada por pedidos</th> "
                        + "<th id=\"number\" style=\"min-width: 10em; max-width: 10em;\">Cantidad procesada por contratos</th> "
                        + "<th id=\"number\" style=\"min-width: 10em; max-width: 10em;\">Saldo</th> "
                        + "</tr> "
                        + body 
                        + computeMailFooterEndTable();
                
                sendMail(client, SModSysConsts.CFGS_TP_MMS_CON_SAL, body, "", recipientsTo, recipientsCc, recipientsBcc);
            }
            else {
                throw new Exception("No existe información para el periodo seleccionado.");
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }
    }
    
    /**
     * Send by mail the required sales document.
     * @param client ERP Client interface.
     * @param cfd
     * @param subtypeCfd
     * @param contactType
     * @param bizPartnerId
     * @param bizPartnerBranchId
     * @param catchExceptions
     * @return <code>true</code> when sent, otherwise <code>false</code>.
     * @throws javax.mail.MessagingException
     * @throws java.sql.SQLException
     */
    public static boolean sendMailCfd(final SClientInterface client, final SDataCfd cfd, final int subtypeCfd, final int contactType, final int bizPartnerId, final int bizPartnerBranchId, boolean catchExceptions) throws MessagingException, SQLException, Exception {
        boolean send = false;
        
        try {
            String mails = "";
            SDbMms mms = getMms(client, SModSysConsts.CFGS_TP_MMS_CFD);

            if (mms.getQueryResultId() != SDbConsts.READ_OK) {
                throw new Exception("Se carece de correo-e para envío de documentos.");
            }
            else {
                mails = getMailToSendForCfd(client, bizPartnerId, bizPartnerBranchId, contactType);

                if (mails.isEmpty()) {
                    throw new Exception("El receptor carece de correo-e para recepción de documentos.");
                }
                else {
                    String docNumber = "";
                    String docType = "";
                    boolean isCancelled = false;
                    Date cancelledDate = null;
                    
                    switch (cfd.getFkCfdTypeId()) {
                        case SDataConstantsSys.TRNS_TP_CFD_INV:
                            SDataDps dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                            docNumber = dps.getDpsNumber();
                            isCancelled = dps.getFkDpsStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED;
                            cancelledDate = dps.getUserEditTs();
                            if (SLibUtils.belongsTo(dps.getDpsTypeKey(), new int[][] { SDataConstantsSys.TRNU_TP_DPS_PUR_INV, SDataConstantsSys.TRNU_TP_DPS_SAL_INV })) {
                                docType = "Factura";
                            }
                            else if (SLibUtils.belongsTo(dps.getDpsTypeKey(), new int[][] { SDataConstantsSys.TRNU_TP_DPS_PUR_CN, SDataConstantsSys.TRNU_TP_DPS_SAL_CN })) {
                                docType = "Nota de credito";
                            }
                            break;

                        case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                            docNumber = cfd.getCfdNumber();
                            isCancelled = cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED;
                            cancelledDate = cfd.getUserProcessingTs();
                            docType =  SDataCfdPayment.NAME;
                            break;

                        case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                            switch (subtypeCfd) {
                                case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                                    SDataFormerPayroll payrollFormer = (SDataFormerPayroll) SDataUtilities.readRegistry(client, SDataConstants.HRS_SIE_PAY, new int[] { cfd.getFkPayrollPayrollId_n() }, SLibConstants.EXEC_MODE_SILENT);
                                    SDataFormerPayrollEmp payrollFormerEmp = (SDataFormerPayrollEmp) SDataUtilities.readRegistry(client, SDataConstants.HRS_SIE_PAY_EMP, new int[] { cfd.getFkPayrollPayrollId_n(), cfd.getFkPayrollEmployeeId_n() }, SLibConstants.EXEC_MODE_SILENT);
                                    docNumber = payrollFormer.getYear() + " " + (payrollFormer.getType().compareTo(SHrsFormerConsts.PAY_WEE) == 0 ? SHrsFormerConsts.PAY_WEE_ABB : payrollFormer.getType().compareTo(SHrsFormerConsts.PAY_FOR) == 0 ? SHrsFormerConsts.PAY_FOR_ABB : payrollFormer.getType().compareTo(SHrsFormerConsts.PAY_MON) == 0 ? SHrsFormerConsts.PAY_MON_ABB : "") + " " +
                                        SLibUtils.DecimalFormatCalendarDay.format(payrollFormer.getNumber()) + " " + (payrollFormerEmp.getNumberSeries().length() > 0 ? payrollFormerEmp.getNumberSeries() + "-" : "") + payrollFormerEmp.getNumber();
                                    isCancelled = cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED;
                                    cancelledDate = payrollFormerEmp.getLastDbUpdate();
                                    docType = "Recibo de nomina";
                                    break;

                                case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                                    SDbPayrollReceipt payrollReceipt = new SDbPayrollReceipt();
                                    payrollReceipt.read(client.getSession(), new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n() });
                                    docNumber = SDbPayroll.composePayrollNumber(client.getSession(), cfd.getFkPayrollReceiptPayrollId_n());
                                    docNumber += " " + payrollReceipt.getPayrollReceiptIssueNumber();
                                    isCancelled = payrollReceipt.getChildPayrollReceiptIssue() == null ? cfd.getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED : payrollReceipt.getChildPayrollReceiptIssue().getFkReceiptStatusId() == SDataConstantsSys.TRNS_ST_DPS_ANNULED;
                                    cancelledDate = payrollReceipt.getChildPayrollReceiptIssue() == null ? cfd.getTimestamp() : payrollReceipt.getChildPayrollReceiptIssue().getTsUserUpdate();
                                    docType = "Recibo de nomina";
                                    break;

                                default:
                                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                            }
                            break;
                            
                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }

                    // send mail:
                    
                    String mailSubject = mms.getTextSubject() + " " + docNumber;
                    if (isCancelled) {
                        mailSubject += " (cancelacion)";
                    }

                    String mailBody = "Emisor: " + client.getSessionXXX().getCurrentCompany().getDbmsDataCompany().getBizPartner() + "\n" +
                                "RFC: " + client.getSessionXXX().getCurrentCompany().getDbmsDataCompany().getFiscalId() + "\n" +
                                "Comprobante: " + docType + "\n" +
                                "Folio: " + docNumber + "\n" +
                                "Emision: " + SLibUtils.DateFormatDate.format(cfd.getTimestamp()) + "\n";

                    if (isCancelled) {
                        mailBody += "Cancelacion: " + SLibUtils.DateFormatDate.format(cancelledDate) + "\n";
                    }
                    else {
                        mailBody += "\n" + mms.getTextBody();
                    }

                    SMailSender sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());
                    SMail mail = new SMail(sender, mailSubject, SLibUtils.textToHtml(mailBody), new ArrayList<>(Arrays.asList(SLibUtils.textExplode(mails, ";"))));

                    if (!isCancelled) {
                        File pdfFile = new File(client.getSessionXXX().getParamsCompany().getXmlBaseDirectory() + cfd.getDocXmlName().replaceAll(".xml", ".pdf"));
                        if (!pdfFile.exists()) {
                            throw new Exception("El archivo PDF no existe.");
                        }

                        File xmlFile = new File(client.getSessionXXX().getParamsCompany().getXmlBaseDirectory() + cfd.getDocXmlName());
                        if (!xmlFile.exists()) {
                            throw new Exception("El archivo XML no existe.");
                        }

                        mail.getAttachments().add(xmlFile);
                        mail.getAttachments().add(pdfFile);
                    }

                    mail.send();
                    send = true;
                    SCfdUtils.insertCfdSendLog(client, cfd, mails, false);
                }
            }
        }
        catch (Exception e) {
            if (!catchExceptions) {
                throw e;
            }
        }
          
        return send;
    }
    
    public static void sendDocumentSoriana(final SClientInterface client, final int[] dpsKey) throws Exception {
        Cursor cursor = client.getFrame().getCursor();
        SDataDps dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, dpsKey, SLibConstants.EXEC_MODE_SILENT);
        SDataCfd cfd = dps.getDbmsDataCfd();
        
        int statusId = 0;
        String status = "";

        sor.utils.SSorianaUtils sorianaUtils;
        sorianaUtils = new sor.utils.SSorianaUtils();
        sorianaUtils.sendDocumentWs(SCfdUtils.removeNode(dps.getDbmsDataCfd().getDocXml(), "myadd:Addenda1"));

        if (sorianaUtils.getAcknowledgmentStatus().compareTo(SModSysConsts.TXT_ST_XML_DVY_REJECT) == 0) {
            statusId = SModSysConsts.TRNS_ST_XML_DVY_REJECT;
            status = "RECHAZADO:\n" + "- " + sorianaUtils.getAcknowledgmentMsg() + ".";
        }
        else if (sorianaUtils.getAcknowledgmentStatus().compareTo(SModSysConsts.TXT_ST_XML_DVY_APPROVED) == 0) {
            statusId = SModSysConsts.TRNS_ST_XML_DVY_APPROVED;
            status = "ACEPTADO.";
        }
        else {
            statusId = SModSysConsts.TRNS_ST_XML_DVY_PENDING;
            status = "PENDIENTE.";
        }
        
        cfd.saveField(client.getSession().getStatement().getConnection(), SDataCfd.FIELD_DVY_ACK, sorianaUtils.getAcknowledgment().replaceAll("'", "\""));
        cfd.saveField(client.getSession().getStatement().getConnection(), SDataCfd.FIELD_DVY_MSG, sorianaUtils.getAcknowledgmentMsg().replaceAll("'", "\""));

        /* 2018-01-26 (Sergio Flores): "Paralelo" web service is supposed not to be active anymore.
        soriana.utils.SSorianaUtils sorianaUtilsParalelo;
        sorianaUtilsParalelo = new soriana.utils.SSorianaUtils();
        sorianaUtilsParalelo.sendDocumentWs(SCfdUtils.removeNode(dps.getDbmsDataCfd().getDocXml(), "myadd:Addenda1"));

        if (sorianaUtilsParalelo.getAcknowledgmentStatus().compareTo(SModSysConsts.TXT_ST_XML_DVY_REJECT) == 0) {
            statusId = SModSysConsts.TRNS_ST_XML_DVY_REJECT;
            status = "RECHAZADO:\n" + "- " + sorianaUtilsParalelo.getAcknowledgmentMsg() + ".";
        }
        else if (sorianaUtilsParalelo.getAcknowledgmentStatus().compareTo(SModSysConsts.TXT_ST_XML_DVY_APPROVED) == 0) {
            statusId = SModSysConsts.TRNS_ST_XML_DVY_APPROVED;
            status = "ACEPTADO.";
        }
        else {
            statusId = SModSysConsts.TRNS_ST_XML_DVY_PENDING;
            status = "PENDIENTE.";
        }

        cfd.saveField(client.getSession().getStatement().getConnection(), SDataCfd.FIELD_ACK_DVY, sorianaUtilsParalelo.getAcknowledgment().replaceAll("'", "\""));
        cfd.saveField(client.getSession().getStatement().getConnection(), SDataCfd.FIELD_MSJ_DVY, sorianaUtilsParalelo.getAcknowledgmentMsg().replaceAll("'", "\""));
        */
        
        cfd.saveField(client.getSession().getStatement().getConnection(), SDataCfd.FIELD_DVY_TP, SModSysConsts.TRNS_TP_XML_DVY_WS_SOR);
        cfd.saveField(client.getSession().getStatement().getConnection(), SDataCfd.FIELD_DVY_ST, statusId);
        cfd.saveField(client.getSession().getStatement().getConnection(), SDataCfd.FIELD_DVY_USR, client.getSession().getUser().getPkUserId());

        client.getFrame().setCursor(cursor);
        client.showMsgBoxInformation("El documento '" + dps.getNumberSeries() + (dps.getNumberSeries().length() > 0 ? "-" : "") + dps.getNumber() + (statusId == SModSysConsts.TRNS_ST_XML_DVY_PENDING ? "' sigue " : "' fue ") + status);
    }

    /**
     * Obtain configuration of Mail Messaging Service the type specific.
     * @param client ERP Client interface.
     * @param typeMms Type of Mail Messaging Service.
     * @return Configuration registry.
     */
    public static SDbMms getMms(final SClientInterface client, final int typeMms) {
        String sql = "";
        ResultSet resultSet = null;
        SDbMms mms = null;

        try {
            mms = new SDbMms();

            sql = "SELECT id_mms FROM cfg_mms WHERE fk_tp_mms = " + typeMms + " and b_del = 0 ORDER BY id_mms DESC ";

            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                mms.read(client.getSession(), new int[] { resultSet.getInt("id_mms") });
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(STrnUtilities.class.getName(), e);
        }

        return mms;
    }

    public static boolean insertDpsSendLog(final SClientInterface client, final SDataDps dps, final String sendTo, final boolean isSend) throws Exception {
        String sql = "";
        int id_snd = 0;
        ResultSet resultSet = null;

        sql = "SELECT COALESCE(MAX(id_snd), 0) + 1 AS f_snd FROM trn_dps_snd_log WHERE id_year = " + dps.getPkYearId()+ " AND id_doc = " + dps.getPkDocId()+ " ";

        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id_snd = resultSet.getInt("f_snd");
        }

        sql = "INSERT INTO trn_dps_snd_log VALUES(" + dps.getPkYearId() + ", " + dps.getPkDocId() + ", " +
                id_snd + ", '" + SLibUtils.DbmsDateFormatDate.format(client.getSession().getCurrentDate()) + "', '" + sendTo + "', " + isSend + ", " + client.getSession().getUser().getPkUserId() + ", NOW())";

        client.getSession().getStatement().execute(sql);

        return true;
    }

    /**
     * Create report with contract moves.
     * @param client ERP Client interface.
     * @param keyDoc Document key.
     */
    public static void createReportContractAnalysis(final SClientInterface client, int[] keyDoc) {
        createReportContractAnalysis(client, null, keyDoc);
    }

    /**
     * Create report with contract moves.
     * @param client ERP Client interface.
     * @param trnDoc STrnDoc object.
     * @param keyDoc Document key.
     */
    public static void createReportContractAnalysis(final SClientInterface client, STrnDoc trnDoc, int[] keyDoc) {
        Cursor cursor = null;
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        FileOutputStream outputStreamPdf = null;

        try {
            cursor = client.getFrame().getCursor();
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            map = client.createReportParams();

            map.put("nIdYear", trnDoc != null ? ((int[]) trnDoc.getPkDoc())[0] : keyDoc[0]);
            map.put("nIdDoc", trnDoc != null ? ((int[]) trnDoc.getPkDoc())[1] : keyDoc[1]);
            map.put("nIdCatBp", SDataConstantsSys.BPSS_CT_BP_CUS);

            jasperPrint = SDataUtilities.fillReport(client, SDataConstantsSys.REP_TRN_CON_MOV, map);

            if (trnDoc == null) {
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Movimientos de contrato");
                jasperViewer.setVisible(true);
            }
            else {
                outputStreamPdf = new FileOutputStream("CON_" + trnDoc.getNumberSeries() +
                (trnDoc.getNumberSeries().isEmpty() ? "" : "_") + trnDoc.getNumber() + ".pdf");

                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStreamPdf);

                outputStreamPdf.close();
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }
        finally {
            client.getFrame().setCursor(cursor);
        }
    }
    
    public static void createReportOrder(final SClientInterface client, final File file, final SDataDps dps, final int pnPrintMode) {
        Cursor cursor = null;
        String sUserBuyer = "";
        String sUserAuthorize = "";
        int nFkEmiAddressFormatTypeId_n = 0;
        int nFkRecAddressFormatTypeId_n = 0;
        boolean bincludeCountry = false;
        String[] addressOficial = null;
        String[] addressDelivery = null;
        String[] addressDeliveryCompany = null;
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        SDataBizPartnerBranch oCompanyBranch = null;
        SDataBizPartnerBranch oBizPartnerBranch = null;
        SDataBizPartnerBranchAddress oAddress = null;
        SDataUser oUserBuyer = null;
        SDataUser oUserAuthorize = null;
        SDataBizPartner bizPartnerUserBuyer = null;
        SDataBizPartner bizPartnerUserAuthorize = null;
        SDataEmployee employeeUserBuyer = null;
        SDataEmployee employeeUserAuthorize = null;
        boolean isPurchase = false;
        boolean isPending = false;
        boolean isReject = false;
        FileOutputStream outputStreamPdf = null;
        
        try {
            cursor = client.getFrame().getCursor();
            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            map = client.createReportParams();
            
            isPurchase = dps.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_PUR;
            isPending = dps.getFkDpsAuthorizationStatusId() == SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING;
            isReject = dps.getFkDpsAuthorizationStatusId() == SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT;
            
            oCompanyBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BPB, new int[] { dps.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
            oBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BPB, new int[] { dps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);
            oAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BPB_ADD, new int [] { dps.getFkBizPartnerBranchId(),
                dps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);
            
            if (oBizPartnerBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                nFkRecAddressFormatTypeId_n = oBizPartnerBranch.getFkAddressFormatTypeId_n();
            }
            else {
                nFkRecAddressFormatTypeId_n = client.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
            }

            if (oCompanyBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                nFkEmiAddressFormatTypeId_n = oAddress.getFkAddressTypeId();
            }
            else {                
                nFkEmiAddressFormatTypeId_n = client.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
            }
            
            addressOficial = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkRecAddressFormatTypeId_n,
                SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
            addressDelivery = oAddress.obtainAddress(nFkRecAddressFormatTypeId_n,
                SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
            addressDeliveryCompany = oCompanyBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkEmiAddressFormatTypeId_n,
                SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
            oUserBuyer = (SDataUser) SDataUtilities.readRegistry(client, SDataConstants.USRU_USR, new int[] { dps.getFkUserNewId() }, SLibConstants.EXEC_MODE_SILENT);
            oUserAuthorize = (SDataUser) SDataUtilities.readRegistry(client, SDataConstants.USRU_USR, new int[] { dps.getFkUserAuthorizedId() }, SLibConstants.EXEC_MODE_SILENT);

            sUserBuyer = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.USRU_USR, new int[] { dps.getFkUserNewId() });
            sUserAuthorize = SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.USRU_USR, new int[] { dps.getFkUserAuthorizedId() });
            
            if (oUserBuyer.getFkBizPartnerId_n() != SLibConstants.UNDEFINED) {
                bizPartnerUserBuyer = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[] { oUserBuyer.getFkBizPartnerId_n() }, SLibConstants.EXEC_MODE_SILENT); 
                employeeUserBuyer = bizPartnerUserBuyer.getDbmsDataEmployee();
            }
                        
            if (oUserAuthorize.getFkBizPartnerId_n() != SLibConstants.UNDEFINED) {
                bizPartnerUserAuthorize = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[] { oUserAuthorize.getFkBizPartnerId_n() }, SLibConstants.EXEC_MODE_SILENT); 
                employeeUserAuthorize = bizPartnerUserAuthorize.getDbmsDataEmployee();
            }
            
            if (isPurchase) {
                map.put("oUserBuyer", employeeUserBuyer);
                map.put("oUserAuthorize", employeeUserAuthorize);
            }
            
            map.put("nIdYear", dps.getPkYearId());
            map.put("nIdDoc", dps.getPkDocId());
            map.put("sTitle", SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.TRNU_TP_DPS, new int[] { dps.getFkDpsCategoryId(),
            dps.getFkDpsClassId(), dps.getFkDpsTypeId() }));
            map.put("bIsSupplier", isPurchase);
            map.put("sAddressLine1", addressOficial[0]);
            map.put("sAddressLine2", addressOficial[1]);
            map.put("sAddressLine3", addressOficial[2]);
            map.put("sAddressLine4", addressOficial[3]);
            map.put("sAddressDelivery1", isPurchase ? addressDeliveryCompany[0] : addressDelivery[0]);
            map.put("sAddressDelivery2", isPurchase ? addressDeliveryCompany[1] : addressDelivery[1]);
            map.put("sAddressDelivery3", isPurchase ? addressDeliveryCompany[2] : addressDelivery[2]);
            map.put("sAddressDelivery4", isPurchase ? addressDeliveryCompany.length > 3 ? addressDeliveryCompany[3] : "" :
                addressDelivery.length > 3 ? addressDelivery[3] : "");
            map.put("sUserBuyer", sUserBuyer != null ? sUserBuyer : oUserBuyer.getUser());
            map.put("sUserAuthorize", sUserAuthorize != null ? sUserAuthorize : oUserAuthorize.getUser());
            map.put("bIsPending", isPending);
            map.put("bIsReject", isReject);
            map.put("nBizPartnerCategory", isPurchase ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
            map.put("nIdTpCarSup", SModSysConsts.LOGS_TP_CAR_CAR);
            map.put("sNotes", client.getSessionXXX().getParamsCompany().getNotesPurchasesOrder());

            jasperPrint = SDataUtilities.fillReport(client, SDataConstantsSys.REP_TRN_DPS_ORDER, map);
            
            switch (pnPrintMode) {
                case SDataConstantsPrint.PRINT_MODE_VIEWER:
                    jasperViewer = new JasperViewer(jasperPrint, false);
                    jasperViewer.setTitle("Impresión de pedido");
                    jasperViewer.setVisible(true);
                    break;
                case SDataConstantsPrint.PRINT_MODE_PDF_FILE:
                    outputStreamPdf = new FileOutputStream(file);

                    JasperExportManager.exportReportToPdfStream(jasperPrint, outputStreamPdf);

                    outputStreamPdf.close();
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }
        finally {
            client.getFrame().setCursor(cursor);
        }
    }

    /**
     * Obtain keys the contracts to send, specified or edited after the last send.
     * @param client ERP Client interface.
     * @param numberStart Number contract initial.
     * @param numberEnd Number contract final.
     * @return Keys the contracts.
     */
    public static ArrayList<STrnDoc> obtainContractsToSend(final SClientInterface client, int numberStart, int numberEnd) {
        String sql = "";
        ResultSet resulSet = null;
        ArrayList<STrnDoc> aTrnDocs = null;
        STrnDoc trnDoc = null;

        try {
            aTrnDocs = new ArrayList<STrnDoc>();

            // Contracts edited:

            sql = "SELECT id_year AS f_year, id_doc AS f_doc, num_ser AS f_ser, num AS f_num "
                    + "FROM trn_dps AS d "
                    + "WHERE d.b_del = 0 AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[1] + " "
                    + "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] + " ";
            if (numberStart != SLibConstants.UNDEFINED && numberEnd != SLibConstants.UNDEFINED) {
                sql += "AND num >= " + numberStart + " AND num <= " + numberEnd + " ";
            }
            else {
                sql += " AND d.ts_edit > (SELECT MAX(ts_usr) FROM trn_mms_log) "
                        + "UNION "
                        + "SELECT s.id_src_year AS f_year, s.id_src_doc AS f_doc, ds.num_ser AS f_ser, ds.num AS f_num "
                        + "FROM trn_dps AS d "
                        + "INNER JOIN trn_dps_dps_supply AS s ON s.id_des_year = d.id_year AND s.id_des_doc = d.id_doc "
                        + "INNER JOIN trn_dps AS ds ON s.id_src_year = ds.id_year AND s.id_src_doc = ds.id_doc "
                        + "WHERE d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1] + " "
                        + "AND d.b_del = 0 AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2] + " "
                        + "AND d.ts_edit > (SELECT MAX(ts_usr) FROM trn_mms_log) ";
            }

            resulSet = client.getSession().getStatement().executeQuery(sql);
            while (resulSet.next()) {
                trnDoc = new STrnDoc();

                trnDoc.setPkDoc(new int[] { resulSet.getInt("f_year"), resulSet.getInt("f_doc") });
                trnDoc.setNumberSeries(resulSet.getString("f_ser"));
                trnDoc.setNumber(resulSet.getString("f_num"));

                aTrnDocs.add(trnDoc);
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }

        return aTrnDocs;
    }

    /**
     * Get all system notes, except deleted ones, for given type of DPS and currency.
     * @param client GUI client.
     * @param dpsTypeKey Key of DPS type.
     * @param currencyId Currency.
     * @return Array of requested system notes.
     * @throws Exception 
     */
    public static ArrayList<SDataSystemNotes> getSystemNotes(final SClientInterface client, final int[] dpsTypeKey, final int currencyId) throws Exception {
        ArrayList<SDataSystemNotes> systemNotesArray = new ArrayList<>();

        String sql = "SELECT id_nts "
                + "FROM trn_sys_nts "
                + "WHERE NOT b_del AND "
                + "fid_ct_dps = " + dpsTypeKey[0] + " AND fid_cl_dps = " + dpsTypeKey[1] + " AND fid_tp_dps = " + dpsTypeKey[2] + " AND "
                + "fid_cur = " + currencyId + ";";

        try (Statement statement = client.getSession().getStatement().getConnection().createStatement()) {
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                SDataSystemNotes systemNotes = (SDataSystemNotes) SDataUtilities.readRegistry(client, SDataConstants.TRN_SYS_NTS, new int[] { resulSet.getInt(1) }, SLibConstants.EXEC_MODE_SILENT);
                systemNotesArray.add(systemNotes);
            }
        }

        return systemNotesArray;
    }

    /**
     * Create DPS notes from system notes, all or automatic or non-automatic only, except deleted ones, for given type of DPS and currency.
     * @param client GUI client.
     * @param dpsTypeKey Key of DPS type.
     * @param currencyId Currency.
     * @param automatic When <code>true</code> only automatic notes are retrieved. When <code>false</code> only non-automatic notes are retrieved. When <code>null</code> all notes are retrieved.
     * @return Array of DPS notes.
     * @throws Exception 
     */
    public static ArrayList<SDataDpsNotes> createDpsNotesFromSystemNotes(final SClientInterface client, final int[] dpsTypeKey, final int currencyId, final Boolean automatic) throws Exception {
        ArrayList<SDataSystemNotes> systemNotesArray = getSystemNotes(client, dpsTypeKey, currencyId);
        ArrayList<SDataDpsNotes> dpsNotesArray = new ArrayList<>();

        for (SDataSystemNotes systemNotes : systemNotesArray) {
            if (automatic == null || automatic && systemNotes.getIsAutomatic() || !automatic && !systemNotes.getIsAutomatic()) {
                SDataDpsNotes dpsNotes = new SDataDpsNotes();
                
                //dpsNotes.setPkYearId(...);
                //dpsNotes.setPkDocId(...);
                //dpsNotes.setPkNotesId(...);
                dpsNotes.setNotes(systemNotes.getNotes());
                dpsNotes.setCfdComplementDisposition(systemNotes.getCfdComplementDisposition());
                dpsNotes.setCfdComplementRule(systemNotes.getCfdComplementRule());
                dpsNotes.setIsAllDocs(true);
                dpsNotes.setIsPrintable(systemNotes.getIsPrintable());
                dpsNotes.setIsCfdComplement(systemNotes.getIsCfdComplement());
                //dpsNotes.setIsDeleted(...);
                dpsNotes.setFkUserNewId(client.getSessionXXX().getUser().getPkUserId());
                dpsNotes.setFkUserEditId(SUtilConsts.USR_NA_ID);
                dpsNotes.setFkUserDeleteId(SUtilConsts.USR_NA_ID);
                //dpsNotes.setUserNewTs(...);
                //dpsNotes.setUserEditTs(...);
                //dpsNotes.setUserDeleteTs(...);
                
                dpsNotesArray.add(dpsNotes);
            }
        }

        return dpsNotesArray;
    }
    
    public static ArrayList<SDataDpsNotesRow> createDpsNotesRowsFromDpsNotes(final ArrayList<SDataDpsNotes> dpsNotesArray) throws Exception {
        ArrayList<SDataDpsNotesRow> dpsNotesRows = new ArrayList<>();
        
        for (SDataDpsNotes dpsNotes : dpsNotesArray) {
            SDataDpsNotesRow dpsNotesRow = new SDataDpsNotesRow(dpsNotes);
            dpsNotesRows.add(dpsNotesRow);
        }
        
        return dpsNotesRows;
    }

     /**
     * Gets link between order and an invoice.
     * @param statement Database statement.
     * @param desYearId Invoice year ID.
     * @param desDocId Invoice document ID.
     * @param desEntryId Invoice entry ID.
     * @return Array with link key (0: order year ID, 1: order document ID, 2: order entry ID).
     */
    public static int[] getSourceLink(final Statement statement, final int desYearId, final int desDocId, final int desEntryId) throws SQLException {
        int[] sourceLinkKey = null;
        
        String sql = "SELECT id_src_year, id_src_doc, id_src_ety "
                    + "FROM trn_dps_dps_supply "
                    + "WHERE id_des_year = " + desYearId +" "
                    + "AND id_des_doc = " + desDocId + " "
                    + "AND id_des_ety = " + desEntryId;
            
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            sourceLinkKey = new int[] { resultSet.getInt("id_src_year"), resultSet.getInt("id_src_doc"), resultSet.getInt("id_src_ety") };
        }
        
        return sourceLinkKey;
    }
    
    /**
     * Gets quantity to return in an invoice credit note.
     * @param statement Database statement.
     * @param dpsYear Invoice year ID.
     * @param dpsDoc Invoice document ID.
     * @param dpsEntry Invoice entry ID.
     * @return Array with returned quantities in an invoice credit note (0: quantity, 1: original quantity).
     */
    public static double[] getQuantitiesReturned(final Statement statement, final int dpsYear, final int dpsDoc, final int dpsEntry) throws SQLException {
        double returned[] = null; 
        
            String sql = "SELECT COALESCE(SUM(qty), 0.0) AS totQty, "
                    + "COALESCE(SUM(orig_qty), 0.0) AS totOrigQty "
                    + "FROM trn_dps_dps_adj" + " "
                    + "WHERE id_dps_year = " + dpsYear + " "
                    + "AND id_dps_doc = " + dpsDoc + " "
                    + "AND id_dps_ety = " + dpsEntry;
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            if (resultSet.next()) {
                returned = new double[] { resultSet.getDouble("totQty"), resultSet.getDouble("totOrigQty") };
            }
        
        return returned;
    }
    
    /**
     * Gets quantity of supplied entries in an invoice.
     * @param statement Database statement.
     * @param dpsYear Invoice year ID.
     * @param dpsDoc Invoice document ID.
     * @param dpsEntry Invoice entry ID.
     * @return Array with quantities of supplied entries in an invoice (0: quantity, 1: original quantity).
     */
    public static double[] getQuantitiesOfSuppliedEntries (final Statement statement, final int dpsYear, final int dpsDoc, final int dpsEntry) throws SQLException {
        double totalSupplied[] = null;

            String sql = "SELECT SUM(qty) AS totQty, SUM(orig_qty) AS totOrigQty "
                        + "FROM trn_dps_dps_supply "
                        + "WHERE id_des_year = " + dpsYear + " "
                        + "AND id_des_doc = " + dpsDoc + " "
                        + "AND id_des_ety = " + dpsEntry;
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            if (resultSet.next()) {
                totalSupplied = new double[] { resultSet.getDouble("totQty"), resultSet.getDouble("totOrigQty") };
            }
        
        return totalSupplied;
    }
    
    public static void deleteLink(final SClientInterface client, final int srcYearId, final int srcDocId, final int desYearId, final int desDocId) {
        String sql = "";

        try {
            sql = "DELETE "
                    + "FROM trn_dps_dps_supply "
                    + "WHERE id_src_year = " + srcYearId + " AND id_src_doc = " + srcDocId + " AND "
                    + "id_des_year = " + desYearId + " AND id_des_doc = " + desDocId + " ";

            if (client.getSession().getStatement().execute(sql)) {
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }
    }

    /**
     * Check if business partner has expired documents.
     * @param client ERP client interface.
     * @param idBizPartner Business partner ID.
     * @param idBizPartnerCategory Category of bizPartner
     * @param cutoffDate Cutoff date.
     * @param idDocYear Primary key of document to exclude from query.
     * @param idDocDoc Primary key of document to exclude from query.
     * @return <code>true</code> if business partner has expired documents.
     */
    public static boolean hasBizPartnerExpiredDocs(final SClientInterface client, 
            final int idBizPartner, final int idBizPartnerCategory, final Date cutoffDate, int idDocYear, final int idDocDoc) {
        int year = SLibTimeUtils.digestYear(cutoffDate)[0];
        boolean hasExpiredDocuments = false;
        String sSql = "";
        ResultSet resulSet = null;

        sSql = "SELECT d.id_year, d.id_doc, " +
            "IF(DATEDIFF('" + SLibUtils.DbmsDateFormatDate.format(cutoffDate) + "', " +
                "DATE_ADD(d.dt_start_cred, INTERVAL(d.days_cred + IF(ct.b_cred_usr, ct.days_grace, btp.days_grace)) DAY)) > 0, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " AND re.fid_tp_sys_mov_xxx = " +
            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS f_def " +
            "FROM fin_rec AS r " +
            "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND " +
            "r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = FALSE AND re.b_del = FALSE AND r.id_year = " + year + " AND " +
            "r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(cutoffDate) + "' AND re.fid_ct_sys_mov_xxx = " +
            (idBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0]) + " " +
            " AND re.fid_tp_sys_mov_xxx = " +
            (idBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]) + " " +
            "INNER JOIN fin_acc AS ac ON LEFT(re.fid_acc, INSTR(re.fid_acc, '-')) = LEFT(ac.id_acc, INSTR(ac.id_acc, '-')) AND ac.lev = 1 " +
            "INNER JOIN erp.bpsu_bp AS bp ON re.fid_bp_nr = bp.id_bp " +
            "INNER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + idBizPartnerCategory + " " +
            "INNER JOIN erp.bpsu_tp_bp AS btp ON ct.fid_tp_bp = btp.id_tp_bp AND btp.id_ct_bp = " + idBizPartnerCategory + " " +
            "INNER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.fid_cob = 1 " +
            "WHERE re.fid_bp_nr = " + idBizPartner + " AND NOT (re.fid_dps_year_n = " + idDocYear + " AND re.fid_dps_doc_n = " + idDocDoc + ") " +
            "GROUP BY d.id_year, d.id_doc, d.fid_bpb " +
            "HAVING f_def <> 0 " +
            "ORDER BY d.id_year, d.id_doc, d.fid_bpb ";

        try {
            resulSet = client.getSession().getStatement().executeQuery(sSql);
            if (resulSet.next()) {
                hasExpiredDocuments = true;
            }
        }
        catch (SQLException e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }

        return hasExpiredDocuments;
    }

    /**
     * Obtain activation code for the stamps acquisition, from date and quantity specified:
     * @param dateAcquisition date of acquisition.
     * @param stampQty stamps quantity acquired.
     * @return String activation code.
     */
    public static String getCodeActivationStamp(final int pacId, final Date dateAcquisition, final int stampQty) {
        return String.valueOf(dateAcquisition.getTime() + stampQty + pacId);
    }

    /**
     * Validate if exists the registry of acquisition of stamp for date specified:
     * @param client ERP Client interface.
     * @param nPacId pac ID.
     * @param dateAcquisition date of acquisition.
     * @return true fi exists.
     * @throws Exception
     */
    public static boolean validateExistsAcquisitionStamp(final SClientInterface client, final int nPacId,  final Date dateAcquisition) {
        String sSql = "";
        ResultSet oResultSet = null;
        boolean exists = false;

        try {
            sSql = "SELECT COUNT(*) AS f_count FROM trn_sign WHERE id_year = YEAR('" + SLibUtils.DbmsDateFormatDate.format(dateAcquisition) + "') AND " +
                    "id_pac = " + nPacId + " AND dt = '" + SLibUtils.DbmsDateFormatDate.format(dateAcquisition) + "' AND " +
                    "fid_ct_sign = " + SDataConstantsSys.TRNS_TP_SIGN_IN_ACQUIRED[0] + " AND fid_tp_sign = " + SDataConstantsSys.TRNS_TP_SIGN_IN_ACQUIRED[1] + " ";

            oResultSet = client.getSession().getStatement().executeQuery(sSql);
            if (oResultSet.next()) {
                if (oResultSet.getInt("f_count") > 0) {
                    exists = true;
                }
            }
        }
        catch (SQLException e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }

        return exists;
    }

    /**
    * Obtain last price used from the DPS entry specified:
    * @param client ERP Client interface.
    * @param dpsSourceKey DPS source from need to get the last dps price used.
    * @return int array (last dps price key used).
    */
    public static int [] getLastDpsEntryPriceKey(final SClientInterface client,  final Object dpsSourceKey) {
        String sql = "";
        int key [] = null;
        ResultSet resultSet = null;

        try {  
            sql = "SELECT dep.id_year, dep.id_doc, dep.id_ety, dep.id_prc, dep.con_prc_year, dep.con_prc_mon, " +
                    "@orig_qty := dep.orig_qty AS orig_qty, " +
                    "@orig_qty_sup := COALESCE(( SELECT SUM(ddp.orig_qty) FROM trn_dps_dps_supply AS ddp INNER JOIN trn_dps AS dx ON dx.id_year = ddp.id_des_year AND dx.id_doc = ddp.id_des_doc AND dx.b_del = 0 INNER JOIN trn_dps_ety AS dex  ON dex.id_year = ddp.id_des_year AND dex.id_doc = ddp.id_des_doc AND dex.id_ety = ddp.id_des_ety AND dex.b_del = 0 WHERE ddp.id_src_year = de.id_year AND ddp.id_src_doc = de.id_doc AND ddp.id_src_ety = de.id_ety AND dex.con_prc_year = dep.con_prc_year AND dex.con_prc_mon = dep.con_prc_mon), 0) AS orig_qty_sup, " +
                    "@orig_qty - @orig_qty_sup AS rem " +
                    "FROM trn_dps AS d " +
                    "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND d.b_del = 0 AND de.b_del = 0 " +
                    "INNER JOIN trn_dps_ety_prc AS dep ON de.id_year = dep.id_year AND de.id_doc = dep.id_doc AND de.id_ety = dep.id_ety AND dep.b_del = 0 " +
                    "WHERE de.id_year = " + ((int[]) dpsSourceKey)[0] + " AND de.id_doc = " + ((int[]) dpsSourceKey)[1] + " AND de.id_ety = " + ((int[]) dpsSourceKey)[2] + " " +
                    "HAVING orig_qty_sup < orig_qty " +
                    "ORDER BY dep.con_prc_year, dep.con_prc_mon " +
                    "LIMIT 1; ";

            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                key = new int [4];
                key[0] = resultSet.getInt("dep.id_year");
                key[1] = resultSet.getInt("dep.id_doc");
                key[2] = resultSet.getInt("dep.id_ety");
                key[3] = resultSet.getInt("dep.id_prc");
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }
        return key;
    }
    
    public static double getQuantityProcessForDpsEntryPrice(final SClientInterface client, final int[] entryPriceKey, final int[] entryDpsExclude) {
        String sql = "";
        double qtyProc = 0;
        ResultSet resultSet = null;

        try {
            sql = "SELECT COALESCE(SUM(de.orig_qty * ("
                    + "IF((d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2] + ") OR "
                    + "(d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] + ") "
                    + ", -1.0, 1.0))), 0) AS orig_qty_proc "
                    + "FROM trn_dps_ety_prc AS dep "
                    + "LEFT OUTER JOIN trn_dps_dps_supply AS dps_sup ON dps_sup.id_src_year = dep.id_year AND dps_sup.id_src_doc = dep.id_doc AND dps_sup.id_src_ety = dep.id_ety "
                    + "LEFT OUTER JOIN trn_dps AS d ON d.id_year = dps_sup.id_des_year AND d.id_doc = dps_sup.id_des_doc "
                    + "LEFT OUTER JOIN trn_dps_ety AS de ON de.id_year = d.id_year AND de.id_doc = d.id_doc AND de.id_ety = dps_sup.id_des_ety AND dep.con_prc_year = de.con_prc_year AND dep.con_prc_mon = de.con_prc_mon ";
                    if (entryDpsExclude != null) {
                        if (entryDpsExclude[1] != SLibConstants.UNDEFINED) {
                            sql += " AND NOT (de.id_year = " + entryDpsExclude[0] + " AND de.id_doc = " + entryDpsExclude[1] + ") ";
                        }
                    }
                    sql += "WHERE dep.id_year = " + entryPriceKey[0] + " AND dep.id_doc = " + entryPriceKey[1] + " AND dep.id_ety = " + entryPriceKey[2] + " and dep.id_prc = " + entryPriceKey[3] + " "
                            + "GROUP BY dep.id_year, dep.id_doc, dep.id_ety, dep.id_prc "
                            + "ORDER BY dep.id_year, dep.id_doc, dep.id_ety, dep.id_prc; ";
           
            resultSet = client.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {                
               qtyProc = resultSet.getDouble("orig_qty_proc");                
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }
        return qtyProc;
    }
    
    /**
     * Obtain the firts document the order type lincked.
     * @param client Interface Client current.
     * @param dps Document to obtain link.
     * @return SDataDps order.
     */
    public static SDataDps getFirtsLinkOrderType(final SClientInterface client, final SDataDps dps) {
        SDataDps dpsOrder = null;
    
        for (SDataDpsEntry entryDocumento : dps.getDbmsDpsEntries()) {
            if (entryDocumento.isAccountable()) {
                for (SDataDpsDpsLink linkPedido : entryDocumento.getDbmsDpsLinksAsDestiny()) {
                    if (!linkPedido.getDbmsIsSourceDeleted() && !linkPedido.getDbmsIsSourceEntryDeleted()) {
                        dpsOrder = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, linkPedido.getDbmsSourceDpsKey(), SLibConstants.EXEC_MODE_VERBOSE);

                        break;  // a "pedido" was found
                    }
                }
            }
        }
        return dpsOrder;
    }
    
    /**
    * Calculate price using base, future and factor for DPS entry:
    * @param contractBase for calculate price
    * @param contractFuture for calculate price
    * @param conversionFactor equivalence for  kg/lb for calculate price
    * @param contractFactor for calculate price
    * @param conversionOriginalQuantity to convert the price to USD / UNIT of document
    * @return calculated price in the CUR/UN of document.
    */
    public static double calculateDpsEntryPriceUnitary (final double contractBase, final double contractFuture, final double conversionFactor,  final double contractFactor, final double conversionOriginalQuantity) {
        double result = 0;
        result = (contractBase + contractFuture) * conversionFactor * contractFactor; //Result expresed in TON by default
        result = result / conversionOriginalQuantity; //Convert result to USD /KG to USD / UN
        return result;
    }
    
    public static int[] readMmsConfigurationByLinkType(final SClientInterface client, final int mmsType, final int itemId) throws SQLException {
        String sql = "";
        int[] configKey = null;
        ArrayList<int[]> linkTypes = new ArrayList<>();
        HashMap<Integer, int[]> mapConfigs = new HashMap<>();
        Statement statement = client.getSession().getDatabase().getConnection().createStatement();
        Statement statementCfg = client.getSession().getDatabase().getConnection().createStatement();
        ResultSet resultSet = null;
        ResultSet resultSetCfg = null;

        // Read configuration for item:

        sql = "SELECT tmc.fk_tp_link, tmc.fk_ref " 
                + "FROM trn_mms_cfg AS tmc " 
                + "INNER JOIN erp.cfgs_tp_mms AS tm ON tm.id_tp_mms = tmc.id_tp_mms " 
                + "INNER JOIN cfg_mms AS cm ON cm.fk_tp_mms = tm.id_tp_mms " 
                + "WHERE tmc.b_del = 0 AND tm.id_tp_mms = " + mmsType + " " 
                + "ORDER BY tmc.fk_tp_link ASC; ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            linkTypes.add(new int[] { resultSet.getInt(1), resultSet.getInt(2) });
        }

        // Read item by link type:

        for (int[] linkType : linkTypes) {
            sql = "";
            
            switch(linkType[0]) {
                case SDataConstantsSys.TRNS_TP_LINK_ITEM:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c "
                            + "INNER JOIN erp.itmu_item AS i ON c.fk_ref = i.id_item "
                            + "WHERE c.b_del = 0 AND i.id_item = " + itemId + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;

                case SDataConstantsSys.TRNS_TP_LINK_MFR:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itmu_mfr AS mfr "
                            + "INNER JOIN erp.itmu_item AS i ON mfr.id_mfr = i.fid_mfr "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND i.fid_mfr = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;

                case SDataConstantsSys.TRNS_TP_LINK_BRD:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itmu_brd AS brd "
                            + "INNER JOIN erp.itmu_item AS i ON brd.id_brd = i.fid_brd "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND i.fid_brd = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_LINE:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itmu_line AS l "
                            + "INNER JOIN erp.itmu_item AS i ON l.id_line = i.fid_line_n "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND i.fid_line_n = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IGEN:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itmu_igen AS g "
                            + "INNER JOIN erp.itmu_item AS i ON g.id_igen = i.fid_igen "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND i.fid_igen = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IGRP:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itmu_igrp AS grp "
                            + "INNER JOIN erp.itmu_igen AS gen ON grp.id_igrp = gen.fid_igrp "
                            + "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND gen.fid_igrp = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_IFAM:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itmu_ifam AS fam "
                            + "INNER JOIN erp.itmu_igrp AS grp ON fam.id_ifam = grp.fid_ifam "
                            + "INNER JOIN erp.itmu_igen AS gen ON gen.fid_igrp = grp.id_igrp "
                            + "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND grp.fid_ifam = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_TP_ITEM:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itms_tp_item AS tp "
                            + "INNER JOIN erp.itmu_igen AS gen ON gen.fid_tp_item = tp.id_tp_item "
                            + "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND gen.fid_tp_item = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_CL_ITEM:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itms_cl_item AS cl "
                            + "INNER JOIN erp.itmu_igen AS gen ON gen.fid_cl_item = cl.id_cl_item "
                            + "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND gen.fid_cl_item = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;
                case SDataConstantsSys.TRNS_TP_LINK_CT_ITEM:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c, erp.itms_ct_item AS ct "
                            + "INNER JOIN erp.itmu_igen AS gen ON gen.fid_ct_item = ct.id_ct_item "
                            + "INNER JOIN erp.itmu_item AS i ON gen.id_igen = i.fid_igen "
                            + "WHERE c.b_del = 0 AND c.fk_tp_link = " + linkType[0] + " AND c.fk_ref = " + linkType[1] + " AND  i.id_item = " + itemId + " AND gen.fid_ct_item = " + linkType[1] + " AND c.id_tp_mms = " + mmsType + "; ";
                    break;

                case SDataConstantsSys.TRNS_TP_LINK_ALL:
                    sql = "SELECT c.id_tp_mms, c.id_cfg, c.fk_tp_link, c.fk_ref "
                            + "FROM trn_mms_cfg AS c  "
                            + "WHERE c.b_del = 0 AND c.fk_ref = 0; ";
                    break;
                    
                default:
            }

            resultSetCfg = statementCfg.executeQuery(sql);
            while (resultSetCfg.next()) {
                mapConfigs.put(resultSetCfg.getInt("c.fk_tp_link"), new int[] { resultSetCfg.getInt("c.id_tp_mms"), resultSetCfg.getInt("c.id_cfg") });
            }
        }

        for (int i = SDataConstantsSys.TRNS_TP_LINK_ITEM; i >= SDataConstantsSys.TRNS_TP_LINK_ALL; i--) {
            if (mapConfigs.get(i) != null) {
                configKey = mapConfigs.get(i);
                break;
            }
        }

        return configKey != null ? configKey : new int[] { SLibConsts.UNDEFINED, SLibConsts.UNDEFINED };
    }
    
    public static String computeMailHeader(final String companyName) {
        String mail = "<html> "
            + "<head> "
            + "<style> "
            + "p#title { font-weight: bold; font-size: 1.2em; } "
            + "#info { font-size: 0.7em; } "
            + "#website { font-size: 0.7em; } "
            + "#release { font-size: 0.5em; } "
            + "span#num { font-weight: bold; font-size: 1em; } "
            + "table, th, td { border: 1px solid black; border-collapse: collapse; padding: 0.2em; font-size: 0.9em; } "
            + "th { text-align: left; font-size: 0.9em; vertical-align: top; min-width: 6.5em; } "
            + "td#number, th#number { text-align: right; } "
            + "</style></head> "
            + "<body> "
            + "<p id= \"title\">" + SLibUtils.textToHtml(companyName) + "</p> ";
        
        return mail;
    }
    
    public static String computeMailHeaderBeginTable(final String companyName, final String dpsType, final String dpsNumber, final String bpName, final Date dpsDate, final Date dpsDateTime, final boolean isEdited, final boolean isRebill) {
        String mail = computeMailHeader(companyName)
            + "<p><b>" + SLibUtils.textToHtml(dpsType) + " " + (!isEdited ? "NUEVO" : "MODIFICADO") + "</b><span id =\"info\"> (" + SLibUtils.DateFormatDatetime.format(dpsDateTime) + ")</span></p>"
            + "<p>Folio: <span id=\"num\">" + dpsNumber + "</span>; Fecha: <b>" + SLibUtils.DateFormatDate.format(dpsDate) + "</b>" + (isRebill ? SLibUtils.textToHtml("; Refacturación!") : "") + "</p>"
            + "<p><span>" + SLibUtils.textToHtml(bpName) + "</span></p>"
            + "<table><tr> "
            + "<th>" + SLibUtils.textToHtml("Doc. origen") + "</th> "
            + "<th>" + SLibUtils.textToHtml("Referencia") + "</th> "
            + "<th id=\"number\">" + SLibUtils.textToHtml("Cantidad") + "</th> "
            + "<th id=\"number\">" + SLibUtils.textToHtml("Acum. día") + "</th> "
            + "<th id=\"number\">" + SLibUtils.textToHtml("Acum. mes") + "</th> "
            + "<th id=\"number\">" + SLibUtils.textToHtml("Entrega men.") + "<sup>*</sup></th> "
            + "<th id=\"number\">" + SLibUtils.textToHtml("Cant. prog.") + "<sup>*</sup></th> "
            + "<th id=\"number\">" + SLibUtils.textToHtml("Remanente mes.") + "<sup>*</sup></th> "
            + "<th id=\"number\">" + SLibUtils.textToHtml("Cant. total") + "<sup>*</sup></th> "
            + "<th id=\"number\">" + SLibUtils.textToHtml("Remanente total") + "<sup>*</sup></th> "        
            + "</tr>";
        
        return mail;
    }
     
    public static String computeMailItem(final SClientInterface client, final int idItem, final int idUnit, final String itemCode, final String itemName, 
            final String contractNumber, final int contractYearId, final int contractDocId, final int contractMonths, final int priceYear, final int priceMonth, 
            final String orderNumberSeries, final String orderNumber, final String orderNumberReference, 
            final double quantity, final String unitOfMeasure, final Date date,
            final int[] keyDpsType, final boolean isEdited, final boolean isRebill) {
            double accumDay = computeAccumulatedItem(client, idItem, idUnit, keyDpsType, date, date, isRebill, orderNumberSeries, orderNumber);
            double accumMonth = computeAccumulatedItem(client, idItem, idUnit, keyDpsType, SLibTimeUtils.getBeginOfMonth(date), date, isRebill, orderNumberSeries, orderNumber);
            double programmedQuantityMonth = computeAccumulatedDeliveryMonthly(client, contractYearId, contractDocId, priceMonth, isRebill);
            double remainderQuantityMonth = (programmedQuantityMonth - (computeAccumulatedItem(client, idItem, idUnit, keyDpsType, SLibTimeUtils.getBeginOfMonth(date), date, isRebill, orderNumberSeries, orderNumber)));
            double quantityTotal = computeAccumulatedTotal(client, contractYearId, contractDocId, isRebill);
            double remainderQuantityTotal = (quantityTotal - computeAccumulatedDelivery(client, contractYearId, contractDocId, isRebill));
                        
            String mail = "<tr>"
            + "<td>" + itemCode + "</td>"
            + "<td colspan = \"9\">" + SLibUtils.textToHtml(itemName) + "</td>"
            + "<tr>"
            + "<td>" + contractNumber + "</td>"   // contract number
            + "<td>" + orderNumberReference + "</td>"  // contract reference
            + "<td id=\"number\">" + SLibUtils.DecimalFormatValue2D.format(quantity) + " " + unitOfMeasure + "</td>"// quantity the order
            + "<td id=\"number\">" + (isRebill ? "N/A" : SLibUtils.DecimalFormatValue2D.format(accumDay) + " " + unitOfMeasure) + "</td>"   // accumulated quantity
            + "<td id=\"number\">" + (isRebill ? "N/A" : SLibUtils.DecimalFormatValue2D.format(accumMonth) + " " + unitOfMeasure) + "</td>" // accumulated quantity
            + "<td id=\"number\">" + (isRebill ? "N/A" : (priceYear + "-" + SLibUtils.DecimalFormatCalendarMonth.format(priceMonth))) + "</td>" // date period current
            + "<td id=\"number\">" + (isRebill ? "N/A" : SLibUtils.DecimalFormatValue2D.format(programmedQuantityMonth) + " " + unitOfMeasure) + "</td>" // programmed quantity
            + "<td id=\"number\">" + (isRebill ? "N/A" : SLibUtils.DecimalFormatValue2D.format(remainderQuantityMonth) + " " + unitOfMeasure) + "</td>" // accumulated month quantity
            + "<td id=\"number\">" + (isRebill ? "N/A" : SLibUtils.DecimalFormatValue2D.format(quantityTotal) + " " + unitOfMeasure) + "</td>" // total quantity
            + "<td id=\"number\">" + (isRebill ? "N/A" : SLibUtils.DecimalFormatValue2D.format(remainderQuantityTotal) + " " + unitOfMeasure) + "</td>" // accumulated total quantity        
            + "</tr>";

        return mail;
    }
    
    public static String computeMailFooterEndTable() {
        return "</table>"
                + "<font size=\"1\">"
                + "<p>*Entrega men: Mes del que se esta retirando</p>"
                + "<p>*Cant. prog: Cantidad programada para el mes actual</p>"
                + "<p>*Remanente mes: Cantidad restante del mes</p>"
                + "<p>*Cant. total: Cantidad total del contrato</p>"
                + "<p>*Remanente cant. total: Cantidad restante de la cantidad total del contrato</p>"
                + "</font>"
                + "<br>"
                + "<br>"
                + "<hr>"
                + composeMailFooter("")
                + "</body>"
                + "</html>";
    }
    
    public static String composeMailFooter(final String htmlClass) {
        return "<p" + (htmlClass.isEmpty() ? "" : " class=\"" +  htmlClass+ "\"") + ">"
                + "<b>Favor de no responder a este correo-e.</b><br>"
                + "Fue generado de forma automatica con " + SClient.APP_NAME + " | &copy;" + SClient.APP_COPYRIGHT + " " + SClient.VENDOR + "<br>"
                + "tel.: " + SClient.VENDOR_PHONE + " | mail: " + SClient.VENDOR_MAIL + " | web: " + SClient.VENDOR_WEBSITE + "<br>"
                + SClient.APP_RELEASE
                + "</p>";
    }
        
    /**
     * Take out the amount collected each monthly delivery
     * @param client ERP Client interface.
     * @param idItem ID item the contract.
     * @param idOriginalUnit Type unit of item.
     * @param keyDpsType Key type Dps document contract.
     * @param start Date start contract.
     * @param end Date end contract.
     * @param isRebill Flag is rebilling.
     * @param numberSeries  Series of document. 
     * @param number Number series of document.
     * @return Quantity of accumulated item.
     */
    private static double computeAccumulatedItem(final SClientInterface client, final int idItem, final int idOriginalUnit,
            final int[] keyDpsType, final Date start, final Date end, final boolean isRebill, final String numberSeries, final String number) {
        String sql = "";
        double total = 0;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            if (!isRebill) {                
                sql = "SELECT COALESCE(SUM(de.orig_qty), 0) " +
                      "FROM trn_dps AS d " +
                      "INNER JOIN " +
                      "trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                      "WHERE d.b_del = 0 AND de.b_del = 0 AND d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(start) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(end)+ "' AND " +
                      "de.fid_item = " + idItem + " AND de.fid_orig_unit = " + idOriginalUnit + " AND fid_ct_dps = " + keyDpsType[0] + " AND fid_cl_dps = " + keyDpsType[1] + " AND fid_tp_dps = " + keyDpsType[2] + " AND d.b_rebill = 0 " +
                      "AND d.num_ser = '" + numberSeries + "' and d.num <= '" + number + "' ";

                statement = client.getSession().getDatabase().getConnection().createStatement();
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    total = resultSet.getDouble(1);
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }

        return total;
    }
    
    /**
     * Calculate the total amount of the order
     * @param client ERP Client interface.
     * @param contractYearId Year of contract work.
     * @param contractDocId Id document contract.
     * @param isRebill Flag is rebilling.
     * @return Quantity of accumulated delivery.
     */
    private static double computeAccumulatedDelivery(final SClientInterface client,  final int contractYearId, final int contractDocId, final boolean isRebill) {
        String sql = "";
        double total = 0;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            if (!isRebill) {
                sql = "SELECT SUM(qty) " +
                      "FROM TRN_DPS_DPS_SUPPLY AS cu " +
                      "WHERE cu.id_src_year = " + contractYearId + " AND cu.id_src_doc = " + contractDocId;

                statement = client.getSession().getDatabase().getConnection().createStatement();
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    total = resultSet.getDouble(1);
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }

        return total;
    }

    /**
     * Calculate accumulated total retired until this month
     * @param client ERP Client interface.
     * @param contractYearId Year of contract work.
     * @param contractDocId Id document contract.
     * @param isRebill Flag is rebilling.
     * @return Quantity accumulated Total.
     */
    private static double computeAccumulatedTotal(final SClientInterface client, final int contractYearId, final int contractDocId, final boolean isRebill) {
        String sql = "";
        double total = 0;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            if (!isRebill) {
                sql = "SELECT qty " +
                      "FROM trn_dps_ety " +
                      "WHERE id_year = " + contractYearId + " AND id_doc = " + contractDocId ;

                statement = client.getSession().getDatabase().getConnection().createStatement();
                resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    total = resultSet.getDouble(1);
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }

        return total;
    }

    /**
     * Calculate the cumulative monthly delivery until this month
     * @param contractYearId Year of contract work.
     * @param contractDocId Id document contract.
     * @param isRebill Flag is rebilling.
     * @return Quantity accumulated Ddlivery Monthly.
     */
    private static double computeAccumulatedDeliveryMonthly(final SClientInterface client, final int contractYearId, final int contractDocId, final int dspReferenceNumber, final boolean isRebill) {
        String sql = "";
        int totalId = 0;
        double total = 0;        
        ResultSet resultIdSet = null;
        ResultSet resultSet = null;
        Statement statement = null;

        try {
            if (!isRebill) {
                sql = "SELECT id_prc " +
                      "FROM trn_dps_ety_prc " +
                      "WHERE id_year = " + contractYearId + " AND id_doc = " + contractDocId + " " +
                      "AND con_prc_mon = " + dspReferenceNumber + " LIMIT 1 " ;

                statement = client.getSession().getDatabase().getConnection().createStatement();
                resultIdSet = statement.executeQuery(sql);
                    if (resultIdSet.next()) {
                        totalId = resultIdSet.getInt(1);
                    }

                    sql = "SELECT SUM(orig_qty) " +
                          "FROM trn_dps_ety_prc " +
                          "WHERE id_year = " + contractYearId + " AND id_doc = " + contractDocId  + " " +
                          "AND id_prc = " + totalId ;

                    statement = client.getSession().getDatabase().getConnection().createStatement();
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        total = resultSet.getDouble(1);
                    }
                }
            }
            catch (Exception e) {
                SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }

        return total;
    }
    
    /**
     * Send mail with information of contracts specificated.
     * @param client ERP client interface.
     * @param mmsType Mail Messaging Service type.
     * @param requestedRecipientsTo Recipients for mail.
     * @param requestedRecipientsCc Recipients carbon copy for mail.
     * @param requestedRecipientsBcc Recipients blind carbon copy for mail.
     * @param body Mail body.
     * @param subjectComplement Mail subject complement. Can be null or empty.
     * @throws java.lang.Exception
     */
    public static void sendMail(final SClientInterface client, final int mmsType, final String body, final String subjectComplement, 
            final ArrayList<String> requestedRecipientsTo, final ArrayList<String> requestedRecipientsCc, final ArrayList<String> requestedRecipientsBcc) throws Exception {
        client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        SDbMms mms = getMms(client, mmsType);
        
        if (mms.getQueryResultId() != SDbConsts.READ_OK) {
            throw new Exception("No pudo encontrarse la configuración de servicio de mensajería por correo-e para el tipo " + mmsType + ".");
        }
        else {
            ArrayList<String> recipientsTo = new ArrayList<>();
            ArrayList<String> recipientsCc = new ArrayList<>();
            ArrayList<String> recipientsBcc = new ArrayList<>();
            
            if (requestedRecipientsTo != null && !requestedRecipientsTo.isEmpty()) {
                for(String recipient : requestedRecipientsTo) {
                    recipientsTo.addAll(Arrays.asList(SLibUtils.textExplode(recipient, ";")));
                }
                
                if (requestedRecipientsCc != null && !requestedRecipientsCc.isEmpty()) {
                    for(String recipient: recipientsCc) {
                        recipientsCc.addAll(Arrays.asList(SLibUtils.textExplode(recipient, ";")));
                    }
                }
                
                if (requestedRecipientsBcc != null && !requestedRecipientsBcc.isEmpty()) {
                    for(String recipient: requestedRecipientsBcc) {
                        recipientsBcc.addAll(Arrays.asList(SLibUtils.textExplode(recipient, ";")));
                    }
                }
            }
            else {
                recipientsTo.addAll(Arrays.asList(SLibUtils.textExplode(mms.getRecipientTo(), ";")));
                recipientsCc.addAll(Arrays.asList(SLibUtils.textExplode(mms.getRecipientCarbonCopy(), ";")));
                recipientsBcc.addAll(Arrays.asList(SLibUtils.textExplode(mms.getRecipientBlindCarbonCopy(), ";")));
            }
            
            if (recipientsTo.isEmpty()) {
                throw new Exception("No se ha configurado ningún correo-e destinatario.");
            }
            else {
                try {
                    SMailSender sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());
                    String subject = mms.getTextSubject() + (subjectComplement != null && !subjectComplement.isEmpty() ? " " + SLibUtils.textTrim(subjectComplement) : "");
                    new SMail(sender, subject, body, SMailConsts.CONT_TP_TEXT_HTML, recipientsTo, recipientsCc, recipientsBcc).send();
                } 
                catch (Exception e) {
                    SLibUtilities.renderException(STrnUtilities.class.getName(), e);
                }
                finally {
                    client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }    
    }
    
    /**
     * To send mail with PDF file attached.
     * @param client GUI client.
     * @param mmsType Tipe of Mail Messaging Service (SModSysConsts.CFGS_TP_MMS_FIN_PAY_AUTH_REQ or SModSysConsts.CFGS_TP_MMS_CFD).
     * @param pdf PDF file to attach.
     * @param subject Mail subject.
     * @param body Mail body.
     * @return <code>true</code> if mail send.
     * @throws java.lang.Exception 
     */
    public static boolean sendMailPdf(final SClientInterface client, final int mmsType, final File pdf, final String subject, final String body) throws Exception {
        return sendMailPdf(client, mmsType, pdf, subject, body, "");
    }
    
    /**
     * To send mail with PDF file attached.
     * @param client GUI client.
     * @param mmsType Tipe of Mail Messaging Service (SModSysConsts.CFGS_TP_MMS_FIN_PAY_AUTH_REQ or SModSysConsts.CFGS_TP_MMS_CFD).
     * @param pdf PDF file to attach.
     * @param subject Mail subject.
     * @param body Mail body.
     * @param recipients Mail recipients, semicolon separated.
     * @return <code>true</code> if mail send.
     * @throws java.lang.Exception 
     */
    public static boolean sendMailPdf(final SClientInterface client, final int mmsType, final File pdf, final String subject, final String body, final String recipients) throws Exception {
        SDbMms mms = null;
        SMailSender sender = null;
        ArrayList<String> toRecipients = null;
        ArrayList<String> ccRecipients = null;
        SMail mail = null;
        boolean sent = false;
        
        if (pdf != null) {
            mms = STrnUtilities.getMms(client, mmsType);

            if (mms != null && mms.getQueryResultId() == SDbConsts.READ_OK) {
                if (!recipients.isEmpty()) {
                    toRecipients = new ArrayList<>(Arrays.asList(SLibUtils.textExplode(recipients, ";")));
                }
                else if (!mms.getRecipientTo().isEmpty()) {
                    toRecipients = new ArrayList<>(Arrays.asList(SLibUtils.textExplode(mms.getRecipientTo(), ";")));
                }

                if (toRecipients == null || toRecipients.isEmpty()) {
                    throw new Exception("No existen destinatarios para el envío del correo-e.");
                }

                if (!mms.getRecipientCarbonCopy().isEmpty()) {
                    ccRecipients = new ArrayList<>(Arrays.asList(SLibUtilities.textExplode(mms.getRecipientCarbonCopy(), ";")));
                }

                sender = new SMailSender(mms.getHost(), mms.getPort(), mms.getProtocol(), mms.isStartTls(), mms.isAuth(), mms.getUser(), mms.getUserPassword(), mms.getUser());

                if (ccRecipients == null) {
                    mail = new SMail(sender, subject, body, toRecipients);
                }
                else {
                    mail = new SMail(sender, subject, body, toRecipients, ccRecipients);
                }

                mail.getAttachments().add(pdf);
                mail.send();
                sent = true;
            }
            else {
                throw new Exception("No existe ninguna configuración para envío de solicitudes.");
            }
        }
        else {
            throw new Exception("No existe PDF para el envío.");
        }
        
        return sent;
    }
    
    public static boolean updateDeliveryAddress(final SClientInterface client, final int[] idBranchAddress, final int[] pkTrnDps) {
        String sql = "";
        Statement statement = null;
        boolean updated = false;
        
        try {
            sql = "UPDATE trn_dps SET fid_add = " + idBranchAddress[1] + " WHERE id_year = " + pkTrnDps[0] + " and id_doc = " + pkTrnDps[1] + ";";

            statement = client.getSession().getDatabase().getConnection().createStatement();
            statement.executeUpdate(sql);
            updated = true;
        }
        catch (Exception e) {
            SLibUtilities.printOutException(STrnUtilities.class.getName(), e);
        }
        
        return updated;
    }
    
    /**
     * Processes a stock move and generates a entry for a diog
     * @param client ERP Client interface.
     * @param stockMove object STrnStockMove type
     * @return object SDataDiogEntry type
     * @throws java.lang.Exception 
     */
    private static SDataDiogEntry processStockEntries(final SClientInterface client, STrnStockMove stockMove) throws java.lang.Exception {
        SDataDiogEntry diogEntry = null;
        SDataItem item = null;

        item = (SDataItem) SDataUtilities.readRegistry(client, SDataConstants.ITMU_ITEM, new int[] { stockMove.getPkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);

        diogEntry = new SDataDiogEntry();
        diogEntry.setPkYearId(SLibConstants.UNDEFINED);
        diogEntry.setPkDocId(SLibConstants.UNDEFINED);
        diogEntry.setPkEntryId(SLibConstants.UNDEFINED);
        diogEntry.setQuantity(stockMove.getQuantity());
        diogEntry.setValueUnitary(0);
        diogEntry.setValue(0);
        diogEntry.setOriginalQuantity(stockMove.getQuantity());
        diogEntry.setOriginalValueUnitary(0);
        diogEntry.setSortingPosition(0);
        diogEntry.setIsInventoriable(true);
        diogEntry.setIsDeleted(false);
        diogEntry.setFkItemId(item.getPkItemId());
        diogEntry.setFkUnitId(item.getFkUnitId());
        diogEntry.setFkOriginalUnitId(item.getFkUnitId());

        diogEntry.setFkDpsYearId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkDpsDocId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkDpsEntryId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkDpsAdjustmentEntryId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkMfgOrderId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkMfgChargeId_n(SLibConstants.UNDEFINED);
        diogEntry.setFkMaintAreaId(SModSysConsts.TRN_MAINT_AREA_NA);

        diogEntry.setFkUserNewId(client.getSession().getUser().getPkUserId());
        diogEntry.setFkUserEditId(client.getSession().getUser().getPkUserId());
        diogEntry.setFkUserDeleteId(client.getSession().getUser().getPkUserId());

        diogEntry.setDbmsItem(item.getItem());
        diogEntry.setDbmsItemKey(item.getKey());
        diogEntry.setDbmsUnit(item.getDbmsDataUnit().getUnit());
        diogEntry.setDbmsUnitSymbol(item.getDbmsDataUnit().getSymbol());
        diogEntry.setDbmsOriginalUnit(item.getDbmsDataUnit().getUnit());
        diogEntry.setDbmsOriginalUnitSymbol(item.getDbmsDataUnit().getSymbol());

        diogEntry.getAuxStockMoves().add(stockMove);

        return diogEntry;
    }
    
    /**
     * 
     * @param client client ERP Client interface.
     * @param year year for Diog
     * @param date date for Diog
     * @param companyBranch company branch for Diog
     * @param warehouse warehouse for Diog
     * @param diogtype Diog type SModSysConsts.TRNS_TP_IOG_IN_ ...
     * @param numberSerie number serie for Diog
     * @param stockMoves array list stock moves to process
     * @return object SDataDiog type
     * @throws java.lang.Exception 
     */
    public static SDataDiog createDataDiogSystem(final SClientInterface client, final int year, final Date date, final int companyBranch, final int warehouse, final int[] diogtype, final String numberSerie, final Vector<STrnStockMove> stockMoves) throws java.lang.Exception {
        Vector<SDataDiogEntry> iogEntries = new Vector<>();
        SDataDiog iog = null;
        
        iogEntries.clear();
        for (STrnStockMove stockMove : stockMoves) {
            iogEntries.add(processStockEntries(client, stockMove));
        }
        
        iog = new SDataDiog();

        iog.setPkYearId(year);
        iog.setPkDocId(0);
        iog.setDate(date);
        iog.setNumberSeries(numberSerie);
        iog.setNumber("");
        iog.setValue_r(0d);
        iog.setCostAsigned(0);
        iog.setCostTransferred(0);
        iog.setIsShipmentRequired(false);
        iog.setIsShipped(false);
        iog.setIsAudited(false);
        iog.setIsAuthorized(false);
        iog.setIsRecordAutomatic(false);
        iog.setIsSystem(true);
        iog.setIsDeleted(false);
        iog.setFkDiogCategoryId(diogtype[0]);
        iog.setFkDiogClassId(diogtype[1]);
        iog.setFkDiogTypeId(diogtype[2]);
        iog.setFkDiogAdjustmentTypeId(SModSysConsts.TRNU_TP_IOG_ADJ_NA);
        iog.setFkCompanyBranchId(companyBranch);
        iog.setFkWarehouseId(warehouse);
        iog.setFkMaintMovementTypeId(SModSysConsts.TRNS_TP_MAINT_MOV_NA);
        iog.setFkMaintUserId_n(SLibConsts.UNDEFINED);
        iog.setFkMaintUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
        iog.setFkMaintReturnUserId_n(SLibConsts.UNDEFINED);
        iog.setFkMaintReturnUserSupervisorId(SModSysConsts.TRN_MAINT_USER_SUPV_NA);
        iog.setFkUserShippedId(SUtilConsts.USR_NA_ID);
        iog.setFkUserAuditedId(SUtilConsts.USR_NA_ID);
        iog.setFkUserAuthorizedId(SUtilConsts.USR_NA_ID);
        iog.setFkUserNewId(client.getSession().getUser().getPkUserId());
        iog.getDbmsEntries().clear();
        iog.getDbmsEntries().addAll(iogEntries);
                
        return iog;
    }
    
    /**
     * Get CFD primary key by its own UUID.
     * @param statement DB statement.
     * @param uuid Desired UUID.
     * @return 
     */
    public static int getCfdIdByUuid(final Statement statement, final String uuid) {
        String sql = "";
        ResultSet resultSet = null;
        int id = SLibConstants.UNDEFINED;

        try {
            sql = "SELECT id_cfd "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " "
                    + "WHERE uuid = '" + uuid + "';";
            
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            SLibUtils.printException(STrnUtilities.class.getName(), e);
        }

        return id;
    }
    
    /**
     * Get DPS primary key by UUID of its own CFD.
     * @param statement DB statement.
     * @param uuid Desired UUID.
     * @return 
     */
    public static int[] getDpsKeyByUuid(final Statement statement, final String uuid) {
        String sql = "";
        ResultSet resultSet = null;
        int[] key = null;

        try {
            sql = "SELECT fid_dps_year_n, fid_dps_doc_n "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_CFD) + " "
                    + "WHERE uuid = '" + uuid + "';";
            
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                key = new int[] { resultSet.getInt(1), resultSet.getInt(0) };
            }
        }
        catch (Exception e) {
            SLibUtils.printException(STrnUtilities.class.getName(), e);
        }

        return key;
    }
    
    /**
     * Counts how many payments has the provided DPS, excluding, if any, the provided CFD.
     * @param statement DB statement.
     * @param dpsKey Primary key of desired DPS.
     * @param cfdId Primary key of desired CFD to exclude.
     * @return 
     */
    public static int countDpsPayments(final Statement statement, final int[] dpsKey, final int cfdId) {
        String sql = "";
        ResultSet resultSet = null;
        int count = 0;

        try {
            sql = "SELECT count(*) "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " "
                    + "WHERE fid_dps_year_n =" + dpsKey[0] + " AND fid_dps_doc_n = " + dpsKey[1] + " AND fid_cfd_n <> " + cfdId + " AND "
                    + "fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0] + " AND fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1] + " AND "
                    + "fid_cl_sys_mov = " + SModSysConsts.FINS_CL_SYS_MOV_MI + " AND "
                    + "NOT b_del;";
            
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            SLibUtils.printException(STrnUtilities.class.getName(), e);
        }

        return count;
    }
}
