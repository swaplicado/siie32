/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 *
 * Created on 10/11/2009
 */

package erp.mmkt.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mmkt.data.SDataPriceListBizPartnerLink;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Néstor Ávalos, Uriel Castañeda
 */
public class SFormPriceListBizPartnerLink extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private int mnReferenceDataType;
    private int mnParamBizPartnerLinkType;
    private int mnParamBizPartnerCategory;

    private erp.mmkt.data.SDataPriceListBizPartnerLink moPriceListBizPartnerLink;
    private erp.lib.form.SFormField moFieldPkReferenceId;
    private erp.lib.form.SFormField moFieldPkBizPartnerBranchId;
    private erp.lib.form.SFormField moFieldPkDateStartId;
    private erp.lib.form.SFormField moFieldPercentage;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldFkPriceListId;
    private erp.lib.form.SFormField moFieldFkDiscountApplicationTypeId;

    /** Creates new form SFormElement */
    public SFormPriceListBizPartnerLink(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;
        mnFormType = SDataConstants.MKT_PLIST_BP_LINK;

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
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jlPkReferenceId = new javax.swing.JLabel();
        jcbPkReferenceId = new javax.swing.JComboBox<SFormComponentItem>();
        jbPkReferenceId = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jlBizPartnerBranchId = new javax.swing.JLabel();
        jcbPkBizPartnerBranchId = new javax.swing.JComboBox<SFormComponentItem>();
        jbPkBizPartnerBranchId = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jlPkDateStartId = new javax.swing.JLabel();
        jtfPkDateStartId = new javax.swing.JFormattedTextField();
        jbPkDateStartId = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jlFkPriceListId = new javax.swing.JLabel();
        jcbFkPriceListId = new javax.swing.JComboBox<SFormComponentItem>();
        jbFkPriceListId = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jlFkDiscountApplicationTypeId = new javax.swing.JLabel();
        jcbFkDiscountApplicationTypeId = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel15 = new javax.swing.JPanel();
        jlPercentage = new javax.swing.JLabel();
        jtfPercentage = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lista de precios por cliente");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.GridLayout(7, 1, 5, 5));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkReferenceId.setForeground(java.awt.Color.blue);
        jlPkReferenceId.setText("<Referencia: *>");
        jlPkReferenceId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel10.add(jlPkReferenceId);

        jcbPkReferenceId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel10.add(jcbPkReferenceId);

        jbPkReferenceId.setText("...");
        jbPkReferenceId.setToolTipText("Seleccionar <referencia>");
        jbPkReferenceId.setFocusable(false);
        jbPkReferenceId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbPkReferenceId);

        jPanel9.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerBranchId.setForeground(java.awt.Color.blue);
        jlBizPartnerBranchId.setText("Sucursal: *");
        jlBizPartnerBranchId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jlBizPartnerBranchId);

        jcbPkBizPartnerBranchId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel11.add(jcbPkBizPartnerBranchId);

        jbPkBizPartnerBranchId.setText("...");
        jbPkBizPartnerBranchId.setToolTipText("Seleccionar cliente");
        jbPkBizPartnerBranchId.setFocusable(false);
        jbPkBizPartnerBranchId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbPkBizPartnerBranchId);

        jPanel9.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkDateStartId.setForeground(java.awt.Color.blue);
        jlPkDateStartId.setText("Fecha inicial vigencia: *");
        jlPkDateStartId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel12.add(jlPkDateStartId);

        jtfPkDateStartId.setText("01/01/2001");
        jtfPkDateStartId.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jtfPkDateStartId);

        jbPkDateStartId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPkDateStartId.setToolTipText("Seleccionar fecha inicial vigencia");
        jbPkDateStartId.setFocusable(false);
        jbPkDateStartId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel12.add(jbPkDateStartId);

        jPanel9.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkPriceListId.setText("Lista de precios: *");
        jlFkPriceListId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel13.add(jlFkPriceListId);

        jcbFkPriceListId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel13.add(jcbFkPriceListId);

        jbFkPriceListId.setText("...");
        jbFkPriceListId.setToolTipText("Seleccionar lista de precios");
        jbFkPriceListId.setFocusable(false);
        jbFkPriceListId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel13.add(jbFkPriceListId);

        jPanel9.add(jPanel13);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFkDiscountApplicationTypeId.setText("Tipo de descuento: *");
        jlFkDiscountApplicationTypeId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel14.add(jlFkDiscountApplicationTypeId);

        jcbFkDiscountApplicationTypeId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel14.add(jcbFkDiscountApplicationTypeId);

        jPanel9.add(jPanel14);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPercentage.setText("Descuento: *");
        jlPercentage.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel15.add(jlPercentage);

        jtfPercentage.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfPercentage.setText("0.00%");
        jtfPercentage.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel15.add(jtfPercentage);

        jPanel9.add(jPanel15);

        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel30.add(jckIsDeleted);

        jPanel9.add(jPanel30);

        jPanel3.add(jPanel9, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOK.setText("Aceptar");
        jbOK.setToolTipText("[Ctrl + Enter]");
        jbOK.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbOK);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(496, 336));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        mnParamBizPartnerLinkType = SLibConstants.UNDEFINED;
        mnParamBizPartnerCategory = SLibConstants.UNDEFINED;

        moFieldPkReferenceId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkReferenceId, jlPkReferenceId);
        moFieldPkBizPartnerBranchId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkBizPartnerBranchId, jlBizPartnerBranchId);
        moFieldPkDateStartId = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jtfPkDateStartId, jlPkDateStartId);
        moFieldPercentage = new SFormField(miClient, SLibConstants.DATA_TYPE_FLOAT, true, jtfPercentage, jlPercentage);
        moFieldPercentage.setDecimalFormat(miClient.getSessionXXX().getFormatters().getDecimalsPercentageFormat());
        moFieldPercentage.setIsPercent(true);
        moFieldPercentage.setFloatMin(0);
        moFieldPercentage.setMinInclusive(true);
        moFieldPercentage.setFloatMax(1);
        moFieldPercentage.setMaxInclusive(true);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, false, jckIsDeleted);
        moFieldFkPriceListId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkPriceListId, jlFkPriceListId);
        moFieldFkDiscountApplicationTypeId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbFkDiscountApplicationTypeId, jlFkDiscountApplicationTypeId);

        mvFields.add(moFieldPkReferenceId);
        mvFields.add(moFieldPkBizPartnerBranchId);
        mvFields.add(moFieldPkDateStartId);
        mvFields.add(moFieldFkPriceListId);
        mvFields.add(moFieldFkDiscountApplicationTypeId);
        mvFields.add(moFieldPercentage);
        mvFields.add(moFieldIsDeleted);

        jbOK.addActionListener(this);
        jbCancel.addActionListener(this);
        jbPkReferenceId.addActionListener(this);
        jbPkBizPartnerBranchId.addActionListener(this);
        jbPkDateStartId.addActionListener(this);
        jbFkPriceListId.addActionListener(this);
        
        jcbPkReferenceId.addItemListener(this);
        jcbFkDiscountApplicationTypeId.addItemListener(this);

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
            if (jcbPkReferenceId.isEnabled()) jcbPkReferenceId.requestFocus(); else jcbFkPriceListId.requestFocus();
        }
    }

    private void computeBizPartnerLinkType() {
        String label = "";
        
        switch(mnParamBizPartnerLinkType) {
            case SModSysConsts.BPSS_LINK_CUS_MKT_TP:
                mnReferenceDataType = SDataConstants.MKTU_TP_CUS;
                label = SUtilConsts.TXT_TP + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_CUS, SUtilConsts.NUM_SNG).toLowerCase();
                break;
            case SModSysConsts.BPSS_LINK_BP_TP:
                mnReferenceDataType = SDataConstants.BPSU_TP_BP;
                label = SUtilConsts.TXT_TP + " " + SBpsUtils.getBizPartnerCategoryName(SLibConsts.UNDEFINED, SUtilConsts.NUM_SNG).toLowerCase() + " " + SBpsUtils.getBizPartnerCategoryName(mnParamBizPartnerCategory, SUtilConsts.NUM_PLR).toLowerCase();
                break;
            case SModSysConsts.BPSS_LINK_BP:
            case SModSysConsts.BPSS_LINK_BPB:
                mnReferenceDataType = mnParamBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS ? SDataConstants.BPSX_BP_CUS : SDataConstants.BPSX_BP_SUP;
                label = (mnParamBizPartnerLinkType == SModSysConsts.BPSS_LINK_BP ? "" : SUtilConsts.TXT_BRANCH + " ") + SBpsUtils.getBizPartnerCategoryName(mnParamBizPartnerCategory, SUtilConsts.NUM_SNG);
                break;
            default:
        }
        
        jlPkReferenceId.setText(label + ":*");
        jbPkReferenceId.setToolTipText(SGuiConsts.TXT_BTN_SELECT + " " + label.toLowerCase());
        setTitle("Lista de precios por " + label.toLowerCase());
        
        formRefreshCatalogues();
    }

    private void itemStateChangedPkReferenceId() {
        if (jcbPkReferenceId.getSelectedIndex() <= 0 || mnParamBizPartnerLinkType != SModSysConsts.BPSS_LINK_BPB) {
            jcbPkBizPartnerBranchId.setEnabled(false);
            jbPkBizPartnerBranchId.setEnabled(false);
            
            jcbPkBizPartnerBranchId.removeAllItems();
        }
        else {
            jcbPkBizPartnerBranchId.setEnabled(true);
            jbPkBizPartnerBranchId.setEnabled(true);
            
            SFormUtilities.populateComboBox(miClient, jcbPkBizPartnerBranchId, SDataConstants.BPSU_BPB, moFieldPkReferenceId.getKey());
        }
    }
    
    private void itemStateChangedFkDiscountApplicationTypeId() {
        if (jcbFkDiscountApplicationTypeId.getSelectedIndex() <= 0 || moFieldFkDiscountApplicationTypeId.getKeyAsIntArray()[0] == SDataConstantsSys.MKTS_TP_DISC_APP_NA) {
            jtfPercentage.setEnabled(false);
            jtfPercentage.setFocusable(false);
            
            moFieldPercentage.resetField();
        }
        else {
            jtfPercentage.setEnabled(true);
            jtfPercentage.setFocusable(true);
        }
    }

    private void actionPkReferenceId() {
        miClient.pickOption(
            mnParamBizPartnerLinkType == SModSysConsts.BPSS_LINK_CUS_MKT_TP ? SDataConstants.MKTU_TP_CUS :
            mnParamBizPartnerLinkType == SModSysConsts.BPSS_LINK_BP_TP ? SDataConstants.BPSU_TP_BP :
            mnParamBizPartnerLinkType == SModSysConsts.BPSS_LINK_BP ? SDataConstants.BPSX_BP_CUS :
            mnParamBizPartnerLinkType == SModSysConsts.BPSS_LINK_BPB ? SDataConstants.BPSX_BP_CUS : SLibConstants.UNDEFINED, moFieldPkReferenceId, null);
    }
    
    private void actionPkBizPartnerBranchId() {
        miClient.pickOption(SDataConstants.BPSU_BPB, moFieldPkBizPartnerBranchId,  moFieldPkReferenceId.getKey());
    }

    private void actionSetPkDateStartId() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldPkDateStartId.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldPkDateStartId.setDate(miClient.getGuiDatePickerXXX().getGuiDate());
            jtfPkDateStartId.requestFocus();
        }
    }

    private void actionFkPriceListId() {
        miClient.pickOption(SDataConstants.MKT_PLIST, moFieldFkPriceListId, mnParamBizPartnerCategory);
    }

    private void actionOk() {
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

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbFkPriceListId;
    private javax.swing.JButton jbOK;
    private javax.swing.JButton jbPkBizPartnerBranchId;
    private javax.swing.JButton jbPkDateStartId;
    private javax.swing.JButton jbPkReferenceId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkDiscountApplicationTypeId;
    private javax.swing.JComboBox<SFormComponentItem> jcbFkPriceListId;
    private javax.swing.JComboBox<SFormComponentItem> jcbPkBizPartnerBranchId;
    private javax.swing.JComboBox<SFormComponentItem> jcbPkReferenceId;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JLabel jlBizPartnerBranchId;
    private javax.swing.JLabel jlFkDiscountApplicationTypeId;
    private javax.swing.JLabel jlFkPriceListId;
    private javax.swing.JLabel jlPercentage;
    private javax.swing.JLabel jlPkDateStartId;
    private javax.swing.JLabel jlPkReferenceId;
    private javax.swing.JTextField jtfPercentage;
    private javax.swing.JFormattedTextField jtfPkDateStartId;
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

        mnReferenceDataType = SLibConsts.UNDEFINED;
        moPriceListBizPartnerLink = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        moFieldPkDateStartId.setFieldValue(miClient.getSessionXXX().getWorkingDate());

        jcbPkReferenceId.setEnabled(true);
        jbPkReferenceId.setEnabled(true);
        jtfPkDateStartId.setEditable(true);
        jtfPkDateStartId.setFocusable(true);
        jbPkDateStartId.setEnabled(true);
        jckIsDeleted.setEnabled(false);
    }

    @Override
    public void formRefreshCatalogues() {
        if (mnReferenceDataType != SLibConsts.UNDEFINED) {
            switch(mnParamBizPartnerLinkType) {
                case SModSysConsts.BPSS_LINK_CUS_MKT_TP:
                    SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, mnReferenceDataType);
                    break;
                case SModSysConsts.BPSS_LINK_BP_TP:
                    SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, mnReferenceDataType,mnParamBizPartnerCategory);
                    break;
                case SModSysConsts.BPSS_LINK_BP:
                case SModSysConsts.BPSS_LINK_BPB:    
                    SFormUtilities.populateComboBox(miClient, jcbPkReferenceId, mnReferenceDataType);
                default:
            }
        }
               
        SFormUtilities.populateComboBox(miClient, jcbFkPriceListId, SDataConstants.MKT_PLIST, mnParamBizPartnerCategory);
        SFormUtilities.populateComboBox(miClient, jcbFkDiscountApplicationTypeId, SDataConstants.MKTS_TP_DISC_APP);
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
        moPriceListBizPartnerLink = (SDataPriceListBizPartnerLink) registry;

        switch(mnParamBizPartnerLinkType) {
            case SModSysConsts.BPSS_LINK_CUS_MKT_TP:
                moFieldPkReferenceId.setKey(new int[] { moPriceListBizPartnerLink.getPkReference1Id() });
                break;
            case SModSysConsts.BPSS_LINK_BP_TP:
                moFieldPkReferenceId.setKey(new int[] { moPriceListBizPartnerLink.getPkReference1Id(), moPriceListBizPartnerLink.getPkReference2Id() });
                break;
            case SModSysConsts.BPSS_LINK_BP:
                moFieldPkReferenceId.setKey(new int[] { moPriceListBizPartnerLink.getPkReference1Id() });
                break;
            case SModSysConsts.BPSS_LINK_BPB:
                moFieldPkReferenceId.setKey(new int[] { moPriceListBizPartnerLink.getPkReference1Id() });
                moFieldPkBizPartnerBranchId.setKey(new int[] { moPriceListBizPartnerLink.getPkReference2Id() });
                break;
            default:
        }
        
        moFieldPkDateStartId.setDate(moPriceListBizPartnerLink.getPkDateStartId());
        moFieldFkPriceListId.setKey(new int[] { moPriceListBizPartnerLink.getFkPriceListId() });
        moFieldFkDiscountApplicationTypeId.setKey(new int[] { moPriceListBizPartnerLink.getFkDiscountApplicationTypeId() });
        moFieldPercentage.setDouble(moPriceListBizPartnerLink.getDiscountPercentage());
        moFieldIsDeleted.setBoolean(moPriceListBizPartnerLink.getIsDeleted());

        jcbPkReferenceId.setEnabled(false);
        jbPkReferenceId.setEnabled(false);
        jcbPkBizPartnerBranchId.setEnabled(false);
        jbPkBizPartnerBranchId.setEnabled(false);
        jtfPkDateStartId.setEditable(false);
        jtfPkDateStartId.setFocusable(false);
        jbPkDateStartId.setEnabled(false);
        jckIsDeleted.setEnabled(true);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moPriceListBizPartnerLink == null) {
            moPriceListBizPartnerLink = new SDataPriceListBizPartnerLink();
            
            moPriceListBizPartnerLink.setPkLinkId(mnParamBizPartnerLinkType);
            moPriceListBizPartnerLink.setPkReference1Id(moFieldPkReferenceId.getKeyAsIntArray()[0]);

            switch(mnParamBizPartnerLinkType) {
                case SModSysConsts.BPSS_LINK_CUS_MKT_TP:
                    break;
                case SModSysConsts.BPSS_LINK_BP_TP:
                    moPriceListBizPartnerLink.setPkReference2Id(moFieldPkReferenceId.getKeyAsIntArray()[1]);
                    break;
                case SModSysConsts.BPSS_LINK_BP:
                    break;
                case SModSysConsts.BPSS_LINK_BPB:
                    moPriceListBizPartnerLink.setPkReference2Id(moFieldPkBizPartnerBranchId.getKeyAsIntArray()[0]);
                    break;
                default:
            }
            
            moPriceListBizPartnerLink.setPkDateStartId(moFieldPkDateStartId.getDate());
            
            moPriceListBizPartnerLink.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moPriceListBizPartnerLink.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moPriceListBizPartnerLink.setIsDeleted(moFieldIsDeleted.getBoolean());
        moPriceListBizPartnerLink.setFkPriceListId(moFieldFkPriceListId.getKeyAsIntArray()[0]);
        moPriceListBizPartnerLink.setFkDiscountApplicationTypeId(moFieldFkDiscountApplicationTypeId.getKeyAsIntArray()[0]);
        moPriceListBizPartnerLink.setDiscountPercentage(moFieldPercentage.getDouble());

        return moPriceListBizPartnerLink;
    }

    @Override
    public void setValue(int type, Object value) {
         switch (type) {
            case SDataConstants.BPSS_LINK:
                mnParamBizPartnerLinkType = (Integer) value;
                computeBizPartnerLinkType();
                break;
            case SModConsts.BPSS_CT_BP:
                mnParamBizPartnerCategory = (Integer) value;
                break;
            default:
        }
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

            if (button == jbOK) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbPkReferenceId) {
                actionPkReferenceId();
            }
            else if (button == jbPkBizPartnerBranchId) {
                actionPkBizPartnerBranchId();
            }
            else if (button == jbPkDateStartId) {
                actionSetPkDateStartId();
            }
            else if (button == jbFkPriceListId) {
                actionFkPriceListId();
            }
        }
    }
    
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox && e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox comboBox = (JComboBox) e.getSource();
        
            if (comboBox == jcbPkReferenceId) {
                itemStateChangedPkReferenceId();
            }
            else if (comboBox == jcbFkDiscountApplicationTypeId) {
                itemStateChangedFkDiscountApplicationTypeId();
            }
        } 
    }
}
