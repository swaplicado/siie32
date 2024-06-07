/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mfin.form.SPanelAccount;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Isabel Servín
 */
public class SDialogCostCenter extends javax.swing.JDialog implements ActionListener {
    
    public static final int DESCRIPTION = 1; 

    private final SClientInterface miClient;
    private erp.mfin.form.SPanelAccount moPanelFkCostCenterId_n;
    
    private int mnFormResult;
    
    /**
     * Creates new form SDialogCostCenter
     * @param client
     */
    public SDialogCostCenter(SClientInterface client) {
        miClient = client;
        initComponents();
        initComponentsExtra();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Seleccionar centro de costo");
        setModal(true);
        setResizable(false);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("[Panel centro de costo]");
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setPreferredSize(new java.awt.Dimension(81, 23));
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(416, 139));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsExtra() {
        try {
            moPanelFkCostCenterId_n = new SPanelAccount(miClient, SDataConstants.FIN_CC, false, false, false);
            moPanelFkCostCenterId_n.setLabelsWidth(100);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        moPanelFkCostCenterId_n.resetPanel();
        jPanel1.remove(jLabel1);
        jPanel1.add(moPanelFkCostCenterId_n);
        
        addListeners();
    }
    
    private void addListeners() {
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
    }
    
    private void actionOk() {
        mnFormResult = SLibConstants.FORM_RESULT_OK;
        setVisible(false);
    }
    
    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    // End of variables declaration//GEN-END:variables

    public Object getValue(int value) {
        Object obj = null;
        switch (value) {
            case SDataConstants.FIN_CC:
                obj = moPanelFkCostCenterId_n.getCurrentInputCostCenter();
                break;
            case DESCRIPTION:
                obj = moPanelFkCostCenterId_n.getAccountDescription();
                break;
        }
        return obj;
    }
    
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }
    
    public int getFormResult() {
        return mnFormResult;
    }
    
    public erp.mfin.form.SPanelAccount getPanel() {
        return moPanelFkCostCenterId_n;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
        }
    }
}