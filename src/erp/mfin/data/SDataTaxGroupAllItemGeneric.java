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
public class SDataTaxGroupAllItemGeneric extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemGenericId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;

    protected java.util.Vector<erp.mfin.data.SDataTaxGroupItemGeneric> mvDbmsTaxGroupItemGenerics;

    public SDataTaxGroupAllItemGeneric() {
        super(SDataConstants.FINX_TAX_GRP_ALL_IGEN);
        mvDbmsTaxGroupItemGenerics = new Vector<SDataTaxGroupItemGeneric>();
        reset();
    }

    public void setPkItemGenericId(int n) { mnPkItemGenericId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }

    public int getPkItemGenericId() { return mnPkItemGenericId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }

    public java.util.Vector<erp.mfin.data.SDataTaxGroupItemGeneric> getDbmsTaxGroupItemGenerics() { return mvDbmsTaxGroupItemGenerics; }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemGenericId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemGenericId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemGenericId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;

        mvDbmsTaxGroupItemGenerics.clear();
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

            // Read all tax groups for item generic:

            sql = "SELECT tgi.id_igen, tgi.id_tax_reg, tgi.id_dt_start, tgi.b_del, " +
                    "tr.tax_reg, tg.tax_grp " +
                    "FROM fin_tax_grp_igen AS tgi " +
                    "INNER JOIN erp.finu_tax_reg AS tr ON " +
                    "tgi.id_tax_reg = tr.id_tax_reg " +
                    "INNER JOIN fin_tax_grp AS tg ON " +
                    "tgi.fid_tax_grp = tg.id_tax_grp " +
                    "WHERE id_igen = " + key[0] + " " +
                    "ORDER BY tr.tax_reg, tgi.id_tax_reg, " +
                    "tgi.id_dt_start, tg.tax_grp, tgi.fid_tax_grp, tgi.b_del ";
            resultSet = statementAux.executeQuery(sql);
            while (resultSet.next()) {
                SDataTaxGroupItemGeneric itemGeneric = new SDataTaxGroupItemGeneric();
                if (itemGeneric.read(new Object[] { resultSet.getInt("id_igen"), resultSet.getInt("id_tax_reg"), resultSet.getDate("id_dt_start") }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    mvDbmsTaxGroupItemGenerics.add(itemGeneric);
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

            for (int i = 0; i < mvDbmsTaxGroupItemGenerics.size(); i++) {
                if (mvDbmsTaxGroupItemGenerics.get(i).getIsRegistryNew()) {
                    mvDbmsTaxGroupItemGenerics.get(i).setPkItemGenericId(mnPkItemGenericId);
                    mvDbmsTaxGroupItemGenerics.get(i).setFkUserNewId(mnFkUserNewId);
                }
                else {
                    mvDbmsTaxGroupItemGenerics.get(i).setFkUserEditId(mnFkUserEditId);
                }

                if (mvDbmsTaxGroupItemGenerics.get(i).save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                    mnDbmsErrorId = mvDbmsTaxGroupItemGenerics.get(i).getDbmsErrorId();
                    msDbmsError = mvDbmsTaxGroupItemGenerics.get(i).getDbmsError();
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
