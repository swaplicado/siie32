/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBolMerchandise extends SDbRegistryUser implements Serializable {

    protected int mnPkBillOfLadingId;
    protected int mnPkMerchandiseId;
    protected double mdQuantity;
    protected boolean mbHazardousMaterial;
    protected String msHazardousMaterialKey;
    protected String msPackagingKey;
    protected String msPackagingDescription;
    protected double mdWeight;
    protected String msExternalUUID;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    
    protected ArrayList<SDbBolMerchandiseQuantity> maChildBolMerchandiseQuantities;
    
    protected SDataItem moDataItem;
    protected SDataUnit moDataUnit;

    protected String msXtaItemClaveProdServ;
    protected String msXtaClaveUnidad;
    
    public SDbBolMerchandise() {
        super(SModConsts.LOG_BOL_MERCH);
    }
    
    public void addMerchandiseQuantity (SDbBolMerchandiseQuantity o) {
        boolean found = false;
        for (SDbBolMerchandiseQuantity merchQty : maChildBolMerchandiseQuantities) {
            if (merchQty.getFkOriginBizPartnerAddress_n() == o.getFkOriginBizPartnerAddress_n() &&
                    merchQty.getFkOriginAddressAddress_n() == o.getFkOriginAddressAddress_n() &&
                    merchQty.getFkDestinationBizPartnerAddress_n() == o.getFkDestinationBizPartnerAddress_n() &&
                    merchQty.getFkDestinationAddressAddress_n() == o.getFkDestinationAddressAddress_n()) {
                found = true;
                merchQty.setQuantity(o.getQuantity());
            }
        }
        if (!found) {
            maChildBolMerchandiseQuantities.add(o);
        }
    }
    
    public void removeMerchandiseQuantity(SDbBolMerchandiseQuantity o) {
        for (SDbBolMerchandiseQuantity merchQty : maChildBolMerchandiseQuantities) {
            if (merchQty.getFkOriginBizPartnerAddress_n() == o.getFkOriginBizPartnerAddress_n() &&
                    merchQty.getFkOriginAddressAddress_n() == o.getFkOriginAddressAddress_n() &&
                    merchQty.getFkDestinationBizPartnerAddress_n() == o.getFkDestinationBizPartnerAddress_n() &&
                    merchQty.getFkDestinationAddressAddress_n() == o.getFkDestinationAddressAddress_n()) {
                maChildBolMerchandiseQuantities.remove(merchQty);
                break;
            }
        }
    }
    
    public void updateTotalItemQuantity() {
        double qty = 0;
        for (SDbBolMerchandiseQuantity merchQty : maChildBolMerchandiseQuantities) {
            if (merchQty.getFkOriginBizPartnerAddress_n() != 0 && merchQty.getFkOriginAddressAddress_n() != 0) {
                qty += merchQty.getQuantity();
            }
        }
        mdQuantity = qty;
    }
    
    public ArrayList<SDbBolMerchandiseQuantity> getCharge(int[] locationKey) {
        ArrayList<SDbBolMerchandiseQuantity> charge = new ArrayList<>();
        for (SDbBolMerchandiseQuantity qty : maChildBolMerchandiseQuantities) {
            if (qty.getFkOriginBizPartnerAddress_n() == locationKey[0] && qty.getFkOriginAddressAddress_n() == locationKey[1]) {
                charge.add(qty);
            }
        }
        return charge;
    }
    
    public ArrayList<SDbBolMerchandiseQuantity> getDischarge(int[] locationKey) {
        ArrayList<SDbBolMerchandiseQuantity> discharge = new ArrayList<>();
        for (SDbBolMerchandiseQuantity qty : maChildBolMerchandiseQuantities) {
            if (qty.getFkDestinationBizPartnerAddress_n() == locationKey[0] && qty.getFkDestinationAddressAddress_n()== locationKey[1]) {
                discharge.add(qty);
            }
        }
        return discharge;
    }
    
    public double getDiferenceWeightChargedDischarged() {
        double charged = 0;
        double discharged = 0;
        for (SDbBolMerchandiseQuantity merchQty : maChildBolMerchandiseQuantities) {
            if (merchQty.getFkOriginBizPartnerAddress_n() != 0 && merchQty.getFkOriginAddressAddress_n() != 0) {
                charged += merchQty.getQuantity();
            }
            else if (merchQty.getFkDestinationBizPartnerAddress_n()!= 0 && merchQty.getFkDestinationAddressAddress_n()!= 0) {
                discharged += merchQty.getQuantity();
            }
        }
        return charged - discharged;
    }
    
    public void updateSatCodes(SGuiSession session) {
        try {
            String sql = "SELECT icfd.code AS item_code, igencfd.code AS igen_code FROM erp.itmu_item AS i " +
                "INNER JOIN erp.itmu_igen AS igen ON igen.id_igen = i.fid_igen " +
                "INNER JOIN erp.itms_cfd_prod_serv AS igencfd ON igencfd.id_cfd_prod_serv = igen.fid_cfd_prod_serv " +
                "LEFT OUTER JOIN erp.itms_cfd_prod_serv AS icfd ON icfd.id_cfd_prod_serv = i.fid_cfd_prod_serv_n " +
                "WHERE i.id_item = " + mnFkItemId + ";";
            ResultSet resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                msXtaItemClaveProdServ = resultSet.getString("item_code") == null ? resultSet.getString("igen_code") : resultSet.getString("item_code");
            }
            
            sql = "SELECT ucfd.code FROM erp.itmu_unit AS u " +
                "INNER JOIN erp.itms_cfd_unit AS ucfd ON ucfd.id_cfd_unit = u.fid_cfd_unit " +
                "WHERE u.id_unit = " + mnFkUnitId + ";";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                msXtaClaveUnidad = resultSet.getString(1);
            }
        }
        catch (Exception e) {}
    }
    
    public void readXta(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT m.*, icfd.code AS item_code, igencfd.code AS igen_code, ucfd.code AS unit_code " +
                "FROM " + getSqlTable() + " AS m " +
                "INNER JOIN erp.itmu_item AS i ON i.id_item = m.fk_item " +
                "INNER JOIN erp.itmu_igen AS igen ON igen.id_igen = i.fid_igen " +
                "INNER JOIN erp.itms_cfd_prod_serv AS igencfd ON igencfd.id_cfd_prod_serv = igen.fid_cfd_prod_serv " +
                "INNER JOIN erp.itmu_unit AS u ON u.id_unit = m.fk_unit " +
                "INNER JOIN erp.itms_cfd_unit AS ucfd ON ucfd.id_cfd_unit = u.fid_cfd_unit " +
                "LEFT OUTER JOIN erp.itms_cfd_prod_serv AS icfd ON icfd.id_cfd_prod_serv = i.fid_cfd_prod_serv_n " +
                "WHERE id_bol = " + pk[0] + " AND id_merch = " + pk[1];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBillOfLadingId = resultSet.getInt("id_bol");
            mnPkMerchandiseId = resultSet.getInt("id_merch");
            mdQuantity = resultSet.getDouble("qty");
            mbHazardousMaterial = resultSet.getBoolean("hazardous_mat");
            msHazardousMaterialKey = resultSet.getString("hazardous_mat_key");
            msPackagingKey = resultSet.getString("packaging_key");
            msPackagingDescription = resultSet.getString("packaging_descrip");
            mdWeight = resultSet.getDouble("weight");
            msExternalUUID = resultSet.getString("ext_uuid");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            
            msXtaItemClaveProdServ = resultSet.getString("item_code") == null ? resultSet.getString("igen_code") : resultSet.getString("item_code");
            msXtaClaveUnidad = resultSet.getString("unit_code");

            mbRegistryNew = false;
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.READ_OK;
    }
    
    public void setPkBillOfLadingId(int n) { mnPkBillOfLadingId = n; }
    public void setPkMerchandiseId(int n) { mnPkMerchandiseId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setHazardousMaterial(boolean b) { mbHazardousMaterial = b; }
    public void setHazardousMaterialKey(String s) { msHazardousMaterialKey = s; }
    public void setPackagingKey(String s) { msPackagingKey = s; }
    public void setPackagingDescription(String s) { msPackagingDescription = s; }
    public void setWeight(double d) { mdWeight = d; }
    public void setExternalUUID(String s) { msExternalUUID = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    
    public int getPkBillOfLadingId() { return mnPkBillOfLadingId; }
    public int getPkMerchandiseId() { return mnPkMerchandiseId; }
    public double getQuantity() { return mdQuantity; }
    public boolean isHazardousMaterial() { return mbHazardousMaterial; }
    public String getHazardousMaterialKey() { return msHazardousMaterialKey; }
    public String getPackagingKey() { return msPackagingKey; }
    public String getPackagingDescription() { return msPackagingDescription; }
    public double getWeight() { return mdWeight; }
    public String getExternalUUID() { return msExternalUUID; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    
    public ArrayList<SDbBolMerchandiseQuantity> getChildBolMerchandiseQuantities() { return maChildBolMerchandiseQuantities; }
    
    public void setDataItem(SDataItem o) { moDataItem = o; }
    public void setDataUnit(SDataUnit o) { moDataUnit = o; }
    
    public SDataItem getDataItem() { return moDataItem; }
    public SDataUnit getDataUnit() { return moDataUnit; }
    
    public void setXtaItemClaveProdServ(String s) { msXtaItemClaveProdServ = s; }
    public void setXtaClaveUnidad(String s) { msXtaClaveUnidad = s; }
    
    public String getXtaItemClaveProdServ() { return msXtaItemClaveProdServ; }
    public String getXtaClaveUnidad() { return msXtaClaveUnidad; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBillOfLadingId = pk[0];
        mnPkMerchandiseId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBillOfLadingId, mnPkMerchandiseId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBillOfLadingId = 0;
        mnPkMerchandiseId = 0;
        mdQuantity = 0;
        mbHazardousMaterial = false;
        msHazardousMaterialKey = "";
        msPackagingKey = "";
        msPackagingDescription = "";
        mdWeight = 0;
        msExternalUUID = "";
        mbDeleted = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        
        maChildBolMerchandiseQuantities = new ArrayList<>();
        
        moDataItem = null;
        moDataUnit = null;
        
        msXtaItemClaveProdServ = "";
        msXtaClaveUnidad = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBillOfLadingId + " AND id_merch = " + mnPkMerchandiseId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " AND id_merch = " + pk[1];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkMerchandiseId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_merch), 0) + 1 FROM " + getSqlTable() + " WHERE id_bol = " + mnPkBillOfLadingId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMerchandiseId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT m.*, icfd.code AS item_code, igencfd.code AS igen_code, ucfd.code AS unit_code " +
                "FROM " + getSqlTable() + " AS m " +
                "INNER JOIN erp.itmu_item AS i ON i.id_item = m.fk_item " +
                "INNER JOIN erp.itmu_igen AS igen ON igen.id_igen = i.fid_igen " +
                "INNER JOIN erp.itms_cfd_prod_serv AS igencfd ON igencfd.id_cfd_prod_serv = igen.fid_cfd_prod_serv " +
                "INNER JOIN erp.itmu_unit AS u ON u.id_unit = m.fk_unit " +
                "INNER JOIN erp.itms_cfd_unit AS ucfd ON ucfd.id_cfd_unit = u.fid_cfd_unit " +
                "LEFT OUTER JOIN erp.itms_cfd_prod_serv AS icfd ON icfd.id_cfd_prod_serv = i.fid_cfd_prod_serv_n " +
                "WHERE id_bol = " + pk[0] + " AND id_merch = " + pk[1];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBillOfLadingId = resultSet.getInt("id_bol");
            mnPkMerchandiseId = resultSet.getInt("id_merch");
            mdQuantity = resultSet.getDouble("qty");
            mbHazardousMaterial = resultSet.getBoolean("hazardous_mat");
            msHazardousMaterialKey = resultSet.getString("hazardous_mat_key");
            msPackagingKey = resultSet.getString("packaging_key");
            msPackagingDescription = resultSet.getString("packaging_descrip");
            mdWeight = resultSet.getDouble("weight");
            msExternalUUID = resultSet.getString("ext_uuid");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            
            msXtaItemClaveProdServ = resultSet.getString("item_code") == null ? resultSet.getString("igen_code") : resultSet.getString("item_code");
            msXtaClaveUnidad = resultSet.getString("unit_code");

            mbRegistryNew = false;
        }
        
        // Read merchandise quantity
        
        msSql = "SELECT id_merch_qty "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_MERCH_QTY) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " AND id_merch = " + mnPkMerchandiseId + " "
                + "ORDER BY id_merch_qty ";
        resultSet = session.getDatabase().getConnection().createStatement().executeQuery(msSql);
        while (resultSet.next()) {
            SDbBolMerchandiseQuantity merchQty = new SDbBolMerchandiseQuantity();
            merchQty.read(session, new int[] { mnPkBillOfLadingId, mnPkMerchandiseId, resultSet.getInt("id_merch_qty") } );
            maChildBolMerchandiseQuantities.add(merchQty);
        }
        
        // Read item
        
        moDataItem = new SDataItem();
        moDataItem.read(new int[] { mnFkItemId }, session.getStatement());
        
        // Read unit
        
        moDataUnit = new SDataUnit();
        moDataUnit.read(new int[] { mnFkUnitId }, session.getStatement());
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        Statement statement;
        
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
                mdQuantity + ", " + 
                (mbHazardousMaterial ? 1 : 0) + ", " + 
                "'" + msHazardousMaterialKey + "', " + 
                "'" + msPackagingKey + "', " + 
                "'" + msPackagingDescription + "', " + 
                mdWeight + ", " + 
                "'" + msExternalUUID + "', " + 
                (mbDeleted ? 1 : 0) + ", " + 
                mnFkItemId + ", " + 
                mnFkUnitId + " " + 
                ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                //"id_bol = " + mnPkBillOfLadingId + ", " +
                //"id_merch = " + mnPkMerchandiseId + ", " +
                "qty = " + mdQuantity + ", " +
                "hazardous_mat = " + (mbHazardousMaterial ? 1 : 0) + ", " +
                "hazardous_mat_key = '" + msHazardousMaterialKey + "', " +
                "packaging_key = '" + msPackagingKey + "', " +
                "packaging_descrip = '" + msPackagingDescription + "', " +
                "weight = " + mdWeight + ", " +
                "ext_uuid = '" + msExternalUUID + "', " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_item = " + mnFkItemId + ", " +
                "fk_unit = " + mnFkUnitId + " " +
                getSqlWhere();
        }
        
        statement = session.getDatabase().getConnection().createStatement();
        statement.execute(msSql);
        
        // Save merchandise quantity
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.LOG_BOL_MERCH_QTY) + " "
                + "WHERE id_bol = " + mnPkBillOfLadingId + " AND id_merch = " + mnPkMerchandiseId + " ";
        statement.execute(msSql);
        
        for (SDbBolMerchandiseQuantity merchQty : maChildBolMerchandiseQuantities) {
            merchQty.setPkBillOfLadingId(mnPkBillOfLadingId);
            merchQty.setPkMerchandiseId(mnPkMerchandiseId);
            merchQty.setRegistryNew(true);
            merchQty.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBolMerchandise clone() throws CloneNotSupportedException {
        SDbBolMerchandise registry = new SDbBolMerchandise();
        
        registry.setPkBillOfLadingId(this.getPkBillOfLadingId());
        registry.setPkMerchandiseId(this.getPkMerchandiseId());
        registry.setQuantity(this.getQuantity());
        registry.setHazardousMaterial(this.isHazardousMaterial());
        registry.setHazardousMaterialKey(this.getHazardousMaterialKey());
        registry.setPackagingKey(this.getPackagingKey());
        registry.setPackagingDescription(this.getPackagingDescription());
        registry.setWeight(this.getWeight());
        registry.setExternalUUID(this.getExternalUUID());
        registry.setDeleted(this.isDeleted());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        
        for (SDbBolMerchandiseQuantity mq : this.getChildBolMerchandiseQuantities()) {
            registry.getChildBolMerchandiseQuantities().add(mq.clone());
        }
        
        registry.setDataItem(this.getDataItem()); // el clon comparte este registro que es de sólo lectura
        registry.setDataUnit(this.getDataUnit()); // el clon comparte este registro que es de sólo lectura
        
        registry.setXtaItemClaveProdServ(this.getXtaItemClaveProdServ());
        registry.setXtaClaveUnidad(this.getXtaClaveUnidad());

        return registry;
    }
}
