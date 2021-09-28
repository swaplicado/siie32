/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import erp.lib.form.SFormComponentItem;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author SW
 */
public class SDialogRepBizPartnerLastMove extends javax.swing.JDialog implements java.awt.event.ActionListener, java.awt.event.ItemListener{

    private erp.client.SClientInterface miClient;
    boolean mbFirstTime;

    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldSalesAgent;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    
    public SDialogRepBizPartnerLastMove(erp.client.SClientInterface client, int idBizPartnerCategory) {
        super(client.getFrame(), true);
        miClient = client;
        initComponents();
        initComponentsExtra();
    }

    private void initComponentsExtra() {
        moFieldDate = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jftDate, jlDate);
        moFieldSalesAgent = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbSalesAgent, jlSalesAgent);
        
        jftDate.setEnabled(false);
        
        mvFields = new Vector<>();
        mvFields.add(moFieldDate);
        mvFields.add(moFieldSalesAgent);

        resetForm();

        SFormUtilities.populateComboBox(miClient, jcbSalesAgent, SDataConstants.BPSX_BP_ATT_SAL_AGT);
        
        setModalityType(ModalityType.MODELESS);
        SFormUtilities.createActionMap(rootPane, this, "actionPrint", "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionClose", "close", KeyEvent.VK_ESCAPE, 0);
    }
    
    private void print() {
        int year = SLibTimeUtilities.digestYear(moFieldDate.getDate())[0];  
        String filterBp = "";
        String filterSalesAgent = "";
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        
        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            filterSalesAgent =  (jcbSalesAgent.getSelectedIndex() <= 0 ? "" : " AND d.fid_sal_agt_n = " + moFieldSalesAgent.getKeyAsIntArray()[0] + " ");
            
            map = miClient.createReportParams();
            map.put("nYear", year);
            map.put("sFilterBizPartner", filterBp);
            map.put("sFilterSalesAgent", filterSalesAgent);
            
            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_FIN_DPS_LAST_MOV, map);
            jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle(getTitle());
            setVisible(false);
            jasperViewer.setVisible(true);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            setCursor(cursor);
        }
    }

    public void actionPrint() {  
        print();
    }

    public void actionClose() {
        setVisible(false);
    }
    
    public void resetForm() {
        mbFirstTime = true;
        moFieldDate.setFieldValue(miClient.getSession().getCurrentDate());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jlDate = new javax.swing.JLabel();
        jftDate = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jpPrint = new javax.swing.JButton();
        jpClose = new javax.swing.JButton();
        jlSalesAgent = new javax.swing.JLabel();
        jcbSalesAgent = new javax.swing.JComboBox<SFormComponentItem>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ultimo movimiento");
        setAlwaysOnTop(true);
        setIconImage(null);
        setIconImages(null);

        jlDate.setText("Fecha");
        jlDate.setPreferredSize(new java.awt.Dimension(100, 23));

        jftDate.setText("dd/mm/yyyy");
        jftDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jftDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jftDateActionPerformed(evt);
            }
        });

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

        jlSalesAgent.setText("Agente ventas:");
        jlSalesAgent.setPreferredSize(new java.awt.Dimension(150, 23));

        jcbSalesAgent.setPreferredSize(new java.awt.Dimension(325, 23));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlSalesAgent, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcbSalesAgent, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jftDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jftDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbSalesAgent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlSalesAgent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jpPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpPrintActionPerformed
        actionPrint();
    }//GEN-LAST:event_jpPrintActionPerformed

    private void jpCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpCloseActionPerformed
        actionClose();
    }//GEN-LAST:event_jpCloseActionPerformed

    private void jftDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jftDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jftDateActionPerformed

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<SFormComponentItem> jcbSalesAgent;
    private javax.swing.JFormattedTextField jftDate;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlSalesAgent;
    private javax.swing.JButton jpClose;
    private javax.swing.JButton jpPrint;
    // End of variables declaration//GEN-END:variables
}
