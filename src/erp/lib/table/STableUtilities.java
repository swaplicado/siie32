/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SortOrder;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class STableUtilities {

    private static int seek(erp.lib.table.STablePane tablePane, int pnCol, javax.swing.SortOrder poSortOrder, int pnFirstRow, int pnLastRow, double pdValue) {
        int nMiddleRow = 0;
        double dComparison = 0;
        
        nMiddleRow = pnFirstRow + ((pnLastRow - pnFirstRow) / 2);
        dComparison = pdValue - ((Number) tablePane.getTable().getValueAt(nMiddleRow, pnCol)).doubleValue();

        if (dComparison == 0) {
            return nMiddleRow;
        }

        if (poSortOrder == SortOrder.ASCENDING) {
            if (dComparison < 0) {
                if ((nMiddleRow - 1) >= pnFirstRow) {
                    return seek(tablePane, pnCol, poSortOrder, pnFirstRow, nMiddleRow - 1, pdValue);
                }
            }
            else {
                if (pnLastRow >= (nMiddleRow + 1)) {
                    return seek(tablePane, pnCol, poSortOrder, nMiddleRow + 1, pnLastRow, pdValue);
                }
            }
        }
        else if (poSortOrder == SortOrder.DESCENDING) {
            if (dComparison < 0) {
                if (pnLastRow >= (nMiddleRow + 1)) {
                    return seek(tablePane, pnCol, poSortOrder, nMiddleRow + 1, pnLastRow, pdValue);
                }
            }
            else {
                if ((nMiddleRow - 1) >= pnFirstRow) {
                    return seek(tablePane, pnCol, poSortOrder, pnFirstRow, nMiddleRow - 1, pdValue);
                }
            }
        }

        return -1;
    }

    private static int seek(erp.lib.table.STablePane tablePane, int pnCol, javax.swing.SortOrder poSortOrder, int pnFirstRow, int pnLastRow, java.lang.String psValue) {
        int nMiddleRow = 0;
        int nComparison = 0;
        
        nMiddleRow = pnFirstRow + ((pnLastRow - pnFirstRow) / 2);
        nComparison = psValue.compareToIgnoreCase(
        (((java.lang.String) tablePane.getTable().getValueAt(nMiddleRow, pnCol)).length() > psValue.length() ?
        ((java.lang.String) tablePane.getTable().getValueAt(nMiddleRow, pnCol)).substring(0, psValue.length()) :
        (java.lang.String) tablePane.getTable().getValueAt(nMiddleRow, pnCol)));

        if (nComparison == 0) {
            return nMiddleRow;
        }

        if (poSortOrder == SortOrder.ASCENDING) {
            if (nComparison < 0) {
                if ((nMiddleRow - 1) >= pnFirstRow) {
                    return seek(tablePane, pnCol, poSortOrder, pnFirstRow, nMiddleRow - 1, psValue);
                }
            }
            else {
                if (pnLastRow >= (nMiddleRow + 1)) {
                    return seek(tablePane, pnCol, poSortOrder, nMiddleRow + 1, pnLastRow, psValue);
                }
            }
        }
        else if (poSortOrder == SortOrder.DESCENDING) {
            if (nComparison < 0) {
                if (pnLastRow >= (nMiddleRow + 1)) {
                    return seek(tablePane, pnCol, poSortOrder, nMiddleRow + 1, pnLastRow, psValue);
                }
            }
            else {
                if ((nMiddleRow - 1) >= pnFirstRow) {
                    return seek(tablePane, pnCol, poSortOrder, pnFirstRow, nMiddleRow - 1, psValue);
                }
            }
        }

        return -1;
    }

    private static int seek(erp.lib.table.STablePane tablePane, int pnCol, javax.swing.SortOrder poSortOrder, int pnFirstRow, int pnLastRow, java.util.Date ptValue) {
        int nMiddleRow = 0;
        int nComparison = 0;
        
            nMiddleRow = pnFirstRow + ((pnLastRow - pnFirstRow) / 2);
            nComparison = ptValue.compareTo((java.util.Date) tablePane.getTable().getValueAt(nMiddleRow, pnCol));

            if (nComparison == 0) {
                return nMiddleRow;
            }

            if (poSortOrder == SortOrder.ASCENDING) {
                if (nComparison < 0) {
                    if ((nMiddleRow - 1) >= pnFirstRow) {
                        return seek(tablePane, pnCol, poSortOrder, pnFirstRow, nMiddleRow - 1, ptValue);
                    }
                }
                else {
                    if (pnLastRow >= (nMiddleRow + 1)) {
                        return seek(tablePane, pnCol, poSortOrder, nMiddleRow + 1, pnLastRow, ptValue);
                    }
                }
            }
            else if (poSortOrder == SortOrder.DESCENDING) {
                if (nComparison < 0) {
                    if (pnLastRow >= (nMiddleRow + 1)) {
                        return seek(tablePane, pnCol, poSortOrder, nMiddleRow + 1, pnLastRow, ptValue);
                    }
                }
                else {
                    if ((nMiddleRow - 1) >= pnFirstRow) {
                        return seek(tablePane, pnCol, poSortOrder, pnFirstRow, nMiddleRow - 1, ptValue);
                    }
                }
            }

        return -1;
    }

    public static void actionSeek(erp.client.SClientInterface piClient, erp.lib.table.STablePane poTablePane, java.lang.String psValue) {
        int col = 0;
        int row = 0;
        int rows = 0;
        double dValue = 0;
        java.util.Date tValue = null;
        javax.swing.SortOrder sortOrder = null;

        if (poTablePane.getTable() != null) {
            if (poTablePane.getTable().getRowSorter().getSortKeys().isEmpty()) {
                col = poTablePane.getTable().convertColumnIndexToView(0);
                row = -1;
                rows = poTablePane.getTableGuiRowCount();
                sortOrder = javax.swing.SortOrder.ASCENDING;
            }
            else {
                col = poTablePane.getTable().convertColumnIndexToView(((javax.swing.RowSorter.SortKey) poTablePane.getTable().getRowSorter().getSortKeys().get(0)).getColumn());
                row = -1;
                rows = poTablePane.getTableGuiRowCount();
                sortOrder = ((javax.swing.RowSorter.SortKey) poTablePane.getTable().getRowSorter().getSortKeys().get(0)).getSortOrder();
            }

            if (rows > 0) {
                if (col >= 0 && col < poTablePane.getTable().getColumnCount()) {
                    switch (poTablePane.getTableColumn(col).getColumnType()) {
                        case SLibConstants.DATA_TYPE_BOOLEAN:
                        case SLibConstants.DATA_TYPE_INTEGER:
                        case SLibConstants.DATA_TYPE_LONG:
                        case SLibConstants.DATA_TYPE_FLOAT:
                        case SLibConstants.DATA_TYPE_DOUBLE:
                            dValue = SLibUtilities.parseDouble(psValue);
                            row = seek(poTablePane, col, sortOrder, 0, rows - 1, dValue);
                            break;
                        case SLibConstants.DATA_TYPE_STRING:
                            row = seek(poTablePane, col, sortOrder, 0, rows - 1, psValue);
                            break;
                        case SLibConstants.DATA_TYPE_DATE:
                        case SLibConstants.DATA_TYPE_DATE_TIME:
                        case SLibConstants.DATA_TYPE_TIME:
                            SimpleDateFormat dateFormat = null;

                            if (poTablePane.getTableColumn(col).getColumnType() == SLibConstants.DATA_TYPE_DATE) {
                                dateFormat = piClient.getSessionXXX().getFormatters().getDateFormat();
                            }
                            else if (poTablePane.getTableColumn(col).getColumnType() == SLibConstants.DATA_TYPE_DATE_TIME) {
                                dateFormat = piClient.getSessionXXX().getFormatters().getDatetimeFormat();
                            }
                            else {
                                dateFormat = piClient.getSessionXXX().getFormatters().getTimeFormat();
                            }

                            try {
                                tValue = dateFormat.parse(psValue);
                                row = seek(poTablePane, col, sortOrder, 0, rows - 1, tValue);
                            }
                            catch (java.text.ParseException e) {
                                SLibUtilities.renderException(STableTab.class.getName(), e);
                            }
                            break;
                        default:
                            break;
                    }
                    
                    if (row == -1) {
                        row = seekGeneral(poTablePane, psValue);
                    }
                    
                    if (row == -1) {
                        piClient.showMsgBoxWarning("No se encontró ningún registro para el valor '" + psValue + "'.");
                    }
                    else {
                        try {
                            // Go back to first occurrence of value:
                            switch (poTablePane.getTableColumn(col).getColumnType()) {
                                case SLibConstants.DATA_TYPE_BOOLEAN:
                                case SLibConstants.DATA_TYPE_INTEGER:
                                case SLibConstants.DATA_TYPE_LONG:
                                case SLibConstants.DATA_TYPE_FLOAT:
                                case SLibConstants.DATA_TYPE_DOUBLE:
                                    while (row > 0 && dValue == ((Number) poTablePane.getTable().getValueAt(row - 1, col)).doubleValue()) {
                                        row--;
                                    }
                                    break;
                                case SLibConstants.DATA_TYPE_STRING:
                                    while (row > 0 && psValue.compareToIgnoreCase(
                                    (((String) poTablePane.getTable().getValueAt(row - 1, col)).length() > psValue.length() ?
                                    ((String) poTablePane.getTable().getValueAt(row - 1, col)).substring(0, psValue.length()) :
                                    (String) poTablePane.getTable().getValueAt(row - 1, col))) == 0) {
                                        row--;
                                    }
                                    break;
                                case SLibConstants.DATA_TYPE_DATE:
                                case SLibConstants.DATA_TYPE_DATE_TIME:
                                case SLibConstants.DATA_TYPE_TIME:
                                    while (row > 0 && tValue.compareTo((java.util.Date) poTablePane.getTable().getValueAt(row - 1, col)) == 0) {
                                        row--;
                                    }
                                    break;
                                default:
                            }
                        }
                        catch (Exception e) {
                            SLibUtilities.printOutException(STableUtilities.class, e);
                        }

                        // Scroll to value:
                        poTablePane.setTableRowSelection(row);
                        poTablePane.getTable().requestFocus();
                    }
                }
            }
        }
    }
    
    private static int seekGeneral(erp.lib.table.STablePane poTablePane, String psValue) {
        int result = -1;
        int columnIndex = 0;
        					
        // While the column is not the last
        while (columnIndex < poTablePane.getTable().getColumnCount()) {
            switch (poTablePane.getTableColumn(columnIndex).getColumnType()) {
                case SLibConstants.DATA_TYPE_STRING:
                    result = seekByString(poTablePane, columnIndex, psValue);
                    break;
                case SLibConstants.DATA_TYPE_BOOLEAN:
                case SLibConstants.DATA_TYPE_DOUBLE:
                case SLibConstants.DATA_TYPE_FLOAT:
                case SLibConstants.DATA_TYPE_INTEGER:
                case SLibConstants.DATA_TYPE_LONG:
                    result = seekByPrimitiveValue(poTablePane, columnIndex, psValue);
                    break;
                case SLibConstants.DATA_TYPE_DATE:
                case SLibConstants.DATA_TYPE_DATE_TIME:
                case SLibConstants.DATA_TYPE_TIME:
                    result = seekByDate(poTablePane, columnIndex, psValue);
                    break;
                default:
            }
            
            if (result > -1) {
                break;
            }
            else {
                columnIndex++;
            }
        }

        return result;
    }

    private static int seekByString(erp.lib.table.STablePane poTablePane, int column, String psValue) {
        int row = 0;
        int numRows = poTablePane.getTableGuiRowCount();
        String value = "";
        int result = -1;
        
        while (row < numRows) {
            // Get the value of the table in the search column and current row as string
            value = (String) poTablePane.getTable().getValueAt(row, column);
            if (value != null && value.toUpperCase().contains(psValue.toUpperCase())) {
                result = row;
                break;
            }
            row++;
        }
    
	// If the value was not found, return -1	
        return result;
    }
    
    private static int seekByPrimitiveValue(erp.lib.table.STablePane poTablePane, int column, String psValue) {
        int row = 0;
        int numRows = poTablePane.getTableGuiRowCount();
        String value = "";
        int result = -1;
        
        while (row < numRows) {
            try {
                // Get the value of the table in the search column and current row and it is transformed into a string
                value = ((Number) poTablePane.getTable().getValueAt(row, column)).toString();
                if (value != null && value.toUpperCase().contains(psValue.toUpperCase())) {
                    result = row;
                    break;
                }
            }
            catch (Exception e) {
                SLibUtilities.printOutException(STableUtilities.class, e);
            }

            row++;
        }
        
        // If the value was not found, return -1
        return result;
    }
    
    private static int seekByDate(erp.lib.table.STablePane poTablePane, int column, String psValue) {
        int row = 0;
        int numRows = poTablePane.getTableGuiRowCount();
        String value = "";
        int result = -1;
        SimpleDateFormat formatDate = null;
        
        switch (poTablePane.getTableColumn(column).getColumnType()) {
            case SLibConstants.DATA_TYPE_DATE:
                formatDate = SLibUtils.DateFormatDate;
                break;
            case SLibConstants.DATA_TYPE_DATE_TIME:
                formatDate = SLibUtils.DateFormatDatetime;
                break;
            case SLibConstants.DATA_TYPE_TIME:
                formatDate = SLibUtils.DateFormatTime;
                break;
            default:
                formatDate = SLibUtils.DateFormatDate;
        }
        
        while (row < numRows) {
            try {
                // Get the value of the table in the search column and current row as date and format
                value = formatDate.format((Date) poTablePane.getTable().getValueAt(row, column));
                if (value != null && value.toUpperCase().contains(psValue.toUpperCase())) {
                    result = row;
                    break;
                }
            }
            catch (Exception e) {
                SLibUtilities.printOutException(STableUtilities.class, e);
            }
            
            row++;
        }
        
        // If the value was not found, return -1
        return result;
    }

    public static void actionExportCsv(erp.client.SClientInterface client, erp.lib.table.STablePane tablePane, java.lang.String title) {
        int col = 0;
        int colIndex = 0;
        int row = 0;
        boolean[] isApostropheOnCsvRequiredArray = null;
        java.lang.String buffer = "";
        java.util.Vector values = null;

        client.getFileChooser().setSelectedFile(new File(title + " " + client.getSessionXXX().getFormatters().getFileNameDatetimeFormat().format(new java.util.Date()) + ".csv"));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath() + (client.getFileChooser().getSelectedFile().getAbsolutePath().endsWith(".csv") ? "" : ".csv"));

            try {
                // Prepare apostrophe control for strings:
                isApostropheOnCsvRequiredArray = new boolean[tablePane.getTable().getColumnCount()];
                for (col = 0; col < tablePane.getTable().getColumnCount(); col++) {
                    isApostropheOnCsvRequiredArray[col] = tablePane.getTableColumn(col).isApostropheOnCsvRequired();
                }

                //FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

                for (col = 0; col < tablePane.getTable().getColumnCount(); col++) {
                    buffer += (buffer.length() == 0 ? "" : ",") + "\"" + SLibUtilities.textToAscii(tablePane.getTableColumn(col).getColumnTitle()) + "\"";
                }
                //fw.write(buffer);
                bw.write(buffer);

                for (row = 0; row < tablePane.getTableGuiRowCount(); row++) {
                    buffer = "";
                    values = tablePane.getTableRow(row).getValues();

                    for (col = 0; col < tablePane.getTable().getColumnCount(); col++) {
                        buffer += (col == 0 ? "" : ",");
                        colIndex = tablePane.getTable().convertColumnIndexToModel(col);

                        if (values.get(colIndex) != null) {
                            switch (tablePane.getTableColumn(col).getColumnType()) {
                                case SLibConstants.DATA_TYPE_BOOLEAN:
                                    buffer += (((Boolean) values.get(colIndex)).booleanValue() ? "1" : "0");
                                    break;
                                case SLibConstants.DATA_TYPE_INTEGER:
                                case SLibConstants.DATA_TYPE_LONG:
                                    buffer += ((Number) values.get(colIndex)).longValue();
                                    break;
                                case SLibConstants.DATA_TYPE_FLOAT:
                                case SLibConstants.DATA_TYPE_DOUBLE:
                                    buffer += ((Number) values.get(colIndex)).doubleValue();
                                    break;
                                case SLibConstants.DATA_TYPE_STRING:
                                    buffer += "\"" + (!isApostropheOnCsvRequiredArray[col] ? "" : "'") + ((String) values.get(colIndex)).replaceAll("\"", "\"\"") + "\"";
                                    break;
                                case SLibConstants.DATA_TYPE_DATE:
                                    buffer += client.getSessionXXX().getFormatters().getCsvDateFormat().format((java.util.Date) values.get(colIndex));
                                    break;
                                case SLibConstants.DATA_TYPE_DATE_TIME:
                                    buffer += client.getSessionXXX().getFormatters().getCsvDatetimeFormat().format((java.util.Date) values.get(colIndex));
                                    break;
                                case SLibConstants.DATA_TYPE_TIME:
                                    buffer += client.getSessionXXX().getFormatters().getCsvTimeFormat().format((java.util.Date) values.get(colIndex));
                                    break;
                                default:
                                    buffer += "\"?\"";
                            }
                        }
                    }
                    //fw.write("\n");
                    //fw.write(buffer);
                    bw.write("\n");
                    bw.write(SLibUtilities.textToAscii(buffer));
                }

                //fw.flush();
                //fw.close();
                bw.flush();
                bw.close();

                if (client.showMsgBoxConfirm(SLibConstants.MSG_INF_FILE_CREATE + file.getPath() + "\n" + SLibConstants.MSG_CNF_FILE_OPEN) == JOptionPane.YES_OPTION) {
                    SLibUtilities.launchFile(file.getPath());
                }
            }
            catch (java.lang.Exception e) {
                SLibUtilities.renderException(STableUtilities.class.getName(), e);
            }
        }
    }

    /**
     * Calculates Reverse Polish Notation (RPN).
     * Example:
     * (4 + 2) / 3 in RPN: 4 2 + 3 /
     *
     * @param paRpnArguments RPN arguments, operands (STableField objects) and operators (Character objects, i.e. '+', '-', '*' and '/').
     * @param poResultSet Database resultet which from operands are taken.
     */
    public static double calculateRpn(java.util.ArrayList<sa.lib.SLibRpnArgument> paRpnArguments, java.sql.ResultSet poResultSet) {
        double a = 0;
        double b = 0;
        double value = 0;
        Deque<Object> stack = new ArrayDeque<Object>();

        try {
            for (int i = 0; i < paRpnArguments.size(); i++) {
                if (paRpnArguments.get(i).getArgumentType() == SLibRpnArgumentType.OPERAND) {
                    stack.addFirst(poResultSet.getDouble((String) paRpnArguments.get(i).getArgument()));
                }
                else {
                    if (stack.size() < 2) {
                        throw new Exception(SLibConstants.MSG_ERR_RPN_ARGS_FEW);
                    }
                    else {
                        b = ((Number) stack.pollFirst()).doubleValue();
                        a = ((Number) stack.pollFirst()).doubleValue();

                        switch ((SLibRpnOperator) paRpnArguments.get(i).getArgument()) {
                            case ADDITION:
                                stack.addFirst(a + b);
                                break;
                            case SUBTRACTION:
                                stack.addFirst(a - b);
                                break;
                            case MULTIPLICATION:
                                stack.addFirst(a * b);
                                break;
                            case DIVISION:
                                stack.addFirst(b == 0d ? 0d : a / b);
                                break;
                            default:
                                throw new Exception(SLibConstants.MSG_ERR_RPN_OPER_UNDEF);
                        }
                    }
                }
            }

            if (stack.size() == 1) {
                value = ((Number) stack.pollFirst()).doubleValue();
            }
            else if (stack.size() < 1) {
                throw new Exception(SLibConstants.MSG_ERR_RPN_ARGS_FEW);
            }
            else if (stack.size() > 1) {
                throw new Exception(SLibConstants.MSG_ERR_RPN_ARGS_MANY);
            }
        }
        catch (java.sql.SQLException e) {
            SLibUtilities.renderException(STableUtilities.class.getName(), e);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(STableUtilities.class.getName(), e);
        }

        return value;
    }
}
