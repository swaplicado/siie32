/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.swap.form;

import erp.client.SClientInterface;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataTax;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mloc.data.SDataBolCounty;
import erp.mloc.data.SDataBolLocality;
import erp.mloc.data.SDataBolZipCode;
import erp.mloc.data.SDataCountry;
import erp.mloc.data.SDataState;
import erp.mloc.data.SLocUtils;
import erp.mod.SModConsts;
import erp.mod.cfg.db.SDbFunctionalSubArea;
import erp.mod.fin.db.SFinUtils;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SMassAccountUtils {
    
    /** ERP items. key = item ID; value = ERP item */
    private static final HashMap<Integer, SDataItem> ErpItemsMap = new HashMap<>();
    
    /** ERP units. key = unit ID; value = ERP unit */
    private static final HashMap<Integer, SDataUnit> ErpUnitsMap = new HashMap<>();
    
    /** ERP taxes. key = base tax ID + "-" + tax ID; value = ERP tax */
    private static final HashMap<String, SDataTax> ErpTaxesMap = new HashMap<>();
    
    /** SAT BOL localities. key = locality code ID + "-" + state code ID; value = SAT BOL locality */
    private static final HashMap<String, SDataBolLocality> BolLocalitiesMap = new HashMap<>();
    
    /** SAT BOL counties. key = county code ID + "-" + state code ID; value = SAT BOL county */
    private static final HashMap<String, SDataBolCounty> BolCountiesMap = new HashMap<>();
    
    /** SAT BOL ZIP codes. key = ZIP code ID + "-" + state code ID; value = SAT BOL ZIP code */
    private static final HashMap<String, SDataBolZipCode> BolZipCodesMap = new HashMap<>();
    
    /** ERP states. key = state code; value = ERP state */
    private static final HashMap<String, SDataState> ErpStates = new HashMap<>();
    
    /** ERP countries. key = country code; value = ERP country */
    private static final HashMap<String, SDataCountry> ErpCountries = new HashMap<>();
    
    /** ERP business partners. key: business partner ID; value: business partner */
    private static final HashMap<Integer, SDataBizPartner> BizPartnersMap = new HashMap<>();
    
    /** ERP functional sub-areas. key: functional sub-area ID; value: functional sub-area */
    private static final HashMap<Integer, SDbFunctionalSubArea> FunctionalSubAreassMap = new HashMap<>();
    
    /** Company account codes vs. ID's. key: account code; value: account ID */
    private static final HashMap<String, Integer> AccountCodesMap = new HashMap<>();
    
    /** Company cost center codes vs. ID's. key: cost center code; value: cost center ID */
    private static final HashMap<String, Integer> CostCenterCodesMap = new HashMap<>();
    
    /*
     * Public static methods
     */
    
    /**
     * Get item registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param itemId ID of required registry.
     * @return
     * @throws Exception 
     */
    public static SDataItem getErpItem(final SClientInterface client, final int itemId) throws Exception {
        SDataItem item = ErpItemsMap.get(itemId);

        if (item == null) {
            item = new SDataItem();
            if (item.read(new int[] { itemId }, client.getSession().getStatement()) == SLibConstants.DB_ACTION_READ_OK) {
                ErpItemsMap.put(itemId, item);
            }
            else {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Item ID " + itemId + ")");
            }
        }
        
        return item;
    }
    
    /**
     * Get unit registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param unitId ID of required registry.
     * @return
     * @throws Exception 
     */
    public static SDataUnit getErpUnit(final SClientInterface client, final int unitId) throws Exception {
        SDataUnit unit = ErpUnitsMap.get(unitId);

        if (unit == null) {
            unit = new SDataUnit();
            if (unit.read(new int[] { unitId }, client.getSession().getStatement()) == SLibConstants.DB_ACTION_READ_OK) {
                ErpUnitsMap.put(unitId, unit);
            }
            else {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Unidad ID " + unitId + ")");
            }
        }
        
        return unit;
    }
    
    /**
     * Get tax registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param taxKey PKf required registry.
     * @return
     * @throws Exception 
     */
    public static SDataTax getErpTax(final SClientInterface client, final int[] taxKey) throws Exception {
        String key = SLibUtils.textKey(taxKey);
        SDataTax tax = ErpTaxesMap.get(key);

        if (tax == null) {
            tax = new SDataTax();
            if (tax.read(taxKey, client.getSession().getStatement()) == SLibConstants.DB_ACTION_READ_OK) {
                ErpTaxesMap.put(key, tax);
            }
            else {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Impuesto ID " + key + ")");
            }
        }
        
        return tax;
    }
    
    /**
     * Get BOL locality registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param localityCode Code of required locality.
     * @param stateCode Code of state.
     * @return
     * @throws Exception 
     */
    public static SDataBolLocality getBolLocality(final SClientInterface client, final String localityCode, final String stateCode) throws Exception {
        String key = localityCode + "-" + stateCode;
        SDataBolLocality bolLocality = BolLocalitiesMap.get(key);

        if (bolLocality == null) {
            bolLocality = new SDataBolLocality();
            if (bolLocality.read(new String[] { localityCode, stateCode }, client.getSession().getStatement()) == SLibConstants.DB_ACTION_READ_OK) {
                BolLocalitiesMap.put(key, bolLocality);
            }
            else {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Localidad carta porte ID " + key + ")");
            }
        }
        
        return bolLocality;
    }
    
    /**
     * Get BOL county registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param countyCode Code of required county.
     * @param stateCode Code of state.
     * @return
     * @throws Exception 
     */
    public static SDataBolCounty getBolCounty(final SClientInterface client, final String countyCode, final String stateCode) throws Exception {
        String key = countyCode + "-" + stateCode;
        SDataBolCounty bolCounty = BolCountiesMap.get(key);

        if (bolCounty == null) {
            bolCounty = new SDataBolCounty();
            if (bolCounty.read(new String[] { countyCode, stateCode }, client.getSession().getStatement()) == SLibConstants.DB_ACTION_READ_OK) {
                BolCountiesMap.put(key, bolCounty);
            }
            else {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Municipio carta porte ID " + key + ")");
            }
        }
        
        return bolCounty;
    }
    
    /**
     * Get BOL ZIP-code registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param zipCode Code of required ZIP code.
     * @param stateCode Code of state.
     * @return
     * @throws Exception 
     */
    public static SDataBolZipCode getBolZipCode(final SClientInterface client, final String zipCode, final String stateCode) throws Exception {
        String key = zipCode + "-" + stateCode;
        SDataBolZipCode bolZipCode = BolZipCodesMap.get(key);

        if (bolZipCode == null) {
            bolZipCode = new SDataBolZipCode();
            if (bolZipCode.read(new String[] { zipCode, stateCode }, client.getSession().getStatement()) == SLibConstants.DB_ACTION_READ_OK) {
                BolZipCodesMap.put(key, bolZipCode);
            }
            else {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Código postal carta porte ID " + key + ")");
            }
        }
        
        return bolZipCode;
    }
    
    /**
     * Get state registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param stateCode Code of required state.
     * @return
     * @throws Exception 
     */
    public static SDataState getErpState(final SClientInterface client, final String stateCode) throws Exception {
        SDataState state = ErpStates.get(stateCode);

        if (state == null) {
            state = SLocUtils.readStateByCode(client.getSession().getStatement(), stateCode);
            if (state != null) {
                ErpStates.put(stateCode, state);
            }
            else {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(Estado código " + stateCode + ")");
            }
        }
        
        return state;
    }
    
    /**
     * Get country registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param countryCode Code of required country.
     * @return
     * @throws Exception 
     */
    public static SDataCountry getErpCountry(final SClientInterface client, final String countryCode) throws Exception {
        SDataCountry country = ErpCountries.get(countryCode);

        if (country == null) {
            country = SLocUtils.readCountryByCode(client.getSession().getStatement(), countryCode);
            if (country != null) {
                ErpCountries.put(countryCode, country);
            }
            else {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT + "\n(País código " + countryCode + ")");
            }
        }
        
        return country;
    }
    
    /**
     * Get business partner registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param bizPartnerId ID of required business partner.
     * @return
     */
    public static SDataBizPartner getBizPartner(final SClientInterface client, final int bizPartnerId) {
        SDataBizPartner bizPartner = BizPartnersMap.get(bizPartnerId);
        
        if (bizPartner == null) {
            bizPartner = new SDataBizPartner();
            if (bizPartner.read(new int[] { bizPartnerId }, client.getSession().getStatement()) == SLibConstants.DB_ACTION_READ_OK) {
                BizPartnersMap.put(bizPartnerId, bizPartner);
            }
            else {
                bizPartner = null;
            }
        }
        
        return bizPartner;
    }
    
    /**
     * Get functional sub-area registry. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param funcSubAreaId ID of required functional sub-area.
     * @return
     */
    public static SDbFunctionalSubArea getFunctionalSubArea(final SClientInterface client, final int funcSubAreaId) {
        SDbFunctionalSubArea funcSubArea = FunctionalSubAreassMap.get(funcSubAreaId);
        
        if (funcSubArea == null) {
            funcSubArea = (SDbFunctionalSubArea) client.getSession().readRegistry(SModConsts.CFGU_FUNC_SUB, new int[] { funcSubAreaId });
            FunctionalSubAreassMap.put(funcSubAreaId, funcSubArea);
        }
        
        return funcSubArea;
    }
    
    /**
     * Get company account ID. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param accountCode Code of required account.
     * @return
     */
    public static int getAccountId(final SClientInterface client, final String accountCode) {
        Integer id = AccountCodesMap.get(accountCode);
        
        if (id == null) {
            id = SFinUtils.getAccountId(client.getSession(), accountCode);
            AccountCodesMap.put(accountCode, id);
        }
        
        return id;
    }
    
    /**
     * Get company cost center ID. Get it from memory, otherwise retrieve it from database.
     * @param client GUI client.
     * @param costCenterCode Code of required cost center.
     * @return
     */
    public static int getCostCenterId(final SClientInterface client, final String costCenterCode) {
        Integer id = CostCenterCodesMap.get(costCenterCode);
        
        if (id == null) {
            id = SFinUtils.getCostCenterId(client.getSession(), costCenterCode);
            CostCenterCodesMap.put(costCenterCode, id);
        }
        
        return id;
    }
    
    /**
     * Create Regex Pattern to extract occurrence of "warehouse" word within a text (in Spanish).
     * @return 
     */
    public static Pattern createPatternForWarehouse() {
        String regex = "(?i)\\bbodega\\b";

        return Pattern.compile(regex);
    }
    
    /**
     * Create Regex Pattern to extract scale ticket numbers within a text.
     * Expected format: [BOLETO|BOL|B|TICKET|TIC|T][.] [NÚMERO|NÚM|#][.][:] (999999|999,999|999 999)
     * @return 
     */
    public static Pattern createPatternForScaleTicketBol() {
        String regex =
                "(?i)\\b(?:" +
                "(?:BOL(?:ETO)?\\.?|B\\.?|TICKETE?|TIKETE?|TIQUETE?|TI[CKQ]\\.?|T\\.?)" +
                "\\s*" +
                "(?:N[ÚU]M(?:ERO)?\\.?|NO\\.?|N\\.?|#)?" +
                "\\s*:?" +
                "\\s*" +
                ")?" +
                "(\\d{5,7}|\\d{1,3}(?:[ ,]\\d{3}){1,2})\\b";
        
        return Pattern.compile(regex);
    }
    
    /**
     * Create Regex Pattern to extract scale ticket numbers within a text reference.
     * Expected format: BB/ABC-999999
     * @return 
     */
    public static Pattern createPatternForScaleTicketRef() {
        String regex = "BB/[^-]+-(\\d{5,7})";
        
        return Pattern.compile(regex);
    }
    
    /**
     * Extract the scale ticket number within a text.
     * @param text Text that has a scale ticket number.
     * @param pattern Regex Pattern to extract scale ticket numbers.
     * @param verbose Whether processing messages are required to be displayed in system's console.
     * @return Scale ticket number, when found, other wise an empty string is returned.
     */
    public static String extractScaleTicket(final String text, final Pattern pattern, final boolean verbose) {
        if (verbose) {
            System.out.println("Given text: \"" + text + "\"");
        }
        
        String scaleTicket = "";
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            String rawNumber = matcher.group(1);

            // Normalize: remove spaces or commas
            scaleTicket = rawNumber.replaceAll("[, ]", ""); // normalize (remove commas and blanks) before returning scale ticket

            if (verbose) {
                System.out.println("Found: \"" + rawNumber + "\" -> \"" + scaleTicket + "\".");
            }
        }
        else if (verbose) {
            System.out.println("No match!");
        }
        
        return scaleTicket;
    }
}
