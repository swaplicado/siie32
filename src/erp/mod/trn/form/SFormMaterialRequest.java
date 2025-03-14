/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.form;

import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.form.SFormCapturingNotes;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataUnit;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.SModuleItm;
import erp.mod.cfg.utils.SAuthorizationUtils;
import erp.mod.trn.db.SDbMaterialCostCenterGroup;
import erp.mod.trn.db.SDbMaterialRequest;
import erp.mod.trn.db.SDbMaterialRequestCostCenter;
import erp.mod.trn.db.SDbMaterialRequestEntry;
import erp.mod.trn.db.SDbMaterialRequestEntryItemChange;
import erp.mod.trn.db.SDbMaterialRequestEntryNote;
import erp.mod.trn.db.SDbMaterialRequestNote;
import erp.mod.trn.db.SMaterialRequestUtils;
import erp.mod.trn.db.SRowMaterialRequestItemSupply;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneForm;
import sa.lib.grid.SGridPaneFormOwner;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiFieldKeyGroup;
import sa.lib.gui.SGuiFields;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFieldKey;

/**
 *
 * @author Isabel Servín
 * 
 */
public class SFormMaterialRequest extends sa.lib.gui.bean.SBeanForm implements SGridPaneFormOwner, ActionListener, ListSelectionListener, ItemListener, FocusListener, CellEditorListener {

    private SDbMaterialRequest moRegistry;
    
    private SGuiModule moModule;
    private HashMap<Integer, Vector<SGuiItem>> moMapItems;
    
    private ArrayList<SDbMaterialRequestCostCenter> maMatReqCC;
    private ArrayList<SDbMaterialRequestEntry> maMatReqEntries;
    private ArrayList<SDbMaterialRequestNote> maMatReqNotes;
    
    private ArrayList<SRowMaterialRequestItemSupply> maMatReqItemSupply;
    
    private SGridPaneForm moGridMatReqCC;
    private SGridPaneForm moGridMatReqList;
    
    private SFormMaterialRequestCostCenter moFormMatReqCC;
    private SFormCapturingNotes moFormCapturingNotesReq;
    private SFormCapturingNotes moFormCapturingNotesEty;
    
    private SDialogItemPicker moDialogPickerItem;
    private SDialogItemPicker moDialogPickerItemRef;
    private SDialogItemPicker moDialogPickerItemRefEty;
    private SDialogUnitPicker moDialogPickerUnit;
    
    private String msReqNotes;
    private String msEtyNotes;
    
    private JLabel jlKeyWhs;
    private SBeanFieldKey moKeyWhs;
    
    private boolean isReqInv;
    private boolean isEtyNew;
    private boolean isCapturingData;
    private boolean isRegistryEditable;
    private boolean isProvPurForm;
    private boolean isPurForm;
    private boolean hasUserRevRight;
    private boolean hasUserProvRight;
    private boolean hasUserPurRight;
    private boolean hasUserProvPurRight;
    private boolean hasUserReclassRight;
    private boolean hasLinkMatReq;
    private boolean mbCcChanged;
    
    private int mnStatusReqId;
    
    private int[] moWahId;
    
    private SGuiFieldKeyGroup moFieldKeyConsEntityEty;
    private SGuiFields moFieldsEty;
    private SDataItem moItemEty;
    private SDataUnit moUnitEty;
    private SDataItem moItemRefEty;
    
    private double mdPriceUnitaryEty;
    
    private JButton jbSaveAndSend;
    
    private int mnItemRefCt;
    private int mnItemRefPickerSeccSelected;
    private int mnItemRefPickerSeccSelectedEty;
    
