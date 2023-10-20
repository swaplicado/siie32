/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormMaintDiog.java
 *
 * Created on 23/10/2009, 08:48:14 AM
 */

package erp.mtrn.form;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
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
import erp.mcfg.data.SDataCompanyBranchEntity;
import erp.mitm.data.SDataItem;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.itm.db.SItmConsts;
import erp.mod.trn.db.SDbMaintConfig;
import erp.mod.trn.db.SDbMaintDiogSignature;
import erp.mod.trn.db.SDbMaintUser;
import erp.mod.trn.db.SDbMaintUserSupervisor;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogEntry;
import erp.mtrn.data.SDataDiogEtyMatConsEntCostCenter;
import erp.mtrn.data.SDataDiogMaintMovementEntryRow;
import erp.mtrn.data.SDataDiogNotes;
import erp.mtrn.data.STrnItemFound;
import erp.mtrn.data.STrnMaintConstants;
import erp.mtrn.data.STrnMaintUtilities;
import erp.mtrn.data.STrnStockMove;
import erp.mtrn.data.STrnStockValidator;
import erp.mtrn.data.STrnUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;


/**
 *
 * @author Gil De Jesús, Sergio Flores, Claudio Peña, Edwin Carmona
 */
public class SFormMaintDiog extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.FocusListener {
    
    public static final String SERIES_ADJ_IN = "MAE";
    public static final String SERIES_ADJ_OUT = "MAS";
    public static final String SERIES_TRA_IN = "MTE";
    public static final String SERIES_TRA_OUT = "MTS";
    public static final Color COLOR_SIGNED = new Color(255, 255, 102);
    public static final Color COLOR_NONSIGNED = new Color(102, 255, 102);
    
    private int mnFormType;
    
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    protected int mnTabTypeAux01;
    private boolean mbResetingForm;
    
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataDiog moDiog;
    private erp.mod.trn.db.SDbMaintDiogSignature moMaintDiogSignature;
    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldMaintUser;
    private erp.lib.form.SFormField moFieldMaintUserSupervisor;
    private erp.lib.form.SFormField moFieldNotes;
    private erp.lib.form.SFormField moFieldMaintReturnUser;
    private erp.lib.form.SFormField moFieldMaintReturnUserSupervisor;
    private erp.lib.form.SFormField moFieldEntryMaintArea;
    private erp.lib.form.SFormField moFieldEntryConsEntity;
    private erp.lib.form.SFormField moFieldEntrySubConsEntity;
    private erp.lib.form.SFormField moFieldEntryCostCenter;
    private erp.lib.form.SFormField moFieldEntryQuantity;
    private erp.lib.form.SFormField moFieldEntryValueUnit;
    private erp.lib.form.SFormField moFieldEntryValue;
   
    private java.util.Vector<erp.mtrn.data.SDataDiogEntry> mvDeletedDiogEntries;
    private erp.lib.table.STablePane moPaneDiogEntries;

    private int mnParamMaintMovementType;
    private int mnParamMaintUserType;
    private int mnMaintMovementIogCategory;
    private int mnIogCategoryId;
    private int[] manIogTypeKey;
    private boolean mbMaintAreaNeeded;
    private boolean mbWarehouseDestinyNeeded;
    private String msSeriesIogCounterpart;

    private erp.mcfg.data.SDataCompanyBranchEntity moWarehouseSource;
    private erp.mcfg.data.SDataCompanyBranchEntity moWarehouseDestiny;
    private erp.mitm.data.SDataItem moEntryItem;
    
    /**
     * Creates new form SFormMaintDiog.
     * @param client GUI client.
     */
    public SFormMaintDiog(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;
        mnFormType = SDataConstants.TRNX_MAINT_DIOG;

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
        jpMaintMovementType = new javax.swing.JPanel();
        jlMaintMovementType = new javax.swing.JLabel();
        jtfMaintMovementType = new javax.swing.JTextField();
        jpDocDate = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jckIsSystem = new javax.swing.JCheckBox();
        jckIsDeleted = new javax.swing.JCheckBox();
        jpDocNumber = new javax.swing.JPanel();
        jlNumber = new javax.swing.JLabel();
        jtfSeries = new javax.swing.JTextField();
        jtfNumber = new javax.swing.JTextField();
        jpMaintUser = new javax.swing.JPanel();
        jlMaintUser = new javax.swing.JLabel();
        jcbMaintUser = new javax.swing.JComboBox<SFormComponentItem>();
        jpMaintUserSupervisor = new javax.swing.JPanel();
        jlMaintUserSupervisor = new javax.swing.JLabel();
        jcbMaintUserSupervisor = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel22 = new javax.swing.JPanel();
        jpPanel1 = new javax.swing.JPanel();
        jlNotes = new javax.swing.JLabel();
        jtfNotes = new javax.swing.JTextField();
        jpPanel2 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jlMaintReturnUser = new javax.swing.JLabel();
        jcbMaintReturnUser = new javax.swing.JComboBox<SFormComponentItem>();
        jbCopyMaintUser = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jlMaintReturnUserSupervisor = new javax.swing.JLabel();
        jcbMaintReturnUserSupervisor = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jtfEntryTextToFind = new javax.swing.JTextField();
        jbEntryFind = new javax.swing.JButton();
        jlEntryDummy01 = new javax.swing.JLabel();
        jlEntryMaintArea = new javax.swing.JLabel();
        jlEntryQuantity = new javax.swing.JLabel();
        jlEntryValueUnit = new javax.swing.JLabel();
        jlEntryValue = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jtfEntryItemCode = new javax.swing.JTextField();
        jtfEntryItem = new javax.swing.JTextField();
        jcbEntryMaintArea = new javax.swing.JComboBox();
        jtfEntryQuantity = new javax.swing.JTextField();
        jtfEntryUnitSymbol = new javax.swing.JTextField();
        jtfEntryValueUnit = new javax.swing.JTextField();
        jtfEntryValue = new javax.swing.JTextField();
        jbEntryAdd = new javax.swing.JButton();
        jbEntryClear = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jbEntryDelete = new javax.swing.JButton();
        jLEntryConsEntity = new javax.swing.JLabel();
        jcbEntryConsEntity = new javax.swing.JComboBox<String>();
        jLEntrySubConsEntity = new javax.swing.JLabel();
        jcbEntrySubConsEntity = new javax.swing.JComboBox<String>();
        jLEntryCostCenter = new javax.swing.JLabel();
        jcbEntryCostCenter = new javax.swing.JComboBox<String>();
        jpDiogEntries = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlDocQuantity = new javax.swing.JLabel();
        jtfDocQuantity = new javax.swing.JTextField();
        jlDocValue = new javax.swing.JLabel();
        jtfDocValue = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jpCommands1 = new javax.swing.JPanel();
        jbSign = new javax.swing.JButton();
        jtfSignatureStatus = new javax.swing.JTextField();
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

        jPanel2.setLayout(new java.awt.BorderLayout(0, 6));

        jPanel21.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del documento:"));
        jPanel8.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jpMaintMovementType.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMaintMovementType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlMaintMovementType.setText("Movimiento:");
        jlMaintMovementType.setPreferredSize(new java.awt.Dimension(100, 23));
        jpMaintMovementType.add(jlMaintMovementType);

        jtfMaintMovementType.setEditable(false);
        jtfMaintMovementType.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfMaintMovementType.setText("MOVEMENT TYPE");
        jtfMaintMovementType.setFocusable(false);
        jtfMaintMovementType.setPreferredSize(new java.awt.Dimension(350, 23));
        jpMaintMovementType.add(jtfMaintMovementType);

        jPanel8.add(jpMaintMovementType);

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

        jLabel1.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocDate.add(jLabel1);

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

        jlNumber.setText("Serie y folio:");
        jlNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocNumber.add(jlNumber);

        jtfSeries.setEditable(false);
        jtfSeries.setText("TEXT");
        jtfSeries.setFocusable(false);
        jtfSeries.setPreferredSize(new java.awt.Dimension(50, 23));
        jpDocNumber.add(jtfSeries);

        jtfNumber.setEditable(false);
        jtfNumber.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNumber.setText("0");
        jtfNumber.setFocusable(false);
        jtfNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocNumber.add(jtfNumber);

        jPanel8.add(jpDocNumber);

        jpMaintUser.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMaintUser.setText("Responsable: *");
        jlMaintUser.setPreferredSize(new java.awt.Dimension(100, 23));
        jpMaintUser.add(jlMaintUser);

        jcbMaintUser.setMaximumRowCount(16);
        jcbMaintUser.setPreferredSize(new java.awt.Dimension(350, 23));
        jpMaintUser.add(jcbMaintUser);

        jPanel8.add(jpMaintUser);

        jpMaintUserSupervisor.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMaintUserSupervisor.setText("Residente:");
        jlMaintUserSupervisor.setPreferredSize(new java.awt.Dimension(100, 23));
        jpMaintUserSupervisor.add(jlMaintUserSupervisor);

        jcbMaintUserSupervisor.setMaximumRowCount(16);
        jcbMaintUserSupervisor.setPreferredSize(new java.awt.Dimension(350, 23));
        jpMaintUserSupervisor.add(jcbMaintUserSupervisor);

        jPanel8.add(jpMaintUserSupervisor);

        jPanel21.add(jPanel8, java.awt.BorderLayout.WEST);
        jPanel8.getAccessibleContext().setAccessibleName("Asignaciones:");

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos adicionales:"));
        jPanel22.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jpPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNotes.setText("Comentarios:");
        jlNotes.setPreferredSize(new java.awt.Dimension(100, 23));
        jpPanel1.add(jlNotes);

        jtfNotes.setText("TEXT");
        jtfNotes.setPreferredSize(new java.awt.Dimension(350, 23));
        jpPanel1.add(jtfNotes);

        jPanel22.add(jpPanel1);

        jpPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel22.add(jpPanel2);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel22.add(jPanel27);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMaintReturnUser.setText("Responsable rec.: *");
        jlMaintReturnUser.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlMaintReturnUser);

