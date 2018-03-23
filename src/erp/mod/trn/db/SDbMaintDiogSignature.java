/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbMaintDiogSignature extends SDbRegistryUser {

    protected int mnPkMaintDiogSignatureId;
    protected int mnFkDiogYearId;
    protected int mnFkDiogDocId;
    protected int mnFkMaintUserId;
    protected int mnFkMaintUserSupervisorId;
    /*
    protected int mnFkUserInsertId;
    protected Date mtTsUserInsert;
    */
    
    public SDbMaintDiogSignature() {
        super(SModConsts.TRN_MAINT_DIOG_SIG);
    }

    public void setPkMaintDiogSignatureId(int n) { mnPkMaintDiogSignatureId = n; }
    public void setFkDiogYearId(int n) { mnFkDiogYearId = n; }
    public void setFkDiogDocId(int n) { mnFkDiogDocId = n; }
    public void setFkMaintUserId(int n) { mnFkMaintUserId = n; }
    public void setFkMaintUserSupervisorId(int n) { mnFkMaintUserSupervisorId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }

    public int getPkMaintDiogSignatureId() { return mnPkMaintDiogSignatureId; }
    public int getFkDiogYearId() { return mnFkDiogYearId; }
    public int getFkDiogDocId() { return mnFkDiogDocId; }
    public int getFkMaintUserId() { return mnFkMaintUserId; }
    public int getFkMaintUserSupervisorId() { return mnFkMaintUserSupervisorId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMaintDiogSignatureId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMaintDiogSignatureId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkMaintDiogSignatureId = 0;
        mnFkDiogYearId = 0;
        mnFkDiogDocId = 0;
        mnFkMaintUserId = 0;
        mnFkMaintUserSupervisorId = 0;
        mnFkUserInsertId = 0;
        mtTsUserInsert = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_maint_diog_sig = " + mnPkMaintDiogSignatureId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_maint_diog_sig = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMaintDiogSignatureId = 0;

        msSql = "SELECT COALESCE(MAX(id_maint_diog_sig), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMaintDiogSignatureId = resultSet.getInt(1);
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
            mnPkMaintDiogSignatureId = resultSet.getInt("id_maint_diog_sig");
            mnFkDiogYearId = resultSet.getInt("fk_diog_year");
            mnFkDiogDocId = resultSet.getInt("fk_diog_doc");
            mnFkMaintUserId = resultSet.getInt("fid_maint_user");
            mnFkMaintUserSupervisorId = resultSet.getInt("fid_maint_user_supv");
            mnFkUserInsertId = resultSet.getInt("fk_usr");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr");

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

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkMaintDiogSignatureId + ", " + 
                    mnFkDiogYearId + ", " + 
                    mnFkDiogDocId + ", " + 
                    mnFkMaintUserId + ", " + 
                    mnFkMaintUserSupervisorId + ", " + 
                    mnFkUserInsertId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMaintDiogSignature clone() throws CloneNotSupportedException {
        SDbMaintDiogSignature registry = new SDbMaintDiogSignature();

        registry.setPkMaintDiogSignatureId(this.getPkMaintDiogSignatureId());
        registry.setFkDiogYearId(this.getFkDiogYearId());
        registry.setFkDiogDocId(this.getFkDiogDocId());
        registry.setFkMaintUserId(this.getFkMaintUserId());
        registry.setFkMaintUserSupervisorId(this.getFkMaintUserSupervisorId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setTsUserInsert(this.getTsUserInsert());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
