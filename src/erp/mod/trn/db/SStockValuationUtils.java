/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValuationUtils {
    
    static final int DEBIT = 1;
    static final int CREDIT = 2;
    
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
    private static String getStockQuery(final Statement statement, final int diogCategory, final Date startDate, final Date cutDate) throws Exception {
        String sql = "SELECT  "
                + "    stk.*, "
                + "    mre.fk_item_ref_n AS ref_ety, "
                + "    mr.fk_item_ref_n ref_rm, "
                + "    de.fid_mat_req_n, "
                + "    de.fid_mat_req_ety_n "
                + "FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " stk "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " d ON stk.fid_diog_year = d.id_year "
                + "        AND stk.fid_diog_doc = d.id_doc "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " de ON stk.fid_diog_year = de.id_year "
                + "        AND stk.fid_diog_doc = de.id_doc "
                + "        AND stk.fid_diog_ety = de.id_ety "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " mre ON de.fid_mat_req_n = mre.id_mat_req "
                + "        AND de.fid_mat_req_ety_n = mre.id_ety "
                + "        LEFT JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " mr ON d.fid_mat_req_n = mr.id_mat_req "
                + "WHERE "
                + "    NOT d.b_del AND NOT de.b_del AND NOT stk.b_del "
                + "    AND stk.id_year = YEAR('" + SLibUtils.DbmsDateFormatDate.format(cutDate) + "') "
                + "    AND stk.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(startDate) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(cutDate) + "' "
                + "    AND stk.fid_ct_iog = " + diogCategory + " ";

        SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(statement);
        ArrayList<int[]> iogTpmovs;
        if (diogCategory == SModSysConsts.TRNS_CT_IOG_IN) {
            iogTpmovs = oCfg.getIogTpmovsIn();
            sql += "AND stk.mov_in > 0 ";
        }
        else {
            iogTpmovs = oCfg.getIogTpmovsOut();
            sql += "AND stk.mov_out > 0 ";
        }

        if (iogTpmovs.isEmpty()) {
            throw new Exception("No existe configuración de movimientos de almacén para la valuación");
        }

        sql += "AND (";

        for (int[] iogTpmov : iogTpmovs) {
            sql += "(stk.fid_cl_iog = " + iogTpmov[1] + " AND stk.fid_tp_iog = " + iogTpmov[2] + ") OR";
        }

        sql = sql.substring(0, sql.length() - 2);

        sql += ") "
                + "ORDER BY stk.dt ASC, de.id_doc ASC, de.id_ety ASC";

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
        String sql = SStockValuationUtils.getStockQuery(session.getStatement().getConnection().createStatement(), diogCategory, startDate, cutDate);
        
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oEntry;
        while (res.next()) {
            oEntry = new SDbStockValuationMvt();

            oEntry.setDateMove(res.getDate("dt"));
            oEntry.setQuantityMovement(res.getDouble("mov_in"));
            oEntry.setCostUnitary(res.getDouble("cost_u"));
            oEntry.setCost_r(res.getDouble("debit"));
            oEntry.setFkStockValuationId(idValuation);
            oEntry.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_IN);
            oEntry.setFkDiogYearInId(res.getInt("fid_diog_year"));
            oEntry.setFkDiogDocInId(res.getInt("fid_diog_doc"));
            oEntry.setFkDiogEntryInId(res.getInt("fid_diog_ety"));
            oEntry.setFkItemId(res.getInt("id_item"));
            oEntry.setFkUnitId(res.getInt("id_unit"));
            oEntry.setFkLotId(res.getInt("id_lot"));
            oEntry.setFkCompanyBranchId(res.getInt("id_cob"));
            oEntry.setFkWarehouseId(res.getInt("id_wh"));
            oEntry.setFkUserInsertId(session.getUser().getPkUserId());
            
            oEntry.save(session);
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
    private static ArrayList<SDbStockValuationMvt> getNotConsumedEntries(SGuiSession session, final int idYear) throws Exception {
        String sql = "SELECT "
                + "ve.id_stk_val_mvt, "
                + "ve.fk_stk_val, "
                + "SUM(IF(ve.fk_ct_iog = 1, ve.qty_mov, ve.qty_mov * - 1)) AS qty, "
                + "ve.dt_mov, "
                + "ve.fk_diog_year_in , "
                + "ve.fk_diog_doc_in , "
                + "ve.fk_diog_ety_in, "
                + "ve.fk_item, "
                + "ve.fk_unit, "
                + "ve.fk_lot, "
                + "ve.cost_u, "
                + "ve.fk_cob,"
                + "ve.fk_wh "
                + "FROM "
                + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS ve "
                + "WHERE "
                + "NOT ve.b_del AND ve.fk_diog_year_in = " + idYear + " "
                + "GROUP BY ve.fk_cob , ve.fk_wh , ve.fk_item , ve.fk_unit , ve.fk_lot , ve.cost_u , ve.fk_diog_year_in , ve.fk_diog_doc_in , ve.fk_diog_ety_in, ve.fk_stk_val "
                + "HAVING qty > 0 "
                + "ORDER BY ve.dt_mov ASC , ve.fk_stk_val ASC";
      
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oEntry;
        ArrayList<SDbStockValuationMvt> entries = new ArrayList<>();
        while (res.next()) {
            oEntry = new SDbStockValuationMvt();

            oEntry.setDateMove(res.getDate("dt_mov"));
            oEntry.setQuantityMovement(res.getDouble("qty"));
            oEntry.setCostUnitary(res.getDouble("cost_u"));
            oEntry.setPkStockValuationMvtId(res.getInt("id_stk_val_mvt"));
            oEntry.setFkStockValuationId(res.getInt("fk_stk_val"));
            oEntry.setFkDiogYearInId(res.getInt("fk_diog_year_in"));
            oEntry.setFkDiogDocInId(res.getInt("fk_diog_doc_in"));
            oEntry.setFkDiogEntryInId(res.getInt("fk_diog_ety_in"));
            oEntry.setFkItemId(res.getInt("fk_item"));
            oEntry.setFkUnitId(res.getInt("fk_unit"));
            oEntry.setFkLotId(res.getInt("fk_lot"));
            oEntry.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_IN);
            oEntry.setFkCompanyBranchId(res.getInt("fk_cob"));
            oEntry.setFkWarehouseId(res.getInt("fk_wh"));

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
     * @throws java.lang.Exception
     */
    public static ArrayList<SDbStockValuationMvt> consumeEntries(SGuiSession session, final int diogCategory, final Date startDate, final Date cutDate, final int idValuation) throws Exception {
        String sql = SStockValuationUtils.getStockQuery(session.getStatement().getConnection().createStatement(), diogCategory, startDate, cutDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int idYear = calendar.get(Calendar.YEAR);
        ArrayList<SDbStockValuationMvt> entries = SStockValuationUtils.getNotConsumedEntries(session, idYear);
        ArrayList<SDbStockValuationMvt> lConsumptions = new ArrayList<>();
        ArrayList<SDbStockValuationMvt> lTempConsumptions = new ArrayList<>();

        try (ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql)) {
            while (res.next()) {
                double qtyDiogEty = res.getDouble("mov_out");
                double qtyToConsume = qtyDiogEty;
                lTempConsumptions.clear();
                for (SDbStockValuationMvt entry : entries) {
                    if (res.getInt("id_item") == entry.getFkItemId() && res.getInt("id_unit") == entry.getFkUnitId() && ! entry.isAuxConsumed()) {
                        double consumeQuantity = 0d;
                        double entryAvailableQuantity = entry.getQuantityMovement() - entry.getAuxConsumption();
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
                        oConsumption.setQuantityMovement(consumeQuantity);
                        oConsumption.setCostUnitary(entry.getCostUnitary());
                        oConsumption.setCost_r(SLibUtils.roundAmount(consumeQuantity * oConsumption.getCostUnitary()));
                        oConsumption.setFkItemId(res.getInt("id_item"));
                        oConsumption.setFkUnitId(res.getInt("id_unit"));
                        oConsumption.setFkLotId(res.getInt("id_lot"));
                        oConsumption.setFkDiogYearInId(entry.getFkDiogYearInId());
                        oConsumption.setFkDiogDocInId(entry.getFkDiogDocInId());
                        oConsumption.setFkDiogEntryInId(entry.getFkDiogEntryInId());
                        oConsumption.setFkDiogYearOutId_n(res.getInt("fid_diog_year"));
                        oConsumption.setFkDiogDocOutId_n(res.getInt("fid_diog_doc"));
                        oConsumption.setFkDiogEntryOutId_n(res.getInt("fid_diog_ety"));
                        oConsumption.setFkDiogCategoryId(SModSysConsts.TRNS_CT_IOG_OUT);
                        oConsumption.setFkStockValuationId(idValuation);
                        oConsumption.setFkStockValuationMvtId_n(entry.getPkStockValuationMvtId());

                        oConsumption.setFkCompanyBranchId(res.getInt("id_cob"));
                        oConsumption.setFkWarehouseId(res.getInt("id_wh"));
                        oConsumption.setFkUserInsertId(session.getUser().getPkUserId());
                        
                        if (res.getInt("fid_mat_req_n") > 0) {
                            oConsumption.setFkMaterialRequestId_n(res.getInt("fid_mat_req_n"));
                            oConsumption.setFkMaterialRequestEntryId_n(res.getInt("fid_mat_req_ety_n"));
                        }
                        
                        lTempConsumptions.add(oConsumption);

                        if (qtyToConsume == 0d) {
                            break;
                        }
                    }
                }

                if (qtyToConsume > 0d) {
//                    throw new Exception("No hay suficiente stock para consumir.");
                    System.out.println("WARNING: No hay suficiente stock para consumir. ID_YEAR = " + (res.getInt("fid_diog_year")) + ", "
                            + "ID_DOC = " + (res.getInt("fid_diog_doc")) + ", ID_ETY = " + res.getInt("fid_diog_ety"));
                }
                else {
                    lConsumptions.addAll(lTempConsumptions);
                }
            }
        }

        return lConsumptions;
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
    
    /**
     * The function checks if a stock valuation can be created based on a given date.
     * 
     * @param session The session parameter is an object of type SGuiSession, which represents the user
     * session in the application. It is used to access the database connection and execute SQL
     * queries.
     * @param dtDiog The dtDiog parameter is a Date object representing the date of the document.
     * @return The method is returning a boolean value.
     */
    public static boolean canCreateDiogByValuation(SGuiSession session, final Date dtDiog) {
        try {
            String sql = "SELECT id_stk_val "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " "
                    + "WHERE NOT b_del "
                    + "AND '" + SLibUtils.DbmsDateFormatDate.format(dtDiog) + "' BETWEEN dt_sta AND dt_end;";
            
            ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
            
            return ! res.next();
        }
        catch (SQLException ex) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static String revaluateValuations(SGuiClient client, ArrayList<SDbStockValuation> lValuations) throws SQLException {
        try {
            client.getSession().getStatement().getConnection().setAutoCommit(false);
            
            SDbStockValuation oNewValuation;
            for (SDbStockValuation oValuation : lValuations) {
                oNewValuation = (SDbStockValuation) oValuation.clone();
                oNewValuation.setRegistryNew(true);
                oNewValuation.setDeleted(false);
                oNewValuation.setAuxRecordPk(oValuation.getAuxRecordPk());
                oNewValuation.setAuxIsAllInsert(true);
                
                oNewValuation.save(client.getSession());
            }
            
            client.getSession().getStatement().getConnection().commit();
        }
        catch (CloneNotSupportedException ex) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
            
            client.getSession().getStatement().getConnection().rollback();
            
            return ex.getMessage();
        }
        catch (Exception ex) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
            
            return ex.getMessage();
        }
        
        return "";
    }
    
    public static ArrayList<SDbStockValuation> deleteValuations(SGuiClient client, Date endDate) {
        try {
            client.getSession().getStatement().getConnection().setAutoCommit(false);

            Date firstInvalidValuationDate = SStockValuationUtils.getFirstInvalidValuationDate(client.getSession(), endDate);

            String sql = "SELECT val.id_stk_val "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS val "
                    + "WHERE NOT val.b_del "
                    + "AND dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "' "
                    + "AND YEAR(dt_sta) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "') "
                    + "ORDER BY dt_end ASC";

            ResultSet res = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
            ArrayList<SDbStockValuation> lValuations = new ArrayList<>();
            SDbStockValuation oVal;
            while (res.next()) {
                oVal = new SDbStockValuation();
                oVal.read(client.getSession(), new int[] { res.getInt("id_stk_val") });
                oVal.setAuxIsAllDelete(true);
                oVal.delete(client.getSession());

                if (oVal.getQueryResultId() != SDbConsts.SAVE_OK) {
                    throw new Exception("Hubo un error al eliminar las valuaciones.");
                }
                
                lValuations.add(oVal);
            }
            
            client.getSession().getStatement().getConnection().commit();
            
            return lValuations;
        }
        catch (SQLException ex) {
            try {
                client.getSession().getStatement().getConnection().rollback();
            }
            catch (SQLException ex1) {
                Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(SStockValuationUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static String canDeleteValuations(SClientInterface client, Date endDate) throws Exception {
        Date firstInvalidValuationDate = SStockValuationUtils.getFirstInvalidValuationDate(client.getSession(), endDate);
        
        String sql = "SELECT DISTINCT val.id_stk_val, r.dt, val.dt_sta, val.dt_end, r.id_tp_rec, r.id_num "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS val "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " AS vacc ON (val.id_stk_val = vacc.fk_stk_val) "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FIN_REC) + " AS r ON ("
                + "vacc.fk_fin_rec_year = r.id_year AND "
                + "vacc.fk_fin_rec_per = r.id_per AND "
                + "vacc.fk_fin_rec_bkc = r.id_bkc AND "
                + "vacc.fk_fin_rec_tp_rec = r.id_tp_rec AND "
                + "vacc.fk_fin_rec_num = r.id_num) "
                + "WHERE NOT val.b_del AND NOT vacc.b_del "
                + "AND val.dt_sta >= '" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "' "
                + "AND YEAR(val.dt_sta) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "') "
                + "ORDER BY val.dt_end ASC";
        
        ResultSet res = client.getSession().getStatement().getConnection().createStatement().executeQuery(sql);
        String result = "";
        while (res.next()) {
//            oVal = new SDbStockValuation();
//            oVal.read(session, new int[] { res.getInt("id_stk_val") });
            
            if (! SDataUtilities.isPeriodOpen(client, res.getDate("dt"))) {
                result += "La valuación del '" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "' al "
                            + "'" + SLibUtils.DbmsDateFormatDate.format(firstInvalidValuationDate) + "' no se puede reevaluar porque la póliza: "
                            + "'" + res.getString("r.id_tp_rec") + "', num: " + (res.getInt("r.id_num")) + ", "
                            + "fecha: '" + SLibUtils.DateFormatDate.format(res.getDate("dt")) + "' está en un periodo cerrado.";
            }
        }
        
        return result;
    }
    
    public static Date getFirstInvalidValuationDate(SGuiSession session, Date endDate) throws SQLException, Exception {
        String sql = "SELECT id_stk_val, dt_sta "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " "
                + "WHERE NOT b_del "
                + "AND dt_end < '" + SLibUtils.DbmsDateFormatDate.format(endDate) + "' "
                + "AND YEAR(dt_sta) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(endDate) + "') "
                + "ORDER BY dt_end ASC";

        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        while (res.next()) {
            if (! isValuationDiogsValidById(session, res.getInt("id_stk_val")).isEmpty() || 
                ! isValuationMatReqValidById(session, res.getInt("id_stk_val")).isEmpty()) {
                return res.getDate("dt_sta");
            }
        }
        
        return null;
    }
    
    /**
     * Recorre las valuaciones anteriores a la actual y determina si alguna no es válida.
     * Si alguna no es válida, en el string de retorno se muestra el detalle de qué valuaciones no son válidas.
     * 
     * @param session
     * @param endDate
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public static String arePastValuationsValid(SGuiSession session, Date endDate) throws SQLException, Exception {
        String sql = "SELECT id_stk_val, dt_sta, dt_end "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " "
                + "WHERE NOT b_del "
                + "AND dt_end < '" + SLibUtils.DbmsDateFormatDate.format(endDate) + "' "
                + "AND YEAR(dt_sta) = YEAR('" + SLibUtils.DbmsDateFormatDate.format(endDate) + "') "
                + "ORDER BY dt_end ASC";

        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        ArrayList<SDataDiog> lDiogs = null;
        ArrayList<SDbMaterialRequest> lMatReqs = null;
        String result = "";
        while (res.next()) {
            lDiogs = isValuationDiogsValidById(session, res.getInt("id_stk_val"));
            if (! lDiogs.isEmpty()) {
                for (SDataDiog oDiog : lDiogs) {
                    result += "El movimiento de almacén: " + oDiog.getDbmsDiogCategory() + "-"
                            + oDiog.getDbmsDiogClass() + "-"
                            + oDiog.getDbmsDiogType() + " "
                            + "Numero: " + oDiog.getNumber() + " "
                            + "Fecha: '" + SLibUtils.DateFormatDate.format(oDiog.getDate()) + "' "
                            + "invalida la valuación de las fechas del '" + SLibUtils.DateFormatDate.format(res.getDate("dt_sta")) + "' "
                            + "al '" + SLibUtils.DateFormatDate.format(res.getDate("dt_end")) + "' \n";
                }
            }
            
            lMatReqs = isValuationMatReqValidById(session, res.getInt("id_stk_val"));
            if (! lMatReqs.isEmpty()) {
                for (SDbMaterialRequest oMatReq : lMatReqs) {
                    result += "La requisición con folio: " + oMatReq.getNumber() + ", "
                            + "Fecha: '" + SLibUtils.DateFormatDate.format(oMatReq.getDate()) + "' "
                            + "invalida la valuación de las fechas del '" + SLibUtils.DateFormatDate.format(res.getDate("dt_sta")) + "' "
                            + "al '" + SLibUtils.DateFormatDate.format(res.getDate("dt_end")) + "' \n";
                }
            }
        }
        
        return result;
    }
    
    /**
     * Determina si la valuación con el ID recibida es válida.
     * Consulta en la BD si hay un DIOG que se haya actualizado después del timestamp de la fecha de última 
     * actualización de la valuación.
     * Si hay movimientos de almacén que cumplan con este criterio los devuelve en la lista, si la lista está vacía 
     * significa que la valuación es válida.
     * 
     * @param session
     * @param idValuation
     * @return
     * @throws Exception 
     */
    public static ArrayList<SDataDiog> isValuationDiogsValidById(SGuiSession session, final int idValuation) throws Exception {
        SDbStockValuation oVal = new SDbStockValuation();
        oVal.read(session, new int[] { idValuation });
        
        String sql = "SELECT DISTINCT d.id_year, d.id_doc "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG) + " AS d "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DIOG_ETY) + " AS de ON "
                    + "(d.id_year = de.id_year AND d.id_doc = de.id_doc) " 
                    + "WHERE d.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(oVal.getDateStart()) + "' AND "
                    + "'" + SLibUtils.DbmsDateFormatDate.format(oVal.getDateEnd()) + "' " 
                    + "        AND (d.ts_edit >= '" + SLibUtils.DbmsDateFormatDate.format(oVal.getTsUserUpdate()) + "' " 
                    + "        OR de.ts_edit >= '" + SLibUtils.DbmsDateFormatDate.format(oVal.getTsUserUpdate()) + "')"
                    + "AND NOT d.b_del AND NOT de.b_del;";
            
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        ArrayList<SDataDiog> lDiogs = new ArrayList<>();
        while (res.next()) {
            SDataDiog oDiog = new SDataDiog();
            oDiog.read(new int [] { res.getInt("id_year"), res.getInt("id_doc") }, session.getStatement().getConnection().createStatement());
            lDiogs.add(oDiog);
        }
        
        return lDiogs;
    }
    
    /**
     * Determina si la valuación con el ID recibida es válida.
     * Consulta en la BD si hay una Requisición de Materiales que se haya actualizado después del timestamp de la fecha de última 
     * actualización de la valuación.
     * Si hay requisiciones que cumplan con este criterio las devuelve en la lista, si la lista está vacía 
     * significa que la valuación es válida.
     * 
     * @param session
     * @param idValuation
     * @return
     * @throws Exception 
     */
    public static ArrayList<SDbMaterialRequest> isValuationMatReqValidById(SGuiSession session, final int idValuation) throws Exception {
        String sql = "SELECT DISTINCT " +
                    " mr.id_mat_req " +
                    "FROM " +
                    " " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS v " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS vm ON v.id_stk_val = vm.fk_stk_val " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ_ETY) + " AS mre ON vm.fk_mat_req_n = mre.id_mat_req " +
                    " AND vm.fk_mat_req_ety_n = mre.id_ety " +
                    "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS mr ON mre.id_mat_req = mr.id_mat_req " +
                    "WHERE " +
                    "    NOT v.b_del AND NOT vm.b_del " +
                    "    AND v.id_stk_val = " + idValuation + " " +
                    "    AND mr.ts_usr_chg >= v.ts_usr_upd;";
            
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        ArrayList<SDbMaterialRequest> lMatReqs = new ArrayList<>();
        while (res.next()) {
            SDbMaterialRequest oMatReq = new SDbMaterialRequest();
            oMatReq.read(session, new int[] { res.getInt("id_mat_req") });
            lMatReqs.add(oMatReq);
        }
        
        return lMatReqs;
    }
    
    private static void updateTrnStockRowCost(SGuiSession session, final int idYear,
                                                                final int idItem, 
                                                                final int idUnit, 
                                                                final int idLot, 
                                                                final int idCob, 
                                                                final int idWh, 
                                                                final int idMov, 
                                                                final double dCost,
                                                                final double dQty,
                                                                final int opType) throws SQLException {
        double dTotal = SLibUtils.roundAmount(dCost * dQty);
        String sql = "UPDATE "
                + "" + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " SET "
                + "cost_u = " + SLibUtils.roundAmount(dCost) + ", "
                + "cost = " + SLibUtils.roundAmount(dTotal) + ", ";
        
        sql += (DEBIT == opType ? "debit = " : "credit = ") + SLibUtils.roundAmount(dTotal) + " ";

        sql += "WHERE (id_year = " + idYear + ") and "
                + "(id_item = " + idItem + ") and "
                + "(id_unit = " + idUnit + ") and "
                + "(id_lot = " + idLot + ") and "
                + "(id_cob = " + idCob + ") and "
                + "(id_wh = " + idWh + ") and "
                + "(id_mov = " + idMov + ");";

       session.getStatement().getConnection().createStatement().executeUpdate(sql);
    }
    

    /**
     * The function updates the cost and quantity of a row in the TRN_STK table based on the given
     * parameters.
     * 
     * @param session The session parameter is an object of type SGuiSession, which represents the user
     * session in the system. It is used to access the database connection and execute SQL queries.
     * @param idDiogYear The year of the document.
     * @param idDiogDoc The idDiogDoc parameter represents the ID of the document in the inventory
     * movement.
     * @param idDiogEty The parameter idDiogEty represents the ID of the document entry in the
     * inventory movement document (diog) table. It is used to identify the specific entry in the diog
     * table for which the stock row cost needs to be updated.
     * @param dCost The parameter "dCost" represents the cost value that needs to be updated in the
     * TrnStock table.
     * @param dQty dQty is the quantity of the item being updated in the transaction stock row.
     * @param opType The "opType" parameter is an integer that represents the type of operation to be
     * performed. It is used to determine how the stock row cost should be updated. The specific values
     * and their meanings would depend on the implementation of the "updateTrnStockRowCost" method.
     */
    public static void updateTrnStockRowCostByDiog(SGuiSession session, 
                                                            final int idDiogYear, 
                                                            final int idDiogDoc, 
                                                            final int idDiogEty, 
                                                            final double dCost,
                                                            final double dQty,
                                                            final int opType) throws SQLException {
        String sql = "SELECT id_year, id_item, id_unit, id_lot, id_cob, id_wh, id_mov "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK) + " "
                + "WHERE fid_diog_year = " + idDiogYear + " "
                + "AND fid_diog_doc = " + idDiogDoc + " "
                + "AND fid_diog_ety = " + idDiogEty + " "
                + "AND NOT b_del;";
        ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sql);
        while (res.next()) {
            updateTrnStockRowCost(session,
                            res.getInt("id_year"), 
                            res.getInt("id_item"), 
                            res.getInt("id_unit"), 
                            res.getInt("id_lot"), 
                            res.getInt("id_cob"), 
                            res.getInt("id_wh"), 
                            res.getInt("id_mov"),
                            dCost,
                            dQty,
                            opType);
        }
    }
}
