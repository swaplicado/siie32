/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SRowExchangeRate extends SDbRegistry implements SGridRow {

    protected int mnPkCurrencyId;
    protected int mnPkDateId;
    protected String msCurrencyCode;
    protected Date mtDate;
    protected double mdExchangeRate;

    protected ArrayList<SRowExchangeRate> maRowsExchangeRates;

    public SRowExchangeRate() {
        super(SModConsts.FIN_EXC_RATE);
    }

    public void setPkCurrencyId(int n) { mnPkCurrencyId = n; }
    public void setPkDateId(int n) { mnPkDateId = n; }
    public void setCurrencyCode(String s) { msCurrencyCode = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }

    public int getPkCurrencyId() { return mnPkCurrencyId; }
    public int getPkDateId() { return mnPkDateId; }
    public String getDocType() { return msCurrencyCode; }
    public Date getDate() { return mtDate; }
    public double getExchangeRate() { return mdExchangeRate; }

    public ArrayList<SRowExchangeRate> getRowsExchangeRates() { return maRowsExchangeRates; }

    public void getRows(SGuiSession session, int nParamPkCurrencyId) throws SQLException {
        SRowExchangeRate row = null;

        ResultSet resultSet = null;
        Statement statement = null;

        msSql = "SELECT e.id_cur, e.id_dt, e.exc_rate, c.cur_key " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_EXC_RATE) + " AS e " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_CUR) + " AS c ON e.id_cur = c.id_cur " +
            "WHERE e.b_del = 0 AND e.id_dt BETWEEN ADDDATE('" + SLibUtils.DbmsDateFormatDate.format(session.getCurrentDate()) +
                "', -31) AND ADDDATE('" + SLibUtils.DbmsDateFormatDate.format(session.getCurrentDate()) + "', 31) " +
            (nParamPkCurrencyId == 0 ? "" : "AND e.id_cur = " + nParamPkCurrencyId + " ") +
            "ORDER BY c.cur_key, e.id_dt DESC ";

        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {

            row = new SRowExchangeRate();

            row.setPkCurrencyId(resultSet.getInt("e.id_cur"));
            row.setPkDateId(resultSet.getInt("e.id_dt"));
            row.setCurrencyCode(resultSet.getString("c.cur_key"));
            row.setDate(resultSet.getDate("e.id_dt"));
            row.setExchangeRate(resultSet.getDouble("e.exc_rate"));

            maRowsExchangeRates.add(row);
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCurrencyId = pk[0];
        //mnPkDateId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCurrencyId /*, mnPkDateId */ };
    }

    @Override
    public void initRegistry() {
        mnPkCurrencyId = 0;
        mnPkDateId = 0;
        msCurrencyCode = "";
        mtDate = null;
        mdExchangeRate = 0;

        maRowsExchangeRates = new ArrayList<SRowExchangeRate>();
    }

    @Override
    public String getSqlTable() {
        return "";
    }

    @Override
    public String getSqlWhere() {
        return "";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {

    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {

    }

    @Override
    public SRowExchangeRate clone() throws CloneNotSupportedException {
        SRowExchangeRate registry = new SRowExchangeRate();

        registry.setPkCurrencyId(this.getPkCurrencyId());
        registry.setPkDateId(this.getPkDateId());
        registry.setCurrencyCode(this.getDocType());
        registry.setDate(this.getDate());
        registry.setExchangeRate(this.getExchangeRate());

        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
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
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {

    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msCurrencyCode;
                break;
            case 1:
                value = mtDate;
                break;
            case 2:
                value = mdExchangeRate;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
            default:
        }
    }
}
