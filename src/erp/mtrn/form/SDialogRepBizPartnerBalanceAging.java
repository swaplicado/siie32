/*
 * SDialogRepBizPartnerBalanceAging.java
 *
 * Created on 19/08/2008, 11:10 AM
 * Modified on 15/03/2008, 07:49 AM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
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
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;

/**
 *
 * @author  Néstor Ávalos, Sergio Flores
 */
public class SDialogRepBizPartnerBalanceAging extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private int mnBizPartnerCategory;
    private int mnBizPartnerPicker;
    private int[] manSysMovementTypeKey;
    private erp.lib.form.SFormField moFieldDateCutoff;
    private erp.lib.form.SFormField moFieldCoBranch;
    private erp.lib.form.SFormField moFieldBizPartner;
    private erp.lib.form.SFormField moFieldSalesAgent;
    private erp.lib.form.SFormField moFieldCurrency;


    /** Creates new form SDialogRepBizPartnerBalanceAging */
    public SDialogRepBizPartnerBalanceAging(erp.client.SClientInterface client, String title, int bizPartnerCategory) {
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

        bgPeriod = new javax.swing.ButtonGroup();
        bgDate = new javax.swing.ButtonGroup();
        bgType = new javax.swing.ButtonGroup();
        bgOrder = new javax.swing.ButtonGroup();
        jpParams = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlDateCutoff = new javax.swing.JLabel();
        jtfDateCutoff = new javax.swing.JFormattedTextField();
        jbPickDateCutoff = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jlCoBranch = new javax.swing.JLabel();
        jcbCoBranch = new javax.swing.JComboBox<SFormComponentItem>();
        jbPickCoBranch = new javax.swing.JButton();
        jPanel101 = new javax.swing.JPanel();
        jlRepType = new javax.swing.JLabel();
        jrbRepTypeDetail = new javax.swing.JRadioButton();
        jrbRepTypeSummary = new javax.swing.JRadioButton();
        jPanel103 = new javax.swing.JPanel();
        jlDueType = new javax.swing.JLabel();
        jrbDueType30_60_90d = new javax.swing.JRadioButton();
        jrbDueType15_30_45d = new javax.swing.JRadioButton();
        jPanel99 = new javax.swing.JPanel();
        jlDocDate = new javax.swing.JLabel();
        jrbDocDateDueDate = new javax.swing.JRadioButton();
        jrbDocDateBaseCredit = new javax.swing.JRadioButton();
        jPanel100 = new javax.swing.JPanel();
        jlDocSort = new javax.swing.JLabel();
        jrbDocSortDate = new javax.swing.JRadioButton();
        jrbDocSortNumber = new javax.swing.JRadioButton();
        jPanel102 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jcbBizPartner = new javax.swing.JComboBox<SFormComponentItem>();
        jbPickBizPartner = new javax.swing.JButton();
        jPanel104 = new javax.swing.JPanel();
        jlSalesAgent = new javax.swing.JLabel();
        jcbSalesAgent = new javax.swing.JComboBox<SFormComponentItem>();
        jbPickSalesAgent = new javax.swing.JButton();
        jPanel98 = new javax.swing.JPanel();
        jlCurrency = new javax.swing.JLabel();
        jcbCurrency = new javax.swing.JComboBox<SFormComponentItem>();
        jbPickCurrency = new javax.swing.JButton();
        jlCurrencyWarning = new javax.swing.JLabel();
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

        jPanel7.setLayout(new java.awt.GridLayout(9, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateCutoff.setText("Fecha corte: *");
        jlDateCutoff.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDateCutoff);

        jtfDateCutoff.setText("dd/mm/yyyy");
        jtfDateCutoff.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jtfDateCutoff);

        jbPickDateCutoff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPickDateCutoff.setToolTipText("Seleccionar fecha corte");
        jbPickDateCutoff.setFocusable(false);
        jbPickDateCutoff.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbPickDateCutoff);

        jPanel7.add(jPanel4);

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

        jPanel7.add(jPanel15);

        jPanel101.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlRepType.setText("Tipo reporte:");
        jlRepType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel101.add(jlRepType);

        bgType.add(jrbRepTypeDetail);
        jrbRepTypeDetail.setSelected(true);
        jrbRepTypeDetail.setText("Detallado");
        jrbRepTypeDetail.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel101.add(jrbRepTypeDetail);

        bgType.add(jrbRepTypeSummary);
        jrbRepTypeSummary.setText("Resumen");
        jrbRepTypeSummary.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel101.add(jrbRepTypeSummary);

        jPanel7.add(jPanel101);

        jPanel103.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDueType.setText("Tipo vencimientos:");
        jlDueType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel103.add(jlDueType);

        bgPeriod.add(jrbDueType30_60_90d);
        jrbDueType30_60_90d.setSelected(true);
        jrbDueType30_60_90d.setText("30, 60, 90 d");
        jrbDueType30_60_90d.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel103.add(jrbDueType30_60_90d);

        bgPeriod.add(jrbDueType15_30_45d);
        jrbDueType15_30_45d.setText("15, 30, 45, 60, 90, 180 d");
        jrbDueType15_30_45d.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel103.add(jrbDueType15_30_45d);

        jPanel7.add(jPanel103);

        jPanel99.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocDate.setText("Fecha doctos.:");
        jlDocDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel99.add(jlDocDate);

        bgDate.add(jrbDocDateDueDate);
        jrbDocDateDueDate.setText("Fecha de vencimiento");
        jrbDocDateDueDate.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel99.add(jrbDocDateDueDate);

        bgDate.add(jrbDocDateBaseCredit);
        jrbDocDateBaseCredit.setText("Fecha base de crédito");
        jrbDocDateBaseCredit.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel99.add(jrbDocDateBaseCredit);

        jPanel7.add(jPanel99);

        jPanel100.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocSort.setText("Orden doctos.:");
        jlDocSort.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel100.add(jlDocSort);

        bgOrder.add(jrbDocSortDate);
        jrbDocSortDate.setText("Por fecha");
        jrbDocSortDate.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel100.add(jrbDocSortDate);

        bgOrder.add(jrbDocSortNumber);
        jrbDocSortNumber.setText("Por folio");
        jrbDocSortNumber.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel100.add(jrbDocSortNumber);

        jPanel7.add(jPanel100);

        jPanel102.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Asociado negocios: *");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel102.add(jlBizPartner);

        jcbBizPartner.setPreferredSize(new java.awt.Dimension(325, 23));
        jPanel102.add(jcbBizPartner);

        jbPickBizPartner.setText("...");
        jbPickBizPartner.setToolTipText("Seleccionar asociado negocios");
        jbPickBizPartner.setFocusable(false);
        jbPickBizPartner.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel102.add(jbPickBizPartner);

        jPanel7.add(jPanel102);

        jPanel104.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSalesAgent.setText("Agente ventas:");
        jlSalesAgent.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel104.add(jlSalesAgent);

        jcbSalesAgent.setPreferredSize(new java.awt.Dimension(325, 23));
        jPanel104.add(jcbSalesAgent);

        jbPickSalesAgent.setText("...");
        jbPickSalesAgent.setToolTipText("Seleccionar agente ventas");
        jbPickSalesAgent.setFocusable(false);
        jbPickSalesAgent.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel104.add(jbPickSalesAgent);

        jPanel7.add(jPanel104);

        jPanel98.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCurrency.setText("Moneda: *");
        jlCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel98.add(jlCurrency);

        jcbCurrency.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel98.add(jcbCurrency);

        jbPickCurrency.setText("...");
        jbPickCurrency.setToolTipText("Seleccionar moneda");
        jbPickCurrency.setFocusable(false);
        jbPickCurrency.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel98.add(jbPickCurrency);

        jlCurrencyWarning.setForeground(java.awt.Color.red);
        jlCurrencyWarning.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlCurrencyWarning.setText("NOTA: ¡Solo saldos en XXX!");
        jlCurrencyWarning.setPreferredSize(new java.awt.Dimension(170, 23));
        jPanel98.add(jlCurrencyWarning);

        jPanel7.add(jPanel98);

        jpParams.add(jPanel7, java.awt.BorderLayout.NORTH);

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

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(496, 339));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsCustom() {
        String name = "";
        
        moFieldDateCutoff = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jtfDateCutoff, jlDateCutoff);
        moFieldDateCutoff.setPickerButton(jbPickDateCutoff);
        moFieldCoBranch = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCoBranch, jlCoBranch);
        moFieldCoBranch.setPickerButton(jbPickCoBranch);
        moFieldBizPartner = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbBizPartner, jlBizPartner);
        moFieldBizPartner.setPickerButton(jbPickBizPartner);
        moFieldSalesAgent = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbSalesAgent, jlSalesAgent);
        moFieldSalesAgent.setPickerButton(jbPickSalesAgent);
        moFieldCurrency = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCurrency, jlCurrency);
        moFieldCurrency.setPickerButton(jbPickCurrency);

        mvFields = new Vector<>();
        mvFields.add(moFieldDateCutoff);
        mvFields.add(moFieldCoBranch);
        mvFields.add(moFieldBizPartner);
        mvFields.add(moFieldSalesAgent);
        mvFields.add(moFieldCurrency);

        jbPrint.addActionListener(this);
        jbClose.addActionListener(this);
        jbPickDateCutoff.addActionListener(this);
        jbPickCoBranch.addActionListener(this);
        jbPickBizPartner.addActionListener(this);
        jbPickSalesAgent.addActionListener(this);
        jbPickCurrency.addActionListener(this);
        jrbRepTypeDetail.addItemListener(this);
        jrbRepTypeSummary.addItemListener(this);
        jrbDueType30_60_90d.addItemListener(this);
        jrbDueType15_30_45d.addItemListener(this);
        jcbCurrency.addItemListener(this);

        name = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategory, SUtilConsts.NUM_SNG);
        jlBizPartner.setText(name + ":");
        jbPickBizPartner.setToolTipText(SUtilConsts.TXT_SELECT + " " + name.toLowerCase());
        
        switch (mnBizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                mnBizPartnerPicker = SDataConstants.BPSX_BP_SUP;
                manSysMovementTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
                jlSalesAgent.setEnabled(false);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                mnBizPartnerPicker = SDataConstants.BPSX_BP_CUS;
                manSysMovementTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
                jlSalesAgent.setEnabled(true); // defined already enabled, just for consistency
                break;
            default:
        }
        
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
            jtfDateCutoff.requestFocus();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void computeReport30_60_90() {
        // New standard report style:
        
        int idSysAccountClass = SLibConsts.UNDEFINED;
        String txtCoBranch = "";
        String txtBizPartner = "";
        String txtSalesAgent= "";
        String txtCurrency = "";
        String sqlFilterCoBranch = "";
        String sqlFilterBizPartner = "";
        String sqlFilterSalesAgent = "";
        String sqlFilterCurrency = "";
        String sqlSortBy = "";
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
            
            if (jcbBizPartner.getSelectedIndex() <= 0) {
                txtBizPartner = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategory, SUtilConsts.NUM_SNG).toUpperCase() + ": " + SUtilConsts.ALL;
            }
            else {
                txtBizPartner = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategory, SUtilConsts.NUM_SNG).toUpperCase() + ": " + jcbBizPartner.getSelectedItem().toString();
                sqlFilterBizPartner = "AND re.fid_bp_nr = " + moFieldBizPartner.getKeyAsIntArray()[0] + " ";
            }
            
            if (jcbSalesAgent.getSelectedIndex() <= 0) {
                txtSalesAgent = "";
            }
            else {
                txtSalesAgent = SBpsConsts.BPS_ATT_SAL_AGT.toUpperCase() + ": " + jcbSalesAgent.getSelectedItem().toString();
                sqlFilterSalesAgent = "AND d.fid_sal_agt_n = " + moFieldSalesAgent.getKeyAsIntArray()[0] + " ";
            }
            
            txtCurrency = SUtilConsts.TXT_CURRENCY.toUpperCase() + ": " + jcbCurrency.getSelectedItem().toString();
            
            if (!miClient.getSession().getSessionCustom().isLocalCurrency(moFieldCurrency.getKeyAsIntArray())) {
                sqlFilterCurrency = "AND (d.fid_cur = " + moFieldCurrency.getKeyAsIntArray()[0] + " OR (re.fid_dps_year_n IS NULL AND re.fid_cur = " + moFieldCurrency.getKeyAsIntArray()[0] + ")) ";
            }
            
            sqlSortBy = "d.num_ser, d.num, d.id_year, d.id_doc, re.ref"; // sorting columns needed on both options
            
            if (jrbDocSortNumber.isSelected()) {
                sqlSortBy += " ";
            }
            else {
                sqlSortBy = (jrbDocDateDueDate.isSelected() ? "_due_dt" : "d.dt_start_cred") + ", " + sqlSortBy + " ";
            }
            
            switch (mnBizPartnerCategory) {
                case SDataConstantsSys.BPSS_CT_BP_SUP:
                    idSysAccountClass = SModSysConsts.FINS_CL_SYS_ACC_BPR_SUP;
                    break;
                case SDataConstantsSys.BPSS_CT_BP_CUS:
                    idSysAccountClass = SModSysConsts.FINS_CL_SYS_ACC_BPR_CUS;
                    break;
                default:
            }
            
            map = miClient.createReportParams();
            map.put("sTitle", getTitle().toUpperCase());
            map.put("bShowDetail", jrbRepTypeDetail.isSelected());
            map.put("sCoBranch", txtCoBranch);
            map.put("sBizPartner", txtBizPartner);
            map.put("sSalesAgent", txtSalesAgent);
            map.put("nLocalCurrencyId", miClient.getSession().getSessionCustom().getLocalCurrencyKey()[0]);
            map.put("nCurrencyId", moFieldCurrency.getKeyAsIntArray()[0]);
            map.put("sCurrency", txtCurrency);
            map.put("nYear", SLibTimeUtils.digestYear(moFieldDateCutoff.getDate())[0]);
            map.put("tDateCutoff", moFieldDateCutoff.getDate());
            map.put("nSysAccountClassId", idSysAccountClass);
            map.put("nAccountTypeId", SModSysConsts.FINS_TP_ACC_BAL);
            map.put("sSqlFilterCoBranch", sqlFilterCoBranch);
            map.put("sSqlFilterBizPartner", sqlFilterBizPartner);
            map.put("sSqlFilterSalesAgent", sqlFilterSalesAgent);
            map.put("sSqlFilterCurrency", sqlFilterCurrency);
            map.put("sSqlSortBy", sqlSortBy);

            // Report view:

            print = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_BPS_ACC_AGI, map);
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
    
    @SuppressWarnings("unchecked")
    private void computeReport15_30_45() {
        // Former report style:
        
        int nSortBizPartner = 0;
        String nameCoBranch = "";
        String nameBizPartner = "";
        String sqlCoBranch = "";
        String sqlBizPartner = "";
        String sqlOrderBy = "";
        Cursor oCursor = getCursor();
        JasperPrint oPrint = null;
        JasperViewer oViewer = null;
        Map<String, Object> oMap = null;
        
        if (jcbCoBranch.getSelectedIndex() <= 0) {
            nameCoBranch = SUtilConsts.ALL_F;
        }
        else {
            sqlCoBranch = " AND d.fid_cob = " + moFieldCoBranch.getKeyAsIntArray()[0] + " ";
            nameCoBranch = jcbCoBranch.getSelectedItem().toString();
        }

        if (jcbBizPartner.getSelectedIndex() <= 0) {
            nameBizPartner = SUtilConsts.ALL;
        }
        else {
            sqlBizPartner = " AND re.fid_bp_nr = " + moFieldBizPartner.getKeyAsIntArray()[0] + " ";
            nameBizPartner = jcbBizPartner.getSelectedItem().toString();
        }

        if (mnBizPartnerCategory == SModSysConsts.BPSS_CT_BP_SUP) {
            nSortBizPartner = miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId();
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sqlOrderBy = "ct.bp_key, bp.bp_comm, bp.bp, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r ASC ";
            }
            else if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
                sqlOrderBy = "bp.bp, ct.bp_key, bp.bp_comm, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r ASC ";
            }
            else {
                sqlOrderBy = "bp.bp_comm, ct.bp_key, bp.bp, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r ASC ";
            }
        }
        else {
            nSortBizPartner = miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId();
            if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sqlOrderBy = "ct.bp_key, bp.bp_comm, bp.bp, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r DESC ";
            }
            else if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
                sqlOrderBy = "bp.bp, ct.bp_key, bp.bp_comm, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r DESC ";
            }
            else {
                sqlOrderBy = "bp.bp_comm, ct.bp_key, bp.bp, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r DESC ";
            }
        }

        try {
            oMap = miClient.createReportParams();
            oMap.put("sReportTitle", getTitle().toUpperCase());
            oMap.put("sCob", nameCoBranch);
            oMap.put("sBp", nameBizPartner);
            oMap.put("sCurrency", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getCurrency());
            oMap.put("nFkCtBpId", mnBizPartnerCategory);
            oMap.put("nFkCtSysMovSupId", SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0]);
            oMap.put("nFkTpSysMovSupId", SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1]);
            oMap.put("nFkCtSysMovId", manSysMovementTypeKey[0]);
            oMap.put("nFkTpSysMovId", manSysMovementTypeKey[1]);
            oMap.put("nFkYearId", SLibTimeUtilities.digestYear(moFieldDateCutoff.getDate())[0]);
            oMap.put("tDateCut", moFieldDateCutoff.getDate());
            oMap.put("nDateEval", (jrbDocDateDueDate.isSelected() ? 1 : 0)); // 1: Due's date, 0: Credit's date.
            oMap.put("nSort", (jrbDocSortDate.isSelected() ? 1 : 0)); // 1: Sort by date, 0: Sort by num doc.
            oMap.put("nCfgsTpSortNameKey", SDataConstantsSys.CFGS_TP_SORT_NAME_KEY);
            oMap.put("nCfgsTpSortKeyName", SDataConstantsSys.CFGS_TP_SORT_KEY_NAME);
            oMap.put("nCfgsTpSort", nSortBizPartner);
            oMap.put("sSqlFkBpId", sqlBizPartner);
            oMap.put("sSqlCob", sqlCoBranch);
            oMap.put("sSqlOrderBy", sqlOrderBy);
            oMap.put("nTP_ACC", mnBizPartnerCategory == SModSysConsts.BPSS_CT_BP_SUP ? SDataConstantsSys.FINS_CL_ACC_ASSET[0] : SDataConstantsSys.FINS_CL_ACC_LIABTY[0]);
            oMap.put("nCL_ACC", mnBizPartnerCategory == SModSysConsts.BPSS_CT_BP_SUP ? SDataConstantsSys.FINS_CL_ACC_ASSET[1] : SDataConstantsSys.FINS_CL_ACC_LIABTY[1]);
            oMap.put("nFkBpCtSupId", SDataConstantsSys.BPSS_CT_BP_SUP);
            oMap.put("nRange1", 15);
            oMap.put("nRange2", 30);
            oMap.put("nRange3", 45);
            oMap.put("nRange4", 60);
            oMap.put("nRange5", 90);
            oMap.put("nRange6", 180);
            oMap.put("nRange7", 360);
            oMap.put("bIsDetail", jrbRepTypeDetail.isSelected());

            oPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_BAL_AGI, oMap);
            oViewer = new JasperViewer(oPrint, false);
            oViewer.setTitle(getTitle());
            oViewer.setVisible(true);
        }
        catch(Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            setCursor(oCursor);
        }
    }
    
    private void itemStateChangedRepType() {
        jrbDocDateDueDate.setSelected(true);
        jrbDocSortDate.setSelected(true);
        moFieldBizPartner.resetField();
        
        if (jrbRepTypeDetail.isSelected()) {
            jrbDocDateDueDate.setEnabled(true);
            jrbDocDateBaseCredit.setEnabled(true);
            jrbDocSortDate.setEnabled(true);
            jrbDocSortNumber.setEnabled(true);
            jcbBizPartner.setEnabled(true);
            jbPickBizPartner.setEnabled(true);
        }
        else {
            jrbDocDateDueDate.setEnabled(false);
            jrbDocDateBaseCredit.setEnabled(false);
            jrbDocSortDate.setEnabled(false);
            jrbDocSortNumber.setEnabled(false);
            jcbBizPartner.setEnabled(false);
            jbPickBizPartner.setEnabled(false);
        }
    }

    private void itemStateChangedDueType() {
        moFieldSalesAgent.resetField();
        moFieldCurrency.setFieldValue(miClient.getSession().getSessionCustom().getLocalCurrencyKey());
                
        if (jrbDueType30_60_90d.isSelected()) {
            jcbSalesAgent.setEnabled(mnBizPartnerCategory == SModSysConsts.BPSS_CT_BP_CUS);
            jbPickSalesAgent.setEnabled(mnBizPartnerCategory == SModSysConsts.BPSS_CT_BP_CUS);
            jcbCurrency.setEnabled(true);
            jbPickCurrency.setEnabled(true);
        }
        else {
            jcbSalesAgent.setEnabled(false);
            jbPickSalesAgent.setEnabled(false);
            jcbCurrency.setEnabled(false);
            jbPickCurrency.setEnabled(false);
        }
    }

    private void itemStateChangedCurrency() {
        if (jcbCurrency.getSelectedIndex() <= 0) {
            jlCurrencyWarning.setText("");
        }
        else {
            jlCurrencyWarning.setText(miClient.getSession().getSessionCustom().isLocalCurrency(moFieldCurrency.getKeyAsIntArray()) ? 
                    "" : "NOTA: ¡Solo saldos en " + miClient.getSession().getSessionCustom().getCurrencyCode(moFieldCurrency.getKeyAsIntArray()) + "!");
        }
    }
    
    private void actionPickDateCutoff() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateCutoff.getDate(), moFieldDateCutoff);
    }

    private void actionPickCoBranch() {
        miClient.pickOption(SDataConstants.BPSU_BPB, moFieldCoBranch, new int[] { miClient.getSessionXXX().getCompany().getPkCompanyId() });
    }
    
    private void actionPickBizPartner() {
        miClient.pickOption(mnBizPartnerPicker, moFieldBizPartner, null);
    }

    private void actionPickSalesAgent() {
        miClient.pickOption(SDataConstants.BPSX_BP_ATT_SAL_AGT, moFieldSalesAgent, null);
    }

    private void actionPickCurrency() {
        miClient.pickOption(SDataConstants.CFGU_CUR, moFieldCurrency, null);
    }

    private void actionPrint() {
        SFormValidation validation = formValidate();

        if (!validation.getIsError()) {
            if (jrbDueType30_60_90d.isSelected()) {
                computeReport30_60_90(); // new standard report style
            }
            else {
                computeReport15_30_45(); // former report style
            }
        }
        else {
            miClient.showMsgBoxWarning(validation.getMessage());
            validation.getComponent().requestFocus();
        }
    }

    private void actionClose() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgDate;
    private javax.swing.ButtonGroup bgOrder;
    private javax.swing.ButtonGroup bgPeriod;
    private javax.swing.ButtonGroup bgType;
    private javax.swing.JPanel jPanel100;
    private javax.swing.JPanel jPanel101;
    private javax.swing.JPanel jPanel102;
    private javax.swing.JPanel jPanel103;
    private javax.swing.JPanel jPanel104;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbPickBizPartner;
    private javax.swing.JButton jbPickCoBranch;
    private javax.swing.JButton jbPickCurrency;
    private javax.swing.JButton jbPickDateCutoff;
    private javax.swing.JButton jbPickSalesAgent;
    private javax.swing.JButton jbPrint;
    private javax.swing.JComboBox<SFormComponentItem> jcbBizPartner;
    private javax.swing.JComboBox<SFormComponentItem> jcbCoBranch;
    private javax.swing.JComboBox<SFormComponentItem> jcbCurrency;
    private javax.swing.JComboBox<SFormComponentItem> jcbSalesAgent;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlCoBranch;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlCurrencyWarning;
    private javax.swing.JLabel jlDateCutoff;
    private javax.swing.JLabel jlDocDate;
    private javax.swing.JLabel jlDocSort;
    private javax.swing.JLabel jlDueType;
    private javax.swing.JLabel jlRepType;
    private javax.swing.JLabel jlSalesAgent;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpParams;
    private javax.swing.JRadioButton jrbDocDateBaseCredit;
    private javax.swing.JRadioButton jrbDocDateDueDate;
    private javax.swing.JRadioButton jrbDocSortDate;
    private javax.swing.JRadioButton jrbDocSortNumber;
    private javax.swing.JRadioButton jrbDueType15_30_45d;
    private javax.swing.JRadioButton jrbDueType30_60_90d;
    private javax.swing.JRadioButton jrbRepTypeDetail;
    private javax.swing.JRadioButton jrbRepTypeSummary;
    private javax.swing.JFormattedTextField jtfDateCutoff;
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

        moFieldDateCutoff.setDate(miClient.getSessionXXX().getWorkingDate());
        
        if (miClient.getSessionXXX().getCurrentCompanyBranch() != null) {
            moFieldCoBranch.setKey(miClient.getSessionXXX().getCurrentCompanyBranch().getPrimaryKey());
        }
        
        jrbRepTypeDetail.setSelected(true);
        itemStateChangedRepType(); // sets default values for remaining parameters
        
        jrbDueType30_60_90d.setSelected(true);
        itemStateChangedDueType(); // sets default values for remaining parameters
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbCoBranch, SDataConstants.BPSU_BPB, new int[] { miClient.getSessionXXX().getCompany().getPkCompanyId() });
        SFormUtilities.populateComboBox(miClient, jcbBizPartner, mnBizPartnerPicker);
        SFormUtilities.populateComboBox(miClient, jcbSalesAgent, SDataConstants.BPSX_BP_ATT_SAL_AGT);
        SFormUtilities.populateComboBox(miClient, jcbCurrency, SDataConstants.CFGU_CUR);
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
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbClose) {
                actionClose();
            }
            else if (button == jbPickDateCutoff) {
                actionPickDateCutoff();
            }
            else if (button == jbPickCoBranch) {
                actionPickCoBranch();
            }
            else if (button == jbPickBizPartner) {
                actionPickBizPartner();
            }
            else if (button == jbPickSalesAgent) {
                actionPickSalesAgent();
            }
            else if (button == jbPickCurrency) {
                actionPickCurrency();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox) e.getSource();
                
                if (comboBox == jcbCurrency) {
                    itemStateChangedCurrency();
                }
            }
        }
        else if (e.getSource() instanceof JRadioButton) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton radioButton = (JRadioButton) e.getSource();
                
                if (radioButton == jrbRepTypeDetail) {
                    itemStateChangedRepType();
                }
                else if (radioButton == jrbRepTypeSummary) {
                    itemStateChangedRepType();
                }
                else if (radioButton == jrbDueType30_60_90d) {
                    itemStateChangedDueType();
                }
                else if (radioButton == jrbDueType15_30_45d) {
                    itemStateChangedDueType();
                }
            }
        }
    }
}
