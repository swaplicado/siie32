/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

/**
 *
 * @author Edwin Carmona
 */
import erp.mod.trn.utils.SStockValuationUtils;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SFinAccountConfigEntry;
import erp.mfin.data.SFinAccountUtilities;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

public class SStockValuationRecordUpdate {

    public static void updateRecordsByMonth(SGuiSession session, final int iYear, final int iMonth) {
        try {
            // ejecutar query
            String sqlSelect = "SELECT  " +
                    "DISTINCT vmvt.*, vacc.fk_fin_rec_bkc_n ";

            String sqlRecEtySelect = "SELECT DISTINCT vacc.*, fre.* ";

            String sql = "FROM " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " AS vacc " +
                    "        INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_MVT) + " AS vmvt ON (vacc.fk_stk_val_mvt = vmvt.id_stk_val_mvt) " +
                    "        INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL) + " AS v ON (vmvt.fk_stk_val = v.id_stk_val) " +
                    "        INNER JOIN " +
                    "    " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " AS fre ON (vacc.fk_fin_rec_year_n = fre.id_year " +
                    "        AND vacc.fk_fin_rec_per_n = fre.id_per " +
                    "        AND vacc.fk_fin_rec_bkc_n = fre.id_bkc " +
                    "        AND vacc.fk_fin_rec_tp_rec_n = fre.id_tp_rec " +
                    "        AND vacc.fk_fin_rec_num_n = fre.id_num " +
                    "        AND vacc.fk_fin_rec_ety_n = fre.id_ety) " +
                    "WHERE " +
                    "    NOT vacc.b_del AND NOT vmvt.b_del AND NOT v.b_del " +
                    "        AND vacc.fk_fin_rec_year_n = " + iYear + " " +
                    "        AND vacc.fk_fin_rec_per_n = " + iMonth + " ";
            
            String whereMvt = "AND vmvt.id_stk_val_mvt = ";
            String orderBy = "ORDER BY fre.id_ety ASC;";

            ResultSet res = session.getStatement().getConnection().createStatement().executeQuery(sqlSelect + sql + orderBy);
            HashMap<Integer, java.util.Vector<erp.mfin.data.SFinAccountConfigEntry>> purCfgs = new HashMap<>();
            SStockValuationConfiguration oCfg = SStockValuationUtils.getStockValuationConfig(session.getStatement().getConnection().createStatement());
            java.util.Vector<erp.mfin.data.SFinAccountConfigEntry> vPurAccConfigs;
            ArrayList<SFinAccountConfigEntry> lPurAccConfigs = new ArrayList<>();
            ArrayList<SDbStockValuationAccount> lMvtRecordEntries = new ArrayList<>();
            ArrayList<SDbStockValuationAccount> lMvtRecordEntriesCopy = new ArrayList<>();
            ArrayList<SDbStockValuationAccount> lMvtRecordEntriesToUpdate = new ArrayList<>();
            Date firstDateOfMonth = SLibTimeUtils.getBeginOfMonth(SLibTimeUtils.createDate(iYear, iMonth, 1));
            SDbStockValuationAccount oStockValuationAccount;
            SDataRecordEntry oRecordEntry;
            int idMatReq = 0;
            int idMatReqEty = 0;
            int idItem = 0;
            int idMvt = 0;
            String sqlMvt = "";
            // recorrer rows
            int counter = 1;
            while (res.next()) {
                idMvt = res.getInt("vmvt.id_stk_val_mvt");
                System.out.println("Actualizando renglón " + counter + ", id_mvt: " + idMvt);
                lPurAccConfigs.clear();
                /**
                 * Si el consumo tiene asociada una Requisición de materiales
                 */
                idMatReq = res.getInt("vmvt.fk_mat_req_n");
                idMatReqEty = res.getInt("vmvt.fk_mat_req_ety_n");
                if (idMatReq > 0) {
                    if (idMatReqEty > 0) {
                        lPurAccConfigs = SFinAccountUtilities.getConsumptionWhsAccountConfigsByMatReqEty(
                                session.getStatement().getConnection(),
                                idMatReq, idMatReqEty);
                    }
                    if (lPurAccConfigs.isEmpty()
                            || (!lPurAccConfigs.isEmpty() && lPurAccConfigs.get(0).getCostCenterId().isEmpty())) {
                        lPurAccConfigs = SFinAccountUtilities.getConsumptionWhsAccountConfigsByMatReq(
                                session.getStatement().getConnection(),
                                idMatReq);
                    }
                }

                // Si la configuración es vacía se toma en cuenta la configuración del paquete
                // contable
                idItem = res.getInt("vmvt.fk_item");
                if (lPurAccConfigs.isEmpty()) {
                    /**
                     * Configuración para partida de gastos (debit)
                     */
                    if (purCfgs.containsKey(idItem)) {
                        vPurAccConfigs = purCfgs.get(idItem);
                    } else {
                        vPurAccConfigs = SFinAccountUtilities.obtainItemAccountConfigs(idItem,
                                res.getInt("vacc.fk_fin_rec_bkc_n"),
                                firstDateOfMonth,
                                oCfg.getFinTpAccItemAsset(),
                                true,
                                session.getStatement().getConnection().createStatement());
                        purCfgs.put(idItem, vPurAccConfigs);
                    }

                    lPurAccConfigs.addAll(vPurAccConfigs);
                }
                
                counter++;
                if (lPurAccConfigs.size() == 1 && lPurAccConfigs.get(0).getPercentage() == 1d) {
                    continue;
                }

                sqlMvt = sqlRecEtySelect + sql + whereMvt + idMvt + " " + orderBy;
                ResultSet resEty = session.getStatement().getConnection().createStatement().executeQuery(sqlMvt);

                lMvtRecordEntries.clear();
                while (resEty.next()) {
                    oStockValuationAccount = new SDbStockValuationAccount();
                    oStockValuationAccount.setPkStockValuationAccountId(resEty.getInt("vacc.id_stk_val_acc"));
                    oStockValuationAccount.setDeleted(resEty.getBoolean("vacc.b_del"));
                    oStockValuationAccount.setFkFinRecYearId_n(resEty.getInt("vacc.fk_fin_rec_year_n"));
                    oStockValuationAccount.setFkFinRecPeriodId_n(resEty.getInt("vacc.fk_fin_rec_per_n"));
                    oStockValuationAccount.setFkFinBookKeepingCenterId_n(resEty.getInt("vacc.fk_fin_rec_bkc_n"));
                    oStockValuationAccount.setFkFinRecordTypeId_n(resEty.getString("vacc.fk_fin_rec_tp_rec_n"));
                    oStockValuationAccount.setFkFinRecNumberId_n(resEty.getInt("vacc.fk_fin_rec_num_n"));
                    oStockValuationAccount.setFkFinRecEntryId_n(resEty.getInt("vacc.fk_fin_rec_ety_n"));
                    oStockValuationAccount.setProrationPercentage(resEty.getDouble("vacc.prorat_per"));
                    oStockValuationAccount.setFkStockValuationId(resEty.getInt("vacc.fk_stk_val"));
                    oStockValuationAccount.setFkValuationMvtId(resEty.getInt("vacc.fk_stk_val_mvt"));

                    oRecordEntry = new SDataRecordEntry();
                    oRecordEntry.setPkYearId(resEty.getInt("fre.id_year"));
                    oRecordEntry.setPkPeriodId(resEty.getInt("fre.id_per"));
                    oRecordEntry.setPkBookkeepingCenterId(resEty.getInt("fre.id_bkc"));
                    oRecordEntry.setPkRecordTypeId(resEty.getString("fre.id_tp_rec"));
                    oRecordEntry.setPkNumberId(resEty.getInt("fre.id_num"));
                    oRecordEntry.setPkEntryId(resEty.getInt("fre.id_ety"));
                    oRecordEntry.setConcept(resEty.getString("fre.concept"));
                    oRecordEntry.setUnits(resEty.getDouble("fre.units"));
                    oRecordEntry.setSortingPosition(resEty.getInt("fre.sort_pos"));
                    oRecordEntry.setIsDeleted(resEty.getBoolean("fre.b_del"));
                    oRecordEntry.setFkAccountIdXXX(resEty.getString("fre.fid_acc"));
                    oRecordEntry.setFkAccountId(resEty.getInt("fre.fk_acc"));
                    oRecordEntry.setFkCostCenterId_n(resEty.getInt("fre.fk_cc_n"));
                    oRecordEntry.setFkCostCenterIdXXX_n(resEty.getString("fre.fid_cc_n"));
                    oRecordEntry.setFkYearId_n(resEty.getInt("fre.fid_year_n"));
                    oRecordEntry.setFkDpsYearId_n(resEty.getInt("fre.fid_dps_year_n"));
                    oRecordEntry.setFkDpsDocId_n(resEty.getInt("fre.fid_dps_doc_n"));
                    oRecordEntry.setFkDiogYearId_n(resEty.getInt("fre.fid_diog_year_n"));
                    oRecordEntry.setFkDiogDocId_n(resEty.getInt("fre.fid_diog_doc_n"));
                    oRecordEntry.setFkItemId_n(resEty.getInt("fre.fid_item_n"));
                    oRecordEntry.setFkItemAuxId_n(resEty.getInt("fre.fid_item_aux_n"));

                    oStockValuationAccount.setAuxRecordEntry(oRecordEntry);

                    lMvtRecordEntries.add(oStockValuationAccount);
                }

                // copia de lMvtRecordEntries:
                lMvtRecordEntriesCopy.clear();
                lMvtRecordEntriesCopy.addAll(lMvtRecordEntries);
                lMvtRecordEntriesToUpdate.clear();

                double dQuantity = res.getDouble("vmvt.qty_mov");
                double dQuantitySum = 0d;
                for (SFinAccountConfigEntry oPurConfig : lPurAccConfigs) {
                    for (SDbStockValuationAccount oMvtRecordEntry : lMvtRecordEntriesCopy) {
                        if (oMvtRecordEntry.getAuxRecordEntry().getFkAccountIdXXX().equals(oPurConfig.getAccountId()) &&
                            oMvtRecordEntry.getAuxRecordEntry().getFkCostCenterIdXXX_n().equals(oPurConfig.getCostCenterId())) {
                                oMvtRecordEntry.setProrationPercentage(oPurConfig.getPercentage());
                                oMvtRecordEntry.setAuxQuantity(SLibUtils.round((oMvtRecordEntry.getProrationPercentage() * dQuantity), 3));
                                dQuantitySum = SLibUtils.round(dQuantitySum + oMvtRecordEntry.getAuxQuantity(), 3);
                                lMvtRecordEntriesCopy.remove(oMvtRecordEntry);
                                lMvtRecordEntriesToUpdate.add(oMvtRecordEntry);
                                break;
                        }
                    }
                }
                // si hay diferencia entre la suma de las cantidades, la diferencia se suma o
                // resta de la cantidad del último registro
                // para garantizar que se mantenga la cantidad original:
                if (dQuantity != dQuantitySum) {
                    lMvtRecordEntriesToUpdate.get(0).setAuxQuantity(SLibUtils
                            .round((lMvtRecordEntriesToUpdate.get(0).getAuxQuantity() + (dQuantity - dQuantitySum)), 3));
                }

                // Actualización de los registros de la tabla de partidas de valuación de
                // existencias y de los registros contables:
                for (SDbStockValuationAccount oMvtRecordEntryToUpdate : lMvtRecordEntriesToUpdate) {
                    String query = "UPDATE " + SModConsts.TablesMap.get(SModConsts.FIN_REC_ETY) + " SET units = '" + oMvtRecordEntryToUpdate.getAuxQuantity()
                            + "' WHERE (id_year = '" + oMvtRecordEntryToUpdate.getAuxRecordEntry().getPkYearId()
                            + "') and (id_per = '" + oMvtRecordEntryToUpdate.getAuxRecordEntry().getPkPeriodId()
                            + "') and (id_bkc = '" + oMvtRecordEntryToUpdate.getAuxRecordEntry().getPkBookkeepingCenterId()
                            + "') and (id_tp_rec = '" + oMvtRecordEntryToUpdate.getAuxRecordEntry().getPkRecordTypeId()
                            + "') and (id_num = '" + oMvtRecordEntryToUpdate.getAuxRecordEntry().getPkNumberId()
                            + "') and (id_ety = '" + oMvtRecordEntryToUpdate.getAuxRecordEntry().getPkEntryId() + "');";
                        
                    session.getStatement().getConnection().createStatement().executeUpdate(query);

                    String query2 = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_STK_VAL_ACC) + " SET prorat_per = '"
                            + oMvtRecordEntryToUpdate.getProrationPercentage() + "' WHERE (id_stk_val_acc = '"
                            + oMvtRecordEntryToUpdate.getPkStockValuationAccountId() + "');";

                    session.getStatement().getConnection().createStatement().executeUpdate(query2);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SStockValuationRecordUpdate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SStockValuationRecordUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
