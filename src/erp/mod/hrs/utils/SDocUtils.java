/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.utils;

import erp.mod.SModConsts;
import erp.mod.hrs.db.SRowPreceptSubsection;
import java.sql.ResultSet;
import java.util.ArrayList;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDocUtils {
    
    /**
     * Compose descriptions of required precept subsections.
     * @param session GUI session.
     * @param preceptSubsectionKeys Required precept subsections.
     * @param separator Separator.
     * @return Descriptions of required precept subsections.
     */
    public static String composePreceptSubsections(final SGuiSession session, final ArrayList<int[]> preceptSubsectionKeys, String separator) {
        String preceptSubsections = "";
        
        for (int[] key : preceptSubsectionKeys) {
            preceptSubsections += (preceptSubsections.isEmpty() ? "" : separator) + (String) session.readField(SModConsts.HRS_PREC_SUBSEC, key, SDbRegistry.FIELD_NAME);
        }
        
        return preceptSubsections;
    }
    
    /**
     * Get rows for grid of precept subsections.
     * @param session GUI session.
     * @return Rows for grid of precept subsections.
     * @throws Exception 
     */
    public static ArrayList<SRowPreceptSubsection> getPreceptSubsectionRows(final SGuiSession session) throws Exception {
        ArrayList<SRowPreceptSubsection> rows = new ArrayList<>();
        
        String sql = "SELECT pss.id_prec, pss.id_sec, pss.id_subsec, "
                + "p.code, p. name, ps.code, ps.name, ps.sort, pss.code, pss.name, pss.sort "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PREC) + " AS p "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PREC_SEC) + " AS ps ON ps.id_prec = p.id_prec "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.HRS_PREC_SUBSEC) + " AS pss ON pss.id_prec = ps.id_prec AND pss.id_sec = ps.id_sec "
                + "WHERE NOT p.b_del AND NOT ps.b_del AND NOT pss.b_del "
                + "ORDER BY p.name, ps.sort, pss.sort, pss.id_prec, pss.id_sec, pss.id_subsec;";
        
        int positionGlobal = 0;
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                rows.add(new SRowPreceptSubsection(
                        new int[] { resultSet.getInt("pss.id_prec"), resultSet.getInt("pss.id_sec"), resultSet.getInt("pss.id_subsec") }, 
                        resultSet.getString("p.code"), 
                        resultSet.getString("ps.code"), 
                        resultSet.getString("pss.code"), 
                        resultSet.getString("pss.name"), 
                        ++positionGlobal, 
                        0));
            }
        }
        
        return rows;
    }
    
    /**
     * Get the most recent version of required document type.
     * @param session GUI session.
     * @param docType Document type. Constants defined in SModSysConsts.CFGU_DOC_...
     * @return ID of the most recent version of required document type.
     * @throws Exception 
     */
    public static int getDocCurrentVersionId(final SGuiSession session, final int docType) throws Exception {
        int id = 0;
        
        String sql = "SELECT id_doc "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_DOC) + " "
                + "WHERE fk_tp_doc = " + docType + " AND NOT b_obs AND NOT b_del "
                + "ORDER BY dt_doc DESC "
                + "LIMIT 1;";
        
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        
        return id;
    }
}
