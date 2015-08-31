/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Sergio Flores
 */
public class STablePane extends javax.swing.JScrollPane {

    protected erp.client.SClientInterface miClient;

    protected javax.swing.JTable mjTable;
    protected erp.lib.table.STableModel moTableModel;
    protected java.lang.Object moDoubleClickObject;
    protected java.lang.reflect.Method moDoubleClickMethod;
    protected java.util.Vector<erp.lib.table.STableField> mvPrimaryKeyFields;
    protected java.util.Vector<erp.lib.table.STableField> mvAditionalFields;

    public STablePane(erp.client.SClientInterface client) {
        miClient = client;
        mvPrimaryKeyFields = new Vector<STableField>();
        mvAditionalFields = new Vector<STableField>();
        reset();
    }

    protected void resetTable() {
        moTableModel.clearTable();
        if (mjTable != null ) {
            mjTable.invalidate();
            validate();
        }
    }

    protected void resetTableRows() {
        moTableModel.clearTableRows();
        if (mjTable != null ) {
            mjTable.invalidate();
            validate();
        }
    }

    public void setTable(javax.swing.JTable o) { mjTable = o; }
    public void setTableModel(erp.lib.table.STableModel o) { moTableModel = o; }
    public void setDoubleClickObject(java.lang.Object o) { moDoubleClickObject = o; }
    public void setDoubleClickMethod(java.lang.reflect.Method o) { moDoubleClickMethod = o; }

    public javax.swing.JTable getTable() { return mjTable; }
    public erp.lib.table.STableModel getTableModel() { return moTableModel; }
    public java.lang.Object getDoubleClickObject() { return moDoubleClickObject; }
    public java.lang.reflect.Method getDoubleClickMethod() { return moDoubleClickMethod; }
    public java.util.Vector<erp.lib.table.STableField> getPrimaryKeyFields() { return mvPrimaryKeyFields; }
    public java.util.Vector<erp.lib.table.STableField> getAditionalFields() { return mvAditionalFields; }

    public void reset() {
        mjTable = null;
        moTableModel = new STableModel();
        moDoubleClickObject = null;
        moDoubleClickMethod = null;
        mvPrimaryKeyFields.clear();
        mvAditionalFields.clear();
    }

    public void clearTable() {
        resetTable();
        renderTable();
    }

    public void clearTableRows() {
        resetTableRows();
        renderTableRows();
    }

    public void renderTable() {
        moTableModel.renderTable();
    }

    public void renderTableRows() {
        moTableModel.renderTableRows();
    }

    public void addTableRow(erp.lib.table.STableRow tableRow) {
        moTableModel.addTableRow(tableRow);
    }

    public void addTableColumn(erp.lib.table.STableColumn tableColumn) {
        moTableModel.addTableColumn(tableColumn);
    }

    public void setTableRow(erp.lib.table.STableRow tableRow, int row) {
        moTableModel.setTableRow(tableRow, mjTable == null || mjTable.getRowCount() == 0 ? row : mjTable.convertRowIndexToModel(row));
    }

    public void setTableColumn(erp.lib.table.STableColumn tableColumn, int col) {
        moTableModel.setTableColumn(tableColumn, mjTable == null || mjTable.getColumnCount() == 0 ? col : mjTable.convertColumnIndexToModel(col));
    }

    public void insertTableRow(erp.lib.table.STableRow tableRow, int row) {
        moTableModel.insertTableRow(tableRow, mjTable == null || mjTable.getRowCount() == 0 ? row : mjTable.convertRowIndexToModel(row));
    }

    public void insertTableColumn(erp.lib.table.STableColumn tableColumn, int col) {
        moTableModel.insertTableColumn(tableColumn, mjTable == null || mjTable.getColumnCount() == 0 ? col : mjTable.convertColumnIndexToModel(col));
    }

    public erp.lib.table.STableRow removeTableRow(int row) {
        return moTableModel.removeTableRow(mjTable == null || mjTable.getRowCount() == 0 ? row : mjTable.convertRowIndexToModel(row));
    }

    public erp.lib.table.STableColumn removeTableColumn(int col) {
        return moTableModel.removeTableColumn(mjTable == null || mjTable.getColumnCount() == 0 ? col : mjTable.convertColumnIndexToModel(col));
    }

    public erp.lib.table.STableRow getTableRow(int row) {
        return moTableModel.getTableRow(mjTable == null || mjTable.getRowCount() == 0 ? row : mjTable.convertRowIndexToModel(row));
    }

    public erp.lib.table.STableColumn getTableColumn(int col) {
        return moTableModel.getTableColumn(mjTable == null || mjTable.getColumnCount() == 0 ? col : mjTable.convertColumnIndexToModel(col));
    }

    public int getTableGuiRowCount() {
        return mjTable == null ? 0 : mjTable.getRowCount();
    }

    public int getTableModelRowCount() {
        return moTableModel.getRowCount();
    }

    public int getTableGuiColumnCount() {
        return mjTable == null ? 0 : mjTable.getColumnCount();
    }

    public int getTableModelColumnCount() {
        return moTableModel.getColumnCount();
    }

