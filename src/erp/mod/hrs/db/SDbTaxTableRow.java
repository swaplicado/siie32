/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbTaxTableRow extends SDbRegistryUser implements SGridRow {

    protected int mnPkTaxTableId;
    protected int mnPkRowId;
    protected double mdLowerLimit;
    protected double mdFixedFee;
    protected double mdTaxRate;

    public SDbTaxTableRow() {
        super(SModConsts.HRS_TAX_ROW);
    }

    public void setPkTaxTableId(int n) { mnPkTaxTableId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setLowerLimit(double d) { mdLowerLimit = d; }
    public void setFixedFee(double d) { mdFixedFee = d; }
    public void setTaxRate(double d) { mdTaxRate = d; }

    public int getPkTaxTableId() { return mnPkTaxTableId; }
    public int getPkRowId() { return mnPkRowId; }
    public double getLowerLimit() { return mdLowerLimit; }
    public double getFixedFee() { return mdFixedFee; }
    public double getTaxRate() { return mdTaxRate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTaxTableId = pk[0];
        mnPkRowId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTaxTableId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTaxTableId = 0;
        mnPkRowId = 0;
        mdLowerLimit = 0;
        mdFixedFee = 0;
        mdTaxRate = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tax = " + mnPkTaxTableId + " AND "
                + "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tax = " + pk[0] + " AND "
                + "id_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_tax = " + mnPkTaxTableId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRowId = resultSet.getInt(1);
        }
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
            mnPkTaxTableId = resultSet.getInt("id_tax");
            mnPkRowId = resultSet.getInt("id_row");
            mdLowerLimit = resultSet.getDouble("low_lim");
            mdFixedFee = resultSet.getDouble("fix_fee");
            mdTaxRate = resultSet.getDouble("tax_rate");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkTaxTableId + ", " +
                    mnPkRowId + ", " +
                    mdLowerLimit + ", " +
                    mdFixedFee + ", " +
                    mdTaxRate + " " +
                    ")";
        }
        else {
            throw new Exception("Not supported.");
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbTaxTableRow clone() throws CloneNotSupportedException {
        SDbTaxTableRow registry = new SDbTaxTableRow();

        registry.setPkTaxTableId(this.getPkTaxTableId());
        registry.setPkRowId(this.getPkRowId());
        registry.setLowerLimit(this.getLowerLimit());
        registry.setFixedFee(this.getFixedFee());
        registry.setTaxRate(this.getTaxRate());

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
        return isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return isDeletable();
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = mnPkRowId;
                break;
            case 1:
                value = mdLowerLimit;
                break;
            case 2:
                value = mdFixedFee;
                break;
            case 3:
                value = mdTaxRate;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
