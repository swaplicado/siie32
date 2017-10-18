package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.mitm.data.SDataItem;
import erp.mmfg.data.SDataProductionOrder;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Edwin Carmona
 */
public abstract class STrnStockSegregationUtils {
    
    private static final int IDX_REF = 2;
    private static final int IDX_ROOT = 5;
    private static final int IDX_QTY_PROD = 6;
    
    private static final int IDX_ID_ITEM = 1;
    private static final int IDX_QTY = 2;
    private static final int IDX_B_EXP = 6;
    
    private static final int IDX_ID_BOM = 0;
    
    public static boolean mbStockRestriction = true;  // XXX this needs to be replaced by system configuration parameter (2017-03-16, ecarmona)
    
    /**
     * Depart from stock the units needed for the production order
     * 
     * @param client
     * @param reference of the origin of segregation PO = [ productionOrderId, productionOrderYear ].
     * @param segregationType type of the origin of segregation.
     * @throws erp.mtrn.data.SStockException throw the SStockException when there are not enough available stock
     */
    public static void segregate(final SClientInterface client, final int [] reference, final int segregationType) throws SStockException {
        SDataStockSegregation segregation = null;
        SServerRequest request = null;
        SServerResponse response = null;
        
        try {
            switch (segregationType) {
                case SDataConstantsSys.TRNS_TP_STK_SEG_SHIP:
                    break;
                    
                case SDataConstantsSys.TRNS_TP_STK_SEG_MFG_ORD:
                    segregation = populateSegregationFromProdOrder(client, reference);
                    segregation = STrnStockSegregationUtils.validateStock(client, segregation);
                    break;                                                                          
            }
            
            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
            request.setPacket(segregation);
            response = client.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(response.getMessage());
            }
            else {
                if (response.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage()));
                }
            }
        }
        catch (SStockException se) {
            throw se;
        }
        catch (Exception e) {
            SLibUtils.showException(client, e);
        }
    }
    
    /**
     * Releases segregated units by segregation
     * 
     * @param client
     * @param reference of the origin of segregation PO = [ productionOrderId, productionOrderYear ].
     * @param segregationType type of the origin of segregation.
     */
    public static void releaseSegregation(final SClientInterface client, final int [] reference, final int segregationType) {
        SDataStockSegregation segregation = new SDataStockSegregation();
        SServerRequest request = null;
        SServerResponse response = null;
        
        try {
            segregation.read(new int [] { STrnStockSegregationUtils.getStockSegregationIdByReference(client.getSession(), reference, segregationType) }, client.getSession().getStatement().getConnection().createStatement());
            segregation.setDeleted(true);
            segregation.setFkUserDeleteId(client.getSession().getUser().getPkUserId());
            segregation.setFkUserEditId(client.getSession().getUser().getPkUserId());
            
            if (!segregation.getIsRegistryNew()) {
                request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
                request.setPacket(segregation);
                response = client.getSessionXXX().request(request);

                if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                    throw new Exception(response.getMessage());
                }
                else {
                    if (response.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage()));
                    }
                }
            }
        }
        catch (Exception ex) {
            SLibUtils.printException(client, ex);
        }
    }
    
    /**
     * Fill object of segregation from a production order
     * 
     * @param client
     * @param reference of the origin of segregation PO = [ productionOrderId, productionOrderYear ].
     * @return the segregation object with de production order values
     * @throws Exception 
     */
    private static SDataStockSegregation populateSegregationFromProdOrder(final SClientInterface client, final int [] reference) throws Exception {
        SDataStockSegregation segregation = new SDataStockSegregation();
        SDataStockSegregationWarehouse warehouse = null;
        SDataStockSegregationWarehouseEntry warehouseEntry = null;
        
        SDataProductionOrder productionOrder = null;
        Vector<Object> items = new Vector<Object>();
        SDataItem item = null;
        Object[] itemAux = null;
        Object[] itemObject = null;
        boolean canExplote = true;
        
        productionOrder = (SDataProductionOrder) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.MFG_ORD, new int[] { (Integer) reference[0], (Integer) reference[1] }, SLibConstants.EXEC_MODE_VERBOSE);
        
        segregation.read(new int[] { STrnStockSegregationUtils.getStockSegregationIdByReference(client.getSession(), reference, SDataConstantsSys.TRNS_TP_STK_SEG_MFG_ORD) }, client.getSession().getStatement());
        
        if (segregation.getPkStockSegregationId() == SLibConstants.UNDEFINED) {
            segregation.setFkStockSegregationTypeId(SDataConstantsSys.TRNS_TP_STK_SEG_MFG_ORD);
            segregation.setFkReference1Id(productionOrder.getPkYearId());
            segregation.setFkReference2Id(productionOrder.getPkOrdId());
            segregation.setFkUserEditId(client.getSession().getUser().getPkUserId());
            segregation.setFkUserNewId(client.getSession().getUser().getPkUserId());
            segregation.setFkUserDeleteId(client.getSession().getUser().getPkUserId());
        }
        segregation.setDeleted(false);
        
        warehouse = new SDataStockSegregationWarehouse();
        warehouse.setPkWarehouseId(productionOrder.getDbmsFkWarehouseId());
	warehouse.setFkCompanyBranchId(productionOrder.getDbmsFkCompanyBranchId());
	warehouse.setFkWarehouseId(productionOrder.getDbmsFkWarehouseId());
        
        segregation.getChildEntries().clear();
        segregation.getChildEntries().add(warehouse);
        
        items = STrnStockSegregationUtils.readItems(client, reference);
        
        itemObject = null;
        for (int indexItem = 0; indexItem < items.size(); indexItem++) {
            itemObject = (java.lang.Object[]) items.get(indexItem);
            canExplote = true;
            
            try {
                // [id_boom, boom, root]
                itemAux = SDataUtilities.isBillOfMaterials((SClientInterface) client, (Integer) itemObject[IDX_ID_ITEM]);

                // Check if item can be explode and is bill of materials (IDX_BOM)
                if ((Boolean) itemObject[IDX_B_EXP] == false && (Integer) itemAux[IDX_ID_BOM] != 0) {
                        //segregate(client, new int [] { moProductionOrder.getFkOrdId_n(), moProductionOrder.getFkOrdYearId_n()  }, SModSysConsts.TRNS_TP_STK_SEG_PROD_ORD);
                    canExplote = false;
                }
            }
            catch (Exception e) {
                // if exception is thrown the vector is initialized and broke the cycle
                SLibUtils.showException(client, e);
                items.removeAllElements();
                break;
            }

            // Sum and add items that not are Bill Of Materials (IDX_BOM):
            if (canExplote) {
                item = (SDataItem) SDataUtilities.readRegistry((SClientInterface) client, SDataConstants.ITMU_ITEM, new int[] { (Integer) itemObject[IDX_ID_ITEM] }, SLibConstants.EXEC_MODE_SILENT);
                warehouseEntry = new SDataStockSegregationWarehouseEntry();
                
                warehouseEntry.setQuantityIncrement(((Double) itemObject[IDX_QTY]));
                warehouseEntry.setFkStockSegregationMovementTypeId(SDataConstantsSys.TRNS_TP_STK_SEG_INC);
                warehouseEntry.setFkYearId(client.getSession().getCurrentYear());
                warehouseEntry.setFkItemId(item.getPkItemId());
                warehouseEntry.setFkUnitId(item.getFkUnitId());
                
                segregation.getChildEntries().get(0).getChildEntries().add(warehouseEntry);
            }
        }
            
        return segregation;
    }
    
    /**
     * Return the segregation id corresponding to the reference, if it does not find it, return 0
     * 
     * @param session
     * @param reference of the origin of segregation PO = [ productionOrderId, productionOrderYear ].
     * @param segregationType type of the origin of segregation.
     * @return the id of segregation that corresponds to the reference, if it exists.
     * @throws Exception 
     */
    public static int getStockSegregationIdByReference(final SGuiSession session, final int reference[], final int segregationType) throws Exception {
        ResultSet resultSet = null;
        String sql = "";
        int id = 0;
        
        sql = "SELECT id_stk_seg " +
                "FROM trn_stk_seg " +
                "WHERE fid_ref_1 = " + reference[0] + " AND fid_ref_2 = " + reference[1] + " AND fid_tp_stk_seg = " + segregationType +"; ";
        
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }
        
        return id;
    }
    
    /**
     * Obtains all stocks, including segregations
     * 
     * @param client
     * @param stockMoveParams contains the attributes needed for the query.
     * @return STrnStock object with de values of stock segregated and net existence
     * @throws Exception 
     */
    public static STrnStock getAllStock(final SClientInterface client, final STrnStockMove stockMoveParams) throws Exception {
        STrnStock stock = null;
        STrnStock segregatedStock = null;
        
        stock = getStock(client, stockMoveParams);
        
        segregatedStock = getStockSegregated(client, stockMoveParams);
        stock.setSegregationIncreases(segregatedStock.getSegregationIncreases());
        stock.setSegregationDecreases(segregatedStock.getSegregationDecreases());
            
        return stock;
    }
    
    /**
     * Obtains stocks ignoring segregations
     * 
     * @param client
     * @param stockMoveParams contains the attributes needed for the filter.
     * @return The STrnStock object containing the values obtained.
     * @throws Exception 
     */
    public static STrnStock getStock(final SClientInterface client, final STrnStockMove stockMoveParams) throws Exception {
        STrnStock stock = new STrnStock();
        ResultSet result = null;
        
        String sql = "SELECT COALESCE(SUM(stk.mov_in), 0) AS f_mov_in, COALESCE(SUM(stk.mov_out), 0) AS f_mov_out " +
                "FROM trn_stk AS stk " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON stk.id_cob = ent.id_cob AND stk.id_wh = ent.id_ent " +
                "WHERE NOT ent.b_del AND NOT stk.b_del AND stk.id_year = " + stockMoveParams.getPkYearId() + " AND stk.id_item = " + stockMoveParams.getPkItemId() + " AND stk.id_unit = " + stockMoveParams.getPkUnitId() + " ";
        
        if (stockMoveParams.getPkCompanyBranchId() != 0) {
            sql += "AND stk.id_cob = " + stockMoveParams.getPkCompanyBranchId() + " ";
        }
        if (stockMoveParams.getPkWarehouseId()!= 0) {
            sql += "AND stk.id_wh = " + stockMoveParams.getPkWarehouseId() + " ";
        }
        if (stockMoveParams.getWarehouseType() != 0) {
            sql += "AND ent.fid_tp_ent = " + stockMoveParams.getWarehouseType() + " ";
        }
        if (stockMoveParams.getPkLotId() != 0) {
            sql += "AND stk.id_lot = " + stockMoveParams.getPkLotId() + " ";
        }
        
        try {
            result = client.getSession().getStatement().executeQuery(sql);
            
            if (result.next()) {
                stock.setMovementIn(result.getDouble("f_mov_in"));
                stock.setMovementOut(result.getDouble("f_mov_out"));
            }
            
        }
        catch (SQLException ex) {
            SLibUtils.printException(client, ex);
        }
        
        return stock;
    }
    
    /**
     * Obtain stocks segregated by a production order or shipment, this method receives its reference.
     * The STrnStockMove object is used to pass the filtering parameters
     * 
     * @param client
     * @param stockMoveParams contains the attributes needed for the query.
     * @return STrnStock object with the values of segregated stock.
     * @throws Exception 
     */
    public static STrnStock getStkSegregatedByReference(final SClientInterface client, final STrnStockMove stockMoveParams) throws Exception {
        stockMoveParams.setIsCurrentSegregationExcluded(false);
        
        return getStockSegregated(client, stockMoveParams);
    }
    
    /**
     * Obtain the segregated stocks.
     * The STrnStockMove object is used to pass the filtering parameters
     * 
     * @param client
     * @param stockMoveParams contains the attributes needed for the query.
     * @return STrnStock object with the values of segregated stock.
     * @throws Exception
     */
    public static STrnStock getStockSegregated(final SClientInterface client, final STrnStockMove stockMoveParams) throws Exception {
        STrnStock segregatedStock = new STrnStock(); 
        ResultSet result = null;
        
        if (stockMoveParams.getSegregationId() == 0 && stockMoveParams.getSegregationReference() != null) {
            stockMoveParams.setSegregationId(STrnStockSegregationUtils.getStockSegregationIdByReference(client.getSession(), stockMoveParams.getSegregationReference(), stockMoveParams.getSegregationType()));
        }
        
        String sql = "SELECT COALESCE(SUM(wety.qty_inc), 0) AS f_inc_seg, COALESCE(SUM(wety.qty_dec), 0) AS f_dec_seg " +
                "FROM trn_stk_seg_whs swhs " +
                "INNER JOIN trn_stk_seg_whs_ety wety ON swhs.id_stk_seg = wety.id_stk_seg AND swhs.id_whs = wety.id_whs " +
                "WHERE fid_year = " + stockMoveParams.getPkYearId() + " AND fid_item = " + stockMoveParams.getPkItemId() + " AND fid_unit = " + stockMoveParams.getPkUnitId() + " ";
        
        if (stockMoveParams.getPkCompanyBranchId() != 0) {
            sql += "AND swhs.fid_cob = " + stockMoveParams.getPkCompanyBranchId() + " ";
        }
        
        if (stockMoveParams.getPkWarehouseId() != 0) {
            sql += "AND swhs.fid_whs = " + stockMoveParams.getPkWarehouseId() + " ";
        }
        
        if (stockMoveParams.getSegregationId() != 0) {
            sql += " AND wety.id_stk_seg " + (stockMoveParams.getIsCurrentSegregationExcluded() ? "<> " : "= ") + stockMoveParams.getSegregationId();
        }
        
        result = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);

        if (result.next()) {
            segregatedStock.setSegregationIncreases(result.getDouble("f_inc_seg"));
            segregatedStock.setSegregationDecreases(result.getDouble("f_dec_seg"));
        }
        
        return segregatedStock;
    }

    /**
     * Validate if there is enough stock for segregation, if not, only segregate what is available
     * 
     * @param client
     * @param segregation object with the amounts to evaluate
     * @return the SDataStockSegregation object with de values to be segregated
     * @throws erp.mtrn.data.SStockException throw the SStockException when there are not enough available stock
     * @throws Exception 
     */
    public static SDataStockSegregation validateStock(final SClientInterface client, final SDataStockSegregation segregation) throws SStockException, Exception {
        STrnStockMove stockMoveParams = null;
        STrnStock stock = null;
        SDataStockSegregation currentSegregation = segregation;
        
        if (currentSegregation != null) {
            for (SDataStockSegregationWarehouse warehouse : currentSegregation.getChildEntries()) {
                for (SDataStockSegregationWarehouseEntry warehouseEntry : warehouse.getChildEntries()) {
                    stockMoveParams = new STrnStockMove();
                    stockMoveParams.setPkItemId(warehouseEntry.getFkItemId());
                    stockMoveParams.setPkUnitId(warehouseEntry.getFkUnitId());
                    stockMoveParams.setPkYearId(warehouseEntry.getFkYearId());
                    stockMoveParams.setPkCompanyBranchId(warehouse.getFkCompanyBranchId());
                    stockMoveParams.setPkWarehouseId(warehouse.getPkWarehouseId());
                    stockMoveParams.setSegregationId(currentSegregation.getPkStockSegregationId());
                    stockMoveParams.setIsCurrentSegregationExcluded(true);
                    
                    stock = STrnStockSegregationUtils.getAllStock(client, stockMoveParams);
                    
                    if (stock.getAvailableStock() < warehouseEntry.getQuantityIncrement()) {
                        if (mbStockRestriction) {
                            throw new SStockException("No hay existencias disponibles suficientes para realizar la segregación.");
                        }
                        else {
                            warehouseEntry.setQuantityIncrement(stock.getAvailableStock());
                        }
                    }
                }
            }
        }
        
        return currentSegregation;
    }

    /**
     * Read the items in a production order
     * 
     * @param client
     * @param productionOrderPk primary key of production order that contains the items. PO = [ prodOrderId, year]
     * @return a Vector object with the items
        * 0 f_item
        * 1 id_item
        * 2 f_qty
        * 3 t.id_bom
        * 4 t.ts_start
        * 5 t.level
        * 6 t.b_exp
        * 7 t.fid_unit
        * 8 t.item
        * 9 u.symbol
        * 10 t.b_req
     */
    public static Vector readItems(final SClientInterface client, final int[] productionOrderPk) {
        Vector<Object> paramsProductionOrder = new Vector<Object>();
        Vector<Object> items = new Vector<Object>();
        Object[] productionOrderData = null;
        
        paramsProductionOrder.removeAllElements();
        
        /**
         *  paramsProductionOrder
         * 
         *  IN i_Type SMALLINT UNSIGNED,
         *  IN i_FromYearId INTEGER UNSIGNED,
         *  IN i_FromId INTEGER UNSIGNED,
         *  IN i_ToYearId INTEGER UNSIGNED,
         *  IN i_ToId INTEGER UNSIGNED,
         *  IN i_IsForecast BOOLEAN,
         */
        
        paramsProductionOrder.add(1);
        paramsProductionOrder.add(productionOrderPk[0]);
        paramsProductionOrder.add(productionOrderPk[1]);
        paramsProductionOrder.add(productionOrderPk[0]);
        paramsProductionOrder.add(productionOrderPk[1]);
        paramsProductionOrder.add(false);
        paramsProductionOrder = SDataUtilities.callProcedure((SClientInterface) client, SProcConstants.MFG_ORD_ITEM_QRY, paramsProductionOrder, SLibConstants.EXEC_MODE_SILENT);
        
        /*
        returned paramsProductionOrder: 
        
            0 = id_year
            1 = id_ord
            2 = ref
            3 = id_bom
            4 = bom
            5 = root
            6 = qty
            7 = symbol
            8 = dt_dly
            9 = chgs
        */
        
        productionOrderData = (Object[]) paramsProductionOrder.get(0);
        
        /*
        * productionOrderData:
        *
        * i_root
        * i_ref
        * i_qty
        * i_id_year
        * i_id_ord
        * i_tmpTable
        * i_cfgs_tp_sort_key_name
        */
        
        items.removeAllElements();
        items.add((Integer) productionOrderData[IDX_ROOT]);
        items.add(productionOrderData[IDX_REF].toString());
        items.add((Double) productionOrderData[IDX_QTY_PROD]);
        items.add(0);
        items.add(0);
        items.add("Ttmp");
        items.add(((SClientInterface) client).getSessionXXX().getParamsErp().getFkSortingItemTypeId());
        
        return SDataUtilities.callProcedure((SClientInterface) client, SProcConstants.MFG_BOM_EXP_GREQ, items, SLibConstants.EXEC_MODE_SILENT);
    }
    
    /**
     * validate if the year of segregation is equal to year of inventory movements
     * 
     * @param connection
     * @param segregationId segregation primary key id
     * @param yearId Year of inventory movements
     * @return True if the year of segregation is equal to year of inventory movements
     * @throws SQLException 
     */
    public static boolean isValidYear(final java.sql.Connection connection, final int segregationId, final int yearId) throws SQLException {
        boolean isValidYear = true;
        ResultSet result = null;
        String sqlStkSeg = "SELECT * FROM trn_stk_seg_whs_ety AS wety WHERE id_stk_seg = " + segregationId + " AND " + " fid_year <> " + yearId;
            
        result = connection.createStatement().executeQuery(sqlStkSeg);

        if (result.next()) {
            isValidYear = false;
        }
        
        return isValidYear;
    }  
}
