/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import java.sql.ResultSet;

/**
 *
 * @author Uriel Castañeda
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
        String sql = "";
        ResultSet resulSet = null;
        double totalSupplied = 0;

        sql = "SELECT SUM(orig_qty) as tot_qty_sup FROM trn_diog_ety AS de WHERE b_del = 0 AND fid_dps_year_n = " + entryKey[0] + " AND fid_dps_doc_n = " + entryKey[1] + " AND fid_dps_ety_n = " + entryKey[2] + " ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        if (resulSet.next()) {
            totalSupplied = resulSet.getDouble("tot_qty_sup");
        }

        return totalSupplied;
    }
    
    /**
     * Verify is a DPS is an order and if it has any inventory document
     * @param client
     * @param dps
     * @param entry
     * @return 
     * @throws Exception 
     */
    public static boolean IsSourceOrderSupplied(final SClientInterface client, final SDataDps dps, final SDataDpsEntry entry) throws Exception {
        double totalQtySupplied = 0;    
        
        totalQtySupplied = obtainEntryTotalQuantitySupplied(client, new int[] { entry.getPkYearId(), entry.getPkDocId(), entry.getPkEntryId() });
       
        return dps.isOrder() && totalQtySupplied > 0;
    }

    /**
     * Checks if source order is authorized.
     * @param client
     * @param dps
     * @return 
    */
    public static boolean isDpsAuthorized(final SClientInterface client, final SDataDps dps) {
        boolean isAuthorized = true;
        boolean isStatuaAuthorized = dps.getFkDpsAuthorizationStatusId() == SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN;
        
        if (dps.isOrder()) {
            if (dps.isOrderPur()) {
                isAuthorized = client.getSessionXXX().getParamsCompany().getIsAuthorizationPurchasesOrderAutomatic() && isStatuaAuthorized;
            } 
            else {
                isAuthorized = client.getSessionXXX().getParamsCompany().getIsAuthorizationSalesOrderAutomatic() && isStatuaAuthorized;
            }
        } 
        else if (dps.isDocument()) {
            if (dps.isDocumentPur()) {
                isAuthorized = client.getSessionXXX().getParamsCompany().getIsAuthorizationPurchasesDocAutomatic() && isStatuaAuthorized;
            } 
            else {
                isAuthorized = client.getSessionXXX().getParamsCompany().getIsAuthorizationSalesDocAutomatic() && isStatuaAuthorized;
            }
        }
        
        if (!isAuthorized) {
            client.showMsgBoxWarning(SLibConstants.MSG_INF_NOT_AUTHORN_ORD);
        }

        return isAuthorized;
    }
}
