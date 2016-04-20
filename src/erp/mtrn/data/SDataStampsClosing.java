/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Juan Barajas
 */
public class SDataStampsClosing extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkPacId;
    protected int mnPkMoveId;
    protected int mnMoveIn;
    protected int mnMoveOut;
    
    protected java.text.SimpleDateFormat moAuxSimpleDateFormat;

    public SDataStampsClosing() {
        super(SDataConstants.TRN_SIGN);
        reset();
    }
    
    private void processStampsClosing(java.sql.Connection connection) throws java.lang.Exception {
        boolean isProcessOk = false;
        String sql = "";
        ResultSet rsQry = null;
        ResultSet rsUpd = null;
        ResultSet rsUpdAux = null;
        SDataSign dataSign = null;
        Vector<SDataSign> vSign = new Vector<>();
        Statement stQry = null;
        Statement stUpd = null;
        Statement stUpdAux = null;
        
        stQry = connection.createStatement();
        stUpd = connection.createStatement();
        stUpdAux = connection.createStatement();
        
        sql = "SELECT id_pac, b_pre_pay FROM trn_pac WHERE b_del = 0 ";
        rsQry = stQry.executeQuery(sql);
        
        while (rsQry.next()) {
            if (rsQry.getBoolean("b_pre_pay")) {
                mnPkPacId = rsQry.getInt("id_pac");
            
                sql = "SELECT id_mov FROM trn_sign WHERE " +
                        "id_year = " + mnPkYearId + " AND id_pac = " + mnPkPacId + " AND b_del = 0 AND " + 
                        "fid_ct_sign = " + SDataConstantsSys.TRNS_TP_SIGN_IN_INV_OPEN[0] + " AND " +
                        "fid_tp_sign = " + SDataConstantsSys.TRNS_TP_SIGN_IN_INV_OPEN[1] + " AND " +
                        "dt = '" + moAuxSimpleDateFormat.format(SLibTimeUtilities.createDate(mnPkYearId, 1, 1)) + "' ";
                rsUpdAux = stUpdAux.executeQuery(sql);

                while (rsUpdAux.next()) {
                    SDataSign sign = new SDataSign();
                    if (sign.read(new int[] { mnPkYearId, mnPkPacId, rsUpdAux.getInt("id_mov") }, connection.createStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        vSign.add(sign);
                    }
                }

                for (SDataSign sign : vSign) {
                    sign.setIsDeleted(true);
                    if (sign.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                    else {
                        mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                    }
                }
                
                sql = "SELECT COALESCE((SUM(mov_in) - SUM(mov_out)), 0) AS f_stamp " +
                    "FROM trn_sign WHERE b_del = 0 AND id_pac = " + mnPkPacId + " AND id_year = " + (mnPkYearId - 1) + " AND dt <= '" + moAuxSimpleDateFormat.format(SLibTimeUtilities.createDate(mnPkYearId - 1, 12, 31)) + "' ";

                rsUpd = stUpd.executeQuery(sql);
                if (rsUpd.next()) {
                    mnMoveIn = rsUpd.getInt("f_stamp");
                }
                
                if (mnMoveIn > 0) {
                    dataSign = new SDataSign();
                    dataSign.setPkYearId(mnPkYearId);
                    dataSign.setPkPacId(mnPkPacId);
                    dataSign.setDate(SLibTimeUtilities.createDate(mnPkYearId, 1, 1));
                    dataSign.setMoveIn(mnMoveIn);
                    dataSign.setMoveOut(mnMoveOut);
                    dataSign.setIsDeleted(false);
                    dataSign.setFkSignCategoryId(SDataConstantsSys.TRNS_TP_SIGN_IN_INV_OPEN[0]);
                    dataSign.setFkSignTypeId(SDataConstantsSys.TRNS_TP_SIGN_IN_INV_OPEN[1]);

                    if (dataSign.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                    }
                    
                    isProcessOk = true;
                }
            }
        }
        if(!isProcessOk) {
            throw new Exception("No existen timbres disponibles.");        
        }
    }
    
    @Override
    public void setPrimaryKey(Object pk) {
       mnPkYearId = ((int[]) pk)[0];
       mnPkPacId = ((int[]) pk)[1];
       mnPkMoveId = ((int[]) pk)[2];
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkPacId(int n) { mnPkPacId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setMoveIn(int n) { mnMoveIn = n; }
    public void setMoveOut(int n) { mnMoveOut = n; }
    
    public void setAuxSimpleDateFormat(java.text.SimpleDateFormat o) { moAuxSimpleDateFormat = o; }
    
    @Override
    public Object getPrimaryKey() {
       return new int[] { mnPkYearId, mnPkPacId, mnPkMoveId };
    }
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkPacId() { return mnPkPacId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public int getMoveIn() { return mnMoveIn; }
    public int getMoveOut() { return mnMoveOut; }
    
    public java.text.SimpleDateFormat getAuxSimpleDateFormat() { return moAuxSimpleDateFormat; }

    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkPacId = 0;
        mnPkMoveId = 0;
        mnMoveIn = 0;
        mnMoveOut = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            processStampsClosing(connection);

            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
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
        return null;
    }
}
