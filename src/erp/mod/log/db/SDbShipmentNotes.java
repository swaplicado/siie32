/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbShipmentNotes extends SDbRegistry {

    protected int mnPkShipmentId;
    protected int mnPkNotesId;
    protected String msNotes;
    protected boolean mbPrintable;

    public SDbShipmentNotes() {
        super(SModConsts.LOG_SHIP_NTS);
    }

    public void setPkShipmentId(int n) { mnPkShipmentId = n; }
    public void setPkNotesId(int n) { mnPkNotesId = n; }
    public void setNotes(String s) { msNotes = s; }
    public void setPrintable(boolean b) { mbPrintable = b; }

    public int getPkShipmentId() { return mnPkShipmentId; }
    public int getPkNotesId() { return mnPkNotesId; }
    public String getNotes() { return msNotes; }
    public boolean isPrintable() { return mbPrintable; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkShipmentId = pk[0];
        mnPkNotesId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkShipmentId, mnPkNotesId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkShipmentId = 0;
        mnPkNotesId = 0;
        msNotes = "";
        mbPrintable = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ship = " + mnPkShipmentId + " AND id_nts = " + mnPkNotesId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ship = " + pk[0] + " AND id_nts = " + pk[1];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNotesId = 0;

        msSql = "SELECT COALESCE(MAX(id_nts), 0) + 1 FROM " + getSqlTable() + " WHERE id_ship = " + mnPkShipmentId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkNotesId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        statement = session.getDatabase().getConnection().createStatement();
        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkShipmentId = resultSet.getInt("id_ship");
            mnPkNotesId = resultSet.getInt("id_nts");
            msNotes = resultSet.getString("nts");
            mbPrintable = resultSet.getBoolean("b_prt");

            mbRegistryNew = false;
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
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                mnPkShipmentId + ", " +
                mnPkNotesId + ", " +
                "'" + msNotes + "', " +
                (mbPrintable ? 1 : 0) + " " +
                ")";
        }
        else {

            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_ship = " + mnPkShipmentId + ", " +
                "id_nts = " + mnPkNotesId + ", " +
                "nts = '" + msNotes + "', " +
                "b_prt = " + (mbPrintable ? 1 : 0) + " " +
                getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbShipmentNotes clone() throws CloneNotSupportedException {
        SDbShipmentNotes registry = new SDbShipmentNotes();

        registry.setPkShipmentId(this.getPkShipmentId());
        registry.setPkNotesId(this.getPkNotesId());
        registry.setNotes(this.getNotes());
        registry.setPrintable(this.isPrintable());

        return registry;
    }
}
