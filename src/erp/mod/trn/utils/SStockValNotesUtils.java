/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.utils;

import erp.mod.SModConsts;
import erp.mod.trn.db.SDbStockValuationMvtNote;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SStockValNotesUtils {
    
    public static List<SDbStockValuationMvtNote> getAllStockValuationMvtNotes(SGuiSession session, int stockValuationMvtId) throws Exception {
        List<SDbStockValuationMvtNote> notes = new ArrayList<>();
        int mvtCategory = 0;
        // Si el movimiento es de entrada solo se muestran las notas del movimiento de entrada
        String sqlMvt = "SELECT fk_ct_iog FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " "
                + "WHERE id_stk_val_mvt = " + stockValuationMvtId;
        ResultSet mvtResultSet = session.getStatement().getConnection().createStatement().executeQuery(sqlMvt);
        if (mvtResultSet.next()) {
            mvtCategory = mvtResultSet.getInt("fk_ct_iog");
        }
        if (mvtCategory == 2) {
            // buscar las notas de la entrada
            String sqlMvtIn = "SELECT fk_stk_val_mvt_n FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " "
                    + "WHERE id_stk_val_mvt = " + stockValuationMvtId;

            ResultSet mvtInResultSet = session.getStatement().getConnection().createStatement().executeQuery(sqlMvtIn);
            if (mvtInResultSet.next()) {
                notes.addAll(SStockValNotesUtils.getStockValuationMvtNotes(session, mvtInResultSet.getInt("fk_stk_val_mvt_n")));
            }
        }

        notes.addAll(SStockValNotesUtils.getStockValuationMvtNotes(session, stockValuationMvtId));
        
        return notes;
    }

    public static List<SDbStockValuationMvtNote> getStockValuationMvtNotes(SGuiSession session, int stockValuationMvtId) throws Exception {
        List<SDbStockValuationMvtNote> notes = new ArrayList<>();
        String sql = "SELECT id_stk_val_mvt_note FROM " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT_NOTE) + " "
                + "WHERE fk_stk_val_mvt = " + stockValuationMvtId + " "
                + "AND b_del = 0 "
                + "ORDER BY id_stk_val_mvt_note ";
        
        ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
        while (resultSet.next()) {
            SDbStockValuationMvtNote note = new SDbStockValuationMvtNote();
            note.read(session, new int[] { resultSet.getInt("id_stk_val_mvt_note") });
            notes.add(note);
        }
        
        return notes;
    }
}
