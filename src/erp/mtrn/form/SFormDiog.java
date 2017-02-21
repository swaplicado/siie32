/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormDiog.java
 *
 * Created on 23/10/2009, 08:48:14 AM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.form.SFormOptionPickerItems;
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
import erp.lib.table.STableRow;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.form.SDialogPickerCompanyBranchEntity;
import erp.mcfg.data.SDataCompanyBranchEntity;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mmfg.data.SDataProductionOrder;
import erp.mod.SModSysConsts;
import erp.mod.itm.db.SItmConsts;
import erp.mtrn.data.STrnStockSegregationUtils;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.SDataDiogEntryRow;
import erp.mtrn.data.SDataDiogNotes;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataStockLot;
import erp.mtrn.data.STrnDpsStockReturnRow;
import erp.mtrn.data.STrnDpsStockSupplyRow;
import erp.mtrn.data.STrnItemFound;
import erp.mtrn.data.STrnProdOrderStockAssignRow;
import erp.mtrn.data.STrnProdOrderStockFinishRow;
import erp.mtrn.data.STrnStockMove;
import erp.mtrn.data.STrnStockValidator;
import erp.mtrn.data.STrnUtilities;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Sergio Flores, Uriel Castañeda
 */
public class SFormDiog extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener {

    public static final String TXT_DEC_INC = "Ver más decimales";
    public static final String TXT_DEC_DEC = "Ver menos decimales";
    private int mnColQty;

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataDiog moDiog;
    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldSeries;
    private erp.lib.form.SFormField moFieldProdOrderSource;
    private erp.lib.form.SFormField moFieldProdOrderDestiny;
    private erp.lib.form.SFormField moFieldNotes;
    private erp.lib.form.SFormField moFieldEntryQuantity;
    private erp.lib.form.SFormField moFieldEntryValueUnitary;
    private erp.lib.form.SFormField moFieldEntryValue;
    private erp.lib.form.SFormField moFieldDiogAdjustmentType;

    private java.util.Vector<erp.mtrn.data.SDataDiogEntry> mvDeletedDiogEntries;
    private erp.lib.table.STablePane moPaneDiogEntries;

    private int mnParamIogCategoryId;
    private int[] manParamIogClassKey;
    private int[] manParamIogTypeKey;
    private int[] manParamProdOrderSourceKey;
    private int[] manParamProdOrderDestinyKey;
    private boolean mbSwitchProdOrderDestinySelected;
    private erp.mtrn.data.SDataDps moParamDpsSource;

    private boolean mbCanShowForm;
    private boolean mbWarehouseDestinyNeeded;
    private int[] manLastWarehouseSourceKey;
    private int[] manLastWarehouseDestinyKey;
    private erp.mtrn.form.SDialogStockLots moDialogStockLots;
    private erp.mtrn.form.SDialogStockLots moDialogStockLotsProdOrder;
    private erp.mtrn.form.SDialogPickerStockLots moPickerStockLots;
    private erp.mbps.form.SDialogPickerCompanyBranchEntity moPickerWarehouseSource;
    private erp.mbps.form.SDialogPickerCompanyBranchEntity moPickerWarehouseDestiny;
    private erp.mtrn.form.SDialogPickerProdOrder moPickerProdOrderDestiny;
    private erp.mbps.data.SDataBizPartnerBranch moCompanyBranchSource;
    private erp.mbps.data.SDataBizPartnerBranch moCompanyBranchDestiny;
    private erp.mcfg.data.SDataCompanyBranchEntity moWarehouseSource;
    private erp.mcfg.data.SDataCompanyBranchEntity moWarehouseDestiny;
    private erp.mmfg.data.SDataProductionOrder moProdOrderSource;
    private erp.mmfg.data.SDataProductionOrder moProdOrderDestiny;
    private erp.mitm.data.SDataItem moEntryItem;
    private erp.mitm.data.SDataItem moItemProdOrderSource;
    private erp.mitm.data.SDataItem moItemProdOrderDestiny;
    private erp.mitm.data.SDataUnit moUnitProdOrderSource;
    private erp.mitm.data.SDataUnit moUnitProdOrderDestiny;
    private erp.mtrn.data.STrnStockMove moStockMoveEntry;
    private erp.mtrn.form.SDialogDpsStockSupply moDialogDpsStockSupply;
    private erp.mtrn.form.SDialogDpsStockReturn moDialogDpsStockReturn;
    private erp.mtrn.form.SDialogProdOrderStockAssign moDialogProdOrderStockAssignForAssing;
    private erp.mtrn.form.SDialogProdOrderStockAssign moDialogProdOrderStockAssignForReturn;
    private erp.mtrn.form.SDialogProdOrderStockFinish moDialogProdOrderStockFinishForFinish;
    private erp.mtrn.form.SDialogProdOrderStockFinish moDialogProdOrderStockFinishForReturn;

