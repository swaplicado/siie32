/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.data;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDataSqlField {
    
    private int mnFieldType;
    private java.lang.String msFieldName;
    private java.lang.Object moValue;
    private boolean mbIsEditable;
    
    public SDataSqlField(int fieldType, java.lang.String fieldName, boolean isEditable) {
        mnFieldType = fieldType;
        msFieldName = fieldName;
        moValue = null;
        mbIsEditable = isEditable;
    }
    
    public void setFieldType(int n) { mnFieldType = n; }
    public void setFieldName(java.lang.String s) { msFieldName = s; }
    public void setValue(java.lang.Object o) { moValue = o; }
    public void setIsEditable(boolean b) { mbIsEditable = b; }
    
    public int getFieldType() { return mnFieldType; }
    public java.lang.String getFieldName() { return msFieldName; }
    public java.lang.Object getValue() { return moValue; }
    public boolean getIsEditable() { return mbIsEditable; }
    
    public void setValueBoolean(boolean b) { moValue = new java.lang.Boolean(b); }
    public void setValueInteger(int n) { moValue = new java.lang.Integer(n); }
    public void setValueLong(long l) { moValue = new java.lang.Long(l); }
    public void setValueFloat(float f) { moValue = new java.lang.Float(f); }
    public void setValueDouble(double d) { moValue = new java.lang.Double(d); }
    public void setValueString(java.lang.String s) { moValue = new java.lang.String(s); }
    public void setValueDate(java.util.Date t) { moValue = new java.util.Date(t.getTime()); }
    
    public boolean getValueBoolean() { return ((java.lang.Boolean) moValue).booleanValue(); }
    public int getValueInteger() { return ((java.lang.Integer) moValue).intValue(); }
    public long getValueLong() { return ((java.lang.Long) moValue).longValue(); }
    public float getValueFloat() { return ((java.lang.Float) moValue).floatValue(); }
    public double getValueDouble() { return ((java.lang.Double) moValue).doubleValue(); }
    public java.lang.String getValueString() { return (java.lang.String) moValue; }
    public java.util.Date getValueDate() { return (java.util.Date) moValue; }
}
