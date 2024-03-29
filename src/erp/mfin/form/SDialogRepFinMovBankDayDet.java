/*
 * DFormCompany.java
 *
 * Created on 19 de agosto de 2008, 11:10 AM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mfin.data.SDataAccountCash;
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
 * @author  Néstor Ávalos
 */
public class SDialogRepFinMovBankDayDet extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldFkCobId;
    private erp.lib.form.SFormField moFieldFkEntityId;
    private erp.lib.form.SFormField moFieldDateCut;

    private int mnParamReportTypePerDay;
    private int[] mnParamTpSysMov;

    /** Creates new form DFormCompany */
    public SDialogRepFinMovBankDayDet(erp.client.SClientInterface client, java.lang.Object oType) {
        super(client.getFrame(), true);
        miClient = client;

        initComponents();
        initComponentsExtra();

        formRefreshCatalogues();
        formReset();
        setValue(1, oType);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup2 = new javax.swing.ButtonGroup();
        jpData = new javax.swing.JPanel();
        jpReg = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlDateCut = new javax.swing.JLabel();
        jtfDateCut = new javax.swing.JFormattedTextField();
        jbDateCut = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jlFkCobId = new javax.swing.JLabel();
        jcbFkCobId = new javax.swing.JComboBox();
        jbFkCobId = new javax.swing.JButton();
        jPanel98 = new javax.swing.JPanel();
        jlFkEntityId = new javax.swing.JLabel();
        jcbFkEntityId = new javax.swing.JComboBox();
        jbFkEntityId = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jlRepType = new javax.swing.JLabel();
        jrbRepDet = new javax.swing.JRadioButton();
        jrbRepCon = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jbPrint = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Movimientos de "); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpData.setLayout(new java.awt.BorderLayout());

        jpReg.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del reporte:"));
        jpReg.setPreferredSize(new java.awt.Dimension(498, 265));
        jpReg.setRequestFocusEnabled(false);
        jpReg.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Período:"));
        jPanel8.setPreferredSize(new java.awt.Dimension(243, 56));
        jPanel8.setRequestFocusEnabled(false);
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlDateCut.setText("Fecha de corte: *");
        jlDateCut.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel4.add(jlDateCut);

        jtfDateCut.setText("dd/mm/yyyy");
        jtfDateCut.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jtfDateCut);

        jbDateCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateCut.setToolTipText("Seleccionar fecha de corte");
        jbDateCut.setFocusable(false);
        jbDateCut.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbDateCut);

        jPanel8.add(jPanel4, java.awt.BorderLayout.CENTER);

        jpReg.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtro:"));
        jPanel7.setLayout(new java.awt.GridLayout(5, 0, 5, 0));

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlFkCobId.setText("Sucursal de la empresa:");
        jlFkCobId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel15.add(jlFkCobId);

        jcbFkCobId.setPreferredSize(new java.awt.Dimension(315, 23));
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

        jPanel7.add(jPanel15);

        jPanel98.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlFkEntityId.setText("[Asoc. de neg.:]");
        jlFkEntityId.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel98.add(jlFkEntityId);

        jcbFkEntityId.setPreferredSize(new java.awt.Dimension(315, 23));
        jPanel98.add(jcbFkEntityId);

        jbFkEntityId.setText("...");
        jbFkEntityId.setToolTipText("Seleccionar proveedor");
        jbFkEntityId.setFocusable(false);
        jbFkEntityId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel98.add(jbFkEntityId);

        jPanel7.add(jPanel98);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlRepType.setText("Tipo:");
        jlRepType.setPreferredSize(new java.awt.Dimension(120, 23));
        jPanel3.add(jlRepType);

        buttonGroup2.add(jrbRepDet);
        jrbRepDet.setSelected(true);
        jrbRepDet.setText("Detalle");
        jPanel3.add(jrbRepDet);

        buttonGroup2.add(jrbRepCon);
        jrbRepCon.setText("Concentrado");
        jPanel3.add(jrbRepCon);

        jPanel7.add(jPanel3);

        jpReg.add(jPanel7, java.awt.BorderLayout.CENTER);

        jpData.add(jpReg, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jpData, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbPrint.setText("Imprimir");
        jbPrint.setToolTipText("[Ctrl + Enter]");
        jbPrint.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbPrint);

        jbCancel.setText("Cerrar"); // NOI18N
        jbCancel.setToolTipText("[Escape]");
        jbCancel.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(508, 334));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jcbFkCobIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbFkCobIdItemStateChanged
        itemStateChangedFkCobId();
    }//GEN-LAST:event_jcbFkCobIdItemStateChanged

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldFkEntityId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkEntityId, jlFkEntityId);
        moFieldFkEntityId.setPickerButton(jbFkEntityId);
        moFieldFkCobId = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbFkCobId, jlFkCobId);
        moFieldFkCobId.setPickerButton(jbFkCobId);
        moFieldDateCut = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jtfDateCut, jlDateCut);
        moFieldDateCut.setPickerButton(jbDateCut);

        mvFields.add(moFieldFkEntityId);
        mvFields.add(moFieldFkCobId);
        mvFields.add(moFieldDateCut);

        jbCancel.addActionListener(this);
        jbDateCut.addActionListener(this);
        jbFkEntityId.addActionListener(this);
        jbFkCobId.addActionListener(this);
        jbPrint.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionRenderReport(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);

        setModalityType(ModalityType.MODELESS);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jtfDateCut.requestFocus();
        }
    }

    @SuppressWarnings("unchecked")
    private void itemStateChangedFkCobId() {
        if (jcbFkCobId.getSelectedIndex() > 0) {
            if (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH)) {
                SFormUtilities.populateComboBox(miClient, jcbFkEntityId, SDataConstants.FINX_ACC_CASH_CASH, new int[] { moFieldFkCobId.getKeyAsIntArray()[0] });
            }
            else if (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK)) {
                SFormUtilities.populateComboBox(miClient, jcbFkEntityId, SDataConstants.FINX_ACC_CASH_BANK, new int[] { moFieldFkCobId.getKeyAsIntArray()[0] });
            }
        }
    }

    private void actionFkEntityId() {
        if (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH)) {
            miClient.pickOption(SDataConstants.CFGU_COB_ENT, moFieldFkEntityId, new int[]{moFieldFkCobId.getKeyAsIntArray()[0], SDataConstantsSys.CFGS_CT_ENT_CASH, SDataConstantsSys.CFGS_TP_ENT_CASH_CASH[1] });
        }
        else if (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK)) {
            miClient.pickOption(SDataConstants.CFGU_COB_ENT, moFieldFkEntityId, new int[]{moFieldFkCobId.getKeyAsIntArray()[0], SDataConstantsSys.CFGS_CT_ENT_CASH, SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[1] });
        }
    }

    private void actionFkCobId() {
        miClient.pickOption(SDataConstants.BPSU_BPB, moFieldFkCobId, new int[] { miClient.getSessionXXX().getCompany().getPkCompanyId() });
    }

    private void actionDateCut() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateCut.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateCut.setDate(miClient.getGuiDatePickerXXX().getGuiDate());
            jtfDateCut.requestFocus();
        }
    }

    private void actionRenderReport() {
        int nFkCurId = 0;
        String sSqlCob = "";
        String sSqlEntity = "";
        String sCob = "";
        String sEntity = "";
        String sCurrency = "";
        String sReportTitle = "MOVIMIENTOS DE ";

        SDataAccountCash oAccCash = null;

        SFormValidation validation = new SFormValidation();

        validation = formValidate();

        if (!validation.getIsError()) {

            // By company branch (cob) or to all companies branches:

            if (jcbFkCobId.getSelectedIndex() > 0) {

                if (jrbRepDet.isSelected()) {
                    sSqlCob = " AND r.fid_cob = " + moFieldFkCobId.getKeyAsIntArray()[0] + " ";
                } else {
                    sSqlCob = " AND id_cob = " + moFieldFkCobId.getKeyAsIntArray()[0] + " ";
                }
                sCob = jcbFkCobId.getSelectedItem().toString();
            }
            else {
                sCob = "(TODAS)";
            }

            // By account cash/bank or to all account cash/bank:

            if (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH)) {
                sReportTitle += "CAJAS POR DÍA";
            }
            else if (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK)) {
                sReportTitle += "BANCOS POR DÍA";
            }

            // Check if is for entity or all entities:

            if (jcbFkEntityId.getSelectedIndex() > 0) {

                // Check if report is detail or concentrate:

                if (jrbRepDet.isSelected()) {
                    sSqlEntity = "AND re.fid_cob_n = " + moFieldFkEntityId.getKeyAsIntArray()[0] + " AND re.fid_ent_n = " + moFieldFkEntityId.getKeyAsIntArray()[1] + " ";
                }
                else {
                    sSqlEntity = "id_cob = " + moFieldFkEntityId.getKeyAsIntArray()[0] + " id_ent = " + moFieldFkEntityId.getKeyAsIntArray()[1] + " ";
                }

                // Get currency of entity:

                oAccCash = (SDataAccountCash) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC_CASH, moFieldFkEntityId.getKey(), SLibConstants.EXEC_MODE_VERBOSE);
                sEntity += oAccCash.getAuxEntity();
                sCurrency = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.CFGU_CUR, new int[] { oAccCash.getFkCurrencyId() }, SLibConstants.DESCRIPTION_NAME);
                nFkCurId = oAccCash.getFkCurrencyId();
            }
            else {
                sEntity += "(TODAS)";
                sCurrency += "(TODAS)";
            }

            printReport(sReportTitle, sSqlCob, sSqlEntity,  sCob, sEntity, sCurrency, nFkCurId);
        }
        else {
            miClient.showMsgBoxWarning(validation.getMessage());
            validation.getComponent().requestFocus();
        }
    }

    @SuppressWarnings("unchecked")
    private void printReport(java.lang.String sReportTitle, java.lang.String sSqlCob, java.lang.String sSqlEntity, java.lang.String sCob,
            java.lang.String sEntity, java.lang.String sCurrency, int nFkCurId) {
        Cursor oCursor = getCursor();
        JasperPrint oPrint = null;
        JasperViewer oViewer = null;
        Map<String, Object> oMap = null;

        try {

            // Report params:

            oMap = miClient.createReportParams();
            oMap.put("sReportTitle", sReportTitle + (jrbRepCon.isSelected() ? " CONCENTRADO DIARIO" : ""));
            oMap.put("tDateBalOp", SLibTimeUtilities.addDate(moFieldDateCut.getDate(), 0, 0, -1));
            oMap.put("tDateCut", moFieldDateCut.getDate());
            oMap.put("sCurrency", (jrbRepDet.isSelected() ? sCurrency : miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getCurrency()));
            oMap.put("sCob", sCob);
            oMap.put("sSqlEntity", sSqlEntity);
            oMap.put("sSqlCob", sSqlCob);
            oMap.put("nFkCtSysMovId", mnParamTpSysMov[0]);
            oMap.put("nFkTpSysMovId", mnParamTpSysMov[1]);
            oMap.put("nFkYearId", SLibTimeUtilities.digestYear(moFieldDateCut.getDate())[0]);

            // Params for detail/concentrate report:

            if (!jrbRepDet.isSelected()) {
                oMap.put("nFkCtEntId", (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH) ? SDataConstantsSys.CFGS_TP_ENT_CASH_CASH[0] : SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[0]));
                oMap.put("nFkTpEntId", (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH) ? SDataConstantsSys.CFGS_TP_ENT_CASH_CASH[1] : SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[1]));
                oMap.put("sEntTitle", SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH) ? "CAJA" : "BANCO");
            }
            else {
                oMap.put("sKeyCurLoc", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                oMap.put("sEntity", sEntity);
                oMap.put("sEntTitle", SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH) ? "CAJA: " : "CUENTA BANCARIA: ");
                oMap.put("nFkCobId", moFieldFkEntityId.getKeyAsIntArray()[0]);
                oMap.put("nFkEntId", moFieldFkEntityId.getKeyAsIntArray()[1]);
                oMap.put("nFkCurId", nFkCurId);
                oMap.put("nNumRecordLength", SDataConstantsSys.NUM_LEN_FIN_REC);
                oMap.put("nErpCurId", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId());
            }

            oPrint = SDataUtilities.fillReport(miClient, jrbRepDet.isSelected() ? SDataConstantsSys.REP_FIN_CASH_MOV_DET : SDataConstantsSys.REP_FIN_CASH_MOV_CON, oMap);
            oViewer = new JasperViewer(oPrint, false);
            oViewer.setTitle("Reporte de " + this.getTitle().toLowerCase() + (jrbRepDet.isSelected() ? "" : " concentrado diario"));
            oViewer.setVisible(true);
        }
        catch(Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            setCursor(oCursor);
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDateCut;
    private javax.swing.JButton jbFkCobId;
    private javax.swing.JButton jbFkEntityId;
    private javax.swing.JButton jbPrint;
    private javax.swing.JComboBox jcbFkCobId;
    private javax.swing.JComboBox jcbFkEntityId;
    private javax.swing.JLabel jlDateCut;
    private javax.swing.JLabel jlFkCobId;
    private javax.swing.JLabel jlFkEntityId;
    private javax.swing.JLabel jlRepType;
    private javax.swing.JPanel jpData;
    private javax.swing.JPanel jpReg;
    private javax.swing.JRadioButton jrbRepCon;
    private javax.swing.JRadioButton jrbRepDet;
    private javax.swing.JFormattedTextField jtfDateCut;
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

        moFieldDateCut.setDate(miClient.getSessionXXX().getWorkingDate());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbFkCobId, SDataConstants.BPSU_BPB, new int[] { miClient.getSessionXXX().getCompany().getPkCompanyId() });
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

    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {

        return null;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
            switch (type) {
            case 1:
                mnParamTpSysMov = (int[]) ((Object[]) value)[0];
                mnParamReportTypePerDay = (Integer)((Object []) value)[1];

                if (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH)) {
                    jlFkEntityId.setText("Caja:");
                    this.setTitle(this.getTitle() + "cajas ");
                    jbFkEntityId.setToolTipText("Seleccionar caja");
                }
                else if (SLibUtilities.compareKeys(mnParamTpSysMov, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK)) {
                    jlFkEntityId.setText("Cuenta bancaria:");
                    this.setTitle(this.getTitle() + "cuentas bancarias ");
                    jbFkEntityId.setToolTipText("Seleccionar cuenta bancaria");
                }

                if (mnParamReportTypePerDay != SDataConstants.UNDEFINED) {
                    this.setTitle(this.getTitle() + "por día");
                }

                if (miClient.getSessionXXX().getCurrentCompanyBranch() != null) {
                    moFieldFkCobId.setKey(miClient.getSessionXXX().getCurrentCompanyBranch().getPrimaryKey());
                }

                if (miClient.getSessionXXX().getCurrentCompanyBranchEntityKey(SDataConstantsSys.CFGS_CT_ENT_CASH) != null) {
                    moFieldFkEntityId.setKey(miClient.getSessionXXX().getCurrentCompanyBranchEntityKey(SDataConstantsSys.CFGS_CT_ENT_CASH));
                }

                break;
        }
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

            if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbDateCut) {
                actionDateCut();
            }
            else if (button == jbPrint) {
                if (jrbRepDet.isSelected()) {
                    actionRenderReport();
                }
                else {
                    actionRenderReport();
                    //actionRenderReportCon();
                }
            }
            else if (button == jbFkEntityId) {
                actionFkEntityId();
            }
            else if (button == jbFkCobId) {
                actionFkCobId();
            }
        }
    }
}
