/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepAccountConcept.java
 *
 * Created on 30/08/2010, 04:33:25 PM
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
 * @author Alfonso Flores, Sergio Flores
 */
public class SDialogRepAccountConcept extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {
    
    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private int mnConcept;
    private erp.lib.form.SFormField moFieldDateInitial;
    private erp.lib.form.SFormField moFieldDateEnd;
    private erp.lib.form.SFormField moFieldConcept;

    /**
     * Creates new form SDialogRepAccountConcept
     * @param client GUI client.
     * @param concept Concept type desired. Constants defined in <code>SDataConstants</code> (FINU_TP_ADM_CPT or FINU_TP_TAX_CPT).
     */
    public SDialogRepAccountConcept(erp.client.SClientInterface client, int concept) {
        super(client.getFrame(), true);
        miClient = client;
        mnConcept = concept;

        initComponents();
        initComponentsExtra();
        
        formRefreshCatalogues();
        formReset();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        jrbByAccountConcept = new javax.swing.JRadioButton();
        jrbbyConceptAccount = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        jcbConcept = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jbPrint = new javax.swing.JButton();
        jbExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reporte de conceptos administrativos");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

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

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Ordenamiento:"));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        buttonGroup1.add(jrbByAccountConcept);
        jrbByAccountConcept.setText("Por cuenta contable, concepto");
        jPanel7.add(jrbByAccountConcept);

        buttonGroup1.add(jrbbyConceptAccount);
        jrbbyConceptAccount.setText("Por concepto, cuenta contable");
        jPanel7.add(jrbbyConceptAccount);

        jPanel6.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros del reporte:"));
        jPanel8.setPreferredSize(new java.awt.Dimension(100, 53));
        jPanel8.setLayout(new java.awt.GridLayout(1, 1, 0, 1));

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlConcept.setText("Concepto:");
        jlConcept.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlConcept);

        jcbConcept.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbConcept.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel9.add(jcbConcept);

        jPanel8.add(jPanel9);

        jPanel2.add(jPanel8, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

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

        setSize(new java.awt.Dimension(400, 305));
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
        moFieldConcept = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbConcept, jlConcept);

        mvFields.add(moFieldDateInitial);
        mvFields.add(moFieldDateEnd);
        mvFields.add(moFieldConcept);

        jbPrint.addActionListener(this);
        jbExit.addActionListener(this);
        jbDateInitial.addActionListener(this);
        jbDateEnd.addActionListener(this);

        if (mnConcept == SDataConstants.FINU_TP_ADM_CPT) {
            setTitle("Reporte de conceptos administrativos");
        }
        else  {
            setTitle("Reporte de conceptos de impuestos");
        }

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
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jftDateInitial.requestFocus();
            jrbByAccountConcept.setSelected(true);
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
                map.put("sConcept", moFieldConcept.getKeyAsIntArray()[0] == 0 ? "(TODOS)" : jcbConcept.getSelectedItem().toString());
                map.put("sSqlIfConcept", mnConcept == SDataConstants.FINU_TP_ADM_CPT ? " IF(i.fid_tp_adm_cpt <> " + SDataConstantsSys.NA + ", cpi.tp_adm_cpt, cpig.tp_adm_cpt) AS concept, " :
                    " IF(i.fid_tp_tax_cpt <> " + SDataConstantsSys.NA + ", cpi.tp_tax_cpt, cpig.tp_tax_cpt) AS concept, ");
                map.put("sTableConcept", getSqlTextInnerJoin());
                map.put("sSqlConceptItem", moFieldConcept.getKeyAsIntArray()[0] == 0 ? "" : getSqlTextConcept());
                map.put("sSqlGroupBy", getSqlTextGroupOrderBy()[0]);
                map.put("sSqlOrderBy", getSqlTextGroupOrderBy()[1]);
                map.put("nIdYear", SLibTimeUtilities.digestYear(moFieldDateInitial.getDate())[0]);
                map.put("sTitle", "REPORTE DE CONCEPTOS" + (mnConcept == SDataConstants.FINU_TP_ADM_CPT ? " ADMINISTRATIVOS" : " DE IMPUESTOS"));
                map.put("bIsByConcept", jrbByAccountConcept.isSelected() ? false : true);

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_ACC_CPT, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Reporte de conceptos " + (mnConcept == SDataConstants.FINU_TP_ADM_CPT ? "administrativos" : "de impuestos"));
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

