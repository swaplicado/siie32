/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import java.util.Date;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Sergio Flores
 */
public class SAddendaAmc71XmlLine implements SAddenda {
    
    public static final String PUR_ORD = "pur_ord";
    public static final String PUR_ORD_DATE = "pur_ord_date";
    public static final String PUR_BARCODE = "barcode";

    public String PurchaseOrder;
    public Date PurchaseOrderDate;
    public String Barcode;
    
    public SAddendaAmc71XmlLine() {
        reset();
    }
    
    public final void reset() {
        PurchaseOrder = "";
        PurchaseOrderDate = null;
        Barcode = "";
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public String encodeJson() {
        JSONObject root = new JSONObject();
        root.put(SAddendaConsts.ADDENDA, SAddendaAmc71XmlHeader.AMECE_71);
        
        JSONObject data = new JSONObject();
        data.put(PUR_ORD, PurchaseOrder);
        data.put(PUR_ORD_DATE, SAddendaUtils.DateFormatIso.format(PurchaseOrderDate));
        data.put(PUR_BARCODE, Barcode);
        
        root.put(SAddendaConsts.DATA, data);
        
        return root.toJSONString();
    }
    
    @Override
    public void decodeJson(final String json) throws ParseException, Exception {
        JSONParser parser = new JSONParser();
        
        JSONObject root = (JSONObject) parser.parse(json);
        
        String addenda = (String) root.get(SAddendaConsts.ADDENDA);
        
        if (addenda == null || !addenda.equals(SAddendaAmc71XmlHeader.AMECE_71)) {
            throw new Exception(SAddendaConsts.ERR_WRONG_ADDENDA);
        }
        
        JSONObject data = (JSONObject) root.get(SAddendaConsts.DATA);
        
        PurchaseOrder = SAddendaUtils.normalize((String) data.get(PUR_ORD));
        
        String date = SAddendaUtils.normalize((String) data.get(PUR_ORD_DATE));
        PurchaseOrderDate = date.isEmpty() ? null : SAddendaUtils.DateFormatIso.parse(date);
        
        Barcode = SAddendaUtils.normalize((String) data.get(PUR_BARCODE));
    }
}
