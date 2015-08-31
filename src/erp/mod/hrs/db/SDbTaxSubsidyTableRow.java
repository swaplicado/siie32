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
public class SDbTaxSubsidyTableRow extends SDbRegistryUser implements SGridRow {

    protected int mnPkTaxSubsidyTableId;
    protected int mnPkRowId;
    protected double mdLowerLimit;
    protected double mdTaxSubsidy;

    public SDbTaxSubsidyTableRow() {
        super(SModConsts.HRS_TAX_SUB_ROW);
    }

    public void setPkTaxSubsidyTableId(int n) { mnPkTaxSubsidyTableId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setLowerLimit(double d) { mdLowerLimit = d; }
    public void setTaxSubsidy(double d) { mdTaxSubsidy = d; }

    public int getPkTaxSubsidyTableId() { return mnPkTaxSubsidyTableId; }
    public int getPkRowId() { return mnPkRowId; }
    public double getLowerLimit() { return mdLowerLimit; }
    public double getTaxSubsidy() { return mdTaxSubsidy; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTaxSubsidyTableId = pk[0];
        mnPkRowId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTaxSubsidyTableId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTaxSubsidyTableId = 0;
        mnPkRowId = 0;
        mdLowerLimit = 0;
        mdTaxSubsidy = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tax_sub = " + mnPkTaxSubsidyTableId + " AND "
                + "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tax_sub = " + pk[0] + " AND "
                + "id_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_tax_sub = " + mnPkTaxSubsidyTableId + " ";
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
            mnPkTaxSubsidyTableId = resultSet.getInt("id_tax_sub");
            mnPkRowId = resultSet.getInt("id_row");
            mdLowerLimit = resultSet.getDouble("low_lim");
            mdTaxSubsidy = resultSet.getDouble("tax_sub");

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
                    mnPkTaxSubsidyTableId + ", " +
                    mnPkRowId + ", " +
                    mdLowerLimit + ", " +
                    mdTaxSubsidy + " " +
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
    public SDbTaxSubsidyTableRow clone() throws CloneNotSupportedException {
        SDbTaxSubsidyTableRow registry = new SDbTaxSubsidyTableRow();

        registry.setPkTaxSubsidyTableId(this.getPkTaxSubsidyTableId());
        registry.setPkRowId(this.getPkRowId());
        registry.setLowerLimit(this.getLowerLimit());
        registry.setTaxSubsidy(this.getTaxSubsidy());

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
                value = mdTaxSubsidy;
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
