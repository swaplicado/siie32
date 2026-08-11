/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Adrian Aviles
 */
public class SDataFacilityRec implements java.io.Serializable {
    protected int mnPkIdExtFacilityRec;
    protected int mnRecYear;
    protected int mnRecMonth;
    protected int mnRecWeek;
    protected int mnUserId;
    protected int mnExtDataId;
    protected boolean mbCanEdit;
    protected boolean mbCanDel;
    protected boolean mbIsDeleted;
    protected int mnFkExtFacility;
    protected int mnFkRecYear;
    protected int mnFkRecPer;
    protected int mnFkRecBkc;
    protected String msFkRecTpRec;
    protected int mnFkRecNum;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtNewTs;
    protected java.util.Date mtEditTs;
    protected java.util.Date mtDeleteTs;
    
    protected int mnLastDbActionResult;
    
    public SDataFacilityRec(){
        reset();
    }
    
    public void reset() {
        mnRecYear = 0;
        mnRecMonth = 0;
        mnRecWeek = 0;
        mnUserId = 0;
        mnExtDataId = 0;
        mbCanEdit = false;
        mbCanDel = false;
        mbIsDeleted = false;
        mnFkExtFacility = 0;
        mnFkRecYear = 0;
        mnFkRecPer = 0;
        mnFkRecBkc = 0;
        msFkRecTpRec = "";
        mnFkRecNum = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtNewTs = null;
        mtEditTs = null;
        mtDeleteTs = null;
    }

    public int getMnRecYear() { return mnRecYear; }
    public void setMnRecYear(int mnRecYear) { this.mnRecYear = mnRecYear; }
    public int getMnRecMonth() { return mnRecMonth; }
    public void setMnRecMonth(int mnRecMonth) { this.mnRecMonth = mnRecMonth; }
    public int getMnRecWeek() { return mnRecWeek; }
    public void setMnRecWeek(int mnRecWeek) { this.mnRecWeek = mnRecWeek; }
    public int getMnExtDataId() { return mnExtDataId; }
    public void setMnExtDataId(int mnExtDataId) { this.mnExtDataId = mnExtDataId; }
    public boolean isMbCanEdit() { return mbCanEdit; }
    public void setMbCanEdit(boolean mbCanEdit) { this.mbCanEdit = mbCanEdit; }
    public boolean isMbCanDel() { return mbCanDel; }
    public void setMbCanDel(boolean mbCanDel) { this.mbCanDel = mbCanDel; }
    public boolean isMbIsDeleted() { return mbIsDeleted; }
    public void setMbIsDeleted(boolean mbIsDeleted) { this.mbIsDeleted = mbIsDeleted; }
    public int getMnFkExtFacility() { return mnFkExtFacility; }
    public void setMnFkExtFacility(int mnFkExtFacility) { this.mnFkExtFacility = mnFkExtFacility; }
    public int getMnFkRecYear() { return mnFkRecYear; }
    public void setMnFkRecYear(int mnFkRecYear) { this.mnFkRecYear = mnFkRecYear; }
    public int getMnFkRecPer() { return mnFkRecPer; }
    public void setMnFkRecPer(int mnFkRecPer) { this.mnFkRecPer = mnFkRecPer; }
    public int getMnFkRecBkc() { return mnFkRecBkc; }
    public void setMnFkRecBkc(int mnFkRecBkc) { this.mnFkRecBkc = mnFkRecBkc; }
    public String getMsFkRecTpRec() { return msFkRecTpRec; }
    public void setMsFkRecTpRec(String msFkRecTpRec) { this.msFkRecTpRec = msFkRecTpRec; }
    public int getMnFkRecNum() { return mnFkRecNum; }
    public void setMnFkRecNum(int mnFkRecNum) { this.mnFkRecNum = mnFkRecNum; }
    public int getMnFkUserNewId() { return mnFkUserNewId; }
    public void setMnFkUserNewId(int mnFkUserNewId) { this.mnFkUserNewId = mnFkUserNewId; }
    public int getMnFkUserEditId() { return mnFkUserEditId; }
    public void setMnFkUserEditId(int mnFkUserEditId) { this.mnFkUserEditId = mnFkUserEditId; }
    public int getMnFkUserDeleteId() { return mnFkUserDeleteId; }
    public void setMnFkUserDeleteId(int mnFkUserDeleteId) { this.mnFkUserDeleteId = mnFkUserDeleteId; }
    public Date getMtNewTs() { return mtNewTs; }
    public void setMtNewTs(Date mtUserNewTs) { this.mtNewTs = mtUserNewTs; }
    public Date getMtEditTs() { return mtEditTs; }
    public void setMtEditTs(Date mtUserEditTs) { this.mtEditTs = mtUserEditTs; }
    public Date getMtDeleteTs() { return mtDeleteTs; }
    public void setMtDeleteTs(Date mtUserDeleteTs) { this.mtDeleteTs = mtUserDeleteTs; }
    public int getMnPkIdExtFacilityRec() { return mnPkIdExtFacilityRec; }
    public void setMnUserId(int mnUserId) { this.mnUserId = mnUserId; }
    public int getMnUserId() { return mnUserId; }
    
