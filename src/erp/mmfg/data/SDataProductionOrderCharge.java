/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderCharge extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkOrderId;
    protected int mnPkChargeId;
    protected java.lang.String msNumber;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected double mdDbmsQuantity;
    protected double mdDbmsCharges;
    protected int mnDbmsFkCobId;
    protected java.lang.String msDbmsProductionOrder;
    protected java.lang.String msDbmsFinishedGood;
    protected java.lang.String msDbmsFormula;
    protected java.lang.String msDbmsType;
    protected java.lang.String msDbmsUnit;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    protected java.util.Vector<erp.mmfg.data.SDataProductionOrderChargeEntry> mvDbmsProductionOrderChargeEntries;

    public SDataProductionOrderCharge() {
        super(SDataConstants.MFG_ORD_CHG);
        mvDbmsProductionOrderChargeEntries = new Vector<SDataProductionOrderChargeEntry>();

        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkOrderId(int n) { mnPkOrderId = n; }
    public void setPkChargeId(int n) { mnPkChargeId = n; }
    public void setNumber(java.lang.String s) { msNumber = s;  }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkOrderId() { return mnPkOrderId; }
    public int getPkChargeId() { return mnPkChargeId; }
    public java.lang.String getNumber() { return msNumber; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsFkCobId(int n) { mnDbmsFkCobId = n; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsProductionOrder() { return msDbmsProductionOrder; }
    public java.lang.String getDbmsFinishedGood() { return msDbmsFinishedGood; }
    public java.lang.String getDbmsBom() { return msDbmsFormula; }
    public java.lang.String getDbmsProductionOrderType() { return msDbmsType; }
    public java.lang.String getDbmsUnitSymbol() { return msDbmsUnit; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    public double getDbmsQuantiy() { return mdDbmsQuantity; }
    public double getDbmsCharges() { return mdDbmsCharges; }

    public java.util.Vector<erp.mmfg.data.SDataProductionOrderChargeEntry> getDbmsProductionOrderChargeEntries() { return mvDbmsProductionOrderChargeEntries; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkOrderId = ((int[]) pk)[1];
        mnPkChargeId = ((int[]) pk)[2];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkOrderId, mnPkChargeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkOrderId = 0;
        mnPkChargeId = 0;
        msNumber = "";
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mdDbmsQuantity = 0;
        mdDbmsCharges = 0;
        mnDbmsFkCobId = 0;
        msDbmsProductionOrder = "";
        msDbmsFinishedGood = "";
        msDbmsFormula = "";
        msDbmsType = "";
        msDbmsUnit = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
        
        mvDbmsProductionOrderChargeEntries.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;

        ResultSet resultSet = null;
        Statement statementAux = null;

        SDataProductionOrderChargeEntry oProductionOrderChargeEntry = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT c.*, CONCAT(o.id_year, '-',erp.lib_fix_int(o.id_ord,6), ' ', o.ref) AS f_ref, o.ref, o.qty, o.chgs, CONCAT(i.item_key, ' - ', i.item) AS f_item, u.symbol, b.bom, t.tp, un.usr, ue.usr, ud.usr " +
                    "FROM mfg_ord_chg AS c " +
                    "INNER JOIN mfg_ord AS o ON c.id_year = o.id_year AND c.id_ord = o.id_ord " +
                    "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                    "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
                    "INNER JOIN mfg_bom AS b ON o.fid_bom = b.id_bom " +
                    "INNER JOIN erp.mfgu_tp_ord AS t ON o.fid_tp_ord = t.id_tp " +
                    "INNER JOIN erp.usru_usr AS un ON c.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON c.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON c.fid_usr_del = ud.id_usr " +
                    "WHERE c.id_year = " + key[0] + " AND c.id_ord = " + key[1] + " AND c.id_chg = " + key[2] + " " +
                    "GROUP BY c.id_year, c.id_ord, c.id_chg ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("c.id_year");
                mnPkOrderId = resultSet.getInt("c.id_ord");
                mnPkChargeId = resultSet.getInt("c.id_chg");
                msNumber = resultSet.getString("c.num");
                mbIsDeleted = resultSet.getBoolean("c.b_del");
                mnFkUserNewId = resultSet.getInt("c.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("c.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("c.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("c.ts_new");
                mtUserEditTs = resultSet.getTimestamp("c.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("c.ts_del");

                msDbmsProductionOrder = resultSet.getString("f_ref");
                msDbmsFinishedGood = resultSet.getString("f_item");
                msDbmsFormula = resultSet.getString("b.bom");
                msDbmsUnit = resultSet.getString("u.symbol");
                msDbmsType = resultSet.getString("t.tp");
                mdDbmsQuantity = resultSet.getDouble("o.qty");
                mdDbmsCharges = resultSet.getDouble("o.chgs");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                // Read entries:

                mvDbmsProductionOrderChargeEntries.removeAllElements();
                statementAux = statement.getConnection().createStatement();
                sql = "SELECT * FROM mfg_ord_chg_ety " +
                        "WHERE id_year = " + key[0] + " AND id_ord = " + key[1] + " AND id_chg = " + key[2] + " " +
                        "ORDER BY id_year, id_ord, id_chg, id_ety ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oProductionOrderChargeEntry = new SDataProductionOrderChargeEntry();                    
                    oProductionOrderChargeEntry.setDbmsFkCobId(mnDbmsFkCobId);
                    if (oProductionOrderChargeEntry.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_ord"), resultSet.getInt("id_chg"), resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {                        
                        mvDbmsProductionOrderChargeEntries.add(oProductionOrderChargeEntry);
                    }
                }
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
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
        int nParam = 1;
        CallableStatement callableStatement = null;

        SDataProductionOrderChargeEntry oProductionOrderChargeEntry = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_ord_chg_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkOrderId);
            callableStatement.setInt(nParam++, mnPkChargeId);
            callableStatement.setString(nParam++, msNumber);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkChargeId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            // Save entries:
            
            for (int i=0; i<mvDbmsProductionOrderChargeEntries.size(); i++) {
                oProductionOrderChargeEntry = mvDbmsProductionOrderChargeEntries.get(i);

                oProductionOrderChargeEntry.setPkYearId(mnPkYearId);
                oProductionOrderChargeEntry.setPkOrderId(mnPkOrderId);
                oProductionOrderChargeEntry.setPkChargeId(mnPkChargeId);
                //oProductionOrderChargeEntry.setPkEntryId(0);
                if (oProductionOrderChargeEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
            }

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
    public Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
