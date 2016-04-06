package erp.mod.trn.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbDelivery extends SDbRegistryUser {
    
    protected int mnPkDeliveryId;
    protected int mnNumber;
    protected Date mtDate;
    //protected boolean mbDeleted;
    protected int mnFkDpsYearId;
    protected int mnFkDpsDocId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbDeliveryEntry> maChildEntries;
    
    public SDbDelivery() {
        super(SModConsts.TRN_DVY);
    }
    
    private int getNextNumber(final SGuiSession session) throws Exception {
        int number = 0;
        ResultSet resultSet = null;
        
        msSql = "SELECT COALESCE(MAX(num), 0) "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DVY) + " "
                + "WHERE b_del = 0; ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            number = resultSet.getInt(1);
        }
        
        return ++number;
    }
    
    private ArrayList<SDbDeliveryEntry> readEntries(final SGuiSession session) throws Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        ArrayList<SDbDeliveryEntry> entries = new ArrayList<>();
        
        statement = session.getStatement().getConnection().createStatement();

        msSql = "SELECT id_ety FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DVY_ETY) + " " + getSqlWhere()
                + "ORDER BY id_ety; ";
        resultSet = statement.executeQuery(msSql);
        while (resultSet.next()) {
            SDbDeliveryEntry child = new SDbDeliveryEntry();
            child.read(session, new int[] { mnPkDeliveryId, resultSet.getInt(1) });
            entries.add(child);
        }
        
        return entries;
    }

    public void setPkDeliveryId(int n) { mnPkDeliveryId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDpsYearId(int n) { mnFkDpsYearId = n; }
    public void setFkDpsDocId(int n) { mnFkDpsDocId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDeliveryId() { return mnPkDeliveryId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkDpsYearId() { return mnFkDpsYearId; }
    public int getFkDpsDocId() { return mnFkDpsDocId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbDeliveryEntry> getChildEntries() { return maChildEntries; }
    
    public int[] getUtilKeyDps() { return new int[] { mnFkDpsYearId, mnFkDpsDocId }; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDeliveryId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDeliveryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkDeliveryId = 0;
        mnNumber = 0;
        mtDate = null;
        mbDeleted = false;
        mnFkDpsYearId = 0;
        mnFkDpsDocId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildEntries = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dvy = " + mnPkDeliveryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dvy = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDeliveryId = 0;

        msSql = "SELECT COALESCE(MAX(id_dvy), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDeliveryId = resultSet.getInt(1);
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
            mnPkDeliveryId = resultSet.getInt("id_dvy");
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkDpsYearId = resultSet.getInt("fk_dps_year");
            mnFkDpsDocId = resultSet.getInt("fk_dps_doc");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // Read aswell child registries:
            
            maChildEntries.addAll(readEntries(session));
            
            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        ArrayList<SDbDeliveryEntry> formerEntries = null;
        
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
                    mnPkDeliveryId + ", " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkDpsYearId + ", " + 
                    mnFkDpsDocId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_dvy = " + mnPkDeliveryId + ", " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_dps_year = " + mnFkDpsYearId + ", " +
                    "fk_dps_doc = " + mnFkDpsDocId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Save aswell child registries:
        
        // a) preserve former child registries:
        
        formerEntries = readEntries(session);
        
        // b) reset entry effect of former child registries:
        
        for (SDbDeliveryEntry entry : formerEntries) {
            entry.resetEntryEffect(session);
        }
        
        // c) delete former child registries:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DVY_ETY) + " " + getSqlWhere();
        session.getStatement().execute(msSql);
        
        // d) save new child registries:
        
        for (SDbDeliveryEntry child : maChildEntries) {
            child.setPkDeliveryId(mnPkDeliveryId);
            child.setRegistryNew(true);
            child.save(session);
        }
        
        // e) apply entry effect of new child registries:
        
        for (SDbDeliveryEntry child : maChildEntries) {
            child.applyEntryEffect(session);
        }
        
        // f) dispose entry effect of former child registries:
        
        for (SDbDeliveryEntry entry : formerEntries) {
            entry.disposeEntryEffect(session);
        }
        
        // Finish registry saving:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbDelivery  clone() throws CloneNotSupportedException {
        SDbDelivery registry = new SDbDelivery();
        
        registry.setPkDeliveryId(this.getPkDeliveryId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setDeleted(this.isDeleted());
        registry.setFkDpsYearId(this.getFkDpsYearId());
        registry.setFkDpsDocId(this.getFkDpsDocId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbDeliveryEntry child : maChildEntries) {
            registry.getChildEntries().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
