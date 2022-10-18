/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBolMerchandiseQuantity extends SDbRegistryUser implements SGridRow, Serializable {

    protected int mnPkBillOfLadingId;
    protected int mnPkMerchandiseId;
    protected int mnPkMerchandiseQuantityId;
    protected double mdQuantity;
    protected int mnFkOriginBizPartnerAddress_n;
    protected int mnFkOriginAddressAddress_n;
    protected int mnFkDestinationBizPartnerAddress_n;
    protected int mnFkDestinationAddressAddress_n;
    
    protected SDbBolMerchandise moParentMerchandise;
    protected SDataBizPartnerBranchAddress moDataOriginBizPartnerBranchAddress;
    protected SDataBizPartnerBranchAddress moDataDestinationBizPartnerBranchAddress;
    
    protected String msXtaItemName;
    protected String msXtaUnitName;
    
    public SDbBolMerchandiseQuantity() {
        super(SModConsts.LOG_BOL_MERCH_QTY);
    }
    
    public void setPkBillOfLadingId(int n) { mnPkBillOfLadingId = n; }
    public void setPkMerchandiseId(int n) { mnPkMerchandiseId = n; }
    public void setPkMerchandiseQuantityId(int n) { mnPkMerchandiseQuantityId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setFkOriginBizPartnerAddress_n(int n) { mnFkOriginBizPartnerAddress_n = n; }
    public void setFkOriginAddressAddress_n(int n) { mnFkOriginAddressAddress_n = n; }
    public void setFkDestinationBizPartnerAddress_n(int n) { mnFkDestinationBizPartnerAddress_n = n; }
    public void setFkDestinationAddressAddress_n(int n) { mnFkDestinationAddressAddress_n = n; }
    
    public int getPkBillOfLadingId() { return mnPkBillOfLadingId; }
    public int getPkMerchandiseId() { return mnPkMerchandiseId; }
    public int getPkMerchandiseQuantityId() { return mnPkMerchandiseQuantityId; }
    public double getQuantity() { return mdQuantity; }
    public int getFkOriginBizPartnerAddress_n() { return mnFkOriginBizPartnerAddress_n; }
    public int getFkOriginAddressAddress_n() { return mnFkOriginAddressAddress_n; }
    public int getFkDestinationBizPartnerAddress_n() { return mnFkDestinationBizPartnerAddress_n; }
    public int getFkDestinationAddressAddress_n() { return mnFkDestinationAddressAddress_n; }
    
    public void setParentMerchandise(SDbBolMerchandise o) { moParentMerchandise = o; }
    public void setDataOriginBizPartnerBranchAddress(SDataBizPartnerBranchAddress o) { moDataOriginBizPartnerBranchAddress = o; }
    public void setDataDestinationBizPartnerBranchAddress(SDataBizPartnerBranchAddress o) { moDataDestinationBizPartnerBranchAddress = o; }
    
    public SDbBolMerchandise getParentMerchandise() { return moParentMerchandise; } 
    public SDataBizPartnerBranchAddress getDataOriginBizPartnerBranchAddress() { return moDataOriginBizPartnerBranchAddress; }
    public SDataBizPartnerBranchAddress getDataDestinationBizPartnerBranchAddress() { return moDataDestinationBizPartnerBranchAddress; }
    
    public void setXtaItemName(String s) { msXtaItemName = s; } 
    public void setXtaUnitName(String s) { msXtaUnitName = s; } 
    
    public String getXtaItemName() { return msXtaItemName; }
    public String getXtaUnitName() { return msXtaUnitName; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBillOfLadingId = pk[0];
        mnPkMerchandiseId = pk[1];
        mnPkMerchandiseQuantityId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBillOfLadingId, mnPkMerchandiseId, mnPkMerchandiseQuantityId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBillOfLadingId = 0;
        mnPkMerchandiseId = 0;
        mnPkMerchandiseQuantityId = 0;
        mdQuantity = 0;
        mnFkOriginBizPartnerAddress_n = 0;
        mnFkOriginAddressAddress_n = 0;
        mnFkDestinationBizPartnerAddress_n = 0;
        mnFkDestinationAddressAddress_n = 0;
        
        moParentMerchandise = null;
        moDataOriginBizPartnerBranchAddress = null;
        moDataDestinationBizPartnerBranchAddress = null;
        
        msXtaItemName = "";
        msXtaUnitName = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBillOfLadingId + " AND id_merch = " + mnPkMerchandiseId + " AND id_merch_qty = " + mnPkMerchandiseQuantityId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " AND id_merch = " + pk[1] + " AND id_merch_qty " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkMerchandiseQuantityId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_merch_qty), 0) + 1 FROM " + getSqlTable() + " WHERE id_bol = " + mnPkBillOfLadingId + " AND id_merch = " + mnPkMerchandiseId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMerchandiseQuantityId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statement = session.getDatabase().getConnection().createStatement();
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT mq.*, i.name AS item, u.unit FROM " + getSqlTable() + " AS mq " +
                "INNER JOIN log_bol_merch AS m ON mq.id_bol = m.id_bol AND mq.id_merch = m.id_merch " +
                "INNER JOIN erp.itmu_item AS i ON m.fk_item = i.id_item " + 
                "INNER JOIN erp.itmu_unit AS u ON m.fk_unit = u.id_unit " + 
                "WHERE mq.id_bol = " + pk[0] + " " +
                "AND mq.id_merch = " + pk[1] + " AND mq.id_merch_qty = " + pk[2];
        resultSet = statement.executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBillOfLadingId = resultSet.getInt("id_bol");
            mnPkMerchandiseId = resultSet.getInt("id_merch");
            mnPkMerchandiseQuantityId = resultSet.getInt("id_merch_qty");
            mdQuantity = resultSet.getDouble("qty");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkOriginBizPartnerAddress_n = resultSet.getInt("fk_orig_bpb_add_n");
            mnFkOriginAddressAddress_n = resultSet.getInt("fk_orig_add_add_n");
            mnFkDestinationBizPartnerAddress_n = resultSet.getInt("fk_dest_bpb_add_n");
            mnFkDestinationAddressAddress_n = resultSet.getInt("fk_dest_add_add_n");
            
            msXtaItemName = resultSet.getString("item");
            msXtaUnitName = resultSet.getString("unit");
            
            // Read merchandise
            
            moParentMerchandise = new SDbBolMerchandise();
            moParentMerchandise.readXta(session, new int[] { mnPkBillOfLadingId, mnPkMerchandiseId });
            
            // Read BizPartnerBranchAddresses
            
            if (mnFkOriginBizPartnerAddress_n != 0 && mnFkOriginAddressAddress_n != 0) {
                moDataOriginBizPartnerBranchAddress = new SDataBizPartnerBranchAddress();
                moDataOriginBizPartnerBranchAddress.read(new int[] { mnFkOriginBizPartnerAddress_n, mnFkOriginAddressAddress_n }, statement);
            }
            
            if (mnFkDestinationBizPartnerAddress_n != 0 && mnFkDestinationAddressAddress_n != 0) {
                moDataDestinationBizPartnerBranchAddress = new SDataBizPartnerBranchAddress();
                moDataDestinationBizPartnerBranchAddress.read(new int[] { mnFkDestinationBizPartnerAddress_n, mnFkDestinationAddressAddress_n }, statement);
            }
            
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                mnPkBillOfLadingId + ", " + 
                mnPkMerchandiseId + ", " + 
                mnPkMerchandiseQuantityId + ", " + 
                mdQuantity + ", " + 
                (mbDeleted ? 1 : 0) + ", " + 
                (mnFkOriginBizPartnerAddress_n == 0 ? "NULL, " : mnFkOriginBizPartnerAddress_n + ", ") + 
                (mnFkOriginAddressAddress_n == 0 ? "NULL, " : mnFkOriginAddressAddress_n + ", ") + 
                (mnFkDestinationBizPartnerAddress_n == 0 ? "NULL, " : mnFkDestinationBizPartnerAddress_n + ", ") + 
                (mnFkDestinationAddressAddress_n == 0 ? "NULL " : mnFkDestinationAddressAddress_n + " ") + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                "id_bol = " + mnPkBillOfLadingId + ", " +
                "id_merch = " + mnPkMerchandiseId + ", " +
                "id_merch_qty = " + mnPkMerchandiseQuantityId + ", " +
                "qty = " + mdQuantity + ", " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_orig_bpb_add_n = " + (mnFkOriginBizPartnerAddress_n == 0 ? "NULL, " : mnFkOriginBizPartnerAddress_n + ", ") +
                "fk_orig_add_add_n = " + (mnFkOriginAddressAddress_n == 0 ? "NULL, " : mnFkOriginAddressAddress_n + ", ") +
                "fk_dest_bpb_add_n = " + (mnFkDestinationBizPartnerAddress_n == 0 ? "NULL, " : mnFkDestinationBizPartnerAddress_n + ", ") +
                "fk_dest_add_add_n = " + (mnFkDestinationAddressAddress_n == 0 ? "NULL " : mnFkDestinationAddressAddress_n + " ") +
                getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBolMerchandiseQuantity clone() throws CloneNotSupportedException {
        SDbBolMerchandiseQuantity registry = new SDbBolMerchandiseQuantity();
        
        registry.setPkBillOfLadingId(this.getPkBillOfLadingId());
        registry.setPkMerchandiseId(this.getPkMerchandiseId());
        registry.setPkMerchandiseQuantityId(this.getPkMerchandiseQuantityId());
        registry.setQuantity(this.getQuantity());
        registry.setDeleted(this.isDeleted());
        registry.setFkOriginBizPartnerAddress_n(this.getFkOriginBizPartnerAddress_n());
        registry.setFkOriginAddressAddress_n(this.getFkOriginAddressAddress_n());
        registry.setFkDestinationBizPartnerAddress_n(this.getFkDestinationBizPartnerAddress_n());
        registry.setFkDestinationAddressAddress_n(this.getFkDestinationAddressAddress_n());
        
        registry.setParentMerchandise(this.getParentMerchandise()); // el clon comparte este registro que es inmutable
        registry.setDataOriginBizPartnerBranchAddress(this.getDataOriginBizPartnerBranchAddress()); // el clon comparte este registro que es de sólo lectura
        registry.setDataDestinationBizPartnerBranchAddress(this.getDataDestinationBizPartnerBranchAddress()); // el clon comparte este registro que es de sólo lectura
        
        registry.setXtaItemName(this.getXtaItemName());
        registry.setXtaUnitName(this.getXtaUnitName());

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
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return isRowEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRowEdited(edited);
    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;
        
        switch (row) {
            case 0:
                value = msXtaItemName;
                break;
            case 1:
                value = mdQuantity;
                break;
            case 2:
                value = msXtaUnitName;
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object o, int row) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
