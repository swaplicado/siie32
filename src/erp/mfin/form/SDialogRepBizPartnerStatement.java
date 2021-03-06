/*
 * SDialogRepBizPartnerStatement.java
 *
 * Created on 19/08/2008, 11:10 AM
 * Modified on 14/03/2016, 08:10 AM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataRepConstants;
import erp.data.SDataUtilities;
import erp.gui.SGuiUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsConsts;
import erp.mod.bps.db.SBpsUtils;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JRadioButton;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author  Néstor Ávalos, Sergio Flores
 */
public class SDialogRepBizPartnerStatement extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private erp.client.SClientInterface miClient;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;

    private int mnBizPartnerCategory;
    private int mnBizPartnerPicker;
    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateEnd;
    private erp.lib.form.SFormField moFieldCoBranch;
    private erp.lib.form.SFormField moFieldBizPartner;
    private erp.lib.form.SFormField moFieldSalesAgent;

    /** Creates new form SDialogRepBizPartnerStatement */
    public SDialogRepBizPartnerStatement(erp.client.SClientInterface client, String title, int bizPartnerCategory) {
        super(client.getFrame(), title, false);
        miClient = client;
        mnBizPartnerCategory = bizPartnerCategory;

        initComponents();
        initComponentsCustom();

        formRefreshCatalogues();
        formReset();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgBizPartner = new javax.swing.ButtonGroup();
        jpParams = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbPickDateStart = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        jftDateEnd = new javax.swing.JFormattedTextField();
        jbPickDateEnd = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jlCoBranch = new javax.swing.JLabel();
        jcbCoBranch = new javax.swing.JComboBox<SFormComponentItem>();
        jbPickCoBranch = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jrbBizPartner = new javax.swing.JRadioButton();
        jPanel11 = new javax.swing.JPanel();
        jrbSalesAgent = new javax.swing.JRadioButton();
        jPanel98 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jcbBizPartner = new javax.swing.JComboBox<SFormComponentItem>();
        jbPickBizPartner = new javax.swing.JButton();
        jPanel99 = new javax.swing.JPanel();
        jlSalesAgent = new javax.swing.JLabel();
        jcbSalesAgent = new javax.swing.JComboBox<SFormComponentItem>();
        jbPickSalesAgent = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jckAdvancePayments = new javax.swing.JCheckBox();
        jpControls = new javax.swing.JPanel();
        jbPrint = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpParams.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jpParams.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDateStart);

        jftDateStart.setText("dd/mm/yyyy");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jftDateStart);

        jbPickDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPickDateStart.setToolTipText("Seleccionar fecha inicial");
        jbPickDateStart.setFocusable(false);
        jbPickDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbPickDateStart);

        jPanel8.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final: *");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlDateEnd);

        jftDateEnd.setText("dd/mm/yyyy");
        jftDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jftDateEnd);

        jbPickDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPickDateEnd.setToolTipText("Seleccionar fecha final");
        jbPickDateEnd.setFocusable(false);
        jbPickDateEnd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbPickDateEnd);

        jPanel8.add(jPanel5);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCoBranch.setText("Sucursal empresa:");
        jlCoBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel15.add(jlCoBranch);

        jcbCoBranch.setPreferredSize(new java.awt.Dimension(325, 23));
        jPanel15.add(jcbCoBranch);

        jbPickCoBranch.setText("...");
        jbPickCoBranch.setToolTipText("Seleccionar sucursal");
        jbPickCoBranch.setFocusable(false);
        jbPickCoBranch.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel15.add(jbPickCoBranch);

        jPanel8.add(jPanel15);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgBizPartner.add(jrbBizPartner);
        jrbBizPartner.setSelected(true);
        jrbBizPartner.setText("Asociado negocios");
        jrbBizPartner.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel2.add(jrbBizPartner);

        jPanel8.add(jPanel2);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgBizPartner.add(jrbSalesAgent);
        jrbSalesAgent.setText("Agente ventas");
        jrbSalesAgent.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel11.add(jrbSalesAgent);

        jPanel8.add(jPanel11);

        jPanel98.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Asociado negocios: *");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel98.add(jlBizPartner);

        jcbBizPartner.setPreferredSize(new java.awt.Dimension(325, 23));
        jPanel98.add(jcbBizPartner);

        jbPickBizPartner.setText("...");
        jbPickBizPartner.setToolTipText("Seleccionar asociado negocios");
        jbPickBizPartner.setFocusable(false);
        jbPickBizPartner.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel98.add(jbPickBizPartner);

        jPanel8.add(jPanel98);

        jPanel99.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSalesAgent.setText("Agente ventas: *");
        jlSalesAgent.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel99.add(jlSalesAgent);

        jcbSalesAgent.setPreferredSize(new java.awt.Dimension(325, 23));
        jPanel99.add(jcbSalesAgent);

        jbPickSalesAgent.setText("...");
        jbPickSalesAgent.setToolTipText("Seleccionar agente ventas");
        jbPickSalesAgent.setFocusable(false);
        jbPickSalesAgent.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel99.add(jbPickSalesAgent);

        jPanel8.add(jPanel99);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckAdvancePayments.setText("Incluir anticipos");
        jckAdvancePayments.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel1.add(jckAdvancePayments);

        jPanel8.add(jPanel1);

        jpParams.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jpParams, java.awt.BorderLayout.CENTER);

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbPrint.setText("Imprimir");
        jbPrint.setToolTipText("[Ctrl + Enter]");
        jbPrint.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbPrint);

        jbClose.setText("Cerrar"); // NOI18N
        jbClose.setToolTipText("[Escape]");
        jbClose.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbClose);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(496, 339));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsCustom() {
        String name = "";
        
        moFieldDateStart = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbPickDateStart);
        moFieldDateEnd = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateEnd, jlDateEnd);
        moFieldDateEnd.setPickerButton(jbPickDateEnd);
        moFieldCoBranch = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCoBranch, jlCoBranch);
        moFieldCoBranch.setPickerButton(jbPickCoBranch);
        moFieldBizPartner = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbBizPartner, jlBizPartner);
        moFieldBizPartner.setPickerButton(jbPickBizPartner);
        moFieldSalesAgent = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbSalesAgent, jlSalesAgent);
        moFieldSalesAgent.setPickerButton(jbPickSalesAgent);

        mvFields = new Vector<>();
        mvFields.add(moFieldDateStart);
        mvFields.add(moFieldDateEnd);
        mvFields.add(moFieldCoBranch);
        mvFields.add(moFieldBizPartner);
        mvFields.add(moFieldSalesAgent);

        jbPickDateStart.addActionListener(this);
        jbPickDateEnd.addActionListener(this);
        jbPickBizPartner.addActionListener(this);
        jbPickSalesAgent.addActionListener(this);
        jbPrint.addActionListener(this);
        jbClose.addActionListener(this);
        jrbBizPartner.addItemListener(this);
        jrbSalesAgent.addItemListener(this);
        
        name = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategory, SUtilConsts.NUM_SNG);
        jlBizPartner.setText(name + ": *");
        jrbBizPartner.setText(name);
        jbPickBizPartner.setToolTipText(SUtilConsts.TXT_SELECT + " " + name.toLowerCase());
        
        switch (mnBizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                mnBizPartnerPicker = SDataConstants.BPSX_BP_SUP;
                jckAdvancePayments.setEnabled(true);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                mnBizPartnerPicker = SDataConstants.BPSX_BP_CUS;
                jckAdvancePayments.setEnabled(true);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CDR:
                mnBizPartnerPicker = SDataConstants.BPSX_BP_CDR;
                jckAdvancePayments.setEnabled(false);
                break;
            case SDataConstantsSys.BPSS_CT_BP_DBR:
                mnBizPartnerPicker = SDataConstants.BPSX_BP_DBR;
                jckAdvancePayments.setEnabled(false);
                break;
            default:
        }
        
        if (mnBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS) {
            jrbBizPartner.setEnabled(true);
            jrbSalesAgent.setEnabled(true);
            jlSalesAgent.setEnabled(true);
        }
        else {
            jrbBizPartner.setEnabled(false);
            jrbSalesAgent.setEnabled(false);
            jlSalesAgent.setEnabled(false);
        }
        
        jcbSalesAgent.setEnabled(false);
        jbPickSalesAgent.setEnabled(false);
        
        AbstractAction actionPrint = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPrint(); }
        };

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionClose(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionPrint, "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jftDateStart.requestFocus();
        }
    }

    @SuppressWarnings("unchecked")
    private void computeReport() {
        int idSysAccountClass = SLibConsts.UNDEFINED;
        String txtCoBranch = "";
        String txtBizPartner = "";
        String txtCurrency = "";
        String txtAdvancePayments = "";
        String sqlFilterAccountClass = "";
        String sqlFilterCoBranch = "";
        String sqlFilterBizPartner = "";
        Cursor oCursor = getCursor();
        JasperPrint print = null;
        JasperViewer viewer = null;
        HashMap<String, Object> map = null;
        
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // Report params:

            if (jcbCoBranch.getSelectedIndex() <= 0) {
                txtCoBranch = SUtilConsts.TXT_BRANCH.toUpperCase() + ": " + SUtilConsts.ALL_F;
            }
            else {
                txtCoBranch = SUtilConsts.TXT_BRANCH.toUpperCase() + ": " + jcbCoBranch.getSelectedItem().toString();
                sqlFilterCoBranch = "AND (d.fid_cob = " + moFieldCoBranch.getKeyAsIntArray()[0] + " OR (re.fid_dps_year_n IS NULL AND r.fid_cob = " + moFieldCoBranch.getKeyAsIntArray()[0] + ")) ";
            }

            if (mnBizPartnerCategory == SModSysConsts.BPSS_CT_BP_CUS) {
                if (jrbBizPartner.isSelected()) {
                    // Customer:
                    txtBizPartner = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategory, SUtilConsts.NUM_SNG).toUpperCase() + ": " + jcbBizPartner.getSelectedItem().toString();
                    sqlFilterBizPartner = "AND re.fid_bp_nr = " + moFieldBizPartner.getKeyAsIntArray()[0] + " ";
                }
                else {
                    // Sales agent:
                    txtBizPartner = SBpsConsts.BPS_ATT_SAL_AGT.toUpperCase() + ": " + jcbSalesAgent.getSelectedItem().toString();
                    sqlFilterBizPartner = "AND d.fid_sal_agt_n = " + moFieldSalesAgent.getKeyAsIntArray()[0] + " ";
                }
            }
            else {
                // Other business partner categories:
                txtBizPartner = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategory, SUtilConsts.NUM_SNG).toUpperCase() + ": " + jcbBizPartner.getSelectedItem().toString();
                sqlFilterBizPartner = "AND re.fid_bp_nr = " + moFieldBizPartner.getKeyAsIntArray()[0] + " ";
            }
            
            txtCurrency = SUtilConsts.TXT_CURRENCY.toUpperCase() + ": " + miClient.getSession().getSessionCustom().getLocalCurrency();
            
            switch (mnBizPartnerCategory) {
                case SDataConstantsSys.BPSS_CT_BP_SUP:
                    idSysAccountClass = SModSysConsts.FINS_CL_SYS_ACC_BPR_SUP;
                    sqlFilterAccountClass = jckAdvancePayments.isSelected() ? "" + SModSysConsts.FINS_CL_ACC_LIABTY[1] + ", " + SModSysConsts.FINS_CL_ACC_ASSET[1] : "" + SModSysConsts.FINS_CL_ACC_LIABTY[1];
                    txtAdvancePayments = "(" + (jckAdvancePayments.isSelected() ? SUtilConsts.WITH.toUpperCase() : SUtilConsts.WITHOUT.toUpperCase()) + " " + SDataRepConstants.ADV_PAYS.toUpperCase() + ")";
                    break;
                case SDataConstantsSys.BPSS_CT_BP_CUS:
                    idSysAccountClass = SModSysConsts.FINS_CL_SYS_ACC_BPR_CUS;
                    sqlFilterAccountClass = jckAdvancePayments.isSelected() ? "" + SModSysConsts.FINS_CL_ACC_ASSET[1] + ", " + SModSysConsts.FINS_CL_ACC_LIABTY[1] : "" + SModSysConsts.FINS_CL_ACC_ASSET[1];
                    txtAdvancePayments = "(" + (jckAdvancePayments.isSelected() ? SUtilConsts.WITH.toUpperCase() : SUtilConsts.WITHOUT.toUpperCase()) + " " + SDataRepConstants.ADV_PAYS.toUpperCase() + ")";
                    break;
                case SDataConstantsSys.BPSS_CT_BP_CDR:
                    idSysAccountClass = SModSysConsts.FINS_CL_SYS_ACC_BPR_CDR;
                    sqlFilterAccountClass = "" + SModSysConsts.FINS_CL_ACC_LIABTY[1];
                    break;
                case SDataConstantsSys.BPSS_CT_BP_DBR:
                    idSysAccountClass = SModSysConsts.FINS_CL_SYS_ACC_BPR_DBR;
                    sqlFilterAccountClass = "" + SModSysConsts.FINS_CL_ACC_ASSET[1];
                    break;
                default:
            }
            
            map = miClient.createReportParams();
            map.put("sTitle", getTitle().toUpperCase());
            map.put("sCoBranch", txtCoBranch);
            map.put("sBizPartner", txtBizPartner);
            map.put("sCurrency", txtCurrency);
            map.put("sAdvancePayments", txtAdvancePayments);
            map.put("nYear", SLibTimeUtils.digestYear(moFieldDateStart.getDate())[0]);
            map.put("tDateStart", moFieldDateStart.getDate());
            map.put("tDateEnd", moFieldDateEnd.getDate());
            map.put("nSysAccountClassId", idSysAccountClass);
            map.put("nAccountTypeId", SModSysConsts.FINS_TP_ACC_BAL);
            map.put("sSqlFilterAccountClass", sqlFilterAccountClass);
            map.put("sSqlFilterCoBranch", sqlFilterCoBranch);
            map.put("sSqlFilterBizPartner", sqlFilterBizPartner);

            // Report view:

            print = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_STA_BPS, map);
            viewer = new JasperViewer(print, false);
            viewer.setTitle(getTitle());
            viewer.setVisible(true);
        }
        catch(Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            setCursor(oCursor);
        }
    }
    
    private void itemStateChangedBizPartner() {
        jcbBizPartner.setEnabled(true);
        jbPickBizPartner.setEnabled(true);
        jcbSalesAgent.setEnabled(false);
        jbPickSalesAgent.setEnabled(false);
    }
    
    private void itemStateChangedSalesAgent() {
        jcbBizPartner.setEnabled(false);
        jbPickBizPartner.setEnabled(false);
        jcbSalesAgent.setEnabled(true);
        jbPickSalesAgent.setEnabled(true);
    }
    
    private void actionPickDateStart() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateStart.getDate(), moFieldDateStart);
    }

    private void actionPickDateEnd() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateEnd.getDate(), moFieldDateEnd);
    }

    private void actionPickBizPartner() {
        miClient.pickOption(mnBizPartnerPicker, moFieldBizPartner, null);
    }

    private void actionPickSalesAgent() {
        miClient.pickOption(SDataConstants.BPSX_BP_ATT_SAL_AGT, moFieldSalesAgent, moFieldSalesAgent.getKeyAsObjectArray());
    }

    private void actionPrint() {
        SFormValidation validation = formValidate();
                
        if (validation.getIsError()) {
            miClient.showMsgBoxWarning(validation.getMessage());
            validation.getComponent().requestFocus();
        }
        else {
            computeReport();
        }
    }

    private void actionClose() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgBizPartner;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbPickBizPartner;
    private javax.swing.JButton jbPickCoBranch;
    private javax.swing.JButton jbPickDateEnd;
    private javax.swing.JButton jbPickDateStart;
    private javax.swing.JButton jbPickSalesAgent;
    private javax.swing.JButton jbPrint;
    private javax.swing.JComboBox<SFormComponentItem> jcbBizPartner;
    private javax.swing.JComboBox<SFormComponentItem> jcbCoBranch;
    private javax.swing.JComboBox<SFormComponentItem> jcbSalesAgent;
    private javax.swing.JCheckBox jckAdvancePayments;
    private javax.swing.JFormattedTextField jftDateEnd;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlCoBranch;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlSalesAgent;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpParams;
    private javax.swing.JRadioButton jrbBizPartner;
    private javax.swing.JRadioButton jrbSalesAgent;
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

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        // Reset to default values:

        moFieldDateStart.setDate(SLibTimeUtilities.getBeginOfMonth(miClient.getSessionXXX().getWorkingDate()));
        moFieldDateEnd.setDate(SLibTimeUtilities.getEndOfMonth(miClient.getSessionXXX().getWorkingDate()));
        
        if (miClient.getSessionXXX().getCurrentCompanyBranch() != null) {
            moFieldCoBranch.setKey(miClient.getSessionXXX().getCurrentCompanyBranch().getPrimaryKey());
        }
        
        if (SLibUtils.belongsTo(mnBizPartnerCategory, new int[] { SModSysConsts.BPSS_CT_BP_SUP, SModSysConsts.BPSS_CT_BP_CUS })) {
            jckAdvancePayments.setSelected(true);
        }
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbCoBranch, SDataConstants.BPSU_BPB, new int[] { miClient.getSessionXXX().getCompany().getPkCompanyId() });
        SFormUtilities.populateComboBox(miClient, jcbBizPartner, mnBizPartnerPicker);
        
        if (mnBizPartnerCategory == SModSysConsts.BPSS_CT_BP_CUS) {
            SFormUtilities.populateComboBox(miClient, jcbSalesAgent, SDataConstants.BPSX_BP_ATT_SAL_AGT);
        }            
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        String msg = "";
        SFormValidation validation = new SFormValidation();

        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }
        
        if (!validation.getIsError()) {
            msg = SGuiUtilities.validateDateRange(moFieldDateStart.getDate(), moFieldDateEnd.getDate());

            if (!msg.isEmpty()) {
                validation.setMessage(msg);
                validation.setComponent(jftDateStart);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbPickDateStart) {
                actionPickDateStart();
            }
            else if (button == jbPickDateEnd) {
                actionPickDateEnd();
            }
            else if (button == jbPickBizPartner) {
                actionPickBizPartner();
            }
            else if (button == jbPickSalesAgent) {
                actionPickSalesAgent();
            }
            else if (button == jbPrint) {
                actionPrint();
            }
            if (button == jbClose) {
                actionClose();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton radioButton = (JRadioButton) e.getSource();
                
                if (radioButton == jrbBizPartner) {
                    itemStateChangedBizPartner();
                }
                else if (radioButton == jrbSalesAgent) {
                    itemStateChangedSalesAgent();
                }
            }
        }
    }
}
