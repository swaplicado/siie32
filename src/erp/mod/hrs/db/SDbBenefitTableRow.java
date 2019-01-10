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
 * @author Juan Barajas
 */
public class SDbBenefitTableRow extends SDbRegistryUser implements SGridRow {

    protected int mnPkBenefitId;
    protected int mnPkRowId;
    protected int mnMonths;
    protected int mnBenefitDays;
    protected double mdBenefitBonusPercentage;

    public SDbBenefitTableRow() {
        super(SModConsts.HRS_BEN_ROW);
    }

    public void setPkBenefitId(int n) { mnPkBenefitId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setMonths(int n) { mnMonths = n; }
    public void setBenefitDays(int n) { mnBenefitDays = n; }
    public void setBenefitBonusPercentage(double d) { mdBenefitBonusPercentage = d; }

    public int getPkBenefitId() { return mnPkBenefitId; }
    public int getPkRowId() { return mnPkRowId; }
    public int getMonths() { return mnMonths; }
    public int getBenefitDays() { return mnBenefitDays; }
    public double getBenefitBonusPercentage() { return mdBenefitBonusPercentage; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBenefitId = pk[0];
        mnPkRowId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBenefitId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBenefitId = 0;
        mnPkRowId = 0;
        mnMonths = 0;
        mnBenefitDays = 0;
        mdBenefitBonusPercentage = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ben = " + mnPkBenefitId + " AND "
                + "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ben = " + pk[0] + " AND "
                + "id_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_ben = " + mnPkBenefitId + " ";
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
            mnPkBenefitId = resultSet.getInt("id_ben");
            mnPkRowId = resultSet.getInt("id_row");
            mnMonths = resultSet.getInt("mon");
            mnBenefitDays = resultSet.getInt("ben_day");
            mdBenefitBonusPercentage = resultSet.getDouble("ben_bon_per");

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
                    mnPkBenefitId + ", " + 
                    mnPkRowId + ", " + 
                    mnMonths + ", " + 
                    mnBenefitDays + ", " + 
                    mdBenefitBonusPercentage + " " + 
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
    public SDbBenefitTableRow clone() throws CloneNotSupportedException {
        SDbBenefitTableRow registry = new SDbBenefitTableRow();

        registry.setPkBenefitId(this.getPkBenefitId());
        registry.setPkRowId(this.getPkRowId());
        registry.setMonths(this.getMonths());
        registry.setBenefitDays(this.getBenefitDays());
        registry.setBenefitBonusPercentage(this.getBenefitBonusPercentage());

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
                value = mnMonths;
                break;
            case 2:
                value = mnMonths / 12;
                break;
            case 3:
                value = mnBenefitDays;
                break;
            case 4:
                value = mdBenefitBonusPercentage;
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