        jcbMaintReturnUser.setMaximumRowCount(16);
        jcbMaintReturnUser.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel24.add(jcbMaintReturnUser);

        jbCopyMaintUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_copy.gif"))); // NOI18N
        jbCopyMaintUser.setToolTipText("Copiar responsable");
        jbCopyMaintUser.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jbCopyMaintUser.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel24.add(jbCopyMaintUser);

        jPanel22.add(jPanel24);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMaintReturnUserSupervisor.setText("Residente rec.:");
        jlMaintReturnUserSupervisor.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel25.add(jlMaintReturnUserSupervisor);

        jcbMaintReturnUserSupervisor.setMaximumRowCount(16);
        jcbMaintReturnUserSupervisor.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel25.add(jcbMaintReturnUserSupervisor);

        jPanel22.add(jPanel25);

        jPanel21.add(jPanel22, java.awt.BorderLayout.CENTER);
        jPanel22.getAccessibleContext().setAccessibleName("Devoluciones:");

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

        jlEntryDummy01.setPreferredSize(new java.awt.Dimension(172, 23));
        jPanel7.add(jlEntryDummy01);

        jlEntryMaintArea.setText("Área de mantenimiento:");
        jlEntryMaintArea.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jlEntryMaintArea);

        jlEntryQuantity.setText("Cantidad:");
        jlEntryQuantity.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel7.add(jlEntryQuantity);

        jlEntryValueUnit.setText("Precio unitario:");
        jlEntryValueUnit.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel7.add(jlEntryValueUnit);

        jlEntryValue.setText("Importe:");
        jlEntryValue.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel7.add(jlEntryValue);

        jPanel5.add(jPanel7);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfEntryItemCode.setEditable(false);
        jtfEntryItemCode.setText("TEXT");
        jtfEntryItemCode.setFocusable(false);
        jtfEntryItemCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jtfEntryItemCode);

        jtfEntryItem.setEditable(false);
        jtfEntryItem.setText("TEXT");
        jtfEntryItem.setFocusable(false);
        jtfEntryItem.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel10.add(jtfEntryItem);

        jcbEntryMaintArea.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(jcbEntryMaintArea);

        jtfEntryQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfEntryQuantity.setText("0.00");
        jtfEntryQuantity.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel10.add(jtfEntryQuantity);

        jtfEntryUnitSymbol.setEditable(false);
        jtfEntryUnitSymbol.setText("TEXT");
        jtfEntryUnitSymbol.setFocusable(false);
        jtfEntryUnitSymbol.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel10.add(jtfEntryUnitSymbol);

        jtfEntryValueUnit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfEntryValueUnit.setText("0.00");
        jtfEntryValueUnit.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel10.add(jtfEntryValueUnit);

        jtfEntryValue.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfEntryValue.setText("0.00");
        jtfEntryValue.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel10.add(jtfEntryValue);

        jbEntryAdd.setText("Agregar");
        jbEntryAdd.setToolTipText("Agregar partida (Ctrl + A)");
        jbEntryAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryAdd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jbEntryAdd);

        jbEntryClear.setText("Limpiar");
        jbEntryClear.setToolTipText("Limpiar partida (Ctrl + L)");
        jbEntryClear.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryClear.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jbEntryClear);

        jPanel5.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbEntryDelete.setText("Eliminar");
        jbEntryDelete.setToolTipText("Eliminar partida (Ctrl + E)");
        jbEntryDelete.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbEntryDelete.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jbEntryDelete);

        jLEntryConsEntity.setText("Centro consumo:");
        jLEntryConsEntity.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel11.add(jLEntryConsEntity);

        jcbEntryConsEntity.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jcbEntryConsEntity);

        jLEntrySubConsEntity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLEntrySubConsEntity.setText("Sub centro consumo:");
        jLEntrySubConsEntity.setPreferredSize(new java.awt.Dimension(110, 23));
        jPanel11.add(jLEntrySubConsEntity);

        jcbEntrySubConsEntity.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jcbEntrySubConsEntity);

        jLEntryCostCenter.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLEntryCostCenter.setText("Centro costo:");
        jLEntryCostCenter.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jLEntryCostCenter);

        jcbEntryCostCenter.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel11.add(jcbEntryCostCenter);

        jPanel5.add(jPanel11);

        jPanel4.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jpDiogEntries.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jlDocQuantity.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlDocQuantity.setText("Piezas:");
        jlDocQuantity.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(jlDocQuantity);

        jtfDocQuantity.setEditable(false);
        jtfDocQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocQuantity.setText("0.00");
        jtfDocQuantity.setFocusable(false);
        jtfDocQuantity.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jtfDocQuantity);

        jlDocValue.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jlDocValue.setText("Total:");
        jlDocValue.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(jlDocValue);

        jtfDocValue.setEditable(false);
        jtfDocValue.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDocValue.setText("0.00");
        jtfDocValue.setFocusable(false);
        jtfDocValue.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel3.add(jtfDocValue);

        jpDiogEntries.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.add(jpDiogEntries, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(592, 33));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jpCommands1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jbSign.setText("Firmar"); // NOI18N
        jbSign.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCommands1.add(jbSign);

        jtfSignatureStatus.setEditable(false);
        jtfSignatureStatus.setFocusable(false);
        jtfSignatureStatus.setPreferredSize(new java.awt.Dimension(250, 20));
        jpCommands1.add(jtfSignatureStatus);

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

        setSize(new java.awt.Dimension(1041, 634));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
       windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jtfEntryTextToFindKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfEntryTextToFindKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jbEntryFind.doClick();
        }
    }//GEN-LAST:event_jtfEntryTextToFindKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionCancel();
    }//GEN-LAST:event_formWindowClosing

    private void initComponentsExtra() {
        mnIogCategoryId = SLibConstants.UNDEFINED;
        manIogTypeKey = null;

        mbWarehouseDestinyNeeded = false;

        mvDeletedDiogEntries = new Vector<SDataDiogEntry>();
        moPaneDiogEntries = new STablePane(miClient);
        jpDiogEntries.add(moPaneDiogEntries, BorderLayout.CENTER);

        moFieldDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDate, jlDate);
        moFieldDate.setPickerButton(jbDate);
        moFieldMaintUser = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbMaintUser, jlMaintUser);
        moFieldMaintUserSupervisor = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbMaintUserSupervisor, jlMaintUserSupervisor);
        moFieldNotes = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfNotes, jlNotes);
        moFieldNotes.setLengthMax(255);
        moFieldMaintReturnUser = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbMaintReturnUser, jlMaintReturnUser);
        moFieldMaintReturnUserSupervisor = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbMaintReturnUserSupervisor, jlMaintReturnUserSupervisor);
        moFieldEntryMaintArea = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbEntryMaintArea, jlEntryMaintArea);
        moFieldEntryQuantity = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfEntryQuantity, jlEntryQuantity);
        moFieldEntryQuantity.setDecimalFormat(SLibUtils.getDecimalFormatQuantity());
        moFieldEntryValueUnit = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfEntryValueUnit, jlEntryValueUnit);
        moFieldEntryValueUnit.setDecimalFormat(SLibUtils.getDecimalFormatAmountUnitary());
        moFieldEntryValue = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfEntryValue, jlEntryValue);
        moFieldEntryValue.setDecimalFormat(SLibUtils.getDecimalFormatAmount());
        moFieldEntryConsEntity = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbEntryConsEntity, jLEntryConsEntity);
        moFieldEntrySubConsEntity = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbEntrySubConsEntity, jLEntrySubConsEntity);
        moFieldEntryCostCenter = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbEntryCostCenter, jLEntryCostCenter);
        
        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldDate);
        mvFields.add(moFieldMaintUser);
        mvFields.add(moFieldMaintUserSupervisor);
        mvFields.add(moFieldNotes);
        mvFields.add(moFieldMaintReturnUser);
        mvFields.add(moFieldMaintReturnUserSupervisor);
        mvFields.add(moFieldEntryMaintArea);
        mvFields.add(moFieldEntryQuantity);
        mvFields.add(moFieldEntryValueUnit);
        mvFields.add(moFieldEntryValue);
        mvFields.add(moFieldEntryConsEntity);
        mvFields.add(moFieldEntrySubConsEntity);
        mvFields.add(moFieldEntryCostCenter);

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbEdit.addActionListener(this);
        jbEditHelp.addActionListener(this);
        jbDate.addActionListener(this);
        jbCopyMaintUser.addActionListener(this);
        jbEntryFind.addActionListener(this);
        jbEntryAdd.addActionListener(this);
        jbEntryClear.addActionListener(this);
        jbEntryDelete.addActionListener(this);
        jbSign.addActionListener(this);
        jtfEntryTextToFind.addActionListener(this);
        jcbMaintUser.addItemListener(this);
        jcbMaintReturnUser.addItemListener(this);
        jcbEntryConsEntity.addItemListener(this);
        jcbEntrySubConsEntity.addItemListener(this);
        jtfEntryQuantity.addFocusListener(this);
        jtfEntryValueUnit.addFocusListener(this);
        jtfEntryValue.addFocusListener(this);

        int i = 0;
        STableColumnForm tableColumnsEntry[] = new STableColumnForm[17];
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Ítem", 250);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Área mantenimiento", 150);
        tableColumnsEntry[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Cantidad", STableConstants.WIDTH_QUANTITY);
        tableColumnsEntry[i++].setCellRenderer(SGridUtils.getCellRendererNumberQuantity());
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        tableColumnsEntry[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Precio unitario $", STableConstants.WIDTH_VALUE_UNITARY);
        tableColumnsEntry[i++].setCellRenderer(SGridUtils.getCellRendererNumberAmountUnitary());
        tableColumnsEntry[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Importe $", STableConstants.WIDTH_VALUE_2X);
        tableColumnsEntry[i++].setCellRenderer(SGridUtils.getCellRendererNumberAmount());
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro consumo", 150);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Sub centro consumo", 150);
        tableColumnsEntry[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Centro de costo", 150);
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

        SFormUtilities.createActionMap(rootPane, this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, 0);
        SFormUtilities.createActionMap(rootPane, this, "actionRowAdd", "rowAdd", KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionRowClear", "rowClear", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionRowDelete", "rowDelete", KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (jftDate.isEditable()) {
                jftDate.requestFocus();
            }
            else {
                jbCancel.requestFocus();
            }
        }
    }
    
    private boolean shouldEnableMaintUserSupervisor() {
        return mnParamMaintUserType == SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR &&
                mnMaintMovementIogCategory == SModSysConsts.TRNS_CT_IOG_OUT &&                
                jcbMaintUser.getSelectedIndex() > 0;                
    }

    private boolean shouldEnableMaintReturnUserSupervisor() {
        return mnParamMaintUserType == SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR &&
                mnMaintMovementIogCategory == SModSysConsts.TRNS_CT_IOG_IN &&
                jcbMaintReturnUser.getSelectedIndex() > 0;
    }
    
    private boolean shouldCheckMaintUserStock() {
        return SLibUtils.belongsTo(mnParamMaintMovementType, 
                new int[] {
                    SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT,
                    SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT,
                    SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST });
//                    SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT,
//                    SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT,
//                    SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST });
    }

    private void populateMaintUserSupervisor() {
        jcbMaintUserSupervisor.setEnabled(false);
        jcbMaintUserSupervisor.removeAllItems();
        jcbMaintUser.getSelectedItem();
        
        if (shouldEnableMaintUserSupervisor()) {
            miClient.getSession().populateCatalogue(jcbMaintUserSupervisor, SModConsts.TRN_MAINT_USER_SUPV, SLibConstants.UNDEFINED, new SGuiParams(((SGuiItem) jcbMaintUser.getSelectedItem()).getPrimaryKey()));
            jcbMaintUserSupervisor.setEnabled(true);
        }
        
//        int idBp = ((SGuiItem) jcbMaintUser.getSelectedItem()).getPrimaryKey()[0];
//        int idConsumeEntity = STrnConsumeUtils.getDefaultEntityOfBp(miClient.getSession(), idBp);
//        SGuiUtils.locateItem(jcbEntryConsEntity, new int[] { idConsumeEntity });
    }
    
    private void populateMaintReturnUserSupervisor() {
        jcbMaintReturnUserSupervisor.setEnabled(false);
        jcbMaintReturnUserSupervisor.removeAllItems();
        
        if (shouldEnableMaintReturnUserSupervisor()) {
            miClient.getSession().populateCatalogue(jcbMaintReturnUserSupervisor, SModConsts.TRN_MAINT_USER_SUPV, SLibConstants.UNDEFINED, new SGuiParams(((SGuiItem) jcbMaintReturnUser.getSelectedItem()).getPrimaryKey()));
            jcbMaintReturnUserSupervisor.setEnabled(true);
       }
    }
    
    private void populateSubEntityConsume() {
        jcbEntrySubConsEntity.setEnabled(false);
        jcbEntrySubConsEntity.removeAllItems();
        
        if (((SGuiItem) jcbEntryConsEntity.getSelectedItem()) == null || ((SGuiItem) jcbEntryConsEntity.getSelectedItem()).getPrimaryKey().length == 0) {
            jcbEntrySubConsEntity.setEnabled(false);
        }
        else {
            SGuiParams params = new SGuiParams();
            params.setKey(new int[] { ((SGuiItem) jcbEntryConsEntity.getSelectedItem()).getPrimaryKey()[0] });
            miClient.getSession().populateCatalogue(jcbEntrySubConsEntity, SModConsts.TRN_MAT_CONS_SUBENT, SLibConstants.UNDEFINED, params);

            jcbEntrySubConsEntity.setEnabled(jcbEntrySubConsEntity.getItemCount() > 1);
        }
    }
    
    private void populateCostCenter() {
        jcbEntryCostCenter.setEnabled(false);
        jcbEntryCostCenter.removeAllItems();
        
        if (((SGuiItem) jcbEntrySubConsEntity.getSelectedItem()) == null || ((SGuiItem) jcbEntrySubConsEntity.getSelectedItem()).getPrimaryKey().length == 0) {
            jcbEntryCostCenter.setEnabled(false);
        }
        else {
            SGuiParams params = new SGuiParams();
            params.getParamsMap().put(SModConsts.TRN_MAT_CONS_SUBENT, ((SGuiItem) jcbEntrySubConsEntity.getSelectedItem()).getPrimaryKey());
            miClient.getSession().populateCatalogue(jcbEntryCostCenter, SModConsts.FIN_CC, SModConsts.TRN_DIOG, params);

            jcbEntryCostCenter.setEnabled(jcbEntryCostCenter.getItemCount() > 1);
        }
    }
    
    private Vector<SDataDiogEntry> getAllEntries() {
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
            
            jcbMaintUser.setEnabled(false);
            jcbMaintUserSupervisor.setEnabled(false);
            jtfNotes.setEditable(false);
            jtfNotes.setFocusable(false);
            jcbMaintReturnUser.setEnabled(false);
            jcbMaintReturnUserSupervisor.setEnabled(false);
            jbCopyMaintUser.setEnabled(false);

            jtfEntryTextToFind.setEditable(false);
            jtfEntryTextToFind.setFocusable(false);
            jcbEntryMaintArea.setEnabled(false);
            jcbEntryConsEntity.setEnabled(false);
            jcbEntrySubConsEntity.setEnabled(false);
            jcbEntryCostCenter.setEnabled(false);
            jtfEntryQuantity.setEditable(false);
            jtfEntryQuantity.setFocusable(false);
            jtfEntryValueUnit.setEditable(false);
            jtfEntryValueUnit.setFocusable(false);
            jtfEntryValue.setEditable(false);
            jtfEntryValue.setFocusable(false);

            jbEntryFind.setEnabled(false);
            jbEntryAdd.setEnabled(false);
            jbEntryClear.setEnabled(false);
            jbEntryDelete.setEnabled(false);
            
            jbSign.setEnabled(false);
        }
        else {
            enable = moPaneDiogEntries.getTableGuiRowCount() == 0;
            
            jftDate.setEditable(enable);
            jftDate.setFocusable(enable);
            jbDate.setEnabled(enable);

            jcbMaintUser.setEnabled(enable);
            jcbMaintUserSupervisor.setEnabled(enable && shouldEnableMaintUserSupervisor());
            jtfNotes.setEditable(true);
            jtfNotes.setFocusable(true);
            jcbMaintReturnUser.setEnabled(mnMaintMovementIogCategory == SModSysConsts.TRNS_CT_IOG_IN);
            jcbMaintReturnUserSupervisor.setEnabled(shouldEnableMaintReturnUserSupervisor());
            jbCopyMaintUser.setEnabled(shouldEnableMaintReturnUserSupervisor());

            jtfEntryTextToFind.setEditable(true);
            jtfEntryTextToFind.setFocusable(true);
            jcbEntryMaintArea.setEnabled(mbMaintAreaNeeded);
            jcbEntryConsEntity.setEnabled(isConsumeCenterMov());
            jcbEntrySubConsEntity.setEnabled(isConsumeCenterMov());
            jcbEntryCostCenter.setEnabled(isConsumeCenterMov());
            jtfEntryQuantity.setEditable(true);
            jtfEntryQuantity.setFocusable(true);
            jtfEntryValueUnit.setEditable(mnIogCategoryId == SModSysConsts.TRNS_CT_IOG_IN);
            jtfEntryValueUnit.setFocusable(mnIogCategoryId == SModSysConsts.TRNS_CT_IOG_IN);
            jtfEntryValue.setEditable(mnIogCategoryId == SModSysConsts.TRNS_CT_IOG_IN);
            jtfEntryValue.setFocusable(mnIogCategoryId == SModSysConsts.TRNS_CT_IOG_IN);

            jbEntryFind.setEnabled(true);
            jbEntryAdd.setEnabled(true);
            jbEntryClear.setEnabled(true);
            jbEntryDelete.setEnabled(true);
            
            jbSign.setEnabled(true);
        }
    }

    private boolean isDiogSigned() {
        return moMaintDiogSignature != null && (moMaintDiogSignature.isRegistryNew() || (!moDiog.getIsRegistryNew() && !moMaintDiogSignature.getTsUserInsert().before(moDiog.getUserEditTs())));
    }
    
    private boolean isConsumeCenterMov() {
        return mnParamMaintMovementType == SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_MAT ||
                mnParamMaintMovementType == SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_MAT;
    }
    
    private void showSignatureStatus() {
        if (isDiogSigned()) {
            jtfSignatureStatus.setText(STrnMaintConstants.SIGNED);
            jtfSignatureStatus.setBackground(COLOR_SIGNED);
            jcbMaintReturnUser.setEnabled(false);
            jcbMaintReturnUserSupervisor.setEnabled(false);
        }
        else {
            jtfSignatureStatus.setText(STrnMaintConstants.NONSIGNED);
            jtfSignatureStatus.setBackground(COLOR_NONSIGNED);
        }
        jtfSignatureStatus.setCaretPosition(0);
    }
    
    private void removeSignature() {
        if (moMaintDiogSignature != null) {
            moMaintDiogSignature = null;
            showSignatureStatus();
        }
    }
    
    private byte[] getSignatoryFingerprint() throws SQLException {
        int dataType = SLibConstants.UNDEFINED;
        int[] signatoryKey = null;
        byte[] fingerprint = null;
                
        switch (mnParamMaintMovementType) {
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_TOOL:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_MAT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST:
                if (jcbMaintReturnUserSupervisor.getSelectedIndex() > 0) {
                    dataType = SModConsts.TRN_MAINT_USER_SUPV;
                    signatoryKey = ((SGuiItem) jcbMaintReturnUserSupervisor.getSelectedItem()).getPrimaryKey();
                }
                else if (jcbMaintReturnUser.getSelectedIndex() > 0) {
                    dataType = SModConsts.TRN_MAINT_USER;
                    signatoryKey = ((SGuiItem) jcbMaintReturnUser.getSelectedItem()).getPrimaryKey();
                }
                break;
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_TOOL:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_MAT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST:
                if (jcbMaintUserSupervisor.getSelectedIndex() > 0) {
                    dataType = SModConsts.TRN_MAINT_USER_SUPV;
                    signatoryKey = ((SGuiItem) jcbMaintUserSupervisor.getSelectedItem()).getPrimaryKey();
                }
                else if (jcbMaintUser.getSelectedIndex() > 0) {
                    dataType = SModConsts.TRN_MAINT_USER;
                    signatoryKey = ((SGuiItem) jcbMaintUser.getSelectedItem()).getPrimaryKey();
                }
                break;
            default:
        }
        
        switch (dataType) {
            case SModConsts.TRN_MAINT_USER:
                SDbMaintUser user = (SDbMaintUser) miClient.getSession().readRegistry(dataType, signatoryKey);
                if (user.getFingerprint_n() != null) {
                    fingerprint = user.getFingerprint_n().getBytes(1, (int) user.getFingerprint_n().length());
                }
                break;
            case SModConsts.TRN_MAINT_USER_SUPV:
                SDbMaintUserSupervisor userSupervisor = (SDbMaintUserSupervisor) miClient.getSession().readRegistry(dataType, signatoryKey);
                if (userSupervisor.getFingerprint_n() != null) {
                    fingerprint = userSupervisor.getFingerprint_n().getBytes(1, (int) userSupervisor.getFingerprint_n().length());
                }
                break;
        }

        return fingerprint;
    }
    
    private int getSignatoryFingerPassword() throws SQLException {
        int dataType = SLibConstants.UNDEFINED;
        int[] signatoryKey = null;
        int fingerPassword = 0;
        
        switch (mnParamMaintMovementType) {
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_TOOL:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_MAT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST:
                if (jcbMaintReturnUserSupervisor.getSelectedIndex() > 0) {
                    dataType = SModConsts.TRN_MAINT_USER_SUPV;
                    signatoryKey = ((SGuiItem) jcbMaintReturnUserSupervisor.getSelectedItem()).getPrimaryKey();
                }
                else if (jcbMaintReturnUser.getSelectedIndex() > 0) {
                    dataType = SModConsts.TRN_MAINT_USER;
                    signatoryKey = ((SGuiItem) jcbMaintReturnUser.getSelectedItem()).getPrimaryKey();
                }
                break;
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_TOOL:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_MAT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST:
                if (jcbMaintUserSupervisor.getSelectedIndex() > 0) {
                    dataType = SModConsts.TRN_MAINT_USER_SUPV;
                    signatoryKey = ((SGuiItem) jcbMaintUserSupervisor.getSelectedItem()).getPrimaryKey();
                }
                else if (jcbMaintUser.getSelectedIndex() > 0) {
                    dataType = SModConsts.TRN_MAINT_USER;
                    signatoryKey = ((SGuiItem) jcbMaintUser.getSelectedItem()).getPrimaryKey();
                }
                break;
            default:
        }
        
        switch (dataType) {
            case SModConsts.TRN_MAINT_USER:
                SDbMaintUser user = (SDbMaintUser) miClient.getSession().readRegistry(dataType, signatoryKey);
                fingerPassword = String.valueOf(user.getPinNumber()).length();
                
                if (String.valueOf(user.getPinNumber()).length() == 4) {
                fingerPassword = user.getPinNumber();
                }
                break;
            case SModConsts.TRN_MAINT_USER_SUPV:
                getSignatoryFingerprint();
                break;
        }

        return fingerPassword;
    }

    private void computeEntryValue() {
        moFieldEntryValue.setDouble(SLibUtils.round(moFieldEntryQuantity.getDouble() * moFieldEntryValueUnit.getDouble(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));
    }

    private void computeEntryValueUnitary() {
        if (moFieldEntryValueUnit.getDouble() == 0) {
            moFieldEntryValueUnit.setDouble(moFieldEntryQuantity.getDouble() == 0 ? 0 : moFieldEntryValue.getDouble() / moFieldEntryQuantity.getDouble());
        }
    }

    private void computeDocTotals() {
        double qty = 0;
        double val = 0;

        for (STableRow row : moPaneDiogEntries.getTableModel().getTableRows()) {
            SDataDiogEntry entry = (SDataDiogEntry) row.getData();
            qty = SLibUtils.round(qty + entry.getQuantity(), SLibUtils.getDecimalFormatQuantity().getMaximumFractionDigits());
            val = SLibUtils.round(val + entry.getValue(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        }

        jtfDocQuantity.setText(SLibUtils.getDecimalFormatQuantity().format(qty));
        jtfDocQuantity.setCaretPosition(0);

        jtfDocValue.setText(SLibUtils.getDecimalFormatAmount().format(val));
        jtfDocValue.setCaretPosition(0);
    }

    private void renderItemEntry(int[] keyItem, double quantity) {
        moEntryItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, keyItem, SLibConstants.EXEC_MODE_SILENT);

        jtfEntryItem.setText(moEntryItem.getXtaItemWidthStatus());
        jtfEntryItemCode.setText(moEntryItem.getKey());
        jtfEntryUnitSymbol.setText(moEntryItem.getDbmsDataUnit().getSymbol());
        jtfEntryQuantity.setText(SLibUtils.getDecimalFormatQuantity().format(quantity));

        jtfEntryItem.setCaretPosition(0);
        jtfEntryItemCode.setCaretPosition(0);
        jtfEntryUnitSymbol.setCaretPosition(0);
        jtfEntryQuantity.setCaretPosition(0);
    }

    private void validateWarehouse(final SDataCompanyBranchEntity warehouse, final String warehouseType) throws Exception {
        if (warehouse == null) {
            throw new Exception("El almacén " + warehouseType + " no ha sido definido.");
        }
        else {
            if (!warehouse.getIsActive()) {
                throw new Exception("El almacén " + warehouseType + " '" + warehouse.getEntity() + "' no está activo.");
            }
            else if (mnIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN && !warehouse.getIsActiveIn()) {
                throw new Exception("El almacén " + warehouseType + " '" + warehouse.getEntity() + "' no está activo para movimientos de entradas.");
            }
            else if (mnIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_OUT && !warehouse.getIsActiveOut()) {
                throw new Exception("El almacén " + warehouseType + " '" + warehouse.getEntity() + "' no está activo para movimientos de salidas.");
            }
        }
    }

    private boolean validateAppropriateWarehousesItem(int idItem) {
        boolean bIsAppropriate = true;

        if (mnIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN) {
            try {
                bIsAppropriate = STrnUtilities.getIsAppropiateWarehouseItem(miClient, idItem, (int[]) moWarehouseSource.getPrimaryKey());
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        else if (SLibUtilities.compareKeys(manIogTypeKey, SDataConstantsSys.TRNS_TP_IOG_OUT_INT_TRA)) {
            try {
                bIsAppropriate = STrnUtilities.getIsAppropiateWarehouseItem(miClient, idItem, (int[]) moWarehouseDestiny.getPrimaryKey());
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }

        return bIsAppropriate;
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

        if (mnFormStatus == SLibConstants.FORM_STATUS_EDIT && (moDiog == null || (moDiog != null && !moDiog.getIsSystem()))) {
            close = miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_FORM_CLOSE) == JOptionPane.YES_OPTION;
        }

        if (close) {
            mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
            setVisible(false);
        }
    }

    private void actionEdit() {
        boolean edit = false;
        
        if (edit) {
            setUpControls();
            removeSignature();

            jbEdit.setEnabled(false);
            jbEditHelp.setEnabled(false);
            jbOk.setEnabled(true);

            if (jftDate.isEditable()) {
                jftDate.requestFocus();
            }
            else if (jtfNumber.isEditable()) {
                jtfNumber.requestFocus();
            }
            else {
                jbCancel.requestFocus();
            }
        }
        
        mnFormStatus = SLibConstants.FORM_STATUS_EDIT;
        
        jbEdit.setEnabled(false);

        jbOk.setEnabled(true);
        jbCancel.setEnabled(true);

        setUpControls();
        removeSignature();

        jtfEntryTextToFind.requestFocus();
    }

    private void actionEditHelp() {
        String help = moDiog.getNonEditableHelp();

        miClient.showMsgBoxInformation(help.length() == 0 ? "No fué posible determinar por qué el documento es de sólo lectura." : help);
    }
    
    private void actionDate() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDate.getDate(), moFieldDate);
    }

    private void actionCopyMaintUser() {
        if (jcbMaintUser.getSelectedIndex() > 0 && jcbMaintReturnUser.isEnabled()) {
            SGuiUtils.locateItem(jcbMaintReturnUser, ((SGuiItem) jcbMaintUser.getSelectedItem()).getPrimaryKey());
            
            if (jcbMaintUserSupervisor.getSelectedIndex() > 0 && jcbMaintReturnUserSupervisor.isEnabled()) {
                SGuiUtils.locateItem(jcbMaintReturnUserSupervisor, ((SGuiItem) jcbMaintUserSupervisor.getSelectedItem()).getPrimaryKey());
                
            }
            
            jcbMaintReturnUser.requestFocus();
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
            if (jcbEntryMaintArea.isEnabled()) {
                jcbEntryMaintArea.requestFocus();
            }
            else {
                jtfEntryQuantity.requestFocus();
            }
        }
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

    public void actionEntryAdd() {
        if (jbEntryAdd.isEnabled()) {
            boolean error = false;

            for (int i = 0; i < mvFields.size(); i++) {
                if (!((SFormField) mvFields.get(i)).validateField()) {
                    error = true;
                    ((SFormField) mvFields.get(i)).getComponent().requestFocus();
                    break;
                }
            }

            if (!error) {
                if (moWarehouseSource == null) {
                    miClient.showMsgBoxWarning("No se ha definido el almacén origen.");
                }
                else if (mbWarehouseDestinyNeeded && moWarehouseDestiny == null) {
                    miClient.showMsgBoxWarning("No se ha definido el almacén destino.");
                }
                else if (moEntryItem == null) {
                    miClient.showMsgBoxWarning("No se ha definido un ítem para la partida.");
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
                else if (moEntryItem.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_RES && mnIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN) {
                    miClient.showMsgBoxWarning(SItmConsts.MSG_ERR_ST_ITEM_RES);
                    jtfEntryTextToFind.requestFocus();
                }
                else if (mbMaintAreaNeeded && jcbEntryMaintArea.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlEntryMaintArea.getText() + "'.");
                    jcbEntryMaintArea.requestFocus();
                }
                else if (moFieldEntryQuantity.getDouble() == 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlEntryQuantity.getText() + "'.");
                    jtfEntryQuantity.requestFocus();
                }
                else if (moFieldEntryValueUnit.getDouble() == 0 && mnIogCategoryId == SDataConstantsSys.TRNS_CT_IOG_IN && miClient.showMsgBoxConfirm("¿Está seguro que desea agregar una partida sin valor?") != JOptionPane.YES_OPTION) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlEntryValueUnit.getText() + "'.");
                    jtfEntryValueUnit.requestFocus();
                }
                else if (isConsumeCenterMov() && jcbEntryConsEntity.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jLEntryConsEntity.getText() + "'.");
                    jcbEntryConsEntity.requestFocus();
                }
                else if (isConsumeCenterMov() && jcbEntrySubConsEntity.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jLEntrySubConsEntity.getText() + "'.");
                    jcbEntrySubConsEntity.requestFocus();
                }
                else if (isConsumeCenterMov() && jcbEntryCostCenter.getSelectedIndex() <= 0) {
                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jLEntryCostCenter.getText() + "'.");
                    jcbEntryCostCenter.requestFocus();
                }
                else if (validateAppropriateWarehousesItem(moEntryItem.getPkItemId())) {
                    SDataDiogEntry iogEntry = new SDataDiogEntry();
                    iogEntry.setPkYearId(SLibConstants.UNDEFINED);
                    iogEntry.setPkDocId(SLibConstants.UNDEFINED);
                    iogEntry.setPkEntryId(SLibConstants.UNDEFINED);
                    iogEntry.setQuantity(moFieldEntryQuantity.getDouble());
                    iogEntry.setValueUnitary(moFieldEntryValueUnit.getDouble());
                    iogEntry.setValue(moFieldEntryValue.getDouble());
                    iogEntry.setOriginalQuantity(moFieldEntryQuantity.getDouble());
                    iogEntry.setOriginalValueUnitary(moFieldEntryValueUnit.getDouble());
                    iogEntry.setSortingPosition(0);
                    iogEntry.setIsInventoriable(true);
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
                    iogEntry.setFkMaintAreaId(jcbEntryMaintArea.getSelectedIndex() <= 0 ? SModSysConsts.TRN_MAINT_AREA_NA : ((SGuiItem) jcbEntryMaintArea.getSelectedItem()).getPrimaryKey()[0]);

                    iogEntry.setFkUserNewId(SUtilConsts.USR_NA_ID);
                    iogEntry.setFkUserEditId(SUtilConsts.USR_NA_ID);
                    iogEntry.setFkUserDeleteId(SUtilConsts.USR_NA_ID);

                    iogEntry.setDbmsItem(moEntryItem.getItem());
                    iogEntry.setDbmsItemKey(moEntryItem.getKey());
//                    iogEntry.setDbmsPartNum(moEntryItem.getPartNumber());
                    iogEntry.setDbmsUnit(moEntryItem.getDbmsDataUnit().getUnit());
                    iogEntry.setDbmsUnitSymbol(moEntryItem.getDbmsDataUnit().getSymbol());
                    iogEntry.setDbmsOriginalUnit(moEntryItem.getDbmsDataUnit().getUnit());
                    iogEntry.setDbmsOriginalUnitSymbol(moEntryItem.getDbmsDataUnit().getSymbol());
                    iogEntry.setDbmsMaintArea(jcbEntryMaintArea.getSelectedIndex() <= 0 ? "" : ((SGuiItem) jcbEntryMaintArea.getSelectedItem()).getItem());

                    int year = (moFieldDate.getDate() != null ? SLibTimeUtilities.digestYear(moFieldDate.getDate())[0] : 0);
                    iogEntry.getAuxStockMoves().add(new STrnStockMove(new int[] { year, iogEntry.getFkItemId(), iogEntry.getFkUnitId(), SDataConstantsSys.TRNX_STK_LOT_DEF_ID, moWarehouseSource.getPkCompanyBranchId(), moWarehouseSource.getPkEntityId() }, iogEntry.getQuantity()));
                    
                    if (isConsumeCenterMov()) {
                        SDataDiogEtyMatConsEntCostCenter oCfgCC = new SDataDiogEtyMatConsEntCostCenter();
                        oCfgCC.setPercentage(100d);
                        oCfgCC.setFkDiogYearId(SLibConstants.UNDEFINED);
                        oCfgCC.setFkDiogDocId(SLibConstants.UNDEFINED);
                        oCfgCC.setFkDiogEntryId(SLibConstants.UNDEFINED);
                        oCfgCC.setFkSubentMatConsumptionEntityId(jcbEntryConsEntity.getSelectedIndex() <= 0 ? SLibConstants.UNDEFINED : ((SGuiItem) jcbEntryConsEntity.getSelectedItem()).getPrimaryKey()[0]);
                        oCfgCC.setFkSubentMatConsumptionSubentityId(jcbEntrySubConsEntity.getSelectedIndex() <= 0 ? SLibConstants.UNDEFINED : ((SGuiItem) jcbEntrySubConsEntity.getSelectedItem()).getPrimaryKey()[0]);
                        oCfgCC.setFkCostCenterId(jcbEntryCostCenter.getSelectedIndex() <= 0 ? SLibConstants.UNDEFINED : ((SGuiItem) jcbEntryCostCenter.getSelectedItem()).getPrimaryKey()[0]);
                        oCfgCC.readAuxs(miClient.getSession().getStatement());
                        iogEntry.getAuxDiogEtyMatEntCcsConfigs().add(oCfgCC);
                    }
                    
                    moPaneDiogEntries.addTableRow(new SDataDiogMaintMovementEntryRow(iogEntry));
                    moPaneDiogEntries.renderTableRows();
                    moPaneDiogEntries.setTableRowSelection(moPaneDiogEntries.getTableGuiRowCount() - 1);

                    setUpControls();
                    actionEntryClear();
                    computeDocTotals();
                    removeSignature();
                }
            }
        }
    }

    public void actionEntryClear() {
        moEntryItem = null;
        jtfEntryTextToFind.setText("");
        jtfEntryItem.setText("");
        jtfEntryItemCode.setText("");
        jtfEntryUnitSymbol.setText("");
        moFieldEntryMaintArea.resetField();
        moFieldEntryQuantity.resetField();
        moFieldEntryValueUnit.resetField();
        moFieldEntryValue.resetField();

        jtfEntryTextToFind.requestFocus();
        stateChangedCosumeEntity();
        populateCostCenter();
    }

    public void actionEntryDelete() {
        int index = 0;
        SDataDiogEntry iogEntry = null;

        if (jbEntryDelete.isEnabled()) {
            index = moPaneDiogEntries.getTable().getSelectedRow();
            if (index != -1) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    iogEntry = (SDataDiogEntry) moPaneDiogEntries.removeTableRow(index).getData();
                    
                    moPaneDiogEntries.renderTableRows();

                    if (!iogEntry.getIsRegistryNew()) {
                        iogEntry.setIsDeleted(true);
                        iogEntry.setIsRegistryEdited(true);
                        mvDeletedDiogEntries.add(iogEntry);
                    }

                    if (moPaneDiogEntries.getTableGuiRowCount() == 0) {
                        setUpControls();
                    }
                    else {
                        moPaneDiogEntries.setTableRowSelection(index < moPaneDiogEntries.getTableGuiRowCount() ? index : moPaneDiogEntries.getTableGuiRowCount() - 1);
                    }

                    computeDocTotals();
                    removeSignature();
                }
            }
        }
    }

    private void actionSign() {
        try {                  
            byte[] fingerprint = getSignatoryFingerprint();
            int fingerPassword = getSignatoryFingerPassword();
            
            if (fingerprint == null && fingerPassword == 0) {
                throw new Exception("No hay un firmante seleccionado o el firmante carece de huella digital y contraseña.");
            }
            if (fingerprint != null) {
                if (STrnMaintUtilities.verifyFingerprint(miClient, fingerprint)) {
                    moMaintDiogSignature = new SDbMaintDiogSignature();
                    showSignatureStatus();
                }
            }            
            else if (fingerPassword != 0) {
                if (STrnMaintUtilities.verifyFingerPassword(miClient, fingerPassword)) {
                    moMaintDiogSignature = new SDbMaintDiogSignature();
                    showSignatureStatus();                
                }
            } 
            else {
                throw new Exception("No hay un firmante seleccionado o el firmante carece de huella digital y contraseña.");
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    private void stateChangedMaintUser() {
        populateMaintUserSupervisor();
    }
    
    private void stateChangedReturnMaintUser() {
        populateMaintReturnUserSupervisor();
    }
    
    private void stateChangedCosumeEntity() {
        populateSubEntityConsume();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLEntryConsEntity;
    private javax.swing.JLabel jLEntryCostCenter;
    private javax.swing.JLabel jLEntrySubConsEntity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
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
    private javax.swing.JButton jbCopyMaintUser;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbEdit;
    private javax.swing.JButton jbEditHelp;
    private javax.swing.JButton jbEntryAdd;
    private javax.swing.JButton jbEntryClear;
    private javax.swing.JButton jbEntryDelete;
    private javax.swing.JButton jbEntryFind;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbSign;
    private javax.swing.JComboBox<String> jcbEntryConsEntity;
    private javax.swing.JComboBox<String> jcbEntryCostCenter;
    private javax.swing.JComboBox jcbEntryMaintArea;
    private javax.swing.JComboBox<String> jcbEntrySubConsEntity;
    private javax.swing.JComboBox<SFormComponentItem> jcbMaintReturnUser;
    private javax.swing.JComboBox<SFormComponentItem> jcbMaintReturnUserSupervisor;
    private javax.swing.JComboBox<SFormComponentItem> jcbMaintUser;
    private javax.swing.JComboBox<SFormComponentItem> jcbMaintUserSupervisor;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsSystem;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDocQuantity;
    private javax.swing.JLabel jlDocValue;
    private javax.swing.JLabel jlEntryDummy01;
    private javax.swing.JLabel jlEntryMaintArea;
    private javax.swing.JLabel jlEntryQuantity;
    private javax.swing.JLabel jlEntryValue;
    private javax.swing.JLabel jlEntryValueUnit;
    private javax.swing.JLabel jlMaintMovementType;
    private javax.swing.JLabel jlMaintReturnUser;
    private javax.swing.JLabel jlMaintReturnUserSupervisor;
    private javax.swing.JLabel jlMaintUser;
    private javax.swing.JLabel jlMaintUserSupervisor;
    private javax.swing.JLabel jlNotes;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JPanel jpCommands1;
    private javax.swing.JPanel jpCommands2;
    private javax.swing.JPanel jpDiogEntries;
    private javax.swing.JPanel jpDocDate;
    private javax.swing.JPanel jpDocNumber;
    private javax.swing.JPanel jpMaintMovementType;
    private javax.swing.JPanel jpMaintUser;
    private javax.swing.JPanel jpMaintUserSupervisor;
    private javax.swing.JPanel jpPanel1;
    private javax.swing.JPanel jpPanel2;
    private javax.swing.JTextField jtfDocQuantity;
    private javax.swing.JTextField jtfDocValue;
    private javax.swing.JTextField jtfEntryItem;
    private javax.swing.JTextField jtfEntryItemCode;
    private javax.swing.JTextField jtfEntryQuantity;
    private javax.swing.JTextField jtfEntryTextToFind;
    private javax.swing.JTextField jtfEntryUnitSymbol;
    private javax.swing.JTextField jtfEntryValue;
    private javax.swing.JTextField jtfEntryValueUnit;
    private javax.swing.JTextField jtfMaintMovementType;
    private javax.swing.JTextField jtfNotes;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfSeries;
    private javax.swing.JTextField jtfSignatureStatus;
    // End of variables declaration//GEN-END:variables

    /**
     * Sets parameters of type of maintenance movement and maintenance user.
     * Must be called before <code>formReset()</code>;
     * @param maintMovementType Type of maintenance movement. Constants in <code>SModSysConsts.TRNS_TP_MAINT_MOV_...</code>
     * @param maintUserType Type of maintenance user. Constants in <code>SModSysConsts.TXT_TRNX_TP_MAINT_USER_...</code>
     * @throws Exception 
     */
    public void setParamMaintMovementSettings(final int maintMovementType, final int maintUserType) throws Exception {
        mbResetingForm = true;
        
        mnParamMaintMovementType = maintMovementType;
        mnParamMaintUserType = maintUserType;
        mnMaintMovementIogCategory = SLibConstants.UNDEFINED;
        manIogTypeKey = null;
        mbMaintAreaNeeded = false;
        mbWarehouseDestinyNeeded = false;
        msSeriesIogCounterpart = "";
        
        moWarehouseSource = null;
        moWarehouseDestiny = null;
        
        // show movement type:
        
        jtfMaintMovementType.setText((String) miClient.getSession().readField(SModConsts.TRNS_TP_MAINT_MOV, new int[] { mnParamMaintMovementType }, SDbRegistry.FIELD_NAME));
        jtfMaintMovementType.setCaretPosition(0);
        
        // validate and configure maintenance movement type:
        
        SDbMaintConfig maintConfig = (SDbMaintConfig) miClient.getSession().readRegistry(SModConsts.TRN_MAINT_CFG, new int[] { 1 });
        
        int[] warehouseSourceKey = null;
        int[] warehouseDestinyKey = null;
        
        switch (mnParamMaintMovementType) {
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_TOOL:
                mnMaintMovementIogCategory = SModSysConsts.TRNS_CT_IOG_IN;
                manIogTypeKey = SModSysConsts.TRNS_TP_IOG_IN_ADJ_ADJ;
                jtfSeries.setText(SERIES_ADJ_IN);
                
                mbMaintAreaNeeded = true;
                
                switch (mnParamMaintMovementType) {
                    case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART:
                        warehouseSourceKey = maintConfig.getKeyWarehouseParts();
                        break;
                    case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_TOOL:
                        warehouseSourceKey = maintConfig.getKeyWarehouseToolsAvailable();
                        break;
                    default:
                }
                break;
                
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_MAT:
                mnMaintMovementIogCategory = SModSysConsts.TRNS_CT_IOG_IN;
                manIogTypeKey = SModSysConsts.TRNS_TP_IOG_IN_DEV_CONS;
                jtfSeries.setText(SERIES_ADJ_IN);
                
                mbMaintAreaNeeded = true;
                
                switch (mnParamMaintMovementType) {
                    case SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_MAT:
                        warehouseSourceKey = maintConfig.getKeyWarehouseParts();
                        break;
                    default:
                }
                break;
                
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST:
                mnMaintMovementIogCategory = SModSysConsts.TRNS_CT_IOG_IN;
                manIogTypeKey = SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA; // transfers originated in stock out movements
                jtfSeries.setText(SERIES_TRA_OUT);
                
                mbWarehouseDestinyNeeded = true;
                
                warehouseDestinyKey = maintConfig.getKeyWarehouseToolsAvailable();
                
                switch (mnParamMaintMovementType) {
                    case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT:
                        warehouseSourceKey = maintConfig.getKeyWarehouseToolsLent();
                        break;
                    case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT:
                        warehouseSourceKey = maintConfig.getKeyWarehouseToolsMaint();
                        break;
                    case SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST:
                        warehouseSourceKey = maintConfig.getKeyWarehouseToolsLost();
                        break;
                    default:
                }
                break;
                
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_TOOL:
                mnMaintMovementIogCategory = SModSysConsts.TRNS_CT_IOG_OUT;
                manIogTypeKey = SModSysConsts.TRNS_TP_IOG_OUT_ADJ_ADJ;
                jtfSeries.setText(SERIES_ADJ_OUT);
                
                mbMaintAreaNeeded = true;
                
                switch (mnParamMaintMovementType) {
                    case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART:
                        warehouseSourceKey = maintConfig.getKeyWarehouseParts();
                        break;
                    case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_TOOL:
                        warehouseSourceKey = maintConfig.getKeyWarehouseToolsAvailable();
                        break;
                    default:
                }
                break;
                
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_MAT:
                mnMaintMovementIogCategory = SModSysConsts.TRNS_CT_IOG_OUT;
                manIogTypeKey = SModSysConsts.TRNS_TP_IOG_OUT_SUPP_CONS;
                jtfSeries.setText(SERIES_ADJ_OUT);
                
                mbMaintAreaNeeded = true;
                
                switch (mnParamMaintMovementType) {
                    case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_MAT:
                        warehouseSourceKey = maintConfig.getKeyWarehouseParts();
                        break;
                    default:
                }
                break;
                
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT:
            case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST:
                mnMaintMovementIogCategory = SModSysConsts.TRNS_CT_IOG_OUT;
                manIogTypeKey = SModSysConsts.TRNS_TP_IOG_OUT_INT_TRA; // transfers originated in stock out movements
                jtfSeries.setText(SERIES_TRA_OUT);
                
                mbWarehouseDestinyNeeded = true;
                
                warehouseSourceKey = maintConfig.getKeyWarehouseToolsAvailable();
                
                switch (mnParamMaintMovementType) {
                    case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT:
                        warehouseDestinyKey = maintConfig.getKeyWarehouseToolsLent();
                        break;
                    case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT:
                        warehouseDestinyKey = maintConfig.getKeyWarehouseToolsMaint();
                        break;
                    case SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST:
                        warehouseDestinyKey = maintConfig.getKeyWarehouseToolsLost();
                        break;
                    default:
                }
                break;
                
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
        
        mnIogCategoryId = manIogTypeKey[0];
        jtfSeries.setCaretPosition(0);

        moWarehouseSource = (SDataCompanyBranchEntity) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_COB_ENT, warehouseSourceKey, SLibConstants.EXEC_MODE_VERBOSE);
        validateWarehouse(moWarehouseSource, "origen");
        
        if (mbWarehouseDestinyNeeded) {
            msSeriesIogCounterpart = SERIES_TRA_IN;
            
            moWarehouseDestiny = (SDataCompanyBranchEntity) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_COB_ENT, warehouseDestinyKey, SLibConstants.EXEC_MODE_VERBOSE);
            validateWarehouse(moWarehouseDestiny, "destino");
            
            if (!STrnUtilities.canIogTypeWarehousesBeTheSame(manIogTypeKey) && SLibUtilities.compareKeys(moWarehouseSource.getPrimaryKey(), moWarehouseDestiny.getPrimaryKey())) {
                throw new Exception("Los almacenes origen y destino deben ser distintos.");
            }
        }
        
        // validate and configure maintenance use type:
        
        switch (mnParamMaintUserType) {
            case SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE:
            case SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR:
            case SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV:
                break;
                
            default:
                throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
        
        mbResetingForm = false;
    }
    
    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Resets form.
     * Must be called after <code>setParamMaintMovementSettings()</code>;
     */
    @Override
    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.FORM_STATUS_EDIT;
        mbFirstTime = true;

        moDiog = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((SFormField) mvFields.get(i)).resetField();
        }

        // Initialize form fields:
        
        moFieldDate.setDate(miClient.getSessionXXX().getWorkingDate());
        //jtfSeries.setText(""); // already set in method setParamMaintMovementSettings()!
        jtfNumber.setText("");
        
        jbOk.setEnabled(true);
        jbCancel.setEnabled(true);

        jbEdit.setEnabled(false);
        jbEditHelp.setEnabled(false);

        mvDeletedDiogEntries.clear();
        moPaneDiogEntries.clearTableRows();
        
        setUpControls();
        actionEntryClear();
        computeDocTotals();
        removeSignature();

        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;
        
        miClient.getSession().populateCatalogue(jcbMaintUser, SModConsts.TRN_MAINT_USER, mnParamMaintUserType, null);
        miClient.getSession().populateCatalogue(jcbMaintReturnUser, SModConsts.TRN_MAINT_USER, mnParamMaintUserType, null);
        miClient.getSession().populateCatalogue(jcbEntryMaintArea, SModConsts.TRN_MAINT_AREA, SLibConstants.UNDEFINED, null);
        miClient.getSession().populateCatalogue(jcbEntryConsEntity, SModConsts.TRN_MAT_CONS_ENT, SLibConstants.UNDEFINED, null);
        jcbEntrySubConsEntity.setEnabled(false);
        mbResetingForm = false;
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];
        
            if (!SDataUtilities.isPeriodOpen(miClient, moFieldDate.getDate())) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                validation.setComponent(jftDate);
            }
            else if (moDiog != null && moDiog.getPkYearId() != year) {
                validation.setMessage(SLibConstants.MSG_ERR_GUI_PER_YEAR);
                validation.setComponent(jftDate);
            }
            else if (moPaneDiogEntries.getTableGuiRowCount() == 0) {
                validation.setMessage("El documento debe tener al menos una partida.");
            }
            else {
                // Validate stock:
                
                try {
                    Vector<SDataDiogEntry> entries = new Vector<>(getAllEntries());

                    // Validate that item is not inactive or restricted on in moves:

                    for (SDataDiogEntry entry : entries) {
                        SDataItem item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { entry.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);

                        if (item.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_INA) {
                            throw new Exception(SItmConsts.MSG_ERR_ST_ITEM_INA + "\n" + 
                                    SGuiConsts.TXT_LBL_CODE + ": " + item.getKey() + "\n" +
                                    SGuiConsts.TXT_LBL_NAME + ": " + item.getItem());
                        }
                        else if (item.getFkItemStatusId() == SModSysConsts.ITMS_ST_ITEM_RES && mnIogCategoryId == SModSysConsts.TRNS_CT_IOG_IN) {
                            throw new Exception(SItmConsts.MSG_ERR_ST_ITEM_RES + "\n" + 
                                    SGuiConsts.TXT_LBL_CODE + ": " + item.getKey() + "\n" +
                                    SGuiConsts.TXT_LBL_NAME + ": " + item.getItem());
                        }
                    }

                    // Validate item lots:
                   
                    String validationMsg = STrnStockValidator.validateStockLots(miClient, entries, mnIogCategoryId, false);

                    if (!validationMsg.isEmpty()) {
                        throw new Exception(validationMsg);
                    }

                    validationMsg = STrnStockValidator.validateStockMoves(miClient, entries, mnIogCategoryId, moDiog == null ? new int[] { year, 0 } : (int[]) moDiog.getPrimaryKey(), 
                            (int[]) moWarehouseSource.getPrimaryKey(), false, moFieldDate.getDate(), 
                            SLibConstants.UNDEFINED, null, !shouldCheckMaintUserStock() ? SLibConstants.UNDEFINED : ((SGuiItem) jcbMaintUser.getSelectedItem()).getPrimaryKey()[0]);
                    if (!validationMsg.isEmpty()) {
                        throw new Exception(validationMsg);
                    }
            
                    if (!isDiogSigned()) {
                        if (getSignatoryFingerprint() == null) {
                            miClient.showMsgBoxInformation("El firmante carece de huella digital.\nEl movimiento deberá ser firmado posteriormente.");
                        }
                        else {
                            if (miClient.showMsgBoxConfirm("¿Deseas dejar el movimiento sin firmar?") != JOptionPane.YES_OPTION) {
                                validation.setMessage("Se debe firmar el movimiento.");
                                validation.setComponent(jbSign);
                            }
                        }
                    }                
                }
                catch (Exception e) {
                    validation.setMessage(e.toString());
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
        mbResetingForm = true;
        
        mnFormStatus = SLibConstants.FORM_STATUS_READ_ONLY;

        moDiog = (SDataDiog) registry;
        
        // define type of maintenance-user:

        int maintUserType = SLibConstants.UNDEFINED;
        SDbMaintUser maintUser = (SDbMaintUser) miClient.getSession().readRegistry(SModConsts.TRN_MAINT_USER, new int[] { moDiog.getFkMaintUserId_n() });
       
        if (maintUser.isEmployee()) {
            maintUserType = SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE;
        }
        else if (maintUser.isContractor()) {
            maintUserType = SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR;
        }
        else if (maintUser.isToolsMaintProvider()) {
            maintUserType = SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV;
        }

        // set form parameters to configure form accordingly:

        try {
            setParamMaintMovementSettings(moDiog.getFkMaintMovementTypeId(), maintUserType);
            formRefreshCatalogues();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        // Form controls:
                
        jbOk.setEnabled(false);
        jbCancel.setEnabled(true);

        if (moDiog.isRegistryEditable()) {
            jbEdit.setEnabled(true);
            jbEditHelp.setEnabled(false);
        }
        else {
            jbEdit.setEnabled(false);
            jbEditHelp.setEnabled(true);
        }

        // Form fields:

        moFieldDate.setFieldValue(moDiog.getDate());
        jtfSeries.setText(moDiog.getNumberSeries());
        jtfNumber.setText(moDiog.getNumber());
        jckIsSystem.setSelected(moDiog.getIsSystem());
        jckIsDeleted.setSelected(moDiog.getIsDeleted());
        
        SGuiUtils.locateItem(jcbMaintUser, new int[] { moDiog.getFkMaintUserId_n() });
        populateMaintUserSupervisor();
        SGuiUtils.locateItem(jcbMaintUserSupervisor, new int[] { moDiog.getFkMaintUserSupervisorId() });
        
        SGuiUtils.locateItem(jcbMaintReturnUser, new int[] { moDiog.getFkMaintReturnUserId_n() });
        populateMaintReturnUserSupervisor();
        SGuiUtils.locateItem(jcbMaintReturnUserSupervisor, new int[] { moDiog.getFkMaintReturnUserSupervisorId() });

        // Document entries:
        
        for (SDataDiogEntry entry : moDiog.getDbmsEntries()) {
            if (!entry.getIsDeleted()) {
                moPaneDiogEntries.addTableRow(new SDataDiogMaintMovementEntryRow(entry));
            }
        }        
        moPaneDiogEntries.renderTableRows();
        moPaneDiogEntries.setTableRowSelection(0);
        computeDocTotals();
        
        // Document notes:

        if (!moDiog.getDbmsNotes().isEmpty()) {
            moFieldNotes.setString(moDiog.getDbmsNotes().get(0).getNotes());
        }
        
        // Document signature:
        
        if (moDiog.getDbmsLastDiogSignatureId() == SLibConstants.UNDEFINED) {
            moMaintDiogSignature = null;
        }
        else {
            moMaintDiogSignature = (SDbMaintDiogSignature) miClient.getSession().readRegistry(SModConsts.TRN_MAINT_DIOG_SIG, new int[] { moDiog.getDbmsLastDiogSignatureId() });
        }
        
        showSignatureStatus();
        
        setUpControls();
        
        mbResetingForm = false;
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moDiog == null) {
            moDiog = new SDataDiog();
            moDiog.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

            moDiog.setPkYearId(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
            //moDiog.setPkDocId(...);
            moDiog.setNumberSeries(jtfSeries.getText());
            //moDiog.setNumber(...);

            moDiog.setFkDiogCategoryId(manIogTypeKey[0]);
            moDiog.setFkDiogClassId(manIogTypeKey[1]);
            moDiog.setFkDiogTypeId(manIogTypeKey[2]);
            moDiog.setFkDiogAdjustmentTypeId(SDataConstantsSys.TRNU_TP_IOG_ADJ_NA);
            moDiog.setFkCompanyBranchId(moWarehouseSource.getPkCompanyBranchId());
            moDiog.setFkWarehouseId(moWarehouseSource.getPkEntityId());
        }
        else {
            moDiog.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moDiog.setDate(moFieldDate.getDate());
        //moDiog.setReference(...);
        
        computeDocTotals();
        moDiog.setValue_r(SLibUtilities.parseDouble(jtfDocValue.getText()));
        
        //moDiog.setCostAsigned(...);
        //moDiog.setCostTransferred(...);
        //moDiog.setIsShipmentRequired(...);
        //moDiog.setIsShipped(...);
        //moDiog.setIsAudited(...);
        //moDiog.setIsAuthorized(...);
        //moDiog.setIsRecordAutomatic(...);
        //moDiog.setIsSystem(...);
        //moDiog.setIsDeleted(...);
        //...
        //moDiog.setFkDpsYearId_n(...);
        //moDiog.setFkDpsDocId_n(...);
        //moDiog.setFkDiogYearId_n(...);
        //moDiog.setFkDiogDocId_n(...);
        //moDiog.setFkMfgYearId_n(...);
        //moDiog.setFkMfgOrderId_n(...);
        //moDiog.setFkBookkeepingYearId_n(...);
        //moDiog.setFkBookkeepingNumberId_n(...);

        moDiog.setFkMaintMovementTypeId(mnParamMaintMovementType);
        moDiog.setFkMaintUserId_n(((SGuiItem) jcbMaintUser.getSelectedItem()).getPrimaryKey()[0]);
        moDiog.setFkMaintUserSupervisorId(jcbMaintUserSupervisor.getSelectedIndex() <= 0 ? SModSysConsts.TRN_MAINT_USER_SUPV_NA : ((SGuiItem) jcbMaintUserSupervisor.getSelectedItem()).getPrimaryKey()[0]);
        moDiog.setFkMaintReturnUserId_n(jcbMaintReturnUser.getSelectedIndex() <= 0 ? SLibConstants.UNDEFINED : ((SGuiItem) jcbMaintReturnUser.getSelectedItem()).getPrimaryKey()[0]);
        moDiog.setFkMaintReturnUserSupervisorId(jcbMaintReturnUserSupervisor.getSelectedIndex() <= 0 ? SModSysConsts.TRN_MAINT_USER_SUPV_NA : ((SGuiItem) jcbMaintReturnUserSupervisor.getSelectedItem()).getPrimaryKey()[0]);

        moDiog.setFkUserShippedId(SUtilConsts.USR_NA_ID);
        moDiog.setFkUserAuditedId(SUtilConsts.USR_NA_ID);
        moDiog.setFkUserAuthorizedId(SUtilConsts.USR_NA_ID);

        // Document entries:

        moDiog.getDbmsEntries().clear();
        moDiog.getDbmsEntries().addAll(getAllEntries());

        // Document notes:

        SDataDiogNotes diogNotes = null;
        
        if (!moDiog.getDbmsNotes().isEmpty()) {
            diogNotes = moDiog.getDbmsNotes().get(0);
        }

        if (moFieldNotes.getString().isEmpty()) {
            if (diogNotes != null) {
                diogNotes.setIsDeleted(true);
                diogNotes.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            }
        }
        else {
            if (diogNotes == null) {
                diogNotes = new SDataDiogNotes();
                diogNotes.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
            }
            else {
                diogNotes.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
            }

            diogNotes.setNotes(moFieldNotes.getString());
            diogNotes.setIsPrintable(true);
        }

        moDiog.getDbmsNotes().clear();
        if (diogNotes != null) {
            diogNotes.setIsRegistryEdited(true);
            moDiog.getDbmsNotes().add(diogNotes);
        }
        
        if (mbWarehouseDestinyNeeded) {
            try {
                int[] diogTypeCounterpartKey = STrnUtilities.getIogTypeCounterpart(manIogTypeKey);
                
                SDataDiog diogCounterpart = moDiog.clone();
                diogCounterpart.setIsSystem(true);
                diogCounterpart.setFkDiogCategoryId(diogTypeCounterpartKey[0]);
                diogCounterpart.setFkDiogClassId(diogTypeCounterpartKey[1]);
                diogCounterpart.setFkDiogTypeId(diogTypeCounterpartKey[2]);
                diogCounterpart.setFkCompanyBranchId(moWarehouseDestiny.getPkCompanyBranchId());
                diogCounterpart.setFkWarehouseId(moWarehouseDestiny.getPkEntityId());
                diogCounterpart.setFkDpsYearId_n(SLibConstants.UNDEFINED);
                diogCounterpart.setFkDpsDocId_n(SLibConstants.UNDEFINED);
                diogCounterpart.setFkDiogYearId_n(SLibConstants.UNDEFINED); // necesary to break link to himself
                diogCounterpart.setFkDiogDocId_n(SLibConstants.UNDEFINED);  // necesary to break link to himself

                if (moDiog.getDbmsDataCounterpartDiog() == null) {
                    diogCounterpart.setPkYearId(moDiog.getPkYearId());                    
                    diogCounterpart.setPkDocId(0);
                    diogCounterpart.setNumberSeries(msSeriesIogCounterpart);
                    diogCounterpart.setNumber("");
                }
                else {
                    diogCounterpart.setPkYearId(moDiog.getDbmsDataCounterpartDiog().getPkYearId());
                    diogCounterpart.setPkDocId(moDiog.getDbmsDataCounterpartDiog().getPkDocId());
                    diogCounterpart.setNumberSeries(moDiog.getDbmsDataCounterpartDiog().getNumberSeries());
                    diogCounterpart.setNumber(moDiog.getDbmsDataCounterpartDiog().getNumber());

                    for (SDataDiogEntry entry : diogCounterpart.getDbmsEntries()) {
                        entry.setPkYearId(moDiog.getDbmsDataCounterpartDiog().getPkYearId());
                        entry.setPkDocId(moDiog.getDbmsDataCounterpartDiog().getPkDocId());
                    }
                } 

                diogCounterpart.setDbmsDataCounterpartDiog(null);
                diogCounterpart.setAuxIsDbmsDataCounterpartDiog(true);

                moDiog.setDbmsDataCounterpartDiog(diogCounterpart);
            }
            catch (CloneNotSupportedException e) {
                SLibUtilities.renderException(this, e);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
        
        if (isDiogSigned()) {
            moDiog.setAuxSignDiog(true);
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
    public void actionPerformed(ActionEvent e) {
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
            else if (button == jbCopyMaintUser) {
                actionCopyMaintUser();
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
            else if (button == jbEntryDelete) {
                actionEntryDelete();
            }
            else if (button == jbSign) {
                actionSign();
            }
        }
        else if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfEntryTextToFind) {
                actionEntryTextToFind();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbResetingForm) {
            if (e.getSource() instanceof JComboBox) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox comboBox = (JComboBox) e.getSource();

                    if (comboBox == jcbMaintUser) {
                        stateChangedMaintUser();
                    }
                    else if (comboBox == jcbMaintReturnUser) {
                        stateChangedReturnMaintUser();
                    }
                    else if (comboBox == jcbEntryConsEntity) {
                        stateChangedCosumeEntity();
                    }
                    else if (comboBox == jcbEntrySubConsEntity) {
                        populateCostCenter();
                    }
                }
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

            if (textField == jtfEntryQuantity) {
                computeEntryValue();
            }
            else if (textField == jtfEntryValueUnit) {
                computeEntryValue();
            }
            else if (textField == jtfEntryValue) {
                computeEntryValueUnitary();
                computeEntryValue();
            }
        }
    } 
}
