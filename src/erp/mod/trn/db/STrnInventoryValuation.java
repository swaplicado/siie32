package erp.mod.trn.db;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.STrnStockMove;
import erp.mtrn.data.STrnUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.SLibValue;
import sa.lib.SLibValueProration;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.prt.SPrtConsts;
import sa.lib.prt.SPrtUtils;

/**
 *
 * @author Sergio Flores
 */
public class STrnInventoryValuation {
    
    private static final int IOG_CLASS_PUR = SModSysConsts.TRNS_CL_IOG_OUT_PUR[1]; // purchase stock movement class
    private static final int IOG_CLASS_INT = SModSysConsts.TRNS_CL_IOG_OUT_INT[1]; // manufacturing stock movement class
    private static final int IOG_CLASS_MFG = SModSysConsts.TRNS_CL_IOG_OUT_MFG[1]; // manufacturing stock movement class
    private static final int ITERATION_RM = 1; // IOG type raw materials
    private static final int ITERATION_WP = 2; // IOG type work in progress
    private static final int ITERATION_FG = 3; // IOG type finished goods
    private static final int MAX_MSG_LINES = 10; // maximum message lines
    private static final String SERIES_CST_RM = "CM";
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
    
    /**
     * Validates if a default warehouse of work in progress has been set.
     * @throws Exception 
     */
    private void validateDefaultWarehouseWip() throws Exception {
        manDefaultWarehouseWip = null;
        
        try (Statement statement = moSession.getStatement().getConnection().createStatement()) {
            String sql = "SELECT id_cob, id_ent "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " "
                    + "WHERE fid_ct_ent = " + SModSysConsts.CFGS_TP_ENT_WH_MFG_WP[0] + " AND fid_tp_ent = " + SModSysConsts.CFGS_TP_ENT_WH_MFG_WP[1] + " AND "
                    + "NOT b_del AND b_def ";

            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(ERR_MSG_WIP);
            }
            else {
                manDefaultWarehouseWip = new int[] { resultSet.getInt("id_cob"), resultSet.getInt("id_ent") };
            }
        }
    }
    
    /**
     * Validates if all manufacturing job orders belonging to current period have properly consumed its assigned raw materials and products.
     * @throws Exception 
     */
    private void validateConsumptionsMfgOrders() throws Exception {
        int lines = 0;
        String sql = "";
        String message = "";
        Statement stJobOrder = moSession.getStatement().getConnection().createStatement();
        Statement stStockMove = moSession.getStatement().getConnection().createStatement();
        
        // Get a list of all manufacturing job orders belonging to current period to check if have had properly consume its assigned materials:
            
        sql = "SELECT DISTINCT o.id_year, o.id_ord, CONCAT(o.id_year, '-', o.num) AS _ord, o.dt "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                + "WHERE NOT s.b_del AND NOT d.b_del AND "
                + "s.id_year = " + mnYear + " AND s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                + "ORDER BY o.id_year, o.id_ord; ";

        ResultSet rsJobOrder = stJobOrder.executeQuery(sql);
        while (rsJobOrder.next()) {
            // Check if that last stock movement of current manufacturing job order is the consumption one:
            
            sql = "SELECT d.fid_ct_iog, d.fid_cl_iog, d.fid_tp_iog, "
                    + "d.dt, CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), erp.lib_fix_int(d.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS _num, dt.tp_iog "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRNS_TP_IOG) + " AS dt ON dt.id_ct_iog = d.fid_ct_iog AND dt.id_cl_iog = d.fid_cl_iog AND dt.id_tp_iog = d.fid_tp_iog "
                    + "WHERE NOT s.b_del AND NOT d.b_del AND "
                    + "s.id_year = " + mnYear + " AND s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                    + "d.fid_mfg_year_n = " + rsJobOrder.getInt("o.id_year") + " AND d.fid_mfg_ord_n = " + rsJobOrder.getInt("o.id_ord") + " "
                    + "ORDER BY d.dt DESC, d.ts_edit DESC "
                    + "LIMIT 1; ";
            
            ResultSet rsStockMove = stStockMove.executeQuery(sql);
            if (rsStockMove.next()) {
                int[] key = new int[] { rsStockMove.getInt("d.fid_ct_iog") , rsStockMove.getInt("d.fid_cl_iog"), rsStockMove.getInt("d.fid_tp_iog") };
                if (!SLibUtils.belongsTo(key, new int[][] { SModSysConsts.TRNS_TP_IOG_IN_MFG_CON, SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON })) {
                    if (++lines <= MAX_MSG_LINES) {
                        message += (message.isEmpty() ? "" : "\n") + "El último movimiento de almacén de la OP #" + rsJobOrder.getString("_ord") + " (" + SLibUtils.DateFormatDate.format(rsJobOrder.getDate("o.dt")) + "), "
                                + "#" + rsStockMove.getString("_num") + " (" + SLibUtils.DateFormatDate.format(rsStockMove.getDate("d.dt")) + "), no es \"CONSUMO INSUMOS Y PT\", sino \"" + rsStockMove.getString("dt.tp_iog") + "\".";
                    }
                    else {
                        message += "\nEntre otras.";
                        break;
                    }
                }
            }
            
            rsStockMove.close();
        }
        
        stJobOrder.close();
        stStockMove.close();
        
        if (!message.isEmpty()) {
            throw new Exception(message);
        }
    }
    
    /**
     * Validates if a new inventory valuation can be computed.
     * @throws Exception 
     */
    private void validateNewValuation() throws Exception {
        if (!SDataUtilities.isPeriodOpen((SClientInterface) moSession.getClient(), new int[] { mnYear, mnPeriod })) {
            throw new Exception(SLibConstants.MSG_ERR_GUI_PER_CLOSE + "\nPeríodo cerrado: " + SLibUtils.DecimalFormatCalendarYear.format(mnYear) + "-" + SLibUtils.DecimalFormatCalendarMonth.format(mnPeriod));
        }
        
        validateDefaultWarehouseWip();
        validateConsumptionsMfgOrders();
    }
    
    /**
     * Creates base registries for manufacturing costs, computing total units started and finished and evaluationg ratio (%) of work in progress.
     * @throws Exception 
     */
    private void createInventoryMfgCosts() throws Exception {
        int numOrdersSta = 0;       // number of orders started
        int numOrdersFin = 0;       // number of orders finished
        double qtyUnitsSta = 0;     // quantity of units started
        double qtyUnitsWip = 0;     // quantity of units that remains as work in progress
        double qtyUnitsFin = 0;     // quantity of units finished
        double perUnitsFin = 0;     // percentage of units finished (real)
        double perUnitsFinEff = 0;  // percentage of units finished (effective)
        String sql = "";
        Statement statement = moSession.getStatement().getConnection().createStatement();
        ResultSet resultSet = null;
        SDbInventoryMfgCost inventoryMfgCost = null;
        
        // Retrieve manufacturing job orders and product units started and finished of current period:
        
        /*
        Consider:
        a) units finished as stock input (+)
        b) units returned as stock output (-)
         */
        
        sql = "SELECT de.fid_item, de.fid_unit, i.item, i.item_key, u.symbol, "
                + "o._ord, o._ord_sta, o._ord_fin, o._unt_sta, "
                + "SUM((CASE WHEN d.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " THEN 1 ELSE -1 END) * de.qty) AS _unt_fin "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON de.fid_item = i.id_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON de.fid_unit = u.id_unit "
                + "LEFT OUTER JOIN ("
                + "SELECT o.fid_item_r, o.fid_unit_r, "
                + "COUNT(*) AS _ord, "
                + "SUM(o.fid_st_ord > " + SModSysConsts.MFGS_ST_ORD_NEW + ") AS _ord_sta, "
                + "SUM(o.fid_st_ord = " + SModSysConsts.MFGS_ST_ORD_CLO + ") AS _ord_fin, "
                + "SUM(CASE WHEN o.fid_st_ord > " + SModSysConsts.MFGS_ST_ORD_NEW + " THEN o.qty ELSE 0 END) AS _unt_sta "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o "
                + "WHERE NOT o.b_del AND NOT o.b_for "
                + "AND o.dt_start_n BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                + "GROUP BY o.fid_item_r, o.fid_unit_r "
                + "ORDER BY o.fid_item_r, o.fid_unit_r) AS o ON de.fid_item = o.fid_item_r AND de.fid_unit = o.fid_unit_r "
                + "WHERE NOT d.b_del AND NOT de.b_del "
                + "AND d.id_year = " + mnYear + " AND d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                + "AND ("
                + "(d.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[1] + " AND "
                + "d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_IN_MFG_FG_ASD[2] + ")) "
                + "OR ("
                + "d.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[0] + " AND d.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " AND "
                + "d.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2] + "))) "
                + "GROUP BY i.item, i.item_key, de.fid_item, de.fid_unit, u.symbol "
                + "ORDER BY i.item, i.item_key, de.fid_item, de.fid_unit, u.symbol; ";
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            inventoryMfgCost = new SDbInventoryMfgCost();
            inventoryMfgCost.setPkYearId(mnYear);
            inventoryMfgCost.setPkPeriodId(mnPeriod);
            inventoryMfgCost.setPkItemId(resultSet.getInt("de.fid_item"));
            inventoryMfgCost.setPkUnitId(resultSet.getInt("de.fid_unit"));
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
            Ratio (%) of process termination:
            
            US: units started
            UF: units finished
            
            Existing cases to define ratio (%) of process termination:
               US UF ratio
            a) 0 = 0:   0%
            b) 0 < f: 100%
            c) s > 0:   0%
            d) s = f: 100%
            e) s < f: 100%
            f) s > f: f/s or 100% if all manufacturing job orders of current period are closed
            */
            
            if (qtyUnitsSta == 0) {
                // cases a) & b):
                
                qtyUnitsWip = 0;
                
                if (qtyUnitsFin == 0) {
                    perUnitsFin = 0;    // 0%
                    perUnitsFinEff = 0; // 0%
                }
                else {
                    perUnitsFin = 1;    // 100%
                    perUnitsFinEff = 1; // 100%
                }
            }
            else {
                // other cases:
                
                perUnitsFin = qtyUnitsFin / qtyUnitsSta;
                
                if (qtyUnitsFin == 0) {
                    // case c):
                    
                    qtyUnitsWip = qtyUnitsSta;
                    perUnitsFinEff = 0; // 0%
                }
                else if (qtyUnitsSta <= qtyUnitsFin) {
                    // cases d) & e):
                    
                    qtyUnitsWip = 0;
                    perUnitsFinEff = 1; // 100%
                }
                else {
                    // case f):
                    
                    qtyUnitsWip = numOrdersSta == numOrdersFin ? 0 : qtyUnitsSta - qtyUnitsFin;
                    perUnitsFinEff = numOrdersSta == numOrdersFin ? 1 : qtyUnitsFin / qtyUnitsSta;  // 100% or f/s
                }
            }
            
            inventoryMfgCost.setQuantityWorkInProgress(qtyUnitsWip);
            inventoryMfgCost.setQuantityFinishedPer(perUnitsFin);
            inventoryMfgCost.setQuantityFinishedEffectivePer(perUnitsFinEff);
            
            inventoryMfgCost.save(moSession);
        }
    }
    
    /**
     * Prorates cost to desired product.
     * @param idProduct Product ID.
     * @param idProductUnit Unit ID of product.
     * @param cost Cost to prorate.
     * @throws Exception 
     */
    private void prorateRawMaterialsCosts(final int idProduct, final int idProductUnit, final double cost) throws Exception {
        int amtDecs = SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        int amtUnitDecs = SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits();
        double finishedGoodsAcum = 0;
        String sql = "";
        Statement stProduct = moSession.getStatement().getConnection().createStatement();
        ResultSet rsProduct = null;
        Vector<STrnStockMove> stockMoves = new Vector<>();
        
        // Obtain number of finished goods in each warehose of desired product:
        
        sql = "SELECT id_cob, id_wh, SUM(mov_in - mov_out) AS _stk "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                + "WHERE b_del = 0 AND "
                + "id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                + "fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[0] + " AND fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " AND "
                + "fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + ", "
                + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2] + ") AND "
                + "id_item = " + idProduct + " AND id_unit = " + idProductUnit + " "
                + "GROUP BY id_cob, id_wh "
                + "ORDER BY id_cob, id_wh; ";

        rsProduct = stProduct.executeQuery(sql);
        while (rsProduct.next()) {
            // Create a stock movement for each warehouse that has finished goods of desired product:
            
            double finishedGoods = rsProduct.getDouble("_stk");
            
            if (finishedGoods > 0) {
                finishedGoodsAcum = SLibUtils.round(finishedGoodsAcum + finishedGoods, amtUnitDecs);
                
                stockMoves.add(new STrnStockMove(
                        new int[] { mnYear, idProduct, idProductUnit, 0, rsProduct.getInt("id_cob"), rsProduct.getInt("id_wh") }, 
                        finishedGoods, 0)); // some finished goods, zero value
            }
        }
        
        // prorate cost:
        
        if (stockMoves.isEmpty()) {
            // asign cost directly into default work-in-progress warehose:
            
            stockMoves.add(new STrnStockMove(
                    new int[] { mnYear, idProduct, idProductUnit, 0, manDefaultWarehouseWip[0], manDefaultWarehouseWip[1] }, 
                    0, cost));  // zero finished goods, some value
        }
        else {
            // prorate cost:
            
            SLibValueProration proration = new SLibValueProration();
            
            for (STrnStockMove stockMove : stockMoves) {
                proration.addValue(new SLibValue(stockMove.getStockMoveKey(), stockMove.getQuantity()));
            }
            proration.prorateValue(cost, amtDecs);
            
            // asign prorated cost to each stock movement:

            for (STrnStockMove stockMove : stockMoves) {
                SLibValue val = proration.getValue(stockMove.getStockMoveKey());
                stockMove.setQuantity(0); // clear quantity, because is not longer needed, only prorated value is needed
                stockMove.setValue(val.getValueProrated());
            }
        }
        
        // save stock movements of prorated values:
        
        boolean withCost = false;
        for (STrnStockMove stockMove : stockMoves) {
            Vector<STrnStockMove> moves = new Vector<>();
            moves.add(stockMove);
            SDataDiog diog = STrnUtilities.createDataDiogSystem((SClientInterface) moSession.getClient(), 
                    mnYear, mtPeriodEnd, stockMove.getPkCompanyBranchId(), stockMove.getPkWarehouseId(), SModSysConsts.TRNS_TP_IOG_IN_CST_RM, SERIES_CST_RM, moves, withCost);
                        
            // Update unitary value in diog entry:
            
            for (SDataDiogEntry entry : diog.getDbmsEntries()) {
                entry.setValueUnitary(entry.getAuxStockMoves().get(0).getValue());
            }
            
            if (diog.save(moSession.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
            }
        }
    }
    
    /**
     * Computes values of outputs by consumption of raw materials and products.
     * @throws Exception 
     */
    private void computeConsumptions() throws Exception {
        System.out.println(SLibUtils.textRepeat("-", 80));
        System.out.println("computing consumptions...");
        
        int idMaterial = 0;
        int idMaterialUnit = 0;
        int amtUnitDecs = SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits();
        double stockAcum = 0;
        double costAcum = 0;
        double costByProduct = 0;
        double costUnit = 0;
        double cost = 0;
        String sql = "";
        Statement stOrderType = moSession.getStatement().getConnection().createStatement();
        Statement stProduct = moSession.getStatement().getConnection().createStatement();
        Statement stStockMove = moSession.getStatement().getConnection().createStatement();
        Statement stStockCosts = moSession.getStatement().getConnection().createStatement();
        Statement stUpdates = moSession.getStatement().getConnection().createStatement();
        ResultSet rsOrderType = null;
        ResultSet rsProduct = null;
        ResultSet rsStockMove = null;
        ResultSet rsStockCosts = null;
        
        // Compute costs orderly by tpye of manufacturing order:
        
        sql = "SELECT DISTINCT o.fid_tp_ord, to.tp "
                + "FROM trn_stk AS s "
                + "INNER JOIN trn_diog AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                + "INNER JOIN mfg_ord AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                + "INNER JOIN mfgu_tp_ord AS to ON o.fid_tp_ord = to.id_tp"
                + "WHERE NOT s.b_del AND NOT d.b_del AND "
                + "s.id_year = " + mnYear + " AND s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                + "ORDER BY o.fid_tp_ord, to.tp;";
        rsOrderType = stOrderType.executeQuery(sql);
        while (rsOrderType.next()) {
            // Integrate a list of products (MFG WP & FG) that received inputs in period evaluated in current type of manufacturing order:

            System.out.println("- Costs computation. Current MFG order type: \"" + rsOrderType.getString("to.tp") + "\" (ID = " + rsOrderType.getInt("o.fid_tp_ord") + ")...");
            
            sql = "SELECT DISTINCT i.item, i.item_key, o.fid_item_r, u.symbol, o.fid_unit_r "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON o.fid_item_r = i.id_item "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON o.fid_unit_r = u.id_unit "
                    + "WHERE NOT s.b_del AND NOT d.b_del AND "
                    + "s.id_year = " + mnYear + " AND s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND (("
                    + "s.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[0] + " AND s.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[1] + " AND "
                    + "s.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ")) OR ("
                    + "s.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[0] + " AND s.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " AND "
                    + "s.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + "))) AND "
                    + "o.fid_tp_ord = " + rsOrderType.getInt("o.fid_tp_ord") + " "
                    + "ORDER BY i.item, i.item_key, o.fid_item_r, u.symbol, o.fid_unit_r;";
            rsProduct = stProduct.executeQuery(sql);
            while (rsProduct.next()) {
                // For each product that has received inputs do the following:

                int prodId = rsProduct.getInt("o.fid_item_r");
                int prodUnitId = rsProduct.getInt("o.fid_unit_r");
                costByProduct = 0;

                System.out.println("- * product = \"" + rsProduct.getString("i.item") + ", " + rsProduct.getString("i.item_key") + "\" (ID = " + prodId +  "); "
                        + "unit = \"" + rsProduct.getString("u.symbol") + "\" (ID = " + prodUnitId + ")...");
                    
                // Obtain all warehouse movements that are of type consumption in valued period:

                sql = "SELECT s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, s.id_mov, s.dt, "
                        + "s.mov_in, s.mov_out, "
                        + "o.id_year, o.id_ord "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                        + "WHERE s.b_del = 0 AND d.b_del = 0 AND "
                        + "s.id_year = " + mnYear + " AND s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                        + "s.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[0] + " AND "
                        + "s.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[1] + " AND "
                        + "s.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[2] + " AND "
                        + "o.fid_item_r = " + prodId + " AND o.fid_unit_r = " + prodUnitId + " "
                        + "ORDER BY s.id_item, s.id_unit, s.dt, s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, s.id_lot, s.id_cob, s.id_wh, s.id_mov; ";
                rsStockMove = stStockMove.executeQuery(sql);
                while (rsStockMove.next()) {
                    idMaterial = rsStockMove.getInt("s.id_item");
                    idMaterialUnit = rsStockMove.getInt("s.id_unit");

                    stockAcum = 0;
                    costAcum = 0;

                    sql = "SELECT SUM(mov_in - mov_out) AS _stk, SUM(debit - credit) AS _bal "
                            + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                            + "WHERE b_del = 0 AND "
                            + "id_year = " + mnYear + " AND ("
                            + "dt " + (mnPeriod == 1 ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' OR ("
                            + "dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                            + "fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_IN_INT[0] + " AND "
                            + "fid_cl_iog < " + SModSysConsts.TRNS_CL_IOG_IN_INT[1] + ")) AND "
                            + "id_item = " + idMaterial + " AND id_unit = " + idMaterialUnit + "; ";
                    rsStockCosts = stStockCosts.executeQuery(sql);
                    if (rsStockCosts.next()) {
                        stockAcum = rsStockCosts.getDouble("_stk");
                        costAcum = rsStockCosts.getDouble("_bal");
                    }

                    costUnit = SLibUtils.round(stockAcum == 0 ? 0 : costAcum / stockAcum, amtUnitDecs);

                    cost = SLibUtils.roundAmount(costUnit * rsStockMove.getDouble("s.mov_out"));

                    // Update cost of current outgoing stock movement:

                    sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                            + "SET used = 0, cost_u = " + costUnit + ", cost = 0, debit = 0, credit = " + cost + " "
                            + "WHERE id_year = " + mnYear + " AND id_item = " + idMaterial + " AND id_unit = " + idMaterialUnit + " AND id_lot = " + rsStockMove.getInt("s.id_lot") + " AND "
                            + "id_cob = " + rsStockMove.getInt("s.id_cob") + " AND id_wh = " + rsStockMove.getInt("s.id_wh") + " AND id_mov = " + rsStockMove.getInt("s.id_mov") + "; ";
                    stUpdates.execute(sql);

                    // Totalize cost by product:

                    costByProduct = SLibUtils.roundAmount(costByProduct + cost);
                }

                /* Prorate the value obtained in the previous iteration between the warehouses in which there was production as a function of the net production volume, 
                    or in default to the predetermined store of production in process: */

                if (costByProduct != 0) {
                    prorateRawMaterialsCosts(prodId, prodUnitId, costByProduct);
                }
            }
        }
    }

    /**
     * ?
     * @param key
     * @param cost
     * @throws Exception 
     */
    /* XXX
    private void createRawMaterialsCostsStockEntries(final int[] key, final double cost) throws Exception {
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
        stockMove.setFkDiogDocId(0);
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
    */
    
    /**
     * Computes consumption stock movements of each provided item-unit.
     * @param itemUnits Item-units for computing its consumption stock movements.
     * @throws Exception 
     */
    private void computeConsumptionMovements(ArrayList<ItemUnit> itemUnits) throws Exception {
        System.out.println(SLibUtils.textRepeat("-", 80));
        System.out.println("computing consumption movements...");
        
        String sql;
        int amtUnitDecs = SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits();
        double stockAcum;
        double costAcum;
        double costUnit;
        double cost;
        Statement st = moSession.getStatement().getConnection().createStatement();
        Statement stUpd = moSession.getStatement().getConnection().createStatement();
        ResultSet rs;
        
        for (ItemUnit itemUnit : itemUnits) {
            stockAcum = itemUnit.Stock;
            costAcum = itemUnit.Value;
            
            sql = "SELECT id_item, id_unit, dt, fid_ct_iog, fid_cl_iog, fid_tp_iog, id_lot, id_cob, id_wh, id_mov, "
                    + "s.mov_in, mov_out, cost_u, cost, debit, credit "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                    + "WHERE NOT b_del AND "
                    + "s.id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                    + "s.fid_ct_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[0] + " AND "
                    + "s.fid_cl_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[1] + " AND "
                    + "s.fid_tp_iog = " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_CON[2] + " AND "
                    + "s.id_item = " + itemUnit.ItemId + " AND id_unit = " + itemUnit.UnitId + " "
                    + "ORDER BY id_item, id_unit, dt, fid_ct_iog, fid_cl_iog, fid_tp_iog, id_lot, id_cob, id_wh, id_mov; ";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                costUnit = SLibUtils.round(stockAcum == 0 ? 0 : costAcum / stockAcum, amtUnitDecs);
                
                cost = SLibUtils.roundAmount(costUnit * rs.getDouble("mov_out"));
                
                stockAcum = SLibUtils.round(stockAcum - rs.getDouble("mov_out"), amtUnitDecs);
                costAcum = SLibUtils.roundAmount(costAcum - cost);   // credit
                
                // Update cost of current outgoing stock movement:
                /*
                Registry to be updated is localized straight by PK of stock movement.
                */
                
                sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                        + "SET used = 0, cost_u = " + costUnit + ", cost = 0, debit = 0, credit = " + cost + " "
                        + "WHERE id_year = " + mnYear + " AND id_item = " + itemUnit.ItemId + " AND id_unit = " + itemUnit.UnitId + " AND id_lot = " + rs.getInt("id_lot") + " AND "
                        + "id_cob = " + rs.getInt("id_cob") + " AND id_wh = " + rs.getInt("id_wh") + " AND id_mov = " + rs.getInt("id_mov") + "; ";
                stUpd.execute(sql);
            }
        }
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
        validateNewValuation();    // validate user defined parameters and stock configuration and data for current valuation
        
        try (Statement statement = moSession.getStatement().getConnection().createStatement()) {
            String sql = "";
            
            // Soft delete inventory valuation former registries:
            
            sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " "
                    + "SET b_del = 1, fk_usr_upd = " + moSession.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                    + "WHERE fk_year_year = " + mnYear + " AND fk_year_per = " + mnPeriod + " "
                    + "AND NOT b_del; ";
            statement.execute(sql);
            
            // Delete inventory MFG cost former registries:
            
            sql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + " "
                    + "WHERE id_year = " + mnYear + " AND id_per = " + mnPeriod + "; ";
            statement.execute(sql);
            
            // Clear unit cost and cost from all outgoing stock movements of current period:
            
            sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                    + "SET used = 0, cost_u = 0, cost = 0, debit = 0, credit = 0 "
                    + "WHERE id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                    + "AND fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " "
                    + "AND NOT b_del; ";
            statement.execute(sql);
            
            // Clear unit cost and cost from all internal incoming stock movements of current period:
            
            sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                    + "SET used = 0, cost_u = 0, cost = 0, debit = 0, credit = 0 "
                    + "WHERE id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                    + "AND fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_IN + " AND fid_cl_iog IN (" + SModSysConsts.TRNS_CL_IOG_IN_INT[1] + ", " + SModSysConsts.TRNS_CL_IOG_IN_MFG[1] + ") "
                    + "AND NOT b_del; ";
            statement.execute(sql);
            
            // Soft delete materials cost stock movements of current period (both in and out movements):
            
            sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                    + "SET b_del = 1 "
                    + "WHERE id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                    + "AND fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_EXP[1] + " "
                    + "AND NOT b_del; ";
            statement.execute(sql);
            
            sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " "
                    + "SET b_del = 1 "
                    + "WHERE id_year = " + mnYear + " AND dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' "
                    + "AND fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_EXP[1] + " "
                    + "AND NOT b_del; ";
            statement.execute(sql);
        }
    }
    
    /**
     * Must be called after saving inventory valuation registry, last sentences in method SDbInventoryValuation.save().
     * @param idInventoryValuation Inventory valuation ID.
     * @throws Exception
     */
    public void computeValuation(final int idInventoryValuation) throws Exception {
        int amtUnitDecs = SLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits();
        String sql = "";
        Statement stMain = moSession.getStatement().getConnection().createStatement();
        Statement stStockCosts = moSession.getStatement().getConnection().createStatement();
        Statement stUpdates = moSession.getStatement().getConnection().createStatement();
        ResultSet rsMain = null;
        ResultSet rsStockCosts = null;
        ResultSet rsUpdates = null;
        ItemUnit itemUnit = null;
        ArrayList<ItemUnit> itemUnits = new ArrayList<>();
        
        createInventoryMfgCosts();  // create manufacturing costs
        
        // Compute all (yes, all!) output stock movements in current period.
        
        /*
        Iterate in descending order starting from last class ID of real stock movements (i.e., expenses movements are excluded):
        Then, for each movement class, iterate from RM, to WP and FG.
        */
        
        int currentIogClassMove = IOG_CLASS_MFG; // first IOG class of movements to process: manufacturing IOG class
        int mfgIogClassIteration = ITERATION_RM; // IOG class iterations only for manufacturing IOG class (the first one to be processed)
        
        while (currentIogClassMove >= IOG_CLASS_PUR) { // go down, descending to reach first class ID of real stock movements
            boolean isMfgIogClassBeingProcessed = currentIogClassMove == IOG_CLASS_MFG;
            
            System.out.println(SLibUtils.textRepeat("=", 80));
            System.out.println("Inventory Valuation. Current IOG class = " + currentIogClassMove + (isMfgIogClassBeingProcessed ? "; current IOG class iteration = " + mfgIogClassIteration : "") + "...");
            
            // Process specific output stock movements at a time:
            
            String sqlWhere = "AND s.fid_cl_iog = " + currentIogClassMove + " ";
            String sqlWhereMfgIogTypes = "";
            
            if (isMfgIogClassBeingProcessed) {
                itemUnits.clear();
                
                switch (mfgIogClassIteration) {
                    case ITERATION_RM: // raw materials
                        sqlWhereMfgIogTypes = "" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2];
                        break;
                    case ITERATION_WP: // work in progress
                        sqlWhereMfgIogTypes = "" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2];
                        break;
                    case ITERATION_FG: // finished goods
                        sqlWhereMfgIogTypes = "" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_FG_RET[2];
                        break;
                    default:
                }
                
                sqlWhere += "AND s.fid_tp_iog IN (" + sqlWhereMfgIogTypes + ") ";
            }

            int items = 0;
            int itemId = 0;
            int unitId = 0;
            double stockAcum = 0;
            double costAcum = 0;
            double costUnit = 0;
            double cost = 0;

            sql = "SELECT i.item, i.item_key, s.id_item, u.symbol, s.id_unit, s.dt, s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, s.id_lot, s.id_cob, s.id_wh, s.id_mov, "
                    + "s.mov_in, s.mov_out, s.cost_u, s.cost, s.debit, s.credit, "
                    + "s.fid_diog_year, s.fid_diog_doc, s.fid_diog_ety, "   // original stock movement
                    + "d.fid_diog_year_n, d.fid_diog_doc_n, "               // mirror stock movement
                    + "o.id_year, o.id_ord, o.fid_item_r, o.fid_unit_r "    // columns preserved in query for debugging purposes
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON s.id_item = i.id_item "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON s.id_unit = u.id_unit "
                    + "LEFT OUTER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                    + "WHERE s.b_del = 0 AND d.b_del = 0 AND "
                    + "s.id_year = " + mnYear + " AND s.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                    + "s.fid_ct_iog = " + SModSysConsts.TRNS_CT_IOG_OUT + " "
                    + sqlWhere
                    + "ORDER BY i.item, i.item_key, s.id_item, u.symbol, s.id_unit, s.dt, s.fid_ct_iog, s.fid_cl_iog, s.fid_tp_iog, s.id_lot, s.id_cob, s.id_wh, s.id_mov;";
            rsMain = stMain.executeQuery(sql);
            while (rsMain.next()) {
                if (itemId != rsMain.getInt("s.id_item") || unitId != rsMain.getInt("s.id_unit")) { // check if a different item-unit is available
                    itemId = rsMain.getInt("s.id_item");
                    unitId = rsMain.getInt("s.id_unit");
                    
                    System.out.println(SPrtUtils.formatText("" + ++items, 4, SPrtConsts.ALIGN_RIGHT, SPrtConsts.TRUNC_HIDE) + ". "
                            + "item: \"" + rsMain.getString("i.item") + ", " + rsMain.getString("i.item_key") + "\" (ID = " + itemId +  "); "
                            + "unit: \"" + rsMain.getString("u.symbol") + "\" (ID = " + unitId + ")...");
                    
                    if (isMfgIogClassBeingProcessed) {
                        itemUnit = new ItemUnit(itemId, unitId, 0, 0);
                        itemUnits.add(itemUnit);
                    }
                    
                    // Available stock and total cost:
                    
                    stockAcum = 0;
                    costAcum = 0;
                    
                    // Manufacturing of goods finished (work in progress inclusive) needs a specific kind of value calculation, a "concentrated valuation".
                    // Check if current movement class requires standard valuation, i.e., check if it is not one of manufacturing as well as if current iteration is not of goods finished:
                    
                    boolean isStraightValuation = !(isMfgIogClassBeingProcessed && mfgIogClassIteration != ITERATION_RM);
                    
                    if (isStraightValuation) {
                        // "straight valuation", items valued directly:
                        
                        // retrieve stock and cost (stock movements' balance):
                        
                        sql = "SELECT SUM(mov_in - mov_out) AS _stk, SUM(debit - credit) AS _bal "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                                + "WHERE NOT b_del AND "
                                + "id_year = " + mnYear + " AND (("
                                + "dt " + (mnPeriod == 1 ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "') OR (" // opening stock is January 1st
                                + "dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                                + "fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_IN_INT[0] + " AND "
                                + "fid_cl_iog < " + SModSysConsts.TRNS_CL_IOG_IN_INT[1] + ")) AND "
                                + "id_item = " + itemId + " AND id_unit = " + unitId + ";";
                        rsStockCosts = stStockCosts.executeQuery(sql);
                        if (rsStockCosts.next()) {
                            stockAcum = rsStockCosts.getDouble("_stk");
                            costAcum = rsStockCosts.getDouble("_bal");
                        }
                        
                        costUnit = SLibUtils.round(stockAcum == 0 ? 0 : costAcum / stockAcum, amtUnitDecs);
                        System.out.println(SLibUtils.textRepeat(" ", 4) + "Unit cost: $" + SLibUtils.getDecimalFormatAmountUnitary().format(costUnit) + ".");
                    }
                    else {
                        // "consolidative valuation", items valued indirectly through manufacturing job order (MFG WP & FG only):
                        
                        // retriebe stock:
                        /*
                        Consider only internal MFG stock movements related to WP or FG (just as corresponds on each iteration).
                        */
                        
                        sql = "SELECT SUM(mov_in - mov_out) AS _stk "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                                + "WHERE NOT b_del AND "
                                + "id_year = " + mnYear + " AND (("
                                + "dt " + (mnPeriod == 1 ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "') OR ("
                                + "dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND "
                                + "fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[0] + " AND "
                                + "fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[1] + " AND "
                                + "fid_tp_iog IN (" + sqlWhereMfgIogTypes + "))) AND "
                                + "id_item = " + itemId + " AND id_unit = " + unitId + ";";
                        rsStockCosts = stStockCosts.executeQuery(sql);
                        if (rsStockCosts.next()) {
                            stockAcum = rsStockCosts.getDouble("_stk");
                        }
                        
                        // retriebe cost (stock movements' balance):
                        /*
                        Consider all RM and WP stock movements asigned to and returned from current item.
                        */
                        
                        sql = "SELECT SUM(s.debit-s.credit) AS _bal "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " AS s "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d ON s.fid_diog_year = d.id_year AND s.fid_diog_doc = d.id_doc "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MFG_ORD) + " AS o ON d.fid_mfg_year_n = o.id_year AND d.fid_mfg_ord_n = o.id_ord "
                                + "WHERE NOT s.b_del AND "
                                + "s.id_year = " + mnYear + " AND (("
                                + "s.dt " + (mnPeriod == 1 ? "<=" : "<") + " '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodBegin) + "') OR ("
                                + "s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(mtPeriodEnd) + "' AND (("
                                + "s.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[0] + " AND s.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_IN_MFG[1] + " AND "
                                + "s.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_IN_MFG_RM_ASD[2] + ", " + SModSysConsts.TRNS_TP_IOG_IN_MFG_WP_ASD[2] + ")) OR ("
                                + "s.fid_ct_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[0] + " AND s.fid_cl_iog = " + SModSysConsts.TRNS_CL_IOG_OUT_MFG[1] + " AND "
                                + "s.fid_tp_iog IN (" + SModSysConsts.TRNS_TP_IOG_OUT_MFG_RM_RET[2] + ", " + SModSysConsts.TRNS_TP_IOG_OUT_MFG_WP_RET[2] + "))))) AND "
                                + "o.fid_item_r = " + itemId + " AND o.fid_unit_r = " + unitId + "; ";
                        rsStockCosts = stStockCosts.executeQuery(sql);
                        if (rsStockCosts.next()) {
                            costAcum = rsStockCosts.getDouble("_bal");
                        }
                        
                        // Update corresponding inventory MFG cost registry:
                        
                        sql = "SELECT qty_wip, qty_fin, qty_fin_eff_per "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + " "
                                + "WHERE id_year = " + mnYear + " AND id_per = " + mnPeriod + " AND id_item = " + itemId + " AND id_unit = " + unitId + "; ";
                        rsUpdates = stUpdates.executeQuery(sql);
                        if (!rsUpdates.next()) {
                            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\n(table = '" + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + "', item = " + itemId + ", unit = " + unitId + ")");
                        }
                        else {
                            double mfgQuantityFinEffPer = rsUpdates.getDouble("qty_fin_eff_per");
                            double mfgAmountMatConsFin = SLibUtils.roundAmount(costAcum * mfgQuantityFinEffPer);
                            double mfgAmountMatConsWip = SLibUtils.roundAmount(costAcum - mfgAmountMatConsFin);
                            double mfgQuantityWip = rsUpdates.getDouble("qty_wip");
                            double mfgQuantityFin = rsUpdates.getDouble("qty_fin");
                            
                            costUnit = SLibUtils.round(mfgQuantityFin == 0 ? 0 : mfgAmountMatConsFin / mfgQuantityFin, amtUnitDecs);    // cost unit remains fixed for all movements
                            System.out.println(SLibUtils.textRepeat(" ", 4) + "Unit cost: $" + SLibUtils.getDecimalFormatAmountUnitary().format(costUnit) + ".");
                            
                            sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_INV_MFG_CST) + " "
                                    + "SET "
                                    // amount of materials consumed:
                                    + "amc = " + costAcum + ", "
                                    + "amc_wip = " + mfgAmountMatConsWip + ", "
                                    + "amc_fin = " + mfgAmountMatConsFin + ", "
                                    /*
                                    // manufacturing overhead:
                                    + "moh = ?,"
                                    + "moh_wip = ?,"
                                    + "moh_fin = ?,"
                                    */
                                    // cost:
                                    + "cst = " + costAcum + ", "
                                    + "cst_wip = " + mfgAmountMatConsWip + ", "
                                    + "cst_fin = " + mfgAmountMatConsFin + ", "
                                    // cost unit:
                                    + "cst_u_wip = " + SLibUtils.round(mfgQuantityWip == 0 ? 0 : mfgAmountMatConsWip / mfgQuantityWip, amtUnitDecs) + ", "
                                    + "cst_u_fin = " + costUnit + " "
                                    + "WHERE id_year = " + mnYear + " AND id_per = " + mnPeriod + " AND id_item = " + itemId + " AND id_unit = " + unitId + "; ";
                            stUpdates.execute(sql);
                            
                            // Create stock cost entries:
                            // XXX maybe this call duplicates cost registration in stock!:
                            //createRawMaterialsCostsStockEntries(new int[] { mnYear, idItem, idItemUnit, 1, manDefaultWarehouseWip[0], manDefaultWarehouseWip[1], 0 }, costAcum);
                        }
                    }
                }
                
                cost = SLibUtils.roundAmount(costUnit * rsMain.getDouble("s.mov_out"));
                
                stockAcum = SLibUtils.round(stockAcum - rsMain.getDouble("s.mov_out"), amtUnitDecs);
                costAcum = SLibUtils.roundAmount(costAcum - cost);   // credit
                
                if (isMfgIogClassBeingProcessed) {
                    itemUnit.Stock = stockAcum;
                    itemUnit.Value = costAcum;
                }
                
                // Update cost of current outgoing stock movement:
                /*
                Registry to be updated is localized straight by PK of stock movement.
                */
                
                sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                        + "SET used = 0, cost_u = " + costUnit + ", cost = 0, debit = 0, credit = " + cost + " "
                        + "WHERE id_year = " + mnYear + " AND id_item = " + itemId + " AND id_unit = " + unitId + " AND id_lot = " + rsMain.getInt("s.id_lot") + " AND "
                        + "id_cob = " + rsMain.getInt("s.id_cob") + " AND id_wh = " + rsMain.getInt("s.id_wh") + " AND id_mov = " + rsMain.getInt("s.id_mov") + "; ";
                stUpdates.execute(sql);
                
                // Update cost of corresponding incoming stock movement (MFG and other internal stock movements only):
                /*
                Registry to be updated is localized indirectly by PK of mirror stock movement.
                Remember that when inner stock movements are saved:
                1. input stock movement (mirror one) is created as system registry with no references to its corresponding original registry;
                2. output stock movement is created (original one) is then created as user registry referencing its corresponding mirror registry.
                */
                
                if (SLibUtils.belongsTo(currentIogClassMove, new int[] { IOG_CLASS_INT, IOG_CLASS_MFG })) {
                    sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                            + "SET used = 0, cost_u = " + costUnit + ", cost = 0, debit = " + cost + ", credit = 0 "
                            + "WHERE id_year = " + mnYear + " AND id_item = " + itemId + " AND id_unit = " + unitId + " AND id_lot = " + rsMain.getInt("s.id_lot") + " AND "
                            + "fid_diog_year = " + rsMain.getInt("d.fid_diog_year_n") + " AND fid_diog_doc = " + rsMain.getInt("d.fid_diog_doc_n") + " AND fid_diog_ety = " + rsMain.getInt("s.fid_diog_ety") + "; ";
                    stUpdates.execute(sql);
                }
            }
            
            // Continue with other stock movement types, while necessary:

            if (isMfgIogClassBeingProcessed) {
                computeConsumptionMovements(itemUnits);
                
                if (++mfgIogClassIteration > ITERATION_FG) {
                    computeConsumptions();   // work-in-progress iteration is finishing, so compute values of consumption movements
                    currentIogClassMove--;
                }
            }
            else {
                currentIogClassMove--;
            }
        }
        
        // Update current inventory valuation registry as finished:
        
        sql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_INV_VAL) + " "
                + "SET b_fin = 1, fk_usr_upd = " + moSession.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                + "WHERE id_inv_val = " + idInventoryValuation + "; ";
        stUpdates.execute(sql);
    }
    
    /**
     * Class to preserve processed item-units, to compute its corresponding consumption moves when finished.
     */
    private class ItemUnit {
        int ItemId;
        int UnitId;
        double Stock;
        double Value;
        
        public ItemUnit(int itemId, int unitId, double stock, double value) {
            ItemId = itemId;
            UnitId = unitId;
            Stock = stock;
            Value = value;
        }
        
        public boolean equals(int itemId, int unitId) {
            return ItemId == itemId && UnitId == unitId;
        }
    }
}
