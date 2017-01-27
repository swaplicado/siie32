/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.qty.db;

import erp.mod.SModConsts;
import erp.mtrn.data.STrnStockMove;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Uriel Castañeda
 */
public abstract class SQltUtils {
    
    /**
     * Verifies if de lot exists in quality for the working date
     * @param client
     * @param lot
     * @param idSuplier
     * @return 
     */
    public static boolean existsQualityLot(final SGuiClient client, final STrnStockMove lot, final int idSuplier) {
        boolean exists = false;        
        String sql = "";
        ResultSet resultSet = null;
        
        try {
            Statement statement = client.getSession().getStatement().getConnection().createStatement();
            
            sql = "SELECT id_lot_apr "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.QTLY_LOT) + " "
                + "WHERE fk_item = " + lot.getPkItemId() + " AND "
                + "fk_unit = " + lot.getPkUnitId() + " AND "
                + "fk_bb_n = " + idSuplier + " AND "
                + "dt = '" + SLibUtils.DbmsDateFormatDate.format(client.getSession().getCurrentDate()) + "' AND lot = '" + lot.getAuxLot() + "' ";

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
