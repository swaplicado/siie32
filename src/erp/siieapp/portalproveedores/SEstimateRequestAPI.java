/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp.portalproveedores;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Cesar Orozco
 */
public class SEstimateRequestAPI {
    private SGuiSession oSession;

    public SEstimateRequestAPI(SGuiSession session) {
        oSession = session;
    }

    public SEstimateRequestAPI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getResources(String sJson) throws SQLException {
       String jsonDr = "";
        ArrayList<SEstimateRequestData> lERData = new ArrayList<>();
        int bp = 0;
        String date = "";
        String arregloAuxiliar = "";
        
        JSONParser parser = new JSONParser();
        JSONObject root;
        try {
            root = (JSONObject) parser.parse(sJson);
            bp = Integer.parseInt(root.get("idBp").toString());
            date = root.get("date").toString();
            arregloAuxiliar = arregloAuxiliar + root.get("aBp").toString();
            arregloAuxiliar = arregloAuxiliar.replace("[", "(");
            arregloAuxiliar = arregloAuxiliar.replace("]", ")");
            
        } catch (ParseException ex) {
            Logger.getLogger(SPurcharseOrdersAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        String msSql = "SELECT er.id_est_req, num, prov_name, mails_to,"
                + " subject, body, fk_bp_n, YEAR(ts_usr) as idYear, ts_usr as date "
                + " FROM trn_est_req AS er "
                + " INNER JOIN trn_est_req_rec AS rr "
                + " ON er.id_est_req = rr.id_est_req "
                + " WHERE er.b_del = 0 AND rr.b_del = 0 "
                + " AND ts_usr > " + date 
                + " AND rr.fk_bp_n IN " + arregloAuxiliar + " ";
                
                

        try (ResultSet res = oSession.getDatabase().getConnection().createStatement().executeQuery(msSql)) {
            SDataEstimateRequestResponse er = new SDataEstimateRequestResponse();
            while (res.next()) {
                SEstimateRequestData erd = new SEstimateRequestData();
                
                erd.setIdEstimateRequest(res.getInt("er.id_est_req"));
                erd.setNumber(res.getInt("num"));
                erd.setNameBp(res.getString("prov_name"));
                erd.setIdBp(res.getInt("fk_bp_n"));
                erd.setSubject(res.getString("subject"));
                erd.setMailsTo(res.getString("mails_to"));
                erd.setBody(res.getString("body"));
                erd.setDate(res.getString("date"));
                
                lERData.add(erd);
            }

            er.setERData(lERData);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            jsonDr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(er);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SEstimateRequestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonDr;
        
    }
    
    public String getResoursesEty(String sJson){
        String jsonDr = "";
        ArrayList<SEstimateRequestEtyData> lEREData = new ArrayList<>();
        int idEstReq = 0;
        
        JSONParser parser = new JSONParser();
        JSONObject root;
        try {
            root = (JSONObject) parser.parse(sJson);
            idEstReq = Integer.parseInt(root.get("idEstReq").toString());
            
        } catch (ParseException ex) {
            Logger.getLogger(SEstimateRequestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        String msSql = "SELECT id_est_req, id_ety, qty, "
                + "i.id_item, i.item, u.id_unit, u.unit, u.symbol "
                + "FROM trn_est_req_ety AS ere "
                + "INNER JOIN erp.itmu_item AS i ON "
                + "i.id_item = ere.fk_item "
                + "INNER JOIN erp.itmu_unit AS u ON  "
                + "u.id_unit = ere.fk_unit "
                + "WHERE id_est_req = " + idEstReq + " "
                + "AND ere.b_del = 0;";

        try (ResultSet res = oSession.getDatabase().getConnection().createStatement().executeQuery(msSql)) {
            SDataEstimateRequestEtyResponse  dr = new SDataEstimateRequestEtyResponse ();
            while (res.next()) {
                SEstimateRequestEtyData ered = new SEstimateRequestEtyData();
                
                ered.setIdEstimateRequest(res.getInt("id_est_req"));
                ered.setIdEty(res.getInt("id_ety"));
                ered.setQty(res.getDouble("qty"));
                ered.setIdItem(res.getInt("i.id_item"));
                ered.setNameItem(res.getString("i.item"));
                ered.setIdUnit(res.getInt("u.id_unit"));
                ered.setNameUnit(res.getString("u.unit"));
                ered.setSymbol(res.getString("u.symbol"));
               
                lEREData.add(ered);
            }
            dr.setlEREData(lEREData);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            jsonDr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dr);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SPurcharseOrdersAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SPurcharseOrdersAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonDr;
    }
}
