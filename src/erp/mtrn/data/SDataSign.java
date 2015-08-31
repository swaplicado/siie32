/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Juan Manuel Barajas Cárabes
 */
public class SDataSign extends erp.lib.data.SDataRegistry implements java.io.Serializable  {

    protected int mnPkYearId;
    protected int mnPkPacId;
    protected int mnPkMoveId;
    protected java.util.Date mtDate;
    protected int mnMoveIn;
    protected int mnMoveOut;
    protected boolean mbIsDeleted;
    protected int mnFkSignCategoryId;
    protected int mnFkSignTypeId;
    protected int mnFkCfdId_n;

    public SDataSign() {
        super(SDataConstants.TRN_SIGN);
        reset();
    }

   public void setPkYearId(int n) { mnPkYearId = n; }
   public void setPkPacId(int n) { mnPkPacId = n; }
   public void setPkMoveId(int n) { mnPkMoveId = n; }
   public void setDate(java.util.Date t) { mtDate = t; }
   public void setMoveIn(int n) { mnMoveIn = n; }
   public void setMoveOut(int n) { mnMoveOut = n; }
   public void setIsDeleted(boolean b) { mbIsDeleted = b; }
   public void setFkSignCategoryId(int n) { mnFkSignCategoryId = n; }
   public void setFkSignTypeId(int n) { mnFkSignTypeId = n; }
   public void setFkCfdId_n(int n) { mnFkCfdId_n = n; }

   public int getPkYearId() { return mnPkYearId; }
   public int getPkPacId() { return mnPkPacId; }
   public int getPkMoveId() { return mnPkMoveId; }
   public java.util.Date getDate() { return mtDate; }
   public int getMoveIn() { return mnMoveIn; }
   public int getMoveOut() { return mnMoveOut; }
   public boolean getIsDeleted() { return mbIsDeleted; }
   public int getFkSignCategoryId() { return mnFkSignCategoryId; }
   public int getFkSignTypeId() { return mnFkSignTypeId; }
   public int getFkCfdId_n() { return mnFkCfdId_n; }

   @Override
   public void setPrimaryKey(Object pk) {
       mnPkYearId = ((int[]) pk)[0];
       mnPkMoveId = ((int[]) pk)[1];
   }

   @Override
   public Object getPrimaryKey() {
       return new int[] { mnPkYearId, mnPkPacId, mnPkMoveId };
   }

   @Override
   public void reset() {
       super.resetRegistry();

       mnPkYearId = 0;
       mnPkPacId = 0;
       mnPkMoveId = 0;
       mtDate = null;
       mnMoveIn = 0;
       mnMoveOut = 0;
       mbIsDeleted = false;
       mnFkSignCategoryId = 0;
       mnFkSignTypeId = 0;
       mnFkCfdId_n = 0;
   }

    @Override
    public int read(Object pk, Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM trn_sign WHERE id_year = " + key[0] + " AND id_pac = " + key[1] + " AND id_mov = " + key[2] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkYearId = resultSet.getInt("id_year");
                mnPkPacId = resultSet.getInt("id_pac");
                mnPkMoveId = resultSet.getInt("id_mov");
                mtDate = resultSet.getDate("dt");
                mnMoveIn = resultSet.getInt("mov_in");
                mnMoveOut = resultSet.getInt("mov_out");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkSignCategoryId = resultSet.getInt("fid_ct_sign");
                mnFkSignTypeId = resultSet.getInt("fid_tp_sign");
                mnFkCfdId_n = resultSet.getInt("fid_cfd_n");
            }
            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
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
    public int save(Connection connection) {
        int nParam = 0;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            nParam = 1;
            callableStatement = connection.prepareCall(
                    "{ CALL trn_sign_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkYearId);
            callableStatement.setInt(nParam++, mnPkPacId);
            callableStatement.setInt(nParam++, mnPkMoveId);
            callableStatement.setDate(nParam++, new java.sql.Date(mtDate.getTime()));
            callableStatement.setInt(nParam++, mnMoveIn);
            callableStatement.setInt(nParam++, mnMoveOut);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkSignCategoryId);
            callableStatement.setInt(nParam++, mnFkSignTypeId);
            if (mnFkCfdId_n > 0) callableStatement.setInt(nParam++, mnFkCfdId_n); else callableStatement.setNull(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
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
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }
    
    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