    /**
     * Creates new form SFormItemCost
     * @param client
     * @param title
     * @param type
     */
    public SFormMaterialRequest(SGuiClient client, String title, int type) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRN_MAT_REQ, type, title);
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jpRegistry = new javax.swing.JPanel();
        jpCaptureArea = new javax.swing.JPanel();
        jpRequest = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jpReq1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlProvEnt = new javax.swing.JLabel();
        moKeyProvEnt = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel5 = new javax.swing.JPanel();
        jlNumber = new javax.swing.JLabel();
        moIntNumber = new sa.lib.gui.bean.SBeanFieldInteger();
        moTextDate = new sa.lib.gui.bean.SBeanFieldText();
        moTextTypeReq = new sa.lib.gui.bean.SBeanFieldText();
        jPanel7 = new javax.swing.JPanel();
        jlUsrReq = new javax.swing.JLabel();
        moKeyUsrReq = new sa.lib.gui.bean.SBeanFieldKey();
        jpReq2 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jlContractor = new javax.swing.JLabel();
        moKeyContractor = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel15 = new javax.swing.JPanel();
        jlDocNature = new javax.swing.JLabel();
        moKeyDocNature = new sa.lib.gui.bean.SBeanFieldKey();
        jlReference = new javax.swing.JLabel();
        moTextReferecnce = new sa.lib.gui.bean.SBeanFieldText();
        jbImport = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jlItemRef = new javax.swing.JLabel();
        moKeyItemRef = new sa.lib.gui.bean.SBeanFieldKey();
        jbPickItemRef = new javax.swing.JButton();
        jlInfo = new javax.swing.JLabel();
        jpReq3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jlDateReq = new javax.swing.JLabel();
        moDateReq = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel6 = new javax.swing.JPanel();
        jlDateDelivery = new javax.swing.JLabel();
        moDateDelivery = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel26 = new javax.swing.JPanel();
        jlPriReq = new javax.swing.JLabel();
        moKeyPriReq = new sa.lib.gui.bean.SBeanFieldKey();
        jpReqNotes = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlReqNotes = new javax.swing.JLabel();
        moTextReqNotes = new sa.lib.gui.bean.SBeanFieldText();
        jbReqNotes = new javax.swing.JButton();
        jlAsign = new javax.swing.JLabel();
        moDecAsign = new sa.lib.gui.bean.SBeanFieldDecimal();
        jpTableCC = new javax.swing.JPanel();
        jpEty = new javax.swing.JPanel();
        jpEtyCapture = new javax.swing.JPanel();
        jpItem = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        moTextItemKey = new sa.lib.gui.bean.SBeanFieldText();
        moTextItemName = new sa.lib.gui.bean.SBeanFieldText();
        jbPickItem = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        moBoolNewItem = new sa.lib.gui.bean.SBeanFieldBoolean();
        moTextItemDescription = new sa.lib.gui.bean.SBeanFieldText();
        jlItemRefEty = new javax.swing.JLabel();
        moKeyItemRefEty = new sa.lib.gui.bean.SBeanFieldKey();
        jbPickItemRefEty = new javax.swing.JButton();
        moTextItemRefEty = new sa.lib.gui.bean.SBeanFieldText();
        jPanel44 = new javax.swing.JPanel();
        jlQtyUsr = new javax.swing.JLabel();
        moDecQtyUsr = new sa.lib.gui.bean.SBeanFieldDecimal();
        moTextUnitUsr = new sa.lib.gui.bean.SBeanFieldText();
        jbPickUnitUsr = new javax.swing.JButton();
        jlUnitPriceSist = new javax.swing.JLabel();
        moDecUnitPriceSis = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlDateReqEty = new javax.swing.JLabel();
        moDateReqEty = new sa.lib.gui.bean.SBeanFieldDate();
        jPanel45 = new javax.swing.JPanel();
        jlQty = new javax.swing.JLabel();
        moDecQty = new sa.lib.gui.bean.SBeanFieldDecimal();
        moTextUnit = new sa.lib.gui.bean.SBeanFieldText();
        jLabel2 = new javax.swing.JLabel();
        jlUnitPriceUsr = new javax.swing.JLabel();
        moDecUnitPriceUsr = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlPriEty = new javax.swing.JLabel();
        moKeyPriEty = new sa.lib.gui.bean.SBeanFieldKey();
        jlSpace3 = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        jlConsDays = new javax.swing.JLabel();
        moIntConsDays = new sa.lib.gui.bean.SBeanFieldInteger();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jlTotalEty = new javax.swing.JLabel();
        moDecTotalEty = new sa.lib.gui.bean.SBeanFieldDecimal();
        jlConsEntEty = new javax.swing.JLabel();
        moKeyConsEntEty = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel47 = new javax.swing.JPanel();
        jlUnitPriceRef = new javax.swing.JLabel();
        moTextUnitPriceRef = new sa.lib.gui.bean.SBeanFieldText();
        jLabel8 = new javax.swing.JLabel();
        jlDateDeliveryEty = new javax.swing.JLabel();
        moDateDeliveryEty = new sa.lib.gui.bean.SBeanFieldDate();
        jlConsSubentEty = new javax.swing.JLabel();
        moKeyConsSubentEty = new sa.lib.gui.bean.SBeanFieldKey();
        jPanel48 = new javax.swing.JPanel();
        jlEtyNotes = new javax.swing.JLabel();
        moTextEtyNotes = new sa.lib.gui.bean.SBeanFieldText();
        jbEtyNotes = new javax.swing.JButton();
        jlCostCenterEty = new javax.swing.JLabel();
        moKeyCostCenterEty = new sa.lib.gui.bean.SBeanFieldKey();
        jpButtons = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jbNewEty = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jbEditEty = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jbDeleteEty = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jbRegisterEty = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jbCancelEty = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jbItemStk = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jpTable = new javax.swing.JPanel();
        jpEntries = new javax.swing.JPanel();
        jpValidate = new javax.swing.JPanel();
        jpEstados = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        moTextReqStatus = new sa.lib.gui.bean.SBeanFieldText();
        jlAuthStatus = new javax.swing.JLabel();
        moTextAuthStatus = new sa.lib.gui.bean.SBeanFieldText();
        jbAuthorize = new javax.swing.JButton();
        jbReject = new javax.swing.JButton();
        jlspace = new javax.swing.JLabel();
        jlProvStatus = new javax.swing.JLabel();
        moTextProvStatus = new sa.lib.gui.bean.SBeanFieldText();
        moBoolProvClosed = new sa.lib.gui.bean.SBeanFieldBoolean();
        jlPurStatus = new javax.swing.JLabel();
        moTextPurStatus = new sa.lib.gui.bean.SBeanFieldText();
        moBoolPurClosed = new sa.lib.gui.bean.SBeanFieldBoolean();
        jlTotal = new javax.swing.JLabel();
        moDecTotal = new sa.lib.gui.bean.SBeanFieldDecimal();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpRegistry.setLayout(new java.awt.BorderLayout());

        jpCaptureArea.setLayout(new java.awt.BorderLayout());

        jpRequest.setBorder(javax.swing.BorderFactory.createTitledBorder("Requisición:"));
        jpRequest.setLayout(new java.awt.BorderLayout());

        jPanel20.setLayout(new java.awt.BorderLayout());

        jpReq1.setLayout(new java.awt.GridLayout(3, 0, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProvEnt.setText("Ctro. suministro:*");
        jlProvEnt.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlProvEnt);

        moKeyProvEnt.setPreferredSize(new java.awt.Dimension(180, 23));
        jPanel4.add(moKeyProvEnt);

        jpReq1.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumber.setText("Folio, fecha, tipo:");
        jlNumber.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlNumber);

        moIntNumber.setEnabled(false);
        moIntNumber.setPreferredSize(new java.awt.Dimension(61, 23));
        jPanel5.add(moIntNumber);

        moTextDate.setEnabled(false);
        moTextDate.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel5.add(moTextDate);

        moTextTypeReq.setEnabled(false);
        moTextTypeReq.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel5.add(moTextTypeReq);

        jpReq1.add(jPanel5);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUsrReq.setText("Solicitante:*");
        jlUsrReq.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlUsrReq);

        moKeyUsrReq.setPreferredSize(new java.awt.Dimension(180, 23));
        jPanel7.add(moKeyUsrReq);

        jpReq1.add(jPanel7);

        jPanel20.add(jpReq1, java.awt.BorderLayout.WEST);

        jpReq2.setLayout(new java.awt.GridLayout(3, 0, 0, 5));

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContractor.setText("Contratista:");
        jlContractor.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlContractor);

        moKeyContractor.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel14.add(moKeyContractor);

        jpReq2.add(jPanel14);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocNature.setText("Naturaleza doc.:*");
        jlDocNature.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlDocNature);

        moKeyDocNature.setPreferredSize(new java.awt.Dimension(158, 23));
        jPanel15.add(moKeyDocNature);

        jlReference.setText("Ref.:");
        jlReference.setPreferredSize(new java.awt.Dimension(30, 23));
        jlReference.setRequestFocusEnabled(false);
        jPanel15.add(jlReference);

        moTextReferecnce.setEnabled(false);
        moTextReferecnce.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel15.add(moTextReferecnce);

        jbImport.setText("Importar");
        jbImport.setToolTipText("Importar referencia");
        jbImport.setEnabled(false);
        jbImport.setPreferredSize(new java.awt.Dimension(79, 23));
        jPanel15.add(jbImport);

        jpReq2.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItemRef.setText("Concepto/gasto:*");
        jlItemRef.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlItemRef);

        moKeyItemRef.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel16.add(moKeyItemRef);

        jbPickItemRef.setText("...");
        jbPickItemRef.setToolTipText("Seleccionar ítem");
        jbPickItemRef.setFocusable(false);
        jbPickItemRef.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel16.add(jbPickItemRef);

        jlInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_info.png"))); // NOI18N
        jlInfo.setToolTipText("Corresponde al concepto o gasto para contabilización");
        jlInfo.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel16.add(jlInfo);

        jpReq2.add(jPanel16);

        jPanel20.add(jpReq2, java.awt.BorderLayout.CENTER);

        jpReq3.setLayout(new java.awt.GridLayout(3, 0, 0, 5));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateReq.setText("Fecha requerida:");
        jlDateReq.setToolTipText("");
        jlDateReq.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel1.add(jlDateReq);

        moDateReq.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel1.add(moDateReq);

        jpReq3.add(jPanel1);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateDelivery.setText("Entrega estimada:");
        jlDateDelivery.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel6.add(jlDateDelivery);

        moDateDelivery.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel6.add(moDateDelivery);

        jpReq3.add(jPanel6);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPriReq.setText("Prioridad:*");
        jlPriReq.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel26.add(jlPriReq);

        moKeyPriReq.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel26.add(moKeyPriReq);

        jpReq3.add(jPanel26);

        jPanel20.add(jpReq3, java.awt.BorderLayout.EAST);

        jpReqNotes.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlReqNotes.setText("Notas:");
        jlReqNotes.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel2.add(jlReqNotes);

        moTextReqNotes.setPreferredSize(new java.awt.Dimension(645, 23));
        jPanel2.add(moTextReqNotes);

        jbReqNotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbReqNotes.setToolTipText("Agregar comentario");
        jbReqNotes.setFocusable(false);
        jbReqNotes.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel2.add(jbReqNotes);

        jlAsign.setText("Total asignación:");
        jlAsign.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel2.add(jlAsign);
        jlAsign.getAccessibleContext().setAccessibleName("Suma asignación:");

        moDecAsign.setEditable(false);
        moDecAsign.setText("0.00");
        moDecAsign.setEnabled(false);
        jPanel2.add(moDecAsign);

        jpReqNotes.add(jPanel2);

        jPanel20.add(jpReqNotes, java.awt.BorderLayout.SOUTH);

        jpRequest.add(jPanel20, java.awt.BorderLayout.NORTH);

        jpTableCC.setPreferredSize(new java.awt.Dimension(77, 100));
        jpTableCC.setLayout(new java.awt.BorderLayout());
        jpRequest.add(jpTableCC, java.awt.BorderLayout.CENTER);

        jpCaptureArea.add(jpRequest, java.awt.BorderLayout.NORTH);

        jpEty.setBorder(javax.swing.BorderFactory.createTitledBorder("Partida:"));
        jpEty.setLayout(new java.awt.BorderLayout());

        jpEtyCapture.setLayout(new java.awt.BorderLayout(0, 5));

        jpItem.setLayout(new java.awt.GridLayout(7, 0, 0, 5));

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:*");
        jlItem.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel29.add(jlItem);
        jPanel29.add(moTextItemKey);

        moTextItemName.setEditable(false);
        moTextItemName.setEnabled(false);
        moTextItemName.setPreferredSize(new java.awt.Dimension(665, 23));
        jPanel29.add(moTextItemName);

        jbPickItem.setText("...");
        jbPickItem.setToolTipText("Seleccionar ítem");
        jbPickItem.setFocusable(false);
        jbPickItem.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel29.add(jbPickItem);

        jpItem.add(jPanel29);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolNewItem.setText("Nuevo ítem");
        jPanel30.add(moBoolNewItem);

        moTextItemDescription.setEnabled(false);
        moTextItemDescription.setPreferredSize(new java.awt.Dimension(445, 23));
        jPanel30.add(moTextItemDescription);

        jlItemRefEty.setText("Concepto/gasto:");
        jlItemRefEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jlItemRefEty);

        moKeyItemRefEty.setPreferredSize(new java.awt.Dimension(215, 23));
        jPanel30.add(moKeyItemRefEty);

        jbPickItemRefEty.setText("...");
        jbPickItemRefEty.setToolTipText("Seleccionar ítem");
        jbPickItemRefEty.setFocusable(false);
        jbPickItemRefEty.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel30.add(jbPickItemRefEty);

        moTextItemRefEty.setPreferredSize(new java.awt.Dimension(215, 23));
        jPanel30.add(moTextItemRefEty);

        jpItem.add(jPanel30);

        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlQtyUsr.setText("Cantidad:*");
        jlQtyUsr.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel44.add(jlQtyUsr);
        jPanel44.add(moDecQtyUsr);

        moTextUnitUsr.setEnabled(false);
        jPanel44.add(moTextUnitUsr);

        jbPickUnitUsr.setText("...");
        jbPickUnitUsr.setToolTipText("Seleccionar ítem");
        jbPickUnitUsr.setFocusable(false);
        jbPickUnitUsr.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel44.add(jbPickUnitUsr);

        jlUnitPriceSist.setText("Precio u. sis.:");
        jlUnitPriceSist.setPreferredSize(new java.awt.Dimension(102, 23));
        jPanel44.add(jlUnitPriceSist);

        moDecUnitPriceSis.setEditable(false);
        jPanel44.add(moDecUnitPriceSis);

        jlDateReqEty.setText("Fecha requerida:");
        jlDateReqEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel44.add(jlDateReqEty);

        moDateReqEty.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel44.add(moDateReqEty);

        jpItem.add(jPanel44);

        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlQty.setText("Cant. equivalente:");
        jlQty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel45.add(jlQty);

        moDecQty.setEnabled(false);
        jPanel45.add(moDecQty);

        moTextUnit.setEnabled(false);
        jPanel45.add(moTextUnit);

        jLabel2.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel45.add(jLabel2);

        jlUnitPriceUsr.setText("Precio u.:");
        jlUnitPriceUsr.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel45.add(jlUnitPriceUsr);
        jPanel45.add(moDecUnitPriceUsr);

        jlPriEty.setText("Prioridad:");
        jlPriEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel45.add(jlPriEty);

        moKeyPriEty.setPreferredSize(new java.awt.Dimension(215, 23));
        jPanel45.add(moKeyPriEty);

        jlSpace3.setPreferredSize(new java.awt.Dimension(4, 23));
        jlSpace3.setVerifyInputWhenFocusTarget(false);
        jPanel45.add(jlSpace3);

        jpItem.add(jPanel45);

        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConsDays.setText("Tiempo consumo:");
        jlConsDays.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel46.add(jlConsDays);
        jPanel46.add(moIntConsDays);

        jLabel5.setText("(días estimados)");
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 23));
        jLabel5.setVerifyInputWhenFocusTarget(false);
        jPanel46.add(jLabel5);

        jLabel4.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel46.add(jLabel4);

        jlTotalEty.setText("Importe:");
        jlTotalEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel46.add(jlTotalEty);

        moDecTotalEty.setEditable(false);
        jPanel46.add(moDecTotalEty);

        jlConsEntEty.setText("Ctro. consumo:");
        jlConsEntEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel46.add(jlConsEntEty);

        moKeyConsEntEty.setPreferredSize(new java.awt.Dimension(215, 23));
        jPanel46.add(moKeyConsEntEty);

        jpItem.add(jPanel46);

        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlUnitPriceRef.setText("Ref. precio u.:");
        jlUnitPriceRef.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel47.add(jlUnitPriceRef);

        moTextUnitPriceRef.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel47.add(moTextUnitPriceRef);

        jLabel8.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel47.add(jLabel8);

        jlDateDeliveryEty.setText("Entrega estimada:");
        jlDateDeliveryEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel47.add(jlDateDeliveryEty);

        moDateDeliveryEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel47.add(moDateDeliveryEty);

        jlConsSubentEty.setText("Subctro. consumo:");
        jlConsSubentEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel47.add(jlConsSubentEty);

        moKeyConsSubentEty.setEnabled(false);
        moKeyConsSubentEty.setPreferredSize(new java.awt.Dimension(215, 23));
        jPanel47.add(moKeyConsSubentEty);

        jpItem.add(jPanel47);

        jPanel48.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEtyNotes.setText("Notas:");
        jlEtyNotes.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel48.add(jlEtyNotes);

        moTextEtyNotes.setPreferredSize(new java.awt.Dimension(417, 23));
        jPanel48.add(moTextEtyNotes);

        jbEtyNotes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_edit.gif"))); // NOI18N
        jbEtyNotes.setToolTipText("Agregar comentario");
        jbEtyNotes.setFocusable(false);
        jbEtyNotes.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel48.add(jbEtyNotes);

        jlCostCenterEty.setText("Centro costo:");
        jlCostCenterEty.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel48.add(jlCostCenterEty);

        moKeyCostCenterEty.setEnabled(false);
        moKeyCostCenterEty.setPreferredSize(new java.awt.Dimension(215, 23));
        jPanel48.add(moKeyCostCenterEty);

        jpItem.add(jPanel48);

        jpEtyCapture.add(jpItem, java.awt.BorderLayout.CENTER);

        jpButtons.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.GridLayout(8, 0));

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbNewEty.setText("Crear");
        jbNewEty.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel9.add(jbNewEty);

        jPanel13.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbEditEty.setText("Modificar");
        jbEditEty.setEnabled(false);
        jbEditEty.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel10.add(jbEditEty);

        jPanel13.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbDeleteEty.setText("Eliminar");
        jbDeleteEty.setEnabled(false);
        jbDeleteEty.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel11.add(jbDeleteEty);

        jPanel13.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbRegisterEty.setText("Aceptar");
        jbRegisterEty.setEnabled(false);
        jbRegisterEty.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel12.add(jbRegisterEty);

        jPanel13.add(jPanel12);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbCancelEty.setText("Cancelar");
        jbCancelEty.setEnabled(false);
        jbCancelEty.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel17.add(jbCancelEty);

        jPanel13.add(jPanel17);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel13.add(jPanel21);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel13.add(jPanel22);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbItemStk.setText("Importar");
        jbItemStk.setToolTipText("Importar existencias");
        jbItemStk.setEnabled(false);
        jbItemStk.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel23.add(jbItemStk);

        jPanel13.add(jPanel23);

        jpButtons.add(jPanel13, java.awt.BorderLayout.NORTH);

        jpEtyCapture.add(jpButtons, java.awt.BorderLayout.EAST);

        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel18.add(jPanel19, java.awt.BorderLayout.CENTER);

        jpEtyCapture.add(jPanel18, java.awt.BorderLayout.SOUTH);

        jpEty.add(jpEtyCapture, java.awt.BorderLayout.CENTER);

        jpCaptureArea.add(jpEty, java.awt.BorderLayout.PAGE_END);

        jpRegistry.add(jpCaptureArea, java.awt.BorderLayout.NORTH);

        jpTable.setLayout(new java.awt.BorderLayout(0, 5));

        jpEntries.setName(""); // NOI18N
        jpEntries.setLayout(new java.awt.BorderLayout());
        jpTable.add(jpEntries, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jpTable, java.awt.BorderLayout.CENTER);

        jpValidate.setLayout(new java.awt.BorderLayout());

        jpEstados.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setText("Estatus:");
        jLabel3.setPreferredSize(new java.awt.Dimension(46, 23));
        jpEstados.add(jLabel3);

        moTextReqStatus.setEditable(false);
        moTextReqStatus.setEnabled(false);
        moTextReqStatus.setPreferredSize(new java.awt.Dimension(105, 23));
        moTextReqStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moTextReqStatusActionPerformed(evt);
            }
        });
        jpEstados.add(moTextReqStatus);

        jlAuthStatus.setText("Autorización:");
        jpEstados.add(jlAuthStatus);

        moTextAuthStatus.setEnabled(false);
        moTextAuthStatus.setPreferredSize(new java.awt.Dimension(75, 23));
        jpEstados.add(moTextAuthStatus);

        jbAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_st_thumbs_up.png"))); // NOI18N
        jbAuthorize.setToolTipText("Autorizar");
        jbAuthorize.setEnabled(false);
        jbAuthorize.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEstados.add(jbAuthorize);

        jbReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_st_thumbs_down.png"))); // NOI18N
        jbReject.setToolTipText("Rechazar");
        jbReject.setEnabled(false);
        jbReject.setPreferredSize(new java.awt.Dimension(23, 23));
        jpEstados.add(jbReject);

        jlspace.setPreferredSize(new java.awt.Dimension(10, 23));
        jpEstados.add(jlspace);

        jlProvStatus.setText("Suministro:");
        jpEstados.add(jlProvStatus);

        moTextProvStatus.setEnabled(false);
        moTextProvStatus.setPreferredSize(new java.awt.Dimension(75, 23));
        jpEstados.add(moTextProvStatus);

        moBoolProvClosed.setText("Terminado");
        moBoolProvClosed.setToolTipText("Cerrado para suministro");
        moBoolProvClosed.setEnabled(false);
        moBoolProvClosed.setPreferredSize(new java.awt.Dimension(75, 23));
        jpEstados.add(moBoolProvClosed);

        jlPurStatus.setText("Compras:");
        jpEstados.add(jlPurStatus);

        moTextPurStatus.setEnabled(false);
        moTextPurStatus.setPreferredSize(new java.awt.Dimension(75, 23));
        jpEstados.add(moTextPurStatus);

        moBoolPurClosed.setText("Terminado");
        moBoolPurClosed.setToolTipText("Cerrado para suministro");
        moBoolPurClosed.setEnabled(false);
        moBoolPurClosed.setPreferredSize(new java.awt.Dimension(75, 23));
        jpEstados.add(moBoolPurClosed);

        jlTotal.setText("Total req.:");
        jlTotal.setPreferredSize(new java.awt.Dimension(65, 23));
        jpEstados.add(jlTotal);

        moDecTotal.setEditable(false);
        moDecTotal.setText("0.00");
        moDecTotal.setEnabled(false);
        jpEstados.add(moDecTotal);

        jpValidate.add(jpEstados, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jpValidate, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        actionCancel();
    }//GEN-LAST:event_formWindowClosing

    private void moTextReqStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moTextReqStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moTextReqStatusActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
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
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAuthorize;
    private javax.swing.JButton jbCancelEty;
    private javax.swing.JButton jbDeleteEty;
    private javax.swing.JButton jbEditEty;
    private javax.swing.JButton jbEtyNotes;
    private javax.swing.JButton jbImport;
    private javax.swing.JButton jbItemStk;
    private javax.swing.JButton jbNewEty;
    private javax.swing.JButton jbPickItem;
    private javax.swing.JButton jbPickItemRef;
    private javax.swing.JButton jbPickItemRefEty;
    private javax.swing.JButton jbPickUnitUsr;
    private javax.swing.JButton jbRegisterEty;
    private javax.swing.JButton jbReject;
    private javax.swing.JButton jbReqNotes;
    private javax.swing.JLabel jlAsign;
    private javax.swing.JLabel jlAuthStatus;
    private javax.swing.JLabel jlConsDays;
    private javax.swing.JLabel jlConsEntEty;
    private javax.swing.JLabel jlConsSubentEty;
    private javax.swing.JLabel jlContractor;
    private javax.swing.JLabel jlCostCenterEty;
    private javax.swing.JLabel jlDateDelivery;
    private javax.swing.JLabel jlDateDeliveryEty;
    private javax.swing.JLabel jlDateReq;
    private javax.swing.JLabel jlDateReqEty;
    private javax.swing.JLabel jlDocNature;
    private javax.swing.JLabel jlEtyNotes;
    private javax.swing.JLabel jlInfo;
    private javax.swing.JLabel jlItem;
    private javax.swing.JLabel jlItemRef;
    private javax.swing.JLabel jlItemRefEty;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlPriEty;
    private javax.swing.JLabel jlPriReq;
    private javax.swing.JLabel jlProvEnt;
    private javax.swing.JLabel jlProvStatus;
    private javax.swing.JLabel jlPurStatus;
    private javax.swing.JLabel jlQty;
    private javax.swing.JLabel jlQtyUsr;
    private javax.swing.JLabel jlReference;
    private javax.swing.JLabel jlReqNotes;
    private javax.swing.JLabel jlSpace3;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JLabel jlTotalEty;
    private javax.swing.JLabel jlUnitPriceRef;
    private javax.swing.JLabel jlUnitPriceSist;
    private javax.swing.JLabel jlUnitPriceUsr;
    private javax.swing.JLabel jlUsrReq;
    private javax.swing.JLabel jlspace;
    private javax.swing.JPanel jpButtons;
    private javax.swing.JPanel jpCaptureArea;
    private javax.swing.JPanel jpEntries;
    private javax.swing.JPanel jpEstados;
    private javax.swing.JPanel jpEty;
    private javax.swing.JPanel jpEtyCapture;
    private javax.swing.JPanel jpItem;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpReq1;
    private javax.swing.JPanel jpReq2;
    private javax.swing.JPanel jpReq3;
    private javax.swing.JPanel jpReqNotes;
    private javax.swing.JPanel jpRequest;
    private javax.swing.JPanel jpTable;
    private javax.swing.JPanel jpTableCC;
    private javax.swing.JPanel jpValidate;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolNewItem;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolProvClosed;
    private sa.lib.gui.bean.SBeanFieldBoolean moBoolPurClosed;
    private sa.lib.gui.bean.SBeanFieldDate moDateDelivery;
    private sa.lib.gui.bean.SBeanFieldDate moDateDeliveryEty;
    private sa.lib.gui.bean.SBeanFieldDate moDateReq;
    private sa.lib.gui.bean.SBeanFieldDate moDateReqEty;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecAsign;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecQty;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecQtyUsr;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecTotal;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecTotalEty;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecUnitPriceSis;
    private sa.lib.gui.bean.SBeanFieldDecimal moDecUnitPriceUsr;
    private sa.lib.gui.bean.SBeanFieldInteger moIntConsDays;
    private sa.lib.gui.bean.SBeanFieldInteger moIntNumber;
    private sa.lib.gui.bean.SBeanFieldKey moKeyConsEntEty;
    private sa.lib.gui.bean.SBeanFieldKey moKeyConsSubentEty;
    private sa.lib.gui.bean.SBeanFieldKey moKeyContractor;
    private sa.lib.gui.bean.SBeanFieldKey moKeyCostCenterEty;
    private sa.lib.gui.bean.SBeanFieldKey moKeyDocNature;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItemRef;
    private sa.lib.gui.bean.SBeanFieldKey moKeyItemRefEty;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPriEty;
    private sa.lib.gui.bean.SBeanFieldKey moKeyPriReq;
    private sa.lib.gui.bean.SBeanFieldKey moKeyProvEnt;
    private sa.lib.gui.bean.SBeanFieldKey moKeyUsrReq;
    private sa.lib.gui.bean.SBeanFieldText moTextAuthStatus;
    private sa.lib.gui.bean.SBeanFieldText moTextDate;
    private sa.lib.gui.bean.SBeanFieldText moTextEtyNotes;
    private sa.lib.gui.bean.SBeanFieldText moTextItemDescription;
    private sa.lib.gui.bean.SBeanFieldText moTextItemKey;
    private sa.lib.gui.bean.SBeanFieldText moTextItemName;
    private sa.lib.gui.bean.SBeanFieldText moTextItemRefEty;
    private sa.lib.gui.bean.SBeanFieldText moTextProvStatus;
    private sa.lib.gui.bean.SBeanFieldText moTextPurStatus;
    private sa.lib.gui.bean.SBeanFieldText moTextReferecnce;
    private sa.lib.gui.bean.SBeanFieldText moTextReqNotes;
    private sa.lib.gui.bean.SBeanFieldText moTextReqStatus;
    private sa.lib.gui.bean.SBeanFieldText moTextTypeReq;
    private sa.lib.gui.bean.SBeanFieldText moTextUnit;
    private sa.lib.gui.bean.SBeanFieldText moTextUnitPriceRef;
    private sa.lib.gui.bean.SBeanFieldText moTextUnitUsr;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 1024, 700);
        
        moModule = new SModuleItm(miClient);
        
        moFieldsEty = new SGuiFields();
        
        moKeyProvEnt.setKeySettings(miClient, SGuiUtils.getLabelName(jlProvEnt), true);
        moIntNumber.setIntegerSettings(SGuiUtils.getLabelName(jlNumber), SGuiConsts.GUI_TYPE_INT, true);
        moTextDate.setTextSettings(SGuiUtils.getLabelName("Fecha"), 10, 0);
        moKeyUsrReq.setKeySettings(miClient, SGuiUtils.getLabelName(jlUsrReq), true);
        moKeyContractor.setKeySettings(miClient, SGuiUtils.getLabelName(jlContractor), false);
        moKeyDocNature.setKeySettings(miClient, SGuiUtils.getLabelName(jlDocNature), true);
        moTextReferecnce.setTextSettings(SGuiUtils.getLabelName(jlReference), 25, 0);
        moKeyItemRef.setKeySettings(miClient, SGuiUtils.getLabelName(jlItemRef), true);
        moDateReq.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateReq), false);
        moDateDelivery.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateDelivery), false);
        moKeyPriReq.setKeySettings(miClient, SGuiUtils.getLabelName(jlPriReq), true);
        moTextReqStatus.setTextSettings(SGuiUtils.getLabelName("Estatus"), 100, 0);
        moDecTotal.setDecimalSettings(SGuiUtils.getLabelName(jlTotal), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moDecAsign.setDecimalSettings(SGuiUtils.getLabelName(jlAsign), SGuiConsts.GUI_TYPE_DEC_PER_DISC, true);
        moTextReqNotes.setTextSettings(SGuiUtils.getLabelName(jlReqNotes), 1000, 1);
        moTextTypeReq.setTextSettings(SGuiUtils.getLabelName("Tipo"), 1, 0);
        
        moTextItemKey.setTextSettings(SGuiUtils.getLabelName(jlItem), 500, 1);
        moTextItemName.setTextSettings(SGuiUtils.getLabelName(jlItem), 500, 1);
        moBoolNewItem.setBooleanSettings(SGuiUtils.getLabelName(moBoolNewItem.getText()), false);
        moTextItemDescription.setTextSettings(SGuiUtils.getLabelName("Descripción"), 500, 1);
        
        moDecQtyUsr.setDecimalSettings(SGuiUtils.getLabelName(jlQtyUsr), SGuiConsts.GUI_TYPE_DEC_QTY, true);
        moTextUnitUsr.setTextSettings(SGuiUtils.getLabelName("Unidad usuario"), 10);
        moDecQty.setDecimalSettings(SGuiUtils.getLabelName(jlQty), SGuiConsts.GUI_TYPE_DEC_QTY, true);
        moTextUnit.setTextSettings(SGuiUtils.getLabelName("Unidad"), 10);
        moIntConsDays.setIntegerSettings(SGuiUtils.getLabelName(jlConsDays), SGuiConsts.GUI_TYPE_INT, false);
        moDecUnitPriceSis.setDecimalSettings(SGuiUtils.getLabelName(jlUnitPriceSist), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecUnitPriceUsr.setDecimalSettings(SGuiUtils.getLabelName(jlUnitPriceUsr), SGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecTotalEty.setDecimalSettings(SGuiUtils.getLabelName(jlUnitPriceUsr), SGuiConsts.GUI_TYPE_DEC_AMT, true);
        moTextUnitPriceRef.setTextSettings(SGuiUtils.getLabelName(jlUnitPriceRef), 15, 0);
        moKeyItemRefEty.setKeySettings(miClient, SGuiUtils.getLabelName(jlItemRefEty), false);
        moKeyConsEntEty.setKeySettings(miClient, SGuiUtils.getLabelName(jlConsEntEty), false);
        moKeyConsSubentEty.setKeySettings(miClient, SGuiUtils.getLabelName(jlConsSubentEty), false);
        moKeyCostCenterEty.setKeySettings(miClient, SGuiUtils.getLabelName(jlCostCenterEty), false);
        moDateReqEty.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateReqEty), false);
        moDateDeliveryEty.setDateSettings(miClient, SGuiUtils.getLabelName(jlDateDeliveryEty), false);
        moKeyPriEty.setKeySettings(miClient, SGuiUtils.getLabelName(jlPriEty), false);
        
        moTextAuthStatus.setTextSettings(SGuiUtils.getLabelName(jlAuthStatus), 500, 0);
        moTextProvStatus.setTextSettings(SGuiUtils.getLabelName(jlProvStatus), 500, 0);
        moBoolProvClosed.setBooleanSettings(SGuiUtils.getLabelName(moBoolProvClosed.getText()), false);
        moTextPurStatus.setTextSettings(SGuiUtils.getLabelName(jlPurStatus), 500, 0);
        moBoolPurClosed.setBooleanSettings(SGuiUtils.getLabelName(moBoolPurClosed.getText()), false);

        moFields.addField(moKeyProvEnt);
        moFields.addField(moIntNumber); 
        moFields.addField(moTextDate);
        moFields.addField(moKeyUsrReq);
        moFields.addField(moKeyContractor);
        moFields.addField(moKeyDocNature);
        moFields.addField(moTextReferecnce);
        moFields.addField(moKeyItemRef);
        moFields.addField(moDateReq);
        moFields.addField(moDateDelivery);
        moFields.addField(moKeyPriReq);
        moFields.addField(moBoolProvClosed);
        moFields.addField(moTextReqStatus);
        moFields.addField(moDecTotal);
        moFields.addField(moTextReqNotes);
        
        moFieldsEty.addField(moTextItemKey);
        moFieldsEty.addField(moTextItemName);
        moFieldsEty.addField(moBoolNewItem);
        moFieldsEty.addField(moTextItemDescription);
        
        moFieldsEty.addField(moDecQtyUsr);
        moFieldsEty.addField(moTextUnitUsr);
        moFieldsEty.addField(moDecQty);
        moFieldsEty.addField(moTextUnit);
        moFieldsEty.addField(moIntConsDays);
        moFieldsEty.addField(moDecUnitPriceSis);
        moFieldsEty.addField(moDecUnitPriceUsr);
        moFieldsEty.addField(moDecTotalEty);
        moFieldsEty.addField(moTextUnitPriceRef);
        moFieldsEty.addField(moKeyItemRefEty);
        moFieldsEty.addField(moKeyConsEntEty);
        moFieldsEty.addField(moKeyConsSubentEty);
        moFieldsEty.addField(moKeyCostCenterEty);
        moFieldsEty.addField(moDateReqEty);
        moFieldsEty.addField(moDateDeliveryEty);
        moFieldsEty.addField(moKeyPriEty);
        
        moFieldKeyConsEntityEty = new SGuiFieldKeyGroup(miClient);

        moFields.setFormButton(jbSave);
        
        moFormMatReqCC = new SFormMaterialRequestCostCenter(miClient, getFormSubtype() == SModConsts.TRNX_MAT_REQ_STK_SUP ? SModConsts.TRNX_MAT_REQ_STK_SUP : SLibConsts.UNDEFINED, "Requisición de materiales y centros de costo");
        
        moGridMatReqCC = new SGridPaneForm(miClient, SModConsts.TRN_MAT_REQ_CC, SLibConsts.UNDEFINED, "Requisición de materiales y centros de costo")  {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(true);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();
                
                SGridColumnForm col = new SGridColumnForm(SGridConsts.COL_TYPE_DEC_PER_DISC, "Asignación");
                col.setEditable(true);
                
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Centro consumo"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Subcentro consumo"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ACC, "Centro costo"));
                columns.add(col);
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_CAL_YEAR, "Año presupuesto"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha ini. presupuesto"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha fin presupuesto"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Presupuesto"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Pres. solic. ant."));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Pres. solic. tot."));
                
                return columns;
            }
        };
        moGridMatReqCC.setForm(moFormMatReqCC);
        moGridMatReqCC.setPaneFormOwner(this);
        mvFormGrids.add(moGridMatReqCC);
        jpTableCC.add(moGridMatReqCC, BorderLayout.CENTER);
        
        moGridMatReqList = new SGridPaneForm(miClient, SModConsts.TRN_MAT_REQ_ETY, SLibConsts.UNDEFINED, "Ítems") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public ArrayList<SGridColumnForm> createGridColumns() {
                ArrayList<SGridColumnForm> columns = new ArrayList<>();
                
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Código ítem"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Ítem", 350));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Número parte"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Concepto/gasto"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Cantidad"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_4D, "Cant. equivalente"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad equivalente"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Importe"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_BOOL_S, "Nuevo ítem"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_INT_2B, "Tiempo consumo"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Ctro. consumo"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Subctro. consumo"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, "Fecha requerida"));
                columns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT, "Prioridad"));
                
                return columns;
            }
        };
        
        moGridMatReqList.setPaneFormOwner(this);
        mvFormGrids.add(moGridMatReqList);
        jpEntries.add(moGridMatReqList, BorderLayout.CENTER);
        
        jbSaveAndSend = new JButton();
        jbSaveAndSend.setText("Guardar y solicitar");
        jbSaveAndSend.setPreferredSize(new java.awt.Dimension(200, 23));
        jpCommandRight.remove(jbCancel);
        jpCommandRight.add(jbSaveAndSend);
        jpCommandRight.add(jbCancel);
        
        moKeyCostCenterEty.setEnabled(false);
        moBoolProvClosed.setEnabled(false);
        
        isProvPurForm = getFormSubtype() == SModConsts.TRNX_MAT_REQ_PEND_SUP || getFormSubtype() == SModConsts.TRNX_MAT_REQ_PEND_PUR;
        isPurForm = getFormSubtype() == SModConsts.TRNX_MAT_REQ_PEND_PUR;
        hasUserRevRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_REV).HasRight;
        hasUserProvRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PROV).HasRight;
        hasUserPurRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PUR).HasRight;
        hasUserProvPurRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PROV).HasRight ||
                ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_PUR).HasRight;
        hasUserReclassRight = ((SClientInterface) miClient).getSessionXXX().getUser().hasRight((SClientInterface) miClient, SDataConstantsSys.PRV_INV_REQ_MAT_RECLASS).HasRight;
        
        moDialogPickerItem = null;
        moDialogPickerItemRef = null;
        moDialogPickerItemRefEty = null;
        moDialogPickerUnit = null;
        
        moFormCapturingNotesReq = null;
        moFormCapturingNotesEty = null;
        
        jlKeyWhs = new JLabel("Almacén: ");
        moKeyWhs = new SBeanFieldKey();
        moKeyWhs.setPreferredSize(new java.awt.Dimension(300, 23));
        moKeyWhs.setKeySettings(miClient, SGuiUtils.getLabelName("Almacén"), false);
        moFields.addField(moKeyWhs);
    }
    
    private void populateMatReqCC() {
        try {
            Vector<SGridRow> vRows = new Vector<>();
            if (maMatReqCC.size() > 0) {
                for(SDbMaterialRequestCostCenter cc : maMatReqCC) {
                    cc.setRowConsBudWOReq(SMaterialRequestUtils.getConsumedBudget(miClient.getSession(), 
                                new int[] { cc.getFkBudgetMatConsumptionEntityId(), cc.getFkBudgetYearId(), cc.getFkBudgetPeriodId() }, cc.getPkMatRequestId()));       
                }
                vRows.addAll(maMatReqCC);
            }
            moGridMatReqCC.populateGrid(vRows);
            moGridMatReqCC.getTable().getDefaultEditor(Double.class).addCellEditorListener(this); 
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void canAddRows() {
        if (getFormSubtype() == SModConsts.TRNX_MAT_REQ_STK_SUP) {
            if (moGridMatReqCC.getTable().getRowCount() > 1) {
                moGridMatReqCC.removeGridRow(1);
                moGridMatReqCC.renderGridRows();
                miClient.showMsgBoxInformation("Sólo puede haber un solo centro de consumo en RM de resurtido.");
            }      
        }
        else {
            ArrayList<SPkMatCC> arrpk = new ArrayList<>();
            for (int i = 0; i < moGridMatReqCC.getTable().getRowCount(); i++) {
                boolean found = false;
                for (SPkMatCC pk : arrpk) {
                    if (SLibUtils.compareKeys(moGridMatReqCC.getGridRow(i).getRowPrimaryKey(), pk.getPrimaryKey())){
                        miClient.showMsgBoxInformation("Ya existe el centro de costo seleccionado.");
                        moGridMatReqCC.removeGridRow(i);
                        moGridMatReqCC.renderGridRows();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    SPkMatCC pk = new SPkMatCC();
                    pk.setPrimaryKey(moGridMatReqCC.getGridRow(i).getRowPrimaryKey());
                    arrpk.add(pk);
                }
            }
        }
    }
    
    private void updateReqAsignBudgetRows(){
        double reqBudget = 0;
        double reqAsign = 0;
        if (maMatReqEntries.size() > 0) {
            for (SDbMaterialRequestEntry ety : maMatReqEntries) {
                if (!ety.isDeleted()) {
                    reqBudget += ety.getTotal_r();
                }
            }
            for (SGridRow row : moGridMatReqCC.getModel().getGridRows()) {
                SDbMaterialRequestCostCenter cc = (SDbMaterialRequestCostCenter) row;
                double rowBudget = cc.getRowConsBudWOReq();
                rowBudget += (cc.getPercentage() * reqBudget);
                cc.setRowConsBudWReq(rowBudget);
                reqAsign += cc.getPercentage();
            }
            moGridMatReqCC.renderGridRows();
        }
        
        moDecTotal.setValue(reqBudget);
        moDecAsign.setValue(reqAsign);
    }
    
    private void populateWhs() {
        if (moGridMatReqCC.getModel().getRowCount() > 0) {
            SGuiParams params = new SGuiParams(((SDbMaterialRequestCostCenter) moGridMatReqCC.getModel().getGridRows().get(0)).getPkEntMatConsumptionEntityId());
            miClient.getSession().populateCatalogue(moKeyWhs, SModConsts.CFGU_COB_ENT, SLibConsts.UNDEFINED, params);
        }
    }
    
    private void populateMatReqEntries() throws Exception {
        ArrayList<SDbMaterialRequestEntry> aux = new ArrayList<>();
        int i = 0;
        Vector<SGridRow> vRows = new Vector<>();
        if (maMatReqEntries.size() > 0) {
            for (SDbMaterialRequestEntry ety : maMatReqEntries) {
                if (!ety.isDeleted()) {
                    ety.setAuxRowId(i++);
                    aux.add(ety);
                }
            }
            vRows.addAll(aux);
        }
        moGridMatReqList.populateGrid(vRows, this); 
    }
    
    private void enableReqControls(boolean enable) {
        if (getFormSubtype() == SModConsts.TRNX_MAT_REQ_RECLASS && hasUserReclassRight && enable) {
            moTextTypeReq.setEnabled(false);
            moDecTotal.setEnabled(false);
            moKeyProvEnt.setEnabled(false);
            moKeyUsrReq.setEnabled(false);
            moKeyContractor.setEnabled(false);
            moKeyDocNature.setEnabled(enable && moKeyDocNature.getItemCount() > 2);
            moKeyItemRef.setEnabled(enable);
            jbPickItemRef.setEnabled(enable);
            moDateReq.setEnabled(false);
            moKeyPriReq.setEnabled(false);
            moTextReqNotes.setEnabled(false);
            jbReqNotes.setEnabled(false);
            jbItemStk.setEnabled(false && getFormSubtype() == SModConsts.TRNX_MAT_REQ_STK_SUP);

            jbNewEty.setEnabled(false);
            moGridMatReqCC.setRowButtonsEnabled(enable);
            moGridMatReqCC.setEnabled(enable);

            jbSave.setEnabled(enable);
            jbSaveAndSend.setEnabled(false);
        }
        else {
            moTextTypeReq.setEnabled(false);
            moDecTotal.setEnabled(false);
            moKeyProvEnt.setEnabled(enable);
            moKeyUsrReq.setEnabled(enable);
            moKeyContractor.setEnabled(enable);
            moKeyDocNature.setEnabled(enable && moKeyDocNature.getItemCount() > 2);
            moKeyItemRef.setEnabled(enable);
            jbPickItemRef.setEnabled(enable);
            moDateReq.setEnabled(enable);
            moKeyPriReq.setEnabled(enable);
            moTextReqNotes.setEnabled(enable ? !msReqNotes.isEmpty() ? !(msReqNotes.contains("\n") || msReqNotes.contains("\r\n")) : enable : false);
            jbReqNotes.setEnabled(enable);
            jbItemStk.setEnabled(enable && getFormSubtype() == SModConsts.TRNX_MAT_REQ_STK_SUP);

            jbNewEty.setEnabled(enable);
            moGridMatReqCC.setRowButtonsEnabled(enable);
            moGridMatReqCC.setEnabled(enable);

            jbSave.setEnabled(enable);
            jbSaveAndSend.setEnabled(enable);
        }
    }
    
    private void clearEntryControls() {
        moDecUnitPriceSis.setValue(0.0);
        moDecTotalEty.setValue(0.0);
        moDecUnitPriceUsr.setValue(0.0);
        mdPriceUnitaryEty = 0.0;
        moTextUnitPriceRef.setValue("");
        moTextItemKey.setValue("");
        moTextItemName.setValue("");
        moTextItemDescription.setValue("");
        moBoolNewItem.setValue(false);
        
        moDecQtyUsr.setValue(0.0);
        moTextUnitUsr.setValue("");
        moDecQty.setValue(0.0);
        moTextUnit.setValue("");
        moIntConsDays.setValue(0);
        moKeyConsEntEty.setSelectedIndex(0);
        moDateReqEty.setValue(null);
        moDateDeliveryEty.setValue(null);
        moKeyPriEty.setSelectedIndex(0);
        moTextEtyNotes.setValue("");
        msEtyNotes = "";
        moKeyItemRefEty.setSelectedIndex(0);
        moItemRefEty = null;
    }
    
    private void enableEntryControls(boolean enable) {
        if (getFormSubtype() == SModConsts.TRNX_MAT_REQ_RECLASS && hasUserReclassRight && enable) {
            moDecUnitPriceSis.setEnabled(false);
            moDecTotalEty.setEnabled(false);
            moDecUnitPriceUsr.setEnabled(false); // se deja en falso temporalmente para que tome el mismo valor que el precio de sistema, regresar a enable para funcionalidad predefinida 24/01/2024
            moTextUnitPriceRef.setEnabled(false);
            moTextItemKey.setEnabled(false);
            jbPickItem.setEnabled(false);
            moBoolNewItem.setEnabled(false); 
            moTextItemDescription.setEnabled(false);
            moDecQtyUsr.setEnabled(false);
            moIntConsDays.setEnabled(false);
            moKeyConsEntEty.setEnabled(enable);
            moKeyItemRefEty.setEnabled(enable);
            jbPickItemRefEty.setEnabled(enable);
            jbPickUnitUsr.setEnabled(false);
            moDateReqEty.setEnabled(false);
            moDateDeliveryEty.setEnabled(false);
            moKeyPriEty.setEnabled(false);
            moTextEtyNotes.setEnabled(false);
            jbEtyNotes.setEnabled(false);

            moKeyConsSubentEty.setEnabled(moKeyConsEntEty.isEnabled() && moKeyConsEntEty.getSelectedIndex() > 0);
            moKeyCostCenterEty.setEnabled(moKeyConsSubentEty.isEnabled() && moKeyConsSubentEty.getSelectedIndex() > 0);

            jbRegisterEty.setEnabled(enable);
            jbCancelEty.setEnabled(enable);
            jbNewEty.setEnabled(false);
            moTextItemRefEty.setEnabled(false);
        }
        else {
            moDecUnitPriceSis.setEnabled(false);
            moDecTotalEty.setEnabled(false);
            moDecUnitPriceUsr.setEnabled(false); // se deja en falso temporalmente para que tome el mismo valor que el precio de sistema, regresar a enable para funcionalidad predefinida 24/01/2024
            moTextUnitPriceRef.setEnabled(enable);
            moTextItemKey.setEnabled(enable);
            jbPickItem.setEnabled(enable);
            moBoolNewItem.setEnabled(enable); 
            moTextItemDescription.setEnabled(!enable ? enable : moBoolNewItem.getValue());
            moDecQtyUsr.setEnabled(enable);
            moIntConsDays.setEnabled(enable);
            moKeyConsEntEty.setEnabled(enable && getFormSubtype() != SModConsts.TRNX_MAT_REQ_STK_SUP);
            moKeyItemRefEty.setEnabled(enable);
            jbPickItemRefEty.setEnabled(enable);
            jbPickUnitUsr.setEnabled(enable);
            moDateReqEty.setEnabled(enable);
            moDateDeliveryEty.setEnabled(false);
            moKeyPriEty.setEnabled(enable);
            moTextEtyNotes.setEnabled(enable ? !msEtyNotes.isEmpty() ? !(msEtyNotes.contains("\n") || msEtyNotes.contains("\r\n")) : enable : false);
            jbEtyNotes.setEnabled(enable);

            moKeyConsSubentEty.setEnabled(isCapturingData && moKeyConsEntEty.isEnabled() && moKeyConsEntEty.getSelectedIndex() > 0);
            moKeyCostCenterEty.setEnabled(isCapturingData && moKeyConsSubentEty.isEnabled() && moKeyConsSubentEty.getSelectedIndex() > 0);

            jbRegisterEty.setEnabled(enable);
            jbCancelEty.setEnabled(enable);
            jbNewEty.setEnabled(isRegistryEditable && !enable && getFormSubtype() != SModConsts.TRNX_MAT_REQ_RECLASS);
            moTextItemRefEty.setEnabled(false);
        }
        
        if (moKeyItemRefEty.isEnabled()) {
            reloadCatalogueItemRef();
            moKeyItemRefEty.setVisible(true);
            jbPickItemRefEty.setVisible(true);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void reloadCatalogueItemRef() {
        if (moItemRefEty != null) {
            SGuiParams params = new SGuiParams();
            params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, moItemRefEty.getDbmsDataItemGeneric().getFkItemCategoryId());
            //moKeyItemRefEty.removeAllItems();
            //miClient.getSession().populateCatalogue(moKeyItemRefEty, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, params);
//            Vector<SGuiItem> items = moModule.readItems(SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, params);
//            for (SGuiItem item : items) {
//                moKeyItemRefEty.addItem(item);
//            }
            switch (moItemRefEty.getDbmsDataItemGeneric().getFkItemCategoryId()) {
                case SModSysConsts.ITMS_CT_ITEM_SAL: mnItemRefPickerSeccSelectedEty = SDialogItemPicker.SAL_ITEMS; break;
                case SModSysConsts.ITMS_CT_ITEM_ASS: mnItemRefPickerSeccSelectedEty = SDialogItemPicker.ASS_ITEMS; break;
                case SModSysConsts.ITMS_CT_ITEM_PUR: mnItemRefPickerSeccSelectedEty = SDialogItemPicker.PUR_ITEMS; break;
                case SModSysConsts.ITMS_CT_ITEM_EXP: mnItemRefPickerSeccSelectedEty = SDialogItemPicker.EXP_ITEMS; break;
            }
            if (!moMapItems.containsKey(mnItemRefPickerSeccSelectedEty)) {
                moMapItems.put(mnItemRefPickerSeccSelectedEty, moModule.readItems(SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, params)); 
            }
            moKeyItemRefEty.removeAllItems();
            for (SGuiItem item : moMapItems.get(mnItemRefPickerSeccSelectedEty)) {
                moKeyItemRefEty.addItem(item);
            }
            moKeyItemRefEty.setValue(moItemRefEty.getPrimaryKey());
        }
        else {
            moKeyItemRefEty.removeAllItems();
            if (moMapItems.containsKey(mnItemRefPickerSeccSelectedEty)) {
                for (SGuiItem item : moMapItems.get(mnItemRefPickerSeccSelectedEty)) {
                    moKeyItemRefEty.addItem(item);
                }
            }
            else {
                for (SGuiItem item : moMapItems.get(SDialogItemPicker.EXP_ITEMS)) {
                    moKeyItemRefEty.addItem(item);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void setComponetsEntryData(SDbMaterialRequestEntry ety) {
        if (ety != null) {
            moItemEty = ety.getDataItem();
            moUnitEty = ety.getDataUnitUsr();
            moItemRefEty = ety.getDataItemRef();
            
            moTextItemKey.setValue(moItemEty.getCode());
            moTextItemName.setValue(moItemEty.getName());
            moBoolNewItem.setValue(ety.isNewItem());
            
            moDecQtyUsr.setValue(ety.getUserQuantity());
            moTextUnitUsr.setValue(moUnitEty.getSymbol());
            moDecQty.setValue(ety.getQuantity());
            moTextUnit.setValue(moItemEty.getDbmsDataUnit().getSymbol());
            moDecUnitPriceSis.setValue(ety.getPriceUnitarySystem());
            moDecUnitPriceUsr.setValue(ety.getUserPriceUnitary());
            mdPriceUnitaryEty = ety.getPriceUnitary();
            
            moDecTotalEty.setValue(ety.getTotal_r());
            moIntConsDays.setValue(ety.getCosnsumptionEstimated());
            moKeyConsEntEty.setValue(new int[] { ety.getFkEntMatConsumptionEntityId_n() });
            moKeyConsSubentEty.setValue(new int[] { ety.getFkSubentMatConsumptionEntityId_n(), ety.getFkSubentMatConsumptionSubentityId_n() });
            moKeyCostCenterEty.setValue(new int[] { ety.getFkCostCenterId_n() });
            moDateReqEty.setValue(ety.getDateRequest_n());
            moDateDeliveryEty.setValue(ety.getDateDelivery_n());
            moKeyPriEty.setValue(new int[] { ety.getFkMatRequestPriorityId_n() });
            moTextItemDescription.setValue("");
            moTextEtyNotes.setValue("");
            msEtyNotes = "";
            //moKeyItemRefEty.setValue(new int[] { ety.getFkItemReferenceId_n() });
            
            moKeyItemRefEty.removeAllItems();
            moKeyItemRefEty.addItem(ety.getDataItemRef()!= null ? ety.getDataItemRef().getKey() + " - " + ety.getDataItemRef().getItem() : "");
            moKeyItemRefEty.setSelectedIndex(0);
            moKeyItemRefEty.setVisible(false);
            jbPickItemRefEty.setVisible(false);
            moTextItemRefEty.setText(ety.getDataItemRef()!= null ? ety.getDataItemRef().getKey() + " - " + ety.getDataItemRef().getItem() : "");
            moTextItemRefEty.setVisible(true);
            moTextItemRefEty.setCaretPosition(0);
            
            for (SDbMaterialRequestEntryNote note : ety.getChildNotes()) {
                if (note.getIsDescription()) {
                    moTextItemDescription.setValue(note.getNotes());
                }
                else {
                    moTextEtyNotes.setValue(note.getNotes());
                    msEtyNotes = note.getNotes();
                    moTextEtyNotes.setEnabled(!(msEtyNotes.contains("\n") || msEtyNotes.contains("\r\n")));
                }
            }
        }
        else {
            moItemEty = null;
            moItemRefEty = null;
            moTextItemKey.setValue("");
            moTextItemName.setValue("");
            moBoolNewItem.setValue(false);
            moDecQtyUsr.setValue(0.0);
            moTextUnitUsr.setValue("");
            moDecQty.setValue(0.0);
            moTextUnit.setValue("");
            moDecUnitPriceSis.setValue(0.0);
            moDecUnitPriceUsr.setValue(0.0);
            mdPriceUnitaryEty = 0.0;
            moTextUnitPriceRef.setValue("");
            moDecTotalEty.setValue(0.0);
            moIntConsDays.setValue(0);
            moKeyConsEntEty.setSelectedIndex(0);
            moDateReqEty.setValue(null);
            moDateDeliveryEty.setValue(null);
            moKeyPriEty.setSelectedIndex(0);
            moTextItemDescription.setValue("");
            moTextEtyNotes.setValue("");
            msEtyNotes = "";
            moKeyItemRefEty.setSelectedIndex(0);
            moTextItemRefEty.setText("");
        }
        isCapturingData = false;
        enableEntryControls(false);
        stateChangeConsEntEty();
    }
    
    private SDbMaterialRequestEntry setEtyValues(SDbMaterialRequestEntry ety) throws Exception {
        ety.setDateRequest_n(moDateReqEty.getValue());
        ety.setDateDelivery_n(moDateDeliveryEty.getValue());
        ety.setUserQuantity(moDecQtyUsr.getValue());
        ety.setQuantity(moDecQty.getValue());
        
        ety.setCosnsumptionEstimated(moIntConsDays.getValue());
        ety.setNewItem(moBoolNewItem.getValue());
        ety.setPriceUnitarySystem(moDecUnitPriceSis.getValue());
        ety.setUserPriceUnitary(moDecUnitPriceUsr.getValue());
        ety.setPriceUnitary(mdPriceUnitaryEty);
        ety.setTotal_r(moDecTotalEty.getValue());
        ety.setFkItemId(moItemEty.getPkItemId());
        ety.setFkUserUnitId(moUnitEty.getPkUnitId());
        ety.setFkUnitId(moItemEty.getFkUnitId());
        
        ety.setFkMatRequestPriorityId_n(moKeyPriEty.getSelectedIndex() == 0 ? 0 : moKeyPriEty.getValue()[0]);
        ety.setFkEntMatConsumptionEntityId_n(moKeyConsEntEty.getSelectedIndex() == 0 ? 0 : moKeyConsEntEty.getValue()[0]);
        ety.setFkSubentMatConsumptionEntityId_n(moKeyConsSubentEty.getSelectedIndex() <= 0 ? 0 : moKeyConsSubentEty.getValue()[0]);
        ety.setFkSubentMatConsumptionSubentityId_n(moKeyConsSubentEty.getSelectedIndex() <= 0 ? 0 : moKeyConsSubentEty.getValue()[1]);
        ety.setFkCostCenterId_n(moKeyCostCenterEty.getSelectedIndex() <= 0 ? 0 : moKeyCostCenterEty.getValue()[0] );
        ety.setFkItemReferenceId_n(moKeyItemRefEty.getSelectedIndex() <= 0 ? 0 : moKeyItemRefEty.getValue()[0]);
        ety.setDataItem(moItemEty);
        ety.setDataUnitUsr(moUnitEty);
        ety.readOptionalInfo(miClient.getSession());
        
        if (moKeyItemRefEty.getSelectedIndex() != 0) {
            SDataItem i = new SDataItem();
            i.read(moKeyItemRefEty.getValue(), miClient.getSession().getStatement());
            ety.setDataItemRef(i);
        }
        
        ety.getChildNotes().clear();
        if (moBoolNewItem.getValue()) {
            SDbMaterialRequestEntryNote note = new SDbMaterialRequestEntryNote();
            note.setNotes(moTextItemDescription.getValue());
            note.setIsDescription(true);
            ety.getChildNotes().add(note);
        }
        
        if (!moTextEtyNotes.getValue().isEmpty()) {
            SDbMaterialRequestEntryNote note = new SDbMaterialRequestEntryNote();
            note.setNotes(msEtyNotes);
            note.setIsDescription(false);
            ety.getChildNotes().add(note);
        }
        
        if (isProvPurForm && hasUserProvPurRight) {
            SDbMaterialRequestEntryItemChange ch = new SDbMaterialRequestEntryItemChange();
            ch.setFkItemId(moItemEty.getPkItemId());
            ety.getChildItemChange().add(ch);
            ety.setNewItem(false);
            jbSave.setEnabled(true);
        }
        
        return ety;
    }
    
    private int getNextNumber() {
        try {
            String sql = "SELECT COALESCE(MAX(num), 0) + 1 FROM trn_mat_req WHERE fk_mat_prov_ent = " + moKeyProvEnt.getValue()[0] + " AND NOT b_del;";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        return 0;
    }
    
    private int[] getDefaultPriority() {
        try {
            String sql = "SELECT param_value FROM cfg_param WHERE param_key = '" + SDataConstantsSys.CFG_PARAM_TRN_MAT_REQ_PTY_DEFAULT + "';";
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                return new int[] { resultSet.getInt(1) };
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
        return new int[] { 0 };
    }
    
    private void enableGridButtons() {
        if (moGridMatReqList.getTable().getSelectedRow() >= 0 && isRegistryEditable) {
            jbEditEty.setEnabled(true);
            jbDeleteEty.setEnabled(true && getFormSubtype() != SModConsts.TRNX_MAT_REQ_RECLASS);
        }
        else {
            if (moGridMatReqList.getSelectedGridRow() != null) {
                if (isProvPurForm && hasUserProvPurRight) {
                    jbEditEty.setEnabled(true);
                }
                else {
                    jbEditEty.setEnabled(false);
                }
            }
        }
    }
    
    private void stateChangeNewItem() {
        try {
            moTextItemDescription.setEnabled(moBoolNewItem.getValue());
            moTextItemKey.setEnabled(!moBoolNewItem.getValue());
            if (moBoolNewItem.getValue()) {
                String sql = "SELECT param_value FROM cfg_param WHERE param_key = '" + SDataConstantsSys.CFG_PARAM_TRN_MAT_REQ_ETY_ITEM_NEW + "';";
                ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
                if (resultSet.next()) {
                    moItemEty = new SDataItem();
                    moUnitEty = new SDataUnit();
                    moItemEty.read(new int[] { resultSet.getInt(1) }, miClient.getSession().getStatement());
                    moTextItemKey.setValue(moItemEty.getCode());
                    moTextItemName.setValue(moItemEty.getName());
                    moUnitEty.read(new int[] { moItemEty.getFkUnitId() }, miClient.getSession().getStatement());
                    moTextUnitUsr.setValue(moUnitEty.getSymbol());
                    moTextUnit.setValue(moUnitEty.getSymbol());
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void stateChangeConsEntEty() {
        if (!isRegistryEditable){
            moKeyConsSubentEty.setEnabled(false);
        }
        else if (moKeyConsEntEty.getSelectedIndex() <= 0 ) {
            if (moKeyConsSubentEty.getItemCount() > 0) moKeyConsSubentEty.setSelectedIndex(0);
            moKeyConsSubentEty.setEnabled(false);
        }
        else if (isRegistryEditable && moKeyConsEntEty.getSelectedIndex() > 0 && isCapturingData) {
            moKeyConsSubentEty.setEnabled(true);
        }
    }
    
    private void stateChangeConsSubentEty() {
        if (moKeyConsSubentEty.getSelectedIndex() <= 0) {
            moKeyCostCenterEty.removeAllItems();
            moKeyCostCenterEty.setEnabled(false);
        }
        else {
            SGuiParams params = new SGuiParams();
            params.getParamsMap().put(SModConsts.USRU_USR, miClient.getSession().getUser().getPkUserId());
            params.getParamsMap().put(SModConsts.TRN_MAT_CONS_SUBENT, moKeyConsSubentEty.getValue());
            miClient.getSession().populateCatalogue(moKeyCostCenterEty, SModConsts.FIN_CC, SModConsts.TRN_MAT_REQ, params);
            moKeyCostCenterEty.setEnabled(isCapturingData);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void actionItemStk() {
        try {
            if (jbItemStk.isEnabled()) {
                if (moGridMatReqCC.getModel().getRowCount() != 1) {
                    miClient.showMsgBoxInformation("Debe haber un centro de consumo elegido.");
                }
                else {
                    SDialogMaterialRequestItemSupply sup = new SDialogMaterialRequestItemSupply(miClient, "Selección de ítems por máximos y mínimos");
                    sup.setValue(SDialogMaterialRequestItemSupply.PARAM_CONS_ENT_ID, 
                            ((SDbMaterialRequestCostCenter) moGridMatReqCC.getModel().getGridRows().get(0)).getPkEntMatConsumptionEntityId());
                    sup.setValue(SDialogMaterialRequestItemSupply.PARAM_WAH_ID, moWahId);
                    sup.prepareDialog();
                    sup.setVisible(true);
                    if (sup.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                        moWahId = (int[]) sup.getValue(SDialogMaterialRequestItemSupply.PARAM_WAH_ID);
                        moKeyWhs.setValue(moWahId);
                        moKeyWhs.setEnabled(false);
                        maMatReqItemSupply = (ArrayList<SRowMaterialRequestItemSupply>) sup.getValue(SDialogMaterialRequestItemSupply.PARAM_ARR_ITM_SELECTED);
                        for(SRowMaterialRequestItemSupply is : maMatReqItemSupply) {
                            SDbMaterialRequestEntry ety = new SDbMaterialRequestEntry();
                            SDataItem item = new SDataItem();
                            item.read(new int[] { is.mnItemId }, miClient.getSession().getStatement());
                            ety.setDataItem(item);
                            ety.setDataUnitUsr(item.getDbmsDataUnit());
                            ety.setFkUserUnitId(item.getFkUnitId());
                            ety.setQuantity(is.mdQty);
                            ety.setNewItem(false);
                            ety.setPriceUnitarySystem(1.0);
                            ety.setPriceUnitary(1.0);
                            ety.setTotal_r(is.mdQty);
                            ety.setFkItemId(is.mnItemId);
                            ety.setFkUnitId(is.mnUnitId);
                            maMatReqEntries.add(ety);
                        }
                        populateMatReqEntries();
                        updateReqAsignBudgetRows();
                    }
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage()); 
        }
    }
    
    private void actionPickItem() {
        try {
            int[] itemId; 
            
            if (moGridMatReqCC.getTable().getRowCount() >= 1 || moKeyCostCenterEty.getSelectedIndex() >= 1) {
                ArrayList<SDbMaterialCostCenterGroup> ccg = new ArrayList<>();
                for (SGridRow row : moGridMatReqCC.getModel().getGridRows()) {
                    SDbMaterialRequestCostCenter cc = (SDbMaterialRequestCostCenter) row;
                    ccg = SMaterialRequestUtils.getCostCenterGroupByUser(miClient.getSession(), 
                            new int[] { cc.getPkSubentMatConsumptionEntityId(), cc.getPkSubentMatConsumptionSubentityId() }, cc.getPkCostCenterId());
                }
                if (ccg.isEmpty()) {
                    int[] pkConsSubent = moKeyConsSubentEty.getValue();
                    int pkCc = moKeyCostCenterEty.getValue()[0];
                    ccg = SMaterialRequestUtils.getCostCenterGroupByUser(miClient.getSession(), 
                            pkConsSubent, pkCc);
                }
                
                SGuiParams params = new SGuiParams();
                params.getParamsMap().put(SModConsts.USRU_USR, miClient.getSession().getUser().getPkUserId());
                params.getParamsMap().put(SModConsts.TRN_MAT_CC_GRP, ccg);
                
                if (moGridMatReqList.getTable().getRowCount() > 0) {
                    params.getParamsMap().put(SModSysConsts.ITMU_ITEM_INV, isReqInv);
                }

                if (moDialogPickerItem == null || mbCcChanged) {
                    moDialogPickerItem = SMaterialRequestUtils.getOptionItemPicker(miClient, SModConsts.ITMU_ITEM, SModConsts.TRN_MAT_REQ, params);
                    moDialogPickerItem.resetPicker();
                    moDialogPickerItem.initComponentsCustom();
                    moDialogPickerItem.setShowedItems("Ítems del (los) centro(s) de consumo: " + getShowedItems());
                    mbCcChanged = false;
                    //moDialogPickerItem.setItemPickerInvDefault(true);
                }
                
                moDialogPickerItem.setDefaultEnableRadio(SDialogItemPicker.INV_ITEMS);
                moDialogPickerItem.moSearchItem.setText("");
                moDialogPickerItem.setPickerVisible(true);

                if (moDialogPickerItem.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
                    itemId = (int[]) moDialogPickerItem.getOption();

                    if (itemId != null) {
                        readItemEty(itemId);
                    }
                }
            }
            else {
                miClient.showMsgBoxInformation("Debe seleccionar un centro de costo ya sea a nivel de partida o a nivel de requisición.");
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private String getShowedItems() {
        String text = "";
        for (SGridRow row : moGridMatReqCC.getModel().getGridRows()) {
            SDbMaterialRequestCostCenter cc = (SDbMaterialRequestCostCenter) row;
            text += text.isEmpty() ? "" : ", ";
            text += cc.getDbmsConsSubent().getXtaConsumptionEntityName();
        }
        return text;
    }
    
    private void readItemEty(int[] itemId) {
        moItemEty = new SDataItem();
        moUnitEty = new SDataUnit();
        moItemEty.read(itemId, miClient.getSession().getStatement());
        
        if (moGridMatReqList.getTable().getRowCount() == 0) {
            isReqInv = moItemEty.getIsInventoriable();
        }
        
        moTextItemKey.setValue(moItemEty.getKey());
        moTextItemName.setValue(moItemEty.getName());
        moUnitEty.read(new int[] { moItemEty.getFkUnitId() }, miClient.getSession().getStatement());
        moTextUnitUsr.setValue(moUnitEty.getSymbol());
        moTextUnit.setValue(moUnitEty.getSymbol());
        moKeyItemRefEty.setValue(moKeyItemRef.getValue()[0] != 0 ? new int[] { 0 } : new int[] { moItemEty.getDbmsFkDefaultItemRefId_n() });
        obtainItemPrice();
    }
    
    private void obtainItemPrice() {
        double price = SMaterialRequestUtils.getItemPriceCommercial(miClient.getSession(), moItemEty.getPkItemId(), moUnitEty.getPkUnitId());
        moDecUnitPriceSis.setValue(price);
        moDecUnitPriceUsr.setValue(price);
        actionUnitPriceUsr();
        actionUpdateQtyPrice();
    }
    
    private void actionPickItemRef() {
        int[] itemId; 
        
        if (moDialogPickerItemRef == null) {
            moDialogPickerItemRef = SMaterialRequestUtils.getOptionItemPicker(miClient, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, null);
            moDialogPickerItemRef.resetPicker();
            moDialogPickerItemRef.initComponentsCustom();
        }
        if (mnItemRefPickerSeccSelected == 0) {
            mnItemRefPickerSeccSelected = SDialogItemPicker.EXP_ITEMS;
        }
        
        //moDialogPickerItemRef.setItemPickerInvDefault(false);
        moDialogPickerItemRef.setDefaultEnableRadio(mnItemRefPickerSeccSelected);
        moDialogPickerItemRef.setOption(moKeyItemRef.getValue());
        moDialogPickerItemRef.moSearchItem.setText("");
        moDialogPickerItemRef.setPickerVisible(true);

        if (moDialogPickerItemRef.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            itemId = (int[]) moDialogPickerItemRef.getOption();

            if (itemId != null) {
                SGuiParams params = new SGuiParams();
                switch (moDialogPickerItemRef.getSetectedMode()) {
                    case SDialogItemPicker.INV_ITEMS:
                        params.getParamsMap().put(SModSysConsts.ITMU_ITEM_INV, true);
                        mnItemRefPickerSeccSelected = SDialogItemPicker.INV_ITEMS;
                        break;
                    case SDialogItemPicker.NOINV_ITEMS:
                        params.getParamsMap().put(SModSysConsts.ITMU_ITEM_INV, false);
                        mnItemRefPickerSeccSelected = SDialogItemPicker.NOINV_ITEMS;
                        break;
                    case SDialogItemPicker.SAL_ITEMS:
                        params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_SAL);
                        mnItemRefPickerSeccSelected = SDialogItemPicker.SAL_ITEMS;
                        break;
                    case SDialogItemPicker.ASS_ITEMS:
                        params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_ASS);
                        mnItemRefPickerSeccSelected = SDialogItemPicker.ASS_ITEMS;
                        break;
                    case SDialogItemPicker.PUR_ITEMS:
                        params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_PUR);
                        mnItemRefPickerSeccSelected = SDialogItemPicker.PUR_ITEMS;
                        break;
                    case SDialogItemPicker.EXP_ITEMS:
                        params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_EXP);
                        mnItemRefPickerSeccSelected = SDialogItemPicker.EXP_ITEMS;
                        break;
                }
                miClient.getSession().populateCatalogue(moKeyItemRef, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, params);
                moKeyItemRef.setValue(itemId);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private void actionPickItemRefEty() {
        int[] itemId; 
        
        if (moDialogPickerItemRefEty == null) {
            moDialogPickerItemRefEty = SMaterialRequestUtils.getOptionItemPicker(miClient, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, null);
            moDialogPickerItemRefEty.resetPicker();
            moDialogPickerItemRefEty.initComponentsCustom();
        }
        
        if (mnItemRefPickerSeccSelectedEty == 0) {
            mnItemRefPickerSeccSelectedEty = SDialogItemPicker.EXP_ITEMS;
        }
        //moDialogPickerItemRef.setItemPickerInvDefault(false);
        moDialogPickerItemRefEty.setDefaultEnableRadio(mnItemRefPickerSeccSelectedEty);
        moDialogPickerItemRefEty.setOption(moKeyItemRefEty.getValue());
        moDialogPickerItemRefEty.moSearchItem.setText("");
        moDialogPickerItemRefEty.setPickerVisible(true);

        if (moDialogPickerItemRefEty.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            itemId = (int[]) moDialogPickerItemRefEty.getOption();

            if (itemId != null) {
                SGuiParams params = new SGuiParams();
                switch (moDialogPickerItemRefEty.getSetectedMode()) {
                    case SDialogItemPicker.INV_ITEMS:
                        params.getParamsMap().put(SModSysConsts.ITMU_ITEM_INV, true);
                        mnItemRefPickerSeccSelectedEty = SDialogItemPicker.INV_ITEMS;
                        break;
                    case SDialogItemPicker.NOINV_ITEMS:
                        params.getParamsMap().put(SModSysConsts.ITMU_ITEM_INV, false);
                        mnItemRefPickerSeccSelectedEty = SDialogItemPicker.NOINV_ITEMS;
                        break;
                    case SDialogItemPicker.SAL_ITEMS:
                        params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_SAL);
                        mnItemRefPickerSeccSelectedEty = SDialogItemPicker.SAL_ITEMS;
                        break;
                    case SDialogItemPicker.ASS_ITEMS:
                        params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_ASS);
                        mnItemRefPickerSeccSelectedEty = SDialogItemPicker.ASS_ITEMS;
                        break;
                    case SDialogItemPicker.PUR_ITEMS:
                        params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_PUR);
                        mnItemRefPickerSeccSelectedEty = SDialogItemPicker.PUR_ITEMS;
                        break;
                    case SDialogItemPicker.EXP_ITEMS:
                        params.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_EXP);
                        mnItemRefPickerSeccSelectedEty = SDialogItemPicker.EXP_ITEMS;
                        break;
                }
                //miClient.getSession().populateCatalogue(moKeyItemRefEty, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, params);
//                Vector<SGuiItem> items = moModule.readItems(SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, params);
//                for (SGuiItem item : items) {
//                    moKeyItemRefEty.addItem(item);
//                }
                if (!moMapItems.containsKey(mnItemRefPickerSeccSelectedEty)) {
                    moMapItems.put(mnItemRefPickerSeccSelectedEty, moModule.readItems(SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, params)); 
                }
                moKeyItemRefEty.removeAllItems();
                for (SGuiItem item : moMapItems.get(mnItemRefPickerSeccSelectedEty)) {
                    moKeyItemRefEty.addItem(item);
                }
                
                moKeyItemRefEty.setValue(itemId);
            }
        }
    }
    
    private void actionPickUnit() {
        int[] unitId;
        
        if (moDialogPickerUnit == null) {
        }
        SGuiParams params = new SGuiParams();
        params.getParamsMap().put(0, moItemEty.getFkUnitId());
        params.getParamsMap().put(1, moItemEty.getPkItemId());
        moDialogPickerUnit = SMaterialRequestUtils.getOptionUnitPicker(miClient, SModConsts.ITMU_UNIT, SMaterialRequestUtils.EQUIVALENCES, params);
        
        moDialogPickerUnit.resetPicker();
        moDialogPickerUnit.initComponentsCustom();
        moDialogPickerUnit.setPickerVisible(true);
        
        if (moDialogPickerUnit.getPickerResult() == SGuiConsts.FORM_RESULT_OK) {
            unitId = (int[]) moDialogPickerUnit.getOption();
            
            if (unitId != null) {
                moUnitEty.read(unitId, miClient.getSession().getStatement());
                moTextUnitUsr.setValue(moUnitEty.getSymbol());
                obtainItemPrice();
            }
        }
    }
    
    private void actionItemKey() {
        try {
            if (moTextItemKey.getValue() == null || moTextItemKey.getValue().isEmpty()) {
                return;
            }
            
            String sql2 = "SELECT * FROM trn_mat_cc_grp_item AS cgi " +
                    "INNER JOIN trn_mat_cc_grp_usr AS cgu ON " +
                    "cgi.id_mat_cc_grp = cgu.id_mat_cc_grp " +
                    "WHERE cgu.id_link = " + SModSysConsts.USRS_LINK_USR + " " +
                    "AND cgu.id_ref = " + miClient.getSession().getUser().getPkUserId() + " ";
            
            String sql = "SELECT id_item, fid_igen, item FROM erp.itmu_item WHERE item_key = '" + moTextItemKey.getValue() + "' ";
            Statement statement = miClient.getSession().getDatabase().getConnection().createStatement();
            
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                int idItem = resultSet.getInt(1);
                int idIgen = resultSet.getInt(2);
                String item = resultSet.getString(3);
                
                sql = sql2 + "AND cgi.id_link = " + SModSysConsts.ITMS_LINK_ITEM + " and cgi.id_ref = " + idItem;
                ResultSet resultSet2 = statement.executeQuery(sql);
                if (resultSet2.next()) {
                    readItemEty(new int[] { idItem });
                }
                else {
                    sql = sql2 + "AND cgi.id_link = " + SModSysConsts.ITMS_LINK_IGEN + " and cgi.id_ref = " + idIgen;
                    resultSet2 = statement.executeQuery(sql);
                    if (resultSet2.next()) {
                        readItemEty(new int[] { idItem });
                    }
                    else {
                        miClient.showMsgBoxInformation("No tiene acceso al ítem " + item + ".");
                    }
                }
            }
            else {
                miClient.showMsgBoxInformation("No hay ningún ítem con el código " + moTextItemKey.getValue() + ".");
            }
        }
        catch(Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionUpdateQtyPrice() {
        if (!SLibUtils.compareKeys(moItemEty.getFkUnitId(), moUnitEty.getPkUnitId())) {
            int nDecsQty = ((SClientInterface) miClient).getSessionXXX().getParamsErp().getDecimalsQuantity();
            double dTot = moDecUnitPriceUsr.getValue() * moDecQtyUsr.getValue();
            
            double dFactQty = ((SSessionCustom) miClient.getSession().getSessionCustom()).getUnitsFactorForQuantity(moItemEty.getPkItemId(), moUnitEty.getPkUnitId(), moItemEty.getFkUnitId());
            
            double mdQuantity = SLibUtilities.round(moDecQtyUsr.getValue() * dFactQty, nDecsQty);
            
            mdPriceUnitaryEty = SLibUtilities.round(dTot / mdQuantity, nDecsQty);
            moDecQty.setValue(mdQuantity);
        }
        else {
            moDecQty.setValue(moDecQtyUsr.getValue());
            mdPriceUnitaryEty = moDecUnitPriceUsr.getValue();
        }
    }
    
    private void actionUnitPriceUsr() {
        moDecTotalEty.setValue(SLibUtils.roundAmount(moDecQtyUsr.getValue() * moDecUnitPriceUsr.getValue()));
    }
    
    private void actionItemDesc() {
        moTextItemDescription.setToolTipText(moTextItemDescription.getText());
    }
    
    private void actionNew() {
        isEtyNew = true;
        isCapturingData = true;
        clearEntryControls();
        enableEntryControls(true);
        jbEditEty.setEnabled(false);
        jbDeleteEty.setEnabled(false);
    }
    
    private void actionRegister() {
        try {
            SGuiValidation validation = moFieldsEty.validateFields();
            
            if (SGuiUtils.computeValidation(miClient, validation)) {
                if (isEtyNew) {
                    SDbMaterialRequestEntry ety = new SDbMaterialRequestEntry();
                    ety = setEtyValues(ety);
                    ety.setRegistryNew(true);
                    maMatReqEntries.add(ety);
                }
                else {
                    if (isProvPurForm) {
                        for (SDbMaterialRequestEntry ety : maMatReqEntries) {
                            if (SLibUtils.compareKeys(ety.getPrimaryKey(), ((SDbMaterialRequestEntry) moGridMatReqList.getSelectedGridRow()).getPrimaryKey())) {
                                ety.setRegistryNew(false);
                                setEtyValues(ety);
                            }
                        }
                    }
                    else {
                        int rowId = ((SDbMaterialRequestEntry) moGridMatReqList.getSelectedGridRow()).getAuxRowId();
                        for (SDbMaterialRequestEntry ety : maMatReqEntries) {
                            if (ety.getAuxRowId() == rowId) {
                                ety.setRegistryNew(false);
                                setEtyValues(ety);
                            }
                        }
                    }
                }
                
                populateMatReqEntries();
                isCapturingData = false;
                enableEntryControls(false);
                updateReqAsignBudgetRows();
            }
            else {
                miClient.showMsgBoxInformation(validation.getMessage());
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionEditEty() {
        if (isRegistryEditable) {
            isEtyNew = false;
            isCapturingData = true;
            jbEditEty.setEnabled(false);
            jbDeleteEty.setEnabled(false);
            enableEntryControls(true);
        }
        else if (isProvPurForm && hasUserProvPurRight) {
            jbPickItem.setEnabled(!((SDbMaterialRequestEntry) moGridMatReqList.getSelectedGridRow()).getItemNewDescription().isEmpty() && !hasLinkMatReq);
            moDateDeliveryEty.setEnabled(isPurForm && hasUserPurRight);
            jbRegisterEty.setEnabled(true);
            jbCancelEty.setEnabled(true);
            jbEditEty.setEnabled(false);
        }
    }
    
    private void actionDeleteEty() {
        try {
            int rowId = ((SDbMaterialRequestEntry) moGridMatReqList.getSelectedGridRow()).getAuxRowId();
            for (SDbMaterialRequestEntry ety : maMatReqEntries) {
                if (ety.getAuxRowId() == rowId) {
                    ety.setDeleted(true);
                }
            }
            populateMatReqEntries();
            isCapturingData = false;
            enableEntryControls(false);
        }
        catch (Exception e) {
            miClient.showMsgBoxError(e.getMessage());
        }
    }
    
    private void actionCancelEty() {
        if (isCapturingData) {
            if (miClient.showMsgBoxConfirm("¿Esta seguro(a) que desea cancelar la captura de la partida?") == JOptionPane.OK_OPTION) {
                isCapturingData = false;
                clearEntryControls();
                enableEntryControls(false);
                jbEditEty.setEnabled(true);
                jbDeleteEty.setEnabled(true);
            }
        }
    }
    
    private void actionSave(int statusReq) {
        mnStatusReqId = statusReq;
        super.actionSave();
    }
    
    @Override
    public void actionCancel() {
        if (jbCancel.isEnabled()) {
            if (isRegistryEditable) {
                if (miClient.showMsgBoxConfirm("¿Esta seguro(a) que desea cerrar la captura sin guardar la requisición de materiales?") == JOptionPane.OK_OPTION)
                    super.actionCancel();
            }
            else {
                super.actionCancel();
            }
        }
    }
    
    private void actionAuthorizeOrRejectResource(final int iAction) {
        try {
            String response = SAuthorizationUtils.authorizeOrReject(miClient.getSession(), 
                                                                    moRegistry.getPrimaryKey(), 
                                                                    SAuthorizationUtils.AUTH_TYPE_MAT_REQUEST, 
                                                                    iAction);
            
            if (response.length() > 0) {
                miClient.showMsgBoxError(response);
            }
            else {
                miClient.showMsgBoxInformation((iAction == SAuthorizationUtils.AUTH_ACTION_AUTHORIZE ? "Autorizado" : "Rechazado") + 
                        " con éxito");
                jbSave.setEnabled(true);
                super.actionSave();
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionReqNotes() {
        if (moFormCapturingNotesReq == null) {
            moFormCapturingNotesReq = new SFormCapturingNotes((SClientInterface) miClient);
        }
        if (moTextReqNotes.isEnabled()) {
            moFormCapturingNotesReq.setValue(SFormCapturingNotes.NOTE_TEXT, moTextReqNotes.getValue());
        }
        else {
            moFormCapturingNotesReq.setValue(SFormCapturingNotes.NOTE_TEXT, msReqNotes);
        }
        moFormCapturingNotesReq.setValue(SFormCapturingNotes.NOTE_LEN, 1000);
        moFormCapturingNotesReq.setFormVisible(true);
        if (moFormCapturingNotesReq.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            msReqNotes = (String) moFormCapturingNotesReq.getValue(SFormCapturingNotes.NOTE_TEXT);
            moTextReqNotes.setValue(msReqNotes);
            moTextReqNotes.setEnabled(!(msReqNotes.contains("\n") || msReqNotes.contains("\r\n")));
        }
    }
    
    private void actionEtyNotes() {
        if (moFormCapturingNotesEty == null) {
            moFormCapturingNotesEty = new SFormCapturingNotes((SClientInterface) miClient);
        }
        if (moTextEtyNotes.isEnabled()) {
            moFormCapturingNotesEty.setValue(SFormCapturingNotes.NOTE_TEXT, moTextEtyNotes.getValue());
        }
        else {
            moFormCapturingNotesEty.setValue(SFormCapturingNotes.NOTE_TEXT, msEtyNotes);
        }
        moFormCapturingNotesEty.setValue(SFormCapturingNotes.NOTE_LEN, 1000);
        moFormCapturingNotesEty.setVisible(true);
        if (moFormCapturingNotesEty.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            msEtyNotes = (String) moFormCapturingNotesEty.getValue(SFormCapturingNotes.NOTE_TEXT);
            moTextEtyNotes.setValue(msEtyNotes);
            moTextEtyNotes.setEnabled(!(msEtyNotes.contains("\n") || msEtyNotes.contains("\r\n")));
        }
    }

    @Override
    public void addAllListeners() {
        jbImport.addActionListener(this);
        jbPickItem.addActionListener(this);
        jbNewEty.addActionListener(this);
        jbRegisterEty.addActionListener(this);
        jbEditEty.addActionListener(this);
        jbDeleteEty.addActionListener(this);
        jbCancelEty.addActionListener(this);
        jbAuthorize.addActionListener(this);
        jbReject.addActionListener(this);
        jbReqNotes.addActionListener(this);
        jbEtyNotes.addActionListener(this);
        jbItemStk.addActionListener(this);
        moTextReqNotes.addFocusListener(this);
        moTextEtyNotes.addFocusListener(this);
        jbSave.addActionListener(this);
        jbSaveAndSend.addActionListener(this);
        moBoolNewItem.addItemListener(this);
        moTextItemKey.addFocusListener(this);
        moDecQtyUsr.addFocusListener(this);
        moDecUnitPriceUsr.addFocusListener(this);
        moKeyConsSubentEty.addItemListener(this);
        jbPickItemRef.addActionListener(this);
        jbPickItemRefEty.addActionListener(this);
        jbPickUnitUsr.addActionListener(this);
        moTextItemDescription.addFocusListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbImport.removeActionListener(this);
        jbPickItem.removeActionListener(this);
        jbNewEty.removeActionListener(this);
        jbRegisterEty.removeActionListener(this);
        jbEditEty.removeActionListener(this);
        jbDeleteEty.removeActionListener(this);
        jbCancelEty.removeActionListener(this);
        jbAuthorize.removeActionListener(this);
        jbReject.removeActionListener(this);
        jbReqNotes.removeActionListener(this);
        jbEtyNotes.removeActionListener(this);
        jbItemStk.removeActionListener(this);
        moTextReqNotes.removeFocusListener(this);
        moTextEtyNotes.removeFocusListener(this);
        jbSave.removeActionListener(this);
        jbSaveAndSend.removeActionListener(this);
        moBoolNewItem.removeItemListener(this);
        moTextItemKey.removeFocusListener(this);
        moDecQtyUsr.removeFocusListener(this);
        moDecUnitPriceUsr.removeFocusListener(this);
        moKeyConsSubentEty.removeItemListener(this);
        jbPickItemRef.removeActionListener(this);
        jbPickItemRefEty.removeActionListener(this);
        jbPickUnitUsr.removeActionListener(this);
        moTextItemDescription.removeFocusListener(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reloadCatalogues() {
        SGuiParams params = new SGuiParams();
        params.getParamsMap().put(SModConsts.USRU_USR, miClient.getSession().getUser().getPkUserId());
        SGuiParams paramsItemRef = new SGuiParams();
        paramsItemRef.getParamsMap().put(SModConsts.ITMS_CT_ITEM, mnItemRefCt);
        SGuiParams paramsItemRefEty = new SGuiParams();
        paramsItemRefEty.getParamsMap().put(SModConsts.ITMS_CT_ITEM, SModSysConsts.ITMS_CT_ITEM_EXP);

        moFieldKeyConsEntityEty.initGroup();
        moFieldKeyConsEntityEty.addFieldKey(moKeyConsEntEty, SModConsts.TRN_MAT_CONS_ENT, hasUserProvRight || hasUserRevRight ? SLibConsts.UNDEFINED : SModConsts.USRU_USR, params);
        moFieldKeyConsEntityEty.addFieldKey(moKeyConsSubentEty, SModConsts.TRN_MAT_CONS_SUBENT, hasUserProvRight || hasUserRevRight ? SLibConsts.UNDEFINED : SModConsts.USRU_USR, params);
        moFieldKeyConsEntityEty.populateCatalogues();
        moTextItemRefEty.setText("");
        
        miClient.getSession().populateCatalogue(moKeyProvEnt, SModConsts.TRN_MAT_PROV_ENT, hasUserProvRight || hasUserRevRight ? SLibConsts.UNDEFINED : SModConsts.USRU_USR, params);
        miClient.getSession().populateCatalogue(moKeyUsrReq, SModConsts.USRU_USR, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyContractor, SModConsts.TRN_MAINT_USER, SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR, null);
        miClient.getSession().populateCatalogue(moKeyDocNature, SModConsts.TRNU_DPS_NAT, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyPriReq, SModConsts.TRNU_MAT_REQ_PTY, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyPriEty, SModConsts.TRNU_MAT_REQ_PTY, SLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyItemRef, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, paramsItemRef);
        //miClient.getSession().populateCatalogue(moKeyItemRefEty, SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, paramsItemRefEty);
        
        moMapItems = new HashMap<>();
        moMapItems.put(SDialogItemPicker.EXP_ITEMS, moModule.readItems(SModConsts.ITMU_ITEM, SLibConsts.UNDEFINED, paramsItemRefEty));
        for (SGuiItem item : moMapItems.get(SDialogItemPicker.EXP_ITEMS)) {
            moKeyItemRefEty.addItem(item);
        }
        
        mnItemRefPickerSeccSelected = 0;
        mnItemRefPickerSeccSelectedEty = 0;
        isReqInv = false;
        mbCcChanged = false;
        
        msReqNotes = "";
        msEtyNotes = "";
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        moRegistry = (SDbMaterialRequest) registry;

        mnFormResult = SLibConsts.UNDEFINED;
        mbFirstActivation = true;

        if (moRegistry.isRegistryNew()) {
            mnItemRefCt = SModSysConsts.ITMS_CT_ITEM_EXP;
        }
        else {
            mnItemRefCt = moRegistry.getDbmsItemRef().getDbmsDataItemGeneric().getFkItemCategoryId();
        }
        
        removeAllListeners();
        reloadCatalogues();
        clearEntryControls();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            moRegistry.setDate(miClient.getSession().getSystemDate());
            jtfRegistryKey.setText("");
            moKeyUsrReq.setValue(new int[] { miClient.getSession().getUser().getPkUserId() });
            moKeyPriReq.setValue(getDefaultPriority());
            if (moKeyProvEnt.getItemCount() == 2) {
                moKeyProvEnt.setSelectedIndex(1);
            } 
            if (getFormSubtype() == SModConsts.TRNX_MAT_REQ_STK_SUP) {
                moTextTypeReq.setValue(SModSysConsts.TRNS_MAT_REQ_TP_R);
            }
            else {
                moTextTypeReq.setValue(SModSysConsts.TRNS_MAT_REQ_TP_C);
            }
            if (moKeyDocNature.getItemCount() == 2) {
                moKeyDocNature.setValue(new int[] { 1 });
            }
        }
        else {
            jtfRegistryKey.setText(SLibUtils.textKey(moRegistry.getPrimaryKey()));
            moKeyUsrReq.setValue(new int[] { moRegistry.getFkUserRequesterId() });
            moKeyPriReq.setValue(new int[] { moRegistry.getFkMatRequestPriorityId() });
            moKeyProvEnt.setValue(new int[] { moRegistry.getFkMatProvisionEntityId() });
            moTextTypeReq.setValue(moRegistry.getTypeRequest());
            moKeyDocNature.setValue(new int[] { moRegistry.getFkDpsNatureId() });
        }

        moDecQtyUsr.setValue(moRegistry.getTotal_r());
        moIntNumber.setValue(moRegistry.getNumber());
        moTextDate.setValue(SLibUtils.DateFormatDate.format(moRegistry.getDate()));
        moKeyContractor.setValue(new int[] { moRegistry.getFkContractorId_n() });
        moTextReferecnce.setValue(moRegistry.getReference());
        moKeyItemRef.setValue(new int[] { moRegistry.getFkItemReferenceId_n() });
        moDateReq.setValue(moRegistry.getDateRequest_n());
        moDateDelivery.setValue(moRegistry.getDateDelivery_n());
        moTextReqStatus.setValue(moRegistry.getAuxReqStatus());
        
        moWahId = new int[] { moRegistry.getFkWarehouseCompanyBranch_n(), moRegistry.getFkWarehouseWarehouse_n() };
        
        moBoolProvClosed.setValue(moRegistry.isCloseProvision());
        moBoolPurClosed.setValue(moRegistry.isClosePurchase());
        
        maMatReqCC = new ArrayList<>();
        maMatReqCC = moRegistry.getChildCostCenters();
        maMatReqNotes = new ArrayList<>();
        maMatReqNotes = moRegistry.getChildNotes();
        maMatReqEntries = new ArrayList<>();
        maMatReqEntries = moRegistry.getChildEntries();
        
        if (!maMatReqEntries.isEmpty()) {
            isReqInv = maMatReqEntries.get(0).getDataItem().getIsInventoriable();
        }
        
        populateMatReqCC();
        
        if (maMatReqNotes.size() > 0) {
            moTextReqNotes.setValue(maMatReqNotes.get(0).getNotes());
            msReqNotes = maMatReqNotes.get(0).getNotes();
        }
        else {
            moTextReqNotes.setValue("");
            msReqNotes = "";
        }
        
        setFormEditable(true);

        if (moRegistry.isRegistryNew()) { 
            enableReqControls(true);
            isRegistryEditable = true;
        }
        else if (moRegistry.getFkMatRequestStatusId() == SModSysConsts.TRNS_ST_MAT_REQ_NEW || getFormSubtype() == SModConsts.TRNX_MAT_REQ_RECLASS) {
            enableReqControls(true);
            isRegistryEditable = true;
        }
        else {
            enableReqControls(false);
            isRegistryEditable = false;
        }
        
        moDateDelivery.setEnabled(moRegistry.getFkMatRequestStatusId() == SModSysConsts.TRNS_ST_MAT_REQ_PUR);
        
        moTextAuthStatus.setValue(moRegistry.getAuxAuthStatus());
        
        if (getFormSubtype() == SModConsts.TRNX_MAT_REQ_PEND_SUP || 
            getFormSubtype() == SModConsts.TRNX_MAT_REQ_PEND_PUR ||
            getFormSubtype() == SModConsts.TRNX_MAT_REQ_EST) {
            jbSave.setEnabled(getFormSubtype() == SModConsts.TRNX_MAT_REQ_PEND_PUR);
            jbSaveAndSend.setEnabled(false);
            hasLinkMatReq = !SMaterialRequestUtils.hasLinksMaterialRequest(miClient.getSession(), moRegistry.getPrimaryKey()).isEmpty();
        }
        
        populateWhs();
        if (!moRegistry.isRegistryNew()) {
            jbAuthorize.setEnabled(hasUserRevRight && !isProvPurForm);
            jbReject.setEnabled(hasUserRevRight && !isProvPurForm);
            if (moRegistry.getTypeRequest().equals(SModSysConsts.TRNS_MAT_REQ_TP_R)){
                moGridMatReqList.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlKeyWhs);
                moGridMatReqList.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moKeyWhs);
                moKeyWhs.setValue(new int[] { moRegistry.getFkWarehouseCompanyBranch_n(), moRegistry.getFkWarehouseWarehouse_n() });
                moKeyWhs.setEnabled(false);
            } 
        }
        
        if (getFormSubtype() == SModConsts.TRNX_MAT_REQ_STK_SUP) {
            moGridMatReqList.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jlKeyWhs);
            moGridMatReqList.getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moKeyWhs);
        }
        
        addAllListeners();
        populateMatReqEntries();
        enableEntryControls(false);
        moBoolProvClosed.setEnabled(false);
        moKeyUsrReq.setEnabled(false);
        moTextDate.setEnabled(false);
        enableGridButtons();
        updateReqAsignBudgetRows();
        
        isEtyNew = false;
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        SDbMaterialRequest registry = (SDbMaterialRequest) moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setFkMatProvisionStatusId(SModSysConsts.TRNS_ST_MAT_PROV_NA);
            registry.setFkMatPurchaseStatusId(SModSysConsts.TRNS_ST_MAT_PUR_NA);
        }
        if (registry.isRegistryNew() || moRegistry.getFkMatProvisionEntityId() != moKeyProvEnt.getValue()[0]) {
            registry.setNumber(getNextNumber());
        }
        
        String[] date = moTextDate.getValue().split("/");
        Date d = SLibTimeUtils.createDate(SLibUtils.parseInt(date[2]), SLibUtils.parseInt(date[1]), SLibUtils.parseInt(date[0]));
        
        registry.setTypeRequest(moTextTypeReq.getValue());
        registry.setDate(d);
        registry.setDateRequest_n(moDateReq.getValue());
        registry.setDateDelivery_n(moDateDelivery.getValue());
        registry.setReference(moTextReferecnce.getValue());
        registry.setExternalSystemId("");
        registry.setTotal_r(moDecTotal.getValue());
        registry.setCloseProvision(moBoolProvClosed.getValue());
        registry.setFkMatProvisionEntityId(moKeyProvEnt.getValue()[0]);
        registry.setFkMatRequestPriorityId(moKeyPriReq.getValue()[0]);
        if (!isProvPurForm) {
            registry.setFkMatRequestStatusId(mnStatusReqId);
        }
        registry.setFkUserRequesterId(moKeyUsrReq.getValue()[0]);
        registry.setFkContractorId_n(moKeyContractor.getSelectedIndex() == 0 ? 0 : moKeyContractor.getValue()[0]);
        registry.setFkDpsNatureId(moKeyDocNature.getValue()[0]);
        registry.setFkWarehouseCompanyBranch_n(moKeyWhs.getSelectedIndex() == 0 ? 0 : moKeyWhs.getValue()[0]);
        registry.setFkWarehouseWarehouse_n(moKeyWhs.getSelectedIndex() == 0 ? 0 : moKeyWhs.getValue()[1]);
        registry.setFkItemReferenceId_n(moKeyItemRef.getSelectedIndex() == 0 ? 0 : moKeyItemRef.getValue()[0]);
        
        if (getFormSubtype() == SModConsts.TRNX_MAT_REQ_RECLASS) {
            registry.setAuxChangeStatus(true);
        }
        
        registry.getChildCostCenters().clear();
        for (SGridRow row : moGridMatReqCC.getModel().getGridRows()) {
            SDbMaterialRequestCostCenter cc = (SDbMaterialRequestCostCenter) row;
            registry.getChildCostCenters().add(cc);
        }
        
        registry.getChildNotes().clear();
        if (!moTextReqNotes.getValue().isEmpty()) {
            SDbMaterialRequestNote note = new SDbMaterialRequestNote();
            note.setNotes(msReqNotes);
            registry.getChildNotes().add(note);
        }
        
        registry.getChildEntries().clear();
        for (SDbMaterialRequestEntry ety : maMatReqEntries) {
            registry.getChildEntries().add(ety);
        }
        
        return registry;
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (!SLibUtils.compareAmount(moDecAsign.getValue(), 1.0)) {
                validation.setMessage("La suma de los porcentajes de los centros de costo es " + moDecAsign.getValue() * 100 + "% y deberia ser igual al 100%");
            }
        }
        
        if (validation.isValid() && maMatReqEntries.size() <= 0) { 
            validation.setMessage("Debe agregar al menos un ítem para solicitar.");
        }
        
        if (getFormSubtype() == SModConsts.TRNX_MAT_REQ_STK_SUP) {
            if (moKeyWhs.getSelectedIndex() == 0) {
                validation.setMessage("Debe seleccionar un almacén");
                validation.setComponent(moKeyWhs);
            }
        }
        
        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button == jbItemStk) {
                actionItemStk();
            }
            else if (button == jbPickItem) {
                actionPickItem();
            }
            else if (button == jbPickItemRef) {
                actionPickItemRef();
            }
            else if (button == jbPickItemRefEty) {
                actionPickItemRefEty();
            }
            else if (button == jbPickUnitUsr) {
                actionPickUnit();
                actionUpdateQtyPrice();
            }
            else if (button == jbNewEty) {
                actionNew();
            }
            else if (button == jbRegisterEty) {
                actionRegister();
            }
            else if (button == jbEditEty) {
                actionEditEty();
            }
            else if (button == jbDeleteEty) {
                actionDeleteEty();
            }
            else if (button == jbCancelEty) {
                actionCancelEty();
            }
            else if (button == jbSave) {
                actionSave(SModSysConsts.TRNS_ST_MAT_REQ_NEW);
            }
            else if (button == jbSaveAndSend) {
                if (miClient.showMsgBoxConfirm("¿Esta seguro de enviar la requisición?") == JOptionPane.OK_OPTION) {
                    actionSave(SModSysConsts.TRNS_ST_MAT_REQ_AUTH);
                }
            }
            else if (button == jbAuthorize) {
                actionAuthorizeOrRejectResource(SAuthorizationUtils.AUTH_ACTION_AUTHORIZE);
            }
            else if (button == jbReject) {
                actionAuthorizeOrRejectResource(SAuthorizationUtils.AUTH_ACTION_REJECT);
            }
            else if (button == jbReqNotes) {
                actionReqNotes();
            }
            else if (button == jbEtyNotes) {
                actionEtyNotes();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            if (checkBox == moBoolNewItem) {
                stateChangeNewItem();
            }
        }
        else if (e.getSource() instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();
            if (comboBox == moKeyConsEntEty) {
                stateChangeConsEntEty();
            }
            else if (comboBox == moKeyConsSubentEty) {
                stateChangeConsSubentEty();
            }
        }
    }

    @Override
    public void notifyRowNew(int gridType, int gridSubtype, int row, SGridRow gridRow) {
        switch (gridType) {
            case SModConsts.TRN_MAT_REQ_CC:
                canAddRows();
                updateReqAsignBudgetRows();
                populateWhs();
                mbCcChanged = true;
                break;
        }
    }

    @Override
    public void notifyRowEdit(int gridType, int gridSubtype, int row, SGridRow gridRow) {
    
    }

    @Override
    public void notifyRowDelete(int gridType, int gridSubtype, int row, SGridRow gridRow) {
    
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            enableGridButtons();
            setComponetsEntryData((SDbMaterialRequestEntry) moGridMatReqList.getSelectedGridRow());
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();
            
            if (textField == moTextItemKey) {
                actionItemKey();
            }
            else if (textField == moDecQtyUsr) {
                actionUnitPriceUsr();
                actionUpdateQtyPrice();
            }
            else if (textField == moDecUnitPriceUsr) {
                actionUnitPriceUsr();
                actionUpdateQtyPrice();
            }
            else if (textField == moTextItemDescription) {
                actionItemDesc();
            }
            else if (textField == moTextReqNotes) {
                msReqNotes = moTextReqNotes.getValue();
            }
            else if (textField == moTextEtyNotes) {
                msEtyNotes = moTextEtyNotes.getValue();
            }
        }
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        updateReqAsignBudgetRows();
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
    }
}

class SPkMatCC {
    protected int mnPkMatRequestId;
    protected int mnPkEntMatConsumptionEntityId;
    protected int mnPkSubentMatConsumptionEntityId;
    protected int mnPkSubentMatConsumptionSubentityId;
    protected int mnPkCostCenterId;
    
    public void setPrimaryKey(int[] pk) {
        mnPkMatRequestId = pk[0];
        mnPkEntMatConsumptionEntityId = pk[1];
        mnPkSubentMatConsumptionEntityId = pk[2];
        mnPkSubentMatConsumptionSubentityId = pk[3];
        mnPkCostCenterId = pk[4];
    }

    public int[] getPrimaryKey() {
        return new int[] { mnPkMatRequestId, mnPkEntMatConsumptionEntityId, mnPkSubentMatConsumptionEntityId, mnPkSubentMatConsumptionSubentityId, mnPkCostCenterId };
    }
}
