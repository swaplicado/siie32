/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import sa.lib.grid.SGridConsts;

/**
 *
 * @author Sergio Flores
 */
public class STableField extends sa.lib.grid.SGridColumn implements java.io.Serializable {

    //protected int mnColumnType;
    //protected java.lang.String msFieldName;

    /**
     * @param columnType Constants defined in erp.lib.SLibConstants.
     * @param fieldName Field name in SQL query.
     */
    public STableField(int columnType, java.lang.String fieldName) {
        super(SGridConsts.COL_TYPE_INT_1B, fieldName, "", 100, null);   // COL_TYPE_INT_1B is a dummy value, due super class validities in constructor
        
        mnColumnType = columnType;
        msFieldName = fieldName;
    }

    public void setColumnType(int n) { mnColumnType = n; }
    public void setFieldName(java.lang.String s) { msFieldName = s; }

    public int getColumnType() { return mnColumnType; }
    public java.lang.String getFieldName() { return msFieldName; }
}
