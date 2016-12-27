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
public class SDataExplotionMaterials extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkExpId;
    protected java.util.Date mtDateDocument;
    protected java.lang.String msReference;
    protected java.lang.String msComments;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected boolean mbDbmsIsExplotion;
    protected double mdDbmsProductionOrderQuantity;
    protected java.lang.String msDbmsProductionOrderInitial;
    protected java.lang.String msDbmsProductionOrderEnd;
    protected java.lang.String msDbmsProductionOrderUnit;
    protected java.lang.String msDbmsCompanyBranch;
    protected java.lang.String msDbmsCompanyBranchCode;
    protected java.lang.String msDbmsWarehouse;
    protected java.lang.String msDbmsWarehouseCode;

    protected int mnDbmsAuxSortingItem;

    protected java.util.Vector<erp.mmfg.data.SDataExplotionMaterialsEntry> mvDbmsExplotionMaterialsEntry;
    protected java.util.Vector<erp.mmfg.data.SDataExplotionMaterialsEntryItem> mvDbmsExplotionMaterialsEntryItem;
    protected java.util.Vector<erp.mmfg.data.SDataProductionOrder> mvDbmsProductionsOrders;
    protected java.util.Vector<erp.mmfg.data.SDataProductionOrderCharge> mvDbmsProductionOrderCharges;
    protected java.util.Vector<erp.mmfg.data.SDataProductionOrderBomSubgoods> mvDbmsProductionOrderSubgoods;
    
    protected erp.mmfg.data.SDataExplotionMaterialsEntry moExplotionMaterialsEntry = null;
    protected erp.mmfg.data.SDataExplotionMaterialsEntryItem moExplotionMaterialsEntryItem = null;
    protected erp.mmfg.data.SDataExplotionMaterialsProdOrder moExplotionMaterialsProductionOrder = null;
    protected erp.mmfg.data.SDataProductionOrder moProductionOrder = null;
    protected erp.mmfg.data.SDataProductionOrderCharge moProductionOrderCharges = null;
    protected erp.mmfg.data.SDataProductionOrderBomSubgoods moProductionOrderSubgoods = null;

    public SDataExplotionMaterials() {
        super(SDataConstants.MFG_EXP);
        mvDbmsExplotionMaterialsEntry = new Vector<SDataExplotionMaterialsEntry>();
        mvDbmsExplotionMaterialsEntryItem = new Vector<SDataExplotionMaterialsEntryItem>();
        mvDbmsProductionOrderCharges = new Vector<SDataProductionOrderCharge>();
        mvDbmsProductionOrderSubgoods = new Vector<SDataProductionOrderBomSubgoods>();
        mvDbmsProductionsOrders = new Vector<SDataProductionOrder>();

        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkExpId(int n) { mnPkExpId = n; }
    public void setDateDocument(java.util.Date t) { mtDateDocument = t; }
    public void setReference(java.lang.String n) { msReference = n; }
    public void setComments(java.lang.String s) { msComments = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
        
    public int getPkYearId() { return mnPkYearId; }
    public int getPkExpId() { return mnPkExpId; }
    public java.util.Date getDateDocument() { return mtDateDocument; }
    public java.lang.String getReference() { return msReference; }
    public java.lang.String getComments() { return msComments; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId(int n) { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsIsExplotion(boolean b) { mbDbmsIsExplotion = b; }
    public void setIsNewRegistry(boolean b) { mbIsRegistryNew = b; }
    public void setDbmsExplotionMaterialsEntry(java.util.Vector<SDataExplotionMaterialsEntry> v) { mvDbmsExplotionMaterialsEntry = v; }
    public void setDbmsExplotionMaterialsEntryItem(java.util.Vector<SDataExplotionMaterialsEntryItem> v) { mvDbmsExplotionMaterialsEntryItem = v; }
    public void setDbmsProductionsOrders(java.util.Vector<SDataProductionOrder> v) { mvDbmsProductionsOrders = v; }
    public void setDbmsProductionOrderCharges(java.util.Vector<SDataProductionOrderCharge> v) { mvDbmsProductionOrderCharges = v; }
    public void setDbmsProductionOrderSubgoods(java.util.Vector<SDataProductionOrderBomSubgoods> v) { mvDbmsProductionOrderSubgoods = v; }
    public void setDbmsAuxSortingItem(int n) { mnDbmsAuxSortingItem = n; }

    public boolean getDbmsIsExplotion() { return mbDbmsIsExplotion; }
    public double getDbmsProductionOrderQuantity() { return mdDbmsProductionOrderQuantity; }
    public java.lang.String getDbmsProductionOrderInitial() { return msDbmsProductionOrderInitial; }
    public java.lang.String getDbmsProductionOrderEnd() { return msDbmsProductionOrderEnd; }
    public java.lang.String getDbmsProductionOrderUnit() { return msDbmsProductionOrderUnit; }
    public java.lang.String getDbmsCompanyBranch() { return msDbmsCompanyBranch; }
    public java.lang.String getDbmsCompanyBranchCode() { return msDbmsCompanyBranchCode; }
    public java.lang.String getDbmsWarehouse() { return msDbmsWarehouse; }
    public java.lang.String getDbmsWarehouseCode() { return msDbmsWarehouseCode; }
    public int getDbmsAuxSortingItem() { return mnDbmsAuxSortingItem; }

    public java.util.Vector<SDataExplotionMaterialsEntry> getDbmsExplotionMaterialsEntry() { return mvDbmsExplotionMaterialsEntry; }
    public java.util.Vector<SDataExplotionMaterialsEntryItem> getDbmsExplotionMaterialsEntryItem() { return mvDbmsExplotionMaterialsEntryItem; }
    public java.util.Vector<SDataProductionOrder> getDbmsProductionsOrders() { return mvDbmsProductionsOrders; }
    public java.util.Vector<SDataProductionOrderCharge> getDbmsProductionOrderCharges() { return mvDbmsProductionOrderCharges; }
    public java.util.Vector<SDataProductionOrderBomSubgoods> getDbmsProductionOrderSubgoods() { return mvDbmsProductionOrderSubgoods; }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkExpId = 0;
        mtDateDocument = null;
        msReference = "";
        msComments = "";
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mbDbmsIsExplotion = false;
        mdDbmsProductionOrderQuantity = 0;
        msDbmsProductionOrderInitial = "";
        msDbmsProductionOrderEnd = "";
        msDbmsProductionOrderUnit = "";
        msDbmsCompanyBranch = "";
        msDbmsCompanyBranchCode = "";
        msDbmsWarehouse = "";
        msDbmsWarehouseCode = "";

        moExplotionMaterialsEntry = null;
        moExplotionMaterialsEntryItem = null;
        moExplotionMaterialsProductionOrder = null;
        moProductionOrderCharges = null;
        moProductionOrderSubgoods = null;

        mvDbmsExplotionMaterialsEntry.clear();
        mvDbmsExplotionMaterialsEntryItem.clear();
        mvDbmsProductionsOrders.clear();
        mvDbmsProductionOrderCharges.clear();
        mvDbmsProductionOrderSubgoods.clear();
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkYearId = ((int[]) key)[0];
        mnPkExpId = ((int[]) key)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkExpId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
                
        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        Statement statementAux = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT m.id_exp_year, m.id_exp, CONCAT(MIN(m.id_ord_year), '-',erp.lib_fix_int(MIN(o.num),6), ', ', o.ref) AS ord_ini, " +
                "CONCAT(MAX(m.id_ord_year), '-', erp.lib_fix_int(MAX(o.num),6), ', ', " + 
                "(SELECT ref FROM mfg_ord WHERE id_year =  MAX(m.id_ord_year) AND id_ord = MAX(m.id_ord))) AS ord_end, " +
                "o.qty AS qty, u.symbol, bpb.bpb, bpb.code, ent.ent, ent.code, e.* " +
                "FROM mfg_exp AS e " +
                "INNER JOIN mfg_exp_ety AS et ON e.id_year = et.id_year AND e.id_exp = et.id_exp " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON et.fid_cob = bpb.id_bpb " +                
                "INNER JOIN mfg_exp_ord AS m ON e.id_year = m.id_exp_year AND e.id_exp = m.id_exp " +
                "INNER JOIN mfg_ord AS o ON m.id_ord_year = o.id_year AND m.id_ord = o.id_ord " +
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
                "INNER JOIN erp.usru_usr AS un ON e.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON e.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON e.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS ent ON et.fid_cob_n = ent.id_cob AND et.fid_wh_n = ent.id_ent " +
                "WHERE e.id_year = " + key[0] + " AND e.id_exp = " + key[1] + " " +
                "GROUP BY m.id_exp_year, m.id_exp ";

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("e.id_year");
                mnPkExpId = resultSet.getInt("e.id_exp");
                mtDateDocument = resultSet.getTimestamp("e.dt_doc");
                msReference = resultSet.getString("e.ref");
                msComments = resultSet.getString("e.comms");
                mbIsDeleted = resultSet.getBoolean("e.b_del");
                mnFkUserNewId = resultSet.getInt("e.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("e.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("e.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("e.ts_new");
                mtUserEditTs = resultSet.getTimestamp("e.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("e.ts_del");

                msDbmsCompanyBranch = resultSet.getString("bpb.bpb");
                msDbmsCompanyBranchCode = resultSet.getString("bpb.code");
                msDbmsWarehouse = resultSet.getString("ent.ent");
                if (resultSet.wasNull()) msDbmsWarehouse = "(n/a)";
                msDbmsWarehouseCode = resultSet.getString("ent.code");
                if (resultSet.wasNull()) msDbmsWarehouseCode = "(n/a)";
                msDbmsProductionOrderInitial = resultSet.getString("ord_ini");
                msDbmsProductionOrderEnd = resultSet.getString("ord_end");                
                if (msDbmsProductionOrderInitial.compareTo(msDbmsProductionOrderEnd) != 0) {
                    msDbmsProductionOrderUnit = "(n/a)";
                    mdDbmsProductionOrderQuantity = 0;
                }
                else {
                    msDbmsProductionOrderUnit = resultSet.getString("symbol");
                    mdDbmsProductionOrderQuantity = resultSet.getDouble("qty");
                }
                
                // Read entries:

                statementAux = statement.getConnection().createStatement();
                mvDbmsExplotionMaterialsEntry.removeAllElements();                
                sql = "SELECT e.*, i.item, i.item_key " +
                    "FROM mfg_exp_ety AS e " +
                    "INNER JOIN erp.itmu_item AS i ON e.id_item = i.id_item " +
                    "WHERE e.id_year = " + key[0] + " AND e.id_exp = " + key[1] + " " +
                    (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "ORDER BY i.item_key, i.item " : "ORDER BY i.item, i.item_key ");
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    moExplotionMaterialsEntry = new SDataExplotionMaterialsEntry();                    
                    if (moExplotionMaterialsEntry.read(new int[] { resultSet.getInt("e.id_year"), resultSet.getInt("e.id_exp"), resultSet.getInt("e.id_item"), resultSet.getInt("e.id_unit") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsExplotionMaterialsEntry.add(moExplotionMaterialsEntry);
                    }
                }

                // Read entries items:
                
                mvDbmsExplotionMaterialsEntryItem.removeAllElements();
                sql = "SELECT e.*, i.item_key, i.item " +
                    "FROM mfg_exp_ety_item AS e " +
                    "INNER JOIN erp.itmu_item AS i ON e.id_item = i.id_item " +
                    "WHERE e.id_year = " + key[0] + " AND e.id_exp = " + key[1] + " " +
                    (mnDbmsAuxSortingItem == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "ORDER BY i.item_key, i.item " : "ORDER BY i.item, i.item_key ");
                resultSet = statement.executeQuery(sql);                
                while (resultSet.next()) {
                    moExplotionMaterialsEntryItem = new SDataExplotionMaterialsEntryItem();

                    moExplotionMaterialsEntryItem.setPkYearId(resultSet.getInt("e.id_year"));
                    moExplotionMaterialsEntryItem.setPkExpId(resultSet.getInt("e.id_exp"));
                    moExplotionMaterialsEntryItem.setPkItemId(resultSet.getInt("e.id_item"));
                    moExplotionMaterialsEntryItem.setPkUnitId(resultSet.getInt("e.id_unit"));
                    moExplotionMaterialsEntryItem.setPkOrdYearId(resultSet.getInt("e.id_ord_year"));
                    moExplotionMaterialsEntryItem.setPkOrdId(resultSet.getInt("e.id_ord"));
                    moExplotionMaterialsEntryItem.setGrossRequirement(resultSet.getDouble("e.gross_req"));                    
                    mvDbmsExplotionMaterialsEntryItem.add(moExplotionMaterialsEntryItem);
                }

                // Read productions orders:
                
                mvDbmsProductionsOrders.removeAllElements();
                statementAux = statement.getConnection().createStatement();
                sql = "SELECT * " +
                        "FROM mfg_exp_ord " +
                        "WHERE id_exp_year = " + key[0] + " AND id_exp = " + key[1];
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    moProductionOrder = new SDataProductionOrder();
                    moProductionOrder.setPrimaryKey(new int[] { resultSet.getInt("id_ord_year"), resultSet.getInt("id_ord") });
                    mvDbmsProductionsOrders.add(moProductionOrder);

                    // Read production orders vs subgoods:
                    
                    sql = "SELECT * FROM mfg_ord_sgds WHERE id_year = " + resultSet.getInt("id_ord_year") + " AND id_ord = " + resultSet.getInt("id_ord");
                    statementAux = statement.getConnection().createStatement();
                    resultSetAux = statementAux.executeQuery(sql);
                    while (resultSetAux.next()) {
                        moProductionOrderSubgoods = new SDataProductionOrderBomSubgoods();
                        statementAux = statement.getConnection().createStatement();
                        if (moProductionOrderSubgoods.read(new int[] { resultSetAux.getInt("id_year"), resultSetAux.getInt("id_ord"), resultSetAux.getInt("id_sgds") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            mvDbmsProductionOrderSubgoods.add(moProductionOrderSubgoods);
                        }
                    }
                }
                
                // Read productions orders charges:
                
                mvDbmsProductionOrderCharges.removeAllElements();
                statementAux = statement.getConnection().createStatement();
                for (int i=0; i<mvDbmsProductionsOrders.size(); i++) {

                    moProductionOrder = mvDbmsProductionsOrders.get(i);
                    sql = "SELECT * FROM mfg_ord_chg WHERE id_year = " + moProductionOrder.getPkYearId() + " AND id_ord = " + moProductionOrder.getPkOrdId();
                    resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        moProductionOrderCharges = new SDataProductionOrderCharge();
                        if (moProductionOrderCharges.read(new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_ord"), resultSet.getInt("id_chg") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {                    
                            mvDbmsProductionOrderCharges.add(moProductionOrderCharges);
                        }
                    }
                }
                
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {        
        int i=0;
        int j=0;
        int nParam=1;
        String sSql="";

        CallableStatement callableStatement = null;
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL mfg_exp_save(" +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkExpId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDateDocument.getTime()));
            callableStatement.setString(nParam++, msReference);
            callableStatement.setString(nParam++, msComments);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkYearId = callableStatement.getInt(nParam - 4);
            mnPkExpId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
                // Check if is explotion action or only save information:

                if (mbDbmsIsExplotion) {
                    
                    // Delete explotion materials entries:

                    statement = connection.createStatement();
                    sSql = "DELETE FROM mfg_exp_ety WHERE id_year = " + mnPkYearId + " AND id_exp = " + mnPkExpId;
                    statement.executeUpdate(sSql);

                    // Save entries:

                    for (i=0; i<mvDbmsExplotionMaterialsEntry.size(); i++) {
                        moExplotionMaterialsEntry = (SDataExplotionMaterialsEntry) mvDbmsExplotionMaterialsEntry.get(i);
                        if (moExplotionMaterialsEntry != null) {
                            moExplotionMaterialsEntry.setPkYearId(mnPkYearId);
                            moExplotionMaterialsEntry.setPkExpId(mnPkExpId);
                            if (moExplotionMaterialsEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                            }
                        }
                    }
                    // Delete explotion materials entries item:

                    sSql = "DELETE FROM mfg_exp_ety_item WHERE id_year = " + mnPkYearId + " AND id_exp = " + mnPkExpId;
                    statement.executeUpdate(sSql);

                    // Save entries items:

                    for (i=0; i<mvDbmsExplotionMaterialsEntryItem.size(); i++) {
                        moExplotionMaterialsEntryItem = (SDataExplotionMaterialsEntryItem) mvDbmsExplotionMaterialsEntryItem.get(i);
                        if (moExplotionMaterialsEntryItem != null) {
                            moExplotionMaterialsEntryItem.setPkYearId(mnPkYearId);
                            moExplotionMaterialsEntryItem.setPkExpId(mnPkExpId);
                            if (moExplotionMaterialsEntryItem.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                            }
                        }
                    }
                    // Delete explotion material vs orders production:

                    sSql = "DELETE FROM mfg_exp_ord WHERE id_exp_year = " + mnPkYearId + " AND id_exp = " + mnPkExpId;
                    statement.executeUpdate(sSql);

                    // Save production orders:

                    for (i=0; i<mvDbmsProductionsOrders.size(); i++) {

                        moProductionOrder = mvDbmsProductionsOrders.get(i);
                        moExplotionMaterialsProductionOrder = new SDataExplotionMaterialsProdOrder();

                        moExplotionMaterialsProductionOrder.setPkExpYearId(mnPkYearId);
                        moExplotionMaterialsProductionOrder.setPkExpId(mnPkExpId);
                        moExplotionMaterialsProductionOrder.setPkOrdYearId(moProductionOrder.getPkYearId());
                        moExplotionMaterialsProductionOrder.setPkOrdId(moProductionOrder.getPkOrdId());

                        if (moExplotionMaterialsProductionOrder.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }

                        // Save production order vs subgoods entries:

                        for (j=0; j<mvDbmsProductionOrderSubgoods.size(); j++) {
                            moProductionOrderSubgoods = (SDataProductionOrderBomSubgoods) mvDbmsProductionOrderSubgoods.get(j);
                            if (moProductionOrderSubgoods != null) {
                                if (moProductionOrderSubgoods.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                                }
                            }
                        }
                        // Delete production order charges lots:

                        try {
                            sSql = "DELETE FROM mfg_ord_chg_ety_lot WHERE id_year = " + moProductionOrder.getPkYearId() + " AND id_ord = " + moProductionOrder.getPkOrdId();
                            statement.executeUpdate(sSql);
                        }
                        catch (java.lang.Exception e) {
                            SLibUtilities.printOutException(this, e);

                            // Update production order charges entries lot (b_del = 1):

                            statement = connection.createStatement();
                            sSql = "UPDATE mfg_ord_chg_ety_lot SET b_del = 1, ts_del = NOW() WHERE id_year = " + moProductionOrder.getPkYearId() + " AND id_ord = " + moProductionOrder.getPkOrdId();
                            statement.executeUpdate(sSql);
                        }
                        // Delete production order charges entries:

                        try {
                            sSql = "DELETE FROM mfg_ord_chg_ety WHERE id_year = " + moProductionOrder.getPkYearId() + " AND id_ord = " + moProductionOrder.getPkOrdId();
                            statement.executeUpdate(sSql);
                        }
                        catch (java.lang.Exception e) {
                            SLibUtilities.printOutException(this, e);

                            // Update production order charges entries (b_del = 1):

                            statement = connection.createStatement();
                            sSql = "UPDATE mfg_ord_chg_ety SET b_del = 1, ts_del = NOW() WHERE id_year = " + moProductionOrder.getPkYearId() + " AND id_ord = " + moProductionOrder.getPkOrdId();
                            statement.executeUpdate(sSql);
                        }
                        // Delete production order charges:

                        try {
                            sSql = "DELETE FROM mfg_ord_chg WHERE id_year = " + moProductionOrder.getPkYearId() + " AND id_ord = " + moProductionOrder.getPkOrdId();
                            statement.executeUpdate(sSql);
                            
                        }
                        catch (java.lang.Exception e) {
                            SLibUtilities.printOutException(this, e);

                            // Update production order charges (b_del = 1):

                            statement = connection.createStatement();
                            sSql = "UPDATE mfg_ord_chg SET b_del = 1, ts_del = NOW() WHERE id_year = " + moProductionOrder.getPkYearId() + " AND id_ord = " + moProductionOrder.getPkOrdId();
                            statement.executeUpdate(sSql);
                        }
                    }
                    // Save production order charges:

                    for (i=0; i<mvDbmsProductionOrderCharges.size(); i++) {

                        moProductionOrderCharges = mvDbmsProductionOrderCharges.get(i);
                        moProductionOrderCharges.setPkChargeId(0);
                        if (moProductionOrderCharges.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
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
        return null;
    }
}
