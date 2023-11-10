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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author swaplicado
 */
public class SPurcharseOrdersAPI {
    private SGuiSession oSession;

    public SPurcharseOrdersAPI(SGuiSession session) {
        oSession = session;
    }

    public SPurcharseOrdersAPI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getResources(String sJson) throws SQLException {
        String jsonDr = "";
        ArrayList<SPurcharseOrdersData> lOCData = new ArrayList<>();
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
        
        
        
        String msSql = "SELECT "
                + "d.id_year, "
                + "d.id_doc, "
                + "d.dt, "
                + "d.dt_start_cred, "
                + "d.dt_doc_lapsing_n, "
                + "d.dt_doc_delivery_n, "
                + "d.num_ser AS serie, d.num AS folio, "
                + "CONCAT(d.num_ser, "
                + " IF(LENGTH(d.num_ser) = 0, '', '-'), "
                + " d.num) AS f_num, "
                + "d.days_cred, "
                + "d.stot_r, "
                + "d.tax_charged_r, "
                + "d.tax_retained_r, "
                + "d.tot_r, "
                + "d.exc_rate, "
                + "d.stot_cur_r, "
                + "d.tax_charged_cur_r, "
                + "d.tax_retained_cur_r, "
                + "d.tot_cur_r, "
                + "bp.id_bp, "
                + "bp.bp, "
                + "bp.fiscal_id, "
                + "bpc.bp_key, "
                + "bpb.id_bpb, "
                + "bpb.bpb, "
                + " (SELECT "
                + " c.cur_key FROM "
                + " erp.cfgu_cur AS c WHERE"
                + " d.fid_cur = c.id_cur) AS f_cur_key, "
                + " 'MXN' AS f_cur_key_local "
                + " FROM "
                + " trn_dps AS d "
                + " INNER JOIN "
                + " erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp "
                + " INNER JOIN "
                + " erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp "
                + " AND bpc.id_ct_bp = 2 "
                + " INNER JOIN "
                + " erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb "
                + " INNER JOIN "
                + " erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps "
                + " AND d.fid_cl_dps = dt.id_cl_dps "
                + " AND d.fid_tp_dps = dt.id_tp_dps "
                + " AND d.fid_ct_dps = 1 "
                + " AND d.fid_cl_dps = 2 "
                + " WHERE "
                + " d.b_del = 0 "
                + " AND d.fid_tp_dps = 1 "
                + " AND d.fid_st_dps = 2 "
                + " AND d.fid_st_dps_authorn = 4 "
                + " AND d.b_authorn = 1 "
                + " AND d.fid_func IN (1,2,3,4,5,6) "
                + " AND d.dt > '" + date + "' " 
                + " AND bp.id_bp IN " + arregloAuxiliar + " ";
                

        try (ResultSet res = oSession.getDatabase().getConnection().createStatement().executeQuery(msSql)) {
            SDataResponse dr = new SDataResponse();
            while (res.next()) {
                SPurcharseOrdersData ocd = new SPurcharseOrdersData();

                ocd.setIdYear(res.getInt("d.id_year"));
                ocd.setIdDoc(res.getInt("d.id_doc"));
                ocd.setDate(res.getString("d.dt"));
                ocd.setDateStartCred(res.getString("d.dt_start_cred"));
                ocd.setDateDocDelivery(res.getString("d.dt_doc_delivery_n"));
                ocd.setSerie(res.getString("serie"));
                ocd.setFolio(res.getString("folio"));
                ocd.setNumRef(res.getString("f_num"));
                ocd.setDaysCred(res.getInt("d.days_cred"));
                ocd.setStot(res.getDouble("d.stot_r"));
                ocd.setTaxCharged(res.getDouble("d.tax_charged_r"));
                ocd.setTaxRetained(res.getDouble("d.tax_retained_r"));
                ocd.setTot(res.getDouble("d.tot_r"));
                ocd.setExcRate(res.getFloat("d.exc_rate"));
                ocd.setStotCur(res.getDouble("d.stot_cur_r"));
                ocd.setTaxChargedCur(res.getDouble("d.tax_charged_cur_r"));
                ocd.setTaxRetainedCur(res.getDouble("d.tax_retained_cur_r"));
                ocd.setTotCur(res.getDouble("d.tot_cur_r"));
                ocd.setIdBP(res.getInt("bp.id_bp"));
                ocd.setBp(res.getString("bp.bp"));
                ocd.setFiscalId(res.getString("bp.fiscal_id"));
                ocd.setBpb(res.getString("bpb.bpb"));
                ocd.setfCurKey(res.getString("f_cur_key"));
                ocd.setfCurKeyLocal(res.getString("f_cur_key_local"));
               
                lOCData.add(ocd);
            }

            dr.setlPOData(lOCData);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            jsonDr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dr);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SPurcharseOrdersAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jsonDr;
    }
    
