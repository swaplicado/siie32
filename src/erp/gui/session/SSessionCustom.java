/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import erp.cfd.SXmlCatalog;
import erp.gui.account.SAccount;
import erp.gui.account.SAccountConsts;
import erp.gui.account.SAccountLedger;
import erp.gui.account.SAccountUtils;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.mod.fin.db.SDbAbpBizPartner;
import erp.mod.fin.db.SDbAbpBizPartnerLink;
import erp.mod.fin.db.SDbAbpEntity;
import erp.mod.fin.db.SDbAbpEntityLink;
import erp.mod.fin.db.SDbAbpItem;
import erp.mod.fin.db.SDbAbpItemLink;
import erp.mod.fin.db.SDbAbpTax;
import erp.mtrn.data.SDataDpsEntryTax;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibKey;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiSessionCustom;

/**
 *
 * @author Sergio Flores
 */
public class SSessionCustom implements SGuiSessionCustom {

    private SGuiSession moSession;
    private int[] manLocalCountryKey;
    private int[] manLocalCurrencyKey;
    private String msLocalCountry;
    private String msLocalCountryCode;
    private String msLocalCurrency;
    private String msLocalCurrencyCode;
    private String msLocalLanguage;

    private long mlSessionNumber;
    private int[] manCurrentCompanyKey;
    private int[] manCurrentBranchKey;
    private int[] manCurrentCashKey;
    private int[] manCurrentWarehouseKey;
    private int[] manCurrentPlantKey;

    //private BizPartnerCompanyRegistry...

    private HashMap<Integer, SSessionUnit> moUnitsMap;
    private HashMap<Integer, SSessionItem> moItemsMap;
    private HashMap<Integer, SSessionTaxGroup> moTaxGroupsMap;
    private HashMap<SLibKey, SSessionTax> moTaxesMap;
    private ArrayList<SSessionTaxItemLink> maTaxItemLinksArray;

    private ArrayList<SAccountLedger> maAccountLedgers;
    private ArrayList<SAccountLedger> maCostCenterLedgers;

    private HashMap<Integer, SDbAbpEntity> moAbpEntitiesMap;
    private ArrayList<SDbAbpEntityLink> maAbpEntitiesLinksArray;
    private HashMap<Integer, SDbAbpBizPartner> moAbpBizPartnersMap;
    private ArrayList<SDbAbpBizPartnerLink> maAbpBizPartnersLinksArray;
    private HashMap<Integer, SDbAbpItem> moAbpItemsMap;
    private ArrayList<SDbAbpItemLink> maAbpItemsLinksArray;
    private ArrayList<SDbAbpTax> maAbpTaxesArray;
    
    private SXmlCatalog moXmlCatalog;

    /**
     * Creates new SSessionCustom object.
     */
    public SSessionCustom(SGuiSession session) {
        moSession = session;
        moUnitsMap = new HashMap<Integer, SSessionUnit>();
        moItemsMap = new HashMap<Integer, SSessionItem>();
        moTaxGroupsMap = new HashMap<Integer, SSessionTaxGroup>();
        moTaxesMap = new HashMap<SLibKey, SSessionTax>();
        maTaxItemLinksArray = new ArrayList<SSessionTaxItemLink>();

        maAccountLedgers = new ArrayList<SAccountLedger>();
        maCostCenterLedgers = new ArrayList<SAccountLedger>();

        moAbpEntitiesMap = new HashMap<Integer, SDbAbpEntity>();
        maAbpEntitiesLinksArray = new ArrayList<SDbAbpEntityLink>();
        moAbpBizPartnersMap = new HashMap<Integer, SDbAbpBizPartner>();
        maAbpBizPartnersLinksArray = new ArrayList<SDbAbpBizPartnerLink>();
        moAbpItemsMap = new HashMap<Integer, SDbAbpItem>();
        maAbpItemsLinksArray = new ArrayList<SDbAbpItemLink>();
        maAbpTaxesArray = new ArrayList<SDbAbpTax>();
        
        moXmlCatalog = null;
    }

    /*
     * Private methods
     */

    /**
     * @param idItem Item ID.
     * @param idUnitSource Source unit ID.
     * @param idUnitDestiny  Destiny unit ID.
     * @param isForQuantity Quantity (when <code>true</code>) of unit price (when <code>false</code>) request.
     */
    private double computeUnitsFactor(final int idItem, final int idUnitSource, final int idUnitDestiny, final boolean isForQuantity) throws SQLException, Exception {
        double factor = 1;
        SSessionItem sessionItem = null;
        SSessionUnit sessionUnitSource = moUnitsMap.get(idUnitSource);
        SSessionUnit sessionUnitDestiny = moUnitsMap.get(idUnitDestiny);

        if (sessionUnitSource != null && sessionUnitDestiny != null) {
            if (sessionUnitSource.getFkUnitTypeId() == sessionUnitDestiny.getFkUnitTypeId()) {
                // Source and destiny units are same type:
                
                if (sessionUnitSource.getUnitBaseEquivalence() != 0d && sessionUnitDestiny.getUnitBaseEquivalence() != 0d) {
                    if (isForQuantity) {
                        factor = sessionUnitSource.getUnitBaseEquivalence() / sessionUnitDestiny.getUnitBaseEquivalence();
                    }
                    else {
                        factor = 1d / sessionUnitSource.getUnitBaseEquivalence() * sessionUnitDestiny.getUnitBaseEquivalence();
                    }
                }
            }
            else {
                // Source and destiny units are different type, use alternative unit if possible:
                
                sessionItem = getSessionItem(idItem);
                if (sessionItem != null) {
                    if (sessionUnitSource.getFkUnitTypeId() == sessionItem.getFkUnitAlternativeTypeId()) {
                        if (sessionUnitSource.getUnitBaseEquivalence() != 0d && sessionItem.getUnitAlternativeBaseEquivalence() != 0d) {
                            if (isForQuantity) {
                                factor = sessionUnitSource.getUnitBaseEquivalence() / sessionItem.getUnitAlternativeBaseEquivalence();
                            }
                            else {
                                factor = 1d / sessionUnitSource.getUnitBaseEquivalence() * sessionItem.getUnitAlternativeBaseEquivalence();
                            }
                        }
                    }
                }
            }
        }

        return factor;
    }

