/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.form;

import cfd.DCfdConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormOptionPickerInterface;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataCfdRecordRow;
import erp.mfin.data.SDataCheck;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SDataTax;
import erp.mfin.data.SValidationUtils;
import erp.mfin.data.diot.SDiotUtils;
import erp.mitm.data.SDataItem;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SFinConsts;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import sa.lib.SLibUtils;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Sergio Flores, Isabel Servín, Edwin Carmona
 */
public class SFormRecordEntry extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.FocusListener, java.awt.event.ItemListener {
    
    private static final String LABEL_BIZ_PARTNER = "Asoc. negocios";

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private int mnAccountSystemTypeId;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private boolean mbIsTaxCfg;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataRecordEntry moRecordEntry;
    private erp.mfin.data.SDataRecord moRecord;
    private erp.mitm.data.SDataItem moItem;
    private erp.mitm.data.SDataItem moItemAux;
    private erp.lib.form.SFormField moFieldConcept;
    private erp.lib.form.SFormField moFieldFkBizPartnerId_nr;
    private erp.lib.form.SFormField moFieldOccasionalFiscalId;
    private erp.lib.form.SFormField moFieldReference;
    private erp.lib.form.SFormField moFieldIsReferenceTax;
    private erp.lib.form.SFormField moFieldFkTaxId_n;
    private erp.lib.form.SFormField moFieldFkEntityId_n;
    private erp.lib.form.SFormField moFieldFkItemId_n;
    private erp.lib.form.SFormField moFieldUnits;
    private erp.lib.form.SFormField moFieldFkItemAuxId_n;
    private erp.lib.form.SFormField moFieldFileXml;
    private erp.lib.form.SFormField moFieldFkYearId_n;
    private erp.lib.form.SFormField moFieldFkCheckId_n;
    private erp.lib.form.SFormField moFieldFkCurrencyId;
    private erp.lib.form.SFormField moFieldDebitCy;
    private erp.lib.form.SFormField moFieldCreditCy;
    private erp.lib.form.SFormField moFieldExchangeRateSystem;
    private erp.lib.form.SFormField moFieldExchangeRate;
    private erp.lib.form.SFormField moFieldDebit;
    private erp.lib.form.SFormField moFieldCredit;

    private int mnOptionsBizPartnerType;
    private int mnOptionsEntityType;
    private int mnOptionsItemType;
    private boolean mbIsBizPartnerRequired;
    private boolean mbIsItemRequired;
    private boolean mbIsTaxRequired;
    private int[] manCurrentEntityKey_n;
    private int[] manLastCurrencyKey;
    private int[] manDpsClassKey;
    private int[] manDpsAdjClassKey;
    private java.lang.String msCurrentFkAccountId;
    private erp.mfin.data.SDataAccountCash moEntryAccountCash;
    private erp.mtrn.data.SDataDps moEntryDps;
    private erp.mtrn.data.SDataDps moEntryDpsAdj;
    private erp.mfin.form.SPanelAccount moPanelFkAccountId;
    private erp.mfin.form.SPanelAccount moPanelFkCostCenterId_n;

    private String msXmlPath;
    private ArrayList<SDataCfdRecordRow> maCfdRecordRows = null;

    /** Creates new form SFormRecordEntry
     * @param client GUI client.
     */
    public SFormRecordEntry(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FIN_REC_ETY;
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

        bgTax = new javax.swing.ButtonGroup();
        jpRegistry = new javax.swing.JPanel();
        jlDummyAccount = new javax.swing.JLabel();
        jpRegistryCenter = new javax.swing.JPanel();
        jpRegistryCenterNorth = new javax.swing.JPanel();
        jpSettings = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        jtfConcept = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jlFkBizPartnerId_nr = new javax.swing.JLabel();
        jcbFkBizPartnerId_nr = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkBizPartnerId_nr = new javax.swing.JButton();
        jlOccasionalFiscalId = new javax.swing.JLabel();
        jcbOccasionalFiscalId = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jlReference = new javax.swing.JLabel();
        jtfReference = new javax.swing.JTextField();
        jbReference = new javax.swing.JButton();
        jckIsReferenceTax = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jlFkTaxId_n = new javax.swing.JLabel();
        jcbFkTaxId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkTaxId_n = new javax.swing.JButton();
        jrbTaxCash = new javax.swing.JRadioButton();
        jrbTaxPend = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jlFkEntityId_n = new javax.swing.JLabel();
        jcbFkEntityId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkEntityId_n = new javax.swing.JButton();
        jtfEntityCurrencyKey = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jlFkItemId_n = new javax.swing.JLabel();
        jcbFkItemId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemId_n = new javax.swing.JButton();
        jtfUnits = new javax.swing.JTextField();
        jtfUnitsSymbol = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jlFkItemAuxId_n = new javax.swing.JLabel();
        jcbFkItemAuxId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkItemAuxId_n = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jlFkDps = new javax.swing.JLabel();
        jtfFkDps = new javax.swing.JTextField();
        jbFkDps = new javax.swing.JButton();
        jbFkDpsRemove = new javax.swing.JButton();
        jlDummy02 = new javax.swing.JLabel();
        jlFkDpsAdj = new javax.swing.JLabel();
        jtfFkDpsAdj = new javax.swing.JTextField();
        jbFkDpsAdj = new javax.swing.JButton();
        jbFkDpsAdjRemove = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jlFileXml = new javax.swing.JLabel();
        jtfFileXml = new javax.swing.JTextField();
        jbFileXml = new javax.swing.JButton();
        jbFileXmlRemove = new javax.swing.JButton();
        jtfXmlFilesNumber = new javax.swing.JTextField();
        jbFileXmlAdd = new javax.swing.JButton();
        jbGetXml = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jlFkYearId_n = new javax.swing.JLabel();
        jtfFkYearId_n = new javax.swing.JTextField();
        jlDummy03 = new javax.swing.JLabel();
        jckIsCheckApplying = new javax.swing.JCheckBox();
        jcbFkCheckId_n = new javax.swing.JComboBox<SFormComponentItem>();
        jpValue = new javax.swing.JPanel();
        jlFkCurrencyId = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jcbFkCurrencyId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkCurrencyId = new javax.swing.JButton();
        jlDummy11 = new javax.swing.JLabel();
        jckIsExchangeDifference = new javax.swing.JCheckBox();
        jlDebitCy = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jtfDebitCy = new javax.swing.JTextField();
        jbDebitCy = new javax.swing.JButton();
        jlExchangeRateSystem = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jtfExchangeRateSystem = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jbExchangeRateSystem = new javax.swing.JButton();
        jbExchangeRateAccountCashView = new javax.swing.JButton();
        jlCreditCy = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jtfCreditCy = new javax.swing.JTextField();
        jbCreditCy = new javax.swing.JButton();
        jlExchangeRate = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jtfExchangeRate = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jbExchangeRate = new javax.swing.JButton();
        jbExchangeRateAccountCashSet = new javax.swing.JButton();
        jlDebit = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jtfDebit = new javax.swing.JTextField();
        jbDebit = new javax.swing.JButton();
        jlDummy13 = new javax.swing.JLabel();
        jckIsSystem = new javax.swing.JCheckBox();
        jlCredit = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jtfCredit = new javax.swing.JTextField();
        jbCredit = new javax.swing.JButton();
        jlDummy15 = new javax.swing.JLabel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jlDummyCostCenter = new javax.swing.JLabel();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Partida de la póliza contable"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpRegistry.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpRegistry.setLayout(new java.awt.BorderLayout(5, 5));

        jlDummyAccount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlDummyAccount.setText("[Panel cuenta contable]");
        jlDummyAccount.setPreferredSize(new java.awt.Dimension(100, 50));
        jpRegistry.add(jlDummyAccount, java.awt.BorderLayout.NORTH);

        jpRegistryCenter.setLayout(new java.awt.BorderLayout());

        jpRegistryCenterNorth.setLayout(new java.awt.BorderLayout(0, 5));

        jpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la partida:"));
        jpSettings.setLayout(new java.awt.GridLayout(10, 1, 0, 1));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlConcept.setText("Concepto partida: *");
        jlConcept.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel2.add(jlConcept);

        jtfConcept.setText("ACCOUNT");
        jtfConcept.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel2.add(jtfConcept);

        jpSettings.add(jPanel2);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkBizPartnerId_nr.setText("Asoc. negocios:");
        jlFkBizPartnerId_nr.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlFkBizPartnerId_nr);

        jcbFkBizPartnerId_nr.setMaximumRowCount(16);
        jcbFkBizPartnerId_nr.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel4.add(jcbFkBizPartnerId_nr);

