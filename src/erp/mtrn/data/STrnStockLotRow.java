/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

/**
 *
 * @author Sergio Flores
 */
public class STrnStockLotRow extends erp.lib.table.STableRow {

    public static final int TYPE_QTY = 1;
    public static final int TYPE_QTY_VAL = 2;

    protected int mnType;

    /**
     * Enables STrnStockMove objects to be rendered in table panes.
     * @param stockMove Stock move object to be rendered in table pane rows.
     * @param type Row type. Constants defined in this class:
     * a) quantity (TYPE_QTY) or
     * b) quantity and value (TYPE_QTY_VAL).
     */
    public STrnStockLotRow(STrnStockMove stockMove, int type) {
        moData = stockMove;
        mnType = type;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        STrnStockMove stockMove = (STrnStockMove) moData;

        mvValues.clear();

        switch (mnType) {
            case TYPE_QTY:
                mvValues.add(stockMove.getAuxRowNumber());
                mvValues.add(stockMove.getQuantity());
                mvValues.add(stockMove.getAuxLot());
                mvValues.add(stockMove.getAuxLotDateExpiration());
                break;
            case TYPE_QTY_VAL:
                mvValues.add(stockMove.getAuxRowNumber());
                mvValues.add(stockMove.getQuantity());
                mvValues.add(stockMove.getAuxLot());
                mvValues.add(stockMove.getAuxLotDateExpiration());
                mvValues.add(stockMove.getAuxIsLotBlocked());
                mvValues.add(stockMove.getValue());
                break;
            default:
        }
    }
}
