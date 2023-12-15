/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.data.SDataConstants;
import erp.mod.trn.db.SDbIdentifiedCostCalculation.Supply;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Edwin Carmona
 */
public class STrnIdentifiedCostLotUtils {
    
    public static ArrayList<Supply> analizeLots(Connection conn, String externalDatabase, ArrayList<Supply> supplies) {
        for (Supply supply : supplies) {
//            if (supply.LotLotId == 0 && supply.SupplyLotId > 0) {
//                String lotResult = STrnIdentifiedCostLotUtils.getLot(conn, externalDatabase, supply.SupplyLotId, supply.DocEntryItemId, supply.DocEntryUnitId, supply.SupplyLotLot);
//                supply.LotSituationAux = lotResult;
//            }
            if (supply.LotLotId == 0 && supply.SupplyLotId == 0) {
                supply.LotSituationAux = "No existen surtidos para esta partida";
            }
            else if (supply.LotLotId == 0) {
                String lotResult = STrnIdentifiedCostLotUtils.searchSiieLot(conn, supply.SupplyLotLot, supply.DocEntryItemId, supply.DocEntryUnitId);
                if (! lotResult.isEmpty()) {
//                    supply.LotSituationAux += supply.LotSituationAux.isEmpty() ? lotResult : ", " + lotResult;
                    supply.LotSituationAux += lotResult;
                }
            }
        }
        
        return supplies;
    }
    
    private static String searchSiieLot(Connection conn, String lot, int idItemSupply, int idUnitSupply) {
        String sqlSiie = "SELECT "
                + " * "
                + "FROM "
                + SDataConstants.TablesMap.get(SDataConstants.TRN_LOT) + " "
                + "WHERE "
                + "lot = '" + lot + "' "
                + "AND NOT b_del;";
        
        SIdentifiedAuxLot oLotAux;
        try {
            ResultSet res = conn.createStatement().executeQuery(sqlSiie);
            if (res.next()) {
                oLotAux = new SIdentifiedAuxLot();
                
                oLotAux.idItem = res.getInt("id_item");
                oLotAux.idUnit = res.getInt("id_unit");
                oLotAux.idLot = res.getInt("id_lot");
                
                if (oLotAux.idItem != idItemSupply || oLotAux.idUnit != idUnitSupply) {
                    return "El lote existente en siie no corresponde con el ítem o la unidad del surtido";
                }
            }
            else {
                return "No existe lote en siie";
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnIdentifiedCostLotUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
    
    private static String getLot(Connection conn, String externalDatabase, int idLot, int idItem, int idUnit, String lot) {
        String sqlLot = "SELECT  "
                + "    l.id_lot, "
                + "    l.dt_expiry, "
                + "    l.item_id, "
                + "    l.unit_id, "
                + "    i.external_id AS external_item, "
                + "    u.external_id AS external_unit "
                + "FROM "
                + "    " + externalDatabase + ".wms_lots AS l "
                + "        INNER JOIN "
                + "    " + externalDatabase + ".erpu_items AS i ON l.item_id = i.id_item "
                + "        INNER JOIN "
                + "    " + externalDatabase + ".erpu_units AS u ON l.unit_id = u.id_unit "
                + "WHERE "
                + "    l.lot = '" + lot + "' AND NOT l.is_deleted "
                + "ORDER BY l.created_at DESC;";
        
        ArrayList<SIdentifiedAuxLot> lLots = new ArrayList<>();
        try {
            ResultSet res = conn.createStatement().executeQuery(sqlLot);
            SIdentifiedAuxLot oLotAux;
            while (res.next()) {
                oLotAux = new SIdentifiedAuxLot();
                
                oLotAux.idLotExternal = res.getInt("id_lot");
                oLotAux.idItem = res.getInt("external_item");
                oLotAux.idUnit = res.getInt("external_unit");
                oLotAux.expiration = res.getDate("dt_expiry");
                
                if (idLot == oLotAux.idLotExternal) {
                    if (oLotAux.idItem != idItem || oLotAux.idUnit != idUnit) {
                        return "El ítem o la unidad no corresponden con el lote del surtido";
                    }
                }
                
                lLots.add(oLotAux);
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(STrnIdentifiedCostLotUtils.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        
        if (lLots.isEmpty()) {
            return "El lote no existe en el sistema externo";
        }
        else {
            if (lLots.size() == 1) {
                SIdentifiedAuxLot oLot = lLots.get(0);
                
                if (idItem != oLot.idItem || idUnit != oLot.idUnit) {
                    return "El ítem o la unidad no corresponden con el lote";
                }
            }
            else {
                return "Hay varios lotes en el sistema externo que corresponden al lote consultado";
            }
        }
        
        return "";
    }
}
