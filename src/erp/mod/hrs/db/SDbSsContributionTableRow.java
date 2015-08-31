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
public class SDbSsContributionTableRow extends SDbRegistryUser implements SGridRow {

    protected int mnPkSsContributionTableId;
    protected int mnPkRowId;
    protected String msConcept;
    protected String msSubconcept;
    protected double mdWorkerPercentage;
    protected double mdCompanyPercentage;
    protected int mnLowerLimitMwzReference;
    protected int mnLimitMwzReference;

    public SDbSsContributionTableRow() {
        super(SModConsts.HRS_SSC_ROW);
    }

    public void setPkSsContributionTableId(int n) { mnPkSsContributionTableId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setConcept(String s) { msConcept = s; }
    public void setSubconcept(String s) { msSubconcept = s; }
    public void setWorkerPercentage(double d) { mdWorkerPercentage = d; }
    public void setCompanyPercentage(double d) { mdCompanyPercentage = d; }
    public void setLowerLimitMwzReference(int n) { mnLowerLimitMwzReference = n; }
    public void setLimitMwzReference(int n) { mnLimitMwzReference = n; }

    public int getPkSsContributionTableId() { return mnPkSsContributionTableId; }
    public int getPkRowId() { return mnPkRowId; }
    public String getConcept() { return msConcept; }
    public String getSubconcept() { return msSubconcept; }
    public double getWorkerPercentage() { return mdWorkerPercentage; }
    public double getCompanyPercentage() { return mdCompanyPercentage; }
    public int getLowerLimitMwzReference() { return mnLowerLimitMwzReference; }
    public int getLimitMwzReference() { return mnLimitMwzReference; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSsContributionTableId = pk[0];
        mnPkRowId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSsContributionTableId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSsContributionTableId = 0;
        mnPkRowId = 0;
        msConcept = "";
        msSubconcept = "";
        mdWorkerPercentage = 0;
        mdCompanyPercentage = 0;
        mnLowerLimitMwzReference = 0;
        mnLimitMwzReference = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ssc = " + mnPkSsContributionTableId+ " AND "
                + "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ssc = " + pk[0] + " AND "
                + "id_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_ssc = " + mnPkSsContributionTableId+ " ";
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
            mnPkSsContributionTableId = resultSet.getInt("id_ssc");
            mnPkRowId = resultSet.getInt("id_row");
            msConcept = resultSet.getString("con");
            msSubconcept = resultSet.getString("sub_con");
            mdWorkerPercentage = resultSet.getDouble("wrk_per");
            mdCompanyPercentage = resultSet.getDouble("com_per");
            mnLowerLimitMwzReference = resultSet.getInt("low_lim_mwz_ref");
            mnLimitMwzReference = resultSet.getInt("lim_mwz_ref");

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
                    mnPkSsContributionTableId + ", " +
                    mnPkRowId + ", " +
                    "'" + msConcept + "', " +
                    "'" + msSubconcept + "', " +
                    mdWorkerPercentage + ", " +
                    mdCompanyPercentage + ", " +
                    mnLowerLimitMwzReference + ", " +
                    mnLimitMwzReference + " " +
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
    public SDbSsContributionTableRow clone() throws CloneNotSupportedException {
        SDbSsContributionTableRow registry = new SDbSsContributionTableRow();

        registry.setPkSsContributionTableId(this.getPkSsContributionTableId());
        registry.setPkRowId(this.getPkRowId());
        registry.setConcept(this.getConcept());
        registry.setSubconcept(this.getSubconcept());
        registry.setWorkerPercentage(this.getWorkerPercentage());
        registry.setCompanyPercentage(this.getCompanyPercentage());
        registry.setLowerLimitMwzReference(this.getLowerLimitMwzReference());
        registry.setLimitMwzReference(this.getLimitMwzReference());

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
                value = msConcept;
                break;
            case 2:
                value = msSubconcept;
                break;
            case 3:
                value = mdWorkerPercentage;
                break;
            case 4:
                value = mdCompanyPercentage;
                break;
            case 5:
                value = mnLowerLimitMwzReference;
                break;
            case 6:
                value = mnLimitMwzReference;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 3:
                mdWorkerPercentage = (Double) value;
                break;
            case 4:
                mdCompanyPercentage = (Double) value;
                break;
            case 5:
                mnLowerLimitMwzReference = (Integer) value;
                break;
            case 6:
                mnLimitMwzReference = (Integer) value;
                break;
            default:
        }
    }
}
