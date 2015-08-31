/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mcfg.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author Juan Barajas
 */
public class SDataCertificate extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCerificateId;
    protected java.util.Date mtDate;
    protected java.util.Date mtExpirationDate;
    protected java.lang.String msNumber;
    //protected java.sql.Blob moPrivateKey_n;
    //protected java.sql.Blob moPublicKey_n;
    //protected java.sql.Blob moFinkokPrivateKey_n;
    //protected java.sql.Blob moFinkokPublicKey_n;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected byte[] mayExtraPrivateKeyBytes_n;
    protected byte[] mayExtraPublicKeyBytes_n;
    protected byte[] mayExtraFnkPrivateKeyBytes_n;
    protected byte[] mayExtraFnkPublicKeyBytes_n;

    public SDataCertificate() {
        super(SDataConstants.CFGU_CERT);
        reset();
    }

    public void setPkCertificateId(int n) { mnPkCerificateId = n; }
    public void setDate(java.util.Date t) { mtDate = t; }
    public void setExpirationDate(java.util.Date t) { mtExpirationDate = t; }
    public void setNumber(java.lang.String s) { msNumber = s; }
    //public void setPrivateKey_n(java.sql.Blob o) { moPrivateKey_n = o; }
    //public void setPublicKey_n(java.sql.Blob o) { moPublicKey_n = o; }
    //public void setFinkokPrivateKey_n(java.sql.Blob o) { moFinkokPrivateKey_n = o; }
    //public void setFinkokPublicKey_n(java.sql.Blob o) { moFinkokPublicKey_n = o; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public void setExtraPrivateKeyBytes_n(byte[] ay) { mayExtraPrivateKeyBytes_n = ay; }
    public void setExtraPublicKeyBytes_n(byte[] ay) { mayExtraPublicKeyBytes_n = ay; }
    public void setExtraFnkPrivateKeyBytes_n(byte[] ay) { mayExtraFnkPrivateKeyBytes_n = ay; }
    public void setExtraFnkPublicKeyBytes_n(byte[] ay) { mayExtraFnkPublicKeyBytes_n = ay; }

    public int getPkCertificateId() { return mnPkCerificateId; }
    public java.util.Date getDate() { return mtDate; }
    public java.util.Date getExpirationDate() { return mtExpirationDate; }
    public java.lang.String getNumber() { return msNumber; }
    //public java.sql.Blob getPrivateKey_n() { return moPrivateKey_n; }
    //public java.sql.Blob getPublicKey_n() { return moPublicKey_n; }
    //public java.sql.Blob getFinkokPrivateKey_n() { return moFinkokPrivateKey_n; }
    //public java.sql.Blob getFinkokPublicKey_n() { return moFinkokPublicKey_n; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }
    
    public byte[] getExtraPrivateKeyBytes_n() { return mayExtraPrivateKeyBytes_n; }
    public byte[] getExtraPublicKeyBytes_n() { return mayExtraPublicKeyBytes_n; }
    public byte[] getExtraFnkPrivateKeyBytes_n() { return mayExtraFnkPrivateKeyBytes_n; }
    public byte[] getExtraFnkPublicKeyBytes_n() { return mayExtraFnkPublicKeyBytes_n; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCerificateId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCerificateId };
    }

    @Override
    public final void reset() {
        super.resetRegistry();

        mnPkCerificateId = 0;
        mtDate = null;
        mtExpirationDate = null;
        msNumber = "";
        //moCertPrivateKey_n = null;
        //moCertPublicKey_n = null;
        //moFinkokPrivateKey_n = null;
        //moFinkokPublicKey_n = null;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        mayExtraPrivateKeyBytes_n = null;
        mayExtraPublicKeyBytes_n = null;
        mayExtraFnkPrivateKeyBytes_n = null;
        mayExtraFnkPublicKeyBytes_n = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Blob oCertPrivateKey_n;
        Blob oCertPublicKey_n;
        Blob oCertFnkPrivateKey_n;
        Blob oCertFnkPublicKey_n;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM cfgu_cert WHERE id_cert = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCerificateId = resultSet.getInt("id_cert");
                mtDate = resultSet.getDate("dt");
                mtExpirationDate = resultSet.getDate("exp_dt");
                msNumber = resultSet.getString("num");
                oCertPrivateKey_n = resultSet.getBlob("key_priv_n");
                oCertPublicKey_n = resultSet.getBlob("key_pub_n");
                oCertFnkPrivateKey_n = resultSet.getBlob("fnk_key_priv_n");
                oCertFnkPublicKey_n = resultSet.getBlob("fnk_key_pub_n");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                
                if (oCertPrivateKey_n != null) {
                    mayExtraPrivateKeyBytes_n = SLibUtilities.convertBlobToBytes(oCertPrivateKey_n);
                }

                if (oCertPublicKey_n != null) {
                    mayExtraPublicKeyBytes_n = SLibUtilities.convertBlobToBytes(oCertPublicKey_n);
                }
                
                if (oCertFnkPrivateKey_n != null) {
                    mayExtraFnkPrivateKeyBytes_n = SLibUtilities.convertBlobToBytes(oCertFnkPrivateKey_n);
                }

                if (oCertFnkPublicKey_n != null) {
                    mayExtraFnkPublicKeyBytes_n = SLibUtilities.convertBlobToBytes(oCertFnkPublicKey_n);
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
        java.sql.CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall("{ CALL cfgu_cert_save(?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkCerificateId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setDate(nParam++, new java.sql.Date(mtExpirationDate.getTime()));
            callableStatement.setString(nParam++, msNumber);
            //callableStatement.setBlob(nParam++, moPrivateKey_n);
            //callableStatement.setBlob(nParam++, moPublicKey_n);
            //callableStatement.setBlob(nParam++, moFinkokPrivateKey_n);
            //callableStatement.setBlob(nParam++, moFinkokPublicKey_n);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

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
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
