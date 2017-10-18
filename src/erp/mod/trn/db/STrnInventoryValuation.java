package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.SDataStockMove;
import erp.mtrn.data.STrnStockMove;
import erp.mtrn.data.STrnUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.SLibValue;
import sa.lib.SLibValueProration;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class STrnInventoryValuation {
    
    private static final int STEP_RM = 1; // IOG type raw materials
    private static final int STEP_WP = 2; // IOG type work in progress
    private static final int STEP_FG = 3; // IOG type finished goods
    private static final String ERR_MSG_WIP = "No se encontró ningún almacén predeterminado de tipo 'producción en proceso'.";
    
    protected int mnYear;
    protected int mnPeriod;
    protected int[] manDefaultWarehouseWip; // default warehouse of work in progress
    protected Date mtPeriodBegin;
    protected Date mtPeriodEnd;
    protected SGuiSession moSession;
    
    public STrnInventoryValuation(SGuiSession session, int year, int period) {
        moSession = session;
        mnYear = year;
        mnPeriod = period;
        manDefaultWarehouseWip = null;
        mtPeriodBegin = SLibTimeUtils.createDate(mnYear, mnPeriod, 1);
        mtPeriodEnd = SLibTimeUtils.getEndOfMonth(mtPeriodBegin);
    }
    
    /*
     * Private methods
     */
    
    private void createInventoryMfgCosts() throws Exception {
        int numOrdersSta = 0;
        int numOrdersFin = 0;
        double qtyUnitsSta = 0;
        double qtyUnitsWip = 0;
        double qtyUnitsFin = 0;
        double perUnitsFin = 0;
        double perUnitsFinEffective = 0;
        String sql = "";
        Statement statement = moSession.getStatement().getConnection().createStatement();
        ResultSet resultSet = null;
        SDbInventoryMfgCost inventoryMfgCost = null;
        
        // Retrieve MFG job orders and product units started and finished of current period:
        
        sql = "SELECT de.fid_item, de.fid_unit, i.item, i.item_key, u.symbol, "
                + "o._ord, o._ord_sta, o._ord_fin, o._unt_sta, "
                + "SUM((CASE WHEN d.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " THEN 1 ELSE -1 END) * de.qty) AS _unt_fin "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON de.fid_unit = u.id_unit "
                + "LEFT OUTER JOIN ("
                + "SELECT o.fid_item_r, o.fid_unit_r, "
                + "COUNT(*) AS _ord, SUM(o.fid_st_ord>" + SModSysConsts.MFGS_ST_ORD_NEW + ") AS _ord_sta, SUM(o.fid_st_ord = " + SModSysConsts.MFGS_ST_ORD_CLO + ") AS _ord_fin, "
                + "SUM(CASE WHEN o.fid_st_ord>" + SModSysConsts.MFGS_ST_ORD_NEW + " THEN o.qty ELSE 0 END) AS _unt_sta "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o "
                + "WHERE o.b_del = 0 AND o.b_for = 0 AND o.dt_start_n BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                + "GROUP BY o.fid_item_r, o.fid_unit_r "
                + "ORDER BY o.fid_item_r, o.fid_unit_r) AS o ON de.fid_item = o.fid_item_r AND de.fid_unit = o.fid_unit_r "
                + "WHERE d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND d.b_del = 0 AND de.b_del = 0 AND ("
                + "(d.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[1] + " AND "
                + "d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_ASD[2] + ")) OR "
                + "(d.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " AND "
                + "d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_RET[2] + ", " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_RET[2] + "))) "
                + "GROUP BY de.fid_item, de.fid_unit, i.item, i.item_key, u.symbol "
                + "ORDER BY de.fid_item, de.fid_unit, i.item, i.item_key, u.symbol; ";
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            inventoryMfgCost = new SDbInventoryMfgCost();
            inventoryMfgCost.setPkYearId(mnYear);
            inventoryMfgCost.setPkPeriodId(mnPeriod);
            inventoryMfgCost.setPkItemId(resultSet.getInt("fid_item"));
            inventoryMfgCost.setPkUnitId(resultSet.getInt("fid_unit"));
            inventoryMfgCost.setOrders(resultSet.getInt("_ord"));
            inventoryMfgCost.setOrdersStarted(numOrdersSta = resultSet.getInt("_ord_sta"));
            inventoryMfgCost.setOrdersFinished(numOrdersFin = resultSet.getInt("_ord_fin"));
            inventoryMfgCost.setQuantityStarted(qtyUnitsSta = resultSet.getDouble("_unt_sta"));
            inventoryMfgCost.setQuantityWorkInProgress(0);
            inventoryMfgCost.setQuantityFinished(qtyUnitsFin = resultSet.getDouble("_unt_fin"));
            inventoryMfgCost.setQuantityFinishedPer(0);
            inventoryMfgCost.setQuantityFinishedEffectivePer(0);
            inventoryMfgCost.setRmCosts(0);
            inventoryMfgCost.setRmCostsWorkInProgress(0);
            inventoryMfgCost.setRmCostsFinishedGoods(0);
            inventoryMfgCost.setMohCosts(0);
            inventoryMfgCost.setMohCostsWorkInProgress(0);
            inventoryMfgCost.setMohCostsFinishedGoods(0);
            inventoryMfgCost.setCosts(0);
            inventoryMfgCost.setCostsWorkInProgress(0);
            inventoryMfgCost.setCostsFinishedGoods(0);
            inventoryMfgCost.setCostUnitWorkInProgress(0);
            inventoryMfgCost.setCostUnitFinishedGoods(0);
            //inventoryMfgCost.setDeleted(...);
            //inventoryMfgCost.setFkUserInsertId(...);
            //inventoryMfgCost.setFkUserUpdateId(...);
            //inventoryMfgCost.setTsUserInsert(...);
            //inventoryMfgCost.setTsUserUpdate(...);
            
            /*
            US: units started
            UF: units finished
            Cases:
               US UF
            a) 0 = 0:   0%
            b) 0 < n: 100%
            c) n > 0:   0%
            d) n = n: 100%
            e) n < m: 100%
            f) m > n: m/n or 100% if all job orders of current period are closed
            */
            
            if (qtyUnitsSta == 0) {
                qtyUnitsWip = 0;
                perUnitsFinEffective = perUnitsFin = qtyUnitsFin == 0 ? 0 : 1; // cases a) & b)
            }
            else {
                perUnitsFin = qtyUnitsFin / qtyUnitsSta;
                
                if (qtyUnitsFin == 0) {
                    qtyUnitsWip = qtyUnitsSta;
                    perUnitsFinEffective = 0; // case c)
                }
                else if (qtyUnitsSta <= qtyUnitsFin) {
                    qtyUnitsWip = 0;
                    perUnitsFinEffective = 1; // cases d) & e)
                }
                else {
                    qtyUnitsWip = numOrdersSta == numOrdersFin ? 0 : qtyUnitsSta - qtyUnitsFin;
                    perUnitsFinEffective = numOrdersSta == numOrdersFin ? 1 : qtyUnitsFin / qtyUnitsSta; // case f)
                }
            }
            
            inventoryMfgCost.setQuantityWorkInProgress(qtyUnitsWip);
            inventoryMfgCost.setQuantityFinishedPer(perUnitsFin);
            inventoryMfgCost.setQuantityFinishedEffectivePer(perUnitsFinEffective);
            
            inventoryMfgCost.save(moSession);
        }
    }
    
    /**
     * Checks if a default warehouse of work in progress has been set.
     * @throws Exception 
     */
    private void validateDefaultWarehouseWip() throws Exception {
        String sql = "";
        Statement stProduct = moSession.getStatement().getConnection().createStatement();
        ResultSet rsProduct = null;
        
        manDefaultWarehouseWip = null;
        
        sql = "SELECT id_cob, id_ent "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " "
                + "WHERE fid_ct_ent = " + SModSysConsts.CFGS_TP_ENT_WH_MFG_WP[0] + " AND fid_tp_ent = " + SModSysConsts.CFGS_TP_ENT_WH_MFG_WP[1] + " AND "
                + "NOT b_del AND b_def ";

        rsProduct = stProduct.executeQuery(sql);
        if (!rsProduct.next()) {
            throw new Exception(ERR_MSG_WIP);
        }
        else {
            manDefaultWarehouseWip = new int[] { rsProduct.getInt(1), rsProduct.getInt(2) };
        }
    }
    
    /**
     * Checks if all production orders belonging to current period have properly consume its assigned materials.
     * @throws Exception 
     */
    private void validateConsumptionProductionOrder() throws Exception {
        String sql = "";
        String msgJobOrders = "";
        Statement stJobOrder = moSession.getStatement().getConnection().createStatement();
        Statement stStockMove = moSession.getStatement().getConnection().createStatement();
        ResultSet rsJobOrder = null;
        ResultSet rsStockMove = null;
        
        // Get a list of all production orders belonging to current period to check if have had properly consume its assigned materials:
            
        sql = "SELECT DISTINCT o.id_year, o.id_ord "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                + "WHERE d.b_del = 0 AND s.b_del = 0 AND s.id_year = " + mnYear + " AND "
                + "s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "d.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " AND "
                + "d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ", "
                + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") "
                + "ORDER BY o.fid_item_r, o.fid_unit_r; ";

        rsJobOrder = stJobOrder.executeQuery(sql);
        while (rsJobOrder.next()) {
            sql = "SELECT d.dt, d.id_year, d.id_doc, d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, CONCAT(o.id_year, '-', o.num) AS _ord "
                    + "FROM trn_stk AS s "
                    + "INNER JOIN trn_diog AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                    + "INNER JOIN mfg_ord AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                    + "WHERE NOT d.b_del AND NOT s.b_del AND "
                    + "s.id_year = " + mnYear + " AND s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                    + "d.fid_mfg_year_n = " + rsJobOrder.getInt("o.id_year") + " AND d.fid_mfg_ord_n = " + rsJobOrder.getInt("o.id_ord") + " "
                    + "ORDER BY  d.dt DESC, d.id_year DESC, d.id_doc DESC; ";
            
            rsStockMove = stStockMove.executeQuery(sql);
            if (rsStockMove.next()) {
                int[] key = new int[] { rsStockMove.getInt("d.fid_ct_iog") , rsStockMove.getInt("d.fid_cl_iog"), rsStockMove.getInt("d.fid_tp_iog") };
                if (!SLibUtils.belongsTo(key, new int[][] { SModSysConsts.TRNS_TP_IOG_IN_MFG_CON, SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON })) {
                    msgJobOrders += (msgJobOrders.isEmpty() ? "" : "\n") + "El último movimiento de la OP '" + rsStockMove.getString("_ord") + "' no es el consumo de MP y P.";
                }
            }
        }
        
        if (!msgJobOrders.isEmpty()) {
            throw new Exception(msgJobOrders);
        }
    }
    
    /**
     * Checks if a new inventory valuation can be computed.
     * @throws Exception 
     */
    private void validateValuation() throws Exception {
        if (!SDataUtilities.isPeriodOpen((SClientInterface) moSession.getClient(), new int[] { mnYear, mnPeriod })) {
            throw new Exception(SLibConstants.MSG_ERR_GUI_PER_CLOSE + "\nPeríodo cerrado: " + mnYear + "-" + mnPeriod);
        }
        
        validateDefaultWarehouseWip();
        validateConsumptionProductionOrder();
    }
    
    /**
     * Prorate the cost of raw materials between quantity produced.
     * @param idProduct item produced ID.
     * @param idUnitProduct unit of item produced ID.
     * @param cost cost of raw materials
     * @throws Exception 
     */
    private void prorateCostRawMaterialsProduct(final int idProduct, final int idUnitProduct, final double cost) throws Exception {
        double qtyProduced = 0;
        double qtyProducedAcumm = 0;
        String sql = "";
        Statement stProduct = moSession.getStatement().getConnection().createStatement();
        ResultSet rsProduct = null;
        Vector<STrnStockMove> stockMoves = new Vector<STrnStockMove>();
        SDataDiog diog = null;
        SLibValueProration proration = null;
        
        sql = "SELECT COALESCE(SUM(s.mov_in - s.mov_out), 0) AS _stk, s.id_cob, s.id_wh "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                + "WHERE d.b_del = 0 AND s.b_del = 0 AND s.id_year = " + mnYear + " AND "
                + "s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "d.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " AND "
                + "d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ", "
                + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2] + ") AND "
                + "s.id_item = " + idProduct + " AND s.id_unit = " + idUnitProduct + " "
                + "ORDER BY s.id_cob, s.id_wh; ";

        rsProduct = stProduct.executeQuery(sql);
        while (rsProduct.next()) {
            qtyProduced = rsProduct.getDouble("_stk");
            
            if (qtyProduced > 0) {
                qtyProducedAcumm += qtyProduced;
                
                stockMoves.add(new STrnStockMove(
                        new int[] { mnYear, idProduct, idUnitProduct, 0, rsProduct.getInt("s.id_cob"), rsProduct.getInt("s.id_wh") }, 
                        qtyProduced));
            }
        }
        
        // create stock move for default warehouse production:
        
        if (stockMoves.isEmpty()) {
            if (manDefaultWarehouseWip == null) {
                throw new Exception(ERR_MSG_WIP);
            }
            else {
                stockMoves.add(new STrnStockMove(
                        new int[] { mnYear, idProduct, idUnitProduct, 0, manDefaultWarehouseWip[0], manDefaultWarehouseWip[1] }, 
                        0.0));
            }
        }
        
        // cost proration:
        
        proration = new SLibValueProration();
        
        if (qtyProducedAcumm > 0) {
            for (STrnStockMove stkMove : stockMoves) {
                proration.addValue(new SLibValue(stkMove.getStockMoveKey(), stkMove.getQuantity()));
            }
            proration.prorateValue(cost, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());

            for (STrnStockMove stkMove : stockMoves) {
                SLibValue val = proration.getValue(stkMove.getStockMoveKey());

                stkMove.setQuantity(0);
                stkMove.setValue(val.getValueProrated());
            }
        }
        else {
            for (STrnStockMove stkMove : stockMoves) {
                stkMove.setValue(cost);
            }
        }
        
        // Generate a system store movement of "MATERIAL COST ENTRY" for each prorated value:
        
        if (!stockMoves.isEmpty()) {
            diog = STrnUtilities.createDataDiogSystem((SClientInterface) moSession.getClient(), mnYear, mtPeriodEnd, stockMoves.get(0).getPkCompanyBranchId(), stockMoves.get(0).getPkWarehouseId(), SModSysConsts.TRNS_TP_IOG_IN_CST_RM, "CM", stockMoves);
                        
            // Update unitary value in diog entry:
            for (SDataDiogEntry entry : diog.getDbmsEntries()) {
                if (!entry.getIsDeleted()) {
                    entry.setValueUnitary(entry.getAuxStockMoves().get(0).getValue());
                }
            }
            
            if (diog.save(moSession.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
            }
        }
    }
    
    /**
     * Values outputs by consumption of raw materials and PT.
     * @throws Exception 
     */
    private void computeConsumeMove() throws Exception {
        int idProduct = 0;
        int idUnitProduct = 0;
        int idRawMaterial = 0;
        int idUnitRawMaterial = 0;
        double acumCostByProduct = 0;
        double acumStock = 0;
        double acumCosts = 0;
        double auxCost = 0;
        double auxCostU = 0;
        double auxValue = 0;
        String sql = "";
        Statement stProduct = moSession.getStatement().getConnection().createStatement();
        Statement stStockMove = moSession.getStatement().getConnection().createStatement();
        Statement stStockCosts = moSession.getStatement().getConnection().createStatement();
        Statement stUpdates = moSession.getStatement().getConnection().createStatement();
        ResultSet rsProduct = null;
        ResultSet rsStockMove = null;
        ResultSet rsStockCosts = null;
        
        // Integrate a list of products (PP / PT) that received inputs in the period evaluated:
            
        sql = "SELECT DISTINCT o.fid_item_r, o.fid_unit_r "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                + "WHERE d.b_del = 0 AND s.b_del = 0 AND s.id_year = " + mnYear + " AND "
                + "s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "d.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " AND "
                + "d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ", "
                + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") "
                + "ORDER BY o.fid_item_r, o.fid_unit_r; ";

        rsProduct = stProduct.executeQuery(sql);
        while (rsProduct.next()) {
            // For each product that has received inputs do the following:
            
            if (idProduct != rsProduct.getInt("o.fid_item_r") || idUnitProduct != rsProduct.getInt("o.fid_unit_r")) { // check if a different item-unit is available
                idProduct = rsProduct.getInt("o.fid_item_r");
                idUnitProduct = rsProduct.getInt("o.fid_unit_r");
                
                acumCostByProduct = 0;

                // Obtain all warehouse movements of the valued period that are of the type "OUTPUT BY CONSUMPTION OF INPUTS AND PT":
                
                sql = "SELECT s.id_item, s.id_unit, s.dt, s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, s.id_lot, s.id_cob, s.id_wh, s.id_mov, "
                        + "s.mov_in, s.mov_out, s.cost_u, s.cost, s.debit, s.credit, "
                        + "s.fid_diog_year, s.fid_diog_doc, s.fid_diog_ety, d.fid_diog_year_n, d.fid_diog_doc_n, "
                        + "o.id_year, o.id_ord, o.fid_item_r, o.fid_unit_r "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                        + "WHERE d.b_del = 0 AND s.b_del = 0 AND s.id_year = " + mnYear + " AND "
                        + "s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                        + "d.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[1] + " AND d.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[2] + " AND "
                        + "o.fid_item_r = " + idProduct + " AND o.fid_unit_r = " + idUnitProduct + " "
                        + "ORDER BY s.id_item, s.id_unit, s.dt, s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, s.id_lot, s.id_cob, s.id_wh, s.id_mov; ";

                rsStockMove = stStockMove.executeQuery(sql);
                while (rsStockMove.next()) {
                    idRawMaterial = rsStockMove.getInt("s.id_item");
                    idUnitRawMaterial = rsStockMove.getInt("s.id_unit");

                    acumStock = 0;
                    acumCosts = 0;

                    sql = "SELECT SUM(s.mov_in - s.mov_out) AS _stk, SUM(s.debit - s.credit) AS _bal "
                            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                            + "WHERE s.b_del = 0 AND s.id_year = " + mnYear + " AND ("
                            + "s.dt " + (mnPeriod == 1 ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' OR ("
                            + "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                            + "s.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " AND s.fid_cl_iog < " + SModSysConsts.TRNS_CL_IOG_IN_INT[1] + ")) AND "
                            + "s.id_item = " + idRawMaterial + " AND s.id_unit = " + idUnitRawMaterial + "; ";

                    rsStockCosts = stStockCosts.executeQuery(sql);
                    if (rsStockCosts.next()) {
                        acumStock = rsStockCosts.getDouble("_stk");
                        acumCosts = rsStockCosts.getDouble("_bal");
                    }

                    auxCostU = SLibUtils.round(acumStock == 0 ? 0 : acumCosts / acumStock, SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits());

                    auxValue = SLibUtils.round(auxCostU * rsStockMove.getDouble("s.mov_out"), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());

                    acumStock -= rsStockMove.getDouble("s.mov_out");
                    acumCosts -= auxValue; // credit

                    // Add the value of these outputs:

                    acumCostByProduct = SLibUtils.round(acumCostByProduct + auxValue, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());

                    auxCost = SLibUtils.round(acumStock == 0 ? 0 : acumCosts / acumStock, SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits());

                    // Update cost of current outgoing stock movement:

                    sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                            + "SET cost_u = " + auxCostU + ", cost = " + auxCost + ", debit = " + 0 + ", credit = " + auxValue + " "
                            + "WHERE id_year = " + mnYear + " AND id_item = " + idRawMaterial + " AND id_unit = " + idUnitRawMaterial + " AND id_lot = " + rsStockMove.getInt("s.id_lot") + " AND "
                            + "id_cob = " + rsStockMove.getInt("s.id_cob") + " AND id_wh = " + rsStockMove.getInt("s.id_wh") + " AND id_mov = " + rsStockMove.getInt("s.id_mov") + "; ";
                    stUpdates.execute(sql);
                }
                
                /* Prorate the value obtained in the previous step between the warehouses in which there was production as a function of the net production volume, 
                    or in default to the predetermined store of production in process: */
              
                if (acumCostByProduct > 0) {
                    prorateCostRawMaterialsProduct(idProduct, idUnitProduct, acumCostByProduct);
                }
            }
        }
    }
    
    private void createStockMoveCostRawMaterials(final int[] key, final double cost) throws Exception {
        SDataStockMove stockMove = new SDataStockMove();
        
        stockMove.setPrimaryKey(key);
        
        //stockMove.setPkYearId(...);
        //stockMove.setPkItemId(...);
        //stockMove.setPkUnitId(...);
        //stockMove.setPkLotId(...);
        //stockMove.setPkCompanyBranchId(...);
        //stockMove.setPkWarehouseId(...);
        //stockMove.setPkMoveId(...);
        stockMove.setDate(mtPeriodEnd);
        //stockMove.setMoveIn(...);
        //stockMove.setMoveOut(...);
        //stockMove.setUsed(...);
        stockMove.setCostUnitary(cost);
        //stockMove.setCost(...);
        stockMove.setDebit(cost);
        //stockMove.setCredit(...);
        //stockMove.setIsDeleted(...);
        stockMove.setFkDiogCategoryId(SModSysConsts.TRNS_TP_IOG_IN_CST_RM[0]);
        stockMove.setFkDiogClassId(SModSysConsts.TRNS_TP_IOG_IN_CST_RM[1]);
        stockMove.setFkDiogTypeId(SModSysConsts.TRNS_TP_IOG_IN_CST_RM[2]);
        stockMove.setFkDiogAdjustmentTypeId(SModSysConsts.TRNU_TP_IOG_ADJ_NA);
        stockMove.setFkDiogYearId(mnYear);
        stockMove.setFkDiogDocId(1);
        stockMove.setFkDiogEntryId(1);
        //stockMove.setFkDpsYearId_n(...);
        //stockMove.setFkDpsDocId_n(...);
        //stockMove.setFkDpsEntryId_n(...);
        //stockMove.setFkDpsAdjustmentYearId_n(...);
        //stockMove.setFkDpsAdjustmentDocId_n(...);
        //stockMove.setFkDpsAdjustmentEntryId_n(...);
        //stockMove.setFkMfgYearId_n(...);
        //stockMove.setFkMfgOrderId_n(...);
        //stockMove.setFkMfgChargeId_n(...);
        //stockMove.setFkBookkeepingYearId_n(...);
        //stockMove.setFkBookkeepingNumberId_n(...);
        
        stockMove.save(moSession.getStatement().getConnection());
    }
    
    /*
     * Public methods
     */
    
    public int getYear() { return mnYear; }
    public int getPeriod() { return mnPeriod; }
    
    /**
     * Must be called before saving inventory valuation registry, first sentences in method SDbInventoryValuation.save().
     */
    public void prepareValuation() throws Exception {
        String sql = "";
        Statement statement = moSession.getStatement().getConnection().createStatement();
        
        validateValuation();
        
        // Soft delete inventory valuation former registries:
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " "
                + "SET b_del = 1, fk_usr_upd = " + moSession.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                + "WHERE b_del = 0 AND fk_year_year = " + mnYear + " AND fk_year_per = " + mnPeriod + "; ";
        statement.execute(sql);
        
        // Delete inventory MFG cost former registries:
        
        sql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + " "
                + "WHERE id_year = " + mnYear + " AND id_per = " + mnPeriod + "; ";
        statement.execute(sql);
        
        // Clear unit cost and cost from all outgoing stock movements of current period:
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                + "SET used = 0, cost_u = 0, cost = 0, debit = 0, credit = 0 "
                + "WHERE id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + "; ";
        statement.execute(sql);
        
        // Clear unit cost and cost from all internal incoming stock movements of current period:
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                + "SET used = 0, cost_u = 0, cost = 0, debit = 0, credit = 0 "
                + "WHERE id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " AND fid_cl_iog IN (" + SModSysConsts.TRNS_CL_IOG_IN_INT[1] + ", " + SModSysConsts.TRNS_CL_IOG_IN_MFG[1] + "); ";
        statement.execute(sql);
        
        // Soft delete materials cost stock movements of current period:
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                + "SET b_del = 1 "
                + "WHERE b_del = 0 AND id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_EXP[1] + "; ";
        statement.execute(sql);
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " "
                + "SET b_del = 1 "
                + "WHERE b_del = 0 AND id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_EXP[1] + "; ";
        statement.execute(sql);
        
        /* XXX check if it is still valid!
        // Soft delete materials cost consume stock movements of current period:
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                + "SET b_del = 1 "
                + "WHERE b_del = 0 AND id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_CST_RM[0] + " AND fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_CST_RM[1] + " AND fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_CST_RM[2] + " ; ";
        statement.execute(sql);
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " "
                + "SET b_del = 1 "
                + "WHERE b_del = 0 AND id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_CST_RM[0] + " AND fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_CST_RM[1] + " AND fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_CST_RM[2] + " ; ";
        statement.execute(sql);*/
    }
    
    /**
     * Must be called after saving inventory valuation registry, last sentences in method SDbInventoryValuation.save().
     */
    public void computeValuation(final int idInventoryValuation) throws Exception {
        int idMovClass = 0;
        int idMovClassStep = 0;
        int idItem = 0;
        int idUnit = 0;
        double qtyWip = 0;
        double qtyFin = 0;
        double qtyFinEffPer = 0;
        double acumStock = 0;
        double acumCosts = 0;
        double auxCost = 0;
        double auxCostU = 0;
        double auxValue = 0;
        double auxAmcWip = 0;
        double auxAmcFin = 0;
        boolean isStandard = false;
        String sql = "";
        Statement stMain = moSession.getStatement().getConnection().createStatement();
        Statement stStockCosts = moSession.getStatement().getConnection().createStatement();
        Statement stUpdates = moSession.getStatement().getConnection().createStatement();
        ResultSet rsMain = null;
        ResultSet rsStockCosts = null;
        ResultSet rsUpdates = null;
        
        createInventoryMfgCosts();
        
        // Iterate starting from last class ID of real stock movements:
        
        idMovClass = SModSysConsts.TRNS_CL_IOG_OUT_MFG[1]; // class ID is #6
        idMovClassStep = STEP_RM; // first step
        
        while (idMovClass >= SModSysConsts.TRNS_CL_IOG_OUT_PUR[1]) {
            
            System.out.println("Inventory Valuation. Current IOG class = " + idMovClass + (idMovClass == SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] ? "; current IOG class step = " + idMovClassStep : "") + "...");
            
            // Process outgoing stock movements:
            
            sql = "AND d.fid_cl_iog = " + idMovClass + " ";
            
            if (idMovClass == SModSysConsts.TRNS_CL_IOG_OUT_MFG[1]) {
                switch (idMovClassStep) {
                    case STEP_RM: // raw materials
                        sql += "AND d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ") ";
                        break;
                    case STEP_WP: // work in progress
                        sql += "AND d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ") ";
                        break;
                    case STEP_FG: // finished goods
                        sql += "AND d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2] + ") ";
                        break;
                    default:
                }
            }
            
            idItem = 0;         // not supposed to be necessary, just in case
            idUnit = 0;         // not supposed to be necessary, just in case
            isStandard = false; // not supposed to be necessary, just in case

            sql = "SELECT s.id_item, s.id_unit, s.dt, s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, s.id_lot, s.id_cob, s.id_wh, s.id_mov, "
                    + "s.mov_in, s.mov_out, s.cost_u, s.cost, s.debit, s.credit, "
                    + "s.fid_diog_year, s.fid_diog_doc, s.fid_diog_ety, d.fid_diog_year_n, d.fid_diog_doc_n, "
                    + "o.id_year, o.id_ord, o.fid_item_r, o.fid_unit_r "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                    + "WHERE d.b_del = 0 AND s.b_del = 0 AND s.id_year = " + mnYear + " AND "
                    + "s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                    + "d.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " "
                    + sql
                    + "ORDER BY s.id_item, s.id_unit, s.dt, s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, s.id_lot, s.id_cob, s.id_wh, s.id_mov; ";
            rsMain = stMain.executeQuery(sql);
            while (rsMain.next()) {
                if (idItem != rsMain.getInt("s.id_item") || idUnit != rsMain.getInt("s.id_unit")) { // check if a different item-unit is available
                    idItem = rsMain.getInt("s.id_item");
                    idUnit = rsMain.getInt("s.id_unit");
                    
                    System.out.println("* item = " + idItem +  "; unit = " + idUnit + "...");
                    
                    acumStock = 0;
                    acumCosts = 0;
                    
                    isStandard = !(idMovClass == SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] && SLibUtils.belongsTo(idMovClassStep, new int[] { STEP_WP, STEP_FG }));
                    
                    if (isStandard) {
                        // Standard valuation, items valued directly:
                        
                        sql = "SELECT SUM(s.mov_in - s.mov_out) AS _stk, SUM(s.debit - s.credit) AS _bal "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                                + "WHERE s.b_del = 0 AND s.id_year = " + mnYear + " AND ("
                                + "s.dt " + (mnPeriod == 1 ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' OR ("
                                + "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                                + "s.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " AND s.fid_cl_iog < " + SModSysConsts.TRNS_CL_IOG_IN_INT[1] + ")) AND "
                                + "s.id_item = " + idItem + " AND s.id_unit = " + idUnit + "; ";
                        rsStockCosts = stStockCosts.executeQuery(sql);
                        if (rsStockCosts.next()) {
                            acumStock = rsStockCosts.getDouble("_stk");
                            acumCosts = rsStockCosts.getDouble("_bal");
                        }
                    }
                    else {
                        // Concentrated valuation, items valued indirectly through job order (MFG WP & FG only):
                        
                        sql = "SELECT SUM(s.debit-s.credit) AS _bal "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                                + "WHERE s.b_del = 0 AND s.id_year = " + mnYear + " AND ("
                                + "s.dt " + (mnPeriod == 1 ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' OR ("
                                + "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND (("
                                + "s.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[0] + " AND s.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[1] + " AND s.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ") OR ("
                                + "s.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[0] + " AND s.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[1] + " AND s.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ")))) AND "
                                + "o.fid_item_r = " + idItem + " AND o.fid_unit_r = " + idUnit + "; ";
                        rsStockCosts = stStockCosts.executeQuery(sql);
                        if (rsStockCosts.next()) {
                            acumCosts = rsStockCosts.getDouble("_bal");
                        }
                        
                        // Update corresponding inventory MFG cost registry:
                        
                        sql = "SELECT qty_wip, qty_fin, qty_fin_eff_per "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + " "
                                + "WHERE id_year = " + mnYear + " AND id_per = " + mnPeriod + " AND id_item = " + idItem + " AND id_unit = " + idUnit + "; ";
                        rsUpdates = stUpdates.executeQuery(sql);
                        if (!rsUpdates.next()) {
                            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n(table = '" + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + "', item = " + idItem + ", unit = " + idUnit + ")");
                        }
                        else {
                            qtyWip = rsUpdates.getDouble("qty_wip");
                            qtyFin = rsUpdates.getDouble("qty_fin");
                            qtyFinEffPer = rsUpdates.getDouble("qty_fin_eff_per");
                            auxAmcFin = SLibUtils.round(acumCosts * qtyFinEffPer, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                            auxAmcWip = SLibUtils.round(acumCosts - auxAmcFin, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                            auxCostU = SLibUtils.round(qtyFin == 0 ? 0 : auxAmcFin / qtyFin, SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits());
                            
                            sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + " "
                                    + "SET "
                                    + "amc = " + acumCosts + ", amc_wip = " + auxAmcWip + ", amc_fin = " + auxAmcFin + ", "
                                    + "cst = " + acumCosts + ", cst_wip = " + auxAmcWip + ", cst_fin = " + auxAmcFin + ", "
                                    + "cst_u_wip = " + SLibUtils.round(qtyWip == 0 ? 0 : auxAmcWip / qtyWip, SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits()) + ", "
                                    + "cst_u_fin = " + auxCostU + " "
                                    + "WHERE id_year = " + mnYear + " AND id_per = " + mnPeriod + " AND id_item = " + idItem + " AND id_unit = " + idUnit + "; ";
                            stUpdates.execute(sql);
                            
                            createStockMoveCostRawMaterials(new int[] { mnYear, idItem, idUnit, 1, manDefaultWarehouseWip[0], manDefaultWarehouseWip[1], 0 }, acumCosts);
                        }
                    }
                }
                
                if (isStandard) {
                    auxCostU = SLibUtils.round(acumStock == 0 ? 0 : acumCosts / acumStock, SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits());
                }
                
                auxValue = SLibUtils.round(auxCostU * rsMain.getDouble("s.mov_out"), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                
                acumStock -= rsMain.getDouble("s.mov_out");
                acumCosts -= auxValue; // credit
                
                auxCost = SLibUtils.round(acumStock == 0 ? 0 : acumCosts / acumStock, SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits());
                
                // Update cost of current outgoing stock movement:
                
                sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                        + "SET cost_u = " + auxCostU + ", cost = " + auxCost + ", debit = " + 0 + ", credit = " + auxValue + " "
                        + "WHERE id_year = " + mnYear + " AND id_item = " + idItem + " AND id_unit = " + idUnit + " AND id_lot = " + rsMain.getInt("s.id_lot") + " AND "
                        + "id_cob = " + rsMain.getInt("s.id_cob") + " AND id_wh = " + rsMain.getInt("s.id_wh") + " AND id_mov = " + rsMain.getInt("s.id_mov") + "; ";
                stUpdates.execute(sql);
                
                // Update cost of corresponding incoming stock movement (MFG and other internal stock movements only):
                
                if (SLibUtils.belongsTo(idMovClass, new int[] { SModSysConsts.TRNS_CL_IOG_OUT_INT[1], SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] })) {
                    if (!isStandard) {
                        auxCost = auxValue; // revaluate cost properly
                    }
                    
                    sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                            + "SET cost_u = " + auxCostU + ", cost = " + auxCost + ", debit = " + auxValue + ", credit = " + 0 + " "
                            + "WHERE id_year = " + mnYear + " AND id_item = " + idItem + " AND id_unit = " + idUnit + " AND id_lot = " + rsMain.getInt("s.id_lot") + " AND "
                            + "fid_diog_year = " + rsMain.getInt("d.fid_diog_year_n") + " AND fid_diog_doc = " + rsMain.getInt("d.fid_diog_doc_n") + " AND fid_diog_ety = " + rsMain.getInt("s.fid_diog_ety") + "; ";
                    stUpdates.execute(sql);
                }
            }
            
            // Continue with other stock movement types, while necessary:

            if (idMovClass == SModSysConsts.TRNS_CL_IOG_OUT_MFG[1]) {
                if (idMovClassStep < STEP_FG) {
                    idMovClassStep++;
                    
                    if (idMovClassStep == STEP_WP) {
                        computeConsumeMove();
                    }
                }
                else {
                    idMovClass--;
                }
            }
            else {
                idMovClass--;
            }
        }
        
        // Update current inventory valuation registry as finished:
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " "
                + "SET b_fin = 1, fk_usr_upd = " + moSession.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                + "WHERE id_inv_val = " + idInventoryValuation + "; ";
        stUpdates.execute(sql);
    }
}
