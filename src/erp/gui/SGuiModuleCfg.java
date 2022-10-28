/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.form.SFormOptionPicker;
import erp.gui.mod.cfg.SCfgMenu;
import erp.gui.mod.cfg.SCfgMenuSection;
import erp.gui.mod.cfg.SCfgMenuSectionItem;
import erp.gui.mod.cfg.SCfgMenuSectionSeparator;
import erp.gui.mod.cfg.SCfgModule;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.mcfg.data.SDataCompanyBranchEntity;
import erp.mcfg.data.SDataCurrency;
import erp.mcfg.data.SDataLanguage;
import erp.mcfg.form.SFormCompanyBranchEntity;
import erp.mcfg.form.SFormCurrency;
import erp.mcfg.form.SFormLanguage;
import erp.mod.SModConsts;
import erp.mod.hrs.form.SDialogCfdiGeneration;
import erp.mtrn.data.SDataDiogDncCompanyBranch;
import erp.mtrn.data.SDataDiogDncCompanyBranchEntity;
import erp.mtrn.data.SDataDiogDocumentNumberSeries;
import erp.mtrn.data.SDataDiogDocumentNumberingCenter;
import erp.mtrn.data.SDataDocumentNatureCatalogue;
import erp.mtrn.data.SDataDpsDncCompanyBranch;
import erp.mtrn.data.SDataDpsDncCompanyBranchEntity;
import erp.mtrn.data.SDataDpsDocumentNumberSeries;
import erp.mtrn.data.SDataDpsDocumentNumberingCenter;
import erp.mtrn.data.SDataSystemNotes;
import erp.mtrn.form.SDialogUtilStampsClosing;
import erp.mtrn.form.SFormDncCompanyBranchEntity;
import erp.mtrn.form.SFormDocumentNature;
import erp.mtrn.form.SFormDocumentNumberSeries;
import erp.mtrn.form.SFormDocumentNumberignCenter;
import erp.mtrn.form.SFormSystemNotes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Sergio Flores
 */
