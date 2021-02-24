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
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas, Claudio Peña
 */
public class SDataStockClosing extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected Vector<SDataDiog> mvDbmsDiogs;
    
    public SDataStockClosing() {
        super(SDataConstants.TRN_DIOG);
        mvDbmsDiogs = new Vector<>();
        reset();
    }
    
    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    
    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public Vector<SDataDiog> getDbmsDiogs() { return mvDbmsDiogs; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Object getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void reset() {
        super.resetRegistry();
        
        mnPkYearId = 0;
        mnPkDocId = 0;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        String sql = "";
        ResultSet rs = null;
        Statement stQry = null;
        Statement stUpd = null;
        Vector<SDataDiog> diogs = new Vector<>();
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            stQry = connection.createStatement();
            stUpd = connection.createStatement();
            
            sql = "SELECT id_doc FROM trn_diog WHERE " +
                    "id_year = " + mnPkYearId + " AND b_sys = 1 AND b_del = 0 AND " + 
                    "fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_ADJ_INV [0] + " AND " +
                    "fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_ADJ_INV [1] + " AND " +
                    "fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_ADJ_INV [2] + " AND " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(SLibTimeUtilities.createDate(mnPkYearId, 1, 1)) + "' ";
            rs = stQry.executeQuery(sql);
            
            while (rs.next()) {
                SDataDiog diog = new SDataDiog();
                if (diog.read(new int[] { mnPkYearId, rs.getInt("id_doc") }, stUpd) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    diogs.add(diog);
                }
            }
            
            for (SDataDiog diog : diogs) {
                diog.setIsDeleted(true);
                if (diog.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
                else {
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }
            }
            
            for (SDataDiog diog : mvDbmsDiogs) {
                if (diog.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
                else {
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }
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
        return null;
    }
}
