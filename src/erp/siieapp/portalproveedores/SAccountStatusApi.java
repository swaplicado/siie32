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
import erp.siieapp.portalproveedores.*;

/**
 *
 * @author César Orozco
 */
public class SAccountStatusApi {
    private SGuiSession oSession;

    public SAccountStatusApi(SGuiSession session) {
        oSession = session;
    }

    public SAccountStatusApi() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getResources(String sJson) throws SQLException {
        String jsonDr = "";
        ArrayList<SAccountStatusData> lASData = new ArrayList<>();
        int idBp = 0;
        String idYear = "";
        String dateIni = "";
        String dateFin = "";
        
        
        JSONParser parser = new JSONParser();
        JSONObject root;
        try {
            root = (JSONObject) parser.parse(sJson);
            idBp = Integer.parseInt(root.get("idBp").toString());
            idYear = root.get("idYear").toString();
            dateIni = root.get("dateIni").toString();
            dateFin = root.get("dateFin").toString();
            
        } catch (ParseException ex) {
            Logger.getLogger(SPurcharseOrdersAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        String msSql = "SELECT '2023' AS id_year, '0' AS id_per, '0' AS id_bkc, '' AS id_tp_rec, '0' AS id_num, 0 AS sort_pos, "
                + "ADDDATE('"+ dateIni +"', -1) AS dt, 'SALDO INICIAL' concept, "
                + "0.0 AS debit, SUM(re.credit - re.debit) AS credit, "
                + "'0' AS exc_rate, "
                + "0.0 AS debit_cur, SUM(re.credit_cur - re.debit_cur)AS credit_cur, "
                + "' ' AS f_cur_key "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del "
                + "AND r.id_year = " + idYear + " " 
                + "AND r.dt < '" + dateIni + "' "
                + "AND re.fid_ct_sys_mov_xxx = 4 AND re.fid_tp_sys_mov_xxx = 2 AND re.fid_bp_nr = 887 "
                + "UNION ALL "
                + "SELECT r.id_year, r.id_per, r.id_bkc, r.id_tp_rec, LPAD(r.id_num,8,'0'), re.sort_pos, "
                + "r.dt, re.concept, re.debit, re.credit, re.exc_rate, re.debit_cur, re.credit_cur, "
                + "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE re.fid_cur = c.id_cur) AS f_cur_key "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del "
                + "AND r.id_year = " + idYear + " "
                + "AND r.dt BETWEEN '"+ dateIni +"' AND '" + dateFin + "' "
                + "AND re.fid_ct_sys_mov_xxx = 4 AND re.fid_tp_sys_mov_xxx = 2 AND re.fid_bp_nr = " + idBp + " "
                + "ORDER BY dt, id_year, id_per, id_bkc, id_tp_rec, id_num ";
                
                

        try (ResultSet res = oSession.getDatabase().getConnection().createStatement().executeQuery(msSql)) {
            SDataAccountStatusResponse aux = new SDataAccountStatusResponse();
                        
            while (res.next()) {
                SAccountStatusData asd = new SAccountStatusData();

                asd.setIdYear(res.getInt("id_year"));
                asd.setDate(res.getString("dt"));
                asd.setConcept(res.getString("concept"));
                asd.setDebit(res.getDouble("debit"));
                asd.setCredit(res.getDouble("credit"));
                asd.setExcRate(res.getFloat("exc_rate"));
                asd.setImportForeignCurrency(res.getDouble("debit_cur")+res.getDouble("credit_cur"));
                asd.setCurrencyCode(res.getString("f_cur_key"));
               
                lASData.add(asd);
            }
            
            aux.setlASData(lASData);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            jsonDr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(aux);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SPurcharseOrdersAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonDr;
    }
}