    public int read(java.lang.Object pk, java.sql.Statement statement){
        Object[] key = (Object[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();
        
        try {
            sql = "SELECT * FROM fin_ext_facility_rec where id_ext_facility_rec = " + key[0];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkIdExtFacilityRec = resultSet.getInt("id_ext_facility_rec");
                mnRecYear = resultSet.getInt("rec_year");
                mnRecMonth = resultSet.getInt("rec_month");
                mnRecWeek = resultSet.getInt("rec_week");
                mnUserId = resultSet.getInt("usr_id");
                mnExtDataId = resultSet.getInt("ext_data_id");
                mbCanEdit = resultSet.getBoolean("b_can_edit");
                mbCanDel = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkExtFacility = resultSet.getInt("fid_ext_facility");
                mnFkRecYear = resultSet.getInt("fid_rec_year");
                mnFkRecPer = resultSet.getInt("fid_rec_per");
                mnFkRecBkc = resultSet.getInt("fid_rec_bkc");
                msFkRecTpRec = resultSet.getString("fid_rec_tp_rec");
                mnFkRecNum = resultSet.getInt("fid_rec_num");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtNewTs = resultSet.getDate("ts_new");
                mtEditTs = resultSet.getDate("ts_edit");
                mtDeleteTs = resultSet.getDate("ts_del");
                
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
    
    public int save(java.sql.Connection connection) {
        String sql = "";
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            statement = connection.createStatement();
            
            sql = "SELECT COALESCE(MAX(id_ext_facility_rec), 0) + 1 AS _new_id " +
                        "FROM fin_ext_facility_rec;";
                
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                mnPkIdExtFacilityRec = resultSet.getInt(1);
            }

            sql = "INSERT INTO fin_ext_facility_rec ("
                    + "id_ext_facility_rec, rec_year, rec_month, rec_week, ext_data_id, usr_id, "
                    + "b_can_edit, b_can_del, b_del, "
                    + "fid_ext_facility, fid_rec_year, fid_rec_per, fid_rec_bkc, fid_rec_tp_rec, fid_rec_num, "
                    + "fid_usr_new, fid_usr_edit, fid_usr_del, "
                    + "ts_new, ts_edit, ts_del"
                    + ") " +
                    "VALUES ("
                    + mnPkIdExtFacilityRec + ", " + mnRecYear + ", " + mnRecMonth + ", " + mnRecWeek + ", " + mnExtDataId + ", " + mnUserId + ", " 
                    + mbCanEdit + ", " + mbCanDel + ", " + mbIsDeleted + ", "
                    + mnFkExtFacility + ", " + mnFkRecYear + ", " + mnFkRecPer + ", " + mnFkRecBkc + ", \"" + msFkRecTpRec + "\", " + mnFkRecNum + ", "
                    + mnFkUserNewId + ", " + mnFkUserEditId + ", " + mnFkUserDeleteId + ", " 
                    + "NOW()" + ", " + "NOW()" + ", " + "NOW()"
                    + ")";
            statement.execute(sql);
            
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }
    
    public int delete(java.sql.Connection connection) {
        String sql = "";
        Statement statement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            statement = connection.createStatement();
            
            sql = "UPDATE fin_ext_facility_rec SET "
                    + "b_del = 1, "
                    + "fid_usr_del = " + mnFkUserDeleteId + ", "
                    + "ts_del = " + "NOW()" + " "
                    + "WHERE id_ext_facility_rec = " + mnPkIdExtFacilityRec;
            
            statement.execute(sql);
            
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_DELETE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        
        return mnLastDbActionResult;
    }
    
    public int findByExtDataId(int data_id, java.sql.Statement statement) {
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;
        boolean exist = false;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT COUNT(*) > 0 AS exist FROM fin_ext_facility_rec where b_del = 0 AND ext_data_id = " + data_id;
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                // EXISTS devuelve 1 (true) o 0 (false)
                exist = resultSet.getInt("exist") == 1;
            }
            
            if (!exist) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
                return mnLastDbActionResult;
            }
            
            sql = "SELECT * FROM fin_ext_facility_rec where b_del = 0 AND ext_data_id = " + data_id;
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkIdExtFacilityRec = resultSet.getInt("id_ext_facility_rec");
                mnRecYear = resultSet.getInt("rec_year");
                mnRecMonth = resultSet.getInt("rec_month");
                mnRecWeek = resultSet.getInt("rec_week");
                mnUserId = resultSet.getInt("usr_id");
                mnExtDataId = resultSet.getInt("ext_data_id");
                mbCanEdit = resultSet.getBoolean("b_can_edit");
                mbCanDel = resultSet.getBoolean("b_can_del");
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkExtFacility = resultSet.getInt("fid_ext_facility");
                mnFkRecYear = resultSet.getInt("fid_rec_year");
                mnFkRecPer = resultSet.getInt("fid_rec_per");
                mnFkRecBkc = resultSet.getInt("fid_rec_bkc");
                msFkRecTpRec = resultSet.getString("fid_rec_tp_rec");
                mnFkRecNum = resultSet.getInt("fid_rec_num");
                mnFkUserNewId = resultSet.getInt("fid_usr_new");
                mnFkUserEditId = resultSet.getInt("fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("fid_usr_del");
                mtNewTs = resultSet.getDate("ts_new");
                mtEditTs = resultSet.getDate("ts_edit");
                mtDeleteTs = resultSet.getDate("ts_del");
                
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
}
