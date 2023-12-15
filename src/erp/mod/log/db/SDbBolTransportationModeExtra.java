/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import cfd.ver40.DCfdi40Catalogs;
import erp.cfd.SCfdXmlCatalogs;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBolTransportationModeExtra extends SDbRegistryUser implements SGridRow, Serializable {

    protected int mnPkBillOfLadingId;
    protected int mnPkTransportationModeId;
    protected int mnPkExtraId;
    protected String msTransportationPart;
    protected int mnFkBillOfLadingPersonId;
    
    protected SDbBolPerson moBolPerson; 
    
    protected String msXtaTransportationPartDescription;
    
    public SDbBolTransportationModeExtra() {
        super(SModConsts.LOG_BOL_TRANSP_MODE_EXTRA);
    }

    public void setPkBillOfLadingId(int n) { mnPkBillOfLadingId = n; }
    public void setPkTransportationModeId(int n) { mnPkTransportationModeId = n; }
    public void setPkExtraId(int n) { mnPkExtraId = n; }
    public void setTransportationPart(String s) { msTransportationPart = s; }
    public void setFkBillOfLadingPersonId(int n) { mnFkBillOfLadingPersonId = n; }

    public int getPkBillOfLadingId() { return mnPkBillOfLadingId; }
    public int getPkTransportationModeId() { return mnPkTransportationModeId; }
    public int getPkExtraId() { return mnPkExtraId; }
    public String getTransportationPart() { return msTransportationPart; }
    public int getFkBillOfLadingPersonId() { return mnFkBillOfLadingPersonId; }
    
    public void setBolPerson(SDbBolPerson o) { moBolPerson = o; }
    
    public SDbBolPerson getBolPerson() { return moBolPerson; }
    
    public void setXtaTransportationPartDescription(String s) { msXtaTransportationPartDescription = s; }
    
    public String getXtaTransportationPartDescription() { return msXtaTransportationPartDescription; }
    
    public void readBolPerson(SGuiSession session) throws Exception {
        moBolPerson = new SDbBolPerson();
        moBolPerson.read(session, new int[] { mnFkBillOfLadingPersonId });
    }
    
    public void readXtaTransportationPartDescription(SGuiSession session) {
        SCfdXmlCatalogs catalogs = ((SSessionCustom) session.getSessionCustom()).getCfdXmlCatalogs();
        msXtaTransportationPartDescription = catalogs.composeEntryDescription(SDataConstantsSys.TRNS_CFD_CAT_BOL_TRANSP_PART, msTransportationPart);
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBillOfLadingId = pk[0];
        mnPkTransportationModeId = pk[1];
        mnPkExtraId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBillOfLadingId, mnPkTransportationModeId, mnPkExtraId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBillOfLadingId = 0;
        mnPkTransportationModeId = 0;
        mnPkExtraId = 0;
        msTransportationPart = "";
        mnFkBillOfLadingPersonId = 0;
        
        moBolPerson = null;
        msXtaTransportationPartDescription = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBillOfLadingId + " AND id_transp_mode = " + mnPkTransportationModeId + " AND id_extra = " + mnPkExtraId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " AND  id_transp_mode = " + pk[1] + " AND id_extra = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkExtraId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_extra), 0) + 1 FROM " + getSqlTable() + " WHERE id_bol = " + mnPkBillOfLadingId + " AND id_transp_mode = " + mnPkTransportationModeId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkExtraId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * FROM " + getSqlTable() + " WHERE id_bol = " + pk[0] + 
                " AND id_transp_mode = " + pk[1] + 
                " AND id_extra = " + pk[2];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBillOfLadingId = resultSet.getInt("id_bol");
            mnPkTransportationModeId = resultSet.getInt("id_transp_mode");
            mnPkExtraId = resultSet.getInt("id_extra");
            msTransportationPart = resultSet.getString("transp_part");
            mnFkBillOfLadingPersonId = resultSet.getInt("fk_bol_person");

            mbRegistryNew = false;
        }
        
        // Read extra
        readBolPerson(session);
        readXtaTransportationPartDescription(session);
        
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
                mnPkTransportationModeId + ", " + 
                mnPkExtraId + ", " + 
                "'" + msTransportationPart + "', " + 
                mnFkBillOfLadingPersonId + " " + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                //"id_bol = " + mnPkBillOfLadingId + ", " +
                //"id_transp_mode = " + mnPkTransportationModeId + ", " +
                //"id_extra = " + mnPkExtraId + ", " +
                "transp_part = '" + msTransportationPart + "', " +
                "fk_bol_person = " + mnFkBillOfLadingPersonId + " " +
                getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBolTransportationModeExtra clone() throws CloneNotSupportedException {
        SDbBolTransportationModeExtra registry = new SDbBolTransportationModeExtra();
        
        registry.setPkBillOfLadingId(this.getPkBillOfLadingId());
        registry.setPkTransportationModeId(this.getPkTransportationModeId());
        registry.setPkExtraId(this.getPkExtraId());
        registry.setTransportationPart(this.getTransportationPart());
        registry.setFkBillOfLadingPersonId(this.getFkBillOfLadingPersonId());
        
        registry.setBolPerson(this.getBolPerson()); // el clon comparte este registro que es de sólo lectura
        
        registry.setXtaTransportationPartDescription(this.getXtaTransportationPartDescription());

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
                value = DCfdi40Catalogs.FiguraTransporte.get(moBolPerson.getDbmsBolPersonTypeCode());
                break;
            case 1: 
                value = moBolPerson.getName();
                break;
            case 2: 
                value = moBolPerson.getFiscalId();
                break;
            case 3: 
                value = moBolPerson.getFiscalForeginId();
                break;
            case 4: 
                value = moBolPerson.getDriverLicense();        
                break;
            case 5:
                value = msXtaTransportationPartDescription;
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
