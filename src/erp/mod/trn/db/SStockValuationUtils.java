/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValuationUtils {
    
    /**
     * The function retrieves a stock valuation configuration object from a JSON string.
     * 
     * @param statement The "statement" parameter is an object of type "Statement". It is used as a
     * parameter to retrieve a configuration value from a system parameter using the
     * "SCfgUtils.getParamValue()" method. The retrieved configuration value is then deserialized into
     * an object of type "SStockConfiguration" using the Jackson
     * @return The method is returning an object of type SStockConfiguration.
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public static SStockValuationConfiguration getStockValuationConfig(final Statement statement) throws JsonProcessingException, Exception {
        String sCfg = SCfgUtils.getParamValue(statement, SDataConstantsSys.CFG_PARAM_TRN_STK_MAT_VAL);
        ObjectMapper mapper = new ObjectMapper();
        SStockValuationConfiguration oCfg = mapper.readValue(sCfg, SStockValuationConfiguration.class);
        
        return oCfg;
    }
    
    /**
     * The function returns a SQL query to retrieve stock results based on certain parameters.
     * 
     * @param statement The `statement` parameter is an instance of the `Statement` class, which is
     * used to execute SQL statements and retrieve results from a database.
     * @param diogCategory The diogCategory parameter represents the category of inventory movements.
     * It is an integer value that determines whether the inventory movements are for incoming or
     * outgoing transactions. The value 1 represents incoming transactions, while the value 2
     * represents outgoing transactions.
     * @param startDate The start date of the stock result query. It specifies the beginning of the
     * date range for which you want to retrieve stock results.
     * @param cutDate The cutDate parameter is the date used to determine the end of the time period
     * for the stock query.
     * @return The method is returning a SQL query string.
     */
    private static String getDiogsQuery(final Statement statement, final int diogCategory, final Date startDate, final Date cutDate) throws Exception {
        String sql = "SELECT  " +
                        " de.*, "
                        + "d.dt, "
                        + "d.fid_ct_iog, "
                        + "d.fid_cl_iog, "
                        + "d.fid_tp_iog, "
                        + "d.fid_cob, "
                        + "d.fid_wh, "
                        + "mre.fk_item_ref_n AS ref_ety, "
                        + "mr.fk_item_ref_n ref_rm " +
                        "FROM " +
                        SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " d " +
                        " INNER JOIN " +
                        SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " de ON d.id_year = de.id_year AND d.id_doc = de.id_doc " +
                        " LEFT JOIN " +
                        SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " mre ON de.fid_mat_req_n = mre.id_mat_req AND de.fid_mat_req_ety_n = mre.id_ety " +
                        " LEFT JOIN " +
                        SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " mr ON d.fid_mat_req_n = mr.id_mat_req " +
                        "WHERE NOT d.b_del AND NOT de.b_del " +
                        "    AND d.id_year = YEAR('" + SLibUtils.DbmsDateFormatDate.format(cutDate) + "') " +
                        "    AND d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(startDate) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(cutDate) + "' " +
                        "    AND fid_ct_iog = " + diogCategory + " ";
        
        SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(statement);
        ArrayList<int[]> iogTpmovs;
        if (diogCategory == SModSysConsts.TRNS_CT_IOG_IN) {
            iogTpmovs = oCfg.getIogTpmovsIn();
        }
        else {
            iogTpmovs = oCfg.getIogTpmovsOut();
        }
        
        sql += "AND de.qty > 0 ";
        
        if (iogTpmovs.isEmpty()) {
            throw new Exception("No existe configuración de movimientos de almacén para la valuación");
        }
        
        sql += "AND (";
        
        for (int[] iogTpmov : iogTpmovs) {
            sql += "(d.fid_cl_iog = " + iogTpmov[1] + " AND d.fid_tp_iog = " + iogTpmov[2]  + ") OR";
        }
        
        sql = sql.substring(0, sql.length() - 2);
        
        sql += ") " +
                "ORDER BY d.dt ASC, de.id_doc ASC, de.id_ety ASC";
        
        return sql;
    }
    
    /**
     * The function creates stock valuation entries based on a given category, start date, cut date,
     * and valuation ID.
     * 
     * @param session The session parameter is an instance of the SGuiSession class, which represents
     * the user session in the application.
     * @param diogCategory The diogCategory parameter represents the category of the document related
     * to the stock movement. It is used to filter the stock result query and retrieve only the entries
     * with the specified category.
     * @param startDate The start date is the date from which the stock valuation entries should be
     * created. It is used to filter the results of the query that retrieves the stock movements.
     * @param cutDate The cutDate parameter is the date until which the stock valuation entries will be
     * created. It is used to filter the results of the SQL query that retrieves the stock information.
     * Only the stock movements that occurred before or on the cutDate will be considered for creating
     * the entries.
     * @param idValuation The idValuation parameter is an integer value representing the ID of the
     * stock valuation.
     * @throws java.lang.Exception
     */
    public static void createEntries(SGuiSession session, final int diogCategory, final Date startDate, final Date cutDate, final int idValuation) throws Exception {
        String sql = SStockValuationUtils.getDiogsQuery(session.getStatement().getConnection().createStatement(), diogCategory, startDate, cutDate);
        
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oEntry;
        while (res.next()) {
            oEntry = new SDbStockValuationMvt();

            oEntry.setDateMove(res.getDate("dt"));
            oEntry.setQuantityEntry(res.getDouble("qty"));
            oEntry.setCostUnitary(res.getDouble("val_u"));
            oEntry.setCost_r(res.getDouble("val"));
            oEntry.setFkItemId(res.getInt("fid_item"));
            oEntry.setFkUnitId(res.getInt("fid_unit"));
            oEntry.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_IN);
            oEntry.setFkDiogYear(res.getInt("id_year"));
            oEntry.setFkDiogDocId(res.getInt("id_doc"));
            oEntry.setFkDiogEntryId(res.getInt("id_ety"));
            oEntry.setFkItemReference_n(res.getInt("ref_ety") == 0 ? res.getInt("ref_rm") : res.getInt("ref_ety"));
            if (oEntry.getFkItemReference_n() == 0) {
              oEntry.setFkItemReference_n(res.getInt("fid_item"));
            }
            oEntry.setFkStockValuationId(idValuation);

            oEntry.setAuxWarehousePk(new int[] { res.getInt("fid_cob"), res.getInt("fid_wh") });
            oEntry.setFkUserInsertId(session.getUser().getPkUserId());
            
            oEntry.save(session);
            System.out.println("Entry mvt: " + oEntry.getDateMove().toString());
        }
    }

    /**
     * This function retrieves a list of stock valuation movement entries that have not been consumed.
     * 
     * @param session The session parameter is an object of type SGuiSession, which represents the user
     * session in the application. It is used to access the database connection and execute the SQL
     * query.
     * @return The method is returning an ArrayList of SDbStockValuationMvt objects.
     */
    private static ArrayList<SDbStockValuationMvt> getNotConsumedEntries(SGuiSession session) throws Exception {
        String sql = "SELECT " +
               "ve.id_stk_val_mvt, " +
               "ve.fk_stk_val, " +
               "SUM(ve.qty_entry - ve.qty_cons_r) AS qty, " +
               "ve.dt_mov, " +
               "ve.fk_item, " +
               "ve.fk_unit, " +
               "ve.cost_u, " +
               "ve.fk_item_ref_n, " +
               "td.fid_cob," +
               "td.fid_wh " +
            "FROM " +
            SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS ve " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS td ON (ve.fk_diog_year = td.id_year AND ve.fk_diog_doc = td.id_doc) " +
            "WHERE " +
            "NOT ve.b_del " +
            "GROUP BY ve.dt_mov, ve.fk_stk_val, td.fid_cob, td.fid_wh, ve.fk_item, ve.fk_unit, ve.cost_u, ve.fk_item_ref_n " +
            "HAVING qty > 0 " +
            "ORDER BY dt_mov ASC, id_stk_val_mvt ASC;";
      
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oEntry;
        ArrayList<SDbStockValuationMvt> entries = new ArrayList<>();
        while (res.next()) {
            oEntry = new SDbStockValuationMvt();

            oEntry.setPkStockValuationMvtId(res.getInt("id_stk_val_mvt"));
            oEntry.setDateMove(res.getDate("dt_mov"));
            oEntry.setQuantityEntry(res.getDouble("qty"));
            oEntry.setCostUnitary(res.getDouble("cost_u"));
            oEntry.setFkItemId(res.getInt("fk_item"));
            oEntry.setFkUnitId(res.getInt("fk_unit"));
            oEntry.setFkItemReference_n(res.getInt("fk_item_ref_n"));
            oEntry.setFkStockValuationId(res.getInt("fk_stk_val"));
            oEntry.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_IN);

            oEntry.setAuxWarehousePk(new int[] { res.getInt("fid_cob"), res.getInt("fid_wh") });

            entries.add(oEntry);
        }

        return entries;
    }
    
    /**
     * The function consumes stock valuation entries based on certain criteria and returns a list of
     * consumed entries.
     * 
     * @param session The session parameter is an object of type SGuiSession, which represents the user
     * session in the application.
     * @param diogCategory The diogCategory parameter represents the category of the document related
     * to the stock movement. It is an integer value that determines the type of document, such as a
     * purchase order, sales order, or inventory adjustment.
     * @param startDate The start date is the date from which the stock valuation movement entries
     * should be considered for consumption.
     * @param cutDate The cutDate parameter is the date until which the stock valuation entries should
     * be considered. It is used to filter the entries based on their date.
     * @param idValuation The parameter `idValuation` is an integer that represents the ID of the stock
     * valuation.
     * @return The method is returning an ArrayList of SDbStockValuationMvt objects.
     */
    public static ArrayList<SDbStockValuationMvt> consumeEntries(SGuiSession session, final int diogCategory, final Date startDate, final Date cutDate, final int idValuation) throws Exception {
        String sql = SStockValuationUtils.getDiogsQuery(session.getStatement().getConnection().createStatement(), diogCategory, startDate, cutDate);
        ArrayList<SDbStockValuationMvt> entries = SStockValuationUtils.getNotConsumedEntries(session);
        ArrayList<SDbStockValuationMvt> consumptions = new ArrayList<>();

        try (ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
          while (res.next()) {
            double qtyToConsume = res.getDouble("qty");
            for (SDbStockValuationMvt entry : entries) {
              if (res.getInt("fid_item") == entry.getFkItemId() && res.getInt("fid_unit") == entry.getFkUnitId() && !entry.isAuxConsumed()) {
                double consumeQuantity = 0d;
                double entryAvailableQuantity = entry.getQuantityEntry() - entry.getAuxConsumption();
                if (qtyToConsume >= entryAvailableQuantity) {
                  entry.setAuxConsumed(true);
                  entry.setAuxConsumption(entry.getAuxConsumption() + entryAvailableQuantity);
                  consumeQuantity = entryAvailableQuantity;
                  qtyToConsume = qtyToConsume - entryAvailableQuantity;
                }
                else {
                  entry.setAuxConsumption(entry.getAuxConsumption() + qtyToConsume);
                  consumeQuantity = qtyToConsume;
                  qtyToConsume = 0d;
                }

                SDbStockValuationMvt oConsumption = new SDbStockValuationMvt();
                oConsumption.setDateMove(res.getDate("dt"));
                oConsumption.setQuantityConsumption_r(consumeQuantity);
                oConsumption.setCostUnitary(entry.getCostUnitary());
                oConsumption.setCost_r(SLibUtils.roundAmount(consumeQuantity * oConsumption.getCostUnitary()));
                oConsumption.setFkItemId(res.getInt("fid_item"));
                oConsumption.setFkUnitId(res.getInt("fid_unit"));
                oConsumption.setFkDiogYear(res.getInt("id_year"));
                oConsumption.setFkDiogDocId(res.getInt("id_doc"));
                oConsumption.setFkDiogEntryId(res.getInt("id_ety"));
                oConsumption.setFkItemReference_n(res.getInt("ref_ety") == 0 ? res.getInt("ref_rm") : res.getInt("ref_ety"));
                if (oConsumption.getFkItemReference_n() == 0) {
                    oConsumption.setFkItemReference_n(res.getInt("fid_item"));
                }
                oConsumption.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_OUT);
                oConsumption.setFkStockValuationId(idValuation);
                oConsumption.setFkStockValuationMvtId_n(entry.getPkStockValuationMvtId());
                oConsumption.setAuxWarehousePk(entry.getAuxWarehousePk());
                oConsumption.setFkUserInsertId(session.getUser().getPkUserId());
                consumptions.add(oConsumption);

                if (qtyToConsume == 0d) {
                  break;
                }
              }
            }

            if (qtyToConsume > 0d) {
              throw new Exception("No hay suficiente stock para consumir.");
            }
          }
        }

        return consumptions;
    }
    
    /**
     * The function `deleteValuation` deletes a valuation record and related records from the database.
     * 
     * @param session The session parameter is an object of type SGuiSession, which represents the user
     * session in the application. It is used to access the database connection and execute SQL
     * queries.
     * @param idValuation The idValuation parameter is an integer that represents the ID of the
     * valuation to be deleted.
     * @return The method is returning a boolean value of true.
     * @throws java.sql.SQLException
     */
    public static boolean deleteValuation(SGuiSession session, final int idValuation) throws SQLException {
        String sql = "SELECT  " +
                    "    * " +
                    "FROM " +
                    "  " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " " +
                    "WHERE " +
                    "    fk_stk_val = " + idValuation + ";";
        
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        String delRecEtys = "";
        while (res.next()) {
            delRecEtys = "UPDATE "
                    + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " "
                    + "SET b_del = 1,"
                    + "sort_pos = 0 "
                    + "WHERE (id_year = " + res.getInt("fk_fin_rec_year") + ") "
                                                + "AND (id_per = " + res.getInt("fk_fin_rec_per") + ") "
                                                + "AND (id_bkc = " + res.getInt("fk_fin_rec_bkc") + ") "
                                                + "AND (id_tp_rec = '" + res.getString("fk_fin_rec_tp_rec") + "') "
                                                + "AND (id_num = " + res.getInt("fk_fin_rec_num") + ") "
                                                + "AND (id_ety = " + res.getInt("fk_fin_rec_ety") + ");";
            
            session.getStatement().getConnection().createStatement().executeUpdate(delRecEtys);
        }
        
        String sqlDelLinks = "UPDATE " +
                            SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " " +
                            "SET b_del = 1 " +
                            "WHERE " +
                            "    fk_stk_val = " + idValuation + ";";
                            
        session.getStatement().getConnection().createStatement().executeUpdate(sqlDelLinks);
        
        String sqlDelEtys = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " SET b_del = 1 "
                + "WHERE fk_stk_val = " + idValuation + ";";
        
        session.getStatement().getConnection().createStatement().executeUpdate(sqlDelEtys);
        
        return true;
    }
}
