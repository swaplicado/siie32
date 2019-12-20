/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data.cfd;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Sergio Flores
 */
public class SAddendaAmc71XmlHeader implements SAddenda {
    
    public static final String AMECE_71 = "AMC7.1";
    
    public static final String SUP_GLN = "sup_gln";
    public static final String SUP_NUM = "sup_num";
    public static final String COM_GLN = "com_gln";
    public static final String COM_CON = "com_con";
    public static final String COM_BRA_GLN = "com_bra_gln";
    public static final String SHIP_TO = "ship_to";
    public static final String SHIP_TO_NAME = "name";
    public static final String SHIP_TO_ADDR = "addr";
    public static final String SHIP_TO_CITY = "city";
    public static final String SHIP_TO_PC = "pc";
    
    public SAddendaAmc71Supplier Supplier;
    public SAddendaAmc71Company Company;
    public SAddendaAmc71CompanyBranch CompanyBranch;
    
    public SAddendaAmc71XmlHeader() {
        reset();
    }
    
    public void reset() {
        CompanyBranch = new SAddendaAmc71CompanyBranch();
        
        Company = new SAddendaAmc71Company();
        Company.CompanyBranches.add(CompanyBranch);
        
        Supplier = new SAddendaAmc71Supplier();
        Supplier.Companies.add(Company);
    }
    
    @Override
    public String encodeJson() {
        JSONObject root = new JSONObject();
        root.put(SAddendaConsts.ADDENDA, AMECE_71);
        
        JSONObject data = new JSONObject();
        data.put(SUP_GLN, Supplier.SupplierGln);
        data.put(SUP_NUM, Supplier.SupplierNumber);
        data.put(COM_GLN, Company.CompanyGln);
        data.put(COM_CON, Company.CompanyContact);
        data.put(COM_BRA_GLN, CompanyBranch.CompanyBranchGln);
        
        JSONObject shipTo = new JSONObject();
        shipTo.put(SHIP_TO_NAME, CompanyBranch.ShipToName);
        shipTo.put(SHIP_TO_ADDR, CompanyBranch.ShipToAddress);
        shipTo.put(SHIP_TO_CITY, CompanyBranch.ShipToCity);
        shipTo.put(SHIP_TO_PC, CompanyBranch.ShipToPostalCode);
        
        data.put(SHIP_TO, shipTo);
        
        root.put(SAddendaConsts.DATA, data);
        
        return root.toJSONString();
    }
    
    @Override
    public void decodeJson(final String json) throws ParseException, Exception {
        reset();
        
        JSONParser parser = new JSONParser();
        
        JSONObject root = (JSONObject) parser.parse(json);
        
        String addenda = (String) root.get(SAddendaConsts.ADDENDA);
        
        if (addenda == null || !addenda.equals(AMECE_71)) {
            throw new Exception(SAddendaConsts.ERR_WRONG_ADDENDA);
        }
        
        JSONObject data = (JSONObject) root.get(SAddendaConsts.DATA);
        Supplier.SupplierGln = SAddendaUtils.normalize((String) data.get(SUP_GLN));
        Supplier.SupplierNumber = SAddendaUtils.normalize((String) data.get(SUP_NUM));
        Company.CompanyGln = SAddendaUtils.normalize((String) data.get(COM_GLN));
        Company.CompanyContact = SAddendaUtils.normalize((String) data.get(COM_CON));
        CompanyBranch.CompanyBranchGln = SAddendaUtils.normalize((String) data.get(COM_BRA_GLN));
        
        JSONObject shipTo = (JSONObject) data.get(SHIP_TO);
        CompanyBranch.ShipToName = SAddendaUtils.normalize((String) shipTo.get(SHIP_TO_NAME));
        CompanyBranch.ShipToAddress = SAddendaUtils.normalize((String) shipTo.get(SHIP_TO_ADDR));
        CompanyBranch.ShipToCity = SAddendaUtils.normalize((String) shipTo.get(SHIP_TO_CITY));
        CompanyBranch.ShipToPostalCode = SAddendaUtils.normalize((String) shipTo.get(SHIP_TO_PC));
    }
}
