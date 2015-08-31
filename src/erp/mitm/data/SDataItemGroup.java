/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDataItemGroup extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemGroupId;
    protected java.lang.String msItemGroup;
    protected boolean mbIsFreePrice;
    protected boolean mbIsFreeDiscount;
    protected boolean mbIsFreeDiscountUnitary;
    protected boolean mbIsFreeDiscountEntry;
    protected boolean mbIsFreeDiscountDoc;
    protected boolean mbIsFreeCommissions;
    protected boolean mbIsDeleted;
    protected int mnFkItemFamilyId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected erp.mitm.data.SDataItemFamily moDbmsDataItemFamily;

    public SDataItemGroup() {
        super(SDataConstants.ITMU_IGRP);
        reset();
    }

    public void setPkItemGroupId(int n) { mnPkItemGroupId = n; }
    public void setItemGroup(java.lang.String s) { msItemGroup = s; }
    public void setIsFreePrice(boolean b) { mbIsFreePrice = b; }
    public void setIsFreeDiscount(boolean b) { mbIsFreeDiscount = b; }
    public void setIsFreeDiscountUnitary(boolean b) { mbIsFreeDiscountUnitary = b; }
    public void setIsFreeDiscountEntry(boolean b) { mbIsFreeDiscountEntry = b; }
    public void setIsFreeDiscountDoc(boolean b) { mbIsFreeDiscountDoc = b; }
    public void setIsFreeCommissions(boolean b) { mbIsFreeCommissions = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkItemFamilyId(int n) { mnFkItemFamilyId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkItemGroupId() { return mnPkItemGroupId; }
    public java.lang.String getItemGroup() { return msItemGroup; }
    public boolean getIsFreePrice() { return mbIsFreePrice; }
    public boolean getIsFreeDiscount() { return mbIsFreeDiscount; }
    public boolean getIsFreeDiscountUnitary() { return mbIsFreeDiscountUnitary; }
    public boolean getIsFreeDiscountEntry() { return mbIsFreeDiscountEntry; }
    public boolean getIsFreeDiscountDoc() { return mbIsFreeDiscountDoc; }
    public boolean getIsFreeCommissions() { return mbIsFreeCommissions; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkItemFamilyId() { return mnFkItemFamilyId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public erp.mitm.data.SDataItemFamily getDbmsDataItemFamily() { return moDbmsDataItemFamily; }
    public boolean getDbmsIsFreePrice() { return mbIsFreePrice || (moDbmsDataItemFamily == null ? false : moDbmsDataItemFamily.getDbmsIsFreePrice()); }
    public boolean getDbmsIsFreeDiscount() { return mbIsFreeDiscount || (moDbmsDataItemFamily == null ? false : moDbmsDataItemFamily.getDbmsIsFreeDiscount()); }
    public boolean getDbmsIsFreeDiscountUnitary() { return mbIsFreeDiscountUnitary || (moDbmsDataItemFamily == null ? false : moDbmsDataItemFamily.getDbmsIsFreeDiscountUnitary()); }
    public boolean getDbmsIsFreeDiscountEntry() { return mbIsFreeDiscountEntry || (moDbmsDataItemFamily == null ? false : moDbmsDataItemFamily.getDbmsIsFreeDiscountEntry()); }
    public boolean getDbmsIsFreeDiscountDoc() { return mbIsFreeDiscountDoc || (moDbmsDataItemFamily == null ? false : moDbmsDataItemFamily.getDbmsIsFreeDiscountDoc()); }
    public boolean getDbmsIsFreeCommissions() { return mbIsFreeCommissions || (moDbmsDataItemFamily == null ? false : moDbmsDataItemFamily.getDbmsIsFreeCommissions()); }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemGroupId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemGroupId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemGroupId = 0;
        msItemGroup = "";
        mbIsFreePrice = false;
        mbIsFreeDiscount = false;
        mbIsFreeDiscountUnitary = false;
        mbIsFreeDiscountEntry = false;
        mbIsFreeDiscountDoc = false;
        mbIsFreeCommissions = false;
        mbIsDeleted = false;
        mnFkItemFamilyId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        moDbmsDataItemFamily = null;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.itmu_igrp WHERE id_igrp = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemGroupId = resultSet.getInt("id_igrp");
                msItemGroup = resultSet.getString("igrp");
                mbIsFreePrice = resultSet.getBoolean("b_free_price");
                mbIsFreeDiscount = resultSet.getBoolean("b_free_disc");
                mbIsFreeDiscountUnitary = resultSet.getBoolean("b_free_disc_u");
                mbIsFreeDiscountEntry = resultSet.getBoolean("b_free_disc_ety");
                mbIsFreeDiscountDoc = resultSet.getBoolean("b_free_disc_doc");
                mbIsFreeCommissions = resultSet.getBoolean("b_free_comms");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkItemFamilyId = resultSet.getInt("fid_ifam");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");

                // Read aswell item family object:

                moDbmsDataItemFamily = new SDataItemFamily();
                if (moDbmsDataItemFamily.read(new int[] { mnFkItemFamilyId }, statement) != SLibConstants.DB_ACTION_READ_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
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
                    "{ CALL erp.itmu_igrp_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkItemGroupId);
            callableStatement.setString(nParam++, msItemGroup);
            callableStatement.setBoolean(nParam++, mbIsFreePrice);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscount);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountUnitary);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountEntry);
            callableStatement.setBoolean(nParam++, mbIsFreeDiscountDoc);
            callableStatement.setBoolean(nParam++, mbIsFreeCommissions);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkItemFamilyId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkItemGroupId = callableStatement.getInt(nParam - 3);
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
}
