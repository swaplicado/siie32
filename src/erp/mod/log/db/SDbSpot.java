/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SDbSpot extends SDbRegistryUser {

    public static final int FIELD_FK_TP_SPOT = FIELD_BASE + 1;

    protected int mnPkSpotId;
    protected String msCode;
    protected String msName;
    protected String msDistrict;
    protected String msReference;
    protected String msLocalityCode;
    protected String msLocality;
    protected String msCountyCode;
    protected String msCounty;
    protected String msState;
    protected String msZipCode;
    protected boolean mbGeneric;
    //protected boolean mbDeleted;
    protected int mnFkSpotTypeId;
    protected int mnFkDeliveryTypeId;
    protected int mnFkCountryId_n;
    protected int mnFkStateId_n;
    protected int mnFkNeighborhoodZipCodeId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbSpot() {
        super(SModConsts.LOGU_SPOT);
    }

    public void setPkSpotId(int n) { mnPkSpotId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDistrict(String s) { msDistrict = s; }
    public void setReference(String s) { msReference = s; }
    public void setLocalityCode(String s) { msLocalityCode = s; }
    public void setLocality(String s) { msLocality = s; }
    public void setCountyCode(String s) { msCountyCode = s; }
    public void setCounty(String s) { msCounty = s; }
    public void setState(String s) { msState = s; }
    public void setZipCode(String s) { msZipCode = s; }
    public void setGeneric(boolean b) { mbGeneric = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkSpotTypeId(int n) { mnFkSpotTypeId = n; }
    public void setFkDeliveryTypeId(int n) { mnFkDeliveryTypeId = n; }
    public void setFkCountryId_n(int n) { mnFkCountryId_n = n; }
    public void setFkStateId_n(int n) { mnFkStateId_n = n; }
    public void setFkNeighborhoodZipCodeId_n(int n) { mnFkNeighborhoodZipCodeId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkSpotId() { return mnPkSpotId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getDistrict() { return msDistrict; }
    public String getReference() { return msReference; }
    public String getLocalityCode() { return msLocalityCode; }
    public String getLocality() { return msLocality; }
    public String getCountyCode() { return msCountyCode; }
    public String getCounty() { return msCounty; }
    public String getState() { return msState; }
    public String getZipCode() { return msZipCode; }
    public boolean isGeneric() { return mbGeneric; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkSpotTypeId() { return mnFkSpotTypeId; }
    public int getFkDeliveryTypeId() { return mnFkDeliveryTypeId; }
    public int getFkCountryId_n() { return mnFkCountryId_n; }
    public int getFkStateId_n() { return mnFkStateId_n; }
    public int getFkNeighborhoodZipCodeId_n() { return mnFkNeighborhoodZipCodeId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSpotId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSpotId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSpotId = 0;
        msCode = "";
        msName = "";
        msDistrict = "";
        msReference = "";
        msLocalityCode = "";
        msLocality = "";
        msCountyCode = "";
        msCounty = "";
        msState = "";
        msZipCode = "";
        mbGeneric = false;
        mbDeleted = false;
        mnFkSpotTypeId = 0;
        mnFkDeliveryTypeId = 0;
        mnFkCountryId_n = 0;
        mnFkStateId_n = 0;
        mnFkNeighborhoodZipCodeId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_spot = " + mnPkSpotId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_spot = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSpotId = 0;

        msSql = "SELECT COALESCE(MAX(id_spot), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSpotId = resultSet.getInt(1);
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
            mnPkSpotId = resultSet.getInt("id_spot");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msDistrict = resultSet.getString("district");
            msReference = resultSet.getString("reference");
            msLocalityCode = resultSet.getString("locality_code");
            msLocality = resultSet.getString("locality");
            msCountyCode = resultSet.getString("county_code");
            msCounty = resultSet.getString("county");
            msState = resultSet.getString("state");
            msZipCode = resultSet.getString("zip_code");
            mbGeneric = resultSet.getBoolean("b_gen");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkSpotTypeId = resultSet.getInt("fk_tp_spot");
            mnFkDeliveryTypeId = resultSet.getInt("fk_tp_dly");
            mnFkCountryId_n = resultSet.getInt("fk_cty_n");
            mnFkStateId_n = resultSet.getInt("fk_sta_n");
            mnFkNeighborhoodZipCodeId_n = resultSet.getInt("fk_nei_zip_code_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSpotId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msDistrict + "', " + 
                    "'" + msReference + "', " + 
                    "'" + msLocalityCode + "', " + 
                    "'" + msLocality + "', " + 
                    "'" + msCountyCode + "', " +    
                    "'" + msCounty + "', " + 
                    "'" + msState + "', " + 
                    "'" + msZipCode + "', " + 
                    (mbGeneric ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkSpotTypeId + ", " + 
                    mnFkDeliveryTypeId + ", " + 
                    (mnFkCountryId_n == 0 ? "NULL" : mnFkCountryId_n) + ", " + 
                    (mnFkStateId_n == 0 ? "NULL" : mnFkStateId_n) + ", " + 
                    (mnFkNeighborhoodZipCodeId_n == 0 ? "NULL" : mnFkNeighborhoodZipCodeId_n) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_spot = " + mnPkSpotId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "district = '" + msDistrict + "', " +
                    "reference = '" + msReference + "', " +
                    "locality_code = '" + msLocalityCode + "', " +
                    "locality = '" + msLocality + "', " +
                    "county_code = '" + msCountyCode + "', " +
                    "county = '" + msCounty + "', " +
                    "state = '" + msState + "', " +
                    "zip_code = '" + msZipCode + "', " +
                    "b_gen = " + (mbGeneric ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_spot = " + mnFkSpotTypeId + ", " +
                    "fk_tp_dly = " + mnFkDeliveryTypeId + ", " +
                    "fk_cty_n = " + (mnFkCountryId_n == 0 ? "NULL" : mnFkCountryId_n) + ", " +
                    "fk_sta_n = " + (mnFkStateId_n == 0 ? "NULL" : mnFkStateId_n) + ", " +
                    "fk_nei_zip_code_n = " + (mnFkNeighborhoodZipCodeId_n == 0 ? "NULL" : mnFkNeighborhoodZipCodeId_n) + ", " +
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
    public Object readField(Statement statement, int[] pk, int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT ";

        switch (field) {
            case FIELD_FK_TP_SPOT:
                msSql += "fk_tp_spot FROM " + getSqlTable() + " ";
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        resultSet = statement.executeQuery(msSql);

        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            switch (field) {
                case FIELD_FK_TP_SPOT:
                    value = resultSet.getInt(1);
                    break;
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }

        mnQueryResultId = SDbConsts.READ_OK;

        return value;
    }

    @Override
    public SDbSpot clone() throws CloneNotSupportedException {
        SDbSpot registry = new SDbSpot();

        registry.setPkSpotId(this.getPkSpotId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDistrict(this.getDistrict());
        registry.setReference(this.getReference());
        registry.setLocalityCode(this.getLocalityCode());
        registry.setLocality(this.getLocality());
        registry.setCountyCode(this.getCountyCode());
        registry.setCounty(this.getCounty());
        registry.setState(this.getState());
        registry.setZipCode(this.getZipCode());
        registry.setGeneric(this.isGeneric());
        registry.setDeleted(this.isDeleted());
        registry.setFkSpotTypeId(this.getFkSpotTypeId());
        registry.setFkDeliveryTypeId(this.getFkDeliveryTypeId());
        registry.setFkCountryId_n(this.getFkCountryId_n());
        registry.setFkStateId_n(this.getFkStateId_n());
        registry.setFkNeighborhoodZipCodeId_n(this.getFkNeighborhoodZipCodeId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
