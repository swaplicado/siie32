/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataBom extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBomId;
    protected java.util.Date mtDateStart;
    protected int mnLevel;
    protected java.util.Date mtDateEnd_n;
    protected java.lang.String msBom;
    protected double mdQty;
    protected double mdPercentage;
    protected double mdCost;
    protected double mdCostPercentage;
    protected double mdDuration;
    protected int mnRoot;
    protected boolean mbIsNotExplotion;
    protected boolean mbIsRequested;
    protected boolean mbIsDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkItemId_n;
    protected int mnFkUnitId_n;
    protected int mnFkCobId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.util.Vector<erp.mmfg.data.SDataBom> mvDbmsBomLevels;
    protected java.util.Vector<erp.mmfg.data.SDataBomNotes> mvDbmsBomNotes;
    protected java.util.Vector<erp.mmfg.data.SDataBomSubgoods> mvDbmsBomSubgoods;    
    protected java.util.Vector<erp.mmfg.data.SDataBomSubstitute> mvDbmsBomSubstitute;

    protected java.lang.String msDbmsCompanyBranch;
    protected java.lang.String msDbmsItem;
    protected java.lang.String msDbmsItemUnitSymbol;
    protected java.lang.String msDbmsItemKeyRm;
    protected java.lang.String msDbmsItemRm;
    protected java.lang.String msDbmsItemRmUnitSymbol;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    protected boolean mbDbmsRecursive;
    protected int mnDbmsAuxSortingItem;

    public SDataBom() {
        super(SDataConstants.MFG_BOM);

        mvDbmsBomLevels = new Vector<SDataBom>();
        mvDbmsBomNotes = new Vector<SDataBomNotes>();
        mvDbmsBomSubgoods = new Vector<SDataBomSubgoods>();
        mvDbmsBomSubstitute = new Vector<SDataBomSubstitute>();
        reset();
    }
    
    public void setPkBomId(int n) { mnPkBomId = n; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setLevel(int n) { mnLevel = n; }
    public void setBom(java.lang.String s) { msBom = s; }
    public void setQty(double d) { mdQty = d; }
    public void setRoot(int n) { mnRoot = n; }
    public void setDuration(double d) { mdDuration = d; }
    public void setCost(double d) { mdCost = d; }
    public void setCostPercentage(double d) { mdCostPercentage = d; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setIsNotExplotion(boolean b) { mbIsNotExplotion = b; }
    public void setIsRequested(boolean b) { mbIsRequested = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkItemId_n(int n) { mnFkItemId_n = n; }
    public void setFkUnitId_n(int n) { mnFkUnitId_n = n; }
    public void setFkCobId(int n) { mnFkCobId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBomId() { return mnPkBomId; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public int getLevel() { return mnLevel; }
    public java.lang.String getBom() { return msBom; }
    public double getQuantity() { return mdQty; }
    public int getRoot() { return mnRoot; }
    public double getDuration() { return mdDuration; }
    public double getCost() { return mdCost; }
    public double getCostPercentage() { return mdCostPercentage; }
    public double getPercentage() { return mdPercentage; }
    public boolean getIsNotExplotion() { return mbIsNotExplotion; }
    public boolean getIsRequested() { return mbIsRequested; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkItemId_n() { return mnFkItemId_n; }
    public int getFkUnitId_n() { return mnFkUnitId_n; }
    public int getFkCobId() { return mnFkCobId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsItem(java.lang.String s) { msDbmsItem = s; }
    public void setDbmsItemUnitSymbol(java.lang.String s) { msDbmsItemUnitSymbol = s; }
    public void setDbmsItemKeyRm(java.lang.String s) { msDbmsItemKeyRm = s; }
    public void setDbmsItemRm(java.lang.String s) { msDbmsItemRm = s; }
    public void setDbmsItemRmUnitSymbol(java.lang.String s) { msDbmsItemRmUnitSymbol = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }
    public void setDbmsRecursive(boolean b) { mbDbmsRecursive = b; }
    public void setDbmsAuxSortingItem(int n) { mnDbmsAuxSortingItem = n; }

    public java.lang.String getDbmsCompanyBranch() { return msDbmsCompanyBranch; }
    public java.lang.String getDbmsItem() { return msDbmsItem; }
    public java.lang.String getDbmsItemUnitSymbol() { return msDbmsItemUnitSymbol; }
    public java.lang.String getDbmsItemKeyRm() { return msDbmsItemKeyRm; }
    public java.lang.String getDbmsItemRm() { return msDbmsItemRm; }
    public java.lang.String getDbmsItemRmUnitSymbol() { return msDbmsItemRmUnitSymbol; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    public int getDbmsAuxSortingItem() { return mnDbmsAuxSortingItem; }
    
    public java.util.Vector<SDataBom> getDbmsLevel() { return mvDbmsBomLevels; }
    public java.util.Vector<SDataBomNotes> getDbmsNotes() { return mvDbmsBomNotes; }
    public java.util.Vector<SDataBomSubgoods> getDbmsBomSubgoods() { return mvDbmsBomSubgoods; }
    public java.util.Vector<SDataBomSubstitute> getDbmsBomSubstitute() { return mvDbmsBomSubstitute; }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBomId = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mnLevel = 0;
        msBom = "";
        mdQty = 0;
        mdDuration = 0;
        mnRoot = 0;
        mdCost = 0;
        mdCostPercentage = 0;
        mdPercentage = 0;
        mbIsNotExplotion = false;
        mbIsRequested = false;
        mbIsDeleted = false;        
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkItemId_n = 0;
        mnFkUnitId_n = 0;
        mnFkCobId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsCompanyBranch = "";
        msDbmsItem = "";
        msDbmsItemUnitSymbol = "";
        msDbmsItemKeyRm = "";
        msDbmsItemRm = "";
        msDbmsItemRmUnitSymbol = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
        mbDbmsRecursive = false;
        
        mvDbmsBomSubgoods.clear();
        mvDbmsBomLevels.clear();
        mvDbmsBomNotes.clear();
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkBomId = ((int[]) key)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBomId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        int nParam = 1;
        String sql = "";

        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        Statement statementAux = null;
        CallableStatement callableStatement = null;

        SDataBom level = null;
        SDataBomNotes notes = null;
        SDataBomSubgoods subgood = null;
        SDataBomSubstitute substitute = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT b.*, bpb.bpb, " +
                    (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? 
                    "CONCAT(it.item_key, ' - ', it.item) " : "CONCAT(it.item, ' - ', it.item_key) ") + " AS f_item, " +
                    "ui.symbol, it.item_key, it.item, uit.symbol, un.usr, ue.usr, ud.usr " +
                    "FROM mfg_bom AS b " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON b.fid_cob = bpb.id_bpb " +
                    "LEFT JOIN erp.itmu_item AS i ON b.fid_item_n = i.id_item " +
                    "INNER JOIN erp.itmu_item AS it ON b.fid_item = it.id_item " +                    
                    "LEFT JOIN erp.itmu_unit AS ui ON b.fid_unit = ui.id_unit " +
                    "INNER JOIN erp.itmu_unit AS uit ON it.fid_unit = uit.id_unit " +
                    "INNER JOIN erp.usru_usr AS un ON b.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON b.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON b.fid_usr_del = ud.id_usr " +
                    "WHERE b.id_bom = " + key[0] + " ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBomId = resultSet.getInt("b.id_bom");
                mtDateStart = resultSet.getTimestamp("b.ts_start");
                mtDateEnd_n = resultSet.getTimestamp("b.ts_end_n");
                if (resultSet.wasNull()) mtDateEnd_n = null;
                mnLevel = resultSet.getInt("b.lev");
                msBom = resultSet.getString("b.bom");
                mdQty = resultSet.getDouble("b.qty");
                mdDuration = resultSet.getDouble("b.duration");
                mnRoot = resultSet.getInt("b.root");
                mdCost = resultSet.getDouble("b.cost");
                mdCostPercentage = resultSet.getDouble("b.cost_per");
                mdPercentage = resultSet.getDouble("b.per");
                mbIsNotExplotion = resultSet.getBoolean("b.b_exp");
                mbIsRequested = resultSet.getBoolean("b.b_req");
                mbIsDeleted = resultSet.getBoolean("b.b_del");
                mnFkItemId = resultSet.getInt("b.fid_item");
                mnFkUnitId = resultSet.getInt("b.fid_unit");
                mnFkItemId_n = resultSet.getInt("fid_item_n");
                if (resultSet.wasNull()) mnFkItemId_n = 0;
                mnFkUnitId_n = resultSet.getInt("fid_unit_n");
                if (resultSet.wasNull()) mnFkUnitId_n = 0;
                mnFkCobId = resultSet.getInt("b.fid_cob");
                mnFkUserNewId = resultSet.getInt("b.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("b.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("b.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("b.ts_new");
                mtUserEditTs = resultSet.getTimestamp("b.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("b.ts_del");

                msDbmsCompanyBranch = resultSet.getString("bpb.bpb");
                msDbmsItem = resultSet.getString("f_item");
                msDbmsItemUnitSymbol = resultSet.getString("ui.symbol");
                msDbmsItemKeyRm = resultSet.getString("it.item_key");
                msDbmsItemRm = resultSet.getString("it.item");
                msDbmsItemRmUnitSymbol = resultSet.getString("uit.symbol");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                // Read subgoods:

                statementAux = statement.getConnection().createStatement();
                mvDbmsBomSubgoods.removeAllElements();
                sql = "SELECT s.id_sgds, s.fid_bom, s.fid_item, i.item_key, i.item, i.id_item " +
                    "FROM mfg_sgds AS s " +
                    "INNER JOIN erp.itmu_item AS i ON s.fid_item = i.id_item " +
                    "WHERE s.fid_bom = " + key[0] + " " +
                    "ORDER BY " + (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        "i.item_key, i.item " : "i.item, i.item_key ");
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    subgood = new SDataBomSubgoods();
                    if (subgood.read(new int[] { resultSet.getInt("s.id_sgds") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBomSubgoods.add(subgood);
                    }
                }
                
                // Read levels:

                if (!mbDbmsRecursive) {
                    callableStatement = statement.getConnection().prepareCall(
                        "{ CALL mfg_bom_qry(" +
                        "?, ?, ?, ?, ?, ?) }");
                    callableStatement.setInt(nParam++, mnFkItemId);
                    callableStatement.setString(nParam++, msBom);
                    callableStatement.setString(nParam++, "tmpBom" + java.util.Calendar.SECOND + key[0]);
                    callableStatement.setInt(nParam++, mnDbmsAuxSortingItem);
                    callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
                    callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
                    callableStatement.execute();

                    mnDbmsErrorId = callableStatement.getInt(nParam - 2);
                    msDbmsError = callableStatement.getString(nParam - 1);

                    if (mnDbmsErrorId != 0) {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
                    }
                    else {
                        resultSet = callableStatement.getResultSet();
                        while (resultSet.next()) {
                            level = new SDataBom();
                            level.setDbmsRecursive(true);
                            level.setDbmsAuxSortingItem(mnDbmsAuxSortingItem);
                            level.setBom(msBom);
                            if (level.read(new int[] { resultSet.getInt("id_bom") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                            else {
                                // Read raw material substitute:

                                sql = "SELECT * FROM mfg_bom_sub WHERE b_del = 0 AND id_bom = " + level.getPkBomId() + " AND id_item = " + level.getFkItemId() + " AND id_unit = " + level.getFkUnitId();
                                resultSetAux = statement.executeQuery(sql);
                                while (resultSetAux.next()) {
                                    substitute = new SDataBomSubstitute();
                                    if (substitute.read(new int[] { resultSetAux.getInt("id_bom"), resultSetAux.getInt("id_item"), resultSetAux.getInt("id_unit"), resultSetAux.getInt("id_bom_sub") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                                    }
                                    else {
                                        level.mvDbmsBomSubstitute.add(substitute);
                                    }
                                }

                                mvDbmsBomLevels.add(level);
                            }
                        }
                    }
                }

                // Read notes:

                mvDbmsBomNotes.removeAllElements();
                sql = "SELECT * " +
                      "FROM mfg_bom_nts " +
                      "WHERE id_bom = " + key[0] + " ";
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    notes = new SDataBomNotes();
                    if (notes.read(new int[] { resultSet.getInt("id_bom"), resultSet.getInt("id_nts") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsBomNotes.add(notes);
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
        int i = 0;
        int j = 0;
        int nParam = 1;

        CallableStatement callableStatement = null;
        SDataBomSubgoods subgood = null;
        SDataBomSubstitute substitute = null;
        SDataBom level = null;
        SDataBomNotes notes = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_bom_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkBomId);
            callableStatement.setTimestamp(nParam++, new java.sql.Timestamp(mtDateStart.getTime()));
            if (mtDateEnd_n != null) callableStatement.setDate(nParam++, new java.sql.Date(mtDateEnd_n.getTime())); else callableStatement.setNull(nParam++, java.sql.Types.DATE);
            callableStatement.setInt(nParam++, mnLevel);
            callableStatement.setString(nParam++, msBom);
            callableStatement.setDouble(nParam++, mdQty);
            callableStatement.setDouble(nParam++, mdDuration);
            callableStatement.setInt(nParam++, mnRoot);
            callableStatement.setDouble(nParam++, mdCost);
            callableStatement.setDouble(nParam++, mdCostPercentage);
            callableStatement.setDouble(nParam++, mdPercentage);
            callableStatement.setBoolean(nParam++, mbIsNotExplotion);
            callableStatement.setBoolean(nParam++, mbIsRequested);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemId);
            callableStatement.setInt(nParam++, mnFkUnitId);
            if (mnLevel > 0) callableStatement.setInt(nParam++, mnFkItemId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            if (mnLevel > 0) callableStatement.setInt(nParam++, mnFkUnitId_n); else callableStatement.setNull(nParam++, java.sql.Types.INTEGER);
            callableStatement.setInt(nParam++, mnFkCobId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkBomId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
                // Save subgoods:

                for (i=0; i<mvDbmsBomSubgoods.size(); i++) {
                    subgood = (SDataBomSubgoods) mvDbmsBomSubgoods.get(i);
                    if (subgood != null) {
                        subgood.setFkBomId(mnPkBomId);
                        if (subgood.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }
                // Save levels:

                for (i=0; i<mvDbmsBomLevels.size(); i++) {
                    level = (SDataBom) mvDbmsBomLevels.get(i);
                    if (level != null) {
                        level.setFkCobId(mnFkCobId);
                        level.setRoot(mnRoot);
                        level.setBom(msBom);
                        if (level.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                        
                        // Save raw material substitute:

                        for(j=0; j<level.getDbmsBomSubstitute().size(); j++) {
                            substitute = level.getDbmsBomSubstitute().get(j);

                            substitute.setPkBomId(level.getPkBomId());
                            if (substitute.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                            }
                        }
                    }
                }

                // Save notes:

                for (i = 0; i < mvDbmsBomNotes.size(); i++) {
                    notes = (SDataBomNotes) mvDbmsBomNotes.get(i);
                    if (notes != null) {
                        notes.setPkBomId(mnPkBomId);
                        if (notes.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
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
