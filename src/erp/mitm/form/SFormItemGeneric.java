/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormItemGeneric.java
 *
 * Created on 26/08/2009, 04:54:35 PM
 */

package erp.mitm.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComboBoxGroup;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mitm.data.SDataItemGeneric;
import erp.mitm.data.SDataItemGenericBizArea;
import erp.mitm.data.SDataItemGroup;
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

/*
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SFormItemGeneric extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mitm.data.SDataItemGeneric moItemGeneric;
    private erp.lib.form.SFormComboBoxGroup moComboBoxFamilyItem;
    private erp.lib.form.SFormComboBoxGroup moComboBoxCategoryItem;
    private erp.lib.form.SFormField moFieldFkItemCategoryId;
    private erp.lib.form.SFormField moFieldFkItemClassId;
    private erp.lib.form.SFormField moFieldFkItemTypeId;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldFkDbmsItemFamilyId;
    private erp.lib.form.SFormField moFieldFkItemGroupId;
    private erp.lib.form.SFormField moFieldIsItemShortApplying;
    private erp.lib.form.SFormField moFieldItemGeneric;
    private erp.lib.form.SFormField moFieldItemGenericShort;
    private erp.lib.form.SFormField moFieldIsItemKeyApplying;
    private erp.lib.form.SFormField moFieldIsItemKeyAutomatic;
    private erp.lib.form.SFormField moFieldCode;
    private erp.lib.form.SFormField moFieldIsItemLineApplying;
    private erp.lib.form.SFormField moFieldIsItemKeyEditable;
    private erp.lib.form.SFormField moFieldIsItemNameEditable;
    private erp.lib.form.SFormField moFieldIsBulk;
    private erp.lib.form.SFormField moFieldIsInventoriable;
    private erp.lib.form.SFormField moFieldIsLotApplying;
    private erp.lib.form.SFormField moFieldDaysForExpiration;
    private erp.lib.form.SFormField moFieldFkSerialNumberTypeId;
    private erp.lib.form.SFormField moFieldSerialNumber;
    private erp.lib.form.SFormField moFieldSerialNumberFormat;
    private erp.lib.form.SFormField moFieldSurplusPercentage;
    private erp.lib.form.SFormField moFieldFkUnitTypeId;
    private erp.lib.form.SFormField moFieldIsUnitsContainedApplying;
    private erp.lib.form.SFormField moFieldFkUnitUnitsContainedTypeId;
    private erp.lib.form.SFormField moFieldIsUnitsVirtualApplying;
    private erp.lib.form.SFormField moFieldFkUnitUnitsVirtualTypeId;
    private erp.lib.form.SFormField moFieldIsUnitsPackageApplying;
    private erp.lib.form.SFormField moFieldIsNetContentApplying;
    private erp.lib.form.SFormField moFieldFkUnitNetContentTypeId;
    private erp.lib.form.SFormField moFieldIsNetContentUnitaryApplying;
    private erp.lib.form.SFormField moFieldFkUnitNetContentUnitaryTypeId;
    private erp.lib.form.SFormField moFieldIsNetContentVariable;
    private erp.lib.form.SFormField moFieldNamingOrdinaryPosItemGeneric;
    private erp.lib.form.SFormField moFieldNamingOrdinaryPosBrand;
    private erp.lib.form.SFormField moFieldNamingOrdinaryPosManufacturer;
    private erp.lib.form.SFormField moFieldNamingOrdinaryPosName;
    private erp.lib.form.SFormField moFieldNamingOrdinaryPosPresentation;
    private erp.lib.form.SFormField moFieldKeyOrdinaryPosItemGeneric;
    private erp.lib.form.SFormField moFieldKeyOrdinaryPosBrand;
    private erp.lib.form.SFormField moFieldKeyOrdinaryPosManufacturer;
    private erp.lib.form.SFormField moFieldKeyOrdinaryPosCode;
    private erp.lib.form.SFormField moFieldNamingLinePosItemGeneric;
    private erp.lib.form.SFormField moFieldNamingLinePosItemLine;
    private erp.lib.form.SFormField moFieldNamingLinePosBrand;
    private erp.lib.form.SFormField moFieldNamingLinePosManufacturer;
    private erp.lib.form.SFormField moFieldKeyLinePosItemGeneric;
    private erp.lib.form.SFormField moFieldKeyLinePosItemLine;
    private erp.lib.form.SFormField moFieldKeyLinePosBrand;
    private erp.lib.form.SFormField moFieldKeyLinePosManufacturer;
    private erp.lib.form.SFormField moFieldIsLengthApplying;
    private erp.lib.form.SFormField moFieldIsLengthUnitaryApplying;
    private erp.lib.form.SFormField moFieldIsLengthVariable;
    private erp.lib.form.SFormField moFieldIsSurfaceApplying;
    private erp.lib.form.SFormField moFieldIsSurfaceUnitaryApplying;
    private erp.lib.form.SFormField moFieldIsSurfaceVariable;
    private erp.lib.form.SFormField moFieldIsVolumeApplying;
    private erp.lib.form.SFormField moFieldIsVolumeUnitaryApplying;
    private erp.lib.form.SFormField moFieldIsVolumeVariable;
    private erp.lib.form.SFormField moFieldIsMassApplying;
    private erp.lib.form.SFormField moFieldIsMassUnitaryApplying;
    private erp.lib.form.SFormField moFieldIsMassVariable;
    private erp.lib.form.SFormField moFieldIsWeightGrossApplying;
    private erp.lib.form.SFormField moFieldIsWeightDeliveryApplying;
    private erp.lib.form.SFormField moFieldIsFreeDiscountUnitary;
    private erp.lib.form.SFormField moFieldIsFreeDiscountEntry;
    private erp.lib.form.SFormField moFieldIsFreeDiscountDoc;
    private erp.lib.form.SFormField moFieldIsFreePrice;
    private erp.lib.form.SFormField moFieldIsFreeDiscount;
    private erp.lib.form.SFormField moFieldIsFreeCommissions;
    private erp.lib.form.SFormField moFieldBizArea;
    private erp.lib.form.SFormField moFieldBizAreaItemGeneric;
    private erp.lib.form.SFormField moFieldIsItemRefRequired;
    private erp.lib.form.SFormField moFieldFkDefaultItemRefeferenceId_n;
    private erp.lib.form.SFormField moFieldFkAdministrativeConceptTypeId;
    private erp.lib.form.SFormField moFieldFkTaxableConceptTypeId;

    private boolean mbIsLotApplyingBySystem;

    /** Creates new form SFormItemGeneric */
    public SFormItemGeneric(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;
        mnFormType = SDataConstants.ITMU_IGEN;

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

        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel43 = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jTabbedPane = new javax.swing.JTabbedPane();
        jpRegistry = new javax.swing.JPanel();
        jpRegistryNorth = new javax.swing.JPanel();
        jpRegistry1 = new javax.swing.JPanel();
        jpRegistry11 = new javax.swing.JPanel();
        jpRegistry111 = new javax.swing.JPanel();
        jlFkItemCategoryId = new javax.swing.JLabel();
        jcbFkItemCategoryId = new javax.swing.JComboBox<SFormComponentItem>();
        jpRegistry112 = new javax.swing.JPanel();
        jlFkItemClassId = new javax.swing.JLabel();
        jcbFkItemClassId = new javax.swing.JComboBox<SFormComponentItem>();
        jpRegistry113 = new javax.swing.JPanel();
        jlFkItemTypeId = new javax.swing.JLabel();
        jcbFkItemTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jpRegistry12 = new javax.swing.JPanel();
        jpRegistry121 = new javax.swing.JPanel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jpRegistry122 = new javax.swing.JPanel();
        jpRegistry123 = new javax.swing.JPanel();
        jpRegistry2 = new javax.swing.JPanel();
        jpRegistry21 = new javax.swing.JPanel();
        jpRegistry211 = new javax.swing.JPanel();
        jlDbmsFkItemFamilyId = new javax.swing.JLabel();
        jcbFkDbmsItemFamilyId = new javax.swing.JComboBox<SFormComponentItem>();
        jbDbmsFkItemFamilyId = new javax.swing.JButton();
        jpRegistry212 = new javax.swing.JPanel();
        jlFkItemGroupId = new javax.swing.JLabel();
        jcbFkItemGroupId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemGroupId = new javax.swing.JButton();
        jpRegistry213 = new javax.swing.JPanel();
        jckIsItemShortApplying = new javax.swing.JCheckBox();
        jpRegistry214 = new javax.swing.JPanel();
        jlItemGeneric = new javax.swing.JLabel();
        jtfItemGeneric = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jpRegistry215 = new javax.swing.JPanel();
        jlItemGenericShort = new javax.swing.JLabel();
        jtfItemGenericShort = new javax.swing.JTextField();
        jbCopyItemGeneric = new javax.swing.JButton();
        jpRegistry216 = new javax.swing.JPanel();
        jckIsItemKeyApplying = new javax.swing.JCheckBox();
        jpRegistry217 = new javax.swing.JPanel();
        jckIsItemKeyAutomatic = new javax.swing.JCheckBox();
        jpRegistry218 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        jtfCode = new javax.swing.JTextField();
        jpRegistry22 = new javax.swing.JPanel();
        jpRegistry221 = new javax.swing.JPanel();
        jpRegistry2211 = new javax.swing.JPanel();
        jckIsItemLineApplying = new javax.swing.JCheckBox();
        jpRegistry2212 = new javax.swing.JPanel();
        jckIsItemNameEditable = new javax.swing.JCheckBox();
        jpRegistry2213 = new javax.swing.JPanel();
        jckIsItemKeyEditable = new javax.swing.JCheckBox();
        jpRegistry2215 = new javax.swing.JPanel();
        jckIsBulk = new javax.swing.JCheckBox();
        jpRegistry2214 = new javax.swing.JPanel();
        jckIsInventoriable = new javax.swing.JCheckBox();
        jckIsLotApplying = new javax.swing.JCheckBox();
        jlDaysForExpiration = new javax.swing.JLabel();
        jtfDaysForExpiration = new javax.swing.JTextField();
        jpRegistry2216 = new javax.swing.JPanel();
        jlFkSerialNumberTypeId = new javax.swing.JLabel();
        jcbFkSerialNumberTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jpRegistry2217 = new javax.swing.JPanel();
        jlSerialNumber = new javax.swing.JLabel();
        jtfSerialNumber = new javax.swing.JTextField();
        jlSerialNumberExample = new javax.swing.JLabel();
        jpRegistry2218 = new javax.swing.JPanel();
        jlSerialNumberFormat = new javax.swing.JLabel();
        jtfSerialNumberFormat = new javax.swing.JTextField();
        jlSerialNumberFormatExample = new javax.swing.JLabel();
        jpRegistryCenter = new javax.swing.JPanel();
        jpRegistry3 = new javax.swing.JPanel();
        jpRegistry31 = new javax.swing.JPanel();
        jlFkUnitTypeId = new javax.swing.JLabel();
        jcbFkUnitTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsUnitsContainedApplying = new javax.swing.JCheckBox();
        jcbFkUnitUnitsContainedTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsUnitsVirtualApplying = new javax.swing.JCheckBox();
        jcbFkUnitUnitsVirtualTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsUnitsPackageApplying = new javax.swing.JCheckBox();
        jpRegistry32 = new javax.swing.JPanel();
        jckIsNetContentApplying = new javax.swing.JCheckBox();
        jcbFkUnitNetContentTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsNetContentUnitaryApplying = new javax.swing.JCheckBox();
        jcbFkUnitNetContentUnitaryTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jckIsNetContentVariable = new javax.swing.JCheckBox();
        jpProperties = new javax.swing.JPanel();
        jpProperties1 = new javax.swing.JPanel();
        jpProperties11 = new javax.swing.JPanel();
        jpProperties111 = new javax.swing.JPanel();
        jpProperties1111 = new javax.swing.JPanel();
        jpProperties11111 = new javax.swing.JPanel();
        jlNamingOrdinaryPosItemGeneric = new javax.swing.JLabel();
        jtfNamingOrdinaryPosItemGeneric = new javax.swing.JTextField();
        jlNamingOrdinaryPosBrand = new javax.swing.JLabel();
        jtfNamingOrdinaryPosBrand = new javax.swing.JTextField();
        jlNamingOrdinaryPosManufacturer = new javax.swing.JLabel();
        jtfNamingOrdinaryPosManufacturer = new javax.swing.JTextField();
        jlNamingOrdinaryPosName = new javax.swing.JLabel();
        jtfNamingOrdinaryPosName = new javax.swing.JTextField();
        jlNamingOrdinaryPosPresentation = new javax.swing.JLabel();
        jtfNamingOrdinaryPosPresentation = new javax.swing.JTextField();
        jpProperties11112 = new javax.swing.JPanel();
        jlKeyOrdinaryPosItemGeneric = new javax.swing.JLabel();
        jtfKeyOrdinaryPosItemGeneric = new javax.swing.JTextField();
        jlKeyOrdinaryPosBrand = new javax.swing.JLabel();
        jtfKeyOrdinaryPosBrand = new javax.swing.JTextField();
        jlKeyOrdinaryPosManufacturer = new javax.swing.JLabel();
        jtfKeyOrdinaryPosManufacturer = new javax.swing.JTextField();
        jlKeyOrdinaryPosCode = new javax.swing.JLabel();
        jtfKeyOrdinaryPosCode = new javax.swing.JTextField();
        jpProperties1112 = new javax.swing.JPanel();
        jPanel62 = new javax.swing.JPanel();
        jlNamingLinePosItemGeneric = new javax.swing.JLabel();
        jtfNamingLinePosItemGeneric = new javax.swing.JTextField();
        jlNamingLinePosItemLine = new javax.swing.JLabel();
        jtfNamingLinePosItemLine = new javax.swing.JTextField();
        jlNamingLinePosBrand = new javax.swing.JLabel();
        jtfNamingLinePosBrand = new javax.swing.JTextField();
        jlNamingLinePosManufacturer = new javax.swing.JLabel();
        jtfNamingLinePosManufacturer = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jlKeyLinePosItemGeneric = new javax.swing.JLabel();
        jtfKeyLinePosItemGeneric = new javax.swing.JTextField();
        jlKeyLinePosItemLine = new javax.swing.JLabel();
        jtfKeyLinePosItemLine = new javax.swing.JTextField();
        jlKeyLinePosBrand = new javax.swing.JLabel();
        jtfKeyLinePosBrand = new javax.swing.JTextField();
        jlKeyLinePosManufacturer = new javax.swing.JLabel();
        jtfKeyLinePosManufacturer = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jpProperties12 = new javax.swing.JPanel();
        jpProperties121 = new javax.swing.JPanel();
        jpProperties1211 = new javax.swing.JPanel();
        jckIsLengthApplying = new javax.swing.JCheckBox();
        jckIsLengthUnitaryApplying = new javax.swing.JCheckBox();
        jckIsLengthVariable = new javax.swing.JCheckBox();
        jpProperties1212 = new javax.swing.JPanel();
        jckIsSurfaceApplying = new javax.swing.JCheckBox();
        jckIsSurfaceUnitaryApplying = new javax.swing.JCheckBox();
        jckIsSurfaceVariable = new javax.swing.JCheckBox();
        jpProperties1213 = new javax.swing.JPanel();
        jckIsVolumeApplying = new javax.swing.JCheckBox();
        jckIsVolumeUnitaryApplying = new javax.swing.JCheckBox();
        jckIsVolumeVariable = new javax.swing.JCheckBox();
        jpProperties1214 = new javax.swing.JPanel();
        jckIsMassApplying = new javax.swing.JCheckBox();
        jckIsMassUnitaryApplying = new javax.swing.JCheckBox();
        jckIsMassVariable = new javax.swing.JCheckBox();
        jpProperties1215 = new javax.swing.JPanel();
        jckIsWeightGrossApplying = new javax.swing.JCheckBox();
        jckIsWeightDeliveryApplying = new javax.swing.JCheckBox();
        jpProperties122 = new javax.swing.JPanel();
        jpProperties1221 = new javax.swing.JPanel();
        jckIsFreeDiscountUnitary = new javax.swing.JCheckBox();
        jckIsFreePrice = new javax.swing.JCheckBox();
        jckIsFreeDiscountEntry = new javax.swing.JCheckBox();
        jckIsFreeDiscount = new javax.swing.JCheckBox();
        jckIsFreeDiscountDoc = new javax.swing.JCheckBox();
        jckIsFreeCommissions = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlSurplusPercentage = new javax.swing.JLabel();
        jtfSurplusPercentage = new javax.swing.JTextField();
        jpProperties2 = new javax.swing.JPanel();
        jpProperties21 = new javax.swing.JPanel();
        jpProperties211 = new javax.swing.JPanel();
        jlBizAreaAvailable = new javax.swing.JLabel();
        jsptBizAreaAvailable = new javax.swing.JScrollPane();
        jltBizAreaAvailable = new javax.swing.JList<SFormComponentItem>();
        jpProperties212 = new javax.swing.JPanel();
        jpProperties2121 = new javax.swing.JPanel();
        jbBizAreaAssign = new javax.swing.JButton();
        jbBizAreaAssignAll = new javax.swing.JButton();
        jbBizAreaRemove = new javax.swing.JButton();
        jbBizAreaRemoveAll = new javax.swing.JButton();
        jpProperties213 = new javax.swing.JPanel();
        jlBizAreaAsignated = new javax.swing.JLabel();
        jspBizAreaAsignated = new javax.swing.JScrollPane();
        jltBizAreaAsignated = new javax.swing.JList<SFormComponentItem>();
        jpProperties22 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jckIsItemRefRequired = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jlFkDefaultItemRefId_n = new javax.swing.JLabel();
        jcbFkDefaultItemRefId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkDefaultItemRefId_n = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jlFkAdministrativeConceptTypeId = new javax.swing.JLabel();
        jcbFkAdministrativeConceptTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkAdministrativeConceptTypeId = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jlFkTaxableConceptTypeId = new javax.swing.JLabel();
        jcbFkTaxableConceptTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkTaxableConceptTypeId = new javax.swing.JButton();
        jpCommand = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        jToggleButton1.setText("jToggleButton1");

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ítem genérico");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpRegistry.setLayout(new java.awt.BorderLayout());

        jpRegistryNorth.setLayout(new java.awt.BorderLayout());

        jpRegistry1.setBorder(javax.swing.BorderFactory.createTitledBorder("Clasificación del ítem genérico:"));
        jpRegistry1.setPreferredSize(new java.awt.Dimension(100, 110));
        jpRegistry1.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jpRegistry11.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpRegistry111.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemCategoryId.setText("Categoría de ítem: *");
        jlFkItemCategoryId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry111.add(jlFkItemCategoryId);

        jcbFkItemCategoryId.setPreferredSize(new java.awt.Dimension(300, 23));
        jpRegistry111.add(jcbFkItemCategoryId);

        jpRegistry11.add(jpRegistry111);

        jpRegistry112.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemClassId.setText("Clase de ítem: *");
        jlFkItemClassId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry112.add(jlFkItemClassId);

        jcbFkItemClassId.setPreferredSize(new java.awt.Dimension(300, 23));
        jpRegistry112.add(jcbFkItemClassId);

        jpRegistry11.add(jpRegistry112);

        jpRegistry113.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemTypeId.setText("Tipo de ítem: *");
        jlFkItemTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry113.add(jlFkItemTypeId);

        jcbFkItemTypeId.setPreferredSize(new java.awt.Dimension(300, 23));
        jpRegistry113.add(jcbFkItemTypeId);

        jpRegistry11.add(jpRegistry113);

        jpRegistry1.add(jpRegistry11);

        jpRegistry12.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpRegistry121.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jckIsDeleted.setForeground(java.awt.Color.red);
        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckIsDeleted.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry121.add(jckIsDeleted);

        jpRegistry12.add(jpRegistry121);

        jpRegistry122.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpRegistry12.add(jpRegistry122);

        jpRegistry123.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpRegistry12.add(jpRegistry123);

        jpRegistry1.add(jpRegistry12);

        jpRegistryNorth.add(jpRegistry1, java.awt.BorderLayout.NORTH);

        jpRegistry2.setPreferredSize(new java.awt.Dimension(100, 250));
        jpRegistry2.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jpRegistry21.setBorder(javax.swing.BorderFactory.createTitledBorder("Nombre y código del ítem genérico:"));
        jpRegistry21.setPreferredSize(new java.awt.Dimension(675, 260));
        jpRegistry21.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jpRegistry211.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDbmsFkItemFamilyId.setText("Familia de ítems: *");
        jlDbmsFkItemFamilyId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry211.add(jlDbmsFkItemFamilyId);

        jcbFkDbmsItemFamilyId.setPreferredSize(new java.awt.Dimension(300, 23));
        jpRegistry211.add(jcbFkDbmsItemFamilyId);

        jbDbmsFkItemFamilyId.setText("jButton1");
        jbDbmsFkItemFamilyId.setToolTipText("Seleccionar familia de ítems");
        jbDbmsFkItemFamilyId.setFocusable(false);
        jbDbmsFkItemFamilyId.setPreferredSize(new java.awt.Dimension(23, 23));
        jpRegistry211.add(jbDbmsFkItemFamilyId);

        jpRegistry21.add(jpRegistry211);

        jpRegistry212.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemGroupId.setText("Grupo de ítems: *");
        jlFkItemGroupId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry212.add(jlFkItemGroupId);

        jcbFkItemGroupId.setPreferredSize(new java.awt.Dimension(300, 23));
        jpRegistry212.add(jcbFkItemGroupId);

        jbFkItemGroupId.setText("jButton1");
        jbFkItemGroupId.setToolTipText("Seleccionar grupo de ítems");
        jbFkItemGroupId.setFocusable(false);
        jbFkItemGroupId.setPreferredSize(new java.awt.Dimension(23, 23));
        jpRegistry212.add(jbFkItemGroupId);

        jpRegistry21.add(jpRegistry212);

        jpRegistry213.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsItemShortApplying.setText("Aplica nombre corto");
        jckIsItemShortApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry213.add(jckIsItemShortApplying);

        jpRegistry21.add(jpRegistry213);

        jpRegistry214.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemGeneric.setText("Nombre: *");
        jlItemGeneric.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry214.add(jlItemGeneric);

        jtfItemGeneric.setText("TEXT");
        jtfItemGeneric.setPreferredSize(new java.awt.Dimension(300, 23));
        jpRegistry214.add(jtfItemGeneric);

        jLabel3.setPreferredSize(new java.awt.Dimension(300, 14));
        jpRegistry214.add(jLabel3);

        jpRegistry21.add(jpRegistry214);

        jpRegistry215.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemGenericShort.setText("Nombre corto: *");
        jlItemGenericShort.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry215.add(jlItemGenericShort);

        jtfItemGenericShort.setText("TEXT");
        jtfItemGenericShort.setPreferredSize(new java.awt.Dimension(300, 23));
        jpRegistry215.add(jtfItemGenericShort);

        jbCopyItemGeneric.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbCopyItemGeneric.setToolTipText("Copiar nombre");
        jbCopyItemGeneric.setFocusable(false);
        jbCopyItemGeneric.setPreferredSize(new java.awt.Dimension(23, 23));
        jpRegistry215.add(jbCopyItemGeneric);

        jpRegistry21.add(jpRegistry215);

        jpRegistry216.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsItemKeyApplying.setText("Aplican claves de ítems");
        jckIsItemKeyApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry216.add(jckIsItemKeyApplying);

        jpRegistry21.add(jpRegistry216);

        jpRegistry217.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsItemKeyAutomatic.setText("Claves de ítems automáticas");
        jckIsItemKeyAutomatic.setPreferredSize(new java.awt.Dimension(250, 23));
        jpRegistry217.add(jckIsItemKeyAutomatic);

        jpRegistry21.add(jpRegistry217);

        jpRegistry218.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código: *");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry218.add(jlCode);

        jtfCode.setText("TEXT");
        jtfCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry218.add(jtfCode);

        jpRegistry21.add(jpRegistry218);

        jpRegistry2.add(jpRegistry21);

        jpRegistry22.setBorder(javax.swing.BorderFactory.createTitledBorder("Propiedades del ítem genérico:"));
        jpRegistry22.setPreferredSize(new java.awt.Dimension(450, 100));
        jpRegistry22.setLayout(new java.awt.BorderLayout());

        jpRegistry221.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jpRegistry2211.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsItemLineApplying.setText("Aplica línea de ítems");
        jckIsItemLineApplying.setPreferredSize(new java.awt.Dimension(250, 23));
        jpRegistry2211.add(jckIsItemLineApplying);

        jpRegistry221.add(jpRegistry2211);

        jpRegistry2212.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsItemNameEditable.setText("Los nombres de ítems se pueden modificar");
        jckIsItemNameEditable.setPreferredSize(new java.awt.Dimension(250, 23));
        jpRegistry2212.add(jckIsItemNameEditable);

        jpRegistry221.add(jpRegistry2212);

        jpRegistry2213.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsItemKeyEditable.setText("Las claves de ítems se pueden modificar");
        jckIsItemKeyEditable.setPreferredSize(new java.awt.Dimension(250, 23));
        jpRegistry2213.add(jckIsItemKeyEditable);

        jpRegistry221.add(jpRegistry2213);

        jpRegistry2215.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsBulk.setText("Es a granel");
        jckIsBulk.setPreferredSize(new java.awt.Dimension(125, 23));
        jpRegistry2215.add(jckIsBulk);

        jpRegistry221.add(jpRegistry2215);

        jpRegistry2214.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsInventoriable.setText("Es inventariable");
        jckIsInventoriable.setPreferredSize(new java.awt.Dimension(125, 23));
        jpRegistry2214.add(jckIsInventoriable);

        jckIsLotApplying.setText("Aplica lote");
        jckIsLotApplying.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry2214.add(jckIsLotApplying);

        jlDaysForExpiration.setText("Días anaquel:");
        jlDaysForExpiration.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry2214.add(jlDaysForExpiration);

        jtfDaysForExpiration.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDaysForExpiration.setText("0");
        jtfDaysForExpiration.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry2214.add(jtfDaysForExpiration);

        jpRegistry221.add(jpRegistry2214);

        jpRegistry2216.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkSerialNumberTypeId.setText("Tipo de número de serie: *");
        jlFkSerialNumberTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry2216.add(jlFkSerialNumberTypeId);

        jcbFkSerialNumberTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jpRegistry2216.add(jcbFkSerialNumberTypeId);

        jpRegistry221.add(jpRegistry2216);

        jpRegistry2217.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSerialNumber.setText("Nombre del número de serie:");
        jlSerialNumber.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry2217.add(jlSerialNumber);

        jtfSerialNumber.setText("TEXT");
        jtfSerialNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry2217.add(jtfSerialNumber);

        jlSerialNumberExample.setText("(e.g. S/N)");
        jlSerialNumberExample.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry2217.add(jlSerialNumberExample);

        jpRegistry221.add(jpRegistry2217);

        jpRegistry2218.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSerialNumberFormat.setText("Formato del número de serie:");
        jlSerialNumberFormat.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry2218.add(jlSerialNumberFormat);

        jtfSerialNumberFormat.setText("TEXT");
        jtfSerialNumberFormat.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry2218.add(jtfSerialNumberFormat);

        jlSerialNumberFormatExample.setText("(e.g. 99AAXX)");
        jlSerialNumberFormatExample.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry2218.add(jlSerialNumberFormatExample);

        jpRegistry221.add(jpRegistry2218);

        jpRegistry22.add(jpRegistry221, java.awt.BorderLayout.NORTH);

        jpRegistry2.add(jpRegistry22);

        jpRegistryNorth.add(jpRegistry2, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jpRegistryNorth, java.awt.BorderLayout.NORTH);

        jpRegistryCenter.setBorder(javax.swing.BorderFactory.createTitledBorder("Unidades de medida del ítem genérico:"));
        jpRegistryCenter.setLayout(new java.awt.BorderLayout());

        jpRegistry3.setPreferredSize(new java.awt.Dimension(100, 105));
        jpRegistry3.setLayout(new java.awt.GridLayout(1, 2, 10, 5));

        jpRegistry31.setLayout(new java.awt.GridLayout(4, 2, 5, 5));

        jlFkUnitTypeId.setText("Tipo de unidad física: *");
        jlFkUnitTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry31.add(jlFkUnitTypeId);

        jcbFkUnitTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry31.add(jcbFkUnitTypeId);

        jckIsUnitsContainedApplying.setText("Tipo de unidad contenida:");
        jckIsUnitsContainedApplying.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry31.add(jckIsUnitsContainedApplying);

        jcbFkUnitUnitsContainedTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry31.add(jcbFkUnitUnitsContainedTypeId);

        jckIsUnitsVirtualApplying.setText("Tipo de unidad virtual:");
        jckIsUnitsVirtualApplying.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry31.add(jckIsUnitsVirtualApplying);

        jcbFkUnitUnitsVirtualTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry31.add(jcbFkUnitUnitsVirtualTypeId);

        jckIsUnitsPackageApplying.setText("Aplican unidades para conversión");
        jckIsUnitsPackageApplying.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry31.add(jckIsUnitsPackageApplying);

        jpRegistry3.add(jpRegistry31);

        jpRegistry32.setLayout(new java.awt.GridLayout(4, 2, 5, 5));

        jckIsNetContentApplying.setText("Tipo de unidad contenido neto:");
        jckIsNetContentApplying.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry32.add(jckIsNetContentApplying);

        jcbFkUnitNetContentTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry32.add(jcbFkUnitNetContentTypeId);

        jckIsNetContentUnitaryApplying.setText("Tipo de unidad contenido neto unit.:");
        jckIsNetContentUnitaryApplying.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry32.add(jckIsNetContentUnitaryApplying);

        jcbFkUnitNetContentUnitaryTypeId.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry32.add(jcbFkUnitNetContentUnitaryTypeId);

        jckIsNetContentVariable.setText("Contenido neto variable");
        jckIsNetContentVariable.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry32.add(jckIsNetContentVariable);

        jpRegistry3.add(jpRegistry32);

        jpRegistryCenter.add(jpRegistry3, java.awt.BorderLayout.NORTH);

        jpRegistry.add(jpRegistryCenter, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Datos del registro", jpRegistry);

        jpProperties.setPreferredSize(new java.awt.Dimension(931, 600));
        jpProperties.setLayout(new java.awt.BorderLayout());

        jpProperties1.setLayout(new java.awt.BorderLayout());

        jpProperties11.setPreferredSize(new java.awt.Dimension(100, 185));
        jpProperties11.setLayout(new java.awt.BorderLayout());

        jpProperties111.setPreferredSize(new java.awt.Dimension(100, 200));
        jpProperties111.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jpProperties1111.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de los nombres de ítems:"));
        jpProperties1111.setPreferredSize(new java.awt.Dimension(435, 300));
        jpProperties1111.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jpProperties11111.setBorder(javax.swing.BorderFactory.createTitledBorder("Posiciones para el nombre:"));
        jpProperties11111.setPreferredSize(new java.awt.Dimension(215, 288));
        jpProperties11111.setLayout(new java.awt.GridLayout(5, 2, 5, 5));

        jlNamingOrdinaryPosItemGeneric.setText("Ítem genérico:");
        jpProperties11111.add(jlNamingOrdinaryPosItemGeneric);

        jtfNamingOrdinaryPosItemGeneric.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingOrdinaryPosItemGeneric.setText("0");
        jpProperties11111.add(jtfNamingOrdinaryPosItemGeneric);

        jlNamingOrdinaryPosBrand.setText("Marca:");
        jpProperties11111.add(jlNamingOrdinaryPosBrand);

        jtfNamingOrdinaryPosBrand.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingOrdinaryPosBrand.setText("0");
        jpProperties11111.add(jtfNamingOrdinaryPosBrand);

        jlNamingOrdinaryPosManufacturer.setText("Fabricante:");
        jpProperties11111.add(jlNamingOrdinaryPosManufacturer);

        jtfNamingOrdinaryPosManufacturer.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingOrdinaryPosManufacturer.setText("0");
        jpProperties11111.add(jtfNamingOrdinaryPosManufacturer);

        jlNamingOrdinaryPosName.setText("Nombre:");
        jpProperties11111.add(jlNamingOrdinaryPosName);

        jtfNamingOrdinaryPosName.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingOrdinaryPosName.setText("0");
        jpProperties11111.add(jtfNamingOrdinaryPosName);

        jlNamingOrdinaryPosPresentation.setText("Presentación:");
        jpProperties11111.add(jlNamingOrdinaryPosPresentation);

        jtfNamingOrdinaryPosPresentation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingOrdinaryPosPresentation.setText("0");
        jpProperties11111.add(jtfNamingOrdinaryPosPresentation);

        jpProperties1111.add(jpProperties11111);

        jpProperties11112.setBorder(javax.swing.BorderFactory.createTitledBorder("Posiciones para la clave:"));
        jpProperties11112.setLayout(new java.awt.GridLayout(5, 2, 5, 5));

        jlKeyOrdinaryPosItemGeneric.setText("Ítem genérico:");
        jpProperties11112.add(jlKeyOrdinaryPosItemGeneric);

        jtfKeyOrdinaryPosItemGeneric.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfKeyOrdinaryPosItemGeneric.setText("0");
        jpProperties11112.add(jtfKeyOrdinaryPosItemGeneric);

        jlKeyOrdinaryPosBrand.setText("Marca:");
        jpProperties11112.add(jlKeyOrdinaryPosBrand);

        jtfKeyOrdinaryPosBrand.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfKeyOrdinaryPosBrand.setText("0");
        jpProperties11112.add(jtfKeyOrdinaryPosBrand);

        jlKeyOrdinaryPosManufacturer.setText("Fabricante:");
        jpProperties11112.add(jlKeyOrdinaryPosManufacturer);

        jtfKeyOrdinaryPosManufacturer.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfKeyOrdinaryPosManufacturer.setText("0");
        jpProperties11112.add(jtfKeyOrdinaryPosManufacturer);

        jlKeyOrdinaryPosCode.setText("Código del ítem:");
        jpProperties11112.add(jlKeyOrdinaryPosCode);

        jtfKeyOrdinaryPosCode.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfKeyOrdinaryPosCode.setText("0");
        jpProperties11112.add(jtfKeyOrdinaryPosCode);

        jpProperties1111.add(jpProperties11112);

        jpProperties111.add(jpProperties1111);

        jpProperties1112.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de los nombres de líneas de ítems:"));
        jpProperties1112.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jPanel62.setBorder(javax.swing.BorderFactory.createTitledBorder("Posiciones para el nombre:"));
        jPanel62.setPreferredSize(new java.awt.Dimension(220, 288));
        jPanel62.setLayout(new java.awt.GridLayout(5, 2, 5, 5));

        jlNamingLinePosItemGeneric.setText("Ítem genérico:");
        jPanel62.add(jlNamingLinePosItemGeneric);

        jtfNamingLinePosItemGeneric.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingLinePosItemGeneric.setText("0");
        jPanel62.add(jtfNamingLinePosItemGeneric);

        jlNamingLinePosItemLine.setText("Línea de ítems:");
        jPanel62.add(jlNamingLinePosItemLine);

        jtfNamingLinePosItemLine.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingLinePosItemLine.setText("0");
        jPanel62.add(jtfNamingLinePosItemLine);

        jlNamingLinePosBrand.setText("Marca:");
        jPanel62.add(jlNamingLinePosBrand);

        jtfNamingLinePosBrand.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingLinePosBrand.setText("0");
        jPanel62.add(jtfNamingLinePosBrand);

        jlNamingLinePosManufacturer.setText("Fabricante:");
        jPanel62.add(jlNamingLinePosManufacturer);

        jtfNamingLinePosManufacturer.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNamingLinePosManufacturer.setText("0");
        jPanel62.add(jtfNamingLinePosManufacturer);

        jLabel1.setText("+ nom., pres., vars.");
        jPanel62.add(jLabel1);
        jPanel62.add(jLabel2);

        jpProperties1112.add(jPanel62);

        jPanel63.setBorder(javax.swing.BorderFactory.createTitledBorder("Posiciones para la clave:"));
        jPanel63.setLayout(new java.awt.GridLayout(5, 2, 5, 5));

        jlKeyLinePosItemGeneric.setText("Ítem genérico:");
        jPanel63.add(jlKeyLinePosItemGeneric);

        jtfKeyLinePosItemGeneric.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfKeyLinePosItemGeneric.setText("0");
        jPanel63.add(jtfKeyLinePosItemGeneric);

        jlKeyLinePosItemLine.setText("Línea de ítems:");
        jPanel63.add(jlKeyLinePosItemLine);

        jtfKeyLinePosItemLine.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfKeyLinePosItemLine.setText("0");
        jPanel63.add(jtfKeyLinePosItemLine);

        jlKeyLinePosBrand.setText("Marca:");
        jPanel63.add(jlKeyLinePosBrand);

        jtfKeyLinePosBrand.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfKeyLinePosBrand.setText("0");
        jPanel63.add(jtfKeyLinePosBrand);

        jlKeyLinePosManufacturer.setText("Fabricante:");
        jPanel63.add(jlKeyLinePosManufacturer);

        jtfKeyLinePosManufacturer.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfKeyLinePosManufacturer.setText("0");
        jPanel63.add(jtfKeyLinePosManufacturer);

        jLabel4.setText("+ cód. ítem, vars.");
        jPanel63.add(jLabel4);
        jPanel63.add(jLabel5);

        jpProperties1112.add(jPanel63);

        jpProperties111.add(jpProperties1112);

        jpProperties11.add(jpProperties111, java.awt.BorderLayout.CENTER);

        jpProperties1.add(jpProperties11, java.awt.BorderLayout.NORTH);

        jpProperties12.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jpProperties121.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de dimensiones físicas:"));
        jpProperties121.setLayout(new java.awt.GridLayout(5, 3, 5, 5));

        jpProperties1211.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsLengthApplying.setText("Aplica longitud");
        jckIsLengthApplying.setPreferredSize(new java.awt.Dimension(125, 23));
        jpProperties1211.add(jckIsLengthApplying);

        jckIsLengthUnitaryApplying.setText("Aplica longitud unitaria");
        jckIsLengthUnitaryApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1211.add(jckIsLengthUnitaryApplying);

        jckIsLengthVariable.setText("Es de longitud variable");
        jckIsLengthVariable.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1211.add(jckIsLengthVariable);

        jpProperties121.add(jpProperties1211);

        jpProperties1212.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsSurfaceApplying.setText("Aplica superficie");
        jckIsSurfaceApplying.setPreferredSize(new java.awt.Dimension(125, 23));
        jpProperties1212.add(jckIsSurfaceApplying);

        jckIsSurfaceUnitaryApplying.setText("Aplica superficie unitaria");
        jckIsSurfaceUnitaryApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1212.add(jckIsSurfaceUnitaryApplying);

        jckIsSurfaceVariable.setText("Es de superficie variable");
        jckIsSurfaceVariable.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1212.add(jckIsSurfaceVariable);

        jpProperties121.add(jpProperties1212);

        jpProperties1213.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsVolumeApplying.setText("Aplica volumen");
        jckIsVolumeApplying.setPreferredSize(new java.awt.Dimension(125, 23));
        jpProperties1213.add(jckIsVolumeApplying);

        jckIsVolumeUnitaryApplying.setText("Aplica volumen unitario");
        jckIsVolumeUnitaryApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1213.add(jckIsVolumeUnitaryApplying);

        jckIsVolumeVariable.setText("Es de volumen variable");
        jckIsVolumeVariable.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1213.add(jckIsVolumeVariable);

        jpProperties121.add(jpProperties1213);

        jpProperties1214.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsMassApplying.setText("Aplica masa");
        jckIsMassApplying.setPreferredSize(new java.awt.Dimension(125, 23));
        jpProperties1214.add(jckIsMassApplying);

        jckIsMassUnitaryApplying.setText("Aplica masa unitaria");
        jckIsMassUnitaryApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1214.add(jckIsMassUnitaryApplying);

        jckIsMassVariable.setText("Es de masa variable");
        jckIsMassVariable.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1214.add(jckIsMassVariable);

        jpProperties121.add(jpProperties1214);

        jpProperties1215.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsWeightGrossApplying.setText("Aplica peso bruto");
        jckIsWeightGrossApplying.setPreferredSize(new java.awt.Dimension(125, 23));
        jpProperties1215.add(jckIsWeightGrossApplying);

        jckIsWeightDeliveryApplying.setText("Aplica peso flete");
        jckIsWeightDeliveryApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jpProperties1215.add(jckIsWeightDeliveryApplying);

        jpProperties121.add(jpProperties1215);

        jpProperties12.add(jpProperties121);

        jpProperties122.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración para comercialización:"));
        jpProperties122.setLayout(new java.awt.BorderLayout());

        jpProperties1221.setPreferredSize(new java.awt.Dimension(100, 80));
        jpProperties1221.setLayout(new java.awt.GridLayout(3, 2, 5, 0));

        jckIsFreeDiscountUnitary.setText("Sin descuento unitario");
        jpProperties1221.add(jckIsFreeDiscountUnitary);

        jckIsFreePrice.setText("Sin precio");
        jpProperties1221.add(jckIsFreePrice);

        jckIsFreeDiscountEntry.setText("Sin descuento en partida");
        jpProperties1221.add(jckIsFreeDiscountEntry);

        jckIsFreeDiscount.setText("Sin descuento en listas de precios");
        jpProperties1221.add(jckIsFreeDiscount);

        jckIsFreeDiscountDoc.setText("Sin descuento en documento");
        jpProperties1221.add(jckIsFreeDiscountDoc);

        jckIsFreeCommissions.setText("Sin comisiones de venta");
        jpProperties1221.add(jckIsFreeCommissions);

        jpProperties122.add(jpProperties1221, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSurplusPercentage.setText("Excedente predeterminado:");
        jlSurplusPercentage.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel2.add(jlSurplusPercentage);

        jtfSurplusPercentage.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSurplusPercentage.setText("0.00%");
        jtfSurplusPercentage.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jtfSurplusPercentage);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jpProperties122.add(jPanel1, java.awt.BorderLayout.CENTER);

        jpProperties12.add(jpProperties122);

        jpProperties1.add(jpProperties12, java.awt.BorderLayout.CENTER);

        jpProperties.add(jpProperties1, java.awt.BorderLayout.NORTH);

        jpProperties2.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jpProperties21.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de áreas de negocios:"));
        jpProperties21.setLayout(new java.awt.BorderLayout(5, 5));

        jpProperties211.setPreferredSize(new java.awt.Dimension(200, 300));
        jpProperties211.setLayout(new java.awt.BorderLayout());

        jlBizAreaAvailable.setText("Áreas de negocios disponibles:");
        jlBizAreaAvailable.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jlBizAreaAvailable.setPreferredSize(new java.awt.Dimension(100, 23));
        jpProperties211.add(jlBizAreaAvailable, java.awt.BorderLayout.NORTH);

        jltBizAreaAvailable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltBizAreaAvailableMouseClicked(evt);
            }
        });
        jsptBizAreaAvailable.setViewportView(jltBizAreaAvailable);

        jpProperties211.add(jsptBizAreaAvailable, java.awt.BorderLayout.CENTER);

        jpProperties21.add(jpProperties211, java.awt.BorderLayout.WEST);

        jpProperties212.setLayout(new java.awt.BorderLayout());

        jpProperties2121.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 0, 0, 0));
        jpProperties2121.setLayout(new java.awt.GridLayout(4, 1, 5, 5));

        jbBizAreaAssign.setText(">");
        jbBizAreaAssign.setToolTipText("Asignar");
        jbBizAreaAssign.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbBizAreaAssign.setMaximumSize(new java.awt.Dimension(23, 23));
        jbBizAreaAssign.setMinimumSize(new java.awt.Dimension(23, 23));
        jbBizAreaAssign.setPreferredSize(new java.awt.Dimension(23, 23));
        jbBizAreaAssign.setRequestFocusEnabled(false);
        jpProperties2121.add(jbBizAreaAssign);

        jbBizAreaAssignAll.setText(">>");
        jbBizAreaAssignAll.setToolTipText("Asignar todos");
        jbBizAreaAssignAll.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbBizAreaAssignAll.setMaximumSize(new java.awt.Dimension(23, 23));
        jbBizAreaAssignAll.setMinimumSize(new java.awt.Dimension(23, 23));
        jbBizAreaAssignAll.setPreferredSize(new java.awt.Dimension(23, 23));
        jbBizAreaAssignAll.setRequestFocusEnabled(false);
        jpProperties2121.add(jbBizAreaAssignAll);

        jbBizAreaRemove.setText("<");
        jbBizAreaRemove.setToolTipText("Remover");
        jbBizAreaRemove.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbBizAreaRemove.setMaximumSize(new java.awt.Dimension(23, 23));
        jbBizAreaRemove.setMinimumSize(new java.awt.Dimension(23, 23));
        jbBizAreaRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jbBizAreaRemove.setRequestFocusEnabled(false);
        jpProperties2121.add(jbBizAreaRemove);

        jbBizAreaRemoveAll.setText("<<");
        jbBizAreaRemoveAll.setToolTipText("Remover todos");
        jbBizAreaRemoveAll.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbBizAreaRemoveAll.setMaximumSize(new java.awt.Dimension(23, 23));
        jbBizAreaRemoveAll.setMinimumSize(new java.awt.Dimension(23, 23));
        jbBizAreaRemoveAll.setPreferredSize(new java.awt.Dimension(23, 23));
        jbBizAreaRemoveAll.setRequestFocusEnabled(false);
        jpProperties2121.add(jbBizAreaRemoveAll);

        jpProperties212.add(jpProperties2121, java.awt.BorderLayout.NORTH);

        jpProperties21.add(jpProperties212, java.awt.BorderLayout.CENTER);

        jpProperties213.setPreferredSize(new java.awt.Dimension(200, 300));
        jpProperties213.setLayout(new java.awt.BorderLayout());

        jlBizAreaAsignated.setText("Áreas de negocios asignadas:");
        jlBizAreaAsignated.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jlBizAreaAsignated.setPreferredSize(new java.awt.Dimension(100, 23));
        jpProperties213.add(jlBizAreaAsignated, java.awt.BorderLayout.NORTH);

        jltBizAreaAsignated.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltBizAreaAsignatedMouseClicked(evt);
            }
        });
        jspBizAreaAsignated.setViewportView(jltBizAreaAsignated);

        jpProperties213.add(jspBizAreaAsignated, java.awt.BorderLayout.CENTER);

        jpProperties21.add(jpProperties213, java.awt.BorderLayout.EAST);

        jpProperties2.add(jpProperties21);

        jpProperties22.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración para contabilización:"));
        jpProperties22.setPreferredSize(new java.awt.Dimension(450, 100));
        jpProperties22.setLayout(new java.awt.BorderLayout());

        jPanel35.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsItemRefRequired.setText("Requiere ítem de referencia");
        jckIsItemRefRequired.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel38.add(jckIsItemRefRequired);

        jPanel35.add(jPanel38);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDefaultItemRefId_n.setText("Ítem de referencia predeterminado:");
        jlFkDefaultItemRefId_n.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel6.add(jlFkDefaultItemRefId_n);

        jcbFkDefaultItemRefId_n.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel6.add(jcbFkDefaultItemRefId_n);

        jbFkDefaultItemRefId_n.setText("jButton1");
        jbFkDefaultItemRefId_n.setToolTipText("Seleccionar ítem");
        jbFkDefaultItemRefId_n.setFocusable(false);
        jbFkDefaultItemRefId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbFkDefaultItemRefId_n);

        jPanel35.add(jPanel6);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkAdministrativeConceptTypeId.setText("Tipo de concepto administrativo: *");
        jlFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel36.add(jlFkAdministrativeConceptTypeId);

        jcbFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel36.add(jcbFkAdministrativeConceptTypeId);

        jbFkAdministrativeConceptTypeId.setText("jButton1");
        jbFkAdministrativeConceptTypeId.setToolTipText("Seleccionar tipo de concepto administrativo");
        jbFkAdministrativeConceptTypeId.setFocusable(false);
        jbFkAdministrativeConceptTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel36.add(jbFkAdministrativeConceptTypeId);

        jPanel35.add(jPanel36);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTaxableConceptTypeId.setText("Tipo de concepto de impuestos: *");
        jlFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel28.add(jlFkTaxableConceptTypeId);

        jcbFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel28.add(jcbFkTaxableConceptTypeId);

        jbFkTaxableConceptTypeId.setText("jButton1");
        jbFkTaxableConceptTypeId.setToolTipText("Seleccionar tipo de concepto de impuestos");
        jbFkTaxableConceptTypeId.setFocusable(false);
        jbFkTaxableConceptTypeId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel28.add(jbFkTaxableConceptTypeId);

        jPanel35.add(jPanel28);

        jpProperties22.add(jPanel35, java.awt.BorderLayout.NORTH);

        jpProperties2.add(jpProperties22);

        jpProperties.add(jpProperties2, java.awt.BorderLayout.CENTER);

        jTabbedPane.addTab("Configuración", jpProperties);

        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        jpCommand.setPreferredSize(new java.awt.Dimension(792, 33));
        jpCommand.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCommand.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpCommand.add(jbCancel);

        getContentPane().add(jpCommand, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(960, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jltBizAreaAvailableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltBizAreaAvailableMouseClicked
        if (evt.getClickCount() == 2) {
            actionBizAreaAssign();
        }
    }//GEN-LAST:event_jltBizAreaAvailableMouseClicked

    private void jltBizAreaAsignatedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltBizAreaAsignatedMouseClicked
        if (evt.getClickCount() == 2) {
            actionBizAreaRemove();
        }
    }//GEN-LAST:event_jltBizAreaAsignatedMouseClicked

    /*
     * Private methods:
     */

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moComboBoxFamilyItem = new SFormComboBoxGroup(miClient);
        moComboBoxCategoryItem = new SFormComboBoxGroup(miClient);

        moFieldFkItemCategoryId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemCategoryId, jlFkItemCategoryId);
        moFieldFkItemCategoryId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkItemClassId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemClassId, jlFkItemClassId);
        moFieldFkItemClassId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkItemTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemTypeId, jlFkItemTypeId);
        moFieldFkItemTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);
        moFieldIsDeleted.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkDbmsItemFamilyId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkDbmsItemFamilyId, jlDbmsFkItemFamilyId);
        moFieldFkDbmsItemFamilyId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkDbmsItemFamilyId.setPickerButton(jbDbmsFkItemFamilyId);
        moFieldFkItemGroupId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemGroupId, jlFkItemGroupId);
        moFieldFkItemGroupId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkItemGroupId.setPickerButton(jbFkItemGroupId);
        moFieldIsItemShortApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsItemShortApplying);
        moFieldIsItemShortApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldItemGeneric = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfItemGeneric, jlItemGeneric);
        moFieldItemGeneric.setTabbedPaneIndex(0, jTabbedPane);
        moFieldItemGeneric.setLengthMax(50);
        moFieldItemGenericShort = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfItemGenericShort, jlItemGenericShort);
        moFieldItemGenericShort.setTabbedPaneIndex(0, jTabbedPane);
        moFieldItemGenericShort.setLengthMax(25);
        moFieldIsItemKeyApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsItemKeyApplying);
        moFieldIsItemKeyApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsItemKeyAutomatic = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsItemKeyAutomatic);
        moFieldIsItemKeyAutomatic.setTabbedPaneIndex(0, jTabbedPane);
        moFieldCode = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfCode, jlCode);
        moFieldCode.setTabbedPaneIndex(0, jTabbedPane);
        moFieldCode.setLengthMax(5);
        moFieldIsItemLineApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsItemLineApplying);
        moFieldIsItemLineApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsItemKeyEditable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsItemKeyEditable);
        moFieldIsItemKeyEditable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsItemNameEditable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsItemNameEditable);
        moFieldIsItemNameEditable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsBulk = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsBulk);
        moFieldIsBulk.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsInventoriable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsInventoriable);
        moFieldIsInventoriable.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsLotApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsLotApplying);
        moFieldIsLotApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldDaysForExpiration = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfDaysForExpiration, jlDaysForExpiration);
        moFieldDaysForExpiration.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkSerialNumberTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkSerialNumberTypeId, jlFkSerialNumberTypeId);
        moFieldFkSerialNumberTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldSerialNumber = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSerialNumber, jlSerialNumber);
        moFieldSerialNumber.setLengthMax(50);
        moFieldSerialNumber.setTabbedPaneIndex(0, jTabbedPane);
        moFieldSerialNumberFormat = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfSerialNumberFormat, jlSerialNumberFormat);
        moFieldSerialNumberFormat.setLengthMax(50);
        moFieldSerialNumberFormat.setTabbedPaneIndex(0, jTabbedPane);
        moFieldSurplusPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_FLOAT, false, jtfSurplusPercentage, jlSurplusPercentage);
        moFieldSurplusPercentage.setIsPercent(true);
        moFieldSurplusPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        moFieldFkUnitTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitTypeId, jlFkUnitTypeId);
        moFieldFkUnitTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsUnitsContainedApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsUnitsContainedApplying);
        moFieldIsUnitsContainedApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitUnitsContainedTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitUnitsContainedTypeId, jckIsUnitsContainedApplying);
        moFieldFkUnitUnitsContainedTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsUnitsVirtualApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsUnitsVirtualApplying);
        moFieldIsUnitsVirtualApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitUnitsVirtualTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitUnitsVirtualTypeId, jckIsUnitsVirtualApplying);
        moFieldFkUnitUnitsVirtualTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsUnitsPackageApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsUnitsPackageApplying);
        moFieldIsUnitsPackageApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsNetContentApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsNetContentApplying);
        moFieldIsNetContentApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitNetContentTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitNetContentTypeId, jckIsNetContentApplying);
        moFieldFkUnitNetContentTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsNetContentUnitaryApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsNetContentUnitaryApplying);
        moFieldIsNetContentUnitaryApplying.setTabbedPaneIndex(0, jTabbedPane);
        moFieldFkUnitNetContentUnitaryTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkUnitNetContentUnitaryTypeId, jckIsNetContentUnitaryApplying);
        moFieldFkUnitNetContentUnitaryTypeId.setTabbedPaneIndex(0, jTabbedPane);
        moFieldIsNetContentVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsNetContentVariable);
        moFieldIsNetContentVariable.setTabbedPaneIndex(0, jTabbedPane);

        moFieldNamingOrdinaryPosItemGeneric = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfNamingOrdinaryPosItemGeneric, jlNamingLinePosItemGeneric);
        moFieldNamingOrdinaryPosItemGeneric.setTabbedPaneIndex(1, jTabbedPane);
        moFieldNamingOrdinaryPosBrand = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfNamingOrdinaryPosBrand, jlNamingOrdinaryPosBrand);
        moFieldNamingOrdinaryPosBrand.setTabbedPaneIndex(1, jTabbedPane);
        moFieldNamingOrdinaryPosManufacturer = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfNamingOrdinaryPosManufacturer, jlNamingOrdinaryPosManufacturer);
        moFieldNamingOrdinaryPosManufacturer.setTabbedPaneIndex(1, jTabbedPane);
        moFieldNamingOrdinaryPosName = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfNamingOrdinaryPosName, jlNamingOrdinaryPosName);
        moFieldNamingOrdinaryPosName.setTabbedPaneIndex(1, jTabbedPane);
        moFieldNamingOrdinaryPosPresentation = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfNamingOrdinaryPosPresentation, jlNamingOrdinaryPosPresentation);
        moFieldNamingOrdinaryPosPresentation.setTabbedPaneIndex(1, jTabbedPane);
        moFieldKeyOrdinaryPosItemGeneric = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfKeyOrdinaryPosItemGeneric, jlKeyOrdinaryPosItemGeneric);
        moFieldKeyOrdinaryPosItemGeneric.setTabbedPaneIndex(1, jTabbedPane);
        moFieldKeyOrdinaryPosBrand = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfKeyOrdinaryPosBrand, jlKeyOrdinaryPosBrand);
        moFieldKeyOrdinaryPosBrand.setTabbedPaneIndex(1, jTabbedPane);
        moFieldKeyOrdinaryPosManufacturer = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfKeyOrdinaryPosManufacturer, jlKeyOrdinaryPosManufacturer);
        moFieldKeyOrdinaryPosManufacturer.setTabbedPaneIndex(1, jTabbedPane);
        moFieldKeyOrdinaryPosCode = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfKeyOrdinaryPosCode, jlKeyOrdinaryPosCode);
        moFieldKeyOrdinaryPosCode.setTabbedPaneIndex(1, jTabbedPane);
        moFieldNamingLinePosItemGeneric = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfNamingLinePosItemGeneric, jlNamingLinePosItemGeneric);
        moFieldNamingLinePosItemGeneric.setTabbedPaneIndex(1, jTabbedPane);
        moFieldNamingLinePosItemLine = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfNamingLinePosItemLine, jlNamingLinePosItemLine);
        moFieldNamingLinePosItemLine.setTabbedPaneIndex(1, jTabbedPane);
        moFieldNamingLinePosBrand = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfNamingLinePosBrand, jlNamingLinePosBrand);
        moFieldNamingLinePosBrand.setTabbedPaneIndex(1, jTabbedPane);
        moFieldNamingLinePosManufacturer = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfNamingLinePosManufacturer, jlNamingLinePosManufacturer);
        moFieldNamingLinePosManufacturer.setTabbedPaneIndex(1, jTabbedPane);
        moFieldKeyLinePosItemGeneric = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfKeyLinePosItemGeneric, jlKeyLinePosItemGeneric);
        moFieldKeyLinePosItemGeneric.setTabbedPaneIndex(1, jTabbedPane);
        moFieldKeyLinePosItemLine = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfKeyLinePosItemLine, jlKeyLinePosItemLine);
        moFieldKeyLinePosItemLine.setTabbedPaneIndex(1, jTabbedPane);
        moFieldKeyLinePosBrand = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfKeyLinePosBrand, jlKeyLinePosBrand);
        moFieldKeyLinePosBrand.setTabbedPaneIndex(1, jTabbedPane);
        moFieldKeyLinePosManufacturer = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfKeyLinePosManufacturer, jlKeyLinePosManufacturer);
        moFieldKeyLinePosManufacturer.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsLengthApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsLengthApplying);
        moFieldIsLengthApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsLengthUnitaryApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsLengthUnitaryApplying);
        moFieldIsLengthUnitaryApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsLengthVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsLengthVariable);
        moFieldIsLengthVariable.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsSurfaceApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSurfaceApplying);
        moFieldIsSurfaceApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsSurfaceUnitaryApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSurfaceUnitaryApplying);
        moFieldIsSurfaceUnitaryApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsSurfaceVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsSurfaceVariable);
        moFieldIsSurfaceVariable.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsVolumeApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsVolumeApplying);
        moFieldIsVolumeApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsVolumeUnitaryApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsVolumeUnitaryApplying);
        moFieldIsVolumeUnitaryApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsVolumeVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsVolumeVariable);
        moFieldIsVolumeVariable.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsMassApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsMassApplying);
        moFieldIsMassApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsMassUnitaryApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsMassUnitaryApplying);
        moFieldIsMassUnitaryApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsMassVariable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsMassVariable);
        moFieldIsMassVariable.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsWeightGrossApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsWeightGrossApplying);
        moFieldIsWeightGrossApplying.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsWeightDeliveryApplying = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsWeightDeliveryApplying);
        moFieldIsWeightDeliveryApplying.setTabbedPaneIndex(1, jTabbedPane);
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
        moFieldBizArea = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jltBizAreaAvailable, jlBizAreaAvailable);
        moFieldBizArea.setTabbedPaneIndex(1, jTabbedPane);
        moFieldBizAreaItemGeneric = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jltBizAreaAsignated, jlBizAreaAsignated);
        moFieldBizAreaItemGeneric.setListValidationType(SLibConstants.LIST_VALIDATION_BY_CONTENT);
        moFieldBizAreaItemGeneric.setTabbedPaneIndex(1, jTabbedPane);
        moFieldIsItemRefRequired = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsItemRefRequired);
        moFieldIsItemRefRequired.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkDefaultItemRefeferenceId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkDefaultItemRefId_n, jlFkDefaultItemRefId_n);
        moFieldFkDefaultItemRefeferenceId_n.setPickerButton(jbFkDefaultItemRefId_n);
        moFieldFkDefaultItemRefeferenceId_n.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkAdministrativeConceptTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkAdministrativeConceptTypeId, jlFkAdministrativeConceptTypeId);
        moFieldFkAdministrativeConceptTypeId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkAdministrativeConceptTypeId.setPickerButton(jbFkAdministrativeConceptTypeId);
        moFieldFkTaxableConceptTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTaxableConceptTypeId, jlFkTaxableConceptTypeId);
        moFieldFkTaxableConceptTypeId.setTabbedPaneIndex(1, jTabbedPane);
        moFieldFkTaxableConceptTypeId.setPickerButton(jbFkTaxableConceptTypeId);

        mvFields.add(moFieldFkItemCategoryId);
        mvFields.add(moFieldFkItemClassId);
        mvFields.add(moFieldFkItemTypeId);
        mvFields.add(moFieldIsDeleted);
        mvFields.add(moFieldFkDbmsItemFamilyId);
        mvFields.add(moFieldFkItemGroupId);
        mvFields.add(moFieldIsItemShortApplying);
        mvFields.add(moFieldItemGeneric);
        mvFields.add(moFieldItemGenericShort);
        mvFields.add(moFieldIsItemKeyApplying);
        mvFields.add(moFieldIsItemKeyAutomatic);
        mvFields.add(moFieldIsItemLineApplying);
        mvFields.add(moFieldIsItemKeyEditable);
        mvFields.add(moFieldIsItemNameEditable);
        mvFields.add(moFieldIsBulk);
        mvFields.add(moFieldIsInventoriable);
        mvFields.add(moFieldIsLotApplying);
        mvFields.add(moFieldDaysForExpiration);
        mvFields.add(moFieldFkSerialNumberTypeId);
        mvFields.add(moFieldSerialNumber);
        mvFields.add(moFieldSerialNumberFormat);
        mvFields.add(moFieldSurplusPercentage);
        mvFields.add(moFieldFkUnitTypeId);
        mvFields.add(moFieldIsUnitsContainedApplying);
        mvFields.add(moFieldFkUnitUnitsContainedTypeId);
        mvFields.add(moFieldIsUnitsVirtualApplying);
        mvFields.add(moFieldFkUnitUnitsVirtualTypeId);
        mvFields.add(moFieldIsUnitsPackageApplying);
        mvFields.add(moFieldIsNetContentApplying);
        mvFields.add(moFieldFkUnitNetContentTypeId);
        mvFields.add(moFieldIsNetContentUnitaryApplying);
        mvFields.add(moFieldFkUnitNetContentUnitaryTypeId);
        mvFields.add(moFieldIsNetContentVariable);
        mvFields.add(moFieldNamingOrdinaryPosItemGeneric);
        mvFields.add(moFieldNamingOrdinaryPosBrand);
        mvFields.add(moFieldNamingOrdinaryPosManufacturer);
        mvFields.add(moFieldNamingOrdinaryPosName);
        mvFields.add(moFieldNamingOrdinaryPosPresentation);
        mvFields.add(moFieldKeyOrdinaryPosItemGeneric);
        mvFields.add(moFieldKeyOrdinaryPosBrand);
        mvFields.add(moFieldKeyOrdinaryPosManufacturer);
        mvFields.add(moFieldKeyOrdinaryPosCode);
        mvFields.add(moFieldNamingLinePosItemGeneric);
        mvFields.add(moFieldNamingLinePosItemLine);
        mvFields.add(moFieldNamingLinePosBrand);
        mvFields.add(moFieldNamingLinePosManufacturer);
        mvFields.add(moFieldKeyLinePosItemGeneric);
        mvFields.add(moFieldKeyLinePosItemLine);
        mvFields.add(moFieldKeyLinePosBrand);
        mvFields.add(moFieldKeyLinePosManufacturer);
        mvFields.add(moFieldIsLengthApplying);
        mvFields.add(moFieldIsLengthUnitaryApplying);
        mvFields.add(moFieldIsLengthVariable);
        mvFields.add(moFieldIsSurfaceApplying);
        mvFields.add(moFieldIsSurfaceUnitaryApplying);
        mvFields.add(moFieldIsSurfaceVariable);
        mvFields.add(moFieldIsVolumeApplying);
        mvFields.add(moFieldIsVolumeUnitaryApplying);
        mvFields.add(moFieldIsVolumeVariable);
        mvFields.add(moFieldIsMassApplying);
        mvFields.add(moFieldIsMassUnitaryApplying);
        mvFields.add(moFieldIsMassVariable);
        mvFields.add(moFieldIsWeightGrossApplying);
        mvFields.add(moFieldIsWeightDeliveryApplying);
        mvFields.add(moFieldIsFreeDiscountUnitary);
        mvFields.add(moFieldIsFreeDiscountEntry);
        mvFields.add(moFieldIsFreeDiscountDoc);
        mvFields.add(moFieldIsFreePrice);
        mvFields.add(moFieldIsFreeDiscount);
        mvFields.add(moFieldIsFreeCommissions);
        mvFields.add(moFieldBizArea);
        mvFields.add(moFieldBizAreaItemGeneric);
        mvFields.add(moFieldIsItemRefRequired);
        mvFields.add(moFieldFkDefaultItemRefeferenceId_n);
        mvFields.add(moFieldFkAdministrativeConceptTypeId);
        mvFields.add(moFieldFkTaxableConceptTypeId);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbDbmsFkItemFamilyId.addActionListener(this);
        jbFkItemGroupId.addActionListener(this);
        jbCopyItemGeneric.addActionListener(this);
        jbFkDefaultItemRefId_n.addActionListener(this);
        jbFkAdministrativeConceptTypeId.addActionListener(this);
        jbFkTaxableConceptTypeId.addActionListener(this);
        jbBizAreaAssign.addActionListener(this);
        jbBizAreaAssignAll.addActionListener(this);
        jbBizAreaRemove.addActionListener(this);
        jbBizAreaRemoveAll.addActionListener(this);

        jtfItemGeneric.addFocusListener(this);

        jcbFkItemGroupId.addItemListener(this);
        jcbFkSerialNumberTypeId.addItemListener(this);
        jckIsItemShortApplying.addItemListener(this);
        jckIsItemKeyApplying.addItemListener(this);
        jckIsItemKeyAutomatic.addItemListener(this);
        jckIsItemLineApplying.addItemListener(this);
        jckIsInventoriable.addItemListener(this);
        jckIsLotApplying.addItemListener(this);
        jckIsUnitsContainedApplying.addItemListener(this);
        jckIsUnitsVirtualApplying.addItemListener(this);
        jckIsNetContentApplying.addItemListener(this);
        jckIsNetContentUnitaryApplying.addItemListener(this);
        jckIsLengthApplying.addItemListener(this);
        jckIsSurfaceApplying.addItemListener(this);
        jckIsVolumeApplying.addItemListener(this);
        jckIsMassApplying.addItemListener(this);
        jckIsItemRefRequired.addItemListener(this);

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
            jcbFkItemCategoryId.requestFocus();
        }
    }

    private void renderItemGenericShortSettings() {
        if (jckIsItemShortApplying.isSelected()) {
            jtfItemGenericShort.setEnabled(true);
            jbCopyItemGeneric.setEnabled(true);
        }
        else {
            jtfItemGenericShort.setEnabled(false);
            jbCopyItemGeneric.setEnabled(false);

            moFieldItemGenericShort.setFieldValue("");
        }
    }

    private void renderItemKeySettings() {
        if (jckIsItemKeyApplying.isSelected()) {
            jckIsItemKeyAutomatic.setEnabled(true);
        }
        else {
            jckIsItemKeyAutomatic.setEnabled(false);

            moFieldIsItemKeyAutomatic.setFieldValue(false);
        }

        renderItemKeyAutomaticSettings();
        renderItemLineSettings();
    }

    private void renderItemKeyAutomaticSettings() {
        if (jckIsItemKeyAutomatic.isSelected()) {
            jtfCode.setEnabled(true);
            jtfKeyOrdinaryPosItemGeneric.setEnabled(true);
            jtfKeyOrdinaryPosBrand.setEnabled(true);
            jtfKeyOrdinaryPosManufacturer.setEnabled(true);
            jtfKeyOrdinaryPosCode.setEnabled(true);
        }
        else {
            jtfCode.setEnabled(false);
            jtfKeyOrdinaryPosItemGeneric.setEnabled(false);
            jtfKeyOrdinaryPosBrand.setEnabled(false);
            jtfKeyOrdinaryPosManufacturer.setEnabled(false);
            jtfKeyOrdinaryPosCode.setEnabled(false);

            moFieldCode.setFieldValue("");
            moFieldKeyOrdinaryPosItemGeneric.setFieldValue(0);
            moFieldKeyOrdinaryPosBrand.setFieldValue(0);
            moFieldKeyOrdinaryPosManufacturer.setFieldValue(0);
            moFieldKeyOrdinaryPosCode.setFieldValue(0);
        }

        renderItemLineSettings();
    }

    private void renderItemLineSettings() {
        if (jckIsItemLineApplying.isSelected()) {
            jtfNamingLinePosItemGeneric.setEnabled(true);
            jtfNamingLinePosItemLine.setEnabled(true);
            jtfNamingLinePosBrand.setEnabled(true);
            jtfNamingLinePosManufacturer.setEnabled(true);
        }
        else {
            jtfNamingLinePosItemGeneric.setEnabled(false);
            jtfNamingLinePosItemLine.setEnabled(false);
            jtfNamingLinePosBrand.setEnabled(false);
            jtfNamingLinePosManufacturer.setEnabled(false);

            moFieldNamingLinePosItemGeneric.setFieldValue(0);
            moFieldNamingLinePosItemLine.setFieldValue(0);
            moFieldNamingLinePosBrand.setFieldValue(0);
            moFieldNamingLinePosManufacturer.setFieldValue(0);
        }

        if (jckIsItemLineApplying.isSelected() && jckIsItemKeyAutomatic.isSelected()) {
            jtfKeyLinePosItemGeneric.setEnabled(true);
            jtfKeyLinePosItemLine.setEnabled(true);
            jtfKeyLinePosBrand.setEnabled(true);
            jtfKeyLinePosManufacturer.setEnabled(true);
        }
        else {
            jtfKeyLinePosItemGeneric.setEnabled(false);
            jtfKeyLinePosItemLine.setEnabled(false);
            jtfKeyLinePosBrand.setEnabled(false);
            jtfKeyLinePosManufacturer.setEnabled(false);

            moFieldKeyLinePosItemGeneric.setFieldValue(0);
            moFieldKeyLinePosItemLine.setFieldValue(0);
            moFieldKeyLinePosBrand.setFieldValue(0);
            moFieldKeyLinePosManufacturer.setFieldValue(0);
        }
    }

    private void renderIsInventoriable() {
        if (jckIsInventoriable.isSelected()) {
            jckIsLotApplying.setEnabled(mbIsLotApplyingBySystem);
            jcbFkSerialNumberTypeId.setEnabled(true);
        }
        else {
            jckIsLotApplying.setEnabled(false);
            jcbFkSerialNumberTypeId.setEnabled(false);

            moFieldFkSerialNumberTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMS_TP_SNR_NA });
        }

        if (!jckIsLotApplying.isEnabled()) {
            moFieldIsLotApplying.setFieldValue(false);
        }

        itemStateChangedFkSerialNumberTypeId();
        renderIsLotApplying();
    }

    private void renderIsLotApplying() {
        if (jckIsLotApplying.isSelected()) {
            jtfDaysForExpiration.setEnabled(true);
        }
        else {
            jtfDaysForExpiration.setEnabled(false);
        }

        if (!jtfDaysForExpiration.isEnabled()) {
            moFieldDaysForExpiration.setFieldValue(0);
        }
    }

    private void renderUnitUnitsContainedTypeSettings() {
        if (jckIsUnitsContainedApplying.isSelected()) {
            jcbFkUnitUnitsContainedTypeId.setEnabled(true);
        }
        else if (!jckIsUnitsContainedApplying.isSelected()) {
            jcbFkUnitUnitsContainedTypeId.setEnabled(false);
            moFieldFkUnitUnitsContainedTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_UNIT_NA });
        }
    }

    private void renderUnitUnitsVirtualTypeSettings() {
        if (jckIsUnitsVirtualApplying.isSelected()) {
            jcbFkUnitUnitsVirtualTypeId.setEnabled(true);
        }
        else if (!jckIsUnitsVirtualApplying.isSelected()) {
            jcbFkUnitUnitsVirtualTypeId.setEnabled(false);
            moFieldFkUnitUnitsVirtualTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_UNIT_NA });
        }
    }

    private void renderUnitNetContentTypeSettings() {
        if (jckIsNetContentApplying.isSelected()) {
            jcbFkUnitNetContentTypeId.setEnabled(true);
        }
        else {
            jcbFkUnitNetContentTypeId.setEnabled(false);
            moFieldFkUnitNetContentTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_UNIT_NA });
        }
    }

    private void renderUnitNetContentUnitaryTypeSettings() {
        if (jckIsNetContentUnitaryApplying.isSelected()) {
            jcbFkUnitNetContentUnitaryTypeId.setEnabled(true);
        }
        else {
            jcbFkUnitNetContentUnitaryTypeId.setEnabled(false);
            moFieldFkUnitNetContentUnitaryTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMU_TP_UNIT_NA });
        }
    }

    private void renderLength() {
        if (jckIsLengthApplying.isSelected()) {
            jckIsLengthUnitaryApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsLengthUnitaryApplying());
            jckIsLengthVariable.setEnabled(miClient.getSessionXXX().getParamsErp().getIsLengthVariable());
        }
        else {
            jckIsLengthUnitaryApplying.setEnabled(false);
            jckIsLengthVariable.setEnabled(false);
        }

        if (!jckIsLengthUnitaryApplying.isEnabled()) {
            moFieldIsLengthUnitaryApplying.setFieldValue(false);
        }
        if (!jckIsLengthVariable.isEnabled()) {
            moFieldIsLengthVariable.setFieldValue(false);
        }
    }

    private void renderSurface() {
        if (jckIsSurfaceApplying.isSelected()) {
            jckIsSurfaceUnitaryApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsSurfaceUnitaryApplying());
            jckIsSurfaceVariable.setEnabled(miClient.getSessionXXX().getParamsErp().getIsSurfaceVariable());
        }
        else {
            jckIsSurfaceUnitaryApplying.setEnabled(false);
            jckIsSurfaceVariable.setEnabled(false);
        }

        if (!jckIsSurfaceUnitaryApplying.isEnabled()) {
            moFieldIsSurfaceUnitaryApplying.setFieldValue(false);
        }
        if (!jckIsSurfaceVariable.isEnabled()) {
            moFieldIsSurfaceVariable.setFieldValue(false);
        }
    }

    private void renderVolume() {
        if (jckIsVolumeApplying.isSelected()) {
            jckIsVolumeUnitaryApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsVolumeUnitaryApplying());
            jckIsVolumeVariable.setEnabled(miClient.getSessionXXX().getParamsErp().getIsVolumeVariable());
        }
        else {
            jckIsVolumeUnitaryApplying.setEnabled(false);
            jckIsVolumeVariable.setEnabled(false);
        }

        if (!jckIsVolumeUnitaryApplying.isEnabled()) {
            moFieldIsVolumeUnitaryApplying.setFieldValue(false);
        }
        if (!jckIsVolumeVariable.isEnabled()) {
            moFieldIsVolumeVariable.setFieldValue(false);
        }
    }

    private void renderMass() {
        if (jckIsMassApplying.isSelected()) {
            jckIsMassUnitaryApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsMassUnitaryApplying());
            jckIsMassVariable.setEnabled(miClient.getSessionXXX().getParamsErp().getIsMassVariable());
        }
        else {
            jckIsMassUnitaryApplying.setEnabled(false);
            jckIsMassVariable.setEnabled(false);
        }

        if (!jckIsMassUnitaryApplying.isEnabled()) {
            moFieldIsMassUnitaryApplying.setFieldValue(false);
        }
        if (!jckIsMassVariable.isEnabled()) {
            moFieldIsMassVariable.setFieldValue(false);
        }
    }

    private void renderIsItemRefRequired() {
        if (jckIsItemRefRequired.isSelected()) {
            jcbFkDefaultItemRefId_n.setEnabled(true);
            jbFkDefaultItemRefId_n.setEnabled(true);
        }
        else {
            jcbFkDefaultItemRefId_n.setEnabled(false);
            jbFkDefaultItemRefId_n.setEnabled(false);

            moFieldFkDefaultItemRefeferenceId_n.setFieldValue(null);
        }
    }

    private void renderBizArea() {
        if (jltBizAreaAvailable.getModel().getSize() == 1) {
            actionBizAreaAssign();

            jltBizAreaAvailable.setEnabled(false);
            jltBizAreaAsignated.setEnabled(false);

            jbBizAreaAssign.setEnabled(false);
            jbBizAreaAssignAll.setEnabled(false);
            jbBizAreaRemove.setEnabled(false);
            jbBizAreaRemoveAll.setEnabled(false);
        }
        else {
            jltBizAreaAvailable.setEnabled(true);
            jltBizAreaAsignated.setEnabled(true);

            jbBizAreaAssign.setEnabled(true);
            jbBizAreaAssignAll.setEnabled(true);
            jbBizAreaRemove.setEnabled(true);
            jbBizAreaRemoveAll.setEnabled(true);
        }
    }

    private void readErpParams() {
        if (miClient.getSessionXXX().getParamsErp().getIsItemShortApplying()) {
            jckIsItemShortApplying.setEnabled(true);
            jtfItemGenericShort.setEnabled(true);
            jbCopyItemGeneric.setEnabled(true);
        }
        else {
            jckIsItemShortApplying.setEnabled(false);
            jtfItemGenericShort.setEnabled(false);
            jbCopyItemGeneric.setEnabled(false);
        }

        jckIsItemLineApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsItemNameWithVarieties());
        jckIsItemNameEditable.setEnabled(miClient.getSessionXXX().getParamsErp().getIsItemNameEditable());
        jckIsItemKeyApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsItemKeyApplying());
        jckIsItemKeyAutomatic.setEnabled(miClient.getSessionXXX().getParamsErp().getIsItemKeyAutomatic());
        jckIsItemKeyEditable.setEnabled(miClient.getSessionXXX().getParamsErp().getIsItemKeyEditable());
        jckIsInventoriable.setEnabled(miClient.getSessionXXX().getParamsErp().getIsInventoriable());
        jckIsBulk.setEnabled(miClient.getSessionXXX().getParamsErp().getIsBulk());
        jckIsUnitsContainedApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsUnitsContainedApplying());
        jckIsUnitsVirtualApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsUnitsVirtualApplying());
        jckIsUnitsPackageApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsUnitsPackageApplying());
        jckIsNetContentApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsNetContentApplying());
        jckIsNetContentUnitaryApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsNetContentUnitaryApplying());
        jckIsNetContentVariable.setEnabled(miClient.getSessionXXX().getParamsErp().getIsNetContentVariable());
        jckIsLengthApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsLengthApplying());
        jckIsSurfaceApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsSurfaceApplying());
        jckIsVolumeApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsVolumeApplying());
        jckIsMassApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsMassApplying());
        jckIsWeightGrossApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsWeightGrossApplying());
        jckIsWeightDeliveryApplying.setEnabled(miClient.getSessionXXX().getParamsErp().getIsWeightDeliveryApplying());

        mbIsLotApplyingBySystem = miClient.getSessionXXX().getParamsErp().getIsLotApplying();

        renderIsInventoriable();
        renderUnitUnitsContainedTypeSettings();
        renderUnitUnitsVirtualTypeSettings();
        renderUnitNetContentTypeSettings();
        renderUnitNetContentUnitaryTypeSettings();
        renderLength();
        renderSurface();
        renderVolume();
        renderMass();
        renderIsItemRefRequired();
    }

    private void readItemGroupSettings() {
        SDataItemGroup oItemGroup = new SDataItemGroup();

        if (jcbFkItemGroupId.getSelectedIndex() <= 0) {
                jckIsFreeDiscountUnitary.setEnabled(false);
                jckIsFreeDiscountEntry.setEnabled(false);
                jckIsFreeDiscountDoc.setEnabled(false);
                jckIsFreePrice.setEnabled(false);
                jckIsFreeDiscount.setEnabled(false);
                jckIsFreeCommissions.setEnabled(false);

                moFieldIsFreeDiscountUnitary.setFieldValue(false);
                moFieldIsFreeDiscountEntry.setFieldValue(false);
                moFieldIsFreeDiscountDoc.setFieldValue(false);
                moFieldIsFreePrice.setFieldValue(false);
                moFieldIsFreeDiscount.setFieldValue(false);
                moFieldIsFreeCommissions.setFieldValue(false);
        }
        else {
            oItemGroup = (SDataItemGroup) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_IGRP, moFieldFkItemGroupId.getKey(), SLibConstants.EXEC_MODE_SILENT);

            jckIsFreeDiscountUnitary.setEnabled(oItemGroup.getDbmsIsFreeDiscountUnitary());
            moFieldIsFreeDiscountUnitary.setFieldValue(oItemGroup.getDbmsIsFreeDiscountUnitary());

            jckIsFreeDiscountEntry.setEnabled(oItemGroup.getDbmsIsFreeDiscountEntry());
            moFieldIsFreeDiscountEntry.setFieldValue(oItemGroup.getDbmsIsFreeDiscountEntry());

            jckIsFreeDiscountDoc.setEnabled(oItemGroup.getDbmsIsFreeDiscountDoc());
            moFieldIsFreeDiscountDoc.setFieldValue(oItemGroup.getDbmsIsFreeDiscountDoc());

            jckIsFreePrice.setEnabled(oItemGroup.getDbmsIsFreePrice());
            moFieldIsFreePrice.setFieldValue(oItemGroup.getDbmsIsFreePrice());

            jckIsFreeDiscount.setEnabled(oItemGroup.getDbmsIsFreeDiscount());
            moFieldIsFreeDiscount.setFieldValue(oItemGroup.getDbmsIsFreeDiscount());

            jckIsFreeCommissions.setEnabled(oItemGroup.getDbmsIsFreeCommissions());
            moFieldIsFreeCommissions.setFieldValue(oItemGroup.getDbmsIsFreeCommissions());
        }
    }

   private int comparePositions(int[] data) {
       int position = 0;
       boolean isEqual = false;

       for (int i = 0; i < data.length; i++) {
           position = data[i];
           for (int j = i+1; j < data.length; j++) {
               if (position == data[j]) {
                   isEqual = true;
                   return position;
               }
           }
       }

       return isEqual ? position : 0;
   }

    /*
     * Action listener methods:
     */

    private void actionCopyItemGeneric() {
        if (jtfItemGeneric.getText().length() > moFieldItemGenericShort.getLengthMax()) {
            jtfItemGenericShort.setText(jtfItemGeneric.getText().substring(0, moFieldItemGenericShort.getLengthMax() - 1));
        }
        else {
            jtfItemGenericShort.setText(jtfItemGeneric.getText());
        }
    }

    private void actionBizAreaAssign() {
        int index = jltBizAreaAvailable.getSelectedIndex();

        if (index != -1) {
            SFormUtilities.addListItem(jltBizAreaAsignated, SFormUtilities.removeListSelectedItem(jltBizAreaAvailable));

            if (jltBizAreaAvailable.getModel().getSize() > 0) {
                jltBizAreaAvailable.setSelectedIndex(index < jltBizAreaAvailable.getModel().getSize() ? index : jltBizAreaAvailable.getModel().getSize() - 1);
            }

            jltBizAreaAsignated.setSelectedIndex(jltBizAreaAsignated.getModel().getSize() - 1);
        }
    }

    private void actionBizAreaAssignAll() {
        if (jltBizAreaAvailable.getModel().getSize() > 0) {
            SFormUtilities.addListItems(jltBizAreaAsignated, SFormUtilities.removeListAllItems(jltBizAreaAvailable));
            jltBizAreaAsignated.setSelectedIndex(jltBizAreaAsignated.getModel().getSize() - 1);
        }
    }

    private void actionBizAreaRemove() {
        int index = jltBizAreaAsignated.getSelectedIndex();

        if (index != -1) {
            SFormUtilities.addListItem(jltBizAreaAvailable, SFormUtilities.removeListSelectedItem(jltBizAreaAsignated));

            if (jltBizAreaAsignated.getModel().getSize() > 0) {
                jltBizAreaAsignated.setSelectedIndex(index < jltBizAreaAsignated.getModel().getSize() ? index : jltBizAreaAsignated.getModel().getSize() - 1);
            }

            jltBizAreaAvailable.setSelectedIndex(jltBizAreaAvailable.getModel().getSize() - 1);
        }
    }

    private void actionBizAreaRemoveAll() {
        if (jltBizAreaAsignated.getModel().getSize() > 0) {
            SFormUtilities.addListItems(jltBizAreaAvailable, SFormUtilities.removeListAllItems(jltBizAreaAsignated));
            jltBizAreaAvailable.setSelectedIndex(jltBizAreaAvailable.getModel().getSize() - 1);
        }
    }

    private void actionDbmsFkItemFamilyId() {
        miClient.pickOption(SDataConstants.ITMU_IFAM, moFieldFkDbmsItemFamilyId, null);
    }

    private void actionFkItemGroupId() {
        miClient.pickOption(SDataConstants.ITMU_IGRP, moFieldFkItemGroupId, moFieldFkDbmsItemFamilyId.getKeyAsIntArray());
    }

    private void actionFkDefaultItemRefId_n() {
        miClient.pickOption(SDataConstants.ITMX_ITEM_IOG, moFieldFkDefaultItemRefeferenceId_n, null);
    }

    private void actionFkAdministrativeConceptTypeId() {
        miClient.pickOption(SDataConstants.FINU_TP_ADM_CPT, moFieldFkAdministrativeConceptTypeId, moFieldFkAdministrativeConceptTypeId.getKeyAsIntArray());
    }

    private void actionFkTaxableConceptTypeId() {
        miClient.pickOption(SDataConstants.FINU_TP_TAX_CPT, moFieldFkTaxableConceptTypeId, moFieldFkTaxableConceptTypeId.getKeyAsIntArray());
    }

    private void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getTabbedPaneIndex() >=  0) {
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

    /*
     * Focus listener methods:
     */

    private void focusLostItemGeneric() {
        if (jtfItemGenericShort.isEnabled() && SLibUtilities.textTrim(jtfItemGenericShort.getText()).length() == 0) {
            actionCopyItemGeneric();
        }
    }

    /*
     * Item listener methods:
     */

    private void itemStateChangedFkItemGroupId() {
        readItemGroupSettings();
    }

    private void itemStateChangedFkSerialNumberTypeId() {
        if (jcbFkSerialNumberTypeId.getSelectedIndex() > 0 && moFieldFkSerialNumberTypeId.getKeyAsIntArray()[0] != SDataConstantsSys.ITMS_TP_SNR_NA) {
            jtfSerialNumber.setEnabled(true);
            jtfSerialNumberFormat.setEnabled(true);
        }
        else {
            jtfSerialNumber.setEnabled(false);
            jtfSerialNumberFormat.setEnabled(false);

            moFieldSerialNumber.setFieldValue("");
            moFieldSerialNumberFormat.setFieldValue("");
        }
    }

    private void itemStateChangedIsItemShortApplying() {
        renderItemGenericShortSettings();
    }

    private void itemStateChangedIsItemKeyApplying() {
        renderItemKeySettings();
    }

    private void itemStateChangedIsItemKeyAutomatic() {
        renderItemKeyAutomaticSettings();
    }

    private void itemStateChangedIsItemLineApplying() {
        renderItemLineSettings();
    }

    private void itemStateChangedIsInventoriable() {
        renderIsInventoriable();
    }

    private void itemStateChangedIsLotApplying() {
        renderIsLotApplying();
    }

    private void itemStateChangedIsUnitsContainedApplying() {
        renderUnitUnitsContainedTypeSettings();
    }

    private void itemStateChangedIsUnitsVirtualApplying() {
        renderUnitUnitsVirtualTypeSettings();
    }

    public void itemStateChangedIsNetContentApplying() {
        renderUnitNetContentTypeSettings();
    }

    private void itemStateChangedIsNetContentUnitaryApplying() {
        renderUnitNetContentUnitaryTypeSettings();
    }

    private void itemStateChangedIsLenghtApplying() {
        renderLength();
    }

    public void itemStateChangedIsSurfaceApplying() {
        renderSurface();
    }

    private void itemStateChangedIsVolumeApplying() {
        renderVolume();
    }

    private void itemStateChangedIsMassApplying() {
        renderMass();
    }

    private void itemStateChangedIsItemRefRequired() {
        renderIsItemRefRequired();
    }

    /*
     * Public listener methods:
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JButton jbBizAreaAssign;
    private javax.swing.JButton jbBizAreaAssignAll;
    private javax.swing.JButton jbBizAreaRemove;
    private javax.swing.JButton jbBizAreaRemoveAll;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCopyItemGeneric;
    private javax.swing.JButton jbDbmsFkItemFamilyId;
    private javax.swing.JButton jbFkAdministrativeConceptTypeId;
    private javax.swing.JButton jbFkDefaultItemRefId_n;
    private javax.swing.JButton jbFkItemGroupId;
    private javax.swing.JButton jbFkTaxableConceptTypeId;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkAdministrativeConceptTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkDbmsItemFamilyId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkDefaultItemRefId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemCategoryId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemClassId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemGroupId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkSerialNumberTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTaxableConceptTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitNetContentTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitNetContentUnitaryTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitUnitsContainedTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkUnitUnitsVirtualTypeId;
    private javax.swing.JCheckBox jckIsBulk;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsFreeCommissions;
    private javax.swing.JCheckBox jckIsFreeDiscount;
    private javax.swing.JCheckBox jckIsFreeDiscountDoc;
    private javax.swing.JCheckBox jckIsFreeDiscountEntry;
    private javax.swing.JCheckBox jckIsFreeDiscountUnitary;
    private javax.swing.JCheckBox jckIsFreePrice;
    private javax.swing.JCheckBox jckIsInventoriable;
    private javax.swing.JCheckBox jckIsItemKeyApplying;
    private javax.swing.JCheckBox jckIsItemKeyAutomatic;
    private javax.swing.JCheckBox jckIsItemKeyEditable;
    private javax.swing.JCheckBox jckIsItemLineApplying;
    private javax.swing.JCheckBox jckIsItemNameEditable;
    private javax.swing.JCheckBox jckIsItemRefRequired;
    private javax.swing.JCheckBox jckIsItemShortApplying;
    private javax.swing.JCheckBox jckIsLengthApplying;
    private javax.swing.JCheckBox jckIsLengthUnitaryApplying;
    private javax.swing.JCheckBox jckIsLengthVariable;
    private javax.swing.JCheckBox jckIsLotApplying;
    private javax.swing.JCheckBox jckIsMassApplying;
    private javax.swing.JCheckBox jckIsMassUnitaryApplying;
    private javax.swing.JCheckBox jckIsMassVariable;
    private javax.swing.JCheckBox jckIsNetContentApplying;
    private javax.swing.JCheckBox jckIsNetContentUnitaryApplying;
    private javax.swing.JCheckBox jckIsNetContentVariable;
    private javax.swing.JCheckBox jckIsSurfaceApplying;
    private javax.swing.JCheckBox jckIsSurfaceUnitaryApplying;
    private javax.swing.JCheckBox jckIsSurfaceVariable;
    private javax.swing.JCheckBox jckIsUnitsContainedApplying;
    private javax.swing.JCheckBox jckIsUnitsPackageApplying;
    private javax.swing.JCheckBox jckIsUnitsVirtualApplying;
    private javax.swing.JCheckBox jckIsVolumeApplying;
    private javax.swing.JCheckBox jckIsVolumeUnitaryApplying;
    private javax.swing.JCheckBox jckIsVolumeVariable;
    private javax.swing.JCheckBox jckIsWeightDeliveryApplying;
    private javax.swing.JCheckBox jckIsWeightGrossApplying;
    private javax.swing.JLabel jlBizAreaAsignated;
    private javax.swing.JLabel jlBizAreaAvailable;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlDaysForExpiration;
    private javax.swing.JLabel jlDbmsFkItemFamilyId;
    private javax.swing.JLabel jlFkAdministrativeConceptTypeId;
    private javax.swing.JLabel jlFkDefaultItemRefId_n;
    private javax.swing.JLabel jlFkItemCategoryId;
    private javax.swing.JLabel jlFkItemClassId;
    private javax.swing.JLabel jlFkItemGroupId;
    private javax.swing.JLabel jlFkItemTypeId;
    private javax.swing.JLabel jlFkSerialNumberTypeId;
    private javax.swing.JLabel jlFkTaxableConceptTypeId;
    private javax.swing.JLabel jlFkUnitTypeId;
    private javax.swing.JLabel jlItemGeneric;
    private javax.swing.JLabel jlItemGenericShort;
    private javax.swing.JLabel jlKeyLinePosBrand;
    private javax.swing.JLabel jlKeyLinePosItemGeneric;
    private javax.swing.JLabel jlKeyLinePosItemLine;
    private javax.swing.JLabel jlKeyLinePosManufacturer;
    private javax.swing.JLabel jlKeyOrdinaryPosBrand;
    private javax.swing.JLabel jlKeyOrdinaryPosCode;
    private javax.swing.JLabel jlKeyOrdinaryPosItemGeneric;
    private javax.swing.JLabel jlKeyOrdinaryPosManufacturer;
    private javax.swing.JLabel jlNamingLinePosBrand;
    private javax.swing.JLabel jlNamingLinePosItemGeneric;
    private javax.swing.JLabel jlNamingLinePosItemLine;
    private javax.swing.JLabel jlNamingLinePosManufacturer;
    private javax.swing.JLabel jlNamingOrdinaryPosBrand;
    private javax.swing.JLabel jlNamingOrdinaryPosItemGeneric;
    private javax.swing.JLabel jlNamingOrdinaryPosManufacturer;
    private javax.swing.JLabel jlNamingOrdinaryPosName;
    private javax.swing.JLabel jlNamingOrdinaryPosPresentation;
    private javax.swing.JLabel jlSerialNumber;
    private javax.swing.JLabel jlSerialNumberExample;
    private javax.swing.JLabel jlSerialNumberFormat;
    private javax.swing.JLabel jlSerialNumberFormatExample;
    private javax.swing.JLabel jlSurplusPercentage;
    private javax.swing.JList<SFormComponentItem> jltBizAreaAsignated;
    private javax.swing.JList<SFormComponentItem> jltBizAreaAvailable;
    private javax.swing.JPanel jpCommand;
    private javax.swing.JPanel jpProperties;
    private javax.swing.JPanel jpProperties1;
    private javax.swing.JPanel jpProperties11;
    private javax.swing.JPanel jpProperties111;
    private javax.swing.JPanel jpProperties1111;
    private javax.swing.JPanel jpProperties11111;
    private javax.swing.JPanel jpProperties11112;
    private javax.swing.JPanel jpProperties1112;
    private javax.swing.JPanel jpProperties12;
    private javax.swing.JPanel jpProperties121;
    private javax.swing.JPanel jpProperties1211;
    private javax.swing.JPanel jpProperties1212;
    private javax.swing.JPanel jpProperties1213;
    private javax.swing.JPanel jpProperties1214;
    private javax.swing.JPanel jpProperties1215;
    private javax.swing.JPanel jpProperties122;
    private javax.swing.JPanel jpProperties1221;
    private javax.swing.JPanel jpProperties2;
    private javax.swing.JPanel jpProperties21;
    private javax.swing.JPanel jpProperties211;
    private javax.swing.JPanel jpProperties212;
    private javax.swing.JPanel jpProperties2121;
    private javax.swing.JPanel jpProperties213;
    private javax.swing.JPanel jpProperties22;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpRegistry1;
    private javax.swing.JPanel jpRegistry11;
    private javax.swing.JPanel jpRegistry111;
    private javax.swing.JPanel jpRegistry112;
    private javax.swing.JPanel jpRegistry113;
    private javax.swing.JPanel jpRegistry12;
    private javax.swing.JPanel jpRegistry121;
    private javax.swing.JPanel jpRegistry122;
    private javax.swing.JPanel jpRegistry123;
    private javax.swing.JPanel jpRegistry2;
    private javax.swing.JPanel jpRegistry21;
    private javax.swing.JPanel jpRegistry211;
    private javax.swing.JPanel jpRegistry212;
    private javax.swing.JPanel jpRegistry213;
    private javax.swing.JPanel jpRegistry214;
    private javax.swing.JPanel jpRegistry215;
    private javax.swing.JPanel jpRegistry216;
    private javax.swing.JPanel jpRegistry217;
    private javax.swing.JPanel jpRegistry218;
    private javax.swing.JPanel jpRegistry22;
    private javax.swing.JPanel jpRegistry221;
    private javax.swing.JPanel jpRegistry2211;
    private javax.swing.JPanel jpRegistry2212;
    private javax.swing.JPanel jpRegistry2213;
    private javax.swing.JPanel jpRegistry2214;
    private javax.swing.JPanel jpRegistry2215;
    private javax.swing.JPanel jpRegistry2216;
    private javax.swing.JPanel jpRegistry2217;
    private javax.swing.JPanel jpRegistry2218;
    private javax.swing.JPanel jpRegistry3;
    private javax.swing.JPanel jpRegistry31;
    private javax.swing.JPanel jpRegistry32;
    private javax.swing.JPanel jpRegistryCenter;
    private javax.swing.JPanel jpRegistryNorth;
    private javax.swing.JScrollPane jspBizAreaAsignated;
    private javax.swing.JScrollPane jsptBizAreaAvailable;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfDaysForExpiration;
    private javax.swing.JTextField jtfItemGeneric;
    private javax.swing.JTextField jtfItemGenericShort;
    private javax.swing.JTextField jtfKeyLinePosBrand;
    private javax.swing.JTextField jtfKeyLinePosItemGeneric;
    private javax.swing.JTextField jtfKeyLinePosItemLine;
    private javax.swing.JTextField jtfKeyLinePosManufacturer;
    private javax.swing.JTextField jtfKeyOrdinaryPosBrand;
    private javax.swing.JTextField jtfKeyOrdinaryPosCode;
    private javax.swing.JTextField jtfKeyOrdinaryPosItemGeneric;
    private javax.swing.JTextField jtfKeyOrdinaryPosManufacturer;
    private javax.swing.JTextField jtfNamingLinePosBrand;
    private javax.swing.JTextField jtfNamingLinePosItemGeneric;
    private javax.swing.JTextField jtfNamingLinePosItemLine;
    private javax.swing.JTextField jtfNamingLinePosManufacturer;
    private javax.swing.JTextField jtfNamingOrdinaryPosBrand;
    private javax.swing.JTextField jtfNamingOrdinaryPosItemGeneric;
    private javax.swing.JTextField jtfNamingOrdinaryPosManufacturer;
    private javax.swing.JTextField jtfNamingOrdinaryPosName;
    private javax.swing.JTextField jtfNamingOrdinaryPosPresentation;
    private javax.swing.JTextField jtfSerialNumber;
    private javax.swing.JTextField jtfSerialNumberFormat;
    private javax.swing.JTextField jtfSurplusPercentage;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        moItemGeneric = null;
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moItemGeneric = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        moComboBoxFamilyItem.reset();
        moComboBoxCategoryItem.reset();
        moFieldFkSerialNumberTypeId.setFieldValue(new int[] { SDataConstantsSys.ITMS_TP_SNR_NA });
        moFieldNamingOrdinaryPosName.setFieldValue(1);
        moFieldNamingOrdinaryPosPresentation.setFieldValue(2);
        moFieldFkDefaultItemRefeferenceId_n.setFieldValue(null);
        moFieldFkAdministrativeConceptTypeId.setFieldValue(new int[] { SDataConstantsSys.FINU_TP_ADM_CPT_NA });
        moFieldFkTaxableConceptTypeId.setFieldValue(new int[] { SDataConstantsSys.FINU_TP_TAX_CPT_NA });
        jltBizAreaAsignated.setListData(new Vector<SFormComponentItem>());

        readErpParams();
        readItemGroupSettings();
        renderItemGenericShortSettings();
        renderItemKeySettings();
        renderItemKeyAutomaticSettings();
        renderItemLineSettings();
        renderIsInventoriable();
        renderUnitNetContentTypeSettings();
        renderUnitNetContentUnitaryTypeSettings();
        renderUnitUnitsContainedTypeSettings();
        renderUnitUnitsVirtualTypeSettings();
        renderLength();
        renderSurface();
        renderVolume();
        renderMass();
        renderIsItemRefRequired();
        renderBizArea();

        jckIsDeleted.setEnabled(false);

        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;

        moComboBoxCategoryItem.clear();
        moComboBoxCategoryItem.addComboBox(SDataConstants.ITMS_CT_ITEM, jcbFkItemCategoryId);
        moComboBoxCategoryItem.addComboBox(SDataConstants.ITMS_CL_ITEM, jcbFkItemClassId);
        moComboBoxCategoryItem.addComboBox(SDataConstants.ITMS_TP_ITEM, jcbFkItemTypeId);

        moComboBoxFamilyItem.clear();
        moComboBoxFamilyItem.addComboBox(SDataConstants.ITMU_IFAM, jcbFkDbmsItemFamilyId, jbDbmsFkItemFamilyId);
        moComboBoxFamilyItem.addComboBox(SDataConstants.ITMU_IGRP, jcbFkItemGroupId, jbFkItemGroupId);

        SFormUtilities.populateComboBox(miClient, jcbFkUnitTypeId, SDataConstants.ITMU_TP_UNIT);
        SFormUtilities.populateComboBox(miClient, jcbFkUnitUnitsContainedTypeId, SDataConstants.ITMU_TP_UNIT);
        SFormUtilities.populateComboBox(miClient, jcbFkUnitUnitsVirtualTypeId, SDataConstants.ITMU_TP_UNIT);
        SFormUtilities.populateComboBox(miClient, jcbFkUnitNetContentTypeId, SDataConstants.ITMU_TP_UNIT);
        SFormUtilities.populateComboBox(miClient, jcbFkUnitNetContentUnitaryTypeId, SDataConstants.ITMU_TP_UNIT);
        SFormUtilities.populateComboBox(miClient, jcbFkSerialNumberTypeId, SDataConstants.ITMS_TP_SNR);
        SFormUtilities.populateComboBox(miClient, jcbFkDefaultItemRefId_n, SDataConstants.ITMU_ITEM);
        SFormUtilities.populateComboBox(miClient, jcbFkAdministrativeConceptTypeId, SDataConstants.FINU_TP_ADM_CPT);
        SFormUtilities.populateComboBox(miClient, jcbFkTaxableConceptTypeId, SDataConstants.FINU_TP_TAX_CPT);

        SFormUtilities.populateList(miClient, jltBizAreaAvailable, SDataConstants.BPSU_BA);
    }

    @Override
    public SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();
        int[] posNameOrd = new int[] { moFieldNamingOrdinaryPosItemGeneric.getInteger(), moFieldNamingOrdinaryPosBrand.getInteger(),
            moFieldNamingOrdinaryPosManufacturer.getInteger(), moFieldNamingOrdinaryPosName.getInteger(), moFieldNamingOrdinaryPosPresentation.getInteger() };
        int[] posNameLine = new int[] { moFieldNamingLinePosItemGeneric.getInteger(), moFieldNamingLinePosItemLine.getInteger(),
            moFieldNamingLinePosBrand.getInteger(), moFieldNamingLinePosManufacturer.getInteger() };
        int[] posKeyOrd = new int[] { moFieldKeyOrdinaryPosItemGeneric.getInteger(), moFieldKeyOrdinaryPosBrand.getInteger(),
            moFieldKeyOrdinaryPosManufacturer.getInteger(), moFieldKeyOrdinaryPosCode.getInteger() };
        int[] posKeyLine = new int[] { moFieldKeyLinePosItemGeneric.getInteger(), moFieldKeyLinePosItemLine.getInteger(), moFieldKeyLinePosBrand.getInteger(),
            moFieldKeyLinePosManufacturer.getInteger() };

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            Object[] oParamsIn = new Object[] { moItemGeneric == null ? 0 : moItemGeneric.getPkItemGenericId(), moFieldItemGeneric.getString() };

            if (SDataUtilities.callProcedureVal(miClient, SProcConstants.ITMU_IGEN_VAL, oParamsIn, SLibConstants.EXEC_MODE_VERBOSE) > 0) {
                if (miClient.showMsgBoxConfirm("El valor del campo '" + jlItemGeneric.getText() + "' ya existe, ¿desea conservalo? ") == JOptionPane.NO_OPTION) {
                    validation.setTabbedPaneIndex(0);
                    validation.setComponent(jtfItemGeneric);
                    validation.setIsError(true);
                }
            }
            if (!validation.getIsError()) {
                if (jckIsItemShortApplying.isSelected() && jtfItemGenericShort.getText().length() == 0) {
                    validation.setTabbedPaneIndex(0);
                    validation.setComponent(jtfItemGenericShort);
                    validation.setMessage("Se debe ingresar un valor para el campo '" + jlItemGenericShort.getText() + "'.");
                }
                else if (jckIsItemKeyApplying.isSelected() && jckIsItemKeyAutomatic.isSelected() && jtfCode.getText().length() == 0) {
                    validation.setTabbedPaneIndex(0);
                    validation.setComponent(jtfCode);
                    validation.setMessage("Se debe ingresar un valor para el campo '" + jlCode.getText() + "'.");
                }
                else if (jcbFkUnitTypeId.getSelectedIndex() <= 0 || moFieldFkUnitTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.ITMU_TP_UNIT_NA) {
                    validation.setTabbedPaneIndex(0);
                    validation.setComponent(jcbFkUnitTypeId);
                    validation.setMessage("Se debe seleccionar una opción para el campo '" + jlFkUnitTypeId.getText() + "'.");
                }
                /*
                else if (jckIsUnitsContainedApplying.isSelected() && moFieldFkUnitUnitsContainedTypeId.getKeyAsIntArray()[0] == 0) {
                   validation.setTabbedPaneIndex(0);
                   validation.setComponent(jcbFkUnitUnitsContainedTypeId);
                   validation.setMessage("Se debe seleccionar una opción para el campo '" + jckIsUnitsContainedApplying.getText() + "'.");
                }
                else if (jckIsUnitsVirtualApplying.isSelected() && moFieldFkUnitUnitsVirtualTypeId.getKeyAsIntArray()[0] == 0) {
                    validation.setTabbedPaneIndex(0);
                    validation.setComponent(jcbFkUnitUnitsVirtualTypeId);
                    validation.setMessage("Se debe seleccionar una opción para el campo '" + jckIsUnitsVirtualApplying.getText() + "'.");
                }
                else if (jckIsNetContentApplying.isSelected() && moFieldFkUnitNetContentTypeId.getKeyAsIntArray()[0] == 0) {
                    validation.setTabbedPaneIndex(0);
                    validation.setComponent(jcbFkUnitNetContentTypeId);
                    validation.setMessage("Se debe seleccionar una opción para el campo '" + jckIsNetContentApplying.getText() + "'.");
                }
                else if (jckIsNetContentUnitaryApplying.isSelected() && moFieldFkUnitNetContentUnitaryTypeId.getKeyAsIntArray()[0] == 0) {
                    validation.setTabbedPaneIndex(0);
                    validation.setComponent(jcbFkUnitNetContentUnitaryTypeId);
                    validation.setMessage("Se debe seleccionar una opción para el campo '" + jckIsNetContentUnitaryApplying.getText() + "'.");
                }
                */
                else if (jckIsInventoriable.isSelected() &&
                        !(SLibUtilities.belongsTo(moFieldFkItemClassId.getKeyAsIntArray(), new int[][] { SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO, SDataConstantsSys.ITMS_CL_ITEM_PUR_CON }))) {
                    validation.setTabbedPaneIndex(0);
                    validation.setComponent(jckIsInventoriable);
                    validation.setMessage("No se puede seleccionar el campo '" + jckIsInventoriable.getText() + "' si la opción elegida en el campo '" + jlFkItemCategoryId.getText() + "'\nes distinta a " +
                            "'" + SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMS_CL_ITEM, SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO) + "' y a " +
                            "'" + SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.ITMS_CL_ITEM, SDataConstantsSys.ITMS_CL_ITEM_PUR_CON) + "'.");
                }
                else if (moFieldNamingOrdinaryPosItemGeneric.getInteger() == 0 && moFieldNamingOrdinaryPosBrand.getInteger() == 0 && moFieldNamingOrdinaryPosManufacturer.getInteger() == 0 &&
                        moFieldNamingOrdinaryPosName.getInteger() == 0 && moFieldNamingOrdinaryPosPresentation.getInteger() == 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfNamingOrdinaryPosItemGeneric);
                    validation.setMessage("Se debe especificar al menos una posición para el nombre de la configuración de los nombres de ítems.");
                }
                else if (moFieldNamingOrdinaryPosItemGeneric.getInteger() > SDataConstantsSys.ITMS_NAM_ORD_POS_QTY ||
                        moFieldNamingOrdinaryPosBrand.getInteger() > SDataConstantsSys.ITMS_NAM_ORD_POS_QTY ||
                        moFieldNamingOrdinaryPosManufacturer.getInteger() > SDataConstantsSys.ITMS_NAM_ORD_POS_QTY ||
                        moFieldNamingOrdinaryPosName.getInteger() > SDataConstantsSys.ITMS_NAM_ORD_POS_QTY ||
                        moFieldNamingOrdinaryPosPresentation.getInteger() > SDataConstantsSys.ITMS_NAM_ORD_POS_QTY) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfNamingOrdinaryPosItemGeneric);
                    validation.setMessage("La posición de los campos para el nombre de la configuración de los nombres de ítems no puede ser mayor a " + SDataConstantsSys.ITMS_NAM_ORD_POS_QTY + ".");
                }
                else if (comparePositions(posNameOrd) > 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfNamingOrdinaryPosItemGeneric);
                    validation.setMessage("La posición para el nombre de la configuración de los nombres de ítems " + comparePositions(posNameOrd) + " esta repetida.");
                }
                else if (jckIsItemLineApplying.isSelected() &&
                        moFieldNamingLinePosItemGeneric.getInteger() == 0 && moFieldNamingLinePosItemLine.getInteger() == 0 &&
                        moFieldNamingLinePosBrand.getInteger() == 0 && moFieldNamingLinePosManufacturer.getInteger() == 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfNamingLinePosItemGeneric);
                    validation.setMessage("Se debe especificar al menos una posición para el nombre de la configuración de los nombres de líneas de ítems.");
                }
                else if (jckIsItemLineApplying.isSelected() &&
                        (moFieldNamingLinePosItemGeneric.getInteger() > SDataConstantsSys.ITMS_NAM_LINE_POS_QTY ||
                        moFieldNamingLinePosItemLine.getInteger() > SDataConstantsSys.ITMS_NAM_LINE_POS_QTY ||
                        moFieldNamingLinePosBrand.getInteger() > SDataConstantsSys.ITMS_NAM_LINE_POS_QTY ||
                        moFieldNamingLinePosManufacturer.getInteger() > SDataConstantsSys.ITMS_NAM_LINE_POS_QTY)) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfNamingLinePosItemGeneric);
                    validation.setMessage("La posición de los campos para el nombre de la configuración de los nombres de líneas de ítems no puede ser mayor a " + SDataConstantsSys.ITMS_NAM_LINE_POS_QTY + ".");
                }
                else if (jckIsItemLineApplying.isSelected() && comparePositions(posNameLine) > 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfNamingLinePosItemGeneric);
                    validation.setMessage("La posición para el nombre de la configuración de los nombres de líneas de ítems " + comparePositions(posNameLine) + " esta repetida.");
                }
                else if (jckIsItemKeyAutomatic.isSelected() &&
                        moFieldKeyOrdinaryPosItemGeneric.getInteger() == 0 && moFieldKeyOrdinaryPosBrand.getInteger() == 0 &&
                        moFieldKeyOrdinaryPosManufacturer.getInteger() == 0 && moFieldKeyOrdinaryPosCode.getInteger() == 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfKeyOrdinaryPosItemGeneric);
                    validation.setMessage("Se debe especificar al menos una posición para la clave de la configuración de los nombres de ítems.");
                }
                else if (jckIsItemKeyAutomatic.isSelected() &&
                        (moFieldKeyOrdinaryPosItemGeneric.getInteger() > SDataConstantsSys.ITMS_KEY_ORD_POS_QTY ||
                        moFieldKeyOrdinaryPosBrand.getInteger() > SDataConstantsSys.ITMS_KEY_ORD_POS_QTY ||
                        moFieldKeyOrdinaryPosManufacturer.getInteger() > SDataConstantsSys.ITMS_KEY_ORD_POS_QTY ||
                        moFieldKeyOrdinaryPosCode.getInteger() > SDataConstantsSys.ITMS_KEY_ORD_POS_QTY)) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfKeyOrdinaryPosCode);
                    validation.setMessage("La posición de los campos para la clave de la configuración de los nombres de ítems no puede ser mayor a " + SDataConstantsSys.ITMS_KEY_ORD_POS_QTY + ".");
                }
                else if (jckIsItemKeyAutomatic.isSelected() && comparePositions(posKeyOrd) > 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfKeyOrdinaryPosItemGeneric);
                    validation.setMessage("La posición para la clave de la configuración de los nombres de ítems " + comparePositions(posKeyOrd) + " esta repetida.");
                }
                else if (jckIsItemLineApplying.isSelected() && jckIsItemKeyAutomatic.isSelected() &&
                        moFieldKeyLinePosItemGeneric.getInteger() == 0 && moFieldKeyLinePosItemLine.getInteger() == 0 &&
                        moFieldKeyLinePosBrand.getInteger() == 0 && moFieldKeyLinePosManufacturer.getInteger() == 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfKeyLinePosItemGeneric);
                    validation.setMessage("Se debe especificar al menos una posición para la clave de la configuración de los nombres de líneas de ítems.");
                }
                else if (jckIsItemLineApplying.isSelected() && jckIsItemKeyAutomatic.isSelected() &&
                        (moFieldKeyLinePosItemGeneric.getInteger() > SDataConstantsSys.ITMS_KEY_LINE_POS_QTY ||
                        moFieldKeyLinePosItemLine.getInteger() > SDataConstantsSys.ITMS_KEY_LINE_POS_QTY ||
                        moFieldKeyLinePosBrand.getInteger() > SDataConstantsSys.ITMS_KEY_LINE_POS_QTY ||
                        moFieldKeyLinePosManufacturer.getInteger() > SDataConstantsSys.ITMS_KEY_LINE_POS_QTY)) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfKeyLinePosItemGeneric);
                    validation.setMessage("La posición de los campos para la clave de la configuración de los nombres de líneas de ítems no puede ser mayor a " + SDataConstantsSys.ITMS_KEY_LINE_POS_QTY + ".");
                }
                else if (jckIsItemLineApplying.isSelected() && jckIsItemKeyAutomatic.isSelected() && comparePositions(posKeyLine) > 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setComponent(jtfKeyLinePosItemGeneric);
                    validation.setMessage("La posición para la clave de la configuración de los nombres de líneas de ítems " + comparePositions(posKeyLine) + " esta repetida.");
                }
                else if (jltBizAreaAsignated.getModel().getSize() == 0) {
                    validation.setTabbedPaneIndex(1);
                    validation.setMessage("Debe seleccionar una opción para las áreas de negocios del ítem génerico.");
                }
            }
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
        moItemGeneric = (SDataItemGeneric) registry;

        moFieldFkItemCategoryId.setFieldValue(new int[] { moItemGeneric.getFkItemCategoryId() });
        moFieldFkItemClassId.setFieldValue(new int[] { moItemGeneric.getFkItemCategoryId(), moItemGeneric.getFkItemClassId() });
        moFieldFkItemTypeId.setFieldValue(new int[] { moItemGeneric.getFkItemCategoryId(), moItemGeneric.getFkItemClassId(), moItemGeneric.getFkItemTypeId() });
        moFieldIsDeleted.setFieldValue(moItemGeneric.getIsDeleted());
        moFieldFkDbmsItemFamilyId.setFieldValue(new int[] { moItemGeneric.getDbmsDataItemGroup().getFkItemFamilyId() });
        moFieldFkItemGroupId.setFieldValue(new int[] { moItemGeneric.getFkItemGroupId() });
        moFieldIsItemShortApplying.setFieldValue(moItemGeneric.getIsItemShortApplying());
        moFieldItemGeneric.setFieldValue(moItemGeneric.getItemGeneric());
        moFieldItemGenericShort.setFieldValue(moItemGeneric.getItemGenericShort());
        moFieldIsItemKeyApplying.setFieldValue(moItemGeneric.getIsItemKeyApplying());
        moFieldIsItemKeyAutomatic.setFieldValue(moItemGeneric.getIsItemKeyAutomatic());
        moFieldCode.setFieldValue(moItemGeneric.getCode());
        moFieldIsItemLineApplying.setFieldValue(moItemGeneric.getIsItemLineApplying());
        moFieldIsItemKeyEditable.setFieldValue(moItemGeneric.getIsItemKeyEditable());
        moFieldIsItemNameEditable.setFieldValue(moItemGeneric.getIsItemNameEditable());
        moFieldIsBulk.setFieldValue(moItemGeneric.getIsBulk());
        moFieldIsInventoriable.setFieldValue(moItemGeneric.getIsInventoriable());
        moFieldIsLotApplying.setFieldValue(moItemGeneric.getIsLotApplying());
        moFieldDaysForExpiration.setFieldValue(moItemGeneric.getDaysForExpiration());
        moFieldFkSerialNumberTypeId.setFieldValue(new int[] { moItemGeneric.getFkSerialNumberTypeId() });
        moFieldSerialNumber.setFieldValue(moItemGeneric.getSerialNumber());
        moFieldSerialNumberFormat.setFieldValue(moItemGeneric.getSerialNumberFormat());
        moFieldSurplusPercentage.setFieldValue(moItemGeneric.getSurplusPercentage());
        moFieldFkUnitTypeId.setFieldValue(new int[] { moItemGeneric.getFkUnitTypeId() });
        moFieldIsUnitsContainedApplying.setFieldValue(moItemGeneric.getIsUnitsContainedApplying());
        moFieldFkUnitUnitsContainedTypeId.setFieldValue(new int[] { moItemGeneric.getFkUnitUnitsContainedTypeId() });
        moFieldIsUnitsVirtualApplying.setFieldValue(moItemGeneric.getIsUnitsVirtualApplying());
        moFieldFkUnitUnitsVirtualTypeId.setFieldValue(new int[] { moItemGeneric.getFkUnitUnitsVirtualTypeId() });
        moFieldIsUnitsPackageApplying.setFieldValue(moItemGeneric.getIsUnitsPackageApplying());
        moFieldIsNetContentApplying.setFieldValue(moItemGeneric.getIsNetContentApplying());
        moFieldFkUnitNetContentTypeId.setFieldValue(new int[] { moItemGeneric.getFkUnitNetContentTypeId() });
        moFieldIsNetContentUnitaryApplying.setFieldValue(moItemGeneric.getIsNetContentUnitaryApplying());
        moFieldFkUnitNetContentUnitaryTypeId.setFieldValue(new int[] { moItemGeneric.getFkUnitNetContentUnitaryTypeId() });
        moFieldIsNetContentVariable.setFieldValue(moItemGeneric.getIsNetContentVariable());
        moFieldNamingOrdinaryPosItemGeneric.setFieldValue(moItemGeneric.getNamingOrdinaryPosItemGeneric());
        moFieldNamingOrdinaryPosBrand.setFieldValue(moItemGeneric.getNamingOrdinaryPosBrand());
        moFieldNamingOrdinaryPosManufacturer.setFieldValue(moItemGeneric.getNamingOrdinaryPosManufacturer());
        moFieldNamingOrdinaryPosName.setFieldValue(moItemGeneric.getNamingOrdinaryPosName());
        moFieldNamingOrdinaryPosPresentation.setFieldValue(moItemGeneric.getNamingOrdinaryPosPresentation());
        moFieldKeyOrdinaryPosItemGeneric.setFieldValue(moItemGeneric.getKeyOrdinaryPosItemGeneric());
        moFieldKeyOrdinaryPosBrand.setFieldValue(moItemGeneric.getKeyOrdinaryPosBrand());
        moFieldKeyOrdinaryPosManufacturer.setFieldValue(moItemGeneric.getKeyOrdinaryPosManufacturer());
        moFieldKeyOrdinaryPosCode.setFieldValue(moItemGeneric.getKeyOrdinaryPosCode());
        moFieldNamingLinePosItemGeneric.setFieldValue(moItemGeneric.getNamingLinePosItemGeneric());
        moFieldNamingLinePosItemLine.setFieldValue(moItemGeneric.getNamingLinePosItemLine());
        moFieldNamingLinePosBrand.setFieldValue(moItemGeneric.getNamingLinePosBrand());
        moFieldNamingLinePosManufacturer.setFieldValue(moItemGeneric.getNamingLinePosManufacturer());
        moFieldKeyLinePosItemGeneric.setFieldValue(moItemGeneric.getKeyLinePosItemGeneric());
        moFieldKeyLinePosItemLine.setFieldValue(moItemGeneric.getKeyLinePosItemLine());
        moFieldKeyLinePosBrand.setFieldValue(moItemGeneric.getKeyLinePosBrand());
        moFieldKeyLinePosManufacturer.setFieldValue(moItemGeneric.getKeyLinePosManufacturer());
        moFieldIsLengthApplying.setFieldValue(moItemGeneric.getIsLengthApplying());
        moFieldIsLengthUnitaryApplying.setFieldValue(moItemGeneric.getIsLengthUnitaryApplying());
        moFieldIsLengthVariable.setFieldValue(moItemGeneric.getIsLengthVariable());
        moFieldIsSurfaceApplying.setFieldValue(moItemGeneric.getIsSurfaceApplying());
        moFieldIsSurfaceUnitaryApplying.setFieldValue(moItemGeneric.getIsSurfaceUnitaryApplying());
        moFieldIsSurfaceVariable.setFieldValue(moItemGeneric.getIsSurfaceVariable());
        moFieldIsVolumeApplying.setFieldValue(moItemGeneric.getIsVolumeApplying());
        moFieldIsVolumeUnitaryApplying.setFieldValue(moItemGeneric.getIsVolumeUnitaryApplying());
        moFieldIsVolumeVariable.setFieldValue(moItemGeneric.getIsVolumeVariable());
        moFieldIsMassApplying.setFieldValue(moItemGeneric.getIsMassApplying());
        moFieldIsMassUnitaryApplying.setFieldValue(moItemGeneric.getIsMassUnitaryApplying());
        moFieldIsMassVariable.setFieldValue(moItemGeneric.getIsMassVariable());
        moFieldIsWeightGrossApplying.setFieldValue(moItemGeneric.getIsWeightGrossApplying());
        moFieldIsWeightDeliveryApplying.setFieldValue(moItemGeneric.getIsWeightDeliveryApplying());
        moFieldIsFreeDiscountUnitary.setFieldValue(moItemGeneric.getIsFreeDiscountUnitary());
        moFieldIsFreeDiscountEntry.setFieldValue(moItemGeneric.getIsFreeDiscountEntry());
        moFieldIsFreeDiscountDoc.setFieldValue(moItemGeneric.getIsFreeDiscountDoc());
        moFieldIsFreePrice.setFieldValue(moItemGeneric.getIsFreePrice());
        moFieldIsFreeDiscount.setFieldValue(moItemGeneric.getIsFreeDiscount());
        moFieldIsFreeCommissions.setFieldValue(moItemGeneric.getIsFreeCommissions());
        moFieldIsItemRefRequired.setFieldValue(moItemGeneric.getIsItemReferenceRequired());
        moFieldFkDefaultItemRefeferenceId_n.setFieldValue(new int[] { moItemGeneric.getFkDefaultItemRefId_n() });
        moFieldFkAdministrativeConceptTypeId.setFieldValue(new int[] { moItemGeneric.getFkAdministrativeConceptTypeId() });
        moFieldFkTaxableConceptTypeId.setFieldValue(new int[] { moItemGeneric.getFkTaxableConceptTypeId() });

        if (!moItemGeneric.getIsItemShortApplying()) {
            jtfItemGenericShort.setEnabled(false);
        }

        if (moItemGeneric.getDbmsBizAreas().size() == 0) {
            renderBizArea();
        }
        else {
            for (int i = 0; i < moItemGeneric.getDbmsBizAreas().size(); i++) {
                for (int j = 0; j < jltBizAreaAvailable.getModel().getSize(); j++ ) {
                    if(((int[])((SFormComponentItem) jltBizAreaAvailable.getModel().getElementAt(j)).getPrimaryKey())[0] == moItemGeneric.getDbmsBizAreas().get(i).getPkBizAreaId()) {
                        jltBizAreaAvailable.setSelectedIndex(j);
                        SFormUtilities.addListItem(jltBizAreaAsignated, SFormUtilities.removeListSelectedItem(jltBizAreaAvailable));
                    }
                }
            }
        }

        readItemGroupSettings();
        renderItemGenericShortSettings();
        renderItemKeySettings();
        renderItemKeyAutomaticSettings();
        renderItemLineSettings();
        renderIsInventoriable();
        renderUnitUnitsContainedTypeSettings();
        renderUnitUnitsVirtualTypeSettings();
        renderUnitNetContentTypeSettings();
        renderUnitNetContentUnitaryTypeSettings();
        renderLength();
        renderSurface();
        renderVolume();
        renderMass();
        renderIsItemRefRequired();

        jckIsDeleted.setEnabled(true);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        SDataItemGenericBizArea bizArea = null;

        if (moItemGeneric == null) {
            moItemGeneric = new SDataItemGeneric();
            moItemGeneric.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moItemGeneric.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moItemGeneric.setFkItemCategoryId(moFieldFkItemTypeId.getKeyAsIntArray()[0]);
        moItemGeneric.setFkItemClassId(moFieldFkItemTypeId.getKeyAsIntArray()[1]);
        moItemGeneric.setFkItemTypeId(moFieldFkItemTypeId.getKeyAsIntArray()[2]);
        moItemGeneric.setIsDeleted(moFieldIsDeleted.getBoolean());
        moItemGeneric.setFkItemGroupId(moFieldFkItemGroupId.getKeyAsIntArray()[0]);
        moItemGeneric.setIsItemShortApplying(moFieldIsItemShortApplying.getBoolean());
        moItemGeneric.setItemGeneric(moFieldItemGeneric.getString());
        moItemGeneric.setItemGenericShort(jckIsItemShortApplying.isSelected() ? moFieldItemGenericShort.getString() : "");
        moItemGeneric.setIsItemKeyApplying(moFieldIsItemKeyApplying.getBoolean());
        moItemGeneric.setIsItemKeyAutomatic(moFieldIsItemKeyApplying.getBoolean() ? moFieldIsItemKeyAutomatic.getBoolean() : false);
        moItemGeneric.setCode(moFieldCode.getString());
        moItemGeneric.setIsItemLineApplying(moFieldIsItemLineApplying.getBoolean());
        moItemGeneric.setIsItemKeyEditable(moFieldIsItemKeyEditable.getBoolean());
        moItemGeneric.setIsItemNameEditable(moFieldIsItemNameEditable.getBoolean());
        moItemGeneric.setIsBulk(moFieldIsBulk.getBoolean());
        moItemGeneric.setIsInventoriable(moFieldIsInventoriable.getBoolean());
        moItemGeneric.setIsLotApplying(moFieldIsLotApplying.getBoolean());
        moItemGeneric.setDaysForExpiration(moFieldDaysForExpiration.getInteger());
        moItemGeneric.setFkSerialNumberTypeId(moFieldFkSerialNumberTypeId.getKeyAsIntArray()[0]);
        moItemGeneric.setSerialNumber(moFieldSerialNumber.getString());
        moItemGeneric.setSerialNumberFormat(moFieldSerialNumberFormat.getString());
        moItemGeneric.setSurplusPercentage(moFieldSurplusPercentage.getDouble());
        moItemGeneric.setFkUnitTypeId(moFieldFkUnitTypeId.getKeyAsIntArray()[0]);
        moItemGeneric.setIsUnitsContainedApplying(moFieldIsUnitsContainedApplying.getBoolean());
        moItemGeneric.setFkUnitUnitsContainedTypeId(moFieldFkUnitUnitsContainedTypeId.getKeyAsIntArray()[0]);
        moItemGeneric.setIsUnitsVirtualApplying(moFieldIsUnitsVirtualApplying.getBoolean());
        moItemGeneric.setFkUnitUnitsVirtualTypeId(moFieldFkUnitUnitsVirtualTypeId.getKeyAsIntArray()[0]);
        moItemGeneric.setIsUnitsPackageApplying(moFieldIsUnitsPackageApplying.getBoolean());
        moItemGeneric.setIsNetContentApplying(moFieldIsNetContentApplying.getBoolean());
        moItemGeneric.setFkUnitNetContentTypeId(moFieldFkUnitNetContentTypeId.getKeyAsIntArray()[0]);
        moItemGeneric.setIsNetContentUnitaryApplying(moFieldIsNetContentUnitaryApplying.getBoolean());
        moItemGeneric.setFkUnitNetContentUnitaryTypeId(moFieldFkUnitNetContentUnitaryTypeId.getKeyAsIntArray()[0]);
        moItemGeneric.setIsNetContentVariable(moFieldIsNetContentVariable.getBoolean());
        moItemGeneric.setNamingOrdinaryPosItemGeneric(moFieldNamingOrdinaryPosItemGeneric.getInteger());
        moItemGeneric.setNamingOrdinaryPosBrand(moFieldNamingOrdinaryPosBrand.getInteger());
        moItemGeneric.setNamingOrdinaryPosManufacturer(moFieldNamingOrdinaryPosManufacturer.getInteger());
        moItemGeneric.setNamingOrdinaryPosName(moFieldNamingOrdinaryPosName.getInteger());
        moItemGeneric.setNamingOrdinaryPosPresentation(moFieldNamingOrdinaryPosPresentation.getInteger());
        moItemGeneric.setKeyOrdinaryPosItemGeneric(jckIsItemKeyApplying.isSelected() && jckIsItemKeyAutomatic.isSelected() ? moFieldKeyOrdinaryPosItemGeneric.getInteger() : 0);
        moItemGeneric.setKeyOrdinaryPosBrand(jckIsItemKeyApplying.isSelected() && jckIsItemKeyAutomatic.isSelected() ? moFieldKeyOrdinaryPosBrand.getInteger() : 0);
        moItemGeneric.setKeyOrdinaryPosManufacturer(jckIsItemKeyApplying.isSelected() && jckIsItemKeyAutomatic.isSelected() ? moFieldKeyOrdinaryPosManufacturer.getInteger() : 0);
        moItemGeneric.setKeyOrdinaryPosCode(jckIsItemKeyApplying.isSelected() && jckIsItemKeyAutomatic.isSelected() ? moFieldKeyOrdinaryPosCode.getInteger() : 0);
        moItemGeneric.setNamingLinePosItemGeneric(moFieldNamingLinePosItemGeneric.getInteger());
        moItemGeneric.setNamingLinePosItemLine(moFieldNamingLinePosItemLine.getInteger());
        moItemGeneric.setNamingLinePosBrand(moFieldNamingLinePosBrand.getInteger());
        moItemGeneric.setNamingLinePosManufacturer(moFieldNamingLinePosManufacturer.getInteger());
        moItemGeneric.setKeyLinePosItemGeneric(moFieldKeyLinePosItemGeneric.getInteger());
        moItemGeneric.setKeyLinePosItemLine(moFieldKeyLinePosItemLine.getInteger());
        moItemGeneric.setKeyLinePosBrand(moFieldKeyLinePosBrand.getInteger());
        moItemGeneric.setKeyLinePosManufacturer(moFieldKeyLinePosManufacturer.getInteger()) ;
        moItemGeneric.setIsLengthApplying(moFieldIsLengthApplying.getBoolean());
        moItemGeneric.setIsLengthUnitaryApplying(moFieldIsLengthUnitaryApplying.getBoolean());
        moItemGeneric.setIsLengthVariable(moFieldIsLengthVariable.getBoolean());
        moItemGeneric.setIsSurfaceApplying(moFieldIsSurfaceApplying.getBoolean());
        moItemGeneric.setIsSurfaceUnitaryApplying(moFieldIsSurfaceUnitaryApplying.getBoolean());
        moItemGeneric.setIsSurfaceVariable(moFieldIsSurfaceVariable.getBoolean());
        moItemGeneric.setIsVolumeApplying(moFieldIsVolumeApplying.getBoolean());
        moItemGeneric.setIsVolumeUnitaryApplying(moFieldIsVolumeUnitaryApplying.getBoolean());
        moItemGeneric.setIsVolumeVariable(moFieldIsVolumeVariable.getBoolean());
        moItemGeneric.setIsMassApplying(moFieldIsMassApplying.getBoolean());
        moItemGeneric.setIsMassUnitaryApplying(moFieldIsMassUnitaryApplying.getBoolean());
        moItemGeneric.setIsMassVariable(moFieldIsMassVariable.getBoolean());
        moItemGeneric.setIsWeightGrossApplying(moFieldIsWeightGrossApplying.getBoolean());
        moItemGeneric.setIsWeightDeliveryApplying(moFieldIsWeightDeliveryApplying.getBoolean());
        moItemGeneric.setIsFreeDiscountUnitary(moFieldIsFreeDiscountUnitary.getBoolean());
        moItemGeneric.setIsFreeDiscountEntry(moFieldIsFreeDiscountEntry.getBoolean());
        moItemGeneric.setIsFreeDiscountDoc(moFieldIsFreeDiscountDoc.getBoolean());
        moItemGeneric.setIsFreePrice(moFieldIsFreePrice.getBoolean());
        moItemGeneric.setIsFreeDiscount(moFieldIsFreeDiscount.getBoolean());
        moItemGeneric.setIsFreeCommissions(moFieldIsFreeCommissions.getBoolean());
        moItemGeneric.setIsItemReferenceRequired(moFieldIsItemRefRequired.getBoolean());
        moItemGeneric.setFkDefaultItemRefId_n(moFieldFkDefaultItemRefeferenceId_n.getKeyAsIntArray()[0]);
        moItemGeneric.setFkAdministrativeConceptTypeId(moFieldFkAdministrativeConceptTypeId.getKeyAsIntArray()[0]);
        moItemGeneric.setFkTaxableConceptTypeId(moFieldFkTaxableConceptTypeId.getKeyAsIntArray()[0]);

        moItemGeneric.getDbmsBizAreas().clear();
        if ( jltBizAreaAsignated.getModel().getSize() > 0) {
            for (int i = 0 ; i < jltBizAreaAsignated.getModel().getSize(); i++) {
                bizArea = new SDataItemGenericBizArea();
                bizArea.setPkBizAreaId(((int[]) ((SFormComponentItem) jltBizAreaAsignated.getModel().getElementAt(i)).getPrimaryKey())[0]);
                bizArea.setPkItemGenericId(moItemGeneric.getPkItemGenericId());
                moItemGeneric.getDbmsBizAreas().add(bizArea);
            }
        }

        return moItemGeneric;
    }

    @Override
    public void setValue(int type, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValue(int type) {
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
            else if (button == jbDbmsFkItemFamilyId) {
                actionDbmsFkItemFamilyId();
            }
            else if (button == jbFkItemGroupId) {
                actionFkItemGroupId();
            }
            else if (button == jbCopyItemGeneric) {
                actionCopyItemGeneric();
                jtfItemGenericShort.requestFocus();
            }
            else if (button == jbBizAreaAssign) {
                actionBizAreaAssign();
            }
            else if (button == jbBizAreaAssignAll) {
                actionBizAreaAssignAll();
            }
            else if (button == jbBizAreaRemove) {
                actionBizAreaRemove();
                if (jltBizAreaAsignated.getModel().getSize() > 0) {
                    jltBizAreaAsignated.setSelectedIndex(0);
                }
            }
            else if (button == jbBizAreaRemoveAll) {
                actionBizAreaRemoveAll();
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
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfItemGeneric) {
                focusLostItemGeneric();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbResetingForm) {
            if (e.getSource() instanceof javax.swing.JComboBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox comboBox = (JComboBox) e.getSource();

                    if (comboBox == jcbFkItemGroupId) {
                        itemStateChangedFkItemGroupId();
                    }
                    else if (comboBox == jcbFkSerialNumberTypeId) {
                        itemStateChangedFkSerialNumberTypeId();
                    }
                }
            }
            else if (e.getSource() instanceof javax.swing.JCheckBox) {
                JCheckBox checkBox = (JCheckBox) e.getSource();

                if (checkBox == jckIsItemShortApplying) {
                    itemStateChangedIsItemShortApplying();
                }
                else if (checkBox == jckIsItemKeyApplying) {
                    itemStateChangedIsItemKeyApplying();
                }
                else if (checkBox == jckIsItemKeyAutomatic) {
                    itemStateChangedIsItemKeyAutomatic();
                }
                else if (checkBox == jckIsItemLineApplying) {
                    itemStateChangedIsItemLineApplying();
                }
                else if (checkBox == jckIsInventoriable) {
                    itemStateChangedIsInventoriable();
                }
                else if (checkBox == jckIsLotApplying) {
                    itemStateChangedIsLotApplying();
                }
                else if (checkBox == jckIsUnitsContainedApplying) {
                    itemStateChangedIsUnitsContainedApplying();
                }
                else if (checkBox == jckIsUnitsVirtualApplying) {
                    itemStateChangedIsUnitsVirtualApplying();
                }
                else if (checkBox == jckIsNetContentApplying) {
                    itemStateChangedIsNetContentApplying();
                }
                else if (checkBox == jckIsNetContentUnitaryApplying) {
                    itemStateChangedIsNetContentUnitaryApplying();
                }
                else if (checkBox == jckIsLengthApplying) {
                    itemStateChangedIsLenghtApplying();
                }
                else if (checkBox == jckIsSurfaceApplying) {
                    itemStateChangedIsSurfaceApplying();
                }
                else if (checkBox == jckIsVolumeApplying) {
                    itemStateChangedIsVolumeApplying();
                }
                else if (checkBox == jckIsMassApplying) {
                    itemStateChangedIsMassApplying();
                }
                else if (checkBox == jckIsItemRefRequired) {
                    itemStateChangedIsItemRefRequired();
                }
            }
        }
    }
}
