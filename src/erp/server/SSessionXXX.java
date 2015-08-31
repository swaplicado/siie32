/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.server;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mcfg.data.SDataCompanyBranchEntity;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public class SSessionXXX implements java.io.Serializable {

    private int mnSessionId;
    private int mnSystemYear;
    private java.util.Date mtSystemDate;
    private int mnWorkingYear;
    private java.util.Date mtWorkingDate;

    private int mnCurrentCompanyBranchId;

    private erp.musr.data.SDataUser moUser;
    private erp.mcfg.data.SDataCompany moCompany;
    private erp.mcfg.data.SDataParamsCompany moParamsCompany;
    private erp.mcfg.data.SDataParamsErp moParamsErp;
    private erp.server.SFormatters moFormatters;
    private erp.server.SSessionServerRemote miSessionServer;

    private java.util.Vector<erp.mcfg.data.SDataSysEntityCategory> mvEntityCategories;
    private java.util.Vector<erp.mcfg.data.SDataCompanyBranchEntity> mvCurrentCompanyBranchEntities;
    private java.util.Vector<erp.mcfg.data.SDataCertificate> mvDbmsCertificates;

    public SSessionXXX() {
        mnSessionId = 0;
        mnSystemYear = 0;
        mtSystemDate = null;
        mnWorkingYear = 0;
        mtWorkingDate = null;

        mnCurrentCompanyBranchId = 0;

        moUser = null;
        moCompany = null;
        moParamsCompany = null;
        moParamsErp = null;
        moFormatters = null;
        miSessionServer = null;

        mvEntityCategories = new Vector<>();
        mvCurrentCompanyBranchEntities = new Vector<>();
        mvDbmsCertificates = new Vector<>();
    }

    public void setSessionId(int n) { mnSessionId = n; }
    public void setSystemDate(java.util.Date t) { mtSystemDate = t; mnSystemYear = SLibTimeUtilities.digestYear(mtSystemDate)[0]; setWorkingDate(mtSystemDate); }
    public void setWorkingDate(java.util.Date t) { mtWorkingDate = t; mnWorkingYear = SLibTimeUtilities.digestYear(mtWorkingDate)[0]; }

    public void setCurrentCompanyBranchId(int n) { mnCurrentCompanyBranchId = n; }

    public void setUser(erp.musr.data.SDataUser o) { moUser = o; }
    public void setCompany(erp.mcfg.data.SDataCompany o) { moCompany = o; }
    public void setParamsCompany(erp.mcfg.data.SDataParamsCompany o) { moParamsCompany = o; }
    public void setParamsErp(erp.mcfg.data.SDataParamsErp o) { moParamsErp = o; }
    public void setFormatters(erp.server.SFormatters o) { moFormatters = o; }
    public void setSessionServer(erp.server.SSessionServerRemote i) { miSessionServer = i; }

    public int getSessionId() { return mnSessionId; }
    public int getSystemYear() { return mnSystemYear; }
    public java.util.Date getSystemDate() { return mtSystemDate; }
    public int getWorkingYear() { return mnWorkingYear; }
    public java.util.Date getWorkingDate() { return mtWorkingDate; }

    public int getCurrentCompanyBranchId() { return mnCurrentCompanyBranchId; }

    public erp.musr.data.SDataUser getUser() { return moUser; }
    public erp.mcfg.data.SDataCompany getCompany() { return moCompany; }
    public erp.mcfg.data.SDataParamsCompany getParamsCompany() { return moParamsCompany; }
    public erp.mcfg.data.SDataParamsErp getParamsErp() { return moParamsErp; }
    public erp.server.SFormatters getFormatters() { return moFormatters; }
    public erp.server.SSessionServerRemote getSessionServer() { return miSessionServer; }

    public java.util.Vector<erp.mcfg.data.SDataSysEntityCategory> getEntityCategories() { return mvEntityCategories; }
    public java.util.Vector<erp.mcfg.data.SDataCompanyBranchEntity> getCurrentCompanyBranchEntities() { return mvCurrentCompanyBranchEntities; }
    public java.util.Vector<erp.mcfg.data.SDataCertificate> getDbmsCertificates() { return mvDbmsCertificates; }

    public int getLanguage() { return moParamsErp == null ? SLibConstants.LAN_SPANISH : moParamsErp.getFkLanguageId(); }

    /**
     * Sets default access to company branch and its default entities for current user session.
     */
    public void prepareAccess() {
        int i = 0;
        int j = 0;

        mnCurrentCompanyBranchId = 0;
        mvCurrentCompanyBranchEntities.clear();

        // Lookup for default company branch:

        for (i = 0; i < moUser.getDbmsAccessCompanyBranches().size(); i++) {
            if (moCompany.getPkCompanyId() == moUser.getDbmsAccessCompanyBranches().get(i).getDbmsFkCompanyId() &&
                    moUser.getDbmsAccessCompanyBranches().get(i).getIsDefault()) {
                mnCurrentCompanyBranchId = moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId();
                break;  // no more default company branches
            }
        }

        if (mnCurrentCompanyBranchId != 0) {
            // Lookup for default company branch entities:

            for (i = 0; i < mvEntityCategories.size(); i++) {
                for (j = 0; j < moUser.getDbmsAccessCompanyBranchEntities().size(); j++) {
                    if (mnCurrentCompanyBranchId == moUser.getDbmsAccessCompanyBranchEntities().get(j).getPkCompanyBranchId() &&
                            mvEntityCategories.get(i).getPkCategoryId() == moUser.getDbmsAccessCompanyBranchEntities().get(j).getDbmsCompanyBranchEntity().getFkEntityCategoryId() &&
                            moUser.getDbmsAccessCompanyBranchEntities().get(j).getIsDefault() &&
                            moUser.getDbmsAccessCompanyBranchEntities().get(j).getDbmsCompanyBranchEntity().getIsActive()) {
                            mvCurrentCompanyBranchEntities.add(moUser.getDbmsAccessCompanyBranchEntities().get(j).getDbmsCompanyBranchEntity());
                            break;  // no more default company branch entities in current category
                    }
                }
            }
        }
    }

    /**
     * Sets an entity for current user session.
     * @param categoryId Category of entity (for crosschecking, at the most one entity can be selected by category).
     * @param companyBranchEntityKey Primary key of entity.
     */
    public void setCurrentCompanyBranchEntityKey(int categoryId, int[] companyBranchEntityKey) {
        int i = 0;
        SDataCompanyBranchEntity entity = null;

        if (getIsUniversal() || getIsUniversalCurrentCompany() || getIsUniversalCurrentCompanyBranch() || getIsUniversalCurrentCompanyBranchEntities(categoryId)) {
            // Search for company branch entity in whole entities set:

            for (i = 0; i < moCompany.getDbmsCompanyBranchEntities().size(); i++) {
                if (SLibUtilities.compareKeys(companyBranchEntityKey, moCompany.getDbmsCompanyBranchEntities().get(i).getPrimaryKey())) {
                    entity = moCompany.getDbmsCompanyBranchEntities().get(i);
                    break;
                }
            }
        }
        else {
            // Search for company branch entity in user entities subset:

            for (i = 0; i < moUser.getDbmsAccessCompanyBranchEntities().size(); i++) {
                if (SLibUtilities.compareKeys(companyBranchEntityKey,
                        new int[] { moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId(), moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkEntityId() })) {
                    entity = moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity();
                    break;
                }
            }
        }

        if (entity != null) {
            if (categoryId == entity.getFkEntityCategoryId()) {
                // Only one company branch entity per category at a time is allowed:

                for (i = 0; i < mvCurrentCompanyBranchEntities.size(); i++) {
                    if (categoryId == mvCurrentCompanyBranchEntities.get(i).getFkEntityCategoryId()) {
                        mvCurrentCompanyBranchEntities.removeElementAt(i);
                    }
                }

                mvCurrentCompanyBranchEntities.add(entity);
            }
        }
    }

    /**
     * Gets entity's ID selected in current user session, by category.
     * @param categoryId Category of entity desired.
     */
    public int[] getCurrentCompanyBranchEntityKey(int categoryId) {
        int[] id = null;

        for (int i = 0; i < mvCurrentCompanyBranchEntities.size(); i++) {
            if (categoryId == mvCurrentCompanyBranchEntities.get(i).getFkEntityCategoryId()) {
                id = new int[] { mvCurrentCompanyBranchEntities.get(i).getPkCompanyBranchId(), mvCurrentCompanyBranchEntities.get(i).getPkEntityId() };
                break;
            }
        }

        return id;
    }

    /**
     * Gets company selected in current user session.
     */
    public erp.mcfg.data.SDataCompany getCurrentCompany() {
        return getCompany();
    }

    /**
     * Gets company branch selected in current user session.
     */
    public erp.mbps.data.SDataBizPartnerBranch getCurrentCompanyBranch() {
        SDataBizPartnerBranch branch = null;

        for (int i = 0; i < moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().size(); i++) {
            if (mnCurrentCompanyBranchId == moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().get(i).getPkBizPartnerBranchId()) {
                branch = moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().get(i);
                break;
            }
        }

        return branch;
    }

    /**
     * Gets company's name selected in current user session.
     */
    public java.lang.String getCurrentCompanyName() {
        return getCompany().getCompany();
    }

    /**
     * Gets company branch's name selected in current user session.
     */
    public java.lang.String getCurrentCompanyBranchName() {
        SDataBizPartnerBranch branch = getCurrentCompanyBranch();
        return branch == null ? "" : branch.getBizPartnerBranch();
    }

    /**
     * Gets company branch's name selected in current user session.
     */
    public java.lang.String getCurrentCompanyBranchCode() {
        SDataBizPartnerBranch branch = getCurrentCompanyBranch();
        return branch == null ? "" : branch.getCode();
    }

    /**
     * Gets entity's name selected in current user session, by category.
     * @param categoryId Category of entity desired.
     */
    public java.lang.String getCurrentCompanyBranchEntityName(int categoryId) {
        String entity = "";

        for (int i = 0; i < mvCurrentCompanyBranchEntities.size(); i++) {
            if (categoryId == mvCurrentCompanyBranchEntities.get(i).getFkEntityCategoryId()) {
                entity = mvCurrentCompanyBranchEntities.get(i).getEntity();
                break;
            }
        }

        return entity;
    }

    /**
     * Gets entity's code selected in current user session, by category.
     * @param categoryId Category of entity desired.
     */
    public java.lang.String getCurrentCompanyBranchEntityCode(int categoryId) {
        return getCompanyBranchEntityCode(getCurrentCompanyBranchEntityKey(categoryId));
    }

    /**
     * Checks if user has universal access.
     */
    public boolean getIsUniversal() {
        return moUser.getIsUniversal();
    };

    /**
     * Checks if user has universal access in a company.
     * @param companyId ID of desired company.
     */
    public boolean getIsUniversalCompany(int companyId) {
        boolean isUniversal = false;

        for (int i = 0; i < moUser.getDbmsAccessCompanies().size(); i++) {
            if (companyId == moUser.getDbmsAccessCompanies().get(i).getPkCompanyId()) {
                isUniversal = moUser.getDbmsAccessCompanies().get(i).getIsUniversal();
                break;
            }
        }

        return isUniversal;
    }

    /**
     * Checks if user has universal access in a company branch.
     * @param companyBranchId ID of desired company branch.
     */
    public boolean getIsUniversalCompanyBranch(int companyBranchId) {
        boolean isUniversal = false;

        for (int i = 0; i < moUser.getDbmsAccessCompanyBranches().size(); i++) {
            if (companyBranchId == moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId()) {
                isUniversal = moUser.getDbmsAccessCompanyBranches().get(i).getIsUniversal();
                break;
            }
        }

        return isUniversal;
    }

    /**
     * Checks if user has universal access on a specific category in a company branch.
     * @param companyBranchId ID of desired company branch.
     */
    public boolean getIsUniversalCompanyBranchEntities(int companyBranchId, int categoryId) {
        boolean isUniversal = false;

        for (int i = 0; i < moUser.getDbmsAccessCompanyBranchEntitiesUniversal().size(); i++) {
            if (companyBranchId == moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(i).getPkCompanyBranchId() &&
                    categoryId == moUser.getDbmsAccessCompanyBranchEntitiesUniversal().get(i).getPkEntityCategoryId()) {
                isUniversal = true;
                break;
            }
        }

        return isUniversal;
    }

    /**
     * Checks if user has universal access in current company.
     */
    public boolean getIsUniversalCurrentCompany() {
        return getIsUniversalCompany(moCompany.getPkCompanyId());
    }

    /**
     * Checks if user has universal access in current company branch.
     */
    public boolean getIsUniversalCurrentCompanyBranch() {
        return getIsUniversalCompanyBranch(mnCurrentCompanyBranchId);
    }

    /**
     * Checks if user has universal access on a specific entity category in current company branch.
     * @param categoryId ID of desired category.
     */
    public boolean getIsUniversalCurrentCompanyBranchEntities(int categoryId) {
        return getIsUniversalCompanyBranchEntities(mnCurrentCompanyBranchId, categoryId);
    }

    public boolean isDefaultCompany(int companyId) {
        boolean isDefault = false;

        for (int i = 0; i < moUser.getDbmsAccessCompanies().size(); i++) {
            if (companyId == moUser.getDbmsAccessCompanies().get(i).getPkCompanyId()) {
                isDefault = moUser.getDbmsAccessCompanies().get(i).getIsDefault();
                break;
            }
        }

        return isDefault;
    }

    public boolean isDefaultCompanyBranch(int companyBranchId) {
        boolean isDefault = false;

        for (int i = 0; i < moUser.getDbmsAccessCompanyBranches().size(); i++) {
            if (companyBranchId == moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId()) {
                isDefault = moUser.getDbmsAccessCompanyBranches().get(i).getIsDefault();
                break;
            }
        }

        return isDefault;
    }

    public boolean isDefaultCompanyBranchEntity(int[] companyBranchEntityKey) {
        boolean isDefault = false;

        for (int i = 0; i < moUser.getDbmsAccessCompanyBranchEntities().size(); i++) {
            if (SLibUtilities.compareKeys(companyBranchEntityKey,
                    new int[] { moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId(), moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkEntityId() })) {
                isDefault = moUser.getDbmsAccessCompanyBranchEntities().get(i).getIsDefault();
                break;
            }
        }

        return isDefault;
    }

    public java.util.Vector<erp.lib.form.SFormComponentItem> getAllCompanyBranchesAsComponentItems(boolean addSelectionItem) {
        Vector<SFormComponentItem> items = new Vector<SFormComponentItem>();

        if (addSelectionItem) {
            items.add(new SFormComponentItem(new int[] { 0 }, "(Seleccionar sucursal de la empresa)"));
        }

        for (int i = 0; i < moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().size(); i++) {
            if (!moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().get(i).getIsDeleted()) {
                items.add(new SFormComponentItem(new int[] { moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().get(i).getPkBizPartnerBranchId() },
                        moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().get(i).getBizPartnerBranch() +
                        " (" + moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().get(i).getCode() + ")" +
                        (!isDefaultCompanyBranch(moCompany.getDbmsDataCompany().getDbmsBizPartnerBranches().get(i).getPkBizPartnerBranchId()) ? "" : " *")));
            }
        }

        return items;
    }

    public java.util.Vector<erp.lib.form.SFormComponentItem> getAllCompanyBranchEntitiesAsComponentItems(int companyBranchId, int categoryId, boolean addSelectionItem) {
        int i = 0;
        int j = 0;
        Vector<SFormComponentItem> items = new Vector<SFormComponentItem>();

        if (addSelectionItem) {
            for (i = 0; i < mvEntityCategories.size(); i++) {
                if (categoryId == mvEntityCategories.get(i).getPkCategoryId()) {
                    items.add(new SFormComponentItem(new int[] { 0 }, "(Seleccionar " + mvEntityCategories.get(i).getCategory().toLowerCase() + ")"));
                    break;
                }
            }

            if (items.size() == 0) {
                items.add(new SFormComponentItem(new int[] { 0 }, "(Seleccionar entidad)"));
            }
        }

        for (i = 0; i < moCompany.getDbmsCompanyBranchEntities().size(); i++) {
            if (companyBranchId == moCompany.getDbmsCompanyBranchEntities().get(i).getPkCompanyBranchId() &&
                categoryId == moCompany.getDbmsCompanyBranchEntities().get(i).getFkEntityCategoryId() &&
                moCompany.getDbmsCompanyBranchEntities().get(i).getIsActive() && !moCompany.getDbmsCompanyBranchEntities().get(i).getIsDeleted()) {

                items.add(new SFormComponentItem(new int[] { companyBranchId, moCompany.getDbmsCompanyBranchEntities().get(i).getPkEntityId() },
                        moCompany.getDbmsCompanyBranchEntities().get(i).getEntity() +
                        " (" + moCompany.getDbmsCompanyBranchEntities().get(i).getCode() + ")" +
                        (moCompany.getDbmsCompanyBranchEntities().get(i).getFkEntityCategoryId() != SDataConstantsSys.CFGS_CT_ENT_WH ? "" : textWarehouseStatus(moCompany.getDbmsCompanyBranchEntities().get(i))) +
                        (!isDefaultCompanyBranchEntity(new int[] { companyBranchId, moCompany.getDbmsCompanyBranchEntities().get(i).getPkEntityId() }) ? "" : " *")));
            }
        }

        return items;
    }

    public java.util.Vector<erp.lib.form.SFormComponentItem> getUserCompanyBranchesAsComponentItems(boolean addSelectionItem) {
        Vector<SFormComponentItem> items = new Vector<SFormComponentItem>();

        if (addSelectionItem) {
            items.add(new SFormComponentItem(new int[] { 0 }, "(Seleccionar sucursal de la empresa)"));
        }

        for (int i = 0; i < moUser.getDbmsAccessCompanyBranches().size(); i++) {
            if (moCompany.getPkCompanyId() == moUser.getDbmsAccessCompanyBranches().get(i).getDbmsFkCompanyId()) {
                items.add(new SFormComponentItem(new int[] { moUser.getDbmsAccessCompanyBranches().get(i).getPkCompanyBranchId() },
                        moUser.getDbmsAccessCompanyBranches().get(i).getDbmsCompanyBranch() +
                        " (" + moUser.getDbmsAccessCompanyBranches().get(i).getDbmsCompanyBranchCode() + ")" +
                        (!moUser.getDbmsAccessCompanyBranches().get(i).getIsDefault() ? "" : " *")));
            }
        }

        return items;
    }

    public java.util.Vector<erp.lib.form.SFormComponentItem> getUserCompanyBranchEntitiesAsComponentItems(int companyBranchId, int categoryId, boolean addSelectionItem) {
        int i = 0;
        Vector<SFormComponentItem> items = new Vector<SFormComponentItem>();

        if (addSelectionItem) {
            for (i = 0; i < mvEntityCategories.size(); i++) {
                if (categoryId == mvEntityCategories.get(i).getPkCategoryId()) {
                    items.add(new SFormComponentItem(new int[] { 0 }, "(Seleccionar " + mvEntityCategories.get(i).getCategory().toLowerCase() + ")"));
                    break;
                }
            }

            if (items.size() == 0) {
                items.add(new SFormComponentItem(new int[] { 0 }, "(Seleccionar entidad)"));
            }
        }

        for (i = 0; i < moUser.getDbmsAccessCompanyBranchEntities().size(); i++) {
            if (companyBranchId == moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkCompanyBranchId() &&
                categoryId == moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getFkEntityCategoryId() &&
                moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getIsActive()) {

                items.add(new SFormComponentItem(new int[] { companyBranchId, moUser.getDbmsAccessCompanyBranchEntities().get(i).getPkEntityId() },
                        moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getEntity() +
                        " (" + moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getCode() + ")" +
                        (moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity().getFkEntityCategoryId() != SDataConstantsSys.CFGS_CT_ENT_WH ? "" : textWarehouseStatus(moUser.getDbmsAccessCompanyBranchEntities().get(i).getDbmsCompanyBranchEntity())) +
                        (!moUser.getDbmsAccessCompanyBranchEntities().get(i).getIsDefault() ? "" : " *")));
            }
        }

        return items;
    }

    public String textWarehouseStatus(SDataCompanyBranchEntity e) {
        String sText = "";

        if (e.getIsActiveIn() && e.getIsActiveOut()) {
            sText = "";
        }
        else {
            sText += " (";
            if (!e.getIsActiveIn()) {
                sText += "Bloq. ent." + (e.getIsActiveOut() ? "" : ", Bloq. sal.");
            }
            else {
                sText += "Bloq. sal.";
            }
            sText += ")";
        }

        return sText;
    }

    public java.lang.String getCompanyBranchEntityName(int[] companyBranchEntityKey) {
        String code = "";

        if (companyBranchEntityKey != null) {
            for (int i = 0; i < moCompany.getDbmsCompanyBranchEntities().size(); i++) {
                if (SLibUtilities.compareKeys(companyBranchEntityKey, moCompany.getDbmsCompanyBranchEntities().get(i).getPrimaryKey())) {
                    code = moCompany.getDbmsCompanyBranchEntities().get(i).getEntity();
                    break;
                }
            }
        }

        return code;
    }

    public java.lang.String getCompanyBranchEntityCode(int[] companyBranchEntityKey) {
        String code = "";

        if (companyBranchEntityKey != null) {
            for (int i = 0; i < moCompany.getDbmsCompanyBranchEntities().size(); i++) {
                if (SLibUtilities.compareKeys(companyBranchEntityKey, moCompany.getDbmsCompanyBranchEntities().get(i).getPrimaryKey())) {
                    code = moCompany.getDbmsCompanyBranchEntities().get(i).getCode();
                    break;
                }
            }
        }

        return code;
    }

    public erp.server.SServerResponse request(erp.server.SServerRequest request) {
        erp.server.SServerResponse response = null;

        try {
            response = miSessionServer.request(request);
        }
        catch (RemoteException e) {
            SLibUtilities.renderException(this, e);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return response;
    }
}
