/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import erp.data.SDataConstantsSys;
import erp.mbps.data.SDataBizPartner;
import erp.mcfg.data.SCfgUtils;
import erp.musr.data.SDataUser;
import erp.musr.data.SDataUserAppRow;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author AdrianAviles
 */
public class SUserExportUtils {
    private SGuiClient miClient;
    
    public SUserExportUtils(SGuiClient client) {
        miClient = client;
    }
    
    public String readUser(final int idUser) {
        Statement statement = miClient.getSession().getStatement();
            
        String sql = "SELECT * "
                    + " FROM erp.usru_usr as u"
                    + " LEFT JOIN erp.bpsu_bp as b"
                    + " ON u.fid_bp_n = b.id_bp"
                    + " WHERE id_usr = " + idUser + ";";
           
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(SUserExportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public String loginSiieApp() throws SQLException, ParseException{
        String urls = "";
        String url = "";
        String value = "";
        try {
            urls = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_APP_URLS);
            String arrayUrls[] = urls.split(";");
            url = arrayUrls[0];
        }
        catch (Exception ex) {
            Logger.getLogger(erp.siieapp.SUserExportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Statement statement = miClient.getSession().getStatement();
        String paramValue = "";
        String sql = "SELECT usr_pswd FROM erp.usru_usr WHERE usr = 'app.link';";

        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                paramValue = resultSet.getString(1);
            }
        }

        String data = "{"
                    + "\"username\": \"app.link\","
                    + "\"password\": \"" + paramValue + "\""
                    + "}";
        
        SConectionUtils oCon = new SConectionUtils(miClient);
        oCon.conectWithSiieApp(url, "POST", data, null);
        
        if( oCon.responseCode != 200){
            miClient.showMsgBoxError("No fue posible conectarse con SIIE App.");
            return null;
        }

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(oCon.response.toString());
        value = (String) jsonObject.get("token");
        
        return value;
    }
    
    public ArrayList<SDataUserAppRow> getUserApps(int userId, String token){
        JSONObject jsonObject = null;
        JSONArray lAppsArray = null;
        ArrayList<SDataUserAppRow> apps = new ArrayList<>();
        
        try {
            String urls = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_APP_URLS);
            String arrayUrls[] = urls.split(";");
            String urlExportUser = arrayUrls[2];

            String data = "{"
                        + "\"id\": \"" + userId + "\""
                        + "}";

            SConectionUtils oCon = new SConectionUtils(miClient);
            oCon.conectWithSiieApp(urlExportUser, "POST", data, "Bearer " + token);

            if( oCon.responseCode != 200){
                miClient.showMsgBoxError("No fue posible obtener el acceso a SIIE App.");
                return null;
            }

            String aux = oCon.response.toString();

            JSONParser parser = new JSONParser();

            jsonObject = (JSONObject) parser.parse(aux);

            boolean success = (boolean) jsonObject.get("success");
            lAppsArray = (JSONArray) jsonObject.get("lApps");
            
            SDataUserAppRow appRow;
            for (Object obj : lAppsArray) {
                JSONObject app = (JSONObject) obj;
                appRow = new SDataUserAppRow(Integer.parseInt(app.get("id_app").toString()), app.get("name").toString(), app.get("assigned").toString().equals("1"), 0);
                apps.add(appRow);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return apps;
    }
    
    public boolean exportUser(SDataUser user, SDataBizPartner moBizPartner, ArrayList<Vector> lApps) throws SQLException, ParseException{
        if(user.getEmail().isEmpty()){
            miClient.showMsgBoxWarning("Debe introducir una direccion de email");
            return false;
        }
        
        String token = this.loginSiieApp();
        
        if(token.isEmpty()){
            return false;
        }
        
        Statement statement = miClient.getSession().getStatement();
        String sql = "SELECT usr_pswd FROM erp.usru_usr WHERE id_usr = " + user.getPkUserId() + ";";
        String userPassword = "";
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                userPassword = resultSet.getString(1);
            }
        }
        
        try {
            String sJson = "[";
            int size = lApps.size();
                        
            for (Vector app : lApps) {
                size--;
                sJson = sJson
                        + "{"
                        + "\"id_app\": \"" + app.get(0) + "\","
                        + "\"assigned\": \"" + app.get(2) + "\""
                        + "}";
                
                if(size > 0){
                    sJson = sJson + ",";
                }
            }
            sJson = sJson + "]";
            
            String data = "{"
                        + "\"id\": \"" + user.getPkUserId() + "\","
                        + "\"username\": \"" + user.getUser() + "\","
                        + "\"email\": \"" + user.getEmail() + "\","
                        + "\"password\": \"" + userPassword + "\","
                        + "\"last_name\": \"" + moBizPartner.getLastname() + "\","
                        + "\"names\": \"" + moBizPartner.getFirstname() + "\","
                        + "\"full_name\": \"" + moBizPartner.getLastname() + " " + moBizPartner.getFirstname() + "\","
                        + "\"lApps\": " + sJson
                        + "}";

            String urls = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_APP_URLS);
            String arrayUrls[] = urls.split(";");
            String urlExportUser = arrayUrls[1];

            SConectionUtils oCon = new SConectionUtils(miClient);
            oCon.conectWithSiieApp(urlExportUser, "POST", data, "Bearer " + token);

            if( oCon.responseCode != 200 ){
                miClient.showMsgBoxError("No fue posible exportar el usuario");
                return false;
            }
            
        } catch (Exception e) {
            Logger.getLogger(erp.siieapp.SUserExportUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return true;
    }
    
    public Array getApps(){
        return null;
    }
    
    public boolean unactiveUser(int userId, boolean isActive, boolean isDeleted) throws SQLException, ParseException{
        String token = this.loginSiieApp();
        
        if(token.isEmpty()){
            return false;
        }
                
        try {
            String data = "{"
                        + "\"id\": \"" + userId + "\","
                        + "\"isActive\": \"" + isActive + "\","
                        + "\"isDeleted\": \"" + isDeleted + "\""
                        + "}";

            String urls = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_APP_URLS);
            String arrayUrls[] = urls.split(";");
            String urlExportUser = arrayUrls[3];

            SConectionUtils oCon = new SConectionUtils(miClient);
            oCon.conectWithSiieApp(urlExportUser, "POST", data, "Bearer " + token);

            if( oCon.responseCode != 200 && oCon.responseCode != 404 ){
                miClient.showMsgBoxError("No fue posible actualizar el usuario en SIIE App.");
                return false;
            }
            
        } catch (Exception e) {
            Logger.getLogger(erp.siieapp.SUserExportUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return true;
    }
    
    public boolean SynchronizeExternal(){
        try {
            SGuiUtils.setCursorWait(miClient);
                
            String token = this.loginSiieApp();
        
            if(token.isEmpty()){
                return false;
            }
            
            Statement statement = miClient.getSession().getStatement();
            
            String sql = "SELECT id_usr, b_act, b_del FROM erp.usru_usr;";
            
            String sJson = "[";

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    
                    sJson = sJson
                            + "{"
                            + "\"userId\": \"" + resultSet.getString("id_usr") + "\","
                            + "\"isActive\": \"" + resultSet.getString("b_act") + "\","
                            + "\"isDeleted\": \"" + resultSet.getString("b_del") + "\""
                            + "}";
                    
                    if(!resultSet.isLast()){
                        sJson = sJson + ",";
                    }
                }
            }
            
            sJson = sJson + "]";
            
            String data = "{"
                    + "\"lData\": " + sJson 
                    + "}";
            
            String urls = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_SIIE_APP_URLS);
            String arrayUrls[] = urls.split(";");
            String urlExportUser = arrayUrls[4];
            
            SConectionUtils oCon = new SConectionUtils(miClient);
            oCon.conectWithSiieApp(urlExportUser, "POST", data, "Bearer " + token);
            
        } catch (Exception e) {
            Logger.getLogger(erp.siieapp.SUserExportUtils.class.getName()).log(Level.SEVERE, null, e);
            miClient.showMsgBoxError(e.getMessage());
        } finally {
                SGuiUtils.setCursorDefault(miClient);
                miClient.showMsgBoxInformation("Usuarios sincronizados con SIIE App.");
        }
        
        return true;
    }
}
