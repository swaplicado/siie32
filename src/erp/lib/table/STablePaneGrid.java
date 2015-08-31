/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import erp.lib.data.SDataRegistry;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class STablePaneGrid extends erp.lib.table.STablePane {

    protected int mnGridViewStatus;
    protected java.util.Vector<erp.lib.table.STableRow> mvGridRows; // holds all rows, even the deleted ones

    public STablePaneGrid(erp.client.SClientInterface client) {
        super(client);
        mvGridRows = new Vector<STableRow>();
        resetGrid();
    }

    private void restoreGridRows() {
        resetTableRows();

        switch (mnGridViewStatus) {
            case STableConstants.VIEW_STATUS_ALL:
                for (STableRow row : mvGridRows) {
                    super.addTableRow(row);
                }
                break;
            case STableConstants.VIEW_STATUS_ALIVE:
                for (STableRow row : mvGridRows) {
                    if (!((SDataRegistry) row.getData()).getIsDeleted()) {
                        super.addTableRow(row);
                    }
                }
                break;
            default:
        }
    }

    public void setGridViewStatus(int n) {
        mnGridViewStatus = n;
    }

    public int getGridViewStatus() {
        return mnGridViewStatus;
    }

    /** Holds all rows, even the deleted ones. */
    public java.util.Vector<erp.lib.table.STableRow> getGridRows() {
        return mvGridRows;
    }

    public void resetGrid() {
        super.reset();

        mnGridViewStatus = STableConstants.VIEW_STATUS_ALL;
        mvGridRows.clear();
    }

    @Override
    public void clearTable() {
        mvGridRows.clear();
        super.clearTable();
    }

    @Override
    public void clearTableRows() {
        mvGridRows.clear();
        super.clearTableRows();
    }

    @Override
    public void renderTable() {
        restoreGridRows();
        super.renderTable();
    }

    @Override
    public void renderTableRows() {
        restoreGridRows();
        super.renderTableRows();
    }

    @Override
    public void addTableRow(erp.lib.table.STableRow tableRow) {
        mvGridRows.add(tableRow);
        super.addTableRow(tableRow);
    }

    @Override
    public void setTableRow(erp.lib.table.STableRow tableRow, int row) {
        STableRow modelRow = super.getTableRow(row);

        for (int i = 0; i < mvGridRows.size(); i++) {
            if (modelRow == mvGridRows.get(i)) {
                mvGridRows.setElementAt(tableRow, i);
                break;
            }
        }

        super.setTableRow(tableRow, row);
    }

    @Override
    public void insertTableRow(erp.lib.table.STableRow tableRow, int row) {
        STableRow modelRow = super.getTableRow(row);

        for (int i = 0; i < mvGridRows.size(); i++) {
            if (modelRow == mvGridRows.get(i)) {
                mvGridRows.insertElementAt(tableRow, i);
                break;
            }
        }

        super.insertTableRow(tableRow, row);
    }

    @Override
    public erp.lib.table.STableRow removeTableRow(int row) {
        STableRow modelRow = super.getTableRow(row);

        for (int i = 0; i < mvGridRows.size(); i++) {
            if (modelRow == mvGridRows.get(i)) {
                mvGridRows.removeElementAt(i);
                break;
            }
        }

        return super.removeTableRow(row);
    }

    /**
     * Status bar for table pane is not needed.
     */
    @Override
    public void createTable() {
        createTable(null);
    }

    /**
     * Status bar for table pane bar is needed.
     */
    @Override
    public void createTable(javax.swing.event.ListSelectionListener poListSelectionListener) {
        super.createTable(poListSelectionListener);
        setTableHeaderChangesAllowed(false);
    }

    @Override
    public void setTableRowSelection(int index) {
        super.setTableRowSelection(index);
    }
}
