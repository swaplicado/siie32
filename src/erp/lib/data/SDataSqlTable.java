/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.data;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDataSqlTable {

    private erp.lib.data.SDataDatabase moDatabase;
    private java.lang.String msTableName;
    private java.lang.String msTableAlias;
    
    public SDataSqlTable(erp.lib.data.SDataDatabase database, java.lang.String name) {
        moDatabase = database;
        msTableName = name;
        msTableAlias = "";
    }
    
    public SDataSqlTable(erp.lib.data.SDataDatabase database, java.lang.String name, java.lang.String alias) {
        moDatabase = database;
        msTableName = name;
        msTableAlias = alias;
    }
    
    public void setDatabase(erp.lib.data.SDataDatabase o) { moDatabase = o; }
    public void setTableName(java.lang.String s) { msTableName = s; }
    public void setTableAlias(java.lang.String s) { msTableAlias = s; }
    
    public erp.lib.data.SDataDatabase getDatabase() { return moDatabase; }
    public java.lang.String getTableName() { return msTableName; }
    public java.lang.String getTableAlias() { return msTableAlias; }
}
