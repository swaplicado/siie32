/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Sergio Flores
 */
public class SDataDpsXml extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected java.util.Date mtTimestamp;
    protected java.lang.String msCertNumber;
    protected java.lang.String msStringSigned;
    protected java.lang.String msSignature;
    protected java.lang.String msDocXml;
    protected java.lang.String msDocXmlName;
    protected java.lang.String msAcknowledgmentCancellation;
    protected java.lang.String msUuid;
    protected byte[] moQrc_n;
    protected int mnFkXmlTypeId;
    protected int mnFkXmlStatusId;

    public SDataDpsXml() {
        super(SDataConstants.TRN_DPS_XML);
        reset();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setTimestamp(java.util.Date t) { mtTimestamp = t; }
    public void setCertNumber(java.lang.String s) { msCertNumber = s; }
    public void setStringSigned(java.lang.String s) { msStringSigned = s; }
    public void setSignature(java.lang.String s) { msSignature = s; }
    public void setDocXml(java.lang.String s) { msDocXml = s; }
    public void setDocXmlName(java.lang.String s) { msDocXmlName = s; }
    public void setAcknowledgmentCancellation(java.lang.String s) { msAcknowledgmentCancellation = s; }
    public void setUuid(java.lang.String s) { msUuid = s; }
    public void setQrc_n(byte[] o) { moQrc_n = o; }
    public void setFkXmlTypeId(int n) { mnFkXmlTypeId = n; }
    public void setFkXmlStatusId(int n) { mnFkXmlStatusId = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public java.util.Date getTimestamp() { return mtTimestamp; }
    public java.lang.String getCertNumber() { return msCertNumber; }
    public java.lang.String getStringSigned() { return msStringSigned; }
    public java.lang.String getSignature() { return msSignature; }
    public java.lang.String getDocXml() { return msDocXml; }
    public java.lang.String getDocXmlName() { return msDocXmlName; }
    public java.lang.String getAcknowledgmentCancellation() { return msAcknowledgmentCancellation; }
    public java.lang.String getUuid() { return msUuid; }
    public byte[] getQrc_n() { return moQrc_n; }
    public int getFkXmlTypeId() { return mnFkXmlTypeId; }
    public int getFkXmlStatusId() { return mnFkXmlStatusId; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkYearId = ((int[]) pk)[0];
        mnPkDocId = ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mtTimestamp = null;
        msCertNumber = "";
        msStringSigned = "";
        msSignature = "";
        msDocXml = "";
        msDocXmlName = "";
        msAcknowledgmentCancellation = "";
        msUuid = "";
        moQrc_n = null;
        mnFkXmlTypeId = 0;
        mnFkXmlStatusId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_dps_xml WHERE id_year = " + key[0] + " AND id_doc = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkDocId = resultSet.getInt("id_doc");
                mtTimestamp = resultSet.getTimestamp("ts");
                msCertNumber = resultSet.getString("cert_num");
                msStringSigned = resultSet.getString("str_signed");
                msSignature = resultSet.getString("signature");
                msDocXml = resultSet.getString("doc_xml");
                msDocXmlName = resultSet.getString("doc_xml_name");
                msAcknowledgmentCancellation = resultSet.getString("ack_can");
                msUuid = resultSet.getString("uuid");
                mnFkXmlTypeId = resultSet.getInt("fid_tp_xml");
                mnFkXmlStatusId = resultSet.getInt("fid_st_xml");

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
        String sql = "";
        Statement statement = null;
        PreparedStatement preparedStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            statement = connection.createStatement();

            sql = "DELETE FROM trn_dps_xml WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " ";
            statement.execute(sql);

            sql = "INSERT INTO trn_dps_xml (id_year, id_doc, " +
                    "ts, cert_num, str_signed, signature, doc_xml, doc_xml_name, ack_can, uuid, qrc_n, fid_tp_xml, fid_st_xml) " +
                    "VALUES (" + mnPkYearId + ", " + mnPkDocId + ", " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setTimestamp(1, new java.sql.Timestamp(mtTimestamp.getTime()));
            preparedStatement.setString(2, msCertNumber);
            preparedStatement.setString(3, msStringSigned);
            preparedStatement.setString(4, msSignature);
            preparedStatement.setString(5, msDocXml);
            preparedStatement.setString(6, msDocXmlName);
            preparedStatement.setString(7, msAcknowledgmentCancellation);
            if (mnFkXmlTypeId != SDataConstantsSys.TRNS_TP_XML_CFDI) {
                preparedStatement.setString(8, "");
                preparedStatement.setNull(9, java.sql.Types.BLOB);
            }
            else {
                preparedStatement.setString(8, msUuid);

                if (moQrc_n == null) {
                    preparedStatement.setNull(9, java.sql.Types.BLOB);
                }
                else {
                    preparedStatement.setBytes(9, moQrc_n);
                }
            }
            preparedStatement.setInt(10, mnFkXmlTypeId);
            preparedStatement.setInt(11, mnFkXmlStatusId);
            preparedStatement.execute();

            mnDbmsErrorId = 0;
            msDbmsError = "";

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public java.util.Date getLastDbUpdate() {
        return null;
    }
}
