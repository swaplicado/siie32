/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import cfd.DCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 * @author Sergio Flores
 */
public class SDataBizPartnerAddressee extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerAddresseeId;
    protected java.lang.String msName;
    protected java.lang.String msFiscalId;
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
    protected boolean mbIsDeleted;
    protected int mnFkBizPartnerId;
    protected int mnFkCountryId_n;
    protected int mnFkStateId_n;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected java.lang.String msDbmsCountryCode; // read-only
    protected java.lang.String msDbmsStateCode; // read-only

    public SDataBizPartnerAddressee() {
        super(SDataConstants.BPSU_BP_ADDEE);
        reset();
    }

    public void setPkBizPartnerAddresseeId(int n) { mnPkBizPartnerAddresseeId = n; }
    public void setName(java.lang.String s) { msName = s; }
    public void setFiscalId(java.lang.String s) { msFiscalId = s; }
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
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkCountryId_n(int n) { mnFkCountryId_n = n; }
    public void setFkStateId_n(int n) { mnFkStateId_n = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerAddresseeId() { return mnPkBizPartnerAddresseeId; }
    public java.lang.String getName() { return msName; }
    public java.lang.String getFiscalId() { return msFiscalId; }
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
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkCountryId_n() { return mnFkCountryId_n; }
    public int getFkStateId_n() { return mnFkStateId_n; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerAddresseeId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerAddresseeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerAddresseeId = 0;
        msName = "";
        msFiscalId = "";
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
        mbIsDeleted = false;
        mnFkBizPartnerId = 0;
        mnFkCountryId_n = 0;
        mnFkStateId_n = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        msDbmsCountryCode = "";
        msDbmsStateCode = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * " +
                    "FROM erp.bpsu_bp_addee " +
                    "WHERE id_bp_addee = " + key[0] + ";";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerAddresseeId = resultSet.getInt("id_bp_addee");
                msName = resultSet.getString("name");
                msFiscalId = resultSet.getString("fiscal_id");
                msAddress = resultSet.getString("address");
                msStreet = resultSet.getString("street");
                msStreetNumberExt = resultSet.getString("street_num_ext");
                msStreetNumberInt = resultSet.getString("street_num_int");
                msNeighborhood = resultSet.getString("neighborhood");
                msReference = resultSet.getString("reference");
                msLocality = resultSet.getString("locality");
                msCounty = resultSet.getString("county");
                msState = resultSet.getString("state");
                msZipCode = resultSet.getString("zip_code");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkBizPartnerId = resultSet.getInt("fid_bp");
                mnFkCountryId_n = resultSet.getInt("fid_cty_n");
                mnFkStateId_n = resultSet.getInt("fid_sta_n");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                
                // read contry code:
                
                if (mnFkCountryId_n != 0) {
                    sql = "SELECT cty_code FROM erp.locu_cty WHERE id_cty = " + mnFkCountryId_n + ";";
                }
                else {
                    sql = "SELECT cty_code FROM erp.locu_cty WHERE id_cty = (SELECT fid_cty FROM erp.cfg_param_erp WHERE id_erp = 1);";
                }
                
                resultSet = statement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                }
                else {
                    msDbmsCountryCode = resultSet.getString(1);
                }
                
                // read state code:
                
                if (mnFkStateId_n != 0) {
                    sql = "SELECT sta_code FROM erp.locu_sta WHERE id_sta = " + mnFkStateId_n + ";";
                    
                    resultSet = statement.executeQuery(sql);
                    if (!resultSet.next()) {
                        throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
                    }
                    else {
                        msDbmsStateCode = resultSet.getString(1);
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
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bp_addee_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerAddresseeId);
            callableStatement.setString(nParam++, msName);
            callableStatement.setString(nParam++, msFiscalId);
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
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkBizPartnerId);
            if (mnFkCountryId_n > 0) callableStatement.setInt(nParam++, mnFkCountryId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            if (mnFkStateId_n > 0) callableStatement.setInt(nParam++, mnFkStateId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);            
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkBizPartnerAddresseeId = callableStatement.getInt(nParam - 3);
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
     * Exports this registry as a Business-Partner-Branch Address.
     * Give what is possible to give.
     * @return SDataBizPartnerBranchAddress.
     */
    public SDataBizPartnerBranchAddress exportAddress() {
        SDataBizPartnerBranchAddress address = new SDataBizPartnerBranchAddress();
        
        //address.setPkBizPartnerBranchId(...);
        //address.setPkAddressId(...);
        address.setAddress(this.getAddress());
        address.setStreet(this.getStreet());
        address.setStreetNumberExt(this.getStreetNumberExt());
        address.setStreetNumberInt(this.getStreetNumberInt());
        address.setNeighborhood(this.getNeighborhood());
        address.setReference(this.getReference());
        address.setLocality(this.getLocality());
        address.setCounty(this.getCounty());
        address.setState(this.getState());
        address.setZipCode(this.getZipCode());
        address.setPoBox("");
        address.setIsDefault(false);
        address.setIsDeleted(this.getIsDeleted());
        address.setFkAddressTypeId(SDataConstantsSys.BPSS_TP_ADD_OFF);
        address.setFkCountryId_n(this.getFkCountryId_n());
        address.setFkStateId_n(this.getFkStateId_n());
    
        return address;
    }
    
    /**
     * Imports a Business-Partner-Branch Address into this registry.
     * Take what is possible to take.
     * @param address Business-Partner-Branch Address to import.
     */
    public void importAddress(SDataBizPartnerBranchAddress address) {
        //setPkBizPartnerAddresseeId(...);
        //setName(...);
        //setFiscalId(...);
        setAddress(address.getAddress());
        setStreet(address.getStreet());
        setStreetNumberExt(address.getStreetNumberExt());
        setStreetNumberInt(address.getStreetNumberInt());
        setNeighborhood(address.getNeighborhood());
        setReference(address.getReference());
        setLocality(address.getLocality());
        setCounty(address.getCounty());
        setState(address.getState());
        setZipCode(address.getZipCode());
        setIsDeleted(address.getIsDeleted());
        //setFkBizPartnerId(...);
        setFkCountryId_n(address.getFkCountryId_n());
        setFkStateId_n(address.getFkStateId_n());
    }
    /**
     * Creates element root for International Commerce addressee for business partner indicated.
     * @param cfdiVersion CFDI version (3.3 only supported).
     * @return cfd.ver3.cce11.DElementDestinatario node for International Commerce receptor.
     * @throws java.lang.Exception 
     */
    public cfd.ver3.cce11.DElementDestinatario createRootElementDestinatarioIntCommerce(double cfdiVersion) throws java.lang.Exception {
        cfd.ver3.cce11.DElementDestinatario destinatario = null;

        if (cfdiVersion == DCfdConsts.CFDI_VER_33) {
            destinatario = new cfd.ver3.cce11.DElementDestinatario();

            destinatario.getAttNumRegIdTrib().setString(msFiscalId);
            destinatario.getAttNombre().setString(msName);

            destinatario.getEltDomicilio().getAttCalle().setString(msStreet);
            destinatario.getEltDomicilio().getAttNoExterior().setString(msStreetNumberExt);
            destinatario.getEltDomicilio().getAttNoInterior().setString(msStreetNumberInt);
            destinatario.getEltDomicilio().getAttColonia().setString(msNeighborhood);
            destinatario.getEltDomicilio().getAttLocalidad().setString(msLocality);
            destinatario.getEltDomicilio().getAttReferencia().setString(msReference);
            destinatario.getEltDomicilio().getAttMunicipio().setString(msCounty);
            destinatario.getEltDomicilio().getAttEstado().setString(!msDbmsStateCode.isEmpty() ? msDbmsStateCode : msState);
            destinatario.getEltDomicilio().getAttCodigoPostal().setString(msZipCode);
            destinatario.getEltDomicilio().getAttPais().setString(msDbmsCountryCode);
        }
        else if (cfdiVersion == DCfdConsts.CFDI_VER_32) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        return destinatario;
    }
    
    public void validate(SClientInterface client) throws Exception {
        SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[] { mnFkBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);
        
        if (bizPartner == null) {
            throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n" + SDataBizPartner.class.getName());
        }
        else {
            int countryId = bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getFkCountryId_n();
            if (countryId == 0 || countryId == client.getSessionXXX().getParamsErp().getFkCountryId()) {
                throw new Exception("Solamente los asociados de negocios del extranjero pueden tener destinatarios.\n"
                        + "Se debe seleccionar otro asociado de negocios.");
            }
        }
        
        if (mnFkCountryId_n == 0 || mnFkCountryId_n == client.getSessionXXX().getParamsErp().getFkCountryId()) {
            throw new Exception("El destinatario debe ser del extranjero.\n"
                    + "Se debe seleccionar otro país.");
        }
        
        String error = "";
        String errorNote = "\n(NOTA: Considérese que el registro referido puede estar incluso eliminado.)";
        String[][] sqlsErrors = new String[][] { 
            new String[] { "SELECT COUNT(*) FROM erp.bpsu_bp_addee WHERE name = '" + msName + "' AND fid_bp = " + mnFkBizPartnerId + " AND id_bp_addee <> " + mnPkBizPartnerAddresseeId + ";", 
                "¡El nombre del destinatario ('" + msName + "') ya existe en el catálogo de destinatarios de este asociado de negocios!" + errorNote}, 
            new String[] { "SELECT COUNT(*) FROM erp.bpsu_bp_addee WHERE fiscal_id = '" + msFiscalId + "' AND fid_bp = " + mnFkBizPartnerId + " AND id_bp_addee <> " + mnPkBizPartnerAddresseeId + ";", 
                "¡El ID fiscal del destinatario ('" + msFiscalId + "') ya existe en el catálogo de destinatarios de este asociado de negocios!" + errorNote}, 
            new String[] { "SELECT COUNT(*) FROM erp.bpsu_bp_addee WHERE address = '" + msAddress + "' AND fid_bp = " + mnFkBizPartnerId + " AND id_bp_addee <> " + mnPkBizPartnerAddresseeId + ";", 
                "¡El Id. domicilio de este destinatario ('" + msAddress + "') ya existe en el catálogo de destinatarios de este asociado de negocios!" + errorNote}, 
            new String[] { "SELECT COUNT(*) FROM erp.bpsu_bp WHERE bp = '" + msName + "';", 
                "¡El nombre del destinatario ('" + msName + "') ya existe en el catálogo de asociados de negocios!\n"
                + "No es necesario este destinatario, porque se puede utilizar en su lugar directamente al asociado de negocios en cuestión." + errorNote}, 
            new String[] { "SELECT COUNT(*) FROM erp.bpsu_bp WHERE fiscal_frg_id = '" + msFiscalId + "';", 
                "¡El ID fiscal del destinatario ('" + msFiscalId + "') ya existe en el catálogo de asociados de negocios!\n"
                + "No es necesario este destinatario, porque se puede utilizar en su lugar directamente al asociado de negocios en cuestión." + errorNote}, 
        };
        
        for (String[] sqlError : sqlsErrors) {
            try (ResultSet resultSet = client.getSession().getStatement().executeQuery(sqlError[0])) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    error = sqlError[1];
                    break;
                }
            }
        }
        
        if (!error.isEmpty()) {
            throw new Exception(error);
        }
    }
}