    private ArrayList<SSessionTaxItemLink> getTaxItemLinks(final int idLink, final int idReference) {
        ArrayList<SSessionTaxItemLink> taxItemLinks = new ArrayList<SSessionTaxItemLink>();

        for (SSessionTaxItemLink til : maTaxItemLinksArray) {
            if (til.getPkLinkId() == idLink && til.getPkReferenceId() == idReference) {
                taxItemLinks.add(til);
            }
        }

        return taxItemLinks;
    }

    private ArrayList<SAccount> readChildrenAccounts(final int type, final String accountCodeStd, final int mask, final int deep, final int level, final Statement[] statements) throws SQLException, Exception {
        String sql = "";
        String field = "";
        String table = "";
        ResultSet resultSet = null;
        SAccount account = null;
        ArrayList<SAccount> children = new ArrayList<SAccount>();

        switch (type) {
            case SAccountConsts.TYPE_ACCOUNT:
                field = "acc";
                table = SModConsts.TablesMap.get(SModConsts.FIN_ACC);
                break;
            case SAccountConsts.TYPE_COST_CENTER:
                field = "cc";
                table = SModConsts.TablesMap.get(SModConsts.FIN_CC);
                break;
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (level >= 2 && level <= deep && level <= SAccountConsts.LEVELS) {
            sql = "SELECT pk_" + field + ", code, " + field + ", b_del "
                + "FROM " + table + " "
                + "WHERE code LIKE '" + SAccountUtils.subtractCodeStd(accountCodeStd, level - 1) + "%' AND lev = " + level + " "
                + "ORDER BY code ";
            resultSet = statements[level - 1].executeQuery(sql);
            while (resultSet.next()) {
                account = new SAccount(resultSet.getInt("pk_" + field), resultSet.getString("code"), resultSet.getString(field), resultSet.getBoolean("b_del"), level, SAccountUtils.getDigits(mask, level));

                if (level < deep && level < SAccountConsts.LEVELS) {
                    account.getChildren().addAll(readChildrenAccounts(type, account.getCodeStd(), mask, deep, level + 1, statements));
                }

                children.add(account);
            }
        }

        return children;
    }

    /*
     * Public methods
     */

    public void setSessionNumber(long l) { mlSessionNumber = l; }
    public void setCurrentCompanyKey(int[] key) { manCurrentCompanyKey = key; }
    public void setCurrentBranchKey(int[] key) { manCurrentBranchKey = key; }
    public void setCurrentCashKey(int[] key) { manCurrentCashKey = key; }
    public void setCurrentWarehouseKey(int[] key) { manCurrentWarehouseKey = key; }
    public void setCurrentPlantKey(int[] key) { manCurrentPlantKey = key; }

    public long getSessionNumber() { return mlSessionNumber; }
    public int[] getCurrentCompanyKey() { return manCurrentCompanyKey; }
    public int[] getCurrentBranchKey() { return manCurrentBranchKey; }
    public int[] getCurrentCashKey() { return manCurrentCashKey; }
    public int[] getCurrentWarehouseKey() { return manCurrentWarehouseKey; }
    public int[] getCurrentPlantKey() { return manCurrentPlantKey; }

    public void setLocalCountryKey(int[] key) { manLocalCountryKey = key; }
    public void setLocalCurrencyKey(int[] key) { manLocalCurrencyKey = key; }
    public void setLocalCountry(String name) { msLocalCountry = name; }
    public void setLocalCountryCode(String code) { msLocalCountryCode = code; }
    public void setLocalCurrency(String name) { msLocalCurrency = name; }
    public void setLocalCurrencyCode(String code) { msLocalCurrencyCode = code; }
    public void setLocalLanguage(String name) { msLocalLanguage = name; }

    public ArrayList<SAccountLedger> getAccountLedgers() { return maAccountLedgers; }
    public ArrayList<SAccountLedger> getCostCenterLedgers() { return maCostCenterLedgers; }
    
    public SXmlCatalog getXmlCatalog() { return moXmlCatalog; }

    @Override
    public int[] getLocalCountryKey() {
        return manLocalCountryKey;
    }

    @Override
    public String getLocalCountry() {
        return msLocalCountry;
    }

    @Override
    public String getLocalCountryCode() {
        return msLocalCountryCode;
    }

    @Override
    public boolean isLocalCountry(int[] key) {
        return SLibUtils.compareKeys(key, manLocalCountryKey);
    }

    @Override
    public int[] getLocalCurrencyKey() {
        return manLocalCurrencyKey;
    }

    @Override
    public String getLocalCurrency() {
        return msLocalCurrency;
    }

    @Override
    public String getLocalCurrencyCode() {
        return msLocalCurrencyCode;
    }

    @Override
    public boolean isLocalCurrency(int[] key) {
        return SLibUtils.compareKeys(key, manLocalCurrencyKey);
    }

    @Override
    public String getLocalLanguage() {
        return msLocalLanguage;
    }

    @Override
    public String getCountry(int[] key) {
        return (String) (String) moSession.readField(SModConsts.LOCU_CTY, key, SDbRegistry.FIELD_NAME);
    }

    @Override
    public String getCountryCode(int[] key) {
        return (String) (String) moSession.readField(SModConsts.LOCU_CTY, key, SDbRegistry.FIELD_CODE);
    }

    @Override
    public String getCurrency(int[] key) {
        return (String) (String) moSession.readField(SModConsts.CFGU_CUR, key, SDbRegistry.FIELD_NAME);
    }

    @Override
    public String getCurrencyCode(int[] key) {
        return (String) (String) moSession.readField(SModConsts.CFGU_CUR, key, SDbRegistry.FIELD_CODE);
    }

    @Override
    public String getLanguage(int[] key) {
        return (String) (String) moSession.readField(SModConsts.CFGU_LAN, key, SDbRegistry.FIELD_NAME);
    }

    @Override
    public int getTerminal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateSessionSettings() {
        int maxLevels = 0;
        int maskAccount = 0;
        int maskCostCenter = 0;
        int[] keyType = null;
        int[] keyLanguage = null;
        String sql = "";
        ResultSet resultSet = null;
        ResultSet resultSetAux = null;
        Statement statementAux = null;
        Statement[] statements = null;
        SSessionUnit unit = null;
        SSessionItem item = null;
        SSessionTax tax = null;
        SSessionTaxGroup taxGroup = null;
        SSessionTaxGroupEntry taxGroupEntry = null;
        SSessionTaxItemLink taxItemLink = null;
        SAccountLedger accountLedger = null;
        SAccountLedger costCenterLedger = null;
        SDbAbpEntity abpEntity = null;
        SDbAbpEntityLink abpEntityLink = null;
        SDbAbpBizPartner abpBizPartner = null;
        SDbAbpBizPartnerLink abpBizPartnerLink = null;
        SDbAbpItem abpItem = null;
        SDbAbpItemLink abpItemLink = null;
        SDbAbpTax abpTax = null;

        try {
            // System settings: 

            sql = "SELECT fid_cur, fid_cty, fid_lan FROM " + SModConsts.TablesMap.get(SModConsts.CFG_PARAM_ERP) + " ";
            resultSet = moSession.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                manLocalCurrencyKey = new int[] { resultSet.getInt(1) };
                manLocalCountryKey = new int[] { resultSet.getInt(2) };
                keyLanguage = new int[] { resultSet.getInt(3) };
            }

            msLocalCountry = (String) moSession.readField(SModConsts.LOCU_CTY, manLocalCountryKey, SDbRegistry.FIELD_NAME);
            msLocalCountryCode = (String) moSession.readField(SModConsts.LOCU_CTY, manLocalCountryKey, SDbRegistry.FIELD_CODE);

            msLocalCurrency = (String) moSession.readField(SModConsts.CFGU_CUR, manLocalCurrencyKey, SDbRegistry.FIELD_NAME);
            msLocalCurrencyCode = (String) moSession.readField(SModConsts.CFGU_CUR, manLocalCurrencyKey, SDbRegistry.FIELD_CODE);

            msLocalLanguage = (String) moSession.readField(SModConsts.CFGU_CUR, keyLanguage, SDbRegistry.FIELD_CODE);

            // Units (all registries):

            moUnitsMap.clear();
            sql = "SELECT id_unit, unit_base_equiv, fid_tp_unit "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " "
                    + "ORDER BY id_unit ";
            resultSet = moSession.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                unit = new SSessionUnit(resultSet.getInt("id_unit"));
                unit.setFkUnitTypeId(resultSet.getInt("fid_tp_unit"));
                unit.setUnitBaseEquivalence(resultSet.getDouble("unit_base_equiv"));
                moUnitsMap.put(unit.getPkUnitId(), unit);
            }

            // Items (all registries):

            moItemsMap.clear();
            sql = "SELECT i.id_item, i.fid_mfr, i.fid_brd, i.fid_igen, i.fid_line_n, i.unit_alt_base_equiv, i.fid_tp_unit_alt, "
                    + "igrp.id_igrp, igrp.fid_ifam, itp.tp_idx, icl.cl_idx, ict.ct_idx "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON i.fid_igen = igen.id_igen "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGRP) + " AS igrp ON igen.fid_igrp = igrp.id_igrp "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_TP_ITEM) + " itp ON igen.fid_ct_item = itp.id_ct_item AND igen.fid_cl_item = itp.id_cl_item AND igen.fid_tp_item = itp.id_tp_item "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_CL_ITEM) + " icl ON igen.fid_ct_item = icl.id_ct_item AND igen.fid_cl_item = icl.id_cl_item "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_CT_ITEM) + " ict ON igen.fid_ct_item = ict.id_ct_item "
                    + "ORDER BY i.id_item ";
            resultSet = moSession.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                item = new SSessionItem(resultSet.getInt("i.id_item"));
                item.setFkPropManufacturerId(resultSet.getInt("i.fid_mfr"));
                item.setFkPropBrandId(resultSet.getInt("i.fid_brd"));
                item.setFkItemLineId(resultSet.getInt("i.fid_line_n"));
                item.setFkItemGenericId(resultSet.getInt("i.fid_igen"));
                item.setFkItemGroupId(resultSet.getInt("igrp.id_igrp"));
                item.setFkItemFamilyId(resultSet.getInt("igrp.fid_ifam"));
                item.setFkTypeTypeId(resultSet.getInt("itp.tp_idx"));
                item.setFkTypeClassId(resultSet.getInt("icl.cl_idx"));
                item.setFkTypeCategoryId(resultSet.getInt("ict.ct_idx"));
                item.setFkUnitAlternativeTypeId(resultSet.getInt("i.fid_tp_unit_alt"));
                item.setUnitAlternativeBaseEquivalence(resultSet.getDouble("i.unit_alt_base_equiv"));
                moItemsMap.put(item.getPkItemId(), item);
            }

