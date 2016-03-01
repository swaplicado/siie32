/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mmfg.data;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SMfgUtils {
    
    public static void validateBomItems(final SGuiSession session, final int idBom, final int idRootItem) throws Exception {
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT count(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.MFG_BOM) + " AS b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON b.fid_item=i.id_item "
                + "WHERE b.root=" + idRootItem + " AND b.lev>0 AND b.b_del=0 AND i.fid_st_item IN (" + SModSysConsts.ITMS_ST_ITEM_RES + ", " + SModSysConsts.ITMS_ST_ITEM_INA + ")";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next() && resultSet.getInt(1) > 0) {
            throw new Exception("La explosión de materiales tiene " + SLibUtils.DecimalFormatInteger.format(resultSet.getInt(1)) + " componente(s) restringido(s) o inactivo(s).");
        }
        
        sql = "SELECT count(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.MFG_BOM_SUB) + " AS b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON b.fid_item_sub=i.id_item "
                + "WHERE b.id_bom=" + idBom + " AND b.b_del=0 AND i.fid_st_item IN (" + SModSysConsts.ITMS_ST_ITEM_RES + ", " + SModSysConsts.ITMS_ST_ITEM_INA + ")";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next() && resultSet.getInt(1) > 0) {
            throw new Exception("La explosión de materiales tiene " + SLibUtils.DecimalFormatInteger.format(resultSet.getInt(1)) + " componente(s) sustituto(s) restringido(s) o inactivo(s).");
        }
        
        sql = "SELECT count(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.MFG_SGDS) + " AS b "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON b.fid_item=i.id_item "
                + "WHERE b.fid_bom=" + idBom + " AND b.b_del=0 AND i.fid_st_item IN (" + SModSysConsts.ITMS_ST_ITEM_RES + ", " + SModSysConsts.ITMS_ST_ITEM_INA + ")";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next() && resultSet.getInt(1) > 0) {
            throw new Exception("La explosión de materiales tiene " + SLibUtils.DecimalFormatInteger.format(resultSet.getInt(1)) + " subproducto(s) restringido(s) o inactivo(s).");
        }
    }
}
