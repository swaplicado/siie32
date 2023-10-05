package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbEstimationRequestEntry extends SDbRegistryUser {
    
    protected int mnPkEstimationRequestId;
    protected int mnPkEstimationRequestEtyId;
    protected double mdQuantity;
    //protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkMatRequestId_n;
    protected int mnFkMatRequestEntryId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbEstimationRequestEntry() {
        super(SModConsts.TRN_EST_REQ_ETY);
    }

    public void setPkEstimationRequestId(int n) { mnPkEstimationRequestId = n; }
    public void setPkEstimationRequestEtyId(int n) { mnPkEstimationRequestEtyId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkMatRequestId_n(int n) { mnFkMatRequestId_n = n; }
    public void setFkMatRequestEntryId_n(int n) { mnFkMatRequestEntryId_n = n; }

    public int getPkEstimationRequestId() { return mnPkEstimationRequestId; }
    public int getPkEstimationRequestEtyId() { return mnPkEstimationRequestEtyId; }
    public double getQuantity() { return mdQuantity; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkMatRequestId_n() { return mnFkMatRequestId_n; }
    public int getFkMatRequestEntryId_n() { return mnFkMatRequestEntryId_n; }
    
    public int[] getKeyMaterialRequestEntry() { return new int[] { mnFkMatRequestId_n, mnFkMatRequestEntryId_n }; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEstimationRequestId = pk[0];
        mnPkEstimationRequestEtyId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEstimationRequestId, mnPkEstimationRequestEtyId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkEstimationRequestId = 0;
        mnPkEstimationRequestEtyId = 0;
        mdQuantity = 0;
        mbDeleted = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkMatRequestId_n = 0;
        mnFkMatRequestEntryId_n = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_est_req = " + mnPkEstimationRequestId + " AND id_ety = " + mnPkEstimationRequestEtyId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_est_req = " + pk[0] + " AND id_ety = " + pk[1];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEstimationRequestEtyId = 0;

        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " WHERE id_est_req = " + mnPkEstimationRequestId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEstimationRequestEtyId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkEstimationRequestId = resultSet.getInt("id_est_req");
            mnPkEstimationRequestEtyId = resultSet.getInt("id_ety");
            mdQuantity = resultSet.getDouble("qty");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkMatRequestId_n = resultSet.getInt("fk_mat_req_n");
            mnFkMatRequestEntryId_n = resultSet.getInt("fk_mat_req_ety_n");
            
            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEstimationRequestId + ", " + 
                    mnPkEstimationRequestEtyId + ", " + 
                    mdQuantity + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    mnFkMatRequestId_n + ", " + 
                    mnFkMatRequestEntryId_n + " " + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
//                    "id_est_req = " + mnPkEstimationRequestId + ", " +
                    "id_ety = " + mnPkEstimationRequestEtyId + ", " +
                    "qty = " + mdQuantity + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_mat_req_n = " + mnFkMatRequestId_n + ", " +
                    "fk_mat_req_ety_n = " + mnFkMatRequestEntryId_n + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Finish registry saving:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbEstimationRequestEntry  clone() throws CloneNotSupportedException {
        SDbEstimationRequestEntry registry = new SDbEstimationRequestEntry();
        
        registry.setPkEstimationRequestId(this.getPkEstimationRequestId());
        registry.setPkEstimationRequestEtyId(this.getPkEstimationRequestEtyId());
        registry.setQuantity(this.getQuantity());
        registry.setDeleted(this.isDeleted());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkMatRequestId_n(this.getFkMatRequestId_n());
        registry.setFkMatRequestEntryId_n(this.getFkMatRequestEntryId_n());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        
        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();
        
        save(session);
    }
}
