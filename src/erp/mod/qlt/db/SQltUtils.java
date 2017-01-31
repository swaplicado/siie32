/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.qlt.db;

import erp.mod.SModConsts;
import erp.mtrn.data.STrnStockMove;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Uriel Castañeda
 */
public abstract class SQltUtils {
    
    /**
     * Verifies if lot exists in quality for the given parameters
     * @param client
     * @param date of quality approval
     * @param idBizPartner Supplier of the item
     * @param stockMove move with lot information
     * @return true if the lot exists
     */
    public static boolean checkLotApproved(final SGuiClient client, final Date date, final int idBizPartner, final STrnStockMove stockMove) {
        boolean exists = false;        
        String sql = "";
        ResultSet resultSet = null;
        
        try {

            Statement statement = client.getSession().getStatement().getConnection().createStatement();
            
            sql = "SELECT id_lot_apr "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_LOT_APR) + " "
                + "WHERE " + "dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND "
                + "fk_bp = " + idBizPartner + " AND "
                + "fk_item = " + stockMove.getPkItemId() + " AND "
                + "fk_unit = " + stockMove.getPkUnitId() + " AND "
                +  "lot = '" + stockMove.getAuxLot() + "' ";

            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
               exists = true;
            }
        }
        catch (Exception e) {
            SLibUtils.showException(SQltUtils.class.getName(), e);
        }
        return exists;
    } 
    
}