    public String getResoursesEty(String sJson){
        String jsonDr = "";
        ArrayList<SPurcharseOrderEtyData> lPOEData = new ArrayList<>();
        int idDoc = 0;
        int idYear = 0;
        
        JSONParser parser = new JSONParser();
        JSONObject root;
        try {
            root = (JSONObject) parser.parse(sJson);
            idDoc = Integer.parseInt(root.get("idDoc").toString());
            idYear = Integer.parseInt(root.get("idYear").toString());
            
        } catch (ParseException ex) {
            Logger.getLogger(SPurcharseOrdersAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        String msSql = "SELECT "
                + "ety.id_year, "
                + "ety.id_doc, "
                + "ety.id_ety, "
                + "ety.concept_key, "
                + "ety.concept, "
                + "ety.ref, "
                + "ety.qty, "
                + "ety.price_u, "
                + "ety.stot_r, "
                + "ety.tax_charged_r, "
                + "ety.tax_retained_r, "
                + "ety.tot_r, "
                + "ety.price_u_real_r, "
                + "ety.price_u_cur, "
                + "ety.stot_cur_r, "
                + "ety.tax_charged_cur_r, "
                + "ety.tax_retained_cur_r, "
                + "ety.tot_cur_r, "
                + "ety.price_u_real_cur_r, "
                + "i.id_item, "
                + "i.name, "
                + "i.item, "
                + "u.unit, "
                + "u.id_unit, "
                + "ou.unit, "
                + "ou.id_unit "
                + " FROM "
                + " trn_dps_ety AS ety "
                + " INNER JOIN erp.itmu_item AS i ON ety.fid_item = i.id_item "
                + " INNER JOIN erp.itmu_unit AS u ON ety.fid_unit = u.id_unit "
                + " INNER JOIN erp.itmu_unit AS ou ON ety.fid_unit = ou.id_unit "
                + " WHERE ety.id_year = " + idYear 
                + " AND ety.id_doc = " + idDoc
                + " AND ety.b_del = 0; ";
                

        try (ResultSet res = oSession.getDatabase().getConnection().createStatement().executeQuery(msSql)) {
            SDataEtyResponse dr = new SDataEtyResponse();
            while (res.next()) {
                SPurcharseOrderEtyData poed = new SPurcharseOrderEtyData();

                poed.setIdYear(res.getInt("ety.id_year"));
                poed.setIdDoc(res.getInt("ety.id_doc"));
                poed.setIdEty(res.getInt("ety.id_ety"));
                poed.setConceptKey(res.getString("ety.concept_key"));
                poed.setConcept(res.getString("ety.concept"));
                poed.setRef(res.getString("ety.ref"));
                poed.setQty(res.getFloat("ety.qty"));
                poed.setPriceUnit(res.getFloat("ety.price_u"));
                poed.setsTot(res.getDouble("ety.stot_r"));
                poed.setTaxCharged(res.getDouble("ety.tax_charged_r"));
                poed.setTaxRetained(res.getDouble("ety.tax_retained_r"));
                poed.setTot(res.getDouble("tot_r"));
                poed.setPriceUReal(res.getDouble("price_u_real_r"));
                poed.setPriceUCur(res.getDouble("ety.price_u_cur"));
                poed.setsTotCur(res.getDouble("ety.stot_cur_r"));
                poed.setTaxChargedCur(res.getDouble("ety.tax_charged_cur_r"));
                poed.setTaxRetainedCur(res.getDouble("ety.tax_retained_cur_r"));
                poed.setTotCur(res.getDouble("ety.tot_cur_r"));
                poed.setPriceURealCur(res.getDouble("ety.price_u_real_cur_r"));
                poed.setIdItem(res.getInt("i.id_item"));
                poed.setName(res.getString("i.name"));
                poed.setItem(res.getString("i.item"));
                poed.setUnit(res.getString("u.unit"));
                poed.setIdUnit(res.getInt("u.id_unit"));
                poed.setOriginalUnit(res.getString("ou.unit"));
                poed.setIdOriginalUnit(res.getInt("ou.id_unit"));
               
                lPOEData.add(poed);
            }

            dr.setlPOEData(lPOEData);

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
