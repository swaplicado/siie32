/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbTaxSubsidyTable extends SDbRegistryUser {

    protected int mnPkTaxSubsidyTableId;
    protected Date mtDateStart;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected ArrayList<SDbTaxSubsidyTableRow> maChildRows;

    public SDbTaxSubsidyTable() {
        super(SModConsts.HRS_TAX_SUB);
    }

    public void setPkTaxSubsidyTableId(int n) { mnPkTaxSubsidyTableId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkTaxSubsidyTableId() { return mnPkTaxSubsidyTableId; }
    public Date getDateStart() { return mtDateStart; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbTaxSubsidyTableRow> getChildRows() { return maChildRows; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTaxSubsidyTableId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTaxSubsidyTableId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTaxSubsidyTableId = 0;
        mtDateStart = null;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        maChildRows = new ArrayList<SDbTaxSubsidyTableRow>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tax_sub = " + mnPkTaxSubsidyTableId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tax_sub = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTaxSubsidyTableId = 0;

        msSql = "SELECT COALESCE(MAX(id_tax_sub), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTaxSubsidyTableId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mtDateStart = resultSet.getDate("dt_sta");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            maChildRows.clear();

            msSql = "SELECT id_row FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX_SUB_ROW) + " "
                    + "WHERE id_tax_sub = " + mnPkTaxSubsidyTableId + " "
                    + "ORDER BY id_row ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                maChildRows.add((SDbTaxSubsidyTableRow) session.readRegistry(SModConsts.HRS_TAX_SUB_ROW, new int[] { mnPkTaxSubsidyTableId, resultSet.getInt(1) }));
            }

            // Finish registry reading:

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
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_tax_sub = " + mnPkTaxSubsidyTableId + ", " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_TAX_SUB_ROW) + " "
                + "WHERE id_tax_sub = " + mnPkTaxSubsidyTableId + " ";

        session.getStatement().execute(msSql);

        for (SDbTaxSubsidyTableRow child : maChildRows) {
            child.setPkTaxSubsidyTableId(mnPkTaxSubsidyTableId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry saving:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbTaxSubsidyTable clone() throws CloneNotSupportedException {
        SDbTaxSubsidyTable registry = new SDbTaxSubsidyTable();

        registry.setPkTaxSubsidyTableId(this.getPkTaxSubsidyTableId());
        registry.setDateStart(this.getDateStart());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
