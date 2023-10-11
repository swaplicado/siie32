package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbEstimationRequest extends SDbRegistryUser {
    
    protected int mnPkEstimationRequestId;
    protected int mnNumber;
    //protected boolean mbDeleted;
    protected int mnFkMatRequestId_n;
    protected int mnFkUserId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUser;
//    protected Date mtTsUserUpdate;
    
    protected ArrayList<SDbEstimationRequestEntry> maChildEntries;
    protected ArrayList<SDbEstimationRequestRecipient> maRecipients;
    
    public SDbEstimationRequest() {
        super(SModConsts.TRN_EST_REQ);
    }
    
    private int getNextNumber(final SGuiSession session) throws Exception {
        int number = 0;
        ResultSet resultSet = null;
        
        msSql = "SELECT COALESCE(MAX(num), 0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ) + " "
                + "WHERE b_del = 0; ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            number = resultSet.getInt(1);
        }
        
        return ++number;
    }
    
    private ArrayList<SDbEstimationRequestEntry> readEntries(final SGuiSession session) throws Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<SDbEstimationRequestEntry> entries = new ArrayList<>();
        
        statement = session.getStatement().getConnection().createStatement();

        msSql = "SELECT id_ety FROM " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_ETY) + " " 
                + getSqlWhere()
                + "ORDER BY id_ety; ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbEstimationRequestEntry child = new SDbEstimationRequestEntry();
            child.read(session, new int[] { mnPkEstimationRequestId, resultSet.getInt(1) });
            entries.add(child);
        }
        
        return entries;
    }
    
    private ArrayList<SDbEstimationRequestRecipient> readRecipients(final SGuiSession session) throws Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<SDbEstimationRequestRecipient> entries = new ArrayList<>();
        
        statement = session.getStatement().getConnection().createStatement();

        msSql = "SELECT id_rec FROM " + SModConsts.TablesMap.get(SModConsts.TRN_EST_REQ_REC) + " " 
                + getSqlWhere()
                + "ORDER BY id_rec; ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbEstimationRequestRecipient child = new SDbEstimationRequestRecipient();
            child.read(session, new int[] { mnPkEstimationRequestId, resultSet.getInt(1) });
            entries.add(child);
        }
        
        return entries;
    }

    public void setPkEstimationRequestId(int n) { mnPkEstimationRequestId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkMatRequestId_n(int n) { mnFkMatRequestId_n = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkEstimationRequestId() { return mnPkEstimationRequestId; }
    public int getNumber() { return mnNumber; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkMatRequestId_n() { return mnFkMatRequestId_n; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }

    public ArrayList<SDbEstimationRequestEntry> getChildEntries() { return maChildEntries; }
    public ArrayList<SDbEstimationRequestRecipient> getChildRecipients() { return maRecipients; }
    
    public int[] getKeyMaterialRequest() { return new int[] { mnFkMatRequestId_n }; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEstimationRequestId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEstimationRequestId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkEstimationRequestId = 0;
        mnNumber = 0;
        mbDeleted = false;
        mnFkMatRequestId_n = 0;
        mnFkUserId = 0;
        mtTsUser = null;
        
        maChildEntries = new ArrayList<>();
        maRecipients = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_est_req = " + mnPkEstimationRequestId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_est_req = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEstimationRequestId = 0;

        msSql = "SELECT COALESCE(MAX(id_est_req), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEstimationRequestId = resultSet.getInt(1);
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
            mnNumber = resultSet.getInt("num");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkMatRequestId_n = resultSet.getInt("fk_mat_req_n");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");
            
            // Read aswell child registries:
            
            maChildEntries.addAll(readEntries(session));
            maRecipients.addAll(readRecipients(session));
            
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
            
            if (mnNumber == 0) {
                mnNumber = getNextNumber(session);
            }

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEstimationRequestId + ", " + 
                    mnNumber + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mnFkMatRequestId_n <= 0 ? "null" : mnFkMatRequestId_n) + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_est_req = " + mnPkEstimationRequestId + ", " +
                    "num = " + mnNumber + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_mat_req_n = " + mnFkMatRequestId_n + ", " +
                    "fk_usr = " + mnFkUserId + ", " +
                    "ts_usr = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Save aswell child registries:
        
        for (SDbEstimationRequestEntry child : maChildEntries) {
            child.setPkEstimationRequestId(mnPkEstimationRequestId);
            child.setRegistryNew(true);
            child.save(session);
        }
        
        // Save aswell recipients registries:
        
        for (SDbEstimationRequestRecipient child : maRecipients) {
            child.setPkEstimationRequestId(mnPkEstimationRequestId);
            child.setRegistryNew(true);
            child.save(session);
        }
        
        // Finish registry saving:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbEstimationRequest  clone() throws CloneNotSupportedException {
        SDbEstimationRequest registry = new SDbEstimationRequest();
        
        registry.setPkEstimationRequestId(this.getPkEstimationRequestId());
        registry.setNumber(this.getNumber());
        registry.setDeleted(this.isDeleted());
        registry.setFkMatRequestId_n(this.getFkMatRequestId_n());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUserInsert(this.getTsUserInsert());
        
        for (SDbEstimationRequestEntry child : maChildEntries) {
            registry.getChildEntries().add(child.clone());
        }
        
        for (SDbEstimationRequestRecipient child : maRecipients) {
            registry.getChildRecipients().add(child.clone());
        }

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
