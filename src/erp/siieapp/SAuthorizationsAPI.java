/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.mod.SModConsts;
import erp.mod.cfg.utils.SAuthorizationUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author AdrianAviles
 */
public class SAuthorizationsAPI {
    private SGuiSession oSession;
    
    public SAuthorizationsAPI(SGuiSession session){
        oSession = session;
    }
    
    /**
     * Metodo autorizar recurso
     * 
     */
    public void authorizeResouorce(){
        
    }
    
    public void rejectResource(){
        
    }
    
    public boolean isAutorized(int pk){
        boolean res = SAuthorizationUtils.isAuthorized(oSession, 1, new int[] { pk });
        return res;
    }
    
    public int getStatus(int pk){
        int res = SAuthorizationUtils.getAuthStatus(oSession, 1, new int[] { pk });
        return res;
    }
    
    public String getResources() throws SQLException{
        String jsonDr = "";
        ArrayList<SAuthorizationsData> lAuthData = new ArrayList<>();
        
        String msSql = "SELECT " +
                        "    CASE " +
                        "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST + " THEN " +
                        "             (SELECT LPAD(tmq.num, 6, '0') AS num_req " +
                        "                FROM " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_REQ) + " AS tmq " +
                        "               WHERE tmq.id_mat_req = v.res_pk_n1_n) " +
                        "        WHEN v.fk_tp_authorn = " + SAuthorizationUtils.AUTH_TYPE_DPS + " THEN " +
                        "            (SELECT  " +
                        "                    CONCAT(t.dt_doc, ' ', t.num_ser, ' ', t.num, ' ', t.num_ref) " +
                        "                FROM " +
                        "                    trn_dps AS t " +
                        "                WHERE " +
                        "                    t.id_year =  v.res_pk_n1_n " +
                        "                        AND t.id_doc =  v.res_pk_n2_n) " +
                        "        ELSE '---' " +
                        "END AS doc_reference, " +
                        "v.id_authorn_step AS " + SDbConsts.FIELD_ID + "1, " +
                        "'' AS " + SDbConsts.FIELD_CODE + ", " +
                        "cta.name AS " + SDbConsts.FIELD_NAME + ", " +
                        "v.*, " +
                        "IF(v.b_authorn, " + SGridConsts.ICON_THUMBS_UP + ", IF(v.b_reject, " + SGridConsts.ICON_THUMBS_DOWN + ", " + SGridConsts.ICON_NULL + ")) AS f_ico_auth_st, " +
                        "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " +
                        "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", " +
                        "ua.usr AS auth_user, " +
                        "v.dt_time_authorn_n, " +
                        "ur.usr AS rej_user, " +
                        "v.dt_time_reject_n, " +
                        "us.usr AS step_user, " +
                        "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", " +
                        "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", " +
                        "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", " +
                        "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", " +
                        "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", " +
                        "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " " +
                        "FROM " + SModConsts.TablesMap.get(SModConsts.CFGU_AUTHORN_STEP) + " AS v " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGS_TP_AUTHORN) + " AS cta ON " +
                        "v.fk_tp_authorn = cta.id_tp_authorn " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON " +
                        "v.fk_usr_ins = ui.id_usr " +
                        "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON " +
                        "v.fk_usr_upd = uu.id_usr " +
                        "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON " +
                        "v.fk_usr_authorn_n = ua.id_usr " +
                        "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ur ON " +
                        "v.fk_usr_reject_n = ur.id_usr " +
                        "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS us ON " +
                        "v.fk_usr_step = us.id_usr " +
                        "WHERE " +
                        "NOT v.b_del " +
                        "ORDER BY v.fk_tp_authorn ASC, v.lev ASC";
        
        try (ResultSet res = oSession.getDatabase().getConnection().createStatement().executeQuery(msSql)) {
            SDataResponse dr = new SDataResponse();
            while(res.next()){
                SAuthorizationsData au = new SAuthorizationsData();
                
                switch(res.getInt("fk_tp_authorn")){
                    case SAuthorizationUtils.AUTH_TYPE_DPS:
                        au.setIdData(new int []{ res.getInt("res_pk_n1_n"), res.getInt("res_pk_n2_n") });
                        au.setDate(new Date());
                        au.setFolio("");
                        au.setFkUserCreator(1);
                        au.setFkUserUpdator(1);
                        au.setDateInsert(new Date());
                        au.setDateUpdate(new Date());
                        break;
                    case SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST:
                        au.setIdData(new int []{ res.getInt("res_pk_n1_n") });
                        au.setDate(new Date());
                        au.setFolio("");
                        au.setFkUserCreator(1);
                        au.setFkUserUpdator(1);
                        au.setDateInsert(new Date());
                        au.setDateUpdate(new Date());
                        break;
                    default:
                        break;
                }
                au.setAuthorizationStatus(5);
                lAuthData.add(au);
            }
            
            dr.setlAuthData(lAuthData);
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            
            jsonDr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dr);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SAuthorizationsAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return jsonDr;
    }
}
