/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.form;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Isabel Servin
 */
public class SDialogChoseAccountingCustomReport extends SBeanFormDialog implements ActionListener {

    public static final int PARAM_REP_ID = 1;
    public static final int PARAM_REP_NAME = 2;
    
    private final SGuiClient miClient;
    private int mnFormResult;
    
    /**
     * Creates new form SDialogChoseAccountingCustomReport
     * @param client
     */
    public SDialogChoseAccountingCustomReport(SGuiClient client) {
        miClient = client;
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlAccCusRep = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        moKeyAccCusRep = new sa.lib.gui.bean.SBeanFieldKey();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Consulta personalizada de aux. contables");
        setResizable(false);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel4.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel4.add(jbCancel);

        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros de la consulta:"));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(2, 0));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlAccCusRep.setText("Seleccionar opción:");
        jlAccCusRep.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel7.add(jlAccCusRep);

        jPanel6.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moKeyAccCusRep.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel8.add(moKeyAccCusRep);

        jPanel6.add(jPanel8);

        jPanel5.add(jPanel6, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JLabel jlAccCusRep;
    private sa.lib.gui.bean.SBeanFieldKey moKeyAccCusRep;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 400, 250);
        moKeyAccCusRep.setKeySettings(miClient, SGuiUtils.getLabelName(jlAccCusRep), true);
        
        SGuiParams params = new SGuiParams(miClient.getSession().getUser().getPkUserId());
        miClient.getSession().populateCatalogue(moKeyAccCusRep, SModConsts.FIN_REP_CUS_ACC, SLibConsts.UNDEFINED, params);
    
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);
    }
    
    @Override
    public int getFormResult(){
        return mnFormResult;
    }
    
    @Override
    public Object getValue(int param) {
        Object value = null;
        
        switch(param) {
            case PARAM_REP_ID: value = moKeyAccCusRep.getValue()[0]; break;
            case PARAM_REP_NAME: value = moKeyAccCusRep.getSelectedItem().getItem(); break;
        }
        
        return value;
    }
    
    private void actionOk() {
        if (moKeyAccCusRep.getSelectedIndex() >= 1) {
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            this.setVisible(false);
        }
        else {
            miClient.showMsgBoxInformation("Debe seleccionar una opción para mostrar.");
            moKeyAccCusRep.requestFocus();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
                this.setVisible(false);
            }
        }
    }

    @Override
    public void addAllListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reloadCatalogues() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiValidation validateForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}