/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepConsumptionBudget.java
 *
 * Created on 02/08/2012, 11:09:39 AM
 */

package erp.mtrn.form;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author Isabel Servín
 */
public class SDialogRepConsumptionBudget extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    
    private Date moDateIni;
    private Date moDateEnd;
    private erp.lib.form.SFormField moFieldPkPeriodId;
    
    /** Creates new form SDialogRepConsumptionBudget
     * @param client */
    public SDialogRepConsumptionBudget(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;

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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jbPrint = new javax.swing.JButton();
        jbExit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        jsYear = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        jlPeriod = new javax.swing.JLabel();
        jtfPeriod = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jrbDetail = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        jrbSummary = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reporte de presupuestos vs. gastos");
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

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del reporte:"));
        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 1));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Año: *");
        jlYear.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlYear);

        jsYear.setPreferredSize(new java.awt.Dimension(80, 26));
        jPanel6.add(jsYear);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPeriod.setText("Periodo: *");
        jlPeriod.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlPeriod);

        jtfPeriod.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfPeriod.setText("0000");
        jtfPeriod.setPreferredSize(new java.awt.Dimension(80, 24));
        jPanel7.add(jtfPeriod);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        buttonGroup1.add(jrbDetail);
        jrbDetail.setText("Modalidad a detalle");
        jPanel8.add(jrbDetail);

        jPanel2.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        buttonGroup1.add(jrbSummary);
        jrbSummary.setText("Modalidad resumen");
        jPanel9.add(jrbSummary);

        jPanel2.add(jPanel9);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(436, 226));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        jsYear.setValue(miClient.getSession().getSystemYear());
        moFieldPkPeriodId = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfPeriod, jlPeriod);
        moFieldPkPeriodId.setInteger(miClient.getSession().getSystemYear());
        
        jrbDetail.setSelected(true);
        
        mvFields.add(moFieldPkPeriodId);

        jbPrint.addActionListener(this);
        jbExit.addActionListener(this);

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
            jsYear.requestFocus();
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
                map.put("sTitle", "REPORTE DE PRESUPUESTOS VS. GASTOS");
                map.put("nAnio", (int)jsYear.getValue());
                map.put("nPeriodo", moFieldPkPeriodId.getInteger());
                map.put("bShowDetail", jrbDetail.isSelected());
                map.put("nTotalPres", getTotalBudget());
                map.put("dFechaIni", moDateIni);
                map.put("dFechaFin", moDateEnd);
                
                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_MAT_CONS_BUDGET, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Reporte de presupuestos vs. gastos");
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

    private double getTotalBudget() {
        double budget = 0.0;
        try {
            String sql = "SELECT SUM(budget) FROM trn_mat_cons_ent_budget "
                    + "WHERE id_year = " + (int)jsYear.getValue() + " AND id_period = " + moFieldPkPeriodId.getInteger();
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                budget = resultSet.getDouble(1);
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
        return budget;
    }
    
    private erp.lib.form.SFormValidation compareDates() {
        SFormValidation validation = new SFormValidation();
        moDateIni = null;
        moDateEnd = null;
        try {
            String sql = "SELECT dt_start, dt_end FROM trn_mat_cons_ent_budget "
                    + "WHERE id_year = " + (int)jsYear.getValue() + " AND id_period = " + moFieldPkPeriodId.getInteger();
            ResultSet resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                if (moDateIni == null && moDateEnd == null) {
                    moDateIni = resultSet.getDate(1);
                    moDateEnd = resultSet.getDate(2);
                }
                else {
                    if (moDateIni.compareTo(resultSet.getDate(1)) != 0 || moDateEnd.compareTo(resultSet.getDate(2)) != 0) {
                        validation.setIsError(true);
                        validation.setMessage("No se puede generar el reporte debido a que la fecha inicial y final de los presupuestos no son iguales entre si.");
                    }
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning(e.getMessage());
        }
        return validation;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbExit;
    private javax.swing.JButton jbPrint;
    private javax.swing.JLabel jlPeriod;
    private javax.swing.JLabel jlYear;
    private javax.swing.JRadioButton jrbDetail;
    private javax.swing.JRadioButton jrbSummary;
    private javax.swing.JSpinner jsYear;
    private javax.swing.JTextField jtfPeriod;
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

        mbResetingForm = false;
        
        jrbDetail.setSelected(true);
        
        moFieldPkPeriodId.setInteger(miClient.getSession().getSystemYear());
        
    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        mbResetingForm = true;
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
            validation = compareDates();
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
        }
    }
}
