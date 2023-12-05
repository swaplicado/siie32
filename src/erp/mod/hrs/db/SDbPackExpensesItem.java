/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SDataParamsErp;
import erp.mod.SModConsts;
import erp.mod.itm.db.SDbItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbPackExpensesItem extends SDbRegistryUser implements SGridRow {

    protected int mnPkPackExpensesId;
    protected int mnPkExpenseTypeId;
    protected int mnFkItemId;
    
    protected String msDbmsExpenseTypeCode;
    protected String msDbmsExpenseTypeName;
    protected String msDbmsItemCode;
    protected String msDbmsItemName;

    public SDbPackExpensesItem() {
        super(SModConsts.HRSU_PACK_EXP_ITEM);
    }

    public void setPkPackExpensesId(int n) { mnPkPackExpensesId = n; }
    public void setPkExpenseTypeId(int n) { mnPkExpenseTypeId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }

    public int getPkPackExpensesId() { return mnPkPackExpensesId; }
    public int getPkExpenseTypeId() { return mnPkExpenseTypeId; }
    public int getFkItemId() { return mnFkItemId; }

    public void setDbmsExpenseTypeCode(String s) { msDbmsExpenseTypeCode = s; }
    public void setDbmsExpenseTypeName(String s) { msDbmsExpenseTypeName = s; }
    public void setDbmsItemCode(String s) { msDbmsItemCode = s; }
    public void setDbmsItemName(String s) { msDbmsItemName = s; }

    public String getDbmsExpenseTypeCode() { return msDbmsExpenseTypeCode; }
    public String getDbmsExpenseTypeName() { return msDbmsExpenseTypeName; }
    public String getDbmsItemCode() { return msDbmsItemCode; }
    public String getDbmsItemName() { return msDbmsItemName; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPackExpensesId = pk[0];
        mnPkExpenseTypeId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPackExpensesId, mnPkExpenseTypeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPackExpensesId = 0;
        mnPkExpenseTypeId = 0;
        mnFkItemId = 0;
        
        msDbmsExpenseTypeCode = "";
        msDbmsExpenseTypeName = "";
        msDbmsItemCode = "";
        msDbmsItemName = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pack_exp = " + mnPkPackExpensesId + " "
                + "AND id_tp_exp = " + mnPkExpenseTypeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pack_exp = " + pk[0] + " "
                + "AND id_tp_exp = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkPackExpensesId = resultSet.getInt("id_pack_exp");
            mnPkExpenseTypeId = resultSet.getInt("id_tp_exp");
            mnFkItemId = resultSet.getInt("fk_item");

            mbRegistryNew = false;
            
            // read DBMS data:
            
            SDbExpenseType expenseType = (SDbExpenseType) session.readRegistry(SModConsts.HRSU_TP_EXP, new int[] { mnPkExpenseTypeId });
            
            msDbmsExpenseTypeCode = expenseType.getCode();
            msDbmsExpenseTypeName = expenseType.getName();
            
            SDbItem item = new SDbItem();
            
            msDbmsItemCode = (String) item.readField(session.getStatement(), new int[] { mnFkItemId }, SDbRegistry.FIELD_CODE);
            String itemName = (String) item.readField(session.getStatement(), new int[] { mnFkItemId }, SDbRegistry.FIELD_NAME);
            
            if (!((SDataParamsErp) session.getConfigSystem()).getIsItemKeyApplying()) {
                msDbmsItemName = itemName;
            }
            else {
                if (((SDataParamsErp) session.getConfigSystem()).getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    msDbmsItemName = msDbmsItemCode + " - " + itemName;
                }
                else {
                    msDbmsItemName = itemName + " - " + msDbmsItemCode;
                }
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPackExpensesId + ", " + 
                    mnPkExpenseTypeId + ", " + 
                    mnFkItemId + " " + 
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_pack_exp = " + mnPkPackExpensesId + ", " +
                    //"id_tp_exp = " + mnPkExpenseTypeId + ", " +
                    "fk_item = " + mnFkItemId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbPackExpensesItem clone() throws CloneNotSupportedException {
        SDbPackExpensesItem registry = new SDbPackExpensesItem();

        registry.setPkPackExpensesId(this.getPkPackExpensesId());
        registry.setPkExpenseTypeId(this.getPkExpenseTypeId());
        registry.setFkItemId(this.getFkItemId());

        registry.setDbmsExpenseTypeCode(this.getDbmsExpenseTypeCode());
        registry.setDbmsExpenseTypeName(this.getDbmsExpenseTypeName());
        registry.setDbmsItemCode(this.getDbmsItemCode());
        registry.setDbmsItemName(this.getDbmsItemName());
    
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
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
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = msDbmsExpenseTypeName;
                break;
                
            case 1:
                value = msDbmsItemName;
                break;
                
            case 2:
                value = msDbmsItemCode;
                break;
                
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        
    }
}
