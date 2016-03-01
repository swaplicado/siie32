/*
 * SFormProductionOrder.java
 *
 * Created on 21 de septiembre de 2009, 05:53 PM
 */

package erp.mmfg.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComboBoxGroup;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mmfg.data.SDataBom;
import erp.mmfg.data.SDataProductionOrder;
import erp.mmfg.data.SDataProductionOrderCharge;
import erp.mmfg.data.SDataProductionOrderChargeRow;
import erp.mmfg.data.SDataProductionOrderNotes;
import erp.mmfg.data.SDataProductionOrderNotesRow;
import erp.mmfg.data.SDataProductionOrderPeriod;
import erp.mmfg.data.SDataProductionOrderPeriodRow;
import erp.mmfg.data.SMfgUtils;
import erp.mod.SModSysConsts;
import erp.mod.itm.db.SItmConsts;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 *
 * @author  Néstor Ávalos
 */
public class SFormProductionOrder extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private int mnIndex;
    private boolean mbFirstTime;
    private boolean mbResetingForm;

    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;


    private erp.mmfg.data.SDataProductionOrder moProductionOrder;
    private erp.lib.form.SFormField moFieldNumber;
    private erp.lib.form.SFormField moFieldReference;
    private erp.lib.form.SFormField moFieldQuantityOriginal;
    private erp.lib.form.SFormField moFieldQuantityRework;
    private erp.lib.form.SFormField moFieldQuantity;
    private erp.lib.form.SFormField moFieldCharges;
    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldDateDelivery;
    private erp.lib.form.SFormField moFieldDateLot_n;
    private erp.lib.form.SFormField moFieldDateLotAssigned_n;
    private erp.lib.form.SFormField moFieldDateStart_n;
    private erp.lib.form.SFormField moFieldDateEnd_n;
    private erp.lib.form.SFormField moFieldDateClose_n;
    private erp.lib.form.SFormField moFieldFkOrdTypeId;
    private erp.lib.form.SFormField moFieldFkItemId_r;
    private erp.lib.form.SFormField moFieldFkBomId;
    private erp.lib.form.SFormField moFieldFkCobId;
    private erp.lib.form.SFormField moFieldFkEntityId;
    private erp.lib.form.SFormField moFieldFkOrdPriorityId;
    private erp.lib.form.SFormField moFieldFkBizPartnerId_n;
    private erp.lib.form.SFormField moFieldFkBizPartnerOperatorId_n;
    private erp.lib.form.SFormField moFieldNumberReference_n;
    private erp.lib.form.SFormField moFieldFkProductionOrderId_n;
    private erp.lib.form.SFormField moFieldFkLotId_n;
    private erp.lib.form.SFormField moFieldFkTurnDeliveryId;
    private erp.lib.form.SFormField moFieldFkTurnLotAssigedId;
    private erp.lib.form.SFormField moFieldFkTurnStartId;
    private erp.lib.form.SFormField moFieldFkTurnEndId;
    private erp.lib.form.SFormField moFieldFkTurnCloseId;
    private erp.lib.form.SFormField moFieldTsLotAssigned_n;
    private erp.lib.form.SFormField moFieldTsStart_n;
    private erp.lib.form.SFormField moFieldTsEnd_n;
    private erp.lib.form.SFormField moFieldTsClose_n;
    private erp.lib.form.SFormField moFieldIsForecast;
    private erp.lib.form.SFormField moFieldIsDeleted;

    private erp.lib.form.SFormField moFieldDbmsExplotionMaterialsReference;
    private erp.lib.form.SFormField moFieldDbmsProductionOrderStatus;
    private erp.lib.form.SFormField moFieldDbmsUserLotAssigned;
    private erp.lib.form.SFormField moFieldDbmsLotDateExpired;
    private erp.lib.form.SFormField moFieldDbmsUserStart;
    private erp.lib.form.SFormField moFieldDbmsUserEnd;
    private erp.lib.form.SFormField moFieldDbmsUserClose;

    private erp.lib.table.STablePane moProductionOrderChargesPane;
    private erp.lib.table.STablePane moProductionOrderNotesPane;
    private erp.lib.table.STablePane moProductionOrderPeriodsPane;

    private erp.lib.form.SFormComboBoxGroup moComboBoxItemBom;

    private erp.mmfg.data.SDataProductionOrderNotes moProductionOrderNotes;
    private erp.mmfg.data.SDataProductionOrderNotesRow moProductionOrderNotesRow;
    private erp.mmfg.data.SDataProductionOrderPeriod moProductionOrderPeriod;
    private erp.mmfg.data.SDataProductionOrderPeriodRow moProductionOrderPeriodRow;
    private erp.mmfg.data.SDataProductionOrderCharge moProductionOrderCharges = null;
    private erp.mmfg.data.SDataProductionOrderChargeRow moProductionOrderChargesRow;
    private erp.mmfg.form.SFormProductionOrderNotes moProductionOrderNotesForm;
    private erp.mmfg.form.SFormProductionOrderPeriod moProductionOrderPeriodForm;
    private erp.mmfg.form.SFormProductionOrderCharge moProductionOrderChargesForm;

    private erp.mitm.data.SDataItem moItem;
    private erp.mitm.data.SDataUnit moUnit;
    private erp.mmfg.data.SDataBom moBom;

    private java.util.Vector<erp.lib.form.SFormComponentItem> mvItemsEntities = null;

    private boolean mbIsUniversalCompany;

    /** Creates new form DFormCompany */
    public SFormProductionOrder(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.MFG_ORD;
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jlFkCobId = new javax.swing.JLabel();
        jcbFkCobId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkCobId = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jlFkEntityId = new javax.swing.JLabel();
        jcbFkEntityId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkEntityId = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jlFkOrdTypeId = new javax.swing.JLabel();
        jcbFkOrdTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel19 = new javax.swing.JPanel();
        jlFkItemId = new javax.swing.JLabel();
        jcbFkItemId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemId = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        jlFkBomId = new javax.swing.JLabel();
        jcbFkBomId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkBomId = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jlReference = new javax.swing.JLabel();
        jtfReference = new javax.swing.JTextField();
        jbCopyReference = new javax.swing.JButton();
        jPanel40 = new javax.swing.JPanel();
        jlQuantityOriginal = new javax.swing.JLabel();
        jtfQuantityOriginal = new javax.swing.JTextField();
        jtfQuantityOriginalUnit = new javax.swing.JTextField();
        jPanel41 = new javax.swing.JPanel();
        jlQuantityRework = new javax.swing.JLabel();
        jtfQuantityRework = new javax.swing.JTextField();
        jtfQuantityReworkUnit = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jlQuantity = new javax.swing.JLabel();
        jtfQuantity = new javax.swing.JTextField();
        jtfQuantityUnit = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jlCharges = new javax.swing.JLabel();
        jtfCharges = new javax.swing.JTextField();
        jlDummy10 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jtfDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jlDateDelivery = new javax.swing.JLabel();
        jtfDateDelivery = new javax.swing.JFormattedTextField();
        jbDateDelivery = new javax.swing.JButton();
        jPanel37 = new javax.swing.JPanel();
        jlFkTurnDeliveryId = new javax.swing.JLabel();
        jcbFkTurnDeliveryId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel11 = new javax.swing.JPanel();
        jlFkOrdPriorityId = new javax.swing.JLabel();
        jcbFkOrdPriorityId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel26 = new javax.swing.JPanel();
        jlFkProductionOrderId_n = new javax.swing.JLabel();
        jcbFkProductionOrderId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkProductionOrderId_n = new javax.swing.JButton();
        jtfDummyProductionOrder_n = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jlFkBizPartnerId_n = new javax.swing.JLabel();
        jcbFkBizPartnerId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkBizPartnerId_n = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jlNumberReference_n = new javax.swing.JLabel();
        jtfNumberReference_n = new javax.swing.JTextField();
        jPanel42 = new javax.swing.JPanel();
        jlFkBizPartnerOperatorId_n = new javax.swing.JLabel();
        jcbFkBizPartnerOperatorId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkBizPartnerOperatorId_n = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jlNumber = new javax.swing.JLabel();
        jtfNumber = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jlDbmsStatus = new javax.swing.JLabel();
        jtfDbmsStatus = new javax.swing.JTextField();
        jPanel39 = new javax.swing.JPanel();
        jlDbmsExplotionMaterialsReference = new javax.swing.JLabel();
        jtfDbmsExplotionMaterialsReference = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jlFkLotId_n = new javax.swing.JLabel();
        jtfFkLotId_n = new javax.swing.JTextField();
        jlDummy16 = new javax.swing.JLabel();
        jtfDbmsLotDateExpired = new javax.swing.JFormattedTextField();
        jPanel23 = new javax.swing.JPanel();
        jlDateLot_n = new javax.swing.JLabel();
        jtfDateLot_n = new javax.swing.JFormattedTextField();
        jPanel35 = new javax.swing.JPanel();
        jlDateLotAssigned_n = new javax.swing.JLabel();
        jtfDateLotAssigned_n = new javax.swing.JFormattedTextField();
        jtfTsLotAssigned_n = new javax.swing.JFormattedTextField();
        jPanel36 = new javax.swing.JPanel();
        jlFkTurnLotAssignedId = new javax.swing.JLabel();
        jcbFkTurnLotAssignedId = new javax.swing.JComboBox<SFormComponentItem>();
        jtfDbmsUserLotAssigned = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jlDateStart_n = new javax.swing.JLabel();
        jtfDateStart_n = new javax.swing.JFormattedTextField();
        jtfTsStart_n = new javax.swing.JFormattedTextField();
        jPanel32 = new javax.swing.JPanel();
        jlFkTurnStartId = new javax.swing.JLabel();
        jcbFkTurnStartId = new javax.swing.JComboBox<SFormComponentItem>();
        jtfDbmsUserStart = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jlDateEnd_n = new javax.swing.JLabel();
        jtfDateEnd_n = new javax.swing.JFormattedTextField();
        jtfTsEnd_n = new javax.swing.JFormattedTextField();
        jPanel33 = new javax.swing.JPanel();
        jlFkTurnEndId = new javax.swing.JLabel();
        jcbFkTurnEndId = new javax.swing.JComboBox<SFormComponentItem>();
        jtfDbmsUserEnd = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jlDateClose_n = new javax.swing.JLabel();
        jtfDateClose_n = new javax.swing.JFormattedTextField();
        jtfTsClose_n = new javax.swing.JFormattedTextField();
        jPanel34 = new javax.swing.JPanel();
        jlFkTurnCloseId = new javax.swing.JLabel();
        jcbFkTurnCloseId = new javax.swing.JComboBox<SFormComponentItem>();
        jtfDbmsUserClose = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jckIsForecast = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpCharges = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jbChargesNew = new javax.swing.JButton();
        jbChargesEdit = new javax.swing.JButton();
        jbChargesDelete = new javax.swing.JButton();
        jpPeriods = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jbPeriodNew = new javax.swing.JButton();
        jbPeriodEdit = new javax.swing.JButton();
        jbPeriodDelete = new javax.swing.JButton();
        jpNotes = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jbNotesNew = new javax.swing.JButton();
        jbNotesEdit = new javax.swing.JButton();
        jbNotesDelete = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Orden de producción"); // NOI18N
        setMinimumSize(new java.awt.Dimension(247, 450));
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel3.setMinimumSize(new java.awt.Dimension(428, 494));
        jPanel3.setPreferredSize(new java.awt.Dimension(700, 475));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel13.setPreferredSize(new java.awt.Dimension(565, 475));
        jPanel13.setLayout(new java.awt.GridLayout(18, 1));

        jPanel15.setPreferredSize(new java.awt.Dimension(390, 23));
        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkCobId.setForeground(java.awt.Color.blue);
        jlFkCobId.setText("Sucursal: *");
        jlFkCobId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel15.add(jlFkCobId);

        jcbFkCobId.setPreferredSize(new java.awt.Dimension(250, 23));
        jcbFkCobId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkCobIdItemStateChanged(evt);
            }
        });
        jPanel15.add(jcbFkCobId);

        jbFkCobId.setText("...");
        jbFkCobId.setToolTipText("Seleccionar sucursal");
        jbFkCobId.setFocusable(false);
        jbFkCobId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel15.add(jbFkCobId);

        jPanel13.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkEntityId.setForeground(java.awt.Color.blue);
        jlFkEntityId.setText("Planta: *");
        jlFkEntityId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel16.add(jlFkEntityId);

        jcbFkEntityId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel16.add(jcbFkEntityId);

        jbFkEntityId.setText("...");
        jbFkEntityId.setToolTipText("Seleccionar planta");
        jbFkEntityId.setFocusable(false);
        jbFkEntityId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel16.add(jbFkEntityId);

        jPanel13.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkOrdTypeId.setForeground(java.awt.Color.blue);
        jlFkOrdTypeId.setText("Tipo: *");
        jlFkOrdTypeId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel17.add(jlFkOrdTypeId);

        jcbFkOrdTypeId.setPreferredSize(new java.awt.Dimension(250, 23));
        jcbFkOrdTypeId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkOrdTypeIdItemStateChanged(evt);
            }
        });
        jPanel17.add(jcbFkOrdTypeId);

        jPanel13.add(jPanel17);

        jPanel19.setPreferredSize(new java.awt.Dimension(556, 23));
        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkItemId.setText("Producto: *");
        jlFkItemId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel19.add(jlFkItemId);

        jcbFkItemId.setMaximumRowCount(16);
        jcbFkItemId.setPreferredSize(new java.awt.Dimension(400, 23));
        jcbFkItemId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkItemIdItemStateChanged(evt);
            }
        });
        jPanel19.add(jcbFkItemId);

        jbFkItemId.setText("...");
        jbFkItemId.setToolTipText("Seleccionar producto");
        jbFkItemId.setFocusable(false);
        jbFkItemId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel19.add(jbFkItemId);

        jPanel13.add(jPanel19);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkBomId.setText("Fórmula: *");
        jlFkBomId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel20.add(jlFkBomId);

        jcbFkBomId.setMaximumRowCount(16);
        jcbFkBomId.setPreferredSize(new java.awt.Dimension(400, 23));
        jcbFkBomId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbFkBomIdItemStateChanged(evt);
            }
        });
        jcbFkBomId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jcbFkBomIdFocusLost(evt);
            }
        });
        jPanel20.add(jcbFkBomId);

        jbFkBomId.setText("...");
        jbFkBomId.setToolTipText("Seleccionar fórmula");
        jbFkBomId.setFocusable(false);
        jbFkBomId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel20.add(jbFkBomId);

        jPanel13.add(jPanel20);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReference.setText("Referencia: *");
        jlReference.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel18.add(jlReference);

        jtfReference.setText("REFERENCE");
        jtfReference.setPreferredSize(new java.awt.Dimension(400, 23));
        jtfReference.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtfReferenceFocusGained(evt);
            }
        });
        jPanel18.add(jtfReference);

        jbCopyReference.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbCopyReference.setToolTipText("Copiar texto de fórmula");
        jbCopyReference.setFocusable(false);
        jbCopyReference.setPreferredSize(new java.awt.Dimension(23, 22));
        jPanel18.add(jbCopyReference);

        jPanel13.add(jPanel18);

        jPanel40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlQuantityOriginal.setText("Cantidad original: *");
        jlQuantityOriginal.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel40.add(jlQuantityOriginal);

        jtfQuantityOriginal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfQuantityOriginal.setText("0");
        jtfQuantityOriginal.setPreferredSize(new java.awt.Dimension(100, 23));
        jtfQuantityOriginal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfQuantityOriginalFocusLost(evt);
            }
        });
        jPanel40.add(jtfQuantityOriginal);

        jtfQuantityOriginalUnit.setEditable(false);
        jtfQuantityOriginalUnit.setText("UNIT");
        jtfQuantityOriginalUnit.setFocusable(false);
        jtfQuantityOriginalUnit.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel40.add(jtfQuantityOriginalUnit);

        jPanel13.add(jPanel40);

        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlQuantityRework.setText("Cantidad reproceso:");
        jlQuantityRework.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel41.add(jlQuantityRework);

        jtfQuantityRework.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfQuantityRework.setText("0");
        jtfQuantityRework.setPreferredSize(new java.awt.Dimension(100, 23));
        jtfQuantityRework.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfQuantityReworkFocusLost(evt);
            }
        });
        jPanel41.add(jtfQuantityRework);

        jtfQuantityReworkUnit.setEditable(false);
        jtfQuantityReworkUnit.setText("UNIT");
        jtfQuantityReworkUnit.setFocusable(false);
        jtfQuantityReworkUnit.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel41.add(jtfQuantityReworkUnit);

        jPanel13.add(jPanel41);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlQuantity.setText("Cantidad total:");
        jlQuantity.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel21.add(jlQuantity);

        jtfQuantity.setEditable(false);
        jtfQuantity.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfQuantity.setText("0");
        jtfQuantity.setFocusable(false);
        jtfQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel21.add(jtfQuantity);

        jtfQuantityUnit.setEditable(false);
        jtfQuantityUnit.setText("UNIT");
        jtfQuantityUnit.setFocusable(false);
        jtfQuantityUnit.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel21.add(jtfQuantityUnit);

        jPanel13.add(jPanel21);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCharges.setText("Cargas: *");
        jlCharges.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel27.add(jlCharges);

        jtfCharges.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfCharges.setText("0");
        jtfCharges.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jtfCharges);

        jlDummy10.setPreferredSize(new java.awt.Dimension(2, 23));
        jPanel27.add(jlDummy10);

        jPanel13.add(jPanel27);

        jPanel30.setPreferredSize(new java.awt.Dimension(230, 23));
        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha orden prod.: *");
        jlDate.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel30.add(jlDate);

        jtfDate.setText("DATE");
        jtfDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel30.add(jtfDate);

        jbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDate.setToolTipText("Seleccionar fecha");
        jbDate.setFocusable(false);
        jbDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel30.add(jbDate);

        jPanel13.add(jPanel30);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateDelivery.setText("Fecha entrega prog.: *");
        jlDateDelivery.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel12.add(jlDateDelivery);

        jtfDateDelivery.setText("DELIVERY DATE");
        jtfDateDelivery.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jtfDateDelivery);

        jbDateDelivery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateDelivery.setToolTipText("Seleccionar fecha");
        jbDateDelivery.setFocusable(false);
        jbDateDelivery.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbDateDelivery);

        jPanel13.add(jPanel12);

        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTurnDeliveryId.setText("Turno arranque prog.: *");
        jlFkTurnDeliveryId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel37.add(jlFkTurnDeliveryId);

        jcbFkTurnDeliveryId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel37.add(jcbFkTurnDeliveryId);

        jPanel13.add(jPanel37);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkOrdPriorityId.setText("Prioridad: *");
        jlFkOrdPriorityId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel11.add(jlFkOrdPriorityId);

        jcbFkOrdPriorityId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel11.add(jcbFkOrdPriorityId);

        jPanel13.add(jPanel11);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkProductionOrderId_n.setText("Orden prod. padre:");
        jlFkProductionOrderId_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel26.add(jlFkProductionOrderId_n);

        jcbFkProductionOrderId_n.setMaximumRowCount(16);
        jcbFkProductionOrderId_n.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel26.add(jcbFkProductionOrderId_n);

        jbFkProductionOrderId_n.setText("...");
        jbFkProductionOrderId_n.setToolTipText("Seleccionar orden producción padre");
        jbFkProductionOrderId_n.setFocusable(false);
        jbFkProductionOrderId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel26.add(jbFkProductionOrderId_n);

        jtfDummyProductionOrder_n.setEditable(false);
        jtfDummyProductionOrder_n.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jtfDummyProductionOrder_n.setText("0");
        jtfDummyProductionOrder_n.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel26.add(jtfDummyProductionOrder_n);

        jPanel13.add(jPanel26);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkBizPartnerId_n.setText("Cliente: ");
        jlFkBizPartnerId_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel31.add(jlFkBizPartnerId_n);

        jcbFkBizPartnerId_n.setMaximumRowCount(12);
        jcbFkBizPartnerId_n.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel31.add(jcbFkBizPartnerId_n);

        jbFkBizPartnerId_n.setText("...");
        jbFkBizPartnerId_n.setToolTipText("Seleccionar cliente");
        jbFkBizPartnerId_n.setFocusable(false);
        jbFkBizPartnerId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel31.add(jbFkBizPartnerId_n);

        jPanel13.add(jPanel31);

        jPanel38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumberReference_n.setText("Pedido cliente:");
        jlNumberReference_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel38.add(jlNumberReference_n);

        jtfNumberReference_n.setText("ORDER");
        jtfNumberReference_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel38.add(jtfNumberReference_n);

        jPanel13.add(jPanel38);

        jPanel42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkBizPartnerOperatorId_n.setText("Operador: ");
        jlFkBizPartnerOperatorId_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel42.add(jlFkBizPartnerOperatorId_n);

        jcbFkBizPartnerOperatorId_n.setMaximumRowCount(12);
        jcbFkBizPartnerOperatorId_n.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel42.add(jcbFkBizPartnerOperatorId_n);

        jbFkBizPartnerOperatorId_n.setText("...");
        jbFkBizPartnerOperatorId_n.setToolTipText("Seleccionar operador");
        jbFkBizPartnerOperatorId_n.setFocusable(false);
        jbFkBizPartnerOperatorId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel42.add(jbFkBizPartnerOperatorId_n);

        jPanel13.add(jPanel42);

        jPanel3.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Estado del documento:"));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 33));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(new java.awt.GridLayout(18, 2, 0, 1));

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumber.setText("Folio orden prod.:");
        jlNumber.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel25.add(jlNumber);

        jtfNumber.setEditable(false);
        jtfNumber.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jtfNumber.setText("NUMBER");
        jtfNumber.setFocusable(false);
        jtfNumber.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel25.add(jtfNumber);

        jPanel1.add(jPanel25);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDbmsStatus.setText("Estado orden prod.:");
        jlDbmsStatus.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel10.add(jlDbmsStatus);

        jtfDbmsStatus.setEditable(false);
        jtfDbmsStatus.setText("STATUS");
        jtfDbmsStatus.setFocusable(false);
        jtfDbmsStatus.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel10.add(jtfDbmsStatus);

        jPanel1.add(jPanel10);

        jPanel39.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDbmsExplotionMaterialsReference.setText("Explosión de materiales:");
        jlDbmsExplotionMaterialsReference.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel39.add(jlDbmsExplotionMaterialsReference);

        jtfDbmsExplotionMaterialsReference.setEditable(false);
        jtfDbmsExplotionMaterialsReference.setText("EXPLOTION MATERIALS");
        jtfDbmsExplotionMaterialsReference.setFocusable(false);
        jtfDbmsExplotionMaterialsReference.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel39.add(jtfDbmsExplotionMaterialsReference);

        jPanel1.add(jPanel39);

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkLotId_n.setText("Lote/fecha caducidad:");
        jlFkLotId_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel29.add(jlFkLotId_n);

        jtfFkLotId_n.setEditable(false);
        jtfFkLotId_n.setText("LOT");
        jtfFkLotId_n.setFocusable(false);
        jtfFkLotId_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel29.add(jtfFkLotId_n);

        jlDummy16.setText(" /");
        jlDummy16.setFocusable(false);
        jlDummy16.setPreferredSize(new java.awt.Dimension(8, 23));
        jPanel29.add(jlDummy16);

        jtfDbmsLotDateExpired.setEditable(false);
        jtfDbmsLotDateExpired.setText("DATE EXPIRED");
        jtfDbmsLotDateExpired.setFocusable(false);
        jtfDbmsLotDateExpired.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel29.add(jtfDbmsLotDateExpired);

        jPanel1.add(jPanel29);

        jPanel23.setPreferredSize(new java.awt.Dimension(210, 23));
        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateLot_n.setText("Fecha en pesado:");
        jlDateLot_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel23.add(jlDateLot_n);

        jtfDateLot_n.setEditable(false);
        jtfDateLot_n.setText("LOT DATE");
        jtfDateLot_n.setFocusable(false);
        jtfDateLot_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel23.add(jtfDateLot_n);

        jPanel1.add(jPanel23);

        jPanel35.setPreferredSize(new java.awt.Dimension(210, 23));
        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateLotAssigned_n.setText("Fecha en piso:");
        jlDateLotAssigned_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel35.add(jlDateLotAssigned_n);

        jtfDateLotAssigned_n.setEditable(false);
        jtfDateLotAssigned_n.setText("LOT ASSIG DATE");
        jtfDateLotAssigned_n.setFocusable(false);
        jtfDateLotAssigned_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel35.add(jtfDateLotAssigned_n);

        jtfTsLotAssigned_n.setEditable(false);
        jtfTsLotAssigned_n.setText("TS ASSIG LOT");
        jtfTsLotAssigned_n.setToolTipText("Marca de tiempo 'Fecha lotes asignados'");
        jtfTsLotAssigned_n.setFocusable(false);
        jtfTsLotAssigned_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel35.add(jtfTsLotAssigned_n);

        jPanel1.add(jPanel35);

        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTurnLotAssignedId.setText("Turno en piso:");
        jlFkTurnLotAssignedId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel36.add(jlFkTurnLotAssignedId);

        jcbFkTurnLotAssignedId.setEnabled(false);
        jcbFkTurnLotAssignedId.setFocusable(false);
        jcbFkTurnLotAssignedId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel36.add(jcbFkTurnLotAssignedId);

        jtfDbmsUserLotAssigned.setEditable(false);
        jtfDbmsUserLotAssigned.setText("USER ASSIG LOT");
        jtfDbmsUserLotAssigned.setToolTipText("Usuario 'Turno lotes asignados'");
        jtfDbmsUserLotAssigned.setFocusable(false);
        jtfDbmsUserLotAssigned.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel36.add(jtfDbmsUserLotAssigned);

        jPanel1.add(jPanel36);

        jPanel14.setPreferredSize(new java.awt.Dimension(230, 23));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart_n.setText("Fecha en proceso:");
        jlDateStart_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel14.add(jlDateStart_n);

        jtfDateStart_n.setEditable(false);
        jtfDateStart_n.setText("START DATE");
        jtfDateStart_n.setFocusable(false);
        jtfDateStart_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel14.add(jtfDateStart_n);

        jtfTsStart_n.setEditable(false);
        jtfTsStart_n.setText("TS START");
        jtfTsStart_n.setToolTipText("Marca de tiempo 'Fecha inicio'");
        jtfTsStart_n.setFocusable(false);
        jtfTsStart_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel14.add(jtfTsStart_n);

        jPanel1.add(jPanel14);

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTurnStartId.setText("Turno en proceso:");
        jlFkTurnStartId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel32.add(jlFkTurnStartId);

        jcbFkTurnStartId.setEnabled(false);
        jcbFkTurnStartId.setFocusable(false);
        jcbFkTurnStartId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel32.add(jcbFkTurnStartId);

        jtfDbmsUserStart.setEditable(false);
        jtfDbmsUserStart.setText("USER START");
        jtfDbmsUserStart.setToolTipText("Usuario 'Turno inicio'");
        jtfDbmsUserStart.setFocusable(false);
        jtfDbmsUserStart.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel32.add(jtfDbmsUserStart);

        jPanel1.add(jPanel32);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd_n.setText("Fecha termino: ");
        jlDateEnd_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel22.add(jlDateEnd_n);

        jtfDateEnd_n.setEditable(false);
        jtfDateEnd_n.setText("END DATE");
        jtfDateEnd_n.setFocusable(false);
        jtfDateEnd_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel22.add(jtfDateEnd_n);

        jtfTsEnd_n.setEditable(false);
        jtfTsEnd_n.setText("TS END");
        jtfTsEnd_n.setToolTipText("Marca de tiempo 'Fecha termino'");
        jtfTsEnd_n.setFocusable(false);
        jtfTsEnd_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel22.add(jtfTsEnd_n);

        jPanel1.add(jPanel22);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTurnEndId.setText("Turno termino:");
        jlFkTurnEndId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel33.add(jlFkTurnEndId);

        jcbFkTurnEndId.setEnabled(false);
        jcbFkTurnEndId.setFocusable(false);
        jcbFkTurnEndId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel33.add(jcbFkTurnEndId);

        jtfDbmsUserEnd.setEditable(false);
        jtfDbmsUserEnd.setText("USER END");
        jtfDbmsUserEnd.setToolTipText("Usuario 'Turno termino'");
        jtfDbmsUserEnd.setFocusable(false);
        jtfDbmsUserEnd.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel33.add(jtfDbmsUserEnd);

        jPanel1.add(jPanel33);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateClose_n.setText("Fecha cierre: ");
        jlDateClose_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel28.add(jlDateClose_n);

        jtfDateClose_n.setEditable(false);
        jtfDateClose_n.setText("CLOSE DATE");
        jtfDateClose_n.setFocusable(false);
        jtfDateClose_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel28.add(jtfDateClose_n);

        jtfTsClose_n.setEditable(false);
        jtfTsClose_n.setText("TS CLOSE");
        jtfTsClose_n.setToolTipText("Marca de tiempo 'Fecha cierre'");
        jtfTsClose_n.setFocusable(false);
        jtfTsClose_n.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel28.add(jtfTsClose_n);

        jPanel1.add(jPanel28);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkTurnCloseId.setText("Turno cierre:");
        jlFkTurnCloseId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel34.add(jlFkTurnCloseId);

        jcbFkTurnCloseId.setEnabled(false);
        jcbFkTurnCloseId.setFocusable(false);
        jcbFkTurnCloseId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel34.add(jcbFkTurnCloseId);

        jtfDbmsUserClose.setEditable(false);
        jtfDbmsUserClose.setText("USER CLOSE");
        jtfDbmsUserClose.setToolTipText("Usuario 'Turno cierre'");
        jtfDbmsUserClose.setFocusable(false);
        jtfDbmsUserClose.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel34.add(jtfDbmsUserClose);

        jPanel1.add(jPanel34);
        jPanel1.add(jPanel7);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jckIsForecast.setText("Pronóstico");
        jckIsForecast.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel24.add(jckIsForecast);

        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel24.add(jckIsDeleted);

        jPanel1.add(jPanel24);

        jPanel3.add(jPanel1, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle:"));
        jTabbedPane1.setFocusable(false);

        jpCharges.setFocusable(false);
        jpCharges.setLayout(new java.awt.BorderLayout());

        jPanel6.setFocusable(false);
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jbChargesNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbChargesNew.setToolTipText("Crear");
        jbChargesNew.setEnabled(false);
        jbChargesNew.setFocusable(false);
        jbChargesNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbChargesNew);

        jbChargesEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbChargesEdit.setToolTipText("Modificar");
        jbChargesEdit.setFocusable(false);
        jbChargesEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbChargesEdit);

        jbChargesDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbChargesDelete.setToolTipText("Modificar");
        jbChargesDelete.setEnabled(false);
        jbChargesDelete.setFocusable(false);
        jbChargesDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbChargesDelete);

        jpCharges.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Cargas", jpCharges);

        jpPeriods.setFocusable(false);
        jpPeriods.setLayout(new java.awt.BorderLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(61, 23));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jbPeriodNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbPeriodNew.setToolTipText("Crear");
        jbPeriodNew.setFocusable(false);
        jbPeriodNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbPeriodNew);

        jbPeriodEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbPeriodEdit.setToolTipText("Modificar");
        jbPeriodEdit.setFocusable(false);
        jbPeriodEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbPeriodEdit);

        jbPeriodDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbPeriodDelete.setToolTipText("Eliminar");
        jbPeriodDelete.setEnabled(false);
        jbPeriodDelete.setFocusable(false);
        jbPeriodDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbPeriodDelete);

        jpPeriods.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Períodos", jpPeriods);

        jpNotes.setFocusable(false);
        jpNotes.setLayout(new java.awt.BorderLayout());

        jPanel5.setFocusable(false);
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jbNotesNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_new.gif"))); // NOI18N
        jbNotesNew.setToolTipText("Crear");
        jbNotesNew.setFocusable(false);
        jbNotesNew.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbNotesNew);

        jbNotesEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbNotesEdit.setToolTipText("Modificar");
        jbNotesEdit.setFocusable(false);
        jbNotesEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbNotesEdit);

        jbNotesDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbNotesDelete.setToolTipText("Modificar");
        jbNotesDelete.setEnabled(false);
        jbNotesDelete.setFocusable(false);
        jbNotesDelete.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbNotesDelete);

        jpNotes.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Notas", jpNotes);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setFocusable(false);
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(1040, 729));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jcbFkOrdTypeIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkOrdTypeIdItemStateChanged
        itemStateChangedFkOrdTypeId();
    }//GEN-LAST:event_jcbFkOrdTypeIdItemStateChanged

    private void jcbFkItemIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkItemIdItemStateChanged
        itemStateChangedFkItemId();
    }//GEN-LAST:event_jcbFkItemIdItemStateChanged

    private void jcbFkCobIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkCobIdItemStateChanged
        itemStateChangedFkCobIdItem();
}//GEN-LAST:event_jcbFkCobIdItemStateChanged

    private void jcbFkBomIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jcbFkBomIdFocusLost
        itemFocusLostFkBomId();
    }//GEN-LAST:event_jcbFkBomIdFocusLost

    private void jtfReferenceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfReferenceFocusGained
        referenceFocusGained();
    }//GEN-LAST:event_jtfReferenceFocusGained

    private void jtfQuantityOriginalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfQuantityOriginalFocusLost
        actionQuantityOriginalFocusLost();
    }//GEN-LAST:event_jtfQuantityOriginalFocusLost

    private void jtfQuantityReworkFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfQuantityReworkFocusLost
        actionQuantityReworkFocusLost();
    }//GEN-LAST:event_jtfQuantityReworkFocusLost

    private void jcbFkBomIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkBomIdItemStateChanged
        actionFkBomIdItemStateChanged();
    }//GEN-LAST:event_jcbFkBomIdItemStateChanged

    private void initComponentsExtra() {
        int i = 0;
        mnIndex = 0;

        mbIsUniversalCompany = miClient.getSessionXXX().getIsUniversal() || miClient.getSessionXXX().getIsUniversalCompany(miClient.getSessionXXX().getCompany().getPkCompanyId());

        STableColumnForm oColsPeriod[] = null;
        STableColumnForm oColsNotes[] = null;
        STableColumnForm oColsCharges[] = null;

        moProductionOrderNotes = null;
        moProductionOrderNotesRow = null;
        moProductionOrderPeriod = null;
        moProductionOrderPeriodRow = null;
        moProductionOrderCharges = null;
        moProductionOrderChargesRow = null;
        moProductionOrderNotesForm = new SFormProductionOrderNotes(miClient);
        moProductionOrderPeriodForm = new SFormProductionOrderPeriod(miClient);
        moProductionOrderChargesForm = new SFormProductionOrderCharge(miClient);

        mvFields = new Vector<SFormField>();
        moProductionOrderPeriodsPane = new STablePane(miClient);
        moProductionOrderPeriodsPane.setDoubleClickAction(this, "publicActionPeriodEdit");
        jpPeriods.add(moProductionOrderPeriodsPane, BorderLayout.CENTER);
        moProductionOrderNotesPane = new STablePane(miClient);
        moProductionOrderNotesPane.setDoubleClickAction(this, "publicActionNotesEdit");
        jpNotes.add(moProductionOrderNotesPane, BorderLayout.CENTER);
        moProductionOrderChargesPane = new STablePane(miClient);
        moProductionOrderChargesPane.setDoubleClickAction(this, "publicActionChargesEdit");
        jpCharges.add(moProductionOrderChargesPane, BorderLayout.CENTER);
        moComboBoxItemBom = new SFormComboBoxGroup(miClient);

        moFieldFkCobId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCobId, jlFkCobId);
        moFieldFkCobId.setPickerButton(jbFkCobId);
        moFieldFkEntityId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkEntityId, jlFkEntityId);
        moFieldFkEntityId.setPickerButton(jbFkEntityId);
        moFieldFkOrdTypeId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkOrdTypeId, jlFkOrdTypeId);
        moFieldFkItemId_r = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkItemId, jlFkItemId);
        moFieldFkItemId_r.setPickerButton(jbFkItemId);
        moFieldFkBomId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkBomId, jlFkBomId);
        moFieldFkBomId.setPickerButton(jbFkBomId);
        moFieldReference = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfReference, jlReference);
        moFieldReference.setLengthMax(50);
        moFieldQuantityOriginal = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfQuantityOriginal, jlQuantityOriginal);
        moFieldQuantityRework = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfQuantityRework, jlQuantityRework);
        moFieldQuantity = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfQuantity, jlQuantity);
        moFieldCharges = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfCharges, jlCharges);
        moFieldFkLotId_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFkLotId_n, jlFkLotId_n);
        moFieldDate = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jtfDate, jlDate);
        moFieldDate.setPickerButton(jbDate);
        moFieldFkOrdPriorityId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkOrdPriorityId, jlFkOrdPriorityId);
        moFieldFkBizPartnerId_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkBizPartnerId_n, jlFkBizPartnerId_n);
        moFieldFkBizPartnerId_n.setPickerButton(jbFkBizPartnerId_n);
        moFieldFkBizPartnerOperatorId_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkBizPartnerOperatorId_n, jlFkBizPartnerOperatorId_n);
        moFieldFkBizPartnerOperatorId_n.setPickerButton(jbFkBizPartnerOperatorId_n);
        moFieldNumberReference_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNumberReference_n, jlNumberReference_n);
        moFieldNumberReference_n.setLengthMax(25);
        moFieldFkProductionOrderId_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkProductionOrderId_n, jlFkProductionOrderId_n);
        moFieldFkProductionOrderId_n.setPickerButton(jbFkProductionOrderId_n);
        moFieldNumber = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNumber, jlNumber);
        moFieldDbmsExplotionMaterialsReference = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfDbmsExplotionMaterialsReference, jlDbmsExplotionMaterialsReference);
        moFieldDbmsProductionOrderStatus = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfDbmsStatus, jlDbmsStatus);
        moFieldDateDelivery = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jtfDateDelivery, jlDateDelivery);
        moFieldDateDelivery.setPickerButton(jbDateDelivery);
        moFieldDateLot_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jtfDateLot_n, jlDateLot_n);
        moFieldDateLotAssigned_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jtfDateLotAssigned_n, jlDateLotAssigned_n);
        moFieldDbmsLotDateExpired = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jtfDbmsLotDateExpired, jlFkLotId_n);
        moFieldTsLotAssigned_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE_TIME, false, jtfTsLotAssigned_n, jlDateLotAssigned_n);
        moFieldFkTurnDeliveryId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTurnDeliveryId, jlFkTurnDeliveryId);
        moFieldFkTurnLotAssigedId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTurnLotAssignedId, jlFkTurnLotAssignedId);
        moFieldDbmsUserLotAssigned = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfDbmsUserLotAssigned, jlFkTurnLotAssignedId);
        moFieldDateStart_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jtfDateStart_n, jlDateStart_n);
        moFieldTsStart_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE_TIME, false, jtfTsStart_n, jlDateStart_n);
        moFieldFkTurnStartId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTurnStartId, jlFkTurnStartId);
        moFieldDbmsUserStart = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfDbmsUserStart, jlFkTurnStartId);
        moFieldDateEnd_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jtfDateEnd_n, jlDateEnd_n);
        moFieldTsEnd_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE_TIME, false, jtfTsEnd_n, jlDateEnd_n);
        moFieldFkTurnEndId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTurnEndId, jlFkTurnEndId);
        moFieldDbmsUserEnd = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfDbmsUserEnd, jlFkTurnEndId);
        moFieldDateClose_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jtfDateClose_n, jlDateClose_n);
        moFieldTsClose_n = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE_TIME, false, jtfTsClose_n, jlDateClose_n);
        moFieldFkTurnCloseId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkTurnCloseId, jlFkTurnCloseId);
        moFieldDbmsUserClose = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfDbmsUserClose, jlFkTurnCloseId);
        moFieldIsForecast = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsForecast);
        moFieldIsDeleted = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);

        mvFields.add(moFieldFkCobId);
        mvFields.add(moFieldFkEntityId);
        mvFields.add(moFieldFkOrdTypeId);
        mvFields.add(moFieldFkItemId_r);
        mvFields.add(moFieldFkBomId);
        mvFields.add(moFieldReference);
        mvFields.add(moFieldQuantityOriginal);
        mvFields.add(moFieldQuantityRework);
        mvFields.add(moFieldQuantity);
        mvFields.add(moFieldCharges);
        mvFields.add(moFieldFkLotId_n);
        mvFields.add(moFieldDate);
        mvFields.add(moFieldFkOrdPriorityId);
        mvFields.add(moFieldFkBizPartnerId_n);
        mvFields.add(moFieldFkBizPartnerOperatorId_n);
        mvFields.add(moFieldNumberReference_n);
        mvFields.add(moFieldFkProductionOrderId_n);
        mvFields.add(moFieldNumber);
        mvFields.add(moFieldDbmsProductionOrderStatus);
        mvFields.add(moFieldDbmsExplotionMaterialsReference);
        mvFields.add(moFieldDateDelivery);
        mvFields.add(moFieldFkTurnDeliveryId);
        mvFields.add(moFieldDateLot_n);
        mvFields.add(moFieldDateLotAssigned_n);
        mvFields.add(moFieldTsLotAssigned_n);
        mvFields.add(moFieldFkTurnLotAssigedId);
        mvFields.add(moFieldDbmsUserLotAssigned);
        mvFields.add(moFieldDateStart_n);
        mvFields.add(moFieldTsStart_n);
        mvFields.add(moFieldFkTurnStartId);
        mvFields.add(moFieldDbmsUserStart);
        mvFields.add(moFieldDateEnd_n);
        mvFields.add(moFieldTsEnd_n);
        mvFields.add(moFieldFkTurnEndId);
        mvFields.add(moFieldDbmsUserEnd);
        mvFields.add(moFieldDateClose_n);
        mvFields.add(moFieldDbmsLotDateExpired);
        mvFields.add(moFieldTsClose_n);
        mvFields.add(moFieldFkTurnCloseId);
        mvFields.add(moFieldDbmsUserClose);
        mvFields.add(moFieldIsForecast);
        mvFields.add(moFieldIsDeleted);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbPeriodNew.addActionListener(this);
        jbPeriodEdit.addActionListener(this);
        jbChargesEdit.addActionListener(this);
        jbNotesNew.addActionListener(this);
        jbNotesEdit.addActionListener(this);
        jbCopyReference.addActionListener(this);
        jbDate.addActionListener(this);
        jbDateDelivery.addActionListener(this);
        jbFkItemId.addActionListener(this);
        jbFkCobId.addActionListener(this);
        jbFkEntityId.addActionListener(this);
        jbFkBomId.addActionListener(this);
        jbFkBizPartnerId_n.addActionListener(this);
        jbFkBizPartnerOperatorId_n.addActionListener(this);
        jbFkProductionOrderId_n.addActionListener(this);

        i = 0;
        oColsPeriod = new STableColumnForm[4];
        oColsPeriod[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "F. inicial", STableConstants.WIDTH_DATE);
        oColsPeriod[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "F. final", STableConstants.WIDTH_DATE);
        oColsPeriod[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cantidad", STableConstants.WIDTH_QUANTITY);
        oColsPeriod[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        for (i = 0; i < oColsPeriod.length; i++) {
            moProductionOrderPeriodsPane.addTableColumn(oColsPeriod[i]);
        }
        moProductionOrderPeriodsPane.createTable(null);

        i = 0;
        oColsCharges = new STableColumnForm[8];
        oColsCharges[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "# carga", 50);
        oColsCharges[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        oColsCharges[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        oColsCharges[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        oColsCharges[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        oColsCharges[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        oColsCharges[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        oColsCharges[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < oColsCharges.length; i++) {
            moProductionOrderChargesPane.addTableColumn(oColsCharges[i]);
        }
        moProductionOrderChargesPane.createTable(null);

        i = 0;
        oColsNotes = new STableColumnForm[8];
        oColsNotes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Notas", 300);
        oColsNotes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        oColsNotes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        oColsNotes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        oColsNotes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        oColsNotes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        oColsNotes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        oColsNotes[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < oColsNotes.length; i++) {
            moProductionOrderNotesPane.addTableColumn(oColsNotes[i]);
        }
        moProductionOrderNotesPane.createTable(null);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(true); }
        };
        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };
        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            if (jcbFkCobId.isEnabled()) {
                jcbFkCobId.requestFocus();
            }
            else if (jcbFkItemId.isEnabled()) {
                jcbFkItemId.requestFocus();
            }
            else {
                jbChargesEdit.requestFocus();
            }
        }
    }

    private void itemStateChangedFkCobIdItem() {

        if (jcbFkCobId.getSelectedIndex() <= 0) {
            mvItemsEntities = new Vector<SFormComponentItem>();
        }
        else {
            if (isUniversalCompanyBranch()) {
                mvItemsEntities = miClient.getSessionXXX().getAllCompanyBranchEntitiesAsComponentItems(moFieldFkCobId.getKeyAsIntArray()[0], SDataConstantsSys.CFGS_CT_ENT_PLANT, true);
            }
            else {
                mvItemsEntities = miClient.getSessionXXX().getUserCompanyBranchEntitiesAsComponentItems(moFieldFkCobId.getKeyAsIntArray()[0], SDataConstantsSys.CFGS_CT_ENT_PLANT, true);
            }
        }

        jcbFkEntityId.removeAllItems();
        for (SFormComponentItem item : mvItemsEntities) {
            jcbFkEntityId.addItem(item);
        }

        if (jcbFkEntityId.getItemCount() > 1) {
            jcbFkEntityId.setEnabled(true);
            jbFkEntityId.setEnabled(true);

            if (jcbFkEntityId.getItemCount() == 2) {
                jcbFkEntityId.setSelectedIndex(1);
            }
        }
        else {
            jcbFkEntityId.setEnabled(false);
            jbFkEntityId.setEnabled(false);
        }
    }

    private void actionQuantityOriginalFocusLost() {
        moFieldQuantity.setFieldValue(moFieldQuantityOriginal.getDouble() + moFieldQuantityRework.getDouble());
    }

    private void actionQuantityReworkFocusLost() {
        moFieldQuantity.setFieldValue(moFieldQuantityOriginal.getDouble() + moFieldQuantityRework.getDouble());
    }

    private void referenceFocusGained() {
        if (moFieldReference.getString().length() == 0 && jcbFkBomId.getSelectedIndex() > 0) {
            moFieldReference.setString(jcbFkBomId.getSelectedItem().toString());
        }
    }

    private void itemStateChangedFkItemId() {
        if (jcbFkItemId.getSelectedIndex() > 0) {
            moItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldFkItemId_r.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);

            if (jcbFkBomId.getItemCount() > 1) {
                if (jcbFkBomId.getItemCount() == 2) {
                    jcbFkBomId.setSelectedIndex(1);
                }
            }
        }
    }

    private void actionFkBomIdItemStateChanged() {
        if (jcbFkBomId.getSelectedIndex() > 0) {
            moBom = (SDataBom) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_BOM, moFieldFkBomId.getKey(), SLibConstants.EXEC_MODE_VERBOSE);
            moUnit = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { moBom.getFkUnitId() }, SLibConstants.EXEC_MODE_VERBOSE);
            jtfQuantityUnit.setText(moUnit.getSymbol());
            jtfQuantityOriginalUnit.setText(moUnit.getSymbol());
            jtfQuantityReworkUnit.setText(moUnit.getSymbol());
        }
    }

    private void itemStateChangedFkOrdTypeId() {
        if (jcbFkOrdTypeId.getSelectedIndex() >= 0) {
            if (jcbFkOrdTypeId.isEnabled()) {
                switch (moFieldFkOrdTypeId.getKeyAsIntArray()[0]) {
                    case SDataConstantsSys.MFGS_TP_ORD_CONTINUE:
                        jtfQuantityOriginal.setEnabled(false);
                        jtfQuantityOriginal.setFocusable(false);
                        moFieldQuantityOriginal.setIsMandatory(false);
                        jbPeriodNew.setEnabled(true);
                        jbPeriodNew.setFocusable(true);
                        jbPeriodEdit.setEnabled(true);
                        jbPeriodEdit.setFocusable(true);
                        jcbFkProductionOrderId_n.setEnabled(false);
                        jcbFkProductionOrderId_n.setFocusable(false);
                        jbFkProductionOrderId_n.setEnabled(false);
                        break;
                    default:
                        jtfQuantityOriginal.setEnabled(true);
                        jtfQuantityOriginal.setFocusable(true);
                        moFieldQuantityOriginal.setIsMandatory(true);
                        jbPeriodNew.setEnabled(false);
                        jbPeriodNew.setFocusable(false);
                        jbPeriodEdit.setEnabled(false);
                        jbPeriodEdit.setFocusable(false);
                        jcbFkProductionOrderId_n.setEnabled(true);
                        jcbFkProductionOrderId_n.setFocusable(true);
                        jbFkProductionOrderId_n.setEnabled(true);
                        break;
                }
            }

            if (((SFormComponentItem) jcbFkOrdTypeId.getModel().getSelectedItem()).getComplement() != null) {
                if (((Boolean)((SFormComponentItem) jcbFkOrdTypeId.getModel().getSelectedItem()).getComplement()).booleanValue()) {
                    jcbFkProductionOrderId_n.setEnabled(true);
                    jbFkProductionOrderId_n.setEnabled(true);
                }
                else {
                    jcbFkProductionOrderId_n.setEnabled(false);
                    jbFkProductionOrderId_n.setEnabled(false);
                }
            }
        }
    }

    private void itemFocusLostFkBomId() {
        if (jcbFkBomId.getSelectedIndex() > 0) {
            if (moFieldQuantityOriginal.getDouble() <= 0) {
                moFieldQuantityOriginal.setFieldValue(SLibUtilities.parseDouble(((erp.lib.form.SFormComponentItem) jcbFkBomId.getModel().getSelectedItem()).getComplement().toString()));
            }

            if (moFieldReference.getString().length() == 0) {
                moFieldReference.setString(jcbFkBomId.getSelectedItem().toString());
            }
        }
    }

    private void actionFkCobId() {
        miClient.pickOption(SDataConstants.BPSU_BPB, moFieldFkCobId, new int[] { miClient.getSessionXXX().getParamsCompany().getPkConfigCoId() });
    }

    private void actionFkEntityId() {
        miClient.pickOption(SDataConstants.CFGU_COB_ENT, moFieldFkEntityId, new int[] { moFieldFkEntityId.getKeyAsIntArray()[0], SDataConstantsSys.CFGS_CT_ENT_PLANT });
    }

    private void actionFkItemId() {
        miClient.pickOption(SDataConstants.ITMX_ITEM_BOM_ITEM, moFieldFkItemId_r, null);
    }

    private void actionFkBomId() {
        miClient.pickOption(SDataConstants.MFG_BOM, moFieldFkBomId, moFieldFkItemId_r.getKeyAsIntArray());
    }

    private void actionFkBizPartnerId_n() {
        miClient.pickOption(SDataConstants.BPSX_BP_CUS, moFieldFkBizPartnerId_n, moFieldFkBizPartnerId_n.getKey());
    }

    private void actionFkBizPartnerOperatorId_n() {
        miClient.pickOption(SDataConstants.BPSX_BP_ATT_EMP_MFG, moFieldFkBizPartnerOperatorId_n, moFieldFkBizPartnerOperatorId_n.getKey());
    }

    private void actionCopyReference() {
        boolean b = false;

        if (jcbFkBomId.getSelectedIndex() > 0) {
            if (jtfReference.getText().length() == 0) {
                b = true;
            }
            else {
                if (miClient.showMsgBoxConfirm("¿Está seguro(a) de reemplazar el texto del campo 'Referencia'?") == JOptionPane.YES_OPTION) {
                    b = true;
                }
            }

            if (b) {
                if (jcbFkBomId.getSelectedItem().toString().length() > 50) {
                    jtfReference.setText(jcbFkBomId.getSelectedItem().toString().substring(0, 49));
                }
                else {
                    jtfReference.setText(jcbFkBomId.getSelectedItem().toString());
                }
            }
        }
    }

    private void actionDate() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDate.getDate(), moFieldDate);
    }

    private void actionSetDateDelivery() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateDelivery.getDate(), moFieldDateDelivery);
    }

    private void actionFkProductionOrderId_n() {
        miClient.pickOption(SDataConstants.MFG_ORD, moFieldFkProductionOrderId_n, new Object[] { "" + SDataConstantsSys.MFGS_ST_ORD_NEW, SDataConstants.MFGX_ORD_MAIN_FA, false });
    }

    private void enabledFields(boolean b) {
        jcbFkItemId.setEnabled(b);
        jbFkItemId.setEnabled(b);
        jcbFkBomId.setEnabled(b);
        jbFkBomId.setEnabled(b);
        jtfReference.setEditable(b);
        jbCopyReference.setEnabled(b);
        jtfQuantityOriginal.setEditable(b);
        jtfCharges.setEditable(b);
        jtfDate.setEditable(b);
        jbDate.setEnabled(b);
        jtfDateDelivery.setEditable(b);
        jbDateDelivery.setEnabled(b);
        jcbFkTurnDeliveryId.setEnabled(b);
        jcbFkOrdPriorityId.setEnabled(b);
        jcbFkBizPartnerId_n.setEnabled(b);
        jbFkBizPartnerId_n.setEnabled(b);
        jcbFkBizPartnerOperatorId_n.setEnabled(b);
        jbFkBizPartnerOperatorId_n.setEnabled(b);
        jtfNumberReference_n.setEditable(b);
        jckIsForecast.setEnabled(b);
    }

    private boolean isUniversalCompany() {
        return mbIsUniversalCompany;
    }

    private boolean isUniversalCompanyBranch() {
        return isUniversalCompany() ||
                miClient.getSessionXXX().getIsUniversalCompanyBranch(moFieldFkCobId.getKeyAsIntArray()[0]) ||
                miClient.getSessionXXX().getIsUniversalCompanyBranchEntities(moFieldFkCobId.getKeyAsIntArray()[0], SDataConstantsSys.CFGS_CT_ENT_PLANT);
    }

    private boolean actionOk(boolean b) {
        if (jbOk.isEnabled()) {
            SFormValidation validation = formValidate();

            if (validation.getIsError()) {
                if (validation.getComponent() != null) {
                    validation.getComponent().requestFocus();
                }
                if (validation.getMessage().length() > 0) {
                    miClient.showMsgBoxWarning(validation.getMessage());
                }
                b = false;
            }
            else {
                mnFormResult = SLibConstants.FORM_RESULT_OK;

                if (b) {
                    setVisible(false);
                }
                b = true;
            }
        }

        return b;
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionChargesEdit() {
        mnIndex = moProductionOrderChargesPane.getTable().getSelectedRow();
        moProductionOrderChargesForm.formRefreshCatalogues();
        moProductionOrderChargesForm.formReset();
        moProductionOrderCharges = (SDataProductionOrderCharge) moProductionOrderChargesForm.getRegistry();

        if (mnIndex != -1) {
            moProductionOrderCharges = (SDataProductionOrderCharge) moProductionOrderChargesPane.getTableRow(mnIndex).getData();
            moProductionOrderChargesForm.setValue(1, moProductionOrder);
            moProductionOrderChargesForm.setRegistry(moProductionOrderCharges);
            moProductionOrderChargesForm.setVisible(true);

            if (moProductionOrderChargesForm.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                moProductionOrderCharges = (SDataProductionOrderCharge) moProductionOrderChargesForm.getRegistry();
                moProductionOrderChargesPane.setTableRow(moProductionOrderChargesRow = new SDataProductionOrderChargeRow(moProductionOrderCharges), mnIndex);
                moProductionOrderChargesPane.renderTableRows();
                moProductionOrderChargesPane.setTableRowSelection(mnIndex);
            }
        }
    }

    private void actionPeriodNew() {
        moProductionOrderPeriodForm.formRefreshCatalogues();
        moProductionOrderPeriodForm.formReset();
        moProductionOrderPeriodForm.setValue(1, jtfQuantityUnit.getText());
        moProductionOrderPeriodForm.setValue(2, (moProductionOrder != null ? moProductionOrder.getFkOrdStatusId() : 0));
        moProductionOrderPeriodForm.setVisible(true);

        if (moProductionOrderPeriodForm.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
            moProductionOrderPeriod = (SDataProductionOrderPeriod) moProductionOrderPeriodForm.getRegistry();
            moProductionOrderPeriodsPane.addTableRow(moProductionOrderPeriodRow = new SDataProductionOrderPeriodRow(moProductionOrderPeriod));
            moProductionOrderPeriodsPane.renderTableRows();
            mnIndex = moProductionOrderPeriodsPane.getTableGuiRowCount() - 1;
            moProductionOrderPeriodsPane.getTable().setRowSelectionInterval(mnIndex, mnIndex);
            moProductionOrderPeriodsPane.getVerticalScrollBar().setValue((mnIndex + 1) * moProductionOrderPeriodsPane.getTable().getRowHeight());
        }
    }

    private void actionPeriodEdit() {
        mnIndex = moProductionOrderPeriodsPane.getTable().getSelectedRow();
        moProductionOrderPeriodForm.formRefreshCatalogues();
        moProductionOrderPeriodForm.formReset();
        moProductionOrderPeriod = (SDataProductionOrderPeriod) moProductionOrderPeriodForm.getRegistry();

        if (mnIndex != -1) {
            moProductionOrderPeriod = (SDataProductionOrderPeriod) moProductionOrderPeriodsPane.getTableRow(mnIndex).getData();
            moProductionOrderPeriodForm.setRegistry(moProductionOrderPeriod);
            moProductionOrderPeriodForm.setValue(1, jtfQuantityUnit.getText());
            moProductionOrderPeriodForm.setValue(2, (moProductionOrder != null ? moProductionOrder.getFkOrdStatusId() : 0));
            moProductionOrderPeriodForm.setVisible(true);

            if (moProductionOrderPeriodForm.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                moProductionOrderPeriod = (SDataProductionOrderPeriod) moProductionOrderPeriodForm.getRegistry();
                moProductionOrderPeriodsPane.setTableRow(moProductionOrderPeriodRow = new SDataProductionOrderPeriodRow(moProductionOrderPeriod), mnIndex);
                moProductionOrderPeriodsPane.renderTableRows();
                moProductionOrderPeriodsPane.setTableRowSelection(mnIndex);
            }
        }
    }

    private void actionNotesNew() {
        moProductionOrderNotesForm.formRefreshCatalogues();
        moProductionOrderNotesForm.formReset();
        moProductionOrderNotesForm.setValue(1, (moProductionOrder != null ? moProductionOrder.getFkOrdStatusId() : 0));
        moProductionOrderNotesForm.setVisible(true);
        if (moProductionOrderNotesForm.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
            moProductionOrderNotes = (SDataProductionOrderNotes) moProductionOrderNotesForm.getRegistry();

            moProductionOrderNotes.setDbmsUserNew(miClient.getSessionXXX().getUser().getUser());
            moProductionOrderNotes.setDbmsUserEdit("(n/a)");
            moProductionOrderNotes.setDbmsUserDelete("(n/a)");
            moProductionOrderNotes.setUserNewTs(miClient.getSessionXXX().getWorkingDate());
            moProductionOrderNotes.setUserEditTs(miClient.getSessionXXX().getWorkingDate());
            moProductionOrderNotes.setUserDeleteTs(miClient.getSessionXXX().getWorkingDate());

            moProductionOrderNotesPane.addTableRow(moProductionOrderNotesRow = new SDataProductionOrderNotesRow(moProductionOrderNotes));
            moProductionOrderNotesPane.renderTableRows();
            mnIndex = moProductionOrderNotesPane.getTableGuiRowCount() - 1;
            moProductionOrderNotesPane.getTable().setRowSelectionInterval(mnIndex, mnIndex);
            moProductionOrderNotesPane.getVerticalScrollBar().setValue((mnIndex + 1) * moProductionOrderNotesPane.getTable().getRowHeight());
        }
    }

    private void actionNotesEdit() {
        mnIndex = moProductionOrderNotesPane.getTable().getSelectedRow();
        moProductionOrderNotesForm.formRefreshCatalogues();
        moProductionOrderNotesForm.formReset();
        moProductionOrderNotes = (SDataProductionOrderNotes) moProductionOrderNotesForm.getRegistry();

        if (mnIndex != -1) {
            moProductionOrderNotes = (SDataProductionOrderNotes) moProductionOrderNotesPane.getTableRow(mnIndex).getData();
            moProductionOrderNotesForm.setRegistry(moProductionOrderNotes);
            moProductionOrderNotesForm.setValue(1, (moProductionOrder != null ? moProductionOrder.getFkOrdStatusId() : 0));
            moProductionOrderNotesForm.setVisible(true);

            if (moProductionOrderNotesForm.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                moProductionOrderNotes = (SDataProductionOrderNotes) moProductionOrderNotesForm.getRegistry();
                moProductionOrderNotes.setDbmsUserEdit(miClient.getSessionXXX().getUser().getUser());
                moProductionOrderNotes.setUserEditTs(miClient.getSessionXXX().getWorkingDate());

                if (moProductionOrderNotes.getIsDeleted()) {
                    moProductionOrderNotes.setDbmsUserDelete(miClient.getSessionXXX().getUser().getUser());
                    moProductionOrderNotes.setUserDeleteTs(miClient.getSessionXXX().getWorkingDate());
                }

                moProductionOrderNotesPane.setTableRow(moProductionOrderNotesRow = new SDataProductionOrderNotesRow(moProductionOrderNotes), mnIndex);
                moProductionOrderNotesPane.renderTableRows();
                moProductionOrderNotesPane.setTableRowSelection(mnIndex);
            }
        }
    }

    public void publicActionPeriodEdit() {
        actionPeriodEdit();
    }

    public void publicActionChargesEdit() {
        actionChargesEdit();
    }

    public void publicActionNotesEdit() {
        actionNotesEdit();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
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
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbChargesDelete;
    private javax.swing.JButton jbChargesEdit;
    private javax.swing.JButton jbChargesNew;
    private javax.swing.JButton jbCopyReference;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbDateDelivery;
    private javax.swing.JButton jbFkBizPartnerId_n;
    private javax.swing.JButton jbFkBizPartnerOperatorId_n;
    private javax.swing.JButton jbFkBomId;
    private javax.swing.JButton jbFkCobId;
    private javax.swing.JButton jbFkEntityId;
    private javax.swing.JButton jbFkItemId;
    private javax.swing.JButton jbFkProductionOrderId_n;
    private javax.swing.JButton jbNotesDelete;
    private javax.swing.JButton jbNotesEdit;
    private javax.swing.JButton jbNotesNew;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbPeriodDelete;
    private javax.swing.JButton jbPeriodEdit;
    private javax.swing.JButton jbPeriodNew;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBizPartnerId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBizPartnerOperatorId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBomId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCobId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkEntityId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkOrdPriorityId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkOrdTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkProductionOrderId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTurnCloseId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTurnDeliveryId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTurnEndId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTurnLotAssignedId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTurnStartId;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsForecast;
    private javax.swing.JLabel jlCharges;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDateClose_n;
    private javax.swing.JLabel jlDateDelivery;
    private javax.swing.JLabel jlDateEnd_n;
    private javax.swing.JLabel jlDateLotAssigned_n;
    private javax.swing.JLabel jlDateLot_n;
    private javax.swing.JLabel jlDateStart_n;
    private javax.swing.JLabel jlDbmsExplotionMaterialsReference;
    private javax.swing.JLabel jlDbmsStatus;
    private javax.swing.JLabel jlDummy10;
    private javax.swing.JLabel jlDummy16;
    private javax.swing.JLabel jlFkBizPartnerId_n;
    private javax.swing.JLabel jlFkBizPartnerOperatorId_n;
    private javax.swing.JLabel jlFkBomId;
    private javax.swing.JLabel jlFkCobId;
    private javax.swing.JLabel jlFkEntityId;
    private javax.swing.JLabel jlFkItemId;
    private javax.swing.JLabel jlFkLotId_n;
    private javax.swing.JLabel jlFkOrdPriorityId;
    private javax.swing.JLabel jlFkOrdTypeId;
    private javax.swing.JLabel jlFkProductionOrderId_n;
    private javax.swing.JLabel jlFkTurnCloseId;
    private javax.swing.JLabel jlFkTurnDeliveryId;
    private javax.swing.JLabel jlFkTurnEndId;
    private javax.swing.JLabel jlFkTurnLotAssignedId;
    private javax.swing.JLabel jlFkTurnStartId;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlNumberReference_n;
    private javax.swing.JLabel jlQuantity;
    private javax.swing.JLabel jlQuantityOriginal;
    private javax.swing.JLabel jlQuantityRework;
    private javax.swing.JLabel jlReference;
    private javax.swing.JPanel jpCharges;
    private javax.swing.JPanel jpNotes;
    private javax.swing.JPanel jpPeriods;
    private javax.swing.JTextField jtfCharges;
    private javax.swing.JFormattedTextField jtfDate;
    private javax.swing.JFormattedTextField jtfDateClose_n;
    private javax.swing.JFormattedTextField jtfDateDelivery;
    private javax.swing.JFormattedTextField jtfDateEnd_n;
    private javax.swing.JFormattedTextField jtfDateLotAssigned_n;
    private javax.swing.JFormattedTextField jtfDateLot_n;
    private javax.swing.JFormattedTextField jtfDateStart_n;
    private javax.swing.JTextField jtfDbmsExplotionMaterialsReference;
    private javax.swing.JFormattedTextField jtfDbmsLotDateExpired;
    private javax.swing.JTextField jtfDbmsStatus;
    private javax.swing.JTextField jtfDbmsUserClose;
    private javax.swing.JTextField jtfDbmsUserEnd;
    private javax.swing.JTextField jtfDbmsUserLotAssigned;
    private javax.swing.JTextField jtfDbmsUserStart;
    private javax.swing.JTextField jtfDummyProductionOrder_n;
    private javax.swing.JTextField jtfFkLotId_n;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfNumberReference_n;
    private javax.swing.JTextField jtfQuantity;
    private javax.swing.JTextField jtfQuantityOriginal;
    private javax.swing.JTextField jtfQuantityOriginalUnit;
    private javax.swing.JTextField jtfQuantityRework;
    private javax.swing.JTextField jtfQuantityReworkUnit;
    private javax.swing.JTextField jtfQuantityUnit;
    private javax.swing.JTextField jtfReference;
    private javax.swing.JFormattedTextField jtfTsClose_n;
    private javax.swing.JFormattedTextField jtfTsEnd_n;
    private javax.swing.JFormattedTextField jtfTsLotAssigned_n;
    private javax.swing.JFormattedTextField jtfTsStart_n;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        moProductionOrder = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jtfQuantityUnit.setText("");
        jtfQuantityOriginalUnit.setText("");
        jtfQuantityReworkUnit.setText("");

        moProductionOrderPeriodsPane.createTable(null);
        moProductionOrderPeriodsPane.clearTableRows();
        moProductionOrderChargesPane.createTable(null);
        moProductionOrderChargesPane.clearTableRows();
        moProductionOrderNotesPane.createTable(null);
        moProductionOrderNotesPane.clearTableRows();
        jTabbedPane1.setSelectedIndex(0);

        moFieldDate.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateDelivery.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateLot_n.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateLotAssigned_n.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateStart_n.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateEnd_n.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateClose_n.setFieldValue(miClient.getSessionXXX().getWorkingDate());

        moFieldNumber.setFieldValue("0");
        moFieldDbmsProductionOrderStatus.setFieldValue("NUEVA");
        moFieldDbmsUserLotAssigned.setFieldValue("(n/a)");
        moFieldDbmsUserStart.setFieldValue("(n/a)");
        moFieldDbmsUserEnd.setFieldValue("(n/a)");
        moFieldDbmsUserClose.setFieldValue("(n/a)");
        jcbFkTurnLotAssignedId.setSelectedIndex(1);
        jcbFkTurnStartId.setSelectedIndex(1);
        jcbFkTurnEndId.setSelectedIndex(1);
        jcbFkTurnCloseId.setSelectedIndex(1);

        jcbFkCobId.setEnabled(true);
        jcbFkCobId.setFocusable(true);
        jbFkCobId.setEnabled(true);
        jcbFkEntityId.setEnabled(true);
        jcbFkEntityId.setFocusable(true);
        jcbFkEntityId.setEnabled(true);
        jcbFkOrdTypeId.setEnabled(true);
        jcbFkOrdTypeId.setFocusable(true);
        jtfQuantityOriginal.setEnabled(false);
        jtfQuantityOriginal.setFocusable(false);
        jtfQuantityRework.setEditable(false);
        jtfQuantityRework.setFocusable(false);
        jbPeriodNew.setEnabled(false);
        jbPeriodNew.setFocusable(false);
        jbPeriodEdit.setEnabled(false);
        jbPeriodEdit.setFocusable(false);
        jckIsDeleted.setEnabled(false);
        jbOk.setEnabled(true);

        enabledFields(true);

        jcbFkProductionOrderId_n.setVisible(true);
        jbFkProductionOrderId_n.setEnabled(true);
        jtfDummyProductionOrder_n.setVisible(false);
        moComboBoxItemBom.reset();
        moItem = null;

        moFieldCharges.setFieldValue(1); // By default 1
        jcbFkOrdPriorityId.setSelectedIndex(1); // By default 1
        moFieldFkCobId.setKey(new int[] { miClient.getSessionXXX().getCurrentCompanyBranchId() });
    }

    @Override
    public void formRefreshCatalogues() {
        Vector<SFormComponentItem> items = null;

        if (isUniversalCompany()) {
            items = miClient.getSessionXXX().getAllCompanyBranchesAsComponentItems(true);
        }
        else {
            items = miClient.getSessionXXX().getUserCompanyBranchesAsComponentItems(true);
        }

        jcbFkCobId.removeAllItems();
        for (SFormComponentItem item : items) {
            jcbFkCobId.addItem(item);
        }

        moComboBoxItemBom.clear();
        moComboBoxItemBom.addComboBox(SDataConstants.ITMX_ITEM_BOM_ITEM, jcbFkItemId, jbFkItemId, null);
        moComboBoxItemBom.addComboBox(SDataConstants.MFG_BOM, jcbFkBomId, jbFkBomId);
        
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerId_n, SDataConstants.BPSX_BP_CUS);
        SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerOperatorId_n, SDataConstants.BPSX_BP_ATT_EMP_MFG);

        SFormUtilities.populateComboBox(miClient, jcbFkOrdTypeId, SDataConstants.MFGU_TP_ORD);
        SFormUtilities.populateComboBox(miClient, jcbFkOrdPriorityId, SDataConstants.MFGS_PTY_ORD);
        SFormUtilities.populateComboBox(miClient, jcbFkProductionOrderId_n, SDataConstants.MFG_ORD, new Object[] { "" + SDataConstantsSys.MFGS_ST_ORD_NEW, SDataConstants.MFGX_ORD_MAIN_FA, false });

        SFormUtilities.populateComboBox(miClient, jcbFkTurnDeliveryId, SDataConstants.MFGU_TURN);
        SFormUtilities.populateComboBox(miClient, jcbFkTurnLotAssignedId, SDataConstants.MFGU_TURN);
        SFormUtilities.populateComboBox(miClient, jcbFkTurnStartId, SDataConstants.MFGU_TURN);
        SFormUtilities.populateComboBox(miClient, jcbFkTurnEndId, SDataConstants.MFGU_TURN);
        SFormUtilities.populateComboBox(miClient, jcbFkTurnCloseId, SDataConstants.MFGU_TURN);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        String msg = "";
        SDataProductionOrder productionOrder = null;
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            // Validate if comboBox is disabled:

            if (jlFkCobId.getText().compareTo(mvFields.get(i).getFieldName()) == 0 ||
                jlFkEntityId.getText().compareTo(mvFields.get(i).getFieldName()) == 0 ||
                jlFkOrdTypeId.getText().compareTo(mvFields.get(i).getFieldName()) == 0) {
                if (!((erp.lib.form.SFormField) mvFields.get(i)).validateFieldForcing()) {
                    validation.setIsError(true);
                    validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                    break;
                }
            }
            else {
                if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                    validation.setIsError(true);
                    validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                    break;
                }
            }
        }

        if (!validation.getIsError()) {
            if (((Boolean)((SFormComponentItem) jcbFkOrdTypeId.getModel().getSelectedItem()).getComplement()).booleanValue() &&
                    (productionOrder == null || productionOrder.getFkOrdStatusId() == SDataConstantsSys.MFGS_ST_ORD_NEW) &&
                    jcbFkProductionOrderId_n.getSelectedIndex() <= 0 && !jckIsForecast.isSelected()) {
                validation.setMessage("La orden de producción requiere orden de producción padre.");
                validation.setComponent(moFieldFkProductionOrderId_n.getComponent());
            }
            else {
                // Validate that item is not restricted or inactive:
                
                if (moItem.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_INA) {
                    validation.setMessage(SItmConsts.MSG_ERR_ST_ITEM_INA + "\n" + SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkItemId.getText() + "'."); // validate that item is not inactive
                    validation.setComponent(jcbFkItemId);
                }
                else if (moItem.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_RES) {
                    validation.setMessage(SItmConsts.MSG_ERR_ST_ITEM_RES + "\n" + SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkItemId.getText() + "'."); // falidate that item is not restricted
                    validation.setComponent(jcbFkItemId);
                }
            }
        }

        /* (2016-02-29, sflores: Why is this code block commented?, should it be removed?
        if (!validation.getIsError()) {
            if (((Boolean)((SFormComponentItem) jcbFkOrdTypeId.getModel().getSelectedItem()).getComplement()).booleanValue() &&
                    (productionOrder == null || productionOrder.getFkOrdStatusId() == SDataConstantsSys.MFGS_ST_ORD_NEW) &&
                    jcbFkProductionOrderId_n.getSelectedIndex() > 0 && !jckIsForecast.isSelected()) {

                productionOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, moFieldFkProductionOrderId_n.getKey(), SLibConstants.EXEC_MODE_VERBOSE);
                try {
                    if (SDataUtilities.validateIngredientInFormula(miClient, productionOrder.getFkItemId_r(), productionOrder.getFkUnitId_r(), moFieldFkItemId_r.getKeyAsIntArray()[0],
                            moUnit.getPkUnitId(), jcbFkBomId.getSelectedItem().toString())<=0) {
                        validation.setMessage("La orden de producción padre no es un insumo de la orden de producción hijo, verifique las unidades");
                        validation.setComponent(moFieldFkProductionOrderId_n.getComponent());
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        */

        if (!validation.getIsError()) {
            if (!SDataUtilities.isPeriodOpen(miClient, moFieldDate.getDate())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                validation.setComponent(jtfDate);
            }
            else if (productionOrder != null) {
                try {
                    msg = SDataUtilities.checkProductionOrderExternalRelations(miClient, productionOrder.getPkYearId(), productionOrder.getPkOrdId());
                    if (!msg.isEmpty()) {
                        validation.setMessage("No se puede modificar la orden de producción por tener movimientos de '"+ msg + "'.");
                    }
                }
                catch (Exception e) {
                    validation.setMessage(e.toString());
                }
            }
            else {
                try {
                    SMfgUtils.validateBomItems(miClient.getSession(), moFieldFkBomId.getKeyAsIntArray()[0], moFieldFkItemId_r.getKeyAsIntArray()[0]); // check for restricted or inactive components
                }
                catch (Exception e) {
                    validation.setMessage(e.toString());
                }
            }
        }

        if (!validation.getIsError() && productionOrder != null) {
            if (productionOrder.getFkLotItemId_nr() > 0 && productionOrder.getFkLotUnitId_nr() > 0 && productionOrder.getFkLotId_n() > 0) {
                if (moItem.getPkItemId() != productionOrder.getFkLotItemId_nr() && moItem.getFkUnitId() != productionOrder.getFkLotUnitId_nr()) {
                    if(miClient.showMsgBoxConfirm("El lote asignado a la orden de producción no corresponde al producto seleccionado. \n "
                            + "¿Desea eliminar el lote '" + productionOrder.getDbmsLotItem() + " " + productionOrder.getDbmsLotUnit()
                            + ", lote: " + productionOrder.getDbmsLot() + "'?") == JOptionPane.YES_OPTION) {
                        productionOrder.setFkLotItemId_nr(0);
                        productionOrder.setFkLotUnitId_nr(0);
                        productionOrder.setFkLotId_n(0);
                    }
                    else {
                        validation.setMessage("El lote '" + productionOrder.getDbmsLotItem() + " " + productionOrder.getDbmsLotUnit()
                            + ", lote: " + productionOrder.getDbmsLot() + "' \n asignado a la orden de producción no corresponde al producto seleccionado.");
                        validation.setComponent(moFieldFkItemId_r.getComponent());
                    }
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
        int i = 0;
        String sMove = "";

        moProductionOrder = (SDataProductionOrder) registry;
        moFieldNumber.setFieldValue(moProductionOrder.getDbmsNumber());
        moFieldFkCobId.setFieldValue(new int[] { moProductionOrder.getFkCobId() });
        moFieldFkEntityId.setFieldValue(new int[] { moProductionOrder.getFkCobId(), moProductionOrder.getFkEntityId() });
        moFieldFkOrdTypeId.setFieldValue(new int[] { moProductionOrder.getFkOrdTypeId() });
        moFieldFkItemId_r.setFieldValue(new int[] { moProductionOrder.getFkItemId_r() });
        moFieldFkBomId.setFieldValue(new int[] { moProductionOrder.getFkBomId() });
        moFieldReference.setFieldValue(moProductionOrder.getReference());
        moFieldQuantityOriginal.setFieldValue(moProductionOrder.getQuantityOriginal());
        jtfQuantityOriginalUnit.setText(moProductionOrder.getDbmsBomUnitSymbol());
        moFieldQuantityRework.setFieldValue(moProductionOrder.getQuantityRework());
        jtfQuantityReworkUnit.setText(moProductionOrder.getDbmsBomUnitSymbol());
        moFieldQuantity.setFieldValue(moProductionOrder.getQuantity());
        jtfQuantityUnit.setText(moProductionOrder.getDbmsBomUnitSymbol());
        moFieldCharges.setFieldValue(moProductionOrder.getCharges());
        moFieldNumberReference_n.setFieldValue(moProductionOrder.getNumberReference_n());
        moFieldDate.setFieldValue(moProductionOrder.getDate());
        moFieldFkOrdPriorityId.setFieldValue(new int[] { moProductionOrder.getFkOrdPriorityId() });
        moFieldFkBizPartnerId_n.setFieldValue(new int[] { moProductionOrder.getFkBizPartnerId_n() });
        moFieldFkBizPartnerOperatorId_n.setFieldValue(new int[] { moProductionOrder.getFkBizPartnerOperatorId_n() });
        moFieldFkProductionOrderId_n.setFieldValue(new int[] { moProductionOrder.getFkOrdYearId_n(), moProductionOrder.getFkOrdId_n() });
        moFieldFkLotId_n.setFieldValue(moProductionOrder.getDbmsLot());
        moFieldDbmsExplotionMaterialsReference.setFieldValue(moProductionOrder.getDbmsExplotionMaterialsReference());
        moFieldDbmsLotDateExpired.setFieldValue(moProductionOrder.getDbmsLotDateExpired());
        moFieldDbmsProductionOrderStatus.setFieldValue(moProductionOrder.getDbmsProductionOrderStatus());
        moFieldDateDelivery.setFieldValue(moProductionOrder.getDateDelivery());
        moFieldDateLot_n.setFieldValue(moProductionOrder.getDateLot_n());
        moFieldDateLotAssigned_n.setFieldValue(moProductionOrder.getDateLotAssigned_n());
        moFieldTsLotAssigned_n.setFieldValue(moProductionOrder.getTsLotAssigned_n());
        moFieldDbmsUserLotAssigned.setFieldValue(moProductionOrder.getDbmsUserLotAssigned());
        moFieldDateStart_n.setFieldValue(moProductionOrder.getDateStart_n());
        moFieldTsStart_n.setFieldValue(moProductionOrder.getTsStart_n());
        moFieldDbmsUserStart.setFieldValue(moProductionOrder.getDbmsUserStart());
        moFieldDateEnd_n.setFieldValue(moProductionOrder.getDateEnd_n());
        moFieldTsEnd_n.setFieldValue(moProductionOrder.getTsEnd_n());
        moFieldDbmsUserEnd.setFieldValue(moProductionOrder.getDbmsUserEnd());
        moFieldDateClose_n.setFieldValue(moProductionOrder.getDateClose_n());
        moFieldTsClose_n.setFieldValue(moProductionOrder.getTsClose_n());
        moFieldDbmsUserClose.setFieldValue(moProductionOrder.getDbmsUserClose());
        moFieldFkTurnDeliveryId.setFieldValue(new int[] { moProductionOrder.getFkTurnDeliveryId() });
        moFieldFkTurnLotAssigedId.setFieldValue(new int[] { moProductionOrder.getFkTurnLotAssignedId() });
        moFieldFkTurnStartId.setFieldValue(new int[] { moProductionOrder.getFkTurnStartId() });
        moFieldFkTurnEndId.setFieldValue(new int[] { moProductionOrder.getFkTurnEndId() });
        moFieldFkTurnCloseId.setFieldValue(new int[] { moProductionOrder.getFkTurnCloseId() });
        moFieldIsForecast.setFieldValue(moProductionOrder.getIsForecast());
        moFieldIsDeleted.setFieldValue(moProductionOrder.getIsDeleted());

        // Validate if production order status is new:

        if (moProductionOrder.getFkOrdStatusId() > SDataConstantsSys.MFGS_ST_ORD_NEW) {
            jcbFkProductionOrderId_n.setVisible(false);
            jbFkProductionOrderId_n.setVisible(false);
            jtfDummyProductionOrder_n.setText(moProductionOrder.getDbmsNumberFather() + " - " + moProductionOrder.getDbmsProductionOrder_n());
            jtfDummyProductionOrder_n.setVisible(true);
        }
        else {
            jcbFkProductionOrderId_n.setVisible(true);
            jbFkProductionOrderId_n.setVisible(true);
            jtfDummyProductionOrder_n.setVisible(false);
        }

        for (i = 0; i < moProductionOrder.getDbmsProductionOrderPeriods().size(); i++) {
            moProductionOrderPeriodRow = new SDataProductionOrderPeriodRow(moProductionOrder.getDbmsProductionOrderPeriods().get(i));
            moProductionOrderPeriodsPane.addTableRow(moProductionOrderPeriodRow);
        }

        for (i = 0; i < moProductionOrder.getDbmsProductionOrderCharges().size(); i++) {
            moProductionOrderChargesRow = new SDataProductionOrderChargeRow(moProductionOrder.getDbmsProductionOrderCharges().get(i));
            moProductionOrderChargesPane.addTableRow(moProductionOrderChargesRow);
        }

        for (i = 0; i < moProductionOrder.getDbmsProductionOrderNotes().size(); i++) {
            moProductionOrderNotesRow = new SDataProductionOrderNotesRow(moProductionOrder.getDbmsProductionOrderNotes().get(i));
            moProductionOrderNotesPane.addTableRow(moProductionOrderNotesRow);
        }

        if (moProductionOrderChargesPane.getTableGuiRowCount() > 0) {
            moProductionOrderChargesPane.setTableRowSelection(0);
        }

        // Validate if is enabled 'save' button:

        if (moProductionOrder.getFkOrdStatusId() < SDataConstantsSys.MFGS_ST_ORD_PROC) {
            jbOk.setEnabled(true);
        }
        else {
            jbOk.setEnabled(false);
        }

        jcbFkCobId.setEnabled(false);
        jcbFkCobId.setFocusable(false);
        jbFkCobId.setEnabled(false);
        jcbFkEntityId.setEnabled(false);
        jcbFkEntityId.setFocusable(false);
        jbFkEntityId.setEnabled(false);
        jcbFkOrdTypeId.setEnabled(false);
        jcbFkOrdTypeId.setFocusable(false);

        if (moProductionOrder.getFkOrdStatusId() != SDataConstantsSys.MFGS_ST_ORD_NEW) {
            enabledFields(false);
        }
        else {
            try {
                sMove = SDataUtilities.checkProductionOrderExternalRelations(miClient, moProductionOrder.getPkYearId(), moProductionOrder.getPkOrdId());
                if (sMove.length()>0) {
                    enabledFields(false);
                    jbOk.setEnabled(false);
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }

        itemStateChangedFkOrdTypeId();
        jckIsDeleted.setEnabled(true);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int i=0;

        if (moProductionOrder == null) {
            int[] period = SLibTimeUtilities.digestYearMonth(moFieldDate.getDate());

            moProductionOrder = new SDataProductionOrder();
            moProductionOrder.setPkYearId(period[0]);
            moProductionOrder.setFkOrdStatusId(SDataConstantsSys.MFGS_ST_ORD_NEW);
            moProductionOrder.setFkOrdYearId_n(moFieldFkProductionOrderId_n.getKeyAsIntArray()[0]);
            moProductionOrder.setFkOrdId_n(moFieldFkProductionOrderId_n.getKeyAsIntArray()[1]);
            moProductionOrder.setFkUserLotAssignedId(1);
            moProductionOrder.setFkUserStartId(1);
            moProductionOrder.setFkUserEndId(1);
            moProductionOrder.setFkUserCloseId(1);
            moProductionOrder.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moProductionOrder.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

            if (moProductionOrder.getFkOrdStatusId() == SDataConstantsSys.MFGS_ST_ORD_NEW) {
                moProductionOrder.setFkOrdYearId_n(moFieldFkProductionOrderId_n.getKeyAsIntArray()[0]);
                moProductionOrder.setFkOrdId_n(moFieldFkProductionOrderId_n.getKeyAsIntArray()[1]);
            }
        }

        moProductionOrder.setFkCobId(moFieldFkCobId.getKeyAsIntArray()[0]);
        moProductionOrder.setFkEntityId(moFieldFkEntityId.getKeyAsIntArray()[1]);
        moProductionOrder.setFkOrdTypeId(moFieldFkOrdTypeId.getKeyAsIntArray()[0]);
        moProductionOrder.setFkItemId_r(moFieldFkItemId_r.getKeyAsIntArray()[0]);
        moProductionOrder.setFkUnitId_r(moUnit.getPkUnitId());
        moProductionOrder.setFkBomId(moFieldFkBomId.getKeyAsIntArray()[0]);
        moProductionOrder.setReference(moFieldReference.getString());
        moProductionOrder.setQuantityOriginal(moFieldQuantityOriginal.getDouble());
        moProductionOrder.setQuantityRework(moFieldQuantityRework.getDouble());
        moProductionOrder.setQuantity(moFieldQuantity.getDouble());
        moProductionOrder.setCharges(moFieldCharges.getInteger());
        moProductionOrder.setNumberReference_n(moFieldNumberReference_n.getString());
        moProductionOrder.setDate(moFieldDate.getDate());
        moProductionOrder.setFkOrdPriorityId(moFieldFkOrdPriorityId.getKeyAsIntArray()[0]);
        moProductionOrder.setDateDelivery(moFieldDateDelivery.getDate());
        moProductionOrder.setDateLot_n(moFieldDateLot_n.getDate());
        moProductionOrder.setIsForecast(moFieldIsForecast.getBoolean());
        moProductionOrder.setIsDeleted(moFieldIsDeleted.getBoolean());
        moProductionOrder.setFkBizPartnerId_n(moFieldFkBizPartnerId_n.getKeyAsIntArray()[0]);
        moProductionOrder.setFkBizPartnerOperatorId_n(moFieldFkBizPartnerOperatorId_n.getKeyAsIntArray()[0]);
        moProductionOrder.setFkTurnDeliveryId(moFieldFkTurnDeliveryId.getKeyAsIntArray()[0]);
        moProductionOrder.setFkTurnLotAssignedId(moFieldFkTurnLotAssigedId.getKeyAsIntArray()[0]);
        moProductionOrder.setFkTurnStartId(moFieldFkTurnStartId.getKeyAsIntArray()[0]);
        moProductionOrder.setFkTurnEndId(moFieldFkTurnEndId.getKeyAsIntArray()[0]);
        moProductionOrder.setFkTurnCloseId(moFieldFkTurnCloseId.getKeyAsIntArray()[0]);
        moProductionOrder.setDateLotAssigned_n(moFieldDateLotAssigned_n.getDate());
        moProductionOrder.setDateStart_n(moFieldDateStart_n.getDate());
        moProductionOrder.setDateEnd_n(moFieldDateEnd_n.getDate());
        moProductionOrder.setDateClose_n(moFieldDateClose_n.getDate());

        moProductionOrder.getDbmsProductionOrderPeriods().removeAllElements();
        for (i = 0; i < moProductionOrderPeriodsPane.getTableGuiRowCount(); i++) {
            moProductionOrder.getDbmsProductionOrderPeriods().add((SDataProductionOrderPeriod) moProductionOrderPeriodsPane.getTableRow(i).getData());
        }

        moProductionOrder.getDbmsProductionOrderCharges().removeAllElements();
        for (i = 0; i < moProductionOrderChargesPane.getTableGuiRowCount(); i++) {
            moProductionOrder.getDbmsProductionOrderCharges().add((SDataProductionOrderCharge) moProductionOrderChargesPane.getTableRow(i).getData());
        }

        moProductionOrder.getDbmsProductionOrderNotes().removeAllElements();
        for (i = 0; i < moProductionOrderNotesPane.getTableGuiRowCount(); i++) {
            moProductionOrder.getDbmsProductionOrderNotes().add((SDataProductionOrderNotes) moProductionOrderNotesPane.getTableRow(i).getData());
        }

        return moProductionOrder;
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
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk(true);
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbCopyReference) {
                actionCopyReference();
            }
            else if (button == jbDate) {
                actionDate();
            }
            else if (button == jbDateDelivery) {
                actionSetDateDelivery();
            }
            else if (button == jbPeriodNew) {
                actionPeriodNew();
            }
            else if (button == jbPeriodEdit) {
                actionPeriodEdit();
            }
            else if (button == jbChargesEdit) {
                actionChargesEdit();
            }
            else if (button == jbNotesNew) {
                actionNotesNew();
            }
            else if (button == jbNotesEdit) {
                actionNotesEdit();
            }
            else if (button == jbFkItemId) {
                actionFkItemId();
            }
            else if (button == jbFkCobId) {
                actionFkCobId();
            }
            else if (button == jbFkEntityId) {
                actionFkEntityId();
            }
            else if (button == jbFkBomId) {
                actionFkBomId();
            }
            else if (button == jbFkBizPartnerId_n) {
                actionFkBizPartnerId_n();
            }
            else if (button == jbFkBizPartnerOperatorId_n) {
                actionFkBizPartnerOperatorId_n();
            }
            else if (button == jbFkProductionOrderId_n) {
                actionFkProductionOrderId_n();
            }
        }
    }
}
