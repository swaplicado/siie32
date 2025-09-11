/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mitm.data;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import sa.gui.util.SUtilConsts;

/**
 * Configuración personalizada de item vs. socio de negocios, con el enfoque producto vs. cliente.
 * 
 * @author Sergio Flores
 */
public class SDataItemCustomer extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkItemId;
    protected int mnPkBizPartnerId;
    protected int mnNumberOfStores;
    protected int mnNumberOfDistCenters;
    protected boolean mbIsCanEdit;
    protected boolean mbIsCanDelete;
    protected boolean mbIsDeleted;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;
    
    protected boolean mbOldIsDeleted;

    public SDataItemCustomer() {
        super(SDataConstants.ITMU_ITEM_CUS);
        reset();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setNumberOfStores(int n) { mnNumberOfStores = n; }
    public void setNumberOfDistCenters(int n) { mnNumberOfDistCenters = n; }
    public void setIsCanEdit(boolean b) { mbIsCanEdit = b; }
    public void setIsCanDelete(boolean b) { mbIsCanDelete = b; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getNumberOfStores() { return mnNumberOfStores; }
    public int getNumberOfDistCenters() { return mnNumberOfDistCenters; }
    public boolean getIsCanEdit() { return mbIsCanEdit; }
    public boolean getIsCanDelete() { return mbIsCanDelete; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setOldIsDeleted(boolean b) { mbOldIsDeleted = b; }
    public boolean getOldIsDeleted() { return mbOldIsDeleted; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkItemId = ((int[]) pk)[0];
        mnPkBizPartnerId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkBizPartnerId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkItemId = 0;
        mnPkBizPartnerId = 0;
        mnNumberOfStores = 0;
        mnNumberOfDistCenters = 0;
        mbIsCanEdit = false;
        mbIsCanDelete = false;
        mbIsDeleted = false;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;
        
        mbOldIsDeleted = false;
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql;
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.itmu_item_cus WHERE id_item = " + key[0] + " AND id_bp = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkItemId = resultSet.getInt("id_item");
                mnPkBizPartnerId = resultSet.getInt("id_bp");
                mnNumberOfStores = resultSet.getInt("num_stores");
                mnNumberOfDistCenters = resultSet.getInt("num_dist_centers");
                mbIsCanEdit = resultSet.getBoolean("b_can_edit");
                mbIsCanDelete = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("ts_new");
                mtUserEditTs = resultSet.getTimestamp("ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("ts_del");
                
                mbOldIsDeleted = mbIsDeleted;

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
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            try (Statement statement = connection.createStatement()) {
                boolean exists = false;
                String sql = "SELECT COUNT(*) AS _count FROM erp.itmu_item_cus WHERE id_item = " + mnPkItemId + " AND id_bp = " + mnPkBizPartnerId + " ";
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    exists = resultSet.getInt("_count") > 0;
                    
                    if (exists && mbIsRegistryNew) {
                        // combination cannot be created again:
                        
                        String msg = "";
                        
                        sql = "SELECT i.item, i.item_key, b.bp, bc.bp_key "
                                + "FROM erp.itmu_item_cus AS ib "
                                + "INNER JOIN erp.itmu_item AS i ON i.id_item = ib.id_item "
                                + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = ib.id_bp "
                                + "INNER JOIN erp.bpsu_bp_ct AS bc ON bc.id_bp = b.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " "
                                + "WHERE i.id_item = " + mnPkItemId + " AND b.id_bp = " + mnPkBizPartnerId + " ";
                        resultSet = statement.executeQuery(sql);
                        
                        if (resultSet.next()) {
                            msg = "- Cliente: " + resultSet.getString("b.bp") + " (" + resultSet.getString("bc.bp_key") + ")\n"
                                    + "- Producto: " + resultSet.getString("i.item") + " (" + resultSet.getString("i.item_key") + ")";
                        }
                        
                        mnDbmsErrorId = -1;
                        msDbmsError = "¡No se puede crear el registro! La combinación cliente-producto ya existe" + (msg.isEmpty() ? "." : ":\n" + msg) + "\n";
                        msDbmsError += "SUGERENCIAS:\n"
                                + "a) Intente crear una combinación nueva.\n"
                                + "b) Modifique la combinación existente mencionada.";
                        
                        throw new Exception(msDbmsError);
                    }
                }
                
                if (!exists) {
                    mnFkUserEditId = SUtilConsts.USR_NA_ID;
                    mnFkUserDeleteId = mbIsDeleted ? mnFkUserNewId : SUtilConsts.USR_NA_ID;
                    
                    sql = "INSERT INTO erp.itmu_item_cus VALUES ("
                            + mnPkItemId + ", "
                            + mnPkBizPartnerId + ", "
                            + mnNumberOfStores + ", "
                            + mnNumberOfDistCenters + ", "
                            + (mbIsCanEdit ? 1 : 0) + ", "
                            + (mbIsCanDelete ? 1 : 0) + ", "
                            + (mbIsDeleted ? 1 : 0) + ", "
                            + mnFkUserNewId + ", "
                            + mnFkUserEditId + ", "
                            + mnFkUserDeleteId + ", "
                            + "NOW(), "
                            + "NOW(), "
                            + "NOW());";
                }
                else {
                    if (mbOldIsDeleted != mbIsDeleted) {
                        mnFkUserDeleteId = mnFkUserEditId;
                    }
                    
                    sql = "UPDATE erp.itmu_item_cus SET "
                            + "num_stores=" + mnNumberOfStores + ", "
                            + "num_dist_centers=" + mnNumberOfDistCenters + ", "
                            + "b_can_edit=" + (mbIsCanEdit ? 1 : 0) + ", "
                            + "b_can_del=" + (mbIsCanDelete ? 1 : 0) + ", "
                            + "b_del=" + (mbIsDeleted ? 1 : 0) + ", "
                            //+ "fid_usr_new=" + mnFkUserNewId + ", "
                            + "fid_usr_edit=" + mnFkUserEditId + ", "
                            + (mbOldIsDeleted != mbIsDeleted ? "fid_usr_del=" + mnFkUserDeleteId + ", " : "")
                            //+ "ts_new=NOW(), "
                            + "ts_edit=NOW()"
                            + (mbOldIsDeleted != mbIsDeleted ? ", ts_del=NOW()" : "")
                            + " WHERE "
                            + "id_item=" + mnPkItemId + " "
                            + "AND id_bp=" + mnPkBizPartnerId + ";";
                }
                
                statement.execute(sql);

                mnDbmsErrorId = 0;
                msDbmsError = "";

                if (mnDbmsErrorId != 0) {
                    throw new Exception(msDbmsError);
                }
                else {
                    mbIsRegistryNew = false;
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }
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
