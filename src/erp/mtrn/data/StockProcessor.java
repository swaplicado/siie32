/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Edwin Carmona
 */
class SItemUnitLotAux {
    private final int idItem;
    private final int idUnit;
    private final int idLot;

    public SItemUnitLotAux(int idItem, int idUnit, int idLot) {
        this.idItem = idItem;
        this.idUnit = idUnit;
        this.idLot = idLot;
    }
    public int getIdItem() {
        return idItem;
    }

    public int getIdUnit() {
        return idUnit;
    }

    public int getIdLot() {
        return idLot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SItemUnitLotAux)) return false;
        SItemUnitLotAux that = (SItemUnitLotAux) o;
        return idItem == that.idItem && idUnit == that.idUnit && idLot == that.idLot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idItem, idUnit, idLot);
    }
}

public class StockProcessor {

    /**
     *
     * @param vStock
     * @return
     */
    public static Set<SItemUnitLotAux> getUniqueItemUnitLot(List<STrnStockMove> vStock) {
        Set<SItemUnitLotAux> uniqueItems = new HashSet<>();
        for (STrnStockMove move : vStock) {
            uniqueItems.add(new SItemUnitLotAux(move.getPkItemId(), move.getPkUnitId(), move.getPkLotId()));
        }
        return uniqueItems;
    }

    /**
     *
     * @param vStock
     * @param itemUnitLot
     * @return
     */
    public static ArrayList<STrnStockMove> getStockByItemUnitLot(List<STrnStockMove> vStock, SItemUnitLotAux itemUnitLot) {
        ArrayList<STrnStockMove> stock = new ArrayList<>();
        for (STrnStockMove move : vStock) {
            if (move.getPkItemId() == itemUnitLot.getIdItem() && move.getPkUnitId() == itemUnitLot.getIdUnit() && move.getPkLotId() == itemUnitLot.getIdLot()) {
                stock.add(move);
            }
        }
        return stock;
    }
}
