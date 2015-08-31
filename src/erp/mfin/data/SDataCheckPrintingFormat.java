/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Alfonso Flores
 */
public class SDataCheckPrintingFormat extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkCheckFormatId;
    protected java.lang.String msCheckFormat;
    protected double mdDateX;
    protected double mdDateY;
    protected double mdBeneficiaryX;
    protected double mdBeneficiaryY;
    protected double mdBeneficiaryAccountX;
    protected double mdBeneficiaryAccountY;
    protected double mdValueX;
    protected double mdValueY;
    protected double mdValueTextX;
    protected double mdValueTextY;
    protected double mdIssueLocalityX;
    protected double mdIssueLocalityY;
    protected double mdRecordNumberX;
    protected double mdRecordNumberY;
    protected double mdCheckNumberX;
    protected double mdCheckNumberY;
    protected double mdBackX;
    protected double mdBackY;
    protected boolean mbIsIssueLocalityApplying;
    protected boolean mbIsRecordNumberApplying;
    protected boolean mbIsCheckNumberApplying;
    protected boolean mbIsRotate;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    public SDataCheckPrintingFormat() {
        super(SDataConstants.FINU_CHECK_FMT);
        reset();
    }

    public void setPkCheckFormatId(int n) { mnPkCheckFormatId = n; }
    public void setCheckFormat(java.lang.String s) { msCheckFormat = s; }
    public void setDateX(double d) { mdDateX = d; }
    public void setDateY(double d) { mdDateY = d; }
    public void setBeneficiaryX(double d) { mdBeneficiaryX = d; }
    public void setBeneficiaryY(double d) { mdBeneficiaryY = d; }
    public void setBeneficiaryAccountX(double d) { mdBeneficiaryAccountX = d; }
    public void setBeneficiaryAccountY(double d) { mdBeneficiaryAccountY = d; }
    public void setValueX(double d) { mdValueX = d; }
    public void setValueY(double d) { mdValueY = d; }
    public void setValueTextX(double d) { mdValueTextX = d; }
    public void setValueTextY(double d) { mdValueTextY = d; }
    public void setIssueLocalityX(double d) { mdIssueLocalityX = d; }
    public void setIssueLocalityY(double d) { mdIssueLocalityY = d; }
    public void setRecordNumberX(double d) { mdRecordNumberX = d; }
    public void setRecordNumberY(double d) { mdRecordNumberY = d; }
    public void setCheckNumberX(double d) { mdCheckNumberX = d; }
    public void setCheckNumberY(double d) { mdCheckNumberY = d; }
    public void setBackX(double d) { mdBackX = d; }
    public void setBackY(double d) { mdBackY = d; }
    public void setIsIssueLocalityApplying(boolean b) { mbIsIssueLocalityApplying = b; }
    public void setIsRecordNumberApplying(boolean b) { mbIsRecordNumberApplying = b; }
    public void setIsCheckNumberApplying(boolean b) { mbIsCheckNumberApplying = b; }
    public void setIsRotate(boolean b) { mbIsRotate = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkCheckFormatId() { return mnPkCheckFormatId; }
    public java.lang.String getCheckFormat() { return msCheckFormat; }
    public double getDateX() { return mdDateX; }
    public double getDateY() { return mdDateY; }
    public double getBeneficiaryX() { return mdBeneficiaryX; }
    public double getBeneficiaryY() { return mdBeneficiaryY; }
    public double getBeneficiaryAccountX() { return mdBeneficiaryAccountX; }
    public double getBeneficiaryAccountY() { return mdBeneficiaryAccountY; }
    public double getValueX() { return mdValueX; }
    public double getValueY() { return mdValueY; }
    public double getValueTextX() { return mdValueTextX; }
    public double getValueTextY() { return mdValueTextY; }
    public double getIssueLocalityX() { return mdIssueLocalityX; }
    public double getIssueLocalityY() { return mdIssueLocalityY; }
    public double getRecordNumberX() { return mdRecordNumberX; }
    public double getRecordNumberY() { return mdRecordNumberY; }
    public double getCheckNumberX() { return mdCheckNumberX; }
    public double getCheckNumberY() { return mdCheckNumberY; }
    public double getBackX() { return mdBackX; }
    public double getBackY() { return mdBackY; }
    public boolean getIsIssueLocalityApplying() { return mbIsIssueLocalityApplying; }
    public boolean getIsRecordNumberApplying() { return mbIsRecordNumberApplying; }
    public boolean getIsCheckNumberApplying() { return mbIsCheckNumberApplying; }
    public boolean getIsRotate() { return mbIsRotate; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkCheckFormatId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkCheckFormatId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkCheckFormatId = 0;
        msCheckFormat = "";
        mdDateX = 0;
        mdDateY = 0;
        mdBeneficiaryX = 0;
        mdBeneficiaryY = 0;
        mdBeneficiaryAccountX = 0;
        mdBeneficiaryAccountY = 0;
        mdValueX = 0;
        mdValueY = 0;
        mdValueTextX = 0;
        mdValueTextY = 0;
        mdIssueLocalityX = 0;
        mdIssueLocalityY = 0;
        mdRecordNumberX = 0;
        mdRecordNumberY = 0;
        mdCheckNumberX = 0;
        mdCheckNumberY = 0;
        mdBackX = 0;
        mdBackY = 0;
        mbIsIssueLocalityApplying = false;
        mbIsRecordNumberApplying = false;
        mbIsCheckNumberApplying = false;
        mbIsRotate = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.finu_check_fmt WHERE id_check_fmt = " + key[0]  + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkCheckFormatId = resultSet.getInt("id_check_fmt");
                msCheckFormat = resultSet.getString("check_fmt");
                mdDateX = resultSet.getDouble("dt_x");
                mdDateY = resultSet.getDouble("dt_y");
                mdBeneficiaryX = resultSet.getDouble("benef_x");
                mdBeneficiaryY = resultSet.getDouble("benef_y");
                mdBeneficiaryAccountX = resultSet.getDouble("benef_acc_x");
                mdBeneficiaryAccountY = resultSet.getDouble("benef_acc_y");
                mdValueX = resultSet.getDouble("val_x");
                mdValueY = resultSet.getDouble("val_y");
                mdValueTextX = resultSet.getDouble("val_txt_x");
                mdValueTextY = resultSet.getDouble("val_txt_y");
                mdIssueLocalityX = resultSet.getDouble("iss_loc_x");
                mdIssueLocalityY = resultSet.getDouble("iss_loc_y");
                mdRecordNumberX = resultSet.getDouble("num_rec_x");
                mdRecordNumberY = resultSet.getDouble("num_rec_y");
                mdCheckNumberX = resultSet.getDouble("num_check_x");
                mdCheckNumberY = resultSet.getDouble("num_check_y");
                mdBackX = resultSet.getDouble("back_x");
                mdBackY = resultSet.getDouble("back_y");
                mbIsIssueLocalityApplying = resultSet.getBoolean("b_iss_loc");
                mbIsRecordNumberApplying = resultSet.getBoolean("b_num_rec");
                mbIsCheckNumberApplying = resultSet.getBoolean("b_num_check");
                mbIsRotate = resultSet.getBoolean("b_rot");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

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
                    "{ CALL erp.finu_check_fmt_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkCheckFormatId);
            callableStatement.setString(nParam++, msCheckFormat);
            callableStatement.setDouble(nParam++, mdDateX);
            callableStatement.setDouble(nParam++, mdDateY);
            callableStatement.setDouble(nParam++, mdBeneficiaryX);
            callableStatement.setDouble(nParam++, mdBeneficiaryY);
            callableStatement.setDouble(nParam++, mdBeneficiaryAccountX);
            callableStatement.setDouble(nParam++, mdBeneficiaryAccountY);
            callableStatement.setDouble(nParam++, mdValueX);
            callableStatement.setDouble(nParam++, mdValueY);
            callableStatement.setDouble(nParam++, mdValueTextX);
            callableStatement.setDouble(nParam++, mdValueTextY);
            callableStatement.setDouble(nParam++, mdIssueLocalityX);
            callableStatement.setDouble(nParam++, mdIssueLocalityY);
            callableStatement.setDouble(nParam++, mdRecordNumberX);
            callableStatement.setDouble(nParam++, mdRecordNumberY);
            callableStatement.setDouble(nParam++, mdCheckNumberX);
            callableStatement.setDouble(nParam++, mdCheckNumberY);
            callableStatement.setDouble(nParam++, mdBackX);
            callableStatement.setDouble(nParam++, mdBackY);
            callableStatement.setBoolean(nParam++, mbIsIssueLocalityApplying);
            callableStatement.setBoolean(nParam++, mbIsRecordNumberApplying);
            callableStatement.setBoolean(nParam++, mbIsCheckNumberApplying);
            callableStatement.setBoolean(nParam++, mbIsRotate);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.CHAR);
            callableStatement.execute();

            mnPkCheckFormatId = callableStatement.getInt(nParam - 3);
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
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
