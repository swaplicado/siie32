/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import erp.lib.SLibConstants;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class STableModel extends javax.swing.table.AbstractTableModel {

    private java.util.Vector<erp.lib.table.STableRow> mvTableRows;
    private java.util.Vector<erp.lib.table.STableColumn> mvTableColumns;

    public STableModel() {
        mvTableRows = new Vector<STableRow>();
        mvTableColumns = new Vector<STableColumn>();
    }

    public java.util.Vector<erp.lib.table.STableRow> getTableRows() { return mvTableRows; }
    public java.util.Vector<erp.lib.table.STableColumn> getTableColumns() { return mvTableColumns; }

    @Override
    public int getRowCount() {
        return mvTableRows.size();
    }

    @Override
    public int getColumnCount() {
        return mvTableColumns.size();
    }

    @Override
    public java.lang.Object getValueAt(int row, int col) {
        return mvTableRows.get(row).getValues().get(col);
    }

    @Override
    public java.lang.String getColumnName(int col) {
        return mvTableColumns.get(col).getColumnTitle();
    }

    @Override
    public java.lang.Class getColumnClass(int col) {
        java.lang.Class c = null;

        switch (mvTableColumns.get(col).getColumnType()) {
            case SLibConstants.DATA_TYPE_BOOLEAN:
                c = java.lang.Boolean.class;
                break;
            case SLibConstants.DATA_TYPE_INTEGER:
                c = java.lang.Integer.class;
                break;
            case SLibConstants.DATA_TYPE_LONG:
                c = java.lang.Long.class;
                break;
            case SLibConstants.DATA_TYPE_FLOAT:
                c = java.lang.Float.class;
                break;
            case SLibConstants.DATA_TYPE_DOUBLE:
                c = java.lang.Double.class;
                break;
            case SLibConstants.DATA_TYPE_STRING:
                c = java.lang.String.class;
                break;
            case SLibConstants.DATA_TYPE_DATE:
                c = java.util.Date.class;
                break;
            case SLibConstants.DATA_TYPE_DATE_TIME:
                c = java.util.Date.class;
                break;
            default:
        }

        return c;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return mvTableColumns.get(col).isEditable();
    }

    @Override
    public void setValueAt(java.lang.Object value, int row, int col) {
        mvTableRows.get(row).getValues().setElementAt(value, col);
    }

    public void clearTable() {
        mvTableRows.clear();
        mvTableColumns.clear();
    }

    public void clearTableRows() {
        mvTableRows.clear();
    }

    public void renderTable() {
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    public void renderTableRows() {
        fireTableDataChanged();
    }

    public void addTableRow(erp.lib.table.STableRow tableRow) {
        mvTableRows.add(tableRow);
    }

    public void addTableColumn(erp.lib.table.STableColumn tableColumn) {
        mvTableColumns.add(tableColumn);
    }

    public void setTableRow(erp.lib.table.STableRow tableRow, int row) {
        mvTableRows.setElementAt(tableRow, row);
    }

    public void setTableColumn(erp.lib.table.STableColumn tableColumn, int col) {
        mvTableColumns.setElementAt(tableColumn, col);
    }

    public void insertTableRow(erp.lib.table.STableRow tableRow, int row) {
        mvTableRows.insertElementAt(tableRow, row);
    }

    public void insertTableColumn(erp.lib.table.STableColumn tableColumn, int col) {
        mvTableColumns.insertElementAt(tableColumn, col);
    }

    public erp.lib.table.STableRow removeTableRow(int row) {
        return mvTableRows.remove(row);
    }

    public erp.lib.table.STableColumn removeTableColumn(int col) {
        return mvTableColumns.remove(col);
    }

    public erp.lib.table.STableRow getTableRow(int row) {
        return mvTableRows.get(row);
    }

    public erp.lib.table.STableColumn getTableColumn(int col) {
        return mvTableColumns.get(col);
    }
}
