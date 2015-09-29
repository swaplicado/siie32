package erp.gui.mod.cfg;

import erp.gui.mod.xml.SXmlConfig;
import erp.gui.mod.xml.SXmlMenu;
import erp.gui.mod.xml.SXmlMenuSection;
import erp.gui.mod.xml.SXmlMenuSectionItem;
import erp.gui.mod.xml.SXmlModConsts;
import erp.gui.mod.xml.SXmlModule;
import erp.gui.mod.xml.SXmlScope;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SCfgProcessor {
    
    private SXmlConfig moConfig;
    private int mnCompanyId;
    private int mnUserId;
    private SXmlScope[] maScopes;
    
    public SCfgProcessor(SXmlConfig config, int companyId, int userId) {
        moConfig = config;
        mnCompanyId = companyId;
        mnUserId = userId;
        maScopes = new SXmlScope[3];
        maScopes[0] = config.getScope(SXmlModConsts.AMBIT_USR_ID, userId);
        maScopes[1] = config.getScope(SXmlModConsts.AMBIT_COM_ID, companyId);
        maScopes[2] = config.getScope(SXmlModConsts.AMBIT_SYS_ID, SLibConsts.UNDEFINED);
    }
    
    /*
     * Public methods:
     */

    /*
        Configuration GUI elements hierarchy:
            scope
            module
            module/menu
            module/menu/section
            module/menu/section/item
     */
    
    /**
     * Gets module by identifier.
     * @param identifier Module identifier (format: moduleId).
     * @return Desired module.
     */
    public SXmlModule getModule(String identifier) {
        SXmlModule xmlModule = null;
        
        for (SXmlScope scope : maScopes) {
            if (scope != null) {
                xmlModule = scope.getChildModule(identifier);
                if (xmlModule != null) {
                    break;
                }
            }
        }
        
        return xmlModule;
    }
    
    /**
     * Gets menu by identifier.
     * @param identifier Menu identifier (format: moduleId/menuId).
     * @return Desired menu.
     */
    public SXmlMenu getMenu(String identifier) {
        String[] ids = SLibUtils.textExplode(identifier, "/");
        SXmlModule xmlModule = null;
        SXmlMenu xmlMenu = null;
        
        for (SXmlScope scope : maScopes) {
            if (scope != null) {
                xmlModule = scope.getChildModule(ids[0]);
                if (xmlModule != null) {
                    xmlMenu = xmlModule.getChildMenu(ids[1]);
                    if (xmlMenu != null) {
                        break;
                    }
                }
            }
        }
        
        return xmlMenu;
    }
    
    /**
     * Gets menu section by identifier.
     * @param identifier Menu section identifier (format: moduleId/menuId/sectionId).
     * @return Desired menu section.
     */
    public SXmlMenuSection getMenuSection(String identifier) {
        String[] ids = SLibUtils.textExplode(identifier, "/");
        SXmlModule xmlModule = null;
        SXmlMenu xmlMenu = null;
        SXmlMenuSection xmlMenuSection = null;
        
        for (SXmlScope scope : maScopes) {
            if (scope != null) {
                xmlModule = scope.getChildModule(ids[0]);
                if (xmlModule != null) {
                    xmlMenu = xmlModule.getChildMenu(ids[1]);
                    if (xmlMenu != null) {
                        xmlMenuSection = xmlMenu.getChildSection(ids[2]);
                        if (xmlMenuSection != null) {
                            break;
                        }
                    }
                }
            }
        }
        
        return xmlMenuSection;
    }
    
    /**
     * Gets menu section item by identifier.
     * @param identifier Menu section identifier (format: moduleId/menuId/sectionId/itemId).
     * @return Desired menu section item.
     */
    public SXmlMenuSectionItem getMenuSectionItem(String identifier) {
        String[] ids = SLibUtils.textExplode(identifier, "/");
        SXmlModule xmlModule = null;
        SXmlMenu xmlMenu = null;
        SXmlMenuSection xmlMenuSection = null;
        SXmlMenuSectionItem xmlMenuSectionItem = null;
        
        for (SXmlScope scope : maScopes) {
            if (scope != null) {
                xmlModule = scope.getChildModule(ids[0]);
                if (xmlModule != null) {
                    xmlMenu = xmlModule.getChildMenu(ids[1]);
                    if (xmlMenu != null) {
                        xmlMenuSection = xmlMenu.getChildSection(ids[2]);
                        if (xmlMenuSection != null) {
                            xmlMenuSectionItem = xmlMenuSection.getChildItem(ids[3]);
                            if (xmlMenuSectionItem != null) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        return xmlMenuSectionItem;
    }
    
    /**
     * Checks if module is visible by identifier.
     * @param identifier Module identifier (format: moduleId).
     * @return Visible status.
     */
    public boolean isModuleVisible(String identifier) {
        boolean visible = true;
        SXmlModule module = getModule(identifier);
        
        if (module != null) {
            visible = !module.getChildMenus().isEmpty();
        }
        
        return visible;
    }
    
    /**
     * Checks if menu is visible by identifier.
     * @param identifier Menu identifier (format: moduleId/menuId).
     * @return Visible status.
     */
    public boolean isMenuVisible(String identifier) {
        boolean visible = true;
        SXmlMenu menu = getMenu(identifier);
        
        if (menu != null) {
            visible = !menu.getChildSections().isEmpty();
        }
        
        return visible;
    }
    
    /**
     * Checks if menu section is visible by identifier.
     * @param identifier Menu section identifier (format: moduleId/menuId/sectionId).
     * @return Visible status.
     */
    public boolean isMenuSectionVisible(String identifier) {
        boolean visible = true;
        SXmlMenuSection section = getMenuSection(identifier);
        
        if (section != null) {
            visible = !section.getChildItems().isEmpty();
        }
        
        return visible;
    }
    
    /**
     * Checks if menu section item is visible by identifier.
     * @param identifier Menu section item identifier (format: moduleId/menuId/sectionId/itemId).
     * @return Visible status.
     */
    public boolean isMenuSectionItemVisible(String identifier) {
        boolean visible = true;
        SXmlMenuSectionItem xml = getMenuSectionItem(identifier);
        
        if (xml != null) {
            visible = false;
        }
        
        return visible;
    }

    /**
     * Processes GUI controls for setting them as visible or hiding them.
     * @param module GUI module.
     */
    public void processModule(final SCfgModule module) {
        boolean visible = false;
        
        visible = isModuleVisible("" + module.getIdentifier());
        module.setVisible(visible);
        
        if (visible) {
            for (SCfgMenu menu : module.getChildMenus()) {
                visible = isMenuVisible("" + module.getIdentifier() + "/" + menu.getIdentifier());
                menu.setVisible(visible);

                if (visible) {
                    for (SCfgMenuSection menuSection : menu.getChildSections()) {
                        visible = isMenuSectionVisible("" + module.getIdentifier() + "/" + menu.getIdentifier() + "/" + menuSection.getIdentifier());
                        menuSection.setVisible(visible);

                        if (visible) {
                            for (SCfgMenuSectionItem menuSectionItem : menuSection.getChildItems()) {
                                visible = isMenuSectionItemVisible("" + module.getIdentifier() + "/" + menu.getIdentifier() + "/" + menuSection.getIdentifier() + "/" + menuSectionItem.getIdentifier());
                                menuSectionItem.setVisible(visible);

                                if (visible) {

                                }
                            }
                        }

                        menuSection.checkSeparatorVisibility();
                    }
                }
            }
        }
    }
}
