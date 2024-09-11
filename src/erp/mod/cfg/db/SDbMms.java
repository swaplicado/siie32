/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.cfg.db;

import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mod.SModConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.musr.data.SDataUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Isabel Servín
 */
public class SDbMms extends SDbRegistryUser {

    protected int mnPkMmsId;
    protected String msHost;
    protected String msPort;
    protected String msProtocol;
    protected String msUser;
    protected String msUserPassword;
    protected String msTextSubject;
    protected String msTextBody;
    protected String msRecipientTo;
    protected String msRecipientCarbonCopy;
    protected String msRecipientBlindCarbonCopy;
    protected String msArbitraryEmail;
    protected int mnMmsCase;
    protected boolean mbStartTls;
    protected boolean mbAuth;
    //protected boolean mbDeleted;
    protected int mnFkMmsTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msXtaMailReplyTo;
    
    public SDbMms() {
        super(SModConsts.CFG_MMS);
    }

    public void setPkMmsId(int n) { mnPkMmsId = n; }
    public void setHost(String s) { msHost = s; }
    public void setPort(String s) { msPort = s; }
    public void setProtocol(String s) { msProtocol = s; }
    public void setUser(String s) { msUser = s; }
    public void setUserPassword(String s) { msUserPassword = s; }
    public void setTextSubject(String s) { msTextSubject = s; }
    public void setTextBody(String s) { msTextBody = s; }
    public void setRecipientTo(String s) { msRecipientTo = s; }
    public void setRecipientCarbonCopy(String s) { msRecipientCarbonCopy = s; }
    public void setRecipientBlindCarbonCopy(String s) { msRecipientBlindCarbonCopy = s; }
    public void setArbitraryMail(String s) { msArbitraryEmail = s; }
    public void setMmsCase(int n) { mnMmsCase = n; }
    public void setStartTls(boolean b) { mbStartTls = b; }
    public void setAuth(boolean b) { mbAuth = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkMmsTypeId(int n) { mnFkMmsTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkMmsId() { return mnPkMmsId; }
    public String getHost() { return msHost; }
    public String getPort() { return msPort; }
    public String getProtocol() { return msProtocol; }
    public String getUser() { return msUser; }
    public String getUserPassword() { return msUserPassword; }
    public String getTextSubject() { return msTextSubject; }
    public String getTextBody() { return msTextBody; }
    public String getRecipientTo() { return msRecipientTo; }
    public String getRecipientCarbonCopy() { return msRecipientCarbonCopy; }
    public String getRecipientBlindCarbonCopy() { return msRecipientBlindCarbonCopy; }
    public String getArbitraryMail() { return msArbitraryEmail; }
    public int getMmsCase() { return mnMmsCase; }
    public boolean isStartTls() { return mbStartTls; }
    public boolean isAuth() { return mbAuth; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkMmsTypeId() { return mnFkMmsTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public void setXtaMailReplyTo(String s) { msXtaMailReplyTo = s; }
    
    public String getXtaMailReplyTo() { return msXtaMailReplyTo; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkMmsId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkMmsId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkMmsId = 0;
        msHost = "";
        msPort = "";
        msProtocol = "";
        msUser = "";
        msUserPassword = "";
        msTextSubject = "";
        msTextBody = "";
        msRecipientTo = "";
        msRecipientCarbonCopy = "";
        msRecipientBlindCarbonCopy = "";
        msArbitraryEmail = "";
        mnMmsCase = 0;
        mbStartTls = false;
        mbAuth = false;
        mbDeleted = false;
        mnFkMmsTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msXtaMailReplyTo = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_mms = " + mnPkMmsId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_mms = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMmsId = 0;

        msSql = "SELECT COALESCE(MAX(id_mms), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMmsId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        Statement statment = session.getDatabase().getConnection().createStatement();
        SDataUser user;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkMmsId = resultSet.getInt("id_mms");
            msHost = resultSet.getString("host");
            msPort = resultSet.getString("port");
            msProtocol = resultSet.getString("prot");
            msUser = resultSet.getString("usr");
            msUserPassword = resultSet.getString("usr_pswd");
            msTextSubject = resultSet.getString("txt_subj");
            msTextBody = resultSet.getString("txt_body");
            msRecipientTo = resultSet.getString("rec_to");
            msRecipientCarbonCopy = resultSet.getString("rec_cc");
            msRecipientBlindCarbonCopy = resultSet.getString("rec_bcc");
            msArbitraryEmail = resultSet.getString("arb_email");
            mnMmsCase = resultSet.getInt("mms_case");
            mbStartTls = resultSet.getBoolean("b_tls");
            mbAuth = resultSet.getBoolean("b_auth");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkMmsTypeId = resultSet.getInt("fk_tp_mms");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            switch (mnMmsCase) {
                case 1:
                    user = new SDataUser();
                    user.read(new int [] { session.getUser().getPkUserId() }, statment);
                    SDataBizPartnerBranchContact con = null;
                    if (user.getFkBizPartnerId_n() != 0) {
                        con = SBpsUtils.getBizParterContact(session, user.getFkBizPartnerId_n());
                    }
                    // El campo Email01 corresponde al email personal, el Email02 es el institucional
                    msXtaMailReplyTo = con != null && !con.getEmail02().isEmpty() ? con.getEmail02() : user.getEmail().isEmpty() ? msArbitraryEmail.isEmpty() ? msUser : msArbitraryEmail : user.getEmail(); // Empleado o usuario o arbitrario o servicio
                    break;
                case 2:
                    user = new SDataUser();
                    user.read(new int [] { session.getUser().getPkUserId() }, statment);
                    msXtaMailReplyTo = user.getEmail().isEmpty() ? msArbitraryEmail.isEmpty() ? msUser : msArbitraryEmail : user.getEmail(); // Usuario o arbitrario o servicio
                    break;
                case 3:
                    msXtaMailReplyTo = msArbitraryEmail.isEmpty() ? msUser : msArbitraryEmail; // Arbitrario o servicio
                    break;
                case 4:
                default:
                    msXtaMailReplyTo = msUser; // Servicio
                    break;
            }

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
                    mnPkMmsId + ", " + 
                    "'" + msHost + "', " + 
                    "'" + msPort + "', " + 
                    "'" + msProtocol + "', " + 
                    "'" + msUser + "', " + 
                    "'" + msUserPassword + "', " +
                    "'" + msTextSubject + "', " + 
                    "'" + msTextBody + "', " + 
                    "'" + msRecipientTo + "', " + 
                    "'" + msRecipientCarbonCopy + "', " + 
                    "'" + msRecipientBlindCarbonCopy + "', " +
                    "'" + msArbitraryEmail + "', " + 
                    mnMmsCase + ", " + 
                    (mbStartTls ? 1 : 0) + ", " +
                    (mbAuth ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkMmsTypeId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_mms = " + mnPkMmsId + ", " +
                    */
                    "host = '" + msHost + "', " +
                    "port = '" + msPort + "', " +
                    "prot = '" + msProtocol + "', " +
                    "usr = '" + msUser + "', " +
                    "usr_pswd = '" + msUserPassword + "', " +
                    "txt_subj = '" + msTextSubject + "', " +
                    "txt_body = '" + msTextBody + "', " +
                    "rec_to = '" + msRecipientTo + "', " +
                    "rec_cc = '" + msRecipientCarbonCopy + "', " +
                    "rec_bcc = '" + msRecipientBlindCarbonCopy + "', " +
                    "arb_email = '" + msArbitraryEmail + "', " +
                    "mms_case = " + mnMmsCase + ", " +
                    "b_tls = " + (mbStartTls ? 1 : 0) + ", " +
                    "b_auth = " + (mbAuth ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_mms = " + mnFkMmsTypeId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbMms clone() throws CloneNotSupportedException {
        SDbMms registry = new SDbMms();

        registry.setPkMmsId(this.getPkMmsId());
        registry.setHost(this.getHost());
        registry.setPort(this.getPort());
        registry.setProtocol(this.getProtocol());
        registry.setUser(this.getUser());
        registry.setUserPassword(this.getUserPassword());
        registry.setTextSubject(this.getTextSubject());
        registry.setTextBody(this.getTextBody());
        registry.setRecipientTo(this.getRecipientTo());
        registry.setRecipientCarbonCopy(this.getRecipientCarbonCopy());
        registry.setRecipientBlindCarbonCopy(this.getRecipientBlindCarbonCopy());
        registry.setArbitraryMail(this.getArbitraryMail());
        registry.setMmsCase(this.getMmsCase());
        registry.setStartTls(this.isStartTls());
        registry.setAuth(this.isAuth());
        registry.setDeleted(this.isDeleted());
        registry.setFkMmsTypeId(this.getFkMmsTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setXtaMailReplyTo(this.getXtaMailReplyTo());
        
        return registry;
    }
}
