/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.SClientUtils;
import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Adrian Aviles
 */
public class SDataCfdStatusLog extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkLogId;
    protected java.util.Date mtRequestDatetime;
    protected java.lang.String msUuid;
    protected boolean mbResponse;
    protected java.lang.String msResponseXml;
    protected java.lang.String msStatusCode;
    protected java.lang.String msStatusDescription;
    protected java.lang.String msIsCancellable;
    protected java.lang.String msCancellableStatus;
    protected java.lang.String msEfosValidation;
    protected int mnUserId;
    
    public SDataCfdStatusLog() {
        super(SModConsts.TRN_CFD_ST_LOG);
        reset();
    }

    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setRequestDatetime(java.util.Date t) { mtRequestDatetime = t; }
    public void setUuid(java.lang.String s) { msUuid = s; }
    public void setResponse(boolean b) { mbResponse = b; }
    public void setResponseXml(java.lang.String s) { msResponseXml = s; }
    public void setStatusCode(java.lang.String s) { msStatusCode = s; }
    public void setStatusDescription(java.lang.String s) { msStatusDescription = s; }
    public void setIsCancellable(java.lang.String s) { msIsCancellable = s; }
    public void setCancellableStatus(java.lang.String s) { msCancellableStatus = s; }
    public void setEfosValidation(java.lang.String s) { msEfosValidation = s; }
    public void setUserId(int n) { mnUserId = n; }

    public int getPkLogId() { return mnPkLogId; }
    public java.util.Date getRequestDatetime() { return mtRequestDatetime; }
    public java.lang.String getUuid() { return msUuid; }
    public boolean getResponse() { return mbResponse; }
    public java.lang.String getResponseXml() { return msResponseXml; }
    public java.lang.String getStatusCode() { return msStatusCode; }
    public java.lang.String getStatusDescription() { return msStatusDescription; }
    public java.lang.String getIsCancellable() { return msIsCancellable; }
    public java.lang.String getCancellableStatus() { return msCancellableStatus; }
    public java.lang.String getEfosValidation() { return msEfosValidation; }
    public int getUserId() { return mnUserId; }

    public void reset() {
        mnPkLogId = 0;
        mtRequestDatetime = null;
        msUuid = "";
        mbResponse = false;
        msResponseXml = "";
        msStatusCode = "";
        msStatusDescription = "";
        msIsCancellable = "";
        msCancellableStatus = "";
        msEfosValidation = "";
        mnUserId = 0;
    }
    
    @Override
    public int save(java.sql.Connection connection) {
        String sql = "";
        Statement statement = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        
        try {
            statement = connection.createStatement();
            
            sql = "INSERT INTO " + SClientUtils.getComplementaryDbName(connection) + ".trn_cfd_st_log (" +
                        "req_dt, " +
                        "uuid, " +
                        "b_resp, " +
                        "resp_xml, " +
                        "st_code, " +
                        "st_desc, " +
                        "is_can, " +
                        "user_id, " +
                        "can_st, " +
                        "efos_val " +
                    ")" +
                    "VALUES (" +
                        "NOW(), " +
                        "'" + msUuid + "', " +
                        (mbResponse ? 1 : 0) + ", " +
                        "'" + msResponseXml + "', " +
                        "'" + msStatusCode + "', " +
                        "'" + msStatusDescription + "', " +
                        "'" + msIsCancellable+ "', " +
                        mnUserId + ", " +
                        "'" + msCancellableStatus + "', " +
                        "'" + msEfosValidation + "' " +
                    ");";
            
            statement.execute(sql);
            
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public void setPrimaryKey(Object pk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int read(Object pk, Statement statement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}