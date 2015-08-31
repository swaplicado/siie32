/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.form.SFormOptionPicker;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.mcfg.data.*;
import erp.mcfg.form.*;
import erp.mod.SModConsts;
import erp.mtrn.data.*;
import erp.mtrn.form.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.SLibConsts;

/**
 *
 * @author Sergio Flores
 */
public class SGuiModuleCfg extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCat;
    private javax.swing.JMenuItem jmiCatLanguage;
    private javax.swing.JMenuItem jmiCatCurrency;
    private javax.swing.JMenuItem jmiCatCompanyERP;
    private javax.swing.JMenuItem jmiCatCompany;
    private javax.swing.JMenuItem jmiCatCompanyBranchEntity;
    private javax.swing.JMenu jmCfg;
    private javax.swing.JMenuItem jmiCfgParamsCompany;
    private javax.swing.JMenuItem jmiCfgParamsErp;
    private javax.swing.JMenuItem jmiCfgDpsDns;    // Document Number Series
    private javax.swing.JMenuItem jmiCfgDiogDns;   // Document Number Series
    private javax.swing.JMenuItem jmiCfgDpsDnc;    // Document Numbering Center
    private javax.swing.JMenuItem jmiCfgDiogDnc;   // Document Numbering Center
    private javax.swing.JMenuItem jmiCfgDpsDncCompanyBranch;
    private javax.swing.JMenuItem jmiCfgDiogDncCompanyBranch;
    private javax.swing.JMenuItem jmiCfgDpsDncCompanyBranchEntity;
    private javax.swing.JMenuItem jmiCfgDiogDncCompanyBranchEntity;
    private javax.swing.JMenuItem jmiCfgMMSItem;
    private javax.swing.JMenu jmCatCfdi;
    private javax.swing.JMenuItem jmiStampAvailable;
    private javax.swing.JMenuItem jmiStampAcquisition;
    private javax.swing.JMenuItem jmiStampClosing;
    private javax.swing.JMenuItem jmiCfgDpsNature;
    private javax.swing.JMenuItem jmiCfgSystemNotes;

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

        jmCat = new JMenu("Catálogos");
        jmiCatLanguage = new JMenuItem("Idiomas");
        jmiCatCurrency = new JMenuItem("Monedas");
        jmiCatCompanyERP = new JMenuItem("Empresas ERP");
        jmiCatCompany = new JMenuItem("Empresas");
        jmiCatCompanyBranchEntity = new JMenuItem("Entidades de sucursales");

        jmCat.add(jmiCatLanguage);
        jmCat.add(jmiCatCurrency);
        jmCat.addSeparator();
        jmCat.add(jmiCatCompanyERP);
        jmCat.add(jmiCatCompany);
        jmCat.add(jmiCatCompanyBranchEntity);

        jmCfg = new JMenu("Configuración");
        jmiCfgParamsCompany = new JMenuItem("Parámetros de empresas");
        jmiCfgParamsErp = new JMenuItem("Parámetros de sistema");
        jmiCfgDpsDns = new JMenuItem("Series de docs. de C/V");
        jmiCfgDiogDns = new JMenuItem("Series de docs. de inventarios");
        jmiCfgDpsDnc = new JMenuItem("Centros de foliado de docs. de C/V");
        jmiCfgDiogDnc = new JMenuItem("Centros de foliado de docs. de inventarios");
        jmiCfgDpsDncCompanyBranch = new JMenuItem("Centros de foliado de docs. de C/V por sucursal");
        jmiCfgDiogDncCompanyBranch = new JMenuItem("Centros de foliado de docs. de inventarios por sucursal");
        jmiCfgDpsDncCompanyBranchEntity = new JMenuItem("Centros de foliado de docs. de C/V por entidad");
        jmiCfgDiogDncCompanyBranchEntity = new JMenuItem("Centros de foliado de docs. de inventarios por entidad");
        jmiCfgMMSItem = new JMenuItem("Configuración de ítems para envío por email");
        jmCatCfdi = new JMenu("Comprobantes fiscales digitales");
        jmiStampAvailable = new JMenuItem("Timbres disponibles");
        jmiStampAcquisition = new JMenuItem("Adquisición de timbres");
        jmiStampClosing = new JMenuItem("Generación de inventario inicial de timbres...");
        jmiCfgDpsNature = new JMenuItem("Naturaleza de docs. de C/V");
        jmiCfgSystemNotes = new JMenuItem("Notas predefinidas de docs. de C/V");

        jmCfg.add(jmiCfgParamsCompany);
        jmCfg.add(jmiCfgParamsErp);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgDpsDns);
        jmCfg.add(jmiCfgDiogDns);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgDpsDnc);
        jmCfg.add(jmiCfgDiogDnc);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgDpsDncCompanyBranch);
        jmCfg.add(jmiCfgDiogDncCompanyBranch);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgDpsDncCompanyBranchEntity);
        jmCfg.add(jmiCfgDiogDncCompanyBranchEntity);
        jmCatCfdi.add(jmiStampAvailable);
        jmCatCfdi.add(jmiStampAcquisition);
        jmCatCfdi.addSeparator();
        jmCatCfdi.add(jmiStampClosing);
        jmCfg.add(jmCatCfdi);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgDpsNature);
        jmCfg.add(jmiCfgSystemNotes);
        jmCfg.addSeparator();
        jmCfg.add(jmiCfgMMSItem);

        // XXX
        jmiCfgParamsCompany.setEnabled(false);
        jmiCfgParamsErp.setEnabled(false);
        // XXX

        jmiCatLanguage.addActionListener(this);
        jmiCatCurrency.addActionListener(this);
        jmiCatCompanyERP.addActionListener(this);
        jmiCatCompany.addActionListener(this);
        jmiCatCompanyBranchEntity.addActionListener(this);
        jmiCfgParamsCompany.addActionListener(this);
        jmiCfgParamsErp.addActionListener(this);
        jmiCfgDpsDnc.addActionListener(this);
        jmiCfgDiogDnc.addActionListener(this);
        jmiCfgDpsDns.addActionListener(this);
        jmiCfgDiogDns.addActionListener(this);
        jmiCfgDpsDncCompanyBranch.addActionListener(this);
        jmiCfgDiogDncCompanyBranch.addActionListener(this);
        jmiCfgDpsDncCompanyBranchEntity.addActionListener(this);
        jmiCfgDiogDncCompanyBranchEntity.addActionListener(this);
        jmiCfgMMSItem.addActionListener(this);
        jmiStampAvailable.addActionListener(this);
        jmiStampAcquisition.addActionListener(this);
        jmiStampClosing.addActionListener(this);
        jmiCfgDpsNature.addActionListener(this);
        jmiCfgSystemNotes.addActionListener(this);

        moFormLanguage = null;
        moFormCurrency = null;
        moFormDocumentNumberignCenter = null;
        moFormDocumentNumberSerie = null;
        moFormDncCompanyBranchEntity = null;
        moFormDocumentNature = null;
        moFormSystemNotes = null;

        moPickerLanguage = null;
        moPickerCurrency = null;
        moPickerCompany = null;
        moPickerCompanyBranchEntity = null;

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

        jmCat.setEnabled(hasRightLanguage || hasRightCurrency || hasRightCompany);
        jmiCatLanguage.setEnabled(hasRightLanguage);
        jmiCatCurrency.setEnabled(hasRightCurrency);
        jmiCatCompanyERP.setEnabled(hasRightCompany);
        jmiCatCompany.setEnabled(hasRightCompany);
        jmiCatCompanyBranchEntity.setEnabled(hasRightCompany);

        jmCfg.setEnabled(hasRightParamsCompany || hasRightParamsErp || hasRightFinanceDpsDnc || hasRightPurchasesDpsDnc || hasRightSalesDpsDnc ||
                            hasRightDiogDnc || hasRightFinanceDpsDns || hasRightPurchasesDpsDns || hasRightSalesDpsDns || hasRightDiogDns);
        //jmiParamsCompany.setEnabled(hasRightParamsCompany);   XXX implement view!
        //jmiParamsErp.setEnabled(hasRightParamsErp);   XXX implement view!
        jmiCfgDpsDns.setEnabled(hasRightFinanceDpsDns || hasRightPurchasesDpsDns || hasRightSalesDpsDns);
        jmiCfgDiogDns.setEnabled(hasRightDiogDns);
        jmiCfgDpsDnc.setEnabled(hasRightFinanceDpsDnc || hasRightPurchasesDpsDnc || hasRightSalesDpsDnc);
        jmiCfgDiogDnc.setEnabled(hasRightDiogDnc);
        jmiCfgDpsDncCompanyBranch.setEnabled(hasRightFinanceDpsDnc || hasRightPurchasesDpsDnc || hasRightSalesDpsDnc);
        jmiCfgDiogDncCompanyBranch.setEnabled(hasRightDiogDnc);
        jmiCfgDpsDncCompanyBranchEntity.setEnabled(hasRightFinanceDpsDnc || hasRightPurchasesDpsDnc || hasRightSalesDpsDnc);
        jmiCfgDiogDncCompanyBranchEntity.setEnabled(hasRightDiogDnc);
        jmiCfgMMSItem.setEnabled(hasRightParamsCompany || hasRightParamsErp); // XXX pending to define if has right access
        jmiCfgDpsNature.setEnabled(hasRightDiogDnc);
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

        menues = new JMenu[menuesUsr.length + menuesLoc.length + menuesBps.length + menuesItm.length + 2];

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

        menues [i++] = jmCfg;

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

            if (item == jmiCatLanguage){
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
            else if (item == jmiCfgParamsCompany) {
                showView(SDataConstants.CFGU_PARAM_CO);
            }
            else if (item == jmiCfgParamsErp) {
                showView(SDataConstants.CFGU_PARAM_ERP);
            }
            else if (item == jmiCfgDpsDns) {
                showView(SDataConstants.TRN_DNS_DPS);
            }
            else if (item == jmiCfgDiogDns) {
                showView(SDataConstants.TRN_DNS_DIOG);
            }
            else if (item == jmiCfgDpsDnc) {
                showView(SDataConstants.TRN_DNC_DPS);
            }
            else if (item == jmiCfgDiogDnc) {
                showView(SDataConstants.TRN_DNC_DIOG);
            }
            else if (item == jmiCfgDpsDncCompanyBranch) {
                showView(SDataConstants.TRN_DNC_DPS_COB);
            }
            else if (item == jmiCfgDiogDncCompanyBranch) {
                showView(SDataConstants.TRN_DNC_DIOG_COB);
            }
            else if (item == jmiCfgDpsDncCompanyBranchEntity) {
                showView(SDataConstants.TRN_DNC_DPS_COB_ENT);
            }
            else if (item == jmiCfgDiogDncCompanyBranchEntity) {
                showView(SDataConstants.TRN_DNC_DIOG_COB_ENT);
            }
            else if (item == jmiCfgMMSItem) {
                 miClient.getSession().showView(SModConsts.TRN_MMS_CFG, SLibConsts.UNDEFINED, null);
            }
            else if (item == jmiStampAcquisition) {
                showView(SDataConstants.TRN_SIGN);
            }
            else if (item == jmiStampClosing) {
                moDialogUtilStampsClosing.resetForm();
                moDialogUtilStampsClosing.setVisible(true);
            }
            else if (item == jmiStampAvailable) {
                showView(SDataConstants.TRNX_STAMP_AVL);
            }
            else if (item == jmiCfgDpsNature) {
                showView(SDataConstants.TRNU_DPS_NAT);
            }
            else if (item == jmiCfgSystemNotes) {
                showView(SDataConstants.TRN_SYS_NTS);
            }
        }
    }
}
