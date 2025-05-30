/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sa.lib.SLibUtils;

import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public abstract class SStockValuationAdjustsUtils {

    public static List<SDbStockValuationMvt> makeStockValuationAdjusts(SGuiSession session, Date dateStart, Date dateEnd, final int idValuation) throws SQLException, Exception {
        List<SDbStockValuationMvt> lStkValMvtToRev = new ArrayList<>();
        List<SDbStockValuationMvt> lStkValMvtAdjusts = new ArrayList<>();

        String sql = "SELECT  "
                + "    mvt.*, "
                + "    diog_e.val_u, "
                + "    oc_e.price_u_real_r, "
                + "    fac_e.price_u_real_r "
                + "FROM "
                + "    erp_aeth.trn_stk_val_mvt AS mvt "
                + "        INNER JOIN "
                + "    trn_diog_ety AS diog_e ON mvt.fk_diog_year_in_n = diog_e.id_year "
                + "        AND mvt.fk_diog_doc_in_n = diog_e.id_doc "
                + "        AND mvt.fk_diog_ety_in_n = diog_e.id_ety "
                + "        INNER JOIN "
                + "    trn_diog AS diog ON diog_e.id_year = diog.id_year "
                + "        AND diog_e.id_doc = diog.id_doc "
                + "        INNER JOIN "
                + "    trn_dps_ety AS oc_e ON diog_e.fid_dps_year_n = oc_e.id_year "
                + "        AND diog_e.fid_dps_doc_n = oc_e.id_doc "
                + "        AND diog_e.fid_dps_ety_n = oc_e.id_ety "
                + "        INNER JOIN "
                + "    trn_dps_dps_supply AS supp ON oc_e.id_year = supp.id_src_year "
                + "        AND oc_e.id_doc = supp.id_src_doc "
                + "        AND oc_e.id_ety = supp.id_src_ety "
                + "        INNER JOIN "
                + "	trn_dps_ety AS fac_e ON fac_e.id_year = supp.id_des_year "
                + "        AND fac_e.id_doc = supp.id_des_doc "
                + "        AND fac_e.id_ety = supp.id_des_ety "
                + "WHERE "
                + "    NOT mvt.b_del AND mvt.b_temp_price "
                + "        AND NOT mvt.b_rev "
                + "        AND mvt.fk_ct_iog = 2;";
        
        ResultSet resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
        SDbStockValuationMvt oMvtAdjust = null;
        SDbStockValuationMvt oMvtRevised = null;
        while (resultSet.next()) {
            oMvtAdjust = new SDbStockValuationMvt();

            oMvtAdjust.setDateMove(resultSet.getDate("dt_mov"));
            oMvtAdjust.setQuantityMovement(0d);
            oMvtAdjust.setCostUnitary(resultSet.getDouble("fac_e.price_u_real_r"));
            oMvtAdjust.setCost_r(SLibUtils.round((resultSet.getDouble("fac_e.price_u_real_r") * resultSet.getDouble("qty_mov")) 
                                                - resultSet.getDouble("mvt.cost_r"), 8));
            oMvtAdjust.setFkStockValuationId(idValuation);
            oMvtAdjust.setFkStockValuationMvtId_n(resultSet.getInt("fk_stk_val_mvt_n"));
            oMvtAdjust.setFkDiogCategoryId(resultSet.getInt("fk_ct_iog"));
            oMvtAdjust.setFkDiogYearInId_n(resultSet.getInt("fk_diog_year_in_n"));
            oMvtAdjust.setFkDiogDocInId_n(resultSet.getInt("fk_diog_doc_in_n"));
            oMvtAdjust.setFkDiogEntryInId_n(resultSet.getInt("fk_diog_ety_in_n"));
            oMvtAdjust.setFkDiogYearOutId_n(resultSet.getInt("fk_diog_year_out_n"));
            oMvtAdjust.setFkDiogDocOutId_n(resultSet.getInt("fk_diog_doc_out_n"));
            oMvtAdjust.setFkDiogEntryOutId_n(resultSet.getInt("fk_diog_ety_out_n"));
            oMvtAdjust.setFkMaterialRequestId_n(resultSet.getInt("fk_mat_req_n"));
            oMvtAdjust.setFkMaterialRequestEntryId_n(resultSet.getInt("fk_mat_req_ety_n"));
            oMvtAdjust.setFkStockValuationMvtRevisionId_n(resultSet.getInt("id_stk_val_mvt"));
            oMvtAdjust.setFkItemId(resultSet.getInt("fk_item"));
            oMvtAdjust.setFkUnitId(resultSet.getInt("fk_unit"));
            oMvtAdjust.setFkLotId(resultSet.getInt("fk_lot"));
            oMvtAdjust.setFkCompanyBranchId(resultSet.getInt("fk_cob"));
            oMvtAdjust.setFkWarehouseId(resultSet.getInt("fk_wh"));

            oMvtAdjust.save(session);

            lStkValMvtAdjusts.add(oMvtAdjust);

            oMvtRevised = new SDbStockValuationMvt();
            oMvtRevised.setPkStockValuationMvtId(oMvtAdjust.getFkStockValuationMvtRevisionId_n());
            oMvtRevised.setFkStockValuationMvtRevisionId_n(oMvtAdjust.getPkStockValuationMvtId());
            oMvtRevised.setRevised(true);
            
            SStockValuationAdjustsUtils.updateStockValuationMvt(session, oMvtRevised);

            lStkValMvtToRev.add(oMvtRevised);
        }
        
        return lStkValMvtAdjusts;
    }

    private static void updateStockValuationMvt(SGuiSession session, SDbStockValuationMvt oStkValMvtRev) throws SQLException {
        String sql = "UPDATE "
                + "    erp_aeth.trn_stk_val_mvt "
                + "SET "
                + "    fk_stk_val_mvt_rev_n = " + oStkValMvtRev.getFkStockValuationMvtRevisionId_n() + ","
                + "    b_rev = " + oStkValMvtRev.isRevised() + " "
                + "WHERE "
                + "    id_stk_val_mvt = " + oStkValMvtRev.getPkStockValuationMvtId() + ";";

        
        session.getStatement().getConnection().createStatement().executeUpdate(sql);
    }
}