    /** Creates new form SFormDiog */
    public SFormDiog(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;
        mnFormType = SDataConstants.TRN_DIOG;

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

        jPanel2 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jpDocType = new javax.swing.JPanel();
        jlDiogType = new javax.swing.JLabel();
        jtfDiogType = new javax.swing.JTextField();
        jcbDiogAdjustmentType = new javax.swing.JComboBox<SFormComponentItem>();
        jpDocDate = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jckIsShipmentRequired = new javax.swing.JCheckBox();
        jckIsSystem = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jpDocNumber = new javax.swing.JPanel();
        jlSeries = new javax.swing.JLabel();
        jcbSeries = new javax.swing.JComboBox<SFormComponentItem>();
        jtfNumber = new javax.swing.JTextField();
        jlDummy01 = new javax.swing.JLabel();
        jbWarehouseExchange = new javax.swing.JButton();
        jbWarehouseClear = new javax.swing.JButton();
        jpWarehouseSource = new javax.swing.JPanel();
        jlWarehouseSource = new javax.swing.JLabel();
        jtfCompanyBranchSource = new javax.swing.JTextField();
        jtfCompanyBranchSourceCode = new javax.swing.JTextField();
        jtfWarehouseSource = new javax.swing.JTextField();
        jtfWarehouseSourceCode = new javax.swing.JTextField();
        jbWarehouseSource = new javax.swing.JButton();
        jpWarehouseDestiny = new javax.swing.JPanel();
        jlWarehouseDestiny = new javax.swing.JLabel();
        jtfCompanyBranchDestiny = new javax.swing.JTextField();
        jtfCompanyBranchDestinyCode = new javax.swing.JTextField();
        jtfWarehouseDestiny = new javax.swing.JTextField();
        jtfWarehouseDestinyCode = new javax.swing.JTextField();
        jbWarehouseDestiny = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jlProdOrderSource = new javax.swing.JLabel();
        jcbProdOrderSource = new javax.swing.JComboBox<SFormComponentItem>();
        jbProdOrderSource = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jlItemSource = new javax.swing.JLabel();
        jtfItemSourceCode = new javax.swing.JTextField();
        jtfItemSource = new javax.swing.JTextField();
        jtfQuantitySource = new javax.swing.JTextField();
        jtfUnitSymbolSource = new javax.swing.JTextField();
        jpProductionOrderDestiny = new javax.swing.JPanel();
        jlProdOrderDestiny = new javax.swing.JLabel();
        jcbProdOrderDestiny = new javax.swing.JComboBox<SFormComponentItem>();
        jbProdOrderDestiny = new javax.swing.JButton();
        jtbSwitchProdOrderDestiny = new javax.swing.JToggleButton();
        jpProductionOrderDestinyItem = new javax.swing.JPanel();
        jlItemDestiny = new javax.swing.JLabel();
        jtfItemDestinyCode = new javax.swing.JTextField();
        jtfItemDestiny = new javax.swing.JTextField();
        jtfQuantityDestiny = new javax.swing.JTextField();
        jtfUnitSymbolDestiny = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jlNotes = new javax.swing.JLabel();
        jtfNotes = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jtfEntryTextToFind = new javax.swing.JTextField();
        jbEntryFind = new javax.swing.JButton();
        jtbDecimals = new javax.swing.JToggleButton();
        jlEntryDummy01 = new javax.swing.JLabel();
        jlEntryQuantity = new javax.swing.JLabel();
        jlEntryDummy02 = new javax.swing.JLabel();
        jlEntryValueUnitary = new javax.swing.JLabel();
        jlEntryValue = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jtfEntryItemCode = new javax.swing.JTextField();
        jtfEntryItem = new javax.swing.JTextField();
        jtfEntryQuantity = new javax.swing.JTextField();
        jtfEntryUnitSymbol = new javax.swing.JTextField();
        jtfEntryValueUnitary = new javax.swing.JTextField();
        jtfEntryValue = new javax.swing.JTextField();
        jbEntryAdd = new javax.swing.JButton();
        jbEntryClear = new javax.swing.JButton();
        jbEntryPickLot = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jbEntryDelete = new javax.swing.JButton();
        jbEntryViewLots = new javax.swing.JButton();
        jtfDpsSourceNumber = new javax.swing.JTextField();
        jtfDpsSourceBizPartner = new javax.swing.JTextField();
        jbEntryImport = new javax.swing.JButton();
        jpDiogEntries = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlValue = new javax.swing.JLabel();
        jtfValue = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jpCommands1 = new javax.swing.JPanel();
        jpCommands2 = new javax.swing.JPanel();
        jbEdit = new javax.swing.JButton();
        jbEditHelp = new javax.swing.JButton();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Documento de entradas y salidas de mercancías");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel21.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del documento:"));
        jPanel8.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jpDocType.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDiogType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlDiogType.setText("Tipo documento:");
        jlDiogType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocType.add(jlDiogType);

        jtfDiogType.setEditable(false);
        jtfDiogType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfDiogType.setText("DOCUMENT TYPE");
        jtfDiogType.setFocusable(false);
        jtfDiogType.setPreferredSize(new java.awt.Dimension(155, 23));
        jpDocType.add(jtfDiogType);

        jcbDiogAdjustmentType.setToolTipText("Tipo de ajuste");
        jcbDiogAdjustmentType.setPreferredSize(new java.awt.Dimension(193, 23));
        jpDocType.add(jcbDiogAdjustmentType);

        jPanel8.add(jpDocType);

        jpDocDate.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha: *");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocDate.add(jlDate);

        jftDate.setText("dd/mm/yyyy");
        jftDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocDate.add(jftDate);

        jbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDate.setToolTipText("Seleccionar fecha");
        jbDate.setFocusable(false);
        jbDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpDocDate.add(jbDate);

        jckIsShipmentRequired.setText("Embarque");
        jckIsShipmentRequired.setPreferredSize(new java.awt.Dimension(85, 23));
        jpDocDate.add(jckIsShipmentRequired);

        jckIsSystem.setText("Sistema");
        jckIsSystem.setEnabled(false);
        jckIsSystem.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocDate.add(jckIsSystem);

        jckIsDeleted.setText("Eliminado");
        jckIsDeleted.setEnabled(false);
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocDate.add(jckIsDeleted);

        jPanel8.add(jpDocDate);

        jpDocNumber.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSeries.setText("Serie y folio:");
        jlSeries.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocNumber.add(jlSeries);

        jcbSeries.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocNumber.add(jcbSeries);

        jtfNumber.setEditable(false);
        jtfNumber.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNumber.setText("0");
        jtfNumber.setFocusable(false);
        jtfNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocNumber.add(jtfNumber);

        jlDummy01.setPreferredSize(new java.awt.Dimension(137, 23));
        jpDocNumber.add(jlDummy01);

        jbWarehouseExchange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_switch.gif"))); // NOI18N
        jbWarehouseExchange.setToolTipText("Intercambiar almacenes");
        jbWarehouseExchange.setFocusable(false);
        jbWarehouseExchange.setPreferredSize(new java.awt.Dimension(23, 23));
        jpDocNumber.add(jbWarehouseExchange);

        jbWarehouseClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbWarehouseClear.setToolTipText("Borrar almacenes");
        jbWarehouseClear.setFocusable(false);
        jbWarehouseClear.setPreferredSize(new java.awt.Dimension(23, 23));
        jpDocNumber.add(jbWarehouseClear);

        jPanel8.add(jpDocNumber);

        jpWarehouseSource.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWarehouseSource.setText("Almacén origen:");
        jlWarehouseSource.setPreferredSize(new java.awt.Dimension(100, 23));
        jpWarehouseSource.add(jlWarehouseSource);

        jtfCompanyBranchSource.setBackground(java.awt.Color.lightGray);
        jtfCompanyBranchSource.setEditable(false);
        jtfCompanyBranchSource.setText("BRANCH");
        jtfCompanyBranchSource.setFocusable(false);
        jtfCompanyBranchSource.setPreferredSize(new java.awt.Dimension(75, 23));
        jpWarehouseSource.add(jtfCompanyBranchSource);

        jtfCompanyBranchSourceCode.setBackground(java.awt.Color.lightGray);
        jtfCompanyBranchSourceCode.setEditable(false);
        jtfCompanyBranchSourceCode.setText("CODE");
        jtfCompanyBranchSourceCode.setFocusable(false);
        jtfCompanyBranchSourceCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jpWarehouseSource.add(jtfCompanyBranchSourceCode);

        jtfWarehouseSource.setBackground(java.awt.Color.lightGray);
        jtfWarehouseSource.setEditable(false);
        jtfWarehouseSource.setText("WAREHOUSE");
        jtfWarehouseSource.setFocusable(false);
        jtfWarehouseSource.setPreferredSize(new java.awt.Dimension(135, 23));
        jpWarehouseSource.add(jtfWarehouseSource);

        jtfWarehouseSourceCode.setBackground(java.awt.Color.lightGray);
        jtfWarehouseSourceCode.setEditable(false);
        jtfWarehouseSourceCode.setText("CODE");
        jtfWarehouseSourceCode.setFocusable(false);
        jtfWarehouseSourceCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jpWarehouseSource.add(jtfWarehouseSourceCode);

        jbWarehouseSource.setText("...");
        jbWarehouseSource.setToolTipText("Seleccionar almacén origen");
        jbWarehouseSource.setFocusable(false);
        jbWarehouseSource.setPreferredSize(new java.awt.Dimension(23, 23));
        jpWarehouseSource.add(jbWarehouseSource);

        jPanel8.add(jpWarehouseSource);

        jpWarehouseDestiny.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWarehouseDestiny.setText("Almacén destino:");
        jlWarehouseDestiny.setPreferredSize(new java.awt.Dimension(100, 23));
        jpWarehouseDestiny.add(jlWarehouseDestiny);

        jtfCompanyBranchDestiny.setBackground(java.awt.Color.lightGray);
        jtfCompanyBranchDestiny.setEditable(false);
        jtfCompanyBranchDestiny.setText("BRANCH");
        jtfCompanyBranchDestiny.setFocusable(false);
        jtfCompanyBranchDestiny.setPreferredSize(new java.awt.Dimension(75, 23));
        jpWarehouseDestiny.add(jtfCompanyBranchDestiny);

        jtfCompanyBranchDestinyCode.setBackground(java.awt.Color.lightGray);
        jtfCompanyBranchDestinyCode.setEditable(false);
        jtfCompanyBranchDestinyCode.setText("CODE");
        jtfCompanyBranchDestinyCode.setFocusable(false);
        jtfCompanyBranchDestinyCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jpWarehouseDestiny.add(jtfCompanyBranchDestinyCode);

        jtfWarehouseDestiny.setBackground(java.awt.Color.lightGray);
        jtfWarehouseDestiny.setEditable(false);
        jtfWarehouseDestiny.setText("WAREHOUSE");
        jtfWarehouseDestiny.setFocusable(false);
        jtfWarehouseDestiny.setPreferredSize(new java.awt.Dimension(135, 23));
        jpWarehouseDestiny.add(jtfWarehouseDestiny);

        jtfWarehouseDestinyCode.setBackground(java.awt.Color.lightGray);
        jtfWarehouseDestinyCode.setEditable(false);
        jtfWarehouseDestinyCode.setText("CODE");
        jtfWarehouseDestinyCode.setFocusable(false);
        jtfWarehouseDestinyCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jpWarehouseDestiny.add(jtfWarehouseDestinyCode);

        jbWarehouseDestiny.setText("...");
        jbWarehouseDestiny.setToolTipText("Seleccionar almacén destino");
        jbWarehouseDestiny.setFocusable(false);
        jbWarehouseDestiny.setPreferredSize(new java.awt.Dimension(23, 23));
        jpWarehouseDestiny.add(jbWarehouseDestiny);

        jPanel8.add(jpWarehouseDestiny);

        jPanel21.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos adicionales:"));
        jPanel22.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProdOrderSource.setText("Ord. prod. origen:");
        jlProdOrderSource.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlProdOrderSource);

        jcbProdOrderSource.setMaximumRowCount(16);
        jcbProdOrderSource.setPreferredSize(new java.awt.Dimension(344, 23));
        jcbProdOrderSource.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbProdOrderSourceItemStateChanged(evt);
            }
        });
        jPanel24.add(jcbProdOrderSource);

        jbProdOrderSource.setText("...");
        jbProdOrderSource.setToolTipText("Seleccionar orden de producción origen");
        jbProdOrderSource.setFocusable(false);
        jbProdOrderSource.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel24.add(jbProdOrderSource);

        jPanel22.add(jPanel24);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemSource.setText("Producto origen:");
        jlItemSource.setFocusable(false);
        jlItemSource.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlItemSource);

        jtfItemSourceCode.setEditable(false);
        jtfItemSourceCode.setText("CODE");
        jtfItemSourceCode.setFocusable(false);
        jtfItemSourceCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel25.add(jtfItemSourceCode);

        jtfItemSource.setEditable(false);
        jtfItemSource.setText("ITEM");
        jtfItemSource.setFocusable(false);
        jtfItemSource.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel25.add(jtfItemSource);

        jtfQuantitySource.setEditable(false);
        jtfQuantitySource.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantitySource.setText("0.00");
        jtfQuantitySource.setFocusable(false);
        jtfQuantitySource.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel25.add(jtfQuantitySource);

        jtfUnitSymbolSource.setEditable(false);
        jtfUnitSymbolSource.setText("UNIT");
        jtfUnitSymbolSource.setFocusable(false);
        jtfUnitSymbolSource.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel25.add(jtfUnitSymbolSource);

        jPanel22.add(jPanel25);

        jpProductionOrderDestiny.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProdOrderDestiny.setText("Ord. prod. destino:");
        jlProdOrderDestiny.setPreferredSize(new java.awt.Dimension(100, 23));
        jpProductionOrderDestiny.add(jlProdOrderDestiny);

        jcbProdOrderDestiny.setMaximumRowCount(16);
        jcbProdOrderDestiny.setPreferredSize(new java.awt.Dimension(344, 23));
        jcbProdOrderDestiny.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbProdOrderDestinyItemStateChanged(evt);
            }
        });
        jpProductionOrderDestiny.add(jcbProdOrderDestiny);

        jbProdOrderDestiny.setText("...");
        jbProdOrderDestiny.setToolTipText("Seleccionar orden de producción destino");
        jbProdOrderDestiny.setFocusable(false);
        jbProdOrderDestiny.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProductionOrderDestiny.add(jbProdOrderDestiny);

        jtbSwitchProdOrderDestiny.setText("+");
        jtbSwitchProdOrderDestiny.setToolTipText("Mostrar otras órdenes de producción");
        jtbSwitchProdOrderDestiny.setEnabled(false);
        jtbSwitchProdOrderDestiny.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbSwitchProdOrderDestiny.setPreferredSize(new java.awt.Dimension(23, 23));
        jpProductionOrderDestiny.add(jtbSwitchProdOrderDestiny);

        jPanel22.add(jpProductionOrderDestiny);

        jpProductionOrderDestinyItem.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemDestiny.setText("Producto destino:");
        jlItemDestiny.setFocusable(false);
        jlItemDestiny.setPreferredSize(new java.awt.Dimension(100, 23));
        jpProductionOrderDestinyItem.add(jlItemDestiny);

        jtfItemDestinyCode.setEditable(false);
        jtfItemDestinyCode.setText("CODE");
        jtfItemDestinyCode.setFocusable(false);
        jtfItemDestinyCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProductionOrderDestinyItem.add(jtfItemDestinyCode);

        jtfItemDestiny.setEditable(false);
        jtfItemDestiny.setText("ITEM");
        jtfItemDestiny.setFocusable(false);
        jtfItemDestiny.setPreferredSize(new java.awt.Dimension(200, 23));
        jpProductionOrderDestinyItem.add(jtfItemDestiny);

        jtfQuantityDestiny.setEditable(false);
        jtfQuantityDestiny.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantityDestiny.setText("0.00");
        jtfQuantityDestiny.setFocusable(false);
        jtfQuantityDestiny.setPreferredSize(new java.awt.Dimension(75, 23));
        jpProductionOrderDestinyItem.add(jtfQuantityDestiny);

        jtfUnitSymbolDestiny.setEditable(false);
        jtfUnitSymbolDestiny.setText("UNIT");
        jtfUnitSymbolDestiny.setFocusable(false);
        jtfUnitSymbolDestiny.setPreferredSize(new java.awt.Dimension(35, 23));
        jpProductionOrderDestinyItem.add(jtfUnitSymbolDestiny);

        jPanel22.add(jpProductionOrderDestinyItem);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNotes.setText("Comentarios:");
        jlNotes.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jlNotes);

        jtfNotes.setText("TEXT");
        jtfNotes.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel27.add(jtfNotes);

        jPanel22.add(jPanel27);

        jPanel21.add(jPanel22, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel21, java.awt.BorderLayout.NORTH);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Partidas del documento:"));
        jPanel4.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel5.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfEntryTextToFind.setText("TEXT");
        jtfEntryTextToFind.setPreferredSize(new java.awt.Dimension(150, 23));
        jtfEntryTextToFind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfEntryTextToFindKeyPressed(evt);
            }
        });
        jPanel7.add(jtfEntryTextToFind);

        jbEntryFind.setText("jButton3");
        jbEntryFind.setToolTipText("Buscar ítem");
        jbEntryFind.setFocusable(false);
        jbEntryFind.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbEntryFind);

        jtbDecimals.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_dec_inc.gif"))); // NOI18N
        jtbDecimals.setToolTipText("Usar más decimales");
        jtbDecimals.setAlignmentY(0.0F);
        jtbDecimals.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbDecimals.setPreferredSize(new java.awt.Dimension(23, 23));
        jtbDecimals.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_dec_dec.gif"))); // NOI18N
        jPanel7.add(jtbDecimals);

        jlEntryDummy01.setPreferredSize(new java.awt.Dimension(222, 23));
        jPanel7.add(jlEntryDummy01);

        jlEntryQuantity.setText("Cantidad:");
        jlEntryQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlEntryQuantity);

        jlEntryDummy02.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel7.add(jlEntryDummy02);

        jlEntryValueUnitary.setText("Precio unitario:");
        jlEntryValueUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlEntryValueUnitary);

        jlEntryValue.setText("Importe:");
        jlEntryValue.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlEntryValue);

        jPanel5.add(jPanel7);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfEntryItemCode.setEditable(false);
        jtfEntryItemCode.setText("CODE");
        jtfEntryItemCode.setFocusable(false);
        jtfEntryItemCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jtfEntryItemCode);

        jtfEntryItem.setEditable(false);
        jtfEntryItem.setText("ITEM");
        jtfEntryItem.setFocusable(false);
        jtfEntryItem.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel10.add(jtfEntryItem);

        jtfEntryQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfEntryQuantity.setText("0.00");
        jtfEntryQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jtfEntryQuantity);

        jtfEntryUnitSymbol.setEditable(false);
        jtfEntryUnitSymbol.setText("UNIT");
        jtfEntryUnitSymbol.setFocusable(false);
        jtfEntryUnitSymbol.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel10.add(jtfEntryUnitSymbol);

        jtfEntryValueUnitary.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfEntryValueUnitary.setText("0.00");
        jtfEntryValueUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jtfEntryValueUnitary);

        jtfEntryValue.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfEntryValue.setText("0.00");
        jtfEntryValue.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jtfEntryValue);

        jbEntryAdd.setText("Agregar");
        jbEntryAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jbEntryAdd);

        jbEntryClear.setText("Limpiar");
        jbEntryClear.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryClear.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jbEntryClear);

        jbEntryPickLot.setText("Elegir lote");
        jbEntryPickLot.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryPickLot.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jbEntryPickLot);

        jPanel5.add(jPanel10);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbEntryDelete.setText("Eliminar");
        jbEntryDelete.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryDelete.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel13.add(jbEntryDelete);

        jbEntryViewLots.setText("Ver lotes");
        jbEntryViewLots.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryViewLots.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel13.add(jbEntryViewLots);

        jtfDpsSourceNumber.setEditable(false);
        jtfDpsSourceNumber.setText("DPS NUMBER");
        jtfDpsSourceNumber.setFocusable(false);
        jtfDpsSourceNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jtfDpsSourceNumber);

        jtfDpsSourceBizPartner.setEditable(false);
        jtfDpsSourceBizPartner.setText("BUSINESS PARTNER");
        jtfDpsSourceBizPartner.setFocusable(false);
        jtfDpsSourceBizPartner.setPreferredSize(new java.awt.Dimension(285, 23));
        jPanel13.add(jtfDpsSourceBizPartner);

        jbEntryImport.setText("Importar");
        jbEntryImport.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryImport.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel13.add(jbEntryImport);

        jPanel5.add(jPanel13);

        jPanel4.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jpDiogEntries.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlValue.setText("Total:");
        jlValue.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(jlValue);

        jtfValue.setEditable(false);
        jtfValue.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfValue.setText("0.00");
        jtfValue.setFocusable(false);
        jtfValue.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jtfValue);

        jpDiogEntries.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.add(jpDiogEntries, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(592, 33));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jpCommands1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel1.add(jpCommands1);

        jpCommands2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbEdit.setText("Modificar"); // NOI18N
        jpCommands2.add(jbEdit);

        jbEditHelp.setText("?");
        jbEditHelp.setToolTipText("¿Por qué no se puede modificar el documento?");
        jbEditHelp.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jbEditHelp.setPreferredSize(new java.awt.Dimension(23, 23));
        jpCommands2.add(jbEditHelp);

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCommands2.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpCommands2.add(jbCancel);

        jPanel1.add(jpCommands2);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1041)/2, (screenSize.height-634)/2, 1041, 634);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
       windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jcbProdOrderSourceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbProdOrderSourceItemStateChanged
        if (!mbResetingForm) {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                itemStateChangedProdOrderSource();
            }
        }
    }//GEN-LAST:event_jcbProdOrderSourceItemStateChanged

    private void jcbProdOrderDestinyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbProdOrderDestinyItemStateChanged
        if (!mbResetingForm) {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                itemStateChangedProdOrderDestiny();
            }
        }
    }//GEN-LAST:event_jcbProdOrderDestinyItemStateChanged

    private void jtfEntryTextToFindKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfEntryTextToFindKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jbEntryFind.doClick();
        }
    }//GEN-LAST:event_jtfEntryTextToFindKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionCancel();
    }//GEN-LAST:event_formWindowClosing

    private void initComponentsExtra() {
        int i = 0;
        STableColumnForm tableColumnsEntry[];

        mnParamIogCategoryId = SLibConstants.UNDEFINED;
        manParamIogClassKey = null;
        manParamIogTypeKey = null;
        manParamProdOrderSourceKey = null;

        mbCanShowForm = true;
        mbWarehouseDestinyNeeded = false;
        manLastWarehouseSourceKey = null;
        manLastWarehouseDestinyKey = null;

        mvDeletedDiogEntries = new Vector<SDataDiogEntry>();
        moPaneDiogEntries = new STablePane(miClient);
        jpDiogEntries.add(moPaneDiogEntries, BorderLayout.CENTER);

        moFieldDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDate, jlDate);
        moFieldDate.setPickerButton(jbDate);
        moFieldSeries = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbSeries, jlSeries);
        moFieldProdOrderSource = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbProdOrderSource, jlProdOrderSource);
        moFieldProdOrderSource.setPickerButton(jbProdOrderSource);
        moFieldProdOrderDestiny = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbProdOrderDestiny, jlProdOrderDestiny);
        moFieldProdOrderDestiny.setPickerButton(jbProdOrderDestiny);
        moFieldNotes = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNotes, jlNotes);
        moFieldNotes.setLengthMax(255);
        moFieldEntryQuantity = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfEntryQuantity, jlEntryQuantity);
        moFieldEntryQuantity.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
        moFieldEntryValueUnitary = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfEntryValueUnitary, jlEntryValueUnitary);
        moFieldEntryValueUnitary.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat());
        moFieldEntryValue = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfEntryValue, jlEntryValue);
        moFieldEntryValue.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat());
        moFieldDiogAdjustmentType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbDiogAdjustmentType, jlDiogType);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldDate);
        mvFields.add(moFieldSeries);
        mvFields.add(moFieldProdOrderSource);
        mvFields.add(moFieldProdOrderDestiny);
        mvFields.add(moFieldNotes);
        mvFields.add(moFieldEntryQuantity);
        mvFields.add(moFieldEntryValueUnitary);
        mvFields.add(moFieldEntryValue);
        mvFields.add(moFieldDiogAdjustmentType);

        moDialogStockLots = new SDialogStockLots(miClient, SLibConstants.MODE_QTY);
        moDialogStockLotsProdOrder = new SDialogStockLots(miClient, SLibConstants.MODE_QTY_EXT);
        moPickerStockLots = new SDialogPickerStockLots(miClient, true);
        moPickerWarehouseSource = new SDialogPickerCompanyBranchEntity(miClient, SDataConstantsSys.CFGS_CT_ENT_WH);
        moPickerWarehouseDestiny = new SDialogPickerCompanyBranchEntity(miClient, SDataConstantsSys.CFGS_CT_ENT_WH);
        moPickerProdOrderDestiny = null;                       // instanciated when needed
        moDialogDpsStockSupply = null;                  // instanciated when needed
        moDialogDpsStockReturn = null;                  // instanciated when needed
        moDialogProdOrderStockAssignForAssing = null;   // instanciated when needed
        moDialogProdOrderStockAssignForReturn = null;   // instanciated when needed
        moDialogProdOrderStockFinishForFinish = null;   // instanciated when needed
        moDialogProdOrderStockFinishForReturn = null;   // instanciated when needed

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbEdit.addActionListener(this);
        jbEditHelp.addActionListener(this);
        jbDate.addActionListener(this);
        jbWarehouseClear.addActionListener(this);
        jbWarehouseSource.addActionListener(this);
        jbWarehouseDestiny.addActionListener(this);
        jbWarehouseExchange.addActionListener(this);
        jbProdOrderSource.addActionListener(this);
        jbProdOrderDestiny.addActionListener(this);
        jbEntryFind.addActionListener(this);
        jtbDecimals.addActionListener(this);
        jbEntryAdd.addActionListener(this);
        jbEntryClear.addActionListener(this);
        jbEntryPickLot.addActionListener(this);
        jbEntryDelete.addActionListener(this);
        jbEntryImport.addActionListener(this);
        jbEntryViewLots.addActionListener(this);
        jtbSwitchProdOrderDestiny.addActionListener(this);
        jtfEntryTextToFind.addActionListener(this);
        jtfEntryQuantity.addFocusListener(this);
        jtfEntryValueUnitary.addFocusListener(this);
        jtfEntryValue.addFocusListener(this);

        i = 0;
        tableColumnsEntry = new STableColumnForm[14];
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave", STableConstants.WIDTH_ITEM_KEY);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem", 250);
        mnColQty = i;
        tableColumnsEntry[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cantidad", STableConstants.WIDTH_QUANTITY_2X);
        tableColumnsEntry[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Lotes", 150);
        tableColumnsEntry[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor u. $", STableConstants.WIDTH_VALUE_UNITARY);
        tableColumnsEntry[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
        tableColumnsEntry[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Valor $", STableConstants.WIDTH_VALUE_2X);
        tableColumnsEntry[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < tableColumnsEntry.length; i++) {
            moPaneDiogEntries.addTableColumn(tableColumnsEntry[i]);
        }

        moPaneDiogEntries.createTable();
        moPaneDiogEntries.setDoubleClickAction(this, "doubleClickPaneDiogEntries");

        SFormUtilities.createActionMap(rootPane, this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, 0);
        SFormUtilities.createActionMap(rootPane, this, "actionRowAdd", "rowAdd", KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionRowDelete", "rowDelete", KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionRowClear", "rowClear", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            mbCanShowForm = true;

            if (moWarehouseSource == null) {
                actionWarehouseSource();

                if (moWarehouseSource == null) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseSource.getText() + "' para crear el documento.");
                    mbCanShowForm = false;
                    actionCancel();
                }
            }

            if (mbCanShowForm && moDiog == null) {
                if (moWarehouseDestiny == null && mbWarehouseDestinyNeeded) {
                    actionWarehouseDestiny();

                    if (moWarehouseDestiny == null) {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseDestiny.getText() + "' para crear el documento.");
                        mbCanShowForm = false;
                        actionCancel();
                    }
                }
            }

            if (mbCanShowForm && moDiog == null) {
                // Only for new documents:

                if (!STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey)) {
                    manParamProdOrderSourceKey = null;
                    manParamProdOrderDestinyKey = null;
                }
                else {
                    if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey) && manParamProdOrderSourceKey != null && manParamProdOrderDestinyKey == null) {
                        pickProdOrderDestinyOnWindowActivated();

                        if (manParamProdOrderDestinyKey == null) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlProdOrderDestiny.getText() + "' para crear el documento.");
                            mbCanShowForm = false;
                            actionCancel();
                        }
                    }
                }
            }

            if (mbCanShowForm) {
                initFormData();

                if (moDiog == null && jcbSeries.getItemCount() == 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_CFG_DNS);
                    mbCanShowForm = false;
                    actionCancel();
                }

                if (mbCanShowForm) {
                    if (moDiog == null) {
                        moFieldDiogAdjustmentType.setFieldValue(new int[] { SDataConstantsSys.TRNU_TP_IOG_ADJ_NA });
                    }

                    if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT && (moParamDpsSource != null || moProdOrderSource != null)) {
                        actionEntryImport();
                    }
                    else if (jftDate.isEditable()) {
                        jftDate.requestFocus();
                    }
                    else {
                        jbCancel.requestFocus();
                    }
                }
            }
        }
    }

    private void renderWarehouseSource(int[] key, boolean validateIsOpen) {
        if (key == null) {
            moCompanyBranchSource = null;
            moWarehouseSource = null;

            jtfCompanyBranchSource.setText("");
            jtfCompanyBranchSourceCode.setText("");
            jtfWarehouseSource.setText("");
            jtfWarehouseSourceCode.setText("");
        }
        else {
            moCompanyBranchSource = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, key, SLibConstants.EXEC_MODE_VERBOSE);
            moWarehouseSource = (SDataCompanyBranchEntity) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_COB_ENT, key, SLibConstants.EXEC_MODE_VERBOSE);

            jtfCompanyBranchSource.setText(moCompanyBranchSource.getBizPartnerBranch());
            jtfCompanyBranchSourceCode.setText(moCompanyBranchSource.getCode());
            jtfWarehouseSource.setText(moWarehouseSource.getEntity());
            jtfWarehouseSourceCode.setText(moWarehouseSource.getCode());

            jtfCompanyBranchSource.setCaretPosition(0);
            jtfCompanyBranchSourceCode.setCaretPosition(0);
            jtfWarehouseSource.setCaretPosition(0);
            jtfWarehouseSourceCode.setCaretPosition(0);

            if (validateIsOpen) {
                isWarehouseSourceOpen();
            }
        }
    }

    private void renderWarehouseDestiny(int[] key, boolean validateIsOpen) {
        if (key == null) {
            moCompanyBranchDestiny = null;
            moWarehouseDestiny = null;

            jtfCompanyBranchDestiny.setText("");
            jtfCompanyBranchDestinyCode.setText("");
            jtfWarehouseDestiny.setText("");
            jtfWarehouseDestinyCode.setText("");
        }
        else {
            moCompanyBranchDestiny = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, key, SLibConstants.EXEC_MODE_VERBOSE);
            moWarehouseDestiny = (SDataCompanyBranchEntity) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_COB_ENT, key, SLibConstants.EXEC_MODE_VERBOSE);

            jtfCompanyBranchDestiny.setText(moCompanyBranchDestiny.getBizPartnerBranch());
            jtfCompanyBranchDestinyCode.setText(moCompanyBranchDestiny.getCode());
            jtfWarehouseDestiny.setText(moWarehouseDestiny.getEntity());
            jtfWarehouseDestinyCode.setText(moWarehouseDestiny.getCode());

            jtfCompanyBranchDestiny.setCaretPosition(0);
            jtfCompanyBranchDestinyCode.setCaretPosition(0);
            jtfWarehouseDestiny.setCaretPosition(0);
            jtfWarehouseDestinyCode.setCaretPosition(0);

            if (validateIsOpen) {
                isWarehouseDestinyOpen();
            }
        }
    }

    private boolean isWarehouseSourceOpen() {
        boolean open = false;

        if (moWarehouseSource == null) {
            miClient.showMsgBoxWarning("La entidad '" + jlWarehouseSource.getText() + "' no ha sido definida.");
        }
        else {
            if (!moWarehouseSource.getIsActive()) {
                miClient.showMsgBoxWarning("La entidad '" + jlWarehouseSource.getText() + "' no está activa.");
            }
            else if (mnParamIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN && !moWarehouseSource.getIsActiveIn()) {
                miClient.showMsgBoxWarning("La entidad '" + jlWarehouseSource.getText() + "' no está activa para movimientos de entrada.");
            }
            else if (mnParamIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_OUT && !moWarehouseSource.getIsActiveOut()) {
                miClient.showMsgBoxWarning("La entidad '" + jlWarehouseSource.getText() + "' no está activa para movimientos de salida.");
            }
            else {
                open = true;
            }
        }

        return open;
    }

    private boolean isWarehouseDestinyOpen() {
        boolean open = false;

        if (moWarehouseDestiny == null) {
            miClient.showMsgBoxWarning("La entidad '" + jlWarehouseDestiny.getText() + "' no ha sido definida.");
        }
        else {
            if (!moWarehouseDestiny.getIsActive()) {
                miClient.showMsgBoxWarning("La entidad '" + jlWarehouseDestiny.getText() + "' no está activa.");
            }
            else if (mnParamIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_OUT && !moWarehouseDestiny.getIsActiveIn()) {
                miClient.showMsgBoxWarning("La entidad '" + jlWarehouseDestiny.getText() + "' no está activa para movimientos de entrada.");
            }
            else if (mnParamIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN && !moWarehouseDestiny.getIsActiveOut()) {
                miClient.showMsgBoxWarning("La entidad '" + jlWarehouseDestiny.getText() + "' no está activa para movimientos de salida.");
            }
            else {
                open = true;
            }
        }

        return open;
    }

    private boolean isRegistryEditable() {
        boolean editable = true;

        if (moDiog.getIsShipped()) {
            editable = false;
        }
        else if (moDiog.getIsAudited()) {
            editable = false;
        }
        else if (moDiog.getIsAuthorized()) {
            editable = false;
        }
        else if (moDiog.getIsSystem()) {
            editable = false;
        }
        else if (moDiog.getIsDeleted()) {
            editable = false;
        }

        return editable;
    }

    private String getNonEditableHelp() {
        String help = "";

        if (moDiog.getIsShipped()) {
            help += "\n- El documento está embarcado.";
        }
        else if (moDiog.getIsAudited()) {
            help += "\n- El documento está auditado.";
        }
        else if (moDiog.getIsAuthorized()) {
            help += "\n- El documento está autorizado.";
        }
        else if (moDiog.getIsSystem()) {
            help += "\n- El documento es de sistema.";
        }
        else if (moDiog.getIsDeleted()) {
            help += "\n- El documento está eliminado.";
        }

        if (help.length() > 0) {
            help = "No se puede modificar el documento porque:" + help;
        }

        return help;
    }

    private boolean canEditEntry(erp.mtrn.data.SDataDiogEntry entry) {
        boolean can = true;

        if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
            if (entry.hasDiogLinksShipment() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DIOG_ETY_COUNT_SHIP, entry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                can = false;
                miClient.showMsgBoxWarning("Esta partida del documento está asociada al menos con un documento de embarques.");
            }
        }

        return  can;
    }

    private Vector<SDataDiogEntry> getCompleteEntries() {
        Vector<SDataDiogEntry> entries = new Vector<SDataDiogEntry>();

        for (STableRow row : moPaneDiogEntries.getTableModel().getTableRows()) {
            entries.add((SDataDiogEntry) row.getData());
        }

        entries.addAll(mvDeletedDiogEntries);

        return entries;
    }

    private void setUpControls() {
        boolean enable = false;

        if (mnFormStatus != SLibConstants.FORM_STATUS_EDIT) {
            jftDate.setEditable(false);
            jftDate.setFocusable(false);
            jbDate.setEnabled(false);

            jckIsShipmentRequired.setEnabled(false);

            jcbSeries.setEnabled(false);

            jtfNotes.setEditable(false);
            jtfNotes.setFocusable(false);

            jtfEntryTextToFind.setEditable(false);
            jtfEntryTextToFind.setFocusable(false);
            jtfEntryQuantity.setEditable(false);
            jtfEntryQuantity.setFocusable(false);
            jtfEntryValueUnitary.setEditable(false);
            jtfEntryValueUnitary.setFocusable(false);
            jtfEntryValue.setEditable(false);
            jtfEntryValue.setFocusable(false);

            jbEntryFind.setEnabled(false);
            jtbDecimals.setEnabled(false);
            jbEntryAdd.setEnabled(false);
            jbEntryClear.setEnabled(false);
            jbEntryDelete.setEnabled(false);
            jbEntryImport.setEnabled(false);
            jcbDiogAdjustmentType.setEnabled(false);
        }
        else {
            enable = !STrnUtilities.isIogTypeForProdOrderWp(manParamIogTypeKey) && !STrnUtilities.isIogTypeForProdOrderFg(manParamIogTypeKey);

            jftDate.setEditable(true);
            jftDate.setFocusable(true);
            jbDate.setEnabled(true);

            jckIsShipmentRequired.setEnabled(STrnUtilities.isIogTypeForTransfer(manParamIogTypeKey));

            jcbSeries.setEnabled(moDiog == null && jcbSeries.getItemCount() > 1);

            jtfNotes.setEditable(true);
            jtfNotes.setFocusable(true);

            jtfEntryTextToFind.setEditable(enable);
            jtfEntryTextToFind.setFocusable(enable);
            jtfEntryQuantity.setEditable(enable);
            jtfEntryQuantity.setFocusable(enable);
            jtfEntryValueUnitary.setEditable(enable);
            jtfEntryValueUnitary.setFocusable(enable);
            jtfEntryValue.setEditable(enable);
            jtfEntryValue.setFocusable(enable);

            jbEntryFind.setEnabled(enable);
            jtbDecimals.setEnabled(true);
            jbEntryAdd.setEnabled(enable);
            jbEntryClear.setEnabled(enable);
            jbEntryDelete.setEnabled(true);
            jbEntryImport.setEnabled(moParamDpsSource != null || moProdOrderSource != null || SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_ADJ_INV));
            jcbDiogAdjustmentType.setEnabled(STrnUtilities.isIogTypeAdjustment(manParamIogTypeKey));
        }

        jbEntryViewLots.setEnabled(true);

        setUpControlsWarehouses();
        setUpControlsProdOrders();
    }

    private void setUpControlsWarehouses() {
        boolean enable = false;

        if (mnFormStatus != SLibConstants.FORM_STATUS_EDIT) {
            jbWarehouseClear.setEnabled(false);
            jbWarehouseSource.setEnabled(false);
            jbWarehouseDestiny.setEnabled(false);
            jbWarehouseExchange.setEnabled(false);
        }
        else {
            enable = moPaneDiogEntries.getTableGuiRowCount() == 0;

            jbWarehouseClear.setEnabled(enable);
            jbWarehouseSource.setEnabled(enable);
            jbWarehouseDestiny.setEnabled(enable && mbWarehouseDestinyNeeded);
            jbWarehouseExchange.setEnabled(enable && mbWarehouseDestinyNeeded);
        }
    }

    private void setUpControlsProdOrders() {
        boolean enable = false;

        mbResetingForm = true;

        if (mnFormStatus != SLibConstants.FORM_STATUS_EDIT || !STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey)) {
            jcbProdOrderSource.setEnabled(false);
            jbProdOrderSource.setEnabled(false);

            if (moDiog == null) {
                moFieldProdOrderSource.setKey(null);
                itemStateChangedProdOrderSource();
            }

            jcbProdOrderDestiny.setEnabled(false);
            jbProdOrderDestiny.setEnabled(false);
            jtbSwitchProdOrderDestiny.setEnabled(false);
            jtbSwitchProdOrderDestiny.setSelected(false);

            if (moDiog == null) {
                moFieldProdOrderDestiny.setKey(null);
                itemStateChangedProdOrderDestiny();
            }
        }
        else {
            enable = moPaneDiogEntries.getTableGuiRowCount() == 0;

            jcbProdOrderSource.setEnabled(enable);
            jbProdOrderSource.setEnabled(enable);

            if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey)) {
                // Destiny production order is needed:

                jcbProdOrderDestiny.setEnabled(enable && jcbProdOrderSource.getSelectedIndex() > 0);
                jbProdOrderDestiny.setEnabled(enable && jcbProdOrderSource.getSelectedIndex() > 0);
                jtbSwitchProdOrderDestiny.setEnabled(enable && jcbProdOrderSource.getSelectedIndex() > 0);
                jtbSwitchProdOrderDestiny.setSelected(mbSwitchProdOrderDestinySelected || (moProdOrderSource != null && moProdOrderDestiny != null && !SLibUtilities.compareKeys(moProdOrderSource.getPrimaryKey(), moProdOrderDestiny.getParentProductionOrderKey())));
            }
            else {
                // Destiny production order is not needed:

                jcbProdOrderDestiny.setEnabled(false);
                jbProdOrderDestiny.setEnabled(false);
                jtbSwitchProdOrderDestiny.setEnabled(false);
                jtbSwitchProdOrderDestiny.setSelected(false);

                if (moDiog == null) {
                    moFieldProdOrderDestiny.setKey(null);
                    itemStateChangedProdOrderDestiny();
                }
            }
        }

        mbResetingForm = false;
    }

    private void updateNoEntriesRelatedControls() {
        boolean enable = mnFormStatus == SLibConstants.FORM_STATUS_EDIT && moPaneDiogEntries.getTableGuiRowCount() == 0 && moDiog == null;

        jftDate.setEditable(enable);
        jftDate.setFocusable(enable);
        jbDate.setEnabled(enable);

        jbWarehouseClear.setEnabled(enable);
        jbWarehouseSource.setEnabled(enable);
        jbWarehouseDestiny.setEnabled(enable && mbWarehouseDestinyNeeded);
        jbWarehouseExchange.setEnabled(enable && mbWarehouseDestinyNeeded);

        if (STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey)) {
            jcbProdOrderSource.setEnabled(enable);
            jbProdOrderSource.setEnabled(enable);

            if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey)) {
                jcbProdOrderDestiny.setEnabled(enable && jcbProdOrderSource.getSelectedIndex() > 0);
                jbProdOrderDestiny.setEnabled(enable && jcbProdOrderSource.getSelectedIndex() > 0);
                jtbSwitchProdOrderDestiny.setEnabled(enable && jcbProdOrderSource.getSelectedIndex() > 0);
            }
        }
    }

    private void pickProdOrderDestinyOnWindowActivated() {
        mbResetingForm = true;

        if (moPickerProdOrderDestiny == null) {
            moPickerProdOrderDestiny = new SDialogPickerProdOrder(miClient, SLibConstants.MODE_AS_DES);
        }

        moPickerProdOrderDestiny.formReset();
        moPickerProdOrderDestiny.setFormParams(manParamProdOrderSourceKey);
        moPickerProdOrderDestiny.formRefreshCatalogues();
        moPickerProdOrderDestiny.setVisible(true);

        if (moPickerProdOrderDestiny.getFormResult() != SLibConstants.FORM_RESULT_OK) {
            manParamProdOrderDestinyKey = null;
            mbSwitchProdOrderDestinySelected = false;
        }
        else {
            manParamProdOrderDestinyKey = moPickerProdOrderDestiny.getSelectedProdOrder();
            mbSwitchProdOrderDestinySelected = moPickerProdOrderDestiny.isSwitchProdOrderSelected();
        }

        jtbSwitchProdOrderDestiny.setSelected(mbSwitchProdOrderDestinySelected);

        mbResetingForm = false;
    }

    private void initFormData() {
        Vector<SFormComponentItem> items = null;
        SDataProductionOrder productionOrder = null;

        mbResetingForm = true;

        jtfDiogType.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNS_TP_IOG, manParamIogTypeKey));
        jtfDiogType.setCaretPosition(0);

        if (moDiog == null) {
            jcbSeries.removeAllItems();

            if (moWarehouseSource != null) {
                items = moWarehouseSource.getDnsForDiog(manParamIogClassKey);
                for (SFormComponentItem item : items) {
                    jcbSeries.addItem(item);
                }
            }
        }

        if (STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey)) {
            // When destiny production order is provided, check if source one is its parent:

            if (manParamProdOrderDestinyKey != null) {
                productionOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, manParamProdOrderDestinyKey, SLibConstants.EXEC_MODE_VERBOSE);
                if (!SLibUtilities.compareKeys(manParamProdOrderSourceKey, productionOrder.getParentProductionOrderKey()) && !jtbSwitchProdOrderDestiny.isSelected()) {
                    jtbSwitchProdOrderDestiny.setSelected(true);    // in order to show not only parent's children production orders
                }
            }

            // Render production orders:

            moFieldProdOrderSource.setFieldValue(manParamProdOrderSourceKey);
            itemStateChangedProdOrderSource();

            moFieldProdOrderDestiny.setFieldValue(manParamProdOrderDestinyKey);
            itemStateChangedProdOrderDestiny();
        }

        setUpControls();

        mbResetingForm = false;
    }

    private void computeEntryValue() {
        moFieldEntryValue.setDouble(moFieldEntryQuantity.getDouble() * moFieldEntryValueUnitary.getDouble());
    }

    private void computeEntryValueUnitary() {
        if (moFieldEntryValueUnitary.getDouble() == 0d) {
            moFieldEntryValueUnitary.setDouble(moFieldEntryQuantity.getDouble() == 0d ? 0d : moFieldEntryValue.getDouble() / moFieldEntryQuantity.getDouble());
        }
    }

    private void computeDocValue() {
        double value = 0;
        SDataDiogEntry entry = null;

        for (STableRow row : moPaneDiogEntries.getTableModel().getTableRows()) {
            entry = (SDataDiogEntry) row.getData();
            value += entry.getValue();
        }

        jtfValue.setText(miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(value));
        jtfValue.setCaretPosition(0);
    }

    private void renderItemEntry(int[] key, double quantity) {
        moEntryItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, key, SLibConstants.EXEC_MODE_SILENT);

        jtfEntryItem.setText(moEntryItem.getXtaItemWidthStatus());
        jtfEntryItemCode.setText(moEntryItem.getKey());
        jtfEntryUnitSymbol.setText(moEntryItem.getDbmsDataUnit().getSymbol());
        jtfEntryQuantity.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(quantity));

        jbEntryPickLot.setEnabled(moEntryItem.getIsLotApplying() && mnParamIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_OUT);
    }

    private String validateAppropriateWarehouses() {
        String msg = "";
        String msgSource = moWarehouseSource == null ? "" : STrnUtilities.validateAppropiateWarehouseType(miClient, manParamIogTypeKey, moWarehouseSource.getEntityTypeKey(), SLibConstants.MODE_AS_SRC);
        String msgDestiny = moWarehouseDestiny == null ? "" : STrnUtilities.validateAppropiateWarehouseType(miClient, manParamIogTypeKey, moWarehouseDestiny.getEntityTypeKey(), SLibConstants.MODE_AS_DES);

        if (msgSource.length() > 0) {
            msg += msg.length() > 0 ? "\n" : "El tipo de entidad de los almacenes es inadecuado para el tipo de documento '" + jtfDiogType.getText() + "':\n";
            msg += "- La entidad '" + jlWarehouseSource.getText() + "' '" + moWarehouseSource.getEntity() + "', código '" + moWarehouseSource.getCode() + "' es de tipo " +
                    "'" + SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGS_TP_ENT, moWarehouseSource.getEntityTypeKey()) + "',\n" +
                    "cuando debería ser '" + msgSource + "'.";
        }

        if (msgDestiny.length() > 0) {
            msg += msg.length() > 0 ? "\n" : "El tipo de entidad de los almacenes es inadecuado para el tipo de documento '" + jtfDiogType.getText() + "':\n";
            msg += "- La entidad '" + jlWarehouseDestiny.getText() + "' '" + moWarehouseDestiny.getEntity() + "', código '" + moWarehouseDestiny.getCode() + "' es de tipo " +
                    "'" + SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGS_TP_ENT, moWarehouseDestiny.getEntityTypeKey()) + "',\n" +
                    "cuando debería ser '" + msgDestiny + "'.";
        }

        if (msg.length() > 0) {
            if (miClient.showMsgBoxConfirm(msg + "\n\n" + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                msg = "";
            }
            else {
                msg = msgSource.length() == 0 ? "" : SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlWarehouseSource.getText() + "'.\n" +
                        "El tipo del almacén debe ser: '" + msgSource + "'.";

                if (msgDestiny.length() > 0) {
                    msg += (msg.length() == 0 ? "" : "\n\n") + SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlWarehouseDestiny.getText() + "'.\n" +
                            "El tipo del almacén debe ser: '" + msgDestiny + "'.";
                }
            }
        }

        return msg;
    }

    private boolean validateAppropriateWarehousesItem(int nItemId) {
        boolean bIsAppropriate = true;

        if (mnParamIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN || SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_IN_SAL_SAL)) {
            try {
                bIsAppropriate = STrnUtilities.getIsAppropiateWarehouseItem(miClient, nItemId, new int[] { moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() });
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        else if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_INT_TRA)) {
            try {
                bIsAppropriate = STrnUtilities.getIsAppropiateWarehouseItem(miClient, nItemId, new int[] { moWarehouseDestiny.getPkCompanyBranchId(), moWarehouseDestiny.getPkEntityId() });
            }
            catch (Exception ex) {
                SLibUtilities.renderException(this, ex);
            }
        }

        return bIsAppropriate;
    }

    private boolean validateAppropriateWarehouseDns() {
        boolean bIsApropriate = false;

        try {
            bIsApropriate = STrnUtilities.getIsAppropiateWarehouseDns(miClient, new int[] { moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() }, moParamDpsSource.getNumberSeries(),
                    new int[] { moParamDpsSource.getFkDpsCategoryId(), moParamDpsSource.getFkDpsClassId(), moParamDpsSource.getFkDpsTypeId() });
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return bIsApropriate;
    }

    private void importFromDps() {
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];
        int decsQty = miClient.getSessionXXX().getParamsErp().getDecimalsQuantity();
        int decsValUnit = miClient.getSessionXXX().getParamsErp().getDecimalsValueUnitary();
        int mode = STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey) ? SLibConstants.MODE_QTY_EXT : jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        boolean add = true;
        String msg = "";
        String sItemsInappropriate = "";
        SDataItem item = null;
        SDataDiog iog = null;
        SDataDiogEntry iogEntry = null;
        SDataDpsEntry dpsEntry = null;
        STrnDpsStockSupplyRow stockSupplyRow = null;
        STrnDpsStockReturnRow stockReturnRow = null;
        Vector<SDataDiogEntry> iogEntries = new Vector<SDataDiogEntry>();
        Vector<STrnDpsStockSupplyRow> stockSupplyRowsAux = new Vector<STrnDpsStockSupplyRow>();
        Vector<STrnDpsStockSupplyRow> stockSupplyRows = new Vector<STrnDpsStockSupplyRow>();
        Vector<STrnDpsStockReturnRow> stockReturnRowsAux = new Vector<STrnDpsStockReturnRow>();
        Vector<STrnDpsStockReturnRow> stockReturnRows = new Vector<STrnDpsStockReturnRow>();

        if (!SDataUtilities.isPeriodOpen(miClient, moFieldDate.getDate())) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
            jftDate.requestFocus();
        }
        else if (moDiog != null && moDiog.getPkYearId() != year) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_YEAR);
            jftDate.requestFocus();
        }
        else if (moWarehouseSource == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseSource.getText() + "'.");
        }
        else if ((moParamDpsSource.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_SAL) && !validateAppropriateWarehouseDns()) {
            miClient.showMsgBoxWarning("La serie '" + moParamDpsSource.getNumberSeries() + "' del doc. de c/v no está configurada para el almacén '" + moWarehouseSource.getEntity() + "', código '" + moWarehouseSource.getCode() + "'.");
        }
        else {
            msg = validateAppropriateWarehouses();

            if (msg.length() > 0) {
                miClient.showMsgBoxWarning(msg);
            }
            else {
                iog = moDiog != null ? moDiog : new SDataDiog();
                iog.setDate(moFieldDate.getDate());
                iog.setFkCompanyBranchId(moWarehouseSource.getPkCompanyBranchId());
                iog.setFkWarehouseId(moWarehouseSource.getPkEntityId());

                iog.getDbmsEntries().clear();
                iog.getDbmsEntries().addAll(getCompleteEntries());

                if (STrnUtilities.isIogTypeForDpsSupply(manParamIogTypeKey)) {

                    if (moDialogDpsStockSupply == null) {
                        moDialogDpsStockSupply = new SDialogDpsStockSupply(miClient);
                    }

                    moDialogDpsStockSupply.formReset();
                    moDialogDpsStockSupply.setFormParams(moParamDpsSource, iog);
                    moDialogDpsStockSupply.setVisible(true);

                    if (moDialogDpsStockSupply.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                        stockSupplyRows = moDialogDpsStockSupply.obtainDpsStockSupplyRows();

                        for (int row = 0; row < stockSupplyRows.size(); row++) {
                            stockSupplyRow = stockSupplyRows.get(row);
                            item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { stockSupplyRow.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);

                            if (validateAppropriateWarehousesItem(item.getPkItemId())) {
                                stockSupplyRowsAux.add(stockSupplyRows.get(row));
                            }
                            else {
                                sItemsInappropriate += "  * '" + item.getKey() + " - " + item.getItem() + "'\n";
                            }
                        }

                        if (sItemsInappropriate.length() > 0) {
                            miClient.showMsgBoxInformation("Los ítems:\n" + sItemsInappropriate + " no están configurados para el almacén '" +
                                    moWarehouseSource.getEntity() + "', código '" + moWarehouseSource.getCode() + "'.");
                        }

                        for (int row = 0; row < stockSupplyRowsAux.size(); row++) {
                            stockSupplyRow = stockSupplyRowsAux.get(row);
                            item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { stockSupplyRow.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);

                            if (item.getIsLotApplying()) {
                                moDialogStockLots.formReset();
                                moDialogStockLots.setFormParams(mnParamIogCategoryId, year, stockSupplyRow.getFkItemId(), stockSupplyRow.getFkUnitId(),
                                        (int[]) moWarehouseSource.getPrimaryKey(), (int[]) (moDiog == null ? null : moDiog.getPrimaryKey()),
                                        stockSupplyRow.getQuantityToSupply(), SLibConstants.FORM_STATUS_EDIT, mode, moParamDpsSource, moFieldDate.getDate());
                                moDialogStockLots.setCurrentEntry(row + 1, stockSupplyRowsAux.size());
                                moDialogStockLots.setVisible(true);

                                if (moDialogStockLots.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                    add = false;
                                    break;
                                }
                            }

                            dpsEntry = moParamDpsSource.getDbmsDpsEntry(stockSupplyRow.getDpsEntryKey());

                            iogEntry = new SDataDiogEntry();
                            iogEntry.setPkYearId(SLibConstants.UNDEFINED);
                            iogEntry.setPkDocId(SLibConstants.UNDEFINED);
                            iogEntry.setPkEntryId(SLibConstants.UNDEFINED);
                            iogEntry.setQuantity(stockSupplyRow.getQuantityToSupply());
                            iogEntry.setValueUnitary(dpsEntry.getPriceUnitaryReal_r());
                            iogEntry.setValue(SLibUtilities.round(dpsEntry.getPriceUnitaryReal_r() * stockSupplyRow.getQuantityToSupply(), decsQty));
                            iogEntry.setOriginalQuantity(stockSupplyRow.getOriginalQuantityToSupply());
                            iogEntry.setOriginalValueUnitary(iogEntry.getOriginalQuantity() == 0 ? 0 : SLibUtilities.round(iogEntry.getValue() / iogEntry.getOriginalQuantity(), decsValUnit));
                            iogEntry.setSortingPosition(0);
                            iogEntry.setIsInventoriable(item.getIsInventoriable());
                            iogEntry.setIsDeleted(false);
                            iogEntry.setFkItemId(stockSupplyRow.getFkItemId());
                            iogEntry.setFkUnitId(stockSupplyRow.getFkUnitId());
                            iogEntry.setFkOriginalUnitId(stockSupplyRow.getFkOriginalUnitId());

                            iogEntry.setFkDpsYearId_n(stockSupplyRow.getPkDpsYearId());
                            iogEntry.setFkDpsDocId_n(stockSupplyRow.getPkDpsDocId());
                            iogEntry.setFkDpsEntryId_n(stockSupplyRow.getPkDpsEntryId());
                            iogEntry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkDpsAdjustmentEntryId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkMfgOrderId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkMfgChargeId_n(SLibConstants.UNDEFINED);

                            iogEntry.setFkUserNewId(1);
                            iogEntry.setFkUserEditId(1);
                            iogEntry.setFkUserDeleteId(1);

                            iogEntry.setDbmsItem(stockSupplyRow.getAuxItem());
                            iogEntry.setDbmsItemKey(stockSupplyRow.getAuxItemKey());
                            iogEntry.setDbmsUnit(stockSupplyRow.getAuxUnit());
                            iogEntry.setDbmsUnitSymbol(stockSupplyRow.getAuxUnitSymbol());
                            iogEntry.setDbmsOriginalUnit(stockSupplyRow.getAuxOriginalUnit());
                            iogEntry.setDbmsOriginalUnitSymbol(stockSupplyRow.getAuxOriginalUnitSymbol());

                            if (item.getIsLotApplying()) {
                                iogEntry.getAuxStockMoves().addAll(moDialogStockLots.getStockMoves());
                            }
                            else {
                                iogEntry.getAuxStockMoves().add(new STrnStockMove(new int[] { year, iogEntry.getFkItemId(), iogEntry.getFkUnitId(), SDataConstantsSys.TRNX_STK_LOT_DEF_ID, moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() }, iogEntry.getQuantity()));
                            }

                            iogEntries.add(iogEntry);
                        }

                        if (add) {
                            for (SDataDiogEntry e : iogEntries) {
                                moPaneDiogEntries.addTableRow(new SDataDiogEntryRow(e));

                                if (moPaneDiogEntries.getTableGuiRowCount() == 1) {
                                    updateNoEntriesRelatedControls();    // if needed, inactivate production order controls
                                }
                            }

                            moPaneDiogEntries.renderTableRows();
                            moPaneDiogEntries.setTableRowSelection(moPaneDiogEntries.getTableGuiRowCount() - 1);

                            actionEntryClear();
                            computeDocValue();
                        }
                    }
                }
                else if (STrnUtilities.isIogTypeForDpsReturn(manParamIogTypeKey)) {
                    if (moDialogDpsStockReturn == null) {
                        moDialogDpsStockReturn = new SDialogDpsStockReturn(miClient);
                    }

                    moDialogDpsStockReturn.formReset();
                    moDialogDpsStockReturn.setFormParams(moParamDpsSource, iog);
                    moDialogDpsStockReturn.setVisible(true);

                    if (moDialogDpsStockReturn.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                        stockReturnRows = moDialogDpsStockReturn.obtainDpsStockReturnRows();

                        for (int row = 0; row < stockReturnRows.size(); row++) {
                            stockReturnRow = stockReturnRows.get(row);
                            item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { stockReturnRow.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);

                            if (validateAppropriateWarehousesItem(item.getPkItemId())) {
                                stockReturnRowsAux.add(stockReturnRows.get(row));
                            }
                            else {
                                sItemsInappropriate += "  * '" + item.getKey() + " - " + item.getItem() + "'\n";
                            }
                        }

                        if (sItemsInappropriate.length() > 0) {
                            miClient.showMsgBoxInformation("Los ítems:\n" + sItemsInappropriate + " no están configurados para el almacén '" +
                                    moWarehouseSource.getEntity() + "', código '" + moWarehouseSource.getCode() + "'.");
                        }

                        for (int row = 0; row < stockReturnRowsAux.size(); row++) {
                            stockReturnRow = stockReturnRowsAux.get(row);
                            item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { stockReturnRow.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);

                            if (item.getIsLotApplying()) {
                                moDialogStockLots.formReset();
                                moDialogStockLots.setFormParams(mnParamIogCategoryId, year, stockReturnRow.getFkItemId(), stockReturnRow.getFkUnitId(),
                                        (int[]) moWarehouseSource.getPrimaryKey(), (int[]) (moDiog == null ? null : moDiog.getPrimaryKey()),
                                        stockReturnRow.getQuantityToReturn(), SLibConstants.FORM_STATUS_EDIT, mode, moParamDpsSource, moFieldDate.getDate());
                                moDialogStockLots.setCurrentEntry(row + 1, stockReturnRowsAux.size());
                                moDialogStockLots.setVisible(true);

                                if (moDialogStockLots.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                    add = false;
                                    break;
                                }
                            }

                            dpsEntry = moParamDpsSource.getDbmsDpsEntry(stockReturnRow.getDpsAdjustmentEntryKey());

                            iogEntry = new SDataDiogEntry();
                            iogEntry.setPkYearId(SLibConstants.UNDEFINED);
                            iogEntry.setPkDocId(SLibConstants.UNDEFINED);
                            iogEntry.setPkEntryId(SLibConstants.UNDEFINED);
                            iogEntry.setQuantity(stockReturnRow.getQuantityToReturn());
                            iogEntry.setValueUnitary(dpsEntry.getPriceUnitaryReal_r());
                            iogEntry.setValue(SLibUtilities.round(dpsEntry.getPriceUnitaryReal_r() * stockReturnRow.getQuantityToReturn(), decsQty));
                            iogEntry.setOriginalQuantity(stockReturnRow.getOriginalQuantityToReturn());
                            iogEntry.setOriginalValueUnitary(iogEntry.getOriginalQuantity() == 0 ? 0 : SLibUtilities.round(iogEntry.getValue() / iogEntry.getOriginalQuantity(), decsValUnit));
                            iogEntry.setSortingPosition(0);
                            iogEntry.setIsInventoriable(item.getIsInventoriable());
                            iogEntry.setIsDeleted(false);
                            iogEntry.setFkItemId(stockReturnRow.getFkItemId());
                            iogEntry.setFkUnitId(stockReturnRow.getFkUnitId());
                            iogEntry.setFkOriginalUnitId(stockReturnRow.getFkOriginalUnitId());

                            iogEntry.setFkDpsYearId_n(stockReturnRow.getFkDpsYearId());
                            iogEntry.setFkDpsDocId_n(stockReturnRow.getFkDpsDocId());
                            iogEntry.setFkDpsEntryId_n(stockReturnRow.getFkDpsEntryId());
                            iogEntry.setFkDpsAdjustmentYearId_n(stockReturnRow.getPkDpsAdjustmentYearId());
                            iogEntry.setFkDpsAdjustmentDocId_n(stockReturnRow.getPkDpsAdjustmentDocId());
                            iogEntry.setFkDpsAdjustmentEntryId_n(stockReturnRow.getPkDpsAdjustmentEntryId());
                            iogEntry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkMfgOrderId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkMfgChargeId_n(SLibConstants.UNDEFINED);

                            iogEntry.setFkUserNewId(1);
                            iogEntry.setFkUserEditId(1);
                            iogEntry.setFkUserDeleteId(1);

                            iogEntry.setDbmsItem(stockReturnRow.getAuxItem());
                            iogEntry.setDbmsItemKey(stockReturnRow.getAuxItemKey());
                            iogEntry.setDbmsUnit(stockReturnRow.getAuxUnit());
                            iogEntry.setDbmsUnitSymbol(stockReturnRow.getAuxUnitSymbol());
                            iogEntry.setDbmsOriginalUnit(stockReturnRow.getAuxOriginalUnit());
                            iogEntry.setDbmsOriginalUnitSymbol(stockReturnRow.getAuxOriginalUnitSymbol());

                            if (item.getIsLotApplying()) {
                                iogEntry.getAuxStockMoves().addAll(moDialogStockLots.getStockMoves());
                            }
                            else {
                                iogEntry.getAuxStockMoves().add(new STrnStockMove(new int[] { year, iogEntry.getFkItemId(), iogEntry.getFkUnitId(), SDataConstantsSys.TRNX_STK_LOT_DEF_ID, moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() }, iogEntry.getQuantity()));
                            }

                            iogEntries.add(iogEntry);
                        }

                        if (add) {
                            for (SDataDiogEntry e : iogEntries) {
                                moPaneDiogEntries.addTableRow(new SDataDiogEntryRow(e));

                                if (moPaneDiogEntries.getTableGuiRowCount() == 1) {
                                    updateNoEntriesRelatedControls();    // if needed, inactivate production order controls
                                }
                            }

                            moPaneDiogEntries.renderTableRows();
                            moPaneDiogEntries.setTableRowSelection(moPaneDiogEntries.getTableGuiRowCount() - 1);

                            actionEntryClear();
                            computeDocValue();
                        }
                    }
                }
                else {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                }
            }
        }
    }

    private void importFromProdOrder() {
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];
        int[] key = null;
        int mode = STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey) ? SLibConstants.MODE_QTY_EXT : jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        boolean add = true;
        boolean proceed = true;
        String msg = "";
        SDataItem item = null;
        SDataStockLot lot = null;
        SDataDiog iog = null;
        SDataDiogEntry iogEntry = null;
        STrnProdOrderStockAssignRow stockAssignRow = null;
        Vector<STrnProdOrderStockAssignRow> stockAssignRows = new Vector<STrnProdOrderStockAssignRow>();
        STrnProdOrderStockFinishRow stockFinishRow = null;
        Vector<STrnProdOrderStockFinishRow> stockFinishRows = new Vector<STrnProdOrderStockFinishRow>();
        STrnStockMove stockMove = null;
        Vector<STrnStockMove> stockMoves = null;
        Vector<SDataDiogEntry> iogEntries = new Vector<SDataDiogEntry>();

        // Validate document settings:

        if (!SDataUtilities.isPeriodOpen(miClient, moFieldDate.getDate())) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
            jftDate.requestFocus();
        }
        else if (moDiog != null && moDiog.getPkYearId() != year) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_YEAR);
            jftDate.requestFocus();
        }
        else if (moWarehouseSource == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseSource.getText() + "'.");
        }
        else if (moWarehouseDestiny == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseDestiny.getText() + "'.");
        }
        else if (!STrnUtilities.canIogTypeWarehousesBeTheSame(manParamIogTypeKey) && SLibUtilities.compareKeys(moWarehouseSource.getPrimaryKey(), moWarehouseDestiny.getPrimaryKey())) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlWarehouseDestiny.getText() + "'.\nEl almacén origen y el almacén destino deben ser distintos.");
        }
        else if (jcbProdOrderSource.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlProdOrderSource.getText() + "'.");
            jcbProdOrderSource.requestFocus();
        }
        else if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey) && jcbProdOrderDestiny.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlProdOrderDestiny.getText() + "'.");
            jcbProdOrderDestiny.requestFocus();
        }
        else if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey) && SLibUtilities.compareKeys(moFieldProdOrderSource.getKeyAsIntArray(), moFieldProdOrderDestiny.getKeyAsIntArray())) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlProdOrderDestiny.getText() + "'.\nLa orden de producción origen y la orden de producción destino deben ser distintas.");
            jcbProdOrderDestiny.requestFocus();
        }
        else {
            msg = validateAppropriateWarehouses();

            if (msg.length() > 0) {
                miClient.showMsgBoxWarning(msg);
            }
            else {
                if (STrnUtilities.isIogTypeForProdOrderWp(manParamIogTypeKey) || STrnUtilities.isIogTypeForProdOrderFg(manParamIogTypeKey)) {
                    // Validate if lot has being set when necessary:

                    if (moItemProdOrderSource.getIsLotApplying()) {
                        if (moProdOrderSource.getFkLotId_n() == SLibConstants.UNDEFINED) {
                            proceed = false;
                            miClient.showMsgBoxWarning("El lote del ítem '" + jtfItemSource.getText() + "', clave '" + jtfItemSourceCode.getText() + "' no ha sido definido en la orden de producción.");
                        }
                        else {
                            key = new int[] { moProdOrderSource.getFkLotItemId_nr(), moProdOrderSource.getFkLotUnitId_nr(), moProdOrderSource.getFkLotId_n() }; // just for more readable code
                            lot = (SDataStockLot) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_LOT, key, SLibConstants.EXEC_MODE_VERBOSE);
                        }

                        if (proceed) {
                            if (lot == null) {
                                proceed = false;
                                miClient.showMsgBoxWarning("El lote del ítem '" + jtfItemSource.getText() + "', clave '" + jtfItemSourceCode.getText() + "' no existe.");
                            }
                            else {
                                if (lot.getIsBlocked()) {
                                    proceed = false;
                                    miClient.showMsgBoxWarning("El lote '" + lot.getLot() + "' del ítem '" + jtfItemSource.getText() + "', clave '" + jtfItemSourceCode.getText() + "' está bloqueado.");
                                }
                                else if (lot.getIsDeleted()) {
                                    proceed = false;
                                    miClient.showMsgBoxWarning("El lote '" + lot.getLot() + "' del ítem '" + jtfItemSource.getText() + "', clave '" + jtfItemSourceCode.getText() + "' está eliminado.");
                                }
                            }
                        }
                    }
                }

                if (proceed) {
                    iog = moDiog != null ? moDiog : new SDataDiog();
                    iog.setDate(moFieldDate.getDate());
                    iog.setFkCompanyBranchId(moWarehouseSource.getPkCompanyBranchId());
                    iog.setFkWarehouseId(moWarehouseSource.getPkEntityId());

                    iog.getDbmsEntries().clear();
                    iog.getDbmsEntries().addAll(getCompleteEntries());

                    if (STrnUtilities.isIogTypeForProdOrderRm(manParamIogTypeKey)) {
                        // Raw materials move:

                        if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD)) {
                            // Assign move:

                            if (moDialogProdOrderStockAssignForAssing == null) {
                                moDialogProdOrderStockAssignForAssing = new SDialogProdOrderStockAssign(miClient, SLibConstants.MODE_STK_ASD);
                            }

                            moDialogProdOrderStockAssignForAssing.formReset();
                            moDialogProdOrderStockAssignForAssing.setFormParams(iog, moProdOrderSource, (int[]) moWarehouseSource.getPrimaryKey(), (int[]) moWarehouseDestiny.getPrimaryKey());
                            moDialogProdOrderStockAssignForAssing.setVisible(true);

                            if (moDialogProdOrderStockAssignForAssing.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                add = false;
                            }
                            else {
                                stockAssignRows = moDialogProdOrderStockAssignForAssing.obtainProdOrderStockAssignRows();
                            }
                        }
                        else if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET)) {
                            // Return move:

                            if (moDialogProdOrderStockAssignForReturn == null) {
                                moDialogProdOrderStockAssignForReturn = new SDialogProdOrderStockAssign(miClient, SLibConstants.MODE_STK_RET);
                            }

                            moDialogProdOrderStockAssignForReturn.formReset();
                            moDialogProdOrderStockAssignForReturn.setFormParams(iog, moProdOrderSource, (int[]) moWarehouseSource.getPrimaryKey(), (int[]) moWarehouseDestiny.getPrimaryKey());
                            moDialogProdOrderStockAssignForReturn.setVisible(true);

                            if (moDialogProdOrderStockAssignForReturn.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                add = false;
                            }
                            else {
                                stockAssignRows = moDialogProdOrderStockAssignForReturn.obtainProdOrderStockAssignRows();
                            }
                        }
                        else {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                        }

                        if (add) {
                            for (int row = 0; row < stockAssignRows.size(); row++) {
                                stockAssignRow = stockAssignRows.get(row);
                                item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { stockAssignRow.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);

                                if (item.getIsLotApplying()) {
                                    // Obtain lots defined for production order:

                                    if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD)) {
                                        stockMoves = STrnUtilities.getProdOrderChargeLotsToAssign(miClient, moProdOrderSource, year, (int[]) moWarehouseSource.getPrimaryKey(),
                                                stockAssignRow.getPkChargeId(), stockAssignRow.getFkItemId(), stockAssignRow.getFkUnitId(), stockAssignRow.getQuantityAssigned(), stockAssignRow.getQuantityToAssign());
                                    }
                                    else {
                                        stockMoves = STrnUtilities.getProdOrderChargeLotsToReturn(miClient, moProdOrderSource, year, (int[]) moWarehouseSource.getPrimaryKey(),
                                                stockAssignRow.getPkChargeId(), stockAssignRow.getFkItemId(), stockAssignRow.getFkUnitId(), stockAssignRow.getQuantityAssigned(), stockAssignRow.getQuantityToAssign());
                                    }

                                    // Define lots for document entry:

                                    try {
                                        moDialogStockLotsProdOrder.formReset();
                                        moDialogStockLotsProdOrder.setFormParams(mnParamIogCategoryId, year, stockAssignRow.getFkItemId(), stockAssignRow.getFkUnitId(),
                                                (int[]) moWarehouseSource.getPrimaryKey(), (int[]) (moDiog == null ? null : moDiog.getPrimaryKey()),
                                                stockAssignRow.getQuantityToAssign(), SLibConstants.FORM_STATUS_EDIT, mode, moParamDpsSource, moFieldDate.getDate());
                                        moDialogStockLotsProdOrder.setStockMoves(stockMoves);
                                        moDialogStockLotsProdOrder.setCurrentEntry(row + 1, stockAssignRows.size());
                                        moDialogStockLotsProdOrder.setVisible(true);

                                        if (moDialogStockLotsProdOrder.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                            add = false;
                                            break;
                                        }
                                    }
                                    catch (Exception e) {
                                        SLibUtilities.renderException(this, e);
                                    }
                                }

                                iogEntry = new SDataDiogEntry();
                                iogEntry.setPkYearId(SLibConstants.UNDEFINED);
                                iogEntry.setPkDocId(SLibConstants.UNDEFINED);
                                iogEntry.setPkEntryId(SLibConstants.UNDEFINED);
                                iogEntry.setQuantity(stockAssignRow.getQuantityToAssign());
                                iogEntry.setValueUnitary(0);
                                iogEntry.setValue(0);
                                iogEntry.setOriginalQuantity(stockAssignRow.getQuantityToAssign());
                                iogEntry.setOriginalValueUnitary(0);
                                iogEntry.setSortingPosition(0);
                                iogEntry.setIsInventoriable(item.getIsInventoriable());
                                iogEntry.setIsDeleted(false);
                                iogEntry.setFkItemId(stockAssignRow.getFkItemId());
                                iogEntry.setFkUnitId(stockAssignRow.getFkUnitId());
                                iogEntry.setFkOriginalUnitId(stockAssignRow.getFkUnitId());

                                iogEntry.setFkDpsYearId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsDocId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsEntryId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsAdjustmentEntryId_n(SLibConstants.UNDEFINED);

                                iogEntry.setFkMfgYearId_n(stockAssignRow.getPkYearId());
                                iogEntry.setFkMfgOrderId_n(stockAssignRow.getPkOrderId());
                                iogEntry.setFkMfgChargeId_n(stockAssignRow.getPkChargeId());

                                iogEntry.setClonedFkMfgYearId_n(SLibConstants.UNDEFINED);
                                iogEntry.setClonedFkMfgOrderId_n(SLibConstants.UNDEFINED);
                                iogEntry.setClonedFkMfgChargeId_n(SLibConstants.UNDEFINED);

                                iogEntry.setFkUserNewId(1);
                                iogEntry.setFkUserEditId(1);
                                iogEntry.setFkUserDeleteId(1);

                                iogEntry.setDbmsItem(stockAssignRow.getAuxItem());
                                iogEntry.setDbmsItemKey(stockAssignRow.getAuxItemKey());
                                iogEntry.setDbmsUnit(stockAssignRow.getAuxUnit());
                                iogEntry.setDbmsUnitSymbol(stockAssignRow.getAuxUnitSymbol());
                                iogEntry.setDbmsOriginalUnit(stockAssignRow.getAuxUnit());
                                iogEntry.setDbmsOriginalUnitSymbol(stockAssignRow.getAuxUnitSymbol());

                                if (item.getIsLotApplying()) {
                                    iogEntry.getAuxStockMoves().addAll(moDialogStockLotsProdOrder.getStockMoves());
                                }
                                else {
                                    key = new int[] { year, iogEntry.getFkItemId(), iogEntry.getFkUnitId(), SDataConstantsSys.TRNX_STK_LOT_DEF_ID, moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() };   // just for more readable code
                                    iogEntry.getAuxStockMoves().add(new STrnStockMove(key, iogEntry.getQuantity()));
                                }

                                iogEntries.add(iogEntry);
                            }

                            if (add) {
                                for (SDataDiogEntry entry : iogEntries) {
                                    moPaneDiogEntries.addTableRow(new SDataDiogEntryRow(entry));

                                    if (moPaneDiogEntries.getTableGuiRowCount() == 1) {
                                        updateNoEntriesRelatedControls();    // if needed, inactivate production order controls
                                    }
                                }

                                moPaneDiogEntries.renderTableRows();
                                moPaneDiogEntries.setTableRowSelection(moPaneDiogEntries.getTableGuiRowCount() - 1);

                                actionEntryClear();
                                computeDocValue();
                            }
                        }
                    }
                    else if (STrnUtilities.isIogTypeForProdOrderWp(manParamIogTypeKey) || STrnUtilities.isIogTypeForProdOrderFg(manParamIogTypeKey)) {
                        // Work in progress or finished goods move:

                        if (SLibUtilities.belongsTo(manParamIogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD })) {
                            // Assign move:

                            if (moDialogProdOrderStockFinishForFinish == null) {
                                moDialogProdOrderStockFinishForFinish = new SDialogProdOrderStockFinish(miClient, SLibConstants.MODE_STK_ASD);
                            }

                            moDialogProdOrderStockFinishForFinish.formReset();
                            moDialogProdOrderStockFinishForFinish.setFormParams(iog, moProdOrderSource, moProdOrderDestiny, (int[]) moWarehouseSource.getPrimaryKey(), (int[]) moWarehouseDestiny.getPrimaryKey());
                            moDialogProdOrderStockFinishForFinish.setVisible(true);

                            if (moDialogProdOrderStockFinishForFinish.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                add = false;
                            }
                            else {
                                stockFinishRows = moDialogProdOrderStockFinishForFinish.obtainProdOrderStockFinishRows();
                            }
                        }
                        else if (SLibUtilities.belongsTo(manParamIogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET })) {
                            // Return move:

                            if (moDialogProdOrderStockFinishForReturn == null) {
                                moDialogProdOrderStockFinishForReturn = new SDialogProdOrderStockFinish(miClient, SLibConstants.MODE_STK_RET);
                            }

                            moDialogProdOrderStockFinishForReturn.formReset();
                            moDialogProdOrderStockFinishForReturn.setFormParams(iog, moProdOrderSource, moProdOrderDestiny, (int[]) moWarehouseSource.getPrimaryKey(), (int[]) moWarehouseDestiny.getPrimaryKey());
                            moDialogProdOrderStockFinishForReturn.setVisible(true);

                            if (moDialogProdOrderStockFinishForReturn.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                add = false;
                            }
                            else {
                                stockFinishRows = moDialogProdOrderStockFinishForReturn.obtainProdOrderStockFinishRows();
                            }
                        }
                        else {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                        }

                        if (add) {
                            for (int row = 0; row < stockFinishRows.size(); row++) {
                                stockFinishRow = stockFinishRows.get(row);

                                iogEntry = new SDataDiogEntry();
                                iogEntry.setPkYearId(SLibConstants.UNDEFINED);
                                iogEntry.setPkDocId(SLibConstants.UNDEFINED);
                                iogEntry.setPkEntryId(SLibConstants.UNDEFINED);
                                iogEntry.setQuantity(stockFinishRow.getQuantityToFinish());
                                iogEntry.setValueUnitary(0);
                                iogEntry.setValue(0);
                                iogEntry.setOriginalQuantity(stockFinishRow.getQuantityToFinish());
                                iogEntry.setOriginalValueUnitary(0);
                                iogEntry.setSortingPosition(0);
                                iogEntry.setIsInventoriable(moItemProdOrderSource.getIsInventoriable());
                                iogEntry.setIsDeleted(false);
                                iogEntry.setFkItemId(moProdOrderSource.getFkItemId_r());
                                iogEntry.setFkUnitId(moProdOrderSource.getFkUnitId_r());
                                iogEntry.setFkOriginalUnitId(moProdOrderSource.getFkUnitId_r());

                                iogEntry.setFkDpsYearId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsDocId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsEntryId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
                                iogEntry.setFkDpsAdjustmentEntryId_n(SLibConstants.UNDEFINED);

                                if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET)) {
                                    // Work in progress return moves exchange warehouses:

                                    iogEntry.setFkMfgYearId_n(stockFinishRow.getFkDestinyYearId());
                                    iogEntry.setFkMfgOrderId_n(stockFinishRow.getFkDestinyOrderId());
                                    iogEntry.setFkMfgChargeId_n(stockFinishRow.getFkDestinyChargeId());

                                    iogEntry.setClonedFkMfgYearId_n(SLibConstants.UNDEFINED);
                                    iogEntry.setClonedFkMfgOrderId_n(SLibConstants.UNDEFINED);
                                    iogEntry.setClonedFkMfgChargeId_n(SLibConstants.UNDEFINED);
                                }
                                else {
                                    // Other moves use warehouses as they were defined:

                                    iogEntry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
                                    iogEntry.setFkMfgOrderId_n(SLibConstants.UNDEFINED);
                                    iogEntry.setFkMfgChargeId_n(SLibConstants.UNDEFINED);

                                    iogEntry.setClonedFkMfgYearId_n(stockFinishRow.getFkDestinyYearId());
                                    iogEntry.setClonedFkMfgOrderId_n(stockFinishRow.getFkDestinyOrderId());
                                    iogEntry.setClonedFkMfgChargeId_n(stockFinishRow.getFkDestinyChargeId());
                                }

                                iogEntry.setFkUserNewId(1);
                                iogEntry.setFkUserEditId(1);
                                iogEntry.setFkUserDeleteId(1);

                                iogEntry.setDbmsItem(moItemProdOrderSource.getItem());
                                iogEntry.setDbmsItemKey(moItemProdOrderSource.getKey());
                                iogEntry.setDbmsUnit(moUnitProdOrderSource.getUnit());
                                iogEntry.setDbmsUnitSymbol(moUnitProdOrderSource.getSymbol());
                                iogEntry.setDbmsOriginalUnit(moUnitProdOrderSource.getUnit());
                                iogEntry.setDbmsOriginalUnitSymbol(moUnitProdOrderSource.getSymbol());

                                if (moItemProdOrderSource.getIsLotApplying()) {
                                    key = new int[] { year, moProdOrderSource.getFkLotItemId_nr(), moProdOrderSource.getFkLotUnitId_nr(), moProdOrderSource.getFkLotId_n(), moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() };      // just for more readable code
                                    stockMove = new STrnStockMove(key, stockFinishRow.getQuantityToFinish());
                                    stockMove.setAuxLot(lot.getLot());
                                    stockMove.setAuxIsLotBlocked(lot.getIsBlocked());
                                    stockMove.setAuxLotDateExpiration(lot.getDateExpiration_n());
                                }
                                else {
                                    key = new int[] { year, moProdOrderSource.getFkLotItemId_nr(), moProdOrderSource.getFkLotUnitId_nr(), SDataConstantsSys.TRNX_STK_LOT_DEF_ID, moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() }; // just for more readable code
                                    stockMove = new STrnStockMove(key, stockFinishRow.getQuantityToFinish());
                                }

                                iogEntry.getAuxStockMoves().add(stockMove);

                                moPaneDiogEntries.addTableRow(new SDataDiogEntryRow(iogEntry));

                                if (moPaneDiogEntries.getTableGuiRowCount() == 1) {
                                    updateNoEntriesRelatedControls();    // if needed, inactivate production order controls
                                }

                                moPaneDiogEntries.renderTableRows();
                                moPaneDiogEntries.setTableRowSelection(moPaneDiogEntries.getTableGuiRowCount() - 1);

                                actionEntryClear();
                                computeDocValue();
                            }
                        }
                    }
                    else {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
                    }
                }
            }
        }
    }

    private void importStockWarehouse() {
        int nYear = 0;
        java.util.Date tDate = null;
        SDataItem item = null;
        SDataDiogEntry iogEntry = null;
        Vector<STrnStockMove> stockMoves = null;

        tDate = moFieldDate.getDate();
        nYear = SLibTimeUtilities.digestYear(tDate)[0];

        try {
            stockMoves = STrnUtilities.obtainStockWarehouse(miClient, nYear, tDate, new int[] { moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() });

            for (STrnStockMove stockMove : stockMoves) {
                item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { stockMove.getPkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);

                iogEntry = new SDataDiogEntry();
                iogEntry.setPkYearId(SLibConstants.UNDEFINED);
                iogEntry.setPkDocId(SLibConstants.UNDEFINED);
                iogEntry.setPkEntryId(SLibConstants.UNDEFINED);
                iogEntry.setQuantity(stockMove.getQuantity());
                iogEntry.setValueUnitary(0);
                iogEntry.setValue(0);
                iogEntry.setOriginalQuantity(stockMove.getQuantity());
                iogEntry.setOriginalValueUnitary(0);
                iogEntry.setSortingPosition(0);
                iogEntry.setIsInventoriable(true);
                iogEntry.setIsDeleted(false);
                iogEntry.setFkItemId(item.getPkItemId());
                iogEntry.setFkUnitId(item.getFkUnitId());
                iogEntry.setFkOriginalUnitId(item.getFkUnitId());

                iogEntry.setFkDpsYearId_n(SLibConstants.UNDEFINED);
                iogEntry.setFkDpsDocId_n(SLibConstants.UNDEFINED);
                iogEntry.setFkDpsEntryId_n(SLibConstants.UNDEFINED);
                iogEntry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
                iogEntry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
                iogEntry.setFkDpsAdjustmentEntryId_n(SLibConstants.UNDEFINED);
                iogEntry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
                iogEntry.setFkMfgOrderId_n(SLibConstants.UNDEFINED);
                iogEntry.setFkMfgChargeId_n(SLibConstants.UNDEFINED);

                iogEntry.setFkUserNewId(1);
                iogEntry.setFkUserEditId(1);
                iogEntry.setFkUserDeleteId(1);

                iogEntry.setDbmsItem(item.getItem());
                iogEntry.setDbmsItemKey(item.getKey());
                iogEntry.setDbmsUnit(item.getDbmsDataUnit().getUnit());
                iogEntry.setDbmsUnitSymbol(item.getDbmsDataUnit().getSymbol());
                iogEntry.setDbmsOriginalUnit(item.getDbmsDataUnit().getUnit());
                iogEntry.setDbmsOriginalUnitSymbol(item.getDbmsDataUnit().getSymbol());

                iogEntry.getAuxStockMoves().add(stockMove);

                moPaneDiogEntries.addTableRow(new SDataDiogEntryRow(iogEntry));
                moPaneDiogEntries.renderTableRows();
                moPaneDiogEntries.setTableRowSelection(moPaneDiogEntries.getTableGuiRowCount() - 1);

                if (moPaneDiogEntries.getTableGuiRowCount() == 1) {
                    updateNoEntriesRelatedControls();    // if needed, inactivate production order controls
                }

                actionEntryClear();
                computeDocValue();
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    private void actionDate() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDate.getDate(), moFieldDate);
    }

    private void actionWarehouseClear() {
        renderWarehouseSource(null, false);
        renderWarehouseDestiny(null, false);
    }

    private void actionWarehouseSource() {
        boolean reset = false;

        if (moPaneDiogEntries.getTableGuiRowCount() > 0) {
            miClient.showMsgBoxWarning("No se puede elegir un valor para el campo '" + jlWarehouseSource.getText() + "' porque el documento tiene partidas.");
        }
        else {
            moPickerWarehouseSource.formReset();
            moPickerWarehouseSource.formRefreshCatalogues();
            moPickerWarehouseSource.setIogSettings(manParamIogTypeKey, SLibConstants.MODE_AS_SRC);
            moPickerWarehouseSource.setCompanyBranchEntityKey(moWarehouseSource == null ? null : (int[]) moWarehouseSource.getPrimaryKey());

            if (moWarehouseSource == null) {
                moPickerWarehouseSource.setCompanyBranchKey(new int[] { miClient.getSessionXXX().getCurrentCompanyBranchId() });
            }

            moPickerWarehouseSource.setFormVisible(true);

            if (moPickerWarehouseSource.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                if (moWarehouseDestiny != null) {
                    if (!STrnUtilities.canIogTypeWarehousesBeTheSame(manParamIogTypeKey) && SLibUtilities.compareKeys(moWarehouseDestiny.getPrimaryKey(), moPickerWarehouseSource.getCompanyBranchEntityKey())) {
                        reset = true;
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlWarehouseSource.getText() + "'.\nEl almacén origen no puede ser el mismo que el almacén destino.");
                    }
                }

                if (!reset) {
                    renderWarehouseSource(moPickerWarehouseSource.getCompanyBranchEntityKey(), false);
                    if (!isWarehouseSourceOpen()) {
                        reset = true;   // previous call to isWarehouseSourceOpen() has already shown error user dialogs
                    }
                }

                if (reset) {
                    renderWarehouseSource(null, false);
                }
                else {
                    manLastWarehouseSourceKey = moPickerWarehouseSource.getCompanyBranchEntityKey();
                }
            }
        }
    }

    private void actionWarehouseDestiny() {
        boolean reset = false;

        if (moPaneDiogEntries.getTableGuiRowCount() > 0) {
            miClient.showMsgBoxWarning("No se puede elegir un valor para el campo '" + jlWarehouseDestiny.getText() + "' porque el documento tiene partidas.");
        }
        else {
            moPickerWarehouseDestiny.formReset();
            moPickerWarehouseDestiny.formRefreshCatalogues();
            moPickerWarehouseDestiny.setIogSettings(manParamIogTypeKey, SLibConstants.MODE_AS_DES);
            moPickerWarehouseDestiny.setCompanyBranchEntityKey(moWarehouseDestiny == null ? null : (int[]) moWarehouseDestiny.getPrimaryKey());

            if (moWarehouseDestiny == null && moWarehouseSource != null) {
                moPickerWarehouseDestiny.setCompanyBranchKey(new int[] { moWarehouseSource.getPkCompanyBranchId() });
            }

            moPickerWarehouseDestiny.setFormVisible(true);

            if (moPickerWarehouseDestiny.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                if (moWarehouseSource != null) {
                    if (!STrnUtilities.canIogTypeWarehousesBeTheSame(manParamIogTypeKey) && SLibUtilities.compareKeys(moWarehouseSource.getPrimaryKey(), moPickerWarehouseDestiny.getCompanyBranchEntityKey())) {
                        reset = true;
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlWarehouseDestiny.getText() + "'.\nEl almacén destino no puede ser el mismo que el almacén origen.");
                    }
                }

                if (!reset) {
                    renderWarehouseDestiny(moPickerWarehouseDestiny.getCompanyBranchEntityKey(), false);
                    if (!isWarehouseDestinyOpen()) {
                        reset = true;   // previous call to isWarehouseDestinyOpen() has already shown error user dialogs
                    }
                }

                if (reset) {
                    renderWarehouseDestiny(null, false);
                }
                else {
                    manLastWarehouseDestinyKey = moPickerWarehouseDestiny.getCompanyBranchEntityKey();
                }
            }
        }
    }

    private void actionWarehouseExchange() {
        int[] keySource = moWarehouseSource == null ? null : (int[]) moWarehouseSource.getPrimaryKey();
        int[] keyDestiny = moWarehouseDestiny == null ? null : (int[]) moWarehouseDestiny.getPrimaryKey();

        renderWarehouseSource(keyDestiny, false);
        renderWarehouseDestiny(keySource, false);
    }

    private void actionEntryTextToFind() {
        STrnItemFound itemFound = STrnUtilities.computeItemFound(miClient, SLibUtilities.textTrim(jtfEntryTextToFind.getText()));

        if (itemFound.getItemFoundIds() == null) {
            miClient.showMsgBoxWarning("No se encontró ningún ítem con clave '" + itemFound.getItemKey() + "'.");
        }
        else if (itemFound.getItemFoundIds().length > 1) {
            miClient.showMsgBoxWarning("Se encontraron " + itemFound.getItemFoundIds().length + " ítems con clave '" + itemFound.getItemKey() + "'.");
        }
        else {
            renderItemEntry(new int[] { itemFound.getItemFoundIds()[0] }, itemFound.getQuantity());
            jtfEntryQuantity.requestFocus();
        }
    }

    private void actionEntryFind() {
        SFormOptionPickerItems picker = (SFormOptionPickerItems) miClient.getOptionPicker(SDataConstants.ITMX_ITEM_IOG);

        picker.formReset();
        picker.setFilterKey(null);
        picker.setFormParam(SLibConstants.VALUE_INV_ONLY, true);
        picker.formRefreshOptionPane();
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            renderItemEntry((int[]) picker.getSelectedPrimaryKey(), SLibUtilities.parseDouble(jtfEntryTextToFind.getText()));
            jtfEntryQuantity.requestFocus();
        }
    }

    public void actionDecimals() {
        String toolTipText = !jtbDecimals.isSelected() ? TXT_DEC_INC : TXT_DEC_DEC;
        DefaultTableCellRenderer tcr = !jtbDecimals.isSelected() ?
            miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity() :
            miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary();

        moFieldEntryQuantity.setDecimalFormat((!jtbDecimals.isSelected() ?
            miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat() :
            miClient.getSessionXXX().getFormatters().getDecimalsValueUnitaryFormat()));
        moFieldEntryQuantity.setFieldValue(moFieldEntryQuantity.getDouble());

        moPaneDiogEntries.getTableColumn(mnColQty).setCellRenderer(tcr);
        jtbDecimals.setToolTipText(toolTipText);

        moPaneDiogEntries.createTable();
        moPaneDiogEntries.renderTableRows();
        moPaneDiogEntries.setTableRowSelection(0);
        computeDocValue();
    }

    private void actionEntryAdd() {
        int year = (moFieldDate.getDate() != null ? SLibTimeUtilities.digestYear(moFieldDate.getDate())[0] : 0);
        int mode = STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey) ? SLibConstants.MODE_QTY_EXT : jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        boolean add = true;
        String msg = "";
        SDataDiogEntry iogEntry = null;
        SDialogStockLots dialogStockLots = STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey) ? moDialogStockLotsProdOrder : moDialogStockLots;

        if (jbEntryAdd.isEnabled()) {
            if (moWarehouseSource == null) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseSource.getText() + "'.");
                jbWarehouseSource.requestFocus();
            }
            else if (mbWarehouseDestinyNeeded && moWarehouseDestiny == null) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseDestiny.getText() + "'.");
                jbWarehouseDestiny.requestFocus();
            }
            else if (STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey) && jcbProdOrderSource.getSelectedIndex() <= 0) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlProdOrderSource.getText() + "'.");
                jcbProdOrderSource.requestFocus();
            }
            else if (moEntryItem == null) {
                miClient.showMsgBoxWarning("No se ha definido un ítem para la partida del documento.");
                jtfEntryTextToFind.requestFocus();
            }
            else if (moEntryItem.getIsDeleted()) {
                miClient.showMsgBoxWarning(SItmConsts.MSG_ERR_ITEM_DEL);
                jtfEntryTextToFind.requestFocus();
            }
            else if (!moEntryItem.getIsInventoriable()) {
                miClient.showMsgBoxWarning(SItmConsts.MSG_ERR_ITEM_INV_NOT);
                jtfEntryTextToFind.requestFocus();
            }
            else if (moEntryItem.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_INA) {
                miClient.showMsgBoxWarning(SItmConsts.MSG_ERR_ST_ITEM_INA);
                jtfEntryTextToFind.requestFocus();
            }
            else if (moEntryItem.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_RES && mnParamIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN) {
                miClient.showMsgBoxWarning(SItmConsts.MSG_ERR_ST_ITEM_RES);
                jtfEntryTextToFind.requestFocus();
            }
            else if (moFieldEntryQuantity.getDouble() == 0d) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlEntryQuantity.getText() + "'.");
                jtfEntryQuantity.requestFocus();
            }
            else if (moFieldEntryValue.getDouble() == 0d && miClient.showMsgBoxConfirm("¿Está seguro que desea agregar al documento una partida sin valor?") != JOptionPane.YES_OPTION) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlEntryValueUnitary.getText() + "'.");
                jtfEntryValueUnitary.requestFocus();
            }
            else {
                msg = validateAppropriateWarehouses();

                if (msg.length() > 0) {
                    miClient.showMsgBoxWarning(msg);
                }
                else {
                    if (validateAppropriateWarehousesItem(moEntryItem.getPkItemId())) {
                        if (moEntryItem.getIsLotApplying()) {
                            if (moStockMoveEntry == null) {
                                dialogStockLots.formReset();
                                dialogStockLots.setFormParams(mnParamIogCategoryId, year, moEntryItem.getPkItemId(), moEntryItem.getFkUnitId(),
                                        (int[]) moWarehouseSource.getPrimaryKey(), (int[]) (moDiog == null ? null : moDiog.getPrimaryKey()),
                                        moFieldEntryQuantity.getDouble(), mnFormStatus, mode, moParamDpsSource, moFieldDate.getDate());
                                dialogStockLots.setVisible(true);

                                if (dialogStockLots.getFormResult() != SLibConstants.FORM_RESULT_OK) {
                                    add = false;
                                }
                            }
                        }

                        if (add) {
                            iogEntry = new SDataDiogEntry();
                            iogEntry.setPkYearId(SLibConstants.UNDEFINED);
                            iogEntry.setPkDocId(SLibConstants.UNDEFINED);
                            iogEntry.setPkEntryId(SLibConstants.UNDEFINED);
                            iogEntry.setQuantity(moFieldEntryQuantity.getDouble());
                            iogEntry.setValueUnitary(moFieldEntryValueUnitary.getDouble());
                            iogEntry.setValue(moFieldEntryValue.getDouble());
                            iogEntry.setOriginalQuantity(moFieldEntryQuantity.getDouble());
                            iogEntry.setOriginalValueUnitary(moFieldEntryValueUnitary.getDouble());
                            iogEntry.setSortingPosition(0);
                            iogEntry.setIsInventoriable(moEntryItem.getIsInventoriable());
                            iogEntry.setIsDeleted(false);
                            iogEntry.setFkItemId(moEntryItem.getPkItemId());
                            iogEntry.setFkUnitId(moEntryItem.getFkUnitId());
                            iogEntry.setFkOriginalUnitId(moEntryItem.getFkUnitId());

                            iogEntry.setFkDpsYearId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkDpsDocId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkDpsEntryId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkDpsAdjustmentYearId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkDpsAdjustmentDocId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkDpsAdjustmentEntryId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkMfgYearId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkMfgOrderId_n(SLibConstants.UNDEFINED);
                            iogEntry.setFkMfgChargeId_n(SLibConstants.UNDEFINED);

                            iogEntry.setFkUserNewId(1);
                            iogEntry.setFkUserEditId(1);
                            iogEntry.setFkUserDeleteId(1);

                            iogEntry.setDbmsItem(moEntryItem.getItem());
                            iogEntry.setDbmsItemKey(moEntryItem.getKey());
                            iogEntry.setDbmsUnit(moEntryItem.getDbmsDataUnit().getUnit());
                            iogEntry.setDbmsUnitSymbol(moEntryItem.getDbmsDataUnit().getSymbol());
                            iogEntry.setDbmsOriginalUnit(moEntryItem.getDbmsDataUnit().getUnit());
                            iogEntry.setDbmsOriginalUnitSymbol(moEntryItem.getDbmsDataUnit().getSymbol());

                            if (moEntryItem.getIsLotApplying()) {
                                if (moStockMoveEntry == null) {
                                    iogEntry.getAuxStockMoves().addAll(dialogStockLots.getStockMoves());
                                }
                                else {
                                    moStockMoveEntry.setQuantity(iogEntry.getQuantity());
                                    iogEntry.getAuxStockMoves().add(moStockMoveEntry);
                                }
                            }
                            else {
                                iogEntry.getAuxStockMoves().add(new STrnStockMove(new int[] { year, iogEntry.getFkItemId(), iogEntry.getFkUnitId(), SDataConstantsSys.TRNX_STK_LOT_DEF_ID, moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() }, iogEntry.getQuantity()));
                            }

                            moPaneDiogEntries.addTableRow(new SDataDiogEntryRow(iogEntry));
                            moPaneDiogEntries.renderTableRows();
                            moPaneDiogEntries.setTableRowSelection(moPaneDiogEntries.getTableGuiRowCount() - 1);

                            if (moPaneDiogEntries.getTableGuiRowCount() == 1) {
                                updateNoEntriesRelatedControls();    // if needed, inactivate production order controls
                            }

                            actionEntryClear();
                            computeDocValue();
                        }
                    }
                    else {
                        miClient.showMsgBoxInformation("El ítem '" + moEntryItem.getKey() + " - " + moEntryItem.getItem() + "'\n no está configurado para el almacén '" +
                                   (moWarehouseDestiny == null ? moWarehouseSource.getEntity() : moWarehouseDestiny.getEntity()) + "', código '" + (moWarehouseDestiny == null ? moWarehouseSource.getCode() : moWarehouseDestiny.getCode()) + "'.");
                    }
                }
            }
        }
    }

    private void actionEntryClear() {
        moEntryItem = null;
        moStockMoveEntry = null;
        jtfEntryTextToFind.setText("");
        jtfEntryItem.setText("");
        jtfEntryItemCode.setText("");
        jtfEntryUnitSymbol.setText("");
        moFieldEntryQuantity.resetField();
        moFieldEntryValueUnitary.resetField();
        moFieldEntryValue.resetField();

        jbEntryPickLot.setEnabled(false);

        jtfEntryTextToFind.requestFocus();
    }

    private void actionEntryPickLot() {
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];

        if (moWarehouseSource == null) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseSource.getText() + "'.");
                jbWarehouseSource.requestFocus();
        }
        else if (mbWarehouseDestinyNeeded && moWarehouseDestiny == null) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlWarehouseDestiny.getText() + "'.");
            jbWarehouseDestiny.requestFocus();
        }
        else {
            moPickerStockLots.formReset();
            moPickerStockLots.setFormParams(year, moEntryItem.getPkItemId(), moEntryItem.getFkUnitId(),
                    (int[]) moWarehouseSource.getPrimaryKey(), (int[]) (moDiog == null ? null : moDiog.getPrimaryKey()));
            moPickerStockLots.setVisible(true);

            if (moPickerStockLots.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                moStockMoveEntry = moPickerStockLots.getSelectecStockMove();

                moFieldEntryQuantity.setFieldValue(moStockMoveEntry.getQuantity());
                moFieldEntryValueUnitary.setFieldValue(moStockMoveEntry.getQuantity() == 0d ? 0d : (moStockMoveEntry.getValue() / moStockMoveEntry.getQuantity()));
                moFieldEntryValue.setFieldValue(moStockMoveEntry.getValue());

                jbEntryAdd.requestFocus();   // on focus lost unitary value will be computed
            }
        }
    }

    private void actionEntryDelete() {
        int index = 0;
        SDataDiogEntry iogEntry = null;

        if (jbEntryDelete.isEnabled()) {
            index = moPaneDiogEntries.getTable().getSelectedRow();
            if (index != -1) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    iogEntry = (SDataDiogEntry) moPaneDiogEntries.getTableRow(index).getData();

                    if (iogEntry.hasDiogLinksShipment() || SDataUtilities.callProcedureVal(miClient, SProcConstants.TRN_DIOG_ETY_COUNT_SHIP, iogEntry.getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT) > 0) {
                        miClient.showMsgBoxWarning("No se puede eliminar: ¡La partida está asociada a un documento de embarques!");
                    }
                    else {
                        iogEntry = (SDataDiogEntry) moPaneDiogEntries.removeTableRow(index).getData();
                        moPaneDiogEntries.renderTableRows();

                        if (!iogEntry.getIsRegistryNew()) {
                            iogEntry.setIsDeleted(true);
                            iogEntry.setIsRegistryEdited(true);
                            mvDeletedDiogEntries.add(iogEntry);
                        }

                        if (moPaneDiogEntries.getTableGuiRowCount() == 0) {
                            updateNoEntriesRelatedControls();    // if needed, activate production order controls
                        }
                        else {
                            moPaneDiogEntries.setTableRowSelection(index < moPaneDiogEntries.getTableGuiRowCount() ? index : moPaneDiogEntries.getTableGuiRowCount() - 1);
                        }

                        computeDocValue();
                    }
                }
            }
        }
    }

    private void actionEntryImport() {
        if (jbEntryImport.isEnabled()) {
            if (moParamDpsSource != null) {
                importFromDps();
            }
            else if (moProdOrderSource != null) {
                importFromProdOrder();
            }
            else if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_ADJ_INV)) {
                if (miClient.showMsgBoxConfirm("El almacén '" + moWarehouseSource.getEntity() + "', código '" + moWarehouseSource.getCode() + "' quedará sin existencias al '" + miClient.getSessionXXX().getFormatters().getDateFormat().format(moFieldDate.getDate()) + "'.\n ¿Desea continuar?") == JOptionPane.YES_OPTION) {
                    try {
                        if (STrnUtilities.canStockWarehouseBeCancel(miClient, moFieldDate.getDate(), new int[] { moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() })) {
                            importStockWarehouse();
                            jbEntryImport.setEnabled(moPaneDiogEntries.getTableGuiRowCount() == 0);
                        }
                        else {
                            miClient.showMsgBoxWarning("No se pueden cancelar las existencias en el almacén '" + moWarehouseSource.getEntity() + "', código '" + moWarehouseSource.getCode() + "' debido a que existen movimientos de salida de inventario\n con fecha igual o posterior al '" + miClient.getSessionXXX().getFormatters().getDateFormat().format(moFieldDate.getDate()) + "'.");
                            jftDate.requestFocus();
                        }
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
            }
            else {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }
        }
    }

    private void actionEntryViewLots() {
        boolean canEdit = false;
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];
        int index = 0;
        int mode = STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey) ? SLibConstants.MODE_QTY_EXT : jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        SDataDiogEntry entry = null;
        SDialogStockLots dialogStockLots = STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey) ? moDialogStockLotsProdOrder : moDialogStockLots;

        if (jbEntryViewLots.isEnabled()) {
            index = moPaneDiogEntries.getTable().getSelectedRow();
            if (index != -1) {
                entry = (SDataDiogEntry) moPaneDiogEntries.getSelectedTableRow().getData();

                if (entry.getAuxStockMoves().size() == 0 || (entry.getAuxStockMoves().size() == 1 && entry.getAuxStockMoves().get(0).getPkLotId() == SDataConstantsSys.TRNX_STK_LOT_DEF_ID)) {
                    miClient.showMsgBoxInformation("La partida seleccionada no tiene lotes.");
                }
                else {
                    try {
                        if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT) {
                            canEdit = canEditEntry(entry);
                        }

                        dialogStockLots.formReset();
                        dialogStockLots.setFormParams(mnParamIogCategoryId, year, entry.getFkItemId(), entry.getFkUnitId(),
                                (int[]) moWarehouseSource.getPrimaryKey(), (int[]) (moDiog == null ? null : moDiog.getPrimaryKey()),
                                entry.getQuantity(), (canEdit ? mnFormStatus : SLibConstants.FORM_STATUS_READ_ONLY), mode, moParamDpsSource, moFieldDate.getDate());
                        dialogStockLots.setStockMoves(entry.getAuxStockMoves());
                        dialogStockLots.setVisible(true);

                        if (dialogStockLots.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                            entry.getAuxStockMoves().clear();
                            entry.getAuxStockMoves().addAll(dialogStockLots.getStockMoves());

                            moPaneDiogEntries.getSelectedTableRow().prepareTableRow();
                            moPaneDiogEntries.renderTableRows();
                            moPaneDiogEntries.setTableRowSelection(index);
                        }
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
            }
        }
    }

    private void  actionSwitchProdOrderDestiny() {
        mbResetingForm = true;

        SFormUtilities.populateComboBox(miClient, jcbProdOrderDestiny, SDataConstants.MFGX_ORD,
                (manParamProdOrderSourceKey == null || jtbSwitchProdOrderDestiny.isSelected()) ?
                    new Object[] { STrnUtilities.getProdOrderActiveStatus() } :
                    new Object[] { STrnUtilities.getProdOrderActiveStatus(), manParamProdOrderSourceKey });

        itemStateChangedProdOrderDestiny();

        jcbProdOrderDestiny.requestFocus();

        mbResetingForm = false;
    }

    private void itemStateChangedProdOrderSource() {
        boolean enable = false;

        if (jcbProdOrderSource.getSelectedIndex() <= 0) {
            moProdOrderSource = null;
            moItemProdOrderSource = null;
            moUnitProdOrderSource = null;

            jtfItemSource.setText("");
            jtfItemSourceCode.setText("");
            jtfQuantitySource.setText("");
            jtfUnitSymbolSource.setText("");
        }
        else {
            moProdOrderSource = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, moFieldProdOrderSource.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
            moItemProdOrderSource = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { moProdOrderSource.getFkItemId_r() }, SLibConstants.EXEC_MODE_VERBOSE);
            moUnitProdOrderSource = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { moProdOrderSource.getFkUnitId_r() }, SLibConstants.EXEC_MODE_VERBOSE);

            jtfItemSource.setText(moItemProdOrderSource.getItem());
            jtfItemSourceCode.setText(moItemProdOrderSource.getKey());
            jtfQuantitySource.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(moProdOrderSource.getQuantity()));
            jtfUnitSymbolSource.setText(moUnitProdOrderSource.getSymbol());

            jtfItemSource.setCaretPosition(0);
            jtfItemSourceCode.setCaretPosition(0);
            jtfQuantitySource.setCaretPosition(0);
            jtfUnitSymbolSource.setCaretPosition(0);
        }

        jbEntryImport.setEnabled(mnFormStatus == SLibConstants.FORM_STATUS_EDIT && moParamDpsSource != null || moProdOrderSource != null || SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_ADJ_INV));
        jcbDiogAdjustmentType.setEnabled(mnFormStatus == SLibConstants.FORM_STATUS_EDIT && STrnUtilities.isIogTypeAdjustment(manParamIogTypeKey));

        if (!STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey) || jcbProdOrderSource.getSelectedIndex() <= 0) {
            jcbProdOrderDestiny.setEnabled(false);
            jbProdOrderDestiny.setEnabled(false);
            jtbSwitchProdOrderDestiny.setEnabled(false);

            jcbProdOrderDestiny.removeAllItems();
        }
        else {
            enable = mnFormStatus == SLibConstants.FORM_STATUS_EDIT && moPaneDiogEntries.getTable().getRowCount() == 0;

            jcbProdOrderDestiny.setEnabled(enable);
            jbProdOrderDestiny.setEnabled(enable);
            jtbSwitchProdOrderDestiny.setEnabled(enable);

            SFormUtilities.populateComboBox(miClient, jcbProdOrderDestiny, SDataConstants.MFGX_ORD,
                    jtbSwitchProdOrderDestiny.isSelected() ?
                        new Object[] { STrnUtilities.getProdOrderActiveStatus() } :
                        new Object[] { STrnUtilities.getProdOrderActiveStatus(), moProdOrderSource.getPrimaryKey() });
        }
    }

    private void itemStateChangedProdOrderDestiny() {
        if (jcbProdOrderDestiny.getSelectedIndex() <= 0) {
            moProdOrderDestiny = null;
            moItemProdOrderDestiny = null;
            moUnitProdOrderDestiny = null;

            jtfItemDestiny.setText("");
            jtfItemDestinyCode.setText("");
            jtfQuantityDestiny.setText("");
            jtfUnitSymbolDestiny.setText("");
        }
        else {
            moProdOrderDestiny = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, moFieldProdOrderDestiny.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
            moItemProdOrderDestiny = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { moProdOrderDestiny.getFkItemId_r() }, SLibConstants.EXEC_MODE_VERBOSE);
            moUnitProdOrderDestiny = (SDataUnit) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_UNIT, new int[] { moProdOrderDestiny.getFkUnitId_r() }, SLibConstants.EXEC_MODE_VERBOSE);

            jtfItemDestiny.setText(moItemProdOrderDestiny.getItem());
            jtfItemDestinyCode.setText(moItemProdOrderDestiny.getKey());
            jtfQuantityDestiny.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(moProdOrderDestiny.getQuantity()));
            jtfUnitSymbolDestiny.setText(moUnitProdOrderDestiny.getSymbol());

            jtfItemDestiny.setCaretPosition(0);
            jtfItemDestinyCode.setCaretPosition(0);
            jtfQuantityDestiny.setCaretPosition(0);
            jtfUnitSymbolDestiny.setCaretPosition(0);
        }
    }

    private void actionProdOrderSource() {
        miClient.pickOption(SDataConstants.MFGX_ORD, moFieldProdOrderSource,
                new Object[] { STrnUtilities.getProdOrderActiveStatus() });
    }

    private void actionProdOrderDestiny() {
        miClient.pickOption(SDataConstants.MFGX_ORD, moFieldProdOrderDestiny,
                jtbSwitchProdOrderDestiny.isSelected() ?
                    new Object[] { STrnUtilities.getProdOrderActiveStatus() } :
                    new Object[] { STrnUtilities.getProdOrderActiveStatus(), moFieldProdOrderSource.getKey() });
    }

    public void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
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

    public void actionCancel() {
        boolean close = true;

        if (mbCanShowForm && mnFormStatus == SLibConstants.FORM_STATUS_EDIT && (moDiog == null || (moDiog != null && !moDiog.getIsSystem()))) {
            close = miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_FORM_CLOSE) == JOptionPane.YES_OPTION;
        }

        if (close) {
            mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
            setVisible(false);
        }
    }

    public void actionEdit() {
        mnFormStatus = SLibConstants.FORM_STATUS_EDIT;

        jbEdit.setEnabled(false);

        jbOk.setEnabled(true);
        jbCancel.setEnabled(true);

        setUpControls();

        jftDate.requestFocus();
    }

    public void actionEditHelp() {
        String help = getNonEditableHelp();

        miClient.showMsgBoxInformation(help.length() == 0 ? "No fué posible determinar por qué el documento es de sólo lectura." : help);
    }

    public void doubleClickPaneDiogEntries() {
        actionEntryViewLots();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbEdit;
    private javax.swing.JButton jbEditHelp;
    private javax.swing.JButton jbEntryAdd;
    private javax.swing.JButton jbEntryClear;
    private javax.swing.JButton jbEntryDelete;
    private javax.swing.JButton jbEntryFind;
    private javax.swing.JButton jbEntryImport;
    private javax.swing.JButton jbEntryPickLot;
    private javax.swing.JButton jbEntryViewLots;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbProdOrderDestiny;
    private javax.swing.JButton jbProdOrderSource;
    private javax.swing.JButton jbWarehouseClear;
    private javax.swing.JButton jbWarehouseDestiny;
    private javax.swing.JButton jbWarehouseExchange;
    private javax.swing.JButton jbWarehouseSource;
    private javax.swing.JComboBox<SFormComponentItem> jcbDiogAdjustmentType;
    private javax.swing.JComboBox<SFormComponentItem> jcbProdOrderDestiny;
    private javax.swing.JComboBox<SFormComponentItem> jcbProdOrderSource;
    private javax.swing.JComboBox<SFormComponentItem> jcbSeries;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsShipmentRequired;
    private javax.swing.JCheckBox jckIsSystem;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDiogType;
    private javax.swing.JLabel jlDummy01;
    private javax.swing.JLabel jlEntryDummy01;
    private javax.swing.JLabel jlEntryDummy02;
    private javax.swing.JLabel jlEntryQuantity;
    private javax.swing.JLabel jlEntryValue;
    private javax.swing.JLabel jlEntryValueUnitary;
    private javax.swing.JLabel jlItemDestiny;
    private javax.swing.JLabel jlItemSource;
    private javax.swing.JLabel jlNotes;
    private javax.swing.JLabel jlProdOrderDestiny;
    private javax.swing.JLabel jlProdOrderSource;
    private javax.swing.JLabel jlSeries;
    private javax.swing.JLabel jlValue;
    private javax.swing.JLabel jlWarehouseDestiny;
    private javax.swing.JLabel jlWarehouseSource;
    private javax.swing.JPanel jpCommands1;
    private javax.swing.JPanel jpCommands2;
    private javax.swing.JPanel jpDiogEntries;
    private javax.swing.JPanel jpDocDate;
    private javax.swing.JPanel jpDocNumber;
    private javax.swing.JPanel jpDocType;
    private javax.swing.JPanel jpProductionOrderDestiny;
    private javax.swing.JPanel jpProductionOrderDestinyItem;
    private javax.swing.JPanel jpWarehouseDestiny;
    private javax.swing.JPanel jpWarehouseSource;
    private javax.swing.JToggleButton jtbDecimals;
    private javax.swing.JToggleButton jtbSwitchProdOrderDestiny;
    private javax.swing.JTextField jtfCompanyBranchDestiny;
    private javax.swing.JTextField jtfCompanyBranchDestinyCode;
    private javax.swing.JTextField jtfCompanyBranchSource;
    private javax.swing.JTextField jtfCompanyBranchSourceCode;
    private javax.swing.JTextField jtfDiogType;
    private javax.swing.JTextField jtfDpsSourceBizPartner;
    private javax.swing.JTextField jtfDpsSourceNumber;
    private javax.swing.JTextField jtfEntryItem;
    private javax.swing.JTextField jtfEntryItemCode;
    private javax.swing.JTextField jtfEntryQuantity;
    private javax.swing.JTextField jtfEntryTextToFind;
    private javax.swing.JTextField jtfEntryUnitSymbol;
    private javax.swing.JTextField jtfEntryValue;
    private javax.swing.JTextField jtfEntryValueUnitary;
    private javax.swing.JTextField jtfItemDestiny;
    private javax.swing.JTextField jtfItemDestinyCode;
    private javax.swing.JTextField jtfItemSource;
    private javax.swing.JTextField jtfItemSourceCode;
    private javax.swing.JTextField jtfNotes;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfQuantityDestiny;
    private javax.swing.JTextField jtfQuantitySource;
    private javax.swing.JTextField jtfUnitSymbolDestiny;
    private javax.swing.JTextField jtfUnitSymbolSource;
    private javax.swing.JTextField jtfValue;
    private javax.swing.JTextField jtfWarehouseDestiny;
    private javax.swing.JTextField jtfWarehouseDestinyCode;
    private javax.swing.JTextField jtfWarehouseSource;
    private javax.swing.JTextField jtfWarehouseSourceCode;
    // End of variables declaration//GEN-END:variables

    public void setParamIogTypeKey(final int[] iogTypeKey) {
        int[] key = null;

        mbResetingForm = true;

        manParamIogTypeKey = iogTypeKey;
        manParamIogClassKey = new int[] { iogTypeKey[0], iogTypeKey[1] };
        mnParamIogCategoryId = iogTypeKey[0];
        mbWarehouseDestinyNeeded = STrnUtilities.needsIogTypeWarehouseDestiny(manParamIogTypeKey);

        manParamProdOrderSourceKey = null;
        manParamProdOrderDestinyKey = null;
        mbSwitchProdOrderDestinySelected = false;

        if (moDiog == null) {
            key = miClient.getSessionXXX().getCurrentCompanyBranchEntityKey(SDataConstantsSys.CFGS_CT_ENT_WH);
            renderWarehouseSource(key != null ? key : manLastWarehouseSourceKey, true);
            renderWarehouseDestiny(!mbWarehouseDestinyNeeded ? null : manLastWarehouseDestinyKey, true);
        }

        if (STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey)) {
            SFormUtilities.populateComboBox(miClient, jcbProdOrderSource, SDataConstants.MFGX_ORD,
                    new Object[] { STrnUtilities.getProdOrderActiveStatus() });
        }
        else {
            jcbProdOrderSource.removeAllItems();
            jcbProdOrderDestiny.removeAllItems();
        }

        jlWarehouseDestiny.setEnabled(mbWarehouseDestinyNeeded);

        jlProdOrderSource.setEnabled(STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey));
        jlItemSource.setEnabled(jlProdOrderSource.isEnabled());

        jlProdOrderDestiny.setEnabled(STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey));
        jlItemDestiny.setEnabled(jlProdOrderDestiny.isEnabled());

        jcbProdOrderSource.setEnabled(jlProdOrderSource.isEnabled());
        jbProdOrderSource.setEnabled(jlProdOrderSource.isEnabled());

        itemStateChangedProdOrderSource();
        itemStateChangedProdOrderDestiny();

        mbResetingForm = false;
    }

    public void setParamDpsSource(final SDataDps dpsSource) {
        moParamDpsSource = dpsSource;

        if (moParamDpsSource == null) {
            jbEntryImport.setEnabled(false);

            jtfDpsSourceNumber.setText("");
            jtfDpsSourceBizPartner.setText("");
        }
        else {
            jbEntryImport.setEnabled(true);

            jtfDpsSourceNumber.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.TRNU_TP_DPS, moParamDpsSource.getDpsTypeKey(), SLibConstants.DESCRIPTION_CODE) + " " +
                    (moParamDpsSource.getNumberSeries().length() == 0 ? "" : moParamDpsSource.getNumberSeries() + "-") + moParamDpsSource.getNumber());
            jtfDpsSourceBizPartner.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSU_BP, new int[] { moParamDpsSource.getFkBizPartnerId_r() }));

            jtfDpsSourceNumber.setCaretPosition(0);
            jtfDpsSourceBizPartner.setCaretPosition(0);
        }

        jtfDpsSourceNumber.setToolTipText(jtfDpsSourceNumber.getText().length() == 0 ? null : jtfDpsSourceNumber.getText());
        jtfDpsSourceBizPartner.setToolTipText(jtfDpsSourceBizPartner.getText().length() == 0 ? null : jtfDpsSourceBizPartner.getText());
    }

    public void setParamProdOrderSource(final int[] key) {
        manParamProdOrderSourceKey = key;
    }

    public void setParamProdOrderDestiny(final int[] key) {
        manParamProdOrderDestinyKey = key;
    }

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.FORM_STATUS_EDIT;
        mbFirstTime = true;

        moDiog = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        // Initialize form fields:

        moFieldDate.setDate(miClient.getSessionXXX().getWorkingDate());
        jtfDiogType.setText("");
        jtfNumber.setText("");

        jckIsShipmentRequired.setSelected(false);

        // Enable/disable form fields:

        jcbSeries.removeAllItems();
        jcbSeries.setEditable(false);

        jbOk.setEnabled(true);
        jbCancel.setEnabled(true);

        jbEdit.setEnabled(false);
        jbEditHelp.setEnabled(false);

        jtbDecimals.setSelected(false);
        jtbDecimals.setToolTipText(TXT_DEC_INC);

        // Initialize form document fields:

        mvDeletedDiogEntries.clear();
        moPaneDiogEntries.clearTableRows();
        actionDecimals();

        actionEntryClear();
        computeDocValue();

        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbDiogAdjustmentType, SDataConstants.TRNU_TP_IOG_ADJ);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];
        String msg = "";
        Vector<Object> params = new Vector<Object>();
        Vector<SDataDiogEntry> entries = new Vector<SDataDiogEntry>();
        SDataItem item = null;
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }

        if (moDiog == null && !validation.getIsError()) {
            params.removeAllElements();

            params.add(SDataConstants.TRN_DIOG);
            params.add(((int []) ((erp.lib.form.SFormComponentItem) jcbSeries.getSelectedItem()).getComplement())[0]);
            params.add(((int []) ((erp.lib.form.SFormComponentItem) jcbSeries.getSelectedItem()).getComplement())[1]);
            params.add(jcbSeries.getSelectedItem().toString());
            params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DIOG_NUM_SER_VAL,  params, SLibConstants.EXEC_MODE_SILENT);

            if (params.size() > 0) {
                if (SLibUtilities.parseInt(params.get(1).toString()) > 0) {
                    validation.setMessage(params.get(2).toString());
                    validation.setComponent(jcbSeries);
                }
            }
        }

        if (!validation.getIsError()) {
            if (!SDataUtilities.isPeriodOpen(miClient, moFieldDate.getDate())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                validation.setComponent(jftDate);
            }
            else if (moDiog != null && moDiog.getPkYearId() != year) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_YEAR);
                validation.setComponent(jftDate);
            }
            else if (jcbDiogAdjustmentType.isEnabled() && moFieldDiogAdjustmentType.getKeyAsIntArray()[0] == SDataConstantsSys.TRNU_TP_IOG_ADJ_NA) {
                validation.setMessage("Se debe especificar un valor diferente para el campo '" + jcbDiogAdjustmentType.getToolTipText() + "'.");
                validation.setComponent(jcbDiogAdjustmentType);
            }
            else if (moPaneDiogEntries.getTableGuiRowCount() == 0) {
                validation.setMessage("El documento debe tener al menos una partida.");
            }
            else if (!isWarehouseSourceOpen()) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlWarehouseSource.getText() + "'.");
            }
            else if (mbWarehouseDestinyNeeded && !isWarehouseDestinyOpen()) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlWarehouseDestiny.getText() + "'.");
            }
            else if (mbWarehouseDestinyNeeded && !STrnUtilities.canIogTypeWarehousesBeTheSame(manParamIogTypeKey) && SLibUtilities.compareKeys(moWarehouseSource.getPrimaryKey(), moWarehouseDestiny.getPrimaryKey())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlWarehouseDestiny.getText() + "'.\nEl almacén origen y el almacén destino deben ser distintos.");
            }
            else if (STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey) && jcbProdOrderSource.getSelectedIndex() <= 0) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlProdOrderSource.getText() + "'.");
                validation.setComponent(jcbProdOrderSource);
            }
            else if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey) && jcbProdOrderDestiny.getSelectedIndex() <= 0) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlProdOrderDestiny.getText() + "'.");
                validation.setComponent(jcbProdOrderDestiny);
            }
            else if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey) && SLibUtilities.compareKeys(moFieldProdOrderSource.getKeyAsIntArray(), moFieldProdOrderDestiny.getKeyAsIntArray())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlProdOrderDestiny.getText() + "'.\nLa orden de producción origen y la orden de producción destino deben ser distintas.");
                validation.setComponent(jcbProdOrderDestiny);
            }
            else {
                msg = validateAppropriateWarehouses();

                if (msg.length() > 0) {
                    validation.setMessage(msg);
                }

                if (!validation.getIsError()) {
                    try {
                        entries.addAll(getCompleteEntries());
                        
                        // Validate that item is not restricted on in moves or inactive:
                        
                        for (SDataDiogEntry entry : entries) {
                            item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { entry.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);
                            
                            if (item.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_INA) {
                                throw new Exception(SItmConsts.MSG_ERR_ST_ITEM_INA + "\n" + 
                                        SGuiConsts.TXT_LBL_CODE + ": " + item.getKey() + "\n" +
                                        SGuiConsts.TXT_LBL_NAME + ": " + item.getItem());
                            }
                            else if (item.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_RES && mnParamIogCategoryId == SModSysConsts.TRNS_CT_IOG_IN) {
                                throw new Exception(SItmConsts.MSG_ERR_ST_ITEM_RES + "\n" + 
                                        SGuiConsts.TXT_LBL_CODE + ": " + item.getKey() + "\n" +
                                        SGuiConsts.TXT_LBL_NAME + ": " + item.getItem());
                            }
                        }
                        
                        // Validate item lots:

                        msg = STrnStockValidator.validateStockLots(miClient, entries, mnParamIogCategoryId, false);
                        if (msg.length() > 0) {
                            throw new Exception(msg);
                        }

                        if (!SLibUtilities.belongsTo(manParamIogTypeKey, new int[][] { SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD })) {
                            int [] reference = null;
                            int segregationType = 0;
                            
                            if (moProdOrderSource != null) {
                                reference = new int[] { moProdOrderSource.getPkOrdId(), moProdOrderSource.getPkYearId() };
                                segregationType = SDataConstantsSys.TRNS_TP_STK_SEG_MFG_ORD;
                            }
                            msg = STrnStockValidator.validateStockMoves(miClient, entries, mnParamIogCategoryId, moDiog == null ? new int[] { year, 0 } : (int[]) moDiog.getPrimaryKey(), (int[]) moWarehouseSource.getPrimaryKey(), false, moFieldDate.getDate(), reference, segregationType);
                            if (msg.length() > 0) {
                                throw new Exception(msg);
                            }
                        }
                    }
                    catch (Exception e) {
                        validation.setMessage(e.toString());
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
        mnFormStatus = SLibConstants.FORM_STATUS_READ_ONLY;

        moDiog = (SDataDiog) registry;
        setParamIogTypeKey(moDiog.getDiogTypeKey());

        // Form controls:

        jcbSeries.setEditable(true);
        jbOk.setEnabled(false);
        jbCancel.setEnabled(true);

        if (isRegistryEditable()) {
            jbEdit.setEnabled(true);
            jbEditHelp.setEnabled(false);
        }
        else {
            jbEdit.setEnabled(false);
            jbEditHelp.setEnabled(true);
        }

        // Form fields:

        moFieldDate.setFieldValue(moDiog.getDate());
        moFieldSeries.setFieldValue(moDiog.getNumberSeries());
        jckIsShipmentRequired.setSelected(moDiog.getIsShipmentRequired());
        jtfNumber.setText(moDiog.getNumber());
        jckIsSystem.setSelected(moDiog.getIsSystem());
        jckIsDeleted.setSelected(moDiog.getIsDeleted());

        moFieldDiogAdjustmentType.setFieldValue(new int[] { moDiog.getFkDiogAdjustmentTypeId() });
        renderWarehouseSource(moDiog.getWarehouseKey(), true);
        renderWarehouseDestiny(moDiog.getDbmsDataCounterpartDiog() == null ? null : moDiog.getDbmsDataCounterpartDiog().getWarehouseKey(), true);

        setParamDpsSource((SDataDps) (moDiog.getLinkedDpsKey_n() == null ? null : SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moDiog.getLinkedDpsKey_n(), SLibConstants.EXEC_MODE_VERBOSE)));

        if (STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey)) {
            if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET)) {
                // Work in progress return moves exchange warehouses:

                setParamProdOrderSource(moDiog.getDbmsDataCounterpartDiog() == null ? null : moDiog.getDbmsDataCounterpartDiog().getProdOrderKey_n());
                setParamProdOrderDestiny(moDiog.getProdOrderKey_n());
            }
            else {
                // Other moves use warehouses as they were defined:

                setParamProdOrderSource(moDiog.getProdOrderKey_n());

                if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey)) {
                    setParamProdOrderDestiny(moDiog.getDbmsDataCounterpartDiog() == null ? null : moDiog.getDbmsDataCounterpartDiog().getProdOrderKey_n());
                }
            }
        }

        // Document entries:

        for (SDataDiogEntry entry : moDiog.getDbmsEntries()) {
            if (!entry.getIsDeleted()) {
                moPaneDiogEntries.addTableRow(new SDataDiogEntryRow(entry));
            }
        }
        moPaneDiogEntries.renderTableRows();
        moPaneDiogEntries.setTableRowSelection(0);
        computeDocValue();

        // Document notes:

        if (moDiog.getDbmsNotes().size() > 0) {
            moFieldNotes.setString(moDiog.getDbmsNotes().get(0).getNotes());
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];
        int[] iogTypeKey = null;
        SDataDiog iogCounterpart = null;
        SDataDiogNotes iogNote = null;
        Vector<SFormComponentItem> items = null;

        if (moDiog == null) {
            moDiog = new SDataDiog();
            moDiog.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

            moDiog.setPkYearId(year);
            moDiog.setPkDocId(0);
            moDiog.setNumberSeries(jcbSeries.getSelectedItem().toString());
            moDiog.setNumber("");   // number will be set in store procedure

            moDiog.setFkDiogCategoryId(manParamIogTypeKey[0]);
            moDiog.setFkDiogClassId(manParamIogTypeKey[1]);
            moDiog.setFkDiogTypeId(manParamIogTypeKey[2]);
        }
        else {
            moDiog.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moDiog.setDate(moFieldDate.getDate());
        moDiog.setIsShipmentRequired(jckIsShipmentRequired.isSelected());

        computeDocValue();
        moDiog.setValue_r(SLibUtilities.parseDouble(jtfValue.getText()));

        /*
        moDiog.setCostAsigned(...
        moDiog.setCostTransferred(...
        moDiog.setIsShipmentRequired(...
        moDiog.setIsShipped(...
        moDiog.setIsAudited(...
        moDiog.setIsAuthorized(...
        moDiog.setIsRecordAutomatic(...
        moDiog.setIsSystem(...
        moDiog.setIsDeleted(...
        */

        moDiog.setFkDiogAdjustmentTypeId(moFieldDiogAdjustmentType.getKeyAsIntArray()[0]);
        moDiog.setFkCompanyBranchId(moWarehouseSource.getPkCompanyBranchId());
        moDiog.setFkWarehouseId(moWarehouseSource.getPkEntityId());

        if (moParamDpsSource == null) {
            moDiog.setFkDpsYearId_n(SLibConstants.UNDEFINED);
            moDiog.setFkDpsDocId_n(SLibConstants.UNDEFINED);
        }
        else {
            moDiog.setFkDpsYearId_n(moParamDpsSource.getPkYearId());
            moDiog.setFkDpsDocId_n(moParamDpsSource.getPkDocId());
        }

        /*
        moDiog.setFkDiogYearId_n(SLibConstants.UNDEFINED);
        moDiog.setFkDiogDocId_n(SLibConstants.UNDEFINED);
        */

        if (STrnUtilities.isIogTypeForProdOrder(manParamIogTypeKey)) {
            try {
                if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET)) {
                    // Work in progress return moves exchange warehouses:
                    
                    moDiog.setFkMfgYearId_n(moProdOrderDestiny.getPkYearId());
                    moDiog.setFkMfgOrderId_n(moProdOrderDestiny.getPkOrdId());
                }
                else {
                    // Other moves use warehouses as they were defined:
                    
                    moDiog.setFkMfgYearId_n(moProdOrderSource.getPkYearId());
                    moDiog.setFkMfgOrderId_n(moProdOrderSource.getPkOrdId());
                }
                
                moDiog.setAuxSegregationStockId(STrnStockSegregationUtils.getStockSegregationIdByReference(miClient.getSession(), new int [] { moProdOrderSource.getPkYearId(), moProdOrderSource.getPkOrdId()}, SDataConstantsSys.TRNS_TP_STK_SEG_MFG_ORD));
            } catch (Exception ex) {
                SLibUtils.printException(this, ex);
            }
        }

        /*
        moDiog.setFkBookkeepingYearId_n(...
        moDiog.setFkBookkeepingNumberId_n(...
        */

        moDiog.setFkUserShippedId(1);       // "non-applicable" value
        moDiog.setFkUserAuditedId(1);       // "non-applicable" value
        moDiog.setFkUserAuthorizedId(1);    // "non-applicable" value

        // Document entries:

        moDiog.getDbmsEntries().clear();
        moDiog.getDbmsEntries().addAll(getCompleteEntries());

        // Document notes:

        if (moDiog.getDbmsNotes().size() > 0) {
            iogNote = moDiog.getDbmsNotes().get(0);
        }

        moDiog.getDbmsNotes().clear();
        if (moFieldNotes.getString().length() == 0) {
            if (iogNote != null) {
                iogNote.setIsDeleted(true);
                iogNote.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            }
        }
        else {
            if (iogNote == null) {
                iogNote = new SDataDiogNotes();
                iogNote.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            }
            else {
                iogNote.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            }

            iogNote.setNotes(moFieldNotes.getString());
            iogNote.setIsPrintable(true);
        }

        if (iogNote != null) {
            iogNote.setIsRegistryEdited(true);
            moDiog.getDbmsNotes().add(iogNote);
        }

        if (mbWarehouseDestinyNeeded) {
            try {
                iogTypeKey = STrnUtilities.getIogTypeCounterpart(manParamIogTypeKey);

                iogCounterpart = moDiog.clone();
                iogCounterpart.setIsSystem(true);
                iogCounterpart.setFkDiogCategoryId(iogTypeKey[0]);
                iogCounterpart.setFkDiogClassId(iogTypeKey[1]);
                iogCounterpart.setFkDiogTypeId(iogTypeKey[2]);
                iogCounterpart.setFkDiogAdjustmentTypeId(moFieldDiogAdjustmentType.getKeyAsIntArray()[0]);
                iogCounterpart.setFkCompanyBranchId(moWarehouseDestiny.getPkCompanyBranchId());
                iogCounterpart.setFkWarehouseId(moWarehouseDestiny.getPkEntityId());
                iogCounterpart.setFkDpsYearId_n(SLibConstants.UNDEFINED);
                iogCounterpart.setFkDpsDocId_n(SLibConstants.UNDEFINED);
                iogCounterpart.setFkDiogYearId_n(SLibConstants.UNDEFINED);
                iogCounterpart.setFkDiogDocId_n(SLibConstants.UNDEFINED);

                if (STrnUtilities.needsIogTypeProdOrderDestiny(manParamIogTypeKey)) {
                    if (SLibUtilities.compareKeys(manParamIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET)) {
                        // Work in progress return moves exchange warehouses:

                        iogCounterpart.setFkMfgYearId_n(moProdOrderSource.getPkYearId());
                        iogCounterpart.setFkMfgOrderId_n(moProdOrderSource.getPkOrdId());
                    }
                    else {
                        // Other moves use warehouses as they were defined:

                        iogCounterpart.setFkMfgYearId_n(moProdOrderDestiny.getPkYearId());
                        iogCounterpart.setFkMfgOrderId_n(moProdOrderDestiny.getPkOrdId());
                    }
                }

                if (moDiog.getDbmsDataCounterpartDiog() == null) {
                    items = moWarehouseDestiny.getDnsForDiog(new int[] { iogTypeKey[0], iogTypeKey[1] });

                    iogCounterpart.setPkYearId(moDiog.getPkYearId());
                    iogCounterpart.setPkDocId(0);
                    iogCounterpart.setNumberSeries(items.size() == 0 ? moDiog.getNumberSeries() : items.get(0).getItem());
                    iogCounterpart.setNumber("");   // number will be set in store procedure
                }
                else {
                    iogCounterpart.setPkYearId(moDiog.getDbmsDataCounterpartDiog().getPkYearId());
                    iogCounterpart.setPkDocId(moDiog.getDbmsDataCounterpartDiog().getPkDocId());
                    iogCounterpart.setNumberSeries(moDiog.getDbmsDataCounterpartDiog().getNumberSeries());
                    iogCounterpart.setNumber(moDiog.getDbmsDataCounterpartDiog().getNumber());

                    for (SDataDiogEntry entry : iogCounterpart.getDbmsEntries()) {
                        entry.setPkYearId(moDiog.getDbmsDataCounterpartDiog().getPkYearId());
                        entry.setPkDocId(moDiog.getDbmsDataCounterpartDiog().getPkDocId());
                    }

                    for (SDataDiogEntry entryOriginal : moDiog.getDbmsDataCounterpartDiog().getDbmsEntries()) {
                        for (SDataDiogEntry entryNew : iogCounterpart.getDbmsEntries()) {
                            if (SLibUtilities.compareKeys(entryOriginal.getPrimaryKey(), entryNew.getPrimaryKey())) {
                                entryNew.setFkMfgYearId_n(entryOriginal.getFkMfgYearId_n());
                                entryNew.setFkMfgOrderId_n(entryOriginal.getFkMfgOrderId_n());
                                entryNew.setFkMfgChargeId_n(entryOriginal.getFkMfgChargeId_n());

                                entryNew.setClonedFkMfgYearId_n(entryOriginal.getClonedFkMfgYearId_n());
                                entryNew.setClonedFkMfgOrderId_n(entryOriginal.getClonedFkMfgOrderId_n());
                                entryNew.setClonedFkMfgChargeId_n(entryOriginal.getClonedFkMfgChargeId_n());
                                break;
                            }
                        }
                    }
                }

                iogCounterpart.setDbmsDataCounterpartDiog(null);
                iogCounterpart.setAuxIsDbmsDataCounterpartDiog(true);

                moDiog.setDbmsDataCounterpartDiog(iogCounterpart);
            }
            catch (CloneNotSupportedException e) {
                SLibUtilities.renderException(this, e);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }

        return moDiog;
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
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbEdit) {
                actionEdit();
            }
            else if (button == jbEditHelp) {
                actionEditHelp();
            }
            else if (button == jbDate) {
                actionDate();
            }
            else if (button == jbWarehouseClear) {
                actionWarehouseClear();
            }
            else if (button == jbWarehouseSource) {
                actionWarehouseSource();
            }
            else if (button == jbWarehouseDestiny) {
                actionWarehouseDestiny();
            }
            else if (button == jbWarehouseExchange) {
                actionWarehouseExchange();
            }
            else if (button == jbProdOrderSource) {
                actionProdOrderSource();
            }
            else if (button == jbProdOrderDestiny) {
                actionProdOrderDestiny();
            }
            else if (button == jbEntryFind) {
                actionEntryFind();
            }
            else if (button == jbEntryAdd) {
                actionEntryAdd();
            }
            else if (button == jbEntryClear) {
                actionEntryClear();
            }
            else if (button == jbEntryPickLot) {
                actionEntryPickLot();
            }
            else if (button == jbEntryDelete) {
                actionEntryDelete();
            }
            else if (button == jbEntryImport) {
                actionEntryImport();
            }
            else if (button == jbEntryViewLots) {
                actionEntryViewLots();
            }
        }
        else if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfEntryTextToFind) {
                actionEntryTextToFind();
            }
        }
        else if (e.getSource() instanceof JToggleButton) {
            if (!mbResetingForm) {
                JToggleButton toggleButton = (JToggleButton) e.getSource();

                if (toggleButton == jtbSwitchProdOrderDestiny) {
                    actionSwitchProdOrderDestiny();
                }
                else if (toggleButton == jtbDecimals) {
                    actionDecimals();
                }
            }
        }
    }

    @Override
    public void focusGained(java.awt.event.FocusEvent e) {

    }

    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfEntryQuantity) {
                computeEntryValue();
            }
            else if (textField == jtfEntryValueUnitary) {
                computeEntryValue();
            }
            else if (textField == jtfEntryValue) {
                computeEntryValueUnitary();
                computeEntryValue();
            }
        }
    }
}
