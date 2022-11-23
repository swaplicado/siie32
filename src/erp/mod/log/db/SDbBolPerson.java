/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.log.db;

import erp.mloc.data.SDataCountry;
import erp.mloc.data.SDataState;
import erp.mod.SModConsts;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Isabel Servín
 */
public class SDbBolPerson extends SDbRegistryUser implements Serializable {

    protected int mnPkBolPersonId;
    protected String msName;
    protected String msFiscalId;
    protected String msFiscalForeginId;
    protected String msDriverLicense;
    protected String msTelephone;
    protected String msStreet;
    protected String msStreetNumberExt;
    protected String msStreetNumberInt;
    protected String msNeighborhood;
    protected String msReference;
    protected String msLocality;
    protected String msState;
    protected String msZipCode;
    //protected boolean mbDeleted;
    protected int mnFkBolPersonTypeId;
    protected int mnFkCountryId_n;
    protected int mnFkStateId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msDbmsBolPersonTypeCode;
    
    protected erp.mloc.data.SDataState moDataState;
    protected erp.mloc.data.SDataCountry moDataCountry;
    
    public SDbBolPerson() {
        super(SModConsts.LOG_BOL_PERSON);
    }
    
    public void setPkBolPersonId(int n) { mnPkBolPersonId = n; }
    public void setName(String s) { msName = s; }
    public void setFiscalId(String s) { msFiscalId = s; }
    public void setFiscalForeginId(String s) { msFiscalForeginId = s; }
    public void setDriverLicense(String s) { msDriverLicense = s; }
    public void setTelephone(String s) { msTelephone = s; }
    public void setStreet(String s) { msStreet = s; }
    public void setStreetNumberExt(String s) { msStreetNumberExt = s; }
    public void setStreetNumberInt(String s) { msStreetNumberInt = s; }
    public void setNeighborhood(String s) { msNeighborhood = s; }
    public void setReference(String s) { msReference = s; }
    public void setLocality(String s) { msLocality = s; }
    public void setState(String s) { msState = s; }
    public void setZipCode(String s) { msZipCode = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBolPersonTypeId(int n) { mnFkBolPersonTypeId = n; }
    public void setFkCountryId_n(int n) { mnFkCountryId_n = n; }
    public void setFkStateId_n(int n) { mnFkStateId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBolPersonId() { return mnPkBolPersonId; }
    public String getName() { return msName; }
    public String getFiscalId() { return msFiscalId; }
    public String getFiscalForeginId() { return msFiscalForeginId; }
    public String getDriverLicense() { return msDriverLicense; }
    public String getTelephone() { return msTelephone; }
    public String getStreet() { return msStreet; }
    public String getStreetNumberExt() { return msStreetNumberExt; }
    public String getStreetNumberInt() { return msStreetNumberInt; }
    public String getNeighborhood() { return msNeighborhood; }
    public String getReference() { return msReference; }
    public String getLocality() { return msLocality; }
    public String getState() { return msState; }
    public String getZipCode() { return msZipCode; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBolPersonTypeId() { return mnFkBolPersonTypeId; }
    public int getFkCountryId_n() { return mnFkCountryId_n; }
    public int getFkStateId_n() { return mnFkStateId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setDbmsBolPersonTypeCode(String s) { msDbmsBolPersonTypeCode = s; }
    
    public void setDataState(SDataState o) { moDataState = o; }
    public void setDataCountry(SDataCountry o) { moDataCountry = o; }
    
    public String getDbmsBolPersonTypeCode() { return msDbmsBolPersonTypeCode; }
    
    public SDataState getDataState() { return moDataState; }
    public SDataCountry getDataCountry() { return moDataCountry; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBolPersonId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolPersonId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBolPersonId = 0;
        msName = "";
        msFiscalId = "";
        msFiscalForeginId = "";
        msDriverLicense = "";
        msTelephone = "";
        msStreet = "";
        msStreetNumberExt = "";
        msStreetNumberInt = "";
        msNeighborhood = "";
        msReference = "";
        msLocality = "";
        msState = "";
        msZipCode = "";
        mbDeleted = false;
        mnFkBolPersonTypeId = 0;
        mnFkCountryId_n = 0;
        mnFkStateId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msDbmsBolPersonTypeCode = "";
        
        moDataState = null;
        moDataCountry = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol_person = " + mnPkBolPersonId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol_person = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkBolPersonId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_bol_person), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBolPersonId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * FROM " + getSqlTable() + " WHERE id_bol_person = " + pk[0];
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBolPersonId = resultSet.getInt("id_bol_person");
            msName = resultSet.getString("name");
            msFiscalId = resultSet.getString("fiscal_id");
            msFiscalForeginId = resultSet.getString("fiscal_frg_id");
            msDriverLicense = resultSet.getString("driver_lic");
            msTelephone = resultSet.getString("telephone");
            msStreet = resultSet.getString("street");
            msStreetNumberExt = resultSet.getString("street_num_ext");
            msStreetNumberInt = resultSet.getString("street_num_int");
            msNeighborhood = resultSet.getString("neighborhood");
            msReference = resultSet.getString("reference");
            msLocality = resultSet.getString("locality");
            msState = resultSet.getString("state");
            msZipCode = resultSet.getString("zip_code");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBolPersonTypeId = resultSet.getInt("fk_tp_bol_person");
            mnFkCountryId_n = resultSet.getInt("fk_cty_n");
            mnFkStateId_n = resultSet.getInt("fk_sta_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            msDbmsBolPersonTypeCode = (String) session.readField(SModConsts.LOGS_TP_BOL_PERSON, new int[] { mnFkBolPersonTypeId }, FIELD_CODE);

            mbRegistryNew = false;
        }
        
        // Read state:
        
        if (mnFkStateId_n != 0) {
            moDataState = new SDataState();
            moDataState.read(new int[] { mnFkStateId_n }, session.getStatement());
        }
        
        // Read country:
        
        if (mnFkCountryId_n != 0) {
            moDataCountry = new SDataCountry();
            moDataCountry.read(new int[] { mnFkCountryId_n }, session.getStatement());
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
        
        msDbmsBolPersonTypeCode = (String) session.readField(SModConsts.LOGS_TP_BOL_PERSON, new int[] { mnFkBolPersonTypeId }, FIELD_CODE);
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" + 
                mnPkBolPersonId + ", " + 
                "'" + msName + "', " + 
                "'" + msFiscalId + "', " + 
                "'" + msFiscalForeginId + "', " + 
                "'" + msDriverLicense + "', " + 
                "'" + msTelephone + "', " + 
                "'" + msStreet + "', " + 
                "'" + msStreetNumberExt + "', " + 
                "'" + msStreetNumberInt + "', " + 
                "'" + msNeighborhood + "', " + 
                "'" + msReference + "', " + 
                "'" + msLocality + "', " + 
                "'" + msState + "', " + 
                "'" + msZipCode + "', " + 
                (mbDeleted ? 1 : 0) + ", " + 
                mnFkBolPersonTypeId + ", " + 
                (mnFkCountryId_n == 0 ? "NULL, " : mnFkCountryId_n + ", ") + 
                (mnFkStateId_n == 0 ? "NULL, " : mnFkStateId_n + ", ") +
                mnFkUserInsertId + ", " + 
                mnFkUserUpdateId + ", " + 
                "NOW()" + ", " + 
                "NOW()" + " " + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                //"id_bol_person = " + mnPkBolPersonId + ", " +
                "name = '" + msName + "', " +
                "fiscal_id = '" + msFiscalId + "', " +
                "fiscal_frg_id = '" + msFiscalForeginId + "', " +
                "driver_lic = '" + msDriverLicense + "', " +
                "telephone = '" + msTelephone + "', " +
                "street = '" + msStreet + "', " +
                "street_num_ext = '" + msStreetNumberExt + "', " +
                "street_num_int = '" + msStreetNumberInt + "', " +
                "neighborhood = '" + msNeighborhood + "', " +
                "reference = '" + msReference + "', " +
                "locality = '" + msLocality + "', " +
                "state = '" + msState + "', " +
                "zip_code = '" + msZipCode + "', " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_tp_bol_person = " + mnFkBolPersonTypeId + ", " +
                "fk_cty_n = " + (mnFkCountryId_n == 0 ? "NULL, " : mnFkCountryId_n + ", ") +
                "fk_sta_n = " + (mnFkStateId_n == 0 ? "NULL, " : mnFkStateId_n + ", ") +
                //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                //"ts_usr_ins = " + "NOW()" + ", " +
                "ts_usr_upd = " + "NOW()" + " " +
                getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBolPerson clone() throws CloneNotSupportedException {
        SDbBolPerson registry = new SDbBolPerson();
        
        registry.setPkBolPersonId(this.getPkBolPersonId());
        registry.setName(this.getName());
        registry.setFiscalId(this.getFiscalId());
        registry.setFiscalForeginId(this.getFiscalForeginId());
        registry.setDriverLicense(this.getDriverLicense());
        registry.setTelephone(this.getTelephone());
        registry.setStreet(this.getStreet());
        registry.setStreetNumberExt(this.getStreetNumberExt());
        registry.setStreetNumberInt(this.getStreetNumberInt());
        registry.setNeighborhood(this.getNeighborhood());
        registry.setReference(this.getReference());
        registry.setLocality(this.getLocality());
        registry.setState(this.getState());
        registry.setZipCode(this.getZipCode());
        registry.setDeleted(this.isDeleted());
        registry.setFkBolPersonTypeId(this.getFkBolPersonTypeId());
        registry.setFkCountryId_n(this.getFkCountryId_n());
        registry.setFkStateId_n(this.getFkStateId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setDbmsBolPersonTypeCode(this.getDbmsBolPersonTypeCode());
        
        registry.setDataState(this.getDataState()); // el clon comparte este registro que es de sólo lectura
        registry.setDataCountry(this.getDataCountry()); // el clon comparte este registro que es de sólo lectura

        return registry;
    }
}