public class SGuiModuleCfg extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCfg;
    private javax.swing.JMenuItem jmiCfgParamsCompany;
    private javax.swing.JMenuItem jmiCfgParamsErp;
    private javax.swing.JSeparator jsCfgParams;
    private javax.swing.JMenuItem jmiCfgDnsDps;    // Document Number Series
    private javax.swing.JMenuItem jmiCfgDnsDiog;   // Document Number Series
    private javax.swing.JSeparator jsCfgDns;
    private javax.swing.JMenuItem jmiCfgDncDps;    // Document Numbering Center
    private javax.swing.JMenuItem jmiCfgDncDiog;   // Document Numbering Center
    private javax.swing.JSeparator jsCfgDnc;
    private javax.swing.JMenuItem jmiCfgDncDpsCompanyBranch;
    private javax.swing.JMenuItem jmiCfgDncDiogCompanyBranch;
    private javax.swing.JMenuItem jmiCfgDncDpsCompanyBranchEntity;
    private javax.swing.JMenuItem jmiCfgDncDiogCompanyBranchEntity;
    private javax.swing.JMenu jmCfgCfdi;
    private javax.swing.JMenuItem jmiCfgCfdiStampAvailable;
    private javax.swing.JMenuItem jmiCfgCfdiStampAcquisition;
    private javax.swing.JMenuItem jmiCfgCfdiStampClosing;
    private javax.swing.JMenuItem jmiCfgDpsNature;
    private javax.swing.JMenuItem jmiCfgFunctionalAreas;
    private javax.swing.JMenuItem jmiCfgSystemNotes;
    private javax.swing.JMenuItem jmiCfgMmsItem;
    private javax.swing.JMenuItem jmiCfgItemRequiredDps;
    private javax.swing.JMenu jmCat;
    private javax.swing.JMenuItem jmiCatLanguage;
    private javax.swing.JMenuItem jmiCatCurrency;
    private javax.swing.JMenuItem jmiCatCompanyERP;
    private javax.swing.JMenuItem jmiCatCompany;
    private javax.swing.JMenuItem jmiCatCompanyBranchEntity;
    private javax.swing.JMenuItem jmiImpCfdiGeneration;
    private javax.swing.JMenuItem jmiNoWorkingDays;

    private erp.mcfg.form.SFormLanguage moFormLanguage;
    private erp.mcfg.form.SFormCurrency moFormCurrency;
    private erp.mcfg.form.SFormCompanyBranchEntity moFormCompanyBranchEntity;
    private erp.mtrn.form.SFormDocumentNumberignCenter moFormDocumentNumberignCenter;
    private erp.mtrn.form.SFormDocumentNumberSeries moFormDocumentNumberSerie;
    private erp.mtrn.form.SFormDncCompanyBranchEntity moFormDncCompanyBranchEntity;
    private erp.mtrn.form.SFormDocumentNature moFormDocumentNature;
    private erp.mtrn.form.SFormSystemNotes moFormSystemNotes;
    private erp.mtrn.form.SDialogUtilStampsClosing moDialogUtilStampsClosing;

    private erp.form.SFormOptionPicker moPickerLanguage;
    private erp.form.SFormOptionPicker moPickerCurrency;
    private erp.form.SFormOptionPicker moPickerCompany;
    private erp.form.SFormOptionPicker moPickerCompanyBranchEntity;
    private erp.form.SFormOptionPicker moPickerFunctionalArea;
    
    public SGuiModuleCfg(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_CFG);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightLanguage = false;
        boolean hasRightCurrency = false;
        boolean hasRightCompany = false;
        boolean hasRightParamsCompany = false;
        boolean hasRightParamsErp = false;
        boolean hasRightFinanceDpsDns = false;
        boolean hasRightFinanceDpsDnc = false;
        boolean hasRightPurchasesDpsDns = false;
        boolean hasRightPurchasesDpsDnc = false;
        boolean hasRightSalesDpsDns = false;
        boolean hasRightSalesDpsDnc = false;
        boolean hasRightDiogDns = false;
        boolean hasRightDiogDnc = false;
        
        moDialogUtilStampsClosing = new SDialogUtilStampsClosing(miClient);

        jmCfg = new JMenu("Configuración");
        
        jmiCfgParamsCompany = new JMenuItem("Parámetros de empresas");
        jmiCfgParamsErp = new JMenuItem("Parámetros de sistema");
        jsCfgParams = new JPopupMenu.Separator();
        jmiCfgDnsDps = new JMenuItem("Series de docs. de C/V");
        jmiCfgDnsDiog = new JMenuItem("Series de docs. de inventarios");
        jsCfgDns = new JPopupMenu.Separator();
        jmiCfgDncDps = new JMenuItem("Centros de foliado de docs. de C/V");
        jmiCfgDncDiog = new JMenuItem("Centros de foliado de docs. de inventarios");
        jsCfgDnc = new JPopupMenu.Separator();
        jmiCfgDncDpsCompanyBranch = new JMenuItem("Centros de foliado de docs. de C/V por sucursal");
        jmiCfgDncDiogCompanyBranch = new JMenuItem("Centros de foliado de docs. de inventarios por sucursal");
        jmiCfgDncDpsCompanyBranchEntity = new JMenuItem("Centros de foliado de docs. de C/V por entidad");
        jmiCfgDncDiogCompanyBranchEntity = new JMenuItem("Centros de foliado de docs. de inventarios por entidad");
        jmCfgCfdi = new JMenu("Comprobantes fiscales digitales");
        jmiCfgCfdiStampAvailable = new JMenuItem("Timbres disponibles");
        jmiCfgCfdiStampAcquisition = new JMenuItem("Adquisición de timbres");
        jmiCfgCfdiStampClosing = new JMenuItem("Generación de inventario inicial de timbres...");
        jmiCfgDpsNature = new JMenuItem("Naturaleza de docs. de C/V");
        jmiCfgFunctionalAreas = new JMenuItem("Áreas funcionales");
        jmiCfgSystemNotes = new JMenuItem("Notas predefinidas de docs. de C/V");
        jmiCfgMmsItem = new JMenuItem("Configuración de ítems para envío por correo-e");
        jmiCfgItemRequiredDps = new JMenuItem("Configuración de ítems obligatorios con documentos origen");
        jmiNoWorkingDays = new JMenuItem("Calendario dias no laborables");

        jmCfg.add(jmiCfgParamsCompany);
        jmCfg.add(jmiCfgParamsErp);
        jmCfg.add(jsCfgParams); // separator
        jmCfg.add(jmiCfgDnsDps);
        jmCfg.add(jmiCfgDnsDiog);
        jmCfg.add(jsCfgDns);    // separator
        jmCfg.add(jmiCfgDncDps);
        jmCfg.add(jmiCfgDncDiog);
        jmCfg.add(jsCfgDnc);    // separator
        jmCfg.add(jmiCfgDncDpsCompanyBranch);
        jmCfg.add(jmiCfgDncDiogCompanyBranch);
        jmCfg.add(jmiCfgDncDpsCompanyBranchEntity);
        jmCfg.add(jmiCfgDncDiogCompanyBranchEntity);
        jmCfg.addSeparator();
        jmCfgCfdi.add(jmiCfgCfdiStampAvailable);
        jmCfgCfdi.add(jmiCfgCfdiStampAcquisition);
        jmCfgCfdi.addSeparator();
        jmCfgCfdi.add(jmiCfgCfdiStampClosing);
        jmCfg.add(jmCfgCfdi);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgDpsNature);
        jmCfg.add(jmiCfgFunctionalAreas);
        jmCfg.add(jmiCfgSystemNotes);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgMmsItem);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgItemRequiredDps);
        jmCfg.addSeparator();
        jmCfg.add(jmiNoWorkingDays);

        jmCat = new JMenu("Catálogos");
        
        jmiCatLanguage = new JMenuItem("Idiomas");
        jmiCatCurrency = new JMenuItem("Monedas");
        jmiCatCompanyERP = new JMenuItem("Empresas ERP");
        jmiCatCompany = new JMenuItem("Empresas");
        jmiCatCompanyBranchEntity = new JMenuItem("Entidades de sucursales");
        jmiImpCfdiGeneration = new JMenuItem("Generación de archivos de CFDIs");

        jmCat.add(jmiCatLanguage);
        jmCat.add(jmiCatCurrency);
        jmCat.addSeparator();
        jmCat.add(jmiCatCompanyERP);
        jmCat.add(jmiCatCompany);
        jmCat.add(jmiCatCompanyBranchEntity);
        jmCat.addSeparator();
        jmCat.add(jmiImpCfdiGeneration);

        jmiCfgParamsCompany.addActionListener(this);
        jmiCfgParamsErp.addActionListener(this);
        jmiCfgDncDps.addActionListener(this);
        jmiCfgDncDiog.addActionListener(this);
        jmiCfgDnsDps.addActionListener(this);
        jmiCfgDnsDiog.addActionListener(this);
        jmiCfgDncDpsCompanyBranch.addActionListener(this);
        jmiCfgDncDiogCompanyBranch.addActionListener(this);
        jmiCfgDncDpsCompanyBranchEntity.addActionListener(this);
        jmiCfgDncDiogCompanyBranchEntity.addActionListener(this);
        jmiCfgCfdiStampAvailable.addActionListener(this);
        jmiCfgCfdiStampAcquisition.addActionListener(this);
        jmiCfgCfdiStampClosing.addActionListener(this);
        jmiCfgDpsNature.addActionListener(this);
        jmiCfgFunctionalAreas.addActionListener(this);
        jmiCfgSystemNotes.addActionListener(this);
        jmiCfgMmsItem.addActionListener(this);
        jmiCfgItemRequiredDps.addActionListener(this);
        jmiNoWorkingDays.addActionListener(this);
        
        jmiCatLanguage.addActionListener(this);
        jmiCatCurrency.addActionListener(this);
        jmiCatCompanyERP.addActionListener(this);
        jmiCatCompany.addActionListener(this);
        jmiCatCompanyBranchEntity.addActionListener(this);
        jmiImpCfdiGeneration.addActionListener(this);

        hasRightLanguage = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_CFG_LAN).HasRight;
        hasRightCurrency = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_CFG_CUR).HasRight;
        hasRightCompany = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_CFG_CO).HasRight;

        hasRightParamsCompany = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CFG_CO).HasRight;
        hasRightParamsErp = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CFG_ERP).HasRight;

        hasRightFinanceDpsDns = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_DPS_DNS).HasRight;
        hasRightFinanceDpsDnc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_DPS_DNC).HasRight;

        hasRightPurchasesDpsDns = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DPS_DNS).HasRight;
        hasRightPurchasesDpsDnc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DPS_DNC).HasRight;

        hasRightSalesDpsDns = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DPS_DNS).HasRight;
        hasRightSalesDpsDnc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DPS_DNC).HasRight;

        hasRightDiogDns = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_DIOG_CFG).HasRight;
        hasRightDiogDnc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_DNC_DIOG).HasRight;

        jmCfg.setEnabled(hasRightParamsCompany || hasRightParamsErp || hasRightFinanceDpsDnc || hasRightPurchasesDpsDnc || hasRightSalesDpsDnc ||
                            hasRightDiogDnc || hasRightFinanceDpsDns || hasRightPurchasesDpsDns || hasRightSalesDpsDns || hasRightDiogDns);
        jmiCfgParamsCompany.setEnabled(false);
        jmiCfgParamsErp.setEnabled(false);
        jmiCfgDnsDps.setEnabled(hasRightFinanceDpsDns || hasRightPurchasesDpsDns || hasRightSalesDpsDns);
        jmiCfgDnsDiog.setEnabled(hasRightDiogDns);
        jmiCfgDncDps.setEnabled(hasRightFinanceDpsDnc || hasRightPurchasesDpsDnc || hasRightSalesDpsDnc);
        jmiCfgDncDiog.setEnabled(hasRightDiogDnc);
        jmiCfgDncDpsCompanyBranch.setEnabled(hasRightFinanceDpsDnc || hasRightPurchasesDpsDnc || hasRightSalesDpsDnc);
        jmiCfgDncDiogCompanyBranch.setEnabled(hasRightDiogDnc);
        jmiCfgDncDpsCompanyBranchEntity.setEnabled(hasRightFinanceDpsDnc || hasRightPurchasesDpsDnc || hasRightSalesDpsDnc);
        jmiCfgDncDiogCompanyBranchEntity.setEnabled(hasRightDiogDnc);
        jmiCfgDpsNature.setEnabled(hasRightParamsCompany || hasRightParamsErp);
        jmiCfgFunctionalAreas.setEnabled((hasRightParamsCompany || hasRightParamsErp) && miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas());
        jmiCfgSystemNotes.setEnabled(hasRightParamsCompany || hasRightParamsErp);
        jmiCfgMmsItem.setEnabled(hasRightParamsCompany || hasRightParamsErp);
        jmiCfgItemRequiredDps.setEnabled(hasRightParamsCompany || hasRightParamsErp);

        jmCat.setEnabled(hasRightLanguage || hasRightCurrency || hasRightCompany);
        jmiCatLanguage.setEnabled(hasRightLanguage);
        jmiCatCurrency.setEnabled(hasRightCurrency);
        jmiCatCompanyERP.setEnabled(hasRightCompany);
        jmiCatCompany.setEnabled(hasRightCompany);
        jmiCatCompanyBranchEntity.setEnabled(hasRightCompany);
        jmiImpCfdiGeneration.setEnabled(miClient.getSessionXXX().getUser().isAdministrator());
        
        // GUI configuration:
        
        if (((erp.SClient) miClient).getCfgProcesor() != null) {
            SCfgModule module = new SCfgModule("" + mnModuleType);
            SCfgMenu menu = null;
            SCfgMenuSection section = null;
            
            menu = new SCfgMenu(jmCfg, "" + SDataConstants.MOD_CFG_CFG);
            section = new SCfgMenuSection("" + SDataConstants.MOD_CFG_CFG_CFG);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiCfgParamsCompany, "" + SDataConstants.CFG_PARAM_CO));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiCfgParamsErp, "" + SDataConstants.CFG_PARAM_ERP));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsCfgParams));
            menu.getChildSections().add(section);
            section = new SCfgMenuSection("" + SDataConstants.MOD_CFG_CFG_DNS);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiCfgDnsDps, "" + SDataConstants.TRN_DNS_DPS));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiCfgDnsDiog, "" + SDataConstants.TRN_DNS_DIOG));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsCfgDns));
            menu.getChildSections().add(section);
            section = new SCfgMenuSection("" + SDataConstants.MOD_CFG_CFG_DNC);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiCfgDncDps, "" + SDataConstants.TRN_DNC_DPS));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiCfgDncDiog, "" + SDataConstants.TRN_DNC_DIOG));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsCfgDnc));
            menu.getChildSections().add(section);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmCat, "" + SDataConstants.MOD_CFG_CAT);
            module.getChildMenus().add(menu);
            
            ((erp.SClient) miClient).getCfgProcesor().processModule(module);
        }
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.CFGU_LAN:
                    if (moFormLanguage == null) {
                        moFormLanguage = new SFormLanguage(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataLanguage();
                    }
                    miForm = moFormLanguage;
                    break;
                case SDataConstants.CFGU_CUR:
                    if (moFormCurrency == null) {
                        moFormCurrency = new SFormCurrency(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCurrency();
                    }
                    miForm = moFormCurrency;
                    break;
                case SDataConstants.CFGU_COB_ENT:
                    if (moFormCompanyBranchEntity == null) {
                        moFormCompanyBranchEntity = new SFormCompanyBranchEntity(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCompanyBranchEntity();
                    }
                    miForm = moFormCompanyBranchEntity;
                    break;
                case SDataConstants.TRN_DNC_DPS:
                case SDataConstants.TRN_DNC_DIOG:
                    if (moFormDocumentNumberignCenter == null) {
                        moFormDocumentNumberignCenter = new SFormDocumentNumberignCenter(miClient);
                    }
                    switch(formType) {
                        case SDataConstants.TRN_DNC_DPS:
                            moRegistry = new SDataDpsDocumentNumberingCenter();
                            break;
                        case SDataConstants.TRN_DNC_DIOG:
                            moRegistry = new SDataDiogDocumentNumberingCenter();
                            break;
                        default:
                    }
                    miForm = moFormDocumentNumberignCenter;
                    miForm.setValue(SDataConstantsSys.VALUE_TYPE_DOC, new int[] { formType });
                    break;
                case SDataConstants.TRN_DNS_DPS:
                case SDataConstants.TRN_DNS_DIOG:
                    if (moFormDocumentNumberSerie == null) {
                        moFormDocumentNumberSerie = new SFormDocumentNumberSeries(miClient);
                    }
                    switch(formType) {
                        case SDataConstants.TRN_DNS_DPS:
                            moRegistry = new SDataDpsDocumentNumberSeries();
                            break;
                        case SDataConstants.TRN_DNS_DIOG:
                            moRegistry = new SDataDiogDocumentNumberSeries();
                            break;
                        default:
                    }
                    miForm = moFormDocumentNumberSerie;
                    miForm.setValue(SDataConstantsSys.VALUE_TYPE_DOC, new int[] { formType });
                    break;
                case SDataConstants.TRN_DNC_DPS_COB:
                case SDataConstants.TRN_DNC_DIOG_COB:
                case SDataConstants.TRN_DNC_DPS_COB_ENT:
                case SDataConstants.TRN_DNC_DIOG_COB_ENT:
                    if (moFormDncCompanyBranchEntity == null) {
                        moFormDncCompanyBranchEntity = new SFormDncCompanyBranchEntity(miClient);
                    }
                    if (pk != null) {
                        switch(formType) {
                            case SDataConstants.TRN_DNC_DPS_COB:
                                moRegistry = new SDataDpsDncCompanyBranch();
                                break;
                            case SDataConstants.TRN_DNC_DIOG_COB:
                                moRegistry = new SDataDiogDncCompanyBranch();
                                break;
                            case SDataConstants.TRN_DNC_DPS_COB_ENT:
                                moRegistry = new SDataDpsDncCompanyBranchEntity();
                                break;
                            case SDataConstants.TRN_DNC_DIOG_COB_ENT:
                                moRegistry = new SDataDiogDncCompanyBranchEntity();
                                break;
                            default:
                        }
                    }
                    miForm = moFormDncCompanyBranchEntity;
                    miForm.setValue(SDataConstantsSys.VALUE_TYPE_DOC, new int[] { formType });
                    break;

                case SDataConstants.TRNU_DPS_NAT:
                    if (moFormDocumentNature == null) {
                        moFormDocumentNature = new SFormDocumentNature(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDocumentNatureCatalogue();
                    }
                    miForm = moFormDocumentNature;
                    break;
                case SDataConstants.TRN_SYS_NTS:
                    if (moFormSystemNotes == null) {
                        moFormSystemNotes = new SFormSystemNotes(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataSystemNotes();
                    }
                    miForm = moFormSystemNotes;
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM);
            }

            result = processForm(pk, isCopy);
            clearFormComplement();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }

        return result;
    }

    @Override
    public void showView(int viewType) {
        showView(viewType, 0, 0);
    }

    @Override
    public void showView(int viewType, int auxType) {
        showView(viewType, auxType, 0);
    }

    @Override
    public void showView(int viewType, int auxType01, int auxType02) {
        java.lang.String sViewTitle = "";
        java.lang.Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.CFGU_LAN:
                    oViewClass = erp.mcfg.view.SViewLanguage.class;
                    sViewTitle = "Idiomas";
                    break;
                case SDataConstants.CFGU_CUR:
                    oViewClass = erp.mcfg.view.SViewCurrency.class;
                    sViewTitle = "Monedas";
                    break;
                case SDataConstants.CFGU_CO:
                    oViewClass = erp.mcfg.view.SViewCompany.class;
                    sViewTitle = "Empresas ERP";
                    break;
                case SDataConstants.BPSX_BP_CO:
                    oViewClass = erp.mbps.view.SViewBizPartner.class;
                    sViewTitle = "Empresas";
                    auxType01 = viewType;
                    break;
                case SDataConstants.CFGU_COB_ENT:
                    oViewClass = erp.mcfg.view.SViewCompanyBranchEntity.class;
                    sViewTitle = "Entidades sucursales";
                    break;
                case SDataConstants.TRN_DNS_DPS:
                case SDataConstants.TRN_DNS_DIOG:
                    oViewClass = erp.mtrn.view.SViewDocumentNumberSeries.class;
                    switch(viewType) {
                        case SDataConstants.TRN_DNS_DPS:
                            sViewTitle = "Series docs. C/V";
                            break;
                        case SDataConstants.TRN_DNS_DIOG:
                            sViewTitle = "Series docs. inventarios";
                            break;
                        default:
                    }
                    auxType01 = viewType;
                    break;
                case SDataConstants.TRN_DNC_DPS:
                case SDataConstants.TRN_DNC_DIOG:
                    oViewClass = erp.mtrn.view.SViewDocumentNumberingCenter.class;
                    switch(viewType) {
                        case SDataConstants.TRN_DNC_DPS:
                            sViewTitle = "Cen. foliado docs. C/V";
                            break;
                        case SDataConstants.TRN_DNC_DIOG:
                            sViewTitle = "Cen. foliado docs. inventarios";
                            break;
                        default:
                    }
                    auxType01 = viewType;
                    break;
                case SDataConstants.TRN_DNC_DPS_COB:
                case SDataConstants.TRN_DNC_DIOG_COB:
                case SDataConstants.TRN_DNC_DPS_COB_ENT:
                case SDataConstants.TRN_DNC_DIOG_COB_ENT:
                    oViewClass = erp.mtrn.view.SViewDncCompanyBranchEntity.class;
                    switch(viewType) {
                        case SDataConstants.TRN_DNC_DPS_COB:
                            sViewTitle = "Cen. foliado docs. C/V x sucursal";
                            break;
                        case SDataConstants.TRN_DNC_DIOG_COB:
                            sViewTitle = "Cen. foliado docs. inventarios x sucursal";
                            break;
                        case SDataConstants.TRN_DNC_DPS_COB_ENT:
                            sViewTitle = "Cen. foliado docs. C/V x entidad";
                            break;
                        case SDataConstants.TRN_DNC_DIOG_COB_ENT:
                            sViewTitle = "Cen. foliado docs. inventarios x entidad";
                            break;
                        default:
                    }
                    auxType01 = viewType;
                    break;
                case SDataConstants.TRNX_STAMP_AVL:
                    oViewClass = erp.mtrn.view.SViewStampAvailable.class;
                    sViewTitle = "Timbres disponibles";
                    break;
                case SDataConstants.TRN_SIGN:
                    oViewClass = erp.mtrn.view.SViewStampAcquisition.class;
                    sViewTitle = "Adquisición timbres";
                    break;
                case SDataConstants.TRNU_DPS_NAT:
                    oViewClass = erp.mtrn.view.SViewDocumentNature.class;
                    sViewTitle = "Naturaleza docs. C/V";
                    break;
                case SDataConstants.TRN_SYS_NTS:
                    oViewClass = erp.mtrn.view.SViewSystemNotes.class;
                    sViewTitle = "Notas predef. docs. C/V";
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
            }

            processView(oViewClass, sViewTitle, viewType, auxType01, auxType02);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }
    }

    @Override
    public int showForm(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, false);
    }

    @Override
    public int showForm(int formType, int auxType, Object pk) {
        return showForm(formType, auxType, pk, false);
    }

    @Override
    public int showFormForCopy(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, true);
    }

    @Override
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType) {
        SFormOptionPickerInterface picker = null;

        try {
            switch (optionType) {
                case SDataConstants.CFGU_LAN:
                    picker = moPickerLanguage = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerLanguage);
                    break;
                case SDataConstants.CFGU_CUR:
                    picker = moPickerCurrency = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCurrency);
                    break;
                case SDataConstants.CFGU_CO:
                    picker = moPickerCompany = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCompany);
                    break;
                case SDataConstants.CFGU_COB_ENT:
                    picker = moPickerCompanyBranchEntity = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCompanyBranchEntity);
                    break;
                case SModConsts.CFGU_FUNC:
                    picker = moPickerFunctionalArea = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerFunctionalArea);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM_PICK);
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return picker;
    }

    @Override
    public int annulRegistry(int registryType, java.lang.Object pk, sa.lib.gui.SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteRegistry(int registryType, java.lang.Object pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JMenu[] getMenues() {
        int i = 0;
        JMenu[] menues = null;
        JMenu[] menuesUsr = miClient.getGuiModule(SDataConstants.GLOBAL_CAT_USR).getMenuesForModule(SDataConstants.MOD_CFG);
        JMenu[] menuesLoc = miClient.getGuiModule(SDataConstants.GLOBAL_CAT_LOC).getMenuesForModule(SDataConstants.MOD_CFG);
        JMenu[] menuesBps = miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).getMenuesForModule(SDataConstants.MOD_CFG);
        JMenu[] menuesItm = miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).getMenuesForModule(SDataConstants.MOD_CFG);

        menues = new JMenu[2 + menuesUsr.length + menuesLoc.length + menuesBps.length + menuesItm.length];

        menues [i++] = jmCfg;
        menues [i++] = jmCat;

        for (JMenu menu : menuesUsr) {
            menues[i++] = menu;
        }

        for (JMenu menu : menuesLoc) {
            menues[i++] = menu;
        }

        for (JMenu menu : menuesBps) {
            menues[i++] = menu;
        }

        for (JMenu menu : menuesItm) {
            menues[i++] = menu;
        }

        /*
        int i = 0;
        int m = 0;
        javax.swing.JMenu jmCatalogues = new JMenu("Catálogos");
        javax.swing.JMenu[] menuesCatUsr = ;
        javax.swing.JMenu[] menuesCatLoc = miClient.getGuiModule(SDataConstants.GLOBAL_CAT_LOC).getMenuesForModule(SDataConstants.GLOBAL_CAT_LOC);
        javax.swing.JMenu[] menuesCatItm = miClient.getGuiModule(SDataConstants.GLOBAL_CAT_ITM).getMenuesForModule(SDataConstants.GLOBAL_CAT_ITM);
        javax.swing.JMenu[] menuesCatBps = miClient.getGuiModule(SDataConstants.GLOBAL_CAT_BPS).getMenuesForModule(SDataConstants.GLOBAL_CAT_BPS);

        for (i = 0; i < menuesCatUsr.length; i++) {
            jmCatalogues.add(menuesCatUsr[i]);
        }

        for (i = 0; i < menuesCatUsr.length; i++) {
            jmCatalogues.add(menuesCatLoc[i]);
        }

        for (i = 0; i < menuesCatItm.length; i++) {
            jmCatalogues.add(menuesCatItm[i]);
        }

        for (i = 0; i < menuesCatBps.length; i++) {
            jmCatalogues.add(menuesCatBps[i]);
        }

        jmCatalogues.add(jmiCurrency);
        jmCatalogues.add(jmiLanguage);
        */

        return menues;
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiCfgParamsCompany) {
                showView(SDataConstants.CFG_PARAM_CO);
            }
            else if (item == jmiCfgParamsErp) {
                showView(SDataConstants.CFG_PARAM_ERP);
            }
            else if (item == jmiCfgDnsDps) {
                showView(SDataConstants.TRN_DNS_DPS);
            }
            else if (item == jmiCfgDnsDiog) {
                showView(SDataConstants.TRN_DNS_DIOG);
            }
            else if (item == jmiCfgDncDps) {
                showView(SDataConstants.TRN_DNC_DPS);
            }
            else if (item == jmiCfgDncDiog) {
                showView(SDataConstants.TRN_DNC_DIOG);
            }
            else if (item == jmiCfgDncDpsCompanyBranch) {
                showView(SDataConstants.TRN_DNC_DPS_COB);
            }
            else if (item == jmiCfgDncDiogCompanyBranch) {
                showView(SDataConstants.TRN_DNC_DIOG_COB);
            }
            else if (item == jmiCfgDncDpsCompanyBranchEntity) {
                showView(SDataConstants.TRN_DNC_DPS_COB_ENT);
            }
            else if (item == jmiCfgDncDiogCompanyBranchEntity) {
                showView(SDataConstants.TRN_DNC_DIOG_COB_ENT);
            }
            else if (item == jmiCfgMmsItem) {
                 miClient.getSession().showView(SModConsts.TRN_MMS_CFG, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgItemRequiredDps) {
                 miClient.getSession().showView(SModConsts.TRNU_TP_DPS_SRC_ITEM, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgCfdiStampAcquisition) {
                showView(SDataConstants.TRN_SIGN);
            }
            else if (item == jmiCfgCfdiStampClosing) {
                moDialogUtilStampsClosing.resetForm();
                moDialogUtilStampsClosing.setVisible(true);
            }
            else if (item == jmiCfgCfdiStampAvailable) {
                showView(SDataConstants.TRNX_STAMP_AVL);
            }
            else if (item == jmiCfgDpsNature) {
                showView(SDataConstants.TRNU_DPS_NAT);
            }
            else if (item == jmiCfgFunctionalAreas) {
                 miClient.getSession().showView(SModConsts.CFGU_FUNC, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiCfgSystemNotes) {
                showView(SDataConstants.TRN_SYS_NTS);
            }
            else if (item == jmiCatLanguage) {
                showView(SDataConstants.CFGU_LAN);
            }
            else if (item == jmiCatCurrency) {
                showView(SDataConstants.CFGU_CUR);
            }
            else if (item == jmiCatCompanyERP) {
                showView(SDataConstants.CFGU_CO);
            }
            else if (item == jmiCatCompany) {
                showView(SDataConstants.BPSX_BP_CO);
            }
            else if (item == jmiCatCompanyBranchEntity) {
                showView(SDataConstants.CFGU_COB_ENT);
            }
            else if (item == jmiImpCfdiGeneration) {
                SDialogCfdiGeneration dialog;
                
                try {
                    dialog = new SDialogCfdiGeneration((SGuiClient) miClient, "Generación de archivos");
                    dialog.resetForm();
                    dialog.setVisible(true);
                }
                catch (Exception ex) {
                    Logger.getLogger(SGuiModuleHrs.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
