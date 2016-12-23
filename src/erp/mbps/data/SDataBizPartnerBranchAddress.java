/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mcfg.data.SDataParamsErp;
import erp.mloc.data.SDataCountry;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataBizPartnerBranchAddress extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    public static final int ADDRESS_2ROWS = 2;
    public static final int ADDRESS_3ROWS = 3;
    public static final int ADDRESS_4ROWS = 4;

    protected int mnPkBizPartnerBranchId;
    protected int mnPkAddressId;
    protected java.lang.String msAddress;
    protected java.lang.String msStreet;
    protected java.lang.String msStreetNumberExt;
    protected java.lang.String msStreetNumberInt;
    protected java.lang.String msNeighborhood;
    protected java.lang.String msReference;
    protected java.lang.String msLocality;
    protected java.lang.String msCounty;
    protected java.lang.String msState;
    protected java.lang.String msZipCode;
    protected java.lang.String msPoBox;
    protected boolean mbIsDefault;
    protected boolean mbIsDeleted;
    protected int mnFkAddressTypeId;
    protected int mnFkCountryId_n;
    protected int mnFkStateId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsAddressType;
    protected java.lang.String msDbmsUserNew;
    protected java.lang.String msDbmsUserEdit;
    protected java.lang.String msDbmsUserDelete;
    
    protected int mnAuxCountrySysId;

    protected erp.mloc.data.SDataCountry moDbmsDataCountry;
    protected erp.mcfg.data.SDataParamsErp moParamsErp;

    public SDataBizPartnerBranchAddress() {
        super(SDataConstants.BPSU_BPB_ADD);
        reset();
    }

    public void setPkBizPartnerBranchId(int n) { mnPkBizPartnerBranchId = n; }
    public void setPkAddressId(int n) { mnPkAddressId = n; }
    public void setAddress(java.lang.String s) { msAddress = s; }
    public void setStreet(java.lang.String s) { msStreet = s; }
    public void setStreetNumberExt(java.lang.String s) { msStreetNumberExt = s; }
    public void setStreetNumberInt(java.lang.String s) { msStreetNumberInt = s; }
    public void setNeighborhood(java.lang.String s) { msNeighborhood = s; }
    public void setReference(java.lang.String s) { msReference = s; }
    public void setLocality(java.lang.String s) { msLocality = s; }
    public void setCounty(java.lang.String s) { msCounty = s; }
    public void setState(java.lang.String s) { msState = s; }
    public void setZipCode(java.lang.String s) { msZipCode = s; }
    public void setPoBox(java.lang.String s) { msPoBox = s; }
    public void setIsDefault(boolean b) { mbIsDefault = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkAddressTypeId(int n) { mnFkAddressTypeId = n; }
    public void setFkCountryId_n(int n) { mnFkCountryId_n = n; }
    public void setFkStateId_n(int n) { mnFkStateId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public int getPkBizPartnerBranchId() { return mnPkBizPartnerBranchId; }
    public int getPkAddressId() { return mnPkAddressId; }
    public java.lang.String getAddress() { return msAddress; }
    public java.lang.String getStreet() { return msStreet; }
    public java.lang.String getStreetNumberExt() { return msStreetNumberExt; }
    public java.lang.String getStreetNumberInt() { return msStreetNumberInt; }
    public java.lang.String getNeighborhood() { return msNeighborhood; }
    public java.lang.String getReference() { return msReference; }
    public java.lang.String getLocality() { return msLocality; }
    public java.lang.String getCounty() { return msCounty; }
    public java.lang.String getState() { return msState; }
    public java.lang.String getZipCode() { return msZipCode; }
    public java.lang.String getPoBox() { return msPoBox; }
    public boolean getIsDefault() { return mbIsDefault; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkAddressTypeId() { return mnFkAddressTypeId; }
    public int getFkCountryId_n() { return mnFkCountryId_n; }
    public int getFkStateId_n() { return mnFkStateId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public void setDbmsAddressType(java.lang.String s) { msDbmsAddressType = s; }
    public void setDbmsUserNew(java.lang.String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(java.lang.String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(java.lang.String s) { msDbmsUserDelete = s; }

    public void setAuxCountrySysId(int n) { mnAuxCountrySysId = n; }
    
    public java.lang.String getDbmsAddressType() { return msDbmsAddressType; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }
    public erp.mloc.data.SDataCountry getDbmsDataCountry() { return moDbmsDataCountry; }

    public int getAuxCountrySysId() { return mnAuxCountrySysId; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerBranchId = ((int[]) pk)[0];
        mnPkAddressId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerBranchId, mnPkAddressId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerBranchId = 0;
        mnPkAddressId = 0;
        msAddress = "";
        msStreet = "";
        msStreetNumberExt = "";
        msStreetNumberInt = "";
        msNeighborhood = "";
        msReference = "";
        msLocality = "";
        msCounty = "";
        msState = "";
        msZipCode = "";
        msPoBox = "";
        mbIsDefault = false;
        mbIsDeleted = false;
        mnFkAddressTypeId = 0;
        mnFkCountryId_n = 0;
        mnFkStateId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsAddressType = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
        moDbmsDataCountry = null;
        
        mnAuxCountrySysId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT a.*, ta.tp_add, un.usr, ue.usr, ud.usr " +
                    "FROM erp.bpsu_bpb_add AS a " +
                    "INNER JOIN erp.bpss_tp_add AS ta ON " +
                    "a.fid_tp_add = ta.id_tp_add " +
                    "INNER JOIN erp.usru_usr AS un ON " +
                    "a.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON " +
                    "a.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON " +
                    "a.fid_usr_del = ud.id_usr " +
                    "WHERE a.id_bpb = " + key[0] + " AND a.id_add = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerBranchId = resultSet.getInt("a.id_bpb");
                mnPkAddressId = resultSet.getInt("a.id_add");
                msAddress = resultSet.getString("a.bpb_add");
                msStreet = resultSet.getString("a.street");
                msStreetNumberExt = resultSet.getString("a.street_num_ext");
                msStreetNumberInt = resultSet.getString("a.street_num_int");
                msNeighborhood = resultSet.getString("a.neighborhood");
                msReference = resultSet.getString("a.reference");
                msLocality = resultSet.getString("a.locality");
                msCounty = resultSet.getString("a.county");
                msState = resultSet.getString("a.state");
                msZipCode = resultSet.getString("a.zip_code");
                msPoBox = resultSet.getString("a.po_box");
                mbIsDefault = resultSet.getBoolean("a.b_def");
                mbIsDeleted = resultSet.getBoolean("a.b_del");
                mnFkAddressTypeId = resultSet.getInt("a.fid_tp_add");
                mnFkCountryId_n = resultSet.getInt("a.fid_cty_n");
                mnFkStateId_n = resultSet.getInt("a.fid_sta_n");
                mnFkUserNewId = resultSet.getInt("a.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("a.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("a.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("a.ts_new");
                mtUserEditTs = resultSet.getTimestamp("a.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("a.ts_del");

                msDbmsAddressType = resultSet.getString("ta.tp_add");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

                // Read aswell dependant registries:

                moDbmsDataCountry = new SDataCountry();
                
                if (mnFkStateId_n != SLibConstants.UNDEFINED) {
                    sql = "SELECT sta FROM erp.locu_sta WHERE id_sta = " + mnFkStateId_n + " ";
                    resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        msState = resultSet.getString("sta");
                    }
                }
                
                if (mnFkCountryId_n == SLibConstants.UNDEFINED) {
                    moParamsErp = new SDataParamsErp();
                    if (moParamsErp.read(new int[] { 1 }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mnFkCountryId_n = moParamsErp.getFkCountryId();
                    }
                }

                if (moDbmsDataCountry.read(new int[] { mnFkCountryId_n }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
                }
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

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bpb_add_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerBranchId);
            callableStatement.setInt(nParam++, mnPkAddressId);
            callableStatement.setString(nParam++, msAddress);
            callableStatement.setString(nParam++, msStreet);
            callableStatement.setString(nParam++, msStreetNumberExt);
            callableStatement.setString(nParam++, msStreetNumberInt);
            callableStatement.setString(nParam++, msNeighborhood);
            callableStatement.setString(nParam++, msReference);
            callableStatement.setString(nParam++, msLocality);
            callableStatement.setString(nParam++, msCounty);
            callableStatement.setString(nParam++, msState);
            callableStatement.setString(nParam++, msZipCode);
            callableStatement.setString(nParam++, msPoBox);
            callableStatement.setBoolean(nParam++, mbIsDefault);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkAddressTypeId);
            if (mnFkCountryId_n > 0 && mnFkCountryId_n != mnAuxCountrySysId) callableStatement.setInt(nParam++, mnFkCountryId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkStateId_n > SLibConstants.UNDEFINED) callableStatement.setInt(nParam++, mnFkStateId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);            
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkAddressId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
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
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }


    /**
     * Arranges branch address fields in user requested way.
     * @param language Constants defined in erp.lib.SLibConstants.
     * @param addressStyle Constants defined in this class, erp.mbps.data.SDataBizPartnerBranchAddress.
     * @return Array of java.lang.String containing:
     * index 0: Street and street number and external street number.
     * index 1: Neighborhood [zip code].
     * index 2: Locality [zip code].
     * index 3: [Country].
     */
    public java.lang.String[] obtainAddress(int addressFormat, int addressStyle, boolean includeCountry) {
        String[] address = null;

        switch (addressFormat) {
            case SDataConstantsSys.BPSS_TP_ADD_FMT_STD:
                switch (addressStyle) {
                    case ADDRESS_2ROWS:
                        address = new String[2];
                        address[0] = SLibUtilities.textTrim(msStreet + " " + msStreetNumberExt + " " + msStreetNumberInt + ", " + msNeighborhood + " " + msReference);
                        address[1] = msZipCode + " " + msLocality + (msCounty.length() > 0 ? ", " : "") + msCounty + (msState.length() > 0 ? ", " : "") + msState + (!includeCountry ? "" : ", " + moDbmsDataCountry.getCountry());
                        break;
                    case ADDRESS_3ROWS:
                        address = new String[3];
                        address[0] = SLibUtilities.textTrim(msStreet + " " + msStreetNumberExt + " " + msStreetNumberInt);
                        address[1] = msNeighborhood + " " + msReference;
                        address[2] = msZipCode + " " + msLocality + (msCounty.length() > 0 ? ", " : "") + msCounty + (msState.length() > 0 ? ", " : "") + msState + (!includeCountry ? "" : ", " + moDbmsDataCountry.getCountry());
                        break;
                    case ADDRESS_4ROWS:
                        address = new String[4];
                        address[0] = SLibUtilities.textTrim(msStreet + " " + msStreetNumberExt + " " + msStreetNumberInt);
                        address[1] = msNeighborhood + " " + msReference;
                        address[2] = msZipCode + " " + msLocality + (msCounty.length() > 0 ? ", " : "") + msCounty + (msState.length() > 0 ? ", " : "") + msState;
                        address[3] = (!includeCountry ? "" : moDbmsDataCountry.getCountry());
                        break;
                    default:
                }
                break;

            case SDataConstantsSys.BPSS_TP_ADD_FMT_US:
                switch (addressStyle) {
                    case ADDRESS_2ROWS:
                        address = new String[2];
                        address[0] = SLibUtilities.textTrim(msStreetNumberExt + " " + msStreetNumberInt + " "  + msStreet + " " + msNeighborhood);
                        address[1] =  msLocality + (msCounty.length() > 0 ? ", " : "") + msCounty + (msState.length() > 0 ? ", " : "") + msState + " " + msZipCode + " " + (!includeCountry ? "" : ", " + moDbmsDataCountry.getCountry()) + " " + msZipCode;
                        break;
                    case ADDRESS_3ROWS:
                        address = new String[3];
                        address[0] = SLibUtilities.textTrim(msStreetNumberExt + " " + msStreetNumberInt  + " " + msStreet + " " + msNeighborhood);
                        address[1] = msLocality + (msCounty.length() > 0 ? ", " : "") + msCounty + (msState.length() > 0 ? ", " : "") + msState + " " + msZipCode;
                        address[2] = !includeCountry ? "" : moDbmsDataCountry.getCountry();
                        break;
                    case ADDRESS_4ROWS:
                        address = new String[4];
                        address[0] = SLibUtilities.textTrim(msStreetNumberExt  + " " + msStreetNumberInt + " " + msStreet + " " + msNeighborhood);
                        address[1] = msLocality + (msCounty.length() > 0 ? ", " : "") + msCounty + (msState.length() > 0 ? ", " : "") + msState + " " + msZipCode;
                        address[2] = !includeCountry ? "" : moDbmsDataCountry.getCountry();
                        address[3] = "";
                        break;
                    default:
                }
                break;

            default:
        }

        return address;
    }
}