            // Tax groups (only undeleted registries):

            moTaxGroupsMap.clear();
            sql = "SELECT id_tax_grp "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_TAX_GRP) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY id_tax_grp ";
            resultSet = moSession.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                taxGroup = new SSessionTaxGroup(resultSet.getInt("id_tax_grp"));
                moTaxGroupsMap.put(taxGroup.getPkTaxGroupId(), taxGroup);
            }

            for (SSessionTaxGroup tg : moTaxGroupsMap.values()) {
                sql = "SELECT id_tp_tax_idy_emir, id_tp_tax_idy_recr, app_order, dt_start, dt_end_n, fid_tax_bas, fid_tax "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_TAX_GRP_ETY) + " "
                        + "WHERE id_tax_grp = " + tg.getPkTaxGroupId() + " "
                        + "ORDER BY id_tp_tax_idy_emir, id_tp_tax_idy_recr, dt_start, dt_end_n, app_order, fid_tax_bas, fid_tax ";
                resultSet = moSession.getStatement().executeQuery(sql);
                while (resultSet.next()) {
                    taxGroupEntry = new SSessionTaxGroupEntry(resultSet.getInt("id_tp_tax_idy_emir"), resultSet.getInt("id_tp_tax_idy_recr"));
                    taxGroupEntry.setApplicationOrder(resultSet.getInt("app_order"));
                    taxGroupEntry.setDateStart(resultSet.getDate("dt_start"));
                    taxGroupEntry.setDateEnd_n(resultSet.getDate("dt_end_n"));
                    taxGroupEntry.setFkTaxBasicId(resultSet.getInt("fid_tax_bas"));
                    taxGroupEntry.setFkTaxId(resultSet.getInt("fid_tax"));
                    tg.getEntries().add(taxGroupEntry);
                }
            }

            // Taxes (all registries):

            moTaxesMap.clear();
            sql = "SELECT t.id_tax_bas, t.id_tax, t.per, t.val_u, t.val, t.fid_tp_tax, t.fid_tp_tax_cal, t.fid_tp_tax_app, "
                    + "t.tax, tt.tp_tax, tc.tp_tax_cal, ta.tp_tax_app "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FINU_TAX) + " AS t "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_TAX) + " AS tt ON t.fid_tp_tax = tt.id_tp_tax "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_TAX_CAL) + " AS tc ON t.fid_tp_tax_cal = tc.id_tp_tax_cal "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.FINS_TP_TAX_APP) + " AS ta ON t.fid_tp_tax_app = ta.id_tp_tax_app "
                    + "ORDER BY id_tax_bas, id_tax ";
            resultSet = moSession.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                tax = new SSessionTax(resultSet.getInt("t.id_tax_bas"), resultSet.getInt("t.id_tax"));
                tax.setPercentage(resultSet.getDouble("t.per"));
                tax.setValueUnitary(resultSet.getDouble("t.val_u"));
                tax.setValue(resultSet.getDouble("t.val"));
                tax.setFkTaxTypeId(resultSet.getInt("t.fid_tp_tax"));
                tax.setFkTaxCalculationTypeId(resultSet.getInt("t.fid_tp_tax_cal"));
                tax.setFkTaxApplicationTypeId(resultSet.getInt("t.fid_tp_tax_app"));
                tax.setTax(resultSet.getString("t.tax"));
                tax.setTaxType(resultSet.getString("tt.tp_tax"));
                tax.setTaxCalculationType(resultSet.getString("tc.tp_tax_cal"));
                tax.setTaxApplicationType(resultSet.getString("ta.tp_tax_app"));
                moTaxesMap.put(tax.getTaxKey(), tax);
            }

            // Tax item links (only undeleted registries):

            maTaxItemLinksArray.clear();
            sql = "SELECT id_link, id_ref, dt_start, fk_tax_reg, fk_tax_grp "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_TAX_ITEM_LINK) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY id_link, id_ref, dt_start, fk_tax_reg, fk_tax_grp ";
            resultSet = moSession.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                taxItemLink = new SSessionTaxItemLink(resultSet.getInt("id_link"), resultSet.getInt("id_ref"));
                taxItemLink.setDateStart(resultSet.getDate("dt_start"));
                taxItemLink.setFkTaxRegionId(resultSet.getInt("fk_tax_reg"));
                taxItemLink.setFkTaxGroupId(resultSet.getInt("fk_tax_grp"));
                maTaxItemLinksArray.add(taxItemLink);
            }

            // Prepare statements:

            maskAccount = ((SDataParamsCompany) moSession.getConfigCompany()).getMaskAccount();
            maskCostCenter = ((SDataParamsCompany) moSession.getConfigCompany()).getMaskCostCenter();

            maxLevels = SAccountUtils.getLevels(maskAccount);
            if (SAccountUtils.getLevels(maskCostCenter) > maxLevels) {
                maxLevels = SAccountUtils.getLevels(maskCostCenter);
            }

            statements = new Statement[maxLevels];
            for (int i = 0; i < maxLevels; i++) {
                statements[i] = moSession.getStatement().getConnection().createStatement();
            }

            // Accounts (all registries):

            maAccountLedgers.clear();
            sql = "SELECT pk_acc, code, acc, deep, b_del, fid_tp_acc_r, fid_cl_acc_r, fid_cls_acc_r "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC) + " "
                    + "WHERE lev = 1 "
                    + "ORDER BY code ";
            resultSet = moSession.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                keyType = new int[] { resultSet.getInt("fid_tp_acc_r"), resultSet.getInt("fid_cl_acc_r"), resultSet.getInt("fid_cls_acc_r") };
                accountLedger = new SAccountLedger(resultSet.getInt("pk_acc"), resultSet.getString("code"), resultSet.getString("acc"), resultSet.getBoolean("b_del"),
                        resultSet.getInt("deep"), SAccountUtils.getDigits(maskAccount, 1), keyType);
                if (accountLedger.getDeep() > 1) {
                    accountLedger.getChildren().addAll(readChildrenAccounts(SAccountConsts.TYPE_ACCOUNT, accountLedger.getCodeStd(), maskAccount, accountLedger.getDeep(), 2, statements));
                }
                maAccountLedgers.add(accountLedger);
            }

            // Cost centers (all registries):

            maCostCenterLedgers.clear();
            sql = "SELECT pk_cc, code, cc, deep, b_del "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_CC) + " "
                    + "WHERE lev = 1 "
                    + "ORDER BY code ";
            resultSet = moSession.getStatement().executeQuery(sql);
            while (resultSet.next()) {
                costCenterLedger = new SAccountLedger(resultSet.getInt("pk_cc"), resultSet.getString("code"), resultSet.getString("cc"), resultSet.getBoolean("b_del"),
                        resultSet.getInt("deep"), SAccountUtils.getDigits(maskCostCenter, 1));
                if (costCenterLedger.getDeep() > 1) {
                    costCenterLedger.getChildren().addAll(readChildrenAccounts(SAccountConsts.TYPE_COST_CENTER, costCenterLedger.getCodeStd(), maskCostCenter, costCenterLedger.getDeep(), 2, statements));
                }
                maCostCenterLedgers.add(costCenterLedger);
            }

            // Automatic bookkeeping packages:

            statementAux = moSession.getStatement().getConnection().createStatement();

            // For entities (only undeleted registries):

            moAbpEntitiesMap.clear();
            sql = "SELECT id_abp_ent "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_ENT) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY id_abp_ent ";
            resultSetAux = statementAux.executeQuery(sql);
            while (resultSetAux.next()) {
                abpEntity = new SDbAbpEntity();
                abpEntity.read(moSession, new int[] { resultSetAux.getInt(1) });
                moAbpEntitiesMap.put(abpEntity.getPkAbpEntityId(), abpEntity);
            }

            maAbpEntitiesLinksArray.clear();
            sql = "SELECT id_abp_ent, id_cob, id_ent "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_ENT_LINK) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY id_cob, id_ent, id_abp_ent ";
            resultSetAux = statementAux.executeQuery(sql);
            while (resultSetAux.next()) {
                abpEntityLink = new SDbAbpEntityLink();
                abpEntityLink.read(moSession, new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2), resultSetAux.getInt(3) });
                maAbpEntitiesLinksArray.add(abpEntityLink);
            }

            // For business partners (only undeleted registries):

            moAbpBizPartnersMap.clear();
            sql = "SELECT id_abp_bp "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_BP) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY id_abp_bp ";
            resultSetAux = statementAux.executeQuery(sql);
            while (resultSetAux.next()) {
                abpBizPartner = new SDbAbpBizPartner();
                abpBizPartner.read(moSession, new int[] { resultSetAux.getInt(1) });
                moAbpBizPartnersMap.put(abpBizPartner.getPkAbpBizPartnerId(), abpBizPartner);
            }

            maAbpBizPartnersLinksArray.clear();
            sql = "SELECT id_abp_bp, id_link, id_ref "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_BP_LINK) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY id_link, id_ref, id_abp_bp ";
            resultSetAux = statementAux.executeQuery(sql);
            while (resultSetAux.next()) {
                abpBizPartnerLink = new SDbAbpBizPartnerLink();
                abpBizPartnerLink.read(moSession, new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2), resultSetAux.getInt(3) });
                maAbpBizPartnersLinksArray.add(abpBizPartnerLink);
            }

            // For items (only undeleted registries):

            moAbpItemsMap.clear();
            sql = "SELECT id_abp_item "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_ITEM) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY id_abp_item ";
            resultSetAux = statementAux.executeQuery(sql);
            while (resultSetAux.next()) {
                abpItem = new SDbAbpItem();
                abpItem.read(moSession, new int[] { resultSetAux.getInt(1) });
                moAbpItemsMap.put(abpItem.getPkAbpItemId(), abpItem);
            }

            maAbpItemsLinksArray.clear();
            sql = "SELECT id_abp_item, id_link, id_ref "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_ITEM_LINK) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY id_link, id_ref, id_abp_item ";
            resultSetAux = statementAux.executeQuery(sql);
            while (resultSetAux.next()) {
                abpItemLink = new SDbAbpItemLink();
                abpItemLink.read(moSession, new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2), resultSetAux.getInt(3) });
                maAbpItemsLinksArray.add(abpItemLink);
            }

            // For taxes (only undeleted registries):

            maAbpTaxesArray.clear();
            sql = "SELECT id_abp_tax, fk_tax_bas, fk_tax "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ABP_TAX) + " "
                    + "WHERE b_del = 0 "
                    + "ORDER BY fk_tax_bas, fk_tax, id_abp_tax ";
            resultSetAux = statementAux.executeQuery(sql);
            while (resultSetAux.next()) {
                abpTax = new SDbAbpTax();
                abpTax.read(moSession, new int[] { resultSetAux.getInt(1) });
                maAbpTaxesArray.add(abpTax);
            }
            
            moXmlCatalog = new SXmlCatalog(moSession);
        }
        catch (SQLException e) {
            SLibUtils.showException(this, e);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            try {
                for (Statement statement : statements) {
                    if (statement != null && !statement.isClosed()) {
                        statement.close();
                    }
                }
            }
            catch (SQLException e) {
                SLibUtils.showException(this, e);
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
        }
    }

    public SSessionItem createSessionItem(final int idItem) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;
        SSessionItem item = null;

        sql = "SELECT i.id_item, i.fid_mfr, i.fid_brd, i.fid_igen, i.fid_line_n, i.unit_alt_base_equiv, i.fid_tp_unit_alt, "
                + "igrp.id_igrp, igrp.fid_ifam, itp.tp_idx, icl.cl_idx, ict.ct_idx "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGEN) + " AS igen ON i.fid_igen = igen.id_igen AND i.id_item = " + idItem + " "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_IGRP) + " AS igrp ON igen.fid_igrp = igrp.id_igrp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_TP_ITEM) + " itp ON igen.fid_ct_item = itp.id_ct_item AND igen.fid_cl_item = itp.id_cl_item AND igen.fid_tp_item = itp.id_tp_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_CL_ITEM) + " icl ON igen.fid_ct_item = icl.id_ct_item AND igen.fid_cl_item = icl.id_cl_item "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMS_CT_ITEM) + " ict ON igen.fid_ct_item = ict.id_ct_item ";
        resultSet = moSession.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            item = new SSessionItem(resultSet.getInt("i.id_item"));
            item.setFkPropManufacturerId(resultSet.getInt("i.fid_mfr"));
            item.setFkPropBrandId(resultSet.getInt("i.fid_brd"));
            item.setFkItemLineId(resultSet.getInt("i.fid_line_n"));
            item.setFkItemGenericId(resultSet.getInt("i.fid_igen"));
            item.setFkItemGroupId(resultSet.getInt("igrp.id_igrp"));
            item.setFkItemFamilyId(resultSet.getInt("igrp.fid_ifam"));
            item.setFkTypeTypeId(resultSet.getInt("itp.tp_idx"));
            item.setFkTypeClassId(resultSet.getInt("icl.cl_idx"));
            item.setFkTypeCategoryId(resultSet.getInt("ict.ct_idx"));
            item.setFkUnitAlternativeTypeId(resultSet.getInt("i.fid_tp_unit_alt"));
            item.setUnitAlternativeBaseEquivalence(resultSet.getDouble("i.unit_alt_base_equiv"));
        }

        return item;
    }

    public SSessionItem getSessionItem(final int idItem) throws SQLException, Exception {
        SSessionItem item = moItemsMap.get(idItem);

        if (item == null) {
            item = createSessionItem(idItem);
            moItemsMap.put(item.getPkItemId(), item);
        }

        return item;
    }

    public double getUnitsFactorForQuantity(int idItem, int idUnitSource, int idUnitDestiny) {
        double factor = 0;

        try {
            factor = computeUnitsFactor(idItem, idUnitSource, idUnitDestiny, true);
        }
        catch (Exception e) {
            factor = 1;
            SLibUtils.showException(this, e);
        }

        return factor;
    }

    public double getUnitsFactorForUnitaryValue(int idItem, int idUnitSource, int idUnitDestiny) {
        double factor = 0;

        try {
            factor = computeUnitsFactor(idItem, idUnitSource, idUnitDestiny, false);
        }
        catch (Exception e) {
            factor = 1;
            SLibUtils.showException(this, e);
        }

        return factor;
    }

    public SSessionTaxGroup getTaxGroup(int idItem, int idTaxRegion, Date date) throws SQLException, Exception {
        ArrayList<SSessionTaxItemLink> taxItemLinks = null;
        SSessionTaxGroup taxGroup = null;
        SSessionItem item = getSessionItem(idItem);

        if (item != null) {
            for (int link : SSessionConsts.ITEM_LINKS) {
                taxItemLinks = getTaxItemLinks(link, item.getReferenceId(link));

                for (SSessionTaxItemLink til : taxItemLinks) {
                    if (idTaxRegion == til.getFkTaxRegionId() && date.compareTo(til.getDateStart()) >= 0) {
                        taxGroup = moTaxGroupsMap.get(til.getFkTaxGroupId());
                    }
                }

                if (taxGroup != null) {
                    break;
                }
            }
        }

        return taxGroup;
    }

    public ArrayList<SDataDpsEntryTax> getDpsEntryTaxes(int idItem, boolean isPrepayment, int idTaxRegion, int idIdyEmisor, int idIdyReceptor, Date date, double quantity, double amountCy, double exchangeRate) throws SQLException, Exception {
        int order = 0;
        boolean orderFound = false;
        double amountTaxCy = 0;
        double amountNetCy = 0;
        SSessionTax tax = null;
        SSessionTaxGroup taxGroup = getTaxGroup(idItem, idTaxRegion, date);
        ArrayList<SDataDpsEntryTax> dpsEntryTaxes = new ArrayList<SDataDpsEntryTax>();
        ArrayList<SDataDpsEntryTax> dpsEntryTaxesAux = new ArrayList<SDataDpsEntryTax>();

        if (taxGroup != null) {
            orderFound = true;
            amountNetCy = amountCy;

            while (orderFound) {
                order++;
                orderFound = false;
                dpsEntryTaxesAux.clear();

                // Calculate taxes for current order of application:

                for (SSessionTaxGroupEntry tge : taxGroup.getEntries()) {
                    if (order == tge.getApplicationOrder() && idIdyEmisor == tge.getPkTaxIdentityEmisorTypeId() && idIdyReceptor == tge.getPkTaxIdentityReceptorTypeId() &&
                            date.compareTo(tge.getDateStart()) >= 0 && (tge.getDateEnd_n() == null || date.compareTo(tge.getDateEnd_n()) <= 0)) {

                        tax = moTaxesMap.get(tge.getTaxKey());

                        if (tax != null) {
                            SDataDpsEntryTax entryTax = new SDataDpsEntryTax();
                            entryTax.setPkTaxBasicId(tax.getPkTaxBasicId());
                            entryTax.setPkTaxId(tax.getPkTaxId());
                            entryTax.setPercentage(tax.getPercentage());
                            entryTax.setValueUnitary(tax.getValueUnitary());
                            entryTax.setValue(tax.getValue());
                            entryTax.setFkTaxTypeId(tax.getFkTaxTypeId());
                            entryTax.setFkTaxCalculationTypeId(tax.getFkTaxCalculationTypeId());
                            entryTax.setFkTaxApplicationTypeId(isPrepayment && quantity >= 0 ? SModSysConsts.FINS_TP_TAX_APP_ACCR : tax.getFkTaxApplicationTypeId());

                            entryTax.setDbmsTax(tax.getTax());
                            entryTax.setDbmsTaxType(tax.getTaxType());
                            entryTax.setDbmsTaxCalculationType(tax.getTaxCalculationType());
                            entryTax.setDbmsTaxApplicationType(isPrepayment && quantity >= 0 ? SModSysConsts.FINS_TP_TAX_APP_ACCR_NAME : tax.getTaxApplicationType());

                            switch (tax.getFkTaxCalculationTypeId()) {
                                case SModSysConsts.FINS_TP_TAX_CAL_RATE:
                                    amountTaxCy = SLibUtils.round(amountNetCy * tax.getPercentage(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                                    break;
                                case SModSysConsts.FINS_TP_TAX_CAL_AMT_FIX_U:
                                    amountTaxCy = SLibUtils.round(quantity * tax.getValueUnitary(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                                    break;
                                case SModSysConsts.FINS_TP_TAX_CAL_AMT_FIX:
                                    amountTaxCy = SLibUtils.round(tax.getValue(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
                                    break;
                                case SModSysConsts.FINS_TP_TAX_CAL_AMT:
                                    amountTaxCy = 0d;
                                    break;
                                default:
                                    amountTaxCy = 0d;
                            }

                            entryTax.setTaxCy(amountTaxCy);
                            entryTax.setTax(SLibUtils.round(amountTaxCy * exchangeRate, SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));

                            dpsEntryTaxesAux.add(entryTax);
                        }

                        orderFound = true;
                    }
                }

                // Calculate net amount resulting in current order of application:

                if (!dpsEntryTaxesAux.isEmpty()) {
                    for (SDataDpsEntryTax det : dpsEntryTaxesAux) {
                        if (det.getFkTaxTypeId() == SModSysConsts.FINS_TP_TAX_CHARGED) {
                            amountNetCy += det.getTaxCy();
                        }
                        else {
                            amountNetCy -= det.getTaxCy();
                        }
                    }

                    dpsEntryTaxes.addAll(dpsEntryTaxesAux);
                }
            }
        }

        return dpsEntryTaxes;
    }

    /**
     * Gets Automatic Bookkeeping Package (ABP) for provided entity.
     * @param keyEntity Entity ID (index 0: company branch ID; index 1: entity ID).
     * @return Found ABP if any, otherwise <code>null</code>.
     */
    public SDbAbpEntity getAbpEntity(final int[] keyEntity) {
        SDbAbpEntity abp = null;

        // Links for entities are sorted by company branch ID and entity ID.

        for (SDbAbpEntityLink link: maAbpEntitiesLinksArray) {
            if (keyEntity[0] > link.getPkCompanyBranchId()) {
                break;  // link will never be found
            }
            else {
                if (SLibUtils.compareKeys(keyEntity, new int[] { link.getPkCompanyBranchId(), link.getPkEntityId() })) {
                    abp = moAbpEntitiesMap.get(link.getPkAbpEntityId());
                    break;
                }
            }
        }

        return abp;
    }

    /**
     * Gets Automatic Bookkeeping Package (ABP) for provided business partner.
     * @param idBizPartner Business partner ID.
     * @return Found ABP if any, otherwise <code>null</code>.
     */
    public SDbAbpBizPartner getAbpBizPartner(final int idBizPartner, final int idBizPartnerCategory) {
        int index = SLibConsts.UNDEFINED;
        int reference = SLibConsts.UNDEFINED;
        boolean obtain = false;
        boolean resolved = false;
        SDbAbpBizPartner abp = null;
        SDbAbpBizPartner abpAux = null;

        // Links for business partners are sorted by link type ID reference ID.

        for (SDbAbpBizPartnerLink link : maAbpBizPartnersLinksArray) {
            obtain = true;

            switch (link.getPkLinkId()) {
                case SModSysConsts.BPSS_LINK_BP:
                    reference = idBizPartner;
                    break;
                case SModSysConsts.BPSS_LINK_BP_TP:
                    if (!resolved) {
                        index = SBpsUtils.getBizPartnerTypeIndex(moSession, idBizPartner, idBizPartnerCategory);
                        resolved = true;
                    }
                    reference = index;
                    break;
                case SModSysConsts.BPSS_LINK_ALL:
                    reference = SLibConsts.UNDEFINED;
                    break;
                default:
                    obtain = false;
            }

            if (obtain && link.getPkReferenceId() == reference) {
                abpAux = moAbpBizPartnersMap.get(link.getPkAbpBizPartnerId());
                if (abpAux.getFkBizPartnerCategoryId() == idBizPartnerCategory) {
                    abp = abpAux;
                    break;
                }
            }
        }

        return abp;
    }

    /**
     * Gets Automatic Bookkeeping Package (ABP) for provided item.
     * @param idItem Item ID.
     * @return Found ABP if any, otherwise <code>null</code>.
     */
    public SDbAbpItem getAbpItem(final int idItem) throws SQLException, Exception {
        int reference = SLibConsts.UNDEFINED;
        boolean obtain = false;
        SDbAbpItem abp = null;
        SSessionItem item = getSessionItem(idItem);

        // Links for items are sorted by link type ID reference ID.

        if (item != null) {
            for (SDbAbpItemLink link : maAbpItemsLinksArray) {
                obtain = true;

                switch (link.getPkLinkId()) {
                    case SModSysConsts.ITMS_LINK_ITEM:
                        reference = item.getPkItemId();
                        break;
                    case SModSysConsts.ITMS_LINK_MFR:
                        reference = item.getFkPropManufacturerId();
                        break;
                    case SModSysConsts.ITMS_LINK_BRD:
                        reference = item.getFkPropBrandId();
                        break;
                    case SModSysConsts.ITMS_LINK_LINE:
                        reference = item.getFkItemLineId();
                        break;
                    case SModSysConsts.ITMS_LINK_IGEN:
                        reference = item.getFkItemGenericId();
                        break;
                    case SModSysConsts.ITMS_LINK_IGRP:
                        reference = item.getFkItemGroupId();
                        break;
                    case SModSysConsts.ITMS_LINK_IFAM:
                        reference = item.getFkItemFamilyId();
                        break;
                    case SModSysConsts.ITMS_LINK_TP_ITEM:
                        reference = item.getFkTypeTypeId();
                        break;
                    case SModSysConsts.ITMS_LINK_CL_ITEM:
                        reference = item.getFkTypeClassId();
                        break;
                    case SModSysConsts.ITMS_LINK_CT_ITEM:
                        reference = item.getFkTypeCategoryId();
                        break;
                    case SModSysConsts.ITMS_LINK_ALL:
                        reference = SLibConsts.UNDEFINED;
                        break;
                    default:
                        obtain = false;
                }

                if (obtain && link.getPkReferenceId() == reference) {
                    abp = moAbpItemsMap.get(link.getPkAbpItemId());
                    break;
                }
            }
        }

        return abp;
    }

    /**
     * Gets Automatic Bookkeeping Package (ABP) for tax.
     * @param keyTax Tax ID (index 0: basic tax ID; index 1: tax ID).
     * @return Found ABP if any, otherwise <code>null</code>.
     */
    public SDbAbpTax getAbpTax(final int[] keyTax) {
        SDbAbpTax abp = null;

        // Links for entities are sorted by basic tax ID and tax ID.

        for (SDbAbpTax a : maAbpTaxesArray) {
            if (keyTax[0] > a.getFkTaxBasicId()) {
                break;  // link will never be found
            }
            if (SLibUtils.compareKeys(keyTax, new int[] { a.getFkTaxBasicId(), a.getFkTaxId() })) {
                abp = a;
                break;
            }
        }

        return abp;
    }
}