        jbFkBizPartnerId_nr.setText("...");
        jbFkBizPartnerId_nr.setToolTipText("Seleccionar asociado de negocios");
        jbFkBizPartnerId_nr.setFocusable(false);
        jbFkBizPartnerId_nr.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbFkBizPartnerId_nr);

        jlOccasionalFiscalId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlOccasionalFiscalId.setText("RFC ocasional:");
        jlOccasionalFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlOccasionalFiscalId);

        jcbOccasionalFiscalId.setEditable(true);
        jcbOccasionalFiscalId.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel4.add(jcbOccasionalFiscalId);

        jpSettings.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlReference.setText("Reposit. contable:");
        jlReference.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlReference);

        jtfReference.setText("REF");
        jtfReference.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jtfReference);

        jbReference.setText("...");
        jbReference.setToolTipText("Seleccionar repositorio contable");
        jbReference.setFocusable(false);
        jbReference.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbReference);

        jckIsReferenceTax.setText("Aplican impuestos");
        jckIsReferenceTax.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jckIsReferenceTax.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jckIsReferenceTax);

        jpSettings.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkTaxId_n.setText("Impuesto: *");
        jlFkTaxId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlFkTaxId_n);

        jcbFkTaxId_n.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(jcbFkTaxId_n);

        jbFkTaxId_n.setText("...");
        jbFkTaxId_n.setToolTipText("Seleccionar impuesto");
        jbFkTaxId_n.setFocusable(false);
        jbFkTaxId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbFkTaxId_n);

        bgTax.add(jrbTaxCash);
        jrbTaxCash.setSelected(true);
        jrbTaxCash.setText("Efectivo");
        jrbTaxCash.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jrbTaxCash);

        bgTax.add(jrbTaxPend);
        jrbTaxPend.setText("Pendiente");
        jrbTaxPend.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jrbTaxPend);

        jpSettings.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkEntityId_n.setText("Entidad: *");
        jlFkEntityId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlFkEntityId_n);

        jcbFkEntityId_n.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel7.add(jcbFkEntityId_n);

        jbFkEntityId_n.setText("...");
        jbFkEntityId_n.setToolTipText("Seleccionar entidad");
        jbFkEntityId_n.setFocusable(false);
        jbFkEntityId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbFkEntityId_n);

        jtfEntityCurrencyKey.setEditable(false);
        jtfEntityCurrencyKey.setText("CUR");
        jtfEntityCurrencyKey.setToolTipText("Moneda");
        jtfEntityCurrencyKey.setFocusable(false);
        jtfEntityCurrencyKey.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel7.add(jtfEntityCurrencyKey);

        jpSettings.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkItemId_n.setText("Ítem: *");
        jlFkItemId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlFkItemId_n);

        jcbFkItemId_n.setMaximumRowCount(16);
        jcbFkItemId_n.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel8.add(jcbFkItemId_n);

        jbFkItemId_n.setText("...");
        jbFkItemId_n.setToolTipText("Seleccionar ítem");
        jbFkItemId_n.setFocusable(false);
        jbFkItemId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbFkItemId_n);

        jtfUnits.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfUnits.setText("0.0000");
        jtfUnits.setToolTipText("Unidades");
        jtfUnits.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jtfUnits);

        jtfUnitsSymbol.setEditable(false);
        jtfUnitsSymbol.setText("UN");
        jtfUnitsSymbol.setToolTipText("Unidades");
        jtfUnitsSymbol.setFocusable(false);
        jtfUnitsSymbol.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel8.add(jtfUnitsSymbol);

        jpSettings.add(jPanel8);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkItemAuxId_n.setText("Ítem auxiliar:");
        jlFkItemAuxId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel14.add(jlFkItemAuxId_n);

        jcbFkItemAuxId_n.setMaximumRowCount(16);
        jcbFkItemAuxId_n.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel14.add(jcbFkItemAuxId_n);

        jbFkItemAuxId_n.setText("...");
        jbFkItemAuxId_n.setToolTipText("Seleccionar ítem auxiliar");
        jbFkItemAuxId_n.setFocusable(false);
        jbFkItemAuxId_n.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbFkItemAuxId_n);

        jpSettings.add(jPanel14);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkDps.setText("Docto. (factura):");
        jlFkDps.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlFkDps);

        jtfFkDps.setEditable(false);
        jtfFkDps.setText("DPS");
        jtfFkDps.setFocusable(false);
        jtfFkDps.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(jtfFkDps);

        jbFkDps.setText("...");
        jbFkDps.setToolTipText("Seleccionar documento (factura) de compras-ventas");
        jbFkDps.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbFkDps);

        jbFkDpsRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbFkDpsRemove.setToolTipText("Remover documento (factura) de compras-ventas");
        jbFkDpsRemove.setFocusable(false);
        jbFkDpsRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbFkDpsRemove);

        jlDummy02.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel10.add(jlDummy02);

        jlFkDpsAdj.setText("Docto. ajuste (nota crédito):");
        jlFkDpsAdj.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(jlFkDpsAdj);

        jtfFkDpsAdj.setEditable(false);
        jtfFkDpsAdj.setText("DPS ADJUSTMENT");
        jtfFkDpsAdj.setFocusable(false);
        jtfFkDpsAdj.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(jtfFkDpsAdj);

        jbFkDpsAdj.setText("...");
        jbFkDpsAdj.setToolTipText("Seleccionar documento de ajuste (nota crédito) de compras-ventas");
        jbFkDpsAdj.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbFkDpsAdj);

        jbFkDpsAdjRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbFkDpsAdjRemove.setToolTipText("Remover documento de ajuste (nota crédito) de compras-ventas");
        jbFkDpsAdjRemove.setFocusable(false);
        jbFkDpsAdjRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbFkDpsAdjRemove);

        jpSettings.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFileXml.setText("Archivo XML:");
        jlFileXml.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlFileXml);

        jtfFileXml.setEditable(false);
        jtfFileXml.setText("XML");
        jtfFileXml.setOpaque(false);
        jtfFileXml.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jtfFileXml);

        jbFileXml.setText("...");
        jbFileXml.setToolTipText("Seleccionar archivo XML...");
        jbFileXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbFileXml);

        jbFileXmlRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif"))); // NOI18N
        jbFileXmlRemove.setToolTipText("Eliminar archivo XML");
        jbFileXmlRemove.setFocusable(false);
        jbFileXmlRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbFileXmlRemove);

        jtfXmlFilesNumber.setEditable(false);
        jtfXmlFilesNumber.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfXmlFilesNumber.setText("100");
        jtfXmlFilesNumber.setToolTipText("Número de archivos XML");
        jtfXmlFilesNumber.setPreferredSize(new java.awt.Dimension(25, 23));
        jPanel11.add(jtfXmlFilesNumber);

        jbFileXmlAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_add.gif"))); // NOI18N
        jbFileXmlAdd.setToolTipText("Agregar archivos XML");
        jbFileXmlAdd.setFocusable(false);
        jbFileXmlAdd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbFileXmlAdd);

        jbGetXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_doc_xml.gif"))); // NOI18N
        jbGetXml.setToolTipText("Descargar archivos XML");
        jbGetXml.setFocusable(false);
        jbGetXml.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbGetXml);

        jpSettings.add(jPanel11);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlFkYearId_n.setText("Ejercicio contable:");
        jlFkYearId_n.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel13.add(jlFkYearId_n);

        jtfFkYearId_n.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfFkYearId_n.setText("2000");
        jtfFkYearId_n.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel13.add(jtfFkYearId_n);

        jlDummy03.setPreferredSize(new java.awt.Dimension(175, 23));
        jPanel13.add(jlDummy03);

        jckIsCheckApplying.setText("Cheque del movimiento:");
        jckIsCheckApplying.setFocusable(false);
        jckIsCheckApplying.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel13.add(jckIsCheckApplying);

        jcbFkCheckId_n.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel13.add(jcbFkCheckId_n);

        jpSettings.add(jPanel13);

        jpRegistryCenterNorth.add(jpSettings, java.awt.BorderLayout.PAGE_START);

        jpValue.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor de la partida:"));
        jpValue.setLayout(new java.awt.GridLayout(5, 4, 5, 1));

        jlFkCurrencyId.setText("Moneda de la partida: *");
        jpValue.add(jlFkCurrencyId);

        jPanel1.setLayout(new java.awt.BorderLayout(2, 0));
        jPanel1.add(jcbFkCurrencyId, java.awt.BorderLayout.CENTER);

        jbFkCurrencyId.setText("...");
        jbFkCurrencyId.setToolTipText("Seleccionar moneda");
        jbFkCurrencyId.setFocusable(false);
        jbFkCurrencyId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel1.add(jbFkCurrencyId, java.awt.BorderLayout.EAST);

        jpValue.add(jPanel1);
        jpValue.add(jlDummy11);

        jckIsExchangeDifference.setText("Diferencia cambiaria");
        jckIsExchangeDifference.setFocusable(false);
        jpValue.add(jckIsExchangeDifference);

        jlDebitCy.setText("Cargo: *");
        jpValue.add(jlDebitCy);

        jPanel22.setLayout(new java.awt.BorderLayout(2, 0));

        jtfDebitCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDebitCy.setText("0.00");
        jPanel22.add(jtfDebitCy, java.awt.BorderLayout.CENTER);

        jbDebitCy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbDebitCy.setToolTipText("Calcular");
        jbDebitCy.setFocusable(false);
        jbDebitCy.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel22.add(jbDebitCy, java.awt.BorderLayout.EAST);

        jpValue.add(jPanel22);

        jlExchangeRateSystem.setText("Tipo de cambio sistema:");
        jpValue.add(jlExchangeRateSystem);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jtfExchangeRateSystem.setEditable(false);
        jtfExchangeRateSystem.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfExchangeRateSystem.setText("0.0000");
        jtfExchangeRateSystem.setFocusable(false);
        jPanel9.add(jtfExchangeRateSystem, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jbExchangeRateSystem.setText("...");
        jbExchangeRateSystem.setToolTipText("Seleccionar tipo de cambio");
        jbExchangeRateSystem.setFocusable(false);
        jbExchangeRateSystem.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbExchangeRateSystem);

        jbExchangeRateAccountCashView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_look.gif"))); // NOI18N
        jbExchangeRateAccountCashView.setToolTipText("Ver tipo de cambio acumulado");
        jbExchangeRateAccountCashView.setFocusable(false);
        jbExchangeRateAccountCashView.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbExchangeRateAccountCashView);

        jPanel9.add(jPanel3, java.awt.BorderLayout.EAST);

        jpValue.add(jPanel9);

        jlCreditCy.setText("Abono: *");
        jpValue.add(jlCreditCy);

        jPanel23.setLayout(new java.awt.BorderLayout(2, 0));

        jtfCreditCy.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCreditCy.setText("0.00");
        jPanel23.add(jtfCreditCy, java.awt.BorderLayout.CENTER);

        jbCreditCy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbCreditCy.setToolTipText("Calcular");
        jbCreditCy.setFocusable(false);
        jbCreditCy.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel23.add(jbCreditCy, java.awt.BorderLayout.EAST);

        jpValue.add(jPanel23);

        jlExchangeRate.setText("Tipo de cambio: *");
        jpValue.add(jlExchangeRate);

        jPanel12.setLayout(new java.awt.BorderLayout());

        jtfExchangeRate.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfExchangeRate.setText("0.0000");
        jPanel12.add(jtfExchangeRate, java.awt.BorderLayout.CENTER);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jbExchangeRate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbExchangeRate.setToolTipText("Calcular");
        jbExchangeRate.setFocusable(false);
        jbExchangeRate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel15.add(jbExchangeRate);

        jbExchangeRateAccountCashSet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_money.gif"))); // NOI18N
        jbExchangeRateAccountCashSet.setToolTipText("Asignar tipo de cambio acumulado");
        jbExchangeRateAccountCashSet.setFocusable(false);
        jbExchangeRateAccountCashSet.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel15.add(jbExchangeRateAccountCashSet);

        jPanel12.add(jPanel15, java.awt.BorderLayout.EAST);

        jpValue.add(jPanel12);

        jlDebit.setText("Cargo en moneda local (ML): *");
        jpValue.add(jlDebit);

        jPanel25.setLayout(new java.awt.BorderLayout(2, 0));

        jtfDebit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfDebit.setText("0.00");
        jPanel25.add(jtfDebit, java.awt.BorderLayout.CENTER);

        jbDebit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbDebit.setToolTipText("Calcular");
        jbDebit.setFocusable(false);
        jbDebit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel25.add(jbDebit, java.awt.BorderLayout.EAST);

        jpValue.add(jPanel25);
        jpValue.add(jlDummy13);

        jckIsSystem.setText("Registro de sistema");
        jckIsSystem.setEnabled(false);
        jpValue.add(jckIsSystem);

        jlCredit.setText("Abono en moneda local (ML): *");
        jpValue.add(jlCredit);

        jPanel26.setLayout(new java.awt.BorderLayout(2, 0));

        jtfCredit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCredit.setText("0.00");
        jPanel26.add(jtfCredit, java.awt.BorderLayout.CENTER);

        jbCredit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_action.gif"))); // NOI18N
        jbCredit.setToolTipText("Calcular");
        jbCredit.setFocusable(false);
        jbCredit.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel26.add(jbCredit, java.awt.BorderLayout.EAST);

        jpValue.add(jPanel26);
        jpValue.add(jlDummy15);

        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setFocusable(false);
        jpValue.add(jckIsDeleted);

        jpRegistryCenterNorth.add(jpValue, java.awt.BorderLayout.CENTER);

        jpRegistryCenter.add(jpRegistryCenterNorth, java.awt.BorderLayout.NORTH);

        jlDummyCostCenter.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlDummyCostCenter.setText("[Panel centro de costo-beneficio]");
        jlDummyCostCenter.setPreferredSize(new java.awt.Dimension(100, 50));
        jpRegistryCenter.add(jlDummyCostCenter, java.awt.BorderLayout.SOUTH);

        jpRegistry.add(jpRegistryCenter, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(816, 639));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<>();

        // Create and add form panels:

        try {
            // account and const center panels:
            moPanelFkAccountId = new SPanelAccount(miClient, SDataConstants.FIN_ACC, false, true, false);
            moPanelFkAccountId.setBorder(new TitledBorder("Cuenta contable:"));
            moPanelFkCostCenterId_n = new SPanelAccount(miClient, SDataConstants.FIN_CC, false, false, false);
            moPanelFkCostCenterId_n.setBorder(new TitledBorder("Centro de costo:"));
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        jpRegistry.remove(jlDummyAccount);
        jpRegistry.add(moPanelFkAccountId, BorderLayout.NORTH);

        jpRegistryCenter.remove(jlDummyCostCenter);
        jpRegistryCenter.add(moPanelFkCostCenterId_n, BorderLayout.SOUTH);

        // Create form fields:

        moFieldConcept = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfConcept, jlConcept);
        moFieldConcept.setLengthMax(100);
        moFieldFkBizPartnerId_nr = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkBizPartnerId_nr, jlFkBizPartnerId_nr);
        moFieldFkBizPartnerId_nr.setPickerButton(jbFkBizPartnerId_nr);
        moFieldOccasionalFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jcbOccasionalFiscalId, jlOccasionalFiscalId);
        moFieldOccasionalFiscalId.setLengthMin(DCfdConsts.LEN_RFC_ORG);
        moFieldOccasionalFiscalId.setLengthMax(DCfdConsts.LEN_RFC_PER);
        moFieldReference = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfReference, jlReference);
        moFieldReference.setLengthMax(15);
        moFieldReference.setPickerButton(jbReference);
        moFieldIsReferenceTax = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsReferenceTax);
        moFieldFkTaxId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkTaxId_n, jlFkTaxId_n);
        moFieldFkTaxId_n.setPickerButton(jbFkTaxId_n);
        moFieldFkEntityId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkEntityId_n, jlFkEntityId_n);
        moFieldFkEntityId_n.setPickerButton(jbFkEntityId_n);
        moFieldFkItemId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkItemId_n, jlFkItemId_n);
        moFieldFkItemId_n.setPickerButton(jbFkItemId_n);
        moFieldUnits = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfUnits, new JLabel("Unidades " + jlFkItemId_n.getText()));
        moFieldUnits.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat());
        moFieldFkItemAuxId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkItemAuxId_n, jlFkItemAuxId_n);
        moFieldFkItemAuxId_n.setPickerButton(jbFkItemAuxId_n);
        moFieldFileXml = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfFileXml, jlFileXml);
        moFieldFkYearId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfFkYearId_n, jlFkYearId_n);
        moFieldFkYearId_n.setIntegerMin(2000);
        moFieldFkYearId_n.setIntegerMax(2100);
        moFieldFkYearId_n.setMinInclusive(true);
        moFieldFkYearId_n.setMaxInclusive(true);
        moFieldFkYearId_n.setDecimalFormat(miClient.getSessionXXX().getFormatters().getYearFormat());
        moFieldFkCheckId_n = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkCheckId_n, jckIsCheckApplying);
        moFieldFkCurrencyId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkCurrencyId, jlFkCurrencyId);
        moFieldFkCurrencyId.setPickerButton(jbFkCurrencyId);
        moFieldDebitCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDebitCy, jlDebitCy);
        moFieldDebitCy.setMinInclusive(true);
        moFieldCreditCy = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfCreditCy, jlCreditCy);
        moFieldCreditCy.setMinInclusive(true);
        moFieldExchangeRateSystem = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfExchangeRateSystem, jlExchangeRateSystem);
        moFieldExchangeRateSystem.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsExchangeRateFormat());
        moFieldExchangeRate = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, true, jtfExchangeRate, jlExchangeRate);
        moFieldExchangeRate.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsExchangeRateFormat());
        moFieldExchangeRate.setPickerButton(jbExchangeRateSystem);
        moFieldDebit = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDebit, jlDebit);
        moFieldDebit.setMinInclusive(true);
        moFieldCredit = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfCredit, jlCredit);
        moFieldCredit.setMinInclusive(true);

        mvFields.add(moFieldConcept);
        mvFields.add(moFieldFkBizPartnerId_nr);
        mvFields.add(moFieldOccasionalFiscalId);
        mvFields.add(moFieldReference);
        mvFields.add(moFieldIsReferenceTax);
        mvFields.add(moFieldFkTaxId_n);
        mvFields.add(moFieldFkEntityId_n);
        mvFields.add(moFieldFkItemId_n);
        mvFields.add(moFieldUnits);
        mvFields.add(moFieldFkItemAuxId_n);
        mvFields.add(moFieldFileXml);
        mvFields.add(moFieldFkYearId_n);
        mvFields.add(moFieldFkCheckId_n);
        mvFields.add(moFieldFkCurrencyId);
        mvFields.add(moFieldDebitCy);
        mvFields.add(moFieldCreditCy);
        mvFields.add(moFieldExchangeRateSystem);
        mvFields.add(moFieldExchangeRate);
        mvFields.add(moFieldDebit);
        mvFields.add(moFieldCredit);

        // Form listeners:

        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
        jbFkBizPartnerId_nr.addActionListener(this);
        jbReference.addActionListener(this);
        jbFkTaxId_n.addActionListener(this);
        jbFkEntityId_n.addActionListener(this);
        jbFkItemId_n.addActionListener(this);
        jbFkItemAuxId_n.addActionListener(this);
        jbFkDps.addActionListener(this);
        jbFileXml.addActionListener(this);
        jbFkDpsRemove.addActionListener(this);
        jbFileXmlRemove.addActionListener(this);
        jbFileXmlAdd.addActionListener(this);
        jbGetXml.addActionListener(this);
        jbFkDpsAdj.addActionListener(this);
        jbFkDpsAdjRemove.addActionListener(this);
        jbFkCurrencyId.addActionListener(this);
        jbExchangeRateSystem.addActionListener(this);
        jbExchangeRate.addActionListener(this);
        jbExchangeRateAccountCashSet.addActionListener(this);
        jbExchangeRateAccountCashView.addActionListener(this);
        jbDebitCy.addActionListener(this);
        jbCreditCy.addActionListener(this);
        jbDebit.addActionListener(this);
        jbCredit.addActionListener(this);

        jcbFkEntityId_n.addItemListener(this);
        jcbFkItemId_n.addItemListener(this);
        jcbFkItemAuxId_n.addItemListener(this);
        jcbFkCurrencyId.addItemListener(this);
        jckIsCheckApplying.addItemListener(this);
        jckIsExchangeDifference.addItemListener(this);

        moPanelFkAccountId.getFieldAccount().getComponent().addFocusListener(this);
        jcbFkEntityId_n.addFocusListener(this);
        jcbFkCurrencyId.addFocusListener(this);
        jtfDebitCy.addFocusListener(this);
        jtfCreditCy.addFocusListener(this);
        jtfDebit.addFocusListener(this);
        jtfCredit.addFocusListener(this);
        jtfExchangeRate.addFocusListener(this);
        
        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
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
            moPanelFkAccountId.getFieldAccount().getComponent().requestFocus();
        }
    }

    private void triggerFocusLost() {
        if (moPanelFkAccountId.getFieldAccount().getComponent().isFocusOwner()) {
            actionFkAccountIdFocusLost();
        }
        else if (jcbFkCurrencyId.isFocusOwner()) {
            actionFkCurrencyIdFocusLost();
        }
        else if (jtfDebitCy.isFocusOwner()) {
            actionDebitCyFocusLost();
        }
        else if (jtfCreditCy.isFocusOwner()) {
            actionCreditCyFocusLost();
        }
        else if (jtfDebit.isFocusOwner()) {
            actionDebitFocusLost();
        }
        else if (jtfCredit.isFocusOwner()) {
            actionCreditFocusLost();
        }
        else if (jtfExchangeRate.isFocusOwner()) {
            actionExchangeRateFocusLost();
        }
    }

    private void renderAccountSettings() {
        mbResetingForm = true;

        int[] anAuxBizParnerKey = (int[]) moFieldFkBizPartnerId_nr.getKey();
        int[] anAuxTaxKey = (int[]) moFieldFkTaxId_n.getKey();
        int[] anAuxEntityKey = (int[]) moFieldFkEntityId_n.getKey();
        int[] anAuxItemKey = (int[]) moFieldFkItemId_n.getKey();
        int[] anAuxItemAuxKey = (int[]) moFieldFkItemAuxId_n.getKey();
        double dUnits = moFieldUnits.getDouble();
        int nYearId = moFieldFkYearId_n.getInteger();
        Object oFilterKey = null;
        SDataAccount oAccountMajor = null;
        SDataAccountCash oAuxAccountCash = moEntryAccountCash;
        SDataDps oAuxDps = moEntryDps;
        SDataDps oAuxDpsAdj = moEntryDpsAdj;

        mnAccountSystemTypeId = SDataConstantsSys.UNDEFINED;
        manDpsClassKey = null;
        manDpsAdjClassKey = null;
        moEntryAccountCash = null;
        moEntryDps = null;
        moEntryDpsAdj = null;

        mnOptionsBizPartnerType = SDataConstants.UNDEFINED;
        mnOptionsEntityType = SDataConstants.UNDEFINED;
        mnOptionsItemType = SDataConstants.UNDEFINED;
        mbIsBizPartnerRequired = false;
        mbIsItemRequired = false;
        mbIsTaxRequired = false;
        mbIsTaxCfg = false;
        jlFkBizPartnerId_nr.setText(LABEL_BIZ_PARTNER + ":");
        bgTax.clearSelection();

        /*
         * In this form, moPanelFkAccountId.getFieldAccount().getComponent() has 2 focus events listeners:
         * 1. Listener in panel itself.
         * 2. Listener in this form.
         *
         * When focus lost event is invocated, the first listener that is triggered is number 2., so account
         * object in panel is not still available, until listener number 1. is triggered.
         * So, by now, it is necesary to read account registry in this form.
         */

        //account = moPanelFkAccountId.getCurrentInputAccount();

        if (!moPanelFkAccountId.isEmptyAccountId()) {
            oAccountMajor = moPanelFkAccountId.getDataAccountMajor();
        }

        if (oAccountMajor == null) {
            jlFkBizPartnerId_nr.setEnabled(false);
            jcbFkBizPartnerId_nr.setEnabled(false);
            jbFkBizPartnerId_nr.setEnabled(false);
            jlOccasionalFiscalId.setEnabled(false);
            jcbOccasionalFiscalId.setEnabled(false);
            jlReference.setEnabled(false);
            jtfReference.setEnabled(false);
            jbReference.setEnabled(false);
            jckIsReferenceTax.setEnabled(false);
            jlFkTaxId_n.setEnabled(false);
            jcbFkTaxId_n.setEnabled(false);
            jbFkTaxId_n.setEnabled(false);
            jrbTaxCash.setEnabled(false);
            jrbTaxPend.setEnabled(false);
            jlFkEntityId_n.setEnabled(false);
            jcbFkEntityId_n.setEnabled(false);
            jbFkEntityId_n.setEnabled(false);
            jtfEntityCurrencyKey.setEnabled(false);
            jlFkItemId_n.setEnabled(false);
            jcbFkItemId_n.setEnabled(false);
            jbFkItemId_n.setEnabled(false);
            jtfUnits.setEnabled(false);
            jtfUnitsSymbol.setEnabled(false);
            jlFkItemAuxId_n.setEnabled(false);
            jcbFkItemAuxId_n.setEnabled(false);
            jbFkItemAuxId_n.setEnabled(false);
            jlFkDps.setEnabled(false);
            jtfFkDps.setEnabled(false);
            jbFkDps.setEnabled(false);
            jbFkDpsRemove.setEnabled(false);
            jlFileXml.setEnabled(false);
            jtfFileXml.setEnabled(false);
            jbFileXml.setEnabled(false);
            jbFileXmlRemove.setEnabled(false);
            jbFileXmlAdd.setEnabled(false);
            jbGetXml.setEnabled(true); 
            jlFkDpsAdj.setEnabled(false);
            jtfFkDpsAdj.setEnabled(false);
            jbFkDpsAdj.setEnabled(false);
            jbFkDpsAdjRemove.setEnabled(false);
            jlFkYearId_n.setEnabled(false);
            jtfFkYearId_n.setEnabled(false);

            moFieldFkBizPartnerId_nr.resetField();
            moFieldReference.resetField();
            moFieldIsReferenceTax.resetField();
            moFieldFkTaxId_n.resetField();
            moFieldFkEntityId_n.resetField();
            moFieldFkItemId_n.resetField();
            moFieldFkItemAuxId_n.resetField();
            moFieldFkYearId_n.resetField();

            jtfEntityCurrencyKey.setText("");
            jtfUnitsSymbol.setText("");
            jtfFkDps.setText("");
            jtfFileXml.setText("");
            jtfFkDpsAdj.setText("");

            moFieldUnits.resetField();
            moFieldFkYearId_n.resetField();
        }
        else {
            mnAccountSystemTypeId = oAccountMajor.getFkAccountSystemTypeId();
            int[] anAccountSubclass = new int[] { oAccountMajor.getFkAccountTypeId_r(), oAccountMajor.getFkAccountClassId_r(), oAccountMajor.getFkAccountSubclassId_r() };
            
            boolean isAccSysBizPartnerAll = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS, SDataConstantsSys.FINS_TP_ACC_SYS_CDR, SDataConstantsSys.FINS_TP_ACC_SYS_DBR });
            boolean isAccSysBizPartnerSupCus = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS });
            boolean isAccSysPurchases = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PUR, SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ });
            boolean isAccClsPurchases = SLibUtilities.belongsTo(anAccountSubclass, new int[][] {SDataConstantsSys.FINS_CLS_ACC_PUR, SDataConstantsSys.FINS_CLS_ACC_PUR_ADJ });
            boolean isAccSysSales = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SAL, SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ });
            boolean isAccClsSales = SLibUtilities.belongsTo(anAccountSubclass, new int[][] {SDataConstantsSys.FINS_CLS_ACC_SAL, SDataConstantsSys.FINS_CLS_ACC_SAL_ADJ });
            boolean isAccSysTax = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT });
            boolean isDiotAccount = false;
            
            try {
                isDiotAccount = SDiotUtils.isDiotAccount(miClient.getSession().getStatement(), moPanelFkAccountId.getDataAccountMajor()) || 
                        SDiotUtils.isDiotAccount(miClient.getSession().getStatement(), moPanelFkAccountId.getCurrentInputAccount());
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }

            // Check if it is necesary to enable business partner fields:

            if (oAccountMajor.getIsRequiredBizPartner() || isAccSysBizPartnerAll || isAccSysPurchases || isAccClsPurchases || isAccSysSales || isAccClsSales || isAccSysTax || isDiotAccount) {
                if (isAccSysBizPartnerAll || (isAccSysPurchases && isAccClsPurchases) || (isAccSysSales && isAccClsSales)) {
                    mbIsBizPartnerRequired = true;
                    jlFkBizPartnerId_nr.setText(LABEL_BIZ_PARTNER + ": *");
                }

                jlFkBizPartnerId_nr.setEnabled(true);
                jcbFkBizPartnerId_nr.setEnabled(true);
                jbFkBizPartnerId_nr.setEnabled(true);
                jlOccasionalFiscalId.setEnabled(true);
                jcbOccasionalFiscalId.setEnabled(true);

                if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_SUP || isAccSysPurchases || isDiotAccount) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_SUP;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_CUS || isAccSysSales) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_CUS;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_CDR) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_CDR;
                }
                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_DBR) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_DBR;
                }
                else if (isAccSysTax) {
                    mnOptionsBizPartnerType = SDataConstants.BPSX_BP_X_SUP_CUS; // suppliers and customers!
                }
                else {
                    switch (oAccountMajor.getFkAccountLedgerTypeId()) {
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_CUS:
                            mnOptionsBizPartnerType = SDataConstants.BPSX_BP_CUS;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_DBR:
                            mnOptionsBizPartnerType = SDataConstants.BPSX_BP_X_CUS_DBR;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_SUP:
                            mnOptionsBizPartnerType = SDataConstants.BPSX_BP_SUP;
                            break;
                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_CDR:
                            mnOptionsBizPartnerType = SDataConstants.BPSX_BP_X_SUP_CDR;
                            break;
                        default:
                            mnOptionsBizPartnerType = SDataConstants.BPSU_BP; // all business partners!
                    }
                }

                SFormUtilities.populateComboBox(miClient, jcbFkBizPartnerId_nr, mnOptionsBizPartnerType);
                SFormUtilities.locateComboBoxItem(jcbFkBizPartnerId_nr, anAuxBizParnerKey);
            }
            else {
                jlFkBizPartnerId_nr.setEnabled(false);
                jcbFkBizPartnerId_nr.setEnabled(false);
                jbFkBizPartnerId_nr.setEnabled(false);
                jlOccasionalFiscalId.setEnabled(false);
                jcbOccasionalFiscalId.setEnabled(false);
            }

            // Check if it is necesary to enable money reference fields:

            if (isAccSysBizPartnerAll) {
                jlReference.setEnabled(true);
                jtfReference.setEnabled(true);
                jbReference.setEnabled(true);
                jckIsReferenceTax.setEnabled(true);
            }
            else {
                jlReference.setEnabled(false);
                jtfReference.setEnabled(false);
                jbReference.setEnabled(false);
                jckIsReferenceTax.setEnabled(false);
            }

            // Check if it is necesary to enable tax fields:
            int[] taxFk = SValidationUtils.getTaxFkByAcc(miClient.getSession(), moPanelFkAccountId.getFieldAccount().getString());
            if (isAccSysTax || isAccSysPurchases || taxFk != null) {
                mbIsTaxRequired = isAccSysTax;
                
                if (taxFk != null) {
                    mbIsTaxCfg = true;
                    anAuxTaxKey = taxFk;
                    jcbFkTaxId_n.setEnabled(false);
                    jbFkTaxId_n.setEnabled(false);
                    jrbTaxCash.setEnabled(false);
                    jrbTaxPend.setEnabled(false);

                    jrbTaxCash.setSelected(true);
                }
                else {
                    mbIsTaxCfg = false;
                    jlFkTaxId_n.setEnabled(true);
                    jcbFkTaxId_n.setEnabled(true);
                    jbFkTaxId_n.setEnabled(true);
                    jrbTaxCash.setEnabled(true);
                    jrbTaxPend.setEnabled(true);

                    jrbTaxCash.setSelected(true);
                }

                SFormUtilities.populateComboBox(miClient, jcbFkTaxId_n, SDataConstants.FINX_TAX_BAS_TAX);
                SFormUtilities.locateComboBoxItem(jcbFkTaxId_n, anAuxTaxKey);
            }
            else {
                moFieldFkTaxId_n.resetField();
                jlFkTaxId_n.setEnabled(false);
                jcbFkTaxId_n.setEnabled(false);
                jbFkTaxId_n.setEnabled(false);
                jrbTaxCash.setEnabled(false);
                jrbTaxPend.setEnabled(false);
                
                bgTax.clearSelection();
            }

            // Check if it is necesary to enable company branch entity fields:
            
            boolean isAccSysCash = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK });
            boolean isAccSysInventory = mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_INV;

            if (oAccountMajor.getIsRequiredEntity() || isAccSysCash || isAccSysInventory) {
                jlFkEntityId_n.setEnabled(true);
                jcbFkEntityId_n.setEnabled(true);
                jbFkEntityId_n.setEnabled(true);

                switch (mnAccountSystemTypeId) {
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                        mnOptionsEntityType = SDataConstants.FINX_ACC_CASH_CASH;
                        oFilterKey = new int[] { moRecord.getFkCompanyBranchId() };   // XXX probably filter will not be used, in order to make moves into other branch accounts
                        break;
                    case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                        mnOptionsEntityType = SDataConstants.FINX_ACC_CASH_BANK;
                        oFilterKey = new int[] { moRecord.getFkCompanyBranchId() };   // XXX probably filter will not be used, in order to make moves into other branch accounts
                        break;
                    case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                        mnOptionsEntityType = SDataConstants.CFGX_COB_ENT_WH;
                        oFilterKey = new int[] { moRecord.getFkCompanyBranchId() };   // XXX probably filter will not be used, in order to make moves into other branch inventories
                        break;
                }

                if (isAccSysCash) {
                    // If there is no entity selected, and record header has a cash account, set it as default entity:

                    if (moRecord.getFkCompanyBranchId_n() != 0 && moRecord.getFkAccountCashId_n() != 0 && (anAuxEntityKey == null || jcbFkEntityId_n.getSelectedIndex() <= 0)) {
                        anAuxEntityKey = new int[] { moRecord.getFkCompanyBranchId_n(), moRecord.getFkAccountCashId_n() };
                    }

                    moEntryAccountCash = oAuxAccountCash;
                }

                SFormUtilities.populateComboBox(miClient, jcbFkEntityId_n, mnOptionsEntityType, oFilterKey);
                SFormUtilities.locateComboBoxItem(jcbFkEntityId_n, anAuxEntityKey);

                itemStateFkEntityId_n();
            }
            else {
                jlFkEntityId_n.setEnabled(false);
                jcbFkEntityId_n.setEnabled(false);
                jbFkEntityId_n.setEnabled(false);

                itemStateFkEntityId_n();
            }

            // Check if it is necesary to enable item fields:

            if (oAccountMajor.getIsRequiredItem() || isAccSysBizPartnerSupCus || isAccSysPurchases || isAccSysSales || isAccSysInventory || isAccSysTax) {
                mnOptionsItemType = mnAccountSystemTypeId;
                mbIsItemRequired = oAccountMajor.getIsRequiredItem();

                jlFkItemId_n.setEnabled(true);
                jcbFkItemId_n.setEnabled(true);
                jbFkItemId_n.setEnabled(true);
                jlFkItemAuxId_n.setEnabled(true);
                jcbFkItemAuxId_n.setEnabled(true);
                jbFkItemAuxId_n.setEnabled(true);

                SFormUtilities.populateComboBox(miClient, jcbFkItemId_n, SDataConstants.ITMU_ITEM);
                SFormUtilities.populateComboBox(miClient, jcbFkItemAuxId_n, SDataConstants.ITMU_ITEM);
                SFormUtilities.locateComboBoxItem(jcbFkItemId_n, anAuxItemKey);
                SFormUtilities.locateComboBoxItem(jcbFkItemAuxId_n, anAuxItemAuxKey);
                moFieldUnits.setFieldValue(dUnits);

                itemStateFkItemId_n();
            }
            else {
                jlFkItemId_n.setEnabled(false);
                jcbFkItemId_n.setEnabled(false);
                jbFkItemId_n.setEnabled(false);
                jlFkItemAuxId_n.setEnabled(false);
                jcbFkItemAuxId_n.setEnabled(false);
                jbFkItemAuxId_n.setEnabled(false);

                itemStateFkItemId_n();
            }

            // Check if it is necesary to enable DPS fields:
            
            boolean isTrnSupplierTax = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT });
            boolean isTrnCustomerTax = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_CUS, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT });

            if (isAccSysBizPartnerSupCus || isAccSysPurchases || isAccSysSales || isAccSysTax || isDiotAccount) {
                jlFkDps.setEnabled(true);
                jtfFkDps.setEnabled(true);
                jbFkDps.setEnabled(true);
                jbFkDpsRemove.setEnabled(true);
                jlFileXml.setEnabled(true);
                jtfFileXml.setEnabled(true);
                jbFileXmlAdd.setEnabled(true);
                updateFilesXmlInfo();

                moEntryDps = oAuxDps;

                if (isAccSysPurchases || isTrnSupplierTax) {
                    manDpsClassKey = SDataConstantsSys.TRNS_CL_DPS_PUR_DOC;
                }
                else {
                    manDpsClassKey = SDataConstantsSys.TRNS_CL_DPS_SAL_DOC;
                }
            }
            else {
                jlFkDps.setEnabled(false);
                jtfFkDps.setEnabled(false);
                jbFkDps.setEnabled(false);
                jbFkDpsRemove.setEnabled(false);
                jlFileXml.setEnabled(false);
                jtfFileXml.setEnabled(false);
                jbFileXml.setEnabled(false);
                jbFileXmlRemove.setEnabled(false);
                jbFileXmlAdd.setEnabled(false);
            }

            // Check if it is necesary to enable DPS adjustment fields:

            if (isTrnSupplierTax || isTrnCustomerTax || SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ, SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ })) {
                jlFkDpsAdj.setEnabled(true);
                jtfFkDpsAdj.setEnabled(true);
                jbFkDpsAdj.setEnabled(true);
                jbFkDpsAdjRemove.setEnabled(true);

                moEntryDpsAdj = oAuxDpsAdj;

                if (isTrnSupplierTax || mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ) {
                    manDpsAdjClassKey = SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ;
                }
                else {
                    manDpsAdjClassKey = SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ;
                }
            }
            else {
                jlFkDpsAdj.setEnabled(false);
                jtfFkDpsAdj.setEnabled(false);
                jbFkDpsAdj.setEnabled(false);
                jbFkDpsAdjRemove.setEnabled(false);
            }

            // Check if it is necesary to enable profit & loss fields:

            if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_PROF_LOSS) {
                jlFkYearId_n.setEnabled(true);
                jtfFkYearId_n.setEnabled(true);

                moFieldFkYearId_n.setFieldValue(nYearId);
            }
            else {
                jlFkYearId_n.setEnabled(false);
                jtfFkYearId_n.setEnabled(false);
            }
            
            jlFileXml.setEnabled(true);
            jtfFileXml.setEnabled(true);
            jbFileXmlAdd.setEnabled(true);
            updateFilesXmlInfo();
        }

        // Once current account settings are set, render documents, if applicable:

        renderDps();
        renderDpsAdj();

        mbResetingForm = false;
    }

    private void renderEntitySettings() {
        if (mnOptionsEntityType == SDataConstants.FINX_ACC_CASH_CASH || mnOptionsEntityType == SDataConstants.FINX_ACC_CASH_BANK) {
            moEntryAccountCash = (SDataAccountCash) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC_CASH, moFieldFkEntityId_n.getKey(), SLibConstants.EXEC_MODE_SILENT);
            jtfEntityCurrencyKey.setText(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGU_CUR, new int[] { moEntryAccountCash.getFkCurrencyId() }, SLibConstants.DESCRIPTION_CODE));
        }
        else {
            moEntryAccountCash = null;
            jtfEntityCurrencyKey.setText("");
        }
    }

    private void renderDps() {
        if (moEntryDps == null) {
            jtfFkDps.setText("");
            jtfFkDps.setToolTipText(null);
        }
        else {
            jtfFkDps.setText(SFinConsts.TXT_INVOICE + " " + moEntryDps.getDpsNumber() + ", " +
                    SLibUtils.DateFormatDate.format(moEntryDps.getDate()) + ", " +
                    "$" + SLibUtils.getDecimalFormatAmount().format(moEntryDps.getTotalCy_r()) + " " + moEntryDps.getDbmsCurrencyKey());
            jtfFkDps.setToolTipText(jtfFkDps.getText());
            jtfFkDps.setCaretPosition(0);
        }
    }

    private void renderDpsAdj() {
        if (moEntryDpsAdj == null) {
            jtfFkDpsAdj.setText("");
            jtfFkDpsAdj.setToolTipText(null);
        }
        else {
            jtfFkDpsAdj.setText(miClient.getSessionXXX().getFormatters().getDateFormat().format(moEntryDpsAdj.getDate()) +
                    ", #" + moEntryDpsAdj.getNumberSeries() + (moEntryDpsAdj.getNumberSeries().length() == 0 ? "" : "-") + moEntryDpsAdj.getNumber() +
                    ", $" + miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(moEntryDpsAdj.getTotalCy_r()) + " " + moEntryDpsAdj.getDbmsCurrencyKey());
            jtfFkDpsAdj.setToolTipText(jtfFkDpsAdj.getText());
            jtfFkDpsAdj.setCaretPosition(0);
        }
    }

    private void renderCurrencySettings() {
        if (jcbFkCurrencyId.getSelectedIndex() != -1)  {
            if (moFieldFkCurrencyId.getKeyAsIntArray()[0] == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                // Local currency:

                jtfExchangeRateSystem.setEnabled(false);
                jtfExchangeRate.setEnabled(false);
                jbExchangeRateSystem.setEnabled(false);
                jbExchangeRate.setEnabled(false);
                jbExchangeRateAccountCashSet.setEnabled(false);
                jbExchangeRateAccountCashView.setEnabled(false);

                jtfDebit.setEnabled(false);
                jtfCredit.setEnabled(false);
                jtfDebitCy.setEnabled(!jckIsExchangeDifference.isSelected());
                jtfCreditCy.setEnabled(!jckIsExchangeDifference.isSelected());

                moFieldDebit.setFieldValue(!jckIsExchangeDifference.isSelected() ? moFieldDebit.getDouble() : 0);
                moFieldCredit.setFieldValue(!jckIsExchangeDifference.isSelected() ? moFieldCredit.getDouble() : 0);
                moFieldDebitCy.setFieldValue(jtfDebitCy.isEnabled() ? moFieldDebitCy.getDouble() : 0);
                moFieldCreditCy.setFieldValue(jtfCreditCy.isEnabled() ? moFieldCreditCy.getDouble() : 0);

                jbDebitCy.setEnabled(false);
                jbCreditCy.setEnabled(false);
                jbDebit.setEnabled(false);
                jbCredit.setEnabled(false);

                moFieldExchangeRateSystem.setFieldValue(jckIsExchangeDifference.isSelected() ? 0d : 1d);
                moFieldExchangeRate.setFieldValue(jckIsExchangeDifference.isSelected() ? 0d : 1d);
            }
            else {
                // Foreign currency:

                jtfExchangeRateSystem.setEnabled(!jckIsExchangeDifference.isSelected());
                jtfExchangeRate.setEnabled(!jckIsExchangeDifference.isSelected());
                jbExchangeRateSystem.setEnabled(!jckIsExchangeDifference.isSelected());
                jbExchangeRate.setEnabled(!jckIsExchangeDifference.isSelected());
                jbExchangeRateAccountCashSet.setEnabled(enableExchangeRateAccountCash());
                jbExchangeRateAccountCashView.setEnabled(enableExchangeRateAccountCash());

                jtfDebit.setEnabled(true);
                jtfCredit.setEnabled(true);
                jtfDebitCy.setEnabled(!jckIsExchangeDifference.isSelected());
                jtfCreditCy.setEnabled(!jckIsExchangeDifference.isSelected());

                moFieldDebitCy.setFieldValue(jtfDebitCy.isEnabled() ? moFieldDebitCy.getDouble() : 0);
                moFieldCreditCy.setFieldValue(jtfCreditCy.isEnabled() ? moFieldCreditCy.getDouble() : 0);
                moFieldExchangeRateSystem.setFieldValue(!jckIsExchangeDifference.isSelected() ? moFieldExchangeRateSystem.getDouble() : 0);
                moFieldExchangeRate.setFieldValue(jtfExchangeRate.isEnabled() ? moFieldExchangeRate.getDouble() : 0);

                jbDebitCy.setEnabled(!jckIsExchangeDifference.isSelected());
                jbCreditCy.setEnabled(!jckIsExchangeDifference.isSelected());
                jbDebit.setEnabled(!jckIsExchangeDifference.isSelected());
                jbCredit.setEnabled(!jckIsExchangeDifference.isSelected());

                if (SLibUtilities.compareKeys(manLastCurrencyKey, new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() }) && !mbResetingForm) {
                    moFieldExchangeRateSystem.setFieldValue(0d);
                    moFieldExchangeRate.setFieldValue(0d);
                }
            }

            // Exchange difference:
            /*
            if (jckIsExchangeDifference.isSelected()) {
                jcbFkCurrencyId.setEnabled(false);
                jbFkCurrencyId.setEnabled(false);
            }
            else {
                jcbFkCurrencyId.setEnabled(true);
                jbFkCurrencyId.setEnabled(true);
            }
            */
            manLastCurrencyKey = moFieldFkCurrencyId.getKeyAsIntArray();
        }
    }

    private erp.mtrn.data.SDataDps obtainDps(int[] classKey) {
        SDataDps dps = null;
        SFormOptionPickerInterface picker = miClient.getOptionPicker(SDataConstants.TRN_DPS);

        picker.formReset();
        picker.setFilterKey(new Object[] { classKey, moFieldFkBizPartnerId_nr.getKeyAsIntArray() });
        picker.formRefreshOptionPane();
        picker.setSelectedPrimaryKey(moEntryDps == null ? null : moEntryDps.getPrimaryKey());
        picker.setFormVisible(true);

        if (picker.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, picker.getSelectedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
        }

        return dps;
    }

    private double[] obtainCurrentAccountCashBalance() {
        double[] balance = new double[2];

        try {
            balance = SDataUtilities.obtainAccountCashBalanceUpdated(miClient,
                moFieldFkCurrencyId.getKeyAsIntArray()[0], moRecord.getDate(),
                moRecord.getDbmsDataAccountCash().getPrimaryKey(),
                moRecord.getDbmsDataAccountCash().getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH ?
                    SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH : SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK,
                moRecord, moRecordEntry);

        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }

        return balance;
    }

    private double obtainTodayExchangeRate() {
        double rate = 0;

        try {
            rate = SDataUtilities.obtainExchangeRate(miClient,
                moFieldFkCurrencyId.getKeyAsIntArray()[0], moRecord.getDate());
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }

        return rate;
    }

    private double obtainTodayExchangeRateAccountCash() {
        double rate = 0;
        double[] balance = null;

        try {
            balance = obtainCurrentAccountCashBalance();
            rate = balance[1] == 0d ? 0d : balance[0] / balance[1];
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }

        return rate;
    }

    private int[] getSystemAccountTypeKey() {
        int[] type = null;

        if (moPanelFkAccountId.getDataAccountMajor() != null) {
            switch (moPanelFkAccountId.getDataAccountMajor().getFkAccountSystemTypeId()) {
                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                    type = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                    type = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                    type = SModSysConsts.FINS_TP_SYS_ACC_ENT_WAH_WAH;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    type = SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    type = SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                    type = SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                    type = SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL;
                    break;
                default:
                    type = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
            }
        }

        return type;
    }

    private int[] getSystemMoveTypeKey() {
        int[] key = null;

        if (moPanelFkAccountId.getDataAccountMajor() != null) {
            switch (moPanelFkAccountId.getDataAccountMajor().getFkAccountSystemTypeId()) {
                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                    key = moFieldDebit.getDouble() >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ : SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    key = moFieldDebit.getDouble() >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    key = moFieldDebit.getDouble() >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                    key = moFieldDebit.getDouble() >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                    key = moFieldDebit.getDouble() >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT:
                    key = SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT:
                    key = SModSysConsts.FINS_TP_SYS_MOV_JOU_CDT;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_PUR:
                    key = SModSysConsts.FINS_TP_SYS_MOV_PUR;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ:
                    key = SModSysConsts.FINS_TP_SYS_MOV_PUR_DEC;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SAL:
                    key = SModSysConsts.FINS_TP_SYS_MOV_SAL;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ:
                    key = SModSysConsts.FINS_TP_SYS_MOV_SAL_DEC;
                    break;

                default:
                    key = moFieldDebit.getDouble() >= 0 ? SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT : SModSysConsts.FINS_TP_SYS_MOV_JOU_CDT;
            }
        }

        return key;
    }

    private int[] getSystemMoveTypeKeyXXX() {
        int[] key = SDataConstantsSys.FINS_TP_SYS_MOV_NA;
        SDataTax tax = null;
        SDataItem item = null;

        if (moPanelFkAccountId.getDataAccountMajor() != null) {
            switch (moPanelFkAccountId.getDataAccountMajor().getFkAccountSystemTypeId()) {
                case SDataConstantsSys.FINS_TP_ACC_SYS_ASSET_FIX:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_ASSET_ASSET;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_ASSET_STOCK;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT:
                    key = jrbTaxCash.isSelected() ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT:
                    key = jrbTaxCash.isSelected() ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_PUR:
                case SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ:
                    item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldFkItemId_n.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                    if (item == null) {
                        key = SDataConstantsSys.FINS_TP_SYS_MOV_PUR_SERV;   // no item was provided!, by the way, it is optional.
                    }
                    else {
                        if (item.getDbmsDataItemGeneric().getFkItemCategoryId() == SDataConstantsSys.ITMS_CT_ITEM_ASS) {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_PUR_ASSET;
                        }
                        else if (SLibUtilities.belongsTo(new int[] { item.getDbmsDataItemGeneric().getFkItemCategoryId(), item.getDbmsDataItemGeneric().getFkItemClassId() },
                                new int[][] { SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO, SDataConstantsSys.ITMS_CL_ITEM_PUR_CON })) {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_PUR_GOOD;
                        }
                        else {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_PUR_SERV;
                        }
                    }
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SAL:
                case SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ:
                    item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldFkItemId_n.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
                    if (item == null) {
                        key = SDataConstantsSys.FINS_TP_SYS_MOV_SAL_SERV;   // no item was provided!, by the way, it is optional.
                    }
                    else {
                        if (item.getDbmsDataItemGeneric().getFkItemCategoryId() == SDataConstantsSys.ITMS_CT_ITEM_ASS) {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_SAL_ASSET;
                        }
                        else if (SLibUtilities.belongsTo(new int[] { item.getDbmsDataItemGeneric().getFkItemCategoryId(), item.getDbmsDataItemGeneric().getFkItemClassId() },
                                new int[][] { SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO, SDataConstantsSys.ITMS_CL_ITEM_PUR_CON })) {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_SAL_GOOD;
                        }
                        else {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_SAL_SERV;
                        }
                    }
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_PROF_LOSS:
                    if (moFieldDebit.getDouble() > 0d || moFieldCredit.getDouble() < 0d) {
                        // Debit:

                        key = SDataConstantsSys.FINS_TP_SYS_MOV_PROF_LOSS;
                    }
                    else {
                        // Credit:

                        key = SDataConstantsSys.FINS_TP_SYS_MOV_PROF_PROF;
                    }
                    break;

                default:
            }
        }

        return key;
    }

    private boolean enableExchangeRateAccountCash() {
        return moRecord != null && moRecord.getDbmsDataAccountCash() != null &&
                SLibUtilities.compareKeys(moRecord.getDbmsDataAccountCash().getPrimaryKey(), moFieldFkEntityId_n.getKey()) &&
                moRecord.getDbmsDataAccountCash().getFkCurrencyId() == moFieldFkCurrencyId.getKeyAsIntArray()[0];
    }

    private void itemStateFkEntityId_n() {
        if (enableExchangeRateAccountCash()) {
            jbExchangeRateAccountCashSet.setEnabled(true);
            jbExchangeRateAccountCashView.setEnabled(true);
        }
        else {
            jbExchangeRateAccountCashSet.setEnabled(false);
            jbExchangeRateAccountCashView.setEnabled(false);
        }
    }

    private void itemStateFkItemId_n() {
        if (!jcbFkItemId_n.isEnabled() || jcbFkItemId_n.getSelectedIndex() <= 0) {
            moItem = null;
            jtfUnits.setEnabled(false);
            jtfUnitsSymbol.setEnabled(false);
            jtfUnitsSymbol.setText("");
        }
        else {
            moItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldFkItemId_n.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
            jtfUnits.setEnabled(true);
            jtfUnitsSymbol.setEnabled(true);
            jtfUnitsSymbol.setText((String) ((SFormComponentItem) jcbFkItemId_n.getSelectedItem()).getComplement());
        }
    }

    private void itemStateFkItemAuxId_n() {
        if (!jcbFkItemAuxId_n.isEnabled() || jcbFkItemAuxId_n.getSelectedIndex() <= 0) {
            moItemAux = null;
        }
        else {
            moItemAux = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, moFieldFkItemAuxId_n.getKeyAsIntArray(), SLibConstants.EXEC_MODE_SILENT);
        }
    }
    
    private void itemStateIsCheckApplying() {
        if (!jckIsCheckApplying.isSelected()) {
            jcbFkCheckId_n.setEnabled(false);
        }
        else {
            jcbFkCheckId_n.setEnabled(true);
            jcbFkCheckId_n.requestFocus();
        }
    }

    private void itemStateIsExchangeDifference() {
        /*if (jckIsExchangeDifference.isSelected()) {
            moFieldFkCurrencyId.setFieldValue(new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() });
        }*/

        renderCurrencySettings();

        //jtfDebitCy.requestFocus();
    }
    
    public void updateFilesXmlInfo() {
        int countFilesXml = 0;
        String nameXml = "";
        
        for (SDataCfdRecordRow row : maCfdRecordRows) {
            if (!row.getNameXml().isEmpty()) {
                countFilesXml++;
                nameXml = row.getNameXml();
            }
        }
        moFieldFileXml.setFieldValue(countFilesXml != 1 ? "" : nameXml);
        jtfXmlFilesNumber.setText(countFilesXml + "");
        
        jbFileXml.setEnabled(countFilesXml == 0);
        jbFileXmlRemove.setEnabled(countFilesXml == 1);
    }

    private void actionFkAccountIdFocusGained() {
        if (msCurrentFkAccountId.compareTo(moPanelFkAccountId.getFieldAccount().getString()) != 0) {
            renderAccountSettings();
        }

        msCurrentFkAccountId = moPanelFkAccountId.getFieldAccount().getString();
    }

    private void actionFkAccountIdFocusLost() {
        if (msCurrentFkAccountId.compareTo(moPanelFkAccountId.getFieldAccount().getString()) != 0) {
            renderAccountSettings();
        }
    }

    private void actionFkEntityId_nFocusGained() {
        manCurrentEntityKey_n = moFieldFkEntityId_n.getKeyAsIntArray();
    }

    private void actionFkEntityId_nFocusLost() {
        if (!SLibUtilities.compareKeys(manCurrentEntityKey_n, moFieldFkEntityId_n.getKeyAsIntArray())) {
            renderEntitySettings();
        }
    }

    private void actionFkCurrencyIdFocusGained() {
        manLastCurrencyKey = moFieldFkCurrencyId.getKeyAsIntArray();
    }

    private void actionFkCurrencyIdFocusLost() {
        if (!SLibUtilities.compareKeys(manLastCurrencyKey, moFieldFkCurrencyId.getKeyAsIntArray())) {
            renderCurrencySettings();
        }
    }

    private void actionjbFkBizPartnerId_nr() {
        miClient.pickOption(mnOptionsBizPartnerType, moFieldFkBizPartnerId_nr, null);
    }

    private void actionReference() {
        // XXX
    }

    private void actionFkTaxId_n() {
        miClient.pickOption(SDataConstants.FINU_TAX, moFieldFkTaxId_n, null);
    }

    private void actionFkEntityId_n() {
        miClient.pickOption(mnOptionsEntityType, moFieldFkEntityId_n, null);
    }

    private void actionFkItemId_n() {
        miClient.pickOption(SDataConstants.ITMU_ITEM, moFieldFkItemId_n, null);
    }

    private void actionFkItemAuxId_n() {
        miClient.pickOption(SDataConstants.ITMU_ITEM, moFieldFkItemAuxId_n, null);
    }

    private void actionFkDps() {
        SDataDps dps = null;

        if (jcbFkBizPartnerId_nr.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkBizPartnerId_nr.getText() + "'");
            jcbFkBizPartnerId_nr.requestFocus();
        }
        else {
            dps = obtainDps(manDpsClassKey);
        }

        if (dps != null) {
            moEntryDps = dps;
            renderDps();
        }
    }

    private void actionLoadFileXml() {
        FileFilter filter = new FileNameExtensionFilter("XML file", "xml");
        miClient.getFileChooser().repaint();
        miClient.getFileChooser().setAcceptAllFileFilterUsed(false);
        miClient.getFileChooser().setFileFilter(filter);

        try {
            if (miClient.getFileChooser().showOpenDialog(miClient.getFrame()) == JFileChooser.APPROVE_OPTION) {
                msXmlPath = miClient.getFileChooser().getSelectedFile().getAbsolutePath();

                if (SCfdUtils.checkCompanyAsCfdiReceptor(miClient, msXmlPath)) {
                    maCfdRecordRows.add(new SDataCfdRecordRow(maCfdRecordRows.size() + 1, 0, miClient.getFileChooser().getSelectedFile().getName(), miClient.getFileChooser().getSelectedFile().getAbsolutePath()));
                }
            }
            updateFilesXmlInfo();
            miClient.getFileChooser().resetChoosableFileFilters();
            miClient.getFileChooser().setAcceptAllFileFilterUsed(true);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    private void actionFkDpsRemove() {
        moEntryDps = null;
        renderDps();
    }

    private void actionFileXmlDelete() {
        int index = 0;
       int countFilesXml = 0;
       
       for (int i = 0; i < maCfdRecordRows.size(); i++) {
           if (!maCfdRecordRows.get(i).getNameXml().isEmpty()) {
                countFilesXml++;
                index = i;
            }
       }
       
        if (countFilesXml == 1) {
            maCfdRecordRows.get(index).setNameXml("");
        }
        updateFilesXmlInfo();
    }
    
    private void actionFileXmlAdd() {
        SDialogRecordEntryXml recordEntryDps = null;
        ArrayList<SDataCfdRecordRow> aCfdRecordRows = null;
        
        aCfdRecordRows = new ArrayList<>();
        
        for (SDataCfdRecordRow row : maCfdRecordRows) {
            aCfdRecordRows.add(new SDataCfdRecordRow(row.getMoveId(), row.getCfdId(), row.getNameXml(), row.getPathXml()));
        }
        
        recordEntryDps = new SDialogRecordEntryXml(miClient, aCfdRecordRows);
        recordEntryDps.setVisible(true);
        
        if (recordEntryDps.getFormResult() == SLibConstants.FORM_RESULT_OK) {
            maCfdRecordRows.clear();
            maCfdRecordRows = recordEntryDps.getGridRows();
        }
        updateFilesXmlInfo();
    }
    
    private void actionGetXml() {
        try {
            HashSet<SDataCfd> cfds = new HashSet<>();
            maCfdRecordRows.stream().map((cfdRecordRow) -> (SDataCfd) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_CFD, new int[] { cfdRecordRow.getCfdId() }, SLibConstants.EXEC_MODE_SILENT)).forEach((cfd) -> {
                cfds.add(cfd);
            });
            SCfdUtils.getXmlCfds(miClient, cfds);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    private void actionFkDpsAdj() {
        SDataDps dpsAdj = null;

        if (jcbFkBizPartnerId_nr.getSelectedIndex() <= 0) {
            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkBizPartnerId_nr.getText() + "'");
            jcbFkBizPartnerId_nr.requestFocus();
        }
        else {
            dpsAdj = obtainDps(manDpsAdjClassKey);
        }

        if (dpsAdj != null) {
            moEntryDpsAdj = dpsAdj;
            renderDpsAdj();
        }
    }

    private void actionFkDpsAdjRemove() {
        moEntryDpsAdj = null;
        renderDpsAdj();
    }

    private void actionFkCurrencyId() {
        manLastCurrencyKey = moFieldFkCurrencyId.getKeyAsIntArray();

        if (miClient.pickOption(SDataConstants.CFGU_CUR, moFieldFkCurrencyId, null) == SLibConstants.FORM_RESULT_OK) {
            if (!SLibUtilities.compareKeys(manLastCurrencyKey, moFieldFkCurrencyId.getKeyAsIntArray())) {
                renderCurrencySettings();
            }
        }
    }

    private void actionExchangeRateSystem() {
        double rate = miClient.pickExchangeRate(moFieldFkCurrencyId.getKeyAsIntArray()[0], moRecord.getDate());

        if (rate != 0d) {
            moFieldExchangeRateSystem.setFieldValue(rate);
            moFieldExchangeRate.setFieldValue(rate);
            jtfExchangeRate.requestFocus();
        }
    }

    private void actionDebitCy() {
        if (moFieldExchangeRate.getDouble() == 0d) {
            moFieldExchangeRate.getComponent().requestFocus();
        }
        else {
            moFieldDebitCy.setFieldValue(SLibUtilities.round(moFieldDebit.getDouble() / moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
            moFieldDebitCy.getComponent().requestFocus();
        }
    }

    private void actionCreditCy() {
        if (moFieldExchangeRate.getDouble() == 0d) {
            moFieldExchangeRate.getComponent().requestFocus();
        }
        else {
            moFieldCreditCy.setFieldValue(SLibUtilities.round(moFieldCredit.getDouble() / moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
            moFieldCreditCy.getComponent().requestFocus();
        }
    }

    private void actionDebit() {
        if (moFieldExchangeRate.getDouble() == 0d) {
            moFieldExchangeRate.getComponent().requestFocus();
        }
        else {
            moFieldDebit.setFieldValue(SLibUtilities.round(moFieldDebitCy.getDouble() * moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
            moFieldDebit.getComponent().requestFocus();
        }
    }

    private void actionCredit() {
        if (moFieldExchangeRate.getDouble() == 0d) {
            moFieldExchangeRate.getComponent().requestFocus();
        }
        else {
            moFieldCredit.setFieldValue(SLibUtilities.round(moFieldCreditCy.getDouble() * moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
            moFieldCredit.getComponent().requestFocus();
        }
    }

    private void actionExchangeRate() {
        if (moFieldDebitCy.getDouble() != 0d) {
            if (moFieldDebit.getDouble() == 0d) {
                moFieldDebit.getComponent().requestFocus();
            }
            else {
                moFieldExchangeRate.setFieldValue(SLibUtilities.round(moFieldDebit.getDouble() / moFieldDebitCy.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsExchangeRate()));
                moFieldExchangeRate.getComponent().requestFocus();
            }
        }
        else if (moFieldCreditCy.getDouble() != 0d) {
            if (moFieldCredit.getDouble() == 0d) {
                moFieldCredit.getComponent().requestFocus();
            }
            else {
                moFieldExchangeRate.setFieldValue(SLibUtilities.round(moFieldCredit.getDouble() / moFieldCreditCy.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsExchangeRate()));
                moFieldExchangeRate.getComponent().requestFocus();
            }
        }
        else if (moFieldDebit.getDouble() != 0d) {
            moFieldDebit.getComponent().requestFocus();
        }
        else if (moFieldCredit.getDouble() != 0d) {
            moFieldCredit.getComponent().requestFocus();
        }
        else {
            moFieldExchangeRate.getComponent().requestFocus();
        }
    }

    private void actionExchangeRateAccountCashView() {
        double[] balance = null;

        if (jcbFkCurrencyId.getSelectedIndex() > 0) {
            try {
                balance = obtainCurrentAccountCashBalance();

                miClient.showMsgBoxInformation(
                        "Tipo de cambio acumulado al día: " + miClient.getSessionXXX().getFormatters().getDateFormat().format(moRecord.getDate()) + "\n" +
                        "Saldo " + miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey() + ": $ " +
                        miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(balance[0]) + ".\n" +
                        "Saldo " + ((SFormComponentItem) jcbFkCurrencyId.getSelectedItem()).getComplement() + ": $ " +
                        miClient.getSessionXXX().getFormatters().getDecimalsValueFormat().format(balance[1]) + ".\n" +
                        "Tipo de cambio acumulado: " + miClient.getSessionXXX().getFormatters().getDecimalsExchangeRateFormat().format(balance[1] == 0d ? 0d : balance[0] / balance[1]) + ".");
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
        }
    }

    private void actionExchangeRateAccountCashSet() {
        if (jcbFkCurrencyId.getSelectedIndex() > 0) {
            moFieldExchangeRate.setFieldValue(obtainTodayExchangeRateAccountCash());
            jtfExchangeRate.requestFocus();
        }
    }

    private void actionDebitCyFocusLost() {
        if (moFieldDebitCy.getDouble() != 0d) {
            // Clear counterpart:

            moFieldCreditCy.setFieldValue(0d);
            moFieldCredit.setFieldValue(0d);

            if (!jtfDebit.isEnabled() || moFieldDebit.getDouble() == 0d) {
                double rate = 0;

                if (moFieldExchangeRate.getDouble() == 0d && moFieldFkCurrencyId.getKeyAsIntArray()[0] != miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                    rate = obtainTodayExchangeRate();
                    moFieldExchangeRateSystem.setFieldValue(rate);
                    moFieldExchangeRate.setFieldValue(rate);
                }

                moFieldDebit.setFieldValue(SLibUtilities.round(moFieldDebitCy.getDouble() * moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
            }
            else if (moFieldExchangeRate.getDouble() == 0d) {
                moFieldExchangeRate.setFieldValue(SLibUtilities.round(moFieldDebit.getDouble() / moFieldDebitCy.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsExchangeRate()));
            }
        }
    }

    private void actionCreditCyFocusLost() {
        if (moFieldCreditCy.getDouble() != 0d) {
            // Clear counterpart:

            moFieldDebitCy.setFieldValue(0d);
            moFieldDebit.setFieldValue(0d);

            if (!jtfCredit.isEnabled() || moFieldCredit.getDouble() == 0d) {
                double rate = 0;

                if (moFieldExchangeRate.getDouble() == 0d && moFieldFkCurrencyId.getKeyAsIntArray()[0] != miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                    if (enableExchangeRateAccountCash()) {
                        moFieldExchangeRateSystem.setFieldValue(obtainTodayExchangeRate());
                        moFieldExchangeRate.setFieldValue(obtainTodayExchangeRateAccountCash());
                    }
                    else {
                        rate = obtainTodayExchangeRate();
                        moFieldExchangeRateSystem.setFieldValue(rate);
                        moFieldExchangeRate.setFieldValue(rate);
                    }
                }

                moFieldCredit.setFieldValue(SLibUtilities.round(moFieldCreditCy.getDouble() * moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
            }
            else if (moFieldExchangeRate.getDouble() == 0d) {
                moFieldExchangeRate.setFieldValue(SLibUtilities.round(moFieldCredit.getDouble() / moFieldCreditCy.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsExchangeRate()));
            }
        }
    }

    private void actionDebitFocusLost() {
        if (moFieldDebit.getDouble() != 0d) {
            // Clear counterpart:

            moFieldCreditCy.setFieldValue(0d);
            moFieldCredit.setFieldValue(0d);

            if (moFieldDebitCy.getDouble() == 0d && moFieldExchangeRate.getDouble() != 0d) {
                moFieldDebitCy.setFieldValue(SLibUtilities.round(moFieldDebit.getDouble() / moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
            }
            else if (moFieldDebitCy.getDouble() != 0d && moFieldExchangeRate.getDouble() == 0d) {
                moFieldExchangeRate.setFieldValue(SLibUtilities.round(moFieldDebit.getDouble() / moFieldDebitCy.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsExchangeRate()));
            }
        }
    }

    private void actionCreditFocusLost() {
        if (moFieldCredit.getDouble() != 0d) {
            // Clear counterpart:

            moFieldDebitCy.setFieldValue(0d);
            moFieldDebit.setFieldValue(0d);

            if (moFieldCreditCy.getDouble() == 0d && moFieldExchangeRate.getDouble() != 0d) {
                moFieldCreditCy.setFieldValue(SLibUtilities.round(moFieldCredit.getDouble() / moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
            }
            else if (moFieldCreditCy.getDouble() != 0d && moFieldExchangeRate.getDouble() == 0d) {
                moFieldExchangeRate.setFieldValue(SLibUtilities.round(moFieldCredit.getDouble() / moFieldCreditCy.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsExchangeRate()));
            }
        }
    }

    private void actionExchangeRateFocusLost() {
        if (moFieldDebitCy.getDouble() != 0d && moFieldDebit.getDouble() == 0d) {
            moFieldDebit.setFieldValue(SLibUtilities.round(moFieldDebitCy.getDouble() * moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
        }
        else if (moFieldCreditCy.getDouble() != 0d && moFieldCredit.getDouble() == 0d) {
            moFieldCredit.setFieldValue(SLibUtilities.round(moFieldCreditCy.getDouble() * moFieldExchangeRate.getDouble(),
                    miClient.getSessionXXX().getParamsErp().getDecimalsValue()));
        }
    }

    private void actionEdit(boolean edit) {

    }

    private void actionOk() {
        SFormValidation validation = null;

        triggerFocusLost();     // this forces all pending focus lost function to be called

        validation = formValidate();

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

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgTax;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCredit;
    private javax.swing.JButton jbCreditCy;
    private javax.swing.JButton jbDebit;
    private javax.swing.JButton jbDebitCy;
    private javax.swing.JButton jbExchangeRate;
    private javax.swing.JButton jbExchangeRateAccountCashSet;
    private javax.swing.JButton jbExchangeRateAccountCashView;
    private javax.swing.JButton jbExchangeRateSystem;
    private javax.swing.JButton jbFileXml;
    private javax.swing.JButton jbFileXmlAdd;
    private javax.swing.JButton jbFileXmlRemove;
    private javax.swing.JButton jbFkBizPartnerId_nr;
    private javax.swing.JButton jbFkCurrencyId;
    private javax.swing.JButton jbFkDps;
    private javax.swing.JButton jbFkDpsAdj;
    private javax.swing.JButton jbFkDpsAdjRemove;
    private javax.swing.JButton jbFkDpsRemove;
    private javax.swing.JButton jbFkEntityId_n;
    private javax.swing.JButton jbFkItemAuxId_n;
    private javax.swing.JButton jbFkItemId_n;
    private javax.swing.JButton jbFkTaxId_n;
    private javax.swing.JButton jbGetXml;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbReference;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkBizPartnerId_nr;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCheckId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkCurrencyId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkEntityId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemAuxId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkItemId_n;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkTaxId_n;
    private javax.swing.JComboBox jcbOccasionalFiscalId;
    private javax.swing.JCheckBox jckIsCheckApplying;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsExchangeDifference;
    private javax.swing.JCheckBox jckIsReferenceTax;
    private javax.swing.JCheckBox jckIsSystem;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlCredit;
    private javax.swing.JLabel jlCreditCy;
    private javax.swing.JLabel jlDebit;
    private javax.swing.JLabel jlDebitCy;
    private javax.swing.JLabel jlDummy02;
    private javax.swing.JLabel jlDummy03;
    private javax.swing.JLabel jlDummy11;
    private javax.swing.JLabel jlDummy13;
    private javax.swing.JLabel jlDummy15;
    private javax.swing.JLabel jlDummyAccount;
    private javax.swing.JLabel jlDummyCostCenter;
    private javax.swing.JLabel jlExchangeRate;
    private javax.swing.JLabel jlExchangeRateSystem;
    private javax.swing.JLabel jlFileXml;
    private javax.swing.JLabel jlFkBizPartnerId_nr;
    private javax.swing.JLabel jlFkCurrencyId;
    private javax.swing.JLabel jlFkDps;
    private javax.swing.JLabel jlFkDpsAdj;
    private javax.swing.JLabel jlFkEntityId_n;
    private javax.swing.JLabel jlFkItemAuxId_n;
    private javax.swing.JLabel jlFkItemId_n;
    private javax.swing.JLabel jlFkTaxId_n;
    private javax.swing.JLabel jlFkYearId_n;
    private javax.swing.JLabel jlOccasionalFiscalId;
    private javax.swing.JLabel jlReference;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpRegistryCenter;
    private javax.swing.JPanel jpRegistryCenterNorth;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JPanel jpValue;
    private javax.swing.JRadioButton jrbTaxCash;
    private javax.swing.JRadioButton jrbTaxPend;
    private javax.swing.JTextField jtfConcept;
    private javax.swing.JTextField jtfCredit;
    private javax.swing.JTextField jtfCreditCy;
    private javax.swing.JTextField jtfDebit;
    private javax.swing.JTextField jtfDebitCy;
    private javax.swing.JTextField jtfEntityCurrencyKey;
    private javax.swing.JTextField jtfExchangeRate;
    private javax.swing.JTextField jtfExchangeRateSystem;
    private javax.swing.JTextField jtfFileXml;
    private javax.swing.JTextField jtfFkDps;
    private javax.swing.JTextField jtfFkDpsAdj;
    private javax.swing.JTextField jtfFkYearId_n;
    private javax.swing.JTextField jtfReference;
    private javax.swing.JTextField jtfUnits;
    private javax.swing.JTextField jtfUnitsSymbol;
    private javax.swing.JTextField jtfXmlFilesNumber;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mbResetingForm = true;

        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moRecordEntry = null;
        moRecord = null;

        msCurrentFkAccountId = "";
        manCurrentEntityKey_n = null;
        manLastCurrencyKey = null;

        for (int i = 0; i < mvFields.size(); i++) {
            mvFields.get(i).resetField();
        }

        moFieldFkCurrencyId.setFieldValue(new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() });
        jckIsCheckApplying.setSelected(false);
        jckIsExchangeDifference.setSelected(false);
        jckIsSystem.setSelected(false);
        jckIsDeleted.setSelected(false);

        jckIsDeleted.setEnabled(false);

        moPanelFkAccountId.resetPanel();
        moPanelFkCostCenterId_n.resetPanel();

        jcbFkBizPartnerId_nr.removeAllItems();
        jcbFkTaxId_n.removeAllItems();
        jcbFkEntityId_n.removeAllItems();
        jcbFkItemId_n.removeAllItems();
        jcbFkItemAuxId_n.removeAllItems();
        msXmlPath = "";
        jtfXmlFilesNumber.setText("");
        maCfdRecordRows = new ArrayList<>();

        renderCurrencySettings();
        renderAccountSettings();

        renderEntitySettings();

        renderDps();
        renderDpsAdj();

        itemStateFkEntityId_n();
        itemStateFkItemId_n();
        itemStateIsCheckApplying();

        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkCurrencyId, SDataConstants.CFGU_CUR);
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        String message = "";
        String currency = "";
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(mvFields.get(i).getComponent());
                break;
            }
        }

        if (!validation.getIsError()) {
            // Validate account:

            message = SDataUtilities.validateAccount(miClient, moPanelFkAccountId.getCurrentInputAccount(), moRecord.getDate());

            if (!message.isEmpty()) {
                validation.setMessage(message);
                validation.setComponent(moPanelFkAccountId.getFieldAccount().getComponent());
            }
            else {
                SDataTax tax = null;
                if (jcbFkTaxId_n.getSelectedIndex() > 0) {
                    tax = (SDataTax) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX, moFieldFkTaxId_n.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);
                }

                boolean isDiotAccount = false;
                try {
                    isDiotAccount = SDiotUtils.isDiotAccount(miClient.getSession().getStatement(), moPanelFkAccountId.getDataAccountMajor()) || 
                            SDiotUtils.isDiotAccount(miClient.getSession().getStatement(), moPanelFkAccountId.getCurrentInputAccount());
                }
                catch (Exception e) {
                    SLibUtils.showException(this, e);
                }
                
                boolean isNotDefinedBizPartner = jcbFkBizPartnerId_nr.isEnabled() && jcbFkBizPartnerId_nr.getSelectedIndex() <= 0 && jcbOccasionalFiscalId.isEnabled() && moFieldOccasionalFiscalId.getString().isEmpty();
                boolean mustBeDefinedItem = mbIsItemRequired || SLibUtilities.belongsTo(mnOptionsItemType, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PUR, SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ, SDataConstantsSys.FINS_TP_ACC_SYS_SAL, SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ }); // convenience variable
                boolean isDefinedItemAux = jcbFkItemAuxId_n.isEnabled() && jcbFkItemAuxId_n.getSelectedIndex() > 0;
                boolean isDefinedUnits = jtfUnits.isEnabled() && moFieldUnits.getDouble() != 0d;
                
                if (mbIsBizPartnerRequired && jcbFkBizPartnerId_nr.isEnabled() && jcbFkBizPartnerId_nr.getSelectedIndex() <= 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkBizPartnerId_nr.getText() + "'.");
                    validation.setComponent(jcbFkBizPartnerId_nr);
                }
                else if (jcbFkBizPartnerId_nr.isEnabled() && jcbFkBizPartnerId_nr.getSelectedIndex() > 0 && jcbOccasionalFiscalId.isEnabled() && !moFieldOccasionalFiscalId.getString().isEmpty()) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkBizPartnerId_nr.getText() + "' o '" + jlOccasionalFiscalId.getText() + "', pero no para ambos.");
                    validation.setComponent(jcbFkBizPartnerId_nr);
                }
                else if (isNotDefinedBizPartner && isDiotAccount && miClient.showMsgBoxConfirm("¿Está seguro que no desea proporcionar un valor para el campo '" + jlFkBizPartnerId_nr.getText() + "' o '" + jlOccasionalFiscalId.getText() + "'?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkBizPartnerId_nr.getText() + "' o '" + jlOccasionalFiscalId.getText() + "'.");
                    validation.setComponent(jcbFkBizPartnerId_nr);
                }
                else if (isNotDefinedBizPartner && jcbFkTaxId_n.isEnabled() && jcbFkTaxId_n.getSelectedIndex() > 0 && miClient.showMsgBoxConfirm("¿Está seguro que no desea proporcionar un valor para el campo '" + jlFkBizPartnerId_nr.getText() + "' o '" + jlOccasionalFiscalId.getText() + "'?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkBizPartnerId_nr.getText() + "' o '" + jlOccasionalFiscalId.getText() + "'.");
                    validation.setComponent(jcbFkBizPartnerId_nr);
                }
                else if (mbIsTaxRequired && jcbFkTaxId_n.isEnabled() && jcbFkTaxId_n.getSelectedIndex() <= 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkTaxId_n.getText() + "'.");
                    validation.setComponent(jcbFkTaxId_n);
                }
                else if (isDiotAccount && tax != null && tax.getVatType().isEmpty()) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlFkTaxId_n.getText() + "'.\n" + SDataTax.ERR_MSG_VAT_TYPE + "'" + tax.getTax() + "'.");
                    validation.setComponent(jcbFkTaxId_n);
                }
                else if (jcbFkEntityId_n.isEnabled() && jcbFkEntityId_n.getSelectedIndex() <= 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkEntityId_n.getText() + "'.");
                    validation.setComponent(jcbFkEntityId_n);
                }
                else if ((jcbFkItemId_n.isEnabled() && jcbFkItemId_n.getSelectedIndex() <= 0) && (mustBeDefinedItem || isDefinedItemAux || isDefinedUnits)) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkItemId_n.getText() + "'.");
                    validation.setComponent(jcbFkItemId_n);
                }
                else if (isDefinedItemAux && isDefinedUnits) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_NOT_REQ + "'" + jtfUnits.getToolTipText() + "'.");
                    validation.setComponent(jtfUnits);
                }
                else if (jtfFkYearId_n.isEnabled() && !moFieldFkYearId_n.validateFieldForcing()) {
                    validation.setIsError(true);
                    validation.setComponent(jtfFkYearId_n);
                }
                else if (jckIsCheckApplying.isSelected() && jcbFkCheckId_n.getSelectedIndex() <= 0) {
                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jckIsCheckApplying.getText() + "'.");
                    validation.setComponent(jcbFkCheckId_n);
                }
                else {
                    boolean isCompany = moFieldFkBizPartnerId_nr.getKeyAsIntArray() != null && moFieldFkBizPartnerId_nr.getKeyAsIntArray()[0] == miClient.getSessionXXX().getCurrentCompany().getPkCompanyId();
                    
                    if (moEntryDps == null && moEntryDpsAdj != null) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkDps.getText() + "'.");
                        validation.setComponent(jbFkDps);
                    }
                    else if (moEntryDps == null && isDiotAccount && !isCompany && miClient.showMsgBoxConfirm("¿Está seguro que no desea proporcionar un valor para el campo '" + jlFkDps.getText() + "'?") != JOptionPane.YES_OPTION) {
                        validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlFkDps.getText() + "'.");
                        validation.setComponent(jbFkDps);
                    }
                    else if (moEntryDps != null && moFieldFkCurrencyId.getKeyAsIntArray()[0] != moEntryDps.getFkCurrencyId()) {
                        validation.setMessage("El valor para el campo '" + jlFkCurrencyId.getText() + "' debe ser: '" + moEntryDps.getDbmsCurrency() + "'.");
                        validation.setComponent(jcbFkCurrencyId);
                    }
                    else if (moEntryDps != null && moEntryDpsAdj != null && moEntryDps.getFkCurrencyId() != moEntryDpsAdj.getFkCurrencyId()) {
                        validation.setMessage("La moneda de los campos '" + jlFkDps.getText() + "' y '" + jlFkDpsAdj.getText() + "' debe ser la misma.");
                        validation.setComponent(jbFkDps);
                    }
                    else {
                        if (moFieldDebitCy.getDouble() == 0d && moFieldCreditCy.getDouble() == 0d && !jckIsExchangeDifference.isSelected()) {
                            if (miClient.showMsgBoxConfirm("No se ha especificado un valor para los campos '" + jlDebitCy.getText() + "' o '" + jlCreditCy.getText() + "'.\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDebitCy.getText() + "' o '" + jlCreditCy.getText() + "'.");
                                validation.setComponent(jtfDebitCy);
                            }
                        }

                        if (!validation.getIsError()) {
                            if (jckIsExchangeDifference.isSelected() && moFieldFkCurrencyId.getKeyAsIntArray()[0] == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                                validation.setMessage("El valor para el campo '" + jlFkCurrencyId.getText() + "' debe ser diferente de: '" + miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getCurrency() + "',\n" +
                                        " debido a que está seleccionado el campo '" + jckIsExchangeDifference.getText() + "'.");
                                validation.setComponent(jcbFkCurrencyId);
                            }

                            if (moFieldDebit.getDouble() == 0d && moFieldCredit.getDouble() == 0d) {
                               if (miClient.showMsgBoxConfirm("No se ha especificado un valor para los campos '" + jlDebit.getText() + "' o '" + jlCredit.getText() + "'.\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlDebit.getText() + "' o '" + jlCredit.getText() + "'.");
                                    validation.setComponent(jtfDebit);
                                }
                            }

                            if (!validation.getIsError()) {
                                if (moFieldExchangeRate.getDouble() == 0d && !jckIsExchangeDifference.isSelected()) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + jlExchangeRate.getText() + "'.");
                                    validation.setComponent(jtfExchangeRate);
                                }
                                else if (moPanelFkCostCenterId_n.isEmptyAccountId() && (
                                        moPanelFkAccountId.getDataAccountMajor().getFkAccountTypeId_r() == SDataConstantsSys.FINS_TP_ACC_RES ||
                                        moPanelFkAccountId.getDataAccountMajor().getDbmsIsRequiredCostCenter() ||
                                        moPanelFkAccountId.getCurrentInputAccount().getDbmsIsRequiredCostCenter())) {
                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + moPanelFkCostCenterId_n.getFieldAccountLabel().getText() + "'.");
                                    validation.setComponent(moPanelFkCostCenterId_n.getFieldAccount().getComponent());
                                }
                                else {
                                    if (!moPanelFkCostCenterId_n.isEmptyAccountId()) {
                                        // Cost center has been specified and must be validated:

                                        message = SDataUtilities.validateCostCenter(miClient, moPanelFkCostCenterId_n.getCurrentInputCostCenter(), moRecord.getDate());

                                        if (message.length() > 0) {
                                            validation.setMessage(message);
                                            validation.setComponent(moPanelFkCostCenterId_n.getFieldAccount().getComponent());
                                        }
                                    }

                                    if (!validation.getIsError()) {
                                        if (moRecord.getDbmsDataAccountCash() != null) {
                                            // Record has a cash account, validate record entry currency:

                                            if (moRecord.getDbmsDataAccountCash().getFkCurrencyId() != moFieldFkCurrencyId.getKeyAsIntArray()[0] && !jckIsExchangeDifference.isSelected()) {
                                                currency = SDataReadDescriptions.getCatalogueDescription(miClient,
                                                        SDataConstants.CFGU_CUR, new int[] { moRecord.getDbmsDataAccountCash().getFkCurrencyId() });
                                                if (miClient.showMsgBoxConfirm("La moneda de esta partida no coincide con " +
                                                        "la moneda de la cuenta de efectivo de la póliza contable (" + currency + ").\n" +
                                                        "¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                                    validation.setMessage("El valor para el campo '" + jlFkCurrencyId.getText() + "' debe ser: '" + currency + "'.");
                                                    validation.setComponent(jcbFkCurrencyId);
                                                }
                                            }
                                        }

                                        if (!validation.getIsError()) {
                                            if (moEntryAccountCash != null) {
                                                // Record entry has a cash account:

                                                // Validate bookeeping account:

                                                if (moEntryAccountCash.getFkAccountId().compareTo(moPanelFkAccountId.getCurrentInputAccount().getPkAccountIdXXX()) != 0) {
                                                    if (miClient.showMsgBoxConfirm("La cuenta contable de esta partida no coincide con " +
                                                            "la cuenta contable de la cuenta de efectivo de la partida (" + moEntryAccountCash.getFkAccountId() + ").\n" +
                                                            "¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                                        validation.setMessage("El valor para el campo '" + moPanelFkAccountId.getFieldAccountLabel().getText() + "' debe ser: '" + moEntryAccountCash.getFkAccountId() + "'.");
                                                        validation.setComponent(moPanelFkAccountId.getFieldAccount().getComponent());
                                                    }
                                                }

                                                if (!validation.getIsError()) {
                                                    // Validate record entry currency:

                                                    if (moEntryAccountCash.getFkCurrencyId() != moFieldFkCurrencyId.getKeyAsIntArray()[0] && !jckIsExchangeDifference.isSelected()) {
                                                        currency = SDataReadDescriptions.getCatalogueDescription(miClient,
                                                                SDataConstants.CFGU_CUR, new int[] { moEntryAccountCash.getFkCurrencyId() });
                                                        if (miClient.showMsgBoxConfirm("La moneda de esta partida no coincide con " +
                                                                "la moneda de la cuenta de efectivo de la partida (" + currency + ").\n" +
                                                                "¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                                            validation.setMessage("El valor para el campo '" + jlFkCurrencyId.getText() + "' debe ser: '" + currency + "'.");
                                                            validation.setComponent(jcbFkCurrencyId);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (!validation.getIsError()) {
                                        // Validate than amounts in domestic and original currencies are correct:

                                        if (moFieldDebitCy.getDouble() != 0d) {
                                            // Validate debit amount:
                                            message = SDataUtilities.validateExchangeRate(miClient, moFieldDebitCy.getDouble(), moFieldExchangeRate.getDouble(), moFieldDebit.getDouble(), jlExchangeRate.getText());
                                            if (message.length() > 0) {
                                                if (miClient.showMsgBoxConfirm(message + "\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDebitCy.getText() + "'.");
                                                    validation.setComponent(jtfDebitCy);
                                                }
                                            }
                                        }
                                        else {
                                            // Validate credit amount:
                                            message = SDataUtilities.validateExchangeRate(miClient, moFieldCreditCy.getDouble(), moFieldExchangeRate.getDouble(), moFieldCredit.getDouble(), jlExchangeRate.getText());
                                            if (message.length() > 0) {
                                                if (miClient.showMsgBoxConfirm(message + "\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                                    validation.setMessage(SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + jlDebitCy.getText() + "'.");
                                                    validation.setComponent(jtfDebitCy);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        if (!validation.getIsError()) {
                            if (moEntryDps != null && (jcbFkBizPartnerId_nr.isEnabled() && jcbFkBizPartnerId_nr.getSelectedIndex() > 0)) { 
                                if (moEntryDps.getFkBizPartnerId_r() != moFieldFkBizPartnerId_nr.getKeyAsIntArray()[0]) {
                                    validation.setMessage("El asociado de negocios de la partida debe ser igual al del documento seleccionado.");
                                    validation.setComponent(jcbFkBizPartnerId_nr);
                                }
                            }
                            
                            if (!validation.getIsError() && moEntryDpsAdj != null && (jcbFkBizPartnerId_nr.isEnabled() && jcbFkBizPartnerId_nr.getSelectedIndex() > 0)) { 
                                if (moEntryDpsAdj.getFkBizPartnerId_r() != moFieldFkBizPartnerId_nr.getKeyAsIntArray()[0]) {
                                    validation.setMessage("El asociado de negocios de la partida debe ser igual al del documento de ajuste seleccionado.");
                                    validation.setComponent(jcbFkBizPartnerId_nr);
                                }
                            }
                        }
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
        mbResetingForm = true;

        moRecordEntry = (SDataRecordEntry) registry;

        moFieldConcept.setFieldValue(moRecordEntry.getConcept());
        moFieldDebit.setFieldValue(moRecordEntry.getDebit());
        moFieldCredit.setFieldValue(moRecordEntry.getCredit());
        moFieldExchangeRate.setFieldValue(moRecordEntry.getExchangeRate());
        moFieldExchangeRateSystem.setFieldValue(moRecordEntry.getExchangeRateSystem());
        moFieldDebitCy.setFieldValue(moRecordEntry.getDebitCy());
        moFieldCreditCy.setFieldValue(moRecordEntry.getCreditCy());
        moFieldFkCurrencyId.setFieldValue(new int[] { moRecordEntry.getFkCurrencyId() });
        jckIsCheckApplying.setSelected(moRecordEntry.getAuxCheckNumber() != 0 || (moRecordEntry.getFkCheckWalletId_n() != 0 && moRecordEntry.getFkCheckId_n() != 0));
        jckIsExchangeDifference.setSelected(moRecordEntry.getIsExchangeDifference());
        jckIsSystem.setSelected(moRecordEntry.getIsSystem());
        jckIsDeleted.setSelected(moRecordEntry.getIsDeleted());

        msCurrentFkAccountId = moRecordEntry.getFkAccountIdXXX();
        moPanelFkAccountId.getFieldAccount().setFieldValue(msCurrentFkAccountId);
        moPanelFkAccountId.refreshPanel();

        moPanelFkCostCenterId_n.getFieldAccount().setFieldValue(moRecordEntry.getFkCostCenterIdXXX_n().length() == 0 ? moPanelFkCostCenterId_n.getEmptyAccountId() : moRecordEntry.getFkCostCenterIdXXX_n());
        moPanelFkCostCenterId_n.refreshPanel();

        renderCurrencySettings();
        renderAccountSettings();

        moFieldFkBizPartnerId_nr.setFieldValue(new int[] { moRecordEntry.getFkBizPartnerId_nr() });
        moFieldOccasionalFiscalId.setFieldValue(moRecordEntry.getOccasionalFiscalId());
        moFieldReference.setFieldValue(moRecordEntry.getReference());
        moFieldIsReferenceTax.setFieldValue(moRecordEntry.getIsReferenceTax());
        
        moFieldFkTaxId_n.setFieldValue(new int[] { moRecordEntry.getFkTaxBasicId_n(), moRecordEntry.getFkTaxId_n() });
        if (SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] {  // mnAccountSystemTypeId set on method renderAccountSettings()
            SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT })) {
            if (SLibUtilities.belongsTo(moRecordEntry.getKeySystemMoveTypeXXX(), new int[][] { SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT, SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT })) {
                jrbTaxCash.setSelected(true);
            }
            else {
                jrbTaxPend.setSelected(true);
            }
        }
        
        moFieldFkEntityId_n.setFieldValue(new int[] { moRecordEntry.getFkCompanyBranchId_n(), moRecordEntry.getFkEntityId_n() });
        moFieldFkItemId_n.setFieldValue(new int[] { moRecordEntry.getFkItemId_n() });
        moFieldUnits.setFieldValue(moRecordEntry.getUnits());
        moFieldFkItemAuxId_n.setFieldValue(new int[] { moRecordEntry.getFkItemAuxId_n() });
        moFieldFkYearId_n.setFieldValue(moRecordEntry.getFkYearId_n());

        moFieldFkCheckId_n.setFieldValue(new int[] { moRecordEntry.getFkCheckWalletId_n(), moRecordEntry.getFkCheckId_n() });
        if (jcbFkCheckId_n.getSelectedIndex() <= 0 && moRecordEntry.getAuxCheckNumber() != 0) {
            SFormUtilities.locateComboBoxItemByComplement(jcbFkCheckId_n, moRecordEntry.getAuxCheckNumber());
        }

        if (moRecordEntry.getFkDpsYearId_n() != 0 && moRecordEntry.getFkDpsDocId_n() != 0) {
            moEntryDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { moRecordEntry.getFkDpsYearId_n(), moRecordEntry.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_VERBOSE);
        }

        if (moRecordEntry.getFkDpsAdjustmentYearId_n() != 0 && moRecordEntry.getFkDpsAdjustmentDocId_n() != 0) {
            moEntryDpsAdj = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { moRecordEntry.getFkDpsAdjustmentYearId_n(), moRecordEntry.getFkDpsAdjustmentDocId_n() }, SLibConstants.EXEC_MODE_VERBOSE);
        }

        jckIsDeleted.setEnabled(!moRecordEntry.getIsSystem());

        renderEntitySettings();

        renderDps();
        renderDpsAdj();

        itemStateFkEntityId_n();
        itemStateFkItemId_n();
        itemStateIsCheckApplying();

        if (moRecordEntry.getDbmsDataCfd().size() > 0) {
            for (SDataCfd cfd : moRecordEntry.getDbmsDataCfd()) {
                maCfdRecordRows.add(new SDataCfdRecordRow(maCfdRecordRows.size() + 1, cfd.getPkCfdId(), cfd.getDocXmlName(), ""));
            }
            updateFilesXmlInfo();
        }

        mbResetingForm = false;
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        int[] keySystemAccountType = getSystemAccountTypeKey();
        int[] keySystemMoveType = getSystemMoveTypeKey();
        int[] keySystemMoveTypeXXX = getSystemMoveTypeKeyXXX();

        if (moRecordEntry == null) {
            moRecordEntry = new SDataRecordEntry();
            moRecordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
            moRecordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
            moRecordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
            moRecordEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

            moRecordEntry.setDbmsAccountingMoveSubclass(SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL));
        }
        else {
            moRecordEntry.setIsRegistryEdited(true);
            moRecordEntry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moRecordEntry.setConcept(moFieldConcept.getString());
        moRecordEntry.setDebit(moFieldDebit.getDouble());
        moRecordEntry.setCredit(moFieldCredit.getDouble());
        moRecordEntry.setExchangeRate(moFieldExchangeRate.getDouble());
        moRecordEntry.setExchangeRateSystem(moFieldExchangeRateSystem.getDouble());
        moRecordEntry.setDebitCy(moFieldDebitCy.getDouble());
        moRecordEntry.setCreditCy(moFieldCreditCy.getDouble());
        moRecordEntry.setFkCurrencyId(moFieldFkCurrencyId.getKeyAsIntArray()[0]);
        moRecordEntry.setFkAccountIdXXX(moPanelFkAccountId.getFieldAccount().getString());
        moRecordEntry.setFkCostCenterIdXXX_n(moPanelFkCostCenterId_n.isEmptyAccountId() ? "" : moPanelFkCostCenterId_n.getFieldAccount().getString());
        moRecordEntry.setIsExchangeDifference(jckIsExchangeDifference.isSelected());
        moRecordEntry.setIsSystem(jckIsSystem.isSelected());
        moRecordEntry.setIsDeleted(jckIsDeleted.isSelected());

        moRecordEntry.setFkSystemMoveClassId(keySystemMoveType[0]);
        moRecordEntry.setFkSystemMoveTypeId(keySystemMoveType[1]);
        moRecordEntry.setFkSystemAccountClassId(keySystemAccountType[0]);
        moRecordEntry.setFkSystemAccountTypeId(keySystemAccountType[1]);
        moRecordEntry.setFkSystemMoveCategoryIdXXX(keySystemMoveTypeXXX[0]);
        moRecordEntry.setFkSystemMoveTypeIdXXX(keySystemMoveTypeXXX[1]);

        moRecordEntry.setDbmsAccount(moPanelFkAccountId.getCurrentInputAccount() == null ? "" : moPanelFkAccountId.getCurrentInputAccount().getAccount());
        moRecordEntry.setDbmsAccountComplement("");
        moRecordEntry.setDbmsCostCenter_n(moPanelFkCostCenterId_n.getCurrentInputCostCenter() == null ? "" : moPanelFkCostCenterId_n.getCurrentInputCostCenter().getCostCenter());
        moRecordEntry.setDbmsCurrencyKey((String) ((SFormComponentItem) jcbFkCurrencyId.getSelectedItem()).getComplement());

        if (jcbFkBizPartnerId_nr.isEnabled() && jcbFkBizPartnerId_nr.getSelectedIndex() > 0) {
            moRecordEntry.setFkBizPartnerId_nr(moFieldFkBizPartnerId_nr.getKeyAsIntArray()[0]);
            moRecordEntry.setFkBizPartnerBranchId_n(((SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, moFieldFkBizPartnerId_nr.getKey(), SLibConstants.EXEC_MODE_VERBOSE)).getDbmsHqBranch().getPkBizPartnerBranchId());
            moRecordEntry.setDbmsBizPartnerCode(moFieldFkBizPartnerId_nr.getKey().toString());
            moRecordEntry.setDbmsBizPartner(moFieldFkBizPartnerId_nr.getString());
        }
        else {
            moRecordEntry.setFkBizPartnerId_nr(0);
            moRecordEntry.setFkBizPartnerBranchId_n(0);
            moRecordEntry.setDbmsBizPartnerCode("");
            moRecordEntry.setDbmsBizPartner("");
        }
        
        if (jcbOccasionalFiscalId.isEnabled() && !moFieldOccasionalFiscalId.getString().isEmpty()) {
            moRecordEntry.setOccasionalFiscalId(moFieldOccasionalFiscalId.getString());
        }
        else {
            moRecordEntry.setOccasionalFiscalId("");
        }

        if (jtfReference.isEnabled()) {
            moRecordEntry.setReference(moFieldReference.getString());
            moRecordEntry.setIsReferenceTax(moFieldIsReferenceTax.getBoolean());
        }
        else {
            moRecordEntry.setReference("");
            moRecordEntry.setIsReferenceTax(false);
        }

        if ((jcbFkTaxId_n.isEnabled() || mbIsTaxCfg) && jcbFkTaxId_n.getSelectedIndex() > 0) {
            moRecordEntry.setFkTaxBasicId_n(moFieldFkTaxId_n.getKeyAsIntArray()[0]);
            moRecordEntry.setFkTaxId_n(moFieldFkTaxId_n.getKeyAsIntArray()[1]);
            moRecordEntry.setDbmsTax(moFieldFkTaxId_n.getString());
        }
        else {
            moRecordEntry.setFkTaxBasicId_n(0);
            moRecordEntry.setFkTaxId_n(0);
            moRecordEntry.setDbmsTax("");
        }

        if (jcbFkEntityId_n.isEnabled() && jcbFkEntityId_n.getSelectedIndex() > 0) {
            moRecordEntry.setFkCompanyBranchId_n(moFieldFkEntityId_n.getKeyAsIntArray()[0]);
            moRecordEntry.setFkEntityId_n(moFieldFkEntityId_n.getKeyAsIntArray()[1]);
            moRecordEntry.setDbmsEntityCode(moFieldFkEntityId_n.getKey().toString());
            moRecordEntry.setDbmsEntity(moFieldFkEntityId_n.getString());
        }
        else {
            moRecordEntry.setFkCompanyBranchId_n(0);
            moRecordEntry.setFkEntityId_n(0);
            moRecordEntry.setDbmsEntityCode("");
            moRecordEntry.setDbmsEntity("");
        }

        if (jcbFkItemId_n.isEnabled() && jcbFkItemId_n.getSelectedIndex() > 0) {
            moRecordEntry.setFkItemId_n(moItem.getPkItemId());
            moRecordEntry.setDbmsItemCode(moItem.getCode());
            moRecordEntry.setDbmsItem(moItem.getItem());
            
            if (jcbFkItemAuxId_n.isEnabled() && jcbFkItemAuxId_n.getSelectedIndex() > 0) {
                moRecordEntry.setUnits(0);
                moRecordEntry.setFkUnitId_n(SModSysConsts.ITMU_UNIT_NA);
                moRecordEntry.setFkItemAuxId_n(moFieldFkItemAuxId_n.getKeyAsIntArray()[0]);
                moRecordEntry.setDbmsItemAuxCode(moItemAux.getCode());
                moRecordEntry.setDbmsItemAux(moItemAux.getItem());
            }
            else {
                moRecordEntry.setUnits(moFieldUnits.getDouble());
                moRecordEntry.setFkUnitId_n(moItem.getFkUnitId());
                moRecordEntry.setFkItemAuxId_n(SLibConstants.UNDEFINED);
                moRecordEntry.setDbmsItemAuxCode("");
                moRecordEntry.setDbmsItemAux("");
            }

            moRecordEntry.setDbmsAccountComplement(moItem.getItem());
        }
        else {
            moRecordEntry.setUnits(0);
            moRecordEntry.setFkItemId_n(0);
            moRecordEntry.setFkUnitId_n(0);
            moRecordEntry.setFkItemAuxId_n(0);
            moRecordEntry.setDbmsItemCode("");
            moRecordEntry.setDbmsItem("");
            moRecordEntry.setDbmsItemAuxCode("");
            moRecordEntry.setDbmsItemAux("");
            
            moRecordEntry.setDbmsAccountComplement("");
        }

        if (jtfFkYearId_n.isEnabled()) {
            moRecordEntry.setFkYearId_n(moFieldFkYearId_n.getInteger());
        }
        else {
            moRecordEntry.setFkYearId_n(0);
        }

        if (!jckIsCheckApplying.isSelected()) {
            moRecordEntry.setFkCheckWalletId_n(0);
            moRecordEntry.setFkCheckId_n(0);
            moRecordEntry.setAuxCheckNumber(0);
        }
        else {
            moRecordEntry.setFkCheckWalletId_n(moFieldFkCheckId_n.getKeyAsIntArray()[0]);
            moRecordEntry.setFkCheckId_n(moFieldFkCheckId_n.getKeyAsIntArray()[1]);
            moRecordEntry.setAuxCheckNumber((Integer) ((SFormComponentItem) jcbFkCheckId_n.getSelectedItem()).getComplement());
        }

        if (jbFkDps.isEnabled() && moEntryDps != null) {
            moRecordEntry.setFkDpsYearId_n(moEntryDps.getPkYearId());
            moRecordEntry.setFkDpsDocId_n(moEntryDps.getPkDocId());
            moRecordEntry.setDbmsDps(moEntryDps.getDpsNumber());
        }
        else {
            moRecordEntry.setFkDpsYearId_n(0);
            moRecordEntry.setFkDpsDocId_n(0);
            moRecordEntry.setDbmsDps("");
        }

        if (jbFkDpsAdj.isEnabled() && moEntryDpsAdj != null) {
            moRecordEntry.setFkDpsAdjustmentYearId_n(moEntryDpsAdj.getPkYearId());
            moRecordEntry.setFkDpsAdjustmentDocId_n(moEntryDpsAdj.getPkDocId());
        }
        else {
            moRecordEntry.setFkDpsAdjustmentYearId_n(0);
            moRecordEntry.setFkDpsAdjustmentDocId_n(0);
        }
        
        
        // obtain XML to delete:
        
        for (SDataCfd cfdAux : moRecordEntry.getDbmsDataCfd()) {
            if (maCfdRecordRows.isEmpty()) {
                moRecordEntry.getAuxDataCfdToDel().add(cfdAux);
            }
            else {
                boolean found = false;
                for (SDataCfdRecordRow row : maCfdRecordRows) {
                    if (SLibUtilities.compareKeys(new int[] { row.getCfdId() }, new int[] { cfdAux.getPkCfdId() })) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    moRecordEntry.getAuxDataCfdToDel().add(cfdAux);
                }
            }
        }
        
        // process added XML files of CFDI:
        
        ArrayList<SDataCfd> cfds = new ArrayList<>();
        
        for (SDataCfdRecordRow row : maCfdRecordRows) {
            SDataCfd cfd = null;
            
            for (SDataCfd cfdAux : moRecordEntry.getDbmsDataCfd()) {
                if (SLibUtilities.compareKeys(new int[] { row.getCfdId() }, new int[] { cfdAux.getPkCfdId() })) {
                    cfd = cfdAux;
                    break;
                }
            }
            
            if (!row.getNameXml().isEmpty()) {
                try {
                    if (cfd == null) {
                        String xml = SXmlUtils.readXml(row.getPathXml());
                        
                        cfd = new SDataCfd();
                        cfd.setCertNumber("");
                        cfd.setStringSigned("");
                        cfd.setSignature("");
                        cfd.setDocXml(xml);
                        cfd.setDocXmlName(row.getNameXml());
                        cfd.setIsConsistent(true);
                        cfd.setFkCfdTypeId(SDataConstantsSys.TRNS_TP_CFD_INV);
                        cfd.setFkXmlTypeId(SDataConstantsSys.TRNS_TP_XML_NA);
                        cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
                        cfd.setFkXmlDeliveryTypeId(SModSysConsts.TRNS_TP_XML_DVY_NA);
                        cfd.setFkXmlDeliveryStatusId(SModSysConsts.TRNS_ST_XML_DVY_PENDING);
                        cfd.setFkUserProcessingId(miClient.getSession().getUser().getPkUserId());
                        cfd.setFkUserDeliveryId(miClient.getSession().getUser().getPkUserId());
                    }
                }
                catch (FileNotFoundException e) {
                    SLibUtilities.renderException(this, e);
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
            else {
                try {
                    if (row.getNameXml().length() == 0 && cfd != null) {
                        cfd.setDocXml("");
                        cfd.setDocXmlName("");
                        cfd.setIsConsistent(true);
                        cfd.setFkXmlStatusId(SDataConstantsSys.TRNS_ST_DPS_NEW);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
            
            if (cfd != null) {
                cfds.add(cfd);
            }
        }
        
        moRecordEntry.getDbmsDataCfd().clear();
        if (cfds.size() > 0) {
            moRecordEntry.getDbmsDataCfd().addAll(cfds);
        }
        
        moRecordEntry.setDbmsXmlFilesNumber(Integer.parseInt(jtfXmlFilesNumber.getText()));

        return moRecordEntry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SLibConstants.VALUE_TEXT:
                moFieldConcept.setFieldValue((String) value);
                break;

            case SDataConstants.FIN_REC:
                moRecord = (SDataRecord) value;

                if (moRecordEntry == null) {
                    if (moRecord.getDbmsDataAccountCash() != null) {
                        moFieldFkCurrencyId.setFieldValue(new int[] { moRecord.getDbmsDataAccountCash().getFkCurrencyId() });
                        renderCurrencySettings();
                    }
                }
                break;

            case SDataConstants.FIN_CHECK:
                SFormComponentItem item = null;
                Vector<SDataCheck> checks = (Vector<SDataCheck>) value;

                jcbFkCheckId_n.removeAllItems();
                jcbFkCheckId_n.addItem(new SFormComponentItem(new int[] { 0, 0 }, "(Seleccionar cheque)"));

                for (SDataCheck check : checks) {
                    item = new SFormComponentItem(check.getPrimaryKey(), "#" + check.getNumber() + "; " +
                            miClient.getSessionXXX().getFormatters().getDecimalsCurrencyFormat().format(check.getValue()) + "; " +
                            check.getBeneficiary());
                    item.setComplement(check.getNumber());

                    jcbFkCheckId_n.addItem(item);
                }

                jcbFkCheckId_n.setSelectedIndex(0);
                break;

            default:
                break;
        }
    }

    @Override
    public java.lang.Object getValue(int type) {
        Object value = null;

        switch (type) {
            case SLibConstants.VALUE_TEXT:
                value = moFieldConcept.getString();
                break;
            default:
        }

        return value;
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbFkBizPartnerId_nr) {
                actionjbFkBizPartnerId_nr();
            }
            else if (button == jbReference) {
                actionReference();
            }
            else if (button == jbFkTaxId_n) {
                actionFkTaxId_n();
            }
            else if (button == jbFkEntityId_n) {
                actionFkEntityId_n();
            }
            else if (button == jbFkItemId_n) {
                actionFkItemId_n();
            }
            else if (button == jbFkItemAuxId_n) {
                actionFkItemAuxId_n();
            }
            else if (button == jbFkDps) {
                actionFkDps();
            }
            else if (button == jbFileXml) {
                actionLoadFileXml();
            }
            else if (button == jbFkDpsRemove) {
                actionFkDpsRemove();
            }
            else if (button == jbFileXmlRemove) {
                actionFileXmlDelete();
            }
            else if (button == jbFileXmlAdd) {
                actionFileXmlAdd();
            }
            else if (button == jbGetXml) {
                actionGetXml();
            }
            else if (button == jbFkDpsAdj) {
                actionFkDpsAdj();
            }
            else if (button == jbFkDpsAdjRemove) {
                actionFkDpsAdjRemove();
            }
            else if (button == jbFkCurrencyId) {
                actionFkCurrencyId();
            }
            else if (button == jbExchangeRateSystem) {
                actionExchangeRateSystem();
            }
            else if (button == jbExchangeRate) {
                actionExchangeRate();
            }
            else if (button == jbExchangeRateAccountCashSet) {
                actionExchangeRateAccountCashSet();
            }
            else if (button == jbExchangeRateAccountCashView) {
                actionExchangeRateAccountCashView();
            }
            else if (button == jbDebitCy) {
                actionDebitCy();
            }
            else if (button == jbCreditCy) {
                actionCreditCy();
            }
            else if (button == jbDebit) {
                actionDebit();
            }
            else if (button == jbCredit) {
                actionCredit();
            }
        }
    }

    @Override
    public void focusGained(java.awt.event.FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JFormattedTextField) {
            JFormattedTextField formattedTextField = (JFormattedTextField) e.getSource();

            if (formattedTextField == moPanelFkAccountId.getFieldAccount().getComponent()) {
                actionFkAccountIdFocusGained();
            }
        }
        else if (e.getSource() instanceof javax.swing.JComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();

            if (comboBox == jcbFkEntityId_n) {
                actionFkEntityId_nFocusGained();
            }
            else if (comboBox == jcbFkCurrencyId) {
                actionFkCurrencyIdFocusGained();
            }
        }
    }

    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        if (e.getSource() instanceof javax.swing.JFormattedTextField) {
            JFormattedTextField formattedTextField = (JFormattedTextField) e.getSource();

            if (formattedTextField == moPanelFkAccountId.getFieldAccount().getComponent()) {
                actionFkAccountIdFocusLost();
            }
        }
        else if (e.getSource() instanceof javax.swing.JComboBox) {
            JComboBox comboBox = (JComboBox) e.getSource();

            if (comboBox == jcbFkEntityId_n) {
                actionFkEntityId_nFocusLost();
            }
            else if (comboBox == jcbFkCurrencyId) {
                actionFkCurrencyIdFocusLost();
            }
        }
        else if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfDebitCy) {
                actionDebitCyFocusLost();
            }
            else if (textField == jtfCreditCy) {
                actionCreditCyFocusLost();
            }
            else if (textField == jtfDebit) {
                actionDebitFocusLost();
            }
            else if (textField == jtfCredit) {
                actionCreditFocusLost();
            }
            else if (textField == jtfExchangeRate) {
                actionExchangeRateFocusLost();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!mbResetingForm) {
            if (e.getSource() instanceof JComboBox) {
                JComboBox comboBox = (JComboBox) e.getSource();

                if (comboBox == jcbFkEntityId_n) {
                    itemStateFkEntityId_n();
                }
                else if (comboBox == jcbFkItemId_n) {
                    itemStateFkItemId_n();
                }
                else if (comboBox == jcbFkItemAuxId_n) {
                    itemStateFkItemAuxId_n();
                }
                else if (comboBox == jcbFkCurrencyId) {
                    renderCurrencySettings();
                }
            }
            else if (e.getSource() instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) e.getSource();

                if (checkBox == jckIsCheckApplying) {
                    itemStateIsCheckApplying();
                }
                else if (checkBox == jckIsExchangeDifference) {
                    itemStateIsExchangeDifference();
                }
            }
        }
    }
}
