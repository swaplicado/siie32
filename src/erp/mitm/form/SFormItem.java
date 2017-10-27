/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormItem.java
 *
 * Created on 2/09/2009, 01:06:13 PM
 */

package erp.mitm.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mitm.data.SDataBrand;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataItemBarcode;
import erp.mitm.data.SDataItemBarcodeRow;
import erp.mitm.data.SDataItemForeignLanguage;
import erp.mitm.data.SDataItemForeignLanguageRow;
import erp.mitm.data.SDataItemGeneric;
import erp.mitm.data.SDataItemLine;
import erp.mitm.data.SDataManufacturer;
import erp.mitm.data.SDataUnit;
import erp.mitm.data.SDataUnitType;
import erp.mitm.data.SDataVariety;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.STrnUtilities;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Alfonso Flores, Sergio Flores, Juan Barajas, Cesar Orozco
 */
public class SFormItem extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private boolean mbWasInventoriable;
    private boolean mbWasLotApplying;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldFkItemGenericId;
    private erp.lib.form.SFormField moFieldFkItemLineId_n;
    private erp.lib.form.SFormField moFieldName;
    private erp.lib.form.SFormField moFieldNameShort;
    private erp.lib.form.SFormField moFieldPresentation;
    private erp.lib.form.SFormField moFieldPresentationShort;
    private erp.lib.form.SFormField moFieldCode;
    private erp.lib.form.SFormField moFieldItemKey;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldIsInventoriable;
    private erp.lib.form.SFormField moFieldIsLotApplying;
    private erp.lib.form.SFormField moFieldIsBulk;
    private erp.lib.form.SFormField moFieldFkBrandId;
    private erp.lib.form.SFormField moFieldFkManufacturerId;
    private erp.lib.form.SFormField moFieldFkElementId;
    private erp.lib.form.SFormField moFieldFkVariety01Id;
    private erp.lib.form.SFormField moFieldFkVariety02Id;
    private erp.lib.form.SFormField moFieldFkVariety03Id;
    private erp.lib.form.SFormField moFieldFkUnitId;
    private erp.lib.form.SFormField moFieldUnitsContained;
    private erp.lib.form.SFormField moFieldFkUnitUnitsContainedId;
    private erp.lib.form.SFormField moFieldUnitsVirtual;
    private erp.lib.form.SFormField moFieldFkUnitUnitsVirtualId;
    private erp.lib.form.SFormField moFieldNetContent;
    private erp.lib.form.SFormField moFieldFkUnitNetContentId;
    private erp.lib.form.SFormField moFieldIsNetContentVariable;
    private erp.lib.form.SFormField moFieldNetContentUnitary;
    private erp.lib.form.SFormField moFieldFkUnitNetContentUnitaryId;
    private erp.lib.form.SFormField moFieldUnitsPackage;
    private erp.lib.form.SFormField moFieldFkItemPackageId_n;
    private erp.lib.form.SFormField moFieldProductionTime;
    private erp.lib.form.SFormField moFieldProductionCost;
    private erp.lib.form.SFormField moFieldWeightGross;
    private erp.lib.form.SFormField moFieldWeightDelivery;
    private erp.lib.form.SFormField moFieldLength;
    private erp.lib.form.SFormField moFieldLengthUnitary;
    private erp.lib.form.SFormField moFieldIsLengthVariable;
    private erp.lib.form.SFormField moFieldSurface;
    private erp.lib.form.SFormField moFieldSurfaceUnitary;
    private erp.lib.form.SFormField moFieldIsSurfaceVariable;
    private erp.lib.form.SFormField moFieldVolume;
    private erp.lib.form.SFormField moFieldVolumeUnitary;
    private erp.lib.form.SFormField moFieldIsVolumeVariable;
    private erp.lib.form.SFormField moFieldMass;
    private erp.lib.form.SFormField moFieldMassUnitary;
    private erp.lib.form.SFormField moFieldIsMassVariable;
    private erp.lib.form.SFormField moFieldFkUnitAlternativeTypeId;
    private erp.lib.form.SFormField moFieldFkLevelTypeId;
    private erp.lib.form.SFormField moFieldUnitAlternativeBaseEquivalence;

    private erp.lib.form.SFormField moFieldIsFreeDiscountUnitary;
    private erp.lib.form.SFormField moFieldIsFreeDiscountEntry;
    private erp.lib.form.SFormField moFieldIsFreeDiscountDoc;
    private erp.lib.form.SFormField moFieldIsFreePrice;
    private erp.lib.form.SFormField moFieldIsFreeDiscount;
    private erp.lib.form.SFormField moFieldIsFreeCommissions;
    private erp.lib.form.SFormField moFieldSurplusPercentage;
    private erp.lib.form.SFormField moFieldIsReference;
    private erp.lib.form.SFormField moFieldIsPrepayment;
    private erp.lib.form.SFormField moFieldFkDefaultItemRefId_n;
    private erp.lib.form.SFormField moFieldFkAdministrativeConceptTypeId;
    private erp.lib.form.SFormField moFieldFkTaxableConceptTypeId;
    private erp.lib.form.SFormField moFieldFkAccountEbitdaTypeId;
    private erp.lib.form.SFormField moFieldFkFiscalAccountIncId;
    private erp.lib.form.SFormField moFieldFkFiscalAccountExpId;
    private erp.lib.form.SFormField moFieldFkCfdProdServId_n;
    private erp.lib.form.SFormField moFieldTariff;

    private erp.lib.table.STablePane moItemForeignLanguagePane;
    private erp.lib.table.STablePane moItemBarcodePane;

    private erp.mitm.form.SFormItemBarcode moFormItemBarcode;
    private erp.mitm.form.SFormItemForeignLanguage moFormItemForeignLanguage;
    private erp.mitm.form.SFormNewItemCode moFormNewItemCode;

    private erp.mitm.data.SDataItemGeneric moItemGeneric;
    private erp.mitm.data.SDataItemLine moItemLine;
    private erp.mitm.data.SDataItem moItem;

    /** Creates new form SFormItem */
    public SFormItem(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;
        mnFormType = SDataConstants.ITMU_ITEM;

        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbgStatus = new javax.swing.ButtonGroup();
        jTabbedPane = new javax.swing.JTabbedPane();
        jpRegistry = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jlFkItemGenericId = new javax.swing.JLabel();
        jcbFkItemGenericId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemGenericId = new javax.swing.JButton();
        jbEditItemGeneric = new javax.swing.JButton();
        jPanel53 = new javax.swing.JPanel();
        jlFkItemLineId_n = new javax.swing.JLabel();
        jcbFkItemLineId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemLineId_n = new javax.swing.JButton();
        jPanel54 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jPanel60 = new javax.swing.JPanel();
        jlNameShort = new javax.swing.JLabel();
        jtfNameShort = new javax.swing.JTextField();
        jbCopyName = new javax.swing.JButton();
        jPanel55 = new javax.swing.JPanel();
        jlPresent = new javax.swing.JLabel();
        jtfPresentation = new javax.swing.JTextField();
        jPanel61 = new javax.swing.JPanel();
        jlPresentShort = new javax.swing.JLabel();
        jtfPresentationShort = new javax.swing.JTextField();
        jbCopyPresentation = new javax.swing.JButton();
        jPanel56 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        jtfCode = new javax.swing.JTextField();
        jbComputeNewCode = new javax.swing.JButton();
        jPanel57 = new javax.swing.JPanel();
        jlItemKey = new javax.swing.JLabel();
        jtfItemKey = new javax.swing.JTextField();
        jPanel58 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        jtfItemNameRo = new javax.swing.JTextField();
        jPanel59 = new javax.swing.JPanel();
        jlItemShort = new javax.swing.JLabel();
        jtfItemNameShortRo = new javax.swing.JTextField();
        jPanel51 = new javax.swing.JPanel();
        jPanel62 = new javax.swing.JPanel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel65 = new javax.swing.JPanel();
        jckIsBulk = new javax.swing.JCheckBox();
        jckIsInventoriable = new javax.swing.JCheckBox();
        jckIsLotApplying = new javax.swing.JCheckBox();
        jPanel66 = new javax.swing.JPanel();
        jlFkBrandId = new javax.swing.JLabel();
        jcbFkBrandId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel67 = new javax.swing.JPanel();
        jlFkManufacturerId = new javax.swing.JLabel();
        jcbFkManufacturerId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel68 = new javax.swing.JPanel();
        jlFkElementId = new javax.swing.JLabel();
        jcbFkElementId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel69 = new javax.swing.JPanel();
        jlFkVariety01Id = new javax.swing.JLabel();
        jcbFkVariety01Id = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel70 = new javax.swing.JPanel();
        jlFkVariety02Id = new javax.swing.JLabel();
        jcbFkVariety02Id = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel71 = new javax.swing.JPanel();
        jlFkVariety03Id = new javax.swing.JLabel();
        jcbFkVariety03Id = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel63 = new javax.swing.JPanel();
        jlFkLevelTypeId = new javax.swing.JLabel();
        jcbFkLevelTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel64 = new javax.swing.JPanel();
        jlItemStatus = new javax.swing.JLabel();
        jrbStatusActive = new javax.swing.JRadioButton();
        jrbStatusRestricted = new javax.swing.JRadioButton();
        jrbStatusInactive = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlFkUnitId = new javax.swing.JLabel();
        jcbFkUnitId = new javax.swing.JComboBox<SFormComponentItem>();
        jbEditUnit = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jlFkUnitUnitsContainedId = new javax.swing.JLabel();
        jtfUnitsContained = new javax.swing.JTextField();
        jcbFkUnitUnitsContainedId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel6 = new javax.swing.JPanel();
        jlFkUnitUnitsVirtualId = new javax.swing.JLabel();
        jtfUnitsVirtual = new javax.swing.JTextField();
        jcbFkUnitUnitsVirtualId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel27 = new javax.swing.JPanel();
        jlFkUnitNetContentId = new javax.swing.JLabel();
        jtfNetContent = new javax.swing.JTextField();
        jcbFkUnitNetContentId = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsNetContentVariable = new javax.swing.JCheckBox();
        jPanel30 = new javax.swing.JPanel();
        jlFkItemPackageId_n = new javax.swing.JLabel();
        jtfUnitsPackage = new javax.swing.JTextField();
        jcbFkItemPackageId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemPackageId_n = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jlFkUnitNetContentUnitaryId = new javax.swing.JLabel();
        jtfNetContentUnitary = new javax.swing.JTextField();
        jcbFkUnitNetContentUnitaryId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel8 = new javax.swing.JPanel();
        jlProductionTime = new javax.swing.JLabel();
        jtfProductionTime = new javax.swing.JTextField();
        jtfProductionTimeUnitRo = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jlProductionCost = new javax.swing.JLabel();
        jtfProductionCost = new javax.swing.JTextField();
        jtfProductionCostUnit = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jlWeightGross = new javax.swing.JLabel();
        jtfWeightGross = new javax.swing.JTextField();
        jlWeightDelivery = new javax.swing.JLabel();
        jtfWeightDelivery = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jlLength = new javax.swing.JLabel();
        jtfLength = new javax.swing.JTextField();
        jlLengthUnitary = new javax.swing.JLabel();
        jtfLengthUnitary = new javax.swing.JTextField();
        jckIsLengthVariable = new javax.swing.JCheckBox();
        jPanel34 = new javax.swing.JPanel();
        jlSurface = new javax.swing.JLabel();
        jtfSurface = new javax.swing.JTextField();
        jlSurfaceUnitary = new javax.swing.JLabel();
        jtfSurfaceUnitary = new javax.swing.JTextField();
        jckIsSurfaceVariable = new javax.swing.JCheckBox();
        jPanel35 = new javax.swing.JPanel();
        jlVolume = new javax.swing.JLabel();
        jtfVolume = new javax.swing.JTextField();
        jlVolumeUnitary = new javax.swing.JLabel();
        jtfVolumeUnitary = new javax.swing.JTextField();
        jckIsVolumeVariable = new javax.swing.JCheckBox();
        jPanel38 = new javax.swing.JPanel();
        jlMass = new javax.swing.JLabel();
        jtfMass = new javax.swing.JTextField();
        jlMassUnitary = new javax.swing.JLabel();
        jtfMassUnitary = new javax.swing.JTextField();
        jckIsMassVariable = new javax.swing.JCheckBox();
        jPanel32 = new javax.swing.JPanel();
        jlFkUnitAlternativeTypeId = new javax.swing.JLabel();
        jcbFkUnitAlternativeTypeId = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jlUnitBaseEquivalence = new javax.swing.JLabel();
        jtfUnitAlternativeBaseEquivalence = new javax.swing.JTextField();
        jtfUnitAlternativeBaseRo = new javax.swing.JTextField();
        jpConfig = new javax.swing.JPanel();
        jpConfig1 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jckIsFreeDiscountUnitary = new javax.swing.JCheckBox();
        jckIsFreePrice = new javax.swing.JCheckBox();
        jckIsFreeDiscountEntry = new javax.swing.JCheckBox();
        jckIsFreeDiscount = new javax.swing.JCheckBox();
        jckIsFreeDiscountDoc = new javax.swing.JCheckBox();
        jckIsFreeCommissions = new javax.swing.JCheckBox();
        jlSurplusPercentage = new javax.swing.JLabel();
        jtfSurplusPercentage = new javax.swing.JTextField();
        jckIsReference = new javax.swing.JCheckBox();
        jckIsPrepayment = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jlFkDefaultItemRefId_n = new javax.swing.JLabel();
        jcbFkDefaultItemRefId_n = new javax.swing.JComboBox();
        jbFkDefaultItemRefId_n = new javax.swing.JButton();
        jPanel44 = new javax.swing.JPanel();
        jlFkAdministrativeConceptTypeId = new javax.swing.JLabel();
        jcbFkAdministrativeConceptTypeId = new javax.swing.JComboBox();
        jbFkAdministrativeConceptTypeId = new javax.swing.JButton();
        jPanel43 = new javax.swing.JPanel();
        jlFkTaxableConceptTypeId = new javax.swing.JLabel();
        jcbFkTaxableConceptTypeId = new javax.swing.JComboBox();
        jbFkTaxableConceptTypeId = new javax.swing.JButton();
        jPanel47 = new javax.swing.JPanel();
        jlFkAccountEbitdaTypeId = new javax.swing.JLabel();
        jcbFkAccountEbitdaTypeId = new javax.swing.JComboBox();
        jbFkAccountEbitdaTypeId = new javax.swing.JButton();
        jPanel45 = new javax.swing.JPanel();
        jlFkFiscalAccountIncId = new javax.swing.JLabel();
        jcbFkFiscalAccountIncId = new javax.swing.JComboBox();
        jbFkFiscalAccountIncId = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jlFkFiscalAccountExpId = new javax.swing.JLabel();
        jcbFkFiscalAccountExpId = new javax.swing.JComboBox();
        jbFkFiscalAccountExpId = new javax.swing.JButton();
        jPanel48 = new javax.swing.JPanel();
        jlFkCfdProdServId_n = new javax.swing.JLabel();
        moKeyCfdProdServId_n = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel7 = new javax.swing.JPanel();
        jlTariff = new javax.swing.JLabel();
        moTextTariff = new sa.lib.gui.bean.SBeanFieldText();
        jpConfig2 = new javax.swing.JPanel();
        jpConfig2Language = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jbAddItemForeignLanguage = new javax.swing.JButton();
        jbModifyItemForeignLanguage = new javax.swing.JButton();
        jpConfig2Barcode = new javax.swing.JPanel();
        jpItemBarcode = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jbAddItemBarcode = new javax.swing.JButton();
        jbModifyItemBarcode = new javax.swing.JButton();
        jpCommand = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jtfPkItemId_Ro = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ítem");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpRegistry.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel50.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemGenericId.setText("Ítem genérico: *");
        jlFkItemGenericId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel52.add(jlFkItemGenericId);

        jcbFkItemGenericId.setMaximumRowCount(12);
        jcbFkItemGenericId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkItemGenericId.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel52.add(jcbFkItemGenericId);

        jbFkItemGenericId.setText("jButton1");
        jbFkItemGenericId.setToolTipText("Seleccionar ítem genérico");
        jbFkItemGenericId.setFocusable(false);
        jbFkItemGenericId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel52.add(jbFkItemGenericId);

        jbEditItemGeneric.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditItemGeneric.setToolTipText("Modificar ítem genérico");
        jbEditItemGeneric.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEditItemGeneric.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel52.add(jbEditItemGeneric);

        jPanel50.add(jPanel52);

        jPanel53.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemLineId_n.setText("Línea de ítems: *");
        jlFkItemLineId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel53.add(jlFkItemLineId_n);

        jcbFkItemLineId_n.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkItemLineId_n.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel53.add(jcbFkItemLineId_n);

        jbFkItemLineId_n.setText("jButton1");
        jbFkItemLineId_n.setToolTipText("Seleccionar línea de ítem");
        jbFkItemLineId_n.setFocusable(false);
        jbFkItemLineId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel53.add(jbFkItemLineId_n);

        jPanel50.add(jPanel53);

        jPanel54.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlName.setText("Nombre: *");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel54.add(jlName);

        jtfName.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel54.add(jtfName);

        jPanel50.add(jPanel54);

        jPanel60.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNameShort.setText("Nombre corto: *");
        jlNameShort.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel60.add(jlNameShort);

        jtfNameShort.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel60.add(jtfNameShort);

        jbCopyName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbCopyName.setToolTipText("Copiar nombre");
        jbCopyName.setFocusable(false);
        jbCopyName.setPreferredSize(new java.awt.Dimension(23, 22));
        jPanel60.add(jbCopyName);

        jPanel50.add(jPanel60);

        jPanel55.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPresent.setText("Presentación:");
        jlPresent.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel55.add(jlPresent);

        jtfPresentation.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel55.add(jtfPresentation);

        jPanel50.add(jPanel55);

        jPanel61.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPresentShort.setText("Presentación corta:");
        jlPresentShort.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel61.add(jlPresentShort);

        jtfPresentationShort.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel61.add(jtfPresentationShort);

        jbCopyPresentation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbCopyPresentation.setToolTipText("Copiar presentación");
        jbCopyPresentation.setFocusable(false);
        jbCopyPresentation.setPreferredSize(new java.awt.Dimension(23, 22));
        jPanel61.add(jbCopyPresentation);

        jPanel50.add(jPanel61);

        jPanel56.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código del ítem: *");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel56.add(jlCode);

        jtfCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel56.add(jtfCode);

        jbComputeNewCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_wizard.gif"))); // NOI18N
        jbComputeNewCode.setToolTipText("Generar nuevo código");
        jbComputeNewCode.setFocusable(false);
        jbComputeNewCode.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel56.add(jbComputeNewCode);

        jPanel50.add(jPanel56);

        jPanel57.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemKey.setText("Clave del ítem: *");
        jlItemKey.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel57.add(jlItemKey);

        jtfItemKey.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel57.add(jtfItemKey);

        jPanel50.add(jPanel57);

        jPanel58.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel58.add(jlItem);

        jtfItemNameRo.setEditable(false);
        jtfItemNameRo.setText("ITEM");
        jtfItemNameRo.setFocusable(false);
        jtfItemNameRo.setMinimumSize(new java.awt.Dimension(6, 23));
        jtfItemNameRo.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel58.add(jtfItemNameRo);

        jPanel50.add(jPanel58);

        jPanel59.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemShort.setText("Ítem corto:");
        jlItemShort.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel59.add(jlItemShort);

        jtfItemNameShortRo.setEditable(false);
        jtfItemNameShortRo.setText("ITEM CORTO");
        jtfItemNameShortRo.setFocusable(false);
        jtfItemNameShortRo.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel59.add(jtfItemNameShortRo);

        jPanel50.add(jPanel59);

        jPanel1.add(jPanel50, java.awt.BorderLayout.CENTER);

        jPanel51.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

        jPanel62.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jckIsDeleted.setForeground(java.awt.Color.red);
        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckIsDeleted.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel62.add(jckIsDeleted);

        jPanel51.add(jPanel62);

        jPanel65.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsBulk.setText("Es a granel");
        jckIsBulk.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel65.add(jckIsBulk);

        jckIsInventoriable.setText("Es inventariable");
        jckIsInventoriable.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel65.add(jckIsInventoriable);

        jckIsLotApplying.setText("Aplica lote");
        jckIsLotApplying.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel65.add(jckIsLotApplying);

        jPanel51.add(jPanel65);

        jPanel66.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkBrandId.setText("Marca: *");
        jlFkBrandId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel66.add(jlFkBrandId);

        jcbFkBrandId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkBrandId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel66.add(jcbFkBrandId);

        jPanel51.add(jPanel66);

        jPanel67.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkManufacturerId.setText("Fabricante: *");
        jlFkManufacturerId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel67.add(jlFkManufacturerId);

        jcbFkManufacturerId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkManufacturerId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel67.add(jcbFkManufacturerId);

        jPanel51.add(jPanel67);

        jPanel68.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkElementId.setText("Elemento: *");
        jlFkElementId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel68.add(jlFkElementId);

        jcbFkElementId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkElementId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel68.add(jcbFkElementId);

        jPanel51.add(jPanel68);

        jPanel69.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkVariety01Id.setText("Variedad 1: *");
        jlFkVariety01Id.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel69.add(jlFkVariety01Id);

        jcbFkVariety01Id.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkVariety01Id.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel69.add(jcbFkVariety01Id);

        jPanel51.add(jPanel69);

        jPanel70.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkVariety02Id.setText("Variedad 2: *");
        jlFkVariety02Id.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel70.add(jlFkVariety02Id);

        jcbFkVariety02Id.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkVariety02Id.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel70.add(jcbFkVariety02Id);

        jPanel51.add(jPanel70);

        jPanel71.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkVariety03Id.setText("Variedad 3: *");
        jlFkVariety03Id.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel71.add(jlFkVariety03Id);

        jcbFkVariety03Id.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkVariety03Id.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel71.add(jcbFkVariety03Id);

        jPanel51.add(jPanel71);

        jPanel63.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkLevelTypeId.setText("Tipo de nivel: *");
        jlFkLevelTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel63.add(jlFkLevelTypeId);

        jcbFkLevelTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkLevelTypeId.setToolTipText("Tipo de nivel");
        jcbFkLevelTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel63.add(jcbFkLevelTypeId);

        jPanel51.add(jPanel63);

        jPanel64.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemStatus.setText("Estatus: *");
        jlItemStatus.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel64.add(jlItemStatus);

        jbgStatus.add(jrbStatusActive);
        jrbStatusActive.setText("Activo");
        jrbStatusActive.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel64.add(jrbStatusActive);

        jbgStatus.add(jrbStatusRestricted);
        jrbStatusRestricted.setText("Restringido");
        jrbStatusRestricted.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel64.add(jrbStatusRestricted);

        jbgStatus.add(jrbStatusInactive);
        jrbStatusInactive.setText("Inactivo");
        jrbStatusInactive.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel64.add(jrbStatusInactive);

        jPanel51.add(jPanel64);

        jPanel1.add(jPanel51, java.awt.BorderLayout.EAST);

        jpRegistry.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Unidades de medida del ítem:"));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new java.awt.GridLayout(1, 2));

        jPanel10.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkUnitId.setText("Unidad física: *");
        jlFkUnitId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlFkUnitId);

        jcbFkUnitId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkUnitId.setPreferredSize(new java.awt.Dimension(270, 23));
        jPanel4.add(jcbFkUnitId);

        jbEditUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEditUnit.setToolTipText("Modificar unidad física");
        jbEditUnit.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEditUnit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbEditUnit);

        jPanel10.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkUnitUnitsContainedId.setText("Unids. contenidas: *");
        jlFkUnitUnitsContainedId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlFkUnitUnitsContainedId);

        jtfUnitsContained.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfUnitsContained.setText("0.0000");
        jtfUnitsContained.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel5.add(jtfUnitsContained);

        jcbFkUnitUnitsContainedId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jcbFkUnitUnitsContainedId);

        jPanel10.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkUnitUnitsVirtualId.setText("Unids. virtuales: *");
        jlFkUnitUnitsVirtualId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlFkUnitUnitsVirtualId);

        jtfUnitsVirtual.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfUnitsVirtual.setText("0.0000");
        jtfUnitsVirtual.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel6.add(jtfUnitsVirtual);

        jcbFkUnitUnitsVirtualId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jcbFkUnitUnitsVirtualId);

        jPanel10.add(jPanel6);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkUnitNetContentId.setText("Cont. neto: *");
        jlFkUnitNetContentId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jlFkUnitNetContentId);

        jtfNetContent.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNetContent.setText("0.0000");
        jtfNetContent.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel27.add(jtfNetContent);

        jcbFkUnitNetContentId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel27.add(jcbFkUnitNetContentId);

        jckIsNetContentVariable.setText("Variable");
        jckIsNetContentVariable.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel27.add(jckIsNetContentVariable);

        jPanel10.add(jPanel27);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemPackageId_n.setText("Unids. conversión:");
        jlFkItemPackageId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jlFkItemPackageId_n);

        jtfUnitsPackage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfUnitsPackage.setText("0.0000");
        jtfUnitsPackage.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel30.add(jtfUnitsPackage);

        jcbFkItemPackageId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel30.add(jcbFkItemPackageId_n);

        jbFkItemPackageId_n.setText("jButton1");
        jbFkItemPackageId_n.setToolTipText("Seleccionar ítem para conversión");
        jbFkItemPackageId_n.setFocusable(false);
        jbFkItemPackageId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel30.add(jbFkItemPackageId_n);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkUnitNetContentUnitaryId.setText("Cont. neto unit.: *");
        jlFkUnitNetContentUnitaryId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel28.add(jlFkUnitNetContentUnitaryId);

        jtfNetContentUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNetContentUnitary.setText("0.0000");
        jtfNetContentUnitary.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel28.add(jtfNetContentUnitary);

        jcbFkUnitNetContentUnitaryId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel28.add(jcbFkUnitNetContentUnitaryId);

        jPanel30.add(jPanel28);

        jPanel10.add(jPanel30);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProductionTime.setText("Tiempo producción:");
        jlProductionTime.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlProductionTime);

        jtfProductionTime.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfProductionTime.setText("0.0000");
        jtfProductionTime.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel8.add(jtfProductionTime);

        jtfProductionTimeUnitRo.setEditable(false);
        jtfProductionTimeUnitRo.setText("hr");
        jtfProductionTimeUnitRo.setFocusable(false);
        jtfProductionTimeUnitRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jtfProductionTimeUnitRo);

        jPanel10.add(jPanel8);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProductionCost.setText("Costo producción:");
        jlProductionCost.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlProductionCost);

        jtfProductionCost.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfProductionCost.setText("0.0000");
        jtfProductionCost.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel15.add(jtfProductionCost);

        jtfProductionCostUnit.setEditable(false);
        jtfProductionCostUnit.setFocusable(false);
        jtfProductionCostUnit.setPreferredSize(new java.awt.Dimension(75, 23));
        jtfProductionCostUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfProductionCostUnitActionPerformed(evt);
            }
        });
        jPanel15.add(jtfProductionCostUnit);

        jPanel10.add(jPanel15);

        jPanel14.add(jPanel10);

        jPanel11.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWeightGross.setText("Peso bruto: *");
        jlWeightGross.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel31.add(jlWeightGross);

        jtfWeightGross.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfWeightGross.setText("0.0000");
        jtfWeightGross.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel31.add(jtfWeightGross);

        jlWeightDelivery.setText("Peso flete: *");
        jlWeightDelivery.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel31.add(jlWeightDelivery);

        jtfWeightDelivery.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfWeightDelivery.setText("0.0000");
        jtfWeightDelivery.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel31.add(jtfWeightDelivery);

        jPanel11.add(jPanel31);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLength.setText("Longitud: *");
        jlLength.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel33.add(jlLength);

        jtfLength.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfLength.setText("0.0000");
        jtfLength.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel33.add(jtfLength);

        jlLengthUnitary.setText("Longitud unit.:");
        jlLengthUnitary.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel33.add(jlLengthUnitary);

        jtfLengthUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfLengthUnitary.setText("0.0000");
        jtfLengthUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jtfLengthUnitary);

        jckIsLengthVariable.setText("Variable");
        jckIsLengthVariable.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel33.add(jckIsLengthVariable);

        jPanel11.add(jPanel33);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSurface.setText("Superficie: *");
        jlSurface.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel34.add(jlSurface);

        jtfSurface.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSurface.setText("0.0000");
        jtfSurface.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel34.add(jtfSurface);

        jlSurfaceUnitary.setText("Superficie unit.:");
        jlSurfaceUnitary.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel34.add(jlSurfaceUnitary);

        jtfSurfaceUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSurfaceUnitary.setText("0.0000");
        jtfSurfaceUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel34.add(jtfSurfaceUnitary);

        jckIsSurfaceVariable.setText("Variable");
        jckIsSurfaceVariable.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel34.add(jckIsSurfaceVariable);

        jPanel11.add(jPanel34);

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlVolume.setText("Volumen: *");
        jlVolume.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel35.add(jlVolume);

        jtfVolume.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfVolume.setText("0.0000");
        jtfVolume.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel35.add(jtfVolume);

        jlVolumeUnitary.setText("Volumen unit.:");
        jlVolumeUnitary.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel35.add(jlVolumeUnitary);

        jtfVolumeUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfVolumeUnitary.setText("0.0000");
        jtfVolumeUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel35.add(jtfVolumeUnitary);

        jckIsVolumeVariable.setText("Variable");
        jckIsVolumeVariable.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel35.add(jckIsVolumeVariable);

        jPanel11.add(jPanel35);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMass.setText("Masa: *");
        jlMass.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel38.add(jlMass);

        jtfMass.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfMass.setText("0.0000");
        jtfMass.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel38.add(jtfMass);

        jlMassUnitary.setText("Masa unit.:");
        jlMassUnitary.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel38.add(jlMassUnitary);

        jtfMassUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfMassUnitary.setText("0.0000");
        jtfMassUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jtfMassUnitary);

        jckIsMassVariable.setText("Variable");
        jckIsMassVariable.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel38.add(jckIsMassVariable);

        jPanel11.add(jPanel38);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkUnitAlternativeTypeId.setText("Tipo de unidad alternativa:*");
        jlFkUnitAlternativeTypeId.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel32.add(jlFkUnitAlternativeTypeId);

        jcbFkUnitAlternativeTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkUnitAlternativeTypeId.setToolTipText("Tipo unidad alterna");
        jcbFkUnitAlternativeTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel32.add(jcbFkUnitAlternativeTypeId);

        jPanel11.add(jPanel32);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUnitBaseEquivalence.setText("Equivalencia unidad alternativa:");
        jlUnitBaseEquivalence.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel3.add(jlUnitBaseEquivalence);

        jtfUnitAlternativeBaseEquivalence.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfUnitAlternativeBaseEquivalence.setText("0.0000");
        jtfUnitAlternativeBaseEquivalence.setToolTipText("Equivalencia unidad base");
        jtfUnitAlternativeBaseEquivalence.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel3.add(jtfUnitAlternativeBaseEquivalence);

        jtfUnitAlternativeBaseRo.setEditable(false);
        jtfUnitAlternativeBaseRo.setText("UNIT");
        jtfUnitAlternativeBaseRo.setFocusable(false);
        jtfUnitAlternativeBaseRo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jtfUnitAlternativeBaseRo);

        jPanel11.add(jPanel3);

        jPanel14.add(jPanel11);

        jPanel9.add(jPanel14, java.awt.BorderLayout.NORTH);

        jpRegistry.add(jPanel9, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Clasificación", jpRegistry);

        jpConfig.setLayout(new java.awt.BorderLayout());

        jpConfig1.setLayout(new java.awt.BorderLayout());

        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración para comercialización:"));
        jPanel39.setLayout(new java.awt.GridLayout(7, 2, 5, 5));

        jckIsFreeDiscountUnitary.setText("Sin descuento unitario");
        jPanel39.add(jckIsFreeDiscountUnitary);

        jckIsFreePrice.setText("Sin precio");
        jPanel39.add(jckIsFreePrice);

        jckIsFreeDiscountEntry.setText("Sin descuento en partida");
        jPanel39.add(jckIsFreeDiscountEntry);

        jckIsFreeDiscount.setText("Sin descuento en listas de precios");
        jPanel39.add(jckIsFreeDiscount);

        jckIsFreeDiscountDoc.setText("Sin descuento en documento");
        jPanel39.add(jckIsFreeDiscountDoc);

        jckIsFreeCommissions.setText("Sin comisiones de venta");
        jPanel39.add(jckIsFreeCommissions);

        jlSurplusPercentage.setText("Excedente predeterminado:");
        jlSurplusPercentage.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel39.add(jlSurplusPercentage);

        jtfSurplusPercentage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSurplusPercentage.setText("0.00%");
        jtfSurplusPercentage.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel39.add(jtfSurplusPercentage);

        jckIsReference.setText("Referencia obligatoria");
        jPanel39.add(jckIsReference);

        jckIsPrepayment.setText("Es anticipo");
        jPanel39.add(jckIsPrepayment);

        jpConfig1.add(jPanel39, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración contable:"));
        jPanel2.setPreferredSize(new java.awt.Dimension(550, 242));
        jPanel2.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jPanel49.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDefaultItemRefId_n.setText("Ítem de referencia predeterminado:");
        jlFkDefaultItemRefId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel49.add(jlFkDefaultItemRefId_n);

        jcbFkDefaultItemRefId_n.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkDefaultItemRefId_n.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel49.add(jcbFkDefaultItemRefId_n);

        jbFkDefaultItemRefId_n.setText("...");
        jbFkDefaultItemRefId_n.setToolTipText("Seleccionar ítem ref. predeterminado");
        jbFkDefaultItemRefId_n.setFocusable(false);
        jbFkDefaultItemRefId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel49.add(jbFkDefaultItemRefId_n);

        jPanel2.add(jPanel49);

        jPanel44.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkAdministrativeConceptTypeId.setText("Tipo de concepto administrativo: *");
        jlFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel44.add(jlFkAdministrativeConceptTypeId);

        jcbFkAdministrativeConceptTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel44.add(jcbFkAdministrativeConceptTypeId);

        jbFkAdministrativeConceptTypeId.setText("...");
        jbFkAdministrativeConceptTypeId.setToolTipText("Seleccionar tipo concepto administrativo");
        jbFkAdministrativeConceptTypeId.setFocusable(false);
        jbFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel44.add(jbFkAdministrativeConceptTypeId);

        jPanel2.add(jPanel44);

        jPanel43.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel43.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTaxableConceptTypeId.setText("Tipo de concepto impuestos: *");
        jlFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel43.add(jlFkTaxableConceptTypeId);

        jcbFkTaxableConceptTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel43.add(jcbFkTaxableConceptTypeId);

        jbFkTaxableConceptTypeId.setText("...");
        jbFkTaxableConceptTypeId.setToolTipText("Seleccionar tipo concepto impuestos");
        jbFkTaxableConceptTypeId.setFocusable(false);
        jbFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel43.add(jbFkTaxableConceptTypeId);

        jPanel2.add(jPanel43);

        jPanel47.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkAccountEbitdaTypeId.setText("Tipo cuenta EBITDA: *");
        jlFkAccountEbitdaTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel47.add(jlFkAccountEbitdaTypeId);

        jcbFkAccountEbitdaTypeId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkAccountEbitdaTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel47.add(jcbFkAccountEbitdaTypeId);

        jbFkAccountEbitdaTypeId.setText("...");
        jbFkAccountEbitdaTypeId.setToolTipText("Seleccionar tipo de cuenta EBITDA");
        jbFkAccountEbitdaTypeId.setFocusable(false);
        jbFkAccountEbitdaTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel47.add(jbFkAccountEbitdaTypeId);

        jPanel2.add(jPanel47);

        jPanel45.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkFiscalAccountIncId.setText("Código agrupador SAT (ingresos): *");
        jlFkFiscalAccountIncId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel45.add(jlFkFiscalAccountIncId);

        jcbFkFiscalAccountIncId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkFiscalAccountIncId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel45.add(jcbFkFiscalAccountIncId);

        jbFkFiscalAccountIncId.setText("...");
        jbFkFiscalAccountIncId.setToolTipText("Seleccionar código agrupador SAT (ingresos)");
        jbFkFiscalAccountIncId.setFocusable(false);
        jbFkFiscalAccountIncId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel45.add(jbFkFiscalAccountIncId);

        jPanel2.add(jPanel45);

        jPanel46.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkFiscalAccountExpId.setText("Código agrupador SAT (egresos): *");
        jlFkFiscalAccountExpId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel46.add(jlFkFiscalAccountExpId);

        jcbFkFiscalAccountExpId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbFkFiscalAccountExpId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel46.add(jcbFkFiscalAccountExpId);

        jbFkFiscalAccountExpId.setText("...");
        jbFkFiscalAccountExpId.setToolTipText("Seleccionar código agrupador SAT (egresos)");
        jbFkFiscalAccountExpId.setFocusable(false);
        jbFkFiscalAccountExpId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel46.add(jbFkFiscalAccountExpId);

        jPanel2.add(jPanel46);

        jPanel48.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel48.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCfdProdServId_n.setText("ProdServ SAT:");
        jlFkCfdProdServId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel48.add(jlFkCfdProdServId_n);

        moKeyCfdProdServId_n.setMaximumRowCount(20);
        moKeyCfdProdServId_n.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel48.add(moKeyCfdProdServId_n);

        jPanel2.add(jPanel48);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTariff.setText("Fracc. arancelaria:");
        jlTariff.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlTariff);
        jPanel7.add(moTextTariff);

        jPanel2.add(jPanel7);

        jpConfig1.add(jPanel2, java.awt.BorderLayout.EAST);

        jpConfig.add(jpConfig1, java.awt.BorderLayout.NORTH);

        jpConfig2.setLayout(new java.awt.BorderLayout(5, 5));

        jpConfig2Language.setBorder(javax.swing.BorderFactory.createTitledBorder("Descripciones en lengua extranjera:"));
        jpConfig2Language.setPreferredSize(new java.awt.Dimension(100, 200));
        jpConfig2Language.setLayout(new java.awt.BorderLayout());

        jPanel41.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jbAddItemForeignLanguage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbAddItemForeignLanguage.setToolTipText("Crear [Alt+N]");
        jbAddItemForeignLanguage.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel41.add(jbAddItemForeignLanguage);

        jbModifyItemForeignLanguage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbModifyItemForeignLanguage.setToolTipText("Modificar [Alt+M]");
        jbModifyItemForeignLanguage.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel41.add(jbModifyItemForeignLanguage);

        jpConfig2Language.add(jPanel41, java.awt.BorderLayout.NORTH);

        jpConfig2.add(jpConfig2Language, java.awt.BorderLayout.CENTER);

        jpConfig2Barcode.setBorder(javax.swing.BorderFactory.createTitledBorder("Códigos de barras:"));
        jpConfig2Barcode.setPreferredSize(new java.awt.Dimension(300, 100));
        jpConfig2Barcode.setLayout(new java.awt.BorderLayout());

        jpItemBarcode.setLayout(new java.awt.BorderLayout());

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jbAddItemBarcode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbAddItemBarcode.setToolTipText("Crear [Ctrl+N]");
        jbAddItemBarcode.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel29.add(jbAddItemBarcode);

        jbModifyItemBarcode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbModifyItemBarcode.setToolTipText("Modificar [Ctrl+M]");
        jbModifyItemBarcode.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel29.add(jbModifyItemBarcode);

        jpItemBarcode.add(jPanel29, java.awt.BorderLayout.NORTH);

        jpConfig2Barcode.add(jpItemBarcode, java.awt.BorderLayout.CENTER);

        jpConfig2.add(jpConfig2Barcode, java.awt.BorderLayout.EAST);

        jpConfig.add(jpConfig2, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Configuración", jpConfig);

        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jpCommand.setPreferredSize(new java.awt.Dimension(792, 33));
        jpCommand.setLayout(new java.awt.GridLayout(1, 0));

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

        jtfPkItemId_Ro.setEditable(false);
        jtfPkItemId_Ro.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPkItemId_Ro.setToolTipText("ID del registro");
        jtfPkItemId_Ro.setFocusable(false);
        jtfPkItemId_Ro.setPreferredSize(new java.awt.Dimension(65, 23));
        jPanel13.add(jtfPkItemId_Ro);

        jpCommand.add(jPanel13);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel12.add(jbCancel);

        jpCommand.add(jPanel12);

        getContentPane().add(jpCommand, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(976, 647));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jtfProductionCostUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfProductionCostUnitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfProductionCostUnitActionPerformed

    /*
     * Private methods:
     */

    private void initComponentsExtra() {
        int i = 0;
        erp.lib.table.STableColumnForm[] tableColumnsItemForeignLanguage;
        erp.lib.table.STableColumnForm[] tableColumnsItemBarcode;

        mvFields = new Vector<SFormField>();

        moItemBarcodePane = new STablePane(miClient);
        moItemBarcodePane.setDoubleClickAction(this, "publicActionModifyItemBarcode");
        jpItemBarcode.add(moItemBarcodePane, BorderLayout.CENTER);

        moItemForeignLanguagePane = new STablePane(miClient);
        moItemForeignLanguagePane.setDoubleClickAction(this, "publicActionModifyItemForeignLanguage");
        jpConfig2Language.add(moItemForeignLanguagePane, BorderLayout.CENTER);

        moFieldFkItemGenericId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemGenericId, jlFkItemGenericId);
        moFieldFkItemGenericId.setPickerButton(jbFkItemGenericId);
        moFieldFkItemGenericId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkItemLineId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemLineId_n, jlFkItemLineId_n);
        moFieldFkItemLineId_n.setPickerButton(jbFkItemLineId_n);
        moFieldFkItemLineId_n.setTabbedPaneIndex(0, jTabbedPane);
        moFieldName = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfName, jlName);
        moFieldName.setLengthMax(50);
        moFieldName.setTabbedPaneIndex(0, jTabbedPane);
        moFieldNameShort = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfNameShort, jlNameShort);
        moFieldNameShort.setLengthMax(25);
        moFieldNameShort.setTabbedPaneIndex(0, jTabbedPane);
        moFieldPresentation = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPresentation, jlPresent);
        moFieldPresentation.setLengthMax(50);
        moFieldPresentation.setTabbedPaneIndex(0, jTabbedPane);
        moFieldPresentationShort = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfPresentationShort, jlPresentShort);
        moFieldPresentationShort.setLengthMax(25);
        moFieldPresentationShort.setTabbedPaneIndex(0, jTabbedPane);
        moFieldCode = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfCode, jlCode);
        moFieldCode.setLengthMax(10);
        moFieldCode.setTabbedPaneIndex(0, jTabbedPane);
        moFieldItemKey = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfItemKey, jlItemKey);
        moFieldItemKey.setLengthMax(35);
        moFieldItemKey.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);
        moFieldIsDeleted.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsBulk = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsBulk);
        moFieldIsBulk.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsInventoriable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsInventoriable);
        moFieldIsInventoriable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsLotApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsLotApplying);
        moFieldIsLotApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkBrandId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBrandId, jlFkBrandId);
        moFieldFkBrandId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkManufacturerId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkManufacturerId, jlFkManufacturerId);
        moFieldFkManufacturerId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkElementId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkElementId, jlFkElementId);
        moFieldFkElementId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkVariety01Id = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkVariety01Id, jlFkVariety01Id);
        moFieldFkVariety01Id.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkVariety02Id = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkVariety02Id, jlFkVariety02Id);
        moFieldFkVariety02Id.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkVariety03Id = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkVariety03Id, jlFkVariety03Id);
        moFieldFkVariety03Id.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitId, jlFkUnitId);
        moFieldFkUnitId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldUnitsContained = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfUnitsContained, jlFkUnitUnitsContainedId);
        moFieldUnitsContained.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitUnitsContainedId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitUnitsContainedId, jlFkUnitUnitsContainedId);
        moFieldFkUnitUnitsContainedId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldUnitsVirtual = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfUnitsVirtual, jlFkUnitUnitsVirtualId);
        moFieldUnitsVirtual.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitUnitsVirtualId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitUnitsVirtualId, jlFkUnitUnitsVirtualId);
        moFieldFkUnitUnitsVirtualId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldNetContent = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfNetContent, jlFkUnitNetContentId);
        moFieldNetContent.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsNetContentFormat());
        moFieldNetContent.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitNetContentId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitNetContentId, jlFkUnitNetContentId);
        moFieldFkUnitNetContentId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsNetContentVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsNetContentVariable);
        moFieldIsNetContentVariable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldNetContentUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfNetContentUnitary, jlFkUnitNetContentUnitaryId);
        moFieldNetContentUnitary.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsNetContentFormat());
        moFieldNetContentUnitary.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitNetContentUnitaryId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitNetContentUnitaryId, jlFkUnitNetContentUnitaryId);
        moFieldFkUnitNetContentUnitaryId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldUnitsPackage = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfUnitsPackage, jlFkItemPackageId_n);
        moFieldUnitsPackage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsNetContentFormat());
        moFieldUnitsPackage.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkItemPackageId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkItemPackageId_n, jlFkItemPackageId_n);
        moFieldFkItemPackageId_n.setTabbedPaneIndex(0, jTabbedPane);
        moFieldProductionTime = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfProductionTime, jlProductionTime);
        moFieldProductionTime.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldProductionTime.setTabbedPaneIndex(0, jTabbedPane);
        moFieldProductionCost = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfProductionCost, jlProductionCost);
        moFieldProductionTime.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldProductionCost.setTabbedPaneIndex(0, jTabbedPane);
        moFieldWeightGross = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfWeightGross, jlWeightGross);
        moFieldWeightGross.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsWeigthGrossFormat());
        moFieldWeightGross.setTabbedPaneIndex(0, jTabbedPane);
        moFieldWeightDelivery = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfWeightDelivery, jlWeightDelivery);
        moFieldWeightDelivery.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsWeightDeliveryFormat());
        moFieldWeightDelivery.setTabbedPaneIndex(0, jTabbedPane);
        moFieldLength = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfLength, jlLength);
        moFieldLength.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsLengthFormat());
        moFieldLength.setTabbedPaneIndex(0, jTabbedPane);
        moFieldLengthUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfLengthUnitary, jlLengthUnitary);
        moFieldLengthUnitary.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsLengthFormat());
        moFieldLengthUnitary.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsLengthVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsLengthVariable);
        moFieldIsLengthVariable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldSurface = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfSurface, jlSurface);
        moFieldSurface.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsSurfaceFormat());
        moFieldSurface.setTabbedPaneIndex(0, jTabbedPane);
        moFieldSurfaceUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfSurfaceUnitary, jlSurfaceUnitary);
        moFieldSurfaceUnitary.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsSurfaceFormat());
        moFieldSurfaceUnitary.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsSurfaceVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsSurfaceVariable);
        moFieldIsSurfaceVariable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldVolume = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfVolume, jlVolume);
        moFieldVolume.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsVolumeFormat());
        moFieldVolume.setTabbedPaneIndex(0, jTabbedPane);
        moFieldVolumeUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfVolumeUnitary, jlVolumeUnitary);
        moFieldVolumeUnitary.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsVolumeFormat());
        moFieldVolumeUnitary.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsVolumeVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsVolumeVariable);
        moFieldIsVolumeVariable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldMass = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfMass, jlMass);
        moFieldMass.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsMassFormat());
        moFieldMass.setTabbedPaneIndex(0, jTabbedPane);
        moFieldMassUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfMassUnitary, jlMassUnitary);
        moFieldMassUnitary.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsMassFormat());
        moFieldMassUnitary.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsMassVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsMassVariable);
        moFieldIsMassVariable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitAlternativeTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitAlternativeTypeId, jlFkUnitAlternativeTypeId);
        moFieldFkUnitAlternativeTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkLevelTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkLevelTypeId, jlFkLevelTypeId);
        moFieldFkLevelTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldUnitAlternativeBaseEquivalence = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfUnitAlternativeBaseEquivalence, jlUnitBaseEquivalence);
        moFieldUnitAlternativeBaseEquivalence.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsBaseEquivalenceFormat());
        moFieldUnitAlternativeBaseEquivalence.setTabbedPaneIndex(0, jTabbedPane);

        moFieldIsFreeDiscountUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsFreeDiscountUnitary);
        moFieldIsFreeDiscountUnitary.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsFreeDiscountEntry = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsFreeDiscountEntry);
        moFieldIsFreeDiscountEntry.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsFreeDiscountDoc = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsFreeDiscountDoc);
        moFieldIsFreeDiscountDoc.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsFreePrice = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsFreePrice);
        moFieldIsFreePrice.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsFreeDiscount = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsFreeDiscount);
        moFieldIsFreeDiscount.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsFreeCommissions = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsFreeCommissions);
        moFieldIsFreeCommissions.setTabbedPaneIndex(1, jTabbedPane);
        moFieldSurplusPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_FLOAT, false, jtfSurplusPercentage, jlSurplusPercentage);
        moFieldSurplusPercentage.setIsPercent(true);
        moFieldSurplusPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        moFieldSurplusPercentage.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsReference = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsReference);
        moFieldIsReference.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsPrepayment = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsPrepayment);
        moFieldIsPrepayment.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkDefaultItemRefId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkDefaultItemRefId_n, jlFkDefaultItemRefId_n);
        moFieldFkDefaultItemRefId_n.setPickerButton(jbFkDefaultItemRefId_n);
        moFieldFkDefaultItemRefId_n.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkAdministrativeConceptTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkAdministrativeConceptTypeId, jlFkAdministrativeConceptTypeId);
        moFieldFkAdministrativeConceptTypeId.setPickerButton(jbFkAdministrativeConceptTypeId);
        moFieldFkAdministrativeConceptTypeId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkTaxableConceptTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxableConceptTypeId, jlFkTaxableConceptTypeId);
        moFieldFkTaxableConceptTypeId.setPickerButton(jbFkTaxableConceptTypeId);
        moFieldFkTaxableConceptTypeId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkAccountEbitdaTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkAccountEbitdaTypeId, jlFkAccountEbitdaTypeId);
        moFieldFkAccountEbitdaTypeId.setPickerButton(jbFkAccountEbitdaTypeId);
        moFieldFkAccountEbitdaTypeId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkFiscalAccountIncId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkFiscalAccountIncId, jlFkFiscalAccountIncId);
        moFieldFkFiscalAccountIncId.setPickerButton(jbFkFiscalAccountIncId);
        moFieldFkFiscalAccountIncId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkFiscalAccountExpId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkFiscalAccountExpId, jlFkFiscalAccountExpId);
        moFieldFkFiscalAccountExpId.setPickerButton(jbFkFiscalAccountExpId);
        moFieldFkFiscalAccountExpId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkCfdProdServId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, moKeyCfdProdServId_n.getComponent(), jlFkCfdProdServId_n);
        moFieldFkCfdProdServId_n.setTabbedPaneIndex(1, jTabbedPane);
        moKeyCfdProdServId_n.setKeySettings((SGuiClient) miClient, SGuiUtils.getLabelName(jlFkCfdProdServId_n.getText()), true);
        moFieldTariff = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, moTextTariff.getComponent(), jlTariff);
        moFieldTariff.setLengthMax(10);
        moTextTariff.setTextSettings(SGuiUtils.getLabelName(jlTariff.getText()), 10);
        
        mvFields.add(moFieldFkItemGenericId);
        mvFields.add(moFieldFkItemLineId_n);
        mvFields.add(moFieldName);
        mvFields.add(moFieldNameShort);
        mvFields.add(moFieldPresentation);
        mvFields.add(moFieldPresentationShort);
        mvFields.add(moFieldItemKey);
        mvFields.add(moFieldIsDeleted);
        mvFields.add(moFieldIsBulk);
        mvFields.add(moFieldIsInventoriable);
        mvFields.add(moFieldIsLotApplying);
        mvFields.add(moFieldFkBrandId);
        mvFields.add(moFieldFkManufacturerId);
        mvFields.add(moFieldFkElementId);
        mvFields.add(moFieldFkVariety01Id);
        mvFields.add(moFieldFkVariety02Id);
        mvFields.add(moFieldFkVariety03Id);
        mvFields.add(moFieldFkUnitId);
        mvFields.add(moFieldUnitsContained);
        mvFields.add(moFieldFkUnitUnitsContainedId);
        mvFields.add(moFieldUnitsVirtual);
        mvFields.add(moFieldFkUnitUnitsVirtualId);
        mvFields.add(moFieldNetContent);
        mvFields.add(moFieldFkUnitNetContentId);
        mvFields.add(moFieldIsNetContentVariable);
        mvFields.add(moFieldNetContentUnitary);
        mvFields.add(moFieldFkUnitNetContentUnitaryId);
        mvFields.add(moFieldUnitsPackage);
        mvFields.add(moFieldFkItemPackageId_n);
        mvFields.add(moFieldProductionTime);
        mvFields.add(moFieldProductionCost);
        mvFields.add(moFieldWeightGross);
        mvFields.add(moFieldWeightDelivery);
        mvFields.add(moFieldLength);
        mvFields.add(moFieldLengthUnitary);
        mvFields.add(moFieldIsLengthVariable);
        mvFields.add(moFieldSurface);
        mvFields.add(moFieldSurfaceUnitary);
        mvFields.add(moFieldIsSurfaceVariable);
        mvFields.add(moFieldVolume);
        mvFields.add(moFieldVolumeUnitary);
        mvFields.add(moFieldIsVolumeVariable);
        mvFields.add(moFieldMass);
        mvFields.add(moFieldMassUnitary);
        mvFields.add(moFieldIsMassVariable);
        mvFields.add(moFieldFkUnitAlternativeTypeId);
        mvFields.add(moFieldFkLevelTypeId);
        mvFields.add(moFieldUnitAlternativeBaseEquivalence);

        mvFields.add(moFieldIsFreeDiscountUnitary);
        mvFields.add(moFieldIsFreeDiscountEntry);
        mvFields.add(moFieldIsFreeDiscountDoc);
        mvFields.add(moFieldIsFreePrice);
        mvFields.add(moFieldIsFreeDiscount);
        mvFields.add(moFieldIsFreeCommissions);
        mvFields.add(moFieldSurplusPercentage);
        mvFields.add(moFieldIsReference);
        mvFields.add(moFieldIsPrepayment);
        mvFields.add(moFieldFkDefaultItemRefId_n);
        mvFields.add(moFieldFkAdministrativeConceptTypeId);
        mvFields.add(moFieldFkTaxableConceptTypeId);
        mvFields.add(moFieldFkAccountEbitdaTypeId);
        mvFields.add(moFieldFkFiscalAccountIncId);
        mvFields.add(moFieldFkFiscalAccountExpId);
        mvFields.add(moFieldFkCfdProdServId_n);
        mvFields.add(moFieldTariff);

        moFormItemBarcode = new SFormItemBarcode(miClient);
        moFormItemForeignLanguage = new SFormItemForeignLanguage(miClient);
        moFormNewItemCode = new SFormNewItemCode(miClient);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbCopyName.addActionListener(this);
        jbCopyPresentation.addActionListener(this);
        jbComputeNewCode.addActionListener(this);
        jbEditItemGeneric.addActionListener(this);
        jbEditUnit.addActionListener(this);
        jbFkItemGenericId.addActionListener(this);
        jbFkItemLineId_n.addActionListener(this);
        jbFkItemPackageId_n.addActionListener(this);
        jbAddItemBarcode.addActionListener(this);
        jbModifyItemBarcode.addActionListener(this);
        jbAddItemForeignLanguage.addActionListener(this);
        jbModifyItemForeignLanguage.addActionListener(this);
        jbFkDefaultItemRefId_n.addActionListener(this);
        jbFkAdministrativeConceptTypeId.addActionListener(this);
        jbFkTaxableConceptTypeId.addActionListener(this);
        jbFkAccountEbitdaTypeId.addActionListener(this);
        jbFkFiscalAccountIncId.addActionListener(this);
        jbFkFiscalAccountExpId.addActionListener(this);
        
        jtfProductionCostUnit.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        jtfName.addFocusListener(this);
        jtfNameShort.addFocusListener(this);
        jtfPresentation.addFocusListener(this);
        jtfPresentationShort.addFocusListener(this);
        jtfCode.addFocusListener(this);

        jckIsInventoriable.addItemListener(this);
        jcbFkItemGenericId.addItemListener(this);
        jcbFkItemLineId_n.addItemListener(this);
        jcbFkBrandId.addItemListener(this);
        jcbFkManufacturerId.addItemListener(this);
        jcbFkElementId.addItemListener(this);
        jcbFkVariety01Id.addItemListener(this);
        jcbFkVariety02Id.addItemListener(this);
        jcbFkVariety03Id.addItemListener(this);
        jcbFkUnitId.addItemListener(this);
        jcbFkUnitAlternativeTypeId.addItemListener(this);

        i = 0;
        tableColumnsItemForeignLanguage = new STableColumnForm[4];
        tableColumnsItemForeignLanguage[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem", 400);
        tableColumnsItemForeignLanguage[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem corto", 200);
        tableColumnsItemForeignLanguage[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Idioma", 100);
        tableColumnsItemForeignLanguage[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);

        for (i = 0; i < tableColumnsItemForeignLanguage.length; i++) {
            moItemForeignLanguagePane.addTableColumn(tableColumnsItemForeignLanguage[i]);
        }

        i = 0;
        tableColumnsItemBarcode = new STableColumnForm[2];
        tableColumnsItemBarcode[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Código barras", 400);
        tableColumnsItemBarcode[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);

        for (i = 0; i < tableColumnsItemBarcode.length; i++) {
            moItemBarcodePane.addTableColumn(tableColumnsItemBarcode[i]);
        }

        SFormUtilities.createActionMap(rootPane, this, "publicActionAddItemBarcode", "addBarcode", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionModifyItemBarcode", "modifyBarcode", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionAddItemForeignLanguage", "addForeignLanguage", KeyEvent.VK_N, KeyEvent.ALT_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "publicActionModifyItemForeignLanguage", "modifyForeignLanguage", KeyEvent.VK_M, KeyEvent.ALT_DOWN_MASK);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jTabbedPane.setSelectedIndex(0);
            if (jcbFkItemGenericId.isEnabled()) {
                jcbFkItemGenericId.requestFocus();
            }
            else {
                jtfName.requestFocus();
            }
        }
    }

    private void readItemGenericParams() {
        jtfItemNameRo.setText("");
        jtfItemNameShortRo.setText("");

        if (jcbFkItemGenericId.getSelectedIndex() <= 0) {
            moItemGeneric = null;

            jbFkItemLineId_n.setEnabled(false);
            jcbFkItemLineId_n.setEnabled(false);
            jcbFkItemLineId_n.removeAllItems();

            jtfNameShort.setEnabled(false);
            jtfPresentationShort.setEnabled(false);
            jbCopyName.setEnabled(false);
            jbCopyPresentation.setEnabled(false);

            jtfNameShort.setText("");
            jtfPresentationShort.setText("");
            jtfItemNameShortRo.setText("");

            jtfCode.setEditable(false);
            jtfCode.setFocusable(false);
            jbComputeNewCode.setEnabled(false);
            jtfItemKey.setEditable(false);
            jtfItemKey.setFocusable(false);

            jtfCode.setText("");
            jtfItemKey.setText("");

            jckIsInventoriable.setEnabled(false);
            jckIsInventoriable.setSelected(false);

            jckIsLotApplying.setEnabled(false);
            jckIsLotApplying.setSelected(false);

            jckIsBulk.setEnabled(false);
            jckIsBulk.setSelected(false);

            jcbFkUnitId.removeAllItems();
            jcbFkUnitUnitsContainedId.removeAllItems();
            jcbFkUnitUnitsVirtualId.removeAllItems();
            jcbFkUnitNetContentId.removeAllItems();
            jcbFkUnitNetContentUnitaryId.removeAllItems();

            jcbFkUnitId.setEnabled(false);
            jbEditUnit.setEnabled(false);

            jtfUnitsContained.setEnabled(false);
            jcbFkUnitUnitsContainedId.setEnabled(false);
            moFieldUnitsContained.setFieldValue(0d);
            moFieldFkUnitUnitsContainedId.setFieldValue(new int[] { SDataConstantsSys.ITMU_UNIT_NA });

            jtfUnitsVirtual.setEnabled(false);
            jcbFkUnitUnitsVirtualId.setEnabled(false);
            moFieldUnitsVirtual.setFieldValue(0d);
            moFieldFkUnitUnitsVirtualId.setFieldValue(new int[] { SDataConstantsSys.ITMU_UNIT_NA });

            jtfNetContent.setEnabled(false);
            jcbFkUnitNetContentId.setEnabled(false);
            moFieldNetContent.setFieldValue(0d);
            moFieldFkUnitNetContentId.setFieldValue(new int[] { SDataConstantsSys.ITMU_UNIT_NA });

            jckIsNetContentVariable.setSelected(false);
            jckIsNetContentVariable.setEnabled(false);

            jtfNetContentUnitary.setEnabled(false);
            jcbFkUnitNetContentUnitaryId.setEnabled(false);
            moFieldNetContentUnitary.setFieldValue(0d);
            moFieldFkUnitNetContentUnitaryId.setFieldValue(new int[] { SDataConstantsSys.ITMU_UNIT_NA });

            jtfUnitsPackage.setEnabled(false);
            jcbFkItemPackageId_n.setEnabled(false);
            jbFkItemPackageId_n.setEnabled(false);
            moFieldUnitsPackage.setFieldValue(0d);
            moFieldFkItemPackageId_n.setFieldValue(null);

            jtfWeightGross.setEnabled(false);
            moFieldProductionTime.setFieldValue(0d);
            moFieldProductionCost.setFieldValue(0d);
            moFieldWeightGross.setFieldValue(0d);

            jtfWeightDelivery.setEnabled(false);
            moFieldWeightDelivery.setFieldValue(0d);

            jtfLength.setEnabled(false);
            moFieldLength.setFieldValue(0d);

            jtfLengthUnitary.setEnabled(false);
            moFieldLengthUnitary.setFieldValue(0d);

            jckIsLengthVariable.setSelected(false);
            jckIsLengthVariable.setEnabled(false);

            jtfSurface.setEnabled(false);
            moFieldSurface.setFieldValue(0d);

            jtfSurfaceUnitary.setEnabled(false);
            moFieldSurfaceUnitary.setFieldValue(0d);

            jckIsSurfaceVariable.setSelected(false);
            jckIsSurfaceVariable.setEnabled(false);

            jtfVolume.setEnabled(false);
            moFieldVolume.setFieldValue(0d);

            jtfVolumeUnitary.setEnabled(false);
            moFieldVolumeUnitary.setFieldValue(0d);

            jckIsVolumeVariable.setSelected(false);
            jckIsVolumeVariable.setEnabled(false);

            jtfMass.setEnabled(false);
            moFieldMass.setFieldValue(0d);

            jtfMassUnitary.setEnabled(false);
            moFieldMassUnitary.setFieldValue(0d);

            jckIsMassVariable.setSelected(false);
            jckIsMassVariable.setEnabled(false);

            jcbFkUnitAlternativeTypeId.setEnabled(false);
            moFieldFkUnitAlternativeTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_UNIT_NA });

            moFieldFkLevelTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_LEV_NA });

            jckIsFreeDiscountUnitary.setEnabled(false);
            jckIsFreeDiscountUnitary.setSelected(false);
            jckIsFreeDiscountEntry.setEnabled(false);
            jckIsFreeDiscountEntry.setSelected(false);
            jckIsFreeDiscountDoc.setEnabled(false);
            jckIsFreeDiscountDoc.setSelected(false);
            jckIsFreePrice.setEnabled(false);
            jckIsFreePrice.setSelected(false);
            jckIsFreeDiscount.setEnabled(false);
            jckIsFreeDiscount.setSelected(false);
            jckIsFreeCommissions.setEnabled(false);
            jckIsFreeCommissions.setSelected(false);

            jcbFkDefaultItemRefId_n.setEnabled(false);
            jbFkDefaultItemRefId_n.setEnabled(false);
            moFieldFkDefaultItemRefId_n.setFieldValue(null);

            jtfProductionTime.setEnabled(false);
            jtfProductionCost.setEnabled(false);
        }
        else {
            moItemGeneric = (SDataItemGeneric) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_IGEN, moFieldFkItemGenericId.getKey(), SLibConstants.EXEC_MODE_SILENT);

            if (moItemGeneric.getIsItemLineApplying()) {
                jcbFkItemLineId_n.setEnabled(true);
                jbFkItemLineId_n.setEnabled(true);
                SFormUtilities.populateComboBox(miClient, jcbFkItemLineId_n, SDataConstants.ITMU_LINE, moFieldFkItemGenericId.getKeyAsIntArray());
            }
            else {
                jcbFkItemLineId_n.setEnabled(false);
                jbFkItemLineId_n.setEnabled(false);
                jcbFkItemLineId_n.removeAllItems();
            }

            if (moItemGeneric.getIsItemShortApplying()) {
                jtfNameShort.setEnabled(true);
                jtfPresentationShort.setEnabled(true);
                jbCopyName.setEnabled(true);
                jbCopyPresentation.setEnabled(true);
            }
            else {
                jtfNameShort.setEnabled(false);
                jtfPresentationShort.setEnabled(false);
                jbCopyName.setEnabled(false);
                jbCopyPresentation.setEnabled(false);

                jtfNameShort.setText("");
                jtfPresentationShort.setText("");
                jtfItemNameShortRo.setText("");
            }

            if (!moItemGeneric.getIsItemKeyApplying()) {
                jtfCode.setEditable(false);
                jtfCode.setFocusable(false);
                jbComputeNewCode.setEnabled(false);
                jtfItemKey.setEditable(false);
                jtfItemKey.setFocusable(false);
            }
            else {
                if (moItemGeneric.getIsItemKeyAutomatic()) {
                    jtfCode.setEditable(true);
                    jtfCode.setFocusable(true);
                    jbComputeNewCode.setEnabled(true);
                    jtfItemKey.setEditable(false);
                    jtfItemKey.setFocusable(false);
                }
                else {
                    jtfCode.setEditable(false);
                    jtfCode.setFocusable(false);
                    jbComputeNewCode.setEnabled(false);
                    jtfItemKey.setEditable(true);
                    jtfItemKey.setFocusable(true);
                }
            }

            if (!jtfCode.isEditable()) {
                jtfCode.setText("");
            }

            if (!jtfItemKey.isEditable()) {
                jtfItemKey.setText("");
            }

            jckIsInventoriable.setEnabled(moItemGeneric.getIsInventoriable());
            jckIsInventoriable.setSelected(moItemGeneric.getIsInventoriable());

            jckIsLotApplying.setEnabled(moItemGeneric.getIsLotApplying());
            jckIsLotApplying.setSelected(moItemGeneric.getIsLotApplying());

            jckIsBulk.setEnabled(moItemGeneric.getIsBulk());
            jckIsBulk.setSelected(moItemGeneric.getIsBulk());

            SFormUtilities.populateComboBox(miClient, jcbFkUnitId, SDataConstants.ITMU_UNIT, new int[] { moItemGeneric.getFkUnitTypeId() });
            SFormUtilities.populateComboBox(miClient, jcbFkUnitUnitsContainedId, SDataConstants.ITMU_UNIT, new int[] { moItemGeneric.getFkUnitUnitsContainedTypeId()} );
            SFormUtilities.populateComboBox(miClient, jcbFkUnitUnitsVirtualId, SDataConstants.ITMU_UNIT, new int[] { moItemGeneric.getFkUnitUnitsVirtualTypeId() });
            SFormUtilities.populateComboBox(miClient, jcbFkUnitNetContentId, SDataConstants.ITMU_UNIT, new int[] { moItemGeneric.getFkUnitNetContentTypeId() });
            SFormUtilities.populateComboBox(miClient, jcbFkUnitNetContentUnitaryId, SDataConstants.ITMU_UNIT, new int[] { moItemGeneric.getFkUnitNetContentUnitaryTypeId() });

            jcbFkUnitId.setEnabled(true);
            jbEditUnit.setEnabled(false);

            jtfUnitsContained.setEnabled(moItemGeneric.getIsUnitsContainedApplying());
            jcbFkUnitUnitsContainedId.setEnabled(moItemGeneric.getIsUnitsContainedApplying());
            if (!moItemGeneric.getIsUnitsContainedApplying()) {
                moFieldUnitsContained.setFieldValue(0d);
                moFieldFkUnitUnitsContainedId.setFieldValue(new int[] { SDataConstantsSys.ITMU_UNIT_NA });
            }

            jtfUnitsVirtual.setEnabled(moItemGeneric.getIsUnitsVirtualApplying());
            jcbFkUnitUnitsVirtualId.setEnabled(moItemGeneric.getIsUnitsVirtualApplying());
            if (!moItemGeneric.getIsUnitsVirtualApplying()) {
                moFieldUnitsVirtual.setFieldValue(0d);
                moFieldFkUnitUnitsVirtualId.setFieldValue(new int[] { SDataConstantsSys.ITMU_UNIT_NA });
            }

            jtfNetContent.setEnabled(moItemGeneric.getIsNetContentApplying());
            jcbFkUnitNetContentId.setEnabled(moItemGeneric.getIsNetContentApplying());
            if (!moItemGeneric.getIsNetContentApplying()) {
                moFieldNetContent.setFieldValue(0d);
                moFieldFkUnitNetContentId.setFieldValue(new int[] { SDataConstantsSys.ITMU_UNIT_NA });
            }

            jckIsNetContentVariable.setSelected(moItemGeneric.getIsNetContentVariable());
            jckIsNetContentVariable.setEnabled(moItemGeneric.getIsNetContentVariable());

            jtfNetContentUnitary.setEnabled(moItemGeneric.getIsNetContentUnitaryApplying());
            jcbFkUnitNetContentUnitaryId.setEnabled(moItemGeneric.getIsNetContentUnitaryApplying());
            if (!moItemGeneric.getIsNetContentUnitaryApplying()) {
                moFieldNetContentUnitary.setFieldValue(0d);
                moFieldFkUnitNetContentUnitaryId.setFieldValue(new int[] { SDataConstantsSys.ITMU_UNIT_NA });
            }

            jtfUnitsPackage.setEnabled(moItemGeneric.getIsUnitsPackageApplying());
            jcbFkItemPackageId_n.setEnabled(moItemGeneric.getIsUnitsPackageApplying());
            jbFkItemPackageId_n.setEnabled(moItemGeneric.getIsUnitsPackageApplying());
            if (!moItemGeneric.getIsUnitsPackageApplying()) {
                moFieldUnitsPackage.setFieldValue(0d);
                moFieldFkItemPackageId_n.setFieldValue(null);
            }

            moFieldProductionTime.setFieldValue(0d);
            moFieldProductionCost.setFieldValue(0d);
            
            jtfWeightGross.setEnabled(moItemGeneric.getIsWeightGrossApplying());
            if (!moItemGeneric.getIsWeightGrossApplying()) {
                moFieldWeightGross.setFieldValue(0d);
            }

            jtfWeightDelivery.setEnabled(moItemGeneric.getIsWeightDeliveryApplying());
            if (!moItemGeneric.getIsWeightDeliveryApplying()) {
                moFieldWeightDelivery.setFieldValue(0d);
            }

            jtfLength.setEnabled(moItemGeneric.getIsLengthApplying());
            if (!moItemGeneric.getIsLengthApplying()) {
                moFieldLength.setFieldValue(0d);
            }

            jtfLengthUnitary.setEnabled(moItemGeneric.getIsLengthUnitaryApplying());
            if (!moItemGeneric.getIsLengthUnitaryApplying()) {
                moFieldLengthUnitary.setFieldValue(0d);
            }

            jckIsLengthVariable.setSelected(moItemGeneric.getIsLengthVariable());
            jckIsLengthVariable.setEnabled(moItemGeneric.getIsLengthVariable());

            jtfSurface.setEnabled(moItemGeneric.getIsSurfaceApplying());
            if (!moItemGeneric.getIsSurfaceApplying()) {
                moFieldSurface.setFieldValue(0d);
            }

            jtfSurfaceUnitary.setEnabled(moItemGeneric.getIsSurfaceUnitaryApplying());
            if (moItemGeneric.getIsSurfaceUnitaryApplying()) {
                moFieldSurfaceUnitary.setFieldValue(0d);
            }

            jckIsSurfaceVariable.setSelected(moItemGeneric.getIsSurfaceVariable());
            jckIsSurfaceVariable.setEnabled(moItemGeneric.getIsSurfaceVariable());

            jtfVolume.setEnabled(moItemGeneric.getIsVolumeApplying());
            if (!moItemGeneric.getIsVolumeApplying()) {
                moFieldVolume.setFieldValue(0d);
            }

            jtfVolumeUnitary.setEnabled(moItemGeneric.getIsVolumeUnitaryApplying());
            if (!moItemGeneric.getIsVolumeUnitaryApplying()) {
                moFieldVolumeUnitary.setFieldValue(0d);
            }

            jckIsVolumeVariable.setSelected(moItemGeneric.getIsVolumeVariable());
            jckIsVolumeVariable.setEnabled(moItemGeneric.getIsVolumeVariable());

            jtfMass.setEnabled(moItemGeneric.getIsMassApplying());
            if (!moItemGeneric.getIsMassApplying()) {
                moFieldMass.setFieldValue(0d);
            }

            jtfMassUnitary.setEnabled(moItemGeneric.getIsMassUnitaryApplying());
            if (!moItemGeneric.getIsMassUnitaryApplying()) {
                moFieldMassUnitary.setFieldValue(0d);
            }

            jckIsMassVariable.setSelected(moItemGeneric.getIsMassVariable());
            jckIsMassVariable.setEnabled(moItemGeneric.getIsMassVariable());

            jcbFkUnitAlternativeTypeId.setEnabled(moFieldIsInventoriable.getBoolean());

            jckIsFreeDiscountUnitary.setEnabled(moItemGeneric.getDbmsIsFreeDiscountUnitary());
            moFieldIsFreeDiscountUnitary.setFieldValue(moItemGeneric.getDbmsIsFreeDiscountUnitary());

            jckIsFreeDiscountEntry.setEnabled(moItemGeneric.getDbmsIsFreeDiscountEntry());
            moFieldIsFreeDiscountEntry.setFieldValue(moItemGeneric.getDbmsIsFreeDiscountEntry());

            jckIsFreeDiscountDoc.setEnabled(moItemGeneric.getDbmsIsFreeDiscountDoc());
            moFieldIsFreeDiscountDoc.setFieldValue(moItemGeneric.getDbmsIsFreeDiscountDoc());

            jckIsFreePrice.setEnabled(moItemGeneric.getDbmsIsFreePrice());
            moFieldIsFreePrice.setFieldValue(moItemGeneric.getDbmsIsFreePrice());

            jckIsFreeDiscount.setEnabled(moItemGeneric.getDbmsIsFreeDiscount());
            moFieldIsFreeDiscount.setFieldValue(moItemGeneric.getDbmsIsFreeDiscount());

            jckIsFreeCommissions.setEnabled(moItemGeneric.getDbmsIsFreeCommissions());
            moFieldIsFreeCommissions.setFieldValue(moItemGeneric.getDbmsIsFreeCommissions());

            jcbFkDefaultItemRefId_n.setEnabled(moItemGeneric.getIsItemReferenceRequired());
            jbFkDefaultItemRefId_n.setEnabled(moItemGeneric.getIsItemReferenceRequired());
            if (!moItemGeneric.getIsItemReferenceRequired()) {
                moFieldFkDefaultItemRefId_n.setFieldValue(null);
            }
            if (SLibUtilities.belongsTo(new int[] { moItemGeneric.getFkItemCategoryId(), moItemGeneric.getFkItemClassId(), moItemGeneric.getFkItemTypeId()}, new int[][] { SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_FG, SDataConstantsSys.ITMS_TP_ITEM_SAL_PRO_WP})) {
                jtfProductionTime.setEnabled(true);
                jtfProductionCost.setEnabled(true);
            }
            else {
                jtfProductionTime.setEnabled(false);
                jtfProductionCost.setEnabled(true);
            }
        }

        readItemLineParams();
    }

    private void readItemLineParams() {
        if (jcbFkItemLineId_n.getSelectedIndex() <= 0) {
            moItemLine = null;

            jcbFkBrandId.setEnabled(true);
            jcbFkManufacturerId.setEnabled(true);
            jcbFkElementId.setEnabled(true);

            moFieldFkBrandId.setFieldValue(new int[] { SDataConstantsSys.ITMU_BRD_NA });
            moFieldFkManufacturerId.setFieldValue(new int[] { SDataConstantsSys.ITMU_MFR_NA });
            moFieldFkElementId.setFieldValue(new int[] { SDataConstantsSys.ITMU_EMT_NA });

            jcbFkVariety01Id.setEnabled(false);
            jcbFkVariety02Id.setEnabled(false);
            jcbFkVariety03Id.setEnabled(false);
            jcbFkVariety01Id.removeAllItems();
            jcbFkVariety02Id.removeAllItems();
            jcbFkVariety03Id.removeAllItems();
        }
        else {
            moItemLine = (SDataItemLine) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_LINE, moFieldFkItemLineId_n.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);

            jcbFkBrandId.setEnabled(!(moItemLine.getIsFixedBrand() || moItemLine.getFkBrandId() == SDataConstantsSys.ITMU_BRD_NA));
            jcbFkManufacturerId.setEnabled(!(moItemLine.getIsFixedManufacturer() || moItemLine.getFkManufacturerId() == SDataConstantsSys.ITMU_MFR_NA));
            jcbFkElementId.setEnabled(!(moItemLine.getIsFixedElement() || moItemLine.getFkElementId() == SDataConstantsSys.ITMU_EMT_NA));

            moFieldFkBrandId.setFieldValue(new int[] { moItemLine.getFkBrandId() });
            moFieldFkManufacturerId.setFieldValue(new int[] { moItemLine.getFkManufacturerId() });
            moFieldFkElementId.setFieldValue(new int[] { moItemLine.getFkElementId() });

            jcbFkVariety01Id.setEnabled(moItemLine.getFkVarietyType01Id() != SDataConstantsSys.ITMU_TP_VAR_NA);
            jcbFkVariety02Id.setEnabled(moItemLine.getFkVarietyType02Id() != SDataConstantsSys.ITMU_TP_VAR_NA);
            jcbFkVariety03Id.setEnabled(moItemLine.getFkVarietyType03Id() != SDataConstantsSys.ITMU_TP_VAR_NA);
            SFormUtilities.populateComboBox(miClient, jcbFkVariety01Id, SDataConstants.ITMU_VAR, new int[] { moItemLine.getFkVarietyType01Id() });
            SFormUtilities.populateComboBox(miClient, jcbFkVariety02Id, SDataConstants.ITMU_VAR, new int[] { moItemLine.getFkVarietyType02Id() });
            SFormUtilities.populateComboBox(miClient, jcbFkVariety03Id, SDataConstants.ITMU_VAR, new int[] { moItemLine.getFkVarietyType03Id() });

            if (!jcbFkVariety01Id.isEnabled()) {
                moFieldFkVariety01Id.setFieldValue(new int[] { SDataConstantsSys.ITMU_VAR_NA });
            }
            if (!jcbFkVariety02Id.isEnabled()) {
                moFieldFkVariety02Id.setFieldValue(new int[] { SDataConstantsSys.ITMU_VAR_NA });
            }
            if (!jcbFkVariety03Id.isEnabled()) {
                moFieldFkVariety03Id.setFieldValue(new int[] { SDataConstantsSys.ITMU_VAR_NA });
            }
        }
    }

    private java.util.Vector<Integer> getItemLanguages() {
        java.util.Vector<Integer> languages = new Vector<Integer>();

        for (int i = 0; i < moItemForeignLanguagePane.getTableGuiRowCount(); i++) {
            languages.add(((SDataItemForeignLanguage) moItemForeignLanguagePane.getTableRow(i).getData()).getPkLanguageId());
        }

        return languages;
    }

    private void computeItemKeyAndName() {
        computeItemKey();
        computeItemName();
        computeItemNameShort();
    }

    private void computeItemKey() {
        String key = "";
        SDataBrand brd = null;
        SDataManufacturer mfr = null;
        SDataVariety var = null;

        if (jcbFkItemGenericId.getSelectedIndex() > 0) {
            if (moItemGeneric.getIsItemKeyApplying() && moItemGeneric.getIsItemKeyAutomatic()) {
                brd = (SDataBrand) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_BRD, jcbFkBrandId.getSelectedIndex() <= 0 ? new int[] { SDataConstantsSys.ITMU_BRD_NA } : moFieldFkBrandId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                mfr = (SDataManufacturer) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_MFR, jcbFkManufacturerId.getSelectedIndex() <= 0 ? new int[] { SDataConstantsSys.ITMU_MFR_NA } : moFieldFkManufacturerId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);

                if (!moItemGeneric.getIsItemLineApplying()) {
                    for (int pos = 1; pos <= SDataConstantsSys.ITMS_KEY_ORD_POS_QTY; pos++) {
                        key += SLibUtilities.textTrim(getItemKeyAndNameByPosition(brd, mfr, pos, SDataConstantsSys.ITMS_DEF_ITEM_KEY));
                    }
                }
                else {
                    for (int pos = 1; pos <= SDataConstantsSys.ITMS_KEY_LINE_POS_QTY; pos++) {
                        key += SLibUtilities.textTrim(getItemKeyAndNameByPosition(brd, mfr, pos, SDataConstantsSys.ITMS_DEF_ITEM_KEY));
                    }

                    key += moFieldCode.getString();

                    if (jcbFkVariety01Id.getSelectedIndex() > 0 && moFieldFkVariety01Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                        var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety01Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                        key += var.getCode();
                    }
                    if (jcbFkVariety02Id.getSelectedIndex() > 0 && moFieldFkVariety02Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                        var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety02Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                        key += var.getCode();
                    }
                    if (jcbFkVariety03Id.getSelectedIndex() > 0 && moFieldFkVariety03Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                        var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety03Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                        key += var.getCode();
                    }
                }
                
                moFieldItemKey.setFieldValue(key);
            }
        }
    }

    private void computeItemName() {
        String name = "";
        SDataBrand brd = null;
        SDataManufacturer mfr = null;
        SDataVariety var = null;

        if (jcbFkItemGenericId.getSelectedIndex() > 0) {
            brd = (SDataBrand) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_BRD, jcbFkBrandId.getSelectedIndex() <= 0 ? new int[] { SDataConstantsSys.ITMU_BRD_NA } : moFieldFkBrandId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
            mfr = (SDataManufacturer) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_MFR, jcbFkManufacturerId.getSelectedIndex() <= 0 ? new int[] { SDataConstantsSys.ITMU_MFR_NA } : moFieldFkManufacturerId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);

            if (!moItemGeneric.getIsItemLineApplying()) {
                for (int pos = 1; pos <= SDataConstantsSys.ITMS_NAM_ORD_POS_QTY; pos++) {
                    name += (name.length() == 0 ? "" : " ") + SLibUtilities.textTrim(getItemKeyAndNameByPosition(brd, mfr, pos, SDataConstantsSys.ITMS_DEF_ITEM));
                }
            }
            else {
                for (int pos = 1; pos <= SDataConstantsSys.ITMS_NAM_LINE_POS_QTY; pos++) {
                    name += (name.length() == 0 ? "" : " ") + SLibUtilities.textTrim(getItemKeyAndNameByPosition(brd, mfr, pos, SDataConstantsSys.ITMS_DEF_ITEM));
                }

                name += (name.length() == 0 ? "" : " ") + moFieldName.getString();
                name += (name.length() == 0 ? "" : " ") + moFieldPresentation.getString();

                if (jcbFkVariety01Id.getSelectedIndex() > 0 && moFieldFkVariety01Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                    var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety01Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                    name += (name.length() == 0 ? "" : " ") + var.getVariety();
                }
                if (jcbFkVariety02Id.getSelectedIndex() > 0 && moFieldFkVariety02Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                    var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety02Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                    name += (name.length() == 0 ? "" : " ") + var.getVariety();
                }
                if (jcbFkVariety03Id.getSelectedIndex() > 0 && moFieldFkVariety03Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                    var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety03Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                    name += (name.length() == 0 ? "" : " ") + var.getVariety();
                }
            }

            jtfItemNameRo.setText(SLibUtilities.textTrim(name));
            jtfItemNameRo.setCaretPosition(0);
        }
    }

    private void computeItemNameShort() {
        String nameShort = "";
        SDataBrand brd = null;
        SDataManufacturer mfr = null;
        SDataVariety var = null;

        if (jcbFkItemGenericId.getSelectedIndex() > 0) {
            if (moItemGeneric.getIsItemShortApplying()) {
                brd = (SDataBrand) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_BRD, jcbFkBrandId.getSelectedIndex() <= 0 ? new int[] { SDataConstantsSys.ITMU_BRD_NA } : moFieldFkBrandId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                mfr = (SDataManufacturer) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_MFR, jcbFkManufacturerId.getSelectedIndex() <= 0 ? new int[] { SDataConstantsSys.ITMU_MFR_NA } : moFieldFkManufacturerId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);

                if (!moItemGeneric.getIsItemLineApplying()) {
                    for (int pos = 1; pos <= SDataConstantsSys.ITMS_NAM_ORD_POS_QTY; pos++) {
                        nameShort += (nameShort.length() == 0 ? "" : " ") + SLibUtilities.textTrim(getItemKeyAndNameByPosition(brd, mfr, pos, SDataConstantsSys.ITMS_DEF_ITEM_SHORT));
                    }
                }
                else {
                    for (int pos = 1; pos <= SDataConstantsSys.ITMS_NAM_LINE_POS_QTY; pos++) {
                        nameShort += (nameShort.length() == 0 ? "" : " ") + SLibUtilities.textTrim(getItemKeyAndNameByPosition(brd, mfr, pos, SDataConstantsSys.ITMS_DEF_ITEM_SHORT));
                    }

                    nameShort += (nameShort.length() == 0 ? "" : " ") + moFieldNameShort.getString();
                    nameShort += (nameShort.length() == 0 ? "" : " ") + moFieldPresentationShort.getString();

                    if (jcbFkVariety01Id.getSelectedIndex() > 0 && moFieldFkVariety01Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                        var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety01Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                        nameShort += (nameShort.length() == 0 ? "" : " ") + var.getCode();
                    }
                    if (jcbFkVariety02Id.getSelectedIndex() > 0 && moFieldFkVariety02Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                        var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety02Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                        nameShort += (nameShort.length() == 0 ? "" : " ") + var.getCode();
                    }
                    if (jcbFkVariety03Id.getSelectedIndex() > 0 && moFieldFkVariety03Id.getKeyAsIntArray()[0] != SDataConstantsSys.ITMU_VAR_NA) {
                        var = (SDataVariety) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_VAR, moFieldFkVariety03Id.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                        nameShort += (nameShort.length() == 0 ? "" : " ") + var.getCode();
                    }
                }
            }
            
            jtfItemNameShortRo.setText(SLibUtilities.textTrim(nameShort));
            jtfItemNameShortRo.setCaretPosition(0);
        }
    }

    private java.lang.String getItemKeyAndNameByPosition(SDataBrand brd, SDataManufacturer mfr, int position, int fieldType) {
        String text = "";

        switch (fieldType) {
            case SDataConstantsSys.ITMS_DEF_ITEM_KEY:
                if (!moItemGeneric.getIsItemLineApplying() || moItemLine == null) {
                    if (moItemGeneric.getKeyOrdinaryPosItemGeneric() == position) {
                        text = moItemGeneric.getCode();
                    }
                    else if (moItemGeneric.getKeyOrdinaryPosBrand() == position && brd.getPkBrandId() != SDataConstantsSys.ITMU_BRD_NA) {
                        text = brd.getCode();
                    }
                    else if (moItemGeneric.getKeyOrdinaryPosManufacturer() == position && mfr.getPkManufacturerId() != SDataConstantsSys.ITMU_MFR_NA) {
                        text = mfr.getCode();
                    }
                    else if (moItemGeneric.getKeyOrdinaryPosCode() == position) {
                        text = SLibUtilities.textTrim(moFieldCode.getString()).toUpperCase();
                    }
                }
                else {
                    if (moItemGeneric.getKeyLinePosItemGeneric() == position) {
                        text = moItemGeneric.getCode();
                    }
                    else if (moItemGeneric.getKeyLinePosItemLine() == position) {
                        text = moItemLine.getCode();
                    }
                    else if (moItemGeneric.getKeyLinePosBrand() == position && brd.getPkBrandId() != SDataConstantsSys.ITMU_BRD_NA) {
                        text = brd.getCode();
                    }
                    else if (moItemGeneric.getKeyLinePosManufacturer() == position && mfr.getPkManufacturerId() != SDataConstantsSys.ITMU_MFR_NA) {
                        text = mfr.getCode();
                    }
                }
                break;

            case SDataConstantsSys.ITMS_DEF_ITEM:
                if (!moItemGeneric.getIsItemLineApplying() || moItemLine == null) {
                    if (moItemGeneric.getNamingOrdinaryPosItemGeneric() == position) {
                        text = moItemGeneric.getItemGeneric();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosBrand() == position && brd.getPkBrandId() != SDataConstantsSys.ITMU_BRD_NA) {
                        text = brd.getBrand();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosManufacturer() == position && mfr.getPkManufacturerId() != SDataConstantsSys.ITMU_MFR_NA) {
                        text = mfr.getManufacturer();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosName() == position) {
                        text = SLibUtilities.textTrim(moFieldName.getString()).toUpperCase();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosPresentation() == position) {
                        text = SLibUtilities.textTrim(moFieldPresentation.getString()).toUpperCase();
                    }
                }
                else {
                    if (moItemGeneric.getNamingLinePosItemGeneric() == position) {
                        text = moItemGeneric.getItemGeneric();
                    }
                    else if (moItemGeneric.getNamingLinePosItemLine() == position) {
                        text = moItemLine.getItemLine();
                    }
                    else if (moItemGeneric.getNamingLinePosBrand() == position && brd.getPkBrandId() != SDataConstantsSys.ITMU_BRD_NA) {
                        text = brd.getBrand();
                    }
                    else if (moItemGeneric.getNamingLinePosManufacturer() == position && mfr.getPkManufacturerId() != SDataConstantsSys.ITMU_MFR_NA) {
                        text = mfr.getManufacturer();
                    }
                }
                break;

            case SDataConstantsSys.ITMS_DEF_ITEM_SHORT:
                if (!moItemGeneric.getIsItemLineApplying() || moItemLine == null) {
                    if (moItemGeneric.getNamingOrdinaryPosItemGeneric() == position) {
                        text = moItemGeneric.getItemGenericShort();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosBrand() == position && brd.getPkBrandId() != SDataConstantsSys.ITMU_BRD_NA) {
                        text = brd.getBrand();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosManufacturer() == position && mfr.getPkManufacturerId() != SDataConstantsSys.ITMU_MFR_NA) {
                        text = mfr.getManufacturer();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosName() == position) {
                        text = moFieldNameShort.getString().toUpperCase();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosPresentation() == position) {
                        text = moFieldPresentationShort.getString().toUpperCase();
                    }
                }
                else {
                    if (moItemGeneric.getNamingLinePosItemGeneric() == position) {
                        text = moItemGeneric.getItemGenericShort();
                    }
                    else if (moItemGeneric.getNamingLinePosItemLine() == position) {
                        text = moItemLine.getItemLineShort();
                    }
                    else if (moItemGeneric.getNamingLinePosBrand() == position && brd.getPkBrandId() != SDataConstantsSys.ITMU_BRD_NA) {
                        text = brd.getBrand();
                    }
                    else if (moItemGeneric.getNamingOrdinaryPosManufacturer() == position && mfr.getPkManufacturerId() != SDataConstantsSys.ITMU_MFR_NA) {
                        text = mfr.getManufacturer();
                    }
                }
                break;

            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return text;
    }

    private boolean hasStock() {
        boolean has = false;

        try {
            has = STrnUtilities.hasStock(miClient, miClient.getSession().getSystemYear(), moItem.getPkItemId(), SLibConstants.UNDEFINED, SLibTimeUtilities.getEndOfYear(miClient.getSession().getSystemDate()));
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }

        return has;
    }

    /*
     * Action listener methods:
     */

    private void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getTabbedPaneIndex() >= 0) {
                jTabbedPane.setSelectedIndex(validation.getTabbedPaneIndex());
            }
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionCopyName() {
        if (jtfName.getText().length() > moFieldName.getLengthMax()) {
            jtfNameShort.setText(jtfName.getText().substring(0, moFieldName.getLengthMax() - 1));
        }
        else {
            jtfNameShort.setText(jtfName.getText());
        }
    }

    private void actionCopyPresentation() {
        if (jtfPresentation.getText().length() > moFieldPresentation.getLengthMax()) {
            jtfPresentationShort.setText(jtfPresentation.getText().substring(0, moFieldPresentation.getLengthMax() - 1));
        }
        else {
            jtfPresentationShort.setText(jtfPresentation.getText());
        }
    }

    private void actionComputeNewCode() {
        moFormNewItemCode.formRefreshCatalogues();
        moFormNewItemCode.formReset();
        moFormNewItemCode.setParamItemGeneric(jcbFkItemGenericId.getSelectedItem().toString());
        moFormNewItemCode.setParamItemKey(moFieldItemKey.getString().replace(moFieldCode.getString(), ""));
        moFormNewItemCode.setParamPkItemGeneric(moFieldFkItemGenericId.getKeyAsIntArray()[0]);
        moFormNewItemCode.setFormVisible(true);

        if (moFormNewItemCode.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
            moFieldCode.setFieldValue((String) moFormNewItemCode.getValue(SDataConstants.ITMU_ITEM));
            jtfCode.requestFocus();
        }
    }

    private void actionEditItemGeneric() {
        boolean edit = false;

        edit = miClient.showMsgBoxConfirm("Cambiar el valor del campo '" + jlFkItemGenericId.getText() + "' afectará considerablemente la configuración del ítem.\n" + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION;

        if (edit && hasStock()) {
            edit = miClient.showMsgBoxConfirm("El ítem '" + jtfName.getText() + "' tiene existencias en los inventarios.\n" + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION;
        }

        if (edit) {
            jbEditItemGeneric.setEnabled(false);
            jcbFkItemGenericId.setEnabled(true);
            jbFkItemGenericId.setEnabled(true);
            jcbFkItemLineId_n.setEnabled(jcbFkItemLineId_n.getItemCount() > 0);
            jbFkItemLineId_n.setEnabled(jcbFkItemLineId_n.getItemCount() > 0);

            jcbFkItemGenericId.requestFocus();
        }
    }

    private void actionEditUnit() {
        boolean edit = false;

        edit = miClient.showMsgBoxConfirm("Cambiar el valor del campo '" + jlFkUnitId.getText() + "' afectará al control de las existencias en los inventarios.\n" + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION;

        if (edit && hasStock()) {
            edit = miClient.showMsgBoxConfirm("El ítem '" + jtfName.getText() + "' tiene existencias en los inventarios.\n" + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION;
        }

        if (edit) {
            jbEditUnit.setEnabled(false);
            jcbFkUnitId.setEnabled(true);

            jcbFkUnitId.requestFocus();
        }
    }

    private void actionAddItemBarcode() {
        if (jbAddItemBarcode.isEnabled()) {
            moFormItemBarcode.formRefreshCatalogues();
            moFormItemBarcode.formReset();
            moFormItemBarcode.setVisible(true);

            if (moFormItemBarcode.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                moItemBarcodePane.addTableRow(new SDataItemBarcodeRow(moFormItemBarcode.getRegistry()));
                moItemBarcodePane.renderTableRows();
                moItemBarcodePane.setTableRowSelection(moItemBarcodePane.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionModifyItemBarcode() {
        int index = 0;

        if (jbModifyItemBarcode.isEnabled()) {
            index = moItemBarcodePane.getTable().getSelectedRow();

            if (index != -1) {
                moFormItemBarcode.formRefreshCatalogues();
                moFormItemBarcode.formReset();
                moFormItemBarcode.setRegistry((SDataItemBarcode) moItemBarcodePane.getTableRow(index).getData());
                moFormItemBarcode.setVisible(true);

                if (moFormItemBarcode.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                    moItemBarcodePane.getTableModel().getTableRows().set(index, new SDataItemBarcodeRow(moFormItemBarcode.getRegistry()));
                    moItemBarcodePane.renderTableRows();
                    moItemBarcodePane.setTableRowSelection(index);
                }
            }
        }
    }

    private void actionAddItemForeignLanguage() {
        if (jbAddItemForeignLanguage.isEnabled()) {
            moFormItemForeignLanguage.formRefreshCatalogues();
            moFormItemForeignLanguage.formReset();
            moFormItemForeignLanguage.setParamIsItemShortEnable(jtfNameShort.isEnabled());
            moFormItemForeignLanguage.setParamLanguageIds(getItemLanguages());
            moFormItemForeignLanguage.setVisible(true);

            if (moFormItemForeignLanguage.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                moItemForeignLanguagePane.addTableRow(new SDataItemForeignLanguageRow(moFormItemForeignLanguage.getRegistry()));
                moItemForeignLanguagePane.renderTableRows();
                moItemForeignLanguagePane.setTableRowSelection(moItemForeignLanguagePane.getTableGuiRowCount() - 1);
            }
        }
    }

    private void actionModifyItemForeignLanguage() {
        int index = 0;

        if (jbModifyItemForeignLanguage.isEnabled()) {
            index = moItemForeignLanguagePane.getTable().getSelectedRow();

            if (index != -1) {
                moFormItemForeignLanguage.formRefreshCatalogues();
                moFormItemForeignLanguage.formReset();
                moFormItemForeignLanguage.setParamIsItemShortEnable(jtfNameShort.isEnabled());
                moFormItemForeignLanguage.setParamLanguageIds(getItemLanguages());
                moFormItemForeignLanguage.setRegistry((SDataItemForeignLanguage) moItemForeignLanguagePane.getTableRow(index).getData());
                moFormItemForeignLanguage.setVisible(true);

                if (moFormItemForeignLanguage.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                    moItemForeignLanguagePane.getTableModel().getTableRows().set(index, new SDataItemForeignLanguageRow(moFormItemForeignLanguage.getRegistry()));
                    moItemForeignLanguagePane.renderTableRows();
                    moItemForeignLanguagePane.setTableRowSelection(index);
                }
            }
        }
    }

    private void actionFkItemGenericId() {
        miClient.pickOption(SDataConstants.ITMU_IGEN, moFieldFkItemGenericId, null);
    }

    private void actionFkItemLineId_n() {
        miClient.pickOption(SDataConstants.ITMU_LINE, moFieldFkItemLineId_n, moFieldFkItemGenericId.getKeyAsIntArray());
    }

    private void actionFkItemPackageId_n() {
        miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldFkItemPackageId_n, null);
    }

    private void actionFkDefaultItemRefId_n() {
        miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldFkDefaultItemRefId_n, null);
    }

    private void actionFkAdministrativeConceptTypeId() {
        miClient.pickOption(SDataConstants.FINU_TP_ADM_CPT, moFieldFkAdministrativeConceptTypeId, null);
    }

    private void actionFkTaxableConceptTypeId() {
        miClient.pickOption(SDataConstants.FINU_TP_TAX_CPT, moFieldFkTaxableConceptTypeId, null);
    }

    private void actionFkAccountEbitdaTypeId() {
        miClient.pickOption(SDataConstants.FINU_TP_ACC_EBITDA, moFieldFkAccountEbitdaTypeId, null);
    }

    private void actionFkFiscalAccountIncId() {
        miClient.pickOption(SDataConstants.FINS_FISCAL_ACC, moFieldFkFiscalAccountIncId, null);
    }

    private void actionFkFiscalAccountExpId() {
        miClient.pickOption(SDataConstants.FINS_FISCAL_ACC, moFieldFkFiscalAccountExpId, null);
    }

    /*
     * Focus listener methods:
     */

    private void focusLostName() {
        computeItemKeyAndName();

        if (jtfNameShort.isEnabled() && SLibUtilities.textTrim(jtfNameShort.getText()).length() == 0) {
            actionCopyName();
        }
    }

    private void focusLostNameShort() {
        computeItemKeyAndName();
    }

    private void focusLostPresentation() {
        computeItemKeyAndName();

        if (jtfPresentationShort.isEnabled() && SLibUtilities.textTrim(jtfPresentationShort.getText()).length() == 0) {
            actionCopyPresentation();
        }
    }

    private void focusLostPresentationShort() {
        computeItemKeyAndName();
    }

    private void focusLostCode() {
        computeItemKeyAndName();
    }

    /*
     * Item listener methods:
     */

    private void itemStateIsInventoriable() {
        if (jckIsInventoriable.isSelected()) {
            jckIsLotApplying.setEnabled(moItemGeneric.getIsLotApplying());
            jcbFkUnitAlternativeTypeId.setEnabled(true);
        }
        else {
            jckIsLotApplying.setEnabled(false);
            jcbFkUnitAlternativeTypeId.setEnabled(false);
        }

        if (!jckIsLotApplying.isEnabled()) {
            moFieldIsLotApplying.setFieldValue(false);
        }
        if (!jcbFkUnitAlternativeTypeId.isEnabled()) {
            moFieldFkUnitAlternativeTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_UNIT_NA });
        }
        if (!jcbFkLevelTypeId.isEnabled()) {
            moFieldFkLevelTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_LEV_NA });
        }

        itemStateFkUnitAlternativeTypeId();
    }

    private void itemStateFkItemGenericId() {
        readItemGenericParams();
        computeItemKeyAndName();
    }

    private void itemStateFkItemLineId_n() {
        readItemLineParams();
        computeItemKeyAndName();
    }

    private void itemStateFkBrandId() {
        computeItemKeyAndName();
    }

    private void itemStateFkManufacturerId() {
        computeItemKeyAndName();
    }

    private void itemStateFkElementId() {
        computeItemKeyAndName();
    }

    private void itemStateFkVariety01Id() {
        computeItemKeyAndName();
    }

    private void itemStateFkVariety02Id() {
        computeItemKeyAndName();
    }

    private void itemStateFkVariety03Id() {
        computeItemKeyAndName();
    }

    private void itemStateFkUnitAlternativeTypeId() {
        String text = "";
        SDataUnit unit = null;
        SDataUnitType unitType = null;

        if (jcbFkUnitAlternativeTypeId.getSelectedIndex() <= 0 || moFieldFkUnitAlternativeTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.ITMU_TP_UNIT_NA) {
            jtfUnitAlternativeBaseEquivalence.setEnabled(false);
            moFieldUnitAlternativeBaseEquivalence.setFieldValue(0d);
        }
        else {
            jtfUnitAlternativeBaseEquivalence.setEnabled(true);

            if (jcbFkUnitId.getSelectedIndex() > 0) {
                unit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, moFieldFkUnitId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                text = unit.getSymbol();
            }

            unitType = (SDataUnitType) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_TP_UNIT, moFieldFkUnitAlternativeTypeId.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
            text = (text.length() == 0 ? "" : text + " / ") + unitType.getUnitBase();
        }

        jtfUnitAlternativeBaseRo.setText(text);
    }

    /*
     * Public methods:
     */

    public void publicActionAddItemBarcode() {
        if (jTabbedPane.getSelectedIndex() == 1) {
            if (jbAddItemBarcode.isEnabled()) {
                actionAddItemBarcode();
            }
        }
    }

    public void publicActionModifyItemBarcode() {
        if (jTabbedPane.getSelectedIndex() == 1) {
            if (jbModifyItemBarcode.isEnabled()) {
                actionModifyItemBarcode();
            }
        }
    }

    public void publicActionAddItemForeignLanguage() {
        if (jTabbedPane.getSelectedIndex() == 1) {
            if (jbAddItemForeignLanguage.isEnabled()) {
                actionAddItemForeignLanguage();
            }
        }
    }

    public void publicActionModifyItemForeignLanguage() {
        if (jTabbedPane.getSelectedIndex() == 1) {
            if (jbModifyItemForeignLanguage.isEnabled()) {
                actionModifyItemForeignLanguage();
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton jbAddItemBarcode;
    private javax.swing.JButton jbAddItemForeignLanguage;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbComputeNewCode;
    private javax.swing.JButton jbCopyName;
    private javax.swing.JButton jbCopyPresentation;
    private javax.swing.JButton jbEditItemGeneric;
    private javax.swing.JButton jbEditUnit;
    private javax.swing.JButton jbFkAccountEbitdaTypeId;
    private javax.swing.JButton jbFkAdministrativeConceptTypeId;
    private javax.swing.JButton jbFkDefaultItemRefId_n;
    private javax.swing.JButton jbFkFiscalAccountExpId;
    private javax.swing.JButton jbFkFiscalAccountIncId;
    private javax.swing.JButton jbFkItemGenericId;
    private javax.swing.JButton jbFkItemLineId_n;
    private javax.swing.JButton jbFkItemPackageId_n;
    private javax.swing.JButton jbFkTaxableConceptTypeId;
    private javax.swing.JButton jbModifyItemBarcode;
    private javax.swing.JButton jbModifyItemForeignLanguage;
    private javax.swing.JButton jbOk;
    private javax.swing.ButtonGroup jbgStatus;
    private javax.swing.JComboBox jcbFkAccountEbitdaTypeId;
    private javax.swing.JComboBox jcbFkAdministrativeConceptTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBrandId;
    private javax.swing.JComboBox jcbFkDefaultItemRefId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkElementId;
    private javax.swing.JComboBox jcbFkFiscalAccountExpId;
    private javax.swing.JComboBox jcbFkFiscalAccountIncId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemGenericId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemLineId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemPackageId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkLevelTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkManufacturerId;
    private javax.swing.JComboBox jcbFkTaxableConceptTypeId;
    private javax.swing.JComboBox jcbFkUnitAlternativeTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitNetContentId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitNetContentUnitaryId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitUnitsContainedId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitUnitsVirtualId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkVariety01Id;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkVariety02Id;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkVariety03Id;
    private javax.swing.JCheckBox jckIsBulk;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsFreeCommissions;
    private javax.swing.JCheckBox jckIsFreeDiscount;
    private javax.swing.JCheckBox jckIsFreeDiscountDoc;
    private javax.swing.JCheckBox jckIsFreeDiscountEntry;
    private javax.swing.JCheckBox jckIsFreeDiscountUnitary;
    private javax.swing.JCheckBox jckIsFreePrice;
    private javax.swing.JCheckBox jckIsInventoriable;
    private javax.swing.JCheckBox jckIsLengthVariable;
    private javax.swing.JCheckBox jckIsLotApplying;
    private javax.swing.JCheckBox jckIsMassVariable;
    private javax.swing.JCheckBox jckIsNetContentVariable;
    private javax.swing.JCheckBox jckIsPrepayment;
    private javax.swing.JCheckBox jckIsReference;
    private javax.swing.JCheckBox jckIsSurfaceVariable;
    private javax.swing.JCheckBox jckIsVolumeVariable;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlFkAccountEbitdaTypeId;
    private javax.swing.JLabel jlFkAdministrativeConceptTypeId;
    private javax.swing.JLabel jlFkBrandId;
    private javax.swing.JLabel jlFkCfdProdServId_n;
    private javax.swing.JLabel jlFkDefaultItemRefId_n;
    private javax.swing.JLabel jlFkElementId;
    private javax.swing.JLabel jlFkFiscalAccountExpId;
    private javax.swing.JLabel jlFkFiscalAccountIncId;
    private javax.swing.JLabel jlFkItemGenericId;
    private javax.swing.JLabel jlFkItemLineId_n;
    private javax.swing.JLabel jlFkItemPackageId_n;
    private javax.swing.JLabel jlFkLevelTypeId;
    private javax.swing.JLabel jlFkManufacturerId;
    private javax.swing.JLabel jlFkTaxableConceptTypeId;
    private javax.swing.JLabel jlFkUnitAlternativeTypeId;
    private javax.swing.JLabel jlFkUnitId;
    private javax.swing.JLabel jlFkUnitNetContentId;
    private javax.swing.JLabel jlFkUnitNetContentUnitaryId;
    private javax.swing.JLabel jlFkUnitUnitsContainedId;
    private javax.swing.JLabel jlFkUnitUnitsVirtualId;
    private javax.swing.JLabel jlFkVariety01Id;
    private javax.swing.JLabel jlFkVariety02Id;
    private javax.swing.JLabel jlFkVariety03Id;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlItemKey;
    private javax.swing.JLabel jlItemShort;
    private javax.swing.JLabel jlItemStatus;
    private javax.swing.JLabel jlLength;
    private javax.swing.JLabel jlLengthUnitary;
    private javax.swing.JLabel jlMass;
    private javax.swing.JLabel jlMassUnitary;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlNameShort;
    private javax.swing.JLabel jlPresent;
    private javax.swing.JLabel jlPresentShort;
    private javax.swing.JLabel jlProductionCost;
    private javax.swing.JLabel jlProductionTime;
    private javax.swing.JLabel jlSurface;
    private javax.swing.JLabel jlSurfaceUnitary;
    private javax.swing.JLabel jlSurplusPercentage;
    private javax.swing.JLabel jlTariff;
    private javax.swing.JLabel jlUnitBaseEquivalence;
    private javax.swing.JLabel jlVolume;
    private javax.swing.JLabel jlVolumeUnitary;
    private javax.swing.JLabel jlWeightDelivery;
    private javax.swing.JLabel jlWeightGross;
    private javax.swing.JPanel jpCommand;
    private javax.swing.JPanel jpConfig;
    private javax.swing.JPanel jpConfig1;
    private javax.swing.JPanel jpConfig2;
    private javax.swing.JPanel jpConfig2Barcode;
    private javax.swing.JPanel jpConfig2Language;
    private javax.swing.JPanel jpItemBarcode;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JRadioButton jrbStatusActive;
    private javax.swing.JRadioButton jrbStatusInactive;
    private javax.swing.JRadioButton jrbStatusRestricted;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfItemKey;
    private javax.swing.JTextField jtfItemNameRo;
    private javax.swing.JTextField jtfItemNameShortRo;
    private javax.swing.JTextField jtfLength;
    private javax.swing.JTextField jtfLengthUnitary;
    private javax.swing.JTextField jtfMass;
    private javax.swing.JTextField jtfMassUnitary;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfNameShort;
    private javax.swing.JTextField jtfNetContent;
    private javax.swing.JTextField jtfNetContentUnitary;
    private javax.swing.JTextField jtfPkItemId_Ro;
    private javax.swing.JTextField jtfPresentation;
    private javax.swing.JTextField jtfPresentationShort;
    private javax.swing.JTextField jtfProductionCost;
    private javax.swing.JTextField jtfProductionCostUnit;
    private javax.swing.JTextField jtfProductionTime;
    private javax.swing.JTextField jtfProductionTimeUnitRo;
    private javax.swing.JTextField jtfSurface;
    private javax.swing.JTextField jtfSurfaceUnitary;
    private javax.swing.JTextField jtfSurplusPercentage;
    private javax.swing.JTextField jtfUnitAlternativeBaseEquivalence;
    private javax.swing.JTextField jtfUnitAlternativeBaseRo;
    private javax.swing.JTextField jtfUnitsContained;
    private javax.swing.JTextField jtfUnitsPackage;
    private javax.swing.JTextField jtfUnitsVirtual;
    private javax.swing.JTextField jtfVolume;
    private javax.swing.JTextField jtfVolumeUnitary;
    private javax.swing.JTextField jtfWeightDelivery;
    private javax.swing.JTextField jtfWeightGross;
    private sa.lib.gui.bean.SBeanFieldKey moKeyCfdProdServId_n;
    private sa.lib.gui.bean.SBeanFieldText moTextTariff;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        moItem = null;
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        mbWasInventoriable = false;
        mbWasLotApplying = false;

        moItem = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        readItemGenericParams();

        jtfPkItemId_Ro.setText("");
        jrbStatusActive.setSelected(true);
        moItemForeignLanguagePane.createTable(null);
        moItemForeignLanguagePane.clearTableRows();
        moItemBarcodePane.createTable(null);
        moItemBarcodePane.clearTableRows();

        moFieldFkUnitAlternativeTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_UNIT_NA });
        moFieldFkLevelTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_LEV_NA });
        moFieldFkAdministrativeConceptTypeId.setFieldValue(new int[] { SDataConstantsSys.NA });
        moFieldFkTaxableConceptTypeId.setFieldValue(new int[] { SDataConstantsSys.NA });
        moFieldFkAccountEbitdaTypeId.setFieldValue(new int[] { SDataConstantsSys.NA });
        moFieldFkFiscalAccountIncId.setFieldValue(new int[] { SModSysConsts.FINS_FISCAL_ACC_NA });
        moFieldFkFiscalAccountExpId.setFieldValue(new int[] { SModSysConsts.FINS_FISCAL_ACC_NA });

        jckIsDeleted.setEnabled(false);
        jbEditItemGeneric.setEnabled(false);
        jcbFkItemGenericId.setEnabled(true);
        jbFkItemGenericId.setEnabled(true);
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;
        
        SFormUtilities.populateComboBox(miClient, jcbFkItemGenericId, SDataConstants.ITMU_IGEN);
        SFormUtilities.populateComboBox(miClient, jcbFkBrandId, SDataConstants.ITMU_BRD);
        SFormUtilities.populateComboBox(miClient, jcbFkManufacturerId, SDataConstants.ITMU_MFR);
        SFormUtilities.populateComboBox(miClient, jcbFkElementId, SDataConstants.ITMU_EMT);
        SFormUtilities.populateComboBox(miClient, jcbFkItemPackageId_n, SDataConstants.ITMU_ITEM);
        SFormUtilities.populateComboBox(miClient, jcbFkUnitAlternativeTypeId, SDataConstants.ITMU_TP_UNIT);
        SFormUtilities.populateComboBox(miClient, jcbFkLevelTypeId, SDataConstants.ITMU_TP_LEV);
        SFormUtilities.populateComboBox(miClient, jcbFkDefaultItemRefId_n, SDataConstants.ITMU_ITEM);
        SFormUtilities.populateComboBox(miClient, jcbFkAdministrativeConceptTypeId, SDataConstants.FINU_TP_ADM_CPT);
        SFormUtilities.populateComboBox(miClient, jcbFkTaxableConceptTypeId, SDataConstants.FINU_TP_TAX_CPT);
        SFormUtilities.populateComboBox(miClient, jcbFkAccountEbitdaTypeId, SDataConstants.FINU_TP_ACC_EBITDA);
        SFormUtilities.populateComboBox(miClient, jcbFkFiscalAccountIncId, SDataConstants.FINS_FISCAL_ACC);
        SFormUtilities.populateComboBox(miClient, jcbFkFiscalAccountExpId, SDataConstants.FINS_FISCAL_ACC);
        miClient.getSession().populateCatalogue(moKeyCfdProdServId_n, SModConsts.ITMS_CFD_PROD_SERV, SLibConsts.UNDEFINED, null);
        
        mbResetingForm = false;
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }

        try {
            if (!validation.getIsError()) {
                Object[] params = null;

                params = new Object[] { moItem == null ? 0 : moItem.getPkItemId(), jtfItemNameRo.getText() };
                if (SDataUtilities.callProcedureVal(miClient, SProcConstants.ITMU_ITEM_VAL, params, SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                    if (miClient.showMsgBoxConfirm("El valor del campo '" + jlItem.getText() + "' ya existe, ¿desea conservalo? ") == JOptionPane.NO_OPTION) {
                        validation.setTabbedPaneIndex(0);
                        validation.setComponent(jtfItemNameRo);
                        validation.setIsError(true);
                    }
                }

                if (!validation.getIsError()) {
                    params = new Object[] { moItem == null ? 0 : moItem.getPkItemId(), moFieldItemKey.getString() };
                    if (!moFieldItemKey.getString().isEmpty() && SDataUtilities.callProcedureVal(miClient, SProcConstants.ITMU_ITEM_KEY_VAL, params, SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                        if (miClient.showMsgBoxConfirm("El valor del campo '" + jlItemKey.getText() + "' ya existe, ¿desea conservalo? ") == JOptionPane.NO_OPTION) {
                            validation.setTabbedPaneIndex(0);
                            validation.setComponent(jtfItemKey);
                            validation.setIsError(true);
                        }
                    }

                    if (!validation.getIsError()) {
                        if (moFieldUnitsPackage.getDouble() != 0d && jcbFkItemPackageId_n.getSelectedIndex() <= 0) {
                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkItemPackageId_n.getText() + "'.");
                            validation.setComponent(jcbFkItemPackageId_n);
                        }
                        else if (moFieldUnitsPackage.getDouble() == 0d && jcbFkItemPackageId_n.getSelectedIndex() > 0) {
                            validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkItemPackageId_n.getText() + "'.");
                            validation.setComponent(jtfUnitsPackage);
                        }
                        else if (moItem != null && moItem.getPkItemId() == moFieldFkItemPackageId_n.getKeyAsIntArray()[0]) {
                            validation.setMessage("El valor del campo '" + jlFkItemPackageId_n.getText() + "' debe ser distinto.");
                            validation.setComponent(jcbFkItemPackageId_n);
                        }
                        else if (moFieldFkUnitId.getKeyAsIntArray()[0] == SDataConstants.UNDEFINED) {
                            validation.setMessage("Se debe seleccionar una opción para el campo '" + jlFkUnitId.getText() + "'.");
                            jcbFkUnitId.setEnabled(true);
                            jbEditUnit.setEnabled(false);
                            validation.setComponent(jcbFkUnitId);
                        }
                        else if (moFieldFkUnitId.getKeyAsIntArray()[0] == moFieldFkUnitAlternativeTypeId.getKeyAsIntArray()[0]) {
                            validation.setMessage("El valor de los campos '" + jlFkUnitId.getText() + "' y '" + jlFkUnitAlternativeTypeId.getText() + "' debe ser distinto.");
                            validation.setComponent(jcbFkUnitAlternativeTypeId);
                        }
                    }

                    if (!validation.getIsError()) {
                        if (moFieldIsDeleted.getBoolean() && hasStock()) {
                            validation.setMessage("El ítem '" + jtfName.getText() + "' no puede ser eliminado\n debido a que tiene existencias en los inventarios.");
                            validation.setComponent(jckIsDeleted);
                        }
                        else if (mbWasInventoriable && !moFieldIsInventoriable.getBoolean() && hasStock()) {
                            validation.setMessage("Debe seleccionar el campo '" + jckIsInventoriable.getText() + "' para el ítem '" + jtfName.getText() + "'\n debido a que tiene existencias en los inventarios.");
                            validation.setComponent(jckIsInventoriable);
                        }
                        else if (mbWasLotApplying && !moFieldIsLotApplying.getBoolean() && hasStock()) {
                            validation.setMessage("Debe seleccionar el campo '" + jckIsLotApplying.getText() + "' para el ítem '" + jtfName.getText() + "'\n debido a que tiene existencias en los inventarios.");
                            validation.setComponent(jckIsLotApplying);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }

        return validation;
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormStatus() {
        return mnFormStatus;
    }

    @Override
    public int getFormResult() {
        return mnFormResult;
    }

    @Override
    public void setRegistry(erp.lib.data.SDataRegistry registry) {
        int i = 0;
        SDataItemBarcodeRow itemBarcodeRow = null;
        SDataItemForeignLanguageRow itemForeignLanguageDescriptionRow = null;

        mbResetingForm = true;

        moItem = (SDataItem) registry;

        moFieldFkItemGenericId.setFieldValue(new int[] { moItem.getFkItemGenericId() });
        readItemGenericParams();

        moFieldFkItemLineId_n.setFieldValue(new int[] { moItem.getFkItemLineId_n() });
        readItemLineParams();

        moFieldName.setFieldValue(moItem.getName());
        moFieldNameShort.setFieldValue(moItem.getNameShort());
        moFieldPresentation.setFieldValue(moItem.getPresentation());
        moFieldPresentationShort.setFieldValue(moItem.getPresentationShort());
        moFieldCode.setFieldValue(moItem.getCode());
        moFieldItemKey.setFieldValue(moItem.getKey());
        jtfItemNameRo.setText(moItem.getItem());
        jtfItemNameShortRo.setText(moItem.getItemShort());
        moFieldIsDeleted.setFieldValue(moItem.getIsDeleted());
        moFieldIsBulk.setFieldValue(moItem.getIsBulk());
        moFieldIsInventoriable.setFieldValue(mbWasInventoriable = moItem.getIsInventoriable());
        moFieldIsLotApplying.setFieldValue(mbWasLotApplying = moItem.getIsLotApplying());
        moFieldFkBrandId.setFieldValue(new int[] { moItem.getFkBrandId() });
        moFieldFkManufacturerId.setFieldValue(new int[] { moItem.getFkManufacturerId() });
        moFieldFkElementId.setFieldValue(new int[] { moItem.getFkElementId() });
        moFieldFkVariety01Id.setFieldValue(new int[] { moItem.getFkVariety01Id() });
        moFieldFkVariety02Id.setFieldValue(new int[] { moItem.getFkVariety02Id() });
        moFieldFkVariety03Id.setFieldValue(new int[] { moItem.getFkVariety03Id() });
        moFieldFkUnitId.setFieldValue(new int[] { moItem.getFkUnitId() });
        moFieldUnitsContained.setFieldValue(moItem.getUnitsContained());
        moFieldFkUnitUnitsContainedId.setFieldValue(new int[] { moItem.getFkUnitUnitsContainedId() });
        moFieldUnitsVirtual.setFieldValue(moItem.getUnitsVirtual());
        moFieldFkUnitUnitsVirtualId.setFieldValue(new int[] { moItem.getFkUnitUnitsVirtualId() });
        moFieldNetContent.setFieldValue(moItem.getNetContent());
        moFieldFkUnitNetContentId.setFieldValue(new int[] { moItem.getFkUnitNetContentId() });
        moFieldIsNetContentVariable.setFieldValue(moItem.getIsNetContentVariable());
        moFieldNetContentUnitary.setFieldValue(moItem.getNetContentUnitary());
        moFieldFkUnitNetContentUnitaryId.setFieldValue(new int[] { moItem.getFkUnitNetContentUnitaryId() });
        moFieldUnitsPackage.setFieldValue(moItem.getUnitsPackage());
        moFieldFkItemPackageId_n.setFieldValue(new int[] { moItem.getFkItemPackageId_n() });
        moFieldProductionTime.setFieldValue(moItem.getProductionTime());
        moFieldProductionCost.setFieldValue(moItem.getProductionCost());
        moFieldWeightGross.setFieldValue(moItem.getWeightGross());
        moFieldWeightDelivery.setFieldValue(moItem.getWeightDelivery());
        moFieldLength.setFieldValue(moItem.getLength());
        moFieldLengthUnitary.setFieldValue(moItem.getLengthUnitary());
        moFieldIsLengthVariable.setFieldValue(moItem.getIsLengthVariable());
        moFieldSurface.setFieldValue(moItem.getSurface());
        moFieldSurfaceUnitary.setFieldValue(moItem.getSurfaceUnitary());
        moFieldIsSurfaceVariable.setFieldValue(moItem.getIsSurfaceVariable());
        moFieldVolume.setFieldValue(moItem.getVolume());
        moFieldVolumeUnitary.setFieldValue(moItem.getVolumeUnitary());
        moFieldIsVolumeVariable.setFieldValue(moItem.getIsVolumeVariable());
        moFieldMass.setFieldValue(moItem.getMass());
        moFieldMassUnitary.setFieldValue(moItem.getMassUnitary());
        moFieldIsMassVariable.setFieldValue(moItem.getIsMassVariable());
        moFieldFkUnitAlternativeTypeId.setFieldValue(new int[] { moItem.getFkUnitAlternativeTypeId() });
        moFieldFkLevelTypeId.setFieldValue(new int[] { moItem.getFkLevelTypeId() });
        moFieldUnitAlternativeBaseEquivalence.setFieldValue(moItem.getUnitAlternativeBaseEquivalence());

        moFieldIsFreeDiscountUnitary.setFieldValue(moItem.getIsFreeDiscountUnitary());
        moFieldIsFreeDiscountEntry.setFieldValue(moItem.getIsFreeDiscountEntry());
        moFieldIsFreeDiscountDoc.setFieldValue(moItem.getIsFreeDiscountDoc());
        moFieldIsFreePrice.setFieldValue(moItem.getIsFreePrice());
        moFieldIsFreeDiscount.setFieldValue(moItem.getIsFreeDiscount());
        moFieldIsFreeCommissions.setFieldValue(moItem.getIsFreeCommissions());
        moFieldSurplusPercentage.setFieldValue(moItem.getSurplusPercentage());
        moFieldIsReference.setFieldValue(moItem.getIsReference());
        moFieldIsPrepayment.setFieldValue(moItem.getIsPrepayment());
        moFieldFkDefaultItemRefId_n.setFieldValue(new int[] { moItem.getFkDefaultItemRefId_n() });
        moFieldFkAdministrativeConceptTypeId.setFieldValue(new int[] { moItem.getFkAdministrativeConceptTypeId() });
        moFieldFkTaxableConceptTypeId.setFieldValue(new int[] { moItem.getFkTaxableConceptTypeId() });
        moFieldFkAccountEbitdaTypeId.setFieldValue(new int[] { moItem.getFkAccountEbitdaTypeId()});
        moFieldFkFiscalAccountIncId.setFieldValue(new int[] { moItem.getFkFiscalAccountIncId() });
        moFieldFkFiscalAccountExpId.setFieldValue(new int[] { moItem.getFkFiscalAccountExpId() });
        moKeyCfdProdServId_n.setValue(new int[] { moItem.getFkCfdProdServId_n() });
        moTextTariff.setValue(moItem.getTariff());
        jtfPkItemId_Ro.setText("" + moItem.getPkItemId());

        jckIsDeleted.setEnabled(true);

        jcbFkItemGenericId.setEnabled(false);
        jbFkItemGenericId.setEnabled(false);
        jcbFkItemLineId_n.setEnabled(false);
        jbFkItemLineId_n.setEnabled(false);
        jbEditItemGeneric.setEnabled(true);

        jcbFkUnitId.setEnabled(false);
        jbEditUnit.setEnabled(true);

        itemStateIsInventoriable();
        computeItemKeyAndName();
        
        switch (moItem.getFkItemStatusId()) {
            case SModSysConsts.ITMS_ST_ITEM_ACT:
                jrbStatusActive.setSelected(true);
                break;
            case SModSysConsts.ITMS_ST_ITEM_RES:
                jrbStatusRestricted.setSelected(true);
                break;
            case SModSysConsts.ITMS_ST_ITEM_INA:
                jrbStatusInactive.setSelected(true);
                break;
            default:
        }

        // Read the item foreign language descriptions:

        for (i = 0; i < moItem.getDbmsItemForeignLanguageDescriptions().size(); i++) {
            itemForeignLanguageDescriptionRow = new SDataItemForeignLanguageRow(moItem.getDbmsItemForeignLanguageDescriptions().get(i));
            moItemForeignLanguagePane.addTableRow(itemForeignLanguageDescriptionRow);
        }
        moItemForeignLanguagePane.renderTableRows();
        moItemForeignLanguagePane.setTableRowSelection(0);

        // Read the item barcodes:

        for (i = 0; i < moItem.getDbmsItemBarcodes().size(); i++) {
            itemBarcodeRow = new SDataItemBarcodeRow(moItem.getDbmsItemBarcodes().get(i));
            moItemBarcodePane.addTableRow(itemBarcodeRow);
        }
        moItemBarcodePane.renderTableRows();
        moItemBarcodePane.setTableRowSelection(0);

        mbResetingForm = false;
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int i = 0;

        if (moItem == null) {
            moItem = new SDataItem();
            moItem.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moItem.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moItem.setFkItemGenericId(moFieldFkItemGenericId.getKeyAsIntArray()[0]);
        moItem.setFkItemLineId_n(!jcbFkItemLineId_n.isEnabled() ? SLibConstants.UNDEFINED : moFieldFkItemLineId_n.getKeyAsIntArray()[0]);
        moItem.setName(moFieldName.getString());
        moItem.setNameShort(moFieldNameShort.getString());
        moItem.setPresentation(moFieldPresentation.getString());
        moItem.setPresentationShort(moFieldPresentationShort.getString());
        moItem.setCode(moFieldCode.getString());
        moItem.setKey(moFieldItemKey.getString());
        moItem.setItem(jtfItemNameRo.getText());
        moItem.setItemShort(jtfItemNameShortRo.getText());
        moItem.setIsDeleted(moFieldIsDeleted.getBoolean());
        moItem.setIsBulk(moFieldIsBulk.getBoolean());
        moItem.setIsInventoriable(moFieldIsInventoriable.getBoolean());
        moItem.setIsLotApplying(moFieldIsLotApplying.getBoolean());
        moItem.setFkBrandId(moFieldFkBrandId.getKeyAsIntArray()[0]);
        moItem.setFkManufacturerId(moFieldFkManufacturerId.getKeyAsIntArray()[0]);
        moItem.setFkElementId(moFieldFkElementId.getKeyAsIntArray()[0]);
        moItem.setFkVariety01Id(jcbFkVariety01Id.getSelectedIndex() <= 0 ? 1 : moFieldFkVariety01Id.getKeyAsIntArray()[0]);
        moItem.setFkVariety02Id(jcbFkVariety02Id.getSelectedIndex() <= 0 ? 1 : moFieldFkVariety02Id.getKeyAsIntArray()[0]);
        moItem.setFkVariety03Id(jcbFkVariety03Id.getSelectedIndex() <= 0 ? 1 : moFieldFkVariety03Id.getKeyAsIntArray()[0]);
        moItem.setFkUnitId(moFieldFkUnitId.getKeyAsIntArray()[0]);
        moItem.setUnitsContained(moFieldUnitsContained.getDouble());
        moItem.setFkUnitUnitsContainedId(moFieldFkUnitUnitsContainedId.getKeyAsIntArray()[0]);
        moItem.setUnitsVirtual(moFieldUnitsVirtual.getDouble());
        moItem.setFkUnitUnitsVirtualId(moFieldFkUnitUnitsVirtualId.getKeyAsIntArray()[0]);
        moItem.setNetContent(moFieldNetContent.getDouble());
        moItem.setFkUnitNetContentId(moFieldFkUnitNetContentId.getKeyAsIntArray()[0]);
        moItem.setIsNetContentVariable(moFieldIsNetContentVariable.getBoolean());
        moItem.setNetContentUnitary(moFieldNetContentUnitary.getDouble());
        moItem.setFkUnitNetContentUnitaryId(moFieldFkUnitNetContentUnitaryId.getKeyAsIntArray()[0]);
        moItem.setUnitsPackage(moFieldUnitsPackage.getDouble());
        moItem.setFkItemPackageId_n(jcbFkItemPackageId_n.getSelectedIndex() <= 0 ? 0 : moFieldFkItemPackageId_n.getKeyAsIntArray()[0]);
        moItem.setProductionTime(moFieldProductionTime.getDouble());
        moItem.setProductionCost(moFieldProductionCost.getDouble());
        moItem.setWeightGross(moFieldWeightGross.getDouble());
        moItem.setWeightDelivery(moFieldWeightDelivery.getDouble());
        moItem.setLength(moFieldLength.getDouble());
        moItem.setLengthUnitary(moFieldLengthUnitary.getDouble());
        moItem.setIsLengthVariable(moFieldIsLengthVariable.getBoolean());
        moItem.setSurface(moFieldSurface.getDouble());
        moItem.setSurfaceUnitary(moFieldSurfaceUnitary.getDouble());
        moItem.setIsSurfaceVariable(moFieldIsSurfaceVariable.getBoolean());
        moItem.setVolume(moFieldVolume.getDouble());
        moItem.setVolumeUnitary(moFieldVolumeUnitary.getDouble());
        moItem.setIsVolumeVariable(moFieldIsVolumeVariable.getBoolean());
        moItem.setMass(moFieldMass.getDouble());
        moItem.setMassUnitary(moFieldMassUnitary.getDouble());
        moItem.setIsMassVariable(moFieldIsMassVariable.getBoolean());
        moItem.setFkUnitAlternativeTypeId(moFieldFkUnitAlternativeTypeId.getKeyAsIntArray()[0]);
        moItem.setFkLevelTypeId(moFieldFkLevelTypeId.getKeyAsIntArray()[0]);
        moItem.setUnitAlternativeBaseEquivalence(moFieldUnitAlternativeBaseEquivalence.getDouble());

        moItem.setIsFreeDiscountUnitary(moFieldIsFreeDiscountUnitary.getBoolean());
        moItem.setIsFreeDiscountEntry(moFieldIsFreeDiscountEntry.getBoolean());
        moItem.setIsFreeDiscountDoc(moFieldIsFreeDiscountDoc.getBoolean());
        moItem.setIsFreePrice(moFieldIsFreePrice.getBoolean());
        moItem.setIsFreeDiscount(moFieldIsFreeDiscount.getBoolean());
        moItem.setIsFreeCommissions(moFieldIsFreeCommissions.getBoolean());
        moItem.setSurplusPercentage(moFieldSurplusPercentage.getDouble());
        moItem.setIsReference(moFieldIsReference.getBoolean());
        moItem.setIsPrepayment(moFieldIsPrepayment.getBoolean());
        moItem.setFkDefaultItemRefId_n(jcbFkDefaultItemRefId_n.getSelectedIndex() <= 0 ? 0 : moFieldFkDefaultItemRefId_n.getKeyAsIntArray()[0]);
        moItem.setFkAdministrativeConceptTypeId(moFieldFkAdministrativeConceptTypeId.getKeyAsIntArray()[0]);
        moItem.setFkTaxableConceptTypeId(moFieldFkTaxableConceptTypeId.getKeyAsIntArray()[0]);
        moItem.setFkAccountEbitdaTypeId(moFieldFkAccountEbitdaTypeId.getKeyAsIntArray()[0]);
        moItem.setFkFiscalAccountIncId(moFieldFkFiscalAccountIncId.getKeyAsIntArray()[0]);
        moItem.setFkFiscalAccountExpId(moFieldFkFiscalAccountExpId.getKeyAsIntArray()[0]);
        moItem.setFkCfdProdServId_n(moKeyCfdProdServId_n.getSelectedIndex() <= 0 ? 0 : moKeyCfdProdServId_n.getValue()[0]);
        moItem.setTariff(moTextTariff.getValue());
        
        if (jrbStatusActive.isSelected()) {
            moItem.setFkItemStatusId(SModSysConsts.ITMS_ST_ITEM_ACT);
        }
        else if (jrbStatusRestricted.isSelected()) {
            moItem.setFkItemStatusId(SModSysConsts.ITMS_ST_ITEM_RES);
        }
        else {
            moItem.setFkItemStatusId(SModSysConsts.ITMS_ST_ITEM_INA);
        }

        // Save item foreign language descriptions:

        moItem.getDbmsItemForeignLanguageDescriptions().clear();
        for (i = 0; i < moItemForeignLanguagePane.getTable().getRowCount(); i++) {
            moItem.getDbmsItemForeignLanguageDescriptions().add(
                    (erp.mitm.data.SDataItemForeignLanguage) moItemForeignLanguagePane.getTableRow(i).getData());
        }

        // Save the item barcodes:

        moItem.getDbmsItemBarcodes().clear();
        for (i = 0; i < moItemBarcodePane.getTable().getRowCount(); i++) {
            moItem.getDbmsItemBarcodes().add((erp.mitm.data.SDataItemBarcode) moItemBarcodePane.getTableRow(i).getData());
        }

        return moItem;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.lang.Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbCopyName) {
                actionCopyName();
                jtfNameShort.requestFocus();
            }
            else if (button == jbCopyPresentation) {
                actionCopyPresentation();
                jtfPresentationShort.requestFocus();
            }
            else if (button == jbComputeNewCode) {
                actionComputeNewCode();
            }
            else if (button == jbEditItemGeneric) {
                actionEditItemGeneric();
            }
            else if (button == jbEditUnit) {
                actionEditUnit();
            }
            else if (button == jbAddItemForeignLanguage) {
                actionAddItemForeignLanguage();
            }
            else if (button == jbModifyItemForeignLanguage) {
                actionModifyItemForeignLanguage();
            }
            else if (button == jbAddItemBarcode) {
                actionAddItemBarcode();
            }
            else if (button == jbModifyItemBarcode) {
                actionModifyItemBarcode();
            }
            else if (button == jbFkItemGenericId) {
                actionFkItemGenericId();
            }
            else if (button == jbFkItemLineId_n) {
                actionFkItemLineId_n();
            }
            else if (button == jbFkItemPackageId_n) {
                actionFkItemPackageId_n();
            }
            else if (button == jbFkDefaultItemRefId_n) {
                actionFkDefaultItemRefId_n();
            }
            else if (button == jbFkAdministrativeConceptTypeId) {
                actionFkAdministrativeConceptTypeId();
            }
            else if (button == jbFkTaxableConceptTypeId) {
                actionFkTaxableConceptTypeId();
            }
            else if (button == jbFkAccountEbitdaTypeId) {
                actionFkAccountEbitdaTypeId();
            }
            else if (button == jbFkFiscalAccountIncId) {
                actionFkFiscalAccountIncId();
            }
            else if (button == jbFkFiscalAccountExpId) {
                actionFkFiscalAccountExpId();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();
            if (textField == jtfName) {
                focusLostName();
            }
            else if (textField == jtfNameShort) {
                focusLostNameShort();
            }
            else if (textField == jtfPresentation) {
                focusLostPresentation();
            }
            else if (textField == jtfPresentationShort) {
                focusLostPresentationShort();
            }
            else if (textField == jtfCode) {
                focusLostCode();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbResetingForm) {
            if (e.getSource() instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) e.getSource();

                if (checkBox == jckIsInventoriable) {
                    itemStateIsInventoriable();
                }
            }
            else if (e.getSource() instanceof JComboBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox comboBox = (JComboBox) e.getSource();

                    if (comboBox == jcbFkItemGenericId) {
                        itemStateFkItemGenericId();
                    }
                    else if (comboBox == jcbFkItemLineId_n) {
                        itemStateFkItemLineId_n();
                    }
                    else if (comboBox == jcbFkBrandId) {
                        itemStateFkBrandId();
                    }
                    else if (comboBox == jcbFkManufacturerId) {
                        itemStateFkManufacturerId();
                    }
                    else if (comboBox == jcbFkElementId) {
                        itemStateFkElementId();
                    }
                    else if (comboBox == jcbFkVariety01Id) {
                        itemStateFkVariety01Id();
                    }
                    else if (comboBox == jcbFkVariety02Id) {
                        itemStateFkVariety02Id();
                    }
                    else if (comboBox == jcbFkVariety03Id) {
                        itemStateFkVariety03Id();
                    }
                    else if (comboBox == jcbFkUnitId) {
                        itemStateFkUnitAlternativeTypeId();
                    }
                    else if (comboBox == jcbFkUnitAlternativeTypeId) {
                        itemStateFkUnitAlternativeTypeId();
                    }
                }
            }
        }
    }
}
