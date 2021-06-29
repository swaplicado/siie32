/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepSalesPurchasesDiary.java
 *
 * Created on 11/08/2010, 05:37:35 PM
 */

package erp.mtrn.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mtrn.data.STrnFunctionalAreaUtils;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Alfonso Flores
 */
public class SDialogRepSalesPurchasesJournal extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldDateInitial;
    private erp.lib.form.SFormField moFieldDateEnd;
    private erp.lib.form.SFormField moFieldCompanyBranch;

    private boolean mbParamIsSupplier;
    
    private erp.mtrn.form.SDialogFilterFunctionalArea moDialogFilterFunctionalArea;
    private int mnFunctionalAreaId;
    private int[] manDataFilter;
    private String msFunctionalAreasIds;

    /** Creates new form SDialogRepSalesPurchasesDiary */
    public SDialogRepSalesPurchasesJournal(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;

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

        jPanel1 = new javax.swing.JPanel();
        jbPrint = new javax.swing.JButton();
        jbExit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlDateInitial = new javax.swing.JLabel();
        jftDateInitial = new javax.swing.JFormattedTextField();
        jbDateInitial = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        jftDateEnd = new javax.swing.JFormattedTextField();
        jbDateEnd = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlCompanyBranch = new javax.swing.JLabel();
        jcbCompanyBranch = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        jlBizPartner1 = new javax.swing.JLabel();
        jtfFunctionalArea = new javax.swing.JTextField();
        jbFunctionalArea = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jckWithoutRelatedParty = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reporte diario de ventas");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(392, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbPrint.setText("Imprimir");
        jbPrint.setToolTipText("[Ctrl + Enter]");
        jbPrint.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbPrint);

        jbExit.setText("Cerrar");
        jbExit.setToolTipText("[Escape]");
        jbExit.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbExit);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del reporte:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Período:"));
        jPanel3.setPreferredSize(new java.awt.Dimension(376, 77));
        jPanel3.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateInitial.setText("Fecha inicial: *");
        jlDateInitial.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDateInitial);

        jftDateInitial.setText("dd/mm/yyyy");
        jftDateInitial.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jftDateInitial);

        jbDateInitial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateInitial.setToolTipText("Seleccionar fecha inicial");
        jbDateInitial.setFocusable(false);
        jbDateInitial.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbDateInitial);

        jPanel3.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final: *");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlDateEnd);

        jftDateEnd.setText("dd/mm/yyyy");
        jftDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jftDateEnd);

        jbDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateEnd.setToolTipText("Seleccionar fecha final");
        jbDateEnd.setFocusable(false);
        jbDateEnd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel5.add(jbDateEnd);

        jPanel3.add(jPanel5);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros del reporte:"));
        jPanel6.setLayout(new java.awt.GridLayout(4, 1));

        jPanel7.setLayout(new java.awt.GridLayout(1, 1, 0, 1));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCompanyBranch.setText("Sucursal de la empresa:");
        jlCompanyBranch.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel8.add(jlCompanyBranch);

        jcbCompanyBranch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCompanyBranch.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel8.add(jcbCompanyBranch);

        jPanel7.add(jPanel8);

        jPanel6.add(jPanel7);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner1.setText("Área funcional:");
        jlBizPartner1.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel14.add(jlBizPartner1);

        jtfFunctionalArea.setEditable(false);
        jtfFunctionalArea.setPreferredSize(new java.awt.Dimension(195, 23));
        jPanel14.add(jtfFunctionalArea);

        jbFunctionalArea.setText("...");
        jbFunctionalArea.setToolTipText("Seleccionar asociado de negocios:");
        jbFunctionalArea.setFocusable(false);
        jbFunctionalArea.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel14.add(jbFunctionalArea);

        jPanel6.add(jPanel14);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        jLabel1.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jLabel1);

        jckWithoutRelatedParty.setText("Sin partes relacionadas");
        jPanel12.add(jckWithoutRelatedParty);

        jPanel6.add(jPanel12);

        jPanel2.add(jPanel6, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldDateInitial = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateInitial, jlDateInitial);
        moFieldDateInitial.setPickerButton(jbDateInitial);
        moFieldDateEnd = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateEnd, jlDateEnd);
        moFieldDateEnd.setPickerButton(jbDateEnd);
        moFieldCompanyBranch = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbCompanyBranch, jlCompanyBranch);

        mvFields.add(moFieldDateInitial);
        mvFields.add(moFieldDateEnd);
        mvFields.add(moFieldCompanyBranch);

        jbPrint.addActionListener(this);
        jbExit.addActionListener(this);
        jbDateInitial.addActionListener(this);
        jbDateEnd.addActionListener(this);
        jbFunctionalArea.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPrint(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionClose(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "exit", KeyEvent.VK_ESCAPE, 0);

        setModalityType(ModalityType.MODELESS);
        
        // áreas funcionales:
        mnFunctionalAreaId = SLibConstants.UNDEFINED;
        manDataFilter = new int[] { miClient.getSession().getUser().getPkUserId() };
        moDialogFilterFunctionalArea = new SDialogFilterFunctionalArea((SClientInterface) miClient, manDataFilter);
        renderFunctionalArea();
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            if (mbParamIsSupplier) {
                setTitle("Reporte de diario de compras");
            }
            else {
                setTitle("Reporte de diario de ventas");
            }
            jftDateInitial.requestFocus();
        }
    }

    private void actionPrint() {
        Cursor cursor = getCursor();
        SFormValidation validation = formValidate();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                map = miClient.createReportParams();
                map.put("tDtInitial", moFieldDateInitial.getDate());
                map.put("tDtEnd", moFieldDateEnd.getDate());
                map.put("sCompanyBranch", moFieldCompanyBranch.getKeyAsIntArray()[0] == 0 ? "(TODAS)" : jcbCompanyBranch.getSelectedItem().toString());
                map.put("sTitle", mbParamIsSupplier ? "COMPRAS" : "VENTAS");
                map.put("nFidCtDps", mbParamIsSupplier ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0]);
                map.put("nFidClDps", mbParamIsSupplier ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1]);
                map.put("nFidCtRef", mbParamIsSupplier ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
                map.put("nFidCtDpsAdj", mbParamIsSupplier ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0]);
                map.put("nFidClDpsAdj", mbParamIsSupplier ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1]);
                map.put("nFidTpDpsAdj", mbParamIsSupplier ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2]);
                map.put("nFidStDps", SDataConstantsSys.TRNS_ST_DPS_NEW);
                map.put("sTpRec", mbParamIsSupplier ? SDataConstantsSys.FINU_TP_REC_PUR : SDataConstantsSys.FINU_TP_REC_SAL);
                map.put("sSqlWhereCompanyBranch", moFieldCompanyBranch.getKeyAsIntArray()[0] == 0 ? "" : " AND d.fid_cob = " + moFieldCompanyBranch.getKeyAsIntArray()[0]);
                map.put("sSqlWhereWithoutRelatedParty", jckWithoutRelatedParty.isSelected() ? " AND b.b_att_rel_pty = 0 " : "");
                map.put("bIsPurchases", mbParamIsSupplier);
                map.put("nFidTpAccMov", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR[0] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL[0]);
                map.put("nFidClAccMov", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR[1] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL[1]);
                map.put("nFidClsAccMov", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR[2] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL[2]);
                map.put("nFidTpAccMovAdjRet", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR_ADJ_DISC[0] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL_ADJ_DISC[0]);
                map.put("nFidClAccMovAdjRet", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR_ADJ_DISC[1] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL_ADJ_DISC[1]);
                map.put("nFidClsAccMovAdjRet", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR_ADJ_DISC[2] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL_ADJ_DISC[2]);
                map.put("nFidTpAccMovAdjDis", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR_ADJ_DISC[0] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL_ADJ_DISC[0]);
                map.put("nFidClAccMovAdjDis", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR_ADJ_DISC[1] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL_ADJ_DISC[1]);
                map.put("nFidClsAccMovAdjDis", mbParamIsSupplier ? SDataConstantsSys.FINS_CLS_ACC_MOV_PUR_ADJ_DISC[2] : SDataConstantsSys.FINS_CLS_ACC_MOV_SAL_ADJ_DISC[2]);
                map.put("sFuncText", jtfFunctionalArea.getText());
                map.put("sFilterFunctionalArea", " AND d.fid_func IN ( " + msFunctionalAreasIds + " ) ");
                map.put("sCurrencyErp", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                map.put("nFidAccountSystemCategoryId", mbParamIsSupplier ? SDataConstantsSys.FINS_CT_SYS_MOV_PUR : SDataConstantsSys.FINS_CT_SYS_MOV_SAL);
                map.put("sMark", mbParamIsSupplier ? "" : SDataConstantsSys.TXT_UNSIGNED);

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_PS_DIARY, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Reporte de diario " + (mbParamIsSupplier ? "de compras" : "de ventas"));
                jasperViewer.setVisible(true);
            }
            catch(Exception e) {
                SLibUtilities.renderException(this, e);
            }
            finally {
                setCursor(cursor);
            }
        }
    }

    private void actionClose() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionDateInitial() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateInitial.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateInitial.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateInitial.requestFocus();
        }
    }

    private void actionDateEnd() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateEnd.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateEnd.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateEnd.requestFocus();
        }
    }
    
    private void actionFunctionalArea() {
        moDialogFilterFunctionalArea.formRefreshCatalogues();
        moDialogFilterFunctionalArea.formReset();
        moDialogFilterFunctionalArea.setFunctionalAreaId(mnFunctionalAreaId);
        moDialogFilterFunctionalArea.setFormVisible(true);

        if (moDialogFilterFunctionalArea.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
            mnFunctionalAreaId = moDialogFilterFunctionalArea.getFunctionalAreaId();
            renderFunctionalArea();
        }
    }
    
    private void renderFunctionalArea() {
        String texts[] = STrnFunctionalAreaUtils.getTextFilterOfFunctionalAreas((SClientInterface) miClient, mnFunctionalAreaId);
        msFunctionalAreasIds = texts[0];
        
        jtfFunctionalArea.setText(texts[1]);
        jtfFunctionalArea.setCaretPosition(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbDateEnd;
    private javax.swing.JButton jbDateInitial;
    private javax.swing.JButton jbExit;
    private javax.swing.JButton jbFunctionalArea;
    private javax.swing.JButton jbPrint;
    private javax.swing.JComboBox<SFormComponentItem> jcbCompanyBranch;
    private javax.swing.JCheckBox jckWithoutRelatedParty;
    private javax.swing.JFormattedTextField jftDateEnd;
    private javax.swing.JFormattedTextField jftDateInitial;
    private javax.swing.JLabel jlBizPartner1;
    private javax.swing.JLabel jlCompanyBranch;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateInitial;
    private javax.swing.JTextField jtfFunctionalArea;
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

        moFieldDateInitial.setFieldValue(SLibTimeUtilities.getBeginOfMonth(miClient.getSessionXXX().getWorkingDate()));
        moFieldDateEnd.setFieldValue(SLibTimeUtilities.getEndOfMonth(miClient.getSessionXXX().getWorkingDate()));
        
        jckWithoutRelatedParty.setSelected(false);
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbCompanyBranch, SDataConstants.BPSU_BPB, new int[] { miClient.getSessionXXX().getCurrentCompany().getPkCompanyId() });
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

        if (!validation.getIsError()) {
            if (moFieldDateEnd.getDate().compareTo(moFieldDateInitial.getDate()) < 0) {
                validation.setMessage("La fecha final debe ser mayor o igual a la fecha inicial.");
                validation.setComponent(jftDateEnd);
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

            if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbExit) {
                actionClose();
            }
            else if (button == jbDateInitial) {
                actionDateInitial();
            }
            else if (button == jbDateEnd) {
                actionDateEnd();
            }
            else if (button == jbFunctionalArea) {
                actionFunctionalArea();
            }
        }
    }

    public void setParamIsSupplier(boolean b) { mbParamIsSupplier = b; }
}
