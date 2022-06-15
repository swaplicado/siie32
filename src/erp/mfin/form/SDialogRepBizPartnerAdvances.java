/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepBizPartnerAdvances.java
 *
 * Created on 23/02/2017
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Edwin Carmona
 */
public class SDialogRepBizPartnerAdvances extends javax.swing.JDialog implements java.awt.event.ActionListener, java.awt.event.ItemListener {

    private final erp.client.SClientInterface miClient;
    int mnBizPartnerCategoryId;
    boolean mbFirstTime;

    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldCurrency;
    private erp.lib.form.SFormField moFieldBizPartner;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;

    int mnOptionPickerId;
    int[] manSysMoveTypeKey;
    private java.lang.String msBizPartnerCatSng;

    /** Creates new form SDialogRepBizPartnerAdv
     * @param client
     * @param idBizPartnerCategory */
    public SDialogRepBizPartnerAdvances(erp.client.SClientInterface client, int idBizPartnerCategory) {
        super(client.getFrame(), true);
        miClient = client;
        mnBizPartnerCategoryId = idBizPartnerCategory;
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

        bgCurrency = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jbPickDate = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jrbCurrencyLoc = new javax.swing.JRadioButton();
        jPanel16 = new javax.swing.JPanel();
        jrbCurrencyDoc = new javax.swing.JRadioButton();
        jlCurrencyDocWarning = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jlCurrency = new javax.swing.JLabel();
        jcbCurrency = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jcbBizPartner = new javax.swing.JComboBox();
        jbPickBizPartner = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jpPrint = new javax.swing.JButton();
        jpClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha corte: *");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlDate);

        jftDate.setText("dd/mm/yyyy");
        jftDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jftDate);

        jbPickDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPickDate.setToolTipText("Seleccionar fecha");
        jbPickDate.setFocusable(false);
        jbPickDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbPickDate);

        jPanel6.add(jPanel3);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgCurrency.add(jrbCurrencyLoc);
        jrbCurrencyLoc.setText("Moneda local");
        jrbCurrencyLoc.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel15.add(jrbCurrencyLoc);

        jPanel6.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        bgCurrency.add(jrbCurrencyDoc);
        jrbCurrencyDoc.setText("Moneda original");
        jrbCurrencyDoc.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel16.add(jrbCurrencyDoc);

        jlCurrencyDocWarning.setForeground(new java.awt.Color(255, 0, 0));
        jlCurrencyDocWarning.setText("NOTA: ¡Solo operaciones en la moneda seleccionada!");
        jlCurrencyDocWarning.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel16.add(jlCurrencyDocWarning);

        jPanel6.add(jPanel16);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCurrency.setText("Moneda:*");
        jlCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel18.add(jlCurrency);

        jcbCurrency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCurrency.setSelectedIndex(-1);
        jcbCurrency.setName(""); // NOI18N
        jcbCurrency.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel18.add(jcbCurrency);

        jPanel6.add(jPanel18);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("<Asoc. negocios>:");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlBizPartner);

        jcbBizPartner.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbBizPartner.setSelectedIndex(-1);
        jcbBizPartner.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel4.add(jcbBizPartner);

        jbPickBizPartner.setText("...");
        jbPickBizPartner.setToolTipText("Seleccionar asociado de negocios:");
        jbPickBizPartner.setFocusable(false);
        jbPickBizPartner.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbPickBizPartner);

        jPanel6.add(jPanel4);

        jPanel2.add(jPanel6, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jpPrint.setText("Imprimir");
        jpPrint.setToolTipText("[Ctrl + Enter]");
        jpPrint.setPreferredSize(new java.awt.Dimension(75, 23));
        jpPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jpPrintActionPerformed(evt);
            }
        });
        jPanel1.add(jpPrint);

        jpClose.setText("Cerrar");
        jpClose.setToolTipText("[Escape]");
        jpClose.setPreferredSize(new java.awt.Dimension(75, 23));
        jpClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jpCloseActionPerformed(evt);
            }
        });
        jPanel1.add(jpClose);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(576, 389));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jpPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpPrintActionPerformed
        actionPrint();
    }//GEN-LAST:event_jpPrintActionPerformed

    private void jpCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpCloseActionPerformed
        actionClose();
    }//GEN-LAST:event_jpCloseActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    @SuppressWarnings("unchecked")
    private void initComponentsExtra() {
        msBizPartnerCatSng = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategoryId, SUtilConsts.NUM_SNG);
        
        setTitle("Saldos de anticipos contables de " + (mnBizPartnerCategoryId  == SDataConstantsSys.BPSS_CT_BP_CUS ? "clientes" : "proveedores"));
        jlBizPartner.setText(msBizPartnerCatSng + ":");
        jbPickBizPartner.setToolTipText(SUtilConsts.TXT_SELECT + " " + msBizPartnerCatSng.toLowerCase());
        
        switch (mnBizPartnerCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                mnOptionPickerId = SDataConstants.BPSX_BP_SUP;
                manSysMoveTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                mnOptionPickerId = SDataConstants.BPSX_BP_CUS;
                manSysMoveTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        moFieldDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDate, jlDate);
        moFieldDate.setPickerButton(jbPickDate);
        moFieldCurrency = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCurrency, jlCurrency);
        moFieldBizPartner = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbBizPartner, jlBizPartner);
        moFieldBizPartner.setPickerButton(jbPickBizPartner);

        mvFields = new Vector<>();
        mvFields.add(moFieldDate);
        mvFields.add(moFieldCurrency);
        mvFields.add(moFieldBizPartner);

        jbPickDate.addActionListener(this);
        jrbCurrencyLoc.addItemListener(this);
        jrbCurrencyDoc.addItemListener(this);
        jcbCurrency.addItemListener(this);
        jbPickBizPartner.addActionListener(this);

        resetForm();

        SFormUtilities.populateComboBox(miClient, jcbCurrency, SDataConstants.CFGU_CUR);
        SFormUtilities.populateComboBox(miClient, jcbBizPartner, mnOptionPickerId);
        
        setModalityType(ModalityType.MODELESS);
        SFormUtilities.createActionMap(rootPane, this, "actionPrint", "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionClose", "close", KeyEvent.VK_ESCAPE, 0);
        jrbCurrencyLoc.setSelected(true);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jftDate.requestFocus();
        }
    }
    
    private void itemStateChangedCurrencyOptions() {
        if (jrbCurrencyLoc.isSelected()) {
            jlCurrencyDocWarning.setVisible(false);
            jcbCurrency.setEnabled(false);
            moFieldCurrency.setKey(miClient.getSession().getSessionCustom().getLocalCurrencyKey());
        }
        else if (jrbCurrencyDoc.isSelected()) {
            jlCurrencyDocWarning.setVisible(true);
            jcbCurrency.setEnabled(true);
            moFieldCurrency.resetField();
        }
    }

    private void print() {
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];  
        int period = SLibTimeUtilities.digestYearMonth(moFieldDate.getDate())[1];
        String filterBp = "";
        String filterCur = "";
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        boolean isCtBpCus = mnBizPartnerCategoryId  == SDataConstantsSys.BPSS_CT_BP_CUS;
        
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            filterBp =  (jcbBizPartner.getSelectedIndex() <= 0 ? "" : " AND ety.fid_bp_nr = " + moFieldBizPartner.getKeyAsIntArray()[0] + " ");
            filterCur = !jrbCurrencyDoc.isSelected() ? "" : " AND ety.fid_cur = " + moFieldCurrency.getKeyAsIntArray()[0] + " ";
            
            map = miClient.createReportParams();
            map.put("nCurrencyId", moFieldCurrency.getKeyAsIntArray()[0]);
            map.put("sCurrency", jcbCurrency.getSelectedItem().toString());
            map.put("bIsLocalCurrency", miClient.getSession().getSessionCustom().getLocalCurrencyKey()[0] == moFieldCurrency.getKeyAsIntArray()[0]);
            map.put("nYear", year);
            map.put("nPeriod", period);
            map.put("sDate", SLibUtils.DateFormatDate.format(moFieldDate.getDate()));
            map.put("nBizPartnerCus", SDataConstantsSys.BPSS_CT_BP_CUS);
            map.put("nBizPartnerType", mnBizPartnerCategoryId);
            map.put("sBizPartnerType", isCtBpCus ? "CLIENTES" : "PROVEEDORES");
            map.put("nClassSysMovF", isCtBpCus ? SModSysConsts.FINS_CL_SYS_MOV_SAL : SModSysConsts.FINS_CL_SYS_MOV_PUR);
            map.put("nClassSysMovNFT1", isCtBpCus ? SModSysConsts.FINS_CL_SYS_MOV_MI :SModSysConsts.FINS_CL_SYS_MOV_MO);
            map.put("nClassSysMovNFT2", isCtBpCus ? SModSysConsts.FINS_CL_SYS_MOV_MO : SModSysConsts.FINS_CL_SYS_MOV_MI);
            map.put("nClassSysMovNFT3", isCtBpCus? SModSysConsts.FINS_CL_SYS_MOV_CUS : SModSysConsts.FINS_CL_SYS_MOV_SUP);
            map.put("nClassSysAcc", isCtBpCus ? SModSysConsts.FINS_CL_SYS_ACC_BPR_CUS : SModSysConsts.FINS_CL_SYS_ACC_BPR_SUP);
            map.put("sBizPartner", (jcbBizPartner.getSelectedIndex() <= 0 ? "(TODOS)" : moFieldBizPartner.getString()));
            map.put("sFilterBizPartner", filterBp);
            map.put("sFilterCurrency", filterCur);
            
            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_REC_ADV, map);
            jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle(getTitle());
            jasperViewer.setVisible(true);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            setCursor(cursor);
        }
    }

    private void actionPickDate() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDate.getDate(), moFieldDate);
    }

    private void actionPickBizPartner() {
        miClient.pickOption(mnOptionPickerId, moFieldBizPartner, null);
    }

    public void actionPrint() {
        SFormValidation validation = formValidate();
                
        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (!validation.getMessage().isEmpty()) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            print();
        }
    }

    public void actionClose() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgCurrency;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jbPickBizPartner;
    private javax.swing.JButton jbPickDate;
    private javax.swing.JComboBox jcbBizPartner;
    private javax.swing.JComboBox jcbCurrency;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlCurrencyDocWarning;
    private javax.swing.JLabel jlDate;
    private javax.swing.JButton jpClose;
    private javax.swing.JButton jpPrint;
    private javax.swing.JRadioButton jrbCurrencyDoc;
    private javax.swing.JRadioButton jrbCurrencyLoc;
    // End of variables declaration//GEN-END:variables

    public void resetForm() {
        mbFirstTime = true;
        moFieldDate.setFieldValue(miClient.getSession().getCurrentDate());
    }
    
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPickDate) {
                actionPickDate();
            }
            else if (button == jbPickBizPartner) {
                actionPickBizPartner();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JRadioButton) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton radioButton = (JRadioButton) e.getSource();
                
                if (radioButton == jrbCurrencyLoc) {
                    itemStateChangedCurrencyOptions();
                }
                else if (radioButton == jrbCurrencyDoc) {
                    itemStateChangedCurrencyOptions();
                }
            }
        }
    } 
}
