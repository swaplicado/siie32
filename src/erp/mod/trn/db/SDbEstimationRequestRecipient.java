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
public class SDbEstimationRequestRecipient extends SDbRegistryUser {
    
    protected int mnPkEstimationRequestId;
    protected int mnPkEstimationRequestRecipientId;
    protected String msProviderName;
    protected String msMailsTo;
    protected String msMailsCc;
    protected String msMailsCco;
    protected String msSubject;
    protected String msBody;
    //protected boolean mbDeleted;
    protected int mnFkBusinessPartnerId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public SDbEstimationRequestRecipient() {
        super(SModConsts.TRN_EST_REQ_REC);
    }

    public void setPkEstimationRequestId(int n) { mnPkEstimationRequestId = n; }
    public void setPkEstimationRequestRecipientId(int n) { mnPkEstimationRequestRecipientId = n; }
    public void setProviderName(String s) { msProviderName = s; }
    public void setMailsTo(String s) { msMailsTo = s; }
    public void setMailsCc(String s) { msMailsCc = s; }
    public void setMailsCco(String s) { msMailsCco = s; }
    public void setSubject(String s) { msSubject = s; }
    public void setBody(String s) { msBody = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBusinessPartnerId_n(int n) { mnFkBusinessPartnerId_n = n; }

    public int getPkEstimationRequestId() { return mnPkEstimationRequestId; }
    public int getPkEstimationRequestRecipientId() { return mnPkEstimationRequestRecipientId; }
    public String getProviderName() { return msProviderName; }
    public String getMailsTo() { return msMailsTo; }
    public String getMailsCc() { return msMailsCc; }
    public String getMailsCco() { return msMailsCco; }
    public String getSubject() { return msSubject; }
    public String getBody() { return msBody; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBusinessPartnerId_n() { return mnFkBusinessPartnerId_n; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEstimationRequestId = pk[0];
        mnPkEstimationRequestRecipientId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEstimationRequestId, mnPkEstimationRequestRecipientId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkEstimationRequestId = 0;
        mnPkEstimationRequestRecipientId = 0;
        msProviderName = "";
        msMailsTo = "";
        msMailsCc = "";
        msMailsCco = "";
        msSubject = "";
        msBody = null;
        mbDeleted = false;
        mnFkBusinessPartnerId_n = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_est_req = " + mnPkEstimationRequestId + " AND id_rec = " + mnPkEstimationRequestRecipientId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_est_req = " + pk[0] + " AND id_rec = " + pk[1];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEstimationRequestRecipientId = 0;

        msSql = "SELECT COALESCE(MAX(id_rec), 0) + 1 FROM " + getSqlTable() + " WHERE id_est_req = " + mnPkEstimationRequestId;
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEstimationRequestRecipientId = resultSet.getInt(1);
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
            mnPkEstimationRequestRecipientId = resultSet.getInt("id_rec");
            msProviderName = resultSet.getString("prov_name");
            msMailsTo = resultSet.getString("mails_to");
            msMailsCc = resultSet.getString("mails_cc");
            msMailsCco = resultSet.getString("mails_cco");
            msSubject = resultSet.getString("subject");
            msBody = resultSet.getString("body");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBusinessPartnerId_n = resultSet.getInt("fk_bp_n");
            
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
                    mnPkEstimationRequestRecipientId + ", " + 
                    "'" + msProviderName + "', " + 
                    "'" + msMailsTo + "', " + 
                    "'" + msMailsCc + "', " + 
                    "'" + msMailsCco + "', " + 
                    "'" + msSubject + "', " + 
                    "'" + msBody + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mnFkBusinessPartnerId_n <= 0 ? "null" : mnFkBusinessPartnerId_n) + " " + 
                ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    "id_est_req = " + mnPkEstimationRequestId + ", " +
                    "id_rec = " + mnPkEstimationRequestRecipientId + ", " +
                    "prov_name = '" + msProviderName + "', " +
                    "mails_to = '" + msMailsTo + "', " +
                    "mails_cc = '" + msMailsCc + "', " +
                    "mails_cco = '" + msMailsCco + "', " +
                    "subject = '" + msSubject + "', " +
                    "body = '" + msBody + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_bp_n = " + (mnFkBusinessPartnerId_n <= 0 ? "null" : mnFkBusinessPartnerId_n) + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Finish registry saving:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbEstimationRequestRecipient  clone() throws CloneNotSupportedException {
        SDbEstimationRequestRecipient registry = new SDbEstimationRequestRecipient();
        
        registry.setPkEstimationRequestId(this.getPkEstimationRequestId());
        registry.setPkEstimationRequestRecipientId(this.getPkEstimationRequestRecipientId());
        registry.setProviderName(this.getProviderName());
        registry.setMailsTo(this.getMailsTo());
        registry.setMailsCc(this.getMailsCc());
        registry.setMailsCco(this.getMailsCco());
        registry.setSubject(this.getSubject());
        registry.setBody(this.getBody());
        registry.setDeleted(this.isDeleted());
        registry.setFkBusinessPartnerId_n(this.getFkBusinessPartnerId_n());

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
