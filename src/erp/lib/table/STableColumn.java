/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import java.util.Vector;
import sa.lib.SLibRpnArgument;

/**
 *
 * @author Sergio Flores
 */
public class STableColumn extends STableField implements java.io.Serializable {

    //protected int mnColumnType;
    protected int mnColumnWidth;
    protected java.lang.String msColumnTitle;
    //protected java.lang.String msFieldName;
    protected javax.swing.table.TableCellRenderer miCellRenderer;
    protected boolean mbApostropheOnCsvRequired;
    protected java.util.Vector<sa.lib.SLibRpnArgument> mvRpnArguments;

    protected boolean mbSumApplying;
    private boolean mbEditable;

    /**
     * @param type Constants defined in erp.lib.SLibConstants.
     * @param name Field name in SQL query.
     * @param title Column header title in table.
     * @param width Column width in table.
     */
    public STableColumn(int type, java.lang.String name, java.lang.String title, int width) {
        super(type, name);

        mnColumnType = type;
        mnColumnWidth = width;
        msColumnTitle = title;
        msFieldName = name;
        miCellRenderer = null;
        mbApostropheOnCsvRequired = false;
        mvRpnArguments = new Vector<SLibRpnArgument>();

        mbSumApplying = false;
        mbEditable = false;
    }

    public void setColumnType(int n) { mnColumnType = n; }
    public void setColumnWidth(int n) { mnColumnWidth = n; }
    public void setColumnTitle(java.lang.String s) { msColumnTitle = s; }
    public void setFieldName(java.lang.String s) { msFieldName = s; }
    public void setCellRenderer(javax.swing.table.TableCellRenderer i) { miCellRenderer = i; }
    public void setApostropheOnCsvRequired(boolean b) { mbApostropheOnCsvRequired = b; }  // works only if column data type is string
    public void setSumApplying(boolean b) { mbSumApplying = b; }
    public void setEditable(boolean b) { mbEditable = b; }

    public int getColumnType() { return mnColumnType; }
    public int getColumnWidth() { return mnColumnWidth; }
    public java.lang.String getColumnTitle() { return msColumnTitle; }
    public java.lang.String getFieldName() { return msFieldName; }
    public javax.swing.table.TableCellRenderer getCellRenderer() { return miCellRenderer; }
    public boolean isApostropheOnCsvRequired() { return mbApostropheOnCsvRequired; }
    public boolean isSumApplying() { return mbSumApplying; }
    public boolean isEditable() { return mbEditable; }

    public java.util.Vector<sa.lib.SLibRpnArgument> getRpnArguments() { return mvRpnArguments; }
}
