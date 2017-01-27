/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.qty.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Uriel Castañeda
 */
public class SDbQualityLotApr extends SDbRegistryUser {

    protected int mnPkLotApprovedId;
    protected Date mtDate;
    protected String msLot;
    protected boolean mbDeleted;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkBizPartnerId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbQualityLotApr() {
        super(SModConsts.QTLY_LOT);
    }
    
    /*
     * Public methods
     */

    public void setPkLotApprovedId(int n) { mnPkLotApprovedId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setLot(String s) { msLot = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    
    public int getPkLotApprovedId() { return mnPkLotApprovedId; }
    public Date getDate() { return mtDate; }
    public String getLot() { return msLot; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLotApprovedId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLotApprovedId = 0;
        mtDate = null;
        msLot = "";
        mbDeleted = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkBizPartnerId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;       
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_lot_apr = " + mnPkLotApprovedId ;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lot_apr = " + pk[0] ;
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLotApprovedId = 0;

        msSql = "SELECT COALESCE(MAX(id_lot_apr), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLotApprovedId = resultSet.getInt(1);
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
            mnPkLotApprovedId = resultSet.getInt("id_lot_apr");
            mtDate = resultSet.getDate("dt");
            msLot = resultSet.getString("lot");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUnitId = resultSet.getInt("fk_unit");
            mnFkBizPartnerId = resultSet.getInt("fk_bp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLotApprovedId + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    "'" + msLot + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkItemId + ", " + 
                    mnFkUnitId + ", " + 
                    mnFkBizPartnerId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ") ";

        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_lot_apr = " + mnPkLotApprovedId + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "lot = '" + msLot + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_unit = " + mnFkUnitId + ", " +
                    "fk_bp = " + mnFkBizPartnerId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + ", " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbQualityLotApr clone() throws CloneNotSupportedException {
        SDbQualityLotApr registry = new SDbQualityLotApr();

        registry.setPkLotApprovedId(this.getPkLotApprovedId());
        registry.setDate(this.getDate());
        registry.setLot(this.getLot());
        registry.setDeleted(this.isDeleted());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkBizPartnerId(this.getFkBizPartnerId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        return registry;
    }

}
