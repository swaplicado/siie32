/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepAccount.java
 *
 * Created on 29/06/2010, 05:02:26 PM
 */

package erp.mfin.form;

import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.account.SAccountConsts;
import erp.gui.account.SAccountUtils;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.mcfg.data.SDataParamsCompany;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.gui.SGuiClient;

/**
 *
 * @author Sergio Flores
 */
public class SDialogRepAccount extends javax.swing.JDialog {

    private erp.client.SClientInterface miClient;
    private erp.lib.form.SFormField moFieldDate;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;

    private String msCodeAccMin;
    private String msCodeAccMax;

    /** Creates new form SDialogRepAccount */
    public SDialogRepAccount(erp.client.SClientInterface client) {
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

        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jlLevel = new javax.swing.JLabel();
        jsLevel = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jpAccountBegin = new javax.swing.JPanel();
        moAccountBegPanel = new erp.gui.account.SBeanPanelAccount();
        jpAccountEnd = new javax.swing.JPanel();
        moAccountEndPanel = new erp.gui.account.SBeanPanelAccount();
        jPanel1 = new javax.swing.JPanel();
        jpPrint = new javax.swing.JButton();
        jpClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Impresión de cuentas contables");
        setResizable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel6.setLayout(new java.awt.GridLayout(2, 1, 0, 1));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlDate.setText("Fecha de corte:");
        jlDate.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(jlDate);

        jftDate.setText("dd/mm/yyyy");
        jftDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jftDate);

        jbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDate.setToolTipText("Seleccionar fecha");
        jbDate.setFocusable(false);
        jbDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jbDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDateActionPerformed(evt);
            }
        });
        jPanel3.add(jbDate);

        jPanel6.add(jPanel3);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlLevel.setText("Nivel de profundidad:");
        jlLevel.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jlLevel);

        jsLevel.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jsLevel);

        jPanel6.add(jPanel7);

        jPanel5.add(jPanel6, java.awt.BorderLayout.NORTH);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        jpAccountBegin.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuenta contable inicial:"));
        jpAccountBegin.setLayout(new java.awt.BorderLayout());
        jpAccountBegin.add(moAccountBegPanel, java.awt.BorderLayout.CENTER);

        jPanel4.add(jpAccountBegin);

        jpAccountEnd.setBorder(javax.swing.BorderFactory.createTitledBorder("Cuenta contable final:"));
        jpAccountEnd.setLayout(new java.awt.BorderLayout());
        jpAccountEnd.add(moAccountEndPanel, java.awt.BorderLayout.CENTER);

        jPanel4.add(jpAccountEnd);

        jPanel5.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel2.add(jPanel5, java.awt.BorderLayout.NORTH);

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

        setSize(new java.awt.Dimension(600, 400));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jpPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpPrintActionPerformed
        actionPrint();
    }//GEN-LAST:event_jpPrintActionPerformed

    private void jpCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpCloseActionPerformed
        actionClose();
    }//GEN-LAST:event_jpCloseActionPerformed

    private void jbDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDateActionPerformed
        miClient.getGuiDatePickerXXX().pickDate(moFieldDate.getDate(), moFieldDate);
    }//GEN-LAST:event_jbDateActionPerformed

    private void initComponentsExtra() {
        moFieldDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDate, jlDate);
        moFieldDate.setPickerButton(jbDate);

        moFieldDate.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moAccountBegPanel.setPanelSettings((SGuiClient) miClient, SAccountConsts.TYPE_ACCOUNT, false, true, false);
        moAccountEndPanel.setPanelSettings((SGuiClient) miClient, SAccountConsts.TYPE_ACCOUNT, false, true, false);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldDate);

        jsLevel.setModel(new SpinnerNumberModel(1, 1, miClient.getSessionXXX().getParamsErp().getDeepAccounts(), 1));

        moAccountBegPanel.initPanel();
        moAccountEndPanel.initPanel();

        setModalityType(ModalityType.MODELESS);
        SFormUtilities.createActionMap(rootPane, this, "actionPrint", "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionClose", "close", KeyEvent.VK_ESCAPE, 0);
    }

    private void print() {
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        try {
            miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));

            map = miClient.createReportParams();
            map.put("tDate", moFieldDate.getDate());
            map.put("nLevel", (Integer) jsLevel.getValue());
            map.put("nDeep", miClient.getSessionXXX().getParamsErp().getDeepAccounts());
            map.put("sCodeMin", msCodeAccMin);
            map.put("sCodeMax", msCodeAccMax);
            map.put("sAccountIdMin", SAccountUtils.convertCodeUsr(((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount(), msCodeAccMin));
            map.put("sAccountIdMax", SAccountUtils.convertCodeUsr(((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount(), msCodeAccMax));

            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_ACC, map);
            jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle(getTitle());
            jasperViewer.setVisible(true);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            miClient.getFrame().setCursor(cursor);
        }
    }

    public void actionPrint() {
        boolean error = false;
        String msg = "";
        JComponent component = null;

        msCodeAccMin = moAccountBegPanel.getSelectedAccount() == null ? SAccountUtils.composeCodeStdMin() : moAccountBegPanel.getSelectedAccount().getCodeStd();
        msCodeAccMax = moAccountEndPanel.getSelectedAccount() == null ? SAccountUtils.composeCodeStdMax(((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount()) : moAccountEndPanel.getSelectedAccount().getCodeStd();

        for (SFormField field : mvFields) {
            if (!field.validateField()) {
                error = true;
                component = field.getComponent();
            }
        }

        if (error) {
            if (component != null) {
                component.requestFocus();
            }
        }
        else {
            if (msCodeAccMin.compareTo(msCodeAccMax) > 0) {
                msg = "El valor para el campo '" + ((TitledBorder) jpAccountBegin.getBorder()).getTitle() + "' " +
                        "no puede ser mayor al del campo '" + ((TitledBorder) jpAccountEnd.getBorder()).getTitle() + "'.";
            }
            if (msg.length() > 0) {
                miClient.showMsgBoxWarning(msg);
            }
            else {
                print();
            }
        }
    }

    public void actionClose() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton jbDate;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlLevel;
    private javax.swing.JPanel jpAccountBegin;
    private javax.swing.JPanel jpAccountEnd;
    private javax.swing.JButton jpClose;
    private javax.swing.JButton jpPrint;
    private javax.swing.JSpinner jsLevel;
    private erp.gui.account.SBeanPanelAccount moAccountBegPanel;
    private erp.gui.account.SBeanPanelAccount moAccountEndPanel;
    // End of variables declaration//GEN-END:variables

}
