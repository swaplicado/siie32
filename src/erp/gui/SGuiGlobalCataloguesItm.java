/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.form.SFormOptionPicker;
import erp.form.SFormOptionPickerItems;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.mitm.data.*;
import erp.mitm.form.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author Sergio Flores
 */
public class SGuiGlobalCataloguesItm extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmMenuItem;
    private javax.swing.JMenuItem jmiItem;
    private javax.swing.JMenuItem jmiItemLine;
    private javax.swing.JMenuItem jmiItemGeneric;
    private javax.swing.JMenuItem jmiItemGroup;
    private javax.swing.JMenuItem jmiItemFamily;
    private javax.swing.JMenuItem jmiItemSaleProduct;
    private javax.swing.JMenuItem jmiItemSaleService;
    private javax.swing.JMenuItem jmiItemAssets;
    private javax.swing.JMenuItem jmiItemPurchaseInput;
    private javax.swing.JMenuItem jmiItemPurchaseExpense;
    private javax.swing.JMenuItem jmiItemExpenseMfg;
    private javax.swing.JMenuItem jmiItemExpenseOperation;
    private javax.swing.JMenuItem jmiBrand;
    private javax.swing.JMenuItem jmiManufacturer;
    private javax.swing.JMenuItem jmiElement;
    private javax.swing.JMenuItem jmiVariety;
    private javax.swing.JMenuItem jmiUnit;
    private javax.swing.JMenuItem jmiBrandType;
    private javax.swing.JMenuItem jmiManufacturerType;
    private javax.swing.JMenuItem jmiElementType;
    private javax.swing.JMenuItem jmiVarietyType;
    private javax.swing.JMenuItem jmiUnitType;
    private javax.swing.JMenuItem jmiLevelType;
    private javax.swing.JMenuItem jmiItemPackage;
    private javax.swing.JMenuItem jmiItemBarcode;
    private javax.swing.JMenuItem jmiBizAreaItemGeneric;
    private javax.swing.JMenuItem jmiItemBizPartnerDescription;

    private erp.mitm.form.SFormItem moFormItem;
    private erp.mitm.form.SFormItemSimplified moFormItemSimplified;
    private erp.mitm.form.SFormItemLine moFormItemLine;
    private erp.mitm.form.SFormItemGeneric moFormItemGeneric;
    private erp.mitm.form.SFormItemGroup moFormItemGroup;
    private erp.mitm.form.SFormItemFamily moFormItemFamily;
    private erp.mitm.form.SFormBrand moFormBrand;
    private erp.mitm.form.SFormManufacturer moFormManufacturer;
    private erp.mitm.form.SFormElement moFormElement;
    private erp.mitm.form.SFormVariety moFormVariety;
    private erp.mitm.form.SFormUnit moFormUnit;
    private erp.mitm.form.SFormBrandType moFormBrandType;
    private erp.mitm.form.SFormManufacturerType moFormManufacturerType;
    private erp.mitm.form.SFormElementType moFormElementType;
    private erp.mitm.form.SFormVarietyType moFormVarietyType;
    private erp.mitm.form.SFormUnitType moFormUnitType;
    private erp.mitm.form.SFormItemLevelType moFormItemLevelType;
    private erp.mitm.form.SFormBizPartnerDescription moFormBizPartnerDescription;

    private erp.form.SFormOptionPicker moPickerItemCategory;
    private erp.form.SFormOptionPicker moPickerItemClass;
    private erp.form.SFormOptionPicker moPickerItemType;
    private erp.form.SFormOptionPicker moPickerItem;
    private erp.form.SFormOptionPicker moPickerItemLine;
    private erp.form.SFormOptionPicker moPickerItemGeneric;
    private erp.form.SFormOptionPicker moPickerItemGenericLine;
    private erp.form.SFormOptionPicker moPickerItemGroup;
    private erp.form.SFormOptionPicker moPickerItemFamily;
    private erp.form.SFormOptionPicker moPickerItemBrand;
    private erp.form.SFormOptionPicker moPickerItemManufacturer;
    private erp.form.SFormOptionPicker moPickerItemElement;
    private erp.form.SFormOptionPicker moPickerVariety;
    private erp.form.SFormOptionPicker moPickerUnit;
    private erp.form.SFormOptionPicker moPickerBrandType;
    private erp.form.SFormOptionPicker moPickerManufacturerType;
    private erp.form.SFormOptionPicker moPickerElementType;
    private erp.form.SFormOptionPicker moPickerVarietyType;
    private erp.form.SFormOptionPicker moPickerItemBizPartnerDescription;
    private erp.form.SFormOptionPicker moPickerUnitType;
    private erp.form.SFormOptionPickerItems moPickerItems;

    public SGuiGlobalCataloguesItm(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_CFG);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightUnit;
        boolean hasRightVariety;
        boolean hasRightItemConfiguration;
        boolean hasRightItem;

        jmMenuItem = new JMenu("Ítems");
        jmiItem = new JMenuItem("Ítems");
        jmiItemLine = new JMenuItem("Líneas de ítems");
        jmiItemGeneric = new JMenuItem("Ítems genéricos");
        jmiItemGroup = new JMenuItem("Grupos de ítems");
        jmiItemFamily = new JMenuItem("Familias de ítems");
        jmiItemSaleProduct = new JMenuItem("Ítems productos");
        jmiItemSaleService = new JMenuItem("Ítems sevicios");
        jmiItemAssets = new JMenuItem("Ítems activos fijos");
        jmiItemPurchaseInput = new JMenuItem("Ítems insumos");
        jmiItemPurchaseExpense = new JMenuItem("Ítems gastos de compra");
        jmiItemExpenseMfg = new JMenuItem("Ítems gastos de producción");
        jmiItemExpenseOperation = new JMenuItem("Ítems gastos de operación");
        jmiBrand = new JMenuItem("Marcas");
        jmiManufacturer = new JMenuItem("Fabricantes");
        jmiElement = new JMenuItem("Elementos");
        jmiVariety = new JMenuItem("Variedades");
        jmiUnit = new JMenuItem("Unidades");
        jmiBrandType = new JMenuItem("Tipos de marcas");
        jmiManufacturerType = new JMenuItem("Tipos de fabricantes");
        jmiElementType = new JMenuItem("Tipos de elementos");
        jmiVarietyType = new JMenuItem("Tipos de variedades");
        jmiUnitType = new JMenuItem("Tipos de unidades");
        jmiLevelType = new JMenuItem("Tipos de niveles");
        jmiItemPackage = new JMenuItem("Conversiones de ítems");
        jmiItemBarcode = new JMenuItem("Códigos de barras de ítems");
        jmiBizAreaItemGeneric = new JMenuItem("Áreas de negocios de ítems genéricos");
        jmiItemBizPartnerDescription = new JMenuItem("Descripciones de ítems para asociados de negocios");

        jmMenuItem.add(jmiItem);
        jmMenuItem.add(jmiItemLine);
        jmMenuItem.add(jmiItemGeneric);
        jmMenuItem.add(jmiItemGroup);
        jmMenuItem.add(jmiItemFamily);
        jmMenuItem.addSeparator();
        jmMenuItem.add(jmiItemSaleProduct);
        jmMenuItem.add(jmiItemSaleService);
        jmMenuItem.add(jmiItemAssets);
        jmMenuItem.add(jmiItemPurchaseInput);
        jmMenuItem.add(jmiItemPurchaseExpense);
        jmMenuItem.add(jmiItemExpenseMfg);
        jmMenuItem.add(jmiItemExpenseOperation);
        jmMenuItem.addSeparator();
        jmMenuItem.add(jmiBrand);
        jmMenuItem.add(jmiManufacturer);
        jmMenuItem.add(jmiElement);
        jmMenuItem.add(jmiVariety);
        jmMenuItem.add(jmiUnit);
        jmMenuItem.addSeparator();
        jmMenuItem.add(jmiBrandType);
        jmMenuItem.add(jmiManufacturerType);
        jmMenuItem.add(jmiElementType);
        jmMenuItem.add(jmiVarietyType);
        jmMenuItem.add(jmiUnitType);
        jmMenuItem.add(jmiLevelType);
        jmMenuItem.addSeparator();
        jmMenuItem.add(jmiItemPackage);
        jmMenuItem.add(jmiItemBarcode);
        jmMenuItem.add(jmiBizAreaItemGeneric);
        jmMenuItem.add(jmiItemBizPartnerDescription);

        jmiItem.addActionListener(this);
        jmiItemLine.addActionListener(this);
        jmiItemGeneric.addActionListener(this);
        jmiItemGroup.addActionListener(this);
        jmiItemFamily.addActionListener(this);
        jmiItemSaleProduct.addActionListener(this);
        jmiItemSaleService.addActionListener(this);
        jmiItemAssets.addActionListener(this);
        jmiItemPurchaseInput.addActionListener(this);
        jmiItemPurchaseExpense.addActionListener(this);
        jmiItemExpenseMfg.addActionListener(this);
        jmiItemExpenseOperation.addActionListener(this);
        jmiBrand.addActionListener(this);
        jmiManufacturer.addActionListener(this);
        jmiElement.addActionListener(this);
        jmiVariety.addActionListener(this);
        jmiUnit.addActionListener(this);
        jmiBrandType.addActionListener(this);
        jmiManufacturerType.addActionListener(this);
        jmiElementType.addActionListener(this);
        jmiVarietyType.addActionListener(this);
        jmiUnitType.addActionListener(this);
        jmiLevelType.addActionListener(this);
        jmiItemPackage.addActionListener(this);
        jmiItemBarcode.addActionListener(this);
        jmiBizAreaItemGeneric.addActionListener(this);
        jmiItemBizPartnerDescription.addActionListener(this);

        moFormItem = null;
        moFormItemLine = null;
        moFormItemGeneric = null;
        moFormItemGroup = null;
        moFormItemFamily = null;
        moFormBrand = null;
        moFormManufacturer = null;
        moFormElement = null;
        moFormVariety = null;
        moFormUnit = null;
        moFormBrandType = null;
        moFormManufacturerType = null;
        moFormElementType = null;
        moFormVarietyType = null;
        moFormUnitType = null;
        moFormItemLevelType = null;
        moFormBizPartnerDescription = null;

        moPickerItemCategory = null;
        moPickerItemClass = null;
        moPickerItemType = null;
        moPickerItem = null;
        moPickerItemLine = null;
        moPickerItemGeneric = null;
        moPickerItemGenericLine = null;
        moPickerItemGroup = null;
        moPickerItemFamily = null;
        moPickerItemBrand = null;
        moPickerItemManufacturer = null;
        moPickerItemElement = null;
        moPickerVariety = null;
        moPickerUnit = null;
        moPickerBrandType = null;
        moPickerManufacturerType = null;
        moPickerElementType = null;
        moPickerVarietyType = null;
        moPickerItemBizPartnerDescription = null;
        moPickerUnitType = null;
        moPickerItems = null;

        hasRightItem = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_ITEM).HasRight;
        hasRightUnit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_UNIT).HasRight;
        hasRightVariety = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_ITEM_VAR).HasRight;
        hasRightItemConfiguration = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_ITM_ITEM_CFG).HasRight;

        jmMenuItem.setEnabled(hasRightItem || hasRightUnit || hasRightVariety || hasRightItemConfiguration);
        jmiItem.setEnabled(hasRightItem);
        jmiItemLine.setEnabled(hasRightItem);
        jmiItemGeneric.setEnabled(hasRightItem);
        jmiItemGroup.setEnabled(hasRightItem);
        jmiItemFamily.setEnabled(hasRightItem);
        jmiItemSaleProduct.setEnabled(hasRightItem);
        jmiItemSaleService.setEnabled(hasRightItem);
        jmiItemAssets.setEnabled(hasRightItem);
        jmiItemPurchaseInput.setEnabled(hasRightItem);
        jmiItemPurchaseExpense.setEnabled(hasRightItem);
        jmiItemExpenseMfg.setEnabled(hasRightItem);
        jmiItemExpenseOperation.setEnabled(hasRightItem);
        jmiBrand.setEnabled(hasRightItemConfiguration);
        jmiManufacturer.setEnabled(hasRightItemConfiguration);
        jmiElement.setEnabled(hasRightItemConfiguration);
        jmiVariety.setEnabled(hasRightVariety);
        jmiUnit.setEnabled(hasRightUnit);
        jmiBrandType.setEnabled(hasRightItemConfiguration);
        jmiElementType.setEnabled(hasRightItemConfiguration);
        jmiManufacturerType.setEnabled(hasRightItemConfiguration);
        jmiVarietyType.setEnabled(hasRightVariety);
        jmiUnitType.setEnabled(hasRightUnit);
        jmiLevelType.setEnabled(hasRightItem);
        jmiItemPackage.setEnabled(hasRightItemConfiguration);
        jmiItemBarcode.setEnabled(hasRightItemConfiguration);
        jmiBizAreaItemGeneric.setEnabled(hasRightItemConfiguration);
        jmiItemBizPartnerDescription.setEnabled(hasRightItemConfiguration);
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.ITMU_IFAM:
                    if (moFormItemFamily == null) {
                        moFormItemFamily = new SFormItemFamily(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataItemFamily();
                    }
                    miForm = moFormItemFamily;
                    break;
                case SDataConstants.ITMU_IGRP:
                    if (moFormItemGroup == null) {
                        moFormItemGroup = new SFormItemGroup(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataItemGroup();
                    }
                    miForm = moFormItemGroup;
                    break;
                case SDataConstants.ITMU_IGEN:
                    if (moFormItemGeneric == null) {
                        moFormItemGeneric = new SFormItemGeneric(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataItemGeneric();
                    }
                    miForm = moFormItemGeneric;
                    break;
                case SDataConstants.ITMU_LINE:
                    if (moFormItemLine == null) {
                        moFormItemLine = new SFormItemLine(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataItemLine();
                    }
                    miForm = moFormItemLine;
                    break;
                case SDataConstants.ITMU_ITEM:
                    if (moFormItem == null) {
                        moFormItem = new SFormItem(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataItem();
                    }
                    miForm = moFormItem;
                    break;
                case SDataConstants.ITMU_TP_UNIT:
                    if (moFormUnitType == null) {
                        moFormUnitType = new SFormUnitType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataUnitType();
                    }
                    miForm = moFormUnitType;
                    break;
                case SDataConstants.ITMU_TP_LEV:
                    if (moFormItemLevelType == null) {
                        moFormItemLevelType = new SFormItemLevelType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataItemLevelType();
                    }
                    miForm = moFormItemLevelType;
                    break;
                case SDataConstants.ITMU_UNIT:
                    if (moFormUnit == null) {
                        moFormUnit = new SFormUnit(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataUnit();
                    }
                    miForm = moFormUnit;
                    break;
                case SDataConstants.ITMU_TP_VAR:
                    if (moFormVarietyType == null) {
                        moFormVarietyType = new SFormVarietyType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataVarietyType();
                    }
                    miForm = moFormVarietyType;
                    break;
                case SDataConstants.ITMU_VAR:
                    if (moFormVariety == null) {
                        moFormVariety = new SFormVariety(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataVariety();
                    }
                    miForm = moFormVariety;
                    break;
                case SDataConstants.ITMU_TP_BRD:
                    if (moFormBrandType == null) {
                        moFormBrandType = new SFormBrandType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBrandType();
                    }
                    miForm = moFormBrandType;
                    break;
                case SDataConstants.ITMU_BRD:
                    if (moFormBrand == null) {
                        moFormBrand = new SFormBrand(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBrand();
                    }
                    miForm = moFormBrand;
                    break;
                case SDataConstants.ITMU_TP_MFR:
                    if (moFormManufacturerType == null) {
                        moFormManufacturerType = new SFormManufacturerType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataManufacturerType();
                    }
                    miForm = moFormManufacturerType;
                    break;
                case SDataConstants.ITMU_MFR:
                    if (moFormManufacturer == null) {
                        moFormManufacturer = new SFormManufacturer(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataManufacturer();
                    }
                    miForm = moFormManufacturer;
                    break;
                case SDataConstants.ITMU_TP_EMT:
                    if (moFormElementType == null) {
                        moFormElementType = new SFormElementType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataElementType();
                    }
                    miForm = moFormElementType;
                    break;
                case SDataConstants.ITMU_EMT:
                    if (moFormElement == null) {
                        moFormElement = new SFormElement(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataElement();
                    }
                    miForm = moFormElement;
                    break;
                case SDataConstants.ITMU_CFG_ITEM_BP:
                    if (moFormBizPartnerDescription == null) {
                        moFormBizPartnerDescription = new SFormBizPartnerDescription(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartnerItemDescription();
                    }
                    miForm = moFormBizPartnerDescription;
                    break;
                case SDataConstants.ITMX_ITEM_SIMPLE:
                    if (moFormItemSimplified == null) {
                        moFormItemSimplified = new SFormItemSimplified(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataItem();
                    }
                    moFormItemSimplified.setValue(SDataConstants.ITMX_ITEM_SIMPLE, auxType);
                    miForm = moFormItemSimplified;
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
        int[] auxKey = new int[] { auxType01, auxType02 };
        java.lang.String sViewTitle = "";
        java.lang.Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.ITMU_IFAM:
                    oViewClass = erp.mitm.view.SViewItemFamily.class;
                    sViewTitle = "Familias ítems";
                    break;
                case SDataConstants.ITMU_IGRP:
                    oViewClass = erp.mitm.view.SViewItemGroup.class;
                    sViewTitle = "Grupos ítems";
                    break;
                case SDataConstants.ITMU_IGEN:
                    oViewClass = erp.mitm.view.SViewItemGeneric.class;
                    sViewTitle = "Ítems genéricos";
                    break;
                case SDataConstants.ITMU_LINE:
                    oViewClass = erp.mitm.view.SViewItemLine.class;
                    sViewTitle = "Líneas ítems";
                    break;
                case SDataConstants.ITMU_ITEM:
                    oViewClass = erp.mitm.view.SViewItem.class;
                    sViewTitle = "Ítems";
                    break;
                case SDataConstants.ITMX_ITEM_SIMPLE:
                    oViewClass = erp.mitm.view.SViewItemSimplified.class;
                    switch (auxType01) {
                        case SDataConstants.ITMX_ITEM_IDX_SAL_PRO:
                            sViewTitle = "Ítems productos";
                            break;
                        case SDataConstants.ITMX_ITEM_IDX_SAL_SRV:
                            sViewTitle = "Ítems servicios";
                            break;
                        case SDataConstants.ITMX_ITEM_IDX_ASS_ASS:
                            sViewTitle = "Ítems activos fijos";
                            break;
                        case SDataConstants.ITMX_ITEM_IDX_PUR_CON:
                            sViewTitle = "Ítems insumos";
                            break;
                        case SDataConstants.ITMX_ITEM_IDX_PUR_EXP:
                            sViewTitle = "Ítems gastos compra";
                            break;
                        case SDataConstants.ITMX_ITEM_IDX_EXP_MFG:
                            sViewTitle = "Ítems gastos producción";
                            break;
                        case SDataConstants.ITMX_ITEM_IDX_EXP_OPE:
                            sViewTitle = "Ítems gastos operación";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                case SDataConstants.ITMU_ITEM_BARC:
                    oViewClass = erp.mitm.view.SViewItemBarcode.class;
                    sViewTitle = "Códigos barras ítems";
                    break;
                case SDataConstants.ITMU_CFG_ITEM_BP:
                    oViewClass = erp.mitm.view.SViewItemBizPartnerDescription.class;
                    sViewTitle = "Descrip. ítems asoc. negocios ";
                    break;
                case SDataConstants.ITMU_IGEN_BA:
                    oViewClass = erp.mitm.view.SViewBizAreaItemGeneric.class;
                    sViewTitle = "Áreas negocios ítems genéricos";
                    break;
                case SDataConstants.ITMU_TP_UNIT:
                    oViewClass = erp.mitm.view.SViewUnitType.class;
                    sViewTitle = "Tipos unidades";
                    break;
                case SDataConstants.ITMU_TP_LEV:
                    oViewClass = erp.mitm.view.SViewItemLevelType.class;
                    sViewTitle = "Tipos niveles";
                    break;
                case SDataConstants.ITMU_UNIT:
                    oViewClass = erp.mitm.view.SViewUnit.class;
                    sViewTitle = "Unidades";
                    break;
                case SDataConstants.ITMU_TP_VAR:
                    oViewClass = erp.mitm.view.SViewVarietyType.class;
                    sViewTitle = "Tipos variedades";
                    break;
                case SDataConstants.ITMU_VAR:
                    oViewClass = erp.mitm.view.SViewVariety.class;
                    sViewTitle = "Variedades";
                    break;
                case SDataConstants.ITMU_TP_BRD:
                    oViewClass = erp.mitm.view.SViewBrandType.class;
                    sViewTitle = "Tipos marcas";
                    break;
                case SDataConstants.ITMU_BRD:
                    oViewClass = erp.mitm.view.SViewBrand.class;
                    sViewTitle = "Marcas";
                    break;
                case SDataConstants.ITMU_TP_MFR:
                    oViewClass = erp.mitm.view.SViewManufacturerType.class;
                    sViewTitle = "Tipos fabricantes";
                    break;
                case SDataConstants.ITMU_MFR:
                    oViewClass = erp.mitm.view.SViewManufacturer.class;
                    sViewTitle = "Fabricantes";
                    break;
                case SDataConstants.ITMU_TP_EMT:
                    oViewClass = erp.mitm.view.SViewElementType.class;
                    sViewTitle = "Tipos elementos";
                    break;
                case SDataConstants.ITMU_EMT:
                    oViewClass = erp.mitm.view.SViewElement.class;
                    sViewTitle = "Elementos";
                    break;
                case SDataConstants.ITMX_ITEM_PACK:
                    oViewClass = erp.mitm.view.SViewItemPackage.class;
                    sViewTitle = "Conversiones ítems";
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
    public int showForm(int formType, int auxType, java.lang.Object pk) {
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
                case SDataConstants.ITMS_CT_ITEM:
                    picker = moPickerItemCategory = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemCategory);
                    break;
                case SDataConstants.ITMS_CL_ITEM:
                    picker = moPickerItemClass = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemClass);
                    break;
                case SDataConstants.ITMS_TP_ITEM:
                    picker = moPickerItemType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemType);
                    break;
                case SDataConstants.ITMU_IFAM:
                    picker = moPickerItemFamily = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemFamily);
                    break;
                case SDataConstants.ITMU_IGRP:
                    picker = moPickerItemGroup = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemGroup);
                    break;
                case SDataConstants.ITMU_IGEN:
                    picker = moPickerItemGeneric = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemGeneric);
                    break;
                case SDataConstants.ITMX_IGEN_LINE:
                    picker = moPickerItemGenericLine = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemGenericLine);
                    break;
                case SDataConstants.ITMU_LINE:
                    picker = moPickerItemLine = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemLine);
                    break;
                case SDataConstants.ITMU_ITEM:
                case SDataConstants.ITMX_ITEM_BOM_ITEM:
                case SDataConstants.ITMX_ITEM_BOM_LEVEL:
                    picker = moPickerItem = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItem);
                    break;
                case SDataConstants.ITMU_CFG_ITEM_BP:
                    picker = moPickerItemBizPartnerDescription = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemBizPartnerDescription);
                    break;
                case SDataConstants.ITMU_TP_UNIT:
                    picker = moPickerUnitType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerUnitType);
                    break;
                case SDataConstants.ITMU_UNIT:
                    picker = moPickerUnit = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerUnit);
                    break;
                case SDataConstants.ITMU_TP_VAR:
                    picker = moPickerVarietyType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerVarietyType);
                    break;
                case SDataConstants.ITMU_VAR:
                    picker = moPickerVariety = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerVariety);
                    break;
                case SDataConstants.ITMU_BRD:
                    picker = moPickerItemBrand = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemBrand);
                    break;
                case SDataConstants.ITMU_MFR:
                    picker = moPickerItemManufacturer = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemManufacturer);
                    break;
                case SDataConstants.ITMU_EMT:
                    picker = moPickerItemElement = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerItemElement);
                    break;
                case SDataConstants.ITMU_TP_BRD:
                    picker = moPickerBrandType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBrandType);
                    break;
                case SDataConstants.ITMU_TP_MFR:
                    picker = moPickerManufacturerType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerManufacturerType);
                    break;
                case SDataConstants.ITMU_TP_EMT:
                    picker = moPickerElementType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerElementType);
                    break;
                case SDataConstants.ITMX_ITEM_IOG:
                    picker = moPickerItems = SFormOptionPickerItems.createOptionPicker(miClient, optionType, moPickerItems);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        javax.swing.JMenu[] menues = null;

        switch (moduleType) {
            case SDataConstants.MOD_CFG:
                menues = new JMenu[] { jmMenuItem };
                break;
            default:
                break;
        }

        return menues;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiItem) {
                showView(SDataConstants.ITMU_ITEM);
            }
            else if (item == jmiItemLine) {
                showView(SDataConstants.ITMU_LINE);
            }
            else if (item == jmiItemGeneric) {
                showView(SDataConstants.ITMU_IGEN);
            }
            else if (item == jmiItemGroup) {
                showView(SDataConstants.ITMU_IGRP);
            }
            else if (item == jmiItemFamily) {
                showView(SDataConstants.ITMU_IFAM);
            }
            else if (item == jmiItemSaleProduct) {
                showView(SDataConstants.ITMX_ITEM_SIMPLE, SDataConstants.ITMX_ITEM_IDX_SAL_PRO);
            }
            else if (item == jmiItemSaleService) {
                showView(SDataConstants.ITMX_ITEM_SIMPLE, SDataConstants.ITMX_ITEM_IDX_SAL_SRV);
            }
            else if (item == jmiItemAssets) {
                showView(SDataConstants.ITMX_ITEM_SIMPLE, SDataConstants.ITMX_ITEM_IDX_ASS_ASS);
            }
            else if (item == jmiItemPurchaseInput) {
                showView(SDataConstants.ITMX_ITEM_SIMPLE, SDataConstants.ITMX_ITEM_IDX_PUR_CON);
            }
            else if (item == jmiItemPurchaseExpense) {
                showView(SDataConstants.ITMX_ITEM_SIMPLE, SDataConstants.ITMX_ITEM_IDX_PUR_EXP);
            }
            else if (item == jmiItemExpenseMfg) {
                showView(SDataConstants.ITMX_ITEM_SIMPLE, SDataConstants.ITMX_ITEM_IDX_EXP_MFG);
            }
            else if (item == jmiItemExpenseOperation) {
                showView(SDataConstants.ITMX_ITEM_SIMPLE, SDataConstants.ITMX_ITEM_IDX_EXP_OPE);
            }
            else if (item == jmiBrand) {
                showView(SDataConstants.ITMU_BRD);
            }
            else if (item == jmiManufacturer) {
                showView(SDataConstants.ITMU_MFR);
            }
            else if (item == jmiElement) {
                showView(SDataConstants.ITMU_EMT);
            }
            else if (item == jmiVariety) {
                showView(SDataConstants.ITMU_VAR);
            }
            else if (item == jmiUnit) {
                showView(SDataConstants.ITMU_UNIT);
            }
            else if (item == jmiBrandType) {
                showView(SDataConstants.ITMU_TP_BRD);
            }
            else if (item == jmiManufacturerType) {
                showView(SDataConstants.ITMU_TP_MFR);
            }
            else if (item == jmiElementType) {
                showView(SDataConstants.ITMU_TP_EMT);
            }
            else if (item == jmiVarietyType) {
                showView(SDataConstants.ITMU_TP_VAR);
            }
            else if (item == jmiUnitType) {
                showView(SDataConstants.ITMU_TP_UNIT);
            }
            else if (item == jmiLevelType) {
                showView(SDataConstants.ITMU_TP_LEV);
            }
            else if (item == jmiItemPackage) {
                showView(SDataConstants.ITMX_ITEM_PACK);
            }
            else if (item == jmiItemBarcode) {
                showView(SDataConstants.ITMU_ITEM_BARC);
            }
            else if (item == jmiBizAreaItemGeneric) {
                showView(SDataConstants.ITMU_IGEN_BA);
            }
            else if (item == jmiItemBizPartnerDescription) {
                showView(SDataConstants.ITMU_CFG_ITEM_BP);
            }
        }
    }
}
