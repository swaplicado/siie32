/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.SErpConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sa.lib.SLibUtils;

/**
 *
 * @author Uriel Castañeda, Adrián Avilés, Isabel Servín
 */
public abstract class STrnDpsUtilities {
 
    /**
     * Obtain total quantity supplied for a DPS in inventory.
     * @param client GUI client.
     * @param entryKey Entry key.
     * @return Total quantity supplied in inventory movements
     * @throws Exception
     */
    public static double obtainEntryTotalQuantitySupplied(final SClientInterface client, final int[] entryKey) throws Exception {
        String sql;
        ResultSet resulSet;
        double totalSupplied = 0;

        sql = "SELECT SUM(de.orig_qty) as tot_qty_sup "
                + "FROM trn_diog AS d "
                + "INNER JOIN trn_diog_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc "
                + "WHERE NOT d.b_del AND NOT de.b_del AND "
                + "de.fid_dps_year_n = " + entryKey[0] + " AND de.fid_dps_doc_n = " + entryKey[1] + " AND de.fid_dps_ety_n = " + entryKey[2] + " ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            totalSupplied = resulSet.getDouble("tot_qty_sup");
        }

        return totalSupplied;
    }
    
    /**
     * Obtiene si el ítem de la partida de un dps está eliminado.
     * @param client GUI client.
     * @param entryKey Entry key.
     * @return true or false
     * @throws Exception
     */
    public static boolean isDpsEntryItemDeleted(final SClientInterface client, final int[] entryKey) throws Exception {
        boolean deleted = false;
        String sql;
        ResultSet resultSet;
        
        sql = "SELECT i.b_del FROM trn_dps_ety AS de " 
                + "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " 
                + "WHERE de.id_year = " + entryKey[0] + " AND de.id_doc = " + entryKey[1] + " AND de.id_ety = " + entryKey[2];
        
        resultSet = client.getSession().getStatement().executeQuery(sql);
        if (resultSet.next()) {
            deleted = resultSet.getBoolean(1);
        }
        
        return deleted;
    }
    
    public static SDataDps getDpsSourceFromCreditNote(final Statement statement, final int[] entryKey) {
        try {
            String sql = "";
            ResultSet resulSet = null;
            
            sql = "SELECT id_dps_year, id_dps_doc "
                    + "FROM trn_dps_dps_adj AS de "
                    + "INNER JOIN trn_dps AS d ON de.id_dps_year = d.id_year AND de.id_dps_doc = d.id_doc "
                    + "INNER JOIN trn_dps_ety AS dety ON de.id_dps_year = dety.id_year AND de.id_dps_doc = dety.id_doc AND de.id_dps_ety = dety.id_ety "
                    + "WHERE NOT d.b_del AND NOT dety.b_del AND "
                    + "de.id_adj_year = " + entryKey[0] + " AND de.id_adj_doc = " + entryKey[1] + " AND de.id_adj_ety = " + entryKey[2] + " ";
            
            resulSet = statement.getConnection().createStatement().executeQuery(sql);
            if (resulSet.next()) {
                SDataDps dps = new SDataDps();
                dps.read(new int[] { resulSet.getInt("id_dps_year"), resulSet.getInt("id_dps_doc") }, statement.getConnection().createStatement());
                
                if (dps.getLastDbActionResult() == SLibConstants.DB_ACTION_READ_OK) {
                    return dps;
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnDpsUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Verify is a DPS is an order and if it has any inventory document
     * @param client
     * @param dps
     * @param entry
     * @return 
     * @throws Exception 
     */
    public static boolean isSourceOrderSupplied(final SClientInterface client, final SDataDps dps, final SDataDpsEntry entry) throws Exception {
        double totalQtySupplied = 0;    
        
        totalQtySupplied = obtainEntryTotalQuantitySupplied(client, new int[] { entry.getPkYearId(), entry.getPkDocId(), entry.getPkEntryId() });
       
        return dps.isOrder() && totalQtySupplied > 0;
    }

    /**
     * Checks if source order is authorized.
     * @param client GUI client.
     * @param dps DPS.
     * @return 
     */
    public static boolean isDpsAuthorized(final SClientInterface client, final SDataDps dps) {
        boolean authorized = true;
        if (dps.isDpsTypeContract()) {
            if (dps.isDpsTypeContractPur()) {
                if (client.getSessionXXX().getParamsCompany().getIsAuthorizationPurchasesContractAutomatic() && dps.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                    authorized = false;
                    client.showMsgBoxWarning(SLibConstants.MSG_INF_NOT_AUTHORN_CON);
                }
            }
            else {
                if (client.getSessionXXX().getParamsCompany().getIsAuthorizationSalesContractAutomatic() && dps.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                    authorized = false;
                    client.showMsgBoxWarning(SLibConstants.MSG_INF_NOT_AUTHORN_CON);
                }
            }
        }
        else if (dps.isOrder()) {
            if (dps.isOrderPur()) {
                if (client.getSessionXXX().getParamsCompany().getIsAuthorizationPurchasesOrderAutomatic() && dps.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                    authorized = false;
                    client.showMsgBoxWarning(SLibConstants.MSG_INF_NOT_AUTHORN_ORD);
                }
            } 
            else {
                if (client.getSessionXXX().getParamsCompany().getIsAuthorizationSalesOrderAutomatic() && dps.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                    authorized = false;
                    client.showMsgBoxWarning(SLibConstants.MSG_INF_NOT_AUTHORN_ORD);
                }
            }
        }
        else if (dps.isDocument()) {
            if (dps.isDocumentPur()) {
                if (client.getSessionXXX().getParamsCompany().getIsAuthorizationPurchasesDocAutomatic() && dps.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                    authorized = false;
                    client.showMsgBoxWarning(SLibConstants.MSG_INF_NOT_AUTHORN_DOC);
                }
            } 
            else {
                if (client.getSessionXXX().getParamsCompany().getIsAuthorizationSalesDocAutomatic() && dps.getFkDpsAuthorizationStatusId() != SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN) {
                    authorized = false;
                    client.showMsgBoxWarning(SLibConstants.MSG_INF_NOT_AUTHORN_DOC);
                }
            }
        }
        return authorized;
    }
    
    public static double obtainDpsAmountSupplied(final SClientInterface client, final int[] dpsKey) throws Exception {
        double amount = 0;
        
        String sql = "SELECT SUM(dde.tot_cur_r) "
                + "FROM trn_dps_dps_supply AS dds "
                + "INNER JOIN trn_dps_ety AS sde ON dds.id_des_year = sde.id_year AND dds.id_des_doc = sde.id_doc AND dds.id_des_ety = sde.id_ety "
                + "INNER JOIN trn_dps AS dd ON dds.id_des_year = dd.id_year AND dds.id_des_doc = dd.id_doc "
                + "INNER JOIN trn_dps_ety AS dde ON dds.id_des_year = dde.id_year AND dds.id_des_doc = dde.id_doc AND dds.id_des_ety = dde.id_ety "
                + "WHERE NOT sde.b_del AND NOT dd.b_del AND NOT dde.b_del "
                + "AND dds.id_src_year = " + dpsKey[0] + " AND dds.id_src_doc = " + dpsKey[1] + ";";
        
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                amount = resultSet.getDouble(1);
            }
        }
        
        return amount;
    }
    
    /**
     * Devuelve el monto surtido de una partida de un DPS.
     * @param client GUI client.
     * @param dpsEtyKey
     * @return 
     * @throws java.lang.Exception 
     */
    public static double obtainDpsEntryAmountSupplied(final SClientInterface client, final int[] dpsEtyKey) throws Exception {
        double amount = 0;
        
        String sql = "SELECT SUM(dde.tot_cur_r) "
                + "FROM trn_dps_dps_supply AS dds "
                + "INNER JOIN trn_dps AS dd ON dds.id_des_year = dd.id_year AND dds.id_des_doc = dd.id_doc "
                + "INNER JOIN trn_dps_ety AS dde ON dds.id_des_year = dde.id_year AND dds.id_des_doc = dde.id_doc AND dds.id_des_ety = dde.id_ety "
                + "WHERE NOT dd.b_del AND NOT dde.b_del "
                + "AND dds.id_src_year = " + dpsEtyKey[0] + " AND dds.id_src_doc = " + dpsEtyKey[1] + " AND dds.id_src_ety = " + dpsEtyKey[2] + ";";
        
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                amount = resultSet.getDouble(1);
            }
        }
        
        return amount;
    }
    
    /**
     * Devuelve true si el DPS Entry tiene surtidos como servicio.
     * @param client GUI client.
     * @param dpsEtyKey
     * @return 
     * @throws java.lang.Exception 
     */
    public static boolean isDpsEntrySuppliedAsService(final SClientInterface client, final int[] dpsEtyKey) throws Exception {
        boolean suppliedAsService = false;
        
        String sql = "SELECT SUM(dds.qty) "
                + "FROM trn_dps_dps_supply AS dds "
                + "INNER JOIN trn_dps AS dd ON dds.id_des_year = dd.id_year AND dds.id_des_doc = dd.id_doc "
                + "INNER JOIN trn_dps_ety AS dde ON dds.id_des_year = dde.id_year AND dds.id_des_doc = dde.id_doc AND dds.id_des_ety = dde.id_ety "
                + "WHERE NOT dd.b_del AND NOT dde.b_del "
                + "AND dds.id_src_year = " + dpsEtyKey[0] + " AND dds.id_src_doc = " + dpsEtyKey[1] + " AND dds.id_src_ety = " + dpsEtyKey[2] + ";";
        
        try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                suppliedAsService = resultSet.getDouble(1) == 0d;
            }
        }
        
        return suppliedAsService;
    }
    
    /**
     * Devuelve el cálculo del porcentaje de vinculación para un servicio.
     * @param dpsEntries
     * @param priceUnitary
     * @return 
     */
    public static double calculateLinkedServicePct(final ArrayList<SDataDpsEntry> dpsEntries, final double priceUnitary) {
        double sumPricesUnitary = 0;
        sumPricesUnitary = dpsEntries.stream().map((importedDps) -> importedDps.getOriginalPriceUnitaryCy()).reduce(sumPricesUnitary, (accumulator, _item) -> accumulator + _item);
        return SLibUtils.round(priceUnitary / sumPricesUnitary, SErpConsts.VAL_QTY_MAX_DECS);
    }
}
