/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.util.Vector;
import java.util.Date;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mtrn.data.STrnStockMove;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataProductionOrderChargeEntry extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkOrderId;
    protected int mnPkChargeId;
    protected int mnPkEntryId;
    protected double mdGrossRequirement_r;
    protected boolean mbIsRequest;
    protected boolean mbIsDeleted;
    protected int mnFkItemId_r;
    protected int mnFkUnitId_r;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected boolean mbDbmsItemSubstitute;
    protected double mdDbmsQuantity;
    protected double mdDbmsCharges;
    protected double mdDbmsGrossRequirement_r;
    protected int mnDbmsFkCobId;
    protected java.lang.String msDbmsLevelItem;
    protected java.lang.String msDbmsLevelItemKey;
    protected java.lang.String msDbmsLevelUnitSymbol;
    protected java.lang.String msDbmsProductionOrder;
    protected java.lang.String msDbmsFinishedGood;
    protected java.lang.String msDbmsFinishedGoodKey;
    protected java.lang.String msDbmsBom;
    protected java.lang.String msDbmsProductionOrderType;
    protected java.lang.String msDbmsUnitSymbol;
    protected java.lang.String msDbmsLots;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;

    protected java.util.Vector<erp.mmfg.data.SDataProductionOrderChargeEntryLot> mvDbmsProductionOrderChargeEntryLots;
    protected java.util.Vector<erp.mtrn.data.STrnStockMove> mvAuxStockMoves;

    public SDataProductionOrderChargeEntry() {
        super(SDataConstants.MFG_ORD_CHG_ETY);
        mvDbmsProductionOrderChargeEntryLots = new Vector<SDataProductionOrderChargeEntryLot>();
        mvAuxStockMoves = new Vector<STrnStockMove>();

        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkOrderId(int n) { mnPkOrderId = n; }
    public void setPkChargeId(int n) { mnPkChargeId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setGrossRequirement_r(double d) { mdGrossRequirement_r = d; }
    public void setIsRequest(boolean b) { mbIsRequest = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemId_r(int n) { mnFkItemId_r = n; }
    public void setFkUnitId_r(int n) { mnFkUnitId_r = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkOrderId() { return mnPkOrderId; }
    public int getPkChargeId() { return mnPkChargeId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getGrossRequirement_r() { return mdGrossRequirement_r; }
    public boolean getIsRequest() { return mbIsRequest; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemId_r() { return mnFkItemId_r; }
    public int getFkUnitId_r() { return mnFkUnitId_r; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsLevelItem(java.lang.String s) { msDbmsLevelItem = s; }
    public void setDbmsLevelItemKey(java.lang.String s) { msDbmsLevelItemKey = s; }
    public void setDbmsLevelUnitSymbol(java.lang.String s) { msDbmsLevelUnitSymbol = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }
    public void setDbmsFkCobId(int n) { mnDbmsFkCobId = n; }
    public void setDbmsLots(java.lang.String s) { msDbmsLots = s; }

    public boolean getDbmsItemSubstitute() { return mbDbmsItemSubstitute; }
    public double getDbmsQuantiy() { return mdDbmsQuantity; }
    public double getDbmsCharges() { return mdDbmsCharges; }
    public double getDbmsGrossRequirement_r() { return mdDbmsGrossRequirement_r; }
    public int getDbmsFkCobId() { return mnDbmsFkCobId; }
    public java.lang.String getDbmsLevelItem() { return msDbmsLevelItem; }
    public java.lang.String getDbmsLevelItemKey() { return msDbmsLevelItemKey; }
    public java.lang.String getDbmsLevelUnitSymbol() { return msDbmsLevelUnitSymbol; }
    public java.lang.String getDbmsProductionOrder() { return msDbmsProductionOrder; }
    public java.lang.String getDbmsFinishedGood() { return msDbmsFinishedGood; }
    public java.lang.String getDbmsFinishedGoodKey() { return msDbmsFinishedGoodKey; }
    public java.lang.String getDbmsBom() { return msDbmsBom; }
    public java.lang.String getDbmsProductionOrderType() { return msDbmsProductionOrderType; }
    public java.lang.String getDbmsUnitSymbol() { return msDbmsUnitSymbol; }
    public java.lang.String getDbmsLots() { return msDbmsLots; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    public java.util.Vector<erp.mmfg.data.SDataProductionOrderChargeEntryLot> getDbmsProductionOrderChargeEntryLots() { return mvDbmsProductionOrderChargeEntryLots; }
    public java.util.Vector<erp.mtrn.data.STrnStockMove> getAuxStockMoves() { return mvAuxStockMoves; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkOrderId = ((int[]) pk)[1];
        mnPkChargeId = ((int[]) pk)[2];
        mnPkEntryId = ((int[]) pk)[3];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkOrderId, mnPkChargeId, mnPkEntryId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkOrderId = 0;
        mnPkChargeId = 0;
        mnPkEntryId = 0;
        mdGrossRequirement_r = 0;
        mbIsRequest = false;
        mbIsDeleted = false;
        mnFkItemId_r = 0;
        mnFkUnitId_r = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mbDbmsItemSubstitute = false;
        mdDbmsQuantity = 0;
        mdDbmsCharges = 0;
        mdDbmsGrossRequirement_r = 0;
        mnDbmsFkCobId = 0;
        msDbmsLevelItem = "";
        msDbmsLevelItemKey = "";
        msDbmsLevelUnitSymbol = "";
        msDbmsProductionOrder = "";
        msDbmsFinishedGood = "";
        msDbmsFinishedGoodKey = "";
        msDbmsBom = "";
        msDbmsProductionOrderType = "";
        msDbmsUnitSymbol = "";
        msDbmsLots = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";

        mvDbmsProductionOrderChargeEntryLots.removeAllElements();
        mvAuxStockMoves.removeAllElements();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;

        ResultSet resultSet = null;
        Statement statementAux = null;

        SDataProductionOrderChargeEntryLot oProductionOrderChargeEntryLot = null;
        STrnStockMove move = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT ce.*, CONCAT(o.id_year, '-',erp.lib_fix_int(o.id_ord,6), ', ', o.ref) AS f_ref, o.ref, o.qty, o.chgs, i.item_key, i.item, u.symbol, b.bom, t.tp, " +
                "ci.item_key, ci.item, cu.symbol, un.usr, ue.usr, ud.usr, IF(MAX(bs.id_bom)>0, TRUE, FALSE) AS f_substitute " +
                "FROM mfg_ord AS o " +
                "INNER JOIN mfg_ord_chg AS c ON o.id_year = c.id_year AND o.id_ord = c.id_ord AND c.b_del = 0 " +
                "INNER JOIN mfg_ord_chg_ety AS ce ON c.id_year = ce.id_year AND c.id_ord = ce.id_ord AND c.id_chg = ce.id_chg AND ce.b_del = 0 " +
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
                "INNER JOIN mfg_bom AS b ON o.fid_bom = b.id_bom " +
                "INNER JOIN erp.mfgu_tp_ord AS t ON o.fid_tp_ord = t.id_tp " +
                "INNER JOIN erp.itmu_item AS ci ON ce.fid_item_r = ci.id_item " +
                "INNER JOIN erp.itmu_unit AS cu ON ce.fid_unit_r = cu.id_unit " +
                "INNER JOIN erp.usru_usr AS un ON ce.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON ce.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON ce.fid_usr_del = ud.id_usr " +
                "INNER JOIN mfg_bom AS bom ON o.fid_item_r = bom.fid_item_n " + // XXX "AND o.fid_unit_r = bom.fid_unit_n " +
                "LEFT OUTER JOIN mfg_bom_sub AS bs ON bom.id_bom = bs.id_bom AND ce.fid_item_r = bs.id_item AND ce.fid_unit_r = bs.id_unit AND bs.b_del = 0 " +
                "WHERE ce.id_year = " + key[0] + " AND ce.id_ord = " + key[1] + " AND ce.id_chg = " + key[2] + " AND ce.id_ety = " + key[3] + " " +
                "GROUP BY ce.id_year, ce.id_ord, ce.id_chg, ce.id_ety " +
                "ORDER BY ci.item_key, ci.item; ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {                
                mnPkYearId = resultSet.getInt("ce.id_year");
                mnPkOrderId = resultSet.getInt("ce.id_ord");
                mnPkChargeId = resultSet.getInt("ce.id_chg");
                mnPkEntryId = resultSet.getInt("ce.id_ety");
                mdGrossRequirement_r = resultSet.getDouble("ce.gross_req_r");
                mbIsRequest = resultSet.getBoolean("ce.b_req");
                mbIsDeleted = resultSet.getBoolean("ce.b_del");
                mnFkItemId_r = resultSet.getInt("ce.fid_item_r");
                mnFkUnitId_r = resultSet.getInt("ce.fid_unit_r");
                mnFkUserNewId = resultSet.getInt("ce.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("ce.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("ce.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ce.ts_new");
                mtUserEditTs = resultSet.getTimestamp("ce.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ce.ts_del");

                mdDbmsQuantity = resultSet.getDouble("o.qty");
                mdDbmsCharges = resultSet.getDouble("o.chgs");
                mdDbmsGrossRequirement_r = resultSet.getDouble("ce.gross_req_r");
                msDbmsLevelItem = resultSet.getString("ci.item");
                msDbmsLevelItemKey = resultSet.getString("ci.item_key");
                msDbmsLevelUnitSymbol = resultSet.getString("cu.symbol");
                msDbmsProductionOrder = resultSet.getString("f_ref");
                msDbmsFinishedGood = resultSet.getString("i.item");
                msDbmsFinishedGoodKey = resultSet.getString("i.item_key");
                msDbmsBom = resultSet.getString("b.bom");
                msDbmsUnitSymbol = resultSet.getString("u.symbol");
                msDbmsProductionOrderType = resultSet.getString("t.tp");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");
                mbDbmsItemSubstitute = resultSet.getBoolean("f_substitute");

                // Read entries lots:

                mvDbmsProductionOrderChargeEntryLots.removeAllElements();
                statementAux = statement.getConnection().createStatement();
                sql = "SELECT * FROM mfg_ord_chg_ety_lot " +
                        "WHERE id_year = " + key[0] + " AND id_ord = " + key[1] + " AND id_chg = " + key[2] + " AND id_ety = " + key[3] + " " +
                        "ORDER BY id_year, id_ord, id_chg, id_ety, id_item, id_unit, id_lot ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    oProductionOrderChargeEntryLot = new SDataProductionOrderChargeEntryLot();
                    if (oProductionOrderChargeEntryLot.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_ord"), resultSet.getInt("id_chg"), resultSet.getInt("id_ety"),
                            resultSet.getInt("id_item"), resultSet.getInt("id_unit"), resultSet.getInt("id_lot") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        msDbmsLots = msDbmsLots + oProductionOrderChargeEntryLot.getDbmsLot() + "; ";
                        mvDbmsProductionOrderChargeEntryLots.add(oProductionOrderChargeEntryLot);

                        move = new STrnStockMove(new int[] {
                            oProductionOrderChargeEntryLot.getPkYearId(),
                            oProductionOrderChargeEntryLot.getPkItemId(),
                            oProductionOrderChargeEntryLot.getPkUnitId(),
                            oProductionOrderChargeEntryLot.getPkLotId(),
                            mnDbmsFkCobId,
                            0 },
                            oProductionOrderChargeEntryLot.getQuantity());
                        move.setAuxLot(oProductionOrderChargeEntryLot.getDbmsLot());
                        move.setAuxLotDateExpiration(oProductionOrderChargeEntryLot.getDbmsDateExpiration_n());
                        mvAuxStockMoves.add(move);
                    }

                    // Read aswell stock moves:
                    /*
                    sql = "SELECT s.id_year, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, s.mov_in, s.mov_out, l.lot, l.dt_exp_n " +
                            "FROM trn_stk AS s " +
                            "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot " +
                            "WHERE s.id_year = " + resultSet.getInt("id_year") + " AND s.id_item = " + resultSet.getInt("id_item") + " AND " +
                            " s.id_unit = " + resultSet.getInt("id_unit") + " AND s.id_lot = " + resultSet.getInt("id_lot") + " AND s.b_del = 0 ";
                    resultSet = statement.executeQuery(sql);
                    System.out.println("sql: " + sql);
                    while (resultSet.next()) {
                        move = new STrnStockMove(new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6) },
                                resultSet.getDouble("s.mov_in") + resultSet.getDouble("s.mov_out"));
                        move.setAuxLot(resultSet.getString("l.lot"));
                        move.setAuxDateExpiration(resultSet.getDate("l.dt_exp_n"));
                        mvAuxStockMoves.add(move);
                    }*/
                }

                // Assign lots:

                if (msDbmsLots.length() > 0) {
                    msDbmsLots = msDbmsLots.substring(0, msDbmsLots.length() - 2);
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
        String sSql = "";

        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        SDataProductionOrderChargeEntryLot oProductionOrderChargeEntryLot = null;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_ord_chg_ety_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkOrderId);
            callableStatement.setInt(nParam++, mnPkChargeId);
            callableStatement.setInt(nParam++, mnPkEntryId);
            callableStatement.setDouble(nParam++, mdGrossRequirement_r);
            callableStatement.setBoolean(nParam++, mbIsRequest);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemId_r);
            callableStatement.setInt(nParam++, mnFkUnitId_r);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkEntryId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            // Delete entries lot:

            sSql = "DELETE FROM mfg_ord_chg_ety_lot WHERE id_year = " + mnPkYearId + " AND id_ord = " + mnPkOrderId + " AND id_chg = " + mnPkChargeId + " AND id_ety = " + mnPkEntryId + " ";
            connection.createStatement().execute(sSql);

            // Save entries lot:

            for (int i=0; i<mvDbmsProductionOrderChargeEntryLots.size(); i++) {
                oProductionOrderChargeEntryLot = mvDbmsProductionOrderChargeEntryLots.get(i);

                oProductionOrderChargeEntryLot.setPkYearId(mnPkYearId);
                oProductionOrderChargeEntryLot.setPkOrderId(mnPkOrderId);
                oProductionOrderChargeEntryLot.setPkChargeId(mnPkChargeId);
                oProductionOrderChargeEntryLot.setPkEntryId(mnPkEntryId);
                oProductionOrderChargeEntryLot.setPkLotId(oProductionOrderChargeEntryLot.getPkLotId());
                if (oProductionOrderChargeEntryLot.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
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
