/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.util.Date;
import sa.lib.grid.SGridRow;

/**
 *
 * @author Edwin Carmona
 */
public class SDataUnitEquivalence extends erp.lib.data.SDataRegistry implements SGridRow {

    protected int mnPkUnitId;
    protected int mnPkUnitEquivalentId;
    protected double mdEquivalence;
    protected boolean mbDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected Date mtUserNewTs;
    protected Date mtUserEditTs;
    protected Date mtUserDeleteTs;
    
    protected SDataUnit moAuxUnit;
    protected SDataUnit moAuxUnitEquivalent;

    public SDataUnitEquivalence() {
        super(SDataConstants.ITMU_UNIT_EQUIV);
        reset();
    }

    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkUnitEquivalentId(int n) { mnPkUnitEquivalentId = n; }
    public void setEquivalence(double d) { mdEquivalence = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(Date t) { mtUserNewTs = t; }
    public void setUserEditTs(Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(Date t) { mtUserDeleteTs = t; }

    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkUnitEquivalentId() { return mnPkUnitEquivalentId; }
    public double getEquivalence() { return mdEquivalence; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public Date getUserNewTs() { return mtUserNewTs; }
    public Date getUserEditTs() { return mtUserEditTs; }
    public Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public SDataUnit getAuxUnitData() { return moAuxUnit; }
    public SDataUnit getAuxUnitEquivalent() { return moAuxUnitEquivalent; }
    
    private String getSqlWhere() {
        return "WHERE id_unit = " + mnPkUnitId + " AND id_unit_equiv = " + mnPkUnitEquivalentId + " ";
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkUnitId = ((int[]) pk)[0];
        mnPkUnitEquivalentId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkUnitId, mnPkUnitEquivalentId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkUnitId = 0;
        mnPkUnitEquivalentId = 0;
        mdEquivalence = 0;
        mbDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        moAuxUnit = null;
        moAuxUnitEquivalent = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM " + SDataConstants.TablesMap.get(mnRegistryType) + " WHERE id_unit = " + key[0] + " AND "
                    + "id_unit_equiv = " + key[1] + ";";
            resultSet = statement.getConnection().createStatement().executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkUnitId = resultSet.getInt("id_unit");
                mnPkUnitEquivalentId = resultSet.getInt("id_unit_equiv");
                mdEquivalence = resultSet.getDouble("equiv");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
                
                moAuxUnit = new SDataUnit();
                moAuxUnit.read(new int[] { mnPkUnitId }, statement);
                moAuxUnitEquivalent = new SDataUnit();
                moAuxUnitEquivalent.read(new int[] { mnPkUnitEquivalentId }, statement);
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        String sql = "";
        try {
            if (mbIsRegistryNew) {
                sql = "INSERT INTO " + SDataConstants.TablesMap.get(mnRegistryType) + " VALUES (" +
                        mnPkUnitId + ", " + 
                        mnPkUnitEquivalentId + ", " + 
                        mdEquivalence + ", " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        mnFkUserNewId + ", " + 
                        mnFkUserEditId + ", " + 
                        mnFkUserDeleteId + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + ", " + 
                        "NOW()" + " " + 
                        ")";
            }
            else {
                sql = "UPDATE " + SDataConstants.TablesMap.get(mnRegistryType) + " SET " +
//                        "id_unit = " + mnPkUnitId + ", " +
//                        "id_unit_equiv = " + mnPkUnitEquivalentId + ", " +
                        "equiv = " + mdEquivalence + ", " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
//                        "fid_usr_new = " + mnFkUserNewId + ", " +
                        "fid_usr_edit = " + mnFkUserEditId + ", " +
                        (mbDeleted ? ("fid_usr_del = " + mnFkUserDeleteId + ", ") : "") +
//                        "ts_new = " + "NOW()" + ", " +
                        "ts_edit = " + "NOW()" + (mbDeleted ? ", " : " ") +
                        (mbDeleted ? ("ts_del = " + "NOW()" + " ") : "") +
                        getSqlWhere();
            }

            connection.createStatement().execute(sql);
            
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return this.getUserEditTs();
    }
    
    @Override
    public SDataUnitEquivalence clone() throws CloneNotSupportedException {
        SDataUnitEquivalence registry = new SDataUnitEquivalence();

        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkUnitEquivalentId(this.getPkUnitEquivalentId());
        registry.setEquivalence(this.getEquivalence());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserNewId(this.getFkUserNewId());
        registry.setFkUserEditId(this.getFkUserEditId());
        registry.setFkUserDeleteId(this.getFkUserDeleteId());
        registry.setUserNewTs(this.getUserNewTs());
        registry.setUserEditTs(this.getUserEditTs());
        registry.setUserDeleteTs(this.getUserDeleteTs());

        registry.setIsRegistryNew(this.getIsRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return (int[]) getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0: value = moAuxUnit.getUnit(); break;
            case 1: value = getEquivalence(); break;
            case 2: value = moAuxUnitEquivalent.getUnit(); break;
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        
    }
}