    public erp.lib.table.STableRow getSelectedTableRow() {
        return mjTable.getSelectedRow() == -1 ? null : moTableModel.getTableRow(mjTable.convertRowIndexToModel(mjTable.getSelectedRow()));
    }

    public void setTableRowSelection(int index) {
        int value = 0;

        if (mjTable != null && index >= 0 && index < mjTable.getRowCount()) {
            mjTable.setRowSelectionInterval(index, index);

            value = index * mjTable.getRowHeight();
            if (value < getVerticalScrollBar().getValue() || value > getVerticalScrollBar().getValue() + getVerticalScrollBar().getVisibleAmount()) {
                getVerticalScrollBar().setValue(value);
            }
        }
    }

    public void setTableColumnSelection(int index) {
        if (mjTable != null && index >= 0 && index < mjTable.getColumnCount()) {
            mjTable.setColumnSelectionInterval(index, index);
        }
    }

    public void setTableHeaderChangesAllowed(boolean fixed) {
        //mjTable.getTableHeader().setResizingAllowed(fixed);
        mjTable.getTableHeader().setReorderingAllowed(fixed);
    }

    public void setDoubleClickAction(java.lang.Object object, java.lang.String method) {
        try {
            moDoubleClickObject = object;
            moDoubleClickMethod = object.getClass().getMethod(method, (java.lang.Class<?>[]) null);
        }
        catch (java.lang.NoSuchMethodException e) {
            SLibUtilities.renderException(this, e);
        }
        catch (java.lang.SecurityException e) {
            SLibUtilities.renderException(this, e);
        }
    }

    public void layoutTable() {
        int i = 0;
        TableCellRenderer cellRenderer = null;

        for (i = 0; i < moTableModel.getColumnCount(); i++) {
            if (moTableModel.getTableColumn(i).getCellRenderer() != null) {
                cellRenderer = moTableModel.getTableColumn(i).getCellRenderer();
            }
            else {
                switch (moTableModel.getTableColumn(i).getColumnType()) {
                    case SLibConstants.DATA_TYPE_BOOLEAN:
                        cellRenderer = moTableModel.getTableColumn(i).isEditable() ? null : miClient.getSessionXXX().getFormatters().getTableCellRendererBoolean();
                        break;
                    case SLibConstants.DATA_TYPE_INTEGER:
                    case SLibConstants.DATA_TYPE_LONG:
                        cellRenderer = miClient.getSessionXXX().getFormatters().getTableCellRendererNumberLong();
                        break;
                    case SLibConstants.DATA_TYPE_FLOAT:
                    case SLibConstants.DATA_TYPE_DOUBLE:
                        cellRenderer = miClient.getSessionXXX().getFormatters().getTableCellRendererNumberDouble();
                        break;
                    case SLibConstants.DATA_TYPE_DATE:
                        cellRenderer = miClient.getSessionXXX().getFormatters().getTableCellRendererDate();
                        break;
                    case SLibConstants.DATA_TYPE_DATE_TIME:
                        cellRenderer = miClient.getSessionXXX().getFormatters().getTableCellRendererDatetime();
                        break;
                    case SLibConstants.DATA_TYPE_TIME:
                        cellRenderer = miClient.getSessionXXX().getFormatters().getTableCellRendererTime();
                        break;
                    default:
                        cellRenderer = miClient.getSessionXXX().getFormatters().getTableCellRendererDefault();
                        break;
                }
            }

            if (cellRenderer != null) {
                mjTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            }
            mjTable.getColumnModel().getColumn(i).setPreferredWidth(moTableModel.getTableColumn(i).getColumnWidth());
        }
    }

    /**
     * Status bar for table pane is not needed.
     */
    public void createTable() {
        createTable(null);
    }

    /**
     * Status bar for table pane bar is needed.
     */
    public void createTable(javax.swing.event.ListSelectionListener poListSelectionListener) {
        String[] toolTips = null;

        mjTable = new JTable(moTableModel);
        mjTable.setRowSorter(new TableRowSorter<AbstractTableModel>(moTableModel));
        mjTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mjTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mjTable.setColumnSelectionAllowed(false);

        if (poListSelectionListener != null) {
            mjTable.getSelectionModel().addListSelectionListener(poListSelectionListener);
        }

        mjTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (event.getClickCount() == 2 && moDoubleClickMethod != null && moDoubleClickObject != null) {
                    try {
                        moDoubleClickMethod.invoke(moDoubleClickObject);
                    }
                    catch (java.lang.IllegalAccessException e) {
                        SLibUtilities.renderException(this, e);
                    }
                    catch (java.lang.IllegalArgumentException e) {
                        SLibUtilities.renderException(this, e);
                    }
                    catch (java.lang.reflect.InvocationTargetException e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
            }
        });

        layoutTable();

        toolTips = new String[moTableModel.getColumnCount()];
        for (int i = 0; i < moTableModel.getColumnCount(); i++) {
            toolTips[i] = moTableModel.getTableColumn(i).getColumnTitle();
        }

        mjTable.setTableHeader(new STableToolTipHeader(mjTable.getColumnModel(), toolTips));

        setViewportView(mjTable);
    }
}
