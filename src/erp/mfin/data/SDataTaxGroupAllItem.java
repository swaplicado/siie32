/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import java.sql.ResultSet;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class SDataTaxGroupAllItem extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;

    protected java.util.Vector<erp.mfin.data.SDataTaxGroupItem> mvDbmsTaxGroupItems;

    public SDataTaxGroupAllItem() {
        super(SDataConstants.FINX_TAX_GRP_ALL_ITEM);
        mvDbmsTaxGroupItems = new Vector<SDataTaxGroupItem>();
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }

    public int getPkItemId() { return mnPkItemId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }

    public java.util.Vector<erp.mfin.data.SDataTaxGroupItem> getDbmsTaxGroupItems() { return mvDbmsTaxGroupItems; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;

        mvDbmsTaxGroupItems.clear();
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        Statement statementAux = null;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            setPrimaryKey(pk);
            statementAux = statement.getConnection().createStatement();

            // Read all tax groups for item:

            sql = "SELECT tgi.id_item, tgi.id_tax_reg, tgi.id_dt_start, tgi.b_del, " +
                    "tr.tax_reg, tg.tax_grp " +
                    "FROM fin_tax_grp_item AS tgi " +
                    "INNER JOIN erp.finu_tax_reg AS tr ON " +
                    "tgi.id_tax_reg = tr.id_tax_reg " +
                    "INNER JOIN fin_tax_grp AS tg ON " +
                    "tgi.fid_tax_grp = tg.id_tax_grp " +
                    "WHERE id_item = " + key[0] + " " +
                    "ORDER BY tr.tax_reg, tgi.id_tax_reg, " +
                    "tgi.id_dt_start, tg.tax_grp, tgi.fid_tax_grp, tgi.b_del ";
            resultSet = statementAux.executeQuery(sql);
            while (resultSet.next()) {
                SDataTaxGroupItem item = new SDataTaxGroupItem();
                if (item.read(new Object[] { resultSet.getInt("id_item"), resultSet.getInt("id_tax_reg"), resultSet.getDate("id_dt_start") }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mvDbmsTaxGroupItems.add(item);
                }
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
    public int save(java.sql.Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            mnDbmsErrorId = 0;
            msDbmsError = "";

            for (int i = 0; i < mvDbmsTaxGroupItems.size(); i++) {
                if (mvDbmsTaxGroupItems.get(i).getIsRegistryNew()) {
                    mvDbmsTaxGroupItems.get(i).setPkItemId(mnPkItemId);
                    mvDbmsTaxGroupItems.get(i).setFkUserNewId(mnFkUserNewId);
                }
                else {
                    mvDbmsTaxGroupItems.get(i).setFkUserEditId(mnFkUserEditId);
                }

                if (mvDbmsTaxGroupItems.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    mnDbmsErrorId = mvDbmsTaxGroupItems.get(i).getDbmsErrorId();
                    msDbmsError = mvDbmsTaxGroupItems.get(i).getDbmsError();
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                }
            }

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
        return null;
    }
}
