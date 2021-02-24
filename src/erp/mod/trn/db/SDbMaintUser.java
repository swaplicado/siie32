/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Gil De Jesús, Sergio Flores, Claudio Peña
 */
public class SDbMaintUser extends SDbRegistryUser {
    
    protected int mnPkMaintUserId;
    protected java.sql.Blob moFingerprint_n;
    protected int mnPinNumber;
    protected boolean mbEmployee;
    protected boolean mbContractor;
    protected boolean mbToolsMaintProvider;
    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected byte[] maAuxFingerprintBytes;
    
    private boolean testDelete(SGuiSession session) throws Exception {
        ResultSet resultSet = null;
        boolean canDelete = false;
        int mnYear = session.getCurrentYear();
        String date = SLibUtils.DbmsDateFormatDate.format(SLibTimeUtils.getEndOfYear(session.getSystemDate()));
        
        msSql = "SELECT count(*), " +
                "SUM(s.mov_in) AS f_mov_i, " +
                "SUM(s.mov_out) AS f_mov_o, " +
                "SUM(s.mov_in - s.mov_out) AS f_stk " +
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON i.id_item = s.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON u.id_unit = s.id_unit " +
                "INNER JOIN trn_stk_cfg AS sc ON sc.id_item = s.id_item AND sc.id_unit = s.id_unit AND sc.id_cob = s.id_cob AND sc.id_wh = s.id_wh " +
                "INNER JOIN trn_diog AS d ON d.id_year = s.fid_diog_year AND d.id_doc = s.fid_diog_doc " + // xxx1
                "INNER JOIN trn_maint_user_supv AS supv ON d.fid_maint_user_supv = supv.id_maint_user_supv " +
                "LEFT OUTER JOIN erp.bpsu_bp AS b ON b.id_bp = d.fid_maint_user_n " +
                "WHERE NOT s.b_del AND s.id_year = " + mnYear + " AND s.dt <= '" + date + "' " +
                "AND s.id_wh BETWEEN " + SModSysConsts.TRNX_MAINT_TOOL_AVL + " AND " + SModSysConsts.TRNX_MAINT_TOOL_MAINT + " " +
                "AND b.id_bp = " + getPkMaintUserId() + " " +
                "GROUP BY s.id_item, s.id_unit, i.item_key, i.item, u.symbol, sc.qty_min, sc.rop, sc.qty_max, b.id_bp, b.bp" + " " +
                "HAVING f_stk <> 0 " +
                "ORDER BY i.item_key, i.item, s.id_item, u.symbol, s.id_unit, b.bp, b.id_bp" + " ";
                
                resultSet = session.getStatement().executeQuery(msSql);

        if (!resultSet.next()) {
            canDelete = true;
        }
        
        return canDelete;
    }

    public SDbMaintUser() {
        super(SModConsts.TRN_MAINT_USER);
    }

    public void setPkMaintUserId(int n) { mnPkMaintUserId = n; }
    public void setFingerprint_n(java.sql.Blob o) { moFingerprint_n = o; }
    public void setPinNumber(int n) { mnPinNumber = n; }
    public void setEmployee(boolean b) { mbEmployee = b; }
    public void setContractor(boolean b) { mbContractor = b; }
    public void setToolsMaintProvider(boolean b) { mbToolsMaintProvider = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkMaintUserId() { return mnPkMaintUserId; }
    public java.sql.Blob getFingerprint_n() { return moFingerprint_n; }
    public int getPinNumber() { return mnPinNumber; }
    public boolean isEmployee() { return mbEmployee; }
    public boolean isContractor() { return mbContractor; }
    public boolean isToolsMaintProvider() { return mbToolsMaintProvider; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxFingerprintBytes(byte[] bytes) { maAuxFingerprintBytes = bytes; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMaintUserId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMaintUserId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMaintUserId = 0;
        moFingerprint_n = null;
        mnPinNumber = 0;
        mbEmployee = false;
        mbContractor = false;
        mbToolsMaintProvider = false;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maAuxFingerprintBytes = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_maint_user = " + mnPkMaintUserId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_maint_user = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession sgs) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            mnPkMaintUserId = resultSet.getInt("id_maint_user");
            moFingerprint_n = resultSet.getBlob("fingerprint_n");
            mnPinNumber = resultSet.getInt("pin_number");
            mbEmployee = resultSet.getBoolean("b_employee");
            mbContractor = resultSet.getBoolean("b_contractor");
            mbToolsMaintProvider = resultSet.getBoolean("b_tool_maint_prov");
            mbDeleted = resultSet.getBoolean("b_del");
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
        
        verifyRegistryNew(session);

        if (mbRegistryNew) {
            //computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMaintUserId + ", " + 
                    "NULL, " + 
                    mnPinNumber + ", " + 
                    (mbEmployee ? 1 : 0) + ", " + 
                    (mbContractor ? 1 : 0) + ", " + 
                    (mbToolsMaintProvider ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_maint_user = " + mnPkMaintUserId + ", " +
                    //"fingerprint_n = " + moFingerprint_n + ", " +
                    "pin_number = " + (mnPinNumber) + ", " +
                    "b_employee = " + (mbEmployee ? 1 : 0) + ", " +
                    "b_contractor = " + (mbContractor ? 1 : 0) + ", " +
                    "b_tool_maint_prov = " + (mbToolsMaintProvider ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save fingerprint:
        PreparedStatement preparedStatement = session.getStatement().getConnection().prepareStatement("UPDATE " + getSqlTable() + " SET fingerprint_n = ? " + getSqlWhere());
        if (maAuxFingerprintBytes == null) {
            preparedStatement.setNull(1, Types.BLOB);
        }
        else {
            preparedStatement.setBlob(1, new ByteArrayInputStream(maAuxFingerprintBytes));
        }
        preparedStatement.execute();
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaintUser clone() throws CloneNotSupportedException {
        SDbMaintUser registry = new SDbMaintUser();

        registry.setPkMaintUserId(this.getPkMaintUserId());
        registry.setFingerprint_n(this.getFingerprint_n());
        registry.setPinNumber(this.getPinNumber());
        registry.setEmployee(this.isEmployee());
        registry.setContractor(this.isContractor());
        registry.setToolsMaintProvider(this.isToolsMaintProvider());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setAuxFingerprintBytes(maAuxFingerprintBytes == null ? null : maAuxFingerprintBytes.clone());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        boolean canDelete = super.canDelete(session);

        if (canDelete) {
            if (!testDelete(session)) {
                canDelete = false;
                msQueryResult = "¡No es posible eliminar el usuario, tiene documentos de invetarios vigentes!";
            }
        }
        return canDelete;
    }
}
