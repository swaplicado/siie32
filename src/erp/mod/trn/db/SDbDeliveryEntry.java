package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbDeliveryEntry extends SDbRegistryUser implements SGridRow {
    
    protected int mnPkDeliveryId;
    protected int mnPkEntryId;
    protected double mdQuantity;
    protected double mdOriginalQuantity;
    protected int mnFkDpsYearId;
    protected int mnFkDpsDocId;
    protected int mnFkDpsEntryId;
    protected int mnFkOrderYearId;
    protected int mnFkOrderDocId;
    protected int mnFkOrderEntryId;
    
    protected String msXtaOrderNumberSeries;
    protected String msXtaOrderNumber;
    protected String msXtaOrderNumberReference;
    protected Date mtXtaOrderDate;
    protected int mnXtaEntrySortingPosition;
    protected String msXtaEntryItemCode;
    protected String msXtaEntryItemName;
    protected String msXtaEntryOriginalUnitCode;
    
    public SDbDeliveryEntry() {
        super(SModConsts.TRN_DVY_ETY);
    }

    private void readXtaMembers(final SGuiSession session) {
        SDbDps order = (SDbDps) session.readRegistry(SModConsts.TRN_DPS, new int[] { mnFkOrderYearId, mnFkOrderDocId }, SDbConsts.MODE_STEALTH);
        SDbDpsEntry orderEntry = (SDbDpsEntry) session.readRegistry(SModConsts.TRN_DPS_ETY, new int[] { mnFkOrderYearId, mnFkOrderDocId, mnFkOrderEntryId }, SDbConsts.MODE_STEALTH);
        
        msXtaOrderNumberSeries = order.getNumberSeries();
        msXtaOrderNumber = order.getNumber();
        msXtaOrderNumberReference = order.getNumberReference();
        mtXtaOrderDate = order.getDate();
        mnXtaEntrySortingPosition = orderEntry.getSortingPosition();
        msXtaEntryItemCode = (String) session.readField(SModConsts.ITMU_ITEM, new int[] { orderEntry.getFkItemId() }, FIELD_CODE);
        msXtaEntryItemName = (String) session.readField(SModConsts.ITMU_ITEM, new int[] { orderEntry.getFkItemId() }, FIELD_NAME);
        msXtaEntryOriginalUnitCode = (String) session.readField(SModConsts.ITMU_UNIT, new int[] { orderEntry.getFkOriginalUnitId() }, FIELD_CODE);
    }

    public void setPkDeliveryId(int n) { mnPkDeliveryId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setOriginalQuantity(double d) { mdOriginalQuantity = d; }
    public void setFkDpsYearId(int n) { mnFkDpsYearId = n; }
    public void setFkDpsDocId(int n) { mnFkDpsDocId = n; }
    public void setFkDpsEntryId(int n) { mnFkDpsEntryId = n; }
    public void setFkOrderYearId(int n) { mnFkOrderYearId = n; }
    public void setFkOrderDocId(int n) { mnFkOrderDocId = n; }
    public void setFkOrderEntryId(int n) { mnFkOrderEntryId = n; }

    public int getPkDeliveryId() { return mnPkDeliveryId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getQuantity() { return mdQuantity; }
    public double getOriginalQuantity() { return mdOriginalQuantity; }
    public int getFkDpsYearId() { return mnFkDpsYearId; }
    public int getFkDpsDocId() { return mnFkDpsDocId; }
    public int getFkDpsEntryId() { return mnFkDpsEntryId; }
    public int getFkOrderYearId() { return mnFkOrderYearId; }
    public int getFkOrderDocId() { return mnFkOrderDocId; }
    public int getFkOrderEntryId() { return mnFkOrderEntryId; }

    public void setXtaOrderNumberSeries(String s) { msXtaOrderNumberSeries = s; }
    public void setXtaOrderNumber(String s) { msXtaOrderNumber = s; }
    public void setXtaOrderNumberReference(String s) { msXtaOrderNumberReference = s; }
    public void setXtaOrderDate(Date t) { mtXtaOrderDate = t; }
    public void setXtaEntrySortingPosition(int n) { mnXtaEntrySortingPosition = n; }
    public void setXtaEntryItemCode(String s) { msXtaEntryItemCode = s; }
    public void setXtaEntryItemName(String s) { msXtaEntryItemName = s; }
    public void setXtaEntryOriginalUnitCode(String s) { msXtaEntryOriginalUnitCode = s; }

    public String getXtaOrderNumberSeries() { return msXtaOrderNumberSeries; }
    public String getXtaOrderNumber() { return msXtaOrderNumber; }
    public String getXtaOrderNumberReference() { return msXtaOrderNumberReference; }
    public Date getXtaOrderDate() { return mtXtaOrderDate; }
    public int getXtaEntrySortingPosition() { return mnXtaEntrySortingPosition; }
    public String getXtaEntryItemCode() { return msXtaEntryItemCode; }
    public String getXtaEntryItemName() { return msXtaEntryItemName; }
    public String getXtaEntryOriginalUnitCode() { return msXtaEntryOriginalUnitCode; }
    
    public int[] getKeyDpsEntry() { return new int[] { mnFkDpsYearId, mnFkDpsDocId, mnFkDpsEntryId }; }
    public int[] getKeyOrderEntry() { return new int[] { mnFkOrderYearId, mnFkOrderDocId, mnFkOrderEntryId }; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDeliveryId = pk[0];
        mnPkEntryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDeliveryId, mnPkEntryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkDeliveryId = 0;
        mnPkEntryId = 0;
        mdQuantity = 0;
        mdOriginalQuantity = 0;
        mnFkDpsYearId = 0;
        mnFkDpsDocId = 0;
        mnFkDpsEntryId = 0;
        mnFkOrderYearId = 0;
        mnFkOrderDocId = 0;
        mnFkOrderEntryId = 0;
        
        msXtaOrderNumberSeries = "";
        msXtaOrderNumber = "";
        msXtaOrderNumberReference = "";
        mtXtaOrderDate = null;
        mnXtaEntrySortingPosition = 0;
        msXtaEntryItemCode = "";
        msXtaEntryItemName = "";
        msXtaEntryOriginalUnitCode = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dvy = " + mnPkDeliveryId + " AND "
                + "id_ety = " + mnPkEntryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dvy = " + pk[0] + " AND "
                + "id_ety = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEntryId = 0;

        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " "
                + "WHERE id_dvy = " + mnPkDeliveryId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEntryId = resultSet.getInt(1);
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
            mnPkDeliveryId = resultSet.getInt("id_dvy");
            mnPkEntryId = resultSet.getInt("id_ety");
            mdQuantity = resultSet.getDouble("qty");
            mdOriginalQuantity = resultSet.getDouble("orig_qty");
            mnFkDpsYearId = resultSet.getInt("fk_dps_year");
            mnFkDpsDocId = resultSet.getInt("fk_dps_doc");
            mnFkDpsEntryId = resultSet.getInt("fk_dps_ety");
            mnFkOrderYearId = resultSet.getInt("fk_ord_year");
            mnFkOrderDocId = resultSet.getInt("fk_ord_doc");
            mnFkOrderEntryId = resultSet.getInt("fk_ord_ety");
            
            readXtaMembers(session);

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

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDeliveryId + ", " + 
                    mnPkEntryId + ", " + 
                    mdQuantity + ", " + 
                    mdOriginalQuantity + ", " + 
                    mnFkDpsYearId + ", " + 
                    mnFkDpsDocId + ", " + 
                    mnFkDpsEntryId + ", " + 
                    mnFkOrderYearId + ", " + 
                    mnFkOrderDocId + ", " + 
                    mnFkOrderEntryId + " " + 
                    ")";
        }
        else {
            throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbDeliveryEntry  clone() throws CloneNotSupportedException {
        SDbDeliveryEntry registry = new SDbDeliveryEntry();
        
        registry.setPkDeliveryId(this.getPkDeliveryId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setQuantity(this.getQuantity());
        registry.setOriginalQuantity(this.getOriginalQuantity());
        registry.setFkDpsYearId(this.getFkDpsYearId());
        registry.setFkDpsDocId(this.getFkDpsDocId());
        registry.setFkDpsEntryId(this.getFkDpsEntryId());
        registry.setFkOrderYearId(this.getFkOrderYearId());
        registry.setFkOrderDocId(this.getFkOrderDocId());
        registry.setFkOrderEntryId(this.getFkOrderEntryId());

        registry.setXtaOrderNumberSeries(this.getXtaOrderNumberSeries());
        registry.setXtaOrderNumber(this.getXtaOrderNumber());
        registry.setXtaOrderNumberReference(this.getXtaOrderNumberReference());
        registry.setXtaOrderDate(this.getXtaOrderDate());
        registry.setXtaEntrySortingPosition(this.getXtaEntrySortingPosition());
        registry.setXtaEntryOriginalUnitCode(this.getXtaEntryOriginalUnitCode());
    
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
                value = mnPkEntryId;
                break;
            case 1:
                value = msXtaEntryItemCode;
                break;
            case 2:
                value = msXtaEntryItemName;
                break;
            case 3:
                value = mdOriginalQuantity;
                break;
            case 4:
                value = msXtaEntryOriginalUnitCode;
                break;
            case 5:
                value = STrnUtils.formatDocNumber(msXtaOrderNumberSeries, msXtaOrderNumber);
                break;
            case 6:
                value = msXtaOrderNumberReference;
                break;
            case 7:
                value = mtXtaOrderDate;
                break;
            case 8:
                value = mnXtaEntrySortingPosition;
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void compute(final SGuiSession session) {
        readXtaMembers(session);
    }
    
    /**
     * Resets DPS supply effect (at delivery level) of current DPS and Order entries.
     */
    public void resetEntryEffect(final SGuiSession session) throws Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " SET "
                + "qty = (qty -("
                + "SELECT COALESCE(SUM(qty), 0.0) "
                + "FROM " + getSqlTable() + " "
                + "WHERE fk_dps_year = " + mnFkDpsYearId + " AND fk_dps_doc = " + mnFkDpsDocId + " AND fk_dps_ety = " + mnFkDpsEntryId + " AND "
                + "fk_ord_year = " + mnFkOrderYearId + " AND fk_ord_doc = " + mnFkOrderDocId + " AND fk_ord_ety = " + mnFkOrderEntryId + " AND "
                + "(id_dvy = " + mnPkDeliveryId + "))), "
                + "orig_qty = (orig_qty -("
                + "SELECT COALESCE(SUM(orig_qty), 0.0) "
                + "FROM " + getSqlTable() + " "
                + "WHERE fk_dps_year = " + mnFkDpsYearId + " AND fk_dps_doc = " + mnFkDpsDocId + " AND fk_dps_ety = " + mnFkDpsEntryId + " AND "
                + "fk_ord_year = " + mnFkOrderYearId + " AND fk_ord_doc = " + mnFkOrderDocId + " AND fk_ord_ety = " + mnFkOrderEntryId + " AND "
                + "(id_dvy = " + mnPkDeliveryId + "))) "
                + "WHERE id_src_year = " + mnFkOrderYearId + " AND id_src_doc = " + mnFkOrderDocId + " AND id_src_ety = " + mnFkOrderEntryId + " AND "
                + "id_des_year = " + mnFkDpsYearId + " AND id_des_doc = " + mnFkDpsDocId + " AND id_des_ety =  " + mnFkDpsEntryId + "; ";
        session.getStatement().execute(msSql);
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
    
    /**
     * Applies DPS supply effect (at delivery entry level) of current DPS and Order entries.
     */
    public void applyEntryEffect(final SGuiSession session) throws Exception {
        boolean insert = false;
        ResultSet resultSet = null;
        
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        msSql = "SELECT COUNT(*) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " "
                + "WHERE id_src_year = " + mnFkOrderYearId + " AND id_src_doc = " + mnFkOrderDocId + " AND id_src_ety = " + mnFkOrderEntryId + " AND "
                + "id_des_year = " + mnFkDpsYearId + " AND id_des_doc = " + mnFkDpsDocId + " AND id_des_ety =  " + mnFkDpsEntryId + "; ";

        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            insert = resultSet.getInt(1) == 0;
        }
        
        if (insert) {
            msSql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " VALUES ("
                    + mnFkOrderYearId + ", " + mnFkOrderDocId + ", " + mnFkOrderEntryId + ", "
                    + mnFkDpsYearId + ", " + mnFkDpsDocId + ", " + mnFkDpsEntryId + ", "
                    + mdQuantity + ", " + mdOriginalQuantity                
                    + "); ";
        }
        else {
            msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " SET "
                    + "qty = qty + " + mdQuantity + ", orig_qty = orig_qty + " + mdOriginalQuantity + " "
                    + "WHERE id_src_year = " + mnFkOrderYearId + " AND id_src_doc = " + mnFkOrderDocId + " AND id_src_ety = " + mnFkOrderEntryId + " AND "
                    + "id_des_year = " + mnFkDpsYearId + " AND id_des_doc = " + mnFkDpsDocId + " AND id_des_ety =  " + mnFkDpsEntryId + "; ";
        }
        
        session.getStatement().execute(msSql);
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
    
    /**
     * Disposes DPS supply effect (at delivery entry level) of current DPS and Order entries if needed.
     */
    public void disposeEntryEffect(final SGuiSession session) throws Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        statement = session.getStatement().getConnection().createStatement();
        
        msSql = "SELECT COALESCE(SUM(qty), 0.0), COALESCE(SUM(orig_qty), 0.0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " "
                + "WHERE id_src_year = " + mnFkOrderYearId + " AND id_src_doc = " + mnFkOrderDocId + " AND id_src_ety = " + mnFkOrderEntryId + " AND "
                + "id_des_year = " + mnFkDpsYearId + " AND id_des_doc = " + mnFkDpsDocId + " AND id_des_ety =  " + mnFkDpsEntryId + "; ";

        resultSet = statement.executeQuery(msSql);
        if (resultSet.next()) {
            if (resultSet.getDouble(1) == 0 && resultSet.getDouble(2) == 0) {
                msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_SUPPLY) + " "
                        + "WHERE id_src_year = " + mnFkOrderYearId + " AND id_src_doc = " + mnFkOrderDocId + " AND id_src_ety = " + mnFkOrderEntryId + " AND "
                        + "id_des_year = " + mnFkDpsYearId + " AND id_des_doc = " + mnFkDpsDocId + " AND id_des_ety =  " + mnFkDpsEntryId + "; ";
                session.getStatement().execute(msSql);
            }
        }
        
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
}