    private String getSqlTextInnerJoin() {
        String sQuery = "";

        if (mnConcept == SDataConstants.FINU_TP_ADM_CPT) {
            sQuery = "INNER JOIN erp.finu_tp_adm_cpt AS cpi ON " +
                    "i.fid_tp_adm_cpt = cpi.id_tp_adm_cpt " +
                    "INNER JOIN erp.finu_tp_adm_cpt AS cpig ON " +
                    "ig.fid_tp_adm_cpt = cpig.id_tp_adm_cpt ";
        }
        else {
            sQuery = "INNER JOIN erp.finu_tp_tax_cpt AS cpi ON " +
                    "i.fid_tp_tax_cpt = cpi.id_tp_tax_cpt " +
                    "INNER JOIN erp.finu_tp_tax_cpt AS cpig ON " +
                    "ig.fid_tp_tax_cpt = cpig.id_tp_tax_cpt ";
        }

        return sQuery;
    }

    private String[] getSqlTextGroupOrderBy() {
        String[] sText = new String[2];

        if (jrbByAccountConcept.isSelected()) {
            sText[0] = " GROUP BY re.fid_acc, concept ";
            sText[1] = " ORDER BY re.fid_acc, concept ";
        }
        else {
            sText[0] = " GROUP BY concept, re.fid_acc ";
            sText[1] = " ORDER BY concept, re.fid_acc ";
        }

        return sText;
    }

    private String getSqlTextConcept() {
        String text = "";

        if (mnConcept == SDataConstants.FINU_TP_ADM_CPT) {
            if (moFieldConcept.getKeyAsIntArray()[0] == SDataConstantsSys.NA) {
                text += " AND (i.fid_tp_adm_cpt = " + SDataConstantsSys.NA + " AND ig.fid_tp_adm_cpt = " + SDataConstantsSys.NA + ") ";
            }
            else {
                text += " AND (i.fid_tp_adm_cpt = " + moFieldConcept.getKeyAsIntArray()[0] + " OR ig.fid_tp_adm_cpt = " + moFieldConcept.getKeyAsIntArray()[0] + ") ";
            }
        }
        else {
            if (moFieldConcept.getKeyAsIntArray()[0] == SDataConstantsSys.NA) {
                text += " AND (i.fid_tp_tax_cpt = " + SDataConstantsSys.NA + " AND ig.fid_tp_tax_cpt = " + SDataConstantsSys.NA + ") ";
            }
            else {
                text += " AND (i.fid_tp_tax_cpt = " + moFieldConcept.getKeyAsIntArray()[0] + " OR ig.fid_tp_tax_cpt = " + moFieldConcept.getKeyAsIntArray()[0] + ") ";
            }
        }

        return text;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbDateEnd;
    private javax.swing.JButton jbDateInitial;
    private javax.swing.JButton jbExit;
    private javax.swing.JButton jbPrint;
    private javax.swing.JComboBox jcbConcept;
    private javax.swing.JFormattedTextField jftDateEnd;
    private javax.swing.JFormattedTextField jftDateInitial;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateInitial;
    private javax.swing.JRadioButton jrbByAccountConcept;
    private javax.swing.JRadioButton jrbbyConceptAccount;
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
    }

    @Override
    public void formRefreshCatalogues() {
        SFormUtilities.populateComboBox(miClient, jcbConcept, mnConcept);
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
            else if (SLibTimeUtilities.digestYear(moFieldDateEnd.getDate())[0] != SLibTimeUtilities.digestYear(moFieldDateInitial.getDate())[0]) {
                validation.setMessage("La fecha inicial y final deben pertenecer al mismo ejercicio.");
                validation.setComponent(jftDateInitial);
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
        }
    }
}
