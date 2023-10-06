/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepExchangeRateDiff.java
 *
 */

package erp.mfin.form;

import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Isabel Servín
 */
public class SDialogRepExchangeRateDiff extends javax.swing.JDialog implements java.awt.event.ActionListener {

    private final erp.client.SClientInterface miClient;
    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateEnd;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    
    /** Creates new form SDialogRepBalanceSheet
     * @param client */
    public SDialogRepExchangeRateDiff(erp.client.SClientInterface client) {
        super(client.getFrame(), false);
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jrbCus = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jrbSup = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbDateStart = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        jftDateEnd = new javax.swing.JFormattedTextField();
        jbDateEnd = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jrbDocSold = new javax.swing.JRadioButton();
        jPanel12 = new javax.swing.JPanel();
        jrbDocRev = new javax.swing.JRadioButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jpPrint = new javax.swing.JButton();
        jpClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Diferencias cambiarias");
        setResizable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        buttonGroup2.add(jrbCus);
        jrbCus.setSelected(true);
        jrbCus.setText("Diferencias cambiarias de clientes");
        jPanel3.add(jrbCus);

        jPanel6.add(jPanel3);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        buttonGroup2.add(jrbSup);
        jrbSup.setText("Diferencias cambiarias de proveedores");
        jPanel7.add(jrbSup);

        jPanel6.add(jPanel7);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlDateStart);

        jftDateStart.setText("dd/mm/yyyy");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jftDateStart);

        jbDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateStart.setToolTipText("Seleccionar fecha");
        jbDateStart.setFocusable(false);
        jbDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jbDateStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDateStartActionPerformed(evt);
            }
        });
        jPanel4.add(jbDateStart);

        jPanel6.add(jPanel4);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final: *");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlDateEnd);

        jftDateEnd.setText("dd/mm/yyyy");
        jftDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jftDateEnd);

        jbDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDateEnd.setToolTipText("Seleccionar fecha");
        jbDateEnd.setFocusable(false);
        jbDateEnd.setPreferredSize(new java.awt.Dimension(23, 23));
        jbDateEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDateEndActionPerformed(evt);
            }
        });
        jPanel10.add(jbDateEnd);

        jPanel6.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        buttonGroup1.add(jrbDocSold);
        jrbDocSold.setSelected(true);
        jrbDocSold.setText("Diferencias cambiarias de cuentas liquidadas (documentos y anticipos)");
        jPanel11.add(jrbDocSold);

        jPanel6.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        buttonGroup1.add(jrbDocRev);
        jrbDocRev.setText("Diferencias cambiarias de cuentas revaluadas (documentos y anticipos)");
        jPanel12.add(jrbDocRev);

        jPanel6.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel6.add(jPanel13);

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

    private void jbDateStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDateStartActionPerformed

    }//GEN-LAST:event_jbDateStartActionPerformed

    private void jbDateEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDateEndActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbDateEndActionPerformed

    @SuppressWarnings("unchecked")
    private void initComponentsExtra() {
        moFieldDateStart = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbDateStart);
        moFieldDateEnd = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateEnd, jlDateEnd);
        moFieldDateEnd.setPickerButton(jbDateEnd);
        
        moFieldDateStart.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        moFieldDateEnd.setFieldValue(miClient.getSessionXXX().getWorkingDate());
        
        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldDateStart);
        mvFields.add(moFieldDateEnd);
        
        jbDateStart.addActionListener(this);
        jbDateEnd.addActionListener(this);
        
        SFormUtilities.createActionMap(rootPane, this, "actionPrint", "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionClose", "close", KeyEvent.VK_ESCAPE, 0);
    }

    private void print() {
        Cursor cursor = getCursor();
        Map<String, Object> map;
        JasperPrint jasperPrint;
        JasperViewer jasperViewer;
        
        String title = "DIFERENCIAS CAMBIARIAS ";
        String compareType;
        String tpSysMov;
        
        if (jrbCus.isSelected()) {
            title += "DE CLIENTES ";
            tpSysMov = "fre.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0] + " AND fre.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1] + " ";
        }
        else {
            title += "DE PROVEEDORES ";
            tpSysMov = "fre.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND fre.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " ";
        }
        
        String period = "fre.ts_new BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(moFieldDateStart.getDate()) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(moFieldDateEnd.getDate()) + "'";
        
        if (jrbDocSold.isSelected()) {
            title += "DE CUENTAS LIQUIDADAS";
            compareType = "=";
        }
        else {
            title += "DE CUENTAS REVALUADAS";
            compareType = "<>";
        }

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            map = miClient.createReportParams();
            map.put("oDateTextFormat", miClient.getSessionXXX().getFormatters().getDateTextFormat());
            map.put("tDateStart", moFieldDateStart.getDate());
            map.put("tDateEnd", moFieldDateEnd.getDate());
            map.put("sTitle", title);
            map.put("sCompareType", compareType);
            map.put("sTpSysMov", tpSysMov);
            map.put("sPeriod", period);
            
            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_EXC_DIFF, map);
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

    private void actionDateStart() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateStart.getDate(), moFieldDateStart);
    }
    
    private void actionDateEnd() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateEnd.getDate(), moFieldDateEnd);
    }

    public void actionPrint() {
        boolean error = false;
        JComponent component = null;

        for (SFormField field : mvFields) {
            if (!field.validateField()) {
                error = true;
                component = field.getComponent();
            }
        }

        
        if (!error) {
            if (moFieldDateStart.getDate().compareTo(moFieldDateEnd.getDate()) > 0) {
                miClient.showMsgBoxWarning("La fecha inicial (" + SLibUtils.DateFormatDate.format(moFieldDateStart.getDate()) + ") "
                    + "debe ser anterior o igual a la fecha final (" + SLibUtils.DateFormatDate.format(moFieldDateEnd.getDate()) + ").");
                error = true;
                component = moFieldDateEnd.getComponent();
            }
        }
        if (!error) {
            if (SLibTimeUtils.digestYear(moFieldDateStart.getDate())[0] != SLibTimeUtils.digestYear(moFieldDateEnd.getDate())[0]) {
                miClient.showMsgBoxWarning("El ejercicio de la fecha inicial y final deben coincidir.");
                error = true;
                component = moFieldDateEnd.getComponent();
            }
        }
        if (error) {
            if (component != null) {
                component.requestFocus();
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton jbDateEnd;
    private javax.swing.JButton jbDateStart;
    private javax.swing.JFormattedTextField jftDateEnd;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JButton jpClose;
    private javax.swing.JButton jpPrint;
    private javax.swing.JRadioButton jrbCus;
    private javax.swing.JRadioButton jrbDocRev;
    private javax.swing.JRadioButton jrbDocSold;
    private javax.swing.JRadioButton jrbSup;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbDateStart) {
                actionDateStart();
            }
            else if (button == jbDateEnd) {
                actionDateEnd();
            }
            
        }
    }
}