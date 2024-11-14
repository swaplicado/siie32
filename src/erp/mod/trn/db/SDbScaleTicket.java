/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

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
 * @author Isabel Servín
 */
public class SDbScaleTicket extends SDbRegistryUser {

    protected int mnPkScaleTicketId;
    protected int mnNumber;
    protected Date mtDate;
    protected String msPlate;
    protected String msPlateCage;
    protected String msDriver;
    protected Date mtDatetimeArrival;
    protected Date mtDatetimeDeparture;
    protected double mdWeightArrival;
    protected double mdWeightDeparture;
    protected double mdWeightNet_r;
    protected String msCommentArrival;
    protected String msCommentDeparture;
    protected String msTicketCase;
    protected boolean mbAutomatic;
    protected boolean mbAutomaticArrival;
    protected boolean mbAutomaticDeparture;
    protected boolean mbTared;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkCompanyId;
    protected int mnFkDiogCategoryId;
    protected int mnFkScaleId;
    protected int mnFkItemId;
    protected int mnFkBizPartnerId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public SDbScaleTicket() {
        super(SModConsts.TRNU_SCA_TIC);
    }

    public void setPkScaleTicketId(int n) { mnPkScaleTicketId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setPlate(String s) { msPlate = s; }
    public void setPlateCage(String s) { msPlateCage = s; }
    public void setDriver(String s) { msDriver = s; }
    public void setDatetimeArrival(Date t) { mtDatetimeArrival = t; }
    public void setDatetimeDeparture(Date t) { mtDatetimeDeparture = t; }
    public void setWeightArrival(double d) { mdWeightArrival = d; }
    public void setWeightDeparture(double d) { mdWeightDeparture = d; }
    public void setWeightNet_r(double d) { mdWeightNet_r = d; }
    public void setCommentArrival(String s) { msCommentArrival = s; }
    public void setCommentDeparture(String s) { msCommentDeparture = s; }
    public void setTicketCase(String s) { msTicketCase = s; }
    public void setAutomatic(boolean b) { mbAutomatic = b; }
    public void setAutomaticArrival(boolean b) { mbAutomaticArrival = b; }
    public void setAutomaticDeparture(boolean b) { mbAutomaticDeparture = b; }
    public void setTared(boolean b) { mbTared = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkCompanyId(int n) { mnFkCompanyId = n; }
    public void setFkDiogCategoryId(int n) { mnFkDiogCategoryId = n; }
    public void setFkScaleId(int n) { mnFkScaleId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkScaleTicketId() { return mnPkScaleTicketId; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public String getPlate() { return msPlate; }
    public String getPlateCage() { return msPlateCage; }
    public String getDriver() { return msDriver; }
    public Date getDatetimeArrival() { return mtDatetimeArrival; }
    public Date getDatetimeDeparture() { return mtDatetimeDeparture; }
    public double getWeightArrival() { return mdWeightArrival; }
    public double getWeightDeparture() { return mdWeightDeparture; }
    public double getWeightNet_r() { return mdWeightNet_r; }
    public String getCommentArrival() { return msCommentArrival; }
    public String getCommentDeparture() { return msCommentDeparture; }
    public String getTicketCase() { return msTicketCase; }
    public boolean isAutomatic() { return mbAutomatic; }
    public boolean isAutomaticArrival() { return mbAutomaticArrival; }
    public boolean isAutomaticDeparture() { return mbAutomaticDeparture; }
    public boolean isTared() { return mbTared; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkCompanyId() { return mnFkCompanyId; }
    public int getFkDiogCategoryId() { return mnFkDiogCategoryId; }
    public int getFkScaleId() { return mnFkScaleId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkScaleTicketId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkScaleTicketId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkScaleTicketId = 0;
        mnNumber = 0;
        mtDate = null;
        msPlate = "";
        msPlateCage = "";
        msDriver = "";
        mtDatetimeArrival = null;
        mtDatetimeDeparture = null;
        mdWeightArrival = 0;
        mdWeightDeparture = 0;
        mdWeightNet_r = 0;
        msCommentArrival = null;
        msCommentDeparture = null;
        msTicketCase = "";
        mbAutomatic = false;
        mbAutomaticArrival = false;
        mbAutomaticDeparture = false;
        mbTared = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkCompanyId = 0;
        mnFkDiogCategoryId = 0;
        mnFkScaleId = 0;
        mnFkItemId = 0;
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
        return "WHERE id_sca_tic = " + mnPkScaleTicketId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_sca_tic = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet;
        
        mnPkScaleTicketId = 0;
        
        msSql = "SELECT COALESCE(MAX(id_sca_tic), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkScaleTicketId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet;
        
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;
        
        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkScaleTicketId = resultSet.getInt("id_sca_tic");
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            msPlate = resultSet.getString("plate");
            msPlateCage = resultSet.getString("plate_cage");
            msDriver = resultSet.getString("driver");
            mtDatetimeArrival = resultSet.getTimestamp("ts_arr");
            mtDatetimeDeparture = resultSet.getTimestamp("ts_dep");
            mdWeightArrival = resultSet.getDouble("wei_arr");
            mdWeightDeparture = resultSet.getDouble("wei_dep");
            mdWeightNet_r = resultSet.getDouble("wei_net_r");
            msCommentArrival = resultSet.getString("cmt_arr");
            msCommentDeparture = resultSet.getString("cmt_dep");
            msTicketCase = resultSet.getString("tic_case");
            mbAutomatic = resultSet.getBoolean("b_aut");
            mbAutomaticArrival = resultSet.getBoolean("b_aut_arr");
            mbAutomaticDeparture = resultSet.getBoolean("b_aut_dep");
            mbTared = resultSet.getBoolean("b_tar");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkCompanyId = resultSet.getInt("fk_co");
            mnFkDiogCategoryId = resultSet.getInt("fk_ct_iog");
            mnFkScaleId = resultSet.getInt("fk_sca");
            mnFkItemId = resultSet.getInt("fk_item");
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
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkScaleTicketId + ", " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    "'" + msPlate + "', " + 
                    "'" + msPlateCage + "', " + 
                    "'" + msDriver + "', " +  
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeArrival) + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeDeparture) + "', " + 
                    mdWeightArrival + ", " + 
                    mdWeightDeparture + ", " + 
                    mdWeightNet_r + ", " + 
                    "'" + msCommentArrival + "', " + 
                    "'" + msCommentDeparture + "', " + 
                    "'" + msTicketCase + "', " + 
                    (mbAutomatic ? 1 : 0) + ", " + 
                    (mbAutomaticArrival ? 1 : 0) + ", " + 
                    (mbAutomaticDeparture ? 1 : 0) + ", " + 
                    (mbTared ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkCompanyId + ", " + 
                    mnFkDiogCategoryId + ", " + 
                    mnFkScaleId + ", " + 
                    mnFkItemId + ", " + 
                    mnFkBizPartnerId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " + 
                    //"id_sca_tic = " + mnPkScaleTicketId + ", " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "plate = '" + msPlate + "', " +
                    "plate_cage = '" + msPlateCage + "', " +
                    "driver = '" + msDriver + "', " +
                    "ts_arr = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeArrival) + "', " +
                    "ts_dep = '" + SLibUtils.DbmsDateFormatDatetime.format(mtDatetimeDeparture) + "', " +
                    "wei_arr = " + mdWeightArrival + ", " +
                    "wei_dep = " + mdWeightDeparture + ", " +
                    "wei_net_r = " + mdWeightNet_r + ", " +
                    "cmt_arr = '" + msCommentArrival + "', " +
                    "cmt_dep = '" + msCommentDeparture + "', " +
                    "tic_case = '" + msTicketCase + "', " +
                    "b_aut = " + (mbAutomatic ? 1 : 0) + ", " +
                    "b_aut_arr = " + (mbAutomaticArrival ? 1 : 0) + ", " +
                    "b_aut_dep = " + (mbAutomaticDeparture ? 1 : 0) + ", " +
                    "b_tar = " + (mbTared ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_co = " + mnFkCompanyId + ", " +
                    "fk_sca = " + mnFkScaleId + ", " +
                    "fk_ct_iog = " + mnFkDiogCategoryId + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    "fk_bp = " + mnFkBizPartnerId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbScaleTicket clone() throws CloneNotSupportedException {
        SDbScaleTicket registry = new SDbScaleTicket();
        
        registry.setPkScaleTicketId(this.getPkScaleTicketId());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setPlate(this.getPlate());
        registry.setPlateCage(this.getPlateCage());
        registry.setDriver(this.getDriver());
        registry.setDatetimeArrival(this.getDatetimeArrival());
        registry.setDatetimeDeparture(this.getDatetimeDeparture());
        registry.setWeightArrival(this.getWeightArrival());
        registry.setWeightDeparture(this.getWeightDeparture());
        registry.setWeightNet_r(this.getWeightNet_r());
        registry.setCommentArrival(this.getCommentArrival());
        registry.setCommentDeparture(this.getCommentDeparture());
        registry.setTicketCase(this.getTicketCase());
        registry.setAutomatic(this.isAutomatic());
        registry.setAutomaticArrival(this.isAutomaticArrival());
        registry.setAutomaticDeparture(this.isAutomaticDeparture());
        registry.setTared(this.isTared());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkCompanyId(this.getFkCompanyId());
        registry.setFkDiogCategoryId(this.getFkDiogCategoryId());
        registry.setFkScaleId(this.getFkScaleId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkBizPartnerId(this.getFkBizPartnerId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }
    
    public boolean hasLinks(SGuiSession session) throws SQLException, Exception {
        String sql = "SELECT id_sca_tic FROM trn_sca_tic_dps_ety WHERE id_sca_tic = " + mnPkScaleTicketId + " " +
                "UNION " + 
                "SELECT id_sca_tic FROM trn_sca_tic_dps WHERE id_sca_tic = " + mnPkScaleTicketId;
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        return resultSet.next();
    }
    
    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        return !hasLinks(session);
    }
}
